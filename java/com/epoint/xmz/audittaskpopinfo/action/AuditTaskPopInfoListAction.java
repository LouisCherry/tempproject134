package com.epoint.xmz.audittaskpopinfo.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.audittaskpopinfo.api.IAuditTaskPopInfoService;


/**
 * 弹窗信息维护list页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@RestController("audittaskpopinfolistaction")
@Scope("request")
public class AuditTaskPopInfoListAction  extends BaseController
{
	@Autowired
    private IAuditTaskPopInfoService service;
    
    /**
     * 弹窗信息维护实体对象
     */
    private AuditTaskPopInfo dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskPopInfo> model;
  	
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
        addCallbackParam("msg", l("成功删除！"));
    }
    
    public DataGridModel<AuditTaskPopInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskPopInfo>()
            {

                @Override
                public List<AuditTaskPopInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditTaskPopInfo> list = service.findList(
                            ListGenerator.generateSql("audit_task_pop_info", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditTaskPopInfo(ListGenerator.generateSql("audit_task_pop_info", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditTaskPopInfo getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditTaskPopInfo();
    	}
        return dataBean;
    }

    public void setDataBean(AuditTaskPopInfo dataBean)
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
