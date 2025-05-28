package com.epoint.cs.tsproject.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.cs.tsproject.api.entity.TsProject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cs.tsproject.api.ITsProjectService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 推送数据list页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RestController("tsprojectlistwithadvancedsearchaction")
@Scope("request")
public class TsProjectListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private ITsProjectService service;  
    
    /**
     * 推送数据实体对象
     */
    private TsProject dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<TsProject> model;
  	
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
    
    public DataGridModel<TsProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<TsProject>()
            {

                @Override
                public List<TsProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<TsProject> list = service.findList(
                            ListGenerator.generateSql("ts_project", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("ts_project", conditionSql), TsProject.class,
                            conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public TsProject getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new TsProject();
    	}
        return dataBean;
    }

    public void setDataBean(TsProject dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("accepttime,accpetname,applyname,applytime,banjiename,banjietime,flow,status,taskname", "受理时间,受理人员,申请人,申请时间,办结人员,办结时间,办件编号,办件状态,事项名称");
        }
        return exportModel;
    }
    


}
