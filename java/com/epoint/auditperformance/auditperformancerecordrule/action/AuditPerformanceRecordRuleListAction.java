package com.epoint.auditperformance.auditperformancerecordrule.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录考评规则list页面对应的后台
 * 
 * @author 泪流云
 * @version [版本号, 2018-01-09 16:07:06]
 */
@RestController("auditperformancerecordrulelistaction")
@Scope("request")
public class AuditPerformanceRecordRuleListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -7326778865541876437L;
    @Autowired
    private IAuditPerformanceRecordRuleService service;
    @Autowired
    private IAuditPerformanceRule iAuditPerformanceRule;

    /**
     * 考评记录考评规则实体对象
     */
    private AuditPerformanceRecordRule dataBean;
    private LazyTreeModal9 treeModel = null;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    private String recordRowguid;
    /**
    * 考评对象类别下拉列表model
    */
    private List<SelectItem> objecttypeModel = null;
    /**
     * 考评项目下拉列表model
     */
    private List<SelectItem> projectModel = null;
    private AuditPerformanceRecord auditPerformanceRecord;
    private String objecttype = "";

    @Override
    public void pageLoad() {
        recordRowguid = this.getRequestParameter("recordrowguid");
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())) {
            addCallbackParam("msg", "人员没有分配到中心!");
        }
        auditPerformanceRecord = CommonDao.getInstance().find(AuditPerformanceRecord.class, recordRowguid);
        objecttype = auditPerformanceRecord.getObjecttype();
    }

    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllRule());
            if (!isPostback()) {
                treeModel.setSelectNode(getSelectRule(recordRowguid));
            }
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("所有规则");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    public List<SelectItem> getSelectRule(String recordRowguid) {
        List<SelectItem> ruleItemList = new ArrayList<SelectItem>();
        if (StringUtil.isNotBlank(recordRowguid)) {
            List<AuditPerformanceRecordRule> ruleList = service.findListByCondition(recordRowguid,
                    ZwfwUserSession.getInstance().getCenterGuid(),objecttype);
            if (ruleList != null && ruleList.size() > 0) {
                for (AuditPerformanceRecordRule rule : ruleList) {
                    ruleItemList.add(new SelectItem(rule.getRuleguid(), rule.getRulename()));
                }
            }
        }
        return ruleItemList;
    }

    public SimpleFetchHandler9 loadAllRule() {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {

                List listrule = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    listrule = iAuditPerformanceRule.selectRuleListByCondition(conndition, objecttype,
                            ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                }
                return listrule;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                list = iAuditPerformanceRule
                        .selectRuleListByCondition("", objecttype, ZwfwUserSession.getInstance().getCenterGuid())
                        .getResult();
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                return 0;
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeDataList = new ArrayList<TreeData>();
                if (dataList != null && dataList.size() > 0) {
                    for (Object ob : dataList) {
                        if (ob instanceof AuditPerformanceRule) {
                            AuditPerformanceRule auditPerformanceRule = (AuditPerformanceRule) ob;
                            TreeData treeData = new TreeData();
                            treeData.setObjectcode(auditPerformanceRule.getRowguid());
                            treeData.setTitle(auditPerformanceRule.getRulename());
                            treeDataList.add(treeData);
                        }
                    }
                }
                return treeDataList;

            }
        };
        return fetchHandler9;
    }

    public AuditPerformanceRecordRule getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecordRule();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecordRule dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getObjecttypeModel() {
        if (objecttypeModel == null) {
            objecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, true));
        }
        return this.objecttypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getProjectModel() {
        if (projectModel == null) {
            projectModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评项目", null, true));
        }
        return this.projectModel;
    }

    public void saveRuleToRecord(String guidList, String recordRowguid) {
        if (StringUtil.isNotBlank(recordRowguid)) {
            // 添加新的考评规则关联关系
            if (StringUtil.isNotBlank(guidList)) {
                String[] array = guidList.split("_SPLIT_");
                
                if (array.length > 0) {
                    String guids = "";
                    String[] ruleGuids = array[0].split(";");
                    for (int i = 0; i < ruleGuids.length; i++) {
                        //先根据ruleGuids[i]和recordRowguid判断这个规则在记录规则表是否存在，不存在就插入，存在就不进行任何操作
                        String rowguid = service.getRowguid(recordRowguid, ruleGuids[i]);
                        if (StringUtil.isBlank(rowguid)) {
                            //获取考评记录实例
                            auditPerformanceRecord = CommonDao.getInstance().find(AuditPerformanceRecord.class,
                                    recordRowguid);
                            //获取考评规则实例
                            AuditPerformanceRule auditPerformanceRule = new AuditPerformanceRule();
                            auditPerformanceRule = CommonDao.getInstance().find(AuditPerformanceRule.class,
                                    ruleGuids[i]);
                            AuditPerformanceRecordRule auditPerformanceRecordRule = new AuditPerformanceRecordRule();
                            auditPerformanceRecordRule.setRowguid(UUID.randomUUID().toString());
                            auditPerformanceRecordRule.setRuleguid(ruleGuids[i]);
                            auditPerformanceRecordRule.setRulename(auditPerformanceRule.getRulename());
                            auditPerformanceRecordRule.setRecordrowguid(recordRowguid);
                            auditPerformanceRecordRule.setRecordname(auditPerformanceRecord.getRecordname());
                            auditPerformanceRecordRule.setRulescore(auditPerformanceRule.getRulescore());
                            auditPerformanceRecordRule.setObjecttype(auditPerformanceRule.getObjecttype());
                            auditPerformanceRecordRule.setOrdernum(auditPerformanceRule.getOrdernum());
                            auditPerformanceRecordRule.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
                            CommonDao.getInstance().insert(auditPerformanceRecordRule);
                        }
                        //拼接查询条件
                        guids += "'" + ruleGuids[i] + "',";
                        
                    }
                    //最后删除没有被选中的垃圾数据
                    guids = guids.substring(0, guids.length() - 1);
                    service.deleteRecordRuleByRecordGuid(recordRowguid,guids);
                }
                else {
                    //判断选中的规则为空，那么删除该记录下面所以规则
                    service.deleteRecordRuleByRecordGuid(recordRowguid,"");
                }
            }
        }
    }
}
