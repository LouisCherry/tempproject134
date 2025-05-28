package com.epoint.auditperformance.auditperformancerecord.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评记录list页面对应的后台
 * 
 * @author 14408
 * @version [版本号, 2018-01-09 16:33:20]
 */
@RestController("auditperformancerecordlistaction")
@Scope("request")
public class AuditPerformanceRecordListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditPerformanceRecord service;

    @Autowired
    private IAuditPerformanceRecordObject objservice;

    @Autowired
    private IAuditPerformanceRecordRuleService recordRuleService;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService recordRuleDetailService;
    /**
     * 考评记录实体对象
     */
    private AuditPerformanceRecord dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditPerformanceRecord> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 考评对象类别下拉列表model
     */
    private List<SelectItem> objecttypeModel = null;

    
    @Override
    public void pageLoad() {
        dataBean = new AuditPerformanceRecord();
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())) {
            addCallbackParam("msg", "人员没有分配到中心!");
        }
    }

    /**
     * 更改事项开启状态
     * @throws InterruptedException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void changeAuditTaskStatus(String taskGuid) throws InterruptedException {

        dataBean = service.find(taskGuid).getResult();
        if ("1".equals(dataBean.getIfenabled())) {
            dataBean.setIfenabled("0");
        }
        else {
            dataBean.setIfenabled("1");
        }
        service.update(dataBean);
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        String usingrecordname ="";
        for (String sel : select) {
            AuditPerformanceRecord selectedrecord = service.find(sel).getResult();
            //system.out.println(selectedrecord.getStatus());
            if("1".equals(selectedrecord.getStatus())){
                recordRuleDetailService.deleteRecordRuleDetailByRecordGuid(sel);
                recordRuleService.deleteRecordRuleByRecordGuid(sel, null);
                objservice.delFieldByRecordRowguid(sel);
                service.deleteByGuid(sel);   
            }else if("2".equals(selectedrecord.getStatus())) {
                usingrecordname +=selectedrecord.getRecordname()+";";
            }
        }
        if("".equals(usingrecordname)){
            addCallbackParam("msg","删除成功！");
        }else {
            addCallbackParam("msg", "草稿已删除,无法删除在用记录！（"+usingrecordname+"）"); 
        }

    }

    /**
     * 验证对象和细则
     * 
     */
    public void validate(String rowguid) {
        String msg = "";
        //system.out.println(rowguid);
        if (StringUtil.isNotBlank(rowguid)) {
            //system.out.println(1111111);
            if (objservice.findFieldByRecordRowguid("objectname,objectguid", rowguid).getResult().size() <= 0) {
                msg = "nullobj";
            }
            else if (recordRuleDetailService.findListByRecordRowguid(rowguid).size() <= 0) {
                msg = "nulldetail";
            }
        }
        addCallbackParam("msg", msg);
    }

    /**
     * 开启考评
     * 
     */
    public void start(String rowguid) {
        dataBean = service.find(rowguid).getResult();
        dataBean.setStatus("2");
        service.update(dataBean);
        addCallbackParam("msg", "开启成功！");
    }

    public DataGridModel<AuditPerformanceRecord> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceRecord>()
            {

                @Override
                public List<AuditPerformanceRecord> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    sql.in("Status", "1,2");
                    sql.setOrderAsc("Status");
                    sql.setOrderDesc("operatedate");
                    if (StringUtil.isNotBlank(dataBean.getRecordname())) {
                        sql.like("Recordname", dataBean.getRecordname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getObjecttype())) {
                        sql.eq("Objecttype", dataBean.getObjecttype());
                    }
                    if (StringUtil.isNotBlank(dataBean.getBegintime())) {
                        sql.ge("begintime", EpointDateUtil.getBeginOfDate(dataBean.getBegintime()));
                    }
                    if (StringUtil.isNotBlank(dataBean.getEndtime())) {
                        sql.le("endtime", EpointDateUtil.getEndOfDate(dataBean.getEndtime()));
                    }
                    PageData<AuditPerformanceRecord> pageData = service
                            .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;

    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getObjecttypeModel() {
        if (objecttypeModel == null) {
            objecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, true));
        }
        return this.objecttypeModel;
    }

    public AuditPerformanceRecord getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceRecord();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecord dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
