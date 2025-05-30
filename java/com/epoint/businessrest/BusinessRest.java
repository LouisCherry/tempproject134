package com.epoint.businessrest;
/*
 *@title BusinessRest
 *@description
 *@author 丁雪健
 *@version 1.0
 *@project epoint-jining
 *@create 2024/1/8 9:30
 */

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author xuejianding
 * @Date 2024/1/8 9:30
 */
@RestController
@RequestMapping("/businessrest")
public class BusinessRest {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    /**
     * 用户个人信息API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;

    /**
     * 一件事办件撤回接口
     *
     * @description
     * @author xuejianding
     * @date 2024/1/8 9:30
     */
    @RequestMapping(value = "/withdrawsubapp", method = RequestMethod.POST)
    public String withdrawsubapp(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=======开始调用withdrawsubapp接口=======" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister == null || com.epoint.core.utils.string.StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
                log.info("当前用户不存在或未登录！");
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
            }
            AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                    .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
            if (auditOnlineIndividual == null) {
                log.info("当前用户不存在或未登录！");
                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
            }
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject dataJson = new JSONObject();
                String projectguid = obj.getString("projectguid");
                log.info("projectguid=======" + projectguid);
                if (StringUtil.isBlank(projectguid)) {
                    return JsonUtils.zwdtRestReturn("0", "获取projectguid为空！", "");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("sourceguid", projectguid);
                PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                        sql.getMap(), 0,
                        10, "applydate", "desc").getResult();
                log.info("pageData=======" + pageData);
                List<AuditOnlineProject> auditOnlineProjectList = pageData.getList();
                log.info("auditOnlineProjectList=======" + auditOnlineProjectList);
                if (auditOnlineProjectList != null && !auditOnlineProjectList.isEmpty()) {
                    if (!auditOnlineIndividual.getApplyerguid().equals(auditOnlineProjectList.get(0).getApplyerguid())) {
                        return JsonUtils.zwdtRestReturn("0", "当前办件不属于当前登录人!", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取AuditOnlineProject为空！", "");
                }
                List<AuditSpISubapp> spISubapps = iAuditSpISubapp.getSubappByBIGuid(projectguid).getResult();
                log.info("spISubapps" + spISubapps.get(0));
                if (spISubapps.get(0) != null) {
                    if ("12".equals(auditOnlineProjectList.get(0).getStatus())) {
                        // 子申报状态
                        spISubapps.get(0).setStatus("10");
                        iAuditSpISubapp.updateAuditSpISubapp(spISubapps.get(0));

                        // 办件状态
                        auditOnlineProjectList.get(0).setStatus("10");
                        iAuditOnlineProject.updateProject(auditOnlineProjectList.get(0));
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "当前办件已审核，不能撤回！", "");
                    }
                }
                log.info("=======结束调用withdrawsubapp接口=======" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "撤回成功！", dataJson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
            }
        } catch (Exception e) {
            log.info("=======结束调用withdrawsubapp接口=======" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======withdrawsubapp异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "撤回失败！" + e.getMessage(), "");
        }
    }

    public AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
                .getComponent(IAuditOnlineRegister.class);
        AuditOnlineRegister auditOnlineRegister = null;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        return auditOnlineRegister;
    }

}
