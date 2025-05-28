package com.epoint.auditperformance.auditperformancerecord.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

@RestController("auditperformancerecordlistcopyaction")
@Scope("request")
public class AuditPerformanceRecordListCopyAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 表格控件model
     */
    @Autowired
    private IAuditPerformanceRecord service;

    @Autowired
    private IAuditPerformanceRecordObject recordObjectService;

    @Autowired
    private IAuditPerformanceRecordRuleService ruleservice;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService detailService;

    private DataGridModel<AuditPerformanceRecord> model;

    /**
     * 考评对象类别下拉列表model
     */
    private List<SelectItem> centerModel = null;

    /**
     * 考评对象类别下拉列表model
     */
    private List<SelectItem> objecttypeModel = null;

    private AuditPerformanceRecord dataBean;

    private String centerguid;

    @Autowired
    private IAuditOrgaServiceCenter serviceCenter;

    @Override
    public void pageLoad() {
        dataBean = new AuditPerformanceRecord();
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())) {
            addCallbackParam("msg", "人员没有分配到中心!");
        }
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
                    for (AuditPerformanceRecord performanceRecord : pageData.getList()) {
                        AuditOrgaServiceCenter orgaServiceCenter = serviceCenter
                                .findAuditServiceCenterByGuid(performanceRecord.getCenterguid()).getResult();
                        if (orgaServiceCenter != null) {
                            performanceRecord.set("centername", orgaServiceCenter.getCentername());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    /**
     * 复制选定
     * 
     */
    public void copySelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            //复制考评
            AuditPerformanceRecord performanceRecord = service.getPerformanceRecordByGuid(sel).getResult();
            performanceRecord.setOperatedate(new Date());
            performanceRecord.setOperateusername(userSession.getDisplayName());
            performanceRecord.setRowguid(UUID.randomUUID().toString());
            performanceRecord.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            if(performanceRecord.getRecordname().length()<=196){
                
                String recordname = performanceRecord.getRecordname() + "（复制）";
                //判断是否有重名规则
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                sql.eq("recordname", recordname);
                int num = service.getNumByCondition(sql.getMap()).getResult();
                if(num>0){
                    addCallbackParam("msg", "已存在名称为"+recordname+"的记录！");
                    return;
                }
                performanceRecord.setRecordname(recordname);
            }
            performanceRecord.setIfenabled("1");
            performanceRecord.setStatus("1");
            service.insertPerformanceRecord(performanceRecord);
            //复制考评对象
            List<AuditPerformanceRecordObject> recordObjects = recordObjectService
                    .selectPerformanceRecordObjectList(sel).getResult();
            if (recordObjects != null && recordObjects.size() > 0) {
                for (AuditPerformanceRecordObject auditPerformanceRecordObject : recordObjects) {
                    auditPerformanceRecordObject.setOperatedate(new Date());
                    auditPerformanceRecordObject.setOperateusername(userSession.getDisplayName());
                    auditPerformanceRecordObject.setRowguid(UUID.randomUUID().toString());
                    auditPerformanceRecordObject.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
                    auditPerformanceRecordObject.setRecordrowguid(performanceRecord.getRowguid());
                    recordObjectService.insertPerformanceRecordObject(auditPerformanceRecordObject);
                }
            }
            //复制考评规则
            List<AuditPerformanceRecordRule> rules = ruleservice.selectRuleListByRecordGuid(sel).getResult();
            if (rules != null && rules.size() > 0) {
                for (int i = 0; i < rules.size(); i++) {
                    AuditPerformanceRecordRule rule = rules.get(i);
                    //复制考评细则
                    List<AuditPerformanceRecordRuleDetail> ruleDetails = detailService.findListByRecordRowguid(sel,
                            rule.getRowguid());
                    rule.setOperatedate(new Date());
                    rule.setOperateusername(userSession.getDisplayName());
                    rule.setRecordrowguid(performanceRecord.getRowguid());
                    rule.setRowguid(UUID.randomUUID().toString());
                    ruleservice.insert(rule);
                    if (ruleDetails != null && ruleDetails.size() > 0) {
                        for (int j = 0; j < ruleDetails.size(); j++) {
                            AuditPerformanceRecordRuleDetail ruleDetail = ruleDetails.get(j);
                            ruleDetail.setOperatedate(new Date());
                            ruleDetail.setOperateusername(userSession.getDisplayName());
                            ruleDetail.setRowguid(UUID.randomUUID().toString());
                            ruleDetail.setRecordrulerowguid(rule.getRowguid());
                            ruleDetail.setRecordrowguid(performanceRecord.getRowguid());
                            detailService.insert(ruleDetail);
                        }

                    }
                }
            }

        }
        addCallbackParam("msg", "复制成功！");
    }

    @SuppressWarnings("serial")
    public List<SelectItem> getCenterModel() {
        if (centerModel == null) {
            centerModel = new ArrayList<SelectItem>()
            {
                {
                    add(new SelectItem("", "请选择"));
                }
            };
            List<AuditOrgaServiceCenter> serviceCenters = serviceCenter.getAuditOrgaServiceCenterByCondition(null)
                    .getResult();
            if (serviceCenters != null && serviceCenters.size() > 0) {
                for (AuditOrgaServiceCenter auditOrgaServiceCenter : serviceCenters) {
                    centerModel.add(new SelectItem(auditOrgaServiceCenter.getRowguid(),
                            auditOrgaServiceCenter.getCentername()));
                }
            }
        }
        return this.centerModel;
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
        return dataBean;
    }

    public void setDataBean(AuditPerformanceRecord dataBean) {
        this.dataBean = dataBean;
    }

    public String getCenterguid() {
        return centerguid;
    }

    public void setCenterguid(String centerguid) {
        this.centerguid = centerguid;
    }
}
