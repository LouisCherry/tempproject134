package com.epoint.sghd.auditjianguancenter.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;

/**
 * 中心未认领监管
 *
 * @author swy
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectcenterwrlaction")
@Scope("request")
public class JnProjectCenterWrlAction extends BaseController
{
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
     * 树控件model
     */
    private TreeModel treeModel;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask audittaskServiec;

    /**
     * 左边部门树列表
     */
    private String leftTreeNodeGuid;
    private String treeNode;

    String ouGuid = "";
    String areaCode = "";

    private int yrlNum;
    private int wrlNum;

    @Autowired
    private IAuditTaskExtension audittaskExtensionServiec;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    private ExportModel exportModel;

    @Override
    public void pageLoad() {
        areaCode = ZwfwUserSession.getInstance().getAreaCode();
        ouGuid = this.getRequestParameter("ouguid");
        treeNode = this.getRequestParameter("leftTreeNodeGuid");
        if (!"undefined".equals(ouGuid)) {
            leftTreeNodeGuid = ouGuid;
        }

    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = " applyDate";
                    sortOrder = "desc";
                    String sqlFind = "select p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid,a.jg_ouguid,a.jg_ouname from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID left join audit_task_jn_renling r on a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' where  ";
                    String sqlNum = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID left join audit_task_jn_renling r on a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' where  ";

                    String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
                    sqlFind = sqlFind + " p.handleareacode like '" + handleareacode + "%' ";
                    sqlNum = sqlNum + " p.handleareacode like '" + handleareacode + "%' ";

                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel() != null
                            && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    }
                    else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sqlFind = sqlFind + " and p.areaCode = " + area + " ";
                    sqlNum = sqlNum + " and p.areaCode = " + area + " ";
                    sqlFind = sqlFind + " and p.Banjieresult = '40' ";
                    sqlNum = sqlNum + " and p.Banjieresult =  '40' ";
                    if (StringUtil.isNotBlank(leftTreeNodeGuid)) {
                        // 慢查询调整：将条件放在join中
                        sqlFind = sqlFind.replace("is_hz = 1", "is_hz = 1 and jg_ouguid = '" + leftTreeNodeGuid + "' ");
                        sqlNum = sqlNum.replace("is_hz = 1", "is_hz = 1 and jg_ouguid = '" + leftTreeNodeGuid + "' ");
                    }

                    if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                        sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                        sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                    }
                    if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                        sqlFind = sqlFind + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                        sqlNum = sqlNum + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                    }
                    if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
                        sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                        sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                    }
                    if (StringUtil.isNotBlank(dataBean.getStr("jg_ouname"))) {
                        sqlFind = sqlFind + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
                        sqlNum = sqlNum + " and a.jg_ouname like '%" + dataBean.getStr("jg_ouname") + "%' ";
                    }

                    sqlFind = sqlFind + " and p.status = 90 ";
                    sqlNum = sqlNum + " and p.status = 90 ";
                    
                    // 未认领
                    sqlFind = sqlFind + " and r.RowGuid is null ";
                    sqlNum = sqlNum + " and r.RowGuid is null ";

                    // 系统参数，审管互动认领时间起始
                    String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
                    if (StringUtil.isNotBlank(sghdstarttime)) {
                        sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
                        sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
                    }

                    sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

                    List<AuditProject> list = new ArrayList<>();
                    list = jnAuditJianGuanCenter.getProejctCenter(sqlFind, first, pageSize);

                    int count = jnAuditJianGuanCenter.getProejctCenterNum(sqlNum);
//                    if (list != null) {
//                        for (AuditProject auditProject : list) {
//                            setAudittask(
//                                    audittaskServiec.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult());
//                            AuditTaskExtension auditTaskExtension = audittaskExtensionServiec
//                                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
//                            if (auditTaskExtension != null) {
//                                if (ZwfwConstant.CONSTANT_STR_ONE
//                                        .equals(auditTaskExtension.getIszijianxitong().toString())) {
//                                    auditProject.put("projectname", "【自建系统】" + auditProject.getProjectname());
//                                }
//                            }
//                            // 新增字段“部门名称”的显示
//                            String taskguid = auditProject.getTaskguid();
//                            auditProject.put("ouname", jnAuditJianGuanCenter.getOuNameFromAuditTask(taskguid));
//
//                        }
//                        count = jnAuditJianGuanCenter.getProejctCenterNum(sqlNum);
//                    }
                    this.setRowCount(count);
                    return list;
                }
            };
        }

        return model;
    }

    public void hasProGuid(String rowguid) {
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public TreeModel getOUTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = 1L;

                /**
                 * 树加载事件
                 *
                 * @param treeNode
                 *            当前展开的节点
                 * @return List<TreeNode>
                 */
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();

                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    else {
                        List<Record> listOu = new ArrayList<>();
                        listOu = jnAuditJianGuanCenter.listOuByAreacode(areaCode);

                        for (int i = 0; i < listOu.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(listOu.get(i).getStr("OUGUID"));
                            node.setText(listOu.get(i).getStr("OUSHORTNAME"));
                            node.setPid("f9root");
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("flowsn,projectname,ouname,applyername,banjiedate,jg_ouname",
                    "办件编号,事项名称,部门,申请人,办结时间,监管部门");
        }
        return exportModel;
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

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public String getLeftTreeNodeGuid() {
        return leftTreeNodeGuid;
    }

    public void setLeftTreeNodeGuid(String leftTreeNodeGuid) {
        this.leftTreeNodeGuid = leftTreeNodeGuid;
    }

    public int getYrlNum() {
        return yrlNum;
    }

    public void setYrlNum(int yrlNum) {
        this.yrlNum = yrlNum;
    }

    public int getWrlNum() {
        return wrlNum;
    }

    public void setWrlNum(int wrlNum) {
        this.wrlNum = wrlNum;
    }

}
