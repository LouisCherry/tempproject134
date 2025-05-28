package com.epoint.xmz.xsxkjl.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkjl.api.entity.XsXkjl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsxkjl.api.IXsXkjlService;


/**
 * 国土许可记录list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:17]
 */
@RestController("xsxkjllistaction")
@Scope("request")
public class XsXkjlListAction  extends BaseController
{
	@Autowired
    private IXsXkjlService service;
    
    /**
     * 国土许可记录实体对象
     */
    private XsXkjl dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsXkjl> model;
  	
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
    
    public DataGridModel<XsXkjl> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsXkjl>()
            {

                @Override
                public List<XsXkjl> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsXkjl> list = service.findList(
                            ListGenerator.generateSql("XS_XKJL", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsXkjl(ListGenerator.generateSql("XS_XKJL", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsXkjl getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsXkjl();
    	}
        return dataBean;
    }

    public void setDataBean(XsXkjl dataBean)
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
