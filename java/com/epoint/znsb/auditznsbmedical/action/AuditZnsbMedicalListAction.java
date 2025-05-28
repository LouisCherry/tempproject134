package com.epoint.znsb.auditznsbmedical.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbmedical.api.entity.AuditZnsbMedical;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbmedical.api.IAuditZnsbMedicalService;


/**
 * 静态医疗信息查询list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-27 09:56:35]
 */
@RestController("auditznsbmedicallistaction")
@Scope("request")
public class AuditZnsbMedicalListAction  extends BaseController
{
	@Autowired
    private IAuditZnsbMedicalService service;
    
    /**
     * 静态医疗信息查询实体对象
     */
    private AuditZnsbMedical dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbMedical> model;
  	
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
    
    public DataGridModel<AuditZnsbMedical> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbMedical>()
            {

                @Override
                public List<AuditZnsbMedical> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditZnsbMedical> list = service.findList(
                            ListGenerator.generateSql("AUDIT_ZNSB_MEDICAL", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditZnsbMedical(ListGenerator.generateSql("AUDIT_ZNSB_MEDICAL", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditZnsbMedical getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbMedical();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbMedical dataBean)
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
