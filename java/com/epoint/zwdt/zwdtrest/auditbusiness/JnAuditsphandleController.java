package com.epoint.zwdt.zwdtrest.auditbusiness;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;

import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * [一件事查询接口]
 */
@RestController
@RequestMapping("/jnauditsphandlecontroller")
public class JnAuditsphandleController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTaskExtension taskExtensionService;
    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;
    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditProject projectService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditOrgaWindow windowService;
    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointService;
    @Autowired
    private IAuditProjectUnusual iAuditProjectUnusual;
    @Autowired
    private IWFInstanceAPI9 iWFInstanceAPI9;

    @Autowired
    private IAuditProjectSparetime sparetimeService;

    /**
     * 申请资格认定（申报前验证）
     * 
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getAuditsphandleDetail", method = RequestMethod.POST)
    public String checkQualifications(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAuditsphandleDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            String tablesid = "";
            // 事项与材料情况
            String materialdesc = "";
            // 材料提交情况
            String materialsubmitdesc = "";
            String rcvaddress = "";
            AuditSpIntegratedCompany dataBean = null;
            List<AuditSpITask> spITasks = null;
            Date createdate = null;
            AuditLogisticsBasicinfo auditLogisticsBasicinfo = null;
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String biguid = obj.getString("biguid"); // 事项唯一标识
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditSpInstance spInstance = auditSpInstanceService.getDetailByBIGuid(biguid).getResult();
                if (spInstance != null) {
                    dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid())
                            .getResult();
                    createdate = spInstance.getCreatedate();
                    String tcflowsn = StringUtil.isBlank(spInstance.getStr("tcflowsn")) ? ""
                            : "（套餐编号：" + spInstance.getStr("tcflowsn") + "）";
                    // 获取主题相关信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                            .getAuditSpBusinessByRowguid(spInstance.getBusinessguid()).getResult();
                    if (auditSpBusiness != null) {
                        if (StringUtil.isNotBlank(auditSpBusiness.get("dzbdcategoryguid"))) {
                            // addCallbackParam("ifneeddzbd", 1);
                            // addCallbackParam("categoryGuid",
                            // auditSpBusiness.get("dzbdcategoryguid"));
                            // 过滤该套餐的情形下的电子表单
                            List<AuditSpITask> auditSpITaskList = auditSpITaskService
                                    .getSpITaskByBIGuid(spInstance.getRowguid()).getResult();
                            if (auditSpITaskList != null && auditSpITaskList.size() > 0) {
                                for (int i = 0; i < auditSpITaskList.size(); i++) {
                                    AuditSpITask spitask = auditSpITaskList.get(i);

                                    AuditTaskExtension extension = taskExtensionService
                                            .getTaskExtensionByTaskGuid(spitask.getTaskguid(), false).getResult();

                                    if (extension != null) {
                                        if (StringUtil.isNotBlank(extension.get("tableid"))) {
                                            tablesid += extension.get("tableid") + "-";
                                        }
                                    }
                                }
                            }
                            // addCallbackParam("eformurl",
                            // configservice.getFrameConfigValue("电子表单地址"));
                            // addCallbackParam("ywguid",
                            // spInstance.getRowguid());
                            if (StringUtil.isNotBlank(tablesid)) {
                                // addCallbackParam("tableIds",
                                // tablesid.substring(0, tablesid.length() -
                                // 1));
                            }
                            // iframe高度 宽度
                            if (StringUtil.isNotBlank(auditSpBusiness.get("dzbdheight"))) {
                                // addCallbackParam("dzbdheight",
                                // auditSpBusiness.get("dzbdheight"));
                            }
                            if (StringUtil.isNotBlank(auditSpBusiness.get("dzbdwidth"))) {
                                // addCallbackParam("dzbdwidth",
                                // auditSpBusiness.get("dzbdwidth"));
                            }
                        }
                    }
                }

                spITasks = auditSpITaskService.getSpITaskByBIGuid(biguid).getResult();
                String taskCount = "0";
                String materialCount = "0";
                // 电子材料
                int dzMaterial = 0;
                // 纸质材料
                int zzMaterial = 0;
                // 容缺材料
                int rqMaterial = 0;
                // 补正材料
                int bzMaterial = 0;
                if (spITasks != null) {
                    taskCount = String.valueOf(spITasks.size());
                }
                List<AuditSpIMaterial> auditSpIMaterials = auditSpIMaterialService.getSpIMaterialByBIGuid(biguid)
                        .getResult();
                if (auditSpIMaterials != null) {
                    materialCount = String.valueOf(auditSpIMaterials.size());
                    for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                        if ("15".equals(auditSpIMaterial.getStatus())) {
                            zzMaterial++;
                        }
                        else if ("20".equals(auditSpIMaterial.getStatus())) {
                            dzMaterial++;
                        }
                        else if ("25".equals(auditSpIMaterial.getStatus())) {
                            zzMaterial++;
                            dzMaterial++;
                        }
                        if ("1".equals(auditSpIMaterial.getAllowrongque())) {
                            rqMaterial++;
                        }
                        if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                            bzMaterial++;
                        }
                    }
                }
                materialdesc = "共" + taskCount + "个事项，" + materialCount + "份材料";
                materialsubmitdesc = "提交电子材料" + dzMaterial + "份 提交纸质材料" + zzMaterial + "份 容缺（暂无）" + rqMaterial + "份 需整改"
                        + bzMaterial + "份";
                // ymq
                if (dataBean != null) {
                    // 使用物流
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.get("if_express"))) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("yewuguid=", spInstance.getYewuguid());
                        List<AuditLogisticsBasicinfo> auditLogisticsBasicinfoList = iAuditLogisticsBasicInfo
                                .getAuditLogisticsBasicinfoList(map, "", "").getResult();
                        if (auditLogisticsBasicinfoList != null && auditLogisticsBasicinfoList.size() > 0) {
                            auditLogisticsBasicinfo = auditLogisticsBasicinfoList.get(0);
                            rcvaddress = auditLogisticsBasicinfo.getRcv_prov() + auditLogisticsBasicinfo.getRcv_city()
                                    + auditLogisticsBasicinfo.getRcv_country();
                        }
                    }
                    // addCallbackParam("applyertype",
                    // dataBean.get("applyertype"));
                    // addCallbackParam("if_express",
                    // dataBean.get("if_express"));
                }
                // 获取子申报状态
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biguid).getResult();
                if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
                    // addCallbackParam("subStatus",
                    // auditSpISubapps.get(0).getStatus());
                }
                List<AuditSpITask> auditSpITasks = new ArrayList<>();
                if (spITasks != null && spITasks.size() > 0) {

                    for (AuditSpITask auditSpITask : spITasks) {
                        if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            String fields = " * ";
                            AuditProject auditProject = null;
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStr("isbxq"))
                                    || StringUtil.isBlank(auditSpITask.getStr("isbxq"))) {
                                auditProject = projectService
                                        .getAuditProjectByRowGuid(fields, auditSpITask.getProjectguid(), "320582")
                                        .getResult();
                            }
                            else {
                                // 套餐事项涉及到其他辖区的事项
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("belongxiaqu", auditSpITask.getStr("isbxq"));

                                auditProject = projectService
                                        .getAuditProjectByRowGuid(fields, auditSpITask.getProjectguid(), "320582")
                                        .getResult();

                            }

                            if (auditProject != null) {
                                if (auditProject.getStatus() >= 30) {
                                    auditSpITask.put("pviguid", auditProject.getPviguid());
                                }
                                else {
                                    auditSpITask.put("pviguid", "111");
                                }
                                auditSpITask.put("windowname", auditProject.getWindowname());
                                int status = auditProject.getStatus();
                                auditSpITask.put("status",
                                        codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));
                                // 返回部门名称 测试发现有些办件记录中ouname没有值只有ouguid
                                if (StringUtil.isNotBlank(auditProject.getOuname())) {
                                    auditSpITask.put("ouname", auditProject.getOuname());
                                }
                                else {
                                    if (StringUtil.isNotBlank(auditProject.getOuguid())) {
                                        FrameOu ou = ouservice.getOuByOuGuid(auditProject.getOuguid());
                                        if (ou != null) {
                                            auditSpITask.put("ouname", ou.getOuname());
                                        }
                                    }
                                }

                                // 返回当前处理人
                                String username = "";
                                List<String> usernamelist = new ArrayList<String>();
                                if (auditProject.getStatus() == 24) {// 如果当前步骤为刚收完件并分发了办件，则事项当前处理人为对应窗口人员
                                    List<AuditOrgaWindow> windowList = windowService
                                            .getWindowListByTaskId(auditProject.getTask_id()).getResult();
                                    if (windowList != null && windowList.size() > 0) {
                                        for (AuditOrgaWindow window : windowList) {
                                            List<AuditOrgaWindowUser> windowUser = windowService
                                                    .getUserByWindow(window.getRowguid()).getResult();
                                            if (windowUser != null && windowUser.size() > 0) {
                                                for (AuditOrgaWindowUser user : windowUser) {
                                                    if (StringUtil.isNotBlank(user.getUsername())) {
                                                        usernamelist.add(user.getUsername());
                                                    }

                                                }
                                            }
                                        }
                                        usernamelist = removeDuplicate(usernamelist);// list去重
                                        username = StringUtil.join(usernamelist, ";");
                                    }
                                }
                                if (auditProject.getStatus() == 26) { // 如果是受理人

                                    List<AuditTaskRiskpoint> riskpointList = auditTaskRiskpointService
                                            .getRiskPointListByTaksGuid(auditProject.getTaskguid(), false).getResult();
                                    String acceptguid = "";
                                    if (riskpointList != null && riskpointList.size() > 0) {
                                        for (AuditTaskRiskpoint auditTaskRiskpoint : riskpointList) {

                                            if ("受理".equals(auditTaskRiskpoint.getActivityname())) {
                                                username = auditTaskRiskpoint.getAcceptname();
                                            }
                                        }

                                    }
                                    if (StringUtil.isBlank(username)) {
                                        username = auditProject.getAcceptusername();
                                    }

                                }
                                else if (auditProject.getStatus() > 90) {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("projectguid", auditProject.getRowguid());
                                    AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual
                                            .getAuditProjectUnusualListByPage(sql.getMap(), 0, 1, "OperateDate", "desc")
                                            .getResult().getList().get(0);
                                    username = auditProjectUnusual.getOperateusername();
                                }
                                else {
                                    ProcessVersionInstance pvi = iWFInstanceAPI9
                                            .getProcessVersionInstance(auditProject.getPviguid());
                                    List<WorkflowWorkItem> itemlist = new ArrayList<WorkflowWorkItem>();
                                    List<Integer> statuslist = new ArrayList<Integer>();

                                    statuslist.add(20);
                                    if (pvi != null) {
                                        itemlist = iWFInstanceAPI9.getWorkItemListByPVIGuidAndStatus(pvi, statuslist);
                                        for (WorkflowWorkItem item : itemlist) {
                                            if (StringUtil.isNotBlank(item.getTransactorName())) {
                                                usernamelist.add(item.getTransactorName());
                                            }

                                        }
                                        if (usernamelist != null) {
                                            username = StringUtil.join(usernamelist, ";");
                                        }

                                    }
                                }
                                auditSpITask.put("transactorname", username);
                                if (auditProject.getStatus() == 90) {
                                    auditSpITask.put("banjieusername", auditProject.getBanjieusername());
                                    auditSpITask.put("banjiedate", auditProject.getBanjiedate());
                                    auditSpITask.put("transactorname", auditProject.getBanjieusername());
                                }
                                // 返回预警类型
                                Map<String, String> conditionMap = new HashMap<String, String>();
                                conditionMap.put("infoguid=", auditProject.getRowguid());

                                auditSpITask.put("alerttype", "--");
                                String sparetime = "--";
                                if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
                                        && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ
                                        && !ZwfwConstant.ITEMTYPE_JBJ.equals(auditProject.getTasktype().toString())) {

                                    if (StringUtil.isNotBlank(auditProject.getIs_pause())
                                            && ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
                                        sparetime = "暂停计时";// 办件处于暂停计时状态
                                    }
                                    else {
                                        AuditProjectSparetime auditprojectsparetime = sparetimeService
                                                .getSparetimeByProjectGuid(auditSpITask.getProjectguid()).getResult();
                                        if (auditprojectsparetime != null) {
                                            sparetime = CommonUtil
                                                    .getSpareTimes(auditprojectsparetime.getSpareminutes());
                                        }
                                    }
                                }
                                auditSpITask.put("sparetime", sparetime);
                                auditSpITask.put("businessname", spInstance.getItemname());
                            }
                            auditSpITasks.add(auditSpITask);
                        }
                    }
                }
                if (dataBean != null) {
                    dataJson.put("basejson", dataBean);
                    dataJson.put("zzjgdmz", dataBean.getZzjgdmz());
                    dataJson.put("companyname", dataBean.getStr("companyname"));
                    dataJson.put("address", dataBean.getStr("address"));
                    dataJson.put("contactphone", dataBean.getStr("contactphone"));
                    dataJson.put("jbrname", dataBean.getStr("jbrname"));
                    dataJson.put("jbrtel", dataBean.getStr("jbrtel"));
                    dataJson.put("capital", dataBean.getStr("capital"));
                    dataJson.put("malladdress", dataBean.getStr("malladdress"));
                    String isjbr = "";
                    if (StringUtil.isNotBlank(iCodeItemsService.getItemTextByCodeName("是否", dataBean.getStr("isbr")))) {
                        isjbr = iCodeItemsService.getItemTextByCodeName("是否", dataBean.getStr("isbr"));
                    }
                    dataJson.put("isbr", isjbr);
                    dataJson.put("remark", dataBean.getStr("remark"));
                    dataJson.put("createdate", EpointDateUtil.convertDate2String(createdate, "yyyy-MM-dd"));
                    dataJson.put("materialdesc", materialdesc);
                    dataJson.put("materialsubmitdesc", materialsubmitdesc);
                    for (int i = 0; i < auditSpITasks.size(); i++) {
                        auditSpITasks.get(i).set("index", i + 1);
                    }
                    dataJson.put("plicationlist", auditSpITasks);
                    log.info("=======结束调用getAuditsphandleDetail接口=======");
                    return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取详情信息成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getAuditsphandleDetail接口参数：params【" + params + "】=======");
            log.info("=======getAuditsphandleDetail异常信息：" + e + "=======");
            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取详情信息失败", "");
        }
    }

    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

}
