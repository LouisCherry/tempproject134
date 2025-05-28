package com.epoint.auditsp.auditspbasetaskp.action;

import com.epoint.auditsp.auditspbasetaskp.domain.AuditSpBasetaskP;
import com.epoint.auditsp.auditspbasetaskp.inter.IAuditSpBasetaskP;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* 3.0新增逻辑开始 */

/**
 * 并联审批事项表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2019-04-25 10:07:31]
 */
@RestController("jnauditspbasetaskparentselectaction")
@Scope("request")
public class JnAuditSpBasetaskParentSelectAction extends BaseController {

    private static final long serialVersionUID = 7798753811284837045L;

    @Autowired
    private IAuditSpBasetask service;

    @Autowired
    private IAuditSpBasetaskR servicer;

    @Autowired
    private IAuditSpBasetaskP servicep;

    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpBasetaskP> model;

    private String taskname;
    private String taskcode;

    @Override
    public void pageLoad() {
        // TODO Auto-generated method stub

    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            servicep.delAuditSpBasetaskPByrowguid(sel);
        }
        addCallbackParam("msg", "删除成功！");
    }

    public DataGridModel<AuditSpBasetaskP> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpBasetaskP>() {

                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditSpBasetaskP> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(taskname)) {
                        sqlc.like("tasktaskname", taskname);
                    }
                    if (StringUtil.isNotBlank(taskcode)) {
                        sqlc.like("tasktaskcode", taskcode);
                    }
                    sqlc.setOrderDesc("operatedate");
                    PageData<AuditSpBasetaskP> pagedata = servicep
                            .getAuditSpBasetaskPByPage(sqlc.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();

                    for (AuditSpBasetaskP auditspbasetaskp : pagedata.getList()) {
                        auditspbasetaskp.put("phasename",
                                iCodeItemsService.getItemTextByCodeName("审批阶段", auditspbasetaskp.getPhaseid()));
                    }
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }

            };
        }
        return model;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode;
    }

    /* 3.0新增逻辑结束 */

}
