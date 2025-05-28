package com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.action;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 施工许可单体记录表list页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 22:59:17]
 */
@RestController("auditprojectformsgxkzdantilistaction")
@Scope("request")
public class AuditProjectFormSgxkzDantiListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditProjectFormSgxkzDantiService service;
    
    /**
     * 施工许可单体记录表实体对象
     */
    private AuditProjectFormSgxkzDanti dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormSgxkzDanti> model;
  	
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
    
    public DataGridModel<AuditProjectFormSgxkzDanti> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectFormSgxkzDanti>()
            {

                @Override
                public List<AuditProjectFormSgxkzDanti> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditProjectFormSgxkzDanti> list = service.findList(
                            ListGenerator.generateSql("audit_project_form_sgxkz_danti", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("audit_project_form_sgxkz_danti", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditProjectFormSgxkzDanti getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditProjectFormSgxkzDanti();
    	}
        return dataBean;
    }

    public void setDataBean(AuditProjectFormSgxkzDanti dataBean)
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
