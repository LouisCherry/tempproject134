package com.epoint.auditproject.auditdoc.action;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBuxuke;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBuxuke;
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
 * 不予许可决定书打印页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-08 10:10:52]
 */
@RestController("auditprintdocpbuxukeaction")
@Scope("request")
public class AuditPrintdocPBuxukeAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 772524245512206304L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,projectname,applydate,orgwn,flowsn,status,applyername ";
    @Autowired
    private ICodeItemsService codeItemsService;
    /**
     * 不予许可决定书实体对象
     */
    private AuditPrintdocBuxuke auditprintdocBuxuke = null;

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
     * 事项guid
     */
    private String taskguid;

    /**
     * 受理依据
     */
    private String laws;

    /**
     * 事项类别
     */
    private String tasktype;

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

    private String wn1;
    /**
     * 申请日期
     */
    private String dyrq1;
    private String dyrq;
    /**
     * 办件名称
     */
    private String sbsx;

    private String sbsx2;

    /**
     * 审批类别
     */
    private String tasktypeExtension;

    /**
     * 不予许可决定书快照的html
     */
    private String divhtml;

    private String itemvalue;

    private String span1;

    private String span2;

    /**
     * 送达文件名称
     */
    private String sendFileName;

    /**
     * 送达文件文号
     */
    private String sendFileOrgWN;

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

    /**
     * 回执类型
     */
    private String huiZhiType;

    @Autowired
    IAuditProjectDocSnap projectDocSnapService;

    @Autowired
    IAttachService attachService;

    @Autowired
    IAuditPrintdocBuxuke printdocBuxukeService;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    IAuditTask auditTaskService;

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
        taskguid = getRequestParameter("taskguid");
        itemvalue = getRequestParameter("itemvalue");
        huiZhiType = "1";
        audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        if(audittask!=null){
            if(StringUtils.isNotBlank(audittask.getStr("ifsignwenshu"))){
                addCallbackParam("ifsignwenshu",audittask.getStr("ifsignwenshu"));
            }
        }
        refreshinfo();

    }

    /**
     * 高拍仪上传
     */
    public void btnGPH() {
        String doctype = String.valueOf(ZwfwConstant.DOC_TYPE_BYXKJDS);
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYXKJDS).getResult();
        if (list != null && !list.isEmpty()) {
            String GPHtmladress = "../../GaoPaiPages/GPHtml4LT2.aspx?projectguid=" + projectguid + "&doctype=" + doctype
                    + "&huiZhiType=" + huiZhiType;
            addCallbackParam("msg", GPHtmladress);
        } else {
            addCallbackParam("msg", "请先打印文书再上传");
        }
    }

    /**
     * 查看文书图片
     */
    public void btnGPHPic() {
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYXKJDS).getResult();
        if ("2".equals(huiZhiType)) {// 回执
            if (!list.isEmpty() && StringUtil.isNotBlank(list.get(0).getHuizhiattachguid())) {
                List<FrameAttachInfo> attachlist = attachService
                        .getAttachInfoListByGuid(list.get(0).getHuizhiattachguid());
                String GPHtmlPicaddress = "../../../Pages/AttachStorageInfo/ReadAttachFile.aspx?attachguid="
                        + attachlist.get(0).getAttachGuid();
                addCallbackParam("msg", GPHtmlPicaddress);
            } else {
                addCallbackParam("msg", "请先上传回执图片");
            }
        } else {
            if (!list.isEmpty()) {
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
        AuditPrintdocBuxuke auditprintdocbuxuke = printdocBuxukeService.getDocBuxukeByProjectGuid(projectguid)
                .getResult();
        // 是否打印了回执
        auditprintdocbuxuke.setSdhzflag("Y");
        printdocBuxukeService.updateDocBuxuke(auditprintdocbuxuke);
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYXKJDS).getResult();
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setSdhzflag("Y");
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYXKJDS);
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 打印不予许可决定书
     */
    public void btnprintwenshuClick(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBuxuke auditprintdocbuxuke = printdocBuxukeService.getDocBuxukeByProjectGuid(projectguid)
                .getResult();

        String wenshuflag = "Y";
        auditprintdocbuxuke.setWenshuflag(wenshuflag);
        printdocBuxukeService.updateDocBuxuke(auditprintdocbuxuke);

        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYXKJDS).getResult();
        if (StringUtil.isNotBlank(divhtml)) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYXKJDS);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 更新不予许可信息
     */
    @SuppressWarnings("unused")
    public void refreshinfo() {
        auditprintdocBuxuke = printdocBuxukeService.getDocBuxukeByProjectGuid(projectguid).getResult();
        // 事项信息
        audittask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
        // 办件信息
        auditproject = auditProjectService.getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        tasktypeExtension = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        // 日期
        dyrq = EpointDateUtil.convertDate2String(auditprintdocBuxuke.getDyrq(), "yyyy年MM月dd日");

        operatedate = EpointDateUtil.convertDate2String(auditprintdocBuxuke.getOperatedate(), "yyyy年MM月dd日");
        dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
        String newtaskname = auditprintdocBuxuke.getTaskname();
        StringBuffer orgnwninfoold;
        orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
        String wnninfo = orgnwninfoold.toString();
        String whnumber = wnninfo.replace("〕", "〕第");
        int numberbh = whnumber.indexOf('第') + 1;

        wn = whnumber.substring(0, numberbh);
        txtbh = auditprintdocBuxuke.getBh();

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
        AuditProjectOperation DBBoperation = auditprojectoperationservice
                .selectOperationByProjectGuidAndType(projectguid, ZwfwConstant.BANJIAN_STATUS_DBB);
        AuditProjectOperation YSLoperation = auditprojectoperationservice
                .selectOperationByProjectGuidAndType(projectguid, ZwfwConstant.BANJIAN_STATUS_YSL);
        if (DBBoperation != null && YSLoperation != null) {
            printid = 3;
        }
        else {
            printid = 2;
        }
        String printhwdz = sdhzwh + printid;
        wn1 = printhwdz;*/

        // 不许类型为不显示日期
        if ("7".equals(itemvalue)) {
            granttype = null;
            granttype1 = "不予你（单位）行政";
        }

        // 不许类型为变更
        if ("1".equals(itemvalue)) {
            granttype = "变更";
            granttype1 = "不予你（单位）变更";
            tasktypeExtension = "";
        }
        // 不许类型为延期
        else if ("2".equals(itemvalue)) {
            granttype = "延期";
            granttype1 = "不予你（单位）延期";
            tasktypeExtension = "";
        }
        // 不许类型为备案
        else if ("4".equals(itemvalue)) {
            granttype = "备案";
            granttype1 = "不予你（单位）备案";
            tasktypeExtension = "";
        }
        // 不许类型为审核
        else if ("5".equals(itemvalue)) {
            granttype = "上报";
            granttype1 = "将你（单位）的行政许可申请不予上报";
            tasktypeExtension = "";
        }
        // 不许类型为空
        else if ("0".equals(itemvalue) || "6".equals(itemvalue)) {
            granttype = null;
            granttype1 = "不予你（单位）行政";
        }
        sbsx = newtaskname;
        sbsx2 = newtaskname;
        span1 = newtaskname;
        span2 = newtaskname;
        String Title;
        if (auditprintdocBuxuke.getGranttype() == null || "".equals(auditprintdocBuxuke.getGranttype())) {
            Title = "不予行政" + codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb()) + "决定书";
            // Title = "不予行政决定书";
        } else {
            Title = "不予" + auditprintdocBuxuke.getGranttype() + "行政"
                    + codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb()) + "决定书";
            // Title = "不予" + auditprintdocBuxukebean.getGranttype() + "行政决定书";
        }
        sendFileName = Title;
        sendFileOrgWN = wn + txtbh + "号";

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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "材料类别", null, true));
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "送达地址", null, false));
        }
        return this.sdaddressModal;
    }

    public AuditPrintdocBuxuke getAuditprintdocBuxuke() {
        return auditprintdocBuxuke;
    }

    public void setAuditprintdocBuxuke(AuditPrintdocBuxuke auditprintdocBuxuke) {
        this.auditprintdocBuxuke = auditprintdocBuxuke;
    }

    /**
     * 签章并打印受理通知书
     */
    public void btnprintwenshuClickAndSign(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBuxuke auditprintdocbuxuke = printdocBuxukeService.getDocBuxukeByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        auditprintdocbuxuke.setWenshuflag(wenshuflag);
        printdocBuxukeService.updateDocBuxuke(auditprintdocbuxuke);

        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = projectDocSnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_BYXKJDS).getResult();
        if (StringUtil.isNotBlank(divhtml)) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            projectDocSnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_BYXKJDS);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
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
            auditProjectDocSnapHistory.setDoctype(ZwfwConstant.DOC_TYPE_BYXKJDS);
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
            frameAttachInfo.setAttachFileName("不予许可通知书.pdf");
            frameAttachInfo.setCliengTag("不予许可通知书");
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
        String xrecord = ConfigUtil.getConfigValue("xmzArgs", "byxkx");;
        String yrecord = ConfigUtil.getConfigValue("xmzArgs", "byxky");
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

    public String getWn1() {
        return wn1;
    }

    public void setWn1(String wn1) {
        this.wn1 = wn1;
    }

    public String getSbsx2() {
        return sbsx2;
    }

    public void setSbsx2(String sbsx2) {
        this.sbsx2 = sbsx2;
    }

    public String getDivhtml() {
        return divhtml;
    }

    public void setDivhtml(String divhtml) {
        this.divhtml = divhtml;
    }

    public String getItemvalue() {
        return itemvalue;
    }

    public void setItemvalue(String itemvalue) {
        this.itemvalue = itemvalue;
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
