package com.epoint.xmz.xmzfdckfzzzs.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xmzfdckfzzzs.api.entity.XmzFdckfzzzs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xmzfdckfzzzs.api.IXmzFdckfzzzsService;


/**
 * 房地产开发资质证书list页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-12 09:40:37]
 */
@RestController("xmzfdckfzzzslistaction")
@Scope("request")
public class XmzFdckfzzzsListAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IXmzFdckfzzzsService service;
    
    /**
     * 房地产开发资质证书实体对象
     */
    private XmzFdckfzzzs dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XmzFdckfzzzs> model;
  	
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
    
    public DataGridModel<XmzFdckfzzzs> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XmzFdckfzzzs>()
            {

                @Override
                public List<XmzFdckfzzzs> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XmzFdckfzzzs> list = service.findList(
                            ListGenerator.generateSql("xmz_fdckfzzzs", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXmzFdckfzzzs(ListGenerator.generateSql("xmz_fdckfzzzs", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XmzFdckfzzzs getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XmzFdckfzzzs();
    	}
        return dataBean;
    }

    public void setDataBean(XmzFdckfzzzs dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("dz,fddbr,fzjg,fzrq,gglx,pzsj,qhdm,qx,qymc,yxqz,yyzz,zczb,zsbh,zzdj", "地址,法定代表人,发证机关,发证日期,公司类型,批准时间,区划代码,区县,企业名称,有效期至,营业执照,注册资本(万元),证书编号,资质等级");
        }
        return exportModel;
    }
    


}
