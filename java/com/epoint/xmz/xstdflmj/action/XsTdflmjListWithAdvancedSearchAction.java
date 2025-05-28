package com.epoint.xmz.xstdflmj.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.xstdflmj.api.entity.XsTdflmj;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.xstdflmj.api.IXsTdflmjService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土土地分类面积list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:56]
 */
@RestController("xstdflmjlistwithadvancedsearchaction")
@Scope("request")
public class XsTdflmjListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IXsTdflmjService service;  
    
    /**
     * 国土土地分类面积实体对象
     */
    private XsTdflmj dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsTdflmj> model;
  	
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
    
    public DataGridModel<XsTdflmj> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsTdflmj>()
            {

                @Override
                public List<XsTdflmj> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsTdflmj> list = service.findList(
                            ListGenerator.generateSql("XS_TDFLMJ", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countXsTdflmj(ListGenerator.generateSql("XS_TDFLMJ", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsTdflmj getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsTdflmj();
    	}
        return dataBean;
    }

    public void setDataBean(XsTdflmj dataBean)
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
