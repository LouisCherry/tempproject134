package com.epoint.auditperformance.auditperformanceaccount.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancedetail.inter.IAuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 考评明细list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-09 10:31:38]
 */
@RestController("auditperformancedetailforaccountaction")
@Scope("request")
public class AuditPerformanceDetailForAccountAction extends BaseController
{
    private static final long serialVersionUID = -4577900728771402039L;

    @Autowired
    private IAuditPerformanceDetail service;
    
    @Autowired
    private IAuditPerformanceAccount accountservice;
    
    @Autowired
    private IAuditPerformanceRecord recrodservice;
    
    @Autowired 
    private IAuditPerformanceRecordRuleService ruleService;
    
    @Autowired 
    private IAuditPerformanceRecordRuleDetailService detailService ;

    /**
     * 考评明细实体对象
     */
    private AuditPerformanceDetail dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceDetail> model;
    
    private AuditPerformanceAccount account;
    
    private AuditPerformanceRecord record;
    
    private String objectguid;
    private String ruleguid;
    private String recordguid;
    private String recorddetailrulerowguid; 
    private List<SelectItem> detailrulemodel;
    
    @Override
    public void pageLoad() {
        objectguid = this.getRequestParameter("objectguid");
        ruleguid = this.getRequestParameter("ruleguid");
        recordguid = this.getRequestParameter("recordguid");
        String accountguid = this.getRequestParameter("guid");
        account = accountservice.findDetailByRowguid(accountguid).getResult();
        if(account!=null){
            record = recrodservice.find(account.getRecordrowguid()).getResult();
            ruleguid = account.getRecordrulerowguid();
            objectguid = account.getObjectguid();
            recordguid = account.getRecordrowguid();
            recorddetailrulerowguid = account.getRecorddetailrulerowguid();
            record.set("rulename", account.getRecordrulename());
        }else{            
            record = recrodservice.find(recordguid).getResult(); 
            record.set("rulename", ruleService.find(ruleguid).getRulename());
        }
        
    }


    public DataGridModel<AuditPerformanceDetail> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceDetail>()
            {

                @Override
                public List<AuditPerformanceDetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(recorddetailrulerowguid)){                       
                        sql.eq("a.recorddetailrulerowguid", recorddetailrulerowguid);
                    }
                    sql.setOrderDesc("a.gradetime");
                    sql.eq("a.recordrowguid",recordguid);
                    sql.eq("a.objectguid", objectguid);                        
                    sql.eq("a.recordrulerowguid", ruleguid);
                    sql.eq("a.centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    PageData<AuditPerformanceDetail> pagedata = service
                            .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }

    public List<SelectItem> getDetailModel(){
        if(detailrulemodel==null ){
            detailrulemodel = new ArrayList<SelectItem>();
            SelectItem item;
            List<AuditPerformanceRecordRuleDetail> detaillist = detailService.findFiledByRecordRowguidAndRuleguid("detailrulename,rowguid", recordguid, ruleguid).getResult();
            item = new SelectItem();
            item.setText("请选择...");
            detailrulemodel.add(item);
            for (AuditPerformanceRecordRuleDetail auditPerformanceRecordRuleDetail : detaillist) {
                item = new SelectItem();
                item.setText(auditPerformanceRecordRuleDetail.getDetailrulename());
                item.setValue(auditPerformanceRecordRuleDetail.getRowguid());
                detailrulemodel.add(item);   
            }
        }
        return detailrulemodel;
    }
    
    public AuditPerformanceDetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceDetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceDetail dataBean) {
        this.dataBean = dataBean;
    }
    
    public AuditPerformanceRecord getRecord() {
        return record;
    }

    public void setRecord(AuditPerformanceRecord record) {
        this.record = record;
    }
    
    public String getRecorddetailrulerowguid() {
        return recorddetailrulerowguid;
    }

    public void setRecorddetailrulerowguid(String recorddetailrulerowguid) {
        this.recorddetailrulerowguid = recorddetailrulerowguid;
    }
}
