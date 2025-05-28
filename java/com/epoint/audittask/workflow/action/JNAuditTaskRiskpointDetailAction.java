package com.epoint.audittask.workflow.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 事项岗位风险点信息表detail页面对应的后台
 */
@RestController("jnaudittaskriskpointdetailaction")
@Scope("request")
public class JNAuditTaskRiskpointDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -3654364071195527677L;
    /**
     * 事项岗位风险点信息表实体对象
     */
    private AuditTaskRiskpoint dataBean;

    /**
     * 事项
     */
    private AuditTask dataBeanTask;
    /**
     * 事项标识
     */
    private String taskGuid;
    /**
     * 流程版本guid
     */
    private String processversionguid = "";

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskRiskpoint> model;
    /**
     * 当前版本taskguid
     */
    private String currentTaskGuid;
    
    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointimpl;
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = this.getRequestParameter("taskGuid");
        currentTaskGuid = this.getViewData("currentTaskGuid");
        if (StringUtil.isBlank(currentTaskGuid)) {
            currentTaskGuid = this.getRequestParameter("currentGuid");
        }
        dataBeanTask = auditTaskBasicImpl.getAuditTaskByGuid(currentTaskGuid, false).getResult();
        if (dataBeanTask != null) {
            //获取流程版本guid
            processversionguid = dataBeanTask.getPvguid();
            this.addCallbackParam("processversionguid", processversionguid);
        }
        else {
            this.addCallbackParam("processversionguid", "");
        }

    }

    /**
     * 
     * 初始化表格数据
     * 
     * @return
     */
    public DataGridModel<AuditTaskRiskpoint> getDataGridData1() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskRiskpoint>()
            {

                @Override
                public List<AuditTaskRiskpoint> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(currentTaskGuid)) {
                        sql.eq("TaskGuid", currentTaskGuid);
                    }
                    else {
                        sql.eq("TaskGuid", taskGuid);
                    }
                    sql.setOrderDesc("ordernum");
                    sql.setOrderAsc("operatedate");
                    PageData<AuditTaskRiskpoint> pageData = auditTaskRiskpointimpl
                            .getAuditTaskRiskpointPageData(sql.getMap(), first, 0,
                            sortField, sortOrder).getResult();
                    for (AuditTaskRiskpoint auditTaskRiskpoint : pageData.getList()) {
                        if (auditTaskRiskpoint.getIsriskpoint() != null) {
                            if (ZwfwConstant.CONSTANT_INT_ONE == auditTaskRiskpoint.getIsriskpoint()) {
                                auditTaskRiskpoint.put("isRiskPoint", "true");
                            }
                            else if (ZwfwConstant.CONSTANT_INT_ZERO == auditTaskRiskpoint.getIsriskpoint()) {
                                auditTaskRiskpoint.put("isRiskPoint", "false");
                            }
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };

        }
        return model;
    }

    /**
     * 
     *  把当前的guid和比较的guid存入view
     *  @param currentTaskGuid
     *  @param compareTaskGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addTaskGuidToView(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
    }

    public AuditTaskRiskpoint getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskRiskpoint();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskRiskpoint dataBean) {
        this.dataBean = dataBean;
    }

}
