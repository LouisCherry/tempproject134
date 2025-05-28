package com.epoint.auditspitem.action;

import com.epoint.auditspitem.api.IAuditSpItemService;
import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目信息表list页面对应的后台
 *
 * @author lzhming
 * @version [版本号, 2023-03-17 09:30:48]
 */
@RestController("auditspzditemlistaction")
@Scope("request")
public class AuditSpzdItemListAction extends BaseController {
    @Autowired
    private IAuditSpItemService service;

    /**
     * 项目信息表实体对象
     */
    private AuditSpItem dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpItem> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    private String project;

    private String key;
    private String areacode;
    private String searchareacode;
    private String role;

    public void pageLoad() {
        role = getRequestParameter("role");
        areacode = getRequestParameter("areacode");
        //判断是否领导，去掉新增记录按钮，删除按钮以及编辑按钮，同时去掉不展示的列
        if ("leader".equals(role)) {
            addCallbackParam("role", role);
        }
    }

    /**
     * 删除选定
     */
    public void deleteSelect(String rowguids) {
        if (StringUtil.isNotBlank(rowguids)) {
            String[] rowguid = rowguids.split(",");
            for (String sel : rowguid) {
                service.deleteByGuid(sel);
            }
            addCallbackParam("msg", "成功删除！");
        } else {
            addCallbackParam("msg", "未勾选记录！");
        }

    }

    public void tuisong(String rowguids) {

    }

    public DataGridModel<AuditSpItem> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpItem>() {

                @Override
                public List<AuditSpItem> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(key)) {
                        sql.like("itemname", key);
                    }
                    if (StringUtil.isNotBlank(areacode) && !"all".equals(areacode)) {
                        sql.eq("jurisdictions", areacode);
                    } else if (StringUtil.isNotBlank(searchareacode) && !searchareacode.contains("all")) {
                        String jurisdictions = "";
                        if (searchareacode.contains(",")) {
                            String[] areacodes = searchareacode.split(",");
                            for (int a = 0; a < areacodes.length; a++) {
                                if (StringUtil.isNotBlank(jurisdictions)) {
                                    jurisdictions += ",'" + areacodes[a] + "'";
                                } else {
                                    jurisdictions += "'" + areacodes[a] + "'";
                                }
                            }
                            sql.in("jurisdictions", jurisdictions);
                        } else {
                            sql.eq("jurisdictions", searchareacode);
                        }
                    }
                    if (StringUtil.isNotBlank(project) && !"all".equals(project)) {
                        sql.like("itemlevel", project);
                    }
                    sql.eq("sfzditem", "1");
                    sql.setOrderDesc("operateDate");
                    PageData<AuditSpItem> pageData = service.getAuditSpItemList(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    if (pageData != null) {
                        for (AuditSpItem auditSpItem : pageData.getList()) {
                            if ("1".equals(auditSpItem.getStr("sfzditem"))){
                                String[] itemlevel = auditSpItem.getStr("itemlevel").split(",");
                                auditSpItem.put("itemlevel", itemlevel);
                            }
                            String[] projecttype = auditSpItem.getStr("projecttype").split(",");
                            auditSpItem.put("projecttype", projecttype);
                            if (StringUtil.isNotBlank(auditSpItem.getStr("biguid"))) {
                                auditSpItem.put("blsplife", "<a  href='javascript:void(0)' onclick='openLifeDetail(\""
                                        + auditSpItem.getStr("biguid") + "\")'>项目进度</a>");
                            } else {
                                auditSpItem.put("blsplife", "--");
                            }
                            if (StringUtil.isNotBlank(role)) {
                                auditSpItem.put("role", role);
                            } else {
                                auditSpItem.put("role", "role");
                            }
                        }
                        int count = pageData.getRowCount();
                        this.setRowCount(count);
                        return pageData.getList();
                    } else {
                        return null;
                    }

                }

            }

            ;
        }
        return model;
    }

    public AuditSpItem getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpItem();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpItem dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSearchareacode() {
        return searchareacode;
    }

    public void setSearchareacode(String searchareacode) {
        this.searchareacode = searchareacode;
    }
}
