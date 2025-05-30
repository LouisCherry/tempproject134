
package com.epoint.xmz.dzyz;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

@RestController
@RequestMapping("/jndzyz")
public class JnDzyzController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static final String dzyz_url = ConfigUtil.getConfigValue("epointframe", "dzyzurl");

    public static final String appid = ConfigUtil.getConfigValue("epointframe", "dzyzappid");

    public static final String appsecret = ConfigUtil.getConfigValue("epointframe", "dzyzappsecret");

    public static final String granttype = ConfigUtil.getConfigValue("epointframe", "dzyzgranttype");

    @Autowired
    private IAttachService attachService;

    /**
     * 获取电子印章系统的认证token
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getaccesstoken", method = RequestMethod.POST)
    public String getAccessToken(@RequestBody String params) {
        try {
            log.info("=======开始调用getAccessToken接口=======");
            // 1、接口的入参转化为JSON对象
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("app_id", appid);
            data.put("app_secret", appsecret);
            data.put("grant_type", granttype);
            String res = HttpUtil.doPostJson(dzyz_url + "/v1/oauth/token", data.toString(), null);
            log.info("getaccesstoken输出：" + res);
            if (StringUtil.isNotBlank(res)) {
                JSONObject json = JSON.parseObject(res);
                String return_message = json.getString("return_message");
                if ("success".equals(return_message)) {
                    JSONObject datas = json.getJSONObject("data");
                    dataJson.put("accesstoken", datas.getString("access_token"));
                    log.info("=======结束调用getAccessToken接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取acctss_token成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAccessToken接口参数：params【" + params + "】=======");
            log.info("=======getAccessToken异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
        }
    }


    /**
     * 查询企业信息接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getcompanydetail", method = RequestMethod.POST)
    public String getCompanyDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getCompanyDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getCompanyDetail入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            String json = jsonObject1.getString("detail");
            String token = jsonObject1.getString("token");
            String sign = SignUtils.createSign(json, appsecret);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("sign", sign);
            headers.put("Authentication", token);
            String result = HttpUtil.doPostJson(dzyz_url + "/v1/user/company/query-user", json, headers);
            log.info("getcompanydetail输出：" + result);
            if (StringUtil.isNotBlank(result)) {
                JSONObject jsonresult = JSON.parseObject(result);
                String result_code = jsonresult.getString("result_code");
                if ("0".equals(result_code)) {
                    JSONObject data = jsonresult.getJSONObject("data");
                    dataJson.put("userid", data.getString("user_id"));
                    log.info("=======结束调用getCompanyDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "查询企业信息接口成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCompanyDetail接口参数：params【" + params + "】=======");
            log.info("=======getCompanyDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询企业信息接口失败：" + e.getMessage(), "");
        }
    }


    /**
     * 上传附件到电子印章系统
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/uploadAttach", method = RequestMethod.POST)
    public String uploadAttach(@RequestBody String params) {
        try {
            log.info("=======开始调用uploadAttach接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject dataJson = new JSONObject();
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            Map<String, String> param = new HashMap<>();
            String cliengguid = jsonObject1.getString("cliengguid");
            List<FrameAttachInfo> attachinfos = attachService.getAttachInfoListByGuid(cliengguid);
            if (attachinfos != null && attachinfos.size() > 0) {
                FrameAttachInfo attachinfo = attachinfos.get(0);
                String attachGuid = attachinfo.getAttachGuid();
                FrameAttachStorage attachStorage = attachService.getAttach(attachGuid);
                String filename = attachinfo.getAttachFileName();
                String flietype = attachinfo.getContentType();
                if (!flietype.contains("pdf")) {
                    return JsonUtils.zwdtRestReturn("0", "签章的文件只能是PDF文件！", "");
                }
                if ("1".equals(attachinfo.getStr("signstatus"))) {
                    return JsonUtils.zwdtRestReturn("0", "该文件已签章！", "");
                }
                String userid = jsonObject1.getString("userid");
                String token = jsonObject1.getString("token");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authentication", token);
                InputStream inputStream = attachStorage.getContent();

                if (StringUtil.isBlank(flietype) || flietype == null) {
                    flietype = ".pdf";
                }
                try {
                    // 添加附件属性
                    param.put("file_name", filename);
                    param.put("file_type", "contract");
                    param.put("user_id", userid);
                    // HttpUtil工具类调用
                    String result =
                            com.epoint.core.utils.httpclient.HttpUtil.upload(dzyz_url + "/v1/file/upload", headers,
                                    param, inputStream, filename);
                    log.info("uploadAttach输出：" + result);
                    JSONObject jsonresult = JSON.parseObject(result);
                    String result_code = jsonresult.getString("result_code");
                    if ("0".equals(result_code)) {
                        JSONObject data = jsonresult.getJSONObject("data");
                        dataJson.put("fileid", data.getString("file_id"));
                        dataJson.put("attachguid", attachinfo.getAttachGuid());
                        log.info("=======结束调用uploadAttach接口=======");
                        return JsonUtils.zwdtRestReturn("1", "文件上传成功", dataJson.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "文件上传失败", dataJson.toString());
                    }
                } catch (Exception e) {
                    return JsonUtils.zwdtRestReturn("0", "文件上传失败", "");
                } finally {
                    // 关闭流
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {

                    }
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "请先上传附件！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======uploadAttach接口参数：params【" + params + "】=======");
            log.info("=======uploadAttach异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "文件上传失败：" + e.getMessage(), "");
        }

    }


    /**
     * 添加合同信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/addcontract", method = RequestMethod.POST)
    public String addContract(@RequestBody String params) {
        try {
            log.info("=======开始调用addContract接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            log.info("addBus入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            String token = jsonObject1.getString("token");
            String json = jsonObject1.getString("detail");
            String sign = SignUtils.createSign(json, appsecret);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("sign", sign);
            headers.put("Authentication", token);
            String result = HttpUtil.doPostJson(dzyz_url + "/v1/contract/add", json, headers);
            log.info("addcontract输出：" + result);
            if (StringUtil.isNotBlank(result)) {
                JSONObject jsonresult = JSON.parseObject(result);
                String result_code = jsonresult.getString("result_code");
                if ("0".equals(result_code)) {
                    JSONObject data = jsonresult.getJSONObject("data");
                    dataJson.put("contractid", data.getString("contract_id"));
                    long expirationtime = new Date(new Date().getTime() + 1000 * 60 * 15).getTime();
                    dataJson.put("expirationtime", expirationtime);
                    log.info("=======结束调用addContract接口=======");
                    return JsonUtils.zwdtRestReturn("1", "添加合同信息成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "添加合同信息失败", result);
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "添加合同信息失败", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addContract接口参数：params【" + params + "】=======");
            log.info("=======addContract异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "添加合同信息失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取签章信息结果
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getReuslt", method = RequestMethod.POST)
    public String getReuslt(@RequestBody String params) {
        try {
            log.info("=======开始调用getReuslt接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("getAttach入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            String businessid = jsonObject.getString("business_id");
            String signstatus = jsonObject.getString("sign_status");
            FrameAttachInfo attachinfo = attachService.getAttachInfoDetail(businessid);
            if (attachinfo != null) {
                if ("1".equals(signstatus)) {
                    attachinfo.set("signstatus", "1");
                    attachService.updateAttach(attachinfo, null);
                    log.info("=======结束调用getReuslt接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取签章信息结果成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getReuslt接口参数：params【" + params + "】=======");
            log.info("=======getReuslt异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取签章信息结果失败：" + e.getMessage(), "");
        }
    }

//    public static void main(String[] args) {
//        Object[] objects = getInputStream("https://jnseal.aiosign.com:8000/api/v1/file/download?fileId=0cfa689c1f99487c86a220a808e56f20", "d2d6e6c7-4050-4754-9e42-0a16081b19c2");
//        if (objects.length == 2) {
//            int count = (int) objects[1];
//            InputStream inputstream = (InputStream) objects[0];
//            byte[] byt = new byte[count];
//            System.out.println("=======byt.length=======" + byt.length);
//        }
//    }

    /**
     * 替换附件
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/updateAttach", method = RequestMethod.POST)
    public String updateAttach(@RequestBody String params) {
        try {
            log.info("=======开始调用updateAttach接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject jsonObject1 = jsonObject.getJSONObject("params");
            log.info("getAttach入参：" + jsonObject);
            // 8、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            String businessid = jsonObject1.getString("attachguid");
            String fileid = jsonObject1.getString("fileid");
            String token = jsonObject1.getString("token");
            FrameAttachInfo attachinfo = attachService.getAttachInfoDetail(businessid);
            Object[] objects = getInputStream(dzyz_url + "/v1/file/download?fileId=" + fileid + "&access_token=" + token, token);
            if (objects.length == 2) {
                InputStream inputstream = (InputStream) objects[0];
                if (attachinfo != null && inputstream != null) {
                    if ("1".equals(attachinfo.getStr("signstatus"))) {
                        int count = (int) objects[1];
                        log.info("=======count.length=======" + count);
                        attachinfo.setAttachLength(Long.valueOf((long) count));
                        attachService.updateAttach(attachinfo, inputstream);
                        log.info("=======结束调用updateAttach接口=======");
                        return JsonUtils.zwdtRestReturn("1", "签章成功！", dataJson.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "签章失败！", dataJson.toString());
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "签章失败！", "");
                }
            }else{
                return JsonUtils.zwdtRestReturn("0", "签章失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateAttach接口参数：params【" + params + "】=======");
            log.info("=======updateAttach异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "签章失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获得服务器端的数据,以InputStream形式返回
     *
     * @return
     */
    public static Object[] getInputStream(String URLPATH, String header) {
        Object[] result = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(URLPATH);
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                // 设置连接网络的超时时间
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setDoInput(true);
                // 表示设置本次http请求使用GET方式请求
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.addRequestProperty("Authentication", header);
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    inputStream = httpURLConnection.getInputStream();
                    result = new Object[2];
                    result[0] = inputStream;
                    result[1] = httpURLConnection.getContentLength();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
