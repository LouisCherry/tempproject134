package com.epoint.xmz.certhwslysjyxk.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;


/**
 * 省际普通货物水路运输经营许可本地库list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@RestController("certhwslysjyxklistaction")
@Scope("request")
public class CertHwslysjyxkListAction  extends BaseController
{
	@Autowired
    private ICertHwslysjyxkService service;
    
    /**
     * 省际普通货物水路运输经营许可本地库实体对象
     */
    private CertHwslysjyxk dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<CertHwslysjyxk> model;
  	
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
    
    public DataGridModel<CertHwslysjyxk> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CertHwslysjyxk>()
            {

                @Override
                public List<CertHwslysjyxk> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
//                    conditionSql += " and is_enable = '1'";
                    
                    List<CertHwslysjyxk> list = service.findList(
                            ListGenerator.generateSql("cert_hwslysjyxk", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countCertHwslysjyxk(ListGenerator.generateSql("cert_hwslysjyxk", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public CertHwslysjyxk getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new CertHwslysjyxk();
    	}
        return dataBean;
    }

    public void setDataBean(CertHwslysjyxk dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("fddbr,jyqy,jyrdz,jyzmc,lxdh,qyxz", "法定代表人,经营区域,经营人地址,经营人名称,联系电话,企业性质");
        }
        return exportModel;
    }
    


}
