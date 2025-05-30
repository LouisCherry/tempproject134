package com.epoint.zwdt.zwdtrest.auditbusiness;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
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
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspshareoption.api.IAuditSpShareoption;
import com.epoint.basic.auditsp.auditspshareoption.domain.AuditSpShareoption;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.danti.bizcommon.DantiBizcommon;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.dantiinfov3.api.entity.DantiInfoV3;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;
import com.epoint.xmz.zjxl.util.AesDemoUtil;
import com.epoint.zwdt.zwdtrest.project.api.IJnDantiinfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工改一件事相关接口
 */

@RestController
@RequestMapping("/jnggYjsZwdtBusiness")
public class JnGgYjsAuditOnlineBusinessController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 主题api
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IYjsCzcApplyerService iYjsCzcApplyerService;

    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;

    /**
     * 法人API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private ICxBusService iCxBusService;

    /**
     * 微信消息推送相关API
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;

    /**
     * 办件操作API
     */
    @Autowired
    private IHandleProject iHandleProject;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    /**
     * 问卷相关api
     */
    @Autowired
    private IAuditSpShareoption iauditspshareoption;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 流水号API
     */
    @Autowired
    private IHandleFlowSn iHandleFlowSn;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 网上用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;

    @Autowired
    private IAuditSpBasetask basetaskService;

    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;

    @Autowired
    private IAuditSpBasetaskR basetaskRService;

    /**
     * 共享材料关系API
     */
    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;

    @Autowired
    private IAuditSpSelectedOptionService iAuditSpSelectedOptionService;

    /**
     * 主题共享材料API
     */
    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    /**
     * 材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 关联事项API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;


    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;

    /**
     * 事项情形API
     */
    @Autowired
    private IAuditSpElementService iAuditSpElementService;

    /**
     * 套餐式开公司API
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;

    /**
     * 情形选项API
     */
    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    @Autowired
    IAuditSpOptiontownshipService iauditspoptiontownshipservice;

//########################  工改一件事 ####################################
    /**
     * 项目信息API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigservice;

    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;

    @Autowired
    private IDantiInfoService iDantiInfoService;

    @Autowired
    private IJnDantiinfoService iJnDantiinfoService;
    /**
     * 通过企业统一信用代码来获取项目列表信息
      * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getitemlistforyjs", method = RequestMethod.POST)
    public String getitemlistforyjs(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getitemlistforyjs接口=======");
            // 入参
            JSONObject object = JSONObject.parseObject(params);
            JSONObject paramsObject = object.getJSONObject("params");
            //统一社会信用代码
            String creditcode = paramsObject.getString("creditcode");
            //一件事的phaseid
            String phaseid = paramsObject.getString("phaseid");
            //每页记录数
            String pagesize = paramsObject.getString("pagesize");
            //当前页码
            String currentpage = paramsObject.getString("currentpage");
            // 2、获取用户注册信息
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister != null) {
                // 3、获取用户身份是代办人或者管理者的企业ID
                SqlConditionUtil grantSqlConditionUtil = new SqlConditionUtil();
                grantSqlConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                grantSqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                grantSqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                // 0-否，1-是
                grantSqlConditionUtil.eq("m_isactive", "1"); // 代办人或管理者身份已激活
                List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                        .selectCompanyGrantByConditionMap(grantSqlConditionUtil.getMap()).getResult();
                String strWhereCompanyId = "";// 拼接被授权的所有企业id
                for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                    strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                }
                // 4、获取用户身份是代办人或者管理者的企业ID
                SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
                legalSqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                legalSqlConditionUtil.eq("is_history", "0");
                legalSqlConditionUtil.eq("isactivated", "1");
                List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                        .selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
                String strInCreditCode = "";
                for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                    strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
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
                            strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
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
                    if (StringUtil.isNotBlank(creditcode)) {
                        conditionUtil.eq("itemlegalcertnum", creditcode);
                    }
                    conditionUtil.in("itemlegalcertnum", strInCreditCode);
                    conditionUtil.isBlank("parentid");
                    // 3、 获取项目信息
                    PageData<AuditRsItemBaseinfo> auditRsItemBaseinfosPageData = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(),
                                    (Integer.parseInt(currentpage) - 1) * Integer.parseInt(pagesize),
                                    Integer.parseInt(pagesize), "", "")
                            .getResult();
                    totalCount = auditRsItemBaseinfosPageData.getRowCount();
                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
                    if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                        // 3.1、 将项目信息返回
                        int id = (Integer.parseInt(currentpage) - 1) * Integer.parseInt(pagesize) + 1; // 序号
                        String jyItemName = iConfigservice.getFrameConfigValue("XM_JIANYI_NAME"); // 建议项目名称
                        for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                            JSONObject itemJson = new JSONObject();
                            itemJson.put("id", id++);
                            itemJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                            itemJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目代码
                            itemJson.put("itemguid", auditRsItemBaseinfo.getRowguid());
                            itemJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());// 建设单位
                            itemJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            String biGuid = auditRsItemBaseinfo.getBiguid();
                            String businessGuid = "";
                            String phaseGuid = "";
                            String hasJy = "";
                            if (StringUtil.isNotBlank(biGuid)) {
                                // 根据主题实例查询主题配置
                                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid)
                                        .getResult();
                                if (auditSpInstance != null) {
                                    businessGuid = auditSpInstance.getBusinessguid();

                                    // 根据主题标识查询主题
                                    if (StringUtil.isNotBlank(jyItemName)) {
                                        AuditSpBusiness business = iAuditSpBusiness
                                                .getAuditSpBusinessByRowguid(businessGuid).getResult();
                                        if (business != null) {
                                            if (jyItemName.equals(business.getBusinessname())) {
                                                hasJy = "1";
                                            }
                                        }
                                    }

                                    // 根据主题标识和阶段标识查询阶段主键
                                    SqlConditionUtil sqlSelectPhase = new SqlConditionUtil();
                                    sqlSelectPhase.eq("businedssguid", businessGuid);
                                    sqlSelectPhase.eq("phaseid", phaseid);
                                    // 查询出该主题的某阶段
                                    List<AuditSpPhase> auditSpPhaseList = iAuditSpPhase
                                            .getAuditSpPhase(sqlSelectPhase.getMap()).getResult();
                                    if (auditSpPhaseList != null && !auditSpPhaseList.isEmpty()) {
                                        AuditSpPhase auditSpPhase = auditSpPhaseList.get(0);
                                        if (auditSpPhase != null) {
                                            phaseGuid = auditSpPhase.getRowguid();
                                        }
                                    }
                                }
                            }
                            itemJson.put("businessguid", businessGuid); // 主题guid
                            itemJson.put("hasjianyi", hasJy); // 是否是建议项目
                            itemJson.put("phaseguid", phaseGuid); // 阶段guid
                            itemList.add(itemJson);
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("itemlist", itemList);
                dataJson.put("totalcount", totalCount);
                return JsonUtils.zwdtRestReturn("1", "获取我所在企业下的项目列表成功！", dataJson.toString());
            }else{
                return JsonUtils.zwdtRestReturn("0", "未获取当前用户登录信息", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("getitemlistforyjs 入参：" + params);
            return JsonUtils.zwdtRestReturn("0", "获取项目列表失败：" + e.getMessage(), "");
        }

}

    /**
     * 保存单体接口
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/submitDantiInfo", method = RequestMethod.POST)
    public String submitDantiInfo(@RequestBody String params, @Context HttpServletRequest request) {
        return new DantiBizcommon().saveDantiInfo(params,request,true);
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

//########################  工改一件事 ####################################

}
