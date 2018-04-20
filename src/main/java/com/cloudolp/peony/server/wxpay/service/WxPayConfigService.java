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
package com.cloudolp.peony.server.wxpay.service;

import com.github.binarywang.wxpay.service.WxPayService;

import java.io.IOException;

/**
 * Description:
 *
 * @author weber
 * @date 4/20/2018 1:55 PM
 */
public interface WxPayConfigService {
    WxPayService config() throws IOException;   //根据店铺获取店铺下面的微信支付配置
}