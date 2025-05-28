package com.epoint.hcp.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

@RestController
@RequestMapping("/lsjmm")
public class LSJmmController
{

    @Autowired
    private IAuditQueue auditqueueservice;

    @Autowired
    private IHandleQueue handlequeueservice;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private IAuditOrgaServiceCenter centerservice;

    @Autowired
    private IAuditOrgaHall hallservice;
    @Autowired
    private IOuService ouservice;
    /**
     * 请求ID参数(唯一参数,使用UUID生成即可)
     **/

    public static final String AEP_NONCE = "x-sop-nonce";

    /**
     * appkey参数(应用appkey)
     */

    public static final String AEP_APPKEY = "x-sop-pinId";

    /**
     * 时间戳参数
     */

    public static final String AEP_TIMESTAMP = "x-sop-timestamp";

    /**
     * 签名参数
     **/

    public static final String AEP_SIGNATURE = "x-sop-signature";

    /**
     * 网关地址
     */
    // final static String AEP_GATE_URL =
    // "https://jst.jiningdq.cn/ebs/api/gtxdjxxzwqh";
    final static String AEP_GATE_URL = "http://172.20.58.5:7090/ebs/api/gtxdlsxzwqh";
    final static String appKeyValue = "gtxdlsxzwqh";
    final static String acceptOrgToken = "0277ca15d7cc4410";
    final static String acceptOrgId = "370832000000063";
    final static String appSecretValue = "d6c14a91ae9b41959fe9433e14989d58";
    final static String privateKey = "D29262892372154FA8E63D3CB14C95F7FB3B25A2205605A34414F606812870E6";
    final static String publicKey = "045E65BA2F5713C862395E2F2564249AA70EB9C8B00D8F17FCC016F0434264FDF9A2D6F74AAD37DBCA9770010C0729822B7A79EBC43FEA7D1C5D7A499DC1F2A724";

    private static Logger log = Logger.getLogger(LSJmmController.class);

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            String ewmcount = object.getString("ewmcount");
            JSONObject dataJson = new JSONObject();
            JSONObject entity = new JSONObject();
            entity.put("acceptOrgId", acceptOrgId);
            entity.put("codeContent", ewmcount);
            log.info(AEP_GATE_URL + "=======getUserInfo ：entity" + entity.toJSONString());
            String backString = post("/idcodeCore/api/gateway/decode", entity);
            // String backString =
            // "{'Response':{'RequestId':'1a6bfff13d81a8e9','Error':null,'Data':{'userNameCyp':'047F8545D8D253F2E07E569AE9E9C87C1C765797353D5AFC9C41A545C000ADE6466E2913315286C3906D8B9FA1F3A8CE55798DB0BAA9D0A676DF42109E402A2E6783250AC0552CCFDD99452CC04616DA363CDCD5E204384AD6E66B9B50E3FB9957F809AC6B74E8','mobileCyp':'041B82D6D1F96D52C7976F697D7DB690D17B64D42E40B2A6EBE657C63D781B29277F86AD5F7F826FCD3489EC5C773795CB27F903794E600752A5B851BE35613B83507426A2AABF4FB3F872E0F14AE611F1192436B558C6FF5EA3626F0DBB42D90954BE837BE1A5F60998639E','certNoCyp':'042D687C8B8CEE691A987BDAA7F81F8E3BF3AE80F593148A5DA6D1E784F536884746F8E39C1963B469B6A73EC9EC5E5ED06BA5C77E3CAB77A55CCBCA35E681DF2B0B1E22FAEE5B938C3594FE5C9A81E1E5B20CFF96C31ACDB664EA40DD180D5DF6CCCCD288BC549DD0A672CD715F24F62B3636','certTypeCyp':'04205218A39E3E2AC8A65982026F8877EB68C740689253A43041EE3D9CB6315E686C0FD1186D789C8484EEB1AC30BA13903432B85C83AA640977C425BEECF6CF8A3EBE169F14FD3247BFE48075CCFE7D26A039EBDFD1B4B04D3FA2DC874DACB649FE','msgToken':'8811efc44db2447e8a499cd1c255ce28','tokenExpireTime':'7200','genExtinfo':null}}}";
            log.info("=======getUserInfo ：" + backString);
            JSONObject backJson = JSONObject.parseObject(backString);
            JSONObject Response = backJson.getJSONObject("Response");
            if (StringUtil.isNotBlank(Response.getString("Error"))) {
                return JsonUtils.zwdtRestReturn("1", "获取人员信息失败", Response.getJSONObject("Error").getString("Message"));
            }
            if (StringUtil.isNotBlank(Response.getString("Data"))) {
                String userNameCyp = Response.getJSONObject("Data").getString("userNameCyp");
                String mobileCyp = Response.getJSONObject("Data").getString("mobileCyp");
                String certNoCyp = Response.getJSONObject("Data").getString("certNoCyp");
                String certTypeCyp = Response.getJSONObject("Data").getString("certTypeCyp");
                dataJson.put("username", sm2Decrypt(privateKey, userNameCyp));
                dataJson.put("mobile", sm2Decrypt(privateKey, mobileCyp));
                dataJson.put("sfz", sm2Decrypt(privateKey, certNoCyp));
                dataJson.put("certtype", sm2Decrypt(privateKey, certTypeCyp));

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取人员信息失败,未获取到人员信息", "");
            }
            return JsonUtils.zwdtRestReturn("1", "获取人员信息成功", dataJson.toString());
        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "调用失败" + e, "");
        }
    }

    @RequestMapping(value = "/sendjmmQueueMessage", method = RequestMethod.POST)
    public String sendjmmQueueMessage(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject object = (JSONObject) jsonObject.get("params");
            String qno = object.getString("qno");
            String username = object.getString("username");
            String centerguid = object.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            JSONObject numExtInfo = new JSONObject();
            JSONObject entity = new JSONObject();
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum,getnotime,hallname ";
            AuditQueue auditqueue = auditqueueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (auditqueue != null) {
                AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(centerguid).getResult();
                if (center != null) {
                    numExtInfo.put("serviceHallName", center.getCentername());
                }
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (auditqueuetasktype != null) {
                    numExtInfo.put("itemName", auditqueuetasktype.getTasktypename());
                    FrameOu ou = ouservice.getOuByOuGuid(auditqueuetasktype.getOuguid());
                    if (ou != null) {
                        numExtInfo.put("acceptUnit", ou.getOuname());
                    }
                }
                AuditOrgaHall hall = hallservice.getAuditHallByRowguid(auditqueue.getHallguid()).getResult();
                if (hall != null) {
                    numExtInfo.put("processArea", hall.getHallname());
                }
                else {
                    numExtInfo.put("processArea", "市政务大厅");
                }

                numExtInfo.put("queueTime", auditqueue.getGetnotime());
                numExtInfo.put("processWindow", auditqueue.getHandlewindowno());
                numExtInfo.put("queueNo", qno);
                numExtInfo.put("remark", "");
                numExtInfo.put("lvl", "");
                numExtInfo.put("windowNo", auditqueue.getHandlewindowno());

                numExtInfo.put("personName", username);
                numExtInfo.put("waitCount",
                        handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult());
                numExtInfo.put("hallPhone", "");
                numExtInfo.put("hallBusRoute", "");
                numExtInfo.put("hallSubwayRoute", "");
                numExtInfo.put("hallAddress", "");
                entity.put("certNoCyp", sm2encrypt(auditqueue.getIdentitycardnum(), publicKey));
                entity.put("bizSerNo", auditqueue.getFlowno());
            }

            entity.put("acceptOrgId", acceptOrgId);
            entity.put("numExtInfo", numExtInfo);
            log.info(AEP_GATE_URL + "======= sendjmmQueueMessage ：entity" + entity.toJSONString());
            String backString = post("/idcodeCore/api/gateway/numResultNotify", entity);
            log.info("=======sendjmmQueueMessage ：" + backString);

            return JsonUtils.zwdtRestReturn("1", "发送号码信息成功", dataJson.toString());
        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "调用失败" + e, "");
        }
    }

    public static String post(String url, JSONObject jsonObject) {
        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(AEP_GATE_URL.concat(url));
            httpPost.addHeader(AEP_NONCE, uuid);
            httpPost.addHeader(AEP_APPKEY, appKeyValue);
            httpPost.addHeader(AEP_TIMESTAMP, timestamp);
            httpPost.addHeader(AEP_SIGNATURE, getBody(uuid, timestamp));
            // 解决中文乱码问题
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                params.add(pair);
            }
            stringEntity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "UTF-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getBody(String uuid, String time) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put(AEP_NONCE, uuid);
        objectMap.put(AEP_APPKEY, acceptOrgToken);
        objectMap.put(AEP_TIMESTAMP, time);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(time).append(acceptOrgToken).append(uuid).append(time);
        // hamcsm3算法
        HMac hmac = new HMac(HmacAlgorithm.HmacSM3, acceptOrgToken.getBytes());
        return hmac.digestHex(stringBuffer.toString());
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        final StringBuffer content = new StringBuffer();
        final List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        int size = keys.size();
        for (int i = 0; i < size; ++i) {
            final String key = keys.get(i);
            final String value = sortedParams.get(key);
            if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(value)) {
                content.append(((index == 0) ? "" : "&") + key + "=" + value);
                ++index;
            }
        }
        return content.toString();
    }

    public static String hmacSm3Str(final String key, final String data) {
        try {
            return SmUtil.hmacSm3(key.getBytes(StandardCharsets.UTF_8)).digestHex(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * public static void main(String[] args) {
     * String privateKey =
     * "D29262892372154FA8E63D3CB14C95F7FB3B25A2205605A34414F606812870E6";
     * String publicKey =
     * "045E65BA2F5713C862395E2F2564249AA70EB9C8B00D8F17FCC016F0434264FDF9A2D6F74AAD37DBCA9770010C0729822B7A79EBC43FEA7D1C5D7A499DC1F2A724";
     * String encrypt = sm2encrypt("张三", publicKey);
     * System.out.println("加密串：" + encrypt);
     * String sm2Decrypt = sm2Decrypt(privateKey, encrypt);
     * System.out.println("解密结果：" + sm2Decrypt);
     * }
     */

    public static String sm2Decrypt(String privateKey, String data) {
        SM2 sm2 = SmUtil.sm2(hexToByte(privateKey), null);
        // 公钥加密，私钥解密
        return StrUtil.utf8Str(sm2.decryptFromBcd(data, KeyType.PrivateKey));
    }

    public static String sm2encrypt(String data, String publicKey) {
        SM2 sm2 = SmUtil.sm2(null, hexToByte(publicKey));
        return sm2.encryptBcd(data, KeyType.PublicKey);
    }

    /**
     * 16进制转换成byte
     *
     * @param hexStr
     *            hexStr
     * @return String
     */
    public static byte[] hexToByte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
