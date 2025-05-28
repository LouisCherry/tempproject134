package com.epoint.auditspphasetask.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择事项页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:06:22]
 */
@RestController("selecttasklistaction")
@Scope("request")
public class SelectTaskListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 事项表实体对象
     */
    private AuditTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;

    public void pageLoad() {
    }

    public DataGridModel<AuditTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    sql.eq("taskname", "出版物印刷企业未按规定承印内部资料的处罚");
                    AuditTask auditTask = new AuditTask();
                    auditTask.setRowguid("000086955FB04CB28409E47AA41BE166");
                    auditTask.setTask_id("190d37bc-65cb-446c-a75d-0033403658f9");
                    auditTask.setOuguid("04831a67-e40b-4f12-8f47-fd47e21fc252");
                    auditTask.setOuname("北庄村村民委员会");
                    auditTask.setTaskname("残疾人辅助器具适配补贴");
log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day()+",user:"+userSession.getUserGuid());
                    auditTask.setPromise_day(2);
                            log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day()+",user:"+userSession.getUserGuid());

                    List<AuditTask> list = new ArrayList<>();
                    list.add(auditTask);
                    /*
                     * PageData<AuditTask> pageData = iAuditTask
                     * .getAuditEnableTaskPageData(
                     * "rowguid,task_id,taskname,ouguid,ouname,promise_day",
                     * sql.getMap(), first, pageSize, sortField, sortOrder)
                     * .getResult();
                     * this.setRowCount(pageData.getRowCount());
                     */
                    /* return pageData.getList(); */

                    return list;

                }

            };
        }
        return model;
    }

    public AuditTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }

}
