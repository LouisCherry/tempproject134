package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已发起的许可变更意见action
 *
 * @version [版本号, 2018年10月8日]
 * @作者 shibin
 */
@RestController("auditprojectyifaqilistaction")
@Scope("request")
public class AuditProjectYiFaQiListAction extends BaseController {

    private static final long serialVersionUID = -4046499456177644472L;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectPermissionChange> model = null;

    /**
     * 督办时间
     */
    private String supervisedateStart;
    private String supervisedateEnd;
    private String searchProjectname;
    private String isAll = "1";

    private String isMyMonitor;

    private String monitorPerson;

    private List<SelectItem> alerttypeModel = null;

    private String alertType;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Override
    public void pageLoad() {
        isMyMonitor = getRequestParameter("isMyMonitor");

    }

    public DataGridModel<AuditProjectPermissionChange> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectPermissionChange>() {

                @Override
                public List<AuditProjectPermissionChange> fetchData(int first, int pageSize, String sortField,
                                                                    String sortOrder) {

                    List<AuditProjectPermissionChange> list = new ArrayList<>();

                    String areaCode = ZwfwUserSession.getInstance().getAreaCode();
                    int num = 0;
                    //String sql = "";
                    //String sqlNum = "";
                    // 中心审管角色，显示所有的。部门审管角色，显示本部门的
                    if (iRoleService.isExistUserRoleName(userSession.getUserGuid(), "中心审管")) {
                        /*sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
                        sqlNum = "SELECT COUNT(a.themeguid) from (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ? ) a ";
                        list = dao.findList(sql, AuditProjectPermissionChange.class, areaCode, first, pageSize);
                        num = dao.queryInt(sqlNum, areaCode);*/

                        list = jnAuditJianGuanService.getAuditProjectPermission2(areaCode, first, pageSize);
                        num = jnAuditJianGuanService.getAuditProjectPermissionNum2(areaCode);
                    } else {
                        String ouGuid = userSession.getOuGuid();
                        /*sql = "select * from audit_project_permissionchange b INNER JOIN (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ?) a ON b.themeguid = a.themeguid where b.ouguid = ? GROUP BY b.themeguid ORDER BY b.replydate DESC limit ?,? ";
                        sqlNum = "SELECT COUNT(a.themeguid) from (SELECT DISTINCT themeguid from audit_project_permissionchange p INNER JOIN frame_ou_extendinfo f ON p.ouguid = f.OUGUID WHERE f.AREACODE = ? and p.ouguid = ?) a ";
                        list = dao.findList(sql, AuditProjectPermissionChange.class, areaCode, ouGuid, first, pageSize);
                        num = dao.queryInt(sqlNum, areaCode, ouGuid);*/

                        list = jnAuditJianGuanService.getAuditProjectPermissionByOuguid2(areaCode, ouGuid, first,
                                pageSize);
                        num = jnAuditJianGuanService.getAuditProjectPermissionNumByOuguid2(areaCode, ouGuid);
                    }

                    //dao.close();
                    this.setRowCount(num);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 判断交流状态
     *
     * @param rowguid
     */
    public void judgeSign(String rowguid) {
        Boolean flag = jnAuditJianGuanService.judgeSign(rowguid);

        String handleurl = "";
        if (!flag) {
            handleurl = jnAuditJianGuanService.getHandleUrlByRowguid(rowguid);
        }
        addCallbackParam("msg", flag);
        addCallbackParam("rowguid", rowguid);
        addCallbackParam("handleurl", handleurl);
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAlerttypeModel() {
        if (this.alerttypeModel == null) {
            this.alerttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "反馈状态", (String) null, true));
        }

        return this.alerttypeModel;
    }

    public String getSupervisedateStart() {
        return supervisedateStart;
    }

    public void setSupervisedateStart(String supervisedateStart) {
        this.supervisedateStart = supervisedateStart;
    }

    public String getSupervisedateEnd() {
        return supervisedateEnd;
    }

    public void setSupervisedateEnd(String supervisedateEnd) {
        this.supervisedateEnd = supervisedateEnd;
    }

    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
    }

    public String getMonitorPerson() {
        return monitorPerson;
    }

    public void setMonitorPerson(String monitorPerson) {
        this.monitorPerson = monitorPerson;
    }

    public String getSearchProjectname() {
        return searchProjectname;
    }

    public void setSearchProjectname(String searchProjectname) {
        this.searchProjectname = searchProjectname;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getIsMyMonitor() {
        return isMyMonitor;
    }

    public void setIsMyMonitor(String isMyMonitor) {
        this.isMyMonitor = isMyMonitor;
    }

}
