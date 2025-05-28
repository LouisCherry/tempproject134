package com.epoint.auditproject.auditproject.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;

/**
 * 办件退回对应的后台
 * 
 * @author lzm
 * @version [版本号, 2023-03-09 11:46:07]
 */
@RestController("jnauditprojectbackaction")
@Scope("request")
public class JNAuditProjectBackAction extends BaseController
{
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditProject auditProjectService;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    private String backReason;

    @Override
    public void pageLoad() {

    }

    public void back() {

        String projectGuid = getRequestParameter("projectguid");
        if (StringUtil.isNotBlank(projectGuid)) {
            String fields = "biguid,subappguid,rowguid,areacode,status";
            AuditProject auditProject = auditProjectService
                    .getAuditProjectByRowGuid(fields, projectGuid, ZwfwUserSession.getInstance().getAreaCode())
                    .getResult();
            if (auditProject != null) {
                auditProject.setStatus(101);// 退回状态
                auditProject.set("backreason", backReason);// 退回原因

                if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid())
                            .getResult();
                    if (auditSpISubapp != null) {
                        auditSpISubapp.setStatus("26");
                        iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                    }
                }

                // 更新一件事办件iAuditOnlineProject
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditProject.getBiguid())
                            .getResult();
                    if (auditSpInstance != null) {
                        AuditOnlineProject auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(
                                auditProject.getBiguid(), auditSpInstance.getApplyerguid()).getResult();
                        if (auditOnlineProject != null) {
                            auditOnlineProject.setStatus("101");
                            iAuditOnlineProject.updateProject(auditOnlineProject);
                        }
                    }
                }

                auditProjectService.updateProject(auditProject);
                addCallbackParam("msg", "退回成功！");
            }
        }
    }

    public String getBackReason() {
        return backReason;
    }

    public void setBackReason(String backReason) {
        this.backReason = backReason;
    }

}
