package com.epoint.znsb.jnzwfw.water;

import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @program: openPlatform
 * @description: c扫b
 * @author: Mr.YS
 * @CreatDate: 2019/6/17/017 15:27
 */
public class CsaobPay {
    static String appId = "8a81c1bf76674cf60178eea075f802f2";
    static String appKey = "9700e93468e5445eb45cb373b9b585cb";
    static String authorization;

    public static void main(String[] args) throws Exception {

        paystatus("30J5202112031001378533619039");
       // paystatus("30J5202112021359247823089973");
    }

    public static String payV2(int totalAmount) throws Exception {
        /**
         * {"msgId":"001","requestTimestamp":"2019-10-22 11:23:48","srcReserve":"请求系统预留字段","mid":"898340149000035","tid":"38557688","instMid":"QRPAYDEFAULT","billNo":"1017201910221123480739037062","billDate":"2019-10-22","billDesc":"账单描述","totalAmount":1.03,"expireTime":"2019-10-22 12:23:48","notifyUrl":"http://www.baidu.com","returnUrl":"https://www.sina.com.cn/","walletOption":"SINGLE"}
         */
        /* post参数,格式:JSON */
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
        json.put("totalAmount", totalAmount);      // 支付总金额


//        json.put("expireTime", DateFormatUtils.format(new Date().getTime()+60*60*1000,"yyyy-MM-dd HH:mm:ss"));  // 订单过期时间,这里设置为一小时后
        json.put("expireTime", DateFormatUtils.format(new Date().getTime()+60*1000,"yyyy-MM-dd HH:mm:ss"));  // 订单过期时间,这里设置为一分钟后
        json.put("notifyUrl", "http://www.baidu.com");  // 支付结果通知地址,修改为商户自己的地址
        json.put("returnUrl", "https://www.sina.com.cn/");  // 网页跳转地址,修改为商户自己的地址
//        json.put("qrCodeId", "111111");   // 二维码id,针对需要自行生成二维码的情况
//        json.put("systemId", "111111");   // 系统id
//        json.put("secureTransaction", false);   // 担保交易标识
        json.put("walletOption", "SINGLE");   // 单钱包和多钱包,多钱包为:MULTIPLE

        /* 实名认证 */
        /*json.put("name", "实名认证姓名"); // 实名认证姓名
        json.put("mobile", "1345678910");   // 实名认证手机号
        json.put("certType", "IDENTITY_CARD");  // 实名认证
        json.put("certNo", "12434134213124");   // 实名认证证件号
        json.put("fixBuyer", "T");  // 是否需要实名认证*/

//        json.put("limitCreditCard", "false");   // 是否需要限制信用卡支付

        // 准备商品信息
        /*List<Goods> goodsList = new ArrayList<Goods>();
        goodsList.add(new Goods("0001", "充值0.01元", 1L, 100L, "Auto", "充值0.01元"));
        goodsList.add(new Goods("0002", "Goods Name", 2L, 200L, "Auto", "goods body"));

        json.put("goodsList", goodsList);*/
        System.out.println("请求报文:\n"+json);

        String url = "https://api-mop.chinaums.com/v1/netpay/bills/get-qrcode";
        String send = send(url, json.toString());

        System.out.println("返回结果:\n"+send);
        return send;
    }



    //获取订单状态
    public static String paystatus(String ordernum) throws Exception {

        /* post参数,格式:JSON */
        JSONObject json = new JSONObject();
        json.put("msgId", "001");   // 消息Id,原样返回
        json.put("requestTimestamp", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));    // 报文请求时间
        json.put("srcReserve", "请求系统预留字段"); // 请求系统预留字段
        json.put("mid", "89837084900W519"); // 商户号
        json.put("tid", "86864544");    // 终端号
        json.put("instMid", "QRPAYDEFAULT"); // 业务类型
        json.put("billNo", ordernum); // 商户订单号
        json.put("billDate", DateFormatUtils.format(new Date(), "yyyy-MM-dd")); // 账单日期


        System.out.println("请求报文:\n"+json);

        String url = "https://api-mop.chinaums.com/v1/netpay/bills/query";
        String send = send(url, json.toString());

        System.out.println("返回结果:\n"+send);



        return send;
    }


    /**
     * 发送请求
     *
     * @param url    eg:http://58.247.0.18:29015/v1/netpay/bills/get-qrcode
     * @param entity
     * @return
     * @throws Exception
     */
    public static String send(String url, String entity) throws Exception {
        authorization = getOpenBodySig(appId, appKey, entity);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Authorization", authorization);
        StringEntity se = new StringEntity(entity, "UTF-8");
        se.setContentType("application/json");
        httpPost.setEntity(se);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity1 = response.getEntity();
        String resStr = null;
        if (entity1 != null) {
            resStr = EntityUtils.toString(entity1, "UTF-8");
        }
        httpClient.close();
        response.close();
        return resStr;
    }

    /**
     * open-body-sig方式获取到Authorization 的值
     *
     * @param appId
     * @param appKey
     * @param body
     * @return
     * @throws Exception
     */
    public static String getOpenBodySig(String appId, String appKey, String body) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());   // eg:20190227113148
        String nonce = UUID.randomUUID().toString().replace("-", ""); // eg:be46cd581c9f46ecbd71b9858311ea12
        byte[] data = body.getBytes("UTF-8");
        System.out.println("data:\n" + body);
        InputStream is = new ByteArrayInputStream(data);
        String bodyDigest = testSHA256(is); // eg:d60bc3aedeb853e2a11c0c096baaf19954dd9b752e48dea8e919e5fb29a42a8d
        System.out.println("bodyDigest:\n" + bodyDigest);
        String str1_C = appId + timestamp + nonce + bodyDigest; // eg:f0ec96ad2c3848b5b810e7aadf369e2f + 20190227113148 + be46cd581c9f46ecbd71b9858311ea12 + d60bc3aedeb853e2a11c0c096baaf19954dd9b752e48dea8e919e5fb29a42a8d

        System.out.println("str1_C:" + str1_C);

//        System.out.println("appKey_D:\n" + appKey);

        byte[] localSignature = hmacSHA256(str1_C.getBytes(), appKey.getBytes());

        String localSignatureStr = Base64.encodeBase64String(localSignature);   // Signature
        System.out.println("Authorization:\n" + "OPEN-BODY-SIG AppId=" + "\"" + appId + "\"" + ", Timestamp=" + "\"" + timestamp + "\"" + ", Nonce=" + "\"" + nonce + "\"" + ", Signature=" + "\"" + localSignatureStr + "\"\n");
        return ("OPEN-BODY-SIG AppId=" + "\"" + appId + "\"" + ", Timestamp=" + "\"" + timestamp + "\"" + ", Nonce=" + "\"" + nonce + "\"" + ", Signature=" + "\"" + localSignatureStr + "\"");
    }

    /**
     * 进行加密
     *
     * @param is
     * @return 加密后的结果
     */
    private static String testSHA256(InputStream is) {
        try {
//            System.out.println(is.hashCode());
            return DigestUtils.sha256Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     *
     * @param data
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] hmacSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }
}
