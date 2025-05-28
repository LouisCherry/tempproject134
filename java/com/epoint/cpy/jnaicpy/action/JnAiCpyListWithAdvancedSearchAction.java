package com.epoint.cpy.jnaicpy.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.cpy.jnaicpy.api.entity.JnAiCpy;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cpy.jnaicpy.api.IJnAiCpyService;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 成品油零售经营企业库list页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RestController("jnaicpylistwithadvancedsearchaction")
@Scope("request")
public class JnAiCpyListWithAdvancedSearchAction  extends BaseController
{
	@Autowired
	private IJnAiCpyService service;  
    
    /**
     * 成品油零售经营企业库实体对象
     */
    private JnAiCpy dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<JnAiCpy> model;
  	
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
    
    public DataGridModel<JnAiCpy> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnAiCpy>()
            {

                @Override
                public List<JnAiCpy> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql += " and is_enable = '1'";
                    List<JnAiCpy> list = service.findList(
                            ListGenerator.generateSql("jn_ai_cpy", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countJnAiCpy(ListGenerator.generateSql("jn_ai_cpy", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public JnAiCpy getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new JnAiCpy();
    	}
        return dataBean;
    }

    public void setDataBean(JnAiCpy dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("address,certdate,certificate,certno,code,legal,name,validitytime", "地址,发证日期,发证机关,证书编号,统一社会信用代码,法定代表人(企业负责人),企业名称,有效期");
        }
        return exportModel;
    }
    


}
