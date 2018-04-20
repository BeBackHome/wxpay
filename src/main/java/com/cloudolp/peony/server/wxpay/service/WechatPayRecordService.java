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

import com.cloudolp.peony.server.wxpay.constants.Constants;
import com.cloudolp.peony.server.wxpay.entities.WechatPayRecord;

import java.util.List;

/**
 * Description:
 *
 * @author weber
 * @date 4/20/2018 2:45 PM
 */
public interface WechatPayRecordService {
    WechatPayRecord insertOrUpdate(WechatPayRecord wechatPayRecord);

    WechatPayRecord loadByOrderNo(String orderNo);

    List<WechatPayRecord> listByOrderNoPrefix(String orderNo);

    WechatPayRecord loadTopByOrderNoPrefix(String orderNo);

    WechatPayRecord updateStatus(Constants.PayStatus status, String orderNo);
}