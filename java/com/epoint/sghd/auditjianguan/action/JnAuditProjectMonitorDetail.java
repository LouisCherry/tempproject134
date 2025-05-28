package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitor.inter.IAuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitorreply.domain.AuditProjectMonitorReply;
import com.epoint.basic.auditproject.auditprojectmonitorreply.inter.IAuditProjectMonitorReply;
import com.epoint.basic.auditproject.auditprojectmonitorresult.domain.AuditProjectMonitorResult;
import com.epoint.basic.auditproject.auditprojectmonitorresult.inter.IAuditProjectMonitorResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 督查督办详细页面
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnauditprojectmonitordetail")
@Scope("request")
public class JnAuditProjectMonitorDetail extends BaseController {
    private static final long serialVersionUID = 181286499037869484L;

    private String monitorGuid = "";

    private String monitorOpinion = "";
    private String banjieOpinion = "";

    private AuditProjectMonitor dataBean = null;

    private AuditProjectMonitorResult result = null;

    private String response = "未认定";

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectMonitorReply> model;

    @Autowired
    private IAuditProjectMonitor superviseService;

    @Autowired
    private IAuditProjectMonitorReply monitorReplyService;

    @Autowired
    private IAuditProjectMonitorResult monitorResultService;

    @Override
    public void pageLoad() {
        monitorGuid = getRequestParameter("monitorGuid");
        dataBean = superviseService.getMonitorSuperviseByGuid(monitorGuid).getResult();
        result = monitorResultService.getMonitorResultByMonitorNO(dataBean.getNo()).getResult();

        if (result != null) {
            if ("2".equals(result.getConfirm())) {
                response = "主观过失";
            } else if ("1".equals(result.getConfirm())) {
                response = "客观因素";
            }
            addCallbackParam("msg", "true");
        }
    }

    public DataGridModel<AuditProjectMonitorReply> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectMonitorReply>() {

                @Override
                public List<AuditProjectMonitorReply> fetchData(int first, int pageSize, String sortField,
                                                                String sortOrder) {
                    List<AuditProjectMonitorReply> monitorReplies = monitorReplyService
                            .selectMonitorReplyByMonitorNO(dataBean.getNo()).getResult();
                    this.setRowCount(monitorReplies != null ? monitorReplies.size() : 0);
                    return monitorReplies;
                }
            };
        }
        return model;
    }

    public AuditProjectMonitor getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectMonitor dataBean) {
        this.dataBean = dataBean;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AuditProjectMonitorResult getResult() {
        return result;
    }

    public void setResult(AuditProjectMonitorResult result) {
        this.result = result;
    }

    public String getMonitorOpinion() {
        return monitorOpinion;
    }

    public void setMonitorOpinion(String monitorOpinion) {
        this.monitorOpinion = monitorOpinion;
    }

    public String getBanjieOpinion() {
        return banjieOpinion;
    }

    public void setBanjieOpinion(String banjieOpinion) {
        this.banjieOpinion = banjieOpinion;
    }
}
