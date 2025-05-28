package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocShouli;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 通用受理通知书预览页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-10-28 16:46:35]
 */
@RestController("auditprintdocshouliaction")
@Scope("request")
public class AuditPrintdocShouliAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 93959663016111039L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = "*";
    /**
     * 通用通知书service
     */
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IJNAuditProject iJNAuditProject;

    /**
     * service
     */
    @Autowired
    private IAuditPrintdocShouli iAuditPrintdocShouli;
    /**
     * 事项材料
     */
    @Autowired
    private IAuditTaskMaterial auditTaskMaterial;

    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 办件材料service
     */
    @Autowired
    private IHandleMaterial materialservice;

    /**
     * 通用受理通知书service
     */

    /**
     * 通用受理通知书实体对象
     */
    private AuditPrintdocShouli auditprintdocshouli = null;

    private AuditLogisticsBasicinfo auditLogisticsBasicInfo = null;

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
     * 办件事项
     */
    private String sbsx;

    /**
     * 办号
     */
    private String txtbh = "";

    /**
     * 已上传材料列表
     */
    private String txtmaterial;

    /**
     * 文号
     */
    private String wn;

    /**
     * 操作时间
     */

    private String dyrq;

    private String dyrq1;

    /**
     * 打印时间
     */

    private String operatedate;
    private String areacode;

    /**
     * 事项类别
     */
    private String tasktype;

    @Autowired
    private IAuditOrgaWorkingDay auditWorkingDay;

    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private ICodeItemsService codeItemsService;

    private String chkcode;
    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

    /**
     * 情形材料
     */
    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;


    @SuppressWarnings("unused")
    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        // processVersionInstanceGuid=getRequestParameter("ProcessVersionInstanceGuid");
        // 办件信息
        auditproject = iJNAuditProject.getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        // 事项信息
        if (auditproject != null) {
            audittask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        }
        //乡镇延伸个性化情况
        if (audittask != null) {
            String area = "";
            if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
                area = auditproject.getAcceptareacode();
            } else {
                area = ZwfwUserSession.getInstance().getAreaCode();
            }
            AuditTaskDelegate delegate = auditTaskDelegateService
                    .findByTaskIDAndAreacode(audittask.getTask_id(), area).getResult();
            if (delegate != null) {
                if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(delegate.getUsecurrentinfo())) {
                    if (StringUtil.isNotBlank(delegate.getPromise_day())) {
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                        audittask.setPromise_day(delegate.getPromise_day());
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                    }
                    if (StringUtil.isNotBlank(delegate.getAcceptcondition())) {
                        audittask.setAcceptcondition(delegate.getAcceptcondition());
                    }
                    if (StringUtil.isNotBlank(delegate.getLink_tel())) {
                        audittask.setLink_tel(delegate.getLink_tel());
                    }
                    if (StringUtil.isNotBlank(delegate.getSupervise_tel())) {
                        audittask.setSupervise_tel(delegate.getSupervise_tel());
                    }
                    if (StringUtil.isNotBlank(delegate.getApplyaddress())) {
                        audittask.setTransact_addr(delegate.getApplyaddress());
                    }
                    if (StringUtil.isNotBlank(delegate.getApplytime())) {
                        audittask.setTransact_time(delegate.getApplytime());
                    }
                }
            }
        }
        // 受理通知书实体
        auditprintdocshouli = iAuditPrintdocShouli.getDocShouliByProjectGuid(projectguid).getResult();
        if (auditprintdocshouli == null) {
            auditprintdocshouli = new AuditPrintdocShouli();
            auditprintdocshouli.setOperatedate(new Date());
            auditprintdocshouli.setOperateusername(userSession.getDisplayName());
        }
        if (auditproject != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditproject.getIf_express())) {
            addCallbackParam("chk", "1");

            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if ("1".equals(isJSTY)) {
                Map<String, String> conditionMap = new HashMap<String, String>(16);
                conditionMap.put("projectguid = ", auditproject.getRowguid());
                List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                        .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
                if (list != null && !list.isEmpty()) {
                    for (AuditLogisticsBasicinfo logisticsBasicinfo : list) {
                        if (!"1".equals(logisticsBasicinfo.getNet_type())) {
                            auditLogisticsBasicInfo = logisticsBasicinfo;
                        }
                    }
                }
            } else {
                auditLogisticsBasicInfo = iAuditLogisticsBasicInfo.getAuditLogisticsBasicinfoListByFlowsn(projectguid)
                        .getResult();
            }
            if (auditLogisticsBasicInfo != null) {
                chkcode = auditLogisticsBasicInfo.getChk_code();
            }
        } else {
            addCallbackParam("chk", "0");
        }
        if (!isPostback()) {
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            // 日期
            dyrq = EpointDateUtil.convertDate2String(
                    (auditprintdocshouli.getDyrq() == null ? auditproject.getAcceptuserdate() : auditprintdocshouli.getDyrq()),
                    "yyyy年MM月dd日");
            operatedate = EpointDateUtil.convertDate2String(
                    (auditprintdocshouli.getOperatedate() == null ? new Date() : auditprintdocshouli.getOperatedate()),
                    "yyyy年MM月dd日");
            dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            // 获取方式信息
            lxr = StringUtil.isBlank(auditprintdocshouli.getLxr()) ? userSession.getDisplayName()
                    : auditprintdocshouli.getLxr();
            lxfsinfo = "联系人：" + lxr;
            lxfsinfo += "&nbsp;&nbsp;&nbsp;联系电话："
                    + (StringUtil.isBlank(audittask.getLink_tel()) ? "" : audittask.getLink_tel())
                    + "&nbsp;&nbsp;&nbsp;";
            lxfsinfo += "监督电话："
                    + (StringUtil.isBlank(audittask.getSupervise_tel()) ? "" : audittask.getSupervise_tel());
            // 受理依据
            if (StringUtil.isNotBlank(audittask.getAcceptcondition())) {
                laws = StringUtil.isBlank(auditprintdocshouli.getLaws()) ? audittask.getAcceptcondition()
                        : auditprintdocshouli.getLaws();
            } else {
                laws = StringUtil.isBlank(auditprintdocshouli.getLaws()) ? "《中华人民共和国行政许可法》"
                        : auditprintdocshouli.getLaws();
            }
            // 受理时间
            if (StringUtil.isBlank(auditprintdocshouli.getRowguid())) {
                Date date = new Date();
                if (StringUtil.isNotBlank(auditproject.getReceivedate())) {
                    date = auditproject.getReceivedate();
                }
                Date ShouldEndDate = auditWorkingDay
                        .getWorkingDayWithOfficeSet(ZwfwUserSession.getInstance().getCenterGuid(), date,
                                audittask.getPromise_day())
                        .getResult();
                String datastring = "";
                if ("1753-01-01".equals(EpointDateUtil.convertDate2String(ShouldEndDate, "yyyy-MM-dd"))) {
                    datastring = "结束日期已超过配置的最大工作日，请联系管理员配置工作日！";
                } else {
                    datastring = "请于" + EpointDateUtil.convertDate2String(ShouldEndDate, "yyyy年MM月dd日") + "凭本单来取件或缴费。";
                }
                timeinfo = audittask.getPromise_day() == null ? "0"
                        : audittask.getPromise_day() + "个工作日内办结，" + datastring;
            } else {
                timeinfo = auditprintdocshouli.getTimeinfo();
            }

            // 办件事项
            sbsx = StringUtil.isBlank(auditprintdocshouli.getTaskname()) ? auditproject.getProjectname()
                    : auditprintdocshouli.getTaskname();
            // 获取时间年份 ZJG201405140002 2014
            // 办件编号的前缀 ZJG
            String AS_FLOWSN_PRE = handleConfigService
                    .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String fyear = AS_FLOWSN_PRE + year; // ZJG2014
            // 文号 〔2015〕2号
            String orgwn = auditproject.getOrgwn();
            if (StringUtil.isBlank(orgwn)) {
                // 获取新文书号
                // TODO 暂缓
                int newbhinfo = auditProjectService
                        .getMaxOrgNumberinfo(null, projectguid, areacode, auditproject.getTask_id()).getResult();
                txtbh = String.valueOf(newbhinfo);
                String orgwnnew = "〔" + year + "〕" + txtbh + "号";
                auditproject.setOrgnumber(Integer.parseInt(txtbh));
                auditproject.setOrgwn(orgwnnew);
                auditProjectService.updateProject(auditproject);
            }

            // 获取更新文书号后办件信息

            // 是文书上的“补、受”字
            // TOOD
            // 获取更新文书号后办件信息
            String wninfo = auditproject.getOrgwn();// 〔2015〕2号
            String whnumber = wninfo.replace("〕", "〕第");// 〔2015〕第2号
            int numberbh = whnumber.indexOf('第') + 1;
            String numberwn = whnumber.substring(numberbh);// 2号
            int dwz = numberwn.indexOf('号');
            String numberwh = numberwn.substring(0, dwz);// 2
            wn = whnumber.substring(0, numberbh);// 〔2015〕第
            // 办号
            txtbh = StringUtil.isBlank(auditprintdocshouli.getRowguid()) ? numberwh : auditprintdocshouli.getBh();
            // 无情形
            String material = "";
            List<Record> projectMaterial = materialservice
                    .getProjectMaterial(auditproject.getRowguid(), auditproject.getBiguid()).getResult();
            if (projectMaterial != null) {
                projectMaterial.sort((c,
                                      d) -> (StringUtil.isNotBlank(getMaterialOrderNum(d.get("Taskmaterialguid"))) ? getMaterialOrderNum(d.get("Taskmaterialguid"))
                        : Integer.valueOf(0))
                        .compareTo(StringUtil.isNotBlank(getMaterialOrderNum(c.get("Taskmaterialguid")))
                                ? getMaterialOrderNum(c.get("Taskmaterialguid")) : 0));
            }
            if (projectMaterial != null && !projectMaterial.isEmpty()) {
                for (int i = 0; i < projectMaterial.size(); i++) {
                    int a = i + 1;
                    material += "&nbsp;&nbsp;&nbsp;&nbsp;" + a + "、" + projectMaterial.get(i).get("MATERIALNAME");
                    material += "；" + "<br>";
                }
            }
            // TODO
            txtmaterial = material;
            // TODO 暂缓
            // AuditProjectOperation auditprojectoperation =
            // auditprojectoperationservice
            // .selectOperationByProjectGuidAndType(projectguid,
            // ZwfwConstant.BANJIAN_STATUS_YSL);
            // if (auditprojectoperation != null) {
            // updateSave();
            // }
            //打印日期改为受理时间
            if (StringUtil.isNotBlank(auditproject.getAcceptuserdate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getAcceptuserdate(), "yyyy年MM月dd日");
            }
            addViewData("dyrq", dyrq);// 打印日期
            addViewData("operatedate", operatedate);
        } else {
            lxr = getViewData("lxr");
            operatedate = getViewData("operatedate");
            if (StringUtil.isNotBlank(auditproject.getAcceptuserdate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getAcceptuserdate(), "yyyy年MM月dd日");
            }
            else{
                dyrq = getViewData("dyrq");
            }
        }

        String result = "";
        result = handleConfigService.getFrameConfig("AS_FRONT_FUWUKA", ZwfwUserSession.getInstance().getCenterGuid())
                .getResult();
        this.addCallbackParam("msg", result);
        this.addCallbackParam("sdaddress", auditprintdocshouli.getSdaddress());
        this.addCallbackParam("sdkind", auditprintdocshouli.getSdkind());
    }

    /**
     * 获取材料排序
     * [一句话功能简述]
     * [功能详细描述]
     *
     * @param Taskmaterialguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getMaterialOrderNum(String Taskmaterialguid) {
        AuditTaskMaterial taskMaterial = auditTaskMaterial.getAuditTaskMaterialByRowguid(Taskmaterialguid).getResult();
        return taskMaterial.getOrdernum();
    }

    /**
     * 保存文书信息
     */
    protected void updateSave() {
        String wninfo = auditproject.getOrgwn();
        int n = wninfo.indexOf('〔');
        String wnumber = wninfo.substring(n);
        int numberbh = wnumber.indexOf('〕') + 1;
        String orgwnnew;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";
//        String str = dyrq.replace("年", "-");
//        str = str.replace("月", "-");
//        str = str.replace("日", "");
//        operatedate = operatedate.replace("年", "-");
//        operatedate = operatedate.replace("月", "-");
//        operatedate = operatedate.replace("日", "");
        // 根据ProjectGuid判断是否存在不予批准决定书
        if (StringUtil.isNotBlank(auditprintdocshouli.getRowguid())) {
            auditprintdocshouli.setLaws(laws);
            auditprintdocshouli.setTaskname(sbsx);
            auditprintdocshouli.setTimeinfo(timeinfo);
            auditprintdocshouli.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocshouli.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocshouli.setLxr(lxr);
            auditprintdocshouli.setBh(txtbh);
            iAuditPrintdocShouli.update(auditprintdocshouli);
        } else {
            auditprintdocshouli.setRowguid(UUID.randomUUID().toString());
            auditprintdocshouli.setProjectguid(projectguid);
            auditprintdocshouli.setTaskname(sbsx);
            auditprintdocshouli.setTimeinfo(timeinfo);
            auditprintdocshouli.setLaws(laws);
            auditprintdocshouli.setMaterials(txtmaterial);
            auditprintdocshouli.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy-MM-dd"));
            auditprintdocshouli.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocshouli.setLxr(lxr);
            auditprintdocshouli.setBh(txtbh);
            iAuditPrintdocShouli.add(auditprintdocshouli);
        }
        // auditproject.setOrgnumber(Integer.parseInt(txtbh));
        auditproject.setOrgwn(orgwnnew);
        auditProjectService.updateProject(auditproject);
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "pauditprintdocshouli?projectguid=" + projectguid
                + "&taskguid=" + audittask.getRowguid();
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

    public AuditPrintdocShouli getAuditprintdocshouli() {
        return auditprintdocshouli;
    }

    public void setAuditprintdocshouli(AuditPrintdocShouli auditprintdocshouli) {
        this.auditprintdocshouli = auditprintdocshouli;
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

    public String getTxtmaterial() {
        return txtmaterial;
    }

    public void setTxtmaterial(String txtmaterial) {
        this.txtmaterial = txtmaterial;
    }

    public String getWn() {
        return wn;
    }

    public void setWn(String wn) {
        this.wn = wn;
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

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getChkcode() {
        return chkcode;
    }

    public void setChkcode(String chkcode) {
        this.chkcode = chkcode;
    }

    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }
}
