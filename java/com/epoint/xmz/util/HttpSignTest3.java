package com.epoint.xmz.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Security;
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
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.esotericsoftware.minlog.Log;
import com.sure.signserver.SureCSPAPI;
import com.sure.signserver.util.BasicDefine;
import com.sure.signserver.util.ErrCode;
import com.tzwy.util.FileUtils;
import com.tzwy.util.HttpClientUtil;
import com.tzwy.util.HttpRequestUtil;
import com.tzwy.util.PfxRequestSign;
import com.tzwy.util.SignBySoftCert;
import com.tzwy.util.SM.SM3Utils;
import com.tzwy.util.orig.OrigFilePath;
import com.tzwy.util.orig.OrigUtils;

/**
 * 非电子证照签章接口(PDF、OFD)，位置、骑缝章
 * 
 *先执行demo进行签章验证，签章成功后开始对接业务系统
 * 
 *对接流程如下：
 * 1、先对接签章接口，确保签章接口在业务系统中能够正常签章。
 * 2、业务系统方向第三方ca机构申请数字证书并生成数字签名。
 * 3、数字证书传递给电子签章系统进行注册。
 * 4、业务系统进行签章并将签章文件发送给电子签章系统进行验证。
 * 5、签章文件验证通过，则具备对接正式环境的条件。
 * 
 * 签章接口对接涉及到两个数字签名值，一个是身份认证签名值；一个是文件追溯签名值。
 * 初始对接时两个签名值可以传递空值，待签章接口对接完成并能够正常签章之后再传递签名值，并由电子签章系统进行验证
 */
public class HttpSignTest3 {

	
	private static String Xcoord = ConfigUtil.getConfigValue("datasyncjdbc", "xcoord");
	private static String Ycoord = ConfigUtil.getConfigValue("datasyncjdbc", "ycoord");
	    
	public static String url = "http://59.206.202.165:8080/yesign/sign/";
	public static String vurl = "http://59.206.202.165:8080/yesign/verify/";	
	static String sealId = "f817ef125f774aaabba432ef3623f855";// ";// 印章ID 
	static String certId = "0a13907700dd43a3abcaa3d7e778cef1"; //证书ID，正式环境该值传递空值
	static String appId = "2cb28a948233ee2f01823d7de7930091";// 业务系统ID
	public static String appkey = ""; 
	static String secretKey = ""; //
	static String certDN = "";//
	

	/**
	 * OFD
	 * 处理签名请求报文
	 * @param path
	 * @return
	 */
	private static  String handleSignParam(String path,String type){
		HashMap m1 = new LinkedHashMap();
		HashMap content = new LinkedHashMap(); //业务参数
		byte[] base = FileUtils.readFileTobytes(path);
		content.put("fileData",Base64.toBase64String(base));
		content.put("appId",appId);
		content.put("certId",certId);
		content.put("fileName","签章文件测试名称");
        
		/**
		 * 生成原文hash和文件目录hash
		 * path 文件路径
		 * 
		 * 将原文hash值生成数字签名并作为参数传递给签章接口
		 */
		OrigFilePath origFilePath= OrigUtils.createOrigAndFilePaths(path);
		//原文hash值
		byte[] orighash = origFilePath.getOrig();
		
		/**
		 * 生成签名值
		 * 1、原文hash值-----byte[] orighash
		 * 2、生成数字签名-----byte[]类型
		 * 3、数字签名转换为16进制-----Hex.toHexString("签名值")
		 * 4、作为参数传递给签章接口
		 * 
		 * 
		 * 传递给签章系统的签名值为16进制(Hex.toHexString(signValue))
		 * 
		 */
		content.put("fileSign", "");//签名值  16进制  
		content.put("filePaths", origFilePath.getFilePaths());//文件目录

		if("conn".equals(type)){
			content.put("connPage", "3");// 骑缝页数
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
		m1.put("message_header",handleHeader(re1,""));
		m1.put("message_content",re1);
		String re = JSONUtils.toJSONString(m1);
		return re;
	}
	
	
	/**
	 * 处理签名请求报文
	 * @param path
	 * @return
	 */
	private static  String handleVerifyParam(String path,String type){
		HashMap m1 = new LinkedHashMap();
		HashMap content = new LinkedHashMap(); //业务参数
		byte[] base = FileUtils.readFileTobytes(path);
		content.put("file", Base64.toBase64String(base));
		content.put("type", type);
		String re1 = JSONUtils.toJSONString(content);
		m1.put("message_header",handleHeader(re1,""));
		m1.put("message_content",re1);
		String re = JSONUtils.toJSONString(m1);
		return re;
	}
	/**
	 * 身份认证签名值
	 * 处理头参数
	 * @param re1
	 * @return
	 */
	private static HashMap handleHeader(String re1,String bb){
		HashMap header = new LinkedHashMap();
		/**
		 * 参数re1作为原文生成数字签名
		 * 1、re1转换为bytes[]类型
		 * 2、生成数字签名值----byte[]类型
		 * 3、签名值转换为base64类型
		 * 4、base64类型类型作为参数传递给签章接口
		 * 
		 * 签名值生成过程：原文(string)->转成bytes[]类型->生成签名值(byte[])->转成base64(传递参数)
		 * 
         * 
         * 数字签名
         * base64字节
         * PKCS1 验签
         */
		header.put("sign",bb);// 数字签名值base64字节
		header.put("ctime",EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
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
	 * OFD签章接口
	 */
	public static void signOfd(String type) {
		String res="";
		Map<String, Object> param = new HashMap<>();
		String params =  handleSignParam("D:\\test1.ofd",type);
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
				FileUtils.write(fileData,"D:\\testsc.ofd");
				//system.out.println("完成");
			}else{
				//system.out.println("fail"+re.get("errorText"));
			}
		}else{
			//system.out.println("请求异常");
		}

	}
	/**
	 * PDF签章接口
	 */
	public static  String signPdf(String sealId,String clientGuid,String type) {
		sealId = "f817ef125f774aaabba432ef3623f855";
		IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
		List<FrameAttachInfo> attachs = iAttachService.getAttachInfoListByGuid(clientGuid);
		FrameAttachInfo attach = null;
		ByteArrayInputStream inputStream = null;
		String result = "";
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
				String params =  handleSignParamPDF(storage.getFilePath(),storage.getAttachFileName(),sealId,base,type);
				if("position".equals(type)){
					res=HttpRequestUtil.sendJsonPost(url+"signPdf",params, false);
				}else if("conn".equals(type)){
					res=HttpRequestUtil.sendJsonPost(url+"signPdfConn",params, false);
				}else{
					res=HttpRequestUtil.sendJsonPost(url+"signPdfKeyword",params, false);
				}
				Log.info("电子签章res:"+res);
				Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);

				if(null!=re&&!re.isEmpty()){
					String errorCode = (String) re.get("errorCode");
					if("0".equals(errorCode)){
						String base64 = (String) re.get("file");
						base64 = base64.replace("&#43;", "+"); // base64的数据，传输中 + 被转义为&#43;，需要替换
						byte[] fileData = Base64.decode(base64);
						inputStream = new ByteArrayInputStream(fileData);
						attach.setAttachLength(Long.valueOf((long) fileData.length));
						iAttachService.updateAttach(attach, inputStream);
						Log.info("电子签章附件修改成功！");
						result = "签章完成";
					}else{
						result = "fail"+re.get("errorText");
					}
				}else{
					result = "请求异常";
				}
			}else {
				result = "附件未找到";
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "请求异常";
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 验证接口
	 */
	public static void verifyFile() {
		Map<String, Object> param = new HashMap<>();
//		String params =  handleVerifyParam("D:\\test.ofd","ofd");
		String params =  handleVerifyParam("D:\\test.pdf","pdf");
		String res=HttpRequestUtil.sendJsonPost(vurl+"verifyFile",params, false);
		    	Map<String, Object> re = (Map<String, Object>) JSONUtils.parse((String) res);

		if(null!=re&&!re.isEmpty()){
			String errorCode = (String) re.get("errorCode");
			if("0".equals(errorCode)){
				List<Map<String, Object>> list = (List<Map<String, Object>>) re.get("data");
				if (null != list && list.size() > 0) {
					for(Map<String, Object> m :list) {
						//system.out.println("印章id:" + m.get("id"));
						//system.out.println("印章有效应:" + m.get("availability"));
						//system.out.println("印章名称:" + m.get("name"));
						//system.out.println("序号:" + m.get("index"));
						//system.out.println("印章有效性编码 :" + m.get("formatErrorCode"));
						//system.out.println("验证结果 :" + m.get("info"));
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
	
	/**
	   * 得到图片字节流 数组大小
	   * */
	  public static byte[] readStream(InputStream inStream) throws Exception{	
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];	  
	    int len = -1;	
	    while((len = inStream.read(buffer)) != -1){	  
	      outStream.write(buffer, 0, len);	  
	    }	  
	    outStream.close();	  
	    inStream.close();
	    return outStream.toByteArray();	  
	  }
	  
	  /**
		 * PDF
		 * 处理签名请求报文
		 * @param path
		 * @return 
		 * @throws Exception 
		 */
		private static  String handleSignParamPDF(String srcpath,String fileName,String sealId,byte[] base,String type) throws Exception{
			HashMap m1 = new LinkedHashMap();
			HashMap content = new LinkedHashMap(); //业务参数
			content.put("fileData",Base64.toBase64String(base));
			content.put("appId",appId);
			content.put("certId",certId);
			content.put("fileName",fileName);

			//生成原文hash和文件目录hash
//			OrigFilePath origFilePath=OrigUtils.createOrigAndRange(path);
			//原文hash值(通过确信将原文hash生成签名值（byte类型）)
//			byte[] orighash = origFilePath.getOrig();
			//system.out.println("orighash---"+Base64.toBase64String(base));
			//--------------------------
			int result = 0;
			SureCSPAPI handle = new SureCSPAPI();
			
			  // 从配置文件中获取许可证名称
            String surecryptocfgurl = "SureCryptoCfg.xml";
            // 获取许可证文件路径
            String path = ClassPathUtil.getClassesPath() + surecryptocfgurl;
            
			//建立链接
			result = handle.InitServerConnect(path);
			if (result != ErrCode.ERR_SUCCESS) {
				//system.out.println("InitServerConnect error, code = " + result);
				//System.exit(0);
			}
//			byte[] plainData = "Hello world.".getBytes();
			ByteArrayOutputStream byteos = new ByteArrayOutputStream();
			result = handle.SVS_PKCS1SignData(BasicDefine.ALG_ASYM_SM2, base, byteos, "WfGafzfjYctbZWFW");
			//system.out.println("SVS_PKCS1_SignData = " + result);
			byte[] bb = byteos.toByteArray();
			
			result = handle.SVS_PKCS1VerifyData("WfGafzfjYctbZWFW", BasicDefine.ALG_ASYM_SM2,byteos.toByteArray(), base);
			
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
			content.put("filePaths", srcpath);//文件目录
			
			if("conn".equals(type)){
				content.put("connPage", "6");// 骑缝页数
				content.put("connX", "10");// 签章坐标x
				content.put("connY", "120");// 签章坐标Y
				content.put("certDN", certDN);//签名延签服务器 公钥证书DN项  可为空
				content.put("secretKey", secretKey);//签名验签服务器 秘钥索引   可为空
				content.put("sealId", "e52ec7eb8d574f79821628481c7c1f53");
			}else {
				List list = new ArrayList();
				HashMap data = new LinkedHashMap();
				data.put("sealId","e52ec7eb8d574f79821628481c7c1f53");
				data.put("pageNum",1);
				data.put("zx",Integer.parseInt(Xcoord));
				data.put("zy",Integer.parseInt(Ycoord));
				data.put("certDN",certDN);
				data.put("secretKey",secretKey);
				data.put("keyPin","");
				list.add(data);
				content.put("signList",list);
	
				String re1 = JSONUtils.toJSONString(content);
				m1.put("message_header",handleHeader(re1,Hex.toHexString(bb)));
				m1.put("message_content",re1);
				String re = JSONUtils.toJSONString(m1);
				return re;
			}
			return type;
		}
		
	  

	
}
