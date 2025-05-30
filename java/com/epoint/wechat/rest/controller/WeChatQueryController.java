package com.epoint.wechat.rest.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.search.inteligentsearch.sdk.util.EpointDateUtil;
import com.epoint.wechat.rest.api.IWeChat;
import com.epoint.zwdt.util.TARequestUtil;

import net.sf.json.JSONArray;

@RestController
@RequestMapping("/wechatQuery")
public class WeChatQueryController
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

    @Autowired
    private IWeChat weChatService;

    /*********************************************职工缴费查询接口*******************************************************/

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

    @RequestMapping(value = "/getgsjfdetail", method = RequestMethod.POST)
    public String getgsjfdetail(@RequestBody String params) {
        try {
            // 职工工伤缴费的接口
            log.info("=======开始调用getgsjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsny = objinfo.getString("qsny");
            String zzny = objinfo.getString("zzny");

            if (StringUtil.isNotBlank(sfzhm) && (StringUtil.isNotBlank(qsny) && StringUtil.isNotBlank(zzny))) {

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
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======getgsjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getgsjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getgsjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getyljfdetail", method = RequestMethod.POST)
    public String getyljfdetail(@RequestBody String params) {
        try {
            // 职工养老缴费的接口
            log.info("=======开始调用getyljfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);

            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsny = objinfo.getString("qsny");
            String zzny = objinfo.getString("zzny");
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
                if (StringUtil.isNotBlank(result)) {
                    dataresult = xmlUtil.subString(result, "<ns:return>", "</ns:return>");
                    dataJson = xmlUtil.parsexml(dataresult);
                }
                return JsonUtils.zwdtRestReturn("1", "getyljfdetail接口调用成功", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getyljfdetail接口参数：params【" + params + "】=======");
            log.info("=======getyljfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getyljfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getsyjfdetail", method = RequestMethod.POST)
    public String getsyjfdetail(@RequestBody String params) {
        try {
            // 职工生育缴费的接口
            log.info("=======开始调用getsyjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);

            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsny = objinfo.getString("qsny");
            String zzny = objinfo.getString("zzny");

            if (StringUtil.isNotBlank(sfzhm) && StringUtil.isNotBlank(qsny) && StringUtil.isNotBlank(zzny)) {
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
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getsyjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getsyjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getsyjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getysjfdetail", method = RequestMethod.POST)
    public String getysjfdetail(@RequestBody String params) {
        try {
            // 职工失业缴费的接口
            log.info("=======开始调用getysjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);

            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsny = objinfo.getString("qsny");
            String zzny = objinfo.getString("zzny");

            if (StringUtil.isNotBlank(sfzhm) && StringUtil.isNotBlank(qsny) && StringUtil.isNotBlank(zzny)) {
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
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getysjfdetail接口参数：params【" + params + "】=======");
            log.info("=======getysjfdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getysjfdetail接口调用异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getlyjfdetail", method = RequestMethod.POST)
    public String getlyjfdetail(@RequestBody String params) {

        try {
            // 职工医疗缴费的接口
            log.info("=======开始调用getlyjfdetail接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);

            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsny = objinfo.getString("qsny");
            String zzny = objinfo.getString("zzny");

            if (StringUtil.isNotBlank(sfzhm) && StringUtil.isNotBlank(qsny) && StringUtil.isNotBlank(zzny)) {
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
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
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
            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            JSONObject jsonObject = new JSONObject();

            if (StringUtil.isNotBlank(sfzhm)) {

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
                return JsonUtils.zwdtRestReturn("1", "getCardye接口调用成功", jsonObject.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
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
    @RequestMapping(value = "/getSbinfoList", method = RequestMethod.POST)
    public String getSbinfoList(@RequestBody String params) {
        try {
            // 职工工伤缴费的接口
            log.info("=======开始调用getSbinfoList接口=======" + new Date());
            String dataJson = "";
            String dataresult = "";
            JSONObject json = JSON.parseObject(params);
            JSONObject objinfo = (JSONObject) json.get("params");
            String sfzhm = objinfo.getString("sfzhm");
            String qsrq = objinfo.getString("qsrq");
            String zzrq = objinfo.getString("zzrq");

            if (StringUtil.isNotBlank(sfzhm)) {

                String postxml = "";
                postxml = "<?xml version=\"1.0\" encoding=\"GBK\"?><p>";
                postxml += "<s sfzhm=\"" + sfzhm + "\"/>";
                postxml += "<s qsrq=\"" + qsrq + "\"/>";
                postxml += "<s zzrq=\"" + zzrq + "\"/></p>";

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
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======getSbinfoList接口参数：params【" + params + "】=======");
            log.info("=======getSbinfoList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getSbinfoList接口调用异常：" + e.getMessage(), "");
        }
    }

    /*********************************************微信驾驶证查询接口*******************************************************/

    @RequestMapping(value = "/getYzm", method = RequestMethod.POST)
    public String getYzm(@RequestBody String params) {
        String result = "";
        try {
            // 获取查询使用的验证码
            log.info("=======开始调用getYzm接口=======" + new Date());

            Map<String, String> signMap = new HashMap<String, String>();
            signMap.put("params", "{}");
            signMap.put("sessionGuid", "");
            //59.206.26.61:8080         10.200.40.95
            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForHuna/getYzm";

            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用getYzm接口=======" + new Date());
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======getYzm接口参数：params【" + params + "】=======");
            log.info("=======getYzm异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用getYzm接口=======" + new Date());
            return result;
        }

    }

    @RequestMapping(value = "/drivequery", method = RequestMethod.POST)
    public String drivequery(@RequestBody String params) {
        String result = "";
        try {
            // 驾驶证积分查询的接口
            log.info("=======开始调用drivequery接口=======" + new Date());

            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String jszh = obj.getString("jszh");
            String dabh = obj.getString("dabh");
            String yzm = obj.getString("yzm");

            String parameter = "{\"jszh\":\"" + jszh + "\",\"dabh\":\"" + dabh + "\",\"yzm\":\"" + yzm + "\"}";

            Map<String, String> signMap = new HashMap<String, String>();
            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");
            //59.206.26.61:8080         10.200.40.95
            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForHuna/getDriverLicense";

            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用drivequery接口=======" + new Date());
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======drivequery接口参数：params【" + params + "】=======");
            log.info("=======drivequery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用drivequery接口=======" + new Date());
            return result;
        }

    }

    @RequestMapping(value = "/illegalquery", method = RequestMethod.POST)
    public String illegalquery(@RequestBody String params) {

        String result = "";
        try {
            // 违章查询的接口
            log.info("=======开始调用illegalquery接口=======" + new Date());

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

            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForHuna/getVio";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用illegalquery接口=======" + new Date());
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======illegalquery接口参数：params【" + params + "】=======");
            log.info("=======illegalquery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用illegalquery接口=======" + new Date());
            return result;
        }
    }

    /*********************************************微信快递查询接口*******************************************************/
    @RequestMapping(value = "/expressquery", method = RequestMethod.POST)
    public String expressquery(@RequestBody String params) {

        String result = "";
        try {
            // 违章查询的接口
            log.info("=======开始调用expressquery接口=======" + new Date());

            /*     {
                "qurl": "http://117.73.252.120:8082/epoint-web-zwdt",
                "params": {
                    "postid": "SF1001891567704"
                },
                "token": "Epoint_WebSerivce_**##0601"
            }*/
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String postid = obj.getString("postid");

            Map<String, String> signMap = new HashMap<String, String>();

            String parameter = "{\"postid\":\"" + postid + "\"}";

            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");

            //String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForKuaidi100/searchExpressInfoTwo";
            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForKuaidi100/searchExpressInfoTwo";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);
            log.info("=======结束调用expressquery接口=======" + new Date());
            //result = result.replace("\\", "");
            JSONObject dataJson = new JSONObject();
            //JSONObject jsonObject1 =JSONObject.parseObject(result);
            dataJson.put("info", result);
            //String info = dataJson.toString()
            //return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toString());
            return result;
        }
        catch (Exception e) {
            // 异常时  调用失败
            log.info("=======expressquery接口参数：params【" + params + "】=======");
            log.info("=======expressquery异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用expressquery接口=======" + new Date());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！", "");
        }
    }

    /*********************************************微信获取部门电话及地址*******************************************************/
    /**
     * 微信获取部门电话及地址
     * @authory shibin
     * @version 2019年10月19日 下午1:36:49
     * @param params
     * @return
     */
    @RequestMapping(value = "/getOuTelAndAddress", method = RequestMethod.POST)
    public String getOuTelAndAddress(@RequestBody String params) {

        String areacode = "";
        String keyword = "";

        try {
            log.info("=======开始调用getOuTelAndAddress接口=======" + new Date());

            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            areacode = obj.getString("areacode");
            keyword = obj.getString("keyword");
            Record recordNew = null;
            Record recordTwo = null;
            Record recordOu = null;
            List<Record> json = null;
            List<FrameOu> twoOuList = null;
            List<Record> OuList = null;

            List<FrameOu> firstOuList = weChatService.findFirstOu(areacode, keyword);
            List<AuditOrgaWindow> windowList = new ArrayList<AuditOrgaWindow>();
            List<Record> result = new ArrayList<Record>();
            for (FrameOu frameOu : firstOuList) {
                json = new ArrayList<Record>();
                recordNew = new Record();
                recordNew.put("ouname", frameOu.getOuname());
                recordNew.put("ouguid", frameOu.getOuguid());
                twoOuList = weChatService.findOuByParentguid(frameOu.getOuguid());
                if (twoOuList.size() > 0 && twoOuList != null) {
                    for (FrameOu frameOu2 : twoOuList) {
                        OuList = new ArrayList<>();
                        windowList = weChatService.findWindowInfo(frameOu2.getOuguid());
                        for (AuditOrgaWindow winodw : windowList) {
                            recordOu = new Record();
                            recordOu.put("windowno", winodw.getWindowno());
                            recordOu.put("windowtel", winodw.getTel());
                            recordOu.put("remark", winodw.getNote());
                            OuList.add(recordOu);
                        }
                        recordTwo = new Record();
                        recordTwo.put("ouname", frameOu2.getOuname());
                        recordTwo.put("ouguid", frameOu2.getOuguid());
                        recordTwo.put("tel", frameOu2.getTel());
                        recordTwo.put("address", frameOu2.getAddress());
                        recordTwo.put("description", OuList);
                        json.add(recordTwo);
                    }
                    recordNew.put("child", json);
                }
                else {

                    // 获取一级部门窗口信息
                    OuList = new ArrayList<>();
                    windowList = weChatService.findWindowInfo(frameOu.getOuguid());

                    for (AuditOrgaWindow winodw : windowList) {
                        recordOu = new Record();
                        recordOu.put("windowno", winodw.getWindowno());
                        recordOu.put("windowtel", winodw.getTel());
                        recordOu.put("remark", winodw.getNote());
                        OuList.add(recordOu);
                    }
                    recordNew.put("tel", frameOu.getTel());
                    recordNew.put("address", frameOu.getAddress());
                    recordNew.put("PARENT_ID", frameOu.getOuguid());
                    recordNew.put("description", OuList);
                }
                result.add(recordNew);
            }

            JSONObject dataJson = new JSONObject();

            dataJson.put("ouList", result);
            dataJson.put("total", firstOuList.size());
            log.info("=======结束调用getOuTelAndAddress接口=======" + new Date());
            return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toString());

        }
        catch (

        Exception e) {
            log.info("=======getOuTelAndAddress异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }

    /***************************************************公积金查询接口*********************************************************/
    /**
     * 个人基本信息接口
     * @authory shibin
     * @version 2019年10月19日 上午10:25:36
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPrivateBaseInfo", method = RequestMethod.POST)
    public String getPrivateBaseInfo(@RequestBody String params) {

        String sfzhm = "";
        try {
            // 个人基本信息
            log.info("=======开始调用getPrivateBaseInfo接口=======" + new Date());
            JSONObject json = JSON.parseObject(params);
            JSONObject objinfo = (JSONObject) json.get("params");
            sfzhm = objinfo.getString("sfzh");

            JSONObject obj = new JSONObject();
            JSONObject obj1 = new JSONObject();
            obj1.put("sfzh", sfzhm);
            obj.put("APIKEY", "201901301535255977d9c0-c83");
            obj.put("ParamJson", obj1);
            String httpArg = obj.toJSONString();

            String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/15273ac5-abee-4575-a7bf-49ed53b6ddfa";
            String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");

            List<String> list = new ArrayList<>();
            if (StringUtil.isNotBlank(result)) {
                result = result.replace("<root>", "");
                result = result.replace("</root>", "");
                JSONObject jsonstring = JSON.parseObject(result);
                JSONArray jsonArr = JSONArray.fromObject(jsonstring.getString("data"));

                for (int i = 0; i < jsonArr.size(); i++) {
                    list.add(jsonArr.getJSONObject(i).toString());
                }
            }

            // 2.公司信息接口
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            obj3.put("sfzh", sfzhm);
            obj2.put("APIKEY", "201902151642347c663314-ae3");
            obj2.put("ParamJson", obj3);
            String httpArgTwo = obj2.toJSONString();

            //String httpUrlTwo = "http://172.20.224.2:9999/API/proxy/handlebykey/15273ac5-abee-4575-a7bf-49ed53b6ddfa";
            String resultTwo = TARequestUtil.sendPost(httpUrl, httpArgTwo, "", "");

            List<String> listTwo = new ArrayList<>();
            if (StringUtil.isNotBlank(resultTwo)) {
                resultTwo = resultTwo.replace("<root>", "");
                resultTwo = resultTwo.replace("</root>", "");
                JSONObject jsonstring = JSON.parseObject(resultTwo);
                JSONArray jsonArr = JSONArray.fromObject(jsonstring.getString("data"));

                for (int i = 0; i < jsonArr.size(); i++) {
                    listTwo.add(jsonArr.getJSONObject(i).toString());
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("privateinfo", list);
            jsonObject.put("companyinfo", listTwo);

            return JsonUtils.zwdtRestReturn("1", "getPrivateBaseInfo接口调用成功", jsonObject.toString());
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======getPrivateBaseInfo接口参数：params【" + params + "】=======");
            log.info("=======getPrivateBaseInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getPrivateBaseInfo接口调用异常：" + e.getMessage(), "");
        }
    }

    /**
     * 公积金详情接口
     * @authory shibin
     * @version 2019年10月19日 上午10:27:30
     * @param params
     * @return
     */
    @RequestMapping(value = "/getAccumulationFundDetail", method = RequestMethod.POST)
    public String getAccumulationFundDetail(@RequestBody String params) {
        String sfzh = "";
        String ksrq = "";
        String jsrq = "";
        try {
            log.info("=======开始调用getAccumulationFundDetail接口=======" + new Date());
            JSONObject json = JSON.parseObject(params);
            JSONObject objinfo = (JSONObject) json.get("params");
            sfzh = objinfo.getString("sfzh");
            ksrq = objinfo.getString("ksrq");
            jsrq = objinfo.getString("jsrq");
            JSONObject obj = new JSONObject();
            JSONObject obj1 = new JSONObject();
            obj1.put("sfzh", sfzh);
            obj1.put("ksrq", ksrq);
            obj1.put("jsrq", jsrq);
            obj.put("APIKEY", "201903061050401766ef6c-ab0");
            obj.put("ParamJson", obj1);
            String httpArg = obj.toJSONString();

            String httpUrl = "http://172.20.224.2:9999/API/proxy/handlebykey/1766ef6c-ab06-4916-9341-d97f5cec6a73";
            String result = TARequestUtil.sendPost(httpUrl, httpArg, "", "");

            String ret = "";
            List<JSONObject> list = new ArrayList<>();
            if (StringUtil.isNotBlank(result)) {
                result = result.replace("<root>", "");
                result = result.replace("</root>", "");
                if (StringUtil.isNotBlank(result)) {
                    if (!"null".equals(result)) {
                        JSONObject jsonreturn = JSON.parseObject(result);
                        ret = jsonreturn.getString("ret");
                        if ("1".equals(ret)) {
                            return JsonUtils.zwdtRestReturn("0", "未查到您的个人账户缴存明细", "");
                        }
                        else {
                            com.alibaba.fastjson.JSONArray dataArray = jsonreturn.getJSONArray("data");
                            for (int i = 0; i < dataArray.size(); i++) {
                                list.add(dataArray.getJSONObject(i));
                            }
                        }
                    }
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info", list);

            return JsonUtils.zwdtRestReturn("1", "getAccumulationFundDetail接口调用成功", jsonObject.toString());
        }
        catch (Exception e) {
            // 异常时，保存信息
            log.info("=======getAccumulationFundDetail接口参数：params【" + params + "】=======");
            log.info("=======getAccumulationFundDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getAccumulationFundDetail接口调用异常：" + e.getMessage(), "");
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
            log.info("=======开始调用queryBusstops接口=======" + new Date());

            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String address = obj.getString("address");

            Map<String, String> signMap = new HashMap<String, String>();

            String parameter = "{\"cityNo\":\"325\",\"address\":\"" + address + "\"}";

            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");

            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForBaiduAPI/getLocalBusStationInfo";
            result = TARequestUtil.httpPostWithForm(httpUrl, signMap);

            JSONObject json1 = JSON.parseObject(result);
            JSONObject json2 = (JSONObject) json1.get("result");
            com.alibaba.fastjson.JSONArray json3 = (com.alibaba.fastjson.JSONArray) json2.get("content");

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

            log.info("=======结束调用queryBusstops接口=======" + new Date());
            JSONObject dataJson = new JSONObject();
            dataJson.put("info", list);
            return result;
            //return JsonUtils.zwdtRestReturn("1", "queryBusRoutes接口调用成功", dataJson.toString());
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
            log.info("=======开始调用queryBusRoutes接口=======" + new Date());
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

            String parameter = "{\"cityNo\":\"325\",\"statName\":\"" + from_statName + "\",\"statUid\":\""
                    + from_statUid + "\",\"startX\":\"" + from_startX + "\",\"startY\":\"" + from_startY
                    + "\",\"endName\":\"" + to_statName + "\",\"endUid\":\"" + to_statUid + "\",\"endX\":\"" + to_startX
                    + "\",\"endY\":\"" + to_startY + "\"}";

            signMap.put("params", parameter);
            signMap.put("sessionGuid", "");

            String httpUrl = "http://59.206.26.61:8080/IDRIProjectForPublicService/rest/interfaceForBaiduAPI/getTwoStationLines";
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
