package com.epoint.xmz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.alibaba.druid.support.json.JSONUtils;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.esotericsoftware.minlog.Log;
import com.sure.signserver.SureCSPAPI;
import com.sure.signserver.util.BasicDefine;
import com.sure.signserver.util.ErrCode;
import com.tzwy.util.FileUtils;
import com.tzwy.util.HttpRequestUtil;
import com.tzwy.util.SM.SM3Utils;
import com.tzwy.util.orig.OrigFilePath;
import com.tzwy.util.orig.OrigUtils;

/**
 * 非电子证照签章接口(PDF、OFD)，位置、骑缝章
 * 
 *先执行demo进行签章验证，签章成功后开始对接业务系统
 * 
 *对接流程如下：
 * 1、先对接签章接口，确保签章接口能够正常签章
 * 2、对接确信平台并申请数字证书(数字证书发送给电子签章系统)
 * 3、通过确信平台生成签名值并传递给电子签章系统，验证签名值
 * 4、签章成功后将签章文件发送给电子印章系统进行文件验证
 * 5、执行完以上4步后，具备正式环境对接条件
 */
public class HttpSignTest
{

    public static String url = "http://59.206.202.165:8080/yesign/sign/";
    public static String vurl = "http://59.206.202.165:8080/yesign/verify/";
    static String certId = "0a13907700dd43a3abcaa3d7e778cef1"; // 证书ID，正式环境该值传递空值
    public static String appkey = "";
    static String secretKey = ""; // 正式环境使用
    static String certDN = "";//

    /**
     * PDF
     * 处理签名请求报文
     * @param path
     * @return 
     * @throws Exception 
     */
    private static String handleSignParamPDF(String size, String Xcoord, String Ycoord, String appId, String path,
            String fileName, String sealId, String type) throws Exception {
        IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

        HashMap m1 = new LinkedHashMap();
        HashMap content = new LinkedHashMap(); // 业务参数
        byte[] base = FileUtils.readFileTobytes(path);
        content.put("fileData", Base64.toBase64String(base));
        content.put("appId", appId);
        content.put("certId", certId);
        content.put("fileName", fileName);

        // 生成原文hash和文件目录hash
        OrigFilePath origFilePath = OrigUtils.createOrigAndRange(path);
        // 原文hash值(通过确信将原文hash生成签名值（byte类型）)
        byte[] orighash = origFilePath.getOrig();
        //system.out.println("orighash---" + Base64.toBase64String(orighash));
        // --------------------------
        int result = 0;
        SureCSPAPI handle = new SureCSPAPI();

        // 从配置文件中获取许可证名称
        String surecryptocfgurl = "SureCryptoCfg.xml";
        // 获取许可证文件路径
        String surepath = ClassPathUtil.getClassesPath() + surecryptocfgurl;

        // 建立链接
        result = handle.InitServerConnect(surepath);
        if (result != ErrCode.ERR_SUCCESS) {
            //system.out.println("InitServerConnect error, code = " + result);
//            System.exit(0);
        }
//		byte[] plainData = "Hello world.".getBytes();
        ByteArrayOutputStream byteos = new ByteArrayOutputStream();
        result = handle.SVS_PKCS1SignData(BasicDefine.ALG_ASYM_SM2, orighash, byteos, "SdJnSpjYCSL");
        //system.out.println("SVS_PKCS1_SignData = " + result);
        byte[] bb = byteos.toByteArray();

        result = handle.SVS_PKCS1VerifyData("SdJnSpjYCSL", BasicDefine.ALG_ASYM_SM2, byteos.toByteArray(), orighash);

        byteos.close();
        //system.out.println("SVS_PKCS1_VerifyData = " + result);

        // 释放连接
        result = handle.FinalizeServerConnect();
        //system.out.println("FinalizeServerConnect = " + result);
        //system.out.println("bb------" + bb);

        /**
         * 文件追溯
         * 原文hash值的数字签名
         * 1、通过调用确信平台并将原文hash值传递给确信，由确信平台生成原文hash值的数字签名（byte类型）
         * 2、将生成的数字签名作为参数传递给签章系统
         *
         * 传递给签章系统的签名值为16进制(Hex.toHexString(signValue)
         */
        // -----------
        //system.out.println("Hex.toHexString(bb)-----" + Hex.toHexString(bb));
        content.put("fileSign", Hex.toHexString(bb));// 签名值 16进制
        content.put("filePaths", origFilePath.getFilePaths());// 文件目录

        if ("conn".equals(type)) {
            content.put("connPage", "6");// 骑缝页数
            content.put("connX", "10");// 签章坐标x
            content.put("connY", "120");// 签章坐标Y
            content.put("certDN", certDN);// 签名延签服务器 公钥证书DN项 可为空
            content.put("secretKey", secretKey);// 签名验签服务器 秘钥索引 可为空
            content.put("sealId", sealId);
        }
        else {
            List list = new ArrayList();
            HashMap data = new LinkedHashMap();
            data.put("sealId", sealId);
            data.put("pageNum", size);
            data.put("zx", Integer.parseInt(Xcoord));
            data.put("zy", Integer.parseInt(Ycoord));
            data.put("certDN", certDN);
            data.put("secretKey", secretKey);
            data.put("keyPin", "");
            list.add(data);
            content.put("signList", list);

            String re1 = JSONUtils.toJSONString(content);
            m1.put("message_header", handleHeader(re1));
            m1.put("message_content", re1);
            String re = JSONUtils.toJSONString(m1);
            return re;
        }
        return type;
    }

    /**
     * 处理签名请求报文
     * @param path
     * @return
     * @throws  
     */
    private static String handleVerifyParam(String path, String type) throws IOException {
        HashMap m1 = new LinkedHashMap();
        HashMap content = new LinkedHashMap(); // 业务参数
        byte[] base = FileUtils.readFileTobytes(path);
        content.put("file", Base64.toBase64String(base));
        content.put("type", type);
        String re1 = JSONUtils.toJSONString(content);
        m1.put("message_header", handleHeader(re1));
        m1.put("message_content", re1);
        String re = JSONUtils.toJSONString(m1);
        return re;
    }

    /**
     * 处理头参数
     * @param re1
     * @return
     * @throws IOException 
     */
    private static HashMap handleHeader(String re1) throws IOException {
        HashMap header = new LinkedHashMap();
        // 生成数字签名
        byte[] bb = sure.qxszqm(re1.getBytes("UTF-8"));
        /**签名值由确信生成，用于确认业务系统身份
         * 1、首次对接时，签名值传递空值，后台不做验证。
         * 2、签章接口对接成功后，对接确信平台生成数字签名并申请数字证书，并将数字证书发送给电子印章系统，联调测试。
         * 3、对接正式环境时向确信申请正式环境数字证书并发送给电子印章系统，注册业务系统。
         * 
         * 签名值生成过程：原文(string)->转成bytes[]类型->生成签名值(byte[])->转成base64(传递参数)
         * 
         * 数字签名
         * base64字节
         * PKCS1 验签
         */
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        String nowTime = smf.format(new Date());

        header.put("sign", Base64.toBase64String(bb));// 数字签名base64字节
        header.put("ctime", nowTime);

        header.put("version", "1.0");

        String mac = null;
        try {
            mac = SM3Utils.hmac(appkey, re1);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        header.put("hmac", mac);
        return header;
    }

    /**
     * PDF签章接口
     * @throws Exception 
     */
    public static String signPdf(String size, String Xcoord, String Ycoord, String sealId, String appid,
            String clientGuid, String type) throws Exception {
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        List<FrameAttachInfo> attachs = iAttachService.getAttachInfoListByGuid(clientGuid);
        FrameAttachInfo attach = null;
        ByteArrayInputStream inputStream = null;
        String result = "";
        String filepath = "";
        if (!attachs.isEmpty()) {
            attach = attachs.get(0);
        }
        try {
            if (attach != null) {
                Map<String, Object> param = new HashMap<>();
                String res = "";
                byte[] base;
                FrameAttachStorage storage = iAttachService.getAttach(attach.getAttachGuid());
                base = readStream(storage.getContent());

                filepath = savefile(attach, base);
                if (!"error".equals(filepath)) {
                    String params = handleSignParamPDF(size, Xcoord, Ycoord, appid, filepath,
                            storage.getAttachFileName(), sealId, type);
                    if ("position".equals(type)) {
                        res = HttpRequestUtil.sendJsonPost(url + "signPdf", params, false);
                    }
                    else if ("conn".equals(type)) {
                        res = HttpRequestUtil.sendJsonPost(url + "signPdfConn", params, false);
                    }
                    else {
                        res = HttpRequestUtil.sendJsonPost(url + "signPdfKeyword", params, false);
                    }
                    Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);
                    if (null != re && !re.isEmpty()) {
                        String errorCode = (String) re.get("errorCode");
                        if ("0".equals(errorCode)) {
                            String base64 = (String) re.get("file");
                            base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
                            byte[] fileData = Base64.decode(base64);
                            inputStream = new ByteArrayInputStream(fileData);
                            attach.setAttachLength(Long.valueOf((long) fileData.length));
                            iAttachService.updateAttach(attach, inputStream);
                            Log.info("电子签章附件修改成功！");
                            result = "签章完成";
                        }
                        else {
                            result = "fail" + re.get("errorText");
                        }
                    }
                    else {
                        result = "请求异常";
                    }
                }
                else {
                    result = "附件转换失败";
                }
            }
            else {
                result = "证照正本或副本附件未找到";
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "请求异常";
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(filepath);
            // 上传完成后删除文件
            if (file.isFile() && file.exists()) {
//				file.delete();
            }

        }

    }

    /**
     * 验证接口
     * @throws IOException 
     */
    public static void verifyFile() throws IOException {
        Map<String, Object> param = new HashMap<>();
//		String params =  handleVerifyParam("D:\\test234.pdf","pdf");
        String params = handleVerifyParam("D:\\test234.pdf", "pdf");
        String res = HttpRequestUtil.sendJsonPost(vurl + "verifyFile", params, false);
        Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);

        if (null != re && !re.isEmpty()) {
            String errorCode = (String) re.get("errorCode");
            if ("0".equals(errorCode)) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) re.get("data");
                if (null != list && list.size() > 0) {
                    for (Map<String, Object> m : list) {
                        //system.out.println("id:" + m.get("id"));
                        //system.out.println("availability:" + m.get("availability"));
                        //system.out.println("name:" + m.get("name"));
                        //system.out.println("index:" + m.get("index"));
                        //system.out.println("formatErrorCode :" + m.get("formatErrorCode"));
                        //system.out.println("info :" + m.get("info"));
                    }
                }
                else {
                    //system.out.println("not data");
                }
                //system.out.println("完成");
            }
            else {
                //system.out.println("fail" + re.get("errorText"));
            }
        }
        else {
            //system.out.println("请求异常");
        }

    }

    /**
       * 得到图片字节流 数组大小
       * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static String savefile(FrameAttachInfo attach, byte[] datas) {
        String ofdFilepath = ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/"
                + UUID.randomUUID().toString() + attach.getAttachFileName();

        File files = FileManagerUtil.createFile(ClassPathUtil.getDeployWarPath() + "epointcert/resultfile/zhengzhao/");
        // 如果文件夹不存在则创建
        if (!files.exists()) {
            files.mkdirs();
        }

        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(datas);
            convertInputStreamToFileNio(input, ofdFilepath);
            return ofdFilepath;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        finally {
            try {
                if (input != null) {
                    input.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void convertInputStreamToFileNio(InputStream is, String outputFile) throws IOException {
        File dest = new File(outputFile);
        Files.copy(is, dest.toPath());
    }

}
