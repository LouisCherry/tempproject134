package com.epoint.xmz.cjrmzarea.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.cjrmzarea.api.entity.CjrMzArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.cjrmzarea.api.ICjrMzAreaService;


/**
 * 残疾人民政辖区对应表list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-05-24 17:45:08]
 */
@RestController("cjrmzarealistaction")
@Scope("request")
public class CjrMzAreaListAction  extends BaseController
{
	@Autowired
    private ICjrMzAreaService service;
    
    /**
     * 残疾人民政辖区对应表实体对象
     */
    private CjrMzArea dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<CjrMzArea> model;
  	
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
    
    public DataGridModel<CjrMzArea> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CjrMzArea>()
            {

                @Override
                public List<CjrMzArea> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<CjrMzArea> list = service.findList(
                            ListGenerator.generateSql("cjr_mz_area", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countCjrMzArea(ListGenerator.generateSql("cjr_mz_area", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public CjrMzArea getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new CjrMzArea();
    	}
        return dataBean;
    }

    public void setDataBean(CjrMzArea dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("cjrareacode,cjrname,mzareacode,mzname", "残疾人所属辖区,残疾人辖区名称,民政所属辖区,民政名称");
        }
        return exportModel;
    }
    


}
