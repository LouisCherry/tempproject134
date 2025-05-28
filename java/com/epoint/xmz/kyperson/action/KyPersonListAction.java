package com.epoint.xmz.kyperson.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.kyperson.api.entity.KyPerson;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.kyperson.api.IKyPersonService;


/**
 * 勘验人员表list页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:34:44]
 */
@RestController("kypersonlistaction")
@Scope("request")
public class KyPersonListAction  extends BaseController
{
	@Autowired
    private IKyPersonService service;
    
    /**
     * 勘验人员表实体对象
     */
    private KyPerson dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<KyPerson> model;
  	
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
    
    public DataGridModel<KyPerson> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<KyPerson>()
            {

                @Override
                public List<KyPerson> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<KyPerson> list = service.findList(
                            ListGenerator.generateSql("ky_person", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countKyPerson(ListGenerator.generateSql("ky_person", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public KyPerson getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new KyPerson();
    	}
        return dataBean;
    }

    public void setDataBean(KyPerson dataBean)
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
