package com.epoint.zoucheng.device.infopub.webinfo.action;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.zoucheng.device.infopub.webinfo.api.IInfopubWebinfoService;
import com.epoint.zoucheng.device.infopub.webinfo.api.entity.InfopubWebinfo;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;


/**
 * 网页信息表list页面对应的后台
 * 
 * @author why
 * @version [版本号, 2019-09-17 11:17:19]
 */
@RestController("infopubwebinfolistaction")
@Scope("request")
public class InfopubWebinfoListAction  extends BaseController
{
	@Autowired
    private IInfopubWebinfoService service;
    
    /**
     * 网页信息表实体对象
     */
    private InfopubWebinfo dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<InfopubWebinfo> model;
  	
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
    
    public DataGridModel<InfopubWebinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<InfopubWebinfo>()
            {

                @Override
                public List<InfopubWebinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<InfopubWebinfo> list = service.findList(
                            ListGenerator.generateSql("InfoPub_WebInfo", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("InfoPub_WebInfo", conditionSql, sortField, sortOrder), conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
    
    public InfopubWebinfo getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new InfopubWebinfo();
    	}
        return dataBean;
    }

    public void setDataBean(InfopubWebinfo dataBean)
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
