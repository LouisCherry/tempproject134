package com.epoint.xmz.thirdreporteddata.auditqypgtask.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.IAuditQypgTaskService;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.entity.AuditQypgTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 区域评估事项表list页面对应的后台
 *
 * @author ysai
 * @version [版本号, 2023-11-02 14:37:06]
 */
@RestController("auditqypgtasklistaction")
@Scope("request")
public class AuditQypgTaskListAction extends BaseController {
    @Autowired
    private IAuditQypgTaskService service;

    /**
     * 区域评估事项表实体对象
     */
    private AuditQypgTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditQypgTask> model;


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
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditQypgTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditQypgTask>() {

                @Override
                public List<AuditQypgTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    PageData<AuditQypgTask> pageData = service.getPageData(sql.getMap(), first, pageSize, sortField,
                            sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public void saveAll(String json) {
        List<AuditQypgTask> list = JsonUtil.jsonToList(json, AuditQypgTask.class);
        Date d = new Date();
        for (AuditQypgTask auditQypgTask : list) {
            auditQypgTask.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
            if (StringUtil.isNotBlank(auditQypgTask.getRowguid())) {
                service.update(auditQypgTask);
            } else {
                auditQypgTask.setRowguid(UUID.randomUUID().toString());
                auditQypgTask.setCreatedate(d);
                service.insert(auditQypgTask);
            }
        }
    }

    public AuditQypgTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQypgTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditQypgTask dataBean) {
        this.dataBean = dataBean;
    }

}
