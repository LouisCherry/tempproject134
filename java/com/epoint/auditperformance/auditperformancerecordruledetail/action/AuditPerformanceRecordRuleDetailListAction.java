package com.epoint.auditperformance.auditperformancerecordruledetail.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.FetchHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录考评细则list页面对应的后台
 * 
 * @author 泪流云
 * @version [版本号, 2018-01-09 16:07:13]
 */
@RestController("auditperformancerecordruledetaillistaction")
@Scope("request")
public class AuditPerformanceRecordRuleDetailListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -7761347527541580591L;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService service;
    @Autowired
    private IAuditPerformanceRecordRuleService iAuditPerformanceRecordRuleService;
    @Autowired
    private IAuditPerformanceRuleDetail iAuditPerformanceRuleDetail;
    @Autowired
    private IAuditPerformanceRecordObject iAuditPerformanceRecordObject;
    @Autowired
    private IAuditPerformanceRecord iAuditPerformanceRecord;

    /**
     * 考评记录考评细则实体对象
     */
    private AuditPerformanceRecordRuleDetail dataBean;
    /**
     * 考评记录实体对象
     */
    private AuditPerformanceRecord auditPerformanceRecord;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceRecordRuleDetail> model;
    /**
     * 规则树model
     */
    private LazyTreeModal9 treeModel = null;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    private String recordrowguid;
    /**
    * 考评对象类别下拉列表model
    */
    private List<SelectItem> objecttypeModel = null;
    /**
     * 考评方式下拉列表model
     */
    private List<SelectItem> typeModel = null;
    private String leftTreeNodeGuid;
    private String detailRuleName;
    private String recordrulerowguid;

    private String objecttype = "";

    @Override
    public void pageLoad() {
        recordrowguid = this.getRequestParameter("recordrowguid");
        recordrulerowguid = this.getRequestParameter("recordrulerowguid");
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())) {
            addCallbackParam("msg", "人员没有分配到中心!");
        }
        auditPerformanceRecord = CommonDao.getInstance().find(AuditPerformanceRecord.class, recordrowguid);
        objecttype = auditPerformanceRecord.getObjecttype();
    }

    /***
     * 
     * 用treebean构造细则
     * 
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public LazyTreeModal9 getTreeModelXZ() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(loadAllXZ(recordrowguid));
            if (!isPostback()) {
                treeModel.setSelectNode(getSelectXZ(recordrowguid));
            }
            treeModel.setTreeType(ConstValue9.CHECK_SINGLE);
            treeModel.setInitType(ConstValue9.GUID_INIT);
            treeModel.setRootName("所有规则");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    public List<SelectItem> getSelectXZ(String recordRowguid) {
        List<SelectItem> detailItemList = new ArrayList<SelectItem>();
        List<AuditPerformanceRecordRuleDetail> ruleDetailList;
        if (StringUtil.isNotBlank(recordRowguid)) {
            if (StringUtil.isBlank(recordrulerowguid)) {
                ruleDetailList = service.findListByRecordRowguid(recordRowguid);
            }
            else {
                ruleDetailList = service.findListByRecordRowguid(recordRowguid, recordrulerowguid);
            }

            if (ruleDetailList != null && ruleDetailList.size() > 0) {
                for (AuditPerformanceRecordRuleDetail detail : ruleDetailList) {
                    detailItemList.add(new SelectItem(detail.getDetailruleGuid(), detail.getDetailrulename()));
                }
            }
        }
        return detailItemList;
    }

    /**
     * 验证对象是否配置
     */
    public void validate() {
        String msg = "";
        int objcount = iAuditPerformanceRecordObject.findFieldByRecordRowguid("objectguid", recordrowguid, objecttype)
                .getResult().size();
        int detailcount = service.findListByRecordRowguid(recordrowguid).size();
        if (objcount <= 0) {
            msg = "nullobj";
        }
        else if (detailcount <= 0) {
            msg = "nulldetail";
        }
        addCallbackParam("msg", msg);
    }

    public String yanzheng(AuditPerformanceRecordRuleDetail detail) {
        String msg = "";
        int highscore = detail.getHighscore();
        int stdscore = detail.getStdscore();
        int singleaddscore = detail.getSingleaddscore();
        int singleminusscore = detail.getSingleminusscore();

        /* if (stdscore < singleaddscore) {
            msg = "考评基准分必须大于等于单次加分！";

        }*/
        if (stdscore < singleminusscore) {
            msg = "考评基准分必须大于等于单次减分！";

        }
        if (stdscore + singleaddscore > highscore) {
            msg = "考评基准分加单次加分必须小于等于上限分值！";

        }
        if (singleaddscore == 0 && singleminusscore == 0) {
            msg = "单次加分和单次减分不能同时为0！";

        }
        return msg;

    }

    /**
     * 保存修改
     */
    String msg = "";

    public void saveAll() {
        //List<AuditPerformanceRecordRuleDetail> ruleDetailList =service.findListByRecordRowguid(auditPerformanceRecord.getRowguid());
        List<AuditPerformanceRecordRuleDetail> ruleDetailList = getDataGridData().getWrappedData();
        msg = "";
        for (AuditPerformanceRecordRuleDetail detail : ruleDetailList) {
            msg = yanzheng(detail);
            if (detail.getOrdernum() == null) {// null会跑到最前
                detail.setOrdernum(0);
            }
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("msg", msg);
                return;
            }
            CommonDao.getInstance().update(detail);
        }

        addCallbackParam("msg", StringUtil.isBlank(msg) ? "保存成功！" : msg);
    }

    /**
     * 开启考评
     * 
     */
    public void start() {
        saveAll();
        if (StringUtil.isBlank(msg)) {
            List<AuditPerformanceRecordRuleDetail> ruleDetailList = service
                    .findListByRecordRowguid(auditPerformanceRecord.getRowguid());
            for (AuditPerformanceRecordRuleDetail auditPerformanceRecordRuleDetail : ruleDetailList) {
                msg = yanzheng(auditPerformanceRecordRuleDetail);
                if (StringUtil.isNotBlank(msg)) {
                    break;
                }
                ;
            }
        }

        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("msg", msg);
            return;
        }

        auditPerformanceRecord.setStatus("2");
        iAuditPerformanceRecord.update(auditPerformanceRecord);
        addCallbackParam("msg", "开启考评成功！");
    }

    /**
     * 
     * 获取细则树
     * 
     * @param recordrowguid
     *            
     * @return LazyTreeModal9
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    public SimpleFetchHandler9 loadAllXZ(String recordrowguid) {
        SimpleFetchHandler9 fetchHandler9 = new SimpleFetchHandler9()
        {
            private static final long serialVersionUID = 1L;

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> search(String conndition) {
                List<AuditPerformanceRecordRule> list = new ArrayList();
                List listdetail = new ArrayList<>();
                if (StringUtil.isNotBlank(conndition)) {
                    if (StringUtil.isBlank(recordrulerowguid)) {
                        list = iAuditPerformanceRecordRuleService.findListByCondition(recordrowguid,
                                ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                    }
                    else {
                        list = iAuditPerformanceRecordRuleService.findListByRecordRowguid(recordrowguid,
                                ZwfwUserSession.getInstance().getCenterGuid(), recordrulerowguid);
                    }
                    if (list != null && list.size() > 0) {
                        for (AuditPerformanceRecordRule rule : list) {
                            listdetail.addAll(iAuditPerformanceRuleDetail
                                    .selectRuleDetailListByCondition(objecttype, rule.getRuleguid(), conndition)
                                    .getResult());
                        }
                    }
                }
                return listdetail;
            }

            @Override
            @SuppressWarnings({"rawtypes", "unchecked" })
            public <T> List<T> fetchData(int level, TreeData treeData) {
                List list = new ArrayList();
                // 一开始加载或者点击节点的时候触发
                if (level == FetchHandler9.FETCH_ONELEVEL) {
                    // 最开始加载的时候，把所有的规则加载出来，最开始treeData的guid为空
                    if (StringUtil.isBlank(treeData.getObjectGuid())) {
                        if (StringUtil.isBlank(recordrulerowguid)) {
                            list = iAuditPerformanceRecordRuleService.findListByCondition(recordrowguid,
                                    ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                        }
                        else {
                            list = iAuditPerformanceRecordRuleService.findListByRecordRowguid(recordrowguid,
                                    ZwfwUserSession.getInstance().getCenterGuid(), recordrulerowguid);
                        }
                        // 如果treeData的guid不为空，则说明该节点下面有子节点，获取该规则部门下的所有的细则
                    }
                    else {
                        list = iAuditPerformanceRuleDetail
                                .selectRuleDetailListByCondition(objecttype, treeData.getObjectGuid(), "").getResult();
                        List<AuditPerformanceRecordRule> ruleList;

                        if (StringUtil.isBlank(recordrulerowguid)) {
                            ruleList = iAuditPerformanceRecordRuleService.findListByCondition(treeData.getObjectGuid(),
                                    ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                        }
                        else {
                            ruleList = iAuditPerformanceRecordRuleService.findListByRecordRowguid(
                                    treeData.getObjectGuid(), ZwfwUserSession.getInstance().getCenterGuid(),
                                    recordrowguid);
                        }

                        if (ruleList != null && ruleList.size() > 0) {
                            for (AuditPerformanceRecordRule rule : ruleList) {
                                list.add(rule);
                            }
                        }
                    }
                }
                // 点击checkbox的时候触发
                else {

                    if ("auditperformancerecordrule".equals(treeData.getObjectcode())) {
                        list = iAuditPerformanceRuleDetail
                                .selectRuleDetailListByCondition(objecttype, treeData.getObjectGuid(), "").getResult();
                        List<AuditPerformanceRecordRule> ruleList = iAuditPerformanceRecordRuleService
                                .findListByCondition(treeData.getObjectGuid(),
                                        ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                        if (ruleList != null && ruleList.size() > 0) {
                            for (AuditPerformanceRecordRule ou : ruleList) {
                                list.add(ou);
                            }
                        }
                    }
                    else {
                        List<AuditPerformanceRecordRule> ruleList = iAuditPerformanceRecordRuleService
                                .findListByCondition(treeData.getObjectGuid(),
                                        ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                        for (AuditPerformanceRecordRule topou : ruleList) {
                            list.addAll(iAuditPerformanceRuleDetail
                                    .selectRuleDetailListByCondition(objecttype, topou.getRuleguid(), "").getResult());
                        }
                    }
                }
                return list;
            }

            @Override
            public int fetchChildCount(TreeData treeData) {
                int a = 0;
                int b = 0;
                if (StringUtil.isBlank(recordrulerowguid)) {
                    a = iAuditPerformanceRecordRuleService.findListByCondition(treeData.getObjectGuid(),
                            ZwfwUserSession.getInstance().getCenterGuid(), objecttype).size();
                }
                else {
                    a = iAuditPerformanceRecordRuleService.findListByRecordRowguid(treeData.getObjectGuid(),
                            ZwfwUserSession.getInstance().getCenterGuid(), recordrowguid).size();
                }

                b = iAuditPerformanceRuleDetail
                        .selectRuleDetailListByCondition(objecttype, treeData.getObjectGuid(), "").getResult().size();
                if (a > 0 || b > 0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }

            @Override
            public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                List<TreeData> treeList = new ArrayList<TreeData>();
                if (dataList != null) {
                    for (Object obj : dataList) {
                        if (obj instanceof AuditPerformanceRecordRule) {
                            AuditPerformanceRecordRule auditPerformanceRecordRule = (AuditPerformanceRecordRule) obj;
                            TreeData treeData = new TreeData();
                            treeData.setObjectGuid(auditPerformanceRecordRule.getRuleguid());
                            treeData.setTitle(auditPerformanceRecordRule.getRulename());
                            // 没有子节点的不允许点击
                            List<AuditPerformanceRuleDetail> detail = iAuditPerformanceRuleDetail
                                    .selectRuleDetailListByCondition(objecttype,
                                            auditPerformanceRecordRule.getRuleguid(), "")
                                    .getResult();
                            int childCount = 0;
                            if (detail != null) {
                                childCount = detail.size();
                            }
                            if (childCount == 0) {
                                treeData.setNoClick(true);
                                treeList.add(treeData);
                            }
                            else {
                                // objectcode的作用是来区分是点击了部门还是事项
                                treeData.setObjectcode("auditperformancerecordrule");
                                treeList.add(treeData);
                            }
                        }

                        if (obj instanceof AuditPerformanceRuleDetail) {
                            AuditPerformanceRuleDetail auditPerformanceRuleDetail = (AuditPerformanceRuleDetail) obj;
                            TreeData treeData = new TreeData();
                            if (auditPerformanceRuleDetail != null) {
                                treeData.setObjectGuid(auditPerformanceRuleDetail.getRowguid());
                                treeData.setTitle(auditPerformanceRuleDetail.getDetailrulename());
                            }
                            treeData.setObjectcode("detail");
                            treeList.add(treeData);
                        }
                    }
                }

                return treeList;
            }

        };
        return fetchHandler9;
    }

    @SuppressWarnings("serial")
    public LazyTreeModal9 getTreeModel() {
        if (treeModel == null) {
            treeModel = new LazyTreeModal9(new SimpleFetchHandler9()
            {

                @Override
                @SuppressWarnings({"rawtypes", "unchecked" })
                public <T> List<T> search(String condition) {
                    List list = new ArrayList();
                    return list;
                }

                @Override
                @SuppressWarnings({"rawtypes", "unchecked" })
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List list = new ArrayList();
                    list = iAuditPerformanceRecordRuleService.findListByCondition(recordrowguid,
                            ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                    return list;
                }

                @Override
                public int fetchChildCount(TreeData arg0) {
                    return 0;
                }

                @Override
                public List<TreeData> changeDBListToTreeDataList(List<?> objlist) {
                    List<TreeData> treeDataList = new ArrayList<TreeData>();
                    if (objlist != null && objlist.size() > 0) {
                        for (Object ob : objlist) {
                            if (ob instanceof AuditPerformanceRecordRule) {
                                AuditPerformanceRecordRule auditPerformanceRecordRule = (AuditPerformanceRecordRule) ob;
                                TreeData treeData = new TreeData();
                                treeData.setObjectcode(auditPerformanceRecordRule.getRowguid());
                                treeData.setTitle(auditPerformanceRecordRule.getRulename());
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }
            });
            treeModel.setRootName("所有考评规则");
            treeModel.setRootSelect(false);
        }
        return treeModel;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditPerformanceRecordRuleDetail> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceRecordRuleDetail>()
            {
                @Override
                public List<AuditPerformanceRecordRuleDetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.setOrderAsc("recordrulename");
                    if (StringUtil.isNotBlank(recordrowguid)) {
                        sql.eq("Recordrowguid", recordrowguid);
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("Recordrulerowguid", leftTreeNodeGuid);
                    }
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    if (StringUtil.isNotBlank(detailRuleName)) {
                        sql.like("Detailrulename", detailRuleName);
                    }
                    else {
                        List<AuditPerformanceRecordRule> list = iAuditPerformanceRecordRuleService.findListByCondition(
                                recordrowguid, ZwfwUserSession.getInstance().getCenterGuid(), objecttype);
                        if (list != null) {
                            String recordRuleRowguids = "'";
                            for (AuditPerformanceRecordRule auditPerformanceRecordRule : list) {
                                recordRuleRowguids += auditPerformanceRecordRule.getRowguid() + "','";
                            }
                            recordRuleRowguids += "'";
                            sql.in("Recordrulerowguid", recordRuleRowguids);
                        }

                    }

                    PageData<AuditPerformanceRecordRuleDetail> pageData = service
                            .getAuditPerformanceRecordRuleDetailPageData(sql.getMap(), first, pageSize, sortField,
                                    sortOrder)
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditPerformanceRecordRuleDetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecordRuleDetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecordRuleDetail dataBean) {
        this.dataBean = dataBean;
    }

    public AuditPerformanceRecord getAuditPerformanceRecord() {
        if (auditPerformanceRecord == null) {
            auditPerformanceRecord = new AuditPerformanceRecord();
        }
        return auditPerformanceRecord;
    }

    public void setAuditPerformanceRecord(AuditPerformanceRecord auditPerformanceRecord) {
        this.auditPerformanceRecord = auditPerformanceRecord;
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
    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评方式", null, true));
        }
        return this.typeModel;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getDetailrulename() {
        return detailRuleName;
    }

    public void setDetailruleName(String detailRuleName) {
        this.detailRuleName = detailRuleName;
    }

    public void saveRuleToRecord(String guidList, String recordRowguid) {
        // 先删除考评记录考评细则下的所有关联的考评细则
        if (StringUtil.isNotBlank(recordRowguid)) {
            if (StringUtil.isBlank(recordrulerowguid)) {
                service.deleteRecordRuleDetailByRecordGuid(recordRowguid);
            }
            else {
                service.deleteRecordRuleDetailByRecordGuid(recordRowguid, recordrulerowguid);
            }

            // 添加新的考评规则关联关系,这边guidlist是细则的
            if (StringUtil.isNotBlank(guidList)) {
                String[] array = guidList.split("_SPLIT_");
                if (array.length > 0) {
                    String[] detailGuids = array[0].split(";");
                    if (StringUtil.isNotBlank(recordRowguid)) {
                        for (int i = 0; i < detailGuids.length; i++) {
                            //获取考评记录实例
                            auditPerformanceRecord = CommonDao.getInstance().find(AuditPerformanceRecord.class,
                                    recordRowguid);
                            //获取考评细则实例
                            AuditPerformanceRuleDetail auditPerformanceRuleDetail = new AuditPerformanceRuleDetail();
                            auditPerformanceRuleDetail = CommonDao.getInstance().find(AuditPerformanceRuleDetail.class,
                                    detailGuids[i]);

                            AuditPerformanceRecordRuleDetail auditPerformanceRecordRuleDetail = new AuditPerformanceRecordRuleDetail();
                            auditPerformanceRecordRuleDetail.setRowguid(UUID.randomUUID().toString());
                            auditPerformanceRecordRuleDetail
                                    .setDetailrulename(auditPerformanceRuleDetail.getDetailrulename());

                            auditPerformanceRecordRuleDetail.setRecordrowguid(auditPerformanceRecord.getRowguid());

                            auditPerformanceRecordRuleDetail.setRecordname(auditPerformanceRecord.getRecordname().replace(",", "，"));
                            //关联考评记录考评规则表的rowguid
                            auditPerformanceRecordRuleDetail.setRecordrulerowguid(iAuditPerformanceRecordRuleService
                                    .getRowguid(recordRowguid, auditPerformanceRuleDetail.getRulerowguid()));

                            auditPerformanceRecordRuleDetail.setRecordrulename(iAuditPerformanceRecordRuleService
                                    .getRecordRuleName(recordRowguid, auditPerformanceRuleDetail.getRulerowguid().replace(",", "，")));

                            auditPerformanceRecordRuleDetail.setObjecttype(auditPerformanceRuleDetail.getObjecttype());
                            auditPerformanceRecordRuleDetail.setType(auditPerformanceRuleDetail.getType());
                            auditPerformanceRecordRuleDetail.setHighscore(auditPerformanceRuleDetail.getHighscore());
                            auditPerformanceRecordRuleDetail.setStdscore(auditPerformanceRuleDetail.getStdscore());
                            auditPerformanceRecordRuleDetail
                                    .setSingleaddscore(auditPerformanceRuleDetail.getSingleaddscore());
                            auditPerformanceRecordRuleDetail
                                    .setSingleminusscore(auditPerformanceRuleDetail.getSingleminusscore());
                            auditPerformanceRecordRuleDetail
                                    .setDescription(auditPerformanceRuleDetail.getDescription());
                            //数据来源地址
                            auditPerformanceRecordRuleDetail.setDatasource(auditPerformanceRuleDetail.getDatasource());
                            auditPerformanceRecordRuleDetail
                                    .setConditions(auditPerformanceRuleDetail.getSqlcondition());
                            auditPerformanceRecordRuleDetail.setOrdernum(auditPerformanceRuleDetail.getOrdernum());
                            auditPerformanceRecordRuleDetail
                                    .setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
                            auditPerformanceRecordRuleDetail.setDetailruleGuid(auditPerformanceRuleDetail.getRowguid());
                            CommonDao.getInstance().insert(auditPerformanceRecordRuleDetail);
                        }
                    }
                }
            }
        }
    }
}
