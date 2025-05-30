package com.epoint.zwdt.zwdtrest.rs;

import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.TextSimilarity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditcollection.inter.IAuditOnlineCollection;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
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
import com.epoint.basic.auditsp.audityjscsb.domain.AuditYjsCsb;
import com.epoint.basic.auditsp.audityjscsb.inter.IAuditYjsCsbService;
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.faq.inter.IAuditTaskFaq;
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibrary;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.onlinecloud.cloudfiles.domain.AuditOnlineCloudFiles;
import com.epoint.cert.basic.onlinecloud.cloudfiles.inter.IAuditOnlineCloudFiles;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdtutil.ZwdtUserAuthValid;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;

/**
 *  人社对接接口
 * @作者 hlx
 * @version [F9.3.4, 2022年04月12日]
 */
@RestController
@RequestMapping("/projectrsdock")
public class ProjectRsDockController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    /**
     * 日志
     */
	public static final String WD_RS_URL= ConfigUtil.getConfigValue("datasyncjdbc", "WD_RS_URL");
	public static final String WD_PrivateKey= ConfigUtil.getConfigValue("datasyncjdbc", "WD_PrivateKey");

	
    /**
     *  获取人社token接口信息
     *  
     *  @param params 传入的参数
     *  @param request HTTP请求
     *  @return    
     */
    @RequestMapping(value = "/getRsToken", method = RequestMethod.POST)
    public String getRsToken(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getRsToken接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取辖区编码
                String areaCode = obj.getString("areacode");
                
                JSONObject data = new JSONObject();
                data.put("privatekey", WD_PrivateKey);
                data.put("xzqh",  areaCode);
                String reuslt = HttpUtil.doPostJson(WD_RS_URL+"/getToken", data.toString());
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("businessList", "");
                log.info("=======结束调用getRsToken接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取人社Token信息成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getRsToken接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取人社Token信息失败：" + e.getMessage(), "");
        }
    }
    
    
    
    /**
     *  获取获取单位基本信息接口
     *  
     *  @param params 传入的参数
     *  @param request HTTP请求
     *  @return    
     */
    @RequestMapping(value = "/getRsCompanyMessage", method = RequestMethod.POST)
    public String getRsCompanyMessage(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getRsCompanyMessage接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取业务类型
                String YWLX = "pullInfo";
                // 1.1、获取流水号
                String JYLSH = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                // 1.1、获取版本信息
                String SAFEINFO = "V1.0";
                // 1.1、操作名称
                String actionname = "publicGetCompanyInfo";
                // 1.1、令牌
                String rstoken = obj.getString("token");
                // 1.1、法人单位主键
                String aaz001 = obj.getString("certnum");
                // 1.1、地市代码
                String aab301 = obj.getString("areacode");
                
                JSONObject data = new JSONObject();
                JSONObject input = new JSONObject();
                input.put("actionname", actionname);
                input.put("token", rstoken);
                input.put("aaz001", aaz001);
                input.put("aab301", aab301);
                data.put("YWLX", YWLX);
                data.put("JYLSH",  JYLSH);
                data.put("SAFEINFO",  SAFEINFO);
                data.put("INPUT", input);
                String reuslt = HttpUtil.doPostJson(WD_RS_URL+"/pullInfo", data.toString());
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("businessList", "");
                log.info("=======结束调用getRsCompanyMessage接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取人社单位基本信息成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getRsCompanyMessage接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取人社单位基本信息失败：" + e.getMessage(), "");
        }
    }

    
}
