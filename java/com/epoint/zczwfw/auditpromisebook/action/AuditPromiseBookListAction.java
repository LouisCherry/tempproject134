package com.epoint.zczwfw.auditpromisebook.action;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.zczwfw.auditpromisebook.api.IAuditPromiseBookService;
import com.epoint.zczwfw.auditpromisebook.api.entity.AuditPromiseBook;


/**
 * 卫生许可告知书附件表list页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@RestController("auditpromisebooklistaction")
@Scope("request")
public class AuditPromiseBookListAction  extends BaseController
{
	@Autowired
    private IAuditPromiseBookService service;
    
    /**
     * 卫生许可告知书附件表实体对象
     */
    private AuditPromiseBook dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditPromiseBook> model;
  	
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
    
    public DataGridModel<AuditPromiseBook> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPromiseBook>()
            {

                @Override
                public List<AuditPromiseBook> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditPromiseBook> list = service.findList(
                            ListGenerator.generateSql("audit_promise_book", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditPromiseBook(ListGenerator.generateSql("audit_promise_book", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditPromiseBook getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditPromiseBook();
    	}
        return dataBean;
    }

    public void setDataBean(AuditPromiseBook dataBean)
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
