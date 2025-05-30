package com.epoint.zwdt.zwdtrest.consult;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsultExt;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsultExt;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdtutil.ZwdtUserAuthValid;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.zwdt.zwdtrest.service.ZwdtService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网上办事大厅咨询投诉相关接口
 *
 * @author WST
 * @version [9.3, 2017年10月25日]
 */
@RestController
@RequestMapping("/zwdtConsult")
public class AuditOnlineConsultController {

    private ZwdtService service = new ZwdtService();

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private IUserService userservice;
    /**
     * 部门API
     */
    @Autowired
    private IOuService iOuService;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 咨询投诉API
     */
    @Autowired
    private IAuditOnlineConsult iAuditOnlineConsult;
    /**
     * 咨询投诉追问API
     */
    @Autowired
    private IAuditOnlineConsultExt iAuditOnlineConsultExt;
    /**
     * 网上办事大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 中心API
     */
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
    /**
     * 部门API
     */
    @Autowired
    private IOuService ouservice;

    @Autowired
    private IMessagesCenterService centerService;

    /**
     * 获取咨询和投诉数量
     * 获取咨询数量、投诉数量、等待答复数量、已答复数量
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getConsultNum", method = RequestMethod.POST)
    public String getConsultNum(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getConsultNum接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉类型
                String cosultType = obj.getString("cosulttype");
                // 1.2、获取用户唯一标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                String accountGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
                if (StringUtil.isNotBlank(accountGuid)) {
                    // 2、咨询数量
                    SqlConditionUtil zxSqlConditionUtil = new SqlConditionUtil();
                    zxSqlConditionUtil.eq("ASKERUSERGUID", accountGuid);
                    zxSqlConditionUtil.eq("CONSULTTYPE", ZwfwConstant.CONSULT_TYPE_ZX);
                    int ziXunCount = iAuditOnlineConsult.getConsultCount(zxSqlConditionUtil.getMap()).getResult();
                    // 3、投诉数量
                    SqlConditionUtil txSqlConditionUtil = new SqlConditionUtil();
                    txSqlConditionUtil.eq("ASKERUSERGUID", accountGuid);
                    txSqlConditionUtil.eq("CONSULTTYPE", ZwfwConstant.CONSULT_TYPE_TS);
                    int touSuCount = iAuditOnlineConsult.getConsultCount(txSqlConditionUtil.getMap()).getResult();
                    // 4、等待答复数量
                    int waitAnswerCount = iAuditOnlineConsult.getAnswerCount(accountGuid, false, cosultType)
                            .getResult();
                    // 5、已答复数量
                    int alreadyAnswerCount = iAuditOnlineConsult.getAnswerCount(accountGuid, true, cosultType)
                            .getResult();
                    // 6、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("zixuncount", ziXunCount);
                    dataJson.put("tousucount", touSuCount);
                    dataJson.put("waitanswercount", waitAnswerCount);
                    dataJson.put("alreadyanswercount", alreadyAnswerCount);
                    log.info("=======结束调用getConsultNum接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取咨询或者投诉数量及答复数量成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultNum接口参数：params【" + params + "】=======");
            log.info("=======getConsultNum异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询或者投诉数量及答复数量失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取咨询或者投诉列表接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getConsultListByType", method = RequestMethod.POST)
    public String getConsultListByType(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getConsultListByType接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉类型 1：咨询 2：投诉
                String consultType = obj.getString("consulttype");
                // 1.2、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.3、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.4、获取用户唯一标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                String accountGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
                if (StringUtil.isNotBlank(accountGuid)) {
                    // 2、获取咨询投诉的分页列表
                    SqlConditionUtil zxSqlConditionUtil = new SqlConditionUtil();
                    zxSqlConditionUtil.eq("ASKERUSERGUID", accountGuid);
                    if (StringUtil.isNotBlank(consultType)) {
                        zxSqlConditionUtil.eq("consulttype", consultType);
                    }
                    List<AuditOnlineConsult> auditOnlineConsults = iAuditOnlineConsult.selectConsultByPage(
                            zxSqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "askdate", "desc").getResult();
                    // 3、拼接返回的咨询投诉列表
                    List<JSONObject> consultList = new ArrayList<JSONObject>();
                    for (AuditOnlineConsult auditOnlineConsult : auditOnlineConsults) {
                        JSONObject consultJson = new JSONObject();
                        consultJson.put("consultguid", auditOnlineConsult.getRowguid());// 咨询投诉标识
                        consultJson.put("question", auditOnlineConsult.getQuestion());// 问题
                        consultJson.put("askdate", EpointDateUtil.convertDate2String(auditOnlineConsult.getAskdate(),
                                EpointDateUtil.DATE_FORMAT));// 提问时间
                        consultJson.put("consulttype", auditOnlineConsult.getConsulttype());// 咨询类型
                        consultJson.put("isanswer", "0");// 默认未回复
                        consultJson.put("attachcount", "0"); // 默认没有附件
                        consultJson.put("answerattachcount", 0); // 默认答复附件
                        consultJson.put("status", auditOnlineConsult.getStatus());// 咨询答复状态

                        //特殊处理，如果状态为9说明是撤回的件，可以重新提交
                        String title = auditOnlineConsult.getTitle();
                        if ("9".equals(auditOnlineConsult.getStatus())) {
                            consultJson.put("title", title + "【撤回】");// 标题
                        } else {
                            consultJson.put("title", title);// 标题
                        }

                        // 4、判断咨询投诉是否已回复
                        if (auditOnlineConsult.getAnswerdate() != null) {
                            consultJson.put("isanswer", "1");
                            consultJson.put("answer", auditOnlineConsult.getAnswer());
                            consultJson.put("answerdate", EpointDateUtil.convertDate2String(
                                    auditOnlineConsult.getAnswerdate(), EpointDateUtil.DATE_TIME_FORMAT));// 回答时间
                            if (StringUtil.isNotBlank(auditOnlineConsult.getAnswerouguid())) {
                                FrameOu frameOu = iOuService.getOuByOuGuid(auditOnlineConsult.getAnswerouguid());
                                consultJson.put("ouname", frameOu == null ? "" : frameOu.getOuname());
                            } else {
                                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(auditOnlineConsult.getCenterguid()).getResult();
                                consultJson.put("ouname",
                                        auditOrgaServiceCenter == null ? "" : auditOrgaServiceCenter.getCentername());
                            }

                            // 4.1、判断咨询投诉第一次答复是否有附件
                            String clientApplyGuid = auditOnlineConsult.getClientapplyguid();
                            if (StringUtil.isNotBlank(clientApplyGuid)) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(clientApplyGuid);
                                List<JSONObject> attachList = new ArrayList<JSONObject>();
                                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                    JSONObject attachJson = new JSONObject();
                                    attachJson.put("answerattachlength", frameAttachInfo.getAttachLength());
                                    attachJson.put("answerattachguid", frameAttachInfo.getAttachGuid());
                                    attachJson.put("answerattchname", frameAttachInfo.getAttachFileName());
                                    attachList.add(attachJson);
                                }
                                consultJson.put("answerclientguid", clientApplyGuid);
                                consultJson.put("answerattachcount", attachList.size());
                                consultJson.put("answerattachlist", attachList);
                            }
                        }
                        // 5、获取咨询投诉附件
                        String clientGuid = auditOnlineConsult.getClientguid();
                        if (StringUtil.isNotBlank(clientGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                            List<JSONObject> attachList = new ArrayList<JSONObject>();
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                JSONObject attachJson = new JSONObject();
                                attachJson.put("attachlength", frameAttachInfo.getAttachLength());
                                attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                                attachJson.put("attchname", frameAttachInfo.getAttachFileName());
                                attachList.add(attachJson);
                            }
                            consultJson.put("clientguid", clientGuid);
                            consultJson.put("attachcount", attachList.size());
                            consultJson.put("attachlist", attachList);
                        }
                        consultList.add(consultJson);
                    }
                    // 6、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    List<AuditOnlineConsult> allConsults = iAuditOnlineConsult
                            .selectAuditOnlineConsultList(zxSqlConditionUtil.getMap()).getResult();
                    dataJson.put("totalcount", allConsults.size());
                    dataJson.put("consultlist", consultList);
                    log.info("=======结束调用getConsultListByType接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取咨询或者投诉列表成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultListByType接口参数：params【" + params + "】=======");
            log.info("=======getConsultListByType异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询或者投诉列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取咨询或者投诉详细信息接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getConsultDetailByRowGuid", method = RequestMethod.POST)
    public String getConsultDetailByRowGuid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getConsultDetailByRowGuid接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultguid = obj.getString("consultguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultguid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取咨询投诉信息
                AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultguid)
                        .getResult();
                JSONObject consultJson = new JSONObject();
                consultJson.put("consultguid", auditOnlineConsult.getRowguid());// 咨询投诉标识
                consultJson.put("title", auditOnlineConsult.getTitle());// 咨询投诉标题
                consultJson.put("question", auditOnlineConsult.getQuestion());// 咨询投诉内容
                consultJson.put("consulttype", auditOnlineConsult.getConsulttype());
                consultJson.put("askname", auditOnlineConsult.getAskerusername());
                consultJson.put("mobile", auditOnlineConsult.getAskerMobile());
                consultJson.put("askdate",
                        EpointDateUtil.convertDate2String(auditOnlineConsult.getAskdate(), EpointDateUtil.DATE_FORMAT));// 提问时间
                consultJson.put("isanswer", "0");// 默认未回复
                consultJson.put("answerattachcount", "0");// 默认回复附件数量0
                consultJson.put("attachcount", "0");// 默认咨询投诉数量
                // 3、判断咨询投诉是否已回复
                if (auditOnlineConsult.getAnswerdate() != null) {
                    consultJson.put("isanswer", "1");
                    consultJson.put("answer", auditOnlineConsult.getAnswer());// 回答内容
                    consultJson.put("answerdate", EpointDateUtil.convertDate2String(auditOnlineConsult.getAnswerdate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 回答时间
                    if (StringUtil.isNotBlank(auditOnlineConsult.getAnswerouguid())) { // 判断是否是部门还是中心
                        FrameOu frameOu = iOuService.getOuByOuGuid(auditOnlineConsult.getAnswerouguid());
                        consultJson.put("ouname", frameOu == null ? "" : frameOu.getOuname());// 回答部门
                    } else {
                        AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                .findAuditServiceCenterByGuid(auditOnlineConsult.getCenterguid()).getResult();
                        consultJson.put("ouname",
                                auditOrgaServiceCenter == null ? "" : auditOrgaServiceCenter.getCentername());
                    }
                    // 3.1、判断咨询投诉第一次答复是否有附件
                    String clientApplyGuid = auditOnlineConsult.getClientapplyguid();
                    if (StringUtil.isNotBlank(clientApplyGuid)) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                .getAttachInfoListByGuid(clientApplyGuid);
                        List<JSONObject> attachList = new ArrayList<JSONObject>();
                        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                            JSONObject attachJson = new JSONObject();
                            attachJson.put("answerattachlength", frameAttachInfo.getAttachLength());
                            attachJson.put("answerattachguid", frameAttachInfo.getAttachGuid());
                            attachJson.put("answerattchname", frameAttachInfo.getAttachFileName());
                            attachList.add(attachJson);
                        }
                        consultJson.put("answerclientguid", clientApplyGuid);
                        consultJson.put("answerattachcount", attachList.size());
                        consultJson.put("answerattachlist", attachList);
                    }
                }
                // 4、获取咨询投诉附件
                String clientGuid = auditOnlineConsult.getClientguid();
                if (StringUtil.isNotBlank(clientGuid)) {
                    List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                    List<JSONObject> attachList = new ArrayList<JSONObject>();
                    for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                        JSONObject attachJson = new JSONObject();
                        attachJson.put("attachlength", frameAttachInfo.getAttachLength());
                        attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                        attachJson.put("attchname", frameAttachInfo.getAttachFileName());
                        attachList.add(attachJson);
                    }
                    consultJson.put("clientguid", clientGuid);
                    consultJson.put("attachcount", attachList.size());
                    consultJson.put("attachlist", attachList);
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("consultdetail", consultJson);
                log.info("=======结束调用getConsultDetailByRowGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取咨询或者投诉列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultDetailByRowGuid接口参数：params【" + params + "】=======");
            log.info("=======getConsultDetailByRowGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询或者投诉列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 新增咨询投诉接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/addConsult", method = RequestMethod.POST)
    public String addConsult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用addConsult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (StringUtil.isBlank(obj.getString("askname"))) {
                    return JsonUtils.zwdtRestReturn("0", "提问者姓名不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("xianzhi"))) { // 随便加的一个字段，让第三方调用失败
                    return JsonUtils.zwdtRestReturn("0", "提交失败xianzhi", "");
                }

                if (StringUtil.isBlank(obj.getString("mobile"))) {
                    return JsonUtils.zwdtRestReturn("0", "手机号不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("title"))) {
                    return JsonUtils.zwdtRestReturn("0", "标题不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("question"))) {
                    return JsonUtils.zwdtRestReturn("0", "内容不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("taskguid"))) {
                    return JsonUtils.zwdtRestReturn("0", "事项不能为空！", "");
                }

                if (auditOnlineRegister != null) {

                    if ("郑志超".equals(auditOnlineRegister.getUsername())
                            || "15265711210".equals(auditOnlineRegister.getMobile())) {
                        return JsonUtils.zwdtRestReturn("0", "新增咨询投诉失败！", "");
                    }

                    // 1.2、获取接口的入参
                    String question = URLDecoder.decode(obj.getString("question"), "utf-8");// 咨询投诉内容
                    String title = URLDecoder.decode(obj.getString("title"), "utf-8");// 咨询投诉标题
                    String consultType = obj.getString("consulttype");// 1:咨询 2:投诉
                    String clientGuid = obj.getString("clientguid");// 咨询投诉附件标识
                    String serviceCenterGuid = obj.getString("servicecenterguid");// audit_orga_servicecenter表中的ROWGUID
                    String isPublic = obj.getString("ispublic");// 是否公开
                    String isAnonymous = obj.getString("isanonymous");// 是否匿名
                    String askName = obj.getString("askname");// 提问者
                    String mobile = obj.getString("mobile");// 手机号
                    String ouGuid = obj.getString("ouguid"); // 部门标识
                    String taskGuid = obj.getString("taskguid"); // 事项标识
                    String businessGuid = obj.getString("businessguid");// 主题标识
                    // 2、获取中心所在的区域
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    conditionsql.eq("rowguid", serviceCenterGuid);
                    List<AuditOrgaServiceCenter> auditOrgaServiceCenters = iAuditOrgaServiceCenter
                            .getAuditOrgaServiceCenterByCondition(conditionsql.getMap()).getResult();
                    AuditOrgaServiceCenter auditOrgaServiceCenter = auditOrgaServiceCenters.get(0);
                    String areaCode = auditOrgaServiceCenter == null ? "" : auditOrgaServiceCenter.getBelongxiaqu();
                    // 3、新增咨询
                    AuditOnlineConsult auditOnlineConsult = new AuditOnlineConsult();
                    String consultguid = UUID.randomUUID().toString();
                    auditOnlineConsult.setRowguid(consultguid);
                    auditOnlineConsult.setTaskguid(taskGuid);
                    auditOnlineConsult.setPublishonweb("true".equalsIgnoreCase(isPublic) ? "1" : "0");// 是否对外发布
                    auditOnlineConsult.set("iswtshow","true".equalsIgnoreCase(isPublic) ? "1" : "0");
                    auditOnlineConsult.setIsAnonymous("true".equalsIgnoreCase(isAnonymous) ? "1" : "0");// 是否匿名
                    auditOnlineConsult.setCenterguid(serviceCenterGuid);// 中心标识
                    auditOnlineConsult.setAreaCode(areaCode);// 区域标识
                    auditOnlineConsult.setAskdate(new Date());// 提问时间
                    auditOnlineConsult.setClientguid(clientGuid);
                    auditOnlineConsult.setAskeruserguid(auditOnlineRegister.getAccountguid());// 用户唯一标识
                    auditOnlineConsult.setOuguid(ouGuid);// 问题所属部门

                    auditOnlineConsult.setAskerusername(askName);// 提问人姓名
                    auditOnlineConsult.setAskerMobile(mobile);// 提问人手机号
                    auditOnlineConsult.setAskerloginid(auditOnlineRegister.getLoginid());// 提问人登录账号
                    auditOnlineConsult.setTitle(title);// 咨询投诉标题
                    auditOnlineConsult.setQuestion(question);// 咨询投诉内容
                    auditOnlineConsult.setBusinessguid(businessGuid);// 套餐标识
                    auditOnlineConsult.setConsulttype(consultType);// 咨询投诉类型
                    auditOnlineConsult.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_NO);// 咨询记录阅读状态：未读
                    auditOnlineConsult.setSource(ZwdtConstant.CONSULT_SORUCE_WWKJ);// 咨询建议来源：外网空间
                    auditOnlineConsult.setStatus(ZwfwConstant.ZIXUN_TYPE_DDF);// 状态：待答复
                    iAuditOnlineConsult.addConsult(auditOnlineConsult);

                    addConsultMessage(ouGuid, new Date(), areaCode);

                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("consultguid", consultguid);
                    log.info("=======结束调用addConsult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "新增成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addConsult接口参数：params【" + params + "】=======");
            log.info("=======addConsult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "新增咨询投诉失败：" + e.getMessage(), "");
        }
    }

    /**
     * 更新咨询状态为已读的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/changeconsultisread", method = RequestMethod.POST)
    public String changeconsultisread(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用changeconsultisread接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultGuid = obj.getString("consultguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultGuid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取咨询投诉信息
                AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultGuid)
                        .getResult();
                if (auditOnlineConsult != null) {
                    // 3、更新咨询投诉信息为已读
                    auditOnlineConsult.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_YES);
                    iAuditOnlineConsult.updateConsult(auditOnlineConsult);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用changeconsultisread接口=======");
                return JsonUtils.zwdtRestReturn("1", "修改成已读状态", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======changeconsultisread接口参数：params【" + params + "】=======");
            log.info("=======changeconsultisread异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改成已读状态失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取追问列表的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getAppendConsultList", method = RequestMethod.POST)
    public String getAppendConsultList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppendConsultList接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultGuid = obj.getString("consultguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultGuid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("consultguid", consultGuid);
                // 2、获取追问列表（暂定20条数据 ）
                List<AuditOnlineConsultExt> auditOnlineConsultExts = iAuditOnlineConsultExt
                        .selectConsultExtByPage(conditionsql.getMap(), 0, 20, "qadate", "asc").getResult();
                List<JSONObject> appendList = new ArrayList<JSONObject>();
                for (AuditOnlineConsultExt auditOnlineConsultExt : auditOnlineConsultExts) {
                    JSONObject consultJson = new JSONObject();
                    consultJson.put("content", auditOnlineConsultExt.getContent());// 追问或追答内容
                    consultJson.put("qadate", EpointDateUtil.convertDate2String(auditOnlineConsultExt.getQadate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 追问或追答时间
                    consultJson.put("type", auditOnlineConsultExt.getType()); // 1.追问 2.追答
                    if (StringUtil.isNotBlank(auditOnlineConsultExt.getOuguid())) { // 判断是否是部门还是中心
                        FrameOu frameOu = iOuService.getOuByOuGuid(auditOnlineConsultExt.getOuguid());
                        consultJson.put("extouname", frameOu == null ? "" : frameOu.getOuname() + "答复");// 追问回答部门
                    } else {
                        if (auditOnlineConsultExt.getType() == 2) { // 判断是否是回复消息
                            AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultGuid) // 获取追问主题的咨询投诉信息
                                    .getResult();
                            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                    .findAuditServiceCenterByGuid(auditOnlineConsult.getCenterguid()).getResult(); // 获取追主题的中心信息
                            consultJson.put("extouname", auditOrgaServiceCenter == null ? ""
                                    : auditOrgaServiceCenter.getCentername() + "答复");
                        }
                    }
                    // 3、获取追问附件
                    String clientGuid = auditOnlineConsultExt.getClientguid();
                    if (StringUtil.isNotBlank(clientGuid)) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                        List<JSONObject> attachList = new ArrayList<JSONObject>();
                        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                            JSONObject attachJson = new JSONObject();
                            attachJson.put("attachguid", frameAttachInfo.getAttachGuid());
                            attachJson.put("attchname", frameAttachInfo.getAttachFileName());
                            attachJson.put("attachlength", frameAttachInfo.getAttachLength());
                            attachList.add(attachJson);
                        }
                        consultJson.put("attachcount", attachList.size());
                        consultJson.put("attachlist", attachList);
                    }
                    appendList.add(consultJson);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("appendList", appendList);
                log.info("=======结束调用getAppendConsultList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取追问列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAppendConsultList接口参数：params【" + params + "】=======");
            log.info("=======getAppendConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取追问列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 追问的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/appendConsult", method = RequestMethod.POST)
    public String appendConsult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用appendConsult接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultGuid = obj.getString("consultguid");
                // 1.2、获取追问内容
                String question = obj.getString("question");
                // 1.3、获取附件标识
                String clientGuid = obj.getString("clientguid");
                // 1.4、获取用户基本信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultGuid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 2、更新咨询投诉主表状态：已答复、追问已答复=》追问未答复
                    AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultGuid)
                            .getResult();
                    String consultStatus = auditOnlineConsult.getStatus();
                    String ouguid = auditOnlineConsult.getOuguid();
                    if ((ZwfwConstant.ZIXUN_TYPE_YDF).equals(consultStatus)
                            || (ZwfwConstant.ZIXUN_TYPE_ZWYDF).equals(consultStatus)) {
                        auditOnlineConsult.setStatus(ZwfwConstant.ZIXUN_TYPE_ZWDDF);
                    }
                    iAuditOnlineConsult.updateConsult(auditOnlineConsult);
                    // 3、新增追问数据
                    AuditOnlineConsultExt auditOnlineConsultExt = new AuditOnlineConsultExt();
                    auditOnlineConsultExt.setRowguid(UUID.randomUUID().toString());
                    auditOnlineConsultExt.setQauserguid(auditOnlineRegister.getAccountguid());// 追问人标识
                    auditOnlineConsultExt.setQausername(auditOnlineRegister.getUsername());// 追问人名称
                    auditOnlineConsultExt.setQaloginguid(auditOnlineRegister.getLoginid());// 追问人登录id
                    auditOnlineConsultExt.setType(ZwfwConstant.ZIXUN_EXT_TYPE_ZW);// 类型：追问
                    auditOnlineConsultExt.setQadate(new Date());// 追问时间
                    auditOnlineConsultExt.setConsultguid(consultGuid);// 关联咨询投诉主表标识
                    auditOnlineConsultExt.setContent(question);// 追问内容
                    auditOnlineConsultExt.setClientguid(clientGuid);// 追问附件标识
                    iAuditOnlineConsultExt.addConsultExt(auditOnlineConsultExt);

                    addConsultMessage(ouguid, new Date(), auditOnlineConsult.getAreacode());

                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    log.info("=======结束调用appendConsult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "追问成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======appendConsult接口参数：params【" + params + "】=======");
            log.info("=======appendConsult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "追问失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取资询/追问/投诉未答复数据接口（自检页面调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getConsultCheckInfo", method = RequestMethod.POST)
    public String getConsultCheckInfo(@RequestBody String params) {
        try {
            log.info("=======开始获取getConsultCheckInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取状态
                String status = obj.getString("status");// 0:咨询未答复，2:追问未答复
                // 1.2、获取类别
                String consultType = obj.getString("consultType"); // 1:咨询，2：投诉
                // 1.3、获取当前页
                String currentPage = obj.getString("pageindex");
                // 1.4、获取每页数量
                String pageSize = obj.getString("pagesize");
                // 2、获取符合条件的的咨询投诉列表
                List<JSONObject> consultJsonList = new ArrayList<JSONObject>();
                SqlConditionUtil conditionUtil = new SqlConditionUtil();
                conditionUtil.eq("status", status);
                conditionUtil.eq("consulttype", consultType);
                List<AuditOnlineConsult> auditOnlineConsultList = iAuditOnlineConsult.selectConsultByPage(
                        conditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "ouguid,askdate", "desc").getResult();
                if (auditOnlineConsultList != null && auditOnlineConsultList.size() > 0) {
                    for (AuditOnlineConsult auditOnlineConsult : auditOnlineConsultList) {
                        JSONObject recordJson = new JSONObject();
                        recordJson.put("askusername", auditOnlineConsult.getAskerusername());
                        recordJson.put("question", auditOnlineConsult.getQuestion());
                        recordJson.put("askdate", EpointDateUtil.convertDate2String(auditOnlineConsult.getAskdate(),
                                EpointDateUtil.DATE_FORMAT));
                        recordJson.put("askmobile", auditOnlineConsult.getAskerMobile());
                        // 2.1、 找出对应的部门名称
                        if (StringUtil.isBlank(auditOnlineConsult.getOuguid())) {
                            recordJson.put("ouname", "中心");
                        } else {
                            FrameOu frameOu = ouservice.getOuByOuGuid(auditOnlineConsult.getOuguid());
                            if (frameOu != null) {
                                recordJson.put("ouname", frameOu.getOuname());
                            } else {
                                recordJson.put("ouname", "该部门已被删除");
                            }
                        }
                        consultJsonList.add(recordJson);
                    }
                }
                // 3、获取未回复的咨询投诉数量
                int count = iAuditOnlineConsult.getConsultCount(conditionUtil.getMap()).getResult();
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("consultJsonList", consultJsonList);
                dataJson.put("count", count);
                log.info("=======结束调用getConsultCheckInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取资询/追问未答复数据成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultCheckInfo接口参数：params【" + params + "】=======");
            log.info("=======getConsultCheckInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取资询/追问未答复数据异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取咨询部门，获取部门电话
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getOuList", method = RequestMethod.POST)
    public String getOuList(@RequestBody String params) {
        try {
            log.info("=======开始获取getOuList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域
                String areacode = obj.getString("areacode");
                List<FrameOu> list = service.getOuListByAreacode(areacode);
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", list);
                return JsonUtils.zwdtRestReturn("1", "获取部门数据成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getOuList接口参数：params【" + params + "】=======");
            log.info("=======getOuList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门数据异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取咨询部门，获取部门电话
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getOuListQuestion", method = RequestMethod.POST)
    public String getOuListQuestion(@RequestBody String params) {
        try {
            log.info("=======开始获取getOuListQuestion接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域
                String areacode = obj.getString("areacode");
                List<FrameOu> list = service.getOuListByAreacodeHaveTasks(areacode);
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", list);
                return JsonUtils.zwdtRestReturn("1", "获取部门数据成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getOuListQuestion接口参数：params【" + params + "】=======");
            log.info("=======getOuListQuestion异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门数据异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取咨询列表
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/getConsultList", method = RequestMethod.POST)
    public String getConsultList(@RequestBody String params) {
        try {
            log.info("=======开始获取getConsultList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页
                String currentPage = obj.getString("currentpage");
                // 1.2、获取每页数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取部门
                String ouguid = obj.getString("ouguid");
                // 1.4、标题名称
                String titlename = obj.getString("titlename");
                // 1.5、事项名称
                String taskname = obj.getString("taskname");
                // 1.5、回复状态
                String status = "1";
                if (StringUtil.isBlank(ouguid)) {
                    ouguid = "all";
                }
                // 1.3、获取辖区
                String areacode = obj.getString("areacode");
                List<AuditOnlineConsult> list = service.getConsultListByOu(areacode, ouguid,
                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                        titlename, taskname, status);
                for (AuditOnlineConsult auditOnlineConsult : list) {
                    AuditOnlineConsultExt consultExt = iAuditOnlineConsultExt
                            .getConsultExtByRowguid(auditOnlineConsult.getRowguid()).getResult();
                    if (consultExt != null) {
                        auditOnlineConsult.set("isadd", "有追问");
                    } else {
                        auditOnlineConsult.set("isadd", "无追问");
                    }
                    if ("0".equals(auditOnlineConsult.getStatus())) {
                        auditOnlineConsult.setStatus("未答复");
                    } else {
                        auditOnlineConsult.setStatus("已答复");
                    }
                }
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("consultlist", list);
                return JsonUtils.zwdtRestReturn("1", "获取咨询数据成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultList接口参数：params【" + params + "】=======");
            log.info("=======getConsultList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询数据异常：" + e.getMessage(), "");
        }

    }

    /**
     * 新增评议与调查
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/addExmine", method = RequestMethod.POST)
    public String addExmine(@RequestBody String params) {
        try {
            log.info("=======开始获取addExmine接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、年龄 1 25岁以下 2 25岁-34岁 3 35岁-44岁 4 45岁以上
                String age = obj.getString("age");
                // 1.2、学历 1. 高中 2. 大专 3. 本科 4. 硕士及以上 5. 其他
                String education = obj.getString("education");
                // 1.3、栏目
                String line = obj.getString("line");
                // 1.3、意见
                String text = obj.getString("text");

                service.addExmine(age, education, line, text);

                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                return JsonUtils.zwdtRestReturn("1", "新增调查成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addExmine接口参数：params【" + params + "】=======");
            log.info("=======addExmine异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询数据异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取资讯投诉详情
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getDetail", method = RequestMethod.POST)
    public String getDetail(@RequestBody String params) {
        try {
            log.info("=======开始获取getDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 咨询guid
                String consultguid = obj.getString("consultguid");
                JSONObject json = new JSONObject();
                AuditOnlineConsult consult = iAuditOnlineConsult.getConsultByRowguid(consultguid).getResult();
                if (consult != null) {
                    json.put("title", consult.getTitle());
                    json.put("question", consult.getQuestion());
                    json.put("answer", consult.getAnswer());
                    json.put("askdate", EpointDateUtil.convertDate2String(consult.getAskdate()));
                    json.put("answerdate", EpointDateUtil.convertDate2String(consult.getAnswerdate()));
                    if ("1".equals(consult.getStatus())) {
                        json.put("isdf", "已答复");
                    } else {
                        json.put("isdf", "未答复");
                    }
                }

                FrameOu ou = ouservice.getOuByOuGuid(consult.getOuguid());
                json.put("ouname", ou == null ? "" : ou.getOuname());

                AuditOnlineConsultExt consultExt = iAuditOnlineConsultExt.getConsultExtByRowguid(consultguid)
                        .getResult();
                if (consultExt != null) {
                    json.put("qdate", consultExt.getQadate());
                    json.put("iszw", "有追问");
                } else {
                    json.put("iszw", "无追问");
                }

                String askname = consult.getAskerusername();

                json.put("askerusername", replaceNameX(askname));
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("detail", json);
                return JsonUtils.zwdtRestReturn("1", "获取详情成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取详情失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDetail接口参数：params【" + params + "】=======");
            log.info("=======getDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取咨询数据异常：" + e.getMessage(), "");
        }

    }


    /**
     * 撤回接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/withdrawConsult", method = RequestMethod.POST)
    public String withdrawConsult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用withdrawConsult接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultguid = obj.getString("consultguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultguid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取咨询投诉信息
                AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultguid)
                        .getResult();
                if (auditOnlineConsult != null) {
                    //修改状态，置为9
                    auditOnlineConsult.setStatus("9");
                    iAuditOnlineConsult.updateConsult(auditOnlineConsult);
                    log.info("=======结束调用getConsultDetailByRowGuid接口=======");
                    return JsonUtils.zwdtRestReturn("1", "撤回成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn("0", "查询失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======withdrawConsult接口参数：params【" + params + "】=======");
            log.info("=======withdrawConsult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取咨询详情接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getConsultByGuid", method = RequestMethod.POST)
    public String getConsultByGuid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getConsultByGuid接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取咨询投诉标识
                String consultguid = obj.getString("consultguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, consultguid,
                            ZwdtConstant.USERVALID_CONSULT)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取咨询投诉信息
                AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultguid)
                        .getResult();
                JSONObject datajson = new JSONObject();
                if (auditOnlineConsult != null) {
                    datajson.put("taskguid", auditOnlineConsult.getTaskguid());
                    datajson.put("consulttype", auditOnlineConsult.getConsulttype());
                    datajson.put("title", auditOnlineConsult.getTitle());
                    datajson.put("question", auditOnlineConsult.getQuestion());
                }
                log.info("=======结束调用getConsultByGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功！", datajson.toJSONString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConsultByGuid接口参数：params【" + params + "】=======");
            log.info("=======getConsultByGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "失败：" + e.getMessage(), "");
        }
    }


    /**
     * 更新咨询投诉接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/updateConsult", method = RequestMethod.POST)
    public String updateConsult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用updateConsult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (StringUtil.isBlank(obj.getString("askname"))) {
                    return JsonUtils.zwdtRestReturn("0", "提问者姓名不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("xianzhi"))) { // 随便加的一个字段，让第三方调用失败
                    return JsonUtils.zwdtRestReturn("0", "提交失败xianzhi", "");
                }

                if (StringUtil.isBlank(obj.getString("mobile"))) {
                    return JsonUtils.zwdtRestReturn("0", "手机号不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("title"))) {
                    return JsonUtils.zwdtRestReturn("0", "标题不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("question"))) {
                    return JsonUtils.zwdtRestReturn("0", "内容不能为空！", "");
                }

                if (StringUtil.isBlank(obj.getString("taskguid"))) {
                    return JsonUtils.zwdtRestReturn("0", "事项不能为空！", "");
                }

                String consultguid = obj.getString("consultguid");

                if (auditOnlineRegister != null) {
                    // 1.2、获取接口的入参
                    String question = URLDecoder.decode(obj.getString("question"), "utf-8");// 咨询投诉内容
                    String title = URLDecoder.decode(obj.getString("title"), "utf-8");// 咨询投诉标题
                    String consultType = obj.getString("consulttype");// 1:咨询 2:投诉
                    String clientGuid = obj.getString("clientguid");// 咨询投诉附件标识
                    String serviceCenterGuid = obj.getString("servicecenterguid");// audit_orga_servicecenter表中的ROWGUID
                    String isPublic = obj.getString("ispublic");// 是否公开
                    String isAnonymous = obj.getString("isanonymous");// 是否匿名
                    String askName = obj.getString("askname");// 提问者
                    String mobile = obj.getString("mobile");// 手机号
                    String ouGuid = obj.getString("ouguid"); // 部门标识
                    String taskGuid = obj.getString("taskguid"); // 事项标识
                    String businessGuid = obj.getString("businessguid");// 主题标识
                    // 2、获取中心所在的区域
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    conditionsql.eq("rowguid", serviceCenterGuid);
                    List<AuditOrgaServiceCenter> auditOrgaServiceCenters = iAuditOrgaServiceCenter
                            .getAuditOrgaServiceCenterByCondition(conditionsql.getMap()).getResult();
                    AuditOrgaServiceCenter auditOrgaServiceCenter = auditOrgaServiceCenters.get(0);
                    String areaCode = auditOrgaServiceCenter == null ? "" : auditOrgaServiceCenter.getBelongxiaqu();
                    // 更新咨询
                    AuditOnlineConsult auditOnlineConsult = iAuditOnlineConsult.getConsultByRowguid(consultguid).getResult();
                    if (auditOnlineConsult != null) {
                        auditOnlineConsult.setTaskguid(taskGuid);
                        auditOnlineConsult.setPublishonweb("true".equalsIgnoreCase(isPublic) ? "1" : "0");// 是否对外发布
                        auditOnlineConsult.set("iswtshow","true".equalsIgnoreCase(isPublic) ? "1" : "0");
                        auditOnlineConsult.setIsAnonymous("true".equalsIgnoreCase(isAnonymous) ? "1" : "0");// 是否匿名
                        auditOnlineConsult.setCenterguid(serviceCenterGuid);// 中心标识
                        auditOnlineConsult.setAreaCode(areaCode);// 区域标识
                        auditOnlineConsult.setAskdate(new Date());// 提问时间
                        auditOnlineConsult.setClientguid(clientGuid);
                        auditOnlineConsult.setAskeruserguid(auditOnlineRegister.getAccountguid());// 用户唯一标识
                        auditOnlineConsult.setOuguid(ouGuid);// 问题所属部门

                        auditOnlineConsult.setAskerusername(askName);// 提问人姓名
                        auditOnlineConsult.setAskerMobile(mobile);// 提问人手机号
                        auditOnlineConsult.setAskerloginid(auditOnlineRegister.getLoginid());// 提问人登录账号
                        auditOnlineConsult.setTitle(title);// 咨询投诉标题
                        auditOnlineConsult.setQuestion(question);// 咨询投诉内容
                        auditOnlineConsult.setBusinessguid(businessGuid);// 套餐标识
                        auditOnlineConsult.setConsulttype(consultType);// 咨询投诉类型
                        auditOnlineConsult.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_NO);// 咨询记录阅读状态：未读
                        auditOnlineConsult.setSource(ZwdtConstant.CONSULT_SORUCE_WWKJ);// 咨询建议来源：外网空间
                        auditOnlineConsult.setStatus(ZwfwConstant.ZIXUN_TYPE_DDF);// 状态：待答复

                        iAuditOnlineConsult.updateConsult(auditOnlineConsult);
                        addConsultMessage(ouGuid, new Date(), areaCode);
                    }

                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("consultguid", consultguid);
                    log.info("=======结束调用updateConsult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "更新成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateConsult接口参数：params【" + params + "】=======");
            log.info("=======updateConsult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "更新咨询投诉失败：" + e.getMessage(), "");
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
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    public void addConsultMessage(String ouGuid, Date date, String areacode) {
        FrameOu ou = iOuService.getOuByOuGuid(ouGuid);
        String ouname = null;
        if (ou != null) {
            ouname = ou.getOuname();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        String msg = sdf1.format(date);

        String Content = ouname + "," + msg + "政务服务网有一条咨询/投诉问题，请于3个工作日内通过一窗受理系统受理回复";

        List<String> moblies = service.findUserMoblieList(ouGuid);
        if (moblies != null && moblies.size() > 0) {
            for (String mobile : moblies) {
                if (StringUtil.isNotBlank(mobile)) {
                    // 电话号码
                    centerService.insertSmsMessage(UUID.randomUUID().toString(), Content, date, 0, null, mobile, "", "",
                            "", "", "", ouGuid, ouGuid, false, areacode);
                }
            }

        }

    }

    public String replaceNameX(String str) {
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int i = 0;
        while (m.find()) {
            i++;
            if (i == 1)
                continue;
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
