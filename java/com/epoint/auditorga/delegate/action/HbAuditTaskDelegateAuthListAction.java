package com.epoint.auditorga.delegate.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.delegate.domain.AuditTaskDelegateAuth;
import com.epoint.basic.auditorga.delegate.inter.IAuditTaskDelegateAuth;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;

/**
 * 下放事项权限设置表list页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-12-06 14:58:53]
 */
@SuppressWarnings({"serial", "unchecked" })
@RestController("hbaudittaskdelegateauthlistaction")
@Scope("request")
public class HbAuditTaskDelegateAuthListAction extends BaseController
{
     
    @Autowired
    private IAuditTaskDelegateAuth authService;

    @Autowired
    private IAuditOrgaArea iAuditorgaAtra;

    @Autowired
    private IAuditTaskRiskpoint iAuditTaskRiskpoint;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditTaskDelegate iAuditTaskDelegate;

    @Autowired
    private IWorkflowActivityService workflowActivityService9;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 下放事项权限设置表实体对象
     */
    private AuditTaskDelegateAuth dataBean;

    /**
     * 事项基本信息实体
     */
    private AuditTask auditTask;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaArea> model;

    /**
     * 事项下放类型下拉列表model
     */
    private List<SelectItem> delegateTypeModel = null;

    /**
     * 辖区名称
     */
    private String xiaquname;

    /**
     * 辖区编号
     */
    private String xiaqucode;

    /**
     * 事项标识
     */
    private String taskGuid;

    /**
     * 事项下放类型
     */
    private String delegatetype;

    /**
     * 事项名称
     */
    private String taskname;

    /**
     * 事项办件类型
     */
    private String typename;
    
    /**
     * 辖区编码类型
     */
    private String citylevel;
    /**
     * 辖区编码
     */
    private String xqcode;

    /**
     * 办理环节标识集合
     */
    private List<String> activityGuidList = new ArrayList<>();

    private List<AuditTaskRiskpoint> auditTaskRiskpointOrigs;

    
    @Override
    public void pageLoad() {
        taskGuid = getRequestParameter("taskGuid");
        citylevel =getRequestParameter("citylevel");
        xqcode = getRequestParameter("xqcode");
        auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
        taskname = auditTask.getTaskname();
        if (StringUtil.isNotBlank(auditTask.getType())) {
            typename = iCodeItemsService.getItemTextByCodeName("事项类型", auditTask.getType().toString());
        }
        if (!isPostback()) {
            // 默认上级委托
            delegatetype = ZwfwConstant.TASKDELEGATE_TYPE_SJWT;
            addViewData("delegatetype", delegatetype);
        }

        auditTaskRiskpointOrigs = iAuditTaskRiskpoint.getRiskPointListByTaksGuid(taskGuid, false).getResult();

        // 第一步，获取办理环节第一步，即开始后一步骤
        WorkflowActivity workflowActivityFirst = workflowActivityService9.getFirstActivity(auditTask.getPvguid());
        if (workflowActivityFirst != null) {
            activityGuidList.add(0, workflowActivityFirst.getActivityGuid());
            // 第二步，根据办理环节第一步，开始递归排序
            recursiveActivityGuid(workflowActivityFirst.getActivityGuid());
        }

        initDynamicActivity();
    }

    /**
     * 切换事项下放类型
     * 
     * @param e
     */
    public void delegateTypeChange(String type) {
        addViewData("delegatetype", type);
        initDynamicActivity();
    }

    /**
     * 动态初始化列表
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void initDynamicActivity() {
        delegatetype = getViewData("delegatetype");
        JSONObject rtnJson = new JSONObject();
        List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
        addFirstColumn(columnList);
        if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
            // 即办件简易模式时，展示授权
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(auditTask.getType().toString())
                    && ZwfwConstant.JBJMODE_SIMPLE.equals(auditTask.getJbjmode())) {
                Map<String, Object> tmpMap = new HashMap<String, Object>(16);
                tmpMap.put("field", "selected0");
                tmpMap.put("width", "150");
                tmpMap.put("headerAlign", "center");
                tmpMap.put("align", "center");
                tmpMap.put("name", "selected0");
                tmpMap.put("trueValue", ZwfwConstant.CONSTANT_INT_ONE);
                tmpMap.put("falseValue", ZwfwConstant.CONSTANT_INT_ZERO);
                tmpMap.put("type", "checkboxcolumn");
                tmpMap.put("header", "授权");
                columnList.add(tmpMap);
            }
            // 根据办理环节岗位，初始化动态列
            else if (activityGuidList != null && !activityGuidList.isEmpty()) {
                for (int i = 0; i < activityGuidList.size(); i++) {
                    Map<String, Object> tmpMap = new HashMap<String, Object>(16);
                    tmpMap.put("field", "selected" + i + "");
                    tmpMap.put("width", "150");
                    tmpMap.put("headerAlign", "center");
                    tmpMap.put("align", "center");
                    tmpMap.put("name", "selected" + i + "");
                    tmpMap.put("trueValue", ZwfwConstant.CONSTANT_INT_ONE);
                    tmpMap.put("falseValue", ZwfwConstant.CONSTANT_INT_ZERO);
                    tmpMap.put("type", "checkboxcolumn");
                    AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint
                            .getAuditTaskRiskpointByActivityguid(activityGuidList.get(i), false).getResult();
                    // 定义环节名称
                    String activityname = "";
                    if (auditTaskRiskpoint != null) {
                        activityname = auditTaskRiskpoint.getActivityname();
                    }
                    tmpMap.put("header", activityname);
                    columnList.add(tmpMap);

                }
                if (auditTaskRiskpointOrigs.size() < 5) {
                    Map<String, Object> nullMap = new HashMap<String, Object>(16);
                    nullMap.put("width", (5 - auditTaskRiskpointOrigs.size()) * 150);
                    nullMap.put("header", "");
                    columnList.add(nullMap);
                }
            }
            else {
                Map<String, Object> tmpMap = new HashMap<String, Object>(16);
                tmpMap.put("width", "150");
                tmpMap.put("headerAlign", "center");
                tmpMap.put("align", "center");
                tmpMap.put("header", "无流程配置");
                columnList.add(tmpMap);
            }
        }
        else {
            Map<String, Object> tmpMap = new HashMap<String, Object>(16);
            tmpMap.put("field", "selected0");
            tmpMap.put("width", "150");
            tmpMap.put("headerAlign", "center");
            tmpMap.put("align", "center");
            tmpMap.put("name", "selected0");
            tmpMap.put("trueValue", ZwfwConstant.CONSTANT_INT_ONE);
            tmpMap.put("falseValue", ZwfwConstant.CONSTANT_INT_ZERO);
            tmpMap.put("type", "checkboxcolumn");
            tmpMap.put("header", "授权");
            columnList.add(tmpMap);
        }

        addEndColumn(columnList);

        try {
            rtnJson.put("columns", columnList);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        addCallbackParam("columns", rtnJson.toString());

    }

    /**
     * 增加首列{即动态环节列之前的列}
     * 
     * @param columnList
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void addFirstColumn(List<Map<String, Object>> columnList) {
        //序
        Map<String, Object> idMap = new HashMap<String, Object>(16);
        idMap.put("field", "id");
        idMap.put("width", "40");
        idMap.put("headerAlign", "center");
        idMap.put("align", "center");
        idMap.put("header", "序");
        columnList.add(idMap);
        //乡镇名称
        Map<String, Object> tmpMap = new HashMap<String, Object>(16);
        tmpMap.put("field", "xiaquname");
        tmpMap.put("width", "200");
        tmpMap.put("headerAlign", "center");
        tmpMap.put("align", "center");
        tmpMap.put("header", "辖区名称");
         if("4".equals(citylevel)){
            tmpMap.put("header", "村（社区）名称");
        }
        columnList.add(tmpMap);

    }

    /**
     * 增加尾列{即动态环节列之后的列}
     * 
     * @param columnList
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected void addEndColumn(List<Map<String, Object>> columnList) {

    }

    /**
     * 保存设置按纽
     */
    public void saveBtn() {
        delegatetype=getRequestParameter("delegatetype");
        List<AuditOrgaArea> list = getDataGridData().getWrappedData();
        String[] columns = getDataGridData().getFieldColumns().split(",");
        List<String> selected = new ArrayList<String>();

        for (String column : columns) {
            if (column.trim().startsWith("selected")) {
                selected.add(column.trim());
            }
        }
        for (AuditOrgaArea fu : list) {
            boolean flag = false;
            // 是否允许受理，默认不允许
            String isallowaccept = ZwfwConstant.CONSTANT_STR_ZERO;
            List<String> frList = null;
            try {
                if (fu.get("frList") instanceof JSONArray) {
                    frList = new ArrayList<String>();
                    JSONArray ja = fu.get("frList");
                    for (int i = 0; i < ja.size(); i++) {
                        frList.add(ja.getString(i));
                    }
                }
                else {
                    frList = fu.get("frList");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            // 给乡镇授权权限
            for (int i = 0; i < selected.size(); i++) {
                // 保存设置页面为上级委托时，首次删除这个乡镇的，上级委托的所有办理环节授权信息(只处理一次，避免删除重新勾选的内容)
                if (!flag && ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                    // 删除这个乡镇的，上级委托的所有办理环节授权信息
                    authService.deleteAuthByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(),
                            ZwfwConstant.TASKDELEGATE_TYPE_SJWT);
                }
                else if (ZwfwConstant.TASKDELEGATE_TYPE_ZCBS.equals(delegatetype)) {
                    // 删除这个乡镇的，部门驻场的授权信息
                    authService.deleteAuthByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(),
                            ZwfwConstant.TASKDELEGATE_TYPE_ZCBS);
                }
                if (StringUtil.isNotBlank(fu.get(selected.get(i)))
                        && fu.get(selected.get(i)).toString().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    if (i == 0) {
                        isallowaccept = ZwfwConstant.CONSTANT_STR_ONE;
                    }
                    if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                        // 保存设置页面为上级委托时，存在环节授权，则删除这个乡镇的，部门驻场的所有办理环节授权信息
                        authService.deleteAuthByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(),
                                ZwfwConstant.TASKDELEGATE_TYPE_ZCBS);
                        if (StringUtil.isNotBlank(frList)) {
                            Record record = iAuditTaskRiskpoint.getNameByTaskGuidAndRpId(taskGuid, frList.get(i))
                                    .getResult();
                            // 定义环节名称
                            String activityname = "";
                            if (record != null) {
                                activityname = record.get("activityname");
                            }
                            // 保存乡镇授权信息-上级委托
                            authService.insertAuthRiskIdByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(),
                                    frList.get(i), activityname, delegatetype);
                        }
                        // 即办件，简易模式
                        else {
                            authService.insertAuthRiskIdByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(), "",
                                    "", delegatetype);
                        }
                    }
                    else {
                        // 保存设置页面为部门驻场，并且授权时，删除这个乡镇的，上级委托的所有办理环节授权信息
                        authService.deleteAuthByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(), "");
                        // 保存乡镇授权信息-部门驻场
                        authService.insertAuthRiskIdByTaskIdAndOuguid(auditTask.getTask_id(), fu.getOuguid(), "", "",
                                delegatetype);
                    }
                    // 如果存在授权，则标记为true，后续不在删除环节授权表数据
                    flag = true;
                }
            }

            // 下放事项状态，如果下放环节授权表中还有数据，则启用状态，反正则禁用
            String status = "";
            // 判断是否已选择
            int authCount = authService
                    .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(), fu.getOuguid(), "").getResult();
            if (authCount > 0) {
                status = ZwfwConstant.CONSTANT_STR_ONE;
            }
            else {
                status = ZwfwConstant.CONSTANT_STR_ZERO;
            }

            // 将事项下放至乡镇事项表中
            // 根据事项版本唯一标识和辖区标识判断是否已授权入库，如果已存在，则更新；如果不存在，则新增。
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(status)) {
                // 判断是否已存在
                List<AuditTaskDelegate> taskDelegateList = iAuditTaskDelegate
                        .selectDelegateListByTaskIDAndAreacode(auditTask.getTask_id(), fu.getXiaqucode()).getResult();
                if (taskDelegateList != null && !taskDelegateList.isEmpty()) {
                    updateTaskDelegate(fu, status, isallowaccept);
                }
                else {
                    insertTaskDelegate(fu, isallowaccept);
                }
            }
            // 如果未有授权环节，需将下放事项表中已插入数据state标记为0
            else {
                updateTaskDelegate(fu, status, isallowaccept);
            }

        }
        addCallbackParam("msg", "保存成功");
    }

    /**
     * 新增下放事项信息
     *  
     *  @param area    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void insertTaskDelegate(AuditOrgaArea area, String isallowaccept) {
        // 将事项下放至乡镇事项表中
        AuditTaskDelegate auditTaskDelegate = new AuditTaskDelegate();
        // 主键
        auditTaskDelegate.setRowguid(UUID.randomUUID().toString());
        // 下放事项唯一标识
        auditTaskDelegate.setTaskguid(UUID.randomUUID().toString());
        // 市级事项版本唯一标识
        auditTaskDelegate.setTaskid(auditTask.getTask_id());
        // 乡镇部门标识
        auditTaskDelegate.setOuguid(area.getOuguid());
        // 乡镇部门名称
        auditTaskDelegate.setOuname(area.getOuname());
        // 乡镇辖区
        auditTaskDelegate.setAreacode(area.getXiaqucode());
        // 事项下放类型
        auditTaskDelegate.setDelegatetype(delegatetype);
        // 事项状态
        auditTaskDelegate.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
        // 生成日期
        auditTaskDelegate.setOperatedate(new Date());
        // 处理人
        auditTaskDelegate.setOperateusername(userSession.getDisplayName());
        // 是否允许受理
        auditTaskDelegate.setIsallowaccept(isallowaccept);
        
        auditTaskDelegate.setXzorder(auditTask.getOrdernum());       
        auditTaskDelegate.setPromise_day(auditTask.getPromise_day());
        auditTaskDelegate.setLink_tel(auditTask.getLink_tel());
        auditTaskDelegate.setSupervise_tel(auditTask.getSupervise_tel());
        auditTaskDelegate.setApplyaddress(auditTask.getTransact_addr());
        auditTaskDelegate.setAcceptcondition(auditTask.getAcceptcondition());
        auditTaskDelegate.setApplytime(auditTask.getTransact_time());
        auditTaskDelegate.set("sxtq", "1");
        iAuditTaskDelegate.addAuditTaskDelegate(auditTaskDelegate);

    }

    /**
     * 更新下放事项信息
     *  
     *  @param area
     *  @param state    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTaskDelegate(AuditOrgaArea area, String state, String isallowaccept) {
        // 将事项下放至乡镇事项表中
        AuditTaskDelegate auditTaskDelegate = iAuditTaskDelegate
                .findByTaskIDAndAreacode(auditTask.getTask_id(), area.getXiaqucode()).getResult();
        if (auditTaskDelegate != null) {
            // 事项下放类型
            // 如果被取消授权后，标记事项下放类型为0，即为授权
            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(state)) {
                auditTaskDelegate.setDelegatetype(ZwfwConstant.CONSTANT_STR_ZERO);
            }
            else {
                List<AuditTaskDelegateAuth> authList = authService
                        .getAuthListByTaskIdAndOuguid(auditTask.getTask_id(), area.getOuguid()).getResult();
                if (authList != null && !authList.isEmpty()) {
                    auditTaskDelegate.setDelegatetype(authList.get(0).getDelegatetype());
                }
            }
            // 事项状态
            auditTaskDelegate.setStatus(state);
            auditTaskDelegate.set("sxtq", "1");
            // 生成日期
            auditTaskDelegate.setOperatedate(new Date());
            // 处理人
            auditTaskDelegate.setOperateusername(userSession.getDisplayName());
            // 是否允许受理
            // 如果isallowaccept为1，则直接赋值
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowaccept)) {
                auditTaskDelegate.setIsallowaccept(isallowaccept);
            }
            // 如果isallowaccept为0，则需要判断是在哪个下放类型下保存设置
            // 1、上级委托，isallowaccept为0说明第一步未授权，需要判断该乡镇是否是部门驻场授权，如果是，则是否允许受理标记为1
            // 2、部门驻场，isallowaccept为0说明未授权，如果status为1，则说明存在上级委托授权，isallowaccept不予处理；如果status为0，则说明都无授权，则是否允许受理标记为0
            else {
                if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                    // 判断是否存在部门驻场授权
                    int authCount = authService.getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                            area.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_ZCBS).getResult();
                    // 存在，允许受理
                    if (authCount > 0) {
                        auditTaskDelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    // 不存在，不允许受理
                    else {
                        auditTaskDelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ZERO);
                    }
                }
                else {
                    // 如果status为0，则说明都无授权，则是否允许受理标记为0
                    if (ZwfwConstant.CONSTANT_STR_ZERO.equals(state)) {
                        auditTaskDelegate.setIsallowaccept(ZwfwConstant.CONSTANT_STR_ZERO);
                    }
                }
            }
            iAuditTaskDelegate.updata(auditTaskDelegate);
        }
    }

    public DataGridModel<AuditOrgaArea> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaArea>()
            {
                @Override
                public List<AuditOrgaArea> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 页面上搜索条件
                    if("4".equals(citylevel)){
                        sql.like("xiaqucode", xqcode);
                        sql.nq("xiaqucode", xqcode);
                        sql.eq("citylevel", citylevel);
                    }
                    else if ("1".equals(citylevel)) {
                          sql.eq("citylevel", "2");
                    }
                    else{
                        sql.like("xiaqucode", xqcode);
                        sql.nq("xiaqucode", xqcode);
                        sql.eq("citylevel", citylevel);
                    }
                   
                    if(StringUtil.isNotBlank(sortField)){
                        sql.setOrder(sortField, sortOrder);
                    }
                    sql.setOrderDesc("operatedate");
                    PageData<AuditOrgaArea> pageData = iAuditorgaAtra
                            .getAuditAreaPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    // 对特殊列进行处理
                    List<AuditOrgaArea> list = pageData.getList();
                    if ("1".equals(citylevel)) {
                    	 if (list != null) {
                             if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                                 // 获取当前事项所有办理环节，并保存固定环节标识，插入数组
                                 String[] riskPointIdList = new String[activityGuidList.size()];
                                 for (int i = 0; i < riskPointIdList.length; i++) {
                                     AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint
                                             .getAuditTaskRiskpointByActivityguid(activityGuidList.get(i), false)
                                             .getResult();
                                     if (auditTaskRiskpoint != null) {
                                         riskPointIdList[i] = auditTaskRiskpoint.getRiskpointid();
                                     }
                                 }

                                 // 逐条处理乡镇信息
                                 for (int i = 0; i < list.size(); i++) {
                                     AuditOrgaArea orgaArea = list.get(i);
                                     String xqucode =orgaArea.getXiaqucode();
                                     String xiaquname =orgaArea.getXiaquname();
                                     //添加乡镇链接
                                     orgaArea.setXiaquname("<a href=javascript:openEdit('" + taskGuid
                                             + "','区县延伸配置','"+xqucode+"')>"+xiaquname+"</a>");

                                     // 排序
                                     orgaArea.put("id", first + i + 1);
                                     orgaArea.put("taskname", taskname);
                                     orgaArea.put("type", typename);
                                     // 获取当前事项当前乡镇授权固定环节标识
                                     List<String> riskpointid = authService
                                             .getRpIdByTaskIdAndOuguid(auditTask.getTask_id(), orgaArea.getOuguid())
                                             .getResult();
                                     // 判断是否已选择
                                     if (riskPointIdList.length > 0) {
                                         for (int j = 0; j < riskPointIdList.length; j++) {
                                             if (riskpointid.contains(riskPointIdList[j])) {
                                                 orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ONE);
                                             }
                                             else {
                                                 orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ZERO);
                                             }
                                         }
                                         orgaArea.put("frList", StringUtil.change2ArrayList(riskPointIdList));
                                     }
                                     else {
                                         // 判断是否已选择
                                         int authCount = authService
                                                 .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                         orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_SJWT)
                                                 .getResult();
                                         if (authCount > 0) {
                                             orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                         }
                                         else {
                                             orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                         }
                                     }
                                 }
                             }
                             else {
                                 // 逐条处理乡镇信息
                                 for (int i = 0; i < list.size(); i++) {
                                     AuditOrgaArea orgaArea = list.get(i);
                                     String xqucode =orgaArea.getXiaqucode();
                                     String xiaquname =orgaArea.getXiaquname();
                                     //添加乡镇链接
                                     orgaArea.setXiaquname("<a href=javascript:openEdit('" + taskGuid
                                             + "','区县延伸配置','"+xqucode+"')>"+xiaquname+"</a>");
                                     // 排序
                                     orgaArea.put("id", first + i + 1);
                                     orgaArea.put("taskname", taskname);
                                     orgaArea.put("type", typename);
                                     // 判断是否已选择
                                     int authCount = authService
                                             .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                     orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_ZCBS)
                                             .getResult();
                                     if (authCount > 0) {
                                         orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                     }
                                     else {
                                         orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                     }
                                     // orgaArea.put("frList",
                                     // StringUtil.change2ArrayList(null));
                                 }
                             }
                         }
                    }
                    else if("3".equals(citylevel)){
                    //乡镇点击进入方法
                        if (list != null) {
                            if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                                // 获取当前事项所有办理环节，并保存固定环节标识，插入数组
                                String[] riskPointIdList = new String[activityGuidList.size()];
                                for (int i = 0; i < riskPointIdList.length; i++) {
                                    AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint
                                            .getAuditTaskRiskpointByActivityguid(activityGuidList.get(i), false)
                                            .getResult();
                                    if (auditTaskRiskpoint != null) {
                                        riskPointIdList[i] = auditTaskRiskpoint.getRiskpointid();
                                    }
                                }

                                // 逐条处理乡镇信息
                                for (int i = 0; i < list.size(); i++) {
                                    AuditOrgaArea orgaArea = list.get(i);
                                    String xqucode =orgaArea.getXiaqucode();
                                    String xiaquname =orgaArea.getXiaquname();
                                    //添加乡镇链接
                                    orgaArea.setXiaquname("<a href=javascript:openEdit('" + taskGuid
                                            + "','村（社区）延伸配置','"+xqucode+"')>"+xiaquname+"</a>");

                                    // 排序
                                    orgaArea.put("id", first + i + 1);
                                    orgaArea.put("taskname", taskname);
                                    orgaArea.put("type", typename);
                                    // 获取当前事项当前乡镇授权固定环节标识
                                    List<String> riskpointid = authService
                                            .getRpIdByTaskIdAndOuguid(auditTask.getTask_id(), orgaArea.getOuguid())
                                            .getResult();
                                    // 判断是否已选择
                                    if (riskPointIdList.length > 0) {
                                        for (int j = 0; j < riskPointIdList.length; j++) {
                                            if (riskpointid.contains(riskPointIdList[j])) {
                                                orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ONE);
                                            }
                                            else {
                                                orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ZERO);
                                            }
                                        }
                                        orgaArea.put("frList", StringUtil.change2ArrayList(riskPointIdList));
                                    }
                                    else {
                                        // 判断是否已选择
                                        int authCount = authService
                                                .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                        orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_SJWT)
                                                .getResult();
                                        if (authCount > 0) {
                                            orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                        }
                                        else {
                                            orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                        }
                                    }
                                }
                            }
                            else {
                                // 逐条处理乡镇信息
                                for (int i = 0; i < list.size(); i++) {
                                    AuditOrgaArea orgaArea = list.get(i);
                                    String xqucode =orgaArea.getXiaqucode();
                                    String xiaquname =orgaArea.getXiaquname();
                                    //添加乡镇链接
                                    orgaArea.setXiaquname("<a href=javascript:openEdit('" + taskGuid
                                            + "','村（社区）延伸配置','"+xqucode+"')>"+xiaquname+"</a>");
                                    // 排序
                                    orgaArea.put("id", first + i + 1);
                                    orgaArea.put("taskname", taskname);
                                    orgaArea.put("type", typename);
                                    // 判断是否已选择
                                    int authCount = authService
                                            .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                    orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_ZCBS)
                                            .getResult();
                                    if (authCount > 0) {
                                        orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                    }
                                    else {
                                        orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                    }
                                    // orgaArea.put("frList",
                                    // StringUtil.change2ArrayList(null));
                                }
                            }
                        }
                    }
                    //村（社区）点击进入方法
                    else if("4".equals(citylevel)){
                        //乡镇点击进入方法
                        if (list != null) {
                            if (ZwfwConstant.TASKDELEGATE_TYPE_SJWT.equals(delegatetype)) {
                                // 获取当前事项所有办理环节，并保存固定环节标识，插入数组
                                String[] riskPointIdList = new String[activityGuidList.size()];
                                for (int i = 0; i < riskPointIdList.length; i++) {
                                    AuditTaskRiskpoint auditTaskRiskpoint = iAuditTaskRiskpoint
                                            .getAuditTaskRiskpointByActivityguid(activityGuidList.get(i), false)
                                            .getResult();
                                    if (auditTaskRiskpoint != null) {
                                        riskPointIdList[i] = auditTaskRiskpoint.getRiskpointid();
                                    }
                                }

                                // 逐条处理乡镇信息
                                for (int i = 0; i < list.size(); i++) {
                                    AuditOrgaArea orgaArea = list.get(i);
                                    // 排序
                                    orgaArea.put("id", first + i + 1);
                                    orgaArea.put("taskname", taskname);
                                    orgaArea.put("type", typename);
                                    // 获取当前事项当前乡镇授权固定环节标识
                                    List<String> riskpointid = authService
                                            .getRpIdByTaskIdAndOuguid(auditTask.getTask_id(), orgaArea.getOuguid())
                                            .getResult();
                                    // 判断是否已选择
                                    if (riskPointIdList.length > 0) {
                                        for (int j = 0; j < riskPointIdList.length; j++) {
                                            if (riskpointid.contains(riskPointIdList[j])) {
                                                orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ONE);
                                            }
                                            else {
                                                orgaArea.put("selected" + j, ZwfwConstant.CONSTANT_INT_ZERO);
                                            }
                                        }
                                        orgaArea.put("frList", StringUtil.change2ArrayList(riskPointIdList));
                                    }
                                    else {
                                        // 判断是否已选择
                                        int authCount = authService
                                                .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                        orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_SJWT)
                                                .getResult();
                                        if (authCount > 0) {
                                            orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                        }
                                        else {
                                            orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                        }
                                    }
                                }
                            }
                            else {
                                // 逐条处理乡镇信息
                                for (int i = 0; i < list.size(); i++) {
                                    AuditOrgaArea orgaArea = list.get(i);
                                    // 排序
                                    orgaArea.put("id", first + i + 1);
                                    orgaArea.put("taskname", taskname);
                                    orgaArea.put("type", typename);
                                    // 判断是否已选择
                                    int authCount = authService
                                            .getAuthCountByTaskIdAndOuguidAndDtype(auditTask.getTask_id(),
                                                    orgaArea.getOuguid(), ZwfwConstant.TASKDELEGATE_TYPE_ZCBS)
                                            .getResult();
                                    if (authCount > 0) {
                                        orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ONE);
                                    }
                                    else {
                                        orgaArea.put("selected0", ZwfwConstant.CONSTANT_INT_ZERO);
                                    }
                                    // orgaArea.put("frList",
                                    // StringUtil.change2ArrayList(null));
                                }
                            }
                        }
                    }
                    
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    /**
     * 递归环节排序
     *  
     *  @param fromactivityguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private void recursiveActivityGuid(String fromactivityguid) {
        String nextActivityGuids = getNextActivityGuid(fromactivityguid);
        if (nextActivityGuids != null && nextActivityGuids.length() > 0) {
            String[] nextActivityGuidList = nextActivityGuids.split(";");
            if (nextActivityGuidList != null && nextActivityGuidList.length > 0) {
                if (nextActivityGuidList.length == 1) {
                    // 如果环节记录集合中已存在，则跳过
                    if (!activityGuidList.contains(nextActivityGuidList[0])) {
                        activityGuidList.add(nextActivityGuidList[0]);
                        // 继续递归
                        recursiveActivityGuid(nextActivityGuidList[0]);
                    }
                }
                else if (nextActivityGuidList.length > 1) {
                    for (int j = 0; j < nextActivityGuidList.length; j++) {
                        // 如果环节记录集合中已存在，则跳过
                        if (!activityGuidList.contains(nextActivityGuidList[j])) {
                            activityGuidList.add(nextActivityGuidList[j]);
                        }
                        // 继续递归
                        recursiveActivityGuid(nextActivityGuidList[j]);
                    }
                }
            }
        }
    }

    /**
     * 
     * 获取下个岗位标识
     * 
     */
    private String getNextActivityGuid(String fromactivityguid) {
        String toactivityguid = "";
        List<WorkflowActivity> toActivityList = workflowActivityService9
                .selectActivityListByFromActivityGuid(fromactivityguid);
        if (toActivityList != null && !toActivityList.isEmpty()) {
            if (toActivityList.size() == 1) {
                if (!"结束".equals(toActivityList.get(0).getActivityName())) {
                    toactivityguid = toActivityList.get(0).getActivityGuid();
                }
            }
            else {
                for (int i = 0; i < toActivityList.size(); i++) {
                    if (!"结束".equals(toActivityList.get(i).getActivityName())) {
                        toactivityguid += toActivityList.get(i).getActivityGuid() + ";";
                    }
                }
            }
        }
        return toactivityguid;
    }

    public AuditTaskDelegateAuth getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskDelegateAuth();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskDelegateAuth dataBean) {
        this.dataBean = dataBean;
    }

    public String getXiaquname() {
        return xiaquname;
    }

    public String getXiaqucode() {
        return xiaqucode;
    }

    public void setXiaquname(String xiaquname) {
        this.xiaquname = xiaquname;
    }

    public void setXiaqucode(String xiaqucode) {
        this.xiaqucode = xiaqucode;
    }

    public List<SelectItem> getDelegateTypeModel() {
        if (delegateTypeModel == null) {
            delegateTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "乡镇延伸", null, false));
        }
        return delegateTypeModel;
    }

    public void setDelegateTypeModel(List<SelectItem> delegateTypeModel) {
        this.delegateTypeModel = delegateTypeModel;
    }

    public String getDelegatetype() {
        return delegatetype;
    }

    public void setDelegatetype(String delegatetype) {
        this.delegatetype = delegatetype;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

}
