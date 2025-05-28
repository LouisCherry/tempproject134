package com.epoint.auditproject.auditprojectbatch;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditonlineproject.auditapplyjslog.inter.IAuditApplyJsLog;
import com.epoint.auditonlineproject.auditonlineexpress.domain.AuditOnlineExpress;
import com.epoint.auditonlineproject.auditonlineexpress.inter.IAuditOnlineExpress;
import com.epoint.auditproject.audithandlecontrol.action.JNAuditHandleControlAction;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.auditproject.entity.csfxxb;
import com.epoint.auditproject.entity.jbxxb;
import com.epoint.auditproject.entity.msfxxb;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlinemessages.domain.AuditOnlineMessages;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.auditproject.auditprojectdefaultuser.domain.AuditProjectDefaultuser;
import com.epoint.basic.auditproject.auditprojectdefaultuser.inter.IAuditProjectDefaultuser;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.*;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditsp.handlematerial.inter.IHandleMaterial;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserServiceInternal;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jnycsl.service.YcslService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.sdwaithandle.api.IAuditProjectSamecityService;
import com.epoint.jnzwfw.sdwaithandle.api.entity.AuditProjectSamecity;
import com.epoint.tazwfw.electricity.rest.action.ElectricityController;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.entity.organization.WorkflowUser;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.common.runtime.WorkflowParameterActivity9;
import com.epoint.workflow.service.common.runtime.WorkflowResult9;
import com.epoint.workflow.service.common.runtime.WorkflowTransactor9;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.config.api.IWorkflowActOperationService;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.workflow.service.organ.api.IWFOrganAPI9;
import com.epoint.zczwfw.evaluateproject.api.IEvaluateProjectService;
import com.epoint.zczwfw.evaluateproject.api.entity.EvaluateProject;
import com.epoint.zczwfw.zccommon.api.IZcCommonService;
import com.epoint.zhenggai.api.ZhenggaiService;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2022/5/7.
 * 批量逻辑处理的后台
 *
 * @author ${阳佳}
 */
@RestController("batchoperateaction")
@Scope("request")
public class BatchOperateAction extends BaseController {
    @Autowired
    private IAuditOnlineEvaluat evaluateservice;
    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;

    @Autowired
    IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    /**
     * 一窗受理service
     */
    @Autowired
    private YcslService ycslService;
    @Autowired
    private IJnProjectService iJnProjectService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 同城通办信息表api
     */
    @Autowired
    private IAuditProjectSamecityService iAuditProjectSamecityService;
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditProject auditProjectServcie;

    @Autowired
    private IWFOrganAPI9 orgaService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IHandleMaterial handleMaterialService;

    @Autowired
    private IHandleProject handleProjectService;
    @Autowired
    private ISendMQMessage sendMQMessageService;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;
    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private IAuditProjectNumber projectNumberService;

    @Autowired
    private IAuditProjectNotify projectNotifyService;

    @Autowired
    private IAuditApplyJsLog iAuditApplyJsLog;
    @Autowired
    private IJNAuditProject iAuditProject;

    @Autowired
    private IAuditOnlineIndividual auditOnlieIndividualService;
    @Autowired
    private IAuditProjectOperation auditProjectOperationService;
    @Autowired
    private IAuditOnlineExpress iAuditOnlineExpress;
    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IHandleProject handleProject;
    @Autowired
    private IAuditProjectUnusual projectUnusualService;
    @Autowired
    private IWFInstanceAPI9 wf9instance;

    @Autowired
    private IWFInstanceAPI9 instanceapi;

    @Autowired
    private IWorkflowActivityService workflowActivityService9;
    @Autowired
    private IAuditTaskRiskpoint iAuditTaskRiskpoint;
    @Autowired
    private IWorkflowActOperationService operateService;
    @Autowired
    private IWorkflowTransitionService transitionService;
    @Autowired
    private IUserServiceInternal frameUserService;
    @Autowired
    private IAuditProjectDefaultuser defaultuserService;

    private String userGuid = "";

    private String displayName = "";


    private String reason = "";
    /**
     * 物流基本信息表实体对象
     */
    private AuditLogisticsBasicinfo dataBean = null;
    private Logger logger = Logger.getLogger(JNAuditHandleControlAction.class);

    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");


    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");

    @Override
    public void pageLoad() {
    }

    /**
     * 批量处理受理的逻辑
     *
     * @param params
     */
    public void batchAcceptProject(String params) {
        //记录材料未正确上传的办件
        List<String> projectOfMaterialFailedList = new ArrayList<>();
        //记录材料不通过的办件数量
        List<Integer> countProjectOfMaterialFailedList = new ArrayList<>();
        //记录办件被删除的数量
        List<Integer> countNotExistProjectList = new ArrayList<>();
        //记录成功处理的办件
        List<Integer> countSuccessProjectList = new ArrayList<>();
        Map<String, String> projectMaterialMap = new HashMap<String, String>();
        String[] guids = params.split(",");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int countTool = guids.length;
        CountDownLatch countDownLatch = new CountDownLatch(countTool);
        // 循环处理各个办件
        for (String paramsGuid : guids) {

//            executorService.execute(() -> {
            String taskguid = paramsGuid.split(";")[1];
            String projectguid = paramsGuid.split(";")[0];
            AuditTask auditTask = null;
            AuditTaskExtension auditTaskExtension = null;
            // 获取事项信息
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            AuditLogisticsBasicinfo auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
            String areaCode = "";
            AuditProject auditProject = new AuditProject();
            areaCode = auditTask.getAreacode();
            // 获取办件信息
            String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid,Onlineapplyerguid";
            auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
            if (auditProject == null) {
                //记录办件被删除的记录
                countNotExistProjectList.add(1);
            } else {
                String biguid = auditProject.getBiguid();
                String suappguid = auditProject.getSubappguid();
                String workItemGuid = "";
                //1. 校验该办件的材料
                List<Record> projectMaterial = handleMaterialService.getProjectMaterialALL(projectguid, biguid, suappguid)
                        .getResult();
                //存在材料
                if (!projectMaterial.isEmpty()) {
                    Integer totalcount = projectMaterial.size();
                    projectMaterial.sort((a,
                                          b) -> (StringUtil.isNotBlank(b.get("Ordernum")) ? Integer.valueOf(b.get("Ordernum").toString())
                            : Integer.valueOf(0))
                            .compareTo(StringUtil.isNotBlank(a.get("Ordernum"))
                                    ? Integer.valueOf(a.get("Ordernum").toString()) : 0));
                    for (int i = 0; i < totalcount; i++) {
                        //1.1 判断当前材料是否是非必需或者容缺 如果是 则跳过
                        Record auditProjectMaterial = projectMaterial.get(i);
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditProjectMaterial.getStr("is_allowrongque")) ||
                                "20".equals(auditProjectMaterial.getStr("necessity")) ||
                                "0".equals(auditProjectMaterial.getStr("necessity"))) {
                            continue;
                        } else {
                            //如果是必须或者不容却 则查看材料的提交方式和当前状态
                            String submitType = projectMaterial.get(i).getStr("submittype");
                            Integer materialStatus = projectMaterial.get(i).getInt("status");
                            Integer rtnValue = 0;
                            // 该函数返回3个值，0表示未提交，1表示提交但是不全，2表示已提交
                            switch (submitType) {
                                case "10":// 电子
                                    if (materialStatus >= 20)
                                        rtnValue = 2;
                                    break;
                                case "20":// 纸质
                                    if (materialStatus % 10 == 5)
                                        rtnValue = 2;
                                    break;
                                case "35":// 电子或纸质
                                    if (materialStatus >= 15)
                                        rtnValue = 2;
                                    break;
                                case "40":// 电子和纸质
                                    if (materialStatus >= 15 && materialStatus <= 20)
                                        rtnValue = 1;
                                    else if (materialStatus == 25)
                                        rtnValue = 2;
                                    break;
                            }
                            if (rtnValue != 2) {
                                projectOfMaterialFailedList.add(auditProject.getRowguid());
                                countProjectOfMaterialFailedList.add(1);
                                //放入map中 表示该办件材料不符合要求
                                projectMaterialMap.put(projectguid, "not");
                                //如果该办件存在材料校验不通过的情况 该办件则不执行受理逻辑
                                break;
                            }
                        }
                    }
                }
                String materialCondition = projectMaterialMap.get(projectguid);
                //2.获取该办件材料情况 如果不为not 则执行受理的逻辑
                if (!"not".equals(materialCondition)) {
                    turnhcpevaluate(auditProject, 1, "提交申请信息", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));


                    //保存处理过办件的区域编码(不重复保存)
                    String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
                    String areaStr = "";
                    if (StringUtil.isBlank(auditProject.getHandleareacode())) {
                        areaStr = handleAreaCode + ",";
                    } else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
                        areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
                    }
                    if (StringUtil.isNotBlank(areaStr)) {
                        auditProject.setHandleareacode(areaStr);
                        //auditProjectServcie.updateProject(auditProject);
                    }

                    /*阳佳 批量处理无需弹文书 boolean docflag1 = true;
                    // 事项配置中配置是否不弹出文书
                    String is_notOpenDoc1 = auditTaskExtension.getIs_notopendoc();
                    if (StringUtil.isNotBlank(is_notOpenDoc1)) {
                        String[] str = is_notOpenDoc1.split(",");
                        for (String doctype : str) {
                            if ("1".equals(doctype)) {
                                docflag1 = false;
                                break;
                            }
                        }
                    }
                    if (docflag1) {
                        // 弹出文书
                        if (auditProject != null) {
                            String asdocword = handleConfigService.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid())
                                    .getResult();
                            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
                            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                            String address = handleDocService.getDocEditPage(auditProject.getTaskguid(),
                                    auditProject.getCenterguid(), auditTaskExtension.getIs_notopendoc(),
                                    String.valueOf(ZwfwConstant.DOC_TYPE_SLTZS), false, isword).getResult();
                            if (StringUtil.isNotBlank(address)) {
                                address += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                        + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskguid();
                            }
                            addCallbackParam("address", address.toString());
                            addCallbackParam("isword", isword ? "1" : "0");
                        }
                    }
*/
                    //判断是否推送数据到浪潮
                    String lc = zhenggaiImpl.getchargelc(auditProject.getTaskguid());
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(lc)) {
                        Boolean reslut = pushInfolc(auditTask, auditProject);
                        if (reslut.equals(false)) {
                            return;
                        }
                    }

                    String formid = auditTaskExtension.get("formid");
                    String zjxt = auditTask.getStr("charge_zjxt");
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(zjxt) && "3".equals(formid)) {
                        Boolean reslut = turnfcitembase(auditProject, auditTask);
                        if (reslut.equals(false)) {
                            return;
                        }
                    }

                    // 承诺件或正常流程的即办件
                    if (auditTask.getType().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ))
                            || auditTask.getType().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_SBJ))
                            || (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
                            && ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode()))) {
                        // 承诺件
                        if (1 == auditTaskExtension.getIszijianxitong()) {
                            // TODO
                            EpointFrameDsManager.begin(null);
                            handleProjectService.handleAccept(auditProject, workItemGuid,
                                    UserSession.getInstance().getDisplayName(), UserSession.getInstance().getUserGuid(),
                                    ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid());
                            EpointFrameDsManager.commit();
                            String msgs = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                            msgs += "&handleSendToZjxt:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                            // 测试办件不生成

                            AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                    .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            String areaName = "";
                            if (auditOrgaArea != null) {
                                areaName = auditOrgaArea.getXiaquname();
                            }
                            // 测试办件不生成
                            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                // MQ 江苏省标
                                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                                    msgs = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                            + areaName + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                                    ProducerMQ.sendByExchange("receiveproject", msgs, "");
                                }
                            }
                            addCallbackParam("message", "refresh");

                        }

                        // 如果是中心办理的，那么就可以起流程
                        else {
                            EpointFrameDsManager.begin(null);
                            HashMap<String, String> rtnValue = null;
                            try {
                                rtnValue = new AuditWorkflowBizlogic().startProjectWorkflow(taskguid,
                                        projectguid, auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                                        UserSession.getInstance().getDisplayName());

                                EpointFrameDsManager.commit();
                            } catch (Exception e) {
                                EpointFrameDsManager.rollback();
                            } finally {

                                EpointFrameDsManager.close();

                            }

                            if (StringUtil.isNotBlank(rtnValue.get("message"))) {
                                addCallbackParam("message", rtnValue.get("message"));
                            } else {
                                addCallbackParam("workItemGuid", rtnValue.get("workItemGuid"));
                                addCallbackParam("operationGuid", rtnValue.get("operationGuid"));
                                addCallbackParam("transitionGuid", rtnValue.get("transitionGuid"));
                                addCallbackParam("pviGuid", rtnValue.get("pviGuid"));
                            }
                            if (StringUtil.isNotBlank(lc)) {
                                if ("1".equals(lc)) {
                                    handleProjectService.handleAccept(auditProject, workItemGuid,
                                            UserSession.getInstance().getDisplayName(), UserSession.getInstance().getUserGuid(),
                                            ZwfwUserSession.getInstance().getWindowName(),
                                            ZwfwUserSession.getInstance().getWindowGuid());
                                }
                            }

                            String docflag = "true";
                            // 事项配置中配置是否不弹出文书
                            String is_notOpenDoc = auditTaskExtension.getIs_notopendoc();
                            if (StringUtil.isNotBlank(is_notOpenDoc)) {
                                String[] str = is_notOpenDoc.split(",");
                                for (String doctype : str) {
                                    if ("1".equals(doctype)) {
                                        docflag = "false";
                                        break;
                                    }
                                }
                            }
                            addCallbackParam("docflag", docflag);
                            addCallbackParam("taskid", auditTask.getTask_id());
                            addCallbackParam("taskguid", auditTask.getRowguid());
                        }

                    } else {
                        // TODO
                        EpointFrameDsManager.begin(null);
                        // 即办件，直接更新状态待办结状态,并且发送消息
                        if (auditTask != null) {
                            if (ZwfwUserSession.getInstance().getCitylevel() != null
                                    && ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {

                                AuditTaskDelegate delegate = auditTaskDelegateService.findByTaskIDAndAreacode(
                                        auditTask.getTask_id(), ZwfwUserSession.getInstance().getAreaCode()).getResult();
                                if (delegate != null) {
                                    if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                                            && "是".equals(delegate.getUsecurrentinfo())) {
                                        if (delegate.getPromise_day() != null) {
log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day()+",user:"+userSession.getUserGuid());
                                            auditTask.setPromise_day(delegate.getPromise_day());
                            log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day()+",user:"+userSession.getUserGuid());
                                        }
                                    }
                                }
                            }
                        }
                        auditTaskService.updateAuditTask(auditTask);
                        handleProjectService.handleAccept(auditProject, workItemGuid, UserSession.getInstance().getDisplayName(),
                                UserSession.getInstance().getUserGuid(), ZwfwUserSession.getInstance().getWindowName(),
                                ZwfwUserSession.getInstance().getWindowGuid());
                        EpointFrameDsManager.commit();

                        AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                                .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                        String areaName = "";
                        if (auditOrgaArea != null) {
                            areaName = auditOrgaArea.getXiaquname();
                        }

                        handleProjectService.handleApprove(auditProject, UserSession.getInstance().getDisplayName(),
                                UserSession.getInstance().getUserGuid(), workItemGuid, null);

                        addCallbackParam("message", "refresh");
                    }
                    // 使用物流
                    if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                        String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标", auditLogisticsBasicinfo.getRcv_prov());
                        if (StringUtil.isNotBlank(prov)) {
                            auditLogisticsBasicinfo.setRcv_prov(prov);
                        }
                        String city = iCodeItemsService.getItemTextByCodeName("行政区划国标", auditLogisticsBasicinfo.getRcv_city());
                        if (StringUtil.isNotBlank(city)) {
                            auditLogisticsBasicinfo.setRcv_city(city);
                        }
                        String country = iCodeItemsService.getItemTextByCodeName("行政区划国标",
                                auditLogisticsBasicinfo.getRcv_country());
                        if (StringUtil.isNotBlank(country)) {
                            auditLogisticsBasicinfo.setRcv_country(country);
                        }
                        auditLogisticsBasicinfo.setStatus("0");
                        auditLogisticsBasicinfo.setCallState("05");// 初始化状态
                        auditLogisticsBasicinfo.setNet_type("2");
                        auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
                        auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
                        auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
                        if (StringUtil.isBlank(auditLogisticsBasicinfo.getProjectguid())) {
                            handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
                        } else {
                            handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
                        }
                        handleProjectService.saveProject(auditProject);
                    }
                    // 如果存在待办事宜，则删除待办
                    List<AuditOrgaWindowUser> users = new ArrayList<AuditOrgaWindowUser>();
                    ;
                    List<MessagesCenter> messagesCenterList = new ArrayList<MessagesCenter>();
                    List<MessagesCenter> messagesList = new ArrayList<MessagesCenter>();
                    if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
                        users = iAuditOrgaWindow.getUserByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                        for (AuditOrgaWindowUser user : users) {
                            messagesList = messageCenterService.queryForList(user.getUserguid(), null, null, "",
                                    IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                                    -1);
                            if (messagesList != null && messagesList.size() > 0) {
                                messagesCenterList.addAll(messagesList);
                            }
                        }
                    } else {
                        messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                                IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
                    }


                    if (messagesCenterList != null && messagesCenterList.size() > 0) {
                        for (MessagesCenter messagescenter : messagesCenterList) {
                            messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
                            EpointFrameDsManager.commit();
                        }
                        EpointFrameDsManager.commit();
                    }


                    if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_SPTG) {
                        // 发送待办事宜
                        String title = "【办结】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                        String messageItemGuid = UUID.randomUUID().toString();
                        String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                                ? auditTaskExtension.getCustomformurl() : ZwfwConstant.CONSTANT_FORM_URL;
                        String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                                + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid() + "&MessageItemGuid="
                                + messageItemGuid;
                        messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                                IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                                userSession.getUserGuid(), userSession.getDisplayName(), "待办结", handleUrl, userSession.getOuGuid(),
                                "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                                auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                                userSession.getUserGuid(), "", "");
                        addCallbackParam("bjmessage", title);
                    }
                    //  不使用一窗，
                    boolean useYcsl = ycslService.useYcslByTaskExtensionObj1(auditTaskExtension);
                    logger.info("useYcsl=" + useYcsl);
                    if (useYcsl) {
                        pushReceiveInfo2SelfBuildSystem(auditProject, auditTask, auditTaskExtension);
                    }
                    // 调用电力接口推送办件状态及信息
                    if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YSL ||
                            auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                        if (StringUtil.isNotBlank(auditProject.getBusinessguid()) && "4".equals(auditProject.getStr("is_lczj"))) {
                            auditProject.setStatus(30);
                            auditProject.setAcceptusername(userSession.getDisplayName());
                            auditProject.setAcceptuserdate(new Date());
                            handleProjectService.saveProject(auditProject);
                            new ElectricityController().pushStatusToEc(auditProject, auditTask, null);
                        }
                    }
                    //刷新属地办理信息
                    if (auditTask != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTask.getStr("is_samecity"))) {
                        AuditProjectSamecity auditProjectSamecity = iAuditProjectSamecityService
                                .getProjectSamecityByProjectguid(auditProject.getRowguid());
                        ZwfwUserSession zwfwUserSession = ZwfwUserSession.getInstance();
                        if (auditProjectSamecity != null) {
                            auditProjectSamecity.setShudiarea(zwfwUserSession.getAreaCode());
                            auditProjectSamecity.setShudicenterguid(zwfwUserSession.getCenterGuid());
                            if (StringUtil.isNotBlank(zwfwUserSession.getCenterGuid())) {
                                AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(zwfwUserSession.getCenterGuid()).getResult();
                                if (center != null) {
                                    auditProjectSamecity.setShudicentername(center.getCentername());
                                }
                            }
                            auditProjectSamecity.setShudideptguid(userSession.getOuGuid());
                            auditProjectSamecity.setShudideptname(userSession.getOuName());
                            auditProjectSamecity.setShudirenguid(userSession.getUserGuid());
                            auditProjectSamecity.setShudirenname(userSession.getDisplayName());
                            auditProjectSamecity.setShudirentel(userSession.getMobile());
                            //代办预审通过
                            iAuditProjectSamecityService.update(auditProjectSamecity);
                        }
                    }
                    //生成施工许可表单实体
                    AuditProjectFormSgxkz auditProjectFormSgxkz = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                    if (auditProjectFormSgxkz == null) {
                        auditProjectFormSgxkz = new AuditProjectFormSgxkz();
                        auditProjectFormSgxkz.setRowguid(UUID.randomUUID().toString());
                        auditProjectFormSgxkz.setProjectguid(projectguid);
                        auditProjectFormSgxkz.setOperatedate(new Date());
                        auditProjectFormSgxkz.setOperateusername(userSession.getDisplayName());
                    }

                    //表单信息
                    if (auditProjectFormSgxkz != null && StringUtil.isNotBlank(auditProjectFormSgxkz.getXiangmumingchen())) {
                        AuditProjectFormSgxkz auditProjectFormSgxkz1 = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                        if (auditProjectFormSgxkz1 != null) {
                            iAuditProjectFormSgxkzService.update(auditProjectFormSgxkz);
                        } else {
                            iAuditProjectFormSgxkzService.insert(auditProjectFormSgxkz);
                        }
                    }

                    if ("370829".equals(auditProject.getAreacode()) || "370882".equals(auditProject.getAreacode()) || "370881".equals(auditProject.getAreacode()) || "370827".equals(auditProject.getAreacode()) || "370892".equals(auditProject.getAreacode()) || "370830".equals(auditProject.getAreacode()) || "370831".equals(auditProject.getAreacode()) || "370891".equals(auditProject.getAreacode()) || "370890".equals(auditProject.getAreacode()) || "370832".equals(auditProject.getAreacode()) || "370826".equals(auditProject.getAreacode()) || "370800".equals(auditProject.getAreacode()) || "370828".equals(auditProject.getAreacode()) || "370883".equals(auditProject.getAreacode())) {
                        evaluate(auditProject, "2");

                    } else {
                        if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {
                            evaluate(auditProject, "");
                        }
                    }
                    countSuccessProjectList.add(1);
                }
            }
        }
        //将处理结果返回给前端
        Integer totalProjectCount = guids.length; //全部办件数量
        Integer projectOfMaterialFailedCount = countProjectOfMaterialFailedList.size();//材料校验不通过的数量
        Integer notExistProjectCount = countNotExistProjectList.size();//办件不存在的数量
        addCallbackParam("totalProjectCount", totalProjectCount);
        addCallbackParam("projectOfMaterialFailedCount", projectOfMaterialFailedCount);
        addCallbackParam("notExistProjectCount", notExistProjectCount);
        addCallbackParam("countSuccessProjectList", countSuccessProjectList.size());
        addCallbackParam("projectOfMaterialFailedList", StringUtil.join(projectOfMaterialFailedList));
    }

    /**
     * 流转至下一步的逻辑
     */
    public void handleProject(String type, String params, String selectUserGuids, String projectOfMaterialFailed) {
        //工作流流转至下一步的通用方法
        String[] guids = params.split(",");
        // 循环处理各个办件
        for (String paramsGuid : guids) {
            String taskguid = paramsGuid.split(";")[1];
            String projectguid = paramsGuid.split(";")[0];
            //如果有材料校验不通过的 则不走后面的流程
            if (StringUtil.isNotBlank(projectOfMaterialFailed) && projectOfMaterialFailed.contains(projectguid)) {
                continue;
            }
            AuditTask auditTask = null;
            AuditTaskExtension auditTaskExtension = null;
            // 获取事项信息
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            String areaCode = "";
            AuditProject auditProject;
            areaCode = auditTask.getAreacode();
            // 获取办件信息
            String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid,Onlineapplyerguid";
            auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
            String msg = "";
            ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(auditProject.getPviguid());
            List<Integer> statusList = new ArrayList<>();
            statusList.add(10);//未提交
            statusList.add(20);//已接收未处理

            if (ZwfwConstant.CONSTANT_STR_ONE.equals(String.valueOf(auditTask.getType()))
                    && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTask.getJbjmode())) {
                EpointFrameDsManager.begin(null);
                handleProjectService.handleFinish(auditProject, UserSession.getInstance().getDisplayName(), userGuid,
                        null);
                EpointFrameDsManager.commit();
                continue;
            }

            List<WorkflowWorkItem> workflowWorkItemList = iAuditProject.getWorkItemListByPVIGuidAndStatus(auditProject.getPviguid());

            WorkflowWorkItem workitem = workflowWorkItemList.get(0);
            String workItemGuid = workitem.getWorkItemGuid();
            JSONObject data = new JSONObject();//存储工作流流转需要的信息
            AuditTaskRiskpoint riskpoint = null;//当前的岗位信息
            AuditTaskRiskpoint nextpoint = null;//下一步的岗位信息
            WorkflowActivityOperation operation = null;//活动执行的操作
            WorkflowTransition tran = null;//操作步骤
            List<WorkflowActivityOperation> list_operation = null;//当前操作按钮列表
            List<WorkflowTransition> list_t = transitionService.selectByFromActivityGuid(workitem.getActivityGuid());
            //操作流程
            if (list_t != null && list_t.size() > 0) {
                tran = list_t.get(0);//获取当前活动对应的流程信息
            }
            //获取到当前岗位信息和下一步的活动名称
            riskpoint = iAuditTaskRiskpoint.getAuditTaskRiskpointByActivityguid(workitem.getActivityGuid(), true).getResult();
            nextpoint = iAuditTaskRiskpoint.getAuditTaskRiskpointByActivityguid(tran.getToActivityGuid(), true).getResult();
            String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
            String currentareacode = auditProject.getCurrentareacode();
            IAuditOrgaArea iAuditOrgaArea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
            AuditOrgaArea currentarea = iAuditOrgaArea.getAreaByAreacode(currentareacode).getResult();
            AuditOrgaArea handlearea = iAuditOrgaArea.getAreaByAreacode(handleAreaCode).getResult();
            //设置流程下一步的处理人
            if (StringUtil.isNotBlank(selectUserGuids)) {
                String[] nextguids = selectUserGuids.split(";");
                JSONObject jsonobject = convertToEngineData(nextguids);
                if (jsonobject.containsKey("nextsteplist")) {
                    String nextsteplist = jsonobject.get("nextsteplist").toString();
                    JSONArray jsonnextsteplist = JSONArray.parseArray(nextsteplist);
                    List<Map<String, Object>> mapListJson = (List) jsonnextsteplist;
                    // 取出数组第一个元素
                    Map<String, Object> o = mapListJson.get(0);
                    Object o1 = o.get("handlerlist");
                    List<Map<String, String>> list = (List<Map<String, String>>) o1;
                    IOuServiceInternal frameOuService = ContainerFactory.getContainInfo()
                            .getComponent(IOuServiceInternal.class);
                    auditProject.setCurrentareacode("");
                    if (list != null && !list.isEmpty()) {
                        for (Map<String, String> map : list) {
                            String ouguid = frameUserService.getUserByUserField("userguid", map.get("handlerguid")).getOuGuid();
                            FrameOuExtendInfo frameOuExtendInfo = frameOuService.getFrameOuExtendInfo(ouguid);
                            if (StringUtil.isNotBlank(frameOuExtendInfo.get("areacode"))) {
                                auditProject.setCurrentareacode(frameOuExtendInfo.get("areacode"));
                                break;
                            }
                        }
                    }
                }
                //搬产品原方法
                if (currentarea != null && !ZwfwConstant.AREA_TYPE_SJ.equals(handlearea.getCitylevel())
                        && !(ZwfwConstant.AREA_TYPE_XQJ.equals(currentarea.getCitylevel())
                        || ZwfwConstant.AREA_TYPE_SJ.equals(currentarea.getCitylevel()))) {
                    String usertype = "";
                    if (currentarea != null) {
                        if (ZwfwConstant.AREA_TYPE_XZJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_ZJ;
                        } else if (ZwfwConstant.AREA_TYPE_XQJ.equals(currentarea.getCitylevel())
                                || ZwfwConstant.AREA_TYPE_SJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_SJ;
                        } else if (ZwfwConstant.AREA_TYPE_CJ.equals(currentarea.getCitylevel())) {
                            usertype = ZwfwConstant.ZWFW_DEFAULTUSERTYPE_CJ;
                        }
                    }
                    // 先删除办件预设，再增加当前选择人员
                    defaultuserService.deleteDefaultuserByRpid(riskpoint.getRiskpointid(), usertype);
                    //保存处理过办件的区域编码(不重复保存)
                    for (String nextuserguid : nextguids) {
                        AuditProjectDefaultuser auditProjectDefaultuser = new AuditProjectDefaultuser();
                        auditProjectDefaultuser.setRowguid(UUID.randomUUID().toString());
                        auditProjectDefaultuser.setDefaultuserguid(nextuserguid);
                        String userName = frameUserService.getUserNameByUserGuid(nextuserguid);
                        auditProjectDefaultuser.setDefaultusername(userName);
                        auditProjectDefaultuser.setTaskid(auditProject.getTaskid());
                        auditProjectDefaultuser.setRiskpointid(riskpoint.getRiskpointid());
                        auditProjectDefaultuser.setAddouguid(UserSession.getInstance().getOuGuid());
                        auditProjectDefaultuser.setAdduserguid(userGuid);
                        auditProjectDefaultuser.setDefaultusertype(usertype);
                        defaultuserService.insertDefaultuser(auditProjectDefaultuser);
                    }
                }
            }
            if (nextpoint != null) {
                data.put("nextactivityname", nextpoint.getActivityname());
                if (StringUtil.isBlank(nextpoint.getAcceptguid())) {
                    data.put("transactorguid", UserSession.getInstance().getUserGuid());
                    data.put("transactorname", UserSession.getInstance().getDisplayName());
                } else {
                    data.put("transactorguid", nextpoint.getAcceptguid().split(";")[0]);
                    data.put("transactorname", nextpoint.getAcceptname().split(";")[0]);
                }
                String userGuids = riskpoint.getAcceptguid();
                if (StringUtil.isNotBlank(userGuids)) {
                    userGuid = userGuids.split(";")[0];
                } else {
                    userGuid = UserSession.getInstance().getUserGuid();
                }
            }
            //获取当前操作对应的operationguid
            list_operation = operateService.selectByActivityGuidAndType(workitem.getActivityGuid(),
                    WorkflowKeyNames9.OperationType_Pass);
            if (list_operation != null && list_operation.size() > 0) {
                list_operation.removeIf(op -> "handlenopass".equals(op.getNote()));
                //***非空判断
                if (list_operation != null && list_operation.size() > 0) {
                    operation = list_operation.get(0);
                }
            }
            data.put("note", operation.getNote());
            //处理
            WorkflowResult9 result = goToNextWorkflow(auditTask.getProcessguid(), auditProject.getPviguid(), workItemGuid, UserSession.getInstance().getUserGuid(), data, operation.getOperationGuid());
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            String message = "";
            switch (type) {
                case "handleaccept":
                    // 这里再判断一下是不是直接到办结步骤了
                    WorkflowActivity activity = result.getNextActIstanceList().get(0).getActivity();
                    boolean pizhun = workflowActivityService9.isTheEndActivity(activity.getProcessVersionGuid(),
                            activity.getActivityGuid());

                    EpointFrameDsManager.begin(null);
                    handleProjectService.handleAccept(auditProject, workItemGuid,
                            UserSession.getInstance().getDisplayName(), userGuid,
                            ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid(),
                            pizhun ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO);
                    EpointFrameDsManager.commit();

                    auditTaskExtension = auditTaskExtensionService
                            .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();


                    boolean docflag = true;
                    // 事项配置中配置是否不弹出文书
                    String is_notOpenDoc = auditTaskExtension.getIs_notopendoc();
                    if (StringUtil.isNotBlank(is_notOpenDoc)) {
                        String[] str = is_notOpenDoc.split(",");
                        for (String doctype : str) {
                            if ("1".equals(doctype)) {
                                docflag = false;
                                break;
                            }
                        }
                    }
                    if (docflag) {
                        // 弹出文书
                        if (auditProject != null) {
                            IHandleConfig handleConfigService = ContainerFactory.getContainInfo()
                                    .getComponent(IHandleConfig.class);
                            IHandleDoc handleDocService = ContainerFactory.getContainInfo().getComponent(IHandleDoc.class);
                            String asdocword = handleConfigService
                                    .getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
                            boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
                            String address = handleDocService.getDocEditPage(auditProject.getTaskguid(),
                                    auditProject.getCenterguid(), auditTaskExtension.getIs_notopendoc(),
                                    String.valueOf(ZwfwConstant.DOC_TYPE_SLTZS), false, isword).getResult();
                            if (StringUtil.isNotBlank(address)) {
                                address += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                        + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskguid();
                            }
                            message = address;
                        }
                    }

                    // *************开始***********
                    // add by yrchan,2022-04-21,勘验事项会从小程序调用接口，发送代办给配置的所有窗口人员；
                    if (auditTaskExtension != null
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
                        IAuditOrgaWindow iAuditOrgaWindow = ContainerFactory.getContainInfo()
                                .getComponent(IAuditOrgaWindow.class);
                        IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                                .getComponent(IMessagesCenterService.class);

                        // 根据中心标识 centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送【待受理】待办
                        // 根据指定条件获取窗口人员
                        auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false)
                                .getResult();

                        SqlConditionUtil windowUserSql = new SqlConditionUtil();
                        windowUserSql.setSelectFields("distinct userguid,username");
                        windowUserSql.in("windowguid",
                                "(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
                                        + auditTask.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid()
                                        + "')");
                        List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
                                .getResult();
                        if (!windowUserList.isEmpty()) {
                            for (AuditOrgaWindowUser windowUser : windowUserList) {
                                List<MessagesCenter> messagesList = iMessagesCenterService.queryForList(
                                        windowUser.getUserguid(), null, null, "", IMessagesCenterService.MESSAGETYPE_WAIT,
                                        auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
                                if (!messagesList.isEmpty()) {
                                    for (MessagesCenter messagescenter : messagesList) {
                                        iMessagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                                                messagescenter.getTargetUser());
                                        EpointFrameDsManager.commit();
                                    }
                                }
                            }
                        }
                    }
                    // *************结束***********
                    break;
                case "handlepass":
                    EpointFrameDsManager.begin(null);
                    handleProjectService.handleProjectPass(auditProject, "", userGuid,
                            workItemGuid);
                    EpointFrameDsManager.commit();
                    // 测试办件不生成
                    if (StringUtil.isNotBlank(auditProject.getIs_test())) {
                        if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                            // MQ 环节信息
                            msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                    + auditProject.getPviguid() + ";"
                                    + workItemGuid + ";"
                                    + operation.getOperationGuid() + ";" + auditProject.getAreacode();
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                            // 接办分离 环节信息
                            msg = auditProject.getRowguid() + "." + auditProject.getPviguid() + "."
                                    + workItemGuid + "."
                                    + operation.getOperationGuid();
                            sendMQMessageService.sendByExchange("exchange_handle", msg,
                                    "project." + ZwfwUserSession.getInstance().getAreaCode() + ".process."
                                            + auditProject.getTask_id());
                            // 住建系统对接使用
                            msg = auditProject.getRowguid() + "." + userGuid;
                            sendMQMessageService.sendByExchange("exchange_handle", msg,
                                    "project." + ZwfwUserSession.getInstance().getAreaCode() + ".handlepass."
                                            + auditProject.getTask_id());
                            // MQ 江苏省标
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                                msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                        + auditProject.getPviguid() + ";"
                                        + workItemGuid + ";"
                                        + operation.getOperationGuid() + ";" + auditProject.getAreacode()
                                        + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                                ProducerMQ.sendByExchange("receiveproject", msg, "");
                            }
                        }
                    }

                    // *************开始*************
                    // add by yrchan,2022-04-19,审核通过，是勘验事项
                    AuditTaskExtension taskExtension = auditTaskExtensionService
                            .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if (taskExtension != null && ZwfwConstant.CONSTANT_STR_ONE.equals(taskExtension.getStr("is_inquest"))) {
                        // 是勘验事项
                        IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                                .getComponent(IMessagesCenterService.class);
                        IZcCommonService iZcCommonService = ContainerFactory.getContainInfo()
                                .getComponent(IZcCommonService.class);

                        // 向短信表（messages_center）插入一条短信记录，短信内容如下：
                        // “[邹城市为民服务中心]xx 您好，您于 2022-03-23 11:32:45
                        // 申请的“农药经营许可”勘验事项，经审核勘验通过，请登录微信小程序“邹城 xxx”进行查看”
                        String dateStr = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                "yyyy-MM-dd HH:mm:ss");
                        String content = auditProject.getApplyername() + " 您好，您于" + dateStr + "申请的“"
                                + auditProject.getProjectname() + "”勘验事项，经审核勘验通过， 请登录“爱山东”应用云勘验模块进行重新修改";

                        iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                                new Date(), auditProject.getContactmobile(), auditProject.getApplyeruserguid(),
                                auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                                UserSession.getInstance().getDisplayName(), "", "", "", false, auditProject.getAreacode());

                        // 向微信消息记录表（audit_online_messages）插入一条微信消息
                        AuditOnlineMessages auditOnlineMessages = new AuditOnlineMessages();
                        auditOnlineMessages.setRowguid(UUID.randomUUID().toString());
                        auditOnlineMessages.setOperatedate(new Date());
                        auditOnlineMessages.setOperateusername(UserSession.getInstance().getDisplayName());
                        auditOnlineMessages.setYwguid(auditProject.getRowguid());
                        auditOnlineMessages.setInserttime(new Date());
                        auditOnlineMessages.setClientid(auditProject.getStr("openid"));
                        auditOnlineMessages.setTcnote(content);
                        auditOnlineMessages.setType("4");
                        auditOnlineMessages.setSendtime(new Date());
                        iZcCommonService.insert(auditOnlineMessages);
                    }
                    // *************结束*************

                    break;
                case "handlenopass":
                    handleProjectService.handleProjectNotPass(auditProject, userGuid,
                            workItemGuid);
                    // 测试办件不生成
                    if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                        // MQ 环节信息
                        msg = "handleProcess:" + auditProject.getRowguid() + ";" + auditProject.getPviguid()
                                + ";" + workItemGuid + ";"
                                + operation.getOperationGuid() + ";" + auditProject.getAreacode();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                        // 接办分离 环节信息
                        msg = auditProject.getRowguid() + "." + auditProject.getPviguid() + "."
                                + workItemGuid + "."
                                + operation.getOperationGuid();
                        sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                + ZwfwUserSession.getInstance().getAreaCode() + ".process." + auditProject.getTask_id());
                        // MQ 江苏省标
                        msg = auditProject.getRowguid() + "." + userGuid;
                        sendMQMessageService.sendByExchange("exchange_handle", msg,
                                "project." + ZwfwUserSession.getInstance().getAreaCode() + ".handlenotpass."
                                        + auditProject.getTask_id());
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                    + auditProject.getPviguid() + ";"
                                    + workItemGuid + ";"
                                    + operation.getOperationGuid() + ";" + auditProject.getAreacode()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }
                    }

                    // *************开始*************
                    // add by yrchan,2022-04-19,审核不通过，是勘验事项
                    AuditTaskExtension nopassTaskExtension = auditTaskExtensionService
                            .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if (nopassTaskExtension != null
                            && ZwfwConstant.CONSTANT_STR_ONE.equals(nopassTaskExtension.getStr("is_inquest"))) {
                        // 是勘验事项
                        IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                                .getComponent(IMessagesCenterService.class);
                        IZcCommonService iZcCommonService = ContainerFactory.getContainInfo()
                                .getComponent(IZcCommonService.class);

                        // 向短信表（messages_center）插入一条短信记录，短信内容如下：
                        // “[邹城市为民服务中心]xx 您好，您于 2022-03-23 11:32:45
                        // 申请的“农药经营许可”勘验事项，经审核勘验通过，请登录微信小程序“邹城 xxx”进行查看”
                        String dateStr = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                "yyyy-MM-dd HH:mm:ss");
                        String content = auditProject.getApplyername() + " 您好，您于" + dateStr + "申请的“"
                                + auditProject.getProjectname() + "”勘验事项，经审核不符合现场要求，请登录“爱山东”应用云勘验模块进行重新修改”。";

                        iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                                new Date(), auditProject.getContactmobile(), auditProject.getApplyeruserguid(),
                                auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                                UserSession.getInstance().getDisplayName(), "", "", "", false, auditProject.getAreacode());

                        // 向微信消息记录表（audit_online_messages）插入一条微信消息
                        AuditOnlineMessages auditOnlineMessages = new AuditOnlineMessages();
                        auditOnlineMessages.setRowguid(UUID.randomUUID().toString());
                        auditOnlineMessages.setOperatedate(new Date());
                        auditOnlineMessages.setOperateusername(UserSession.getInstance().getDisplayName());
                        auditOnlineMessages.setYwguid(auditProject.getRowguid());
                        auditOnlineMessages.setInserttime(new Date());
                        auditOnlineMessages.setClientid(auditProject.getStr("openid"));
                        auditOnlineMessages.setTcnote(content);
                        auditOnlineMessages.setType("4");
                        auditOnlineMessages.setSendtime(new Date());
                        iZcCommonService.insert(auditOnlineMessages);
                    }
                    // *************结束*************
                    break;
                case "handlefinish":
                    EpointFrameDsManager.begin(null);
                    handleProjectService.handleFinish(auditProject, UserSession.getInstance().getDisplayName(), userGuid,
                            workItemGuid);
                    EpointFrameDsManager.commit();
                    if (auditProject.getSubappguid() != null) {
                        IAuditSpBusiness iauditspbusiness = ContainerFactory.getContainInfo()
                                .getComponent(IAuditSpBusiness.class);
                        AuditSpBusiness business = iauditspbusiness
                                .getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(business.getBusinesstype())) {
                            handleProjectService.handleblspsub(auditProject, UserSession.getInstance().getUserGuid(),
                                    UserSession.getInstance().getDisplayName());
                        }
                    }
                    message = "finish";
                    // 测试办件不生成
                    if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                        // MQ 环节信息
                        msg = "handleProcess:" + auditProject.getRowguid() + ";" + auditProject.getPviguid()
                                + ";" + workItemGuid + ";"
                                + operation.getOperationGuid() + ";" + auditProject.getAreacode();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                        // 接办分离 环节信息
                        msg = auditProject.getRowguid() + "." + auditProject.getPviguid() + "."
                                + workItemGuid + "."
                                + operation.getOperationGuid();
                        sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                + ZwfwUserSession.getInstance().getAreaCode() + ".process." + auditProject.getTask_id());
                        // MQ 江苏省标
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                    + auditProject.getPviguid() + ";"
                                    + workItemGuid + ";"
                                    + operation.getOperationGuid() + ";" + auditProject.getAreacode()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }

                        // MQ 办结操作
                        msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                + UserSession.getInstance().getDisplayName();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");

                        // 接办分离 办结操作
                        msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "."
                                + UserSession.getInstance().getDisplayName();
                        sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                + ZwfwUserSession.getInstance().getAreaCode() + ".sendresult." + auditProject.getTask_id());

                        // MQ 江苏省标
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                    + UserSession.getInstance().getDisplayName()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }
                    }
                    // *************开始**************
                    // 审批办件仅需保留邹城市级办件，无需算入镇街 ，这项筛选条件改为通过办件ouguid字段关联部门拓展表查询areacode
                    // edit by yrchan,2022-04-15,判断该办件辖区是否为370883，若是，则插表评价办件信息表
                    if (StringUtil.isNotBlank(auditProject.getOuguid())) {
                        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
                        FrameOuExtendInfo frameOuExtendInfo = iOuService.getFrameOuExtendInfo(auditProject.getOuguid());

                        if (frameOuExtendInfo != null && "370883".equals(frameOuExtendInfo.getStr("areacode"))) {
                            IEvaluateProjectService iEvaluateProjectService = ContainerFactory.getContainInfo()
                                    .getComponent(IEvaluateProjectService.class);
                            IConfigService iConfigService = ContainerFactory.getContainInfo()
                                    .getComponent(IConfigService.class);
                            IMessagesCenterService iMessagesCenterService = ContainerFactory.getContainInfo()
                                    .getComponent(IMessagesCenterService.class);

                            Date nowDate = new Date();
                            // 5。存放在评价办件信息表（evaluate_project）
                            EvaluateProject evaluateProject = new EvaluateProject();
                            evaluateProject.setRowguid(UUID.randomUUID().toString());
                            evaluateProject.setOperatedate(nowDate);
                            evaluateProject.setOperateusername(UserSession.getInstance().getDisplayName());
                            evaluateProject.setCreat_date(nowDate);
                            evaluateProject.setAccept_user(auditProject.getAcceptusername());
                            evaluateProject
                                    .setAccept_department(iOuService.getOuNameByUserGuid(auditProject.getAcceptuserguid()));
                            evaluateProject.setTask_name(auditProject.getProjectname());
                            evaluateProject.setApply_object(auditProject.getApplyername());
                            evaluateProject.setApply_id(auditProject.getCertnum());
                            evaluateProject.setAccept_date(auditProject.getAcceptuserdate());
                            evaluateProject.setHandle_date(auditProject.getBanjiedate());
                            evaluateProject.setLink_user(auditProject.getContactperson());
                            evaluateProject.setLink_phone(auditProject.getContactmobile());
                            // 是否发送短信：1
                            evaluateProject.setIs_send(ZwfwConstant.CONSTANT_INT_ONE);
                            // 是否评价：0：未评价
                            evaluateProject.setIs_evaluate(ZwfwConstant.CONSTANT_INT_ZERO);
                            // 评价来源：1：导入办件，2：审批办件
                            evaluateProject.setProject_source(ZwfwConstant.CONSTANT_INT_TWO);

                            // 编号
                            evaluateProject.setProject_no(auditProject.getFlowsn());
                            evaluateProject.setProject_guid(auditProject.getRowguid());

                            // 5.判断办结时间为同一天且同一个手机号的评价办件信息是否存在
                            boolean isExistBj = iEvaluateProjectService.isExistPhoneAndHandleDate(
                                    evaluateProject.getLink_phone(), EpointDateUtil.convertDate2String(
                                            evaluateProject.getHandle_date(), EpointDateUtil.DATE_FORMAT));
                            // 若存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,不发短信
                            if (isExistBj) {
                                iEvaluateProjectService.insert(evaluateProject);
                            } else {
                                // 若不存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,并发短信
                                iEvaluateProjectService.insert(evaluateProject);
                                // 获取系统参数：评价短信发送模板
                                String content = iConfigService.getFrameConfigValue("EAVL_MSG_CONTENT");

                                // 发短信
                                String contentText = content.replaceAll("#=TASK_NAME=#", evaluateProject.getTask_name());
                                iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), contentText, nowDate,
                                        0, nowDate, evaluateProject.getLink_phone(), "-", evaluateProject.getLink_user(),
                                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                        evaluateProject.getLink_phone(), UserSession.getInstance().getOuGuid(), "", true,
                                        "370883");
                            }
                            EpointFrameDsManager.commit();
                        }
                    }
                    // *************结束**************

                    message = "finish";
                    break;
                default:
                    handleProjectService.saveProject(auditProject);
                    // 添加工作流操作日志
                    EpointFrameDsManager.begin(null);
//                String pivguid = auditProject.getPviguid();
                    // IWFInstanceAPI9 wf9instance =
                    // ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
//                ProcessVersionInstance pvi = wf9instance.getProcessVersionInstance(pivguid);
                    WorkflowWorkItem workflowWorkItem = wf9instance.getWorkItem(pvi,
                            workItemGuid);
                    handleProjectService.saveOperateLog(auditProject, userGuid, UserSession.getInstance().getDisplayName(),
                            workflowWorkItem.getActivityName() + " ", workflowWorkItem.getOpinion());

                    EpointFrameDsManager.commit();

                    // wfa.getActivityInstanceName()
                    // 测试办件不生成
                    if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                        // MQ 环节信息
                        msg = "handleProcess:" + auditProject.getRowguid() + ";" + auditProject.getPviguid()
                                + ";" + workItemGuid + ";"
                                + operation.getOperationGuid() + ";" + auditProject.getAreacode();
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                        // 接办分离 环节信息
                        msg = auditProject.getRowguid() + "." + auditProject.getPviguid() + "."
                                + workItemGuid + "."
                                + operation.getOperationGuid();
                        sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                + ZwfwUserSession.getInstance().getAreaCode() + ".process." + auditProject.getTask_id());
                        // MQ 江苏省标
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                            msg = "handleProcess:" + auditProject.getRowguid() + ";"
                                    + auditProject.getPviguid() + ";"
                                    + workItemGuid + ";"
                                    + operation.getOperationGuid() + ";" + auditProject.getAreacode()
                                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                            ProducerMQ.sendByExchange("receiveproject", msg, "");
                        }
                    }
                    break;
            }
        }

    }

    /**
     * 批量接件的操作
     *
     * @param params
     */
    public void batchReceiveProject(String params) {
        String[] guids = params.split(",");
        ZwfwUserSession zwfwUserSession = ZwfwUserSession.getInstance();
        // 循环处理各个办件
        for (String paramsGuid : guids) {

            String taskguid = paramsGuid.split(";")[1];
            String projectguid = paramsGuid.split(";")[0];
            AuditTask auditTask = null;
            AuditTaskExtension auditTaskExtension = null;
            // 获取事项信息
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            AuditLogisticsBasicinfo auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
            String areaCode = "";
            AuditProject auditProject = null;
            areaCode = auditTask.getAreacode();
            // 获取办件信息
            String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid,Onlineapplyerguid";
            auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
            //是否同城通办
            String is_samecity = "";
            if (auditTask != null) {
                is_samecity = auditTask.get("is_samecity");
                String IS_SAMECITY = getRequestParameter("is_samecity");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(is_samecity)
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(IS_SAMECITY)) {
                    auditProject.set("is_samecity", is_samecity);
                    AuditProjectSamecity auditProjectSamecity = new AuditProjectSamecity();
                    auditProjectSamecity.setRowguid(UUID.randomUUID().toString());
                    auditProjectSamecity.setProjectguid(auditProject.getRowguid());
                    auditProjectSamecity.setDaibanrenname(userSession.getDisplayName());
                    //如果是同城通办，插入同城通办记录表记录
                    if (auditProjectSamecity == null) {
                        auditProjectSamecity = new AuditProjectSamecity();
                        auditProjectSamecity.setRowguid(UUID.randomUUID().toString());
                        auditProjectSamecity.setOperatedate(new Date());
                        auditProjectSamecity.setDaibanarea(zwfwUserSession.getAreaCode());
                        auditProjectSamecity.setDaibancenterguid(zwfwUserSession.getCenterGuid());
                        if (StringUtil.isNotBlank(zwfwUserSession.getCenterGuid())) {
                            AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                                    .findAuditServiceCenterByGuid(zwfwUserSession.getCenterGuid()).getResult();
                            if (center != null) {
                                auditProjectSamecity.setDaibancentername(center.getCentername());
                            }
                        }
                        auditProjectSamecity.setDaibandeptguid(userSession.getOuGuid());
                        auditProjectSamecity.setDaibandeptname(userSession.getOuName());
                        auditProjectSamecity.setDaibanrenguid(userSession.getUserGuid());
                        auditProjectSamecity.setDaibanrenname(userSession.getDisplayName());
                        auditProjectSamecity.setDanbanrentel(userSession.getMobile());
                        auditProjectSamecity.setProjectguid(auditProject.getRowguid());
                        auditProjectSamecity.setIs_sendmaterial(auditProject.getIf_express());
                        auditProjectSamecity.setFlowsn(auditProject.getFlowsn());
                        auditProjectSamecity.setApplyname(auditProject.getApplyername());
                        auditProjectSamecity.setApplydate(auditProject.getApplydate());
                        auditProjectSamecity.setProjectname(auditProject.getProjectname());
                        auditProjectSamecity.setTaskguid(auditTask.getRowguid());
                        auditProjectSamecity.setTaskid(auditTask.getTask_id());
                        auditProjectSamecity.setTaskname(auditTask.getTaskname());
                        iAuditProjectSamecityService.insert(auditProjectSamecity);
                    } else {
                        AuditProjectSamecity projectSamecity = iAuditProjectSamecityService
                                .getProjectSamecityByProjectguid(auditProject.getRowguid());
                        auditProjectSamecity.setFlowsn(auditProject.getFlowsn());
                        auditProjectSamecity.setApplyname(auditProject.getApplyername());
                        auditProjectSamecity.setApplydate(auditProject.getApplydate());
                        auditProjectSamecity.setProjectname(auditProject.getProjectname());
                        auditProjectSamecity.setTaskguid(auditTask.getRowguid());
                        auditProjectSamecity.setTaskid(auditTask.getTask_id());
                        auditProjectSamecity.setTaskname(auditTask.getTaskname());
                        auditProjectSamecity.setOperatedate(new Date());
                        if (projectSamecity != null) {
                            projectSamecity.putAll(auditProjectSamecity);
                            iAuditProjectSamecityService.update(auditProjectSamecity);
                        } else {
                            iAuditProjectSamecityService.insert(auditProjectSamecity);
                        }
                    }

                }
            }

            auditProject.setCurrentareacode(ZwfwUserSession.getInstance().getAreaCode());
            //保存处理过办件的区域编码(不重复保存)
            String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
            String areaStr = "";
            if (StringUtil.isBlank(auditProject.getHandleareacode())) {
                areaStr = handleAreaCode + ",";
            } else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
                areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
            }
            if (StringUtil.isNotBlank(areaStr)) {
                auditProject.setHandleareacode(areaStr);
            }
            String sqtzaddress = handleProjectService.handleReceive(auditProject, userSession.getDisplayName(),
                    userSession.getUserGuid(), zwfwUserSession.getWindowGuid(), zwfwUserSession.getWindowName())
                    .getResult();

            //向投资监管平台发送mq
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project." + ZwfwUserSession.getInstance().getAreaCode() + ".receive." + auditProject.getTask_id());

            // 使用物流
            if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标", auditLogisticsBasicinfo.getRcv_prov());
                if (StringUtil.isNotBlank(prov)) {
                    auditLogisticsBasicinfo.setRcv_prov(prov);
                }
                String city = iCodeItemsService.getItemTextByCodeName("行政区划国标", auditLogisticsBasicinfo.getRcv_city());
                if (StringUtil.isNotBlank(city)) {
                    auditLogisticsBasicinfo.setRcv_city(city);
                }
                String country = iCodeItemsService.getItemTextByCodeName("行政区划国标",
                        auditLogisticsBasicinfo.getRcv_country());
                if (StringUtil.isNotBlank(country)) {
                    auditLogisticsBasicinfo.setRcv_country(country);
                }
                auditLogisticsBasicinfo.setStatus("0");
                auditLogisticsBasicinfo.setCallState("05");// 初始化状态
                auditLogisticsBasicinfo.setNet_type("2");
                auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
                auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
                auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
                if (StringUtil.isNotBlank(auditLogisticsBasicinfo.getProjectguid())) {
                    handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
                } else {
                    handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
                }
            }
            try {
//          阳佳      JSONObject jsonBack = JSONObject.parseObject(ButtonControl());
//                jsonBack.put("message", sqtzaddress);
                addCallbackParam("sqtzaddress", sqtzaddress);
//                addCallbackParam("message", jsonBack.toString());
//                addCallbackParam("isword", flag ? "1" : "0");
                addCallbackParam("messageitemguid", "undefined".equals(getRequestParameter("MessageItemGuid")) ? ""
                        : getRequestParameter("MessageItemGuid"));
            } catch (JSONException e) {
            }

            // 如果存在待办事宜，则删除待办
            List<FrameUser> users = new ArrayList<FrameUser>();

            List<MessagesCenter> messagesCenterList = new ArrayList<MessagesCenter>();
            List<MessagesCenter> messagesList = new ArrayList<MessagesCenter>();
            if (StringUtil.isNotBlank(zwfwUserSession.getWindowGuid())) {
                users = iAuditOrgaWindow.getFrameUserByTaskID(auditProject.getTask_id()).getResult();
                for (FrameUser user : users) {
                    if (user == null) {
                        continue;
                    }
                    messagesList = messageCenterService.queryForList(user.getUserGuid(), null, null, "",
                            IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                            -1);
                    if (messagesList != null && messagesList.size() > 0) {
                        messagesCenterList.addAll(messagesList);
                    }
                }
            } else {
                messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                        IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
            }
            if (messagesCenterList != null && messagesCenterList.size() > 0) {
                for (MessagesCenter messagescenter : messagesCenterList) {
                    messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
                }
            }
            // 若 不使用一窗，则发送待办事宜
            boolean useYcsl = ycslService.useYcslByTaskExtensionObj(auditTaskExtension);
            logger.info("useYcsl=" + useYcsl);
            // 发送待办事宜
            if (!useYcsl) {
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(is_samecity)) {
                    String title = "【待受理】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                    String messageItemGuid = UUID.randomUUID().toString();
                    String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                            ? auditTaskExtension.getCustomformurl() : ZwfwConstant.CONSTANT_FORM_URL;
                    String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                            + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                    messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                            IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                            userSession.getUserGuid(), userSession.getDisplayName(), "待受理", handleUrl, userSession.getOuGuid(),
                            "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                            auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                            userSession.getUserGuid(), "", "");
                    addCallbackParam("title", title);
                    // 先判断是否启用江苏通用
                    String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                        // 再判断外网办件
                        if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                                || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                            // 接件只需要调用星网的接口
                            auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                    .getResult();
                            String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                            // 只有在配置的情况下才能进行请求
                            if (StringUtil.isNotBlank(url)) {
                                url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfodetail?declareid="
                                        + auditTask.getTask_id() + "&centerguid=" + auditProject.getCenterguid() + "&taskguid="
                                        + taskguid + "&flowsn=" + auditProject.getFlowsn();

                                Record record = StarProjectInteractUtil.updateOrInsertStar(auditProject.getFlowsn(),
                                        ZwdtConstant.STAR_PROJECT_JJ,
                                        StringUtil.isBlank(auditTask.getDept_yw_reg_no()) ? auditTask.getItem_id()
                                                : auditTask.getDept_yw_reg_no(),
                                        auditTask.getTaskname(), auditProject.getContactperson(),
                                        auditProject.getContactcertnum(), "查看办件", url);
                                iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                            }
                        }
                    }
                }
            } else {
                // 使用一窗的情况下，推送办件信息到自建系统
                pushReceiveInfo2SelfBuildSystem(auditProject, auditTask, auditTaskExtension);
            }
            //生成施工许可表单实体
            AuditProjectFormSgxkz auditProjectFormSgxkz = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
            if (auditProjectFormSgxkz == null) {
                auditProjectFormSgxkz = new AuditProjectFormSgxkz();
                auditProjectFormSgxkz.setRowguid(UUID.randomUUID().toString());
                auditProjectFormSgxkz.setProjectguid(projectguid);
                auditProjectFormSgxkz.setOperatedate(new Date());
                auditProjectFormSgxkz.setOperateusername(userSession.getDisplayName());
            }
            if (auditProjectFormSgxkz != null && StringUtil.isNotBlank(auditProjectFormSgxkz.getXiangmumingchen())) {
                //System.err.println(auditProjectFormSgxkz);
                AuditProjectFormSgxkz auditProjectFormSgxkz1 = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                if (auditProjectFormSgxkz1 != null) {
                    iAuditProjectFormSgxkzService.update(auditProjectFormSgxkz);
                } else {
                    iAuditProjectFormSgxkzService.insert(auditProjectFormSgxkz);
                }
            }
        }
    }

    /**
     * 批量预审通过的方法
     *
     * @param params
     */
    public void batchYstg(String params) {
        String[] guids = params.split(",");
        for (String paramsGuid : guids) {
            String taskguid = paramsGuid.split(";")[1];
            String projectguid = paramsGuid.split(";")[0];
            AuditTask auditTask = null;
            AuditTaskExtension auditTaskExtension = null;
            // 获取事项信息
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            AuditLogisticsBasicinfo auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
            String areaCode = "";
            AuditProject auditProject = new AuditProject();
            areaCode = auditTask.getAreacode();
            // 获取办件信息
            String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid,Onlineapplyerguid";
            auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                if ("1".equals(auditProject.getIf_express_ma())) {// 如果办件有申报材料物流信息则需要呼叫快递
                    Boolean result = call(auditProject);
                    if (!result) {
                        return;
                    }
                }
            }
            int banjianStatus = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
            if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_WWYSTU
                        || auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
                    addCallbackParam("close", "1");
                }
                addCallbackParam("message", "该步骤已经处理完成，请勿重复操作！");
                return;
            }
            String banjianTitle = "预审通过";
            // 1、插入每日一填的记录
            projectNumberService.addProjectNumber(auditProject, false, userSession.getUserGuid(),
                    userSession.getDisplayName());
            // 2.插入在线通知
            String notirytitle = "【" + banjianTitle + "】" + "<" + auditProject.getProjectname() + ">";
            projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
                    notirytitle, "材料齐全、符合条件，预审通过！", "", ZwfwConstant.CLIENTTYPE_BJ, auditProject.getRowguid(),
                    String.valueOf(banjianStatus), userSession.getUserGuid(), userSession.getDisplayName());

            // 3.发送待办事宜
            AuditCommonResult<AuditTask> taskResult = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true);
            AuditTask task = taskResult.getResult();
            String messageItemGuid = UUID.randomUUID().toString();
            String title = "【待接件】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                    ? auditTaskExtension.getCustomformurl()
                    : ZwfwConstant.CONSTANT_FORM_URL;
            String handleUrl = formUrl + "?processguid=" + task.getProcessguid() + "&taskguid=" + auditProject.getTaskguid()
                    + "&projectguid=" + auditProject.getRowguid();
            messageCenterService.insertWaitHandleMessage(messageItemGuid, title, IMessagesCenterService.MESSAGETYPE_WAIT,
                    userSession.getUserGuid(), userSession.getDisplayName(), userSession.getUserGuid(),
                    userSession.getDisplayName(), "待接件", handleUrl, userSession.getOuGuid(), "",
                    ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                    auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getRowguid(),
                    userSession.getUserGuid(), "", "");

            //预审通过以后  预审通过以后给外网发送代办
            String applyerGuid = "";
            int type = auditProject.getApplyertype();
            //个人
            if (type == 20) {
                AuditOnlineIndividual auditonlineindividual = auditOnlieIndividualService.getIndividualByApplyerGuid(auditProject.getApplyeruserguid()).getResult();
                if (auditonlineindividual != null) {
                    applyerGuid = auditonlineindividual.getAccountguid();
                }
            }
            //法人
            else {
                applyerGuid = auditProject.getOnlineapplyerguid();
            }
            String openUrl = configService.getFrameConfigValue("zwdtMsgurl") + "/epointzwmhwz/pages/myspace/detail?projectguid=" + auditProject.getRowguid()
                    + "&tabtype=5&taskguid=" + auditProject.getTaskguid() + "&taskcaseguid=" + auditProject.getTaskcaseguid();
            String dtTitle = "【预审通过】" + auditProject.getProjectname();
            if (StringUtil.isNotBlank(applyerGuid)) {
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), dtTitle, IMessagesCenterService.MESSAGETYPE_WAIT, applyerGuid, "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                        "", openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }

            //删除外网发送的代办
            handleProjectService.delProjectMessage(auditProject.getTask_id(), auditProject.getRowguid());

            String isYS = "";
            auditProject.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            handleProjectService.handleYstg(auditProject, userSession.getDisplayName(), userSession.getUserGuid())
                    .getResult();
            isYS = "10";
            // 添加操作纪录
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", auditProject.getRowguid());
            sql.eq("operatetype", isYS);
            AuditProjectOperation auditProjectOperation = auditProjectOperationService
                    .getAuditOperationByCondition(sql.getMap()).getResult();
            if (auditProjectOperation != null) {
                auditProjectOperation.setRemarks("材料齐全、符合条件，预审通过！");
                auditProjectOperationService.updateAuditProjectOperation(auditProjectOperation);
            }

            // 办件对接
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                // 预审通过后就需要更新窗口名称
                auditProject.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
                auditProject.setWindowname(ZwfwUserSession.getInstance().getWindowName());
                auditProjectServcie.updateProject(auditProject);
                if ("1".equals(auditProject.getIf_express_ma())) {
                    // 查询物流表
                    AuditLogisticsBasicinfo auditlogisticsbasicinfo = iAuditLogisticsBasicInfo
                            .getAuditLogisticsBasicinfoByProjectGuid(auditProject.getRowguid()).getResult();
                    // 无需判断是否外网办件，在这边只要网auditonlineexpress 表中插入相关数据就好
                    AuditOnlineExpress auditonlineexpress = new AuditOnlineExpress();
                    auditonlineexpress.setRowguid(UUID.randomUUID().toString());
                    auditonlineexpress.setProjectguid(auditProject.getRowguid());
                    auditonlineexpress.setFlowsn(auditProject.getFlowsn());
                    auditonlineexpress.setInserttime(new Date());
                    auditonlineexpress.setIssend("0");
                    auditonlineexpress.setCenterguid(auditProject.getCenterguid());
                    auditonlineexpress.setAreacode(auditProject.getAreacode());
                    auditonlineexpress.setWindowGuid(ZwfwUserSession.getInstance().getCenterGuid());
                    auditonlineexpress.setWindowName(ZwfwUserSession.getInstance().getWindowName());
                    auditonlineexpress.setWindowNo(ZwfwUserSession.getInstance().getWindowNo());
                    auditonlineexpress.setLogisticsguid(auditlogisticsbasicinfo.getRowguid());
                    iAuditOnlineExpress.addAuditOnlineExpress(auditonlineexpress);
                }
                String url = "";
                url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 没有配置不走接口调用
                if (StringUtil.isNotBlank(url)) {
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfoedit?declareid=" + task.getTask_id()
                            + "&centerguid=" + auditProject.getCenterguid() + "&taskguid=" + task.getRowguid() + "&flowsn="
                            + auditProject.getFlowsn();
                    // 星网推送状态
                    Record record = StarProjectInteractUtil
                            .updateOrInsertStar(auditProject.getFlowsn(), ZwdtConstant.STAR_PROJECT_YSTG,
                                    StringUtil.isBlank(task.getDept_yw_reg_no()) ? task.getItem_id()
                                            : task.getDept_yw_reg_no(),
                                    task.getTaskname(), auditProject.getContactperson(), auditProject.getContactcertnum(),
                                    "物流确认", url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);

                    // 预审通过以后 需要发送消息
                    String strContent = auditProject.getContactperson() + "你好，您于"
                            + EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy年MM月dd日 HH:mm")
                            + "在网上申请的“" + auditProject.getProjectname() + "”，预审已获通过，请您尽快登录江苏政务服务网确认您的物流信息";//短信内容
                    messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), strContent, new Date(), 0, null,
                            auditProject.getContactmobile(), auditProject.getContactmobile(), "", "", "", "", "", null,
                            false, "短信");
                    // 调用大汉统一消息接口
                    try {
                        Record recorddh = DHSendMsgUtil.PostDHMessage("您申请的办件有了新的进度!",
                                auditProject.getContactperson() + "你好，您于"
                                        + EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                        "yyyy年MM月dd日 HH:mm")
                                        + "在网上申请的“" + auditProject.getProjectname() + "”，预审已获通过，请您尽快在江苏政务服务网确认收件地址。",
                                auditProject.getContactcertnum());
                        iAuditApplyJsLog.insertDHAuditApplyJsLog(recorddh);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 呼叫快递
     */
    public Boolean call(AuditProject auditProject) {
        // 系统参数
        String AS_CALLMODE = handleConfigService.getFrameConfig("AS_CALLMODE", auditProject.getCenterguid()).getResult();// 默认业务窗口呼叫模式
        String AS_ZWFWZX_CODE = handleConfigService.getFrameConfig("AS_ZWFWZX_CODE", auditProject.getCenterguid()).getResult();
        String AS_EMSWINDOW_CALL = handleConfigService.getFrameConfig("AS_EMSWINDOW_CALL", auditProject.getCenterguid())
                .getResult();
        Map<String, String> conditionMap = new HashMap<String, String>(16);
        conditionMap.put("projectguid = ", auditProject.getRowguid());
        conditionMap.put("NET_TYPE = ", "1");
        List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
        if (list != null && !list.isEmpty()) {
            for (AuditLogisticsBasicinfo auditlogisticsbasicinfo : list) {
                dataBean = auditlogisticsbasicinfo;
            }
        }
        if (StringUtil.isBlank(AS_ZWFWZX_CODE)) {
            addCallbackParam("message", "请配置中心编码");
            return false;
        }

        if (StringUtil.isBlank(dataBean.getChk_code())) {
            addCallbackParam("message", "短信验证码未生成");
            return false;
        }

        Map<String, String> conditionMap_ma = new HashMap<String, String>(16);
        conditionMap_ma.put("ems_ord_no =", dataBean.getEms_ord_no());
        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
            conditionMap_ma.put("rowguid !=", dataBean.getRowguid());
        }
        List<AuditLogisticsBasicinfo> list_ma = iAuditLogisticsBasicInfo
                .getAuditLogisticsBasicinfoList(conditionMap_ma, null, null).getResult();
        if (list_ma != null && !list_ma.isEmpty()) {
            addCallbackParam("message", "信封流水号已存在请修改");
            return false;
        }
        dataBean.setRcv_name(
                ZwfwUserSession.getInstance().getWindowName() + "-" + ZwfwUserSession.getInstance().getWindowNo());
        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());// 中心标识
        dataBean.setXiaqucode(ZwfwUserSession.getInstance().getAreaCode());// 辖区编码
        dataBean.setGov_tb_name(
                ZwfwUserSession.getInstance().getWindowName() + "-" + ZwfwUserSession.getInstance().getWindowNo());// 窗口号
        dataBean.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());// 窗口标识
        dataBean.setFlowsn(auditProject.getFlowsn());
        dataBean.setCallState("02");// 呼叫中
        // 如果是EMS专窗呼叫模式 先保存基本信息
        if ("1".equals(AS_CALLMODE)) {
            if (StringUtil.isBlank(AS_EMSWINDOW_CALL)) {
                addCallbackParam("message", "请配置ems专窗联系电话");
                return false;
            } else {
                dataBean.setRcv_phone(AS_EMSWINDOW_CALL);
            }

        } else {// 业务窗口呼叫模式 保存基本信息并发送MQ
            AuditOrgaWindow auditOrgWindow = iAuditOrgaWindow
                    .getWindowByWindowGuid(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
            if (StringUtil.isBlank(auditOrgWindow.getTel())) {
                addCallbackParam("message", "请配置对应窗口联系电话");
                return false;
            } else {
                dataBean.setRcv_phone(auditOrgWindow.getTel());
            }
        }

        if (StringUtil.isBlank(dataBean.getRowguid())) {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            // 不在呼叫快递的时候生成验证码
            // dataBean.setChk_code(String.valueOf((int)((Math.random()*9+1)*100000)));
            iAuditLogisticsBasicInfo.addLogisticsBasicinfo(dataBean);
        } else {
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            iAuditLogisticsBasicInfo.updateLogisticsBasicinfo(dataBean);
        }
        // 发送MQ
        try {
            ProducerMQ.send(ZwfwConstant.QUEEN_NAME_CALLEMS, "callems:" + dataBean.getRowguid() + ";" + AS_ZWFWZX_CODE
                    + "/com.epoint.auditjob.rabbitmqhandle.MQHandleEMSJiangSu");
            addCallbackParam("message", "预审通过，快递已呼叫，请至待收取列表中查看");
            return true;
        } catch (Exception e) {
            dataBean.setCallState("04");// 呼叫失败
            iAuditLogisticsBasicInfo.updateLogisticsBasicinfo(dataBean);
            addCallbackParam("message", "预审通过，呼叫申报材料快递失败，请至待收取列表中查看并再次呼叫");
            return false;
        }
    }

    /**
     * 预审打回
     *
     * @param params
     */
    public void batchYsdh(String params) {
        String[] guids = params.split(",");
        for (String paramsGuid : guids) {
            String taskguid = paramsGuid.split(";")[1];
            String projectguid = paramsGuid.split(";")[0];
            AuditTask auditTask = null;
            AuditTaskExtension auditTaskExtension = null;
            // 获取事项信息
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            AuditLogisticsBasicinfo auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
            String areaCode = "";
            AuditProject auditProject = new AuditProject();
            areaCode = auditTask.getAreacode();
            // 获取办件信息
            String fields = " rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,certrowguid,receiveuserguid,Onlineapplyerguid";
            auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode).getResult();
            String isPass = getRequestParameter("isPass");
            String message = "";
            String title = "";
            String applyerGuid = "";
            String openUrl = "";

            message = publicMethod(ZwfwConstant.BANJIAN_STATUS_WWYSTU, "预审打回", "", reason, auditProject);
            //大厅发代办标题
            title = "【预审退回】" + auditProject.getProjectname();
            openUrl = configService.getFrameConfigValue("zwdtMsgurl") + "/epointzwmhwz/pages/onlinedeclaration/declarationinfo?projectguid=" + auditProject.getRowguid()
                    + "&taskguid=" + auditProject.getTaskguid() + "&taskcaseguid=" + auditProject.getTaskcaseguid() + "&centerguid=" + auditProject.getCenterguid();


            //大厅发代办
            int type = auditProject.getApplyertype();
            //个人
            if (type == 20) {
                AuditOnlineIndividual auditonlineindividual = auditOnlieIndividualService.getIndividualByApplyerGuid(auditProject.getApplyeruserguid()).getResult();
                if (auditonlineindividual != null) {
                    applyerGuid = auditonlineindividual.getAccountguid();
                }
            }
            //法人
            else {
                applyerGuid = auditProject.getOnlineapplyerguid();
            }
            if (StringUtil.isNotBlank(applyerGuid)) {
                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title, IMessagesCenterService.MESSAGETYPE_WAIT, applyerGuid, "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                        "", openUrl, UserSession.getInstance().getOuGuid(), UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
            }

            //删除外网发送的代办
            handleProject.delProjectMessage(auditProject.getTask_id(), auditProject.getRowguid());
        }
    }

    /**
     * 公共方法
     *
     * @param banjianStatus
     * @param banjianTitle
     * @param handurl
     * @param reason
     * @return
     */
    public String publicMethod(int banjianStatus, String banjianTitle, String handurl, String reason, AuditProject auditProject) {
        // 1、插入异常操作信息
        projectUnusualService.insertProjectUnusual(userSession.getUserGuid(), userSession.getDisplayName(),
                auditProject, 5, "", reason);
        // 2、插入每日一填的记录
        projectNumberService.addProjectNumber(auditProject, false, userSession.getUserGuid(),
                userSession.getDisplayName());
        //3.插入在线通知
        String notirytitle = "【" + banjianTitle + "】" + "<" + auditProject.getProjectname() + ">";
        projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
                notirytitle, reason, handurl, ZwfwConstant.CLIENTTYPE_BJ, auditProject.getRowguid(),
                String.valueOf(banjianStatus), userSession.getUserGuid(), userSession.getDisplayName());
        String message = "";
        if (ZwfwConstant.BANJIAN_STATUS_WWYSTG == banjianStatus) {
            // 4.发送待办事宜
            AuditCommonResult<AuditTask> taskResult = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true);
            AuditTask task = taskResult.getResult();
            String messageItemGuid = UUID.randomUUID().toString();
            String title = "【待接件】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            String clientTag = "";
            if (auditProject.getApplyeruserguid() != null) {
                clientTag = auditProject.getRowguid().substring(0, 1);
            }
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                    ? auditTaskExtension.getCustomformurl() : ZwfwConstant.CONSTANT_FORM_URL;
            String handleUrl = formUrl + "?processguid=" + task.getProcessguid() + "&taskguid="
                    + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
            messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                    IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                    userSession.getUserGuid(), userSession.getDisplayName(), "待接件", handleUrl, userSession.getOuGuid(),
                    "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(), clientTag, new Date(),
                    auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            auditProject.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            message = handleProject
                    .handleYstgAddreason(auditProject, userSession.getDisplayName(), userSession.getUserGuid(), reason)
                    .getResult();
        } else {
            AuditCommonResult<AuditTask> taskResult = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true);
            AuditTask task = taskResult.getResult();
            message = handleProject
                    .handleYsdhAddreason(auditProject, userSession.getDisplayName(), userSession.getUserGuid(), reason)
                    .getResult();
            // 先判断是否江苏通用
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 判断是否配置
                if (StringUtil.isNotBlank(url)) {
                    // 调用大汉统一消息接口
                    try {
                        Record recorddh = DHSendMsgUtil.PostDHMessage("您申请的办件有了新的进度!",
                                auditProject.getContactperson() + "你好，您于"
                                        + EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                        "yyyy年MM月dd日 HH:mm")
                                        + "在网上申请的“" + auditProject.getProjectname() + "”，预审未通过，请您尽快在江苏政务服务网进行确认。",
                                auditProject.getContactcertnum());
                        iAuditApplyJsLog.insertDHAuditApplyJsLog(recorddh);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfo?declareid=" + task.getTask_id()
                            + "&centerguid=" + auditProject.getCenterguid() + "&taskguid=" + task.getRowguid()
                            + "&flowsn=" + auditProject.getFlowsn();
                    // 星网推送状态
                    Record record = StarProjectInteractUtil
                            .updateOrInsertStar(auditProject.getFlowsn(), ZwdtConstant.STAR_PROJECT_YSBTG,
                                    StringUtil.isBlank(task.getDept_yw_reg_no()) ? task.getItem_id()
                                            : task.getDept_yw_reg_no(),
                                    task.getTaskname(), auditProject.getContactperson(),
                                    auditProject.getContactcertnum(), "继续申报", url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                }
            }
        }
        return message;
    }

    /**
     * 流程流转至下一步的通用方法
     *
     * @param processGuid
     * @param pviguid
     * @param workItemGuid
     * @param userGuid
     * @param data
     * @param operationGuid
     * @return
     */
    public WorkflowResult9 goToNextWorkflow(String processGuid, String pviguid, String workItemGuid, String userGuid, JSONObject data, String operationGuid) {
        // 初始化
        IWFEngineAPI9 engineapi = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
        // 设置元素
        WorkflowParameter9 param = new WorkflowParameter9();
        param.setProcessGuid(processGuid);
        param.setProcessVersionInstanceGuid(pviguid);
        param.setWorkItemGuid(workItemGuid);
        param.setSendGuid(userGuid);
        param.setOperateType(WorkflowKeyNames9.OperationType_Pass);
        param.setOperationGuid(operationGuid);
        param.setOpinion("处理完成");
        String note = data.getString("note");
        IWFDefineAPI9 defineapi = ContainerFactory.getContainInfo().getComponent(IWFDefineAPI9.class);
        IWFInstanceAPI9 instanceapi = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(pviguid);
        List<WorkflowActivity> activityList = defineapi.getActListByPVGuid(pvi);
        WorkflowActivity nextActivity = null;
        String nextActivityName = data.getString("nextactivityname");
        if (StringUtil.isNotBlank(nextActivityName)) {
            for (WorkflowActivity workflowActivity : activityList) {
                if (nextActivityName.equals(workflowActivity.getActivityName())) {
                    nextActivity = workflowActivity;
                }
            }
            Map<String, String> transactors = null;
            /*     if (!"handleaccept".equals(note)) {*/
            transactors = new HashMap<>();
            transactors.put(data.getString("transactorguid"), data.getString("transactorname"));
            /* }*/
            List<WorkflowParameterActivity9> list = getTransactor(transactors, nextActivity.getActivityGuid());
            param.setWorkFlowParameterActivity(list);

        }
        return engineapi.operate(param.ConvertToJson().toString());

    }

    /**
     * 获取流程处理人
     *
     * @param transactors      流程处理人userguid,name
     * @param nextActivityGuid 下一步流程guid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<WorkflowParameterActivity9> getTransactor(Map<String, String> transactors, String nextActivityGuid) {
        List<WorkflowParameterActivity9> listWorkflowParameterActivity9 = new ArrayList<>();
        WorkflowParameterActivity9 activity = new WorkflowParameterActivity9();
        List<WorkflowTransactor9> listWorkflowTransactor9 = new ArrayList<>();
        if (transactors != null) {
            for (Map.Entry<String, String> entry : transactors.entrySet()) {
                WorkflowTransactor9 workflowtransactor9 = new WorkflowTransactor9();
                workflowtransactor9.setTransactor(entry.getKey());
                workflowtransactor9.setTransactorName(entry.getValue());
                listWorkflowTransactor9.add(workflowtransactor9);
            }
        }
        activity.setNextActivityGuid(nextActivityGuid);
        activity.setTransactorlst(listWorkflowTransactor9);
        listWorkflowParameterActivity9.add(activity);
        return listWorkflowParameterActivity9;
    }

    /**
     * 下一步处理人
     *
     * @param userguids
     * @return
     */
    private JSONObject convertToEngineData(String[] userguids) {
        JSONObject jsonobject = new JSONObject();
        JSONArray list = new JSONArray();
        for (int index = 0; index < userguids.length; index++) {
            String item = userguids[index];
            if (StringUtil.isNotBlank(item)) {
                WorkflowUser user = orgaService.getUserByUserField("userguid", item);
                if (user != null) {
                    Map<String, Object> useritem = new HashMap<String, Object>();
                    useritem.put("handlerguid", user.getUserGuid());
                    useritem.put("handlername", user.getDisplayName());
                    useritem.put("ouguid", user.getOuGuid());
                    list.add(useritem);
                }
            }
        }
        jsonobject.put("nextsteplist", list);
        return jsonobject;
    }

    /**
     * [推送好差评办件服务数据]
     *
     * @param auditTask
     * @param auditProject
     * @param serviceNumber
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void turnhcpevaluate(AuditProject auditProject, int serviceNumber, String servicename, String newserviceTime) {
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        if (auditTask != null) {
            JSONObject json = new JSONObject();
            String ouguid = auditProject.getOuguid();

            String taskType = auditTask.getShenpilb();
            // 公共服务类型不一致，优先转换
            if ("11".equals(taskType)) {
                taskType = "20";
            }
            switch (taskType) {
                case "01":
                case "05":
                case "07":
                case "08":
                case "09":
                case "10":
                case "20":
                    break;
                default:
                    taskType = "99";
                    break;
            }

            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(ouguid);
            String deptcode = "";
            if (frameOuExten != null) {
                deptcode = frameOuExten.getStr("orgcode");
                if (StringUtil.isBlank(deptcode)) {
                    deptcode = "11370900MB28449441";
                }
            } else {
                deptcode = "11370900MB28449441";
            }
            json.put("taskCode", auditTask.getItem_id());
            json.put("areaCode", auditProject.getAreacode().replace("370882", "370812") + "000000");
            json.put("taskName", auditTask.getTaskname());
            json.put("projectNo", auditProject.getFlowsn());
            String proStatus = serviceNumber + "";
            String acceptdate = EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss");
            json.put("proStatus", proStatus);
            json.put("orgcode", auditProject.getAreacode().replace("370882", "370812") + "000000_" + deptcode);
            json.put("ouguid", ouguid);
            // json.put("orgcode", "370900000000_11370900004341048Y");
            FrameOu frameOu = ouservice.getOuByOuGuid(ouguid);
            if (frameOu != null) {
                json.put("orgName", frameOu.getOuname());
            }

            json.put("acceptDate", acceptdate);
            Integer applyertype = auditProject.getApplyertype();
            if (applyertype == 10) {
                applyertype = 2;
            } else if (applyertype == 20) {
                applyertype = 1;
            } else if (applyertype == 30) {
                applyertype = 9;
            } else {
                applyertype = 9;
            }
            json.put("userProp", applyertype);
            json.put("userName", StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无");
            json.put("userPageType", "111");
            json.put("proManager", StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername().trim() : "无");
            json.put("certKey", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
            json.put("certKeyGOV", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
            json.put("serviceName", servicename);// 环节名称

            json.put("serviceNumber", serviceNumber);
            if (StringUtil.isNotBlank(newserviceTime)) {
                json.put("serviceTime", newserviceTime);
            } else {
                json.put("serviceTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("projectType", auditProject.getTasktype());
            if (3 == Integer.parseInt(proStatus)) {
                json.put("resultDate",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("taskType", auditProject.getTasktype());
            json.put("mobile", StringUtil.isNotBlank(auditProject.getContactmobile()) ? auditProject.getContactmobile().trim() : "0");
            json.put("deptCode", deptcode);
            json.put("projectName", "关于" + auditProject.getApplyername().trim() + auditTask.getTaskname() + "的业务");
            json.put("creditNum", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
            // 默认证照类型为身份证
            json.put("creditType", "111");
            json.put("promiseDay", auditTask.getPromise_day() + "");
            // 默认办结时间单位为工作日
            json.put("anticipateDay", "1");
            json.put("subMatter", "窗口");
            json.put("month", "ck");
            // 线上评价为1
            json.put("proChannel", "2");
            json.put("promiseTime", auditTask.getType() + "");
            JSONObject submit = new JSONObject();
            submit.put("params", json);

            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject jsonobject = JSONObject.parseObject(resultsign);
                JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
                if ("200".equals(jsonstatus.get("code").toString())) {
                    JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                    if ("1".equals(jsoncustom.get("code").toString())) {

                    } else
                        logger.info("保存办件服务数据失败：" + auditProject.getFlowsn() + "，原因：" + resultsign);
                } else {
                    logger.info("保存办件服务数据失败：" + auditProject.getFlowsn() + "，原因：" + resultsign);
                }
            } else {
            }
        } else {
            logger.info("事项查询失败：" + auditProject.getFlowsn());
        }
    }

    /**
     * 推送接件/受理信息给浪潮
     *
     * @author 徐本能
     */
    private Boolean pushInfolc(AuditTask auditTask, AuditProject auditProject) {
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        logger.info("推送数据到浪潮");
        ycslData.put("useSelfBuildSystem", true);
        Boolean success = ycslService.ycsllcbyprojectandtask(auditTask, auditProject);
        ycslData.put("success", success);

        addCallbackParam("ycslData", ycslData.toJSONString());
        return success;
    }

    /**
     * 推送房产基本信息、买方、卖方信息
     */
    public Boolean turnfcitembase(AuditProject auditProject, AuditTask auditTask) {
        String projectguid = auditProject.getRowguid();
        Record record = iJnProjectService.DzbdItemBaseinfoByProjectGuid(projectguid);
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        logger.info("推送数据到房产系统");
        ycslData.put("useSelfBuildSystem", true);
        if (record != null) {
            String LSH = auditProject.getFlowsn();
            //业务类型
            String YWLX = record.getStr("YWLX");
            //业务总面积
            String YWZMJ = record.getStr("YWZMJ");
            //房产价值
            String FCJZ = record.getStr("FCJZ");
            //房屋坐落
            String FWZL = record.getStr("FWZL");
            //产权人姓名
            String CQRLX = record.getStr("CQRLX");
            //附属房屋面积
            String FSFWMJ = record.getStr("FSFWMJ");
            //买方姓名
            String MFXM = record.getStr("MFXM");
            //买方身份证明号
            String MFSFZMH = record.getStr("MFSFZMH");
            //买方证件类型名称
            String MFZJLXMC = record.getStr("MFZJLXMC");
            //买方类型
            String MFLX = record.getStr("MFLX");
            //买方间关系
            String MFJGX = record.getStr("MFJGX");
            //买方共有情况
            String MFGYQK = record.getStr("MFGYQK");
            //共有份额
            String GYFE = record.getStr("GYFE");
            //买方联系电话
            String MFLXDH = record.getStr("MFLXDH");
            //买方住址
            String MFZZ = record.getStr("MFZZ");
            //买方户籍所在地
            String MFHJSZD = record.getStr("MFHJSZD");
            //买方性别
            String MFXB = record.getStr("MFXB");
            //买方生日
            Date MFSR = record.getDate("MFSR");
            //卖方姓名
            String MAIFXM = record.getStr("MAIFXM");
            //卖方身份证明号
            String MAIFSFZMH = record.getStr("MAIFSFZMH");
            //卖方证件类型名称
            String MAIFZJLXMC = record.getStr("MAIFZJLXMC");
            //卖方性别
            String MAIFXB = record.getStr("MAIFXB");
            //卖方类型
            String MAIFLX = record.getStr("MAIFLX");
            //卖方生日
            Date MAIFSR = record.getDate("MAIFSR");
            //卖方住址
            String MAIFZZ = record.getStr("MAIFZZ");
            //卖方联系电话
            String MAIFLXDH = record.getStr("MAIFLXDH");
            //卖方间关系
            String MAIFJGX = record.getStr("MAIFJGX");
            //卖方共有情况
            String MAIFGYQK = record.getStr("MAIFGYQK");
            //卖方共有份额
            String MAIFGYFE = record.getStr("MAIFGYFE");
            //卖方户籍所在地
            String MAIFHJSZD = record.getStr("MAIFHJSZD");
            String taskname = "";
            String taskcode = "";
            if (auditTask != null) {
                taskname = auditTask.getTaskname();
                taskcode = auditTask.getItem_id();
            }
            try {
                EpointFrameDsManager.begin(null);
                //卖方类型
                jbxxb jbxxb = new jbxxb();
                jbxxb.set("rowguid", UUID.randomUUID().toString());
                jbxxb.setLsh(LSH);
                jbxxb.setYwlx(YWLX);
                jbxxb.setZmj(YWZMJ);
                jbxxb.setFcjz(FCJZ);
                jbxxb.setFwzl(FWZL);
                jbxxb.setCqrxm(CQRLX);
                jbxxb.setFsfwmj(FSFWMJ);
                jbxxb.set("taskname", taskname);
                jbxxb.set("taskcode", taskcode);

                csfxxb csfxxb = new csfxxb();
                csfxxb.set("rowguid", UUID.randomUUID().toString());
                csfxxb.setLsh(LSH);
                csfxxb.setXm(MAIFXM);
                csfxxb.setZjhm(MAIFSFZMH);
                csfxxb.setZjlx(MAIFZJLXMC);
                csfxxb.setXh(1);
                csfxxb.setMflx(MAIFLX);
                csfxxb.setGyqk(MAIFGYQK);
                csfxxb.setGyfe(MAIFGYFE);
                csfxxb.setLxdh(MAIFLXDH);
                csfxxb.setZz(MAIFZZ);
                csfxxb.setHj(MAIFHJSZD);
                csfxxb.setXb(MAIFXB);
                //csfxxb.setSr(MAIFSR);
                csfxxb.setMfgx(MAIFJGX);


                msfxxb msfxxb = new msfxxb();
                msfxxb.set("rowguid", UUID.randomUUID().toString());
                msfxxb.setLsh(LSH);
                msfxxb.setXm(MFXM);
                msfxxb.setZjhm(MFSFZMH);
                msfxxb.setZjlx(MFZJLXMC);
                msfxxb.setXh(1);
                msfxxb.setMflx(MFLX);
                msfxxb.setGyqk(MFGYQK);
                msfxxb.setGyfe(GYFE);
                msfxxb.setLxdh(MFLXDH);
                msfxxb.setZz(MFZZ);
                msfxxb.setHj(MFHJSZD);
                msfxxb.setXb(MFXB);
                //msfxxb.setSr(MFSR);
                msfxxb.setMfgx(MFJGX);
                //添加记录
                iJnProjectService.insertJbxxb(jbxxb);
                iJnProjectService.insertCsfxxb(csfxxb);
                iJnProjectService.insertMsfxxb(msfxxb);

                EpointFrameDsManager.commit();

                addCallbackParam("msg", "信息推送成功！");
            } catch (Exception ex) {
                EpointFrameDsManager.rollback();
                ex.printStackTrace();
                return false;
            } finally {
                EpointFrameDsManager.close();
            }

            ycslData.put("success", true);
            addCallbackParam("zjxtData", ycslData.toJSONString());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 推送接件/受理信息到部门自建系统
     *
     * @author 徐本能
     */
    private void pushReceiveInfo2SelfBuildSystem(AuditProject auditProject, AuditTask auditTask, AuditTaskExtension auditTaskExtension) {
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        logger.info("使用自建系统");
        ycslData.put("useSelfBuildSystem", true);
        boolean success = ycslService.pushReceiveInfo2DeptSelfBuildSystem(auditProject, auditTask, auditTaskExtension);
        ycslData.put("success", success);
        addCallbackParam("ycslData", ycslData.toJSONString());
    }

    /**
     * 评价
     */
    public void evaluate(AuditProject auditProject, String assessNumber) {
        // rabbitmq推送
        String Macaddress = equipmentservice.getMacaddressbyWindowGuidAndType(
                ZwfwUserSession.getInstance().getWindowGuid(), QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();
        if ("370829".equals(auditProject.getAreacode()) || "370811".equals(auditProject.getAreacode()) || "370882".equals(auditProject.getAreacode()) || "370881".equals(auditProject.getAreacode()) || "370827".equals(auditProject.getAreacode()) || "370892".equals(auditProject.getAreacode()) || "370830".equals(auditProject.getAreacode()) || "370831".equals(auditProject.getAreacode()) || "370891".equals(auditProject.getAreacode()) || "370890".equals(auditProject.getAreacode()) || "370832".equals(auditProject.getAreacode()) || "370826".equals(auditProject.getAreacode()) || "370800".equals(auditProject.getAreacode()) || "370828".equals(auditProject.getAreacode()) || "370883".equals(auditProject.getAreacode())) {
            String proStatus = "";
            Integer status = auditProject.getStatus();
            if (status < 30) {
                proStatus = "1";
                assessNumber = "1";
            } else if (status >= 30 && status <= 90) {
                proStatus = "2";
                assessNumber = "2";
            } else {
                proStatus = "1";
                assessNumber = "1";
            }
            if (StringUtil.isNotBlank(Macaddress)) {
                try {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("status", "100");
                    String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
                    String evalurl = "";
                    if (StringUtil.isNotBlank(centerguid)) {
                        evalurl = handleConfigService.getFrameConfig("AS_EVAL_URL", centerguid).getResult();
                    }
                    if (StringUtil.isBlank(evalurl)) {
                        evalurl = "http://112.6.110.176:28080/jnzwfw";
                    }

                    if ("370883".equals(auditProject.getAreacode())) {
                        dataJson.put("url", evalurl + "/jiningzwfw/pages/zoucchengevaluate/step1?projectNo=" + auditProject.getFlowsn() + "&areacode=" + auditProject.getAreacode() + "&proStatus=" + proStatus + "&assessNumber=" + assessNumber + "&iszj=1");
                    } else {
                        dataJson.put("url", evalurl + "/jiningzwfw/pages/evaluate/step1?projectNo=" + auditProject.getFlowsn() + "&areacode=" + auditProject.getAreacode() + "&acceptareacode=" + auditProject.getAcceptareacode() + "&proStatus=" + proStatus + "&assessNumber=" + assessNumber + "&iszj=1");
                    }
                    ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            String projectguid = auditProject.getRowguid();
            // 先判断是否已评价
            if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {
                try {
                    if (StringUtil.isNotBlank(Macaddress)) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("status", QueueConstant.Evaluate_Status_Evaluate);
                        dataJson.put("clientidentifier", projectguid);
                        dataJson.put("clienttype", ZwfwConstant.Evaluate_clienttype_Project);
                        ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                addCallbackParam("message", "该办件已评价，请勿重复评价！");
            }
        }


    }

    public String getAppMQQueue(String Macaddress) {

        return "mqtt-subscription-" + Macaddress + "qos1";
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
