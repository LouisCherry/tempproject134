package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
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
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
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
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.zjxl.util.AesDemoUtil;
import com.epoint.zwdt.zwdtrest.bdc.api.IBdcService;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.project.api.IYjsProjectService;
import com.epoint.zwdt.zwdtrest.task.keyword.api.IAuditTaskKeywordService;

import net.sf.json.JSONArray;

/**
 * 并联审批相关接口
 *
 * @version [F9.3, 2017年10月28日]
 * @作者 xli
 */
@RestController
@RequestMapping("/jnzwdtItem")
public class JNAuditOnlineItemController
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
    @Autowired
    private IBdcService iBdcService;
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
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigservice;
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
     * 验证码API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;
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

    @Autowired
    private IJnProjectService iJnProjectService;
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;
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

    @Autowired
    private IYjsProjectService iYjsService;

    @Autowired
    private IAuditTaskKeywordService iAuditTaskKeywordService;

    @RequestMapping(value = "/userDetailDecde", method = RequestMethod.POST)
    public String userDetailDecde(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用userDetailDecde接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String data = jsonObject.getString("data");
                AesDemoUtil aes = new AesDemoUtil();
                String result = aes.encrypt(data.getBytes("UTF-8"));
                JSONObject dataJson = new JSONObject();
                dataJson.put("result", result);
                return JsonUtils.zwdtRestReturn("1", " 用户信息加密成功！", dataJson.toString());
            }
            else {
                log.info("=======结束调用userDetailDecde接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======userDetailDecde接口参数：params【" + params + "】=======");
            log.info("=======userDetailDecde异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "用户信息加密失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getMyCompanyList2", method = RequestMethod.POST)
    public String getMyCompanyList2(@RequestBody String params, @Context HttpServletRequest request) {
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
                    sqlConditionUtil.eq("orgalegal_idnumber", idNumber);
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
                        myCompanyJson.put("rowguid", auditRsCompanyBaseinfo.getRowguid());
                        myCompanyJson.put("corpname", auditRsCompanyBaseinfo.getOrganname());
                        myCompanyJson.put("corpcode", auditRsCompanyBaseinfo.getCreditcode());
                        myCompanyJson.put("legalman", auditRsCompanyBaseinfo.getOrganlegal());
                        String iszjjg = auditRsCompanyBaseinfo.getStr("iszjjg");
                        if (StringUtil.isNotBlank(iszjjg)) {
                            myCompanyJson.put("iszjjg", iszjjg);
                        }
                        else {
                            myCompanyJson.put("iszjjg", "0");
                        }
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
                            myCompanyJson.put("rowguid", auditRsCompanyBaseinfo.getRowguid());
                            myCompanyJson.put("corpname", auditRsCompanyBaseinfo.getOrganname());
                            myCompanyJson.put("corpcode", auditRsCompanyBaseinfo.getCreditcode());
                            myCompanyJson.put("legalman", auditRsCompanyBaseinfo.getOrganlegal());
                            String iszjjg = auditRsCompanyBaseinfo.getStr("iszjjg");
                            if (StringUtil.isNotBlank(iszjjg)) {
                                myCompanyJson.put("iszjjg", iszjjg);
                            }
                            else {
                                myCompanyJson.put("iszjjg", "0");
                            }
                            // 5.1.3、添加信息至返回列表
                            myCompanyList.add(myCompanyJson);
                        }
                    }

                    // 6、返回结果数据
                    JSONObject dataJson = new JSONObject();
                    // 6.1、企业列表数据
                    dataJson.put("companylist", myCompanyList);
                    log.info("=======结束调用getMyCompanyList接口=======");
                    return dataJson.toString();
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
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getMyCompanyList接口参数：params【" + params + "】=======");
            log.info("=======getMyCompanyList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户所在企业信息失败：" + e.getMessage(), "");
        }
    }

    /**********************************************
     * 项目相关接口开始
     **********************************************/
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
                    sqlConditionUtil.eq("orgalegal_idnumber", idNumber);
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
                        myCompanyJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
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
                // 1.3 获取是否工改一件事新增：1-是（itemguid传空）
                String isGgyjsAdd = obj.getString("isggyjsadd");
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
                        if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }

                        // 如果itemguid为空且isggyjsadd=1，则表示工改一件事新增项目入口调用，初始化空的项目实体并标记为一件事兼容原逻辑
                        if (StringUtil.isBlank(itemGuid) && ZwfwConstant.CONSTANT_STR_ONE.equals(isGgyjsAdd)) {
                            auditRsItemBaseinfo = new AuditRsItemBaseinfo();
                            auditRsItemBaseinfo.set("isggyjsadd", ZwfwConstant.CONSTANT_STR_ONE);
                        }
                    }
                    if (auditRsItemBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = null;
                    // 如果是工改一件事新增项目入口调用的初始化空的企业实体兼容原逻辑
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditRsItemBaseinfo.getStr("isggyjsadd"))) {
                        auditRsCompanyBaseinfo = new AuditRsCompanyBaseinfo();
                    }
                    // 正常工改入口的，走原逻辑
                    else {
                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isBlank(itemLegalCreditCode)) {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
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
                                .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap()).getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.isEmpty()) {
                            return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                        }
                        auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 3.1.2.2、 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        // 3.1.2.3、 获取企业id
                        String companyId = auditRsCompanyBaseinfo.getCompanyid();
                        if (!idNum.equals(auditOnlineRegister.getIdnumber())) {
                            // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                            SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                            grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                            grantConditionUtil.eq("companyid", companyId);
                            grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                                                                                 // 0-否，1-是
                            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                                    .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                            if (auditOnlineCompanyGrants == null || auditOnlineCompanyGrants.isEmpty()) {
                                return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                            }
                        }
                    }

                    // 处理返回值
                    JSONObject dataJson = new JSONObject();
                    // 如果biguid不为空，则返回主题
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getBiguid())) {
                        AuditSpInstance auditSpInstance = iAuditSpInstance
                                .getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                        if (auditSpInstance != null) {
                            AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                    .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
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
                    List<CodeItems> constructionpropertys = iCodeItemsService.listCodeItemsByCodeName("建设性质");
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
                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getConstructionproperty())) {
                            objJson.put("isselected", 1);
                        }
                        constructionpropertyList.add(objJson);
                    }
                    dataJson.put("constructionproperty", constructionpropertyList);
                    dataJson.put("itemstartdate", EpointDateUtil
                            .convertDate2String(auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                    dataJson.put("itemfinishdate", EpointDateUtil
                            .convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
                    dataJson.put("totalinvest", auditRsItemBaseinfo.getTotalinvest());
                    dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj());
                    List<CodeItems> belongtindustrys = iCodeItemsService.listCodeItemsByCodeName("所属行业");
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
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getIsimprovement())) {
                                objJson.put("isselected", 1);
                            }
                        }
                        isimprovementList.add(objJson);
                    }
                    dataJson.put("isimprovement", isimprovementList);

                    // 是否测绘多测合一
                    List<JSONObject> iscehuiList = new ArrayList<>();
                    for (CodeItems codeItems : shiFouCode) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", codeItems.getItemValue());
                        objJson.put("itemtext", codeItems.getItemText());
                        // 若是否技改项目为配置，则默认显示否
                        if (StringUtil.isBlank(auditRsItemBaseinfo.getStr("is_cehuiorg"))) {
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                objJson.put("isselected", 1);
                            }
                        }
                        else {
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getStr("is_cehuiorg"))) {
                                objJson.put("isselected", 1);
                            }
                        }
                        iscehuiList.add(objJson);
                    }
                    dataJson.put("iscehui", iscehuiList);

                    /* 3.0新增逻辑 */
                    // 立项类型（2.0）
                    List<CodeItems> lxlxList = iCodeItemsService.listCodeItemsByCodeName("国标_立项类型");
                    List<JSONObject> lxlxObjList = new ArrayList<>();
                    for (CodeItems lxlxItem : lxlxList) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", lxlxItem.getItemValue());
                        objJson.put("itemtext", lxlxItem.getItemText());
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("lxlx"))
                                && lxlxItem.getItemValue().equals(auditRsItemBaseinfo.get("lxlx").toString())) {
                            objJson.put("isselected", 1);
                        }
                        lxlxObjList.add(objJson);
                    }
                    dataJson.put("lxlx", lxlxObjList);

                    // 工程行业分类（2.0）
                    List<CodeItems> gchyflList = iCodeItemsService.listCodeItemsByCodeName("国标_工程行业分类");
                    List<JSONObject> gchyflObjList = new ArrayList<>();
                    for (CodeItems gchyflItem : gchyflList) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", gchyflItem.getItemValue());
                        objJson.put("itemtext", gchyflItem.getItemText());
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("gchyfl"))
                                && gchyflItem.getItemValue().equals(auditRsItemBaseinfo.get("gchyfl").toString())) {
                            objJson.put("isselected", 1);
                        }
                        gchyflObjList.add(objJson);
                    }
                    dataJson.put("gchyfl", gchyflObjList);

                    // 建设地点行政区划
                    List<CodeItems> jsddxzqhCode = iCodeItemsService.listCodeItemsByCodeName("行政区划");
                    List<JSONObject> jsddxzqhList = new ArrayList<>();
                    for (CodeItems codeItem : jsddxzqhCode) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", codeItem.getItemValue());
                        objJson.put("itemtext", codeItem.getItemText());
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("jsddxzqh"))
                                && auditRsItemBaseinfo.getStr("jsddxzqh").contains(",")) {
                            String[] split = auditRsItemBaseinfo.getStr("jsddxzqh").split(",");
                            for (String string : split) {
                                if (codeItem.getItemValue().equals(string)) {
                                    objJson.put("isselected", 1);
                                }
                            }
                        }
                        else {
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("jsddxzqh"))
                                    && codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("jsddxzqh"))) {
                                objJson.put("isselected", 1);
                            }
                        }

                        jsddxzqhList.add(objJson);
                    }
                    dataJson.put("jsddxzqh", jsddxzqhList);

                    List<CodeItems> sfxxgcCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                    List<JSONObject> sfxxgcList = new ArrayList<>();
                    for (CodeItems codeItem : sfxxgcCode) {
                        JSONObject objJson = new JSONObject();
                        objJson.put("itemvalue", codeItem.getItemValue());
                        objJson.put("itemtext", codeItem.getItemText());
                        if (StringUtil.isBlank(auditRsItemBaseinfo.getStr("sfxxgc"))) {
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItem.getItemValue())) {
                                objJson.put("isselected", 1);
                            }
                        }
                        else {
                            if (codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("sfxxgc"))) {
                                objJson.put("isselected", 1);
                            }
                        }
                        sfxxgcList.add(objJson);
                    }
                    dataJson.put("sfxxgc", sfxxgcList);

                    dataJson.put("cd", auditRsItemBaseinfo.getDouble("cd"));
                    dataJson.put("gcfw", auditRsItemBaseinfo.getStr("gcfw"));

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
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getXmtzly())
                                && codeItems.getItemValue().equals(auditRsItemBaseinfo.getXmtzly().toString())) {
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
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getTdhqfs())
                                && codeItems.getItemValue().equals(auditRsItemBaseinfo.getTdhqfs().toString())) {
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
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getTdsfdsjfa().toString())) {
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
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getSfwcqypg().toString())) {
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

                    List<CodeItems> financialresourcess = iCodeItemsService.listCodeItemsByCodeName("财政资金来源");
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
                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getFinancialresources())) {
                            objJson.put("isselected", 1);
                        }
                        financialresourcesList.add(objJson);
                    }
                    dataJson.put("financialresources", financialresourcesList);
                    List<CodeItems> quantifyconstructtypes = iCodeItemsService.listCodeItemsByCodeName("量化建设规模的类别");
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
                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getQuantifyconstructtype())) {
                            objJson.put("isselected", 1);
                        }
                        quantifyconstructtypeList.add(objJson);
                    }
                    dataJson.put("quantifyconstructtype", quantifyconstructtypeList);
                    dataJson.put("quantifyconstructcount", auditRsItemBaseinfo.getQuantifyconstructcount());
                    dataJson.put("quantifyconstructdept", auditRsItemBaseinfo.getQuantifyconstructdept());
                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                    dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                    dataJson.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());
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
                    List<CodeItems> itemlegalcerttypes = iCodeItemsService.listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
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
                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            objJson.put("isselected", 1);
                        }
                        itemlegalcerttypeList.add(objJson);
                    }
                    dataJson.put("itemlegalcerttype", itemlegalcerttypeList);
                    // 国标行业
                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGbhy())) {
                        dataJson.put("gbhy",
                                iCodeItemsService.getItemTextByCodeName("国标行业2017", auditRsItemBaseinfo.getGbhy()));
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

                    dataJson.put("xmzb", auditRsItemBaseinfo.getStr("xmzb"));
                    dataJson.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());
                    dataJson.put("legalperson", auditRsCompanyBaseinfo.getOrganlegal());
                    dataJson.put("legalpersonicardnum", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                    dataJson.put("frphone", auditRsItemBaseinfo.getFrphone());
                    dataJson.put("fremail", auditRsItemBaseinfo.getFremail());
                    dataJson.put("contractperson", auditRsItemBaseinfo.getContractperson());
                    dataJson.put("contractidcart", auditRsItemBaseinfo.getContractidcart());
                    dataJson.put("contractphone", auditRsItemBaseinfo.getContractphone());
                    dataJson.put("contractemail", auditRsItemBaseinfo.getContractemail());
                    log.info("=======结束调用getCompanyItemInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
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
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取项目信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("biguid", biGuid);
                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                        .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                    AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
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
                // 1.5 获取项目（法人）单位
                String itemLegalDept = obj.getString("itemlegaldept");
                // 1.6 获取项目（法人）单位统一社会信用代码
                String itemLegalCreditCode = obj.getString("itemlegalcreditcode");
                // 1.7 获取项目法人证照类型
                String itemLegalCertType = obj.getString("itemlegalcerttype");
                // 1.8 项目法人证照号码
                String itemLegalCertNum = obj.getString("itemlegalcertnum");
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
                // 1.16 获取联系人
                String contractPerson = obj.getString("contractperson");
                // 1.17 获取联系电话
                String contractPhone = obj.getString("contractphone");
                // 1.18 获取联系人邮箱
                String contractEmail = obj.getString("contractemail");
                // 1.19 获取法人性质
                String legalProperty = obj.getString("legalproperty");
                // 1.20 获取用地面积(亩)
                Double landArea = obj.getDouble("landarea");
                // 1.21 获新增用地面积
                Double newLandArea = obj.getDouble("newlandarea");
                // 1.22 获取农用地面积
                Double agriculturalLandArea = obj.getDouble("agriculturallandarea");
                // 1.23 获取项目资本金
                Double itemCapital = obj.getDouble("itemcapital");
                // 1.24 获取资金来源
                String fundSources = obj.getString("fundsources");
                // 1.25 获取财政资金来源
                String financialResources = obj.getString("financialresources");
                // 1.26 获取量化建设规模的类别
                String quantifyConstructType = obj.getString("quantifyconstructtype");
                // 1.27 获取量化建设规模的数值
                Double quantifyConstructCount = obj.getDouble("quantifyconstructcount");
                // 1.28 获取量化建设规模的单位
                String quantifyConstructDept = obj.getString("quantifyconstructdept");
                // 1.29 获取是否技改项目
                String isImprovement = obj.getString("isimprovement");
                // 1.30 获取主题唯一标识
                String businessGuid = obj.getString("businessguid");
                // 1.31 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.32 获取建设单位名称
                String departname = obj.getString("departname");
                // 1.33 获取建设单位地址
                String constructionaddress = obj.getString("constructionaddress");
                // 1.34 获取法人
                String legalperson = obj.getString("legalperson");
                // 1.35 获取法人身份证
                String legalpersonicardnum = obj.getString("legalpersonicardnum");
                // 1.36 获取法人电话
                String frphone = obj.getString("frphone");
                // 1.37 获取法人邮箱
                String fremail = obj.getString("fremail");
                // 1.38 获取联系人身份证
                String contractidcart = obj.getString("contractidcart");
                // 1.39 获取法人性质
                String legalproperty = obj.getString("legalproperty");
                // 1.29 获取是否测绘显示
                String iscehui = obj.getString("iscehui");
                // 1.29 获取是否测绘显示
                String hasyjs = obj.getString("hasyjs");
                // 1.29 获取项目投资来源
                String xmtzly = obj.getString("xmtzly");
                // 1.29 获取土地获取方式
                String tdhqfs = obj.getString("tdhqfs");
                // 1.29 获取土地是否带设计方案
                String tdsfdsjfa = obj.getString("tdsfdsjfa");
                // 1.29 获取是否完成区域评估
                String sfwcqypg = obj.getString("sfwcqypg");
                // 1.29 获取是否完成区域评估
                String gbhy = obj.getString("gbhy");
                // 1.29 获取项目资金属性
                String xmzjsx = obj.getString("xmzjsx");
                // 1.29 获取建筑面积（㎡）
                String jzmj = obj.getString("jzmj");

                String xmzb = obj.getString("xmzb");// 项目坐标

                // 工程行业分类
                String gchyfl = obj.getString("gchyfl");
                // 项目经纬度坐标
                // String xmjwdzb = obj.getString("xmjwdzb");
                // 工程范围
                String gcfw = obj.getString("gcfw");
                // 立项类型
                String lxlx = obj.getString("lxlx");
                // 长度
                String cd = obj.getString("cd");
                // 是否线性工程
                String sfxxgc = obj.getString("sfxxgc");
                // 建设地点行政区划
                String jsddxzqh = obj.getString("jsddxzqh");

                // 获取是否工改一件事新增：1-是（itemguid传空）
                String isGgyjsAdd = obj.getString("isggyjsadd");

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid)
                                .getResult();
                    }
                    // 兼容工改一件事新增项目
                    if (auditRsItemBaseinfo == null && ZwfwConstant.CONSTANT_STR_ONE.equals(isGgyjsAdd)) {
                        // 校验项目代码
                        auditRsItemBaseinfo = new AuditRsItemBaseinfo();
                        auditRsItemBaseinfo.setItemcode(itemCode);
                        if (iAuditRsItemBaseinfo.checkItemCodeRepeat(auditRsItemBaseinfo).getResult() > 0) {
                            return JsonUtils.zwdtRestReturn("0", "项目代码已存在", "");
                        }

                        // 校验建设单位统一社会信用代码是否为该用户企业或该企业的授权用户
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        companySqlConditionUtil.eq("creditcode", itemLegalCreditCode);
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                                .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap()).getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.isEmpty()) {
                            return JsonUtils.zwdtRestReturn("0", "保存失败，您的账号下无此企业", "");
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        if (idNum == null) {
                            idNum = "";
                        }
                        if (!idNum.equals(auditOnlineRegister.getIdnumber())) {
                            // 获取企业id
                            String companyId = auditRsCompanyBaseinfo.getCompanyid();
                            if (companyId == null) {
                                companyId = "";
                            }
                            // 查询该用户是否为该企业的代办人或者管理者
                            SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                            grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                            grantConditionUtil.eq("companyid", companyId);
                            grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                                                                                 // 0-否，1-是
                            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                                    .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                            if (auditOnlineCompanyGrants == null || auditOnlineCompanyGrants.isEmpty()) {
                                return JsonUtils.zwdtRestReturn("0", "保存失败，您的账号下无此企业", "");
                            }
                        }

                        auditRsItemBaseinfo.setRowguid(UUID.randomUUID().toString());
                        auditRsItemBaseinfo.setOperateusername(auditOnlineRegister.getUsername()); // 操作人名称
                        auditRsItemBaseinfo.setItemlegaldept(itemLegalDept); // 建设单位名称
                        auditRsItemBaseinfo.setDepartname(itemLegalDept); // 建设单位名称
                        auditRsItemBaseinfo.setItemlegalcerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM); // 证件类型：16-统一社会信用代码
                        auditRsItemBaseinfo.setItemlegalcreditcode(itemLegalCreditCode); // 统一社会信用代码
                        auditRsItemBaseinfo.setItemlegalcertnum(itemLegalCreditCode); // 证件号码：统一社会信用代码
                        auditRsItemBaseinfo.setLegalperson(auditOnlineRegister.getUsername()); // 法人
                        auditRsItemBaseinfo.setLegalpersonicardnum(auditOnlineRegister.getIdnumber()); // 法人身份证
                        auditRsItemBaseinfo.setContractperson(contractPerson); // 联系人
                        auditRsItemBaseinfo.setContractidcart(contractidcart); // 联系人身份证
                        auditRsItemBaseinfo.setContractphone(contractPhone); // 联系人电话
                        auditRsItemBaseinfo.setContractemail(contractEmail); // 联系人邮箱
                        auditRsItemBaseinfo.setDraft(ZwfwConstant.CONSTANT_STR_ZERO); // 是否草稿：0-正式
                        auditRsItemBaseinfo.set("gdjstatus", ZwfwConstant.CONSTANT_INT_ZERO); // 推送供电局状态：0-未推送
                        auditRsItemBaseinfo.set("jsdwlx", ZwfwConstant.CONSTANT_STR_ONE); // 建设单位类型：1-法人及其他组织
                    }
                    // 工改原逻辑
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
                        auditRsItemBaseinfo.set("xmzb", xmzb);
                        auditRsItemBaseinfo.set("is_cehuiorg", iscehui);

                        auditRsItemBaseinfo.set("xmtzly", xmtzly);
                        auditRsItemBaseinfo.set("tdhqfs", tdhqfs);
                        auditRsItemBaseinfo.set("tdsfdsjfa", tdsfdsjfa);
                        auditRsItemBaseinfo.set("sfwcqypg", sfwcqypg);
                        auditRsItemBaseinfo.set("gbhy", gbhy);
                        auditRsItemBaseinfo.set("xmzjsx", xmzjsx);
                        auditRsItemBaseinfo.set("jzmj", jzmj);

                        // 3.0新增字段
                        auditRsItemBaseinfo.set("lxlx", lxlx);
                        auditRsItemBaseinfo.set("gchyfl", gchyfl);
                        auditRsItemBaseinfo.set("xmjwdzb", xmzb);
                        auditRsItemBaseinfo.set("gcfw", gcfw);
                        auditRsItemBaseinfo.set("sfxxgc", sfxxgc);
                        auditRsItemBaseinfo.set("cd", cd);
                        auditRsItemBaseinfo.set("jsddxzqh", String.valueOf(jsddxzqh));

                        if (StringUtil.isBlank(auditRsItemBaseinfo.getBiguid())) {
                            // 3.2、如果项目没有对应的主题实例，则生成对应主题实例
                            if (StringUtil.isNotBlank(businessGuid)) {
                                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                        .getAuditSpBusinessByRowguid(businessGuid).getResult();
                                if (auditSpBusiness != null) {
                                    String newBiguid = iAuditSpInstance.addBusinessInstance(businessGuid,
                                            auditRsItemBaseinfo.getRowguid(), auditOnlineRegister.getAccountguid(),
                                            auditOnlineRegister.getUsername(), ZwfwConstant.APPLY_WAY_NETSBYS, itemName,
                                            auditSpBusiness.getAreacode(), auditSpBusiness.getBusinesstype())
                                            .getResult();
                                    // 3.2.1、项目管理主题实例
                                    auditRsItemBaseinfo.setBiguid(newBiguid);
                                    // 仅在此处需兼容工改一件事新增项目
                                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isGgyjsAdd)) {
                                        auditRsItemBaseinfo.setBelongxiaqucode(auditSpBusiness.getAreacode()); // 项目所属辖区编码置为主题所在辖区
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    // 更新
                                    else {
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }

                                    // 判断申报数据来源是否为一件事或并联审批
                                    if ("1".equals(hasyjs)) {
                                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(newBiguid)
                                                .getResult();
                                        if (auditSpInstance != null) {
                                            auditSpInstance.set("hasyjs", hasyjs);
                                            iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
                                        }
                                    }

                                    JSONObject dataJson = new JSONObject();
                                    dataJson.put("biguid", newBiguid);
                                    dataJson.put("firstflag", "1");
                                    return JsonUtils.zwdtRestReturn("1", "保存成功", dataJson);
                                }
                            }
                            return JsonUtils.zwdtRestReturn("0", "项目性质选择无效", "");
                        }
                        else {
                            // 3.2、如果项目已有对应的主题实例，则直接更新数据
                            auditRsItemBaseinfo.set("gdjstatus", "0");
                            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            JSONObject dataJson = new JSONObject();
                            dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            dataJson.put("firstflag", "0");
                            return JsonUtils.zwdtRestReturn("1", "修改成功", dataJson);
                        }

                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "保存失败！项目不存在", "");
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
                            if (!auditSpPhase.getPhasename().contains("并行推进")) {
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
                            else {
                                flag = false;
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
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取阶段列表
                SqlConditionUtil sqlCondition = new SqlConditionUtil();
                sqlCondition.eq("businedssguid", businessguid);
                sqlCondition.setOrderAsc("operatedate");
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sqlCondition.getMap()).getResult();
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
                                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(auditSpITask.getProjectguid(),null).getResult();
                                if(auditProject==null){
                                    inNum++;
                                }
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
                AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                String jianyiitemname = iConfigservice.getFrameConfigValue("XM_JIANYI_NAME");
                if (business != null) {
                    if (jianyiitemname.equals(business.getBusinessname())) {
                        dataJson.put("hasjianyi", "yes");
                    }
                }
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
     * 获取阶段及对应的子申报实例信息（查看阶段阶段信息页面）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/newGetPhaseAndSPSubappList", method = RequestMethod.POST)
    public String newGetPhaseAndSPSubappList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取newGetPhaseAndSPSubappList接口=======");
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
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取阶段列表
                SqlConditionUtil sqlCondition = new SqlConditionUtil();
                sqlCondition.eq("businedssguid", businessguid);
                sqlCondition.setOrderAsc("operatedate");
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sqlCondition.getMap()).getResult();
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
                            if (StringUtil.isNotBlank(auditSpITask.getStatus())
                                    && !"0".equals(auditSpITask.getStatus())) {
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
                AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                String jianyiitemname = iConfigservice.getFrameConfigValue("XM_JIANYI_NAME");
                if (business != null) {
                    if (jianyiitemname.equals(business.getBusinessname())) {
                        dataJson.put("hasjianyi", "yes");
                    }
                }
                dataJson.put("phaselist", phaseJsonList);
                log.info("=======结束调用newGetPhaseAndSPSubappList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取阶段及对应的子申报实例信息成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======newGetPhaseAndSPSubappList接口参数：params【" + params + "】=======");
            log.info("=======newGetPhaseAndSPSubappList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取阶段及对应的子申报实例信息异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getPhaseAndSPSubappListYt", method = RequestMethod.POST)
    public String getPhaseAndSPSubappListYt(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getPhaseAndSPSubappListYt接口=======");
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
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 3、获取阶段列表
                SqlConditionUtil sqlCondition = new SqlConditionUtil();
                sqlCondition.eq("businedssguid", businessguid);
                sqlCondition.setOrderDesc("ordernumber");
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sqlCondition.getMap()).getResult();
                // 查询实例
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();

                List<JSONObject> lxpfstages1 = new ArrayList<JSONObject>();
                List<JSONObject> lxpfstages2 = new ArrayList<JSONObject>();
                List<JSONObject> lxpfstages3 = new ArrayList<JSONObject>();
                List<JSONObject> lxpfstages4 = new ArrayList<JSONObject>();
                String lxpfcliengguid1 = auditSpInstance.getStr("lxpfliengguid1");
                String lxpfcliengguid2 = auditSpInstance.getStr("lxpfliengguid2");
                String lxpfcliengguid3 = auditSpInstance.getStr("lxpfliengguid3");
                String lxpfcliengguid4 = auditSpInstance.getStr("lxpfliengguid4");

                String url = ConfigUtil.getConfigValue("officeweb365url");
                String documentType = "";

                if (StringUtil.isNotBlank(lxpfcliengguid1)) {
                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid1);
                    if (lxpfAttachCount > 0) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                .getAttachInfoListByGuid(lxpfcliengguid1);
                        for (FrameAttachInfo attach : frameAttachInfos) {
                            JSONObject jsonattach = new JSONObject();
                            documentType = attach.getContentType();
                            String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + attach.getAttachGuid();
                            String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                            String attachurl = url + encryptUrl;
                            jsonattach.put("lxpfattachguid",
                                    "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.getAttachGuid());
                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                            jsonattach.put("365attachurl", attachurl);
                            lxpfstages1.add(jsonattach);
                        }
                    }
                }

                if (StringUtil.isNotBlank(lxpfcliengguid2)) {
                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid2);
                    if (lxpfAttachCount > 0) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                .getAttachInfoListByGuid(lxpfcliengguid2);
                        for (FrameAttachInfo attach : frameAttachInfos) {
                            documentType = attach.getContentType();
                            // url = url + "fname=" + attach.getAttachFileName()
                            // + "&";
                            String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + attach.getAttachGuid();
                            String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                            String attachurl = url + encryptUrl;
                            JSONObject jsonattach = new JSONObject();
                            jsonattach.put("lxpfattachguid",
                                    "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.getAttachGuid());
                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                            jsonattach.put("365attachurl", attachurl);
                            lxpfstages2.add(jsonattach);
                        }
                    }
                }

                if (StringUtil.isNotBlank(lxpfcliengguid3)) {
                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid3);
                    if (lxpfAttachCount > 0) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                .getAttachInfoListByGuid(lxpfcliengguid3);
                        for (FrameAttachInfo attach : frameAttachInfos) {
                            documentType = attach.getContentType();
                            // url = url + "fname=" + attach.getAttachFileName()
                            // + "&";
                            String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + attach.getAttachGuid();
                            String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                            String attachurl = url + encryptUrl;
                            JSONObject jsonattach = new JSONObject();
                            jsonattach.put("lxpfattachguid",
                                    "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.getAttachGuid());
                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                            jsonattach.put("365attachurl", attachurl);
                            lxpfstages3.add(jsonattach);
                        }
                    }
                }

                if (StringUtil.isNotBlank(lxpfcliengguid4)) {
                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid4);
                    if (lxpfAttachCount > 0) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                .getAttachInfoListByGuid(lxpfcliengguid4);
                        for (FrameAttachInfo attach : frameAttachInfos) {
                            documentType = attach.getContentType();
                            String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + attach.getAttachGuid();
                            String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
                            String attachurl = url + encryptUrl;
                            JSONObject jsonattach = new JSONObject();
                            jsonattach.put("lxpfattachguid",
                                    "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.getAttachGuid());
                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                            jsonattach.put("365attachurl", attachurl);
                            lxpfstages4.add(jsonattach);
                        }
                    }
                }

                for (AuditSpPhase auditSpPhase : auditSpPhases) {
                    // 过滤第五阶段，并行推进
                    if ("5".equals(auditSpPhase.getPhaseId())) {
                        continue;
                    }
                    JSONObject phaseObject = new JSONObject();
                    String phasename = auditSpPhase.getPhasename();
                    phaseObject.put("phasename", phasename);

                    if ("立项用地规划许可".equals(phasename)) {
                        phaseObject.put("lxpfattach", lxpfstages1);
                    }
                    else if ("工程建设许可".equals(phasename)) {
                        phaseObject.put("lxpfattach", lxpfstages2);
                    }
                    else if ("施工许可".equals(phasename)) {
                        phaseObject.put("lxpfattach", lxpfstages3);
                    }
                    else if ("竣工验收".equals(phasename)) {
                        phaseObject.put("lxpfattach", lxpfstages4);
                    }
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
                AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                String jianyiitemname = iConfigservice.getFrameConfigValue("XM_JIANYI_NAME");
                if (business != null) {
                    if (jianyiitemname.equals(business.getBusinessname())) {
                        dataJson.put("hasjianyi", "yes");
                    }
                }
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
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByOneField("creditcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum())
                                .getResult();
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
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByOneField("creditcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum())
                                .getResult();
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
                                                                         // TODO
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
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                if (ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus())) {
                    String biGuid = auditSpISubapp.getBiguid();
                    // 2.1、查询申报实例信息
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    String businessGuid = auditSpInstance.getBusinessguid();
                    String companyId = auditSpISubapp.getApplyerguid();
                    String phaseGuid = auditSpISubapp.getPhaseguid();
                    // 2.2、查询申报项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();

                    if (auditRsItemBaseinfo != null) {
                        String lxpfcliengguid = auditRsItemBaseinfo.getStr("lxpfcliengguid");
                        if (StringUtil.isNotBlank(lxpfcliengguid)) {
                            int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid);
                            if (lxpfAttachCount > 0) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(lxpfcliengguid);
                                dataJson.put("lxpfattachguid",
                                        "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + frameAttachInfos.get(0).getAttachGuid());
                                dataJson.put("lxpfattachname", frameAttachInfos.get(0).getAttachFileName());

                            }
                        }
                    }
                    // 2.3 如果材料实例没有初始化，则初始化材料
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                        iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, companyId,
                                auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                    }
                    // 3、获取子申报事项实例列表
                    List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid)
                            .getResult();
                    List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                    for (AuditSpITask auditSpITask : auditSpITasks) {
                        // 4、返回阶段实例中的事项列表
                        JSONObject spiTaskJson = new JSONObject();
                        spiTaskJson.put("taskname", auditSpITask.getTaskname());
                        spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                        // 5、返回该事项下的材料列表
                        List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                        // 5.1、获取事项下的材料列表
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                        // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject spiMaterialJson = new JSONObject();
                            // 5.3、获取事项实例中的材料实例信息
                            AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                    .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                    .getResult();
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
                                    if (ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                        continue;
                                    }
                                }
                                else {
                                    // 没有找到共享材料数据，结束该循环
                                    continue;
                                }
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
                            spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                            spiMaterialJson.put("materialinstanceguid", auditSpIMaterial.getMaterialguid());
                            spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                            // 是否共享材料
                            spiMaterialJson.put("shared", auditSpIMaterial.getShared());
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

                            List<JSONObject> resultattach = new ArrayList<JSONObject>();

                            if (StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                                int AttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                                if (AttachCount > 0) {
                                    List<FrameAttachInfo> frameAttachInfoss = iAttachService
                                            .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                    for (FrameAttachInfo attach : frameAttachInfoss) {
                                        JSONObject jsonattach = new JSONObject();
                                        jsonattach.put("resultattachguid",
                                                "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + attach.getAttachGuid());
                                        jsonattach.put("resultattachname", attach.getAttachFileName());
                                        resultattach.add(jsonattach);
                                    }
                                }
                            }

                            int showButton = 0; // 按钮显示方式
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                    ? "1"
                                    : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                            if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                                // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                    // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.6.2、如果关联了证照库
                                if (count > 0) {
                                    // 4.6.2.1、获取材料类别
                                    String materialType = auditTaskMaterial.get("materialType");
                                    if (materialType == null) {
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
                            spiMaterialJson.put("resultattach", resultattach);
                            spiMaterialJson.put("showbutton", showButton);
                            spiMaterialJson.put("needload", needLoad);
                            spiMaterialJsonList.add(spiMaterialJson);
                        }
                        // 4、存入事项下的材料列表
                        spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                        spiTaskJsonList.add(spiTaskJson);
                    }

                    dataJson.put("spitasklist", spiTaskJsonList);
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
     * 获取事项及材料清单接口（对子申报实例点击上传材料按钮后加载的页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSPTaskAndMaterialInstanceListYt", method = RequestMethod.POST)
    public String getSPTaskAndMaterialInstanceListYt(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSPTaskAndMaterialInstanceListYt接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();

            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
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
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 4、返回阶段实例中的事项列表
                    JSONObject spiTaskJson = new JSONObject();
                    spiTaskJson.put("taskname", auditSpITask.getTaskname());
                    spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                    // 5、返回该事项下的材料列表
                    List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                    // 5.1、获取事项下的材料列表
                    List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                            .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                    // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                        JSONObject spiMaterialJson = new JSONObject();
                        // 5.3、获取事项实例中的材料实例信息
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                .getResult();
                        // 4.11、 判断材料来源
                        String materialsource = auditTaskMaterial.getFile_source();
                        // 5.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                        if (auditSpIMaterial == null) {
                            AuditSpShareMaterialRelation auditSpShareMaterialRelation = iAuditSpShareMaterialRelation
                                    .getRelationByID(auditSpITask.getBusinessguid(), auditTaskMaterial.getMaterialid())
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
                                if (ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                    continue;
                                }
                            }
                            else {
                                // 没有找到共享材料数据，结束该循环
                                continue;
                            }
                        }

                        List<JSONObject> resultattach = new ArrayList<JSONObject>();
                        String url = ConfigUtil.getConfigValue("officeweb365url");
                        String documentType = "";

                        if (StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                            int AttachCount = iAttachService
                                    .getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                            if (AttachCount > 0) {
                                List<FrameAttachInfo> frameAttachInfoss = iAttachService
                                        .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                for (FrameAttachInfo attach : frameAttachInfoss) {
                                    documentType = attach.getContentType();
                                    String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.getAttachGuid();
                                    String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl,
                                            documentType);
                                    String attachurl = url + encryptUrl;
                                    JSONObject jsonattach = new JSONObject();
                                    jsonattach.put("resultattachguid",
                                            "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                    + attach.getAttachGuid());
                                    jsonattach.put("resultattachname", attach.getAttachFileName());
                                    jsonattach.put("365attachurl", attachurl);
                                    resultattach.add(jsonattach);
                                }
                            }
                            else {
                                continue;
                            }
                        }
                        else {
                            continue;
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
                        spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                        spiMaterialJson.put("materialinstanceguid", auditSpIMaterial.getMaterialguid());
                        spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                        // 是否共享材料
                        spiMaterialJson.put("shared", auditSpIMaterial.getShared());
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
                        String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel")) ? "1"
                                : "0"; // 是否需要上传到证照库 0:不需要 、1:需要
                        if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                            // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = count > 0 ? 4 : 3;
                            }
                            else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = count > 0 ? 1 : 0;
                            }
                        }
                        else {
                            // 4.6.2、如果关联了证照库
                            if (count > 0) {
                                // 4.6.2.1、获取材料类别
                                String materialType = auditTaskMaterial.get("materialType");
                                if (materialType == null) {
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
                        spiMaterialJson.put("resultattach", resultattach);
                        spiMaterialJson.put("showbutton", showButton);
                        spiMaterialJson.put("needload", needLoad);
                        spiMaterialJsonList.add(spiMaterialJson);
                    }
                    // 4、存入事项下的材料列表
                    spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                    spiTaskJsonList.add(spiTaskJson);
                }

                dataJson.put("spitasklist", spiTaskJsonList);
                log.info("=======结束调用getSPTaskAndMaterialInstanceListYt接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项及材料清单成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSPTaskAndMaterialInstanceListYt接口异常信息：" + e.getMessage() + "=======");
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
                // 3、获取子申报事项实例列表
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 3.1、返回阶段实例中的事项列表
                    JSONObject spiTaskJson = new JSONObject();
                    spiTaskJson.put("taskname", auditSpITask.getTaskname());
                    // 3.2、返回该事项下的材料列表
                    List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                    // 3.3、返回该事项下的补正材料列表
                    List<JSONObject> bzSpiMaterialJsonList = new ArrayList<JSONObject>();
                    // 4、获取事项信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), false).getResult();
                    // 4.1、获取办件信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("subappguid", subAppGuid);
                    sqlConditionUtil.eq("taskguid", auditTask.getRowguid());
                    sqlConditionUtil.eq("areacode", auditTask.getAreacode());
                    List<AuditProject> auditProjects = iAuditProject
                            .getAuditProjectListByCondition(sqlConditionUtil.getMap()).getResult();
                    if (auditProjects != null && auditProjects.size() > 0) {
                        spiTaskJson.put("projectstatus", auditProjects.get(0).getStatus());
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
                                if (ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                    continue;
                                }
                            }
                            else {
                                // 3.11没有找到共享材料数据，结束该循环
                                continue;
                            }
                        }
                        spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                        if (StringUtil.isBlank(materialsource)) {
                            materialsource = "申请人自备";
                        }
                        else {
                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                        }
                        // TODO 与审批系统确认代码项
                        if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                            JSONObject bzSpiMaterialJson = new JSONObject();
                            bzSpiMaterialJson.put("bzmaterialinstancename", auditTaskMaterial.getMaterialname());
                            bzSpiMaterialJson.put("materialsource", materialsource);
                            bzSpiMaterialJson.put("remark", auditSpIMaterial.getStr("remark"));
                            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                            // 仅当子申报状态为待补正时，才返回待补正状态 28
                            if (ZwfwConstant.LHSP_Status_YSDBZ.equals(auditSpISubapp.getStatus())
                                    || ZwfwConstant.LHSP_Status_DBJ.equals(auditSpISubapp.getStatus())) {
                                spiTaskJson.put("projectstatus", 28);
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
                                if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
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
                            bzSpiMaterialJson.put("remark", auditSpIMaterial.getStr("remark"));
                            bzSpiMaterialJson.put("showbutton", showButton);
                            bzSpiMaterialJson.put("needload", needLoad);
                            bzSpiMaterialJsonList.add(bzSpiMaterialJson);
                        }
                        else {
                            spiMaterialJson.put("materialsource", materialsource);
                            spiMaterialJsonList.add(spiMaterialJson);
                        }
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
                    String strWhereCompanyId = "";// 拼接被授权的所有企业id
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                    }
                    // 2.2、如果当前登陆人是法人，则没有授权信息。需要查法人身份证所属的企业
                    SqlConditionUtil sqlLegal = new SqlConditionUtil();
                    sqlLegal.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlLegal.isBlankOrValue("is_history", "0");
                    sqlLegal.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlLegal.getMap()).getResult();
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        strWhereCompanyId += "'" + auditRsCompanyBaseinfo.getCompanyid() + "',";
                    }
                    if (StringUtil.isBlank(strWhereCompanyId)) {
                        // 2.2.1 如果登陆人没有被授权的企业或被解除授权，则提示“无权”
                        return JsonUtils.zwdtRestReturn("0", "您无权查询项目", "");
                    }
                    else {
                        // 2.2.1 去除字段的最后一个逗号
                        strWhereCompanyId = strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1);
                    }
                    SqlConditionUtil sqlSelectAllSubapps = new SqlConditionUtil();
                    // 2.3 拼接查询条件
                    sqlSelectAllSubapps.in("applyerguid", strWhereCompanyId);
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlSelectAllSubapps.like("subappname", keyWord);
                    }
                    if (StringUtil.isNotBlank(status)) {
                        if (ZwfwConstant.LHSP_Status_DBJ.equals(status)) {
                            status = "'" + ZwfwConstant.LHSP_Status_DBJ + "','" + ZwfwConstant.LHSP_Status_YSDBZ + "'";
                            sqlSelectAllSubapps.in("status", status);
                        }
                        else {
                            sqlSelectAllSubapps.eq("status", status);
                        }
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
                        objJson.put("itemguid", auditSpISubapp.getYewuguid());
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
            return JsonUtils.zwdtRestReturn("0", " 获取项目列表失败：" + e.getMessage(), "");
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
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    if (auditSpInstance != null) {
                        AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("name", auditSpBusiness.getBusinessname());
                        dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                        dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        dataJson.put("startdate",
                                EpointDateUtil.convertDate2String(auditSpInstance.getCreatedate(), "y年M月d日") + "-当前");
                        int days = (int) ((new Date().getTime() - auditSpInstance.getCreatedate().getTime())
                                / (1000 * 3600 * 24));
                        dataJson.put("days", days);
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("businedssguid", auditSpBusiness.getRowguid());
                        sql.setOrder("ordernumber", "desc");
                        List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
                        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp
                                .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
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
                        SqlConditionUtil sqlSelectProject = new SqlConditionUtil();
                        sqlSelectProject.eq("areacode", auditSpBusiness.getAreacode());
                        sqlSelectProject.eq("biguid", biGuid);
                        Map<String, Object> taskMap = new HashMap<>();
                        List<AuditProject> auditProjects = iAuditProject.getAuditProjectListByCondition(
                                "task_id,status,projectname,ouname,banjiedate,operatedate", sqlSelectProject.getMap())
                                .getResult();
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
                        int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
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
                            String paperUnnecrsstity = iConfigservice.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
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
     * 提交套餐办件接口
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
                // 4、更新子申报状态为待预审
                iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
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
                        String paperUnnecrsstity = iConfigservice.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
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
                }
                // 4、更新所有的材料状态为已补正
                iAuditSpIMaterial.updateSpIMaterialPatchStatus(subAppGuid);
                log.info("=======结束调用submitSubappBZMaterial接口=======");
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
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
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
     * 根据项目代码或者项目标识获取项目信息（点击项目查看或者申报按钮时调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getZjcsItemInfoByItemcode", method = RequestMethod.POST)
    public String getZjcsItemInfoByItemcode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getZjcsItemInfoByItemcode接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 3、 获取项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("itemcode", itemCode);
                sqlConditionUtil.isBlankOrValue("is_history", "0");
                // 3.1、根据项目代码和项目名称查询项目信息
                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                        .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditRsItemBaseinfos.size() > 0) {
                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                }
                if (auditRsItemBaseinfo != null) {
                    JSONObject dataJson = new JSONObject();
                    // 3.1.1 获取该项目的申报单位的统一社会信用代码
                    String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                    if (StringUtil.isNotBlank(itemLegalCreditCode)) {
                        dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                        dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        dataJson.put("itemtype", auditRsItemBaseinfo.getItemtype());
                        dataJson.put("constructionproperty", auditRsItemBaseinfo.getConstructionproperty());
                        dataJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                        dataJson.put("itemlegalcerttype", auditRsItemBaseinfo.getItemlegalcerttype());
                        dataJson.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());
                        dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(
                                auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_TIME_FORMAT));
                        dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(
                                auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_TIME_FORMAT));
                        dataJson.put("totalinvest", auditRsItemBaseinfo.getTotalinvest());
                        dataJson.put("belongtindustry", auditRsItemBaseinfo.getBelongtindustry());
                        dataJson.put("contractperson", auditRsItemBaseinfo.getContractperson());
                        dataJson.put("contractphone", auditRsItemBaseinfo.getContractphone());
                        dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
                        dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                        dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                        dataJson.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());
                        log.info("=======结束调用getZjcsItemInfoByItemcode接口=======");
                        return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
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
                log.info("=======结束调用getZjcsItemInfoByItemcode接口=======");
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

    @RequestMapping(value = "/getItemListMaterial", method = RequestMethod.POST)
    public String getItemListMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemListMaterial接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject dataJson = new JSONObject();
            List<JSONObject> resultattach = new ArrayList<JSONObject>();
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1.3、获取一页显示数量
                String pageSize = "200";
                // 1.4、获取当前页数
                String currentPage = "0";
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String CreditCode = obj.getString("creditCode");
                if (StringUtil.isNotBlank(CreditCode)) {
                    List<JSONObject> lxpfstages1 = new ArrayList<JSONObject>();
                    List<JSONObject> lxpfstages2 = new ArrayList<JSONObject>();
                    List<JSONObject> lxpfstages3 = new ArrayList<JSONObject>();
                    List<JSONObject> lxpfstages4 = new ArrayList<JSONObject>();

                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("itemlegalcertnum", CreditCode);
                    conditionUtil.isBlank("parentid");
                    // 3、 获取项目信息
                    PageData<AuditRsItemBaseinfo> auditRsItemBaseinfosPageData = iAuditRsItemBaseinfo
                            .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(),
                                    Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize), "", "")
                            .getResult();
                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
                    if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                        // 3.1、 将项目信息返回
                        for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {

                            String biGuid = auditRsItemBaseinfo.getBiguid();

                            // 根据申报实例查询套餐阶段
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();

                            if (auditSpInstance != null) {

                                String lxpfcliengguid1 = auditSpInstance.getStr("lxpfliengguid1");
                                String lxpfcliengguid2 = auditSpInstance.getStr("lxpfliengguid2");
                                String lxpfcliengguid3 = auditSpInstance.getStr("lxpfliengguid3");
                                String lxpfcliengguid4 = auditSpInstance.getStr("lxpfliengguid4");

                                if (StringUtil.isNotBlank(lxpfcliengguid1)) {
                                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid1);
                                    if (lxpfAttachCount > 0) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(lxpfcliengguid1);
                                        for (FrameAttachInfo attach : frameAttachInfos) {
                                            JSONObject jsonattach = new JSONObject();
                                            jsonattach.put("lxpfattachguid", attach.getAttachGuid());
                                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                                            lxpfstages1.add(jsonattach);
                                        }
                                    }
                                }

                                if (StringUtil.isNotBlank(lxpfcliengguid2)) {
                                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid2);
                                    if (lxpfAttachCount > 0) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(lxpfcliengguid2);
                                        for (FrameAttachInfo attach : frameAttachInfos) {
                                            JSONObject jsonattach = new JSONObject();
                                            jsonattach.put("lxpfattachguid", attach.getAttachGuid());
                                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                                            lxpfstages2.add(jsonattach);
                                        }
                                    }
                                }

                                if (StringUtil.isNotBlank(lxpfcliengguid3)) {
                                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid3);
                                    if (lxpfAttachCount > 0) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(lxpfcliengguid3);
                                        for (FrameAttachInfo attach : frameAttachInfos) {
                                            JSONObject jsonattach = new JSONObject();
                                            jsonattach.put("lxpfattachguid", attach.getAttachGuid());
                                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                                            lxpfstages3.add(jsonattach);
                                        }
                                    }
                                }

                                if (StringUtil.isNotBlank(lxpfcliengguid4)) {
                                    int lxpfAttachCount = iAttachService.getAttachCountByClientGuid(lxpfcliengguid4);
                                    if (lxpfAttachCount > 0) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(lxpfcliengguid4);
                                        for (FrameAttachInfo attach : frameAttachInfos) {
                                            JSONObject jsonattach = new JSONObject();
                                            jsonattach.put("lxpfattachguid", attach.getAttachGuid());
                                            jsonattach.put("lxpfattachname", attach.getAttachFileName());
                                            lxpfstages4.add(jsonattach);
                                        }
                                    }
                                }

                            }

                            if (auditSpInstance != null) {
                                String businessguid = auditSpInstance.getBusinessguid();// 主题guid
                                SqlConditionUtil sqlSelectPhase = new SqlConditionUtil();
                                sqlSelectPhase.eq("businedssguid", businessguid);
                                sqlSelectPhase.setOrderDesc("ordernumber");
                                // 查询出该主题的所有阶段
                                List<AuditSpPhase> auditSpPhaseList = iAuditSpPhase
                                        .getAuditSpPhase(sqlSelectPhase.getMap()).getResult();

                                for (AuditSpPhase auditSpPhase : auditSpPhaseList) {

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
                                    List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp
                                            .getSubappListByMap(sqlConditionUtil.getMap()).getResult();
                                    for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {

                                        String subAppGuid = auditSpISubapp.getRowguid();

                                        // 3、获取子申报事项实例列表
                                        List<AuditSpITask> auditSpITasks = iAuditSpITask
                                                .getTaskInstanceBySubappGuid(subAppGuid).getResult();
                                        for (AuditSpITask auditSpITask : auditSpITasks) {
                                            String taskname = auditSpITask.getTaskname();

                                            if (!"建设工程规划许可证".equals(taskname)) {
                                                continue;
                                            }

                                            List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                                    .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false)
                                                    .getResult();
                                            // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                                            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                                // 5.3、获取事项实例中的材料实例信息
                                                AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                                        .getSpIMaterialByMaterialGuid(subAppGuid,
                                                                auditTaskMaterial.getMaterialid())
                                                        .getResult();
                                                if (auditSpIMaterial == null) {
                                                    continue;
                                                }

                                                String materialname = auditTaskMaterial.getMaterialname();

                                                if ("土地证明文件及附图".equals(materialname)) {
                                                    if (StringUtil.isNotBlank(auditSpIMaterial.getCliengguid())) {
                                                        int AttachCount = iAttachService.getAttachCountByClientGuid(
                                                                auditSpIMaterial.getCliengguid());
                                                        if (AttachCount > 0) {
                                                            List<FrameAttachInfo> frameAttachInfoss = iAttachService
                                                                    .getAttachInfoListByGuid(
                                                                            auditSpIMaterial.getCliengguid());
                                                            for (FrameAttachInfo attach : frameAttachInfoss) {
                                                                JSONObject jsonattach = new JSONObject();
                                                                jsonattach.put("resultattachguid",
                                                                        attach.getAttachGuid());
                                                                jsonattach.put("resultattachname",
                                                                        attach.getAttachFileName());
                                                                resultattach.add(jsonattach);
                                                            }
                                                        }
                                                        else {
                                                            continue;
                                                        }
                                                    }
                                                    else {
                                                        continue;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        dataJson.put("lxpfstages1", lxpfstages1);
                        dataJson.put("lxpfstages2", lxpfstages2);
                        dataJson.put("lxpfstages3", lxpfstages3);
                        dataJson.put("lxpfstages4", lxpfstages4);

                    }
                }

                dataJson.put("resultlist", resultattach);
                log.info("=======结束调用getItemListMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取我所在企业下的材料列表成功！", dataJson.toString());
            }
            else {
                log.info("=======结束调用getItemListMaterial接口=======");
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
     * 获取我的企业下的项目列表（选择项目弹窗选择企业后筛选调用）
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getRealEstateDetail", method = RequestMethod.POST)
    public String getRealEstateDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getRealEstateDetail接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 获取年份
                String bdcnftxt = obj.getString("bdcnftxt");
                // 1.1、 获取不动产权编号
                String bdcyhtxt = obj.getString("bdcyhtxt");

                String ggareacode = obj.getString("ggareacode");

                String data = "鲁（" + bdcnftxt + "）" + ggareacode + "不动产权第" + bdcyhtxt + "号";

                Record bdcdetail = iBdcService.getBdcDetailByBdcdyh(data);
                if (bdcdetail != null) {
                    dataJson.put("itemAddress", bdcdetail.getStr("ZL"));
                    dataJson.put("userterm", bdcdetail.getStr("SYQX"));
                    dataJson.put("areaUsed", bdcdetail.getStr("MJ"));
                    dataJson.put("bdcdanyuanhao", bdcdetail.getStr("BDCDYH"));
                    dataJson.put("areaSources", bdcdetail.getStr("FWQLXZ"));
                    dataJson.put("useLandType", bdcdetail.getStr("FWYTMC"));
                    return JsonUtils.zwdtRestReturn("1", "获取不动产信息成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "未查询到该信息！", data);
                }
            }
            else {
                log.info("=======结束调用getRealEstateDetail接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getRealEstateDetail接口参数：params【" + params + "】=======");
            log.info("=======getRealEstateDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取不动产信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取一件事关键字列表
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getItemListByItemName", method = RequestMethod.POST)
    public String getItemListByItemName(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getItemListByItemName接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 关键字
                String businessname = obj.getString("keyword");
                String keyword = obj.getString("keyword");
                // 1.1、页数
                int currentpage = obj.getInteger("currentpage");
                // 1.3 每页展示多少
                int pagesize = obj.getInteger("pagesize");
                String name = "";
                if (StringUtil.isNotBlank(businessname)) {
                    businessname = businessname.replaceAll("%", "");
                    String[] b = businessname.split("");
                    for (int i = 0; i < b.length; i++) {
                        if (i != b.length - 1) {
                            name += b[i] + "%";
                        }
                        else {
                            name += b[i];
                        }
                    }
                }

                Record record = new Record();
                record.set("businessname", name);
                record.set("keyword", keyword);
                PageData<AuditSpBusiness> business = iYjsService.getAuditSpBusinessByPage(record, currentpage,
                        pagesize);
                int total = business.getRowCount();
                List<AuditSpBusiness> busess = business.getList();
                List<JSONObject> TaskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表

                for (AuditSpBusiness bus : busess) {
                    JSONObject data = new JSONObject();
                    data.put("businessname", bus.getBusinessname());
                    data.put("businessguid", bus.getRowguid());
                    List<JSONObject> arealist = new ArrayList<>();
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("辖区对应关系");
                    for (CodeItems codeitem : codeItems) {
                        JSONObject area = new JSONObject();
                        area.put("areaname", codeitem.getItemText());
                        area.put("addrGuid", codeitem.getItemValue());
                        boolean is_highlight = iYjsService.Ishighlight(codeitem.getItemValue(), bus.getBusinessname());
                        area.put("not_allow", is_highlight);
                        arealist.add(area);
                    }
                    data.put("addList", arealist);
                    TaskJsonList.add(data);
                }

                dataJson.put("yjslist", TaskJsonList);
                dataJson.put("total", total);
                return JsonUtils.zwdtRestReturn("1", "获取一件事列表信息成功！", dataJson.toString());
            }
            else {
                log.info("=======结束调用getItemListByItemName接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getItemListByItemName接口参数：params【" + params + "】=======");
            log.info("=======getItemListByItemName异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取一件事关键字列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取搜索页弹窗数据
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getYjsPopInfo", method = RequestMethod.POST)
    public String getYjsPopInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsPopInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskname = obj.getString("taskname");
                String areacode = obj.getString("areacode");
                JSONObject datajson = new JSONObject();
                JSONObject info = new JSONObject();
                if (StringUtil.isNotBlank(areacode)) {
                    // 查找区一级 一件事
                    if (areacode.length() == 6) {
                        AuditSpBusiness spBusiness = iYjsService.getBusinessByNameAndAreacode(taskname, areacode);
                        if (spBusiness != null) {
                            info.put("businessname", spBusiness.getBusinessname());
                            info.put("areacode", spBusiness.getAreacode());
                            info.put("businessguid", spBusiness.getRowguid());
                            datajson.put("info", info);
                        }
                        // 同时找到辖区开头为这个的所有镇
                        List<JSONObject> arealist = new ArrayList<>();
                        List<AuditOrgaArea> orgaAreas = iAuditTaskKeywordService
                                .getOrgaAreaListByAreacodeAndType(areacode, "1");
                        for (AuditOrgaArea auditOrgaArea : orgaAreas) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", auditOrgaArea.getXiaquname());
                            area.put("addrGuid", auditOrgaArea.getXiaqucode());
                            boolean is_highlight = iYjsService.IshighlightTown(auditOrgaArea.getXiaqucode(), taskname);
                            area.put("not_allow", is_highlight);
                            arealist.add(area);
                        }
                        datajson.put("addList", arealist);
                    }
                    // 镇
                    else if (areacode.length() == 9) {
                        AuditSpBusiness spBusiness = iYjsService.getBusinessByNameAndAreacode(taskname, areacode);
                        if (spBusiness != null) {
                            info.put("businessname", spBusiness.getBusinessname());
                            info.put("areacode", spBusiness.getAreacode());
                            info.put("businessguid", spBusiness.getRowguid());
                            datajson.put("info", info);
                        }
                    }
                }
                log.info("=======结束调用getYjsPopInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取弹窗信息成功", datajson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYjsPopInfo接口参数：params【" + params + "】=======");
            log.info("=======getYjsPopInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据项目代码获取相关办件的所有材料
     *
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMaterialsByItemcode", method = RequestMethod.POST)
    public String getMaterialsByItemcode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMaterialsByItemcode接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.1 获取项目代码
                String certname = obj.getString("certname");
                // 3.1、根据项目代码和项目名称查询项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByItemcode(itemCode).getResult();
                List<JSONObject> resultlist = new ArrayList<JSONObject>();
                String url = ConfigUtil.getConfigValue("officeweb365url");
                String documentType = ".pdf";

                if (auditRsItemBaseinfo != null) {
                    List<Record> subapps = iJnProjectService.getBiguidsByYewuguid(auditRsItemBaseinfo.getRowguid());
                    if (!subapps.isEmpty()) {
                        for (Record subapp : subapps) {
                            List<AuditProject> projects = iJnProjectService
                                    .selectAuditProjectByBiguid(subapp.getStr("biguid"));
                            for (AuditProject project : projects) {
                                if (StringUtil.isNotBlank(project.getCertrowguid())
                                        && (project.getProjectname().contains("建设工程竣工规划核实")
                                                || project.getProjectname().contains("建设工程（含地下管线工程）档案验收")
                                                || project.getProjectname().contains("建筑工程施工许可证核发")
                                                || project.getProjectname().contains("建设工程规划许可证")
                                                || project.getProjectname().contains("建设用地规划许可证")
                                                || project.getProjectname().contains("商品房预售许可"))) {
                                    CertInfo certInfo = iCertInfoExternal
                                            .getCertInfoByRowguid(project.getCertrowguid());
                                    if (certInfo != null) {
                                        if (certInfo.getCertname().equals(certname)) {
                                            // 5、获取结果对应的附件
                                            List<JSONObject> attachList = iCertAttachExternal.getAttachList(
                                                    certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                            if (attachList != null && attachList.size() > 0) {
                                                for (JSONObject attachJson : attachList) {
                                                    JSONObject resultJson = new JSONObject();
                                                    resultJson.put("certname", certInfo.getCertname());
                                                    String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                            + attachJson.getString("attachguid");
                                                    String encryptUrl = OfficeWebUrlEncryptUtil
                                                            .getEncryptUrl(downloadUrl, documentType);
                                                    String attachurl = url + encryptUrl;
                                                    resultJson.put("attachurl", attachurl);
                                                    resultlist.add(resultJson);
                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                    // 3.1、根据项目代码和项目名称查询项目信息
                    List<Record> newsubapps = iJnProjectService.getBiguidsByParentId(auditRsItemBaseinfo.getRowguid());
                    if (!newsubapps.isEmpty()) {
                        for (Record subapp : newsubapps) {
                            List<AuditProject> projects = iJnProjectService
                                    .selectAuditProjectByBiguid(subapp.getStr("biguid"));
                            for (AuditProject project : projects) {
                                if (StringUtil.isNotBlank(project.getCertrowguid())
                                        && (project.getProjectname().contains("建设工程竣工规划核实")
                                                || project.getProjectname().contains("建设工程（含地下管线工程）档案验收")
                                                || project.getProjectname().contains("建筑工程施工许可证核发")
                                                || project.getProjectname().contains("建设工程规划许可证")
                                                || project.getProjectname().contains("建设用地规划许可证")
                                                || project.getProjectname().contains("商品房预售许可"))) {
                                    CertInfo certInfo = iCertInfoExternal
                                            .getCertInfoByRowguid(project.getCertrowguid());
                                    if (certInfo != null) {
                                        if (certInfo.getCertname().equals(certname)) {
                                            // 5、获取结果对应的附件
                                            List<JSONObject> attachList = iCertAttachExternal.getAttachList(
                                                    certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                            if (attachList != null && attachList.size() > 0) {
                                                for (JSONObject attachJson : attachList) {
                                                    JSONObject resultJson = new JSONObject();
                                                    resultJson.put("certname", certInfo.getCertname());
                                                    String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                            + attachJson.getString("attachguid");
                                                    String encryptUrl = OfficeWebUrlEncryptUtil
                                                            .getEncryptUrl(downloadUrl, documentType);
                                                    String attachurl = url + encryptUrl;
                                                    resultJson.put("attachurl", attachurl);
                                                    resultlist.add(resultJson);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    return JsonUtils.zwdtRestReturn("1", " 获取材料清单成功！", resultlist.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", " 获取材料清单失败！", "");
                }
            }
            else {
                log.info("=======结束调用getMaterialsByItemcode接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialsByItemcode接口参数：params【" + params + "】=======");
            log.info("=======getMaterialsByItemcode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料清单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我所在企业下的项目列表（工改一件事）
     *
     * @param request
     *            HTTP请求
     * @param response
     *            HTTP响应
     * @return
     */
    @RequestMapping(value = "/getitemlistforyjs")
    public String getItemListForYjs(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("=======开始调用getitemlistforyjs接口=======");
            // 1、获取接口入参
            // 1.1、获取项目名称搜索关键字
            String keyWord = WebUtil.getRequestParameterStr(request, "keyword");
            // 1.2、获取企业统一社会信用代码
            String creditCode = WebUtil.getRequestParameterStr(request, "creditcode");
            // 1.3、获取一页显示数量
            String pageSize = WebUtil.getRequestParameterStr(request, "pagesize");
            // 1.4、获取当前页数
            String currentPage = WebUtil.getRequestParameterStr(request, "currentpage");
            // 1.5、获取跳转阶段
            String phaseId = WebUtil.getRequestParameterStr(request, "phaseid");
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
                                    (Integer.parseInt(currentPage) - 1) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize), "", "")
                            .getResult();
                    totalCount = auditRsItemBaseinfosPageData.getRowCount();
                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
                    if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                        // 3.1、 将项目信息返回
                        int id = (Integer.parseInt(currentPage) - 1) * Integer.parseInt(pageSize) + 1; // 序号
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
                                    sqlSelectPhase.eq("phaseid", phaseId);
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
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getitemlistforyjs异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我所在企业下的项目列表失败：" + e.getMessage(), "");
        }
    }

}
