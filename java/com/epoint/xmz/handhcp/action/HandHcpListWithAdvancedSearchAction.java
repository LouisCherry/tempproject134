package com.epoint.xmz.handhcp.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.xmz.handhcp.api.entity.HandHcp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.xmz.handhcp.api.IHandHcpService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 手动推送好差评表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-03-29 16:57:08]
 */
@RestController("handhcplistwithadvancedsearchaction")
@Scope("request")
public class HandHcpListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IHandHcpService service;  
    
    /**
     * 手动推送好差评表实体对象
     */
    private HandHcp dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<HandHcp> model;
  	
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
    
    public DataGridModel<HandHcp> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<HandHcp>()
            {

                @Override
                public List<HandHcp> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<HandHcp> list = service.findList(
                            ListGenerator.generateSql("hand_hcp", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countHandHcp(ListGenerator.generateSql("hand_hcp", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public HandHcp getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new HandHcp();
    	}
        return dataBean;
    }

    public void setDataBean(HandHcp dataBean)
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
