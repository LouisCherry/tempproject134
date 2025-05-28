package com.epoint.xmz.xsbzgzxx.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsbzgzxx.api.entity.XsBzgzxx;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsbzgzxx.api.IXsBzgzxxService;


/**
 * 国土补正告知list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:12]
 */
@RestController("xsbzgzxxlistaction")
@Scope("request")
public class XsBzgzxxListAction  extends BaseController
{
	@Autowired
    private IXsBzgzxxService service;
    
    /**
     * 国土补正告知实体对象
     */
    private XsBzgzxx dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsBzgzxx> model;
  	
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
    
    public DataGridModel<XsBzgzxx> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsBzgzxx>()
            {

                @Override
                public List<XsBzgzxx> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsBzgzxx> list = service.findList(
                            ListGenerator.generateSql("XS_BZGZXX", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsBzgzxx(ListGenerator.generateSql("XS_BZGZXX", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsBzgzxx getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsBzgzxx();
    	}
        return dataBean;
    }

    public void setDataBean(XsBzgzxx dataBean)
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
