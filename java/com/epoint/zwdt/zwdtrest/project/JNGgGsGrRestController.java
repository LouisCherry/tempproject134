package com.epoint.zwdt.zwdtrest.project;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
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
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.auditspitaskcorp.api.IAuditSpITaskCorpService;
import com.epoint.xmz.task.commonapi.inter.ITaskCommonService;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.zwdt.zwdtrest.project.utils.GongYongShuiWuUtil;
import com.epoint.zwdt.zwdtrest.project.utils.JNConstant;
import com.epoint.zwdt.zwdtrest.project.utils.ZhongShanShuiWuUtil;
import com.epoint.zwdt.zwdtrest.yyyz.api.IDaTing;

/**
 * 接收三方办件信息
 */
@RestController
@RequestMapping("/jngggsgrrest")
public class JNGgGsGrRestController {
    /**
     * 日志对象
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 项目信息API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IDaTing daTingService;

    @Autowired
    private IAuditSpSpLxydghxkService iAuditSpSpLxydghxkService;

    @Autowired
    private IAuditSpBusiness iAuditSpBusiness ;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;

    @Autowired
    private IAuditSpITask iAuditSpITask;

    @Autowired
    private ITaskCommonService taskCommonService;

    @Autowired
    private IAuditSpITaskCorpService iAuditSpITaskCorpService;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IHandleConfig handleConfig;

    @Autowired
    private IConfigService iConfigService;


    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditTaskCase iAuditTaskCase;

    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;


    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;

    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IParticipantsInfoService iParticipantsInfo;

    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;

    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ICxBusService iCxBusService;

    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;

    @Autowired
    private IJnProjectService iJnProjectService;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    /**
     * 企业申报API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    /**
     * 获取项目列表
     * @return
     */
    @RequestMapping(value = "/getitemlistbycondition", method = RequestMethod.POST)
    public String getitemlistbycondition(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getitemlistbycondition接口=======");
            log.info("=======getitemlistbycondition接口参数：params【" + params + "】=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                int currentpage = obj.getIntValue("currentpage");
                String areacode = obj.getString("areacode");
                int pageSize = obj.getIntValue("pagesize");
                String itemname = obj.getString("itemname");
                String itemcode = obj.getString("itemcode");
                currentpage = 0;
                pageSize = 5000;
                pageSize = StringUtil.isNotBlank(ConfigUtil.getConfigValue("duijie", "pagesize"))?
                        Integer.parseInt(ConfigUtil.getConfigValue("duijie", "pagesize")):pageSize;
                JSONObject dataJson = new JSONObject();
                JSONArray itemlist = new JSONArray();
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.setSelectFields("itemcode,itemname");
                if(StringUtil.isNotBlank(areacode)){
                    sqlConditionUtil.eq("BelongXiaQuCode", areacode);
                }
                if(StringUtil.isNotBlank(itemname)){
                    sqlConditionUtil.like("itemname", itemname);
                }
                if(StringUtil.isNotBlank(itemcode)){
                    sqlConditionUtil.eq("itemcode", itemcode);
                }
                PageData<AuditRsItemBaseinfo> pages = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, sqlConditionUtil.getMap(),
                                currentpage * pageSize,
                                pageSize, "", "")
                        .getResult();
                List<AuditRsItemBaseinfo> auditRsItemBaseinfoList = pages.getList();
                if(auditRsItemBaseinfoList != null && auditRsItemBaseinfoList.size()>0){
                    for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfoList){
                        JSONObject itemJson = new JSONObject();
                        itemJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        itemJson.put("itemname", auditRsItemBaseinfo.getItemname());
                        itemlist.add(itemJson);
                    }
                }
                dataJson.put("totalcount", auditRsItemBaseinfoList.size());
                dataJson.put("itemlist", itemlist);
                return JsonUtils.zwdtRestReturn("1", "获取项目列表成功！", dataJson.toString());
            } else {
                log.info("=======结束调用getitemlistbycondition接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getitemlistbycondition接口参数：params【" + params + "】=======");
            log.info("=======getitemlistbycondition异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 办件状态更新
     * 根据接口中根据操作类型更新办件状态audit_project.status详见代码项,并生成办件流程信息办件审批操作表（AUDIT_PROJECT_OPERATION） 然后进行办件环节操作信息上报
     */
    @RequestMapping(value = "/updateprojectstatus", method = RequestMethod.POST)
    public String updateprojectstatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用updateprojectstatus接口=======");
            log.info("=======updateprojectstatus接口参数：params【" + params + "】=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String xmdm = obj.getString("xmdm");
                String flowsn = obj.getString("flowsn");
                String operatetype = obj.getString("operatetype");
                String operateuser = obj.getString("operateuser");
                String operatedate = obj.getString("operatedate");
                JSONObject dataJson = new JSONObject();
                if(StringUtil.isBlank(operatetype)){
                    return JsonUtils.zwdtRestReturn("0", "办件状态更新失败,参数operatetype为空", "");
                }
                if(StringUtil.isBlank(flowsn)){
                    return JsonUtils.zwdtRestReturn("0", "办件状态更新失败,参数flowsn为空", "");
                }
                if(StringUtil.isBlank(operateuser)){
                    return JsonUtils.zwdtRestReturn("0", "办件状态更新失败,参数operateuser为空", "");
                }
                AuditProject auditProject = daTingService.getAuditProjectByFlowsn(flowsn);
                if (StringUtil.isBlank(auditProject)) {
                    return JsonUtils.zwdtRestReturn("0", "办件状态更新失败,根据传入flowsn未查询到办件", "");
                }
                // 1 已接件
                if (JNConstant.YJJ.equals(operatetype)) {
                    operatetype = "jj";
                    // 生成办件操作记录
                    generateAuditProjectOperate(auditProject, operatetype,operateuser,"1");
                    sendMQ(auditProject, operatetype);
                    // 更新办件状态
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                }
                // 2 已受理
                else if (JNConstant.YSL.equals(operatetype)) {
                    operatetype = "accept";
                    // 生成办件操作记录
                    generateAuditProjectOperate(auditProject, operatetype,operateuser,"1");
                    sendMQ(auditProject, operatetype);
                    // 更新办件状态
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);
                }
                // 4 不受理
                else if (JNConstant.BSL.equals(operatetype)) {
                    operatetype = "notaccept";
                    // 生成办件操作记录
                    generateAuditProjectOperate(auditProject, operatetype,operateuser,"0");
                    sendMQ(auditProject, operatetype);
                    // 更新办件状态
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_BYSL);
                }
                // 6 补正（开始）
                else if (JNConstant.BZ.equals(operatetype)) {

                }
                // 8 部门开始办理
                else if (JNConstant.BMKSBL.equals(operatetype)){
                    operatetype = "bmksbl";
                    // 生成办件操作记录
                    generateAuditProjectOperate(auditProject, operatetype, operateuser,"1");
                    sendMQ(auditProject, operatetype);
                    // 更新办件状态
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_SPTG);
                }
                // 9 特别程序（开始)
                else if (JNConstant.TBCX_START.equals(operatetype)){
                }
                // 10 特别程序（结束)
                else if (JNConstant.TBCX_END.equals(operatetype)){
                }
                // 11 办件通过
                else if (JNConstant.BJ_PASS.equals(operatetype)){
                    operatetype = "sendresult";
                    // 生成办件操作记录
                    generateAuditProjectOperate(auditProject, operatetype,operateuser,"1");
                    sendMQ(auditProject, operatetype);
                    // 更新办件状态
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                }
                // 更新办件状态
                iAuditProject.updateProject(auditProject);
                return JsonUtils.zwdtRestReturn("1", "办件状态更新成功！", dataJson.toString());
            } else {
                log.info("=======结束调用updateprojectstatus接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======updateprojectstatus接口参数：params【" + params + "】=======");
            log.info("=======updateprojectstatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件状态更新失败：" + e.getMessage(), "");
        }
    }

    /**
     * 供水供电提交办件申报信息
     */
    @RequestMapping(value = "/submitgsgdproject", method = RequestMethod.POST)
    public String submitgsgdproject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitgsgdproject接口=======");
            log.info("=======submitgsgdproject接口参数：params【" + params + "】=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String areacode = obj.getString("areacode");// 行政区划代码
                String itemcode = obj.getString("itemcode");// 项目编码
                JSONObject gsinfo = obj.getJSONObject("gsinfo");// 供水报装申报信息
                JSONObject grinfo = obj.getJSONObject("grinfo");// 供热报装申报信息
                JSONArray materiallist = obj.getJSONArray("materiallist");// 材料列表
                if(StringUtil.isBlank(areacode)){
                    return JsonUtils.zwdtRestReturn("0", "提交办件申报信息失败,参数areacode为空", "");
                }
                JSONObject dataJson = new JSONObject();
                // TODO
                //根据itemcode查询项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByItemcode(itemcode).getResult();
                if(auditRsItemBaseinfo == null){
                    return JsonUtils.zwdtRestReturn("0", "提交办件申报信息失败，未查询到项目", "");
                }
                String itemname= auditRsItemBaseinfo.getItemname() ;

                String applyerguid = "";
                String applyername = "";

                // 获取 申请人信息
                String itemlegalcertnum = auditRsItemBaseinfo.getItemlegalcertnum();
                String accountguid = "";
                log.info("=====itemlegalcertnum=====:"+itemlegalcertnum);
                if(StringUtil.isNotBlank(itemlegalcertnum)){
                    AuditOnlineCompany auditOnlineCompany = iJnProjectService.getOnlineCompany(itemlegalcertnum);
                    if(auditOnlineCompany != null){
                        log.info("=====auditOnlineCompany=====:"+auditOnlineCompany);
                        accountguid = auditOnlineCompany.getAccountguid();
                    }
                    else{
                        AuditOnlineIndividual auditOnlineIndividual = iJnProjectService.getOnlineIndividualByCertnum(itemlegalcertnum);
                        if(auditOnlineIndividual != null){
                            log.info("=====auditOnlineIndividual=====:"+auditOnlineIndividual);
                            accountguid = auditOnlineIndividual.getAccountguid();
                        }
                    }
                }
                log.info("=====accountguid=====:"+accountguid);
                AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountguid).getResult();
                log.info("=====auditOnlineRegister=====:"+auditOnlineRegister);
                if(auditOnlineRegister == null){
                    String field = "creditcode";
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                        field = "organcode";
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByOneField(field, auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                    log.info("=====auditRsCompanyBaseinfo=====:"+auditRsCompanyBaseinfo);
                    if (auditRsCompanyBaseinfo != null) {
                        applyerguid = auditRsCompanyBaseinfo.getCompanyid();
                        applyername = auditRsCompanyBaseinfo.getOrganname();
                    }
                }
                else{
                    applyerguid = auditOnlineRegister.getAccountguid();
                    applyername = auditOnlineRegister.getUsername();
                }
                if(StringUtil.isBlank(applyerguid)&&StringUtil.isBlank(applyername)){
                    return JsonUtils.zwdtRestReturn("0", "提交失败，未找到applyerguid和applyername", "");
                }

                String uuid = UUID.randomUUID().toString();  // 生成UUID
                String sixDigits = uuid.replace("-", "").substring(0, 6);  // 移除连字符并取前6位

                //生成子项目编码和名称
                String subitemname = itemname +"-"+sixDigits ;
                String subitemcode = itemcode +"-"+sixDigits;


                //获取businessguid
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("areacode", areacode);
                sqlConditionUtil.eq("businessname", "水电气暖网联合报装一件事");
                List<AuditSpBusiness>  auditSpBusinessList = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();
                String businessguid = "";
                AuditSpBusiness auditSpBusiness=new AuditSpBusiness();
                if (auditSpBusinessList != null && auditSpBusinessList.size() > 0){
                    auditSpBusiness = auditSpBusinessList.get(0);
                    businessguid= auditSpBusiness.getRowguid();
                }

                // 获取 阶段默认第一阶段“立项用地规划许可”
                log.info("=====auditSpBusiness.getRowguid()=====:"+auditSpBusiness.getRowguid());
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase
                            .getSpPaseByBusinedssguid(auditSpBusiness.getRowguid()).getResult();
                AuditSpPhase auditSpPhase = null;
                if(auditSpPhases != null && auditSpPhases.size() > 0){
                    auditSpPhase = auditSpPhases.get(0);
                }
                if(auditSpPhase == null){
                     return JsonUtils.zwdtRestReturn("0", "提交办件申报信息失败,auditSpPhase为空,未找到阶段配置", "");
                }

                //生成audit_sp_instance，该表的rowguid为biguid
                String biguid = iAuditSpInstance.addBusinessInstance(businessguid,auditRsItemBaseinfo.getRowguid(),auditOnlineRegister.getAccountguid(),auditOnlineRegister.getUsername(),null,itemname,areacode,"2").getResult();
                log.info("=====biguid=====:"+biguid);
                //子项目除个别字段不同其余与主项目相同，于是直接拿主项目的实体修改部分值做子项
                AuditRsItemBaseinfo subitem =auditRsItemBaseinfo;
                subitem.setOperatedate(new Date());
                subitem.setParentid(auditRsItemBaseinfo.getRowguid());
                subitem.setRowguid(UUID.randomUUID().toString());
                subitem.setItemcode(subitemcode);
                subitem.setItemname(subitemname);
                subitem.setBiguid(biguid);
                subitem.setDraft("1");
//                auditRsItemBaseinfo.set("xmzb", xmzb);

                // 子项目上报字段要设置为0
                subitem.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
            //    setItemInfo(auditRsItemBaseinfo, obj);
                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(subitem);

                AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                auditSpISubapp.setCreatedate(new Date());
                auditSpISubapp.setRowguid(UUID.randomUUID().toString());
                auditSpISubapp.setOperatedate(new Date());
                auditSpISubapp.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
                auditSpISubapp.setApplyerguid(applyerguid);
                auditSpISubapp.setApplyername(applyername);
                auditSpISubapp.setPhaseguid(auditSpPhase.getRowguid());
                auditSpISubapp.setApplyerway("10");
                auditSpISubapp.setBiguid(biguid);
                auditSpISubapp.setBusinessguid(businessguid);
                auditSpISubapp.setYewuguid(subitem.getRowguid());
                auditSpISubapp.set("yjsbusinessguid", businessguid);
                auditSpISubapp.set("yjsname", auditSpBusiness.getBusinessname());
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_WWDYS);
                auditSpISubapp.setSubappname("水电气暖网联合报装一件事");
                iAuditSpISubapp.addSubapp(auditSpISubapp);

                if(gsinfo != null && !gsinfo.isEmpty()){
                    //todo 供水报装申报信息
                    String field1= gsinfo.getString("field1");
                    field1 = StringUtil.isNotBlank(field1)?URLDecoder.decode(field1, "UTF-8"):"";
                    String field2= gsinfo.getString("field2");
                    field2 = StringUtil.isNotBlank(field2)?URLDecoder.decode(field2, "UTF-8"):"";
                    String field3= gsinfo.getString("field3");
                    field3 = StringUtil.isNotBlank(field3)?URLDecoder.decode(field3, "UTF-8"):"";
                    String field4= gsinfo.getString("field4");
                    field4 = StringUtil.isNotBlank(field4)?URLDecoder.decode(field4, "UTF-8"):"";
                    String field5= gsinfo.getString("field5");
                    field5 = StringUtil.isNotBlank(field5)?URLDecoder.decode(field5, "UTF-8"):"";
                    String field6= gsinfo.getString("field6");
                    field6 = StringUtil.isNotBlank(field6)?URLDecoder.decode(field6, "UTF-8"):"";
                    String field7= gsinfo.getString("field7");
                    field7 = StringUtil.isNotBlank(field7)?URLDecoder.decode(field7, "UTF-8"):"";
                    String field8= gsinfo.getString("field8");
                    field8 = StringUtil.isNotBlank(field8)?URLDecoder.decode(field8, "UTF-8"):"";
                    String field9= gsinfo.getString("field9");
                    field9 = StringUtil.isNotBlank(field9)?URLDecoder.decode(field9, "UTF-8"):"";
                    String field10= gsinfo.getString("field10");
                    field10 = StringUtil.isNotBlank(field10)?URLDecoder.decode(field10, "UTF-8"):"";
                    String field13= gsinfo.getString("field13");
                    field13 = StringUtil.isNotBlank(field13)?URLDecoder.decode(field13, "UTF-8"):"";

                    Record record = new Record();
                    String gstablename = ConfigUtil.getConfigValue("duijie", "gstablename");
                    log.info("=====gstablename=====:"+gstablename);
                    record.setSql_TableName(gstablename);
                    record.setPrimaryKeys("rowguid");
                    record.set("OperateDate",new Date());
                    record.set("rowguid",auditSpISubapp.getRowguid());
                    log.info("======auditSpISubapp.getRowguid()=====:"+auditSpISubapp.getRowguid());
                    record.set("field1",field1);
                    record.set("field2",field2);
                    record.set("field3",field3);
                    record.set("field4",field4);
                    record.set("field5",field5);
                    record.set("field6",field6);
                    record.set("field7",field7);
                    record.set("field8",field8);
                    record.set("field9",field9);
                    record.set("field10",field10);
                    record.set("field13",field13);
                    iCxBusService.insert(record);
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("taskname", "供水报装");
                    conditionUtil.eq("areacode", areacode);
                    conditionUtil.isNotBlank("VERSION");
                    AuditTask task = iAuditTask.getAuditTaskByCondition(conditionUtil.getMap());
                    if(task == null){
                        return JsonUtils.zwdtRestReturn("0", "提交失败，未找到areacode："+areacode+ " 对应的 供水报装事项", "");
                    }
                    iAuditSpITask.addTaskInstance( businessguid,  biguid,  null,
                            task.getRowguid(),task.getTaskname(),  auditSpISubapp.getRowguid(),  null,  areacode);
                    conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("SUBAPPGUID", auditSpISubapp.getRowguid());
                    List<AuditSpITask> listTask = iAuditSpITask.getAuditSpITaskListByCondition(conditionUtil.getMap()).getResult();
                    for (AuditSpITask auditSpITask : listTask){
                        auditSpITask.setStatus("1");
                        iAuditSpITask.updateAuditSpITask(auditSpITask);
                    }
                }
                if(grinfo != null && !grinfo.isEmpty()){
                    //todo 供热报装申报信息
                    String field1= grinfo.getString("field1");
                    field1 = StringUtil.isNotBlank(field1)?URLDecoder.decode(field1, "UTF-8"):"";
                    String field2= grinfo.getString("field2");
                    field2 = StringUtil.isNotBlank(field2)?URLDecoder.decode(field2, "UTF-8"):"";
                    String field3= grinfo.getString("field3");
                    field3 = StringUtil.isNotBlank(field3)?URLDecoder.decode(field3, "UTF-8"):"";
                    String field4= grinfo.getString("field4");
                    field4 = StringUtil.isNotBlank(field4)?URLDecoder.decode(field4, "UTF-8"):"";
                    String field5= grinfo.getString("field5");
                    field5 = StringUtil.isNotBlank(field5)?URLDecoder.decode(field5, "UTF-8"):"";
                    String field6= grinfo.getString("field6");
                    field6 = StringUtil.isNotBlank(field6)?URLDecoder.decode(field6, "UTF-8"):"";
                    String field10= grinfo.getString("field10");
                    field10 = StringUtil.isNotBlank(field10)?URLDecoder.decode(field10, "UTF-8"):"";
                    String field13= grinfo.getString("field13");
                    field13 = StringUtil.isNotBlank(field13)?URLDecoder.decode(field13, "UTF-8"):"";
                    String field14= grinfo.getString("field14");
                    field14 = StringUtil.isNotBlank(field14)?URLDecoder.decode(field14, "UTF-8"):"";

                    Record record = new Record();
                    String grtablename = ConfigUtil.getConfigValue("duijie", "grtablename");
                    log.info("=====grtablename====:"+grtablename);
                    record.setSql_TableName(grtablename);
                    record.setPrimaryKeys("rowguid");
                    record.set("rowguid",auditSpISubapp.getRowguid());
                    log.info("======auditSpISubapp.getRowguid()=====:"+auditSpISubapp.getRowguid());
                    record.set("OperateDate",new Date());
                    record.set("field1",field1);
                    record.set("field2",field2);
                    record.set("field3",field3);
                    record.set("field4",field4);
                    record.set("field5",field5);
                    record.set("field6",field6);
                    record.set("field10",field10);
                    record.set("field13",field13);
                    record.set("field14",field14);

                    iCxBusService.insert(record);

                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("taskname", "供热报装");
                    conditionUtil.eq("areacode", areacode);
                    conditionUtil.isNotBlank("VERSION");
                    AuditTask task = iAuditTask.getAuditTaskByCondition(conditionUtil.getMap());
                    if(task == null){
                        return JsonUtils.zwdtRestReturn("0", "提交失败，未找到areacode："+areacode+ " 对应的 供热报装事项", "");
                    }
                    iAuditSpITask.addTaskInstance( businessguid,  biguid,  null,
                            task.getRowguid(),task.getTaskname(),  auditSpISubapp.getRowguid(),  null,  areacode);
                    conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("SUBAPPGUID", auditSpISubapp.getRowguid());
                    List<AuditSpITask> listTask = iAuditSpITask.getAuditSpITaskListByCondition(conditionUtil.getMap()).getResult();
                    for (AuditSpITask auditSpITask : listTask){
                        auditSpITask.setStatus("1");
                        iAuditSpITask.updateAuditSpITask(auditSpITask);
                    }
                }

                if(materiallist != null && materiallist.size()>0){
                    for (int i = 0; i < materiallist.size(); i++){
                        JSONObject material = materiallist.getJSONObject(i);
                        String materialname = material.getString("materialname");
                        materialname = StringUtil.isNotBlank(materialname)?URLDecoder.decode(materialname, "UTF-8"):"";
                        String cliengguid = material.getString("file");
                        AuditSpIMaterial auditSpIMaterial = new AuditSpIMaterial();
                        auditSpIMaterial.setOperatedate(new Date());
                        auditSpIMaterial.setRowguid(UUID.randomUUID().toString());
                        auditSpIMaterial.setBusinessguid(businessguid);
                        auditSpIMaterial.setBiguid(biguid);
                        auditSpIMaterial.setSubappguid(auditSpISubapp.getRowguid());
                        auditSpIMaterial.setCliengguid(cliengguid);
                        auditSpIMaterial.setMaterialname(materialname);
                        auditSpIMaterial.setOrdernum(1);
                        auditSpIMaterial.setStatus("10");

                        iAuditSpIMaterial.addSpIMaterial(auditSpIMaterial);
                    }
                }

                String flowsn = save("flase", auditSpISubapp.getRowguid());
                dataJson.put("flowsn",flowsn);
                return JsonUtils.zwdtRestReturn("1", "供水供电提交办件申报信息成功！", dataJson.toString());
            } else {
                log.info("=======结束调用submitgsgdproject接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======submitgsgdproject接口参数：params【" + params + "】=======");
            log.info("=======submitgsgdproject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "供水供电提交办件申报信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 附件上传
     */
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String uploadfile(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用uploadfile接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 文件的base64编码字符串
                String file = obj.getString("file");
                String filename = obj.getString("filename");
                if(StringUtil.isBlank(file)){
                    return JsonUtils.zwdtRestReturn("0", "上传失败,file参数为空", "");
                }
                if(StringUtil.isBlank(filename)){
                    return JsonUtils.zwdtRestReturn("0", "上传失败,filename参数为空", "");
                }
                int lastDotIndex = filename.lastIndexOf('.');
                if (lastDotIndex == -1) {
                    return JsonUtils.zwdtRestReturn("0", "上传失败,文件名中没有后缀", "");
                }
                String contentType = "."+filename.substring(lastDotIndex + 1).toLowerCase();
                // 获取文件类
                // base64 转换文件
                if (file.contains("data:")) {
                    int start = file.indexOf(",");
                    file = file.substring(start + 1);
                }
                final Base64.Decoder decoder = Base64.getDecoder();
                file = file.replaceAll("\r|\n", "");
                file = file.trim();
                byte[] bytes = decoder.decode(file);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                String attachguid = UUID.randomUUID().toString();
                String clientGuid = UUID.randomUUID().toString();
                frameAttachInfo.setAttachGuid(attachguid);
                frameAttachInfo.setAttachFileName(filename);
                frameAttachInfo.setContentType(contentType);
                frameAttachInfo.setCliengGuid(clientGuid);
                frameAttachInfo.setUploadUserGuid("接口上传");
                frameAttachInfo.setUploadUserDisplayName("接口上传");
                // 上传附件
                frameAttachInfo = IAttachService.upload(frameAttachInfo, FileManagerUtil.getInput(inputStream));
                JSONObject dataJson = new JSONObject();
                dataJson.put("fileid", frameAttachInfo.getCliengGuid());
                return JsonUtils.zwdtRestReturn("1", "附件上传成功！", dataJson.toString());
            } else {
                log.info("=======结束调用uploadfile接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======uploadfile接口参数：params【" + params + "】=======");
            log.info("=======uploadfile异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "附件上传失败：" + e.getMessage(), "");
        }
    }

    /**
     * 发送mq消息
     * @param auditProject 办件信息
     * @param operate 对应的mq操作
     */
    public void sendMQ(AuditProject auditProject, String operate) {
        try {
            log.info("========== 开始调用发送mq消息方法 ==========");
            ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
            if (auditProject.getIs_test() != Integer.parseInt(ZwfwConstant.CONSTANT_STR_ONE)) {
                log.info("非测试件");
                // 接办分离 受理
                String msg = auditProject.getRowguid() + "." + auditProject.getAreacode();
                sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                        + "370800" + "." + operate + "." + auditProject.getTask_id());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("发送mq方法出现异常");
        }
    }

    /**
     * 生成办件操作表数据
     * 在发送mq消息之前执行
     * @param auditProject 办件信息
     * @param operate 操作
     */
    public String  generateAuditProjectOperate(AuditProject auditProject, String operate,String operateuser,String dealResult) {
        IAuditProjectOperation auditProjectOperationService = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        AuditProjectOperation operation = new AuditProjectOperation();
        String operationType = "";
        if("accept".equals(operate)) {
            // 受理
            operationType = ZwfwConstant.OPERATE_SL;
        }
        else if("notaccept".equals(operate)) {
            // 不予受理
            operationType = ZwfwConstant.OPERATE_BYSL;
        }
        else if("jj".equals(operate)) {
            // 接件
            operationType = ZwfwConstant.OPERATE_JJ;
        }
        else if("bmksbl".equals(operate)) {
            // 接件
            operationType = ZwfwConstant.OPERATE_SPTG;
        }
        else {
            // 办结或审批不通过
            if("1".equals(dealResult)) {
                operationType = ZwfwConstant.OPERATE_BJ;
            }
            else {
                operationType = ZwfwConstant.OPERATE_SPBTG;
            }
        }
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("projectguid", auditProject.getRowguid());
        sqlConditionUtil.eq("operatetype", operationType);
        AuditProjectOperation result = auditProjectOperationService.getAuditOperationByCondition(sqlConditionUtil.getMap()).getResult();
        if(result == null) {
            operation.setRowguid(UUID.randomUUID().toString());
            operation.setProjectGuid(auditProject.getRowguid());
            operation.setOperateType(operationType);
            operation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
            operation.setOperateusername(operateuser);
            operation.setApplyerGuid(auditProject.getApplyeruserguid());
            operation.setApplyerName(auditProject.getApplyername());
            operation.setAreaCode(auditProject.getAreacode());
            operation.setTaskGuid(auditProject.getTaskguid());
            operation.setOperatedate(new Date());
            auditProjectOperationService.addProjectOperation(operation);
            return operation.getRowguid();
        }
        else {
            log.info("办件" + auditProject.getRowguid() + "的操作[" + operationType + "]的数据已存在，不再生成");
            return "";
        }
    }

    private void saveLxydghxkInfo(JSONObject param) {
        String subappguid = param.getString("subappguid");// 申报唯一标识
        String itemname = param.getString("itemname");// 项目名称
        String subappname = param.getString("subappname");// 子申报名称
        String itemcode = param.getString("itemcode");// 投资平台项目代码
        String projectLevel = param.getString("projectLevel");// 重点项目（代码项）
        String itemAddress = param.getString("itemAddress");// 项目地址
        String userterm = param.getString("userterm");
        String bdcdanyuanhao = param.getString("bdcdanyuanhao");
        String area = param.getString("area");// 区（园区）
        String road = param.getString("road");// 街道
        String eastToArea = param.getString("eastToArea");// 四至范围（东至）
        String westToArea = param.getString("westToArea");// 四至范围（西至）
        String southToArea = param.getString("southToArea");// 四至范围（南至）
        String northToArea = param.getString("northToArea");// 四至范围（北至）
        String proTyped = param.getString("proTyped");// 立项类型（代码项）
        String proOu = param.getString("proOu");// 立项部门（代码项）
        String investTyped = param.getString("investTyped");// 投资类型（代码项）
        String hangyeType = param.getString("hangyeType");// 行业类别（代码项）
        String moneySources = param.getString("moneySources");// 资金来源（代码项）
        Double allMoney = param.getDouble("allMoney");// 行业类别
        String areaSources = param.getString("areaSources");// 土地来源
        String useLandType = param.getString("useLandType");// 规划用地性质
        Double areaUsed = param.getDouble("areaUsed");// 用地面积（公顷）
        Double newLandType = param.getDouble("newLandType");// 新增用地面积（公顷）
        String buildType = param.getString("buildType");// 项目类型
        Double allBuildArea = param.getDouble("allBuildArea");// 总建筑面积（M2）
        Double overLoadArea = param.getDouble("overLoadArea");// 地上（M2）
        Double downLoadArea = param.getDouble("downLoadArea");// 地下（M2）
        String buildContent = param.getString("buildContent");// 建设内容
        Date planStartDate = param.getDate("planStartDate");// 计划开工日期
        Date planEndDate = param.getDate("planEndDate");// 计划竣工日期

        String itemSource = param.getString("itemSource");// 项目来源
        Double xznxfl = param.getDouble("xznxfl");// 新增年综合能源消费量吨标煤（当量值）
        Double xznydl = param.getDouble("xznydl");// 新增年用电量（万千瓦时）

        // 更新表单信息
        AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService.findAuditSpSpLxydghxkBysubappguid(subappguid);
        boolean addFlag = false; // 默认为更新
        if (auditSpSpLxydghxk == null) {
            auditSpSpLxydghxk = new AuditSpSpLxydghxk();
            auditSpSpLxydghxk.setRowguid(UUID.randomUUID().toString());
            auditSpSpLxydghxk.setSubappguid(subappguid);
            addFlag = true; // 新增
        }
        auditSpSpLxydghxk.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditSpSpLxydghxk.setOperatedate(new Date());
        auditSpSpLxydghxk.setItemname(itemname);
        auditSpSpLxydghxk.setSubappname(subappname);
        auditSpSpLxydghxk.setItemcode(itemcode);
        auditSpSpLxydghxk.setProjectlevel(projectLevel);
        auditSpSpLxydghxk.set("userterm", userterm);
        auditSpSpLxydghxk.set("bdcdanyuanhao", bdcdanyuanhao);
        auditSpSpLxydghxk.setArea(area);
        auditSpSpLxydghxk.setRoad(road);
        auditSpSpLxydghxk.setEasttoarea(eastToArea);
        auditSpSpLxydghxk.setWesttoarea(westToArea);
        auditSpSpLxydghxk.setSouthtoarea(southToArea);
        auditSpSpLxydghxk.setNorthtoarea(northToArea);
        auditSpSpLxydghxk.setProtyped(proTyped);
        auditSpSpLxydghxk.setProou(proOu);
        auditSpSpLxydghxk.setInvesttyped(investTyped);
        auditSpSpLxydghxk.setHangyetype(hangyeType);
        auditSpSpLxydghxk.setMoneysources(moneySources);
        auditSpSpLxydghxk.setAllmoney(allMoney);
        auditSpSpLxydghxk.setAreasources(areaSources);
        auditSpSpLxydghxk.setUselandtype(useLandType);
        auditSpSpLxydghxk.setAreaused(areaUsed);
        auditSpSpLxydghxk.setNewlandtype(newLandType);
        auditSpSpLxydghxk.setBuildtype(buildType);
        auditSpSpLxydghxk.setAllbuildarea(allBuildArea);
        auditSpSpLxydghxk.setOverloadarea(overLoadArea);
        auditSpSpLxydghxk.setDownloadarea(downLoadArea);
        auditSpSpLxydghxk.setBuildcontent(buildContent);
        auditSpSpLxydghxk.setPlanstartdate(planStartDate);
        auditSpSpLxydghxk.setPlanenddate(planEndDate);
        auditSpSpLxydghxk.setItemsource(itemSource);
        auditSpSpLxydghxk.setXznxfl(xznxfl);
        auditSpSpLxydghxk.setXznydl(xznydl);

        // 第一阶段新增字段
        String xmjsyj = param.getString("xmjsyj");// 项目建设依据
        String xmnxdz = param.getString("xmnxdz");// 项目拟选位置
        String nydmj = param.getString("nydmj");// 拟用地面积（含各地类明细）
        String njsgm = param.getString("njsgm");// 拟建设规模
        String pzydjg = param.getString("pzydjg");// 批准用地机关
        String pzydwh = param.getString("pzydwh");// 批准用地文号
        String tdyt = param.getString("tdyt");// 土地用途
        String yjsgm = param.getString("yjsgm");// 建设规模
        String zbj = param.getString("zbj");// 资本金
        String zztz = param.getString("zztz");// 占总投资

        // 第一阶段新增字段
        auditSpSpLxydghxk.set("xmjsyj", xmjsyj);
        auditSpSpLxydghxk.set("xmnxdz", xmnxdz);
        auditSpSpLxydghxk.set("nydmj", nydmj);
        auditSpSpLxydghxk.set("njsgm", njsgm);
        auditSpSpLxydghxk.set("pzydjg", pzydjg);
        auditSpSpLxydghxk.set("pzydwh", pzydwh);
        auditSpSpLxydghxk.set("tdyt", tdyt);
        auditSpSpLxydghxk.set("jsgm", yjsgm);
        auditSpSpLxydghxk.set("zbj", zbj);
        auditSpSpLxydghxk.set("zztz", zztz);

        // addFlag 为true, 说明数据库中不存在该记录
        if (addFlag) {
            iAuditSpSpLxydghxkService.insert(auditSpSpLxydghxk);
        }
        else {
            iAuditSpSpLxydghxkService.update(auditSpSpLxydghxk);
        }
    }

    private void setItemInfo(AuditRsItemBaseinfo auditRsItemBaseinfo, JSONObject obj) {
        // 1.1 获取项目类型
        String itemType = obj.getString("itemtype");
        // 1.2 获取建设性质
        String constructionProperty = obj.getString("constructionproperty");
        // 1.3 拟开工时间
        Date itemStartDate = obj.getDate("itemstartdate");
        // 1.4 拟建成时间
        Date itemFinishDate = obj.getDate("itemfinishdate");
        // 1.5 获取总投资（万元）
        Double totalInvest = obj.getDouble("totalinvest");
        // 1.6 获取建设地点
        String constructionSite = obj.getString("constructionsite");
        // 1.7 获取建设地点详情
        String constructionSiteDesc = obj.getString("constructionsitedesc");
        // 1.8 获取所属行业
        String belongTindustry = obj.getString("belongtindustry");
        // 1.9 获取建设规模及内容
        String constructionScaleAndDesc = obj.getString("constructionscaleanddesc");
        // 1.10 获取用地面积(亩)
        Double landArea = obj.getDouble("landarea");
        // 1.11 获取是否技改项目
        String isImprovement = obj.getString("isimprovement");
        // 1.12 项目投资来源
        Integer xmtzly = obj.getInteger("xmtzly");
        // 1.13 土地获取方式
        Integer tdhqfs = obj.getInteger("tdhqfs");
        // 1.14 土地是否带设计方案
        Integer tdsfdsjfa = obj.getInteger("tdsfdsjfa");
        // 1.15 是否完成区域评估
        Integer sfwcqypg = obj.getInteger("sfwcqypg");
        // 1.16 建筑面积
        String jzmjStr = obj.getString("jzmj");
        // 1.17 国标行业
        String gbhy = obj.getString("gbhy");
        // 1.18 项目资金属性
        String xmzjsx = obj.getString("xmzjsx");
        // 1.19 工程行业分类
        String gchyfl = obj.getString("gchyfl");
        // 1.20 项目经纬度坐标
        String xmjwdzb = obj.getString("xmjwdzb");
        // 1.21 工程范围
        String gcfw = obj.getString("gcfw");
        // 1.22 立项类型
        String lxlx = obj.getString("lxlx");
        // 1.23 长度
        String cd = obj.getString("cd");
        // 1.24 是否线性工程
        String sfxxgc = obj.getString("sfxxgc");
        // 1.25 建设地点行政区划
        String jsddxzqh = obj.getString("jsddxzqh");
        // 1.26 重点项目
        String zdxmlx = obj.getString("zdxmlx");
        Double jzmj = null;
        if (StringUtil.isNotBlank(jzmjStr)) {
            jzmj = Double.parseDouble(jzmjStr);
        }
        auditRsItemBaseinfo.setOperatedate(new Date());
        auditRsItemBaseinfo.setItemtype(itemType);
        auditRsItemBaseinfo.setConstructionproperty(constructionProperty);
        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
        auditRsItemBaseinfo.setTotalinvest(totalInvest);
        auditRsItemBaseinfo.setConstructionsite(constructionSite);
        auditRsItemBaseinfo.setConstructionsitedesc(constructionSiteDesc);
        auditRsItemBaseinfo.setBelongtindustry(belongTindustry);
        auditRsItemBaseinfo.setConstructionscaleanddesc(constructionScaleAndDesc);
        auditRsItemBaseinfo.setIsimprovement(isImprovement);
        auditRsItemBaseinfo.setXmtzly(xmtzly);
        auditRsItemBaseinfo.setTdhqfs(tdhqfs);
        auditRsItemBaseinfo.setTdsfdsjfa(tdsfdsjfa);
        auditRsItemBaseinfo.setSfwcqypg(sfwcqypg);
        auditRsItemBaseinfo.setJzmj(jzmj);
        auditRsItemBaseinfo.setXmzjsx(xmzjsx);
        auditRsItemBaseinfo.setGbhy(gbhy);
        auditRsItemBaseinfo.set("lxlx", lxlx);
        auditRsItemBaseinfo.set("gchyfl", gchyfl);
        auditRsItemBaseinfo.set("xmjwdzb", xmjwdzb);
        auditRsItemBaseinfo.set("gcfw", gcfw);
        auditRsItemBaseinfo.set("sfxxgc", sfxxgc);
        auditRsItemBaseinfo.set("cd", cd);
        auditRsItemBaseinfo.set("jsddxzqh", String.valueOf(jsddxzqh));
        auditRsItemBaseinfo.set("zdxmlx", zdxmlx);
    }

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
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 这边实现办件的分发
     * [一句话功能简述]
     *
     * @param isck
     */
    public String save(String isck, String subappGuid) {
        log.info("调用---save---isck:"+isck+" subappGuid:"+subappGuid);
        String flowsn = "";
        // List<AuditSpITask> listTask =
        // iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("SUBAPPGUID", subappGuid);
        conditionUtil.eq("STATUS", "1");
        List<AuditSpITask> listTask = iAuditSpITask.getAuditSpITaskListByCondition(conditionUtil.getMap()).getResult();
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();


        // 判断是否第一次发件
        boolean firstsj = true;
        for (AuditSpITask auditspitask : listTask) {
            if (StringUtil.isNotBlank(auditspitask.getProjectguid())) {
                firstsj = false;
            }
        }
        log.info("firstsj:"+firstsj);
        List<String> taskguids = new ArrayList<>();


        /* 3.0新增逻辑验证 */
        // 检查事项是否有推荐关联单位类型,如果有是不是关联了相关类型的单位
        boolean point = false;
        for (AuditSpITask task : listTask) {
            // 查询此事项有没有配置单位类型
            String auditbasetaskguid = taskCommonService
                    .getCropInfo(task.getTaskguid(), subappGuid, task.getPhaseguid()).getResult();
            if (StringUtil.isNotBlank(auditbasetaskguid)) {
                // 查询此基础事项偶没有配置单位类型
                String cropType = taskCommonService.getCropName(auditbasetaskguid).getResult();
                if (StringUtil.isNotBlank(cropType)) {
                    // 查询此单位类型有没有关联单位
                    Integer count = iAuditSpITaskCorpService.countAuditSpITaskCorps(task.getTaskguid(), subappGuid);
                    if (count == 0) {
                        point = true;
                        break;
                    }
                }
            }
        }
        log.info("point:"+point);
        /* 3.0新增逻辑结束 */

        String csaeguid = auditSpISubapp.getCaseguid();
        log.info("csaeguid:"+csaeguid);

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
        log.info("auditSpISubapp.getYewuguid():"+auditSpISubapp.getYewuguid());
        AuditRsItemBaseinfo result = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid())
                .getResult();
        StringBuilder sb = new StringBuilder();
        sb.append("'");
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappGuid);
        List<DantiInfo> alldantiInfo = iDantiInfoService.findListBySubAppguid(subappGuid);
        for (AuditSpITask task : listTask) {
            log.info("task"+task);
            log.info("result"+result);
            log.info("auditSpISubapp"+auditSpISubapp);
            // 先生成办件，再初始化材料
            String projectGuid = handleProjectService.InitBLSPProject(task.getTaskguid(), "", "", "",
                    result.getItemlegalcerttype(), result.getItemlegalcertnum(), "", "", "", auditSpISubapp.getBiguid(),
                    subappGuid, auditSpISubapp.getBusinessguid(), StringUtils.isNotBlank(result.getItemlegalcreditcode())?result.getItemlegalcreditcode():result.getItemlegalcertnum(),
                    result.getItemlegaldept()).getResult();
            log.info("projectGuid:"+projectGuid);
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
            log.info("auditSpISubapp.getYewuguid():"+auditSpISubapp.getYewuguid());
            if (StringUtil.isNotBlank(auditSpISubapp.getYewuguid())) {
                AuditProject project = new AuditProject();
                project.setRowguid(projectGuid);
                project.setProjectname(task.getTaskname() + "(" + result.getItemname() + ")");
                project.setCertnum(result.getItemlegalcertnum());
                project.setApplyername(result.getItemlegaldept());
                project.setApplydate(new Date());
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
                 * String flowsn=getFlowsn(task.getTaskguid());
                 * Random random = new Random();
                 * if(StringUtil.isBlank(flowsn)){
                 * flowsn= "080101"+ random.nextInt(1000 - 100) + 100 + 1;
                 * }
                 * project.setFlowsn(flowsn);
                 */

                auditProjectService.updateProject(project);
            }
            sb.append(projectGuid).append("','");
            // 再进行材料初始化
            handleSPIMaterialService.initProjctMaterial(materiallist, auditSpISubapp.getBusinessguid(), subappGuid,
                    task.getTaskguid(), projectGuid);

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
            // if (StringUtil.isNotBlank(pushTaskItem)) {
            if (ValidateUtil.isNotBlankCollection(pushTaskItemIdList)) {
                // List<String> pushTaskItemIdList =
                // Arrays.stream(pushTaskItem.split(",")).collect(Collectors.toList());
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(task.getTaskguid(), false).getResult();
                if (auditTask != null && !auditTask.isEmpty()) {
                    String itemId = auditTask.getItem_id();
                    log.info("获取事项itemId--->" + itemId);
                    if (pushTaskItemIdList.contains(itemId)) {
                        log.info("该事项属于推送事项");
                        // 属于推送事项，需要调用第三方流程提交检查接口和发起流程接口
                        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, null)
                                .getResult();
                        log.info("推送事项的办件主键--->" + auditProject.getRowguid());
                        if (auditProject != null && !auditProject.isEmpty()) {
                            flowsn = auditProject.getFlowsn();
                             if(grList.contains(itemId)){
                                log.info("供热事项");
                                // 供热事项
                                String projectFlowSn = auditProject.getFlowsn();
                                log.info("获取办件流水号--->" + projectFlowSn);
                                //是，调用山东公用线上接口文档
                                //log.info("==获取电子表单数据==");
                                //String grtablename = ConfigUtil.getConfigValue("duijie", "grtablename");
                                //log.info("==供热电子表单表名==:" + grtablename);
                                // 1。获取电子表单数据
                                //Record dataObj = GongYongShuiWuUtil.grdata(auditProject.getSubappguid(), auditProject, grtablename);
                                // 2。调用山东公用线上接口
                                //log.info("==调用山东公用线上接口==");
                                //String platformid = GongYongShuiWuUtil.submit(dataObj, auditProject);
                                // 3.数据上报
                                //if (StringUtil.isNotBlank(platformid)) {
                                //    log.info("已请求山东公用线上接口，单据编号为：" + platformid);
                                    try {
                                        // 推送成功后将办件状态更新成 26 已接件并且生成audit_project_operate 接件数据
                                        auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                        auditProjectService.updateProject(auditProject);
                                        log.info("生成接件的办件操作数据");
                                        AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                        auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                        auditProjectOperation.setProjectGuid(projectGuid);
                                        auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                        auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                        auditProjectOperation.setOperateusername("业务办理人员");
                                        auditProjectOperation.setOperatedate(new Date());
                                        auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                        auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                        auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                        auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
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
//                                }
//                                else {
//                                    log.info("调用山东公用线上接口异常，未获取到单据编号");
//                                }
                            }
                            else {
                                // 供水事项
                                String projectFlowSn = auditProject.getFlowsn();
                                log.info("获取办件流水号--->" + projectFlowSn);
                                JSONObject checkResult;
                                int i = 0;
                                // 判断当前办理的事项是不是“济宁市以及任城、太白湖、高新、经开区”
                                if(isJiningArea(auditTask.getAreacode())){
                                    log.info("==当前办理事项是济宁市或者任城、太白湖、高新、经开区==");
                                    //是，调用山东公用线上接口文档
                                    //log.info("==获取电子表单数据==");
                                    //String gstablename = ConfigUtil.getConfigValue("duijie", "gstablename");
                                    //log.info("==供水电子表单表名==:" + gstablename);
                                    // 1。获取电子表单数据
                                    //Record dataObj = GongYongShuiWuUtil.ysdata(auditProject.getSubappguid(), auditProject, gstablename);
                                    // 2。调用山东公用线上接口
                                    //log.info("==调用山东公用线上接口==");
                                    //String platformid = GongYongShuiWuUtil.submit(dataObj, auditProject);
                                    // 3.数据上报
                                    //if (StringUtil.isNotBlank(platformid)) {
                                    //    log.info("已请求山东公用线上接口，单据编号为：" + platformid);
                                        try {
                                            // 推送成功后将办件状态更新成 26 已接件并且生成audit_project_operate 接件数据
                                            auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                            auditProjectService.updateProject(auditProject);
                                            log.info("生成接件的办件操作数据");
                                            AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                            auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                            auditProjectOperation.setProjectGuid(projectGuid);
                                            auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                            auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                            auditProjectOperation.setOperateusername("业务办理人员");
                                            auditProjectOperation.setOperatedate(new Date());
                                            auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                            auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                            auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                            auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
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
                                    //}
//                                    else {
//                                        log.info("调用山东公用线上接口异常，未获取到单据编号");
//                                    }
                                }
                                // 不是
                                else{
                                    while (true) {
                                        // TODO 这里的户号 不知道是不是 auditProject.getCertnum()
                                        checkResult = ZhongShanShuiWuUtil.checkSubmitFlow(projectFlowSn, auditProject.getCertnum());
                                        if (checkResult.getBoolean("isSuccess")) {
                                            // 如果请求成功，跳出循环
                                            break;
                                        }
                                        else if (!checkResult.getBoolean("isSuccess")
                                                && "0".equals(checkResult.getString("code"))) {
                                            // 请求失败同时code为0表示流水号有问题，重新生成流水号
                                            /*IHandleFlowSn handleFlowSn = ContainerFactory.getContainInfo()
                                                    .getComponent(IHandleFlowSn.class);
                                            String numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE,
                                                    auditProject.getCenterguid()).getResult();
                                            if (StringUtil.isBlank(numberFlag)) {
                                                numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
                                            }
                                            projectFlowSn = handleFlowSn.getFlowsn("办件编号", numberFlag).getResult();
                                            log.info("重新生成的流水号--->" + projectFlowSn);*/
                                            projectFlowSn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());
                                            auditProject.set("flowsn", projectFlowSn);
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
                                        // String billNo = startProcess(auditProject);
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
                                                // 推送成功后将办件状态更新成 26 已接件并且生成audit_project_operate 接件数据
                                                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
                                                auditProjectService.updateProject(auditProject);
                                                log.info("生成接件的办件操作数据");
                                                AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                                                auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                                auditProjectOperation.setProjectGuid(projectGuid);
                                                auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_JJ);
                                                auditProjectOperation.setOperateUserGuid("45f0c5f9-cad2-49e6-887d-b38dfcbc23de");
                                                auditProjectOperation.setOperateusername("业务办理人员");
                                                auditProjectOperation.setOperatedate(new Date());
                                                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                                auditProjectOperation.setAreaCode(auditProject.getAreacode());
                                                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
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
                }
                else {
                    log.info("未查询到事项");
                }
            }
        }
        sb.append("'");
        return flowsn;
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
     * 判断当前办理的事项是否属于济宁市及其特定区县
     * @param areaCode 地区代码
     * @return 是否属于指定区域
     */
    public boolean isJiningArea(String areaCode) {
        // 检查 areaCode 是否为 null 或空字符串
        if(StringUtil.isBlank(areaCode)){
            return false;
        }
        String jiningAreaCodes = iConfigService.getFrameConfigValue("gygsdjareacode");
        if(StringUtil.isNotBlank(jiningAreaCodes)&& jiningAreaCodes.contains(areaCode)){
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=====开始调用接口test=====");
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        // 供热调接口
        Record record = new Record();
        // 将表单数据放入result
        JSONObject result = new JSONObject();
        result.put("field1", "济宁四和供热有限公司汶上分公司");//field1	所属分公司	SELECT	必填
        result.put("field2", "测试户主名称");//field2	户主名称	TEXT	必填
        result.put("field10", "");//field10	用热位置	GPSMAP	必填
        result.put("field3", "100");//field3	房屋产权面积（㎡）	TEXT	必填
        result.put("field4", "居民");//field4	用热类型	SELECT	必填
        result.put("field5", "13955556666");//field5	联系电话	TEXT	必填
        result.put("field6", "110101200901012708");//field6	身份证号码	TEXT	必填
        result.put("field13", "91320582704068740Y");//field13	社会统一信用代码	TEXT	必填
        // 材料
        JSONObject material = new JSONObject();
        JSONArray field7 = new JSONArray();// 总平面图
        JSONArray field8 = new JSONArray();// 不动产权证或规划许可证
        JSONArray field9 = new JSONArray();// 申报资料
        JSONArray field11 = new JSONArray();// 营业执照
        JSONArray field12 = new JSONArray();// 法人身份证正反面
        JSONArray field14 = new JSONArray();// 供热申请理由
        // 根据附件attachGuid获取附件流
        FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail("f6d5df41-713c-4efb-98d9-e219a80f91da");
        InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
        String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
        String fileBase64 = GongYongShuiWuUtil.attachToBase64(content,mimeType);
        field7.add(fileBase64);
        field8.add(fileBase64);
        field9.add(fileBase64);
        field11.add(fileBase64);
        field12.add(fileBase64);
        field14.add(fileBase64);
        material.put("field7", field7);
        material.put("field8", field8);
        material.put("field9", field9);
        material.put("field11", field11);
        material.put("field12", field12);
        material.put("field14", field14);
        record.put("material", material);
        record.put("result", result);
        AuditProject auditProject = new AuditProject();
        auditProject.setFlowsn("112233");
        auditProject.setCertnum("91320582704068740Y");
        String platformid = GongYongShuiWuUtil.submit(record, auditProject);
        return "";
    }

    @RequestMapping(value = "/testys", method = RequestMethod.POST)
    public String testys(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=====开始调用接口test=====");
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

        // 供热调接口
        Record record = new Record();
        // 将表单数据放入result
        JSONObject result = new JSONObject();
        result.put("field1", "汶上供水");// 所属分公司	SELECT	必填
        result.put("field2", "个人");// 用户类型	SELECT	必填
        result.put("field3", "李四");// 用户名称	TEXT	必填
        result.put("field4", "411221199502053231");// 证件号码	SELECT	必填
        result.put("field5", "连城大道新垌村");// 用水地址	TEXT	必填
        result.put("field6", "新装");// 用水类别	SELECT	必填
        result.put("field7", "DN80");// 水表口径	SELECT	必填
        result.put("field8", "张三");// 法人姓名	TEXT	必填
        result.put("field9", "17613058512");// 联系电话	TEXT	必填
        result.put("field10", "我要接水");// 备注	TEXT	必填
        result.put("field13", "");// 社会统一信用代码	TEXT	必填
        // 材料
        JSONObject material = new JSONObject();
        JSONArray field11 = new JSONArray();// 相关文件
        JSONArray Field14 = new JSONArray();// 不动产权证或规划许可证
        JSONArray Field15 = new JSONArray();// 申报资料
        JSONArray Field16 = new JSONArray();// 营业执照
        JSONArray Field17 = new JSONArray();// 法人身份证正反面
        // 根据附件attachGuid获取附件流
        FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail("f6d5df41-713c-4efb-98d9-e219a80f91da");
        InputStream content = iAttachService.getAttach(frameAttachInfo.getAttachGuid()).getContent();
        String mimeType = GongYongShuiWuUtil.getMimeTypeFromExtension(frameAttachInfo.getAttachFileName());
        String fileBase64 = GongYongShuiWuUtil.attachToBase64(content,mimeType);
        field11.add(fileBase64);
        Field14.add(fileBase64);
        Field15.add(fileBase64);
        Field16.add(fileBase64);
        Field17.add(fileBase64);
        material.put("field11", field11);
        material.put("Field14", Field14);
        material.put("Field15", Field15);
        material.put("Field16", Field16);
        material.put("Field17", Field17);
        record.put("material", material);
        record.put("result", result);
        record.put("busstype", "YSBZ");
        AuditProject auditProject = new AuditProject();
        auditProject.setFlowsn("11223344");
        auditProject.setCertnum("411221199502053231");
        String platformid = GongYongShuiWuUtil.submit(record, auditProject);
        return "";
    }
}


