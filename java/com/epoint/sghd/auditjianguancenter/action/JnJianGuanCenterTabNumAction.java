
package com.epoint.sghd.auditjianguancenter.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.auditjianguan.action.RenlingService;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguancenter.api.IJnAuditJianGuanCenter;

/**
 * 中心监管页面acion
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnjianguancentertabnumaction")
@Scope("request")
public class JnJianGuanCenterTabNumAction extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = -1490905998750897938L;

    @Autowired
    private IHandleFrameOU fserver;

    @Autowired
    private IJnAuditJianGuanCenter jnAuditJianGuanCenter;

    @Override
    public void pageLoad() {
        addCallbackParam("ouname", userSession.getOuName());
        addCallbackParam("username", userSession.getDisplayName());
    }

    /**
     * tabs数据
     */
    public void refreshTabNav() {
        int spxx = getSpxxCount();
        int yrl = getYrl();
        int wrl = getWrl();
        addCallbackParam("spxxnumber", spxx);
        addCallbackParam("yrlnumber", yrl);
        addCallbackParam("wrlnumber", wrl);
        addCallbackParam("bgyjnumber", getBianGengNum());
        addCallbackParam("hdxznumber", getHdxzCount());
        addCallbackParam("zcwjnumber", getFileCount());
    }

    /**
     * 获取中心审批办件数量
     *
     * @return
     */
    public int getSpxxCount() {
        String sqlNum = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid,jg_ouname from audit_task_jn where is_hz = 1 group by task_id,jg_ouguid) a ON a.TASK_ID = p.TASK_ID where 1=1 ";

        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        sqlNum = sqlNum + " and p.handleareacode like '" + handleareacode + "%' ";
        // 如果是镇村接件
        String area;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        sqlNum = sqlNum + " and p.areaCode = '" + area + "'";

        sqlNum = sqlNum + " and p.Banjieresult = 40 and p.status = 90 ";
        // 系统参数，审管互动认领时间起始
        String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
        if (StringUtil.isNotBlank(sghdstarttime)) {
            sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
        }
        int xxcount = jnAuditJianGuanCenter.getSpxxCount(sqlNum);
        return xxcount;
    }

    /**
     * 中心 互动交流文件数量
     *
     * @return
     */
    public int getHdFileCount() {
        SqlConditionUtil sql = new SqlConditionUtil();
        String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        List<FrameOu> list = fserver.getOUListByCenterguid(centerguid).getResult();
        String ouids = "";
        if (list != null && list.size() > 0) {
            ouids = "'" + StringUtil.join(list, "','") + "'";
            sql.in("ouguid", ouids);
        }
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        int num = sqlManageUtil.getListCount(AuditTaskShareFile.class, sql.getMap());
        return num;

    }

    /**
     * 中心领导获取文件数量
     *
     * @return
     */
    public int getFileCount() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        RenlingService rservice = new RenlingService();
        return rservice.getCenterFileNum(areaCode);

    }

    /**
     * 中心许可变更意见数目
     *
     * @return
     */
    public int getBianGengNum() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        int num = 0;
        num = jnAuditJianGuanCenter.getBianGengNum(areaCode);
        return num;
    }

    /**
     * 中心领导获取互动协助数量
     *
     * @return
     */
    public int getHdxzCount() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        int num = 0;
        num = jnAuditJianGuanCenter.getHdxzCount(areaCode);
        return num;
    }

    /**
     * 中心获取已认领的办件数目
     *
     * @return
     */
    public int getYrl() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        int num = 0;
        num = jnAuditJianGuanCenter.getYrl(areaCode);
        return num;

    }

    /**
     * 中心获取未认领的办件数目
     *
     * @return
     */
    public int getWrl() {
        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        int num = 0;
        num = jnAuditJianGuanCenter.getWrl(areaCode);
        return num;
    }

}
