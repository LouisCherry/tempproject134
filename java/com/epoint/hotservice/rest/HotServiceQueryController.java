package com.epoint.hotservice.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.znsb.jnzwfw.selfservicemachine.JnSelfservicemachineSignUtil;
import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/hotServiceQuery")
public class HotServiceQueryController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 事项队列分类相关api
     */
    @Autowired
    IAuditQueueTasktypeTask iAuditQueueTasktypeTask;
    /**
     * 事项队列相关api
     */
    @Autowired
    IAuditQueueTasktype iAuditQueueTasktype;

    /**
     * 队列相关api
     */
    @Autowired
    IAuditQueue iAuditQueue;

    /**
     * 办理队列相关api
     */
    @Autowired
    IHandleQueue iHandleQueue;
    
    final static String appKey = "b97f78b8c85f419cb2da32603ea8dc68";
    final static String appsecret = "U2FsdGVkX18y2KN9pRiz5soKFbZAtoEEHEmuQrwLC1AJ";
    
    final static String webApiUrl = "http://59.206.96.143:8080/IDRIProjectForPublicService";
    /**
     * 红名单企业查询
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getBusinessCreditInfo", method = RequestMethod.POST)
    public String getBusinessCreditInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String creditCode = obj.getString("creditCode");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnfg_hmd1";
            String ApiKey = "596738364502179840";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();
            //红名单
            JSONObject responseJsonObj = new JSONObject();
            Map<String, Object> val = new HashMap<>();
            val.put("creditCode", creditCode);
            val.put("flag", "1");
            String sign = JnSelfservicemachineSignUtil.getSign(val, appsecret);
            String url = httpurl + "?appKey=" + appKey + "&creditCode=" + creditCode + "&flag=1&sign=" + sign;
            String str = getHttp(url, bodyRequest, headerRequest);
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);
            dataJson.put("redList", responseJsonObj);
            //黑名单
            JSONObject responseJsonObj2 = new JSONObject();
            Map<String, Object> val2 = new HashMap<>();
            val2.put("creditCode", creditCode);
            val2.put("flag", "2");
            String sign2 = JnSelfservicemachineSignUtil.getSign(val, appsecret);
            String url2 = httpurl + "?appKey=" + appKey + "&creditCode=" + creditCode + "&flag=2&sign=" + sign2;
            String str2 = getHttp(url2, bodyRequest, headerRequest);
            responseJsonObj2 = (JSONObject) JSONObject.parse(str2);
            String data2 = responseJsonObj.getString("data");
            responseJsonObj2 = (JSONObject) JSONObject.parse(data2);
            dataJson.put("blackList", responseJsonObj2);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 黑名单企业查询
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getblacklist", method = RequestMethod.POST)
    public String getBlackList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String creditCode = obj.getString("creditCode");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnfg_hmd";
            String ApiKey = "596738138009763840";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();
            Map<String, Object> val = new HashMap<>();
            val.put("creditCode", creditCode);
            val.put("flag", "2");
            String sign = JnSelfservicemachineSignUtil.getSign(val, appsecret);
            String url = httpurl + "?appKey=" + appKey + "&creditCode=" + creditCode + "&flag=2&sign=" + sign;
            String str = getHttp(url, bodyRequest, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);
            dataJson.put("hhbList", responseJsonObj);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }
    
	/**
	 * 连接超时 15秒
	 */
	private static final int DEFAULT_TIME_OUT = 15 * 1000;

	/**
	 * 默认编码UTF-8
	 */
	private static final String DEFAULT_ENCODE = "UTF-8";

	public static String getHttp(String url, Map<String, String> bodayParams, Map<String, String> headerParams)
			throws IOException {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
		GetMethod getMethod = new GetMethod(url);
		getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		if (headerParams != null) {
			for (String key : headerParams.keySet()) {
				getMethod.addRequestHeader(key, headerParams.get(key));
			}
		}
		HttpMethodParams httpMethodParams = new HttpMethodParams();
		if (bodayParams != null) {
			for (String key : bodayParams.keySet()) {
				httpMethodParams.setParameter(key, bodayParams.get(key));
			}
		}

		getMethod.setParams(httpMethodParams);
		try {
			httpClient.executeMethod(getMethod);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = getMethod.getResponseBodyAsStream();
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			responseMsg = out.toString(DEFAULT_ENCODE);
		} finally {
			getMethod.releaseConnection();
		}
		return responseMsg;
	}


    /*********************************************驾驶证查询接口*******************************************************/

    @RequestMapping(value = "/getYzm", method = RequestMethod.POST)
    public String getYzm(@RequestBody String params) {
        String result = "";
        try {
            // 获取查询使用的验证码
            log.info("=======开始调用getYzm接口=======" );

            Map<String, String> signMap = new HashMap<String, String>();
            signMap.put("params", "{}");
            signMap.put("sessionGuid", "");
            String httpUrl = webApiUrl+"/rest/interfaceForHuna/getYzm";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用getYzm接口=======" );
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getYzm接口参数：params【" + params + "】=======");
            log.info("=======getYzm异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用getYzm接口=======" );
            return result;
        }

    }

    @RequestMapping(value = "/drivequery", method = RequestMethod.POST)
    public String drivequery(@RequestBody String params) {
        String result = "";
        try {
            // 驾驶证积分查询的接口
            log.info("=======开始调用drivequery接口=======" );

            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String jszh = obj.getString("jszh");
            String dabh = obj.getString("dabh");
            String yzm = obj.getString("yzm");
            String parameter = "{\"jszh\":\"" + jszh + "\",\"dabh\":\"" + dabh + "\",\"yzm\":\"" + yzm + "\"}";
            Map<String, String> signMap = new HashMap<String, String>();
            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");
            String httpUrl = webApiUrl + "/rest/interfaceForHuna/getDriverLicense";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用drivequery接口=======" );
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======drivequery接口参数：params【" + params + "】=======");
            log.info("=======drivequery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用drivequery接口=======" );
            return result;
        }

    }

    @RequestMapping(value = "/illegalquery", method = RequestMethod.POST)
    public String illegalquery(@RequestBody String params) {

        String result = "";
        try {
            // 违章查询的接口
            log.info("=======开始调用illegalquery接口=======" );

            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String hpzl = obj.getString("hpzl");
            String hphm1b = obj.getString("hphm1b");
            String fdjh = obj.getString("fdjh");
            String yzm = obj.getString("yzm");

            Map<String, String> signMap = new HashMap<String, String>();

            String parameter = "{\"hpzl\":\"" + hpzl + "\",\"hphm1b\":\"" + hphm1b + "\",\"fdjh\":\"" + fdjh
                    + "\",\"yzm\":\"" + yzm + "\"}";

            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");

            String httpUrl = webApiUrl + "/rest/interfaceForHuna/getVio";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用illegalquery接口=======" );
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======illegalquery接口参数：params【" + params + "】=======");
            log.info("=======illegalquery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用illegalquery接口=======" );
            return result;
        }
    }

    /*********************************************微信快递查询接口*******************************************************/
    @RequestMapping(value = "/expressquery", method = RequestMethod.POST)
    public String expressquery(@RequestBody String params) {

        String result = "";
        try {
            // 违章查询的接口
            log.info("=======开始调用expressquery接口=======" );
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String postid = obj.getString("postid");
            Map<String, String> signMap = new HashMap<String, String>();
            String parameter = "{\"postid\":\"" + postid + "\"}";
            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");
            String httpUrl = webApiUrl + "/rest/interfaceForKuaidi100/searchExpressInfoTwo";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用expressquery接口=======");
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======expressquery接口参数：params【" + params + "】=======");
            log.info("=======expressquery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用expressquery接口=======" );
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！", "");
        }
    }

    /***************************************************公交线路查询接口*********************************************************/
    /**
     * 公交站点接口
     * @authory shibin
     * @version 2019年10月19日 上午10:27:30
     * @param params
     * @return
     */
    @RequestMapping(value = "/queryBusstops", method = RequestMethod.POST)
    public String queryBusstops(@RequestBody String params) {

        String result = "";
        try {
            log.info("=======开始调用queryBusstops接口=======" );
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String address = obj.getString("address");
            Map<String, String> signMap = new HashMap<String, String>();
            String parameter = "{\"cityNo\":\"286\",\"address\":\"" + address + "\"}";
            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");
            String httpUrl = webApiUrl + "/rest/interfaceForBaiduAPI/getLocalBusStationInfo";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            JSONObject json1 = JSON.parseObject(result);
            JSONArray json3 =  (JSONArray) json1.get("content");
            List<JSONObject> list = new ArrayList<>();
            Record record = null;
            JSONObject job = null;
            JSONObject jsonObject = null;
            //1、循环遍历这个数组
            for (int i = 0; i < json3.size(); i++) {
                //2、把里面的对象转化为JSONObject
                jsonObject = new JSONObject();
                job = json3.getJSONObject(i);
                record = new Record();
                //3、把里面想要的参数一个个用.属性名的方式获取到
                jsonObject.put("name", job.get("name"));
                jsonObject.put("x", job.get("x"));
                jsonObject.put("y", job.get("y"));
                jsonObject.put("uid", job.get("uid"));
                list.add(jsonObject);
            }
            log.info("=======结束调用queryBusstops接口=======" );
            JSONObject dataJson = new JSONObject();
            dataJson.put("info", list);
            return dataJson.toJSONString();
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======queryBusstops接口参数：params【" + params + "】=======");
            log.info("=======queryBusstops异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "queryBusstops接口调用异常：" + e.getMessage(), "");
        }
    }

    /**
     * 公交线路接口
     * @authory shibin
     * @version 2019年10月19日 上午10:27:30
     * @param params
     * @return
     */
    @RequestMapping(value = "/queryBusRoutes", method = RequestMethod.POST)
    public String queryBusRoutes(@RequestBody String params) {

        String result = "";
        try {
            log.info("=======开始调用queryBusRoutes接口=======" );
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String from_statName = obj.getString("from_statName");
            String from_statUid = obj.getString("from_statUid");
            String from_startX = obj.getString("from_startX");
            String from_startY = obj.getString("from_startY");
            String to_statName = obj.getString("to_statName");
            String to_statUid = obj.getString("to_statUid");
            String to_startX = obj.getString("to_startX");
            String to_startY = obj.getString("to_startY");

            Map<String, String> signMap = new HashMap<String, String>();

            String parameter = "{\"cityNo\":\"286\",\"statName\":\"" + from_statName + "\",\"statUid\":\""
                    + from_statUid + "\",\"startX\":\"" + from_startX + "\",\"startY\":\"" + from_startY
                    + "\",\"endName\":\"" + to_statName + "\",\"endUid\":\"" + to_statUid + "\",\"endX\":\"" + to_startX
                    + "\",\"endY\":\"" + to_startY + "\"}";

            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");

            String httpUrl = webApiUrl + "/rest/interfaceForBaiduAPI/getTwoStationLines";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);

            return result;
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======queryBusRoutes接口参数：params【" + params + "】=======");
            log.info("=======queryBusRoutes异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "queryBusRoutes接口调用异常：" + e.getMessage(), "");
        }
    }

}
