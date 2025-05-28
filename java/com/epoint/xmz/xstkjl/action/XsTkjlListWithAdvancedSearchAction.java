package com.epoint.xmz.xstkjl.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.xstkjl.api.entity.XsTkjl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.xstkjl.api.IXsTkjlService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土踏勘记录list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:51]
 */
@RestController("xstkjllistwithadvancedsearchaction")
@Scope("request")
public class XsTkjlListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IXsTkjlService service;  
    
    /**
     * 国土踏勘记录实体对象
     */
    private XsTkjl dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsTkjl> model;
  	
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
    
    public DataGridModel<XsTkjl> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsTkjl>()
            {

                @Override
                public List<XsTkjl> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsTkjl> list = service.findList(
                            ListGenerator.generateSql("XS_TKJL", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countXsTkjl(ListGenerator.generateSql("XS_TKJL", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsTkjl getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsTkjl();
    	}
        return dataBean;
    }

    public void setDataBean(XsTkjl dataBean)
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
