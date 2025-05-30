package com.epoint.jnzwdt.tzzxjgpt.rest;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.fmgl.auditrsitembaseinfoextends.api.entity.AuditRsItemBaseinfoExtends;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.esotericsoftware.minlog.Log;

import ewebeditor.admin.login_jsp;
import net.sf.json.JSONArray;


public class TzzxjgptUtil
{
	
	 /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    public static String sendtZxmbcxmxxcs(AuditRsItemBaseinfoExtends obj, AuditOnlineRegister auditOnlineRegister) {
        ICodeItemsService iCodeItemsService=ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
        // 获取请求头参数信息
        String ApiKey = ConfigUtil.getConfigValue("dhlogin", "bcApiKey");
        //String ApiKey = "622397268057653248";
        //获取接口地址,省发改委，保存项目信息
        String url = ConfigUtil.getConfigValue("dhlogin", "sfgwbcxmxxurl");
        //String url = "http://172.20.84.14/gateway/api/1/sfgw_bcxmxx";
        JSONObject dataJson = new JSONObject();
        try {
            //请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("appkey", ApiKey);
            //baseInfomap
            JSONObject baseInfomap = new JSONObject();
            JSONObject baseInfomap2 = new JSONObject();
            // 设置参数
            Map<String, Object> map = new HashMap<>(16);
            //region_code 区划ID（12位）
            String xzqhdm = obj.getXzqhdm();
            if(StringUtil.isNotBlank(xzqhdm)) {
                map.put("region_code", xzqhdm+"000000");
            }else {
                map.put("region_code","370800000000");
            }
            //区划名称region_name
            String region_name="";
            if(StringUtil.isNotBlank(xzqhdm)) {
                CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("济宁区县", xzqhdm);
                region_name=codeItemByCodeName.getItemText();
                // 区划名称region_name
                map.put("region_name", region_name);
               
            }else {
                // 区划名称region_name
                map.put("region_name", "济宁市");
            }
            map.put("baseInfo", baseInfomap);
           
            //uuid
            if (auditOnlineRegister != null) {
                baseInfomap.put("uuid", auditOnlineRegister.getStr("dhuuid"));
                //用户名userName
                baseInfomap.put("userName", auditOnlineRegister.getUsername());
            }else {
                baseInfomap.put("uuid", UUID.randomUUID().toString());
                //用户名userName
                baseInfomap.put("userName", "济宁工改");
            }
  
            //projectSource 项目来源（济南工程建设填写jngj）
            baseInfomap.put("projectSource", "ytgj");
            
            String divisionName="";
            if(StringUtil.isNotBlank(xzqhdm)) {
                CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("济宁区县", xzqhdm);
                divisionName=codeItemByCodeName.getItemText();
                // 区划名称region_name
                baseInfomap.put("divisionName", divisionName);
               
            }else {
                // 区划名称region_name
            	baseInfomap.put("divisionName", "济宁市");
            }
            
            
            //divisionName 省份
            //baseInfomap.put("divisionName", "山东省");
            //divisionCode 省划代码
            //baseInfomap.put("divisionCode", "370000000000");
            baseInfomap.put("divisionCode",  xzqhdm+"000000");
           // String legalpersonicardnum=obj.getString("legalpersonicardnum");
            String legalpersonicardnum=obj.getId();
            //permitIndustry 投资项目行业分类 
           // String permitIndustry = obj.getString("permitIndustry");
            String permitIndustry =obj.getPermitindustry();
            if(StringUtil.isNotBlank(permitIndustry)) {
                baseInfomap.put("permitIndustry", permitIndustry);
            }else {
                baseInfomap.put("permitIndustry", "");
            }
            //permitItemCode 行业核准目录 
           // String permitItemCode = obj.getString("permitItemCode");
            String permitItemCode =  obj.getPermititemcode();
            if(StringUtil.isNotBlank(permitItemCode)) {
                baseInfomap.put("permitItemCode", permitItemCode);
            }else {
                baseInfomap.put("permitItemCode", "");
            }
            //projectType项目类别
            String projectType =  obj.getProjecttype();
            if(StringUtil.isNotBlank(projectType)) {
                baseInfomap.put("projectType", projectType);
            }else {
                baseInfomap.put("projectType", "");
            }
            //constructPer建设性质
            String constructPer = obj.getConstructper();
            if(StringUtil.isNotBlank(constructPer)) {
                baseInfomap.put("constructPer", constructPer);
            }else {
                baseInfomap.put("constructPer", "");
            }
            //projectName项目名称
            String projectName = obj.getProjectname();
            if(StringUtil.isNotBlank(projectName)) {
                baseInfomap.put("projectName", projectName);
            }else {
                baseInfomap.put("projectName", "");
            }
            //linkMan项目负责人
            String linkMan = obj.getLinkman();
            if(StringUtil.isNotBlank(linkMan)) {
                baseInfomap.put("linkMan", linkMan);
            }else {
                baseInfomap.put("linkMan", "");
            }
            //linkPhone项目负责人联系方式
            String linkPhone = obj.getLinkphone();
            if(StringUtil.isNotBlank(linkPhone)) {
                baseInfomap.put("linkPhone", linkPhone);
            }else {
                baseInfomap.put("linkPhone", "");
            }
            //startYear拟开工时间
            Date startYeardate = obj.getStartyear();
            //Date startYeardate =EpointDateUtil.convertString2DateAuto(startYearstr);
            String startYear=null;
            if(StringUtil.isNotBlank(startYeardate)) {
               startYear = String.valueOf(EpointDateUtil.getYearOfDate(startYeardate));
            }
            if(StringUtil.isNotBlank(startYear)) {
                baseInfomap.put("startYear", startYear);
            }else {
                baseInfomap.put("startYear", "");
            }
            //endYear
            Date endYeardate = obj.getEndyear();
            //Date endYeardate = EpointDateUtil.convertString2DateAuto(endYearstr);
            String endYear = null;
            if(StringUtil.isNotBlank(endYeardate)) {
                endYear = String.valueOf(EpointDateUtil.getYearOfDate(endYeardate));
            }
            if(StringUtil.isNotBlank(endYear)) {
                baseInfomap.put("endYear", endYear);
            }else {
                baseInfomap.put("endYear", "");
            }
            //investment总投资
            String investment = obj.getInvestment();
            if(StringUtil.isNotBlank(investment)) {
                baseInfomap.put("investment", investment);
            }else {
                baseInfomap.put("investment", "");
            }
            //radPlace建设地点
            baseInfomap.put("radPlace", "no");
   
            //placeCode建设地点
            String placeCode = obj.getPlacecode();
            String placeDetailCode=null;
            //String jsddxq=obj.getString("placeDetailCode");
            String placeName=null;
            String placeDetailName =null;
            if(StringUtil.isNotBlank(placeCode)) {
                baseInfomap.put("placeCode", placeCode);
                placeDetailCode="370000,370800,"+placeCode;
               // placeName = iCodeItemsService.getItemTextByCodeID("1015690", placeCode);
                CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("济宁区县", placeCode);
                placeName= codeItemByCodeName.getItemText();
                placeDetailName="山东省,济宁市,"+placeName;
            }else {
                baseInfomap.put("placeCode", "");
            }
            if(StringUtil.isNotBlank(placeName)) {
                baseInfomap.put("placeName", placeName);
            }else {
                baseInfomap.put("placeName", "");
            }
            //placeName1
            baseInfomap.put("placeName1", "");
            //placeDetailName
            if(StringUtil.isNotBlank(placeDetailName)) {
                baseInfomap.put("placeDetailName", placeDetailName);
            }else {
                baseInfomap.put("placeDetailName", "");
            }
            //placeDetailCode
            if(StringUtil.isNotBlank(placeDetailCode)) {
                baseInfomap.put("placeDetailCode", placeDetailCode);
            }else {
                baseInfomap.put("placeDetailCode", "");
            }
            //isDeArea是否开发区项目
            baseInfomap.put("isDeArea", "0");
            //deAreaName开发区名称
            baseInfomap.put("deAreaName", "");

            //industry国标行业
            String industry = obj.getIndustry();
            //industryName国标行业名称
            String industryName =null;
            if(StringUtil.isNotBlank(industry)) {
                String industrystr = industry.substring(0, 1);
                if("A".equals(industrystr)) {
                    baseInfomap.put("industry", industry+"X");
                }else {
                    String industry2 = industry.substring(1, industry.length());
                    baseInfomap.put("industry", "A"+industry2+"X");
                }
                CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("国标行业2017", industry);
                industryName = codeItemByCodeName.getItemText();
            }else {
                baseInfomap.put("industry", "");
            }
           
            if(StringUtil.isNotBlank(industryName)) {
                baseInfomap.put("industryName", industryName);
            }else {
                baseInfomap.put("industryName", "");
            }
            //cyjgtzzdml产业结构目录AA9999
            String cyjgtzzdml = obj.getCyjgtzzdml();
            log.info("cyjgtzzdml输入："+cyjgtzzdml);
            //cyjgtzzdmlName除以上条目外的农林业
            String cyjgtzzdmlName =null;
            if(StringUtil.isNotBlank(cyjgtzzdml)) {
            	CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("产业结构222", cyjgtzzdml);
            	cyjgtzzdmlName = codeItemByCodeName.getItemText();
                 
            	if (cyjgtzzdml.length() == 3) {
            		cyjgtzzdml = cyjgtzzdml.substring(1) + "0000";
            	}
            	
            	if (cyjgtzzdml.length() == 9) {
            		cyjgtzzdml = cyjgtzzdml.substring(3);
            	}
            	log.info("cyjgtzzdml:"+cyjgtzzdml);
                baseInfomap.put("cyjgtzzdml", cyjgtzzdml);
               
            }else {
                baseInfomap.put("cyjgtzzdml", "");
            }
            if(StringUtil.isNotBlank(cyjgtzzdmlName)) {
                baseInfomap.put("cyjgtzzdmlName", cyjgtzzdmlName);
                
            }else {
                baseInfomap.put("cyjgtzzdmlName", "");
            }
            //theIndustry所属行业值
            String theIndustry = obj.getTheindustry();
            //theIndustryName所属行业
            String theIndustryName = null;
            if(StringUtil.isNotBlank(theIndustry)) {
                baseInfomap.put("theIndustry",theIndustry);
                CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("省发改_所属行业", theIndustry);
                theIndustryName =codeItemByCodeName.getItemText();
            }else {
                baseInfomap.put("theIndustry","");
            }
            //theIndustryName所属行业
            if(StringUtil.isNotBlank(theIndustryName)) {
                baseInfomap.put("theIndustryName", theIndustryName);
            }else {
                baseInfomap.put("theIndustryName", "");
            }
            //applyDate申报日期
            Date applyDate=new Date();
            baseInfomap.put("applyDate", applyDate);
            //projectContent建设规模及内容
            String projectContent = obj.getProjectcontent();
            if(StringUtil.isNotBlank(projectContent)) {
                baseInfomap.put("projectContent", projectContent);
            }else {
                baseInfomap.put("projectContent", "");
            }
            //projectStage项目阶段
            String projectStage = obj.getProjectstage();
            if(StringUtil.isNotBlank(projectStage)) {
                baseInfomap.put("projectStage", projectStage);
            }else {
                baseInfomap.put("projectStage", "");
            }
            //projectAttributes项目属性
            String projectAttributes = obj.getProjectattributes();
            if(StringUtil.isNotBlank(projectAttributes)) {
                baseInfomap.put("projectAttributes", projectAttributes);
            }else {
                baseInfomap.put("projectAttributes", "");
            }
            //projectkind 项目性质
            String projectkind = obj.getStr("projectkind");
            if(StringUtil.isNotBlank(projectkind)) {
                baseInfomap.put("projectKind", projectkind);
            }else {
                baseInfomap.put("projectKind", "");
            }
            //tjxm
            String tjxm = obj.getTjxm();
            if(StringUtil.isNotBlank(tjxm)) {
                baseInfomap.put("tjxm", tjxm);
            }else {
                baseInfomap.put("tjxm", "");
            }
            //projectSite项目所在地
            String projectSite = obj.getProjectsite();
            if(StringUtil.isNotBlank(projectSite)) {
                baseInfomap.put("projectSite", projectSite);
            }else {
                baseInfomap.put("projectSite", "");
            }
            //chinaTotalMoney中方投资额(万元)
            String chinaTotalMoney = obj.getChinatotalmoney();
            if(StringUtil.isNotBlank(chinaTotalMoney)) {
                baseInfomap.put("chinaTotalMoney", chinaTotalMoney);
            }else {
                baseInfomap.put("chinaTotalMoney", "");
            }
            //investmentMode投资方式
            String foreignAbroadFlag="0";
            String investmentMode = obj.getInvestmentmode();
            if(StringUtil.isNotBlank(investmentMode)) {
                baseInfomap.put("investmentMode", investmentMode);
                foreignAbroadFlag="2";
            }else {
                String investmentMode2 = obj.getInvestmentmode2();
                if(StringUtil.isNotBlank(investmentMode2)) {
                    baseInfomap.put("investmentMode", investmentMode2);
                    foreignAbroadFlag="1";
                }else {
                    baseInfomap.put("investmentMode", "");
                    
                }
            }
            if("1".equals(foreignAbroadFlag) || "2".equals(foreignAbroadFlag)) {
            	baseInfomap.put("isCountrySecurity", "0");
            }
            
            //isCountrySecurity是否涉及国家安全
           /* String isCountrySecurity = obj.getIscountrysecurity();
            if(StringUtil.isNotBlank(isCountrySecurity)) {
                baseInfomap.put("isCountrySecurity", isCountrySecurity);
            }else {
                baseInfomap.put("isCountrySecurity", "");
            }*/
            
            //securityApprovalNumber安全审查决定文号
   /*         String securityApprovalNumber = obj.getSecurityapprovalnumber();
            if(StringUtil.isNotBlank(securityApprovalNumber)) {
                baseInfomap.put("securityApprovalNumber", securityApprovalNumber);
            }else {
                baseInfomap.put("securityApprovalNumber", "");
            }*/
            baseInfomap.put("securityApprovalNumber", "");
            //totalMoneyDollar总投资额折合美元(万元
                String totalMoneyDollar = obj.getTotalmoneydollar();
                if(StringUtil.isNotBlank(totalMoneyDollar)) {
                    baseInfomap.put("totalMoneyDollar", totalMoneyDollar);
                }else {
                    baseInfomap.put("totalMoneyDollar", "");
                }
            //totalMoneyDollarRate总投资额使用的汇率(人民币/美元)
                String totalMoneyDollarRate = obj.getTotalmoneydollarrate();
                if(StringUtil.isNotBlank(totalMoneyDollarRate)) {
                    baseInfomap.put("totalMoneyDollarRate", totalMoneyDollarRate);
                }else {
                    baseInfomap.put("totalMoneyDollarRate", "");
                }
            //projectCapitalMoney项目资本金(万元)
                String projectCapitalMoney = obj.getProjectcapitalmoney();
                if(StringUtil.isNotBlank(projectCapitalMoney)) {
                    baseInfomap.put("projectCapitalMoney", projectCapitalMoney);
                }else {
                    baseInfomap.put("projectCapitalMoney", "");
                }
            //projectCapitalMoneyDollar项目资本金折合美元(万元)
                String projectCapitalMoneyDollar = obj.getProjectcapitalmoneydollar();
                if(StringUtil.isNotBlank(projectCapitalMoneyDollar)) {
                    baseInfomap.put("projectCapitalMoneyDollar", projectCapitalMoneyDollar);
                }else {
                    baseInfomap.put("projectCapitalMoneyDollar", "");
                }
            //capitalMoneyDollarRate项目资本金使用的汇率(人民币/美元)
                String capitalMoneyDollarRate = obj.getCapitalmoneydollarrate();
                if(StringUtil.isNotBlank(capitalMoneyDollarRate)) {
                    baseInfomap.put("capitalMoneyDollarRate", capitalMoneyDollarRate);
                }else {
                    baseInfomap.put("capitalMoneyDollarRate", "");
                }
            //industrialPolicy适用产业政策条目
                String industrialPolicystr = obj.getIndustrialpolicy();
                String industrialPolicy=null;
                //industrialPolicyName适用产业政策条目代码
                String industrialPolicyName=null;
                if(StringUtil.isNotBlank(industrialPolicystr)) {
                    int length = industrialPolicystr.length();
                   switch (length) {
                    case 1:
                        industrialPolicy=industrialPolicystr+"XXXXX";
                        break;
                    case 2:
                        industrialPolicy=industrialPolicystr+"XXXX";
                        break;
                    case 3:
                        industrialPolicy=industrialPolicystr+"XXX";
                        break;
                    case 4:
                        industrialPolicy=industrialPolicystr+"XX";
                        break;
                    case 5:
                        industrialPolicy=industrialPolicystr+"X";
                        break;
                    default:
                        break;
                }
                    baseInfomap.put("industrialPolicy", industrialPolicy);
                    CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("省发改_外商投资适用产业政策条目（鼓励类）", industrialPolicystr);
                    industrialPolicyName = codeItemByCodeName.getItemText();        
                }else {
                    String industrialPolicy2 = obj.getIndustrialpolicy2();
                    if(StringUtil.isNotBlank(industrialPolicy2)) {
                        baseInfomap.put("industrialPolicy", industrialPolicy2);
                        CodeItems codeItemByCodeName = iCodeItemsService.getCodeItemByCodeName("省发改_外商投资适用产业政策条目（限制类）", industrialPolicy2);
                        industrialPolicyName = codeItemByCodeName.getItemText();        
                    }else {
                        baseInfomap.put("industrialPolicy", "");
                    }
                }
                if(StringUtil.isNotBlank(industrialPolicyName)) {
                    baseInfomap.put("industrialPolicyName", industrialPolicyName);
                }else {
                    baseInfomap.put("industrialPolicyName", "");
                }
              /*  baseInfomap.put("industrialPolicy", "");
                baseInfomap.put("industrialPolicyName", "");*/
            //industrialPolicyType适用产业政策条目类型
                String industrialPolicyType = obj.getIndustrialpolicytype();
                if(StringUtil.isNotBlank(industrialPolicyType)) {
                    baseInfomap.put("industrialPolicyType", industrialPolicyType);
                }else {
                    baseInfomap.put("industrialPolicyType", "");
                }
            //otherInvestmentApplyInfo其他投资方式需予以申报的情况
                String otherInvestmentApplyInfo = obj.getOtherinvestmentapplyInfo();
                if(StringUtil.isNotBlank(otherInvestmentApplyInfo)) {
                    baseInfomap.put("otherInvestmentApplyInfo", otherInvestmentApplyInfo);
                }else {
                    baseInfomap.put("otherInvestmentApplyInfo", "");
                }
            //transactionBothInfo提供交易双方情况
                String transactionBothInfo = obj.getTransactionbothinfo();
                if(StringUtil.isNotBlank(transactionBothInfo)) {
                    baseInfomap.put("transactionBothInfo", transactionBothInfo);
                }else {
                    String transactionBothInfo2 = obj.getTransactionbothinfo2();
                    if(StringUtil.isNotBlank(transactionBothInfo2)) {
                        baseInfomap.put("transactionBothInfo", transactionBothInfo2);
                    }else {
                        baseInfomap.put("transactionBothInfo", "");
                    }
                    
                }
            //mergerPlan并购安排：
                String mergerPlan = obj.getMergerplan();
                if(StringUtil.isNotBlank(mergerPlan)) {
                    baseInfomap.put("mergerPlan", mergerPlan);
                }else {
                    String mergerPlan2 = obj.getMergerplan2();
                    if(StringUtil.isNotBlank(mergerPlan2)) {
                        baseInfomap.put("mergerPlan", mergerPlan2);
                    }else {
                        baseInfomap.put("mergerPlan", "");
                    }
                }
            //mergerManagementModeScope并购后经营方式及经营范围：
                String mergerManagementModeScope = obj.getMergermanagementmodescope();
                if(StringUtil.isNotBlank(mergerManagementModeScope)) {
                    baseInfomap.put("mergerManagementModeScope", mergerManagementModeScope);
                }else {
                    String mergerManagementModeScope2 = obj.getMergermanagementmodescope2();
                    if(StringUtil.isNotBlank(mergerManagementModeScope2)) {
                        baseInfomap.put("mergerManagementModeScope", mergerManagementModeScope2);
                    }else {
                        baseInfomap.put("mergerManagementModeScope", "");
                    }
                   
                }
            //getLandMode土地获取方
                String getLandMode = obj.getGetlandmode();
                if(StringUtil.isNotBlank(getLandMode)) {
                    baseInfomap.put("getLandMode", getLandMode);
                }else {
                    baseInfomap.put("getLandMode", "");
                }
            //landArea总用地面积（平方米）
                String landArea = obj.getLandarea();
                if(StringUtil.isNotBlank(landArea)) {
                    baseInfomap.put("landArea", landArea);
                }else {
                    baseInfomap.put("landArea", "");
                }
           
            //builtArea总建筑面积（平方米）
                String builtArea = obj.getBuiltarea();
                if(StringUtil.isNotBlank(builtArea)) {
                    baseInfomap.put("builtArea", builtArea);
                }else {
                    baseInfomap.put("builtArea", "");
                }
            //isAddDevice是否新增设备
                String isAddDevice = obj.getIsadddevice();
                if(StringUtil.isNotBlank(isAddDevice)) {
                    baseInfomap.put("isAddDevice", isAddDevice);
                }else {
                    baseInfomap.put("isAddDevice", "");
                }
            //importDeviceNumberMoney其中：拟进口设备数量及金额
                String importDeviceNumberMoney = obj.getImportdevicenumbermoney();
                if(StringUtil.isNotBlank(importDeviceNumberMoney)) {
                    baseInfomap.put("importDeviceNumberMoney", importDeviceNumberMoney);
                }else {
                    baseInfomap.put("importDeviceNumberMoney", "");
                }
            if(StringUtil.isNotBlank(placeDetailCode)) {
                baseInfomap.put("placeCodeDetail", placeDetailCode);
            }else {
                baseInfomap.put("placeCodeDetail", "");
            }
            if(StringUtil.isNotBlank(placeDetailName)) {
                baseInfomap.put("placeAreaDetail", placeDetailName);
            }else {
                baseInfomap.put("placeAreaDetail", "");
            }
            //lerepInfo
            JSONArray lerepInfojsons=new JSONArray();
            JSONObject lerepInfojson=new JSONObject();
            //enterpriseName
            String enterpriseName = obj.getEnterprisename();
            lerepInfojson.put("enterpriseName", enterpriseName);
            //lerepCerttype
          //  String lerepCerttype = obj.getString("lerepCerttype");
            lerepInfojson.put("lerepCerttype", "A05300");
            //lerepCertno
            String lerepCertno = obj.getLerepcertno();
            lerepInfojson.put("lerepCertno", lerepCertno);
            //contactName
            String contactName = obj.getContactname();
            lerepInfojson.put("contactName", contactName);
            //contactTel
            String contactTel = obj.getContacttel();
            lerepInfojson.put("contactTel", contactTel);
            //contactEmail
            String contactEmail = obj.getContactemail();
            lerepInfojson.put("contactEmail", contactEmail);
            //enterpriseNature
            String enterpriseNature = obj.getEnterprisenature();
            lerepInfojson.put("enterpriseNature", enterpriseNature);
            //contactPhone
            String contactPhone = obj.getContactphone();
            lerepInfojson.put("contactPhone", contactPhone);
            //contactFax
            String contactFax = obj.getContactfax();
            lerepInfojson.put("contactFax", contactFax);
            //correspondenceAddress
            String correspondenceAddress = obj.getCorrespondenceaddress();
            lerepInfojson.put("correspondenceAddress", correspondenceAddress);
            lerepInfojsons.add(lerepInfojson);
            baseInfomap.put("lerepInfo", lerepInfojsons);
            JSONArray lerepsbinfojsons=new JSONArray();
            JSONObject lerepsbinfojson=new JSONObject();
            //lerepsbinfo
            
            //enterpriseNamesb
            String enterpriseNamesb = obj.getEnterprisenamesb();
            lerepsbinfojson.put("enterpriseNamesb", enterpriseNamesb);
            //lerepCerttypesb
            String lerepCerttypesb = obj.getLerepcerttypesb();
            lerepsbinfojson.put("lerepCerttypesb", lerepCerttypesb);
            //lerepCertnosb
            String lerepCertnosb = obj.getLerepcertnosb();
            lerepsbinfojson.put("lerepCertnosb", lerepCertnosb);
            //contactNamesb
            String contactNamesb = obj.getContactnamesb();
            lerepsbinfojson.put("contactNamesb", contactNamesb);
            //contactTelsb
            String contactTelsb = obj.getContacttelsb();
            lerepsbinfojson.put("contactTelsb", contactTelsb);
            //contactEmailsb
            String contactEmailsb = obj.getContactemailsb();
            lerepsbinfojson.put("contactEmailsb", contactEmailsb);
            //enterpriseNaturesb
            String enterpriseNaturesb = obj.getEnterprisenaturesb();
            lerepsbinfojson.put("enterpriseNaturesb", enterpriseNaturesb);
            //contactPhonesb
            String contactPhonesb = obj.getContactphonesb();
            lerepsbinfojson.put("contactPhonesb", contactPhonesb);
            //contactFaxsb
            String contactFaxsb = obj.getContactfaxsb();
            lerepsbinfojson.put("contactFaxsb", contactFaxsb);
            //correspondenceAddresssb
            String correspondenceAddresssb = obj.getCorrespondenceaddresssb();
            lerepsbinfojson.put("correspondenceAddresssb", correspondenceAddresssb);
            lerepsbinfojsons.add(lerepsbinfojson);
            baseInfomap.put("lerepsbinfo", lerepsbinfojsons);
            baseInfomap.put("foreignAbroadFlag", foreignAbroadFlag);
            if(StringUtil.isNotBlank(foreignAbroadFlag)&&"1".equals(foreignAbroadFlag)) {
                JSONObject continfojson=new JSONObject();
                JSONArray continfojsons=new JSONArray();
                //出资类型businessType  businesstype
               /* String businessType = obj.getBusinesstype();
                continfojson.put("businessType", businessType);*/
                //投资者名称investmentName   investmentname
                String investmentName = obj.getInvestmentname();
                continfojson.put("investmentName", investmentName);
                //注册国别地区regCountry  regcountry
                String regCountry = obj.getRegcountry();
                continfojson.put("regCountry", regCountry);
                //出资方式contributionMode   contributionmode
                String contributionMode = obj.getContributionmode();
                continfojson.put("contributionMode", contributionMode);
                //出资额(万元)contributionLimit   contributionlimit
                String contributionLimit = obj.getContributionlimit();
                continfojson.put("contributionLimit", contributionLimit);
                //出资比例contributionRatio    contributionratio
                String contributionRatio = obj.getContributionratio();
                continfojson.put("contributionRatio", contributionRatio);
                continfojsons.add(continfojson);
                baseInfomap.put("contrInfo", continfojsons);
            }
            String investId =null;
            String seqId=null;
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            String data = HttpUtil.doPost(url, map, headers);
           // String data =  HttpUtil.doPostJson(url, JsonUtil.objectToJson(map), headers);

            Log.info("赋码申请接口返回结果" +data);
            //转为json对象
            JSONObject jsondata1 = JSONObject.parseObject(data);
            String responsecode = jsondata1.getString("code");
            JSONObject jsondata3=null;
            if("200".equals(responsecode)) {
                String responsedata = jsondata1.getString("data");
                JSONObject jsondata2 = JSONObject.parseObject(responsedata);
                String responsecode2 = jsondata2.getString("code");
                if("200".equals(responsecode2)) {
                    String responsedata2 = jsondata2.getString("data");
                    jsondata3 = JSONObject.parseObject(responsedata2);
                    String responsecode3 = jsondata3.getString("code");
                    if("200".equals(responsecode3)) {
                        investId = jsondata3.getString("investId");
                        seqId = jsondata3.getString("seqId");
                        dataJson.put("code", "1");
                        dataJson.put("investId", investId);
                        dataJson.put("seqId", seqId);
                        return dataJson.toString();
                    }else {
                        if(StringUtil.isNotBlank(jsondata3)) {
                            String errortext=jsondata3.getString("error");
                            dataJson.put("error", errortext);
                        }
                        dataJson.put("code", "0");
                        return dataJson.toString();
                    }
                }else {
                    dataJson.put("code", "0");
                    return dataJson.toString();
                }
            }else {
                dataJson.put("code", "0");
                return dataJson.toString();
            }
        }
        catch (Exception e) {
        }
        dataJson.put("code", "0");
        return dataJson.toString();
    }
    
    /**********************************************咨询相关接口结束**********************************************/

}
    
