package com.epoint.auditproject.auditproject.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnycsl.utils.HttpUtils;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 核价页面对应的后台
 *
 * @author Administrator
 */
@RestController("sourceaction")
@Scope("request")
public class SourceAction extends BaseController {

    private static final long serialVersionUID = 7738943336465402257L;

    /**
     * 办件信息实体对象
     */
    private AuditProject auditProject;

    /**
     * 办件信息实体对象
     */
    private Record dateBean = new Record();


    /**
     * 办件信息实体对象
     */
    private AuditTask auditTask;
    /**
     * 事项标识
     */
    private String taskGuid;
    /**
     * 办件标识
     */
    private String projectGuid;
    private String certnum;
    private String num;
    private String idea;
    private String content;
    private String access_token;
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IZjCreidtqueryService iZjCreidtqueryService;

    @Override
    public void pageLoad() {

        projectGuid = getRequestParameter("projectGuid");
        taskGuid = getRequestParameter("taskGuid");
        auditTask = auditTaskService.selectTaskByRowGuid(taskGuid).getResult();
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,is_fee,applyertype,has_xycx ";
        if(auditTask!=null){
            auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, auditTask.getAreacode())
                    .getResult();
        }else{
            auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, null)
                    .getResult();
            if (auditProject != null) {
                auditTask = auditTaskService.selectTaskByRowGuid(auditProject.getTaskguid()).getResult();
                auditProject.set("has_xycx", "1");
                auditProjectService.updateProject(auditProject);
            }
        }


        certnum = getRequestParameter("certnum");
        String applyername = getRequestParameter("applyername");
        String certownertype = getRequestParameter("certownertype");
        try {
            getrourcedetail(applyername, certownertype);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //调用成功信用接口后即可
            if (auditProject != null) {
                auditProject.set("has_xycx", "1");
                auditProjectService.updateProject(auditProject);
            }
        }
    }

    public void getrourcedetail(String applyername, String certownertype) throws IOException, DocumentException {
        Map<String, String> headerRequest = new HashMap<String, String>();
        Map<String, String> bodyRequest = new HashMap<String, String>();
        Map<String, String> body1Request = new HashMap<String, String>();
//        Map<String, String> body2Request = new HashMap<String, String>();
//        headerRequest.put("ApiKey", "725356385771978752");
//        bodyRequest.put("client_id", "8b4eed0006");
//        bodyRequest.put("client_secret", "91b5cc5d573f4758a8210415847d55e0");
//        bodyRequest.put("grant_type", "client_id_secret");
//        bodyRequest.put("resource_provider_id", "sco");
//        String params = JSONObject.toJSONString(bodyRequest);
//        body2Request.put("secret_keys", params);
        String url = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_lhjcsfrz?secret_keys={\"client_id\":\"8b4eed0006\"," +
                "\"client_secret\":\"91b5cc5d573f4758a8210415847d55e0\",\"grant_type\":\"client_id_secret\",\"resource_provider_id\":\"sco\"}";
        log.info("sfgw_xyfw_lhjcsfrz request:" + url);
        String str = HttpUtils.postXinyong(url);
        log.info("sfgw_xyfw_lhjcsfrz respnse:" + str);
        JSONObject responseJsonObj = new JSONObject();
        responseJsonObj = (JSONObject) JSONObject.parse(str);
        if (responseJsonObj != null && "200".equals(responseJsonObj.getString("code"))) {
            String data = responseJsonObj.getString("data");
            String data1 = data.replaceAll("\r\n", "");
            String data2 = data1.replaceAll("\\\\", "");
            JSONObject dateObj = (JSONObject) JSONObject.parse(data2);
            access_token = dateObj.getString("access_token");
            if ("10".equals(certownertype)) {
                certownertype = "1";
            } else {
                certownertype = "2";
                body1Request.put("ztdm", certnum);
            }
            String factUnitName = auditTask.getOuname();

            String matterName = auditTask.getTaskname();

            String systemName = "一窗受理";
//            body1Request.put("ztmc", applyername);
//            body1Request.put("ztlb", certownertype);
//            body1Request.put("unitId", "O0000000000000000096");
//            body1Request.put("factUnitName", factUnitName);
//            body1Request.put("matterCode", auditTask.getItem_id());
//            body1Request.put("matterName", matterName);
//            body1Request.put("powerMaterCode", "-1");
//            body1Request.put("businessProcessId", projectGuid);
//            body1Request.put("systemName", systemName);
//            body1Request.put("access_token", access_token);
            String urldetail = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_lhjccx_zwfwptzy?ztmc=" + applyername
                    + "&ztlb=" + certownertype + "&unitId=O0000000000000000096&factUnitName=" + factUnitName + "&matterCode="
                    + auditTask.getItem_id() + "&matterName=" + matterName + "&powerMaterCode=-1&businessProcessId=" + projectGuid
                    + "&systemName=" + systemName + "&access_token=" + access_token;
            log.info("sfgw_xyfw_lhjccx_zwfwptzy request:" + urldetail);
            String strdetail = HttpUtils.postXinyong(urldetail);
            log.info("sfgw_xyfw_lhjccx_zwfwptzy respnse:" + strdetail);
            AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                    .getAreaByAreacode(auditProject.getAreacode()).getResult();
            String areaName = "";
            if (auditOrgaArea != null) {
                areaName = auditOrgaArea.getXiaquname();
            }

            //添加信用查询调用次数
            ZjCreidtquery ZjCreidtquery = new ZjCreidtquery();
            ZjCreidtquery.setRowguid(UUID.randomUUID().toString());
            ZjCreidtquery.setCreatetime(new Date());
            ZjCreidtquery.setType("2");
            ZjCreidtquery.setAreacode(auditProject.getAreacode());
            ZjCreidtquery.setAreaname(areaName);
            ZjCreidtquery.setXytaotal("1");
            iZjCreidtqueryService.insert(ZjCreidtquery);

            JSONObject strdetailJsonObj = new JSONObject();
            strdetailJsonObj = (JSONObject) JSONObject.parse(strdetail);
            if (strdetailJsonObj != null) {
                String datadatil = strdetailJsonObj.getString("data");
                JSONObject datadatilobj = (JSONObject) JSONObject.parse(datadatil);
                String result = datadatilobj.getString("result");
                result = result.replace("\"", "");

                Document document = DocumentHelper.parseText(result);
                Element rootElement = document.getRootElement();
                Element datas = rootElement.element("datas");
                if (datas.isTextOnly() || datas == null) {

//                    Map<String, String> headerRequest1 = new HashMap<String, String>();
//                    Map<String, String> bodyRequest1 = new HashMap<String, String>();
//                    headerRequest1.put("ApiKey", "725356385771978752");
//                    bodyRequest1.put("creditCode", certnum);
//                    bodyRequest1.put("orgName", applyername);
//                    bodyRequest1.put("unitId", "O0000000000000000096");
//                    bodyRequest1.put("factUnitName", auditTask.getOuname());
//                    bodyRequest1.put("access_token", access_token);
                    String url1 = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_xyhc_zwfwptzy?creditCode=" + certnum
                            + "&orgName=" + applyername + "&unitId=O0000000000000000096&factUnitName=" + auditTask.getOuname()
                            + "&access_token=" + access_token;
                    log.info("sfgw_xyfw_xyhc_zwfwptzy request:" + url1);
                    String str1 = HttpUtils.postXinyong(url1);
                    log.info("sfgw_xyfw_xyhc_zwfwptzy respnse:" + str1);
                    JSONObject JsonObj = (JSONObject) JSONObject.parse(str1);
                    String data5 = JsonObj.getString("data");
                    JSONObject JsonObj1 = (JSONObject) JSONObject.parse(data5);
                    String result1 = JsonObj1.getString("result");
                    String result2 = StringEscapeUtils.unescapeJavaScript(result1);
                    String result3 = removeQuotation(result2);
                    JSONObject JsonObj2 = (JSONObject) JSONObject.parse(result3);
                    String result5 = JsonObj2.getString("result");
                    if (StringUtil.isBlank(result5)) {
                        addCallbackParam("msg", "没有查询到该企业信息");
                        return;
                    }
                    if (result5.contains("domain")) {
                        result5 = result5.replace("domain", "xypt.sd.cegn.cn");
                    }
                    addCallbackParam("msg", "没有查询到红黑名单请继续办理");
                    addCallbackParam("result", result5);
                    return;
                }
                Element jbxx = datas.element("jbxx");
                Element sxcjdx = datas.element("sxcjdx");
                if (sxcjdx == null) {
                    sxcjdx = datas.element("sxjldx");
                    addCallbackParam("flag", "1");
                }
                Element datasx = sxcjdx.element("data");
                String frmc = jbxx.elementText("frmc");
                String nsrsbh = jbxx.elementText("nsrsbh");
                String tyshxydm = jbxx.elementText("tyshxydm");

                if (StringUtil.isNotBlank(tyshxydm) || tyshxydm == null || tyshxydm == "") {
                    tyshxydm = certnum;
                }
                String gszch = jbxx.elementText("gszch");
                String mdlb = datasx.elementText("mdlb");
                String qssj = datasx.elementText("qssj");
                String yxqz = datasx.elementText("yxqz");
                String lryy = datasx.elementText("lryy");
                String sjly = datasx.elementText("sjly");
                String ly = "国家信用信息共享平台";
                if ("2".equals(sjly)) {
                    ly = "山东公共信用信息平台";
                }
                dateBean.put("frmc", frmc);
                dateBean.put("nsrsbh", nsrsbh);
                dateBean.put("tyshxydm", tyshxydm);
                dateBean.put("gszch", gszch);
                dateBean.put("mdlb", mdlb);
                dateBean.put("qssj", qssj);
                dateBean.put("yxqz", yxqz);
                dateBean.put("lryy", lryy);
                dateBean.put("sjly", ly);


//                Map<String, String> headerRequest1 = new HashMap<String, String>();
//                Map<String, String> bodyRequest1 = new HashMap<String, String>();
//                headerRequest1.put("ApiKey", "725356385771978752");
                String tyshxydm1 = dateBean.getStr("tyshxydm");
                if (StringUtil.isNotBlank(tyshxydm1)) {
                    tyshxydm1 = certnum;
                }
//                bodyRequest1.put("creditCode", tyshxydm1);
//                bodyRequest1.put("orgName", dateBean.getStr("frmc"));
//                bodyRequest1.put("unitId", "O0000000000000000096");
//                bodyRequest1.put("factUnitName", auditTask.getOuname());
//                bodyRequest1.put("access_token", access_token);
                String url1 = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_xyhc_zwfwptzy?creditCode=" + tyshxydm1
                        + "&orgName=" + dateBean.getStr("frmc") + "&unitId=O0000000000000000096&factUnitName=" + auditTask.getOuname()
                        + "&access_token=" + access_token;
//                String url1 = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_xyhc_zwfwptzy";
                log.info("sfgw_xyfw_xyhc_zwfwptzy request:" + url1);
                String str1 = HttpUtils.postXinyong(url1);
                log.info("sfgw_xyfw_xyhc_zwfwptzy respnse:" + str1);
                // 添加红黑名单调用次数
                ZjCreidtquery ZjCreidtquery2 = new ZjCreidtquery();
                ZjCreidtquery2.setRowguid(UUID.randomUUID().toString());
                ZjCreidtquery2.setCreatetime(new Date());
                ZjCreidtquery2.setType("1");
                ZjCreidtquery2.setAreacode(auditProject.getAreacode());
                ZjCreidtquery2.setAreaname(areaName);
                ZjCreidtquery2.setXytaotal("1");
                iZjCreidtqueryService.insert(ZjCreidtquery2);
                JSONObject JsonObj = (JSONObject) JSONObject.parse(str1);
                String data5 = JsonObj.getString("data");
                JSONObject JsonObj1 = (JSONObject) JSONObject.parse(data5);
                String result1 = JsonObj1.getString("result");
                String result2 = StringEscapeUtils.unescapeJavaScript(result1);
                String result3 = removeQuotation(result2);
                JSONObject JsonObj2 = (JSONObject) JSONObject.parse(result3);
                String result5 = JsonObj2.getString("result");
                if (StringUtil.isBlank(result5)) {
                    addCallbackParam("msg", "没有查询到该企业信息");
                    return;
                }
                if (result5.contains("domain")) {
                    result5 = result5.replace("domain", "xypt.sd.cegn.cn");
                }
                addCallbackParam("result", result5);

            }

        } else {
            String msg = "请求数据失败，统一认证失败";
            addCallbackParam("msg", msg);
        }
    }


    public void summit() throws DocumentException {
        String applyername = getRequestParameter("applyername");
        String certownertype = getRequestParameter("certownertype");
        Map<String, String> headerRequest = new HashMap<String, String>();
        Map<String, String> bodyRequest = new HashMap<String, String>();
        headerRequest.put("ApiKey", "725356385771978752");
        bodyRequest.put("fkbm", auditTask.getOuname());
        bodyRequest.put("ztmc", applyername);
        if ("10".equals(certownertype)) {
            certownertype = "0";
            bodyRequest.put("tyshxydm", dateBean.getStr("tyshxydm"));

        } else {
            certownertype = "1";
            bodyRequest.put("zjlx", "10");
            bodyRequest.put("zjhm", certnum);
        }
        bodyRequest.put("mdmc", dateBean.getStr("mdlb"));
        bodyRequest.put("ztlb", certownertype);

        String nsrsbh = dateBean.getStr("nsrsbh");
        String jclb = "1";
        if (StringUtil.isNotBlank(nsrsbh)) {
            jclb = "2";
        }
        bodyRequest.put("jclb", jclb);
        bodyRequest.put("csnr", content);
        bodyRequest.put("sjje", num);
        bodyRequest.put("jcsm", idea);
        bodyRequest.put("qlbm", auditTask.getItem_id());
        bodyRequest.put("access_token", access_token);
        String url = "http://59.206.202.180:443/gateway/api/2/sfgw_xyfw_lhjcfk2019";
        String str = HttpUtils.postHttp(url, bodyRequest, headerRequest);
        JSONObject strdetailJsonObj = new JSONObject();
        strdetailJsonObj = (JSONObject) JSONObject.parse(str);
        if (strdetailJsonObj != null && "200".equals(strdetailJsonObj.getString("code"))) {
            String datadatil = strdetailJsonObj.getString("data");
            JSONObject datadatilobj = (JSONObject) JSONObject.parse(datadatil);
            String detail = datadatilobj.getString("result");
            if (StringUtil.isNotBlank(detail)) {
                addCallbackParam("result", "反馈成功");
            } else {
                addCallbackParam("msg", "反馈失败");
            }
        }
    }


    public static String removeQuotation(String begin) {
        String result = "";
        char[] begins = begin.toCharArray();
        int k = 0;
        int m = begins.length;
        if (begins != null) {
            char first = begins[0];
            char last = begins[m - 1];
            if (first == '"') {
                k = 1;
            }
            if (last == '"') {
                m = m - 1;
            }
        }
        result = begin.substring(k, m);
        return result;
    }

    public AuditProject getAuditProject() {
        return auditProject;
    }

    public void setAuditProject(AuditProject auditProject) {
        this.auditProject = auditProject;
    }


    public String getAccess_token() {
        return access_token;
    }


    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public Record getDateBean() {
        return dateBean;
    }


    public void setDateBean(Record dateBean) {
        this.dateBean = dateBean;
    }


    public AuditTask getAuditTask() {
        return auditTask;
    }


    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    public String getCertnum() {
        return certnum;
    }

    public void setCertnum(String certnum) {
        this.certnum = certnum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
