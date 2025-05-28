
package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditprojectmonitorresult.domain.AuditProjectMonitorResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起许可变更意见详情action
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnauditprojectbiangengdetailaction")
@Scope("request")
public class JnAuditProjectBianGengDetailAction extends BaseController {
    private static final long serialVersionUID = 181286499037869484L;

    private String rowguid = "";

    private String userguid = "";
    private String senduserguid = "";
    private String isreply = "";

    private AuditProjectPermissionChange dataBean = null;

    private AuditProjectMonitorResult result = null;

    private String response = "未认定";

    private String monitorOpinion = "";
    private String banjieOpinion = "";

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectPermissionChange> model;

    String themeGuid = "";

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Override
    public void pageLoad() {

        rowguid = getRequestParameter("rowguid");
        //String sqlTheme = "select themeguid from audit_project_permissionchange where rowguid = ? ";
        //String sql = "select * from audit_project_permissionchange where themeguid = ? ORDER BY replydate ASC LIMIT 0,1 ";
        //themeGuid = dao.queryString(sqlTheme, rowguid);

        themeGuid = jnAuditJianGuanService.getThemeguidFromAuditPP(rowguid);

        //dataBean = dao.find(sql, AuditProjectPermissionChange.class, themeGuid);
        dataBean = jnAuditJianGuanService.getAuditPPByThemeguidOrder(themeGuid);

        //String sql2 = "SELECT fileclientguid from audit_project_permissionchange WHERE themeguid = ? ";
        List<Record> list = new ArrayList<>();

        //list = dao.findList(sql2, Record.class, themeGuid);
        list = jnAuditJianGuanService.getFileclientguidByThemeguid(themeGuid);

        List<FrameAttachInfo> frameAttachInfoAllList = new ArrayList<>();
        List<FrameAttachInfo> frameAttachInfoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            //String sqlAttach = "SELECT * from frame_attachinfo WHERE CLIENGGUID = ? ";
            //frameAttachInfoList = dao.findList(sqlAttach, FrameAttachInfo.class, list.get(i).getStr("fileclientguid"));
            frameAttachInfoList = jnAuditJianGuanService
                    .getAllFrameAttachInfoByCliengGuid(list.get(i).getStr("fileclientguid"));
            frameAttachInfoAllList.addAll(frameAttachInfoList);
        }

        addCallbackParam("list", frameAttachInfoAllList);

        //dao.close();

    }

    public DataGridModel<AuditProjectPermissionChange> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectPermissionChange>() {

                @Override
                public List<AuditProjectPermissionChange> fetchData(int first, int pageSize, String sortField,
                                                                    String sortOrder) {
                    //String sql = "select * from audit_project_permissionchange WHERE themeGuid = ? ";
                    //dao.findList(sql, AuditProjectPermissionChange.class, themeGuid);
                    /*List<AuditProjectPermissionChange> monitorReplies = dao.findList(sql,
                            AuditProjectPermissionChange.class, themeGuid);*/
                    List<AuditProjectPermissionChange> monitorReplies = jnAuditJianGuanService
                            .getAuditPPByThemeguid(themeGuid);

                    this.setRowCount(monitorReplies != null ? monitorReplies.size() : 0);
                    //dao.close();
                    return monitorReplies;
                }
            };
        }
        return model;
    }

    public AuditProjectPermissionChange getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectPermissionChange dataBean) {
        this.dataBean = dataBean;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AuditProjectMonitorResult getResult() {
        return result;
    }

    public void setResult(AuditProjectMonitorResult result) {
        this.result = result;
    }

    public String getMonitorOpinion() {
        return monitorOpinion;
    }

    public void setMonitorOpinion(String monitorOpinion) {
        this.monitorOpinion = monitorOpinion;
    }

    public String getBanjieOpinion() {
        return banjieOpinion;
    }

    public void setBanjieOpinion(String banjieOpinion) {
        this.banjieOpinion = banjieOpinion;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }

    public String getSenduserguid() {
        return senduserguid;
    }

    public void setSenduserguid(String senduserguid) {
        this.senduserguid = senduserguid;
    }

    public String getIsreply() {
        return isreply;
    }

    public void setIsreply(String isreply) {
        this.isreply = isreply;
    }
}
