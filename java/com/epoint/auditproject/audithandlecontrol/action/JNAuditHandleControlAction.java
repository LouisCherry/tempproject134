package com.epoint.auditproject.audithandlecontrol.action;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditonlineproject.auditapplyjslog.inter.IAuditApplyJsLog;
import com.epoint.auditonlineproject.auditonlineexpress.domain.AuditOnlineExpress;
import com.epoint.auditonlineproject.auditonlineexpress.inter.IAuditOnlineExpress;
import com.epoint.auditproject.audithandlecontrol.action.api.IHandleControlService;
import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.auditproject.auditproject.api.IZtProjectService;
import com.epoint.auditproject.entity.csfxxb;
import com.epoint.auditproject.entity.jbxxb;
import com.epoint.auditproject.entity.msfxxb;
import com.epoint.auditproject.guiji.api.ProjectReportByInterfaceService;
import com.epoint.auditproject.guiji.impl.ProjectReportByInterfaceServiceImpl;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditproject.util.factory.FactoryUtil;
import com.epoint.auditproject.util.factory.FinishAuditProjectInterface;
import com.epoint.auditresource.cert.util.YQCertInfoUtil;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.handlecontrol.domain.AuditTaskHandleControl;
import com.epoint.basic.audittask.handlecontrol.inter.IAuditTaskHandleControl;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.api.IHandleControlGxh;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.DHSendMsgUtil;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StarProjectInteractUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditsp.handlecontrol.inter.IHandleControl;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.feishuiduijie.utils.FeishuiduijieBizlogic;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.commission.api.ICommisssionSetServiceInternal;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.job.api.IJobService;
import com.epoint.frame.service.organ.job.entity.FrameJob;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameSecretaryInfo;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.jn.inproject.api.entity.eajcstepspecialnode;
import com.epoint.jnycsl.service.YcslService;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.jnzwfw.sdwaithandle.api.IAuditProjectSamecityService;
import com.epoint.jnzwfw.sdwaithandle.api.entity.AuditProjectSamecity;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.tazwfw.electricity.rest.action.ElectricityController;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.xmz.auditprojectdelivery.api.IAuditProjectDeliveryService;
import com.epoint.xmz.auditprojectdelivery.api.entity.AuditProjectDelivery;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.thirdreporteddata.common.GxhSpConstant;
import com.epoint.xmz.thirdreporteddata.task.commonapi.domain.YwxxRelationMapping;
import com.epoint.xmz.thirdreporteddata.task.commonapi.inter.ITaskCommonService;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.zczwfw.evaluateproject.api.IEvaluateProjectService;
import com.epoint.zczwfw.evaluateproject.api.entity.EvaluateProject;
import com.epoint.zhenggai.api.ZhenggaiService;

@RestController("jnaudithandlecontrolaction")
@Scope("request")
public class JNAuditHandleControlAction extends BaseController
{

    /**
     *
     */
    private static final long serialVersionUID = 3717140866922905512L;
    private Logger logger = Logger.getLogger(JNAuditHandleControlAction.class);

    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");
    public static final String lcjbxxurl = ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");

    public static List<String> tasknames = Arrays.asList("其他建设工程消防验收备案抽查", "建设工程消防设计审查", "特殊建设工程消防验收",
            "建设工程消防设计审查（设区的市级权限）（新设）", "建设工程消防验收（设区的市级权限）（新设）", "建设工程消防设计审查（县级权限）（新设）", "建设工程消防验收备案",
            "建设工程消防验收（县级权限）（新设）");

    /**
     * 工作项标识
     */
    private String workItemGuid;

    /**
     * 办件标识
     */
    private String projectguid;
    /**
     * 流程实例标识
     */
    private String processVersionInstanceGuid;
    /**
     * 事项标识
     */
    private String taskguid;
    /**
     * 办件信息
     */
    private AuditProject auditProject;
    /**
     * 事项信息
     */
    private AuditTask auditTask;
    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 文书打印地址
     */
    private String address = "";
    /**
     * 区域编码
     */
    private String areaCode = "";
    /**
     * 同城通办实体
     */
    private AuditProjectSamecity auditProjectSamecity;

    /**
     * 邮寄实体
     */
    private AuditProjectDelivery auditProjectDelivery;

    /**
     * 代码项接口
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IGsProjectService iGsProjectService;

    @Autowired
    private IAuditOrgaArea areaService;

    @Autowired
    private IWebUploaderService iWebUploaderService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IAuditProject auditProjectServcie;

    @Autowired
    private IJNAuditProject iJNAuditProject;

    @Autowired
    private ICertGgxkwsService iCertGgxkwsService;

    @Autowired
    private ICertHwslysjyxkService iCertHwslysjyxkService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private ICertInfo iCertInfo;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private ZhenggaiService zhenggaiImpl;
    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IHandleControl handleControlService;

    @Autowired
    private IHandleDoc handleDocService;

    @Autowired
    private IJnProjectService iJnProjectService;

    @Autowired
    private IZtProjectService iZtProjectService;

    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private IAuditOnlineEvaluat evaluateservice;

    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

    @Autowired
    private IAuditProjectFormJgxkService iAuditProjectFormJgxkService;
    @Autowired
    private ICxBusService iCxBusService;
    @Autowired
    private ICodeItemsService iCodeItemsService;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IAuditProjectNumber projectNumberService;

    @Autowired
    private IAuditProjectNotify projectNotifyService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;

    @Autowired
    private IWFInstanceAPI9 instanceapi;

    @Autowired
    private IWFInitPageAPI9 initapi;

    @Autowired
    private ICommisssionSetServiceInternal commissionService;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private IUserService userService;

    @Autowired
    private IJobService jobService;

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;
    @Autowired
    private IAuditTaskHandleControl auditTaskhandleControlService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;
    private AuditLogisticsBasicinfo auditLogisticsBasicinfo = null;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditApplyJsLog iAuditApplyJsLog;

    @Autowired
    private ICertInfoExternal iCertInfoExternal;

    @Autowired
    private IAuditOnlineExpress iAuditOnlineExpress;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;

    @Autowired
    IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointimpl;

    @Autowired
    private IHandleControlService ihandleControlService;

    @Autowired
    private IAuditSpPhase auditSpPhaseService;

    @Autowired
    private IConfigService configservice;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditProjectMaterial iauditprojectmaterial;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormSgxkzDanti> model;

    /**
     * 同城通办信息表api
     */
    @Autowired
    private IAuditProjectSamecityService iAuditProjectSamecityService;

    /**
     * 物流基本信息表实体对象
     */
    private AuditLogisticsBasicinfo dataBean = null;

    @Autowired
    IAuditOrgaConfig auditconfigservice;

    @Autowired
    IAuditOrgaWindow iAuditOrgaWindow;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditProjectDeliveryService deliveryService;

    /**
     * 呼叫快递模式
     */
    private String AS_CALLMODE;

    /**
     * 政务服务中心编码
     */
    private String AS_ZWFWZX_CODE;

    private AuditProjectFormSgxkz auditProjectFormSgxkz;

    private AuditProjectFormJgxk auditProjectFormJgxk;

    /**
     * EMS专窗电话
     */
    private String AS_EMSWINDOW_CALL;
    /**
     * 一窗受理service
     */
    @Autowired
    private YcslService ycslService;

    @Autowired
    private IAuditProjectUnusual auditProjectUnusual;

    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    @Autowired
    private ITaskCommonService iTaskCommonService;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IKanyanmainService iKanyanmainService;

    private Record otherInfo = new Record();

    public Record getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Record otherInfo) {
        this.otherInfo = otherInfo;
    }

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    public JNAuditHandleControlAction() {
    }

    @Override
    public void pageLoad() {
        processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        taskguid = getRequestParameter("TaskGuid");
        workItemGuid = getRequestParameter("WorkItemGuid");
        projectguid = getRequestParameter("projectguid");

        // 获取事项信息
        if (StringUtil.isNotBlank(taskguid)) {
            auditTask = auditTaskService.getAuditTaskByGuid(taskguid, true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true).getResult();
            if(auditTask!=null){
                areaCode = auditTask.getAreacode();
            }
        }
        // 获取办件信息
        // String fields = "
        // subappguid,certrowguid,acceptuserguid,ACCEPTUSERNAME,Acceptuserdate,rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,is_test,currentareacode,handleareacode,taskid,task_id,is_lczj,businessguid,has_xycx,dataObj_baseinfo,acceptareacode,sparetime
        // ";
        auditProject = auditProjectServcie.getAuditProjectByRowGuid("*", projectguid, areaCode).getResult();

        if (StringUtil.isBlank(projectguid) && StringUtil.isBlank(taskguid)
                && StringUtil.isNotBlank(processVersionInstanceGuid)) {
            auditProject = auditProjectServcie.getAuditProjectByPVIGuid("*", processVersionInstanceGuid,
                    ZwfwUserSession.getInstance().getAreaCode()).getResult();
            if (auditProject != null) {
                auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                projectguid = auditProject.getRowguid();
                if (auditTask != null) {
                    taskguid = auditTask.getRowguid();
                    areaCode = auditTask.getAreacode();
                    auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(taskguid, true)
                            .getResult();
                }
            }
        }

        if (auditProject != null) {

            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                Map<String, String> conditionMap = new HashMap<String, String>(16);
                conditionMap.put("projectguid = ", auditProject.getRowguid());
                List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                        .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
                if (list != null && !list.isEmpty()) {
                    for (AuditLogisticsBasicinfo logisticsBasicinfo : list) {
                        if (!"1".equals(logisticsBasicinfo.getNet_type())) {
                            auditLogisticsBasicinfo = logisticsBasicinfo;
                        }
                    }
                }
            }
            else {
                auditLogisticsBasicinfo = iAuditLogisticsBasicInfo
                        .getAuditLogisticsBasicinfoListByFlowsn(auditProject.getRowguid()).getResult();
            }
            if (auditLogisticsBasicinfo == null) {
                auditLogisticsBasicinfo = new AuditLogisticsBasicinfo();
            }

            // 生成施工许可表单实体
            auditProjectFormSgxkz = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
            if (auditProjectFormSgxkz == null) {
                auditProjectFormSgxkz = new AuditProjectFormSgxkz();
                auditProjectFormSgxkz.setRowguid(UUID.randomUUID().toString());
                auditProjectFormSgxkz.setProjectguid(projectguid);
                auditProjectFormSgxkz.setOperatedate(new Date());
                auditProjectFormSgxkz.setOperateusername(userSession.getDisplayName());
            }
            // 生成竣工表单实体
            auditProjectFormJgxk = iAuditProjectFormJgxkService.getRecordBy(projectguid);
            if (auditProjectFormJgxk == null) {
                auditProjectFormJgxk = new AuditProjectFormJgxk();
                auditProjectFormJgxk.setRowguid(UUID.randomUUID().toString());
                auditProjectFormJgxk.setProjectguid(projectguid);
                auditProjectFormJgxk.setOperatedate(new Date());
                auditProjectFormJgxk.setOperateusername(userSession.getDisplayName());
            }
            if (auditProjectSamecity == null) {
                auditProjectSamecity = new AuditProjectSamecity();
                auditProjectSamecity.setRowguid(UUID.randomUUID().toString());
                auditProjectSamecity.setProjectguid(auditProject.getRowguid());
                auditProjectSamecity.setDaibanrenname(userSession.getDisplayName());
            }

        }

        auditProjectDelivery = deliveryService.getDeliveryByProjectguid(projectguid);
        if (auditProjectDelivery == null) {
            auditProjectDelivery = new AuditProjectDelivery();
        }

    }

    /**
     * 初始化按钮
     *
     * @throws JSONException
     */
    public String init() throws JSONException {
        pageLoad();
        // 按钮显示控制
        if (auditProject == null) {
            return "";
        }
        // return ButtonControl();
        String btns = ButtonControl();
        // 工改3.0逻辑 开始
        // 办件状态为办件
        auditProject = getAuditProject();
        if (getAuditProject().getStatus() == ZwfwConstant.BANJIAN_STATUS_SPTG
                && StringUtil.isNotBlank(auditProject.getSubappguid())) {
            // 判断是否需要展示工改表单
            YwxxRelationMapping ggForm = getGGForm();
            if (ggForm != null) {
                AuditTaskHandleControl ggsave = initGGSaveBtn();
                // 添加按钮展示
                JSONObject obj = JSONObject.parseObject(btns);
                JSONObject zwfwBtn = obj.getJSONObject("ZwfwBtn");
                if (zwfwBtn != null) {
                    JSONObject controlList = zwfwBtn.getJSONObject("controlList");
                    if (controlList != null) {
                        JSONArray controls = controlList.getJSONArray("controls");

                        controls.add(JSONObject.parseObject(JsonUtil.objectToJson(ggsave)));
                        // 添加排序
                        controls.sort((a, b) -> {
                            JSONObject btn = (JSONObject) a;
                            JSONObject btn1 = (JSONObject) b;
                            if (btn != null && btn1 != null && btn1.getInteger("ordernumber") != null
                                    && btn.getInteger("ordernumber") != null) {
                                return btn1.getInteger("ordernumber") - btn.getInteger("ordernumber");
                            }
                            else {
                                return 0;
                            }

                        });
                        controlList.put("controls", controls);
                    }
                }

                addCallbackParam("ggformurl", ggForm.getStr("ggformurl"));
                addCallbackParam("ggformname", ggForm.getYwxxentityname());
                btns = obj.toJSONString();
            }
        }
        return btns;
    }

    /**
     * 控制按钮的显示
     *
     * @throws JSONException
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    public String ButtonControl() throws JSONException {
        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        JSONObject jsonworkflow;
        // 自定义按钮
        // JSONObject controlList =
        // handleControlService.initHandleControl(projectguid, taskguid,
        // workItemGuid, centerGuid,
        // areaCode, userSession.getUserGuid(),
        // ZwfwUserSession.getInstance().getWindowGuid()).getResult();

        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,is_charge,charge_when,is_check,is_fee,is_pause,is_delay,if_express,is_cert,banjieresult,applyway,spendtime,certnum,certrowguid,applyertype,is_lczj,businessguid,subappguid,sparetime,flowsn,ouname,ouguid,Contactmobile,ACCEPTUSERNAME,Acceptuserdate,acceptuserguid,Tasktype ";
        JSONObject controlList = new JSONObject();
        AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(fields, projectguid, areaCode)
                .getResult();
        HashMap<String, HashMap> buttonCrol = null;
        if (auditProject != null) {
            buttonCrol = handleControlService.initHandleControls(auditProject, taskguid, workItemGuid, centerGuid,
                    areaCode, userSession.getUserGuid(), ZwfwUserSession.getInstance().getWindowGuid());

        }

        // 判断是否是江苏通用
        /*
         * String isJSTY = ConfigUtil.getConfigValue("isJSTY"); if
         * ("1".equals(isJSTY))
         * { buttonCrol.get("btnMap").replace("btnhjclkd",
         * ZwfwConstant.CONSTANT_STR_ZERO);//江苏通用显示 } //判断呼叫快递 String callmode =
         * handleConfigService.getFrameConfig("AS_CALLMODE", "").getResult(); if
         * (callmode.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
         * buttonCrol.get("btnMap").replace("btnhjkd",
         * ZwfwConstant.CONSTANT_STR_ONE);//呼叫快递显示 }
         */
        if (buttonCrol != null) {
            // 判断是否有个性化的实现，有的话将map传入以供个性化
            String path = handleConfigService.getFrameConfig("AS_initHandleControl", centerGuid).getResult();
            if (StringUtil.isNotBlank(path)) {
                IHandleControlGxh handleControlGxhService = (IHandleControlGxh) ReflectUtil.getObjByClassName(path);
                buttonCrol = handleControlGxhService.initHandleControl(projectguid, taskguid, workItemGuid, centerGuid,
                        areaCode, userSession.getUserGuid(), ZwfwUserSession.getInstance().getWindowGuid(), buttonCrol);
            }
            HashMap<String, String> btnMap = buttonCrol.get("btnMap");
            HashMap<String, String> lbl = buttonCrol.get("lbl");

            // 待审核，待办结 增加退回按钮
            if (ZwfwConstant.BANJIAN_STATUS_YSL == auditProject.getStatus()
                    || ZwfwConstant.BANJIAN_STATUS_SPTG == auditProject.getStatus()) {
                btnMap.put("btnTuihui", "1");
            }
            // 在办件受理之后，待审核环节新增补正功能按钮，以及审核不通过按钮，已受理待补正增加补正按钮
            if (ZwfwConstant.BANJIAN_STATUS_YSL == auditProject.getStatus()
                    || ZwfwConstant.BANJIAN_STATUS_YSLDBB == auditProject.getStatus()) {
                btnMap.put("btnPatch", "1");
            }

            // 如果最近一次暂停为勘探，则显示按钮

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
            List<AuditTaskHandleControl> listControl = auditTaskhandleControlService
                    .selectAuditTaskHandleControlByAreaCode(areaCode).getResult();
            // 需要的按钮展示
            List<AuditTaskHandleControl> listControlNeed = new ArrayList<AuditTaskHandleControl>();
            if (ZwfwConstant.BANJIAN_STATUS_ZCBJ != auditProject.getStatus()) {
                for (AuditTaskHandleControl control : listControl) {
                    String btnid = control.getControlid();
                    String isDisplay = btnMap.get(btnid);
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isDisplay)) {
                        listControlNeed.add(control);
                    }
                }
                // 这里需要判断一下时候需要修改发证的按钮
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(iscertificate)
                        && ZwfwConstant.CONSTANT_STR_ONE.equals(btnMap.get("btnResult"))) {
                    for (AuditTaskHandleControl taskHandleControl : listControlNeed) {
                        if ("btnResult".equals(taskHandleControl.getControlid().toString())) {
                            taskHandleControl.setControlname(lblresult);
                            if (StringUtil.isNotBlank(zzurl)) {
                                // 如果配置了多个审批结果（出证）
                                if (zzurl.contains(",")) {
                                    String selectUrl = "../../../jiningzwfw/individuation/overall/audittask/result/taskresultselect"
                                            + zzurl.split("epointzwfw/auditbusiness/auditresource/cert/certinfoadd")[1];
                                    taskHandleControl.setOpenwindowurl(selectUrl);
                                    taskHandleControl.setControlname("出证选择");
                                    taskHandleControl.setOpenwindowheight(300);
                                    taskHandleControl.setOpenwindowwidth(500);
                                }
                                else {
                                    taskHandleControl.setOpenwindowurl(zzurl);
                                }
                            }
                            break;
                        }
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
                rtnInfo.put("controls", listControlNeed);
                rtnInfo.put("handlecontrols_btnlst", handlecontrols_btnlst);
                controlList = rtnInfo;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject root = new JSONObject();
        JSONArray jsonArray = initDocModel(auditTask.getType().toString());

        try {
            root.put("cbDoc", jsonArray);
            root.put("controlList", controlList);
            root.put("projectGuid", projectguid);
            root.put("projectStatus", auditProject == null ? "" : auditProject.getStatus());
            root.put("taskGuid", taskguid);
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(auditTask.getType().toString())
                    && !ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())) {
                root.put("taskType", "jbj");
            }
            if (StringUtil.isBlank(processVersionInstanceGuid)) {
                jsonworkflow = new JSONObject();
            }
            else {
                jsonworkflow = JSON.parseObject(handleCtrl());
            }
            // 取消掉审核不通过按钮，审核通过改成：审核意见
            if (jsonworkflow.containsKey("btn") && !jsonworkflow.getJSONArray("btn").isEmpty()) {
                Iterator<Object> jsonObjectIterable = jsonworkflow.getJSONArray("btn").iterator();
                while (jsonObjectIterable.hasNext()) {
                    JSONObject jsonObject = (JSONObject) jsonObjectIterable.next();
                    if (jsonObject != null && "审核不通过".equals(jsonObject.getString("text"))) {
                        // 在办件受理之后，待审核环节新增补正功能按钮，以及审核不通过按钮。
                        if (ZwfwConstant.BANJIAN_STATUS_YSL == auditProject.getStatus()
                                || ZwfwConstant.BANJIAN_STATUS_YSLDBB == auditProject.getStatus()) {

                        }
                        else {
                            // jsonObjectIterable.remove();
                        }
                    }
                    if (jsonObject != null && "审核通过".equals(jsonObject.getString("text"))) {
                        jsonObject.put("text", "审核意见");
                    }
                }
            }
            jsonworkflow.put("ZwfwBtn", root);
            return jsonworkflow.toString();
        }
        catch (JSONException e) {
            return e.getMessage();
        }
    }

    /**
     * 初始化工改保存按钮
     *
     * @return
     */
    public AuditTaskHandleControl initGGSaveBtn() {
        AuditTaskHandleControl btn = new AuditTaskHandleControl();
        btn.setControlname("保存");
        btn.setControltype("20");
        btn.setControlid("btnGGsave");
        btn.setOpenwindowwidth(0);
        btn.setOpenwindowheight(0);
        btn.setRowguid("GGsave");
        btn.setJsfunction("handleGGsave");
        btn.setControlposition("10");
        btn.setTaskguid(UUID.randomUUID().toString());
        btn.setOrdernumber(1250);
        return btn;
    }

    /**
     * 获取工改表单
     *
     * @return
     */
    public YwxxRelationMapping getGGForm() {
        // 判断当前办件标准事项有没有进行维护
        List<AuditSpBasetask> list = iTaskCommonService
                .getAuditSpTask(auditProject.getBusinessguid(), auditProject.getTask_id()).getResult();
        if (EpointCollectionUtils.isNotEmpty(list)) {
            // 查询工程代码
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
            if (auditSpISubapp != null) {
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                if (auditRsItemBaseinfo != null) {
                    YwxxRelationMapping ywxxRelationMapping = GxhSpConstant.objectMap
                            .get(list.get(0).getStr("taskcodeV3"));
                    if (ywxxRelationMapping != null) {
                        String urlString = "jiningzwfw/audittaskzjv3/" + ywxxRelationMapping.getFormadress()
                                + "?spsxslbm=" + auditProject.getFlowsn() + "&gcdm=" + auditRsItemBaseinfo.getItemcode()
                                + "&xzqhdm=370800&bj=1";
                        ywxxRelationMapping.set("ggformurl", urlString);
                    }
                    // 根据事项标准编码 工程代码和审批事项实例编码 查询此事项有没有维护
                    return ywxxRelationMapping;
                }
            }
        }
        return null;
    }

    public String handleCtrl() {
        JSONObject jsonobject = new JSONObject();

        String workitemGuid = getRequestParameter("WorkItemGuid");
        String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        // 0和默认 获取第一步的按钮，1定位至当前运行的工作项 2浏览页面，不显示按钮
        String handleType = getRequestParameter("handleType");
        ProcessVersionInstance pvi = instanceapi.getProcessVersionInstance(processVersionInstanceGuid);

        if (StringUtil.isBlank(workitemGuid) && StringUtil.isNotBlank(processVersionInstanceGuid)) {
            jsonobject.put("processversionguid", pvi.getPvi().getProcessVersionGuid());
        }
        else {
            jsonobject.put("workitemguid", workitemGuid);
        }
        jsonobject.put("userguid", userSession.getUserGuid());
        jsonobject.put("username", userSession.getDisplayName());
        jsonobject.put("pviguid", processVersionInstanceGuid);
        if (StringUtil.isBlank(workitemGuid) && StringUtil.isNotBlank(processVersionInstanceGuid)
                && StringUtil.isNotBlank(handleType)) {
            jsonobject.put("handletype", handleType);
        }

        // 判断是否是当前处理人
        if (StringUtil.isNotBlank(workitemGuid)) {
            JSONObject obj = new JSONObject();
            WorkflowWorkItem workflowWorkItem = instanceapi.getWorkItem(pvi, workitemGuid);
            MessagesCenter mes = messageCenterService.getDetail(workflowWorkItem.getWaitHandleGuid(),
                    userSession.getUserGuid());
            if (mes != null && !"[#=EveryOne#]".equals(workflowWorkItem.getTransactor())) {
                if (!userSession.getUserGuid().equals(workflowWorkItem.getTransactor())) {
                    if (StringUtil.isNotBlank(workflowWorkItem.getJobGuid())) {
                        boolean tag = true;
                        List<FrameJob> joblist = jobService.listJobByBelongUserGuid(userSession.getUserGuid());
                        if (joblist != null && !joblist.isEmpty()) {
                            for (FrameJob job : joblist) {
                                if (workflowWorkItem.getJobGuid().equals(job.getJobGuid())) {
                                    tag = false;
                                    break;
                                }
                            }
                        }
                        if (tag) {
                            addCallbackParam("message", "错误！您不是本待办事项的处理者！");
                            obj.put("message", "错误！您不是本待办事项的处理者！");
                            return obj.toString();
                        }
                    }
                    else// 如果job为空，那么只有在“当前用户可以代理工作项所属人工作时”，才允许打开
                    {
                        // 再判断代理人
                        // 先判断时间授权
                        boolean cancheck = commissionService.checkCanDeal(userSession.getUserGuid(),
                                workflowWorkItem.getTransactor());
                        if (!cancheck) {
                            // 如果不满足 判断事项授权
                            cancheck = commissionService.checkHandleDeal(userSession.getUserGuid(),
                                    workflowWorkItem.getWaitHandleGuid());
                            if (!cancheck) {
                                // 最后判断领导秘书
                                if (IMessagesCenterService.EXCLUDE_JUDGE_COMMISSION.equals(mes.getRdoType())) {
                                    addCallbackParam("message", "错误！您不是本待办事项的处理者！");
                                    obj.put("message", "错误！您不是本待办事项的处理者！");
                                    return obj.toString();
                                }
                                else {
                                    List<FrameSecretaryInfo> infolst = userService
                                            .listSecretaryInfoByLeaderGuid(workflowWorkItem.getTransactor());
                                    boolean tag = true;
                                    if (infolst != null && !infolst.isEmpty()) {
                                        for (FrameSecretaryInfo info : infolst) {
                                            if (userSession.getUserGuid().equals(info.getSecretaryGuid())) {
                                                tag = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (tag) {
                                        addCallbackParam("message", "错误！您不是本待办事项的处理者！");
                                        obj.put("message", "错误！您不是本待办事项的处理者！");
                                        return obj.toString();
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    // 即使是本人，有秘书的时候也应该是秘书先处理
                    if (!IMessagesCenterService.EXCLUDE_JUDGE_COMMISSION.equals(mes.getRdoType())) {
                        List<FrameSecretaryInfo> infolst = userService
                                .listSecretaryInfoByLeaderGuid(workflowWorkItem.getTransactor());
                        if (infolst != null && !infolst.isEmpty()) {
                            addCallbackParam("message", "请您等待秘书先行处理！");
                            obj.put("message", "请您等待秘书先行处理！");
                            return obj.toString();
                        }

                    }
                }
            }
        }
        // return new
        // ZWFWInitPageAPI().init_getHandleControlInfo(jsonobject.toString());
        return initapi.init_getHandleControlInfo(jsonobject.toString());
    }

    /**
     * 控制按键的display属性
     *
     * @param value
     *            字符串常量0或者1
     * @return String 是否显示的样式
     */
    public String displayControl(String value) {
        String display = "none";
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(value)) {
            display = "inline-block";
        }
        return display;
    }

    /**
     * 判断是否要进行接件操作
     */
    public void isReceiveProject() {
        // 如果是窗口人员登记获取办件流水号
        String is_samecity = "";
        auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
        //是否接件即受理
        if(StringUtils.isNotBlank(auditTaskExtension.get("jiejianandshouli")) && "1".equals(auditTaskExtension.get("jiejianandshouli"))){
            addCallbackParam("jiejianandshouli","1");
        }else{
            addCallbackParam("jiejianandshouli","0");
        }
        if (auditTask != null) {
            System.err.println("同城通办===" + is_samecity);
            is_samecity = auditTask.getStr("is_samecity");
        }
        /*
         * auditProject.setFlowsn("123823723");
         * auditProjectServcie.updateProject(auditProject);
         */
        if (auditProject.getApplyway() == 20 && StringUtil.isBlank(auditProject.getFlowsn())) {
            // 获取事项ID
            String unid = zhenggaiImpl.getunidbyTaskid(auditProject.getTask_id());
            // 请求接口获取受理编码
            if (StringUtil.isNotBlank(unid)) {
                String result = FlowsnUtil.createReceiveNum(unid, auditTask.getRowguid());
                if (!"error".equals(result)) {
                    logger.info("========================>获取受理编码成功！" + result);
                    auditProject.setFlowsn(result);
                    auditProjectServcie.updateProject(auditProject);
                }
                else {
                    logger.info("========================>获取受理编码失败！");
                    addCallbackParam("msg", "获取受理编号异常，请重试");
                    return;
                }
            }

        }

        // 判断乡镇部门在接件时判断是否是上级委托事件
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            AuditTaskDelegate auditTaskDelegate = auditTaskDelegateService
                    .findByTaskIDAndAreacode(auditTask.getTask_id(), ZwfwUserSession.getInstance().getAreaCode())
                    .getResult();
            if ("0".equals(auditTaskDelegate.getDelegatetype())) {
                addCallbackParam("noDelegateType", "办件已不再委托此窗口进行办理");
                return;
            }
        }

        // 判断该办件是否已经进行信用接口调用
        if (!"1".equals(auditProject.getStr("has_xycx")) && 10 == auditProject.getApplyertype()
                && !auditProject.getProjectname().contains("建筑工程施工许可")) {
            addCallbackParam("xinyongchaxun", "请先进行企业信用查询！");
            return;
        }

        // 判断此事项是否依然配置在窗口下
        List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow.getWindowListByTaskId(auditTask.getTask_id())
                .getResult();

        System.err.println("同城通办===" + is_samecity);
        System.err.println("同城通办===" + ZwfwUserSession.getInstance().getWindowGuid());
        if (auditOrgaWindows != null && !auditOrgaWindows.isEmpty()) {
            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(is_samecity)
                        || ZwfwUserSession.getInstance().getWindowGuid().equals(auditOrgaWindow.getRowguid())) {
                    return;
                }
            }
            addCallbackParam("noWindow", "窗口已不允许办理该事项");
            return;
        }
        else {
            addCallbackParam("noWindow", "窗口已不允许办理该事项");
            return;
        }

    }

    /**
     * 接件操作
     */
    public void receiveProject() {
        boolean flag = isUserWordDoc();// 判断是否使用word

        ZwfwUserSession zwfwUserSession = ZwfwUserSession.getInstance();
        // 获取事项ID
        auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();

        // 是否同城通办
        String is_samecity = "";
        if (auditTask != null) {
            is_samecity = auditTask.get("is_samecity");
            String IS_SAMECITY = getRequestParameter("is_samecity");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(is_samecity)
                    && ZwfwConstant.CONSTANT_STR_ONE.equals(IS_SAMECITY)) {
                auditProject.set("is_samecity", is_samecity);
                // 如果是同城通办，插入同城通办记录表记录
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
                }
                else {
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
                    }
                    else {
                        iAuditProjectSamecityService.insert(auditProjectSamecity);
                    }
                }

            }
        }

        auditProject.setCurrentareacode(ZwfwUserSession.getInstance().getAreaCode());
        // 保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if (StringUtil.isBlank(auditProject.getHandleareacode())) {
            areaStr = handleAreaCode + ",";
        }
        else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if (StringUtil.isNotBlank(areaStr)) {
            auditProject.setHandleareacode(areaStr);
        }
        String sqtzaddress = handleProjectService.handleReceive(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), zwfwUserSession.getWindowGuid(), zwfwUserSession.getWindowName())
                .getResult();
        // 向投资监管平台发送mq
        String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
        sendMQMessageService.sendByExchange("exchange_handle", msg,
                "project." + ZwfwUserSession.getInstance().getAreaCode() + ".receive." + auditProject.getTask_id());

        // 使用物流
        if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            /*
             * String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_prov()); if
             * (StringUtil.isNotBlank(prov)) {
             * auditLogisticsBasicinfo.setRcv_prov(prov); } String city =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_city()); if
             * (StringUtil.isNotBlank(city)) {
             * auditLogisticsBasicinfo.setRcv_city(city); } String country =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_country()); if
             * (StringUtil.isNotBlank(country)) {
             * auditLogisticsBasicinfo.setRcv_country(country); }
             */
            auditLogisticsBasicinfo.setStatus("0");
            auditLogisticsBasicinfo.setCallState("05");// 初始化状态
            auditLogisticsBasicinfo.setNet_type("2");
            auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
            auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
            if (StringUtil.isNotBlank(auditLogisticsBasicinfo.getProjectguid())) {
                handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
            }
            else {
                handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
            }
        }
        try {
            JSONObject jsonBack = JSONObject.parseObject(ButtonControl());
            jsonBack.put("message", sqtzaddress);
            addCallbackParam("sqtzaddress", sqtzaddress);
            addCallbackParam("message", jsonBack.toString());
            addCallbackParam("isword", flag ? "1" : "0");
            addCallbackParam("messageitemguid", "undefined".equals(getRequestParameter("MessageItemGuid")) ? ""
                    : getRequestParameter("MessageItemGuid"));
        }
        catch (JSONException e) {
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
                if (messagesList != null && !messagesList.isEmpty()) {
                    messagesCenterList.addAll(messagesList);
                }
            }
        }
        else {
            messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                    IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
        }
        if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
            }
        }
        // 若 不使用一窗，则发送待办事宜
        boolean useYcsl = ycslService.useYcslByTaskExtensionObj(auditTaskExtension);
        // logger.info("useYcsl=" + useYcsl);
        // 发送待办事宜
        if (!useYcsl) {
            if (!ZwfwConstant.CONSTANT_STR_ONE.equals(is_samecity)) {
                String title = "【待受理】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                String messageItemGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(),
                        userSession.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "待受理",
                        handleUrl, userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                        auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), new Date(),
                        auditProject.getPviguid(), userSession.getUserGuid(), "", "");
                addCallbackParam("title", title);
                // 先判断是否启用江苏通用
                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    // 再判断外网办件
                    if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                            || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                        // 接件只需要调用星网的接口
                        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                .getResult();
                        String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                        // 只有在配置的情况下才能进行请求
                        if (StringUtil.isNotBlank(url)) {
                            url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfodetail?declareid="
                                    + auditTask.getTask_id() + "&centerguid=" + auditProject.getCenterguid()
                                    + "&taskguid=" + taskguid + "&flowsn=" + auditProject.getFlowsn();

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
        }
        else {
            // 使用一窗的情况下，推送办件信息到自建系统
            pushReceiveInfo2SelfBuildSystem();
        }
        if (auditProjectFormSgxkz != null && StringUtil.isNotBlank(auditProjectFormSgxkz.getXiangmumingchen())) {
            // System.err.println(auditProjectFormSgxkz);
            AuditProjectFormSgxkz auditProjectFormSgxkz1 = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
            if (auditProjectFormSgxkz1 != null) {
                iAuditProjectFormSgxkzService.update(auditProjectFormSgxkz);
            }
            else {
                iAuditProjectFormSgxkzService.insert(auditProjectFormSgxkz);
            }
        }

        afterYsj("false");

        // 推送办件信息表到住建
        projectSendzj("false", "'" + auditProject.getRowguid() + "'");

        //是否接件即受理
//        if(StringUtils.isNotBlank(auditTaskExtension.get("jiejianandshouli")) && "1".equals(auditTaskExtension.get("jiejianandshouli"))){
//
//        }
    }

    public void afterYsj(String isck) {
        // 如果是第四阶段，需要和联合验收系统对接
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
        if (auditSpISubapp != null) {
            AuditSpPhase auditSpPhase = auditSpPhaseService.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid())
                    .getResult();
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();

            AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(auditSpISubapp.getBiguid())
                    .getResult();
            String tkurl = configservice.getFrameConfigValue("AS_BLSP_TKURL");
            if (auditSpPhase != null && auditRsItemBaseinfo != null && auditSpInstance != null
                    && "4".equals(auditSpPhase.getPhaseId()) && StringUtil.isNotBlank(tkurl)) {
                String msg = auditSpISubapp.getRowguid() + "_SPLIT_" + ZwfwUserSession.getInstance().getAreaCode()
                        + "_SPLIT_" + auditRsItemBaseinfo.getItemcode() + "_SPLIT_"
                        + WebUtil.getRequestCompleteUrl(request);
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.tk." + auditSpInstance.getBusinessguid());
            }
        }

    }

    public void projectSendzj(String isck, String projectguids) {
        log.info("接件上报数据" + isck + projectguids);
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();

            AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(auditSpISubapp.getBiguid())
                    .getResult();
            if (auditRsItemBaseinfo != null && auditSpInstance != null) {

                // 发送mq推送项目基本信息
                String prowguid = auditRsItemBaseinfo.getRowguid();
                String issendzj = auditRsItemBaseinfo.getIssendzj();
                // 如果是子项查询主项
                if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                    AuditRsItemBaseinfo dataBean1 = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                    if (dataBean1 != null) {
                        prowguid = dataBean1.getRowguid();
                        issendzj = dataBean1.getIssendzj();
                    }
                }
                String mqmsg;
                if (!auditRsItemBaseinfo.getRowguid().equals(prowguid)
                        && !ZwfwConstant.CONSTANT_STR_ONE.equals(issendzj)) {
                    // 如果是新增项目，则需要把项目推送到住建系统
                    mqmsg = prowguid + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                            + auditSpISubapp.getRowguid();
                    sendMQMessageService.sendByExchange("exchange_handle", mqmsg,
                            "blsp.rsitem." + auditSpInstance.getBusinessguid());
                }
                // 添加推送数据
                mqmsg = auditRsItemBaseinfo.getRowguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                        + auditSpISubapp.getRowguid();
                sendMQMessageService.sendByExchange("exchange_handle", mqmsg,
                        "blsp.rsitem." + auditSpInstance.getBusinessguid());

                // 发送mq消息推送办件信息数据
                String msg = auditSpISubapp.getRowguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                        + auditRsItemBaseinfo.getItemcode();
                if ("false".equals(isck)) {
                    msg += ".nck";
                }
                else {
                    msg += ".isck";
                }
                // 拼接办理人用户标识
                msg += "." + UserSession.getInstance().getUserGuid() + "." + projectguids;
                log.info("msg" + msg);
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.subapp." + auditSpInstance.getBusinessguid());

                // 接件消防推送注释20250214
                // 消防办件推送
                /*
                 * sendMQMessageService.sendByExchange("exchange_handle", msg,
                 * "blsp.xf.subapp." + auditSpInstance.getBusinessguid());
                 */
            }

        }

    }

    @Deprecated
    public void projectSendXf(String isck, String projectguids) {
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();

            AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(auditSpISubapp.getBiguid())
                    .getResult();
            if (auditRsItemBaseinfo != null && auditSpInstance != null) {
                String msg = auditSpISubapp.getRowguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                        + auditRsItemBaseinfo.getItemcode();
                // 发送mq消息推送数据
                if ("false".equals(isck)) {
                    msg += ".nck";
                }
                else {
                    msg += ".isck";
                }
                // 拼接办理人用户标识
                msg += "." + UserSession.getInstance().getUserGuid() + "." + projectguids;
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.subapp." + auditSpInstance.getBusinessguid());
                log.info("消防发送mq=====");
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.xf.subapp." + auditSpInstance.getBusinessguid());
            }

        }

    }

    /**
     * 推送接件/受理信息到部门自建系统
     *
     * @author 徐本能
     */
    private void pushReceiveInfo2SelfBuildSystem() {
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        logger.info("使用自建系统");
        ycslData.put("useSelfBuildSystem", true);
        boolean success = ycslService.pushReceiveInfo2DeptSelfBuildSystem(auditProject, auditTask, auditTaskExtension);
        ycslData.put("success", success);
        addCallbackParam("ycslData", ycslData.toJSONString());
    }

    /**
     * 补正操作
     */
    public void buzhengProject() {

        String bzgzsaddress = handleProjectService
                .handlePatch(auditProject, userSession.getDisplayName(), userSession.getUserGuid()).getResult();

        AuditCommonResult<AuditTask> taskResult = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true);
        AuditTask task = taskResult.getResult();

        // 先判断是否启用江苏通用
        String isJSTY = ConfigUtil.getConfigValue("isJSTY");
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
            // 判断是否外网办件推送状态
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 没有配置不走接口调用
                if (StringUtil.isNotBlank(url)) {
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfoedit?declareid=" + task.getTask_id()
                            + "&centerguid=" + auditProject.getCenterguid() + "&taskguid=" + taskguid + "&flowsn="
                            + auditProject.getFlowsn();
                    Record record = StarProjectInteractUtil
                            .updateOrInsertStar(auditProject.getFlowsn(), ZwdtConstant.STAR_PROJECT_BZ,
                                    StringUtil.isBlank(task.getDept_yw_reg_no()) ? task.getItem_id()
                                            : task.getDept_yw_reg_no(),
                                    task.getTaskname(), auditProject.getContactperson(),
                                    auditProject.getContactcertnum(), "物流确认", url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                    // 调用大汉统一消息接口
                    try {
                        Record recorddh = DHSendMsgUtil.PostDHMessage("您申请的办件有了新的进度!",
                                auditProject.getContactperson() + "你好，您于"
                                        + EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                                "yyyy年MM月dd日 HH:mm")
                                        + "在网上申请的“" + auditProject.getProjectname() + "”，有材料需要进行补正，请您尽快在江苏政务服务网确认收件地址。",
                                auditProject.getContactcertnum());
                        iAuditApplyJsLog.insertDHAuditApplyJsLog(recorddh);
                    }
                    catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(bzgzsaddress)) {
            addCallbackParam("message", bzgzsaddress);
        }
        else {
            addCallbackParam("message", "0");
        }
    }

    /**
     *
     * 恢复计时操作
     *
     */
    /**
     * 恢复计时操作
     */
    public void huiFuProject() {
        // TODO messageItemGuid需要赋值的
        String messageItemGuid = getRequestParameter("MessageItemGuid");
        String unusualGuid = handleProjectService.handleRecover(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), workItemGuid, messageItemGuid).getResult();
        if (StringUtil.isNotBlank(unusualGuid)) {
            // 测试办件不生成
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + unusualGuid
                        + "&handleSpecialResult:" + projectguid + ";" + unusualGuid;

                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // 接办分离 特殊操作
                // msg = projectguid + "." + auditProject.getApplyername() + "."
                // + unusualGuid;
                // sendMQMessageService.sendByExchange("exchange_handle", msg,
                // "project."
                // + ZwfwUserSession.getInstance().getAreaCode() + ".special." +
                // auditProject.getTask_id());
                // 接办分离 特殊操作结果
                msg = projectguid + "." + unusualGuid;
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTask_id());
                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    msg = "handleSpecial:" + projectguid + ";" + unusualGuid + ";" + auditProject.getAreacode()
                            + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                }
            }
        }
        try {
            addCallbackParam("message", ButtonControl());
        }
        catch (JSONException e) {
        }

    }

    /**
     * 获取核价页面地址
     */
    public void getHeJiaUrl() {
        AuditTaskExtension audittaskextension = auditTaskExtensionService
                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
        if (audittaskextension.getCharge_when() == ZwfwConstant.CONSTANT_INT_ONE) {
            this.saveForm();
        }
        else {
            this.saveFormNoDB();
        }
        String hejiaurl = handleConfigService.getFrameConfig("AS_Fee_HeJia_URL", auditProject.getCenterguid())
                .getResult();
        String messageItemGuid = getRequestParameter("MessageItemGuid");
        if (StringUtil.isBlank(hejiaurl)) {
            // 没有个性化的核价地址则使用系统默认的地址
            hejiaurl = "epointzwfw/auditbusiness/auditproject/auditchargerecordtab";
        }
        if (hejiaurl.indexOf("?") > -1) {
            hejiaurl += "&";
        }
        else {
            hejiaurl += "?";
        }
        hejiaurl += "processVersionInstanceGuid=" + processVersionInstanceGuid + "&workItemGuid=" + workItemGuid
                + "&taskGuid=" + taskguid + "&projectguid=" + projectguid + "&sfoperate=hj" + "&MessageItemGuid="
                + messageItemGuid;
        addCallbackParam("message", hejiaurl);
    }

    /**
     * 返回核价后对话框标题
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTitle() {
        String messageItemGuid = getRequestParameter("MessageItemGuid");
        if (auditProject.getIs_check() == null) {
            auditProject.setIs_check(ZwfwConstant.CONSTANT_INT_ZERO);
        }
        if (auditProject.getIs_fee() == null) {
            auditProject.setIs_fee(ZwfwConstant.CONSTANT_INT_ZERO);
        }
        if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_check()) {
            String title = "";
            if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_fee()) {
                title = "【已缴费】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            }
            else {
                title = "【银行收费中】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            }

            // 如果存在待办事宜，则删除待办
            List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                    null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null,
                    null, 0, -1);
            // 如果通过办件主键查询到的待办事宜，则说明该待办为受理前收费产生的，那么需要删除原先待办，并新增银行收费中待办
            AuditTaskExtension audittaskextension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            if (audittaskextension.getCharge_when() == ZwfwConstant.CONSTANT_INT_ONE
                    || (audittaskextension.getCharge_when() == 2
                            && auditTask.getType() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)
                            && ZwfwConstant.JBJMODE_SIMPLE.equals(auditTask.getJbjmode()))) {
                for (MessagesCenter messagescenter : messagesCenterList) {
                    messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                            messagescenter.getTargetUser());
                }
                // 发送待办事宜
                String msgGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                messageCenterService.insertWaitHandleMessage(msgGuid, title, IMessagesCenterService.MESSAGETYPE_WAIT,
                        userSession.getUserGuid(), userSession.getDisplayName(), userSession.getUserGuid(),
                        userSession.getDisplayName(),
                        ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_fee() ? "已缴费" : "银行收费中", handleUrl,
                        userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                        auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getRowguid(),
                        userSession.getUserGuid(), "", "");
            }
            // 反之，则为办结前收费产生，直接替换为银行收费中待办
            else {
                // 如果是办结前收费则会有待办
                MessagesCenter messagescenter = messageCenterService.getDetail(getRequestParameter("MessageItemGuid"),
                        userSession.getUserGuid());
                if (messagescenter != null) {
                    messageCenterService.updateMessageTitleAndShow(messagescenter.getMessageItemGuid(), title,
                            userSession.getUserGuid());
                }
                else {
                    for (MessagesCenter messagesCenter : messagesCenterList) {
                        messageItemGuid = messagesCenter.getMessageItemGuid();
                        messageCenterService.updateMessageTitleAndShow(messagesCenter.getMessageItemGuid(), title,
                                userSession.getUserGuid());
                    }
                }
                // addCallbackParam("messageitemguid",
                // "undefined".equals(getRequestParameter("MessageItemGuid")) ?
                // ""
                // : getRequestParameter("MessageItemGuid"));
                addCallbackParam("messageitemguid", "undefined".equals(messageItemGuid) ? "" : messageItemGuid);
                addCallbackParam("title", title);
                addCallbackParam("ischeck", "1");
            }
        }
    }

    /**
     * 返回收讫后对话框标题
     *
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateShouqi() {
        if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_fee()) {
            String title = "【已缴费】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";

            // 如果存在待办事宜，则删除待办
            List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                    null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null,
                    null, 0, -1);
            // 如果通过办件主键查询到的待办事宜，则说明该待办为受理前收费产生的，那么需要删除原先待办，并新增银行收费中待办
            AuditTaskExtension audittaskextension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
            if (audittaskextension.getCharge_when() == ZwfwConstant.CONSTANT_INT_ONE) {
                for (MessagesCenter messagescenter : messagesCenterList) {
                    messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                            messagescenter.getTargetUser());
                }
                // 发送待办事宜
                String msgGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                messageCenterService.insertWaitHandleMessage(msgGuid, title, IMessagesCenterService.MESSAGETYPE_WAIT,
                        userSession.getUserGuid(), userSession.getDisplayName(), userSession.getUserGuid(),
                        userSession.getDisplayName(), "已缴费", handleUrl, userSession.getOuGuid(), "",
                        ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                        auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                        userSession.getUserGuid(), "", "");
            }
            // 反之，则为办结前收费产生，直接替换为银行收费中待办
            else {
                // 如果是办结前收费则会有待办
                MessagesCenter messagescenter = messageCenterService.getDetail(getRequestParameter("MessageItemGuid"),
                        userSession.getUserGuid());
                if (messagescenter != null) {
                    messageCenterService.updateMessageTitleAndShow(messagescenter.getMessageItemGuid(), title,
                            userSession.getUserGuid());
                }
            }
            addCallbackParam("messageitemguid", "undefined".equals(getRequestParameter("MessageItemGuid")) ? ""
                    : getRequestParameter("MessageItemGuid"));
            addCallbackParam("title", title);
            addCallbackParam("isfee", "1");
        }
    }

    /**
     * 申请人确认
     *
     * @throws Exception
     */
    public void applyerQueRen() {
        // 先保存办件信息
        handleProjectService.saveProject(auditProject);

        try {
            // rabbitmq推送
            String Macaddress = equipmentservice.getMacaddressbyWindowGuidAndType(
                    ZwfwUserSession.getInstance().getWindowGuid(), QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();
            if (StringUtil.isNotBlank(Macaddress)) {
                JSONObject dataJson = new JSONObject();
                dataJson.put("status", QueueConstant.Evaluate_Status_QueRen);
                dataJson.put("projectguid", projectguid);
                dataJson.put("areacode", ZwfwUserSession.getInstance().getAreaCode());
                ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getAppMQQueue(String Macaddress) {

        return "mqtt-subscription-" + Macaddress + "qos1";
    }

    /**
     * 评价
     */
    public void evaluate(String assessNumber) {
        // rabbitmq推送
        String Macaddress = equipmentservice.getMacaddressbyWindowGuidAndType(
                ZwfwUserSession.getInstance().getWindowGuid(), QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();
        if ("370829".equals(auditProject.getAreacode()) || "370811".equals(auditProject.getAreacode())
                || "370882".equals(auditProject.getAreacode()) || "370881".equals(auditProject.getAreacode())
                || "370827".equals(auditProject.getAreacode()) || "370892".equals(auditProject.getAreacode())
                || "370830".equals(auditProject.getAreacode()) || "370831".equals(auditProject.getAreacode())
                || "370891".equals(auditProject.getAreacode()) || "370890".equals(auditProject.getAreacode())
                || "370832".equals(auditProject.getAreacode()) || "370826".equals(auditProject.getAreacode())
                || "370800".equals(auditProject.getAreacode()) || "370828".equals(auditProject.getAreacode())
                || "370883".equals(auditProject.getAreacode())) {
            String proStatus = "";
            Integer status = auditProject.getStatus();
            if (status < 30) {
                proStatus = "1";
                assessNumber = "1";
            }
            else if (status >= 30 && status <= 90) {
                proStatus = "2";
                assessNumber = "2";
            }
            else {
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
                        dataJson.put("url",
                                evalurl + "/jiningzwfw/pages/zoucchengevaluate/step1?projectNo="
                                        + auditProject.getFlowsn() + "&areacode=" + auditProject.getAreacode()
                                        + "&proStatus=" + proStatus + "&assessNumber=" + assessNumber + "&iszj=1");
                    }
                    else {
                        dataJson.put("url",
                                evalurl + "/jiningzwfw/pages/evaluate/step1?projectNo=" + auditProject.getFlowsn()
                                        + "&areacode=" + auditProject.getAreacode() + "&acceptareacode="
                                        + auditProject.getAcceptareacode() + "&proStatus=" + proStatus
                                        + "&assessNumber=" + assessNumber + "&iszj=1");
                    }
                    ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else {
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
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else {
                addCallbackParam("message", "该办件已评价，请勿重复评价！");
            }
        }

    }

    /**
     * 保存表单信息
     */
    public void saveForm() {
        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        String isneedreceive = handleConfigService.getFrameConfig("AS_ISNEED_RECEIVE", centerGuid).getResult();
        AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        if ((auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_INIT
                || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_DJJ
                || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_WWYSTG)
                && (ZwfwConstant.CONSTANT_STR_ZERO.equals(isneedreceive)
                        || (ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                                && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())))) {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
            // 更新即办件在OnlineProject状态
            if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                    || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOnlineProject.class);
                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                            .getComponent(IAuditRsCompanyBaseinfo.class);
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    applyerguid = auditrscompanybaseinfo.getCompanyid();
                }
                Map<String, String> updateFieldMap = new HashMap<>();
                Map<String, String> updateDateFieldMap = new HashMap<>();// 更新日期类型的字段
                updateFieldMap.put("status=", auditProject.getStatus().toString());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", auditProject.getRowguid());
                auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
            }
        }
        // 使用物流
        if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            /*
             * String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_prov()); if
             * (StringUtil.isNotBlank(prov)) {
             * auditLogisticsBasicinfo.setRcv_prov(prov); } String city =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_city()); if
             * (StringUtil.isNotBlank(city)) {
             * auditLogisticsBasicinfo.setRcv_city(city); } String country =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_country()); if
             * (StringUtil.isNotBlank(country)) {
             * auditLogisticsBasicinfo.setRcv_country(country); }
             */
            auditLogisticsBasicinfo.setStatus("0");
            auditLogisticsBasicinfo.setCallState("05");// 初始化状态
            auditLogisticsBasicinfo.setNet_type("2");
            auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
            auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
            if (StringUtil.isBlank(auditLogisticsBasicinfo.getProjectguid())) {
                handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
            }
            else {
                handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
            }
        }
        // 保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if (StringUtil.isBlank(auditProject.getHandleareacode())) {
            areaStr = handleAreaCode + ",";
        }
        else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if (StringUtil.isNotBlank(areaStr)) {
            auditProject.setHandleareacode(areaStr);
        }
        auditProject.setCurrentareacode(ZwfwUserSession.getInstance().getAreaCode());
        handleProjectService.saveProject(auditProject);
        // 承诺件时，点击保存，插入待办
        if (auditProject.getStatus() < ZwfwConstant.BANJIAN_STATUS_YSL
                && "2".equals(String.valueOf(audittask.getType()))) {
            // 如果存在待办事宜，则不再插入待办
            List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                    null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null,
                    null, 0, -1);
            if (messagesCenterList == null || messagesCenterList.isEmpty()) {
                String pretitle = "【待接件】";
                if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                    pretitle = "【待受理】";
                }
                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(isneedreceive)) {
                    pretitle = "【待受理】";
                }
                if (auditProject.getStatus().equals(ZwfwConstant.BANJIAN_STATUS_DBB)) {
                    pretitle = "【待补办】";
                }
                // 发送待办事宜
                String title = pretitle + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                String messageItemGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                String beizhu = pretitle.replaceAll("【", "").replaceAll("】", "");
                messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(),
                        userSession.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), beizhu,
                        handleUrl, userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                        auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), new Date(),
                        auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            }
        }
        // 即办件+简易模式时，点击保存，插入待办
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(String.valueOf(audittask.getType()))
                && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditTask.getJbjmode())) {
            // 如果存在待办事宜，则不再插入待办
            List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                    null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null,
                    null, ZwfwConstant.CONSTANT_INT_ZERO, -1);
            if (messagesCenterList == null || messagesCenterList.isEmpty()) {
                // 发送待办事宜
                String title = "【待受理】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                String messageItemGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(),
                        userSession.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "待受理",
                        handleUrl, userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                        auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), new Date(),
                        auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            }
        }
        // 即办件+正常模式时，点击保存，插入待办
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(String.valueOf(audittask.getType()))
                && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTask.getJbjmode())) {
            // 如果存在待办事宜，则不再插入待办
            List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                    null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null,
                    null, ZwfwConstant.CONSTANT_INT_ZERO, -1);
            if (messagesCenterList == null || messagesCenterList.isEmpty()) {
                // 发送待办事宜
                String title = "【待接件】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
                String messageItemGuid = UUID.randomUUID().toString();
                String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                        ? auditTaskExtension.getCustomformurl()
                        : ZwfwConstant.CONSTANT_FORM_URL;
                String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
                        + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
                messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                        IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(),
                        userSession.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "待接件",
                        handleUrl, userSession.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
                        auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), new Date(),
                        auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            }
        }

        if (auditProjectFormSgxkz == null) {
            auditProjectFormSgxkz = new AuditProjectFormSgxkz();
            auditProjectFormSgxkz.setRowguid(UUID.randomUUID().toString());
            auditProjectFormSgxkz.setProjectguid(projectguid);
            auditProjectFormSgxkz.setOperatedate(new Date());
            auditProjectFormSgxkz.setOperateusername(userSession.getDisplayName());
            iAuditProjectFormSgxkzService.insert(auditProjectFormSgxkz);
        }
        else {
            iAuditProjectFormSgxkzService.update(auditProjectFormSgxkz);
        }
    }

    // 保存表单不发待办
    public void saveFormNoDB() {
        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        String isneedreceive = handleConfigService.getFrameConfig("AS_ISNEED_RECEIVE", centerGuid).getResult();
        AuditTask audittask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        if ((auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_INIT
                || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_DJJ
                || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_WWYSTG)
                && (ZwfwConstant.CONSTANT_STR_ZERO.equals(isneedreceive)
                        || (ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())
                                && !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())))) {
            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
        }
        // 使用物流
        if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            /*
             * String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_prov()); if
             * (StringUtil.isNotBlank(prov)) {
             * auditLogisticsBasicinfo.setRcv_prov(prov); } String city =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_city()); if
             * (StringUtil.isNotBlank(city)) {
             * auditLogisticsBasicinfo.setRcv_city(city); } String country =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_country()); if
             * (StringUtil.isNotBlank(country)) {
             * auditLogisticsBasicinfo.setRcv_country(country); }
             */
            auditLogisticsBasicinfo.setStatus("0");
            auditLogisticsBasicinfo.setCallState("05");// 初始化状态
            auditLogisticsBasicinfo.setNet_type("2");
            auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
            auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
            if (StringUtil.isBlank(auditLogisticsBasicinfo.getProjectguid())) {
                handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
            }
            else {
                handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
            }
        }
        handleProjectService.saveProject(auditProject);

    }

    /**
     * 保存“保存”操作的日志信息
     */
    public void saveOperateLog() {
        handleProjectService.saveOperateLog(auditProject, UserSession.getInstance().getUserGuid(),
                UserSession.getInstance().getDisplayName(), ZwfwConstant.OPERATE_BC, "");
    }

    /**
     * 提交按钮，用作受理和工作流通过类型的操作按钮
     *
     * @throws InterruptedException
     */
    public void acceptProject() throws InterruptedException {

        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
            addCallbackParam("message", "该步骤已经处理完成，请勿重复操作！");
            addCallbackParam("close", "1");
            return;
        }

        // 20240926 沟通取消受理步骤的推送
        // 如果是消防事项就推送到消防平台，不在走后续办件流程
        for (String taskname : tasknames) {
            if (auditProject.getProjectname().contains(taskname)) {
                // 推送到消防平台
                projectSendXf("false", "'" + auditProject.getRowguid() + "'");
                addCallbackParam("message", "办件已推送");
                addCallbackParam("close", "1");
                return;
            }
        }

        // 办件归集受理逻辑
        // TODO 先只限于 370827199511062018
        if ("370827199511062018".equals(auditProject.getCertnum())) {
            String report_status = auditProject.getStr("report_status");
            if (StringUtils.isBlank(report_status) || "0".equals(report_status)) {
                log.info("===============开始推送新办件归集方式办件===============");
                // ############# 新版本的上报 ###################
                ProjectReportByInterfaceService reportService = new ProjectReportByInterfaceServiceImpl();
                reportService.reportBasic(auditProject);
                // ############# 新版本的上报 ###################
                log.info("===============结束推送新办件归集方式办件===============");
            }
        }

        // 评价
        turnhcpevaluate(auditProject, 1, "提交申请信息",
                EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));

        // 保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if (StringUtil.isBlank(auditProject.getHandleareacode())) {
            areaStr = handleAreaCode + ",";
        }
        else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if (StringUtil.isNotBlank(areaStr)) {
            auditProject.setHandleareacode(areaStr);
            // auditProjectServcie.updateProject(auditProject);
        }

        boolean docflag1 = true;
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

        // 判断是否推送数据到浪潮
        String lc = zhenggaiImpl.getchargelc(auditProject.getTaskguid());
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(lc)) {
            Boolean reslut = pushInfolc();
            if (reslut.equals(false)) {
                return;
            }
        }

        String formid = auditTaskExtension.get("formid");
        String zjxt = auditTask.getStr("charge_zjxt");
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(zjxt) && "3".equals(formid)) {
            Boolean reslut = turnfcitembase();
            if (reslut.equals(false)) {
                return;
            }
        }

        // 新增事项中如果配置了默认处理人，改为不弹出窗口
        String isHaveHandleUser = "0";
        List<AuditTaskRiskpoint> riskpointList = auditTaskRiskpointimpl.getRiskPointListByTaksGuid(taskguid, true)
                .getResult();
        if (riskpointList != null && !riskpointList.isEmpty()) {
            for (AuditTaskRiskpoint auditTaskRiskpoint : riskpointList) {
                if (StringUtil.isNotBlank(auditTaskRiskpoint.getActivityname())
                        && "受理".equals(auditTaskRiskpoint.getActivityname())) {
                    String acceptguid = auditTaskRiskpoint.getAcceptguid();
                    // 如果受理环节有配置，则默认不弹出
                    if (StringUtil.isNotBlank(acceptguid)) {
                        isHaveHandleUser = "1";
                    }
                }
            }
        }
        addCallbackParam("isHaveHandleUser", isHaveHandleUser);

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
                boolean flag = isUserWordDoc();// 判断是否使用word
                EpointFrameDsManager.begin(null);
                HashMap<String, String> rtnValue = null;
                try {
                    rtnValue = new AuditWorkflowBizlogic().startProjectWorkflow(taskguid, projectguid,
                            auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                            UserSession.getInstance().getDisplayName());

                    EpointFrameDsManager.commit();
                }
                catch (Exception e) {
                    EpointFrameDsManager.rollback();
                }
                finally {

                    EpointFrameDsManager.close();

                }

                if (StringUtil.isNotBlank(rtnValue.get("message"))) {
                    addCallbackParam("message", rtnValue.get("message"));
                }
                else {
                    workItemGuid = rtnValue.get("workItemGuid");
                    addCallbackParam("workItemGuid", rtnValue.get("workItemGuid"));
                    addCallbackParam("operationGuid", rtnValue.get("operationGuid"));
                    addCallbackParam("transitionGuid", rtnValue.get("transitionGuid"));
                    addCallbackParam("pviGuid", rtnValue.get("pviGuid"));
                    addCallbackParam("isword", flag ? "1" : "0");
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
                // 即办件正常模式不弹受理通知书
                // if
                // (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType())))
                // {
                // docflag = "false";
                // }
                addCallbackParam("docflag", docflag);
                addCallbackParam("taskid", auditTask.getTask_id());
                addCallbackParam("taskguid", auditTask.getRowguid());
                addCallbackParam("userguid", userSession.getUserGuid());
                String stepguid = "";
                WorkflowWorkItem workItem = ihandleControlService.getWorkflowWorkItemByguid(workItemGuid);
                if (workItem != null) {
                    String processversionguid = workItem.getProcessVersionGuid();
                    WorkflowTransition transition = ihandleControlService
                            .getWorkflowTransitionByPvguid(processversionguid, "审核");
                    if (transition != null) {
                        stepguid = transition.getTransitionGuid();
                        addCallbackParam("stepguid", stepguid);
                    }
                }
                AuditTaskRiskpoint riskpoint = ihandleControlService.getRiskpoint(taskguid, "审核");
                String texts = "";
                String values = "";
                if (riskpoint != null) {
                    String userguids = riskpoint.getAcceptguid();
                    if (StringUtil.isNotBlank(userguids)) {
                        String guids[] = userguids.split(";");
                        for (String str : guids) {
                            values += str + ",";
                        }
                        addCallbackParam("values", values);
                    }
                    String usernames = riskpoint.getAcceptname();
                    if (StringUtil.isNotBlank(usernames)) {
                        String names[] = usernames.split(";");
                        for (String str : names) {
                            texts += str + ",";
                        }
                        addCallbackParam("texts", texts);
                    }

                }

            }

        }
        else {
            // TODO
            EpointFrameDsManager.begin(null);
            // 即办件，直接更新状态待办结状态,并且发送消息
            if (auditTask != null) {
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && ZwfwUserSession.getInstance().getCitylevel().equals(ZwfwConstant.AREA_TYPE_XZJ)) {

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

            // 非网上申报需要上报办件数据
            /*
             * if (auditProject.getApplyway() !=
             * Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS) &&
             * auditProject.getApplyway() !=
             * Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {}
             */

            AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                    .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
            String areaName = "";
            if (auditOrgaArea != null) {
                areaName = auditOrgaArea.getXiaquname();
            }
            // 测试办件不生成
            /*
             * if (auditProject.getIs_test() !=
             * Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) { String msg =
             * "handleAccept:" + auditProject.getRowguid() + ";" +
             * auditProject.getAreacode(); // 测试办件不生成 if
             * (auditProject.getIs_test() !=
             * Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
             * ProducerMQ.sendByExchange("receiveproject", msg, ""); //接办分离 受理
             * msg =
             * auditProject.getRowguid() + "." + auditProject.getAreacode();
             * sendMQMessageService.sendByExchange("exchange_handle", msg,
             * "project."+ZwfwUserSession.getInstance().getAreaCode()+".accept."
             * +auditProject.getTask_id()); }
             *
             * }
             */

            handleProjectService.handleApprove(auditProject, UserSession.getInstance().getDisplayName(),
                    UserSession.getInstance().getUserGuid(), workItemGuid, null);

            addCallbackParam("message", "refresh");

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
        }
        // 使用物流
        if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            /*
             * String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_prov()); if
             * (StringUtil.isNotBlank(prov)) {
             * auditLogisticsBasicinfo.setRcv_prov(prov); } String city =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_city()); if
             * (StringUtil.isNotBlank(city)) {
             * auditLogisticsBasicinfo.setRcv_city(city); } String country =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_country()); if
             * (StringUtil.isNotBlank(country)) {
             * auditLogisticsBasicinfo.setRcv_country(country); }
             */
            auditLogisticsBasicinfo.setStatus("0");
            auditLogisticsBasicinfo.setCallState("05");// 初始化状态
            auditLogisticsBasicinfo.setNet_type("2");
            auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
            auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
            if (StringUtil.isBlank(auditLogisticsBasicinfo.getProjectguid())) {
                handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
            }
            else {
                handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
            }
            handleProjectService.saveProject(auditProject);
        }
        // 如果存在待办事宜，则删除待办
        List<FrameUser> users = new ArrayList<FrameUser>();
        ;
        List<MessagesCenter> messagesCenterList = new ArrayList<MessagesCenter>();
        List<MessagesCenter> messagesList = new ArrayList<MessagesCenter>();
        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            users = iAuditOrgaWindow.getFrameUserByTaskID(auditProject.getTask_id()).getResult();
            for (FrameUser user : users) {
                messagesList = messageCenterService.queryForList(user.getUserGuid(), null, null, "",
                        IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                        -1);
                if (messagesList != null && !messagesList.isEmpty()) {
                    messagesCenterList.addAll(messagesList);
                }
            }
        }
        else {
            messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null, null, "",
                    IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
        }

        if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
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
                    ? auditTaskExtension.getCustomformurl()
                    : ZwfwConstant.CONSTANT_FORM_URL;
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
        // 不使用一窗，
        boolean useYcsl = ycslService.useYcslByTaskExtensionObj1(auditTaskExtension);
        // logger.info("useYcsl=" + useYcsl);
        if (useYcsl) {
            pushReceiveInfo2SelfBuildSystem();
        }
        // 调用电力接口推送办件状态及信息
        if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YSL
                || auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
            if (StringUtil.isNotBlank(auditProject.getBusinessguid()) && "4".equals(auditProject.getStr("is_lczj"))) {
                auditProject.setStatus(30);
                auditProject.setAcceptusername(userSession.getDisplayName());
                auditProject.setAcceptuserdate(new Date());
                handleProjectService.saveProject(auditProject);
                logger.info("调用电力接口推送办件状态及信息=========>>>>>>>>受理:" + auditProject.getStatus());
                new ElectricityController().pushStatusToEc(auditProject, auditTask, null);
            }
        }
        // 刷新属地办理信息
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
                // 代办预审通过
                // auditProjectSamecity.setStatus(LygYcslConstant.YCSL_DBSTATUS_DBYSTG);
                iAuditProjectSamecityService.update(auditProjectSamecity);
            }
        }

        // 表单信息
        if (auditProjectFormSgxkz != null && StringUtil.isNotBlank(auditProjectFormSgxkz.getXiangmumingchen())) {
            // System.err.println(auditProjectFormSgxkz);
            AuditProjectFormSgxkz auditProjectFormSgxkz1 = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
            if (auditProjectFormSgxkz1 != null) {
                iAuditProjectFormSgxkzService.update(auditProjectFormSgxkz);
            }
            else {
                iAuditProjectFormSgxkzService.insert(auditProjectFormSgxkz);
            }
        }

        if ("370829".equals(auditProject.getAreacode()) || "370882".equals(auditProject.getAreacode())
                || "370881".equals(auditProject.getAreacode()) || "370827".equals(auditProject.getAreacode())
                || "370892".equals(auditProject.getAreacode()) || "370830".equals(auditProject.getAreacode())
                || "370831".equals(auditProject.getAreacode()) || "370891".equals(auditProject.getAreacode())
                || "370890".equals(auditProject.getAreacode()) || "370832".equals(auditProject.getAreacode())
                || "370826".equals(auditProject.getAreacode()) || "370800".equals(auditProject.getAreacode())
                || "370828".equals(auditProject.getAreacode()) || "370883".equals(auditProject.getAreacode())) {
            evaluate("2");

            // turnhcpevaluate(auditProject,
            // 2,"出证办结",EpointDateUtil.convertDate2String(new Date(),
            // "yyyy-MM-dd HH:mm:ss"));

        }
        else {
            if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {
                evaluate("");
            }
        }

        JSONObject submit = new JSONObject();
        JSONObject params = new JSONObject();
        params.put("projectguid", auditProject.getRowguid());
        params.put("areacode", auditProject.getAreacode());
        params.put("acceptusername",
                StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername() : "一窗系统");
        submit.put("params", params);
        String resultsign = "";
        JSONObject submit2 = new JSONObject();
        submit2.put("projectGuid", auditProject.getRowguid());
        submit2.put("areaCode", auditProject.getAreacode());
        params.put("acceptusername",
                StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername() : "一窗系统");
        submit2.put("status", "01");

        String resultsign2 = "";
        if ("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
            resultsign = TARequestUtil.sendPostInner(lcjbxxurl, submit.toJSONString(), "", "");
            Thread.sleep(2000);
            resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
        }
        // 推送mq消息,同步操作给
        log.info("预审推送信息给省空间对接！");

        log.info("预审推送信息给省空间对接，流水编号：" + auditProject.getFlowsn() + ":" + auditProject.getRowguid() + "----"
                + auditProject.getAreacode());
        sendMQMessageService.sendByExchange("zwdt_exchange_handle",
                auditProject.getRowguid() + "@" + auditProject.getAreacode(), "space.saving.docking.1");

        // 推送办件信息表到住建
        projectSendzj("false", "'" + auditProject.getRowguid() + "'");
    }

    /**
     * 推送接件/受理信息给浪潮
     *
     * @author 徐本能
     */
    private Boolean pushInfolc() {
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        // logger.info("推送数据到浪潮");
        ycslData.put("useSelfBuildSystem", true);
        Boolean success = ycslService.ycsllcbyprojectandtask(auditTask, auditProject);
        ycslData.put("success", success);

        addCallbackParam("ycslData", ycslData.toJSONString());
        return success;
    }

    /**
     * 材料验证不通过时处理
     */
    public void validatePZMaterial() {
        String unusualGuid = handleProjectService.handleMaterialNotPass(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), workItemGuid).getResult();
        if (StringUtil.isNotBlank(unusualGuid)) {
            // 测试办件不生成
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                // MQ 特殊操作
                String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + unusualGuid
                        + "&handleSpecialResult:" + projectguid + ";" + unusualGuid;
                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // 接办分离 特殊操作
                msg = projectguid + "." + auditProject.getApplyername() + "." + unusualGuid;
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".special." + auditProject.getTask_id());
                // 接办分离 特殊操作结果
                msg = projectguid + "." + unusualGuid;
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTask_id());

            }
        }
        try {
            addCallbackParam("message", ButtonControl());
        }
        catch (JSONException e) {
        }
    }

    /**
     * [是否线下窗口登记]
     */
    public void isXxckdj() {
        // 判断该办件是否为线下窗口登记的办件，applyway = 20，且该事项配置了【是否可纸质】(is_tssgs)为是
        Integer applyway = auditProject.getApplyway();
        String is_tssgs = auditTaskExtension.getStr("is_tssgs");
        if (applyway == 20 && StringUtil.isNotBlank(is_tssgs) && "true".equals(is_tssgs)) {
            // 是线下窗口登记的办件，且可纸质
            addCallbackParam("isxxckdj", "1");
        }
        else {
            addCallbackParam("isxxckdj", "0");
        }
    }

    /**
     * [audit_project_material.status是不是都 >= 15]
     */
    public void validateMaterialStatus() {
        if (auditProject != null) {
            List<AuditProjectMaterial> list = iAuditProjectMaterial.selectProjectMaterial(auditProject.getRowguid())
                    .getResult();
            if (!list.isEmpty()) {
                // 检查未通过,返回材料状态为10的个数
                int i = 0;
                for (AuditProjectMaterial auditProjectMaterial : list) {
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    // 必要材料验证
                    if (auditTaskMaterial != null) {
                        if (auditTaskMaterial.getNecessity() == 10) {
                            Integer status = auditProjectMaterial.getStatus();
                            if (status == 10) {
                                i = i + 1;
                            }
                        }
                    }

                }

                if (i > 0) {
                    // 返回未提交个数
                    addCallbackParam("wtj", i);
                }

            }
        }
    }

    /**
     * 批准操作
     */
    public void piZhunProject() {
        if ((auditProject.getBanjieresult() == null ? 0
                : auditProject.getBanjieresult()) == ZwfwConstant.BANJIE_TYPE_BYXK) {
            addCallbackParam("warn", "该办件已不予批准，请勿重复处理！");
            return;
        }
        // TODO messageItemGuid 需要赋值的
        String messageItemGuid = getRequestParameter("MessageItemGuid");
        String[] mStrings = handleProjectService.handleApprove(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), workItemGuid, messageItemGuid).getResult().split("@");
        String message = mStrings[0];
        if (mStrings.length > 1 && StringUtil.isNotBlank(mStrings[1])) {
            // 测试办件不生成
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                // MQ 特殊操作
                String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + mStrings[1]
                        + "&handleSpecialResult:" + projectguid + ";" + mStrings[1];
                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // 接办分离 特殊操作
                msg = projectguid + "." + auditProject.getApplyername() + "." + mStrings[1];
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".special." + auditProject.getTask_id());
                // 接办分离 特殊操作结果
                msg = projectguid + "." + mStrings[1];
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTask_id());

                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    msg = "handleSpecial:" + projectguid + ";" + mStrings[1] + ";" + auditProject.getAreacode()
                            + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                }
            }
        }
        try {
            JSONObject jsonBack = JSONObject.parseObject(ButtonControl());
            // jsonBack.put("message", message);
            addCallbackParam("docmessage", message);
            addCallbackParam("message", jsonBack.toString());
        }
        catch (JSONException e) {
        }

    }

    /**
     * 不予批准操作
     */
    public void buYuPiZhunProject() {
        if ((auditProject.getBanjieresult() == null ? 0
                : auditProject.getBanjieresult()) == ZwfwConstant.BANJIE_TYPE_ZYXK) {
            addCallbackParam("warn", "该办件已被批准，请勿重复处理！");
            return;
        }
        // TODO messageItemGuid需要赋值的
        String messageItemGuid = "";
        String[] mStrings = handleProjectService.handleNotApprove(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), workItemGuid, messageItemGuid).getResult().split("@");
        String message = mStrings[0];
        if (mStrings.length > 1 && StringUtil.isNotBlank(mStrings[1])) {
            // MQ 特殊操作
            // 测试办件不生成
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + mStrings[1]
                        + "&handleSpecialResult:" + projectguid + ";" + mStrings[1];
                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // 接办分离 特殊操作
                msg = projectguid + "." + auditProject.getApplyername() + "." + mStrings[1];
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".special." + auditProject.getTask_id());
                // 接办分离 特殊操作结果
                msg = projectguid + "." + mStrings[1];
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTask_id());

                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    msg = "handleSpecial:" + projectguid + ";" + mStrings[1] + ";" + auditProject.getAreacode()
                            + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                }
            }
        }
        try {
            JSONObject jsonBack = JSONObject.parseObject(ButtonControl());
            addCallbackParam("docmessage", message);
            addCallbackParam("message", jsonBack.toString());
        }
        catch (JSONException e) {
        }
    }

    /**
     * 办结操作
     */
    public void banJieProject() {

        JSONObject submit2 = new JSONObject();

        submit2.put("projectGuid", auditProject.getRowguid());
        submit2.put("areaCode", auditProject.getAreacode());
        submit2.put("status", "04");

        String resultsign2 = "";

        // 获取事项信息
        AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();

        if ("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
            resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
        }

        handleProjectService.handleFinish(auditProject, userSession.getDisplayName(), userSession.getUserGuid(),
                workItemGuid);

        // biguid不为空则为并联审批
        if (auditProject.getBiguid() != null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("biguid", auditProject.getBiguid());
            List<AuditProject> auditProjects = auditProjectServcie.getAuditProjectListByCondition(sql.getMap())
                    .getResult();
            boolean check = true;
            for (AuditProject auditproject : auditProjects) {
                if (auditproject.getStatus() >= 90) {
                    check = true;
                }
                else {
                    check = false;
                    break;
                }
            }
            if (check) {
                AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(auditProject.getBiguid())
                        .getResult();
                String itemName = auditSpInstance.getItemname();
                iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                        auditProject.getAcceptuserguid(), "", auditProject.getAcceptuserguid(),
                        "您发起的项目 " + itemName + "事项已全部办理完毕！", new Date());
            }
        }

        // 如果存在待办事宜，则删除待办
        List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                -1);
        if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
            }
        }
        // 即办件
        String msg = "";
        // 测试办件不生成
        if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
            // MQ 办结操作
            msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                    + UserSession.getInstance().getDisplayName();
            ProducerMQ.sendByExchange("receiveproject", msg, "");
            // 接办分离 办结操作
            msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "."
                    + UserSession.getInstance().getDisplayName();
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".sendresult." + auditProject.getTask_id());

            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                // 江苏省省标环节信息
                msg = "handleJBJProcess:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                        + ZwfwConstant.OPERATE_BJ + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // MQ 江苏省标
                msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                        + UserSession.getInstance().getDisplayName()
                        + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                ProducerMQ.sendByExchange("receiveproject", msg, "");
            }
        }

        // 办件归集其他信息
        // TODO 先只限于 370827199511062018
        if ("370827199511062018".equals(auditProject.getCertnum())) {
            String report_status = auditProject.getStr("report_status");
            if (StringUtils.isBlank(report_status) || "0".equals(report_status)) {
                log.info("===============开始推送新办件归集方式办件===============");
                // ############# 新版本的上报 ###################
                ProjectReportByInterfaceService reportService = new ProjectReportByInterfaceServiceImpl();
                reportService.startReport(new ArrayList<AuditProject>()
                {
                    {
                        add(auditProject);
                    }
                });
                // ############# 新版本的上报 ###################
                log.info("===============结束推送新办件归集方式办件===============");
            }
        }

        // *************开始**************
        // edit by yrchan,2022-04-15,判断该办件辖区是否为370883，若是，则插表评价办件信息表
        // 审批办件仅需保留邹城市级办件，无需算入镇街 ，这项筛选条件改为通过办件ouguid字段关联部门拓展表查询areacode
        AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
        if (task != null && task.getType() == 1 && StringUtil.isNotBlank(auditProject.getOuguid())) {
            // 即办件
            FrameOuExtendInfo frameOuExtendInfo = ouservice.getFrameOuExtendInfo(auditProject.getOuguid());
            if (frameOuExtendInfo != null && "370883".equals(frameOuExtendInfo.getStr("areacode"))) {
                IEvaluateProjectService iEvaluateProjectService = ContainerFactory.getContainInfo()
                        .getComponent(IEvaluateProjectService.class);
                Date nowDate = new Date();
                // 存放在评价办件信息表（evaluate_project）
                EvaluateProject evaluateProject = new EvaluateProject();
                evaluateProject.setRowguid(UUID.randomUUID().toString());
                evaluateProject.setOperatedate(nowDate);
                evaluateProject.setOperateusername(UserSession.getInstance().getDisplayName());
                evaluateProject.setCreat_date(nowDate);
                evaluateProject.setAccept_user(auditProject.getAcceptusername());
                evaluateProject.setAccept_department(ouservice.getOuNameByUserGuid(auditProject.getAcceptuserguid()));
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
                boolean isExistBj = iEvaluateProjectService.isExistPhoneAndHandleDate(evaluateProject.getLink_phone(),
                        EpointDateUtil.convertDate2String(evaluateProject.getHandle_date(),
                                EpointDateUtil.DATE_FORMAT));
                // 若存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,不发短信
                if (isExistBj) {
                    iEvaluateProjectService.insert(evaluateProject);
                }
                else {
                    // 若不存在，则将成功信息入库在评价办件信息表（evaluate_project）表中,并发短信
                    iEvaluateProjectService.insert(evaluateProject);
                    // 获取系统参数：评价短信发送模板
                    String content = configServicce.getFrameConfigValue("EAVL_MSG_CONTENT");

                    // 发短信
                    String contentText = content.replaceAll("#=TASK_NAME=#", evaluateProject.getTask_name());
                    messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), contentText, nowDate, 0,
                            nowDate, evaluateProject.getLink_phone(), "-", evaluateProject.getLink_user(),
                            UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                            evaluateProject.getLink_phone(), UserSession.getInstance().getOuGuid(), "", true, "370883");
                }
                EpointFrameDsManager.commit();
            }
        }
        // *************结束**************
        // 推送浪潮归集库/省前置库
        WavePushInterfaceUtils wavePushInterfaceUtils = new WavePushInterfaceUtils();
        wavePushInterfaceUtils.TsLcGjk(auditProject, auditTask, userSession);
        addCallbackParam("message", "办结成功");

        // 推送mq消息,同步操作给

        sendMQMessageService.sendByExchange("zwdt_exchange_handle",
                auditProject.getRowguid() + "@" + auditProject.getAreacode(), "space.saving.docking.1");
    }

    /**
     * 施工许可证推送大数据前置库
     */
    public void sgxkzts() {
        Record ylgg = new Record();
        ylgg.setSql_TableName("ex_jzgcsgxkzx_1");
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
        if (certinfo != null) {
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                certinfoExtension = new CertInfoExtension();
            }

            ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh")); // 照面编号
            ylgg.set("PROJECT_NAME", certinfoExtension.getStr("xiangmumingchen")); // 项目名称
            ylgg.set("JIANSHEDIZHI", certinfoExtension.getStr("xiangmudidian")); // 建设地址
            ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jiansheguimo")); // 建设规模
            ylgg.set("HETONGJIAGE", certinfoExtension.getStr("hetongjiagewanyuan")); // 合同价格
            ylgg.set("KANCHADANWEI", certinfoExtension.getStr("kanchadanwei")); // 勘察单位
            ylgg.set("SHEJIDANWEI", certinfoExtension.getStr("shejidanwei")); // 设计单位
            // 施工单位
            ylgg.set("SHIGONGDANWEI", certinfoExtension.getStr("shigongzongchengbaoqiye"));
            ylgg.set("JIANLIDANWEI", certinfoExtension.getStr("jianlidanwei")); // 监理单位
            ylgg.set("JIANSHEDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("jianshedanweixiangmufuzeren")); // 建设单位项目负责人
            ylgg.set("KANCHAXIANGMUFUZEREN", certinfoExtension.getStr("kanchadanweixiangmufuzeren")); // 勘察项目负责人
            ylgg.set("SHEJIXIANGMUFUZEREN", certinfoExtension.getStr("shejidanweixiangmufuzeren")); // 设计项目负责人
            // 施工负责人
            ylgg.set("SHIGONGXIANGMUFUZEREN", certinfoExtension.getStr("shigongzongchengbaoqiyexiangmu"));
            ylgg.set("ZONGJIANLIGONGCHENGSHI", certinfoExtension.getStr("zongjianligongchengshi")); // 总监理工程师
            ylgg.set("JIHUAKAIGONGRIQI", certinfoExtension.getStr("hetongkaigongriqi")); // 计划开工日期
            ylgg.set("ZHENGZHAOBEIZHU", certinfoExtension.getStr("bz")); // 证照备注
            ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jianshedanweimingchen")); // 建设单位
            ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("xiangmumingchen")); // 工程名称
            ylgg.set("JIHUAJUNGONGRIQI", certinfoExtension.getStr("hetongjungongriqi")); // 计划竣工日期
            ylgg.set("DEPT_NAME", certinfoExtension.getStr("bzdw")); // 部门名称
            ylgg.set("BDCDYH", certinfoExtension.getStr("bdcdyh")); // 不动产单元号

            // CERTIFICATE_DATE 发证时间-----------------------------当天
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ylgg.set("CERTIFICATE_DATE", format.format(new Date()));
            // VALID_PERIOD_BEGIN 有效期开始---------------------当天
            ylgg.set("VALID_PERIOD_BEGIN", format.format(new Date()));
            // VALID_PERIOD_END 有效期截止------------------------9999-12-31
            ylgg.set("VALID_PERIOD_END", "9999-12-31");
            // CERTIFICATE_LEVEL 可信等级,填写‘A’-------------A
            ylgg.set("CERTIFICATE_LEVEL", "A");
            // SUOSHUXIANQU 照面项：所属县区-------------------高新区
            AuditOrgaArea orgarea = iAuditOrgaArea.getAreaByAreacode(auditProject.getAreacode()).getResult();
            ylgg.set("SUOSHUXIANQU", orgarea.getXiaquname());

            String areacode = ZwfwUserSession.getInstance().getAreaCode();
            if (areacode.length() == 6) {
                areacode = areacode + "000000";
            }
            else if (areacode.length() == 9) {
                areacode = areacode + "000";
            }

            ylgg.set("operatedate", new Date());

            int APPLYERTYPE = auditProject.getApplyertype();
            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                if ("20".equals(APPLYERTYPE + "")) {
                    ylgg.set("HOLDER_TYPE", "自然人");
                }
            }
            else {
                ylgg.set("HOLDER_TYPE", "--");
            }
            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
            ylgg.set("CERTIFICATE_TYPE",
                    codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
            // 联系方式
            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

            // 证照颁发单位
            ylgg.set("DEPT_NAME", userSession.getOuName());

            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
            if (frameOuExten != null) {
                // 证照办法单位组织机构代码
                ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
            }
            // 行政区划名称
            ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                    .getResult().getXiaquname());
            // 行政区划编码
            ylgg.set("DISTRICTS_CODE", areacode);

            ylgg.set("rowguid", UUID.randomUUID().toString());
            ylgg.setPrimaryKeys("rowguid");

            ylgg.set("state", "insert");
            iJnProjectService.inserRecord(ylgg);
        }
    }

    /**
     * 推送大数据拓展
     */
    private void pushbigdata1() {
        FinishAuditProjectInterface finishAuditProjectInterface = FactoryUtil
                .getFinishAuditProjectService(auditTask.getTask_id());
        if (finishAuditProjectInterface != null) {
            finishAuditProjectInterface.handleBusiness(auditProject, auditTask);
        }
        // 道路运输经营许可证(梁山）
        if (auditProject != null && ("11370832MB2855272B4370118500501".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_ZHRMGHGDLYSJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", "鲁交运管许可宁字" + certinfoExtension.getStr("hao") + "号");
                // ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("hao"));
                String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String sxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrq"), "yyyy-MM-dd");
                String shixrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("shixrq"), "yyyy-MM-dd");
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", shixrq);
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", fzrq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", sxrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                // 许可内容
                ylgg.set("PERMIT_CONTENT", "--");
                // 默认'A'
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DIZHI", certinfoExtension.getStr("dz"));
                ylgg.set("YEHUMINGCHENG", certinfoExtension.getStr("yhmc"));
                ylgg.set("JINGYINGFANWEI", certinfoExtension.getStr("jyfw"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_ZHRMGHGDLYSJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 劳务派遣经营许可证(梁山）
        if (auditProject != null && ("11370832MB2855272B4370114004001".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_LWPQJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String sxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrq"), "yyyy-MM-dd");
                String sxrqi = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrqi"), "yyyy-MM-dd");
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", sxrqi);
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", fzrq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", sxrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHENSHUJU", certinfoExtension.getStr("dwmc"));
                ylgg.set("ZHUSUOSHUJU", certinfoExtension.getStr("zs"));
                ylgg.set("FADINGDAIBIAORENSHUJU", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUCEZIBENSHUJU", certinfoExtension.getStr("zczb"));
                ylgg.set("XUKEJINGYINGSHIXIANGSHUJU", certinfoExtension.getStr("xkjysx"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_LWPQJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 取水许可证(梁山）
        if (auditProject != null && ("11370832MB2855272B4370119001001".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_QSXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String sxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrq"), "yyyy-MM-dd");
                String sxrqi = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrqa"), "yyyy-MM-dd");
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", sxrqi);
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", fzrq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", sxrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("dwmc"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("xydm"));
                ylgg.set("QUSHUIDIDIAN", certinfoExtension.getStr("qsdd"));
                ylgg.set("SHUIYUANLEIXINGER", certinfoExtension.getStr("sylx"));
                ylgg.set("QUSHUIYONGTUER", certinfoExtension.getStr("qsyt"));
                ylgg.set("QUSHUILEIXING", certinfoExtension.getStr("qslx"));
                ylgg.set("QUSHUILIANG", certinfoExtension.getStr("qsl"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_QSXKZ_1", ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 人力资源服务许可证(梁山）
        if (auditProject != null && ("11370832MB2855272B4370114011001".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_RLZYFWXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String sxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sxrq"), "yyyy-MM-dd");
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", sxrq);
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", fzrq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", fzrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIGOUMINGCHEN", certinfoExtension.getStr("jgmc"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyshxydm"));
                ylgg.set("DIZHI", certinfoExtension.getStr("dz"));
                ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("JIGOUXINGZHI", certinfoExtension.getStr("jgxz"));
                ylgg.set("XUKEWENHAO", certinfoExtension.getStr("xkwh"));
                ylgg.set("FUWUFANWEI", certinfoExtension.getStr("fwfw"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_RLZYFWXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 出版物经营许可证(梁山）
        if (auditProject != null && ("11370832MB2855272B437017300100201".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_ZHRMGHGCBWJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                // 获取发证日期和有效期截至
                String n = certinfoExtension.getStr("n");
                String y = certinfoExtension.getStr("y");
                if (y.length() < 2) {
                    y = 0 + y;
                }
                String r = certinfoExtension.getStr("r");
                if (r.length() < 2) {
                    r = 0 + r;
                }
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", n + "-" + y + "-" + r);

                String na = certinfoExtension.getStr("na");
                String ya = certinfoExtension.getStr("ya");
                if (ya.length() < 2) {
                    ya = 0 + ya;
                }
                String ra = certinfoExtension.getStr("ra");
                if (ra.length() < 2) {
                    ra = 0 + ra;
                }
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", na + "-" + ya + "-" + ra);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", na + "-" + ya + "-" + ra);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
                ylgg.set("JINGYINGDIZHIYI", certinfoExtension.getStr("jydz"));
                ylgg.set("FADINGDAIBIAORENER", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUCEZIBENER", certinfoExtension.getStr("zczb"));
                ylgg.set("QIYELEIXING", certinfoExtension.getStr("qylx"));
                ylgg.set("JINGYINGFANWEIER", certinfoExtension.getStr("jyfw"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_ZHRMGHGCBWJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // TODO 通过事项名称进行跳转到各自方法里处理
        if (auditProject != null && StringUtils.isNotBlank(auditTask.getTaskname())) {
            switch (auditTask.getTaskname()) {
                case "房屋建筑和市政基础设施工程竣工验收备案":
                    pushfwjzandszjc();
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 映射办件applytype到推送竣工验收到大数据的映射
     * 大数据： 持有者类型:1-自然人、2-法人、3-混合、4-其他
     * 
     * @param applytype
     * @return
     */
    private String getJgysHolderCator(String applytype) {
        String result = "4";
        switch (applytype) {
            case "20":
                result = "1";
            case "10":
                result = "2";
                break;
        }
        return result;
    }

    /**
     * 映射办件certtype到推送竣工验收到大数据的映射
     * 大数据： 持有者证件类型:516港澳居民来往内地通行证,001统一社会信用代码,099其他法人证件,111居民身份证,999其他自然人证件
     * 
     * @param certtype
     * @return
     */
    private String getJgysHolderType(String certtype) {
        String result = "999";
        switch (certtype) {
            case "22":
                result = "111";
            case "25":
                result = "516";
                break;
            case "16":
                result = "001";
                break;
        }
        return result;
    }

    /**
     * 房屋建筑和市政基础设施工程竣工验收备案
     */
    public void pushfwjzandszjc() {
        JnGxhSendUtils utils = new JnGxhSendUtils();
        utils.SendCertInfoJgba(auditProject);
    }

    public void banjieAfter() {
        log.info("开始进入banjieAfter方法，flowsn：" + auditProject.getFlowsn());
        // 放射诊疗一件事数据入库
        try {
            JnAuditFszlDataUpdate.updateFszlData(auditProject);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // todo
        // 非税对接
        try {
            FeishuiduijieBizlogic.startFeiShui(auditProject);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 调用电力接口推送办件状态及信息
    public void banjieElectricity() {
        log.info("开始进入banjieElectricity方法，flowsn：" + auditProject.getFlowsn());
        if (StringUtil.isNotBlank(auditProject.getBusinessguid()) && "4".equals(auditProject.getStr("is_lczj"))) {
            logger.info("调用电力接口推送办件状态及信息=========>>>>>>>>办结：" + auditProject.getStatus());
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            new ElectricityController().pushStatusToEc(auditProject, auditTask, "同意");
        }
    }

    /**
     * 办结推送大数据
     */
    public void banjiePushBigData() {
        banjiePushBigDataMethod(auditProject, auditTask);
    }

    public void banjiePushBigDataMethod(AuditProject paramAuditProject, AuditTask paramAuditTask) {
        if (paramAuditProject != null) {
            auditProject = paramAuditProject;
        }
        if (paramAuditTask != null) {
            auditTask = paramAuditTask;
        }
        log.info("开始进入banjiePushBigData方法，flowsn：" + auditProject.getFlowsn());
        log.info("ITEMID：" + auditTask.getItem_id());
        JSONObject submit2 = new JSONObject();
        submit2.put("projectGuid", auditProject.getRowguid());
        submit2.put("areaCode", auditProject.getAreacode());
        submit2.put("status", "04");

        FrameOuExtendInfo frameOuExten1 = ouservice.getFrameOuExtendInfo(auditProject.getOuguid());

        String xzareacode = frameOuExten1.getStr("areacode");

        // 河道管理范围内建设项目工程建设方案审查 事项
        if ("11370800MB285591847370119022002".equals(auditTask.getItem_id())) {
            sendCertInfoToQzk(auditProject);
        }

        // 推送施工许可信息
        if ("建筑工程施工许可证核发".equals(auditTask.getTaskname()) && auditProject != null) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                if ("鱼台防空地下室建设审批意见书".equals(certinfo.getCertname())) {
                    Record ylgg = new Record();
                    ylgg.setSql_TableName("EX_FKDXSYDJSSPYJS_1");
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                    String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                    String yearz = certinfoExtension.getStr("ZN");
                    String monthz = certinfoExtension.getStr("ZY");
                    String dayz = certinfoExtension.getStr("ZR");

                    // 发证日期x
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yearz + "-" + monthz + "-" + dayz);

                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("PERMIT_CONTENT", "--");
                    ylgg.set("CERTIFICATE_LEVEL", "A");

                    ylgg.set("YIDIJIANSHEFEIJIAONABIAOZHUN", "");
                    ylgg.set("ZHANSHIYONGTU", certinfoExtension.getStr("zsyt"));
                    ylgg.set("JIANSHEXIANGMU", certinfoExtension.getStr("xmmc"));
                    ylgg.set("FANGHULEIBIE", certinfoExtension.getStr("fhlb"));
                    ylgg.set("DIXIAJIANZHUMIANJI", certinfoExtension.getStr("dxjzmj"));
                    ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                    ylgg.set("JIANSHEDIDIAN", certinfoExtension.getStr("jsdd"));
                    ylgg.set("KANGLIDENGJI", certinfoExtension.getStr("kldj"));
                    ylgg.set("YINGJIANFANGKONGDIXIASHI", certinfoExtension.getStr("yjfkdxs"));
                    ylgg.set("DISHANGJIANZHUMIANJI", certinfoExtension.getStr("dsjzmj"));
                    ylgg.set("YIDIJIANSHEFEIJIAONASHUE", "");
                    ylgg.set("BEIZHU", "");

                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }

                    ylgg.set("operatedate", new Date());

                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", userSession.getOuName());

                    FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                    if (frameOuExten != null) {
                        // 证照办法单位组织机构代码
                        ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    }
                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");

                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
                else {

                    String projectguid = auditProject.getRowguid();
                    Record iteminfo = iGsProjectService.getSpglSgxkIteminfoBySubappguid(projectguid);
                    if (iteminfo != null) {
                        String itemrowguid = iteminfo.getStr("ID");
                        Record item = iGsProjectService.getProjectBasicInfoByRowguid(itemrowguid);
                        if (item == null) {
                            iteminfo.setSql_TableName("TB_Project_Info");
                            iteminfo.setPrimaryKeys("id");
                            AuditSpSpSgxk sgxk = iGsProjectService.getSpSgxkByRowguid(auditProject.getSubappguid());
                            if (sgxk != null) {
                                iteminfo.set("Prj_Approval_Depart", sgxk.getStr("lxpfjg"));
                                iteminfo.set("Prj_Approval_Date", EpointDateUtil
                                        .convertDate2String(sgxk.getDate("lxpfsj"), EpointDateUtil.DATE_FORMAT));
                            }
                            else {
                                AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(itemrowguid).getResult();
                                iteminfo.set("Prj_Approval_Depart", auditProject.getOuname());
                                iteminfo.set("Prj_Approval_Date", EpointDateUtil
                                        .convertDate2String(baseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                            }
                            iGsProjectService.insertQzk(iteminfo);
                        }
                    }

                    Record danti = iGsProjectService.getSpglDantiInfoBySubappguid(projectguid);
                    if (danti != null) {
                        danti.setSql_TableName("TB_Unit_Project_Info");
                        danti.setPrimaryKeys("id");
                        iGsProjectService.insertQzk(danti);
                    }

                    List<Record> Partics = iGsProjectService.getSpglParticipnListBySubappguid(projectguid);
                    if (!Partics.isEmpty()) {
                        for (Record Partic : Partics) {
                            Partic.setSql_TableName("TB_Project_Corp_Info");
                            Partic.setPrimaryKeys("id");
                            iGsProjectService.insertQzk(Partic);
                        }
                    }

                    Record sgxkz = iGsProjectService.getSpglSgxkInfoBySubappguid(projectguid);
                    if (sgxkz != null) {
                        sgxkz.setSql_TableName("TB_Builder_Licence_Manage");
                        sgxkz.setPrimaryKeys("id");
                        CertInfoExtension certinfoExtension = null;
                        // 获取照面信息
                        Map<String, Object> filter = new HashMap<>();
                        // 设置基本信息guid
                        filter.put("certinfoguid", auditProject.getCertrowguid());
                        certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                        String Builder_Licence_Num = "";
                        if (certinfoExtension != null) {
                            Builder_Licence_Num = certinfoExtension.getStr("bh");
                        }
                        else {
                            Builder_Licence_Num = "无";
                        }
                        sgxkz.set("Builder_Licence_Num", Builder_Licence_Num);
                        iGsProjectService.insertQzk(sgxkz);
                    }
                }
            }
            else {
                String projectguid = auditProject.getRowguid();
                Record iteminfo = iGsProjectService.getSpglSgxkIteminfoBySubappguid(projectguid);
                if (iteminfo != null) {
                    String itemrowguid = iteminfo.getStr("ID");
                    Record item = iGsProjectService.getProjectBasicInfoByRowguid(itemrowguid);
                    if (item == null) {
                        iteminfo.setSql_TableName("TB_Project_Info");
                        iteminfo.setPrimaryKeys("id");
                        AuditSpSpSgxk sgxk = iGsProjectService.getSpSgxkByRowguid(auditProject.getSubappguid());
                        if (sgxk != null) {
                            iteminfo.set("Prj_Approval_Depart", sgxk.getStr("lxpfjg"));
                            iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(sgxk.getDate("lxpfsj"),
                                    EpointDateUtil.DATE_FORMAT));
                        }
                        else {
                            AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(itemrowguid).getResult();
                            iteminfo.set("Prj_Approval_Depart", auditProject.getOuname());
                            iteminfo.set("Prj_Approval_Date", EpointDateUtil
                                    .convertDate2String(baseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                        }
                        iGsProjectService.insertQzk(iteminfo);
                    }
                }

                Record danti = iGsProjectService.getSpglDantiInfoBySubappguid(projectguid);
                if (danti != null) {
                    danti.setSql_TableName("TB_Unit_Project_Info");
                    danti.setPrimaryKeys("id");
                    iGsProjectService.insertQzk(danti);
                }

                List<Record> Partics = iGsProjectService.getSpglParticipnListBySubappguid(projectguid);
                if (!Partics.isEmpty()) {
                    for (Record Partic : Partics) {
                        Partic.setSql_TableName("TB_Project_Corp_Info");
                        Partic.setPrimaryKeys("id");
                        iGsProjectService.insertQzk(Partic);
                    }
                }

                Record sgxkz = iGsProjectService.getSpglSgxkInfoBySubappguid(projectguid);
                if (sgxkz != null) {
                    sgxkz.setSql_TableName("TB_Builder_Licence_Manage");
                    sgxkz.setPrimaryKeys("id");
                    CertInfoExtension certinfoExtension = null;
                    // 获取照面信息
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", auditProject.getCertrowguid());
                    certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    String Builder_Licence_Num = "";
                    if (certinfoExtension != null) {
                        Builder_Licence_Num = certinfoExtension.getStr("bh");
                    }
                    else {
                        Builder_Licence_Num = "无";
                    }
                    sgxkz.set("Builder_Licence_Num", Builder_Licence_Num);
                    iGsProjectService.insertQzk(sgxkz);
                }
            }

        }

        // 推送房屋建筑工程和市政基础设施工程竣工验收备案
        if ("房屋建筑工程和市政基础设施工程竣工验收备案".equals(auditTask.getTaskname()) && auditProject != null) {
            String projectguid = auditProject.getRowguid();
            String Prj_Type_Num = "";
            Record iteminfo = iGsProjectService.getSpglSgxkIteminfoBySubappguid(projectguid);
            if (iteminfo != null) {
                String itemrowguid = iteminfo.getStr("ID");
                Prj_Type_Num = iteminfo.getStr("Prj_Type_Num");
                Record item = iGsProjectService.getProjectBasicInfoByRowguid(itemrowguid);
                if (item == null) {
                    iteminfo.setSql_TableName("TB_Project_Info");
                    iteminfo.setPrimaryKeys("id");
                    AuditSpSpSgxk sgxk = iGsProjectService.getSpSgxkByRowguid(auditProject.getSubappguid());
                    if (sgxk != null) {
                        iteminfo.set("Prj_Approval_Depart", sgxk.getStr("lxpfjg"));
                        iteminfo.set("Prj_Approval_Date",
                                EpointDateUtil.convertDate2String(sgxk.getDate("lxpfsj"), EpointDateUtil.DATE_FORMAT));
                    }
                    else {
                        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemrowguid)
                                .getResult();
                        iteminfo.set("Prj_Approval_Depart", auditProject.getOuname());
                        iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(baseinfo.getItemstartdate(),
                                EpointDateUtil.DATE_FORMAT));
                    }
                    iGsProjectService.insertQzk(iteminfo);
                }
            }

            Record jgys = iGsProjectService.getSpglJgYsInfoBySubappguid(projectguid);
            if (jgys != null) {
                jgys.setSql_TableName("TB_Project_Finish_Manage");
                jgys.setPrimaryKeys("id");
                if (StringUtil.isBlank(jgys.getStr("Builder_Licence_Num"))) {
                    jgys.set("Builder_Licence_Num", "无");
                }
                if (!"01".equals(Prj_Type_Num)) {
                    Prj_Type_Num = "02";
                }
                String numberName = "Prj_Type_Num";
                Calendar calendar = Calendar.getInstance();
                String numberFlag = "" + calendar.get(Calendar.YEAR)
                        + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + calendar.get(Calendar.DAY_OF_MONTH);
                int theYearLength = 0;
                int snLength = 2;
                String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
                String Prj_Finish_Num = "JB" + auditProject.getAreacode() + certno + Prj_Type_Num;
                jgys.set("Prj_Finish_Num", Prj_Finish_Num);
                iGsProjectService.insertQzk(jgys);
            }
        }

        // 省里推送办件归集
        if (StringUtil.isNotBlank(xzareacode) && auditProject != null
                && !(auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)
                        && StringUtil.isNotBlank(auditProject.getSparetime()) && auditProject.getSparetime() < 0)) {
            String areacodere = "";
            if (xzareacode.length() == 6) {
                areacodere = xzareacode + "000";
            }
            else {
                areacodere = xzareacode;
            }

            // 先查询前置库有没有重复办件
            eajcstepbasicinfogt baseinfo = iWebUploaderService.getQzkBaseInfo(auditProject.getFlowsn());
            if (baseinfo == null) {
                String Orgbusno = UUID.randomUUID().toString();
                baseinfo = new eajcstepbasicinfogt();
                FrameOu frameOu = ouservice.getOuByOuGuid(auditProject.getOuguid());
                if (frameOu == null) {
                    frameOu = iWebUploaderService.getFrameOuByOuname(auditProject.getOuname());
                }
                baseinfo.set("rowguid", Orgbusno);
                baseinfo.setOrgbusno(Orgbusno);
                baseinfo.setProjid(auditProject.getFlowsn());
                baseinfo.setProjpwd(auditProject.getStr("pwd"));
                baseinfo.setValidity_flag("1");
                baseinfo.setDataver("1");
                baseinfo.setStdver("1");
                baseinfo.setItemno(
                        StringUtil.isBlank(auditTask.getStr("inner_code")) ? "无" : auditTask.getStr("inner_code"));
                List<AuditTaskMaterial> materials = iAuditTaskMaterial
                        .getUsableMaterialListByTaskguid(auditTask.getRowguid()).getResult();
                String materialname = "";
                if (materials != null && !materials.isEmpty()) {
                    for (AuditTaskMaterial material : materials) {
                        materialname += "[纸质]" + material.getMaterialname() + ";";
                    }
                }
                else {
                    materialname = "无";
                }
                baseinfo.set("ACCEPTLIST", materialname);
                String shenpilb = "";
                if ("11".equals(auditTask.getShenpilb())) {
                    shenpilb = "20";
                }
                else if ("10".equals(auditTask.getShenpilb())) {
                    shenpilb = "10";
                }
                else if ("07".equals(auditTask.getShenpilb())) {
                    shenpilb = "07";
                }
                else if ("06".equals(auditTask.getShenpilb())) {
                    shenpilb = "08";
                }
                else if ("05".equals(auditTask.getShenpilb())) {
                    shenpilb = "05";
                }
                else if ("02".equals(auditTask.getShenpilb())) {
                    shenpilb = "02";
                }
                else if ("01".equals(auditTask.getShenpilb())) {
                    shenpilb = "01";
                }
                else if ("03".equals(auditTask.getShenpilb())) {
                    shenpilb = "03";
                }
                else if ("04".equals(auditTask.getShenpilb())) {
                    shenpilb = "04";
                }
                else if ("08".equals(auditTask.getShenpilb())) {
                    shenpilb = "09";
                }
                else if ("09".equals(auditTask.getShenpilb())) {
                    shenpilb = "06";
                }

                baseinfo.set("ITEMTYPE", shenpilb);
                baseinfo.set("CATALOGCODE", auditTask.getStr("CATALOGCODE"));
                baseinfo.set("TASKCODE", auditTask.getStr("TASKCODE"));
                if (10 == auditProject.getApplyertype()) {
                    baseinfo.set("APPLYERTYPE", "2");
                    baseinfo.set("ApplyerPageType", "01");
                }
                else {
                    baseinfo.set("APPLYERTYPE", "1");
                    baseinfo.set("ApplyerPageType", "111");
                }
                // 申请人证件号码
                baseinfo.set("ApplyerPageCode", auditProject.getCertnum());
                baseinfo.setItemname(auditTask.getTaskname());
                baseinfo.setProjectname(auditProject.getProjectname());
                baseinfo.setApplicant(auditProject.getApplyername());
                baseinfo.setApplicantCardCode(auditProject.getCertnum());
                baseinfo.setApplicanttel(auditProject.getContactmobile());
                if (frameOu != null) {
                    baseinfo.setAcceptdeptid(frameOu.getOucode());
                    baseinfo.setAcceptdeptname(frameOu.getOuname());
                }
                else {
                    baseinfo.setAcceptdeptid("无");
                    baseinfo.setAcceptdeptname("无");
                }

                baseinfo.setRegion_id(areacodere);
                baseinfo.setApprovaltype("2");
                baseinfo.setPromisetimelimit("0");
                baseinfo.setPromisetimeunit("1");
                baseinfo.setTimelimit("0");
                baseinfo.setItemregionid(areacodere);
                if ("22".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("111");
                }
                else if ("16".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("01");
                }
                else if ("14".equals(auditProject.getCerttype())) {
                    baseinfo.setApplicantCardtype("02");
                }
                baseinfo.setSubmit("1");
                                    baseinfo.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                baseinfo.set("Status", "0");
                if (StringUtil.isNotBlank(auditProject.getCertnum())) {
                    baseinfo.setAcceptdeptcode(auditProject.getCertnum());
                }
                else {
                    baseinfo.setAcceptdeptcode("无");
                }
                baseinfo.setAcceptdeptcode1("无");
                baseinfo.setAcceptdeptcode2("无");

                try {
                    iWebUploaderService.insertQzkBaseInfo(baseinfo);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // 插入办件流程表（受理步骤）
                eajcstepprocgt process = new eajcstepprocgt();
                process.set("rowguid", UUID.randomUUID().toString());
                process.setOrgbusno(Orgbusno);
                process.setProjid(auditProject.getFlowsn());
                process.setValidity_flag("1");
                process.setDataver("1");
                process.setStdver("1");
                process.setSn("1");
                process.setNodename("受理");
                process.setNodecode("act1");
                process.setNodetype("1");
                process.setNodeprocer(UUID.randomUUID().toString());
                process.setNodeprocername(auditProject.getAcceptusername());
                process.setNodeprocerarea(areacodere);
                process.setRegion_id(areacodere);
                if (frameOu != null) {
                    process.setProcunit(frameOu.getOucode());
                    process.setProcunitname(frameOu.getOuname());
                }
                process.setNodestate("02");
                String accepttime = EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                        EpointDateUtil.DATE_TIME_FORMAT);
                process.setNodestarttime(accepttime);
                process.setNodeendtime(accepttime);
                process.setNoderesult("4");
                                    process.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                    process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                process.setSignstate("0");
                process.setItemregionid(areacodere);
                // FIXME 111
                try {
                    iWebUploaderService.insertQzkProcess(process);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                eajcstepdonegt done = new eajcstepdonegt();
                done.set("rowguid", UUID.randomUUID().toString());
                done.setOrgbusno(Orgbusno);
                done.setProjid(auditProject.getFlowsn());
                done.setValidity_flag("1");
                done.setStdver("1");
                done.setRegion_id(areacodere);
                done.setDataver("1");
                done.setDoneresult("0");
                done.setApprovallimit(new Date());
                done.setCertificatenam("111");
                done.setCertificateno(auditProject.getCertnum());
                done.setIsfee("0");
                done.setOccurtime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                done.setTransactor(userSession.getDisplayName());
                done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                done.setSignstate("0");
                done.setItemregionid(areacodere);
                // FIXME 111
                try {
                    iWebUploaderService.insertQzkDone(done);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                // 插入办件流程表（已办结）
                eajcstepprocgt process1 = new eajcstepprocgt();
                process1.set("rowguid", UUID.randomUUID().toString());
                process1.setOrgbusno(Orgbusno);
                process1.setProjid(auditProject.getFlowsn());
                process1.setValidity_flag("1");
                process1.setDataver("1");
                process1.setStdver("1");
                process1.setSn("2");
                process1.setNodename("办结");
                process1.setNodecode("act2");
                process1.setNodetype("3");
                process1.setNodeprocer(userSession.getUserGuid());
                process1.setNodeprocername(userSession.getDisplayName());
                process1.setNodeprocerarea(areacodere);
                process1.setRegion_id(areacodere);
                if (frameOu != null) {
                    process1.setProcunit(frameOu.getOucode());
                    process1.setProcunitname(frameOu.getOuname());
                }
                process1.setNodestate("02");
                String banjietime = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),
                        EpointDateUtil.DATE_TIME_FORMAT);
                process1.setNodestarttime(banjietime);
                process1.setNodeendtime(banjietime);
                process1.setNoderesult("1");
                process1.setOccurtime(EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
                process1.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                process1.setSignstate("0");
                process1.setItemregionid(areacodere);
                // FIXME 111
                try {
                    iWebUploaderService.insertQzkProcess(process1);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("projectguid", auditProject.getRowguid());
                sql.in("OPERATETYPE", "10,11");
                List<AuditProjectUnusual> projectUnusualList = auditProjectUnusual
                        .getAuditProjectUnusualListByPage(sql.getMap(), 0, 99, "OperateDate", "asc").getResult()
                        .getList();
                int snNum = projectUnusualList.size();
                // 特殊环节信息表
                eajcstepspecialnode specialnode = new eajcstepspecialnode();
                if (snNum > 0) {
                    snNum = snNum / 2;
                    int num = 1;

                    // 原系统业务流水
                    specialnode.setOrgbusno(Orgbusno);
                    // 申办号
                    specialnode.setProjid(auditProject.getFlowsn());
                    // 业务办理行政区划
                    specialnode.setRegion_id(areacodere);
                    // 事项所属区划
                    specialnode.setItemregionid(areacodere);
                    // 标准版本号
                    specialnode.setStdver("1");
                    // 数据版本号
                    specialnode.setDataver("1");
                    if (frameOu != null) {
                        // 处理单位，组织机构代码
                        specialnode.setProcunitid(frameOu.getOucode());
                        // 处理单位名称
                        specialnode.setProcunitname(frameOu.getOuname());
                    }

                    // 环节处理意见
                    specialnode.setNodeprocadv("公示暂停");
                    // 环节处理依据 默认济宁市
                    specialnode.setNodeprocaccord("济宁市");
                    // 材料清单 默认：根据相关法律条文
                    specialnode.setLists("根据相关法律条文");
                    // 环节处理结果 默认:公示
                    specialnode.setNoderesult("3");
                    // 数据存库时间
                    specialnode.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    // 标志位
                    specialnode.setSignState(areacodere);
                    // 环节名称
                    specialnode.setNodename("暂停和恢复");
                    for (int i = 0; i < snNum; i++) {
                        if (StringUtil.isNotBlank(projectUnusualList.get(i + num - 1))) {
                            Date startDate = projectUnusualList.get(i + num - 1).getOperatedate();
                            Date endDate = projectUnusualList.get(i + num - 1).getOperatedate();
                            if (StringUtil.isNotBlank(projectUnusualList.get(i + num))) {
                                endDate = projectUnusualList.get(i + num).getOperatedate();
                            }
                            // 序号
                            specialnode.setSn(String.valueOf(num));
                            specialnode.set("rowguid", UUID.randomUUID().toString());
                            // 处理人id 操作人用户标识
                            specialnode.setProcerid(projectUnusualList.get(i + num - 1).getOperateuserguid());
                            // 处理人姓名 操作人用户名称
                            specialnode.setProcername(projectUnusualList.get(i + num - 1).getOperateusername());
                            // 环节开始时间 暂停开始时间
                            specialnode.setNodestarttime(startDate);
                            // 环节结束时间 暂停结束时间 时间为空可以填开始时间
                            specialnode.setNodeendtime(endDate);
                            // 环节处理地点 挂起原因
                            specialnode.setNodeprocaddr(projectUnusualList.get(i + num - 1).getNote());
                            // 环节发生时间 暂停开始时间
                            specialnode.setNodetime(startDate);
                        }
                        try {
                            iWebUploaderService.insertQzkSpecialnode(specialnode);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        num++;
                    }
                }
            }

        }

        // 大数据局推送排污或施工许可事项
        PwOrSgxkzProject(auditProject, auditTask);

        // 济宁涉水
        if (auditProject != null && ("11370800MB285591843370123018002".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018001".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018013".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018012".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018011".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018006".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018010".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018004".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018014".equals(auditTask.getItem_id())
                || "11370800MB285591843370123018003".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310400201".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310400203".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310400202".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_GCSJYYSWSAQCPWSXKPJ_1");
            logger.info("根据item_id进入方法");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                logger.info("成功获取证照信息: certinfo=" + certinfo.toString());
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("pzwh"));
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", yxqs);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", yxqs);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("CHANPINMINGCHEN", certinfoExtension.getStr("cpmc"));
                ylgg.set("CHANPINLEIBIE", certinfoExtension.getStr("cplb"));
                ylgg.set("CHANPINGUIGEHUOXINGHAO", certinfoExtension.getStr("cpgghxh"));
                ylgg.set("CHANPINJISHUXINXI", certinfoExtension.getStr("cpjsxx"));
                ylgg.set("SHENQINGDANWEI", certinfoExtension.getStr("sqdw"));
                ylgg.set("SHENQINGDANWEIDIZHI", certinfoExtension.getStr("sqdwdz"));
                ylgg.set("SHIJISHENGCHANQIYE", certinfoExtension.getStr("sjscqy"));
                ylgg.set("SHENPIJIELUN", certinfoExtension.getStr("spjl"));
                ylgg.set("PIZHUNWENHAO", certinfoExtension.getStr("pzwh"));
                ylgg.set("PIZHUNRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("pzrq"), "yyyy-MM-dd"));
                ylgg.set("PIJIANYOUXIAOQI", certinfoExtension.getStr("yxqz"));
                ylgg.set("BEIZHU", certinfoExtension.getStr("bz"));
                ylgg.set("SHIJISHENGCHANQIYEDIZHI", certinfoExtension.getStr("sjscqy"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = null;
                // 黄色部分数据库判断
                ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_GCSJYYSWSAQCPWSXKPJ_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    logger.info("更新前置库 :" + ylgg.toString());
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    logger.info("插入前置库 :" + ylgg.toString());
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 调用方法2（因为本方法已到允许的最大空间）
        pushbigdata1();

        // 济宁建设工程消防设计审查证照
        if (auditProject != null && ("1137080000431212413370117043000".equals(auditTask.getItem_id())
                || "1137080123456789074370117043000".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_TSJSGCXFSJSCYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String sqrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", sqrq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("SHENQINGRIQI", sqrq);
                ylgg.set("SHENQINGGONGCHENGMINGCHEN", certinfoExtension.getStr("xmmc"));
                ylgg.set("SHENQINGGONGCHENGDIZHI", certinfoExtension.getStr("xmdz"));
                ylgg.set("JIANZHUMIANJI", certinfoExtension.getStr("jzmj"));
                ylgg.set("JIANZHUGAODU", certinfoExtension.getStr("jzgd"));
                ylgg.set("JIANZHUCENGSHU", certinfoExtension.getStr("jzcs"));
                ylgg.set("BAOGAOBIANHAO", certinfoExtension.getStr("sgcscbh"));
                if ("0".equals(certinfoExtension.getStr("hg"))) {
                    ylgg.set("SHENCHAJIELUN", "不合格");
                }
                else {
                    ylgg.set("SHENCHAJIELUN", "合格");
                }

                ylgg.set("QIANSHOURIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("qsrq"), "yyyy-MM-dd"));
                ylgg.set("PINGZHENGWENHAO", certinfoExtension.getStr("sqwh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_TSJSGCXFSJSCYJS_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁施工许可证
        if (auditProject != null && ("11370800004234373R400011710900302".equals(auditTask.getItem_id())
                || "11370830F5034613XX400011710900302".equals(auditTask.getItem_id()))) {
            sgxkzts();
        }

        // 兖州农药经营许可
        if (auditProject != null && "11370812MB2868524U4370120006006".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_NYJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String jzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("jzrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", jzrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JINGYINGZHEMINGCHEN", certinfoExtension.getStr("jyzmc"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyshxydm"));
                ylgg.set("FADINGDAIBIAORENFUZEREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUSUO", certinfoExtension.getStr("zs"));
                ylgg.set("YINGYECHANGSUO", certinfoExtension.getStr("yycs"));
                ylgg.set("CANGCHUCHANGSUO", certinfoExtension.getStr("cccs"));
                ylgg.set("JINGYINGFANWEI", certinfoExtension.getStr("jyfw"));
                ylgg.set("FENZHIJIGOU", certinfoExtension.getStr("fzjg"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_NYJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁建设工程竣工联合验收意见书
        if (auditProject != null && ("11370800MB285591847371017058000".equals(auditTask.getItem_id())
                || "11370832MB2855272B7371017058000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4371017058000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4371017058000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4371017058000".equals(auditTask.getItem_id())
                || "11370828MB286029024371017058000".equals(auditTask.getItem_id())
                || "11370883MB2857374H4371017058000".equals(auditTask.getItem_id())
                || "1137080123456789074371017058000".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCJGLHYSYJS_1");
            String certrowguid = auditProject.get("certrowguid");
            if (StringUtil.isNotBlank(certrowguid) && certrowguid.contains(";")) {
                String[] certrowguids = certrowguid.split(";");
                Date date = new Date();
                for (String rowguid : certrowguids) {
                    CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(rowguid);
                    if (certinfo != null) {
                        if ("建设工程竣工联合验收意见书(济宁）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(汶上）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(鱼台）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书（梁山）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(微山）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(金乡）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(邹城）".equals(certinfo.getCertname())
                                || "建设工程竣工联合验收意见书(高新）".equals(certinfo.getCertname())) {
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", certinfo.getRowguid());
                            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                    false);
                            if (certinfoExtension == null) {
                                certinfoExtension = new CertInfoExtension();
                            }
                            String n = certinfoExtension.getStr("tzsnh");
                            String h = certinfoExtension.getStr("tzsbh");
                            // 证照编号
                            if ("11370830F5034613XX4371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "汶联验" + h + "号");
                            }
                            else if ("11370827MB2857833K4371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "鱼联验字" + h + "号");
                            }
                            else if ("11370832MB2855272B7371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "梁联验字" + h + "号");
                            }
                            else if ("11370826MB2858051T4371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "微联验字" + h + "号");
                            }
                            else if ("11370828MB286029024371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "金联验字" + h + "号");
                            }
                            else if ("11370883MB2857374H4371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "邹联验字" + h + "号");
                            }
                            else if ("1137080123456789074371017058000".equals(auditTask.getItem_id())) {
                                ylgg.set("LICENSE_NUMBER", n + "高联验字" + h + "号");
                            }
                            else {
                                ylgg.set("LICENSE_NUMBER", n + "济联验字" + h + "号");
                            }

                            String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"),
                                    "yyyy-MM-dd");
                            String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"),
                                    "yyyy-MM-dd");
                            String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"),
                                    "yyyy-MM-dd");

                            // 发证日期x
                            ylgg.set("CERTIFICATE_DATE", rq);
                            // 有效期开始
                            ylgg.set("VALID_PERIOD_BEGIN", rq);
                            // 有效期截止
                            ylgg.set("VALID_PERIOD_END", "9999-12-30");

                            ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                            ylgg.set("PERMIT_CONTENT", "--");
                            ylgg.set("CERTIFICATE_LEVEL", "A");

                            ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                            ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("dwmc"));
                            ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc"));
                            ylgg.set("TONGZHISHURIQI", EpointDateUtil
                                    .convertDate2String(certinfoExtension.getDate("tzsrq"), "yyyy-MM-dd"));

                            String areacode = ZwfwUserSession.getInstance().getAreaCode();
                            if (areacode.length() == 6) {
                                areacode = areacode + "000000";
                            }
                            else if (areacode.length() == 9) {
                                areacode = areacode + "000";
                            }

                            ylgg.set("operatedate", date);

                            int APPLYERTYPE = auditProject.getApplyertype();
                            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                                ylgg.set("HOLDER_TYPE",
                                        codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                                if ("20".equals(APPLYERTYPE + "")) {
                                    ylgg.set("HOLDER_TYPE", "自然人");
                                }
                            }
                            else {
                                ylgg.set("HOLDER_TYPE", "--");
                            }
                            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                            ylgg.set("CERTIFICATE_TYPE", codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                    auditProject.getCerttype()));
                            // 联系方式
                            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                            // 证照颁发单位
                            ylgg.set("DEPT_NAME", userSession.getOuName());

                            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                            if (frameOuExten != null) {
                                // 证照办法单位组织机构代码
                                ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                            }
                            // 行政区划名称
                            ylgg.set("DISTRICTS_NAME",
                                    areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                                            .getResult().getXiaquname());
                            // 行政区划编码
                            ylgg.set("DISTRICTS_CODE", areacode);

                            ylgg.set("rowguid", UUID.randomUUID().toString());
                            ylgg.setPrimaryKeys("rowguid");

                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCJGLHYSYJS_1",
                                    ylgg.getStr("LICENSE_NUMBER"));

                            if (ylggrecord != null) {
                                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                                ylgg.set("state", "update");
                                iJnProjectService.UpdateRecord(ylgg);
                            }
                            else {
                                ylgg.set("state", "insert");
                                iJnProjectService.inserRecord(ylgg);
                            }

                        }
                    }
                }
            }
            else {
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    String n = certinfoExtension.getStr("tzsnh");
                    String h = certinfoExtension.getStr("tzsbh");

                    // 证照编号
                    if ("11370830F5034613XX4371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "汶联验" + h + "号");
                    }
                    else if ("11370827MB2857833K4371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "鱼联验字" + h + "号");
                    }
                    else if ("11370826MB2858051T4371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "微联验字" + h + "号");
                    }
                    else if ("11370828MB286029024371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "金联验字" + h + "号");
                    }
                    else if ("11370883MB2857374H4371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "邹联验字" + h + "号");
                    }
                    else if ("1137080123456789074371017058000".equals(auditTask.getItem_id())) {
                        ylgg.set("LICENSE_NUMBER", n + "高联验字" + h + "号");
                    }
                    else {
                        ylgg.set("LICENSE_NUMBER", n + "济联验字" + h + "号");
                    }
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"), "yyyy-MM-dd");

                    // 发证日期x
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", "9999-12-30");

                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("PERMIT_CONTENT", "--");
                    ylgg.set("CERTIFICATE_LEVEL", "A");

                    ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                    ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("dwmc"));
                    ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc"));
                    ylgg.set("TONGZHISHURIQI",
                            EpointDateUtil.convertDate2String(certinfoExtension.getDate("tzsrq"), "yyyy-MM-dd"));

                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }

                    ylgg.set("operatedate", new Date());

                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", userSession.getOuName());

                    FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                    if (frameOuExten != null) {
                        // 证照办法单位组织机构代码
                        ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    }
                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");

                    Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCJGLHYSYJS_1",
                            ylgg.getStr("LICENSE_NUMBER"));
                    if (ylggrecord != null) {
                        ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                        ylgg.set("state", "update");
                        iJnProjectService.UpdateRecord(ylgg);
                    }
                    else {
                        ylgg.set("state", "insert");
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }

        }

        // 济宁房屋建筑工程和市政基础设施工程竣工验收备案表
        if (auditProject != null && ("11370800MB285591847371017058000".equals(auditTask.getItem_id())
                || "11370832MB2855272B7371017058000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4371017058000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4371017058000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4371017058000".equals(auditTask.getItem_id())
                || "1137080123456789074371017058000".equals(auditTask.getItem_id())
                || "11370883MB2857374H4371017058000".equals(auditTask.getItem_id())
                || "11370828MB286029024371017058000".equals(auditTask.getItem_id())
                || "11370800MB28559184337301700100101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_FWJZGCHSZJCSSGCJGYSBAB_1");
            String certrowguid = auditProject.get("certrowguid");
            if (StringUtil.isNotBlank(certrowguid) && certrowguid.contains(";")) {
                String[] certrowguids = certrowguid.split(";");
                Date date = new Date();
                for (String rowguid : certrowguids) {
                    CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(rowguid);
                    if (certinfo != null) {
                        if ("房屋建筑工程和市政基础设施工程竣工验收备案表（济宁）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（汶上）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（鱼台）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（梁山）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（微山）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（高新）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（邹城）".equals(certinfo.getCertname())
                                || "房屋建筑工程和市政基础设施工程竣工验收备案表（金乡）".equals(certinfo.getCertname())) {
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", certinfo.getRowguid());
                            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                    false);
                            if (certinfoExtension == null) {
                                certinfoExtension = new CertInfoExtension();
                            }
                            // 证照编号
                            ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zzbh"));
                            String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("clsj"),
                                    "yyyy-MM-dd");
                            // String yxqs =
                            // EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"),
                            // "yyyy-MM-dd");
                            // String yxqz =
                            // EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"),
                            // "yyyy-MM-dd");

                            // 发证日期x
                            ylgg.set("CERTIFICATE_DATE", rq);
                            // 有效期开始
                            ylgg.set("VALID_PERIOD_BEGIN", rq);
                            // 有效期截止
                            ylgg.set("VALID_PERIOD_END", "9999-12-30");

                            ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                            ylgg.set("PERMIT_CONTENT", "--");
                            ylgg.set("CERTIFICATE_LEVEL", "A");

                            ylgg.set("YIMAGUANDIBIANHAO", certinfoExtension.getStr("ymgdbh"));
                            ylgg.set("JIANSHEDANWEIMINGCHEN", certinfoExtension.getStr("jsdwmc"));
                            ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                            ylgg.set("GONGCHENGDIZHI", certinfoExtension.getStr("gcdz"));
                            ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                            ylgg.set("ZONGJIANZHUMIANJI", certinfoExtension.getStr("zjzmj"));
                            ylgg.set("GONGCHENGZAOJIA", certinfoExtension.getStr("gczj"));
                            ylgg.set("KAIGONGRIQI",
                                    EpointDateUtil.convertDate2String(certinfoExtension.getDate("kgrq"), "yyyy-MM-dd"));
                            ylgg.set("JUNGONGYANSHOURIQI", EpointDateUtil
                                    .convertDate2String(certinfoExtension.getDate("jgysrq"), "yyyy-MM-dd"));
                            ylgg.set("GUIHUAXUKEZHENGBIANHAO", certinfoExtension.getStr("ghxkzbh"));
                            ylgg.set("SHIGONGXUKEZHENGBIANHAO", certinfoExtension.getStr("sgxkzbh"));
                            ylgg.set("GUIHUAHESHIZHENGBIANHAO", certinfoExtension.getStr("ghhszbh"));
                            ylgg.set("XIAOFANGYANSHOUHUOBEIANBIANHAO", certinfoExtension.getStr("xfyshbabh"));
                            ylgg.set("GCZLJDJGMC_30", certinfoExtension.getStr("gczljdjgmc"));
                            ylgg.set("SHOUQISHIJIAN",
                                    EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqsj"), "yyyy-MM-dd"));

                            String areacode = ZwfwUserSession.getInstance().getAreaCode();
                            if (areacode.length() == 6) {
                                areacode = areacode + "000000";
                            }
                            else if (areacode.length() == 9) {
                                areacode = areacode + "000";
                            }

                            ylgg.set("operatedate", date);

                            int APPLYERTYPE = auditProject.getApplyertype();
                            if (StringUtil.isNotBlank(APPLYERTYPE)) {
                                ylgg.set("HOLDER_TYPE",
                                        codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                                if ("20".equals(APPLYERTYPE + "")) {
                                    ylgg.set("HOLDER_TYPE", "自然人");
                                }
                            }
                            else {
                                ylgg.set("HOLDER_TYPE", "--");
                            }
                            ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                            ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                            ylgg.set("CERTIFICATE_TYPE", codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                    auditProject.getCerttype()));
                            // 联系方式
                            ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                            // 证照颁发单位
                            ylgg.set("DEPT_NAME", userSession.getOuName());

                            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                            if (frameOuExten != null) {
                                // 证照办法单位组织机构代码
                                ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                            }
                            // 行政区划名称
                            ylgg.set("DISTRICTS_NAME",
                                    areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                                            .getResult().getXiaquname());
                            // 行政区划编码
                            ylgg.set("DISTRICTS_CODE", areacode);

                            ylgg.set("rowguid", UUID.randomUUID().toString());
                            ylgg.setPrimaryKeys("rowguid");

                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_FWJZGCHSZJCSSGCJGYSBAB_1",
                                    ylgg.getStr("LICENSE_NUMBER"));
                            if (ylggrecord != null) {
                                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                                ylgg.set("state", "update");
                                iJnProjectService.UpdateRecord(ylgg);
                            }
                            else {
                                ylgg.set("state", "insert");
                                iJnProjectService.inserRecord(ylgg);
                            }
                        }
                    }

                }

            }
            else {
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zzbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("clsj"), "yyyy-MM-dd");
                    // String yxqs =
                    // EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"),
                    // "yyyy-MM-dd");
                    // String yxqz =
                    // EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"),
                    // "yyyy-MM-dd");

                    // 发证日期x
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", "9999-12-30");

                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("PERMIT_CONTENT", "--");
                    ylgg.set("CERTIFICATE_LEVEL", "A");

                    ylgg.set("YIMAGUANDIBIANHAO", certinfoExtension.getStr("ymgdbh"));
                    ylgg.set("JIANSHEDANWEIMINGCHEN", certinfoExtension.getStr("jsdwmc"));
                    ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                    ylgg.set("GONGCHENGDIZHI", certinfoExtension.getStr("gcdz"));
                    ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                    ylgg.set("ZONGJIANZHUMIANJI", certinfoExtension.getStr("zjzmj"));
                    ylgg.set("GONGCHENGZAOJIA", certinfoExtension.getStr("gczj"));
                    ylgg.set("KAIGONGRIQI",
                            EpointDateUtil.convertDate2String(certinfoExtension.getDate("kgrq"), "yyyy-MM-dd"));
                    ylgg.set("JUNGONGYANSHOURIQI",
                            EpointDateUtil.convertDate2String(certinfoExtension.getDate("jgysrq"), "yyyy-MM-dd"));
                    ylgg.set("GUIHUAXUKEZHENGBIANHAO", certinfoExtension.getStr("ghxkzbh"));
                    ylgg.set("SHIGONGXUKEZHENGBIANHAO", certinfoExtension.getStr("sgxkzbh"));
                    ylgg.set("GUIHUAHESHIZHENGBIANHAO", certinfoExtension.getStr("ghhszbh"));
                    ylgg.set("XIAOFANGYANSHOUHUOBEIANBIANHAO", certinfoExtension.getStr("xfyshbabh"));
                    ylgg.set("GCZLJDJGMC_30", certinfoExtension.getStr("gczljdjgmc"));
                    ylgg.set("SHOUQISHIJIAN",
                            EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqsj"), "yyyy-MM-dd"));

                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }

                    ylgg.set("operatedate", new Date());

                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", userSession.getOuName());

                    FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                    if (frameOuExten != null) {
                        // 证照办法单位组织机构代码
                        ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    }
                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");

                    Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_FWJZGCHSZJCSSGCJGYSBAB_1",
                            ylgg.getStr("LICENSE_NUMBER"));
                    if (ylggrecord != null) {
                        ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                        ylgg.set("state", "update");
                        iJnProjectService.UpdateRecord(ylgg);
                    }
                    else {
                        ylgg.set("state", "insert");
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }
        }

        // 农药经营许可证延续
        String itemids = "";
        itemids = ConfigUtil.getConfigValue("xmzArgs", "nyjyxkz_itemids");
        if (StringUtils.isNotBlank(itemids) && itemids.contains(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("ex_nyjyxkz_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String jzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("jzrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", jzrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JINGYINGZHEMINGCHEN", certinfoExtension.getStr("jyzmc"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyshxydm"));
                ylgg.set("FADINGDAIBIAORENFUZEREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUSUO", certinfoExtension.getStr("zs"));
                ylgg.set("YINGYECHANGSUO", certinfoExtension.getStr("yycs"));
                ylgg.set("CANGCHUCHANGSUO", certinfoExtension.getStr("cccs"));
                ylgg.set("JINGYINGFANWEI", certinfoExtension.getStr("jyfw"));
                ylgg.set("FENZHIJIGOU", certinfoExtension.getStr("fzjg"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_NYJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁港口经营许可
        if (auditProject != null && ("11370800MB285591843370118037000".equals(auditTask.getItem_id())
                || "11370800MB28559184300011823200201".equals(auditTask.getItem_id())
                || "11370800MB28559184300011823200202".equals(auditTask.getItem_id())
                || "11370800MB28559184300011823200203".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_ZHRMGHGGKJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("GONGSIMINGCHEN", certinfoExtension.getStr("gsmc"));
                ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("BANGONGDIZHI", certinfoExtension.getStr("bgdz"));
                ylgg.set("JINGYINGDIYU", certinfoExtension.getStr("jyqy"));
                ylgg.set("FUYE", certinfoExtension.getStr("fy"));
                ylgg.set("CONGSHIYEWU", certinfoExtension.getStr("ywlx"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_ZHRMGHGGKJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁人防工程竣工验收备案表
        if (auditProject != null && ("11370800MB285591847371043007000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4371043007000".equals(auditTask.getItem_id())
                || "11370832MB2855272B4371043007000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4371043007000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4371043007000".equals(auditTask.getItem_id())
                || "11370883MB2857374H437308000200101".equals(auditTask.getItem_id())
                || "11370828MB28602902437308000200101".equals(auditTask.getItem_id())
                || "11370800MB28559184337308000200101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_RFGCJGYSBAB_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                // String yxqs =
                // EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"),
                // "yyyy-MM-dd");
                // String yxqz =
                // EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"),
                // "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-30");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANSHEDANWEIMINGCHEN", certinfoExtension.getStr("jsdwxx"));
                ylgg.set("JIANSHEDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("jsdwfzr"));
                ylgg.set("KAIGONGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("kgrq"), "yyyy-MM-dd"));
                ylgg.set("JUNGONGYANSHOURIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("jgysrq"), "yyyy-MM-dd"));

                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                ylgg.set("GONGCHENGDIDIAN", certinfoExtension.getStr("gcdd"));
                ylgg.set("RENFANGMIANJI", certinfoExtension.getStr("rfmj"));
                ylgg.set("SHIYONGMIANJI", certinfoExtension.getStr("symj"));
                ylgg.set("GONGCHENGLEIBIE", certinfoExtension.getStr("gclb"));
                ylgg.set("JIEGOULEIXING", certinfoExtension.getStr("jglx"));
                ylgg.set("PINGSHIYONGTU", certinfoExtension.getStr("psyt"));
                ylgg.set("ZHANSHIYONGTU", certinfoExtension.getStr("zsyt"));
                ylgg.set("SHIGONGTUSHENCHAWENHAO", certinfoExtension.getStr("sgtscwh"));
                ylgg.set("RENFANGPIZHUNWENHAO", certinfoExtension.getStr("rfpzwh"));
                ylgg.set("FANGHUDENGJI", certinfoExtension.getStr("fhdj"));
                ylgg.set("LIANTONGKOU", certinfoExtension.getStr("ltk"));
                ylgg.set("SHIWAICHURUKOU", certinfoExtension.getStr("swrk"));
                ylgg.set("SHINACHURUKOU", certinfoExtension.getStr("snrk"));
                ylgg.set("FSDJFHSBAZQK_21", certinfoExtension.getStr("fsdazqk"));
                ylgg.set("KANCHADANWEIMINGCHEN", certinfoExtension.getStr("kcdwmc"));
                ylgg.set("KANCHADANWEIXIANGMUFUZEREN", certinfoExtension.getStr("kcdwxmfzr"));
                ylgg.set("SHEJIDANWEIMINGCHEN", certinfoExtension.getStr("sjdwmc"));
                ylgg.set("SHEJIDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("sjdwxmfzr"));
                ylgg.set("SHIGONGDANWEIMINGCHEN", certinfoExtension.getStr("sgdwmc"));
                ylgg.set("SHIGONGDANWEIFUZEREN", certinfoExtension.getStr("sgdwfzr"));
                ylgg.set("JIANLIDANWEIMINGCHEN", certinfoExtension.getStr("jldwmc"));
                ylgg.set("JIANLIDANWEIFUZEREN", certinfoExtension.getStr("jldwfzr"));
                ylgg.set("TUZHISHENCHADANWEI", certinfoExtension.getStr("tzscdw"));
                ylgg.set("TUZHISHENCHAXIANGMUFUZEREN", certinfoExtension.getStr("tzscxmfzr"));
                ylgg.set("FHSBSCAZDW_32", certinfoExtension.getStr("fhsbazdw"));
                ylgg.set("FHSBSCAZXMFZR_33", certinfoExtension.getStr("fhsbazfzr"));
                ylgg.set("BEIANYIJIAN", certinfoExtension.getStr("bayj"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_RFGCJGYSBAB_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁建设工程竣工档案验收意见书
        if (auditProject != null && ("1137080000431212413371017014000".equals(auditTask.getItem_id())
                || "1137083000433521104371017014000".equals(auditTask.getItem_id())
                || "11370832MB2799274T4371017014000".equals(auditTask.getItem_id())
                || "1137082600452807284371017014000".equals(auditTask.getItem_id())
                || "11370883004326307F437101703000101".equals(auditTask.getItem_id())
                || "11370828004332694N437101703000101".equals(auditTask.getItem_id())
                || "113708000043121241337101703000101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCJGDAYSYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                if ("1137080000431212413371017014000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "济档验第" + certinfoExtension.getStr("bh") + "号");
                }
                else if ("1137083000433521104371017014000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "汶档验第" + certinfoExtension.getStr("bh") + "号");
                }
                else if ("1137082600452807284371017014000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "微档验第" + certinfoExtension.getStr("bh") + "号");
                }
                else if ("11370832MB2799274T4371017014000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "梁档验第" + certinfoExtension.getStr("bh") + "号");
                }
                else if ("11370883004326307F437101703000101".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "邹档验第" + certinfoExtension.getStr("bh") + "号");
                }
                else if ("11370828004332694N437101703000101".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "金档验第" + certinfoExtension.getStr("bh") + "号");
                }

                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("kgrq"), "yyyy-MM-dd");
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("jgrq"), "yyyy-MM-dd");
                // String yxqz =
                // EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"),
                // "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqs);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                ylgg.set("GONGCHENGDIZHI", certinfoExtension.getStr("gcdz"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("KANCHADANWEI", certinfoExtension.getStr("kcdw"));
                ylgg.set("SHEJIDANWEI", certinfoExtension.getStr("sjdw"));
                ylgg.set("JIANLIDANWEI", certinfoExtension.getStr("jldw"));
                ylgg.set("SHIGONGDANWEI", certinfoExtension.getStr("sgdw"));
                ylgg.set("KAIGONGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("kgrq"), "yyyy-MM-dd"));
                ylgg.set("JUNGONGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("jgrq"), "yyyy-MM-dd"));
                ylgg.set("ZHENGGAIYIJIAN", certinfoExtension.getStr("wjcljzgyj"));
                ylgg.set("CERTIFICATE_DATE",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"), "yyyy-MM-dd"));
                ylgg.set("BAOSONGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCJGDAYSYJS_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁建设工程消防验收备案凭证
        if (auditProject != null && ("11370800MB285591843370718000001".equals(auditTask.getItem_id())
                || "11370800MB285591843370718000002".equals(auditTask.getItem_id())
                || "11370832MB2799274T7371017920000".equals(auditTask.getItem_id())
                || "1137083000433521104371017920000".equals(auditTask.getItem_id())
                || "1137082600452807287371017920000".equals(auditTask.getItem_id())
                || "1137080123456789074371017050000".equals(auditTask.getItem_id())
                || "11370883004326307F437301702800101".equals(auditTask.getItem_id())
                || "11370828004332694N437301702800101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCXFYSBAPZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }

                // 证照编号
                if ("1137083000433521104371017920000".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "汶建消备凭字〔" + n + "〕第" + h + "号");
                }
                else if ("1137082600452807287371017920000".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "微建消备凭字〔" + n + "〕第" + h + "号");
                }
                else if ("11370832MB2799274T7371017920000".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "梁建消验备字〔" + n + "〕第" + h + "号");
                }
                else if ("1137080123456789074371017050000".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "高建消验备字〔" + n + "〕第" + h + "号");
                }
                else if ("11370883004326307F437301702800101".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "邹建消验备字〔" + n + "〕第" + h + "号");
                }
                else if ("11370828004332694N437301702800101".equals(auditTask.getItem_id())) {
                    String n = certinfoExtension.getStr("nian");
                    String h = certinfoExtension.getStr("hao");
                    ylgg.set("LICENSE_NUMBER", "金建消验备字〔" + n + "〕第" + h + "号");
                }
                else {
                    String n = certinfoExtension.getStr("n");
                    String h = certinfoExtension.getStr("h");
                    ylgg.set("LICENSE_NUMBER", "济建特消验字〔" + n + "〕第" + h + "号");
                }

                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-30");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("SHENQINGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqsj"), "yyyy-MM-dd"));
                ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                ylgg.set("DIZHI", certinfoExtension.getStr("dz"));
                ylgg.set("BEIANSHENQINGBIANHAO", certinfoExtension.getStr("basqbh"));
                ylgg.set("CAILIAOYI", certinfoExtension.getStr("cly"));
                ylgg.set("CAILIAOER", certinfoExtension.getStr("cle"));
                ylgg.set("CAILIAOSAN", certinfoExtension.getStr("cls"));
                ylgg.set("XUANZEYI", certinfoExtension.getStr("xzy"));
                ylgg.set("XUANZEER", certinfoExtension.getStr("xze"));
                ylgg.set("CERTIFICATE_DATE",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("gzrq"), "yyyy-MM-dd"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCXFYSBAPZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁特殊建设工程消防验收意见书
        if (auditProject != null && ("1137080000431212417370117044000".equals(auditTask.getItem_id())
                || "1137083000433521104370117044000".equals(auditTask.getItem_id())
                || "1137082600452807284370117044000".equals(auditTask.getItem_id())
                || "11370832MB2799274T4370117044000".equals(auditTask.getItem_id())
                || "1137080123456789074370117044000".equals(auditTask.getItem_id())
                || "11370883004326307F3370117044000".equals(auditTask.getItem_id())
                || "11370828004332694N4370117044000".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_TSJSGCXFYSYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                String n = certinfoExtension.getStr("n");
                String h = certinfoExtension.getStr("h");

                if ("1137083000433521104370117044000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "汶建特消验字〔" + n + "〕第" + h + "号");
                }
                else if ("1137082600452807284370117044000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "微建特消验字〔" + n + "〕第" + h + "号");
                }
                else if ("1137080123456789074370117044000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "高建特消验字〔" + n + "〕第" + h + "号");
                }
                else if ("11370883004326307F3370117044000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "邹建特消验字〔" + n + "〕第" + h + "号");
                }
                else if ("11370828004332694N4370117044000".equals(auditTask.getItem_id())) {
                    ylgg.set("LICENSE_NUMBER", "金建特消验字〔" + n + "〕第" + h + "号");
                }
                else {
                    ylgg.set("LICENSE_NUMBER", "济建特消验字〔" + n + "〕第" + h + "号");
                }

                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-30");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("SHENQINGRIQI",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqrq"), "yyyy-MM-dd"));
                ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                ylgg.set("SHOULINIAN", certinfoExtension.getStr("sln"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ftjfj"));
                ylgg.set("PINGZHENGWENHAO", certinfoExtension.getStr("pzwh"));
                ylgg.set("HEGE", certinfoExtension.getStr("hg"));
                ylgg.set("BUHEGE", certinfoExtension.getStr("bhg"));
                ylgg.set("JIELUN", certinfoExtension.getStr("jl"));
                ylgg.set("GAIZHANGRIQI", certinfoExtension.getStr("gzrq"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_TSJSGCXFYSYJS_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }
        // 济宁建设工程竣工规划核实合格证
        if (auditProject != null && ("11370800MB285591847370715007000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370715007000".equals(auditTask.getItem_id())
                || "11370826MB286081384370715007000".equals(auditTask.getItem_id())
                || "11370832004233039B4370715007000".equals(auditTask.getItem_id())
                || "11370800MB285591843370715007000".equals(auditTask.getItem_id())
                || "113708012345678915437071500900101".equals(auditTask.getItem_id())
                || "11370883MB2857374H437071500900101".equals(auditTask.getItem_id())
                || "11370828MB28631335437071500900101".equals(auditTask.getItem_id())
                || "11370800MB28559184337071500900101".equals(auditTask.getItem_id())
                || "11370800MB2855934R337071500900101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCJGGHHSHGZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-30");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                if (StringUtil.isBlank(certinfoExtension.getStr("bdcdyh"))) {
                    ylgg.set("BUDONGCHANDANYUANHAO", "--");
                }
                else {
                    ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));
                }

                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEXIANGMUMINGCHEN", certinfoExtension.getStr("jsxmmc"));
                ylgg.set("JIANSHEWEIZHI", certinfoExtension.getStr("jswz"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));

                if (StringUtil.isBlank(certinfoExtension.getStr("ftjfj"))) {
                    ylgg.set("FUTUJIFUJIANMINGCHEN", "--");
                }
                else {
                    ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ftjfj"));
                }

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JSGCJGGHHSHGZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁农药经营许可证
        if (auditProject != null && ("11370828MB286029027370120006006".equals(auditTask.getItem_id())
                || "1137082856772322GG58e2781001002".equals(auditTask.getItem_id())
                || "11370828004328249X54734e7028010".equals(auditTask.getItem_id())
                || "1137082800433260017b050e8028111".equals(auditTask.getItem_id())
                || "11370828004332635K53ee8a4027012".equals(auditTask.getItem_id())
                || "11370828004332571M516fcc9012563".equals(auditTask.getItem_id())
                || "11370828004319019Y50116f3027123".equals(auditTask.getItem_id())
                || "11370828004332328T52eafa5027001".equals(auditTask.getItem_id())
                || "11370828683238017W5f17d74123011".equals(auditTask.getItem_id())
                || "1137082800433241695107052011012".equals(auditTask.getItem_id())
                || "11370828004314621554e89ae027001".equals(auditTask.getItem_id())
                || "11370828004429381Q7eabab7080001".equals(auditTask.getItem_id())
                || "11370828004332539A5905c28001001".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_NYJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqe"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", yxqs);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JINGYINGZHEMINGCHEN", certinfoExtension.getStr("jyzmc"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyshxydm"));
                ylgg.set("FADINGDAIBIAORENFUZEREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUSUO", certinfoExtension.getStr("zs"));
                ylgg.set("YINGYECHANGSUO", certinfoExtension.getStr("yycs"));
                ylgg.set("CANGCHUCHANGSUO", certinfoExtension.getStr("cccs"));
                ylgg.set("JINGYINGFANWEI", certinfoExtension.getStr("jyfw"));
                ylgg.set("FENZHIJIGOU", certinfoExtension.getStr("fzjg"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_NYJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁燃气经营许可证
        if (auditProject != null && ("11370800MB28559184737011702600001".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702600003".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702600002".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_RQJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", yxqs);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("CHIZHENGSHUOMING", "");
                ylgg.set("QIYEMINGCHENG", certinfoExtension.getStr("qymc"));
                ylgg.set("FADINGDAIBIAORENXINGMING", certinfoExtension.getStr("frdbhzsqfzr"));
                ylgg.set("NIANJIANDENGJIBIAO", "");
                ylgg.set("JINGYINGLEIBIE", certinfoExtension.getStr("jyrqlx"));
                ylgg.set("DENGJIZHUCEDIZHI", certinfoExtension.getStr("dz"));
                ylgg.set("BIANGENGJILUBIAO", "");
                ylgg.set("JINGYINGQUYU", certinfoExtension.getStr("jyrqqy"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_RQJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁供热经营许可证
        if (auditProject != null && ("11370800MB28559184737011702800001".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702800003".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702800002".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700800001".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700800002".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700800003".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700800004".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_GRJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxrq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANDUJIANCHAJILU", certinfoExtension.getStr("jdjcjl"));
                ylgg.set("GONGREQIYEMINGCHENG", certinfoExtension.getStr("qymc"));
                ylgg.set("QIYEZHUYAORENYUANDENGJI", certinfoExtension.getStr("qyzyrydj"));
                ylgg.set("BIANGENGJILU", "");
                ylgg.set("DANWEIFUZEREN", certinfoExtension.getStr("dwfzr"));
                ylgg.set("YINXINGZHANGHAO", certinfoExtension.getStr("yhzh"));
                ylgg.set("CHIZHENGXUZHI", "");
                ylgg.set("DANWEIDIZHI", certinfoExtension.getStr("dwdz"));
                ylgg.set("KAIHUYINXING", certinfoExtension.getStr("khyh"));
                ylgg.set("YOUZHENGBIANMA", certinfoExtension.getStr("yzbm"));
                ylgg.set("DANWEIDIANHUA", certinfoExtension.getStr("dwdh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_GRJYXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }

            }
        }

        // 济宁人防工程施工图设计文件核准
        if (auditProject != null && ("11370800MB285591847370117046000".equals(auditTask.getItem_id())
                || "11370832MB2855272B4370117046000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4370117046000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4370117046000".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711700201".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711700202".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_SPFYXSXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("YXSFWJGZH", certinfoExtension.getStr("yxsjgzh"));
                ylgg.set("YUXIAOSHOUFANGWUJIBENXINXI", certinfoExtension.getStr("yxsfwjbxx"));
                ylgg.set("YXSFWJGYH", certinfoExtension.getStr("yxsfwjgyh"));
                ylgg.set("XIAOQUZUTUANMINGCHEN", certinfoExtension.getStr("xqmc"));
                ylgg.set("YXSFWXMDZ", certinfoExtension.getStr("yxsfwdz"));
                ylgg.set("XIAOSHOUFANGWULEIBIE", certinfoExtension.getStr("xsfwlb"));
                ylgg.set("FANGWUYUXIAOSHOUDANWEI", certinfoExtension.getStr("fwxsdw"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁人防工程施工图设计文件核准
        if (auditProject != null && "11370800MB285591847371043008000".equals(auditTask.getItem_id())
                || "11370830F5034613XX437108000900101".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_RFGCSGTSJWJHZYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));

                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("GONGCHENGMINGCHEN", certinfoExtension.getStr("gcmc"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEWEIZHI", certinfoExtension.getStr("gcdz"));
                ylgg.set("RENFANGGONGCHENGSHEJIDANWEI", certinfoExtension.getStr("sjdw"));
                ylgg.set("RENFANGSHIGONGTUSHENCHADANWEI", certinfoExtension.getStr("scdw"));
                ylgg.set("ZONGJIANZHUMIANJI", certinfoExtension.getStr("zjzmj"));
                ylgg.set("DISHANGJIANZHUMIANJI", certinfoExtension.getStr("dsjzmj"));
                ylgg.set("DIXIAJIANZHUMIANJI", certinfoExtension.getStr("dxjzmj"));
                ylgg.set("RENFANGGONGCHENGJIANZHUMIANJI", certinfoExtension.getStr("rfgcjzmj"));
                ylgg.set("FANGHULEIBIE", certinfoExtension.getStr("fhlb"));
                ylgg.set("JIEJIANSHENPIYIJIANBIANHAO", certinfoExtension.getStr("jjfkd"));
                ylgg.set("FANGHUDANYUAN", certinfoExtension.getStr("fhdya"));
                ylgg.set("JIANZHUMIANJI", certinfoExtension.getStr("mja"));
                ylgg.set("KANGLIJIBIE", certinfoExtension.getStr("kldja"));
                ylgg.set("FANGHUAJIBIE", certinfoExtension.getStr("fhjba"));
                ylgg.set("ZHANSHIGONGNEN", certinfoExtension.getStr("zsa"));
                ylgg.set("PINGSHIYONGTU", certinfoExtension.getStr("psyta"));

                ylgg.set("FANGHUDANYUAN1", certinfoExtension.getStr("fhdyb"));
                ylgg.set("JIANZHUMIANJI1", certinfoExtension.getStr("mjb"));
                ylgg.set("KANGLIJIBIE1", certinfoExtension.getStr("kldjb"));
                ylgg.set("FANGHUAJIBIE1", certinfoExtension.getStr("fhjbb"));
                ylgg.set("ZHANSHIGONGNEN1", certinfoExtension.getStr("zsb"));
                ylgg.set("PINGSHIYONGTU1", certinfoExtension.getStr("psytb"));

                ylgg.set("FANGHUDANYUAN2", certinfoExtension.getStr("fhdyc"));
                ylgg.set("JIANZHUMIANJI2", certinfoExtension.getStr("mjc"));
                ylgg.set("KANGLIJIBIE2", certinfoExtension.getStr("kldjc"));
                ylgg.set("FANGHUAJIBIE2", certinfoExtension.getStr("fhjbc"));
                ylgg.set("ZHANSHIGONGNEN2", certinfoExtension.getStr("zsc"));
                ylgg.set("PINGSHIYONGTU2", certinfoExtension.getStr("psytc"));

                ylgg.set("FANGHUDANYUAN3", certinfoExtension.getStr("fhdyd"));
                ylgg.set("JIANZHUMIANJI3", certinfoExtension.getStr("mjd"));
                ylgg.set("KANGLIJIBIE3", certinfoExtension.getStr("kldjd"));
                ylgg.set("FANGHUAJIBIE3", certinfoExtension.getStr("fhjbd"));
                ylgg.set("ZHANSHIGONGNEN3", certinfoExtension.getStr("zsd"));
                ylgg.set("PINGSHIYONGTU3", certinfoExtension.getStr("psytd"));

                ylgg.set("BEIZHU", "--");

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁防空地下室易地建设审批
        if (auditProject != null && "11370800MB285591847370143004000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370143004000".equals(auditTask.getItem_id())
                || "11370800MB28559184300018010200501".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_FKDXSYDJSSPYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("YIDIJIANSHEFEIJIAONABIAOZHUN", certinfoExtension.getStr("ydjsfjnbz"));
                ylgg.set("ZHANSHIYONGTU", certinfoExtension.getStr("zsyt"));
                ylgg.set("JIANSHEXIANGMU", certinfoExtension.getStr("jsxm"));
                ylgg.set("FANGHULEIBIE", certinfoExtension.getStr("fhlb"));
                ylgg.set("DIXIAJIANZHUMIANJI", certinfoExtension.getStr("dxjzmj"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEDIDIAN", certinfoExtension.getStr("jsdd"));
                ylgg.set("KANGLIDENGJI", certinfoExtension.getStr("kldj"));
                ylgg.set("YINGJIANFANGKONGDIXIASHI", certinfoExtension.getStr("yjfkdxs"));
                ylgg.set("DISHANGJIANZHUMIANJI", certinfoExtension.getStr("dsjzmj"));
                ylgg.set("YIDIJIANSHEFEIJIAONASHUE", certinfoExtension.getStr("ydjsfjnse"));
                ylgg.set("BEIZHU", "--");

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁建设用地规划许可证
        if (auditProject != null && ("11370800MB285591843370115055000".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505500002".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505500001".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505500003".equals(auditTask.getItem_id())
                || "1137080123456789154370115055000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370115055000".equals(auditTask.getItem_id())
                || "11370800MB2855934R300011513100201".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513100301".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513100303".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513100302".equals(auditTask.getItem_id())
                || "11370830004335078G400011513100301".equals(auditTask.getItem_id())
                || "11370830004335078G400011513100303".equals(auditTask.getItem_id())
                || "11370830004335078G400011513100302".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSYDGHXKZX_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("YONGDIDANWEI", certinfoExtension.getStr("yddw"));
                ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("ydxmmc"));
                ylgg.set("PIZHUNYONGDIJIGUAN", certinfoExtension.getStr("pzydjg"));
                ylgg.set("PIZHUNYONGDIWENHAO", certinfoExtension.getStr("pzydwh"));
                ylgg.set("YONGDIWEIZHI", certinfoExtension.getStr("ydwz"));
                ylgg.set("YONGDIMIANJI", certinfoExtension.getStr("ydmj"));
                ylgg.set("TUDIYONGTU", certinfoExtension.getStr("ydxz"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                ylgg.set("TUDIQUDEFANGSHI", certinfoExtension.getStr("tdqdfs"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ft"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁建设项目用地预审与选址意见书
        if (auditProject != null && ("11370800MB285591847370115001000".equals(auditTask.getItem_id())
                || "11370800MB2855934R300011511600301".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSXMYDYSYXZYJS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");

                Calendar calendar = new GregorianCalendar();
                calendar.setTime(certinfoExtension.getDate("rq"));
                calendar.add(Calendar.YEAR, 3); // 把日期往后增加一年，整数往后推，负数往前移
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                Date enddate = calendar.getTime();

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", EpointDateUtil.convertDate2String(enddate, "yyyy-MM-dd"));

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("jsxmmc"));
                ylgg.set("XIANGMUDAIMA", certinfoExtension.getStr("jsxmd"));
                ylgg.set("JIANSHEDANWEIMINGCHEN", certinfoExtension.getStr("jsdwmc"));
                ylgg.set("XIANGMUJIANSHEYIJU", certinfoExtension.getStr("jsxmyj"));
                ylgg.set("XIANGMUNIXUANWEIZHI", certinfoExtension.getStr("jsxmnxwz"));
                ylgg.set("NIYONGDIMIANJI", certinfoExtension.getStr("nydmj"));
                ylgg.set("NIJIANSHEGUIMO", certinfoExtension.getStr("njsgm"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ft"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁建筑节能技术产品初次认定
        if (auditProject != null && ("1137080000431212413370717666897".equals(auditTask.getItem_id())
                || "113708000043121241337071700600101".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_SDSJZJNJSYCPYYRDZS_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("newfzrq"), "yyyy-MM-dd");
                String enddate = EpointDateUtil.convertDate2String(certinfoExtension.getDate("newyxqx"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", enddate);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("sqdw"));
                ylgg.set("YOUXIAOQIXIAN", enddate);
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("qyjgdm"));
                ylgg.set("QIYEZHUCEDIZHI", certinfoExtension.getStr("qyzcdz"));
                ylgg.set("QIYESHENGCHANDIZHI", certinfoExtension.getStr("qyscdz"));
                ylgg.set("CHANPINZHIXINGBIAOZHUN", certinfoExtension.getStr("zxbz"));
                ylgg.set("JIANCEJIGOUMINGCHEN", certinfoExtension.getStr("jcjgmc"));
                ylgg.set("XINGJIANBAOGAOBIANHAO", certinfoExtension.getStr("bgbh"));
                ylgg.set("ZHUYAOXINGNENZHIBIAO", certinfoExtension.getStr("zyxnzb"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁危险化学品经营许可证
        if (auditProject != null && ("11370800004313485J737012501200002".equals(auditTask.getItem_id())
                || "11370800004313485J737012501200001".equals(auditTask.getItem_id())
                || "11370800004313485J737012501200003".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_WXHXPJYXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                String enddate = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", enddate);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
                ylgg.set("QIYEZHUSUO", certinfoExtension.getStr("qydz"));
                ylgg.set("XUKEFANWEI", certinfoExtension.getStr("xkfw"));
                ylgg.set("QIYEFADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("JINGYINGFANGSHI", certinfoExtension.getStr("jyfs"));
                ylgg.set("YOUXIAOQIYANXUSHIJIAN", "");

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("XINGZHENGQUHUADAIMA", areacode);

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁工程勘察
        if (auditProject != null && ("11370800MB285591842370117200237".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200229".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200230".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200231".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200232".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200233".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200234".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200235".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200236".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200239".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200238".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200661".equals(auditTask.getItem_id())
                || "11370800MB285591847370117200226".equals(auditTask.getItem_id())
                || "11370800MB285591843370117200224".equals(auditTask.getItem_id())

        )) {
            Record ylgg = new Record();

            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                if ("工程勘察设计资质证".equals(certinfo.getCertname())) {
                    ylgg.setSql_TableName("EX_GCKCZZZS_1");
                }
                else {
                    ylgg.setSql_TableName("EX_GCSJZZZS_1");
                }
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                String enddate = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", enddate);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", certinfoExtension.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", certinfoExtension.getStr("zzlbjdj"));
                ylgg.set("BEIZHU", certinfoExtension.getStr("remark"));
                ylgg.set("JINGJIXINGZHI", certinfoExtension.getStr("jjxz"));
                ylgg.set("YOUXIAOQI", enddate);
                ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("frdb"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("XINGZHENGQUHUADAIMA", areacode);

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁消毒
        if (auditProject != null && ("11370800MB285591843370123020001".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000102".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800001".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800002".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800003".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_XDCPSCWSXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                String enddate = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", enddate);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHENSHUJU", certinfoExtension.getStr("dwmc"));
                ylgg.set("FADINGDAIBIAORENSHUJU", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUCEDIZHISHUJU", certinfoExtension.getStr("zcdz"));
                ylgg.set("SHENGCHANDIZHISHUJU", certinfoExtension.getStr("scdz"));
                ylgg.set("SHENGCHANFANGSHISHUJU", certinfoExtension.getStr("scfs"));
                ylgg.set("SHENGCHANXIANGMUSHUJU", certinfoExtension.getStr("scxm"));
                ylgg.set("BIANGENGRIQISHUJU", "");

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("XINGZHENGQUHUADAIMA", areacode);

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁消毒变更
        if (auditProject != null && ("11370800MB285591843370123020004".equals(auditTask.getItem_id())
                || "11370800MB285591843370123020003".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000101".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000403".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000402".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000103".equals(auditTask.getItem_id())
                || "11370800MB285591843370123020001".equals(auditTask.getItem_id())
                || "11370800MB28559184337012302000401".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800003".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800002".equals(auditTask.getItem_id())
                || "11370800MB28559184300012310800001".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_XDCPSCWSXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                String enddate = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", enddate);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHENSHUJU", certinfoExtension.getStr("dwmc"));
                ylgg.set("FADINGDAIBIAORENSHUJU", certinfoExtension.getStr("fddbr"));
                ylgg.set("ZHUCEDIZHISHUJU", certinfoExtension.getStr("zcdz"));
                ylgg.set("SHENGCHANDIZHISHUJU", certinfoExtension.getStr("scdz"));
                ylgg.set("SHENGCHANFANGSHISHUJU", certinfoExtension.getStr("scfs"));
                ylgg.set("SHENGCHANXIANGMUSHUJU", certinfoExtension.getStr("scxm"));
                ylgg.set("BIANGENGRIQISHUJU", "");

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("XINGZHENGQUHUADAIMA", areacode);

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_XDCPSCWSXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 鱼台鱼台商品房预售许可
        // if (auditProject != null &&
        // "11370827MB2857833K4370117046000".equals(auditTask.getItem_id())) {
        // Record ylgg = new Record();
        // ylgg.setSql_TableName("EX_SPFYXSXKZ_1");
        // CertInfo certinfo =
        // iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
        // if (certinfo != null) {
        // Map<String, Object> filter = new HashMap<>();
        // // 设置基本信息guid
        // filter.put("certinfoguid", certinfo.getRowguid());
        // CertInfoExtension certinfoExtension =
        // getMongodbUtil().find(CertInfoExtension.class, filter, false);
        // if (certinfoExtension == null) {
        // certinfoExtension = new CertInfoExtension();
        // }
        // // 证照编号
        // ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
        // String rq = EpointDateUtil.convertDate2String(new Date(),
        // "yyyy-MM-dd");
        // String yearz = certinfoExtension.getStr("ZN");
        // String monthz = certinfoExtension.getStr("ZY");
        // String dayz = certinfoExtension.getStr("ZR");
        //
        // // 发证日期x
        // ylgg.set("CERTIFICATE_DATE", rq);
        // // 有效期开始
        // ylgg.set("VALID_PERIOD_BEGIN", rq);
        // // 有效期截止
        // ylgg.set("VALID_PERIOD_END", yearz + "-" + monthz + "-" + dayz);
        //
        // ylgg.set("PROJECT_NAME", auditTask.getTaskname());
        // ylgg.set("PERMIT_CONTENT", "--");
        // ylgg.set("CERTIFICATE_LEVEL", "A");
        //
        // ylgg.set("YXSFWJGZH", certinfoExtension.getStr("yxsjgzh"));
        // ylgg.set("YUXIAOSHOUFANGWUJIBENXINXI",
        // certinfoExtension.getStr("yxsfwjbxx"));
        // ylgg.set("YXSFWJGYH", certinfoExtension.getStr("yxsfwjgyh"));
        // ylgg.set("XIAOQUZUTUANMINGCHEN", certinfoExtension.getStr("xqmc"));
        // ylgg.set("YXSFWXMDZ", certinfoExtension.getStr("yxsfwdz"));
        // ylgg.set("XIAOSHOUFANGWULEIBIE", certinfoExtension.getStr("xsfwlb"));
        // ylgg.set("FANGWUYUXIAOSHOUDANWEI",
        // certinfoExtension.getStr("fwxsdw"));
        //
        // String areacode = ZwfwUserSession.getInstance().getAreaCode();
        // if (areacode.length() == 6) {
        // areacode = areacode + "000000";
        // } else if (areacode.length() == 9) {
        // areacode = areacode + "000";
        // }
        //
        // ylgg.set("operatedate", new Date());
        //
        // int APPLYERTYPE = auditProject.getApplyertype();
        // if (StringUtil.isNotBlank(APPLYERTYPE)) {
        // ylgg.set("HOLDER_TYPE",
        // codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
        // if ("20".equals(APPLYERTYPE + "")) {
        // ylgg.set("HOLDER_TYPE", "自然人");
        // }
        // } else {
        // ylgg.set("HOLDER_TYPE", "--");
        // }
        // ylgg.set("HOLDER_NAME", auditProject.getApplyername());
        // ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
        // ylgg.set("CERTIFICATE_TYPE",
        // codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
        // auditProject.getCerttype()));
        // // 联系方式
        // ylgg.set("CONTACT_PHONE", auditProject.getContactphone());
        //
        // // 证照颁发单位
        // ylgg.set("DEPT_NAME", userSession.getOuName());
        //
        // FrameOuExtendInfo frameOuExten =
        // ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
        // if (frameOuExten != null) {
        // // 证照办法单位组织机构代码
        // ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
        // }
        // // 行政区划名称
        // ylgg.set("DISTRICTS_NAME",
        // areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
        // .getResult().getXiaquname());
        // // 行政区划编码
        // ylgg.set("DISTRICTS_CODE", areacode);
        //
        // ylgg.set("rowguid", UUID.randomUUID().toString());
        // ylgg.setPrimaryKeys("rowguid");
        //
        // ylgg.set("state", "insert");
        // iJnProjectService.inserRecord(ylgg);
        // }
        // }

        // 鱼台建设用地规划许可证
        if (auditProject != null && ("11370827MB2857833K4370115055000".equals(auditTask.getItem_id())
                || "11370800MB2855934R300011513100201".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSYDGHXKZX_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("riqi"), "yyyy-MM-dd");

                Date startdate = certinfoExtension.getDate("riqi");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startdate);
                calendar.add(Calendar.YEAR, 1); // 把日期往后增加一年，整数往后推，负数往前移
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                Date enddate = calendar.getTime();
                String endtime = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", endtime);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("YONGDIDANWEI", certinfoExtension.getStr("yddw"));
                ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc"));
                ylgg.set("PIZHUNYONGDIJIGUAN", certinfoExtension.getStr("pzydjg"));
                ylgg.set("PIZHUNYONGDIWENHAO", certinfoExtension.getStr("pzydwh"));
                ylgg.set("YONGDIWEIZHI", certinfoExtension.getStr("ydwz"));
                ylgg.set("YONGDIMIANJI", certinfoExtension.getStr("ydmj"));
                ylgg.set("TUDIYONGTU", certinfoExtension.getStr("tdyt"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                ylgg.set("TUDIQUDEFANGSHI", certinfoExtension.getStr("tdqdfs"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ftjfjmc"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 济宁建设工程规划许可证
        if (auditProject != null && ("11370800MB285591843370115056000".equals(auditTask.getItem_id())
                || "11370800MB2855934R300011513300301".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505600002".equals(auditTask.getItem_id())
                || "11370832MB2855272B4370115056000".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505600005".equals(auditTask.getItem_id())
                || "11370800MB28559184337011505600001".equals(auditTask.getItem_id())
                || "11370826MB2858051T437011505600001".equals(auditTask.getItem_id())
                || "11370826MB2858051T437011505600005".equals(auditTask.getItem_id())
                || "1137080123456789154370115056000".equals(auditTask.getItem_id())
                || "1137080123456789154370115226000".equals(auditTask.getItem_id())
                || "1137080123456789154370115057000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370115056000".equals(auditTask.getItem_id())
                || "11370830004335078G400011513300501".equals(auditTask.getItem_id())
                || "11370830004335078G400011513300502".equals(auditTask.getItem_id())
                || "11370830004335078G400011513300503".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370115057000".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513300501".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513300502".equals(auditTask.getItem_id())
                || "11370800MB23415709400011513300503".equals(auditTask.getItem_id())
                || "11370800MB2855934R300011513300301".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCGHXKZX_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");

                Date startdate = certinfoExtension.getDate("rq");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startdate);
                calendar.add(Calendar.YEAR, 2); // 把日期往后增加一年，整数往后推，负数往前移
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                Date enddate = calendar.getTime();
                String endtime = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", endtime);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEXIANGMUMINGCHEN", certinfoExtension.getStr("jsxmmc"));
                ylgg.set("JIANSHEWEIZHI", certinfoExtension.getStr("jswz"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ft"));
                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 鱼台建设工程规划许可证
        if (auditProject != null && "11370827MB2857833K4370115056000".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCGHXKZX_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("riqi"), "yyyy-MM-dd");

                Date startdate = certinfoExtension.getDate("riqi");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startdate);
                calendar.add(Calendar.YEAR, 1); // 把日期往后增加一年，整数往后推，负数往前移
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                Date enddate = calendar.getTime();
                String endtime = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", endtime);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEXIANGMUMINGCHEN", certinfoExtension.getStr("jsxmmc"));
                ylgg.set("JIANSHEWEIZHI", certinfoExtension.getStr("jswz"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ftjfj"));
                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 鱼台建设工程竣工规划核实
        if (auditProject != null && "11370827MB2857833K4370715007000".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JSGCJGGHHSHGZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                Date startdate = certinfoExtension.getDate("rq");
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(startdate);
                calendar.add(Calendar.YEAR, 1); // 把日期往后增加一年，整数往后推，负数往前移
                calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                Date enddate = calendar.getTime();
                String endtime = EpointDateUtil.convertDate2String(enddate, EpointDateUtil.DATE_FORMAT);

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", endtime);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jsdw"));
                ylgg.set("JIANSHEXIANGMUMINGCHEN", certinfoExtension.getStr("jsxmmc"));
                ylgg.set("JIANSHEWEIZHI", certinfoExtension.getStr("jswz"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jsgm"));
                ylgg.set("FUTUJIFUJIANMINGCHEN", certinfoExtension.getStr("ftjfj"));
                ylgg.set("BUDONGCHANDANYUANHAO", certinfoExtension.getStr("bdcdyh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        if (auditProject != null && "1838000000000000004371004005003".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("ex_qytzxmba_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", "--");
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                // 发证日期
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("dwmc"));
                ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("FARENZHENGZHAOHAOMA", certinfoExtension.getStr("frzzhm"));
                ylgg.set("XIANGMUDAIMA", certinfoExtension.getStr("xmdm"));
                ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc"));
                ylgg.set("JIANSHEDIDIAN", certinfoExtension.getStr("jsdd"));
                ylgg.set("JIANSHEGUIMOHENARONG", certinfoExtension.getStr("jsgmhnr"));
                ylgg.set("ZONGTOUZI", certinfoExtension.getStr("ztz"));
                ylgg.set("JIANSHEQIZHINIANXIAN", certinfoExtension.getStr("jsqznx"));
                ylgg.set("XIANGMUFUZEREN", certinfoExtension.getStr("xmfzr"));
                ylgg.set("LIANXIDIANHUA", certinfoExtension.getStr("lxdh"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 城市建筑垃圾处置核准
        if (auditProject != null && ("11370800MB28559184737011703800002".equals(auditTask.getItem_id())
                || "11370800MB28559184737011703800001".equals(auditTask.getItem_id())
                || "11370826MB2858051T4370117038000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4370117038000".equals(auditTask.getItem_id())
                || "11370828MB286029024370117038000".equals(auditTask.getItem_id())
                || "11370829771010370B4370117038000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370117038000".equals(auditTask.getItem_id())
                || "11370831MB285634767370117038000".equals(auditTask.getItem_id())
                || "11370832MB2855272B2370117038000".equals(auditTask.getItem_id())
                || "11370881MB2796110U4370117038000".equals(auditTask.getItem_id())
                || "11370812MB2868524U4370117038000".equals(auditTask.getItem_id())
                || "11370883MB2857374H4370117038000".equals(auditTask.getItem_id())
                || "1237080079619134XQ4370117038000".equals(auditTask.getItem_id())
                || "12370800069995325T4370117038000".equals(auditTask.getItem_id())
                || "TE3708732020309011437011703800001".equals(auditTask.getItem_id())
                || "TE3708732020309011437011703800002".equals(auditTask.getItem_id())
                || "11370811MB279691104370117038000".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("ex_csjzljczhz_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zzbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("czrqks"), "yyyy-MM-dd");
                String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("czrqjs"), "yyyy-MM-dd");
                // 发证日期
                ylgg.set("CERTIFICATE_DATE",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd"));
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxq);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("DANWEIMINGCHEN", certinfoExtension.getStr("dwmc"));
                ylgg.set("SHENQINGSHIJIAN",
                        EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqsj"), "yyyy-MM-dd"));
                ylgg.set("YUNSHULUXIAN", certinfoExtension.getStr("yslx"));
                ylgg.set("BANFADANWEI", userSession.getOuName());

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }

        // 如果为医疗事项，推送数据至前置库 11370800MB285591843370123017001
        if (auditProject != null && ("11370800MB285591843370123029001".equals(auditTask.getItem_id())
                || "11370800MB28559184300012313400001".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getYlzmDetailByRowguid(auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_ylggsczm_1");
                ylgg.set("yljgdymc", record.getStr("yljgdymc"));
                ylgg.set("zydjh", record.getStr("zydjh"));
                ylgg.set("fddbr", record.getStr("fddbr"));
                ylgg.set("yljgdz", record.getStr("yljgdz"));
                ylgg.set("qtlbwbk", record.getStr("dxwbk2fbwb"));
                ylgg.set("certguid", auditProject.get("certrowguid"));
                String yljglx = record.getStr("yljglx");
                if (StringUtil.isNotBlank(yljglx)) {
                    ylgg.set("yljglx", codeItemsService.getItemTextByCodeName("医疗广告_机构类别", yljglx));
                }
                else {
                    ylgg.set("yljglx", "");
                }
                ylgg.set("zlkm", record.getStr("zlkm"));
                ylgg.set("cws", record.getStr("cws"));
                String syzxs = record.getStr("syzxs");
                if (StringUtil.isNotBlank(syzxs)) {
                    ylgg.set("syzxs", codeItemsService.getItemTextByCodeName("医疗广告_所有制形式", syzxs));
                }
                else {
                    ylgg.set("syzxs", "");
                }
                ylgg.set("jzsj", record.getStr("jzsj"));
                String ggfblxresult = "";
                String ggfblx = record.getStr("ggfblx");
                if (ggfblx.contains(",")) {
                    String[] ggfblxs = ggfblx.split(",");
                    for (String datail : ggfblxs) {
                        ggfblxresult += codeItemsService.getItemTextByCodeName("医疗广告_媒体类别", datail) + ";";
                    }
                }
                else if (!ggfblx.contains(",") && StringUtil.isNotBlank(ggfblx)) {
                    ggfblxresult = codeItemsService.getItemTextByCodeName("医疗广告_媒体类别", ggfblx);
                }

                ylgg.set("ggfblx", ggfblxresult);

                ylgg.set("fzjg", record.getStr("fzxzbm"));
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("zmwh", certinfoExtension.get("zmwh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                    ylgg.set("rq", rq);

                    String yxqx = record.getStr("yxqx");
                    Date startdate = certinfoExtension.getDate("rq");

                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(startdate);
                    int year = 0;
                    if ("1".equals(yxqx)) {
                        year = 1;
                    }
                    else if ("2".equals(yxqx)) {
                        year = 2;
                    }
                    calendar.add(Calendar.YEAR, year); // 把日期往后增加一年，整数往后推，负数往前移
                    calendar.add(Calendar.DAY_OF_MONTH, -1); // 把日期向前调整一天
                    Date enddate = calendar.getTime();

                    ylgg.set("starttime", rq);
                    ylgg.set("endtime", EpointDateUtil.convertDate2String(enddate, "yyyy-MM-dd"));

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("cezlx", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("cezlx", "自然人");
                    }
                }
                else {
                    ylgg.set("cezlx", "");
                }
                ylgg.set("czrmc", auditProject.getApplyername());
                ylgg.set("czrzjhm", auditProject.getCertnum());
                ylgg.set("czrzjlx",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));

                // 证照颁发单位
                ylgg.set("zzbfdw", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("zzbfdwzzjgdm", frameOuExten.getStr("orgcode"));
                }

                ylgg.set("zzsjqhmc", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 数据状态
                ylgg.set("state", 1);
                ylgg.set("sjrksj", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getYlggsczmByZmwh(ylgg.getStr("zmwh"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", 2);
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    iJnProjectService.inserRecord(ylgg);
                }

            }
        }

        // 省安全生产许可证
        if (auditProject != null && "11370800MB285591843370117002017".equals(auditTask.getItem_id())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Record ylgg = new Record();
                    ylgg.setSql_TableName("EX_JZSGQYAQSCXKZX_1");
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
                    String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                    // 发证日期x
                    ylgg.set("CERTIFICATE_DATE", fzrq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", yxqs);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxqz);

                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("PERMIT_CONTENT", "建筑施工");
                    ylgg.set("CERTIFICATE_LEVEL", "A");

                    ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyxydm"));
                    ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
                    ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("frdb"));
                    ylgg.set("DANWEIDIZHI", certinfoExtension.getStr("xxdz"));
                    ylgg.set("JINGJILEIXING", certinfoExtension.getStr("jjxz"));
                    ylgg.set("XUKEFANWEI", "建筑施工");
                    ylgg.set("XINGZHENGQUHUADAIMA", auditProject.getAreacode());
                    ylgg.set("FDDBRSFZHM_13", auditProject.getLegalid());
                    ylgg.set("FDDBRSFZHMLX_14", "111");
                    ylgg.set("ERWEIMA", certinfo.getStr("erweima"));
                    ylgg.set("ZHENGSHUZHUANGTAI", "注销");
                    ylgg.set("ZHENGSHUZHUANGTAIDAIMA", "05");
                    ylgg.set("ZHENGSHUZHUANGTAIMIAOSHU", "注销");
                    ylgg.set("GUANLIANLEIXING", "关联类型");
                    ylgg.set("GUANLIANZHENGZHAOBIAOSHI",
                            StringUtils.isNotBlank(certinfoExtension.getStr("glzzbs"))
                                    ? certinfoExtension.getStr("glzzbs")
                                    : certinfo.getStr("associatedZzeCertID"));
                    ylgg.set("YEWULEIXINGDAIMA", "业务代码");
                    ylgg.set("YEWUXINXI", "业务类型");
                    ylgg.set("BFJGTYSHXYDM_23", "113700000045030270");
                    ylgg.set("SHOUCIFAZHENGRIQI", yxqs);

                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }

                    ylgg.set("operatedate", new Date());

                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", "山东省住房和城乡建设厅");

                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", "113700000045030270");

                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");

                    Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JZSGQYAQSCXKZX_1",
                            ylgg.getStr("LICENSE_NUMBER"));

                    if (ylggrecord != null) {
                        ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                        ylgg.set("state", "update");
                        iJnProjectService.UpdateRecord(ylgg);
                    }
                    else {
                        ylgg.set("state", "insert");
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }
        }
        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591843370117002015".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002024".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002027".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002026".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002025".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220429150623", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591847370117003005".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003006".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003007".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003004".equals(auditTask.getItem_id())
                || "11370800MB285591843370117003003".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711100002".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003002".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711100003".equals(auditTask.getItem_id())
                || "11370800MB285591842370117008000".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220408161905", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        // 建筑业企业资质许可（增项）
        if (auditProject != null && ("11370800MB285591843370117100121".equals(auditTask.getItem_id())
                || "建筑业企业资质许可（增项）".equals(auditTask.getTaskname()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220509094032", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                        ;

                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591843370117100123".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220622111527", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                        ;

                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591843370117100124".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100125".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100127".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100128".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100130".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100131".equals(auditTask.getItem_id())
                || "11370800MB285591843370117001006".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                        ;
                    }
                }
            }
        }

        // 建筑业企业资质（首次申请）、延续
        if (auditProject != null && ("建筑业企业资质（首次申请）".equals(auditTask.getTaskname())
                || "建筑业企业资质许可（延续）".equals(auditTask.getTaskname()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20230619094541", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                    }
                }
            }
        }

        // 建筑业企业资质证升级
        if (auditProject != null && ("11370800MB285591843370117100133".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220622114015", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591843370117100126".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220622112659", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                    }
                }
            }
        }

        // 变更 注销事项 对应证照注销
        if (auditProject != null && ("11370800MB285591843370117100136".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100137".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100138".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100139".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100140".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100141".equals(auditTask.getItem_id()))) {
            String certrowguid = auditProject.getCertrowguid();
            Record record = iCxBusService.getDzbdDetail("formtable20220705115059", auditProject.getRowguid());
            if (record != null) {
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("zsbh", record.getStr("zsbh"));
                List<CertInfoExtension> certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter,
                        null, false);
                for (CertInfoExtension cert : certinfoExtensions) {
                    if (!certrowguid.equals(cert.getStr("certinfoguid"))) {
                        iJNAuditProject.updateCrtInfo(cert.getStr("certinfoguid"));
                        ;

                    }
                }
            }
        }

        if (auditProject != null && ("11370800MB285591843370117002014".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002013".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002025".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002026".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002027".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002024".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002015".equals(auditTask.getItem_id())

        )) {
            Record record = iCxBusService.getDzbdDetail("formtable20220429150623", auditProject.getRowguid());
            if (record != null) {
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Record ylgg = new Record();
                    ylgg.setSql_TableName("EX_JZSGQYAQSCXKZX_1");
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxqs = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqs"), "yyyy-MM-dd");
                    String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                    // 发证日期x
                    ylgg.set("CERTIFICATE_DATE", fzrq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", yxqs);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxqz);

                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("PERMIT_CONTENT", "建筑施工");
                    ylgg.set("CERTIFICATE_LEVEL", "A");

                    ylgg.set("TONGYISHEHUIXINYONGDAIMA", certinfoExtension.getStr("tyxydm"));
                    ylgg.set("QIYEMINGCHEN", certinfoExtension.getStr("qymc"));
                    ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("frdb"));
                    ylgg.set("DANWEIDIZHI", certinfoExtension.getStr("xxdz"));
                    ylgg.set("JINGJILEIXING", certinfoExtension.getStr("jjxz"));
                    ylgg.set("XUKEFANWEI", "建筑施工");
                    ylgg.set("XINGZHENGQUHUADAIMA", auditProject.getAreacode());
                    ylgg.set("FDDBRSFZHM_13", auditProject.getLegalid());
                    ylgg.set("FDDBRSFZHMLX_14", "111");
                    ylgg.set("ERWEIMA", certinfo.getStr("erweima"));
                    ylgg.set("ZHENGSHUZHUANGTAI", "有效");
                    ylgg.set("ZHENGSHUZHUANGTAIDAIMA", "01");
                    ylgg.set("ZHENGSHUZHUANGTAIMIAOSHU", "有效");
                    ylgg.set("GUANLIANLEIXING", "关联类型");
                    ylgg.set("GUANLIANZHENGZHAOBIAOSHI",
                            StringUtils.isNotBlank(certinfoExtension.getStr("glzzbs"))
                                    ? certinfoExtension.getStr("glzzbs")
                                    : certinfo.getStr("associatedZzeCertID"));
                    ylgg.set("YEWULEIXINGDAIMA", "业务代码");
                    ylgg.set("YEWUXINXI", "业务类型");
                    ylgg.set("BFJGTYSHXYDM_23", "113700000045030270");
                    ylgg.set("SHOUCIFAZHENGRIQI",
                            EpointDateUtil.convertDate2String(certinfo.getAwarddate(), EpointDateUtil.DATE_FORMAT));

                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }

                    ylgg.set("operatedate", new Date());

                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", "山东省住房和城乡建设厅");

                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", "113700000045030270");
                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");

                    Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JZSGQYAQSCXKZX_1",
                            ylgg.getStr("LICENSE_NUMBER"));

                    if (ylggrecord != null) {
                        ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                        ylgg.set("state", "update");
                        iJnProjectService.UpdateRecord(ylgg);
                    }
                    else {
                        ylgg.set("state", "insert");
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }
        }

        // 办结推送安许证 -- 新申请和增补
        if (auditProject != null && ("11370800MB28559184300011711200001".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002016".equals(auditTask.getItem_id())
                || "11370800MB285591843370117002011".equals(auditTask.getItem_id()))) {
            JnGxhSendUtils utils = new JnGxhSendUtils();
            utils.SendCertInfoAxz1(auditProject, auditTask);
        }

        // 办结推送安许证
        // 建筑施工企业安全生产许可（变更）11370800MB28559184300011711200004
        // 建筑施工企业安全生产许可[延期(需要重新审查） 11370800MB28559184300011711200002
        // 建筑施工企业安全生产许可[延期（不需要重新审查）] 11370800MB28559184300011711200003
        if (auditProject != null && ("11370800MB28559184300011711200004".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711200002".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711200003".equals(auditTask.getItem_id()))) {
            JnGxhSendUtils utils = new JnGxhSendUtils();
            utils.SendCertInfoAxz2(auditProject, auditTask);
        }

        // 省施工图审查机构认定证书
        if (auditProject != null && ("11370800MB28559184337011700300001".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300003".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300004".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300005".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300002".equals(auditTask.getItem_id())
                || "11370800MB28559184337011700300006".equals(auditTask.getItem_id()))) {
            JnGxhSendUtils utils = new JnGxhSendUtils();
            utils.SendCertInfoToSgt(userSession, auditProject, auditTask);
        }

        // 省工程监理资质证书
        if (auditProject != null && ("11370800MB285591843370117765220".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765224".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765225".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765226".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765227".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765228".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765229".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765230".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765231".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765232".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765235".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765237".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765238".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765239".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765240".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765241".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765242".equals(auditTask.getItem_id())
                || "11370800MB285591843370117765243".equals(auditTask.getItem_id())
                || "11370800MB285591842370117765236".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sgcjlzzzs_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", record.getStr("zzlbjdj"));
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zzlbjdj"));
                ylgg.set("BEIZHU", "");
                ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800MB28559184");
                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sgcjlzzzs_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 省房地产开发企业资质证书
        if (auditProject != null && ("1137080123456789074370117001005".equals(auditTask.getItem_id())
                || "房地产开发企业二级资质延续".equals(auditTask.getTaskname())
                || "11370832MB2855272B4370117001005".equals(auditTask.getItem_id())
                || "TE37087320203090114370117001005".equals(auditTask.getItem_id())
                || "11370800MB285591843370117001006".equals(auditTask.getItem_id())
                || "房地产开发企业二级资质遗失补办".equals(auditTask.getTaskname())
                || "TE37087320203090113370117001007".equals(auditTask.getItem_id())
                || "房地产开发企业二级资质变更".equals(auditTask.getTaskname())
                || "TE37087320203090114370117001008".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sfdckfqyzzzs_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", record.getStr("zzlbjdj"));
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zzlbjdj"));
                ylgg.set("BEIZHU", "");
                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照颁发单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    //
                    ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sfdckfqyzzzs_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 高新房地产开发企业二级资质新申请等事项
        if (auditProject != null && ("1137080123456789074370117001014".equals(auditTask.getItem_id())
                || "1137080123456789073370117001008".equals(auditTask.getItem_id())
                || "1137080123456789074370117001005".equals(auditTask.getItem_id())
                || "1137080123456789074370117001007".equals(auditTask.getItem_id())
                || "1137080123456789074370117001006".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sfdckfqyzzzs_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", record.getStr("zzlbjdj"));
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zzlbjdj"));
                ylgg.set("BEIZHU", "");
                ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800004234373R");
                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", "济宁高新区城乡建设和交通局");

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sfdckfqyzzzs_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 房地产开发企业二级资质核定
        if (auditProject != null && ("11370800MB28559184300011711800301".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711800303".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711800302".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711800304".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20230721105849", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sfdckfqyzzzs_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", record.getStr("zzlbjdj"));
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zzlbjdj"));
                ylgg.set("BEIZHU", "");
                ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800004234373R");
                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", "济宁市行政审批服务局");

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sfdckfqyzzzs_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 省建筑工程质量检测机构资质证书
        if (auditProject != null && ("11370800MB285591847370117003005".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003006".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003007".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003004".equals(auditTask.getItem_id())
                || "11370800MB285591843370117003003".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711100002".equals(auditTask.getItem_id())
                || "11370800MB285591847370117003002".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711100003".equals(auditTask.getItem_id())
                || "11370800MB285591842370117008000".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20220408161905", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sjzgczljcjgzzzs_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", record.getStr("zizlbjdj"));
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", record.getStr("xxdz"));
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", record.getStr("zizlbjdj"));
                ylgg.set("JIANCEFANWEI", record.getStr("jcfw"));
                ylgg.set("BEIZHU", "");
                ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800MB28559184");
                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", record.getStr("frdb"));

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);
                    ylgg.set("JIANCEFANWEI", certinfoExtension.getStr("jcfw"));

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjzgczljcjgzzzs_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 推动数据至推送前置库ex_sjzyqyzzz_1
        pushDataToSjzyqyzzz();

        // 推送数据至前置库ex_sjzyqyzzz_1 针对双证照的
        pushDataToSjzyqyzzzTwo();

        // 如果为医疗事项，推送数据至前置库 11370800MB285591843370123017001
        if (auditProject != null && ("医疗机构放射性职业病危害建设项目预评价报告审核".equals(auditTask.getTaskname())
                || "11370800MB28559184300012311200101".equals(auditTask.getItem_id())
                || "11370800MB28559184300012311200201".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20211119194634", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_yljgfspjsh_1");

                ylgg.set("PROJECT_NAME", record.getStr("qingsrxmmc"));
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("YILIAOJIGOUMINGCHEN", record.getStr("qingsryljgmc"));
                ylgg.set("XIANGMUMINGCHEN", record.getStr("qingsrxmmc"));
                ylgg.set("XIANGMUDIZHI", record.getStr("wenbk3"));
                String xmzx = record.getStr("xmxz");
                if ("新建".equals(xmzx)) {
                    ylgg.set("XIANGMUXINGZHIXINJIAN", xmzx + " " + record.getStr("jsjtnr"));
                    ylgg.set("XIANGMUXINGZHIGAIJIAN", "/");
                    ylgg.set("XIANGMUXINGZHIKUOJIAN", "/");
                }
                else if ("改建".equals(xmzx)) {
                    ylgg.set("XIANGMUXINGZHIXINJIAN", "/");
                    ylgg.set("XIANGMUXINGZHIGAIJIAN", xmzx + " " + record.getStr("jsjtnr"));
                    ylgg.set("XIANGMUXINGZHIKUOJIAN", "/");
                }
                else if ("扩建".equals(xmzx)) {
                    ylgg.set("XIANGMUXINGZHIXINJIAN", "/");
                    ylgg.set("XIANGMUXINGZHIGAIJIAN", "/");
                    ylgg.set("XIANGMUXINGZHIKUOJIAN", xmzx + " " + record.getStr("jsjtnr"));
                }
                ylgg.set("FADINGDAIBIAOREN", record.getStr("fddbr"));
                ylgg.set("XIANGMUFUZEREN", record.getStr("xmfzr"));
                ylgg.set("LIANXIREN", record.getStr("lxr"));
                ylgg.set("LIANXIDIANHUA", record.getStr("lxdh"));
                ylgg.set("YUPINGSHENDANWEI", record.getStr("ypjdw"));
                ylgg.set("YUPINGJIABAOGAOBIANHAO", record.getStr("ypjbgbh"));
                ylgg.set("operatedate", new Date());

                if (StringUtil.isNotBlank(record.getStr("danxkz8"))) {
                    ylgg.set("ZHIYEBINGWEIHAILEIBIE", record.getStr("danxkz8"));
                }
                if (StringUtil.isNotBlank(record.getStr("danxkz9"))) {
                    ylgg.set("ZHIYEBINGWEIHAILEIBIE", record.getStr("danxkz9"));
                }

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("ID", certinfo.getRowguid());
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", "/");

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", ZwfwUserSession.getInstance().getAreaCode());

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getYljgfsjshByNumber(ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 如果为医疗事项，推送数据至前置库 11370800MB285591847370123022002
        if (auditProject != null && ("医疗机构放射性职业病危害建设项目竣工验收".equals(auditTask.getTaskname())
                || "11370800MB28559184300012311300201".equals(auditTask.getItem_id())
                || "11370800MB28559184300012311300101".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20211119221328", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("EX_JNSFSZLJSXM_1");
                ylgg.set("PROJECT_NAME", record.getStr("wbk1"));
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("YILIAOJIGOUMINGCHEN", record.getStr("yljgmc"));
                ylgg.set("XIANGMUMINGCHEN", record.getStr("wbk1"));
                ylgg.set("XIANGMUDIZHI", record.getStr("wbk2"));
                String xmzx = record.getStr("xmxz");
                ylgg.set("XIANGMUXINGZHI", xmzx + " " + record.getStr("xmjtnr"));
                ylgg.set("FADINGDAIBIAOREN", record.getStr("wbk3"));
                ylgg.set("XIANGMUFUZEREN", record.getStr("wbk5"));
                ylgg.set("LIANXIREN", record.getStr("wbk4"));
                ylgg.set("LIANXIDIANHUA", record.getStr("wbk6"));
                ylgg.set("KONGZHIXIAOGUOPINGJIADANWEI", record.getStr("wbk15"));
                ylgg.set("KZXGPJBGBH_15", record.getStr("wbk16"));
                ylgg.set("ZHIYEBINGWEIHAILEIBIE", record.getStr("dxklb2"));
                ylgg.set("YANSHOUYIJIAN", "--");
                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("ID", certinfo.getRowguid());
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("rq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", "9999-12-30");

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", ZwfwUserSession.getInstance().getAreaCode());

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");
                Record ylggrecord = iJnProjectService.getYljgfsjshByNumber(ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 如果为建（构）筑物工程建筑垃圾处置核准,推送到渣土前置库
        if (auditProject != null && ("11370800MB28559184737011703800002".equals(auditTask.getItem_id())
                || "11370800MB28559184737011703800001".equals(auditTask.getItem_id())
                || "11370826MB2858051T4370117038000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4370117038000".equals(auditTask.getItem_id())
                || "11370828MB286029024370117038000".equals(auditTask.getItem_id())
                || "11370829771010370B4370117038000".equals(auditTask.getItem_id())
                || "11370830F5034613XX4370117038000".equals(auditTask.getItem_id())
                || "11370831MB285634767370117038000".equals(auditTask.getItem_id())
                || "11370832MB2855272B2370117038000".equals(auditTask.getItem_id())
                || "11370881MB2796110U4370117038000".equals(auditTask.getItem_id())
                || "11370812MB2868524U4370117038000".equals(auditTask.getItem_id())
                || "11370883MB2857374H4370117038000".equals(auditTask.getItem_id())
                || "1237080079619134XQ4370117038000".equals(auditTask.getItem_id())
                || "12370800069995325T4370117038000".equals(auditTask.getItem_id())
                || "TE3708732020309011437011703800001".equals(auditTask.getItem_id())
                || "TE3708732020309011437011703800002".equals(auditTask.getItem_id())
                || "11370811MB279691104370117038000".equals(auditTask.getItem_id()))) {
            Record record = iCxBusService.getDzbdDetail("formtable20220420093558", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_csjzljpzs_1");

                ylgg.set("sqrq", record.getDate("sqrq"));
                ylgg.set("dwmc", record.getStr("dwmc"));
                ylgg.set("tyshxydm", record.getStr("tyshxydm"));
                ylgg.set("xm", record.getStr("xm"));
                ylgg.set("lxdh", record.getStr("lxdh"));
                ylgg.set("sfzh", record.getStr("sfzh"));
                ylgg.set("wtdlrxm", record.getStr("wtdlrxm"));
                ylgg.set("wtdlrlxdh", record.getStr("wtdlrlxdh"));
                ylgg.set("wtdlrsfzh", record.getStr("wtdlrsfzh"));
                ylgg.set("gdmc", record.getStr("gdmc"));
                ylgg.set("gdwz", record.getStr("gdwz"));
                ylgg.set("jsdwmc", record.getStr("jsdwmc"));
                ylgg.set("jsdwfddbr", record.getStr("jsdwfddbr"));
                ylgg.set("jsdwlxdh", record.getStr("jsdwlxdh"));
                ylgg.set("sgdwmc", record.getStr("sgdwmc"));
                ylgg.set("sgdwfddbr", record.getStr("sgdwfddbr"));
                ylgg.set("sgdwdh", record.getStr("sgdwdh"));
                ylgg.set("tf", record.getStr("tf"));
                ylgg.set("jzlj", record.getStr("jzlj"));
                ylgg.set("czqxrq", record.getDate("czqxrq"));
                ylgg.set("czqxrqend", record.getDate("czqxrqend"));
                ylgg.set("yssd", record.getStr("yssd"));
                ylgg.set("yslx", record.getStr("yslx"));
                ylgg.set("wbk22", record.getStr("wbk22"));
                ylgg.set("lxdh1", record.getStr("lxdh1"));
                ylgg.set("xncs2", record.getStr("xncs2"));
                ylgg.set("lxdh2", record.getStr("lxdh2"));
                ylgg.set("xncs3", record.getStr("xncs3"));
                ylgg.set("lxdh3", record.getStr("lxdh3"));
                ylgg.set("operatedate", new Date());

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("zzbh", certinfoExtension.getStr("zzbh"));

                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("fzrq", rq);
                }

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iZtProjectService.getDzbdDetailByZzbh("ex_csjzljpzs_1", ylgg.getStr("zzbh"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iZtProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iZtProjectService.inserRecord(ylgg);
                }
            }
        }

        // 推送数据到前置库
        pushDataToQzk(auditProject, auditTask);

        // 公共场所卫生许可证变更法人
        if (auditProject != null && "公共场所卫生许可证变更法人".equals(auditTask.getTaskname())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220414164913", auditProject.getRowguid());
            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
            if (certggxkws != null && record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_ggcswsxkxsq_1");
                ylgg.setPrimaryKeys("rowguid");
                certggxkws.setLegal(record.getStr("xfddbr"));
                certggxkws.setCertnum(record.getStr("xfrsfzh"));
                iCertGgxkwsService.update(certggxkws);
                ylgg.set("FUZEREN", record.getStr("xfddbr"));
                ylgg.setPrimaryKeys("rowguid");
                ylgg.set("operatedate", new Date());
                Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(record.getStr("zzbh"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
            }
        }

        // 公共场所卫生许可变更单位名称
        if (auditProject != null && "公共场所卫生许可证变更单位名称".equals(auditTask.getTaskname())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220413120417", auditProject.getRowguid());
            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
            if (certggxkws != null && record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_ggcswsxkxsq_1");
                ylgg.setPrimaryKeys("rowguid");
                certggxkws.setMonitorname(record.getStr("dwmc"));
                iCertGgxkwsService.update(certggxkws);
                ylgg.set("DANWEIMINGCHEN", record.getStr("dwmc"));

                ylgg.setPrimaryKeys("rowguid");
                ylgg.set("operatedate", new Date());
                Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(record.getStr("zzbh"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
            }
        }

        // 公共场所卫生许可证延续
        if (auditProject != null && "公共场所卫生许可证延续".equals(auditTask.getTaskname())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220413141101", auditProject.getRowguid());
            CertGgxkws certggxkws = iCertGgxkwsService.getCertByCertno(record.getStr("zzbh"));
            if (certggxkws != null && record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_ggcswsxkxsq_1");
                ylgg.setPrimaryKeys("rowguid");
                ylgg.set("operatedate", new Date());

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }

                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"),
                            EpointDateUtil.DATE_FORMAT));
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"),
                            EpointDateUtil.DATE_FORMAT));
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"),
                            EpointDateUtil.DATE_FORMAT));

                }

                ylgg.setPrimaryKeys("rowguid");
                Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(record.getStr("zzbh"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
            }
        }

        // 公共场所卫生许可新申请
        if (auditProject != null && "公共场所卫生许可证新申请".equals(auditTask.getTaskname())
                || "公共场所卫生许可（新办）".equals(auditTask.getTaskname())
                || auditProject.getProjectname().contains("公共场所卫生许可（延续）")
                || auditProject.getProjectname().contains("公共场所卫生许可（变更）")) {
            Record record = iCxBusService.getDzbdDetail("formtable20220228091113", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                CertGgxkws certggxkws = new CertGgxkws();
                certggxkws.setRowguid(UUID.randomUUID().toString());
                certggxkws.setOperatedate(new Date());
                certggxkws.setMonitorname(record.getStr("sqdw"));
                certggxkws.setAreacode(auditProject.getAreacode());
                certggxkws.setManageaddress(record.getStr("dwdz"));
                certggxkws.setRegisteraddress(record.getStr("dwdz"));// xin
                certggxkws.setCreditcode(record.getStr("tyshxydm"));// xin
                certggxkws.setEcontype(record.getStr("ggwsjjxz"));// xin
                certggxkws.setLegal(record.getStr("fddbr"));
                certggxkws.setCerttype("居民身份证");
                certggxkws.setCertnum(record.getStr("frsfzhm"));
                certggxkws.setMajortype(record.getStr("xkjyxm"));
                certggxkws.setMajorchildtype(record.getStr("jyzlb"));
                certggxkws.setWorkertotal(record.getStr("zgrs"));
                certggxkws.setPracticenum(record.getStr("zgrs"));
                certggxkws.setQuailynum(record.getStr("zgrs"));
                certggxkws.setSellarea(record.getStr("symj"));
                certggxkws.setIshvac(record.getStr("sqdw"));
                certggxkws.setWatertype(record.getStr("yys"));
                certggxkws.setApplytype(record.getStr("sqlb"));
                certggxkws.setVersion("0");
                certggxkws.setIs_enable("1");
                certggxkws.setIs_canale("0");

                ylgg.setSql_TableName("ex_ggcswsxkxsq_1");

                ylgg.set("PROJECT_NAME", "--");
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("DANWEIMINGCHEN", record.getStr("sqdw"));
                ylgg.set("FUZEREN", record.getStr("fddbr"));
                ylgg.set("DIZHI", record.getStr("dwdz"));
                ylgg.set("XUKEXIANGMU", record.getStr("xkjyxm"));
                ylgg.set("JINGJIXINGZHIYI", record.getStr("ggwsjjxz"));

                ylgg.set("NIANDUSHENHEJILU1YI", "--");
                ylgg.set("NIANDUSHENHEJILU2YI", "--");
                ylgg.set("NIANDUSHENHEJILUSHIJIAN1YI", "--");
                ylgg.set("NIANDUSHENHEJILUSHIJIAN2YI", "--");
                ylgg.set("WEISHENGJIANDUJIANCHAJILURIQI", "--");
                ylgg.set("WSJDJCJLWFSS_23", "--");
                ylgg.set("WSJDJCJLCFYJ_24", "--");
                ylgg.set("WEISHENGJIANDUJIANCHAJILURIQI1", "--");
                ylgg.set("WSJDJCJLWFSS_26", "--");
                ylgg.set("WSJDJCJLCFYJ_27", "--");
                ylgg.set("operatedate", new Date());

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("ID", certinfo.getRowguid());
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("babh"));

                    certggxkws.setCertno(certinfoExtension.getStr("babh"));
                    certggxkws.setBegintime(certinfoExtension.getDate("fzrq"));
                    certggxkws.setEndtime(certinfoExtension.getDate("yxqz"));

                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxqz);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());

                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                String ouCode = userSession.getOuCode();
                // 如果是兖州区
                if ("JN370882SPJ".equals(ouCode)) {
                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", "济宁市兖州区行政审批服务局");
                }
                else {
                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", userSession.getOuName());
                }

                certggxkws.setOuname(userSession.getOuName());

                if ("TE37087320203090114370123021001".equals(auditTask.getItem_id())) {
                    ylgg.set("DEPT_ORGANIZE_CODE", "TE3708732020309011");
                }
                else {
                    FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                    if (frameOuExten != null) {
                        // 证照办法单位组织机构代码
                        ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    }
                }

                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", ZwfwUserSession.getInstance().getAreaCode());

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                // 新增到公共许可本地库
                iCertGgxkwsService.insert(certggxkws);

                Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    if (StringUtils.isNotBlank(ylgg.getStr("ID"))) {
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }
        }

        // 申请省际普通货物水路运输经营许可
        if (auditProject != null && "申请省际普通货物水路运输经营许可".equals(auditTask.getTaskname())) {
            Record record = iCxBusService.getDzbdDetail("slysqykysq", auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                CertHwslysjyxk slys = new CertHwslysjyxk();
                slys.setRowguid(UUID.randomUUID().toString());
                slys.setOperatedate(new Date());

                slys.setJyzmc(record.getStr("shenqdwmc"));
                slys.setJyrdz(record.getStr("shenqdwdz"));
                slys.setFddbr(auditProject.getLegal());
                slys.setLxdh(record.getStr("shoujhm"));
                slys.setQyxz(record.getStr("shenqdwmc"));
                slys.setJyqy(record.getStr("jingyqy"));
                slys.setNhyh(record.getStr("neihyh"));
                slys.setJyfs(record.getStr("jingyfs"));
                slys.setZczj(record.getStr("zhuczj"));
                slys.setKyjyfw(record.getStr("jingyfwzykyhx"));
                slys.setHyjyfw(record.getStr("jingyfwzyhyhx"));
                slys.setJyfw(record.getStr("jingyfwjy"));
                slys.set("jjlx", record.getStr("jingjlx"));
                slys.setVersion("0");
                slys.setIs_enable("1");
                slys.setIs_cancel("0");
                slys.setFzjg(record.getStr("chaobdw"));

                ylgg.setSql_TableName("ex_sjpthwslysjyxk_1");
                ylgg.set("PROJECT_NAME", "申请省际普通货物水路运输经营许可");
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHENYI", record.getStr("shenqdwmc"));
                ylgg.set("FADINGDAIBIAORENYI", auditProject.getLegal());
                ylgg.set("DIZHIYI", record.getStr("shenqdwdz"));
                ylgg.set("JINGJILEIXINGYI", record.getStr("jingjlx"));
                ylgg.set("JIANYINGYI", record.getStr("jingyfwjy"));
                ylgg.set("JINGYINGQIXIAN", "--");
                ylgg.set("PIZHUNJIGUANJIWENHAO", "--");
                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("ID", certinfo.getRowguid());
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                    String year = certinfoExtension.getStr("QN");
                    String month = certinfoExtension.getStr("QY");
                    String day = certinfoExtension.getStr("QR");
                    String yearz = certinfoExtension.getStr("ZN");
                    String monthz = certinfoExtension.getStr("ZY");
                    String dayz = certinfoExtension.getStr("ZR");
                    slys.setBh(certinfoExtension.getStr("bh"));
                    slys.setJyxkzbh(certinfoExtension.getStr("bh"));
                    slys.setKsrq(EpointDateUtil.convertString2DateAuto(year + "-" + month + "-" + day));
                    slys.setJzrq(EpointDateUtil.convertString2DateAuto(yearz + "-" + monthz + "-" + dayz));
                    slys.setFzrq(EpointDateUtil.convertString2DateAuto(year + "-" + month + "-" + day));

                    slys.setJyqx(certinfoExtension.getStr("JYQX"));
                    slys.setPzjg(certinfoExtension.getStr("PJJGJWH"));

                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", year + "-" + month + "-" + day);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", year + "-" + month + "-" + day);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yearz + "-" + monthz + "-" + dayz);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", ZwfwUserSession.getInstance().getAreaCode());

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                // 新增到公共许可本地库
                iCertHwslysjyxkService.insert(slys);

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 公共场所卫生许可变更单位名称
        if (auditProject != null && "申请省际普通货物水路运输业务经营变更名称、法定代表人、注册地址、经济类型等证书内容".equals(auditTask.getTaskname())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220507171039", auditProject.getRowguid());
            CertHwslysjyxk certHwslysjyxk = iCertHwslysjyxkService.getCertByCertno(record.getStr("zzbh"));
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sjpthwslysjyxk_1");
                ylgg.setPrimaryKeys("rowguid");
                if (certHwslysjyxk != null) {
                    if (StringUtil.isNotBlank(record.getStr("xqymc"))) {
                        certHwslysjyxk.setJyzmc(record.getStr("xqymc"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xdz"))) {
                        certHwslysjyxk.setJyrdz(record.getStr("xdz"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xfddbr"))) {
                        certHwslysjyxk.setFddbr(record.getStr("xfddbr"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xlkys"))) {
                        certHwslysjyxk.setKyjyfw(record.getStr("xlkys"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xhwys"))) {
                        certHwslysjyxk.setHyjyfw(record.getStr("xhwys"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xjy"))) {
                        certHwslysjyxk.setJyfw(record.getStr("xjy"));
                    }
                    if (StringUtil.isNotBlank(record.getStr("xjjlx"))) {
                        certHwslysjyxk.set("jyfs", record.getStr("xjjlx"));
                    }
                    iCertHwslysjyxkService.update(certHwslysjyxk);
                }

                ylgg.setSql_TableName("ex_sjpthwslysjyxk_1");
                ylgg.set("operatedate", new Date());
                ylgg.set("PROJECT_NAME", "申请省际普通货物水路运输经营许可");
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                if (StringUtil.isNotBlank(record.getStr("xqymc"))) {
                    ylgg.set("QIYEMINGCHENYI", record.getStr("xqymc"));
                }
                else {
                    ylgg.set("QIYEMINGCHENYI", certHwslysjyxk.getJyzmc());
                }
                if (StringUtil.isNotBlank(record.getStr("xdz"))) {
                    ylgg.set("DIZHIYI", record.getStr("xdz"));
                }
                else {
                    ylgg.set("DIZHIYI", certHwslysjyxk.getJyrdz());
                }
                ylgg.set("FADINGDAIBIAORENYI", auditProject.getLegal());

                if (StringUtil.isNotBlank(record.getStr("xjjlx"))) {
                    ylgg.set("JINGJILEIXINGYI", record.getStr("xjjlx"));
                }
                else {
                    ylgg.set("JINGJILEIXINGYI", certHwslysjyxk.getJyfs());
                }
                if (StringUtil.isNotBlank(record.getStr("xjy"))) {
                    ylgg.set("JIANYINGYI", record.getStr("xjy"));
                }
                else {
                    ylgg.set("JIANYINGYI", certHwslysjyxk.getJyfw());
                }
                ylgg.set("JINGYINGQIXIAN", "--");
                ylgg.set("PIZHUNJIGUANJIWENHAO", "--");
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("ID", certinfo.getRowguid());
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                    String year = certinfoExtension.getStr("QN");
                    String month = certinfoExtension.getStr("QY");
                    String day = certinfoExtension.getStr("QR");
                    String yearz = certinfoExtension.getStr("ZN");
                    String monthz = certinfoExtension.getStr("ZY");
                    String dayz = certinfoExtension.getStr("ZR");

                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", year + "-" + month + "-" + day);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", year + "-" + month + "-" + day);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yearz + "-" + monthz + "-" + dayz);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                if (StringUtil.isNotBlank(record.getStr("xfddbr"))) {
                    ylgg.set("HOLDER_NAME", record.getStr("xfddbr"));
                }
                else {
                    ylgg.set("HOLDER_NAME", certHwslysjyxk.getFddbr());
                }
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", ZwfwUserSession.getInstance().getAreaCode());

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        if (auditProject != null && "11370800MB285591847370117024000".equals(auditTask.getItem_id())) {
            Record record = iCxBusService.getCsdxggDetailByRowguid(auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_cshwggpzs_1");
                ylgg.set("mchzz", record.getStr("sqdwmc") + "/" + record.getStr("tyxydm"));
                ylgg.set("fddbr", record.getStr("fddbrxm"));

                String szwz = record.getStr("szwz");
                if (StringUtil.isNotBlank(szwz)) {
                    ylgg.set("szwz", szwz);
                }
                else {
                    ylgg.set("szwz", "未涉及");
                }

                String ctggszly = record.getStr("ctggszly");
                if (StringUtil.isNotBlank(ctggszly)) {
                    ylgg.set("ctggszly", ctggszly);
                }
                else {
                    ylgg.set("ctggszly", "未涉及");
                }

                String ctggcph = record.getStr("ctggcph");
                if (StringUtil.isNotBlank(ctggcph)) {
                    ylgg.set("ctggcph", ctggcph);
                }
                else {
                    ylgg.set("ctggcph", "未涉及");
                }

                String ctggjdcxszh = record.getStr("ctggjdcxszh");
                if (StringUtil.isNotBlank(ctggjdcxszh)) {
                    ylgg.set("ctggjdcxszh", ctggjdcxszh);
                }
                else {
                    ylgg.set("ctggjdcxszh", "未涉及");
                }

                String ctggcllx = record.getStr("ctggcllx");
                if (StringUtil.isNotBlank(ctggcllx)) {
                    ylgg.set("ctggcllx", codeItemsService.getItemTextByCodeName("车体广告_车辆类型", ctggcllx));
                }
                else {
                    ylgg.set("ctggcllx", "未涉及");
                }

                String szbw = record.getStr("szbw");
                if (StringUtil.isNotBlank(szbw)) {
                    ylgg.set("szbw", codeItemsService.getItemTextByCodeName("设置部位", szbw));
                }
                else {
                    ylgg.set("szbw", "未涉及");
                }

                String fwcdcq = record.getStr("fwcdcq");

                if (StringUtil.isNotBlank(fwcdcq)) {
                    ylgg.set("fwcdcq", codeItemsService.getItemTextByCodeName("房屋场地产权", fwcdcq));
                }
                else {
                    ylgg.set("fwcdcq", "未涉及");
                }

                String szqxs = EpointDateUtil.convertDate2String(record.getDate("szqxs"), "yyyy-MM-dd");
                ylgg.set("szqxs", szqxs);
                String szqxz = EpointDateUtil.convertDate2String(record.getDate("szqxz"), "yyyy-MM-dd");
                ylgg.set("szqxz", szqxz);
                ylgg.set("szqx", codeItemsService.getItemTextByCodeName("广告期限", record.getStr("qx")));

                String ldsggbw = record.getStr("ldsggbw");

                if (StringUtil.isNotBlank(ldsggbw)) {
                    ylgg.set("ldsggbw", codeItemsService.getItemTextByCodeName("落地式广告设置部位", ldsggbw));
                }
                else {
                    ylgg.set("ldsggbw", "未涉及");
                }

                ylgg.set("pzxs", codeItemsService.getItemTextByCodeName("批准形式", record.getStr("pzxs")));

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    ylgg.set("zmwh", certinfoExtension.get("zmwh"));

                    String sqrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("sqrq"),
                            EpointDateUtil.DATE_FORMAT);
                    ylgg.set("sqrq", sqrq);

                    String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"),
                            EpointDateUtil.DATE_FORMAT);
                    ylgg.set("fzrq", fzrq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("cezlx", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("cezlx", "自然人");
                    }
                }
                else {
                    ylgg.set("cezlx", "");
                }
                ylgg.set("czrmc", auditProject.getApplyername());
                ylgg.set("czrzjhm", auditProject.getCertnum());
                ylgg.set("czrzjlx",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));

                // 证照颁发单位
                ylgg.set("zzbfdw", userSession.getOuName());
                ylgg.set("certguid", auditProject.get("certrowguid"));

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("zzbfdwzzjgdm", frameOuExten.getStr("orgcode"));
                }

                ylgg.set("zzsjqhmc", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 数据状态
                ylgg.set("state", 1);
                ylgg.set("sjrksj", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getCshwggpzsByZmwh(ylgg.getStr("zmwh"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", 2);
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    iJnProjectService.inserRecord(ylgg);
                }

            }
        }

        if (auditProject != null && "11370800MB285591843371073014000".equals(auditTask.getItem_id())) {
            Record record = iCxBusService.getYsDetailByRowguid(auditProject.getRowguid());
            if (record != null) {
                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_tsqkyswts_1");
                ylgg.set("skm", record.getStr("skm"));
                ylgg.set("wtsbh", record.getStr("wtsbh"));
                ylgg.set("cbdwmc", record.getStr("cbdwmc"));
                ylgg.set("cbdwdz", record.getStr("cbdwdz"));
                ylgg.set("ysqymc", record.getStr("ysqymc"));
                ylgg.set("ysqydz", record.getStr("ysqydz"));
                if (StringUtil.isNotBlank(record.getStr("zfxdwdz"))) {
                    ylgg.set("zfxdwdz", record.getStr("zfxdwdz"));
                }
                else {
                    ylgg.set("zfxdwdz", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("zfxdwmc"))) {
                    ylgg.set("zfxdwmc", record.getStr("zfxdwmc"));
                }
                else {
                    ylgg.set("zfxdwmc", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("gjbzsh"))) {
                    ylgg.set("gjbzsh", record.getStr("gjbzsh"));
                }
                else {
                    ylgg.set("gjbzsh", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("banci"))) {
                    ylgg.set("banci", record.getStr("banci"));
                }
                else {
                    ylgg.set("banci", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("zgbzkh"))) {
                    ylgg.set("zgbzkh", record.getStr("zgbzkh"));
                }
                else {
                    ylgg.set("zgbzkh", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("kanqi"))) {
                    ylgg.set("kanqi", record.getStr("kanqi"));
                }
                else {
                    ylgg.set("kanqi", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("kaiben"))) {
                    ylgg.set("kaiben", record.getStr("kaiben"));
                }
                else {
                    ylgg.set("kaiben", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("yinshu"))) {
                    ylgg.set("yinshu", record.getStr("yinshu"));
                }
                else {
                    ylgg.set("yinshu", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("zyz"))) {
                    ylgg.set("zyz", record.getStr("zyz"));
                }
                else {
                    ylgg.set("zyz", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("yeshu"))) {
                    ylgg.set("yeshu", record.getStr("yeshu"));
                }
                else {
                    ylgg.set("yeshu", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("yinzhang"))) {
                    ylgg.set("yinzhang", record.getStr("yinzhang"));
                }
                else {
                    ylgg.set("yinzhang", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("zebian"))) {
                    ylgg.set("zebian", record.getStr("zebian"));
                }
                else {
                    ylgg.set("zebian", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("zishu"))) {
                    ylgg.set("zishu", record.getStr("zishu"));
                }
                else {
                    ylgg.set("zishu", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("dingjia"))) {
                    ylgg.set("dingjia", record.getStr("dingjia"));
                }
                else {
                    ylgg.set("dingjia", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("zebiann"))) {
                    ylgg.set("zebiann", record.getStr("zebiann"));
                }
                else {
                    ylgg.set("zebiann", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("ygys"))) {
                    ylgg.set("ygys", record.getStr("ygys"));
                }
                else {
                    ylgg.set("ygys", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("banshi"))) {
                    ylgg.set("banshi", record.getStr("banshi"));
                }
                else {
                    ylgg.set("banshi", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("zwyz"))) {
                    ylgg.set("zwyz", record.getStr("zwyz"));
                }
                else {
                    ylgg.set("zwyz", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("jyfs"))) {
                    ylgg.set("jyfs", record.getStr("jyfs"));
                }
                else {
                    ylgg.set("jyfs", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("tgsl"))) {
                    ylgg.set("tgsl", record.getStr("tgsl"));
                }
                else {
                    ylgg.set("tgsl", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("tbss"))) {
                    ylgg.set("tbss", record.getStr("tbss"));
                }
                else {
                    ylgg.set("tbss", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("yzgg"))) {
                    ylgg.set("yzgg", record.getStr("yzgg"));
                }
                else {
                    ylgg.set("yzgg", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("ysff"))) {
                    ylgg.set("ysff", record.getStr("ysff"));
                }
                else {
                    ylgg.set("ysff", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("yzsl"))) {
                    ylgg.set("yzsl", record.getStr("yzsl"));
                }
                else {
                    ylgg.set("yzsl", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("zdff"))) {
                    ylgg.set("zdff", record.getStr("zdff"));
                }
                else {
                    ylgg.set("zdff", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("zdcs"))) {
                    ylgg.set("zdcs", record.getStr("zdcs"));
                }
                else {
                    ylgg.set("zdcs", "/");
                }

                if (StringUtil.isNotBlank(record.getStr("wtfjbrxm"))) {
                    ylgg.set("wtfjbrxm", record.getStr("wtfjbrxm"));
                }
                else {
                    ylgg.set("wtfjbrxm", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("wtfjbrdh"))) {
                    ylgg.set("wtfjbrdh", record.getStr("wtfjbrdh"));
                }
                else {
                    ylgg.set("wtfjbrdh", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("sfzhm"))) {
                    ylgg.set("sfzhm", record.getStr("sfzhm"));
                }
                else {
                    ylgg.set("sfzhm", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("stfjbrxm"))) {
                    ylgg.set("stfjbrxm", record.getStr("stfjbrxm"));
                }
                else {
                    ylgg.set("stfjbrxm", "/");
                }
                if (StringUtil.isNotBlank(record.getStr("stfjbrdh"))) {
                    ylgg.set("stfjbrdh", record.getStr("stfjbrdh"));
                }
                else {
                    ylgg.set("stfjbrdh", "/");
                }

                String ywrq = EpointDateUtil.convertDate2String(record.getDate("ywrq"), "yyyy-MM-dd");

                if (StringUtil.isNotBlank(ywrq)) {
                    ylgg.set("ywrq", ywrq);
                }
                else {
                    ylgg.set("ywrq", "/");
                }

                String jsrq = EpointDateUtil.convertDate2String(record.getDate("jsrq"), "yyyy-MM-dd");

                if (StringUtil.isNotBlank(jsrq)) {
                    ylgg.set("jsrq", jsrq);
                }
                else {
                    ylgg.set("jsrq", "/");
                }

                String strq = EpointDateUtil.convertDate2String(record.getDate("strq"), "yyyy-MM-dd");

                if (StringUtil.isNotBlank(strq)) {
                    ylgg.set("strq", strq);
                }
                else {
                    ylgg.set("strq", "/");
                }

                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    String fzrq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    ylgg.set("fzrq", fzrq);

                }

                String tblx = record.getStr("tblx");

                if (StringUtil.isNotBlank(tblx)) {
                    ylgg.set("tblx", codeItemsService.getItemTextByCodeName("图书和期刊印刷委托书_图版类型", tblx));
                }
                else {
                    ylgg.set("tblx", "/");
                }

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("cezlx", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("cezlx", "自然人");
                    }
                }
                else {
                    ylgg.set("cezlx", "/");
                }
                ylgg.set("czrmc", auditProject.getApplyername());
                ylgg.set("czrzjhm", auditProject.getCertnum());
                ylgg.set("czrzjlx",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));

                // 证照颁发单位
                ylgg.set("zzbfdw", userSession.getOuName());
                ylgg.set("certguid", auditProject.get("certrowguid"));

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("zzbfdwzzjgdm", frameOuExten.getStr("orgcode"));
                }

                ylgg.set("zzsjqhmc", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 数据状态
                ylgg.set("state", 1);
                ylgg.set("sjrksj", EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getTsqkyswtsByWtsbh(ylgg.getStr("wtsbh"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", 2);
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    iJnProjectService.inserRecord(ylgg);
                }

            }
        }

        // 其他建设工程消防验收备案抽查(金乡县)
        if (auditProject != null && "11370828004332694N4371017920000".equals(auditTask.getItem_id())) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                if ("建设工程消防验收备案凭证（金乡）".equals(certinfo.getCertname())) {
                    JxxSendUtils utils = new JxxSendUtils();
                    utils.JxxSendCertInfoToLc(certinfo);
                }
            }
        }

        // 个性化推送前置库
        // 取水许可审批（设区的市级权限）（首次申请）
        if (auditProject != null && "11370800MB28559184300011910200501".equals(auditTask.getItem_id())) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                JnGxhSendUtils utils = new JnGxhSendUtils();
                utils.SendCertInfoToLc1(certinfo, auditProject, auditTask);
            }
        }

        // 公路建设项目施工许可
        if (auditProject != null && "11370800MB285591847370118022000".equals(auditTask.getItem_id())) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                JnGxhSendUtils utils = new JnGxhSendUtils();
                utils.SendCertInfoToLc2(certinfo, auditProject, auditTask);
            }
        }

        // 人力资源服务许可证 （新设/变更）
        if (auditProject != null && ("11370828MB28602902400011410700301".equals(auditTask.getItem_id())
                || "11370828MB28602902400011410700302".equals(auditTask.getItem_id()))) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            log.info("certrowguid：" + auditProject.get("certrowguid"));
            if (certinfo != null) {
                JnGxhSendUtils utils = new JnGxhSendUtils();
                utils.SendRlzyCertInfo(certinfo, auditProject, auditTask);
            }
        }

        // 劳务派遣经营许可证 （新设/变更）
        if (auditProject != null && ("11370828MB28602902400011410800301".equals(auditTask.getItem_id())
                || "11370828MB28602902400011410800302".equals(auditTask.getItem_id()))) {
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            log.info("certrowguid：" + auditProject.get("certrowguid"));
            if (certinfo != null) {
                JnGxhSendUtils utils = new JnGxhSendUtils();
                utils.SendLwpqjyCertInfo(certinfo, auditProject, auditTask);
            }
        }

        //tODo 后续推送的代码重构到这里
        switch (auditTask.getItem_id()){
            case "11370800MB28559184737011702400002":
            case "11370800MB28559184737011702400003":
            case "11370800MB28559184737011702400004":
                // 推送相关X射线数据
                pushXshexian();
                break;
            case "11370800MB285591847370117046000":
            case "11370832MB2855272B4370117046000":
            case "11370827MB2857833K4370117046000":
            case "11370826MB2858051T4370117046000":
                // 济宁人防工程施工图设计文件核准
                pushrenfangsgtsjwjhz();
                break;
        }


        // 如果当前办件有证照信息，根据办件表的certrowguid,获取证照基本信息、拓展信息的办件编号，把历史的证照信息is_history置为1
        try {
            updateCertInfoToHistory(auditProject.getCertrowguid());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送数据到前置库
     *
     * @param auditProject
     * @param auditTask
     */
    private void pushDataToQzk(AuditProject auditProject, AuditTask auditTask) {
        try {
            // 如果为城市大型户外广告设置
            if (auditProject != null && ("11370800MB28559184737011702400002".equals(auditTask.getItem_id())
                    || "11370800MB28559184737011702400003".equals(auditTask.getItem_id())
                    || "11370800MB28559184737011702400004".equals(auditTask.getItem_id()))) {
                Record record = iCxBusService.getDzbdDetail("formtable20230418162643", auditProject.getRowguid());
                if (record != null) {
                    try {
                        Record ylgg = new Record();
                        ylgg.setSql_TableName("csdxhwggszsps");
                        ylgg.setPrimaryKeys("rowguid");

                        ylgg.set("rowguid", UUID.randomUUID().toString());
                        ylgg.set("state", "insert");
                        ylgg.set("operatedate", new Date());

                        ylgg.set("tyshxydm", record.get("wbk3"));
                        ylgg.set("fddbrxm", record.get("wbk4"));
                        ylgg.set("fddbrsfzh", record.get("wbk5"));
                        ylgg.set("fddbrlxdh", record.get("wbk6"));
                        ylgg.set("wtdlrxm", record.get("wbk7"));
                        ylgg.set("wtdlrsfzh", record.get("wbk8"));
                        ylgg.set("wtdlrlxdh", record.get("wbk9"));
                        ylgg.set("sznr", record.get("dxwbk1"));
                        ylgg.set("szwz", record.get("wbk11"));
                        ylgg.set("jgjcl", record.get("wbk22"));
                        ylgg.set("lhxg", record.get("wbk23"));
                        ylgg.set("mj", record.get("wbk24"));
                        ylgg.set("chang", record.get("wbk17"));
                        ylgg.set("kuan", record.get("wbk18"));
                        ylgg.set("gao", record.get("wbk19"));

                        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));

                        if (certinfo != null) {
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", certinfo.getRowguid());
                            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                    false);
                            if (certinfoExtension == null) {
                                certinfoExtension = new CertInfoExtension();
                            }

                            ylgg.set("tznr", certinfoExtension.get("tznr"));
                            ylgg.set("fzrq", certinfoExtension.getDate("fzrq"));
                            ylgg.set("sqsj", certinfoExtension.getDate("sqsj"));
                            ylgg.set("zsbh", certinfoExtension.get("zsbh"));
                            ylgg.set("sqdw", certinfoExtension.get("sqdw"));
                            ylgg.set("szqx", certinfoExtension.get("szqx"));
                        }

                        iZtProjectService.inserRecord(ylgg);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // 如果为在城市建筑物、设施上张挂、张贴宣传品审批
            if (auditProject != null && "11370800MB285591847370117036000".equals(auditTask.getItem_id())
                    || "11370800MB28559184300011713900501".equals(auditTask.getItem_id())) {
                Record record = iCxBusService.getDzbdDetail("formtable20230418111305", auditProject.getRowguid());
                if (record != null) {
                    try {
                        // 本地前置库
                        Record ylgg = new Record();
                        ylgg.setSql_TableName("csjzsszgztxcp");
                        ylgg.setPrimaryKeys("rowguid");

                        ylgg.set("rowguid", UUID.randomUUID().toString());
                        ylgg.set("state", "insert");
                        ylgg.set("operatedate", new Date());

                        ylgg.set("tyshxydm", record.get("wbk3"));
                        ylgg.set("fddbrxm", record.get("wbk4"));
                        ylgg.set("fddbrsfzh", record.get("wbk5"));
                        ylgg.set("fddbrlxdh", record.get("wbk6"));
                        ylgg.set("wtdlrxm", record.get("wbk7"));
                        ylgg.set("wtdlrsfzh", record.get("wbk8"));
                        ylgg.set("wtdlrlxdh", record.get("wbk9"));
                        ylgg.set("szly", record.get("dxwbk1"));
                        ylgg.set("szwz", record.get("wbk11"));
                        ylgg.set("gmqmgs", record.get("wbk12"));
                        ylgg.set("dqcqgs", record.get("wbk14"));
                        ylgg.set("kpqqgs", record.get("wbk13"));
                        ylgg.set("tfcfgs", record.get("wbk15"));
                        ylgg.set("qtxs", record.get("wbk16"));
                        ylgg.set("chang", record.get("wbk17"));
                        ylgg.set("kuan", record.get("wbk18"));
                        ylgg.set("gao", record.get("wbk19"));

                        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                        if (certinfo != null) {
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", certinfo.getRowguid());
                            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                    false);
                            if (certinfoExtension == null) {
                                certinfoExtension = new CertInfoExtension();
                            }

                            ylgg.set("tznr", certinfoExtension.get("tznr"));
                            ylgg.set("fzrq", certinfoExtension.getDate("fzsj"));
                            ylgg.set("sqsj", certinfoExtension.getDate("sqsj"));
                            ylgg.set("zsbh", certinfoExtension.get("zsbh"));
                            ylgg.set("sqdw", certinfoExtension.get("sqdw"));
                            ylgg.set("szqx", certinfoExtension.get("szqx"));

                            try {
                                // 大数据前置库
                                Record dsj = new Record();
                                dsj.setSql_TableName("EX_ZCSJZWSSSZGZTXCPSZCHMDPZS_1");
                                dsj.set("LICENSE_NUMBER", certinfoExtension.get("zsbh"));
                                dsj.set("CERTIFICATE_DATE", certinfo.getAwarddate());
                                dsj.set("VALID_PERIOD_BEGIN", certinfo.getExpiredatefrom());
                                dsj.set("VALID_PERIOD_END", certinfo.getExpiredateto());
                                FrameOu ou = ouservice.getOuByOuGuid(certinfo.getOuguid());
                                if (ou != null) {
                                    dsj.set("DEPT_ORGANIZE_CODE", ou.getOucode());
                                    dsj.set("DEPT_NAME", ou.getOuname());
                                }
                                dsj.set("DISTRICTS_CODE", certinfo.getAreacode());
                                if (areaService.getAreaByAreacode(certinfo.getAreacode()).getResult() != null) {
                                    dsj.set("DISTRICTS_NAME", areaService.getAreaByAreacode(certinfo.getAreacode())
                                            .getResult().getXiaquname());
                                }
                                dsj.set("HOLDER_TYPE", certinfo.getCertownertype());
                                dsj.set("HOLDER_NAME", record.get("wbk4"));
                                dsj.set("CERTIFICATE_TYPE", certinfo.getCertownercerttype());
                                dsj.set("CERTIFICATE_NO", certinfo.getCertownerno());
                                dsj.set("CONTACT_PHONE", auditProject.getContactmobile());
                                dsj.set("PROJECT_NAME", auditProject.getProjectname());
                                dsj.set("STATE", "insert");
                                dsj.set("PERMIT_CONTENT", "--");
                                dsj.set("CERTIFICATE_LEVEL", "A");
                                dsj.set("SHENQINGDANWEI", certinfoExtension.get("sqdw"));
                                dsj.set("SHENQINGSHIJIAN", certinfoExtension.getDate("sqsj"));
                                dsj.set("DIAOZHENGNARONG", certinfoExtension.get("tznr"));
                                dsj.set("SHEZHIQIXIAN", certinfoExtension.get("szqx"));
                                String rq = EpointDateUtil.convertDate2String(record.getDate("rqfwxz1"), "yyyy-MM-dd");
                                String yxq = EpointDateUtil.convertDate2String(record.getDate("rqfwxz1end"),
                                        "yyyy-MM-dd");

                                // 有效期开始
                                dsj.set("VALID_PERIOD_BEGIN", rq);
                                // 有效期截止
                                dsj.set("VALID_PERIOD_END", yxq);

                                // 证照颁发单位
                                dsj.set("DEPT_NAME", userSession.getOuName());

                                FrameOuExtendInfo frameOuExten = ouservice
                                        .getFrameOuExtendInfo(userSession.getOuGuid());
                                if (frameOuExten != null) {
                                    // 证照办法单位组织机构代码
                                    dsj.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                                }

                                int APPLYERTYPE = auditProject.getApplyertype();
                                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                                    dsj.set("HOLDER_TYPE",
                                            codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                                    if ("20".equals(APPLYERTYPE + "")) {
                                        dsj.set("HOLDER_TYPE", "自然人");
                                    }
                                }
                                else {
                                    dsj.set("HOLDER_TYPE", "--");
                                }

                                dsj.set("CERTIFICATE_TYPE", codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                                        auditProject.getCerttype()));

                                // 行政区划名称
                                dsj.set("DISTRICTS_NAME",
                                        areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                                                .getResult().getXiaquname());
                                // 行政区划编码
                                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                                if (areacode.length() == 6) {
                                    areacode = areacode + "000000";
                                }
                                else if (areacode.length() == 9) {
                                    areacode = areacode + "000";
                                }
                                dsj.set("DISTRICTS_CODE", areacode);

                                dsj.set("rowguid", UUID.randomUUID().toString());
                                dsj.set("operatedate", new Date());
                                dsj.setPrimaryKeys("rowguid");

                                iJnProjectService.inserRecord(dsj);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        iZtProjectService.inserRecord(ylgg);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 呼叫快递
     */
    public Boolean call() {
        // 系统参数
        AS_CALLMODE = handleConfigService.getFrameConfig("AS_CALLMODE", auditProject.getCenterguid()).getResult();// 默认业务窗口呼叫模式
        AS_ZWFWZX_CODE = handleConfigService.getFrameConfig("AS_ZWFWZX_CODE", auditProject.getCenterguid()).getResult();
        AS_EMSWINDOW_CALL = handleConfigService.getFrameConfig("AS_EMSWINDOW_CALL", auditProject.getCenterguid())
                .getResult();
        Map<String, String> conditionMap = new HashMap<String, String>(16);
        conditionMap.put("projectguid = ", auditProject.getRowguid());
        conditionMap.put("NET_TYPE = ", "1");
        List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
        if (list != null && !list.isEmpty()) {
            for (AuditLogisticsBasicinfo auditLogisticsBasicinfo : list) {
                dataBean = auditLogisticsBasicinfo;
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
            }
            else {
                dataBean.setRcv_phone(AS_EMSWINDOW_CALL);
            }

        }
        else {// 业务窗口呼叫模式 保存基本信息并发送MQ
            AuditOrgaWindow auditOrgWindow = iAuditOrgaWindow
                    .getWindowByWindowGuid(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
            if (StringUtil.isBlank(auditOrgWindow.getTel())) {
                addCallbackParam("message", "请配置对应窗口联系电话");
                return false;
            }
            else {
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
        }
        else {
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
        }
        catch (Exception e) {
            dataBean.setCallState("04");// 呼叫失败
            iAuditLogisticsBasicInfo.updateLogisticsBasicinfo(dataBean);
            addCallbackParam("message", "预审通过，呼叫申报材料快递失败，请至待收取列表中查看并再次呼叫");
            return false;
        }
    }

    /**
     * 预审通过
     */
    public void ystgProject() {
        String isJSTY = ConfigUtil.getConfigValue("isJSTY");
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
            if ("1".equals(auditProject.getIf_express_ma())) {// 如果办件有申报材料物流信息则需要呼叫快递
                Boolean result = call();
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
                auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                userSession.getUserGuid(), "", "");

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
            auditProject.setWindowname(ZwfwUserSession.getInstance().getWindowNo());
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
                        + "在网上申请的“" + auditProject.getProjectname() + "”，预审已获通过，请您尽快登录江苏政务服务网确认您的物流信息";// 短信内容
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
                }
                catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文书列表信息
     *
     * @param tasktype
     *            事项类型
     * @return List<SelectItem> 文书列表
     */
    public JSONArray initDocModel(String tasktype) {
        String notOpenDocs = auditTaskExtension.getIs_notopendoc();
        String asdocword = handleConfigService.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
        boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
        List<Map<String, String>> listDocs = handleProjectService
                .initProjectDocList(projectguid, taskguid, auditProject.getCenterguid(), tasktype, notOpenDocs, isword)
                .getResult();

        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        jsonObject = new JSONObject();
        jsonObject.put("id", "");
        jsonObject.put("text", "请选择文书");
        jsonArray.add(jsonObject);
        if (listDocs != null && !listDocs.isEmpty()) {
            for (Map<String, String> doc : listDocs) {
                if (doc.size() > 0) {
                    jsonObject = new JSONObject();
                    String param = "&projectguid=" + projectguid + "&ProcessVersionInstanceGuid="
                            + processVersionInstanceGuid;
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(doc.get("isPrint"))) {
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(doc.get("isWord"))) {
                            address = getRequestContextPath()
                                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + doc.get("docUrl");
                        }
                        else {
                            address = doc.get("docUrl") + param;
                            address += "&isPrint=1";
                        }
                    }
                    else {
                        address = doc.get("docUrl") + param;
                    }
                    jsonObject.put("id", address);
                    jsonObject.put("text", doc.get("docName")
                            + (ZwfwConstant.CONSTANT_STR_ONE.equals(doc.get("isPrint")) ? "（已打印）" : ""));
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    /**
     * 文书地址
     * <p>
     * e
     */
    public String getWenshuAddress(String wenshuvalue) {
        if (auditProject != null && auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YJJ
                && !"".equals(wenshuvalue)) {
            if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(wenshuvalue)) {
                String asdocword = handleConfigService.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid())
                        .getResult();
                boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
                address = handleDocService.getDocEditPage(taskguid, ZwfwUserSession.getInstance().getCenterGuid(),
                        auditTaskExtension.getIs_notopendoc(), wenshuvalue, false, isword).getResult();
                if (StringUtil.isNotBlank(address)) {
                    if (isword) {
                        address += "&projectguid=" + projectguid + "&ProcessVersionInstanceGuid="
                                + processVersionInstanceGuid + "&doctype=" + wenshuvalue;
                    }
                    else {

                    }
                }
            }
            else {
                address = "";
            }
        }
        return address;
    }

    /**
     * 判断是否使用word模板
     *
     * @return
     */
    public boolean isUserWordDoc() {
        boolean flag = true;
        String asdocword = handleConfigService.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
        if (ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword)) {
            flag = false;
        }
        return flag;
    }

    /**
     * [推送好差评办件服务数据]
     *
     * @param auditProject
     * @param serviceNumber
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void turnhcpevaluate(AuditProject auditProject, int serviceNumber, String servicename,
            String newserviceTime) {
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
        //
        if (auditTask != null) {
            JSONObject json = new JSONObject();
            String ouguid = auditProject.getOuguid();

            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(ouguid);
            String deptcode = "";
            if (frameOuExten != null) {
                deptcode = frameOuExten.getStr("orgcode");
                if (StringUtil.isBlank(deptcode)) {
                    deptcode = "11370900MB28449441";
                }
            }
            else {
                deptcode = "11370900MB28449441";
            }
            json.put("taskCode", auditTask.getItem_id());
            json.put("areaCode", auditProject.getAreacode().replace("370882", "370812") + "000000");
            // 传主项的事项名称
            if (StringUtil.isNotBlank(auditTask.getItem_id()) && auditTask.getItem_id().length() == 33) {
                AuditTask task = ihandleControlService.getAuditTaskByItemid(auditTask.getItem_id().substring(0, 31));
                if (task != null) {
                    json.put("taskName", task.getTaskname());
                }
                else {
                    json.put("taskName", auditTask.getTaskname());
                }
                json.put("taskName", auditTask.getTaskname());
            }
            else {
                json.put("taskName", auditTask.getTaskname());
            }
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
            }
            else if (applyertype == 20) {
                applyertype = 1;
            }
            else if (applyertype == 30) {
                applyertype = 9;
            }
            else {
                applyertype = 9;
            }
            json.put("userProp", applyertype);
            json.put("userName",
                    StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无");
            json.put("userPageType", "111");
            json.put("proManager",
                    StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername().trim()
                            : "无");
            json.put("certKey",
                    StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
            json.put("certKeyGOV",
                    StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
            json.put("serviceName", servicename);// 环节名称

            json.put("serviceNumber", serviceNumber);
            if (StringUtil.isNotBlank(newserviceTime)) {
                json.put("serviceTime", newserviceTime);
            }
            else {
                json.put("serviceTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("projectType", auditProject.getTasktype());
            if (3 == Integer.parseInt(proStatus)) {
                json.put("resultDate",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            }

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

            json.put("taskType", taskType);
            json.put("mobile",
                    StringUtil.isNotBlank(auditProject.getContactmobile()) ? auditProject.getContactmobile().trim()
                            : "0");
            json.put("deptCode", deptcode);
            json.put("projectName", "关于" + auditProject.getApplyername().trim() + auditTask.getTaskname() + "的业务");
            json.put("creditNum",
                    StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
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
            // 启动一个异步任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
                if (StringUtil.isNotBlank(resultsign)) {
                    JSONObject jsonobject = JSONObject.parseObject(resultsign);
                    JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
                    if ("200".equals(jsonstatus.get("code").toString())) {
                        JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                        log.info("");
                        if ("1".equals(jsoncustom.get("code").toString())) {

                        }
                    }
                }
            });

        }
    }

    /**
     * 推送房产基本信息、买方、卖方信息
     */
    public Boolean turnfcitembase() {
        Record record = iJnProjectService.DzbdItemBaseinfoByProjectGuid(projectguid);
        JSONObject ycslData = new JSONObject();
        // 使用自建系统
        ycslData.put("useSelfBuildSystem", true);
        if (record != null) {
            String LSH = auditProject.getFlowsn();
            // 业务类型
            String YWLX = record.getStr("YWLX");
            // 业务总面积
            String YWZMJ = record.getStr("YWZMJ");
            // 房产价值
            String FCJZ = record.getStr("FCJZ");
            // 房屋坐落
            String FWZL = record.getStr("FWZL");
            // 产权人姓名
            String CQRLX = record.getStr("CQRLX");
            // 附属房屋面积
            String FSFWMJ = record.getStr("FSFWMJ");
            // 买方姓名
            String MFXM = record.getStr("MFXM");
            // 买方身份证明号
            String MFSFZMH = record.getStr("MFSFZMH");
            // 买方证件类型名称
            String MFZJLXMC = record.getStr("MFZJLXMC");
            // 买方类型
            String MFLX = record.getStr("MFLX");
            // 买方间关系
            String MFJGX = record.getStr("MFJGX");
            // 买方共有情况
            String MFGYQK = record.getStr("MFGYQK");
            // 共有份额
            String GYFE = record.getStr("GYFE");
            // 买方联系电话
            String MFLXDH = record.getStr("MFLXDH");
            // 买方住址
            String MFZZ = record.getStr("MFZZ");
            // 买方户籍所在地
            String MFHJSZD = record.getStr("MFHJSZD");
            // 买方性别
            String MFXB = record.getStr("MFXB");
            // 买方生日
            Date MFSR = record.getDate("MFSR");
            // 卖方姓名
            String MAIFXM = record.getStr("MAIFXM");
            // 卖方身份证明号
            String MAIFSFZMH = record.getStr("MAIFSFZMH");
            // 卖方证件类型名称
            String MAIFZJLXMC = record.getStr("MAIFZJLXMC");
            // 卖方性别
            String MAIFXB = record.getStr("MAIFXB");
            // 卖方类型
            String MAIFLX = record.getStr("MAIFLX");
            // 卖方生日
            Date MAIFSR = record.getDate("MAIFSR");
            // 卖方住址
            String MAIFZZ = record.getStr("MAIFZZ");
            // 卖方联系电话
            String MAIFLXDH = record.getStr("MAIFLXDH");
            // 卖方间关系
            String MAIFJGX = record.getStr("MAIFJGX");
            // 卖方共有情况
            String MAIFGYQK = record.getStr("MAIFGYQK");
            // 卖方共有份额
            String MAIFGYFE = record.getStr("MAIFGYFE");
            // 卖方户籍所在地
            String MAIFHJSZD = record.getStr("MAIFHJSZD");
            String taskname = "";
            String taskcode = "";
            if (auditTask != null) {
                taskname = auditTask.getTaskname();
                taskcode = auditTask.getItem_id();
            }
            try {
                EpointFrameDsManager.begin(null);
                // 卖方类型
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
                // csfxxb.setSr(MAIFSR);
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
                // msfxxb.setSr(MFSR);
                msfxxb.setMfgx(MFJGX);
                // 添加记录
                iJnProjectService.insertJbxxb(jbxxb);
                iJnProjectService.insertCsfxxb(csfxxb);
                iJnProjectService.insertMsfxxb(msfxxb);

                EpointFrameDsManager.commit();

                addCallbackParam("msg", "信息推送成功！");
            }
            catch (Exception ex) {
                EpointFrameDsManager.rollback();
                ex.printStackTrace();
                return false;
            }
            finally {
                EpointFrameDsManager.close();
            }

            ycslData.put("success", true);
            addCallbackParam("zjxtData", ycslData.toJSONString());
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 人社局接口提交按钮，用作受理的操作按钮 auther:Lya 2020-12-14
     */
    public void acceptRsjProject() {
        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
            addCallbackParam("message", "该步骤已经处理完成，请勿重复操作！");
            addCallbackParam("close", "1");
            return;
        }
        // 如果办件的中心标识为空，重新set值
        if (StringUtil.isBlank(auditProject.getCenterguid())) {
            AuditProject a = new AuditProject();
            a.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            a.setRowguid(auditProject.getRowguid());
            auditProjectServcie.updateProject(a);
        }
        if (StringUtil.isNotBlank(auditProject.getBusinessguid())) {
            AuditSpBusiness bussiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditProject.getBusinessguid())
                    .getResult();
            if (bussiness != null && "1".equals(bussiness.getBusinesstype())) {
                AuditProject a = new AuditProject();
                a.setRowguid(auditProject.getRowguid());
                a.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
                a.setWindowname(ZwfwUserSession.getInstance().getWindowName());
                auditProjectServcie.updateProject(a);
            }
        }

        // 承诺件或正常流程的即办件
        // 新增上报件流程流转
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
                String msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                msg += "&handleSendToZjxt:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 受理
                    msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                            + ZwfwUserSession.getInstance().getAreaCode() + ".accept." + auditProject.getTask_id());
                }
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
                        msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                                + areaName + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                        ProducerMQ.sendByExchange("receiveproject", msg, "");
                    }
                }
                addCallbackParam("message", "refresh");

            }

            // 如果是中心办理的，那么就可以起流程
            else {
                boolean flag = isUserWordDoc();// 判断是否使用word
                EpointFrameDsManager.begin(null);
                HashMap<String, String> rtnValue = null;
                try {
                    rtnValue = new AuditWorkflowBizlogic().startProjectWorkflow(taskguid, projectguid,
                            auditProject.getApplyername(), UserSession.getInstance().getUserGuid(),
                            UserSession.getInstance().getDisplayName());

                    EpointFrameDsManager.commit();
                }
                catch (Exception e) {
                    EpointFrameDsManager.rollback();
                }
                finally {

                    EpointFrameDsManager.close();

                }

                if (rtnValue != null) {
                    if (StringUtil.isNotBlank(rtnValue.get("message"))) {
                        addCallbackParam("message", rtnValue.get("message"));
                    }
                    else {
                        addCallbackParam("workItemGuid", rtnValue.get("workItemGuid"));
                        addCallbackParam("operationGuid", rtnValue.get("operationGuid"));
                        addCallbackParam("transitionGuid", rtnValue.get("transitionGuid"));
                        addCallbackParam("pviGuid", rtnValue.get("pviGuid"));
                        addCallbackParam("isword", flag ? "1" : "0");
                    }
                }

                String docflag = "true";
                // 事项配置中配置是否不弹出文书
                String isNotOpenDoc = auditTaskExtension.getIs_notopendoc();
                if (StringUtil.isNotBlank(isNotOpenDoc)) {
                    String[] str = isNotOpenDoc.split(",");
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

        }
        else {
            // TODO
            EpointFrameDsManager.begin(null);
            // 即办件，直接更新状态待办结状态,并且发送消息
            if (auditTask != null) {
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && ZwfwUserSession.getInstance().getCitylevel().equals(ZwfwConstant.AREA_TYPE_XZJ)) {

                    AuditTaskDelegate delegate = auditTaskDelegateService.findByTaskIDAndAreacode(
                            auditTask.getTask_id(), ZwfwUserSession.getInstance().getAreaCode()).getResult();
                    if (delegate != null) {
                        if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                                && "是".equals(delegate.getUsecurrentinfo())) {
                            if (delegate.getPromise_day() != null) {
                                auditTask.setPromise_day(delegate.getPromise_day());
                            }
                        }
                    }
                }
            }
            auditTaskService.updateAuditTask(auditTask);
            String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
            String areaStr = "";
            if (StringUtil.isBlank(auditProject.getHandleareacode())) {
                areaStr = handleAreaCode + ",";
            }
            else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
                areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
            }
            if (StringUtil.isNotBlank(areaStr)) {
                auditProject.setHandleareacode(areaStr);
            }
            handleProjectService.handleAcceptArea(auditProject, workItemGuid,
                    UserSession.getInstance().getDisplayName(), UserSession.getInstance().getUserGuid(),
                    ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid(),
                    ZwfwUserSession.getInstance().getAreaCode());
            EpointFrameDsManager.commit();
            // 非网上申报需要上报办件数据
            /*
             * if (auditProject.getApplyway() !=
             * Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS) &&
             * auditProject.getApplyway() !=
             * Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {}
             */
            String msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
            // 测试办件不生成
            /*
             * if (auditProject.getIs_test() !=
             * Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
             * ProducerMQ.sendByExchange("receiveproject", msg, ""); // 接办分离 受理
             * msg =
             * auditProject.getRowguid() + "." + auditProject.getAreacode();
             * sendMQMessageService.sendByExchange("exchange_handle", msg,
             * "project." +
             * ZwfwUserSession.getInstance().getAreaCode() + ".accept." +
             * auditProject.getTask_id()); }
             */
            AuditOrgaArea auditOrgaArea = auditOrgaAreaService
                    .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
            String areaName = "";
            if (auditOrgaArea != null) {
                areaName = auditOrgaArea.getXiaquname();
            }
            // 测试办件不生成
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                // 江苏省标
                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    // 江苏省省标基本信息
                    msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                            + areaName + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 江苏省省标流程信息
                    msg = "handleJBJProcess:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                            + ZwfwConstant.OPERATE_SL + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                }
            }
            handleProjectService.handleApprove(auditProject, UserSession.getInstance().getDisplayName(),
                    UserSession.getInstance().getUserGuid(), workItemGuid, null);

            addCallbackParam("message", "refresh");

            boolean docflag = true;
            // 事项配置中配置是否不弹出文书
            String isNotOpenDoc = auditTaskExtension.getIs_notopendoc();
            if (StringUtil.isNotBlank(isNotOpenDoc)) {
                String[] str = isNotOpenDoc.split(",");
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
                    addCallbackParam("address", address);
                    addCallbackParam("isword", isword ? "1" : "0");
                }
            }
            addCallbackParam("docflag", docflag);
        }
        // 使用物流
        if (auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
            /*
             * String prov = iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_prov()); if
             * (StringUtil.isNotBlank(prov)) {
             * auditLogisticsBasicinfo.setRcv_prov(prov); } String city =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_city()); if
             * (StringUtil.isNotBlank(city)) {
             * auditLogisticsBasicinfo.setRcv_city(city); } String country =
             * iCodeItemsService.getItemTextByCodeName("行政区划国标",
             * auditLogisticsBasicinfo.getRcv_country()); if
             * (StringUtil.isNotBlank(country)) {
             * auditLogisticsBasicinfo.setRcv_country(country); }
             */
            auditLogisticsBasicinfo.setStatus("0");
            auditLogisticsBasicinfo.setCallState("05");// 初始化状态
            auditLogisticsBasicinfo.setNet_type("2");
            auditLogisticsBasicinfo.setWindowguid(ZwfwUserSession.getInstance().getWindowGuid());
            auditLogisticsBasicinfo.setFlowsn(auditProject.getFlowsn());
            auditLogisticsBasicinfo.setTaskname(auditProject.getProjectname());
            if (StringUtil.isBlank(auditLogisticsBasicinfo.getProjectguid())) {
                handleProjectService.handleLogistics(auditProject, auditLogisticsBasicinfo);
            }
            else {
                handleProjectService.updateLogistics(auditProject, auditLogisticsBasicinfo);
            }
            handleProjectService.saveProject(auditProject);
        }
        // 如果存在办理事宜，则删除办理
        IBusinessLicenseExtension businessLicenseExtension = ContainerFactory.getContainInfo()
                .getComponent(IBusinessLicenseExtension.class);
        List<Record> userGuidList = businessLicenseExtension
                .getMessageCenterUserGuidListByProjectGuid(auditProject.getRowguid());
        if (StringUtil.isNotBlank(userGuidList)) {
            for (Record userGuid : userGuidList) {
                List<MessagesCenter> messagesCenterDealList = messageCenterService.queryForList(
                        userGuid.getStr("targetuser"), null, null, "", "办理", auditProject.getRowguid(), "", -1, "",
                        null, null, 0, -1);
                if (messagesCenterDealList != null && !messagesCenterDealList.isEmpty()) {
                    for (MessagesCenter messagescenter : messagesCenterDealList) {
                        messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                                messagescenter.getTargetUser());
                    }
                }
            }
        }
        // 如果存在待办事宜，则删除待办
        List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(userSession.getUserGuid(), null,
                null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                -1);
        if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
            }
        }
        // 此处点击受理后，不会给窗口发送办件，因为受理完就要将申报流水号提送给浪潮方，不需要窗口人员
        String flowsn = auditProject.getFlowsn(); // 获取要推送的办件的申报流水号
        JSONObject paramObject = new JSONObject();
        String logs = "";
        if (StringUtil.isNotBlank(flowsn)) {
            paramObject.put("flowsn", flowsn);
            logs = TARequestUtil.sendPost(configServicce.getFrameConfigValue("rsjSendFlowsnUrl"),
                    paramObject.toString(), "", "");
            auditProject.setStatus(1);
            auditProject.setAcceptuserguid(userSession.getUserGuid());
            auditProjectServcie.updateProject(auditProject);
        }

        if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {

            if ("370829".equals(auditProject.getAreacode()) || "370811".equals(auditProject.getAreacode())
                    || "370882".equals(auditProject.getAreacode()) || "370881".equals(auditProject.getAreacode())
                    || "370827".equals(auditProject.getAreacode()) || "370892".equals(auditProject.getAreacode())
                    || "370830".equals(auditProject.getAreacode()) || "370831".equals(auditProject.getAreacode())
                    || "370891".equals(auditProject.getAreacode()) || "370890".equals(auditProject.getAreacode())
                    || "370832".equals(auditProject.getAreacode()) || "370826".equals(auditProject.getAreacode())
                    || "370800".equals(auditProject.getAreacode()) || "370828".equals(auditProject.getAreacode())
                    || "370883".equals(auditProject.getAreacode())) {
                evaluate("2");
            }
            else {
                if (!evaluateservice.isExistEvaluate(projectguid).getResult()) {
                    evaluate("");
                }
            }

            // 推送好差评办件服务数据
            turnhcpevaluate(auditProject, 1, "提交申请信息",
                    EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));

        }

    }

    /**
     * 变更历史证照is_history为1 [一句话功能简述]
     *
     * @param certrowguid
     */
    private void updateCertInfoToHistory(String certrowguid) {
        if (StringUtil.isNotBlank(certrowguid)) {
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certrowguid);
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            String zsbh = "";
            if (certinfoExtension != null) {
                zsbh = certinfoExtension.getStr("zsbh"); // 证书编号
            }
            if (StringUtil.isNotBlank(zsbh)) {
                Map<String, Object> filter2 = new HashMap<>();
                // 设置基本信息guid
                filter2.put("zsbh", zsbh);
                List<CertInfoExtension> findList = getMongodbUtil().findList(CertInfoExtension.class, filter2, null,
                        false);
                List<String> certguidList = new ArrayList<>();
                if (findList != null && findList.size() > 0) {
                    for (CertInfoExtension certInfoExtension2 : findList) {
                        if (!certrowguid.equals(certInfoExtension2.getCertinfoguid())) {
                            certguidList.add(certInfoExtension2.getCertinfoguid());
                        }
                    }
                }
                if (findList != null && findList.size() > 0) {
                    for (String certguid : certguidList) {
                        CertInfo cert = iCertInfo.getCertInfoByRowguid(certguid);
                        if (cert != null) {
                            cert.setIshistory(ZwfwConstant.CONSTANT_INT_ONE);
                            iCertInfo.updateCertInfo(cert);
                        }
                    }
                }
            }
        }

    }

    /**
     * 下列事项推送数据前置库ex_sjzyqyzzz_1 [一句话功能简述]
     */
    public void pushDataToSjzyqyzzz() {
        // 如果为建筑业企业资质（告知承诺方式）新申请，推送数据至前置库 11370800MB285591843370117100122
        if (auditProject != null && ("11370800MB285591843370117100121".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100122".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100123".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100124".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100125".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100126".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100127".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100128".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100131".equals(auditTask.getItem_id())
                || "建筑业企业资质许可（延续）".equals(auditTask.getTaskname())
                || "11370800MB285591843370117100133".equals(auditTask.getItem_id())
                || "建筑业企业资质（首次申请）".equals(auditTask.getTaskname()) || "建筑业企业资质许可（增项）".equals(auditTask.getTaskname())
                || "11370800MB285591843370117100136".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100137".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100138".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100139".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100140".equals(auditTask.getItem_id())
                || "11370800MB285591843370117100141".equals(auditTask.getItem_id())
                || "11370800MB285591843370199013000".equals(auditTask.getItem_id())
                || "1137080000431212413370117049000".equals(auditTask.getItem_id())
                || "1137080000431212413370117100123".equals(auditTask.getItem_id())
                || "1137080000431212413370117100136".equals(auditTask.getItem_id())
                || "1137080000431212413370117100137".equals(auditTask.getItem_id())
                || "1137080000431212413370117100138".equals(auditTask.getItem_id())
                || "1137080000431212413370117100139".equals(auditTask.getItem_id())
                || "1137080000431212413370117100140".equals(auditTask.getItem_id())
                || "1137080000431212413370117100141".equals(auditTask.getItem_id()))) {

            Record record = null;
            // 下列事项所取电子表单区分
            if ("1137080000431212413370117100123".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100136".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100137".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100138".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100139".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100140".equals(auditTask.getItem_id())
                    || "1137080000431212413370117100141".equals(auditTask.getItem_id())
                    || "1137080000431212413370117049000".equals(auditTask.getItem_id())) {
                record = iCxBusService.getDzbdDetail("formtable20221109165008", auditProject.getRowguid());
            }
            else if ("建筑业企业资质许可（延续）".equals(auditTask.getTaskname())
                    || "建筑业企业资质（首次申请）".equals(auditTask.getTaskname())) {
                record = iCxBusService.getDzbdDetail("formtable20230619094541", auditProject.getRowguid());
            }
            else if ("11370800MB285591843370117100133".equals(auditTask.getItem_id())) {
                record = iCxBusService.getDzbdDetail("formtable20220622114015", auditProject.getRowguid());
            }
            else if ("建筑业企业资质许可（增项）".equals(auditTask.getTaskname())) {
                record = iCxBusService.getDzbdDetail("formtable20220509094032", auditProject.getRowguid());
            }
            else {
                record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());

            }
            if (record != null) {
                String PERMIT_CONTENT = "";
                String XIANGXIDIZHI = "";
                String ZIZHILEIBIEJIDENGJI = "";
                String FADINGDAIBIAOREN = "";

                if ("建筑业企业资质许可（延续）".equals(auditTask.getTaskname())
                        || "11370800MB285591843370117100133".equals(auditTask.getItem_id())
                        || "建筑业企业资质（首次申请）".equals(auditTask.getTaskname())) {
                    PERMIT_CONTENT = record.getStr("yyejzz");
                    XIANGXIDIZHI = record.getStr("qydz");
                    ZIZHILEIBIEJIDENGJI = record.getStr("yyejzz");
                    FADINGDAIBIAOREN = record.getStr("fddbr");
                }
                else if ("建筑业企业资质许可（增项）".equals(auditTask.getTaskname())) {
                    PERMIT_CONTENT = record.getStr("yyzz");
                    XIANGXIDIZHI = record.getStr("qydz");
                    ZIZHILEIBIEJIDENGJI = record.getStr("yyzz");
                    FADINGDAIBIAOREN = record.getStr("fddbr");
                }
                else {
                    PERMIT_CONTENT = record.getStr("zzlbjdj");
                    XIANGXIDIZHI = record.getStr("xxdz");
                    ZIZHILEIBIEJIDENGJI = record.getStr("zzlbjdj");
                    FADINGDAIBIAOREN = record.getStr("frdb");
                }

                Record ylgg = new Record();
                ylgg.setSql_TableName("ex_sjzyqyzzz_1");
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", PERMIT_CONTENT);

                ylgg.set("CERTIFICATE_LEVEL", "A");
                ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                ylgg.set("XIANGXIDIZHI", XIANGXIDIZHI);
                ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                ylgg.set("ZIZHILEIBIEJIDENGJI", ZIZHILEIBIEJIDENGJI);
                ylgg.set("BEIZHU", "");
                /*
                 * if ("1137080000431212413370117049000".equals(auditTask.
                 * getItem_id())) {
                 * ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa",
                 * "113708000043121241"); } else
                 * { ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa",
                 * "11370800MB28559184"); }
                 */

                ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                ylgg.set("FADINGDAIBIAOREN", FADINGDAIBIAOREN);

                ylgg.set("operatedate", new Date());
                CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
                if (certinfo != null) {
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certinfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    if (certinfoExtension == null) {
                        certinfoExtension = new CertInfoExtension();
                    }
                    // 证照编号
                    ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                    String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                    String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                    // 发证日期
                    ylgg.set("CERTIFICATE_DATE", rq);
                    // 有效期开始
                    ylgg.set("VALID_PERIOD_BEGIN", rq);
                    // 有效期截止
                    ylgg.set("VALID_PERIOD_END", yxq);
                    ylgg.set("YOUXIAOQI", yxq);

                }
                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照颁发单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    // 颁发机构统一社会信用代码
                    ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");
                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjzyqyzzz_1",
                        ylgg.getStr("LICENSE_NUMBER"));

                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }
    }

    public void pushDataToSjzyqyzzzTwo() {
        // 建筑业企业资质许可（遗失补办），推送数据至前置库 11370800MB285591843370117100130
        if (auditProject != null && "11370800MB285591843370117100130".equals(auditTask.getItem_id())) {
            Record record = iCxBusService.getDzbdDetail("formtable20220402152431", auditProject.getRowguid());
            if (record != null) {
                String XIANGXIDIZHI = record.getStr("xxdz");
                String FADINGDAIBIAOREN = record.getStr("frdb");
                // 说明双证照
                if (auditProject.getCertrowguid().contains(";")) {
                    String[] certrowguids = auditProject.getCertrowguid().split(";");
                    for (String certrowguid : certrowguids) {
                        Record ylgg = new Record();
                        ylgg.setSql_TableName("ex_sjzyqyzzz_1");
                        ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                        ylgg.set("CERTIFICATE_LEVEL", "A");
                        ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                        ylgg.set("XIANGXIDIZHI", XIANGXIDIZHI);
                        ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                        ylgg.set("BEIZHU", "");
                        ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800MB28559184");
                        ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                        String areacode = ZwfwUserSession.getInstance().getAreaCode();
                        if (areacode.length() == 6) {
                            areacode = areacode + "000000";
                        }
                        else if (areacode.length() == 9) {
                            areacode = areacode + "000";
                        }
                        ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                        ylgg.set("FADINGDAIBIAOREN", FADINGDAIBIAOREN);
                        ylgg.set("operatedate", new Date());
                        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
                        if (certinfo != null) {
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", certinfo.getRowguid());
                            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                    false);
                            if (certinfoExtension == null) {
                                certinfoExtension = new CertInfoExtension();
                            }
                            // 证照编号
                            ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                            String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"),
                                    "yyyy-MM-dd");
                            String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"),
                                    "yyyy-MM-dd");
                            // 发证日期
                            ylgg.set("CERTIFICATE_DATE", rq);
                            // 有效期开始
                            ylgg.set("VALID_PERIOD_BEGIN", rq);
                            // 有效期截止
                            ylgg.set("VALID_PERIOD_END", yxq);
                            ylgg.set("YOUXIAOQI", yxq);

                            String zzlbjdj = certinfoExtension.getStr("zzlbjdj");
                            ylgg.set("PERMIT_CONTENT", zzlbjdj);
                            ylgg.set("ZIZHILEIBIEJIDENGJI", zzlbjdj);
                        }
                        int APPLYERTYPE = auditProject.getApplyertype();
                        if (StringUtil.isNotBlank(APPLYERTYPE)) {
                            ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                            if ("20".equals(APPLYERTYPE + "")) {
                                ylgg.set("HOLDER_TYPE", "自然人");
                            }
                        }
                        else {
                            ylgg.set("HOLDER_TYPE", "--");
                        }
                        ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                        ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                        ylgg.set("CERTIFICATE_TYPE",
                                codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                        // 联系方式
                        ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                        // 证照颁发单位
                        ylgg.set("DEPT_NAME", userSession.getOuName());

                        FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                        if (frameOuExten != null) {
                            // 证照办法单位组织机构代码
                            ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                        }
                        // 行政区划名称
                        ylgg.set("DISTRICTS_NAME",
                                areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult()
                                        .getXiaquname());
                        // 行政区划编码
                        ylgg.set("DISTRICTS_CODE", areacode);

                        ylgg.set("rowguid", UUID.randomUUID().toString());
                        ylgg.setPrimaryKeys("rowguid");
                        Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjzyqyzzz_1",
                                ylgg.getStr("LICENSE_NUMBER"));

                        if (ylggrecord != null) {
                            ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                            ylgg.set("state", "update");
                            iJnProjectService.UpdateRecord(ylgg);
                        }
                        else {
                            ylgg.set("state", "insert");
                            iJnProjectService.inserRecord(ylgg);
                        }
                    }
                }
                else {
                    // 说明还是单证照
                    Record ylgg = new Record();
                    ylgg.setSql_TableName("ex_sjzyqyzzz_1");
                    ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                    ylgg.set("CERTIFICATE_LEVEL", "A");
                    ylgg.set("QIYEMINGCHEN", record.getStr("qymc"));
                    ylgg.set("XIANGXIDIZHI", XIANGXIDIZHI);
                    ylgg.set("TONGYISHEHUIXINYONGDAIMA", record.getStr("tyxydm"));
                    ylgg.set("BEIZHU", "");
                    ylgg.set("BanFaJiGouTongYiSheHuiXinYongDaiMa", "11370800MB28559184");
                    ylgg.set("JINGJIXINGZHI", record.getStr("jjxz"));
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if (areacode.length() == 6) {
                        areacode = areacode + "000000";
                    }
                    else if (areacode.length() == 9) {
                        areacode = areacode + "000";
                    }
                    ylgg.set("XINGZHENGQUHUADAIMA", areacode);
                    ylgg.set("FADINGDAIBIAOREN", FADINGDAIBIAOREN);
                    ylgg.set("operatedate", new Date());
                    CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.getCertrowguid());
                    if (certinfo != null) {
                        Map<String, Object> filter = new HashMap<>();
                        // 设置基本信息guid
                        filter.put("certinfoguid", certinfo.getRowguid());
                        CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter,
                                false);
                        if (certinfoExtension == null) {
                            certinfoExtension = new CertInfoExtension();
                        }
                        // 证照编号
                        ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("zsbh"));
                        String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                        String yxq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxq"), "yyyy-MM-dd");
                        // 发证日期
                        ylgg.set("CERTIFICATE_DATE", rq);
                        // 有效期开始
                        ylgg.set("VALID_PERIOD_BEGIN", rq);
                        // 有效期截止
                        ylgg.set("VALID_PERIOD_END", yxq);
                        ylgg.set("YOUXIAOQI", yxq);

                        String zzlbjdj = certinfoExtension.getStr("zzlbjdj");
                        ylgg.set("PERMIT_CONTENT", zzlbjdj);
                        ylgg.set("ZIZHILEIBIEJIDENGJI", zzlbjdj);
                    }
                    int APPLYERTYPE = auditProject.getApplyertype();
                    if (StringUtil.isNotBlank(APPLYERTYPE)) {
                        ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                        if ("20".equals(APPLYERTYPE + "")) {
                            ylgg.set("HOLDER_TYPE", "自然人");
                        }
                    }
                    else {
                        ylgg.set("HOLDER_TYPE", "--");
                    }
                    ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                    ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                    ylgg.set("CERTIFICATE_TYPE",
                            codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                    // 联系方式
                    ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                    // 证照颁发单位
                    ylgg.set("DEPT_NAME", userSession.getOuName());

                    FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                    if (frameOuExten != null) {
                        // 证照办法单位组织机构代码
                        ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                    }
                    // 行政区划名称
                    ylgg.set("DISTRICTS_NAME", areaService
                            .getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult().getXiaquname());
                    // 行政区划编码
                    ylgg.set("DISTRICTS_CODE", areacode);

                    ylgg.set("rowguid", UUID.randomUUID().toString());
                    ylgg.setPrimaryKeys("rowguid");
                    Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjzyqyzzz_1",
                            ylgg.getStr("LICENSE_NUMBER"));

                    if (ylggrecord != null) {
                        ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                        ylgg.set("state", "update");
                        iJnProjectService.UpdateRecord(ylgg);
                    }
                    else {
                        ylgg.set("state", "insert");
                        iJnProjectService.inserRecord(ylgg);
                    }
                }
            }
        }
    }

    public void sendCertInfoToQzk(AuditProject project) {
        CertInfoExtension certinfoExtension = null;
        CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(project.get("certrowguid"));
        if (certinfo != null) {
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
        }
        else {
            certinfo = new CertInfo();
        }

        if (certinfoExtension == null) {
            certinfoExtension = new CertInfoExtension();
        }

        Record record = new Record();
        record.set("Rowguid", UUID.randomUUID().toString());
        record.set("LICENSE_NUMBER", certinfoExtension.getStr("zzbh"));
        record.set("CERTIFICATE_DATE", new Date());
        record.set("VALID_PERIOD_BEGIN", certinfo.getAwarddate()); //
        record.set("VALID_PERIOD_END", EpointDateUtil.convertString2Date("9999-12-30 23:59:59"));
        record.set("DEPT_ORGANIZE_CODE", "11370800MB28559184");
        record.set("DEPT_NAME", "济宁市行政审批服务局");
        record.set("DISTRICTS_CODE", "370800000000");
        record.set("DISTRICTS_NAME", "济宁市");
        record.set("HOLDER_TYPE", "法人");
        record.set("HOLDER_NAME", project.getApplyername());
        record.set("CERTIFICATE_TYPE", "统一信用代码");
        record.set("CERTIFICATE_NO", project.getCertnum());
        record.set("CONTACT_PHONE", project.getContactmobile());
        record.set("PROJECT_NAME", project.getProjectname());
        record.set("STATE", "insert新增");
        record.set("PERMIT_CONTENT", "");
        record.set("CERTIFICATE_LEVEL", "A");
        record.set("GUANYUXX", certinfoExtension.getStr("xmmc")); //
        record.set("DANWEIMINGCHEN", certinfoExtension.getStr("jsgs")); //
        record.set("BAOSONGBAOGAO", certinfoExtension.getStr("bsfhpjbg")); //
        record.set("DUIXXSHENCHA", certinfoExtension.getStr("scfhpjbg")); //
        record.set("FUJIANMINGCHEN", certinfoExtension.getStr("fjfhpjbg")); //
        record.set("OperateDate", new Date());
        YQCertInfoUtil.insertInfo(record);

    }

    public AuditProject getAuditProject() {
        return auditProject;
    }

    public void setAuditProject(AuditProject auditProject) {
        this.auditProject = auditProject;
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

    public AuditLogisticsBasicinfo getAuditLogisticsBasicinfo() {
        return auditLogisticsBasicinfo;
    }

    public void setAuditLogisticsBasicinfo(AuditLogisticsBasicinfo auditLogisticsBasicinfo) {
        this.auditLogisticsBasicinfo = auditLogisticsBasicinfo;
    }

    public AuditProjectFormSgxkz getAuditProjectFormSgxkz() {
        return auditProjectFormSgxkz;
    }

    public void setAuditProjectFormSgxkz(AuditProjectFormSgxkz auditProjectFormSgxkz) {
        this.auditProjectFormSgxkz = auditProjectFormSgxkz;
    }

    public AuditProjectSamecity getAuditProjectSamecity() {
        return auditProjectSamecity;
    }

    public void setAuditProjectSamecity(AuditProjectSamecity auditProjectSamecity) {
        this.auditProjectSamecity = auditProjectSamecity;
    }

    public AuditProjectFormJgxk getAuditProjectFormJgxk() {
        return auditProjectFormJgxk;
    }

    public void setAuditProjectFormJgxk(AuditProjectFormJgxk auditProjectFormJgxk) {
        this.auditProjectFormJgxk = auditProjectFormJgxk;
    }

    /**
     * 大数据局推送排污或施工许可事项
     *
     * @param auditProject
     * @param auditTask
     */
    public void PwOrSgxkzProject(AuditProject auditProject, AuditTask auditTask) {
        // 济宁施工许可证
        if (auditProject != null && ("11370800MB28559184300011710900202".equals(auditTask.getItem_id())
                || "11370827MB2857833K4370117045000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4370117045000".equals(auditTask.getItem_id())
                || "11370832MB2855272B4370117045000".equals(auditTask.getItem_id()))) {

            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_JZGCSGXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("HETONGJUNGONGRIQI"),
                        "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("JIANSHEDIZHI", certinfoExtension.getStr("xiangmudidian"));
                ylgg.set("SHEJIDANWEI", certinfoExtension.getStr("shejidanwei"));
                ylgg.set("HETONGJUNGONGRIQI", certinfoExtension.getStr("hetongjungongriqi"));
                ylgg.set("JIANSHEDANWEI", certinfoExtension.getStr("jianshedanweimingchen"));
                ylgg.set("GONGCHENGMINGCHENG", certinfoExtension.getStr("xiangmumingchen"));
                ylgg.set("HETONGKAIGONGRIQI", certinfoExtension.getStr("hetongkaigongriqi"));
                ylgg.set("JIANSHEGUIMO", certinfoExtension.getStr("jiansheguimo"));
                ylgg.set("SHIGONGDANWEI", certinfoExtension.getStr("shigongzongchengbaoqiye"));
                ylgg.set("JIANLIDANWEI", certinfoExtension.getStr("jianlidanwei"));
                ylgg.set("BEIZHU", certinfoExtension.getStr("bz"));
                ylgg.set("HETONGJIAGE", certinfoExtension.getStr("hetongjiagewanyuan"));
                ylgg.set("KANCHADANWEI", certinfoExtension.getStr("kanchadanwei"));
                ylgg.set("JIANSHEDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("jianshedanweixiangmufuzeren"));
                ylgg.set("SHIGONGDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("shigongzongchengbaoqiyexiangmu"));
                ylgg.set("SHEJIDANWEIXIANGMUFUZEREN", certinfoExtension.getStr("shejidanweixiangmufuzeren"));
                ylgg.set("KANCHADANWEIXIANGMUFUZEREN", certinfoExtension.getStr("kanchadanweixiangmufuzeren"));
                ylgg.set("ZONGJIANLIGONGCHENGSHI", certinfoExtension.getStr("zongjianligongchengshi"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_JZGCSGXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

        // 济宁排污
        if (auditProject != null && ("11370800MB28559184737011702000005".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702000004".equals(auditTask.getItem_id())
                || "11370800MB28559184300011712300201".equals(auditTask.getItem_id())
                || "11370800MB28559184300011712300203".equals(auditTask.getItem_id())
                || "11370800MB28559184737011702000001".equals(auditTask.getItem_id())
                || "11370800MB28559184300011712300201".equals(auditTask.getItem_id())
                || "11370800MB28559184300011712300203".equals(auditTask.getItem_id())
                || "11370800MB28559184300011712300202".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_WSPRPSGWXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("xkzbh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");
                String yxqz = EpointDateUtil.convertDate2String(certinfoExtension.getDate("yxqz"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", yxqz);

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("SFLRZDPWDWML", certinfoExtension.getStr("sflrzdpwdwml"));
                ylgg.set("PAISHUIHULEIXING", certinfoExtension.getStr("pshlx"));
                ylgg.set("PAISHUIHUMINGCHENG", certinfoExtension.getStr("pshmc"));
                ylgg.set("BEIZHU", certinfoExtension.getStr("bz"));
                ylgg.set("YINGYEZHIZHAOZHUCEHAO", certinfoExtension.getStr("tyshxydm"));
                ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                ylgg.set("XIANGXIDIZHI", certinfoExtension.getStr("xxdz"));

                String pwxkxx = "";

                if (StringUtil.isNotBlank(certinfoExtension.getStr("pwskbh"))) {
                    pwxkxx += certinfoExtension.getStr("pwskbh") + "," + certinfoExtension.getStr("psqx") + ","
                            + certinfoExtension.getStr("psl") + "," + certinfoExtension.getStr("wszzqx") + ";";
                }
                if (StringUtil.isNotBlank(certinfoExtension.getStr("paiwskbh"))) {
                    pwxkxx += certinfoExtension.getStr("paiwskbh") + "," + certinfoExtension.getStr("paisqx") + ","
                            + certinfoExtension.getStr("paisl") + "," + certinfoExtension.getStr("wuszzqx") + ";";
                }
                if (StringUtil.isNotBlank(certinfoExtension.getStr("paiwuskbh"))) {
                    pwxkxx += certinfoExtension.getStr("paiwuskbh") + "," + certinfoExtension.getStr("paishuiqx") + ","
                            + certinfoExtension.getStr("pshuil") + "," + certinfoExtension.getStr("wshuizzqx") + ";";
                }
                if (StringUtil.isNotBlank(certinfoExtension.getStr("paiwushuikbh"))) {
                    pwxkxx += certinfoExtension.getStr("paiwushuikbh") + "," + certinfoExtension.getStr("paishueiqux")
                            + "," + certinfoExtension.getStr("psliang") + "," + certinfoExtension.getStr("wszhzqx")
                            + ";";
                }
                if (StringUtil.isNotBlank(certinfoExtension.getStr("paiwushuikobh"))) {
                    pwxkxx += certinfoExtension.getStr("paiwushuikobh") + "," + certinfoExtension.getStr("pshuiquxiang")
                            + "," + certinfoExtension.getStr("paisliang") + "," + certinfoExtension.getStr("wszzqux")
                            + ";";
                }
                if (StringUtil.isNotBlank(certinfoExtension.getStr("pwushuikbh"))) {
                    pwxkxx += certinfoExtension.getStr("pwushuikbh") + "," + certinfoExtension.getStr("paishuiquxiang")
                            + "," + certinfoExtension.getStr("pshuiliang") + ","
                            + certinfoExtension.getStr("wszzqxiang") + ";";
                }

                ylgg.set("PAIWUXIANGQINGBIAO", pwxkxx);

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_WSPRPSGWXKZ_1",
                        ylgg.getStr("LICENSE_NUMBER"));
                if (ylggrecord != null) {
                    ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                    ylgg.set("state", "update");
                    iJnProjectService.UpdateRecord(ylgg);
                }
                else {
                    ylgg.set("state", "insert");
                    iJnProjectService.inserRecord(ylgg);
                }
            }
        }

    }

    // 根据是否有勘验记录判断打开的勘验页面
    public void openWindowByIsKy() {
        boolean checkIsky = iJnProjectService.checkIsky(projectguid);
        if (checkIsky) {
            addCallbackParam("isky", "1");
        }
        else {
            addCallbackParam("isky", "0");
        }
    }

    /**
     * 弹出是否填写快递单号提示
     */
    public void prompt() {
        /*
         * AuditProjectDelivery delivery =
         * deliveryService.getDeliveryByProjectguid(projectguid);
         * if (auditProject != null &&
         * StringUtils.isNotBlank(auditProject.getIf_express()) && delivery !=
         * null
         * && "1".equals(delivery.getIssendback())) {
         * // 说明要回寄结果，填写单号
         * if (StringUtil.isBlank(delivery.getStr("trackingnumber"))) {
         * addCallbackParam("success", "0");
         * addCallbackParam("message", "请填写快递单号！");
         * }
         * else {
         * addCallbackParam("success", "1");
         * }
         * }
         * else {
         * addCallbackParam("success", "1");
         * }
         */

        addCallbackParam("success", "1");
    }

    public void IsPushCertUpdate() {
        if (auditTask != null && ("11370800MB285591843370117002017".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711200005".equals(auditTask.getItem_id()))) {
            addCallbackParam("success", "1");
        }
        else {
            addCallbackParam("success", "0");
        }
    }

    /**
     * 弹出安许证注销接口是否调用成功，如果不成功，不允许办结
     *
     * @return
     */
    public void CertUpdate() {
        String message = PushUtil.getCertUpdate(auditProject.getRowguid());
        // 说明推送成功
        if ("1".equals(message)) {
            addCallbackParam("success", "1");
        }
        else {
            addCallbackParam("success", "0");
            addCallbackParam("message", "安许证变更接口推送失败！");
        }
    }

    /**
     * 济宁人防工程施工图设计文件核准
     */
    private void pushrenfangsgtsjwjhz(){
        // 济宁人防工程施工图设计文件核准
        if (auditProject != null && ("11370800MB285591847370117046000".equals(auditTask.getItem_id())
                || "11370832MB2855272B4370117046000".equals(auditTask.getItem_id())
                || "11370827MB2857833K4370117046000".equals(auditTask.getItem_id())
                || "11370826MB2858051T4370117046000".equals(auditTask.getItem_id()))
                || "11370800MB28559184300011711700201".equals(auditTask.getItem_id())
                || "11370800MB28559184300011711700202".equals(auditTask.getItem_id())) {
            Record ylgg = new Record();
            ylgg.setSql_TableName("EX_SPFYXSXKZ_1");
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            if (certinfo != null) {
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certinfo.getRowguid());
                CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfoExtension.getStr("bh"));
                String rq = EpointDateUtil.convertDate2String(certinfoExtension.getDate("fzrq"), "yyyy-MM-dd");

                // 发证日期x
                ylgg.set("CERTIFICATE_DATE", rq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", rq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", "9999-12-31");

                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                ylgg.set("PERMIT_CONTENT", "--");
                ylgg.set("CERTIFICATE_LEVEL", "A");

                ylgg.set("YXSFWJGZH", certinfoExtension.getStr("yxsjgzh"));
                ylgg.set("YUXIAOSHOUFANGWUJIBENXINXI", certinfoExtension.getStr("yxsfwjbxx"));
                ylgg.set("YXSFWJGYH", certinfoExtension.getStr("yxsfwjgyh"));
                ylgg.set("XIAOQUZUTUANMINGCHEN", certinfoExtension.getStr("xqmc"));
                ylgg.set("YXSFWXMDZ", certinfoExtension.getStr("yxsfwdz"));
                ylgg.set("XIAOSHOUFANGWULEIBIE", certinfoExtension.getStr("xsfwlb"));
                ylgg.set("FANGWUYUXIAOSHOUDANWEI", certinfoExtension.getStr("fwxsdw"));

                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }

                ylgg.set("operatedate", new Date());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());

                // 证照颁发单位
                ylgg.set("DEPT_NAME", userSession.getOuName());

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(userSession.getOuGuid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 行政区划名称
                ylgg.set("DISTRICTS_NAME", areaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode())
                        .getResult().getXiaquname());
                // 行政区划编码
                ylgg.set("DISTRICTS_CODE", areacode);

                ylgg.set("rowguid", UUID.randomUUID().toString());
                ylgg.setPrimaryKeys("rowguid");

                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }
    }

    /**
     * 推送相关X射线数据
     */
    private void pushXshexian() {
        // 设置X射线影像诊断建设项目放射性职业病危害预评价报告审核 && 设置X射线影像诊断的建设项目放射性职业病防护设施竣工验收
        if (auditProject != null && ("11370830F5034613XX400012311200301".equals(auditTask.getItem_id())
                || "11370830F5034613XX400012311300301".equals(auditTask.getItem_id()))) {
            Record ylgg = new Record();
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.get("certrowguid"));
            Record ylggrecord = null;
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", certinfo.getRowguid());
            CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
            if (certinfoExtension == null) {
                certinfoExtension = new CertInfoExtension();
            }
            // 通用信息
            {
                // 证照编号
                ylgg.set("LICENSE_NUMBER", certinfo.getCertno());
                String fzrq = EpointDateUtil.convertDate2String(auditProject.getCertificatedate(), "yyyy-MM-dd");
                String sxrq = EpointDateUtil.convertDate2String(certinfo.getExpiredatefrom(), "yyyy-MM-dd");
                String shixrq = EpointDateUtil.convertDate2String(certinfo.getExpiredateto(), "yyyy-MM-dd");

                // 发证日期
                ylgg.set("CERTIFICATE_DATE", fzrq);
                // 有效期开始
                ylgg.set("VALID_PERIOD_BEGIN", sxrq);
                // 有效期截止
                ylgg.set("VALID_PERIOD_END", shixrq);

                FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(auditProject.getOuguid());
                if (frameOuExten != null) {
                    // 证照办法单位组织机构代码
                    ylgg.set("DEPT_ORGANIZE_CODE", frameOuExten.getStr("orgcode"));
                }
                // 证照颁发单位
                ylgg.set("DEPT_NAME", auditProject.getOuname());

                // 行政区划编码
                String areacode = auditProject.getAreacode();
                if (areacode.length() == 6) {
                    areacode = areacode + "000000";
                }
                else if (areacode.length() == 9) {
                    areacode = areacode + "000";
                }
                ylgg.set("DISTRICTS_CODE", areacode);

                // 行政区划名称
                ylgg.set("DISTRICTS_NAME",
                        areaService.getAreaByAreacode(auditProject.getAreacode()).getResult().getXiaquname());

                int APPLYERTYPE = auditProject.getApplyertype();
                if (StringUtil.isNotBlank(APPLYERTYPE)) {
                    ylgg.set("HOLDER_TYPE", codeItemsService.getItemTextByCodeName("申请人类型", APPLYERTYPE + ""));
                    if ("20".equals(APPLYERTYPE + "")) {
                        ylgg.set("HOLDER_TYPE", "自然人");
                    }
                }
                else {
                    ylgg.set("HOLDER_TYPE", "--");
                }
                // 持证者名称
                ylgg.set("HOLDER_NAME", auditProject.getApplyername());
                // 持有者证件类型
                ylgg.set("CERTIFICATE_TYPE",
                        codeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype()));
                // 持有者证件编号
                ylgg.set("CERTIFICATE_NO", auditProject.getCertnum());
                // 联系方式
                ylgg.set("CONTACT_PHONE", auditProject.getContactphone());
                ylgg.set("PROJECT_NAME", auditTask.getTaskname());
                // 许可内容
                ylgg.set("PERMIT_CONTENT", "--");
                // 默认'A'
                ylgg.set("CERTIFICATE_LEVEL", "A");
            }
            switch (auditTask.getItem_id()) {
                // 设置X射线影像诊断建设项目放射性职业病危害预评价报告审核
                case "11370830F5034613XX400012311200301":
                    ylgg.setSql_TableName("ex_yljgfspjsh_1");
                    ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_yljgfspjsh_1", certinfo.getCertno());
                // 设置X射线影像诊断建设项目放射性职业病危害预评价报告审核 ： 照面信息
                {
                    // YILIAOJIGOUMINGCHEN 照面项：医疗机构名称
                    // XIANGMUMINGCHEN 照面项：项目名称
                    // XIANGMUDIZHI 照面项：项目地址
                    // XIANGMUXINGZHIXINJIAN 照面项：项目性质新建
                    // XIANGMUXINGZHIGAIJIAN 照面项：项目性质改建
                    // XIANGMUXINGZHIKUOJIAN 照面项：项目性质扩建
                    // FADINGDAIBIAOREN 照面项：法定代表人
                    // XIANGMUFUZEREN 照面项：项目负责人
                    // LIANXIREN 照面项：联系人
                    // LIANXIDIANHUA 照面项：联系电话
                    // YUPINGSHENDANWEI 照面项：预评审单位
                    // YUPINGJIABAOGAOBIANHAO 照面项：预评价报告编号
                    // ZHIYEBINGWEIHAILEIBIE 照面项：职业病危害类别
                    ylgg.set("YILIAOJIGOUMINGCHEN", certinfoExtension.getStr("yljgmc"));
                    ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc"));
                    ylgg.set("XIANGMUDIZHI", certinfoExtension.getStr("xmdz"));
                    ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr"));
                    ylgg.set("XIANGMUFUZEREN", certinfoExtension.getStr("xmfzr"));
                    ylgg.set("LIANXIREN", certinfoExtension.getStr("lxr"));
                    ylgg.set("LIANXIDIANHUA", certinfoExtension.getStr("lxdh"));
                    ylgg.set("YUPINGSHENDANWEI", certinfoExtension.getStr("ypjdw"));
                    ylgg.set("YUPINGJIABAOGAOBIANHAO", certinfoExtension.getStr("ypjbgbh"));
                    ylgg.set("ZHIYEBINGWEIHAILEIBIE", certinfoExtension.getStr("zybwhlb"));
                }
                    break;
                // 设置X射线影像诊断的建设项目放射性职业病防护设施竣工验收
                case "11370830F5034613XX400012311300301":
                    ylgg.setSql_TableName("ex_yljgfsjgys_1");
                    ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_yljgfsjgys_1", certinfo.getCertno());
                // 设置X射线影像诊断的建设项目放射性职业病防护设施竣工验收 ： 照面信息
                {
                    ylgg.set("YILIAOJIGOUMINGCHEN", certinfoExtension.getStr("yljgmc")); // 照面项：医疗机构名称
                    ylgg.set("XIANGMUMINGCHEN", certinfoExtension.getStr("xmmc")); // 照面项：项目名称
                    ylgg.set("XIANGMUDIZHI", certinfoExtension.getStr("xmdz")); // 照面项：项目地址
                    ylgg.set("FADINGDAIBIAOREN", certinfoExtension.getStr("fddbr")); // 照面项：法定代表人
                    ylgg.set("XIANGMUFUZEREN", certinfoExtension.getStr("xmfzr")); // 照面项：项目负责人
                    ylgg.set("LIANXIREN", certinfoExtension.getStr("lxr")); // 照面项：联系人
                    ylgg.set("LIANXIDIANHUA", certinfoExtension.getStr("lxdh")); // 照面项：联系电话
                    ylgg.set("KONGZHIXIAOGUOPINGJIADANWEI", certinfoExtension.getStr("kzxgpjdw")); // 照面项：控制效果评价单位
                    ylgg.set("KZXGPJBGBH_15", certinfoExtension.getStr("kzxgpjbgbh")); // 照面项：控制效果评价报告编号
                    ylgg.set("ZHIYEBINGWEIHAILEIBIE", certinfoExtension.getStr("zybwhlb")); // 照面项：职业病危害类别
                }
                    break;
            }

            ylgg.set("operatedate", new Date());
            ylgg.set("rowguid", UUID.randomUUID().toString());
            ylgg.setPrimaryKeys("rowguid");
            if (ylggrecord != null) {
                ylgg.set("rowguid", ylggrecord.getStr("rowguid"));
                ylgg.set("state", "update");
                iJnProjectService.UpdateRecord(ylgg);
            }
            else {
                ylgg.set("state", "insert");
                iJnProjectService.inserRecord(ylgg);
            }
        }
    }

    /**
     * 发起勘验
     */
    public void faqikanyan() {

        try {
            String liangshsantakantask = ConfigUtil.getFrameConfigValue("liangshsantakantask");
            if(StringUtils.isNotBlank(liangshsantakantask)){
                if(auditTask!=null && liangshsantakantask.contains(auditTask.getItem_id())){
                    String materialcliengguid = UUID.randomUUID().toString();
                    String liangshantakanurl = ConfigUtil.getConfigValue("xmzArgs", "liangshantakanurl");
                    JSONObject dataJson = new JSONObject();
                    JSONObject params = new JSONObject();
                    params.put("itemname", auditProject.getProjectname()+"("+auditProject.getApplyername()+")");
                    params.put("taskid", auditTask.getItem_id());
                    params.put("applicant", auditProject.getApplyername());
                    params.put("idcard", auditProject.getCertnum());
                    params.put("applicantphone", auditProject.getContactmobile());
                    params.put("formguid", auditProject.getRowguid());
                    params.put("itemcode", auditProject.getFlowsn());
                    params.put("materialcliengguid", materialcliengguid);
                    dataJson.put("token", "Epoint_WebSerivce_**##0601");
                    dataJson.put("params", params);
                    String result = HttpUtil.doPostJson(liangshantakanurl+"/rest/takan/receive",
                            dataJson.toString());
                    log.info("faqikanyan返回结果：" + result);
                    if(result!=null){
                        JSONObject returnobject = JSONObject.parseObject(result);
                        JSONObject custom = returnobject.getJSONObject("custom");
                        String code = custom.getString("code");
                        if("1".equals(code)){
                            //上传附件
                            List<AuditProjectMaterial> materil = iauditprojectmaterial.selectProjectMaterial(auditProject.getRowguid())
                                    .getResult();
                            if(CollectionUtils.isNotEmpty(materil)){
                                Map<String, String> param = new HashMap<>();
                                for (AuditProjectMaterial auditProjectMaterial : materil) {
                                    List<FrameAttachInfo> attachlist = iAttachService
                                            .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                    for (FrameAttachInfo attachinfo : attachlist) {
                                        String attachGuid = attachinfo.getAttachGuid();
                                        FrameAttachStorage attachStorage = iAttachService.getAttach(attachGuid);
                                        String filename = attachinfo.getAttachFileName();
                                        Map<String, String> headers = new HashMap<String, String>();
                                        InputStream inputStream = attachStorage.getContent();
                                        try {
                                            // 添加附件属性
                                            param.put("name", filename);
                                            param.put("clientguid", materialcliengguid);
                                            param.put("source", "济宁市一窗平台");
                                            param.put("size", attachStorage.getSize()+"");
                                            param.put("fileId", attachGuid);
                                            // HttpUtil工具类调用
                                            String uploadresult =
                                                    com.epoint.core.utils.httpclient.HttpUtil.upload(liangshantakanurl+"/rest/jnzwfwAttach/attachUploadnoauth", headers,
                                                            param, inputStream, filename);
                                            log.info("uploadAttach输出：" + uploadresult);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            // 关闭流
                                            try {
                                                if (inputStream != null) {
                                                    inputStream.close();
                                                }
                                            } catch (IOException e) {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return;
                }
            }
            JSONObject dataJson = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("flowsn", auditProject.getFlowsn());
            params.put("iffukan", "0");
            params.put("username", userSession.getDisplayName());
            params.put("ouname", userSession.getOuName());
            params.put("mobile", userSession.getMobile());
            dataJson.put("token", "Epoint_WebSerivce_**##0601");
            dataJson.put("params", params);
            String result = HttpUtil.doPostJson("http://172.16.53.22:8080/jnzwdt/rest/jnkantan/startkantan",
                    dataJson.toString());
            log.info("faqikanyan返回结果：" + result);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起复核
     */
    public void fuhe() {

        try {
            JSONObject dataJson = new JSONObject();
            JSONObject params = new JSONObject();
            params.put("flowsn", auditProject.getFlowsn());
            params.put("iffukan", "1");
            params.put("username", userSession.getDisplayName());
            params.put("ouname", userSession.getOuName());
            params.put("mobile", userSession.getMobile());
            dataJson.put("token", "Epoint_WebSerivce_**##0601");
            dataJson.put("params", params);
            String result = HttpUtil.doPostJson("http://172.16.53.22:8080/jnzwdt/rest/jnkantan/startkantan",
                    dataJson.toString());
            log.info("faqikanyan返回结果：" + result);
        }
        catch (JSONException e) {
        }
    }

    /**
     * 勘验结果有无
     */
    public void hastayanresult() {

        try {

            String liangshsantakantask = ConfigUtil.getFrameConfigValue("liangshsantakantask");
            if(StringUtils.isNotBlank(liangshsantakantask)) {
                if (auditTask != null && liangshsantakantask.contains(auditTask.getItem_id())) {
                    addCallbackParam("isliangshan", "1");

                    return;
                }
            }
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("projctguid", auditProject.getRowguid());
            List<Kanyanmain> kanyanmains = iKanyanmainService.findList("select * from kanyanmain where projctguid=?",
                    auditProject.getRowguid());
            if (kanyanmains != null && EpointCollectionUtils.isNotEmpty(kanyanmains)) {
                addCallbackParam("rowguid", kanyanmains.get(0).getRowguid());
            }
        }
        catch (JSONException e) {
        }
    }

    public AuditProjectDelivery getAuditProjectDelivery() {
        return auditProjectDelivery;
    }

    public void setAuditProjectDelivery(AuditProjectDelivery auditProjectDelivery) {
        this.auditProjectDelivery = auditProjectDelivery;
    }

}
