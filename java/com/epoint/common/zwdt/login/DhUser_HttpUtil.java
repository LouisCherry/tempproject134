package com.epoint.common.zwdt.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.zwdt.login.dhrsautil.AESUtil;
import com.epoint.common.zwdt.login.dhrsautil.RsatureUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @version [版本号, 2019年1月10日]
 * @作者 zhaoy
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DhUser_HttpUtil {
    /**
     * 数据库操作DAO
     */
    // 审批库
    protected ICommonDao commonDao;

    private static String dhrest = ConfigUtil.getConfigValue("jnlogin1", "dhrestnew");
    private static String dhsignrest = ConfigUtil.getConfigValue("jnlogin1", "dhsignrest");
    private static String jisSsoLogout = ConfigUtil.getConfigValue("jnlogin1", "jisSsoLogout");
    private static String getdhtokenurl = ConfigUtil.getConfigValue("jnlogin1","getdhtokenurl");

    transient static Logger log = LogUtil.getLog(DhUser_HttpUtil.class);


    /**
     * 验证大汉票据，获取令牌
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getTicketvalidate(String appId, String privateKey, String ticket, String origin) {
        JSONObject rtnjson = new JSONObject();
        String tokendata = "";
        try {
            JSONObject bizparams = new JSONObject();
            JSONObject bizjson = new JSONObject();
            bizjson.put("appMark", appId);
            bizparams.put("ticket", ticket);
            bizjson.put("params", bizparams.toJSONString());
            Map<String, Object> params = getDhParams(appId, privateKey, "ticketValidate", bizjson.toJSONString(), origin);
            log.info("ticket接口入参：" + params);
            String rtnstr = HttpUtil.doPost(dhrest, params);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("200".equals(rtnjson.getString("code"))) {
                    String secret = rtnjson.getString("data");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret",secret);
                    jsonObject.put("privateKey",privateKey);
                    String data = HttpUtil.doPostJson(getdhtokenurl,jsonObject.toJSONString());
                    rtnjson = JSON.parseObject(data);
                    log.info("rtnjson："+rtnjson);
                    String retcode = rtnjson.getString("retcode");
                    if ("000000".equals(retcode)) {
                        tokendata = rtnjson.getString("data");
                        rtnjson = JSON.parseObject(tokendata);
                    }
                } else {
                    log.info("【获取Ticketvalidate接口调用返回数据错误】>>>>" + rtnjson);
                }
            } else {
                log.info("【获取Ticketvalidate接口无返回值】>>>>" + params);
            }

        } catch (UnsupportedEncodingException e) {
            log.info("【获取Ticketvalidate接口数据异常】>>>>" + ticket + ">>>>" + e.getMessage());
        } catch (Exception e) {
            log.info("【获取Ticketvalidate接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return rtnjson;
    }

    /**
     * 获取大汉个人用户信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getUserInfo(String appId, String privateKey, String origin, String token) {
        JSONObject rtnjson = new JSONObject();
        String rtndata = "";
        try {
            JSONObject bizparams = new JSONObject();
            JSONObject bizjson = new JSONObject();
            bizjson.put("appMark", appId);
            bizparams.put("token", token);
            bizjson.put("params", bizparams.toJSONString());
            Map<String, Object> params = getDhParams(appId, privateKey, "findPerUserByToken", bizjson.toJSONString(), origin);
            String rtnstr = HttpUtil.doPost(dhrest, params);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                log.info("rtnstr:"+rtnjson);
                if ("200".equals(rtnjson.getString("code"))) {
                    String secret = rtnjson.getString("data");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret",secret);
                    jsonObject.put("privateKey",privateKey);
                    String data = HttpUtil.doPostJson(getdhtokenurl,jsonObject.toJSONString());
                    rtnjson = JSONObject.parseObject(data);
                    String retcode = rtnjson.getString("retcode");
                    log.info("rtnjson:"+rtnjson);
                    if ("000000".equals(retcode)) {
                        rtndata = rtnjson.getString("data");
                        rtnjson = JSONObject.parseObject(rtndata);
                    }
                } else {
                    log.info("【获取findPerUserByToken接口调用返回数据错误】>>>>" + rtnjson);
                }
            } else {
                log.info("【获取findPerUserByToken接口无返回值】>>>>" + params);
            }

        } catch (UnsupportedEncodingException e) {
            log.info("【获取findPerUserByToken接口数据异常】>>>>" + token + ">>>>" + e.getMessage());
        } catch (Exception e) {
            log.info("【获取findPerUserByToken接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return rtnjson;
    }

    /**
     * 获取大汉法人用户信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getCoruserInfo(String appId, String privateKey, String origin, String token) {
        JSONObject rtnjson = new JSONObject();
        String rtndata = "";
        try {
            JSONObject bizparams = new JSONObject();
            JSONObject bizjson = new JSONObject();
            bizjson.put("appMark", appId);
            bizparams.put("token", token);
            bizjson.put("params", bizparams.toJSONString());
            Map<String, Object> params = getDhParams(appId, privateKey, "findCorUserByToken", bizjson.toJSONString(), origin);
            String rtnstr = HttpUtil.doPost(dhrest, params);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("200".equals(rtnjson.getString("code"))) {
                    String secret = rtnjson.getString("data");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret",secret);
                    jsonObject.put("privateKey",privateKey);
                    String data = HttpUtil.doPostJson(getdhtokenurl,jsonObject.toJSONString());
                    rtnjson = JSONObject.parseObject(data);
                    String retcode = rtnjson.getString("retcode");
                    if ("000000".equals(retcode)) {
                        rtndata = rtnjson.getString("data");
                        rtnjson = JSONObject.parseObject(rtndata);
                    }
                } else {
                    log.info("【获取findcoruserytoken接口调用返回数据错误】>>>>" + rtnjson);
                }
            } else {
                log.info("【获取findcoruserytoken接口无返回值】>>>>" + params);
            }

        } catch (UnsupportedEncodingException e) {
            log.info("【获取findcoruserytoken接口数据异常】>>>>" + token + ">>>>" + e.getMessage());
        } catch (Exception e) {
            log.info("【获取findcoruserytoken接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return rtnjson;
    }

    /**
     * 获取大汉法人企业信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getCorInfo(String appId, String privateKey, String origin, String token, String pagesindex) {
        JSONObject rtnjson = new JSONObject();
        String rtndata = "";
        try {
            JSONObject bizparams = new JSONObject();
            JSONObject bizjson = new JSONObject();
            bizjson.put("app_id", appId);
            bizjson.put("servicename", "findCorporationByToken");
            bizparams.put("token", token);
            bizparams.put("pageindex", pagesindex);
            bizparams.put("pagenum", "5");
            bizjson.put("params", bizparams.toJSONString());
            Map<String, Object> params = getDhParams(appId, privateKey, "findcorporationbytoken", bizjson.toJSONString(), origin);
            String rtnstr = HttpUtil.doPost(dhrest, params);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("200".equals(rtnjson.getString("code"))) {
                    String secret = rtnjson.getString("data");
                    String data = RsatureUtil.decrypt(secret, RsatureUtil.getPrivateKey(privateKey));
                    data = (String) JSONObject.parse(data);
                    rtnjson = JSONObject.parseObject(data);
                    String retcode = rtnjson.getString("retcode");
                    if ("000000".equals(retcode)) {
                        rtndata = rtnjson.getString("data");
                        rtnjson = JSONObject.parseObject(rtndata);
                    }
                } else {
                    log.info("【获取findcoruserytoken接口调用返回数据错误】>>>>" + rtnjson);
                }
            } else {
                log.info("【获取findcoruserytoken接口无返回值】>>>>" + params);
            }

        } catch (UnsupportedEncodingException e) {
            log.info("【获取findcoruserytoken接口数据异常】>>>>" + token + ">>>>" + e.getMessage());
        } catch (Exception e) {
            log.info("【获取findcoruserytoken接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return rtnjson;
    }

    /**
     * 注销登陆
     */
    public static String logout(String appmark, String gotourl) {
        String url = jisSsoLogout + "?appMark=" + appmark + "&backUrl=" + gotourl + "&isBackToApp=true";
        return url;
    }

    /**
     * 生成第三方票据信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static JSONObject generateticket(String appId, String privateKey, String origin, String token, String proxyapp, String appword) {
        JSONObject rtnjson = new JSONObject();
        String rtndata = "";
        try {
            JSONObject bizparams = new JSONObject();
            JSONObject bizjson = new JSONObject();
            bizjson.put("app_id", appId);
            bizjson.put("servicename", "generateTicket");
            bizparams.put("token", token);
            bizparams.put("proxyapp", proxyapp);
            bizjson.put("params", AESUtil.encrypt(bizparams.toJSONString(), appword));
            Map<String, Object> params = getDhParams(appId, privateKey, "generateticket", bizjson.toJSONString(), origin);
            String rtnstr = HttpUtil.doPost(dhrest, params);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("200".equals(rtnjson.getString("code"))) {
                    String secret = rtnjson.getString("data");
                    String data = RsatureUtil.decrypt(secret, RsatureUtil.getPrivateKey(privateKey));
                    data = (String) JSONObject.parse(data);
                    rtnjson = JSONObject.parseObject(data);
                    String retcode = rtnjson.getString("retcode");
                    if ("000000".equals(retcode)) {
                        rtndata = rtnjson.getString("data");
                        rtnjson = JSONObject.parseObject(rtndata);
                    }
                } else {
                    log.info("【获取generateticket接口调用返回数据错误】>>>>" + rtnjson);
                }
            } else {
                log.info("【获取generateticket接口无返回值】>>>>" + params);
            }

        } catch (UnsupportedEncodingException e) {
            log.info("【获取generateticket接口数据异常】>>>>" + token + ">>>>" + e.getMessage());
        } catch (Exception e) {
            log.info("【获取generateticket接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return rtnjson;
    }

    public static Map<String, Object> getDhParams(String appId, String privateKey, String interfaceId, String bizContent, String origin) throws UnsupportedEncodingException {
        // 设置参数
        Map<String, String> map = new HashMap<>(16);
        // 应用唯一标识（JMAS平台获取）
        map.put("app_id", appId);
        // 接口唯一标识（JMAS平台获取）
        map.put("interface_id", interfaceId);
        // 版本号(格式:x.x, 例如:1.0)
        map.put("version", "1.0");
        // 三方接口的参数，json格式：{"键":"值", "键":"值"}
        map.put("biz_content", bizContent);
        // 编码格式, 默认UTF-8
        String charset = "UTF-8";
        map.put("charset", charset);
        // 时间戳，毫秒为单位
        long timestamp = System.currentTimeMillis();
        map.put("timestamp", timestamp + "");
        // 来源 0：PC；1：APP；2：支付宝；3：微信
        map.put("origin", "0");
        //sign要通过接口获取-- 山东省统一认证新要求
        String sign = getSign(appId, interfaceId, bizContent);
        map.put("sign", sign);
        Map<String, Object> map2 = new HashMap<>();
        map2.putAll(map);
        return map2;
    }

    /**
     * 获取sign
     */
    public static String getSign(String appId, String interfaceId, String bizContent) {
        JSONObject rtnjson = new JSONObject();
        String sign = "";
        try {
            // 设置参数
            Map<String, Object> map = new HashMap<>(16);
            // 应用唯一标识（JMAS平台获取）
            map.put("app_id", appId);
            // 接口唯一标识（JMAS平台获取）
            map.put("interface_id", interfaceId);
            // 版本号(格式:x.x, 例如:1.0)
            map.put("version", "1.0");
            // 三方接口的参数，json格式：{"键":"值", "键":"值"}
            map.put("biz_content", bizContent);
            // 编码格式, 默认UTF-8
            String charset = "UTF-8";
            map.put("charset", charset);
            // 时间戳，毫秒为单位
            long timestamp = System.currentTimeMillis();
            map.put("timestamp", timestamp + "");
            // 来源 0：PC；1：APP；2：支付宝；3：微信
            map.put("origin", "0");
            String rtnstr = HttpUtil.doPost(dhsignrest, map);
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("true".equals(rtnjson.getString("success"))) {
                    String secret = rtnjson.getString("data");
                    rtnjson = JSON.parseObject(secret);
                    sign = rtnjson.getString("sign");
                }
            } else {
                log.info("【获取大汉签名接口无返回值】>>>>" + map);
            }
        } catch (Exception e) {
            log.info("【获取大汉签名接口解析返回数据异常】>>>>" + rtnjson + ">>>>" + e.getMessage());
            e.printStackTrace();
        }
        return sign;
    }


}
