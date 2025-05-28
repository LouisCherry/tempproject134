package com.epoint.auditproject.auditdoc.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShenqing;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocShenqing;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 申请通知书打印页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-10-26 14:14:32]
 */
@RestController("auditprintdocpshenqingaction")
@Scope("request")
public class AuditPrintdocPShenqingAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -8764467453058944827L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,taskguid,projectname,applydate,orgwn,flowsn,status,ouguid,applyername,contactphone,contactperson,contactmobile,address,contactemail,contactpostcode ";
    /**
     * service
     */
    @Autowired
    private IAuditPrintdocShenqing iAuditPrintdocShenqing;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IOuService iOUService;

    @Autowired
    IAttachService attachService;
    /**
     * 文书快照service
     */
    @Autowired
    private IAuditProjectDocSnap auditProjectDocsnapService;

    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 申请通知书实体对象
     */
    private AuditPrintdocShenqing auditprintdocShenqing = null;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 流程版本实例guid
     */
    private String processVersionInstanceGuid;

    /**
     * 办件实体bean
     */
    private AuditProject auditproject = null;

    /**
     * 事项实体bean
     */
    private AuditTask audittask = null;

    /**
     * 打印日期
     */
    private String dyrq;

    /**
     * 法定代表人(负责人)
     */
    private String legalrepresentative;

    /**
     * 部门名称
     */
    private String govname;

    private String govname1;

    /**
     * 身份证号码
     */
    private String IDNo;

    /**
     * 办理事项名称
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
     * 申请通知书打印html
     */
    private String divhtml;

    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        AuditProject project = auditProjectService.getAuditProjectByRowGuid("applyertype", projectguid, "").getResult();
        if (project != null) {
            if (ZwfwConstant.APPLAYERTYPE_QY.equals(project.getApplyertype().toString())) {
                addCallbackParam("cardtype", "联系人身份证");
            }
        }
        refreshinfo();
    }

    /**
     * 更新申请信息
     */
    public void refreshinfo() {
        // 申请文书信息
        auditprintdocShenqing = iAuditPrintdocShenqing.getDocShenqingByProjectGuid(projectguid).getResult();
        if (auditprintdocShenqing != null) {
            // 办件信息
            auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
            if (auditproject != null) {
                // 事项信息
                audittask = iAuditTask.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
                tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
                txtbh = auditprintdocShenqing.getBh();
                // 身份证号
                IDNo = StringUtil.isBlank(auditprintdocShenqing.getRowguid())
                        ? (ZwfwConstant.CERT_TYPE_SFZ.equals(auditproject.getCerttype()) ? auditproject.getCertnum() : null)
                        : auditprintdocShenqing.getIdno();
                FrameOu ou = iOUService.getOuByOuGuid(auditproject.getOuguid());
                String oushortname = "";
                if (ou != null) {
                    oushortname = ou.getOuname();
                }
                if (oushortname != null && !"".equals(oushortname)) {
                    govname = oushortname;
                    govname1 = oushortname;
                } else {
                    govname = audittask.getOuname();
                    govname1 = audittask.getOuname();
                }
                sbsx = auditprintdocShenqing.getTaskname();
                String wnninfo = auditproject.getOrgwn();
                String whnumber = wnninfo.replace("〕", "〕第");
                int numberbh = whnumber.indexOf('第') + 1;
                wn = whnumber.substring(0, numberbh);
                // 日期
                dyrq = EpointDateUtil.convertDate2String(auditprintdocShenqing.getDyrq(), "yyyy年MM月dd日");
            }
        }
    }

    /**
     * 高拍仪上传
     */
    public void btnGPH() {
        String doctype = String.valueOf(ZwfwConstant.DOC_TYPE_SQTZ);
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SQTZ).getResult();
        if (list != null && !list.isEmpty()) {
            String GPHtmladress = "../../GaoPaiPages/GPHtml4LT2.jspx?projectguid=" + projectguid + "&doctype="
                    + doctype;
            addCallbackParam("msg", GPHtmladress);
        } else {
            addCallbackParam("msg", "请先打印文书再上传");
        }

    }

    /**
     * 查看文书图片
     */
    public void btnGPHPic() {
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SQTZ).getResult();
        if (list != null && !list.isEmpty() && StringUtil.isNotBlank(list.get(0).getDocattachguid())) {
            List<FrameAttachInfo> attachlist = attachService.getAttachInfoListByGuid(list.get(0).getDocattachguid());
            String GPHtmlPicaddress = "../../../Pages/AttachStorageInfo/ReadAttachFile.jspx?attachguid="
                    + attachlist.get(0).getAttachGuid();
            addCallbackParam("msg", GPHtmlPicaddress);
        } else {
            addCallbackParam("msg", "请先上传文书图片");
        }
    }

    /**
     * 打印申请通知书
     */
    public void btnprintwenshuClick(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SQTZ).getResult();
        if (StringUtil.isNotBlank(divhtml)) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getUserGuid());
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_SQTZ);
            auditProjectDocsnap.setTaskname(audittask.getTaskname());
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public AuditPrintdocShenqing getAuditprintdocShenqing() {
        return auditprintdocShenqing;
    }

    public void setAuditprintdocShenqing(AuditPrintdocShenqing auditprintdocShenqing) {
        this.auditprintdocShenqing = auditprintdocShenqing;
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

    public String getGovname() {
        return govname;
    }

    public void setGovname(String govname) {
        this.govname = govname;
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

    public String getGovname1() {
        return govname1;
    }

    public void setGovname1(String govname1) {
        this.govname1 = govname1;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
    }

    public String getLegalrepresentative() {
        return legalrepresentative;
    }

    public void setLegalrepresentative(String legalrepresentative) {
        this.legalrepresentative = legalrepresentative;
    }

    public String getIDNo() {
        return IDNo;
    }

    public void setIDNo(String iDNo) {
        IDNo = iDNo;
    }

    public String getProjectguid() {
        return projectguid;
    }

    public void setProjectguid(String projectguid) {
        this.projectguid = projectguid;
    }

    public String getDyrq() {
        return dyrq;
    }

    public void setDyrq(String dyrq) {
        this.dyrq = dyrq;
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

    public String getDivhtml() {
        return divhtml;
    }

    public void setDivhtml(String divhtml) {
        this.divhtml = divhtml;
    }

}
