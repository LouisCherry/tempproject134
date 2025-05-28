package com.epoint.xmz.zjcreidtquery.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 信用查询调用统计表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@RestController("zjcreidtquerylistwithadvancedsearchaction")
@Scope("request")
public class ZjCreidtqueryListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IZjCreidtqueryService service;  
    
    /**
     * 信用查询调用统计表实体对象
     */
    private ZjCreidtquery dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<ZjCreidtquery> model;
  	
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
    
    public DataGridModel<ZjCreidtquery> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ZjCreidtquery>()
            {

                @Override
                public List<ZjCreidtquery> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<ZjCreidtquery> list = service.findList(
                            ListGenerator.generateSql("zj_creidtquery", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countZjCreidtquery(ListGenerator.generateSql("zj_creidtquery", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public ZjCreidtquery getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new ZjCreidtquery();
    	}
        return dataBean;
    }

    public void setDataBean(ZjCreidtquery dataBean)
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
