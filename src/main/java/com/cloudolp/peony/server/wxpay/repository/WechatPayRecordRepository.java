package com.cloudolp.peony.server.wxpay.repository;


import com.cloudolp.peony.server.wxpay.entities.WechatPayRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
 * project: shop-service
 * <p>
 * Revision History:
 * Date         Version     Name                Description
 * 9/20/2017  1.0         weber         Creation File
 */
public interface WechatPayRecordRepository extends JpaRepository<WechatPayRecord, Integer> {
    WechatPayRecord findTopByOrderNo(String orderNo);

    List<WechatPayRecord> findByOrderNoContainingIgnoreCaseOrderByOrderNoDesc(String orderNo);

    WechatPayRecord findTopByOrderNoContainingIgnoreCaseOrderByOrderNoDesc(String orderNo);
}
