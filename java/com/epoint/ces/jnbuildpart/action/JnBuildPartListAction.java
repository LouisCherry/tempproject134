package com.epoint.ces.jnbuildpart.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;


/**
 * 建筑业企业资质数据库list页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@RestController("jnbuildpartlistaction")
@Scope("request")
public class JnBuildPartListAction  extends BaseController
{
	@Autowired
    private IJnBuildPartService service;
    
    /**
     * 建筑业企业资质数据库实体对象
     */
    private JnBuildPart dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<JnBuildPart> model;
  	
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
    
    public DataGridModel<JnBuildPart> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<JnBuildPart>()
            {

                @Override
                public List<JnBuildPart> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    conditionSql += " and is_enable = '1'";
                    List<JnBuildPart> list = service.findList(
                            ListGenerator.generateSql("jn_build_part", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countJnBuildPart(ListGenerator.generateSql("jn_build_part", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public JnBuildPart getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new JnBuildPart();
    	}
        return dataBean;
    }

    public void setDataBean(JnBuildPart dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("address,certdate,certificate,code,econ,itemname,legal,register,validitytime", "详细地址,发证日期,发证机关,统一社会信用代码(或营业执照注册号),经济性质,企业名称,法定代表人,注册资本,有效期");
        }
        return exportModel;
    }
    


}
