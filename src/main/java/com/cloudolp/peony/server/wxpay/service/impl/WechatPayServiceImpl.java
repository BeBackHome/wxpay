/**
 * Copyright(c) Cloudolp Technology Co.,Ltd.
 * All Rights Reserved.
 * <p>
 * This software is the confidential and proprietary information of Cloudolp
 * Technology Co.,Ltd. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Cloudolp Technology Co.,Ltd.
 * For more information about Cloudolp, welcome to http://www.cloudolp.com
 * <p>
 * project: wxpay
 * <p>
 * Revision History:
 * Date         Version     Name                Description
 * 2/27/2018  1.0         weber         Creation File
 */
package com.cloudolp.peony.server.wxpay.service.impl;

import com.cloudolp.peony.server.wxpay.constants.Constants;
import com.cloudolp.peony.server.wxpay.entities.WechatPayRecord;
import com.cloudolp.peony.server.wxpay.service.WechatPayRecordService;
import com.cloudolp.peony.server.wxpay.service.WechatPayService;
import com.cloudolp.peony.server.wxpay.service.WxPayConfigService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayBaseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Description:
 *
 * @author weber
 * @date 2/27/2018 11:40 AM
 */
@Service
public class WechatPayServiceImpl implements WechatPayService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private WxPayConfigService wxPayConfigService;

    @Autowired
    private WechatPayRecordService wechatPayRecordService;

    //生成销售单号+三位流水号的字符串
    private static String generateOrderNumber(String orderNo, Integer serial) {
        orderNo = orderNo + "_";
        Integer len = serial.toString().length();
        for (int i = 0; i < 3 - len; i++) {
            orderNo = orderNo + 0;
        }
        return orderNo + serial;
    }

    @Override
    public String createScanPayCodeUrl(String orderNo, BigDecimal totalFee, String ip) throws IOException, WxPayException {
        //将针对这一单产生一个三位流水号
        Integer serialNumber = 0;
        WechatPayRecord wechatPayRecordList = wechatPayRecordService.loadTopByOrderNoPrefix(orderNo);
        if (wechatPayRecordList != null) {
            String number = wechatPayRecordList.getOrderNo();
            String[] a = number.split("_");
            serialNumber = Integer.parseInt(a[1]) + 1;
        }
        String orderNumber = generateOrderNumber(orderNo, serialNumber);
        WxPayService wxPayService = wxPayConfigService.config();
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setOutTradeNo(orderNumber);     //应该储存商户内部订单号，现在直接存储系统的orderNo
        wxPayUnifiedOrderRequest.setBody("shopName");
        wxPayUnifiedOrderRequest.setProductId("shopId");
        wxPayUnifiedOrderRequest.setFeeType("CNY"); //币种
        wxPayUnifiedOrderRequest.setTotalFee(WxPayBaseRequest.yuanToFee(totalFee.toPlainString()));
        wxPayUnifiedOrderRequest.setSpbillCreateIp(ip);
        wxPayUnifiedOrderRequest.setNonceStr(Long.toString(System.currentTimeMillis()));    //生成随机字符串

        wxPayUnifiedOrderRequest.checkAndSign(wxPayService.getConfig());    //检查参数生成签名
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
        try {
            logger.debug("Prepare to unified order: [\n{}\n]", wxPayUnifiedOrderRequest);
            wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
        } catch (WxPayException e) {
            logger.error("Wechat pay failed！salenumber：[{}],reason:[{}]", orderNo, e.getMessage());
            e.printStackTrace();
            return null;
        }
        //生成微信支付订单记录
        WechatPayRecord wechatPayRecord = new WechatPayRecord();
        wechatPayRecord.setMchId(wxPayService.getConfig().getMchId());
        wechatPayRecord.setAppId(wxPayService.getConfig().getAppId());
        wechatPayRecord.setOrderNo(orderNumber);
        wechatPayRecord.setTotalFee(totalFee);
        wechatPayRecord.setStatus(Constants.PayStatus.Pending);
        wechatPayRecord.setCodeUrl(wxPayUnifiedOrderResult.getCodeURL());
        wechatPayRecord.setPrepayId(wxPayUnifiedOrderResult.getPrepayId());
        WechatPayRecord wechatPayRecordResult = wechatPayRecordService.insertOrUpdate(wechatPayRecord);
        logger.debug("Insert wechat record successfully.", wechatPayRecordResult.toString());
        return wxPayUnifiedOrderResult.getCodeURL();
    }

    @Override
    @Transactional
    public WxPayNotifyResponse wechatRevoke(String xmlData) {
        WxPayOrderNotifyResult e = WxPayOrderNotifyResult.fromXML(xmlData);
        WxPayNotifyResponse wxPayNotifyResponse = new WxPayNotifyResponse();
        StringBuffer sql = new StringBuffer("UPDATE `wechat_pay_record` SET STATUS = :status WHERE order_no = :order_no");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("order_no", e.getOutTradeNo());
        if (e.getResultCode().equals("SUCCESS")) {
            wxPayNotifyResponse.setReturnCode("SUCCESS");
            query.setParameter("status", Constants.PayStatus.Success.getValue());
            logger.debug("Update wechat record SUCCESS successfully.");
        } else {
            wxPayNotifyResponse.setReturnCode("FAIL");
            logger.error("WeChat Pay failed! " + wxPayNotifyResponse.getReturnMsg(), wxPayNotifyResponse.getReturnMsg());
            query.setParameter("status", Constants.PayStatus.Failed.getValue());
            logger.debug("Update wechat record FAIL successfully.");
        }
        query.executeUpdate();
        return wxPayNotifyResponse;
    }

    @Override
    public Constants.PayStatus queryOrderStatus(String orderNo) {
        WechatPayRecord wechatPayRecord = wechatPayRecordService.loadTopByOrderNoPrefix(orderNo);
        return wechatPayRecord == null ? null : wechatPayRecord.getStatus();
    }

    @Override
    @Transactional
    public Boolean testUpdate(String orderNo, Constants.PayStatus payStatus) {
        StringBuffer sql = new StringBuffer("UPDATE `wechat_pay_record` SET STATUS = :status WHERE order_no = :order_no");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("order_no", orderNo);
        query.setParameter("status", payStatus.getValue());
        query.executeUpdate();
        return true;
    }
}