package com.epoint.zwdt.zwdtrest.project;

import java.io.File;
import java.io.FileInputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
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
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspspgcjsxk.domain.AuditSpSpGcjsxk;
import com.epoint.basic.auditsp.auditspspgcjsxk.inter.IAuditSpSpGcjsxkService;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.auditsp.auditspsplxydghxk.domain.AuditSpSpLxydghxk;
import com.epoint.basic.auditsp.auditspsplxydghxk.inter.IAuditSpSpLxydghxkService;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zwdt.zwdtrest.project.utils.GenerateAttachUtil;

/**
 * 建设项目网上申报相关接口
 *
 * @作者 WXH
 */
@RestController
@RequestMapping("/auditspspitemcontroller")
public class AuditSpSpItemController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 基础申报API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    /**
     * 企业申报API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditSpSpLxydghxkService iAuditSpSpLxydghxkService;
    /**
     * 子申报实例API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IConfigService iConfigService;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    /**
     * 申报实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    /**
     * 申报实例API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    /**
     * 申报实例API
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;
    /**
     * 配置参数API
     */

    @Autowired
    private IAuditSpSpGcjsxkService iAuditSpSpGcjsxkService;
    @Autowired
    private IAuditSpSpSgxkService iAuditSpSpSgxkService;
    @Autowired
    private IAuditSpSpJgysService iAuditSpSpJgysService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditSpBasetask iauditspbasetask;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private IAuditSpTask iAuditSpTask;

    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    @RequestMapping(value = "/initAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String initAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取基本项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                    }
                    else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("itemcode", itemCode);
                        sqlConditionUtil.isBlankOrValue("is_history", "0");
                        // 3.1、根据项目代码和项目名称查询项目信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditRsItemBaseinfos.size() > 0) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }
                    }
                    if (auditRsItemBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }

                    // 3.1.1 获取该项目的申报单位的统一社会信用代码
                    String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                    // 3.1.2 判断当前用户是否属于这家企业
                    if (StringUtil.isBlank(itemLegalCreditCode)) {
                        return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                    }
                    // 3.1.2.1、 查询出这家企业
                    SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                    String field = "creditcode";
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                        field = "organcode";
                    }
                    companySqlConditionUtil.eq(field, itemLegalCreditCode);
                    companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                    companySqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                            .getResult();
                    if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                    // 3.1.2.2、 获取企业法人证件号
                    String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                    // 3.1.2.3、 获取企业id
                    String companyId = auditRsCompanyBaseinfo.getCompanyid();
                    // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.eq("companyid", companyId);
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    // 查询当前阶段
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
                    AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                    String jianyiitemname = iConfigService.getFrameConfigValue("XM_JIANYI_NAME");
                    String hasejianyi = "";
                    if (business != null) {
                        if (jianyiitemname.equals(business.getBusinessname())) {
                            hasejianyi = "1";
                        }

                    }
                    if ((idNum != null && auditOnlineRegister != null && idNum.equals(auditOnlineRegister.getIdnumber()))
                            || (auditOnlineCompanyGrants != null && auditOnlineCompanyGrants.size() > 0)) {
                        JSONObject dataJson = new JSONObject();
                        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                        if (auditSpPhase != null) {
                            if (StringUtil.isNotBlank(auditSpPhase.getPhaseId())) {
                                switch (auditSpPhase.getPhaseId()) {
                                    case "1": // 第一阶段
                                        dataJson = getLxydghxkInfo(subappguid);
                                        dataJson.put("phaseid", "1");
                                        break;
                                    case "2": // 第二阶段
                                        dataJson = getGcjsxkInfo(subappguid);
                                        dataJson.put("phaseid", "2");
                                        break;
                                    case "3": // 第三阶段
                                        dataJson = getSgxkInfo(subappguid, hasejianyi);
                                        dataJson.put("phaseid", "3");
                                        break;
                                    case "4": // 第四阶段
                                        dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                        dataJson.put("phaseid", "4");
                                        break;
                                    case "6": // 装饰装修
                                        dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                        dataJson.put("phaseid", "6");
                                        break;
                                }

                                if ("4".equals(auditSpPhase.getPhaseId())) {
                                    JSONObject formInfo = new JSONObject();
                                    formInfo.put("formid", "576");
                                    formInfo.put("yewuGuid", subappguid);
                                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                    formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                    dataJson.put("formInfo", formInfo);
                                }

                                if ("3".equals(auditSpPhase.getPhaseId())) {
                                    JSONObject formInfo = new JSONObject();
                                    formInfo.put("formid", "601");
                                    formInfo.put("yewuGuid", subappguid);
                                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                    formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                    dataJson.put("formInfo", formInfo);
                                }
                            }
                            dataJson.put("added", "1".equals(auditSpPhase.getStr("ishb")));
                        }

                        if (business != null) {
                            // 查询主题是否 是市里的
                            String areacode = business.getAreacode();
                            if ("370800".equals(areacode)) {
                                dataJson.put("iscity", "1");
                            }
                            else {
                                dataJson.put("iscity", "0");
                            }
                        }

                        // 统一社会信用代码
                        dataJson.put("tyshxydm", auditRsCompanyBaseinfo.getCreditcode());
                        // 单位名称
                        dataJson.put("dzyzusername", auditRsCompanyBaseinfo.getOrganname());

                        // 查询子申报状态.
                        AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        int sbysth = 0;
                        String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                        if (subapp != null) {
                            if (!"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            dataJson.put("applytype", subapp.getStr("applytype"));// 申报方式
                            dataJson.put("applynum", subapp.getStr("applynum1"));// 第一次申报，为1说明是第二次
                        }
                        if (subapp == null) {
                            dataJson.put("isinit", 1);
                        }
                        // 返回是否允许子项目申报标识

                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("biguid", biguid);
                        sql.eq("phaseguid", auditSpPhase.getRowguid());
                        List<AuditSpISubapp> sublist = iAuditSpISubapp.getSubappListByMap(sql.getMap()).getResult();
                        boolean flag = true;// 未进行整体申报
                        // 非草稿的数量
                        int hasApplyCount = 0;
                        for (AuditSpISubapp auditSpISubapp : sublist) {
                            if (auditSpInstance.getYewuguid().equals(auditSpISubapp.getYewuguid()) && !"-1".equals(auditSpISubapp.getStatus())
                                    && !"3".equals(auditSpISubapp.getStatus())) {
                                flag = false;
                            }
                            if (!"-1".equals(auditSpISubapp.getStatus()) && !"3".equals(auditSpISubapp.getStatus())) {
                                hasApplyCount++;
                            }
                        }
                        // 进行了整体申报
                        String zt = "";
                        if (!flag) {
                            zt = ZwfwConstant.CONSTANT_STR_ONE;
                        }
                        else {
                            zt = ZwfwConstant.CONSTANT_STR_ZERO;
                        }
                        // 未进行申报过
                        if (sublist.isEmpty() || hasApplyCount == 0) {
                            zt = ZwfwConstant.CONSTANT_STR_TWO;
                        }
                        dataJson.put("zt", zt);
                        List<CodeItems> shiFouCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                        List<JSONObject> isallowsubitemList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            // 个性化 是否子申报只有是
                            if ("1".equals(codeItems.getItemValue())) {
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                objJson.put("isselected", 1);
                                isallowsubitemList.add(objJson);
                            }
                        }
                        dataJson.put("isallowsubitem", isallowsubitemList);

                        List<JSONObject> isusepowerList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                objJson.put("isselected", 1);
                            }
                            if (StringUtil.isNotBlank(dataJson.getString("isusepower"))) {
                                objJson.put("isselected", dataJson.getString("isusepower").equals(codeItems.getItemValue()) ? 1 : 0);
                            }
                            isusepowerList.add(objJson);
                        }
                        dataJson.put("isusepower", isusepowerList);

                        // 3.0新增开始
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

                        List<CodeItems> zdxmlxs = iCodeItemsService.listCodeItemsByCodeName("国标_重点项目");
                        List<JSONObject> zdxmlxList = new ArrayList<>();
                        // 若未配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", 1);
                            zdxmlxList.add(objJson);
                        }
                        for (CodeItems codeItems : zdxmlxs) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                                objJson.put("isselected", 1);
                            }
                            zdxmlxList.add(objJson);
                        }
                        dataJson.put("zdxmlx", zdxmlxList);

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
                        dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                        dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
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
                        dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getXmtzly()))) {
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getTdhqfs()))) {
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
                        dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj()); // 建筑面积
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
                        dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                        dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                        dataJson.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());

                        // 国标行业
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGbhy())) {
                            dataJson.put("gbhy", iCodeItemsService.getItemTextByCodeName("国标行业2017", auditRsItemBaseinfo.getGbhy()));
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
                        /* 3.0新增逻辑 */
                        // 立项类型（2.0）
                        List<CodeItems> lxlxList = iCodeItemsService.listCodeItemsByCodeName("国标_立项类型");
                        List<JSONObject> lxlxObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("lxlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            lxlxObjList.add(objJson);
                        }
                        for (CodeItems lxlxItem : lxlxList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", lxlxItem.getItemValue());
                            objJson.put("itemtext", lxlxItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("lxlx")) && lxlxItem.getItemValue().equals(auditRsItemBaseinfo.get("lxlx").toString())) {
                                objJson.put("isselected", 1);
                            }
                            lxlxObjList.add(objJson);
                        }
                        dataJson.put("lxlx", lxlxObjList);

                        // 工程行业分类（2.0）
                        List<CodeItems> gchyflList = iCodeItemsService.listCodeItemsByCodeName("国标_工程行业分类");
                        List<JSONObject> gchyflObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("gchyfl"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            gchyflObjList.add(objJson);
                        }
                        for (CodeItems gchyflItem : gchyflList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", gchyflItem.getItemValue());
                            objJson.put("itemtext", gchyflItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("gchyfl")) && gchyflItem.getItemValue().equals(auditRsItemBaseinfo.get("gchyfl").toString())) {
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
                            if (auditRsItemBaseinfo.getStr("jsddxzqh").contains(",")) {
                                String[] split = auditRsItemBaseinfo.getStr("jsddxzqh").split(",");
                                for (String string : split) {
                                    if (codeItem.getItemValue().equals(string)) {
                                        objJson.put("isselected", 1);
                                    }
                                }
                            }
                            else {
                                if (codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("jsddxzqh"))) {
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
                        dataJson.put("xmjwdzb", auditRsItemBaseinfo.getStr("xmjwdzb"));
                        dataJson.put("cd", auditRsItemBaseinfo.getDouble("cd"));
                        dataJson.put("gcfw", auditRsItemBaseinfo.getStr("gcfw"));

                        dataJson.put("sfwcqypg", sfwcqypgList); // 是否完成区域评估

                        dataJson.put("xmzb", auditRsItemBaseinfo.getStr("xmzb"));
                        // 查询一下子项目信息
                        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (auditSpISubapp != null) {
                            // 查询项目信息
                            AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                            if (subItemBaseinfo != null) {
                                dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                dataJson.put("xmzb", subItemBaseinfo.getStr("xmzb"));
                            }
                        }
                        log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/newInitAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String newInitAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用newInitAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取基本项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                    }
                    else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("itemcode", itemCode);
                        sqlConditionUtil.isBlankOrValue("is_history", "0");
                        // 3.1、根据项目代码和项目名称查询项目信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditRsItemBaseinfos.size() > 0) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }
                    }
                    if (auditRsItemBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }

                    // 3.1.1 获取该项目的申报单位的统一社会信用代码
                    String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                    // 3.1.2 判断当前用户是否属于这家企业
                    if (StringUtil.isBlank(itemLegalCreditCode)) {
                        return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                    }
                    // 3.1.2.1、 查询出这家企业
                    SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                    String field = "creditcode";
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                        field = "organcode";
                    }
                    companySqlConditionUtil.eq(field, itemLegalCreditCode);
                    companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                    companySqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                            .getResult();
                    if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                    // 3.1.2.2、 获取企业法人证件号
                    String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                    // 3.1.2.3、 获取企业id
                    String companyId = auditRsCompanyBaseinfo.getCompanyid();
                    // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.eq("companyid", companyId);
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    // 查询当前阶段
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
                    AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                    String jianyiitemname = iConfigService.getFrameConfigValue("XM_JIANYI_NAME");
                    String hasejianyi = "";
                    if (business != null) {
                        if (jianyiitemname.equals(business.getBusinessname())) {
                            hasejianyi = "1";
                        }

                    }
                    if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {
                        JSONObject dataJson = new JSONObject();
                        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                        if (auditSpPhase != null) {
                            if (StringUtil.isNotBlank(auditSpPhase.getPhaseId())) {
                                switch (auditSpPhase.getPhaseId()) {
                                    case "1": // 第一阶段
                                        dataJson = getLxydghxkInfo(subappguid);
                                        dataJson.put("phaseid", "1");
                                        break;
                                    case "2": // 第二阶段
                                        dataJson = getGcjsxkInfo(subappguid);
                                        dataJson.put("phaseid", "2");
                                        break;
                                    case "3": // 第三阶段
                                        dataJson = getSgxkInfo(subappguid, hasejianyi);
                                        dataJson.put("phaseid", "3");
                                        break;
                                    case "4": // 第四阶段
                                        dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                        dataJson.put("phaseid", "4");
                                        break;
                                    case "6": // 装饰装修
                                        dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                        dataJson.put("phaseid", "6");
                                        break;
                                }

                                // if
                                // (StringUtil.isNotBlank(auditSpPhase.getPhaseId()))
                                // {
                                // JSONObject formInfo = new JSONObject();
                                // formInfo.put("formid",
                                // auditSpPhase.getStr("formid"));
                                // formInfo.put("yewuGuid", subappguid);
                                // formInfo.put("eformCommonPage",
                                // "jnzwdt/epointsform/eformpage");
                                // formInfo.put("epointUrl",
                                // iConfigService.getFrameConfigValue("epointsformurlwt"));
                                // dataJson.put("formInfo", formInfo);
                                // }

                                if ("4".equals(auditSpPhase.getPhaseId())) {
                                    JSONObject formInfo = new JSONObject();
                                    formInfo.put("formid", "576");
                                    formInfo.put("yewuGuid", subappguid);
                                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                    formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                    dataJson.put("formInfo", formInfo);
                                }

                                if ("3".equals(auditSpPhase.getPhaseId())) {
                                    JSONObject formInfo = new JSONObject();
                                    formInfo.put("formid", "601");
                                    formInfo.put("yewuGuid", subappguid);
                                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                    formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                    dataJson.put("formInfo", formInfo);
                                }

                            }
                            dataJson.put("added", "1".equals(auditSpPhase.getStr("ishb")));
                        }

                        if (business != null) {
                            // 查询主题是否 是市里的
                            String areacode = business.getAreacode();
                            if ("370800".equals(areacode)) {
                                dataJson.put("iscity", "1");
                            }
                            else {
                                dataJson.put("iscity", "0");
                            }
                        }

                        // 统一社会信用代码
                        dataJson.put("tyshxydm", auditRsCompanyBaseinfo.getCreditcode());
                        // 单位名称
                        dataJson.put("dzyzusername", auditRsCompanyBaseinfo.getOrganname());

                        // 查询子申报状态.
                        AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        int sbysth = 0;
                        String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                        if (subapp != null) {
                            if (!"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            dataJson.put("applytype", subapp.getStr("applytype"));// 申报方式
                            dataJson.put("applynum", subapp.getStr("applynum1"));// 第一次申报，为1说明是第二次
                        }
                        if (subapp == null) {
                            dataJson.put("isinit", 1);
                        }
                        // 返回是否允许子项目申报标识

                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("biguid", biguid);
                        if (auditSpPhase != null) {
                            sql.eq("phaseguid", auditSpPhase.getRowguid());
                        }
                        List<AuditSpISubapp> sublist = iAuditSpISubapp.getSubappListByMap(sql.getMap()).getResult();
                        boolean flag = true;// 未进行整体申报
                        // 非草稿的数量
                        int hasApplyCount = 0;
                        for (AuditSpISubapp auditSpISubapp : sublist) {
                            if (auditSpInstance.getYewuguid().equals(auditSpISubapp.getYewuguid()) && !"-1".equals(auditSpISubapp.getStatus())
                                    && !"3".equals(auditSpISubapp.getStatus())) {
                                flag = false;
                            }
                            if (!"-1".equals(auditSpISubapp.getStatus()) && !"3".equals(auditSpISubapp.getStatus())) {
                                hasApplyCount++;
                            }
                        }
                        // 进行了整体申报
                        String zt = "";
                        if (!flag) {
                            zt = ZwfwConstant.CONSTANT_STR_ONE;
                        }
                        else {
                            zt = ZwfwConstant.CONSTANT_STR_ZERO;
                        }
                        // 未进行申报过
                        if (sublist.isEmpty() || hasApplyCount == 0) {
                            zt = ZwfwConstant.CONSTANT_STR_TWO;
                        }
                        dataJson.put("zt", zt);
                        List<CodeItems> shiFouCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                        List<JSONObject> isallowsubitemList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            // 个性化 是否子申报只有是
                            if ("1".equals(codeItems.getItemValue())) {
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                objJson.put("isselected", 1);
                                isallowsubitemList.add(objJson);
                            }
                        }
                        dataJson.put("isallowsubitem", isallowsubitemList);

                        List<JSONObject> isusepowerList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                objJson.put("isselected", 1);
                            }
                            if (StringUtil.isNotBlank(dataJson.getString("isusepower"))) {
                                objJson.put("isselected", dataJson.getString("isusepower").equals(codeItems.getItemValue()) ? 1 : 0);
                            }
                            isusepowerList.add(objJson);
                        }
                        dataJson.put("isusepower", isusepowerList);

                        // 3.0新增开始
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

                        List<CodeItems> zdxmlxs = iCodeItemsService.listCodeItemsByCodeName("国标_重点项目");
                        List<JSONObject> zdxmlxList = new ArrayList<>();
                        // 若未配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", 1);
                            zdxmlxList.add(objJson);
                        }
                        for (CodeItems codeItems : zdxmlxs) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                                objJson.put("isselected", 1);
                            }
                            zdxmlxList.add(objJson);
                        }
                        dataJson.put("zdxmlx", zdxmlxList);

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
                        dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                        dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
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
                        dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getXmtzly()))) {
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getTdhqfs()))) {
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
                        dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj()); // 建筑面积
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
                        dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                        dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                        dataJson.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());

                        // 国标行业
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGbhy())) {
                            dataJson.put("gbhy", iCodeItemsService.getItemTextByCodeName("国标行业2017", auditRsItemBaseinfo.getGbhy()));
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
                        /* 3.0新增逻辑 */
                        // 立项类型（2.0）
                        List<CodeItems> lxlxList = iCodeItemsService.listCodeItemsByCodeName("国标_立项类型");
                        List<JSONObject> lxlxObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("lxlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            lxlxObjList.add(objJson);
                        }
                        for (CodeItems lxlxItem : lxlxList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", lxlxItem.getItemValue());
                            objJson.put("itemtext", lxlxItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("lxlx")) && lxlxItem.getItemValue().equals(auditRsItemBaseinfo.get("lxlx").toString())) {
                                objJson.put("isselected", 1);
                            }
                            lxlxObjList.add(objJson);
                        }
                        dataJson.put("lxlx", lxlxObjList);

                        // 工程行业分类（2.0）
                        List<CodeItems> gchyflList = iCodeItemsService.listCodeItemsByCodeName("国标_工程行业分类");
                        List<JSONObject> gchyflObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("gchyfl"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            gchyflObjList.add(objJson);
                        }
                        for (CodeItems gchyflItem : gchyflList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", gchyflItem.getItemValue());
                            objJson.put("itemtext", gchyflItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("gchyfl")) && gchyflItem.getItemValue().equals(auditRsItemBaseinfo.get("gchyfl").toString())) {
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
                            if (auditRsItemBaseinfo.getStr("jsddxzqh").contains(",")) {
                                String[] split = auditRsItemBaseinfo.getStr("jsddxzqh").split(",");
                                for (String string : split) {
                                    if (codeItem.getItemValue().equals(string)) {
                                        objJson.put("isselected", 1);
                                    }
                                }
                            }
                            else {
                                if (codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("jsddxzqh"))) {
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
                        dataJson.put("xmjwdzb", auditRsItemBaseinfo.getStr("xmjwdzb"));
                        dataJson.put("cd", auditRsItemBaseinfo.getDouble("cd"));
                        dataJson.put("gcfw", auditRsItemBaseinfo.getStr("gcfw"));

                        dataJson.put("sfwcqypg", sfwcqypgList); // 是否完成区域评估

                        dataJson.put("xmzb", auditRsItemBaseinfo.getStr("xmzb"));
                        // 查询一下子项目信息
                        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (auditSpISubapp != null) {
                            // 查询项目信息
                            AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                            if (subItemBaseinfo != null) {
                                dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                dataJson.put("xmzb", subItemBaseinfo.getStr("xmzb"));
                            }
                        }
                        log.info("=======结束调用newInitAuditRsItemBaseinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用newInitAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======newInitAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======newInitAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "newInitAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    /*
     *
     */
    @RequestMapping(value = "/initlhjgAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String initlhjgAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initkgyjsAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1=" select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                JSONObject dataJson = new JSONObject();
                if (auditOnlineRegister != null) {
                    if (StringUtil.isBlank(subappguid)) {
                        dataJson.put("subappguid", UUID.randomUUID().toString());
                    }
                    else {
                        dataJson.put("subappguid", subappguid);
                    }
                    // 直接获取项目信息返回
                    if (StringUtil.isNotBlank(itemGuid)) {
                        // 3、 获取基本项目
                        AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                        if (StringUtil.isNotBlank(itemGuid)) {
                            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                            dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                            if (auditSpInstance != null) {
                                dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            }

                        }
                        else {
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("itemcode", itemCode);
                            sqlConditionUtil.isBlankOrValue("is_history", "0");
                            // 3.1、根据项目代码和项目名称查询项目信息
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                            if (auditRsItemBaseinfos.size() > 0) {
                                auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                            }
                        }
                        if (auditRsItemBaseinfo == null) {
                            return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                        }

                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                        if (auditSpInstance != null) {
                            dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            if (StringUtil.isBlank(phaseguid)) {
                                SqlConditionUtil phasesql = new SqlConditionUtil();
                                phasesql.eq("BUSINEDSSGUID", auditSpInstance.getBusinessguid());
                                phasesql.eq("phaseid", "2");
                                List<AuditSpPhase> phaselist = iAuditSpPhase.getAuditSpPhase(phasesql.getMap()).getResult();
                                if (ValidateUtil.isBlankCollection(phaselist)) {
                                    return JsonUtils.zwdtRestReturn("0", "项目阶段信息为空", "");
                                }
                                phaseguid = phaselist.get(0).getRowguid();
                            }
                        }

                        dataJson.put("phaseguid", phaseguid);

                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();// 7.10标版auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isBlank(itemLegalCreditCode)) {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                        // 3.1.2.1、 查询出这家企业
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        companySqlConditionUtil.eq(field, itemLegalCreditCode);
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                .getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                            return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 3.1.2.2、 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        // 3.1.2.3、 获取企业id
                        String companyId = auditRsCompanyBaseinfo.getCompanyid();
                        // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                        SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                        grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                        grantConditionUtil.eq("companyid", companyId);
                        grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                        List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                        // 查询当前阶段
                        AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                        String jianyiitemname = iConfigService.getFrameConfigValue("XM_JIANYI_NAME");
                        String hasejianyi = "";
                        if (business != null) {
                            if (jianyiitemname.equals(business.getBusinessname())) {
                                hasejianyi = "1";
                            }

                        }

                        if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {
                            AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                            if (auditSpPhase != null) {
                                if (StringUtil.isNotBlank(auditSpPhase.getPhaseId())) {
                                    switch (auditSpPhase.getPhaseId()) {
                                        case "1": // 第一阶段
                                            dataJson = getLxydghxkInfo(subappguid);
                                            dataJson.put("phaseid", "1");
                                            break;
                                        case "2": // 第二阶段
                                            dataJson = getGcjsxkInfo(subappguid);
                                            dataJson.put("phaseid", "2");
                                            break;
                                        case "3": // 第三阶段
                                            dataJson = getSgxkInfo(subappguid, hasejianyi);
                                            dataJson.put("phaseid", "3");
                                            break;
                                        case "4": // 第四阶段
                                            dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                            dataJson.put("phaseid", "4");
                                            break;
                                        case "6": // 装饰装修
                                            dataJson = getJgysInfo(subappguid, auditSpPhase.getPhaseId());
                                            dataJson.put("phaseid", "6");
                                            break;
                                    }

                                    // if
                                    // (StringUtil.isNotBlank(auditSpPhase.getPhaseId()))
                                    // {
                                    // JSONObject formInfo = new JSONObject();
                                    // formInfo.put("formid",
                                    // auditSpPhase.getStr("formid"));
                                    // formInfo.put("yewuGuid", subappguid);
                                    // formInfo.put("eformCommonPage",
                                    // "jnzwdt/epointsform/eformpage");
                                    // formInfo.put("epointUrl",
                                    // iConfigService.getFrameConfigValue("epointsformurlwt"));
                                    // dataJson.put("formInfo", formInfo);
                                    // }

                                    if ("4".equals(auditSpPhase.getPhaseId())) {
                                        JSONObject formInfo = new JSONObject();
                                        formInfo.put("formid", "576");
                                        formInfo.put("yewuGuid", subappguid);
                                        formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                        formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                        dataJson.put("formInfo", formInfo);
                                    }

                                    if ("3".equals(auditSpPhase.getPhaseId())) {
                                        JSONObject formInfo = new JSONObject();
                                        formInfo.put("formid", "601");
                                        formInfo.put("yewuGuid", subappguid);
                                        formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                                        formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                                        dataJson.put("formInfo", formInfo);
                                    }

                                }
                                dataJson.put("added", "1".equals(auditSpPhase.getStr("ishb")));
                            }

                            if (business != null) {
                                // 查询主题是否 是市里的
                                String areacode = business.getAreacode();
                                if ("370800".equals(areacode)) {
                                    dataJson.put("iscity", "1");
                                }
                                else {
                                    dataJson.put("iscity", "0");
                                }
                            }

                            // 统一社会信用代码
                            dataJson.put("tyshxydm", auditRsCompanyBaseinfo.getCreditcode());
                            // 单位名称
                            dataJson.put("dzyzusername", auditRsCompanyBaseinfo.getOrganname());

                            // 查询子申报状态.
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            int sbysth = 0;
                            String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                            if (subapp != null && !"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            if (subapp == null) {
                                dataJson.put("isinit", 1);
                            }
                            // 返回是否允许子项目申报标识
                            // 查询一下子项目信息
                            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                            if (StringUtil.isNotBlank(subappguid)) {
                                auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            }

                            if (auditSpISubapp != null) {
                                // 查询项目信息
                                AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (subItemBaseinfo != null) {
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                    dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                    dataJson.put("zjitemcode", subItemBaseinfo.getStr("zjitemcode"));
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", subItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ", subItemBaseinfo.getConstructionproperty());// 建设性质

                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", subItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));

                                    dataJson = processDataJson(dataJson, subItemBaseinfo);
                                }
                                else {
                                    dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ",
                                    // auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                    dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                                }
                            }
                            else {
                                dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                // dataJson.put("constructionproperty ", auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                dataJson.put("constructionproperty", getSelectItemList1("建设性质", auditRsItemBaseinfo.getConstructionproperty()));
                                dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                // 拟开工时间
                                dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                // 拟建成时间
                                dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                            }
                        }

                        // 建设地点行政区划
                        List<CodeItems> xfyslevelCode = iCodeItemsService.listCodeItemsByCodeName("行政区划");
                        List<JSONObject> xfyslevelList = new ArrayList<>();
                        for (CodeItems codeItem : xfyslevelCode) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItem.getItemValue());
                            objJson.put("itemtext", codeItem.getItemText());
                            if (auditRsItemBaseinfo.getStr("xfyslevel").contains(",")) {
                                String[] split = auditRsItemBaseinfo.getStr("xfyslevel").split(",");
                                for (String string : split) {
                                    if (codeItem.getItemValue().equals(string)) {
                                        objJson.put("isselected", 1);
                                    }
                                }
                            }
                            else {
                                if (codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("xfyslevel"))) {
                                    objJson.put("isselected", 1);
                                }
                            }

                            xfyslevelList.add(objJson);
                        }
                        dataJson.put("xfyslevel", xfyslevelList);

                        dataJson.put("xfystype", getSelectItemList1("消防验收办理方式", auditRsItemBaseinfo.getStr("xfystype")));
                        // 工程行业分类（2.0）
                        List<CodeItems> gchyflList = iCodeItemsService.listCodeItemsByCodeName("国标_工程行业分类");
                        List<JSONObject> gchyflObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("gchyfl"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            gchyflObjList.add(objJson);
                        }
                        for (CodeItems gchyflItem : gchyflList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", gchyflItem.getItemValue());
                            objJson.put("itemtext", gchyflItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("gchyfl")) && gchyflItem.getItemValue().equals(auditRsItemBaseinfo.get("gchyfl").toString())) {
                                objJson.put("isselected", 1);
                            }
                            gchyflObjList.add(objJson);
                        }
                        dataJson.put("gchyfl", gchyflObjList);

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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getXmtzly()))) {
                                objJson.put("isselected", 1);
                            }
                            xmtzlyList.add(objJson);
                        }
                        dataJson.put("xmtzly", xmtzlyList); // 项目投资来源

                        // 本次申报范围是否包含防空地下室
                        dataJson.put("sfbhfkdxs", getSelectItemList1("是否", auditRsItemBaseinfo.getStr("sfbhfkdxs")));
                    }

                    List<JSONObject> result = getMyItemlist(request, "", "", 20, 0);
                    List<JSONObject> resultJsonList = new ArrayList<>();
                    if (result != null && result.size() > 0) {
                        for (Object o : result) {
                            JSONObject object = (JSONObject) o;
                            JSONObject json1 = new JSONObject();
                            json1.put("itemvalue", object.get("itemguid"));
                            json1.put("itemtext", object.get("itemname"));
                            if (StringUtil.isNotBlank(itemGuid) && itemGuid.equals(object.get("itemguid"))) {
                                json1.put("isselected", 1);
                            }
                            resultJsonList.add(json1);

                        }
                    }

                    dataJson.put("itemnameinit", resultJsonList);
                    log.info("=======结束调用initAuditRsItemBaseinfo接口=======");

                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    /*
     *
     */
    @RequestMapping(value = "/initsrqlhbzAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String initkgyjsAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initkgyjsAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1=" select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                JSONObject dataJson = new JSONObject();
                if (auditOnlineRegister != null) {
                    // 水电气暖信息填充
                    dataJson = getsrqlhbzInfo();
                    if (StringUtil.isBlank(subappguid)) {
                        dataJson.put("subappguid", UUID.randomUUID().toString());
                    }
                    else {
                        dataJson.put("subappguid", subappguid);
                    }
                    // 直接获取项目信息返回
                    if (StringUtil.isNotBlank(itemGuid)) {
                        // 3、 获取基本项目
                        AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                        if (StringUtil.isNotBlank(itemGuid)) {
                            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                            dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                            if (auditSpInstance != null) {
                                dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            }

                        }
                        else {
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("itemcode", itemCode);
                            sqlConditionUtil.isBlankOrValue("is_history", "0");
                            // 3.1、根据项目代码和项目名称查询项目信息
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                            if (auditRsItemBaseinfos.size() > 0) {
                                auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                            }
                        }
                        if (auditRsItemBaseinfo == null) {
                            return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                        }

                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                        if (auditSpInstance != null) {
                            dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            if (StringUtil.isBlank(phaseguid)) {
                                SqlConditionUtil phasesql = new SqlConditionUtil();
                                phasesql.eq("BUSINEDSSGUID", auditSpInstance.getBusinessguid());
                                phasesql.eq("phaseid", "2");
                                List<AuditSpPhase> phaselist = iAuditSpPhase.getAuditSpPhase(phasesql.getMap()).getResult();
                                if (ValidateUtil.isBlankCollection(phaselist)) {
                                    return JsonUtils.zwdtRestReturn("0", "项目阶段信息为空", "");
                                }
                                phaseguid = phaselist.get(0).getRowguid();
                            }
                        }

                        dataJson.put("phaseguid", phaseguid);

                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();// 7.10标版auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isBlank(itemLegalCreditCode)) {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                        // 3.1.2.1、 查询出这家企业
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        companySqlConditionUtil.eq(field, itemLegalCreditCode);
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                .getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                            return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 3.1.2.2、 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        // 3.1.2.3、 获取企业id
                        String companyId = auditRsCompanyBaseinfo.getCompanyid();
                        // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                        SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                        grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                        grantConditionUtil.eq("companyid", companyId);
                        grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                        List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                        if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {

                            // 查询子申报状态.
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            int sbysth = 0;
                            String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                            if (subapp != null && !"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            if (subapp == null) {
                                dataJson.put("isinit", 1);
                            }
                            // 返回是否允许子项目申报标识
                            // 查询一下子项目信息
                            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                            if (StringUtil.isNotBlank(subappguid)) {
                                auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            }

                            if (auditSpISubapp != null) {
                                // 查询项目信息
                                AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (subItemBaseinfo != null) {
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                    dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                    dataJson.put("zjitemcode", subItemBaseinfo.getStr("zjitemcode"));
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", subItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ", subItemBaseinfo.getConstructionproperty());// 建设性质

                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", subItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));

                                    dataJson = processDataJson(dataJson, subItemBaseinfo);
                                }
                                else {
                                    dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ",
                                    // auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                    dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                                }
                            }
                            else {
                                dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                // dataJson.put("constructionproperty ", auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                dataJson.put("constructionproperty", getSelectItemList1("建设性质", auditRsItemBaseinfo.getConstructionproperty()));
                                dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                // 拟开工时间
                                dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                // 拟建成时间
                                dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                            }
                        }

                    }

                    List<JSONObject> result = getMyItemlist(request, "", "", 20, 0);
                    List<JSONObject> resultJsonList = new ArrayList<>();
                    if (result != null && result.size() > 0) {
                        for (Object o : result) {
                            JSONObject object = (JSONObject) o;
                            JSONObject json1 = new JSONObject();
                            json1.put("itemvalue", object.get("itemguid"));
                            json1.put("itemtext", object.get("itemname"));
                            if (StringUtil.isNotBlank(itemGuid) && itemGuid.equals(object.get("itemguid"))) {
                                json1.put("isselected", 1);
                            }
                            resultJsonList.add(json1);

                        }
                    }
                    dataJson.put("itemnameinit", resultJsonList);
                    log.info("=======结束调用initAuditRsItemBaseinfo接口=======");

                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    /*
     * 道路挖掘一件事基本信息
     */
    @RequestMapping(value = "/initDlwjAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String initDlwjAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initDlwjAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 区划代码
                String areacode = obj.getString("areacode");
                // 2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1=" select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                JSONObject dataJson = new JSONObject();
                if (auditOnlineRegister != null) {
                    // 水电气暖信息填充
                    if (StringUtil.isBlank(subappguid)) {
                        dataJson.put("subappguid", UUID.randomUUID().toString());
                    }
                    else {
                        dataJson.put("subappguid", subappguid);
                    }
                    // 直接获取项目信息返回
                    if (StringUtil.isNotBlank(itemGuid)) {
                        // 3、 获取基本项目
                        AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                        if (StringUtil.isNotBlank(itemGuid)) {
                            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                            dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                            if (auditSpInstance != null) {
                                dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            }

                        }
                        else {
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("itemcode", itemCode);
                            sqlConditionUtil.isBlankOrValue("is_history", "0");
                            // 3.1、根据项目代码和项目名称查询项目信息
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                            if (auditRsItemBaseinfos.size() > 0) {
                                auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                            }
                        }
                        if (auditRsItemBaseinfo == null) {
                            return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                        }

                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                        if (auditSpInstance != null) {
                            dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            if (StringUtil.isBlank(phaseguid)) {
                                SqlConditionUtil phasesql = new SqlConditionUtil();
                                phasesql.eq("BUSINEDSSGUID", auditSpInstance.getBusinessguid());
                                phasesql.eq("phaseid", "2");
                                List<AuditSpPhase> phaselist = iAuditSpPhase.getAuditSpPhase(phasesql.getMap()).getResult();
                                if (ValidateUtil.isBlankCollection(phaselist)) {
                                    return JsonUtils.zwdtRestReturn("0", "项目阶段信息为空", "");
                                }
                                phaseguid = phaselist.get(0).getRowguid();
                            }
                        }

                        dataJson.put("phaseguid", phaseguid);

                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();// 7.10标版auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isBlank(itemLegalCreditCode)) {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                        // 3.1.2.1、 查询出这家企业
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        companySqlConditionUtil.eq(field, itemLegalCreditCode);
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                .getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                            return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 3.1.2.2、 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        // 3.1.2.3、 获取企业id
                        String companyId = auditRsCompanyBaseinfo.getCompanyid();
                        dataJson.put("orgname", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("registeraddress", auditRsCompanyBaseinfo.getRegisteraddress());
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            dataJson.put("creditcode", auditRsCompanyBaseinfo.getOrgancode());
                        }
                        else {
                            dataJson.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
                        }
                        dataJson.put("organlegal", auditRsCompanyBaseinfo.getOrganlegal());
                        dataJson.put("orgalegal_idnumber", auditRsCompanyBaseinfo.getOrgalegal_idnumber());

                        // 将项目信息先初始化，不管是否已经存在子项目
                        // dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                        // dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目代码
                        // dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                        // dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode()+"-");//子项目代码
                        // dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                        // dataJson.put("constructionproperty", getSelectItemList1("建设性质", auditRsItemBaseinfo.getConstructionproperty()));

                        // 工程建设项目名称及规划批准文件
                        dataJson.put("ghxk_cliengguid", UUID.randomUUID().toString());
                        // 施工方案附件
                        dataJson.put("sgfa_cliengguid", UUID.randomUUID().toString());
                        // 示意图附件
                        dataJson.put("syt_cliengguid", UUID.randomUUID().toString());

                        // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                        SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                        grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                        grantConditionUtil.eq("companyid", companyId);
                        grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                        List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();

                        // 查询当前阶段
                        AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                        String jianyiitemname = iConfigService.getFrameConfigValue("XM_JIANYI_NAME");
                        String hasejianyi = "";
                        if (business != null) {
                            if (jianyiitemname.equals(business.getBusinessname())) {
                                hasejianyi = "1";
                            }
                        }

                        if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {
                            // 查询子申报状态.
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            int sbysth = 0;
                            String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                            if (subapp != null && !"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            if (subapp == null) {
                                dataJson.put("isinit", 1);
                            }
                            // 返回是否允许子项目申报标识
                            // 查询一下子项目信息
                            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                            if (StringUtil.isNotBlank(subappguid)) {
                                auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            }

                            if (auditSpISubapp != null) {
                                // 查询项目信息
                                AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (subItemBaseinfo != null) {
                                    if (subItemBaseinfo != null && StringUtil.isNotBlank(subItemBaseinfo.getStr("ghxk_cliengguid"))) {
                                        // 工程建设项目名称及规划批准文件
                                        dataJson.put("ghxk_cliengguid", subItemBaseinfo.getStr("ghxk_cliengguid"));
                                    }
                                    if (subItemBaseinfo != null && StringUtil.isNotBlank(subItemBaseinfo.getStr("sgfa_cliengguid"))) {
                                        // 施工方案附件
                                        dataJson.put("sgfa_cliengguid", subItemBaseinfo.getStr("sgfa_cliengguid"));
                                    }
                                    if (subItemBaseinfo != null && StringUtil.isNotBlank(subItemBaseinfo.getStr("syt_cliengguid"))) {
                                        // 示意图附件
                                        dataJson.put("syt_cliengguid", subItemBaseinfo.getStr("syt_cliengguid"));
                                    }
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                    dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                    dataJson.put("zjitemcode", subItemBaseinfo.getStr("zjitemcode"));
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", subItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ", subItemBaseinfo.getConstructionproperty());// 建设性质

                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", subItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                    // 以下是道路挖掘一件事基本信息
                                    dataJson.put("sgdepartment", subItemBaseinfo.getStr("sgdepartment"));
                                    dataJson.put("sgdepartmentaddress", subItemBaseinfo.getStr("sgdepartmentaddress"));
                                    dataJson.put("sgdepartmentcreditcode", subItemBaseinfo.getStr("sgdepartmentcreditcode"));
                                    dataJson.put("sgdepartmentlegal", subItemBaseinfo.getStr("sgdepartmentlegal"));
                                    dataJson.put("sgdepartmentlegalid", subItemBaseinfo.getStr("sgdepartmentlegalid"));
                                    dataJson.put("sbreason", subItemBaseinfo.getStr("sbreason"));
                                    dataJson.put("zgrname", subItemBaseinfo.getStr("zgrname"));
                                    dataJson.put("zgrid", subItemBaseinfo.getStr("zgrid"));
                                    dataJson.put("zgrmobile", subItemBaseinfo.getStr("zgrmobile"));
                                    dataJson.put("jbrname", subItemBaseinfo.getStr("jbrname"));
                                    dataJson.put("jbrid", subItemBaseinfo.getStr("jbrid"));
                                    dataJson.put("jbrmobile", subItemBaseinfo.getStr("jbrmobile"));
                                }
                                else {
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.like("itemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                    sql.setSelectCounts(1);
                                    sql.setOrderDesc("itemcode");
                                    List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                    if (ValidateUtil.isNotBlankCollection(auditRsItemBaseinfos)) {
                                        String maxItemcode = auditRsItemBaseinfos.get(0).getItemcode();
                                        String orderstr = maxItemcode.substring(maxItemcode.lastIndexOf('-') + 1);
                                        int ordernum = Integer.parseInt(orderstr);
                                        ordernum = ordernum + 1;
                                        String subItemCode = auditRsItemBaseinfo.getItemcode() + "-" + String.format("%04d", ordernum);
                                        dataJson.put("subitemcode", subItemCode);
                                    }
                                    else {
                                        dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-0001");
                                    }
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ",
                                    // auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));

                                    // 以下是道路挖掘一件事基本信息
                                    dataJson.put("sgdepartment", auditRsItemBaseinfo.getStr("sgdepartment"));
                                    dataJson.put("sgdepartmentaddress", auditRsItemBaseinfo.getStr("sgdepartmentaddress"));
                                    dataJson.put("sgdepartmentcreditcode", auditRsItemBaseinfo.getStr("sgdepartmentcreditcode"));
                                    dataJson.put("sgdepartmentlegal", auditRsItemBaseinfo.getStr("sgdepartmentlegal"));
                                    dataJson.put("sgdepartmentlegalid", auditRsItemBaseinfo.getStr("sgdepartmentlegalid"));
                                    dataJson.put("sbreason", auditRsItemBaseinfo.getStr("sbreason"));
                                    dataJson.put("zgrname", auditRsItemBaseinfo.getStr("zgrname"));
                                    dataJson.put("zgrid", auditRsItemBaseinfo.getStr("zgrid"));
                                    dataJson.put("zgrmobile", auditRsItemBaseinfo.getStr("zgrmobile"));
                                    dataJson.put("jbrname", auditRsItemBaseinfo.getStr("jbrname"));
                                    dataJson.put("jbrid", auditRsItemBaseinfo.getStr("jbrid"));
                                    dataJson.put("jbrmobile", auditRsItemBaseinfo.getStr("jbrmobile"));
                                }
                            }
                            else {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.like("itemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                sql.setSelectCounts(1);
                                sql.setOrderDesc("itemcode");
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (ValidateUtil.isNotBlankCollection(auditRsItemBaseinfos)) {
                                    String maxItemcode = auditRsItemBaseinfos.get(0).getItemcode();
                                    String orderstr = maxItemcode.substring(maxItemcode.lastIndexOf('-') + 1);
                                    int ordernum = Integer.parseInt(orderstr);
                                    ordernum = ordernum + 1;
                                    String subItemCode = auditRsItemBaseinfo.getItemcode() + "-" + String.format("%04d", ordernum);
                                    dataJson.put("subitemcode", subItemCode);
                                }
                                else {
                                    dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-0001");
                                }
                                dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                // dataJson.put("constructionproperty ", auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                dataJson.put("constructionproperty", getSelectItemList1("建设性质", auditRsItemBaseinfo.getConstructionproperty()));
                                dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                // 拟开工时间
                                dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                // 拟建成时间
                                dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));

                                // 以下是道路挖掘一件事基本信息
                                dataJson.put("sgdepartment", auditRsItemBaseinfo.getStr("sgdepartment"));
                                dataJson.put("sgdepartmentaddress", auditRsItemBaseinfo.getStr("sgdepartmentaddress"));
                                dataJson.put("sgdepartmentcreditcode", auditRsItemBaseinfo.getStr("sgdepartmentcreditcode"));
                                dataJson.put("sgdepartmentlegal", auditRsItemBaseinfo.getStr("sgdepartmentlegal"));
                                dataJson.put("sgdepartmentlegalid", auditRsItemBaseinfo.getStr("sgdepartmentlegalid"));
                                dataJson.put("sbreason", auditRsItemBaseinfo.getStr("sbreason"));
                                dataJson.put("zgrname", auditRsItemBaseinfo.getStr("zgrname"));
                                dataJson.put("zgrid", auditRsItemBaseinfo.getStr("zgrid"));
                                dataJson.put("zgrmobile", auditRsItemBaseinfo.getStr("zgrmobile"));
                                dataJson.put("jbrname", auditRsItemBaseinfo.getStr("jbrname"));
                                dataJson.put("jbrid", auditRsItemBaseinfo.getStr("jbrid"));
                                dataJson.put("jbrmobile", auditRsItemBaseinfo.getStr("jbrmobile"));
                            }

                        }
                    }

                    List<JSONObject> result = getMyItemlist(request, "", "", 20, 0);
                    List<JSONObject> resultJsonList = new ArrayList<>();
                    if (result != null && result.size() > 0) {
                        for (Object o : result) {
                            JSONObject object = (JSONObject) o;
                            JSONObject json1 = new JSONObject();
                            json1.put("itemvalue", object.get("itemguid"));
                            json1.put("itemtext", object.get("itemname"));
                            if (StringUtil.isNotBlank(itemGuid) && itemGuid.equals(object.get("itemguid"))) {
                                json1.put("isselected", 1);
                            }
                            resultJsonList.add(json1);

                        }
                    }
                    dataJson.put("itemnameinit", resultJsonList);

                    AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                    if (auditOrgaArea == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取区划失败！", "");
                    }

                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("taskname", "道路挖掘修复一件事（" + auditOrgaArea.getXiaquname() + "）");
                    List<AuditSpBasetask> auditSpBasetaskList = iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(auditSpBasetaskList)) {
                        String basetaskguid = auditSpBasetaskList.get(0).getRowguid();
                        // 获取所有事项数据
                        sql.clear();
                        sql.eq("basetaskguid", basetaskguid);
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
                        // 去除删除或禁用的事项
                        if (ValidateUtil.isNotBlankCollection(taskids)) {
                            // 遍历spitask，去除taskids中已经评审的事项
                            sql.clear();
                            sql.eq("IS_EDITAFTERIMPORT", "1");
                            sql.eq("IS_ENABLE", "1");
                            sql.isBlankOrValue("IS_HISTORY", "0");

                            if (taskids != null && taskids.size() > 0) {
                                sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                            }
                            // sql.setOrder("areacode", "asc");
                            sql.setSelectFields("task_id,taskname,rowguid,areacode,ouname");
                            auditTasks = auditTaskService.getAllTask(sql.getMap()).getResult();
                        }

                        List<JSONObject> resultJsonList2 = new ArrayList<>();
                        if (auditTasks != null && auditTasks.size() > 0) {
                            for (AuditTask auditTask : auditTasks) {
                                JSONObject json1 = new JSONObject();
                                json1.put("itemvalue", auditTask.getRowguid());
                                json1.put("itemtext", auditTask.getTaskname());
                                json1.put("taskid", auditTask.getTask_id());
                                AuditTaskExtension extend = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                                if (extend != null) {
                                    json1.put("formid", extend.getStr("formid"));
                                }
                                /*
                                 * if (StringUtil.isNotBlank(itemGuid) &&
                                 * itemGuid.equals(object.get("itemguid"))) {
                                 * json1.put("isselected", 1); }
                                 */
                                resultJsonList2.add(json1);
                            }
                        }
                        dataJson.put("tasknameinit", resultJsonList2);
                    }

                    //对creditcode、orgalegal_idnumber、sgdepartmentcreditcode、sgdepartmentlegalid进行脱敏处理
                    if (StringUtil.isNotBlank(dataJson.getString("creditcode"))) {
                        dataJson.put("creditcode", tuoming(dataJson.getString("creditcode")));

                    }
                    if (StringUtil.isNotBlank(dataJson.getString("orgalegal_idnumber"))) {
                        dataJson.put("orgalegal_idnumber", tuoming(dataJson.getString("orgalegal_idnumber")));
                    }
                    if (StringUtil.isNotBlank(dataJson.getString("sgdepartmentcreditcode"))) {
                        dataJson.put("sgdepartmentcreditcode", tuoming(dataJson.getString("sgdepartmentcreditcode")));
                    }
                    if (StringUtil.isNotBlank(dataJson.getString("sgdepartmentlegalid"))) {
                        dataJson.put("sgdepartmentlegalid", tuoming(dataJson.getString("sgdepartmentlegalid")));
                    }
                    SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
                    legalSqlConditionUtil.eq("orgalegal_idnumber", "370811196808020519");
                    legalSqlConditionUtil.isBlankOrValue("is_history", "0");
                    legalSqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
                    log.info("====20241516====auditRsCompanyBaseinfos" + auditRsCompanyBaseinfos);
                    if (auditRsCompanyBaseinfos.size() > 0 && !auditRsCompanyBaseinfos.isEmpty()) {
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        SqlConditionUtil baseinfosql = new SqlConditionUtil();
                        baseinfosql.isNotBlank("parentid");
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                            baseinfosql.eq("itemlegalcreditcode", auditRsCompanyBaseinfo.getCreditcode());
                        }
                        else {
                            baseinfosql.eq("itemlegalcreditcode", auditRsCompanyBaseinfo.getOrgancode());
                        }
                        baseinfosql.setOrderDesc("OperateDate");
                        List<AuditRsItemBaseinfo> baseinfoList = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(baseinfosql.getMap()).getResult();
                        log.info("====20241516====baseinfoList" + baseinfoList);
                        String subitemcode = "";
                        if (baseinfoList != null && !baseinfoList.isEmpty()) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo = baseinfoList.get(0);
                            log.info("====20241516====auditRsItemBaseinfo" + auditRsItemBaseinfo);
                            String code = auditRsItemBaseinfo.getItemcode();
                            String[] split = code.split("-");
                            String s = split[split.length - 1];
                            String s1 = Integer.parseInt(s) + 1 + "";
                            subitemcode = dataJson.getString("itemcode") + "-" + s1;
                        }
                        else {
                            subitemcode = dataJson.getString("itemcode") + "-" + "0001";
                        }
                        dataJson.put("subitemcode", subitemcode);
                    }
                    log.info("=======结束调用initDlwjAuditRsItemBaseinfo接口=======");

                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initDlwjAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initDlwjAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initDlwjAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initDlwjAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    /*
     * 建设项目涉水一件事基本信息
     */
    @RequestMapping(value = "/initJsxmAuditRsItemBaseinfo", method = RequestMethod.POST)
    public String initJsxmAuditRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initJsxmAuditRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");
                // 区划代码
                String areacode = obj.getString("areacode");
                // 2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1=" select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                JSONObject dataJson = new JSONObject();
                if (auditOnlineRegister != null) {
                    // 水电气暖信息填充
                    dataJson = getsrqlhbzInfo();
                    if (StringUtil.isBlank(subappguid)) {
                        dataJson.put("subappguid", UUID.randomUUID().toString());
                    }
                    else {
                        dataJson.put("subappguid", subappguid);
                    }
                    // 直接获取项目信息返回
                    if (StringUtil.isNotBlank(itemGuid)) {
                        // 3、 获取基本项目
                        AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                        if (StringUtil.isNotBlank(itemGuid)) {
                            auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                            dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                            if (auditSpInstance != null) {
                                dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            }

                        }
                        else {
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("itemcode", itemCode);
                            sqlConditionUtil.isBlankOrValue("is_history", "0");
                            // 3.1、根据项目代码和项目名称查询项目信息
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                            if (auditRsItemBaseinfos.size() > 0) {
                                auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                            }
                        }
                        if (auditRsItemBaseinfo == null) {
                            return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                        }

                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                        if (auditSpInstance != null) {
                            dataJson.put("businessguid", auditSpInstance.getBusinessguid());
                            if (StringUtil.isBlank(phaseguid)) {
                                SqlConditionUtil phasesql = new SqlConditionUtil();
                                phasesql.eq("BUSINEDSSGUID", auditSpInstance.getBusinessguid());
                                phasesql.eq("phaseid", "2");
                                List<AuditSpPhase> phaselist = iAuditSpPhase.getAuditSpPhase(phasesql.getMap()).getResult();
                                if (ValidateUtil.isBlankCollection(phaselist)) {
                                    return JsonUtils.zwdtRestReturn("0", "项目阶段信息为空", "");
                                }
                                phaseguid = phaselist.get(0).getRowguid();
                            }
                        }

                        dataJson.put("phaseguid", phaseguid);

                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();// 7.10标版auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isBlank(itemLegalCreditCode)) {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                        // 3.1.2.1、 查询出这家企业
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        companySqlConditionUtil.eq(field, itemLegalCreditCode);
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                .getResult();
                        if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                            return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                        }
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        // 3.1.2.2、 获取企业法人证件号
                        String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                        // 3.1.2.3、 获取企业id
                        String companyId = auditRsCompanyBaseinfo.getCompanyid();
                        // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                        SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                        grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                        grantConditionUtil.eq("companyid", companyId);
                        grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                        List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                        if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {

                            // 查询子申报状态.
                            AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            int sbysth = 0;
                            String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                            if (subapp != null && !"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            if (subapp == null) {
                                dataJson.put("isinit", 1);
                            }
                            // 返回是否允许子项目申报标识
                            // 查询一下子项目信息
                            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                            if (StringUtil.isNotBlank(subappguid)) {
                                auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                            }

                            if (auditSpISubapp != null) {
                                // 查询项目信息
                                AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (subItemBaseinfo != null) {
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                    dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                    dataJson.put("zjitemcode", subItemBaseinfo.getStr("zjitemcode"));
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", subItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ", subItemBaseinfo.getConstructionproperty());// 建设性质

                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", subItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(subItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));

                                    dataJson = processDataJson(dataJson, subItemBaseinfo);
                                }
                                else {
                                    dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                    dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                    // dataJson.put("constructionproperty ",
                                    // auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                    dataJson.put("constructionproperty", getSelectItemList1("建设性质", subItemBaseinfo.getConstructionproperty()));
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                    // 拟开工时间
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                    // 拟建成时间
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                    dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                                }
                            }
                            else {
                                dataJson.put("subitemcode", auditRsItemBaseinfo.getItemcode() + "-");
                                dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
                                dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());// 项目名称
                                dataJson.put("CONSTRUCTIONSCALEANDDESC", auditRsItemBaseinfo.getConstructionscaleanddesc());// 建设规模
                                // dataJson.put("constructionproperty ", auditRsItemBaseinfo.getConstructionproperty());// 建设性质
                                dataJson.put("constructionproperty", getSelectItemList1("建设性质", auditRsItemBaseinfo.getConstructionproperty()));
                                dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());// 建设地点
                                // 拟开工时间
                                dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd"));
                                // 拟建成时间
                                dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd"));
                                dataJson = processDataJson(dataJson, auditRsItemBaseinfo);
                            }
                        }

                    }

                    List<JSONObject> result = getMyItemlist(request, "", "", 20, 0);
                    List<JSONObject> resultJsonList = new ArrayList<>();
                    if (result != null && result.size() > 0) {
                        for (Object o : result) {
                            JSONObject object = (JSONObject) o;
                            JSONObject json1 = new JSONObject();
                            json1.put("itemvalue", object.get("itemguid"));
                            json1.put("itemtext", object.get("itemname"));
                            if (StringUtil.isNotBlank(itemGuid) && itemGuid.equals(object.get("itemguid"))) {
                                json1.put("isselected", 1);
                            }
                            resultJsonList.add(json1);

                        }
                    }
                    dataJson.put("itemnameinit", resultJsonList);

                    AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                    if (auditOrgaArea == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取区划失败！", "");
                    }

                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("taskname", "建设项目涉水审批一件事（" + auditOrgaArea.getXiaquname() + "）");
                    List<AuditSpBasetask> auditSpBasetaskList = iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(auditSpBasetaskList)) {
                        String basetaskguid = auditSpBasetaskList.get(0).getRowguid();
                        // 获取所有事项数据
                        sql.clear();
                        sql.eq("basetaskguid", basetaskguid);
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
                        // 去除删除或禁用的事项
                        if (ValidateUtil.isNotBlankCollection(taskids)) {
                            // 遍历spitask，去除taskids中已经评审的事项
                            sql.clear();
                            sql.eq("IS_EDITAFTERIMPORT", "1");
                            sql.eq("IS_ENABLE", "1");
                            sql.isBlankOrValue("IS_HISTORY", "0");
                            if (taskids != null && taskids.size() > 0) {
                                sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                            }
                            // sql.setOrder("areacode", "asc");
                            sql.setSelectFields("task_id,taskname,rowguid,areacode,ouname");
                            auditTasks = auditTaskService.getAllTask(sql.getMap()).getResult();
                        }

                        List<JSONObject> resultJsonList2 = new ArrayList<>();
                        if (auditTasks != null && auditTasks.size() > 0) {
                            for (AuditTask auditTask : auditTasks) {
                                JSONObject json1 = new JSONObject();
                                json1.put("itemvalue", auditTask.getRowguid());
                                json1.put("itemtext", auditTask.getTaskname());
                                json1.put("taskid", auditTask.getTask_id());
                                AuditTaskExtension extend = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                                if (extend != null) {
                                    json1.put("formid", extend.getStr("formid"));
                                }
                                /*
                                 * if (StringUtil.isNotBlank(itemGuid) &&
                                 * itemGuid.equals(object.get("itemguid"))) {
                                 * json1.put("isselected", 1); }
                                 */
                                resultJsonList2.add(json1);
                            }
                        }
                        dataJson.put("tasknameinit", resultJsonList2);
                    }

                    log.info("=======结束调用initJsxmAuditRsItemBaseinfo接口=======");

                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initJsxmAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initJsxmAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initJsxmAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initJsxmAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 建设项目开关一件事基本信息
     *
     * @param params
     * @param request
     * @return
     */

    @RequestMapping(value = "/initJsxm_kgRsItemBaseinfo", method = RequestMethod.POST)
    public String initJsxm_kgRsItemBaseinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initJsxm_kgRsItemBaseinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.3 获取子申报标识
                String subappguid = obj.getString("subappguid");
                // 1.4 one-第一阶段,立项用地规划许可; two-第二阶段,工程建设许可; three-第三阶段,施工许可;
                // four-第四阶段,竣工验收
                String phaseguid = obj.getString("phaseguid");
                // 1.5 申报实例标识
                String biguid = obj.getString("biguid");

                // 区划代码
                String areacode = obj.getString("areacode");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取基本项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                    }
                    else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("itemcode", itemCode);
                        sqlConditionUtil.isBlankOrValue("is_history", "0");
                        // 3.1、根据项目代码和项目名称查询项目信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditRsItemBaseinfos.size() > 0) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }
                    }
                    if (auditRsItemBaseinfo == null) {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }

                    // 3.1.1 获取该项目的申报单位的统一社会信用代码
                    String itemLegalCreditCode = auditRsItemBaseinfo.getItemlegalcertnum();
                    // 3.1.2 判断当前用户是否属于这家企业
                    if (StringUtil.isBlank(itemLegalCreditCode)) {
                        return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                    }
                    // 3.1.2.1、 查询出这家企业
                    SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                    String field = "creditcode";
                    if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                        field = "organcode";
                    }
                    companySqlConditionUtil.eq(field, itemLegalCreditCode);
                    companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                    companySqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                            .getResult();
                    if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                    // 3.1.2.2、 获取企业法人证件号
                    String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                    // 3.1.2.3、 获取企业id
                    String companyId = auditRsCompanyBaseinfo.getCompanyid();
                    // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.eq("companyid", companyId);
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    // 查询当前阶段
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
                    AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                    String jianyiitemname = iConfigService.getFrameConfigValue("XM_JIANYI_NAME");
                    String hasejianyi = "";
                    if (business != null) {
                        if (jianyiitemname.equals(business.getBusinessname())) {
                            hasejianyi = "1";
                        }

                    }
                    if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {
                        JSONObject dataJson = new JSONObject();
                        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseguid).getResult();
                        if (auditSpPhase != null) {
                            dataJson = getSgxkInfo(subappguid, hasejianyi);
                            dataJson.put("phaseid", "3");
                            JSONObject formInfo = new JSONObject();
                            formInfo.put("formid", "1059");
                            formInfo.put("yewuGuid", subappguid);
                            formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                            formInfo.put("epointUrl", iConfigService.getFrameConfigValue("epointsformurlwt"));
                            dataJson.put("formInfo", formInfo);


                            dataJson.put("added", "1".equals(auditSpPhase.getStr("ishb")));
                        }

                        if (business != null) {
                            // 查询主题是否 是市里的
                            String areacode1 = business.getAreacode();
                            if ("370800".equals(areacode1)) {
                                dataJson.put("iscity", "1");
                            }
                            else {
                                dataJson.put("iscity", "0");
                            }
                        }

                        // 统一社会信用代码
                        dataJson.put("tyshxydm", auditRsCompanyBaseinfo.getCreditcode());
                        // 单位名称
                        dataJson.put("dzyzusername", auditRsCompanyBaseinfo.getOrganname());

                        // 查询子申报状态.
                        AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        int sbysth = 0;
                        String titleStr = "您提交的信息正在审核中，请耐心等待~ ";
                        if (subapp != null) {
                            if (!"3".equals(subapp.getStatus())) {
                                if (ZwfwConstant.LHSP_Status_YSTH.equals(subapp.getStatus())) {
                                    sbysth = 1;
                                }
                                if (Integer.parseInt(subapp.getStatus()) > Integer.parseInt(ZwfwConstant.LHSP_Status_YSTH)) {
                                    titleStr = "您提交的办件状态已发生改变,请到申报中心查看。";
                                }
                                dataJson.put("titleStr", titleStr);
                                dataJson.put("sbysth", sbysth);
                                dataJson.put("reason", subapp.getReason());
                            }
                            dataJson.put("applytype", subapp.getStr("applytype"));// 申报方式
                            dataJson.put("applynum", subapp.getStr("applynum1"));// 第一次申报，为1说明是第二次
                        }
                        if (subapp == null) {
                            dataJson.put("isinit", 1);
                        }
                        // 返回是否允许子项目申报标识

                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("biguid", biguid);
                        if (auditSpPhase != null) {
                            sql.eq("phaseguid", auditSpPhase.getRowguid());
                        }
                        List<AuditSpISubapp> sublist = iAuditSpISubapp.getSubappListByMap(sql.getMap()).getResult();
                        boolean flag = true;// 未进行整体申报
                        // 非草稿的数量
                        int hasApplyCount = 0;
                        for (AuditSpISubapp auditSpISubapp : sublist) {
                            if (auditSpInstance.getYewuguid().equals(auditSpISubapp.getYewuguid()) && !"-1".equals(auditSpISubapp.getStatus())
                                    && !"3".equals(auditSpISubapp.getStatus())) {
                                flag = false;
                            }
                            if (!"-1".equals(auditSpISubapp.getStatus()) && !"3".equals(auditSpISubapp.getStatus())) {
                                hasApplyCount++;
                            }
                        }
                        // 进行了整体申报
                        String zt = "";
                        if (!flag) {
                            zt = ZwfwConstant.CONSTANT_STR_ONE;
                        }
                        else {
                            zt = ZwfwConstant.CONSTANT_STR_ZERO;
                        }
                        // 未进行申报过
                        if (sublist.isEmpty() || hasApplyCount == 0) {
                            zt = ZwfwConstant.CONSTANT_STR_TWO;
                        }
                        dataJson.put("zt", zt);
                        List<CodeItems> shiFouCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                        List<JSONObject> isallowsubitemList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            // 个性化 是否子申报只有是
                            if ("1".equals(codeItems.getItemValue())) {
                                objJson.put("itemvalue", codeItems.getItemValue());
                                objJson.put("itemtext", codeItems.getItemText());
                                objJson.put("isselected", 1);
                                isallowsubitemList.add(objJson);
                            }
                        }
                        dataJson.put("isallowsubitem", isallowsubitemList);

                        List<JSONObject> isusepowerList = new ArrayList<>();
                        for (CodeItems codeItems : shiFouCode) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                objJson.put("isselected", 1);
                            }
                            if (StringUtil.isNotBlank(dataJson.getString("isusepower"))) {
                                objJson.put("isselected", dataJson.getString("isusepower").equals(codeItems.getItemValue()) ? 1 : 0);
                            }
                            isusepowerList.add(objJson);
                        }
                        dataJson.put("isusepower", isusepowerList);

                        // 3.0新增开始
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

                        List<CodeItems> zdxmlxs = iCodeItemsService.listCodeItemsByCodeName("国标_重点项目");
                        List<JSONObject> zdxmlxList = new ArrayList<>();
                        // 若未配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", 1);
                            zdxmlxList.add(objJson);
                        }
                        for (CodeItems codeItems : zdxmlxs) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", codeItems.getItemValue());
                            objJson.put("itemtext", codeItems.getItemText());
                            if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getStr("zdxmlx"))) {
                                objJson.put("isselected", 1);
                            }
                            zdxmlxList.add(objJson);
                        }
                        dataJson.put("zdxmlx", zdxmlxList);

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
                        dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                        dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
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
                        dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getXmtzly()))) {
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
                            if (codeItems.getItemValue().equals(String.valueOf(auditRsItemBaseinfo.getTdhqfs()))) {
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
                        dataJson.put("jzmj", auditRsItemBaseinfo.getJzmj()); // 建筑面积
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
                        dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                        dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                        dataJson.put("constructionscaleanddesc", auditRsItemBaseinfo.getConstructionscaleanddesc());

                        // 国标行业
                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getGbhy())) {
                            dataJson.put("gbhy", iCodeItemsService.getItemTextByCodeName("国标行业2017", auditRsItemBaseinfo.getGbhy()));
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
                        /* 3.0新增逻辑 */
                        // 立项类型（2.0）
                        List<CodeItems> lxlxList = iCodeItemsService.listCodeItemsByCodeName("国标_立项类型");
                        List<JSONObject> lxlxObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("lxlx"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            lxlxObjList.add(objJson);
                        }
                        for (CodeItems lxlxItem : lxlxList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", lxlxItem.getItemValue());
                            objJson.put("itemtext", lxlxItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("lxlx")) && lxlxItem.getItemValue().equals(auditRsItemBaseinfo.get("lxlx").toString())) {
                                objJson.put("isselected", 1);
                            }
                            lxlxObjList.add(objJson);
                        }
                        dataJson.put("lxlx", lxlxObjList);

                        // 工程行业分类（2.0）
                        List<CodeItems> gchyflList = iCodeItemsService.listCodeItemsByCodeName("国标_工程行业分类");
                        List<JSONObject> gchyflObjList = new ArrayList<>();
                        // 若为配置，下拉框默认显示请选择
                        if (StringUtil.isBlank(auditRsItemBaseinfo.get("gchyfl"))) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", "");
                            objJson.put("itemtext", "请选择");
                            objJson.put("isselected", "1");
                            gchyflObjList.add(objJson);
                        }
                        for (CodeItems gchyflItem : gchyflList) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", gchyflItem.getItemValue());
                            objJson.put("itemtext", gchyflItem.getItemText());
                            if (StringUtil.isNotBlank(auditRsItemBaseinfo.get("gchyfl")) && gchyflItem.getItemValue().equals(auditRsItemBaseinfo.get("gchyfl").toString())) {
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
                            if (auditRsItemBaseinfo.getStr("jsddxzqh").contains(",")) {
                                String[] split = auditRsItemBaseinfo.getStr("jsddxzqh").split(",");
                                for (String string : split) {
                                    if (codeItem.getItemValue().equals(string)) {
                                        objJson.put("isselected", 1);
                                    }
                                }
                            }
                            else {
                                if (codeItem.getItemValue().equals(auditRsItemBaseinfo.getStr("jsddxzqh"))) {
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
                        dataJson.put("xmjwdzb", auditRsItemBaseinfo.getStr("xmjwdzb"));
                        dataJson.put("cd", auditRsItemBaseinfo.getDouble("cd"));
                        dataJson.put("gcfw", auditRsItemBaseinfo.getStr("gcfw"));

                        dataJson.put("sfwcqypg", sfwcqypgList); // 是否完成区域评估

                        dataJson.put("xmzb", auditRsItemBaseinfo.getStr("xmzb"));
                        // 查询一下子项目信息
                        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (auditSpISubapp != null) {
                            // 查询项目信息
                            AuditRsItemBaseinfo subItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                            if (subItemBaseinfo != null) {
                                dataJson.put("subitemname", subItemBaseinfo.getItemname());
                                dataJson.put("subitemcode", subItemBaseinfo.getItemcode());
                                dataJson.put("xmzb", subItemBaseinfo.getStr("xmzb"));
                            }
                        }

                        List<JSONObject> result = getMyItemlist(request, "", "", 20, 0);
                        List<JSONObject> resultJsonList = new ArrayList<>();
                        if (result != null && result.size() > 0) {
                            for (Object o : result) {
                                JSONObject object = (JSONObject) o;
                                JSONObject json1 = new JSONObject();
                                json1.put("itemvalue", object.get("itemguid"));
                                json1.put("itemtext", object.get("itemname"));
                                if (StringUtil.isNotBlank(itemGuid) && itemGuid.equals(object.get("itemguid"))) {
                                    json1.put("isselected", 1);
                                }
                                resultJsonList.add(json1);

                            }
                        }
                        dataJson.put("itemnameinit", resultJsonList);

                        AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                        if (auditOrgaArea == null) {
                            return JsonUtils.zwdtRestReturn("0", "获取区划失败！", "");
                        }

                        sql.clear();
                        sql.eq("taskname", "建设项目高效开工一件事（" + auditOrgaArea.getXiaquname() + "）");
                        List<AuditSpBasetask> auditSpBasetaskList = iauditspbasetask.getAuditSpBasetaskByCondition(sql.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(auditSpBasetaskList)) {
                            String basetaskguid = auditSpBasetaskList.get(0).getRowguid();
                            // 获取所有事项数据
                            sql.clear();
                            sql.eq("basetaskguid", basetaskguid);
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
                            // 去除删除或禁用的事项
                            if (ValidateUtil.isNotBlankCollection(taskids)) {
                                // 遍历spitask，去除taskids中已经评审的事项
                                sql.clear();
                                sql.eq("IS_EDITAFTERIMPORT", "1");
                                sql.eq("IS_ENABLE", "1");
                                sql.isBlankOrValue("IS_HISTORY", "0");

                                if (taskids != null && taskids.size() > 0) {
                                    sql.in("task_id", "'" + StringUtil.join(taskids, "','") + "'");
                                }
                                // sql.setOrder("areacode", "asc");
                                sql.setSelectFields("task_id,taskname,rowguid,areacode,ouname");
                                auditTasks = auditTaskService.getAllTask(sql.getMap()).getResult();
                            }

                            List<JSONObject> resultJsonList2 = new ArrayList<>();
                            if (auditTasks != null && auditTasks.size() > 0) {
                                for (AuditTask auditTask : auditTasks) {
                                    JSONObject json1 = new JSONObject();
                                    json1.put("itemvalue", auditTask.getRowguid());
                                    json1.put("itemtext", auditTask.getTaskname());
                                    json1.put("taskid", auditTask.getTask_id());
                                    json1.put("notip", true);
                                    AuditTaskExtension extend = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                                    if (extend != null) {
                                        json1.put("formid", extend.getStr("formid"));
                                    }
                                    /*
                                     * if (StringUtil.isNotBlank(itemGuid) &&
                                     * itemGuid.equals(object.get("itemguid"))) {
                                     * json1.put("isselected", 1); }
                                     */
                                    resultJsonList2.add(json1);
                                }
                            }
                            dataJson.put("tasknameinit", resultJsonList2);
                        }

                        log.info("=======结束调用initJsxm_kgRsItemBaseinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用initJsxm_kgRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initJsxm_kgRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initJsxm_kgRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initJsxm_kgRsItemBaseinfo异常信息：" + e.getMessage(), "");
        }
    }


    /**
     * 脱敏
     *
     * @param str
     * @return
     */
    public String tuoming(String str) {
        if (str.length() == 2) {
            str = str.substring(0, 1) + '*';
        }
        else if (str.length() > 2) {
            String name = "";
            for (int i = 0, len = str.length() - 2; i < len; i++) {
                name += '*';
            }
            str = str.substring(0, 1) + name + str.substring(str.length() - 1);
        }
        return str;
    }


    public static JSONObject processDataJson(JSONObject dataJson, AuditRsItemBaseinfo subItemBaseinfo) {
        // 处理 isusewater 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("isusewater"))) {
            List<JSONObject> isusewaterList = getJsonArrayAsList(dataJson.getJSONArray("isusewater"));
            if (isusewaterList != null) {
                for (JSONObject object : isusewaterList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("isusewater");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("isusewater", isusewaterList);
        }

        // 处理 ismovewater 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("ismovewater"))) {
            List<JSONObject> ismovewaterList = getJsonArrayAsList(dataJson.getJSONArray("ismovewater"));
            if (ismovewaterList != null) {
                for (JSONObject object : ismovewaterList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("ismovewater");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("ismovewater", ismovewaterList);
        }

        // 处理 isuseelec 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("isuseelec"))) {
            List<JSONObject> isuseelecList = getJsonArrayAsList(dataJson.getJSONArray("isuseelec"));
            if (isuseelecList != null) {
                for (JSONObject object : isuseelecList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("isuseelec");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("isuseelec", isuseelecList);
        }

        // 处理 isgas 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("isgas"))) {
            List<JSONObject> isgasList = getJsonArrayAsList(dataJson.getJSONArray("isgas"));
            if (isgasList != null) {
                for (JSONObject object : isgasList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("isgas");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("isgas", isgasList);
        }

        // 处理 iswarm 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("iswarm"))) {
            List<JSONObject> iswarmList = getJsonArrayAsList(dataJson.getJSONArray("iswarm"));
            if (iswarmList != null) {
                for (JSONObject object : iswarmList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("iswarm");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("iswarm", iswarmList);
        }

        // 处理 istx 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("istx"))) {
            List<JSONObject> istxList = getJsonArrayAsList(dataJson.getJSONArray("istx"));
            if (istxList != null) {
                for (JSONObject object : istxList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("istx");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("istx", istxList);
        }

        // 处理 isgd 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("isgd"))) {
            List<JSONObject> isgdList = getJsonArrayAsList(dataJson.getJSONArray("isgd"));
            if (isgdList != null) {
                for (JSONObject object : isgdList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("isgd");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("isgd", isgdList);
        }

        // 处理 iswaijie 相关逻辑
        if (StringUtils.isNotBlank(subItemBaseinfo.getStr("iswaijie"))) {
            List<JSONObject> iswaijieList = getJsonArrayAsList(dataJson.getJSONArray("iswaijie"));
            if (iswaijieList != null) {
                for (JSONObject object : iswaijieList) {
                    String subItemBaseinfoValue = subItemBaseinfo.getStr("iswaijie");
                    String objectValue = object.getString("itemvalue");
                    if (subItemBaseinfoValue.equals(objectValue)) {
                        object.put("isselected", subItemBaseinfoValue);
                    }
                }
            }
            dataJson.put("iswaijie", iswaijieList);
        }

        return dataJson;
    }

    private static List<JSONObject> getJsonArrayAsList(JSONArray jsonArray) {
        if (jsonArray != null) {
            List<JSONObject> list = new java.util.ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }
            return list;
        }
        return null;
    }

    /**
     * 获取我的项目列表
     *
     * @param request
     * @param keyWord
     * @param creditCode
     * @param pageSize
     * @param currentPage
     */
    public List<JSONObject> getMyItemlist(HttpServletRequest request, String keyWord, String creditCode, int pageSize, int currentPage) {
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
            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant.selectCompanyGrantByConditionMap(grantSqlConditionUtil.getMap()).getResult();
            String strWhereCompanyId = "";// 拼接被授权的所有企业id
            for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
            }
            // 4、获取用户身份是代办人或者管理者的企业ID
            SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
            legalSqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
            legalSqlConditionUtil.eq("is_history", "0");
            legalSqlConditionUtil.eq("isactivated", "1");
            List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
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
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(sqlSelectCompanyUtil.getMap())
                            .getResult();
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
                return null;
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
                        .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(), currentPage * pageSize, pageSize, "", "").getResult();
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
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            // 查询实例所有子申报
                            List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                            if (auditSpInstance != null) {
                                String businessguid = auditSpInstance.getBusinessguid();// 主题guid
                                SqlConditionUtil sqlSelectPhase = new SqlConditionUtil();
                                sqlSelectPhase.eq("businedssguid", businessguid);
                                sqlSelectPhase.setOrderDesc("ordernumber");
                                // 查询出该主题的所有阶段
                                List<AuditSpPhase> auditSpPhaseList = iAuditSpPhase.getAuditSpPhase(sqlSelectPhase.getMap()).getResult();
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
                                    objPhase.put("phasename", i == 0 ? auditSpPhase.getPhasename() : auditSpPhase.getPhasename() + "(" + i + ")");
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
                return itemList;
            }
        }
        else {
            return null;
        }
        return null;
    }

    // 存在共同字段的, 先判断一下是否有值
    public List<JSONObject> getSelectItemList1(String codeName, String filedValue) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("itemvalue", "");
        json.put("itemtext", "请选择");
        json.put("isselected", 1);
        resultJsonList.add(json);
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("itemvalue", codeItems.getItemValue());
            objJson.put("itemtext", codeItems.getItemText());
            // 判断是否有默认选中的
            if (codeItems.getItemValue().equals(filedValue)) {
                objJson.put("isselected", 1);
                resultJsonList.get(0).put("isselected", 0);
            }
            resultJsonList.add(objJson);
        }
        return resultJsonList;
    }

    // 默认为请选择
    public List<JSONObject> getSelectItemList1(String codeName) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("itemvalue", "");
        json.put("itemtext", "请选择");
        json.put("isselected", 1);
        resultJsonList.add(json);
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("itemvalue", codeItems.getItemValue());
            objJson.put("itemtext", codeItems.getItemText());
            resultJsonList.add(objJson);
        }
        return resultJsonList;
    }

    public static List<JSONObject> deepCopy(List<JSONObject> originalList) {
        List<JSONObject> copiedList = new ArrayList<>();
        for (JSONObject jsonObject : originalList) {
            JSONObject copiedJsonObject = new JSONObject();
            // 遍历原始JSONObject的键值对，复制到新的JSONObject中
            jsonObject.keySet().forEach(key -> copiedJsonObject.put(key, jsonObject.get(key)));
            copiedList.add(copiedJsonObject);
        }
        return copiedList;
    }

    /**
     * 获取水电气暖相关
     *
     * @return
     */
    private JSONObject getsrqlhbzInfo() {
        JSONObject dataJson = new JSONObject();
        // dataJson.put("xmtzly", getSelectItemList1("项目投资来源"));

        // dataJson.put("itemproperty", getSelectItemList1("水电气_项目性质"));
        // dataJson.put("sdqtype", getSelectItemList2("水电气_水电气报装类型"));
        // dataJson.put("usewatertype", getSelectItemList1("水电气_用水类型"));
        // dataJson.put("movewatertype", getSelectItemList1("水电气_排水类别"));
        // dataJson.put("useelecway", getSelectItemList1("水电气_用电业务类型"));
        // dataJson.put("useelectype", getSelectItemList1("水电气_用电类别"));
        // dataJson.put("warmtype", getSelectItemList1("水电气_采暖方式"));
        // dataJson.put("gassupporttype", getSelectItemList1("水电气_供气公司"));
        // dataJson.put("ouerelc", getSelectItemList1("水电气_有无"));

        List<CodeItems> shiFouCode = iCodeItemsService.listCodeItemsByCodeName("是否");
        List<JSONObject> isusewater = new ArrayList<>();
        List<JSONObject> ismovewater = new ArrayList<>();

        List<JSONObject> isuseelec = new ArrayList<>();
        List<JSONObject> isgas = new ArrayList<>();

        List<JSONObject> iswarm = new ArrayList<>();
        List<JSONObject> istx = new ArrayList<>();

        List<JSONObject> isgd = new ArrayList<>();

        List<JSONObject> iswaijie = new ArrayList<>();
        //
        for (CodeItems codeItems : shiFouCode) {
            JSONObject objJson = new JSONObject();
            objJson.put("itemvalue", codeItems.getItemValue());
            objJson.put("itemtext", codeItems.getItemText());
            objJson.put("isselected", "0");
            isusewater.add(objJson);

        }
        ismovewater = deepCopy(isusewater);
        isuseelec = deepCopy(isusewater);
        isgas = deepCopy(isusewater);
        iswarm = deepCopy(isusewater);
        istx = deepCopy(isusewater);
        iswaijie = deepCopy(isusewater);
        isgd = deepCopy(isusewater);

        dataJson.put("isusewater", isusewater);
        dataJson.put("ismovewater", ismovewater);
        dataJson.put("isuseelec", isuseelec);
        dataJson.put("isgas", isgas);
        dataJson.put("iswarm", iswarm);
        dataJson.put("istx", istx);
        dataJson.put("isgd", isgd);
        dataJson.put("iswaijie", iswaijie);
        return dataJson;
    }

    @RequestMapping(value = "/getSimpleItemInfoCode", method = RequestMethod.POST)
    public String getSimpleItemInfoCode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSimpleItemInfoCode接口=======");
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
                String back = obj.getString("back");
                JSONObject dataJson = new JSONObject();
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();

                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("biguid", biGuid);
                sqlConditionUtil.like("itemcode", auditRsItemBaseinfo.getItemcode());
                sqlConditionUtil.isNotBlank("parentid");
                sqlConditionUtil.setOrderDesc("itemcode");
                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                String newStartNumber = "01";
                // 说明已经存在子项目
                if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                    // 判断是否返回上一步操作（返回上一步项目代码步自增）
                    if (StringUtil.isNotBlank(back) && "1".equals(back)) {
                        dataJson.put("newitemcode", auditRsItemBaseinfos.get(0).getItemcode());
                    }
                    else {
                        AuditRsItemBaseinfo auditRsItemBaseinfoMax = auditRsItemBaseinfos.get(0);
                        int beginIndex = auditRsItemBaseinfoMax.getItemcode().lastIndexOf("-") + 1;
                        int endIndex = auditRsItemBaseinfoMax.getItemcode().length();
                        String itemcodeMax = auditRsItemBaseinfoMax.getItemcode().substring(beginIndex, endIndex);
                        int newNextNumber = Integer.parseInt(itemcodeMax) + 1;
                        String newNextItemCode = String.format("%02d", newNextNumber);
                        dataJson.put("newitemcode", auditRsItemBaseinfo.getItemcode() + "-" + newNextItemCode);
                    }
                }
                else {
                    // 不存在子项目的，直接主项目+0001
                    dataJson.put("newitemcode", auditRsItemBaseinfo.getItemcode() + "-" + newStartNumber);
                }
                dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                // 3、获取项目信息
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
     * 保存竣工联合申报
     */
    @RequestMapping(value = "/savesrqlhjginfo", method = RequestMethod.POST)
    public String savesrqlhjginfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用savesrqlhjginfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    if (StringUtil.isBlank(subappguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数subappguid为空", "");
                    }
                    String biguid = param.getString("biguid"); // 主题实例guid
                    if (StringUtil.isBlank(biguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数biguid为空", "");
                    }
                    param.put("isxfjssc", "1");
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    // 未申报过，或者是草稿或者是预审退回状态
                    if (auditSpISubapp == null || "2".equals(auditSpISubapp.getStatus()) || "3".equals(auditSpISubapp.getStatus())
                            || ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");// 是否允许子申报
                        String itemguid = param.getString("itemguid");
                        String itemguidparernt = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String type = param.getString("type");// 1-保存 2-提交
                        String businessguid = param.getString("businessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String constructionproperty = param.getString("constructionproperty");// 建设性质
                        String constructionsite = param.getString("constructionsite");// 地点
                        // 1.3 拟开工时间
                        Date itemStartDate = param.getDate("itemstartdate");
                        // 1.4 拟建成时间
                        Date itemFinishDate = param.getDate("itemfinishdate");

                        String gchyfl = param.getString("gchyfl");
                        String xmtzly = param.getString("xmtzly");
                        String islhysyjs = param.getString("islhysyjs");
                        JSONArray xfysleveljson = param.getJSONArray("xfyslevel");
                        String xfyslevel = "";
                        if (xfysleveljson.size() > 0 && !xfysleveljson.isEmpty()) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (int i = 0; i < xfysleveljson.size(); i++) {
                                String level = xfysleveljson.getString(i);
                                if (i < xfysleveljson.size() - 1) {
                                    stringBuffer.append(level).append(",");
                                }
                                else {
                                    stringBuffer.append(level);
                                }

                            }
                            xfyslevel = stringBuffer.toString();
                        }

                        String xfystype = param.getString("xfystype");
                        String sfbhfkdxs = param.getString("sfbhfkdxs");

                        // 选择事项taskids
                        String taskids = param.getString("inval").replaceAll(",''", "");
                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }

                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        isallowsubitem = "1";
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            Date date = new Date();
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setOperatedate(date);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                // 判断并处理 isusewater 列
                                if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                    auditRsItemBaseinfo.set("isusewater", param.getString("isusewater"));
                                }
                                if (StringUtils.isNotBlank(gchyfl)) {
                                    auditRsItemBaseinfo.set("gchyfl", gchyfl);
                                }
                                if (StringUtils.isNotBlank(xmtzly)) {
                                    auditRsItemBaseinfo.set("xmtzly", xmtzly);
                                }
                                if (StringUtils.isNotBlank(islhysyjs)) {
                                    auditRsItemBaseinfo.set("islhysyjs", islhysyjs);
                                }
                                if (StringUtils.isNotBlank(xfyslevel)) {
                                    auditRsItemBaseinfo.set("xfyslevel", xfyslevel);
                                }
                                if (StringUtils.isNotBlank(xfystype)) {
                                    auditRsItemBaseinfo.set("xfystype", xfystype);
                                }
                                if (StringUtils.isNotBlank(sfbhfkdxs)) {
                                    auditRsItemBaseinfo.set("sfbhfkdxs", sfbhfkdxs);
                                }

                                // 判断并处理 ismovewater 列
                                if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                    auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                }

                                // 判断并处理 isuseelec 列
                                if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                    auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                }

                                // 判断并处理 isgas 列
                                if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                    auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                }

                                // 判断并处理 iswarm 列
                                if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                    auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                }

                                // 判断并处理 istx 列
                                if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                    auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                }

                                // 判断并处理 isgd 列
                                if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                    auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                }

                                // 判断并处理 iswaijie 列
                                if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                    auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                }
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }
                                        if (StringUtils.isNotBlank(gchyfl)) {
                                            auditRsItemBaseinfo.set("gchyfl", gchyfl);
                                        }
                                        if (StringUtils.isNotBlank(xmtzly)) {
                                            auditRsItemBaseinfo.set("xmtzly", xmtzly);
                                        }
                                        if (StringUtils.isNotBlank(islhysyjs)) {
                                            auditRsItemBaseinfo.set("islhysyjs", islhysyjs);
                                        }
                                        if (StringUtils.isNotBlank(xfyslevel)) {
                                            auditRsItemBaseinfo.set("xfyslevel", xfyslevel);
                                        }
                                        if (StringUtils.isNotBlank(xfystype)) {
                                            auditRsItemBaseinfo.set("xfystype", xfystype);
                                        }
                                        if (StringUtils.isNotBlank(sfbhfkdxs)) {
                                            auditRsItemBaseinfo.set("sfbhfkdxs", sfbhfkdxs);
                                        }
                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        auditRsItemBaseinfo.setDraft(status);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }
                                        if (StringUtils.isNotBlank(gchyfl)) {
                                            auditRsItemBaseinfo.set("gchyfl", gchyfl);
                                        }
                                        if (StringUtils.isNotBlank(xmtzly)) {
                                            auditRsItemBaseinfo.set("xmtzly", xmtzly);
                                        }
                                        if (StringUtils.isNotBlank(islhysyjs)) {
                                            auditRsItemBaseinfo.set("islhysyjs", islhysyjs);
                                        }
                                        if (StringUtils.isNotBlank(xfyslevel)) {
                                            auditRsItemBaseinfo.set("xfyslevel", xfyslevel);
                                        }
                                        if (StringUtils.isNotBlank(xfystype)) {
                                            auditRsItemBaseinfo.set("xfystype", xfystype);
                                        }
                                        if (StringUtils.isNotBlank(sfbhfkdxs)) {
                                            auditRsItemBaseinfo.set("sfbhfkdxs", sfbhfkdxs);
                                        }
                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        setSrqlhbzAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, taskids, auditOnlineRegister, "");

                        log.info("=======结束调用savesrqlhjginfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }
                    else {
                        log.info("=======结束调用savesrqlhjginfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用savesrqlhjginfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======savesrqlhjginfo接口参数：params【" + params + "】=======");
            log.info("=======savesrqlhjginfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "savesrqlhjginfo异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 保存水热气联合申报
     */
    @RequestMapping(value = "/savesrqlhbzinfo", method = RequestMethod.POST)
    public String savesrqlhbzinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用savesrqlhbzinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1 = " select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    if (StringUtil.isBlank(subappguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数subappguid为空", "");
                    }
                    String biguid = param.getString("biguid"); // 主题实例guid
                    if (StringUtil.isBlank(biguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数biguid为空", "");
                    }
                    param.put("isxfjssc", "1");
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    // 未申报过，或者是草稿或者是预审退回状态
                    if (auditSpISubapp == null || "2".equals(auditSpISubapp.getStatus()) || "3".equals(auditSpISubapp.getStatus())
                            || ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");// 是否允许子申报
                        String itemguid = param.getString("itemguid");
                        String itemguidparernt = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String type = param.getString("type");// 1-保存 2-提交
                        String businessguid = param.getString("businessguid");// 主题guid
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String constructionproperty = param.getString("constructionproperty");// 建设性质
                        String constructionsite = param.getString("constructionsite");// 地点
                        // 1.3 拟开工时间
                        Date itemStartDate = param.getDate("itemstartdate");
                        // 1.4 拟建成时间
                        Date itemFinishDate = param.getDate("itemfinishdate");

                        // 选择事项taskids
                        String taskids = param.getString("inval").replaceAll(",''", "");
                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }

                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        isallowsubitem = "1";
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            Date date = new Date();
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setOperatedate(date);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                // 判断并处理 isusewater 列
                                if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                    auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                }

                                // 判断并处理 ismovewater 列
                                if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                    auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                }

                                // 判断并处理 isuseelec 列
                                if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                    auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                }

                                // 判断并处理 isgas 列
                                if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                    auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                }

                                // 判断并处理 iswarm 列
                                if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                    auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                }

                                // 判断并处理 istx 列
                                if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                    auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                }

                                // 判断并处理 isgd 列
                                if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                    auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                }

                                // 判断并处理 iswaijie 列
                                if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                    auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                }
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }

                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        auditRsItemBaseinfo.setDraft(status);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }

                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        setSrqlhbzAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, taskids, auditOnlineRegister, yjsbusinessguid);

                        log.info("=======结束调用savesrqlhbzinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }
                    else {
                        log.info("=======结束调用savesrqlhbzinfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用savesrqlhbzinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======savesrqlhbzinfo接口参数：params【" + params + "】=======");
            log.info("=======savesrqlhbzinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "savesrqlhbzinfo异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 建设项目涉水一件事申报
     */
    @RequestMapping(value = "/savejsxxsszinfo", method = RequestMethod.POST)
    public String savejsxxsszinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用savejsxxsszinfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // String sql1 = " select * from audit_online_register where
                // LOGINID ='h123456h_1213'";
                // auditOnlineRegister = CommonDao.getInstance().find(sql1,
                // AuditOnlineRegister.class);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    if (StringUtil.isBlank(subappguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数subappguid为空", "");
                    }
                    String biguid = param.getString("biguid"); // 主题实例guid
                    if (StringUtil.isBlank(biguid)) {
                        return JsonUtils.zwdtRestReturn("0", "必要参数biguid为空", "");
                    }
                    param.put("isxfjssc", "1");
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    // 未申报过，或者是草稿或者是预审退回状态
                    if (auditSpISubapp == null || "2".equals(auditSpISubapp.getStatus()) || "3".equals(auditSpISubapp.getStatus())
                            || ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");// 是否允许子申报
                        String itemguid = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String type = param.getString("type");// 1-保存 2-提交
                        String businessguid = param.getString("businessguid");// 主题guid
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String taskids = param.getString("taskids");// taskids
                        String constructionproperty = param.getString("constructionproperty");// 建设性质
                        String constructionsite = param.getString("constructionsite");// 地点
                        // 1.3 拟开工时间
                        Date itemStartDate = param.getDate("itemstartdate");
                        // 1.4 拟建成时间
                        Date itemFinishDate = param.getDate("itemfinishdate");


                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                                    flag = true;
                                }
                            }

                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        isallowsubitem = "1";
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            Date date = new Date();
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setOperatedate(date);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                // 判断并处理 isusewater 列
                                if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                    auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                }

                                // 判断并处理 ismovewater 列
                                if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                    auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                }

                                // 判断并处理 isuseelec 列
                                if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                    auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                }

                                // 判断并处理 isgas 列
                                if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                    auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                }

                                // 判断并处理 iswarm 列
                                if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                    auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                }

                                // 判断并处理 istx 列
                                if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                    auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                }

                                // 判断并处理 isgd 列
                                if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                    auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                }

                                // 判断并处理 iswaijie 列
                                if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                    auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                }
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("issendzjV3", ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }

                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setOperatedate(date);
                                        auditRsItemBaseinfo.set("subitemcode", subitemcode);// 子项目代码
                                        auditRsItemBaseinfo.set("subappname", subappname);// 子项目名称
                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                                        auditRsItemBaseinfo.setDraft(status);
                                        // 判断并处理 isusewater 列
                                        if (StringUtils.isNotBlank((String) param.get("isusewater"))) {
                                            auditRsItemBaseinfo.put("isusewater", param.getString("isusewater"));
                                        }

                                        // 判断并处理 ismovewater 列
                                        if (StringUtils.isNotBlank((String) param.get("ismovewater"))) {
                                            auditRsItemBaseinfo.put("ismovewater", param.getString("ismovewater"));
                                        }

                                        // 判断并处理 isuseelec 列
                                        if (StringUtils.isNotBlank((String) param.get("isuseelec"))) {
                                            auditRsItemBaseinfo.put("isuseelec", param.getString("isuseelec"));
                                        }

                                        // 判断并处理 isgas 列
                                        if (StringUtils.isNotBlank((String) param.get("isgas"))) {
                                            auditRsItemBaseinfo.put("isgas", param.getString("isgas"));
                                        }

                                        // 判断并处理 iswarm 列
                                        if (StringUtils.isNotBlank((String) param.get("iswarm"))) {
                                            auditRsItemBaseinfo.put("iswarm", param.getString("iswarm"));
                                        }

                                        // 判断并处理 istx 列
                                        if (StringUtils.isNotBlank((String) param.get("istx"))) {
                                            auditRsItemBaseinfo.put("istx", param.getString("istx"));
                                        }

                                        // 判断并处理 isgd 列
                                        if (StringUtils.isNotBlank((String) param.get("isgd"))) {
                                            auditRsItemBaseinfo.put("isgd", param.getString("isgd"));
                                        }

                                        // 判断并处理 iswaijie 列
                                        if (StringUtils.isNotBlank((String) param.get("iswaijie"))) {
                                            auditRsItemBaseinfo.put("iswaijie", param.getString("iswaijie"));
                                        }
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        setJsxxssAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, taskids, auditOnlineRegister, yjsbusinessguid);
                        // 将子申报的材料初始化状态置为0
                        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (sub != null && !sub.isEmpty()) {
                            sub.setInitmaterial("0");
                            iAuditSpISubapp.updateAuditSpISubapp(sub);
                        }
                        // 先删除之前生成的audit_sp_i_task数据
                        auditSpITaskService.deleteSpITaskBySubappGuid(subappguid);
                        // 这边直接生成audit_sp_i_task数据
                        // ["8f98c8f9-7ede-11ea-83ce-286ed488c9a1","90510739-05eb-407f-b0ed-cf01ea0d5a6d"]
                        if (StringUtil.isNotBlank(taskids)) {
                            String[] ids = taskids.split(",");
                            for (String id : ids) {
                                if (StringUtil.isNotBlank(id)) {
                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
                                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(id).getResult();
                                    if (auditTask != null && basetask != null) {
                                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), UUID.randomUUID().toString(),
                                                basetask.getSflcbsx());
                                    }

                                }
                            }
                        }
                        log.info("=======结束调用savejsxxsszinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }
                    else {
                        log.info("=======结束调用savejsxxsszinfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用savejsxxsszinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======savejsxxsszinfo接口参数：params【" + params + "】=======");
            log.info("=======savejsxxsszinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "savejsxxsszinfo异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 子申报方法
     */
    public void setJsxxssAuditSpISubapp(String biguid, String businessguid, String subappguid, String phaseguid, String itemguid, String subappname, String type, String taskids,
                                        AuditOnlineRegister auditOnlineRegister, String yjsbusinessguid) {
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("biguid", biguid);
        conditionUtil.isBlank("parentid");
        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
        if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
            String field = "creditcode";
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                field = "organcode";
            }
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField(field, StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (auditSpISubapp == null) {
                auditSpISubapp = new AuditSpISubapp();
                auditSpISubapp.setCreatedate(new Date());
                auditSpISubapp.setRowguid(subappguid);
            }
            auditSpISubapp.setOperatedate(new Date());
            auditSpISubapp.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
            auditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
            auditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
            auditSpISubapp.setApplyerway("10");
            auditSpISubapp.setBiguid(biguid);
            auditSpISubapp.setBusinessguid(businessguid);
            auditSpISubapp.set("yjsbusinessguid", yjsbusinessguid);
            if (StringUtil.isNotBlank(yjsbusinessguid)) {
                AuditSpBusiness spBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(yjsbusinessguid).getResult();
                if (spBusiness != null) {
                    auditSpISubapp.set("yjsname", spBusiness.getBusinessname());
                }
            }
            if (StringUtil.isBlank(phaseguid)) {
                // 根据businessguid查询【audit_sp_phase】c查询phaseid包含3的记录取第一条的rowguid
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("businessguid", businessguid);
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
                if (auditSpPhases != null && auditSpPhases.size() > 0) {
                    AuditSpPhase auditSpPhase = auditSpPhases.get(0);
                    phaseguid = auditSpPhase.getRowguid();
                }
            }
            auditSpISubapp.setPhaseguid(phaseguid);
            if (StringUtil.isNotBlank(taskids)) {
                String[] itaskGuids = taskids.split(","); // 材料标识数组
                int guidsCount = itaskGuids.length;
                StringBuffer spitaskguids = new StringBuffer();
                for (int i = 0; i < guidsCount; i++) {
                    String itaskGuid = itaskGuids[i].replaceAll("'", "");
                    ;
                    itaskGuid = itaskGuid.replaceAll("\"", "");
                    spitaskguids.append("'" + itaskGuid + "',");
                    // 3、获取事项信息
                    AuditSpITask auditSpITask = auditSpITaskService.getAuditSpITaskDetail(itaskGuid).getResult();
                    if (auditSpITask != null && !ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus()) && (!ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                            || (ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus()) && StringUtil.isBlank(auditSpITask.getProjectguid())))) {
                        auditSpITask.setStatus("1");
                        auditSpITaskService.updateAuditSpITask(auditSpITask);
                    }
                }
            }

            // 修改subapp的状态为已评审 因为水电气不走内外预审 需要在生成子办件的时候进行事项初始化
            if ("2".equals(type)) {
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YPS);
            }
            else {
                auditSpISubapp.setStatus("2");// 水电气申报保存
            }
            auditSpISubapp.setYewuguid(itemguid);
            auditSpISubapp.setSubappname("建设项目涉水" + "-" + subappname);
            AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (sub != null) {
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
            else {
                iAuditSpISubapp.addSubapp(auditSpISubapp);
            }

            if ("2".equals(type)) {
                // 插入事项实例
                for (String taskid : taskids.split(",")) {
                    log.info("====20241204====taskid" + taskid);
                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(taskid.replaceAll("'", "")).getResult();
                    log.info("====20241204====auditTask" + auditTask);

                    if (auditTask != null) {
                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), "0");

                    }

                }
            }

        }
    }

    /**
     * 子申报方法
     */
    public void setSrqlhbzAuditSpISubapp(String biguid, String businessguid, String subappguid, String phaseguid, String itemguid, String subappname, String type, String taskids,
                                         AuditOnlineRegister auditOnlineRegister, String yjsbusinessguid) {
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("biguid", biguid);
        conditionUtil.isBlank("parentid");
        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
        if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
            String field = "creditcode";
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                field = "organcode";
            }
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField(field, StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (auditSpISubapp == null) {
                auditSpISubapp = new AuditSpISubapp();
                auditSpISubapp.setCreatedate(new Date());
                auditSpISubapp.setRowguid(subappguid);
            }
            auditSpISubapp.setOperatedate(new Date());
            auditSpISubapp.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
            auditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
            auditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
            auditSpISubapp.setApplyerway("10");
            auditSpISubapp.setBiguid(biguid);
            auditSpISubapp.setBusinessguid(businessguid);
            auditSpISubapp.set("yjsbusinessguid", yjsbusinessguid);
            if (StringUtil.isNotBlank(yjsbusinessguid)) {
                AuditSpBusiness spBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(yjsbusinessguid).getResult();
                if (spBusiness != null) {
                    auditSpISubapp.set("yjsname", spBusiness.getBusinessname());
                }
            }
            if (StringUtil.isBlank(phaseguid)) {
                // 根据businessguid查询【audit_sp_phase】c查询phaseid包含3的记录取第一条的rowguid
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("businessguid", businessguid);
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getAuditSpPhase(sql.getMap()).getResult();
                if (auditSpPhases != null && auditSpPhases.size() > 0) {
                    AuditSpPhase auditSpPhase = auditSpPhases.get(0);
                    phaseguid = auditSpPhase.getRowguid();
                }
            }
            // type = "2";
            auditSpISubapp.setPhaseguid(phaseguid);
            // auditSpISubapp.set("wttaskids", taskids);
            // auditSpISubapp.set("is_sdq", "1");
            String taskareacode = "";
            if (StringUtil.isNotBlank(taskids)) {
                // String taskid = taskids.split(",")[0].replaceAll("'", "");
                String[] itaskGuids = taskids.split(","); // 材料标识数组
                int guidsCount = itaskGuids.length;
                StringBuffer spitaskguids = new StringBuffer();
                for (int i = 0; i < guidsCount; i++) {
                    String itaskGuid = itaskGuids[i].replaceAll("'", "");
                    ;
                    itaskGuid = itaskGuid.replaceAll("\"", "");
                    spitaskguids.append("'" + itaskGuid + "',");
                    // 3、获取事项信息
                    AuditSpITask auditSpITask = auditSpITaskService.getAuditSpITaskDetail(itaskGuid).getResult();
                    if (auditSpITask != null && !ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpITask.getStatus()) && (!ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus())
                            || (ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpITask.getStatus()) && StringUtil.isBlank(auditSpITask.getProjectguid())))) {
                        auditSpITask.setStatus("1");
                        auditSpITaskService.updateAuditSpITask(auditSpITask);
                    }
                }
            }

            // 修改subapp的状态为已评审 因为水电气不走内外预审 需要在生成子办件的时候进行事项初始化
            if ("2".equals(type)) {
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YPS);
            }
            else {
                auditSpISubapp.setStatus("2");// 水电气申报保存
            }
            auditSpISubapp.setYewuguid(itemguid);
            auditSpISubapp.setSubappname("水电气暖网联合报装" + "-" + subappname);
            AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (sub != null) {
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
            else {
                iAuditSpISubapp.addSubapp(auditSpISubapp);
            }

            if ("2".equals(type)) {
                // 插入事项实例
                for (String taskid : taskids.split(",")) {
                    log.info("====20241204====taskid" + taskid);
                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(taskid.replaceAll("'", "")).getResult();
                    log.info("====20241204====auditTask" + auditTask);
                    /*
                     * AuditSpBasetask basetask =
                     * iauditspbasetask.getAuditSpBasetaskBytaskid(taskid.
                     * replaceAll("'", ""))
                     * .getResult();
                     * if (auditTask != null && basetask != null) {
                     * auditSpITaskService.addTaskInstance(businessguid, biguid,
                     * phaseguid, auditTask.getRowguid(),
                     * auditTask.getTaskname(), subappguid,
                     * auditTask.getOrdernum() == null ? 0 :
                     * auditTask.getOrdernum(), auditTask.getAreacode(),
                     * basetask.getSflcbsx());
                     * }
                     */

                    // auditTask =
                    // iaudittask.getAuditTaskByGuid("C744D713A8D247CF906EB7315252AA83",
                    // false).getResult();

                    if (auditTask != null) {
                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), "0");

                    }

                }
            }

        }
    }

    /**
     * 保存道路挖掘一件事申报
     */
    @RequestMapping(value = "/savedlwjyjsinfo", method = RequestMethod.POST)
    public String savedlwjyjsinfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用savedlwjyjsinfo接口======" + params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String phasenum = param.getString("phasenum");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    String type = param.getString("type");// 1-保存 2-提交
                    if ("2".equals(type) && auditSpISubapp == null) {
                        return JsonUtils.zwdtRestReturn("0", "请先点击保存", "");
                    }

                    // 定义返回值
                    JSONObject retJson = new JSONObject();
                    if (auditSpISubapp == null || "2".equals(auditSpISubapp.getStatus()) || "3".equals(auditSpISubapp.getStatus())
                            || ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");
                        String itemguid = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String biguid = param.getString("biguid"); // 主题实例guid
                        String businessguid = param.getString("businessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String xmzb = param.getString("xmzb");// 子申报名称
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 一件事主题标识
                        String taskids = param.getString("taskids");// taskids
                        String formids = param.getString("formids");// formids

                        String constructionproperty = param.getString("constructionproperty");// 建设性质
                        String constructionsite = param.getString("constructionsite");// 地点
                        // 1.3 拟开工时间
                        Date itemStartDate = param.getDate("itemstartdate");
                        // 1.4 拟建成时间
                        Date itemFinishDate = param.getDate("itemfinishdate");

                        // 道路挖掘一件事字段
                        String sgdepartment = param.getString("sgdepartment");
                        String sgdepartmentaddress = param.getString("sgdepartmentaddress");
                        String sgdepartmentcreditcode = param.getString("sgdepartmentcreditcode");
                        String sgdepartmentlegal = param.getString("sgdepartmentlegal");
                        String sgdepartmentlegalid = param.getString("sgdepartmentlegalid");
                        String sbreason = param.getString("sbreason");
                        String zgrname = param.getString("zgrname");
                        String zgrid = param.getString("zgrid");
                        String zgrmobile = param.getString("zgrmobile");
                        String jbrname = param.getString("jbrname");
                        String jbrid = param.getString("jbrid");
                        String jbrmobile = param.getString("jbrmobile");
                        String ghxk_cliengguid = param.getString("ghxk_cliengguid");
                        String sgfa_cliengguid = param.getString("sgfa_cliengguid");
                        String syt_cliengguid = param.getString("syt_cliengguid");

                        // 开始判断项目代码和子项目代码是否重复
                        AuditRsItemBaseinfo rsItemBaseinfo = null;
                        if (StringUtil.isNotBlank(itemcode)) {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", itemcode);
                            sql.nq("rowguid", itemguid);
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                            if (ValidateUtil.isNotBlankCollection(auditRsItemBaseinfos)) {
                                return JsonUtils.zwdtRestReturn("0", "项目代码存在", "");
                            }
                        }
                        rsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();

                        // 查询出这家企业
                        SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                        String field = "creditcode";
                        if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(rsItemBaseinfo.getItemlegalcerttype())) {
                            field = "organcode";
                        }
                        companySqlConditionUtil.eq(field, StringUtils.isNotBlank(rsItemBaseinfo.getItemlegalcreditcode())?rsItemBaseinfo.getItemlegalcreditcode():rsItemBaseinfo.getItemlegalcertnum());
                        companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        companySqlConditionUtil.eq("isactivated", "1");
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                .getResult();
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = null;
                        if (!auditRsCompanyBaseinfos.isEmpty() && auditRsCompanyBaseinfos.size() > 0) {
                            auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        }

//                        StringBuilder builder = new StringBuilder();
//                        if (StringUtil.isNotBlank(taskids)) {
//                            String[] ids = taskids.split(",");
//                            for (String id : ids) {
//                                if (StringUtil.isNotBlank(id)) {
//                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
//
//                                    AuditTaskExtension extension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
//                                    if (extension != null && StringUtil.isNotBlank(extension.getStr("formid"))) {
//                                        builder.append(extension.getStr("formid")).append(",");
//                                    }
//
//                                }
//                            }
//                        }
                        // 生成word并返回附件标识
                        JSONObject paramsJson = new JSONObject();
                        boolean isbotanypromise = false;
                        boolean isfacilitiespromise = false;

                        //判断事项名称里面是否包含XXX
                        List<String> tasknamelist = new ArrayList<>();
                        if (StringUtil.isNotBlank(taskids)) {
                            String[] ids = taskids.split(",");
                            for (String id : ids) {
                                if (StringUtil.isNotBlank(id)) {
                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
                                    if (auditTask != null && StringUtil.isNotBlank(auditTask.getTaskname())) {
                                        if (auditTask.getTaskname().contains("砍伐城市树木、迁移古树名木审批") || auditTask.getTaskname().contains("临时占用城市绿化用地审批") || auditTask.getTaskname().contains("改变绿化规划、绿化用地的使用性质审批")) {
                                            isbotanypromise = true;
                                        }
                                        if (auditTask.getTaskname().contains("占用、挖掘城市道路审批") || auditTask.getTaskname().contains("依附于城市道路建设各种管线、杆线等设施审批") || auditTask.getTaskname().contains("城市桥梁上架设各类市政管线审批") || auditTask.getTaskname().contains("拆除、改动、迁移城市公共供水设施审核") || auditTask.getTaskname().contains("拆除、改动城镇排水与污水处理设施审核")) {
                                            isfacilitiespromise = true;
                                        }
                                    }
                                }
                            }
                        }


                        //若存在则读取文档【XXX单位关于工程建设涉及城市绿地.docx】插入到申请表模板的botanypromise域当中
                        if (isbotanypromise) {
//                            String licenseName = ClassPathUtil.getClassesPath() + "EpointAsposeWords.lic";
//                            License license = new License();
//                            license.setLicense(licenseName);
//                            Document doc = new Document(ClassPathUtil.getDeployWarPath() + "template/xxx单位关于工程建设涉及城市绿地.docx");
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            String rtfContent = "";
//                            doc.save(baos, SaveFormat.HTML);
//                            rtfContent = new String(baos.toByteArray(), StandardCharsets.UTF_8);
//                            org.jsoup.nodes.Document doc1 = Jsoup.parse(rtfContent);
//                            // 获取<body>标签内的内容
//                            rtfContent = doc1.body().html();
//
//                            param.put("botanypromise",rtfContent);
                            StringBuffer buffer = new StringBuffer();
                            // 创建File对象，指向要读取的Word文档
                            File file = new File(ClassPathUtil.getDeployWarPath() + "template/xxx单位关于工程建设涉及城市绿地.docx");
                            // 创建FileInputStream对象，用于读取文件内容
                            FileInputStream fis = new FileInputStream(file);
                            // 创建XWPFDocument对象，表示一个Word文档
                            XWPFDocument document = new XWPFDocument(fis);
                            // 遍历文档中的段落
                            for (XWPFParagraph paragraph : document.getParagraphs()) {
                                String text = paragraph.getText();
                                buffer.append(text);
                            }
                            param.put("botanypromise", buffer.toString());
                        }
                        //则读取文档【道路及周边设施保护质量承诺书.docx】插入到申请表模板的facilitiespromise域当中
                        if (isfacilitiespromise) {
//                            String licenseName = ClassPathUtil.getClassesPath() + "EpointAsposeWords.lic";
//                            License license = new License();
//                            license.setLicense(licenseName);
//                            Document doc = new Document(ClassPathUtil.getDeployWarPath() + "template/道路及周边设施保护质量承诺书.docx");
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            String rtfContent = "";
//                            doc.save(baos, SaveFormat.HTML);
//                            rtfContent = new String(baos.toByteArray(), StandardCharsets.UTF_8);
//                            org.jsoup.nodes.Document doc1 = Jsoup.parse(rtfContent);
//                            // 获取<body>标签内的内容
//                            rtfContent = doc1.body().html();
//
//                            param.put("facilitiespromise",rtfContent);
                            StringBuffer buffer = new StringBuffer();
                            // 创建File对象，指向要读取的Word文档
                            File file = new File(ClassPathUtil.getDeployWarPath() + "template/道路及周边设施保护质量承诺书.docx");
                            // 创建FileInputStream对象，用于读取文件内容
                            FileInputStream fis = new FileInputStream(file);
                            // 创建XWPFDocument对象，表示一个Word文档
                            XWPFDocument document = new XWPFDocument(fis);
                            // 遍历文档中的段落
                            for (XWPFParagraph paragraph : document.getParagraphs()) {
                                String text = paragraph.getText();
                                buffer.append(text);
                            }
                            param.put("facilitiespromise", buffer.toString());
                        }

                        if (auditRsCompanyBaseinfo != null) {
                            param.put("applyname", auditRsCompanyBaseinfo.getOrganname());
                            param.put("applyaddress", auditRsCompanyBaseinfo.getRegisteraddress());
                            param.put("applycode", auditRsCompanyBaseinfo.getCreditcode());
                            param.put("applylegal", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                            param.put("applycertnum", auditRsCompanyBaseinfo.getOrganlegal());
                        }
                        param.put("constructionproperty", iCodeItemsService.getItemTextByCodeName("建设性质", constructionproperty));
                        paramsJson.put("rowguid", subappguid);
                        if (StringUtil.isNotBlank(formids)) {
                            paramsJson.put("formids", formids);
                            String attachGuid = GenerateAttachUtil.generateWordFile(paramsJson, param);
                            if (StringUtil.isNotBlank(attachGuid)) {
                                String downloadUrl = "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid;
                                retJson.put("attachUrl", downloadUrl);
                            }
                        }

                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(phasenum)) {
                            switch (phasenum) {
                                case "lxydghxk": // 第一阶段
                                    saveLxydghxkInfo(param);
                                    break;
                                case "gcjsxk": // 第二阶段
                                    saveGcjsxkInfo(param);
                                    break;
                                case "sgxk": // 第三阶段
                                    saveSgxkInfo(param);
                                    break;
                                case "jgys": // 第四阶段
                                    saveJgysInfo(param);
                                    break;
                                case "zszx": // 装饰装修
                                    saveJgysInfo(param);
                                    break;
                            }
                        }
                        isallowsubitem = "1";
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("xmzb", xmzb);

                                auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);

                                // 道路挖掘一件事特有字段
                                auditRsItemBaseinfo.set("sgdepartment", sgdepartment);
                                auditRsItemBaseinfo.set("sgdepartmentaddress", sgdepartmentaddress);
                                auditRsItemBaseinfo.set("sgdepartmentcreditcode", sgdepartmentcreditcode);
                                auditRsItemBaseinfo.set("sgdepartmentlegal", sgdepartmentlegal);
                                auditRsItemBaseinfo.set("sgdepartmentlegalid", sgdepartmentlegalid);
                                auditRsItemBaseinfo.set("sbreason", sbreason);
                                auditRsItemBaseinfo.set("zgrname", zgrname);
                                auditRsItemBaseinfo.set("zgrid", zgrid);
                                auditRsItemBaseinfo.set("zgrmobile", zgrmobile);
                                auditRsItemBaseinfo.set("jbrname", jbrname);
                                auditRsItemBaseinfo.set("jbrid", jbrid);
                                auditRsItemBaseinfo.set("jbrmobile", jbrmobile);
                                auditRsItemBaseinfo.set("ghxk_cliengguid", ghxk_cliengguid);
                                auditRsItemBaseinfo.set("sgfa_cliengguid", sgfa_cliengguid);
                                auditRsItemBaseinfo.set("syt_cliengguid", syt_cliengguid);

                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
//                                setItemInfo(auditRsItemBaseinfo, param);
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);

                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);

                                        // 道路挖掘一件事特有字段
                                        auditRsItemBaseinfo.set("sgdepartment", sgdepartment);
                                        auditRsItemBaseinfo.set("sgdepartmentaddress", sgdepartmentaddress);
                                        auditRsItemBaseinfo.set("sgdepartmentcreditcode", sgdepartmentcreditcode);
                                        auditRsItemBaseinfo.set("sgdepartmentlegal", sgdepartmentlegal);
                                        auditRsItemBaseinfo.set("sgdepartmentlegalid", sgdepartmentlegalid);
                                        auditRsItemBaseinfo.set("sbreason", sbreason);
                                        auditRsItemBaseinfo.set("zgrname", zgrname);
                                        auditRsItemBaseinfo.set("zgrid", zgrid);
                                        auditRsItemBaseinfo.set("zgrmobile", zgrmobile);
                                        auditRsItemBaseinfo.set("jbrname", jbrname);
                                        auditRsItemBaseinfo.set("jbrid", jbrid);
                                        auditRsItemBaseinfo.set("jbrmobile", jbrmobile);
                                        auditRsItemBaseinfo.set("ghxk_cliengguid", ghxk_cliengguid);
                                        auditRsItemBaseinfo.set("sgfa_cliengguid", sgfa_cliengguid);
                                        auditRsItemBaseinfo.set("syt_cliengguid", syt_cliengguid);

//                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);

                                        auditRsItemBaseinfo.set("CONSTRUCTIONPROPERTY ", constructionproperty);// 建设性质
                                        auditRsItemBaseinfo.setConstructionsite(constructionsite);
                                        auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                                        auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);

                                        // 道路挖掘一件事特有字段
                                        auditRsItemBaseinfo.set("sgdepartment", sgdepartment);
                                        auditRsItemBaseinfo.set("sgdepartmentaddress", sgdepartmentaddress);
                                        auditRsItemBaseinfo.set("sgdepartmentcreditcode", sgdepartmentcreditcode);
                                        auditRsItemBaseinfo.set("sgdepartmentlegal", sgdepartmentlegal);
                                        auditRsItemBaseinfo.set("sgdepartmentlegalid", sgdepartmentlegalid);
                                        auditRsItemBaseinfo.set("sbreason", sbreason);
                                        auditRsItemBaseinfo.set("zgrname", zgrname);
                                        auditRsItemBaseinfo.set("zgrid", zgrid);
                                        auditRsItemBaseinfo.set("zgrmobile", zgrmobile);
                                        auditRsItemBaseinfo.set("jbrname", jbrname);
                                        auditRsItemBaseinfo.set("jbrid", jbrid);
                                        auditRsItemBaseinfo.set("jbrmobile", jbrmobile);
                                        auditRsItemBaseinfo.set("ghxk_cliengguid", ghxk_cliengguid);
                                        auditRsItemBaseinfo.set("sgfa_cliengguid", sgfa_cliengguid);
                                        auditRsItemBaseinfo.set("syt_cliengguid", syt_cliengguid);
//                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        newSetAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, yjsbusinessguid);

                        // 将子申报的材料初始化状态置为0
                        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (sub != null && !sub.isEmpty()) {
                            sub.setInitmaterial("0");
                            iAuditSpISubapp.updateAuditSpISubapp(sub);
                        }
                        // 先删除之前生成的audit_sp_i_task数据
                        auditSpITaskService.deleteSpITaskBySubappGuid(subappguid);
                        // 这边直接生成audit_sp_i_task数据
                        // ["8f98c8f9-7ede-11ea-83ce-286ed488c9a1","90510739-05eb-407f-b0ed-cf01ea0d5a6d"]
                        if (StringUtil.isNotBlank(taskids)) {
                            String[] ids = taskids.split(",");
                            for (String id : ids) {
                                if (StringUtil.isNotBlank(id)) {
                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
                                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(id).getResult();
                                    if (auditTask != null && basetask != null) {
                                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), UUID.randomUUID().toString(),
                                                basetask.getSflcbsx());
                                    }

                                }
                            }
                        }

                        // 办结
                        // iAuditSpISubapp.updateSubapp(subappguid,
                        // ZwfwConstant.LHSP_Status_YBJ, null);

                        log.info("=======结束调用savedlwjyjsinfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "信息存储成功", retJson.toString());
                    }
                    else {
                        log.info("=======结束调用savedlwjyjsinfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用savedlwjyjsinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======savedlwjyjsinfo接口参数：params【" + params + "】=======");
            log.info("=======savedlwjyjsinfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存或提交电子表单信息失败!", "");
        }
    }

    /**
     * 工程建设开工一件事的申报
     */
    @RequestMapping(value = "/saveJsxmkgyjsInfo", method = RequestMethod.POST)
    public String saveJsxmkgyjsInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用saveJsxmkgyjsInfo接口======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String phasenum = param.getString("phasenum");
                    if (StringUtil.isBlank(phasenum)){
                        phasenum="sgxk";
                    }
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    String type = param.getString("type");// 1-保存 2-提交
                    if ("2".equals(type) && auditSpISubapp == null) {
                        return JsonUtils.zwdtRestReturn("0", "请先点击保存", "");
                    }
                    if (StringUtil.isNotBlank(phasenum)
                            && (auditSpISubapp == null || "3".equals(auditSpISubapp.getStatus()) || ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus()))) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");
                        String itemguid = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String biguid = param.getString("biguid"); // 主题实例guid
                        String businessguid = param.getString("businessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String xmzb = param.getString("xmzb");// 子申报名称
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 一件事主题标识
                        String taskids = param.getString("taskids");// taskids
                        String formid = param.getString("formids");

                        // 开始判断项目代码和子项目代码是否重复
                        if (StringUtil.isNotBlank(itemcode)) {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", itemcode);
                            sql.nq("rowguid", itemguid);
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                            if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                return JsonUtils.zwdtRestReturn("0", "项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(phasenum)) {
                            switch (phasenum) {
                                case "lxydghxk": // 第一阶段
                                    saveLxydghxkInfo(param);
                                    break;
                                case "gcjsxk": // 第二阶段
                                    saveGcjsxkInfo(param);
                                    break;
                                case "sgxk": // 第三阶段
                                    saveSgxkInfo(param);
                                    break;
                                case "jgys": // 第四阶段
                                    saveJgysInfo(param);
                                    break;
                                case "zszx": // 装饰装修
                                    saveJgysInfo(param);
                                    break;
                            }
                        }

                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("xmzb", xmzb);

                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                setItemInfo(auditRsItemBaseinfo, param);
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        newSetAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, yjsbusinessguid);

                        // 将子申报的材料初始化状态置为0
                        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (sub != null && !sub.isEmpty()) {
                            sub.setInitmaterial("0");
                            iAuditSpISubapp.updateAuditSpISubapp(sub);
                        }
                        // 先删除之前生成的audit_sp_i_task数据
                        auditSpITaskService.deleteSpITaskBySubappGuid(subappguid);
                        // 这边直接生成audit_sp_i_task数据
                        // ["8f98c8f9-7ede-11ea-83ce-286ed488c9a1","90510739-05eb-407f-b0ed-cf01ea0d5a6d"]
                        if (StringUtil.isNotBlank(taskids)) {
                            String[] ids = taskids.split(",");
                            for (String id : ids) {
                                if (StringUtil.isNotBlank(id)) {
                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
                                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(id).getResult();
                                    if (auditTask != null && basetask != null) {
                                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), UUID.randomUUID().toString(),
                                                basetask.getSflcbsx());
                                    }

                                }
                            }
                        }
                        //生成附件
                        JSONObject retJson=new JSONObject();

                        if (StringUtil.isNotBlank(formid)) {
                            JSONObject paramsJson = new JSONObject();
                            paramsJson.put("formids", formid);
                            paramsJson.put("rowguid", subappguid);
                            String attachGuid = GenerateAttachUtil.generateJskgyjsWordFile(paramsJson, param);
                            if (StringUtil.isNotBlank(attachGuid)) {
                                String downloadUrl = "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + attachGuid;
                                retJson.put("attachUrl", downloadUrl);
                            }
                        }

                        // 办结
                        // iAuditSpISubapp.updateSubapp(subappguid,
                        // ZwfwConstant.LHSP_Status_YBJ, null);

                        log.info("=======结束调用saveJsxmkgyjsInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "信息存储成功", retJson);
                    }
                    else {
                        log.info("=======结束调用saveJsxmkgyjsInfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用saveJsxmkgyjsInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveJsxmkgyjsInfo接口参数：params【" + params + "】=======");
            log.info("=======saveJsxmkgyjsInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存或提交工程建设开工一件事的申报失败!", "");
        }
    }

    /**
     * 保存审批申请信息
     */
    @RequestMapping(value = "/newSaveAuditSpItemInfo", method = RequestMethod.POST)
    public String newSaveAuditSpItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用newSaveAuditSpItemInfo接口======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String phasenum = param.getString("phasenum");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    String type = param.getString("type");// 1-保存 2-提交
                    if ("2".equals(type) && auditSpISubapp == null) {
                        return JsonUtils.zwdtRestReturn("0", "请先点击保存", "");
                    }
                    if (StringUtil.isNotBlank(phasenum)
                            && (auditSpISubapp == null || "3".equals(auditSpISubapp.getStatus()) || ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus()))) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");
                        String itemguid = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String biguid = param.getString("biguid"); // 主题实例guid
                        String businessguid = param.getString("businessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String xmzb = param.getString("xmzb");// 子申报名称
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 一件事主题标识
                        String taskids = param.getString("taskids");// taskids
                        // 开始判断项目代码和子项目代码是否重复
                        if (StringUtil.isNotBlank(itemcode)) {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", itemcode);
                            sql.nq("rowguid", itemguid);
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                            if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                return JsonUtils.zwdtRestReturn("0", "项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(phasenum)) {
                            switch (phasenum) {
                                case "lxydghxk": // 第一阶段
                                    saveLxydghxkInfo(param);
                                    break;
                                case "gcjsxk": // 第二阶段
                                    saveGcjsxkInfo(param);
                                    break;
                                case "sgxk": // 第三阶段
                                    saveSgxkInfo(param);
                                    break;
                                case "jgys": // 第四阶段
                                    saveJgysInfo(param);
                                    break;
                                case "zszx": // 装饰装修
                                    saveJgysInfo(param);
                                    break;
                            }
                        }

                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("xmzb", xmzb);

                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                setItemInfo(auditRsItemBaseinfo, param);
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }

                        // 新增子申报
                        newSetAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, yjsbusinessguid);

                        // 将子申报的材料初始化状态置为0
                        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                        if (sub != null && !sub.isEmpty()) {
                            sub.setInitmaterial("0");
                            iAuditSpISubapp.updateAuditSpISubapp(sub);
                        }
                        // 先删除之前生成的audit_sp_i_task数据
                        auditSpITaskService.deleteSpITaskBySubappGuid(subappguid);
                        // 这边直接生成audit_sp_i_task数据
                        // ["8f98c8f9-7ede-11ea-83ce-286ed488c9a1","90510739-05eb-407f-b0ed-cf01ea0d5a6d"]
                        if (StringUtil.isNotBlank(taskids)) {
                            String[] ids = taskids.split(",");
                            for (String id : ids) {
                                if (StringUtil.isNotBlank(id)) {
                                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(id).getResult();
                                    AuditSpBasetask basetask = iauditspbasetask.getAuditSpBasetaskBytaskid(id).getResult();
                                    if (auditTask != null && basetask != null) {
                                        auditSpITaskService.addTaskInstance(businessguid, biguid, phaseguid, auditTask.getRowguid(), auditTask.getTaskname(), subappguid,
                                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode(), UUID.randomUUID().toString(),
                                                basetask.getSflcbsx());
                                    }

                                }
                            }
                        }

                        // 办结
                        // iAuditSpISubapp.updateSubapp(subappguid,
                        // ZwfwConstant.LHSP_Status_YBJ, null);

                        log.info("=======结束调用newSaveAuditSpItemInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "信息存储成功", "");
                    }
                    else {
                        log.info("=======结束调用newSaveAuditSpItemInfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用newSaveAuditSpItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======newSaveAuditSpItemInfo接口参数：params【" + params + "】=======");
            log.info("=======newSaveAuditSpItemInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存或提交电子表单信息失败!", "");
        }
    }

    /**
     * 保存审批申请信息
     */
    @RequestMapping(value = "/saveAuditSpItemInfo", method = RequestMethod.POST)
    public String saveAuditSpItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用saveAuditPhaseOne接口======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String phasenum = param.getString("phasenum");
                    String subappguid = param.getString("subappguid");// 申报唯一标识
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    if (StringUtil.isNotBlank(phasenum)
                            && (auditSpISubapp == null || "3".equals(auditSpISubapp.getStatus()) || ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus()))) {
                        // 如果选择了子项目，新增子项目
                        String isallowsubitem = param.getString("isallowsubitem");
                        String itemguid = param.getString("itemguid");
                        String subitemcode = param.getString("subitemcode");
                        String subitemname = param.getString("subitemname");
                        String type = param.getString("type");// 1-保存 2-提交
                        String biguid = param.getString("biguid"); // 主题实例guid
                        String businessguid = param.getString("businessguid");// 主题guid
                        String phaseguid = param.getString("phaseguid");// 阶段guid
                        String subappname = param.getString("subappname");// 子申报名称
                        String itemcode = param.getString("itemcode");// 子申报名称
                        String xmzb = param.getString("xmzb");// 子申报名称
                        String yjsbusinessguid = param.getString("yjsbusinessguid");// 一件事主题标识
                        // 开始判断项目代码和子项目代码是否重复
                        if (StringUtil.isNotBlank(itemcode)) {
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", itemcode);
                            sql.nq("rowguid", itemguid);
                            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                            if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                return JsonUtils.zwdtRestReturn("0", "项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(subitemcode)) {
                            boolean flag = false;
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("itemcode", subitemcode);
                            if (auditSpISubapp == null) {
                                // subapp 为空，代表是第一次点击提交或者保存，那么如果有项目，就代表重复
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            else {
                                // 不为空的话代表申报过。
                                sql.nq("rowguid", auditSpISubapp.getYewuguid());
                                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(sql.getMap()).getResult();
                                if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
                                    flag = true;
                                }
                            }
                            if (flag) {
                                return JsonUtils.zwdtRestReturn("0", "子项目代码存在", "");
                            }
                        }
                        if (StringUtil.isNotBlank(phasenum)) {
                            switch (phasenum) {
                                case "lxydghxk": // 第一阶段
                                    saveLxydghxkInfo(param);
                                    break;
                                case "gcjsxk": // 第二阶段
                                    saveGcjsxkInfo(param);
                                    break;
                                case "sgxk": // 第三阶段
                                    saveSgxkInfo(param);
                                    break;
                                case "jgys": // 第四阶段
                                    saveJgysInfo(param);
                                    break;
                                case "zszx": // 装饰装修
                                    saveJgysInfo(param);
                                    break;
                            }
                        }

                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isallowsubitem)) {
                            // 判断是提交还是保存，1保存2提交
                            String status = "1";
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                                status = "-1";
                            }
                            if (auditSpISubapp == null) {
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                                auditRsItemBaseinfo.setParentid(itemguid);
                                itemguid = UUID.randomUUID().toString();
                                auditRsItemBaseinfo.setRowguid(itemguid);
                                auditRsItemBaseinfo.setItemcode(subitemcode);
                                auditRsItemBaseinfo.setItemname(subitemname);
                                auditRsItemBaseinfo.setDraft(status);
                                auditRsItemBaseinfo.set("xmzb", xmzb);

                                // 子项目上报字段要设置为0
                                auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                setItemInfo(auditRsItemBaseinfo, param);
                                iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                            }
                            else {
                                // 判断subapp关联的项目是否为主项目，如果是，需重新新增子项目
                                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(auditSpISubapp.getYewuguid()).getResult();
                                if (auditRsItemBaseinfo != null) {
                                    // 如果父项目id是空，证明是主项目
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getParentid())) {
                                        auditRsItemBaseinfo.setParentid(itemguid);
                                        itemguid = UUID.randomUUID().toString();
                                        auditRsItemBaseinfo.setRowguid(itemguid);
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.setIssendzj(ZwfwConstant.CONSTANT_STR_ZERO);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                    else {
                                        itemguid = auditSpISubapp.getYewuguid();
                                        auditRsItemBaseinfo.setItemcode(subitemcode);
                                        auditRsItemBaseinfo.setItemname(subitemname);
                                        auditRsItemBaseinfo.setDraft(status);
                                        auditRsItemBaseinfo.set("xmzb", xmzb);
                                        setItemInfo(auditRsItemBaseinfo, param);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                    }
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "子申报关联项目出错，请联系管理员", "");
                                }
                            }
                        }
                        // 新增子申报
                        setAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, yjsbusinessguid);
                        log.info("=======结束调用saveAuditPhaseOne接口=======");
                        return JsonUtils.zwdtRestReturn("1", "信息存储成功", "");
                    }
                    else {
                        log.info("=======结束调用saveAuditSpItemInfo接口=======");
                        return JsonUtils.zwdtRestReturn("0", "申报状态已发生改变，请勿重复操作。", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用saveAuditPhaseOne接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveAuditPhaseOne接口参数：params【" + params + "】=======");
            log.info("=======saveAuditPhaseOne接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存或提交电子表单信息失败!", "");
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
     * 自增子申报项目代码获取
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getautosubitemcode", method = RequestMethod.POST)
    public String getautosubitemcode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("======开始调用getautosubitemcode接口======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 获取登录用户的标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String itemcode = param.getString("itemcode");
                    SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
                    legalSqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    legalSqlConditionUtil.eq("is_history", "0");
                    legalSqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo.selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
                    if (auditRsCompanyBaseinfos.size() > 0 && !auditRsCompanyBaseinfos.isEmpty()) {
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                        SqlConditionUtil baseinfosql = new SqlConditionUtil();
                        baseinfosql.isNotBlank("parentid");
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                            baseinfosql.eq("itemlegalcreditcode", auditRsCompanyBaseinfo.getCreditcode());
                        }
                        else {
                            baseinfosql.eq("itemlegalcreditcode", auditRsCompanyBaseinfo.getOrgancode());
                        }
                        baseinfosql.setOrderDesc("OperateDate");
                        List<AuditRsItemBaseinfo> baseinfoList = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(baseinfosql.getMap()).getResult();
                        String subitemcode = "";
                        if (baseinfoList != null && !baseinfoList.isEmpty()) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo = baseinfoList.get(0);
                            String code = auditRsItemBaseinfo.getItemcode();
                            String[] split = code.split("-");
                            String s = split[split.length - 1];
                            String s1 = Integer.parseInt(s) + 1 + "";
                            subitemcode = itemcode + s1;
                        }
                        else {
                            subitemcode = itemcode + "0001";
                        }
                        // 定义返回值
                        JSONObject retJson = new JSONObject();
                        retJson.put("subitemcode", subitemcode);
                        return JsonUtils.zwdtRestReturn("1", "获取子申报项目代码成功！", retJson.toString());

                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "未查询到相关企业信息！", "");
                    }


                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                log.info("=======结束调用getautosubitemcode接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getautosubitemcode接口参数：params【" + params + "】=======");
            log.info("=======getautosubitemcode接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "项目代码获取失败!", "");
        }
    }

    /**
     * 加载第一阶段信息
     */
    private JSONObject getLxydghxkInfo(String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "lxydghxk");
        AuditSpSpLxydghxk auditSpSpLxydghxk = iAuditSpSpLxydghxkService.findAuditSpSpLxydghxkBysubappguid(subappguid);
        if (auditSpSpLxydghxk != null) {
            dataJson.put("itemname", auditSpSpLxydghxk.getItemname());// 项目名称
            dataJson.put("subappname", auditSpSpLxydghxk.getSubappname()); // 子申报名称
            dataJson.put("itemcode", auditSpSpLxydghxk.getItemcode()); // 投资平台项目代码
            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpLxydghxk.getProjectlevel())); // 重点项目
            dataJson.put("itemAddress", auditSpSpLxydghxk.getItemaddress()); // 项目地址
            dataJson.put("area", auditSpSpLxydghxk.getArea()); // 区（园区）
            dataJson.put("road", auditSpSpLxydghxk.getRoad()); // 街道
            dataJson.put("eastToArea", auditSpSpLxydghxk.getEasttoarea()); // 四至范围（东至）
            dataJson.put("westToArea", auditSpSpLxydghxk.getWesttoarea()); // 四至范围（西至）
            dataJson.put("southToArea", auditSpSpLxydghxk.getSouthtoarea()); // 四至范围（南至）
            dataJson.put("northToArea", auditSpSpLxydghxk.getNorthtoarea()); // 四至范围（北至）
            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpLxydghxk.getProtyped()));// 立项类型
            dataJson.put("proOu", auditSpSpLxydghxk.getProou()); // 立项部门
            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpLxydghxk.getInvesttyped()));// 投资类型
            dataJson.put("hangyeType", auditSpSpLxydghxk.getHangyetype()); // 行业类别
            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpLxydghxk.getMoneysources())); // 资金来源
            dataJson.put("allMoney", auditSpSpLxydghxk.getAllmoney()); // 总投资
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpLxydghxk.getAreasources())); // 土地来源
            dataJson.put("useLandType", auditSpSpLxydghxk.getUselandtype()); // 规划用地性质
            dataJson.put("areaUsed", auditSpSpLxydghxk.getAreaused());// 用地面积(公顷)
            dataJson.put("newLandType", auditSpSpLxydghxk.getNewlandtype()); // 新增用地面积（公顷）
            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpLxydghxk.getBuildproperties())); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpLxydghxk.getBuildtype())); // 项目类型
            dataJson.put("allBuildArea", auditSpSpLxydghxk.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("overLoadArea", auditSpSpLxydghxk.getOverloadarea()); // 地上
            dataJson.put("downLoadArea", auditSpSpLxydghxk.getDownloadarea()); // 地下
            dataJson.put("buildContent", auditSpSpLxydghxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate",
                    StringUtil.isBlank(auditSpSpLxydghxk.getPlanstartdate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpLxydghxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate",
                    StringUtil.isBlank(auditSpSpLxydghxk.getPlanenddate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpLxydghxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
            dataJson.put("itemSource", getSelectItemList("项目来源", auditSpSpLxydghxk.getStr("itemsource"))); // 项目来源
            dataJson.put("xznxfl", auditSpSpLxydghxk.getXznxfl()); // 新增年综合能源消费量吨标煤（当量值）
            dataJson.put("xznydl", auditSpSpLxydghxk.getXznydl()); // 新增年用电量（万千瓦时）

            dataJson.put("xmjsyj", auditSpSpLxydghxk.getStr("xmjsyj"));
            dataJson.put("xmnxdz", auditSpSpLxydghxk.getStr("xmnxdz"));
            dataJson.put("nydmj", auditSpSpLxydghxk.getStr("nydmj"));
            dataJson.put("njsgm", auditSpSpLxydghxk.getStr("njsgm"));
            dataJson.put("pzydjg", auditSpSpLxydghxk.getStr("pzydjg"));
            dataJson.put("pzydwh", auditSpSpLxydghxk.getStr("pzydwh"));
            dataJson.put("tdyt", auditSpSpLxydghxk.getStr("tdyt"));
            dataJson.put("yjsgm", auditSpSpLxydghxk.getStr("jsgm"));
            dataJson.put("zbj", auditSpSpLxydghxk.getStr("zbj"));
            dataJson.put("zztz", auditSpSpLxydghxk.getStr("zztz"));

        }
        else {
            dataJson.put("projectLevel", getSelectItemList("项目等级")); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型"));// 立项类型
            dataJson.put("investTyped", getSelectItemList("投资类型"));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源")); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地来源")); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质")); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型")); // 项目类型
            dataJson.put("itemSource", getSelectItemList("项目来源")); // 项目类型
        }
        return dataJson;
    }

    /**
     * 加载第二阶段信息
     */
    private JSONObject getGcjsxkInfo(String subappguid) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "gcjsxk");
        AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappguid);
        if (auditSpSpGcjsxk != null) {
            dataJson.put("itemname", auditSpSpGcjsxk.getItemname());// 项目名称
            dataJson.put("subappname", auditSpSpGcjsxk.getSubappname()); // 子申报名称
            dataJson.put("itemcode", auditSpSpGcjsxk.getItemcode()); // 投资平台项目代码
            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpGcjsxk.getProjectlevel())); // 重点项目
            dataJson.put("itemAddress", auditSpSpGcjsxk.getItemaddress()); // 项目地址
            dataJson.put("area", auditSpSpGcjsxk.getArea()); // 区（园区）
            dataJson.put("road", auditSpSpGcjsxk.getRoad()); // 街道
            dataJson.put("eastToArea", auditSpSpGcjsxk.getEasttoarea()); // 四至范围（东至）
            dataJson.put("westToArea", auditSpSpGcjsxk.getWesttoarea()); // 四至范围（西至）
            dataJson.put("southToArea", auditSpSpGcjsxk.getSouthtoarea()); // 四至范围（南至）
            dataJson.put("northToArea", auditSpSpGcjsxk.getNorthtoarea()); // 四至范围（北至）
            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpGcjsxk.getProtyped()));// 立项类型
            dataJson.put("proOu", auditSpSpGcjsxk.getProou()); // 立项部门
            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpGcjsxk.getInvesttyped()));// 投资类型
            dataJson.put("hangyeType", auditSpSpGcjsxk.getHangyetype()); // 行业类别
            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpGcjsxk.getMoneysources())); // 资金来源
            dataJson.put("allMoney", auditSpSpGcjsxk.getAllmoney()); // 总投资
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpGcjsxk.getAreasources())); // 土地来源
            dataJson.put("useLandType", auditSpSpGcjsxk.getUselandtype()); // 规划用地性质
            dataJson.put("areaUsed", auditSpSpGcjsxk.getAreaused());// 用地面积(公顷)
            dataJson.put("newLandType", auditSpSpGcjsxk.getNewlandtype()); // 新增用地面积（公顷）
            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpGcjsxk.getBuildproperties())); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpGcjsxk.getBuildtype())); // 项目类型
            dataJson.put("allBuildArea", auditSpSpGcjsxk.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("overLoadArea", auditSpSpGcjsxk.getOverloadarea()); // 地上
            dataJson.put("downLoadArea", auditSpSpGcjsxk.getDownloadarea()); // 地下
            dataJson.put("buildContent", auditSpSpGcjsxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate",
                    StringUtil.isBlank(auditSpSpGcjsxk.getPlanstartdate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate",
                    StringUtil.isBlank(auditSpSpGcjsxk.getPlanenddate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
            dataJson.put("itemSource", auditSpSpGcjsxk.getItemsource()); // 项目来源
            dataJson.put("xznxfl", auditSpSpGcjsxk.getXznxfl()); // 新增年综合能源消费量吨标煤（当量值）
            dataJson.put("xznydl", auditSpSpGcjsxk.getXznydl()); // 新增年用电量（万千瓦时）
            dataJson.put("rfywzmj", auditSpSpGcjsxk.getRfywzmj()); // 应履行的人防义务总面积
            dataJson.put("sqjsfk", auditSpSpGcjsxk.getSqjsfk()); // 申请建设防空地下室面积
            dataJson.put("dxsly", auditSpSpGcjsxk.getDxsly()); // 申请易地建设防空地下室理由
            dataJson.put("ydjsdxsmj", auditSpSpGcjsxk.getYdjsdxsmj()); // 申请易地建设防空地下室面积
            dataJson.put("ydjszfgzje", auditSpSpGcjsxk.getYdjszfgzje()); // 根据易地建设费征收规则计算的缴费金额（万元）
            dataJson.put("powertime",
                    StringUtil.isBlank(auditSpSpGcjsxk.getDate("powertime")) ? "" : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getDate("powertime"), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("fkdxsjsjfrq",
                    StringUtil.isBlank(auditSpSpGcjsxk.getDate("fkdxsjsjfrq")) ? "" : EpointDateUtil.convertDate2String(auditSpSpGcjsxk.getDate("fkdxsjsjfrq"), "yyyy-MM-dd"));
            dataJson.put("ydjsfjnse", auditSpSpGcjsxk.getStr("ydjsfjnse"));
            dataJson.put("fhlb", auditSpSpGcjsxk.getStr("fhlb"));
            dataJson.put("rfsgtscdw", auditSpSpGcjsxk.getStr("rfsgtscdw"));
            dataJson.put("jjspyjbh", auditSpSpGcjsxk.getStr("jjspyjbh"));
            dataJson.put("rfgcjzmj", auditSpSpGcjsxk.getStr("rfgcjzmj"));

            dataJson.put("powercap", auditSpSpGcjsxk.getStr("powercap")); // 区（园区）
        }
        else {
            dataJson.put("projectLevel", getSelectItemList("项目等级")); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型"));// 立项类型
            dataJson.put("investTyped", getSelectItemList("投资类型"));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源")); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地来源")); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质")); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型")); // 项目类型
        }
        return dataJson;
    }

    /**
     * 加载第三阶段信息
     */
    private JSONObject getSgxkInfo(String subappguid, String hasjianyi) {
        JSONObject dataJson = new JSONObject();
        dataJson.put("phasenum", "sgxk");
        dataJson.put("hasjianyi", hasjianyi);
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid);
        if (auditSpSpSgxk != null) {
            dataJson.put("itemname", auditSpSpSgxk.getItemname());// 项目名称
            dataJson.put("subappname", auditSpSpSgxk.getSubappname()); // 子申报名称
            dataJson.put("itemcode", auditSpSpSgxk.getItemcode()); // 投资平台项目代码
            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpSgxk.getProjectlevel())); // 重点项目
            dataJson.put("itemAddress", auditSpSpSgxk.getItemaddress()); // 项目地址
            dataJson.put("area", auditSpSpSgxk.getArea()); // 区（园区）
            dataJson.put("road", auditSpSpSgxk.getRoad()); // 街道
            dataJson.put("eastToArea", auditSpSpSgxk.getEasttoarea()); // 四至范围（东至）
            dataJson.put("westToArea", auditSpSpSgxk.getWesttoarea()); // 四至范围（西至）
            dataJson.put("southToArea", auditSpSpSgxk.getSouthtoarea()); // 四至范围（南至）
            dataJson.put("northToArea", auditSpSpSgxk.getNorthtoarea()); // 四至范围（北至）
            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpSgxk.getProtyped()));// 立项类型
            dataJson.put("proOu", auditSpSpSgxk.getProou()); // 立项部门
            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpSgxk.getInvesttyped()));// 投资类型
            dataJson.put("hangyeType", auditSpSpSgxk.getHangyetype()); // 行业类别
            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpSgxk.getMoneysources())); // 资金来源
            dataJson.put("allMoney", auditSpSpSgxk.getAllmoney()); // 总投资
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpSgxk.getAreasources())); // 土地来源
            dataJson.put("useLandType", auditSpSpSgxk.getUselandtype()); // 规划用地性质
            dataJson.put("areaUsed", auditSpSpSgxk.getAreaused());// 用地面积(公顷)
            dataJson.put("newLandType", auditSpSpSgxk.getNewlandtype()); // 新增用地面积（公顷）
            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpSgxk.getBuildproperties())); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpSgxk.getBuildtype())); // 项目类型
            dataJson.put("xmfl", getSelectItemList("项目分类", auditSpSpSgxk.getStr("xmfl"))); // 项目类型
            dataJson.put("allBuildArea", auditSpSpSgxk.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("overLoadArea", auditSpSpSgxk.getOverloadarea()); // 地上
            dataJson.put("downLoadArea", auditSpSpSgxk.getDownloadarea()); // 地下
            dataJson.put("buildContent", auditSpSpSgxk.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate",
                    StringUtil.isBlank(auditSpSpSgxk.getPlanstartdate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpSgxk.getPlanenddate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
            dataJson.put("lxwh", auditSpSpSgxk.getLxwh()); // 立项文号
            dataJson.put("lxrq", StringUtil.isBlank(auditSpSpSgxk.getLxrq()) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getLxrq(), "yyyy-MM-dd")); // 立项日期
            dataJson.put("lxjb", auditSpSpSgxk.getLxjb()); // 立项级别
            dataJson.put("xmbm", auditSpSpSgxk.getXmbm()); // 项目编码
            dataJson.put("gcyt", getSelectItemList("工程用途", auditSpSpSgxk.getGcyt())); // 工程用途
            dataJson.put("gcfl", auditSpSpSgxk.getGcfl()); // 工程分类
            dataJson.put("zftzgs", auditSpSpSgxk.getZftzgs()); // 政府投资概算
            dataJson.put("ghyzzpl", auditSpSpSgxk.getGhyzzpl()); // 规划预制装配率
            dataJson.put("xmbh", auditSpSpSgxk.getStr("xmbh")); // 项目编号
            dataJson.put("yxsfwjgyh", auditSpSpSgxk.getStr("yxsfwjgyh"));
            dataJson.put("yxsfwjgzh", auditSpSpSgxk.getStr("yxsfwjgzh"));
            dataJson.put("htgq", auditSpSpSgxk.getStr("htgq")); // 合同工期
            dataJson.put("xmszsf", auditSpSpSgxk.getStr("xmszsf")); // 项目所在省份
            dataJson.put("xmszcs", auditSpSpSgxk.getStr("xmszcs")); // 项目所在城市
            dataJson.put("lxpfjg", auditSpSpSgxk.getStr("lxpfjg")); // 立项批复机关
            dataJson.put("lxpfsj", StringUtil.isBlank(auditSpSpSgxk.getDate("lxpfsj")) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("lxpfsj"), "yyyy-MM-dd")); // 立项批复时间
            dataJson.put("zbtzsbh", auditSpSpSgxk.getStr("zbtzsbh")); // 中标通知书编号
            dataJson.put("sgtsxmbh", auditSpSpSgxk.getStr("sgtsxmbh")); // 施工图审查项目编号
            dataJson.put("jsydghxkzbh", auditSpSpSgxk.getStr("jsydghxkzbh")); // 建设用地规划许可证编号
            dataJson.put("jsgcghxkzbh", auditSpSpSgxk.getStr("jsgcghxkzbh")); // 建设工程规划许可证编号
            dataJson.put("zcd", auditSpSpSgxk.getStr("zcd")); // 总长度（米）
            dataJson.put("dscd", auditSpSpSgxk.getStr("dscd")); // 地上长度（米）
            dataJson.put("dxcd", auditSpSpSgxk.getStr("dxcd")); // 地下长度（米）
            dataJson.put("kuadu", auditSpSpSgxk.getStr("kuadu")); // 跨度（米）
            dataJson.put("jsgm", auditSpSpSgxk.getStr("jsgm")); // 建设规模
            dataJson.put("htjg", auditSpSpSgxk.getStr("htjg")); // 合同价格（万元）
            dataJson.put("htkgrq", StringUtil.isBlank(auditSpSpSgxk.getDate("htkgrq")) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htkgrq"), "yyyy-MM-dd")); // 合同开工日期
            dataJson.put("htjgrq", StringUtil.isBlank(auditSpSpSgxk.getDate("htjgrq")) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("htjgrq"), "yyyy-MM-dd")); // 合同竣工日期
            dataJson.put("gctzxz", getSelectItemList("工程投资性质", auditSpSpSgxk.getStr("gctzxz"))); // 工程投资性质
            dataJson.put("xmszqx", getSelectItemList("所在区县", auditSpSpSgxk.getStr("xmszqx"))); // 所在区县
            dataJson.put("gcyt", getSelectItemList("工程用途", auditSpSpSgxk.getGcyt())); // 工程用途
            dataJson.put("Buildproperties", getSelectItemList("建设性质", auditSpSpSgxk.getBuildproperties())); // 建设性质
            dataJson.put("stdw", auditSpSpSgxk.getStr("stdw")); // 审图单位
            dataJson.put("stxmfzr", auditSpSpSgxk.getStr("stxmfzr")); // 审图单位项目负责人
            dataJson.put("stdwtyshxydm", auditSpSpSgxk.getStr("stdwtyshxydm")); // 审图单位统一社会信用代码

            dataJson.put("rfywzmj", auditSpSpSgxk.get("rfywzmj")); // 应履行的人防义务总面积
            dataJson.put("sqjsfk", auditSpSpSgxk.get("sqjsfk")); // 申请建设防空地下室面积
            dataJson.put("dxsly", auditSpSpSgxk.get("dxsly")); // 申请易地建设防空地下室理由
            dataJson.put("ydjsdxsmj", auditSpSpSgxk.get("ydjsdxsmj")); // 申请易地建设防空地下室面积
            dataJson.put("ydjszfgzje", auditSpSpSgxk.get("ydjszfgzje")); // 根据易地建设费征收规则计算的缴费金额（万元）
            dataJson.put("powertime",
                    StringUtil.isBlank(auditSpSpSgxk.getDate("powertime")) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("powertime"), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("powercap", auditSpSpSgxk.getStr("powercap")); // 区（园区）
            dataJson.put("isusepower", auditSpSpSgxk.getStr("isusepower"));
            dataJson.put("fkdxsjsjfrq",
                    StringUtil.isBlank(auditSpSpSgxk.getDate("fkdxsjsjfrq")) ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getDate("fkdxsjsjfrq"), "yyyy-MM-dd"));
            dataJson.put("ydjsfjnse", auditSpSpSgxk.getStr("ydjsfjnse"));
            dataJson.put("fhlb", auditSpSpSgxk.getStr("fhlb"));
            dataJson.put("rfsgtscdw", auditSpSpSgxk.getStr("rfsgtscdw"));
            dataJson.put("jjspyjbh", auditSpSpSgxk.getStr("jjspyjbh"));
            dataJson.put("rfgcjzmj", auditSpSpSgxk.getStr("rfgcjzmj"));

            if ("1".equals(hasjianyi)) {
                dataJson.put("jiansheleixing", auditSpSpSgxk.getStr("jiansheleixing"));
                dataJson.put("xiangmudanwei", auditSpSpSgxk.getStr("xiangmudanwei"));
                dataJson.put("tongyishehuixinyong", auditSpSpSgxk.getStr("tongyishehuixinyong"));
                dataJson.put("farenleixin", getSelectItemList("简易法人类型", auditSpSpSgxk.getStr("farenleixin")));
                dataJson.put("fadingdaibiaoren", auditSpSpSgxk.getStr("fadingdaibiaoren"));
                dataJson.put("tongxundizhi", auditSpSpSgxk.getStr("tongxundizhi"));
                dataJson.put("youzhengbianma", auditSpSpSgxk.getStr("youzhengbianma"));
                dataJson.put("xiangmufuzeren", auditSpSpSgxk.getStr("xiangmufuzeren"));
                dataJson.put("xmlianxidianhua", auditSpSpSgxk.getStr("xmlianxidianhua"));
                dataJson.put("shouquanshenbaoren", auditSpSpSgxk.getStr("shouquanshenbaoren"));
                dataJson.put("sqlianxidianhua", auditSpSpSgxk.getStr("sqlianxidianhua"));
                dataJson.put("shenfenzhengtype", getSelectItemList("简易身份证类型", auditSpSpSgxk.getStr("shenfenzhengtype")));
                dataJson.put("shenfenzhenghaoma", auditSpSpSgxk.getStr("shenfenzhenghaoma"));
            }

        }
        else {
            if ("1".equals(hasjianyi)) {
                dataJson.put("farenleixin", getSelectItemList("简易法人类型"));
                dataJson.put("shenfenzhengtype", getSelectItemList("简易身份证类型"));
            }
            dataJson.put("gctzxz", getSelectItemList("工程投资性质")); // 工程投资性质
            dataJson.put("gcyt", getSelectItemList("工程用途")); // 工程用途
            dataJson.put("xmszqx", getSelectItemList("所在区县")); // 所在区县
            dataJson.put("projectLevel", getSelectItemList("项目等级")); // 重点项目
            dataJson.put("Buildproperties", getSelectItemList("建设性质")); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型"));// 立项类型
            dataJson.put("investTyped", getSelectItemList("投资类型"));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源")); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地来源")); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质")); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型")); // 项目类型
            dataJson.put("xmfl", getSelectItemList("项目分类")); // 项目类型
        }
        return dataJson;
    }

    /**
     * 加载第四阶段信息
     */
    private JSONObject getJgysInfo(String subappguid, String phaseid) {
        JSONObject dataJson = new JSONObject();
        if ("4".equals(phaseid)) {
            dataJson.put("phasenum", "jgys");
        }
        else if ("6".equals(phaseid)) {
            dataJson.put("phasenum", "zszx");
        }

        AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid);
        if (auditSpSpJgys != null) {
            dataJson.put("itemname", auditSpSpJgys.getItemname());// 项目名称
            dataJson.put("subappname", auditSpSpJgys.getSubappname()); // 子申报名称
            dataJson.put("itemcode", auditSpSpJgys.getItemcode()); // 投资平台项目代码
            dataJson.put("projectLevel", getSelectItemList("项目等级", auditSpSpJgys.getProjectlevel())); // 重点项目
            dataJson.put("itemAddress", auditSpSpJgys.getItemaddress()); // 项目地址
            dataJson.put("area", auditSpSpJgys.getArea()); // 区（园区）
            dataJson.put("road", auditSpSpJgys.getRoad()); // 街道
            dataJson.put("eastToArea", auditSpSpJgys.getEasttoarea()); // 四至范围（东至）
            dataJson.put("westToArea", auditSpSpJgys.getWesttoarea()); // 四至范围（西至）
            dataJson.put("southToArea", auditSpSpJgys.getSouthtoarea()); // 四至范围（南至）
            dataJson.put("northToArea", auditSpSpJgys.getNorthtoarea()); // 四至范围（北至）
            dataJson.put("proTyped", getSelectItemList("立项类型", auditSpSpJgys.getProtyped()));// 立项类型
            dataJson.put("proOu", auditSpSpJgys.getProou()); // 立项部门
            dataJson.put("investTyped", getSelectItemList("投资类型", auditSpSpJgys.getInvesttyped()));// 投资类型
            dataJson.put("hangyeType", auditSpSpJgys.getHangyetype()); // 行业类别
            dataJson.put("moneySources", getSelectItemList("资金来源", auditSpSpJgys.getMoneysources())); // 资金来源
            dataJson.put("allMoney", auditSpSpJgys.getAllmoney()); // 总投资
            dataJson.put("areaSources", getSelectItemList("土地来源", auditSpSpJgys.getAreasources())); // 土地来源
            dataJson.put("useLandType", auditSpSpJgys.getUselandtype()); // 规划用地性质
            dataJson.put("areaUsed", auditSpSpJgys.getAreaused());// 用地面积(公顷)
            dataJson.put("newLandType", auditSpSpJgys.getNewlandtype()); // 新增用地面积（公顷）
            dataJson.put("buildProperties", getSelectItemList("建设性质", auditSpSpJgys.getBuildproperties())); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型", auditSpSpJgys.getBuildtype())); // 项目类型
            dataJson.put("allBuildArea", auditSpSpJgys.getAllbuildarea()); // 总建筑面积（m²）
            dataJson.put("overLoadArea", auditSpSpJgys.getOverloadarea()); // 地上
            dataJson.put("downLoadArea", auditSpSpJgys.getDownloadarea()); // 地下
            dataJson.put("buildContent", auditSpSpJgys.getBuildcontent()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate",
                    StringUtil.isBlank(auditSpSpJgys.getPlanstartdate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate", StringUtil.isBlank(auditSpSpJgys.getPlanenddate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanenddate(), "yyyy-MM-dd")); // 计划竣工日期
            dataJson.put("gcmc", auditSpSpJgys.getGcmc()); // 工程名称
            dataJson.put("lxjb", auditSpSpJgys.getLxjb()); // 立项级别
            dataJson.put("jzgd", auditSpSpJgys.getJzgd()); // 建筑高度
            dataJson.put("jclb", auditSpSpJgys.getJclb()); // 基础类别
            dataJson.put("startDate", StringUtil.isBlank(auditSpSpJgys.getStartdate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(), "yyyy-MM-dd")); // 开工日期
            dataJson.put("endDate", StringUtil.isBlank(auditSpSpJgys.getEnddate()) ? "" : EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(), "yyyy-MM-dd")); // 竣工日期
            dataJson.put("bdh", auditSpSpJgys.getBdh()); // 标段号
            dataJson.put("jzzh", auditSpSpJgys.getJzzh()); // 桩号
            dataJson.put("gcgm", auditSpSpJgys.getGcgm()); // 工程规模
            dataJson.put("gczj", auditSpSpJgys.getGczj()); // 工程造价(万元)
            dataJson.put("rfjzmj", auditSpSpJgys.getRfjzmj()); // 人防建筑面积
            dataJson.put("zxmj", auditSpSpJgys.getZxmj()); // 装修面积
            dataJson.put("dscc", auditSpSpJgys.getDscc()); // 地上层次
            dataJson.put("dxcc", auditSpSpJgys.getDxcc()); // 地下层次
            dataJson.put("nhdj", getSelectItemList("耐火等级", auditSpSpJgys.getNhdj())); // 耐火等级
            dataJson.put("fldj", getSelectItemList("防雷等级", auditSpSpJgys.getFldj())); // 防雷等级
            dataJson.put("gclb", auditSpSpJgys.getGclb()); // 工程类别
            dataJson.put("jglx", auditSpSpJgys.getJglx()); // 结构类型
            dataJson.put("sylx", getSelectItemList("使用类型", auditSpSpJgys.getSylx())); // 使用类型

            if (StringUtil.isBlank(auditSpSpJgys.getStr("applycliengguid"))) {
                dataJson.put("applycliengguid", UUID.randomUUID().toString());
            }
            else {
                dataJson.put("applycliengguid", auditSpSpJgys.getStr("applycliengguid"));
            }

            if (StringUtil.isBlank(auditSpSpJgys.getStr("yscliengguid"))) {
                dataJson.put("yscliengguid", UUID.randomUUID().toString());
            }
            else {
                dataJson.put("yscliengguid", auditSpSpJgys.getStr("yscliengguid"));
            }

            if (StringUtil.isBlank(auditSpSpJgys.getStr("lhchcliengguid"))) {
                dataJson.put("lhchcliengguid", UUID.randomUUID().toString());
            }
            else {
                dataJson.put("lhchcliengguid", auditSpSpJgys.getStr("lhchcliengguid"));
            }

        }
        else {
            dataJson.put("projectLevel", getSelectItemList("项目等级")); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型"));// 立项类型
            dataJson.put("investTyped", getSelectItemList("投资类型"));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源")); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地来源")); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质")); // 建设性质
            dataJson.put("buildType", getSelectItemList("项目类型")); // 项目类型
            dataJson.put("nhdj", getSelectItemList("耐火等级")); // 耐火等级
            dataJson.put("fldj", getSelectItemList("防雷等级")); // 防雷等级
            dataJson.put("sylx", getSelectItemList("使用类型")); // 使用类型

            dataJson.put("applycliengguid", UUID.randomUUID().toString());
            dataJson.put("yscliengguid", UUID.randomUUID().toString());
            dataJson.put("lhchcliengguid", UUID.randomUUID().toString());

        }
        return dataJson;
    }

    // 保存第一阶段表单
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
        auditSpSpLxydghxk.setItemaddress(itemAddress);
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

    // 保存第二阶段表单
    private void saveGcjsxkInfo(JSONObject param) {
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
        Double rfywzmj = param.getDouble("rfywzmj");// 应履行的人防义务总面积
        Double sqjsfk = param.getDouble("sqjsfk");// 申请建设防空地下室面积
        String dxsly = param.getString("dxsly");// 申请易地建设防空地下室理由
        Double ydjsdxsmj = param.getDouble("ydjsdxsmj");// 申请易地建设防空地下室面积
        Double ydjszfgzje = param.getDouble("ydjszfgzje");// 根据易地建设费征收规则计算的缴费金额（万元）

        String isusepower = param.getString("isusepower");// 申请易地建设防空地下室理由
        String powercap = param.getString("powercap");// 申请易地建设防空地下室理由
        Date powertime = param.getDate("powertime");// 申请易地建设防空地下室理由

        Date fkdxsjsjfrq = param.getDate("fkdxsjsjfrq");
        String ydjsfjnse = param.getString("ydjsfjnse");
        String fhlb = param.getString("fhlb");
        String rfsgtscdw = param.getString("rfsgtscdw");
        String jjspyjbh = param.getString("jjspyjbh");
        String rfgcjzmj = param.getString("rfgcjzmj");

        // 更新表单信息
        AuditSpSpGcjsxk auditSpSpGcjsxk = iAuditSpSpGcjsxkService.findAuditSpSpGcjsxkBySubappGuid(subappguid);
        boolean addFlag = false; // 默认为更新
        if (auditSpSpGcjsxk == null) {
            auditSpSpGcjsxk = new AuditSpSpGcjsxk();
            auditSpSpGcjsxk.setRowguid(UUID.randomUUID().toString());
            auditSpSpGcjsxk.setSubappguid(subappguid);
            addFlag = true; // 新增
        }
        auditSpSpGcjsxk.set("isusepower", isusepower);
        auditSpSpGcjsxk.set("powercap", powercap);
        auditSpSpGcjsxk.set("powertime", powertime);

        auditSpSpGcjsxk.set("fkdxsjsjfrq", fkdxsjsjfrq);
        auditSpSpGcjsxk.set("ydjsfjnse", ydjsfjnse);
        auditSpSpGcjsxk.set("fhlb", fhlb);
        auditSpSpGcjsxk.set("rfsgtscdw", rfsgtscdw);
        auditSpSpGcjsxk.set("jjspyjbh", jjspyjbh);
        auditSpSpGcjsxk.set("rfgcjzmj", rfgcjzmj);

        auditSpSpGcjsxk.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditSpSpGcjsxk.setOperatedate(new Date());
        auditSpSpGcjsxk.setItemname(itemname);
        auditSpSpGcjsxk.setSubappname(subappname);
        auditSpSpGcjsxk.setItemcode(itemcode);
        auditSpSpGcjsxk.setProjectlevel(projectLevel);
        auditSpSpGcjsxk.setItemaddress(itemAddress);
        auditSpSpGcjsxk.set("userterm", userterm);
        auditSpSpGcjsxk.set("bdcdanyuanhao", bdcdanyuanhao);
        auditSpSpGcjsxk.setArea(area);
        auditSpSpGcjsxk.setRoad(road);
        auditSpSpGcjsxk.setEasttoarea(eastToArea);
        auditSpSpGcjsxk.setWesttoarea(westToArea);
        auditSpSpGcjsxk.setSouthtoarea(southToArea);
        auditSpSpGcjsxk.setNorthtoarea(northToArea);
        auditSpSpGcjsxk.setProtyped(proTyped);
        auditSpSpGcjsxk.setProou(proOu);
        auditSpSpGcjsxk.setInvesttyped(investTyped);
        auditSpSpGcjsxk.setHangyetype(hangyeType);
        auditSpSpGcjsxk.setMoneysources(moneySources);
        auditSpSpGcjsxk.setAllmoney(allMoney);
        auditSpSpGcjsxk.setAreasources(areaSources);
        auditSpSpGcjsxk.setUselandtype(useLandType);
        auditSpSpGcjsxk.setAreaused(areaUsed);
        auditSpSpGcjsxk.setNewlandtype(newLandType);
        auditSpSpGcjsxk.setBuildtype(buildType);
        auditSpSpGcjsxk.setAllbuildarea(allBuildArea);
        auditSpSpGcjsxk.setOverloadarea(overLoadArea);
        auditSpSpGcjsxk.setDownloadarea(downLoadArea);
        auditSpSpGcjsxk.setBuildcontent(buildContent);
        auditSpSpGcjsxk.setPlanstartdate(planStartDate);
        auditSpSpGcjsxk.setPlanenddate(planEndDate);
        auditSpSpGcjsxk.setItemsource(itemSource);
        auditSpSpGcjsxk.setXznxfl(xznxfl);
        auditSpSpGcjsxk.setXznydl(xznydl);
        auditSpSpGcjsxk.setRfywzmj(rfywzmj);
        auditSpSpGcjsxk.setSqjsfk(sqjsfk);
        auditSpSpGcjsxk.setDxsly(dxsly);
        auditSpSpGcjsxk.setYdjsdxsmj(ydjsdxsmj);
        auditSpSpGcjsxk.setYdjszfgzje(ydjszfgzje);

        // addFlag 为true, 说明数据库中不存在该记录
        if (addFlag) {
            iAuditSpSpGcjsxkService.insert(auditSpSpGcjsxk);
        }
        else {
            iAuditSpSpGcjsxkService.update(auditSpSpGcjsxk);
        }
        // 保存子申报
    }

    // 保存第三阶段表单
    private void saveSgxkInfo(JSONObject param) {
        Double rfywzmj = param.getDouble("rfywzmj");// 应履行的人防义务总面积
        Double sqjsfk = param.getDouble("sqjsfk");// 申请建设防空地下室面积
        String dxsly = param.getString("dxsly");// 申请易地建设防空地下室理由
        Double ydjsdxsmj = param.getDouble("ydjsdxsmj");// 申请易地建设防空地下室面积
        Double ydjszfgzje = param.getDouble("ydjszfgzje");// 根据易地建设费征收规则计算的缴费金额（万元）

        String isusepower = param.getString("isusepower");// 申请易地建设防空地下室理由
        String powercap = param.getString("powercap");// 申请易地建设防空地下室理由
        Date powertime = param.getDate("powertime");// 申请易地建设防空地下室理由

        Date fkdxsjsjfrq = param.getDate("fkdxsjsjfrq");
        String ydjsfjnse = param.getString("ydjsfjnse");
        String fhlb = param.getString("fhlb");
        String rfsgtscdw = param.getString("rfsgtscdw");
        String jjspyjbh = param.getString("jjspyjbh");
        String rfgcjzmj = param.getString("rfgcjzmj");

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

        String xmbh = param.getString("xmbh");// 项目编号
        String gctzxz = param.getString("gctzxz");// 工程投资性质
        String htgq = param.getString("htgq");// 合同工期
        String xmszsf = param.getString("xmszsf");// 项目所在省份
        String xmszcs = param.getString("xmszcs");// 项目所在城市
        String xmszqx = param.getString("xmszqx");// 项目所在区县
        String lxpfjg = param.getString("lxpfjg");// 立项批复机关
        Date lxpfsj = param.getDate("lxpfsj");// 立项批复时间
        String zbtzsbh = param.getString("zbtzsbh");// 中标通知书编号
        String sgtsxmbh = param.getString("sgtsxmbh");// 施工图审查项目编号
        String jsydghxkzbh = param.getString("jsydghxkzbh");// 建设用地规划许可证编号
        String jsgcghxkzbh = param.getString("jsgcghxkzbh");// 建设工程规划许可证编号
        String zcd = param.getString("zcd");// 总长度（米）
        String dscd = param.getString("dscd");// 地上长度（米）
        String dxcd = param.getString("dxcd");// 地下长度（米）
        String kuadu = param.getString("kuadu");// 跨度（米）
        String jsgm = param.getString("jsgm");// 建设规模
        String htjg = param.getString("htjg");// 合同价格（万元）
        String htkgrq = param.getString("htkgrq");// 合同开工日期
        String htjgrq = param.getString("htjgrq");// 合同竣工日期
        String Buildproperties = param.getString("Buildproperties");// 建设性质
        String stdw = param.getString("stdw");// 审图单位
        String stxmfzr = param.getString("stxmfzr");// 审图单位项目负责人
        String stdwtyshxydm = param.getString("stdwtyshxydm");// 审图单位统一社会信用代码
        String xmfl = param.getString("xmfl");// 项目分类

        String lxwh = param.getString("lxwh");// 立项文号
        Date lxrq = param.getDate("lxrq");// 立项日期
        String lxjb = param.getString("lxjb");// 立项级别
        String xmbm = param.getString("xmbm");// 项目编码
        String gcyt = param.getString("gcyt");// 工程用途
        String gcfl = param.getString("gcfl");// 工程分类
        String zftzgs = param.getString("zftzgs");// 政府投资概算
        String ghyzzpl = param.getString("ghyzzpl");// 规划预制装配率

        String yxsfwjgyh = param.getString("yxsfwjgyh");// 规划预制装配率
        String yxsfwjgzh = param.getString("yxsfwjgzh");// 规划预制装配率

        String jiansheleixing = param.getString("jiansheleixing");
        String xiangmudanwei = param.getString("xiangmudanwei");
        String tongyishehuixinyong = param.getString("tongyishehuixinyong");
        String farenleixin = param.getString("farenleixin");
        String fadingdaibiaoren = param.getString("fadingdaibiaoren");
        String tongxundizhi = param.getString("tongxundizhi");
        String youzhengbianma = param.getString("youzhengbianma");
        String xiangmufuzeren = param.getString("xiangmufuzeren");
        String xmlianxidianhua = param.getString("xmlianxidianhua");
        String shouquanshenbaoren = param.getString("shouquanshenbaoren");
        String sqlianxidianhua = param.getString("sqlianxidianhua");
        String shenfenzhengtype = param.getString("shenfenzhengtype");
        String shenfenzhenghaoma = param.getString("shenfenzhenghaoma");

        // 更新表单信息
        AuditSpSpSgxk auditSpSpSgxk = iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid);
        boolean addFlag = false; // 默认为更新
        if (auditSpSpSgxk == null) {
            auditSpSpSgxk = new AuditSpSpSgxk();
            auditSpSpSgxk.setRowguid(UUID.randomUUID().toString());
            auditSpSpSgxk.setSubappguid(subappguid);
            addFlag = true; // 新增
        }
        auditSpSpSgxk.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditSpSpSgxk.setOperatedate(new Date());
        auditSpSpSgxk.setItemname(itemname);
        auditSpSpSgxk.setSubappname(subappname);
        auditSpSpSgxk.setItemcode(itemcode);
        auditSpSpSgxk.setProjectlevel(projectLevel);
        auditSpSpSgxk.setItemaddress(itemAddress);
        auditSpSpSgxk.set("userterm", userterm);
        auditSpSpSgxk.set("bdcdanyuanhao", bdcdanyuanhao);
        auditSpSpSgxk.setArea(area);
        auditSpSpSgxk.setRoad(road);
        auditSpSpSgxk.setEasttoarea(eastToArea);
        auditSpSpSgxk.setWesttoarea(westToArea);
        auditSpSpSgxk.setSouthtoarea(southToArea);
        auditSpSpSgxk.setNorthtoarea(northToArea);
        auditSpSpSgxk.setProtyped(proTyped);
        auditSpSpSgxk.setProou(proOu);
        auditSpSpSgxk.setInvesttyped(investTyped);
        auditSpSpSgxk.setHangyetype(hangyeType);
        auditSpSpSgxk.setMoneysources(moneySources);
        auditSpSpSgxk.setAllmoney(allMoney);
        auditSpSpSgxk.setAreasources(areaSources);
        auditSpSpSgxk.setUselandtype(useLandType);
        auditSpSpSgxk.setAreaused(areaUsed);
        auditSpSpSgxk.setNewlandtype(newLandType);
        auditSpSpSgxk.setBuildtype(buildType);
        auditSpSpSgxk.setAllbuildarea(allBuildArea);
        auditSpSpSgxk.setOverloadarea(overLoadArea);
        auditSpSpSgxk.setDownloadarea(downLoadArea);
        auditSpSpSgxk.setBuildcontent(buildContent);
        auditSpSpSgxk.setPlanstartdate(planStartDate);
        auditSpSpSgxk.setPlanenddate(planEndDate);
        auditSpSpSgxk.setLxwh(lxwh);
        auditSpSpSgxk.setLxrq(lxrq);
        auditSpSpSgxk.setLxjb(lxjb);
        auditSpSpSgxk.setXmbm(xmbm);
        auditSpSpSgxk.setGcyt(gcyt);
        auditSpSpSgxk.setGcfl(gcfl);
        auditSpSpSgxk.setZftzgs(zftzgs);
        auditSpSpSgxk.setGhyzzpl(ghyzzpl);

        auditSpSpSgxk.set("yxsfwjgyh", yxsfwjgyh);
        auditSpSpSgxk.set("yxsfwjgzh", yxsfwjgzh);

        auditSpSpSgxk.set("xmbh", xmbh);
        auditSpSpSgxk.set("gctzxz", gctzxz);
        auditSpSpSgxk.set("htgq", htgq);
        auditSpSpSgxk.set("xmszsf", xmszsf);
        auditSpSpSgxk.set("xmszcs", xmszcs);
        auditSpSpSgxk.set("xmszqx", xmszqx);
        auditSpSpSgxk.set("lxpfjg", lxpfjg);
        auditSpSpSgxk.set("lxpfsj", lxpfsj);
        auditSpSpSgxk.set("zbtzsbh", zbtzsbh);
        auditSpSpSgxk.set("sgtsxmbh", sgtsxmbh);
        auditSpSpSgxk.set("jsydghxkzbh", jsydghxkzbh);
        auditSpSpSgxk.set("jsgcghxkzbh", jsgcghxkzbh);
        auditSpSpSgxk.set("zcd", zcd);
        auditSpSpSgxk.set("dscd", dscd);
        auditSpSpSgxk.set("dxcd", dxcd);
        auditSpSpSgxk.set("kuadu", kuadu);
        auditSpSpSgxk.set("jsgm", jsgm);
        auditSpSpSgxk.set("htjg", htjg);
        auditSpSpSgxk.set("htkgrq", htkgrq);
        auditSpSpSgxk.set("htjgrq", htjgrq);
        auditSpSpSgxk.set("Buildproperties", Buildproperties);
        auditSpSpSgxk.set("stdw", stdw);
        auditSpSpSgxk.set("stxmfzr", stxmfzr);
        auditSpSpSgxk.set("stdwtyshxydm", stdwtyshxydm);
        auditSpSpSgxk.set("xmfl", xmfl);

        auditSpSpSgxk.set("jiansheleixing", jiansheleixing);
        auditSpSpSgxk.set("xiangmudanwei", xiangmudanwei);
        auditSpSpSgxk.set("tongyishehuixinyong", tongyishehuixinyong);
        auditSpSpSgxk.set("farenleixin", farenleixin);
        auditSpSpSgxk.set("fadingdaibiaoren", fadingdaibiaoren);
        auditSpSpSgxk.set("tongxundizhi", tongxundizhi);
        auditSpSpSgxk.set("youzhengbianma", youzhengbianma);
        auditSpSpSgxk.set("xiangmufuzeren", xiangmufuzeren);
        auditSpSpSgxk.set("xmlianxidianhua", xmlianxidianhua);
        auditSpSpSgxk.set("shouquanshenbaoren", shouquanshenbaoren);
        auditSpSpSgxk.set("sqlianxidianhua", sqlianxidianhua);
        auditSpSpSgxk.set("shenfenzhengtype", shenfenzhengtype);
        auditSpSpSgxk.set("shenfenzhenghaoma", shenfenzhenghaoma);

        auditSpSpSgxk.set("rfywzmj", rfywzmj);
        auditSpSpSgxk.set("sqjsfk", sqjsfk);
        auditSpSpSgxk.set("dxsly", dxsly);
        auditSpSpSgxk.set("ydjsdxsmj", ydjsdxsmj);
        auditSpSpSgxk.set("ydjszfgzje", ydjszfgzje);
        auditSpSpSgxk.set("isusepower", isusepower);
        auditSpSpSgxk.set("powercap", powercap);
        auditSpSpSgxk.set("powertime", powertime);
        auditSpSpSgxk.set("fkdxsjsjfrq", fkdxsjsjfrq);
        auditSpSpSgxk.set("ydjsfjnse", ydjsfjnse);
        auditSpSpSgxk.set("fhlb", fhlb);
        auditSpSpSgxk.set("rfsgtscdw", rfsgtscdw);
        auditSpSpSgxk.set("jjspyjbh", jjspyjbh);
        auditSpSpSgxk.set("rfgcjzmj", rfgcjzmj);

        // addFlag 为true, 说明数据库中不存在该记录
        if (addFlag) {
            iAuditSpSpSgxkService.insert(auditSpSpSgxk);
        }
        else {
            iAuditSpSpSgxkService.update(auditSpSpSgxk);
        }
    }

    // 保存第四阶段表单
    private void saveJgysInfo(JSONObject param) {
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

        String applycliengguid = param.getString("applycliengguid");
        String yscliengguid = param.getString("yscliengguid");
        String lhchcliengguid = param.getString("lhchcliengguid");

        String gcmc = param.getString("gcmc");// 工程名称
        String lxjb = param.getString("lxjb1");// 立项级别
        Double jzgd = param.getDouble("jzgd");// 建筑高度
        String jclb = param.getString("jclb");// 基础类别
        Date startDate = param.getDate("startDate");// 开工日期
        Date endDate = param.getDate("endDate");// 竣工日期
        String bdh = param.getString("bdh");// 标段号
        String jzzh = param.getString("jzzh");// 桩号
        String gcgm = param.getString("gcgm");// 工程规模
        Double gczj = param.getDouble("gczj");// 工程造价(万元)
        Double rfjzmj = param.getDouble("rfjzmj");// 人防建筑面积
        Double zxmj = param.getDouble("zxmj");// 装修面积
        String dscc = param.getString("dscc");// 地上层次
        String dxcc = param.getString("dxcc");// 地下层次
        String nhdj = param.getString("nhdj");// 耐火等级
        String fldj = param.getString("fldj");// 防雷等级
        String gclb = param.getString("gclb");// 工程类别
        String jglx = param.getString("jglx");// 结构类型
        String sylx = param.getString("sylx");// 使用类型

        // 更新表单信息
        AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid);
        boolean addFlag = false; // 默认为更新
        if (auditSpSpJgys == null) {
            auditSpSpJgys = new AuditSpSpJgys();
            auditSpSpJgys.setRowguid(UUID.randomUUID().toString());
            auditSpSpJgys.setSubappguid(subappguid);
            addFlag = true; // 新增
        }
        auditSpSpJgys.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditSpSpJgys.setOperatedate(new Date());
        auditSpSpJgys.setItemname(itemname);
        auditSpSpJgys.setSubappname(subappname);
        auditSpSpJgys.setItemcode(itemcode);
        auditSpSpJgys.setProjectlevel(projectLevel);
        auditSpSpJgys.setItemaddress(itemAddress);
        auditSpSpJgys.set("userterm", userterm);
        auditSpSpJgys.set("bdcdanyuanhao", bdcdanyuanhao);
        auditSpSpJgys.setArea(area);
        auditSpSpJgys.setRoad(road);
        auditSpSpJgys.setEasttoarea(eastToArea);
        auditSpSpJgys.setWesttoarea(westToArea);
        auditSpSpJgys.setSouthtoarea(southToArea);
        auditSpSpJgys.setNorthtoarea(northToArea);
        auditSpSpJgys.setProtyped(proTyped);
        auditSpSpJgys.setProou(proOu);
        auditSpSpJgys.setInvesttyped(investTyped);
        auditSpSpJgys.setHangyetype(hangyeType);
        auditSpSpJgys.setMoneysources(moneySources);
        auditSpSpJgys.setAllmoney(allMoney);
        auditSpSpJgys.setAreasources(areaSources);
        auditSpSpJgys.setUselandtype(useLandType);
        auditSpSpJgys.setAreaused(areaUsed);
        auditSpSpJgys.setNewlandtype(newLandType);
        auditSpSpJgys.setBuildtype(buildType);
        auditSpSpJgys.setAllbuildarea(allBuildArea);
        auditSpSpJgys.setOverloadarea(overLoadArea);
        auditSpSpJgys.setDownloadarea(downLoadArea);
        auditSpSpJgys.setBuildcontent(buildContent);
        auditSpSpJgys.setPlanstartdate(planStartDate);
        auditSpSpJgys.setPlanenddate(planEndDate);
        auditSpSpJgys.setGcmc(gcmc);
        auditSpSpJgys.setLxjb(lxjb);
        auditSpSpJgys.setJzgd(jzgd);
        auditSpSpJgys.setJclb(jclb);
        auditSpSpJgys.setStartdate(startDate);
        auditSpSpJgys.setEnddate(endDate);
        auditSpSpJgys.setBdh(bdh);
        auditSpSpJgys.setJzzh(jzzh);
        auditSpSpJgys.setGcgm(gcgm);
        auditSpSpJgys.setGczj(gczj);
        auditSpSpJgys.setRfjzmj(rfjzmj);
        auditSpSpJgys.setZxmj(zxmj);
        auditSpSpJgys.setDscc(dscc);
        auditSpSpJgys.setDxcc(dxcc);
        auditSpSpJgys.setNhdj(nhdj);
        auditSpSpJgys.setFldj(fldj);
        auditSpSpJgys.setGclb(gclb);
        auditSpSpJgys.setJglx(jglx);
        auditSpSpJgys.setSylx(sylx);

        auditSpSpJgys.set("applycliengguid", applycliengguid);
        auditSpSpJgys.set("yscliengguid", yscliengguid);
        auditSpSpJgys.set("lhchcliengguid", lhchcliengguid);

        // addFlag 为true, 说明数据库中不存在该记录
        if (addFlag) {
            iAuditSpSpJgysService.insert(auditSpSpJgys);
        }
        else {
            iAuditSpSpJgysService.update(auditSpSpJgys);
        }
    }

    /**
     * 子申报方法
     */
    public void setAuditSpISubapp(String biguid, String businessguid, String subappguid, String phaseguid, String itemguid, String subappname, String type,
                                  String yjsbusinessguid) {
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("biguid", biguid);
        conditionUtil.isBlank("parentid");
        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
        if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
            String field = "creditcode";
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                field = "organcode";
            }
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField(field, auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (auditSpISubapp == null) {
                auditSpISubapp = new AuditSpISubapp();
                auditSpISubapp.setCreatedate(new Date());
                auditSpISubapp.setRowguid(subappguid);

                // 如果存在一件事主题标识，表示该工改申报是一件事主题发起的，记录一件事主题标识
                if (StringUtil.isNotBlank(yjsbusinessguid)) {
                    auditSpISubapp.set("yjsbusinessguid", yjsbusinessguid);
                    AuditSpBusiness spBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(yjsbusinessguid).getResult();
                    if (spBusiness != null) {
                        auditSpISubapp.set("yjsname", spBusiness.getBusinessname());
                    }

                }
            }
            auditSpISubapp.setOperatedate(new Date());
            auditSpISubapp.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
            auditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
            auditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
            auditSpISubapp.setApplyerway("10");
            auditSpISubapp.setBiguid(biguid);
            auditSpISubapp.setBusinessguid(businessguid);
            auditSpISubapp.setPhaseguid(phaseguid);
            if ("2".equals(type)) {// 提交
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_WWDYS);
            }
            else if ("1".equals(type)) { // 保存
                if (!ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                    auditSpISubapp.setStatus("3");
                }
            }
            if (StringUtils.isNotBlank(itemguid)) {
                AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                if (auditRsItemBaseinfo1 != null) {
                    auditSpISubapp.setYewuguid(itemguid);
                }
                else {
                    auditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                }
            }

            auditSpISubapp.setSubappname(subappname);
            AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (sub != null) {
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
            else {
                iAuditSpISubapp.addSubapp(auditSpISubapp);
            }
            if ("2".equals(type)) {// 提交
                // 发送待办
                sendMessageCenter(phaseguid, biguid, subappguid, subappname);
            }
        }
    }

    /**
     * 子申报方法
     */
    public void newSetAuditSpISubapp(String biguid, String businessguid, String subappguid, String phaseguid, String itemguid, String subappname, String type,
                                     String yjsbusinessguid) {
        SqlConditionUtil conditionUtil = new SqlConditionUtil();
        conditionUtil.eq("biguid", biguid);
        conditionUtil.isBlank("parentid");
        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo.selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
        if (auditRsItemBaseinfos != null && !auditRsItemBaseinfos.isEmpty()) {
            AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
            String field = "creditcode";
            if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                field = "organcode";
            }
            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo.getCompanyByOneField(field, auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (auditSpISubapp == null) {
                auditSpISubapp = new AuditSpISubapp();
                auditSpISubapp.setCreatedate(new Date());
                auditSpISubapp.setRowguid(subappguid);

                // 如果存在一件事主题标识，表示该工改申报是一件事主题发起的，记录一件事主题标识
                if (StringUtil.isNotBlank(yjsbusinessguid)) {
                    auditSpISubapp.set("yjsbusinessguid", yjsbusinessguid);
                    AuditSpBusiness spBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(yjsbusinessguid).getResult();
                    if (spBusiness != null) {
                        auditSpISubapp.set("yjsname", spBusiness.getBusinessname());
                    }
                }
            }
            auditSpISubapp.setOperatedate(new Date());
            auditSpISubapp.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
            auditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
            auditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
            auditSpISubapp.setApplyerway("10");
            auditSpISubapp.setBiguid(biguid);
            auditSpISubapp.setBusinessguid(businessguid);
            auditSpISubapp.setPhaseguid(phaseguid);
            if ("2".equals(type)) {// 提交
                // 调整申报状态
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YPS);
            }
            else if ("1".equals(type)) { // 保存
                if (!ZwfwConstant.LHSP_Status_YSTH.equals(auditSpISubapp.getStatus())) {
                    auditSpISubapp.setStatus("3");
                }
            }
            if (StringUtils.isNotBlank(itemguid)) {
                AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                if (auditRsItemBaseinfo1 != null) {
                    auditSpISubapp.setYewuguid(itemguid);
                }
                else {
                    auditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                }
            }

            auditSpISubapp.setSubappname(subappname);
            AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
            if (sub != null) {
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
            else {
                iAuditSpISubapp.addSubapp(auditSpISubapp);
            }

            // if ("2".equals(type)) {// 提交
            // // 发送待办
            // sendMessageCenter(phaseguid, biguid, subappguid, subappname);
            // }
        }
    }

    /**
     * 发送代办
     */
    public void sendMessageCenter(String phaseGuid, String biguid, String subappGuid, String subappname) {
        // 通过阶段主键获取实体类
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
        String fromUserGuid = ZwdtUserSession.getInstance("").getAccountGuid(); // 发送待办人guid
        String fromUserName = ZwdtUserSession.getInstance("").getClientName(); // 发送待办人名称
        String handleUrl = "/epointzwfw/auditsp/auditsphandle/handlebipreliminarydetail?biGuid=" + biguid + "&subappGuid=" + subappGuid;
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
        String title = "【项目预审】" + subappname + "(" + auditSpISubapp.getApplyername() + ")";
        iAuditOnlineMessages.sendMsg("统一收件人员", title, fromUserGuid, fromUserName, auditSpInstance.getAreacode(), handleUrl, subappGuid, "zwfwMsgurl", null);
    }

    // 默认为请选择
    public List<JSONObject> getSelectItemList(String codeName) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("optionvalue", "");
        json.put("optiontext", "请选择");
        json.put("isselected", 1);
        resultJsonList.add(json);
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("optionvalue", codeItems.getItemValue());
            objJson.put("optiontext", codeItems.getItemText());
            resultJsonList.add(objJson);
        }
        return resultJsonList;
    }

    // 存在共同字段的, 先判断一下是否有值
    public List<JSONObject> getSelectItemList(String codeName, String filedValue) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("optionvalue", "");
        json.put("optiontext", "请选择");
        json.put("isselected", 1);
        resultJsonList.add(json);
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("optionvalue", codeItems.getItemValue());
            objJson.put("optiontext", codeItems.getItemText());
            // 判断是否有默认选中的
            if (codeItems.getItemValue().equals(filedValue)) {
                objJson.put("isselected", 1);
                resultJsonList.get(0).put("isselected", 0);
            }
            resultJsonList.add(objJson);
        }
        return resultJsonList;
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
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid()).getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity()).getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 获取水电气暖网联合报装一件事事项
     */
    @RequestMapping(value = "/getsdqtask", method = RequestMethod.POST)
    public String getsdqtask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getkgtask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 根据前端传的事项类型和辖区查询对应事项的代码项 代码项的组成是辖区编码和taskid 备注是事项名称
                // 2 根据tasktype判断是哪个事项，然后根据代码项获取areacode对应的taskid
                JSONArray tasklist = obj.getJSONArray("tasklist");
                String areacode = obj.getString("areacode");
                JSONArray jsonArray = new JSONArray();
                Map<String, String> taskTypeToCodeName = new HashMap<>();
                taskTypeToCodeName.put("1", "水电气用水报装事项");
                taskTypeToCodeName.put("2", "水电气排水报装事项");
                taskTypeToCodeName.put("3", "水电气用电报装事项");
                taskTypeToCodeName.put("4", "水电气用气报装事项");
                taskTypeToCodeName.put("5", "水电气供热报装事项");
                taskTypeToCodeName.put("6", "水电气通信报装事项");
                taskTypeToCodeName.put("7", "水电气广电报装事项");
                taskTypeToCodeName.put("8", "水电气外线接入事项");
                for (int i = 0; i < tasklist.size(); i++) {
                    Set<String> taskidset = new HashSet<>();
                    JSONObject task = tasklist.getJSONObject(i);
                    // String areacode = task.getString("areacode");
                    String tasktype = task.getString("tasktype");
                    String codeName = taskTypeToCodeName.get(tasktype);

                    if (codeName != null) {
                        List<CodeItems> items = iCodeItemsService.listCodeItemsByCodeName(codeName);
                        if (ValidateUtil.isNotBlankCollection(items)) {
                            String areacodename = iCodeItemsService.getCodeItemByCodeName("辖区对应关系", areacode).getItemText();
                            for (CodeItems item : items) {
                                if (areacode.equals(item.getItemValue())) {
                                    if (item.getItemText().contains(";")) {
                                        String[] taskids = item.getItemText().split(";");
                                        String[] abrs = item.getDmAbr1().split(";");
                                        for (int j = 0; j < taskids.length; j++) {
                                            if (!taskidset.contains(taskids[j])) {
                                                JSONObject jsonData = new JSONObject();
                                                jsonData.put("taskname", abrs[j] + "（" + areacodename + "）");
                                                jsonData.put("task_id", taskids[j]);
                                                jsonArray.add(jsonData);
                                                taskidset.add(taskids[j]);
                                            }

                                        }
                                    }
                                    else {
                                        JSONObject jsonData = new JSONObject();
                                        jsonData.put("taskname", item.getDmAbr1() + "（" + areacodename + "）");
                                        jsonData.put("task_id", item.getItemText());
                                        jsonArray.add(jsonData);
                                    }

                                }
                            }
                        }
                    }
                }

                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", jsonArray);

                return JsonUtils.zwdtRestReturn("1", "事项获取成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getsdqtask接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getsdqtask接口参数：params【" + params + "】=======");
            log.info("=======getsdqtask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getsdqtask异常信息：" + e.getMessage(), "");
        }
    }

    /**
     * 附件列表获取接口
     *
     * @return
     * @params params
     */
    @RequestMapping(value = {"/getAttachListByClientguid"}, method = {RequestMethod.POST})
    public String getAttachListByClientguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            this.log.info("=======开始获取getAttachListByClientguid接口=======");
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if ("Epoint_WebSerivce_**##0601".equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                String clientGuid = obj.getString("clientguid");
                List<FrameAttachInfo> frameAttchInfoList = this.iAttachService.getAttachInfoListByGuid(clientGuid);
                List<JSONObject> attachJsonList = new ArrayList();
                Iterator var9 = frameAttchInfoList.iterator();

                while (var9.hasNext()) {
                    FrameAttachInfo frameAttachInfo = (FrameAttachInfo) var9.next();
                    JSONObject attachJson = new JSONObject();
                    attachJson.put("attachname", frameAttachInfo.getAttachFileName());
                    attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                    attachJson.put("id", frameAttachInfo.getAttachGuid());
                    long attachLengthB = frameAttachInfo.getAttachLength();
                    attachJson.put("length", attachLengthB + "B");
                    if (attachLengthB > 1024L) {
                        long attachLengthKB = frameAttachInfo.getAttachLength() / 1024L;
                        attachJson.put("length", attachLengthKB + "KB");
                        if (attachLengthKB > 1024L) {
                            long attachlengthMB = attachLengthKB / 1024L;
                            attachJson.put("length", attachlengthMB + "MB");
                        }
                    }
                    // 获取请求地址
                    String urlroot = JsonUtils.getRootUrl(request);
                    attachJson.put("attachsrc", urlroot + "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + frameAttachInfo.getAttachGuid());

                    attachJson.put("uploaddate", EpointDateUtil.convertDate2String(frameAttachInfo.getUploadDateTime()));
                    attachJsonList.add(attachJson);
                }

                JSONObject dataJson = new JSONObject();
                dataJson.put("attachlist", attachJsonList);
                this.log.info("=======结束获取getAttachListByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取材料附件列表成功！", dataJson);
            }
            else {
                this.log.info("=======结束获取getAttachListByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception var18) {
            var18.printStackTrace();
            this.log.info("=======获取getAttachListByClientguid接口异常=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料附件列表异常", "");
        }
    }
}
