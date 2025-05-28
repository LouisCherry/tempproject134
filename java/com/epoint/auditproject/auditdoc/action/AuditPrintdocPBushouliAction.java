package com.epoint.auditproject.auditdoc.action;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBushouli;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBushouli;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.util.HtmlUtil;
import com.epoint.xmz.util.HttpSignTest;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Security;
import java.util.*;

/**
 * 通用不予受理决定书打印页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-02 15:38:34]
 */
@RestController("auditprintdocpbushouliaction")
@Scope("request")
public class AuditPrintdocPBushouliAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 7492773091496619808L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,projectname,applydate,orgwn,flowsn,status,applyername ";
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
     * 事项guid
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

    private String sbsx2;

    private String span1;
    private String span2;

    /**
     * 办号
     */
    private String txtbh = "";

    /**
     * 文号
     */
    private String wn;
    private String wn1;
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
     * 否定原因
     */
    private String denyReason;

    /**
     * 办件状态
     */
    private String status;

    /**
     * 送达文件名称
     */
    private String sendFileName;

    /**
     * 送达文件文号
     */
    private String sendFileOrgWN;

    /**
     * 回执类型
     */
    private String huiZhiType;

    /**
     * 不予受理决定书快照的html
     */
    private String divhtml;

    @Autowired
    IAuditProjectDocSnap projectDocSnapService;

    @Autowired
    IAttachService attachService;

    @Autowired
    IAuditPrintdocBushouli printdocBushouliService;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    IAuditTask auditTaskService;

    @Autowired
    ICodeItemsService codeItemsService;

    @Autowired
    IHandleConfig handleConfigService;

    /**
     * 打印时间
     */

    private String operatedate;

    private List<SelectItem> signtypedModal = null;

    private String signname;

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    @Autowired
    private IAuditProjectDocsnapHistroyService iAuditProjectDocsnapHistroyService;

    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        huiZhiType = "1";
        taskguid = getRequestParameter("taskguid");
        audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        if(audittask!=null){
            if(StringUtils.isNotBlank(audittask.getStr("ifsignwenshu"))){
                addCallbackParam("ifsignwenshu",audittask.getStr("ifsignwenshu"));
            }
        }
        refreshinfo();
    }

    /**
     * 查看文书图片
     */
    public void btnGPHPic() {
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYSLJDS).getResult();
        if ("2".equals(huiZhiType)) {// 回执
            if (list != null && !list.isEmpty() && StringUtil.isNotBlank(list.get(0).getHuizhiattachguid())) {
                List<FrameAttachInfo> attachlist = attachService
                        .getAttachInfoListByGuid(list.get(0).getHuizhiattachguid());
                String GPHtmlPicaddress = "../../../Pages/AttachStorageInfo/ReadAttachFile.aspx?attachguid="
                        + attachlist.get(0).getAttachGuid();
                addCallbackParam("msg", GPHtmlPicaddress);
            } else {
                addCallbackParam("msg", "请先上传回执图片");
            }
        } else {
            if (list != null && !list.isEmpty()) {
                if (StringUtil.isNotBlank(list.get(0).getDocattachguid())) {
                    List<FrameAttachInfo> attachlist = attachService
                            .getAttachInfoListByGuid(list.get(0).getDocattachguid());
                    String GPHtmlPicaddress = "../../../Pages/AttachStorageInfo/ReadAttachFile.aspx?attachguid="
                            + attachlist.get(0).getAttachGuid();
                    addCallbackParam("msg", GPHtmlPicaddress);
                } else {
                    addCallbackParam("msg", "请先上传通知书图片");
                }
            } else {
                addCallbackParam("msg", "请先上传通知书图片");
            }
        }
    }

    /**
     * 打印送达回证
     */
    public void btnprintClick() {
        auditprintdocbushouli = printdocBushouliService.getDocBushouliByProjectGuid(projectguid).getResult();
        // 是否打印了回执
        auditprintdocbushouli.setSdhzflag("Y");
        printdocBushouliService.updateDocBushouli(auditprintdocbushouli);
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYSLJDS).getResult();
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setSdhzflag("Y");
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYSLJDS);
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 打印不予受理通知书
     */
    public void btnprintwenshuClick(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBushouli auditprintdocBushouli = printdocBushouliService.getDocBushouliByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        auditprintdocBushouli.setWenshuflag(wenshuflag);
        printdocBushouliService.updateDocBushouli(auditprintdocBushouli);

        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYSLJDS).getResult();
        if (StringUtil.isNotBlank(divhtml)) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYSLJDS);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            projectDocSnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 更新不予受理信息
     */
    @SuppressWarnings("unused")
    public void refreshinfo() {
        // 事项信息
        audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        // 办件信息
        auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, "")
                .getResult();
        // 受理通知书实体
        auditprintdocbushouli = printdocBushouliService.getDocBushouliByProjectGuid(projectguid).getResult();

        tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        // 日期
        dyrq = EpointDateUtil.convertDate2String(auditprintdocbushouli.getDyrq(), "yyyy年MM月dd日");

        operatedate = EpointDateUtil.convertDate2String(auditprintdocbushouli.getOperatedate(), "yyyy年MM月dd日");

        dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");

        String newtaskname = auditprintdocbushouli.getTaskname();
        txtbh = auditprintdocbushouli.getBh();

        // 否定原因
        String opinion = auditprintdocbushouli.getOpinion();
        if (StringUtil.isNotBlank(opinion)) {
            int length = opinion.length();
            String str = String.valueOf(opinion.charAt(length - 1));
            if ("，".equals(str) || "。".equals(str) || "！".equals(str)) {
                denyReason = opinion;
            } else {
                denyReason = opinion;
                denyReason += "，";
            }
        }

        // 去除名称过长限制，显示全部字符
        sbsx = newtaskname;
        span1 = newtaskname;
        /* if (newtaskname.length() > 22) {
            sbsx = newtaskname.substring(0, 22);
            sbsx2 = newtaskname.substring(22);

            span1 = newtaskname.substring(0, 22);
            span2 = newtaskname.substring(22);
        }
        else {
            sbsx = newtaskname;
            span1 = newtaskname;
        }*/
        //int orgnwn = auditproject.getOrgwn().lastIndexOf("〔");
        StringBuffer orgnwninfoold;

        // if ("1".equals(audittask.getIssz())) {
        // orgnwninfoold = new
        // StringBuffer(auditproject.getOrgwn()).insert(orgnwn, "受字");
        // }
        // else {
        orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
        // }
        String wnninfo = orgnwninfoold.toString();
        String whnumber = wnninfo.replace("〕", "〕第");
        int numberbh = whnumber.indexOf('第') + 1;
        wn = whnumber.substring(0, numberbh);

        // 办件编号的前缀 ZJG
        String AS_FLOWSN_PRE = handleConfigService
                .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        String wninfo = (auditproject.getFlowsn() == null) ? "" : auditproject.getFlowsn().replace(AS_FLOWSN_PRE, "").substring(0, 4);

        String haoma = txtbh;
        String sdhzwh = "";
        if (haoma.length() == 1) {
            sdhzwh = wninfo + "00" + haoma + "-";
        } else if (haoma.length() == 2) {
            sdhzwh = wninfo + "0" + haoma + "-";
        } else if (haoma.length() == 3) {
            sdhzwh = wninfo + haoma + "-";
        } else if (haoma.length() > 3) {
            sdhzwh = wninfo + haoma + "-";
        }

        //TODO 暂缓
        /*int printid;
        AuditProjectOperation auditprojectoperation = auditprojectoperationservice
                .selectOperationByProjectGuidAndType(projectguid, ZwfwConstant.BANJIAN_STATUS_DBB);
        if (auditprojectoperation == null) {
            printid = 1;
        }
        else {
            printid = 2;
        }
        String printhwdz = sdhzwh + printid;
        wn1 = printhwdz;*/
        // 办件状态
        status = codeItemsService.getItemTextByCodeName("办件状态", auditproject.getStatus().toString()) + "。";
        tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        String lbTitle = "行政" + tasktype + "申请受理通知书";
        sendFileName = lbTitle;
        sendFileOrgWN = wn + txtbh + "号";
    }

    /**
     * 高拍仪上传
     */
    public void btnGPH() {
        String doctype = String.valueOf(ZwfwConstant.DOC_TYPE_BYSLJDS);
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYSLJDS).getResult();
        if (list != null && !list.isEmpty()) {
            String GPHtmladress = "../../GaoPaiPages/GPHtml4LT2.aspx?projectguid=" + projectguid + "&doctype=" + doctype
                    + "&huiZhiType=" + huiZhiType;
            addCallbackParam("msg", GPHtmladress);
        } else {
            addCallbackParam("msg", "请先打印文书再上传");
        }
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "送达地址", null, false));
        }
        return this.sdaddressModal;
    }

    /**
     * 签章并打印受理通知书
     */
    public void btnprintwenshuClickAndSign(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBushouli auditprintdocBushouli = printdocBushouliService.getDocBushouliByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        auditprintdocBushouli.setWenshuflag(wenshuflag);
        printdocBushouliService.updateDocBushouli(auditprintdocBushouli);

        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYSLJDS).getResult();
        if (StringUtil.isNotBlank(divhtml)) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYSLJDS);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            projectDocSnapService.addDocSnap(auditProjectDocsnap);
        }
        try {
            //保存html到文件里
            //html处理
            StringBuffer sb = new StringBuffer();
            sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><style>\n" +
                    "* {\n" +
                    "  font-family: 'simhei';\n" +
                    "}</style></head>");
            sb.append(divhtml);
            sb.append("</html>");
            String sb1=sb.toString();
            //替换<br>
            sb1=sb1.replace("<br>","<br/>");
            AuditProjectDocsnapHistroy auditProjectDocSnapHistory = new AuditProjectDocsnapHistroy();
            auditProjectDocSnapHistory.setRowguid(UUID.randomUUID().toString());
            auditProjectDocSnapHistory.setProjectguid(projectguid);
            auditProjectDocSnapHistory.setDoctype(ZwfwConstant.DOC_TYPE_BYSLJDS);
            // Html格式的快照
            auditProjectDocSnapHistory.setDochtml(sb1);
            auditProjectDocSnapHistory.setTaskname(auditproject.getProjectname());
            auditProjectDocSnapHistory.setOperatedate(new Date());
            auditProjectDocSnapHistory.setOperateusername(userSession.getDisplayName());
            // html转化为pdf
            ByteArrayOutputStream outputStream = HtmlUtil.htmlToOutputStream(sb1);
            // 存pdf
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            long size = (long) inputStream.available();
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
            frameAttachInfo.setAttachFileName("不予受理通知书.pdf");
            frameAttachInfo.setCliengTag("不予受理通知书");
            frameAttachInfo.setUploadUserGuid("");
            frameAttachInfo.setUploadUserDisplayName("");
            frameAttachInfo.setUploadDateTime(new Date());
            frameAttachInfo.setContentType(".pdf");
            frameAttachInfo.setAttachLength(size);
            String attachguid = attachService.addAttach(frameAttachInfo, inputStream).getAttachGuid();
            auditProjectDocSnapHistory.setDocattachguid(attachguid);
            String msg = signPdf(frameAttachInfo.getCliengGuid());
            addCallbackParam("attachguid",attachguid);
            log.info("msg:"+msg);
            addCallbackParam("msg",msg);
            iAuditProjectDocsnapHistroyService.insert(auditProjectDocSnapHistory);

        }catch (Exception e) {
            e.printStackTrace();
        }
        refreshinfo();
    }

    public String signPdf(String clienguid){
        //印章ID
        String signcode = "";
        String xrecord = ConfigUtil.getConfigValue("xmzArgs", "byslx");;
        String yrecord = ConfigUtil.getConfigValue("xmzArgs", "bysly");
        String onsize = "1";
        //应用标志
        String userid= "";
        if (StringUtil.isBlank(xrecord)) {
            xrecord = "350";
        }
        if (StringUtil.isBlank(yrecord)) {
            yrecord = "220";
        }
        if(audittask!=null){
            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(audittask.getRowguid(),true).getResult();
            if(StringUtils.isNotBlank(signname) && signname.equals(auditTaskExtension.get("signcode"))){
                signcode = auditTaskExtension.get("signcode");
                userid= auditTaskExtension.get("signuserid");
            }else{
                signcode = auditTaskExtension.get("signcode1");
                userid= auditTaskExtension.get("signuserid1");
            }
        }
        Security.addProvider(new BouncyCastleProvider());
        log.info("开始电子签章");
        String msgs = null;
        try {
            log.info("onsize:"+onsize);
            log.info("xrecord:"+xrecord);
            log.info("yrecord:"+yrecord);
            log.info("signcode:"+signcode);
            log.info("userid:"+userid);
            msgs = HttpSignTest.signPdf(onsize, xrecord, yrecord,signcode,
                    userid, clienguid, "position");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!"签章完成".equals(msgs)) {
            return msgs;
        } else {
            return "签章成功！";
        }
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

    public String getSbsx2() {
        return sbsx2;
    }

    public void setSbsx2(String sbsx2) {
        this.sbsx2 = sbsx2;
    }

    public String getSpan1() {
        return span1;
    }

    public void setSpan1(String span1) {
        this.span1 = span1;
    }

    public String getSpan2() {
        return span2;
    }

    public void setSpan2(String span2) {
        this.span2 = span2;
    }

    public String getWn1() {
        return wn1;
    }

    public void setWn1(String wn1) {
        this.wn1 = wn1;
    }

    public String getSendFileName() {
        return sendFileName;
    }

    public void setSendFileName(String sendFileName) {
        this.sendFileName = sendFileName;
    }

    public String getSendFileOrgWN() {
        return sendFileOrgWN;
    }

    public void setSendFileOrgWN(String sendFileOrgWN) {
        this.sendFileOrgWN = sendFileOrgWN;
    }

    public String getHuiZhiType() {
        return huiZhiType;
    }

    public void setHuiZhiType(String huiZhiType) {
        this.huiZhiType = huiZhiType;
    }

    public String getDivhtml() {
        return divhtml;
    }

    public void setDivhtml(String divhtml) {
        this.divhtml = divhtml;
    }
    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }
    public List<SelectItem> getSigntypeModal() {
        if(audittask!=null && signtypedModal==null){
            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(audittask.getRowguid(),true).getResult();
            signtypedModal=new ArrayList<>();
            signtypedModal.add(new SelectItem("","请选择签章"));
            signtypedModal.add(new SelectItem(auditTaskExtension.get("signcode"),auditTaskExtension.get("signname")));
            signtypedModal.add(new SelectItem(auditTaskExtension.get("signcode1"),auditTaskExtension.get("signname1")));
        }
        return this.signtypedModal;
    }

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }

}
