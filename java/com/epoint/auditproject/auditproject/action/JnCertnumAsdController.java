
package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.Util;
import com.epoint.ta.httprequest.util.TaHttpRequestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jncertnumasd")
public class JnCertnumAsdController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
	private String accessId = "JNS_SPJYC";
	private String accessToken = "A17B2F35B3E243B59852F182316E4414";

	@Autowired
	private IAttachService attachService;
	
	 @Autowired
     private IAuditTaskMaterial iAuditTaskMaterial;
	 @Autowired
	 private IAuditTask iAuditTask;


	/**
	 * 获取建筑业企业资信信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getCertIdentifier", method = RequestMethod.POST)
	public String getCertIdentifier(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertIdentifier接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String qrcode = obj.getString("qrcode");
				;
				String cert_identifier = "";
				String name = "";
				String certno = "";
				JSONObject result = new JSONObject();
				if (StringUtil.isNotBlank(qrcode)) {
					String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
							+ "\"},\"data\":{\"qrcode\":\"" + qrcode + "\", \"useCause\":\"济宁政务服务\"}}";
					String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByQRcode";
					String resultParmas = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
					JSONObject resultjson = JSONObject.parseObject(resultParmas);
					JSONObject headjson = resultjson.getJSONObject("head");
					log.info("====resultParmas=====" + resultParmas);
					try {
						if ("0".equals(headjson.getString("status"))) {
							JSONObject datajson = resultjson.getJSONObject("data");
							byte[] decrypt;
							decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey),
									Util.hexToByte(datajson.getString("certData")));
							// 将字节数组转为字符串
							String backresult = new String(decrypt, "utf-8");
							JSONObject bacjson = JSONObject.parseObject(backresult);
							cert_identifier = bacjson.getString("cert_identifier");
							name = bacjson.getString("name");
							certno = bacjson.getString("certno");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 调用省电子证照接口获取证照数据
					result.put("cert_identifier", cert_identifier);
					result.put("name", name);
					result.put("certno", certno);
					return JsonUtils.zwdtRestReturn("1", "获取证照标识成功！", result.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "证照标识信息不能为空！", "");
				}
				
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getCertIdentifier接口参数：params【" + params + "】=======");
			log.info("=======getCertIdentifier异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
		}
	}
	
	@RequestMapping(value = "/getCertIdentifierByCode", method = RequestMethod.POST)
	public String getCertIdentifierByCode(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertIdentifierByCode接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			JSONObject certjson = new JSONObject();
			List<String> licens = new ArrayList<String>();
			
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String certificateNo = obj.getString("certificateNo");
				String taskmaterialguid = obj.getString("taskmaterialguid");
				AuditTaskMaterial taskmaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialguid).getResult();
				if (taskmaterial == null) {
					return JsonUtils.zwdtRestReturn("0", "获取材料信息失败！", "");
				}
				//查询事项信息
				AuditTask task = iAuditTask.getAuditTaskByGuid(taskmaterial.getTaskguid(), false).getResult();
				if (task == null) {
					return JsonUtils.zwdtRestReturn("0", "获取事项信息失败！", "");
				}
				String bigshowtype = StringUtil.isBlank(taskmaterial.getStr("bigshowtype"))? "": taskmaterial.getStr("bigshowtype");
				log.info("关联材料的名称："+bigshowtype);
				String cert_identifier = "";
				if (StringUtil.isNotBlank(certificateNo) && StringUtil.isNotBlank(bigshowtype)) {
					String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
							+ "\"},\"data\":{\"holderCode\":\"" + certificateNo + "\", \"holderTypeCode\":\"111\","
									+ "\"certTypeCode\":\""+bigshowtype+"\",\"useCause\":{\"certificateCopyCause\":\"济宁政务服务\"," +
							"\"certificateCallCategory\":\"0\",\"certificateEventCode\":\"" + task.getItem_id() + "\"," +
							"\"certificateMatterName\":\"" + task.getTaskname() + "\",\"certificateSystemName\":\"济宁市一窗系统\"}}}";
					String httpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrlsByHolderCode";
					String resultParmas = TaHttpRequestUtils.sendPost(httpUrl, postreason, "", "");
					JSONObject resultjson = JSONObject.parseObject(resultParmas);
					JSONObject headjson = resultjson.getJSONObject("head");
					try {
						if ("0".equals(headjson.getString("status"))) {
							JSONObject datajson = resultjson.getJSONObject("data");
							byte[] decrypt;
							decrypt = SM2Utils.decrypt(Util.hexToByte(privatekey),
									Util.hexToByte(datajson.getString("certData")));
							// 将字节数组转为字符串
							String backresult = new String(decrypt, "utf-8");
							JSONObject bacjson = JSONObject.parseObject(backresult);
							log.info("====resultParmas=====" + bacjson);
							cert_identifier = bacjson.getString("cert_identifier");
							licens.add(cert_identifier);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 调用省电子证照接口获取证照数据
					certjson.put("listnos", licens);
					
					return JsonUtils.zwdtRestReturn("1", "获取证照标识成功！", certjson.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "证照标识信息不能为空！", "");
				}
				
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getCertIdentifierByCode接口参数：params【" + params + "】=======");
			log.info("=======getCertIdentifierByCode异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
		}
	}
	
	/**
	 * 获取建筑业企业资信信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/downCertnum", method = RequestMethod.POST)
	public String downCertnum(@RequestBody String params) {
		try {
			log.info("=======开始调用downCertnum接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String cert_identifier = obj.getString("cert_identifier");
				JSONObject result = new JSONObject();
				
				String attachguid = UUID.randomUUID().toString();
				String CliengGuid = obj.getString("cliengguid");;
				String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
						+ "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
						+ "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
				String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
				String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
				log.info("下载附件接口："+getpdfresult);
				JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
				JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
				if ("0".equals(getpdfheadjson.getString("status"))) {

					byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
							Util.hexToByte(getpdfresultjson.getString("data")));

					// 将字节数组转为字符串
					String getpdfbackresult = new String(decrypt2, "utf-8");

					JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
					String getpdfcontent = getpdfbacjson.getString("content");
					// getpdfcontent = "http://59.206.96.173:8080" +
					// getpdfcontent;

					URL url = new URL(getpdfcontent);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
					conn.setConnectTimeout(10 * 1000); // 防止屏蔽程序抓取而返回403错误
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					InputStream inputStream = conn.getInputStream();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = inputStream.read(buffer)) > -1) {
						baos.write(buffer, 0, len);
					}
					baos.flush();

					InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());

					FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
					long size = (long) inputStream.available();
					frameAttachInfo.setAttachGuid(attachguid);
					frameAttachInfo.setCliengGuid(CliengGuid);
					frameAttachInfo.setAttachFileName(EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss") + ".pdf");
					frameAttachInfo.setCliengTag("电子身份证");
//					frameAttachInfo.setUploadUserGuid(userguid);
//					frameAttachInfo.setUploadUserDisplayName(username);
					frameAttachInfo.setUploadDateTime(new Date());
					frameAttachInfo.setContentType(".pdf");
					frameAttachInfo.setAttachLength(size);
					attachService.addAttach(frameAttachInfo, stream1);
					
					result.put("attachguid", attachguid);
					result.put("cliengguid", CliengGuid);
					return JsonUtils.zwdtRestReturn("1", "附件上传成功！", result.toString());
				} else {
					return JsonUtils.zwdtRestReturn("0", "接口调用失败！", "");
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======downCertnum接口参数：params【" + params + "】=======");
			log.info("=======downCertnum异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照失败：" + e.getMessage(), "");
		}
	}
	

	/**
	 * 获取建筑业企业资信信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/downAsdCertnum", method = RequestMethod.POST)
	public String downAsdCertnum(@RequestBody String params) {
		try {
			log.info("=======开始调用downAsdCertnum接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String cert_identifier = obj.getString("cert_identifier");
				String certNum = obj.getString("certNum");
				String userguid = obj.getString("userguid");
				String username = obj.getString("username");
				JSONObject result = new JSONObject();
				
				String attachguid = UUID.randomUUID().toString();
				String CliengGuid = obj.getString("cliengguid");;
				String getpdfreason = "{\"head\": {\"accessId\": \"" + accessId + "\",\"accessToken\": \"" + accessToken
						+ "\"},\"data\": {\"certIdentifier\": \"" + cert_identifier
						+ "\", \"useCause\":\"济宁政务服务\",\"expiryTime\": \"\"}}";
				String getpdfhttpUrl = "http://59.206.96.173:8080/main/CertShare/downloadUrl";
				String getpdfresult = TaHttpRequestUtils.sendPost(getpdfhttpUrl, getpdfreason, "", "");
				log.info("下载附件接口："+getpdfresult);
				JSONObject getpdfresultjson = JSONObject.parseObject(getpdfresult);
				JSONObject getpdfheadjson = getpdfresultjson.getJSONObject("head");
				if ("0".equals(getpdfheadjson.getString("status"))) {

					byte[] decrypt2 = SM2Utils.decrypt(Util.hexToByte(privatekey),
							Util.hexToByte(getpdfresultjson.getString("data")));

					// 将字节数组转为字符串
					String getpdfbackresult = new String(decrypt2, "utf-8");

					JSONObject getpdfbacjson = JSONObject.parseObject(getpdfbackresult);
					String getpdfcontent = getpdfbacjson.getString("content");
					// getpdfcontent = "http://59.206.96.173:8080" +
					// getpdfcontent;

					URL url = new URL(getpdfcontent);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 设置超时间
					conn.setConnectTimeout(10 * 1000); // 防止屏蔽程序抓取而返回403错误
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
					InputStream inputStream = conn.getInputStream();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len;
					while ((len = inputStream.read(buffer)) > -1) {
						baos.write(buffer, 0, len);
					}
					baos.flush();

					InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());

					FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
					long size = (long) inputStream.available();
					frameAttachInfo.setAttachGuid(attachguid);
					frameAttachInfo.setCliengGuid(CliengGuid);
					frameAttachInfo.setAttachFileName(certNum + ".pdf");
					frameAttachInfo.setCliengTag("电子身份证");
					frameAttachInfo.setUploadUserGuid(userguid);
					frameAttachInfo.setUploadUserDisplayName(username);
					frameAttachInfo.setUploadDateTime(new Date());
					frameAttachInfo.setContentType(".pdf");
					frameAttachInfo.setAttachLength(size);
					attachService.addAttach(frameAttachInfo, stream1);
					
					result.put("attachguid", attachguid);
					result.put("cliengguid", CliengGuid);
					return JsonUtils.zwdtRestReturn("1", "附件上传成功！", result.toString());
				} else {
					return JsonUtils.zwdtRestReturn("0", "接口调用失败！", "");
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======downAsdCertnum接口参数：params【" + params + "】=======");
			log.info("=======downAsdCertnum异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 获取当前时间戳
	 *
	 */
	public static String gettime() {
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(datetime);
		return time;
	}

	public static String getMD5(String s) throws UnsupportedEncodingException {
		if (s == null)
			return "";
		else
			return getMD5(s.getBytes("utf-8"));
	}

	public static String getMD5(byte abyte0[]) {
		String s = null;
		char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(abyte0);
			byte abyte1[] = messagedigest.digest();
			char ac1[] = new char[32];
			int i = 0;
			for (int j = 0; j < 16; j++) {
				byte byte0 = abyte1[j];
				ac1[i++] = ac[byte0 >>> 4 & 15];
				ac1[i++] = ac[byte0 & 15];
			}

			s = new String(ac1);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return s;
	}

}
