package com.epoint.xmz.spglgt;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.config.ConfigUtil;

public class GtProjectInfoPush {
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String gt_url= ConfigUtil.getConfigValue("epointframe", "gturl");
	
	
	
	public JSONObject userManager(String regionCode,String userName,String phone,String group,String opType,String loginName,String stage) {
		String url = gt_url + "/checkService/sd/hyhj/userManager";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//		map.add("regionCode", "370100");
//		map.add("userName", "张三");
//		map.add("phone", "15154116700");
//		map.add("group", "10101");
//		map.add("opType", "add");
//		map.add("loginName ", "XXXXXXX");
//		map.add("stage ", " stage");
		map.add("regionCode", regionCode);
		map.add("userName", userName);
		map.add("phone", phone);
		map.add("group", group);
		map.add("opType", opType);
		map.add("loginName ", opType);
		map.add("stage ", stage);
		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		return restTemplate.postForObject(url, request, JSONObject.class);
	}
	
	
	public JSONObject projectRegister(String included,String sign,String userID,String xmmc,String ysxmdm) {
		String url = gt_url + "/checkService/sd/hyhj/projectRegister";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("included", included);
		map.add("sign", sign);
		map.add("userID", userID);
		map.add("xmmc", xmmc);
		map.add("ysxmdm", ysxmdm);

		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		return restTemplate.postForObject(url, request, JSONObject.class);
		
	}
	
	
	public String uploadDataPackage(String filePath, String xmmc, String dataPackageMD5, String datapackageType, String xmbh,String userID,String verify,String sign) {
		String url = gt_url + "/uploadService/fileUpload/uploadDataPackage";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
		form.add("datapackage", new FileSystemResource("D:\\data.zip"));
		form.add("xmmc", xmmc);
		form.add("dataPackageMD5",dataPackageMD5);
		form.add("datapackageType", datapackageType);
		form.add("xmbh", xmbh);
		form.add("userID", userID);
		form.add("verify", verify);
		form.add("sign", sign);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, Object>> datas = new HttpEntity<>(form, headers);
		ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, datas, JSONObject.class);
		String result = responseEntity.getBody().toString();
		//system.out.println("返回结果：" + result.toString());
		return result;
	}

	public JSONObject goVerification(String sign, String userID, String xmbh, String datapackageType) {
		String url = gt_url + "/checkService/sd/hyhj/goVerification";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sign", sign);
		map.add("userID", userID);
		map.add("xmbh", xmbh);
		map.add("datapackageType", datapackageType);
		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
		return result;
	}

	public JSONObject getDzjgh(String sign, String userID, String xmbh) {
		String url = gt_url + "/checkService/sd/hyhj/getDzjgh";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sign", sign);
		map.add("userID", userID);
		map.add("xmbh", xmbh);
		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
		return result;
	}

	public JSONObject downloadDataPackag(String sign, String userID, String xmbh, String datapackageType,String receiveUrl) {
		String url= gt_url + "/checkService/sd/hyhj/downloadDataPackag";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sign", sign);
		map.add("userID", userID);
		map.add("xmbh", xmbh);
		map.add("datapackageType", datapackageType);
		map.add("receiveUrl", receiveUrl);
		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
		return result;
	}

	public static JSONObject queryDownloadProgress(String sign, String userID, String requestID, String methodName) {
		String url = gt_url + "/checkService/sd/hyhj/queryInterfaceResults";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("sign", sign);
		map.add("userID", userID);
		map.add("requestID", requestID);
		map.add("methodName", methodName);
		HttpEntity<MultiValueMap<String, String>> request =
		                new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate=new RestTemplate();
		JSONObject result = restTemplate.postForObject(url, request, JSONObject.class);
		return result;
	}

}
