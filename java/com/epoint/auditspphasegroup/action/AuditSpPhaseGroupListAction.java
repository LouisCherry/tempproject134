package com.epoint.auditspphasegroup.action;

import cn.hutool.core.lang.UUID;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.auditspphasetask.api.IAuditSpPhaseTaskService;
import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前四阶段分组配置表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:01:54]
 */
@RestController("auditspphasegrouplistaction")
@Scope("request")
public class AuditSpPhaseGroupListAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseGroupService service;

    @Autowired
    private IAuditSpPhaseTaskService iAuditSpPhaseTaskService;

    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    @Autowired
    private IAuditSpRelationTaskService iAuditSpRelationTaskService;
    /**
     * 前四阶段分组配置表实体对象
     */
    private AuditSpPhaseGroup dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpPhaseGroup> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

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

    /**
     * 确定选择
     * [一句话功能简述]
     * 
     * @return
     */
    public void ackSelect() {
        String groupguid = getRequestParameter("guid");
        if (StringUtil.isNotBlank(groupguid)) {
            // 分组信息
            AuditSpPhaseGroup auditSpPhaseGroup = service.find(groupguid);
            if (auditSpPhaseGroup != null) {
                List<String> select = getDataGridData().getSelectKeys();
                if (select != null) {
                    // 阶段分组信息
                    for (String sel : select) {
                        AuditSpPhaseGroup find = service.find(sel);
                        if (find != null) {
                            AuditSpRelationTask auditSpRelationTask = new AuditSpRelationTask();
                            auditSpRelationTask.setRowguid(UUID.randomUUID().toString());
                            auditSpRelationTask.setPhasegroupguid(groupguid);
                            auditSpRelationTask.setGroupguid(find.getRowguid());
                            auditSpRelationTask.setBigordernum(Integer.valueOf(find.getBigordernum()));
                            // 阶段基本信息
                            AuditSpPhaseBaseinfo auditSpPhaseBaseinfo = iAuditSpPhaseBaseinfoService
                                    .find(find.getPhaseguid());
                            if (auditSpPhaseBaseinfo != null) {
                                auditSpRelationTask.setPhaseguid(auditSpPhaseBaseinfo.getRowguid());
                                auditSpRelationTask.setPhasename(auditSpPhaseBaseinfo.getPhasename());
                            }
                            iAuditSpRelationTaskService.insert(auditSpRelationTask);

                        }
                    }
                    addCallbackParam("msg", "成功关联！");
                }
                else {
                    addCallbackParam("msg", "未选择分组！");
                }

            }
            else {
                addCallbackParam("msg", "未查询到分组信息！");
            }
        }
    }

    public DataGridModel<AuditSpPhaseGroup> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpPhaseGroup>()
            {

                @Override
                public List<AuditSpPhaseGroup> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.nq("rowguid", getRequestParameter("guid"));
                    PageData<AuditSpPhaseGroup> dbListByPage = service.getDbListByPage(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    this.setRowCount(dbListByPage.getRowCount());
                    return dbListByPage.getList();
                }

            };
        }
        return model;
    }

    public AuditSpPhaseGroup getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPhaseGroup();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseGroup dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
