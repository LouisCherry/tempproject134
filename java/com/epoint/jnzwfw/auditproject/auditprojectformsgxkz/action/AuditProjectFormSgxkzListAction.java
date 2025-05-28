package com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.action;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 施工许可证表单list页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@RestController("auditprojectformsgxkzlistaction")
@Scope("request")
public class AuditProjectFormSgxkzListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditProjectFormSgxkzService service;
    
    /**
     * 施工许可证表单实体对象
     */
    private AuditProjectFormSgxkz dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormSgxkz> model;
  	
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
    
    public DataGridModel<AuditProjectFormSgxkz> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectFormSgxkz>()
            {

                @Override
                public List<AuditProjectFormSgxkz> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditProjectFormSgxkz> list = service.findList(
                            ListGenerator.generateSql("audit_project_form_sgxkz", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("audit_project_form_sgxkz", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditProjectFormSgxkz getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditProjectFormSgxkz();
    	}
        return dataBean;
    }

    public void setDataBean(AuditProjectFormSgxkz dataBean)
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
