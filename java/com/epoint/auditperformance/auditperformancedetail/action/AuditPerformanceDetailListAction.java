package com.epoint.auditperformance.auditperformancedetail.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancedetail.inter.IAuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评明细list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-09 10:31:38]
 */
@RestController("auditperformancedetaillistaction")
@Scope("request")
public class AuditPerformanceDetailListAction extends BaseController
{
    private static final long serialVersionUID = -4577900728771402039L;

    @Autowired
    private IAuditPerformanceDetail service;

    @Autowired
    private IAuditPerformanceRecord recordservice;

    @Autowired
    private IAuditPerformanceRecordRuleService ruleservice;
    /**
     * 考评明细实体对象
     */
    private AuditPerformanceDetail dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceDetail> model;

    private TreeModel treeModel = null;

    private String recordrulename;

    private String recorddetailrulename;
    private String objectname;
    private String gradetimestart;
    private String gradetimeend;
    private String leftTreeNodeGuid;
    private String usable;
    private List<SelectItem> usablemodel;

    /**
     * 考评规则model
     */
    private List<SelectItem> rulemodel = null;

    @Override
    public void pageLoad() {
        usable = ZwfwConstant.CONSTANT_STR_ONE;
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect(String selectobj) {
        String[] arr = selectobj.split(";");
        List<String> select= Arrays.asList(arr);
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditPerformanceDetail> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceDetail>()
            {

                @Override
                public List<AuditPerformanceDetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    List<TreeNode> list =  getRecordTreeModel().getWrappedData();
                    String recorddrowguids = "'";
                    for (TreeNode treeNode : list) {
                        if(StringUtil.isNotBlank(treeNode.getId())){
                            recorddrowguids+=treeNode.getId()+"','";
                        }
                    }
                    if(recorddrowguids.length()>1){
                        recorddrowguids = recorddrowguids.substring(0, recorddrowguids.length()-3);                        
                    }
                    recorddrowguids+="'";
                    sql.in("recordrowguid", recorddrowguids);
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("recordrowguid", leftTreeNodeGuid);
                    }
                    if (StringUtil.isNotBlank(recordrulename)) {
                        sql.like("recordrulename", recordrulename);
                    }
                    if (StringUtil.isNotBlank(recorddetailrulename)) {
                        sql.like("recorddetailrulename", recorddetailrulename);
                    }
                    if (StringUtil.isNotBlank(objectname)) {
                        sql.like("objectname", objectname);
                    }
                    if (StringUtil.isNotBlank(gradetimestart)) {
                        sql.ge("gradetime", EpointDateUtil.getBeginOfDateStr(gradetimestart));
                    }
                    if (StringUtil.isNotBlank(gradetimeend)) {
                        sql.le("gradetime", EpointDateUtil.getEndOfDateStr(gradetimeend));
                    }
                    if(StringUtil.isNotBlank(usable)){
                        sql.eq("usable", usable);
                    }
                    sql.setOrderDesc("gradetime");
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    PageData<AuditPerformanceDetail> pagedata = service
                            .getDetailPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }

    public TreeModel getRecordTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有考评");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);
                        list.addAll(fetch(root));
                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        List<Record> listrecord = recordservice
                                .findListForTree(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                        TreeNode node;
                        if (listrecord != null && listrecord.size() > 0) {
                            for (Record record : listrecord) {
                                node = new TreeNode();
                                node.setId(record.getStr("rowguid"));
                                node.setText(record.getStr("recordname"));
                                node.setPid(objectGuid);
                                node.setLeaf(true);
                                list.add(node);
                            }
                        }
                    }
                    return list;
                }
            };
        }
        return treeModel;
    }

    public List<SelectItem> getRuleModel() {
        if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
            if (rulemodel == null) {
                rulemodel = new ArrayList<SelectItem>();
                SelectItem item;
                List<AuditPerformanceRecordRule> list = ruleservice
                        .findFiledByRecordRowguid("rulename,rowguid", leftTreeNodeGuid).getResult();
                if (list != null && list.size() > 0) {
                    for (AuditPerformanceRecordRule auditPerformanceRecordRule : list) {
                        item = new SelectItem();
                        item.setText(auditPerformanceRecordRule.getRulename());
                        item.setValue(auditPerformanceRecordRule.getRowguid());
                        rulemodel.add(item);
                    }
                }
            }
        }
        return rulemodel;
    }
    
    public List<SelectItem> getusablemodel(){
        if(usablemodel==null){
            usablemodel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("单选按钮组", "是否有效", null, false));
        }
        return usablemodel;
    }

    public AuditPerformanceDetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceDetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceDetail dataBean) {
        this.dataBean = dataBean;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

    public String getGradetimestart() {
        return gradetimestart;
    }

    public void setGradetimestart(String gradetimestart) {
        this.gradetimestart = gradetimestart;
    }

    public String getGradetimeend() {
        return gradetimeend;
    }

    public void setGradetimeend(String gradetimeend) {
        this.gradetimeend = gradetimeend;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getRecordrulename() {
        return recordrulename;
    }

    public void setRecordrulename(String recordrulename) {
        this.recordrulename = recordrulename;
    }

    public String getRecorddetailrulename() {
        return recorddetailrulename;
    }

    public void setRecorddetailrulename(String recorddetailrulename) {
        this.recorddetailrulename = recorddetailrulename;
    }
    

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

}
