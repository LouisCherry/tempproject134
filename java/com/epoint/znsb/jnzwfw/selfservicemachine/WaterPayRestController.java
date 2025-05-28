package com.epoint.znsb.jnzwfw.selfservicemachine;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.znsb.auditznsbwaterjfinfo.api.IAuditZnsbWaterjfinfoService;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;
import com.epoint.znsb.jnzwfw.water.CsaobPay;
import com.epoint.znsb.jnzwfw.water.Util;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueCommonUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 广电接口
 */
@RestController
@RequestMapping(value = "/waterpay")
public class WaterPayRestController {

    transient Logger log = LogUtil.getLog(WaterPayRestController.class);

    @Autowired
    private IAuditZnsbWaterjfinfoService jfService;

    /**
     * 客户信息查询
     * [功能详细描述]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(@RequestBody String params) {

        try {
            JSONObject obj = JSONObject.parseObject(params);
            SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
            String source = "12";
            String tID = obj.getString("code");
            String back = "000";
            String payHourage = getBackFlowno();
            String accountDate = f1.format(new Date());
            String jx = "|0053000XJ";
            String num = obj.getString("num");
            String url = "http://172.16.53.22:8080/jnzwdt/rest/iso8583/sendData";
            JSONObject newJson = new JSONObject();
            newJson.put("ip", "111.14.43.2");
            newJson.put("port", "8899");
            newJson.put("data", source + tID + back + payHourage + accountDate + jx + num);
            JSONObject newDataJson = new JSONObject();
            newDataJson.put("params", newJson);
//            log.info("============水务缴费参数====================" + newDataJson);

            String backString = HttpUtil.doPostJson(url, newDataJson.toString());
//            log.info("============水务缴费返回值===================" + backString);
            JSONObject backJson = JSONObject.parseObject(backString);
            JSONObject custom = backJson.getJSONObject("custom");
            String newBackString = custom.getString("backstring");
            //分析数据 用|隔开的数据分析
            String[] strs = newBackString.split("\\|");
            //每一个区间代表的含义
            JSONObject dataJson = new JSONObject();
            dataJson.put("status",strs[5]);
            dataJson.put("address",strs[3]);
            dataJson.put("name",strs[4]);
            dataJson.put("qmoney",strs[6]);
            dataJson.put("ymoney",strs[7]);
            dataJson.put("paymoney",strs[8]);
            dataJson.put("payflown",payHourage);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 订购
     * [功能详细描述]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String charge(@RequestBody String params) {
        try {
            SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
            JSONObject obj = JSONObject.parseObject(params);
            String source = "14";
            String tID = "022";
            String back = "000";
            String payHourage = obj.getString("flowno");
            String accountDate = f1.format(new Date());
            String jx = "|0053000XJ";
            String num = obj.getString("num");
            String jyje = obj.getString("jyje");
            String url = "http://112.6.110.176:25001/jnzwdt/rest/iso8583/sendData";
            JSONObject newJson = new JSONObject();
            newJson.put("ip", "111.14.43.2");
            newJson.put("port", "8899");
            newJson.put("data", source + tID + back + payHourage + accountDate + jx + num  + "|19700101|" + accountDate +"|"+jyje + "|"+accountDate);

            //system.out.println(newJson.getString("data"));
            JSONObject newDataJson = new JSONObject();
            newDataJson.put("params", newJson);
         /*   String backString = HttpUtil.doPostJson(url, newDataJson.toString());
            log.info("==================水务对账====================" + backString);
*/
            AuditZnsbWaterjfinfo auditZnsbWaterjfinfo = new AuditZnsbWaterjfinfo();

            auditZnsbWaterjfinfo.setRowguid(UUID.randomUUID().toString());
            auditZnsbWaterjfinfo.setWaterflowon(payHourage);
            auditZnsbWaterjfinfo.setWaterpaymoney(jyje);
            auditZnsbWaterjfinfo.setWaternumber(num);
            auditZnsbWaterjfinfo.setWatertime(accountDate);
            auditZnsbWaterjfinfo.setStarttime(accountDate);
            auditZnsbWaterjfinfo.setEndtime(accountDate);
            jfService.insert(auditZnsbWaterjfinfo);

            
            return JsonUtils.zwdtRestReturn("1", "", "");


        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


    //银联缴费二维码

    @RequestMapping(value = "/chargeewm", method = RequestMethod.POST)
    public String chargeewm(@RequestBody String params) {
        try{
            JSONObject obj = JSONObject.parseObject(params);
            JSONObject json = new JSONObject();
            json.put("msgId", "001");   // 消息Id,原样返回
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));    // 报文请求时间
            json.put("srcReserve", "请求系统预留字段"); // 请求系统预留字段
            json.put("mid", "89837084900W519"); // 商户号
            json.put("tid", "86864544");    // 终端号
            json.put("instMid", "QRPAYDEFAULT"); // 业务类型
            json.put("billNo", Util.getBillNo("30J5")); // 商户订单号
            json.put("billDate", DateFormatUtils.format(new Date(), "yyyy-MM-dd")); // 账单日期
            json.put("billDesc", "账单描述");  // 账单描述
            json.put("totalAmount", obj.getString("money"));      // 支付总金额

            json.put("expireTime", DateFormatUtils.format(new Date().getTime()+60*1000,"yyyy-MM-dd HH:mm:ss"));  // 订单过期时间,这里设置为一分钟后
            json.put("notifyUrl", "http://www.baidu.com");  // 支付结果通知地址,修改为商户自己的地址
            json.put("returnUrl", "https://www.sina.com.cn/");  // 网页跳转地址,修改为商户自己的地址
            json.put("walletOption", "SINGLE");   // 单钱包和多钱包,多钱包为:MULTIPLE

//            log.info("===========银联缴费二维码=========" + "请求报文:\n"+json);

            String url = "https://api-mop.chinaums.com/v1/netpay/bills/get-qrcode";
            String backString = CsaobPay.send(url, json.toString());

//            log.info("===========银联缴费二维码=========" + "返回结果:\n"+backString);

             return JsonUtils.zwdtRestReturn("1", "", backString);

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }




    /**
     * 二维码更新
     * @return
     */

    @RequestMapping(value = "/changerwm", method = RequestMethod.POST)
    public String changerwm(@RequestBody String params) {
        try {
            JSONObject obj = JSONObject.parseObject(params);

            JSONObject json = new JSONObject();
            json.put("msgId", "001");   // 消息Id,原样返回
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));    // 报文请求时间
            json.put("srcReserve", "请求系统预留字段"); // 请求系统预留字段
            json.put("mid", "89837084900W519"); // 商户号
            json.put("tid", "86864544");    // 终端号
            json.put("instMid", "QRPAYDEFAULT"); // 业务类型
            json.put("qrCodeId", obj.getString("qrCodeId")); // 二维码id

//            log.info("===========二维码更新=========" + "请求报文:\n"+json);

            String url = "https://api-mop.chinaums.com/v1/netpay/bills/update-qrcode";
            String send = CsaobPay.send(url, json.toString());

//            log.info("===========二维码更新=========" + "返回结果:\n"+send);

            return JsonUtils.zwdtRestReturn("1", "", "");


        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


    /**
     * 获取订单状态
     * @return
     */
    @RequestMapping(value = "/getorderstatus", method = RequestMethod.POST)
    public String getorderstatus(@RequestBody String params) {
        try {
            JSONObject obj = JSONObject.parseObject(params);

            JSONObject json = new JSONObject();
            json.put("msgId", "001");   // 消息Id,原样返回
            json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));    // 报文请求时间
            json.put("srcReserve", "请求系统预留字段"); // 请求系统预留字段
            json.put("mid", "89837084900W519"); // 商户号
            json.put("tid", "86864544");    // 终端号
            json.put("instMid", "QRPAYDEFAULT"); // 业务类型
            json.put("billNo", obj.getString("orderid")); // 商户订单号
            json.put("billDate", DateFormatUtils.format(new Date(), "yyyy-MM-dd")); // 账单日期
//            log.info("===========订单状态=========" + "请求报文:\n"+json);
            String url = "https://api-mop.chinaums.com/v1/netpay/bills/query";
            String send =  CsaobPay.send(url, json.toString());
//            log.info("===========订单状态=========" + "返回结果:\n"+send);
            return JsonUtils.zwdtRestReturn("1", "", send);


        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }




    public static String getBackFlowno() {
        String flowsnno = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        flowsnno = format.format(new Date());
/*        ZwfwRedisCacheUtil redis = null;

        try {
            redis = new ZwfwRedisCacheUtil(false);
            flowsnno = "0000" + EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd")
                    + QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn("SWFlowSN")), 5, '0');

        } catch (Exception e) {
            //log.error(ExceptionUtils.getFullStackTrace(e));
        } finally {
            if (redis != null) {
                redis.close();
            }
        }*/
        //system.out.println(flowsnno);
        return flowsnno;
    }

    public static void main(String[] args) {

        getBackFlowno();
    }

}
