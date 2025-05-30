package com.epoint.utils;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;


/**
 * 
 * 浪潮数据推送工具类
 * @作者 lsting
 * @version [版本号, 2018年10月14日]
 */
@SuppressWarnings("deprecation")
public class WavePushInterfaceUtils
{
    /**
     * 获取系统参数API
     */
    private IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
	private static final String QfWavePreFixUrl = "http://59.206.96.199:8080/";
	private static final String QfWavePreFixUrlgg = "http://59.206.96.199:8070/";




    /**
     * 根据事项ID生成受理编号和查询密码
     *  @param params
     *  @param request
     *  @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("static-access")
    public static JSONObject createReceiveNum(String paramsGet) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrl + "web/approval/createReceiveNum";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.getHttp(interFaceUrl, paramsGet);
        }
        catch (JSONException e) {
        }
        return responseJsonObj;
    }

    /**
     * 根据单位获取已发布事项列表的相关信息
     *  @param params
     *  @param request
     *  @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("static-access")
    public JSONObject getItemList(String paramsGet) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrl + "main/power/getItemList";
        System.out.println("获取事项code接口请求地址及参数：" + interFaceUrl);
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.getHttp(interFaceUrl, paramsGet);
        }
        catch (JSONException e) {
        }
        return responseJsonObj;
    }

    /**
     * 申报数据对接
     * @param paramsJson
     * @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"static-access" })
    public JSONObject acceptDataInfo(Map<String, Object> paramsMap) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrl + "web/approval/acceptforother";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.postHttp(interFaceUrl, paramsMap);
        }
        catch (JSONException e) {

        }
        return responseJsonObj;
    }
    
    
    
    /**
     * 申报数据对接
     * @param paramsJson
     * @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"static-access" })
    public JSONObject acceptDataInfogg(Map<String, Object> paramsMap) throws IOException {
        // 接口返回值
        JSONObject responseJsonObj = new JSONObject();
        // 请求接口地址构造
        String interFaceUrl = QfWavePreFixUrlgg + "web/approval/acceptforother";
        // 开始请求接口
        try {
            responseJsonObj = HttplcUtils.postHttp(interFaceUrl, paramsMap);
        }
        catch (JSONException e) {

        }
        return responseJsonObj;
    }
	/**
	 * 向服务器发送文件
	 * 
	 * @param file_map
	 * @param file_url
	 * @param server_url
	 * @return
	 */
	public String startUploadService(Map<String, String> params, String file_url, String server_url) {
		final String CHARSET = HTTP.UTF_8;
		try {
			// 开启上传队列
			File file = null;
			if (!file_url.equals("")) {
				file = new File(file_url);
			}

			HttpClient httpclient = new DefaultHttpClient();
			// 设置通信协议版本
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			// 文件参数设置
			HttpPost httppost = new HttpPost(server_url);
			MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName(CHARSET));
			if (params != null && !params.isEmpty()) {
				// 编码参数
				/*
				 * for (String k : params.keySet()) { StringBody valueBody = new
				 * StringBody(params.get(k),Charset.forName(CHARSET));
				 * mpEntity.addPart(k, valueBody); }
				 */

				for (Entry<String, String> entry : params.entrySet()) {
					StringBody valueBody = new StringBody(entry.getValue(), Charset.forName(CHARSET));
					mpEntity.addPart(entry.getKey(), valueBody);
				}

			}
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart("file", cbFile);
			httppost.setEntity(mpEntity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				EntityUtils.toString(response.getEntity(), CHARSET);
				// String sss = EntityUtils.toString(response.getEntity(),
				// CHARSET);
				// throw new RuntimeException("请求失败");
				return "{\"code\":\"4000\", \"msg\":\"URL请求失败\", \"result\":\'" + response.getStatusLine().toString()
						+ "\'}";
			}
			String result = (resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
			return result;
		} catch (UnsupportedEncodingException e) {
			return "{\"code\":\"4002\", \"msg\":\"URL请求失败\", \"result\":\'UnsupportedEncodingException:"
					+ e.getMessage() + "\'}";
		} catch (ClientProtocolException e) {
			return "{\"code\":\"4003\", \"msg\":\"URL请求失败\", \"result\":\'ClientProtocolException:" + e.getMessage()
					+ "\'}";
		} catch (IOException e) {
			return "{\"code\":\"4001\", \"msg\":\"URL请求失败\", \"result\":\'IOException:" + e.getMessage() + "\'}";
		}
	}

}
