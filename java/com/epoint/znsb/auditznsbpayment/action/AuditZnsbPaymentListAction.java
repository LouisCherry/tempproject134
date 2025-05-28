package com.epoint.znsb.auditznsbpayment.action;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbpayment.api.entity.AuditZnsbPayment;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbpayment.api.IAuditZnsbPaymentService;


/**
 * 水电气缴费list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-05-18 18:32:19]
 */
@RestController("auditznsbpaymentlistaction")
@Scope("request")
public class AuditZnsbPaymentListAction  extends BaseController
{
	@Autowired
    private IAuditZnsbPaymentService service;
    
    /**
     * 水电气缴费实体对象
     */
    private AuditZnsbPayment dataBean;
  
    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbPayment> model;
  	
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
    
    public DataGridModel<AuditZnsbPayment> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbPayment>()
            {

                @Override
                public List<AuditZnsbPayment> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
                    if(StringUtil.isNotBlank(centerguid)){
                        conditionSql = conditionSql + "  and centerguid = '" + centerguid+  "'";
                    }
                    List<AuditZnsbPayment> list = service.findList(
                            ListGenerator.generateSql("AUDIT_ZNSB_PAYMENT", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                   int count = service.countAuditZnsbPayment(ListGenerator.generateSql("AUDIT_ZNSB_PAYMENT", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    
    public AuditZnsbPayment getDataBean()
    {
    	if(dataBean == null){
    		dataBean = new AuditZnsbPayment();
    	}
        return dataBean;
    }

    public void setDataBean(AuditZnsbPayment dataBean)
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
