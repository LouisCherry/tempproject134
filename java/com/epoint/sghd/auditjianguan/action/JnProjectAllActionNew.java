package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.sghd.auditjianguan.impl.SgytSpxxProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门监管展示列表
 *应付监理-原版
 * @author swy
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectallactionnew")
@Scope("request")
public class JnProjectAllActionNew extends BaseController {

    private static final long serialVersionUID = 1L;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    /**
     * 事项基本信息实体对象
     */
    private AuditTask audittask = new AuditTask();

    /**
     * 办结类型
     */
    private List<SelectItem> banjieTypeModel = null;

    /**
     * 业务来源
     */
    private List<SelectItem> resourceModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private SgytSpxxProjectService sgytSpxxProjectService;

    /**
     * 办结时间
     */
    private String banjiedateStart;

    private String banjiedateEnd;

    private ExportModel exportModel;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 部门监管，默认只能查看当前登录用户所属部门的数据
                    dataBean.set("ouguid", userSession.getOuGuid());
                    dataBean.set("jg_userguid", userSession.getUserGuid());
                    PageData<AuditProject> pageData = sgytSpxxProjectService.findProjectVOPage(first, pageSize,
                            dataBean);
                    setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    /**
     * 办结类型
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getBanjieTypeModel() {
        if (banjieTypeModel == null) {
            banjieTypeModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "办结类型", null, true));
        }
        return this.banjieTypeModel;
    }

    /**
     * 业务来源
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getResourceModel() {
        if (resourceModel == null) {
            resourceModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "业务来源", null, true));
        }
        return this.resourceModel;
    }

    public void hasProGuid(String rowguid) {
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "flowsn,projectname,ouname,applyername,banjiedate,renlingtype,renlingdate,renling_username",
                    "办件编号,事项名称,审批部门,申请人,办结时间,认领状态,认领时间,认领人");
        }
        return exportModel;
    }

    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }

    public String getBanjiedateStart() {
        return banjiedateStart;
    }

    public void setBanjiedateStart(String banjiedateStart) {
        this.banjiedateStart = banjiedateStart;
    }

    public String getBanjiedateEnd() {
        return banjiedateEnd;
    }

    public void setBanjiedateEnd(String banjiedateEnd) {
        this.banjiedateEnd = banjiedateEnd;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }
}
