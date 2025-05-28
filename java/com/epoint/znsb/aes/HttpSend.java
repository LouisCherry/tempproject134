package com.epoint.znsb.aes;


import java.io.BufferedReader;  
import java.io.DataOutputStream;  
import java.io.InputStreamReader;  
import java.net.HttpURLConnection;  
import java.net.InetAddress;  
import java.net.URL;  
import java.net.UnknownHostException;  
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;


public class HttpSend {
	public static final String GET_URL = "http://112.4.27.9/mall-back/if_user/store_list?storeId=32";  
	public static final String POST_URL = "http://61.153.144.77:7001/YBMAPZH/appapi50006.json";  

	public static String encrypt(String value) {
		if(value==null||value.trim().length()==0){
			return null;
		}
		MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] byteArray = value.getBytes();
        byte[] md5Bytes = md5.digest(byteArray);
        
        return String.valueOf(Hex.encodeHex(md5Bytes));
	}
	
	/** 
	* 接口调用 GET 
	*/
	public static void httpURLConectionGET() {  
		try {  
			URL url = new URL(GET_URL);    //把字符串转换为URL请求地址  
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接  
			//addRequestProperty添加相同的key不会覆盖，如果相同，内容会以{name1,name2}  
			connection.addRequestProperty("from", "sfzh");  //来源哪个系统  
			//setRequestProperty添加相同的key会覆盖value信息  
			//setRequestProperty方法，如果key存在，则覆盖；不存在，直接添加。  
			//addRequestProperty方法，不管key存在不存在，直接添加。  
			connection.setRequestProperty("user", "user");  //访问申请用户  
			InetAddress address = InetAddress.getLocalHost();   
			String ip=address.getHostAddress();//获得本机IP  
			connection.setRequestProperty("ip",ip);  //请求来源IP  
			connection.setRequestProperty("encry", "00000");  
			connection.connect();// 连接会话  
			// 获取输入流  
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));  
			String line;  
			StringBuilder sb = new StringBuilder();  
			while ((line = br.readLine()) != null) {// 循环读取流  
				sb.append(line);  
			}
			br.close();// 关闭流  
			connection.disconnect();// 断开连接  
			//system.out.println(sb.toString());  
		} catch (Exception e) {  
			e.printStackTrace();  
			//system.out.println("失败!");  
		}
	}

	/** 
	* 接口调用  POST 
	*/
	public static void httpURLConnectionPOST () {  
		try {  
			URL url = new URL(POST_URL);  
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
			// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)  
			// application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据  
			// ;charset=utf-8 必须要，不然妙兜那边会出现乱码【★★★★★】  
			//addRequestProperty添加相同的key不会覆盖，如果相同，内容会以{name1,name2}  
			//--connection.addRequestProperty("from", "sfzh");  //来源哪个系统  
			connection.addRequestProperty("headpara", "centerId,userId,usertype,deviceType,deviceToken,currenVersion,buzType,devtoken,channel,appid,appkey,appToken,clientIp,bodyCardNumber,accnum,password");
			AesTest aes=null;
			try {
				aes = new AesTest();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String accnum = aes.encrypt("".getBytes("UTF-8"));
			String bodycardnumber = aes.encrypt("330203198212122417".getBytes("UTF-8"));
			String password = aes.encrypt("111111".getBytes("UTF-8"));
			//用于数字签名
			String parm ="centerId=00057400&userId="+bodycardnumber+"&usertype=10&deviceType=3&deviceToken=&currenVersion=1.0&buzType=5432&devtoken=&channel=50&appid=79WruUiO3zh93c3lOUrurv/H4HWb1iXA2dzQ+HP+550=&appkey=vYtUAj6UU7vHgbLGlaBB4scQw1it4PVt3lm1Qcoubny/ZwEoOg+/LmMAOXufGwvB&appToken=&clientIp=R9GKDaKvnhfuNxHvUs/T/A==&bodyCardNumber="
					+bodycardnumber+"&accnum="+accnum+"&password="+password;
			//用于发送http报文
			String parm1 = "centerId=00057400&userId="+bodycardnumber.replace("+", "%2B")+"&usertype=10&deviceType=3&deviceToken=&currenVersion=1.0&buzType=5432&devtoken=&channel=50&appid=79WruUiO3zh93c3lOUrurv/H4HWb1iXA2dzQ%2BHP%2B550=&appkey=vYtUAj6UU7vHgbLGlaBB4scQw1it4PVt3lm1Qcoubny/ZwEoOg%2B/LmMAOXufGwvB&appToken=&clientIp=R9GKDaKvnhfuNxHvUs/T/A==&bodyCardNumber="
					+bodycardnumber.replace("+", "%2B")+"&accnum="+accnum.replace("+", "%2B")+"&password="+password.replace("+", "%2B");
			//system.out.println(EncryptionByMD5.getMD5(parm.getBytes()));
			connection.addRequestProperty("headparaMD5", RSASignature.sign(EncryptionByMD5.getMD5(parm.getBytes()), RSASignature.RSA_PRIVATE));
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");      
			// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)  
			connection.connect();  
			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)  
			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());  
			//system.out.println("传递参数："+parm1);  
			// 将参数输出到连接  
			dataout.writeBytes(parm1);  
			// 输出完成后刷新并关闭流  
			dataout.flush();  
			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)   
			////system.out.println(connection.getResponseCode());  
			// 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)  
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));   
			String line;  
			StringBuilder sb = new StringBuilder(); // 用来存储响应数据  

			// 循环读取流,若不到结尾处  
			while ((line = bf.readLine()) != null) {  
				//sb.append(bf.readLine());  
				sb.append(line).append(System.getProperty("line.separator"));  
			}
			bf.close();    // 重要且易忽略步骤 (关闭流,切记!)   
			connection.disconnect(); // 销毁连接  
			//system.out.println(sb.toString());  
		} catch (Exception e) {  
			e.printStackTrace();  
		}
	}

	public static void main(String[] args) throws UnknownHostException {  
		//httpURLConectionGET();  
		httpURLConnectionPOST();  
		
		//String values = "centerId=00087100&userId=uc3ax5dYlC/Jhk1431vLqkOClzqF0+4LeqWS6dmy8dM=&usertype=10&deviceType=3&deviceToken=&currenVersion=1.0&channel=40&appid=60IHldvLlLoxb8nBdu0lxWZU6QC75Edhgscd3xdFya4=&appkey=HYzpktAxY6fgH8mZfWrFjx/54uo2yzsy3jI0KqBVvCnxauT7mjRABGyJ7bhQruO8&appToken=&tellCode=8802&brcCode=00087102&channelSeq=-11300&tranDate=2016-09-28&clientIp=0:0:0:0:0:0:0:1&buzType=5001&accnum=113099582753";
		//String b = RSASignature.sign(EncryptionByMD5.getMD5(values.getBytes()), RSASignature.RSA_PRIVATE);
		////system.out.println(RSASignature.doCheck(EncryptionByMD5.getMD5(values.getBytes()), b, RSASignature.RSA_ALIPAY_PUBLIC));
	}

}
