package com.epoint.banjiantongjibaobiao.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.banjiantongjibaobiao.api.ITjfxTaskProjectService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 办件列表
 */
@RestController("tjfxsamenameaction")
@Scope("request")
public class TjfxSameNameAction extends BaseController {

    private Logger log = Logger.getLogger(this.getClass());
    private AuditProject baseBean;
    @Autowired
    private IAuditProject apService;

    private DataGridModel<AuditProject> model;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    @Autowired
    private IOuService ouService;
    Map<String, Integer> statusMap = new HashMap<>();// 存储状态对应的字段名称


    @Autowired
    private ITjfxTaskProjectService tjfxService;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<AuditProject> getDataGridData() {
        if (model == null) {
            model = new DataGridModel<AuditProject>() {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortFiled, String sortOrder) {
                    String nodeId = getRequestParameter("nodeId");
                    //     对应统计表不同状态数量的字段
                    String type = getRequestParameter("type");
                    String taskName = getRequestParameter("taskname");
                    String taskType = getRequestParameter("tasktype");
                    String startTime = getRequestParameter("startTime");
                    String endTime = getRequestParameter("endTime");
                    String date = getRequestParameter("date");

                    StringBuilder sql = new StringBuilder();
                    ArrayList<Object> conditionList = new ArrayList<>();
                    sql.append(" projectname =? ");
                    conditionList.add(taskName);

                    if (StringUtil.isBlank(nodeId) || (TreeFunction9.F9ROOT.equals(nodeId))) {// 根节点
                        String userOuGuid = userSession.getOuGuid();
                        List<String> allOuList = tjfxService.getAllOuList(userOuGuid, true);
                        sql.append(" and  ouguid in ( ").append(StringUtil.joinSql(allOuList)).append(") ");
                    }
                    else {// 子节点
                        sql.append(" and  ouguid='").append(nodeId).append("' ");
                    }
                    if (StringUtil.isNotBlank(taskType)) {
                        sql.append(" and tasktype=? ");
                        conditionList.add(taskType);
                    }

                    if (StringUtil.isNotBlank(date)) {
                        sql.append(" and  datediff(date_format( applydate , '%Y-%m-%d'),?)=0");
                        log.info(date);
                        conditionList.add(date);
                    }
                    else {
                        if (StringUtil.isNotBlank(startTime)) {
                            sql.append("  and  datediff(date_format( applydate , '%Y-%m-%d %H:%i:%s'),?)>=0 ");
                            conditionList.add(startTime);
                        }
                        if (StringUtil.isNotBlank(endTime)) {
                            sql.append(" and  datediff(date_format( applydate , '%Y-%m-%d %H:%i:%s'),?)<=0  ");
                            conditionList.add(endTime);
                        }
                    }
                    // 办件状态
                    sql.append(handleStatusType(type));
                    log.info(sql);
                    String fileds = "rowguid, projectname, flowsn , ouguid, ouname , applyername , applydate , receivedate , sparetime , banjiedate, applydate, status , tasktype";
                    String resultSql = "select " + fileds + " from audit_project where " + sql + " order by operatedate desc";

                    // 获取数据
                    List<AuditProject> taskList = (List<AuditProject>) tjfxService.findTaskList(resultSql, conditionList, first, pageSize);
                    String countSql = "select count(1) from audit_project where " + sql;
                    int listCount = tjfxService.getTaskListCount(countSql, conditionList);
                    this.setRowCount(listCount);
                    return taskList;
                }
            };
        }
        return model;
    }

    /**
     * 处理点击链接时要获取的状态
     *
     * @param type 点击的办件量类型
     * @return
     */
    private String handleStatusType(String type) {
        String statusSql = "";
        if (this.statusMap.size() == 0) {
            resetStatusMap();
        }
        if ("projectcount".equals(type)) {
            return "";
        }
        else if ("yjj".equals(type)) {
            statusSql = " and  status>=26 ";
        }
        else if ("ysl".equals(type)) {
            statusSql = " and  status>=30 ";
        }
        else if (statusMap.get(type) != null) {
            statusSql = " and  status=" + statusMap.get(type) + " ";
        }
        return statusSql;
    }

    /**
     * 导出数据
     *
     * @return
     */
    public ExportModel getExportModel() {
        if (exportModel == null) {
            String columnKey = "projectname,flowsn,ouname,applyername,applydate,receivedate,sparetime,banjiedate,status";
            String columnTitile = "事项名称,办件编号,部门,申请人,申请时间,受理时间,办理剩余时间,办结时间,办件状态";
            exportModel = new ExportModel(columnKey, columnTitile);
        }
        return exportModel;
    }

    /**
     * 存储指定状态对应的字段名称
     */
    public void resetStatusMap() {
        this.statusMap.put("wwsbytj", 12);
        this.statusMap.put("wwsbysth", 14);
        this.statusMap.put("djj", 24);
        this.statusMap.put("yjj", 26);
        this.statusMap.put("dbb", 28);
        this.statusMap.put("ysl", 30);
        this.statusMap.put("spbtg", 40);
        this.statusMap.put("sptg", 50);
        this.statusMap.put("zcbj", 90);
        this.statusMap.put("bysl", 97);
        this.statusMap.put("cxsq", 98);
        this.statusMap.put("yczz", 99);
        this.statusMap.put("ysldbb", 37);
        this.statusMap.put("spz", 80);
        this.statusMap.put("ztz", 999);
    }


    /* getter  and setter */
    public AuditProject getBaseBean() {
        return baseBean;
    }

    public void setBaseBean(AuditProject baseBean) {
        this.baseBean = baseBean;
    }

    public Map<String, Integer> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, Integer> statusMap) {
        this.statusMap = statusMap;
    }
}
