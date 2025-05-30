package com.epoint.zwdt.zwdtrest.project.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.wjw.api.ICxBusService;
import okhttp3.*;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公用水务接口封装
 */
public class GongYongShuiWuUtil {
    /**
     * 日志对象
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取公用水务平台地址
     */
    private static String gongYongShuiWuUrl = ConfigUtil.getConfigValue("duijie","gongYongShuiWuUrl");
    /**
     * 公用水务平台的公钥字符串
     */
    private static String gyswpublickey = ConfigUtil.getConfigValue("duijie", "gyswpublickey");
    /**
     * 公用水务平台的orgid
     */
    private static String orgid = ConfigUtil.getConfigValue("duijie", "orgid");
    /**
     * 济南公钥字符串
     */
    private static String jnpublickey = ConfigUtil.getConfigValue("duijie", "jnpublickey");
    /**
     * 济宁私钥字符串
     */
    private static String jnprivatekey = ConfigUtil.getConfigValue("duijie", "jnprivatekey");


    /**
     * RSA 加密
     *
     * @param message   需要加密的数据
     * @return
     * @throws Exception
     */
    public static String encryptNew(String message) {
        // 将公钥字符串转换为 PublicKey 对象
        byte[] publicBytes = Base64.getDecoder().decode(gyswpublickey);
        // 将公钥字符串转换为 PublicKey 对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = null;
        PublicKey pubKey = null;
        int MAX_DATA_LENGTH = 245;

        // 创建 Cipher 对象进行加密
        Cipher cipher = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpec);
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 使用 PKCS1Padding
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            // 初始化一个 StringBuilder 来存储加密后的结果
            StringBuilder encryptedResult = new StringBuilder();

            // 分割数据
            int index = 0;
            while (index < message.length()) {
                int end = Math.min(index + MAX_DATA_LENGTH, message.length());
                String part = message.substring(index, end);
                byte[] partBytes = part.getBytes("UTF-8"); // 使用 UTF-8 编码

                // 确保分段后的字节数不超过最大长度
                while (partBytes.length > MAX_DATA_LENGTH && index < message.length()) {
                    part = part.substring(0, part.length() - 1); // 减少一个字符
                    partBytes = part.getBytes("UTF-8");
                }

                byte[] encryptedPart = cipher.doFinal(partBytes);
                encryptedResult.append(Base64.getEncoder().encodeToString(encryptedPart));
                index += part.length();
            }

            // 返回最终的加密结果
            return encryptedResult.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("==加密数据异常===");
            return "";
        }
    }



    /**
     * 加密数据 使用公用水务平台提供的公钥加密数据
     */
    public static String encrypt(String data)
    {
        // 最大允许的数据长度
        int maxDataLength = 190;
        // 将公钥字符串转换为 PublicKey 对象
        byte[] keyBytes = Base64.getDecoder().decode(gyswpublickey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        PublicKey publicKey = null;
        // 创建 Cipher 对象进行加密
        Cipher cipher = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 初始化一个 StringBuilder 来存储加密后的结果
            StringBuilder encryptedResult = new StringBuilder();
            // 分割数据
            for (int i = 0; i < data.length(); i += maxDataLength) {
                int end = Math.min(i + maxDataLength, data.length());
                String part = data.substring(i, end);
                byte[] partBytes = part.getBytes();
                if (partBytes.length > maxDataLength) {
                    // 如果某个分段的字节数超过190字节，则继续分割
                    int remainingLength = partBytes.length - maxDataLength;
                    byte[] firstPartBytes = new byte[maxDataLength];
                    System.arraycopy(partBytes, 0, firstPartBytes, 0, maxDataLength);
                    byte[] encryptedPart = cipher.doFinal(firstPartBytes);
                    encryptedResult.append(Base64.getEncoder().encodeToString(encryptedPart));
                    // 继续处理剩余的部分
                    while (remainingLength > 0) {
                        byte[] nextPartBytes = new byte[Math.min(remainingLength, maxDataLength)];
                        System.arraycopy(partBytes, maxDataLength, nextPartBytes, 0, nextPartBytes.length);
                        byte[] encryptedNextPart = cipher.doFinal(nextPartBytes);
                        encryptedResult.append(Base64.getEncoder().encodeToString(encryptedNextPart));
                        remainingLength -= maxDataLength;
                    }
                } else {
                    byte[] encryptedPart = cipher.doFinal(partBytes);
                    encryptedResult.append(Base64.getEncoder().encodeToString(encryptedPart));
                }
            }
            // 返回最终的加密结果
            return encryptedResult.toString();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("==加密数据异常===");
            return "";
        }
    }

    /**
     * 签名
     * 使用自己的私钥对数据进行签名
     * @param data
     * @return
     */
    public static String sign(String data) {
        try{
            byte[] keyBytes = Base64.getDecoder().decode(jnprivatekey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            return Base64.getEncoder().encodeToString(signature.sign());
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("签名异常");
            return "";
        }
    }

    /**
     * 用自己的私钥解密
     */
    public static String decryptData(byte[] encryptedData) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jnprivatekey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(encryptedData));
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("解密数据异常");
            return "";
        }
    }


    /**
     * 业务提交
     * @param
     */
    public static String submit(Record data, AuditProject auditProject) {
        String platformid = "";// 网厅平台业务ID
        String requesturl = gongYongShuiWuUrl+"/netbus/thirdpart/buss";
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "multipart/form-data");
        JSONObject params = new JSONObject();
        JSONObject req = new JSONObject();
        // 业务数据
        req.put("data", data.get("result"));
        req.put("busstype", data.getStr("busstype"));// 业务类型 固定传1
        req.put("time", System.currentTimeMillis());
        req.put("source", "GGDJ");
        req.put("bussid", auditProject.getFlowsn());//业务id 传audit_project的flowsn
        req.put("userid", auditProject.getCertnum());// 用户id
        params.put("orgid", orgid);
        log.info("加密前:"+req.toJSONString());
        String jqString = "";
        try {
            jqString = RsaUtil.encrypt(req.toJSONString(), gyswpublickey, RsaUtil.ENCRYPT_BLOCK_2048);
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("req", jqString);
        log.info("加密后:"+jqString);
        //params.put("sign", sign(req.toJSONString()));
        JSONObject material = new JSONObject();
        material = data.get("material");
        // 遍历 material
        for (String key : material.keySet()) {
            JSONArray fileList = material.getJSONArray(key);
            params.put(key, fileList);
        }
        log.info("========== 开始调用业务提交接口 ========== requesturl:"+requesturl+" params"+params.toString());
        String resultStr = doHttpPost(requesturl, params);
        log.info("接口返回值--->" + resultStr);
        if (StringUtil.isBlank(resultStr)) {
            log.info("未获取到接口返回值");
            return "";
        }
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String status = resultJson.getString("status");
        if ("1".equals(status)) {
            if(resultJson.containsKey("extend")){
                platformid = resultJson.getJSONObject("extend").getString("platformid");
                log.info("解密前 平台业务id为："+platformid);
                platformid = decryptData(Base64.getDecoder().decode(platformid));
                log.info("解密后 平台业务id为："+platformid);
            }
        } else {
            // 业务提交失败
            log.info("业务提交失败 原因："+resultJson.getString("message"));
        }
        return platformid;
    }

    /**
     * 用水企业报装 (电子表单数据)
     * @return
     */
    public static Record ysdata(String subAppGuid, AuditProject auditProject,String tableName) {
        Record record = new Record();
        log.info("=======开始执行ysdata");
        log.info("subAppGuid--->" + subAppGuid);
        ICxBusService cxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        IAuditProjectMaterial iAuditProjectMaterial = ContainerFactory.getContainInfo().getComponent(IAuditProjectMaterial.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        Record formDetail = cxBusService.getDzbdDetail(tableName, subAppGuid);
        log.info("查询结果--->" + formDetail);
        // 将表单数据放入result
        JSONObject result = new JSONObject();
        if(formDetail!=null){
            result.put("field1", formDetail.getStr("field1"));// 所属分公司	SELECT	必填
            result.put("field2", formDetail.getStr("field2"));// 用户类型	SELECT	必填
            result.put("field3", formDetail.getStr("field3"));// 用户名称	TEXT	必填
            result.put("field4", formDetail.getStr("field4"));// 证件号码	SELECT	必填
            result.put("field5", formDetail.getStr("field5"));// 用水地址	TEXT	必填
            result.put("field6", formDetail.getStr("field6"));// 用水类别	SELECT	必填
            result.put("field7", formDetail.getStr("field7"));// 水表口径	SELECT	必填
            result.put("field8", formDetail.getStr("field8"));// 法人姓名	TEXT	必填
            result.put("field9", formDetail.getStr("field9"));// 联系电话	TEXT	必填
            result.put("field10", formDetail.getStr("field10"));// 备注	TEXT	必填
            result.put("field13", formDetail.getStr("field13"));// 社会统一信用代码	TEXT	必填
        }

        // 材料
        JSONObject material = new JSONObject();
        JSONArray field11 = new JSONArray();// 相关文件
        JSONArray Field14 = new JSONArray();// 不动产权证或规划许可证
        JSONArray Field15 = new JSONArray();// 申报资料
        JSONArray Field16 = new JSONArray();// 营业执照
        JSONArray Field17 = new JSONArray();// 法人身份证正反面
        log.info("根据projectGuid查询AuditProjectMaterial");
        List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                .selectProjectMaterial(auditProject.getRowguid()).getResult();
        log.info("查询出的materialList--->" + materialList);
        if (ValidateUtil.isNotBlankCollection(materialList)) {
            log.info("附件list不为空");
            for (AuditProjectMaterial auditProjectMaterial : materialList){
                List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                log.info("查询出cliengguid为" + auditProjectMaterial.getCliengguid() +" 材料名称："+ auditProjectMaterial.getTaskmaterial() + "对应的附件list--->" + attachList);
                if (ValidateUtil.isNotBlankCollection(attachList)) {
                    log.info("附件list不为空");
                    // TODO 这边不知道材料名称能不能对上
                    if("不动产权证或规划许可证".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                Field14.add(fileBase64);
                            }
                        }
                    }
                    else if("申报资料".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                Field15.add(fileBase64);
                            }
                        }
                    }
                    else if("营业执照".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                Field16.add(fileBase64);
                            }
                        }
                    }
                    else if("法人身份证正反面".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                Field17.add(fileBase64);
                            }
                        }
                    }
                    else if("相关文件".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field11.add(fileBase64);
                            }
                        }
                    }
                }
            }
        }
        material.put("Field14", Field14);// 不动产权证或规划许可证	FILE	必填
        material.put("Field15", Field15);// 申报资料	FILE	必填
        material.put("field16", Field16);// 营业执照	FILE	必填
        material.put("field17", Field17);// 法人身份证正反面	FILE	必填
        material.put("field11", field11);// 相关文件
        record.put("material", material);
        record.put("result", result);
        record.put("busstype", "YSBZGG");
        return record;
    }

    /**
     * 供热企业报装
     * @return
     */
    public static Record grdata(String subAppGuid, AuditProject auditProject,String tableName) {
        Record record = new Record();
        log.info("=======开始执行grdata");
        log.info("subAppGuid--->" + subAppGuid);
        ICxBusService cxBusService = ContainerFactory.getContainInfo().getComponent(ICxBusService.class);
        IAuditProjectMaterial iAuditProjectMaterial = ContainerFactory.getContainInfo().getComponent(IAuditProjectMaterial.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        // TODO 用水电子表单表名：
        Record formDetail = cxBusService.getDzbdDetail(tableName, subAppGuid);
        log.info("查询结果--->" + formDetail);
        // 将表单数据放入result
        JSONObject result = new JSONObject();
        result.put("field1", formDetail.getStr("field1"));//field1	所属分公司	SELECT	必填
        result.put("field2", formDetail.getStr("field2"));//field2	户主名称	TEXT	必填
        result.put("field10", formDetail.getStr("field10"));//field10	用热位置	GPSMAP	必填
        result.put("field3", formDetail.getStr("field3"));//field3	房屋产权面积（㎡）	TEXT	必填
        result.put("field4", formDetail.getStr("field4"));//field4	用热类型	SELECT	必填
        result.put("field5", formDetail.getStr("field5"));//field5	联系电话	TEXT	必填
        result.put("field6", formDetail.getStr("field6"));//field6	身份证号码	TEXT	必填
        result.put("field13", formDetail.getStr("field13"));//field13	社会统一信用代码	TEXT	必填
        // 材料
        JSONObject material = new JSONObject();
        JSONArray field7 = new JSONArray();// 总平面图
        JSONArray field8 = new JSONArray();// 不动产权证或规划许可证
        JSONArray field9 = new JSONArray();// 申报资料
        JSONArray field11 = new JSONArray();// 营业执照
        JSONArray field12 = new JSONArray();// 法人身份证正反面
        JSONArray field14 = new JSONArray();// 供热申请理由

        log.info("根据projectGuid查询AuditProjectMaterial");
        List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                .selectProjectMaterial(auditProject.getRowguid()).getResult();
        log.info("查询出的materialList--->" + materialList);
        if (ValidateUtil.isNotBlankCollection(materialList)) {
            log.info("附件list不为空");
            for (AuditProjectMaterial auditProjectMaterial : materialList){
                List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                log.info("查询出cliengguid为" + auditProjectMaterial.getCliengguid() +" 材料名称："+ auditProjectMaterial.getTaskmaterial() + "对应的附件list--->" + attachList);
                if (ValidateUtil.isNotBlankCollection(attachList)) {
                    log.info("附件list不为空");
                    // TODO 这边不知道材料名称能不能对上
                    if("总平面图".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)) {
                                field7.add(fileBase64);
                            }
                        }
                    }
                    else if("不动产权证或规划许可证".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field8.add(fileBase64);
                            }
                        }
                    }
                    else if("申报资料".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field9.add(fileBase64);
                            }
                        }
                    }
                    else if("营业执照".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field11.add(fileBase64);
                            }
                        }
                    }
                    else if("法人身份证正反面".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field12.add(fileBase64);
                            }
                        }
                    }
                    else if("供热申请理由".equals(auditProjectMaterial.getTaskmaterial())){
                        for (FrameAttachInfo frameAttachInfo : attachList){
                            // 根据附件attachGuid获取附件流
                            InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
                            String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
                            String fileBase64 = attachToBase64(content,mimeType);
                            if(StringUtil.isNotBlank(fileBase64)){
                                field14.add(fileBase64);
                            }
                        }
                    }
                }
            }
        }
        material.put("field7", field7);// 总平面图
        material.put("field8", field8);// 不动产权证或规划许可证
        material.put("field9", field9);// 申报资料
        material.put("field11", field11);// 营业执照
        material.put("field12", field12);// 法人身份证正反面
        material.put("field14", field14);// 供热申请理由
        record.put("material", material);
        record.put("result", result);
        record.put("busstype", "GRBZGGDJ");
        return record;
    }

    /**
     * 附件转base64
     */
    public static String attachToBase64(InputStream content,String mimeType) {
        String fileBase64 = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = content.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] bytes = outputStream.toByteArray();
        fileBase64 = Base64.getEncoder().encodeToString(bytes);
        return mimeType + ";base64," + fileBase64;
    }

    public static String getMimeTypeFromExtension(String fileName) {
        String extension = getExtension(fileName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "doc":
            case "docx":
                return "application/msword";
            case "xls":
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default:
                return "application/octet-stream";
        }
    }

    private static String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension
        }
        return fileName.substring(lastDotIndex + 1);
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个不验证证书链的 TrustManager
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // 安装所有信任的 TrustManager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 创建一个不验证主机名的 HostnameVerifier
            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(hostnameVerifier);

            return builder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static String doHttpPost(String url, JSONObject data) {
        String resultStr = "";
        OkHttpClient client = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value instanceof String) {
                bodyBuilder.addFormDataPart(key, (String) value);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                bodyBuilder.addFormDataPart(key, jsonArray.toString());
            }
        }
        RequestBody requestBody = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            resultStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    public static void main(String[] args) {
        // 测试加密
        System.out.println(encrypt("123456"));
    }
}
