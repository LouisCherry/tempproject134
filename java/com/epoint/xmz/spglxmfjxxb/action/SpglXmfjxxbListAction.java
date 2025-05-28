package com.epoint.xmz.spglxmfjxxb.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.spglxmfjxxb.api.ISpglXmfjxxbService;


/**
 * 项目附件信息表list页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-10-17 18:08:02]
 */
@RestController("spglxmfjxxblistaction")
@Scope("request")
public class SpglXmfjxxbListAction  extends BaseController
{
	@Autowired
    private ISpglXmfjxxbService service;
    
    /**
     * 项目附件信息表实体对象
     */
    private SpglXmfjxxb dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmfjxxb> model;
  	
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
        addCallbackParam("msg", l("成功删除！"));
    }
    
    public DataGridModel<SpglXmfjxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmfjxxb>()
            {

                @Override
                public List<SpglXmfjxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<SpglXmfjxxb> list = service.findList(
                            ListGenerator.generateSql("spgl_xmfjxxb", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countSpglXmfjxxb(ListGenerator.generateSql("spgl_xmfjxxb", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public SpglXmfjxxb getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new SpglXmfjxxb();
    	}
        return dataBean;
    }

    public void setDataBean(SpglXmfjxxb dataBean)
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
