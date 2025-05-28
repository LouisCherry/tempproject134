package com.epoint.banjiantongjibaobiao.action;


import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.banjiantongjibaobiao.api.ITjfxTaskProjectService;
import com.epoint.banjiantongjibaobiao.api.TjfxTaskProject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 事项统计表action
 *
 * @author Epoint
 */
@RestController("tjfxtaskprojectlistaction")
@Scope("request")
public class TjfxTaskProjectListAction extends BaseController {

    private Logger log = Logger.getLogger(TjfxTaskProjectListAction.class);

    /**
     * 事项办件统计表实体对象
     */
    private TjfxTaskProject dataBean;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 搜索栏中的结束日期
     */
    private Date startTime;
    /**
     * 搜索栏中的开始日期
     */
    private Date endTime;
    /**
     * 搜索中的事项类型
     */
    private String taskType;
    /**
     * 搜索栏事项类型下拉选框
     */
    private List<SelectItem> taskTypeModel;

    private List<SelectItem> taskStatusModel;

    private String nodeInfo;

    /**
     * 前端隐藏域中存储的部门节点信息
     */
    private String ouNodeInfo;

    private String statusBox;


    /**
     * 左侧部门树结构
     */
    private TreeModel ouTreeModel;
    /**
     * 表格控件model
     */
    private DataGridModel<TjfxTaskProject> model;
    @Autowired
    private ITjfxTaskProjectService service;

    private String fileds = " rowguid,taskname,taskguid,projectcount,tasktype,tasktype as ctasktype,ysl,zcbj,bysl,drbjl,date ";
    @Autowired
    private IUserService userService;


    @Autowired
    private IOuService ouService;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<TjfxTaskProject> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<TjfxTaskProject>() {
                @Override
                public List<TjfxTaskProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<>();
                    List<Object> conditionParams = new ArrayList<>();
                    String allOuGuids = "";
                    // 判断开始日期和结束日期
                    if (startTime != null) {
                        conditionList.add(" `date`>=? ");
                        conditionParams.add(EpointDateUtil.convertDate2String(startTime));
                    }
                    if (endTime != null) {
                        conditionList.add(" `date`<=? ");
                        conditionParams.add(EpointDateUtil.convertDate2String(endTime));
                    }
                    if (StringUtil.isNotBlank(taskType)) {
                        conditionList.add(" tasktype = ? ");
                        conditionParams.add(taskType);
                    }
                    if (StringUtil.isNotBlank(statusBox)) {
                        fileds += "," + statusBox;
                    }
                    log.info("----------startTime:" + startTime + "---------- endTime:" + endTime + "---------- taskType:" + taskType);
                    // 处理部门节点
                    if (StringUtil.isNotBlank(nodeInfo)) {
                        String[] split = nodeInfo.split(";");
                        String nodeId = split[0];
                        String nodeType = split[1];
                        if ("rootNode".equals(nodeType)) {
                            //  根节点,需要查询该节点部门及其下属部门的数据
                            String ouGuid = userSession.getOuGuid();
                            // 查询所有的部门
                            List<String> allOuList = service.getAllOuList(ouGuid, true);
                            allOuGuids = "  ouguid in ( " + StringUtil.joinSql(allOuList) + ") ";
                        }
                        if ("subNode".equals(nodeType)) {
                            allOuGuids = " ouguid ='" + nodeId + "' ";
                        }
                    }
                    else {
                        // 第一次加载不包含条件
                        //  根节点,需要查询该节点部门及其下属部门的数据
                        String ouGuid = userSession.getOuGuid();
                        // 查询所有的部门
                        List<String> allOuList = service.getAllOuList(ouGuid, true);
                        allOuGuids = "  ouguid in ( " + StringUtil.joinSql(allOuList) + ") ";
                    }
                    String sql = StringUtil.joinSql(conditionList);
                    sql = sql.replaceAll(",", " and ").replaceAll("'", "");
                    if (!conditionParams.isEmpty()) {
                        sql = " and " + sql;
                    }
                    String countSql = "select count(1) from  tjfx_task_project where " + allOuGuids + sql;
                    int listCount = service.getTaskListCount(countSql, conditionParams);
                    this.setRowCount(listCount);
                    sql = "select " + fileds + " from tjfx_task_project where " + allOuGuids + sql + " order by date desc ";
                    return (List<TjfxTaskProject>) service.findTaskList(sql, conditionParams, first, pageSize);
                }
            };
        }
        return model;
    }

    public TreeModel getOuTreeModel() {
        if (ouTreeModel == null) {
            ouTreeModel = new TreeModel() {
                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    List<TreeNode> nodes = new ArrayList<>();
                    if (treeNode == null) {
                        // 初次加载部门树结构
                        TreeNode rootNode = new TreeNode();
                        // 获取请求用户所在部门
                        String ouGuid = userSession.getOuGuid();
                        FrameOu rootOu = ouService.getOuByOuGuid(ouGuid);
                        rootNode.setId(TreeFunction9.F9ROOT);
                        rootNode.setPid("-1");
                        rootNode.setText(rootOu.getOuname());
                        rootNode.setExpanded(true);
                        // 自定义属性，用于判断节点类型
                        rootNode.getColumns().put("nodeType", "rootNode");
                        nodes.add(rootNode);
                        // 获取当前用户所在部门的下级部门信息列表
                        // 判断部门列表是否为空，不为空则将信息添加到部门树中
                        // 参数5：获取下一层部门，且不包含自己
                        List<FrameOu> frameOuList = ouService.listOUByGuid(ouGuid, 5);
                        if (!frameOuList.isEmpty()) {
                            for (FrameOu ou : frameOuList) {
                                TreeNode node = new TreeNode();
                                node.setId(ou.getOuguid());
                                node.setText(ou.getOuname());
                                node.setPid(TreeFunction9.F9ROOT);
                                node.getColumns().put("nodeType", "subNode");
                                node.setLeaf(true);
                                nodes.add(node);
                            }
                        }
                    }
                    return nodes;
                }
            };
        }
        return ouTreeModel;
    }

    public List<SelectItem> getTaskTypeModel() {
        if (taskTypeModel == null) {
            taskTypeModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "事项类型", null, true));
        }
        return this.taskTypeModel;
    }

    /**
     * 办件状态下拉列表
     *
     * @return
     */
    public List<SelectItem> getTaskStatusModel() {
        if (taskStatusModel == null) {
            List<SelectItem> result = new ArrayList<>();
            result.add(new SelectItem("wwsbytj", "外网申报已提交量"));
            result.add(new SelectItem("wwsbysth", "外网申报预审退回量"));
            result.add(new SelectItem("djj", "待接件量"));
            result.add(new SelectItem("yjj", "已接件量"));
            result.add(new SelectItem("dbb", "待补办量"));
            result.add(new SelectItem("spbtg", "审批不通过量"));
            result.add(new SelectItem("sptg", "审批通过量"));
            result.add(new SelectItem("cxsq", "撤销申请量"));
            result.add(new SelectItem("yczz", "异常中止量"));
            result.add(new SelectItem("ysldbb", "已受理待补办量"));
            result.add(new SelectItem("spz", "审批中量"));
            result.add(new SelectItem("ztz", "暂停中量"));
            this.taskStatusModel = result;
        }
        return taskStatusModel;
    }

    /**
     * 导出数据方法
     *
     * @return
     */
    public ExportModel getExportModel() {
        String columnKey = "taskname,tasktype,projectcount,ysl,zcbj,bysl,drbjl";
        StringBuilder columnTitle = new StringBuilder("事项名称,事项类型,办件量,受理量,正常办结量,不予受理量,导入办件量");
        HashMap<String, String> hashMap = new HashMap<>();
        getTaskStatusModel();
        for (SelectItem sel : taskStatusModel) {
            hashMap.put((String) sel.getValue(), sel.getText());
        }
        if (StringUtil.isNotBlank(statusBox)) {
            columnKey += "," + statusBox;
            for (String key : statusBox.split(",")) {
                columnTitle.append(",").append(hashMap.get(key));
            }
        }
        if (exportModel == null) {
            exportModel = new ExportModel(columnKey, columnTitle.toString());
        }
        return exportModel;
    }

    /* getter and setter */
    public TjfxTaskProject getDataBean() {
        if (dataBean == null) {
            dataBean = new TjfxTaskProject();
        }
        return dataBean;
    }

    public void setDataBean(TjfxTaskProject dataBean) {
        this.dataBean = dataBean;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOuNodeInfo() {
        return ouNodeInfo;
    }

    public void setOuNodeInfo(String ouNodeInfo) {
        this.ouNodeInfo = ouNodeInfo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(String nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public String getStatusBox() {
        return statusBox;
    }

    public void setStatusBox(String statusBox) {
        this.statusBox = statusBox;
    }

    public String getFileds() {
        return fileds;
    }

    public void setFileds(String fileds) {
        this.fileds = fileds;
    }

}
