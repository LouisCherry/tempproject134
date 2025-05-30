package com.epoint.zwdt.zwdtrest.user;

import java.lang.invoke.MethodHandles;
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
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditcollection.inter.IAuditOnlineCollection;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.usersetting.domain.AuditOnlinePostAddress;
import com.epoint.basic.auditonlineuser.usersetting.inter.IAuditOnlinePostAddress;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 
 *  用户信息设置相关接口
 *  
 * @作者 WST
 * @version [F9.3, 2018年1月16日]
 */
@RestController
@RequestMapping("/jnzwdtUserSeting")
public class JnUserSettingRestController
{

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
     * 个人注册信息API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 个人基本信息
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 个人邮寄地址API
     */
    @Autowired
    private IAuditOnlinePostAddress iAuditOnlinePostAddress;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    /**
     * 咨询投诉API
     */
    @Autowired
    private IAuditOnlineConsult iAuditOnlineConsult;
    /**
     * 收藏API
     */
    @Autowired
    private IAuditOnlineCollection iAuditOnlineCollection;
    /**
     * 预约API
     */
    @Autowired
    private IAuditQueueAppointment iAuditQueueAppointment;


    /**
     * 获取个人信息的接口
     * 
     * @param params 接口入参
     * @return
     */
    @RequestMapping(value = "/private/getAccountInfo", method = RequestMethod.POST)
    public String getAccountInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAccountInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取是否需要展示我的预约，我的咨询，我的收藏等数据   1：展示   0：不展示
                String isShowCount = obj.getString("isshowcount");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    if (auditOnlineIndividual != null) {
                        // 2、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        if (auditOnlineRegister != null && auditOnlineIndividual != null) {
                            // 2.1、获取账号基本信息
                            dataJson.put("lastlogintime", EpointDateUtil.convertDate2String(
                                    auditOnlineRegister.getLastlogindate(), EpointDateUtil.DATE_TIME_FORMAT));// 上次登录时间
                            dataJson.put("idnum", auditOnlineIndividual.getIdnumber());// 身份证
                            dataJson.put("mobile", auditOnlineRegister.getMobile());// 手机号码
                            String usertype = auditOnlineRegister.getUsertype();
                            dataJson.put("type", iCodeItemsService.getItemTextByCodeName("申请人类型",
                                    StringUtil.isBlank(usertype) ? "" : usertype)); //账户类型
                            // 2.2、获取个人设置的默认地址
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("accountguid", auditOnlineRegister.getAccountguid());
                            sqlConditionUtil.eq("Isdefault", "1");
                            List<AuditOnlinePostAddress> auditOnlinePostAddresses = iAuditOnlinePostAddress
                                    .getAllAuditOnlinePostAddress(sqlConditionUtil.getMap()).getResult();
                            if (auditOnlinePostAddresses != null && auditOnlinePostAddresses.size() > 0) {
                                dataJson.put("address", auditOnlinePostAddresses.get(0).getArea()
                                        + auditOnlinePostAddresses.get(0).getAddress());//联系地址 TODO
                            }
                            dataJson.put("accounttype", "高级实名认证");// 实名认证
                            dataJson.put("clinetname", auditOnlineIndividual.getClientname());// 真实姓名
                            // 2.3、获取用户头像
                            String profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwmhwz/pages/account/images/def_profilepic.png";
                            if (StringUtil.isNotBlank(auditOnlineRegister.getProfilePic())) {
                                profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/auditattach/readAttach?attachguid="
                                        + auditOnlineRegister.getProfilePic();
                            }
                            dataJson.put("profilepicurl", profilePicUrl);
                            // 2.4、密码强度级别
                            dataJson.put("pwdlevel", StringUtil.isNotBlank(auditOnlineRegister.getPwdlevel())
                                    ? auditOnlineRegister.getPwdlevel() : "0");
                            // 2.5、需要显示我的一些数据
                            if ("1".equals(isShowCount)) {
                                // 3、获取我的咨询数量(已答未阅读的咨询数量)
                                SqlConditionUtil sqlConditionUtilConsult = new SqlConditionUtil();
                                sqlConditionUtilConsult.eq("ASKERUSERGUID", auditOnlineRegister.getAccountguid());
                                sqlConditionUtilConsult.eq("consultType", ZwdtConstant.CONSULT_TYPE_ZX);
                                sqlConditionUtilConsult.eq("readstatus", ZwdtConstant.CONSULT_READSTATUS_NO); // 未读
                                sqlConditionUtilConsult.in("status", "1,3"); //答复过的
                                int consultCount = iAuditOnlineConsult.getConsultCount(sqlConditionUtilConsult.getMap())
                                        .getResult();
                                dataJson.put("consultcount", consultCount);
                                // 4、获取我的收藏数量
                                SqlConditionUtil sqlConditionUtilCollect = new SqlConditionUtil();
                                sqlConditionUtilCollect.eq("accountguid", auditOnlineRegister.getAccountguid());
                                int collectCount = iAuditOnlineCollection
                                        .getAuditCollectionCount(sqlConditionUtilCollect.getMap()).getResult();
                                dataJson.put("collectcount", collectCount);
                                // 5、获取我的申报数量
                                SqlConditionUtil sqlConditionUtilApply = new SqlConditionUtil();
                                sqlConditionUtilApply.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                                sqlConditionUtilApply.in("status", "12,16,24,26,28,30,40,50,90,97,98,99");
                                PageData<AuditOnlineProject> pageDataProject = iAuditOnlineProject
                                        .getAuditOnlineProjectPageData(sqlConditionUtilApply.getMap(), 0, 5,
                                                "applydate", "desc")
                                        .getResult();
                                dataJson.put("applycount", pageDataProject.getRowCount());
                                // 6、获取我的预约数量（1：代表今日预约、2：代表历史预约）
                                PageData<AuditQueueAppointment> pageDataAppoint = iAuditQueueAppointment
                                        .getAppointPageDataByType("1", "", auditOnlineRegister.getAccountguid(), 0, 5)
                                        .getResult();
                                dataJson.put("appointmentcount", pageDataAppoint.getRowCount());
                                // 7、获取我的咨询投诉总数
                                SqlConditionUtil sqlConditionUtilzxts = new SqlConditionUtil();
                                sqlConditionUtilzxts.eq("ASKERUSERGUID", auditOnlineRegister.getAccountguid());
                                int zxtsCount = iAuditOnlineConsult.getConsultCount(sqlConditionUtilzxts.getMap())
                                        .getResult();
                                dataJson.put("zxtscount", zxtsCount);
                            }
                        }
                        log.info("=======结束调用getAccountInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取用户基本信息成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "获取个人信息失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户注册信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取用户基本信息异常：" + e.getMessage(), "");
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
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
        }
        return auditOnlineRegister;
    }
}
