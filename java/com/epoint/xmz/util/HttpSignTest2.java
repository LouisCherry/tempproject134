package com.epoint.xmz.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.alibaba.druid.support.json.JSONUtils;
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
public class HttpSignTest2 {

	public static String url = "http://59.206.202.175:8082/yesign/sign/";
	public static String vurl = "http://59.206.202.175:8082/yesign/verify/";
	static String sealId = "e52ec7eb8d574f79821628481c7c1f53";// 印章ID
	static String certId = "0a13907700dd43a3abcaa3d7e778cef1"; //证书ID，正式环境该值传递空值
	static String appId = "2cb28a91816f968e0181d7a68c990021";// 业务系统ID
	public static String appkey = ""; 
	static String secretKey = ""; //正式环境使用
	static String certDN = "";//
	
	public static void main(String[] args) throws Exception {
		
		
		Security.addProvider(new BouncyCastleProvider());

//		for (int i=1;i<10;i++){
		signPdf("position"); //PDF签章接口，position位置签章 ，  conn 骑缝章
     //signOfd("position");//OFD签章接口，position位置签章，   conn 骑缝章
//      verifyFile();//验章接口
//		}
        
	}
	
	/**
	 * OFD签章接口
	 * @throws IOException 
	 */
	public static void signOfd(String type) throws IOException {
		String res="";
		Map<String, Object> param = new HashMap<>();
		String params =  handleSignParam("D:\\111.ofd",type);
		if("position".equals(type)){
			res=HttpRequestUtil.sendJsonPost(url+"signOfd",params, false);
		}else if("conn".equals(type)){
			res= HttpRequestUtil.sendJsonPost(url+"signOfdConn",params, false);
		}
		    Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);

		if(null!=re&&!re.isEmpty()){
			String errorCode = (String) re.get("errorCode");
			if("0".equals(errorCode)){
				String base64 = (String) re.get("file");
				base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
				byte[] fileData = Base64.decode(base64);
				FileUtils.write(fileData,"D:\\QAZ.ofd");
				//system.out.println("完成");
			}else{
				//system.out.println("fail"+re.get("errorText"));
			}
		}else{
			//system.out.println("请求异常");
		}

	}
	
	/**
	 * OFD
	 * 处理签名请求报文
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	private static  String handleSignParam(String path,String type) throws IOException{
		HashMap m1 = new LinkedHashMap();
		HashMap content = new LinkedHashMap(); //业务参数
		byte[] base = FileUtils.readFileTobytes(path);
		content.put("fileData",Base64.toBase64String(base));
		content.put("appId",appId);
		content.put("certId",certId);
		content.put("fileName","test.pdf");
        
		/**
		 * 生成原文hash和文件目录hash
		 * path 文件路径
		 * 
		 * 将原文hash值生成数字签名并作为参数传递给签章接口
		 */
		OrigFilePath origFilePath= OrigUtils.createOrigAndFilePaths(path);
		//原文hash值
		byte[] orighash = origFilePath.getOrig();
		//生成数字签名
		byte[]  bb= sure.qxszqm(orighash);
		
		/**
		 * 生成签名值
		 * 1、原文hash值-----byte[] orighash
		 * 2、生成数字签名-----byte[]类型
		 * 3、数字签名转换为16进制-----Hex.toHexString("签名值")
		 * 4、作为参数传递给签章接口
		 * 

		 * 传递给签章系统的签名值为16进制(Hex.toHexString(signValue))
		 * 
		 */
		content.put("fileSign", Hex.toHexString(bb));//签名值  16进制  
		content.put("filePaths", origFilePath.getFilePaths());//文件目录

		if("conn".equals(type)){
			content.put("connPage", "0");// 骑缝页数
			content.put("connX", "200");// 签章坐标x
			content.put("connY", "200");// 签章坐标Y
			content.put("certDN", certDN);//签名延签服务器 公钥证书DN项  可为空
			content.put("secretKey", secretKey);//签名验签服务器 秘钥索引   可为空
			content.put("sealId", sealId);
		}else{
			List list = new ArrayList();
			HashMap data = new LinkedHashMap();
			data.put("sealId",sealId);
			data.put("pageNum",0);
			data.put("zx",100);
			data.put("zy",100);
			data.put("certDN",certDN);
			data.put("secretKey",secretKey);
			data.put("keyPin","");
			list.add(data);
			content.put("signList",list);
		}

		String re1 = JSONUtils.toJSONString(content);
		m1.put("message_header",handleHeader(re1));
		m1.put("message_content",re1);
		String re = JSONUtils.toJSONString(m1);
		return re;
	}
	
	/**
	 * PDF
	 * 处理签名请求报文
	 * @param path
	 * @return 
	 * @throws Exception 
	 */
	private static  String handleSignParamPDF(String path,String type) throws Exception{
		path = "D:\\工作\\济宁\\epoint-web-zwfw\\src\\main\\webapp\\epointcert\\resultfile\\zhengzhao\\cca0d5fd-8402-4646-849c-3f571a5d14ad202207260003-孙超.pdf";
		HashMap m1 = new LinkedHashMap();
		HashMap content = new LinkedHashMap(); //业务参数
		byte[] base = FileUtils.readFileTobytes(path);
		content.put("fileData",Base64.toBase64String(base));
		content.put("appId",appId);
		content.put("certId",certId);
		content.put("fileName","test.pdf");

		//生成原文hash和文件目录hash
		OrigFilePath origFilePath=OrigUtils.createOrigAndRange(path);
		//原文hash值(通过确信将原文hash生成签名值（byte类型）)
		byte[] orighash = origFilePath.getOrig();
		//system.out.println("orighash---"+Base64.toBase64String(orighash));
		//--------------------------
		int result = 0;
		SureCSPAPI handle = new SureCSPAPI();
		
		//建立链接
		result = handle.InitServerConnect("D://SureCryptoCfg.xml");
		if (result != ErrCode.ERR_SUCCESS) {
			//system.out.println("InitServerConnect error, code = " + result);
//			System.exit(0);
		}
//		byte[] plainData = "Hello world.".getBytes();
		ByteArrayOutputStream byteos = new ByteArrayOutputStream();
		result = handle.SVS_PKCS1SignData(BasicDefine.ALG_ASYM_SM2, orighash, byteos, "SdJnSpjYCSL");
		//system.out.println("SVS_PKCS1_SignData = " + result);
		byte[] bb = byteos.toByteArray();
		
		result = handle.SVS_PKCS1VerifyData("SdJnSpjYCSL", BasicDefine.ALG_ASYM_SM2,byteos.toByteArray(), orighash);
		
		byteos.close();
		//system.out.println("SVS_PKCS1_VerifyData = " + result);
		
		//释放连接
		result = handle.FinalizeServerConnect();
		//system.out.println("FinalizeServerConnect = " + result);
		//system.out.println("bb------"+bb);
		
		/**
		 * 文件追溯
		 * 原文hash值的数字签名
		 * 1、通过调用确信平台并将原文hash值传递给确信，由确信平台生成原文hash值的数字签名（byte类型）
		 * 2、将生成的数字签名作为参数传递给签章系统
		 *
		 * 传递给签章系统的签名值为16进制(Hex.toHexString(signValue)
		 */
		//-----------
		//system.out.println("Hex.toHexString(bb)-----"+Hex.toHexString(bb));
		content.put("fileSign",Hex.toHexString(bb));//签名值  16进制
		content.put("filePaths", origFilePath.getFilePaths());//文件目录
		
		if("conn".equals(type)){
			content.put("connPage", "6");// 骑缝页数
			content.put("connX", "10");// 签章坐标x
			content.put("connY", "120");// 签章坐标Y
			content.put("certDN", certDN);//签名延签服务器 公钥证书DN项  可为空
			content.put("secretKey", secretKey);//签名验签服务器 秘钥索引   可为空
			content.put("sealId", sealId);
		}else {
		List list = new ArrayList();
		HashMap data = new LinkedHashMap();
		data.put("sealId",sealId);
		data.put("pageNum",1);
		data.put("zx",400);
		data.put("zy",600);
		data.put("certDN",certDN);
		data.put("secretKey",secretKey);
		data.put("keyPin","");
		list.add(data);
		content.put("signList",list);

		String re1 = JSONUtils.toJSONString(content);
		m1.put("message_header",handleHeader(re1));
		m1.put("message_content",re1);
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
	private static  String handleVerifyParam(String path,String type) throws IOException{
		HashMap m1 = new LinkedHashMap();
		HashMap content = new LinkedHashMap(); //业务参数
		byte[] base = FileUtils.readFileTobytes(path);
		content.put("file", Base64.toBase64String(base));
		content.put("type", type);
		String re1 = JSONUtils.toJSONString(content);
		m1.put("message_header",handleHeader(re1));
		m1.put("message_content",re1);
		String re = JSONUtils.toJSONString(m1);
		return re;
	}
	/**
	 * 处理头参数
	 * @param re1
	 * @return
	 * @throws IOException 
	 */
	private static HashMap handleHeader(String re1) throws IOException{
		HashMap header = new LinkedHashMap();
		//生成数字签名
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
		
		header.put("sign",Base64.toBase64String(bb));// 数字签名base64字节
		header.put("ctime",nowTime);
	
		header.put("version","1.0");

		String mac = null;
		try {
			mac = SM3Utils.hmac(appkey,re1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		header.put("hmac",mac);
		return header;
	}


	/**
	 * PDF签章接口
	 * @throws Exception 
	 */
	public static void signPdf(String type) throws Exception {
		Map<String, Object> param = new HashMap<>();
		String res = "";
		String params =  handleSignParamPDF("D:\\工作\\济宁\\epoint-web-zwfw\\src\\main\\webapp\\epointcert\\resultfile\\zhengzhao\\cca0d5fd-8402-4646-849c-3f571a5d14ad202207260003-孙超.pdf",type);
		//system.out.println("params-----"+params);
		if("position".equals(type)){
			res=HttpRequestUtil.sendJsonPost(url+"signPdf",params, false);
		}else if("conn".equals(type)){
			res=HttpRequestUtil.sendJsonPost(url+"signPdfConn",params, false);
		}else{
			res=HttpRequestUtil.sendJsonPost(url+"signPdfKeyword",params, false);
		}
		Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);
		if(null!=re&&!re.isEmpty()){
			String errorCode = (String) re.get("errorCode");
			if("0".equals(errorCode)){
				String base64 = (String) re.get("file");
				base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
				byte[] fileData = Base64.decode(base64);
				FileUtils.write(fileData,"D:\\test234.pdf");
				//system.out.println("完成");
			}else{
				//system.out.println("fail"+re.get("errorText"));
			}
		}else{
			//system.out.println("请求异常");
		}
 
	}
	/**
	 * 验证接口
	 * @throws IOException 
	 */
	public static void verifyFile() throws IOException {
		Map<String, Object> param = new HashMap<>();
//		String params =  handleVerifyParam("D:\\test234.pdf","pdf");
		String params =  handleVerifyParam("D:\\test234.pdf","pdf");
		String res=HttpRequestUtil.sendJsonPost(vurl+"verifyFile",params, false);
		    	Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);

		if(null!=re&&!re.isEmpty()){
			String errorCode = (String) re.get("errorCode");
			if("0".equals(errorCode)){
				List<Map<String, Object>> list = (List<Map<String, Object>>) re.get("data");
				if (null != list && list.size() > 0) {
					for(Map<String, Object> m :list) {
						//system.out.println("id:" + m.get("id"));
						//system.out.println("availability:" + m.get("availability"));
						//system.out.println("name:" + m.get("name"));
						//system.out.println("index:" + m.get("index"));
						//system.out.println("formatErrorCode :" + m.get("formatErrorCode"));
						//system.out.println("info :" + m.get("info"));
					}
				} else {
					//system.out.println("not data");
				}
				//system.out.println("完成");
			}else{
				//system.out.println("fail"+re.get("errorText"));
			}
		}else{
			//system.out.println("请求异常");
		}

	}

	
}
