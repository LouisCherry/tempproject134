package com.epoint.composite.auditsp.handlecontrol.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.handlecontrol.domain.AuditTaskHandleControl;
import com.epoint.basic.audittask.handlecontrol.inter.IAuditTaskHandleControl;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handlecontrol.inter.IHandleControl;
import com.epoint.composite.auditsp.service.HandleAuditProjectService;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.xmz.auditprojectdelivery.api.IAuditProjectDeliveryService;
import com.epoint.xmz.auditprojectdelivery.api.entity.AuditProjectDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class HandleControlImpl implements IHandleControl {
    @Override
    public AuditCommonResult<JSONObject> initHandleControl(String projectGuid, String taskGuid, String workItemGuid,
                                                           String centerGuid, String areaCode, String userGuid, String windowguid) {
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,is_charge,charge_when,is_check,is_fee,is_pause,is_delay,if_express,is_cert,banjieresult,applyway,spendtime,certnum,certrowguid,applyertype";

        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                .getResult();
        return this.initHandleControl(auditProject, taskGuid, workItemGuid, centerGuid, areaCode, userGuid, windowguid);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public AuditCommonResult<JSONObject> initHandleControl(AuditProject auditProject, String taskGuid,
                                                           String workItemGuid, String centerGuid, String areaCode, String userGuid, String windowguid) {
        AuditCommonResult<JSONObject> commonResult = new AuditCommonResult<JSONObject>();
        HashMap<String, HashMap> buttonCrol = this.initHandleControls(auditProject, taskGuid, workItemGuid, centerGuid,
                areaCode, userGuid, windowguid);
        if (buttonCrol != null) {
            HashMap<String, String> btnMap = buttonCrol.get("btnMap");
            HashMap<String, String> lbl = buttonCrol.get("lbl");
            /**
             * 右侧办件状态信息
             */
            String lblstatus = lbl.get("lblstatus");// 办件状态
            String lbljs = lbl.get("lbljs");// 计时
            String lblfs = lbl.get("lblfs");// 申报方式
            String lblshyu = lbl.get("lblshyu");// 计时超时或正常
            String lblshyut = lbl.get("lblshyut");// 计时剩余时间
            String handlecontrols_btnlst = lbl.get("handlecontrols_btnlst");
            String zzurl = lbl.get("zzurl");
            String iscertificate = lbl.get("iscertificate");
            String lblresult = lbl.get("lblresult");
            // 计算完成，开始获取操作按钮
            IAuditTaskHandleControl handlecontrolService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskHandleControl.class);
            List<AuditTaskHandleControl> listControl = handlecontrolService
                    .selectAuditTaskHandleControlByAreaCode(areaCode).getResult();
            // 排除不需要的按钮，剩下的就是需要展示的
            listControl.removeIf(control -> {
                String btnid = control.getControlid();
                String isDisplay = btnMap.get(btnid);
                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(isDisplay)) {
                    return true;
                } else {
                    return false;
                }
            });
            // 这里需要判断一下时候需要修改发证的按钮
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate)
                    && ZwfwConstant.CONSTANT_STR_ONE.equals(btnMap.get("btnResult"))) {
                for (AuditTaskHandleControl taskHandleControl : listControl) {
                    if (taskHandleControl.getControlid().toString().equals("btnResult")) {
                        taskHandleControl.setControlname(lblresult);
                        if (StringUtil.isNotBlank(zzurl)) {
                            taskHandleControl.setOpenwindowurl(zzurl);
                        }
                        break;
                    }
                }
            }
            JSONObject rtnInfo = new JSONObject();
            JSONObject rtnInfolb = new JSONObject();
            try {
                rtnInfolb.put("lblstatus", lblstatus);
                rtnInfolb.put("lbljs", lbljs);
                rtnInfolb.put("lblfs", lblfs);
                rtnInfolb.put("lblshyu", lblshyu);
                rtnInfolb.put("lblshyut", lblshyut);
                rtnInfo.put("lbText", rtnInfolb);
                rtnInfo.put("controls", listControl);
                rtnInfo.put("handlecontrols_btnlst", handlecontrols_btnlst);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            commonResult.setResult(rtnInfo);
            return commonResult;
        } else {
            commonResult.setBusinessFail("未找到对应的事项信息");
            return commonResult;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public HashMap<String, HashMap> initHandleControls(AuditProject auditProject, String taskGuid, String workItemGuid,
                                                       String centerGuid, String areaCode, String userGuid, String windowguid) {
        /**
         * 事项实例化
         */
        IAuditTask auditTaskBasic = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

        IAuditTaskResult auditResultService = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskExtension.class);
        IAuditZnsbEquipment equipmentservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditZnsbEquipment.class);

        IAuditProjectUnusual iauditProjectUnusual  = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        HandleAuditProjectService handleAuditProjectService = new HandleAuditProjectService();

        AuditTask audittask = auditTaskBasic.getAuditTaskByGuid(taskGuid, true).getResult();
        /**
         * 右侧办件状态信息
         */
        String lblstatus;// 办件状态
        String lbljs;// 计时
        String lblfs;// 申报方式
        String lblshyu = "";// 计时超时或正常
        String lblshyut = "";// 计时剩余时间
        String lblresult = "";
        String zzurl = "";
        String handlecontrols_btnlst;
        String IS_USE_EvaluatePAD = "0";

        // 判断该窗口是否配置了评价器pad
        String Macaddress = equipmentservice
                .getMacaddressbyWindowGuidAndType(windowguid, QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();
        if (StringUtil.isNotBlank(Macaddress)) {
            IS_USE_EvaluatePAD = "1";
        }

        HashMap<String, String> btnMap = new HashMap<String, String>(16);
        if (audittask != null) {
            btnMap.put("btnQueRen", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnReceive", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnAccept", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnYstg", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnYsdh", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnPricing", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnReceipt", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnReject", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnApprove", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnNotApprove", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnSave", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnEvaluate", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnResult", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnhjkd", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btndhtx", ZwfwConstant.CONSTANT_STR_ZERO);//新增单号填写按钮
            btnMap.put("txtBank", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("txtDelay", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("txtReport", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("btnFinish", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("txtDeptHandle", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("faqikanyan", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("fukan", ZwfwConstant.CONSTANT_STR_ZERO);
            btnMap.put("kanyan", ZwfwConstant.CONSTANT_STR_ZERO);
            // btnMap.put("btnhjzwzd", ZwfwConstant.CONSTANT_STR_ZERO);//呼叫政务专递
            // btnMap.put("btnhjclkd", ZwfwConstant.CONSTANT_STR_ZERO);//呼叫材料快递
            /*
             * AuditProject auditProject =
             * auditProjectService.getAuditProjectByRowGuid(fields, projectGuid,
             * areaCode) .getResult();
             */
            AuditTaskResult auditresult = auditResultService.getAuditResultByTaskGuid(taskGuid, true).getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskGuid, true)
                    .getResult();

            String iscertificate = ZwfwConstant.CONSTANT_STR_ZERO;// 是否需要发证
            handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;
            // 1、首次创建流程，办件尚未创建
            // 1.1、根据系统参数AS_ISNEED_RECEIVE 判断是否需要显示接件
            IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
            String isneedreceive = handleConfig.getFrameConfig("AS_ISNEED_RECEIVE", centerGuid).getResult();
            if (auditProject == null) {
                // btnStandardOP = ZwfwConstant.CONSTANT_STR_ONE;
                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(isneedreceive)) {
                    // btnMap.replace("btnReceive", value) =
                    // ZwfwConstant.CONSTANT_STR_ZERO;// 不显示接件按钮
                    lblstatus = "未受理";// 办件状态：未受理
                } else {
                    btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ONE);// 显示接件按钮
                    lblstatus = "未接件";// 办件状态：未接件
                }
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(IS_USE_EvaluatePAD)) {
                    btnMap.replace("btnQueRen", ZwfwConstant.CONSTANT_STR_ONE);// 显示申请人确认按钮
                }
                btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存按钮
                handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;// 不显示工作流按钮
                lblfs = "窗口登记";// 申报方式
                lbljs = "未开始计时";// 计时显示内容

                // 模拟办件不需要出现接件、保存按钮
                if (auditTaskExtension.getIs_simulation() != null && ZwfwConstant.CONSTANT_STR_ONE
                        .equals(String.valueOf(auditTaskExtension.getIs_simulation()))) {
                    btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示接件
                    btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示保存
                    if (audittask.getCharge_flag() != null
                            && ZwfwConstant.CONSTANT_STR_ONE
                            .equals(String.valueOf(audittask.getCharge_flag().toString()))
                            && ZwfwConstant.CHARGE_WHEN_SLQ
                            .equals(String.valueOf(auditTaskExtension.getCharge_when().toString()))) {
                        btnMap.replace("btnPricing", ZwfwConstant.CONSTANT_STR_ONE);// 显示核价
                    } else {
                        btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 受理按钮显示
                    }
                }
            }
            // 2、办件已经创建
            else {
                // 2.1根据审批结果配置控制显示打证按钮

                // 事项配置审批结果
                if (auditresult != null) {
                    int resulttype = auditresult.getResulttype();
                    switch (resulttype) {
                        case ZwfwConstant.LHSP_RESULTTYPE_ZZ:// 证照
                            lblresult = "上传证照";
                            iscertificate = ZwfwConstant.CONSTANT_STR_ONE;
                            if (StringUtil.isNotBlank(auditresult.getSharematerialguid())) {
                                ICertConfigExternal iCertConfigExternal = ContainerFactory.getContainInfo()
                                        .getComponent(ICertConfigExternal.class);
                                CertCatalog certCatalog = null;
                                if (auditresult.getSharematerialguid().contains(",")) {
                                    certCatalog = iCertConfigExternal.getCatalogByCatalogid(
                                            auditresult.getSharematerialguid().split(",")[0],
                                            auditProject.getAreacode());
                                } else {
                                    certCatalog = iCertConfigExternal.getCatalogByCatalogid(
                                            auditresult.getSharematerialguid(), auditProject.getAreacode());
                                }

                                // 共享材料存在且处理地址为空
                                if (certCatalog != null && StringUtil.isBlank(auditresult.getResultpageurl())) {
                                    String certrowguid = "";
                                    if (StringUtil.isNotBlank(auditProject.getCertrowguid())) {
                                        certrowguid = auditProject.getCertrowguid();
                                    }
                                    zzurl = "epointzwfw/auditbusiness/auditresource/cert/certinfoadd?guid="
                                            + auditresult.getSharematerialguid() + "&projectguid="
                                            + auditProject.getRowguid() + "&certrowguid=" + certrowguid + "&taskguid="
                                            + taskGuid + "&certownertype=" + auditProject.getApplyertype() + "&ischeck";

                                }
                            }
                            break;
                        case ZwfwConstant.LHSP_RESULTTYPE_PW:// 批文
                            lblresult = "上传批文";
                            iscertificate = ZwfwConstant.CONSTANT_STR_ONE;
                            break;
                        case ZwfwConstant.LHSP_RESULTTYPE_WS:// 文书
                            lblresult = "上传文书";
                            iscertificate = ZwfwConstant.CONSTANT_STR_ONE;
                            break;
                        case ZwfwConstant.LHSP_RESULTTYPE_SQBGZ:// 申请表盖章
                            lblresult = "上传申请表";
                            iscertificate = ZwfwConstant.CONSTANT_STR_ONE;
                            break;
                        default:
                            break;
                    }
                }

                String chargeflag = auditProject.getIs_charge() == null ? null : auditProject.getIs_charge().toString();// 是否收费
                String chargewhen = auditProject.getCharge_when() == null ? null
                        : auditProject.getCharge_when().toString();// 何时收费
                Integer ischeck = auditProject.getIs_check() == null ? ZwfwConstant.CONSTANT_INT_ZERO
                        : auditProject.getIs_check();// 办件是否核价
                Integer isfee = auditProject.getIs_fee() == null ? ZwfwConstant.CONSTANT_INT_ZERO
                        : auditProject.getIs_fee();// 办件是否缴费
                Integer ispause = auditProject.getIs_pause() == null ? ZwfwConstant.CONSTANT_INT_ZERO
                        : auditProject.getIs_pause();// 办件是否暂停
                Integer iscert = auditProject.getIs_cert() == null ? ZwfwConstant.CONSTANT_INT_ZERO
                        : auditProject.getIs_cert();// 办件是否已发证
                Integer isdelay = auditProject.getIs_delay();// 办件是否延期
                String ifexpress = StringUtil.isBlank(auditProject.getIf_express()) ? ZwfwConstant.CONSTANT_STR_ZERO
                        : auditProject.getIf_express();// 办件是否使用呼叫快递
                String ifexpress_ma = StringUtil.isBlank(auditProject.getIf_express_ma())
                        ? ZwfwConstant.CONSTANT_STR_ZERO
                        : auditProject.getIf_express_ma();// 办件是否使用申报材料呼叫快递
                // 2.2当前活动应用程序配置是否配置为banjie
                int isbanjiestep = ZwfwConstant.CONSTANT_INT_ZERO;

                // 非正常模式即办件或自建系统
                int iszijianxitong = auditTaskExtension.getIszijianxitong() == null ? 0
                        : auditTaskExtension.getIszijianxitong();
                if (((ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                        && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())) || iszijianxitong == 1)
                        && ZwfwConstant.BANJIAN_STATUS_SPTG == auditProject.getStatus()) {
                    isbanjiestep = Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE);
                } else {
                    if (StringUtil.isNotBlank(workItemGuid)) {
                        IWFInstanceAPI9 iwfinstance = ContainerFactory.getContainInfo()
                                .getComponent(IWFInstanceAPI9.class);
                        ProcessVersionInstance pvi = iwfinstance.getProcessVersionInstance(auditProject.getPviguid());
                        WorkflowWorkItem workflowWorkItem = iwfinstance.getWorkItem(pvi, workItemGuid);
                        IWorkflowActivityService iworkService = ContainerFactory.getContainInfo()
                                .getComponent(IWorkflowActivityService.class);
                        if (workflowWorkItem != null) {
                            WorkflowActivity workflowActivity = iworkService
                                    .getByActivityGuid(workflowWorkItem.getActivityGuid());
                            if (workflowActivity != null) {
                                if ("banjie".equals(workflowActivity.getApplicationConfig())) {
                                    isbanjiestep = Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE);// 办结活动
                                }
                            }
                        }
                    }
                }

                // 2.3根据办件状态显示按钮
                int status = auditProject.getStatus() == null ? ZwfwConstant.BANJIAN_STATUS_DJJ
                        : auditProject.getStatus();
                String feemode = handleConfig.getFrameConfig("AS_FEEMODE", centerGuid).getResult();
                // 2.3.1尚未受理，不用考虑计时
                if (status < ZwfwConstant.BANJIAN_STATUS_YSL) {
                    // btnStandardOP = ZwfwConstant.CONSTANT_STR_ONE;
                    switch (status) {
                        case ZwfwConstant.BANJIAN_STATUS_WWYTJ:// 外网申报已提交
                            btnMap.replace("btnYstg", ZwfwConstant.CONSTANT_STR_ONE);// 显示预审通过
                            btnMap.replace("btnYsdh", ZwfwConstant.CONSTANT_STR_ONE);// 显示预审打回
                            break;
                        case ZwfwConstant.BANJIAN_STATUS_WWYSTG:// 外网申报预审通过
                        case ZwfwConstant.BANJIAN_STATUS_INIT:// 初始化
                        case ZwfwConstant.BANJIAN_STATUS_YDJ:// 预登记
                        case ZwfwConstant.BANJIAN_STATUS_DJJ:// 待接件
                            btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(IS_USE_EvaluatePAD)) {
                                btnMap.replace("btnQueRen", ZwfwConstant.CONSTANT_STR_ONE);// 显示申请人确认按钮
                            }
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(isneedreceive)) {
                                btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示接件
                                // 需要受理前收费
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(chargeflag)
                                        && ZwfwConstant.CHARGE_WHEN_SLQ.equals(chargewhen)) {
                                    // 如果尚未核价，显示保存、核价、补正、不予受理
                                    if (ischeck == ZwfwConstant.CONSTANT_INT_ZERO) {
                                        btnMap.replace("btnPricing", ZwfwConstant.CONSTANT_STR_ONE);// 显示核价
                                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                        btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                    }
                                    // 已经核价
                                    else {
                                        // 如果尚未收费，显示保存、收讫
                                        if (isfee == ZwfwConstant.CONSTANT_INT_ZERO) {
                                            // 银行收费模式
                                            if ("WB".equals(feemode)) {
                                                btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示收讫
                                                btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ONE);// 显示银行收费提示信息
                                            } else {
                                                btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ONE);// 显示收讫
                                                btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ZERO);// 显示银行收费提示信息;//
                                                // 不显示银行收费提示信息
                                            }
                                            btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示保存
                                            btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示不予受理
                                        }
                                        // 已经收费，显示保存受理
                                        else {
                                            btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存
                                            btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 显示受理
                                            btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                        }
                                    }
                                } else {
                                    btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 显示受理
                                    btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                    btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                }
                            } else {
                                // 即办件不显示接件按钮
                                if (ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                                        && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())) {
                                    btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示接件
                                } else {
                                    btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ONE);// 显示接件
                                }

                            }
                            break;
                        case ZwfwConstant.BANJIAN_STATUS_YJJ:// 已接件
                        case ZwfwConstant.BANJIAN_STATUS_DBB:// 待补办
                            // 需要受理前收费
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(chargeflag)
                                    && ZwfwConstant.CHARGE_WHEN_SLQ.equals(chargewhen)) {
                                // 如果尚未核价，显示保存、核价、补正、不予受理
                                if (ischeck == ZwfwConstant.CONSTANT_INT_ZERO) {
                                    btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存
                                    btnMap.replace("btnPricing", ZwfwConstant.CONSTANT_STR_ONE);// 显示核价
                                    // 即办件不显示补正按钮
                                    /*
                                     * if (ZwfwConstant.ITEMTYPE_JBJ.equals(
                                     * audittask.getType().toString())) {
                                     * btnMap.replace("btnPatch",
                                     * ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                     * }
                                     * else {
                                     * btnMap.replace("btnPatch",
                                     * ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                     * }
                                     */
                                    // 判断当前事项audit_task.businesstype等于1代表依申请事项，显示补正按钮
                                    if ("1".equals(audittask.getStr("businesstype"))) {
                                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                    } else {
                                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                    }
                                    btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                }
                                // 已经核价
                                else {
                                    // 如果尚未收费，显示保存、收讫
                                    if (isfee == ZwfwConstant.CONSTANT_INT_ZERO) {
                                        // 银行收费模式
                                        if ("WB".equals(feemode)) {
                                            btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示收讫
                                            btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ONE);// 显示银行收费提示信息
                                        } else {
                                            btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ONE);// 显示收讫
                                            btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ZERO);// 显示银行收费提示信息;//
                                            // 不显示银行收费提示信息
                                        }
                                        btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示保存
                                        btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示不予受理
                                    }
                                    // 已经收费，显示保存受理
                                    else {
                                        if (ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
                                                && ZwfwConstant.ZIJIANMODE_JJMODE
                                                .equals(String.valueOf(auditTaskExtension.getZijian_mode()))) {
                                            btnMap.replace("txtDeptHandle", ZwfwConstant.CONSTANT_STR_ONE);// 显示部门办理中
                                            // 需要打证，尚未发证
                                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate) && iscert == 0) {
                                                btnMap.replace("btnResult", ZwfwConstant.CONSTANT_STR_ONE);// 显示证书发放
                                            }
                                            // 不需要打证或打证完成
                                            else {
                                                btnMap.replace("btnFinish", ZwfwConstant.CONSTANT_STR_ONE);
                                            }
                                        } else {
                                            btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存
                                            btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 显示受理
                                            btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                            // 即办件不显示补正按钮
                                            /*
                                             * if
                                             * (ZwfwConstant.ITEMTYPE_JBJ.equals
                                             * (audittask.getType().toString()))
                                             * {
                                             * btnMap.replace("btnPatch",
                                             * ZwfwConstant.CONSTANT_STR_ZERO);/
                                             * / 不显示补正
                                             * }
                                             * else {
                                             * btnMap.replace("btnPatch",
                                             * ZwfwConstant.CONSTANT_STR_ONE);//
                                             * 显示补正
                                             * }
                                             */
                                            // 判断当前事项audit_task.businesstype等于1代表依申请事项，显示补正按钮
                                            if ("1".equals(audittask.getStr("businesstype"))) {
                                                btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                            } else {
                                                btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                            }
                                        }
                                    }
                                }
                            }
                            // 不需要受理前收费,显示保存、受理、不予受理、补正
                            else {
                                if (ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
                                        && ZwfwConstant.ZIJIANMODE_JJMODE
                                        .equals(String.valueOf(auditTaskExtension.getZijian_mode()))) {
                                    btnMap.replace("txtDeptHandle", ZwfwConstant.CONSTANT_STR_ONE);// 显示部门办理中
                                    // 需要打证，尚未发证
                                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate) && iscert == 0) {
                                        btnMap.replace("btnResult", ZwfwConstant.CONSTANT_STR_ONE);// 显示证书发放
                                    }
                                    // 不需要打证或打证完成
                                    else {
                                        btnMap.replace("btnFinish", ZwfwConstant.CONSTANT_STR_ONE);
                                    }
                                } else {
                                    btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ONE);// 显示保存
                                    btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 显示受理
                                    btnMap.replace("btnReject", ZwfwConstant.CONSTANT_STR_ONE);// 显示不予受理
                                    // 即办件不显示补正按钮
                                    /*
                                     * if (ZwfwConstant.ITEMTYPE_JBJ.equals(
                                     * audittask.getType().toString())) {
                                     * btnMap.replace("btnPatch",
                                     * ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                     * }
                                     * else {
                                     * btnMap.replace("btnPatch",
                                     * ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                     * }
                                     */

                                    // 判断当前事项audit_task.businesstype等于1代表依申请事项，显示补正按钮
                                    if ("1".equals(audittask.getStr("businesstype"))) {
                                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 显示补正
                                    } else {
                                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    // 模拟办件不需要出现接件、保存按钮
                    if (auditTaskExtension.getIs_simulation() != null
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIs_simulation().toString())
                            && auditProject.getStatus() <= ZwfwConstant.BANJIAN_STATUS_DJJ) {
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(chargeflag)
                                && ZwfwConstant.CHARGE_WHEN_SLQ.equals(chargewhen)) {

                            if (ischeck == 0) {
                                btnMap.replace("btnPricing", ZwfwConstant.CONSTANT_STR_ONE);// 显示核价
                            } else {
                                // 如果尚未收费，显示保存、收讫
                                if (isfee == ZwfwConstant.CONSTANT_INT_ZERO) {
                                    // 银行收费模式
                                    if ("WB".equals(feemode)) {
                                        btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示收讫
                                        btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ONE);// 显示银行收费提示信息;//
                                        // 显示银行收费提示信息
                                    } else {
                                        btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ONE);// 显示收讫
                                        btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ZERO);// 显示银行收费提示信息;//
                                        // 不显示银行收费提示信息
                                    }
                                }
                                // 已经收费，显示保存受理
                                else {
                                    btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 显示受理
                                }
                            }
                        } else {
                            btnMap.replace("btnAccept", ZwfwConstant.CONSTANT_STR_ONE);// 受理按钮显示
                        }
                        btnMap.replace("btnReceive", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示接件
                        btnMap.replace("btnSave", ZwfwConstant.CONSTANT_STR_ZERO);// 显示保存
                    }
                }

                // 2.3.2已受理未办结时，正常计时或暂停计时
                else if (status >= ZwfwConstant.BANJIAN_STATUS_YSL) {
                    // btnSpecialOP = ZwfwConstant.CONSTANT_STR_ONE;
                    // 非办结步骤，显示工作流按钮
                    if (isbanjiestep != ZwfwConstant.CONSTANT_INT_ONE) {
                        handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;// 显示工作流按钮
                        String isPatch = handleConfig.getFrameConfig("AS_PATCH_AFTER_ACCEPT", centerGuid).getResult();
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isPatch)
                                && !ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
                            btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ONE);// 过程中显示补正按钮
                        }
                    }
                    // 判断是否在收讫步骤
                    Boolean isshouqi = false;
                    if (status == ZwfwConstant.BANJIAN_STATUS_SPTG) {
                        // 当前是办结岗位
                        if (isbanjiestep == ZwfwConstant.CONSTANT_INT_ONE) {
                            // 办件尚未录入办结类型，说明尚未点击不予批准或者批准
                            if (!(auditProject.getBanjieresult() == null || auditProject.getBanjieresult() == 0)) {
                                // 办件设置办结前收费
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(chargeflag)
                                        && ZwfwConstant.CHARGE_WHEN_BJQ.equals(chargewhen)) {
                                    // 办件已经核价
                                    if (ischeck == ZwfwConstant.CONSTANT_INT_ONE) {
                                        // 如果尚未收费,显示保存和收讫
                                        if (isfee == ZwfwConstant.CONSTANT_INT_ZERO) {
                                            isshouqi = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // 暂停计时状态 且非自建系统办件时，显示恢复计时(审核通过的情况不在这个里面考虑)，即办件也不计时
                    if (ispause == ZwfwConstant.CONSTANT_INT_ONE
                            && auditTaskExtension.getIszijianxitong() != ZwfwConstant.CONSTANT_INT_ONE
                            && status != ZwfwConstant.BANJIAN_STATUS_SPTG) {
                        handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;
                        btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                        btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ONE);// 显示恢复计时
                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO); // 不显示补正
                        // 上报件和即办件处理
                        if (ZwfwConstant.ITEMTYPE_SBJ.equals(audittask.getType().toString())
                                || ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
                            btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                            btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                        }

                    }
                    // 延期审核中
                    else if (isdelay == 10 && isbanjiestep != ZwfwConstant.CONSTANT_INT_ONE) {
                        btnMap.replace("txtDelay", ZwfwConstant.CONSTANT_STR_ONE);// 显示延期申请提示
                        handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;// 不显示工作流
                    }
                    // 正常计时
                    else {
                        //撤销申请只在办结环节显示
                        if(status==ZwfwConstant.BANJIAN_STATUS_SPTG){
                            btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ONE);// 显示撤销申请
                        }
                        btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ONE);// 显示异常终止
                        btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ONE);// 显示暂停计时
                        btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ONE);// 显示延期申请
                        // 即办件不显示延期及暂停计时
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
                            btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                            btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                        }

                        switch (status) {
                            case ZwfwConstant.BANJIAN_STATUS_YSL:// 办件已受理
                                // 当前是办结岗位,不显示批准和不予批准
                                if (isbanjiestep == ZwfwConstant.CONSTANT_INT_ONE) {
                                    // btnStandardOP =
                                    // ZwfwConstant.CONSTANT_STR_ONE;
                                    handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;// 显示工作流办结
                                    btnMap.replace("btnApprove", ZwfwConstant.CONSTANT_STR_ZERO);// 批准
                                    btnMap.replace("btnNotApprove", ZwfwConstant.CONSTANT_STR_ZERO);// 不予批准
                                    btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 办结不显示延期申请
                                }
                                if (1 == auditTaskExtension.getIszijianxitong()) {
                                    btnMap.replace("txtDeptHandle", ZwfwConstant.CONSTANT_STR_ONE);// 显示部门办理中
                                    btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示撤销申请
                                    btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示异常终止
                                    btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                                    btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                    btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正
                                    // 需要打证，尚未发证
                                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate) && iscert == 0) {
                                        btnMap.replace("btnResult", ZwfwConstant.CONSTANT_STR_ONE);// 显示证书发放
                                    }
                                    // 不需要打证或打证完成
                                    else {
                                        btnMap.replace("btnFinish", ZwfwConstant.CONSTANT_STR_ONE);
                                    }
                                }
                                break;
                            case ZwfwConstant.BANJIAN_STATUS_SPBTG:// 办件审批不通过
                                // btnStandardOP =
                                // ZwfwConstant.CONSTANT_STR_ONE;
                                // 审批不通过后不显示延期
                                btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                // 当前是办结岗位
                                if (isbanjiestep == ZwfwConstant.CONSTANT_INT_ONE) {
                                    // 办件尚未录入办结类型，说明尚未点击不予批准
                                    if (auditProject.getBanjieresult() == null || auditProject.getBanjieresult() == 0) {
                                        btnMap.replace("btnNotApprove", ZwfwConstant.CONSTANT_STR_ONE);// 不予批准
                                    }
                                    // 办件已经录入办结类型，说明已经点击不予批准
                                    else {
                                        btnMap.replace("btnEvaluate", ZwfwConstant.CONSTANT_STR_ONE);// 显示请评价
                                        btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                                        btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                        if (auditTaskExtension != null && ZwfwConstant.CONSTANT_STR_ONE
                                                .equals(auditTaskExtension.getStr("is_inquest"))) {
                                            btnMap.put("txtYky", ZwfwConstant.CONSTANT_STR_ONE);
                                            btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示撤销申请
                                            btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示异常终止
                                        } else {
                                            handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;// 显示工作流办结
                                        }
                                    }
                                }
                                break;
                            case ZwfwConstant.BANJIAN_STATUS_SPTG:// 办件审批通过
                                // btnStandardOP =
                                // ZwfwConstant.CONSTANT_STR_ONE;
                                // 审批通过后不显示延期
                                handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;
                                btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                                btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO); // 不显示补正
                                // 上报件处理
                                if (ZwfwConstant.ITEMTYPE_SBJ.equals(audittask.getType().toString())
                                        || ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
                                    btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                                    btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                }
                                btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                // 当前是办结岗位
                                if (isbanjiestep == ZwfwConstant.CONSTANT_INT_ONE) {
                                    // 办件尚未录入办结类型，说明尚未点击不予批准或者批准
                                    if (auditProject.getBanjieresult() == null || auditProject.getBanjieresult() == 0) {
                                        btnMap.replace("btnApprove", ZwfwConstant.CONSTANT_STR_ONE);// 批准
                                        btnMap.replace("btnNotApprove", ZwfwConstant.CONSTANT_STR_ONE);// 不予批准
                                    }
                                    // 办件已经录入办结类型，说明已经点击不予批准或者批准
                                    else {
                                        // 办件设置办结前收费
                                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(chargeflag)
                                                && ZwfwConstant.CHARGE_WHEN_BJQ.equals(chargewhen)) {
                                            btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                            btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                                            // 如果尚未核价,显示核价
                                            if (ischeck == ZwfwConstant.CONSTANT_INT_ZERO) {
                                                btnMap.replace("btnPricing", ZwfwConstant.CONSTANT_STR_ONE);// 显示核价

                                            }
                                            // 办件已经核价
                                            else {
                                                // 如果尚未收费,显示保存和收讫
                                                if (isfee == ZwfwConstant.CONSTANT_INT_ZERO) {
                                                    // 银行收费模式
                                                    if ("WB".equals(feemode)) {
                                                        btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ZERO);// 显示收讫
                                                        btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ONE);// 显示银行收费提示信息;
                                                        btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                                                        btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示撤销申请
                                                        btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示异常终止
                                                    } else {
                                                        btnMap.replace("btnReceipt", ZwfwConstant.CONSTANT_STR_ONE);// 显示收讫
                                                        btnMap.replace("txtBank", ZwfwConstant.CONSTANT_STR_ZERO);// 显示银行收费提示信息;//
                                                        // 不显示银行收费提示信息
                                                    }
                                                }
                                                // 办件已收费
                                                else {
                                                    // 需要打证，尚未发证
                                                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate)
                                                            && iscert == 0) {
                                                        btnMap.replace("btnResult", ZwfwConstant.CONSTANT_STR_ONE);// 显示证书发放
                                                    }
                                                    // 不需要打证或打证完成
                                                    else {
                                                        // 即办件或自建系统办件显示自定义办结按钮
                                                        if ((ZwfwConstant.ITEMTYPE_JBJ
                                                                .equals(audittask.getType().toString())
                                                                && !ZwfwConstant.JBJMODE_STANDARD
                                                                .equals(audittask.getJbjmode()))
                                                                || 1 == auditTaskExtension.getIszijianxitong()) {
                                                            btnMap.replace("btnFinish", ZwfwConstant.CONSTANT_STR_ONE);
                                                        } else {
                                                            handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;// 显示工作流办结
                                                        }
                                                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(ifexpress)) {
                                                            //btnMap.replace("btnhjkd", ZwfwConstant.CONSTANT_STR_ONE);
                                                            //只有当有回寄材料的时候才会显示按钮
                                                            IAuditProjectDeliveryService deliveryService = ContainerFactory.getContainInfo().getComponent(IAuditProjectDeliveryService.class);
                                                            AuditProjectDelivery delivery = deliveryService.getDeliveryByProjectguid(auditProject.getRowguid());
                                                            if (delivery != null && "1".equals(delivery.getIssendback())) {
                                                                btnMap.replace("btndhtx", ZwfwConstant.CONSTANT_STR_ONE);
                                                            }
                                                        }
                                                    }
                                                    btnMap.replace("btnEvaluate", ZwfwConstant.CONSTANT_STR_ONE);// 显示请评价
                                                    btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                                                    btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                                }
                                            }
                                        }
                                        // 先判断是不是需要显示恢复计时
                                        else if (ispause == ZwfwConstant.CONSTANT_INT_ONE && auditTaskExtension
                                                .getIszijianxitong() != ZwfwConstant.CONSTANT_INT_ONE) {

                                            {
                                                handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ZERO;
                                                btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                                btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ONE);// 显示恢复计时
                                                btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO); // 不显示补正
                                                btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);// 显示撤销申请
                                                btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);// 显示异常终止
                                                // 上报件和即办件处理
                                                if (ZwfwConstant.ITEMTYPE_SBJ.equals(audittask.getType().toString())
                                                        || ZwfwConstant.ITEMTYPE_JBJ
                                                        .equals(audittask.getType().toString())) {
                                                    btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                                                    btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                                }

                                            }
                                        } else {
                                            // 是否需要发证尚未发证
                                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate)
                                                    && iscert == ZwfwConstant.CONSTANT_INT_ZERO) {
                                                btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时按钮
                                                btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 显示恢复计时
                                                btnMap.replace("btnResult", ZwfwConstant.CONSTANT_STR_ONE);// 显示证书发放
                                            } else {
                                                // 即办件显示自定义办结按钮
                                                if ((ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                                                        && !ZwfwConstant.JBJMODE_STANDARD
                                                        .equals(audittask.getJbjmode()))
                                                        || 1 == auditTaskExtension.getIszijianxitong()) {
                                                    btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示恢复计时
                                                    btnMap.replace("btnFinish", ZwfwConstant.CONSTANT_STR_ONE);
                                                } else {
                                                    handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;// 显示工作流办结
                                                }
                                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(ifexpress)) {
                                                    //btnMap.replace("btnhjkd", ZwfwConstant.CONSTANT_STR_ONE);
                                                    //只有当有回寄材料的时候才会显示按钮
                                                    IAuditProjectDeliveryService deliveryService = ContainerFactory.getContainInfo().getComponent(IAuditProjectDeliveryService.class);
                                                    AuditProjectDelivery delivery = deliveryService.getDeliveryByProjectguid(auditProject.getRowguid());
                                                    if (delivery != null && "1".equals(delivery.getIssendback())) {
                                                        btnMap.replace("btndhtx", ZwfwConstant.CONSTANT_STR_ONE);
                                                    }
                                                }
                                            }
                                            btnMap.replace("btnEvaluate", ZwfwConstant.CONSTANT_STR_ONE);// 显示请评价
                                            btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                                            btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示延期申请
                                            btnMap.replace("btnRecover", ZwfwConstant.CONSTANT_STR_ZERO);// 显示恢复计时
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                // 处理已办事项报错
                if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ
                        || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                    handlecontrols_btnlst = ZwfwConstant.CONSTANT_STR_ONE;
                    // btnSpecialOP = ZwfwConstant.CONSTANT_STR_ZERO;
                }
                // 判断流程如果被锁定
                if (handleAuditProjectService.isBtnLocked(auditProject.getPviguid(), workItemGuid, userGuid)) {
                    btnMap.replace("btnSuspension", ZwfwConstant.CONSTANT_STR_ZERO);// 显示撤销申请
                    btnMap.replace("btnRevoke", ZwfwConstant.CONSTANT_STR_ZERO);// 显示异常终止
                    btnMap.replace("btnPause", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示暂停计时
                    btnMap.replace("btnDelay", ZwfwConstant.CONSTANT_STR_ZERO);// 显示延期申请
                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL) {
                        btnMap.replace("btnPatch", ZwfwConstant.CONSTANT_STR_ZERO);// 不显示补正按钮
                    }
                }

                // 2.3.3控制右侧办件信息显示内容
                ICodeItemsService codeItemsService = ContainerFactory.getContainInfo()
                        .getComponent(ICodeItemsService.class);
                lblfs = codeItemsService.getItemTextByCodeName("申请方式", auditProject.getApplyway().toString()); // 申报方式
                lblstatus = codeItemsService.getItemTextByCodeName("办件状态", status + "");// 办件状态
                if ((status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ)
                        || (status == ZwfwConstant.BANJIAN_STATUS_YJJ
                        && ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
                        && ZwfwConstant.ZIJIANMODE_JJMODE
                        .equals(String.valueOf(auditTaskExtension.getZijian_mode())))) {
                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
                        lbljs = "即办件不计时";// 即办件计时显示内容
                    } else {
                        if (ispause == ZwfwConstant.CONSTANT_INT_ONE) {
                            lbljs = "暂停计时";// 办件处于暂停计时状态
                            //选了“现场勘察”暂停计时，旁边弹出“发起勘验”、“复勘”和“勘验结果”按钮
                            List<AuditProjectUnusual> auditProjectUnusuals = iauditProjectUnusual.getProjectUnusualByProjectGuid(auditProject.getRowguid()).getResult();
                            //给这个result排个序
                            if (EpointCollectionUtils.isNotEmpty(auditProjectUnusuals)) {
                                List<AuditProjectUnusual> auditProjectUnusuals2 = auditProjectUnusuals.stream().sorted(Comparator.comparing(AuditProjectUnusual::getOperatedate).reversed()).collect(Collectors.toList());
                                AuditProjectUnusual auditProjectUnusual = auditProjectUnusuals2.get(0);
                                if(auditProjectUnusual!=null && "3".equals(auditProjectUnusual.getPauseReason())){
                                    // 显示“发起勘验”、“复勘”和“勘验结果”
                                    btnMap.replace("faqikanyan", ZwfwConstant.CONSTANT_STR_ONE);
                                    btnMap.replace("fukan", ZwfwConstant.CONSTANT_STR_ONE);
                                    btnMap.replace("kanyan", ZwfwConstant.CONSTANT_STR_ONE);
                                }
                            }
                        } else {
                            lbljs = "正在计时";// 办件处于计时状态
                            int sparetime = 0;
                            // 获取办件剩余时间
                            IAuditProjectSparetime sparetimeService = ContainerFactory.getContainInfo()
                                    .getComponent(IAuditProjectSparetime.class);
                            AuditProjectSparetime auditprojectsparetime = sparetimeService
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                            // 对于存在办理用时的情况
                            if (auditprojectsparetime != null && !auditprojectsparetime.isEmpty()) {
                                sparetime = auditprojectsparetime.getSpareminutes();
                                if (sparetime >= 0) {
                                    lblshyu = "剩余办理时间：";
                                } else {
                                    lblshyu = "超过办理时间：";
                                }
                                lblshyut = CommonUtil.getSpareTimes(sparetime);
                            }
                            // 否则那块不用显示
                            else {

                                lbljs = "停止计时";
                                lblshyu = "办理用时：";
                                // 并联审批已接件，部门没有开始办理，不会设置计时和开始计时，添加非空判断。
                                if (auditProject.getSpendtime() != null) {
                                    lblshyut = sparetimeService.getSpareTimes(auditProject.getSpendtime()).getResult();
                                } else {
                                    lblshyut = "0";
                                }
                            }

                        }
                    }
                } else {
                    lbljs = "未开始计时";
                }
            }

            HashMap buttonCrol = new HashMap(16);
            HashMap<String, String> lbl = new HashMap(16);
            buttonCrol.put("btnMap", btnMap);
            lbl.put("lblstatus", lblstatus);
            lbl.put("lbljs", lbljs);
            lbl.put("lblfs", lblfs);
            lbl.put("lblshyu", lblshyu);
            lbl.put("lblshyut", lblshyut);
            lbl.put("handlecontrols_btnlst", handlecontrols_btnlst);
            lbl.put("zzurl", zzurl);
            lbl.put("iscertificate", iscertificate);
            lbl.put("lblresult", lblresult);
            buttonCrol.put("lbl", lbl);
            return buttonCrol;
        }
        return null;

    }

    /*
     * @SuppressWarnings({"rawtypes", "unchecked" })
     *
     * @Override
     * public HashMap<String, HashMap> initHandleControls(AuditProject
     * auditProject, String isJSTY, String callmode,
     * String taskGuid, String workItemGuid, String centerGuid, String areaCode,
     * String userGuid,
     * String windowguid) {
     * return null;
     *
     *
     * }
     */

}
