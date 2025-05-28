package com.epoint.xmz.xsxmjbqk.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.xsxmjbqk.api.entity.XsXmjbqk;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.xsxmjbqk.api.IXsXmjbqkService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土项目基本情况list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:46:23]
 */
@RestController("xsxmjbqklistwithadvancedsearchaction")
@Scope("request")
public class XsXmjbqkListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IXsXmjbqkService service;  
    
    /**
     * 国土项目基本情况实体对象
     */
    private XsXmjbqk dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsXmjbqk> model;
  	
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
    
    public DataGridModel<XsXmjbqk> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsXmjbqk>()
            {

                @Override
                public List<XsXmjbqk> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsXmjbqk> list = service.findList(
                            ListGenerator.generateSql("XS_XMJBQK", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countXsXmjbqk(ListGenerator.generateSql("XS_XMJBQK", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsXmjbqk getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsXmjbqk();
    	}
        return dataBean;
    }

    public void setDataBean(XsXmjbqk dataBean)
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
