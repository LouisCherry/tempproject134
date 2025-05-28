package com.epoint.xmz.gtxstkjzd.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtxstkjzd.api.entity.GtxsTkjzd;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.gtxstkjzd.api.IGtxsTkjzdService;


/**
 * 国土踏勘界址点list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 18:06:12]
 */
@RestController("gtxstkjzdlistaction")
@Scope("request")
public class GtxsTkjzdListAction  extends BaseController
{
	@Autowired
    private IGtxsTkjzdService service;
    
    /**
     * 国土踏勘界址点实体对象
     */
    private GtxsTkjzd dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<GtxsTkjzd> model;
  	
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
    
    public DataGridModel<GtxsTkjzd> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<GtxsTkjzd>()
            {

                @Override
                public List<GtxsTkjzd> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<GtxsTkjzd> list = service.findList(
                            ListGenerator.generateSql("GTXS_TKJZD", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countGtxsTkjzd(ListGenerator.generateSql("GTXS_TKJZD", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public GtxsTkjzd getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new GtxsTkjzd();
    	}
        return dataBean;
    }

    public void setDataBean(GtxsTkjzd dataBean)
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
