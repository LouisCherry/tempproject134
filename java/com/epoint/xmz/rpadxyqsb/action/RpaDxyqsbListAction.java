package com.epoint.xmz.rpadxyqsb.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.rpadxyqsb.api.entity.RpaDxyqsb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpadxyqsb.api.IRpaDxyqsbService;


/**
 * 大型仪器设备协作共用网list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 17:36:22]
 */
@RestController("rpadxyqsblistaction")
@Scope("request")
public class RpaDxyqsbListAction  extends BaseController
{
	@Autowired
    private IRpaDxyqsbService service;
    
    /**
     * 大型仪器设备协作共用网实体对象
     */
    private RpaDxyqsb dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<RpaDxyqsb> model;
  	
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
    
    public DataGridModel<RpaDxyqsb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<RpaDxyqsb>()
            {

                @Override
                public List<RpaDxyqsb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<RpaDxyqsb> list = service.findList(
                            ListGenerator.generateSql("rpa_dxyqsb", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countRpaDxyqsb(ListGenerator.generateSql("rpa_dxyqsb", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public RpaDxyqsb getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new RpaDxyqsb();
    	}
        return dataBean;
    }

    public void setDataBean(RpaDxyqsb dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("content,name", "扫描内容,公司名称");
        }
        return exportModel;
    }
    


}
