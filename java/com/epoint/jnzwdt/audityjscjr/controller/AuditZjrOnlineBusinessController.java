package com.epoint.jnzwdt.audityjscjr.controller;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
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
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdt.login.dhrsautil.AESUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.jnzwdt.audityjscjr.EnhancedCodeModelFactory;
import com.epoint.jnzwdt.audityjscjr.api.IAuditYjsCjrService;
import com.epoint.jnzwdt.audityjscjr.api.entity.AuditYjsCjr;
import com.epoint.jnzwdt.audityjscjr.api.entity.AuditYjsCjrInfo;
import com.epoint.jnzwdt.audityjscjr.api.entity.AuditYjsCjrInfoDetail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 套餐相关接口
 *
 * @version [F9.3, 2017年10月28日]
 * @作者 xli
 */
@RestController
@RequestMapping("/zwdtCjrBusiness")
public class AuditZjrOnlineBusinessController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 网上用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
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
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;
    /**
     * 关联事项API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;
    
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    
    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    
    /**
     * 阶段事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
    
    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    
    /**
     * 评价API
     */
    @Autowired
    private IAuditOnlineEvaluat iAuditOnlineEvaluat;

    /**
     * 一件事API
     */
    @Autowired
    private IAuditYjsCjrService iAuditYjsCjrService;

    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCjrBusinessInfo", method = RequestMethod.POST)
    public String getCjrBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBirthBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取业务标识
                String cjrGuid = obj.getString("yewuguid");
                // 1.2、获取实例标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isBlank(cjrGuid) && StringUtil.isNotBlank(biGuid)) {
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    cjrGuid = auditSpInstance.getYewuguid();
                    dataJson.put("itemname", auditSpInstance.getItemname());
                    dataJson.put("areacode", auditSpInstance.getAreacode());
                    // 获取主题信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                            .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                    dataJson.put("note", auditSpBusiness.getNote());

                    // 5.1、获取办件满意度
                    AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat.selectEvaluatByClientIdentifier(biGuid)
                            .getResult();
                    if (auditOnlineEvaluat != null) {
                        String satisfied = "";// 满意度
                        String evaluateContent = "";// 评价内容
                        if (auditOnlineEvaluat != null) {
                            satisfied = iCodeItemsService.getItemTextByCodeName("满意度",
                                    auditOnlineEvaluat.getSatisfied());
                            evaluateContent = StringUtil.isBlank(auditOnlineEvaluat.getEvaluatecontent()) ? ""
                                    : auditOnlineEvaluat.getEvaluatecontent();
                        }
                        dataJson.put("satisfied", satisfied);
                        dataJson.put("evaluatecontent", evaluateContent);
                    }
                    // 5.2、默认无需显示提交按钮(考虑到大厅页面没有关闭，审批系统直接受理情况)
                    String materialstatus = "1";
                    List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                            .getSpIMaterialByBIGuid((auditSpInstance.getRowguid())).getResult();
                    if (auditSpIMaterials != null && auditSpIMaterials.size() > 0) {
                        for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                            // 5.2.1、如果材料中存在需要补正的材料，则需要显示提交按钮
                            if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                                materialstatus = "0";
                                break;
                            }
                        }
                    }
                    dataJson.put("materialstatus", materialstatus);

                }
                if (StringUtil.isNotBlank(businessGuid)) {
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        dataJson.put("businessname", auditSpBusiness.getStr("businessname"));
                        dataJson.put("note", auditSpBusiness.getStr("note"));
                    }
                }
                // 2、获取表单实例信息
                AuditYjsCjr auditYjsCjr = iAuditYjsCjrService.find(cjrGuid);
                if (auditYjsCjr != null) {
                    dataJson.put("rowguid", auditYjsCjr.getRowguid());
                    dataJson.put("name", auditYjsCjr.getName());
                    dataJson.put("brith_time", StringUtil.isBlank(auditYjsCjr.getBrith_time()) ? ""
                            : EpointDateUtil.convertDate2String(auditYjsCjr.getBrith_time(), "yyyy-MM-dd")); 
                    dataJson.put("sex", auditYjsCjr.getSex());
                    dataJson.put("idcard", auditYjsCjr.getIdcard());
                    dataJson.put("residence", auditYjsCjr.getResidence());
                    dataJson.put("con_phone", auditYjsCjr.getCon_phone());
                    dataJson.put("con_tel", auditYjsCjr.getCon_tel());
                    dataJson.put("accountbank", auditYjsCjr.getAccountbank());
                    dataJson.put("bankaccount", auditYjsCjr.getBankaccount());
                    dataJson.put("bankaccountholder", auditYjsCjr.getBankaccountholder());
                    dataJson.put("dbzh", auditYjsCjr.getDbzh());
                    dataJson.put("bankaccount", auditYjsCjr.getBankaccount());
                    dataJson.put("residence_area", auditYjsCjr.getResidence_area());
                    dataJson.put("guard_name", auditYjsCjr.getGuard_name());
                    dataJson.put("guard_phone", auditYjsCjr.getGuard_phone());
                    dataJson.put("guard_contelphone", auditYjsCjr.getGuard_contelphone());
                    dataJson.put("guard_idcard", auditYjsCjr.getGuard_idcard());
                }
                List<CodeItems> sexCode = iCodeItemsService.listCodeItemsByCodeName("性别");
                List<CodeItems> mzCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_残联民族");
                List<CodeItems> educatonCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_残联文化程度");
                List<CodeItems> deformityTypeCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_残联残疾类别");
                List<CodeItems> accountCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_开户银行");
                List<CodeItems> hjxzCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_户籍性质");
                List<CodeItems> maritalCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_婚姻状况");
                List<CodeItems> relationCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_监护人关系");
                List<CodeItems> residenceCode = iCodeItemsService.listCodeItemsByCodeName("残联一件事_户口性质");
                List<CodeItems> hasGuardCode = iCodeItemsService.listCodeItemsByCodeName("是否");
                List<JSONObject> sexList = new ArrayList<>();
                for (CodeItems codeItems : sexCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getSex())) {
                        objJson.put("ischecked", 1);
                    }
                    sexList.add(objJson);
                }
                dataJson.put("sexlist", sexList);
                List<JSONObject> hasGuardList = new ArrayList<>();
                for (CodeItems codeItems : hasGuardCode) {
                	JSONObject objJson = new JSONObject();
                	objJson.put("itemvalue", codeItems.getItemValue());
                	objJson.put("itemtext", codeItems.getItemText());
                	if (auditYjsCjr != null
                			&& codeItems.getItemValue().equals(auditYjsCjr.getStr("hasguard"))) {
                		objJson.put("ischecked", 1);
                	}
                	hasGuardList.add(objJson);
                }
                dataJson.put("jianhulist", hasGuardList);
                List<JSONObject> nationList = new ArrayList<>();
                JSONObject nationjson = new JSONObject();
                nationjson.put("itemvalue", "");
                nationjson.put("itemtext", "请选择");
                nationjson.put("isselected", 1);
                nationList.add(nationjson);
                for (CodeItems codeItems : mzCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getNation())) {
                        objJson.put("isselected", 1);
                        nationList.get(0).put("isselected", 0);
                    }
                    nationList.add(objJson);
                }
                dataJson.put("nationlist", nationList);


                List<JSONObject> educationList = new ArrayList<>();
                JSONObject educationJson = new JSONObject();
                educationJson.put("itemvalue", "");
                educationJson.put("itemtext", "请选择");
                educationJson.put("isselected", 1);
                educationList.add(educationJson);
                for (CodeItems codeItems : educatonCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getEducation())) {
                        objJson.put("isselected", 1);
                        educationList.get(0).put("isselected", 0);
                    }
                    educationList.add(objJson);
                }
                dataJson.put("educationlist", educationList);

                List<JSONObject> maritalList = new ArrayList<>();
                JSONObject maritalJson = new JSONObject();
                maritalJson.put("itemvalue", "");
                maritalJson.put("itemtext", "请选择");
                maritalJson.put("isselected", 1);
                maritalList.add(maritalJson);
                for (CodeItems codeItems : maritalCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getMarital())) {
                        objJson.put("isselected", 1);
                        maritalList.get(0).put("isselected", 0);
                    }
                    maritalList.add(objJson);
                }
                dataJson.put("maritallist", maritalList);

                List<JSONObject> residenceList = new ArrayList<>();
                JSONObject residenceJson = new JSONObject();
                residenceJson.put("itemvalue", "");
                residenceJson.put("itemtext", "请选择");
                residenceJson.put("isselected", 1);
                residenceList.add(residenceJson);
                for (CodeItems codeItems : residenceCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getResidence())) {
                        objJson.put("isselected", 1);
                        residenceList.get(0).put("isselected", 0);
                    }
                    residenceList.add(objJson);
                }
                dataJson.put("residencelist", residenceList);


                List<JSONObject> hjxzList = new ArrayList<>();
                JSONObject hjxzJson = new JSONObject();
                hjxzJson.put("itemvalue", "");
                hjxzJson.put("itemtext", "请选择");
                hjxzJson.put("isselected", 1);
                hjxzList.add(hjxzJson);
                for (CodeItems codeItems : hjxzCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getHjxz())) {
                        objJson.put("isselected", 1);
                        hjxzList.get(0).put("isselected", 0);
                    }
                    hjxzList.add(objJson);
                }
                dataJson.put("hjxzlist", hjxzList);


                


                List<JSONObject> deformity_typeList = new ArrayList<>();
                JSONObject deformity_typeJson = new JSONObject();
                deformity_typeJson.put("itemvalue", "");
                deformity_typeJson.put("itemtext", "请选择");
                deformity_typeJson.put("isselected", 1);
                deformity_typeList.add(deformity_typeJson);
                for (CodeItems codeItems : deformityTypeCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getDeformity_type())) {
                        objJson.put("isselected", 1);
                        deformity_typeList.get(0).put("isselected", 0);
                    }
                    deformity_typeList.add(objJson);
                }
                dataJson.put("deformity_typelist", deformity_typeList);
                
                List<JSONObject> account_typeList = new ArrayList<>();
                JSONObject account_typeJson = new JSONObject();
                account_typeJson.put("itemvalue", "");
                account_typeJson.put("itemtext", "请选择");
                account_typeJson.put("isselected", 1);
                account_typeList.add(account_typeJson);
                for (CodeItems codeItems : accountCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getAccountbank())) {
                        objJson.put("isselected", 1);
                        account_typeList.get(0).put("isselected", 0);
                    }
                    account_typeList.add(objJson);
                }
                dataJson.put("account_typelist", account_typeList);
                
                

                List<JSONObject> relationList = new ArrayList<>();
                JSONObject relationJson = new JSONObject();
                relationJson.put("itemvalue", "");
                relationJson.put("itemtext", "请选择");
                relationJson.put("isselected", 1);
                relationList.add(relationJson);
                for (CodeItems codeItems : relationCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (auditYjsCjr != null
                            && codeItems.getItemValue().equals(auditYjsCjr.getRelation())) {
                        objJson.put("isselected", 1);
                        relationList.get(0).put("isselected", 0);
                    }
                    relationList.add(objJson);
                }
                dataJson.put("relationlist", relationList);


                List<JSONObject> districtList = new ArrayList<>();
                JSONObject districtJson = new JSONObject();
                districtJson.put("itemvalue", "");
                districtJson.put("itemtext", "请选择");
                districtJson.put("isselected", 1);
                districtList.add(districtJson);
                List<Map<String, String>> districtCode = EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, "");
                if (districtCode != null && !districtCode.isEmpty()) {
                    for (Map<String, String> map : districtCode) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            System.out.println(entry);
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", entry.getValue());
                            objJson.put("itemtext", entry.getKey());
                            if (auditYjsCjr != null && entry.getValue().equals(auditYjsCjr.getBelong_district())) {
                                objJson.put("isselected", 1);
                                districtList.get(0).put("isselected", 0);
                            }
                            districtList.add(objJson);
                        }
                    }
                }
                dataJson.put("districtlist", districtList);


                List<JSONObject> townList = new ArrayList<>();
                JSONObject townJson = new JSONObject();
                townJson.put("itemvalue", "");
                townJson.put("itemtext", "请选择");
                townJson.put("isselected", 1);
                townList.add(townJson);
                if(auditYjsCjr != null && StringUtil.isNotBlank(auditYjsCjr.getBelong_district())){
                    List<Map<String, String>> townCode = EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, auditYjsCjr.getBelong_district());
                    if (townCode != null && !townCode.isEmpty()) {
                        for (Map<String, String> map : townCode) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", entry.getValue());
                                objJson.put("itemtext", entry.getKey());
                                if (entry.getValue().equals(auditYjsCjr.getBelong_town())) {
                                    objJson.put("isselected", 1);
                                    townList.get(0).put("isselected", 0);
                                }
                                townList.add(objJson);
                            }
                        }
                    }
                }
                dataJson.put("townlist", townList);

                List<JSONObject> villageList = new ArrayList<>();
                JSONObject villageJson = new JSONObject();
                villageJson.put("itemvalue", "");
                villageJson.put("itemtext", "请选择");
                villageJson.put("isselected", 1);
                villageList.add(villageJson);
                if(auditYjsCjr != null && StringUtil.isNotBlank(auditYjsCjr.getBelong_town())){
                    List<Map<String, String>> villageCode = EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, auditYjsCjr.getBelong_town());
                    if (villageCode != null && !villageCode.isEmpty()) {
                        for (Map<String, String> map : villageCode) {
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                JSONObject objJson = new JSONObject();
                                objJson.put("itemvalue", entry.getValue());
                                objJson.put("itemtext", entry.getKey());
                                if (entry.getValue().equals(auditYjsCjr.getBelong_village())) {
                                    objJson.put("isselected", 1);
                                    villageList.get(0).put("isselected", 0);
                                }
                                villageList.add(objJson);
                            }
                        }
                    }
                }
                dataJson.put("villagelist", villageList);
                // 获取残联结果数据
                com.epoint.core.utils.sql.SqlConditionUtil sqlCondition = new com.epoint.core.utils.sql.SqlConditionUtil();
                sqlCondition.eq("cjrguid", cjrGuid);
                List<AuditYjsCjrInfo> list = iAuditYjsCjrService.findList(sqlCondition.getMap(), AuditYjsCjrInfo.class);
                if (list != null && !list.isEmpty()) {
                    AuditYjsCjrInfo auditYjsCjrInfo = list.get(0);
                    auditYjsCjrInfo.set("scbzrq",EpointDateUtil.convertDate2String(auditYjsCjrInfo.getScbzrq(),EpointDateUtil.DATE_FORMAT));
                    auditYjsCjrInfo.set("zhshrq",EpointDateUtil.convertDate2String(auditYjsCjrInfo.getZhshrq(),EpointDateUtil.DATE_FORMAT));
                    auditYjsCjrInfo.set("yxqkssj",EpointDateUtil.convertDate2String(auditYjsCjrInfo.getYxqkssj(),EpointDateUtil.DATE_FORMAT));
                    auditYjsCjrInfo.set("yxqjssj",EpointDateUtil.convertDate2String(auditYjsCjrInfo.getYxqjssj(),EpointDateUtil.DATE_FORMAT));
                    dataJson.put("cjrInfo", auditYjsCjrInfo);
                    sqlCondition.clear();
                    sqlCondition.eq("infoguid", auditYjsCjrInfo.getRowguid());
                    List<AuditYjsCjrInfoDetail> cjrInfoDetailList = iAuditYjsCjrService.findList(sqlCondition.getMap(), AuditYjsCjrInfoDetail.class);
                    if (cjrInfoDetailList != null && !cjrInfoDetailList.isEmpty()) {
                        for (int i = 0; i < cjrInfoDetailList.size(); i++) {
                            cjrInfoDetailList.get(i).set("no", (i+1));
                            cjrInfoDetailList.get(i).set("pdsj",EpointDateUtil.convertDate2String(cjrInfoDetailList.get(i).getPdsj(),EpointDateUtil.DATE_FORMAT));
                        }
                    }
                    dataJson.put("cjrInfoDetailList", cjrInfoDetailList);
                }
                log.info("=======结束调用getBirthBusinessInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息查询异常", "");
        }
    }


    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveCjrBusinessInfo", method = RequestMethod.POST)
    public String saveCjrBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveBirthBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
            	JSONObject obj;
            	//pc端params参数为正常json格式。邹城对接山东app为加密字符串
            	try {
            		obj = JSONObject.parseObject(jsonObject.getString("params"));
				} catch (Exception e) {
					String key="epoint@123!@#$";
					obj = JSONObject.parseObject(AESUtil.decrypt(jsonObject.getString("params"), key));
				}
                String name = obj.getString("name");
                String sex = obj.getString("sex");
                String brith_time = obj.getString("brith_time");
                String idcard = obj.getString("idcard");
                String nation = obj.getString("nation");
                String education = obj.getString("education");
                String marital = obj.getString("marital");
                String residence = obj.getString("residence");
                String con_phone = obj.getString("con_phone");
                String con_tel = obj.getString("con_tel");
                String accountbank = obj.getString("accountbank");
                String bankaccount = obj.getString("bankaccount");
                String bankaccountholder = obj.getString("bankaccountholder");
                String dbzh = obj.getString("dbzh");
                String hjxz = obj.getString("hjxz");
                String belong_district = obj.getString("belong_district");
                String belong_town = obj.getString("belong_town");
                String belong_village = obj.getString("belong_village");
                String residence_area = obj.getString("residence_area");
                String deformity_type = obj.getString("deformity_type");
                String guard_name = obj.getString("guard_name");
                String relation = obj.getString("relation");
                
                String hasguard = obj.getString("hasguard");
                String guard_phone = obj.getString("guard_phone");
                String guard_contelphone = obj.getString("guard_contelphone");
                String guard_idcard = obj.getString("guard_idcard");
                String applyreason = obj.getString("applyreason");

                // 1.31、获取业务标识
                String yewuGuid = obj.getString("yewuguid");
                // 1.32、获取套餐主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.33、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.34、主题实例标识
                String biGuid = obj.getString("biguid");
                // 1.35、主题实例标识
                String subappGuid = obj.getString("subappguid");

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户个人信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取主题基本信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        // 3.1、申报时以套餐配置的辖区为准，如果为空则用当前的辖区编码
                        areaCode = StringUtil.isNotBlank(auditSpBusiness.getAreacode()) ? auditSpBusiness.getAreacode()
                                : areaCode;
                        // 4、保存或更新基本信息
                        AuditYjsCjr auditYjsCjr = iAuditYjsCjrService.find(yewuGuid);
                        if (auditYjsCjr == null) {
                        	
                        	if (StringUtil.isBlank(subappGuid)) {
                                subappGuid = UUID.randomUUID().toString();
                            }
                        	
                            // 4.1、若不存在信息则新增数据
                            auditYjsCjr = new AuditYjsCjr();
                            auditYjsCjr.setRowguid(yewuGuid);
                            auditYjsCjr.setOperatedate(new Date());
                            auditYjsCjr.setName(name);
                            auditYjsCjr.setSex(sex);
                            auditYjsCjr.set("hasguard", hasguard);
                            auditYjsCjr.setBrith_time(EpointDateUtil.convertString2Date(brith_time, EpointDateUtil.DATE_FORMAT));
                            auditYjsCjr.setIdcard(idcard);
                            auditYjsCjr.setNation(nation);
                            auditYjsCjr.setEducation(education);
                            auditYjsCjr.setMarital(marital);
                            auditYjsCjr.setResidence(residence);
                            auditYjsCjr.setCon_phone(con_phone);
                            auditYjsCjr.setCon_tel(con_tel);
                            auditYjsCjr.setAccountbank(accountbank);
                            auditYjsCjr.setBankaccount(bankaccount);
                            auditYjsCjr.setBankaccountholder(bankaccountholder);
                            auditYjsCjr.setDbzh(dbzh);
                            auditYjsCjr.setHjxz(hjxz);
                            auditYjsCjr.setBelong_district(belong_district);
                            auditYjsCjr.setBelong_town(belong_town);
                            auditYjsCjr.setBelong_village(belong_village);
                            auditYjsCjr.setResidence_area(residence_area);
                            auditYjsCjr.setDeformity_type(deformity_type);
                            
                            auditYjsCjr.setGuard_name(guard_name);
                            auditYjsCjr.setRelation(relation);
                            auditYjsCjr.setGuard_phone(guard_phone);
                            auditYjsCjr.setGuard_contelphone(guard_contelphone);
                            auditYjsCjr.setGuard_idcard(guard_idcard);
                            auditYjsCjr.set("subappguid", subappGuid);
                            auditYjsCjr.set("applyreason", applyreason);
                            iAuditYjsCjrService.insert(auditYjsCjr);
                            // 4.2、初始化实例信息
                            // 4.2.1、生成主题实例信息
                            biGuid = iAuditSpInstance.addBusinessInstanceAndBiguid(biGuid, businessGuid, yewuGuid,
                                    auditOnlineIndividual.getApplyerguid(), name, ZwfwConstant.APPLY_WAY_NETSBYS,
                                    auditSpBusiness.getBusinessname(), areaCode, auditSpBusiness.getBusinesstype())
                                    .getResult();
                            // 4.2.2、生成子申报信息
                            String phaseGuid = "";
                            List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid)
                                    .getResult();
                            if (auditSpPhases != null && auditSpPhases.size() > 0) {
                                phaseGuid = auditSpPhases.get(0).getRowguid();
                            }
                            AuditSpISubapp recordSubapp = new AuditSpISubapp();
                            
                            recordSubapp.setApplyerguid(auditOnlineIndividual.getApplyerguid());
                            recordSubapp.setApplyername(auditOnlineIndividual.getClientname());
                            recordSubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                            recordSubapp.setBiguid(biGuid);
                            recordSubapp.setBusinessguid(businessGuid);
                            recordSubapp.setCreatedate(new Date());
                            recordSubapp.setRowguid(subappGuid);
                            recordSubapp.setPhaseguid(phaseGuid);
                            recordSubapp.setStatus(ZwfwConstant.LHSP_Status_DPS);
                            recordSubapp.setSubappname(name);
                            recordSubapp.setInitmaterial(ZwfwConstant.CONSTANT_STR_ONE);
                            iAuditSpISubapp.addSubapp(recordSubapp);
                            // 4.2.3、生成事项实例信息
                            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                                    .getResult();
                            List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                            if (auditSpITasks.size() == 0) {
                                for (AuditSpTask auditSpTask : auditSpTasks) {
                                    // 查询并遍历标准事项关联关系表
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                    List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                            .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                        AuditTask auditTask = iAuditTask
                                                .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                        if (auditTask != null) {
                                            iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                                    auditTask.getRowguid(), auditTask.getTaskname(), subappGuid, 0);
                                        }
                                    }

                                }
                            }

                            // 4.2.4、 初始化套餐式办件信息
                            iAuditOnlineProject.initOnlineProjectForBusiness(businessGuid,
                                    auditOnlineIndividual.getApplyerguid(), areaCode, auditOnlineIndividual.getClientname(), biGuid, subappGuid,
                                    yewuGuid, auditSpBusiness.getBusinessname());
                        }
                        else {
                            // 获取套餐实例
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(yewuGuid)
                                    .getResult();
                            if (auditSpInstance != null) {
                                if (!auditSpInstance.getApplyerguid().equals(auditOnlineIndividual.getApplyerguid())) {
                                    return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                                }
                            }
                            // 5、如果已存在基本信息，更新基本信息
                            auditYjsCjr.setOperatedate(new Date());
                            auditYjsCjr.setOperatedate(new Date());
                            auditYjsCjr.setName(name);
                            auditYjsCjr.setSex(sex);
                            auditYjsCjr.set("hasguard", hasguard);
                            auditYjsCjr.setBrith_time(EpointDateUtil.convertString2Date(brith_time, EpointDateUtil.DATE_FORMAT));
                            auditYjsCjr.setIdcard(idcard);
                            auditYjsCjr.setNation(nation);
                            auditYjsCjr.setEducation(education);
                            auditYjsCjr.setMarital(marital);
                            auditYjsCjr.setResidence(residence);
                            auditYjsCjr.setCon_phone(con_phone);
                            auditYjsCjr.setCon_tel(con_tel);
                            auditYjsCjr.setAccountbank(accountbank);
                            auditYjsCjr.setBankaccount(bankaccount);
                            auditYjsCjr.setBankaccountholder(bankaccountholder);
                            auditYjsCjr.setDbzh(dbzh);
                            auditYjsCjr.setHjxz(hjxz);
                            auditYjsCjr.setBelong_district(belong_district);
                            auditYjsCjr.setBelong_town(belong_town);
                            auditYjsCjr.setBelong_village(belong_village);
                            auditYjsCjr.setResidence_area(residence_area);
                            auditYjsCjr.setDeformity_type(deformity_type);
                            auditYjsCjr.setGuard_name(guard_name);
                            auditYjsCjr.setRelation(relation);
                            auditYjsCjr.setGuard_phone(guard_phone);
                            auditYjsCjr.setGuard_contelphone(guard_contelphone);
                            auditYjsCjr.setGuard_idcard(guard_idcard);
                            iAuditYjsCjrService.update(auditYjsCjr);
                            // 5.1、更新套餐实例的申请人信息
                            if (auditSpInstance != null) {
                                auditSpInstance.set("applyername", name);
                                iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
                                // 5.2、更新子申报申请人信息
                                List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp
                                        .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
                                AuditSpISubapp auditSpISubapp = null;
                                if (auditSpISubappList != null && auditSpISubappList.size() > 0) {
                                    auditSpISubapp = auditSpISubappList.get(0);
                                }
                                auditSpISubapp.set("subappname", name);
                                auditSpISubapp.set("applyername", name);
                                auditSpISubapp.set("initmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                                // 5.3、更新AUDIT_ONLINE_PROJECT办件申请人信息
                                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                                SqlConditionUtil conditionsql = new SqlConditionUtil();
                                updateFieldMap.put("applyername=", name);
                                updateDateFieldMap.put("applydate=",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                                conditionsql.eq("sourceguid", auditSpInstance.getStr("rowguid"));
                                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                        conditionsql.getMap());
                            }
                        }
                    }
                }
                log.info("=======结束调用saveBirthBusinessInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息保存异常", "");
        }
    }


    /**
     * 切换地区
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/changeArea", method = RequestMethod.POST)
    public String changeArea(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用changeArea接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                System.out.println(obj);
                String area = obj.getString("area");
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isNotBlank(area)) {
                    List<JSONObject> list = new ArrayList<>();
                    JSONObject json = new JSONObject();
                    json.put("itemvalue", "");
                    json.put("itemtext", "请选择");
                    json.put("isselected", 1);
                    list.add(json);
                    List<Map<String, String>> code = EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, area);
                    for (Map<String, String> map : code) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("itemvalue", entry.getValue());
                            objJson.put("itemtext", entry.getKey());
                            list.add(objJson);
                        }
                    }
                    dataJson.put("list", list);
                }
                log.info("=======结束调用changeArea接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "切换地区异常", "");
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
}
