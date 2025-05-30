package com.epoint.epointsform;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * [事项打印电子表单后台]
 *
 * @author wsq
 * @version [版本号, 2020年3月27日]
 */
@RestController
@RequestMapping("/eformprint")
public class EformPrintController {

    private Logger log = Logger.getLogger(EformPrintController.class);

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    
    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IConfigService configServicce;

    /**
     * [打印电子表单]
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/printEform", method = RequestMethod.POST)
    public String printEform(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        JsonUtils.checkUserAuth(jsonObject.getString("token"));
        JSONObject obj = (JSONObject) jsonObject.get("params");
        String 	taskguid = obj.getString("taskguid");
    	String projectguid = obj.getString("projectguid");
    	String epointsformurl = configServicce.getFrameConfigValue("epointsformurlwt");
    	JSONObject datajson = new JSONObject();
        String attachGuid = "";
        String docname = "";
        
        
        AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
        
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskguid, true).getResult();

        if (auditTaskExtension == null || task == null) {
        	return JsonUtils.zwdtRestReturn("0", "事项未找到！", "");
        }
        
        if (StringUtil.isNotBlank(auditTaskExtension.getStr("formid"))) {
            docname = task.getTaskname() + ".doc";
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "请先配置电子表单", "");
        }

        // 获取表单配置信息
        JSONObject formData = getEformInfo(auditTaskExtension.getStr("formid"));
        String wordattachguid = formData.getString("attachGuid");
        if (StringUtil.isBlank(wordattachguid)) {
            return JsonUtils.zwdtRestReturn("0", "请先配置表单word打印模板", "");
        }

        String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
        String wordurl = epointsformurl + "rest/attachAction/getContent?isCommondto=true&attachGuid=" + wordattachguid;
        InputStream docinputstream = getWordInputStream(wordurl);
        if (docinputstream == null) {
            return JsonUtils.zwdtRestReturn("0", "请先配置表单word模板", "");
        }
        try {
            new License().setLicense(licenseName);
            Document doc = new Document(docinputstream);
            // 通用替换域
            Map<String, Object> map = setEformToCommon(auditTaskExtension.getStr("formid"), projectguid, doc, formData);
            if (StringUtil.isNotBlank(map.get("msg"))) {
                return JsonUtils.zwdtRestReturn("0", "信息异常", map.get("msg").toString());
            }
            // 将替换后的word上传到附件库中
            String newattachguid = UUID.randomUUID().toString();
            String newcliengguid = UUID.randomUUID().toString();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.save(outputStream, SaveFormat.DOC);// 保存成word
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = inputStream.available();
            FrameAttachInfo frameAttachInfo = AttachUtil.saveFileInputStream(newattachguid, newcliengguid, docname,
                    ".doc", "电子表单", size, inputStream, UUID.randomUUID().toString(), "电子表单");
            attachGuid = frameAttachInfo.getAttachGuid();
            datajson.put("attachGuid", attachGuid);
            return JsonUtils.zwdtRestReturn("1", "打印成功", datajson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "发成异常！", datajson.toString());
        }
    }

    public String getInstanceInfo(String formid, String rowGuid) {
    	String epointsformurl = configServicce.getFrameConfigValue("epointsformurlwt");
        Map<String, String> map = new HashMap<>();
        String getPageDataurl = epointsformurl + "rest/sform/getPageData";
        Map<String, Object> param = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("formId", formid);
        object.put("rowGuid", rowGuid);
        object.put("tableName", "");
        param.put("params", object);
        String result = HttpUtil.doPost(getPageDataurl, param);

        return result;
    }

    public JSONObject getEformInfo(String formid) {
    	String epointsformurl = configServicce.getFrameConfigValue("epointsformurlwt");
        String getEpointSformInfourl = epointsformurl + "rest/sform/getEpointSformInfo";
        JSONObject formData = new JSONObject();
        Map<String, Object> param = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("formId", formid);
        object.put("versionGuid", "");
        param.put("params", object);
        String result = HttpUtil.doPost(getEpointSformInfourl, param);
        if (StringUtil.isNotBlank(result)) {
            JSONObject obj1 = JSONObject.parseObject(result);
            formData = obj1.getJSONObject("custom").getJSONObject("formData");
        }
        return formData;
    }

    /**
     * [打印电子表单一般值替换域方法] 规则：表单中文名和域名一样
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> setEformToCommon(String formid, String projectguid, Document doc, JSONObject formData) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 获取表单实例信息
            String dzbdInfo = getInstanceInfo(formid, projectguid);

            if (StringUtil.isBlank(dzbdInfo)) {
                map.put("msg", "调用电子表单接口失败！");
            }
            else {
                JSONObject obj1 = JSONObject.parseObject(dzbdInfo);
                String code = obj1.getJSONObject("status").getString("code");
                if ("1".equals(code)) {
                    // 获取表单有代码项字段
                    JSONArray jsonArray = formData.getJSONArray("structList");
                    Map<String, JSONArray> eformInfo = new HashMap<String, JSONArray>();
                    if (jsonArray != null && !jsonArray.isEmpty()) {
                        for (Object o : jsonArray) {
                            JSONObject obj2 = (JSONObject) o;
                            JSONArray codeItems = obj2.getJSONArray("codeItems");
                            if (codeItems != null && !codeItems.isEmpty()) {
                                eformInfo.put(obj2.getString("fieldname"), codeItems);
                            }
                        }
                    }

                    JSONObject recordData = obj1.getJSONObject("custom").getJSONObject("recordData");
                    if (recordData != null && !recordData.isEmpty()) {
                        // 获取主表数据
                        JSONArray mainList = recordData.getJSONArray("mainList");
                        if (mainList != null && !mainList.isEmpty()) {
                            JSONArray rowList = mainList.getJSONObject(0).getJSONArray("rowList");
                            // word域名数组
                            List<String> fieldNames = new ArrayList<>();
                            // word域值数组
                            List<String> values = new ArrayList<>();
                            for (int i = 0; i < rowList.size(); i++) {
                                JSONObject obj2 = (JSONObject) rowList.get(i);
                                String fieldname = obj2.getString("FieldName");
                                String fieldchinesename = obj2.getString("FieldChineseName");
                                String text = obj2.getString("text");
                                String value = obj2.getString("value");

                                String[] fieldvalues = null;
                                if (StringUtil.isNotBlank(value) && value.contains(",")) {
                                    fieldvalues = value.split(",");
                                }

                                if (StringUtil.isNotBlank(text)) {
                                    fieldNames.add(fieldchinesename);
                                    values.add(text);
                                }

                                // 判断是否为代码项
                                JSONArray items = eformInfo.get(fieldname);
                                if (items != null && !items.isEmpty()) {
                                    for (Object o : items) {
                                        JSONObject object = (JSONObject) o;
                                        String itemvalue = object.getString("itemvalue");
                                        String itemtext = object.getString("itemtext");

                                        fieldNames.add(fieldchinesename + "_" + itemtext);
                                        if (fieldvalues == null) {
                                            if (itemvalue.equals(value)) {
                                                values.add("☑");
                                            }
                                            else {
                                                values.add("□");
                                            }
                                        }
                                        else {
                                            if (Arrays.asList(fieldvalues).contains(itemvalue)) {
                                                values.add("☑");
                                            }
                                            else {
                                                values.add("□");
                                            }
                                        }
                                    }
                                }
                                else {
                                    fieldNames.add(fieldchinesename);
                                    if (StringUtil.isNotBlank(value)) {
                                        values.add(value);
                                    }
                                    else {
                                        values.add("");
                                    }
                                }
                            }

                            // 渲染域
                            doc.getMailMerge().execute(fieldNames.toArray(new String[fieldNames.size()]),
                                    values.toArray(new String[values.size()]));
                        }
                        else {
                            map.put("msg", "获取电子表单数据失败！请先保存表单！");
                        }

                        // 渲染子表数据
                        JSONArray subList = recordData.getJSONArray("subList");
                        if (subList != null && !subList.isEmpty()) {
                            // 获取子表数据信息
                            String controlsdata = formData.getJSONObject("formVersioninfo").getString("controlsdata");
                            Map<String, String> subtableinfo = new HashMap<String, String>();
                            Map<String, List<String>> subcolumnmap = new HashMap<String, List<String>>();
                            if (StringUtil.isNotBlank(controlsdata)) {
                                JSONArray array = JSONArray.parseArray(controlsdata);
                                for (Object obj : array) {
                                    JSONObject controldata = (JSONObject) obj;
                                    String type = controldata.getString("type");
                                    if ("subtable".equals(type)) {
                                        List<String> columnlist = new ArrayList<>();
                                        subtableinfo.put(controldata.getString("sourceTable"),
                                                controldata.getString("defaultline"));
                                        JSONArray columns = controldata.getJSONArray("columns");
                                        if (columns != null && !columns.isEmpty()) {
                                            for (Object column : columns) {
                                                JSONObject object = (JSONObject) column;
                                                columnlist.add(object.getString("fieldName"));
                                            }
                                        }
                                        subcolumnmap.put(controldata.getString("sourceTable"), columnlist);
                                    }
                                }
                            }
                            mergertable(doc, subList, subtableinfo, subcolumnmap);
                        }
                    }
                    else {
                        map.put("msg", "获取电子表单数据失败！请检查接口入参是否正常！");
                    }
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

        return map;
    }

    public void mergertable(Document doc, JSONArray subList, Map<String, String> subtableinfo,
            Map<String, List<String>> subcolumnmap) throws Exception {
        if (subList != null && !subList.isEmpty()) {
            for (Object subtable : subList) {
                JSONObject subobj = (JSONObject) subtable;
                JSONObject mainRecord = subobj.getJSONArray("mainRecordList").getJSONObject(0);
                String subTableName = mainRecord.getString("subTableName");
                String subTableid = mainRecord.getString("subTableId");
                List<String> columnlist = subcolumnmap.get(subTableid);
                String defaultline = subtableinfo.get(subTableid);
                JSONArray subRecordList = mainRecord.getJSONArray("subRecordList");
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

                int line = 0;

                if (StringUtil.isNotBlank(defaultline)) {
                    line = Integer.parseInt(defaultline);
                }

                if (subRecordList != null && line < subRecordList.size()) {
                    line = subRecordList.size();
                }

                if (line > 0) {
                    for (int i = 0; i < line; i++) {
                        Map<String, Object> data1 = new HashMap<String, Object>(16);

                        if (i < subRecordList.size()) {
                            data1.put("编号", i + 1 + "");
                            JSONObject subRecord = subRecordList.getJSONObject(i);
                            JSONArray rowList = subRecord.getJSONArray("rowList");
                            if (rowList != null && !rowList.isEmpty()) {
                                for (int j = 0; j < rowList.size(); j++) {
                                    // 子表电子表单列名
                                    String fieldName = rowList.getJSONObject(j).getString("FieldName");
                                    if (columnlist.contains(fieldName)) {
                                        String fieldchinesename = rowList.getJSONObject(j)
                                                .getString("FieldChineseName");
                                        String value = rowList.getJSONObject(j).getString("value");

                                        if (StringUtil.isNotBlank(value)) {
                                            data1.put(fieldchinesename, value);
                                        }
                                        else {
                                            data1.put(fieldchinesename, "");
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            data1.put("编号", "");
                            for (String fieldname : columnlist) {
                                data1.put(fieldname, "");
                            }
                        }
                        dataList.add(data1);
                    }
                }

                doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, subTableName));
            }
        }
    }

    public InputStream getWordInputStream(String wordurl) {
        try {
            URL url = new URL(wordurl);
            if (wordurl.startsWith("https")) {
                disableSslVerification();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream in = conn.getInputStream();
                return in;
            }
            else {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream in = conn.getInputStream();
                return in;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
