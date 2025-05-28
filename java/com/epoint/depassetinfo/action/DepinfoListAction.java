package com.epoint.depassetinfo.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.enterprise.inject.New;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.component.Tree;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.depassetinfo.api.IDeptaskservice;
import com.epoint.depassetinfo.domain.Deptask;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.bizlogic.service.execute.WorkflowWorkItemService9;

/**
 * 采购基本信息表list页面对应的后台
 * 
 * @author yangle
 * @version [版本号, 2018-03-27 08:13:16]
 */
@RestController("depinfolistaction")
@Scope("request")
public class DepinfoListAction extends BaseController
{
    private Deptask dataBean;
    /**
     * 项目信息树model
     */
    private TreeModel treeModel;
    /**
     * 查询用户真实姓名
     */
    private FrameUserService9 frameUserService9 = new FrameUserService9();
    /**
     * 表格控件model
     */
    private DataGridModel<Deptask> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String taskguid;

    @Autowired
    private IDeptaskservice service;
    private CommonService commservice = new CommonService();

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            commservice.deleteByGuid(sel, Deptask.class);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<Deptask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Deptask>()
            {

                @Override
                public List<Deptask> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    if (StringUtil.isBlank(taskguid)) {
                        taskguid = getViewData("id");
                    }

                    if (StringUtil.isNotBlank(taskguid)) {
                        addViewData("id", taskguid);
                        String[] args = taskguid.split("_");
                        if (args[1].equals("true")) {
                            conditionSql += " and itemid='" + args[0] + "'";
                        }
                        //system.out.println(conditionSql);

                    }

                    List<Deptask> list = commservice.findList(
                            ListGenerator.generateSql("zj_deptask", conditionSql, sortField, sortOrder), first,
                            pageSize, Deptask.class, conditionList.toArray());
                    int count = commservice.find(ListGenerator.generateSql("zj_deptask", conditionSql), Integer.class,
                            conditionList.toArray());
                    this.setRowCount(count);

                    return list;
                }

            };
        }
        return model;
    }

    public Deptask getDataBean() {
        if (dataBean == null) {
            dataBean = new Deptask();
        }
        return dataBean;
    }

    public void setDataBean(Deptask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {//第一次加载根节点
            treeModel = new TreeModel()
            {

                @Override
                public List<TreeNode> fetch(TreeNode arg0) {
                    // TODO Auto-generated method stub
                    List<TreeNode> nodes = new ArrayList<>();
                    if (arg0 == null) {
                        arg0 = new TreeNode();
                        arg0.setText("所有部门");
                        arg0.setExpanded(true);
                        arg0.setId(TreeFunction9.F9ROOT);
                        List<FrameOu> list = service.findAll();
                        for (FrameOu ou : list) {
                            TreeNode treeNode = new TreeNode();
                            treeNode.setText(ou.getOuname());
                            treeNode.setId(ou.getOuguid());
                            //区分根节点和子节点
                            treeNode.getColumns().put("isSubNode", false);
                            int count = service.findCountByPid(ou.getOuguid());
                            if (count > 0) {
                                treeNode.setLeaf(false);//说明有子节点
                            }
                            else {
                                treeNode.setLeaf(true);//说明没有子节点
                            }
                            treeNode.setPid(arg0.getId());
                            nodes.add(treeNode);
                        }
                        nodes.add(arg0);
                        arg0.getChildren().addAll(nodes);
                    }
                    else {//点击根节点
                        String guid = arg0.getId();
                        List<AuditTask> list = service.findAllByPid(guid);

                        for (AuditTask auditTask : list) {
                            TreeNode treeNode = new TreeNode();
                            treeNode.setText(auditTask.getTaskname());
                            treeNode.setId(auditTask.getItem_id());
                            treeNode.setLeaf(true);
                            treeNode.getColumns().put("isSubNode", true);
                            nodes.add(treeNode);
                        }
                        arg0.getChildren().addAll(nodes);
                    }
                    return nodes;
                }
            };
        }
        return treeModel;
    }

    public void setTreeModel(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public String getTaskguid() {
        return taskguid;
    }

    public void setTaskguid(String taskguid) {
        this.taskguid = taskguid;
    }

}
