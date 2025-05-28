package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.HttpUtil;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.zoucheng.znsb.worktablecomment.util.QueueCommonUtil;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 广电接口
 */
@RestController
@RequestMapping(value = "/unionpay")
public class UnionPayRestController
{

    transient Logger log = LogUtil.getLog(UnionPayRestController.class);

    /**
     * 
     *  客户信息查询
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(@RequestBody String params) {

        try {
            String url = "https://tyzf1.sdgdwl.com:16889/sdp/query.jspx";

            //https://tyzf1.sdgdwl.com:16889/sdp/
            JSONObject obj = JSONObject.parseObject(params);

            JSONObject query = new JSONObject();

            JSONObject commonTradeInfo = getBackKey("");

            query.put("commonTradeInfo", commonTradeInfo);
            query.put("identificationNo", obj.getString("identificationNo"));
            query.put("identificationType", obj.getString("identificationType"));
            JSONObject dataJson = new JSONObject();
            JSONObject queryReq = new JSONObject();
            queryReq.put("queryReq", query);
            Map<String, String> map = new HashMap<>();
            log.info("广电缴费接口客户查询：" + queryReq);
            map.put("params", queryReq.toString());
            String backString = HttpUtil.sendGet(url, map);
            log.info("广电缴费接口客户查询：" +backString);
            //后台分解
            JSONObject queryResJson = JSON.parseObject(backString);
            JSONObject queryRes = queryResJson.getJSONObject("queryRes");

            JSONObject commonResultInfo = queryRes.getJSONObject("commonResultInfo");

            if ("000".equals(commonResultInfo.getString("returnCode"))) {
                JSONArray productInfos = queryRes.getJSONArray("productInfos");
                dataJson.put("sn",commonResultInfo.getString("sn"));
                dataJson.put("zcode",queryRes.getString("code"));
                dataJson.put("productInfos",productInfos);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);

            } else {
                return JsonUtils.zwdtRestReturn("0", "查询失败，请检查输入的参数！", "");
            }

        }catch (Exception e){
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


    /**
     *
     *  订购
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public String charge(@RequestBody String params) {
        try {
        JSONObject obj = JSONObject.parseObject(params);
        String url = "https://tyzf1.sdgdwl.com:16889/sdp/charge.jspx";
        JSONObject charge = new JSONObject();
        charge.put("commonTradeInfo", getBackKey(obj.getString("sn")));
        SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat f3 = new SimpleDateFormat("yyyyMMddHHmmss");
        String source = "SD022";
        String tID = "20170515173559";
        String callBackUrl = "http://baidu.com.cn";
        String payHourage = "1";
        String accountDate = f1.format(new Date());
        String reqTime =  f2.format(new Date());
        String amount = obj.getString("price");;
        String productDesc = obj.getString("prodName");
        String isUsePayPlatform = "Y";
        String payModeCode = "BANK_CHANNEL";
        String defaultPayWay = "000";
        String wxCode = "";

        charge.put("tID", tID);
        charge.put("callBackUrl", callBackUrl);
        charge.put("payHourage", Integer.valueOf(payHourage));

        charge.put("accountDate", accountDate);

        charge.put("reqTime",reqTime);
        charge.put("amount", Integer.valueOf(amount));
        charge.put("productDesc",productDesc);
        charge.put("productID", obj.getString("prodId"));
        charge.put("productName", obj.getString("prodName"));
        charge.put("productNum", 1);
        charge.put("isUsePayPlatform", isUsePayPlatform);
        charge.put("payModeCode", payModeCode);
        charge.put("defaultPayWay", defaultPayWay);
        charge.put("wxCode",wxCode);
        //charge.put("sign","6ad0482c1bd3d3422eb8bd3d75c7fc67000")
           // findCustomerProductSummary();
        ////system.out.println(MD5Util.getMD5(source+tID+callBackUrl+ Integer.valueOf(payHourage)+accountDate+reqTime+Integer.valueOf(amount)+productDesc+obj.getString("prodId")+obj.getString("prodName")+1+isUsePayPlatform+payModeCode).toLowerCase() + defaultPayWay.toLowerCase()+ wxCode.toLowerCase());;
        charge.put("sign",MD5Util.getMD5(source+tID+callBackUrl+ Integer.valueOf(payHourage)+accountDate+reqTime+Integer.valueOf(amount)+productDesc+obj.getString("prodId")+obj.getString("prodName")+1+isUsePayPlatform+payModeCode).toLowerCase() + defaultPayWay.toLowerCase()+ wxCode.toLowerCase());
        JSONObject purchaseProductOrderInfo = new JSONObject();
        purchaseProductOrderInfo.put("customerCode",obj.getString("code"));
        purchaseProductOrderInfo.put("prodId",obj.getString("prodId"));
        purchaseProductOrderInfo.put("mobileNo","");
        purchaseProductOrderInfo.put("extends1","111111");

        charge.put("purchaseProductOrderInfo", purchaseProductOrderInfo);

        JSONObject chargeReq = new JSONObject();

        chargeReq.put("chargeReq", charge);

        Map<String, String> map = new HashMap<>();
        map.put("params", chargeReq.toString());
        log.info("广电缴费接口订购：" + chargeReq);
        String backString = HttpUtil.sendGet(url, map);
        log.info("广电缴费接口订购：" + backString);

        JSONObject chargeResJson = JSON.parseObject(backString);
        JSONObject chargeRes = chargeResJson.getJSONObject("chargeRes");

        JSONObject  commonResultInfo = chargeRes.getJSONObject("commonResultInfo");

        if("000".equals(commonResultInfo.getString("returnCode"))){

            return JsonUtils.zwdtRestReturn("1", chargeRes.getString("qr_code"), "");
        }else{

            return JsonUtils.zwdtRestReturn("0", commonResultInfo.getString("message"), "");
        }

        }catch (Exception e){
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    private static JSONObject getBackKey(String flowsnno){

        JSONObject commonTradeInfo = new JSONObject();
        //获取流水号
        ZwfwRedisCacheUtil redis = null;
        if(StringUtil.isBlank(flowsnno)){
            try{
                redis = new ZwfwRedisCacheUtil(false);
                flowsnno = EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd")
                        + QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn("GDFlowSN")), 5, '0');
                String backString = getBusinessLevelToken();
                JSONObject backJson = JSON.parseObject(backString);

                JSONObject getBusinessLevelTokenRes = backJson.getJSONObject("getBusinessLevelTokenRes");
                commonTradeInfo.put("token", getBusinessLevelTokenRes.getString("token"));


            } catch (Exception e) {
                //log.error(ExceptionUtils.getFullStackTrace(e));
            } finally {
                if (redis != null) {
                    redis.close();
                }
            }
        }else{

            String backString = getOrderLevelToken();
            JSONObject backJson = JSON.parseObject(backString);

            JSONObject getBusinessLevelTokenRes = backJson.getJSONObject("getOrderLevelTokenRes");
            commonTradeInfo.put("token", getBusinessLevelTokenRes.getString("token"));


        }


        String key = "&M3oOKJwY2";

        commonTradeInfo.put("source", "JN001");
        commonTradeInfo.put("sn", flowsnno);
        @SuppressWarnings("deprecation")
        String backstring = MD5Util.getMD5(flowsnno + key);

        commonTradeInfo.put("verificationCode", backstring);
        commonTradeInfo.put("areaCode", "JN");
        commonTradeInfo.put("operation","");

        return commonTradeInfo;

    }


    /**
     * 
     *  订购
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String chargeString(JSONObject commonTradeInfo,String productID, String productName, int productNum,int amount) {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/charge.jspx";
        JSONObject charge = new JSONObject();

        commonTradeInfo = getBackKey(commonTradeInfo.getString(""));

        charge.put("commonTradeInfo", commonTradeInfo);
        SimpleDateFormat f1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat f2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat f3 = new SimpleDateFormat("yyyyMMddHHmmss");
        String source = "JN001";
        String tID = f3.format(new Date());
        String callBackUrl = "http://baidu.com.cn";
        int payHourage = 1;
        String accountDate = f1.format(new Date());
        String reqTime =  f2.format(new Date());
        //int amount = 100;
        String productDesc = productName;
        String isUsePayPlatform = "Y";
        String payModeCode = "BANK_CHANNEL";
        String defaultPayWay = "000";
        String wxCode = "";

        charge.put("tID", tID);
        charge.put("callBackUrl", callBackUrl);
        charge.put("payHourage", payHourage);

        charge.put("accountDate", accountDate);

        charge.put("reqTime",reqTime);
        charge.put("amount", amount);
        charge.put("productDesc",productDesc);
        charge.put("productID", productID);
        charge.put("productName", productName);
        charge.put("productNum", productNum);
        charge.put("isUsePayPlatform", isUsePayPlatform);
        charge.put("payModeCode", payModeCode);
        charge.put("defaultPayWay", defaultPayWay);
        charge.put("wxCode",wxCode);

        charge.put("sign",MD5Util.getMD5(source+tID+callBackUrl+ Integer.valueOf(payHourage)+accountDate+reqTime+Integer.valueOf(amount)+productDesc+productID+productName+1+isUsePayPlatform+payModeCode).toLowerCase() + defaultPayWay.toLowerCase()+ wxCode.toLowerCase());
        //charge.put("sign",MD5Util.getMD5( source+tID+callBackUrl+payHourage+accountDate+reqTime+amount+productDesc+productID+productName+productNum+isUsePayPlatform+payModeCode).toLowerCase() + defaultPayWay.toLowerCase()+ wxCode.toLowerCase());
        JSONObject purchaseProductOrderInfo = new JSONObject();
        purchaseProductOrderInfo.put("customerCode","08100218720");
        purchaseProductOrderInfo.put("prodId",productID);
        purchaseProductOrderInfo.put("mobileNo","");
        purchaseProductOrderInfo.put("extends1","111111");
/*        List<JSONObject> terminalInfos  = new ArrayList<>();
        JSONObject terminalInfo = new JSONObject();
        terminalInfo.put("code","");
        terminalInfo.put("specCode","");
        terminalInfo.put("serialNumber","");
        terminalInfo.put("isNew","");
        terminalInfos.add(terminalInfo);
        rechargeOrderInfo.put("terminalInfos",terminalInfos);
        List<JSONObject> productInfos  = new ArrayList<>();
        JSONObject productInfo = new JSONObject();
        productInfo.put("terminalCode","");
        productInfo.put("productCode","");
        productInfo.put("isNew","");
        productInfo.put("productOfferingCode","");
        productInfo.put("preferencePolicyCode","");
        productInfo.put("openDate","");
        productInfo.put("purchaseDays","");
        productInfos.add(productInfo);
        rechargeOrderInfo.put("productInfos",productInfos);*/
        charge.put("purchaseProductOrderInfo", purchaseProductOrderInfo);

        JSONObject params = new JSONObject();

        params.put("chargeReq", charge);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        //system.out.println(params);
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    @RequestMapping(value = "/getback", method = RequestMethod.POST)
    public String getUrlBackByJN(@RequestBody String params) {

        Map<String, String> headermap = new HashMap<>();
        CloseableHttpClient httpClient;
        try {
            String Authorization = ""; //认证内容
            headermap.put("Authorization", Authorization);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String msgId = "";//消息ID
            String requestTimestamp = dateFormat.format(new Date());
            String srcReserve = "";//请求系统预留字段
            String mid = "";//商户号
            String tid = "";//终端号
            String instMid = "";//业务类型
            String billNo = "";//账单号
            String billDate = "";//账单日期
            String billDesc = "";//账单描述
            String totalAmount = "";//支付总金额
            String divisionFlag = "";//分账标记
            String platformAmount = "";//平台商户分账金额

            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL)
                    .loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSslcontext(sslContext)
                    .setSSLHostnameVerifier((x, y) -> true).build();

            String result = HTTPSClientUtil.doPost(httpClient,
                    "http://112.6.110.176:28080/jnzwfwznsb/rest/companycredit/getUrlBack", headermap,
                    JSONObject.parseObject(params));
            log.info("济宁共享平台接口返回数据：:" + result);
            return result;

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     *  查询客户在用产品
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    private static String findCustomerProducts() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/findCustomerProducts.action";
        JSONObject findCustomerProducts = new JSONObject();

        JSONObject commonTradeInfo = new JSONObject();

        commonTradeInfo.put("source", "PAY");
        commonTradeInfo.put("sn", "2016110300001");

        commonTradeInfo.put("verificationCode", "9a3d73c17ed4a1938a71c0d059fa9905");
        commonTradeInfo.put("areaCode", "TA");
        commonTradeInfo.put("operation", "");
        commonTradeInfo.put("token", "");

        findCustomerProducts.put("commonTradeInfo", commonTradeInfo);
        findCustomerProducts.put("customerCode", "09010249783");
        findCustomerProducts.put("forScene", "003");
        findCustomerProducts.put("productStatusType", "1");

        JSONObject params = new JSONObject();

        params.put("findCustomerProducts", findCustomerProducts);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    /**
     * 
     *  查询客户缴费信息
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String findCustomerPayments(String startTime, String endTime) {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/findCustomerPayments.action";
        JSONObject findCustomerPayments = new JSONObject();

        JSONObject commonTradeInfo = new JSONObject();

        commonTradeInfo.put("source", "0212");
        commonTradeInfo.put("sn", "2016110300001");
        commonTradeInfo.put("verificationCode", "9a3d73c17ed4a1938a71c0d059fa9905");
        commonTradeInfo.put("areaCode", "TA");
        commonTradeInfo.put("operation", "");
        commonTradeInfo.put("token", "");

        findCustomerPayments.put("commonTradeInfo", commonTradeInfo);
        findCustomerPayments.put("customerCode", "09010249783");
        findCustomerPayments.put("startTime", startTime);
        findCustomerPayments.put("endTime", endTime);

        JSONObject params = new JSONObject();

        params.put("findCustomerPayments", findCustomerPayments);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    /**
     * 
     *  查询客户已有产品概要
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String findCustomerProductSummary() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/findCustomerProducts4bus.action";
        JSONObject findCustomerProducts4Bus = new JSONObject();

        JSONObject commonTradeInfo = getBackKey("");


        findCustomerProducts4Bus.put("commonTradeInfo", commonTradeInfo);
        findCustomerProducts4Bus.put("customerCode", "08100218720");
        findCustomerProducts4Bus.put("forScene", "000");
        findCustomerProducts4Bus.put("productStatusType", "2");

        JSONObject params = new JSONObject();

        params.put("findCustomerProducts", findCustomerProducts4Bus);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        //system.out.println(params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    /**
     * 
     *  查询客户可订购商品目录树
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String findPurchaseableProductOfferingTrees() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/findPurchaseableProductOfferingTrees.action";
        JSONObject findPurchaseableProductOfferingTrees = new JSONObject();

        JSONObject commonTradeInfo = new JSONObject();

        commonTradeInfo.put("source", "0212");
        commonTradeInfo.put("sn", "2016110300001");
        commonTradeInfo.put("verificationCode", "9a3d73c17ed4a1938a71c0d059fa9905");
        commonTradeInfo.put("areaCode", "TA");
        commonTradeInfo.put("operation", "");
        commonTradeInfo.put("token", "");

        findPurchaseableProductOfferingTrees.put("commonTradeInfo", commonTradeInfo);
        findPurchaseableProductOfferingTrees.put("customerCode", "09010249783");

        JSONObject params = new JSONObject();

        params.put("findPurchaseableProductOfferingTrees", findPurchaseableProductOfferingTrees);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    /**
     * 
     *  查询商品可用优惠策略
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     =-\
    private static String findProductOfferingPreferentialPolicys() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/findProductOfferingPreferentialPolicys.action";
        JSONObject findProductOfferingPreferentialPolicys = new JSONObject();

        JSONObject commonTradeInfo = new JSONObject();

        commonTradeInfo.put("source", "0212");
        commonTradeInfo.put("sn", "2016110300001");
        commonTradeInfo.put("verificationCode", "9a3d73c17ed4a1938a71c0d059fa9905");
        commonTradeInfo.put("areaCode", "TA");
        commonTradeInfo.put("operation", "");
        commonTradeInfo.put("token", "");

        findProductOfferingPreferentialPolicys.put("commonTradeInfo", commonTradeInfo);
        findProductOfferingPreferentialPolicys.put("customerCode", "09010249783");
        findProductOfferingPreferentialPolicys.put("productOfferingCode", "09010249783");
        findProductOfferingPreferentialPolicys.put("terminalSpecCode", "09010249783");
        findProductOfferingPreferentialPolicys.put("terminalSerialNumber", "09010249783");
        findProductOfferingPreferentialPolicys.put("forScene", "09010249783");
        JSONObject params = new JSONObject();

        params.put("findProductOfferingPreferentialPolicys", findProductOfferingPreferentialPolicys);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }

    
    
    /**
     * 
     *  获取查询类令牌(免登录)
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String getNoLoginLevelToken() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/getNoLoginLevelToken.jspx";
      
        JSONObject getNoLoginLevelTokenReq = new JSONObject();

        getNoLoginLevelTokenReq.put("source", "PAY");

        JSONObject params = new JSONObject();

        params.put("getNoLoginLevelTokenReq", getNoLoginLevelTokenReq);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }
    
    /**
     * 
     *  获取业务受理类令牌
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String getBusinessLevelToken() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/getBusinessLevelToken.jspx";
      
        JSONObject getBusinessLevelTokenReq = new JSONObject();

        getBusinessLevelTokenReq.put("source", "PAY");
        
        getBusinessLevelTokenReq.put("code", "8537003845886968");
        
        getBusinessLevelTokenReq.put("password", "11111");
        JSONObject params = new JSONObject();

        params.put("getBusinessLevelTokenReq", getBusinessLevelTokenReq);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);




        return backString;
    }



    /**
     *
     *  获取业务受理类令牌
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String getOrderLevelToken() {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/getOrderLevelToken.jspx";

        JSONObject getOrderLevelTokenReq = new JSONObject();

        getOrderLevelTokenReq.put("source", "PAY");

        JSONObject params = new JSONObject();

        params.put("getOrderLevelTokenReq", getOrderLevelTokenReq);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);




        return backString;
    }
    
    
    /**
     * 
     *  令牌有效性验证
     *  [功能详细描述]
     *  @param startTime
     *  @param endTime
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private static String verifyToken(String token,Integer level) {

        String url = "https://tyzf1.sdgdwl.com:16889/sdp/verifyToken.jspx";
      
        JSONObject verifyTokenReq = new JSONObject();

        verifyTokenReq.put("token", token);
        verifyTokenReq.put("level", level);

        JSONObject params = new JSONObject();

        params.put("verifyTokenReq", verifyTokenReq);

        Map<String, String> map = new HashMap<>();
        map.put("params", params.toString());
        String backString = HttpUtil.sendGet(url, map);
        //system.out.println(backString);
        return backString;
    }


    public static void main(String[] args) {
        /* findCustomerProducts();
          findCustomerPayments("2000-01-01 00:00:00","2016-10-31 00:00:00");
          findCustomerProductSummary();
          findPurchaseableProductOfferingTrees();
          query();
        */

        String backString = getBusinessLevelToken();
        JSONObject backJson = JSON.parseObject(backString);

        JSONObject getNoLoginLevelTokenRes = backJson.getJSONObject("getBusinessLevelTokenRes");

        String token = getNoLoginLevelTokenRes.getString("token");

        verifyToken(token,15);



        String url = "https://tyzf1.sdgdwl.com:16889/sdp/query.jspx";

        JSONObject query = new JSONObject();

        JSONObject commonTradeInfo = getBackKey("");

        query.put("commonTradeInfo", commonTradeInfo);
        query.put("identificationNo", "1242120900159654");
        query.put("identificationType","3");
        JSONObject dataJson = new JSONObject();

        dataJson.put("queryReq", query);

        Map<String, String> map = new HashMap<>();
        //system.out.println(dataJson);
        map.put("params", dataJson.toString());
        String chargeString = HttpUtil.sendGet(url, map);

        //system.out.println(chargeString);

        JSONObject queryResJson = JSON.parseObject(chargeString);
        JSONObject queryRes = queryResJson.getJSONObject("queryRes");

        JSONObject  commonResultInfo = queryRes.getJSONObject("commonResultInfo");

        if("000".equals(commonResultInfo.getString("returnCode"))){
            JSONArray productInfos = queryRes.getJSONArray("productInfos");

            JSONObject productInfo = productInfos.getJSONObject(0);
            String prodId = productInfo.getString("prodId");
            String prodName = productInfo.getString("prodName");
            int sort = productInfo.getInteger("sort");
            int price = productInfo.getInteger("price");

            chargeString(commonTradeInfo,prodId,prodName,sort,price);
        }


    }



}
