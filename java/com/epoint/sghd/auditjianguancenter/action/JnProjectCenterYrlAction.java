package com.epoint.sghd.auditjianguancenter.action;

import java.util.ArrayList;
import java.util.Date;
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
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnRenlingService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJnRenling;

/**
 * 中心已认领监管list
 *
 * @author swy
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectcenteryrlaction")
@Scope("request")
public class JnProjectCenterYrlAction extends BaseController
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

    @Autowired
    private IAuditTaskExtension audittaskExtensionServiec;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    @Autowired
    private IAuditTaskJnRenlingService auditTaskJnRenlingService;

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

                    String sqlFind = "select p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid,zi.jg_ouguid,zi.jg_ouname from audit_project p INNER JOIN (select a.task_id,b.projectguid,a.jg_ouguid,a.jg_ouname from audit_task_jn a join audit_task_jn_renling b on a.task_id = b.task_id and a.jg_ouguid = b.renling_ouguid and is_hz = 1 GROUP BY a.task_id,a.jg_ouguid,b.projectguid ) zi ON p.TASK_ID = zi.TASK_ID and p.rowguid=zi.projectguid where  ";
                    String sqlNum = "select count(1) from audit_project p INNER JOIN (select a.task_id,b.projectguid,a.jg_ouguid,a.jg_ouname from audit_task_jn a join audit_task_jn_renling b on a.task_id = b.task_id and a.jg_ouguid = b.renling_ouguid and is_hz = 1 GROUP BY a.task_id,a.jg_ouguid,b.projectguid ) zi ON p.TASK_ID = zi.TASK_ID and p.rowguid=zi.projectguid where ";

                    String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
                    sqlFind = sqlFind + "  p.handleareacode like '" + handleareacode + "%' ";
                    sqlNum = sqlNum + " p.handleareacode like '" + handleareacode + "%' ";

                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    }
                    else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sqlFind = sqlFind + " and p.areaCode = " + area + " ";
                    sqlNum = sqlNum + " and p.areaCode = " + area + " ";

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
                    if (StringUtil.isNotBlank(dataBean.getStr("renling_username"))) {
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                                + dataBean.getStr("renling_username") + "%')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                                + dataBean.getStr("renling_username") + "%')";
                    }
                    if (StringUtil.isNotBlank(dataBean.get("renlingdateStart"))) {
                        Date renlingdateStart = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateStart"));
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                                + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                                + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
                    }
                    if (StringUtil.isNotBlank(dataBean.get("renlingdateEnd"))) {
                        Date renlingdateEnd = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateEnd"));
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                                + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE a.task_id = r.task_id AND p.RowGuid = r.projectguid AND a.jg_ouguid = r.renling_ouguid AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                                + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
                    }

                    sqlFind = sqlFind + " and p.status = 90 ";
                    sqlNum = sqlNum + " and p.status = 90 ";

                    // 系统参数，审管互动认领时间起始
                    String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
                    if (StringUtil.isNotBlank(sghdstarttime)) {
                        sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
                        sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
                    }

                    sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

                    List<AuditProject> list = new ArrayList<AuditProject>();
                    list = jnAuditJianGuanCenter.getProejctCenter(sqlFind, first, pageSize);

                    int count = 0;
                    if (list != null) {
                        String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
                        for (AuditProject auditProject : list) {
                            setAudittask(audittaskServiec.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult());
                            AuditTaskExtension auditTaskExtension = audittaskExtensionServiec.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                            if (auditTaskExtension != null) {
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                                    auditProject.put("projectname", "【自建系统】" + auditProject.getProjectname());
                                }
                            }

                            // 新增字段“部门名称”的显示
                            String taskguid = auditProject.getTaskguid();
                            auditProject.put("ouname", jnAuditJianGuanCenter.getOuNameFromAuditTask(taskguid));

                            AuditTaskJnRenling auditTaskJnRenling = auditTaskJnRenlingService.find(renlingSql, new Object[] {auditProject.getTask_id(), auditProject.getRowguid(), auditProject.getStr("jg_ouguid") });
                            if (auditTaskJnRenling != null) {
                                auditProject.put("renlingtype", "已认领");
                                auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                                auditProject.set("renlingdate", EpointDateUtil.convertDate2String(auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                                auditProject.put("renlingtime", auditTaskJnRenling.getRenlingdate());
                            }
                            else {
                                auditProject.put("renlingtype", "未认领");
                            }
                        }
                        count = jnAuditJianGuanCenter.getProejctCenterNum(sqlNum);
                    }
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

                        List<Record> listOu = new ArrayList<Record>();
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
            exportModel = new ExportModel(
                    "flowsn,projectname,ouname,applyername,banjiedate,jg_ouname,renlingdate,renling_username",
                    "办件编号,事项名称,部门,申请人,办结时间,监管部门,认领时间,认领人");
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

}
