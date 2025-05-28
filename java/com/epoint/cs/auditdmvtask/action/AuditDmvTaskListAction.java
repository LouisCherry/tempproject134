package com.epoint.cs.auditdmvtask.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.auditdmvtask.api.entity.AuditDmvTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.auditdmvtask.api.IAuditDmvTaskService;


/**
 * 车管所事项list页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@RestController("auditdmvtasklistaction")
@Scope("request")
public class AuditDmvTaskListAction  extends BaseController
{
	@Autowired
    private IAuditDmvTaskService service;
    
    /**
     * 车管所事项实体对象
     */
    private AuditDmvTask dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditDmvTask> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    
    

    public void pageLoad()
    {
    }

	

    /**
     * 删除选定
     * 
     */
    public void deleteSelect()
    {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
             service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }
    
    public DataGridModel<AuditDmvTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditDmvTask>()
            {

                @Override
                public List<AuditDmvTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditDmvTask> list = service.findList(
                            ListGenerator.generateSql("audit_dmv_task", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditDmvTask(ListGenerator.generateSql("audit_dmv_task", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditDmvTask getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditDmvTask();
    	}
        return dataBean;
    }

    public void setDataBean(AuditDmvTask dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    


}
