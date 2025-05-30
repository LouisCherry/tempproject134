package com.epoint.sgthgz;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sgthgzrest")
public class SgthgzController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static String sgturl = "http://222.173.147.74:808/baoshen/api.aspx";

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    /**
     * 项目基本信息的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getitembasicinfo", method = RequestMethod.POST)
    public String getItemBasicInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getitembasicinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目代码
                String itemcode = obj.getString("itemcode");
                // 1.2、获取项目名称
                String itemname = obj.getString("itemname");
                //必须传一个入参
                if (StringUtil.isBlank(itemcode) && StringUtil.isBlank(itemname)) {
                    return JsonUtils.zwdtRestReturn("0", "至少传入一个参数！", "");
                } else {
                    if (StringUtil.isNotBlank(itemcode) && StringUtil.isBlank(itemname)) {
                        AuditRsItemBaseinfo itemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(itemcode).getResult();
                        if (itemBaseinfo != null) {
                            JSONObject json = new JSONObject();
                            json.put("itemname", itemBaseinfo.getItemname());
                            json.put("itemcode", itemBaseinfo.getItemcode());
                            json.put("itemguid", itemBaseinfo.getRowguid());
                            dataJson.put("iteminfo", json);
                            log.info("=======结束调用getitembasicinfo接口=======");
                            return JsonUtils.zwdtRestReturn("1", "获取成功！", dataJson.toJSONString());
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "查询不到该项目！", "");
                        }
                    } else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.like("itemname", itemname);
                        List<AuditRsItemBaseinfo> list = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (list != null && !list.isEmpty()) {
                            List<JSONObject> itemlist = new ArrayList<>();
                            for (AuditRsItemBaseinfo itemBaseinfo : list) {
                                JSONObject json = new JSONObject();
                                json.put("itemname", itemBaseinfo.getItemname());
                                json.put("itemcode", itemBaseinfo.getItemcode());
                                json.put("itemguid", itemBaseinfo.getRowguid());
                                itemlist.add(json);
                            }
                            dataJson.put("itemlist", itemlist);
                            log.info("=======结束调用getitembasicinfo接口=======");
                            return JsonUtils.zwdtRestReturn("1", "获取成功！", dataJson.toJSONString());
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "查询不到该项目！", "");
                        }
                    }
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getitembasicinfo接口参数：params【" + params + "】=======");
            log.info("=======getitembasicinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取附件的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getfileinfo", method = RequestMethod.POST)
    public String getFileinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getfileinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            //获取项目标识
            String itemguid = obj.getString("itemguid");
            //先调用获取token接口
            JSONObject tokenjson = new JSONObject();
            tokenjson.put("routeid", "fa4acf1ff4b64f5d8cdb46e9a391f2bb");
            tokenjson.put("license", "da6e26998b764942993bc005d3956089");
            String result = doPostJson(sgturl, tokenjson.toJSONString(), "d8aeee0ba41b4d7ba16c91b6f17ad592");
            JSONObject resultjson = JSONObject.parseObject(result);
            String token = resultjson.getString("token");
            //调用合格证附件接口
            JSONObject sgtjson = new JSONObject();
            sgtjson.put("token", token);
            sgtjson.put("itemguid", itemguid);
            sgtjson.put("license", "da6e26998b764942993bc005d3956089");
            String newresult = doPostJson(sgturl, sgtjson.toJSONString(), "fa4acf1ff4b64f5d8cdb46e9a391f2bb");
            return newresult;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskBasicInfo接口参数：params【" + params + "】=======");
            log.info("=======getTaskBasicInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
        }
    }

    public static String doPostJson(String url, String json, String route) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            //请求头参数，不同的route调用不同的功能
            httpPost.setHeader("route", route);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }

}
