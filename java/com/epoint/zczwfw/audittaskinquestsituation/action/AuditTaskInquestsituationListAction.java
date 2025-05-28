package com.epoint.zczwfw.audittaskinquestsituation.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

/**
 * 勘验事项情形表list页面对应的后台
 * 
 * @author yrchan
 * @version [版本号, 2022-04-18 09:53:53]
 */
@RestController("audittaskinquestsituationlistaction")
@Scope("request")
public class AuditTaskInquestsituationListAction extends BaseController
{
    private static final long serialVersionUID = -4176046593983069028L;

    @Autowired
    private IAuditTaskInquestsituationService service;

    /**
     * 勘验事项情形表实体对象
     */
    private AuditTaskInquestsituation dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskInquestsituation> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditTaskInquestsituation> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskInquestsituation>()
            {

                private static final long serialVersionUID = 8539285991947953041L;

                @Override
                public List<AuditTaskInquestsituation> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    String taskGuid = getRequestParameter("taskGuid");
                    if (StringUtil.isNotBlank(taskGuid)) {
                        conditionSql += " and taskguid =  '" + taskGuid + "' ";
                    }

                    List<AuditTaskInquestsituation> list = service.findList(ListGenerator
                            .generateSql("audit_task_inquestsituation", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countAuditTaskInquestsituation(
                            ListGenerator.generateSql("audit_task_inquestsituation", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditTaskInquestsituation getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskInquestsituation();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskInquestsituation dataBean) {
        this.dataBean = dataBean;
    }

}
