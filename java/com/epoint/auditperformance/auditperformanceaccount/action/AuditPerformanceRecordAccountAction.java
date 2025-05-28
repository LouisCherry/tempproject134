package com.epoint.auditperformance.auditperformanceaccount.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
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
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评细则结算list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-12 09:17:06]
 */
@RestController("auditperformancerecordaccountaction")
@Scope("request")
public class AuditPerformanceRecordAccountAction extends BaseController
{
    private static final long serialVersionUID = -4198366793666025364L;

    @Autowired
    private IAuditPerformanceAccount service;

    @Autowired
    private IAuditPerformanceRecord recordservice;

    /**
     * 考评细则结算实体对象
     */
    private AuditPerformanceAccount dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceRecord> model;
    private String leftTreeNodeGuid;
    private TreeModel treeModel;
    private String recordname;
    private String status;
    private String objecttype;
    private List<SelectItem> statusmodel;
    private List<SelectItem> objectmodel;
    
    @Override
    public void pageLoad() {

    }

    public DataGridModel<AuditPerformanceRecord> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceRecord>()
            {

                @Override
                public List<AuditPerformanceRecord> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.gt("Begintime", EpointDateUtil.getBeginOfDateStr(leftTreeNodeGuid+"-01-01 00:00:00"));
                        sql.lt("Begintime", EpointDateUtil.getBeginOfDateStr(String.valueOf(Integer.parseInt(leftTreeNodeGuid) + 1)+"-01-01 00:00:00"));
                    }
                    if (StringUtil.isNotBlank(recordname)) {
                        sql.like("recordname", recordname);
                    }
                    if (StringUtil.isNotBlank(status)) {
                        sql.eq("status", status);
                    }
                    if (StringUtil.isNotBlank(objecttype)) {
                        sql.eq("objecttype", objecttype);
                    }
                    sql.setOrderAsc("status");
                    sql.setOrderDesc("begintime");
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    sql.eq("ifenabled",ZwfwConstant.CONSTANT_STR_ONE);
                    sql.in("status", "2,3");
                    PageData<AuditPerformanceRecord> pagedata = recordservice
                            .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
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
                private static final long serialVersionUID = 8323462298954520965L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("考评开始时间");
                        root.setId("");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);
                        list.addAll(fetch(root));

                    }
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        TreeNode node = new TreeNode();
                        for (String i : getTimeList()) {
                            node = new TreeNode();
                            node.setId(i);
                            node.setText(i);
                            node.setPid(objectGuid);
                            node.setLeaf(true);
                            list.add(node);
                        }
                        
                    }
                    return list;
                }
            };
        }
        return treeModel;
    }

    public List<String> getTimeList() {
        Date date = recordservice.findMinBegintime(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
        Calendar calendar = Calendar.getInstance();
        int current = calendar.get(Calendar.YEAR);
        if(date==null){
            date= new Date();
        }
        calendar.setTime(date);
        int minyear = calendar.get(Calendar.YEAR);
        List<String> list = new ArrayList<String>();
        for (int i = minyear; i <= current; i++) {
            list.add(String.valueOf(i));
        }
        Collections.reverse(list);
        return list;
    }
    public List<SelectItem> getStatusModel() {
        if (statusmodel == null) {
            statusmodel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "考评记录状态", null, false));
            statusmodel.remove(0);
            SelectItem item  = new SelectItem();
            item.setText("请选择...");
            statusmodel.add(0, item);
        }
        return this.statusmodel;
    }
    public List<SelectItem> getObjecttypeModel() {
        if (objectmodel == null) {
            objectmodel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "考评对象类别", null, false));
            SelectItem item  = new SelectItem();
            item.setText("请选择...");
            objectmodel.add(0, item);
        }
        return this.objectmodel;
    }

    public void doAcount() {
        service.doAccount(leftTreeNodeGuid);
    }

    public AuditPerformanceAccount getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceAccount();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceAccount dataBean) {
        this.dataBean = dataBean;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public String getRecordname() {
        return recordname;
    }

    public void setRecordname(String recordname) {
        this.recordname = recordname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjecttype() {
        return objecttype;
    }

    public void setObjecttype(String objecttype) {
        this.objecttype = objecttype;
    }
}
