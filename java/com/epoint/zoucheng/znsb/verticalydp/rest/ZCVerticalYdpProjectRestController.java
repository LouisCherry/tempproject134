package com.epoint.zoucheng.znsb.verticalydp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * [竖屏引导屏办件查询的restful接口] [F9.3竖屏引导屏办件查询的restful接口]
 * 
 * @author xuyunhai
 * @version [1.0, 2017年5月10日]
 
 * @since [智能化产品/v1.0]
 */
@RestController
@RequestMapping("/zcverticalydpproject")
public class ZCVerticalYdpProjectRestController
{
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditOrgaWindowYjs windowservice;

    @Autowired
    private IAuditOrgaConfig auditconfigservice;

    /**
     * 
     * [获取办件编号的前缀] [F9.3获取办件编号的前缀]
     * 
     * @param params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getPreFlowsn", method = RequestMethod.POST)
    public String getPreFlowsn(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));// token验证
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject jsondata = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String config = handleConfigservice.getFrameConfig("AS_FLOWSN_PRE", centerguid).getResult();
            if (StringUtil.isNotBlank(config)) {
                jsondata.put("preflowsn", config);
            }
            else {
                jsondata.put("preflowsn", "STD");
            }

            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 
     * [根据办件流水号获取办件详情] [F9.3 根据办件流水号获取办件详情]
     * 
     * @param params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetailByFlowsn", method = RequestMethod.POST)
    public String getProjectDetailByFlowsn(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));// token验证
            JSONObject obj = (JSONObject) json.get("params");
            String flowsn = obj.getString("flowsn");// 办件号
            String areacode = obj.getString("areacode");
            JSONObject jsondata = new JSONObject();
            if (StringUtil.isNotBlank(flowsn)) {
                jsondata.put("flowsn", flowsn);
                String fields = " rowguid,taskguid,projectname,applyername,acceptuserdate,ouname,banjiedate,status,certnum ";
                AuditProject auditproject = auditProjectService.getAuditProjectByFlowsn(fields, flowsn, areacode)
                        .getResult();
                if (auditproject != null) {
                    jsondata.put("projectname", auditproject.getProjectname());
                    jsondata.put("applyername", auditproject.getApplyername());
                    jsondata.put("rowguid", auditproject.getRowguid());
                    jsondata.put("acceptusertime", auditproject.getAcceptuserdate() != null ? EpointDateUtil
                            .convertDate2String(auditproject.getAcceptuserdate(), EpointDateUtil.DATE_FORMAT) : "");
                    jsondata.put("handleou", auditproject.getOuname());
                    jsondata.put("banjietime", auditproject.getBanjiedate() != null ? EpointDateUtil
                            .convertDate2String(auditproject.getBanjiedate(), EpointDateUtil.DATE_FORMAT) : "");
                    if (StringUtil.isNotBlank(auditproject.getStatus())) {
                        jsondata.put("status",
                                codeItemsService.getItemTextByCodeName("办件状态", auditproject.getStatus().toString()));
                        jsondata.put("statusnum", auditproject.getStatus());
                    }
                    else {
                        jsondata.put("status", "暂无办件状态");
                        jsondata.put("statusnum", 0);
                    }
                    jsondata.put("certnum", auditproject.getCertnum());
                    jsondata.put("searchcode", "ok");
                }
                else {
                    jsondata.put("searchcode", "none");
                }
            }
            else {
                jsondata.put("searchcode", "none");
            }

            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 根据获取办件详情
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyername,acceptuserdate,ouname,banjiedate,status,certnum,flowsn";
            AuditProject project = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (StringUtil.isNotBlank(project)) {

                dataJson.put("projectguid", project.getRowguid());
                dataJson.put("projectname", project.getProjectname());
                dataJson.put("flowsn", project.getFlowsn());
                dataJson.put("ouname", project.getOuname());
                dataJson.put("applyername", project.getApplyername());

                dataJson.put("promiseday", project.getPromise_day() + "个工作日");

                dataJson.put("applydate", EpointDateUtil.convertDate2String(project.getApplydate()));
                dataJson.put("acceptusertime", project.getAcceptuserdate() != null
                        ? EpointDateUtil.convertDate2String(project.getAcceptuserdate(), EpointDateUtil.DATE_FORMAT)
                        : "");

                dataJson.put("banjietime", project.getBanjiedate() != null
                        ? EpointDateUtil.convertDate2String(project.getBanjiedate(), EpointDateUtil.DATE_FORMAT) : "");

                dataJson.put("certnum", project.getCertnum());

                dataJson.put("applyway", project.getApplyway());
                dataJson.put("applywaytext",
                        codeItemsService.getItemTextByCodeName("申请方式", String.valueOf(project.getApplyway())));
                dataJson.put("status", project.getStatus());
                dataJson.put("statustext",
                        codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(project.getStatus())));
                AuditOrgaWindow window = windowservice.getWindowByWindowGuid(project.getWindowguid()).getResult();
                if (window != null) {
                    dataJson.put("windowtel", window.getTel());
                }
                else {
                    dataJson.put("windowtel", "");
                }
                dataJson.put("applyername", project.getApplyername());
                dataJson.put("certnum", project.getCertnum());
                dataJson.put("contactmobile", project.getContactmobile());
                dataJson.put("contactphone", project.getContactphone());
                dataJson.put("contactemail", project.getContactemail());
                dataJson.put("contactperson", project.getContactperson());
                dataJson.put("address", project.getAddress());
                dataJson.put("taskguid", project.getTaskguid());
                dataJson.put("contactpostcode", project.getContactpostcode());
                dataJson.put("contactidnum", project.getContactcertnum());
                dataJson.put("remark", project.getRemark());
                dataJson.put("legal", project.getLegal());
                dataJson.put("legalid", project.getLegalid());
                dataJson.put("promiseenddate", project.getPromiseenddate());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", project.getCenterguid());
                sql.eq("configname", "AS_PROJECT_URL");
                AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    dataJson.put("projecturl", config.getConfigvalue());
                }
                else {
                    dataJson.put("projecturl", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    @RequestMapping(value = "/getProjectListByCertNum", method = RequestMethod.POST)
    public String getProjectListByCertNum(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));// token验证
            JSONObject obj = (JSONObject) json.get("params");
            String certnum = obj.getString("CERTNUM");// 身份证号
            String areacode = obj.getString("areacode");
            String page = obj.getString("page");// 当前页数
            String pagesize = obj.getString("pagesize");// 每页数
            int pageindex = 0;
            if (StringUtil.isNotBlank(page)) {
                pageindex = Integer.parseInt(page);
            }
            int firstresult = pageindex * Integer.parseInt(pagesize);

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("certnum", certnum);
            sql.eq("areacode", areacode);
            sql.in("status ", "12,16,24,26,30,40,50,90");
            String fields = " rowguid,taskguid,projectname,applyername,acceptuserdate,ouname,banjiedate,status,certnum,flowsn ";
            PageData<AuditProject> projectpage = auditProjectService.getAuditProjectPageData(fields, sql.getMap(),
                    firstresult, Integer.parseInt(pagesize), "APPLYDATE", "desc").getResult();
            List<AuditProject> projetclist = projectpage.getList();
            int total = projectpage.getRowCount();
            List<Record> recordlist = new ArrayList<>();
            if (projetclist != null && !projetclist.isEmpty()) {
                for (AuditProject auditProject : projetclist) {
                    Record record = new Record();
                    record.put("flowsn", JsonUtils.converNull2Str(auditProject.getFlowsn()));
                    record.put("certnum", certnum);
                    record.put("projectname", JsonUtils.converNull2Str(auditProject.getProjectname()));
                    record.put("ouname", JsonUtils.converNull2Str(auditProject.getOuname()));
                    record.put("projectguid", JsonUtils.converNull2Str(auditProject.getRowguid()));
                    if (auditProject.getAcceptuserdate() == null) {
                        record.put("acceptuserdate", "");
                    }
                    else {
                        record.put("acceptuserdate", EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                                EpointDateUtil.DATE_FORMAT));
                    }
                    record.put("bjstatus",
                            codeItemsService.getItemTextByCodeName("办件状态", auditProject.getStatus().toString()));
                    recordlist.add(record);
                }
            }

            JSONObject jsondata = new JSONObject();
            jsondata.put("content", recordlist);
            jsondata.put("totalpages", total / 4);

            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
