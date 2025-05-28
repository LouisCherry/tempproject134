package com.epoint.auditresource.auditrscompanybaseinfo.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.api.IJnAuditTaskService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.jnycsl.utils.HttpUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 法人基本信息表list页面对应的后台
 *
 * @author Joyce
 * @version [版本号, 2017-04-11 16:25:35]
 */
@RestController("jnauditrscompanybaseinfolistaction")
@Scope("request")
public class JnAuditRsCompanyBaseinfoListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 3453932596650474422L;

    /**
     * 企业库接口
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IJnAuditTaskService iJnAuditTaskService;

    /**
     * 法人基本信息表实体对象
     */
    private AuditRsCompanyBaseinfo dataBean;


    /**
     * 表格控件model
     */
    private DataGridModel<AuditRsCompanyBaseinfo> model;

    @Override
    public void pageLoad() {
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                    .getAuditRsCompanyBaseinfoByRowguid(sel).getResult();
            auditRsCompanyBaseinfo.setIs_history("1");
            iAuditRsCompanyBaseinfo.updateAuditRsCompanyBaseinfo(auditRsCompanyBaseinfo);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditRsCompanyBaseinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditRsCompanyBaseinfo>() {

                @Override
                public List<AuditRsCompanyBaseinfo> fetchData(int first, int pageSize, String sortField,
                                                              String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //搜索条件
                    if (StringUtil.isNotBlank(dataBean.getOrganname())) {
                        sql.like("organname", dataBean.getOrganname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getCreditcode())) {
                        sql.like("creditcode", dataBean.getCreditcode());
                    }
                    if (StringUtil.isNotBlank(dataBean.getOrgancode())) {
                        sql.like("organcode", dataBean.getOrgancode());
                    }
                    sql.eq("is_history", "0");
                    sql.setOrderDesc("operatedate");
                    PageData<AuditRsCompanyBaseinfo> pageData = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByPage(AuditRsCompanyBaseinfo.class, sql.getMap(), first,
                                    pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public void getAutodetail(String rowguid) throws IOException, DocumentException {
        String msg = "";
        Record xinyong = new Record();
        AuditRsCompanyBaseinfo baseinfo = iAuditRsCompanyBaseinfo.getAuditRsCompanyBaseinfoByRowguid(rowguid).getResult();
        String certnum = baseinfo.getCreditcode();
        String applyername = baseinfo.getOrganname();
        List<Record> tasklist = iJnAuditTaskService.getXinYongTaskList();
        if (tasklist != null && tasklist.size() > 0) {
            Random generator = new SecureRandom();
            int randomIndex = generator.nextInt(tasklist.size());
            Record task = tasklist.get(randomIndex);
            String factUnitName = task.getStr("OUNAME");
            String matterName = task.getStr("taskName");
            String matterCode = task.getStr("item_id");
            Map<String, String> headerRequest = new HashMap<String, String>();
            Map<String, String> bodyRequest = new HashMap<String, String>();
            Map<String, String> body1Request = new HashMap<String, String>();
            Map<String, String> body2Request = new HashMap<String, String>();
            headerRequest.put("ApiKey", "725356385771978752");
            bodyRequest.put("client_id", "8b4eed0006");
            bodyRequest.put("client_secret", "91b5cc5d573f4758a8210415847d55e0");
            bodyRequest.put("grant_type", "client_id_secret");
            bodyRequest.put("resource_provider_id", "sco");
            String params = JSONObject.toJSONString(bodyRequest);
            body2Request.put("secret_keys", params);
            String url = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_lhjcsfrz";
            String str = HttpUtils.postHttp(url, body2Request, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            if (responseJsonObj != null && responseJsonObj.getString("code").equals("200")) {
                String data = responseJsonObj.getString("data");
                String data1 = data.replaceAll("\r\n", "");
                String data2 = data1.replaceAll("\\\\", "");
                JSONObject dateObj = (JSONObject) JSONObject.parse(data2);
                String access_token = dateObj.getString("access_token");
                String systemName = "一窗受理";
                body1Request.put("ztmc", applyername);
                body1Request.put("ztlb", "1");
                body1Request.put("unitId", "O0000000000000000096");
                body1Request.put("factUnitName", factUnitName);
                body1Request.put("matterCode", matterCode);
                body1Request.put("matterName", matterName);
                body1Request.put("powerMaterCode", "-1");
                body1Request.put("businessProcessId", UUID.randomUUID().toString());
                body1Request.put("systemName", systemName);
                body1Request.put("access_token", access_token);
                String urldetail = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_lhjccx_zwfwptzy";
                String strdetail = HttpUtils.postHttp(urldetail, body1Request, headerRequest);
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
                        msg = "没有查询到红黑名单请继续办理";
                        addCallbackParam("msg", msg);
                        return;
                    }
                    Element jbxx = datas.element("jbxx");
                    Element sxcjdx = datas.element("sxcjdx");
                    if (sxcjdx == null) {
                        sxcjdx = datas.element("sxjldx");
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
                    if (sjly.equals("2")) {
                        ly = "山东公共信用信息平台";
                    }
                    xinyong.put("frmc", frmc);
                    xinyong.put("nsrsbh", nsrsbh);
                    xinyong.put("tyshxydm", tyshxydm);
                    xinyong.put("gszch", gszch);
                    xinyong.put("mdlb", mdlb);
                    xinyong.put("qssj", qssj);
                    xinyong.put("yxqz", yxqz);
                    xinyong.put("lryy", lryy);
                    xinyong.put("sjly", ly);

                    Map<String, String> headerRequest1 = new HashMap<String, String>();
                    Map<String, String> bodyRequest1 = new HashMap<String, String>();
                    headerRequest1.put("ApiKey", "725356385771978752");
                    String tyshxydm1 = xinyong.getStr("tyshxydm");
                    if (StringUtil.isNotBlank(tyshxydm1)) {
                        tyshxydm1 = certnum;
                    }
                    bodyRequest1.put("creditCode", tyshxydm1);
                    bodyRequest1.put("orgName", xinyong.getStr("frmc"));
                    bodyRequest1.put("unitId", "O0000000000000000096");
                    bodyRequest1.put("factUnitName", factUnitName);
                    bodyRequest1.put("access_token", access_token);
                    String url1 = "http://59.206.202.180:443/gateway/api/1/sfgw_xyfw_xyhc_zwfwptzy";
                    String str1 = HttpUtils.postHttp(url1, bodyRequest1, headerRequest1);
                    JSONObject JsonObj = (JSONObject) JSONObject.parse(str1);
                    String data5 = JsonObj.getString("data");
                    JSONObject JsonObj1 = (JSONObject) JSONObject.parse(data5);
                    String result1 = JsonObj1.getString("result");
                    String result2 = StringEscapeUtils.unescapeJavaScript(result1);
                    String result3 = removeQuotation(result2);
                    JSONObject JsonObj2 = (JSONObject) JSONObject.parse(result3);
                    String result5 = JsonObj2.getString("result");
                    if (StringUtil.isBlank(result5)) {
                        msg = "没有查询到该企业信息";
                        addCallbackParam("msg", msg);
                        return;
                    }
                    if (result5.contains("domain")) {
                        result5 = result5.replace("domain", "xypt.sd.cegn.cn");
                    }
                    msg = "信用查询成功!,地址为" + result5;
                } else {
                    msg = "未查询到信用接口信息";
                }
            } else {
                msg = "请求数据失败，统一认证失败";
            }
        } else {
            msg = "调用失败！";
        }
        addCallbackParam("msg", msg);
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


    public AuditRsCompanyBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsCompanyBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsCompanyBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

}
