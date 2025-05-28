package com.epoint.audittask.baisc.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 事项基本信息详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-20 14:25:25]
 */
@RestController("jnaudittaskbasiccomparedetailaction")
@Scope("request")
public class JnAuditTaskBasicCompareDetailAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -2878466647412278106L;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    @Autowired
    private IAttachService attachServiceImpl;

    /**
     * 事项基本信息
     */
    private AuditTask auditTask = null;
    /**
     * 上传附件标识
     */
    private String finishproductsamplestemp;
    /**
     * 新上传附件标识
     */
    private String finishproductsamples;

    /**
     * 办理流程附件标识
     */
    private String templateguid;

    /**
     * 事项扩展基本信息
     */
    private AuditTaskExtension auditTaskExtension = null;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("taskGuid");
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())
                || ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            addCallbackParam("xz", "true");
        }
        if (StringUtil.isNotBlank(guid)) {
            auditTask = auditTaskBasicImpl.getAuditTaskByGuid(guid, false).getResult();
            auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(guid, false).getResult();
            // auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(guid,
            // false);
        } else {
            auditTask = new AuditTask();
            auditTaskExtension = new AuditTaskExtension();
        }
        addCallbackParam("type", auditTask.getType());
        addCallbackParam("jbjmode", auditTask.getJbjmode());
        addCallbackParam("sbjmode", auditTask.getAllowreport());
        if (StringUtil.isNotBlank(auditTaskExtension.getStr("is_tssgs"))
                && "true".equals(auditTaskExtension.getStr("is_tssgs"))) {
            auditTaskExtension.set("is_tssgs", "是");
        } else {
            auditTaskExtension.set("is_tssgs", "否");
        }
        finishproductsamplestemp = auditTaskExtension.getFinishproductsamples();
        // 原先是否存空白模板
        if (StringUtils.isNoneBlank(finishproductsamplestemp)) {
            finishproductsamples = finishproductsamplestemp;
        } else {
            finishproductsamples = UUID.randomUUID().toString();
        }
        // 附件信息加载
        List<FrameAttachInfo> frameattachlist = attachServiceImpl.getAttachInfoListByGuid(finishproductsamples);
        if (frameattachlist != null && !frameattachlist.isEmpty()) {
            // 空白模板显示
            this.addCallbackParam("finishproductsamples", getTempUrl(frameattachlist.get(0).getAttachGuid()));
        } else {
            this.addCallbackParam("finishproductsamples", "无附件！");
        }

        /*3.0新增逻辑*/
        if (("1").equals(auditTask.get("is_ggtask"))) {
            addCallbackParam("is_ggtask", "true");
        }
        // 原先是否存在
        if (StringUtils.isNotBlank(auditTask.get("handle_flow"))) {
            templateguid = auditTask.get("handle_flow");
        } else {
            templateguid = UUID.randomUUID().toString();
        }
        // 附件信息加载
        List<FrameAttachInfo> frameattachlist1 = attachServiceImpl.getAttachInfoListByGuid(templateguid);
        if (frameattachlist1 != null && !frameattachlist1.isEmpty()) {
            // 空白模板显示
            this.addCallbackParam("templateattachUrl", getTempUrl(frameattachlist1.get(0).getAttachGuid()));
        } else {
            this.addCallbackParam("templateattachUrl", "无附件！");
        }
        /*3.0新增逻辑结束*/

    }

    /**
     * 附件下载地址
     *
     * @param attachguid 业务guid
     */
    public String getTempUrl(String attachguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(attachguid)) {
            // FrameAttachInfo frameAttachInfo =
            // attachServiceImpl.getAttachInfoDetail(cliengguid);
            FrameAttachInfo frameAttachInfo = attachServiceImpl.getAttachInfoDetail(attachguid);
            if (frameAttachInfo != null && StringUtils.isNoneBlank(frameAttachInfo.getAttachFileName())) {
                String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" " + strURL
                        + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;";
            } else {
                wsmbName = "无附件！";
            }
        } else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }

    public AuditTask getAuditTask() {
        return auditTask;
    }

    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    public AuditTaskExtension getAuditTaskExtension() {
        return auditTaskExtension;
    }

    public void setAuditTaskExtension(AuditTaskExtension auditTaskExtension) {
        this.auditTaskExtension = auditTaskExtension;
    }

    public String getFinishproductsamples() {
        return finishproductsamples;
    }

    public void setFinishproductsamples(String finishproductsamples) {
        this.finishproductsamples = finishproductsamples;
    }

}
