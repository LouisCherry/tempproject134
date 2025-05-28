package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocZhunxuke;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocZhunxuke;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
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
 * 准予许可决定书预览页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-03 10:53:38]
 */
@RestController("auditprintdoczhunxukeaction")
@Scope("request")
public class AuditPrintdocZhunxukeAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -8043193340001239225L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = "*";
    /**
     * service
     */
    @Autowired
    private IAuditPrintdocZhunxuke iAuditPrintdocZhunxuke;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 通用通知书service
     */
    @Autowired
    private IAuditProject auditProjectService;

    /**
     * 办件操作service
     */
    //    private AuditProjectOperationService auditprojectoperationservice = new AuditProjectOperationService();

    /**
     * 办件实体
     */
    private AuditProject auditproject = null;

    /**
     * 事项实体
     */
    private AuditTask audittask = null;

    /**
     * 准予许可决定书实体对象
     */
    private AuditPrintdocZhunxuke auditprintdocZhunxuke = null;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 流程版本实例guid
     */
    private String processVersionInstanceGuid;

    /**
     * 事项类别
     */
    private String tasktype;

    /**
     * 打印时间
     */

    private String dyrq;

    private String dyrq1;

    /**
     * 准予类型
     */
    private String ddlGranttype;

    /**
     * 文书类别
     */
    private String mode;

    private String mode1;

    /**
     * 受理依据
     */
    private String laws;

    /**
     * 办件事项
     */
    private String sbsx;

    /**
     * 准予类型名称
     */
    private String granttype;

    private String granttype1;

    /**
     * 审批类别
     */
    private String tasktypeExtension;

    /**
     * 发送文书日期间隔
     */
    private String days;

    /**
     * 联系人
     */
    private String txtlxr;

    /**
     * 联系电话
     */
    private String txtlxdh;

    /**
     * 办号
     */
    private String txtbh = "";

    /**
     * 文号
     */
    private String wn;

    private String dateend;

    private String address = "";

    /**
     * ddlGranttypeModel下拉列表modal
     */
    private List<SelectItem> ddlGranttypeModel = null;

    /**
     * sdkind下拉列表modal
     */
    private List<SelectItem> sdkindModal = null;

    /**
     * sdaddress下拉列表modal
     */
    private List<SelectItem> sdaddressModal = null;

    @Autowired
    private IHandleConfig handleConfigService;

    private String area = "";

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    /**
     * 打印时间
     */

    private String operatedate;

    @SuppressWarnings("unused")
    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        //processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");

        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        // 办件信息
        auditproject = auditProjectService
                .getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        // 事项信息
        if (auditproject != null) {
            audittask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        }
        //乡镇延伸个性化情况
        if (audittask != null) {
            if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
                String areacode = "";
                if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
                    areacode = auditproject.getAcceptareacode();
                } else {
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                AuditTaskDelegate delegate = auditTaskDelegateService
                        .findByTaskIDAndAreacode(audittask.getTask_id(), areacode).getResult();
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
        }
        // 准予许可决定书实体
        auditprintdocZhunxuke = iAuditPrintdocZhunxuke.getDocZhunxukeByProjectGuid(projectguid).getResult();
        if (auditprintdocZhunxuke == null) {
            auditprintdocZhunxuke = new AuditPrintdocZhunxuke();
            auditprintdocZhunxuke.setOperatedate(new Date());
            auditprintdocZhunxuke.setOperateusername(userSession.getDisplayName());
        }
        if (!isPostback()) {
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            // 日期
            dyrq = EpointDateUtil.convertDate2String(
                    auditprintdocZhunxuke.getDyrq() == null ? auditproject.getBanjiedate() : auditprintdocZhunxuke.getDyrq(),
                    "yyyy年MM月dd日");

            operatedate = EpointDateUtil.convertDate2String(
                    (auditprintdocZhunxuke.getOperatedate() == null ? new Date() : auditprintdocZhunxuke.getOperatedate()),
                    "yyyy年MM月dd日");

            dyrq1 = EpointDateUtil.convertDate2String(
                    auditproject.getApplydate() == null ? new Date() : auditproject.getApplydate(), "yyyy年MM月dd日");
            // 准许类型
            ddlGranttype = StringUtil.isBlank(auditprintdocZhunxuke.getItemvalueforxk()) ? "0"
                    : auditprintdocZhunxuke.getItemvalueforxk();

            mode = "决定";

            // 准予许可依据
            laws = StringUtil.isBlank(auditprintdocZhunxuke.getLaws()) ? "《中华人民共和国行政许可法》"
                    : auditprintdocZhunxuke.getLaws();

            // 办件名称
            sbsx = StringUtil.isBlank(auditprintdocZhunxuke.getTaskname()) ? auditproject.getProjectname()
                    : auditprintdocZhunxuke.getTaskname();

            // 准许类型为空
            if ("0".equals(ddlGranttype)) {
                mode1 = "决定";
                mode = "决定";
                granttype = "";
                granttype1 = "准予你（单位）行政";
                tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            }

            // 准许类型为变更、延期、备案
            if ("1".equals(ddlGranttype) || "2".equals(ddlGranttype) || "4".equals(ddlGranttype)) {
                mode1 = "决定";
                mode = "决定";
                granttype = MAP.get(ddlGranttype).toString();
                granttype1 = "准予你（单位）" + MAP.get(ddlGranttype).toString();
                tasktypeExtension = "";
            }

            // 准许类型为审核
            else if ("5".equals(ddlGranttype)) {
                //                lxfsdisplay = "display:block";
                //                fztimedisplay = "display:none";
                granttype = "上报";
                mode = "申请通知";
                mode1 = "通知";
                granttype1 = "将你（单位）的行政许可申请给予上报";
                tasktypeExtension = "";
            }

            // 准许类型为不显示日期
            else if ("7".equals(ddlGranttype)) {

                //                lxfsdisplay = "display:none";
                //                fztimedisplay = "display:none";
                mode1 = "决定";
                mode = "决定";
                granttype = "";
                granttype1 = "准予你（单位）行政";
                tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());

            }

            // 准许类型为内容核准
            else if ("6".equals(ddlGranttype)) {
                String addr = "epointzwfw/printdoc/common/Zyxkforhz.jspx?projectguid=" + projectguid;
                addCallbackParamEncode("message", addr);
            }

            tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());

            // 发证时间
            if (StringUtil.isBlank(auditprintdocZhunxuke.getRowguid())) {
                days = "10";
                txtlxr = userSession.getDisplayName();
                txtlxdh = audittask.getLink_tel();
            } else {
                days = auditprintdocZhunxuke.getDays();
                txtlxr = auditprintdocZhunxuke.getLxr();
                txtlxdh = auditprintdocZhunxuke.getLxdh();
            }

            // 获取时间年份 ZJG201405140002 2014
            // 办件编号的前缀 ZJG
            String AS_FLOWSN_PRE = handleConfigService
                    .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            // String year = "20" + auditproject.getFlowsn().substring(8, 10);
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
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
            StringBuffer orgnwninfoold;
            //            // 是文书上的“补、受”字
            //            if ("1".equals(audittask.getIssz())) {
            //                orgnwninfoold = new StringBuffer(auditproject.getOrgwn()).insert(orgnwn, "准字");
            //            }
            //            else {
            orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
            //           }
            String whnumber = orgnwninfoold.toString().replace("〕", "〕第");
            int numberbh = whnumber.indexOf('第') + 1;
            String numberwn = whnumber.substring(numberbh);
            int dwz = numberwn.indexOf('号');
            String numberwh = numberwn.substring(0, dwz);
            wn = whnumber.substring(0, numberbh);
            // 办号
            txtbh = StringUtil.isBlank(auditprintdocZhunxuke.getBh()) ? numberwh : auditprintdocZhunxuke.getBh();
            if (StringUtil.isNotBlank(auditproject.getAcceptuserdate())) {
                dateend = EpointDateUtil.convertDate2String(auditproject.getAcceptuserdate(), "yyyy年MM月dd日");
            }
            //TODO 暂缓
            //            AuditProjectOperation auditprojectoperation = auditprojectoperationservice
            //                    .selectOperationByProjectGuidAndType(projectguid, Integer.parseInt(ZwfwConstant.SHENPIOPERATE_TYPE_ZYPZ));
            //            if (auditprojectoperation != null) {
            //                updateSave();
            //            }

            //取办结时间
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            }
            addViewData("operatedate", operatedate);
            addViewData("granttype", granttype);
            addViewData("dyrq", dyrq);
            addViewData("dateend", dateend);
            addViewData("dyrq1", dyrq1);
        } else {
            //取办结时间
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            } else {
                dyrq = getViewData("dyrq");
            }
            operatedate = getViewData("operatedate");
            mode1 = getViewData("mode1");
            mode = getViewData("mode");
            granttype = getViewData("granttype");
            granttype1 = getViewData("granttype1");
            tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别",
                    getViewData("tasktypeExtension"));
            dateend = getViewData("dateend");
            dyrq1 = getViewData("dyrq1");
        }
        this.addCallbackParam("sdaddress", auditprintdocZhunxuke.getSdaddress());
        this.addCallbackParam("sdkind", auditprintdocZhunxuke.getSdkind());
    }

    /**
     * 保存文书信息
     */
    public boolean updateSave() {
        String areacode = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        // 办件信息
        auditproject = auditProjectService
                .getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        // 事项信息
        audittask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        String wninfo = auditproject.getOrgwn();
        int n = wninfo.indexOf('〔');
        String wnumber = wninfo.substring(n);
        int numberbh = wnumber.indexOf('〕') + 1;
        String orgwnnew;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";
        String daysnew = days;
        String lawsinfo = auditprintdocZhunxuke.getRemarks();
        if (StringUtil.isNotBlank(lawsinfo)) {
            lawsinfo = lawsinfo.replace("\n", "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").replace(" ",
                    " ");
        }
        try {
            int daysnew_int = Integer.parseInt(daysnew);

            if (daysnew_int <= 10) {
                // 根据ProjectGuid判断是否存在不予批准决定书
                if (StringUtil.isNotBlank(auditprintdocZhunxuke.getRowguid())) {
                    auditprintdocZhunxuke.setLaws(laws);
                    auditprintdocZhunxuke.setTaskname(sbsx);
                    auditprintdocZhunxuke.setRemarks(lawsinfo);
                    auditprintdocZhunxuke.setGranttype(granttype);
                    auditprintdocZhunxuke.setItemvalueforxk(ddlGranttype);
                    auditprintdocZhunxuke.setDays(days);
                    auditprintdocZhunxuke.setLxr(txtlxr);
                    auditprintdocZhunxuke.setLxdh(txtlxdh);
                    auditprintdocZhunxuke.setBh(txtbh);
                    auditprintdocZhunxuke.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
                    auditprintdocZhunxuke.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
                    iAuditPrintdocZhunxuke.update(auditprintdocZhunxuke);
                } else {
                    auditprintdocZhunxuke.setRowguid(UUID.randomUUID().toString());
                    auditprintdocZhunxuke.setProjectguid(projectguid);
                    auditprintdocZhunxuke.setTaskname(sbsx);
                    auditprintdocZhunxuke.setLaws(laws);
                    auditprintdocZhunxuke.setRemarks(lawsinfo);
                    auditprintdocZhunxuke.setGranttype(granttype);
                    auditprintdocZhunxuke.setItemvalueforxk(ddlGranttype);
                    auditprintdocZhunxuke.setDays(days);
                    auditprintdocZhunxuke.setLxr(txtlxr);
                    auditprintdocZhunxuke.setLxdh(txtlxdh);
                    auditprintdocZhunxuke.setBh(txtbh);
                    auditprintdocZhunxuke.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
                    auditprintdocZhunxuke.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
                    iAuditPrintdocZhunxuke.add(auditprintdocZhunxuke);
                }
                auditproject.setOrgnumber(Integer.parseInt(txtbh));
                auditproject.setOrgwn(orgwnnew);
                auditProjectService.updateProject(auditproject);
                return true;
            } else {
                addCallbackParam("msg", "时间超过10天，请重新输入!");
                return false;
            }
        } catch (Exception e) {
            addCallbackParam("msg", "请重新输入数字!");
            return false;
        }
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        boolean ischeck = updateSave();
        if (ischeck) {
            String printaddress = "pauditprintdoczhunxuke?projectguid=" + projectguid + "&ProcessVersionInstanceGuid="
                    + processVersionInstanceGuid + "&itemvalue=" + ddlGranttype + "&taskguid=" + audittask.getRowguid();
            addCallbackParam("msg", printaddress);
        }
    }

    /**
     * 准予类型变化事件
     */
    public void ddlGranttypeChange(String granttypevalue) {
        AuditProject auditProject = auditProjectService
                .getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
        // 准许类型为变更、延期、备案
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(granttypevalue) || ZwfwConstant.CONSTANT_STR_TWO.equals(granttypevalue) || "4".equals(granttypevalue)) {
            //            lxfsdisplay = "display:none";
            //            fztimedisplay = "display:block";
            mode1 = "决定";
            mode = "决定";
            granttype = MAP.get(granttypevalue).toString();
            granttype1 = "准予你（单位）" + MAP.get(granttypevalue).toString();
            tasktypeExtension = "";
        }

        // 准许类型为审核
        else if ("5".equals(granttypevalue)) {
            //            lxfsdisplay = "display:block";
            //            fztimedisplay = "display:none";
            granttype = "上报";
            mode = "申请通知";
            mode1 = "通知";
            granttype1 = "将你（单位）的行政许可申请给予上报";
            tasktypeExtension = "";
        }

        // 准许类型为不显示日期// 准许类型为空
        else if ("7".equals(granttypevalue) || "0".equals(granttypevalue)) {
            //            lxfsdisplay = "display:none";
            //            fztimedisplay = "display:none";
            mode1 = "决定";
            mode = "决定";
            granttype = "";
            granttype1 = "准予你（单位）行政";
            tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", auditTask.getShenpilb());

        }

        // 准许类型为内容核准
        else if ("6".equals(granttypevalue)) {
            address = "epointzwfw/printdoc/common/Zyxkforhz.jspx?projectguid=" + projectguid;
            // JsfHelper.addCallbackParamEncode("message", address);
        }

        addViewData("mode1", mode1);
        addViewData("mode", mode);
        addViewData("granttype", granttype);
        addViewData("granttype1", granttype1);
        addViewData("tasktypeExtension", tasktypeExtension);

    }

    @SuppressWarnings({"unchecked", "rawtypes", "serial"})
    public final static Map MAP = new HashMap() {
        {
            put("0", "空");
            put("1", "变更");
            put("2", "延期");
            put("4", "备案");
            put("5", "审核");
            put("6", "内容核准");
            put("7", "不显示日期");
        }
    };

    @SuppressWarnings("unchecked")
    public List<SelectItem> getDdlGranttypeModel() {
        if (ddlGranttypeModel == null) {
            ddlGranttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "许可文书类型", null, false));
        }
        return this.ddlGranttypeModel;
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

    public AuditPrintdocZhunxuke getAuditprintdocZhunxuke() {
        return auditprintdocZhunxuke;
    }

    public void setAuditprintdocZhunxuke(AuditPrintdocZhunxuke auditprintdocZhunxuke) {
        this.auditprintdocZhunxuke = auditprintdocZhunxuke;
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

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
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

    public String getDdlGranttype() {
        return ddlGranttype;
    }

    public void setDdlGranttype(String ddlGranttype) {
        this.ddlGranttype = ddlGranttype;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode1() {
        return mode1;
    }

    public void setMode1(String mode1) {
        this.mode1 = mode1;
    }

    public String getLaws() {
        return laws;
    }

    public void setLaws(String laws) {
        this.laws = laws;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
    }

    public String getGranttype() {
        return granttype;
    }

    public void setGranttype(String granttype) {
        this.granttype = granttype;
    }

    public String getGranttype1() {
        return granttype1;
    }

    public void setGranttype1(String granttype1) {
        this.granttype1 = granttype1;
    }

    public String getTasktypeExtension() {
        return tasktypeExtension;
    }

    public void setTasktypeExtension(String tasktypeExtension) {
        this.tasktypeExtension = tasktypeExtension;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTxtlxr() {
        return txtlxr;
    }

    public void setTxtlxr(String txtlxr) {
        this.txtlxr = txtlxr;
    }

    public String getTxtlxdh() {
        return txtlxdh;
    }

    public void setTxtlxdh(String txtlxdh) {
        this.txtlxdh = txtlxdh;
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

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }

}
