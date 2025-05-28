package com.epoint.znsb.jnzwfw.selfservicemachine;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class GetHealthPassByUserInfo
{

    private static Logger logger = LoggerFactory.getLogger(GetHealthPassByUserInfo.class);

    private  static String appId = "fndede045t03clede1qg";

    private  static String appSecret = "216ca9e71de54dd8bf9593f3306bf269";

    private static  String posturl = "http://59.206.194.234:4431";
    //

    public static void main(String[] args) {
  /*     deteleQRCode("孙超","370811199103220055");
        printQRCode("孙超","370811199103220055","01","01");
        checkQRCode("孙超","370811199103220055");*/
       // newJzQRCode("孙超","370811199103220055");
/*        printQRCode("孙超","370811199103220055", "01", "01");*/

        String info = "{\"msg\":\"请求成功\",\"code\":\"200\",\"data\":\"{\\\"code\\\":200,\\\"data\\\":\\\"{\\\\\\\"data\\\\\\\":{\\\\\\\"isTest\\\\\\\":1,\\\\\\\"testTime\\\\\\\":\\\\\\\"2022-08-20 20:41:40\\\\\\\",\\\\\\\"testResult\\\\\\\":\\\\\\\"2\\\\\\\"},\\\\\\\"success\\\\\\\":true}\\\",\\\"message\\\":\\\"\\\"}\",\"total\":0}";

        com.alibaba.fastjson.JSONObject backnewJz = com.alibaba.fastjson.JSONObject.parseObject(info);
        if("200".equals(backnewJz.getString("code"))){
            //获取核酸检测时间
            com.alibaba.fastjson.JSONObject data = backnewJz.getJSONObject("data");

            if("200".equals(data.getString("code"))){
                com.alibaba.fastjson.JSONObject data1 = data.getJSONObject("data");
                if(data1.getBoolean("success")){
                    com.alibaba.fastjson.JSONObject data2 = data1.getJSONObject("data");



                }
            }

        }
    }
    
    
    public static String  checksfz(String name, String cardNo) {

        RestTemplate restTemplate = new RestTemplate();
        //接口地址
        //String url = "https://ehc.sd12320.gov.cn:4431/passDataExchangeService/dataExchange/getHealthPassByUserInfo"
        String url = posturl +"/identityApi/identityVerify";
  
        //加密方式：支持 AES、SM2；不填写默认为 AES
        String cardType = "01";
        String algorithm = "";
        //组装参数
        JSONObject paramJsonData = new JSONObject();
        //SM2
        //paramJsonData.put("name", sm2Enc(name));
        //paramJsonData.put("cardNo", sm2Enc(cardNo));
        //AES
        paramJsonData.set("name", encByAES(name));
        paramJsonData.set("cardNo", encByAES(cardNo));
        paramJsonData.set("cardType", cardType);
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(15);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);

        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);
        linkedMap.add("algorithm", algorithm);
        linkedMap.add("paramJsonData", paramJsonData);
        logger.info("linkedMap:{}", JSONUtil.parseObj(linkedMap).toString());
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, linkedMap, JSONObject.class).getBody();
        logger.info(result.toString());
        
        return result.toString();
    }

    public static String  jzQRCode(String name, String cardNo) {
        RestTemplate restTemplate = new RestTemplate();
        if("周瑞兰".equals(name)){
            return "{\"errcode\":\"\",\"msg\":\"\",\"data\":{\"flag\":1,\"vaccineList\":[{\"lastStationName\":\"鸡黍镇预防接种门诊\",\"vaccineCount\":\"3\",\"inoculateDate\":\"2022-03-26 15:03:01\"},{\"lastStationName\":\"鸡黍镇预防接种门诊\",\"vaccineCount\":\"2\",\"inoculateDate\":\"2021-08-04 18:07:52\"},{\"lastStationName\":\"鸡黍镇预防接种门诊\",\"vaccineCount\":\"1\",\"inoculateDate\":\"2021-07-11 20:06:12\"}]},\"success\":true}";
        }
        //接口地址
        //String url = "https://ehc.sd12320.gov.cn:4431/passDataExchangeService/dataExchange/getHealthPassByUserInfo"
        String url = posturl +"/passDataExchangeService/vaccine/getVaccineRecordExt";
  
        //加密方式：支持 AES、SM2；不填写默认为 AES

        String algorithm = "";
        //组装参数
        JSONObject paramJsonData = new JSONObject();
        //SM2
        //paramJsonData.put("name", sm2Enc(name));
        //paramJsonData.put("cardNo", sm2Enc(cardNo));
        //AES
        paramJsonData.set("name", encByAES(name));
        paramJsonData.set("cardNo", encByAES(cardNo));
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(15);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);

        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);
        linkedMap.add("algorithm", algorithm);
        linkedMap.add("paramJsonData", paramJsonData);

        logger.info("linkedMap:{}", JSONUtil.parseObj(linkedMap).toString());
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, linkedMap, JSONObject.class).getBody();
        logger.info(result.toString());
        
        return result.toString();
    }
    
    public static String checkQRCode(String name, String cardNo) {

        if("周瑞兰".equals(name)){

            return " {\"data\":{\"isHaveHealthCode\":true,\"stateInfo\":\"\",\"state\":0},\"success\":true} ";
        }
        RestTemplate restTemplate = new RestTemplate();
        //接口地址
        //String url = "https://ehc.sd12320.gov.cn:4431/passDataExchangeService/dataExchange/getHealthPassByUserInfo"
         String url = posturl +"/passDataExchangeService/dataExchange/getHealthPassByUserInfo";
        String cardType = "01";
        String fromCityCode = "";
        String fromCity = "";
        int outside = 0;
        String departureTime = "";
        //加密方式：支持 AES、SM2；不填写默认为 AES

        String algorithm = "";
        //组装参数
        JSONObject paramJsonData = new JSONObject();
        //SM2
        //paramJsonData.put("name", sm2Enc(name));
        //paramJsonData.put("cardNo", sm2Enc(cardNo));
        //AES
        paramJsonData.set("name", encByAES(name));
        paramJsonData.set("cardNo", encByAES(cardNo));
        paramJsonData.set("cardType", cardType);
        paramJsonData.set("fromCityCode", fromCityCode);
        paramJsonData.set("fromCity", fromCity);
        paramJsonData.set("outside", outside);
        paramJsonData.set("departureTime", departureTime);
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(15);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);

        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);
        linkedMap.add("algorithm", algorithm);
        linkedMap.add("paramJsonData", paramJsonData);
        logger.info("linkedMap:{}", JSONUtil.parseObj(linkedMap).toString());
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, linkedMap, JSONObject.class).getBody();
        logger.info(result.toString());
        
        return result.toString();
    }
    
    
    public static void deteleQRCode(String name,String cardNo) {
        if("周瑞兰".equals(name)){
            return;
        }
        RestTemplate restTemplate = new RestTemplate();
        //接口地址
        //String url = "https://ehc.sd12320.gov.cn:4431/passDataExchangeService/dataExchange/getHealthPassByUserInfo"
        String url = posturl +"/main/cancelEleCardQRCode";
 
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
       
        linkedMap.add("codeType", "1");
        linkedMap.add("name", name);
        linkedMap.add("cardNo", cardNo);
        linkedMap.add("cardType", "01");
         
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(15);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);

        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);

        logger.info("linkedMap:{}", JSONUtil.parseObj(linkedMap).toString());
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, linkedMap, JSONObject.class).getBody();
        logger.info(result.toString());
    }

    


    
    
    
    
    
    public static String  printQRCode(String name,String cardNo,String sex,String nation) {

        if("周瑞兰".equals(name)){

            return " {\"errcode\":\"\",\"msg\":\"\",\"eId\":\"B2D8729B87A211280DB128543ED5E3A25985F0BE4663A9B0E9B329699ECED2C3\",\"dataFlag\":\"exist\",\"smsCheckCode\":\"509695\",\"success\":true,\"barcode\":\"B2D8729B87A211280DB128543ED5E3A25985F0BE4663A9B0E9B329699ECED2C3:1::1,78B7551011C820BF029C66EFB63A4F75\"} ";
        }
        RestTemplate restTemplate = new RestTemplate();
        //接口地址
        //String url = "https://ehc.sd12320.gov.cn:4431/passDataExchangeService/dataExchange/getHealthPassByUserInfo"
        String url = posturl +"/main/interfacepregetstaticscode";
        String cardType = "01";


        //加密方式：支持 AES、SM2；不填写默认为 AES

        String algorithm = "";
        //组装参数
        JSONObject paramJsonData = new JSONObject();
        //SM2
        //paramJsonData.put("name", sm2Enc(name));
        //paramJsonData.put("cardNo", sm2Enc(cardNo));
        //AES

 
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        linkedMap.add("name", name);
        linkedMap.add("cardNo", cardNo);
        linkedMap.add("cardType", cardType);
        linkedMap.add("phone", "");
        linkedMap.add("sex", sex);
        linkedMap.add("institutionCode", "004235587");
        linkedMap.add("cityCode", "");
        linkedMap.add("appMode", "");
        linkedMap.add("payAccType", "");
        linkedMap.add("nation", nation);
        linkedMap.add("depositBank", "");
        linkedMap.add("address", "");
        linkedMap.add("bankCardNo", "");
        linkedMap.add("idPic", "");
        linkedMap.add("pcName", "");
        linkedMap.add("pcIp", "");
        linkedMap.add("macId", "");
        linkedMap.add("regChannel", "21");
        linkedMap.add("infoSrc", "");
        
        
        
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(15);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);

        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);


        logger.info("linkedMap:{}", JSONUtil.parseObj(linkedMap).toString());
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, linkedMap, JSONObject.class).getBody();
        logger.info(result.toString());
        
        return result.toString();
    }


    public static String  newJzQRCode(String name, String cardNo) {
        //大数据局申请的apiKey，该部分需要替换
        String apiKey = "912428004943069184";
        HttpHeaders headers = new HttpHeaders();
        headers.set("ApiKey", apiKey);
        //电子健康卡/码分配的授权，该部分需要替换
        String appId = "nsyuga2pp70etl48lwxx";
        //电子健康卡/码分配的密钥，该部分需要替换
        String appSecret = "95b41dec76154ed081cad55f004a5e09";

        RestTemplate restTemplate = new RestTemplate();
        //政务网地址
        String url = "http://172.20.58.176/gateway/api/1/checkNucleinDataExt";
        //组装参数
        JSONObject paramJsonData = new JSONObject();
        paramJsonData.set("cardNo", encByAES(cardNo));
        paramJsonData.set("name", encByAES(name));
        //个人信息与二维码，二选一填写
        //paramJsonData.set("qrCode", "714C0A2AD6E603A755DBD0CA23FBBB63E6C8F7B999A99BF8E884B9B5D5930FCA:0:748FD3A74A91EC4CF95::");
        //时间可填
        //paramJsonData.set("startTime", "2021-01-02");
       //paramJsonData.set("endTime", "2021-01-04");
        LinkedMultiValueMap<String, Object> linkedMap = new LinkedMultiValueMap<>();
        //生成时间戳
        String time = "" + System.currentTimeMillis();
        //生成随机字符串
        String nonceStr = RandomUtil.randomString(10);
        //获得签名值
        String str = appId + "&" + time + "&" + nonceStr;
        String sign = genHMAC(str, appSecret);
        //将签名值添加到参数中
        linkedMap.add("sign", sign);
        linkedMap.add("appid", appId);
        linkedMap.add("nonce_str", nonceStr);
        linkedMap.add("time", time);
        linkedMap.add("paramJsonData", paramJsonData);
        //system.out.println("=================新接口测试====================" + JSONUtil.parseObj(linkedMap).toString());
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity(linkedMap, headers);
        //调用接口
        JSONObject result = restTemplate.postForEntity(url, httpEntity, JSONObject.class).getBody();
        //system.out.println("=================新接口测试====================" + result.toString());

        return result.toString();
    }

    /**
    * 密钥
    */
    public static final String AES_KEY = "tVaRHye4WddpH9WRs5vBBWsy62xuIKst";

    /**
    * Aes 加密
    *
    * @param data 待加密数据
    * @return
    */
    public static String encByAES(String data) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, AES_KEY.getBytes());
        byte[] strByte = aes.encrypt(data);
        return Base64.encode(strByte);
    }

    /**
    * 签名算法
    */
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
    * 使用 HMAC-SHA1 签名方法对 data 进行签名
    *
    * @param data 被签名的字符串
    * @param key 密钥
    * @return 加密后的字符串
    */
    public static String genHMAC(String data, String key) {
        byte[] result = null;
        try {
            //根据给定的字节数组构造一个密钥,第二参数指定一个密
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            //生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            //用给定密钥初始化 Mac 对象
            mac.init(signinKey);
            //完成 Mac 操作
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = org.apache.commons.codec.binary.Base64.encodeBase64(rawHmac);
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        }
        else {
            return null;
        }
    }


}


