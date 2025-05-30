
package com.epoint.xmz.jishitong;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/jnjishitong")
public class JnJiShiTongController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String JST_WEB_URL= ConfigUtil.getConfigValue("datasyncjdbc", "JST_WEB_URL");
	public static final String JST_WEB_USER_LOGIN_NAME= ConfigUtil.getConfigValue("datasyncjdbc", "JST_WEB_USER_LOGIN_NAME");
	public static final String JST_WEB_USER_LOGIN_PASSWORD= ConfigUtil.getConfigValue("datasyncjdbc", "JST_WEB_USER_LOGIN_PASSWORD");
	public static final String JST_OPERATORID= ConfigUtil.getConfigValue("datasyncjdbc", "JST_OPERATORID");
	public static final String JST_OPERATORNAME= ConfigUtil.getConfigValue("datasyncjdbc", "JST_OPERATORNAME");
	
		
	@Autowired
	private IAttachService attachService;
	 
	/**
	 * 获取即时通出示对应的附件
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getjstfile", method = RequestMethod.POST)
	public String getAccessToken(@RequestBody String params) {
		try {
			log.info("=======开始调用getjstfile接口=======");
			JSONObject jsonObject = JSONObject.parseObject(params);
			String code = jsonObject.getString("code");
			String cliengguid =  jsonObject.getString("cliengguid");
			if (StringUtil.isBlank(cliengguid)) {
				cliengguid = UUID.randomUUID().toString();
			}
			
			//1、获取token
			JSONObject getTokenJson = new JSONObject();
			getTokenJson.put("userName", JST_WEB_USER_LOGIN_NAME);
			getTokenJson.put("password", JST_WEB_USER_LOGIN_PASSWORD);
			String token = getToken(getTokenJson);
			JSONObject dataJson = new JSONObject();
			
			if (StringUtil.isNotBlank(token)) {
				String eventid = "";
				JSONObject sendMsgJson = new JSONObject();
				String receiveMsgType = "";
				//(1)....手机出示付资料码或出示码，等待电脑扫描......手机端的工作
				//(2)电脑：扫描手机的付资料码。调用接口解析码内容。（接口：queryQrCodeInfo）
				JSONObject qrCodeJson = new JSONObject();
				qrCodeJson.put("code", code); //通过扫描手机的二维码得到code
				JSONObject codeObj = queryQrCodeInfo(qrCodeJson, token);
				log.info("济时通扫描二维码返回信息："+codeObj);
				if(codeObj.getJSONObject("data").getJSONObject("data").getString("codetype").equals("AUTOPAY")){
					//二维码是付资料码
					//电脑（使用方）：创建“申请资料流转”事项（接口：createEventByTo）
					JSONObject createEventJson = new JSONObject();
					createEventJson.put("userid", JST_OPERATORID);
					createEventJson.put("username", JST_OPERATORNAME);
					createEventJson.put("eventexplain", "");//业务说明
					JSONObject dataObj = new JSONObject();//构造data数据
					dataObj.put("assetstypeid","");
					dataObj.put("assetstypename", "");
					dataObj.put("operation", "AUTH");
					dataObj.put("materialname","请求资料");//根据需求自己定义名称
					JSONArray operationexpration = new JSONArray();
					JSONObject operationObject = new JSONObject();
					operationObject.put("name", "出示");
					operationObject.put("id", "SHOW");
					operationexpration.put(operationObject);
					dataObj.put("operationexpration",operationexpration);
					JSONArray third = new JSONArray();
					JSONObject thirdObject = new JSONObject();
					third.put(thirdObject);
					dataObj.put("third",third);
					JSONArray data = new JSONArray();
					data.put(dataObj);
					//如果需要授权多个资产，可以仿照上文的dataObj构造JSONObject，最后放入data中
					JSONObject dataObj2 = new JSONObject();
					//dataObj2构造过程参考上文的dataObj
					data.put(dataObj2);
					createEventJson.put("data", data);
					eventid = createEventByTo(createEventJson, token);
					log.info("创建申请资料流转事项的eventid为："+eventid);

					//电脑：从码的解析结果中获取手机账户信息
					String fileOwnerId = codeObj.getJSONObject("data").getJSONObject("data").getString("userid");//获得资料持有方id
					sendMsgJson = new JSONObject();
					//消息由资料使用方发送给资料持有方
					sendMsgJson.put("fromid", JST_OPERATORID);
					sendMsgJson.put("toid", fileOwnerId);
					sendMsgJson.put("eventid", eventid);
					sendMsgJson.put("type", "APPLY_AUTH");
					sendMsgJson.put("reason", "");
					sendMsgJson.put("data", data);

					receiveMsgType = "AUTH_APPLY_INFO";
				}else if(codeObj.getJSONObject("data").getJSONObject("data").getString("codetype").equals("AUTH")){
					//二维码是出示码
					JSONObject codeInfoJson = JSON.parseObject(codeObj.getJSONObject("data").getJSONObject("data").getString("data"));
					eventid = codeInfoJson.getString("eventid");
					JSONObject updateEventJson = new JSONObject();
					updateEventJson.put("userid", JST_OPERATORID);
					updateEventJson.put("username", JST_OPERATORNAME);
					updateEventJson.put("eventid", eventid);
					updateEventJson.put("token", token);
					JSONObject jsons  = updateEventByTo(updateEventJson, token);

					String fileOwnerId = codeObj.getJSONObject("data").getJSONObject("data").getString("userid");
					sendMsgJson.put("fromid", JST_OPERATORID);
					sendMsgJson.put("toid", fileOwnerId);
					sendMsgJson.put("eventid", eventid);
					sendMsgJson.put("type", "ACCEPT_AUTH");
					sendMsgJson.put("reason", "");
					JSONObject msgJSON = new JSONObject();
					msgJSON.put("reason","");
					msgJSON.put("accept",true);
					sendMsgJson.put("data",msgJSON);

					receiveMsgType = "AUTH_INFO";
				}

				// 将“申请资料流转”事项消息通知手机。
				JSONObject sendMsgObj = sendMsg(sendMsgJson, token);
				
				log.info("济时通发送消息成功，"+sendMsgObj);

				//(5)....手机收到消息后，进行授权......手机端的工作
				Thread.sleep(5000);
				//(6)电脑轮询：查询手机的授权结果（接口：receiveMsg；type为AUTH_APPLY_INFO）。轮询时间建议1秒。
				JSONObject msgJson = new JSONObject();
				msgJson.put("userid", JST_OPERATORID);
				msgJson.put("type", receiveMsgType);
				msgJson.put("eventid", eventid);
				JSONObject msgObj = receiveMsg(msgJson, token);
				if (!msgObj.getJSONObject("data").getJSONArray("jsonarray").isEmpty()) {
					String msgData = msgObj.getJSONObject("data").getJSONArray("jsonarray").getJSONObject(0).getString("data");
					JSONArray msgArray =  new JSONArray(msgData);
					boolean result = false;
					for(int i=0; i<msgArray.length(); i++){
						String assetsid = msgArray.getJSONObject(i).getString("assetsid");
						String filename = msgArray.getJSONObject(i).getString("assetsname");
						//电脑：查看授权内容（接口：useShow）。注意：只能调用一次，第二次将提示无权限。
						JSONObject useShowJson = new JSONObject();
						useShowJson.put("userid", JST_OPERATORID);
						useShowJson.put("assetsid", assetsid); //这里用从二维码解析出的id
						JSONObject useShowObj = useShow(useShowJson, token); //获得附件内容
						JSONObject attach = (JSONObject) useShowObj.getJSONObject("data").getJSONObject("data").getJSONArray("jsonarray").get(0);
						
						String attachcontent = attach.getString("filecontent");
						String filetype = attach.getString("fileformat");
						log.info("文件类型："+filetype);
						if ("json".equals(filetype.toLowerCase())) {
							result = false;
						}else {
			                byte[] pic = Base64Util.decodeBuffer(attachcontent);
			                String attachGuid = UUID.randomUUID().toString();
			                if (pic.length > 0) {
			                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
			                    frameAttachInfo.setAttachGuid(attachGuid);
			                    frameAttachInfo.setCliengGuid(cliengguid);
			                    frameAttachInfo.setAttachFileName(filename);
			                    frameAttachInfo.setCliengTag("济时通附件");
			                    frameAttachInfo.setUploadUserGuid("");
			                    frameAttachInfo.setUploadUserDisplayName("");
			                    frameAttachInfo.setUploadDateTime(new Date());
			                    frameAttachInfo.setContentType("."+filetype);
			                    frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
			                    ByteArrayInputStream input = new ByteArrayInputStream(pic);
			                    attachService.addAttach(frameAttachInfo, input);
			                    input.close();
			                }
			                result = true;
			                dataJson.put("attachguid", attachGuid);
			                dataJson.put("filetype", filetype);
						}
						
					}
					if (result) {
						return JsonUtils.zwdtRestReturn("1", "获取附件成功", dataJson.toString());
					}else {
						return JsonUtils.zwdtRestReturn("0", "未获取到对应的附件", dataJson.toString());
					}
				}else {
					return JsonUtils.zwdtRestReturn("0", "未获取到对应的附件", dataJson.toString());
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "获取附件失败", dataJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getjstfile接口参数：params【" + params + "】=======");
			log.info("=======getjstfile异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取附件失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 获取token
	 * @param getTokenJson
	 * @return
	 * @throws Exception
	 */
	public static String getToken(JSONObject getTokenJson) throws Exception {
		JSONObject tokenObject = httpUtilSubmitData(JST_WEB_URL + "/logon/logonForInner", getTokenJson.toString(), null);

		if("true".equals(tokenObject.getString("success"))) {
			return tokenObject.getString("data");
		}
		throw new Exception("获取token失败");
	}
	
	/**
	 * Http组装调用
	 * @param urlPath
	 * @param para
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpUtilSubmitData(String urlPath, String para, String token) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";

		try {
			HttpPost httpPost = new HttpPost(urlPath);
			httpPost.addHeader("token", token);
			StringEntity entity = new StringEntity(para, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
			return JSON.parseObject(resultString);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("访问[" + urlPath + "],参数[" + para + "]异常:" + e.getMessage());
		} finally {
			try {
				response.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new Exception("访问[" + urlPath + "],参数[" + para + "]异常:" + e2.getMessage());
			}
		}
	}
	
	/**
	 * 根据code获取二维码的信息
	 * @param qrCodeJson
	 * @return
	 * @throws Exception
	 */
	public static JSONObject queryQrCodeInfo(JSONObject qrCodeJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "QrCodeUtils");
			innerDbc.put("method", "queryQrCodeInfo");
			innerDbc.put("params", qrCodeJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(jsonObject==null||jsonObject.getJSONObject("data")==null){
				throw new Exception("返回的参数为空");
			}
			else if(!jsonObject.getJSONObject("data").getString("errCode").equals("200")){
				throw new Exception(jsonObject.getJSONObject("data").getString("errMsg"));
			}
			else{
				return jsonObject;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("获取二维码信息失败");
		}
	}
	
	/**
	 * 申请资料流转
	 * @param createEventJson
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String createEventByTo(JSONObject createEventJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "MsgUtils");
			innerDbc.put("method", "createEventByTo");
			innerDbc.put("params", createEventJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(jsonObject==null||jsonObject.getJSONObject("data")==null){
				throw new Exception("返回的参数为空");
			}
			else if(!jsonObject.getJSONObject("data").getString("errCode").equals("200")){
				throw new Exception(jsonObject.getJSONObject("data").getString("errMsg"));
			}
			else{
				return jsonObject.getJSONObject("data").getJSONObject("data").getString("eventid");
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("资料流转申请失败");
		}
	}
	
	
	/**
	 * 更新事件
	 * @param updateEventJson
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static JSONObject updateEventByTo (JSONObject updateEventJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "MsgUtils");
			innerDbc.put("method", "updateEventByTo");
			innerDbc.put("params", updateEventJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(jsonObject==null||jsonObject.getJSONObject("data")==null){
				throw new Exception("返回的参数为空");
			}
			else if(!jsonObject.getJSONObject("data").getString("errCode").equals("200")){
				throw new Exception(jsonObject.getJSONObject("data").getString("errMsg"));
			}
			else{
				return jsonObject;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("事件更新失败");
		}
	}
	
	
	/**
	 * 发送消息
	 * @param msgJson
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendMsg(JSONObject msgJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "MsgUtils");
			innerDbc.put("method", "sendMsg");
			innerDbc.put("params", msgJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(!jsonObject.getJSONObject("data").getString("errCode").equals("200")){
				throw new Exception(jsonObject.getJSONObject("data").getString("errMsg"));
			}
			else{
				return jsonObject;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("发送消息失败");
		}
	}
	
	
	/**
	 * 接收消息
	 * @param msgJson
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static JSONObject receiveMsg(JSONObject msgJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "MsgUtils");
			innerDbc.put("method", "receiveMsg");
			innerDbc.put("params", msgJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(jsonObject==null){
				throw new Exception("返回的参数为空");
			}
			else if(!jsonObject.getString("errCode").equals("200")){
				throw new Exception(jsonObject.getString("errMsg"));
			}
			else{
				return jsonObject;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("接收消息失败");
		}
	}
	
	
	/**
	 * 使用方获取资料内容
	 * @param userShowJson
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static JSONObject useShow(JSONObject userShowJson, String token) throws Exception {
		try {
			JSONObject innerDbc = new JSONObject();
			innerDbc.put("className", "ShowUtils");
			innerDbc.put("method", "useShow");
			innerDbc.put("params", userShowJson);
			JSONObject jsonObject = httpUtilSubmitData(JST_WEB_URL + "/logon/executeDbcInterface", innerDbc.toString(), token);
			if(jsonObject==null||jsonObject.getJSONObject("data")==null){
				throw new Exception("返回的参数为空");
			}
			else if(!jsonObject.getJSONObject("data").getString("errCode").equals("200")){
				throw new Exception(jsonObject.getJSONObject("data").getString("errMsg"));
			}
			else{
				return jsonObject;
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("使用方获取资料内容失败");
		}
	}
	
	public static boolean isJson(String content) {
		boolean result = false;
        try {
            Object obj = JSON.parse(content);
            result = true;
            return true;
        } catch (Exception e) {
        	result = false;
        }
		return result;
    }

	

}
