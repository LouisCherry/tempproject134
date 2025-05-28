package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.sghd.auditjianguan.inter.IGxhAuditJianguan;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门监管页面对应的后台
 * 应付监理
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnjianguantabnumactionold")
@Scope("request")
public class JnJianGuanTabNumActionOld extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -1490905998750897938L;

    private String windowguid;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Autowired
    private IGxhAuditJianguan gxhAuditJianguan;

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
        int spxx = gxhAuditJianguan.getSpxxCount();
        int yrl = gxhAuditJianguan.getYrlCount();
        int wrl = gxhAuditJianguan.getWrlCount();
        addCallbackParam("spxxnumber", spxx);
        addCallbackParam("yrlnumber", yrl);
        addCallbackParam("wrlnumber", wrl);
        addCallbackParam("hdxznumber", getjgcount());
        addCallbackParam("zcwjnumber", getfilecount());
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
