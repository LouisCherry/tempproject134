package com.epoint.expert.expertinstance.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.expert.expertcompanyavoid.api.IExpertCompanyavoidService;
import com.epoint.expert.expertinfo.api.IExpertInfoService;
import com.epoint.expert.expertinstance.api.IExpertInstanceService;
import com.epoint.expert.expertinstance.api.entity.ExpertInstance;
import com.epoint.expert.expertiresult.api.IExpertIResultService;
import com.epoint.expert.expertiresult.api.entity.ExpertIResult;
import com.epoint.expert.expertirule.api.IExpertIRuleService;
import com.epoint.expert.expertirule.api.entity.ExpertIRule;
import com.epoint.expert.expertisms.api.IExpertISmsService;
import com.epoint.expert.expertisms.api.entity.ExpertISms;
import com.epoint.expert.experttask.api.IExpertTaskService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.util.SqlUtils;

/**
 * 专家抽取实例表新增页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */

@RestController("expertinstanceaddaction")
@Scope("request")
public class ExpertInstanceAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IExpertInstanceService service;
    @Autowired
    private IExpertIRuleService expertIRuleService;
    @Autowired
    private IExpertIResultService expertIResultService;
    @Autowired
    private IExpertInfoService expertInfoService;
    @Autowired
    private IExpertCompanyavoidService companyavoidServcie;
    @Autowired
    private IExpertISmsService smsService;
    @Autowired
    private IAuditTask taskService;
    @Autowired
    private IExpertTaskService expertTaskService;

    /**
     * 专家抽取实例表实体对象
     */
    private ExpertInstance dataBean = null;

    /**
    * 评标地点下拉列表model
    */
    private List<SelectItem> bidaddressModel = null;
    /**
     * 评标耗时下拉列表model
     */
    private List<SelectItem> bidcostModel = null;
    /**
     * 是否已抽取单选按钮组model
     */
    private List<SelectItem> is_extractedModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertIRule> avoidCompanyModel;
    /**
     * 表格控件model
     */
    private DataGridModel<ExpertIRule> avoidExpertModel;
    /**
     * 表格控件model
     */
    private DataGridModel<ExpertIRule> professionModel;
    /**
     * 表格控件model
     */
    private DataGridModel<ExpertISms> smsModel;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertIResult> resultModel;

    @Autowired
    private IHandleFrameOU frameOu;
    private TreeModel treeModel = null;
    private String fields = "rowguid,task_id,item_id,type,taskname,ouguid";

    private String rowguid;
    /**
     * 可抽取专家数
     */
    private int availableExpertCount;

    private String taskcodes;
    private static final String RULEOBJECT_COMPANY = "1";
    private static final String RULEOBJECT_EXPERT = "2";
    private static final String RULEOBJECT_PROFESSION = "3";

    public void pageLoad() {
        rowguid = getRequestParameter("rowguid");
        if (StringUtil.isNotBlank(rowguid)) {
            dataBean = service.find(rowguid);
        }
        else {
            dataBean = new ExpertInstance();
        }

    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        String[] taskarray = taskcodes.split("&");
        dataBean.setItem_id(taskarray[2]);
        dataBean.set("taskguid", taskarray[0]);
        dataBean.setTaskname(taskService.getAuditTaskByGuid(taskarray[0], false).getResult().getTaskname());
        rowguid = UUID.randomUUID().toString();
        dataBean.setRowguid(rowguid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setIs_extracted(ZwfwConstant.CONSTANT_STR_ZERO);
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        addCallbackParam("rowguid", rowguid);
    }

    public DataGridModel<ExpertIRule> getDataGridAvoidCompany() {
        // 获得表格对象
        if (avoidCompanyModel == null) {
            avoidCompanyModel = new DataGridModel<ExpertIRule>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<ExpertIRule> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sqlUtil = new SqlUtils();
                    sqlUtil.eq("instanceguid", rowguid);
                    sqlUtil.eq("objecttype", RULEOBJECT_COMPANY);
                    List<ExpertIRule> list = expertIRuleService.findListByCondition(sqlUtil.getMap());
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return avoidCompanyModel;
    }

    public DataGridModel<ExpertIRule> getDataGridAvoidExpert() {
        // 获得表格对象
        if (avoidExpertModel == null) {
            avoidExpertModel = new DataGridModel<ExpertIRule>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<ExpertIRule> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sqlUtil = new SqlUtils();
                    sqlUtil.eq("instanceguid", rowguid);
                    sqlUtil.eq("objecttype", RULEOBJECT_EXPERT);
                    List<ExpertIRule> list = expertIRuleService.findListByCondition(sqlUtil.getMap());
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return avoidExpertModel;
    }

    public DataGridModel<ExpertIRule> getDataGridProfession() {
        // 获得表格对象
        if (professionModel == null) {
            professionModel = new DataGridModel<ExpertIRule>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<ExpertIRule> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sqlUtil = new SqlUtils();
                    sqlUtil.eq("instanceguid", rowguid);
                    sqlUtil.eq("objecttype", RULEOBJECT_PROFESSION);
                    List<ExpertIRule> list = expertIRuleService.findListByCondition(sqlUtil.getMap());
                    this.setRowCount(list.size());
                    return list;
                }

            };
        }
        return professionModel;
    }

    public DataGridModel<ExpertIResult> getDataGridResult() {
        // 获得表格对象
        if (resultModel == null) {
            resultModel = new DataGridModel<ExpertIResult>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 5834872773407058979L;

                @Override
                public List<ExpertIResult> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlUtils sqlUtil = new SqlUtils();
                    sqlUtil.eq("instanceguid", rowguid);
                    ;
                    List<ExpertIResult> list = expertIResultService.findListByCondition(sqlUtil.getMap());
                    return list;
                }

            };
        }
        return resultModel;
    }

    public DataGridModel<ExpertISms> getSmsDataGridData() {
        // 获得表格对象
        if (smsModel == null) {
            smsModel = new DataGridModel<ExpertISms>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<ExpertISms> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    // 查询条件
                    conditionSql = conditionSql + " and InstanceGuid=?";
                    conditionList.add(rowguid);
                    List<ExpertISms> list = smsService.findList(
                            ListGenerator.generateSql("Expert_I_SMS", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = smsService
                            .findList(ListGenerator.generateSql("Expert_I_SMS", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return smsModel;
    }
/**
 * 
 * 获取可抽取专家数量
 *  @return    
 * @exception/throws [违例类型] [违例说明]
 * @see [类、类#方法、类#成员]
 */
    public List<String> getAvailableExpert() {
        String[] taskarray = taskcodes.split("&");
        dataBean.setItem_id(taskarray[2]);
        dataBean.set("taskguid", taskarray[0]);
        dataBean.setTaskname(taskService.getAuditTaskByGuid(taskarray[0], false).getResult().getTaskname());
        service.update(dataBean);
        SqlUtils sqlUtil = new SqlUtils();
        sqlUtil.eq("instanceguid", rowguid);
        sqlUtil.eq("objecttype", RULEOBJECT_COMPANY);
        List<ExpertIRule> avoidCompanyList = expertIRuleService.findListByCondition(sqlUtil.getMap());
        sqlUtil.clear();
        sqlUtil.eq("instanceguid", rowguid);
        sqlUtil.eq("objecttype", RULEOBJECT_EXPERT);
        List<ExpertIRule> avoidExpertList = expertIRuleService.findListByCondition(sqlUtil.getMap());
        sqlUtil.clear();
        sqlUtil.eq("instanceguid", rowguid);
        sqlUtil.eq("objecttype", RULEOBJECT_PROFESSION);
        List<ExpertIRule> professionList = expertIRuleService.findListByCondition(sqlUtil.getMap());
        List<String> professionExpertList = new ArrayList<>();
        List<String> ruleExpertList = new ArrayList<>();
        List<String> avoidExpertGuidsList = new ArrayList<>();
        List<String> avoidCompanyExpertList = new ArrayList<>();
        AuditTask task = taskService.getAuditTaskByGuid(dataBean.get("taskguid"), false).getResult();
        //事项对应的专家
        List<String> taskExpertList = expertTaskService.getExpertGuidsByTaskid(task.getTask_id());
        if (taskExpertList.size() == 0) {
            addCallbackParam("msg", "当前事项未关联专家！");
            return null;
        }
        //限定了评标专业
        if (professionList.size() > 0) {
            for (int i = 0; i < professionList.size(); i++) {
                    professionExpertList = expertInfoService
                            .getExpertGuidsByProfession(professionList.get(i).getObjectguid());
                    //事项与专业对应的专家取交集
                    taskExpertList.retainAll(professionExpertList);
     
            }
        }
        //设置了回避专家
        if (avoidExpertList.size() > 0) {
            for (ExpertIRule rule : avoidExpertList) {
                //回避专家guid
                avoidExpertGuidsList.add(rule.getObjectguid());
            }
        }
        //设置了回避单位
        if (avoidCompanyList.size() > 0) {
            String companyGuids = "";
            List<String> companyList = new ArrayList<>();
            for (int i = 0; i < avoidCompanyList.size(); i++) {
                companyList.add(avoidCompanyList.get(i).getObjectguid());
            }
            companyGuids = String.join("','", companyList);
            //TODO 级联删除
            avoidCompanyExpertList = companyavoidServcie.getExpertGuidsByAvoidCompanyGuid(companyGuids);
        }
        //抽取规则
        if (dataBean.getAvoiddays() != null || dataBean.getAvoidtimes() != null) {
            ruleExpertList = service.getExtractRule(dataBean.getBidtime(), dataBean.getAvoiddays(),
                    dataBean.getAvoidtimes());
        }
      
        taskExpertList.removeAll(avoidCompanyExpertList);
        taskExpertList.removeAll(ruleExpertList);
        taskExpertList.removeAll(avoidExpertGuidsList);
        availableExpertCount = taskExpertList.size();
        addCallbackParam("availablecount", availableExpertCount);
        return taskExpertList;
    }

    public void extractExpert() {
        String[] taskarray = taskcodes.split("&");
        dataBean.setItem_id(taskarray[2]);
        dataBean.set("taskguid", taskarray[0]);
        dataBean.setTaskname(taskService.getAuditTaskByGuid(taskarray[0], false).getResult().getTaskname());
        dataBean.setIs_extracted("1");
        service.update(dataBean);
        List<String> availableExpertList = getAvailableExpert();
        List<String> chosenExperts = new ArrayList<>();
        for (int i = 0; i < dataBean.getExtractnum(); i++) {
            chosenExperts.add(availableExpertList.remove(new Random().nextInt(availableExpertList.size())));
        }
        String expertGuids = String.join(",", chosenExperts);
        addCallbackParam("expertguids", expertGuids);
        availableExpertList.removeAll(chosenExperts);
        addCallbackParam("unchosenexpertguids", String.join(",", availableExpertList));
    }
    
    public void getUnchosenExperts(){
        List<String> resultExpertList=expertIResultService.selectExpertByInstanceguid(rowguid, "1");
        List<String> availableExpertList = getAvailableExpert();
        availableExpertList.removeAll(resultExpertList);
        addCallbackParam("unchosenexpertguids", String.join(",", availableExpertList));
    }

    public void extractExpertAgain() {
        //先删除结果表
        service.deleteResultByInstanceGuid(rowguid);
        service.deleteSmsByInstanceGuid(rowguid);
        //再抽取
        extractExpert();
    }

    public ExpertInstance getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertInstance();
        }
        return dataBean;
    }

    public void setDataBean(ExpertInstance dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getBidaddressModel() {
        if (bidaddressModel == null) {
            bidaddressModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_评标地点", null, false));
        }
        return this.bidaddressModel;
    }

    public List<SelectItem> getBidcostModel() {
        if (bidcostModel == null) {
            bidcostModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_评标耗时", null, false));
        }
        return this.bidcostModel;
    }

    public List<SelectItem> getIs_extractedModel() {
        if (is_extractedModel == null) {
            is_extractedModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_extractedModel;
    }

    public TreeModel getTaskTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 7262093133578183235L;

                /***
                 * 加载树，懒加载
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    List<AuditTask> listTask = new ArrayList<>();
                    List<FrameOu> listOu = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        if ("root".equals(objectGuid)) {
                            listOu = frameOu.getWindowOUList(ZwfwUserSession.getInstance().getAreaCode(), true)
                                    .getResult();
                        }
                        else {
                            if ("true".equals(treeNode.getColumns().get("isOU"))) {
                                sql.clear();
                                sql.eq("OUGUID", treeData.getObjectGuid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                listTask = taskService.getAuditTaskPageData(sql.getMap(), 0, 0, "ordernum", "desc")
                                        .getResult().getList();
                            }
                        }

                        // 部门绑定
                        for (int i = 0; i < listOu.size(); i++) {
                            if ("root".equals(objectGuid) || (listOu.get(i).getParentOuguid().equals(objectGuid))) {
                                TreeNode node = new TreeNode();
                                node.setId(listOu.get(i).getOuguid());
                                node.setText(listOu.get(i).getOuname());
                                if ("root".equals(objectGuid)) {
                                    node.setPid("root");
                                }
                                else {
                                    node.setPid(listOu.get(i).getParentOuguid());
                                }
                                node.setCkr(false);
                                node.getColumns().put("isOU", "true");//标记：是部门节点
                                node.setLeaf(false);
                                int taskCount = 0;
                                sql.clear();
                                sql.eq("OUGUID", listOu.get(i).getOuguid());
                                sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                                sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                                sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                                taskCount = taskService.getTasknumByCondition(sql.getMap()).getResult();
                                if (taskCount > 0) {
                                    list.add(node);
                                }
                            }
                        }
                        //事项的绑定
                        String itemleft;
                        int taskCount = 0;
                        for (int j = 0; j < listTask.size(); j++) {
                            TreeNode node2 = new TreeNode();
                            node2.setId(listTask.get(j).getRowguid() + "&" + listTask.get(j).getTask_id() + "&"
                                    + listTask.get(j).getItem_id());
                            node2.setText(listTask.get(j).getTaskname().replace(",", "，"));
                            node2.setPid(listTask.get(j).getOuguid());
                            node2.getColumns().put("isOU", "false");//标记：不是部门节点
                            node2.setLeaf(true);
                            sql.clear();
                            sql.eq("ISTEMPLATE", ZwfwConstant.CONSTANT_STR_ZERO);
                            sql.eq("IS_EDITAFTERIMPORT", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.eq("IS_ENABLE", ZwfwConstant.CONSTANT_STR_ONE);
                            sql.isBlankOrValue("IS_HISTORY", ZwfwConstant.CONSTANT_STR_ZERO);
                            itemleft = listTask.get(j).getItem_id();
                            itemleft = itemleft.substring(0, itemleft.length() - 3);
                            sql.nq("item_id", listTask.get(j).getItem_id());
                            sql.leftLike("item_id", itemleft);
                            node2.setPid(objectGuid);
                            node2.setText(listTask.get(j).getTaskname());
                            if (taskCount > 0) {
                                node2.setLeaf(false);
                                node2.getColumns().put("item_id", listTask.get(j).getItem_id());
                            }
                            list.add(node2);

                        }
                    }

                    return list;
                }
            };
        }
        if (dataBean.get("taskguid") != null) {
            AuditTask task = taskService.getAuditTaskByGuid(dataBean.get("taskguid"), false).getResult();
            if (task != null) {
                List<SelectItem> codeList = new ArrayList<>();
                SelectItem item = new SelectItem();
                item.setText(dataBean.getTaskname());
                item.setValue(task.getRowguid() + "&" + task.getTask_id() + "&" + dataBean.getItem_id());
                codeList.add(item);
                treeModel.setSelectNode(codeList);
            }

        }
        return treeModel;
    }

    public String getTaskcodes() {
        return taskcodes;
    }

    public void setTaskcodes(String taskcodes) {
        this.taskcodes = taskcodes;
    }

    public void deleteSmsSelect() {
        List<String> select = getSmsDataGridData().getSelectKeys();
        for (String sel : select) {
            smsService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public void deleteResultSelect() {
        List<String> select = getDataGridResult().getSelectKeys();
        for (String sel : select) {
            expertIResultService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

}
