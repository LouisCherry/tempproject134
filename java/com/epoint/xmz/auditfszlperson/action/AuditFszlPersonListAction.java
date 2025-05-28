package com.epoint.xmz.auditfszlperson.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;


/**
 * 反射工作人员表list页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:38]
 */
@RestController("auditfszlpersonlistaction")
@Scope("request")
public class AuditFszlPersonListAction extends BaseController {
    @Autowired
    private IAuditFszlPersonService service;

    /**
     * 反射工作人员表实体对象
     */
    private AuditFszlPerson dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditFszlPerson> model;


    public void pageLoad() {
        addCallbackParam("fszlGuid", getRequestParameter("fszlGuid"));
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

    public DataGridModel<AuditFszlPerson> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditFszlPerson>() {

                @Override
                public List<AuditFszlPerson> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("fszlguid", getRequestParameter("fszlGuid"));
                    String conditionSql = new SqlHelper().getPatchSql(sqlc.getMap());

                    List<AuditFszlPerson> list = service.findList(
                            ListGenerator.generateSql("audit_fszl_person", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countAuditFszlPerson(ListGenerator.generateSql("audit_fszl_person", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public AuditFszlPerson getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszlPerson();
        }
        return dataBean;
    }

    public void setDataBean(AuditFszlPerson dataBean) {
        this.dataBean = dataBean;
    }


}
