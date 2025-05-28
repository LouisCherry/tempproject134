package com.epoint.znsb.auditznsbytjextend.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;


/**
 * 一体机模块额外配置list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:50]
 */
@RestController("auditznsbytjextendlistaction")
@Scope("request")
public class AuditZnsbYtjextendListAction  extends BaseController
{
	@Autowired
    private IAuditZnsbYtjextendService service;
    
    /**
     * 一体机模块额外配置实体对象
     */
    private AuditZnsbYtjextend dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbYtjextend> model;
  	
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
    
    public DataGridModel<AuditZnsbYtjextend> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbYtjextend>()
            {

                @Override
                public List<AuditZnsbYtjextend> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditZnsbYtjextend> list = service.findList(
                            ListGenerator.generateSql("AUDIT_ZNSB_YTJEXTEND", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditZnsbYtjextend(ListGenerator.generateSql("AUDIT_ZNSB_YTJEXTEND", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditZnsbYtjextend getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbYtjextend();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbYtjextend dataBean)
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
