package com.epoint.xmz.certcbyyysz.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.certcbyyysz.api.ICertCbyyyszService;


/**
 * 船舶营业运输证本地库list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-06-15 14:41:13]
 */
@RestController("certcbyyyszlistaction")
@Scope("request")
public class CertCbyyyszListAction  extends BaseController
{
	@Autowired
    private ICertCbyyyszService service;
    
    /**
     * 船舶营业运输证本地库实体对象
     */
    private CertCbyyysz dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<CertCbyyysz> model;
  	
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
    
    public DataGridModel<CertCbyyysz> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CertCbyyysz>()
            {

                @Override
                public List<CertCbyyysz> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
//                    conditionSql += " and is_enable = '1'";

                    
                    List<CertCbyyysz> list = service.findList(
                            ListGenerator.generateSql("cert_cbyyysz", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countCertCbyyysz(ListGenerator.generateSql("cert_cbyyysz", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public CertCbyyysz getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new CertCbyyysz();
    	}
        return dataBean;
    }

    public void setDataBean(CertCbyyysz dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("cbdjh,cbly,cbsyr,cbzl,fzjg,fzrq,jcrq,jyxkzbh,xkzjyfw,yxrq,yyzbh,zd,zwcm,zyhl", "船舶登记号,船舶来源,船舶所有人,船舶种类,发证机构,发证日期,建成日期,经营许可证编号,许可证经营范围,有效日期,营运证编号,总吨,中文船名,主运货类");
        }
        return exportModel;
    }
    


}
