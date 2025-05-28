
package com.epoint.auditproject.auditproject.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;

@RestController
@RequestMapping("/jnbigshowcert")
public class JnCertReduceController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	
	 //获取参数信息
    private static String appCode = ConfigUtil.getConfigValue("bigshow", "appcode");
    private static String accessToken = ConfigUtil.getConfigValue("bigshow", "accesstoken");
    private static String url = ConfigUtil.getConfigValue("bigshow", "bigshowurl");
    
	 @Autowired
	private IAttachService attachService;
	 
	/**
     * 代码项API
     */
     @Autowired
     private ICodeItemsService iCodeItemsService;
	 
	/**
     * 事项扩展信息API
     */
     @Autowired
     private IJnProjectService iJnProjectService;


	/**
	 * 获取建筑业企业资信信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getLicenceCertNo", method = RequestMethod.POST)
	public String getLicenceCertNo(@RequestBody String params) {
		try {
			log.info("=======开始调用getLicenceCertNo接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String certificateNo = obj.getString("certificateNo");
				String time = new Date().getTime() + "";
				String sign = getMD5("certificateNo="+certificateNo+"&appCode="+appCode +"&time="+ time+"&accessToken="+accessToken);
				String apiurl =  url + "getLicenceListByCertificateTypeAndCertificateNo?";
				apiurl += "certificateNo="+certificateNo+"&appCode="+appCode+"&time="+time+"&sign="+sign;
				String result = HttpUtil.doGet(apiurl);
				JSONObject json = JSON.parseObject(result);
				JSONObject certjson = new JSONObject();
				List<String> licens = new ArrayList<String>();
				String retCode = json.getString("retCode");
				if ("SUCCESS".equals(retCode)) {
					JSONArray array = json.getJSONArray("licenseArray");
					if (array != null && array.size() > 0) {
						for (int i =0;i<array.size();i++) {
							JSONObject licencecert = array.getJSONObject(i);
							String licenseNo = licencecert.getString("licenseNo");
							String licenseTypeName = licencecert.getString("licenseTypeName");
							List<CodeItems> items = iCodeItemsService.listCodeItemsByCodeID("1016093");
							if (items != null && items.size() > 0) {
								for (CodeItems codeitem : items) {
									if (licenseTypeName.equals(codeitem.getItemValue())) {
										List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certificateNo, licenseTypeName);
										log.info("attachinfos:"+attachinfos);
										if (attachinfos == null || attachinfos.size() == 0) {
											licens.add(licenseNo);
										}
									}
								}
							}
						}
					}
					certjson.put("listnos", licens);
					return JsonUtils.zwdtRestReturn("1", "获取证照列表成功！", certjson.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "获取证照列表失败！", "");
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getLicenceCertNo接口参数：params【" + params + "】=======");
			log.info("=======getLicenceCertNo异常信息：" + e.getMessage() + "=======");
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
	@RequestMapping(value = "/getLicenceInfoAndPicture", method = RequestMethod.POST)
	public String getLicenceInfoAndPicture(@RequestBody String params) {
		try {
			log.info("=======开始调用getLicenceInfoAndPicture接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String licenseNo = obj.getString("licenseNo");
				String time = new Date().getTime() + "";
				String sign = getMD5("licenseNo="+licenseNo+"&appCode="+appCode +"&time="+ time+"&accessToken="+accessToken);
				String apiurl =  url + "getLicenceInfoAndPicture?licenseNo="+licenseNo+"&appCode="+appCode+"&time="+time+"&sign="+sign;
				String result = HttpUtil.doGet(apiurl);
				JSONObject json = JSON.parseObject(result);
				JSONObject certjson = new JSONObject();
				String retCode = json.getString("retCode");
				if ("SUCCESS".equals(retCode)) {
					JSONArray array = json.getJSONArray("attachment");
					JSONObject metaData = json.getJSONObject("metaData");
					String certificateNo = metaData.getString("certificateNo");
					if (array != null && array.size() > 0) {
						JSONObject certattach = array.getJSONObject(0);
						String attachcontent = certattach.getString("fileContent");
						String fileName = certattach.getString("fileName");
						String fileType1 = "."+certattach.getString("fileType");
						String originalFileName = certattach.getString("originalFileName");
						String[] strs = fileName.split("-");
						String filetype = "";
						if (strs != null &&strs.length > 0 ) {
							filetype = strs[1];
						}else {
							filetype = "其他";
						}
						
						//删除之前同类型的证照
						List<FrameAttachInfo> attachinfos = iJnProjectService.getFrameAttachByIdenumberBigType(certificateNo, filetype);
						if (attachinfos != null && attachinfos.size() > 0) {
							for (FrameAttachInfo attach : attachinfos) {
								attachService.deleteAttachByAttachGuid(attach.getAttachGuid());
							}
						}
						 byte[] pic = Base64Util.decodeBuffer(attachcontent);
			                String attachGuid = UUID.randomUUID().toString();
			                if (pic.length > 0) {
			                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
			                    frameAttachInfo.setAttachGuid(attachGuid);
			                    frameAttachInfo.set("idnumber", certificateNo);
			                    frameAttachInfo.set("bigshowtype", filetype);
			                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
			                    if (StringUtil.isNotBlank(originalFileName)) {
			                    	frameAttachInfo.setAttachFileName(originalFileName);
			                    }else {
			                    	frameAttachInfo.setAttachFileName(fileName);
			                    }
			                    frameAttachInfo.setCliengTag("大数据电子证照");
			                    frameAttachInfo.setUploadUserGuid("");
			                    frameAttachInfo.setUploadUserDisplayName("");
			                    frameAttachInfo.setUploadDateTime(new Date());
			                    frameAttachInfo.setContentType(fileType1);
			                    frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
			                    ByteArrayInputStream input = new ByteArrayInputStream(pic);
			                    
			                    attachService.addAttach(frameAttachInfo, input);
			                    input.close();
			                }
			                certjson.put("attachguid", attachGuid);
					}
					return JsonUtils.zwdtRestReturn("1", "获取证照详情成功！", certjson.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "获取证照详情失败！", "");
				}
				
				
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getLicenceInfoAndPicture接口参数：params【" + params + "】=======");
			log.info("=======getLicenceInfoAndPicture异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照详情失败：" + e.getMessage(), "");
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
    
    
    public static String getMD5(String s) throws UnsupportedEncodingException 
    {
        if(s == null)
            return "";
        else
            return getMD5(s.getBytes("utf-8"));
}

    public static String getMD5(byte abyte0[])
    {
        String s = null;
        char ac[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f'
        };
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(abyte0);
            byte abyte1[] = messagedigest.digest();
            char ac1[] = new char[32];
            int i = 0;
            for(int j = 0; j < 16; j++)
            {
                byte byte0 = abyte1[j];
                ac1[i++] = ac[byte0 >>> 4 & 15];
                ac1[i++] = ac[byte0 & 15];
            }

            s = new String(ac1);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return s;
    }	


}
