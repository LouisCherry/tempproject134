package com.epoint.auditproject.windowproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
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
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 事项登记list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017年5月22日]
 
 
 */
@RestController("jnwindowworkpanelaction")
@Scope("request")
public class JNWindowWorkPanelAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 实体bean
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 办件剩余时间
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;
    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;
    /**
     * 督办时间
     */
    private String dateStart;
    private String dateEnd;
    private Integer jzcount = 0;

    /**
     * 申请人查询条件
     */
    private String applyerName;

    String projectType;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 多选列表model
     */
    private List<SelectItem> isystuprojectModel = null;

    private int isystuproject;

    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;
    
    @Autowired
    private IWFInstanceAPI9 wfinstance;
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IJNAuditProject iAuditProject;

    @Autowired
    private IWFInstanceAPI9 wfi;

    @Autowired
    private IWorkflowActivityService wfaService;
    @Autowired
    private IAuditProjectUnusual projectUnusualService;
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;
    /**
     * frame_ou对应微服务接口
     */
    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaArea;

    @Autowired
    private IAuditTask taskService;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        projectType = this.getRequestParameter("type");
    }

    /**
     * TODO 2017/4/10 描述办理工作台返回不同类型办件列表的返回值 [一句话功能简述] [功能详细描述]
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void getDataCount() {
        String fieldstr = "rowguid,areacode,pviguid,taskguid,projectname,promiseenddate,tasktype,status,sparetime,applyername,bubandate,applydate,acceptuserdate,WindowName";
        int count = getBanJianList(fieldstr, projectType, windowguid).size();
        addCallbackParam("datapagesize", count);
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                @SuppressWarnings({"unchecked", "rawtypes" })
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    ArrayList list = new ArrayList();
                    if (StringUtil.isNotBlank(windowguid)) {
                        String fieldstr = "rowguid,areacode,pviguid,taskguid,projectname,promiseenddate,tasktype,status,sparetime,applyername,bubandate,applydate,acceptuserdate,WindowName,receivedate,yushendate,acceptuserguid,flowsn,contactmobile,contactcertnum,certnum,OperateUserName,bubanfinishdate,ifnull(commitnum,1) as commitnum,lastcommitdate ";
                        Date datestart = null;
                        Date dateend = null;
                        if (StringUtil.isNotBlank(dateStart)) {
                            datestart = EpointDateUtil.getBeginOfDateStr(dateStart);
                        }
                        if (StringUtil.isNotBlank(dateEnd)) {
                            dateend = EpointDateUtil.getEndOfDateStr(dateEnd);
                        }
                        String area = "";
                        // 如果是镇村接件
                        if (ZwfwUserSession.getInstance().getCitylevel() != null
                                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                            area = ZwfwUserSession.getInstance().getBaseAreaCode();
                        }
                        else {
                            area = ZwfwUserSession.getInstance().getAreaCode();
                        }
                        if (ZwfwConstant.CONSTANT_INT_ONE == isystuproject) {
                            projectType = "YSTU";
                        }
                        String areacode = ZwfwUserSession.getInstance().getAreaCode();
                        PageData<AuditProject> pageData = getBanJianListByCenterguidAndAreacode(fieldstr, projectType,
                                windowguid, first, pageSize, area, ZwfwUserSession.getInstance().getCenterGuid(),
                                applyerName, datestart, dateend, areacode, dataBean.getProjectname(),
                                dataBean.getStr("commitnum"), sortField, sortOrder);
                        if (pageData != null && pageData.getList() != null) {
                            for (AuditProject auditProject : pageData.getList()) {
                                // 返回窗口名字
                                auditProject.put("WindowNameField", auditProject.getWindowname());
                                Map<String, String> mapProjectName = handleIsMyproject(auditProject);
                                // 返回办件名称
                                auditProject.put("TaskName", mapProjectName.get("my")
                                        + "&nbsp;&nbsp;<a  href='javascript:void(0)' onclick='openUrl(\""
                                        + mapProjectName.get("url") + "\")'>" + auditProject.getProjectname() + "</a>");
                                // 返回承诺办结时间
                                auditProject.put("promiseenddate", getTime(auditProject.getPromiseenddate(),
                                        auditProject.getTasktype().toString()));
                                // 返回剩余时间
                                auditProject.put("computeSpareTime",
                                        getSpareTime(auditProject, auditProject.getStatus()));

                                if (auditProject.getReceivedate() == null) {
                                    auditProject.put("receivedate", "--");
                                }
                                String pviguid = auditProject.getPviguid();
                                if (StringUtil.isNotBlank(pviguid)) {
                                    //有流程的，流程审核人员设置为下一步审核人员
                                    ArrayList<Integer> listStatus = new ArrayList<>();
                                    listStatus.add(20);
                                    ProcessVersionInstance pVersionInstance = wfinstance.getProcessVersionInstance(pviguid);
                                    List<WorkflowWorkItem> WorkflowWorkItems = wfinstance.getWorkItemListByPVIGuidAndStatus(pVersionInstance, listStatus);
                                    if (EpointCollectionUtils.isNotEmpty(WorkflowWorkItems)) {
                                        WorkflowWorkItem workflowWorkItem = WorkflowWorkItems.get(0);
                                        auditProject.setOperateusername(workflowWorkItem.getTransactorName());
                                    }
                                }
                                if (ZwfwConstant.BANJIAN_STATUS_WWYSTU == auditProject.getStatus()) {
                                    List<AuditProjectUnusual> result = projectUnusualService.getProjectUnusualByProjectGuid(auditProject.getRowguid()).getResult();
                                    for (AuditProjectUnusual auditProjectUnusual : result) {
                                        if (result !=null) {
                                            auditProject.put("note", auditProjectUnusual.getNote());
                                        }
                                    }
                                    List<AuditProjectOperation> auditProjectOperationList = iAuditProjectOperation.getOperationListForDT(auditProject.getRowguid()).getResult();
                                    for (AuditProjectOperation auditProjectOperation : auditProjectOperationList) {
                                        if ("11".equals(auditProjectOperation.getOperateType())) {
                                            auditProject.put("backdate", EpointDateUtil.convertDate2String(auditProjectOperation.getOperatedate(), "yyyy-MM-dd HH:mm:ss"));
                                            auditProject.put("shusername", auditProjectOperation.getOperateusername());
                                        }
                                        
                                    }
                                }
                            }
                            this.setRowCount(pageData.getRowCount());
                            return pageData.getList();
                        }
                        else {
                            this.setRowCount(0);
                            return list;
                        }
                    }
                    else {
                        this.setRowCount(0);
                        return list;
                    }
                }
            };
        }
        return model;
    }

    public DataGridModel<AuditProject> getJZDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String fieldstr = "rowguid,areacode,pviguid,taskguid,projectname,promiseenddate,tasktype,status,sparetime,applyername,bubandate,applydate,acceptuserdate,WindowName,receivedate,Windowguid";
                    PageData<AuditProject> pageData = getJZListByPage(fieldstr, first, pageSize,
                            ZwfwUserSession.getInstance().getAreaCode(), applyerName);
                    if (pageData.getList() != null) {
                        for (AuditProject auditProject : pageData.getList()) {
                            // 返回窗口名字
                            auditProject.put("WindowNameField", iAuditOrgaWindow
                                    .getWindowByWindowGuid((auditProject.getWindowguid())).getResult().getWindowname());
                            Map<String, String> mapProjectName = handleIsMyproject(auditProject);
                            // 返回办件名称
                            auditProject.put("TaskName", mapProjectName.get("my")
                                    + "&nbsp;&nbsp;<a  href='javascript:void(0)' onclick='openUrl(\""
                                    + mapProjectName.get("url") + "\")'>" + auditProject.getProjectname() + "</a>");
                            // 返回承诺办结时间
                            auditProject.put("promiseenddate",
                                    getTime(auditProject.getPromiseenddate(), auditProject.getTasktype().toString()));
                            // 返回剩余时间
                            auditProject.put("computeSpareTime", getSpareTime(auditProject, auditProject.getStatus()));
                            // 返回办件状态
                            auditProject.put("status", auditProject.getStatus());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    /*********************************************** 待办结 ******************************************/
    /**
     * 获取承诺时间
     */
    public Object getTime(Date time, String tasktype) {
        Object result = null;
        if (ZwfwConstant.ITEMTYPE_JBJ.equals(tasktype)) {
            result = "即办件";
        }
        else {
            result = time;
        }
        return result;
    }

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(AuditProject auditProject, int status) {
        String result = "";
        if (auditProject != null && StringUtil.isNotBlank(status)) {
            if (!(auditProject.getTasktype().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)))
                    || status == ZwfwConstant.BANJIAN_STATUS_SPTG || status == ZwfwConstant.BANJIAN_STATUS_SPBTG) {
                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                int i = auditProject.getSparetime() == null ? 0 : auditProject.getSparetime();
                if (status < 90) {
                    i = auditProjectSparetime == null ? 0 : auditProjectSparetime.getSpareminutes();
                }
                if (i > 0) {
                    if (i < 1440) {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/yellowLight.gif\"/>";
                    }
                    else {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                }
                else if (i == 0) {
                    if (auditProject.getSparetime() == null || auditProjectSparetime == null) {
                        result = "--";
                    }
                    else {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                }
                else {
                    i = -i;
                    result = "超时" + CommonUtil.getSpareTimes(i) + "<img src=\"../../../image/light/redLight.gif\"/>";
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

    /**
     * 刷新tabnav数值
     *     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void refreshTabNav() {

        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid())
                .getResult();
        if (taskidList != null && taskidList.size() > 0) {
            List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
            if (taskidZJList != null && taskidZJList.size() > 0) {
                for (String zjTaskid : taskidZJList) {
                    taskidList.remove(zjTaskid);
                }
            }
        }
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        Map<String, Integer> map = iAuditProject.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList,
                ZwfwUserSession.getInstance().getWindowGuid(), ZwfwUserSession.getInstance().getAreaCode(),
                ZwfwUserSession.getInstance().getCenterGuid(), area).getResult();
        jzcount =getNotJZCount( ZwfwUserSession.getInstance().getAreaCode());
        // 待接件
        addCallbackParam("djj", map == null ? "0" : map.get("DJJ"));
        // 待受理
        addCallbackParam("dsl", map == null ? "0" : map.get("DSL"));
        // 待补办
        addCallbackParam("dbb", map == null ? "0" : map.get("DBB"));
        // 已暂停
        addCallbackParam("yzt", map == null ? "0" : map.get("YZT"));
        // 已办结
        addCallbackParam("dbj", map == null ? "0" : map.get("DBJ"));
        // 待预审
        addCallbackParam("dys", map == null ? "0" : map.get("DYS"));
        // 审核通过
        addCallbackParam("ystg", map == null ? "0" : map.get("YSTG"));
        // 不进驻中心办件
        addCallbackParam("jz", jzcount);
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public List<AuditProject> getBanJianList(String fieldstr, String projectType, String windowguid) {

        List<String> taskGuidList = iAuditOrgaWindow.getTaskGuidsByWindow(windowguid).getResult();
        List<AuditProject> list = iAuditProject.getBanJianList(fieldstr, taskGuidList, projectType, windowguid)
                .getResult();
        return list;
    }

    public PageData<AuditProject> getBanJianListByCenterguid(String fieldstr, String projectType, String windowguid,
            int first, int pageSize, String areaCode, String centerGuid, String applyerName, Date datestart,
            Date dateend) {

        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(windowguid).getResult();
        if (taskidList != null && taskidList.size() > 0) {
            List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
            if (taskidZJList != null && taskidZJList.size() > 0) {
                for (String zjTaskid : taskidZJList) {
                    taskidList.remove(zjTaskid);
                }
            }
        }
        PageData<AuditProject> list = iAuditProject.getBanJianListByCenterGuid(fieldstr, taskidList, projectType,
                windowguid, first, pageSize, areaCode, centerGuid, applyerName, datestart, dateend).getResult();
        return list;
    }

    public PageData<AuditProject> getBanJianListByCenterguidAndAreacode(String fieldstr, String projectType,
            String windowguid, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
            Date datestart, Date dateend, String realAreacode, String projectname, String commitnum, String sortField,
            String sortOrder) {

        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(windowguid).getResult();
        return iAuditProject
                .getBanJianListByCenterGuidAndAreacode(fieldstr, taskidList, projectType, windowguid, first, pageSize,
                        areaCode, centerGuid, applyerName, datestart, dateend, realAreacode, projectname, commitnum,
                        sortField, sortOrder)
                .getResult();
    }

    public PageData<AuditProject> getBanJianListByPage(String fieldstr, String projectType, String windowguid,
            int first, int pageSize, String areaCode, String applyerName, Date datestart, Date dateend) {

        List<String> taskidList = iAuditOrgaWindow.getTaskidsByWindow(windowguid).getResult();
        PageData<AuditProject> list = iAuditProject.getBanJianListByPage(fieldstr, taskidList, projectType, windowguid,
                first, pageSize, areaCode, applyerName, datestart, dateend).getResult();
        return list;
    }

    public PageData<AuditProject> getJZListByPage(String fieldstr, int first, int pageSize, String areaCode,
            String applyerName) {
        List<String> ouguidList = new ArrayList<>();
        AuditOrgaArea area = getAuditOrgaAreaByCode();
        List<FrameOu> frameoulist = ouService.listDependOuByParentGuid(area.getOuguid(), "", 2);
        for (FrameOu ouguid : frameoulist) {
            ouguidList.add(ouguid.getOuguid());
        }
        PageData<AuditProject> list = iAuditProject
                .getJZListByPage(fieldstr, ouguidList, first, pageSize, areaCode, applyerName).getResult();
        return list;
    }

    public AuditOrgaArea getAuditOrgaAreaByCode() {
        Map<String, String> map = new HashMap<String, String>(16);
        ZwfwUserSession instance = ZwfwUserSession.getInstance();
        map.put("xiaqucode=", instance.getAreaCode());
        return auditOrgaArea.getAuditArea(map).getResult();
    }

    /**
     * 获取不进驻中心办件
     * 
     * @param fieldstr
     * @param first
     * @param pageSize
     * @param areaCode
     * @param applyerName
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditProject> getJZList(String fieldstr, String areaCode) {
        List<String> ouguidList = new ArrayList<>();
        AuditOrgaArea area = getAuditOrgaAreaByCode();
        List<AuditProject> list = new ArrayList<>();
        if (area != null) {
//            List<FrameOu> frameoulist = ouService.listDependOuByParentGuid(area.getOuguid(), "", 2);
//            for (FrameOu ouguid : frameoulist) {
//                ouguidList.add(ouguid.getOuguid());
//            }
            list = iAuditProject.getJZList(fieldstr, null, areaCode).getResult();
        }
        return list;
    }

    /**
     * 获取不进驻中心办件数量
     *
     * @param fieldstr
     * @param first
     * @param pageSize
     * @param areaCode
     * @param applyerName
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getNotJZCount(String areaCode) {
        List<String> ouguidList = new ArrayList<>();
        AuditOrgaArea area = getAuditOrgaAreaByCode();
        int result = 0;
        if (area != null) {
//            List<FrameOu> frameoulist = ouService.listDependOuByParentGuid(area.getOuguid(), "", 2);
//            for (FrameOu ouguid : frameoulist) {
//                ouguidList.add(ouguid.getOuguid());
//            }
            result = iAuditProject.getNotJZCount(null, areaCode);
        }
        return result;
    }

    public List<WorkflowWorkItem> getActivityWorkflowWorkItem(String pviGuid) {
        List<WorkflowWorkItem> selectByPVIGuidAndStatus = null;
        if (StringUtil.isNotBlank(pviGuid)) {
            List<Integer> status = new ArrayList<Integer>();
            status.add(10);
            status.add(20);
            ProcessVersionInstance pVersionInstance = wfi.getProcessVersionInstance(pviGuid);
            if (pVersionInstance != null) {
                selectByPVIGuidAndStatus = iAuditProject.getWorkItemListByPVIGuidAndStatus(pviGuid);
            }
        }
        return selectByPVIGuidAndStatus;
    }

    public void hasProGuid(String rowguid) {
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(rowguid, area).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public Map<String, String> handleIsMyproject(AuditProject auditProject) {
        String pviGuid = auditProject.getPviguid();
        String Taskguid = auditProject.getTaskguid();
        String RowGuid = auditProject.getRowguid();
        Integer Status = auditProject.getStatus();
        String acceptuserguid = auditProject.getAcceptuserguid();

        Map<String, String> map = new HashMap<String, String>(16);
        // 1.获取活动的工作项
        List<WorkflowWorkItem> workflowWorkItems = getActivityWorkflowWorkItem(pviGuid);
        if (workflowWorkItems != null && !workflowWorkItems.isEmpty() && ZwfwConstant.BANJIAN_STATUS_YJJ <= Status) {// 存在活动工作项且办件已受理
            // 受理后补办显示补办页面
            if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Status) {
                map.put("url", "epointzwfw/auditbusiness/auditproject/auditprojectinfobuzheng?projectguid=" + RowGuid
                        + "&TaskGuid=" + Taskguid + "&ProcessVersionInstanceGuid=" + pviGuid);
                map.put("my", "<span class='text-danger'>窗口办件</span>");
            }
            else {
                map.put("url",
                        "epointzwfw/auditbusiness/auditwindowbusiness/businessmanage/windowauditbanjianinfo?projectguid="
                                + RowGuid + "&ProcessVersionInstanceGuid=" + pviGuid);
                map.put("my", "<span class='text-danger'>窗口办件</span>");
            }
            // 1.1工作项中的处理人是否有“我”
            for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
                // 流转到“我”的工作流只允许“我”打开
                if (UserSession.getInstance().getUserGuid().equals(workflowWorkItem.getTransactor())) {
                    map.put("url", workflowWorkItem.getHandleUrl()+"&projectguid=" + RowGuid+ "&taskguid="
                            + Taskguid );
                    map.put("my", "<span class='text-danger'>我的办件</span>");
                    break;
                }
            }
        }
        else {// 不存在活动的工作项

            AuditCommonResult<AuditTask> taskResult = taskService.getAuditTaskByGuid(Taskguid, true);
            AuditTask task = taskResult.getResult();
            if (task != null) {
                // map.put("url", null);
                WorkflowActivity activity = wfaService.getFirstActivity(task.getPvguid());
                if (activity != null) {
                    map.put("url", activity.getHandleUrl() + "?processguid=" + task.getProcessguid() + "&taskguid="
                            + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                }
                else {
                    map.put("url", "epointzwfw/auditbusiness/auditproject/auditprojectinfo?processguid="
                            + task.getProcessguid() + "&taskguid=" + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                }
                if (UserSession.getInstance().getUserGuid().equals(acceptuserguid)) {
                    map.put("my", "<span class='text-danger'>我的办件</span>");
                }
            }
        }
        AuditCommonResult<AuditTask> taskResult = taskService.getAuditTaskByGuid(Taskguid, false);
        AuditTask task = taskResult.getResult();
        if (task != null) {
            // 存在个性化标板加载页面
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(Taskguid, false).getResult();
            // log.info("查询到对应的事项拓展信息："+auditTaskExtension.getCustomformurl());
            if (StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                    && auditTaskExtension.getCustomformurl().contains("auditprojectinfo")) {
                // log.info("获取到对应施工许可证事项拓展信息："+auditTaskExtension);
                map.put("url", auditTaskExtension.getCustomformurl() + "?processguid=" + task.getProcessguid()
                        + "&taskguid=" + Taskguid + "&projectguid=" + RowGuid);
            }
        }

        return map;

    }

    //待接件导出
    public ExportModel getExportModelDjj() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称");
        }
        return exportModel;
    }
    
    //待受理导出
    public ExportModel getExportModelDsl() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName,receivedate",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称,接件时间");
        }
        return exportModel;
    }
    
    //待办结导出
    public ExportModel getExportModelDbj() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称");
        }
        return exportModel;
    }
    
    //待预审导出
    public ExportModel getExportModelDys() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName,applydate",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称,申请时间");
        }
        return exportModel;
    }
    
    //预审通过导出
    public ExportModel getExportModelYstg() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName,yushendate",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称,预审时间");
        }
        return exportModel;
    }
    
    //待审核通过导出
    public ExportModel getExportModelDsh() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,operateUserName,acceptuserdate",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称,受理时间");
        }
        return exportModel;
    }
    
    //预审退回导出
    public ExportModel getExportModelYsth() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "projectname,flowsn,applyername,contactmobile,contactcertnum,certnum,shusername,applydate,backdate,note",
                    "事项名称,办件编号,申请人,联系电话,联系人身份证号/法人身份证号,办理人身份证/企业统一信用代码,审核人员名称,申请时间,预审退回时间,退回原因");
        }
        return exportModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsYstuProjectModel() {
        if (isystuprojectModel == null) {
            isystuprojectModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "预审退回", null, false));
        }
        return this.isystuprojectModel;
    }

    public void setIsYstuProjectModel(List<SelectItem> isystuprojectModel) {
        this.isystuprojectModel = isystuprojectModel;
    }

    public int getIsystuproject() {
        return isystuproject;
    }

    public void setIsystuproject(int isystuproject) {
        this.isystuproject = isystuproject;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

}
