package com.epoint.auditperformance.auditperformanceaccount.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 考评细则结算list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-12 09:17:06]
 */
@RestController("auditperformanceobjectaccountaction")
@Scope("request")
public class AuditPerformanceObjectAccountAction extends BaseController
{
    private static final long serialVersionUID = -4198366793666025364L;

    @Autowired
    private IAuditPerformanceAccount service;

    @Autowired
    private IAuditPerformanceRecord recordservice;

    @Autowired
    private IAuditPerformanceRecordRuleService recordruleservice;
    
    @Autowired
    private IAuditPerformanceRecordRuleService iAuditPerformanceRecordRuleService;

    /**
     * 考评细则结算实体对象
     */
    private AuditPerformanceAccount dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> modelrecord;

    private AuditPerformanceRecord record;

    private List<String> listfield;

    private List<Map<String, Object>> columnList;

    private String objectguid;

    private String objectname;
    
    private DataGridModel<AuditPerformanceAccount> model;

    @Override
    public void pageLoad() {
        String recordrowguid = this.getRequestParameter("guid");
        objectguid = this.getRequestParameter("objectguid");
        objectname = this.getRequestParameter("objectname");
        addCallbackParam("objectname",objectname);
        if (StringUtil.isNotBlank(recordrowguid)) {
            record = recordservice.find(recordrowguid).getResult();
            if (record == null) {
                addCallbackParam("msg", "没有该考评记录");
            }
        }
        else {
            addCallbackParam("msg", "没有该考评记录");
        }
        initDynamicColumn();

    }

    public DataGridModel<Record> getTableData() {
        if (modelrecord == null) {
            modelrecord = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> list = new ArrayList<Record>();
                    Record recordrow = null;
                    AuditPerformanceRecordObject auditPerformanceRecordObject = new AuditPerformanceRecordObject();
                    auditPerformanceRecordObject.setObjectguid(objectguid);
                    auditPerformanceRecordObject.setObjectname(objectname);
                    recordrow = new Record();
                    recordrow.set("objectname", auditPerformanceRecordObject.getObjectname());
                    //查询个规则的总分
                    List<AuditPerformanceAccount> listgroup = service.getAccountGroupByObjectguidAndRecordguid(
                            record.getRowguid(), auditPerformanceRecordObject.getObjectguid(),
                            "Recordrulerowguid,Objectguid,recordrowguid").getResult();
                    Integer sumrule = 0;
                    Integer rulescore = 0;
                    for (AuditPerformanceAccount auditPerformanceAccount : listgroup) {
                        rulescore = auditPerformanceAccount.getInt("rulescore");
                        recordrow.set(auditPerformanceAccount.getRecordrulerowguid(), rulescore);
                        sumrule += rulescore;
                    }
                    recordrow.set("objectguid", auditPerformanceRecordObject.getObjectguid());
                    recordrow.set("sumrule", sumrule);
                    list.add(recordrow);
                    this.setRowCount(ZwfwConstant.CONSTANT_INT_ONE);
                    return list;
                }
            };
        }
        return modelrecord;
    }

    private void initDynamicColumn() {
        // 返回给前台用json格式
        JSONObject rtnJson = new JSONObject();
        columnList = new ArrayList<Map<String, Object>>();
        addColumn(columnList);
        try {
            rtnJson.put("columns", columnList);
            addCallbackParam("columns", rtnJson.toString());
            addCallbackParam("columnsnum", columnList.size());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addColumn(List<Map<String, Object>> columnList) {
        List<AuditPerformanceRecordRule> rulelist = recordruleservice
                .findFiledByRecordRowguid("rulename,rowguid,rulescore", record.getRowguid()).getResult();
        Map<String, Object> idMap = new HashMap<String, Object>(16);
        idMap.put("type", "indexcolumn");// 设置特殊列
        idMap.put("width", "50");
        idMap.put("headerAlign", "center");
        idMap.put("align", "center");
        idMap.put("header", "序");
        columnList.add(idMap);
        Map<String, Object> objectMap = new HashMap<String, Object>(16);
        objectMap.put("width", "150");
        objectMap.put("headerAlign", "center");
        objectMap.put("align", "center");
        objectMap.put("header", "对象名称");
        objectMap.put("field", "objectname");
        objectMap.put("name", "objectcolumn");
        columnList.add(objectMap);
        listfield = new ArrayList<String>();
        // 设置普通字段列1s
        Map<String, Object> fieldMap;
        for (AuditPerformanceRecordRule auditPerformanceRecordRule : rulelist) {
            fieldMap = new HashMap<String, Object>(16);
            fieldMap.put("field", auditPerformanceRecordRule.getRowguid());
            listfield.add(auditPerformanceRecordRule.getRowguid());
            fieldMap.put("width", Integer.parseInt("850")/rulelist.size());
            fieldMap.put("headerAlign", "center");
            fieldMap.put("align", "center");
            fieldMap.put("name", "rulecolumn");
            fieldMap.put("header",
                    auditPerformanceRecordRule.getRulename() + "(" + auditPerformanceRecordRule.getRulescore() + "分)");
            columnList.add(fieldMap);
        }
        Map<String, Object> sumrule = new HashMap<String, Object>(16);
        sumrule.put("width", "100");
        sumrule.put("headerAlign", "center");
        sumrule.put("align", "center");
        sumrule.put("header", "总分");
        sumrule.put("field", "sumrule");
        columnList.add(sumrule);

    }
    
    

    public DataGridModel<AuditPerformanceAccount> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditPerformanceAccount>()
            {
                @Override
                public List<AuditPerformanceAccount> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("Objectguid", objectguid);
                    sql.eq("recordrowguid",record.getRowguid());
                    sql.setOrderAsc("recordrulename");
                    PageData<AuditPerformanceAccount> pagedata = service
                            .getPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    for (AuditPerformanceAccount auditPerformanceAccount : pagedata.getList()) {
                        String Recordrulerowguid = auditPerformanceAccount.getRecordrulerowguid();

                        AuditPerformanceRecordRule auditPerformanceRecordRule = iAuditPerformanceRecordRuleService
                                .find(Recordrulerowguid);

                        if (auditPerformanceRecordRule != null) {
                            auditPerformanceAccount.put("recordrulename",auditPerformanceAccount.getRecordrulename()+"(" +auditPerformanceRecordRule.getRulescore()+")");
                            
                        }
                        auditPerformanceAccount.put("endtime", record.getEndtime());
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }
            };
        }
        return model;
    }
    
    

    public AuditPerformanceAccount getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditPerformanceAccount();
        }
        return dataBean;
    }

    public void setDataBean(AuditPerformanceAccount dataBean) {
        this.dataBean = dataBean;
    }


    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

}
