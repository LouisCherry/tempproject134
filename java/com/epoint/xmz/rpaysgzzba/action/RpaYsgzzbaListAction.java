package com.epoint.xmz.rpaysgzzba.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpaysgzzba.api.entity.RpaYsgzzba;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpaysgzzba.api.IRpaYsgzzbaService;


/**
 * 院士工作站备案list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 10:26:25]
 */
@RestController("rpaysgzzbalistaction")
@Scope("request")
public class RpaYsgzzbaListAction  extends BaseController
{
	@Autowired
    private IRpaYsgzzbaService service;
    
    /**
     * 院士工作站备案实体对象
     */
    private RpaYsgzzba dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<RpaYsgzzba> model;
  	
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
    
    public DataGridModel<RpaYsgzzba> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RpaYsgzzba>()
            {

                @Override
                public List<RpaYsgzzba> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<RpaYsgzzba> list = service.findList(
                            ListGenerator.generateSql("rpa_ysgzzba", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countRpaYsgzzba(ListGenerator.generateSql("rpa_ysgzzba", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public RpaYsgzzba getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new RpaYsgzzba();
    	}
        return dataBean;
    }

    public void setDataBean(RpaYsgzzba dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("companyname,noticedate", "单位名称,通知公告日期");
        }
        return exportModel;
    }
    


}
