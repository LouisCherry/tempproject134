package com.epoint.xmz.xssbb.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.xssbb.api.entity.XsSbb;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.xssbb.api.IXsSbbService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土_申报表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:34:58]
 */
@RestController("xssbblistwithadvancedsearchaction")
@Scope("request")
public class XsSbbListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IXsSbbService service;  
    
    /**
     * 国土_申报表实体对象
     */
    private XsSbb dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsSbb> model;
  	
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
    
    public DataGridModel<XsSbb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsSbb>()
            {

                @Override
                public List<XsSbb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsSbb> list = service.findList(
                            ListGenerator.generateSql("XS_SBB", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countXsSbb(ListGenerator.generateSql("XS_SBB", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsSbb getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsSbb();
    	}
        return dataBean;
    }

    public void setDataBean(XsSbb dataBean)
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
