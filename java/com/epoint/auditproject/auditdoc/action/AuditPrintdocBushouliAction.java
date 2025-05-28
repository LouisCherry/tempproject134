package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBushouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBushouli;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 通过不予受理决定书打印预览页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-02 15:38:34]
 */
@RestController("auditprintdocbushouliaction")
@Scope("request")
public class AuditPrintdocBushouliAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 7492773091496619808L;

    /**
     * 办件查询字段
     */
    private static final String FIELDS = "*";

    /**
     * 通过不予受理通知书实体对象
     */
    private AuditPrintdocBushouli auditprintdocbushouli = null;

    /**
     * 办件实体
     */
    private AuditProject auditproject = null;

    /**
     * 事项实体
     */
    private AuditTask audittask = null;

    /**
     * sdkind下拉列表modal
     */
    private List<SelectItem> sdkindModal = null;

    /**
     * sdaddress下拉列表modal
     */
    private List<SelectItem> sdaddressModal = null;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 事项标志
     */
    private String taskguid;

    /**
     * 流程版本实例guid
     */
    private String processVersionInstanceGuid;

    /**
     * 打印时间
     */

    private String dyrq;

    private String dyrq1;

    /**
     * 办件事项
     */
    private String sbsx;
    /**
     * 办号
     */
    private String txtbh = "";
    /**
     * 文号
     */
    private String wn;

    /**
     * 事项类别
     */
    private String tasktype;
    /**
     * 联系人
     */
    private String lxr;

    /**
     * 联系方式
     */
    private String lxfsinfo;

    /**
     * 受理依据
     */
    private String laws;
    /**
     * 受理时间
     */
    private String timeinfo;
    /**
     * 否定原因
     */
    private String denyReason;
    /**
     * 办件状态
     */
    private String status;

    @Autowired
    IAuditPrintdocBushouli bushouliService;

    @Autowired
    IAuditProjectUnusual unusualService;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    ICodeItemsService codeItems;

    @Autowired
    IAuditTask auditTaskService;

    @Autowired
    IHandleConfig handleConfigService;

    private String area = "";


    /**
     * 打印时间
     */

    private String operatedate;

    @SuppressWarnings("unused")
    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");

        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditproject = auditProjectService
                .getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();

        audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        taskguid = audittask.getRowguid();
        // 不予受理通知书实体
        auditprintdocbushouli = bushouliService.getDocBushouliByProjectGuid(projectguid).getResult();
        if (auditprintdocbushouli == null) {
            auditprintdocbushouli = new AuditPrintdocBushouli();
            auditprintdocbushouli.setOperatedate(new Date());
            auditprintdocbushouli.setOperateusername(userSession.getDisplayName());
        }
        if (!isPostback()) {
            // 事项信息
            audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            // 办件信息
            auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
            tasktype = codeItems.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            // 办件事项 办件名称或事项名称
            sbsx = StringUtil.isBlank(auditprintdocbushouli.getTaskname()) ? auditproject.getProjectname()
                    : auditprintdocbushouli.getTaskname();
            // 日期
            dyrq = EpointDateUtil.convertDate2String(
                    auditprintdocbushouli.getDyrq() == null ? auditproject.getBanjiedate() : auditprintdocbushouli.getDyrq(),
                    "yyyy年MM月dd日");
            operatedate = EpointDateUtil.convertDate2String(
                    (auditprintdocbushouli.getOperatedate() == null ? new Date() : auditprintdocbushouli.getOperatedate()),
                    "yyyy年MM月dd日");
            lxr = StringUtil.isBlank(auditprintdocbushouli.getLxr()) ? userSession.getDisplayName()
                    : auditprintdocbushouli.getLxr();
            dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            // 办件特殊操作
            AuditProjectUnusual unusual = unusualService
                    .getProjectUnusualByProjectGuidAndType(projectguid, ZwfwConstant.BANJIANOPERATE_TYPE_BYSL)
                    .getResult();
            if (unusual != null) {
                denyReason = StringUtil.isBlank(auditprintdocbushouli.getOpinion()) ? unusual.getNote()
                        : auditprintdocbushouli.getOpinion();
            } else {
                denyReason = auditprintdocbushouli.getOpinion();
            }

            // 获取时间年份 ZJG201405140002 2014
            // 办件编号的前缀 ZJG
            String AS_FLOWSN_PRE = handleConfigService.getFrameConfig("AS_FLOWSN_PRE", auditproject.getCenterguid())
                    .getResult();
            Calendar a = Calendar.getInstance();
            String year = String.valueOf(a.get(Calendar.YEAR));
            String fyear = AS_FLOWSN_PRE + year; // ZJG2014

            // 文号 〔2014〕1号
            String orgwn = auditproject.getOrgwn();
            if (StringUtil.isBlank(orgwn)) {
                // 获取新文书号
                //TODO 暂缓
                int newbhinfo = auditProjectService.getMaxOrgNumberinfo(null, projectguid,
                        area, auditproject.getTask_id()).getResult();
                txtbh = String.valueOf(newbhinfo);
                String orgwnnew = "〔" + year + "〕" + txtbh + "号";
                auditproject.setOrgnumber(Integer.parseInt(txtbh));
                auditproject.setOrgwn(orgwnnew);
                auditProjectService.updateProject(auditproject);
            }

            // 获取更新文书号后办件信息
            // String wninfo = auditproject.getOrgwn();
            // int orgnwn = wninfo.lastIndexOf("〔");
            StringBuffer orgnwninfoold;
            // 是文书上的“补、受”字
            // if ("1".equals(audittask.getIs_sz())) {
            // orgnwninfoold = new
            // StringBuffer(auditproject.getOrgwn()).insert(orgnwn, "受字");
            // }
            // else {
            orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
            // }
            String whnumber = orgnwninfoold.toString().replace("〕", "〕第");
            int numberbh = whnumber.indexOf('第') + 1;
            String numberwn = whnumber.substring(numberbh);
            int dwz = numberwn.indexOf('号');
            String numberwh = numberwn.substring(0, dwz);
            wn = whnumber.substring(0, numberbh);
            txtbh = numberwh;
            // 办件状态
            status = codeItems.getItemTextByCodeName("办件状态", auditproject.getStatus().toString()) + "。";
            //TODO 暂缓
            /*AuditProjectOperation auditprojectoperation = auditprojectoperationservice
                    .selectOperationByProjectGuidAndType(projectguid, ZwfwConstant.BANJIE_TYPE_BYSL);
            if (auditprojectoperation != null) {
                updateSave();
            }*/

            //取办结时间
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            }

            addViewData("dyrq", dyrq);// 打印日期
            addViewData("lxr", lxr);// 联系人
            addViewData("operatedate", operatedate);
        } else {
            operatedate = getViewData("operatedate");
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            } else {
                dyrq = getViewData("dyrq");
            }

            lxr = getViewData("lxr");
        }
        this.addCallbackParam("sdaddress", auditprintdocbushouli.getSdaddress());
        this.addCallbackParam("sdkind", auditprintdocbushouli.getSdkind());
    }

    /**
     * 保存文书信息
     */
    protected void updateSave() {
        // 事项信息
        audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        // 办件信息
        auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, audittask.getAreacode())
                .getResult();

        String wninfo = auditproject.getOrgwn();
        int n = wninfo.indexOf('〔');
        String wnumber = wninfo.substring(n);
        int numberbh = wnumber.indexOf('〕') + 1;
        // String numberwn = wnumber.substring(numberbh);
        // int dwz = numberwn.indexOf("号");
        // String numberwh = numberwn.substring(0, dwz);
        String orgwnnew;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";

        // 根据ProjectGuid判断是否存在不予受理决定书
        if (StringUtil.isNotBlank(auditprintdocbushouli.getRowguid())) {
            auditprintdocbushouli.setOpinion(denyReason);
            auditprintdocbushouli.setTaskname(sbsx);
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
            auditprintdocbushouli.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocbushouli.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocbushouli.setLxr(lxr);
            auditprintdocbushouli.setBh(txtbh);
            bushouliService.updateDocBushouli(auditprintdocbushouli);
        } else {
            auditprintdocbushouli.setRowguid(UUID.randomUUID().toString());
            auditprintdocbushouli.setProjectguid(projectguid);
            auditprintdocbushouli.setOpinion(denyReason);
            auditprintdocbushouli.setTaskname(sbsx);
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
            auditprintdocbushouli.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocbushouli.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocbushouli.setLxr(lxr);
            auditprintdocbushouli.setBh(txtbh);
            bushouliService.addDocBushouli(auditprintdocbushouli);
        }
        auditproject.setOrgnumber(Integer.parseInt(txtbh));
        auditproject.setOrgwn(orgwnnew);
        auditProjectService.updateProject(auditproject);
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "pauditprintdocbushouli?projectguid=" + projectguid + "&processVersionInstanceGuid="
                + processVersionInstanceGuid + "&taskguid=" + audittask.getRowguid();
        addCallbackParam("msg", printaddress);

    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSdkindModel() {
        if (sdkindModal == null) {
            sdkindModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "送达方式", null, false));
        }
        return this.sdkindModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSdaddressModel() {
        if (sdaddressModal == null) {
            sdaddressModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "送达地点", null, false));
        }
        return this.sdaddressModal;
    }

    public AuditPrintdocBushouli getAuditprintdocbushouli() {
        return auditprintdocbushouli;
    }

    public void setAuditprintdocbushouli(AuditPrintdocBushouli auditprintdocbushouli) {
        this.auditprintdocbushouli = auditprintdocbushouli;
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public String getProjectguid() {
        return projectguid;
    }

    public void setProjectguid(String projectguid) {
        this.projectguid = projectguid;
    }

    public String getProcessVersionInstanceGuid() {
        return processVersionInstanceGuid;
    }

    public void setProcessVersionInstanceGuid(String processVersionInstanceGuid) {
        this.processVersionInstanceGuid = processVersionInstanceGuid;
    }

    public String getDyrq() {
        return dyrq;
    }

    public void setDyrq(String dyrq) {
        this.dyrq = dyrq;
    }

    public String getDyrq1() {
        return dyrq1;
    }

    public void setDyrq1(String dyrq1) {
        this.dyrq1 = dyrq1;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
    }

    public String getTxtbh() {
        return txtbh;
    }

    public void setTxtbh(String txtbh) {
        this.txtbh = txtbh;
    }

    public String getWn() {
        return wn;
    }

    public void setWn(String wn) {
        this.wn = wn;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getLxfsinfo() {
        return lxfsinfo;
    }

    public void setLxfsinfo(String lxfsinfo) {
        this.lxfsinfo = lxfsinfo;
    }

    public String getLaws() {
        return laws;
    }

    public void setLaws(String laws) {
        this.laws = laws;
    }

    public String getTimeinfo() {
        return timeinfo;
    }

    public void setTimeinfo(String timeinfo) {
        this.timeinfo = timeinfo;
    }

    public String getDenyReason() {
        return denyReason;
    }

    public void setDenyReason(String denyReason) {
        this.denyReason = denyReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }

}
