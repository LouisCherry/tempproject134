package com.epoint.cs.yyzzprint.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.yyzzprint.api.entity.Yyzzprint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.yyzzprint.api.IYyzzprintService;


/**
 * 营业执照打印记录表list页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2020-04-23 11:25:13]
 */
@RestController("yyzzprintlistaction")
@Scope("request")
public class YyzzprintListAction  extends BaseController
{
	@Autowired
    private IYyzzprintService service;
    
    /**
     * 营业执照打印记录表实体对象
     */
    private Yyzzprint dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Yyzzprint> model;
  	
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
    
    public DataGridModel<Yyzzprint> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Yyzzprint>()
            {

                @Override
                public List<Yyzzprint> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Yyzzprint> list = service.findList(
                            ListGenerator.generateSql("yyzzprint", conditionSql, "operatedate", "desc"), first, pageSize,
                            conditionList.toArray());
                   int count = service.countYyzzprint(ListGenerator.generateSql("yyzzprint", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public Yyzzprint getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Yyzzprint();
    	}
        return dataBean;
    }

    public void setDataBean(Yyzzprint dataBean)
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
