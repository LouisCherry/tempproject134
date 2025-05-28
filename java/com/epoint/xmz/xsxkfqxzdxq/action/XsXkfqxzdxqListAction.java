package com.epoint.xmz.xsxkfqxzdxq.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkfqxzdxq.api.entity.XsXkfqxzdxq;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsxkfqxzdxq.api.IXsXkfqxzdxqService;


/**
 * 国土许可分区县占地详情list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:32]
 */
@RestController("xsxkfqxzdxqlistaction")
@Scope("request")
public class XsXkfqxzdxqListAction  extends BaseController
{
	@Autowired
    private IXsXkfqxzdxqService service;
    
    /**
     * 国土许可分区县占地详情实体对象
     */
    private XsXkfqxzdxq dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsXkfqxzdxq> model;
  	
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
    
    public DataGridModel<XsXkfqxzdxq> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsXkfqxzdxq>()
            {

                @Override
                public List<XsXkfqxzdxq> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsXkfqxzdxq> list = service.findList(
                            ListGenerator.generateSql("XS_XKFQXZDXQ", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsXkfqxzdxq(ListGenerator.generateSql("XS_XKFQXZDXQ", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsXkfqxzdxq getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsXkfqxzdxq();
    	}
        return dataBean;
    }

    public void setDataBean(XsXkfqxzdxq dataBean)
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
