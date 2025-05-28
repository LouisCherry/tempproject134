package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.audittaskzj.action.AuditSpDetailAction;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.security.TokenUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.ISpglQypgsxxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgsxxxb.api.entity.SpglQypgsxxxb;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.ISpglQypgxxbService;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;
import com.epoint.xmz.thirdreporteddata.util.Office365Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 并联审批查看页面对应的后台
 */
@RestController("gxhauditspdetailaction")
@Scope("request")
public class GxhAuditSpDetailAction extends AuditSpDetailAction {
    @Autowired
    private IAuditProject auditProjectImpl;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditTask audittaskimpl;
    @Autowired
    private IAuditTaskResult iaudittaskresult;
    @Autowired
    private IAuditRsItemBaseinfo auditRsItemBaseinfoImpl;
    @Autowired
    private ISpglQypgxxbService iSpglQypgxxbService;
    @Autowired
    private ISpglQypgsxxxbService iSpglQypgsxxxbService;
    @Autowired
    private IAuditProjectMaterial auditProjectMaterialImpl;
    @Autowired
    private IAuditTaskMaterial auditTaskMaterialImpl;
    @Autowired
    private ICodeItemsService icodeitemsservice;
    @Autowired
    private IOuService iouservice;

    @Autowired
    private IAuditProjectOperation iauditprojectoperation;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private ICertInfoExternal certInfoExternalService;

    /**
     * 获取流程版本实例操作API（注意：TCC主业务服务中不允许调用）
     */
    @Autowired
    private IWFInstanceAPI9 instanceapi;
    /**
     * 获取流程流转页面信息API（注意：TCC主业务服中不允许调用）
     */
    @Autowired
    private IWFInitPageAPI9 initapi;

    /**
     * 获取工作流流程定义信息API
     */
    @Autowired
    private IWFDefineAPI9 defineapi;

    /**
     * 文书快照service
     */
    @Autowired
    private IAuditProjectDocSnap auditProjectDocsnapService;

    @Autowired
    private ICertAttachExternal certAttachService;

    @Override
    public void getFlowData() {
        String flowsn = getRequestParameter("flowsn");
        AuditProject auditproject = auditProjectImpl.getAuditProjectByFlowsn(flowsn, null).getResult();
        JSONObject returnobj = new JSONObject();
        JSONArray arrayobj = new JSONArray();
        returnobj.put("flist", arrayobj);
        String rootPath =
                request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
        String fileurl = rootPath + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
        int index = 0;
        // 已接件，查人接件信息
        if (auditproject.getReceivedate() != null) {
            JSONObject obj = new JSONObject();
            obj.put("key", index + 1);
            obj.put("hjname", "接件");
            obj.put("dealperson", auditproject.getReceiveusername());
            obj.put("dealdate", auditproject.getReceivedate());
            obj.put("operation", "接件");
            obj.put("note", "");
            obj.put("flowfile", "");
            obj.put("returnfile", "");
            obj.put("dealmessage", "");
            arrayobj.add(obj);
        }
        // 判断事项是否是即办件
        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditproject.getTasktype()))) {
            // 判断是否受理
            if (auditproject.getAcceptuserdate() != null) {
                JSONObject obj = new JSONObject();
                obj.put("key", index + 1);
                obj.put("hjname", "受理");
                obj.put("dealperson", auditproject.getAcceptusername());
                obj.put("dealdate", auditproject.getAcceptuserdate());
                obj.put("operation", "受理");
                obj.put("note", "处理完成！");
                obj.put("flowfile", "");
                List<AuditProjectDocSnap> list = auditProjectDocsnapService.selectByProjectguidAndDoctype(
                        auditproject.getRowguid(), ZwfwConstant.DOC_TYPE_SLTZS).getResult();
                if (EpointCollectionUtils.isNotEmpty(list)) {
                    AuditProjectDocSnap auditprojectdocsnap = list.get(0);
                    StringBuffer str = new StringBuffer();
                    str.append("<a href='").append(fileurl).append(auditprojectdocsnap.getPdfattachguid()).append("'>")
                            .append("受理通知书").append("</a>");
                    str.append("<br/>");
                    str.append("【").append(EpointDateUtil.convertDate2String(auditprojectdocsnap.getOperatedate()))
                            .append("生成文书").append("】");
                    obj.put("returnfile", str);
                }
                obj.put("dealmessage", "");
                arrayobj.add(obj);
            }
            if (auditproject.getBanjiedate() != null) {
                JSONObject obj = new JSONObject();
                obj.put("key", index + 1);
                obj.put("hjname", "办结");
                obj.put("dealperson", auditproject.getBanjieusername());
                obj.put("dealdate", auditproject.getBanjiedate());
                obj.put("operation", "办结");
                obj.put("note", "处理完成！");
                obj.put("returnfile", "");
                obj.put("flowfile", "");
                obj.put("dealmessage", "");
                AuditTaskResult audittaskresult = iaudittaskresult.getAuditResultByTaskGuid(auditproject.getTaskguid(),
                        false).getResult();
                if (audittaskresult != null) {
                    obj.put("resultname", audittaskresult.getResultname());
                }
                arrayobj.add(obj);
            }

        } else {
            if (StringUtil.isNotBlank(auditproject.getPviguid())) {
                List<Map<String, Object>> worklflowitemlist = historyCtrl(auditproject.getPviguid());
                for (Map<String, Object> map : worklflowitemlist) {
                    // 去除微未操作的 流程
                    if (map.get("operationdate") != null) {
                        JSONObject obj = new JSONObject();
                        obj.put("key", index + 1);
                        obj.put("hjname", map.get("activityname"));
                        obj.put("dealperson", map.get("username"));
                        obj.put("dealdate", new Date((long) map.get("operationdate")));
                        obj.put("operation", map.get("operationname"));
                        obj.put("note", map.get("opinion"));
                        List<FrameAttachInfo> listattach = attachservice.getAttachInfoListByGuid(
                                map.get("activityinstanceguid").toString());
                        StringBuffer str = new StringBuffer();
                        if (listattach != null && !listattach.isEmpty()) {
                            for (FrameAttachInfo frameAttachInfo : listattach) {
                                str.append("<a href='").append(fileurl).append(frameAttachInfo.getAttachGuid())
                                        .append("'>").append(frameAttachInfo.getAttachFileName()).append("</a>");
                                str.append("<br/>");
                            }
                        }
                        // activityinstanceguid查看有无附件
                        obj.put("flowfile", str.toString());
                        obj.put("returnfile", "");
                        // 如果是受理，查询受理通知书
                        if ("受理".equals(map.get("operationname"))) {
                            List<AuditProjectDocSnap> list = auditProjectDocsnapService.selectByProjectguidAndDoctype(
                                    auditproject.getRowguid(), ZwfwConstant.DOC_TYPE_SLTZS).getResult();
                            if (list != null && !list.isEmpty()) {
                                AuditProjectDocSnap auditprojectdocsnap = list.get(0);
                                str.setLength(0);
                                str.append("<a href='").append(fileurl).append(auditprojectdocsnap.getPdfattachguid())
                                        .append("'>").append("受理通知书").append("</a>");
                                str.append("<br/>");
                                str.append("【")
                                        .append(EpointDateUtil.convertDate2String(auditprojectdocsnap.getOperatedate()))
                                        .append("生成文书").append("】");
                                obj.put("returnfile", str);
                            }
                        }
                        if ("办结".equals(map.get("operationname"))) {
                            AuditTaskResult audittaskresult = iaudittaskresult.getAuditResultByTaskGuid(
                                    auditproject.getTaskguid(), false).getResult();
                            if (audittaskresult != null) {
                                obj.put("resultname", audittaskresult.getResultname());
                            }
                        }
                        obj.put("dealmessage", "");
                        arrayobj.add(obj);
                    }
                }
            }
        }
        addCallbackParam("data", returnobj);
    }

    /**
     * 获取批文批复List
     */
    public void getResultList() {
        String flowsn = getRequestParameter("flowsn");
        AuditProject auditproject = auditProjectImpl.getAuditProjectByFlowsn(flowsn, null).getResult();
        if (auditproject == null) {
            return;
        }
        AuditTaskResult audittaskresult = iaudittaskresult.getAuditResultByTaskGuid(auditproject.getTaskguid(), false)
                .getResult();
        List<JSONObject> list = new ArrayList<>();
        JSONObject obj = new JSONObject();
        // 序
        obj.put("i", 1);
        if (audittaskresult != null) {
            obj.put("resultname", audittaskresult.getResultname());
        }
        obj.put("resulttype", "电子批文");
        obj.put("ouname", auditproject.getOuname());
        obj.put("publishdate", auditproject.getCertificatedate());
        obj.put("iscert", ZwfwConstant.CONSTANT_INT_ZERO);
        obj.put("certurl", "");
        list.add(obj);
        addCallbackParam("resultlist", list);
    }

    /**
     * 获取区域评估List(项目相关)
     */
    public void getQypginfoList() {
        String itemcode = getRequestParameter("itemcode");
        AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByItemcode(itemcode)
                .getResult();
        String preItemGuid;
        if (auditRsItemBaseinfo != null) {
            String parentid = auditRsItemBaseinfo.getParentid();
            if (StringUtil.isNotBlank(parentid)) {
                auditRsItemBaseinfo = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByRowguid(parentid).getResult();
            }
            preItemGuid = auditRsItemBaseinfo.getRowguid();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setLeftJoinTable("spgl_qypgxxb_edit_r b", "a.rowguid", "b.qypgguid");
            sql.eq("b.pre_itemguid", preItemGuid);
            sql.setSelectFields("a.*,b.rowguid rguid,b.subappguid");
            PageData<SpglQypgxxb> pageData = iSpglQypgxxbService.getAuditSpDanitemByPage(sql.getMap(), -1, -1,
                    "b.createdate", "desc").getResult();
            List<SpglQypgxxb> spglQypgxxbList = pageData.getList();
            List<JSONObject> list = new ArrayList<>();
            for (SpglQypgxxb spglQypgxxb : spglQypgxxbList) {
                JSONObject qypginfo = new JSONObject();
                qypginfo.put("qypgqymc", spglQypgxxb.getQypgqymc());
                qypginfo.put("qypgguid", spglQypgxxb.getRowguid());
                qypginfo.put("itemname", auditRsItemBaseinfo.getItemname());
                sql.clear();
                sql.eq("qypgguid", spglQypgxxb.getRowguid());
                List<SpglQypgsxxxb> sxList = iSpglQypgsxxxbService.getListByMap(sql.getMap());
                List<JSONObject> taskList = new ArrayList<>();
                for (SpglQypgsxxxb spglQypgsxxxb : sxList) {
                    JSONObject qypgtask = new JSONObject();
                    qypgtask.put("taskguid", spglQypgsxxxb.getRowguid());
                    qypgtask.put("qypgsxmc", spglQypgsxxxb.getQypgsxmc());
                    qypgtask.put("qypgcgsxrq", EpointDateUtil.convertDate2String(spglQypgsxxxb.getQypgcgsxrq()));
                    qypgtask.put("qypgcgjzrq", EpointDateUtil.convertDate2String(spglQypgsxxxb.getQypgcgjzrq()));
                    qypgtask.put("jhspdfs", spglQypgsxxxb.getJhspdfs());
                    qypgtask.put("jhspdfs_cn",
                            icodeitemsservice.getItemTextByCodeName("国标_应用区域评估成果简化审批方式",
                                    spglQypgsxxxb.getJhspdfs()));
                    String cliengguid = spglQypgsxxxb.getCliengguid();
                    if (StringUtil.isNotBlank(cliengguid)) {
                        List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengguid);
                        if (EpointCollectionUtils.isNotEmpty(attachInfoList)) {
                            FrameAttachInfo frameAttachInfo = attachInfoList.get(0);
                            qypgtask.put("yulanurl", Office365Util.get365Url(frameAttachInfo));
                            qypgtask.put("downloadurl",
                                    WebUtil.getRequestCompleteUrl(request) + "/" + iAttachService.getAttachDownPath(
                                            frameAttachInfo));
                        }
                    }
                    taskList.add(qypgtask);
                }
                qypginfo.put("qypgtasklist", taskList);
                qypginfo.put("tasktotal", taskList.size());
                list.add(qypginfo);
            }
            addCallbackParam("qypginfolist", list);
        }
    }

    /**
     * 获取区域评估List(所有)
     */
    public void getAllQypginfoList() {
        SqlConditionUtil sql = new SqlConditionUtil();
        List<SpglQypgxxb> spglQypgxxbList = iSpglQypgxxbService.getListByMap(new HashMap<>());
        List<JSONObject> list = new ArrayList<>();
        for (SpglQypgxxb spglQypgxxb : spglQypgxxbList) {
            JSONObject qypginfo = new JSONObject();
            qypginfo.put("qypgqymc", spglQypgxxb.getQypgqymc());
            qypginfo.put("qypgguid", spglQypgxxb.getRowguid());
            sql.clear();
            sql.eq("qypgguid", spglQypgxxb.getRowguid());
            List<SpglQypgsxxxb> sxList = iSpglQypgsxxxbService.getListByMap(sql.getMap());
            List<JSONObject> taskList = new ArrayList<>();
            for (SpglQypgsxxxb spglQypgsxxxb : sxList) {
                JSONObject qypgtask = new JSONObject();
                qypgtask.put("taskguid", spglQypgsxxxb.getRowguid());
                qypgtask.put("qypgsxmc", spglQypgsxxxb.getQypgsxmc());
                qypgtask.put("qypgcgsxrq", EpointDateUtil.convertDate2String(spglQypgsxxxb.getQypgcgsxrq()));
                qypgtask.put("qypgcgjzrq", EpointDateUtil.convertDate2String(spglQypgsxxxb.getQypgcgjzrq()));
                qypgtask.put("jhspdfs", spglQypgsxxxb.getJhspdfs());
                qypgtask.put("jhspdfs_cn", icodeitemsservice.getItemTextByCodeName("国标_应用区域评估成果简化审批方式",
                        spglQypgsxxxb.getJhspdfs()));
                String cliengguid = spglQypgsxxxb.getCliengguid();
                if (StringUtil.isNotBlank(cliengguid)) {
                    List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengguid);
                    if (EpointCollectionUtils.isNotEmpty(attachInfoList)) {
                        FrameAttachInfo frameAttachInfo = attachInfoList.get(0);
                        qypgtask.put("yulanurl", Office365Util.get365Url(frameAttachInfo));
                        qypgtask.put("downloadurl",
                                WebUtil.getRequestCompleteUrl(request) + "/" + iAttachService.getAttachDownPath(
                                        frameAttachInfo));
                    }
                }
                taskList.add(qypgtask);
            }
            qypginfo.put("qypgtasklist", taskList);
            qypginfo.put("tasktotal", taskList.size());
            list.add(qypginfo);
        }
        addCallbackParam("qypginfolist", list);
    }

    /**
     * 办件数据的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @Override
    public void getBaseinfo() {
        try {
            String flowsn = getRequestParameter("flowsn");
            String itemcode = getRequestParameter("itemcode");
            AuditProject auditproject = auditProjectImpl.getAuditProjectByFlowsn(flowsn, null).getResult();
            if (auditproject == null) {
                return;
            }
            AuditTask task = audittaskimpl.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
            AuditTaskResult audittaskresult = iaudittaskresult.getAuditResultByTaskGuid(auditproject.getTaskguid(),
                    false).getResult();
            AuditRsItemBaseinfo auditRsItemBaseinfo = new AuditRsItemBaseinfo();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("ITEMCODE", itemcode);
            List<AuditRsItemBaseinfo> itemlist = auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByPage(
                    AuditRsItemBaseinfo.class, sql.getMap(), 0, 0, "OperateDate", "desc").getResult().getList();
            if (itemlist != null && !itemlist.isEmpty()) {
                auditRsItemBaseinfo = itemlist.get(0);
            }
            JSONObject returnobj = new JSONObject();
            JSONObject objinfo = new JSONObject();
            JSONObject taskinfo = new JSONObject();
            JSONObject projectinfo = new JSONObject();
            JSONObject applyinfo = new JSONObject();
            JSONObject contactinfo = new JSONObject();
            returnobj.put("itemname", auditRsItemBaseinfo.getItemname());
            returnobj.put("project", objinfo);
            objinfo.put("taskinfo", taskinfo);
            objinfo.put("projectinfo", projectinfo);
            objinfo.put("applyinfo", applyinfo);
            objinfo.put("contactinfo", contactinfo);
            taskinfo.put("itemname",
                    auditRsItemBaseinfo.getItemname() + "<button class='lookXM highlights'>查看项目单</button>");
            taskinfo.put("itemcode", auditRsItemBaseinfo.getItemcode());
            taskinfo.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
            taskinfo.put("itemlegalcreditcode", auditRsItemBaseinfo.getItemlegalcreditcode());
            taskinfo.put("projecttype",
                    icodeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));
            taskinfo.put("acceptdept", iouservice.getOuNameByUserGuid(auditproject.getAcceptuserguid()));
            taskinfo.put("taskname", task.getTaskname());
            taskinfo.put("item_id", task.getItem_id());

            projectinfo.put("flowsn", auditproject.getFlowsn());
            projectinfo.put("status",
                    icodeitemsservice.getItemTextByCodeName("办件状态", String.valueOf(auditproject.getStatus())));
            projectinfo.put("istime", auditproject.getPromiseenddate() == null ? "非计时" : "计时");
            projectinfo.put("promiseday", auditproject.getPromise_day() + "个工作日");
            projectinfo.put("promiseenddate", auditproject.getPromiseenddate());
            projectinfo.put("banjiedate", auditproject.getBanjiedate());
            if (audittaskresult != null) {
                projectinfo.put("banjiename", audittaskresult.getResultname());
            }
            CertInfo cert = certInfoExternalService.getCertInfoByRowguid(auditproject.getCertrowguid());
            List<JSONObject> list = new ArrayList<>();
            List<JSONObject> list2 = new ArrayList<>();
            List<FrameAttachInfo> list3 = new ArrayList<>();
            if (cert != null) {
                if (StringUtil.isNotBlank(cert.getCertcliengguid())) {
                    list = certAttachService.getAttachList(cert.getCertcliengguid(), auditproject.getAreacode());
                }
                if (StringUtil.isNotBlank(cert.getCopycertcliengguid())) {
                    list2 = certAttachService.getAttachList(cert.getCopycertcliengguid(), auditproject.getAreacode());
                }
            } else {
                list3 = attachservice.getAttachInfoListByGuid(auditproject.getRowguid());
                // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                for (FrameAttachInfo attachInfo : list3) {
                    attachInfo.set("source", "0");
                }
            }
            List<FrameAttachInfo> listattach = new ArrayList<>();
            if (list != null && !list.isEmpty()) {
                for (JSONObject json : list) {
                    FrameAttachInfo info = new FrameAttachInfo();
                    info.setAttachFileName(json.getString("attachname"));
                    info.setUploadDateTime(json.getDate("uploaddatetime"));
                    info.setAttachGuid(json.getString("attachguid"));
                    listattach.add(info);
                }
            }
            if (list2 != null && !list2.isEmpty()) {
                for (JSONObject json : list2) {
                    FrameAttachInfo info = new FrameAttachInfo();
                    info.setAttachFileName(json.getString("attachname"));
                    info.setUploadDateTime(json.getDate("uploaddatetime"));
                    info.setAttachGuid(json.getString("attachguid"));
                    listattach.add(info);
                }
            }

            String rootPath =
                    request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
            String fileurl = rootPath + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
            StringBuffer str = new StringBuffer();
            if (listattach != null && !listattach.isEmpty()) {
                for (FrameAttachInfo frameAttachInfo : listattach) {
                    str.append("<a href='").append(uploadOfficalDoc(frameAttachInfo.getAttachGuid(), auditproject))
                            .append("'>").append(frameAttachInfo.getAttachFileName()).append("</a>");
                    str.append("<br/>");
                }
            }
            if (list3 != null && !list3.isEmpty()) {
                for (FrameAttachInfo frameAttachInfo : list3) {
                    str.append("<a href='").append(fileurl).append(frameAttachInfo.getAttachGuid()).append("'>")
                            .append(frameAttachInfo.getAttachFileName()).append("</a>");
                    str.append("<br/>");
                }
            }
            String attach = str.toString();
            if (attach.length() > 0) {
                attach = attach.substring(0, attach.lastIndexOf("<br/>"));
            }
            projectinfo.put("files", attach);

            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("projectguid", auditproject.getRowguid());
            sqlc.setOrderDesc("operatedate");
            AuditProjectOperation operate = iauditprojectoperation.getAuditOperationByCondition(sqlc.getMap())
                    .getResult();
            String operatename = "";
            if (operate != null) {
                switch (operate.getOperateType()) {
                    case ZwfwConstant.OPERATE_YSTG:
                        operatename = "预审通过";
                        break;
                    case ZwfwConstant.OPERATE_YSDH:
                        operatename = "预审打回";
                        break;
                    case ZwfwConstant.OPERATE_BC:
                        operatename = "保存";
                        break;
                    case ZwfwConstant.OPERATE_JJ:
                        operatename = "接件";
                        break;
                    case ZwfwConstant.OPERATE_BZ:
                        operatename = "补正";
                        break;
                    case ZwfwConstant.OPERATE_SL:
                        operatename = "受理";
                        break;
                    case ZwfwConstant.OPERATE_BYSL:
                        operatename = "不予受理";
                        break;
                    case ZwfwConstant.OPERATE_BJSF:
                        operatename = "办件收费";
                        break;
                    case ZwfwConstant.OPERATE_SQ:
                        operatename = "收讫";
                        break;
                    case ZwfwConstant.OPERATE_BJSFQX:
                        operatename = "办件收费取消";
                        break;
                    case ZwfwConstant.OPERATE_SPTG:
                        operatename = "审批通过";
                        break;
                    case ZwfwConstant.OPERATE_SPBTG:
                        operatename = "审批不通过";
                        break;
                    case ZwfwConstant.OPERATE_ZTJS:
                        operatename = "暂停计时";
                        break;
                    case ZwfwConstant.OPERATE_HFJS:
                        operatename = "恢复计时";
                        break;
                    case ZwfwConstant.OPERATE_PZ:
                        operatename = "批准";
                        break;
                    case ZwfwConstant.OPERATE_BYPZ:
                        operatename = "不予批准";
                        break;
                    case ZwfwConstant.OPERATE_BJ:
                        operatename = "办结";
                        break;
                    case ZwfwConstant.OPERATE_CXSQ:
                        operatename = "撤销申请";
                        break;
                    case ZwfwConstant.OPERATE_YCZZ:
                        operatename = "异常终止";
                        break;
                    case ZwfwConstant.OPERATE_BJYQ:
                        operatename = "办件延期";
                        break;
                    case ZwfwConstant.OPERATE_YQSHBTG:
                        operatename = "办件延期审核不通过";
                        break;
                    case ZwfwConstant.OPERATE_YQSHTG:
                        operatename = "办件延期审核通过";
                        break;
                    case ZwfwConstant.OPERATE_JGFF:
                        operatename = "结果发放";
                        break;
                    case ZwfwConstant.OPERATE_PJ:
                        operatename = "评价";
                        break;
                    default:
                        break;
                }
                projectinfo.put("newoperate", operatename);
                projectinfo.put("newoperatedate", operate.getOperatedate());
            }

            projectinfo.put("applydate", auditproject.getApplydate());
            projectinfo.put("djdate", auditproject.getOperatedate());
            projectinfo.put("acceptdate", auditproject.getAcceptuserdate());

            applyinfo.put("applysource",
                    icodeitemsservice.getItemTextByCodeName("申请方式", String.valueOf(auditproject.getApplyway())));
            contactinfo.put("contactperson", auditproject.getContactperson());
            contactinfo.put("contactcertnum", auditproject.getContactcertnum());
            contactinfo.put("contactphone", auditproject.getContactphone());
            contactinfo.put("contactmobile", "");

            this.addCallbackParam("data", returnobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMaterialList(String buzheng) {
        try {
            String flowsn = getRequestParameter("flowsn");
            JSONObject returnobj = new JSONObject();
            JSONObject mList = new JSONObject();
            JSONObject sharemList = new JSONObject();
            returnobj.put("mlist", mList);
            returnobj.put("sharemlist", sharemList);
            JSONArray list = new JSONArray();
            JSONArray list1 = new JSONArray();
            AuditProject auditproject = auditProjectImpl.getAuditProjectByFlowsn(flowsn, null).getResult();
            String taskname = "";
            if (auditproject != null) {
                AuditTask task = audittaskimpl.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
                taskname = task.getTaskname();
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("projectguid", auditproject.getRowguid());
                if (StringUtil.isNotBlank(buzheng)) {
                    sql.eq("isbuzheng", buzheng);
                }
                List<AuditProjectMaterial> materialList = auditProjectMaterialImpl.getProjectMaterialPageData(
                        sql.getMap(), 0, 0, "operatedate", "desc").getResult().getList();
                if (materialList != null && !materialList.isEmpty()) {
                    for (AuditProjectMaterial material : materialList) {
                        JSONObject materialJSON = new JSONObject();
                        materialJSON.put("mname", material.getTaskmaterial());
                        AuditTaskMaterial taskMaterial = auditTaskMaterialImpl.getAuditTaskMaterialByRowguid(
                                material.getTaskmaterialguid()).getResult();
                        materialJSON.put("filesource",
                                icodeitemsservice.getItemTextByCodeName("来源渠道", taskMaterial.getFile_source()));
                        switch (taskMaterial.getSubmittype()) {
                            case "10":
                                materialJSON.put("papernum1", ZwfwConstant.CONSTANT_STR_ZERO);
                                materialJSON.put("dnum1", ZwfwConstant.CONSTANT_STR_ONE);
                                break;
                            case "20":
                                materialJSON.put("papernum1", ZwfwConstant.CONSTANT_STR_ONE);
                                materialJSON.put("dnum1", ZwfwConstant.CONSTANT_STR_ZERO);
                                break;
                            case "35":
                            case "40":
                                materialJSON.put("papernum1", ZwfwConstant.CONSTANT_STR_ONE);
                                materialJSON.put("dnum1", ZwfwConstant.CONSTANT_STR_ONE);
                                break;
                            default:
                                materialJSON.put("papernum1", ZwfwConstant.CONSTANT_STR_ZERO);
                                materialJSON.put("dnum1", ZwfwConstant.CONSTANT_STR_ZERO);
                                break;
                        }
                        String status = "";
                        switch (material.getStatus()) {
                            case 10:
                                status = "未提交";
                                materialJSON.put("papernum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                materialJSON.put("dnum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                break;
                            case 15:
                                status = "纸质材料";
                                materialJSON.put("papernum2", ZwfwConstant.CONSTANT_STR_ONE);
                                materialJSON.put("dnum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                break;
                            case 20:
                                status = "电子材料";
                                materialJSON.put("papernum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                materialJSON.put("dnum2", ZwfwConstant.CONSTANT_STR_ONE);
                                break;
                            case 25:
                                status = "纸质和电子材料";
                                materialJSON.put("papernum2", ZwfwConstant.CONSTANT_STR_ONE);
                                materialJSON.put("dnum2", ZwfwConstant.CONSTANT_STR_ONE);
                                break;
                            default:
                                materialJSON.put("papernum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                materialJSON.put("dnum2", ZwfwConstant.CONSTANT_STR_ZERO);
                                break;
                        }
                        materialJSON.put("status", status);
                        List<FrameAttachInfo> listattach = attachservice.getAttachInfoListByGuid(
                                material.getCliengguid());
                        String rootPath = request.getRequestURL().toString().replace(request.getRequestURI(), "")
                                + request.getContextPath();
                        String fileurl = rootPath
                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
                        StringBuffer str = new StringBuffer();
                        if (listattach != null && !listattach.isEmpty()) {
                            for (FrameAttachInfo frameAttachInfo : listattach) {
                                str.append("<a href='").append(fileurl).append(frameAttachInfo.getAttachGuid())
                                        .append("'>").append(frameAttachInfo.getAttachFileName()).append("</a>");
                                str.append("<br/>");
                            }
                        }
                        String attach = str.toString();
                        if (attach.length() > 0) {
                            attach = attach.substring(0, attach.lastIndexOf("<br/>"));
                        }
                        materialJSON.put("attach", attach);
                        if (StringUtil.isNotBlank(material.getSharematerialiguid())) {
                            materialJSON.put("key", list.size() + 1);
                            list.add(materialJSON);
                        } else {
                            materialJSON.put("key", list1.size() + 1);
                            list1.add(materialJSON);
                        }
                    }
                }
            }
            mList.put("name", "共性材料");
            mList.put("list", list);
            sharemList.put("name", taskname);
            sharemList.put("list", list1);
            this.addCallbackParam("mList", returnobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String authorize(String cmd) {
        String result = null;
        Boolean hasRight = Boolean.valueOf(false);
        String token = getRequestParameter("access_token");
        if (StringUtil.isNotBlank(token) && TokenUtil.validateToken(token)) {
            hasRight = Boolean.valueOf(true);
        }
        if (!hasRight) {
            result = "资源不存在";
        }
        hasRight = Boolean.valueOf(true);
        return result;
    }

    @Override
    public void getBZMaterialList() {
        getMaterialList(ZwfwConstant.CONSTANT_STR_ONE);
    }

}
