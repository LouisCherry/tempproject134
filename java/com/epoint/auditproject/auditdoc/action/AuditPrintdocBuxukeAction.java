package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBuxuke;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBuxuke;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
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
 * 不予许可决定书预览页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-08 10:10:52]
 */
@RestController("auditprintdocbuxukeaction")
@Scope("request")
public class AuditPrintdocBuxukeAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 772524245512206304L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = "*";
    /**
     * 不予许可决定书实体对象
     */
    private AuditPrintdocBuxuke auditprintdocBuxuke = null;
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 办件实体
     */
    private AuditProject auditproject = null;

    /**
     * 事项实体
     */
    private AuditTask audittask = null;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 办件guid
     */
    private String taskGuid;


    /**
     * 受理依据
     */
    private String laws;

    /**
     * 不予类型
     */
    private String ddlGranttype;

    /**
     * 不予类型名称
     */
    private String granttype;
    private String granttype1;
    /**
     * 办号
     */
    private String txtbh = "";
    /**
     * 文号
     */
    private String wn;
    /**
     * 申请日期
     */
    private String dyrq1;
    private String dyrq;
    /**
     * 办件名称
     */
    private String sbsx;

    /**
     * 审批类别
     */
    private String tasktypeExtension;

    /**
     * 事项类别
     */
    private String tasktype = "";

    /**
     * 联系人
     */
    private String lxr;

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
    IAuditProject auditProjectService;

    @Autowired
    IAuditTask auditTaskService;

    @Autowired
    IAuditPrintdocBuxuke printdocBuxukeService;

    @Autowired
    IAuditProjectNotify projectNotifyService;
    String area = "";

    /**
     * 打印时间
     */

    private String operatedate;

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
                .getAuditProjectByRowGuid(FIELDS, projectguid, "")
                .getResult();

        audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
        taskGuid = audittask.getRowguid();
        // processVersionInstanceGuid =
        // getRequestParameter("ProcessVersionInstanceGuid");
        // 事项信息
        audittask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
        // bu予许可决定书实体
        auditprintdocBuxuke = printdocBuxukeService.getDocBuxukeByProjectGuid(projectguid).getResult();

        if (auditprintdocBuxuke == null) {
            auditprintdocBuxuke = new AuditPrintdocBuxuke();
            auditprintdocBuxuke.setOperatedate(new Date());
            auditprintdocBuxuke.setOperateusername(userSession.getDisplayName());
        }
        if (!isPostback()) {
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            auditprintdocBuxuke.setOperatedate(new Date());
            auditprintdocBuxuke.setOperateusername(userSession.getDisplayName());

            // 不予许可依据
            laws = StringUtil.isBlank(auditprintdocBuxuke.getLaws()) ? "《中华人民共和国行政许可法》" : auditprintdocBuxuke.getLaws();

            // 不许类型
            ddlGranttype = StringUtil.isBlank(auditprintdocBuxuke.getItemvalueforbxk()) ? "0"
                    : auditprintdocBuxuke.getItemvalueforbxk();

            // 不许类型为空
            if ("0".equals(ddlGranttype)) {
                granttype = "";
                granttype1 = "不予你（单位）行政";
                tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            }

            // 不许类型为变更、延期、备案
            if ("1".equals(ddlGranttype) || "2".equals(ddlGranttype) || "4".equals(ddlGranttype)) {
                granttype = MAP.get(ddlGranttype).toString();
                granttype1 = "不予你（单位）" + MAP.get(ddlGranttype).toString();
                tasktypeExtension = "";
            }

            // 不许类型为审核
            else if ("5".equals(ddlGranttype)) {
                granttype = "上报";
                granttype1 = "将你（单位）的行政许可申请不予上报";
                tasktypeExtension = "";
            }

            // 不许类型为不显示日期// 不许类型为内容核准
            else if ("7".equals(ddlGranttype) || "6".equals(ddlGranttype)) {
                granttype = "";
                granttype1 = "不予你（单位）行政";
                tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            }

            // 不许类型
            sbsx = StringUtil.isBlank(auditprintdocBuxuke.getTaskname()) ? auditproject.getProjectname()
                    : auditprintdocBuxuke.getTaskname();

            // 获取时间年份 ZJG201405140002 2014
            // 办件编号的前缀 ZJG
            // String AS_FLOWSN_PRE = handleConfigService.getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            // String year = "20" + auditproject.getFlowsn().substring(8, 10);
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            // 文号 〔2014〕1号
            String orgwn = auditproject.getOrgwn();
            if (StringUtil.isBlank(orgwn)) {
                // 获取新文书号
                //TODO 暂缓
                int newbhinfo = auditProjectService.getMaxOrgNumberinfo(null, projectguid,
                        "", auditproject.getTask_id()).getResult();
                txtbh = String.valueOf(newbhinfo);
                String orgwnnew = "〔" + year + "〕" + txtbh + "号";
                auditproject.setOrgnumber(Integer.parseInt(txtbh));
                auditproject.setOrgwn(orgwnnew);
                auditProjectService.updateProject(auditproject);
            }

            // 获取更新文书号后办件信息
            StringBuffer orgnwninfoold;
            orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
            String whnumber = orgnwninfoold.toString().replace("〕", "〕第");
            int numberbh = whnumber.indexOf('第') + 1;
            String numberwn = whnumber.substring(numberbh);
            int dwz = numberwn.indexOf('号');
            String numberwh = numberwn.substring(0, dwz);
            wn = whnumber.substring(0, numberbh);
            // 办号
            txtbh = StringUtil.isBlank(auditprintdocBuxuke.getBh()) ? numberwh : auditprintdocBuxuke.getBh();
            lxr = StringUtil.isBlank(auditprintdocBuxuke.getLxr()) ? userSession.getDisplayName()
                    : auditprintdocBuxuke.getLxr();
            // 日期
            dyrq = EpointDateUtil.convertDate2String(
                    auditprintdocBuxuke.getDyrq() == null ? auditproject.getBanjiedate() : auditprintdocBuxuke.getDyrq(), "yyyy年MM月dd日");
            operatedate = EpointDateUtil.convertDate2String(
                    (auditprintdocBuxuke.getOperatedate() == null ? new Date() : auditprintdocBuxuke.getOperatedate()),
                    "yyyy年MM月dd日");
            dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");

            //取办结时间
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            }

            addViewData("dyrq", dyrq);// 打印日期
            addViewData("lxr", lxr);// 联系人
            addViewData("granttype", granttype);// 不予受理名称
            addViewData("granttype1", granttype1);// 不予受理名称
            addViewData("tasktypeExtension", tasktypeExtension);
            addViewData("operatedate", operatedate);
        } else {
            operatedate = getViewData("operatedate");
            //取办结时间
            if (StringUtil.isNotBlank(auditproject.getBanjiedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditproject.getBanjiedate(), "yyyy年MM月dd日");
            } else {
                dyrq = getViewData("dyrq");
            }

            lxr = getViewData("lxr");
            granttype = getViewData("granttype");
            granttype1 = getViewData("granttype1");
            tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别",
                    getViewData("tasktypeExtension"));
        }
        this.addCallbackParam("sdaddress", auditprintdocBuxuke.getSdaddress());
        this.addCallbackParam("sdkind", auditprintdocBuxuke.getSdkind());
    }

    /**
     * 保存文书信息
     */
    public void updateSave() {
        String wninfo = auditproject.getOrgwn();
        int n = wninfo.indexOf('〔');
        String wnumber = wninfo.substring(n);
        int numberbh = wnumber.indexOf('〕') + 1;
        String orgwnnew;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";
        String lawsinfo = auditprintdocBuxuke.getOpinion();
        if (StringUtil.isNotBlank(lawsinfo)) {
            lawsinfo = lawsinfo.replace("\n", "</br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").replace(" ",
                    " ");
        }

        // 根据ProjectGuid判断是否存在不予许可决定书
        if (StringUtil.isNotBlank(auditprintdocBuxuke.getRowguid())) {
            auditprintdocBuxuke.setLaws(laws);
            auditprintdocBuxuke.setOpinion(lawsinfo);
            auditprintdocBuxuke.setTaskname(sbsx);
            auditprintdocBuxuke.setGranttype(granttype);
            auditprintdocBuxuke.setItemvalueforbxk(ddlGranttype);
            auditprintdocBuxuke.setLxr(lxr);
            auditprintdocBuxuke.setBh(txtbh);
            auditprintdocBuxuke.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocBuxuke.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            printdocBuxukeService.updateDocBuxuke(auditprintdocBuxuke);
        } else {
            auditprintdocBuxuke.setRowguid(UUID.randomUUID().toString());
            auditprintdocBuxuke.setProjectguid(projectguid);
            auditprintdocBuxuke.setTaskname(sbsx);
            auditprintdocBuxuke.setOpinion(lawsinfo);
            auditprintdocBuxuke.setLaws(laws);
            auditprintdocBuxuke.setGranttype(granttype);
            auditprintdocBuxuke.setItemvalueforbxk(ddlGranttype);
            auditprintdocBuxuke.setLxr(lxr);
            auditprintdocBuxuke.setBh(txtbh);
            auditprintdocBuxuke.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocBuxuke.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            printdocBuxukeService.addDocBuxuke(auditprintdocBuxuke);
        }
        auditproject.setOrgnumber(Integer.parseInt(txtbh));
        auditproject.setOrgwn(orgwnnew);
        auditProjectService.updateProject(auditproject);

        // 插入在线通知
        String notirytitle = "【不予许可】" + "<" + auditproject.getProjectname() + ">";
        String handurl = ""; // 处理页面地址 TODO
        projectNotifyService.addProjectNotify(auditproject.getApplyeruserguid(), auditproject.getApplyername(),
                notirytitle,
                StringUtil.isBlank(auditprintdocBuxuke.getOpinion()) ? "" : auditprintdocBuxuke.getOpinion(), handurl,
                ZwfwConstant.CLIENTTYPE_BJ, auditproject.getRowguid(), String.valueOf(auditproject.getStatus()),
                userSession.getUserGuid(), userSession.getDisplayName());
    }

    /**
     * 不许类型变化事件
     */
    public void ddlGranttypeChange(String granttypevalue) {
        // 不许类型为变更、延期、备案
        if ("1".equals(granttypevalue) || "2".equals(granttypevalue) || "4".equals(granttypevalue)) {
            granttype = MAP.get(granttypevalue).toString();
            granttype1 = "不予你（单位）" + MAP.get(granttypevalue).toString();
            tasktypeExtension = "";
        }

        // 不许类型为审核
        else if ("5".equals(granttypevalue)) {
            granttype = "上报";
            granttype1 = "将你（单位）的行政许可申请不予上报";
            tasktypeExtension = "";
        }

        // 不许类型为不显示日期  不许类型为空 不许类型为内容核准
        else if ("7".equals(granttypevalue) || "0".equals(granttypevalue) || "6".equals(granttypevalue)) {

            granttype = "";
            granttype1 = "不予你（单位）行政";
            tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());

        }

        addViewData("granttype", granttype);
        addViewData("granttype1", granttype1);
        addViewData("tasktypeExtension", tasktypeExtension);
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "pauditprintdocbuxuke?projectguid=" + projectguid
                + "&itemvalue=" + ddlGranttype + "&taskguid=" + audittask.getRowguid();
        addCallbackParam("msg", printaddress);

    }

    @SuppressWarnings({"unchecked", "rawtypes", "serial"})
    public final static Map MAP = new HashMap() {
        {
            put("0", "空");
            put("1", "变更");
            put("2", "延期");
            put("4", "备案");
            put("5", "审核");
            put("6", "内容核不");
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

    public AuditPrintdocBuxuke getAuditprintdocBuxuke() {
        return auditprintdocBuxuke;
    }

    public void setAuditprintdocBuxuke(AuditPrintdocBuxuke auditprintdocBuxuke) {
        this.auditprintdocBuxuke = auditprintdocBuxuke;
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

    public String getLaws() {
        return laws;
    }

    public void setLaws(String laws) {
        this.laws = laws;
    }

    public String getDdlGranttype() {
        return ddlGranttype;
    }

    public void setDdlGranttype(String ddlGranttype) {
        this.ddlGranttype = ddlGranttype;
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

    public String getDyrq1() {
        return dyrq1;
    }

    public void setDyrq1(String dyrq1) {
        this.dyrq1 = dyrq1;
    }

    public String getDyrq() {
        return dyrq;
    }

    public void setDyrq(String dyrq) {
        this.dyrq = dyrq;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
    }

    public String getTasktypeExtension() {
        return tasktypeExtension;
    }

    public void setTasktypeExtension(String tasktypeExtension) {
        this.tasktypeExtension = tasktypeExtension;
    }

    public String getLxr() {
        return lxr;
    }

    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }

}
