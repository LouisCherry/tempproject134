package com.epoint.jiningzwfw.auditsptasktype.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeRService;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeService;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktype;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktypeR;
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
 * 事项分类list页面对应的后台
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 10:56:21]
 */
@RestController("auditsptasktypelistaction")
@Scope("request")
public class AuditSpTasktypeListAction extends BaseController {
    @Autowired
    private IAuditSpTasktypeService service;

    @Autowired
    private IAuditSpTasktypeRService iAuditSpTasktypeRService;

    /**
     * 事项分类实体对象
     */
    private AuditSpTasktype dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpTasktype> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 所属阶段下拉列表model
     */
    private List<SelectItem> phaseidModel = null;


    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<AuditSpTasktype> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpTasktype>() {

                @Override
                public List<AuditSpTasktype> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);

                    if (StringUtil.isNotBlank(dataBean.getTasktypename())) {
                        conditionSql += " and tasktypename like '" + dataBean.getTasktypename() + "' ";
                    }
                    if (StringUtil.isNotBlank(dataBean.getPhaseid())) {
                        conditionSql += " and phaseid = '" + dataBean.getPhaseid() + "' ";
                    }
                    List<AuditSpTasktype> list = service.findList(
                            ListGenerator.generateSql("audit_sp_tasktype", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());


                    for (AuditSpTasktype auditSpTasktype : list) {
                        StringBuilder sb = new StringBuilder();
                        SqlConditionUtil conditionUtil = new SqlConditionUtil();
                        conditionUtil.eq("tasktypeguid", auditSpTasktype.getRowguid());
                        List<AuditSpTasktypeR> rList = iAuditSpTasktypeRService.findListByCondition(conditionUtil.getMap());
                        int i = 0;
                        for (AuditSpTasktypeR auditSpTasktypeR : rList) {
                            sb.append("(").append(auditSpTasktypeR.getXiaquname()).append(")").append(auditSpTasktypeR.getTaskname()).append("<br/>");
                            i++;
                            if (i >= 10) {
                                break;
                            }
                        }
                        if(sb.length()>5){
                            auditSpTasktype.set("chooseTaskname", sb.substring(0, sb.length()-5));
                        }
                    }

                    int count = service.countAuditSpTasktype(ListGenerator.generateSql("audit_sp_tasktype", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public AuditSpTasktype getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpTasktype();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpTasktype dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getPhaseidModel() {
        if (phaseidModel == null) {
            phaseidModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批阶段", null, true));
        }
        return this.phaseidModel;
    }

}
