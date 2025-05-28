package com.epoint.auditproject.auditdoc.action;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;
import com.epoint.auditproject.auditdoc.service.IAuditProjectDocsnapHistroyService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.docs.domain.AuditTaskDoc;
import com.epoint.basic.audittask.docs.inter.IAuditTaskDoc;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 办件文书list页面对应的后台
 *
 */
@RestController("auditprojectdocsnaphistorylistaction")
@Scope("request")
public class AuditProjectDocsnapHistoryListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private IAuditProjectDocsnapHistroyService iAuditProjectDocsnapHistroyService ;
    
   

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectDocsnapHistroy> model;
    
    @Autowired
    private IAttachService iAttachService;

    /**
     * 办件guid
     */
    private String projectguid;

    /**
     * 文书类型
     */
    private String doctype;

    @Override
    public void pageLoad() {
        projectguid = this.getRequestParameter("projectguid");
        doctype = this.getRequestParameter("doctype");
    }
    

    public DataGridModel<AuditProjectDocsnapHistroy> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectDocsnapHistroy>()
            {

                @Override
                public List<AuditProjectDocsnapHistroy> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();                   
                    sql.eq("projectguid",projectguid);
                    sql.eq("doctype",doctype);
                    PageData<AuditProjectDocsnapHistroy> pageData = iAuditProjectDocsnapHistroyService.getListPage(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public String getProjectguid() {
        return projectguid;
    }

    public void setProjectguid(String projectguid) {
        this.projectguid = projectguid;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }
}
