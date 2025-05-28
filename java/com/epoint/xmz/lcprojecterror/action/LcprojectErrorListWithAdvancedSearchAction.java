package com.epoint.xmz.lcprojecterror.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 浪潮推送失败记录表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@RestController("lcprojecterrorlistwithadvancedsearchaction")
@Scope("request")
public class LcprojectErrorListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private ILcprojectErrorService service;  
    
    /**
     * 浪潮推送失败记录表实体对象
     */
    private LcprojectError dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<LcprojectError> model;
  	
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
    
    public DataGridModel<LcprojectError> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<LcprojectError>()
            {

                @Override
                public List<LcprojectError> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<LcprojectError> list = service.findList(
                            ListGenerator.generateSql("lcproject_error", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countLcprojectError(ListGenerator.generateSql("lcproject_error", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public LcprojectError getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new LcprojectError();
    	}
        return dataBean;
    }

    public void setDataBean(LcprojectError dataBean)
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
