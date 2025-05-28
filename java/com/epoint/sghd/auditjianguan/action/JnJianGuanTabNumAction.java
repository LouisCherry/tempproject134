package com.epoint.sghd.auditjianguan.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;

/**
 * 部门监管页面对应的后台
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnjianguantabnumaction")
@Scope("request")
public class JnJianGuanTabNumAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -1490905998750897938L;

    private String windowguid;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        addCallbackParam("ouname", userSession.getOuName());
        addCallbackParam("username", userSession.getDisplayName());
    }

    /**
     * tabs数据
     */
    public void refreshTabNav() {
        int spxx = getSpxxCount();
        int yrl = getYrlCount();
        int wrl = getWrlCount();
        addCallbackParam("spxxnumber", spxx);
        addCallbackParam("yrlnumber", yrl);
        addCallbackParam("wrlnumber", wrl);
        addCallbackParam("hdxznumber", getjgcount());
        addCallbackParam("zcwjnumber", getfilecount());
    }

    /**
     * 部门获取已认领数量
     *
     * @return
     */
    public int getYrlCount() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        String ouGuid = userSession.getOuGuid();
        int num = 0;
        num = jnAuditJianGuanService.getTaJianGuanTabYrlCount(areaCode, ouGuid, userSession.getUserGuid());
        return num;
    }

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    public int getWrlCount() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        String ouGuid = userSession.getOuGuid();
        int num = 0;
        num = jnAuditJianGuanService.getTaJianGuanTabWrlCount(areaCode, ouGuid, userSession.getUserGuid());
        return num;
    }

    /**
     * 部门获取审批信息数量
     *
     * @return
     */
    public int getSpxxCount() {
        String ouGuid = userSession.getOuGuid();
        String sqlNum = "select count(1) from (select DISTINCT p.flowsn from audit_project p INNER JOIN " +
                "(select * from audit_task_jn where jg_ouguid = '" + ouGuid + "' and jg_userguid = '" + userSession.getUserGuid() + "' ) a " +
                " ON a.TASK_ID = p.TASK_ID and a.is_hz = 1 where 1=1 ";
        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        sqlNum = sqlNum + " and p.handleareacode like '%" + handleareacode + "%' ";
        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        sqlNum = sqlNum + " and p.areaCode = " + area + " ";
        sqlNum = sqlNum + " and p.Banjieresult = '40' and p.status = 90 ";

        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }

        sqlNum += " ) c ";

        int yrlNum = jnAuditJianGuanService.getTaJianGuanTabSpxxCount(sqlNum);
        return yrlNum;
    }

    /**
     * 部门获取互动协助的数量
     *
     * @return
     */
    public int getjgcount() {
        String ouGuid = userSession.getOuGuid();
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        int num;
        num = jnAuditJianGuanService.getTaJianGuanTabjgcount(ouGuid, areaCode);
        return num;
    }

    /**
     * 部门获取政策文件数量
     *
     * @return
     */
    public int getfilecount() {
        String ouguid = userSession.getBaseOUGuid();
        RenlingService rservice = new RenlingService();
        return rservice.getShareFileNum(ouguid);
    }

    public String getWindowguid() {
        return windowguid;
    }

    public void setWindowguid(String windowguid) {
        this.windowguid = windowguid;
    }
}
