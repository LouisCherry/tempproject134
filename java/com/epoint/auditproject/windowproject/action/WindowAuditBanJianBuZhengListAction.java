package com.epoint.auditproject.windowproject.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 业务管理list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("windowauditbanjianbuzhenglistaction")
@Scope("request")
public class WindowAuditBanJianBuZhengListAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    // private WindowAuditBanJianService auditBanJianService = new
    // WindowAuditBanJianService();
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> ismaterialModel = null;
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
     * 申请时间
     */
    private String applydateStart;
    private String applydateEnd;
    /**
     * 受理时间
     */
    private String acceptuserdateStart;
    private String acceptuserdateEnd;

    @Autowired
    private IAuditProjectSparetime auditProjectSpareTimeService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditOrgaWindowYjs windowService;

    @Autowired
    private IAuditTask taskService;

    private String keyword;

    @Override
    public void pageLoad() {
        keyword = getRequestParameter("keyword");
 
        keyword = EncodeUtil.decode(keyword);
        if(isPostback()){
            keyword = "";
        }
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1966962324797294215L;

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    boolean like=false;
                    sortField = "applydate";
                    SqlConditionUtil sql = new SqlConditionUtil();
                    String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
                    sql.like("handleareacode", handleareacode+",");
                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    }else{
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sql.eq("areacode", area);
                    if (dataBean.getStatus() != null) {
                        sql.eq("status", dataBean.getStatus().toString());
                    }
                    sql.eq("epidemic", ZwfwConstant.CONSTANT_STR_ONE);
                    
                    if (ZwfwConstant.CONSTANT_INT_ONE == ismyproject){
                        sql.eq("acceptuserguid", UserSession.getInstance().getUserGuid());
                    }
                    if (StringUtil.isNotBlank(keyword)) {
                        if (keyword.indexOf("-")>-1) {
                            String[] strs=keyword.split("-");
                            if(strs.length>0){
                                sql.like("applyername", strs[0]);
                                sql.like("flowsn", strs[1]);
                            }
                        }
                        else {
                            sql.like("applyername+flowsn", keyword);
                        }
                    }
                    if (!dataBean.isEmpty()) {
                        //sql.clear();
                        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                            sql.like("projectname", dataBean.getProjectname());
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
                        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                            sql.like("applyername", dataBean.getApplyername());
                        }
                    }
          

                    String taskids = "";
                    List<String> taskidList = windowService
                            .getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                    //自建系统办件
                    if (taskidList != null && taskidList.size() > 0) {
                        List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
                        if(taskidZJList != null && taskidZJList.size() > 0){
                            for(String zjTaskid : taskidZJList){
                                taskidList.remove(zjTaskid);
                            }
                        }
                    }
                    PageData<AuditProject> pageProject = null;
                    PageData<AuditProject> pageProjectlike = null;
                    if (taskidList != null && taskidList.size() > 0) {
                        taskids = "'" + StringUtil.join(taskidList, "','") + "'";
                        sql.in("task_id", taskids);
                        String fields = "rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,tasktype,flowsn,applydate";
                        if(like){
                            //区分大小写查询
                            sql.like("upper(applyername)", StringUtil.toUpperCase(keyword));
                            pageProjectlike = auditProjectService
                                    .getAuditProjectPageData(fields,sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                            sql.like("upper(applyername)", "");
                            sql.like("upper(flowsn)", StringUtil.toUpperCase(keyword));
                            pageProject = auditProjectService
                                    .getAuditProjectPageData(fields,sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                            pageProject.getList().addAll(pageProjectlike.getList());
                            pageProject.setRowCount(pageProjectlike.getRowCount()+pageProject.getRowCount());
                        }else{
                            pageProject = auditProjectService
                                    .getAuditProjectPageData(fields,sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                        }
                    }
                    List<AuditProject> list = new ArrayList<>();
                    int count = 0;
                    if (pageProject != null) {
                        list = pageProject.getList();
                        for (AuditProject auditProject : list) {
                            AuditProjectSparetime auditProjectSparetime = auditProjectSpareTimeService
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                            if (auditProjectSparetime != null) {
                                auditProject.put("sparetime", getSpareTime(auditProjectSparetime.getSpareminutes(),
                                        auditProject.getTasktype()));
                            }
                            else {
                                auditProject.put("sparetime", "--");
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

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(int minutes, int taskType) {
        String result = "";
        if (StringUtil.isNotBlank(taskType)) {
            if (!(taskType == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ))) {
                if (minutes > 0) {
                    if (minutes < 1440){
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../image/light/yellowLight.gif\"/>";
                    }
                    else{
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                }
                else {
                    minutes = -minutes;
                    result = "超时" + CommonUtil.getSpareTimes(minutes)
                            + "<img src=\"../../../image/light/redLight.gif\"/>";
                }
            }
            else {
                result = "--";
            }
        }
        else {
            result = "--";
        }
        return result;
    }

    @SuppressWarnings("unchecked")
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
                    if (Integer.parseInt(item.getValue().toString()) < ZwfwConstant.BANJIAN_STATUS_DJJ
                            || Integer.parseInt(item.getValue().toString()) >= ZwfwConstant.BANJIAN_STATUS_ZCBJ||Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_YSLDBJ) {
                        it.remove();
                    }
                }
            }
        }
        return this.statusModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsMyProjectModel() {
        if (ismyprojectModel == null) {
            ismyprojectModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "我的办件", null, false));
        }
        return this.ismyprojectModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsMaterialModel() {
        if (ismaterialModel == null) {
            ismaterialModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否上传材料", null, false));
        }
        return this.ismaterialModel;
    }

    public void hasProGuid(String rowguid){
        String areacode = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if(auditProject==null){
            addCallbackParam("message","办件已删除");
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
}
