package com.cloudolp.peony.server.wxpay.service;

import com.cloudolp.peony.server.wxpay.constants.Constants;
import com.cloudolp.peony.server.wxpay.entities.WechatPayRecord;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;

import java.io.IOException;
import java.math.BigDecimal;

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
public interface WechatPayService {

    String createScanPayCodeUrl(String orderNo, BigDecimal totalFee, String ip) throws IOException, WxPayException;

    WxPayNotifyResponse wechatRevoke(String xmlData);

    Constants.PayStatus queryOrderStatus(String orderNo);

    Boolean testUpdate(String orderNo, Constants.PayStatus payStatus);
}
