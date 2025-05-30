package com.epoint.zwdt.zwdtrest.cert.auditxmcert.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.controller.api.ApiBaseController;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.cert.auditxmcert.api.IAuditXmCertService;
import com.epoint.zwdt.zwdtrest.cert.auditxmcert.api.entity.AuditXmCert;

/**
 * 
 * [移动端项目证照接口]
 * 
 * @author 吕闯
 * @version 2022年6月13日
 */
@RequestMapping("xmcert")
@RestController
public class AuditXmCertRest extends ApiBaseController
{
    /*
     * 记录日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IAuditXmCertService iAuditXMCertService;
    
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    
    
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IAttachService iAttachService;

    /**
     * 
     * [根据证照类型获取登录人对应的所有项目]
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "getCertXmList", method = RequestMethod.POST)
    public String getCertXmList(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("====================开始调用getCertXmLsit接口=============================");
        try {
            // 1.解析入参
            JSONObject paramsObject = JSONObject.parseObject(params);
            // 2.校验token
            String checkToken = JsonUtils.checkUserAuth(paramsObject.getString("token"));
            if (StringUtil.isNotBlank(checkToken)) {
                return checkToken;
            }
            // 3.先校验是否登录
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister == null) {
                return JsonUtils.zwdtRestReturn("0", "用户未登录或者登录过期！", "");
            }
            // 4.获取具体参数
            JSONObject json = paramsObject.getJSONObject("params");
            String cert = json.getString("cert");
            if (StringUtil.isBlank(cert)) {
                return JsonUtils.zwdtRestReturn("0", "证照类型cert不能为空！", "");
            }
            
         // 3、获取用户身份是代办人或者管理者的企业ID
            SqlConditionUtil grantSqlConditionUtil = new SqlConditionUtil();
            grantSqlConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
            grantSqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
            grantSqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权： 0-否，1-是
            grantSqlConditionUtil.eq("m_isactive", "1"); // 代办人或管理者身份已激活
            List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                    .selectCompanyGrantByConditionMap(grantSqlConditionUtil.getMap()).getResult();
            String strWhereCompanyId = "";//拼接被授权的所有企业id
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
                //把拼接的where条件最后一个“,”去掉
                if (StringUtil.isNotBlank(strWhereCompanyId)) {
                    strWhereCompanyId = strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1);
                    //根据企业companyid查询出非历史版本被激活的企业的信用代码
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
            SqlConditionUtil conditionUtil = new SqlConditionUtil();
            conditionUtil.in("itemlegalcertnum", strInCreditCode);
            conditionUtil.isBlank("parentid");
            // 1.3、获取一页显示数量
            String pageSize = "500";
            // 1.4、获取当前页数
            String currentPage = "0";
            
            // 3、 获取项目信息
            PageData<AuditRsItemBaseinfo> auditRsItemBaseinfosPageData = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(),
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "", "")
                    .getResult();
            List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            List<JSONObject> xmList = new ArrayList<JSONObject>();
            
            if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
            	 for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                     // 根据项目标识和证照类型查询项目证照表
//                     sqlConditionUtil.clear();
//                     sqlConditionUtil.eq("itemguid", auditRsItemBaseinfo.getRowguid());
//                     sqlConditionUtil.eq("cert", cert);
//                     SQLManageUtil sqlManageUtil = new SQLManageUtil();
//                     String sql = sqlManageUtil.buildSqlComoplete(AuditXmCert.class, sqlConditionUtil.getMap());
//                     AuditXmCert auditXmCert = iAuditXMCertService.find(sql, "");
//                     if (auditXmCert != null) {
//                         JSONObject jsonObject = new JSONObject();
//                         jsonObject.put("itemguid", auditXmCert.getItemguid());
//                         jsonObject.put("itemname", auditRsItemBaseinfo.getItemname());
//                         jsonObject.put("itemcode", auditRsItemBaseinfo.getItemcode());
//                         xmList.add(jsonObject);
//                     }
            		 
            		 JSONObject jsonObject = new JSONObject();
                     jsonObject.put("itemguid", auditRsItemBaseinfo.getRowguid());
                     jsonObject.put("itemname", auditRsItemBaseinfo.getItemname());
                     jsonObject.put("itemcode", auditRsItemBaseinfo.getItemcode());
                     xmList.add(jsonObject);
                     
            	 }
            }
            JSONObject retJson = new JSONObject();
            retJson.put("xmlist", xmList);
            retJson.put("cert", cert);
            log.info("====================结束调用getCertXmLsit接口=============================");
            return JsonUtils.zwdtRestReturn("1", "获取成功！", retJson.toJSONString());
        }
        catch (Exception e) {
            log.error("===============调用getCertXmLsit接口发生异常===============");
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取失败！", "");
        }
    }

    /**
     * 
     * [根据项目标识和证照类型获取对应的所有附件]
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "getCertxminfo", method = RequestMethod.POST)
    public String getCertxminfo(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("====================开始调用getCertxminfo接口=============================");
        try {
            // 1.解析入参
            JSONObject paramsObject = JSONObject.parseObject(params);
            // 2.校验token
            String checkToken = JsonUtils.checkUserAuth(paramsObject.getString("token"));
            if (StringUtil.isNotBlank(checkToken)) {
                return checkToken;
            }
            // 3.先校验是否登录
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister == null) {
                return JsonUtils.zwdtRestReturn("0", "用户未登录或者登录过期！", "");
            }
            // 4.获取具体参数
            JSONObject json = paramsObject.getJSONObject("params");
            String itemguid = json.getString("itemguid");
            if (StringUtil.isBlank(itemguid)) {
                return JsonUtils.zwdtRestReturn("0", "项目唯一标识itemguid不能为空！", "");
            }
            String cert = json.getString("cert");
            if (StringUtil.isBlank(cert)) {
                return JsonUtils.zwdtRestReturn("0", "证照类型cert不能为空！", "");
            }
            // 5.查询具体的证照
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("itemguid", itemguid);
            sqlConditionUtil.eq("cert", cert);
            SQLManageUtil sqlManageUtil = new SQLManageUtil();
            String sql = sqlManageUtil.buildSqlComoplete(AuditXmCert.class, sqlConditionUtil.getMap());
            AuditXmCert auditXmCert = iAuditXMCertService.find(sql, "");
            List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
            String url = ConfigUtil.getConfigValue("officeweb365url");
            String documentType = "";
            
            if (auditXmCert != null) {
                List<FrameAttachInfo> list = iAttachService.getAttachInfoListByGuid(auditXmCert.getClengguid());
                if (!list.isEmpty()) {
                    for (FrameAttachInfo frameAttachInfo : list) {
                    	documentType = frameAttachInfo.getContentType();
						String downloadUrl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
								+ frameAttachInfo.getAttachGuid();
						String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(downloadUrl, documentType);
						String attachurl = url + encryptUrl;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("attachguid", frameAttachInfo.getAttachGuid());
                        jsonObject.put("attachfilename", frameAttachInfo.getAttachFileName());
                        jsonObject.put("filepath",
                                WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + frameAttachInfo.getAttachGuid());
                        jsonObject.put("365attachurl", attachurl);
                        
                        jsonObjectList.add(jsonObject);
                    }
                }

            }
            JSONObject retJson = new JSONObject();
            retJson.put("attachlist", jsonObjectList);
            log.info("====================结束调用getCertxminfo接口=============================");
            return JsonUtils.zwdtRestReturn("1", "获取成功！", retJson.toJSONString());
        }
        catch (Exception e) {
            log.error("===============调用getCertxminfo接口发生异常===============");
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取失败！", "");
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
