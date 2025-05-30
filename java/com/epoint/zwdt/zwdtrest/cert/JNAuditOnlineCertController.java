package com.epoint.zwdt.zwdtrest.cert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
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
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.AttachUtil;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdtutil.ZwdtUserAuthValid;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;

/**
 *  证照库相关接口
 *  
 * @作者 WST
 * @version [F9.3, 2017年11月9日]
 */
@RestController
@RequestMapping("/jnzwdtCert")
public class JNAuditOnlineCertController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
    * 政务大厅注册用户API
    */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 政务大厅用户信息API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 证照目录分类API
     */
    @Autowired
    private ICertConfigExternal iCertConfigExternal;
    /**
     * 证照基本信息API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 证照库附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    @Autowired
    private IConfigService iConfigService;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    
    
    /**
     * 个性化获取大数据证照
     */
    @Autowired
    private IJnProjectService iJnProjectService;
    
    /**
     * 法定代表人API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    
    

    /**
     *  获取证照信息分类的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getCertType", method = RequestMethod.POST)
    public String getCertType(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取政务大厅个人信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取证照分类代码项
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("证照分类");
                    List<CodeItems> personalCodeItems = new ArrayList<CodeItems>();
                    for (CodeItems item : codeItems) {
                        if (item.getItemValue().startsWith(ZwdtConstant.CERTOWNERTYPE_PERSON)
                                || item.getItemValue().startsWith("003")) {
                            personalCodeItems.add(item);
                        }
                    }
                    List<JSONObject> certTypeList = new ArrayList<JSONObject>();
                    // 4、获取用户拥有的各个证照分类的证照实例信息
                    for (CodeItems items : personalCodeItems) {
                        JSONObject certTypeJson = new JSONObject();
                        certTypeJson.put("typevalue", items.getItemValue());
                        certTypeJson.put("typename", items.getItemText());
                        // 4.1、获取用户在此证照分类下的证照实例
                        List<CertInfo> certInfoList = iCertInfoExternal.selectCertInfo(
                                ZwdtConstant.CERTOWNERTYPE_PERSON, auditOnlineIndividual.getIdnumber(),
                                items.getItemValue(), "", ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY,
                                false, "",null);
                        int count = 0; // 证照分类下的统计数量
                        if (certInfoList != null && certInfoList.size() > 0) {
                            for (CertInfo certInfo : certInfoList) {
                                List<JSONObject> attachList = iCertAttachExternal
                                        .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                CertCatalog certCatalog = iCertConfigExternal
                                        .getCatalogByCatalogid(certInfo.getCertcatalogid(), ZwdtConstant.CERTAPPKEY);
                                if (certCatalog != null) {
                                    String materialLevel = certInfo.getCertlevel();//可信等级
                                    if (ZwdtConstant.CERTLEVEL_C.equals(materialLevel)
                                            || ZwdtConstant.CERTLEVEL_D.equals(materialLevel)) {
                                        // 4.3、C\D级别的证照分类只要有证照实例就需要显示                                       
                                        if (attachList != null && attachList.size() > 0) {
                                            count++;
                                        }
                                    }
                                    else {
                                        // 4.4、A\B级别的证照需要有证照实例对应的附件才显示
                                        if (StringUtil.isNotBlank(certInfo.getCertcliengguid())) {
                                            if (attachList != null && attachList.size() > 0) {
                                                count++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        certTypeJson.put("count", count);
                        certTypeList.add(certTypeJson);
                    }
                    
                   /* //添加大数据相关的证照文件
                    List<FrameAttachInfo> attachinfos =  iJnProjectService.getFrameAttachByIdenumberTag(auditOnlineIndividual.getIdnumber(),"大数据电子证照");
                    if (attachinfos != null && attachinfos.size() > 0) {
                    	if (certTypeList != null && certTypeList.size() > 0) {
                    		for (int i=0;i<certTypeList.size();i++) {
                    			 JSONObject certTypeJsonobj = certTypeList.get(i);
                    			 int count = Integer.parseInt(certTypeJsonobj.getString("count"));
                    			 count += attachinfos.size();
                    			 certTypeJsonobj.put("count", count);
                    		}
                    	}
                    	
                    }
                    
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.eq("isactivated", "1");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    if (auditRsCompanyBaseinfoList != null && auditRsCompanyBaseinfoList.size() > 0) {
                    	for (AuditRsCompanyBaseinfo company : auditRsCompanyBaseinfoList) {
                    		 //添加大数据相关的证照文件
                            List<FrameAttachInfo> attachinfoss =  iJnProjectService.getFrameAttachByIdenumberTag(company.getCreditcode(),"大数据电子证照");
                            if (attachinfoss != null && attachinfoss.size() > 0) {
                            	if (certTypeList != null && certTypeList.size() > 0) {
                            		for (int i=0;i<certTypeList.size();i++) {
                            			 JSONObject certTypeJsonobj = certTypeList.get(i);
                            			 int count = Integer.parseInt(certTypeJsonobj.getString("count"));
                            			 count += attachinfoss.size();
                            			 certTypeJsonobj.put("count", count);
                            		}
                            	}
                            	
                            }
                    	}
                    }*/
                    
                    
                    // 5、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("certtypelist", certTypeList);
                    log.info("=======结束调用getCertType接口=======");
                    return JsonUtils.zwdtRestReturn("1", "证照信息分类获取成功", dataJson.toString());
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
            log.info("=======getCertType接口参数：params【" + params + "】=======");
            log.info("=======getCertType异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "证照信息分类获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取证照分类下的证照样本列表
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getCertListByType", method = RequestMethod.POST)
    public String getCertListByType(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertListByType接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String certType = obj.getString("certtype");// 证照类别              
                certType = StringUtil.isNotBlank(certType) ? certType : ZwdtConstant.CERTTYPE_BASICINFO;// 证照类别没有传递则默认为基本信息
                String certLevel = obj.getString("certlevel");// 证照级别     APP保留
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取个人用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、根据证照类别及证照级别获取用户的证照实例信息
                    List<CertInfo> certInfoList = iCertInfoExternal.selectCertInfo(ZwdtConstant.CERTOWNERTYPE_PERSON,
                            auditOnlineIndividual.getIdnumber(), certType, certLevel, ZwdtConstant.MATERIALTYPE_CERT,
                            ZwdtConstant.CERTAPPKEY, false, "",null);
                    List<JSONObject> gradeACertList = new ArrayList<JSONObject>(); // A级证照信息
                    List<JSONObject> gradeBCertList = new ArrayList<JSONObject>(); // B级证照信息                                  
                    int gardeACertnum = 0;// A级证照数量
                    int gardeBCertnum = 0;// B级证照数量            
                    if (certInfoList != null && certInfoList.size() > 0) {
                        for (CertInfo certInfo : certInfoList) {
                            CertCatalog certCatalog = iCertConfigExternal
                                    .getCatalogByCatalogid(certInfo.getCertcatalogid(), ZwdtConstant.CERTAPPKEY);
                            // 4、如果证照实例没有对应的证照样本信息则不需要返回实例信息
                            if (certCatalog != null) {
                                // 5、返回证照实例信息
                                JSONObject certJson = new JSONObject();// 证照
                                certJson.put("certname", certInfo.getCertname()); // 证照名称
                                // certJson.put("sharematerialiguid", certInfo.getRowguid()); // 证照实例标识
                                String source = certInfo.getCertawarddept();
                                if (StringUtil.isBlank(source)) {
                                    source = ZwdtConstant.MATERIALSOURCE_SP.equals(certInfo.getMaterialsource()) ? "自制"
                                            : "其他";
                                }
                                certJson.put("certsource", source);// 证照来源                           
                                certJson.put("expiredatefrom", EpointDateUtil
                                        .convertDate2String(certInfo.getExpiredatefrom(), EpointDateUtil.DATE_FORMAT)); // 证照有效开始日期
                                String notice = "";// 证照是否过期提示信息
                                // 5.1、判断证照有效期是否已到期
                                if (StringUtil.isNotBlank(certInfo.getExpiredateto())) {
                                    // 5.1.1、证照有效期截止日期
                                    Date expireDateTo = EpointDateUtil.convertString2Date(
                                            EpointDateUtil.convertDate2String(certInfo.getExpiredateto()),
                                            EpointDateUtil.DATE_FORMAT);
                                    // 5.1.2、当前日期
                                    Date nowDate = EpointDateUtil.convertString2Date(
                                            EpointDateUtil.convertDate2String(new Date()), EpointDateUtil.DATE_FORMAT);
                                    if (expireDateTo.compareTo(nowDate) == 0) {
                                        // 5.1.3、有效期的截止日期是今天
                                        notice = "今日到期";
                                    }
                                    else if (expireDateTo.compareTo(nowDate) == 1) {
                                        // 5.1.4、有效期的截止日期是今天之后，提示剩余的天数
                                        notice = "还有" + EpointDateUtil.getIntervalDays(expireDateTo, nowDate) + "天过期";
                                    }
                                    else if (expireDateTo.compareTo(nowDate) == -1) {
                                        // 5.1.5、有效期的截止日期是今天之前，提示超期的天数
                                        notice = "已超期" + EpointDateUtil.getIntervalDays(nowDate, expireDateTo) + "天";
                                    }
                                }
                                certJson.put("notice", notice);
                                // 5.2、如果证照实例附件标识为空，随机生成一个（解决：C级证照第一次上传，不点击保存，直接关闭，附件会丢失）
                                String newCliengGuid = certInfo.getCertcliengguid();
                                if (StringUtil.isBlank(newCliengGuid)) {
                                    String cliengguid = UUID.randomUUID().toString();
                                    certInfo.setCertcliengguid(cliengguid);
                                    iCertInfoExternal.submitCertInfo(certInfo, null, ZwdtConstant.MATERIALTYPE_CERT,
                                            ZwdtConstant.CERTAPPKEY);
                                }
                                certJson.put("clientguid", newCliengGuid);
                                // 5.3、获取证照实例对应的封面图片，证照实例是否有对应的附件
                                String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                        + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                                String coverClentGuid = certCatalog.getGraycliengguid();// 证照封面业务标识：默认为灰色封面
                                String coverAttachGuid = ""; // 证照封面附件标识
                                String isUploadAttach = "0"; // 判断证照实例是否有附件  0：无 1：有
                                String clientGuid = certInfo.getCertcliengguid();// 证照实例对应的附件标识
                                if (StringUtil.isNotBlank(clientGuid)) {
                                    List<JSONObject> attachList = iCertAttachExternal
                                            .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                    
                                    List<FrameAttachInfo>  attachs = iAttachService.getAttachInfoListByGuid(clientGuid);
                                    if(attachs != null && attachs.size() > 0) {
                                    	certJson.put("sharematerialiguid", attachs.get(0).getAttachGuid()); // 证照实例标识
                                    }
                                    
                                    if (attachList != null && attachList.size() > 0) {
                                        // 5.3.1、如果证照实例有附件，则显示彩色的封面
                                        coverClentGuid = certCatalog.getColourcliengguid();
                                        isUploadAttach = "1";
                                    }
                                }
                                // 5.3.2、获取证照实例对应的附件
                                if (StringUtil.isNotBlank(coverClentGuid)) {
                                    List<JSONObject> attachList = iCertAttachExternal.getAttachList(coverClentGuid,
                                            ZwdtConstant.CERTAPPKEY);
                                    if (attachList != null && attachList.size() > 0) {
                                        coverAttachGuid = attachList.get(0).getString("attachguid");
                                        coverSrc = WebUtil.getRequestCompleteUrl(request)
                                                + "/rest/zwdtAttach/readCertAttach?attachguid=" + coverAttachGuid
                                                + "&filename=" + attachList.get(0).getString("attachname");
                                    }
                                }
                                certJson.put("coversrc", coverSrc);
                                certJson.put("type", isUploadAttach);
                                // 5.3.3、C\D级别证照只要有证照实例就可显示，A\B证照需要证照实例有对应的附件才能显示
                                String materialLevel = certInfo.getCertlevel();
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(isUploadAttach)) {
                                    // 5.3.3.2、A/B级证照需要证照实例有对应的附件才能显示
                                    if (ZwdtConstant.CERTLEVEL_A.equals(materialLevel)) {
                                        gardeACertnum++;
                                        gradeACertList.add(certJson);
                                    }
                                    else if (ZwdtConstant.CERTLEVEL_B.equals(materialLevel)) {
                                        gardeBCertnum++;
                                        gradeBCertList.add(certJson);
                                    }
                                }
                            }
                        }
                    }
                    /*//添加大数据相关的证照文件
                    List<FrameAttachInfo> attachinfos =  iJnProjectService.getFrameAttachByIdenumberTag(auditOnlineIndividual.getIdnumber(),"大数据电子证照");
                    if (attachinfos != null && attachinfos.size() > 0) {
                    	for (FrameAttachInfo attachinfo : attachinfos) {
                    		JSONObject certJson = new JSONObject();// 证照
                            certJson.put("certname", attachinfo.getAttachFileName()); // 证照名称
                            certJson.put("sharematerialiguid", attachinfo.getAttachGuid()); // 证照实例标识
                            certJson.put("certsource", attachinfo.getCliengTag());// 证照来源                        
                            certJson.put("notice", "");
                            certJson.put("clientguid", attachinfo.getCliengGuid());
                            certJson.put("coversrc", WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/zwdtAttach/readCertAttach?attachguid=" + attachinfo.getAttachGuid()
                                    + "&filename=" + attachinfo.getAttachFileName());
                            certJson.put("type", "1");
                            String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                            certJson.put("coversrc", coverSrc);
                            gardeBCertnum++;
                            gradeBCertList.add(certJson);
                    	}
                    }
                    
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.eq("isactivated", "1");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    if (auditRsCompanyBaseinfoList != null && auditRsCompanyBaseinfoList.size() > 0) {
                    	for (AuditRsCompanyBaseinfo company : auditRsCompanyBaseinfoList) {
                    		 //添加大数据相关的证照文件
                            List<FrameAttachInfo> attachinfoss =  iJnProjectService.getFrameAttachByIdenumberTag(company.getCreditcode(),"大数据电子证照");
                            if (attachinfoss != null && attachinfoss.size() > 0) {
                            	for (FrameAttachInfo attachinfo : attachinfoss) {
                            		JSONObject certJson = new JSONObject();// 证照
                                    certJson.put("certname", attachinfo.getAttachFileName()); // 证照名称
                                    certJson.put("sharematerialiguid", attachinfo.getAttachGuid()); // 证照实例标识
                                    certJson.put("certsource", attachinfo.getCliengTag());// 证照来源                        
                                    certJson.put("notice", "");
                                    certJson.put("clientguid", attachinfo.getCliengGuid());
                                    certJson.put("coversrc", WebUtil.getRequestCompleteUrl(request)
                                            + "/rest/zwdtAttach/readCertAttach?attachguid=" + attachinfo.getAttachGuid()
                                            + "&filename=" + attachinfo.getAttachFileName());
                                    certJson.put("type", "1");
                                    String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                            + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                                    certJson.put("coversrc", coverSrc);
                                    gardeBCertnum++;
                                    gradeBCertList.add(certJson);
                            	}
                            }
                    	}
                    }*/
                    
                    
                    
                    // 6、获取所有C级证照样本
                    List<JSONObject> gradeCCertList = new ArrayList<JSONObject>(); // C级证照信息
                    int gardeCCertnum = 0;// C级证照数量
                    String certCatalogids = ""; // 允许C级证照上传的所有目录标识
                    List<CertCatalog> certCatalogCList = iCertConfigExternal.selectUploadLevelCCertCatalog(certType,"",
                            ZwdtConstant.CERTAPPKEY,"");
                    if (certCatalogCList != null && certCatalogCList.size() > 0) {
                        for (CertCatalog certCatalogC : certCatalogCList) {
                            certCatalogids += certCatalogC.getCertcatalogid() + ";";
                        }
                    }
                    // 6.1、获取所有允许C级证照上传的证照样本下的所有证照实例
                    String havaInfoCertCatalogids = ""; // 有实例的证照目录标识
                    if (StringUtil.isNotBlank(certCatalogids)) {
                        List<CertInfo> certInfoCList = iCertInfoExternal.selectCertByOwner(certCatalogids,
                                ZwdtConstant.CERTOWNERTYPE_PERSON, "", auditOnlineIndividual.getIdnumber(), false,
                                ZwdtConstant.CERTAPPKEY, null);
                        if (certInfoCList != null && certInfoCList.size() > 0) {
                            Collections.sort(certInfoCList, new Comparator<CertInfo>()
                            {
                                @Override
                                public int compare(CertInfo o1, CertInfo o2) {
                                    return o1.getCertlevel().compareTo(o2.getCertlevel());
                                }
                            });
                            for (CertInfo certInfoC : certInfoCList) {
                                // 6.1.1、只需要判断C级证照
                                if (ZwdtConstant.CERTLEVEL_C.equals(certInfoC.getCertlevel())) {
                                    // 6.1.2、如果有对应的C级证照实例，则在所有允许上传的C级证照实例中去除，剩余的证照目录单独返回
                                    certCatalogids = certCatalogids.replace(certInfoC.getCertcatalogid() + ";", "");
                                    CertCatalog certCatalogC = iCertConfigExternal.getCatalogByCatalogid(
                                            certInfoC.getCertcatalogid(), ZwdtConstant.CERTAPPKEY);
                                    JSONObject certJsonC = new JSONObject();// 证照
                                    certJsonC.put("certname", certCatalogC.getCertname()); // 证照名称
                                    certJsonC.put("sharematerialiguid", certInfoC.getRowguid()); // 证照实例标识
                                    String source = certInfoC.getCertawarddept();
                                    if (StringUtil.isBlank(source)) {
                                        source = ZwdtConstant.MATERIALSOURCE_SP.equals(certInfoC.getMaterialsource())
                                                ? "自制" : "其他";
                                    }
                                    certJsonC.put("certsource", source);// 证照来源                                        
                                    // 6.1.2.1、如果证照实例附件标识为空，随机生成一个（解决：C级证照第一次上传，不点击保存，直接关闭，附件会丢失）
                                    String newCliengGuid = certInfoC.getCertcliengguid();
                                    if (StringUtil.isBlank(newCliengGuid)) {
                                        String cliengguid = UUID.randomUUID().toString();
                                        certInfoC.setCertcliengguid(cliengguid);
                                        iCertInfoExternal.submitCertInfo(certInfoC, null,
                                                ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                    }
                                    certJsonC.put("clientguid", newCliengGuid);
                                    // 6.1.2.2、获取证照实例对应的封面图片，证照实例是否有对应的附件
                                    String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                            + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                                    String coverClentGuid = certCatalogC.getGraycliengguid();// 证照封面业务标识：默认为灰色封面
                                    String coverAttachGuid = ""; // 证照封面附件标识
                                    String isUploadAttach = "0"; // 判断证照实例是否有附件  0：无 1：有
                                    String clientGuid = certInfoC.getCertcliengguid();// 证照实例对应的附件标识
                                    if (StringUtil.isNotBlank(clientGuid)) {
                                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(clientGuid,
                                                ZwdtConstant.CERTAPPKEY);
                                        if (attachList != null && attachList.size() > 0) {
                                            // 6.1.2.2.1、如果证照实例有附件，则显示彩色的封面
                                            coverClentGuid = certCatalogC.getColourcliengguid();
                                            isUploadAttach = "1";
                                        }
                                    }
                                    // 6.1.2.2.2、获取证照实例对应的附件
                                    if (StringUtil.isNotBlank(coverClentGuid)) {
                                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(coverClentGuid,
                                                ZwdtConstant.CERTAPPKEY);
                                        if (attachList != null && attachList.size() > 0) {
                                            coverAttachGuid = attachList.get(0).getString("attachguid");
                                            coverSrc = WebUtil.getRequestCompleteUrl(request)
                                                    + "/rest/zwdtAttach/readCertAttach?attachguid=" + coverAttachGuid
                                                    + "&filename=" + attachList.get(0).getString("attachname");
                                        }
                                    }
                                    certJsonC.put("coversrc", coverSrc);
                                    certJsonC.put("type", isUploadAttach);
                                    gardeCCertnum++;
                                    gradeCCertList.add(certJsonC);
                                }
                                if (ZwdtConstant.CERTLEVEL_B.equals(certInfoC.getCertlevel())
                                        || ZwdtConstant.CERTLEVEL_A.equals(certInfoC.getCertlevel())) {
                                    havaInfoCertCatalogids += certInfoC.getCertcatalogid() + ";"; // 拼接有实例的证照目录标识
                                }
                            }
                        }
                    }
                    // 7、将剩余的允许C级上传的证照目录新增返回
                    if (StringUtil.isNotBlank(certCatalogids)) {
                        String[] certCatalogidArr = certCatalogids.split(";");
                        for (int i = 0; i < certCatalogidArr.length; i++) {
                            String certCatalogid = certCatalogidArr[i];
                            CertCatalog certCatalogC = iCertConfigExternal.getCatalogByCatalogid(certCatalogid,
                                    ZwdtConstant.CERTAPPKEY);
                            if (certCatalogC != null) {
                                String singleCertInfo = String.valueOf(certCatalogC.getIsoneCertInfo()); // 证照目录是否对应单实例 TODO
                                boolean isNeedShowCatalog = true; // 是否在C级展示目录
                                if (ZwfwConstant.CONSTANT_STR_ZERO.equals(singleCertInfo)) {
                                    if (havaInfoCertCatalogids.contains(certCatalogid)) {
                                        isNeedShowCatalog = false;
                                    }
                                }
                                if (isNeedShowCatalog) {
                                    JSONObject certJsonC = new JSONObject();// 证照                                
                                    certJsonC.put("certname", certCatalogC.getCertname()); // 证照名称
                                    certJsonC.put("sharematerialiguid", UUID.randomUUID()); // 证照实例标识                         
                                    certJsonC.put("certsource", "自制");// 证照来源（C级无证照来源）
                                    certJsonC.put("type", "0");
                                    String coverClentGuid = certCatalogC.getGraycliengguid(); //无实例时判断是否有灰色封面                            
                                    String coverAttachGuid = ""; // 证照封面附件标识
                                    String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                            + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                                    if (StringUtil.isNotBlank(coverClentGuid)) {
                                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(coverClentGuid,
                                                ZwdtConstant.CERTAPPKEY);
                                        if (attachList != null && attachList.size() > 0) {
                                            coverAttachGuid = attachList.get(0).getString("attachguid");
                                            coverSrc = WebUtil.getRequestCompleteUrl(request)
                                                    + "/rest/zwdtAttach/readCertAttach?attachguid=" + coverAttachGuid
                                                    + "&filename=" + attachList.get(0).getString("attachname");
                                        }
                                    }
                                    certJsonC.put("coversrc", coverSrc);
                                    certJsonC.put("certcatalogid", certCatalogC.getCertcatalogid());
                                    gardeCCertnum++;
                                    gradeCCertList.add(certJsonC);
                                }
                            }
                        }
                    }
                    
                    // 7、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("gradea_certnum", gardeACertnum);
                    dataJson.put("gradea_certs", gradeACertList);
                    dataJson.put("gradeb_certnum", gardeBCertnum);
                    dataJson.put("gradeb_certs", gradeBCertList);
                    dataJson.put("gradec_certnum", gardeCCertnum);
                    dataJson.put("gradec_certs", gradeCCertList);
                    log.info("=======结束调用getCertListByType接口=======");
                    return JsonUtils.zwdtRestReturn("1", "证照分类下的证照样本列表获取成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else

            {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertListByType接口参数：params【" + params + "】=======");
            log.info("=======getCertListByType异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "证照分类下的证照样本列表获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取证照实例详细信息的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getCertDetail", method = RequestMethod.POST)
    public String getCertDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertDetail接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, shareMaterialIGuid,
                            ZwdtConstant.USERVALID_CENT_INFO)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取证照实例信息
                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (certInfo != null) {
                    String certSrc = WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/credentials/images/view.png"; // 证照封面图片地址    
                    String[] contenttypes = {".jpeg", ".jpg", ".gif", ".png", ".bmp", ".JPG" };
                    String contenttype = iConfigService.getFrameConfigValue("showPhotoViewFormat");
                    if (StringUtil.isNotBlank(contenttype)) {
                        contenttypes = contenttype.split(";");
                    }
                    // 3.1、获取证照实例对应的附件信息，返回
                    if (StringUtil.isNotBlank(certInfo.getSltimagecliengguid())) {
                        // 3.1.1、优先获取缩略图内容
                        List<JSONObject> attachList = iCertAttachExternal
                                .getAttachList(certInfo.getSltimagecliengguid(), ZwdtConstant.CERTAPPKEY);
                        if (attachList != null && attachList.size() > 0) {
                            String attachGuid = attachList.get(0).getString("attachguid");
                            certSrc = WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/zwdtAttach/readCertAttach?attachguid=" + attachGuid + "&filename="
                                    + attachList.get(0).getString("attachname");
                        }
                    }
                    else {
                        // 3.1.2、如果缩略图内容没有，则展示证照内容
                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                ZwdtConstant.CERTAPPKEY);
                        if (attachList != null && attachList.size() > 0) {
                            String attachGuid = attachList.get(0).getString("attachguid");
                            JSONObject attachJson = attachList.get(0);
                            String attachfilename = attachJson.get("attachname").toString();
                            // 3.1.3、如果证照内容不是图片，则展示默认图片内容
                            if (Arrays.asList(contenttypes)
                                    .contains(attachfilename.substring(attachfilename.lastIndexOf(".")))) {
                                certSrc = WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/zwdtAttach/readCertAttach?attachguid=" + attachGuid + "&filename="
                                        + attachfilename;
                            }
                        }
                    }
                    dataJson.put("certsrc", certSrc);
                    // 3.2、获取证照照面信息字段并返回
                    List<JSONObject> certJsonList = new ArrayList<JSONObject>();
                    // 3.2.1、获取照面信息所有字段的JSON
                    List<CertMetadata> certMetadataList = iCertConfigExternal.selectMetadataByIdAndVersion(
                            certInfo.getCertcatalogid(), certInfo.getCertcatalogversion().toString(),
                            ZwdtConstant.CERTAPPKEY);
                    CertInfoExtension certInfoExtension = iCertInfoExternal
                            .getCertExtenByCertInfoGuid(certInfo.getRowguid());
                    for (int i = 0; i < certMetadataList.size(); i++) {
                        CertMetadata certMetadata = certMetadataList.get(i);
                        String fieldDisplayType = certMetadata.getFielddisplaytype();
                        String dataSourceCodename = certMetadata.getDatasource_codename();
                        JSONObject dataTable = new JSONObject();
                        String mainFieldName = certMetadata.getFieldname();
                        if (StringUtil.isNotBlank(mainFieldName)) {
                            mainFieldName = mainFieldName.toLowerCase();
                        }
                        dataTable.put("fieldname", certMetadata.getFieldchinesename());// 照面信息字段值
                        // 3.2.2、首先返回主元数据的值
                        if (ZwfwConstant.CONSTANT_STR_ZERO.equals(certMetadata.getIsrelatesubtable())
                                && StringUtil.isBlank(certMetadata.getParentguid())) {
                            if (certInfoExtension != null) {
                                // 单选框组代码项转换
                                if (ZwdtConstant.WIDGET_REDIOBUTLIST.equals(fieldDisplayType)
                                        || ZwdtConstant.WIDGET_CHECKBOXLIST.equals(fieldDisplayType)) {
                                    dataTable.put("fieldvalue", iCodeItemsService.getItemTextByCodeName(
                                            dataSourceCodename, certInfoExtension.get(mainFieldName)));
                                    if (StringUtil.isBlank(certInfoExtension.get(mainFieldName))) {
                                        dataTable.put("fieldvalue", "");
                                    }
                                }
                                // 复选框代码项转换
                                else if (ZwdtConstant.WIDGET_CHECKBOX.equals(fieldDisplayType)) {
                                    String fieldValue = certInfoExtension.get(mainFieldName);
                                    if (StringUtil.isBlank(fieldValue)) {
                                        dataTable.put("fieldvalue", "");
                                    }
                                    else {
                                        if ("true".equals(fieldValue)) {
                                            dataTable.put("fieldvalue", "是");
                                        }
                                        else {
                                            dataTable.put("fieldvalue", "否");
                                        }
                                    }
                                }
                                // 文件上传
                                else if ("webuploader".equals(fieldDisplayType)) {
                                    // 如果照面信息是图片 则将图片的名字返回。
                                    String clientGuid = certInfoExtension.get(mainFieldName);
                                    List<FrameAttachInfo> attachinfolist = iAttachService
                                            .getAttachInfoListByGuid(clientGuid);
                                    // 如果本地库中没有附件，则去独立证照库中查询一下
                                    if (attachinfolist == null || attachinfolist.size() == 0) {
                                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(clientGuid,
                                                ZwdtConstant.CERTAPPKEY);
                                        for (JSONObject json : attachList) {
                                            //独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                            AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), clientGuid,
                                                    json.getString("attachname"), json.getString("contentype"),
                                                    json.getString("attachstorageguid"), json.getLong("size"),
                                                    iCertAttachExternal.getAttach(json.getString("attachguid"), ""), "",
                                                    "");
                                        }
                                    }
                                    attachinfolist = iAttachService.getAttachInfoListByGuid(clientGuid);
                                    // 返回附件信息。
                                    if (attachinfolist != null && attachinfolist.size() > 0) {
                                        dataTable.put("fieldvalue", attachinfolist.get(0).getAttachFileName());
                                        dataTable.put("attachguid", attachinfolist.get(0).getAttachGuid());
                                        dataTable.put("isImage", "isImage");
                                    }
                                }
                                else {
                                    dataTable.put("fieldvalue", certInfoExtension.get(mainFieldName));
                                    if (StringUtil.isBlank(certInfoExtension.get(mainFieldName))) {
                                        dataTable.put("fieldvalue", "");
                                    }
                                    // 如果是日期类型的数据，去除无用的时分秒
                                    else if (certInfoExtension.get(mainFieldName).toString()
                                            .indexOf("00:00:00") != -1) {
                                        String tempDateString = certInfoExtension.get(mainFieldName.toString());
                                        dataTable.put("fieldvalue", tempDateString.replaceAll("00:00:00", ""));
                                    }
                                }
                            }
                            else {
                                dataTable.put("fieldvalue", "");
                            }
                            dataTable.put("isparent", 0);
                            certJsonList.add(dataTable);
                        }

                        // 3.2.3、返回关联子元数据的字段和值
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(certMetadata.getIsrelatesubtable())) {
                            JSONArray jsonArray = null;
                            if (certInfoExtension != null) {
                                jsonArray = JSONArray.parseArray(certInfoExtension.getStr(mainFieldName));
                            }
                            if (jsonArray != null) {
                                for (int n = 0; n < jsonArray.size(); n++) {
                                    JSONObject subdataTable = new JSONObject();
                                    subdataTable.put("isparent", 1);
                                    subdataTable.put("fieldname", certMetadata.getFieldchinesename());// 照面信息字段值
                                    List<CertMetadata> certMetadatas = iCertConfigExternal
                                            .selectSubDispinListByCertguid(certMetadata.getRowguid());
                                    if (certMetadatas != null && certMetadatas.size() > 0) {
                                        List<JSONObject> certJsonSubList = new ArrayList<JSONObject>();
                                        for (CertMetadata certMetadataSub : certMetadatas) {
                                            JSONObject subDataTable = new JSONObject();
                                            subDataTable.put("fieldname", certMetadataSub.getFieldchinesename());
                                            JSONObject object = jsonArray.getJSONObject(n);
                                            String fieldName = certMetadataSub.getFieldname();
                                            if (StringUtil.isNotBlank(fieldName)) {
                                                fieldName = fieldName.toLowerCase();
                                            }
                                            String subFieldDisplayType = certMetadataSub.getFielddisplaytype();
                                            String subDatasourceCodename = certMetadataSub.getDatasource_codename();
                                            // 单选框组代码项转换
                                            if (ZwdtConstant.WIDGET_REDIOBUTLIST.equals(subFieldDisplayType)
                                                    || ZwdtConstant.WIDGET_CHECKBOXLIST.equals(subFieldDisplayType)
                                                    || ZwdtConstant.WIDGET_COMBOBOX.equals(subFieldDisplayType)) {
                                                subDataTable.put("fieldvalue", iCodeItemsService.getItemTextByCodeName(
                                                        subDatasourceCodename, object.get(fieldName).toString()));
                                                if (StringUtil.isBlank(object.get(fieldName))) {
                                                    subDataTable.put("fieldvalue", "");
                                                }
                                            }
                                            // 复选框代码项转换
                                            else if (ZwdtConstant.WIDGET_CHECKBOX.equals(subFieldDisplayType)) {
                                                String fieldValue = object.get(fieldName).toString();
                                                if (StringUtil.isBlank(fieldValue)) {
                                                    subDataTable.put("fieldvalue", "");
                                                }
                                                else {
                                                    if ("true".equals(fieldValue)) {
                                                        subDataTable.put("fieldvalue", "是");
                                                    }
                                                    else {
                                                        subDataTable.put("fieldvalue", "否");
                                                    }
                                                }
                                            }
                                            // 文件上传
                                            else if ("webuploader".equals(fieldDisplayType)) {
                                                // 如果照面信息是图片 则将图片的名字返回。
                                                System.out.println("");
                                                List<FrameAttachInfo> attachinfolist = iAttachService
                                                        .getAttachInfoListByGuid(certInfoExtension.get(mainFieldName));
                                                if (attachinfolist != null && attachinfolist.size() > 0) {
                                                    dataTable.put("fieldvalue",
                                                            attachinfolist.get(0).getAttachFileName());
                                                    dataTable.put("attachguid", attachinfolist.get(0).getAttachGuid());
                                                    dataTable.put("isImage", "isImage");
                                                }
                                            }
                                            else {
                                                subDataTable.put("fieldvalue", object.get(fieldName));
                                                if (StringUtil.isBlank(object.get(fieldName))) {
                                                    subDataTable.put("fieldvalue", "");
                                                }
                                                else if (object.get(fieldName).toString().indexOf("00:00:00") != -1) {
                                                    String tempDateString = object.get(fieldName).toString();
                                                    subDataTable.put("fieldvalue",
                                                            tempDateString.replaceAll("00:00:00", ""));
                                                }
                                            }
                                            certJsonSubList.add(subDataTable);
                                        }
                                        subdataTable.put("fieldvalue", certJsonSubList);
                                        certJsonList.add(subdataTable);
                                    }
                                }
                            }
                        }
                    }
                    // 3.2.4、对返回的json列表进行排序，没有子表的显示在上方    
                    if (certJsonList != null && certJsonList.size() > 0) {
                        Collections.sort(certJsonList, new Comparator<JSONObject>()
                        {
                            @Override
                            public int compare(JSONObject o1, JSONObject o2) {
                                return ((Integer) o1.get("isparent")).compareTo((Integer) o2.get("isparent"));
                            }
                        });
                    }
                    dataJson.put("certdetailfields", certJsonList);
                }
                log.info("=======结束调用getCertDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "证照实例详细信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertDetail接口参数：params【" + params + "】=======");
            log.info("=======getCertDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "证照实例详细信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *   变更证照的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/updateCert", method = RequestMethod.POST)
    public String updateCert(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用updateCert接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            //变更证照
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取证照实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.2、获取证照实例对应的附件标识
                String cliengGuid = obj.getString("cliengguid");
                // 1.3、获取证照版本唯一标识
                String certCatalogid = obj.getString("certcatalogid");
                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, shareMaterialIGuid,
                            ZwdtConstant.USERVALID_CENT_INFO)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 2、获取政务服务个人用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                    if (certInfo != null) {
                        // 3、如果存在证照实例，更新证照实例附件标识
                        certInfo.setCertcliengguid(cliengGuid);
                        shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                        CertInfo certInfo2 = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                        if (StringUtil.isNotBlank(certInfo2)) {
                            cliengGuid = certInfo2.getCertcliengguid();
                        }
                    }
                    else {
                        // 4、如果不存在证照实例，则新增证照实例（仅C级证照存在此情况）
                        // 4.1、获取对应的证照目录
                        String certName = "";
                        CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certCatalogid,
                                ZwdtConstant.CERTAPPKEY);
                        if (StringUtil.isNotBlank(certCatalog)) {
                            certName = certCatalog.getCertname();
                        }
                        CertInfo newCertInfo = new CertInfo();
                        newCertInfo.setCertname(certName); //证照名称
                        newCertInfo.setRowguid(shareMaterialIGuid); //证照实例唯一标识
                        newCertInfo.setOperatedate(new Date()); //操作日期
                        newCertInfo.setCertcatalogid(certCatalogid); //证照目录版本唯一标识
                        newCertInfo.setCertcliengguid(cliengGuid); //附件guid
                        newCertInfo.setCertownertype(ZwdtConstant.CERTOWNERTYPE_PERSON); //持有人类型
                        newCertInfo.setCertownerno(auditOnlineIndividual.getIdnumber()); //持有人证件号码
                        newCertInfo.setCertownername(auditOnlineIndividual.getClientname()); //持有人姓名
                        newCertInfo.setMaterialtype(ZwdtConstant.MATERIALTYPE_CERT); //材料类别
                        newCertInfo.setCertlevel(ZwdtConstant.CERTLEVEL_C); //证照等级
                        newCertInfo.setMaterialsource(ZwdtConstant.MATERIALSOURCE_SP);//证照来源
                        newCertInfo.setCertownercerttype(ZwdtConstant.CERTOWNERCERTTYPE_ID);//持有人证件类型
                        iCertInfoExternal.submitCertInfo(newCertInfo, null, ZwdtConstant.MATERIALTYPE_CERT,
                                ZwdtConstant.CERTAPPKEY);
                        shareMaterialIGuid = newCertInfo.getRowguid();
                        CertInfo certInfo2 = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                        if (StringUtil.isNotBlank(certInfo2)) {
                            cliengGuid = certInfo2.getCertcliengguid();
                        }
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("sharematerialiguid", shareMaterialIGuid);
                dataJson.put("cliengGuid", cliengGuid);
                log.info("=======结束调用updateCert接口=======");
                return JsonUtils.zwdtRestReturn("1", "变更证照成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateCert接口参数：params【" + params + "】=======");
            log.info("=======updateCert异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "变更证照失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取证照实例对应附件列表信息接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getCertAttachList", method = RequestMethod.POST)
    public String getCertAttachList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertAttachList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取证照实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, shareMaterialIGuid,
                            ZwdtConstant.USERVALID_CENT_INFO)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                List<JSONObject> attachList = new ArrayList<JSONObject>();
                // 2、获取证照实例信息
                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                if (certInfo != null) {
                    String clientGuid = certInfo.getCertcliengguid();
                    if (StringUtil.isNotBlank(clientGuid)) {
                        // 3、获取证照实例对应的附件信息
                        List<JSONObject> attachInfoList = iCertAttachExternal.getAttachList(clientGuid,
                                ZwdtConstant.CERTAPPKEY);
                        if (attachInfoList != null && attachInfoList.size() > 0) {
                            for (JSONObject attachInfo : attachInfoList) {
                                String attachGuid = attachInfo.getString("attachguid");
                                JSONObject attachJson = new JSONObject();
                                attachJson.put("attachguid", attachGuid);
                                attachJson.put("attachfilename", attachInfo.get("attachname"));
                                attachJson.put("attachsrc",
                                        WebUtil.getRequestCompleteUrl(request)
                                                + "/rest/zwdtAttach/readCertAttach?attachguid=" + attachGuid
                                                + "&filename=" + attachInfo.get("attachname"));
                                attachList.add(attachJson);
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("attachlist", attachList);
                log.info("=======结束调用getCertAttachList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取证照实例对应附件列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertAttachList接口参数：params【" + params + "】=======");
            log.info("=======getCertAttachList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取证照实例对应附件列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的证照的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getMyCertList", method = RequestMethod.POST)
    public String getMyCertList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCertList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取政务服务个人用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取用户拥有的所有证照实例信息
                    List<CertInfo> certInfoList = iCertInfoExternal.selectCertInfo(ZwdtConstant.CERTOWNERTYPE_PERSON,
                            auditOnlineIndividual.getIdnumber(), "", "", ZwdtConstant.MATERIALTYPE_CERT,
                            ZwdtConstant.CERTAPPKEY, false,"", null);
                    int size = 0;
                    // 3.1、按照证照级别排序 A-B-C-D
                    if (certInfoList != null && certInfoList.size() > 0) {
                        size = certInfoList.size();
                        Collections.sort(certInfoList, new Comparator<CertInfo>()
                        {
                            @Override
                            public int compare(CertInfo o1, CertInfo o2) {
                                return o1.getCertlevel().compareTo(o2.getCertlevel());
                            }
                        });
                    }
                    List<JSONObject> certList = new ArrayList<JSONObject>();
                    // 4、大厅默认显示4个有图片的
                    int certCount = 0; //证照个数   
                    for (int i = 0; i < size; i++) {
                        // 5、证照实例必须有证照样本才显示
                        CertInfo certInfo = certInfoList.get(i);
                        CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certInfo.getCertcatalogid(),
                                ZwdtConstant.CERTAPPKEY);
                        if (certCatalog != null) {
                            JSONObject certJson = new JSONObject();
                            certJson.put("certname", certInfo.getCertname());// 证照名称
                            certJson.put("certguid", certInfo.getRowguid());// 证照标识
                            String source = certInfo.getCertawarddept();
                            if (StringUtil.isBlank(source)) {
                                source = ZwdtConstant.MATERIALSOURCE_SP.equals(certInfo.getMaterialsource()) ? "自制"
                                        : "其他";
                            }
                            certJson.put("certsource", source);// 证照来源
                            // 6、获取证照实例对应的封面图片，证照实例是否有对应的附件
                            String coverClentGuid = certCatalog.getGraycliengguid();// 证照封面业务标识：默认为灰色封面
                            String coverAttachGuid = ""; // 证照封面附件标识
                            String coverSrc = WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwmhwz/pages/myspace/images/nopicture.png";// 证照封面图片地址
                            String isUploadAttach = "0"; // 判断证照实例是否有附件  0：无 1：有
                            String clientGuid = certInfo.getCertcliengguid();// 证照实例对应的附件标识      
                            List<JSONObject> attachList = null;
                            if (StringUtil.isNotBlank(clientGuid)) {
                                attachList = iCertAttachExternal.getAttachList(clientGuid, ZwdtConstant.CERTAPPKEY);
                                if (attachList != null && attachList.size() > 0) {
                                    // 6.1、如果证照实例有附件，则显示彩色的封面
                                    coverClentGuid = certCatalog.getColourcliengguid();
                                    isUploadAttach = "1";
                                }
                            }
                            // 6.2、将封面图片内容拼接地址返回前台
                            if (StringUtil.isNotBlank(coverClentGuid)) {
                                List<JSONObject> coverAttachList = iCertAttachExternal.getAttachList(coverClentGuid,
                                        ZwdtConstant.CERTAPPKEY);
                                if (coverAttachList != null && coverAttachList.size() > 0) {
                                    coverAttachGuid = coverAttachList.get(0).getString("attachguid");
                                    coverSrc = WebUtil.getRequestCompleteUrl(request)
                                            + "/rest/zwdtAttach/readCertAttach?attachguid=" + coverAttachGuid
                                            + "&filename=" + coverAttachList.get(0).getString("attachname");
                                }
                            }
                            certJson.put("certcontent", coverSrc);
                            // 6.3、判断证照级别返回
                            String materialLevel = certInfo.getCertlevel();
                            if (ZwdtConstant.CERTLEVEL_A.equals(materialLevel)) {
                                certJson.put("certlevel", 1);
                            }
                            else if (ZwdtConstant.CERTLEVEL_B.equals(materialLevel)) {
                                certJson.put("certlevel", 2);
                            }
                            else if (ZwdtConstant.CERTLEVEL_C.equals(materialLevel)
                                    || ZwdtConstant.CERTLEVEL_D.equals(materialLevel)) {
                                certJson.put("certlevel", 3);
                            }
                            // 6.4、C/D级证照只要有证照实例就可以显示                           
                            if (ZwdtConstant.CERTLEVEL_C.equals(materialLevel)
                                    || ZwdtConstant.CERTLEVEL_D.equals(materialLevel)) {
                                if (attachList != null && attachList.size() > 0) {
                                    certCount++;
                                }
                                if (certList.size() < 4) {
                                    certList.add(certJson);
                                }
                            }
                            // 6.5、A/B级证照需要证照实例有对应的附件才能显示
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isUploadAttach)) {
                                if (ZwdtConstant.CERTLEVEL_A.equals(materialLevel)
                                        || ZwdtConstant.CERTLEVEL_B.equals(materialLevel)) {
                                    if (attachList != null && attachList.size() > 0) {
                                        certCount++;
                                    }
                                    if (certList.size() < 4) {
                                        certList.add(certJson);
                                    }
                                }
                            }
                        }
                    }
                    
                    /*//添加大数据相关的证照文件
                    List<FrameAttachInfo> attachinfos =  iJnProjectService.getFrameAttachByIdenumberTag(auditOnlineIndividual.getIdnumber(),"大数据电子证照");
                    if (attachinfos != null && attachinfos.size() > 0) {
                    	certCount += attachinfos.size();
                    }
                    
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.eq("isactivated", "1");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    if (auditRsCompanyBaseinfoList != null && auditRsCompanyBaseinfoList.size() > 0) {
                    	for (AuditRsCompanyBaseinfo company : auditRsCompanyBaseinfoList) {
                    		 //添加大数据相关的证照文件
                            List<FrameAttachInfo> attachinfoss =  iJnProjectService.getFrameAttachByIdenumberTag(company.getCreditcode(),"大数据电子证照");
                            if (attachinfoss != null && attachinfoss.size() > 0) {
                            	certCount += attachinfoss.size();
                            }
                    	}
                    }*/
                    // 7、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("certlist", certList);
                    dataJson.put("certcount", certCount);
                    log.info("=======结束调用getMyCertList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的证照成功", dataJson.toString());
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
            log.info("=======getMyCertList接口参数：params【" + params + "】=======");
            log.info("=======getMyCertList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的证照失败：" + e.getMessage(), "");
        }
    }

    /**
     *   更新证照封面的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/updateCertCover", method = RequestMethod.POST)
    public String updateCertCover(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用updateCertCover接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取证照实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.2、获取是否存在附件标识
                String isExist = obj.getString("isExist");
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, shareMaterialIGuid,
                            ZwdtConstant.USERVALID_CENT_INFO)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、定义JSON返回对象
                JSONObject dataJson = new JSONObject();
                String coverSrc = WebUtil.getRequestCompleteUrl(request)
                        + "/epointzwmhwz/pages/credentials/images/nopicture2.png"; // 证照封面图片地址
                if (StringUtil.isNotBlank(shareMaterialIGuid)) {
                    // 3、获取证照实例
                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                    if (certInfo != null) {
                        // 4、获取证照目录
                        CertCatalog certCatalog = iCertConfigExternal.getCatalogByCatalogid(certInfo.getCertcatalogid(),
                                ZwdtConstant.CERTAPPKEY);
                        // 4.1、获取证照实例对应的封面图片，证照实例是否有对应的附件              
                        String coverClentGuid = certCatalog.getGraycliengguid();// 证照封面业务标识：默认为灰色封面
                        String coverAttachGuid = ""; // 证照封面附件标识
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(isExist)) {
                            coverClentGuid = certCatalog.getColourcliengguid();
                        }
                        // 4.2、获取证照实例对应的附件
                        if (StringUtil.isNotBlank(coverClentGuid)) {
                            List<JSONObject> attachList = iCertAttachExternal.getAttachList(coverClentGuid,
                                    ZwdtConstant.CERTAPPKEY);
                            if (attachList != null && attachList.size() > 0) {
                                coverAttachGuid = attachList.get(0).getString("attachguid");
                                coverSrc = WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/zwdtAttach/readCertAttach?attachguid=" + coverAttachGuid + "&filename="
                                        + attachList.get(0).getString("attachname");
                            }
                        }
                        dataJson.put("coversrc", coverSrc);
                    }
                }
                // 5、定义返回JSON对象              
                log.info("=======结束调用updateCertCover接口=======");
                return JsonUtils.zwdtRestReturn("1", "更新证照封面成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateCertCover接口参数：params【" + params + "】=======");
            log.info("=======updateCertCover异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "更新证照封面失败：" + e.getMessage(), "");
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
