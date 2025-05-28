package com.epoint.takan.kanyanproject.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 勘验项目list页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 03:15:13]
 */
@RestController("kanyanprojectlistwithadvancedsearchaction")
@Scope("request")
public class KanyanprojectListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IKanyanprojectService service;  
    
    /**
     * 勘验项目实体对象
     */
    private Kanyanproject dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Kanyanproject> model;
  	
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
    
    public DataGridModel<Kanyanproject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Kanyanproject>()
            {

                @Override
                public List<Kanyanproject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Kanyanproject> list = service.findList(
                            ListGenerator.generateSql("KANYANPROJECT", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countKanyanproject(ListGenerator.generateSql("KANYANPROJECT", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public Kanyanproject getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Kanyanproject();
    	}
        return dataBean;
    }

    public void setDataBean(Kanyanproject dataBean)
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
