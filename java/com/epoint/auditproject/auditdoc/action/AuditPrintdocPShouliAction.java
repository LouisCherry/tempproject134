package com.epoint.auditproject.auditdoc.action;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocShouli;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.util.HtmlUtil;
import com.epoint.xmz.util.HttpSignTest;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.*;

/**
 * 通用受理通知书打印页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-10-28 16:46:35]
 */
@RestController("auditprintdocpshouliaction")
@Scope("request")
public class AuditPrintdocPShouliAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 93959663016111039L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = " rowguid,taskguid,projectname,applydate,orgwn,flowsn,status,ouguid,taskcaseguid,biguid,if_express,applyername,acceptareacode ";
    /**
     * service
     */
    @Autowired
    private IAuditPrintdocShouli iAuditPrintdocShouli;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 事项材料
     */
    @Autowired
    private IAuditTaskMaterial auditTaskMaterial;

    @Autowired
    IAttachService attachService;
    /**
     * 情形材料
     */
    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;
    /**
     * 文书快照service
     */
    @Autowired
    private IAuditProjectDocSnap auditProjectDocsnapService;

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
    private String sbsx2;

    private String span1;

    private String span2;
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
    private String wn1;

    /**
     * 打印时间
     */

    private String dyrq;

    private String dyrq1;

    /**
     * 送达文件名称
     */
    private String sendFileName;

    /**
     * 送达文件文号
     */
    private String sendFileOrgWN;

    /**
     * 事项类别
     */
    private String tasktype;

    /**
     * 受理通知书快照的html
     */
    private String divhtml;

    /**
     * 回执类型
     */

    private String huiZhiType;

    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private ICodeItemsService codeItemsService;

    private String chkcode;

    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

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
        refreshinfo();
        String result = "";
        result = handleConfigService.getFrameConfig("AS_FRONT_FUWUKA", ZwfwUserSession.getInstance().getCenterGuid())
                .getResult();
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditproject.getIf_express())) {
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
                auditLogisticsBasicInfo = iAuditLogisticsBasicInfo.getAuditLogisticsBasicinfoListByFlowsn(projectguid).getResult();
            }
            if (auditLogisticsBasicInfo != null) {
                chkcode = auditLogisticsBasicInfo.getChk_code();
            }
        } else {
            addCallbackParam("chk", "0");
        }
        if(audittask!=null){
            if(StringUtils.isNotBlank(audittask.getStr("ifsignwenshu"))){
                addCallbackParam("ifsignwenshu",audittask.getStr("ifsignwenshu"));
            }
        }
        addCallbackParam("msg", result);
    }

    /**
     * 更新行政受理信息
     */
    public void refreshinfo() {
        String area = "";
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
        } else {
            auditproject = new AuditProject();
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
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            lxfsinfo += "&nbsp;&nbsp;&nbsp;联系电话："
                    + (StringUtil.isBlank(audittask.getLink_tel()) ? "" : audittask.getLink_tel()) + "&nbsp;&nbsp;&nbsp;";
            lxfsinfo += "监督电话：" + (StringUtil.isBlank(audittask.getSupervise_tel()) ? "" : audittask.getSupervise_tel());
            dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
        }
        // 受理通知书实体
        auditprintdocshouli = iAuditPrintdocShouli.getDocShouliByProjectGuid(projectguid).getResult();
        // 获取方式信息
        lxfsinfo = "联系人：" + (StringUtil.isBlank(auditprintdocshouli.getLxr()) ? userSession.getDisplayName()
                : auditprintdocshouli.getLxr());
        // 日期
        dyrq = EpointDateUtil.convertDate2String(auditprintdocshouli.getDyrq(), "yyyy年MM月dd日");
        
        operatedate = EpointDateUtil.convertDate2String(auditprintdocshouli.getOperatedate(), "yyyy年MM月dd日");
        
        String newtaskname = auditprintdocshouli.getTaskname();
        txtbh = auditprintdocshouli.getBh();
        timeinfo = auditprintdocshouli.getTimeinfo();

        sbsx = newtaskname;
        span1 = newtaskname;

        StringBuffer orgnwninfoold;
        orgnwninfoold = new StringBuffer(auditproject.getOrgwn());
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
        int printid = 0;
        String printhwdz = sdhzwh + printid;
        wn1 = printhwdz;
        // 找出所有的材料
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
                int a1 = i + 1;
                material += "&nbsp;&nbsp;&nbsp;&nbsp;" + a1 + "、" + projectMaterial.get(i).get("MATERIALNAME");
                material += "；" + "<br>";
            }
        }

        if (audittask != null) {
            tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
        }
        // TODO
        txtmaterial = material;
        String lbTitle = "行政" + tasktype + "申请受理通知书";
        sendFileName = lbTitle;
        sendFileOrgWN = wn + txtbh + "号";
    }

    /**
     * 高拍仪上传
     */
    public void btnGPH() {
        String doctype = String.valueOf(ZwfwConstant.DOC_TYPE_SLTZS);
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SLTZS).getResult();
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
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SLTZS).getResult();
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
     * 打印受理通知书
     */
    public void btnprintwenshuClick(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocShouli auditprintdocShouli = iAuditPrintdocShouli.getDocShouliByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        auditprintdocShouli.setWenshuflag(wenshuflag);
        iAuditPrintdocShouli.update(auditprintdocShouli);
        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SLTZS).getResult();
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
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_SLTZS);
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
     * 签章并打印受理通知书
     */
    public void btnprintwenshuClickAndSign(String divhtml) {
        divhtml = EncodeUtil.decode(divhtml);
        AuditPrintdocShouli auditprintdocShouli = iAuditPrintdocShouli.getDocShouliByProjectGuid(projectguid)
                .getResult();
        String wenshuflag = "Y";
        auditprintdocShouli.setWenshuflag(wenshuflag);
        iAuditPrintdocShouli.update(auditprintdocShouli);
        // 根据ProjectGuid和DocType判断是否存在办件文书快照
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SLTZS).getResult();
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
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_SLTZS);
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
            auditProjectDocSnapHistory.setDoctype(ZwfwConstant.DOC_TYPE_SLTZS);
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
            frameAttachInfo.setAttachFileName("受理通知书.pdf");
            frameAttachInfo.setCliengTag("受理通知书");
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
        String xrecord = ConfigUtil.getConfigValue("xmzArgs", "sltzsx");;
        String yrecord = ConfigUtil.getConfigValue("xmzArgs", "sltzsy");
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

    /**
     * 打印送达回证
     */
    public void btnprintClick() {
        AuditPrintdocShouli auditprintdocShouli = iAuditPrintdocShouli.getDocShouliByProjectGuid(projectguid)
                .getResult();
        // 是否打印了回执
        auditprintdocShouli.setSdhzflag("Y");
        iAuditPrintdocShouli.update(auditprintdocShouli);
        List<AuditProjectDocSnap> list = auditProjectDocsnapService
                .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_SLTZS).getResult();
        if (list != null && !list.isEmpty()) {
            AuditProjectDocSnap auditProjectDocsnap = list.get(0);
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        } else {
            AuditProjectDocSnap auditProjectDocsnap = new AuditProjectDocSnap();
            auditProjectDocsnap.setRowguid(UUID.randomUUID().toString());
            auditProjectDocsnap.setSdhzflag("Y");
            auditProjectDocsnap.setDoctype(ZwfwConstant.DOC_TYPE_SLTZS);
            auditProjectDocsnap.setProjectguid(projectguid);
            auditProjectDocsnap.setTaskname(auditproject.getProjectname());
            auditProjectDocsnap.setOperatedate(new Date());
            auditProjectDocsnap.setOperateusername(userSession.getDisplayName());
            auditProjectDocsnapService.updateDocSnap(auditProjectDocsnap);
        }
        refreshinfo();
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

        // 根据ProjectGuid判断是否存在不予批准决定书
        if (StringUtil.isNotBlank(auditprintdocshouli.getRowguid())) {
            auditprintdocshouli.setLaws(laws);
            auditprintdocshouli.setTaskname(sbsx);
            auditprintdocshouli.setTimeinfo(timeinfo);
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
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
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
            auditprintdocshouli.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
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
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "PshouliTZS?projectguid=" + projectguid;
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
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "送达地址", null, false));
        }
        return this.sdaddressModal;
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

    public String getDivhtml() {
        return divhtml;
    }

    public void setDivhtml(String divhtml) {
        this.divhtml = divhtml;
    }

    public String getHuiZhiType() {
        return huiZhiType;
    }

    public void setHuiZhiType(String huiZhiType) {
        this.huiZhiType = huiZhiType;
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

    public String getSignname() {
        return signname;
    }

    public void setSignname(String signname) {
        this.signname = signname;
    }
}
