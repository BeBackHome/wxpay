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
package com.cloudolp.peony.server.wxpay.constants;

/**
 * Description:
 *
 * @author weber
 * @date 2/27/2018 2:36 PM
 */
public class Constants {
    // 支付状态
    public enum PayStatus {
        Pending(0), //未支付
        Success(1), //支付成功
        Failed(2), //支付失败
        Closed(3); //关单
        //暂时不支持退款
        public Integer value;

        PayStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}