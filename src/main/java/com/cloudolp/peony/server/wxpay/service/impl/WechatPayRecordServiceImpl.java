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

import com.cloudolp.peony.server.wxpay.constants.Constants;
import com.cloudolp.peony.server.wxpay.entities.WechatPayRecord;
import com.cloudolp.peony.server.wxpay.repository.WechatPayRecordRepository;
import com.cloudolp.peony.server.wxpay.service.WechatPayRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author weber
 * @date 4/20/2018 2:46 PM
 */
@Service
public class WechatPayRecordServiceImpl implements WechatPayRecordService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WechatPayRecordRepository defaultRepository;

    @Override
    public WechatPayRecord insertOrUpdate(WechatPayRecord wechatPayRecord) {
        WechatPayRecord wechatPayRecordExist = this.loadByOrderNo(wechatPayRecord.getOrderNo());
        if (wechatPayRecordExist != null) {
            wechatPayRecord.setId(wechatPayRecordExist.getId());
            wechatPayRecord.setCreatedDateTime(wechatPayRecordExist.getCreatedDateTime());
            wechatPayRecord.setVersion(wechatPayRecordExist.getVersion());
        }
        return defaultRepository.save(wechatPayRecord);
    }

    @Override
    public WechatPayRecord loadByOrderNo(String orderNo) {
        return defaultRepository.findTopByOrderNo(orderNo);
    }

    @Override
    public List<WechatPayRecord> listByOrderNoPrefix(String orderNo) {
        return defaultRepository.findByOrderNoContainingIgnoreCaseOrderByOrderNoDesc(orderNo + "_");
    }

    @Override
    public WechatPayRecord loadTopByOrderNoPrefix(String orderNo) {
        return defaultRepository.findTopByOrderNoContainingIgnoreCaseOrderByOrderNoDesc(orderNo);
    }

    @Override
    public WechatPayRecord updateStatus(Constants.PayStatus status, String orderNo) {
        WechatPayRecord wechatPayRecordExist = this.loadByOrderNo(orderNo);
        if (wechatPayRecordExist == null) {
            return null;
        }
        wechatPayRecordExist.setStatus(status);
        return defaultRepository.save(wechatPayRecordExist);
    }

}