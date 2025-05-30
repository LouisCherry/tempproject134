package com.epoint.zwdt.zwdtrest.project;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditonlineuser.auditonlineverycode.inter.IAuditOnlineVeryCode;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmqqyjxxb;
import com.epoint.basic.spgl.inter.ISpglXmqqyjxxb;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdtutil.ZwdtUserAuthValid;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.util.JnBeanUtil;
import com.epoint.jnzwdt.auditsptasktype.api.IAuditSpTasktypeRService;
import com.epoint.jnzwdt.auditsptasktype.api.IAuditSpTasktypeService;
import com.epoint.jnzwdt.auditsptasktype.api.entity.AuditSpTasktype;
import com.epoint.powersupply.api.IAuditSpProjectinfoGdbzService;
import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.jgtaskmaterialrelation.api.IJgTaskmaterialrelationService;
import com.epoint.xmz.jgtaskmaterialrelation.api.entity.JgTaskmaterialrelation;
import com.epoint.xmz.task.commonapi.inter.ITaskCommonService;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.zwdt.zwdtrest.project.utils.GongYongShuiWuUtil;
import com.epoint.zwdt.zwdtrest.project.utils.ZhongShanShuiWuUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 并联审批相关接口
 *
 * @version [F9.3, 2017年10月28日]
 * @作者 xli
 */
@RestController
@RequestMapping("/zwdtItem")
public class AuditOnlineItemController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 企业基本信息API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;
    @Autowired
    private IAuditTaskExtension iaudittaskextension;
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 项目信息API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;
    /**
     * 子申报实例API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 并联审批主题实例
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    /**
     * 阶段申报事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditSpTasktypeRService iAuditSpTasktypeRService;
    @Autowired
    private IAuditSpTasktypeService iAuditSpTasktypeService;
    /**
     * 阶段申报材料实例API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    /**
     * 共享材料关系API
     */
    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;
    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 证照API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    /**
     * 证照附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;
    /**
     * 咨询投诉API
     */
    @Autowired
    private IAuditOnlineConsult iAuditOnlineConsult;
    /**
     * 部门API
     */
    @Autowired
    private IOuService iOuService;
    /**
     * 办件剩余时间API
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;

    /**
     * sp材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 共享材料API
     */
    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;
    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigservice;
    /**
     * 单位信息
     */
    @Autowired
    private IParticipantsInfoService iparticipantsInfoService;
    /**
     * 验证码API
     */
    @Autowired
    private IAuditOnlineVeryCode iAuditOnlineVeryCode;

    /**
     * 验证码API
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;

    /**
     * 验证码API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    /**
     * 验证码API
     */
    @Autowired
    private IAuditTaskResult iAuditTaskResult;
    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;
    /**
     * 项目前期意见表
     */
    @Autowired
    private ISpglXmqqyjxxb iSpglXmqqyjxxb;

    @Autowired
    private IAuditSpSpLxydghxkService iauditspsplxydghxkservice;

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;

    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IAuditSpSpJgysService iauditspspjgysservice;

    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IJgTaskmaterialrelationService relationService;

    @Autowired
    private IAuditSpITaskCorpService iAuditSpITaskCorpService;

    @Autowired
    private ITaskCommonService taskCommonService;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditTaskCase iAuditTaskCase;

    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    /**
     * 单体API
     */
    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;

    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private IAuditSpProjectinfoGdbzService auditSpProjectinfoGdbzService;

    @Autowired
    private ICxBusService cxBusService;
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    /**
     * 材料类型代码项
     */
    public static final String MATERIAL_TYPE_TSCL = "tscl";
    public static final String MATERIAL_TYPE_CHCL = "chcl";
    public static final String MATERIAL_TYPE_DPCL = "dpcl";
    public static final String MATERIAL_TYPE_LSCL = "lscl";

    /**********************************************
     * 项目相关接口开始
     **********************************************/

    /**
     * @Description: 获取材料列表分区县 @author male @date 2020年6月8日 上午8:52:03 @return String
     *               返回类型 @throws
     */
    @RequestMapping(value = "/getAuditSpProjectList", method = RequestMethod.POST)
    public String getAuditSpProjectList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAuditSpProjectList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");

            // 1.1、获取阶段
            String phaseGuid = obj.getString("phaseGuid");
            String subappGuid = obj.getString("subappGuid");
            String businessGuid = obj.getString("businessGuid");
            String type = obj.getString("type");

            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
            // 查询阶段数据
            AuditSpPhase auditspphase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("phaseguid", phaseGuid);
            List<AuditSpTask> auditSpListTemp = iAuditSpTask.getAllAuditSpTask(sql.getMap()).getResult();
            // 根据spbasetaskguid
            StringBuilder basetaskguids = new StringBuilder();
            basetaskguids.append("'");
            for (AuditSpTask auditSpTask : auditSpListTemp) {
                basetaskguids.append(auditSpTask.getBasetaskguid()).append("','");
            }
            basetaskguids.append("'");
            // 获取所有事项数据
            sql.clear();
            if (StringUtils.isNotBlank(basetaskguids.toString())) {
                sql.in("basetaskguid", basetaskguids.toString());
            }
            String areacode = obj.getString("areacode");
            if (areacode != null && !"ALL".equals(areacode)) {
                sql.eq("areacode", areacode);
            }
            List<AuditSpBasetaskR> listr = iAuditSpBasetaskR.getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
            // 获取所有事项的taskid
            List<String> taskids = new ArrayList<>();
            for (AuditSpBasetaskR auditspbasetaskr : listr) {
                if (!taskids.contains(auditspbasetaskr.getTaskid())) {
                    taskids.add(auditspbasetaskr.getTaskid());
                }
            }

            List<AuditTask> auditTasks = new ArrayList<>();
            // 获取spitask中存在的事项id
            // List<String> spitaskIdlist =
            // iAuditSpITask.getTaskIDBySubappGuid(subappGuid).getResult();
            // 去除删除或禁用的事项
            if (ValidateUtil.isNotBlankCollection(taskids)) {
                // 遍历spitask，去除taskids中已经评审的事项
                // for (String taskid : spitaskIdlist) {
                // if (taskids.contains(taskid)) {
                // taskids.remove(taskid);
                // }
                // }
                sql = new SqlConditionUtil();
                sql.eq("IS_EDITAFTERIMPORT", "1");
                sql.eq("IS_ENABLE", "1");
                sql.isBlankOrValue("IS_HISTORY", "0");

                if ("watch".equals(type)) {
                    if (StringUtils.isNotBlank(auditSpISubapp.getStr("wttaskids"))) {
                        sql.in("task_id", auditSpISubapp.getStr("wttaskids"));
                    }
                }
                else {
                    if (taskids != null && taskids.size() > 0) {
                        sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                    }
                }

                // sql.setOrder("areacode", "asc");
                sql.setSelectFields("task_id,taskname,rowguid,areacode,ouname");
                auditTasks = iAuditTask.getAllTask(sql.getMap()).getResult();
            }

            // 设置里程碑标识 //设置是否主线事项
            List<String> fxtaskids = iAuditSpBasetaskR.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
            for (AuditTask audittask : auditTasks) {

                AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(audittask.getTask_id())
                        .getResult();
                // 获取拓展
                // Record auditTaskTaianByTaskId =
                // daTing.getAuditTaskTaianByTaskId(audittask.getTask_id());
                // if (auditTaskTaianByTaskId != null) {
                // String xftype = auditTaskTaianByTaskId.getStr("xftype");
                // if (StringUtil.isBlank(xftype)) {
                // audittask.set("xftype", "0");
                // }
                // else {
                // audittask.set("xftype", xftype);
                // }
                // String isbdc = auditTaskTaianByTaskId.getStr("isbdc");
                // if (StringUtil.isNotBlank(isbdc)) {
                // audittask.set("isbdc", isbdc);
                // }
                // else {
                // audittask.set("isbdc", "0");
                // }
                // }
                // else {
                // audittask.set("xftype", "0");
                // audittask.set("isbdc", "0");
                // }

                // String is_rfzlde_task =
                // ConfigUtil.getConfigValue("epointframe", "is_rfzlde_task");
                // String isexisttask =
                // ConfigUtil.getFrameConfigValue("ydtaskids");
                // if (isexisttask.contains(audittask.getTask_id())) {
                // audittask.put("isexisttask", Boolean.valueOf(true));
                // }
                // else {
                // audittask.put("isexisttask", Boolean.valueOf(false));
                // }
                // if (is_rfzlde_task.indexOf(audittask.getTask_id()) != -1) {
                // audittask.put("taskid", audittask.getTask_id());
                // }
                if (basetask != null) {
                    audittask.set("sflcbsx", "1".equals(basetask.getSflcbsx()) ? "【里程碑事项】" : "");
                }
                if (fxtaskids.contains(audittask.getTask_id())) {
                    audittask.put("ztsxlx", "0");
                    audittask.put("isztsxlx", "【主线事项】");
                }
                else {
                    audittask.put("ztsxlx", "1");
                    audittask.put("isztsxlx", "");
                }
                // 设置辖区名称
                // if (StringUtil.isNotBlank(audittask.getAreacode())) {
                // audittask.put("areaname",
                // "【" + iCodeItemsService.getItemTextByCodeName("辖区对应关系",
                // audittask.getAreacode()) + "】");
                // }
                // 设置事项部门
                audittask.put("ouname", "【" + audittask.getOuname() + "】");

                // 给事项进行排序 add by wslong
                SqlConditionUtil sqlr = new SqlConditionUtil();
                sqlr.eq("taskid", audittask.getTask_id());
                List<AuditSpBasetaskR> auditSpBasetaskRList = iAuditSpBasetaskR
                        .getAuditSpBasetaskrByCondition(sqlr.getMap()).getResult();

                StringBuilder auditSpBasetaskRBuf = new StringBuilder();
                auditSpBasetaskRBuf.append("'");
                for (AuditSpBasetaskR auditSpBasetaskR : auditSpBasetaskRList) {
                    auditSpBasetaskRBuf.append(auditSpBasetaskR.getBasetaskguid()).append("','");

                }
                auditSpBasetaskRBuf.append("'");
                // 获取审批事项

                SqlConditionUtil sqltask = new SqlConditionUtil();
                sqltask.eq("phaseguid", phaseGuid);
                sqltask.in("basetaskguid", auditSpBasetaskRBuf.toString());
                List<AuditSpTask> AuditSpTasklist = iAuditSpTask.getAllAuditSpTask(sqltask.getMap()).getResult();
                if (AuditSpTasklist != null && !AuditSpTasklist.isEmpty()) {
                    if (StringUtil.isNotBlank(AuditSpTasklist.get(0).getOrdernumber())) {
                        audittask.put("paix", AuditSpTasklist.get(0).getOrdernumber());
                    }
                    else {
                        audittask.put("paix", 0);
                    }

                }
                else {
                    audittask.put("paix", 0);
                }

                // 获取事项表单放入事项信息中
                AuditTaskExtension audittaskextension = iaudittaskextension
                        .getTaskExtensionByTaskGuid(audittask.getRowguid(), false).getResult();
                if (audittaskextension != null && StringUtil.isNotBlank(audittaskextension.getStr("formid"))) {
                    audittask.put("ywformid", audittaskextension.getStr("formid"));
                }
                if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                    audittask.put("phaseformid", auditspphase.getStr("formid"));
                }

            }

            // 如果是简易项目去除泰安市
            // AuditSpBusiness auditSpBusiness =
            // iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
            // if (auditSpBusiness != null &&
            // "社会投资简易低风险工程（厂房、仓库）项目".equals(auditSpBusiness.getBusinessname()))
            // {
            // Iterator<AuditTask> iterator = auditTasks.iterator();
            // while (iterator.hasNext()) {
            // AuditTask auditTask = (AuditTask) iterator.next();
            // if ("【泰安市】".equals(auditTask.getStr("areaname"))) {
            // iterator.remove();
            // }
            // }
            // }

            JSONObject result = new JSONObject();
            // 根据中文拼音排序
            /*
             * auditTasks.sort((task1, task2) ->
             * Collator.getInstance(Locale.CHINESE).compare(task1.getTaskname(),
             * task2.getTaskname()));
             */

            // add by wslong
            // 排序
            result.put("projectname", auditTasks);
            // 展示勾选的事项
            List<String> taskIdlist = new ArrayList<String>();
            if (auditSpISubapp != null && !auditSpISubapp.isEmpty()) {
                taskIdlist = iAuditSpITask.getTaskIDBySubappGuid(subappGuid).getResult();
            }
            if (ValidateUtil.isNotBlankCollection(taskIdlist)) {
                StringBuffer task_ids = new StringBuffer();

                for (String task_id : taskIdlist) {
                    task_ids.append(task_id + ",");
                }
                result.put("taskids", task_ids.toString().substring(0, task_ids.toString().length() - 1));
                result.put("oldtaskids", task_ids.toString().substring(0, task_ids.toString().length() - 1));
            }
            else {
                result.put("taskids", "");
                result.put("oldtaskids", "");
            }
            if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                result.put("formid", auditspphase.getStr("formid"));
                result.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                result.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
            }
            return JsonUtils.zwdtRestReturn("1", "成功", result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 获取材料列表分区县 @author male @date 2020年6月8日 上午8:52:03 @return String
     *               返回类型 @throws
     */
    @RequestMapping(value = "/getAuditSpProjectListNew", method = RequestMethod.POST)
    public String getAuditSpProjectListNew(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAuditSpProjectList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");

            // 1.1、获取阶段
            String phaseGuid = obj.getString("phaseGuid");
            String subappGuid = obj.getString("subappGuid");
            String businessGuid = obj.getString("businessGuid");
            String type = obj.getString("type");

            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
            // 查询阶段数据
            AuditSpPhase auditspphase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("phaseguid", phaseGuid);
            List<AuditSpTask> auditSpListTemp = iAuditSpTask.getAllAuditSpTask(sql.getMap()).getResult();
            // 根据spbasetaskguid
            StringBuilder basetaskguids = new StringBuilder();
            basetaskguids.append("'");
            for (AuditSpTask auditSpTask : auditSpListTemp) {
                basetaskguids.append(auditSpTask.getBasetaskguid()).append("','");
            }
            basetaskguids.append("'");
            // 获取所有事项数据
            sql.clear();
            if (StringUtils.isNotBlank(basetaskguids.toString())) {
                sql.in("basetaskguid", basetaskguids.toString());
            }

            String areacode = obj.getString("areacode");
            if (areacode != null && !"ALL".equals(areacode)) {
                if ("370800".equals(areacode) && "4".equals(auditspphase.getPhaseId())) {
                    // 济宁竣工验收阶段下面展示：任城-经开-北湖--高新的事项
                    String[] array = new String[] {"370800", "370811", "370892", "370891", "370890" };
                    sql.in("areacode", StringUtil.joinSql(array));
                }
                else {
                    sql.eq("areacode", areacode);
                }
            }
            List<AuditSpBasetaskR> listr = iAuditSpBasetaskR.getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
            // 获取所有事项的taskid
            List<String> taskids = new ArrayList<>();
            for (AuditSpBasetaskR auditspbasetaskr : listr) {
                if (!taskids.contains(auditspbasetaskr.getTaskid())) {
                    taskids.add(auditspbasetaskr.getTaskid());
                }
            }

            List<AuditTask> auditTasks = new ArrayList<>();
            // 获取spitask中存在的事项id
            // List<String> spitaskIdlist =
            // iAuditSpITask.getTaskIDBySubappGuid(subappGuid).getResult();
            // 去除删除或禁用的事项
            if (ValidateUtil.isNotBlankCollection(taskids)) {
                // 遍历spitask，去除taskids中已经评审的事项
                // for (String taskid : spitaskIdlist) {
                // if (taskids.contains(taskid)) {
                // taskids.remove(taskid);
                // }
                // }
                sql = new SqlConditionUtil();
                sql.eq("IS_EDITAFTERIMPORT", "1");
                sql.eq("IS_ENABLE", "1");
                sql.isBlankOrValue("IS_HISTORY", "0");

                if ("watch".equals(type)) {
                    if (StringUtils.isNotBlank(auditSpISubapp.getStr("wttaskids"))) {
                        sql.in("task_id", auditSpISubapp.getStr("wttaskids"));
                    }
                }
                else {
                    if (taskids != null && taskids.size() > 0) {
                        sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                    }
                }
                // sql.setOrder("areacode", "asc");
                sql.setSelectFields("task_id,taskname,rowguid,areacode,ouname");
                auditTasks = iAuditTask.getAllTask(sql.getMap()).getResult();
            }

            // 设置里程碑标识 //设置是否主线事项
            List<String> fxtaskids = iAuditSpBasetaskR.gettaskidsByBusinedssguid(businessGuid, "5").getResult();
            for (AuditTask audittask : auditTasks) {

                AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(audittask.getTask_id())
                        .getResult();
                // 获取拓展
                // Record auditTaskTaianByTaskId =
                // daTing.getAuditTaskTaianByTaskId(audittask.getTask_id());
                // if (auditTaskTaianByTaskId != null) {
                // String xftype = auditTaskTaianByTaskId.getStr("xftype");
                // if (StringUtil.isBlank(xftype)) {
                // audittask.set("xftype", "0");
                // }
                // else {
                // audittask.set("xftype", xftype);
                // }
                // String isbdc = auditTaskTaianByTaskId.getStr("isbdc");
                // if (StringUtil.isNotBlank(isbdc)) {
                // audittask.set("isbdc", isbdc);
                // }
                // else {
                // audittask.set("isbdc", "0");
                // }
                // }
                // else {
                // audittask.set("xftype", "0");
                // audittask.set("isbdc", "0");
                // }

                // String is_rfzlde_task =
                // ConfigUtil.getConfigValue("epointframe", "is_rfzlde_task");
                // String isexisttask =
                // ConfigUtil.getFrameConfigValue("ydtaskids");
                // if (isexisttask.contains(audittask.getTask_id())) {
                // audittask.put("isexisttask", Boolean.valueOf(true));
                // }
                // else {
                // audittask.put("isexisttask", Boolean.valueOf(false));
                // }
                // if (is_rfzlde_task.indexOf(audittask.getTask_id()) != -1) {
                // audittask.put("taskid", audittask.getTask_id());
                // }
                if (basetask != null) {
                    audittask.set("sflcbsx", "1".equals(basetask.getSflcbsx()) ? "【里程碑事项】" : "");
                }
                if (fxtaskids.contains(audittask.getTask_id())) {
                    audittask.put("ztsxlx", "0");
                    audittask.put("isztsxlx", "【主线事项】");
                }
                else {
                    audittask.put("ztsxlx", "1");
                    audittask.put("isztsxlx", "");
                }
                // 设置辖区名称
                // if (StringUtil.isNotBlank(audittask.getAreacode())) {
                // audittask.put("areaname",
                // "【" + iCodeItemsService.getItemTextByCodeName("辖区对应关系",
                // audittask.getAreacode()) + "】");
                // }
                // 设置事项部门
                audittask.put("ouname", "【" + audittask.getOuname() + "】");

                // 给事项进行排序 add by wslong
                SqlConditionUtil sqlr = new SqlConditionUtil();
                sqlr.eq("taskid", audittask.getTask_id());
                List<AuditSpBasetaskR> auditSpBasetaskRList = iAuditSpBasetaskR
                        .getAuditSpBasetaskrByCondition(sqlr.getMap()).getResult();

                StringBuilder auditSpBasetaskRBuf = new StringBuilder();
                auditSpBasetaskRBuf.append("'");
                for (AuditSpBasetaskR auditSpBasetaskR : auditSpBasetaskRList) {
                    auditSpBasetaskRBuf.append(auditSpBasetaskR.getBasetaskguid()).append("','");

                }
                auditSpBasetaskRBuf.append("'");
                // 获取审批事项

                SqlConditionUtil sqltask = new SqlConditionUtil();
                sqltask.eq("phaseguid", phaseGuid);
                sqltask.in("basetaskguid", auditSpBasetaskRBuf.toString());
                List<AuditSpTask> AuditSpTasklist = iAuditSpTask.getAllAuditSpTask(sqltask.getMap()).getResult();
                if (AuditSpTasklist != null && !AuditSpTasklist.isEmpty()) {
                    if (StringUtil.isNotBlank(AuditSpTasklist.get(0).getOrdernumber())) {
                        audittask.put("paix", AuditSpTasklist.get(0).getOrdernumber());
                    }
                    else {
                        audittask.put("paix", 0);
                    }

                }
                else {
                    audittask.put("paix", 0);
                }

                // 获取事项表单放入事项信息中
                AuditTaskExtension audittaskextension = iaudittaskextension
                        .getTaskExtensionByTaskGuid(audittask.getRowguid(), false).getResult();
                if (audittaskextension != null && StringUtil.isNotBlank(audittaskextension.getStr("formid"))) {
                    audittask.put("ywformid", audittaskextension.getStr("formid"));
                }
                if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                    audittask.put("phaseformid", auditspphase.getStr("formid"));
                }

            }

            List<String> taskIds = auditTasks.stream().map(AuditTask::getTask_id).collect(Collectors.toList());
            // 查询关联的事项分类
            List<AuditSpTasktype> tasktypes = iAuditSpTasktypeService.findByTaskId(taskids, auditspphase.getPhaseId());
            List<AuditSpTasktype> taskTypeList = new ArrayList<>();
            Map<String, List<AuditTask>> taskMap = new HashMap<>();
            for (AuditSpTasktype tasktype : tasktypes) {
                for (AuditTask auditTask : auditTasks) {
                    if (tasktype.getStr("taskid").equals(auditTask.getTask_id())) {
                        taskMap.computeIfAbsent(tasktype.getRowguid(), k -> new ArrayList<>()).add(auditTask);
                    }
                }
            }

            List<AuditTask> finalAuditTasks = auditTasks;
            taskMap.forEach((tasktypeguid, childtask) -> {
                AuditSpTasktype auditSpTasktype = iAuditSpTasktypeService.find(tasktypeguid);
                auditSpTasktype.set("child", childtask);
                taskTypeList.add(auditSpTasktype);
                int i = 1;
                for (AuditTask auditTask : childtask) {
                    finalAuditTasks.removeIf(auditTask1 -> auditTask1.getTask_id().equals(auditTask.getTask_id()));
                    auditTask.set("key", i++);
                }
            });

            // 如果是简易项目去除泰安市
            // AuditSpBusiness auditSpBusiness =
            // iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
            // if (auditSpBusiness != null &&
            // "社会投资简易低风险工程（厂房、仓库）项目".equals(auditSpBusiness.getBusinessname()))
            // {
            // Iterator<AuditTask> iterator = auditTasks.iterator();
            // while (iterator.hasNext()) {
            // AuditTask auditTask = (AuditTask) iterator.next();
            // if ("【泰安市】".equals(auditTask.getStr("areaname"))) {
            // iterator.remove();
            // }
            // }
            // }

            JSONObject result = new JSONObject();
            // 根据中文拼音排序
            /*
             * auditTasks.sort((task1, task2) ->
             * Collator.getInstance(Locale.CHINESE).compare(task1.getTaskname(),
             * task2.getTaskname()));
             */

            // add by wslong
            // 排序
            result.put("projectname", finalAuditTasks);
            result.put("tasktypes", taskTypeList);
            // 展示勾选的事项
            List<String> taskIdlist = new ArrayList<String>();
            if (auditSpISubapp != null && !auditSpISubapp.isEmpty()) {
                taskIdlist = iAuditSpITask.getTaskIDBySubappGuid(subappGuid).getResult();
            }
            if (ValidateUtil.isNotBlankCollection(taskIdlist)) {
                StringBuffer task_ids = new StringBuffer();

                for (String task_id : taskIdlist) {
                    task_ids.append(task_id + ",");
                }
                result.put("taskids", task_ids.toString().substring(0, task_ids.toString().length() - 1));
                result.put("oldtaskids", task_ids.toString().substring(0, task_ids.toString().length() - 1));
            }
            else {
                result.put("taskids", "");
                result.put("oldtaskids", "");
            }
            if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                result.put("formid", auditspphase.getStr("formid"));
                result.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                result.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
            }
            return JsonUtils.zwdtRestReturn("1", "成功", result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getTaskids", method = RequestMethod.POST)
    public String getTaskids(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskids接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject obj = (JSONObject) jsonObject.get("params");

            // 1.1、获取阶段
            String subappGuid = obj.getString("subappGuid");
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();

            JSONObject result = new JSONObject();

            // 展示勾选的事项
            List<String> taskIdlist = new ArrayList<String>();
            if (auditSpISubapp != null && !auditSpISubapp.isEmpty()) {
                taskIdlist = iAuditSpITask.getTaskIDBySubappGuid(subappGuid).getResult();
            }
            if (ValidateUtil.isNotBlankCollection(taskIdlist)) {
                StringBuffer task_ids = new StringBuffer();

                for (String task_id : taskIdlist) {
                    task_ids.append(task_id + ",");
                }
                result.put("taskids", task_ids.toString().substring(0, task_ids.toString().length() - 1));
            }
            else {
                result.put("taskids", "");
            }
            return JsonUtils.zwdtRestReturn("1", "成功", result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败" + e.getMessage(), "");
        }
    }

    /**
     * * 获取策划生成项目意见列表
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getXmqqyj", method = RequestMethod.POST)
    public String getXmqqyj(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getXmqqyj接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、前期意见项目代码
                String qqyjcode = obj.getString("chcode");
                // 1.1、前期意见项目代码
                String pagesize = obj.getString("pagesize");
                // 1.1、前期意见项目代码
                String pageindex = obj.getString("pageindex");
                // 2、查询前期意见表
                PageData<SpglXmqqyjxxb> spglXmqqyjbPageData = iSpglXmqqyjxxb.getQqyjListByPage(qqyjcode,
                        Integer.valueOf(pagesize) * Integer.valueOf(pageindex), Integer.valueOf(pageindex));
                List<SpglXmqqyjxxb> spglXmqqyjbList = spglXmqqyjbPageData.getList();
                // 3、返回数据
                List<JSONObject> xmqqyjbJSONList = new ArrayList<JSONObject>();
                for (SpglXmqqyjxxb spglXmqqyjxxb : spglXmqqyjbList) {
                    JSONObject xmqqyjbJson = new JSONObject();
                    xmqqyjbJson.put("ouname", spglXmqqyjxxb.getBldwmc());
                    xmqqyjbJson.put("qqyj", spglXmqqyjxxb.getQqyj());
                    xmqqyjbJson.put("blr", spglXmqqyjxxb.getBlr());
                    String attachguid = spglXmqqyjxxb.getFjid();
                    if (StringUtil.isNotBlank(attachguid)) {
                        xmqqyjbJson.put("fjmc", spglXmqqyjxxb.getFjmc());
                        xmqqyjbJson.put("fjid", attachguid);
                        String attachurl = WebUtil.getRequestCompleteUrl(request)
                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                + attachguid;
                        xmqqyjbJson.put("attachurl", attachurl);
                    }
                    xmqqyjbJSONList.add(xmqqyjbJson);
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("xmqqyjbjsonlist", xmqqyjbJSONList);
                dataJson.put("total", spglXmqqyjbPageData.getRowCount());
                log.info("=======结束调用getXmqqyj接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取项目前期意见成功", dataJson);
            }
            else {
                log.info("=======结束调用getXmqqyj接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======调用getXmqqyj接口异常=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目前期意见异常", "");
        }
    }

    /**
     * * 获取策划生成项目列表
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getAuditDgChsqList", method = RequestMethod.POST)
    public String getAuditDgChsqList(@RequestBody String params) {
        try {
            log.info("=======开始调用getAuditDgChsqList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、当前页码
                String currentpage = obj.getString("currentpage");
                // 1.2、页面大小
                String pagesize = obj.getString("pagesize");
                // 1.3、项目标识
                String itemguid = obj.getString("itemguid");
                // 1.4、测绘项目标识
                String chcode = obj.getString("chcode");
                // 2、查询项目基本信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid)
                        .getResult();
                if (auditRsItemBaseinfo != null) {
                    // 3.1、是否开启对接策划系统
                    String chSwitch = iConfigService.getFrameConfigValue("AUDIT_CHSWITCH");
                    if (StringUtil.isNotBlank(chSwitch) && ZwfwConstant.CONSTANT_STR_ONE.equals(chSwitch)) {
                        // 3.2、获取策划系统接口地址
                        String requestUrl = iConfigService.getFrameConfigValue("AUDIT_CHPROJECTURL");
                        if (StringUtil.isNotBlank(requestUrl)) {
                            // 4.1、发送请求：获取策划生成项目
                            JSONObject postJson = new JSONObject();
                            JSONObject paramsJson = new JSONObject();
                            paramsJson.put("currentpage", currentpage);
                            paramsJson.put("pagesize", pagesize);
                            paramsJson.put("certnumber", auditRsItemBaseinfo.getItemlegalcertnum());
                            postJson.put("token", ZwdtConstant.SysValidateData);
                            postJson.put("params", paramsJson);
                            List<JSONObject> chitems = new ArrayList<>();
                            Integer total = 0;
                            String returnMsg = HttpUtil.doPostJson(requestUrl, postJson.toString());
                            // 4.2、解析请求值
                            if (StringUtil.isNotBlank(returnMsg)) {
                                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                                JSONObject objcustom = (JSONObject) objReturn.get("custom");
                                JSONArray projectList = objcustom.getJSONArray("projectlist");
                                total = objcustom.getInteger("total");
                                if (projectList != null && !projectList.isEmpty()) {
                                    for (int i = 0; i < projectList.size(); i++) {
                                        JSONObject projectJson = projectList.getJSONObject(i);
                                        JSONObject chitem = new JSONObject();
                                        chitem.put("chname", projectJson.get("projectname"));
                                        chitem.put("chcode", projectJson.get("projectnum"));
                                        if (StringUtil.isNotBlank(chcode) && chcode.equals(chitem.get("chcode"))) {
                                            chitem.put("checked", true);
                                        }
                                        chitems.add(chitem);
                                    }
                                }
                            }
                            JSONObject dataJson = new JSONObject();
                            dataJson.put("chitems", chitems);
                            dataJson.put("total", total);
                            log.info("=======结束调用getAuditDgChsqList接口=======");
                            return JsonUtils.zwdtRestReturn("1", "获取策划生成项目列表成功！", dataJson);
                        }
                        else {
                            log.info("=======结束调用getAuditDgChsqList接口=======");
                            return JsonUtils.zwdtRestReturn("0", "未配置对接策划系统地址", "");
                        }
                    }
                    else {
                        log.info("=======结束调用getAuditDgChsqList接口=======");
                        return JsonUtils.zwdtRestReturn("0", "未开启对接策划系统", "");
                    }
                }
                else {
                    log.info("=======结束调用getAuditDgChsqList接口=======");
                    return JsonUtils.zwdtRestReturn("0", "获取项目信息失败", "");
                }
            }
            else {
                log.info("=======结束调用getAuditDgChsqList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======调用getAuditDgChsqList接口异常=======");
            return JsonUtils.zwdtRestReturn("0", "获取策划生成项目列表", "");
        }
    }

    /**
     * 获取我的企业列表（选择项目弹窗、项目管理列表筛选调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMyCompanyList", method = RequestMethod.POST)
    public String getMyCompanyList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCompanyList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                // JSONObject obj = (JSONObject) jsonObject.get("params");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户身份证
                    String idNumber = auditOnlineRegister.getIdnumber();
                    // 3、 获取用户对应的法人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(auditOnlineRegister.getUsertype())) {
                        sqlConditionUtil.eq("orgalegal_idnumber", idNumber);
                    }
                    else {
                        if (StringUtil.isNotBlank(auditOnlineRegister.getCompanyidnumber())) {
                            if (auditOnlineRegister.getCompanyidnumber().length() == 18) {
                                sqlConditionUtil.eq("creditcode", auditOnlineRegister.getCompanyidnumber());
                            }
                            else {
                                sqlConditionUtil.eq("organcode", auditOnlineRegister.getCompanyidnumber());
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "企业标识为空", "");
                        }
                    }
                    sqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                    sqlConditionUtil.eq("isactivated", "1");
                    sqlConditionUtil.setOrderDesc("registerdate"); // 按注册时间倒序排列
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    List<JSONObject> myCompanyList = new ArrayList<JSONObject>();
                    // 4、遍历用户对应的所有法人信息
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        // 4.1、获取各个企业的法人信息，存入返回列表中
                        JSONObject myCompanyJson = new JSONObject();
                        myCompanyJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                        myCompanyJson.put("companyid", auditRsCompanyBaseinfo.getCompanyid());
                        myCompanyJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        myCompanyJson.put("creditcode",
                                StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())
                                        ? auditRsCompanyBaseinfo.getCreditcode()
                                        : auditRsCompanyBaseinfo.getOrgancode());
                        myCompanyJson.put("organcode", auditRsCompanyBaseinfo.getOrgancode());
                        // 4.5、添加信息至返回列表
                        myCompanyList.add(myCompanyJson);
                    }
                    // 5、获取用户所对应的管理者信息， 代办人信息
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    conditionUtil.eq("bsqidnum", idNumber);
                    conditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                    // 0-否，1-是
                    conditionUtil.eq("m_isactive", "1"); // 代办人或管理者身份已激活
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(conditionUtil.getMap()).getResult();
                    // 5.1 遍历授权信息，获取用户作为管理者或代办人所在企业的信息
                    String strWhere = "";
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        String companyId = auditOnlineCompanyGrant.getCompanyid();
                        strWhere += "'" + companyId + "',";
                    }
                    if (StringUtil.isNotBlank(strWhere)) {
                        SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                        grantConditionUtil.in("companyid", strWhere.substring(0, strWhere.length() - 1));
                        grantConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        grantConditionUtil.eq("isactivated", "1");
                        // 5.1.1 查询企业信息
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos2 = iAuditRsCompanyBaseinfo
                                .selectAuditRsCompanyBaseinfoByCondition(grantConditionUtil.getMap()).getResult();
                        for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos2) {
                            // 5.1.2、获取各个企业的法人信息，存入返回列表中
                            JSONObject myCompanyJson = new JSONObject();
                            myCompanyJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                            myCompanyJson.put("companyid", auditRsCompanyBaseinfo.getCompanyid());
                            myCompanyJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                            myCompanyJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
                            myCompanyJson.put("organcode", auditRsCompanyBaseinfo.getOrgancode());
                            // 5.1.3、添加信息至返回列表
                            myCompanyList.add(myCompanyJson);
                        }
                    }

                    // 6、返回结果数据
                    JSONObject dataJson = new JSONObject();
                    // 6.1、企业列表数据
                    dataJson.put("companylist", myCompanyList);
                    log.info("=======结束调用getMyCompanyList接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取用户所在企业信息成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getMyCompanyList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyCompanyList接口参数：params【" + params + "】=======");
            log.info("=======getMyCompanyList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户所在企业信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的企业下的项目列表（选择项目弹窗选择企业后筛选调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanyItemList", method = RequestMethod.POST)
    public String getCompanyItemList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyItemList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 获取统一社会信用代码
                String creditCode = obj.getString("creditcode");
                // 1.2、 获取搜索条件（项目名称）
                String keyWord = obj.getString("keyword");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    // 2.1 拼接查询条件
                    if (StringUtil.isNotBlank(keyWord)) {
                        conditionUtil.like("itemname", keyWord);
                    }
                    conditionUtil.eq("itemlegalcertnum", creditCode);
                    conditionUtil.isBlank("parentid");
                    // 3、 获取项目信息
                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                            .selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
                    List<JSONObject> companyItemList = new ArrayList<JSONObject>();
                    if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                        // 3.1、 将项目信息返回
                        for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                            JSONObject itemJson = new JSONObject();
                            itemJson.put("itemname", auditRsItemBaseinfo.getItemname());
                            itemJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                            itemJson.put("departname", auditRsItemBaseinfo.getItemlegaldept());
                            itemJson.put("itemguid", auditRsItemBaseinfo.getRowguid());
                            companyItemList.add(itemJson);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("itemlist", companyItemList);
                    log.info("=======结束调用getCompanyItemList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的企业下的项目列表成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getCompanyItemList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCompanyItemList接口参数：params【" + params + "】=======");
            log.info("=======getCompanyItemList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的企业下的项目列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我所在企业下的项目列表（项目管理页面选择企业后筛选调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMyItemListByCondition", method = RequestMethod.POST)
    public String getMyItemListByCondition(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyItemListByCondition接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目名称搜索关键字
                String keyWord = obj.getString("keyword");
                // 1.2、获取企业统一社会信用代码
                String creditCode = obj.getString("creditcode");
                // 1.3、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.4、获取当前页数
                String currentPage = obj.getString("currentpage");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取用户身份是代办人或者管理者的企业ID
                    SqlConditionUtil grantSqlConditionUtil = new SqlConditionUtil();
                    grantSqlConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantSqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    grantSqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                    // 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(grantSqlConditionUtil.getMap()).getResult();
                    String strWhereCompanyId = "";// 拼接被授权的所有企业id
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                    }
                    // 4、获取用户身份是代办人或者管理者的企业ID
                    SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(auditOnlineRegister.getUsertype())) {
                        legalSqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    }
                    else {
                        if (StringUtil.isNotBlank(auditOnlineRegister.getCompanyidnumber())) {
                            if (auditOnlineRegister.getCompanyidnumber().length() == 18) {
                                legalSqlConditionUtil.eq("creditcode", auditOnlineRegister.getCompanyidnumber());
                            }
                            else {
                                legalSqlConditionUtil.eq("organcode", auditOnlineRegister.getCompanyidnumber());
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "企业标识为空", "");
                        }
                    }
                    legalSqlConditionUtil.eq("is_history", "0");
                    legalSqlConditionUtil.eq("isactivated", "1");
                    // 查询当前用户作为法人的企业信息
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
                    String strInCreditCode = "";
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                            strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                        }
                        else {
                            strInCreditCode += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";
                        }
                    }
                    if (StringUtil.isNotBlank(strWhereCompanyId) || StringUtil.isNotBlank(strInCreditCode)) {
                        // 把拼接的where条件最后一个“,”去掉
                        if (StringUtil.isNotBlank(strWhereCompanyId)) {
                            strWhereCompanyId = strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1);
                            // 根据企业companyid查询出非历史版本被激活的企业的信用代码
                            SqlConditionUtil sqlSelectCompanyUtil = new SqlConditionUtil();
                            sqlSelectCompanyUtil.in("companyid", strWhereCompanyId);
                            sqlSelectCompanyUtil.isBlankOrValue("is_history", "0");
                            sqlSelectCompanyUtil.eq("isactivated", "1");
                            List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                                    .selectAuditRsCompanyBaseinfoByCondition(sqlSelectCompanyUtil.getMap()).getResult();
                            for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                                    strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                                }
                                else if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                                    strInCreditCode += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";
                                }
                            }
                        }
                        if (StringUtil.isNotBlank(strInCreditCode)) {
                            strInCreditCode = strInCreditCode.substring(0, strInCreditCode.length() - 1);
                        }
                    }
                    else {
                        // 如果登陆人没有被授权的企业或不是法人，则提示“无权”
                        return JsonUtils.zwdtRestReturn("0", "您无权查询项目", "");
                    }
                    List<JSONObject> itemList = new ArrayList<>();
                    Integer totalCount = 0;
                    if (StringUtil.isNotBlank(strInCreditCode)) {
                        SqlConditionUtil conditionUtil = new SqlConditionUtil();
                        // 2.1 拼接查询条件
                        if (StringUtil.isNotBlank(keyWord)) {
                            conditionUtil.like("itemname", keyWord);
                        }
                        if (StringUtil.isNotBlank(creditCode)) {
                            conditionUtil.eq("itemlegalcertnum", creditCode);
                        }
                        conditionUtil.in("itemlegalcertnum", strInCreditCode);
                        conditionUtil.isBlank("parentid");
                        // 3、 获取项目信息
                        PageData<AuditRsItemBaseinfo> auditRsItemBaseinfosPageData = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(),
                                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                        Integer.parseInt(pageSize), "", "")
                                .getResult();
                        totalCount = auditRsItemBaseinfosPageData.getRowCount();
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
                        if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                            // 3.1、 将项目信息返回
                            for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                                JSONObject itemJson = new JSONObject();
                                itemJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                itemJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目代码
                                itemJson.put("itemguid", auditRsItemBaseinfo.getRowguid());
                                itemJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());// 建设单位
                                itemJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                                String biGuid = auditRsItemBaseinfo.getBiguid();
                                List<JSONObject> phaseList = new ArrayList<>();
                                if (StringUtil.isNotBlank(biGuid)) {
                                    // 根据申报实例查询套餐阶段
                                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid)
                                            .getResult();
                                    // 查询实例所有子申报
                                    List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                            .getResult();
                                    if (auditSpInstance != null) {
                                        String businessguid = auditSpInstance.getBusinessguid();// 主题guid
                                        SqlConditionUtil sqlSelectPhase = new SqlConditionUtil();
                                        sqlSelectPhase.eq("businedssguid", businessguid);
                                        sqlSelectPhase.setOrderDesc("ordernumber");
                                        // 查询出该主题的所有阶段
                                        List<AuditSpPhase> auditSpPhaseList = iAuditSpPhase
                                                .getAuditSpPhase(sqlSelectPhase.getMap()).getResult();
                                        int key = 0;
                                        for (AuditSpPhase auditSpPhase : auditSpPhaseList) {
                                            int i = 0;
                                            // 查询子申报的数量
                                            for (AuditSpISubapp auditSpISubapp : auditSpISubappList) {
                                                if (auditSpISubapp.getPhaseguid().equals(auditSpPhase.getRowguid())) {
                                                    // 当前子申报数量+1
                                                    i++;
                                                }
                                            }
                                            JSONObject objPhase = new JSONObject();
                                            objPhase.put("key", key++);// 用于控制前台项目列表中的阶段样式
                                            objPhase.put("phasename", i == 0 ? auditSpPhase.getPhasename()
                                                    : auditSpPhase.getPhasename() + "(" + i + ")");
                                            objPhase.put("phasesubappcount", i);
                                            phaseList.add(objPhase);
                                        }
                                        itemJson.put("businessguid", businessguid);
                                    }
                                }
                                itemJson.put("phaselist", phaseList);
                                itemList.add(itemJson);
                            }
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("itemlist", itemList);
                    dataJson.put("totalcount", totalCount);
                    log.info("=======结束调用getMyItemListByCondition接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我所在企业下的项目列表成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getMyItemListByCondition接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyItemListByCondition接口参数：params【" + params + "】=======");
            log.info("=======getMyItemListByCondition异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我所在企业下的项目列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据项目代码或者项目标识获取项目信息（点击项目查看或者申报按钮时调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanyItemInfo", method = RequestMethod.POST)
    public String getCompanyItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyItemInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid)
                                .getResult();
                    }
                    else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("itemcode", itemCode);
                        sqlConditionUtil.isBlankOrValue("is_history", "0");
                        // 3.1、根据项目代码和项目名称查询项目信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                                .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditRsItemBaseinfos.size() > 0) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }
                    }
                    if (auditRsItemBaseinfo != null) {
                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isNotBlank(itemLegalCreditCode)) {
                            // 3.1.2.1、 查询出这家企业
                            SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                            if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                companySqlConditionUtil.eq("creditcode", itemLegalCreditCode);
                            }
                            else {
                                companySqlConditionUtil.eq("organcode", itemLegalCreditCode);
                            }
                            companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                            companySqlConditionUtil.eq("isactivated", "1");
                            List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                                    .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                    .getResult();
                            if (auditRsCompanyBaseinfos != null && auditRsCompanyBaseinfos.size() > 0) {
                                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                                // 3.1.2.2、 获取企业法人证件号
                                String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                                // 3.1.2.3、 获取企业id
                                String companyId = auditRsCompanyBaseinfo.getCompanyid();
                                // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                                SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                                grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                                grantConditionUtil.eq("companyid", companyId);
                                grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                                // 0-否，1-是
                                List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                                        .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                                if (idNum.equals(auditOnlineRegister.getIdnumber())
                                        || auditOnlineCompanyGrants.size() > 0) {
                                    JSONObject dataJson = new JSONObject();
                                    // 如果biguid不为空，则返回主题
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getBiguid())) {
                                        AuditSpInstance auditSpInstance = iAuditSpInstance
                                                .getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                                        if (auditSpInstance != null) {
                                            AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                                    .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid())
                                                    .getResult();
                                            if (auditSpBusiness != null) {
                                                dataJson.put("businessname", auditSpBusiness.getBusinessname());
                                            }
                                        }
                                    }
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                                    List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName("项目类型");
                                    List<JSONObject> itemTypeList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getItemtype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        itemTypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : itemtypes) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemtype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        itemTypeList.add(objJson);
                                    }
                                    dataJson.put("itemtype", itemTypeList);
                                    List<CodeItems> constructionpropertys = iCodeItemsService
                                            .listCodeItemsByCodeName("建设性质");
                                    List<JSONObject> constructionpropertyList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        constructionpropertyList.add(objJson);
                                    }
                                    for (CodeItems codeItems : constructionpropertys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getConstructionproperty())) {
                                            objJson.put("isselected", 1);
                                        }
                                        constructionpropertyList.add(objJson);
                                    }
                                    dataJson.put("constructionproperty", constructionpropertyList);
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(
                                            auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(
                                            auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
                                    dataJson.put("totalinvest", auditRsItemBaseinfo.getTotalinvest());
                                    dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj());
                                    List<CodeItems> belongtindustrys = iCodeItemsService
                                            .listCodeItemsByCodeName("所属行业");
                                    List<JSONObject> belongtindustryList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getBelongtindustry())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        belongtindustryList.add(objJson);
                                    }
                                    for (CodeItems codeItems : belongtindustrys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getBelongtindustry())) {
                                            objJson.put("isselected", 1);
                                        }
                                        belongtindustryList.add(objJson);
                                    }
                                    dataJson.put("belongtindustry", belongtindustryList);
                                    List<CodeItems> shiFouCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                                    List<JSONObject> isimprovementList = new ArrayList<>();
                                    for (CodeItems codeItems : shiFouCode) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是否技改项目为配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getIsimprovement())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getIsimprovement())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        isimprovementList.add(objJson);
                                    }
                                    dataJson.put("isimprovement", isimprovementList);
                                    dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
                                    dataJson.put("newlandarea", auditRsItemBaseinfo.getNewlandarea());
                                    dataJson.put("agriculturallandarea", auditRsItemBaseinfo.getAgriculturallandarea());
                                    dataJson.put("itemcapital", auditRsItemBaseinfo.getItemcapital());
                                    List<CodeItems> xmtzlyItem = iCodeItemsService.listCodeItemsByCodeName("项目投资来源");
                                    List<JSONObject> xmtzlyList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getXmtzly())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        xmtzlyList.add(objJson);
                                    }
                                    for (CodeItems codeItems : xmtzlyItem) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(StringUtil.isNotBlank(auditRsItemBaseinfo.getXmtzly()))) {
                                            objJson.put("isselected", 1);
                                        }
                                        xmtzlyList.add(objJson);
                                    }
                                    dataJson.put("xmtzly", xmtzlyList); // 项目投资来源
                                    List<CodeItems> tdhqfsItem = iCodeItemsService.listCodeItemsByCodeName("土地获取方式");
                                    List<JSONObject> tdhqfsList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getTdhqfs())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        tdhqfsList.add(objJson);
                                    }
                                    for (CodeItems codeItems : tdhqfsItem) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getTdhqfs())) {
                                            objJson.put("isselected", 1);
                                        }
                                        tdhqfsList.add(objJson);
                                    }
                                    dataJson.put("tdhqfs", tdhqfsList); // 土地获取方式
                                    List<JSONObject> tdsfdsjfaList = new ArrayList<>();
                                    for (CodeItems codeItems : shiFouCode) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是土地是否带设计方案未配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getTdsfdsjfa())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getTdsfdsjfa().toString())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        tdsfdsjfaList.add(objJson);
                                    }
                                    dataJson.put("tdsfdsjfa", tdsfdsjfaList); // 土地是否带设计方案
                                    List<JSONObject> sfwcqypgList = new ArrayList<>();
                                    for (CodeItems codeItems : shiFouCode) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是否技改项目为配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getSfwcqypg())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getSfwcqypg().toString())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        sfwcqypgList.add(objJson);
                                    }
                                    dataJson.put("sfwcqypg", sfwcqypgList); // 是否完成区域评估
                                    dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj()); // 建筑面积
                                    List<CodeItems> fundsourcess = iCodeItemsService.listCodeItemsByCodeName("资金来源");
                                    List<JSONObject> fundsourcesList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getFundsources())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        fundsourcesList.add(objJson);
                                    }
                                    for (CodeItems codeItems : fundsourcess) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getFundsources())) {
                                            objJson.put("isselected", 1);
                                        }
                                        fundsourcesList.add(objJson);
                                    }
                                    dataJson.put("fundsources", fundsourcesList);

                                    List<CodeItems> xmzjsx = iCodeItemsService.listCodeItemsByCodeName("项目资金属性");
                                    List<JSONObject> xmzjsxList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getXmzjsx())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        xmzjsxList.add(objJson);
                                    }
                                    for (CodeItems codeItems : xmzjsx) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getXmzjsx())) {
                                            objJson.put("isselected", 1);
                                        }
                                        xmzjsxList.add(objJson);
                                    }
                                    dataJson.put("xmzjsx", xmzjsxList);

                                    List<CodeItems> financialresourcess = iCodeItemsService
                                            .listCodeItemsByCodeName("财政资金来源");
                                    List<JSONObject> financialresourcesList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getFinancialresources())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        financialresourcesList.add(objJson);
                                    }
                                    for (CodeItems codeItems : financialresourcess) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getFinancialresources())) {
                                            objJson.put("isselected", 1);
                                        }
                                        financialresourcesList.add(objJson);
                                    }
                                    dataJson.put("financialresources", financialresourcesList);
                                    List<CodeItems> quantifyconstructtypes = iCodeItemsService
                                            .listCodeItemsByCodeName("量化建设规模的类别");
                                    List<JSONObject> quantifyconstructtypeList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getQuantifyconstructtype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        quantifyconstructtypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : quantifyconstructtypes) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getQuantifyconstructtype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        quantifyconstructtypeList.add(objJson);
                                    }
                                    dataJson.put("quantifyconstructtype", quantifyconstructtypeList);
                                    dataJson.put("quantifyconstructcount",
                                            auditRsItemBaseinfo.getQuantifyconstructcount());
                                    dataJson.put("quantifyconstructdept",
                                            auditRsItemBaseinfo.getQuantifyconstructdept());
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                                    dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                                    dataJson.put("constructionscaleanddesc",
                                            auditRsItemBaseinfo.getConstructionscaleanddesc());
                                    dataJson.put("departname", auditRsItemBaseinfo.getDepartname());
                                    List<CodeItems> legalpropertys = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                                    List<JSONObject> legalpropertyList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getLegalproperty())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        legalpropertyList.add(objJson);
                                    }
                                    for (CodeItems codeItems : legalpropertys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getLegalproperty())) {
                                            objJson.put("isselected", 1);
                                        }
                                        legalpropertyList.add(objJson);
                                    }
                                    dataJson.put("legalproperty", legalpropertyList);
                                    dataJson.put("constructionaddress", auditRsItemBaseinfo.getConstructionaddress());
                                    dataJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                                    dataJson.put("itemlegalcreditcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());
                                    List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                            .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                                    List<JSONObject> itemlegalcerttypeList = new ArrayList<>();
                                    // 若证件类型未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        itemlegalcerttypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : itemlegalcerttypes) {
                                        // 代码项中只保留组织机构代码证和统一社会信用代码。与内网保持一致
                                        if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                                .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                            continue;
                                        }
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        itemlegalcerttypeList.add(objJson);
                                    }
                                    dataJson.put("itemlegalcerttype", itemlegalcerttypeList);
                                    // 国标行业
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGbhy())) {
                                        dataJson.put("gbhy", iCodeItemsService.getItemTextByCodeName("国标行业2017",
                                                auditRsItemBaseinfo.getGbhy()));
                                        dataJson.put("gbhyid", auditRsItemBaseinfo.getGbhy());
                                    }
                                    else {
                                        dataJson.put("gbhy", "请选择");
                                        dataJson.put("gbhyid", "");
                                    }
                                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("国标行业2017");
                                    List<JSONObject> jsonList = new ArrayList<>();
                                    for (CodeItems codeItem : codeItems) {
                                        if (codeItem.getItemValue().length() == 1) {
                                            JSONObject obj1 = new JSONObject();
                                            obj1.put("id", codeItem.getItemValue());
                                            obj1.put("pId", "root");
                                            obj1.put("name", codeItem.getItemText());
                                            jsonList.add(obj1);
                                        }
                                        if (codeItem.getItemValue().length() == 3) {
                                            JSONObject obj1 = new JSONObject();
                                            obj1.put("id", codeItem.getItemValue());
                                            obj1.put("pId", codeItem.getItemValue().substring(0, 1));
                                            obj1.put("name", codeItem.getItemText());
                                            jsonList.add(obj1);
                                        }
                                        if (codeItem.getItemValue().length() == 4) {
                                            JSONObject obj1 = new JSONObject();
                                            obj1.put("id", codeItem.getItemValue());
                                            obj1.put("pId", codeItem.getItemValue().substring(0, 3));
                                            obj1.put("name", codeItem.getItemText());
                                            jsonList.add(obj1);
                                        }
                                        if (codeItem.getItemValue().length() == 5) {
                                            JSONObject obj1 = new JSONObject();
                                            obj1.put("id", codeItem.getItemValue());
                                            obj1.put("pId", codeItem.getItemValue().substring(0, 4));
                                            obj1.put("name", codeItem.getItemText());
                                            jsonList.add(obj1);
                                        }
                                    }
                                    dataJson.put("gbhytree", jsonList);

                                    dataJson.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());
                                    dataJson.put("legalperson", auditRsCompanyBaseinfo.getOrganlegal());
                                    dataJson.put("legalpersonicardnum", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                                    dataJson.put("frphone", auditRsItemBaseinfo.getFrphone());
                                    dataJson.put("fremail", auditRsItemBaseinfo.getFremail());
                                    dataJson.put("contractperson", auditRsItemBaseinfo.getContractperson());
                                    dataJson.put("contractidcart", auditRsItemBaseinfo.getContractidcart());
                                    dataJson.put("contractphone", auditRsItemBaseinfo.getContractphone());
                                    dataJson.put("contractemail", auditRsItemBaseinfo.getContractemail());
                                    // 4.2.34、对接策划生成系统
                                    String chSwitch = iConfigService.getFrameConfigValue("AUDIT_CHSWITCH");
                                    dataJson.put("chswitch", ZwfwConstant.CONSTANT_STR_ONE.equals(chSwitch));
                                    dataJson.put("chcode", auditRsItemBaseinfo.get("chcode"));
                                    log.info("=======结束调用getCompanyItemInfo接口=======");
                                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                                }
                            }
                            else {
                                return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getCompanyItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCompanyItemInfo接口参数：params【" + params + "】=======");
            log.info("=======getCompanyItemInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取简单项目基本信息接口（查看项目阶段时调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSimpleItemInfo", method = RequestMethod.POST)
    public String getSimpleItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSimpleItemInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.2、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 2.1、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, itemguid, 12)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid)
                        .getResult();
                if (auditRsItemBaseinfo != null) {
                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                }
                log.info("=======结束调用getSimpleItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取简单项目基本信息成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSimpleItemInfo接口参数：params【" + params + "】=======");
            log.info("=======getSimpleItemInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取简单项目基本信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 保存项目申报信息（点击保存项目信息按钮时调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveItemInfo", method = RequestMethod.POST)
    public String saveItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveItemInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目名称
                String itemName = obj.getString("itemname");
                // 1.3 获取项目类型
                String itemType = obj.getString("itemtype");
                // 1.4 获取建设性质
                String constructionProperty = obj.getString("constructionproperty");
                // 1.9 拟开工时间
                Date itemStartDate = obj.getDate("itemstartdate");
                // 1.10 拟建成时间
                Date itemFinishDate = obj.getDate("itemfinishdate");
                // 1.11 获取总投资（万元）
                Double totalInvest = obj.getDouble("totalinvest");
                // 1.12 获取建设地点
                String constructionSite = obj.getString("constructionsite");
                // 1.13 获取建设地点详情
                String constructionSiteDesc = obj.getString("constructionsitedesc");
                // 1.14 获取所属行业
                String belongTindustry = obj.getString("belongtindustry");
                // 1.15 获取建设规模及内容
                String constructionScaleAndDesc = obj.getString("constructionscaleanddesc");
                // 1.16 获取用地面积(亩)
                Double landArea = obj.getDouble("landarea");
                // 1.17 获新增用地面积
                Double newLandArea = obj.getDouble("newlandarea");
                // 1.18 获取农用地面积
                Double agriculturalLandArea = obj.getDouble("agriculturallandarea");
                // 1.19 获取项目资本金
                Double itemCapital = obj.getDouble("itemcapital");
                // 1.20 获取资金来源
                String fundSources = obj.getString("fundsources");
                // 1.21 获取财政资金来源
                String financialResources = obj.getString("financialresources");
                // 1.22 获取量化建设规模的类别
                String quantifyConstructType = obj.getString("quantifyconstructtype");
                // 1.23 获取量化建设规模的数值
                Double quantifyConstructCount = obj.getDouble("quantifyconstructcount");
                // 1.24 获取量化建设规模的单位
                String quantifyConstructDept = obj.getString("quantifyconstructdept");
                // 1.25 获取是否技改项目
                String isImprovement = obj.getString("isimprovement");
                // 1.26 获取主题唯一标识
                String businessGuid = obj.getString("businessguid");
                // 1.27 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.28 项目投资来源
                Integer xmtzly = obj.getInteger("xmtzly");
                // 1.29 土地获取方式
                Integer tdhqfs = obj.getInteger("tdhqfs");
                // 1.30 土地是否带设计方案
                Integer tdsfdsjfa = obj.getInteger("tdsfdsjfa");
                // 1.31 是否完成区域评估
                Integer sfwcqypg = obj.getInteger("sfwcqypg");
                // 1.32 建筑面积
                String jzmjStr = obj.getString("jzmj");
                // 1.33 建筑面积
                String gbhy = obj.getString("gbhy");
                // 1.34 项目资金属性
                String xmzjsx = obj.getString("xmzjsx");
                // 1.35 工程分类
                String chcode = obj.getString("chcode");
                Double jzmj = null;
                if (StringUtil.isNotBlank(jzmjStr)) {
                    jzmj = Double.parseDouble(jzmjStr);
                }

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取用户对应的法人信息
                    if (StringUtil.isNotBlank(itemGuid)) {
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                        if (auditRsItemBaseinfo != null) {
                            // 3.1 更新项目信息
                            auditRsItemBaseinfo.setOperatedate(new Date());
                            auditRsItemBaseinfo.setItemname(itemName);
                            auditRsItemBaseinfo.setItemcode(itemCode);
                            auditRsItemBaseinfo.setItemtype(itemType);
                            auditRsItemBaseinfo.setConstructionproperty(constructionProperty);
                            auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                            auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                            auditRsItemBaseinfo.setTotalinvest(totalInvest);
                            auditRsItemBaseinfo.setConstructionsite(constructionSite);
                            auditRsItemBaseinfo.setConstructionsitedesc(constructionSiteDesc);
                            auditRsItemBaseinfo.setBelongtindustry(belongTindustry);
                            auditRsItemBaseinfo.setConstructionscaleanddesc(constructionScaleAndDesc);
                            auditRsItemBaseinfo.setLandarea(landArea);
                            auditRsItemBaseinfo.setNewlandarea(newLandArea);
                            auditRsItemBaseinfo.setAgriculturallandarea(agriculturalLandArea);
                            auditRsItemBaseinfo.setItemcapital(itemCapital);
                            auditRsItemBaseinfo.setFundsources(fundSources);
                            auditRsItemBaseinfo.setFinancialresources(financialResources);
                            auditRsItemBaseinfo.setQuantifyconstructtype(quantifyConstructType);
                            auditRsItemBaseinfo.setQuantifyconstructdept(quantifyConstructDept);
                            auditRsItemBaseinfo.setQuantifyconstructcount(quantifyConstructCount);
                            auditRsItemBaseinfo.setIsimprovement(isImprovement);
                            auditRsItemBaseinfo.setXmtzly(xmtzly);
                            auditRsItemBaseinfo.setTdhqfs(tdhqfs);
                            auditRsItemBaseinfo.setTdsfdsjfa(tdsfdsjfa);
                            auditRsItemBaseinfo.setSfwcqypg(sfwcqypg);
                            auditRsItemBaseinfo.setJzmj(jzmj);
                            auditRsItemBaseinfo.setXmzjsx(xmzjsx);
                            auditRsItemBaseinfo.setGbhy(gbhy);
                            auditRsItemBaseinfo.set("chcode", chcode);
                            if (StringUtil.isBlank(auditRsItemBaseinfo.getBiguid())) {
                                // 3.2、如果项目没有对应的主题实例，则生成对应主题实例
                                if (StringUtil.isNotBlank(businessGuid)) {
                                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                            .getAuditSpBusinessByRowguid(businessGuid).getResult();
                                    if (auditSpBusiness != null) {
                                        String newBiguid = iAuditSpInstance.addBusinessInstance(businessGuid,
                                                auditRsItemBaseinfo.getRowguid(), auditOnlineRegister.getAccountguid(),
                                                auditOnlineRegister.getUsername(), ZwfwConstant.APPLY_WAY_NETSBYS,
                                                itemName, auditSpBusiness.getAreacode(),
                                                auditSpBusiness.getBusinesstype()).getResult();
                                        // 3.2.1、项目管理主题实例
                                        auditRsItemBaseinfo.setBiguid(newBiguid);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                        JSONObject dataJson = new JSONObject();
                                        dataJson.put("biguid", newBiguid);
                                        dataJson.put("firstflag", "1");
                                        return JsonUtils.zwdtRestReturn("1", " 保存成功", dataJson);
                                    }
                                }
                                return JsonUtils.zwdtRestReturn("0", "请选择项目性质", "");
                            }
                            else {
                                // 3.2、如果项目已有对应的主题实例，则直接更新数据
                                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                JSONObject dataJson = new JSONObject();
                                dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                                dataJson.put("firstflag", "0");
                                return JsonUtils.zwdtRestReturn("1", " 修改成功", dataJson);
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", " 保存失败！项目不存在", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", " 项目查询失败", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用saveItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveItemInfo接口参数：params【" + params + "】=======");
            log.info("=======saveItemInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取主题列表接口（项目表单页面下拉选择调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessList", method = RequestMethod.POST)
    public String getBusinessList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、查询辖区下启用的主题
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("businesstype", "1"); // 主题分类：建设性项目
                sqlConditionUtil.eq("del", "0"); // 主题状态：启用
                sqlConditionUtil.eq("areacode", areaCode); // 辖区编码
                sqlConditionUtil.setOrderDesc("ordernumber"); // 按排序字段降序
                List<AuditSpBusiness> auditSpBusinesses = iAuditSpBusiness
                        .getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();
                // 3、定义返回的主题数据
                List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                JSONObject objJson = new JSONObject();
                objJson.put("businessguid", ""); // 主题标识
                objJson.put("businessname", "请选择");// 主题名称
                businessJsonList.add(objJson);
                for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                    // 4、主题的第一阶段中配置了事项，且事项都是可用的
                    boolean flag = true;
                    // 4.1、获取主题下的阶段数据
                    List<AuditSpPhase> auditSpPhases = iAuditSpPhase
                            .getSpPaseByBusinedssguid(auditSpBusiness.getRowguid()).getResult();
                    List<AuditSpTask> auditSpTaskList = new ArrayList<AuditSpTask>();
                    if (auditSpPhases != null && auditSpPhases.size() > 0) {
                        for (AuditSpPhase auditSpPhase : auditSpPhases) {
                            auditSpTaskList = iAuditSpTask.getAllAuditSpTaskByPhaseguid(auditSpPhase.getRowguid())
                                    .getResult();
                            // 4.2、获取阶段下配置的事项
                            for (AuditSpTask auditSpTask : auditSpTaskList) {
                                // 4.3、循环主题第一个阶段下的所有事项，如果有事项是不可用的，则该主题不可申报
                                // 查询并遍历标准事项关联关系表
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("basetaskguid", auditSpTask.getBasetaskguid());
                                List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                        .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                    AuditTask auditTask = iAuditTask
                                            .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                    // 如果事项不可用，则更改标志位，跳出循环
                                    if (auditTask == null) {
                                        flag = false;
                                        break;
                                    }
                                    // 事项可用，则更改标志位
                                    flag = true;
                                }
                                // flag为true，表明该阶段下具有事项且均可用，则跳出循环，返回主题信息
                                if (flag) {
                                    break;
                                }
                            }
                            // flag为true，表明该阶段下具有事项且均可用，则跳出循环，返回主题信息
                            if (flag) {
                                break;
                            }
                        }
                    }
                    // 5、若主题下第一个阶段配置了事项且事项都可用则返回套餐基本信息
                    if (auditSpTaskList.size() != 0 && flag) {
                        JSONObject bussinessJson = new JSONObject();
                        bussinessJson.put("businessguid", auditSpBusiness.getRowguid()); // 主题标识
                        bussinessJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                        businessJsonList.add(bussinessJson);
                    }
                }
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("businesslist", businessJsonList);
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取并联审批主题列表成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessList接口参数：params【" + params + "】=======");
            log.info("=======getBusinessList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取并联审批主题列表失败：" + e.getMessage(), "");
        }
    }

    /**********************************************
     * 项目相关接口结束
     **********************************************/

    /**********************************************
     * 申报相关接口开始
     **********************************************/
    /**
     * 获取阶段及对应的子申报实例信息（查看阶段阶段信息页面）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getPhaseAndSPSubappList", method = RequestMethod.POST)
    public String getPhaseAndSPSubappList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getPhaseAndSPSubappList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.2、获取阶段实例
                String businessguid = obj.getString("businessguid");
                // 1.3、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 2.1、阶段列表List
                List<JSONObject> phaseJsonList = new ArrayList<JSONObject>();
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, itemguid, 12)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取阶段列表
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessguid).getResult();
                // 查询实例
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                for (AuditSpPhase auditSpPhase : auditSpPhases) {
                    // 过滤第五阶段，并行推进
                    if ("5".equals(auditSpPhase.getPhaseId())) {
                        continue;
                    }
                    JSONObject phaseObject = new JSONObject();
                    phaseObject.put("phasename", auditSpPhase.getPhasename());
                    phaseObject.put("phaseguid", auditSpPhase.getRowguid());
                    // 3.1、获取该阶段下子申报列表
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("biguid", biGuid);
                    sqlConditionUtil.eq("phaseguid", auditSpPhase.getRowguid());
                    sqlConditionUtil.nq("status", "-1");
                    sqlConditionUtil.setOrderDesc("CREATEDATE");
                    List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappListByMap(sqlConditionUtil.getMap())
                            .getResult();
                    boolean flag = true;// 未进行整体申报
                    // 3.2、子申报JSON列表
                    List<JSONObject> subappJsonList = new ArrayList<JSONObject>();
                    // 非草稿的数量
                    int hasApplyCount = 0;
                    for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                        JSONObject spiSubappJson = new JSONObject();
                        spiSubappJson.put("subappname", auditSpISubapp.getSubappname());
                        spiSubappJson.put("subappguid", auditSpISubapp.getRowguid());
                        spiSubappJson.put("subappstatus", auditSpISubapp.getStatus());
                        // 查询需要办理的事项的数量
                        List<AuditSpITask> auditSpITasks = iAuditSpITask
                                .getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                        int allNum = auditSpITasks.size();
                        int inNum = 0;
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            // 如果project不为空证明该事项已经分发，或者status不为空和0证明外网已提交过
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())
                                    || (StringUtil.isNotBlank(auditSpITask.getStatus())
                                            && !"0".equals(auditSpITask.getStatus()))) {
                                inNum++;
                            }
                        }
                        spiSubappJson.put("allnum", allNum);
                        spiSubappJson.put("innum", inNum);
                        subappJsonList.add(spiSubappJson);
                        if (auditSpInstance.getYewuguid().equals(auditSpISubapp.getYewuguid())
                                && !"-1".equals(auditSpISubapp.getStatus())
                                && !"3".equals(auditSpISubapp.getStatus())) {
                            flag = false;
                        }
                        if (!"-1".equals(auditSpISubapp.getStatus()) && !"3".equals(auditSpISubapp.getStatus())) {
                            hasApplyCount++;
                        }
                    }
                    phaseObject.put("subapplist", subappJsonList);
                    // 3.3、获取是否允许阶段申报
                    int allowmultisubapply = 0;
                    // 进行了整体申报
                    if (!flag) {
                        allowmultisubapply = 0;
                    }
                    else {
                        allowmultisubapply = 1;
                    }
                    // 未进行申报过
                    if (auditSpISubapps.size() == 0) {
                        allowmultisubapply = 1;
                    }
                    if (auditSpISubapps.size() != 0 && hasApplyCount != auditSpISubapps.size()) {
                        phaseObject.put("hasdraft", "1"); // 是否具有草稿
                    }
                    phaseObject.put("allowmultisubapply", allowmultisubapply);
                    phaseJsonList.add(phaseObject);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("phaselist", phaseJsonList);
                log.info("=======结束调用getPhaseAndSPSubappList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取阶段及对应的子申报实例信息成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPhaseAndSPSubappList接口参数：params【" + params + "】=======");
            log.info("=======getPhaseAndSPSubappList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取阶段及对应的子申报实例信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 阶段申报提交接口（填写阶段表单后点击提交按钮调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/startPhaseDeclaration", method = RequestMethod.POST)
    public String startPhaseDeclaration(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取startPhaseDeclaration接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 1.2、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.3、获取阶段实例
                String phaseGuid = obj.getString("phaseguid");
                // 1.4、获取子申报名称
                String subappName = obj.getString("subappname");
                // 1.5、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("biguid", biGuid);
                    // 3、根据子申报标识获取子申报
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                    if (auditSpISubapp != null) {
                        // 3.1、更新子申报状态为已提交
                        iAuditSpISubapp.updateSubapp(subAppGuid, "5", new Date());
                    }
                    else {
                        // 3.2、获取主题实例信息
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                        // 3.2、获取阶段信息
                        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();
                        // 3.3、生成子申报信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                                .selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
                        AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByOneField(field, auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                        AuditSpISubapp newAuditSpISubapp = new AuditSpISubapp();
                        newAuditSpISubapp.setRowguid(subAppGuid);
                        newAuditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
                        newAuditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
                        newAuditSpISubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                        newAuditSpISubapp.setBiguid(biGuid);
                        newAuditSpISubapp.setBusinessguid(auditSpInstance.getBusinessguid());
                        newAuditSpISubapp.setCreatedate(new Date());
                        newAuditSpISubapp.setPhaseguid(phaseGuid);
                        newAuditSpISubapp.setStatus("5");
                        if (StringUtils.isNotBlank(itemguid)) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                            if (auditRsItemBaseinfo1 != null) {
                                newAuditSpISubapp.setYewuguid(itemguid);
                            }
                            else {
                                newAuditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                            }
                        }

                        if (StringUtil.isBlank(subappName)) {
                            newAuditSpISubapp.setSubappname(auditSpPhase.getPhasename()); // 子申报名称
                        }
                        else {
                            newAuditSpISubapp.setSubappname(subappName);
                        }
                        iAuditSpISubapp.addSubapp(newAuditSpISubapp);
                    }
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("subappguid", subAppGuid);
                    log.info("=======结束调用startPhaseDeclaration接口=======");
                    return JsonUtils.zwdtRestReturn("1", "阶段申报提交成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPhaseAndSPSubappList接口参数：params【" + params + "】=======");
            log.info("=======startPhaseDeclaration接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "阶段申报提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 保存阶段申报接口（填写完阶段表单点击保存按钮时调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/savePhaseDeclaration", method = RequestMethod.POST)
    public String savePhaseDeclaration(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取savePhaseDeclaration接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 1.2、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.3、获取阶段实例
                String phaseGuid = obj.getString("phaseguid");
                // 1.4、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 1.4、获取项目标识
                String subappname = obj.getString("subappname");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("biguid", biGuid);
                    // 查询子申报状态
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                    if (auditSpISubapp == null) {
                        // 3.1、获取主题实例信息
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                        // 3.3、生成子申报信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                                .selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
                        AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByOneField(field, auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                        if (auditRsItemBaseinfo != null) {
                            AuditSpISubapp newAuditSpISubapp = new AuditSpISubapp();
                            newAuditSpISubapp.setRowguid(subAppGuid);
                            newAuditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
                            newAuditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
                            newAuditSpISubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                            newAuditSpISubapp.setBiguid(biGuid);
                            newAuditSpISubapp.setBusinessguid(auditSpInstance.getBusinessguid());
                            newAuditSpISubapp.setCreatedate(new Date());
                            newAuditSpISubapp.setPhaseguid(phaseGuid);
                            newAuditSpISubapp.setStatus("3");
                            newAuditSpISubapp.setSubappname(subappname); // 子申报名称
                            if (StringUtils.isNotBlank(itemguid)) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo
                                        .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                if (auditRsItemBaseinfo1 != null) {
                                    newAuditSpISubapp.setYewuguid(itemguid);
                                }
                                else {
                                    newAuditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                                }
                            }
                            iAuditSpISubapp.addSubapp(newAuditSpISubapp);
                        }
                    }
                    else {
                        auditSpISubapp.setCreatedate(new Date());
                        auditSpISubapp.setSubappname(subappname);
                        iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                    }
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    return JsonUtils.zwdtRestReturn("1", "保存阶段申报成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======savePhaseDeclaration接口参数：params【" + params + "】=======");
            log.info("=======savePhaseDeclaration接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存阶段申报异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取子申报状态接口（查看子申报详情表单页面）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSubappStatus", method = RequestMethod.POST)
    public String getSubappStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSubappStatus接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取子申报的预审不通过原因(如果未生成则不返回)
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                if (auditSpISubapp != null) {
                    dataJson.put("status", auditSpISubapp.getStatus());
                    dataJson.put("reason", auditSpISubapp.getReason());
                }
                log.info("=======结束调用getSubappStatus接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取子申报状态成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取子申报状态失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSubappStatus接口参数：params【" + params + "】=======");
            log.info("=======getSubappStatus接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取退回原因异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项及材料清单接口(新模式)（对子申报实例点击上传材料按钮后加载的页面调用）
     * 事项的材料单独渲染，并且材料实现去重（要考虑重复材料上传对应不同示例材料的情况） by zcm
     * 
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/newGetSPTaskAndMaterialInstanceList", method = RequestMethod.POST)
    public String newGetSPTaskAndMaterialInstanceList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取newGetSPTaskAndMaterialInstanceList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();

                // 查询阶段数据
                AuditSpPhase auditspphase = iAuditSpPhase.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid())
                        .getResult();

                // 查询需要办理的事项的数量
                List<AuditSpITask> auditSpITasks = iAuditSpITask
                        .getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                int allNum = auditSpITasks.size();
                int inNum = 0;
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 如果project不为空证明该事项已经分发，或者status不为空和0证明外网已提交过
                    if (StringUtil.isNotBlank(auditSpITask.getProjectguid())
                            || (StringUtil.isNotBlank(auditSpITask.getStatus())
                                    && !"0".equals(auditSpITask.getStatus()))) {
                        inNum++;
                    }
                }
                if (ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus()) || (allNum - inNum) != 0) {
                    String biGuid = auditSpISubapp.getBiguid();
                    // 2.1、查询申报实例信息
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    String businessGuid = auditSpInstance.getBusinessguid();
                    String companyId = auditSpISubapp.getApplyerguid();
                    String phaseGuid = auditSpISubapp.getPhaseguid();
                    // 2.2、查询申报项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                    // 2.3 如果材料实例没有初始化，则初始化材料
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                        iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, companyId,
                                auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                    }
                    // 3、获取子申报事项实例列表
                    List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                    // 返回该事项下的材料列表
                    List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                    // 获取当前实例化的所有材料
                    // SqlConditionUtil matrlialConditionUtil = new
                    // SqlConditionUtil();
                    // List<String> taskguids = auditSpITasks.stream().map(a ->
                    // a.getStr("taskguid"))
                    // .collect(Collectors.toList());
                    // matrlialConditionUtil.in("taskguid",
                    // StringUtil.joinSql(taskguids));
                    // List<AuditTaskMaterial> auditTaskMaterials =
                    // iAuditTaskMaterial
                    // .selectMaterialListByCondition(matrlialConditionUtil.getMap()).getResult();
                    //
                    // List<String> materialNameList =
                    // auditTaskMaterials.stream().map(a -> a.getMaterialname())
                    // .collect(Collectors.toList());

                    StringBuffer materialnames = new StringBuffer();

                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        long startTime = System.currentTimeMillis();
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false)
                                .getResult();
                        // 将表单id放入后台
                        JSONObject spiTaskJson = new JSONObject();
                        spiTaskJson.put("taskname",
                                auditSpITask.getTaskname() + "【所属部门：" + auditTask.getOuname() + "】");
                        spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                        spiTaskJson.put("itaskguid", auditSpITask.getRowguid());
                        // 控制事项是否被选择
                        if (StringUtil.isNotBlank(auditSpITask.getStatus()) && "1".equals(auditSpITask.getStatus())) {
                            spiTaskJson.put("is_xz", "pointer-events: none");
                            spiTaskJson.put("is_yx", "1");
                            spiTaskJson.put("is_zh", "disabled");
                        }
                        /* 3.0新增逻辑 */
                        String affiliatedUnit = iAuditSpITaskCorpService.getCorpnamesBySubappguidAndTaskguid(subAppGuid,
                                auditSpITask.getTaskguid());
                        spiTaskJson.put("affiliatedunit", affiliatedUnit);
                        String basetaskguidString = taskCommonService
                                .getCropInfo(auditSpITask.getTaskguid(), subAppGuid, auditSpITask.getPhaseguid())
                                .getResult();
                        String corptype = taskCommonService.getCropName(basetaskguidString).getResult();
                        if (StringUtil.isNotBlank(corptype)) {
                            spiTaskJson.put("corptype", corptype);
                            List<String> corptypr_cnList = new ArrayList<>();
                            for (String type : corptype.split(",")) {
                                String text = iCodeItemsService.getItemTextByCodeName("关联单位类型", type);
                                corptypr_cnList.add(text);
                            }
                            spiTaskJson.put("corptype_cn", StringUtil.join(corptypr_cnList, ";"));
                        }
                        /* 3.0结束逻辑 */

                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus())
                                || StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            // 办理中的事项不可再进行材料提交
                            spiTaskJson.put("isblz", 1);
                        }

                        // 获取事项表单放入事项信息中
                        AuditTaskExtension audittaskextension = iaudittaskextension
                                .getTaskExtensionByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                        if (audittaskextension != null && StringUtil.isNotBlank(audittaskextension.getStr("formid"))) {
                            spiTaskJson.put("formid", audittaskextension.getStr("formid"));
                            spiTaskJson.put("ywformid", audittaskextension.getStr("formid"));
                        }
                        if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                            spiTaskJson.put("phaseformid", auditspphase.getStr("formid"));
                        }

                        // 新增四阶段三个事项自动带入监管事项材料 ----hqt
                        String materialid = "";
                        String beizhu = "";
                        String cliengguid = "";
                        AuditTask task = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), true).getResult();
                        if (task != null) {
                            String task_id = task.getTask_id();
                            CodeItems codeItems = iCodeItemsService.getCodeItemByCodeName("监管事项材料对应关系", task_id);
                            if (codeItems != null) {
                                // 事项材料id
                                materialid = codeItems.getItemText();
                                beizhu = codeItems.getDmAbr1();
                            }

                            // 先查询是否满足自动带入条件
                            if (StringUtil.isNotBlank(materialid) && StringUtil.isNotBlank(beizhu)) {
                                JgTaskmaterialrelation jgTaskmaterialrelation = relationService
                                        .getRelationByItemguid(auditRsItemBaseinfo.getRowguid());
                                if (jgTaskmaterialrelation != null) {
                                    if ("工程规划批后监管".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getGccliengguid();
                                    }
                                    else if ("建筑工程质量安全监督".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getJzcliengguid();
                                    }
                                    else if ("人防工程质量安全监督".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getRfcliengguid();
                                    }
                                }
                            }

                        }

                        // 5.1、获取事项下的材料列表
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                        long endTime2 = System.currentTimeMillis();

                        // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                        // 定义一个标志位用于区分该事项下是否有多评多测材料
                        int typeFlag = 0; // 0代表没有多评和多测 1代表只有多评 2代表有多测 3代表全都有
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            if (materialnames.toString().contains(auditTaskMaterial.getMaterialname())) {
                                continue;
                            }
                            JSONObject spiMaterialJson = new JSONObject();
                            // 5.3、获取事项实例中的材料实例信息
                            AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                    .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                    .getResult();
                            if (auditSpIMaterial != null
                                    && ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                continue;
                            }
                            // 4.11、 判断材料来源
                            String materialsource = auditTaskMaterial.getFile_source();
                            // 5.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                            if (auditSpIMaterial == null) {
                                AuditSpShareMaterialRelation auditSpShareMaterialRelation = iAuditSpShareMaterialRelation
                                        .getRelationByID(auditSpITask.getBusinessguid(),
                                                auditTaskMaterial.getMaterialid())
                                        .getResult();
                                // 4.7、如果存在共享材料/结果材料
                                if (auditSpShareMaterialRelation != null) {
                                    AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                            .getAuditSpShareMaterialByShareMaterialGuid(
                                                    auditSpShareMaterialRelation.getSharematerialguid(), businessGuid)
                                            .getResult();
                                    if (auditSpShareMaterial != null) {
                                        materialsource = auditSpShareMaterial.getFile_source();
                                    }
                                    // 4.9、获取存在的材料共享/结果关系
                                    auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(subAppGuid,
                                            auditSpShareMaterialRelation.getSharematerialguid()).getResult();
                                    // 4.10、判断是否是结果材料，如果是就不显示该材料
                                    if (auditSpIMaterial == null
                                            || ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                        continue;
                                    }
                                }
                                else {
                                    // 没有找到共享材料数据，结束该循环
                                    continue;
                                }
                            }

                            // 把综管上传的材料放入该事项对应的材料中去
                            if (StringUtil.isNotBlank(cliengguid) && StringUtil.isNotBlank(materialid)
                                    && materialid.equals(auditTaskMaterial.getMaterialid())) {
                                // 先把原先关联删除在复制
                                iAttachService.deleteAttachByGuid(auditSpIMaterial.getCliengguid());
                                iAttachService.copyAttachByClientGuid(cliengguid, "监管事项材料复制", null,
                                        auditSpIMaterial.getCliengguid());
                                // 同时把状态改为已上传
                                auditSpIMaterial.setStatus("20");
                                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                            }

                            if (StringUtil.isBlank(materialsource)) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                            }
                            spiMaterialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                    : auditTaskMaterial.getPage_num());
                            spiMaterialJson.put("cliengguid", auditSpIMaterial.getCliengguid());
                            spiMaterialJson.put("stantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());
                            spiMaterialJson.put("materialsource", materialsource);
                            spiMaterialJson.put("type", auditTaskMaterial.getType());
                            spiMaterialJson.put("materialguid", auditSpIMaterial.getRowguid());
                            spiMaterialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());
                            String materialtype = "";
                            if (StringUtil.isNotBlank(auditTaskMaterial.getMaterialtype())) {
                                String materialTypeMain = auditTaskMaterial.getMaterialtype().substring(0, 4);
                                if ("tscl".equals(materialTypeMain)) {
                                    materialtype = "【图审】";
                                }
                                else if ("chcl".equals(materialTypeMain)) {
                                    materialtype = "【测绘】";
                                    if (typeFlag == 0) {
                                        typeFlag = 2;
                                    }
                                    else if (typeFlag == 1) {
                                        typeFlag = 3;
                                    }
                                }
                                else if ("dpcl".equals(materialTypeMain)) {
                                    materialtype = "【多评】";
                                    if (typeFlag == 0) {
                                        typeFlag = 1;
                                    }
                                    else if (typeFlag == 2) {
                                        typeFlag = 3;
                                    }
                                }
                                else if ("lscl".equals(materialTypeMain)) {
                                    materialtype = "【联审】";
                                }
                            }
                            spiMaterialJson.put("materialtype", materialtype);
                            spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                            spiMaterialJson.put("materialinstanceguid", auditTaskMaterial.getRowguid());
                            spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                            spiMaterialJson.put("rowguid", auditTaskMaterial.getRowguid());
                            // 是否共享材料
                            spiMaterialJson.put("shared", auditSpIMaterial.getShared());
                            // 共享材料返回共享材料的必要性、容缺、提交方式
                            // 是否必须 10必须 20非必须
                            spiMaterialJson.put("necessity", auditSpIMaterial.getNecessity());
                            // 是否容缺
                            String rongque = "0";// 默认不容缺
                            if (StringUtil.isNotBlank(auditSpIMaterial.getNecessity())
                                    && "10".equals(auditSpIMaterial.getNecessity())) {
                                rongque = auditSpIMaterial.getAllowrongque();// 只有必要的材料才需要显示容缺
                            }
                            spiMaterialJson.put("allowrongque", rongque);
                            spiMaterialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditSpIMaterial.getSubmittype())));
                            // 示例表格
                            String exampleClientGuid = auditTaskMaterial.getExampleattachguid();
                            if (StringUtil.isNotBlank(exampleClientGuid)) {
                                int exampleAttachCount = iAttachService.getAttachCountByClientGuid(exampleClientGuid);
                                if (exampleAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(exampleClientGuid);
                                    spiMaterialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 空白表格
                            String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                            if (StringUtil.isNotBlank(templateClientGuid)) {
                                int templateAttachCount = iAttachService.getAttachCountByClientGuid(templateClientGuid);
                                if (templateAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(templateClientGuid);
                                    spiMaterialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 3.12、 判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton = 0; // 按钮显示方式
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                    ? "1"
                                    : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                            if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                                // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() != null
                                        && auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else {
                                    // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.6.2、如果关联了证照库
                                if (count > 0) {
                                    // 4.6.2.1、获取材料类别
                                    String materialType = auditTaskMaterial.getMaterialtype();
                                    if (StringUtil.isBlank(materialType)) {
                                        materialType = "0";
                                    }
                                    if (Integer.parseInt(materialType) == 1) {
                                        // 4.6.2.1.1、已引用证照库
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                    }
                                    else if (Integer.parseInt(materialType) == 2) {
                                        // 4.6.2.1.2、已引用批文
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                    }
                                    else {
                                        // 4.6.2.1.3、已引用材料
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                    }
                                }
                                else {
                                    // 4.6.2.2、如果没有附件，则标识为未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);

                                }
                            }
                            spiMaterialJson.put("showbutton", showButton);
                            spiMaterialJson.put("needload", needLoad);

                            spiMaterialJson.put("taskguid", auditSpITask.getRowguid());
                            spiMaterialJsonList.add(spiMaterialJson);
                            materialnames.append(auditSpIMaterial.getMaterialname() + ",");
                        }

                        long endTime3 = System.currentTimeMillis();
                        log.info("subAppGuid:" + subAppGuid + ",3工改获取材料运行时间为：" + (endTime3 - endTime2) / 100 + "秒");

                        // 4、存入事项下的材料列表
                        spiTaskJson.put("typeflag", typeFlag);
                        spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                        spiTaskJsonList.add(spiTaskJson);

                        long endTime = System.currentTimeMillis();
                        log.info("subAppGuid:" + subAppGuid + ",工改获取材料运行时间为：" + (endTime - startTime) / 100 + "秒");

                    }
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 实例事项信息
                    dataJson.put("spitasklist", spiTaskJsonList);
                    // 去重之后的材料信息
                    dataJson.put("spimateriallist", spiMaterialJsonList);
                    if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                        dataJson.put("formid", auditspphase.getStr("formid"));
                        dataJson.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                        dataJson.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                    }
                    log.info("=======结束调用newGetSPTaskAndMaterialInstanceList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取事项及材料清单成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "当前阶段不可提交材料！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======newGetSPTaskAndMaterialInstanceList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项及材料清单异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项及材料清单接口（对子申报实例点击上传材料按钮后加载的页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSPTaskAndMaterialInstanceList", method = RequestMethod.POST)
    public String getSPTaskAndMaterialInstanceList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSPTaskAndMaterialInstanceList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();

                // 查询阶段数据
                AuditSpPhase auditspphase = iAuditSpPhase.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid())
                        .getResult();

                // 查询需要办理的事项的数量
                List<AuditSpITask> auditSpITasks = iAuditSpITask
                        .getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                int allNum = auditSpITasks.size();
                int inNum = 0;
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 如果project不为空证明该事项已经分发，或者status不为空和0证明外网已提交过
                    if (StringUtil.isNotBlank(auditSpITask.getProjectguid())
                            || (StringUtil.isNotBlank(auditSpITask.getStatus())
                                    && !"0".equals(auditSpITask.getStatus()))) {
                        inNum++;
                    }
                }
                if (ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus()) || (allNum - inNum) != 0) {
                    String biGuid = auditSpISubapp.getBiguid();
                    // 2.1、查询申报实例信息
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    String businessGuid = auditSpInstance.getBusinessguid();
                    String companyId = auditSpISubapp.getApplyerguid();
                    String phaseGuid = auditSpISubapp.getPhaseguid();
                    // 2.2、查询申报项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                    // 2.3 如果材料实例没有初始化，则初始化材料
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                        iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, companyId,
                                auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                    }
                    // 3、获取子申报事项实例列表
                    List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        long startTime = System.currentTimeMillis();
                        // 判断事项是主线还是辅线
                        // String flag = "【主线】";
                        // List<String> taskids =
                        // iAuditSpBasetaskR.gettaskidsByBusinedssguid(businessGuid,
                        // "5")
                        // .getResult();
                        // AuditTask auditTask =
                        // iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(),
                        // false)
                        // .getResult();
                        // for (int i = 0; i < taskids.size(); i++) {
                        // if (auditTask.getTask_id().equals(taskids.get(i))) {
                        // flag = "【辅线】";
                        // break;
                        // }
                        // }
                        long endTime1 = System.currentTimeMillis();
                        // log.info("subAppGuid:" + subAppGuid +
                        // ",1工改获取材料运行时间为：" + (endTime1 - startTime) / 100 +
                        // "秒");
                        // 4、返回阶段实例中的事项列表

                        // 将表单id放入后台
                        JSONObject spiTaskJson = new JSONObject();
                        spiTaskJson.put("taskname", auditSpITask.getTaskname());
                        spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                        spiTaskJson.put("itaskguid", auditSpITask.getRowguid());

                        /* 3.0新增逻辑 */
                        String affiliatedUnit = iAuditSpITaskCorpService.getCorpnamesBySubappguidAndTaskguid(subAppGuid,
                                auditSpITask.getTaskguid());
                        spiTaskJson.put("affiliatedunit", affiliatedUnit);
                        String basetaskguidString = taskCommonService
                                .getCropInfo(auditSpITask.getTaskguid(), subAppGuid, auditSpITask.getPhaseguid())
                                .getResult();
                        String corptype = taskCommonService.getCropName(basetaskguidString).getResult();
                        if (StringUtil.isNotBlank(corptype)) {
                            spiTaskJson.put("corptype", corptype);
                            List<String> corptypr_cnList = new ArrayList<>();
                            for (String type : corptype.split(",")) {
                                String text = iCodeItemsService.getItemTextByCodeName("关联单位类型", type);
                                corptypr_cnList.add(text);
                            }
                            spiTaskJson.put("corptype_cn", StringUtil.join(corptypr_cnList, ";"));
                        }
                        /* 3.0结束逻辑 */

                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus())
                                || StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            // 办理中的事项不可再进行材料提交
                            spiTaskJson.put("isblz", 1);
                        }

                        // 获取事项表单放入事项信息中
                        AuditTaskExtension audittaskextension = iaudittaskextension
                                .getTaskExtensionByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                        if (audittaskextension != null && StringUtil.isNotBlank(audittaskextension.getStr("formid"))) {
                            spiTaskJson.put("formid", audittaskextension.getStr("formid"));
                        }

                        // 新增四阶段三个事项自动带入监管事项材料 ----hqt
                        String materialid = "";
                        String beizhu = "";
                        String cliengguid = "";
                        AuditTask task = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), true).getResult();
                        if (task != null) {
                            String task_id = task.getTask_id();
                            CodeItems codeItems = iCodeItemsService.getCodeItemByCodeName("监管事项材料对应关系", task_id);
                            if (codeItems != null) {
                                // 事项材料id
                                materialid = codeItems.getItemText();
                                beizhu = codeItems.getDmAbr1();
                            }

                            // 先查询是否满足自动带入条件
                            if (StringUtil.isNotBlank(materialid) && StringUtil.isNotBlank(beizhu)) {
                                JgTaskmaterialrelation jgTaskmaterialrelation = relationService
                                        .getRelationByItemguid(auditRsItemBaseinfo.getRowguid());
                                if (jgTaskmaterialrelation != null) {
                                    if ("工程规划批后监管".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getGccliengguid();
                                    }
                                    else if ("建筑工程质量安全监督".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getJzcliengguid();
                                    }
                                    else if ("人防工程质量安全监督".equals(beizhu)) {
                                        cliengguid = jgTaskmaterialrelation.getRfcliengguid();
                                    }
                                }
                            }

                        }

                        // 5、返回该事项下的材料列表
                        List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                        // 5.1、获取事项下的材料列表
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                        long endTime2 = System.currentTimeMillis();
                        // log.info("subAppGuid:" + subAppGuid +
                        // ",2工改获取材料运行时间为：" + (endTime2 - endTime1) / 100 +
                        // "秒");

                        // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                        // 定义一个标志位用于区分该事项下是否有多评多测材料
                        int typeFlag = 0; // 0代表没有多评和多测 1代表只有多评 2代表有多测 3代表全都有
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject spiMaterialJson = new JSONObject();
                            // 5.3、获取事项实例中的材料实例信息
                            AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                    .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                    .getResult();
                            if (auditSpIMaterial != null
                                    && ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                continue;
                            }
                            // 4.11、 判断材料来源
                            String materialsource = auditTaskMaterial.getFile_source();
                            // 5.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                            if (auditSpIMaterial == null) {
                                AuditSpShareMaterialRelation auditSpShareMaterialRelation = iAuditSpShareMaterialRelation
                                        .getRelationByID(auditSpITask.getBusinessguid(),
                                                auditTaskMaterial.getMaterialid())
                                        .getResult();
                                // 4.7、如果存在共享材料/结果材料
                                if (auditSpShareMaterialRelation != null) {
                                    AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                            .getAuditSpShareMaterialByShareMaterialGuid(
                                                    auditSpShareMaterialRelation.getSharematerialguid(), businessGuid)
                                            .getResult();
                                    if (auditSpShareMaterial != null) {
                                        materialsource = auditSpShareMaterial.getFile_source();
                                    }
                                    // 4.9、获取存在的材料共享/结果关系
                                    auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(subAppGuid,
                                            auditSpShareMaterialRelation.getSharematerialguid()).getResult();
                                    // 4.10、判断是否是结果材料，如果是就不显示该材料
                                    if (auditSpIMaterial == null
                                            || ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                        continue;
                                    }
                                }
                                else {
                                    // 没有找到共享材料数据，结束该循环
                                    continue;
                                }
                            }

                            // 把综管上传的材料放入该事项对应的材料中去
                            if (StringUtil.isNotBlank(cliengguid) && StringUtil.isNotBlank(materialid)
                                    && materialid.equals(auditTaskMaterial.getMaterialid())) {
                                // 先把原先关联删除在复制
                                iAttachService.deleteAttachByGuid(auditSpIMaterial.getCliengguid());
                                iAttachService.copyAttachByClientGuid(cliengguid, "监管事项材料复制", null,
                                        auditSpIMaterial.getCliengguid());
                                // 同时把状态改为已上传
                                auditSpIMaterial.setStatus("20");
                                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                            }

                            if (StringUtil.isBlank(materialsource)) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                            }
                            spiMaterialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                    : auditTaskMaterial.getPage_num());
                            spiMaterialJson.put("cliengguid", auditSpIMaterial.getCliengguid());
                            spiMaterialJson.put("stantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());
                            spiMaterialJson.put("materialsource", materialsource);
                            spiMaterialJson.put("type", auditTaskMaterial.getType());
                            spiMaterialJson.put("materialguid", auditSpIMaterial.getRowguid());
                            spiMaterialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());
                            String materialtype = "";
                            if (StringUtil.isNotBlank(auditTaskMaterial.getMaterialtype())) {
                                String materialTypeMain = auditTaskMaterial.getMaterialtype().substring(0, 4);
                                if ("tscl".equals(materialTypeMain)) {
                                    materialtype = "【图审】";
                                }
                                else if ("chcl".equals(materialTypeMain)) {
                                    materialtype = "【测绘】";
                                    if (typeFlag == 0) {
                                        typeFlag = 2;
                                    }
                                    else if (typeFlag == 1) {
                                        typeFlag = 3;
                                    }
                                }
                                else if ("dpcl".equals(materialTypeMain)) {
                                    materialtype = "【多评】";
                                    if (typeFlag == 0) {
                                        typeFlag = 1;
                                    }
                                    else if (typeFlag == 2) {
                                        typeFlag = 3;
                                    }
                                }
                                else if ("lscl".equals(materialTypeMain)) {
                                    materialtype = "【联审】";
                                }
                            }
                            spiMaterialJson.put("materialtype", materialtype);
                            spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                            spiMaterialJson.put("materialinstanceguid", auditTaskMaterial.getRowguid());
                            spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                            spiMaterialJson.put("rowguid", auditTaskMaterial.getRowguid());
                            // 是否共享材料
                            spiMaterialJson.put("shared", auditSpIMaterial.getShared());
                            // 共享材料返回共享材料的必要性、容缺、提交方式
                            // 是否必须 10必须 20非必须
                            spiMaterialJson.put("necessity", auditSpIMaterial.getNecessity());
                            // 是否容缺
                            String rongque = "0";// 默认不容缺
                            if (StringUtil.isNotBlank(auditSpIMaterial.getNecessity())
                                    && "10".equals(auditSpIMaterial.getNecessity())) {
                                rongque = auditSpIMaterial.getAllowrongque();// 只有必要的材料才需要显示容缺
                            }
                            spiMaterialJson.put("allowrongque", rongque);
                            spiMaterialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditSpIMaterial.getSubmittype())));
                            // 示例表格
                            String exampleClientGuid = auditTaskMaterial.getExampleattachguid();
                            if (StringUtil.isNotBlank(exampleClientGuid)) {
                                int exampleAttachCount = iAttachService.getAttachCountByClientGuid(exampleClientGuid);
                                if (exampleAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(exampleClientGuid);
                                    spiMaterialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 空白表格
                            String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                            if (StringUtil.isNotBlank(templateClientGuid)) {
                                int templateAttachCount = iAttachService.getAttachCountByClientGuid(templateClientGuid);
                                if (templateAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(templateClientGuid);
                                    spiMaterialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 3.12、 判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton = 0; // 按钮显示方式
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                    ? "1"
                                    : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                            if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                                // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() != null
                                        && auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else {
                                    // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.6.2、如果关联了证照库
                                if (count > 0) {
                                    // 4.6.2.1、获取材料类别
                                    String materialType = auditTaskMaterial.getMaterialtype();
                                    if (StringUtil.isBlank(materialType)) {
                                        materialType = "0";
                                    }
                                    if (Integer.parseInt(materialType) == 1) {
                                        // 4.6.2.1.1、已引用证照库
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                    }
                                    else if (Integer.parseInt(materialType) == 2) {
                                        // 4.6.2.1.2、已引用批文
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                    }
                                    else {
                                        // 4.6.2.1.3、已引用材料
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                    }
                                }
                                else {
                                    // 4.6.2.2、如果没有附件，则标识为未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);

                                }
                            }
                            spiMaterialJson.put("showbutton", showButton);
                            spiMaterialJson.put("needload", needLoad);
                            spiMaterialJsonList.add(spiMaterialJson);

                        }

                        long endTime3 = System.currentTimeMillis();
                        log.info("subAppGuid:" + subAppGuid + ",3工改获取材料运行时间为：" + (endTime3 - endTime2) / 100 + "秒");

                        // 4、存入事项下的材料列表
                        spiTaskJson.put("typeflag", typeFlag);
                        spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                        spiTaskJsonList.add(spiTaskJson);

                        long endTime = System.currentTimeMillis();
                        log.info("subAppGuid:" + subAppGuid + ",工改获取材料运行时间为：" + (endTime - startTime) / 100 + "秒");

                    }
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("spitasklist", spiTaskJsonList);
                    if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {
                        dataJson.put("formid", auditspphase.getStr("formid"));
                        dataJson.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                        dataJson.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                    }
                    log.info("=======结束调用getSPTaskAndMaterialInstanceList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取事项及材料清单成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "当前阶段不可提交材料！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSPTaskAndMaterialInstanceList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项及材料清单异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取阶段申报相关办件进度接口（查看子申报办理过程页面加载调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSubappProjectList", method = RequestMethod.POST)
    public String getSubappProjectList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSubappProjectList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                // 3、获取子申报事项实例列表 只查补正的
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 如果project为空并且status为空或者0 代表该事项没有提交，过滤。
                    if (StringUtil.isBlank(auditSpITask.getProjectguid())
                            && (StringUtil.isBlank(auditSpITask.getStatus()) || "0".equals(auditSpITask.getStatus()))) {
                        continue;
                    }
                    // 3.1、返回阶段实例中的事项列表
                    JSONObject spiTaskJson = new JSONObject();
                    spiTaskJson.put("taskname", auditSpITask.getTaskname());
                    spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                    // 3.2、返回该事项下的材料列表
                    List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                    // 3.3、返回该事项下的补正材料列表
                    List<JSONObject> bzSpiMaterialJsonList = new ArrayList<JSONObject>();
                    // 4、获取事项信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false).getResult();
                    // 4.1、获取办件信息
                    AuditProject auditProject = null;
                    if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                        auditProject = iAuditProject.getAuditProjectByRowGuid("status", auditSpITask.getProjectguid(),
                                auditTask.getAreacode()).getResult();
                    }
                    if (auditProject != null) {
                        spiTaskJson.put("projectstatus", auditProject.getStatus());
                    }
                    else {
                        spiTaskJson.put("projectstatus", 20);
                    }
                    // 3.4、获取事项下的材料列表
                    List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                            .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                    // 6、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                        JSONObject spiMaterialJson = new JSONObject();
                        // 6.1、获取事项实例中的材料实例信息
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                .getResult();
                        String materialsource = auditTaskMaterial.getFile_source();
                        // 3.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                        if (auditSpIMaterial == null) {
                            AuditSpShareMaterialRelation auditSpShareMaterialRelations = iAuditSpShareMaterialRelation
                                    .getRelationByID(auditSpITask.getBusinessguid(), auditTaskMaterial.getMaterialid())
                                    .getResult();
                            // 3.7、如果存在共享材料/结果材料
                            if (auditSpShareMaterialRelations != null) {
                                AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                        .getAuditSpShareMaterialByShareMaterialGuid(
                                                auditSpShareMaterialRelations.getSharematerialguid(),
                                                auditSpITask.getBusinessguid())
                                        .getResult();
                                if (auditSpShareMaterial != null) {
                                    materialsource = auditSpShareMaterial.getFile_source();
                                }
                                // 3.9、获取存在的材料共享/结果关系
                                auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(subAppGuid,
                                        auditSpShareMaterialRelations.getSharematerialguid()).getResult();
                                // 3.10、判断是否是结果材料，如果是就不显示该材料
                                if (auditSpIMaterial == null
                                        || ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                    continue;
                                }
                            }
                            else {
                                // 3.11没有找到共享材料数据，结束该循环
                                continue;
                            }
                        }
                        String materialtype = "";
                        if (StringUtil.isNotBlank(auditTaskMaterial.getMaterialtype())) {
                            String materialTypeMain = auditTaskMaterial.getMaterialtype().substring(0, 4);
                            if ("tscl".equals(materialTypeMain)) {
                                materialtype = "【图审】";
                            }
                            else if ("chcl".equals(materialTypeMain)) {
                                materialtype = "【测绘】";
                            }
                            else if ("dpcl".equals(materialTypeMain)) {
                                materialtype = "【多评】";
                            }
                            else if ("lscl".equals(materialTypeMain)) {
                                materialtype = "【联审】";
                            }
                        }
                        spiMaterialJson.put("materialtype", materialtype);
                        spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                        if (StringUtil.isBlank(materialsource)) {
                            materialsource = "申请人自备";
                        }
                        else {
                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                        }
                        // TODO 与审批系统确认代码项
                        if ("1".equals(auditSpIMaterial.getIsbuzheng())
                                && (auditProject == null || auditProject.getStatus() == 28
                                        || auditProject.getStatus() == 37)) {
                            // 两种补正
                            JSONObject bzSpiMaterialJson = new JSONObject();
                            materialtype = "";
                            if (StringUtil.isNotBlank(auditTaskMaterial.getMaterialtype())) {
                                String materialTypeMain = auditTaskMaterial.getMaterialtype().substring(0, 4);
                                if ("tscl".equals(materialTypeMain)) {
                                    materialtype = "【图审】";
                                }
                                else if ("chcl".equals(materialTypeMain)) {
                                    materialtype = "【测绘】";
                                }
                                else if ("dpcl".equals(materialTypeMain)) {
                                    materialtype = "【多评】";
                                }
                                else if ("lscl".equals(materialTypeMain)) {
                                    materialtype = "【联审】";
                                }
                            }
                            bzSpiMaterialJson.put("materialtype", materialtype);
                            bzSpiMaterialJson.put("bzmaterialinstancename", auditTaskMaterial.getMaterialname());
                            bzSpiMaterialJson.put("materialsource", materialsource);
                            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                            // 仅当子申报状态为待补正时，才返回待补正状态 28
                            if (("34".equals(auditSpISubapp.getStr("status"))
                                    || ZwfwConstant.LHSP_Status_DBJ.equals(auditSpISubapp.getStatus()))) {
                                int status = 28;
                                if (auditProject != null) {
                                    status = auditProject.getStatus();
                                }
                                spiTaskJson.put("projectstatus", status);
                            }
                            // 纸质份数
                            bzSpiMaterialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                    : auditTaskMaterial.getPage_num());
                            // 是否共享材料
                            if (ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getShared())) {
                                bzSpiMaterialJson.put("shared", 1);
                            }
                            // 是否必须 10必须 20非必须
                            if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())) {
                                bzSpiMaterialJson.put("necessity", 1);
                            }
                            // 是否容缺
                            int rongque = 0;// 默认不容缺
                            if (StringUtil.isNotBlank(auditSpIMaterial.getNecessity())
                                    && "10".equals(auditSpIMaterial.getNecessity())
                                    && ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getAllowrongque())) {
                                rongque = 1;// 只有必要的材料才需要显示容缺
                            }
                            bzSpiMaterialJson.put("rowguid", auditTaskMaterial.getRowguid());
                            bzSpiMaterialJson.put("allowrongque", rongque);
                            bzSpiMaterialJson.put("cliengguid", auditSpIMaterial.getCliengguid());
                            bzSpiMaterialJson.put("stantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());
                            bzSpiMaterialJson.put("type", auditTaskMaterial.getType());
                            bzSpiMaterialJson.put("materialguid", auditSpIMaterial.getRowguid());
                            bzSpiMaterialJson.put("taskmaterialguid", auditSpIMaterial.getMaterialguid());// 事项材料标识
                            bzSpiMaterialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());
                            // 示例表格
                            String exampleClientGuid = auditTaskMaterial.getExampleattachguid();
                            if (StringUtil.isNotBlank(exampleClientGuid)) {
                                int exampleAttachCount = iAttachService.getAttachCountByClientGuid(exampleClientGuid);
                                if (exampleAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(exampleClientGuid);
                                    spiMaterialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                    bzSpiMaterialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 空白表格
                            String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                            if (StringUtil.isNotBlank(templateClientGuid)) {
                                int templateAttachCount = iAttachService.getAttachCountByClientGuid(templateClientGuid);
                                if (templateAttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(templateClientGuid);
                                    spiMaterialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                    bzSpiMaterialJson.put("templateattachguid",
                                            frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            bzSpiMaterialJson.put("status", auditSpIMaterial.getStatus());
                            // 3.12、 判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton = 0; // 按钮显示方式
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                    ? "1"
                                    : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                            if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                                // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() != null
                                        && auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else {
                                    // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.6.2、如果关联了证照库
                                if (count > 0) {
                                    // 4.6.2.1、获取材料类别
                                    String materialType = auditTaskMaterial.getStr("materialType");
                                    if ("1".equals(materialType)) {
                                        // 4.6.2.1.1、已引用证照库
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                    }
                                    else if ("2".equals(materialType)) {
                                        // 4.6.2.1.2、已引用批文
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                    }
                                    else {
                                        // 4.6.2.1.3、已引用材料
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                    }
                                }
                                else {
                                    // 4.6.2.2、如果没有附件，则标识为未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                }
                            }
                            bzSpiMaterialJson.put("showbutton", showButton);
                            bzSpiMaterialJson.put("needload", needLoad);
                            bzSpiMaterialJsonList.add(bzSpiMaterialJson);
                        }
                        else {
                            spiMaterialJson.put("materialsource", materialsource);
                            spiMaterialJsonList.add(spiMaterialJson);
                        }
                        spiMaterialJson.put("rowguid", auditTaskMaterial.getRowguid());

                    }
                    spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                    spiTaskJson.put("bzspimateriallist", bzSpiMaterialJsonList);
                    spiTaskJsonList.add(spiTaskJson);
                }

                // 3.4.5.1、对事项材料进行排序
                Collections.sort(spiTaskJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        // 3.4.5.1、优先对比材料必要性（必要在前）
                        @SuppressWarnings("unchecked")
                        List<JSONObject> aa = (List<JSONObject>) b1.get("bzspimateriallist");
                        @SuppressWarnings("unchecked")
                        List<JSONObject> bb = (List<JSONObject>) b2.get("bzspimateriallist");
                        Integer b = bb.size();
                        Integer a = aa.size();
                        return b.compareTo(a);
                    }
                });

                dataJson.put("subappProjectList", spiTaskJsonList);
                log.info("=======结束调用getSubappProjectList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取阶段申报相关办件进度接口！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSubappProjectList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取阶段申报相关办件进度接口：" + e.getMessage(), "");
        }
    }

    /**
     * 判断材料是否上传的接口（上传附件页面关闭后调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkSPIMaterialStatus", method = RequestMethod.POST)
    public String checkSPIMaterialStatus(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取并联审批材料附件业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取并联审批材料标识
                String materialGuid = obj.getString("materialguid");
                // 1.3、获取共享材料实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.4、获取材料提交状态
                String status = obj.getString("status");
                // 1.5、获取材料类型
                String type = obj.getString("type");
                // 1.6、获取材料对应附件是否需要上传到证照库（只有C级证照才需要）
                String needLoad = obj.getString("needload");
                // 1.7、获取主题实例标识
                String subAppGuid = obj.getString("subappguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                // 4、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                        .getSpIMaterialBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    if (auditSpIMaterial.getRowguid().equals(materialGuid)) {
                        // 5、获取套餐办件材料对应的附件数量
                        int attachCount = iAttachService.getAttachCountByClientGuid(clientGuid);
                        int intStatus = Integer.parseInt(status);
                        if (attachCount > 0) {
                            // 5.1、若材料有对应的附件，说明电子材料已提交
                            switch (intStatus) {
                                // 5.1.1、材料原先的状态为未提交更新为电子已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                                    break;
                                // 5.1.2、材料原先的状态为纸质已提交更新为电子和纸质都提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                                    break;
                                default:
                                    break;
                            }
                        }
                        else {
                            // 5.2、若材料没有对应的附件，说明电子材料没有提交
                            switch (intStatus) {
                                // 5.2.1、材料原先的状态为电子已提交更新为未提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                                    break;
                                // 5.2.2、材料原先的状态为电子和纸质都提交更新为纸质已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                                    break;
                                default:
                                    break;
                            }
                        }
                        auditSpIMaterial.setStatus(String.valueOf(intStatus));
                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                        // 之前这边材料列表展示的时候做了去重的处理导致生成办件的时候有的办件材料没有附件
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("SUBAPPGUID", subAppGuid);
                        // 与当前材料同名称
                        sql.eq("MATERIALNAME", auditSpIMaterial.getMaterialname());
                        // 剔除当前材料
                        sql.nq("MATERIALGUID", auditSpIMaterial.getMaterialguid());
                        List<AuditSpIMaterial> cfAuditSpIMaterials = iAuditSpIMaterial
                                .getSpIMaterialByCondition(sql.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(cfAuditSpIMaterials)) {
                            for (AuditSpIMaterial spIMaterial : cfAuditSpIMaterials) {
                                // 当前材料有上传附件
                                if (attachCount > 0) {
                                    iAttachService.copyAttachByClientGuid(auditSpIMaterial.getCliengguid(), null, null,
                                            spIMaterial.getCliengguid());
                                }
                                // 当前材料未上传附件
                                else {
                                    iAttachService.deleteAttachByGuid(clientGuid);
                                }
                                spIMaterial.setStatus(String.valueOf(intStatus));
                                iAuditSpIMaterial.updateSpIMaterial(spIMaterial);
                            }
                        }

                        // 6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                        int showButton = 0;
                        String cliengTag = "";
                        if (StringUtil.isBlank(shareMaterialIGuid)) {
                            // 6.1、如果没有从证照库引用附件，则为普通附件及填表
                            if (String.valueOf(WorkflowKeyNames9.MaterialType_Form).equals(type)) {
                                // 6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = attachCount > 0 ? 4 : 3;
                            }
                            else {
                                // 6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = attachCount > 0 ? 1 : 0;
                            }
                        }
                        else {
                            // 5.2、如果关联了证照库
                            if (ZwdtConstant.NEEDLOAD_Y.equals(needLoad)) {
                                // 5.2.1、如果需要更新证照库，有附件则显示已上传，没有附件则显示未未上传
                                showButton = attachCount > 0 ? 1 : 0;
                                // 5.2.2、更新证照库版本
                                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                List<JSONObject> rsInfos = iCertAttachExternal
                                        .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                int countRs = 0;
                                if (rsInfos != null && rsInfos.size() > 0) {
                                    countRs = rsInfos.size();
                                }
                                // 5.2.2.1、数量不一致
                                if (attachCount > 0 && attachCount != countRs) {
                                    certInfo.setCertcliengguid(clientGuid);
                                    shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                            ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                    // 5.2.2.1.1、关联到并联审批材料
                                    auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                }
                                else if (attachCount == countRs) {
                                    // 5.2.2.2、数量一致继续比较
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());

                                    frameAttachInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                    boolean isModify = false;
                                    if (rsInfos != null && rsInfos.size() > 0) {
                                        rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            // 如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                            cliengTag = frameAttachInfos.get(i).getCliengTag();
                                            if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                    || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                    && rsInfos.get(i).getLong("size").longValue() != frameAttachInfos
                                                            .get(i).getAttachLength().longValue()) {
                                                isModify = true;
                                                break;
                                            }
                                            else {
                                                // 若文件未发生改变，则按钮显示维持原样
                                                CertInfo certInfo2 = iCertInfoExternal
                                                        .getCertInfoByRowguid(shareMaterialIGuid);
                                                if (certInfo2 != null) {
                                                    if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                        String materialType = certInfo2.getMaterialtype();
                                                        if (Integer.parseInt(materialType) == 1) {
                                                            showButton = Integer.parseInt(
                                                                    ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                        }
                                                        else if (Integer.parseInt(materialType) == 2) {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                        }
                                                        else {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        isModify = true;
                                    }
                                    if (isModify) {
                                        certInfo.setCertcliengguid(clientGuid);
                                        shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                                ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                        // 5.2.2.2.2、关联到并联审批材料
                                        auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                    }
                                }
                            }
                            else {
                                // 5.2.3、如果不需要更新证照库，有附件则则根据共享材料的类别显示已引用共享材料，没有附件则显示未未上传
                                if (attachCount > 0) {
                                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                    if (certInfo != null) {
                                        // 5.2.3.1、比较证照实例里的附件和上传的附件
                                        List<JSONObject> rsInfos = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        int countRs = 0;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            countRs = rsInfos.size();
                                        }
                                        if (attachCount != countRs) {
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                        }
                                        // 5.2.3.1.1、数量一致比较详细的附件内容
                                        else if (attachCount == countRs) {
                                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                    .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                            frameAttachInfos
                                                    .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                            if (rsInfos != null && rsInfos.size() > 0) {
                                                rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                                for (int i = 0; i < rsInfos.size(); i++) {
                                                    // 5.2.3.1.2、AttachStorageGuid不同说明附件改动过
                                                    cliengTag = frameAttachInfos.get(i).getCliengTag();
                                                    if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                            || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                            && rsInfos.get(i).getLong("size")
                                                                    .longValue() != frameAttachInfos.get(i)
                                                                            .getAttachLength().longValue()) {
                                                        showButton = Integer
                                                                .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                        break;
                                                    }
                                                    else {
                                                        // 5.2.3.1.3、若文件未发生改变，则按钮显示维持原样
                                                        CertInfo certInfo2 = iCertInfoExternal
                                                                .getCertInfoByRowguid(shareMaterialIGuid);
                                                        if (certInfo2 != null) {
                                                            if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                                String materialType = certInfo2.getMaterialtype();
                                                                if (Integer.parseInt(materialType) == 1) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                                }
                                                                else if (Integer.parseInt(materialType) == 2) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                                }
                                                                else {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        // 如果查到附件但证照库里没有，说明这个共享材料是自己上传的
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                    }
                                }
                                else {
                                    // 5.2.3.2、未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                }
                            }

                        }
                        dataJson.put("showbutton", showButton);
                        dataJson.put("needload", needLoad);
                        dataJson.put("status", intStatus);
                        dataJson.put("sharematerialiguid", shareMaterialIGuid);
                    }
                }
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkSPIMaterialStatus接口参数：params【" + params + "】=======");
            log.info("=======checkSPIMaterialStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取申报中心列表（申报中心页面加载调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanySubappList", method = RequestMethod.POST)
    public String getCompanySubappList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanySubappList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、项目名称搜索条件
                String keyWord = obj.getString("keyword");
                // 1.2、每页数量
                String pageSize = obj.getString("pagesize");
                // 1.3、当前页
                String currentPage = obj.getString("currentpage");
                // 1.4、排序方式 0：升序1：降序
                String clockWise = obj.getString("clockwise");
                // 1.5、申报状态
                String status = obj.getString("status");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                    // 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    String strWherecode = "";// 拼接被授权的所有企业统一社会信用代码/组织机构代码证
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        if (StringUtil.isNotBlank(auditOnlineCompanyGrant.getCompanyid())) {
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByCompanyId(auditOnlineCompanyGrant.getCompanyid()).getResult();
                            if (auditRsCompanyBaseinfo != null) {
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                                    strWherecode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                                }
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                                    strWherecode += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";

                                }
                            }
                        }
                    }
                    // 2.2、如果当前登陆人是法人，则没有授权信息。需要查法人身份证所属的企业
                    SqlConditionUtil sqlLegal = new SqlConditionUtil();
                    sqlLegal.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlLegal.isBlankOrValue("is_history", "0");
                    sqlLegal.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlLegal.getMap()).getResult();
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                            strWherecode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                        }
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                            strWherecode += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";
                        }
                    }
                    if (StringUtil.isBlank(strWherecode)) {
                        // 2.2.1 如果登陆人没有被授权的企业或被解除授权，则提示“无权”
                        return JsonUtils.zwdtRestReturn("0", "您无权查询项目", "");
                    }
                    else {
                        // 2.2.1 去除字段的最后一个逗号
                        strWherecode = strWherecode.substring(0, strWherecode.length() - 1);
                    }
                    // 查询项目
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtils.isNotBlank(strWherecode)) {
                        sql.in("itemlegalcertnum", strWherecode);
                    }

                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                            .selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                    String itemguid = "";
                    for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                        // 获取itemguid
                        itemguid += "'" + auditRsItemBaseinfo.getRowguid() + "',";
                    }
                    if (StringUtil.isNotBlank(itemguid)) {
                        itemguid = itemguid.substring(0, itemguid.length() - 1);
                    }
                    else {
                        itemguid = "''";
                    }
                    // 查询biguid
                    sql.clear();
                    if (StringUtils.isNotBlank(itemguid)) {
                        sql.in("yewuguid", itemguid);
                    }

                    List<AuditSpInstance> auditSpInstances = iAuditSpInstance.getInstancelistByCondition(sql.getMap())
                            .getResult();
                    String biguid = "";
                    for (AuditSpInstance auditSpInstance : auditSpInstances) {
                        biguid += "'" + auditSpInstance.getRowguid() + "',";
                    }
                    if (StringUtil.isNotBlank(biguid)) {
                        biguid = biguid.substring(0, biguid.length() - 1);
                    }
                    else {
                        biguid = "''";
                    }
                    SqlConditionUtil sqlSelectAllSubapps = new SqlConditionUtil();
                    // 2.3 拼接查询条件

                    sqlSelectAllSubapps.in("biguid", biguid);
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlSelectAllSubapps.like("subappname", keyWord);
                    }
                    if (StringUtil.isNotBlank(status)) {
                        if (ZwfwConstant.LHSP_Status_DBJ.equals(status)) {
                            status = "'" + ZwfwConstant.LHSP_Status_DBJ + "','" + ZwfwConstant.LHSP_Status_YSDBZ + "'";
                            sqlSelectAllSubapps.in("status", status);
                        }
                        else if (ZwfwConstant.LHSP_Status_YPS.equals(status)) {
                            status = "'" + ZwfwConstant.LHSP_Status_YPS + "','21','22'";
                            sqlSelectAllSubapps.in("status", status);
                        }
                        else {
                            sqlSelectAllSubapps.eq("status", status);
                        }
                    }
                    else {
                        sqlSelectAllSubapps.nq("status", "-1");// 过滤内网的草稿状态
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(clockWise)) {
                        sqlSelectAllSubapps.setOrderDesc("createdate");
                    }
                    else {
                        sqlSelectAllSubapps.setOrderAsc("createdate");
                    }
                    // 3、 分页查询用户申报列表
                    PageData<AuditSpISubapp> auditSpISubappPageData = iAuditSpISubapp.selectSubappsPageDataByCondition(
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            sqlSelectAllSubapps.getMap()).getResult();
                    List<JSONObject> itemList = new ArrayList<>();
                    Integer totalCount = 0;
                    totalCount = auditSpISubappPageData.getRowCount();
                    List<AuditSpISubapp> auditSpISubapps = auditSpISubappPageData.getList();
                    // 3.1 遍历所有子申报
                    for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                        JSONObject objJson = new JSONObject();
                        // 3.1.1 返回参数
                        objJson.put("subappname", auditSpISubapp.getSubappname());
                        objJson.put("subappguid", auditSpISubapp.getRowguid());
                        objJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                        objJson.put("createtime",
                                EpointDateUtil.convertDate2String(auditSpISubapp.getCreatedate(), "yyyy-MM-dd HH:mm"));
                        // 判断是主项目还是子项目，仅返回主项目的itemguid
                        String itemGuid = "";
                        if (StringUtil.isNotBlank(auditSpISubapp.getYewuguid())) {
                            AuditRsItemBaseinfo auditRsItem = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                            if (auditRsItem != null) {
                                if (StringUtil.isNotBlank(auditRsItem.getParentid())) {
                                    // 父级id不为空，代表有主项目，返回主项目标识
                                    itemGuid = auditRsItem.getParentid();
                                }
                                else {
                                    itemGuid = auditRsItem.getRowguid();
                                }
                            }
                        }
                        objJson.put("itemguid", itemGuid);
                        objJson.put("status", auditSpISubapp.getStatus());
                        // 3.1.2获取阶段名称
                        AuditSpPhase auditSpPhase = iAuditSpPhase
                                .getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid()).getResult();
                        objJson.put("phasename", auditSpPhase.getPhasename());
                        // 3.1.3获取项目名称
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditSpISubapp.getBiguid())
                                .getResult();
                        if (auditSpInstance != null) {
                            objJson.put("biguid", auditSpInstance.getRowguid());
                            objJson.put("businessguid", auditSpInstance.getBusinessguid());
                            objJson.put("itemname", auditSpInstance.getItemname());
                        }
                        // 查询一下是否所有事项都已经在办理了。
                        // 查询需要办理的事项的数量
                        List<AuditSpITask> auditSpITasks = iAuditSpITask
                                .getTaskInstanceBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                        int allNum = auditSpITasks.size();
                        int inNum = 0;
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            // 如果project不为空证明该事项已经分发，或者status不为空和0证明外网已提交过
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())
                                    || (StringUtil.isNotBlank(auditSpITask.getStatus())
                                            && !"0".equals(auditSpITask.getStatus()))) {
                                inNum++;
                            }
                        }
                        int allblz = (allNum - inNum) == 0 ? 1 : 0;
                        objJson.put("allblz", allblz);
                        itemList.add(objJson);
                    }
                    // 4、返回Json数据
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("itemlist", itemList);
                    dataJson.put("totalcount", totalCount);
                    log.info("=======结束调用getCompanySubappList接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取申报列表成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getCompanySubappList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", " 获取项目列表失败", "");
        }
    }

    /**
     * 获取简单阶段基本信息接口（阶段申请时页面上方加载时调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSimplePhaseInfo", method = RequestMethod.POST)
    public String getSimplePhaseInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSimplePhaseInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取阶段Guid
                String phaseGuid = obj.getString("phaseguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取项目信息
                AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();
                if (auditSpPhase != null) {
                    dataJson.put("phasename", auditSpPhase.getPhasename());
                    dataJson.put("phaseformurl", auditSpPhase.getEformurl());
                    // dataJson.put("phaseformurl",
                    // "../../../shared/sharedoperatemain?categoryGuid=7ba6dc14-966b-47e3-b43a-75a20aa134ce&tableIds=13");
                }
                log.info("=======结束调用getSimplePhaseInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取简单阶段基本信息成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSimplePhaseInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取简单阶段基本信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取项目进度接口（项目进度调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getFlowData", method = RequestMethod.POST)
    public String getFlowData(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getFlowData接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取业务实例
                String biGuid = obj.getString("biguid");
                if (StringUtil.isNotBlank(biGuid)) {
                    long starttime = System.currentTimeMillis();
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    long starttime2 = System.currentTimeMillis();
                    System.out.println(
                            "===========auditSpInstance==========satrt=============" + (starttime2 - starttime));
                    if (auditSpInstance != null) {
                        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                        long starttime3 = System.currentTimeMillis();
                        System.out.println("===========auditOnlineRegister==========satrt============="
                                + (starttime3 - starttime2));
                        if (auditOnlineRegister != null) {
                            ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                            if (!zwdtUserAuthValid.userValid(auditOnlineRegister, auditSpInstance.getStr("yewuguid"),
                                    12)) {
                                return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                        }
                        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                        long starttime4 = System.currentTimeMillis();
                        System.out.println(
                                "===========auditSpBusiness==========satrt=============" + (starttime4 - starttime3));
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                        long starttime5 = System.currentTimeMillis();
                        System.out.println("===========auditRsItemBaseinfo==========satrt============="
                                + (starttime5 - starttime4));
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("name", auditSpBusiness.getBusinessname());
                        dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                        dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        dataJson.put("startdate",
                                EpointDateUtil.convertDate2String(auditSpInstance.getCreatedate(), "y年M月d日") + "-当前");
                        int days = (int) ((System.currentTimeMillis() - auditSpInstance.getCreatedate().getTime())
                                / (1000 * 3600 * 24));
                        dataJson.put("days", days);
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("businedssguid", auditSpBusiness.getRowguid());
                        sql.setOrder("ordernumber", "desc");
                        List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
                        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp
                                .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
                        long starttime6 = System.currentTimeMillis();
                        System.out.println(
                                "===========auditSpPhases==========satrt=============" + (starttime6 - starttime5));
                        List<JSONObject> phaseList = new ArrayList<>();
                        for (AuditSpPhase auditSpPhase : auditSpPhases) {
                            JSONObject phaseobj = new JSONObject();
                            phaseobj.put("name", auditSpPhase.getPhasename());
                            phaseobj.put("key", auditSpPhase.getRowguid());
                            Date startdate = null;
                            Date endDate = null;
                            // 记录未结束
                            boolean isend = true;
                            String status = "";
                            for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                                if (auditSpISubapp.getPhaseguid().equals(auditSpPhase.getRowguid())) {
                                    if (startdate == null || startdate.compareTo(auditSpISubapp.getCreatedate()) == 1) {
                                        startdate = auditSpISubapp.getCreatedate();
                                    }
                                    if (auditSpISubapp.getFinishdate() == null) {
                                        isend = false;
                                    }
                                    if (endDate == null || auditSpISubapp.getFinishdate() == null
                                            || endDate.compareTo(auditSpISubapp.getFinishdate()) == -1) {
                                        endDate = auditSpISubapp.getFinishdate();
                                    }
                                    status = auditSpISubapp.getStatus();
                                }
                            }
                            String start = startdate == null ? "未开始"
                                    : EpointDateUtil.convertDate2String(startdate, "y年M月d日");
                            phaseobj.put("startdate", start);
                            phaseobj.put("status", status);
                            phaseobj.put("enddate",
                                    (isend && endDate != null) ? EpointDateUtil.convertDate2String(endDate, "y年M月d日")
                                            : "未结束");
                            phaseobj.put("phasetime", auditSpPhase.getPhasetime());
                            phaseList.add(phaseobj);
                        }
                        long starttime7 = System.currentTimeMillis();
                        System.out.println("===========forforforforauditSpPhases==========satrt============="
                                + (starttime7 - starttime6));
                        SqlConditionUtil sqlSelectProject = new SqlConditionUtil();
                        sqlSelectProject.eq("areacode", auditSpBusiness.getAreacode());
                        sqlSelectProject.eq("biguid", biGuid);
                        Map<String, Object> taskMap = new HashMap<>();
                        List<AuditProject> auditProjects = iAuditProject.getAuditProjectListByCondition(
                                "task_id,status,projectname,ouname,banjiedate,operatedate", sqlSelectProject.getMap())
                                .getResult();
                        long starttime8 = System.currentTimeMillis();
                        System.out.println("===========forforforforauditSpPhases==========satrt============="
                                + (starttime8 - starttime7));
                        DecimalFormat df = new DecimalFormat("0.00");
                        for (AuditProject auditProject : auditProjects) {
                            StringBuilder projectname = new StringBuilder();
                            StringBuilder projecttime = new StringBuilder();
                            AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                            if (!taskMap.containsKey(auditProject.getTask_id())) {
                                JSONArray arrjson = new JSONArray();
                                String status = "1";
                                int projectstatus = auditProject.getStatus();
                                if (projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                    status = "2";
                                }
                                else if (ZwfwConstant.BANJIAN_STATUS_YJJ < projectstatus
                                        && projectstatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                    status = "3";
                                }
                                else if (projectstatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                    status = "4";
                                }
                                arrjson.add(status);
                                if ("4".equals(status)) {
                                    arrjson.add(1);
                                }
                                else {
                                    arrjson.add(0);
                                }
                                arrjson.add(1);
                                projectname.append(auditProject.getProjectname()).append(" (")
                                        .append(iOuService.getOuByOuGuid(auditProject.getOuguid()).getOuname())
                                        .append(")");
                                if (auditProject.getBanjiedate() != null) {
                                    int daynum = (int) ((auditProject.getBanjiedate().getTime()
                                            - auditProject.getOperatedate().getTime()) / (1000 * 3600 * 24));
                                    projecttime
                                            .append(EpointDateUtil.convertDate2String(auditProject.getOperatedate(),
                                                    "y年M月d日"))
                                            .append("-").append(EpointDateUtil
                                                    .convertDate2String(auditProject.getBanjiedate(), "y年M月d日"))
                                            .append(" 用时").append(daynum).append("工作日");
                                }
                                else {
                                    projecttime.append(
                                            EpointDateUtil.convertDate2String(auditProject.getOperatedate(), "y年M月d日"))
                                            .append("收件");
                                }
                                arrjson.add(projectname.toString());
                                arrjson.add(projecttime.toString());
                                if ("4".equals(status) || auditProjectSparetime == null) {
                                    arrjson.add("1");
                                }
                                else {
                                    int spend = auditProjectSparetime.getSpendminutes();
                                    int all = auditProjectSparetime.getSpendminutes()
                                            + auditProjectSparetime.getSpareminutes();
                                    arrjson.add(df.format((float) spend / all));
                                }
                                taskMap.put(auditProject.getTask_id(), arrjson);
                            }
                            else {
                                JSONArray arrjson = (JSONArray) taskMap.get(auditProject.getTask_id());
                                String status = "1";
                                int projectstatus = auditProject.getStatus();
                                if (projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                    status = "2";
                                }
                                else if (ZwfwConstant.BANJIAN_STATUS_YJJ < projectstatus
                                        && projectstatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                    status = "3";
                                }
                                else if (projectstatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                    status = "4";
                                }
                                if ("4".equals(status)) {
                                    arrjson.set(0, status);
                                    arrjson.set(1, (int) arrjson.get(1) + 1);
                                    arrjson.set(2, (int) arrjson.get(2) + 1);
                                }
                                else {
                                    arrjson.set(2, (int) arrjson.get(2) + 1);
                                    arrjson.set(0, status);
                                }
                            }
                        }
                        System.out.println("===========forforforfor==========end============="
                                + (System.currentTimeMillis() - starttime8));
                        dataJson.put("phaselist", phaseList);
                        dataJson.put("tasklist", taskMap);
                        return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson);
                    }
                }
                return JsonUtils.zwdtRestReturn("0", "流程图查询失败", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "流程图查询异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（并联审批申报提交时调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                int noSubmitNum = 0;// 必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取材料实例信息
                        int status = StringUtils.isNotBlank(materialStatus)?Integer.parseInt(materialStatus):ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT; // 办件材料表当前材料提交的状态
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(materialGuid)
                                .getResult();
                        String isRongque = "";
                        String necessity = "";
                        // 是否必须 10必须 20非必须
                        necessity = auditSpIMaterial.getNecessity();
                        // 是否容缺
                        isRongque = auditSpIMaterial.getStr("allowrongque");// 只有必要的材料才需要显示容缺
                        if (auditSpIMaterial != null) {
                            // 3.1、图审联审的必要材料无需提交
                            String materialType = auditSpIMaterial.getStr("materialtype");
                            if (StringUtil.isNotBlank(materialType)
                                    && ("tscl".equals(materialType) || "lscl".equals(materialType))) {
                                continue;
                            }
                            // 3.2、获取系统参数：纸质必要材料外网是否一定要上传
                            String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                            paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity) ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : paperUnnecrsstity;
                            // 3.3、必要非容缺材料没有提交：
                            if (ZwfwConstant.NECESSITY_SET_YES.equals(necessity)
                                    && !ZwdtConstant.STRING_YES.equals(isRongque)
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                // 3.3.1、如果纸质材料外网不必上传，对提交纸质材料即可的材料不做限制
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                    // 10代表提交电子材料；20代表提交纸质材料；35代表提交电子或纸质材料；40代表同时提交电子和纸质材料
                                    if ("10".equals(auditSpIMaterial.getSubmittype())
                                            || "40".equals(auditSpIMaterial.getSubmittype())) {
                                        noSubmitNum++;
                                    }
                                }
                                // 3.3.2、如果纸质必要材料外网仍需上传
                                else {
                                    noSubmitNum++;
                                }
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkAllMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String subAppGuid = obj.getString("subappguid");
                // 1.2、 获取选中的事项
                String iTaskGuidArray = obj.getString("itaskguidarray");
                // 申报方式
                String applytype = obj.getString("applytype");
                if (StringUtil.isNotBlank(iTaskGuidArray)) {
                    // 2、将字符串首尾的[]去除，然后组合成数组
                    iTaskGuidArray = iTaskGuidArray.replace("[", "").replace("]", "");
                    String[] itaskGuids = iTaskGuidArray.split(","); // 材料标识数组
                    int guidsCount = itaskGuids.length;
                    for (int i = 0; i < guidsCount; i++) {
                        String itaskGuid = itaskGuids[i];
                        itaskGuid = itaskGuid.replaceAll("\"", "");
                        // 3、获取事项信息
                        AuditSpITask auditSpITask = iAuditSpITask.getAuditSpITaskDetail(itaskGuid).getResult();
                        if (auditSpITask != null && !ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus())
                                && (!ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                                        || (ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                                                && StringUtil.isBlank(auditSpITask.getProjectguid())))) {
                            auditSpITask.setStatus("1");
                            iAuditSpITask.updateAuditSpITask(auditSpITask);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "您提交的事项状态已经变更 ，请刷新后重试", "");
                        }
                    }
                }
                // 4、更新子申报状态为待预审
                AuditSpISubapp spISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                if (spISubapp != null) {
                    spISubapp.setStatus(ZwfwConstant.LHSP_Status_DYS);
                    spISubapp.set("applytype", applytype);// 申报方式 1：分段申报2：综合申报
                    if ("1".equals(applytype)) {
                        if ("1".equals(spISubapp.getStr("applynum1"))) {
                            // 说明是第二次申报
                            spISubapp.set("applynum2", "1");
                        }
                        else {
                            // 说明是第一次申报
                            spISubapp.set("applynum1", "1");
                        }
                    }
                    iAuditSpISubapp.updateAuditSpISubapp(spISubapp);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "子申报查询失败！", "");
                }
                // 向指定用户发送待办
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                String handleUrl = "/epointzwfw/auditsp/auditsphandle/handlebicheckmaterialdetail?biGuid="
                        + auditSpISubapp.getBiguid() + "&subappGuid=" + auditSpISubapp.getRowguid() + "&businesstype=1";
                String title = "【项目材料预审】" + auditSpISubapp.getSubappname() + "(" + auditSpISubapp.getApplyername()
                        + ")";
                // 查询辖区
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                        .getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid()).getResult();
                iAuditOnlineMessages.sendMsg("统一收件人员", title, auditSpISubapp.getApplyerguid(),
                        auditSpISubapp.getApplyername(), auditSpBusiness.getAreacode(), handleUrl,
                        auditSpISubapp.getRowguid(), "zwfwMsgurl", null);
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/newSubmitMaterial", method = RequestMethod.POST)
    public String newSubmitMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String subAppGuid = obj.getString("subappguid");
                // 1.2、 获取选中的事项
                String iTaskGuidArray = obj.getString("itaskguidarray");
                // 申报方式
                String applytype = obj.getString("applytype");
                StringBuffer spitaskguids = new StringBuffer();
                if (StringUtil.isNotBlank(iTaskGuidArray)) {
                    log.info("newSubmitMaterial 入参：" + subAppGuid + "," + iTaskGuidArray + "," + applytype);
                    // 2、将字符串首尾的[]去除，然后组合成数组
                    iTaskGuidArray = iTaskGuidArray.replace("[", "").replace("]", "");
                    String[] itaskGuids = iTaskGuidArray.split(","); // 材料标识数组
                    int guidsCount = itaskGuids.length;
                    for (int i = 0; i < guidsCount; i++) {
                        String itaskGuid = itaskGuids[i];
                        itaskGuid = itaskGuid.replaceAll("\"", "");
                        spitaskguids.append("'" + itaskGuid + "',");
                        // 3、获取事项信息
                        AuditSpITask auditSpITask = iAuditSpITask.getAuditSpITaskDetail(itaskGuid).getResult();
                        if (auditSpITask != null && !ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus())
                                && (!ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                                        || (ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                                                && StringUtil.isBlank(auditSpITask.getProjectguid())))) {
                            auditSpITask.setStatus("1");
                            iAuditSpITask.updateAuditSpITask(auditSpITask);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "您提交的事项状态已经变更 ，请刷新后重试", "");
                        }
                    }
                }
                // 4、更新子申报状态为待预审
                AuditSpISubapp spISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                if (spISubapp != null) {
                    spISubapp.setStatus(ZwfwConstant.LHSP_Status_DYS);
                    if(StringUtils.isNotBlank(applytype)){
                        spISubapp.set("applytype", applytype);// 申报方式 1：分段申报2：综合申报
                        if ("1".equals(applytype)) {
                            if ("1".equals(spISubapp.getStr("applynum1"))) {
                                // 说明是第二次申报
                                spISubapp.set("applynum2", "1");
                            }
                            else {
                                // 说明是第一次申报
                                spISubapp.set("applynum1", "1");
                            }
                        }
                    }
                    // 如果上一步选择的事项都生成办件就设置状态为30（办理中），反之设置为20（上传材料）
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("SUBAPPGUID", subAppGuid);
                    conditionUtil.isBlankOrValue("STATUS", "0");
                    List<AuditSpITask> auditSpITasks = iAuditSpITask
                            .getAuditSpITaskListByCondition(conditionUtil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(auditSpITasks)) {
                        spISubapp.setStatus("20");
                    }
                    else {
                        spISubapp.setStatus("30");
                    }
                    iAuditSpISubapp.updateAuditSpISubapp(spISubapp);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "子申报查询失败！", "");
                }
                // 材料提交之后方法办件
                log.info("spitaskguids:" + spitaskguids);
                if (spitaskguids.toString().length() > 0) {
                    save("flase", subAppGuid, spitaskguids.substring(0, spitaskguids.length() - 1));
                }
                // 向指定用户发送待办
                // AuditSpISubapp auditSpISubapp =
                // iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                // String handleUrl =
                // "/epointzwfw/auditsp/auditsphandle/handlebicheckmaterialdetail?biGuid="
                // + auditSpISubapp.getBiguid() + "&subappGuid=" +
                // auditSpISubapp.getRowguid() + "&businesstype=1";
                // String title = "【项目材料预审】" + auditSpISubapp.getSubappname() +
                // "(" + auditSpISubapp.getApplyername()
                // + ")";
                // // 查询辖区
                // AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                // .getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid()).getResult();
                // iAuditOnlineMessages.sendMsg("统一收件人员", title,
                // auditSpISubapp.getApplyerguid(),
                // auditSpISubapp.getApplyername(),
                // auditSpBusiness.getAreacode(), handleUrl,
                // auditSpISubapp.getRowguid(), "zwfwMsgurl", null);
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 这边实现办件的分发 [一句话功能简述]
     * 
     * @param isck
     */
    public void save(String isck, String subappGuid, String spitaskGuids) {
        // List<AuditSpITask> listTask =
        // iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        log.info(
                "String isck, String subappGuid, String spitaskGuids: " + isck + "," + subappGuid + "," + spitaskGuids);
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("SUBAPPGUID", subappGuid);
        conditionUtil.eq("STATUS", "1");
        conditionUtil.in("rowguid", spitaskGuids);
        List<AuditSpITask> listTask = iAuditSpITask.getAuditSpITaskListByCondition(conditionUtil.getMap()).getResult();
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();

        // 如果状态为已接件，则说明处理完成了
        // if (ZwfwConstant.LHSP_Status_YSJ.equals(auditSpISubapp.getStatus()))
        // {
        // addCallbackParam("msg", "该流程已处理完成！");
        // return;
        // }

        // 判断是否第一次发件
        boolean firstsj = true;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isNotBlank(auditspitask.getProjectguid())) {
                firstsj = false;
            }
        }
        List<String> taskguids = new ArrayList<>();
        // 统一收件
        // if (!ZwfwConstant.LHSP_Status_DYS.equals(auditSpISubapp.getStatus()))
        // {
        // if (getDataGridTask().getSelectKeys() != null) {
        // taskguids.addAll(getDataGridTask().getSelectKeys());
        // }
        // if (fgetDataGridTask().getSelectKeys() != null) {
        // taskguids.addAll(fgetDataGridTask().getSelectKeys());
        // }
        // listTask = listTask.stream().filter(spitask ->
        // (taskguids.contains(spitask.getRowguid()))
        // &&
        // StringUtil.isBlank(spitask.getProjectguid())).collect(Collectors.toList());
        // }
        // else {
        // // 预审逻辑
        // listTask = listTask.stream().filter(
        // a -> ZwfwConstant.CONSTANT_STR_ONE.equals(a.getStatus()) &&
        // StringUtil.isBlank(a.getProjectguid()))
        // .collect(Collectors.toList());
        // }

        /* 3.0新增逻辑验证 */
        // 检查事项是否有推荐关联单位类型,如果有是不是关联了相关类型的单位
        /*
         * boolean point = false; for (AuditSpITask task : listTask) { // 查询此事项有没有配置单位类型
         * String auditbasetaskguid = taskCommonService .getCropInfo(task.getTaskguid(),
         * subappGuid, task.getPhaseguid()).getResult(); if
         * (StringUtil.isNotBlank(auditbasetaskguid)) { // 查询此基础事项偶没有配置单位类型 String
         * cropType = taskCommonService.getCropName(auditbasetaskguid).getResult(); if
         * (StringUtil.isNotBlank(cropType)) { // 查询此单位类型有没有关联单位 Integer count =
         * iAuditSpITaskCorpService.countAuditSpITaskCorps(task.getTaskguid(),
         * subappGuid); if (count == 0) { point = true; break; } } } }
         */
        /* 3.0新增逻辑结束 */

        String csaeguid = auditSpISubapp.getCaseguid();

        AuditTaskCase auditTaskCase = iAuditTaskCase.getAuditTaskCaseByRowguid(csaeguid).getResult();
        String selectoptions = "";
        if (auditTaskCase != null) {
            selectoptions = auditTaskCase.getSelectedoptions();
        }
        List<String> materiallist = new ArrayList<>();
        if (StringUtil.isNotBlank(selectoptions)) {
            JSONObject jsons = JSONObject.parseObject(selectoptions);
            List<String> optionlist = (List<String>) jsons.get("optionlist");
            Map<String, AuditSpOption> optionmap = new HashMap<>();
            if (ValidateUtil.isNotBlankCollection(optionlist)) {
                for (String guid : optionlist) {
                    AuditSpOption auditSpOption = iAuditSpOptionService.find(guid).getResult();
                    optionmap.put(guid, auditSpOption);
                }
                optionmap = iAuditSpOptionService.getAllOptionByOptionMap(optionmap).getResult();
                for (AuditSpOption auditSpOption : optionmap.values()) {
                    if (StringUtil.isBlank(auditSpOption.getMaterialids())) {
                        continue;
                    }
                    String materials = auditSpOption.getMaterialids();
                    if (StringUtil.isNotBlank(auditSpOption.getMaterialids())) {
                        String[] mater = materials.split(";");
                        for (int i = 0; i < mater.length; i++) {
                            if (StringUtil.isNotBlank(mater[i])) {
                                materiallist.add(mater[i]);
                            }
                        }
                    }
                }
            }
        }
        AuditRsItemBaseinfo result = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid())
                .getResult();
        String centerguid1 = ZwfwUserSession.getInstance().getCenterGuid();
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
        List<DantiInfo> alldantiInfo = iDantiInfoService.findListBySubAppguid(subappGuid);
        log.info("listTask.size()"+listTask.size());

        // 判断申报事项是否为供水（370800ZSSW000000000000000000002、3708000000000000000000000000102）
        // 供电（913708001659213936337209900400201、913708001659213936337209900400102）
        // taskGuid查询事项表，根据item_id判断
        log.info("判断当前事项是否是供水、供电或燃气或供热的事项");
        // String pushTaskItem =
        // configservice.getFrameConfigValue("pushTaskItem");
        // log.info("获取系统参数配置的水电气暖事项itemid--->" + pushTaskItem);
        // 供电推送
        String gdPushTaskItem = iConfigService.getFrameConfigValue("gdPushTaskItem");
        // 供水推送
        String gsPushTaskItem = iConfigService.getFrameConfigValue("gsPushTaskItem");
        // 燃气推送
        String rqPushTaskItem = iConfigService.getFrameConfigValue("rqPushTaskItem");
        // 供热推送
        String grPushTaskItem = iConfigService.getFrameConfigValue("grPushTaskItem");
        log.info("gdPushTaskItem--->" + gdPushTaskItem);
        log.info("gsPushTaskItem--->" + gsPushTaskItem);
        log.info("rqPushTaskItem--->" + rqPushTaskItem);
        log.info("grPushTaskItem--->" + grPushTaskItem);
        List<String> pushTaskItemIdList = new ArrayList<>();
        List<String> gdList = new ArrayList<>();
        List<String> gsList = new ArrayList<>();
        List<String> rqList = new ArrayList<>();
        List<String> grList = new ArrayList<>();
        if (StringUtil.isNotBlank(gdPushTaskItem)) {
            gdList = Arrays.stream(gdPushTaskItem.split(",")).collect(Collectors.toList());
            pushTaskItemIdList.addAll(gdList);
        }
        if (StringUtil.isNotBlank(gsPushTaskItem)) {
            gsList = Arrays.stream(gsPushTaskItem.split(",")).collect(Collectors.toList());
            pushTaskItemIdList.addAll(gsList);
        }
        if (StringUtil.isNotBlank(rqPushTaskItem)) {
            rqList = Arrays.stream(rqPushTaskItem.split(",")).collect(Collectors.toList());
            pushTaskItemIdList.addAll(rqList);
        }
        if (StringUtil.isNotBlank(grPushTaskItem)) {
            grList = Arrays.stream(grPushTaskItem.split(",")).collect(Collectors.toList());
            pushTaskItemIdList.addAll(grList);
        }
        log.info("水电气暖热全部推送itemId--->" + pushTaskItemIdList);

//        ExecutorService rqexecutorService = Executors.newFixedThreadPool(10);
        for (AuditSpITask task : listTask) {
            // 先生成办件，再初始化材料
            String projectGuid = handleProjectService.InitBLSPProject(task.getTaskguid(), "", "", "",
                    result.getItemlegalcerttype(), result.getItemlegalcertnum(), "", "", "", auditSpISubapp.getBiguid(),
                    subappGuid, auditSpISubapp.getBusinessguid(), StringUtils.isNotBlank(result.getItemlegalcreditcode())?result.getItemlegalcreditcode():result.getItemlegalcertnum(),
                    result.getItemlegaldept()).getResult();
            log.info("projectGuid"+projectGuid);
            iAuditSpITask.updateProjectGuid(task.getRowguid(), projectGuid);
            if (auditSpSpSgxk != null && "建筑工程施工许可证核发".equals(task.getTaskname())) {
                AuditProjectFormSgxkz sgxkz = new AuditProjectFormSgxkz();
                sgxkz.setRowguid(UUID.randomUUID().toString());
                sgxkz.setProjectguid(projectGuid);
                sgxkz.setXiangmudaima(auditSpSpSgxk.getItemcode());
                sgxkz.setXiangmubianhao(auditSpSpSgxk.getXmbm());
                sgxkz.setXiangmufenlei(auditSpSpSgxk.getStr("xmfl"));
                sgxkz.setXiangmumingchen(auditSpSpSgxk.getItemname());
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("gctzxz"))) {
                    // sgxkz.setInvPropertyNum(auditSpSpSgxk.getStr("gctzxz"));
                }
                // sgxkz.setBargainDays(auditSpSpSgxk.getStr("htgq"));
                sgxkz.setXiangmusuozaishengfen(auditSpSpSgxk.getStr("xmszsf"));
                sgxkz.setXiangmusuozaichengshi(auditSpSpSgxk.getStr("xmszcs"));
                sgxkz.setXiangmusuozaiquxian(auditSpSpSgxk.getStr("xmszqx"));
                sgxkz.setXiangmudidian(auditSpSpSgxk.getItemaddress());
                sgxkz.setLiXiangPiFuJiGuan(auditSpSpSgxk.getStr("lxpfjg"));
                sgxkz.setLixiangpifushijian(auditSpSpSgxk.getDate("lxpfsj"));
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("zbtzsbh"))) {
                    sgxkz.setZhongBiaoTongZhiShuBianHao(auditSpSpSgxk.getStr("zbtzsbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("sgtsxmbh"))) {
                    sgxkz.setShiGongTuShenChaXiangMuBianHao(auditSpSpSgxk.getStr("sgtsxmbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsydghxkzbh"))) {
                    sgxkz.set("JianSheYongDiGuiHuaXuKeZhen", auditSpSpSgxk.getStr("jsydghxkzbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsgcghxkzbh"))) {
                    sgxkz.setJianSheGongChengGuiHuaXuKeZhen(auditSpSpSgxk.getStr("jsgcghxkzbh"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getMoneysources())) {
                    sgxkz.setZijinlaiyuan(auditSpSpSgxk.getMoneysources());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getAllmoney())) {
                    sgxkz.setXiangmuzongtouziwanyuan(auditSpSpSgxk.getAllmoney() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getAllbuildarea())) {
                    sgxkz.setXiangmuzongjianzhumianjipingfa(auditSpSpSgxk.getAllbuildarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getOverloadarea())) {
                    sgxkz.setXiangmudishangjianzhumianjipin(auditSpSpSgxk.getOverloadarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getDownloadarea())) {
                    sgxkz.setXiangmudixiajianzhumianjipingf(auditSpSpSgxk.getDownloadarea() + "");
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("zcd"))) {
                    sgxkz.setZongchangdumi1(auditSpSpSgxk.getStr("zcd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("dscd"))) {
                    sgxkz.setXiangmudishangchangdumi(auditSpSpSgxk.getStr("dscd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("dxcd"))) {
                    sgxkz.setXiangmudixiachangdumi(auditSpSpSgxk.getStr("dxcd"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("kuadu"))) {
                    sgxkz.setXiangmukuadumi(auditSpSpSgxk.getStr("kuadu"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getGcyt())) {
                    sgxkz.setGongChengYongTu(auditSpSpSgxk.getGcyt());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("jsgm"))) {
                    sgxkz.setJiansheguimo(auditSpSpSgxk.getStr("jsgm"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getBuildproperties())) {
                    sgxkz.setJianshexingzhi(auditSpSpSgxk.getBuildproperties());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getProjectlevel())) {
                    sgxkz.setZhongdianxiangmu(auditSpSpSgxk.getProjectlevel());
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("htjg"))) {
                    sgxkz.setHetongjiagewanyuan(auditSpSpSgxk.getStr("htjg"));
                }
                if (StringUtil.isNotBlank(auditSpSpSgxk.getStr("htkgrq"))) {
                    sgxkz.setHetongkaigongriqi(auditSpSpSgxk.getStr("htkgrq"));
                }

                sgxkz.setHetongjungongriqi(auditSpSpSgxk.getStr("htjgrq"));
                sgxkz.setShentudanwei(auditSpSpSgxk.getStr("stdw"));
                sgxkz.setShenTuDanWeiCode(auditSpSpSgxk.getStr("stdwtyshxydm"));
                sgxkz.setShentudanweixiangmufuzeren(auditSpSpSgxk.getStr("stxmfzr"));
                String itemguid = "";
                if (result != null) {
                    // 如果是子项目则获取主项目guid
                    if (StringUtil.isNotBlank(result.getParentid())) {
                        itemguid = result.getParentid();
                    }
                }

                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("corptype", "31");
                sql.eq("itemguid", itemguid);
                List<ParticipantsInfo> jsinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                ParticipantsInfo jsinfo = null;
                if (jsinfos != null && jsinfos.size() > 0) {
                    jsinfo = jsinfos.get(0);
                    sgxkz.setJianshedanweimingchen(jsinfo.getCorpname());
                    sgxkz.setJianSheDanWeiTongYiSheHuiXinYo(jsinfo.getCorpcode());
                    sgxkz.setJianshedanweidizhi(jsinfo.getAddress());
                    sgxkz.setJianshedanweidianhua(jsinfo.getPhone());
                    sgxkz.setJianshedanweifadingdaibiaoren(jsinfo.getLegal());
                    sgxkz.setJianshedanweixiangmufuzeren(jsinfo.getXmfzr());
                }

                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("corptype", "2");
                sql1.eq("subappguid", subappGuid);
                List<ParticipantsInfo> sjinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql1.getMap());
                ParticipantsInfo sjinfo = null;
                if (sjinfos != null && sjinfos.size() > 0) {
                    sjinfo = sjinfos.get(0);
                    sgxkz.setShejidanwei(sjinfo.getCorpname());
                    sgxkz.setShejidanweixiangmufuzeren(sjinfo.getXmfzr());
                    sgxkz.setSheJiDanWeiCode(sjinfo.getCorpcode());
                }

                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("corptype", "1");
                sql2.eq("subappguid", subappGuid);
                List<ParticipantsInfo> kcinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql2.getMap());
                ParticipantsInfo kcinfo = null;
                if (kcinfos != null && kcinfos.size() > 0) {
                    kcinfo = kcinfos.get(0);
                    sgxkz.setKanchadanwei(kcinfo.getCorpname());
                    sgxkz.setKanchadanweixiangmufuzeren(kcinfo.getXmfzr());
                    sgxkz.setKanChaDanWeiCode(kcinfo.getCorpcode());
                }

                SqlConditionUtil sql3 = new SqlConditionUtil();
                sql3.eq("corptype", "3");
                sql3.eq("subappguid", subappGuid);
                List<ParticipantsInfo> sginfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql3.getMap());
                ParticipantsInfo sginfo = null;
                if (sginfos != null && sginfos.size() > 0) {
                    sginfo = sginfos.get(0);
                    sgxkz.setShigongzongchengbaoqiye(sginfo.getCorpname());
                    sgxkz.setShiGongZongChengBaoQiYeTongYiS(sginfo.getCorpcode());
                    sgxkz.setShigongzongchengbaoqiyexiangmu(sginfo.getXmfzperson());
                }

                SqlConditionUtil sql4 = new SqlConditionUtil();
                sql4.eq("corptype", "4");
                sql4.eq("subappguid", subappGuid);
                List<ParticipantsInfo> jlinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql4.getMap());
                ParticipantsInfo jlinfo = null;
                if (jlinfos != null && jlinfos.size() > 0) {
                    jlinfo = jlinfos.get(0);
                    sgxkz.setJianlidanwei(jlinfo.getCorpname());
                    sgxkz.setJianLiDanWeiCode(jlinfo.getCorpcode());
                    sgxkz.setZongjianligongchengshi(jlinfo.getXmfzr());
                }

                SqlConditionUtil sql5 = new SqlConditionUtil();
                sql5.eq("corptype", "11");
                sql5.eq("subappguid", subappGuid);
                List<ParticipantsInfo> jcinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql5.getMap());
                ParticipantsInfo jcinfo = null;
                if (jcinfos != null && jcinfos.size() > 0) {
                    jcinfo = jcinfos.get(0);
                    sgxkz.setJiancedanwei(jcinfo.getCorpname());
                    sgxkz.setJianCeDanWeiCode(jcinfo.getCorpcode());
                    sgxkz.setJianCeDanWeiFuZeRen(jcinfo.getXmfzr());
                }

                iAuditProjectFormSgxkzService.insert(sgxkz);
                log.info("插入施工许可证信息成功：" + sgxkz);

                if (alldantiInfo != null && alldantiInfo.size() > 0) {
                    for (DantiInfo dantiInfo : alldantiInfo) {
                        AuditProjectFormSgxkzDanti danti = new AuditProjectFormSgxkzDanti();
                        danti.setRowguid(UUID.randomUUID().toString());
                        danti.setProjectguid(projectGuid);
                        danti.setDantijiangouzhuwumingchen(dantiInfo.getDantiname());
                        danti.setGongchengzaojiawanyuan(dantiInfo.getPrice() + "");
                        danti.setJianzhumianjipingfangmi(dantiInfo.getZjzmj() + "");
                        danti.setDixiacengshu(dantiInfo.getDxcs() + "");
                        danti.setDishangcengshu(dantiInfo.getDscs() + "");
                        // danti.setSeismicIntensityScale(dantiInfo.getKzlevel()
                        // + "");
                        // danti.setISSuperHightBuilding(dantiInfo.getIslowenergy());
                        iAuditProjectFormSgxkzService.insert(danti);
                        log.info("插入施工许可证单体信息成功：" + danti);
                    }
                }
            }
            // 保存办件表单
            if (auditSpISubapp != null) {
                log.info("auditSpISubapp.getYewuguid():" + auditSpISubapp.getYewuguid());
                if (StringUtil.isNotBlank(auditSpISubapp.getYewuguid())) {
                    AuditProject project = new AuditProject();
                    project.setRowguid(projectGuid);
                    project.setProjectname(task.getTaskname() + "(" + result.getItemname() + ")");
                    project.setCertnum(result.getItemlegalcertnum());
                    project.setApplyername(result.getItemlegaldept());
                    project.setAddress(result.getConstructionsite());
                    project.setContactperson(result.getContractperson());
                    project.setContactcertnum(result.getContractidcart());
                    project.setContactphone(result.getContractphone());
                    project.setContactemail(result.getFremail());
                    project.setXiangmubh(result.getRowguid());
                    project.setTaskcaseguid(csaeguid);
                    project.setRemark(result.getConstructionscaleanddesc());
                    // 这边改成20保证受理及审核上报，不然不走mq
                    project.setApplyway(20);
                    // 初始化办件状态是待接件
                    project.setStatus(24);
                    /*
                     * String flowsn=getFlowsn(task.getTaskguid()); Random random = new Random();
                     * if(StringUtil.isBlank(flowsn)){ flowsn= "080101"+ random.nextInt(1000 - 100)
                     * + 100 + 1; } project.setFlowsn(flowsn);
                     */

                    auditProjectService.updateProject(project);
                }
            }

            sb.append(projectGuid).append("','");
            // 再进行材料初始化
            handleSPIMaterialService.initProjctMaterial(materiallist, auditSpISubapp.getBusinessguid(), subappGuid,
                    task.getTaskguid(), projectGuid);

            // if (StringUtil.isNotBlank(pushTaskItem)) {
            if (ValidateUtil.isNotBlankCollection(pushTaskItemIdList)) {
                // List<String> pushTaskItemIdList =
                // Arrays.stream(pushTaskItem.split(",")).collect(Collectors.toList());
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(task.getTaskguid(), false).getResult();
                if (auditTask != null && !auditTask.isEmpty()) {
                    String itemId = auditTask.getItem_id();
                    log.info("获取事项itemId--->" + itemId);
                    try{
                        if (pushTaskItemIdList.contains(itemId)) {
                            log.info("该事项属于推送事项");
                            // 属于推送事项，需要调用第三方流程提交检查接口和发起流程接口
                            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, null)
                                    .getResult();
                            log.info("推送事项的办件主键--->" + auditProject.getRowguid());
                            if (auditProject != null && !auditProject.isEmpty()) {
                                // if
                                // ("913708001659213936337209900400201".equals(itemId)
                                // ||
                                // "913708001659213936337209900400102".equals(itemId))
                                // {
                                if (gdList.contains(itemId)) {
                                    // 先获取电子表单数据
                                    jsbdysq(subappGuid, auditProject);
                                    // 如果是供电事项，获取申报时传递的预申请编码
                                    log.info("供电事项，获取申报时传递的预申请编码");
                                    log.info("更新了subApp的preAppNo字段，重新查询一下subApp实体");
                                    AuditSpISubapp tempSubApp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
                                    log.info("查询出的subApp实体--->" + tempSubApp);
                                    String preAppNo = tempSubApp.getStr("preAppNo");
                                    AuditSpProjectinfoGdbz bySubAppGuid = auditSpProjectinfoGdbzService
                                            .getBySubAppGuid(subappGuid);
                                    if (StringUtil.isBlank(preAppNo)) {
                                        log.info("从subApp中没有查询出preAppNo，改为从供电报装办件中查询preAppNo");
                                        // bySubAppGuid =
                                        // auditSpProjectinfoGdbzService.getBySubAppGuid(subappGuid);
                                        log.info("供电报装办件实体--->" + bySubAppGuid);
                                        if(bySubAppGuid!=null){
                                            preAppNo = bySubAppGuid.getStr("preAppNo");
                                        }
                                    }
                                    log.info("preAppNo--->" + preAppNo);
                                    // 根据该编码查找对应的供电报装办件
                                    // SqlConditionUtil sql = new
                                    // SqlConditionUtil();
                                    // sql.eq("preAppNo", preAppNo);
                                    // log.info("根据preAppNo查询供电报装办件");
                                    // AuditSpProjectinfoGdbz auditspprojectinfogdbz
                                    // = new SQLManageUtil()
                                    // .getBeanByCondition(AuditSpProjectinfoGdbz.class,
                                    // sql.getMap());
                                    if (bySubAppGuid != null && !bySubAppGuid.isEmpty()) {
                                        log.info("查询到供电报装办件实体，将对应的projectGuid设置到供电报装办件的projectGuid字段");
                                        bySubAppGuid.setProjectGuid(projectGuid);
                                        auditSpProjectinfoGdbzService.update(bySubAppGuid);
                                        log.info("更新完成，对应的projectGuid--->" + projectGuid);
                                        try {
                                            // 推送办件信息表到住建
                                            projectSendzj("false", "'" + auditProject.getRowguid() + "'", subappGuid);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                // else
                                // if("91370800798683639M3372099rq1rq3".equals(itemId)
                                // ||
                                // "91370800798683639M3372099rq1rq2".equals(itemId))
                                // {
                                else if (rqList.contains(itemId)) {
                                    // 燃气事项
                                    log.info("燃气事项");
                                    log.info("注册身份用户信息");
                                    String rqsfzyhzc = rqsfzyhzc(auditProject);
                                    log.info("调用注册身份用户信息结果--->" + rqsfzyhzc);
                                    if (StringUtil.isNotBlank(rqsfzyhzc)) {
                                        log.info("调用注册身份用户信成功，调用燃气报装提交接口");
                                        String rqbztj = rqbztj(subappGuid, projectGuid);
                                        if (StringUtil.isNotBlank(rqbztj)) {
                                            log.info("报装成功");
                                            log.info("生成接件的办件操作数据");
                                            AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                            auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                            auditProjectOperation.setProjectGuid(projectGuid);
                                            auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                            auditProjectOperation.setOperatedate(new Date());
                                            auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                            auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                            auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                            auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                            auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                            auditProjectOperation.setOperateusername("业务办理人员");
                                            auditProjectOperationService.addProjectOperation(auditProjectOperation);
                                            log.info("生成完成");
                                            // 生成完之后，直接发送mq生成受理步骤的数据
                                            log.info("生成受理数据");
                                            String userguid = iConfigService.getFrameConfigValue("yxdj_zxglyguid");
                                            String windowguid = iConfigService.getFrameConfigValue("yxdj_windowguid");

                                            String hrzdmsg = auditProject.getRowguid() + "."+userguid+ "."+windowguid+ "." + auditProject.getAreacode();

                                            sendMQMessageService.sendByExchange("zwdt_exchange_handle", hrzdmsg, "project."
                                                    + auditProject.getAreacode() + ".jnhrrqaccept." + auditProject.getTask_id());

                                            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                                            log.info("发送mq");
                                            if (auditProject.getIs_test() != Integer
                                                    .parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                                log.info("非测试件");
                                                // TODO可能要调用综管的mq
                                                try {

                                                    // 推送办件信息表到住建
                                                    projectSendzj("false", "'" + auditProject.getRowguid() + "'",
                                                            subappGuid);

                                                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                                            + "370800" + "." + "accept" + "." + auditProject.getTask_id());

                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        else {
                                            log.info("报装失败，不做任何操作");
                                        }
                                    }
                                    else {
                                        log.info("调用注册身份用户信失败，不做任何操作");
                                    }
                                }
                                else if (grList.contains(itemId)) {
                                    log.info("供热事项");
                                    // 供热事项
                                    String projectFlowSn = auditProject.getFlowsn();
                                    log.info("获取办件流水号--->" + projectFlowSn);
                                    // 是，调用山东公用线上接口文档
                                    log.info("==获取电子表单数据==");
                                    String grtablename = ConfigUtil.getConfigValue("duijie", "grtablename");
                                    log.info("==供热电子表单表名==:" + grtablename);
                                    // 1。获取电子表单数据
                                    Record dataObj = GongYongShuiWuUtil.grdata(auditProject.getSubappguid(), auditProject,
                                            grtablename);
                                    // 2。调用山东公用线上接口
                                    log.info("==调用山东公用线上接口==");
                                    String platformid = GongYongShuiWuUtil.submit(dataObj, auditProject);
                                    // 3.数据上报
                                    if (StringUtil.isNotBlank(platformid)) {
                                        log.info("已请求山东公用线上接口，单据编号为：" + platformid);
                                        try {
                                            // 推送成功后将办件状态更新成 26
                                            // 已接件并且生成audit_project_operate 接件数据
                                            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                            auditProjectService.updateProject(auditProject);
                                            log.info("生成接件的办件操作数据");
                                            AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                            auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                            auditProjectOperation.setProjectGuid(projectGuid);
                                            auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                            auditProjectOperation.setOperatedate(new Date());
                                            auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                            auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                            auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                            auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                            auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                            auditProjectOperation.setOperateusername("业务办理人员");
                                            auditProjectOperationService.addProjectOperation(auditProjectOperation);
                                            log.info("生成完成");
                                            String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                                            // 推送办件信息表到住建
                                            log.info("发送mq");
                                            if (auditProject.getIs_test() != Integer
                                                    .parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                                log.info("非测试件");
                                                // 推送办件信息表到住建
                                                projectSendzj("false", "'" + auditProject.getRowguid() + "'", subappGuid);
                                                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                                        + "370800" + "." + "jj" + "." + auditProject.getTask_id());
                                            }
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        log.info("调用山东公用线上接口异常，未获取到单据编号");
                                    }
                                }
                                else {
                                    // 供水事项
                                    String projectFlowSn = auditProject.getFlowsn();
                                    log.info("获取办件流水号--->" + projectFlowSn);
                                    JSONObject checkResult;
                                    int i = 0;
                                    // 判断当前办理的事项是不是“济宁市以及任城、太白湖、高新、经开区”
                                    if (isJiningArea(auditTask.getAreacode())) {
                                        log.info("==当前办理事项是济宁市或者任城、太白湖、高新、经开区==");
                                        // 是，调用山东公用线上接口文档
                                        log.info("==获取电子表单数据==");
                                        String gstablename = ConfigUtil.getConfigValue("duijie", "gstablename");
                                        log.info("==供水电子表单表名==:" + gstablename);
                                        // 1。获取电子表单数据
                                        Record dataObj = GongYongShuiWuUtil.ysdata(auditProject.getSubappguid(),
                                                auditProject, gstablename);
                                        // 2。调用山东公用线上接口
                                        log.info("==调用山东公用线上接口==");
                                        String platformid = GongYongShuiWuUtil.submit(dataObj, auditProject);
                                        // 3.数据上报
                                        if (StringUtil.isNotBlank(platformid)) {
                                            log.info("已请求山东公用线上接口，单据编号为：" + platformid);
                                            try {
                                                // 推送成功后将办件状态更新成 26
                                                // 已接件并且生成audit_project_operate 接件数据
                                                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                                auditProjectService.updateProject(auditProject);
                                                log.info("生成接件的办件操作数据");
                                                AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                                auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                                auditProjectOperation.setProjectGuid(projectGuid);
                                                auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                                auditProjectOperation
                                                        .setOperateUserGuid(UserSession.getInstance().getUserGuid());
                                                auditProjectOperation.setOperatedate(new Date());
                                                auditProjectOperation
                                                        .setOperateusername(UserSession.getInstance().getDisplayName());
                                                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                                auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                                auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                                auditProjectOperation.setOperateusername("业务办理人员");
                                                auditProjectOperationService.addProjectOperation(auditProjectOperation);
                                                log.info("生成完成");
                                                String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                                                // 推送办件信息表到住建
                                                log.info("发送mq");
                                                if (auditProject.getIs_test() != Integer
                                                        .parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                                    log.info("非测试件");
                                                    // 推送办件信息表到住建
                                                    projectSendzj("false", "'" + auditProject.getRowguid() + "'",
                                                            subappGuid);
                                                    sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                                                            + "370800" + "." + "jj" + "." + auditProject.getTask_id());
                                                }
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            log.info("调用山东公用线上接口异常，未获取到单据编号");
                                        }
                                    }
                                    // 不是
                                    else {
                                        while (true) {
                                            // TODO 这里的户号 不知道是不是 auditProject.getCertnum()
                                            checkResult = ZhongShanShuiWuUtil.checkSubmitFlow(projectFlowSn,
                                                    auditProject.getCertnum());
                                            if (checkResult.getBoolean("isSuccess")) {
                                                // 如果请求成功，跳出循环
                                                break;
                                            }
                                            else if (!checkResult.getBoolean("isSuccess")
                                                    && "0".equals(checkResult.getString("code"))) {
                                                // 请求失败同时code为0表示流水号有问题，重新生成流水号
                                               /* IHandleFlowSn handleFlowSn = ContainerFactory.getContainInfo()
                                                        .getComponent(IHandleFlowSn.class);
                                                String numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
                                                        auditProject.getCenterguid()).getResult();
                                                if (StringUtil.isBlank(numberFlag)) {
                                                    numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
                                                }
                                                projectFlowSn = handleFlowSn.getFlowsn("办件编号", numberFlag).getResult();
                                                log.info("重新生成的流水号--->" + projectFlowSn);*/
                                                String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());
                                                auditProject.set("flowsn", flowsn);
                                                auditProjectService.updateProject(auditProject);
                                                log.info("更新办件流水号--->" + auditProject.getFlowsn());
                                            }
                                            else {
                                                // 请求失败
                                                log.info("请求失败，" + checkResult.getString("msg"));
                                                break;
                                            }
                                            i++;
                                            if (i == 100) {
                                                log.info("请求达到100次，跳出循环");
                                                break;
                                            }
                                        }
                                        // 走到这里，说明处理完毕，跳出循环的情况只需判断isSuccess是true还是false即可
                                        if (!checkResult.getBoolean("isSuccess")) {
                                            // 请求失败
                                            log.info("跳出循环，请求失败，" + checkResult.getString("msg"));
                                        }
                                        else {
                                            // 继续请求发起流程接口
                                            // String billNo =
                                            // startProcess(auditProject);
                                            String gstablename = ConfigUtil.getConfigValue("duijie", "gstablename");
                                            log.info("==供水电子表单表名==:" + gstablename);
                                            String itemcode = "";
                                            if (auditSpISubapp != null) {
                                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                                        .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                                itemcode = auditRsItemBaseinfo.getItemcode();
                                            }
                                            String billNo = ZhongShanShuiWuUtil.submit(auditProject,gstablename,itemcode);
                                            if (StringUtil.isNotBlank(billNo)) {
                                                log.info("已请求发起流程接口，单据编号为：" + billNo);
                                                try {
                                                    // 推送成功后将办件状态更新成 26
                                                    // 已接件并且生成audit_project_operate
                                                    // 接件数据
                                                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                                    auditProjectService.updateProject(auditProject);
                                                    log.info("生成接件的办件操作数据");
                                                    AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                                    auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                                    auditProjectOperation.setProjectGuid(projectGuid);
                                                    auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                                    auditProjectOperation.setOperatedate(new Date());
                                                    auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                                    auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                                    auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                                    auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                                    auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                                    auditProjectOperation.setOperateusername("业务办理人员");
                                                    auditProjectOperationService.addProjectOperation(auditProjectOperation);
                                                    log.info("生成完成");
                                                    String msg = auditProject.getRowguid() + "."
                                                            + auditProject.getAreacode();
                                                    // 推送办件信息表到住建
                                                    log.info("发送mq");
                                                    if (auditProject.getIs_test() != Integer
                                                            .parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                                                        log.info("非测试件");
                                                        // 推送办件信息表到住建
                                                        projectSendzj("false", "'" + auditProject.getRowguid() + "'",
                                                                subappGuid);
                                                        sendMQMessageService.sendByExchange("exchange_handle", msg,
                                                                "project." + "370800" + "." + "jj" + "."
                                                                        + auditProject.getTask_id());
                                                    }
                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                log.info("请求发起流程接口异常，未获取到单据编号");
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                log.info("未查询到办件，projectGuid：" + projectGuid);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else {
                    log.info("未查询到事项");
                }
            }
        }

        // 关闭线程池，注意要在所有任务都提交完成后进行关闭操作
//        rqexecutorService.shutdown();
//        while (!rqexecutorService.isTerminated()) {
//            // 等待线程池中的所有任务完成
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
        sb.append("'");
        // 办件初始化完成推送办件到住建系统
        // projectSendzj(isck, sb.toString());
        // listTask =
        // auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        // boolean flag = false;
        // for (AuditSpITask auditspitask : listTask) {
        // if (StringUtil.isBlank(auditspitask.getProjectguid())) {
        // flag = true;
        // }
        // }
        // 第二次分发办件不进行下面操作，内网状态
        // if (LHSP_Status_SJZ.equals(auditSpISubapp.getStatus())) {
        // addCallbackParam("msg", "收件完成！");
        // // 判断是否全部办理
        // if (!flag) {
        // auditSpISubappService.updateSubapp(subappGuid,
        // ZwfwConstant.LHSP_Status_YSJ, null);
        // afterYsj(isck);
        // }
        // return;
        // }

        // 第一次申报，如果全部办理，修改为已收件
        // if (flag) {
        // auditSpISubappService.updateSubapp(subappGuid, LHSP_Status_SJZ,
        // null);
        // addCallbackParam("msg", "收件完成！");
        // }
        // else {
        // auditSpISubappService.updateSubapp(subappGuid,
        // ZwfwConstant.LHSP_Status_YSJ, null);
        // afterYsj(isck);
        // }

        // 如果过第一次申报，插入计时数据记录
        // if (firstsj) {
        // String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        // if (StringUtil.isBlank(centerguid)) {
        // // 如果session里面没有centerguid，则获取辖区下第一个中心标识
        // SqlConditionUtil sqlc = new SqlConditionUtil();
        // sqlc.eq("belongxiaqu", ZwfwUserSession.getInstance().getAreaCode());
        // List<AuditOrgaServiceCenter> listcenter = iauditorgaservicecenter
        // .getAuditOrgaServiceCenterByCondition(sqlc.getMap()).getResult();
        // if (listcenter.size() > 0) {
        // centerguid = listcenter.get(0).getRowguid();
        // }
        // }
        // sparetimeService.addSpareTimeByProjectGuid(subappGuid,
        // ZwfwConstant.CONSTANT_INT_ZERO,
        // ZwfwUserSession.getInstance().getAreaCode(), centerguid);
        // // 插入后暂停计时
        // AuditProjectSparetime auditProjectSparetime =
        // sparetimeService.getSparetimeByProjectGuid(subappGuid)
        // .getResult();
        // auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
        // sparetimeService.updateSpareTime(auditProjectSparetime);
        // }
        // else {
        // return;
        // }

        // AuditOrgaArea area =
        // iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        // String roleGuid = roleService.listRole("统一收件人员",
        // "").get(0).getRoleGuid();
        // List<FrameUser> listUser =
        // userService.listUserByOuGuid(area.getOuguid(), roleGuid, "", "",
        // false, true, true,
        // 3);
        // for (FrameUser user : listUser) {
        // messageCenterService.deleteMessageByIdentifier(subappGuid,
        // user.getUserGuid());
        // }

    }

    public void projectSendzj(String isck, String projectguids, String subappguid) {
        log.info("接件上报数据");
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp != null) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();

            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditSpISubapp.getBiguid())
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
                sendMQMessageService.sendByExchange("exchange_handle", msg,
                        "blsp.subapp." + auditSpInstance.getBusinessguid());
            }

        }

    }

    /**
     * 供水-发起流程接口
     *
     * @param auditProject
     *            办件信息
     */
    public String startProcess(AuditProject auditProject) {
        try {
            log.info("========== 开始调用发起流程接口 ==========");
            String apiUrl = iConfigservice.getFrameConfigValue("submitUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到发起流程接口地址");
                return "";
            }

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            Map<String, Object> param = new HashMap<>();
            param.put("clientid", "JNGG");
            param.put("key", "JNGG");
            param.put("appuserno", "10001");
            param.put("flowcode", auditProject.getFlowsn());
            param.put("bustype", "1");
            param.put("cardname", auditProject.getApplyername());
            param.put("address", auditProject.getAddress());
            param.put("linkman", auditProject.getContactperson());
            param.put("tel", auditProject.getContactphone());
            log.info("发起流程接口地址--->" + apiUrl);
            log.info("请求头--->" + header);
            log.info("请求参数--->" + param);
            String resultStr = HttpUtil.doPost(apiUrl, param);
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if (resultJson.getBoolean("success")) {
                // 如果成功，获取单据编号进行保存
                String billNo = resultJson.getString("msg").split(",")[0];
                log.info("办件" + auditProject.getFlowsn() + "对应的单据编号为：" + billNo);
                auditProject.set("billno", billNo);
                auditProjectService.updateProject(auditProject); // 将单据编号保存到办件表中
                log.info("单据编号已保存");
                return billNo;
            }
            else {
                log.info("请求失败，" + resultJson.getString("msg"));
                return "";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("调用发起流程接口出现异常");
            return "";
        }
    }

    /**
     * 燃气报装提交接口
     */
    public String rqbztj(String subAppGuid, String projectGuid) {
        try {
            log.info("========== 开始调用燃气报装提交接口 ==========");
            JSONObject params = new JSONObject();
            params.put("subAppGuid", subAppGuid);
            params.put("projectGuid", projectGuid);
            // 注册接口地址
            String apiUrl = iConfigservice.getFrameConfigValue("rqzwdttjUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到网厅燃气提交接口地址");
                return "";
            }
            log.info("接口地址--->" + apiUrl);
            log.info("接口参数--->" + params.toJSONString());
            String resultStr = "";

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return HttpUtil.doPostJson(apiUrl, params.toJSONString());
                } catch (Exception e) {
                    System.out.println("请求出现异常: " + e.getMessage());
                    return "";
                }
            });

            // 主线程可以继续执行后续代码，这里只是简单示例后续代码可能做的事情
            System.out.println("主线程继续执行其他任务...");

            // 当需要获取请求结果时，可以通过以下方式等待并获取结果
            try {
                resultStr = future.get();
                // 在这里可以对获取到的结果进行进一步处理
                System.out.println("请求结果: " + resultStr);
            } catch (Exception e) {
                System.out.println("获取请求结果出现异常: " + e.getMessage());
            }
            // String resultStr = HttpUtil.doPostJson(apiUrl,
            // params.toJSONString(), new HashMap<>());
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                return "";
            }
            else {
                return resultStr;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用网厅燃气提交接口出现异常", e);
            return "";
        }
    }

    /**
     * 燃气身份证用户注册
     */
    public String rqsfzyhzc(AuditProject auditProject) {
        try {
            log.info("========== 开始调用燃气身份证用户注册接口 ==========");
            JSONObject params = new JSONObject();
            params.put("idCard", auditProject.getCertnum());
            params.put("projectGuid", auditProject.getRowguid());
            // 注册接口地址
            String apiUrl = iConfigservice.getFrameConfigValue("rqzwdtzcUrl");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到网厅燃气注册接口地址");
                return "";
            }
            log.info("接口地址--->" + apiUrl);
            log.info("接口参数--->" + params.toJSONString());
            String resultStr = HttpUtil.doPostJson(apiUrl, params.toJSONString());
            // HttpUtil.doPostJson(apiUrl, params.toJSONString(), new
            // HashMap<>());
            log.info("接口返回值--->" + resultStr);

            if (StringUtil.isBlank(resultStr)) {
                return "";
            }
            else {
                return resultStr;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用网厅燃气注册接口出现异常");
            return "";
        }
    }

    /**
     * 供水-流程提交检查接口
     *
     * @param flow
     *            办件流水号
     */
    public JSONObject checkSubmitFlow(String flow) {
        try {
            log.info("========== 开始调用流程提交检查接口 ==========");
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            String apiUrl = iConfigservice.getFrameConfigValue("checkSubmitFlowUrl");
            if (StringUtil.isBlank(apiUrl)) {
                result.put("msg", "未获取到提交流程检查接口");
                return result;
            }
            log.info("流程提交检查接口地址--->" + apiUrl);
            log.info("请求参数--->" + "?flowcode=" + flow);
            String resultStr = HttpUtil.doPost(apiUrl, null, null);
            log.info("接口返回值--->" + resultStr);
            if (StringUtil.isBlank(resultStr)) {
                log.info("未获取到接口返回值");
                result.put("msg", "未获取到接口返回值");
                result.put("code", "-1");
                return result;
            }

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("200".equals(resultJson.getString("code"))) {
                log.info("请求成功");
                result.put("isSuccess", true);
            }
            else {
                log.info("请求失败");
                result.put("code", "0");
                result.put("msg", resultJson.getString("msg"));
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("调用流程检查接口出现异常");
            JSONObject result = new JSONObject();
            result.put("isSuccess", false);
            result.put("code", "-1");
            result.put("msg", "调用流程检查接口出现异常");
            return result;
        }
    }

    /**
     * [接收办电预申请]
     */
    public String jsbdysq(String subAppGuid, AuditProject auditProject) {
        try {
            log.info("=======开始请求jsbdysq");
            log.info("subAppGuid--->" + subAppGuid);

            // 设置供电报装办件信息subappGuid、preApp
            // 在审批中判断是否是供电报装的办件，是的话设置办件subappGuid，并把办件guid设置到供电报装办件的projectguid

            log.info("查询供电报装的电子表单数据，参数subAppGuid--->" + subAppGuid);
            // 根据参数获取电子表单业务数据，供电报装表名：formtable20231108162746
            Record formDetail = cxBusService.getDzbdDetail("formtable20231108162746", subAppGuid);
            log.info("查询结果--->" + formDetail);
            // 将表单数据放入params
            JSONObject parms = new JSONObject();
            // 预申请编号（接口文档中37408002已被使用，修改为37408003）
            String preAppNo = "DL37408003"
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_NOSPLIT_FORMAT)
                    + (int) (Math.random() * 90 + 10);
            parms.put("preAppNo", preAppNo);
            // 服务渠道，默认64
            parms.put("channelNo", "64");
            // 省proCode，默认370000
            parms.put("proCode", "370000");
            // 市编码，默认370800
            parms.put("cityCode", "370800");
            // 报装意向基本事项标识
            List<AuditSpITask> result = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
            String taskGuid = "";
            for (AuditSpITask auditSpITask : result) {
                if (auditSpITask.getTaskname().contains("电")) {
                    taskGuid = auditSpITask.getTaskguid();
                    break;
                }
            }
            parms.put("meaningtaskguid", taskGuid);
            if(formDetail!=null){
                // 区县编码
                parms.put("countyCode", formDetail.getStr("qx"));
                // 街道
                parms.put("streetCode", formDetail.getStr("jd"));
                // 用电地址（详细地址）
                parms.put("elecAddr", formDetail.getStr("xxdz"));

                // 业务类型
                // parms.put("busiTypeCode", "0102");
                parms.put("busiTypeCode", formDetail.getStr("yewlx"));
                // 业务子类
                // parms.put("reduceCapMode", "010202");
                parms.put("reduceCapMode", formDetail.getStr("yewzl"));
                // 客户类型
                parms.put("custType", formDetail.getStr("kehlx"));
                // 用户编号
                parms.put("consNo", formDetail.getStr("yhbh"));
                // 供电单位
                parms.put("orgNo", formDetail.getStr("gddw"));
                // 用户名称
                parms.put("consName", formDetail.getStr("yhmc"));
                // 备注
                parms.put("remark", formDetail.getStr("bz"));
                // 是否个人充电桩*
                parms.put("ischcEqp", formDetail.getStr("sfgrcdz"));
                // 是否装变压器*
                parms.put("isInstTrans", formDetail.getStr("sfzbyq"));
                // 申请运行容量 capRunCap
                parms.put("capRunCap", formDetail.getStr("sqyxrl"));
                // 申请合同容量 contractCap
                parms.put("contractCap", formDetail.getStr("sqhtrl"));
            }


            // 联系人
            JSONArray custContactLists = new JSONArray();
            JSONObject contact = new JSONObject();
            // 联系信息标识，数字类型主键，不超过16为即可
            contact.put("contactId", new Random().nextInt(900000000) + 100000000);
            // 联系人类型 写死为17
            contact.put("contactMode", "17");
            if(formDetail!=null){
                // 联系人姓名
                contact.put("contactName", formDetail.getStr("lxrxm"));
                // 移动电话
                contact.put("custPhone", formDetail.getStr("yddh"));
            }
            custContactLists.add(contact);
            parms.put("custContactLists", custContactLists);

            // 附件下载地址，传第三方提供的默认值
            String powerEnv = iConfigservice.getFrameConfigValue("powerEnv");
            String fileUrl = "";
            if ("0".equals(powerEnv)) {
                // 测试环境
                fileUrl = "http://27.40.64.214/inner/c000/f01";
            }
            else {
                // 生产环境
                fileUrl = "http://25.219.177.202/inner/c000/f01";
            }
            // 材料
            JSONArray appDataLists = new JSONArray();
            // 根据projectGuid从audit_project_material中查询对应数据（多条），根据每一条数据的cliengguid从frame_attachinfo中查出附件地址存入。
            log.info("根据projectGuid查询AuditProjectMaterial");
            List<AuditProjectMaterial> materialList = iAuditProjectMaterial
                    .selectProjectMaterial(auditProject.getRowguid()).getResult();
            log.info("查询出的materialList--->" + materialList);
            if (ValidateUtil.isNotBlankCollection(materialList)) {
                log.info("材料list不为空");
                for (AuditProjectMaterial material : materialList) {
                    List<FrameAttachInfo> attachList = iAttachService.getAttachInfoListByGuid(material.getCliengguid());
                    log.info("查询出cliengguid为" + material.getCliengguid() + "对应的附件list--->" + attachList);
                    if (ValidateUtil.isNotBlankCollection(attachList)) {
                        log.info("附件list不为空");
                        for (FrameAttachInfo frameAttachInfo : attachList) {
                            JSONObject attachInfo = new JSONObject();
                            // 附件序号采用附件上传的时间戳
                            attachInfo.put("sequenceId", String.valueOf(frameAttachInfo.getUploadDateTime().getTime()));
                            // 附件类型有两个，分别为营业执照（120110）和不动产权证（120111）
                            attachInfo.put("fileType", "营业执照".equals(material.getTaskmaterial()) ? "120110" : "120111");
                            // 联系人类型写死为17
                            attachInfo.put("contactMode", "17");
                            // 证件号码
                            attachInfo.put("certNo", auditProject.getCertnum());
                            // 文件名称
                            attachInfo.put("fileName", frameAttachInfo.getAttachFileName());
                            // 文件地址
                            attachInfo.put("fileUrl", fileUrl);
                            // 文件类型
                            attachInfo.put("fvNo", frameAttachInfo.getContentType().split("\\.")[1]);
                            // 附件接口上传的id TODO 非强制要求，去掉上传附件操作
                            String attachDtlId="";
//                            String attachDtlId = gdFileUpload(frameAttachInfo.getAttachGuid(), frameAttachInfo);
                            if (StringUtil.isNotBlank(attachDtlId)) {
                                log.info("获取到了attachDtlId，添加附件信息");
                                attachInfo.put("fileId", attachDtlId);
                                appDataLists.add(attachInfo);
                            }
                            else {
                                log.info("不添加附件信息");
                            }
                        }
                    }
                }
            }
            parms.put("appDataLists", appDataLists);

            JSONObject paramsJson = new JSONObject();
            paramsJson.put("serviceCode", "20001780");
            paramsJson.put("source", "010262");
            paramsJson.put("target", "37101");
            paramsJson.put("data", parms);
            log.info("接收办电预申请接口参数--->" + paramsJson);
//            try{
//                String returnParam = com.epoint.core.utils.httpclient.HttpUtil
//                        .doPostJson(iConfigservice.getFrameConfigValue("bdysq_url"), paramsJson.toJSONString());
//                log.info("接受办电预申请接口返回值--->" + returnParam);
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return HttpUtil.doPostJson(iConfigservice.getFrameConfigValue("bdysq_url"), paramsJson.toJSONString());
                } catch (Exception e) {
                    System.out.println("请求出现异常: " + e.getMessage());
                    return "";
                }
            });

            // 主线程可以继续执行后续代码，这里只是简单示例后续代码可能做的事情
            System.out.println("主线程继续执行其他任务...");

            // 当需要获取请求结果时，可以通过以下方式等待并获取结果
            try {
                String returnParam = future.get();
                // 在这里可以对获取到的结果进行进一步处理
                System.out.println("请求结果: " + returnParam);
            } catch (Exception e) {
                System.out.println("获取请求结果出现异常: " + e.getMessage());
            }


            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
            log.info("获取当前auditSpISubapp实体--->" + auditSpISubapp);
            if (!ObjectUtils.isEmpty(auditSpISubapp)) {
                log.info("更新auditSpISubapp的preAppNo字段");
                auditSpISubapp.set("preAppNo", preAppNo);
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                log.info("更新成功");
            }
            log.info("新增供电报装办件");
            AuditSpProjectinfoGdbz projectInfoGdbz = new AuditSpProjectinfoGdbz();
            projectInfoGdbz.setRowguid(UUID.randomUUID().toString());
            log.info("设置供电报装办件的subAppGuid--->" + subAppGuid);
            projectInfoGdbz.setSubAppGuid(subAppGuid);
            log.info("设置供电报装办件的preAppNo--->" + preAppNo);
            projectInfoGdbz.set("preAppNo", preAppNo);
            projectInfoGdbz.set("operateDate", new Date());
            auditSpProjectinfoGdbzService.insert(projectInfoGdbz);
            // log.info("更新办件状态为已受理(30)");
            // auditProject.setStatus(30);
            // auditProjectService.updateProject(auditProject);
            log.info("更新成功");
            log.info("=======结束请求jsbdysq");
            return "请求成功";
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======jsbdysq出现异常");
            return "请求失败";
        }
    }

    /**
     * 供电附件上传接口
     */
    public String gdFileUpload(String attachGuid, FrameAttachInfo attachInfo) {
        try {
            log.info("========== 开始调用供电附件上传接口 ==========");
            String apiUrl = iConfigservice.getFrameConfigValue("bdysq_url");
            if (StringUtil.isBlank(apiUrl)) {
                log.info("未获取到供电附件上传接口");
                return null;
            }
            log.info("attachGuid--->" + attachGuid);
            // 根据附件attachGuid获取附件流
            InputStream content = iAttachService.getAttach(attachGuid).getContent();
            JSONObject params = new JSONObject();
            params.put("serviceCode", "20001002");
            params.put("source", "117005004");
            params.put("target", "37101");
            JSONObject data = new JSONObject();
            data.put("attachDtlCateg", "01");
            data.put("centerSrc", "flc");
            data.put("dispOpenFlag", "2");
            log.info("文件流转base64");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = content.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] bytes = outputStream.toByteArray();
            String fileBase64 = Base64.getEncoder().encodeToString(bytes);
            // log.info("文件base64码串--->" + fileBase64);
            data.put("fileContent", fileBase64);
            data.put("fileName", attachInfo.getAttachFileName());
            data.put("uploadStf", StringUtil.isBlank(attachInfo.getUploadUserDisplayName()) ? "系统管理员"
                    : attachInfo.getUploadUserDisplayName());
            params.put("data", data);

            log.info("供电上传附件接口地址--->" + apiUrl);
            // log.info("供电上传附件接口请求参数--->" + params);
            String resultStr = com.epoint.core.utils.httpclient.HttpUtil
                    .doPostJson(iConfigservice.getFrameConfigValue("bdysq_url"), params.toJSONString());
            log.info("接口返回值--->" + resultStr);

            JSONObject resultJson = JSON.parseObject(resultStr);
            if ("1".equals(resultJson.getString("code"))) {
                // 请求成功
                String attachDtlId = resultJson.getJSONObject("data").getJSONObject("data").getString("attachDtlId");
                log.info("附件id--->" + attachDtlId);
                return attachDtlId;
            }
            else {
                // 请求失败
                log.info("请求失败，原因：" + resultJson.getString("message"));
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("调用供电附件上传接口出现异常");
            return null;
        }
    }

    /**
     * 检查补正材料是否都已提交
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkBZMaterialIsSubmit", method = RequestMethod.POST)
    public String checkBZMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkBZMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的列表数据
                String materialList = obj.getString("materiallist");
                int noSubmitNum = 0;// 补正材料没有提交的个数
                // 2、将材料信息转换为弱类型的实体集合
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    // 2.1、获取办件材料的提交状态
                    String status = record.getStr("status");
                    // 2.2、获取材料实例标识
                    String materialguid = record.getStr("materialguid");
                    // 2.3、获取材料实例信息
                    AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(materialguid)
                            .getResult();
                    String isRongque = "";
                    String necessity = "";
                    if ("1".equals(auditSpIMaterial.getShared())) {
                        // 查询共享材料
                        AuditSpShareMaterial auditSpiSharedmaterial = iAuditSpShareMaterial
                                .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                        auditSpIMaterial.getBusinessguid())
                                .getResult();
                        if (auditSpiSharedmaterial != null) {
                            // 是否必须 10必须 20非必须
                            necessity = auditSpiSharedmaterial.getNecessity();
                            // 是否容缺
                            isRongque = auditSpiSharedmaterial.getRongque();// 只有必要的材料才需要显示容缺
                        }
                    }
                    else {
                        // 是否必须 10必须 20非必须
                        necessity = auditSpIMaterial.getNecessity();
                        // 是否容缺
                        isRongque = auditSpIMaterial.getAllowrongque();// 只有必要的材料才需要显示容缺
                    }
                    if (auditSpIMaterial != null) {
                        // 图审联审的必要材料无需提交
                        String materialType = auditSpIMaterial.getStr("materialtype");
                        if (StringUtil.isNotBlank(materialType)
                                && ("tscl".equals(materialType) || "lscl".equals(materialType))) {
                            continue;
                        }
                        // 获取系统参数：纸质必要材料外网是否一定要上传
                        String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                        paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity) ? ZwfwConstant.CONSTANT_STR_ZERO
                                : paperUnnecrsstity;
                        // 必要非容缺材料没有提交：
                        if (ZwfwConstant.NECESSITY_SET_YES.equals(necessity)
                                && !ZwdtConstant.STRING_YES.equals(isRongque)
                                && Integer.parseInt(status) != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC && Integer
                                        .parseInt(status) != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                            // 如果纸质材料外网不必上传，对提交纸质材料即可的材料不做限制
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                // 10代表提交电子材料；20代表提交纸质材料；35代表提交电子或纸质材料；40代表同时提交电子和纸质材料
                                if ("10".equals(auditSpIMaterial.getSubmittype())
                                        || "40".equals(auditSpIMaterial.getSubmittype())) {
                                    noSubmitNum++;
                                }
                            }
                            // 如果纸质必要材料外网仍需上传
                            else {
                                noSubmitNum++;
                            }
                        }
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkBZMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查补正材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkBZMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkBZMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查补正材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交补正材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitSubappBZMaterial", method = RequestMethod.POST)
    public String submitSubappBZMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitSubappBZMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.2、获取子申报实例标识
                String subAppGuid = obj.getString("subappguid");
                // 1.3、获取子申报数据
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                if (auditSpISubapp != null) {
                    // 1.4、子申报状态为办件补正
                    if (ZwfwConstant.LHSP_Status_DBJ.equals(auditSpISubapp.getStatus())) {
                        // 1.4.1、获取子申报下未办理的事项实例的数量
                        List<AuditSpITask> auditSpITaskList = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid)
                                .getResult();
                        Long notBanli = auditSpITaskList.stream()
                                .filter(task -> StringUtil.isBlank(task.getStatus())
                                        || ZwfwConstant.CONSTANT_STR_ZERO.equals(task.getStatus()))
                                .collect(Collectors.counting());
                        // 1.4.2、如果子申报下事项实例都在办理中，修改子申报状态为已收件
                        if (notBanli == ZwfwConstant.CONSTANT_INT_ZERO) {
                            iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_YSJ, null);
                        }
                        // 1.4.3、如果子申报下事项实例仍有未办理，修改子申报状态为待预审
                        else {
                            iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
                        }
                    }
                    // 1.5、子申报状态为办件预审待补正
                    else if (ZwfwConstant.LHSP_Status_YSDBZ.equals(auditSpISubapp.getStatus())) {
                        // 3、更新子申报状态为待预审
                        iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "您提交的事项状态已经变更<br>请前往申报中心查看", "");
                    }
                }
                // 更新spitask 状态为办理中
                // 3、获取子申报事项实例列表 只查补正的
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("status", "2");
                sql.eq("subappguid", subAppGuid);
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getAuditSpITaskListByCondition(sql.getMap())
                        .getResult();
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    auditSpITask.setStatus(ZwfwConstant.CONSTANT_STR_ONE);
                    iAuditSpITask.updateAuditSpITask(auditSpITask);
                }

                // 4、更新所有的材料状态为已补正
                iAuditSpIMaterial.updateSpIMaterialPatchStatus(subAppGuid);
                // 如果是项目材料补正向指定用户发送待办
                if (ZwfwConstant.LHSP_Status_YSDBZ.equals(auditSpISubapp.getStatus())) {
                    // 向指定用户发送待办
                    String handleUrl = "/epointzwfw/auditsp/auditsphandle/handlebicheckmaterialdetail?subappGuid="
                            + auditSpISubapp.getRowguid() + "&businesstype=1&biGuid=" + auditSpISubapp.getBiguid();
                    String title = "【项目材料补正】" + auditSpISubapp.getSubappname() + "(" + auditSpISubapp.getApplyername()
                            + ")";
                    // 查询辖区
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                            .getAuditSpBusinessByRowguid(auditSpISubapp.getBusinessguid()).getResult();
                    iAuditOnlineMessages.sendMsg("统一收件人员", title, auditSpISubapp.getApplyerguid(),
                            auditSpISubapp.getApplyername(), auditSpBusiness.getAreacode(), handleUrl,
                            auditSpISubapp.getRowguid(), "zwfwMsgurl", null);
                }
                log.info("=======结束调用submitSubappBZMaterial接口=======");

                // 补正完成调用综管的接口
                String zwfwurl = iConfigservice.getFrameConfigValue("zwfwurl") + "/rest/jnauditsp/buzhengWc";
                JSONObject data = new JSONObject();
                JSONObject param = new JSONObject();
                data.put("params", param);
                param.put("subappguid", subAppGuid);
                HttpUtil.doPostJson(zwfwurl, data.toJSONString());

                return JsonUtils.zwdtRestReturn("1", "提交材料成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitSubappBZMaterial接口参数：params【" + params + "】=======");
            log.info("=======submitSubappBZMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交材料失败：" + e.getMessage(), "");
        }
    }

    /**********************************************
     * 申报相关接口结束
     **********************************************/

    /**********************************************
     * 咨询相关接口开始
     **********************************************/
    /**
     * 新增并联审批的咨询
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */

    @RequestMapping(value = "/addItemConsult", method = RequestMethod.POST)
    public String addItemConsult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用addItemConsult接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取提问内容
                String question = obj.getString("question");
                // 1.1、获取需要咨询内容代码项
                String consultCode = obj.getString("consultcode");
                // 1.1、获取附件标识
                String clientGuid = obj.getString("clientguid");
                // 1.1、获取提问人名称
                String askName = obj.getString("askname");
                // 1.1、获取手机号码
                String mobile = obj.getString("mobile");
                // 1.1、获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.1、获取项目唯一标识
                String companyid = obj.getString("companyid");
                // 1.1、获取项目辖区
                String areacode = obj.getString("areacode");
                // 图形验证码
                String verifyCode = obj.getString("verifycode");
                // 图形验证码主键
                String codeRowguid = obj.getString("coderowguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.3 如果使用验证码验证
                if (StringUtil.isNotBlank(codeRowguid)) {
                    // 1.3.1、判断是否有redis
                    AuditOnlineVerycode onlineVerycode = null;
                    String imgkey = "img-" + verifyCode + "-" + codeRowguid;
                    if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                        ZwfwRedisCacheUtil redisUtil = new ZwfwRedisCacheUtil();
                        // 1.3.2、去redis中验证扫描信息
                        onlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, imgkey, "6");
                        redisUtil.close();
                    }
                    else {
                        onlineVerycode = iAuditOnlineVeryCode.getVerycodeByRowguid(codeRowguid).getResult();
                    }
                    // 1.3.3、判断验证码不为空
                    if (onlineVerycode != null && StringUtil.isNotBlank(onlineVerycode.getVerifycode())) {
                        // 1.3.4、判断验证码是否正确
                        if (onlineVerycode.getVerifycode().equals(verifyCode.toUpperCase())) {
                            // 1.3.5、判断验证码是否已失效
                            if ("1".equals(onlineVerycode.getIsverified())) {
                                return JsonUtils.zwdtRestReturn("0", "图片验证码已失效，请重新获取", "");
                            }
                            else {
                                // 1.3.6、设置图片验证码为已使用
                                if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
                                    ZwfwRedisCacheUtil updateRedisUtil = new ZwfwRedisCacheUtil();
                                    // 1.3.7、去redis中验证扫描信息
                                    updateRedisUtil.updateByHashzwdtvcode(AuditOnlineVerycode.class, imgkey, "6",
                                            "isverified", "1");
                                    updateRedisUtil.updateByHashzwdtvcode(AuditOnlineVerycode.class, imgkey, "6",
                                            "accountguid", auditOnlineRegister.getAccountguid());
                                    updateRedisUtil.close();
                                }
                                else {
                                    onlineVerycode.setAccountguid(auditOnlineRegister.getAccountguid());
                                    onlineVerycode.setIsverified("1");
                                    iAuditOnlineVeryCode.updateAuditOnlineVerycode(onlineVerycode);
                                }
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "图片验证码输入错误", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "获取验证码失败", "");
                    }

                }
                if (auditOnlineRegister != null) {
                    AuditOnlineConsult auditOnlineConsult = new AuditOnlineConsult();
                    auditOnlineConsult.setRowguid(UUID.randomUUID().toString());
                    auditOnlineConsult.setAskdate(new Date());// 提问时间
                    auditOnlineConsult.setClientguid(clientGuid);
                    auditOnlineConsult.setAskeruserguid(auditOnlineRegister.getAccountguid());// 用户唯一标识
                    auditOnlineConsult.setAskerusername(askName);// 提问人姓名
                    auditOnlineConsult.setAskerMobile(mobile);// 提问人手机号
                    auditOnlineConsult.setAskerloginid(auditOnlineRegister.getLoginid());// 提问人登录账号
                    auditOnlineConsult.setQuestion(question);// 咨询投诉内容
                    auditOnlineConsult.setConsultcode(consultCode);
                    auditOnlineConsult.setItemguid(itemGuid);
                    auditOnlineConsult.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_NO);// 咨询记录阅读状态：未读
                    auditOnlineConsult.setSource(ZwdtConstant.CONSULT_SORUCE_WWKJ);// 咨询建议来源：外网空间
                    auditOnlineConsult.setStatus(ZwfwConstant.ZIXUN_TYPE_DDF);// 状态：待答复
                    auditOnlineConsult.setConsulttype(ZwfwConstant.CONSULT_TYPE_SPZX);
                    auditOnlineConsult.setCompanyid(companyid);
                    auditOnlineConsult.setAreaCode(areacode);
                    iAuditOnlineConsult.addConsult(auditOnlineConsult);
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    log.info("=======结束调用addConsult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "新增成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取登录用户失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getItemConsultList接口参数：params【" + params + "】=======");
            log.info("=======getItemConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "新增并联审批的咨询失败", "");
        }
    }

    /**
     * 获取并联审批的咨询列表
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getItemConsultList", method = RequestMethod.POST)
    public String getItemConsultList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemConsultList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、搜索条件
                String keyWord = obj.getString("keyword");
                // 1.2、一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、当前页码
                String currentPage = obj.getString("currentpage");
                // 1.4、排序方式 0：升序1：降序
                String clockWise = obj.getString("clockwise");
                // 1.5、获取咨询状态 0：未回复1：已回复
                String status = obj.getString("status");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取用户提出的项目相关的咨询
                    // 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                    // 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    String strWhereCompanyId = "";// 拼接被授权的所有企业id
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                    }
                    // 如果当前登陆人是法人，则没有授权信息。需要查法人身份证所属的企业
                    SqlConditionUtil sqlLegal = new SqlConditionUtil();
                    sqlLegal.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlLegal.eq("is_history", "0");
                    sqlLegal.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlLegal.getMap()).getResult();
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        strWhereCompanyId += "'" + auditRsCompanyBaseinfo.getCompanyid() + "',";
                    }
                    Integer totalcount = 0;
                    List<JSONObject> consultJsonList = new ArrayList<JSONObject>();
                    if (StringUtil.isNotBlank(strWhereCompanyId)) {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.in("companyid",
                                strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1));
                        if (StringUtil.isNotBlank(keyWord)) {
                            sqlConditionUtil.like("question", keyWord);
                        }
                        sqlConditionUtil.eq("consulttype", ZwfwConstant.CONSULT_TYPE_SPZX);

                        // 3.1、设置排序
                        if (ZwdtConstant.STRING_NO.equals(clockWise)) {
                            sqlConditionUtil.setOrderAsc("askdate");
                        }
                        else {
                            sqlConditionUtil.setOrderDesc("askdate");
                        }
                        // 3.2、设置状态
                        if (ZwdtConstant.STRING_NO.equals(status)) {
                            sqlConditionUtil.eq("status", ZwfwConstant.ZIXUN_TYPE_DDF);
                        }
                        else if (ZwdtConstant.STRING_YES.equals(status)) {
                            sqlConditionUtil.eq("status", ZwfwConstant.ZIXUN_TYPE_YDF);
                        }
                        PageData<AuditOnlineConsult> auditOnlineConsultPageData = iAuditOnlineConsult
                                .selectConsultByPageData(sqlConditionUtil.getMap(),
                                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                        Integer.parseInt(pageSize), "", "")
                                .getResult();
                        totalcount = auditOnlineConsultPageData.getRowCount();

                        // 4、遍历获取到的咨询
                        for (AuditOnlineConsult auditOnlineConsult : auditOnlineConsultPageData.getList()) {
                            JSONObject consultJson = new JSONObject();
                            consultJson.put("consultguid", auditOnlineConsult.getRowguid());// 咨询投诉标识
                            consultJson.put("question", auditOnlineConsult.getQuestion());// 问题
                            consultJson.put("askdate", EpointDateUtil.convertDate2String(
                                    auditOnlineConsult.getAskdate(), EpointDateUtil.DATE_TIME_FORMAT));// 提问时间
                            String isanswer = ZwdtConstant.STRING_NO;
                            if (ZwfwConstant.ZIXUN_TYPE_YDF.equals(auditOnlineConsult.getStatus())) {
                                isanswer = ZwdtConstant.STRING_YES;
                            }
                            consultJson.put("isanswer", isanswer);
                            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(auditOnlineConsult.getItemguid()).getResult();
                            if (auditRsItemBaseinfo != null) {
                                consultJson.put("itemname", auditRsItemBaseinfo.getItemname());
                            }
                            consultJsonList.add(consultJson);
                        }
                    }

                    JSONObject dataJson = new JSONObject();
                    dataJson.put("totalcount", totalcount);
                    dataJson.put("consultlist", consultJsonList);
                    log.info("=======结束调用getItemConsultList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "查询咨询列表成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取登录用户失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getItemConsultList接口参数：params【" + params + "】=======");
            log.info("=======getItemConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询咨询列表异常" + e.getMessage(), "");
        }
    }

    /**
     * 获取并联审批的咨询详情
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getItemConsultDetail", method = RequestMethod.POST)
    public String getItemConsultDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemConsultDetail接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目咨询标识
                String consultGuid = obj.getString("consultguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、获取咨询基本信息实体
                    AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultGuid)
                            .getResult();
                    if (auditOnlineConsult != null) {
                        JSONObject consultJson = new JSONObject();
                        // 3.1、获取咨询基本信息
                        consultJson.put("askname", auditOnlineConsult.getAskerusername());
                        consultJson.put("askdate", EpointDateUtil.convertDate2String(auditOnlineConsult.getAskdate(),
                                EpointDateUtil.DATE_TIME_FORMAT));
                        consultJson.put("consultcode",
                                StringUtil.isBlank(auditOnlineConsult.getConsultcode()) ? ""
                                        : iCodeItemsService.getItemTextByCodeName("并联审批咨询项",
                                                auditOnlineConsult.getConsultcode()));
                        consultJson.put("question", auditOnlineConsult.getQuestion());
                        String itemGuid = auditOnlineConsult.getItemguid();
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                        String itemName = auditRsItemBaseinfo == null ? "" : auditRsItemBaseinfo.getItemname();// 获取项目名称
                        consultJson.put("itemname", itemName);
                        String deptName = auditRsItemBaseinfo == null ? "" : auditRsItemBaseinfo.getItemlegaldept();// 获取项目名称
                        consultJson.put("companyname", deptName);
                        consultJson.put("isanswer", "0");
                        // 3.3、获取咨询提问的相关附件
                        String clientGuid = auditOnlineConsult.getClientguid();
                        if (StringUtil.isNotBlank(clientGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                            List<JSONObject> attachList = new ArrayList<JSONObject>();
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                JSONObject attachJson = new JSONObject();
                                attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                                attachJson.put("attchname", frameAttachInfo.getAttachFileName());
                                String attchsrc = WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid();
                                attachJson.put("attchsrc", attchsrc);
                                attachList.add(attachJson);
                            }
                            consultJson.put("attachlist", attachList);
                        }
                        // 3.2、获取咨询回复信息
                        if (ZwfwConstant.ZIXUN_TYPE_YDF.equals(auditOnlineConsult.getStatus())) {
                            consultJson.put("isanswer", "1");
                            consultJson.put("answer", auditOnlineConsult.getAnswer());
                            consultJson.put("answerdate", EpointDateUtil.convertDate2String(
                                    auditOnlineConsult.getAnswerdate(), EpointDateUtil.DATE_TIME_FORMAT));// 回答时间
                            if (StringUtil.isNotBlank(auditOnlineConsult.getAnswerouguid())) {
                                FrameOu frameOu = iOuService.getOuByOuGuid(auditOnlineConsult.getAnswerouguid());
                                consultJson.put("ouname", frameOu == null ? "" : frameOu.getOuname());
                            }
                            String clientApplyGuid = auditOnlineConsult.getClientapplyguid();
                            if (StringUtil.isNotBlank(clientApplyGuid)) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(clientApplyGuid);
                                List<JSONObject> attachList = new ArrayList<JSONObject>();
                                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                    JSONObject attachJson = new JSONObject();
                                    attachJson.put("answerattachguid", frameAttachInfo.getAttachGuid());
                                    attachJson.put("answerattchname", frameAttachInfo.getAttachFileName());
                                    String attchsrc = WebUtil.getRequestCompleteUrl(request)
                                            + "/rest/auditattach/readAttach?attachguid="
                                            + frameAttachInfo.getAttachGuid();
                                    attachJson.put("answerattchsrc", attchsrc);
                                    attachList.add(attachJson);
                                }
                                consultJson.put("answerattachlist", attachList);
                            }
                        }
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("consultdetail", consultJson);
                        return JsonUtils.zwdtRestReturn("1", "查询咨询详情成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "获取咨询详细信息失败", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取登录用户失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getItemConsultList接口参数：params【" + params + "】=======");
            log.info("=======getItemConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询咨询列表异常", "");
        }
    }

    /**
     * 获取单位信息
     *
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getParticipants", method = RequestMethod.POST)
    public String getParticipants(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getParticipants接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject param = jsonObject.getJSONObject("params");
                // 1.1、获取子申报实例
                String subAppGuid = param.getString("subappguid");
                // 1.2、获取项目标识
                String itemGuid = param.getString("itemguid");
                // 1.3、获得单位type
                String corpType = param.getString("corptype");
                JSONObject dataJson = new JSONObject();
                List<ParticipantsInfo> participantsInfos = iparticipantsInfoService.getParticipantlist(subAppGuid,
                        itemGuid, corpType);
                List<JSONObject> participantsList = new ArrayList<JSONObject>();
                for (ParticipantsInfo participantsInfo : participantsInfos) {
                    JSONObject particiJson = new JSONObject();
                    particiJson.put("par_rowguid", participantsInfo.getRowguid());// 单位信息的rowguid字段
                    particiJson.put("corpname", participantsInfo.getCorpname());// 单位名称
                    particiJson.put("corpcodestr", participantsInfo.getCorpcode());// 统一社会信用代码
                    particiJson.put("legal", participantsInfo.getLegal());// 法定代表人
                    particiJson.put("phone", participantsInfo.getPhone());// 单位联系电话
                    particiJson.put("fbtime", participantsInfo.getFbtime());// 法人性质
                    particiJson.put("itemlegalcerttype", participantsInfo.getItemlegalcerttype());// 项目(法人)证件类型
                    particiJson.put("xmfzr", participantsInfo.getXmfzr());// 项目负责人
                    particiJson.put("xmfzr_phone", participantsInfo.getXmfzr_phone());// 项目负责人联系方式
                    if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                        particiJson.put("cbdanweitype",
                                "01".equals(participantsInfo.getCbdanweitype()) ? "总包单位" : "专业承包及劳务分包单位");// 项目负责人联系方式
                    }
                    participantsList.add(particiJson);
                }
                dataJson.put("participantsList", participantsList);
                return JsonUtils.zwdtRestReturn("1", "获取单位列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getParticipants接口参数：params【" + params + "】=======");
            log.info("=======getParticipants接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取单位列表获取异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取单位详情
     *
     * @return
     */
    @RequestMapping(value = "/getParticipantsByRowguid", method = RequestMethod.POST)
    public String getParticipantsByRowguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getParticipantsByRowguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject param = jsonObject.getJSONObject("params");
                String rowguid = param.getString("guid");
                String itemguid = param.getString("itemguid");
                JSONObject particiJson = new JSONObject();
                // 建设单位新增
                if (StringUtil.isNotBlank(itemguid)) {
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    particiJson.put("corpname", auditRsItemBaseinfo.getDepartname());// 单位名称
                    particiJson.put("corpcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());// 统一社会信用代码
                    particiJson.put("address", auditRsItemBaseinfo.getConstructionaddress());// 单位地址
                    particiJson.put("phone", auditRsItemBaseinfo.getContractphone());// 单位联系电话
                    particiJson.put("legal", auditRsItemBaseinfo.getItemlegaldept());// 法定代表人
                    particiJson.put("legalproperty", auditRsItemBaseinfo.getLegalproperty());// 法人性质
                    particiJson.put("itemlegalcerttype", auditRsItemBaseinfo.getItemlegalcerttype());// 项目(法人)证照类型
                    particiJson.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());// 项目(法人)证照号码
                    particiJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());// 项目(法人)单位
                    particiJson.put("frphone", auditRsItemBaseinfo.getFrphone());// 法人电话
                    particiJson.put("legalpersonicardnum", auditRsItemBaseinfo.getLegalpersonicardnum());// 法人身份证
                    particiJson.put("fremail", auditRsItemBaseinfo.getFremail());// 法人邮编

                }
                if (StringUtil.isNotBlank(rowguid)) {
                    ParticipantsInfo participantsInfo = iparticipantsInfoService.find(rowguid);
                    particiJson.put("corpname", participantsInfo.getCorpname());// 单位名称
                    particiJson.put("corpcode", participantsInfo.getCorpcode());// 统一社会信用代码
                    particiJson.put("address", participantsInfo.getAddress());// 单位地址
                    particiJson.put("phone", participantsInfo.getPhone());// 单位联系电话
                    particiJson.put("legal", participantsInfo.getLegal());// 法定代表人
                    particiJson.put("legalproperty", participantsInfo.getLegalproperty());// 法人性质
                    particiJson.put("itemlegalcerttype", participantsInfo.getItemlegalcerttype());// 项目(法人)证照类型
                    // 根据代码ID以及代码项值获取代码项文本
                    if (StringUtil.isNotBlank(participantsInfo.getItemlegalcerttype())) {
                        CodeItems itemlegalcerttype = iCodeItemsService.getCodeItemByCodeName("申请人用来唯一标识的证照类型",
                                participantsInfo.getItemlegalcerttype());
                        particiJson.put("itemlegalcerttypeText", itemlegalcerttype.getItemText());// 项目(法人)证照类型文本
                    }
                    particiJson.put("itemlegalcertnum", participantsInfo.getItemlegalcertnum());// 项目(法人)证照号码
                    particiJson.put("itemlegaldept", participantsInfo.getItemlegaldept());// 项目(法人)单位
                    particiJson.put("legalpersonicardnum", participantsInfo.getLegalpersonicardnum());// 法人身份证
                    particiJson.put("frphone", participantsInfo.getFrphone());// 法人电话
                    particiJson.put("fremail", participantsInfo.getFremail());// 法人邮编
                    particiJson.put("xmfzr", participantsInfo.getXmfzr());// 项目负责人
                    particiJson.put("xmfzr_zc", participantsInfo.getXmfzr_zc());// 职称
                    particiJson.put("xmfzr_idcard", participantsInfo.getXmfzr_idcard());// 身份证
                    particiJson.put("xmfzr_phone", participantsInfo.getXmfzr_phone());// 项目负责人联系电话
                    if (StringUtil.isNotBlank(participantsInfo.getCbdanweitype())) {
                        particiJson.put("cbdanweitype", participantsInfo.getCbdanweitype());
                    }
                    particiJson.put("jsfzr", participantsInfo.getJsfzr());
                    particiJson.put("jsfzr_zc", participantsInfo.getJsfzr_zc());
                    particiJson.put("jsfzr_phone", participantsInfo.getJsfzr_phone());
                    particiJson.put("xmfzperson", participantsInfo.getXmfzperson());
                    particiJson.put("xmfzrsafenum", participantsInfo.getXmfzrsafenum());
                    particiJson.put("xmfzrphonenum", participantsInfo.getXmfzrphonenum());
                    particiJson.put("qylxr", participantsInfo.getQylxr());
                    particiJson.put("qylxdh", participantsInfo.getQylxdh());
                    particiJson.put("gdlxr", participantsInfo.getGdlxr());
                    particiJson.put("gdlxdh", participantsInfo.getGdlxdh());
                    particiJson.put("fbsafenum", participantsInfo.getFbsafenum());
                    particiJson.put("fbtime", participantsInfo.getFbtime());
                    particiJson.put("fbscopeofcontract", participantsInfo.getFbscopeofcontract());
                    particiJson.put("fbaqglrysafenum", participantsInfo.getFbaqglrysafenum());
                    particiJson.put("fbqysettime",
                            EpointDateUtil.convertDate2String(participantsInfo.getFbqysettime(), "yyyy-MM-dd"));
                    particiJson.put("fbaqglry", participantsInfo.getFbaqglry());
                    particiJson.put("danweilxr", participantsInfo.getDanweilxr());
                    particiJson.put("danweilxrlxdh", participantsInfo.getDanweilxrlxdh());
                    particiJson.put("danweilxrsfz", participantsInfo.getDanweilxrsfz());
                    particiJson.put("frphone", participantsInfo.getFrphone());
                    particiJson.put("fremail", participantsInfo.getFremail());
                    particiJson.put("legalproperty", participantsInfo.getLegalproperty());
                }
                return JsonUtils.zwdtRestReturn("1", "获取单位详情成功", particiJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getParticipantsByRowguid接口参数：params【" + params + "】=======");
            log.info("=======getParticipantsByRowguid接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取单位详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取法人性质代码项
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @ [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getLegalpropertyModel", method = RequestMethod.POST)
    public String getLegalpropertyModel(@RequestBody String params, @Context HttpServletRequest request) {
        // 接口的入参转化为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String codeName = param.getString("codeName");// 获取代码项名称
            String fileVaule = param.getString("legalproperty");// 获取当前表里存的值
            JSONObject json = new JSONObject();
            List<JSONObject> legalpropertyList = new ArrayList<JSONObject>();
            JSONObject initJson = new JSONObject();
            initJson.put("itemvalue", "");
            initJson.put("itemtext", "请选择");
            initJson.put("isselected", 1);
            legalpropertyList.add(initJson);
            // 获取法人性质代码项目
            List<CodeItems> itemTypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
            for (CodeItems codeItems : itemTypes) {
                JSONObject objJson = new JSONObject();
                objJson.put("itemvalue", codeItems.getItemValue());
                objJson.put("itemtext", codeItems.getItemText());
                // 判断是否有默认选中的
                if (codeItems.getItemValue().equals(fileVaule)) {
                    objJson.put("isselected", 1);
                }
                legalpropertyList.add(objJson);
            }
            json.put("legalpropertyList", legalpropertyList);
            return json.toJSONString();
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }
    }

    /**
     * 需要选择单位和人员的信息的单位新增
     */
    @ResponseBody
    @RequestMapping(value = "/getAddParticipants", method = RequestMethod.POST, produces = "json/html;charset=utf-8")
    public String getAddParticipants(@RequestBody String params, @Context HttpServletRequest request) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String itemguid = param.getString("itemguid");
            String subappguid = param.getString("subappguid");
            if (StringUtil.isNotBlank(itemguid) && StringUtil.isNotBlank(subappguid)) {
                ParticipantsInfo participantsInfo = new ParticipantsInfo();
                participantsInfo.setRowguid(UUID.randomUUID().toString());
                participantsInfo.setOperatedate(new Date());
                participantsInfo.setSubappguid(subappguid);// 子申报示例
                participantsInfo.setItemguid(itemguid);// 项目标识
                participantsInfo.setCorpname(param.getString("corpname"));// 单位名称
                participantsInfo.setCorpcode(param.getString("corpcode"));// 统一社会信用代码
                participantsInfo.setLegal(param.getString("legal"));// 法定代表人
                participantsInfo.setPhone(param.getString("phone"));// 联系电话
                participantsInfo.setAddress(param.getString("address"));// 单位地址
                participantsInfo.setXmfzr(param.getString("xmfzr"));// 项目负责人
                participantsInfo.setXmfzr_zc(param.getString("xmfzr_zc"));// 职称
                participantsInfo.setXmfzr_idcard(param.getString("xmfzr_idcard"));// 身份证
                participantsInfo.setXmfzr_phone(param.getString("xmfzr_phone"));// 项目负责任人联系电话
                participantsInfo.setCorptype(param.getString("corptype"));// 单位类型
                participantsInfo.setLegalproperty(param.getString("legalproperty"));
                participantsInfo.setItemlegalcerttype(param.getString("itemlegalcerttype"));
                participantsInfo.setItemlegalcertnum(param.getString("itemlegalcertnum"));
                participantsInfo.setItemlegaldept(param.getString("itemlegaldept"));
                participantsInfo.setLegalpersonicardnum(param.getString("legalpersonicardnum"));
                participantsInfo.setFrphone(param.getString("frphone"));
                participantsInfo.setFremail(param.getString("fremail"));
                participantsInfo.setDanweilxr(param.getString("danweilxr"));
                participantsInfo.setDanweilxrlxdh(param.getString("danweilxrlxdh"));
                participantsInfo.setDanweilxrsfz(param.getString("danweilxrsfz"));
                participantsInfo.setLegalproperty(param.getString("legalproperty"));
                // 施工单位
                if ("3".equals(param.getString("corptype"))) {
                    participantsInfo.setCbdanweitype(param.getString("cbdanweitype"));
                    participantsInfo.setJsfzr(param.getString("jsfzr"));
                    participantsInfo.setJsfzr_zc(param.getString("jsfzr_zc"));
                    participantsInfo.setJsfzr_phone(param.getString("jsfzr_phone"));
                    participantsInfo.setXmfzperson(param.getString("xmfzperson"));
                    participantsInfo.setXmfzrsafenum(param.getString("xmfzrsafenum"));
                    participantsInfo.setXmfzrphonenum(param.getString("xmfzrphonenum"));
                    participantsInfo.setQylxr(param.getString("qylxr"));
                    participantsInfo.setQylxdh(param.getString("qylxdh"));
                    participantsInfo.setGdlxr(param.getString("gdlxr"));
                    participantsInfo.setGdlxdh(param.getString("gdlxdh"));
                    participantsInfo.setFbsafenum(param.getString("fbsafenum"));
                    participantsInfo.setFbtime(param.getString("fbtime"));
                    participantsInfo.setFbscopeofcontract(param.getString("fbscopeofcontract"));
                    if (StringUtil.isNotBlank(param.getString("fbqysettime"))) {
                        participantsInfo.setFbqysettime(
                                EpointDateUtil.convertString2Date(param.getString("fbqysettime"), "yyyy-MM-dd"));
                    }
                    participantsInfo.setFbaqglry(param.getString("fbaqglry"));
                    participantsInfo.setFbaqglrysafenum(param.getString("fbaqglrysafenum"));
                }

                iparticipantsInfoService.insert(participantsInfo);
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "保存失败！", "");
            }
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }
    }

    /**
     * 根据rowguid修改单位信息
     */
    @ResponseBody
    @RequestMapping(value = "/getUpdateParticipant", method = RequestMethod.POST, produces = "json/html;charset=utf-8")
    public String getUpdateParticipant(@RequestBody String params, @Context HttpServletRequest request) {
        // 1、接口的入参转化为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(params);
        String token = jsonObject.getString("token");
        if (ZwdtConstant.SysValidateData.equals(token)) {
            JSONObject param = jsonObject.getJSONObject("params");
            String rowguid = param.getString("rowguid");
            if (StringUtil.isNotBlank(rowguid)) {
                ParticipantsInfo participantsInfos = iparticipantsInfoService.find(rowguid);
                participantsInfos.setCorpname(param.getString("corpname"));// 单位名称
                participantsInfos.setCorpcode(param.getString("corpcode"));// 统一社会信用代码
                participantsInfos.setAddress(param.getString("address"));// 单位地址
                participantsInfos.setPhone(param.getString("phone"));// 单位联系电话
                participantsInfos.setLegal(param.getString("legal"));// 法定代表人
                participantsInfos.setXmfzr(param.getString("xmfzr"));// 项目负责人
                participantsInfos.setXmfzr_zc(param.getString("xmfzr_zc"));// 职称
                participantsInfos.setXmfzr_idcard(param.getString("xmfzr_idcard"));// 身份证
                participantsInfos.setXmfzr_phone(param.getString("xmfzr_phone"));// 项目负责人联系电话
                participantsInfos.setOperatedate(new Date());
                participantsInfos.setItemlegalcerttype(param.getString("itemlegalcerttype"));
                participantsInfos.setItemlegalcertnum(param.getString("itemlegalcertnum"));
                participantsInfos.setItemlegaldept(param.getString("itemlegaldept"));
                participantsInfos.setLegalpersonicardnum(param.getString("legalpersonicardnum"));
                participantsInfos.setFrphone(param.getString("frphone"));
                participantsInfos.setFremail(param.getString("fremail"));
                participantsInfos.setDanweilxr(param.getString("danweilxr"));
                participantsInfos.setDanweilxrlxdh(param.getString("danweilxrlxdh"));
                participantsInfos.setDanweilxrsfz(param.getString("danweilxrsfz"));
                participantsInfos.setLegalproperty(param.getString("legalproperty"));
                // 施工单位
                if ("3".equals(participantsInfos.getCorptype())) {
                    participantsInfos.setCbdanweitype(param.getString("cbdanweitype"));
                    participantsInfos.setJsfzr(param.getString("jsfzr"));
                    participantsInfos.setJsfzr_zc(param.getString("jsfzr_zc"));
                    participantsInfos.setJsfzr_phone(param.getString("jsfzr_phone"));
                    participantsInfos.setXmfzperson(param.getString("xmfzperson"));
                    participantsInfos.setXmfzrsafenum(param.getString("xmfzrsafenum"));
                    participantsInfos.setXmfzrphonenum(param.getString("xmfzrphonenum"));
                    participantsInfos.setQylxr(param.getString("qylxr"));
                    participantsInfos.setQylxdh(param.getString("qylxdh"));
                    participantsInfos.setGdlxr(param.getString("gdlxr"));
                    participantsInfos.setGdlxdh(param.getString("gdlxdh"));
                    participantsInfos.setFbsafenum(param.getString("fbsafenum"));
                    participantsInfos.setFbtime(param.getString("fbtime"));
                    participantsInfos.setFbscopeofcontract(param.getString("fbscopeofcontract"));
                    if (StringUtil.isNotBlank(param.getString("fbqysettime"))) {
                        participantsInfos.setFbqysettime(
                                EpointDateUtil.convertString2Date(param.getString("fbqysettime"), "yyyy-MM-dd"));
                    }
                    participantsInfos.setFbaqglry(param.getString("fbaqglry"));
                    participantsInfos.setFbaqglrysafenum(param.getString("fbaqglrysafenum"));
                }
                iparticipantsInfoService.update(participantsInfos);
                return JsonUtils.zwdtRestReturn("1", "修改成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "修改失败！", "");
            }
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
        }

    }

    /**
     * 根据rowguid删除单位信息
     */
    @RequestMapping(value = "/delParticipantInfo", method = RequestMethod.POST)
    public String delParticipantInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取delParticipantInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                iparticipantsInfoService.deleteByGuid(obj.getString("rowguid"));
                return JsonUtils.zwdtRestReturn("0", "！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======delParticipantInfo接口参数：params【" + params + "】=======");
            log.info("=======delParticipantInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "单体信息获取异常：" + e.getMessage(), "");
        }
    }

    /**
     * "选择单位"页面下的数据信息
     */
    @RequestMapping(value = "/getCompanyInfo", method = RequestMethod.POST)
    public String getCompanyInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getCompanyInfo接口=======");
            // 接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject param = jsonObject.getJSONObject("params");
                String corpType = param.getString("corpType");
                String corpname = param.getString("corpname");
                String corpcode = param.getString("corpcode");
                int pageIndex = Integer.parseInt(param.getString("pageIndex"));
                int pageSize = Integer.parseInt(param.getString("pageSize"));
                JSONObject json = new JSONObject();
                // 调用数字建设接口
                String churl = iConfigservice.getFrameConfigValue("AS_BLSP_DWURL");
                String chadd = churl + "/DanWeiListInterface/getDanWeiList";
                // 条件map
                JSONObject map = new JSONObject();
                if (StringUtil.isNotBlank(corpname)) {
                    map.put("DANWEINAME", corpname);// 单位名称
                }
                if (StringUtil.isNotBlank(corpType)) {
                    switch (corpcode) {
                        case "31":
                            map.put("DANWITYPE", "11");// 单位类型
                            break;
                        case "2":
                            map.put("DANWITYPE", "16");// sj单位类型
                            break;
                        case "1":
                            map.put("DANWITYPE", "15");// kc单位类型
                            break;
                        case "3":
                            map.put("DANWITYPE", "13");// sg单位类型
                            break;
                        case "4":
                            map.put("DANWITYPE", "14");// jl单位类型
                            break;
                        case "10":
                            map.put("DANWITYPE", "10");// jc单位类型
                            break;
                    }
                }
                if (StringUtil.isNotBlank(corpcode)) {
                    map.put("SOCIALCODE", corpcode); // 统一社会信用代码 可传空值
                }
                map.put("INDEX", pageIndex * pageSize);// 当前页码
                map.put("PAGESIZE", pageSize); // 每页展示条数
                Map<String, Object> paramsCondition = new HashMap<>();
                paramsCondition.put("params", map);
                String result = HttpUtil.doPost(chadd, paramsCondition);
                if (StringUtil.isBlank(result)) {
                    log.info("单位列表初始化失败");
                    return null;
                }
                Map<String, Object> mapresult = JsonUtil.jsonToMap(result);
                String isok = mapresult.get("isok").toString();
                List<JSONObject> tbcorpbasicList = new ArrayList<JSONObject>();
                JSONObject countJson = new JSONObject();
                if (isok != null && isok.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> chlist = (List<Map<String, Object>>) mapresult.get("list");
                    String countStr = mapresult.get("count").toString();
                    countJson.put("countValue", Integer.parseInt(countStr));
                    json.put("count", countJson);
                    if (chlist.size() > 0) {
                        for (int i = 0; i < chlist.size(); i++) {
                            Map<String, Object> jsonstr = (Map<String, Object>) chlist.get(i);
                            JSONObject tbcorpbasicJson = new JSONObject();
                            tbcorpbasicJson.put("corpcode", jsonstr.get("socialcode").toString());
                            tbcorpbasicJson.put("corpname", jsonstr.get("danweiname").toString());
                            tbcorpbasicJson.put("legalman", jsonstr.get("farenname").toString());
                            tbcorpbasicJson.put("linktel", jsonstr.get("danweiphone").toString());
                            tbcorpbasicJson.put("address", jsonstr.get("address").toString());
                            tbcorpbasicList.add(tbcorpbasicJson);
                        }
                    }
                }
                json.put("tbcorpbasicList", tbcorpbasicList);
                return json.toString();
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCompanyInfo接口参数：params【" + params + "】=======");
            log.info("=======getCompanyInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "单位信息获取异常：" + e.getMessage(), "");
        }
    }

    /**
     * "选择人员"页面下的数据信息
     */
    @RequestMapping(value = "/getPersonInfo", method = RequestMethod.POST)
    public String getPersonInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getPersonInfo接口=======");
            // 接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject param = jsonObject.getJSONObject("params");
                String personname = param.getString("personname");
                String corpcode = param.getString("corpcodestr");
                String idcard = param.getString("idcard");
                int pageIndex = Integer.parseInt(param.getString("pageIndex"));
                int pageSize = Integer.parseInt(param.getString("pageSize"));
                String churl = iConfigservice.getFrameConfigValue("AS_BLSP_DWURL");
                String chadd = churl + "/PersonListInterface/getPersonList";
                // 条件map
                JSONObject map = new JSONObject();
                map.put("PERSONTYPE", "");// 单位名称 可传空值
                map.put("SOCIALCODE", corpcode); // 统一社会信用代码 可传空值
                if (StringUtil.isNotBlank(personname)) {
                    map.put("NAME", personname);// 人员姓名
                }
                if (StringUtil.isNotBlank(idcard)) {
                    map.put("IDENTITYNUM", idcard);// 身份证号码
                }
                map.put("INDEX", pageIndex * pageSize);// 当前页码
                map.put("PAGESIZE", pageSize); // 每页展示条数
                Map<String, Object> paramsConditions = new HashMap<>();
                paramsConditions.put("params", map);
                String result = HttpUtil.doPost(chadd, paramsConditions);
                if (StringUtil.isBlank(result)) {
                    log.info("单位人员列表初始化失败");
                    return null;
                }
                Map<String, Object> mapresult = JsonUtil.jsonToMap(result);
                String isok = mapresult.get("isok").toString();
                // 结果json
                JSONObject json = new JSONObject();
                if (isok != null && isok.equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> chlist = (List<Map<String, Object>>) mapresult.get("list");
                    String count = mapresult.get("count").toString();

                    if (chlist.size() > 0) {
                        JSONObject countJson = new JSONObject();
                        countJson.put("countValue", Integer.parseInt(count));
                        json.put("count", countJson);
                        List<JSONObject> tbpersonbasicList = new ArrayList<JSONObject>();
                        for (int i = 0; i < chlist.size(); i++) {
                            Map<String, Object> jsonstr = (Map<String, Object>) chlist.get(i);
                            JSONObject tbpersonbasicJson = new JSONObject();
                            tbpersonbasicJson.put("idcard", jsonstr.get("identitynum").toString());
                            tbpersonbasicJson.put("personname", jsonstr.get("name").toString());
                            tbpersonbasicJson.put("mobile", jsonstr.get("mobilephone").toString());
                            tbpersonbasicJson.put("sbzg", jsonstr.get("zhicheng").toString());
                            tbpersonbasicJson.put("personcorp", jsonstr.get("danweiname").toString());
                            tbpersonbasicList.add(tbpersonbasicJson);
                        }
                        json.put("tbpersonbasicList", tbpersonbasicList);
                        return json.toString();
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该公司下无人员", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "选择人员失败，请重试！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPersonInfo接口参数：params【" + params + "】=======");
            log.info("=======getPersonInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "项目负责人信息获取异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取测绘材料接口
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getChMaterialList", method = RequestMethod.POST)
    public String getChMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 1.2、获取申报标识
                String subappguid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String churl = iConfigservice.getFrameConfigValue("AS_BLSP_CHURL");
                    if (StringUtil.isBlank(churl)) {
                        return JsonUtils.zwdtRestReturn("0", "未配置测绘地址！", "");
                    }
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    String chadd = churl + "lhchinter/getlhchresults";
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", "Epoint_SzjsSgst_**##0424");
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                        AuditRsItemBaseinfo parentAuditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                        map.put("projectno", parentAuditRsItemBaseinfo.getItemcode());
                    }
                    else {
                        map.put("projectno", auditRsItemBaseinfo.getItemcode());
                    }
                    // map.put("projectno", "");
                    String result = HttpUtil.doPost(chadd, map);
                    Map<String, Object> mapresult = JsonUtil.jsonToMap(result);
                    if (StringUtil.isBlank(mapresult.get("data"))) {
                        // 如果data为空，则未获取到数据
                        return JsonUtils.zwdtRestReturn("0", "测绘结果未生成！", "");
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> custom = (Map<String, Object>) mapresult.get("data");
                    if (custom.size() == 0) {
                        log.error("多测合一接口调用失败！报错信息：" + custom.get("error").toString());
                        log.error("多测合一接口数据：" + map);
                        return JsonUtils.zwdtRestReturn("0", "材料初始化失败，请重试！", "");
                    }
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.like("materialtype", "chcl%");
                    sql.eq("subappGuid", subappguid);
                    List<AuditSpIMaterial> listMaterial = iAuditSpIMaterial.getSpIMaterialByCondition(sql.getMap())
                            .getResult();
                    if (listMaterial.size() > 0) {
                        for (AuditSpIMaterial auditSpIMaterial : listMaterial) {
                            if (!"10".equals(auditSpIMaterial.getStatus())
                                    && !"15".equals(auditSpIMaterial.getStatus())) {
                                continue;
                            }
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> chlist = (List<Map<String, Object>>) custom
                                    .get(auditSpIMaterial.getMaterialtype().substring(4));
                            if (chlist != null && chlist.size() > 0) {
                                for (int i = 0; i < chlist.size(); i++) {
                                    String uploadUserDisplayName = chlist.get(i).get("uploaduserdisplayname")
                                            .toString();
                                    String attachFileName = chlist.get(i).get("materialname").toString();
                                    String attachUrl = chlist.get(i).get("attachurl").toString();
                                    if (StringUtil.isNotBlank(attachUrl)) {
                                        JSONObject file = filePathToInputStream(attachUrl);
                                        InputStream is = (InputStream) file.get("inputStream");
                                        String attachguid = UUID.randomUUID().toString();
                                        FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                        frameAttachInfo.setAttachGuid(attachguid);
                                        frameAttachInfo.setCliengGuid(auditSpIMaterial.getCliengguid());
                                        frameAttachInfo.setAttachFileName(attachFileName);
                                        // 长度
                                        frameAttachInfo.setAttachLength(file.getLong("length"));
                                        iAttachService.addAttach(frameAttachInfo, is);
                                        // 修改材料状态
                                        String materialStatus = auditSpIMaterial.getStatus();
                                        if (StringUtil.isBlank(materialStatus)
                                                || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == Integer
                                                        .parseInt(materialStatus)) {
                                            materialStatus = String
                                                    .valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                        }
                                        else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer
                                                .parseInt(materialStatus)) {
                                            materialStatus = String
                                                    .valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                                        }
                                        auditSpIMaterial.setStatus(materialStatus);
                                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                    }
                                }
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", "材料下载成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取登录用户失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getChMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getChMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "材料下载失败", "");
        }
    }

    /**
     * 获取多评材料接口
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getDpMaterialList", method = RequestMethod.POST)
    public String getDpMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getDpMaterialList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 1.2、获取申报标识
                String subappguid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String dpurl = iConfigservice.getFrameConfigValue("AS_BLSP_DPURL");
                    if (StringUtil.isBlank(dpurl)) {
                        return JsonUtils.zwdtRestReturn("0", "未配置多评地址！", "");
                    }
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                    String dpadd = "http://192.168.100.46:8001/dphygl" + "/rest/DphyProject/getMultiResultList";
                    JSONObject map = new JSONObject();
                    JSONObject param = new JSONObject();
                    map.put("token", "Epoint_WebSerivce_**##0601");
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getParentid())) {
                        AuditRsItemBaseinfo parentAuditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(auditRsItemBaseinfo.getParentid()).getResult();
                        param.put("projectno", parentAuditRsItemBaseinfo.getItemcode());
                    }
                    else {
                        param.put("projectno", auditRsItemBaseinfo.getItemcode());
                    }

                    List<AuditSpIMaterial> listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappguid)
                            .getResult();
                    List<String> materialTypeList = new ArrayList<String>();
                    for (AuditSpIMaterial auditSpIMaterial : listMaterial) {
                        String materialType = auditSpIMaterial.getMaterialtype();
                        if (StringUtil.isNotBlank(materialType) && materialType.length() > 4
                                && MATERIAL_TYPE_DPCL.equals(materialType.substring(0, 4))
                                && (String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER)
                                        .equals(auditSpIMaterial.getStatus())
                                        || String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT)
                                                .equals(auditSpIMaterial.getStatus()))) {
                            // 去除dpcl的值，即二级代码项的值。也就是taskcode
                            materialTypeList.add(materialType.substring(4));
                        }
                    }
                    if (materialTypeList.size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                        return "材料已全部获取，请勿重复点击！";
                    }
                    if (materialTypeList.size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                        return JsonUtils.zwdtRestReturn("0", "没有需要多评的材料，请联系管理员", "");
                    }
                    param.put("taskcode", StringUtil.join(materialTypeList, ";"));
                    map.put("params", param);
                    // 去重
                    @SuppressWarnings({"unchecked", "rawtypes" })
                    HashSet<String> h = new HashSet(materialTypeList);
                    materialTypeList.clear();
                    materialTypeList.addAll(h);
                    String result = HttpUtil.doPostJson(dpadd, map.toString());
                    JSONObject resultjson = JSONObject.parseObject(result);
                    if (resultjson == null) {
                        return JsonUtils.zwdtRestReturn("0", "多评结果未生成！", "");
                    }
                    if (ZwfwConstant.CONSTANT_STR_ZERO.equals(resultjson.getJSONObject("custom").getString("code"))) {
                        // 如果data为空，则未获取到数据
                        return JsonUtils.zwdtRestReturn("0", resultjson.getJSONObject("custom").getString("text"), "");
                    }
                    else {
                        JSONObject resultMap = resultjson.getJSONObject("custom").getJSONObject("resultList");
                        for (String materialType : materialTypeList) {
                            if (resultMap.containsKey(materialType)) {
                                JSONArray taskcode = resultMap.getJSONArray(materialType);
                                String type = MATERIAL_TYPE_DPCL + materialType;
                                // 根据subappGuid 和 materialtype 确定唯一材料。 多评的时候可以用。
                                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                                sqlConditionUtil.eq("subappguid", subappguid);
                                sqlConditionUtil.eq("materialtype", type);
                                List<AuditSpIMaterial> materialList = iAuditSpIMaterial
                                        .getSpIMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                                if (materialList == null || materialList.size() == 0 || taskcode.size() == 0) {
                                    continue;
                                }
                                // 为了防止真的配了两个相同类型的材料，对list中的所有节点做操作
                                for (AuditSpIMaterial auditSpIMaterial : materialList) {
                                    for (int i = 0; i < taskcode.size(); i++) {
                                        String cliengguid = auditSpIMaterial.getCliengguid();
                                        String attachurl = taskcode.getJSONObject(i).getString("attachurl");
                                        String attachname = taskcode.getJSONObject(i).getString("attachname");
                                        if (StringUtil.isNotBlank(attachurl)) {
                                            JSONObject file = filePathToInputStream(attachurl);
                                            InputStream is = (InputStream) file.get("inputStream");
                                            String attachguid = UUID.randomUUID().toString();
                                            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                            frameAttachInfo.setAttachGuid(attachguid);
                                            frameAttachInfo.setCliengGuid(cliengguid);
                                            frameAttachInfo.setAttachFileName(attachname);
                                            // 长度
                                            frameAttachInfo.setAttachLength(file.getLong("length"));
                                            iAttachService.addAttach(frameAttachInfo, is);
                                            // 修改材料状态
                                            SqlConditionUtil sqlConditionUtil2 = new SqlConditionUtil();
                                            sqlConditionUtil2.eq("subappguid", subappguid);
                                            sqlConditionUtil2.eq("cliengguid", cliengguid);
                                            List<AuditSpIMaterial> materialList1 = iAuditSpIMaterial
                                                    .getSpIMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                                            if (materialList1 != null && materialList1.size() > 0) {
                                                for (AuditSpIMaterial auditSpIMaterial1 : materialList1) {
                                                    String materialStatus = auditSpIMaterial1.getStatus();
                                                    if (StringUtil.isBlank(materialStatus)
                                                            || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == Integer
                                                                    .parseInt(materialStatus)) {
                                                        materialStatus = String.valueOf(
                                                                ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                                    }
                                                    else if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer
                                                            .parseInt(materialStatus)) {
                                                        materialStatus = String.valueOf(
                                                                ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                                                    }
                                                    auditSpIMaterial1.setStatus(materialStatus);
                                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", "材料下载成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取登录用户失败", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDpMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getDpMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "材料下载失败", "");
        }
    }

    /**
     * 获取材料标准
     */
    @RequestMapping(value = "/getStandard", method = RequestMethod.POST)
    public String getStandard(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getStandard接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject standard = new JSONObject();
                String taskMaterialGuid = obj.getString("taskmaterialrowguid");
                AuditTaskMaterial taskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskMaterialGuid)
                        .getResult();
                if (taskMaterial != null) {
                    standard.put("standard",
                            StringUtil.isBlank(taskMaterial.getStandard()) ? "无" : taskMaterial.getStandard());
                    standard.put("materialname", taskMaterial.getMaterialname());
                }
                dataJson.put("standard", standard);
                return JsonUtils.zwdtRestReturn("1", "获取材料标准成功！", dataJson.toJSONString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getStandard接口参数：params【" + params + "】=======");
            log.info("=======getStandard接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "材料标准获取异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getItemRestlts", method = RequestMethod.POST)
    public String getItemRestlts(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemRestlts接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取子申报标识
                String subAppGuid = obj.getString("subappguid");
                // 1.2、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // 2、查询事项实例
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                // 查询主题信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();
                List<JSONObject> resultlist = new ArrayList<JSONObject>();
                if (auditSpITasks != null && !auditSpITasks.isEmpty() && auditSpBusiness != null) {
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        if (StringUtil.isBlank(auditSpITask.getProjectguid())) {
                            continue;
                        }
                        // 2.1、通过guid查询办件结果
                        AuditProject auditProject = iAuditProject
                                .getAuditProjectByRowGuid(auditSpITask.getProjectguid(), "").getResult();
                        if (auditProject != null) {
                            if (auditProject.getStatus() == 90) {
                                if (StringUtils.isNotBlank(auditProject.getCertrowguid())) {
                                    if (auditProject.getCertrowguid().contains(";")) {
                                        String[] certRowguids = auditProject.getCertrowguid().split(";");
                                        for (String certRowguid : certRowguids) {
                                            // 3、获取证照实例
                                            CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(certRowguid);
                                            if (certInfo != null) {
                                                // 3.1、获取结果对应的附件
                                                List<JSONObject> attachList = iCertAttachExternal.getAttachList(
                                                        certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                                if (attachList != null && attachList.size() > 0) {
                                                    JSONObject resultJson = new JSONObject();
                                                    resultJson.put("taskname", auditSpITask.getTaskname());
                                                    List<JSONObject> attachLists = new ArrayList<JSONObject>();
                                                    for (JSONObject attachJson : attachList) {
                                                        JSONObject attachJsons = new JSONObject();
                                                        attachJsons.put("attachtype", "【证照】");
                                                        attachJsons.put("resultattachname",
                                                                attachJson.get("attachname"));
                                                        attachJsons.put("resultattachurl",
                                                                WebUtil.getRequestCompleteUrl(request)
                                                                        + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                        + attachJson.getString("attachguid"));// 附件图标
                                                        attachLists.add(attachJsons);
                                                    }
                                                    resultJson.put("attachlist", attachLists);
                                                    resultlist.add(resultJson);
                                                }
                                            }
                                        }

                                    }
                                    else {
                                        // 3、获取证照实例
                                        CertInfo certInfo = iCertInfoExternal
                                                .getCertInfoByRowguid(auditProject.getCertrowguid());
                                        if (certInfo != null) {
                                            // 3.1、获取结果对应的附件
                                            List<JSONObject> attachList = iCertAttachExternal.getAttachList(
                                                    certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                            if (attachList != null && attachList.size() > 0) {
                                                JSONObject resultJson = new JSONObject();
                                                resultJson.put("taskname", auditSpITask.getTaskname());
                                                List<JSONObject> attachLists = new ArrayList<JSONObject>();
                                                for (JSONObject attachJson : attachList) {
                                                    JSONObject attachJsons = new JSONObject();
                                                    attachJsons.put("attachtype", "【证照】");
                                                    attachJsons.put("resultattachname", attachJson.get("attachname"));
                                                    attachJsons.put("resultattachurl",
                                                            WebUtil.getRequestCompleteUrl(request)
                                                                    + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                    + attachJson.getString("attachguid"));// 附件图标
                                                    attachLists.add(attachJsons);
                                                }
                                                resultJson.put("attachlist", attachLists);
                                                resultlist.add(resultJson);
                                            }
                                        }
                                    }
                                }
                                else {
                                    List<FrameAttachInfo> listattach = iAttachService
                                            .getAttachInfoListByGuid(auditProject.getRowguid());
                                    if (listattach != null && !listattach.isEmpty()) {
                                        JSONObject resultJson = new JSONObject();
                                        resultJson.put("taskname", auditSpITask.getTaskname());
                                        AuditTaskResult AuditTaskResult = iAuditTaskResult
                                                .getAuditResultByTaskGuid(auditSpITask.getTaskguid(), false)
                                                .getResult();
                                        String attachType = "";
                                        if (AuditTaskResult != null) {
                                            if (StringUtil.isNotBlank(AuditTaskResult.getResulttype())) {
                                                attachType = iCodeItemsService.getItemTextByCodeName("审批结果类型",
                                                        AuditTaskResult.getResulttype().toString());
                                            }
                                        }
                                        List<JSONObject> attachList = new ArrayList<JSONObject>();
                                        // 查询具体事项
                                        String flag = "0";
                                        AuditTask auditTask = iAuditTask
                                                .getAuditTaskByGuid(auditSpITask.getTaskguid(), true).getResult();
                                        if (auditTask != null) {
                                            String taskname = iCodeItemsService.getItemTextByCodeName("消防事项",
                                                    auditTask.getItem_id());
                                            if (StringUtil.isNotBlank(taskname)) {
                                                flag = "1";
                                            }
                                        }
                                        if ("1".equals(flag)) {
                                            // 消防事项特殊处理
                                            // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                                            for (FrameAttachInfo attachInfo : listattach) {
                                                if (StringUtil.isNotBlank(attachInfo.getStr("xffjurl"))) {
                                                    JSONObject attachJsons = new JSONObject();
                                                    attachJsons.put("attachtype", "【" + attachType + "】");
                                                    attachJsons.put("resultattachname", attachInfo.getAttachFileName());
                                                    attachJsons.put("resultattachurl", attachInfo.getStr("xffjurl"));// 附件图标
                                                    attachList.add(attachJsons);
                                                }
                                            }
                                        }
                                        else {
                                            // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                                            for (FrameAttachInfo attachInfo : listattach) {
                                                JSONObject attachJsons = new JSONObject();
                                                attachJsons.put("attachtype", "【" + attachType + "】");
                                                attachJsons.put("resultattachname", attachInfo.getAttachFileName());
                                                attachJsons.put("resultattachurl",
                                                        WebUtil.getRequestCompleteUrl(request)
                                                                + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                + attachInfo.getAttachGuid() + "&filename="
                                                                + attachInfo.getAttachFileName());// 附件图标
                                                attachList.add(attachJsons);
                                            }
                                        }
                                        resultJson.put("attachlist", attachList);
                                        resultlist.add(resultJson);
                                    }
                                }
                            }
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("resultlist", resultlist);
                log.info("=======结束调用getItemRestlts接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "查询失败" + e.getMessage(), "");
        }
    }

    /**
     * 获取消防默认值
     */
    @RequestMapping(value = "/getXfCommonData", method = RequestMethod.POST)
    public String getXfCommonData(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getXfCommonData接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject custom = new JSONObject();
                String biguid = obj.getString("biguid");
                String subappGuid = obj.getString("subappguid");
                String phaseguid = obj.getString("phaseguid");
                AuditSpInstance auditspinstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
                AuditSpPhase auditspphase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                if (auditspinstance == null) {
                    throw new RuntimeException("未查询到申报数据!");
                }
                AuditRsItemBaseinfo auditrsitembaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditspinstance.getYewuguid()).getResult();

                Record phaseform = null;
                switch (auditspphase.getPhaseId()) {
                    case ZwfwConstant.CONSTANT_STR_ONE:
                        phaseform = iauditspsplxydghxkservice.findAuditSpSpLxydghxkBysubappguid(subappGuid);
                        break;
                    case ZwfwConstant.CONSTANT_STR_TWO:
                        phaseform = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappGuid);
                        break;
                    case ZwfwConstant.CONSTANT_STR_THREE:
                        phaseform = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
                        break;
                    case "4":
                        phaseform = iauditspspjgysservice.findAuditSpSpJgysBySubappGuid(subappGuid);
                        break;
                    default:
                        break;
                }
                if (phaseform != null) {
                    auditrsitembaseinfo.putAll(phaseform);
                }
                JnBeanUtil jnbeanutil = new JnBeanUtil("xf.properties");
                jnbeanutil.dataPut(auditrsitembaseinfo);
                // 查询建设单位
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("corptype", "31");
                sql.eq("itemguid", auditspinstance.getYewuguid());
                List<ParticipantsInfo> jsinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                if (jsinfos.size() > 0) {
                    jnbeanutil.dataPut(jsinfos.get(0));
                }

                // 获取投资在线监管平台代码
                AuditRsItemBaseinfo parentAuditrsitembaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditrsitembaseinfo.getParentid()).getResult();
                if (parentAuditrsitembaseinfo != null && !parentAuditrsitembaseinfo.isEmpty()) {
                    custom.put("itemcode", parentAuditrsitembaseinfo.getItemcode());
                }
                else {
                    custom.put("itemcode", auditrsitembaseinfo.getItemcode());
                }
                custom.put("tyshxydm", auditrsitembaseinfo.getItemlegalcertnum());




                Record commondata = jnbeanutil.coverBean(new Record(), true);

                //增加获取用水企业报装，供热企业报装，建筑工程施工许可证核发 表单数据渲染
                if (jsinfos.size() > 0) {
                    //建设单位法人姓名
                    //建设单位统一信用代码
                   commondata.put("field8", jsinfos.get(0).getLegal());
                   commondata.put("field13",jsinfos.get(0).getItemlegalcertnum());
                    //建设单位名称
                   commondata.put("field3", jsinfos.get(0).getCorpname());
                    //建设单位法人身份证号
                   commondata.put("field4", jsinfos.get(0).getLegalpersonicardnum());
                }
                //单位联系电话
               commondata.put("field9", auditrsitembaseinfo.getContractphone());
                //项目地址
               commondata.put("field5", auditrsitembaseinfo.getConstructionsite());

//               commondata.put("field5", "单位联系电话");
                if (jsinfos.size() > 0) {
                    //建设单位名称
                   commondata.put("field2", jsinfos.get(0).getCorpname());
                    //建设单位法人身份证号
                   commondata.put("field6", jsinfos.get(0).getLegalpersonicardnum());
                }
                //项目地址
               commondata.put("field10", auditrsitembaseinfo.getConstructionsite());
//               commondata.put("field13", auditrsitembaseinfo.getItemlegalcertnum());

                if (jsinfos.size() > 0) {
                    //建设单位名称
                   commondata.put("jsdw", jsinfos.get(0).getLegalpersonicardnum());
                    //项目负责人
                   commondata.put("jsdwxmfzr", jsinfos.get(0).getXmfzr());
                }
                // 查询设计单位
                sql.clear();
                sql.eq("corptype", "2");
                sql.eq("itemguid", auditspinstance.getYewuguid());
                List<ParticipantsInfo> sjinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                if (sjinfos.size() > 0) {
                    //设计单位名称
                   commondata.put("sjdw",sjinfos.get(0).getCorpname());
                    //设计单位项目负责人
                   commondata.put("sjdwxmfzr", sjinfos.get(0).getXmfzr());
                }

                // 查询勘察单位
                sql.clear();
                sql.eq("corptype", "1");
                sql.eq("itemguid", auditspinstance.getYewuguid());
                List<ParticipantsInfo> kcinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                if (kcinfos.size() > 0) {
                    //勘察单位名称
                   commondata.put("kcdw", kcinfos.get(0).getCorpname());
                    //勘察负责人
                   commondata.put("kcdwxmfzr", kcinfos.get(0).getXmfzr());
                }

                // 查询监理单位
                sql.clear();
                sql.eq("corptype", "4");
                sql.eq("itemguid", auditspinstance.getYewuguid());
                List<ParticipantsInfo> jlinfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                if (jlinfos.size() > 0) {
                    //监理单位名称
                   commondata.put("jldw", jlinfos.get(0).getCorpname());
                }

                // 查询施工单位
                sql.clear();
                sql.eq("corptype", "3");
                sql.eq("itemguid", auditspinstance.getYewuguid());
                List<ParticipantsInfo> sginfos = iParticipantsInfo.getParticipantsInfoListByCondition(sql.getMap());
                if (sginfos.size() > 0) {
                    //施工单位名称
                   commondata.put("sgdw", sginfos.get(0).getCorpname());
                    //施工单位负责人
                   commondata.put("sgdwxmfzr", sginfos.get(0).getXmfzr());
                }

                //合同竣工日期
               commondata.put("htgqend", StringUtil.isBlank(auditrsitembaseinfo.getItemfinishdate()) ? ""
                        : EpointDateUtil.convertDate2String(auditrsitembaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                //建设规模及内容
               commondata.put("jsgm", auditrsitembaseinfo.getConstructionscaleanddesc());
                //不动产单元号
               commondata.put("bdcdyh", auditrsitembaseinfo.get("bdcdanyuanhao"));
                ////项目地址
               commondata.put("jsxmdz", auditrsitembaseinfo.getConstructionsite());
               commondata.put("xmmc", auditrsitembaseinfo.getItemname());
                //合同价格
               commondata.put("htjg", auditrsitembaseinfo.get("htjg"));

                //合同工期
               commondata.put("htgq", auditrsitembaseinfo.get("htgq"));


                custom.put("commondata", commondata);

                if (StringUtil.isNotBlank(auditspphase.getStr("formid"))) {


                    custom.put("formid", auditspphase.getStr("formid"));
                    custom.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                    custom.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                }
                return JsonUtils.zwdtRestReturn("1", "获取消防默认值成功！", custom);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getStandard接口参数：params【" + params + "】=======");
            log.info("=======getStandard接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取消防默认值异常：" + e.getMessage(), "");
        }
    }

    // 网络文件转InputStream
    public static final JSONObject filePathToInputStream(String filePath) {
        JSONObject result = new JSONObject();
        InputStream is = null;
        String fileName = null;
        Long length = null;
        try {
            if (StringUtil.isNotBlank(filePath)) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "");
                Request request = new Request.Builder().url(filePath).post(body).addHeader("Accept", "*/*")
                        .addHeader("Cache-Control", "no-cache").addHeader("Host", "localhost:8080")
                        .addHeader("accept-encoding", "gzip, deflate").addHeader("content-length", "")
                        .addHeader("Connection", "keep-alive").addHeader("cache-control", "no-cache").build();
                try {
                    Response response = client.newCall(request).execute();
                    is = response.body().byteStream();
                    length = response.body().contentLength();
                    fileName = response.header("Content-Disposition");
                    fileName = URLDecoder.decode(
                            fileName.substring(fileName.indexOf("filename") + 10, fileName.length() - 1), "UTF-8");

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            result.put("fileName", fileName);
            result.put("inputStream", is);
            result.put("length", length);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**********************************************
     * 咨询相关接口结束
     **********************************************/

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 判断当前办理的事项是否属于济宁市及其特定区县
     *
     * @param areaCode
     *            地区代码
     * @return 是否属于指定区域
     */
    public boolean isJiningArea(String areaCode) {
        // 检查 areaCode 是否为 null 或空字符串
        if (StringUtil.isBlank(areaCode)) {
            return false;
        }
        String jiningAreaCodes = iConfigService.getFrameConfigValue("gygsdjareacode");
        if (StringUtil.isNotBlank(jiningAreaCodes) && jiningAreaCodes.contains(areaCode)) {
            return true;
        }
        return false;
    }

}
