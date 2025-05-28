package com.epoint.auditperformance.auditperformanceaccount.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 考评细则结算list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-12 09:17:06]
 */
@RestController("myauditperformanceaccountaction")
@Scope("request")
public class MyAuditPerformanceaccountaction extends BaseController
{
    private static final long serialVersionUID = -4198366793666025364L;

    @Autowired
    private IAuditPerformanceAccount service;
    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;
    @Autowired
    private IAuditPerformanceRecord recordservice;
    @Autowired
    private IAuditPerformanceRecordRuleService iAuditPerformanceRecordRuleService;
    private TreeModel treeModel;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceAccount> model;

    private String leftTreeNodeGuid;

    private String recordrulename = null;

    private String recorddetailrulename = null;
    
    private String objecttype = null;
    private String objectguid = "";
    private String windowguid;
    private String winodwlist = "";
    private List<SelectItem> windowModel = null;

    @Override
    public void pageLoad() {
        objecttype = this.getRequestParameter("objecttype");
        if (Integer.parseInt(objecttype) == 3) {
            //个人考评
            objectguid = userSession.getUserGuid();
        }
        else if (Integer.parseInt(objecttype) == 1) {
            //部门考评
            objectguid = userSession.getBaseOUGuid();
        }
        else {
            String ouguid = userSession.getOuGuid();
            List<AuditOrgaWindow> list = iAuditOrgaWindow.getWindowListByOU(ouguid).getResult();
            for (AuditOrgaWindow auditOrgaWindow : list) {
                //判断这个窗口是否参与考勤
                AuditPerformanceRecord auditPerformanceRecord = recordservice.getTopPerformanceRecordByYear(
                        ZwfwUserSession.getInstance().getCenterGuid(), objecttype, auditOrgaWindow.getRowguid());
                if (auditPerformanceRecord != null) {
                    winodwlist += "'" + auditOrgaWindow.getRowguid() + "',";
                }
            }
            if (StringUtil.isNotBlank(winodwlist)) {
                String[] objects = winodwlist.split(",");
                winodwlist = winodwlist.substring(0, winodwlist.length() - 1);
                objectguid = objects[0].replace("'", "");

            }
            if (!isPostback()) {
                windowguid = objectguid;
                addCallbackParam("windowguid", objectguid);
            }
        }

    }

    public List<SelectItem> getwindowModel() {

        if (windowModel == null) {
            windowModel = new ArrayList<>();
            String ouguid = userSession.getOuGuid();
            List<AuditOrgaWindow> list = iAuditOrgaWindow.getWindowListByOU(ouguid).getResult();
            if (list != null && list.size() > 0) {
                for (AuditOrgaWindow windowlist : list) {
                    AuditPerformanceRecord auditPerformanceRecord = recordservice.getTopPerformanceRecordByYear(
                            ZwfwUserSession.getInstance().getCenterGuid(), objecttype, windowlist.getRowguid());
                    if (auditPerformanceRecord != null) {
                        windowModel.add(new SelectItem(windowlist.getRowguid(), windowlist.getWindowname()));
                    }
                }
            }
        }
        return this.windowModel;
    }
    public TreeModel getTreeModel() {
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(objecttype)) {
            objectguid = windowguid;
        }
        
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 8948758932084086799L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    //初次加载树
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    //查询出1级菜单的绑定信息，该查询需要放在新建类中，放在此处仅提供参考     
                    Date mindata = null;
                    mindata = recordservice
                            .getMinDate(ZwfwUserSession.getInstance().getCenterGuid(), objecttype, objectguid)
                            .getResult();
                    ////system.out.println(mindata);
                    Date maxdate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy");
                    int min = Integer.parseInt(format.format(mindata));
                    int max = Integer.parseInt(format.format(maxdate));
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有年份");
                        root.setId("0");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);//展开下一层
                        list.addAll(fetch(root));//
                    }
                    else {
                        //1级子节点的加载
                        for (int i = max; i >= min; i--) {
                            TreeNode node = new TreeNode();
                            node.setId(Integer.toString(i));
                            node.setText(Integer.toString(i));
                            node.setPid("0");
                            node.setLeaf(true);
                            list.add(node);

                            //2级子节点的加载
                            //查询出2级菜单的绑定信息，该查询需要放在新建类中，放在此处仅提供参考

                            List<AuditPerformanceRecord> listRoot2 = null;
                            listRoot2 = recordservice.getPerformanceRecordByYear(Integer.toString(i),
                                    ZwfwUserSession.getInstance().getCenterGuid(), objecttype, objectguid).getResult();
                            for (int j = 0; j < listRoot2.size(); j++) {
                                String title = "【考评中】";
                                String status = listRoot2.get(j).getStatus();
                                if (Integer.parseInt(status) == 3) {
                                    title = "【已归档】";
                                }
                                Date date = listRoot2.get(j).getBegintime();
                                TreeNode node2 = new TreeNode();
                                node2.setId(listRoot2.get(j).getRowguid());
                                node2.setText(title + listRoot2.get(j).getRecordname());
                                node2.setPid(format.format(date));
                                node2.setLeaf(true);
                                list.add(node2);
                            }
                        }
                    }
                    return list;

                }
            };
        }
        return treeModel;
    }

    public DataGridModel<AuditPerformanceAccount> getDataGridData() {
        if (ZwfwConstant.CONSTANT_STR_TWO.equals(objecttype)) {
            objectguid = windowguid;
        }
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceAccount>()
            {
                @Override
                public List<AuditPerformanceAccount> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(recordrulename)) {
                        sql.like("recordrulename", recordrulename);
                    }
                    if (StringUtil.isNotBlank(recorddetailrulename)) {
                        sql.like("recorddetailrulename", recorddetailrulename);
                    }
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        sql.eq("Recordrowguid", leftTreeNodeGuid);
                    }

                    else {
                        //leftTreeNodeGuid为空表示初次加载
                        AuditPerformanceRecord auditPerformanceRecord = recordservice.getTopPerformanceRecordByYear(
                                ZwfwUserSession.getInstance().getCenterGuid(), objecttype, objectguid);
                        if (auditPerformanceRecord != null) {
                            sql.eq("Recordrowguid", auditPerformanceRecord.getRowguid());
                        }
                    }
                    sql.eq("objecttype", objecttype);
                    sql.eq("Objectguid", objectguid);
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    sql.setOrderAsc("recordrulename");
                    PageData<AuditPerformanceAccount> pagedata = service
                            .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    for (AuditPerformanceAccount auditPerformanceAccount : pagedata.getList()) {
                        String Recordrulerowguid = auditPerformanceAccount.getRecordrulerowguid();

                        AuditPerformanceRecordRule auditPerformanceRecordRule = iAuditPerformanceRecordRuleService
                                .find(Recordrulerowguid);

                        if (auditPerformanceRecordRule != null) {
                            auditPerformanceAccount.put("recordrulename",auditPerformanceAccount.getRecordrulename()+"(" +auditPerformanceRecordRule.getRulescore()+")");
                            
                        }
                        String recordRowguid = auditPerformanceAccount.getRecordrowguid();
                        AuditPerformanceRecord auditPerformanceRecord = recordservice.find(recordRowguid).getResult();
                        //添加考评记录状态和结束时间
                        auditPerformanceAccount.put("status", auditPerformanceRecord.getStatus());
                        auditPerformanceAccount.put("endtime", auditPerformanceRecord.getEndtime());
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }
            };
        }
        return model;
    }

    public String getWindowguid() {
        return windowguid;
    }

    public void setWindowguid(String windowguid) {
        this.windowguid = windowguid;
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
}
