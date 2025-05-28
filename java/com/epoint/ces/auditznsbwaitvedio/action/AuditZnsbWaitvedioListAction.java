package com.epoint.ces.auditznsbwaitvedio.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.auditznsbwaitvedio.api.entity.AuditZnsbWaitvedio;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.auditznsbwaitvedio.api.IAuditZnsbWaitvedioService;


/**
 * 等待屏视频list页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2020-03-31 15:07:02]
 */
@RestController("auditznsbwaitvediolistaction")
@Scope("request")
public class AuditZnsbWaitvedioListAction  extends BaseController
{
	@Autowired
    private IAuditZnsbWaitvedioService service;
    
    /**
     * 等待屏视频实体对象
     */
    private AuditZnsbWaitvedio dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbWaitvedio> model;
  	
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
    
    public DataGridModel<AuditZnsbWaitvedio> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbWaitvedio>()
            {

                @Override
                public List<AuditZnsbWaitvedio> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<AuditZnsbWaitvedio> list = service.findList(
                            ListGenerator.generateSql("audit_znsb_waitvedio", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditZnsbWaitvedio(ListGenerator.generateSql("audit_znsb_waitvedio", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditZnsbWaitvedio getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbWaitvedio();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbWaitvedio dataBean)
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
