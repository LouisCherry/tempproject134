package com.epoint.dhlogin.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

public class DHSingleLoginRestUtil
{
    //获取参数信息
    private static String resturl = ConfigUtil.getConfigValue("dhlogin", "dhrest");
    private static String appmark = ConfigUtil.getConfigValue("dhlogin", "dhappmark");
    private static String appword = ConfigUtil.getConfigValue("dhlogin", "dhappword");

    transient static Logger log = LogUtil.getLog(DHSingleLoginRestUtil.class);

    /**
     * 
     * @Description: 山东单点登录通用调用
     * @author male   
     * @date 2019年3月26日 下午5:22:32
     * @return String    返回类型    
     * @throws
     */
    public static String getSingleLoginInfo(String param, String servicename) {
        //获取必要参数

        String restAddress = "gateway/interface.do";
        /**
         * 时间戳
         */
        String time = gettime();
        /**
         * sign信息
         */
        String sign = MD5Util.getMD5(appmark + appword + time);

        String getParam = "appmark=" + appmark + "&time=" + time + "&sign=" + sign + "&servicename=" + servicename
                + "&params=" + param;
        //获取返回信息 
        String tokenback = sendGet(resturl + restAddress, getParam, "");

        return tokenback;
    }

    /**
     * 
     * @Description: 解析返回值
     * @author male   
     * @date 2019年3月26日 下午5:36:17
     * @return JSONObject    返回类型    
     * @throws
     */
    public static JSONObject analysisReturn(String returnParams, boolean isSM2) {
        JSONObject tokenobj = JSONObject.parseObject(returnParams);
        JSONObject result = new JSONObject();
        try {
            if (tokenobj != null) {
                if ("000000".equals(tokenobj.getString("retcode"))) {
                    String data = tokenobj.getString("data");
                    if (isSM2) {
                        result.put("sm2", data);
                    }
                    else {
                        result = JSON.parseObject(data);
                    }
                }
                else {
                    result.put("msg", tokenobj.getString("msg"));
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前时间戳
     *
     */
    private static String gettime() {
        Date datetime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(datetime);
        return time;
    }

    /**
     * 
     * @Description: 调用大汉接口解析SM2
     * @author male   
     * @date 2019年3月27日 上午10:14:09
     * @return JSONObject    返回类型    
     * @throws
     */
    public static JSONObject dhSM2Decrypt(String privateKey, String data) {
        JSONObject result = new JSONObject();
        String showData = null;
        if (StringUtil.isNotBlank(privateKey) && StringUtil.isNotBlank(data)) {
            JSONObject sumbit = new JSONObject();
            sumbit.put("decodetext", data);
            sumbit.put("decodekey", privateKey);
            //请求
            showData = getSingleLoginInfo(sumbit.toJSONString(), "SM2decode");
            if (StringUtil.isNotBlank(showData)) {
                //解析
                result = analysisReturn(showData, false);
            }
        }
        return result;
    }

    /**
     * 调用大汉接口进行AES加密
     * @authory shibin
     * @version 2019年10月14日 上午9:41:40
     * @param privateKey
     * @param data
     * @return
     */
    public static String dhAESDecrypt(String loginname, String password) {
        String result = "";
        String showData = null;
        if (StringUtil.isNotBlank(loginname) && StringUtil.isNotBlank(password)) {
            JSONObject sumbit = new JSONObject();
            sumbit.put("loginname", loginname);
            sumbit.put("password", password);
            //请求
            showData = getSingleLoginInfo(sumbit.toJSONString(), "AESEncode");
            if (StringUtil.isNotBlank(showData)) {
                //解析
                result = analysisAESReturn(showData);
            }
        }
        
        return result;
    }

    /**
     * 
     * @Description: 解析返回值
     * @author male   
     * @date 2019年3月26日 下午5:36:17
     * @return JSONObject    返回类型    
     * @throws
     */
    public static String analysisAESReturn(String returnParams) {
        JSONObject tokenobj = JSONObject.parseObject(returnParams);
        String data = "";
        try {
            if (tokenobj != null) {
                if ("000001".equals(tokenobj.getString("retcode"))) {
                    data = tokenobj.getString("data");
                }
                else {
                    data = tokenobj.getString("msg");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String token) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (StringUtil.isNotBlank(param)) {
                if (url.indexOf("?") != -1) {
                    urlNameString = url + "&" + param;
                }
                else {
                    urlNameString = url + "?" + param;
                }
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 在Authentication设置 Bearer+空格+AccessToken
            if (StringUtil.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.debug(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            log.error("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
