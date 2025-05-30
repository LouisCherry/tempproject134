package com.epoint.xmz.gjj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;

import com.epoint.xmz.zjxl.util.CryptoMd5;
import com.epoint.xmz.zjxl.util.RSASignature;

/**
 * 向综合服务平台发送请求
 * 20170817
 */
public class CustomServiceCenterMessage{
	
	/**发送报文体内容*/
	private String ybmapMessageJson = null;
	
	private String uploadmessage = null;
	
	List<String> filelist = null;
	/**综服的地址*/
	private static final String url = "http://60.211.249.146:9081/YBMAPZH-WEB/";
	/** 中心客户代码：青岛为00052300*/
	private static final String centerId = "00053700";
	/** 设备区分 ：1-iOS,2-Android,3-pc */
	private static final String deviceType = "3";
	/** 设备识别码：  移动设备专用，非移动设备变量名上传，对应值为空 */
	private static final String deviceToken = "";
	/** 当前版本： 对应渠道当前版本号，如果没有默认上传“1.0”  */
	private static final String currenVersion = "1.0";
	/** 渠道标识：  银行端5**/
	private static final String channel = "18";
	/** 应用标识  */
	private static final String appid = "yondervisioneqm18";
	/** 应用KEY：  由综合服务管理系统维护，提供各服务渠道应用使用  */
	private static final String appkey = "f0065a23407e5bc5da04c86b972b2b9a";
	/** 应用TOKEN： 服务器端同步最新APPTOKEN：使用“应用标识”与“应用KEY”信息到综合服务平台取得最新APPTOKEN，超时需要重新获取  */
	private static final String appToken = "";
	/**综服-渠道应用管理设置认证： AES加密  KEY*/
	private static final String encKey = "a9624ED675497332";
	/** 客户端IP地址  */
	private String clientIp = "";//上传IP地址
	/** 用户ID：为accnum(账号) 或 certnum(证件号)，参与加密运算使用 */
	private String userId = "";
	/** headpara 变量格式为上传参数变量名组合，格式：   变量名1，变量名2，变量名3……  。*/
	private String headpara = "";
	/** 为变量名与值的对应关系MD5 */
	private String headparaMD5 = "";
	
	/**
	 * @Title: getHeadpara   
	 * @Description: 变量格式为上传参数变量名组合，格式：   变量名1，变量名2，变量名3……  。   
	 * @param: @return      
	 * @return: String      
	 */
	private String getHeadpara(){
		StringBuffer headpara_Str = new StringBuffer("");
		headpara_Str.append("centerId").append(",");
		headpara_Str.append("userId").append(",");
		headpara_Str.append("userIdType").append(",");
		headpara_Str.append("deviceType").append(",");
		headpara_Str.append("deviceToken").append(",");
		headpara_Str.append("currenVersion").append(",");
		headpara_Str.append("buzType").append(",");
		headpara_Str.append("channel").append(",");
		headpara_Str.append("appid").append(",");
		headpara_Str.append("appkey").append(",");
		headpara_Str.append("appToken").append(",");
		headpara_Str.append("clientIp").append(",");
		headpara_Str.append("money").append(",");
		headpara_Str.append("original").append(",");
		headpara_Str.append("encryption").append(",");//对ybmapMessage变量上传参进行加密或不加密 0-不加密,1-加密
		headpara_Str.append("transVersion").append("");
		
		return headpara_Str.toString();
	}
	/**
	 * @Title: getHeadparaMD5   
	 * @Description:为变量名与值的对应关系MD5，封装格式如下：变量名1=变量值1&变量名2=变量值2&………..
	 * 然后对该值进行1次MD5处理后进行签名处理
	 * @param: @return      
	 * @return: String      
	 */
	private String getHeadparaMD5(String userIdStr,String userIdTypeStr, String buzTypeStr,String money){
		/** 拼接 报文字符串 */
		clientIp = getClientIP();
		StringBuffer headparaMD5_Str = new StringBuffer("");
		headparaMD5_Str.append("centerId=").append(centerId).append("&");
		headparaMD5_Str.append("userId=").append(getAesEncrypt(userIdStr)).append("&");
		headparaMD5_Str.append("userIdType=").append(getAesEncrypt(userIdTypeStr)).append("&");
		headparaMD5_Str.append("deviceType=").append(deviceType).append("&");
		headparaMD5_Str.append("deviceToken=").append(deviceToken).append("&");
		headparaMD5_Str.append("currenVersion=").append(currenVersion).append("&");
		headparaMD5_Str.append("buzType=").append(buzTypeStr).append("&");
		headparaMD5_Str.append("channel=").append(channel).append("&");
		headparaMD5_Str.append("appid=").append(getAesEncrypt(appid)).append("&");
		headparaMD5_Str.append("appkey=").append(getAesEncrypt(appkey)).append("&");
		headparaMD5_Str.append("appToken=").append(appToken).append("&");
//		headparaMD5_Str.append("tranDate=").append(getCurrentDateString()).append("&");		
		headparaMD5_Str.append("clientIp=").append(clientIp).append("&");
		headparaMD5_Str.append("money=").append(getAesEncrypt(money)).append("&");
		headparaMD5_Str.append("original=").append("").append("&");
		//对ybmapMessage变量上传参进行加密或不加密 0-不加密 1-加密
		headparaMD5_Str.append("encryption=").append("").append("&");
		headparaMD5_Str.append("transVersion=").append("V1.0").append("");		
		
		return headparaMD5_Str.toString();
	}
	/**
	 * 传输参数加密处理
	 * @return
	 */
	private static String getAesEncrypt(String param){
		if("".equals(param) || null == param){
			return "";
		}else{
			return CryptoAes.encrypt(param,encKey);
		}
	}
	 /**
     * 获取ChannelSeq 
     * @return String 当前日期串
     */
    public static String getChannelSeq() {
    	SimpleDateFormat formatter = new SimpleDateFormat("hhmmss");
		return formatter.format(new Date());
    }
    
    /**
     * 获取当前日期字符串，格式'yyyy-mm-dd'
     * @return String 当前日期串
     */
    public static String getCurrentDateString() {
        return (new java.sql.Date(System.currentTimeMillis())).toString();
    }
	/**
	 * @param tranCode 交易码
	 * @param userId  客户ID 
	 * @return
	 * 根据交易码读取tranConfig.xml文件组装报文体
	 */
	public String transBody(String sendMsg , String userID,String userIdType,String buzType,String action) {
		
		/** 业务类型：  详细信息参考以下各业务接口列表中的业务类型  */
		userId = userID;//客户号
		headpara = getHeadpara();//报文头参数赋值
		headparaMD5 = getHeadparaMD5(userID,userIdType,buzType,"0.00");	// 取交易属性，业务类型	
		uploadmessage = headparaMD5;
		//组装发送报文
		String result = transBody(sendMsg,action);//action=buss*/appapi*.json
		return result;
	}
	
	/**
	 * 组装em节点数据
	 * @param em
	 * @return
	 */
	private String transBody(String sendMsg,String action) {
		 
		System.out.println("====上传项 ： \n" + ybmapMessageJson);
		headpara  += ",ybmapMessage";
		ybmapMessageJson = sendMsg.toString();
		headparaMD5 += "&ybmapMessage=" + ybmapMessageJson;
		uploadmessage += "&ybmapMessage=" + ybmapMessageJson;
		String result = send(action);
		return result;
	}

	public String send(String action){
	
		// 进行MD5二次加密
		System.out.println("进行MD5二次加密前：" + headparaMD5);
		headparaMD5 = RSASignature.sign(CryptoMd5.encrypt(headparaMD5), "");
		System.out.println("进行MD5二次加密后：" + headparaMD5);
		long end = System.currentTimeMillis();
		
		String realurl = url + (url.endsWith("/") ? "" : "/") + action;
		Map<String, String> prop = new HashMap<String, String>();
		prop.put("headpara", headpara);
		prop.put("headparaMD5", headparaMD5);
		System.out.println("======================上传报文================="+ uploadmessage);
		System.out.println("===================URL==================="+realurl);
		String ret = ApacheHttpUtil.send_post(realurl, uploadmessage, prop);
		if (ret == null) {
			throw new RuntimeException("未接收到返回信息");
		}

		System.out.println("======================下传报文==========================="+ ret);
		System.out.println("退出setUploadMap方法时间：" + end);
		return ret;
	}

	/***
	 * 解析返回的下传报文
	 * @throws JSONException 
	 */
	public Map<String, String> receiveMap() throws JSONException, ParseException,Exception {
		Map<String, String> returnMap = new HashMap<String, String>();
		return returnMap;
	}
	
	/**
	 * @Title: getClientIP   
	 * @Description:    
	 * @return: String      
	 * @throws ip=127.0.0.1
	 */
	public String getClientIP() {
		HttpServletRequest request =null;
		try{
			String ip = request.getRemoteAddr();  
	        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
	            ip=request.getHeader("Proxy-Client-IP");  
	        }  
	        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
	            ip=request.getHeader("WL-Proxy-Client-IP");  
	        }  
	        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
	            ip=request.getHeader("X-Real-IP");  
	        }  
	        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
	            ip= request.getHeader("X-Forwarded-For");   
	        }
	        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
	            ip= request.getHeader("HTTP_CLIENT_IP");   
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			return ip;
		}catch(Exception ex){
			return "127.0.0.1";
		}
	}

}
