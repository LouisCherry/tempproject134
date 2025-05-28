package com.epoint.znsb.auditznsbytjlabel.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjlabel.api.entity.AuditZnsbYtjlabel;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbytjlabel.api.IAuditZnsbYtjlabelService;


/**
 * 一体机模块标签list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 09:47:21]
 */
@RestController("auditznsbytjlabellistaction")
@Scope("request")
public class AuditZnsbYtjlabelListAction  extends BaseController
{
	@Autowired
    private IAuditZnsbYtjlabelService service;
    
    /**
     * 一体机模块标签实体对象
     */
    private AuditZnsbYtjlabel dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbYtjlabel> model;
  	
    /**
     * 导出模型
     */
    private ExportModel exportModel;


    /**
     * 底色下拉列表model
     */
    private List<SelectItem>  labelcolorModel=null;


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
    
    public DataGridModel<AuditZnsbYtjlabel> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbYtjlabel>()
            {

                @Override
                public List<AuditZnsbYtjlabel> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    //system.out.println(conditionSql);
                    //system.out.println(conditionList);
                    //system.out.println( ListGenerator.generateSql("AUDIT_ZNSB_YTJLABEL", conditionSql, "ordernum", "desc"));
                    List<AuditZnsbYtjlabel> list = service.findList(
                            ListGenerator.generateSql("AUDIT_ZNSB_YTJLABEL", conditionSql, "ordernum", "desc"), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditZnsbYtjlabel(ListGenerator.generateSql("AUDIT_ZNSB_YTJLABEL", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditZnsbYtjlabel getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbYtjlabel();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbYtjlabel dataBean)
    {
        this.dataBean = dataBean;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public  List<SelectItem> getLabelcolorModel(){if(labelcolorModel==null){labelcolorModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","一体机标签底色",null,true));
    } return this.labelcolorModel;}

}
