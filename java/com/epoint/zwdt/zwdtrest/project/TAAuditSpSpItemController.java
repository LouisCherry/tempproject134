package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.domain.AuditRsItemBaseinfoOne;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.inter.IAuditRsItemBaseinfoOneService;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoonewscompany.domain.AuditRsItemBaseinfoOneWscompany;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfoonewscompany.inter.IAuditRsItemBaseinfoOneWscompanyService;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.IAuditRsItemBaseinfoTwoService;
import com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.entity.AuditRsItemBaseinfoTwo;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.phasemonitorinfo.api.IPhaseMonitorInfoService;
import com.epoint.phasemonitorinfo.api.entity.PhaseMonitorInfo;

/**
 * 建设项目网上申报相关接口
 *
 * @作者 WXH
 */
@RestController
@RequestMapping("/taauditspspitemcontroller")
public class TAAuditSpSpItemController extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -8808382622921682282L;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IMessagesCenterService messageCenterService;

    // 第一阶段
    @Autowired
    private IAuditRsItemBaseinfoOneService iAuditRsItemBaseinfoOneService;
    @Autowired
    private IAuditRsItemBaseinfoOneWscompanyService iAuditRsItemBaseinfoOneWscompanyService;
    private AuditRsItemBaseinfoOneWscompany auditRsItemBaseinfoOneWscompany;

    // 第二阶段
    @Autowired
    private IAuditRsItemBaseinfoTwoService iAuditRsItemBaseinfoTwoService;
    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;
    @Autowired
    private IPhaseMonitorInfoService monitorService;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    private String subappGuid;

    /**
     * 施工许可阶段的三个子表
     */
    /*
     * private DataGridModel<Sgxksubtable2> sub2model = null; private
     * DataGridModel<Sgxksubtable3> sub3model = null;
     */
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
                // 4-第一阶段,立项用地规划许可; 3-第二阶段,工程建设许可; 2-第三阶段,施工许可; 1-第四阶段,竣工验收
                String phasenum = obj.getString("phasenum");
                // 2、获取用户注册信息
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                if (auditOnlineRegister == null) {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // 3、 获取基本项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                if (StringUtil.isNotBlank(itemGuid)) {
                    auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                } else {
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
                if (auditRsItemBaseinfo == null) {
                    return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                }
                // 3.1.1 获取该项目的申报单位的统一社会信用代码
                String itemLegalCreditCode = StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum();
                // 3.1.2 判断当前用户是否属于这家企业
                if (StringUtil.isBlank(itemLegalCreditCode)) {
                    return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                }
                // 3.1.2.1、 查询出这家企业
                SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                companySqlConditionUtil.eq("creditcode", itemLegalCreditCode);
                companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                companySqlConditionUtil.eq("isactivated", "1");
                List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                        .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap()).getResult();
                if (auditRsCompanyBaseinfos == null || auditRsCompanyBaseinfos.size() == 0) {
                    return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                }
                //System.out.println("auditRsCompanyBaseinfos王小龙" + auditRsCompanyBaseinfos);
                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                // 3.1.2.2、 获取企业法人证件号
                //System.out.println("auditRsCompanyBaseinfo王小龙" + auditRsCompanyBaseinfo);
                String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                // 3.1.2.3、 获取企业id
                String companyId = auditRsCompanyBaseinfo.getCompanyid();
                // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                grantConditionUtil.eq("companyid", companyId);
                grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
                List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                        .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                System.out.println("auditOnlineCompanyGrants王小龙" + auditOnlineCompanyGrants);
                if (idNum.equals(auditOnlineRegister.getIdnumber()) || auditOnlineCompanyGrants.size() > 0) {
                    JSONObject dataJson = new JSONObject();
                    if (StringUtil.isNotBlank(phasenum)) {
                        switch (phasenum) {
                            case "one": // 第一阶段
                                dataJson = getLxydghxkInfo(subappguid, auditRsItemBaseinfo);
                                break;
                            case "two": // 第二阶段
                                dataJson = getGcjsxkInfo(subappguid, auditRsItemBaseinfo);
                                break;
                            /*
                             * case "two": // 第二阶段 dataJson =
                             * getGcjsxkInfo(subappguid, auditRsItemBaseinfo);
                             * break; case "three": // 第三阶段 dataJson =
                             * getSgxkInfo(subappguid, auditRsItemBaseinfo); break;
                             * case "four": // 第四阶段 dataJson =
                             * getJgysInfo(subappguid, auditRsItemBaseinfo); break;
                             */
                        }
                    }
                    log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                }

            } else {
                log.info("=======结束调用initAuditRsItemBaseinfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======initAuditRsItemBaseinfo接口参数：params【" + params + "】=======");
            log.info("=======initAuditRsItemBaseinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "initAuditRsItemBaseinfo异常信息：" + e.getMessage(), "");
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
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);
                if (auditOnlineRegister != null) {
                    // 获取参数
                    JSONObject param = jsonObject.getJSONObject("params");
                    String phasenum = param.getString("phasenum");
                    if (StringUtil.isNotBlank(phasenum)) {
                        switch (phasenum) {
                            case "one": // 第一阶段
                                saveLxydghxkInfo(param, auditOnlineRegister);
                                break;
                            case "two": // 第二阶段
                                saveGcjsxkInfo(param, auditOnlineRegister);
                                break;
                            /*
                             * case "two": // 第二阶段 saveGcjsxkInfo(param); break;
                             * case "three": // 第三阶段 saveSgxkInfo(param); break;
                             * case "four": // 第四阶段 saveJgysInfo(param); break;
                             */
                        }

                    }
                    log.info("=======结束调用saveAuditPhaseOne接口=======");
                    return JsonUtils.zwdtRestReturn("1", "表单信息存储成功", "");
                } else {
                    log.info("=======结束调用saveAuditPhaseOne接口=======");
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败!", "");
                }
            } else {
                log.info("=======结束调用saveAuditPhaseOne接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败!", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveAuditPhaseOne接口参数：params【" + params + "】=======");
            log.info("=======saveAuditPhaseOne接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存或提交电子表单信息失败!", "");
        }
    }

    /**
     * 加载第一阶段信息
     */
    private JSONObject getLxydghxkInfo(String subappguid, AuditRsItemBaseinfo auditRsItemBaseinfo) {
        JSONObject dataJson = new JSONObject();

        if (auditRsItemBaseinfo != null) {
            ParticipantsInfo participantsInfo = iParticipantsInfoService
                    .findParticipantsInfoByItemGuid(auditRsItemBaseinfo.getRowguid());
            AuditRsItemBaseinfoOne auditRsItemBaseinfoOne = iAuditRsItemBaseinfoOneService
                    .getAuditRsItemBaseinfoOneByparentidandpaseguid(auditRsItemBaseinfo.getBiguid());
            auditRsItemBaseinfoOneWscompany = iAuditRsItemBaseinfoOneWscompanyService
                    .getAuditRsItemBaseinfoOneWscompanyByparentidandpaseguid(auditRsItemBaseinfo.getBiguid(),
                            subappguid);
            if (auditRsItemBaseinfoOne != null) {
                dataJson.put("subappname", auditRsItemBaseinfoOne.getSubappname()); // 子申报名称
                if (StringUtil.isNotBlank(auditRsItemBaseinfoOne.getZdxm())) {
                    dataJson.put("projectLevel", getSelectItemList("是否", auditRsItemBaseinfoOne.getZdxm())); // 重点项目
                } else {
                    dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
                }
                dataJson.put("itemAddress", auditRsItemBaseinfoOne.getXmdz()); // 项目地址
                dataJson.put("eastToArea", auditRsItemBaseinfoOne.getDz()); // 四至范围（东至）
                dataJson.put("westToArea", auditRsItemBaseinfoOne.getXz()); // 四至范围（西至）
                dataJson.put("southToArea", auditRsItemBaseinfoOne.getNz()); // 四至范围（南至）
                dataJson.put("northToArea", auditRsItemBaseinfoOne.getBz()); // 四至范围（北至）
                if (StringUtil.isNotBlank(auditRsItemBaseinfoOne.getZdxm())) {
                    dataJson.put("proTyped", getSelectItemList("立项类型", auditRsItemBaseinfoOne.getLxlx()));// 立项类型
                } else {
                    dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
                }
                dataJson.put("proOu", auditRsItemBaseinfoOne.getLxbm()); // 立项部门
                dataJson.put("hangyeType", auditRsItemBaseinfoOne.getHylb()); // 行业类别
                dataJson.put("useLandType", auditRsItemBaseinfoOne.getGhydxzjxz()); // 规划用地性质
                dataJson.put("overLoadArea", auditRsItemBaseinfoOne.getDsjzmj()); // 地上
                dataJson.put("downLoadArea", auditRsItemBaseinfoOne.getDxjzmj()); // 地下
                dataJson.put("xznxfl", auditRsItemBaseinfoOne.getXznzhnyxh()); // 新增年综合能源消费量吨标煤（当量值）
                dataJson.put("xznydl", auditRsItemBaseinfoOne.getXznydl()); // 新增年用电量（万千瓦时）
                dataJson.put("zcxwj", auditRsItemBaseinfoOne.getZcxwj()); // 支持性文件
            } else {
                dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
                dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
            }
            dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
            dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode()); // 投资平台项目代码
            // dataJson.put("area", auditSpSpLxydghxk.getArea()); // 区（园区）
            // dataJson.put("road", auditSpSpLxydghxk.getRoad()); // 街道
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getXmtzly().toString())) {
                dataJson.put("investTyped", getSelectItemList("项目投资来源", auditRsItemBaseinfo.getXmtzly().toString()));// 投资类型
            } else {
                dataJson.put("investTyped", getSelectItemList("项目投资来源", true));// 投资类型
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getFundsources())) {
                dataJson.put("moneySources", getSelectItemList("资金来源", auditRsItemBaseinfo.getFundsources())); // 资金来源
            } else {
                dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getTdhqfs().toString())) {
                dataJson.put("areaSources", getSelectItemList("土地获取方式", auditRsItemBaseinfo.getTdhqfs().toString())); // 土地来源
            } else {
                dataJson.put("areaSources", getSelectItemList("土地获取方式", true)); // 土地来源
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                dataJson.put("buildProperties",
                        getSelectItemList("建设性质", auditRsItemBaseinfo.getConstructionproperty())); // 建设性质
            } else {
                dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getItemtype())) {
                dataJson.put("buildType", getSelectItemList("审批流程类型", auditRsItemBaseinfo.getItemtype())); // 项目类型
            } else {
                dataJson.put("buildType", getSelectItemList("审批流程类型", true)); // 项目类型
            }
            dataJson.put("allMoney", auditRsItemBaseinfo.getTotalinvest()); // 总投资
            dataJson.put("areaUsed", auditRsItemBaseinfo.getLandarea());// 用地面积(公顷)
            dataJson.put("newLandType", auditRsItemBaseinfo.getNewlandarea()); // 新增用地面积（公顷）
            dataJson.put("allBuildArea", auditRsItemBaseinfo.getJzmj()); // 总建筑面积（m²）

            dataJson.put("buildContent", auditRsItemBaseinfo.getConstructionscaleanddesc()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate", StringUtil.isBlank(auditRsItemBaseinfo.getItemstartdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate", StringUtil.isBlank(auditRsItemBaseinfo.getItemfinishdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd")); // 计划竣工日期
            // dataJson.put("itemSource", getSelectItemList("项目来源",
            // auditSpSpLxydghxk.getItemsource())); // 项目来源

            // 建设单位
            if (participantsInfo != null) {
                dataJson.put("totalSocietyCode", participantsInfo.getCorpcode());// 统一社会信用代码
                dataJson.put("proUnit", participantsInfo.getCorpname());// 项目单位
                if (StringUtil.isNotBlank(participantsInfo.getLegalproperty())) {
                    dataJson.put("corporationType", getSelectItemList("法人性质", participantsInfo.getLegalproperty()));// 法人类型
                } else {
                    dataJson.put("corporationType", getSelectItemList("法人性质", true));
                }
                dataJson.put("corporationName", participantsInfo.getLegal());// 法定代表人
                dataJson.put("telAddress", participantsInfo.getAddress());// 通讯地址
                dataJson.put("postCode", participantsInfo.getStr("zipcode"));// 邮政编码
                dataJson.put("projectPerson", participantsInfo.getXmfzr());// 项目负责人
                dataJson.put("telNumber", participantsInfo.getXmfzr_phone());// 联系电话
                dataJson.put("applicantName", participantsInfo.getDanweilxr());// 授权申报人
                dataJson.put("appTelNumber", participantsInfo.getDanweilxrlxdh());// 联系电话
                // dataJson.put("certType", getSelectItemList("申请人用来唯一标识的证照类型",
                // auditRsItemBaseinfo.getTdhqfs().toString()));// 身份证件类型
                dataJson.put("certNumber", participantsInfo.getXmfzr_idcard());// 身份证件号码
            }
            // 第一阶段新增
            // dataJson.put("standProType",auditSpSpLxydghxk.getStr("standProType"));//
            // 立项类型
            // dataJson.put("projectType",auditSpSpLxydghxk.getStr("projectType"));//
            // 项目类型
            if (auditRsItemBaseinfoOneWscompany != null) {
                dataJson.put("zcaddress", auditRsItemBaseinfoOneWscompany.getZcdd());// 注册地点
                dataJson.put("registeMoney", auditRsItemBaseinfoOneWscompany.getZczj());// 注册资金（万美元）
                dataJson.put("investCountry1", auditRsItemBaseinfoOneWscompany.getTzfj());// 投资方（国别）
                dataJson.put("country1Per", auditRsItemBaseinfoOneWscompany.getTzfjzb());// 占比（%）
                dataJson.put("investCountry2", auditRsItemBaseinfoOneWscompany.getTzfy());// 投资方（国别）
                dataJson.put("country2Per", auditRsItemBaseinfoOneWscompany.getTzfyzb());// 占比（%）
                dataJson.put("investCountry3", auditRsItemBaseinfoOneWscompany.getTzfb());// 投资方（国别）
                dataJson.put("country3Per", auditRsItemBaseinfoOneWscompany.getTzfbzb());// 占比（%）
            }
        } else {
            dataJson.put("corporationType", getSelectItemList("法人性质", true));
            dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
            dataJson.put("investTyped", getSelectItemList("项目投资来源", true));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地获取方式", true)); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
            dataJson.put("buildType", getSelectItemList("审批流程类型", true)); // 项目类型

            // 法人类型
            /*
             * dataJson.put("certType", getSelectItemList("身份证件类型", true)); //
             * 身份证件类型 dataJson.put("itemSource", getSelectItemList("项目来源",
             * true)); // 项目来源 dataJson.put("applytype",
             * getSelectItemList("申请种类", true)); // 项目来源
             */
        }
        return dataJson;
    }

    /**
     * 加载第二阶段信息
     */
    private JSONObject getGcjsxkInfo(String subappguid, AuditRsItemBaseinfo auditRsItemBaseinfo) {
        JSONObject dataJson = new JSONObject();
        if (auditRsItemBaseinfo != null) {
            ParticipantsInfo participantsInfo = iParticipantsInfoService
                    .findParticipantsInfoByItemGuid(auditRsItemBaseinfo.getRowguid());
            AuditRsItemBaseinfoOne auditRsItemBaseinfoOne = iAuditRsItemBaseinfoOneService
                    .getAuditRsItemBaseinfoOneByparentidandpaseguid(auditRsItemBaseinfo.getBiguid());
            AuditRsItemBaseinfoTwo auditRsItemBaseinfoTwo = iAuditRsItemBaseinfoTwoService
                    .getAuditRsItemBaseinfoTwoByparentidandpaseguid(auditRsItemBaseinfo.getBiguid(), subappguid);
            if (auditRsItemBaseinfoOne != null) {
                dataJson.put("subappname", auditRsItemBaseinfoOne.getSubappname()); // 子申报名称
                if (StringUtil.isNotBlank(auditRsItemBaseinfoOne.getZdxm())) {
                    dataJson.put("projectLevel", getSelectItemList("是否", auditRsItemBaseinfoOne.getZdxm())); // 重点项目
                } else {
                    dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
                }
                dataJson.put("itemAddress", auditRsItemBaseinfoOne.getXmdz()); // 项目地址
                dataJson.put("eastToArea", auditRsItemBaseinfoOne.getDz()); // 四至范围（东至）
                dataJson.put("westToArea", auditRsItemBaseinfoOne.getXz()); // 四至范围（西至）
                dataJson.put("southToArea", auditRsItemBaseinfoOne.getNz()); // 四至范围（南至）
                dataJson.put("northToArea", auditRsItemBaseinfoOne.getBz()); // 四至范围（北至）
                if (StringUtil.isNotBlank(auditRsItemBaseinfoOne.getZdxm())) {
                    dataJson.put("proTyped", getSelectItemList("立项类型", auditRsItemBaseinfoOne.getLxlx()));// 立项类型
                } else {
                    dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
                }
                dataJson.put("proOu", auditRsItemBaseinfoOne.getLxbm()); // 立项部门
                dataJson.put("hangyeType", auditRsItemBaseinfoOne.getHylb()); // 行业类别
                dataJson.put("useLandType", auditRsItemBaseinfoOne.getGhydxzjxz()); // 规划用地性质
                dataJson.put("overLoadArea", auditRsItemBaseinfoOne.getDsjzmj()); // 地上
                dataJson.put("downLoadArea", auditRsItemBaseinfoOne.getDxjzmj()); // 地下
            } else {
                dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
                dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
            }
            dataJson.put("itemname", auditRsItemBaseinfo.getItemname());// 项目名称
            dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode()); // 投资平台项目代码
            // dataJson.put("area", auditSpSpLxydghxk.getArea()); // 区（园区）
            // dataJson.put("road", auditSpSpLxydghxk.getRoad()); // 街道
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getXmtzly().toString())) {
                dataJson.put("investTyped", getSelectItemList("项目投资来源", auditRsItemBaseinfo.getXmtzly().toString()));// 投资类型
            } else {
                dataJson.put("investTyped", getSelectItemList("项目投资来源", true));// 投资类型
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getFundsources())) {
                dataJson.put("moneySources", getSelectItemList("资金来源", auditRsItemBaseinfo.getFundsources())); // 资金来源
            } else {
                dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getTdhqfs().toString())) {
                dataJson.put("areaSources", getSelectItemList("土地获取方式", auditRsItemBaseinfo.getTdhqfs().toString())); // 土地来源
            } else {
                dataJson.put("areaSources", getSelectItemList("土地获取方式", true)); // 土地来源
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                dataJson.put("buildProperties",
                        getSelectItemList("建设性质", auditRsItemBaseinfo.getConstructionproperty())); // 建设性质
            } else {
                dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
            }
            if (StringUtil.isNotBlank(auditRsItemBaseinfo.getItemtype())) {
                dataJson.put("buildType", getSelectItemList("审批流程类型", auditRsItemBaseinfo.getItemtype())); // 项目类型
            } else {
                dataJson.put("buildType", getSelectItemList("审批流程类型", true)); // 项目类型
            }
            dataJson.put("allMoney", auditRsItemBaseinfo.getTotalinvest()); // 总投资
            dataJson.put("areaUsed", auditRsItemBaseinfo.getLandarea());// 用地面积(公顷)
            dataJson.put("newLandType", auditRsItemBaseinfo.getNewlandarea()); // 新增用地面积（公顷）
            dataJson.put("allBuildArea", auditRsItemBaseinfo.getJzmj()); // 总建筑面积（m²）

            dataJson.put("buildContent", auditRsItemBaseinfo.getConstructionscaleanddesc()); // 主要建设内容和技术指标(包括必要性)
            dataJson.put("planStartDate", StringUtil.isBlank(auditRsItemBaseinfo.getItemstartdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemstartdate(), "yyyy-MM-dd")); // 计划开工日期
            dataJson.put("planEndDate", StringUtil.isBlank(auditRsItemBaseinfo.getItemfinishdate()) ? ""
                    : EpointDateUtil.convertDate2String(auditRsItemBaseinfo.getItemfinishdate(), "yyyy-MM-dd")); // 计划竣工日期
            // dataJson.put("itemSource", getSelectItemList("项目来源",
            // auditSpSpLxydghxk.getItemsource())); // 项目来源

            // 建设单位
            if (participantsInfo != null) {
                dataJson.put("totalSocietyCode", participantsInfo.getCorpcode());// 统一社会信用代码
                dataJson.put("proUnit", participantsInfo.getCorpname());// 项目单位
                if (StringUtil.isNotBlank(participantsInfo.getLegalproperty())) {
                    dataJson.put("corporationType", getSelectItemList("法人性质", participantsInfo.getLegalproperty()));// 法人类型
                } else {
                    dataJson.put("corporationType", getSelectItemList("法人性质", true));
                }
                dataJson.put("corporationName", participantsInfo.getLegal());// 法定代表人
                dataJson.put("telAddress", participantsInfo.getAddress());// 通讯地址
                dataJson.put("postCode", participantsInfo.getStr("zipcode"));// 邮政编码
                dataJson.put("projectPerson", participantsInfo.getXmfzr());// 项目负责人
                dataJson.put("telNumber", participantsInfo.getXmfzr_phone());// 联系电话
                dataJson.put("applicantName", participantsInfo.getDanweilxr());// 授权申报人
                dataJson.put("appTelNumber", participantsInfo.getDanweilxrlxdh());// 联系电话
                // dataJson.put("certType", getSelectItemList("申请人用来唯一标识的证照类型",
                // auditRsItemBaseinfo.getTdhqfs().toString()));// 身份证件类型
                dataJson.put("certNumber", participantsInfo.getXmfzr_idcard());// 身份证件号码
            }
            // 第二阶段新增
            // dataJson.put("standProType",auditSpSpLxydghxk.getStr("standProType"));//
            // 立项类型
            // dataJson.put("projectType",auditSpSpLxydghxk.getStr("projectType"));//
            // 项目类型
            if (auditRsItemBaseinfoTwo != null) {
                dataJson.put("ldmj", auditRsItemBaseinfoTwo.getLdmj());// 绿地面积
                dataJson.put("ldl", auditRsItemBaseinfoTwo.getLdl());// 绿地率
                dataJson.put("qsfs", auditRsItemBaseinfoTwo.getQsfs());// 取水方式
                dataJson.put("tsfs", auditRsItemBaseinfoTwo.getTsfs());// 退水方式
                dataJson.put("qsl", auditRsItemBaseinfoTwo.getQsl());// 取水量
                dataJson.put("qslx", auditRsItemBaseinfoTwo.getQslx());// 取水类型
                dataJson.put("dyqk", auditRsItemBaseinfoTwo.getDyqk());// 电源情况
                dataJson.put("trqk", auditRsItemBaseinfoTwo.getTrqk());// 土壤情况
                dataJson.put("flth", auditRsItemBaseinfoTwo.getFlth());// 防雷图号
                dataJson.put("jglx", auditRsItemBaseinfoTwo.getJglx());// 结构类型
                dataJson.put("zbdljg", auditRsItemBaseinfoTwo.getZbdljg());// 招标代理机构
                dataJson.put("gczjzxqy", auditRsItemBaseinfoTwo.getGczjzxqy());// 工程造价咨询企业
            }
        } else {
            dataJson.put("corporationType", getSelectItemList("法人性质", true));
            dataJson.put("projectLevel", getSelectItemList("是否", true)); // 重点项目
            dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
            dataJson.put("investTyped", getSelectItemList("项目投资来源", true));// 投资类型
            dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
            dataJson.put("areaSources", getSelectItemList("土地获取方式", true)); // 土地来源
            dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
            dataJson.put("buildType", getSelectItemList("审批流程类型", true)); // 项目类型

            // 法人类型
            /*
             * dataJson.put("certType", getSelectItemList("身份证件类型", true)); //
             * 身份证件类型 dataJson.put("itemSource", getSelectItemList("项目来源",
             * true)); // 项目来源 dataJson.put("applytype",
             * getSelectItemList("申请种类", true)); // 项目来源
             */
        }

        return dataJson;
    }

    /**
     * 加载第三阶段信息
     */
    /*
     * private JSONObject getSgxkInfo(String subappguid, AuditRsItemBaseinfo
     * auditRsItemBaseinfo) { JSONObject dataJson = new JSONObject();
     * AuditSpSpSgxk auditSpSpSgxk =
     * iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid); if
     * (auditSpSpSgxk != null) { dataJson.put("itemname",
     * auditSpSpSgxk.getItemname());// 项目名称 dataJson.put("subappname",
     * auditSpSpSgxk.getSubappname()); // 子申报名称 dataJson.put("itemcode",
     * auditSpSpSgxk.getItemcode()); // 投资平台项目代码 dataJson.put("projectLevel",
     * getSelectItemList("项目等级", auditSpSpSgxk.getProjectlevel())); // 重点项目
     * dataJson.put("itemAddress", auditSpSpSgxk.getItemaddress()); // 项目地址
     * dataJson.put("area", auditSpSpSgxk.getArea()); // 区（园区）
     * dataJson.put("road", auditSpSpSgxk.getRoad()); // 街道
     * dataJson.put("eastToArea", auditSpSpSgxk.getEasttoarea()); // 四至范围（东至）
     * dataJson.put("westToArea", auditSpSpSgxk.getWesttoarea()); // 四至范围（西至）
     * dataJson.put("southToArea", auditSpSpSgxk.getSouthtoarea()); // 四至范围（南至）
     * dataJson.put("northToArea", auditSpSpSgxk.getNorthtoarea()); // 四至范围（北至）
     * dataJson.put("proTyped", getSelectItemList("立项类型",
     * auditSpSpSgxk.getProtyped()));// 立项类型 dataJson.put("proOu",
     * auditSpSpSgxk.getProou()); // 立项部门 dataJson.put("investTyped",
     * getSelectItemList("投资类型", auditSpSpSgxk.getInvesttyped()));// 投资类型
     * dataJson.put("hangyeType", auditSpSpSgxk.getHangyetype()); // 行业类别
     * dataJson.put("moneySources", getSelectItemList("资金来源",
     * auditSpSpSgxk.getMoneysources())); // 资金来源 dataJson.put("allMoney",
     * auditSpSpSgxk.getAllmoney()); // 总投资 dataJson.put("areaSources",
     * getSelectItemList("土地来源", auditSpSpSgxk.getAreasources())); // 土地来源
     * dataJson.put("useLandType", auditSpSpSgxk.getUselandtype()); // 规划用地性质
     * dataJson.put("areaUsed", auditSpSpSgxk.getAreaused());// 用地面积(公顷)
     * dataJson.put("newLandType", auditSpSpSgxk.getNewlandtype()); //
     * 新增用地面积（公顷） dataJson.put("buildProperties", getSelectItemList("建设性质",
     * auditSpSpSgxk.getBuildproperties())); // 建设性质 dataJson.put("buildType",
     * getSelectItemList("项目类型", auditSpSpSgxk.getBuildtype())); // 项目类型
     * dataJson.put("allBuildArea", auditSpSpSgxk.getAllbuildarea()); //
     * 总建筑面积（m²） dataJson.put("overLoadArea", auditSpSpSgxk.getOverloadarea());
     * // 地上 dataJson.put("downLoadArea", auditSpSpSgxk.getDownloadarea()); //
     * 地下 dataJson.put("buildContent", auditSpSpSgxk.getBuildcontent()); //
     * 主要建设内容和技术指标(包括必要性) dataJson.put("planStartDate",
     * StringUtil.isBlank(auditSpSpSgxk.getPlanstartdate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanstartdate(),
     * "yyyy-MM-dd")); // 计划开工日期 dataJson.put("planEndDate",
     * StringUtil.isBlank(auditSpSpSgxk.getPlanenddate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpSgxk.getPlanenddate(),
     * "yyyy-MM-dd")); // 计划竣工日期 dataJson.put("lxwh", auditSpSpSgxk.getLxwh());
     * // 立项文号 dataJson.put("lxrq", StringUtil.isBlank(auditSpSpSgxk.getLxrq())
     * ? "" : EpointDateUtil.convertDate2String(auditSpSpSgxk.getLxrq(),
     * "yyyy-MM-dd")); // 立项日期 dataJson.put("lxjb", auditSpSpSgxk.getLxjb()); //
     * 立项级别 dataJson.put("xmbm", auditSpSpSgxk.getXmbm()); // 项目编码
     * dataJson.put("gcyt", auditSpSpSgxk.getGcyt()); // 工程用途
     * dataJson.put("gcfl", auditSpSpSgxk.getGcfl()); // 工程分类
     * dataJson.put("zftzgs", auditSpSpSgxk.getZftzgs()); // 政府投资概算
     * dataJson.put("ghyzzpl", auditSpSpSgxk.getGhyzzpl()); // 规划预制装配率 // 新增
     * dataJson.put("totalSocietyCode",
     * auditSpSpSgxk.getStr("totalSocietyCode"));// 统一社会信用代码
     * dataJson.put("proUnit", auditSpSpSgxk.getStr("proUnit"));// 项目单位
     * dataJson.put("corporationType", getSelectItemList("法人类型",
     * auditSpSpSgxk.getStr("corporationType")));// 法人类型
     * dataJson.put("corporationName",
     * auditSpSpSgxk.getStr("corporationName"));// 法定代表人
     * dataJson.put("telAddress", auditSpSpSgxk.getStr("telAddress"));// 通讯地址
     * dataJson.put("postCode", auditSpSpSgxk.getStr("postCode"));// 邮政编码
     * dataJson.put("projectPerson", auditSpSpSgxk.getStr("projectPerson"));//
     * 项目负责人 dataJson.put("telNumber", auditSpSpSgxk.getStr("telNumber"));//
     * 联系电话 dataJson.put("applicantName",
     * auditSpSpSgxk.getStr("applicantName"));// 授权申报人
     * dataJson.put("appTelNumber", auditSpSpSgxk.getStr("appTelNumber"));//
     * 联系电话 dataJson.put("certType", getSelectItemList("身份证件类型",
     * auditSpSpSgxk.getStr("certType")));// 身份证件类型
     *
     * dataJson.put("certNumber", auditSpSpSgxk.getStr("certNumber"));// 身份证件号码
     * dataJson.put("stwcrq", auditSpSpSgxk.getStr("stwcrq"));// 审图完成日期
     * dataJson.put("zbfs", auditSpSpSgxk.getStr("zbfs"));// 招标方式
     * dataJson.put("gcyt", auditSpSpSgxk.getStr("gcyt"));// 工程用途
     * dataJson.put("zbrq", auditSpSpSgxk.getStr("zbrq"));// 中标日期
     * dataJson.put("zbje", auditSpSpSgxk.getStr("zbje"));// 中标金额
     * dataJson.put("jgxs", auditSpSpSgxk.getStr("jgxs"));// 结构形式
     * dataJson.put("jksd", auditSpSpSgxk.getStr("jksd"));// 基坑深度
     * dataJson.put("htlb", auditSpSpSgxk.getStr("htlb"));// 合同类别
     * dataJson.put("Ctrl20", auditSpSpSgxk.getStr("Ctrl20"));// 合同签订日期
     * dataJson.put("Ctrl21", auditSpSpSgxk.getStr("Ctrl21"));// 合同工期
     * dataJson.put("htkgsj", auditSpSpSgxk.getStr("Ctrl4"));// 合同开工时间
     * dataJson.put("htjgsj", auditSpSpSgxk.getStr("Ctrl5"));// 合同竣工时间
     * dataJson.put("htje", auditSpSpSgxk.getStr("Ctrl22"));// 合同金额（万元）
     * dataJson.put("tdzbh", auditSpSpSgxk.getStr("Ctrl23"));// 土地证编号
     * dataJson.put("jsgcghxkzbh", auditSpSpSgxk.getStr("Ctrl24"));//
     * 建设工程规划许可证编号 dataJson.put("kcbmxydm", auditSpSpSgxk.getStr("Ctrl26"));//
     * 勘察部门信用代码 dataJson.put("kcbmmc", auditSpSpSgxk.getStr("Ctrl25"));// 勘察部门名称
     * dataJson.put("kcbmfzr", auditSpSpSgxk.getStr("Ctrl27"));// 勘察部门负责人
     * dataJson.put("kcbmtel", auditSpSpSgxk.getStr("Ctrl28"));// 勘察部门电话
     * dataJson.put("sjdwxydm", auditSpSpSgxk.getStr("Ctrl30"));// 设计单位信用代码
     * dataJson.put("sjdwmc", auditSpSpSgxk.getStr("Ctrl29"));// 设计单位名称
     * dataJson.put("sjdwfzr", auditSpSpSgxk.getStr("Ctrl31"));// 设计单位负责人
     * dataJson.put("sjdwtel", auditSpSpSgxk.getStr("Ctrl32"));// 设计单位电话
     * dataJson.put("sgcbdwxydm", auditSpSpSgxk.getStr("Ctrl34"));// 施工总承包单位信用代码
     * dataJson.put("sgcbdwmc", auditSpSpSgxk.getStr("Ctrl33"));// 施工总承包单位名称
     * dataJson.put("sgcbdwfzr", auditSpSpSgxk.getStr("Ctrl35"));// 施工总承包单位负责人
     * dataJson.put("sgcbdwtel", auditSpSpSgxk.getStr("Ctrl36"));// 施工总承包单位电话
     * dataJson.put("jldwxydm", auditSpSpSgxk.getStr("Ctrl38"));// 监理单位信用代码
     * dataJson.put("jldwmc", auditSpSpSgxk.getStr("Ctrl37"));// 监理单位名称
     * dataJson.put("jldwfzr", auditSpSpSgxk.getStr("Ctrl39"));// 监理单位负责人
     * dataJson.put("jldwtel", auditSpSpSgxk.getStr("Ctrl40"));// 监理单位电话
     * dataJson.put("tsjgxydm", auditSpSpSgxk.getStr("Ctrl42"));// 图审机构信用代码
     * dataJson.put("tsjgmc", auditSpSpSgxk.getStr("Ctrl41"));// 图审机构名称
     * dataJson.put("tsjgfzr", auditSpSpSgxk.getStr("Ctrl43"));// 图审机构负责人
     * dataJson.put("tsjgtel", auditSpSpSgxk.getStr("Ctrl44"));// 图审机构电话
     *
     * dataJson.put("Ctrl53", getSelectItemNoEmptyList("建设性质",
     * auditSpSpSgxk.getStr("Ctrl53")));// 类别 dataJson.put("jzxfsjshlb",
     * getSelectItemNoEmptyList("建设工程消防设计审核类别",
     * auditSpSpSgxk.getStr("Ctrl54")));// 建设工程消防设计审核类别 dataJson.put("Ctrl57",
     * auditSpSpSgxk.getStr("Ctrl57"));// 设计单位名称 dataJson.put("Ctrl58",
     * auditSpSpSgxk.getStr("Ctrl58"));// 设计单位等级 dataJson.put("Ctrl59",
     * auditSpSpSgxk.getStr("Ctrl59"));// 设计单位法人 dataJson.put("Ctrl60",
     * auditSpSpSgxk.getStr("Ctrl60"));// 设计单位联系人 dataJson.put("Ctrl62",
     * auditSpSpSgxk.getStr("Ctrl62"));// 设计单位电话 dataJson.put("Ctrl63",
     * auditSpSpSgxk.getStr("Ctrl63"));// 施工单位名称 dataJson.put("Ctrl64",
     * auditSpSpSgxk.getStr("Ctrl64"));// 施工单位等级 dataJson.put("Ctrl65",
     * auditSpSpSgxk.getStr("Ctrl65"));// 施工单位法人 dataJson.put("Ctrl66",
     * auditSpSpSgxk.getStr("Ctrl66"));// 施工单位联系人 dataJson.put("Ctrl67",
     * auditSpSpSgxk.getStr("Ctrl67"));// 施工单位电话 dataJson.put("Ctrl69",
     * auditSpSpSgxk.getStr("Ctrl69"));// 监理单位名称 dataJson.put("Ctrl70",
     * auditSpSpSgxk.getStr("Ctrl70"));// 监理单位等级 dataJson.put("Ctrl71",
     * auditSpSpSgxk.getStr("Ctrl71"));// 监理单位法人 dataJson.put("Ctrl72",
     * auditSpSpSgxk.getStr("Ctrl72"));// 监理单位联系人 dataJson.put("Ctrl74",
     * auditSpSpSgxk.getStr("Ctrl74"));// 监理单位电话
     *
     * dataJson.put("Ctrl105", auditSpSpSgxk.getStr("Ctrl105"));// 设置位置
     * dataJson.put("Ctrl107", auditSpSpSgxk.getStr("Ctrl107"));// 总容量(平方米)
     * dataJson.put("Ctrl108", auditSpSpSgxk.getStr("Ctrl108"));// 储存物质名称
     * dataJson.put("Ctrl106", auditSpSpSgxk.getStr("Ctrl106"));// 储量
     * dataJson.put("Ctrl110", auditSpSpSgxk.getStr("Ctrl110"));// 储存物质名称
     * dataJson.put("Ctrl111", auditSpSpSgxk.getStr("Ctrl111"));// 材料类别
     * dataJson.put("Ctrl113", auditSpSpSgxk.getStr("Ctrl113"));// 保温层数
     * dataJson.put("Ctrl112", auditSpSpSgxk.getStr("Ctrl112"));// 使用性质
     * dataJson.put("Ctrl114", auditSpSpSgxk.getStr("Ctrl114"));// 原有用途
     * dataJson.put("Ctrl116", auditSpSpSgxk.getStr("Ctrl116"));// 保温层数
     * dataJson.put("Ctrl117", auditSpSpSgxk.getStr("Ctrl117"));// 装修层数
     * dataJson.put("Ctrl119", auditSpSpSgxk.getStr("Ctrl119"));// 使用性质
     * dataJson.put("Ctrl121", auditSpSpSgxk.getStr("Ctrl121"));// 原有用途
     * dataJson.put("applycontent", getSelectItemNoEmptyList("申请内容",
     * auditSpSpSgxk.getStr("Ctrl48"))); // 申请内容 dataJson.put("applyreason",
     * getSelectItemNoEmptyList("申请理由", auditSpSpSgxk.getStr("Ctrl49"))); //
     * 申请理由 dataJson.put("fkdxsreason", getSelectItemNoEmptyList("防空地下室易地建设理由",
     * auditSpSpSgxk.getStr("Ctrl50"))); // 防空地下室易地建设理由
     * dataJson.put("fkdxsjmtj", getSelectItemNoEmptyList("防空地下室易地建设费减免条件",
     * auditSpSpSgxk.getStr("Ctrl51"))); // 防空地下室易地建设费减免条件
     * dataJson.put("areagt500", getSelectItemNoEmptyList("密集场所大于500",
     * auditSpSpSgxk.getStr("Ctrl75")));// 密集场所大于500 dataJson.put("areagt1000",
     * getSelectItemNoEmptyList("密集场所大于1000",
     * auditSpSpSgxk.getStr("Ctrl76")));// 密集场所大于1000 dataJson.put("areagt2500",
     * getSelectItemNoEmptyList("密集场所大于2500",
     * auditSpSpSgxk.getStr("Ctrl77")));// 密集场所大于2500
     * dataJson.put("areagt10000", getSelectItemNoEmptyList("密集场所大于10000",
     * auditSpSpSgxk.getStr("Ctrl78")));// 密集场所大于10000
     * dataJson.put("areagt15000", getSelectItemNoEmptyList("密集场所大于15000",
     * auditSpSpSgxk.getStr("Ctrl79")));// 密集场所大于15000
     * dataJson.put("areagt20000", getSelectItemNoEmptyList("密集场所大于20000",
     * auditSpSpSgxk.getStr("Ctrl80")));// 密集场所大于20000 //缺少 一条 Ctrl87
     * dataJson.put("areagtother", getSelectItemNoEmptyList("特殊工程",
     * auditSpSpSgxk.getStr("Ctrl91"))); // 特殊工程 dataJson.put("specialbuild",
     * getSelectItemNoEmptyList("特殊建筑", auditSpSpSgxk.getStr("Ctrl93"))); //
     * 特殊建筑 有异议 dataJson.put("Ctrl101", getSelectItemNoEmptyList("设置型式",
     * auditSpSpSgxk.getStr("Ctrl101"))); // 设置型式 dataJson.put("Ctrl102",
     * getSelectItemNoEmptyList("储存形式", auditSpSpSgxk.getStr("Ctrl102"))); //
     * 储存形式 dataJson.put("cgxz", getSelectItemNoEmptyList("储罐选择",
     * auditSpSpSgxk.getStr("Ctrl103"))); // 储罐选择 dataJson.put("zxxz",
     * getSelectItemNoEmptyList("装修工程", auditSpSpSgxk.getStr("Ctrl104"))); //
     * 装修工程 dataJson.put("zxbw", getSelectItemNoEmptyList("装修部位",
     * auditSpSpSgxk.getStr("Ctrl115"))); // 装修部位 dataJson.put("xfss",
     * getSelectItemNoEmptyList("消防设施种类", auditSpSpSgxk.getStr("Ctrl122"))); //
     * 消防设施种类
     *
     * } else { dataJson.put("projectLevel", getSelectItemList("项目等级", true));
     * // 重点项目 dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
     * dataJson.put("investTyped", getSelectItemList("投资类型", true));// 投资类型
     * dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
     * dataJson.put("areaSources", getSelectItemList("土地来源", true)); // 土地来源
     * dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
     * dataJson.put("buildType", getSelectItemList("项目类型", true)); // 项目类型
     * dataJson.put("corporationType", getSelectItemList("法人类型", true)); // 法人类型
     * dataJson.put("certType", getSelectItemList("身份证件类型", true)); // 身份证件类型
     * dataJson.put("Ctrl53", getSelectItemList("建设性质", false)); // 类别
     * dataJson.put("applycontent", getSelectItemList("申请内容", false)); // 申请内容
     * dataJson.put("applyreason", getSelectItemList("申请理由", false)); // 申请理由
     * dataJson.put("fkdxsreason", getSelectItemList("防空地下室易地建设理由", false)); //
     * 防空地下室易地建设理由 dataJson.put("fkdxsjmtj", getSelectItemList("防空地下室易地建设费减免条件",
     * false)); // 防空地下室易地建设费减免条件 dataJson.put("jzxfsjshlb",
     * getSelectItemList("建设工程消防设计审核类别", false)); // 建设工程消防设计审核类别
     * dataJson.put("areagt500", getSelectItemList("密集场所大于500", false)); //
     * 密集场所大于500 dataJson.put("areagt1000", getSelectItemList("密集场所大于1000",
     * false)); // 密集场所大于1000 dataJson.put("areagt2500",
     * getSelectItemList("密集场所大于2500", false)); // 密集场所大于2500
     * dataJson.put("areagt10000", getSelectItemList("密集场所大于10000", false)); //
     * 密集场所大于10000 dataJson.put("areagt15000", getSelectItemList("密集场所大于15000",
     * false)); // 密集场所大于15000 dataJson.put("areagt20000",
     * getSelectItemList("密集场所大于20000", false)); // 密集场所大于20000
     * dataJson.put("areagtother", getSelectItemList("特殊工程", false)); // 特殊工程
     * dataJson.put("specialbuild", getSelectItemList("特殊建筑", false)); // 特殊建筑
     * dataJson.put("Ctrl101", getSelectItemList("设置型式", false)); // 设置型式
     * dataJson.put("Ctrl102", getSelectItemList("储存形式", false)); // 储存形式
     * dataJson.put("cgxz", getSelectItemList("储罐选择", false)); // 储罐选择
     * dataJson.put("zxxz", getSelectItemList("装修工程", false)); // 装修工程
     * dataJson.put("zxbw", getSelectItemList("装修部位", false)); // 装修部位
     * dataJson.put("xfss", getSelectItemList("消防设施种类", false)); // 消防设施种类 if
     * (auditRsItemBaseinfo != null) { dataJson.put("itemname",
     * auditRsItemBaseinfo.getItemname()); // 项目名称 dataJson.put("itemcode",
     * auditRsItemBaseinfo.getItemcode()); dataJson.put("constructionsite",
     * auditRsItemBaseinfo.getConstructionsite());
     * dataJson.put("totalSocietyCode",
     * StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()); SqlConditionUtil
     * companySqlConditionUtil = new SqlConditionUtil();
     * companySqlConditionUtil.eq("creditcode",
     * StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());
     * companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
     * companySqlConditionUtil.eq("isactivated", "1");
     * List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos =
     * iAuditRsCompanyBaseinfo
     * .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap()
     * ) .getResult(); if (auditRsCompanyBaseinfos != null &&
     * auditRsCompanyBaseinfos.size() > 0) { AuditRsCompanyBaseinfo
     * auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
     * dataJson.put("corporationName", auditRsCompanyBaseinfo.getOrganlegal());
     * } } } return dataJson; }
     */

    /**
     * 加载第四阶段信息
     */
    /*
     * private JSONObject getJgysInfo(String subappguid, AuditRsItemBaseinfo
     * auditRsItemBaseinfo) { JSONObject dataJson = new JSONObject();
     * AuditSpSpJgys auditSpSpJgys =
     * iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid); if
     * (auditSpSpJgys != null) { dataJson.put("itemname",
     * auditSpSpJgys.getItemname());// 项目名称 dataJson.put("subappname",
     * auditSpSpJgys.getSubappname()); // 子申报名称 dataJson.put("itemcode",
     * auditSpSpJgys.getItemcode()); // 投资平台项目代码 dataJson.put("projectLevel",
     * getSelectItemList("项目等级", auditSpSpJgys.getProjectlevel())); // 重点项目
     * dataJson.put("itemAddress", auditSpSpJgys.getItemaddress()); // 项目地址
     * dataJson.put("area", auditSpSpJgys.getArea()); // 区（园区）
     * dataJson.put("road", auditSpSpJgys.getRoad()); // 街道
     * dataJson.put("eastToArea", auditSpSpJgys.getEasttoarea()); // 四至范围（东至）
     * dataJson.put("westToArea", auditSpSpJgys.getWesttoarea()); // 四至范围（西至）
     * dataJson.put("southToArea", auditSpSpJgys.getSouthtoarea()); // 四至范围（南至）
     * dataJson.put("northToArea", auditSpSpJgys.getNorthtoarea()); // 四至范围（北至）
     * dataJson.put("proTyped", getSelectItemList("立项类型",
     * auditSpSpJgys.getProtyped()));// 立项类型 dataJson.put("proOu",
     * auditSpSpJgys.getProou()); // 立项部门 dataJson.put("investTyped",
     * getSelectItemList("投资类型", auditSpSpJgys.getInvesttyped()));// 投资类型
     * dataJson.put("hangyeType", auditSpSpJgys.getHangyetype()); // 行业类别
     * dataJson.put("moneySources", getSelectItemList("资金来源",
     * auditSpSpJgys.getMoneysources())); // 资金来源 dataJson.put("allMoney",
     * auditSpSpJgys.getAllmoney()); // 总投资 dataJson.put("areaSources",
     * getSelectItemList("土地来源", auditSpSpJgys.getAreasources())); // 土地来源
     * dataJson.put("useLandType", auditSpSpJgys.getUselandtype()); // 规划用地性质
     * dataJson.put("areaUsed", auditSpSpJgys.getAreaused());// 用地面积(公顷)
     * dataJson.put("newLandType", auditSpSpJgys.getNewlandtype()); //
     * 新增用地面积（公顷） dataJson.put("buildProperties", getSelectItemList("建设性质",
     * auditSpSpJgys.getBuildproperties())); // 建设性质 dataJson.put("buildType",
     * getSelectItemList("项目类型", auditSpSpJgys.getBuildtype())); // 项目类型
     * dataJson.put("allBuildArea", auditSpSpJgys.getAllbuildarea()); //
     * 总建筑面积（m²） dataJson.put("overLoadArea", auditSpSpJgys.getOverloadarea());
     * // 地上 dataJson.put("downLoadArea", auditSpSpJgys.getDownloadarea()); //
     * 地下 dataJson.put("buildContent", auditSpSpJgys.getBuildcontent()); //
     * 主要建设内容和技术指标(包括必要性) dataJson.put("planStartDate",
     * StringUtil.isBlank(auditSpSpJgys.getPlanstartdate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanstartdate(),
     * "yyyy-MM-dd")); // 计划开工日期 dataJson.put("planEndDate",
     * StringUtil.isBlank(auditSpSpJgys.getPlanenddate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpJgys.getPlanenddate(),
     * "yyyy-MM-dd")); // 计划竣工日期 dataJson.put("gcmc", auditSpSpJgys.getGcmc());
     * // 工程名称 dataJson.put("lxjb", auditSpSpJgys.getLxjb()); // 立项级别
     * dataJson.put("jzgd", auditSpSpJgys.getJzgd()); // 建筑高度
     * dataJson.put("jclb", auditSpSpJgys.getJclb()); // 基础类别
     * dataJson.put("startDate",
     * StringUtil.isBlank(auditSpSpJgys.getStartdate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpJgys.getStartdate(),
     * "yyyy-MM-dd")); // 开工日期 dataJson.put("endDate",
     * StringUtil.isBlank(auditSpSpJgys.getEnddate()) ? "" :
     * EpointDateUtil.convertDate2String(auditSpSpJgys.getEnddate(),
     * "yyyy-MM-dd")); // 竣工日期 dataJson.put("bdh", auditSpSpJgys.getBdh()); //
     * 标段号 dataJson.put("jzzh", auditSpSpJgys.getJzzh()); // 桩号
     * dataJson.put("gcgm", auditSpSpJgys.getGcgm()); // 工程规模
     * dataJson.put("gczj", auditSpSpJgys.getGczj()); // 工程造价(万元)
     * dataJson.put("rfjzmj", auditSpSpJgys.getRfjzmj()); // 人防建筑面积
     * dataJson.put("zxmj", auditSpSpJgys.getZxmj()); // 装修面积
     * dataJson.put("dscc", auditSpSpJgys.getDscc()); // 地上层次
     * dataJson.put("dxcc", auditSpSpJgys.getDxcc()); // 地下层次
     * dataJson.put("nhdj", getSelectItemList("耐火等级", auditSpSpJgys.getNhdj()));
     * // 耐火等级 dataJson.put("fldj", getSelectItemList("防雷等级",
     * auditSpSpJgys.getFldj())); // 防雷等级 dataJson.put("gclb",
     * auditSpSpJgys.getGclb()); // 工程类别 dataJson.put("jglx",
     * auditSpSpJgys.getJglx()); // 结构类型 dataJson.put("sylx",
     * getSelectItemList("使用类型", auditSpSpJgys.getSylx())); // 使用类型 // 新增
     * dataJson.put("totalSocietyCode",
     * auditSpSpJgys.getStr("totalSocietyCode"));// 统一社会信用代码
     * dataJson.put("proUnit", auditSpSpJgys.getStr("proUnit"));// 项目单位
     * dataJson.put("corporationType", getSelectItemList("法人类型",
     * auditSpSpJgys.getStr("corporationType")));// 法人类型
     * dataJson.put("corporationName",
     * auditSpSpJgys.getStr("corporationName"));// 法定代表人
     * dataJson.put("telAddress", auditSpSpJgys.getStr("telAddress"));// 通讯地址
     * dataJson.put("postCode", auditSpSpJgys.getStr("postCode"));// 邮政编码
     * dataJson.put("projectPerson", auditSpSpJgys.getStr("projectPerson"));//
     * 项目负责人 dataJson.put("telNumber", auditSpSpJgys.getStr("telNumber"));//
     * 联系电话 dataJson.put("applicantName",
     * auditSpSpJgys.getStr("applicantName"));// 授权申报人
     * dataJson.put("appTelNumber", auditSpSpJgys.getStr("appTelNumber"));//
     * 联系电话 dataJson.put("certType", getSelectItemList("身份证件类型",
     * auditSpSpJgys.getStr("certType")));// 身份证件类型 dataJson.put("certNumber",
     * auditSpSpJgys.getStr("certNumber"));// 身份证件号码 // 第四阶段新增
     * dataJson.put("ghxkzh", auditSpSpJgys.getStr("ghxkzh")); // 规划许可证号
     * dataJson.put("sgxkzh", auditSpSpJgys.getStr("sgxkzh")); // 施工许可证号
     * dataJson.put("zmjcd", auditSpSpJgys.getStr("zmjcd")); // 总面积/长度（平方米/米）
     * dataJson.put("jdzch", auditSpSpJgys.getStr("jdzch")); // 监督注册号
     * dataJson.put("sjmjcd", auditSpSpJgys.getStr("sjmjcd")); // 实际面积/长度（平方米/米）
     * dataJson.put("jsdw", auditSpSpJgys.getStr("jsdw")); // 建设单位
     * dataJson.put("jsdwfzr", auditSpSpJgys.getStr("jsdwfzr")); // 项目负责人
     * dataJson.put("jsdwdh", auditSpSpJgys.getStr("jsdwdh")); // 联系电话
     * dataJson.put("kcdw", auditSpSpJgys.getStr("kcdw")); // 勘察单位
     * dataJson.put("kcdwfzr", auditSpSpJgys.getStr("kcdwfzr")); // 项目负责人
     * dataJson.put("kcdwdh", auditSpSpJgys.getStr("kcdwdh")); // 联系电话
     * dataJson.put("sjdw", auditSpSpJgys.getStr("sjdw")); // 设计单位
     * dataJson.put("sjdwfzr", auditSpSpJgys.getStr("sjdwfzr")); // 项目负责人
     * dataJson.put("sjdwdh", auditSpSpJgys.getStr("sjdwdh")); // 联系电话
     * dataJson.put("sgdw", auditSpSpJgys.getStr("sgdw")); // 施工单位
     * dataJson.put("sgdwfzr", auditSpSpJgys.getStr("sgdwfzr")); // 项目负责人
     * dataJson.put("sgdwdh", auditSpSpJgys.getStr("sgdwdh")); // 联系电话
     * dataJson.put("jldw", auditSpSpJgys.getStr("jldw")); // 监理单位
     * dataJson.put("jldwfzr", auditSpSpJgys.getStr("jldwfzr")); // 项目负责人
     * dataJson.put("jldwdh", auditSpSpJgys.getStr("jldwdh")); // 联系电话 } else {
     * dataJson.put("projectLevel", getSelectItemList("项目等级", true)); // 重点项目
     * dataJson.put("proTyped", getSelectItemList("立项类型", true));// 立项类型
     * dataJson.put("investTyped", getSelectItemList("投资类型", true));// 投资类型
     * dataJson.put("moneySources", getSelectItemList("资金来源", true)); // 资金来源
     * dataJson.put("areaSources", getSelectItemList("土地来源", true)); // 土地来源
     * dataJson.put("buildProperties", getSelectItemList("建设性质", true)); // 建设性质
     * dataJson.put("buildType", getSelectItemList("项目类型", true)); // 项目类型
     * dataJson.put("nhdj", getSelectItemList("耐火等级", true)); // 耐火等级
     * dataJson.put("fldj", getSelectItemList("防雷等级", true)); // 防雷等级
     * dataJson.put("sylx", getSelectItemList("使用类型", true)); // 使用类型
     * dataJson.put("corporationType", getSelectItemList("法人类型", true)); // 法人类型
     * dataJson.put("certType", getSelectItemList("身份证件类型", true)); // 身份证件类型 if
     * (auditRsItemBaseinfo != null) { dataJson.put("itemname",
     * auditRsItemBaseinfo.getItemname()); // 项目名称 dataJson.put("itemcode",
     * auditRsItemBaseinfo.getItemcode()); dataJson.put("constructionsite",
     * auditRsItemBaseinfo.getConstructionsite());
     * dataJson.put("totalSocietyCode",
     * StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()); SqlConditionUtil
     * companySqlConditionUtil = new SqlConditionUtil();
     * companySqlConditionUtil.eq("creditcode",
     * StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());
     * companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
     * companySqlConditionUtil.eq("isactivated", "1");
     * List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos =
     * iAuditRsCompanyBaseinfo
     * .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap()
     * ) .getResult(); if (auditRsCompanyBaseinfos != null &&
     * auditRsCompanyBaseinfos.size() > 0) { AuditRsCompanyBaseinfo
     * auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
     * dataJson.put("corporationName", auditRsCompanyBaseinfo.getOrganlegal());
     * } } } return dataJson; }
     */

    // 保存第一阶段表单
    private void saveLxydghxkInfo(JSONObject param, AuditOnlineRegister auditOnlineRegister) {
        String type = param.getString("type");// 1-保存 2-提交
        String biguid = param.getString("biguid"); // 主题实例guid
        String businessguid = param.getString("businessguid");// 主题guid
        String phaseguid = param.getString("phaseguid");// 阶段guid
        String subappguid = param.getString("subappguid");// 申报唯一标识
        String itemguid = param.getString("itemguid");// 项目guid
        String itemname = param.getString("itemname");// 项目名称
        String subappname = param.getString("subappname");// 子申报名称
        String itemcode = param.getString("itemcode");// 投资平台项目代码
        String projectLevel = param.getString("projectLevel");// 重点项目（代码项）
        String itemAddress = param.getString("itemAddress");// 项目地址
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
        String buildProperty = param.getString("buildProperty");// 建设性质
        String buildType = param.getString("buildType");// 项目类型
        Double allBuildArea = param.getDouble("allBuildArea");// 总建筑面积（M2）
        Double overLoadArea = param.getDouble("overLoadArea");// 地上（M2）
        Double downLoadArea = param.getDouble("downLoadArea");// 地下（M2）
        String buildContent = param.getString("buildContent");// 建设内容
        Date planStartDate = param.getDate("planStartDate");// 计划开工日期
        Date planEndDate = param.getDate("planEndDate");// 计划竣工日期
        // --------------------- 新增开始 -------------------------------
        String totalSocietyCode = param.getString("totalSocietyCode");// 统一社会信用代码
        String proUnit = param.getString("proUnit");// 项目单位
        String corporationType = param.getString("corporationType");// 法人类型
        String corporationName = param.getString("corporationName");// 法定代表人
        String telAddress = param.getString("telAddress");// 通讯地址
        String postCode = param.getString("postCode");// 邮政编码
        String projectPerson = param.getString("projectPerson");// 项目负责人
        String telNumber = param.getString("telNumber");// 联系电话
        String applicantName = param.getString("applicantName");// 授权申报人
        String appTelNumber = param.getString("appTelNumber");// 联系电话
        String certType = param.getString("certType");// 身份证件类型
        String certNumber = param.getString("certNumber");// 身份证件号码

        String itemSource = param.getString("itemSource");// 项目来源
        String zcxwj = param.getString("zcxwj");
        Double xznxfl = param.getDouble("xznxfl");// 新增年综合能源消费量吨标煤（当量值）
        Double xznydl = param.getDouble("xznydl");// 新增年用电量（万千瓦时）
        // ===================== 第一阶段> 新增开始 ===============================
        String standProType = param.getString("standProType");// 立项类型
        String projectType = param.getString("projectType");// 项目类型
        String zcaddress = param.getString("zcaddress");// 注册地点
        String registeMoney = param.getString("registeMoney");// 注册资金（万美元）
        String gbgc = param.getString("gbgc");// 股比构成 （%）
        String jksbyh = param.getString("jksbyh");// 进口设备用汇（万美元）
        String newRegisteMoney = param.getString("newRegisteMoney");// 新增注册资本(万美元)
        String investCountry1 = param.getString("investCountry1");// 投资方（国别）
        String country1Per = param.getString("country1Per");// 占比（%）
        String investCountry2 = param.getString("investCountry2");// 投资方（国别）
        String country2Per = param.getString("country2Per");// 占比（%）
        String investCountry3 = param.getString("investCountry3");// 投资方（国别）
        String country3Per = param.getString("country3Per");// 占比（%）
        // ===================== 第一阶段> 新增结束 ===============================
        // 更新表单信息
        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid)
                .getResult();
        boolean addFlag = false; // 默认为更新
        if (auditRsItemBaseinfo == null) {
            auditRsItemBaseinfo = new AuditRsItemBaseinfo();
            auditRsItemBaseinfo.setRowguid(UUID.randomUUID().toString());
            addFlag = true; // 新增
        }
        auditRsItemBaseinfo.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditRsItemBaseinfo.setOperatedate(new Date());
        auditRsItemBaseinfo.setItemname(itemname);
        auditRsItemBaseinfo.setItemcode(itemcode);
        auditRsItemBaseinfo.setXmtzly(Integer.valueOf(investTyped));
        auditRsItemBaseinfo.setFundsources(moneySources);
        auditRsItemBaseinfo.setTotalinvest(allMoney);
        auditRsItemBaseinfo.setTdhqfs(Integer.valueOf(areaSources));
        auditRsItemBaseinfo.setLandarea(areaUsed);
        auditRsItemBaseinfo.setNewlandarea(newLandType);
        auditRsItemBaseinfo.setConstructionproperty(buildProperty);
        auditRsItemBaseinfo.setItemtype(buildType);
        auditRsItemBaseinfo.setJzmj(allBuildArea);
        auditRsItemBaseinfo.setConstructionscaleanddesc(buildContent);
        auditRsItemBaseinfo.setItemstartdate(planStartDate);
        auditRsItemBaseinfo.setItemfinishdate(planEndDate);
        if (addFlag) {
            iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
        } else {
            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
        }
        AuditRsItemBaseinfoOne auditRsItemBaseinfoOne = iAuditRsItemBaseinfoOneService
                .getAuditRsItemBaseinfoOneByparentidandpaseguid(biguid);
        boolean addFlagOne = false; // 默认为更新
        if (auditRsItemBaseinfoOne == null) {
            auditRsItemBaseinfoOne = new AuditRsItemBaseinfoOne();
            auditRsItemBaseinfoOne.setRowguid(UUID.randomUUID().toString());
            addFlagOne = true; // 新增
        }
        auditRsItemBaseinfoOne.setParentid(biguid);
        auditRsItemBaseinfoOne.set("subappguid", subappguid);
        auditRsItemBaseinfoOne.setZdxm(projectLevel);
        auditRsItemBaseinfoOne.setXmdz(itemAddress);// 项目地址
        auditRsItemBaseinfoOne.setDz(eastToArea);
        auditRsItemBaseinfoOne.setXz(westToArea);
        auditRsItemBaseinfoOne.setNz(southToArea);
        auditRsItemBaseinfoOne.setBz(northToArea);
        auditRsItemBaseinfoOne.setLxlx(proTyped);// 立项类型
        auditRsItemBaseinfoOne.setLxbm(proOu);// 立项部门
        auditRsItemBaseinfoOne.setHylb(hangyeType);
        auditRsItemBaseinfoOne.setGhydxzjxz(useLandType);
        auditRsItemBaseinfoOne.setDsjzmj(String.valueOf(overLoadArea));// 地上建筑面积
        auditRsItemBaseinfoOne.setDxjzmj(String.valueOf(downLoadArea));
        auditRsItemBaseinfoOne.setZcxwj(zcxwj);
        auditRsItemBaseinfoOne.setXznzhnyxh(String.valueOf(xznxfl));
        auditRsItemBaseinfoOne.setXznydl(String.valueOf(xznydl));
        auditRsItemBaseinfoOne.setSubappname(subappname);
        if (addFlagOne) {
            iAuditRsItemBaseinfoOneService.insert(auditRsItemBaseinfoOne);
        } else {
            iAuditRsItemBaseinfoOneService.update(auditRsItemBaseinfoOne);
        }
        ParticipantsInfo participantsInfo = iParticipantsInfoService.findParticipantsInfoByItemGuid(itemguid);
        // ------新增字段保存-------
        boolean addFlagpart = false; // 默认为更新
        if (participantsInfo == null) {
            participantsInfo = new ParticipantsInfo();
            participantsInfo.setRowguid(UUID.randomUUID().toString());
            participantsInfo.setCorptype("31");
            addFlagpart = true; // 新增
        }
        participantsInfo.setCorpcode(totalSocietyCode);// 统一社会信用代码
        participantsInfo.setCorpname(proUnit);// 项目单位
        participantsInfo.setLegalproperty(corporationType);// 法人类型
        participantsInfo.setLegal(corporationName);// 法定代表人
        participantsInfo.setAddress(telAddress);// 通讯地址
        participantsInfo.set("zipcode", postCode);// 邮政编码
        participantsInfo.setXmfzr(projectPerson);// 项目负责人
        participantsInfo.setXmfzr_phone(telNumber);// 联系电话
        participantsInfo.setDanweilxr(applicantName);// 授权申报人
        participantsInfo.setDanweilxrlxdh(appTelNumber);// 联系电话
        // participantsInfo.set("certType", certType);// 身份证件类型
        participantsInfo.setXmfzr_idcard(certNumber);// 身份证件号码
        if (addFlagpart) {
            iParticipantsInfoService.insert(participantsInfo);
        } else {
            iParticipantsInfoService.update(participantsInfo);
        }
        AuditRsItemBaseinfoOneWscompany auditRsItemBaseinfoOneWscompany = iAuditRsItemBaseinfoOneWscompanyService
                .getAuditRsItemBaseinfoOneWscompanyByparentidandpaseguid(biguid, subappguid);
        boolean addFlagws = false; // 默认为更新
        if (auditRsItemBaseinfoOneWscompany == null) {
            auditRsItemBaseinfoOneWscompany = new AuditRsItemBaseinfoOneWscompany();
            auditRsItemBaseinfoOneWscompany.setRowguid(UUID.randomUUID().toString());
            addFlagws = true; // 新增
        }
        auditRsItemBaseinfoOneWscompany.setParentid(biguid);
        auditRsItemBaseinfoOneWscompany.setPhaseguid(phaseguid);
        auditRsItemBaseinfoOneWscompany.set("subappguid", subappguid);
        auditRsItemBaseinfoOneWscompany.setZcdd(zcaddress);// 注册地点
        auditRsItemBaseinfoOneWscompany.setZcdd(registeMoney);// 注册资金（万美元）
        auditRsItemBaseinfoOneWscompany.setTzfj(investCountry1);// 投资方（国别）
        auditRsItemBaseinfoOneWscompany.setTzfjzb(country1Per);// 占比（%）
        auditRsItemBaseinfoOneWscompany.setTzfy(investCountry2);// 投资方（国别）
        auditRsItemBaseinfoOneWscompany.setTzfyzb(country2Per);// 占比（%）
        auditRsItemBaseinfoOneWscompany.setTzfb(investCountry3);// 投资方（国别）
        auditRsItemBaseinfoOneWscompany.setTzfbzb(country3Per);// 占比（%）
        /*
         * if (addFlagws) { iAuditRsItemBaseinfoOneWscompanyService.insert(
         * auditRsItemBaseinfoOneWscompany); } else {
         * iAuditRsItemBaseinfoOneWscompanyService.update(
         * auditRsItemBaseinfoOneWscompany); }
         */
        if (!addFlagws) {
            iAuditRsItemBaseinfoOneWscompanyService.update(auditRsItemBaseinfoOneWscompany);
        }

        // 保存子申报
        setAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, auditOnlineRegister);
    }

    // 保存第二阶段表单
    private void saveGcjsxkInfo(JSONObject param, AuditOnlineRegister auditOnlineRegister) {
        String type = param.getString("type");// 1-保存 2-提交
        String biguid = param.getString("biguid"); // 主题实例guid
        String businessguid = param.getString("businessguid");// 主题guid
        String phaseguid = param.getString("phaseguid");// 阶段guid
        String subappguid = param.getString("subappguid");// 申报唯一标识
        String itemguid = param.getString("itemguid");// 项目guid
        String itemname = param.getString("itemname");// 项目名称
        String subappname = param.getString("subappname");// 子申报名称
        String itemcode = param.getString("itemcode");// 投资平台项目代码
        String projectLevel = param.getString("projectLevel");// 重点项目（代码项）
        String itemAddress = param.getString("itemAddress");// 项目地址
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
        String buildProperty = param.getString("buildProperty");// 建设性质
        String buildType = param.getString("buildType");// 项目类型
        Double allBuildArea = param.getDouble("allBuildArea");// 总建筑面积（M2）
        Double overLoadArea = param.getDouble("overLoadArea");// 地上（M2）
        Double downLoadArea = param.getDouble("downLoadArea");// 地下（M2）
        String buildContent = param.getString("buildContent");// 建设内容
        Date planStartDate = param.getDate("planStartDate");// 计划开工日期
        Date planEndDate = param.getDate("planEndDate");// 计划竣工日期
        // --------------------- 新增开始 -------------------------------
        String totalSocietyCode = param.getString("totalSocietyCode");// 统一社会信用代码
        String proUnit = param.getString("proUnit");// 项目单位
        String corporationType = param.getString("corporationType");// 法人类型
        String corporationName = param.getString("corporationName");// 法定代表人
        String telAddress = param.getString("telAddress");// 通讯地址
        String postCode = param.getString("postCode");// 邮政编码
        String projectPerson = param.getString("projectPerson");// 项目负责人
        String telNumber = param.getString("telNumber");// 联系电话
        String applicantName = param.getString("applicantName");// 授权申报人
        String appTelNumber = param.getString("appTelNumber");// 联系电话
        String certType = param.getString("certType");// 身份证件类型
        String certNumber = param.getString("certNumber");// 身份证件号码

        String itemSource = param.getString("itemSource");// 项目来源
        // ------------------- 第二阶段表单新增开始 --------------------
        String ldmj = param.getString("ldmj");// 绿地面积
        String ldl = param.getString("ldl"); // 绿地率
        String qsfs = param.getString("qsfs"); // 取水方式
        String tsfs = param.getString("tsfs"); // 退水方式
        String qsl = param.getString("qsl"); // 取水量
        String qslx = param.getString("qslx"); // 取水类型
        String dyqk = param.getString("dyqk"); // 电源情况
        String trqk = param.getString("trqk"); // 土壤情况
        String flth = param.getString("flth"); // 防雷图号
        String jglx = param.getString("jglx"); // 结构类型
        String zbdljg = param.getString("zbdljg");// 招标代理机构
        String gczjzxqy = param.getString("gczjzxqy");// 工程造价咨询企业
        // ------------------- 第二阶段表单新增结束 --------------------
        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid)
                .getResult();
        boolean addFlag = false; // 默认为更新
        if (auditRsItemBaseinfo == null) {
            auditRsItemBaseinfo = new AuditRsItemBaseinfo();
            auditRsItemBaseinfo.setRowguid(UUID.randomUUID().toString());
            addFlag = true; // 新增
        }
        auditRsItemBaseinfo.setOperateusername(ZwdtUserSession.getInstance("").getClientName());
        auditRsItemBaseinfo.setOperatedate(new Date());
        auditRsItemBaseinfo.setItemname(itemname);
        auditRsItemBaseinfo.setItemcode(itemcode);
        auditRsItemBaseinfo.setXmtzly(Integer.valueOf(investTyped));
        auditRsItemBaseinfo.setFundsources(moneySources);
        auditRsItemBaseinfo.setTotalinvest(allMoney);
        auditRsItemBaseinfo.setTdhqfs(Integer.valueOf(areaSources));
        auditRsItemBaseinfo.setLandarea(areaUsed);
        auditRsItemBaseinfo.setNewlandarea(newLandType);
        auditRsItemBaseinfo.setConstructionproperty(buildProperty);
        auditRsItemBaseinfo.setItemtype(buildType);
        auditRsItemBaseinfo.setJzmj(allBuildArea);
        auditRsItemBaseinfo.setConstructionscaleanddesc(buildContent);
        auditRsItemBaseinfo.setItemstartdate(planStartDate);
        auditRsItemBaseinfo.setItemfinishdate(planEndDate);
        if (addFlag) {
            iAuditRsItemBaseinfo.addAuditRsItemBaseinfo(auditRsItemBaseinfo);
        } else {
            iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
        }
        AuditRsItemBaseinfoOne auditRsItemBaseinfoOne = iAuditRsItemBaseinfoOneService
                .getAuditRsItemBaseinfoOneByparentidandpaseguid(biguid);
        boolean addFlagOne = false; // 默认为更新
        if (auditRsItemBaseinfoOne == null) {
            auditRsItemBaseinfoOne = new AuditRsItemBaseinfoOne();
            auditRsItemBaseinfoOne.setRowguid(UUID.randomUUID().toString());
            addFlagOne = true; // 新增
        }
        auditRsItemBaseinfoOne.setParentid(biguid);
        auditRsItemBaseinfoOne.set("subappguid", subappguid);
        auditRsItemBaseinfoOne.setZdxm(projectLevel);
        auditRsItemBaseinfoOne.setXmdz(itemAddress);// 项目地址
        auditRsItemBaseinfoOne.setDz(eastToArea);
        auditRsItemBaseinfoOne.setXz(westToArea);
        auditRsItemBaseinfoOne.setNz(southToArea);
        auditRsItemBaseinfoOne.setBz(northToArea);
        auditRsItemBaseinfoOne.setLxlx(proTyped);// 立项类型
        auditRsItemBaseinfoOne.setLxbm(proOu);// 立项部门
        auditRsItemBaseinfoOne.setHylb(hangyeType);
        auditRsItemBaseinfoOne.setGhydxzjxz(useLandType);
        auditRsItemBaseinfoOne.setDsjzmj(String.valueOf(overLoadArea));// 地上建筑面积
        auditRsItemBaseinfoOne.setDxjzmj(String.valueOf(downLoadArea));
        auditRsItemBaseinfoOne.setSubappname(subappname);
        if (addFlagOne) {
            iAuditRsItemBaseinfoOneService.insert(auditRsItemBaseinfoOne);
        } else {
            iAuditRsItemBaseinfoOneService.update(auditRsItemBaseinfoOne);
        }
        ParticipantsInfo participantsInfo = iParticipantsInfoService.findParticipantsInfoByItemGuid(itemguid);
        // ------新增字段保存-------
        boolean addFlagpart = false; // 默认为更新
        if (participantsInfo == null) {
            participantsInfo = new ParticipantsInfo();
            participantsInfo.setRowguid(UUID.randomUUID().toString());
            participantsInfo.setCorptype("31");
            addFlagpart = true; // 新增
        }
        participantsInfo.setCorpcode(totalSocietyCode);// 统一社会信用代码
        participantsInfo.setCorpname(proUnit);// 项目单位
        participantsInfo.setLegalproperty(corporationType);// 法人类型
        participantsInfo.setLegal(corporationName);// 法定代表人
        participantsInfo.setAddress(telAddress);// 通讯地址
        participantsInfo.set("zipcode", postCode);// 邮政编码
        participantsInfo.setXmfzr(projectPerson);// 项目负责人
        participantsInfo.setXmfzr_phone(telNumber);// 联系电话
        participantsInfo.setDanweilxr(applicantName);// 授权申报人
        participantsInfo.setDanweilxrlxdh(appTelNumber);// 联系电话
        // participantsInfo.set("certType", certType);// 身份证件类型
        participantsInfo.setXmfzr_idcard(certNumber);// 身份证件号码
        if (addFlagpart) {
            iParticipantsInfoService.insert(participantsInfo);
        } else {
            iParticipantsInfoService.update(participantsInfo);
        }
        AuditRsItemBaseinfoTwo auditRsItemBaseinfoTwo = iAuditRsItemBaseinfoTwoService
                .getAuditRsItemBaseinfoTwoByparentidandpaseguid(biguid, subappguid);
        boolean addFlagws = false; // 默认为更新
        if (auditRsItemBaseinfoTwo == null) {
            auditRsItemBaseinfoTwo = new AuditRsItemBaseinfoTwo();
            auditRsItemBaseinfoTwo.setRowguid(UUID.randomUUID().toString());
            addFlagws = true; // 新增
        }
        auditRsItemBaseinfoTwo.setParentid(biguid);
        auditRsItemBaseinfoTwo.setPhaseguid(phaseguid);
        auditRsItemBaseinfoTwo.set("subappguid", subappguid);
        auditRsItemBaseinfoTwo.setLdmj(ldmj);// 绿地面积
        auditRsItemBaseinfoTwo.setLdl(ldl);// 绿地率
        auditRsItemBaseinfoTwo.setQsfs(qsfs);// 取水方式
        auditRsItemBaseinfoTwo.setTsfs(tsfs);// 退水方式
        auditRsItemBaseinfoTwo.setQsl(qsl);// 取水量
        auditRsItemBaseinfoTwo.setQslx(qslx);// 取水类型
        auditRsItemBaseinfoTwo.setDyqk(dyqk);// 电源情况
        auditRsItemBaseinfoTwo.setTrqk(trqk);// 土壤情况
        auditRsItemBaseinfoTwo.setFlth(flth);// 防雷图号
        auditRsItemBaseinfoTwo.setJglx(jglx);// 结构类型
        auditRsItemBaseinfoTwo.setZbdljg(zbdljg);// 招标代理机构
        auditRsItemBaseinfoTwo.setGczjzxqy(gczjzxqy);// 工程造价咨询企业
        /*
         * if (addFlagws) { iAuditRsItemBaseinfoOneWscompanyService.insert(
         * auditRsItemBaseinfoOneWscompany); } else {
         * iAuditRsItemBaseinfoOneWscompanyService.update(
         * auditRsItemBaseinfoOneWscompany); }
         */
        if (!addFlagws) {
            iAuditRsItemBaseinfoTwoService.update(auditRsItemBaseinfoTwo);
        }

        // 保存子申报
        setAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid, subappname, type, auditOnlineRegister);
    }

    // 保存第三阶段表单
    /*
     * private void saveSgxkInfo(JSONObject param) { String type =
     * param.getString("type");// 1-保存 2-提交 String biguid =
     * param.getString("biguid"); // 主题实例guid String businessguid =
     * param.getString("businessguid");// 主题guid String phaseguid =
     * param.getString("phaseguid");// 阶段guid String subappguid =
     * param.getString("subappguid");// 申报唯一标识 String itemguid =
     * param.getString("itemguid");// 项目guid String itemname =
     * param.getString("itemname");// 项目名称 String subappname =
     * param.getString("subappname");// 子申报名称 String itemcode =
     * param.getString("itemcode");// 投资平台项目代码 String projectLevel =
     * param.getString("projectLevel");// 重点项目（代码项） String itemAddress =
     * param.getString("itemAddress");// 项目地址 String area =
     * param.getString("area");// 区（园区） String road = param.getString("road");//
     * 街道 String eastToArea = param.getString("eastToArea");// 四至范围（东至） String
     * westToArea = param.getString("westToArea");// 四至范围（西至） String southToArea
     * = param.getString("southToArea");// 四至范围（南至） String northToArea =
     * param.getString("northToArea");// 四至范围（北至） String proTyped =
     * param.getString("proTyped");// 立项类型（代码项） String proOu =
     * param.getString("proOu");// 立项部门（代码项） String investTyped =
     * param.getString("investTyped");// 投资类型（代码项） String hangyeType =
     * param.getString("hangyeType");// 行业类别（代码项） String moneySources =
     * param.getString("moneySources");// 资金来源（代码项） Double allMoney =
     * param.getDouble("allMoney");// 行业类别 String areaSources =
     * param.getString("areaSources");// 土地来源 String useLandType =
     * param.getString("useLandType");// 规划用地性质 Double areaUsed =
     * param.getDouble("areaUsed");// 用地面积（公顷） Double newLandType =
     * param.getDouble("newLandType");// 新增用地面积（公顷） String buildType =
     * param.getString("buildType");// 项目类型 Double allBuildArea =
     * param.getDouble("allBuildArea");// 总建筑面积（M2） Double overLoadArea =
     * param.getDouble("overLoadArea");// 地上（M2） Double downLoadArea =
     * param.getDouble("downLoadArea");// 地下（M2） String buildContent =
     * param.getString("buildContent");// 建设内容 Date planStartDate =
     * param.getDate("planStartDate");// 计划开工日期 Date planEndDate =
     * param.getDate("planEndDate");// 计划竣工日期
     *
     * String totalSocietyCode = param.getString("totalSocietyCode");// 统一社会信用代码
     * String proUnit = param.getString("proUnit");// 项目单位 String
     * corporationType = param.getString("corporationType");// 法人类型 String
     * corporationName = param.getString("corporationName");// 法定代表人 String
     * telAddress = param.getString("telAddress");// 通讯地址 String postCode =
     * param.getString("postCode");// 邮政编码 String projectPerson =
     * param.getString("projectPerson");// 项目负责人 String telNumber =
     * param.getString("telNumber");// 联系电话 String applicantName =
     * param.getString("applicantName");// 授权申报人 String appTelNumber =
     * param.getString("appTelNumber");// 联系电话 String certType =
     * param.getString("certType");// 身份证件类型 String certNumber =
     * param.getString("certNumber");// 身份证件号码
     *
     * String lxwh = param.getString("lxwh");// 立项文号 Date lxrq =
     * param.getDate("lxrq");// 立项日期 String lxjb = param.getString("lxjb");//
     * 立项级别 String xmbm = param.getString("xmbm");// 项目编码 String gcyt =
     * param.getString("gcyt");// 工程用途 String gcfl = param.getString("gcfl");//
     * 工程分类 String zftzgs = param.getString("zftzgs");// 政府投资概算 String ghyzzpl =
     * param.getString("ghyzzpl");// 规划预制装配率 String stwcrq =
     * param.getString("stwcrq"); // 审图完成日期 String zbfs =
     * param.getString("zbfs"); // 招标方式 // String gcyt =
     * param.getString("gcyt"); // 工程用途 String zbrq = param.getString("zbrq");
     * // 中标日期 String zbje = param.getString("zbje"); // 中标金额 String jgxs =
     * param.getString("jgxs"); // 结构形式 String jksd = param.getString("jksd");
     * // 基坑深度 String htlb = param.getString("htlb"); // 合同类别 String Ctrl20 =
     * param.getString("Ctrl20"); // 合同签订日期 String Ctrl21 =
     * param.getString("Ctrl21"); // 合同工期 String htkgsj =
     * param.getString("htkgsj"); // 合同开工时间 String htjgsj =
     * param.getString("htjgsj"); // 合同竣工时间 String htje =
     * param.getString("htje"); // 合同金额（万元） String tdzbh =
     * param.getString("tdzbh"); // 土地证编号 String jsgcghxkzbh =
     * param.getString("jsgcghxkzbh"); // 建设工程规划许可证编号 String kcbmxydm =
     * param.getString("kcbmxydm"); // 勘察部门信用代码 String kcbmmc =
     * param.getString("kcbmmc"); // 勘察部门名称 String kcbmfzr =
     * param.getString("kcbmfzr"); // 勘察部门负责人 String kcbmtel =
     * param.getString("kcbmtel"); // 勘察部门电话 String sjdwxydm =
     * param.getString("sjdwxydm"); // 设计单位信用代码 String sjdwmc =
     * param.getString("sjdwmc"); // 设计单位名称 String sjdwfzr =
     * param.getString("sjdwfzr"); // 设计单位负责人 String sjdwtel =
     * param.getString("sjdwtel"); // 设计单位电话 String sgcbdwxydm =
     * param.getString("sgcbdwxydm"); // 施工总承包单位信用代码 String sgcbdwmc =
     * param.getString("sgcbdwmc"); // 施工总承包单位名称 String sgcbdwfzr =
     * param.getString("sgcbdwfzr"); // 施工总承包单位负责人 String sgcbdwtel =
     * param.getString("sgcbdwtel"); // 施工总承包单位电话 String jldwxydm =
     * param.getString("jldwxydm"); // 监理单位信用代码 String jldwmc =
     * param.getString("jldwmc"); // 监理单位名称 String jldwfzr =
     * param.getString("jldwfzr"); // 监理单位负责人 String jldwtel =
     * param.getString("jldwtel"); // 监理单位电话 String tsjgxydm =
     * param.getString("tsjgxydm"); // 图审机构信用代码 String tsjgmc =
     * param.getString("tsjgmc"); // 图审机构名称 String tsjgfzr =
     * param.getString("tsjgfzr"); // 图审机构负责人 String tsjgtel =
     * param.getString("tsjgtel"); // 图审机构电话
     *
     * String Ctrl105 = param.getString("Ctrl105"); // 设置位置 String Ctrl107 =
     * param.getString("Ctrl107"); // 总容量(平方米) String Ctrl101 =
     * param.getString("Ctrl101"); // 设置形式 String Ctrl102 =
     * param.getString("Ctrl102"); // 储存形式 String Ctrl108 =
     * param.getString("Ctrl108"); // 储存物质名称 String Ctrl106 =
     * param.getString("Ctrl106"); // 储量 String Ctrl110 =
     * param.getString("Ctrl110"); // 储存物质名称 String Ctrl103 =
     * param.getString("Ctrl103"); // 选择 String Ctrl111 =
     * param.getString("Ctrl111"); // 材料类别 String Ctrl113 =
     * param.getString("Ctrl113"); // 保温层数 String Ctrl112 =
     * param.getString("Ctrl112"); // 使用性质 String Ctrl114 =
     * param.getString("Ctrl114"); // 原有用途 String Ctrl104 =
     * param.getString("Ctrl104"); // 选择 装修工程 String Ctrl115 =
     * param.getString("Ctrl115"); // 装修部位 String Ctrl116 =
     * param.getString("Ctrl116"); // 装修面积 String Ctrl117 =
     * param.getString("Ctrl117"); // 装修层数 String Ctrl119 =
     * param.getString("Ctrl119"); // 使用性质 String Ctrl121 =
     * param.getString("Ctrl121"); // 原有用途 String Ctrl122 =
     * param.getString("Ctrl122"); // 消防设施
     *
     * String Ctrl48 = param.getString("Ctrl48"); // 申请内容 String Ctrl49 =
     * param.getString("Ctrl49"); // 申请理由 String Ctrl50 =
     * param.getString("Ctrl50"); // 防空地下室易地建设理由 String Ctrl51 =
     * param.getString("Ctrl51"); // 防空地下室易地建设费减免条件 String Ctrl53 =
     * param.getString("Ctrl53"); // 类别 String Ctrl54 =
     * param.getString("Ctrl54"); // 类别 String Ctrl57 =
     * param.getString("Ctrl57"); // 设计单位名称 String Ctrl58 =
     * param.getString("Ctrl58"); // 设计单位等级 String Ctrl59 =
     * param.getString("Ctrl59"); // 设计单位法人 String Ctrl60 =
     * param.getString("Ctrl60"); // 设计单位联系人 String Ctrl62 =
     * param.getString("Ctrl62"); // 设计单位电话 String Ctrl63 =
     * param.getString("Ctrl63"); // 施工单位名称 String Ctrl64 =
     * param.getString("Ctrl64"); // 施工单位等级 String Ctrl65 =
     * param.getString("Ctrl65"); // 施工单位法人 String Ctrl66 =
     * param.getString("Ctrl66"); // 施工单位联系人 String Ctrl67 =
     * param.getString("Ctrl67"); // 施工单位电话 String Ctrl69 =
     * param.getString("Ctrl69"); // 监理单位名称 String Ctrl70 =
     * param.getString("Ctrl70"); // 监理单位等级 String Ctrl71 =
     * param.getString("Ctrl71"); // 监理单位法人 String Ctrl72 =
     * param.getString("Ctrl72"); // 监理单位联系人 String Ctrl74 =
     * param.getString("Ctrl74"); // 监理单位电话 String Ctrl75 =
     * param.getString("Ctrl75"); // 建筑总面积大于500m2的 String Ctrl76 =
     * param.getString("Ctrl76"); // 建筑总面积大于1000m2的 String Ctrl77 =
     * param.getString("Ctrl77"); // 建筑总面积大于2500m2的 String Ctrl78 =
     * param.getString("Ctrl78"); // 建筑总面积大于10000m2的 String Ctrl79 =
     * param.getString("Ctrl79"); // 建筑总面积大于15000m2的 String Ctrl80 =
     * param.getString("Ctrl80"); // 建筑总面积大于500m2的 String Ctrl91 =
     * param.getString("Ctrl91"); // 其他特殊工程 String Ctrl93 =
     * param.getString("Ctrl93"); // 除以上两项以外的
     *
     * String subTable1 = param.getString("subtable1"); String subTable2 =
     * param.getString("subtable2"); String subTable3 =
     * param.getString("subtable3");
     *
     * // 更新表单信息 AuditSpSpSgxk auditSpSpSgxk =
     * iAuditSpSpSgxkService.findAuditSpSpSgxkBysubappguid(subappguid); boolean
     * addFlag = false; // 默认为更新 if (auditSpSpSgxk == null) { auditSpSpSgxk =
     * new AuditSpSpSgxk();
     * auditSpSpSgxk.setRowguid(UUID.randomUUID().toString());
     * auditSpSpSgxk.setSubappguid(subappguid); addFlag = true; // 新增 }
     * auditSpSpSgxk.setOperateusername(ZwdtUserSession.getInstance("").
     * getClientName()); auditSpSpSgxk.setOperatedate(new Date());
     * auditSpSpSgxk.setItemname(itemname); auditSpSpSgxk.set("projectname",
     * itemname); auditSpSpSgxk.setSubappname(subappname);
     * auditSpSpSgxk.setItemcode(itemcode); auditSpSpSgxk.set("projectcode",
     * itemcode); auditSpSpSgxk.setProjectlevel(projectLevel);
     * auditSpSpSgxk.setItemaddress(itemAddress);
     * auditSpSpSgxk.set("projectaddress", itemcode);
     * auditSpSpSgxk.setArea(area); auditSpSpSgxk.setRoad(road);
     * auditSpSpSgxk.setEasttoarea(eastToArea);
     * auditSpSpSgxk.setWesttoarea(westToArea);
     * auditSpSpSgxk.setSouthtoarea(southToArea);
     * auditSpSpSgxk.setNorthtoarea(northToArea);
     * auditSpSpSgxk.setProtyped(proTyped); auditSpSpSgxk.setProou(proOu);
     * auditSpSpSgxk.setInvesttyped(investTyped);
     * auditSpSpSgxk.setHangyetype(hangyeType);
     * auditSpSpSgxk.setMoneysources(moneySources);
     * auditSpSpSgxk.setAllmoney(allMoney);
     * auditSpSpSgxk.setAreasources(areaSources);
     * auditSpSpSgxk.setUselandtype(useLandType);
     * auditSpSpSgxk.setAreaused(areaUsed);
     * auditSpSpSgxk.setNewlandtype(newLandType);
     * auditSpSpSgxk.setBuildtype(buildType);
     * auditSpSpSgxk.setAllbuildarea(allBuildArea);
     * auditSpSpSgxk.setOverloadarea(overLoadArea);
     * auditSpSpSgxk.setDownloadarea(downLoadArea);
     * auditSpSpSgxk.setBuildcontent(buildContent);
     * auditSpSpSgxk.setPlanstartdate(planStartDate);
     * auditSpSpSgxk.setPlanenddate(planEndDate); auditSpSpSgxk.setLxwh(lxwh);
     * auditSpSpSgxk.setLxrq(lxrq); auditSpSpSgxk.setLxjb(lxjb);
     * auditSpSpSgxk.setXmbm(xmbm); auditSpSpSgxk.setGcyt(gcyt);
     * auditSpSpSgxk.setGcfl(gcfl); auditSpSpSgxk.setZftzgs(zftzgs);
     * auditSpSpSgxk.setGhyzzpl(ghyzzpl);
     *
     * auditSpSpSgxk.set("totalSocietyCode", totalSocietyCode);// 统一社会信用代码
     * auditSpSpSgxk.set("proUnit", proUnit);// 项目单位
     * auditSpSpSgxk.set("corporationType", corporationType);// 法人类型
     * auditSpSpSgxk.set("corporationName", corporationName);// 法定代表人
     * auditSpSpSgxk.set("telAddress", telAddress);// 通讯地址
     * auditSpSpSgxk.set("postCode", postCode);// 邮政编码
     * auditSpSpSgxk.set("projectPerson", projectPerson);// 项目负责人
     * auditSpSpSgxk.set("telNumber", telNumber);// 联系电话
     * auditSpSpSgxk.set("applicantName", applicantName);// 授权申报人
     * auditSpSpSgxk.set("appTelNumber", appTelNumber);// 联系电话
     * auditSpSpSgxk.set("certType", certType);// 身份证件类型
     * auditSpSpSgxk.set("certNumber", certNumber);// 身份证件号码
     *
     * auditSpSpSgxk.set("stwcrq", stwcrq); // 审图完成日期 auditSpSpSgxk.set("zbfs",
     * zbfs); // 招标方式 auditSpSpSgxk.set("zbrq", zbrq); // 中标日期
     * auditSpSpSgxk.set("zbje", zbje); // 中标金额 auditSpSpSgxk.set("jgxs", jgxs);
     * // 结构形式 auditSpSpSgxk.set("jksd", jksd); // 基坑深度
     * auditSpSpSgxk.set("htlb", htlb); // 合同类别 auditSpSpSgxk.set("Ctrl20",
     * Ctrl20); // 合同签订日期 auditSpSpSgxk.set("Ctrl21", Ctrl21); // 合同工期
     * auditSpSpSgxk.set("Ctrl4", htkgsj); // 合同开工时间 auditSpSpSgxk.set("Ctrl5",
     * htjgsj); // 合同竣工时间 auditSpSpSgxk.set("Ctrl22", htje); // 合同金额（万元）
     * auditSpSpSgxk.set("Ctrl23", tdzbh); // 土地证编号 auditSpSpSgxk.set("Ctrl24",
     * jsgcghxkzbh); // 建设工程规划许可证编号 auditSpSpSgxk.set("Ctrl26", kcbmxydm); //
     * 勘察部门信用代码 auditSpSpSgxk.set("Ctrl25", kcbmmc); // 勘察部门名称
     * auditSpSpSgxk.set("Ctrl27", kcbmfzr); // 勘察部门负责人
     * auditSpSpSgxk.set("Ctrl28", kcbmtel); // 勘察部门电话
     * auditSpSpSgxk.set("Ctrl30", sjdwxydm); // 设计单位信用代码
     * auditSpSpSgxk.set("Ctrl29", sjdwmc); // 设计单位名称
     * auditSpSpSgxk.set("Ctrl31", sjdwfzr); // 设计单位负责人
     * auditSpSpSgxk.set("Ctrl32", sjdwtel); // 设计单位电话
     * auditSpSpSgxk.set("Ctrl34", sgcbdwxydm); // 施工总承包单位信用代码
     * auditSpSpSgxk.set("Ctrl33", sgcbdwmc); // 施工总承包单位名称
     * auditSpSpSgxk.set("Ctrl35", sgcbdwfzr); // 施工总承包单位负责人
     * auditSpSpSgxk.set("Ctrl36", sgcbdwtel); // 施工总承包单位电话
     * auditSpSpSgxk.set("Ctrl38", jldwxydm); // 监理单位信用代码
     * auditSpSpSgxk.set("Ctrl37", jldwmc); // 监理单位名称
     * auditSpSpSgxk.set("Ctrl39", jldwfzr); // 监理单位负责人
     * auditSpSpSgxk.set("Ctrl40", jldwtel); // 监理单位电话
     * auditSpSpSgxk.set("Ctrl42", tsjgxydm); // 图审机构信用代码
     * auditSpSpSgxk.set("Ctrl41", tsjgmc); // 图审机构名称
     * auditSpSpSgxk.set("Ctrl43", tsjgfzr); // 图审机构负责人
     * auditSpSpSgxk.set("Ctrl44", tsjgtel); // 图审机构电话
     * auditSpSpSgxk.set("Ctrl105", Ctrl105); // 设置位置
     * auditSpSpSgxk.set("Ctrl107", Ctrl107); // 总容量(平方米)
     * auditSpSpSgxk.set("Ctrl101", Ctrl101); // 设置形式
     * auditSpSpSgxk.set("Ctrl102", Ctrl102); // 储存形式
     * auditSpSpSgxk.set("Ctrl108", Ctrl108); // 储存物质名称
     * auditSpSpSgxk.set("Ctrl106", Ctrl106); // 储量 auditSpSpSgxk.set("Ctrl110",
     * Ctrl110); // 储存物质名称 auditSpSpSgxk.set("Ctrl103", Ctrl103); // 选择
     * auditSpSpSgxk.set("Ctrl111", Ctrl111); // 材料类别
     * auditSpSpSgxk.set("Ctrl113", Ctrl113); // 保温层数
     * auditSpSpSgxk.set("Ctrl112", Ctrl112); // 使用性质
     * auditSpSpSgxk.set("Ctrl114", Ctrl114); // 原有用途
     * auditSpSpSgxk.set("Ctrl104", Ctrl104); // 选择 装修工程
     * auditSpSpSgxk.set("Ctrl115", Ctrl115); // 装修部位
     * auditSpSpSgxk.set("Ctrl116", Ctrl116); // 装修面积
     * auditSpSpSgxk.set("Ctrl117", Ctrl117); // 装修层数
     * auditSpSpSgxk.set("Ctrl119", Ctrl119); // 使用性质
     * auditSpSpSgxk.set("Ctrl121", Ctrl121); // 原有用途
     * auditSpSpSgxk.set("Ctrl122", Ctrl122); // 消防设施
     * auditSpSpSgxk.set("Ctrl48", Ctrl48); // 申请内容 auditSpSpSgxk.set("Ctrl49",
     * Ctrl49); // 申请理由 auditSpSpSgxk.set("Ctrl50", Ctrl50); // 防空地下室易地建设理由
     * auditSpSpSgxk.set("Ctrl51", Ctrl51); // 防空地下室易地建设费减免条件
     *
     * auditSpSpSgxk.set("Ctrl53", Ctrl53); // 类别 auditSpSpSgxk.set("Ctrl54",
     * Ctrl54); // 类别 auditSpSpSgxk.set("Ctrl57", Ctrl57); // 设计单位名称
     * auditSpSpSgxk.set("Ctrl58", Ctrl58); // 设计单位等级
     * auditSpSpSgxk.set("Ctrl59", Ctrl59); // 设计单位法人
     * auditSpSpSgxk.set("Ctrl60", Ctrl60); // 设计单位联系人
     * auditSpSpSgxk.set("Ctrl62", Ctrl62); // 设计单位电话
     * auditSpSpSgxk.set("Ctrl63", Ctrl63); // 施工单位名称
     * auditSpSpSgxk.set("Ctrl64", Ctrl64); // 施工单位等级
     * auditSpSpSgxk.set("Ctrl65", Ctrl65); // 施工单位法人
     * auditSpSpSgxk.set("Ctrl66", Ctrl66); // 施工单位联系人
     * auditSpSpSgxk.set("Ctrl67", Ctrl67); // 施工单位电话
     * auditSpSpSgxk.set("Ctrl69", Ctrl69); // 监理单位名称
     * auditSpSpSgxk.set("Ctrl70", Ctrl70); // 监理单位等级
     * auditSpSpSgxk.set("Ctrl71", Ctrl71); // 监理单位法人
     * auditSpSpSgxk.set("Ctrl72", Ctrl72); // 监理单位联系人
     * auditSpSpSgxk.set("Ctrl74", Ctrl74); // 监理单位电话
     *
     * auditSpSpSgxk.set("Ctrl75", Ctrl75); // 建筑总面积大于500m2的
     * auditSpSpSgxk.set("Ctrl76", Ctrl76); // 建筑总面积大于1000m2的
     * auditSpSpSgxk.set("Ctrl77", Ctrl77); // 建筑总面积大于2500m2的
     * auditSpSpSgxk.set("Ctrl78", Ctrl78); // 建筑总面积大于10000m2的
     * auditSpSpSgxk.set("Ctrl79", Ctrl79); // 建筑总面积大于15000m2的
     * auditSpSpSgxk.set("Ctrl80", Ctrl80); // 建筑总面积大于500m2的
     * auditSpSpSgxk.set("Ctrl91", Ctrl91); // 其他特殊工程
     * auditSpSpSgxk.set("Ctrl93", Ctrl93); // 除以上两项以外的
     *
     * // 保存施工许可阶段的三个子表 List<Sgxksubtable1> sub1List =
     * JsonUtil.jsonToList(subTable1, Sgxksubtable1.class);; List<Sgxksubtable2>
     * sub2List = JsonUtil.jsonToList(subTable2, Sgxksubtable2.class);
     * List<Sgxksubtable3> sub3List = JsonUtil.jsonToList(subTable3,
     * Sgxksubtable3.class); for(Sgxksubtable1 sub1:sub1List){
     * sub1.setOperatedate(new Date());
     * sub1.setRowguid(UUID.randomUUID().toString());
     * sub1.setSubappguid(subappguid); subtable1Service.insert(sub1); }
     * for(Sgxksubtable2 sub2:sub2List){ sub2.setOperatedate(new Date());
     * sub2.setRowguid(UUID.randomUUID().toString());
     * sub2.setSubappguid(subappguid); subtable2Service.insert(sub2); }
     * for(Sgxksubtable3 sub3:sub3List){ sub3.setOperatedate(new Date());
     * sub3.setRowguid(UUID.randomUUID().toString());
     * sub3.setSubappguid(subappguid); sub3.put("subappguid", subappguid);
     * subtable3Service.insert(sub3); }
     *
     * // addFlag 为true, 说明数据库中不存在该记录 if (addFlag) {
     * iAuditSpSpSgxkService.insert(auditSpSpSgxk); } else {
     * iAuditSpSpSgxkService.update(auditSpSpSgxk); } // 保存子申报
     * setAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid,
     * subappname, type); }
     *
     * //保存第四阶段表单 private void saveJgysInfo(JSONObject param) { String type =
     * param.getString("type");// 1-保存 2-提交 String biguid =
     * param.getString("biguid"); // 主题实例guid String businessguid =
     * param.getString("businessguid");// 主题guid String phaseguid =
     * param.getString("phaseguid");// 阶段guid String subappguid =
     * param.getString("subappguid");// 申报唯一标识 String itemguid =
     * param.getString("itemguid");// 项目guid String itemname =
     * param.getString("itemname");// 项目名称 String subappname =
     * param.getString("subappname");// 子申报名称 String itemcode =
     * param.getString("itemcode");// 投资平台项目代码 String projectLevel =
     * param.getString("projectLevel");// 重点项目（代码项） String itemAddress =
     * param.getString("itemAddress");// 项目地址 String area =
     * param.getString("area");// 区（园区） String road = param.getString("road");//
     * 街道 String eastToArea = param.getString("eastToArea");// 四至范围（东至） String
     * westToArea = param.getString("westToArea");// 四至范围（西至） String southToArea
     * = param.getString("southToArea");// 四至范围（南至） String northToArea =
     * param.getString("northToArea");// 四至范围（北至） String proTyped =
     * param.getString("proTyped");// 立项类型（代码项） String proOu =
     * param.getString("proOu");// 立项部门（代码项） String investTyped =
     * param.getString("investTyped");// 投资类型（代码项） String hangyeType =
     * param.getString("hangyeType");// 行业类别（代码项） String moneySources =
     * param.getString("moneySources");// 资金来源（代码项） Double allMoney =
     * param.getDouble("allMoney");// 行业类别 String areaSources =
     * param.getString("areaSources");// 土地来源 String useLandType =
     * param.getString("useLandType");// 规划用地性质 Double areaUsed =
     * param.getDouble("areaUsed");// 用地面积（公顷） Double newLandType =
     * param.getDouble("newLandType");// 新增用地面积（公顷） String buildType =
     * param.getString("buildType");// 项目类型 Double allBuildArea =
     * param.getDouble("allBuildArea");// 总建筑面积（M2） Double overLoadArea =
     * param.getDouble("overLoadArea");// 地上（M2） Double downLoadArea =
     * param.getDouble("downLoadArea");// 地下（M2） String buildContent =
     * param.getString("buildContent");// 建设内容 Date planStartDate =
     * param.getDate("planStartDate");// 计划开工日期 Date planEndDate =
     * param.getDate("planEndDate");// 计划竣工日期
     *
     * String gcmc = param.getString("gcmc");// 工程名称 String lxjb =
     * param.getString("lxjb");// 立项级别 Double jzgd = param.getDouble("jzgd");//
     * 建筑高度 String jclb = param.getString("jclb");// 基础类别 Date startDate =
     * param.getDate("startDate");// 开工日期 Date endDate =
     * param.getDate("endDate");// 竣工日期 String bdh = param.getString("bdh");//
     * 标段号 String jzzh = param.getString("jzzh");// 桩号 String gcgm =
     * param.getString("gcgm");// 工程规模 Double gczj = param.getDouble("gczj");//
     * 工程造价(万元) Double rfjzmj = param.getDouble("rfjzmj");// 人防建筑面积 Double zxmj
     * = param.getDouble("zxmj");// 装修面积 String dscc =
     * param.getString("dscc");// 地上层次 String dxcc = param.getString("dxcc");//
     * 地下层次 String nhdj = param.getString("nhdj");// 耐火等级 String fldj =
     * param.getString("fldj");// 防雷等级 String gclb = param.getString("gclb");//
     * 工程类别 String jglx = param.getString("jglx");// 结构类型 String sylx =
     * param.getString("sylx");// 使用类型
     *
     * //-------------------- 第四阶段新增开始--------------------------- String
     * totalSocietyCode = param.getString("totalSocietyCode");// 统一社会信用代码 String
     * proUnit = param.getString("proUnit");// 项目单位 String corporationType =
     * param.getString("corporationType");// 法人类型 String corporationName =
     * param.getString("corporationName");// 法定代表人 String telAddress =
     * param.getString("telAddress");// 通讯地址 String postCode =
     * param.getString("postCode");// 邮政编码 String projectPerson =
     * param.getString("projectPerson");// 项目负责人 String telNumber =
     * param.getString("telNumber");// 联系电话 String applicantName =
     * param.getString("applicantName");// 授权申报人 String appTelNumber =
     * param.getString("appTelNumber");// 联系电话 String certType =
     * param.getString("certType");// 身份证件类型 String certNumber =
     * param.getString("certNumber");// 身份证件号码
     *
     * String ghxkzh = param.getString("ghxkzh"); // 规划许可证号 String sgxkzh =
     * param.getString("sgxkzh"); // 施工许可证号 String zmjcd =
     * param.getString("zmjcd"); // 总面积/长度（平方米/米） String jdzch =
     * param.getString("jdzch"); // 监督注册号 String sjmjcd =
     * param.getString("sjmjcd"); // 实际面积/长度（平方米/米） String jsdw =
     * param.getString("jsdw"); // 建设单位 String jsdwfzr =
     * param.getString("jsdwfzr"); // 项目负责人 String jsdwdh =
     * param.getString("jsdwdh"); // 联系电话 String kcdw = param.getString("kcdw");
     * // 勘察单位 String kcdwfzr = param.getString("kcdwfzr"); // 项目负责人 String
     * kcdwdh = param.getString("kcdwdh"); // 联系电话 String sjdw =
     * param.getString("sjdw"); // 设计单位 String sjdwfzr =
     * param.getString("sjdwfzr"); // 项目负责人 String sjdwdh =
     * param.getString("sjdwdh"); // 联系电话 String sgdw = param.getString("sgdw");
     * // 施工单位 String sgdwfzr = param.getString("sgdwfzr"); // 项目负责人 String
     * sgdwdh = param.getString("sgdwdh"); // 联系电话 String jldw =
     * param.getString("jldw"); // 监理单位 String jldwfzr =
     * param.getString("jldwfzr"); // 项目负责人 String jldwdh =
     * param.getString("jldwdh"); // 联系电话 //--------------------
     * 第四阶段新增结束---------------------------
     *
     * // 更新表单信息 AuditSpSpJgys auditSpSpJgys =
     * iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappguid); boolean
     * addFlag = false; // 默认为更新 if (auditSpSpJgys == null) { auditSpSpJgys =
     * new AuditSpSpJgys();
     * auditSpSpJgys.setRowguid(UUID.randomUUID().toString());
     * auditSpSpJgys.setSubappguid(subappguid); addFlag = true; // 新增 }
     * auditSpSpJgys.setOperateusername(ZwdtUserSession.getInstance("").
     * getClientName()); auditSpSpJgys.setOperatedate(new Date());
     * auditSpSpJgys.setItemname(itemname); auditSpSpJgys.set("projectname",
     * itemname); auditSpSpJgys.setSubappname(subappname);
     * auditSpSpJgys.setItemcode(itemcode); auditSpSpJgys.set("projectcode",
     * itemcode); auditSpSpJgys.setProjectlevel(projectLevel);
     * auditSpSpJgys.setItemaddress(itemAddress);
     * auditSpSpJgys.set("projectaddress", itemcode);
     * auditSpSpJgys.setArea(area); auditSpSpJgys.setRoad(road);
     * auditSpSpJgys.setEasttoarea(eastToArea);
     * auditSpSpJgys.setWesttoarea(westToArea);
     * auditSpSpJgys.setSouthtoarea(southToArea);
     * auditSpSpJgys.setNorthtoarea(northToArea);
     * auditSpSpJgys.setProtyped(proTyped); auditSpSpJgys.setProou(proOu);
     * auditSpSpJgys.setInvesttyped(investTyped);
     * auditSpSpJgys.setHangyetype(hangyeType);
     * auditSpSpJgys.setMoneysources(moneySources);
     * auditSpSpJgys.setAllmoney(allMoney);
     * auditSpSpJgys.setAreasources(areaSources);
     * auditSpSpJgys.setUselandtype(useLandType);
     * auditSpSpJgys.setAreaused(areaUsed);
     * auditSpSpJgys.setNewlandtype(newLandType);
     * auditSpSpJgys.setBuildtype(buildType);
     * auditSpSpJgys.setAllbuildarea(allBuildArea);
     * auditSpSpJgys.setOverloadarea(overLoadArea);
     * auditSpSpJgys.setDownloadarea(downLoadArea);
     * auditSpSpJgys.setBuildcontent(buildContent);
     * auditSpSpJgys.setPlanstartdate(planStartDate);
     * auditSpSpJgys.setPlanenddate(planEndDate); auditSpSpJgys.setGcmc(gcmc);
     * auditSpSpJgys.setLxjb(lxjb); auditSpSpJgys.setJzgd(jzgd);
     * auditSpSpJgys.setJclb(jclb); auditSpSpJgys.setStartdate(startDate);
     * auditSpSpJgys.setEnddate(endDate); auditSpSpJgys.setBdh(bdh);
     * auditSpSpJgys.setJzzh(jzzh); auditSpSpJgys.setGcgm(gcgm);
     * auditSpSpJgys.setGczj(gczj); auditSpSpJgys.setRfjzmj(rfjzmj);
     * auditSpSpJgys.setZxmj(zxmj); auditSpSpJgys.setDscc(dscc);
     * auditSpSpJgys.setDxcc(dxcc); auditSpSpJgys.setNhdj(nhdj);
     * auditSpSpJgys.setFldj(fldj); auditSpSpJgys.setGclb(gclb);
     * auditSpSpJgys.setJglx(jglx); auditSpSpJgys.setSylx(sylx);
     *
     * auditSpSpJgys.set("totalSocietyCode", totalSocietyCode);// 统一社会信用代码
     * auditSpSpJgys.set("proUnit", proUnit);// 项目单位
     * auditSpSpJgys.set("corporationType", corporationType);// 法人类型
     * auditSpSpJgys.set("corporationName", corporationName);// 法定代表人
     * auditSpSpJgys.set("telAddress", telAddress);// 通讯地址
     * auditSpSpJgys.set("postCode", postCode);// 邮政编码
     * auditSpSpJgys.set("projectPerson", projectPerson);// 项目负责人
     * auditSpSpJgys.set("telNumber", telNumber);// 联系电话
     * auditSpSpJgys.set("applicantName", applicantName);// 授权申报人
     * auditSpSpJgys.set("appTelNumber", appTelNumber);// 联系电话
     * auditSpSpJgys.set("certType", certType);// 身份证件类型
     * auditSpSpJgys.set("certNumber", certNumber);// 身份证件号码
     *
     * auditSpSpJgys.set("ghxkzh", ghxkzh); // 规划许可证号
     * auditSpSpJgys.set("sgxkzh", sgxkzh); // 施工许可证号 auditSpSpJgys.set("zmjcd",
     * zmjcd); // 总面积/长度（平方米/米） auditSpSpJgys.set("jdzch", jdzch); // 监督注册号
     * auditSpSpJgys.set("sjmjcd", sjmjcd); // 实际面积/长度（平方米/米）
     * auditSpSpJgys.set("jsdw", jsdw); // 建设单位 auditSpSpJgys.set("jsdwfzr",
     * jsdwfzr); // 项目负责人 auditSpSpJgys.set("jsdwdh", jsdwdh); // 联系电话
     * auditSpSpJgys.set("kcdw", kcdw); // 勘察单位 auditSpSpJgys.set("kcdwfzr",
     * kcdwfzr); // 项目负责人 auditSpSpJgys.set("kcdwdh", kcdwdh); // 联系电话
     * auditSpSpJgys.set("sjdw", sjdw); // 设计单位 auditSpSpJgys.set("sjdwfzr",
     * sjdwfzr); // 项目负责人 auditSpSpJgys.set("sjdwdh", sjdwdh); // 联系电话
     * auditSpSpJgys.set("sgdw", sgdw); // 施工单位 auditSpSpJgys.set("sgdwfzr",
     * sgdwfzr); // 项目负责人 auditSpSpJgys.set("sgdwdh", sgdwdh); // 联系电话
     * auditSpSpJgys.set("jldw", jldw); // 监理单位 auditSpSpJgys.set("jldwfzr",
     * jldwfzr); // 项目负责人 auditSpSpJgys.set("jldwdh", jldwdh); // 联系电话
     *
     * // addFlag 为true, 说明数据库中不存在该记录 if (addFlag) {
     * iAuditSpSpJgysService.insert(auditSpSpJgys); } else {
     * iAuditSpSpJgysService.update(auditSpSpJgys); } // 保存子申报
     * setAuditSpISubapp(biguid, businessguid, subappguid, phaseguid, itemguid,
     * subappname, type); }
     */

    /**
     * 子申报方法
     */
    public void setAuditSpISubapp(String biguid, String businessguid, String subappguid, String phaseguid,
                                  String itemguid, String subappname, String type, AuditOnlineRegister auditOnlineRegister) {
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (auditSpISubapp == null) {
            auditSpISubapp = new AuditSpISubapp();
            auditSpISubapp.setCreatedate(new Date());
            auditSpISubapp.setRowguid(subappguid);
        }
        auditSpISubapp.setOperatedate(new Date());
        auditSpISubapp.setOperateusername(auditOnlineRegister.getUsername());
        auditSpISubapp.setApplyerguid(auditOnlineRegister.getAccountguid());
        /*
         * if
         * (StringUtil.isBlank(ZwdtUserSession.getInstance("").getAccountGuid())
         * ) { AuditOnlineRegister auditOnlineRegister =
         * iAuditOnlineRegister.getRegisterByAccountguid(ZwdtUserSession.
         * getInstance("").getAccountGuid()).getResult(); SqlConditionUtil
         * sqlLegal = new SqlConditionUtil(); sqlLegal.eq("orgalegal_idnumber",
         * auditOnlineRegister.getIdnumber());
         * sqlLegal.isBlankOrValue("is_history", "0");
         * sqlLegal.eq("isactivated", "1"); List<AuditRsCompanyBaseinfo>
         * auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
         * .selectAuditRsCompanyBaseinfoByCondition(sqlLegal.getMap()).getResult
         * (); }
         */
        auditSpISubapp.setApplyername(auditOnlineRegister.getUsername());
        auditSpISubapp.setApplyerway("10");
        auditSpISubapp.setBiguid(biguid);
        auditSpISubapp.setBusinessguid(businessguid);
        auditSpISubapp.setPhaseguid(phaseguid);
        if ("2".equals(type)) {// 提交
            auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_WWDYS);
        } else if ("1".equals(type)) { // 保存
            auditSpISubapp.setStatus("3");
        }
        if (StringUtils.isNotBlank(itemguid)) {
            AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
            if (auditRsItemBaseinfo1 != null) {
                auditSpISubapp.setYewuguid(itemguid);
            } else {
                SqlConditionUtil conditionUtil = new SqlConditionUtil();
                conditionUtil.eq("biguid", biguid);
                List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                        .selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
                if (CollectionUtils.isNotEmpty(auditRsItemBaseinfos)) {
                    AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                    auditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                }

            }
        }
        auditSpISubapp.setSubappname(subappname);
        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (sub != null) {
            iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
        } else {
            iAuditSpISubapp.addSubapp(auditSpISubapp);
        }
        if ("2".equals(type)) {// 提交
            // 发送待办
            sendMessageCenter(phaseguid, biguid, subappguid, subappname);
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
        String url = "epointzwfw/auditsp/auditsphandle/handlebipreliminarydetail?biGuid=" + biguid + "&subappGuid="
                + subappGuid;
        // 存放统一收件人员部门的guid
        String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
        List<FrameUser> listUser = userService.listUserByOuGuid("", roleGuid, "", "", false, true, true, 3);
        if (listUser != null && listUser.size() > 0) {
            listUser.removeIf(u -> {
                boolean del = false;
                if (!del) {
                    FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(u.getOuGuid());
                    if (ouExtendInfo == null || StringUtil.isBlank(ouExtendInfo.get("areacode"))
                            || !auditSpInstance.getAreacode().equals(ouExtendInfo.get("areacode"))) {
                        del = true;
                    }
                }
                return del;
            });
            StringBuffer sb = new StringBuffer();
            if (listUser != null && listUser.size() > 0) {
                String content = "您有一条" + auditSpInstance.getItemname() + "申报待预审，请及时进行预审。";
                for (FrameUser user : listUser) {
                    // 发送成功保留记录
                    messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), "【申报预审】" + subappname,
                            IMessagesCenterService.MESSAGETYPE_WAIT, user.getUserGuid(), user.getDisplayName(),
                            fromUserGuid, fromUserName, "申报预审", url, user.getOuGuid(), "", 1, "", "", subappGuid,
                            subappGuid.substring(0, 1), new Date(), "", fromUserGuid, "", "");
                    sb.append(user.getUserGuid()).append(";");
                    if (StringUtil.isNotBlank(user.getMobile())) {
                        // 发送成功保留记录
                        messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0,
                                new Date(), user.getMobile(), user.getUserGuid(), user.getDisplayName(), "", "", "", "",
                                "", false, "短信");
                    }
                }
            }

            // 记录监控
            PhaseMonitorInfo info = monitorService.findBySubappguid(subappGuid, "1");
            AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();
            if (info != null) {
                info.setOperatedate(new Date());
                // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                info.setStatus("1");
                // 设置开始时间
                info.setStarttime(new Date());
                info.set("ouguid", sb.toString());
                monitorService.update(info);
            } else {
                info = new PhaseMonitorInfo();
                info.setRowguid(UUID.randomUUID().toString());
                info.set("subAppGuid", subappGuid);
                info.setOperatedate(new Date());
                info.setItemname(auditSpInstance.getItemname());
                if (auditSpPhase != null) {
                    info.setPhasename(auditSpPhase.getPhasename());
                }
                // 1、申报预审 2、材料审核 3、事项征求
                info.setMonitortype("1");
                info.setStarttime(new Date());
                info.set("ouguid", sb.toString());
                // 设置完成时间
                // info.setProcesstime(new Date());
                // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                info.setStatus("1");
                monitorService.insert(info);
            }
        }
    }

    // 默认为请选择
    public List<JSONObject> getSelectItemList(String codeName, boolean needEmpty) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        if (needEmpty) {
            JSONObject json = new JSONObject();
            json.put("optionvalue", "");
            json.put("optiontext", "请选择");
            json.put("isselected", 1);
            resultJsonList.add(json);
        }
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

    // 存在共同字段的, 先判断一下是否有值
    public List<JSONObject> getSelectItemNoEmptyList(String codeName, String filedValue) {
        List<JSONObject> resultJsonList = new ArrayList<>();
        // 获取代码项目
        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName(codeName);
        for (CodeItems codeItems : itemtypes) {
            JSONObject objJson = new JSONObject();
            objJson.put("optionvalue", codeItems.getItemValue());
            objJson.put("optiontext", codeItems.getItemText());
            // 判断是否有默认选中的
            if (codeItems.getItemValue().equals(filedValue)) {
                objJson.put("isselected", 1);
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
    /*
     * private AuditOnlineRegister getOnlineRegister(HttpServletRequest
     * httpServletRequest) { AuditOnlineRegister auditOnlineRegister;
     * OAuthCheckTokenInfo oAuthCheckTokenInfo =
     * CheckTokenUtil.getCheckTokenInfo(httpServletRequest); if
     * (oAuthCheckTokenInfo != null) { // 手机端 // 通过登录名获取用户 auditOnlineRegister =
     * iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.
     * getLoginid()) .getResult(); } else { // PC端 String accountGuid =
     * ZwdtUserSession.getInstance("").getAccountGuid(); if
     * (StringUtil.isNotBlank(accountGuid)) { auditOnlineRegister =
     * iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult(); }
     * else { // 通过登录名获取用户 auditOnlineRegister =
     * iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.
     * getCurrentIdentity()) .getResult(); } } return auditOnlineRegister; }
     */

    /*
     * @RequestMapping(value="/getSubtable", method=RequestMethod.GET) public
     * String getSubtable1(String subappGuid) { List<Sgxksubtable1> pagadata1 =
     * subtable1Service.findList(
     * "select * from sgxksubtable1 where subappguid = ?1", subappGuid);
     * List<Sgxksubtable2> pagadata2 = subtable2Service.findList(
     * "select * from sgxksubtable2 where subappguid = ?1", subappGuid);
     * List<Sgxksubtable3> pagadata3 = subtable3Service.findList(
     * "select * from sgxksubtable3 where subappguid = ?1", subappGuid);
     * JSONObject dataJson = new JSONObject(); if (pagadata1 != null) {
     * JSONObject jsonObject = new JSONObject(); String data =
     * JsonUtil.listToJson(pagadata1); jsonObject.put("total",
     * pagadata1.size()); jsonObject.put("data", data); dataJson.put("sub1",
     * jsonObject); } if (pagadata2 != null) { JSONObject jsonObject = new
     * JSONObject(); String data = JsonUtil.listToJson(pagadata2);
     * jsonObject.put("total", pagadata2.size()); jsonObject.put("data", data);
     * dataJson.put("sub2", jsonObject); } if (pagadata3 != null) { JSONObject
     * jsonObject = new JSONObject(); String data =
     * JsonUtil.listToJson(pagadata3); jsonObject.put("total",
     * pagadata3.size()); jsonObject.put("data", data); dataJson.put("sub3",
     * jsonObject); } return dataJson.toString(); }
     */

    /*
     * public DataGridModel<Sgxksubtable2> getTableModal2(){ if (sub2model ==
     * null) { sub2model = new DataGridModel<Sgxksubtable2>() {
     *
     * private static final long serialVersionUID = 1L;
     *
     * @Override public List<Sgxksubtable2> fetchData(int first, int pageSize,
     * String sortField, String sortOrder) { List<Sgxksubtable2> pagadata =
     * subtable2Service.findList(
     * "select * from sgxksubtable2 where subappguid = ?1", subappGuid); if
     * (pagadata != null) { this.setRowCount(pagadata.size()); return pagadata;
     * } else { this.setRowCount(0); return new ArrayList<Sgxksubtable2>(); } }
     * }; } return sub2model; }
     *
     * public DataGridModel<Sgxksubtable3> getTableModal3(){ if (sub3model ==
     * null) { sub3model = new DataGridModel<Sgxksubtable3>() {
     *
     * private static final long serialVersionUID = 1L;
     *
     * @Override public List<Sgxksubtable3> fetchData(int first, int pageSize,
     * String sortField, String sortOrder) { List<Sgxksubtable3> pagadata =
     * subtable3Service.findList(
     * "select * from sgxksubtable3 where subappguid = ?1", subappGuid); if
     * (pagadata != null) { this.setRowCount(pagadata.size()); return pagadata;
     * } else { this.setRowCount(0); return new ArrayList<Sgxksubtable3>(); } }
     * }; } return sub3model; }
     */
    @Override
    public void pageLoad() {
        subappGuid = getRequestParameter("subappGuid");
    }
}
