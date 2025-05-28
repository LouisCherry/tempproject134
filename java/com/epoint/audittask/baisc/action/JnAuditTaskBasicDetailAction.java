package com.epoint.audittask.baisc.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.CompareUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController("jnaudittaskbasicdetailaction")
@Scope("request")
public class JnAuditTaskBasicDetailAction extends BaseController {

    /**
     *
     */
    private static final long serialVersionUID = -5811009959141283016L;

    private CompareUtil compareUtil = new CompareUtil();

    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;

    @Autowired
    private IAuditTask auditTaskBasicImpl;

    private FileUploadModel9 finishproductsamplesmodel;

    @Autowired
    private IAttachService attachServiceImpl;

    /**
     * 上传附件标识
     */
    private String finishproductsamplestemp;
    /**
     * 新上传附件标识
     */
    private String finishproductsamples;

    public String getFinishproductsamples() {
        return finishproductsamples;
    }

    public void setFinishproductsamples(String finishproductsamples) {
        this.finishproductsamples = finishproductsamples;
    }

    /**
     * 代码项service
     */
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    /**
     * 事项基本信息
     */
    private AuditTask auditTask;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 当前版本guid
     */
    private String currentTaskGuid;
    /**
     * 对比版本guid
     */
    private String compareTaskGuid;

    /**
     * 办理流程附件标识
     */
    private String templateguid;

    @Override
    public void pageLoad() {
        currentTaskGuid = this.getViewData("currentTaskGuid");
        if (StringUtil.isBlank(currentTaskGuid)) {
            currentTaskGuid = this.getRequestParameter("currentGuid");
        }
        compareTaskGuid = this.getViewData("compareTaskGuid");
        if (StringUtil.isBlank(compareTaskGuid)) {
            compareTaskGuid = this.getRequestParameter("compTaskGuid");
        }
        String compJson = "";
        boolean check = false;
        Map<String, Object> delegateMap = new HashMap<String, Object>(16);
        if (StringUtil.isNotBlank(currentTaskGuid)) {
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())
                    || ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel()))) {
                auditTask = auditTaskBasicImpl
                        .getAuditTaskByGuid(currentTaskGuid, ZwfwUserSession.getInstance().getAreaCode(), false)
                        .getResult();
                AuditTaskDelegate delegate = auditTaskDelegateService
                        .findByTaskIDAndAreacode(auditTask.getTask_id(), ZwfwUserSession.getInstance().getAreaCode())
                        .getResult();
                // 合并个性化字段
                if (delegate != null) {
                    if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(delegate.getUsecurrentinfo())) {
                        check = true;

                        if (delegate.getPromise_day() != null && StringUtil.isNotBlank(delegate.getPromise_day())) {
                            delegateMap.put("promise_day", delegate.getPromise_day());
                        }
                        if (delegate.getLink_tel() != null && StringUtil.isNotBlank(delegate.getLink_tel())) {
                            delegateMap.put("link_tel", delegate.getLink_tel());
                        }
                        if (delegate.getSupervise_tel() != null && StringUtil.isNotBlank(delegate.getSupervise_tel())) {
                            delegateMap.put("supervise_tel", delegate.getSupervise_tel());
                        }
                        if (delegate.getApplyaddress() != null && StringUtil.isNotBlank(delegate.getApplyaddress())) {
                            delegateMap.put("transact_addr", delegate.getApplyaddress());
                        }
                        if (delegate.getApplytime() != null && StringUtil.isNotBlank(delegate.getApplytime())) {
                            delegateMap.put("transact_time", delegate.getApplytime());
                        }
                        if (delegate.getAcceptcondition() != null
                                && StringUtil.isNotBlank(delegate.getAcceptcondition())) {
                            delegateMap.put("acceptcondition", delegate.getAcceptcondition());
                        }
                        if (delegate.getXzorder() != null && StringUtil.isNotBlank(delegate.getXzorder())) {
                            delegateMap.put("ordernum", delegate.getXzorder());
                        }
                    }
                }
            } else {
                auditTask = auditTaskBasicImpl.getAuditTaskByGuid(currentTaskGuid, false).getResult();
            }
            auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(currentTaskGuid, false).getResult();
            if (!currentTaskGuid.equals(compareTaskGuid)) {
                AuditTask compareAuditTask = auditTaskBasicImpl.getAuditTaskByGuid(compareTaskGuid, false).getResult();
                AuditTaskExtension compareAuditTaskExtension = auditTaskExtensionImpl
                        .getTaskExtensionByTaskGuid(compareTaskGuid, false).getResult();

                Map<String, Object> mapAuditTask = compareUtil.compare2Bean(auditTask, compareAuditTask);
                Map<String, Object> mapExtension = compareUtil.compare2Bean(auditTaskExtension,
                        compareAuditTaskExtension);

                Map<String, Object> mapAll = new HashMap<String, Object>(16);
                for (Map.Entry<String, Object> map : mapAuditTask.entrySet()) {
                    if ("type".equals(map.getKey())) {
                        mapAll.put(map.getKey(),
                                codeItemsService.getItemTextByCodeName("事项类型", (String) map.getValue()));
                    } else if ("applyertype".equals(map.getKey())) {
                        mapAll.put(map.getKey(),
                                codeItemsService.getItemTextByCodeName("申请人类型", (String) map.getValue()));
                    } else if ("shenpilb".equals(map.getKey())) {
                        mapAll.put(map.getKey(),
                                codeItemsService.getItemTextByCodeName("审批类别", (String) map.getValue()));
                    } else if ("jbjmode".equals(map.getKey())) {
                        if (ZwfwConstant.JBJMODE_SIMPLE.equals(compareAuditTask.getJbjmode())) {
                            mapAll.put(map.getKey(), "简易模式（不启动流程）");
                        } else if (ZwfwConstant.JBJMODE_STANDARD.equals(compareAuditTask.getJbjmode())) {
                            mapAll.put(map.getKey(), "正常模式");
                        }
                    } else if ("allowreport".equals(map.getKey()) && compareAuditTask.getAllowreport() != null) {
                        if (ZwfwConstant.CONSTANT_INT_ZERO == compareAuditTask.getAllowreport()) {
                            mapAll.put(map.getKey(), "否");
                        } else if (ZwfwConstant.CONSTANT_INT_ONE == compareAuditTask.getAllowreport()) {
                            mapAll.put(map.getKey(), "是");
                        }
                    } else {
                        mapAll.put(map.getKey(), map.getValue());
                    }
                }
                for (Map.Entry<String, Object> map : mapExtension.entrySet()) {
                    if ("is_allowbatchregister".equals(map.getKey())) {
                        mapAll.put(map.getKey(),
                                codeItemsService.getItemTextByCodeName("是否", map.getValue().toString()));
                    } else {
                        mapAll.put(map.getKey(), map.getValue());
                    }
                }
                compJson = JsonUtil.objectToJson(mapAll);
            }
        }
        if (check) {
            compJson = JsonUtil.objectToJson(delegateMap);
            this.addCallbackParam("delegate", "delegate");
        }
        if (auditTask == null) {
            auditTask = new AuditTask();
        }
        if (auditTaskExtension == null) {
            auditTaskExtension = new AuditTaskExtension();
        }
        this.addCallbackParam("compJson", compJson);
        this.addCallbackParam("type", auditTask.getType());
        this.addCallbackParam("jbjmode", auditTask.getJbjmode());
        this.addCallbackParam("sbjmode", auditTask.getAllowreport());
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
        }
        else{
            templateguid = UUID.randomUUID().toString();
        }
        // 附件信息加载
        List<FrameAttachInfo> frameattachlist2 = attachServiceImpl.getAttachInfoListByGuid(templateguid);
        if (frameattachlist2 != null && !frameattachlist2.isEmpty()) {
            // 空白模板显示
            this.addCallbackParam("templateattachUrl", getTempUrl(frameattachlist2.get(0).getAttachGuid()));
        } else {
            this.addCallbackParam("templateattachUrl", "无附件！");
        }
        /*3.0新增逻辑结束*/

    }

    public FileUploadModel9 getFinishproductsamplesmodel() {
        if (finishproductsamplesmodel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    finishproductsamplesmodel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            finishproductsamplesmodel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(finishproductsamples,
                    null, null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return finishproductsamplesmodel;
    }

    /**
     * 对比两个版本事项
     *
     * @param currentTaskGuid
     * @param compareTaskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void compareTwoVersion(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
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

    /**
     * 附件下载地址
     *
     * @param cliengguid 业务guid
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
}
