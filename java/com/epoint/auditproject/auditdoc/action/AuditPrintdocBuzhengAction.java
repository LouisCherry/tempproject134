package com.epoint.auditproject.auditdoc.action;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdoc.domain.AuditPrintdocBuzheng;
import com.epoint.basic.auditproject.auditprojectdoc.inter.IAuditPrintdocBuzheng;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 材料补正告知书预览页面对应的后台
 *
 * @author Dong
 * @version [版本号, 2016-11-09 15:33:34]
 */
@RestController("auditprintdocbuzhengaction")
@Scope("request")
public class AuditPrintdocBuzhengAction extends BaseController {


    /**
     *
     */
    private static final long serialVersionUID = 6583839810892006285L;
    /**
     * 办件查询字段
     */
    private static final String FIELDS = "*";
    /**
     * service
     */
    @Autowired
    private IAuditPrintdocBuzheng iAuditPrintdocBuzheng;

    /**
     * 办件材料service
     */
    @Autowired
    private IHandleMaterial materialservice;


    /**
     * 通用受理通知书service
     */


    /**
     * 通用通知书service
     */
    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 文书快照service
     */
    @Autowired
    private IAuditProjectDocSnap auditProjectDocsnapService;

    /**
     * 注册用户通知消息service
     */
    @Autowired
    IAuditProjectNotify projectNotifyService;

    /**
     * 情形材料
     */
    @Autowired
    IAuditTaskMaterialCase auditTaskMaterialCaseService;

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
    private AuditTask audittask;


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

    /**
     * sdkind下拉列表modal
     */
    private List<SelectItem> sdkindModal = null;

    /**
     * sdaddress下拉列表modal
     */
    private List<SelectItem> sdaddressModal = null;

    private String txtmaterial;

    /**
     * 所有选中的材料
     */
    private String materialbuf = "";

    private String materialbufguid = "";

    /**
     * 材料显示的标志位
     */

    private String txtmaterialisshow;

    @Autowired
    private IAuditProjectMaterial projectMaterialService;

    @Autowired
    private IHandleSPIMaterial spiMaterialService;

    @Autowired
    private IAuditSpISubapp auditSpISubappService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private ICodeItemsService codeItemsService;

    private String area = "";

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

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
        }
        // processVersionInstanceGuid
        // =getRequestParameter("ProcessVersionInstanceGuid");
        auditprintdocbuzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid).getResult();
        if (auditprintdocbuzheng == null) {
            auditprintdocbuzheng = new AuditPrintdocBuzheng();
            auditprintdocbuzheng.setOperatedate(new Date());
            auditprintdocbuzheng.setOperateusername(userSession.getDisplayName());
        }
        List<JSONObject> rootlist = new ArrayList<JSONObject>();
        JSONObject root = new JSONObject();
        if (!isPostback()) {
            // 办件信息
            auditproject = auditProjectService
                    .getAuditProjectByRowGuid(FIELDS, projectguid, "")
                    .getResult();
            // 事项信息
            if (auditproject != null) {
                auditprintdocbuzheng.setTaskname(auditproject.getProjectname());
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
                tasktype = codeItemsService.getItemTextByCodeName("审批类别", audittask.getShenpilb());
            }

            // 日期
            //取补正时间
            SqlConditionUtil conditionUtil = new SqlConditionUtil();
            conditionUtil.eq("projectguid", auditproject.getRowguid());
            //补正状态
            conditionUtil.eq("operatetype", "22");
            AuditProjectOperation operation = iAuditProjectOperation.getAuditOperationByCondition(conditionUtil.getMap()).getResult();
            if (operation != null && StringUtil.isNotBlank(operation.getOperatedate())) {
                dyrq = EpointDateUtil.convertDate2String(auditprintdocbuzheng.getDyrq() == null ?operation.getOperatedate(): auditprintdocbuzheng.getDyrq(), "yyyy年MM月dd日");
            }
            if (auditproject != null) {
                dyrq1 = EpointDateUtil.convertDate2String(auditproject.getApplydate(), "yyyy年MM月dd日");
            }
            operatedate = EpointDateUtil.convertDate2String(
                    (auditprintdocbuzheng.getOperatedate() == null ? new Date() : auditprintdocbuzheng.getOperatedate()),
                    "yyyy年MM月dd日");
            // 办件编号的前缀 ZJG
            String AS_FLOWSN_PRE = handleConfigService
                    .getFrameConfig("AS_FLOWSN_PRE", ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            // 获取方式信息
            lxr = StringUtil.isBlank(auditprintdocbuzheng.getLxr()) ? userSession.getDisplayName()
                    : auditprintdocbuzheng.getLxr();
            lxfsinfo = "联系人：" + (StringUtil.isBlank(lxr) ? userSession.getDisplayName() : lxr);
            if (audittask != null) {
                lxfsinfo += "&nbsp;&nbsp;&nbsp;联系电话："
                        + (StringUtil.isBlank(audittask.getLink_tel()) ? "" : audittask.getLink_tel())
                        + "&nbsp;&nbsp;&nbsp;";
                lxfsinfo += "监督电话："
                        + (StringUtil.isBlank(audittask.getSupervise_tel()) ? "" : audittask.getSupervise_tel());
            }

            // 获取时间年份 ZJG201405140002 2014
            Calendar calendar = Calendar.getInstance();
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String fyear = AS_FLOWSN_PRE + year; // ZJG2014
            // 文号 〔2015〕2号
            String orgwn = "";
            if (auditproject != null) {
                orgwn = auditproject.getOrgwn();
                if (StringUtil.isBlank(orgwn)) {
                    // 获取新文书号
                    int newbhinfo = auditProjectService.getMaxOrgNumberinfo(null, projectguid,
                            null, auditproject.getTask_id()).getResult();
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
                txtbh = StringUtil.isBlank(auditprintdocbuzheng.getBh()) ? numberwh : auditprintdocbuzheng.getBh();
            }
            opinion = StringUtil.isBlank(auditprintdocbuzheng.getOpinion()) ? "" : auditprintdocbuzheng.getOpinion();
            // 办件文书快照
            List<AuditProjectDocSnap> docsnaplist = auditProjectDocsnapService
                    .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
            if (docsnaplist.isEmpty()) {
                // 无情形
                String taskcaseguid = auditproject.getTaskcaseguid();
                List<Record> projectMaterial = new ArrayList<>();
                if (StringUtil.isBlank(taskcaseguid)) {
                    // 找出所有的材料
                    projectMaterial = materialservice
                            .getProjectMaterial(auditproject.getRowguid(), auditproject.getBiguid()).getResult();
                }

                // 提交方式为电子和纸质的，必须是两个都提交了才算提交，否则都算未提交。
                // 其余提交方式，只需要提交一个，就说明提交了。
                int a = 0;
                for (int i = 0; i < projectMaterial.size(); i++) {
                    if (String.valueOf(WorkflowKeyNames9.SubmitType_Submit_And_PaperSubmit)
                            .equals(projectMaterial.get(i).get("SUBMITTYPE"))) {
                        if (!String.valueOf(WorkflowKeyNames9.MaterialInstanceStatus_Submited_And_PaperSubmited)
                                .equals(projectMaterial.get(i).get("STATUS"))) {
                            try {
                                JSONObject map1 = new JSONObject();
                                a++;
                                map1.put("materialname", projectMaterial.get(i).get("MATERIALNAME").toString());
                                map1.put("materialguid", projectMaterial.get(i).get("TASKMATERIALGUID").toString());
                                rootlist.add(map1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        if (String.valueOf(WorkflowKeyNames9.MaterialInstanceStatus_UnSubmited)
                                .equals(projectMaterial.get(i).get("STATUS"))) {
                            try {
                                JSONObject map2 = new JSONObject();
                                a++;
                                map2.put("materialname", projectMaterial.get(i).get("MATERIALNAME").toString());
                                map2.put("materialguid", projectMaterial.get(i).get("TASKMATERIALGUID").toString());
                                rootlist.add(map2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    root.put("material", rootlist);
                    root.put("total", a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                AuditPrintdocBuzheng buzheng = iAuditPrintdocBuzheng.getDocBuzhengByProjectGuid(projectguid)
                        .getResult();
                txtmaterial = buzheng != null ? buzheng.getMaterials() : "";
                txtmaterialisshow = "1";
            }

            //取补正时间
           if (operation != null && StringUtil.isNotBlank(operation.getOperatedate())) {
                dyrq = EpointDateUtil.convertDate2String(operation.getOperatedate(), "yyyy年MM月dd日");
            }
            
            addViewData("dyrq", dyrq);// 打印日期
            addViewData("lxr", lxr);// 联系人
            addViewData("txtbh", txtbh);// 办号
            addViewData("materialbuf", materialbuf);
            addViewData("materialbufguid", materialbufguid);
            addViewData("operatedate", operatedate);
        } else {
            operatedate = getViewData("operatedate");
            //取补正时间
            SqlConditionUtil conditionUtil = new SqlConditionUtil();
            conditionUtil.eq("projectguid", auditproject.getRowguid());
            //补正状态
            conditionUtil.eq("operatetype", "22");
            AuditProjectOperation operation = iAuditProjectOperation.getAuditOperationByCondition(conditionUtil.getMap()).getResult();
            if (operation != null && StringUtil.isNotBlank(operation.getOperatedate())) {
                dyrq = EpointDateUtil.convertDate2String(operation.getOperatedate(), "yyyy年MM月dd日");
            } else {
                dyrq = getViewData("dyrq");
            }
            lxr = getViewData("lxr");
            txtbh = getViewData("txtbh");
            materialbuf = getViewData("materialbuf");
            materialbufguid = getViewData("materialbufguid");
        }
        this.addCallbackParam("msg", root.toString());
        this.addCallbackParam("txtmaterialisshow", txtmaterialisshow);
        this.addCallbackParam("sdaddress", auditprintdocbuzheng.getSdaddress());
        this.addCallbackParam("sdkind", auditprintdocbuzheng.getSdkind());


        String materialnames = "";
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
    }

    /*
     * 保存修改
     *
     */
    public void save() {
        auditprintdocbuzheng.setOperatedate(new Date());
        iAuditPrintdocBuzheng.update(auditprintdocbuzheng);
        addCallbackParam("msg", "修改成功！");
    }

    /**
     * 材料选择触发事件
     */
    public void materialsListener(String title, String guid, String flag) {
        // 当前点击条目
        if ("true".equals(flag)) {
            materialbuf += title + ";";
            materialbufguid += guid + ";";
            addViewData("materialbuf", materialbuf);
            addViewData("materialbufguid", materialbufguid);
        } else {
            String[] arrtitle = materialbuf.split(";");
            String[] arrguid = materialbufguid.split(";");

            StringBuffer newmaterialbuf = new StringBuffer();
            StringBuffer newmaterialbufguid = new StringBuffer();
            for (int i = 0; i < arrguid.length; i++) {
                if (!guid.equals(arrguid[i])) {
                    newmaterialbuf.append(arrtitle[i] + ";");
                    newmaterialbufguid.append(arrguid[i] + ";");
                }
            }
            materialbuf = newmaterialbuf.toString();
            materialbufguid = newmaterialbufguid.toString();
            addViewData("materialbuf", materialbuf);
            addViewData("materialbufguid", materialbufguid);
        }
    }

    /**
     * 保存文书信息
     */
    @SuppressWarnings("unused")
    public void updateSave() {
        materialbuf = getViewData("materialbuf");
        materialbufguid = getViewData("materialbufguid");
        String Materialsinfo = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        int j = 1;
        if (materialbuf.length() != 0) {
            String materialstr = materialbuf.substring(0, materialbuf.length() - 1);
            String[] materialarr = materialstr.split(";");
            // 选中材料数
            int count = materialarr.length;
            for (int i = 0; i < count; i++) {
                if (count <= 1 && StringUtil.isBlank(opinion.trim())) {
                    Materialsinfo += materialarr[i] + ";" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    j++;
                } else {
                    Materialsinfo += String.valueOf(j++) + "、" + materialarr[i] + ";" + "<br>"
                            + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                }
            }
        }

        AuditProject auditProject = auditProjectService
                .getAuditProjectByRowGuid(FIELDS, projectguid, "").getResult();
        String orgwnnew;
        String wninfo = auditProject.getOrgwn();
        int n = wninfo.indexOf('〔');
        String wnumber = wninfo.substring(n);
        int numberbh = wnumber.indexOf('〕') + 1;
        orgwnnew = wnumber.substring(0, numberbh) + txtbh + "号";
        // 根据ProjectGuid判断是否存在不予批准决定书
        if (StringUtil.isNotBlank(auditprintdocbuzheng.getRowguid())) {
            auditprintdocbuzheng.setOpinion(opinion);
            List<AuditProjectDocSnap> auditProjectdocsnaplist = auditProjectDocsnapService
                    .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
            if (auditProjectdocsnaplist.isEmpty()) {
                // auditprintdocbuzheng.setMaterials(Materialsinfo);
                auditprintdocbuzheng.setBznumber(String.valueOf(j));
            }
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
//            auditprintdocbuzheng.setDyrq(EpointDateUtil.convertString2Date(str, "yyyy-MM-dd"));
            auditprintdocbuzheng.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocbuzheng.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocbuzheng.setLxr(lxr);
            auditprintdocbuzheng.setBh(txtbh);
            iAuditPrintdocBuzheng.update(auditprintdocbuzheng);
        } else {
            auditprintdocbuzheng.setRowguid(UUID.randomUUID().toString());
            auditprintdocbuzheng.setProjectguid(projectguid);
            auditprintdocbuzheng.setOpinion(opinion);
            List<AuditProjectDocSnap> auditProjectdocsnaplist = auditProjectDocsnapService
                    .selectByProjectguidAndDoctype(projectguid, ZwfwConstant.DOC_TYPE_CLBZGZS).getResult();
            if (auditProjectdocsnaplist.isEmpty()) {
                // auditprintdocbuzheng.setMaterials(Materialsinfo);
                auditprintdocbuzheng.setBznumber(String.valueOf(j));
            }
            // auditprintdocbuzheng.setMaterials(Materialsinfo);
//            String str = dyrq.replace("年", "-");
//            str = str.replace("月", "-");
//            str = str.replace("日", "");
            auditprintdocbuzheng.setDyrq(EpointDateUtil.convertString2Date(dyrq, "yyyy年MM月dd日"));
            auditprintdocbuzheng.setOperatedate(EpointDateUtil.convertString2Date(operatedate, "yyyy年MM月dd日"));
            auditprintdocbuzheng.setLxr(lxr);
            auditprintdocbuzheng.setBh(txtbh);
            iAuditPrintdocBuzheng.add(auditprintdocbuzheng);
        }

        auditProject.setOrgnumber(Integer.parseInt(txtbh));
        auditProject.setOrgwn(orgwnnew);
        auditProjectService.updateProject(auditProject);

        // 插入在线通知
        String notirytitle = "【材料补正】" + "<" + auditProject.getProjectname() + ">";
        String handurl = ""; // 处理页面地址
        projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
                notirytitle,
                ((StringUtil.isBlank(txtmaterial) ? "" : txtmaterial)
                        + (StringUtil.isBlank(opinion) ? "" : "(" + opinion + ")")),
                handurl, ZwfwConstant.CLIENTTYPE_BJ, auditProject.getRowguid(),
                String.valueOf(auditProject.getStatus()), userSession.getUserGuid(), userSession.getDisplayName());

        if (StringUtil.isNotBlank(auditprintdocbuzheng.getMaterials())) {
            // 更新材料审核状态
            String[] materials = auditprintdocbuzheng.getMaterials().split(",");
            for (String material : materials) {
                if (StringUtil.isNotBlank(material)) {
                    projectMaterialService.updateProjectMaterialAuditStatus(material,
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_DBZ), projectguid);
                }
            }

            // 并联审批更新补正状态
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                spiMaterialService.updateIMaterialBuzheng(auditProject.getSubappguid(),
                        auditprintdocbuzheng.getMaterials(), projectguid);
                auditSpISubappService.updateSubapp(auditProject.getSubappguid(), ZwfwConstant.LHSP_Status_DBJ, null);
            }
        }
    }

    /**
     * 打印
     */
    public void updateClick() {
        updateSave();
        String printaddress = "pauditprintdocbuzheng?projectguid=" + projectguid + "&ProcessVersionInstanceGuid="
                + processVersionInstanceGuid + "&taskguid=" + audittask.getRowguid();
        addCallbackParam("msg", printaddress);
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

    public String getMaterialbuf() {
        return materialbuf;
    }

    public void setMaterialbuf(String materialbuf) {
        this.materialbuf = materialbuf;
    }

    public String getMaterialbufguid() {
        return materialbufguid;
    }

    public void setMaterialbufguid(String materialbufguid) {
        this.materialbufguid = materialbufguid;
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

//	public List<SelectItem> getMaterialsModal() {
//		if (materialsModal == null) {
//			materialsModal = new ArrayList<>();
//			String audittaskmaterialcase = "";
//			// 无情形
//			String taskcaseguid = auditproject.getTaskcaseguid();
//			List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
//					.selectTaskMaterialCaseByCaseGuid(taskcaseguid).getResult();
//			if (StringUtil.isNotBlank(taskcaseguid)) {
//				for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
//					audittaskmaterialcase += auditTaskMaterialCase.getMaterialguid() + ';';
//				}
//			}
//			List<AuditProjectMaterial> projectMaterials = projectMaterialService.selectProjectMaterial(projectguid)
//					.getResult();
//			int index = 1;
//			if (projectMaterials != null) {
//				for (AuditProjectMaterial auditProjectMaterial : projectMaterials) {
//
//					if (StringUtil.isNotBlank(taskcaseguid)) {
//						if (audittaskmaterialcase.contains(auditProjectMaterial.getTaskmaterialguid())) {
//							materialsModal.add(new SelectItem(auditProjectMaterial.getRowguid(),
//									String.valueOf(index) + "、" + auditProjectMaterial.getTaskmaterial()));
//							index++;
//						}
//					} else {
//						materialsModal.add(new SelectItem(auditProjectMaterial.getRowguid(),
//								String.valueOf(index) + "、" + auditProjectMaterial.getTaskmaterial()));
//						index++;
//					}
//				
//				}
//			}
//		}
//		return this.materialsModal;
//	}


    public void setMaterialsModal(List<SelectItem> materialsModal) {
    }


    public String getOperatedate() {
        return operatedate;
    }

    public void setOperatedate(String operatedate) {
        this.operatedate = operatedate;
    }

}
