package com.epoint.cert.auditcertrelation.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zczwfw.zccommon.api.IZcCommonService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 证照字段关系表list页面对应的后台
 * 
 * @author miemieyang12128
 * @version [版本号, 2024-10-16 09:08:40]
 */
@RestController("auditcertrelationlistaction")
@Scope("request")
public class AuditCertRelationListAction  extends BaseController
{
	@Autowired
    private IAuditCertRelationService service;

    @Autowired
    private IZcCommonService iZcCommonService;
    
    /**
     * 证照字段关系表实体对象
     */
    private AuditCertRelation dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditCertRelation> model;
  	
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
    
    public DataGridModel<AuditCertRelation> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditCertRelation>()
            {
                @Override
                public List<AuditCertRelation> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(dataBean.getCertname())){
                        sqlConditionUtil.like("certname", dataBean.getCertname());
                    }
                    PageData<AuditCertRelation> pages = iZcCommonService.paginatorList(AuditCertRelation.class, sqlConditionUtil.getMap(), first, pageSize);
                    List<AuditCertRelation> list = pages.getList();
                    int count = pages.getRowCount();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    
    public AuditCertRelation getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditCertRelation();
    	}
        return dataBean;
    }

    public void setDataBean(AuditCertRelation dataBean)
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
