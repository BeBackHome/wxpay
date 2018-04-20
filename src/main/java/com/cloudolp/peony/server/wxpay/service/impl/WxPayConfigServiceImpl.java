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
 * 4/20/2018  1.0         weber         Creation File
 */
package com.cloudolp.peony.server.wxpay.service.impl;

import com.cloudolp.peony.server.wxpay.service.WxPayConfigService;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Description:
 *
 * @author weber
 * @date 4/20/2018 1:56 PM
 */
@Service
public class WxPayConfigServiceImpl implements WxPayConfigService {
    @Value("${wechat.pay.config.domain}")
    private String wechatPayRevokeDomain;

    @Value("${wechat.pay.config.appId}")
    private String appId;

    @Value("${wechat.pay.config.mchId}")
    private String mchId;

    @Value("${wechat.pay.config.key}")
    private String key;

    @Override
    public WxPayService config() throws IOException {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(appId);
        wxPayConfig.setMchId(mchId);
        wxPayConfig.setMchKey(key);
        wxPayConfig.setTradeType("NATIVE");
        wxPayConfig.setNotifyUrl(wechatPayRevokeDomain + "/rest/pay/invoke.html");
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }

}