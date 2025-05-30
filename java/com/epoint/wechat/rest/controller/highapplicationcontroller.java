package com.epoint.wechat.rest.controller;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.search.inteligentsearch.sdk.util.EpointDateUtil;

import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/highapplication")
public class highapplicationcontroller
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

    EpointDateUtil epointDateUtil = new EpointDateUtil();

    /*********************************************微信缴费查询接口*******************************************************/

    @RequestMapping(value = "/getjftotal", method = RequestMethod.POST)
    public String getjftotal(@RequestBody String params) {
        try {
            // 职工养老缴费的接口
            log.info("=======开始调用getjftotal接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);

            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            //            String qsny = "198601";
            String qsny = "198601";
            String date = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_NOSPLIT_FORMAT).substring(0,
                    6);
            String zzny = date;
            if (StringUtil.isNotBlank(sfzhm) && StringUtil.isNotBlank(qsny) && StringUtil.isNotBlank(zzny)) {
                String postxml = "";
                postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                postxml += "<s qsny=\"" + qsny + "\"/>";
                postxml += "<s zzny=\"" + zzny + "\"/>";
                postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.01\"/><s rsxtid=\"3759\"/></p>";

                JSONObject obj = new JSONObject();
                JSONObject obj2 = new JSONObject();
                obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                obj2.put("serviceName", "DwWebService_HSUAged");
                obj2.put("operationName", "doInvoke");
                obj2.put("xmlPara", postxml);
                obj.put("apikey", "201811081424138dfe9593-b5b");
                obj.put("Paramjson", obj2);
                String httpArg = obj.toJSONString();

                String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/7c3da4c5-4017-4ab9-91a3-50dc8aa84873";
                String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                JSONObject infoJson = new JSONObject();
                JSONObject info = new JSONObject();
                if (StringUtil.isNotBlank(result)) {
                    dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                    dataJson = parsexml(dataresult);
                    String totalyear = "";
                    String totalyear1 = "0";
                    String totalyear2 = "0";
                    String beginyear = "";
                    String endyear = "";
                    //累计缴费0年2月,共中断0个月,最早缴费年月201905,最晚缴费年月201906
                    Pattern pt = Pattern.compile("累计缴费(\\w+)年");
                    Matcher mt = pt.matcher(dataJson);
                    while (mt.find()) {
                        totalyear1 = mt.group(1);
                    }
                    Pattern pt2 = Pattern.compile("年(\\w+)月,共中断");
                    Matcher mt2 = pt2.matcher(dataJson);
                    while (mt2.find()) {
                        totalyear2 = mt2.group(1);
                    }
                    totalyear = totalyear1 + "年" + totalyear2 + "月";
                    Pattern py = Pattern.compile("最早缴费年月(\\w+),最晚缴费年月");
                    Matcher my = py.matcher(dataJson);
                    while (my.find()) {
                        beginyear = my.group(1);
                    }
                    Pattern pe = Pattern.compile("最晚缴费年月(\\w+)");
                    Matcher me = pe.matcher(dataJson);
                    while (me.find()) {
                        endyear = me.group(1);
                    }
                    infoJson.put("totalyear", totalyear);
                    infoJson.put("beginyear", beginyear);
                    infoJson.put("endyear", endyear);

                }
                info.put("info", infoJson);
                return JsonUtils.zwdtRestReturn("1", "getjftotal接口调用成功", info.toJSONString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getjftotal接口参数：params【" + params + "】=======");
            log.info("=======getjftotal异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getjftotal接口调用异常：" + e.getMessage(), "");
        }
    }

    // Doma4j解析xml字符串
    @SuppressWarnings("unchecked")
    public static String parsexml(String xmlString) {
        String jfxx = "";
        try {
            Document document = DocumentHelper.parseText(xmlString);
            List<Attribute> sAttrList = null;
            Element root = document.getRootElement();
            List<Element> sElementList = root.elements("s");
            if (sElementList != null && sElementList.size() > 0) {
                for (Element sElement : sElementList) {
                    sAttrList = sElement.attributes();
                    for (Attribute attribute : sAttrList) {
                        if ("jfxx".equals(attribute.getName())) {
                            jfxx = attribute.getValue();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jfxx;
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getgsjfdetail", method = RequestMethod.POST)
    public String getgsjfdetail(@RequestBody String params) {
        try {
            // 职工工伤缴费的接口
            log.info("=======开始调用getgsjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");
                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "01";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "06";
                    }
                    else {
                        qsny = qsny.replace("-", "");
                        zzny = zzny.replace("-", "");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsny=\"" + qsny + "\"/>";
                    postxml += "<s zzny=\"" + zzny + "\"/>";
                    postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.02\"/><s rsxtid=\"3759\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "DwWebService_HSUAged");
                    obj2.put("operationName", "doInvoke");
                    obj2.put("xmlPara", postxml);
                    obj.put("apikey", "2018112702022692a24384-ade");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/2547297e-399e-4f25-9340-8e0124d13652";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    return JsonUtils.zwdtRestReturn("1", "getgsjfdetail接口调用成功", dataJson);

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }
        }
        catch (

        Exception e) {
            // 异常时，保存信息
            log.info("=======getgsjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getgsjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getgsjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getuser", method = RequestMethod.POST)
    public String getUser(@RequestBody String params) {
        try {
            // 用户信息
            log.info("=======开始调用getyljfdetail接口=======" + new Date());
            JSONObject resultObject = new JSONObject();
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                resultObject.put("name", auditOnlineRegister.getUsername());
                resultObject.put("idcard", auditOnlineRegister.getIdnumber());
                return JsonUtils.zwdtRestReturn("1", "getUser接口调用成功", resultObject.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }

        }
        catch (Exception e) {
            log.info("=======getUser接口参数：params【" + params + "】=======");
            log.info("=======getUser异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getUser接口调用异常：" + e.getMessage(), "");
        }
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getyljfdetail", method = RequestMethod.POST)
    public String getyljfdetail(@RequestBody String params) {
        try {
            // 职工养老缴费的接口
            log.info("=======开始调用getyljfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");
                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "01";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "06";
                    }
                    else {
                        qsny = qsny.replace("-", "");
                        zzny = zzny.replace("-", "");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsny=\"" + qsny + "\"/>";
                    postxml += "<s zzny=\"" + zzny + "\"/>";
                    postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.01\"/><s rsxtid=\"3759\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "DwWebService_HSUAged");
                    obj2.put("operationName", "doInvoke");
                    obj2.put("xmlPara", postxml);
                    obj.put("apikey", "201811081424138dfe9593-b5b");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/7c3da4c5-4017-4ab9-91a3-50dc8aa84873";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    /* resultObject.put("dataJson", dataJson);
                    resultObject.put("name", auditOnlineRegister.getUsername());
                    resultObject.put("idcard", auditOnlineRegister.getIdnumber());*/
                    return JsonUtils.zwdtRestReturn("1", "getyljfdetail接口调用成功", dataJson);

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }

        }
        catch (Exception e) {
            log.info("=======getyljfdetail接口参数：params【" + params + "】=======");
            log.info("=======getyljfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getyljfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getsyjfdetail", method = RequestMethod.POST)
    public String getsyjfdetail(@RequestBody String params) {
        try {
            // 职工生育缴费的接口
            log.info("=======开始调用getsyjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");
                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "01";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "06";
                    }
                    else {
                        qsny = qsny.replace("-", "");
                        zzny = zzny.replace("-", "");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsny=\"" + qsny + "\"/>";
                    postxml += "<s zzny=\"" + zzny + "\"/>";
                    postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.03\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "DwWebService_HSU");
                    obj2.put("operationName", "doInvoke");
                    obj2.put("xmlPara", postxml);
                    obj.put("apikey", "20181127020157cffc3870-112");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/cffc3870-112d-4dcd-85de-6024f842fbb0";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    return JsonUtils.zwdtRestReturn("1", "getsyjfdetail接口调用成功", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }

        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getsyjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getsyjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getsyjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getysjfdetail", method = RequestMethod.POST)
    public String getysjfdetail(@RequestBody String params) {
        try {
            // 职工失业缴费的接口
            log.info("=======开始调用getysjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = (auditOnlineRegister.getIdnumber());
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");
                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "01";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "06";
                    }
                    else {
                        qsny = qsny.replace("-", "");
                        zzny = zzny.replace("-", "");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsny=\"" + qsny + "\"/>";
                    postxml += "<s zzny=\"" + zzny + "\"/>";
                    postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.04\"/><s rsxtid=\"3759\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "DwWebService_HSUAged");
                    obj2.put("operationName", "doInvoke");
                    obj2.put("xmlPara", postxml);
                    obj.put("apikey", "2018112702025437cba7d0-795");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/ec1c9258-1765-4dc1-b5b2-f4041830a096";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    return JsonUtils.zwdtRestReturn("1", "getysjfdetail接口调用成功", dataJson);

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getysjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getysjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getysjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getlyjfdetail", method = RequestMethod.POST)
    public String getlyjfdetail(@RequestBody String params) {

        try {
            // 职工医疗缴费的接口
            log.info("=======开始调用getlyjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");

                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "01";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "06";
                    }
                    else {
                        qsny = qsny.replace("-", "");
                        zzny = zzny.replace("-", "");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsny=\"" + qsny + "\"/>";
                    postxml += "<s zzny=\"" + zzny + "\"/>";
                    postxml += "<s nypdbz=\"1\"/><s sqm=\"DZB-20180815\"/><s dsfjgbh=\"DZB001\"/><s serviceMethod=\"F3705.02.05\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "DwWebService_HSU");
                    obj2.put("operationName", "doInvoke");
                    obj2.put("xmlPara", postxml);
                    obj.put("apikey", "2018112702033257b3876a-0bb");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/f5b5b33d-3b36-4a7a-8801-0ad9317d1975";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    return JsonUtils.zwdtRestReturn("1", "getlyjfdetail接口调用成功", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }
        }
        catch (

        Exception e) {
            // 异常时  调用失败
            log.info("=======getlyjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getlyjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getlyjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    /**
     * 社保卡余额查询
     * @authory shibin
     * @version 2019年10月20日 下午3:54:16
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCardye", method = RequestMethod.POST)
    public String getCardye(@RequestBody String params) {
        try {
            // 职工工伤缴费的接口
            log.info("=======开始调用getCardye接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            // 获取用户基本信息 个性化
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    JSONObject jsonObject = new JSONObject();

                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "ScmsCardService");
                    obj2.put("operationName", "gCardYeBySfzhm");
                    obj2.put("xmlPara", postxml);

                    obj.put("APIKEY", "20190805160422f4d71890-fd7");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/f4d71890-fd7a-4c14-9107-cd3c7cef1be7";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.subString(dataresult, "<p>", "</p>");

                        String[] arr = dataJson.split("\"");
                        dataJson = arr[1];
                    }
                    jsonObject.put("ye", dataJson);
                    jsonObject.put("name", auditOnlineRegister.getUsername());
                    jsonObject.put("idcard", auditOnlineRegister.getIdnumber());
                    return JsonUtils.zwdtRestReturn("1", "getCardye接口调用成功", jsonObject.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======getCardye接口参数：params【" + params + "】=======");
            log.info("=======getCardye异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getCardye接口调用异常：" + e.getMessage(), "");
        }
    }

    /**
     * 社保卡消费查询
     * @authory shibin
     * @version 2019年10月20日 下午4:36:51
     * @param params
     * @return
     */
    @SuppressWarnings("static-access")
    @RequestMapping(value = "/getSbinfoList", method = RequestMethod.POST)
    public String getSbinfoList(@RequestBody String params) {
        try {
            // 职工工伤缴费的接口
            log.info("=======开始调用getSbinfoList接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            String auditOnlineRegisterStr = json.getString("auditonlineregister");
            AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                    AuditOnlineRegister.class);
            if (auditOnlineRegister != null) {
                if (StringUtil.isNotBlank(auditOnlineRegister.getIdnumber())) {
                    JSONObject objinfo = (JSONObject) json.get("params");
                    String sfzhm = auditOnlineRegister.getIdnumber();
                    String qsny = objinfo.getString("sdate");
                    String zzny = objinfo.getString("edate");
                    if (StringUtil.isBlank(qsny) && StringUtil.isBlank(zzny)) {
                        qsny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "0101";
                        zzny = epointDateUtil.convertDate2String(new Date(), "yyyy") + "0601";
                    }
                    else {
                        qsny = qsny.replace("-", "").concat("01");
                        zzny = zzny.replace("-", "").concat("01");
                    }
                    String postxml = "";
                    postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                    postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                    postxml += "<s qsrq=\"" + qsny + "\"/>";
                    postxml += "<s zzrq=\"" + zzny + "\"/></p>";

                    JSONObject obj = new JSONObject();
                    JSONObject obj2 = new JSONObject();
                    obj2.put("licenseKey", "476d3f53-aaa5-43f5-abd6-9036bf659e5e");
                    obj2.put("serviceName", "SiService");
                    obj2.put("operationName", "getCardTransStd");
                    obj2.put("xmlPara", postxml);

                    obj.put("APIKEY", "201906291759343c981afa-e27");
                    obj.put("Paramjson", obj2);
                    String httpArg = obj.toJSONString();

                    String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/3c981afa-e271-4d65-95d4-b596c2381cdb";
                    String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");
                    if (StringUtil.isNotBlank(result)) {
                        dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                        dataJson = xmlUtil.parsexml(dataresult);
                    }
                    return JsonUtils.zwdtRestReturn("1", "getSbinfoList接口调用成功", dataJson);

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请实名认证！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "请登录！", "");
            }
        }
        catch (

        Exception e) {
            // 异常时，保存信息
            log.info("=======getSbinfoList接口参数：params【" + params + "】=======");
            log.info("=======getSbinfoList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getSbinfoList接口调用异常：" + e.getMessage(), "");
        }
    }

}
