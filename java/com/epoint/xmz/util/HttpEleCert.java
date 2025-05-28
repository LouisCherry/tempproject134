package com.epoint.xmz.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.sure.signserver.SureCSPAPI;
import com.sure.signserver.util.BasicDefine;
import com.sure.signserver.util.ErrCode;
import com.tzwy.util.orig.OrigFilePath;
import com.tzwy.util.*;
import com.tzwy.util.SM.SM3Utils;
import com.tzwy.util.orig.OrigUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电子证照接口测试-OFD文件
 * 
 * 先执行demo进行签章验证，签章成功后开始对接业务系统
 * 
 * 对接流程如下：
 * 1、先对接签章接口，确保签章接口能够正常签章
 * 2、对接确信平台并申请数字证书(数字证书发送给电子签章系统)
 * 3、通过确信平台生成签名值并传递给电子签章系统，验证签名值
 * 4、签章成功后将签章文件发送给电子印章系统进行文件验证
 * 5、执行完以上4步后，具备正式环境对接条件
 */
public class HttpEleCert {

    public static String url = "http://59.206.202.175:8082/yesign/eleCert/";
    static String sealId = "e52ec7eb8d574f79821628481c7c1f53";// 印章编号 
    static String certId = "0a13907700dd43a3abcaa3d7e778cef1";  //证书ID。电子签章接口：正式环境该值传空；数字签名接口：正式环境该值传印章编号
    static String appId = "2cb28a918021ffb70180503eec540017";// 业务系统ID
    public static String appkey = ""; // 
    static String secretKey = ""; //秘钥索引
    static String certDN = "";//
   
    public static void main(String[] args) throws IOException {
    	Security.addProvider(new BouncyCastleProvider());
		//delLockSigned();
//        lockSign();
//         signAndLock();//签章并锁定接口
          verifyLockSigned();//验签接口
       // watermarkAndLockSigned();
    }

    public static void test(){
        Map<String, Object> param = new HashMap<>();
        String params = JSONUtils.toJSONString(param);

        String res = HttpRequestUtil.sendJsonPost(url+"register",params, false);
        //system.out.println(res);
    }

    /**
     * 测试删除锁定签名
     */
    public static void delLockSigned(){
        LinkedHashMap<String,Object> m1 = new LinkedHashMap<>();
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();//业务参数
        content.put("fileData", Base64.toBase64String(FileUtils.readFileTobytes("E:\\sealsign\\sign\\lockSign.ofd")));
        content.put("appId", appId);
        String re1 = JSONUtils.toJSONString(content);
        m1.put("message_header", handleHeader(re1,appkey));
        
        m1.put("message_content",re1);
        String params =JSONUtils.toJSONString(m1);

        String res = HttpRequestUtil.sendJsonPost(url+"delLockSigned",params, false);
        //system.out.println(res);
        Map<String, Object> re = (Map<String, Object>) JSONUtils.parse(res);
        if((boolean) re.get("status")) {
            try {
                String base64 = (String) re.get("returnData");
                base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
                byte[] fileData = Base64.decode(base64);
                FileUtil.getFile(fileData, "E:\\sealsign\\sign\\", "delLockSigned.ofd");
            } catch (Exception e) {
                //system.out.println(e);
            }
        }
    }

    /**
     * 测试增加锁定签名
     */
    public static void lockSign(){
        LinkedHashMap<String,Object> m1 = new LinkedHashMap<>();
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();//业务参数
        content.put("fileData", Base64.toBase64String(FileUtils.readFileTobytes("D:\\123.ofd")));
        content.put("appId", appId);
        content.put("certId",certId);
        content.put("certDN",certDN);
        content.put("secretKey",secretKey);
        content.put("keyPin","");
        String re1 = JSONUtils.toJSONString(content);
        m1.put("message_header", handleHeader(re1,appkey));
        
        m1.put("message_content",re1);
        String params =JSONUtils.toJSONString(m1);

        String res = HttpRequestUtil.sendJsonPost(url+"lockSign",params, false);
        //system.out.println(res);
        Map<String, Object> re = (Map<String, Object>) JSONUtils.parse(res);
        if((boolean) re.get("status")) {
            try {
                String base64 = (String) re.get("returnData");
                base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
                byte[] fileData = Base64.decode(base64);
                FileUtil.getFile(fileData, "D:\\", "lockSign.ofd");
            }catch(Exception e) {
                //system.out.println(e);
            }
        }
    }

    /**
     * 测试签章并锁定签名接口
     * @throws IOException 
     */
    public static void signAndLock() throws IOException{
        LinkedHashMap<String,Object> m1 = new LinkedHashMap<>();
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();//业务参数
        content.put("fileData", Base64.toBase64String(FileUtils.readFileTobytes("D:\\已套红头.ofd")));
        content.put("appId",appId);//应用ID
        content.put("fileName","测试签章并锁定签名接口");

        //追责需要参数
        OrigFilePath origFilePath= OrigUtils.createOrigAndFilePaths("D:\\已套红头.ofd");
        byte[] orighash = origFilePath.getOrig();//该值由确信进行数字签名

        //生成确信数字签名
		byte[] bb = sure.qxszqm(orighash);
		/**
		 * 原文hash值的数字签名
		 * 1、通过调用确信平台并将原文hash值传递给确信，由确信平台生成原文hash值的数字签名（byte类型）
		 * 2、将生成的数字签名作为参数传递给签章系统
		 * 
		 * 传递给签章系统的签名值为16进制( Hex.toHexString(signValue))
		 */
        content.put("fileSign", Hex.toHexString(bb));//签名值  16进制
        content.put("filePaths", origFilePath.getFilePaths());

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> ma1 = new HashMap<String, Object>();
        ma1.put("pagenum", "0");
        ma1.put("sigzbh", sealId);
        ma1.put("certId",certId); //软证书ID，非空时使用软证书签名，空时使用签名设备（需要yesign配置文件中配置签名设备信息）
        ma1.put("zx", "50");
        ma1.put("zy", "50");
        ma1.put("certDN", certDN); //签名设备需要的配置，
        ma1.put("secretKey", secretKey);//签名索引，东进签名设备只需要的配置secretKey
        ma1.put("keyPin", "");
        ma1.put("ismain", ""); //主证书标识，如果是0标识为主证书，用于数字签名
        list.add(ma1);
        content.put("data", list);
        String re1 = JSONUtils.toJSONString(content);
        //第一个参数为HMAC原文，第二个参数为应用Secret，作为HMAC的秘钥
        m1.put("message_header", handleHeader(re1,appkey));
        
        m1.put("message_content",re1);
        String params =JSONUtils.toJSONString(m1);

        String res=HttpRequestUtil.sendJsonPost(url+"signAndLock",params, false);
        //system.out.println(res);
        Map<String, Object> re = (Map<String, Object>) JSONUtils.parse(res);
        if((boolean) re.get("status")) {
            try {
                String base64 = (String) re.get("returnData");
                base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
                byte[] fileData = Base64.decode(base64);
                FileUtil.getFile(fileData, "D:\\", "123.ofd");
            }catch(Exception e) {
                //system.out.println(e);
            }
        }

    }


    /**
     * 验签接口
     */
    public static void verifyLockSigned(){
        LinkedHashMap<String,Object> m1 = new LinkedHashMap<>();
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();//业务参数
        content.put("fileData", Base64.toBase64String(FileUtils.readFileTobytes("D:\\lockSign.ofd")));
        content.put("isSingle",false);
        content.put("appId",appId);
        String re1 = JSONUtils.toJSONString(content);
        m1.put("message_header", handleHeader(re1,appkey));
        
        m1.put("message_content",re1);
        String params =JSONUtils.toJSONString(m1);
        //system.out.println("请求参数： "+params);
        String res = HttpRequestUtil.sendJsonPost(url+"verifySigned",params, false);
        //system.out.println(res);
    }

    /**
     * 测试加注件并锁定签名
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static void watermarkAndLockSigned(){
        LinkedHashMap<String,Object> m1 = new LinkedHashMap<>();
        LinkedHashMap<String,Object> content = new LinkedHashMap<>();//业务参数
        content.put("fileData", Base64.toBase64String(FileUtils.readFileTobytes("E:\\sealsign\\zhengzhao.ofd")));
        content.put("appId", appId);
        content.put("certId", certId);
        content.put("certDN", certDN);
        content.put("secretKey", secretKey);
        content.put("keyPin", "");
        content.put("copyCreator", "同智伟业");
        content.put("copyCause", "版权所有，仿冒必究8");
        content.put("year", 1);
        String re1 = JSONUtils.toJSONString(content);
        m1.put("message_header", handleHeader(re1,appkey));
        
        m1.put("message_content",re1);
        String params =JSONUtils.toJSONString(m1);

        String res = HttpRequestUtil.sendJsonPost(url+"watermarkAndLockSigned",params, false);
        //system.out.println(res);
        Map<String, Object> re = (Map<String, Object>) JSONUtils.parse(res);
        if((boolean) re.get("status")) {
            try {
                String base64 = (String) re.get("returnData");
                base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
                byte[] fileData = Base64.decode(base64);
                FileUtil.getFile(fileData, "E:\\sealsign\\sign\\", "watermarkAndLockSigned.ofd");
            }catch(Exception e) {
                //system.out.println(e);
            }
        }

    }
    
    /**
     * 处理头参数
     * @param re1 待摘要数据，String类型
     * @param key 应用Secret，作为HMAC的秘钥
     * @return
     */
    public static HashMap handleHeader(String re1 ,String key){
        HashMap header = new LinkedHashMap();
        //生成数字签名
        byte[] bb = sure.qxszqm(re1.getBytes());
      
        
        /**签名值由确信生成，用于确认业务系统身份
         * 1、首次对接时，签名值传递空值，后台不做验证。
         * 2、签章接口对接成功后，对接确信平台生成数字签名并申请数字证书，并将数字证书发送给电子印章系统，联调测试。
         * 3、对接正式环境时向确信申请正式环境数字证书并发送给电子印章系统，注册业务系统。
         * 
         * 
         *  签名值生成过程：原文(string)->转成bytes[]类型->生成签名值(byte[])->转成base64(传递参数)
         * 数字签名
         * base64字节
         * PKCS1 验签
         */
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = smf.format(new Date());
		//system.out.println("nowTime------"+nowTime);
		
        header.put("sign",Base64.toBase64String(bb));//数字签名
        header.put("ctime",nowTime);
        header.put("version","1.0");

        String mac = null;
        try {
            mac = SM3Utils.hmac(key,re1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        header.put("hmac",mac);
        return header;
    }
}