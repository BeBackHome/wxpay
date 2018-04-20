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
 * project: shopping-mall
 * <p>
 * Revision History:
 * Date         Version     Name                Description
 * 2/26/2018  1.0         weber         Creation File
 */
package com.cloudolp.peony.server.wxpay.entities;


import com.cloudolp.peony.server.wxpay.constants.Constants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Description:
 *
 * @author weber
 * @date 2/26/2018 10:31 AM
 */
@Entity
public class WechatPayRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private long version=0;
    @Column(updatable = false)
    private LocalDateTime createdDateTime;
    @Column(columnDefinition = "int")
    private Boolean deleted;
    private String orderNo;  //销售订单订单号
    private BigDecimal totalFee;    //交易金额，标价金额
    private String prepayId;    //预支付Id
    private String appId;   //公众账号ID
    private String mchId;   //商户号
    private String codeUrl;
    private Constants.PayStatus status; //支付状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public Constants.PayStatus getStatus() {
        return status;
    }

    public void setStatus(Constants.PayStatus status) {
        this.status = status;
    }
}