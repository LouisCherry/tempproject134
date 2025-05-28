package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;


/**
 * 竣工信息表list页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@RestController("auditprojectformjgxklistaction")
@Scope("request")
public class AuditProjectFormJgxkListAction  extends BaseController
{
	@Autowired
    private IAuditProjectFormJgxkService service;
    
    /**
     * 竣工信息表实体对象
     */
    private AuditProjectFormJgxk dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormJgxk> model;
  	
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
    
    public DataGridModel<AuditProjectFormJgxk> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectFormJgxk>()
            {

                @Override
                public List<AuditProjectFormJgxk> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditProjectFormJgxk> list = service.findList(
                            ListGenerator.generateSql("audit_project_form_jgxk", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("audit_project_form_jgxk", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditProjectFormJgxk getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditProjectFormJgxk();
    	}
        return dataBean;
    }

    public void setDataBean(AuditProjectFormJgxk dataBean)
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
