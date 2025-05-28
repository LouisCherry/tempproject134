package com.epoint.xmz.xsyjsfj.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsyjsfj.api.entity.XsYjsfj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsyjsfj.api.IXsYjsfjService;


/**
 * 国土上传预审批复函list页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:42]
 */
@RestController("xsyjsfjlistaction")
@Scope("request")
public class XsYjsfjListAction  extends BaseController
{
	@Autowired
    private IXsYjsfjService service;
    
    /**
     * 国土上传预审批复函实体对象
     */
    private XsYjsfj dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<XsYjsfj> model;
  	
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
    
    public DataGridModel<XsYjsfj> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<XsYjsfj>()
            {

                @Override
                public List<XsYjsfj> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<XsYjsfj> list = service.findList(
                            ListGenerator.generateSql("XS_YJSFJ", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countXsYjsfj(ListGenerator.generateSql("XS_YJSFJ", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public XsYjsfj getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new XsYjsfj();
    	}
        return dataBean;
    }

    public void setDataBean(XsYjsfj dataBean)
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
