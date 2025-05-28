package com.epoint.auditperformance.auditperformanceaccount.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditperformance.auditperformanceaccount.api.IAuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformanceaccount.domain.AuditPerformanceAccount;
import com.epoint.basic.auditperformance.auditperformancerecord.api.IAuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecord.domain.AuditPerformanceRecord;
import com.epoint.basic.auditperformance.auditperformancerecordobject.api.IAuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordobject.domain.AuditPerformanceRecordObject;
import com.epoint.basic.auditperformance.auditperformancerecordrule.domain.AuditPerformanceRecordRule;
import com.epoint.basic.auditperformance.auditperformancerecordrule.inter.IAuditPerformanceRecordRuleService;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
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
@RestController("auditperformanceaccountlistaction")
@Scope("request")
public class AuditPerformanceAccountListAction extends BaseController
{
    private static final long serialVersionUID = -4198366793666025364L;

    @Autowired
    private IAuditPerformanceAccount service;

    @Autowired
    private IAuditPerformanceRecord recordservice;

    @Autowired
    private IAuditPerformanceRecordRuleService recordruleservice;

    @Autowired
    private IAuditPerformanceRecordObject recordobjectservice;

    @Autowired
    private IAuditPerformanceRecordRuleDetailService recordruledetailservice;

    /**
     * 考评细则结算实体对象
     */
    private AuditPerformanceAccount dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> modelrecord;

    private String recordrulename = null;

    private String recorddetailrulename = null;

    private String objectname = null;

    private AuditPerformanceRecord record;

    private List<String> listfield;

    private List<Map<String, Object>> columnList;

    private ExportModel exportModel;

    public static final String RECORD_GD = "3";

    @Override
    public void pageLoad() {
        String recordrowguid = this.getRequestParameter("guid");
        if (StringUtil.isNotBlank(recordrowguid)) {
            record = recordservice.find(recordrowguid).getResult();
            if (record == null) {
                addCallbackParam("msg", "没有该考评记录");
            }
        }
        else {
            addCallbackParam("msg", "没有该考评记录");
        }
        if (StringUtil.isNotBlank(record.getRowguid())) {
            addCallbackParam("status", record.getStatus());
            addCallbackParam("accountdate", record.getAccountdate());
            addCallbackParam("name", record.getRecordname());
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
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(objectname)) {
                        sql.like("objectname", objectname);
                    }
                    sql.eq("recordrowguid", record.getRowguid());
                    PageData<AuditPerformanceRecordObject> listobject = recordobjectservice
                            .getAuditPerformanceRecordObjectPageData(sql.getMap(), "objectname,objectguid", 0, 0,
                                    sortField, sortOrder)
                            .getResult();
                    Record recordrow = null;
                    int i = 0;
                    for (AuditPerformanceRecordObject auditPerformanceRecordObject : listobject.getList()) {
                        recordrow = new Record();
                        recordrow.set("objectname", auditPerformanceRecordObject.getObjectname());
                        recordrow.set("seq", ++i);
                        // 查询规则总分
                        List<AuditPerformanceAccount> listgroup = service.getAccountGroupByObjectguidAndRecordguid(
                                record.getRowguid(), auditPerformanceRecordObject.getObjectguid(),
                                "Recordrulerowguid,Objectguid,recordrowguid").getResult();
                        // 计算记录总分
                        Integer sumrule = 0;
                        Integer rulescore = 0;
                        List<AuditPerformanceRecordRule> rulelist = getRuleColumn();
                        List<AuditPerformanceRecordRule> delrulelist = new ArrayList<AuditPerformanceRecordRule>();
                        for (AuditPerformanceAccount auditPerformanceAccount : listgroup) {
                            rulescore = auditPerformanceAccount.getInt("rulescore");
                            recordrow.set(auditPerformanceAccount.getRecordrulerowguid(), rulescore);
                            // 获取已录入的分数的规则
                            for (AuditPerformanceRecordRule auditPerformanceRecordRule : rulelist) {
                                if (auditPerformanceRecordRule.getRowguid()
                                        .equals(auditPerformanceAccount.getRecordrulerowguid())) {
                                    delrulelist.add(auditPerformanceRecordRule);
                                }
                            }
                            sumrule += rulescore;
                        }
                        // 去除已录入分数规则，给没录入分数的规则赋值0
                        rulelist.removeAll(delrulelist);
                        for (AuditPerformanceRecordRule auditPerformanceRecordRule : rulelist) {
                            recordrow.set(auditPerformanceRecordRule.getRowguid(), 0);
                        }
                        recordrow.set("objectguid", auditPerformanceRecordObject.getObjectguid());
                        recordrow.set("sumrule", sumrule);
                        list.add(recordrow);
                    }
                    this.setRowCount(listobject.getRowCount());
                    return list;
                }
            };
        }
        return modelrecord;
    }

    /**
     * 绘制动态列
     */
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

    private List<AuditPerformanceRecordRule> getRuleColumn() {
        List<AuditPerformanceRecordRule> rulelist = recordruleservice
                .findFiledByRecordRowguid("rulename,rowguid,rulescore", record.getRowguid()).getResult();
        // 去除没有细则的规则
        List<AuditPerformanceRecordRule> delrulelist = new ArrayList<AuditPerformanceRecordRule>();
        for (AuditPerformanceRecordRule auditPerformanceRecordRule : rulelist) {
            int num = recordruledetailservice
                    .findNumByRecordRowguidAndRuleguid(record.getRowguid(), auditPerformanceRecordRule.getRowguid())
                    .getResult();
            if (num <= 0) {
                delrulelist.add(auditPerformanceRecordRule);
            }
        }
        rulelist.removeAll(delrulelist);
        return rulelist;
    }

    private void addColumn(List<Map<String, Object>> columnList) {
        List<AuditPerformanceRecordRule> rulelist = getRuleColumn();
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
            fieldMap.put("width", Integer.parseInt("850") / rulelist.size());
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

    public ExportModel getExportModel() {
        List<String> objectname = new ArrayList<String>();
        List<String> field = new ArrayList<String>();
        for (Map<String, Object> map : columnList) {
            if (StringUtil.isNotBlank((String) map.get("header"))) {
                objectname.add((String) map.get("header"));
                if (StringUtil.isBlank((String) map.get("field"))) {
                    field.add("seq");
                }
                else {
                    field.add((String) map.get("field"));
                }
            }
        }
        if (exportModel == null) {
            exportModel = new ExportModel(StringUtils.join(field, ","), StringUtils.join(objectname, ","));
            exportModel.addColumnWidth("objectname", 10000);
        }
        return exportModel;
    }

    public void doAccount() {
        service.doAccount(record.getRowguid());
        record.setAccountdate(new Date());
        recordservice.update(record);
    }

    public void recordEnd() {
        record.setStatus(RECORD_GD);
        record.setAccountdate(new Date());
        recordservice.update(record);
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

    public String getRecordrulename() {
        return recordrulename;
    }

    public void setRecordrulename(String recordrulename) {
        this.recordrulename = recordrulename;
    }

    public String getRecorddetailrulename() {
        return recorddetailrulename;
    }

    public void setRecorddetailrulename(String recorddetailrulename) {
        this.recorddetailrulename = recorddetailrulename;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }

}
