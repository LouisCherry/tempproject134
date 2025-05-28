package com.epoint.takan.kanyanmain.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;


/**
 * 勘验主表list页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RestController("kanyanmainlistaction")
@Scope("request")
public class KanyanmainListAction  extends BaseController
{
	@Autowired
    private IKanyanmainService service;
    
    /**
     * 勘验主表实体对象
     */
    private Kanyanmain dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<Kanyanmain> model;
  	
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
    
    public DataGridModel<Kanyanmain> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Kanyanmain>()
            {

                @Override
                public List<Kanyanmain> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Kanyanmain> list = service.findList(
                            ListGenerator.generateSql("KANYANMAIN", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countKanyanmain(ListGenerator.generateSql("KANYANMAIN", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public Kanyanmain getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new Kanyanmain();
    	}
        return dataBean;
    }

    public void setDataBean(Kanyanmain dataBean)
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
