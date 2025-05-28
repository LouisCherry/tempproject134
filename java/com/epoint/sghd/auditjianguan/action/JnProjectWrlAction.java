package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.inter.IGxhAuditJianguan;
import com.epoint.sghd.auditjianguan.renlingrecord.api.IRenlingRecordService;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnRenlingService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJnRenling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 部门未认领list页面对应的后台
 *应付监理
 * @author shibin
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectwrlaction")
@Scope("request")
public class JnProjectWrlAction extends BaseController {

    private static final long serialVersionUID = 1L;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IRenlingRecordService iRenlingRecordService;

    @Autowired
    private IAuditTaskJnRenlingService auditTaskJnRenlingService;

    @Autowired
    private IGxhAuditJianguan gxhAuditJianguan;

    private ExportModel exportModel;

    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;
    private String flowsn;
    private String projectname;


    @Override
    public void pageLoad() {

    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 临时模块
                    Record record = new Record();
                    record.set("banjiedateStart", banjiedateStart);
                    record.set("banjiedateEnd", banjiedateEnd);
                    record.set("flowsn", flowsn);
                    record.set("projectname", projectname);
                    record.set("renlingtype", "0");
                    PageData<Record> pageData = gxhAuditJianguan.getAuditJianguanPageData(record, first, pageSize, sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
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
            exportModel = new ExportModel("flowsn,projectname,ouname,applyername,banjiedate",
                    "办件编号,事项名称,审批部门,申请人,办结时间");
        }
        return exportModel;
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

    public String getFlowsn() {
        return flowsn;
    }

    public void setFlowsn(String flowsn) {
        this.flowsn = flowsn;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    /**
     * 认领
     *
     * @param idss
     */
    public void renling(String idss) {
        RenlingService rlservice = new RenlingService();
        String[] ids = idss.split(",");
        StringBuilder strbu = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            strbu.append("'" + ids[i] + "',");
        }
        try {
            rlservice.renling1(strbu.deleteCharAt(strbu.length() - 1).toString());
            addCallbackParam("message", "认领成功");
            ids = idss.split(",");
            // 新增认领记录表
            for (int i = 0; i < ids.length; i++) {
                RenlingRecord renlingRecord = new RenlingRecord();
                renlingRecord.setRowguid(UUID.randomUUID().toString());
                renlingRecord.setProjectguid(ids[i]);
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("rowguid", ids[i]);
                List<AuditProject> auditProjects = auditProjectService
                        .getAuditProjectListByCondition("projectname,rowguid,task_id", sql.getMap()).getResult();
                for (AuditProject auditProject : auditProjects) {
                    renlingRecord.setProjectname(auditProject.getProjectname());
                }
                renlingRecord.setRenlingtime(new Date());
                renlingRecord.setOuguid(UserSession.getInstance().getUserGuid());
                renlingRecord.setOuname(UserSession.getInstance().getOuName());
                renlingRecord.setUserguid(UserSession.getInstance().getUserGuid());
                renlingRecord.setUsername(UserSession.getInstance().getDisplayName());
                iRenlingRecordService.insert(renlingRecord);

                if (auditProjects != null && !auditProjects.isEmpty()) {
                    AuditProject auditProject = auditProjects.get(0);
                    AuditTaskJnRenling auditTaskJnRenling = new AuditTaskJnRenling();
                    auditTaskJnRenling.setOperateusername(userSession.getDisplayName());
                    auditTaskJnRenling.setOperatedate(new Date());
                    auditTaskJnRenling.setRowguid(UUID.randomUUID().toString());
                    auditTaskJnRenling.setTask_id(auditProject.getTask_id());
                    auditTaskJnRenling.setProjectguid(auditProject.getRowguid());
                    auditTaskJnRenling.setRenlingdate(new Date());
                    auditTaskJnRenling.setRenling_ouguid(userSession.getOuGuid());
                    auditTaskJnRenling.setRenling_ouname(userSession.getOuName());
                    auditTaskJnRenling.setRenling_userguid(userSession.getUserGuid());
                    auditTaskJnRenling.setRenling_username(userSession.getDisplayName());
                    auditTaskJnRenlingService.insert(auditTaskJnRenling);
                }
            }
        } catch (Exception e) {
            addCallbackParam("message", "认领失败");
        }
    }

}
