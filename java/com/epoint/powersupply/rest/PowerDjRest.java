package com.epoint.powersupply.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxblxxxxb;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.xml.ReadXML;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.powersupply.api.IAuditSpProjectinfoGdbzService;
import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;
import com.epoint.powersupply.util.NationUtils;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.xmz.cxbus.api.ICxBusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 营销2.0对接
 * 
 */
@RestController
@RequestMapping("/powerdjrest")
public class PowerDjRest
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

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
     * 物流基本信息表实体对象
     */
    private AuditLogisticsBasicinfo auditLogisticsBasicinfo = null;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IAuditProject auditProjectServcie;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IConfigService configService;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

    @Autowired
    private IAuditProjectNotify projectNotifyService;

    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;

    @Autowired
    private IAuditProjectUnusual projectUnusualService;

    @Autowired
    IAuditProjectOperation auditProjectOperationService;

    @Autowired
    private IAuditSpISubapp auditSpISubappService;

    @Autowired
    private IWFInstanceAPI9 wfinstance;

    private AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();

    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    @Autowired
    private IAuditSpITask iauditspitask;

    @Autowired
    private IAuditSpBusiness iauditspbusiness;

    @Autowired
    private IAuditRsItemBaseinfo iauditrsitembaseinfo;

    @Autowired
    private IAuditTaskResult auditResultService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IHandleRSMaterial handleRSMaterialService;

    @Autowired
    private IAuditSpShareMaterialRelation relationService;

    @Autowired
    private IHandleSPIMaterial handleSpiMaterialService;
    @Autowired
    private IAuditSpInstance iauditspinstance;
    @Autowired
    private IAuditSpISubapp iauditspisubapp;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private ISpglXmspsxblxxxxb ispglxmspsxblxxxxb;
    @Autowired
    private ICxBusService cxBusService;
    @Autowired
    private IAuditSpProjectinfoGdbzService auditSpProjectinfoGdbzService;
    /**
     * 审批结果
     */
    private AuditTaskResult auditResult = null;

//    /**
//     * [接收办电预申请]
//     *
//     * @param request
//     * @return
//     * @exception/throws [违例类型] [违例说明]
//     * @see [类、类#方法、类#成员]
//     */
//    public String jsbdysq(String subAppGuid) {
//        try {
//            log.info("=======开始请求jsbdysq");
//            log.info("subAppGuid--->" + subAppGuid);
//
//            // 设置供电报装办件信息subappGuid、preApp
//            // 在审批中判断是否是供电报装的办件，是的话设置办件subappGuid，并把办件guid设置到供电报装办件的projectguid
//
//            // 根据参数获取电子表单业务数据，供电报装表名：formtable20231108162746
//            Record formDetail = cxBusService.getDzbdDetail("formtable20231108162746", subAppGuid);
//            // 将表单数据放入params
//            JSONObject parms = new JSONObject();
//            // 预申请编号
//            String preAppNo = "DL37408002"
//                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT) + (int) (Math.random() * 90 + 10);
//            parms.put("preAppNo", preAppNo);
//            // 服务渠道，默认64
//            parms.put("channelNo", "64");
//            // 省proCode，默认370000
//            parms.put("proCode", "370000");
//            // 市编码，默认370800
//            parms.put("cityCode", "370800");
//            // 区县编码
//            parms.put("countyCode", formDetail.getStr("qx"));
//            // 街道
//            parms.put("streetCode", formDetail.getStr("jd"));
//            // 用电地址（详细地址）
//            parms.put("elecAddr", formDetail.getStr("xxdz"));
//            // 报装意向基本事项标识
//            List<AuditSpITask> result = iauditspitask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
//            String taskGuid = "";
//            for (AuditSpITask auditSpITask : result) {
//                if(auditSpITask.getTaskname().contains("电")) {
//                    taskGuid = auditSpITask.getTaskguid();
//                    break;
//                }
//            }
//            parms.put("meaningtaskguid", taskGuid);
//            // 业务类型
//            parms.put("busiTypeCode", "0102");
//            // 业务子类
//            parms.put("reduceCapMode", "010202");
//            // 客户类型
//            parms.put("custType", formDetail.getStr("kehlx"));
//            // 供电单位
//            parms.put("orgNo", formDetail.getStr("gddw"));
//            // 备注
//            parms.put("remark", formDetail.getStr("bz"));
//            // 是否个人充电桩*
//            parms.put("ischcEqp", formDetail.getStr("sfgrcdz"));
//            // 是否装变压器*
//            parms.put("isInstTrans", formDetail.getStr("sfzbyq"));
//            // 申请运行容量 capRunCap
//            parms.put("capRunCap", formDetail.getStr("sqyxrl"));
//            // 申请合同容量 contractCap
//            parms.put("contractCap", formDetail.getStr("sqhtrl"));
//
//            JSONObject paramsJson = new JSONObject();
//            paramsJson.put("serviceCode", "20001780");
//            paramsJson.put("source", "010262");
//            paramsJson.put("target", "37101");
//            paramsJson.put("data", parms);
//            String returnParam = com.epoint.core.utils.httpclient.HttpUtil
//                    .doPostJson(configService.getFrameConfigValue("bdysq_url"), paramsJson.toJSONString());
//
//            AuditSpISubapp auditSpISubapp = iauditspisubapp.getSubappByGuid(subAppGuid).getResult();
//            log.info("获取当前auditSpISubapp实体--->" + auditSpISubapp);
//            if(!ObjectUtils.isEmpty(auditSpISubapp)) {
//                log.info("更新auditSpISubapp的preAppNo字段");
//                auditSpISubapp.set("preAppNo", preAppNo);
//                iauditspisubapp.updateAuditSpISubapp(auditSpISubapp);
//                log.info("更新成功");
//            }
//            log.info("新增供电报装办件");
//            AuditSpProjectinfoGdbz projectInfoGdbz = new AuditSpProjectinfoGdbz();
//            projectInfoGdbz.setRowguid(UUID.randomUUID().toString());
//            projectInfoGdbz.setSubAppGuid(subAppGuid);
//            auditSpProjectinfoGdbzService.insert(projectInfoGdbz);
//            log.info("=======结束请求jsbdysq");
//            return returnParam;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            log.info("=======jsbdysq出现异常");
//            return JsonUtils.zwfwRestReturn("0", "出现异常", "");
//        }
//
//    }

    /**
     * [办电预申请审批反馈]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/bdysqsbfk", method = RequestMethod.POST)
    public String bdysqsbfk(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始请求bdysqsbfk");
            log.info(params);

            EpointFrameDsManager.begin(null);
            // String apiUrl = configService.getFrameConfigValue("SXXTZ_URL");
            JSONObject paramsJson = JSONObject.parseObject(params);
            // 为获取信息的唯一标识码
//            String token = parms.getString("token");
//            if (!ZwdtConstant.SysValidateData.equals(token)) {
//                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
//            }
            JSONObject parms = paramsJson.getJSONObject("data");
            String serialNo = parms.getString("serialNo");
            String preAppNo = parms.getString("preAppNo");
            String sendElectType = parms.getString("sendElectType");
            String appNo = parms.getString("appNo");
            String consNo = parms.getString("consNo");
            String consName = parms.getString("consName");
            String auditTime = parms.getString("auditTime");
            String reasonNo = parms.containsKey("reasonNo") ? parms.getString("reasonNo") : "";
            String reasonName = parms.containsKey("reasonName") ? parms.getString("reasonName") : "";
            String auditContent = parms.getString("auditContent");
            String auditResult = parms.getString("auditResult");
            String orgNo = parms.getString("orgNo");
            String serialNumber = parms.getString("serialNumber");
            String itemNo = parms.getString("itemNo");
            String DWDM = parms.getString("DWDM");
            String CNSX = parms.getString("CNSX");
            String projectNo = parms.getString("projectNo");
            String orgName = parms.getString("orgName");
            if (StringUtil.isBlank(preAppNo) || StringUtil.isBlank(auditTime) || StringUtil.isBlank(sendElectType)) {
                return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
            }
            log.info("审核类型sendElectType--->" + sendElectType);
            SqlConditionUtil sql = new SqlConditionUtil();
//            sql.setInnerJoinTable("audit_sp_projectinfo_gdbz b", "a.rowguid", "b.projectguid");
            sql.eq("preAppNo", preAppNo);
            log.info("根据preAppNo查询供电报装办件");
            AuditSpProjectinfoGdbz auditspprojectinfogdbz = new SQLManageUtil()
                    .getBeanByCondition(AuditSpProjectinfoGdbz.class, sql.getMap());
            if (auditspprojectinfogdbz == null) {
                return JsonUtils.zwdtRestReturn("0", "数据不存在！", "");
            }
            AuditCommonResult<AuditProject> auditProjectByRowGuid = auditProjectServcie
                    .getAuditProjectByRowGuid(auditspprojectinfogdbz.getProjectGuid(), null);
//            auditProject = commonService.getRecord(sql.getMap(), AuditProject.class).getResult();
            if (auditProjectByRowGuid == null
                    || (auditProjectByRowGuid != null && auditProjectByRowGuid.getResult() == null)) {
                return JsonUtils.zwdtRestReturn("0", "办件查询为空！", "");
            }
            log.info("查询到供电报装办件对应的audit_project");
            auditProject = auditProjectByRowGuid.getResult();
            if (auditProject != null) {

                String isJSTY = ConfigUtil.getConfigValue("isJSTY");
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                    log.info("是江苏通用");
                    Map<String, String> conditionMap = new HashMap<String, String>(16);
                    conditionMap.put("projectguid = ", auditProject.getRowguid());
                    List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                            .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
                    if (list != null && list.size() > 0) {
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
            }
            // 获取事项信息
            if (StringUtil.isNotBlank(auditProject.getTaskguid())) {
                auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                log.info("获取事项信息--->" + auditTask);
                auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                // areaCode = auditTask.getAreacode();
            }
            String projectguid = auditProject.getRowguid();
            // 获取办件信息
            // String fields = "
            // rowguid,subappguid,businessguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,"
            // +
            // "taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,"
            // +
            // "contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,"
            // +
            // "biguid,banjiedate,is_pause,spendtime,if_express,applydate,is_check,is_fee,banjieresult,if_express_ma,is_charge,"
            // +
            // "is_test,currentareacode,handleareacode,taskid,task_id,is_delay,onlineapplyerguid,legalid,charge_when,is_cert,certnum,"
            // +
            // "certrowguid,receiveuserguid,promise_status,xiangmubh,isgzcn,acceptuserdate";
            // auditProject =
            // auditProjectServcie.getAuditProjectByRowGuid(fields,
            // projectguid).getResult();
            if (StringUtil.isBlank(sendElectType)) {
                return JsonUtils.zwdtRestReturn("0", "审核类型不能为空！", "");
            }
            String msg = "成功";
            switch (sendElectType) {
                case "1":
                    msg = receiveProject();
                    break;
                case "6":
                    msg = buzhengProject();
                    break;
                case "4":
                    String reason = "";
                    if (StringUtil.isNotBlank(reasonNo)) {
                        switch (reasonNo) {
                            case "01":
                                reason = "照片不清";
                                break;
                            case "02":
                                reason = "照片不对";
                                break;
                            case "03":
                                reason = "业务未上线";
                                break;
                            case "04":
                                reason = "客户重复申请";
                                break;
                            case "05":
                                reason = "资料审核不通过";
                                break;
                            default:
                                break;
                        }
                    }
                    if (StringUtil.isNotBlank(reasonName)) {
                        log.info("接口返回不通过原因名称--->" + reasonName);
                        reason = reasonName;
                    }
                    msg = notAccept(reason);
                    break;
                case "3":
                    sql.clear();
                    sql.eq("blzt", "1");
                    sql.eq("spsxslbm", auditProject.getFlowsn());
                    // 由于供电那边不推送为接件状态，所以先查看是否存在了为1的在状态
                    AuditCommonResult<List<SpglXmspsxblxxxxb>> result = ispglxmspsxblxxxxb
                            .getListByCondition(sql.getMap());
                    log.info("result--->" + result);
                    if (result != null) {
                        log.info("result不为空");
                        List<SpglXmspsxblxxxxb> list = result.getResult();
                        // List<SpglXmspsxblxxxxb> collectsList =
                        // list.stream().filter(e ->
                        // "1".equals(e.getBlzt())).collect(Collectors.toList());
                        if (list == null || list.isEmpty()) {
                            log.info("list不为空");
                            log.info("执行receiveProject");
                            receiveProject();
                        }
                    }
                    else {
                        log.info("result为空");
                        log.info("执行receiveProject");
                        receiveProject();
                    }
                    log.info("执行acceptProject");
                    msg = acceptProject();
                    break;
                case "11":
                    msg = banJieProject();
                    break;
                default:
                    break;
            }
            log.info("=======结束请求SythAcceptPushRest");
            EpointFrameDsManager.commit();
            // 返回值结构修改为接口文档指定的结构
            JSONObject result = new JSONObject();
            result.put("code", "0000");
            result.put("msg", "成功");
            result.put("data", new JSONObject());
            log.info("反馈接口返回值--->" + result.toJSONString());
            return result.toJSONString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======bdysqsbfk出现异常");
            EpointFrameDsManager.rollback();
            JSONObject result = new JSONObject();
            result.put("code", "0000");
            result.put("msg", "反馈失败");
            result.put("data", new JSONObject());
            log.info("反馈接口返回值--->" + result.toJSONString());
            return result.toJSONString();
        }
        finally {
            EpointFrameDsManager.close();
        }

    }

    /**
     * [出文]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/chuwen", method = RequestMethod.POST)
    public String chuwen(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始请求chuwen");
            EpointFrameDsManager.begin(null);

            log.info("原始参数--->" + params);
            log.info("将参数进行Unicode解码");
            String finalParams = params.replaceAll("\\u003c", "<").replaceAll("\\u003d", "=").replaceAll("\\u003e", ">");
            log.info("解码后参数--->" + finalParams);
            // String apiUrl = configService.getFrameConfigValue("SXXTZ_URL");
//            JSONObject parms = JSONObject.parseObject(params);
            log.info("解码后参数转json");
            JSONObject parms = JSONObject.parseObject(finalParams);
            log.info("转换json结果--->" + parms);
            // 为获取受理信息的唯一标识码
            /*String token = parms.getString("token");
            String serviceCode = parms.getString("serviceCode");
            String source = parms.getString("source");
            String target = parms.getString("target");*/
            // String data = parms.getString("data");
            /*if (!ZwdtConstant.SysValidateData.equals(token)) {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }*/
            log.info("获取json的data对象");
            JSONObject data = parms.getJSONObject("data");
            log.info("data对象--->" + data);

            log.info("获取data的param字符串");
            String param = data.getString("param");
            log.info("param字符串--->" + param);

            log.info("param字符串转xml");
            ReadXML readXML = new ReadXML(param);
            String preAppNo = readXML.getTextContentByTag("preAppNo");
            log.info("从xml获取preAppNo--->" + preAppNo);
            /*String FROM = readXML.getTextContentByTag("FROM");
            String orgNo = readXML.getTextContentByTag("orgNo");
            String BATCHID = readXML.getTextContentByTag("BATCHID");
            if (StringUtil.isBlank(preAppNo) || StringUtil.isBlank(FROM) || StringUtil.isBlank(orgNo)
                    || StringUtil.isBlank(BATCHID)) {
                return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
            }*/

            if (StringUtil.isBlank(preAppNo)) {
                return JsonUtils.zwdtRestReturn("0", "必填参数不能为空！", "");
            }

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("preAppNo", preAppNo);
            AuditSpProjectinfoGdbz auditspprojectinfogdbz = new SQLManageUtil()
                    .getBeanByCondition(AuditSpProjectinfoGdbz.class, sql.getMap());
            if (auditspprojectinfogdbz == null) {
                return JsonUtils.zwdtRestReturn("0", "数据不存在！", "");
            }
            AuditCommonResult<AuditProject> auditProjectByRowGuid = auditProjectServcie
                    .getAuditProjectByRowGuid(auditspprojectinfogdbz.getProjectGuid(), null);
            if (auditProjectByRowGuid == null
                    || (auditProjectByRowGuid != null && auditProjectByRowGuid.getResult() == null)) {
                return JsonUtils.zwdtRestReturn("0", "办件查询为空！", "");
            }
            auditProject = auditProjectByRowGuid.getResult();

            /*SqlConditionUtil sql = new SqlConditionUtil();
            sql.setInnerJoinTable("audit_sp_projectinfo_gdbz b", "a.rowguid", "b.projectguid");
            sql.eq("b.preAppNo", preAppNo);
            auditProject = commonService.getRecord(sql.getMap(), AuditProject.class).getResult();*/
            if (auditProject == null) {
                return JsonUtils.zwdtRestReturn("0", "办件查询为空！", "");
            }
            // 判断办件状态，如果是已经办结的则直接返回
            log.info("判断办件状态--->" + auditProject.getStatus());
            if(auditProject.getStatus() != 90) {
                log.info("未办结");
                String msg = markFaFang(readXML);
                log.info("发放审批结果返回值--->" + msg);

                // 目前第三方调用反馈接口不会发送办结的数据，所以调用出文后，直接在调用一下办结操作
                log.info("办结操作");
                banJieProject();

                EpointFrameDsManager.commit();
            }
            log.info("=======结束请求chuwen");
            log.info("出文接口返回值--->" + NationUtils.rtnXml("1", "success"));
            return NationUtils.rtnXml("1", "success");
        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("=======chuwen出现异常");
//            return JsonUtils.zwfwRestReturn("0", "出现异常", "");
            log.info("出文接口返回值--->" + NationUtils.rtnXml("1", "出现异常"));
            return NationUtils.rtnXml("1", "出现异常");
        }
        finally {
            EpointFrameDsManager.close();
        }

    }

    /**
     * 
     * 发放审批结果
     * 
     */
    public String markFaFang(ReadXML readXML) {
        log.info("发放审批结果");
        String remsg = "";
        String PWBH = readXML.getTextContentByTag("PWBH");
        String FILENAME = readXML.getTextContentByTag("FILENAME");
        String DATAHANDLER = readXML.getTextContentByTag("DATAHANDLER");
        log.info("文号--->" + PWBH);
        log.info("文件名称--->" + FILENAME);
        log.info("文件流码串--->" + DATAHANDLER);
        // base64转为流
        byte[] base64s = Base64Util.decodeBuffer(DATAHANDLER);
        InputStream inputStream = new ByteArrayInputStream(base64s);
        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
        // 保存附件
        frameAttachInfo = new FrameAttachInfo();
        frameAttachInfo.setAttachGuid(UUID.randomUUID().toString());
        frameAttachInfo.setCliengGuid(auditProject.getRowguid());
        frameAttachInfo.setCliengTag(null);
        frameAttachInfo.setCliengInfo("营销2.0系统对接");
        frameAttachInfo.setUploadUserGuid(null);
        frameAttachInfo.setUploadUserDisplayName(null);
        frameAttachInfo.setUploadDateTime(new Date());
        frameAttachInfo.setAttachFileName(FILENAME);
        frameAttachInfo.setContentType((StringUtil.isNotBlank(FILENAME) && FILENAME.contains("."))
                ? FILENAME.substring(FILENAME.lastIndexOf(('.')))
                : "");
        try {
            frameAttachInfo.setAttachLength((long) inputStream.available());
        }
        catch (IOException e) {
            log.error("保存附件长度出现异常");
            e.printStackTrace();
        }
        attachService.addAttach(frameAttachInfo, inputStream);
        log.info("添加附件成功，attachGuid--->" + frameAttachInfo.getAttachGuid());
        String wenhao = PWBH;
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
        // 自建系统办件在发放前自动暂停
        if (Integer.valueOf(ZwfwConstant.CONSTANT_INT_ONE).equals(auditProject.getIs_pause())) {
            handleProjectService.handleRecover(auditProject, "营销2.0系统对接", userguid, null, null);
        }
        auditResult = auditResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
        log.info("根据办件获取事项结果--->" + auditResult);
        // 1、更新办件发证信息
        log.info("更新办件发证信息");
        auditProject.setWenhao(wenhao);
        auditProject.setIs_cert(ZwfwConstant.CONSTANT_INT_ONE);
        auditProject.setCertificatedate(new Date());
        auditProject.setCertificateuserguid(userguid);
        auditProject.setCertificateusername("营销2.0系统对接");
        auditProjectServcie.updateProject(auditProject);
        // 当配置了事项审批结果才生成共享材料实例
        if(EpointCollectionUtils.isNotEmptyMap(auditResult)) {
            log.info("查询到事项审批结果");
            int resulttype = auditResult.getResulttype();
            log.info("resultType--->" + resulttype);
            String certrowguid = UUID.randomUUID().toString();

            // 是证照或批文
            if (resulttype == ZwfwConstant.LHSP_RESULTTYPE_ZZ || resulttype == ZwfwConstant.LHSP_RESULTTYPE_PW
                    || resulttype == ZwfwConstant.LHSP_RESULTTYPE_SQBGZ) {
                log.info("是证照或者批文");
                // 1.生成共享材料实例
                List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(auditProject.getRowguid());
                if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                    String certownertype = "";
                    // 企业类型申请人
                    if (auditProject.getApplyertype() == 10) {
                        certownertype = ZwfwConstant.CERTOWNERTYPE_FR;
                    }
                    // 个人
                    else if (auditProject.getApplyertype() == 20) {
                        certownertype = ZwfwConstant.CERTOWNERTYPE_ZRR;
                    }
                    this.createAuditShareMaterialI(auditResult.getSharematerialguid(), auditProject.getCertnum(),
                            auditProject.getRowguid(), UUID.randomUUID().toString(), auditProject.getApplyername(),
                            certownertype, auditProject.getCerttype(), certrowguid);
                }
                log.info("生成共享材料实例结束");
                // 如果是在主题内进行共享的，那么还需要更新主题中的结果状态
                if (StringUtil.isNotBlank(auditProject.getBiguid())
                        && StringUtil.isNotBlank(auditProject.getSubappguid())) {
                    String cliengguid = "";
                    if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                        cliengguid = auditProject.getRowguid();
                    }
                    List<AuditTaskResult> resultlist = auditResultService
                            .selectResultByTaskID("rowguid", auditProject.getTask_id()).getResult();
                    StringBuffer materilids = new StringBuffer();
                    materilids.append("'");
                    for (AuditTaskResult auditTaskResult : resultlist) {
                        materilids.append(auditTaskResult.getRowguid());
                        materilids.append("','");
                    }

                    materilids.append("'");
                    AuditSpShareMaterialRelation auditSpShareMaterialRelation = relationService
                            .getRelationByIDS(auditProject.getBusinessguid(), materilids.toString()).getResult();

                    if (auditSpShareMaterialRelation != null) {
                        handleSpiMaterialService.updateResultStatusAndAttach(auditProject.getSubappguid(),
                                auditSpShareMaterialRelation.getSharematerialguid(), 10, cliengguid, certrowguid);

                    }
                }
                log.info("更新审批材料结束");
            }
        }
        // 发送mq处理，结果信息
        log.info("发送mq");
        String msg = auditProject.getRowguid() + "." + auditProject.getApplyername() + ".";
        sendMQMessageService.sendByExchange("exchange_handle", msg,
                "project." + "370800" + ".finishfile." + auditProject.getTask_id());
        remsg = "发证成功";
        return remsg;
    }

    /**
     * 
     * 生成共享材料实例
     * 
     * @param shareMaterialGuid
     * @param oldCliengGuid
     * @param newCliengGuid
     */
    public void createAuditShareMaterialI(String shareMaterialGuid, String certnum, String oldCliengGuid,
            String newCliengGuid, String applyername, String certownertype, String certownercerttype,
            String certrowguid) {
        String areacode = "370800";
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        handleRSMaterialService.createShareMaterialI(auditResult.getSharematerialguid(), certnum,
                auditProject.getRowguid(), UUID.randomUUID().toString(), null, ZwfwConstant.ZHENGZHAOADD_WAY_BJFZ,
                userguid, "营销2.0系统对接", applyername, certownertype, certownercerttype, certrowguid, areacode);
    }

    /**
     *
     * 接件操作
     *
     */
    public String receiveProject() {
        log.info("进入receiveProject方法");
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
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
        String sqtzaddress = handleProjectService.handleReceive(auditProject, "营销2.0系统对接", userguid, windowguid, "综合窗口")
                .getResult();
        log.info("==============receiveProject()的sqtzaddress:" + sqtzaddress);
        // 向投资监管平台发送mq---好像对应的mq消费者（SpglxmspsxblxxxxbClientHandle）里面没有对接件类型进行处理，故直接插入
//        String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
//        sendMQMessageService.sendByExchange("exchange_handle", msg,
//                "project." + ZwfwUserSession.getInstance().getAreaCode() + ".receive." + auditProject.getTask_id());
        if (StringUtil.isBlank(auditProject.getBiguid())) {
            log.info("供电报装（接件）：biguid为空,办件projectguid为：" + auditProject.getRowguid());
//            return;
        }
        AuditSpInstance auditspinstance = iauditspinstance.getDetailByBIGuid(auditProject.getBiguid()).getResult();
        if (auditspinstance == null) {
            log.info("供电报装（接件）：没有查询到对应项目实例,办件projectguid为：" + auditProject.getRowguid());
//            return "接件失败";
        }
        AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
        AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                .getResult();
        if (baseinfo == null) {
            log.info("供电报装（接件）：没有查询到对应项目,办件projectguid为：" + auditProject.getRowguid());
//            return "接件失败";
        }
        else {
            // 拿地即开工项目上报处理逻辑，如果是拿地即开工项目且土地出让合同未上传的不上报
            if (StringUtil.isNotBlank(baseinfo.getParentid())) {
                String parentid = baseinfo.getParentid();
                AuditRsItemBaseinfo parentbaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(parentid)
                        .getResult();
                if (StringUtil.isNotBlank(parentbaseinfo)) {
                    if (StringUtil.isNotBlank(parentbaseinfo.getStr("ndjkgstatus"))
                            && StringUtil.isBlank(parentbaseinfo.getStr("tdcrhtissubmit"))) {
                        log.info("供电报装（接件）：拿地即开工项目且土地出让合同未上传的不上报,办件projectguid为：" + auditProject.getRowguid());
//                        return "接件失败";
                    }
                }

            }
        }
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                .getResult();
        if (auditspbusiness == null) {
            log.info("供电报装：没有查询到对应主题,办件projectguid为：" + auditProject.getRowguid());
//            return "接件失败";
        }
        SpglXmspsxblxxxxb spgl1 = new SpglXmspsxblxxxxb();
        spgl1.setRowguid(UUID.randomUUID().toString());
        spgl1.setDfsjzj(auditProject.getRowguid());
        spgl1.setXzqhdm("370800");
        spgl1.setGcdm(baseinfo.getItemcode());
        spgl1.setSpsxslbm(auditProject.getFlowsn());
        spgl1.setBlcs(null);
        spgl1.setBlr(auditProject.getOperateusername());
        spgl1.setBlzt(ZwfwConstant.CONSTANT_INT_ONE);
        spgl1.setBlyj(null);
        spgl1.setBlsj(auditProject.getApplydate());
        spgl1.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
        spgl1.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
        ispglxmspsxblxxxxb.insert(spgl1);
        // 使用物流
        if (StringUtil.isNotBlank(auditProject.getIf_express())
                && auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
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
        if (StringUtil.isNotBlank(sqtzaddress)) {
            return sqtzaddress;
        }
        else {
            return "接件失败";
        }

    }

    /**
     *
     * 补正操作
     *
     */
    public String buzhengProject() {
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        String bzgzsaddress = handleProjectService.handlePatch(auditProject, "营销2.0系统对接", userguid).getResult();

        if (StringUtil.isNotBlank(bzgzsaddress)) {
            return bzgzsaddress;
        }
        else {
            return "补正失败";
        }
    }

    /**
     * 不予受理
     * 
     */
    public String notAccept(String reason) {
        log.info("进入notAccept不予受理方法");
        String remsg = "";
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        log.info("中心管理员guid--->" + userguid);
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
        log.info("窗口guid--->" + windowguid);
        log.info("办件状态--->" + auditProject.getStatus());
        if (auditProject.getStatus() > ZwfwConstant.BANJIAN_STATUS_YSL) {
            log.info("status>30");
            remsg = "该步骤已经处理完成，请勿重复操作！";
            return remsg;
        }
        log.info("status<=30");
        // String pviguid = auditProject.getPviguid();
        // SqlConditionUtil sql = new SqlConditionUtil();
        // sql.eq("pviguid", pviguid);
        // WorkflowWorkItem workflowWorkItem =
        // commonService.getRecord(sql.getMap(),WorkflowWorkItem.class).getResult();
        // log.info("===========workflowWorkItem:"+workflowWorkItem);

        // 1、插入异常操作信息
        log.info("插入异常操作信息");
        projectUnusualService.insertProjectUnusual(userguid, "营销2.0系统对接", auditProject,
                Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_BYSL), "", reason);
        // 2、插入每日一填的记录;（修改：不予受理不插入记录）
        // projectNumberService.addProjectNumber(auditProject, false,
        // userSession.getUserGuid(),userSession.getDisplayName());
        // 3、插入在线通知
        // String notirytitle = "【不予受理】" + "<" + auditProject.getProjectname() +
        // ">";
        // String handurl = ""; // TODO 处理页面地址还没有

        // projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(),
        // auditProject.getApplyername(),
        // notirytitle, reason, handurl, ZwfwConstant.CLIENTTYPE_BJ,
        // auditProject.getRowguid(),
        // String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL),
        // userSession.getUserGuid(),
        // userSession.getDisplayName());
        log.info("办件拒绝操作");
        String message = handleProjectService.handleReject(auditProject, "营销2.0系统对接", userguid, "综合窗口", windowguid)
                .getResult();
        log.info("==============notAccept()的message:" + message);
        String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
                + ZwfwConstant.OPERATE_BYSL + "'";
        String Filds = " rowguid,remarks ";
        // 添加不予受理备注
        log.info("添加不予受理的auditProjectOperation数据");
        AuditProjectOperation auditProjectOperation = auditProjectOperationService
                .getOperationFileldByProjectGuid(strWhere, Filds, "").getResult();
        log.info("办件操作数据--->" + auditProjectOperation);
        if (auditProjectOperation != null) {
            auditProjectOperation.setRemarks(reason);
            auditProjectOperationService.updateAuditProjectOperation(auditProjectOperation);
            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
            // === 个性化添加,待补证状态下的不需要二次推送了
            if (auditProject.getStatus() != ZwfwConstant.BANJIAN_STATUS_DBB) {
                projectSendzj("nck", auditProject);

            }
            log.info("发送mq");
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".notaccept." + auditProject.getTask_id());
        }

        String[] str = message.split(";");
        // 流程中止操作
        if (StringUtil.isNotBlank(auditProject.getPviguid())) {
            log.info("==============流程终止");
            ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
            List<WorkflowWorkItem> listWorkItem = wfinstance.getWorkItemListByUserGuid(pvi, userguid);
            if (pvi != null && listWorkItem.size() > 0) {
                auditWorkflowBizlogic.finish(listWorkItem.get(0).getWorkItemGuid(), reason, pvi, "");
            }
        }

        if (auditProject.getSubappguid() != null) {
            handleProjectService.handleblspsub(auditProject, userguid, "营销2.0系统对接");
        }

        // edit by Dyt
//        // 个性化发送短信
//
//        System.out.println("这里是不予受理");
//        System.out.println(auditProject);
//
//        // 水电气暖，不予受理需要调用，需要调他们的办结接口
//        SDQNServiceUtil.informSdqnEnd(auditProject);

        // 不予受理需求代码编写wzj
        String sgtaskid = configService.getFrameConfigValue("施工许可事项id");
        log.info("施工许可事项id--->" + sgtaskid);
        if (StringUtil.isNotBlank(sgtaskid) && StringUtil.isNotBlank(auditProject.getTask_id())) {
            if (sgtaskid.equals(auditProject.getTask_id())) {
                String biguid = auditProject.getBiguid();
                if (StringUtil.isNotBlank(biguid)) {
                    String subappguid = auditProject.getSubappguid();
                    SqlConditionUtil infosql = new SqlConditionUtil();
                    infosql.eq("SUBAPPGUID", subappguid);
                    infosql.in("corptype", "3,4");
                    List<ParticipantsInfo> infolist = iParticipantsInfoService
                            .getParticipantsInfoListByCondition(infosql.getMap());
                    if (infolist != null && !infolist.isEmpty()) {
                        for (ParticipantsInfo participantsInfo : infolist) {
                            participantsInfo.set("authority", "0");
                            iParticipantsInfoService.update(participantsInfo);
                        }
                    }
                }
            }
        }

        String jgtaskid = configService.getFrameConfigValue("竣工验收事项id");
        log.info("竣工验收事项id--->" + sgtaskid);
        if (StringUtil.isNotBlank(jgtaskid) && StringUtil.isNotBlank(auditProject.getTask_id())) {
            if (jgtaskid.equals(auditProject.getTask_id())) {
                // 判断是办结还是审批通过
                String biguid = auditProject.getBiguid();
                if (StringUtil.isNotBlank(biguid)) {
                    AuditSpISubapp suapp = auditSpISubappService.getSubappByGuid(auditProject.getSubappguid())
                            .getResult();
                    if (suapp != null) {
                        String ywguid = suapp.getYewuguid();
                        SqlConditionUtil subappsql = new SqlConditionUtil();
                        subappsql.setInnerJoinTable("audit_sp_phase b", "a.phaseguid", "b.rowguid");
                        subappsql.eq("YEWUGUID", ywguid);
                        subappsql.like("b.phaseid", "3");
                        List<AuditSpISubapp> subapplist = auditSpISubappService.getSubappListByMap(subappsql.getMap())
                                .getResult();
                        if (subapplist.isEmpty()) {
                            SqlConditionUtil infosql = new SqlConditionUtil();
                            infosql.eq("SUBAPPGUID", auditProject.getSubappguid());
                            infosql.in("corptype", "3,4");
                            List<ParticipantsInfo> infolist = iParticipantsInfoService
                                    .getParticipantsInfoListByCondition(infosql.getMap());
                            if (infolist != null && !infolist.isEmpty()) {
                                for (ParticipantsInfo participantsInfo : infolist) {
                                    participantsInfo.set("authority", "0");
                                    iParticipantsInfoService.update(participantsInfo);
                                }
                            }
                        }
                    }
                }

            }
        }

        if (StringUtil.isNotBlank(str)) {
            return "失败！";
        }
        else {
            return "成功！";
        }

    }

    // 个性化添加
    // 上报办件的1和3 状态信息
    public void projectSendzj(String isck, AuditProject auditproject) {
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        AuditSpISubapp auditSpISubapp = auditSpISubappService.getSubappByGuid(auditproject.getSubappguid()).getResult();
        if (StringUtil.isNotBlank(auditSpISubapp)) {
            // 发送mq消息推送数据
            AuditRsItemBaseinfo auditRsItemBaseinfo = iauditrsitembaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
            if (StringUtil.isNotBlank(auditRsItemBaseinfo)) {
                String msg = auditproject.getSubappguid() + "." + ZwfwUserSession.getInstance().getAreaCode() + "."
                        + auditRsItemBaseinfo.getItemcode();
                if ("false".equals(isck)) {
                    // 受理方式为全流程网上申请办理
                    msg += ".nck";
                }
                else {
                    msg += ".isck";
                }
                // 拼接办理人用户标识
                msg += "." + UserSession.getInstance().getUserGuid() + "." + auditproject.getRowguid();
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.subapp." + auditproject.getBusinessguid());
            }
        }

    }

    /**
     * 受理
     *
     */
    public String acceptProject() {
        log.info("进入acceptProject方法");
        String remsg = "";
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");

        String taskids = configService.getFrameConfigValue("gdbzTaskGuid");
        log.info("获取办件状态--->" + auditProject.getStatus());
        if (auditProject.getStatus() > ZwfwConstant.BANJIAN_STATUS_YSL) {
            log.info("办件状态大于30");
            remsg = "该步骤已经处理完成，请勿重复操作！";
            return remsg;
        }
        // 如果办件的中心标识为空，重新set值
        AuditProject a = new AuditProject();
        if (StringUtil.isBlank(auditProject.getCenterguid())) {
            a.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        }
        // edit by Dyt
//        // 个性化，在受理的时候保存是否需要能源/煤炭消费减量替代值、能源消费减量替代值(m)、能源消费值(n)三个值
//        a.setRowguid(auditProject.getRowguid());
//        a.set("nyxfz", auditProject.getStr("nyxfz"));
//        a.set("nyxfjltdz", auditProject.getStr("nyxfjltdz"));
//        a.set("is_consumptionreplace", auditProject.getStr("is_consumptionreplace"));
//        auditProjectServcie.updateProject(a);

        // 承诺件或正常流程的即办件
        if (auditTask != null && (auditTask.getType().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ))
                || (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))
                        && ZwfwConstant.JBJMODE_STANDARD.equals(auditTask.getJbjmode())))) {
            // 承诺件
            if (StringUtil.isNotBlank(taskids) && taskids.contains(auditTask.getTask_id())) {
                log.info("acceptProject方法，auditTask的taskId--->" + auditTask.getTask_id());
                // String pviguid = auditProject.getPviguid();
                // SqlConditionUtil sql = new SqlConditionUtil();
                // sql.eq("pviguid", pviguid);
                // WorkflowWorkItem workflowWorkItem =
                // commonService.getRecord(sql.getMap(),WorkflowWorkItem.class).getResult();
                // log.info("===========workflowWorkItem:"+workflowWorkItem);
                EpointFrameDsManager.begin(null);
                handleProjectService.handleAccept(auditProject, "", "营销2.0系统对接", userguid, "综合窗口", windowguid);
                EpointFrameDsManager.commit();
                String msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                msg += "&handleSendToZjxt:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 受理
                    msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                    sendMQMessageService.sendByExchange("exchange_handle", msg,
                            "project." + "370800" + ".accept." + auditProject.getTask_id());

                }
                AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode("370800").getResult();
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
                // addCallbackParam("message", "refresh");

            }

            // 如果是中心办理的，那么就可以起流程
            else {
                boolean flag = isUserWordDoc();// 判断是否使用word
                HashMap<String, String> rtnValue = new AuditWorkflowBizlogic().startProjectWorkflow(
                        auditProject.getTaskguid(), auditProject.getRowguid(), auditProject.getApplyername(), userguid,
                        "营销2.0系统对接");
                if (StringUtil.isNotBlank(rtnValue.get("message"))) {
                    remsg = rtnValue.get("message");
                }

            }

        }
        else {
            EpointFrameDsManager.begin(null);
            // 即办件，直接更新状态待办结状态,并且发送消息
            if (auditTask != null) {
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && ZwfwUserSession.getInstance().getCitylevel().equals(ZwfwConstant.AREA_TYPE_XZJ)) {

                    AuditTaskDelegate delegate = auditTaskDelegateService
                            .findByTaskIDAndAreacode(auditTask.getTask_id(), "370800").getResult();
                    if (delegate != null) {
                        if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                                && ZwfwConstant.CONSTANT_STR_ONE.equals(delegate.getUsecurrentinfo())) {
                            if (delegate.getPromise_day() != null) {
log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day());
                                auditTask.setPromise_day(delegate.getPromise_day());
                            log.info("update promise_day-task:"+auditTask.getItem_id()+","+auditTask.getPromise_day());
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
            handleProjectService.handleAcceptArea(auditProject, "", "营销2.0系统对接", userguid, "综合窗口", windowguid, "");
            EpointFrameDsManager.commit();

            // 非网上申报需要上报办件数据
            if (auditProject.getApplyway() != Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS)
                    && auditProject.getApplyway() != Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {
                String msg = "handleAccept:" + auditProject.getRowguid() + ";" + auditProject.getAreacode();
                // 测试办件不生成
                if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                    ProducerMQ.sendByExchange("receiveproject", msg, "");
                    // 接办分离 受理
                    msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                    sendMQMessageService.sendByExchange("exchange_handle", msg,
                            "project." + "370800" + ".accept." + auditProject.getTask_id());

                }
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
            }

            handleProjectService.handleApprove(auditProject, "营销2.0系统对接", userguid, "", null);

            // addCallbackParam("message", "refresh");

        }
        // 使用物流
        if (StringUtil.isNotBlank(auditProject.getIf_express())
                && auditProject.getIf_express().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
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
        remsg = "成功！";
        return remsg;

    }

    /**
     *
     * 办结操作
     *
     */
    public String banJieProject() {
        String remsg = "";
        // 获取中心管理员guid
        String userguid = configService.getFrameConfigValue("yxdj_zxglyguid");
        // 获取窗口guid
        String windowguid = configService.getFrameConfigValue("yxdj_windowguid");
        // String pviguid = auditProject.getPviguid();
        // SqlConditionUtil sql = new SqlConditionUtil();
        // sql.eq("pviguid", pviguid);
        // WorkflowWorkItem workflowWorkItem =
        // commonService.getRecord(sql.getMap(),WorkflowWorkItem.class).getResult();
        // log.info("===========workflowWorkItem:"+workflowWorkItem);
        String blspnotice = validataByBj();
        if (StringUtil.isNotBlank(blspnotice)) {
            remsg = blspnotice;
            return remsg;
        }

        // handleProjectService.handleFinish(auditProject, "营销2.0系统对接",
        // userguid,
        // workItemGuid);
        log.info("办件办结操作");
        handleProjectService.handleFinish(auditProject, "营销2.0系统对接", userguid, "");
        // biguid不为空则为并联审批
        log.info("获取办件subAppGuid--->" + auditProject.getSubappguid());
        if (auditProject.getSubappguid() != null) {
            log.info("办件subAppGuid不为空");
            AuditSpBusiness business = iauditspbusiness.getAuditSpBusinessByRowguid(auditProject.getBusinessguid())
                    .getResult();
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(business.getBusinesstype())) {
                log.info("根据办件判断并联审批是否已办结");
                handleProjectService.handleblspsub(auditProject, userguid, "营销2.0系统对接");
            }
            try {
                // 个性化调整，将施工许可证办结的时间置到主项的基本信息表中
                log.info("将施工许可证办结的时间置到主项的基本信息表中");
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.isBlank("parentid");
                sqlConditionUtil.eq("biguid", auditProject.getBiguid());
                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iauditrsitembaseinfo
                        .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                    AuditRsItemBaseinfo auditrsitembaseinfo = auditRsItemBaseinfos.get(0);
                    auditrsitembaseinfo.set("sgxkbanjiedate", new Date());
                    iauditrsitembaseinfo.updateAuditRsItemBaseinfo(auditrsitembaseinfo);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 即办件
        String msg = "";
        log.info("获取办件IsTest--->" + auditProject.getIs_test());
        // 测试办件不生成
        if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
            log.info("非测试件");
            // MQ 办结操作
            log.info("mq办结操作");
            msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";" + "营销2.0系统对接";
            ProducerMQ.sendByExchange("receiveproject", msg, "");
            // 接办分离 办结操作
            log.info("接办分离办结操作");
            msg = auditProject.getRowguid() + "." + auditProject.getAreacode() + "." + "营销2.0系统对接";
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".sendresult." + auditProject.getTask_id());
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                // 江苏省省标环节信息
                msg = "handleJBJProcess:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";"
                        + ZwfwConstant.OPERATE_BJ + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                ProducerMQ.sendByExchange("receiveproject", msg, "");

                // MQ 江苏省标
                msg = "handleResult:" + auditProject.getRowguid() + ";" + auditProject.getAreacode() + ";" + "营销2.0系统对接"
                        + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                ProducerMQ.sendByExchange("receiveproject", msg, "");
            }
        }
        remsg = "办结成功";
        log.info("办结成功");

//        // 需求代码编写WZJ
        String sgtaskid = configService.getFrameConfigValue("施工许可事项id");
        log.info("施工许可事项id--->" + sgtaskid);
        if (StringUtil.isNotBlank(sgtaskid) && StringUtil.isNotBlank(auditProject.getTask_id())) {
            if (sgtaskid.equals(auditProject.getTask_id())) {
                String biguid = auditProject.getBiguid();
                if (StringUtil.isNotBlank(biguid)) {
                    String subappguid = auditProject.getSubappguid();
                    SqlConditionUtil infosql = new SqlConditionUtil();
                    infosql.eq("SUBAPPGUID", subappguid);
                    infosql.in("corptype", "3,4");
                    List<ParticipantsInfo> infolist = iParticipantsInfoService
                            .getParticipantsInfoListByCondition(infosql.getMap());
                    if (infolist != null && !infolist.isEmpty()) {
                        for (ParticipantsInfo participantsInfo : infolist) {
                            participantsInfo.set("authority", "0");
                            iParticipantsInfoService.update(participantsInfo);
                        }
                    }
                }
            }
        }

        String jgtaskid = configService.getFrameConfigValue("竣工验收事项id");
        log.info("竣工验收事项id--->" + jgtaskid);
        if (StringUtil.isNotBlank(jgtaskid) && StringUtil.isNotBlank(auditProject.getTask_id())) {
            if (jgtaskid.equals(auditProject.getTask_id())) {
                // 判断是办结还是审批通过
                String biguid = auditProject.getBiguid();
                if (StringUtil.isNotBlank(biguid)) {
                    AuditSpISubapp suapp = auditSpISubappService.getSubappByGuid(auditProject.getSubappguid())
                            .getResult();
                    if (suapp != null) {
                        String ywguid = suapp.getYewuguid();
                        SqlConditionUtil subappsql = new SqlConditionUtil();
                        subappsql.setInnerJoinTable("audit_sp_phase b", "a.phaseguid", "b.rowguid");
                        subappsql.eq("YEWUGUID", ywguid);
                        subappsql.like("b.phaseid", "3");
                        List<AuditSpISubapp> subapplist = auditSpISubappService.getSubappListByMap(subappsql.getMap())
                                .getResult();
                        if ((auditProject.getBanjieresult()!=null && 40 != auditProject.getBanjieresult() && subapplist.isEmpty())
                                || (auditProject.getBanjieresult()!=null && 40 == auditProject.getBanjieresult() && subapplist != null
                                && !subapplist.isEmpty())) {
                            SqlConditionUtil infosql = new SqlConditionUtil();
                            infosql.eq("SUBAPPGUID", auditProject.getSubappguid());
                            infosql.in("corptype", "3,4");
                            List<ParticipantsInfo> infolist = iParticipantsInfoService
                                    .getParticipantsInfoListByCondition(infosql.getMap());
                            if (infolist != null && !infolist.isEmpty()) {
                                for (ParticipantsInfo participantsInfo : infolist) {
                                    participantsInfo.set("authority", "0");
                                    iParticipantsInfoService.update(participantsInfo);
                                }
                            }
                        }
                    }
                }

            }
        }
        return remsg;

    }

    public String validataByBj() {
        if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
            AuditSpITask auditspitask = iauditspitask.getAuditSpITaskByProjectGuid(auditProject.getRowguid())
                    .getResult();
            if (auditspitask != null) {
                // 里程碑事项
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditspitask.getSflcbsx())) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("sflcbsx", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlc.nq("rowguid", auditspitask.getRowguid());
                    sqlc.eq("subappguid", auditProject.getSubappguid());
                    // 判断是否所有里程碑事项是否已办结
                    List<AuditSpITask> spitasklist = iauditspitask.getAuditSpITaskListByCondition(sqlc.getMap())
                            .getResult();
                    // flag false不允许办结 ，true允许办结
                    boolean flag = false;
                    for (AuditSpITask spitask : spitasklist) {
                        // 存在为分发办件的里程碑事项
                        if (StringUtil.isBlank(spitask.getProjectguid())) {
                            // 里程碑办件未初始化
                            flag = true;
                            break;
                        }
                        else {
                            // 已初始化判断是否办结
                            AuditProject auditproject = auditProjectServcie
                                    .getAuditProjectByRowGuid(spitask.getProjectguid(), spitask.getAreacode())
                                    .getResult();
                            // 存在未办结的里程碑事项未办结，直接通过
                            if (auditproject.getStatus() < 90) {
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (!flag) {
                        // 代表最后一个里程碑事项办结，查找所有非里程碑事项是否已经办结
                        sqlc.clear();
                        sqlc.eq("subappguid", auditProject.getSubappguid());
                        sqlc.isBlankOrValue("sflcbsx", ZwfwConstant.CONSTANT_STR_ZERO);
                        spitasklist = iauditspitask.getAuditSpITaskListByCondition(sqlc.getMap()).getResult();
                        for (AuditSpITask spitask : spitasklist) {
                            // 所有非里程碑事項
                            if (StringUtil.isBlank(spitask.getProjectguid())) {
                                // addCallbackParam("notice",
                                // "请办结完此次申报所有事项后，再办结里程碑事项！");
                                return "请办结完此次申报所有事项后，再办结里程碑事项！";
                            }
                            else {
                                AuditProject auditproject = auditProjectServcie
                                        .getAuditProjectByRowGuid(spitask.getProjectguid(), spitask.getAreacode())
                                        .getResult();
                                if (auditproject.getStatus() < 90) {
                                    // addCallbackParam("notice",
                                    // "请办结完此次申报所有事项后，再办结里程碑事项！");
                                    return "请办结完此次申报所有事项后，再办结里程碑事项！";
                                }
                            }
                        }
                    }
                }
                ;
            }
        }
        // 告知承諾事項的判斷 去掉告知承诺办件办结逻辑
        String promise_status = auditProject.getStr("promise_status");
        /*
         * if(StringUtil.isNotBlank(promise_status)){
         * //如果是告知承诺制，设置为非必填必填，status：0承诺中 ，1已完成承诺，2，未完成
         * if(!ZwfwConstant.CONSTANT_STR_ONE.equals(promise_status)){ return
         * "办件属于承诺制事项申报，材料补齐后才能办结！"; } }
         */

        // edit by Dyt
//        // 非即办件或非备案件 30分钟内无法办结
//        if (auditTask.getType() != 1 && !"1".equals(auditTask.getStr("isbeian"))) {
//            if (StringUtil.isNotBlank(auditProject.getAcceptuserdate())) {
//                boolean flag = getDatePoor(auditProject.getAcceptuserdate(), new Date());
//                if (!flag) {
//                    if (auditTask.getType() != 1) {
//                        return "非即办件，30分钟内无法办结，请稍后重试！";
//                    }
//
//                    if (!"1".equals(auditTask.getStr("isbeian"))) {
//                        return "非备案件，30分钟内无法办结，请稍后重试！";
//                    }
//                }
//
//            }
//        }

        return "";
    }

    /**
     * 计算时间差有没有超过30分钟 [一句话功能简述]
     * 
     * @param startDate
     * @param endDate
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean getDatePoor(Date startDate, Date endDate) {

        /*
         * long nd = 1000 * 24 * 60 * 60; long nh = 1000 * 60 * 60; long nm =
         * 1000 * 60;
         */

        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        return diff > 1800000;
        // 计算差多少分钟
        // long min = diff % nd % nh / nm;

        // return min > 30;
    }

    /**
     *
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

}
