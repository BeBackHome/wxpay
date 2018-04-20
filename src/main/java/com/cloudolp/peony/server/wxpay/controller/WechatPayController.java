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
package com.cloudolp.peony.server.wxpay.controller;

import com.cloudolp.peony.server.wxpay.constants.Constants;
import com.cloudolp.peony.server.wxpay.constants.ErrorCode;
import com.cloudolp.peony.server.wxpay.service.WechatPayService;
import com.cloudolp.peony.server.wxpay.support.Response;
import com.cloudolp.peony.server.wxpay.util.IpUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Description:
 *
 * @author weber
 * @date 2/27/2018 11:24 AM
 */
@RestController
@RequestMapping("rest/pay")
public class WechatPayController {
    private Logger logger = LoggerFactory.getLogger(WechatPayController.class);

    @Autowired
    private WechatPayService wechatPayService;

    /*
     * 客户端调用此接口生成微信支付二维码
     * */
    @RequestMapping(value = "/generateScanPayCodeUrl", method = RequestMethod.GET)
    public Response generateScanPayCodeUrl(@RequestParam(name = "orderNo") String orderNo,
                                           @RequestParam(name = "totalFee") BigDecimal totalFee,
                                           HttpServletRequest request) throws IOException, WxPayException {
        Response result = new Response<>();
        String codeUrl = wechatPayService.createScanPayCodeUrl(orderNo, totalFee, IpUtil.parseIpOfRequst(request));
        logger.debug("Generate wechat pay url successfully -- primary key: [{}], result is [\n{}\n]",
                orderNo, codeUrl);
        if (codeUrl == null) {
            result.setErrorCode(String.valueOf(ErrorCode.PAY_FAIL));
            result.setErrorMsg("pay failed");
        } else {
            result.setResult(codeUrl);
        }
        return result;
    }

    /*
     * 微信扫码的回调接口，支付成功后微信会回调这个接口，然后在这个接口里面我们会对当前订单的状态进行改变
     * */
    @RequestMapping(value = "/invoke.html", method = RequestMethod.POST)
    public WxPayNotifyResponse wechatRevoke(@RequestBody String xmlData) throws IOException {
        WxPayNotifyResponse wxPayNotifyResponse = wechatPayService.wechatRevoke(xmlData);
        logger.debug("Revoke wechat pay result url successfully, result is [\n{}\n].", wxPayNotifyResponse);
        return wxPayNotifyResponse;
    }

    /*
     * 供客户端轮询调用此接口查询是否支付成功
     * */
    @RequestMapping(value = "/getPayStatus", method = RequestMethod.GET)
    public Response queryPayStatus(@RequestParam(name = "orderNo") String orderNo) throws IOException {
        Response result = new Response<>();
        Constants.PayStatus status = wechatPayService.queryOrderStatus(orderNo);
        logger.debug("getPayStatus successfully -- primary key: [{}], result is [\n{}\n]", status);
        result.setResult(status);
        return result;
    }
}