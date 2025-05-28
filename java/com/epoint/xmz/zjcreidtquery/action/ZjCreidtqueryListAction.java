package com.epoint.xmz.zjcreidtquery.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;


/**
 * 信用查询调用统计表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@RestController("zjcreidtquerylistaction")
@Scope("request")
public class ZjCreidtqueryListAction  extends BaseController
{
	@Autowired
    private IZjCreidtqueryService service;
    
    /**
     * 信用查询调用统计表实体对象
     */
    private Record dataBean;
    
    private Date startDate;

    private Date endDate;
    
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    
    /**
     * 企业标签复选框组model
     */
    private List<SelectItem> qybqModel = null;
    
    private List<SelectItem> typeModel = null;
    
  	
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
    
    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	
                    
                	List<Record> lst = service.finList(first, pageSize,startDate,endDate,dataBean.getStr("type"),dataBean.getStr("areacode"));
                    Integer row = service.finTotal(startDate,endDate,dataBean.getStr("type"),dataBean.getStr("areacode"));
                    this.setRowCount(row == null ? 0 : row);
                    return lst;
                }

            };
        }
        return model;
    }

    
    public Record getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Record();
    	}
        return dataBean;
    }

    public void setDataBean(Record dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }
    
    public List<SelectItem> getQybqModel() {
        if (qybqModel == null) {
            qybqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属区县", null, true));
        }
        return this.qybqModel;
    }
    
    public List<SelectItem> getTypeModel() {
    	if (typeModel == null) {
    		typeModel = DataUtil.convertMap2ComboBox(
    				(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "信用接口类型", null, true));
    	}
    	return this.typeModel;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    


}
