
package com.epoint.xmz.zjxl.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.xmz.zjxl.util.AesTest;
import com.epoint.xmz.zjxl.util.EncryptionByMD5;
import com.epoint.xmz.zjxl.util.RSASignature;

@RestController
@RequestMapping("/jnzjlcsb")
public class JnZjlcSbController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String headpara="centerId,userId,userIdType,deviceType,deviceToken,currenVersion,buzType,channel,appid,appkey,appToken,clientIp,money,original,encryption,transVersion,ybmapMessage";
	public static final String headparams = "centerId=00053700&userId=jAA1lMb49zdDw97EU4EiNhwadXsMSf8NQftgB/g5jok=&userIdType=xc/ezttM2HaU/v1HSTwLLg==&deviceType=3&deviceToken=&currenVersion=1.5.1&buzType=";
	public static final String endparams = "&channel=13&appid=8T+DqtUWjOc0c7x42F76Kje2jtBHP6zSx1wCSGNwGeo=&appkey=B/oqcxgs55L9aZSowmvLKISEpnZKnYT5dEoYD747qYyWtQwxnOYJS1qGI6WxnOMQ&appToken=&clientIp=&money=&original=&encryption=0&transVersion=V1.0&";

	
	
	/**
	 * 提交成品油变更的办件数据推送到
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String submitJnAiCpy(@RequestBody String params) {
		try {
			log.info("=======开始调用submitJnAiCpy接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				AesTest aes = new AesTest();		
				String userId=aes.encrypt("370811199103220055".getBytes("UTF-8"));
				String userIdType=aes.encrypt("10".getBytes("UTF-8"));
				String appId=aes.encrypt("yondervisioneqm18".getBytes("UTF-8"));
				String appKey=aes.encrypt("f0065a23407e5bc5da04c86b972b2b9a".getBytes("UTF-8"));
				String certnum = "370811199103220055";
				System.out.println("userId:"+userId+"userIdType:"+userIdType+" appId:"+appId+" appKey:"+appKey);
				String buzType = "5432";
				String param = headparams + buzType + endparams
						  + "ybmapMessage={\"transchannel\":\"18\",\"flag\":\"4\",\"pwd\":\"\",\"accnum\":\"\",\"certinum\":\""
		                    + certnum + "\"}";
				System.out.println("11:"+param);
				String result="";
				result=httpURLConnectionPOST("http://60.211.249.146:9081/YBMAPZH-WEB/appapi50006.json",param);
				log.info("=======结束调用submitJnAiCpy接口=======");
				return JsonUtils.zwdtRestReturn("1", "提交成品油变更事项成功", result);
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======submitJnAiCpy接口参数：params【" + params + "】=======");
			log.info("=======submitJnAiCpy异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "提交成品油变更事项失败：" + e.getMessage(), "");
		}
	}
	
	
	public static String httpURLConnectionPOST(String httpurl, String params) {
        try {

            URL url = new URL(httpurl);
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)  
            // 此时cnnection只是为一个连接对象,待连接中  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)  
            connection.setDoOutput(true);
            // 设置连接输入流为true  
            connection.setDoInput(true);
            // 设置请求方式为post  
            connection.setRequestMethod("POST");
            // post请求缓存设为false  
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向  
            connection.setInstanceFollowRedirects(true);

            connection.addRequestProperty("headpara", headpara);
            connection.addRequestProperty("headparaMD5",RSASignature.sign(EncryptionByMD5.getMD5(params.getBytes()), RSASignature.RSA_PRIVATE));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)  
            connection.connect();

            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)  
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
        
            // 将参数输出到连接  
            dataout.writeBytes(params);
            // 输出完成后刷新并关闭流  
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)   

            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)  
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据  

            // 循环读取流,若不到结尾处  
            while ((line = bf.readLine()) != null) {
                //sb.append(bf.readLine());  
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close(); // 重要且易忽略步骤 (关闭流,切记!)   
            connection.disconnect(); // 销毁连接  

            //System.out.println(sb.toString());
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

	

}
