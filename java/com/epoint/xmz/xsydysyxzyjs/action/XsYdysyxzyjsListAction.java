package com.epoint.xmz.xsydysyxzyjs.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsydysyxzyjs.api.entity.XsYdysyxzyjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsydysyxzyjs.api.IXsYdysyxzyjsService;


/**
 * 国土用地预审与选址意见书list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:36]
 */
@RestController("xsydysyxzyjslistaction")
@Scope("request")
public class XsYdysyxzyjsListAction  extends BaseController
{
	@Autowired
    private IXsYdysyxzyjsService service;
    
    /**
     * 国土用地预审与选址意见书实体对象
     */
    private XsYdysyxzyjs dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsYdysyxzyjs> model;
  	
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
    
    public DataGridModel<XsYdysyxzyjs> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsYdysyxzyjs>()
            {

                @Override
                public List<XsYdysyxzyjs> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsYdysyxzyjs> list = service.findList(
                            ListGenerator.generateSql("XS_YDYSYXZYJS", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsYdysyxzyjs(ListGenerator.generateSql("XS_YDYSYXZYJS", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsYdysyxzyjs getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsYdysyxzyjs();
    	}
        return dataBean;
    }

    public void setDataBean(XsYdysyxzyjs dataBean)
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
