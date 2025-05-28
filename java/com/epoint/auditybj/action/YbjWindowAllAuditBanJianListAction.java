package com.epoint.auditybj.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditybj.api.IWFPVOP;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务管理list页面对应的后台
 *
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@SuppressWarnings("unchecked")
@RestController("ybjwindowallauditbanjianlistaction")
@Scope("request")
public class YbjWindowAllAuditBanJianListAction extends BaseController {
    private static final long serialVersionUID = 1L;
    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 事项基本信息实体对象
     */
    private AuditTask audittask = new AuditTask();
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> ismaterialModel = null;
    private List<SelectItem> banjieResultModel = null;

    /**
     * 多选列表model
     */
    private List<SelectItem> ismyprojectModel = null;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;
    /**
     * 是否上传材料
     */
    private int ismaterial;
    /**
     * 是否我的办件
     */
    private int ismyproject;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask audittaskServiec;
    @Autowired
    private IAuditOrgaWindow windowService;
    @Autowired
    private IAuditProjectUnusual projectUnusualService;

    /**
     * 申请时间
     */
    private String applydateStart;
    private String applydateEnd;
    /**
     * 受理时间
     */
    private String acceptuserdateStart;
    private String acceptuserdateEnd;
    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;

    @Autowired
    private IAuditTaskExtension audittaskExtensionServiec;

    @Autowired
    private IWFPVOP iwfpvop;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {

                /**
                 *
                 */
                private static final long serialVersionUID = -4066177571313219078L;

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = " applyDate";
                    sortOrder = "desc";
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    } else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sql.setInnerJoinTable("audit_project_operation b", "a.rowguid", "b.projectguid");
                    sql.eq("a.areacode", area);
                    if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                        sql.like("a.applyername", dataBean.getApplyername());
                    }
                    if (StringUtil.isNotBlank(dataBean.getStatus())) {
                        sql.eq("status", dataBean.getStatus().toString());
                    }
                    if (ZwfwConstant.CONSTANT_INT_ONE == ismyproject) {
                        sql.eq("acceptuserguid", UserSession.getInstance().getUserGuid());
                    }
                    if (StringUtil.isNotBlank(applydateStart)) {
                        sql.ge("applydate", EpointDateUtil.getBeginOfDateStr(applydateStart));
                    }
                    if (StringUtil.isNotBlank(applydateEnd)) {
                        sql.le("applydate", EpointDateUtil.getEndOfDateStr(applydateEnd));
                    }
                    if (StringUtil.isNotBlank(acceptuserdateStart)) {
                        sql.ge("acceptuserdate", EpointDateUtil.getBeginOfDateStr(acceptuserdateStart));
                    }
                    if (StringUtil.isNotBlank(acceptuserdateEnd)) {
                        sql.le("acceptuserdate", EpointDateUtil.getEndOfDateStr(acceptuserdateEnd));
                    }
                    if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                        sql.like("projectname", dataBean.getProjectname());
                    }
                    if (StringUtil.isNotBlank(banjiedateStart)) {
                        sql.ge("banjiedate", EpointDateUtil.getBeginOfDateStr(banjiedateStart));
                    }
                    if (StringUtil.isNotBlank(banjiedateEnd)) {
                        sql.le("banjiedate", EpointDateUtil.getEndOfDateStr(banjiedateEnd));
                    }
                    if (StringUtil.isNotBlank(dataBean.getBanjieresult())) {
                        sql.eq("banjieresult", dataBean.getBanjieresult().toString());
                    }
                    sql.in("status", "97,90");
                    String projectguids = "";

                    int count = 0;
                    String userguid = UserSession.getInstance().getUserGuid();
//                    List<String> projectguidlist = iwfpvop.getProjectguidByUserguid(userguid);
//                    Iterator<String> iterator = projectguidlist.iterator();
//                    while (iterator.hasNext()) {
//                        if (StringUtil.isBlank(iterator.next())) {
//                            iterator.remove();
//                        }
//                    }

                    List<AuditProject> list = new ArrayList<>();
//                    if (projectguidlist != null && projectguidlist.size() > 0) {
////                        projectguids = "'" + StringUtil.join(projectguidlist, "','") + "'";
////                        sql.in("rowguid", projectguids);
//
//                    }
                    sql.eq("b.operateuserguid",userguid);
                    sql.isNotBlank("a.rowguid");

                    String fields = "distinct(a.rowguid) as rowguid,a.taskguid as taskguid,projectname,applyeruserguid,a.applyername as applyername,a.areacode as areacode,a.pviguid as pviguid,status,tasktype,flowsn,applydate,banjieresult,banjiedate,spendtime,contactmobile,contactcertnum,certnum ";
                    sql.setSelectFields(fields);
                    PageData<AuditProject> pageProject = auditProjectService
                                .getAuditProjectPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                        if (pageProject != null) {
                            list = pageProject.getList();
                            for (AuditProject auditProject : list) {
                                audittask = audittaskServiec.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                        .getResult();
                                AuditTaskExtension auditTaskExtension = audittaskExtensionServiec
                                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                                if (auditTaskExtension != null) {
                                    if (ZwfwConstant.CONSTANT_STR_ONE
                                            .equals(auditTaskExtension.getIszijianxitong().toString())) {
                                        auditProject.put("projectname", "【自建系统】" + auditProject.getProjectname());
                                    }
                                }
                                if (audittask != null) {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(audittask.getType()))) {
                                        auditProject.put("sparetime", "--");
                                    } else {
                                        String sparetime = "";
                                        Integer spendtime = auditProject.getSpendtime() == null ? ZwfwConstant.CONSTANT_INT_ZERO : auditProject.getSpendtime();
                                        if (spendtime < 0) {
                                            sparetime = "超时" + CommonUtil.getSpareTimes(spendtime);
                                        } else {
                                            sparetime = CommonUtil.getSpareTimes(spendtime);
                                        }
                                        auditProject.put("sparetime", sparetime);
                                    }
                                }
                                if (ZwfwConstant.BANJIAN_STATUS_BYSL == auditProject.getStatus() || ZwfwConstant.BANJIAN_STATUS_SPBTG == auditProject.getStatus()) {
                                    List<AuditProjectUnusual> result = projectUnusualService.getProjectUnusualByProjectGuid(auditProject.getRowguid()).getResult();
                                    //给这个result排个序
                                    if (EpointCollectionUtils.isNotEmpty(result)) {
                                        List<AuditProjectUnusual> auditProjectUnusuals = result.stream().sorted(Comparator.comparing(AuditProjectUnusual::getOperatedate).reversed()).collect(Collectors.toList());
                                        auditProject.put("note", auditProjectUnusuals.get(0).getNote());
                                    }
                                }
                            }
                            count = pageProject.getRowCount();
                        }

                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
            if (statusModel != null) {
                Iterator<SelectItem> it = statusModel.iterator();
                if (it.hasNext()) {
                    it.next();
                }
                while (it.hasNext()) {
                    SelectItem item = it.next();
                    // 删除列表中不需要的列表选项
                    if (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_BYSL
                            || Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
//                        it.remove();
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return this.statusModel;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,applydate,contactmobile,contactcertnum,certnum,banjiedate,banjieresult,note",
                    "事项名称,办件编号,申请人,申请时间,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,办结时间,办结类型,不予受理原因");
        }
        return exportModel;
    }


    public List<SelectItem> getIsMyProjectModel() {
        if (ismyprojectModel == null) {
            ismyprojectModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "我的办件", null, false));
        }
        return this.ismyprojectModel;
    }

    public List<SelectItem> getIsMaterialModel() {
        if (ismaterialModel == null) {
            ismaterialModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否上传材料", null, false));
        }
        return this.ismaterialModel;
    }

    public List<SelectItem> getBanjieResultModel() {
        if (banjieResultModel == null) {
            banjieResultModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办结类型", null, false));
        }
        return this.banjieResultModel;
    }

    public void hasProGuid(String rowguid) {
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        String cxyscltxzurl = configService.getFrameConfigValue("cxyscltxzurl");
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        } else {
            if (auditProject.getProjectname().contains("在设区的市范围内跨区、县进行公路超限运输许可")||auditProject.getProjectname().contains("超限运输车辆行驶公路许可新办")) {
                String code = "";
                if (StringUtil.isBlank(auditProject.getStr("cxcode"))) {
                    String json = "{\"deptId\":\"370800\"}";
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("ApiKey", "767766629319704576");
                    String result = HttpUtil.doPostJson(cxyscltxzurl, json, headers);

                    if (StringUtil.isNotBlank(result)) {
                        JSONObject jsons = JSON.parseObject(result);
                        if ("200".equals(jsons.getString("code"))) {
                            JSONObject data = jsons.getJSONObject("data");
                            if ("200".equals(data.getString("code"))) {
                                String detail = data.getString("data");
                                //解析XML
                                org.dom4j.Document document = null;
                                try {
                                    document = DocumentHelper.parseText(detail);
                                } catch (DocumentException e) {
                                    e.printStackTrace();
                                }
                                if (detail != null && StringUtil.isNotBlank(detail)) {
                                    org.dom4j.Element root = document.getRootElement();
                                    List<org.dom4j.Element> elements = root.elements();
                                    for (org.dom4j.Element element : elements) {
                                        if ("DATA".equals(element.getQualifiedName())) {
                                            code = element.getStringValue();
                                            auditProject.set("cxcode", code);
                                            auditProjectService.updateProject(auditProject);
                                        }
                                    }
                                } else {
                                    code = "";
                                }
                            } else {
                                code = "";
                            }
                        } else {
                            code = "";
                        }
                    }
                } else {
                    code = auditProject.getStr("cxcode");
                }
                addCallbackParam("cxcode", code);
                addCallbackParam("taskchaoxian", "1");
            }
        }
    }

    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIsmaterialModel() {
        return ismaterialModel;
    }

    public void setIsmaterialModel(List<SelectItem> ismaterialModel) {
        this.ismaterialModel = ismaterialModel;
    }

    public List<SelectItem> getIsmyprojectModel() {
        return ismyprojectModel;
    }

    public void setIsmyprojectModel(List<SelectItem> ismyprojectModel) {
        this.ismyprojectModel = ismyprojectModel;
    }

    public void setStatusModel(List<SelectItem> statusModel) {
        this.statusModel = statusModel;
    }

    public int getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(int ismaterial) {
        this.ismaterial = ismaterial;
    }

    public int getIsmyproject() {
        return ismyproject;
    }

    public void setIsmyproject(int ismyproject) {
        this.ismyproject = ismyproject;
    }

    public String getApplydateStart() {
        return applydateStart;
    }

    public void setApplydateStart(String applydateStart) {
        this.applydateStart = applydateStart;
    }

    public String getApplydateEnd() {
        return applydateEnd;
    }

    public void setApplydateEnd(String applydateEnd) {
        this.applydateEnd = applydateEnd;
    }

    public String getAcceptuserdateStart() {
        return acceptuserdateStart;
    }

    public void setAcceptuserdateStart(String acceptuserdateStart) {
        this.acceptuserdateStart = acceptuserdateStart;
    }

    public String getAcceptuserdateEnd() {
        return acceptuserdateEnd;
    }

    public void setAcceptuserdateEnd(String acceptuserdateEnd) {
        this.acceptuserdateEnd = acceptuserdateEnd;
    }

    public String getBanjiedateStart() {
        return banjiedateStart;
    }

    public void setBanjiedateStart(String banjiedateStart) {
        this.banjiedateStart = banjiedateStart;
    }

    public String getBanjiedateEnd() {
        return banjiedateEnd;
    }

    public void setBanjiedateEnd(String banjiedateEnd) {
        this.banjiedateEnd = banjiedateEnd;
    }

}
