package com.epoint.auditproject.auditdoc.action;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBuzheng;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBuzheng;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
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
 * 材料补正告知书打印页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-09 15:33:34]
 */
@RestController("auditprintdocpbuzhengaction")
@Scope("request")
public class AuditPrintdocPBuzhengAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 6583839810892006285L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,taskguid,projectname,applydate,orgwn,flowsn,status,applyername,acceptareacode ";
    /**
     * service
     */
    @Autowired
    private IAuditPrintdocBuzheng iAuditPrintdocBuzheng;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 通用受理通知书service
     */

    // AuditPrintdocShouliService auditPrintdocShouliService = new
    // AuditPrintdocShouliService();

    /**
     * 文书快照service
     */
    @Autowired
    private IAuditProjectDocSnap auditProjectDocsnapService;

    /**
     * 材料补正告知书实体对象
     */
    private AuditPrintdocBuzheng auditprintdocbuzheng = null;

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
     * 流程版本实例guid
     */
    private String processVersionInstanceGuid;

    /**
     * 打印时间
     */

    private String dyrq;

    private String dyrq1;
    /**
     * 回执类型
     */
    @Autowired
    IAttachService attachService;

    /**
     * 意见
     */

    private String opinion;

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
     * 办号
     */
    private String txtbh = "";

    /**
     * 文号
     */
    private String wn;
    private String wn1;

    private String sbsx;

    private String sbsx2;

    private String span1;

    private String span2;

    /**
     * sdkind下拉列表modal
     */
    private List<SelectItem> sdkindModal = null;

    /**
     * sdaddress下拉列表modal
     */
    private List<SelectItem> sdaddressModal = null;

    /**
     * 材料集合
     */

    private String txtmaterial;

    /**
     * 回执类型
     */

    private String huiZhiType;

    /**
     * 材料补正告知书快照的html
     */
    private String divhtml;

    /**
     * 送达文件名称
     */
    private String sendFileName;

    /**
     * 送达文件文号
     */
    private String sendFileOrgWN;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

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
        // processVersionInstanceGuid
        // =getRequestParameter("ProcessVersionInstanceGuid");
        huiZhiType = "1";
        refreshinfo();
        if(audittask!=null){
            if(StringUtils.isNotBlank(audittask.getStr("ifsignwenshu"))){
                addCallbackParam("ifsignwenshu",audittask.getStr("ifsignwenshu"));
            }
        }
    }

    /**
     * 高拍仪上传
     */
    public void btnGPH() {
        String doctype = String.valueOf(ZwfwConstant.DOC_TYPE_CLBZGZS);
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
        if (list != null && !list.isEmpty()) {
            String GPHtmladress = "../../GaoPaiPages/GPHtml4LT2.jspx?projectguid=" + projectguid + "&doctype=" + doctype
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
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
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
            if (!list.isEmpty()) {
                if (list.get(0).getDocattachguid() != null && !"".equals(list.get(0).getDocattachguid())) {
                    List<FrameAttachInfo> attachlist = attachService
                            .getAttachInfoListByGuid(list.get(0).getDocattachguid());
                    String GPHtmlPicaddress = "../../../Pages/AttachStorageInfo/ReadAttachFile.jspx?attachguid="
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
        AuditPrintdocBuzheng printdocbuzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid)
                .getResult();
        // 是否打印了回执
        printdocbuzheng.setSdhzflag("Y");
        iAuditPrintdocBuzheng.update(printdocbuzheng);
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_CLBZGZS);
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            auditProjectDocsnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 打印材料补正告知书
     */
    public void btnprintwenshuClick(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBuzheng printdocbuzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        printdocbuzheng.setWenshuflag(wenshuflag);
        iAuditPrintdocBuzheng.update(printdocbuzheng);

        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
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
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_CLBZGZS);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            auditProjectDocsnapService.addDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
    }

    /**
     * 更新材料补正信息
     */
    public void refreshinfo() {
        auditprintdocbuzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid).getResult();

        lxfsinfo = "联系人：" + (StringUtil.isBlank(auditprintdocbuzheng.getLxr()) ? userSession.getDisplayName()
                : auditprintdocbuzheng.getLxr()) + "&nbsp;&nbsp;&nbsp;";
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
                        if (StringUtil.isNotBlank(delegate.getLink_tel())) {
                            audittask.setLink_tel(delegate.getLink_tel());
                        }
                        if (StringUtil.isNotBlank(delegate.getSupervise_tel())) {
                            audittask.setSupervise_tel(delegate.getSupervise_tel());
                        }
                    }
                }
            }
            lxfsinfo += "&nbsp;&nbsp;&nbsp;联系电话："
                    + (StringUtil.isBlank(audittask.getLink_tel()) ? "" : audittask.getLink_tel()) + "&nbsp;&nbsp;&nbsp;";
            lxfsinfo += "监督电话：" + (StringUtil.isBlank(audittask.getSupervise_tel()) ? "" : audittask.getSupervise_tel());
        }

        // 日期
        dyrq = EpointDateUtil.convertDate2String(auditprintdocbuzheng.getDyrq(), "yyyy年MM月dd日");
        String wnninfo = "";
        if (auditproject != null) {
            dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            wnninfo = auditproject.getOrgwn();
        }
        operatedate = EpointDateUtil.convertDate2String(auditprintdocbuzheng.getOperatedate(), "yyyy年MM月dd日");
        String newtaskname = auditprintdocbuzheng.getTaskname();
        // String Materials = auditprintdocBuzhengbean.getMaterials();
        txtbh = auditprintdocbuzheng.getBh();
        wnninfo = wnninfo.replace("null", "");
        String whnumber = wnninfo.replace("〕", "〕第");
        int numberbh = whnumber.indexOf('第') + 1;

        // String numberwn = whnumber.substring(numberbh);
        // int dwz = numberwn.indexOf("号");
        // String numberwh = numberwn.substring(0, dwz);
        wn = whnumber.substring(0, numberbh);

        // 办件编号的前缀 ZJG
        String AS_FLOWSN_PRE = handleConfigService
                .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        String wninfo = "";
        if (auditproject != null) {
            wninfo = (auditproject.getFlowsn() == null) ? "" : auditproject.getFlowsn().replace(AS_FLOWSN_PRE, "").substring(0, 4);
        }
        // int k = auditproject.getOrgwn().indexOf("〕");
        // int b = auditproject.getOrgwn().indexOf("号");
        // int c = b - k - 1;

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

        int printid = 1;
        String printhwdz = sdhzwh + printid;
        wn1 = printhwdz;

        txtmaterial = auditprintdocbuzheng.getMaterials();
        if (auditprintdocbuzheng.getBznumber() != null && !"".equals(auditprintdocbuzheng.getBznumber())
                && auditprintdocbuzheng.getOpinion() != null && !"".equals(auditprintdocbuzheng.getOpinion())) {
            if ("1".equals(auditprintdocbuzheng.getBznumber())) {
                txtmaterial += auditprintdocbuzheng.getOpinion() + ";";
            } else {
                txtmaterial += auditprintdocbuzheng.getBznumber() + "、" + auditprintdocbuzheng.getOpinion() + ";";
            }
        }

        // 去除字符限制，直接全部显示
        sbsx = newtaskname;
        span1 = newtaskname;
        /*		if (newtaskname.length() > 22) {
        			sbsx = newtaskname.substring(0, 22);
        			sbsx2 = newtaskname.substring(22);

        			span1 = newtaskname.substring(0, 22);
        			span2 = newtaskname.substring(22);
        		} else {
        			sbsx = newtaskname;
        			span1 = newtaskname;
        			// sbsx2display = "display:none";
        			// span2display = "display:none";
        		}*/
        if (audittask != null) {
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        }
        String lbTitle = "行政" + tasktype + "申请材料补正告知书";
        // String lbTitle = "行政申请材料补正告知书";
        sendFileName = lbTitle;
        sendFileOrgWN = wn + txtbh + "号";

        String materialnames = "";
        opinion = StringUtil.isBlank(auditprintdocbuzheng.getOpinion()) ? "" : auditprintdocbuzheng.getOpinion();
        String myOpinion = "意见：<b>" + opinion + "</b>";
        List<AuditProjectMaterial> projectMaterials = projectMaterialService.selectProjectMaterial(projectguid)
                .getResult();
        int index = 1;
        if (projectMaterials != null) {
            for (AuditProjectMaterial auditProjectMaterial : projectMaterials) {
                if (ZwfwConstant.Material_AuditStatus_DBZ.equals(auditProjectMaterial.getAuditstatus())) {
                    materialnames += index + "、" + auditProjectMaterial.getTaskmaterial() + "<br/>";
                    index++;
                }
            }
        }
        addCallbackParam("material", materialnames);
        addCallbackParam("opinion", myOpinion);
    }

    /**
     * 签章并打印受理通知书
     */
    public void btnprintwenshuClickAndSign(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocBuzheng printdocbuzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        printdocbuzheng.setWenshuflag(wenshuflag);
        iAuditPrintdocBuzheng.update(printdocbuzheng);
        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
        if (divhtml != null) {
            divhtml = divhtml.replaceAll("括号", "()");
        }
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_CLBZGZS);
            // Html格式的快照
            auditProjectDocsnap.setDochtml(divhtml);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            auditProjectDocsnapService.addDocSnap(auditProjectDocsnap);

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
            auditProjectDocSnapHistory.setDoctype(ZwfwConstant.DOC_TYPE_CLBZGZS);
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
            frameAttachInfo.setAttachFileName("材料补正通知书.pdf");
            frameAttachInfo.setCliengTag("材料补正通知书");
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
        String xrecord = ConfigUtil.getConfigValue("xmzArgs", "zlbzx");;
        String yrecord = ConfigUtil.getConfigValue("xmzArgs", "zlbzy");
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

    public AuditPrintdocBuzheng getAuditprintdocbuzheng() {
        return auditprintdocbuzheng;
    }

    public void setAuditprintdocbuzheng(AuditPrintdocBuzheng auditprintdocbuzheng) {
        this.auditprintdocbuzheng = auditprintdocbuzheng;
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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
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

    public String getTxtmaterial() {
        return txtmaterial;
    }

    public void setTxtmaterial(String txtmaterial) {
        this.txtmaterial = txtmaterial;
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

    public String getWn1() {
        return wn1;
    }

    public void setWn1(String wn1) {
        this.wn1 = wn1;
    }

    public String getSbsx() {
        return sbsx;
    }

    public void setSbsx(String sbsx) {
        this.sbsx = sbsx;
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
