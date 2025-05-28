package com.epoint.xmz.xstkdk.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstkdk.api.entity.XsTkdk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xstkdk.api.IXsTkdkService;


/**
 * 国土踏勘地块list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:19:01]
 */
@RestController("xstkdklistaction")
@Scope("request")
public class XsTkdkListAction  extends BaseController
{
	@Autowired
    private IXsTkdkService service;
    
    /**
     * 国土踏勘地块实体对象
     */
    private XsTkdk dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsTkdk> model;
  	
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
    
    public DataGridModel<XsTkdk> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsTkdk>()
            {

                @Override
                public List<XsTkdk> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsTkdk> list = service.findList(
                            ListGenerator.generateSql("XS_TKDK", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsTkdk(ListGenerator.generateSql("XS_TKDK", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsTkdk getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsTkdk();
    	}
        return dataBean;
    }

    public void setDataBean(XsTkdk dataBean)
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
