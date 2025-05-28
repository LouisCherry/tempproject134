package com.epoint.znsb.jnzwfw.water;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * @program: openPlatform
 * @description: 工具类
 * @author: Mr.YS
 * @CreatDate: 2019/5/20/020 17:20
 */
public class Util {

    /**
     * 获取到订单号
     *
     * @param msgSrcId 消息来源编号  1017
     * @return 商户订单号    {来源编号(4位)}{时间(yyyyMMddmmHHssSSS)(17位)}{7位随机数}
     */
    public static String getBillNo(String msgSrcId) {
        return msgSrcId + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(7);
    }

    /**
     * 获取到退货订单号refundOrderId
     *
     * @param msgSrcId 消息来源编号  1017
     * @return 商户订单号    {来源编号(4位)}{时间(yyyyMMddmmHHssSSS)(17位)}{7位随机数}
     */
    public static String getRefundOrderId(String msgSrcId) {
        return msgSrcId + DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(7);
    }
}