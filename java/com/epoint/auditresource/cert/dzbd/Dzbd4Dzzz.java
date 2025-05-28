package com.epoint.auditresource.cert.dzbd;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jn.util.JnBeanUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public abstract class Dzbd4Dzzz {

    private static  String E_FORM_URL = "";

    public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public CertInfoExtension dataBean;

    protected AuditTask audittask;

    protected AuditProject auditproject;

    protected String tycertcatcode;

    private IConfigService configServicce = ContainerFactory.getContainInfo().getComponent(IConfigService.class);

    public Dzbd4Dzzz(CertInfoExtension dataBean, AuditTask audittask, AuditProject auditproject, String tycertcatcode) {
        this.dataBean = dataBean;
        this.audittask = audittask;
        this.auditproject = auditproject;
        this.tycertcatcode = tycertcatcode;
        E_FORM_URL = configServicce.getFrameConfigValue("epointsformurl_innernet_ip") + "rest/";
    }

    public abstract void setBasicDataBean();

    public String getFlow(){
        return null;
    };


    public void beforPutData(Record dzbdRecord){
        // 子类个性化处理表单数据使用
    };


    public void setFormData() throws IOException {
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

        //获取配置文件
        JnBeanUtil jnbeanutil = new JnBeanUtil("fz.properties");
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(this.audittask.getRowguid(), false).getResult();
        if(StringUtil.isNotBlank(auditTaskExtension.getStr("formid"))){
            // 设置对应数据
            Record dzbdRecord = getDzbdRecord(auditTaskExtension.getStr("formid"), auditproject.getSubappguid());

            // 预留处理表单数据方法
            beforPutData(dzbdRecord);

            jnbeanutil.dataPut(dzbdRecord);
        }
        else{
            log.info("未获取到表单");
        }

        jnbeanutil.coverData(dataBean,tycertcatcode,true);
    }




    public Record getDzbdRecord(String formId, String rowGuid) {

        Map<String, String> paramMap = new HashMap<>();
        JSONObject reqParam = new JSONObject();
        reqParam.put("formId", formId);
        reqParam.put("rowGuid", rowGuid);
        paramMap.put("params", reqParam.toJSONString());

        Record record = new Record();

        String result = getContent(E_FORM_URL + "sform/getPageData", paramMap);
        log.info("电子表单数据：" + result);
        // 解析表单json
        if (StringUtil.isNotBlank(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject recordData = jsonObject.getJSONObject("custom").getJSONObject("recordData");
            // 主表 基本信息
            JSONArray mainList = recordData.getJSONArray("mainList");
            if (CollectionUtils.isNotEmpty(mainList)) {
                JSONArray rowList = mainList.getJSONObject(0).getJSONArray("rowList");
                // 解析rowList
                if (CollectionUtils.isNotEmpty(rowList)) {
                    rowList.forEach(e -> {
                        JSONObject row = (JSONObject) e;
                        record.set(row.getString("FieldName"), row.getString("value") == null ? "" : row.getString("value"));
                    });
                }
            }
        }
        log.info("record" + JSONObject.toJSONString(record));
        return  record;
    };
    private String getContent(String url, Map<String, String> stringMap) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httpPost = new HttpPost(url);
        try {
            // 设置提交方式
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            // 添加参数
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (stringMap.size() != 0) {
                Set keySet = stringMap.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String k = it.next().toString();// key
                    String v = stringMap.get(k);// value
                    nameValuePairs.add(new BasicNameValuePair(k, v));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 获得http响应体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 响应的结果
                String content = EntityUtils.toString(entity, "UTF-8");
                return content;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "获取数据错误";
    }

    public CertInfoExtension getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertInfoExtension dataBean) {
        this.dataBean = dataBean;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }
    public String getTycertcatcode() {
        return tycertcatcode;
    }

    public void setTycertcatcode(String tycertcatcode) {
        this.tycertcatcode = tycertcatcode;
    }

}
