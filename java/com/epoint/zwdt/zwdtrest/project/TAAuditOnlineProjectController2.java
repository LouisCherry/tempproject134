
package com.epoint.zwdt.zwdtrest.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyLegal;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.onlinecloud.cloudfiles.domain.AuditOnlineCloudFiles;
import com.epoint.cert.basic.onlinecloud.cloudfiles.inter.IAuditOnlineCloudFiles;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;
import com.epoint.jnzwdt.zczwdt.api.IAuditPromiseBookService;
import com.epoint.jnzwdt.zczwdt.api.entity.AuditPromiseBook;
import com.epoint.wechat.project.api.IWeiChatProjectService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.api.IJnService;
import com.epoint.xmz.auditprojectdelivery.api.IAuditProjectDeliveryService;
import com.epoint.xmz.auditprojectdelivery.api.entity.AuditProjectDelivery;
import com.epoint.xmz.certcbyyysz.api.ICertCbyyyszService;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.xmz.rs.api.ICertGgxkwsService;
import com.epoint.xmz.rs.api.entity.CertGgxkws;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.zwdt.auditsp.handleproject.api.ITAHandleProject;
import com.epoint.zwdt.auditsp.handleproject.impl.TAHandleProjectImpl;
import com.epoint.zwdt.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.zwdt.teacherhealthreport.api.entity.TeacherHealthReport;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.AESUtil;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.zwdt.zwdtrest.project.impl.BjwsService;

@RestController
@RequestMapping("/tazwdtProject2")
public class TAAuditOnlineProjectController2
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");

    /**
     * 获取配置文件参数
     */
    private static String tazwfwrootpath = ConfigUtil.getConfigValue("taconfig", "tazwfwrootpath");

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 办件材料API
     */
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;
    /**
     * 事项基本信息API
     */
    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ICxBusService iCxBusService;
    /**
     * 事项扩展信息API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    /**
     * 事项扩展信息API
     */
    @Autowired
    private IJnProjectService iJnProjectService;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;

    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;

    @Autowired
    private IAuditProjectFormSgxkzDantiService iAuditProjectFormSgxkzDantiService;
    /**
     * 事项多情形API
     */
    @Autowired
    private IAuditTaskCase iAuditTaskCase;

    @Autowired
    private IJnService iJnService;
    /**
     * 事项情形与材料关系API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;
    /**
     * 事项审批结果API
     */
    @Autowired
    private IAuditTaskResult iAuditTaskResult;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    /**
     * 办件操作API
     */
    @Autowired
    private IHandleProject iHandleProject;

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditPromiseBookService promiseBookService;

    /**
     * 泰安办件操作API
     */
    @Autowired
    private ITAHandleProject iTaHandleProject;
    @Autowired
    private ICertGgxkwsService iCertGgxkwsService;
    @Autowired
    private ICertCbyyyszService iCertCbyyyszService;
    /**
     * 办件评价API
     */
    @Autowired
    private IAuditOnlineEvaluat iAuditOnlineEvaluat;

    @Autowired
    private ICertHwslysjyxkService iCertHwslysjyxkService;
    /**
     * 办件剩余时间API
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    /**
     * 办件特殊操作API
     */
    @Autowired
    private IAuditProjectUnusual iAuditProjectUnusual;
    /**
     * 办件文书快照API
     */
    @Autowired
    private IAuditProjectDocSnap iAuditProjectDocSnap;
    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;
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
     * 主题实例材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    /**
     * 网上用户注册API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 政务云盘文件API
     */
    @Autowired
    private IAuditOnlineCloudFiles iAuditOnlineCloudFiles;
    /**
     * 证照基本信息API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    /**
     * 事项授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 法定代表人API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 事项基础窗口API
     */
    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;
    /**
     * 证照库附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    /**
     * 系统参数API
     */
    @Autowired
    private IHandleConfig handleConfigService;

    /**
     * 法定代表人信息API
     */
    @Autowired
    private IAuditRsCompanyLegal iAuditRsCompanyLegal;

    @Autowired
    private ISendMaterials sendMaterials;

    @Autowired
    private IOuService ouService;

    @Autowired
    private IConfigService iConfigService;

    /**
     * 微信办件API
     */
    @Autowired
    private IWeiChatProjectService weiChatProjectService;

    /**
     * 教师健康体检api
     */
    @Autowired
    private ITeacherHealthReportService iTeacherHealthReportService;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private IAuditLogisticsBasicInfo iLogisticsBasicInfo;

    @Autowired
    private IAuditProjectDeliveryService deliveryService;

    @Autowired
    private ISendMQMessage sendMQMessageService;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    private static final String sendFlowsnPath = "http://112.6.110.176:25001/jnzwdt/rest//jnrshttpapi/jnzwfw/approve/sendApplyNO";

    /*********************************************
     * 办件公示
     *******************************************************/

    /**
     * 微信获取结果公示办件列表的接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getWeChatProject", method = RequestMethod.POST)
    public String getWeChatProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getWeChatProject接口=======");
            // 1、接口的入参转化为JSON对象
            if (StringUtil.isNotBlank(params)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = JSONObject.parseObject(params);
                String token = jsonObject.getString("token");
                if (ZwdtConstant.SysValidateData.equals(token)) {
                    JSONObject obj = (JSONObject) jsonObject.get("params");
                    // 1.1、获取当前页码
                    String currentPage = obj.getString("currentpage");
                    // 1.2、获取一页显示数量
                    String pageSize = obj.getString("pagesize");
                    // 1.3、获取搜索内容
                    String project = obj.getString("taskname");
                    String applyername = obj.getString("applyername");
                    // 1.4、获取办件的状态（0:全部 1:待提交 2:待补正 3:待提交原件 4:待缴费 5:审批中 6:待签收
                    // 7:待评价 8：已办结 9:其他）
                    // String status = obj.getString("status");
                    // 1.5、获取区域编码
                    String areaCode = obj.getString("areacode");
                    // 1.5、获取时间编码
                    Date starttime = obj.getDate("starttime");
                    Date endtime = obj.getDate("endtime");

                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件
                    if (StringUtil.isNotBlank(applyername)) {
                        sqlConditionUtil.like("applyername", applyername); // 申请人
                    }
                    if (StringUtil.isNotBlank(project)) {
                        sqlConditionUtil.like("projectname", project); // 事项名称
                    }
                    if (StringUtil.isNotBlank(starttime)) {
                        String start = EpointDateUtil.convertDate2String(starttime, EpointDateUtil.DATE_FORMAT);
                        sqlConditionUtil.ge("applydate", "'" + start + "'"); // 开始时间
                    }
                    if (StringUtil.isNotBlank(endtime)) {
                        String end = EpointDateUtil.convertDate2String(endtime, EpointDateUtil.DATE_FORMAT);
                        sqlConditionUtil.le("applydate", "'" + end + "'"); // 结束时间
                    }
                    // 3.2、根据各种办件的状态拼接不同的SQL语句
                    sqlConditionUtil.ge("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));

                    sqlConditionUtil.nq("applyername", null);
                    // 3.4、根据辖区查询到办件数据
                    if (StringUtil.isNotBlank(areaCode)) {
                        sqlConditionUtil.eq("areaCode", areaCode);
                    }

                    // 3.4、根据条件查询到办件数据
                    String sortField = "applydate";
                    String sortOrder = "desc";
                    List<AuditProject> list = weiChatProjectService.findListByPage(sqlConditionUtil,
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize) - Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), sortField, sortOrder);

                    int totalCount = list.size();
                    int total = weiChatProjectService.getTotalNum(sqlConditionUtil);
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
                    for (AuditProject auditProject : list) {
                        String applyer = auditProject.getApplyername();
                        if (StringUtil.isNotBlank(applyer)) {

                            // 4.1、如果是普通办件，返回普通办件的JSON数据
                            String projectGuid = auditProject.getRowguid();
                            // 4.1.1、获取事项基本信息
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                    .getResult();
                            if (auditTask != null) {
                                JSONObject projectJson = new JSONObject();
                                String projectname = auditProject.getProjectname();
                                // 4.1.2.1、获取办件的基本信息拼接JSON
                                projectJson.put("projectname", "关于" + applyer + projectname + "的办件"); // 事项名称
                                projectJson.put("applyername", applyer);// 申请人
                                projectJson.put("projectguid", projectGuid); // 办件标识
                                projectJson.put("flowsn", auditProject.getFlowsn()); // 办件编号

                                String projectType = auditProject.getBanjieresult().toString();
                                switch (projectType) {
                                    case "31":
                                        projectType = "不予受理";
                                        break;
                                    case "40":
                                        projectType = "准予许可";
                                        break;
                                    case "50":
                                        projectType = "不予许可";
                                        break;
                                    case "98":
                                        projectType = "撤销申请";
                                        break;
                                    case "99":
                                        projectType = "异常终止";
                                        break;
                                    default:
                                        projectType = "准予许可";
                                        break;
                                }

                                projectJson.put("projectstatus", projectType); // 办件状态
                                projectJson.put("applydate", EpointDateUtil.convertDate2String(
                                        auditProject.getApplydate(), EpointDateUtil.DATE_TIME_FORMAT)); // 办件申请时间

                                projectJsonList.add(projectJson);
                            }
                        }
                    }
                    dataJson.put("projectlist", projectJsonList);
                    dataJson.put("count", totalCount);
                    dataJson.put("total", total);
                    log.info("=======结束调用getWeChatProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数值为空！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getWeChatProject异常信息：" + e.getMessage() + "=======");
            log.info("=======getWeChatProject接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的办件异常：" + e.getMessage(), "");
        }
    }

    /**
     * 微信获取办件详情的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getWeChatProjectDetail", method = RequestMethod.POST)
    public String getWeChatProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getWeChatProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件guid
                String projectguid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");

                // 2.1、根据条件查询到办件数据
                String fields = "rowguid,projectname,applyername,taskguid,centerguid,flowsn,status,OperateDate,APPLYDATE,APPLYERTYPE,BANJIEDATE,areacode,pviguid,banjieresult,ACCEPTUSERDATE,ACCEPTUSERNAME,ouname,ouguid ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectguid, areaCode)
                        .getResult();

                // 3、定义返回JSON对象
                JSONObject projectJson = new JSONObject();
                if (auditProject != null) {
                    String applyer = auditProject.getApplyername();
                    if (StringUtil.isNotBlank(applyer)) {
                        // 4.1、如果是普通办件，返回普通办件的JSON数据
                        String projectGuid = auditProject.getRowguid();
                        // 4.1.1、获取事项基本信息
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true)
                                .getResult();

                        if (auditTask != null) {

                            // 4.1.2、获取办件基本的信息拼接JSON
                            String taskType = auditTask.getShenpilb(); // 事项类型
                            switch (taskType) {
                                case "1":
                                    taskType = "即办件";
                                    break;
                                case "3":
                                    taskType = "上报件";
                                    break;
                                case "4":
                                    taskType = "代办件";
                                    break;
                                default:
                                    taskType = "承诺件";
                                    break;
                            }

                            String projectname = auditProject.getProjectname();

                            // 4.1.2.1、获取办件的基本信息拼接JSON
                            projectJson.put("projectname", "关于" + applyer + projectname + "的申请"); // 事项名称
                            projectJson.put("applyername", applyer);// 申请人
                            projectJson.put("projectguid", projectGuid); // 办件标识
                            projectJson.put("taskguid", auditProject.getTaskguid()); // 事项标识
                            projectJson.put("taskname", auditTask.getTaskname()); // 事项标识
                            projectJson.put("centerguid", auditProject.getCenterguid()); // 中心标识
                            projectJson.put("type", taskType); // 事项类型
                            projectJson.put("flowsn", auditProject.getFlowsn()); // 办件编号
                            projectJson.put("acceptusername", auditProject.getAcceptusername()); // 办理人
                            projectJson.put("acceptuserdate", EpointDateUtil.convertDate2String(
                                    auditProject.getAcceptuserdate(), EpointDateUtil.DATE_TIME_FORMAT)); // 受理时间

                            // 办结类型 不予受理 31 准予许可 40 不予许可50 撤销申请 98 异常终止 99
                            String projectType = auditProject.getBanjieresult().toString();
                            switch (projectType) {
                                case "31":
                                    projectType = "不予受理";
                                    break;
                                case "40":
                                    projectType = "准予许可";
                                    break;
                                case "50":
                                    projectType = "不予许可";
                                    break;
                                case "98":
                                    projectType = "撤销申请";
                                    break;
                                case "99":
                                    projectType = "异常终止";
                                    break;
                                default:
                                    projectType = "准予许可";
                                    break;
                            }

                            projectJson.put("projectstatus", projectType); // 办结状态
                            projectJson.put("ouname", auditProject.getOuname()); // 办件受理部门
                            projectJson.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                    EpointDateUtil.DATE_TIME_FORMAT)); // 办件申请时间

                            String applyType = auditProject.getApplyertype().toString(); // 办件申请人类型

                            projectJson.put("applyertype", applyType); // 办件申请人类型
                            // 10 法人
                            // 20 个人
                            // 4.1.2.2、获取办件办结时间
                            String banJieData = "";
                            if (auditProject.getBanjiedate() != null) {
                                banJieData = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),
                                        EpointDateUtil.DATE_TIME_FORMAT);
                            }
                            projectJson.put("banjiedate", banJieData);// 办件办结时间
                        }
                    }
                    // dataJson.put("projectlist", projectJson);
                    log.info("=======结束调用getWeChatProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件成功", projectJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "未获取到办件", projectJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getWeChatProjectDetail接口参数：params【" + params + "】=======");
            log.info("=======getWeChatProjectDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 更新微信提交办件状态接口
     *
     * @param params
     * @return
     * @authory shibin
     * @version 2019年9月27日 下午4:46:31
     */
    @RequestMapping(value = "/private/updateWechatProjectInfo", method = RequestMethod.POST)
    public String updateWechatProjectInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用updateWechatProjectInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");

                // 6、更新AUDIT_PROJECT表数据
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                AuditProject auditProject = weiChatProjectService.getAuditProjectByRowGuid(projectGuid);
                auditProject.setStatus(24);
                auditProject.setIs_test(0);
                auditProject.setAreacode(auditTask.getAreacode());
                iAuditProject.updateProject(auditProject);

                return JsonUtils.zwdtRestReturn("1", "更新办件成功!", "");

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateWechatProjectInfo接口参数：params【" + params + "】=======");
            log.info("=======updateWechatProjectInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "更新办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 微信端办件初始化（办件申报页面初始化调用） 不需要用户信息
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/weChatInitProjectReturnMaterials", method = RequestMethod.POST)
    public String weChatInitProjectReturnMaterials(@RequestBody String params) {
        try {
            log.info("=======开始调用weChatInitProjectReturnMaterials接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取区域标识
                String areacode = obj.getString("areacode");
                areacode = areacode.substring(0, 6);// 用来解决乡镇事项无法进入分表的问题
                // 1.3、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.4、获取中心标识
                String centerGuid = obj.getString("centerguid");
                // 1.5、获取申请人类型（10:企业 20:个人）
                String applyerType = obj.getString("applyertype");
                // 设置申请人姓名
                String applyerUserName = obj.getString("applyerUserName");
                // 申请人证照编号
                String Idnumber = obj.getString("Idnumber");
                // 1.6、申请人类型如果为空则默认为个人
                applyerType = StringUtil.isBlank(applyerType) ? ZwfwConstant.APPLAYERTYPE_GR : applyerType;
                // 1.7、是否为手机申报
                String isMobile = obj.getString("ismobile");
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                if (auditTask == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询事项失败", "");
                }
                else {
                    if (StringUtil.isBlank(auditTask.getAreacode())) {
                        return JsonUtils.zwdtRestReturn("0", "配置事项辖区错误", "");
                    }
                    else {
                        // areacode = auditTask.getAreacode();
                        // 1.7、获取用户注册信息
                        // 2、获取用户信息
                        /*
                         * AuditOnlineIndividual auditOnlineIndividual =
                         * iAuditOnlineIndividual
                         * .getIndividualByAccountGuid(auditOnlineRegister.
                         * getAccountguid()).getResult();
                         */
                        // 2.1、设置办件标识
                        String projectGuid = UUID.randomUUID().toString();
                        // 2.2、设置申请人标识(个人类型默认为申请人Applyerguid，企业默认为空)
                        String applyerGuid = "";
                        // 2.3、设置申请人姓名（申请人类型为个人则申请人姓名为个人信息中的姓名，申请人类型为企业则为空）
                        // String applyerUserName = "";
                        // 2.4、设置申请人证照编号（申请人类型为个人则申请人证照编号为个人信息中的身份证，申请人类型为企业则为空）
                        String certNum = "";
                        List<AuditProjectMaterial> auditProjectMaterials = null;
                        // 2.5、如果是手机提交申报的办件
                        if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                            applyerGuid = UUID.randomUUID().toString();
                            applyerUserName = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType) ? applyerUserName : "";
                            certNum = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType) ? Idnumber : "";
                            auditProjectMaterials = iHandleProject
                                    .InitOnlineProjectReturnMaterials(taskGuid, centerGuid, areacode, applyerGuid,
                                            applyerUserName, certNum, projectGuid, taskCaseGuid, applyerType)
                                    .getResult();
                        }

                        // 4、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        List<JSONObject> materialJsonlist = new ArrayList<JSONObject>();
                        if (auditProjectMaterials != null && auditProjectMaterials.size() > 0) {
                            for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                        .getResult();
                                JSONObject materialJson = new JSONObject();
                                // 4.1、获取办件材料基本信息返回JSON
                                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 办件材料名称
                                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid());// 办件材料关联共享材料实例标识
                                materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                                materialJson.put("type", auditTaskMaterial.getType());// 材料类型
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                                // 4.2、返回材料来源，默认为申请人自备
                                String materialSource;
                                if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialSource = "申请人自备";
                                }
                                else {
                                    materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource",
                                        StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                                // 4.3、返回材料必要性（默认为事项材料中设置，如果有多情形需要按照多情形中材料必要性）
                                int necessity = auditTaskMaterial.getNecessity();
                                if (StringUtil.isNotBlank(taskCaseGuid)) {
                                    necessity = auditTaskMaterial.getNecessity();
                                    // 当内网不勾选材料情形时，默认不显示该材料
                                    if (ZwdtConstant.INT_NO == necessity) {
                                        continue;
                                    }
                                }
                                // 首先判断情形，若存在情形，则所有材料为必须材料
                                if (StringUtil.isNotBlank(taskCaseGuid)) {
                                    materialJson.put("necessary", "1");
                                }
                                else {
                                    if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                        materialJson.put("necessary", "0");
                                    }
                                    else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                        materialJson.put("necessary", "1");
                                    }
                                }
                                // 4.4、返回空白表格的附件标识
                                if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                    int templateAttachCount = iAttachService
                                            .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                    if (templateAttachCount > 0) {
                                        materialJson.put("templateattachguid",
                                                auditTaskMaterial.getTemplateattachguid());
                                    }
                                }
                                // 4.4、返回填报示例的附件标识
                                if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                    int exampleAttachCount = iAttachService
                                            .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                    if (exampleAttachCount > 0) {
                                        materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                    }
                                }
                                // 4.5、返回办件材料对应的附件业务标识
                                String clientGuid = auditProjectMaterial.getCliengguid();
                                materialJson.put("clientguid", StringUtil.isNotBlank(clientGuid) ? clientGuid : "");
                                // 4.6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                                // 在线填表、4:已填表）
                                int count = iAttachService.getAttachCountByClientGuid(clientGuid); // 获取办件材料对应的附件数量
                                int showButton = 0; // 按钮显示方式
                                String needLoad = ZwdtConstant.CERTLEVEL_C
                                        .equals(auditProjectMaterial.get("materialLevel")) ? "1" : "0"; // 是否需要上传到证照库
                                // 0:不需要
                                // 、1:需要
                                if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                    // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                    if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                        // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                        showButton = count > 0 ? 4 : 3;
                                    }
                                    else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                        // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                        showButton = count > 0 ? 1 : 0;
                                    }
                                }
                                else {
                                    // 4.6.2、如果关联了证照库
                                    if (count > 0) {
                                        // 4.6.2.1、获取材料类别
                                        String materialType = auditProjectMaterial.get("materialType");
                                        if (Integer.parseInt(materialType) == 1) {
                                            // 4.6.2.1.1、已引用证照库
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                        }
                                        else if (Integer.parseInt(materialType) == 2) {
                                            // 4.6.2.1.2、已引用批文
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                        }
                                        else {
                                            // 4.6.2.1.3、已引用材料
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                        }
                                    }
                                    else {
                                        // 4.6.2.2、如果没有附件，则标识为未上传
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                    }
                                }
                                materialJson.put("needload", needLoad);
                                materialJson.put("showbutton", showButton);
                                materialJsonlist.add(materialJson);
                            }
                        }
                        // 5、对返回的材料列表进行排序
                        Collections.sort(materialJsonlist, new Comparator<JSONObject>()
                        {
                            @Override
                            public int compare(JSONObject b1, JSONObject b2) {
                                Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                                Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                                // 5.1、优先对比材料必要性（必要在前）
                                int comNecessity = necessaryb2.compareTo(necessaryb1);
                                int ret = comNecessity;
                                // 5.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                                    Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                                    ret = ordernumb2.compareTo(ordernumb1);
                                }
                                return ret;
                            }
                        });
                        dataJson.put("materiallist", materialJsonlist);
                        dataJson.put("projectguid", projectGuid);
                        log.info("=======结束调用initProjectReturnMaterials接口=======");
                        return JsonUtils.zwdtRestReturn("1", "初始化办件成功", dataJson.toString());
                    }

                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======weChatInitProjectReturnMaterials接口参数：params【" + params + "】=======");
            log.info("=======weChatInitProjectReturnMaterials异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "初始化办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 微信提交办件的接口（办件申报提交调用） 不要用户信息
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitWechatProjectByTaskguid", method = RequestMethod.POST)
    public String submitWechatProjectByTaskguid(@RequestBody String params) {
        try {
            log.info("=======开始调用submitWechatProjectByTaskguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域编码
                // String areaCode = obj.getString("areacode");
                // 1.4、获取法人代表
                String legal = obj.getString("legal");
                // 1.5、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.6、获取申请人证照编号
                String certNum = obj.getString("idcard");
                // 1.7、获取证照类型
                String certType = obj.getString("certtype");
                // 1.8、获取申请人类型
                String applyerType = obj.getString("applyertype");
                // 1.9、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.10、获取联系人手机号
                String contactMobile = obj.getString("contactmobile");
                // 1.11、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.12、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.13、获取邮编
                String postCode = obj.getString("postcode");
                // 1.14、获取通讯地址
                String address = obj.getString("address");
                // 1.15、获取备注
                String remark = obj.getString("remark");
                // 1.16、获取电子邮箱
                String email = obj.getString("email");
                // 1.17、获取传真
                String fax = obj.getString("fax");
                // 1.19、获取是否需要快递
                String ifExpress = obj.getString("if_express");
                // 1.22、手机端申报参数
                String isMobile = obj.getString("ismobile");
                // ------ 手机端逻辑 -----
                if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    int oldstatus = 24; // 预审打回时使用
                    // 4、获取事项扩展信息
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 5、获取办件状态，如网上申报类型是 网上申报后直接办理，则办件状态更新为已接件
                    int status = ZwfwConstant.BANJIAN_STATUS_DJJ;// 默认办件状态：外网申报已提交
                    if (ZwfwConstant.WEB_APPLY_TYPE_SL.equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                        status = ZwfwConstant.BANJIAN_STATUS_DJJ;
                    }
                    // 6、更新AUDIT_ONLINE_PROJECT表数据
                    Map<String, String> updateFieldMap = new HashMap<>(16);
                    updateFieldMap.put("applyername=", applyerName);
                    updateFieldMap.put("status=", String.valueOf(status));
                    Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                    if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                        updateDateFieldMap.put("applydate=",
                                EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                    }
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("sourceguid", projectGuid);
                    sqlConditionUtil.eq("applyerguid", "");
                    iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                            sqlConditionUtil.getMap());
                    // 7、更新AUDIT_PROJECT表数据
                    AuditProject auditProject = new AuditProject();
                    // 8.1、设置办件证照类型
                    if (StringUtil.isBlank(certType)) {
                        // 8.1.1、如果证照类型为空，需要根据申请人类型设置默认值
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 8.1.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 8.1.1.2、若申请人类型为企业，则默认设置证照类型为组织机构代码证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                        }
                    }
                    else {
                        // 8.1.2、如果证照类型不为空，则获取传递的证照类型
                        auditProject.setCerttype(certType);
                    }
                    auditProject.setRowguid(projectGuid);
                    if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                        if(auditProject.getApplydate()==null){
                            auditProject.setApplydate(new Date());
                        }
                    }

                    auditProject.setApplyertype(Integer.parseInt(applyerType));
                    auditProject.setCertnum(certNum);
                    auditProject.setApplyername(applyerName);
                    auditProject.setContactperson(contactName);
                    auditProject.setContactmobile(contactPhone);
                    auditProject.setContactphone(contactMobile);
                    auditProject.setContactpostcode(postCode);
                    auditProject.setContactcertnum(contactIdnum);
                    auditProject.setRemark(remark);
                    auditProject.setAddress(address);
                    auditProject.setLegal(legal);
                    auditProject.setLegalid("");
                    auditProject.setContactemail(email);
                    auditProject.setContactfax(fax);
                    auditProject.setIf_express(ifExpress);
                    auditProject.setStatus(status);
                    auditProject.setOnlineapplyerguid("");
                    auditProject.setIs_test(0);
                    auditProject.setApplyway(11);
                    iAuditProject.updateProject(auditProject);

                    // 更新材料状态
                    // weiChatTaskService.updateProjectMaterialStatus(projectGuid);
                }

                log.info("=======结束调用submitWechatProjectByTaskguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交办件成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitProject接口参数：params【" + params + "】=======");
            log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    /****************************************************************************************************/

    /**
     * 申报须知接口(办件申报须知调用)
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/declareProjectNotice", method = RequestMethod.POST)
    public String declareProjectNotice(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用declareProjectNotice接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskGuid = obj.getString("taskguid");
                // 2、获取事项基本信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    // 3、获取事项扩展信息
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 5、设置需要返回的事项基本信息
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id()); // 事项业务唯一标识
                    dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                    dataJson.put("taskicon", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码图标内容
                    JSONObject taskElementJson = new JSONObject();
                    String applytype = String.valueOf(auditTaskExtension.getWebapplytype());
                    taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(applytype) ? "0" : "1");// 是否可以网上申报
                    taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                    dataJson.put("taskelement", taskElementJson);
                    dataJson.put("spcondition", auditTask.getAcceptcondition()); // 审批条件
                    // 6、获取事项多情形数据
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseJsonList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && auditTaskCases.size() > 0) {
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            JSONObject caseJson = new JSONObject();
                            caseJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                            caseJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                            caseJsonList.add(caseJson);
                        }
                    }
                    dataJson.put("taskcasecount", caseJsonList.size());
                    dataJson.put("casecondition", caseJsonList);
                    if (caseJsonList.size() == 0) {
                        // 7、没有多情形的情况下需要返回事项配置的材料，多情形情况下会调用下面接口返回事项材料数据
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 7.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>()
                        {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 8.1.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 8.1.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            materialJsonList.add(materialJson);
                        }
                        dataJson.put("taskmateriallist", materialJsonList);
                    }
                    log.info("=======结束调用declareProjectNotice接口=======");
                    return JsonUtils.zwdtRestReturn("1", "申报须知信息获取成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取多情形材料列表(办件申报须知多情形下调用)
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskCaseMaterialsByCaseguid", method = RequestMethod.POST)
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskCaseMaterialsByCaseguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.2、获取事项标识
                // String taskGuid = obj.getString("taskguid");
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                // 2、获取多情形材料列表
                if (StringUtil.isNotBlank(taskCaseGuid)) {
                    List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                            .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                    // 2.1、材料排序（必要的在前）
                    Collections.sort(auditTaskMaterialCases, new Comparator<AuditTaskMaterialCase>()
                    {
                        @Override
                        public int compare(AuditTaskMaterialCase b1, AuditTaskMaterialCase b2) {
                            return b1.getNecessity().compareTo(b2.getNecessity());
                        }
                    });
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        if (auditTaskMaterialCase != null) {

                            // int necessity =
                            // auditTaskMaterialCase.getNecessity();
                            // 3、当内网不勾选材料情形时，默认不显示该材料
                            if (ZwdtConstant.INT_NO == auditTaskMaterialCase.getNecessity()) {
                                continue;
                            }
                            // 4、获取情形与材料关系获取事项材料信息
                            AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                    .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid()).getResult();
                            JSONObject materialJson = new JSONObject();
                            materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            // 4.1、获取填报示例对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachCount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }
                            // 4.2、获取空白模板对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachCount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            // 4.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource", materialsource);
                            // 4.4、获取事项材料必要性
                            String necessary = "0";
                            if (auditTaskMaterialCase != null) {
                                necessary = ZwfwConstant.NECESSITY_SET_YES
                                        .equals(String.valueOf(auditTaskMaterialCase.getNecessity())) ? "1" : "0";// 是否必需
                            }
                            materialJson.put("necessary", necessary);
                            materialJson.put("pagenum", auditTaskMaterial.getPage_num()); // 份数
                            materialJson.put("standard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());// 受理标准
                            materialJson.put("fileexplain",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无"
                                            : auditTaskMaterial.getFile_explian());// 填报须知
                            materialJsonList.add(materialJson);
                        }
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("taskmateriallist", materialJsonList);
                log.info("=======结束调用getTaskCaseMaterialsByCaseguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件基本信息（办件申报信息调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getProjectAndTaskBasic", method = RequestMethod.POST)
    public String getProjectAndTaskBasic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectAndTaskBasic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域标识
                String areaCode = obj.getString("areacode");
                // 1.4、获取中心标识
                String centerguid = obj.getString("centerguid");
                // 1.5、申请人类型
                String applyertype = obj.getString("applyertype");
                // 1.5、获取用户基本信息 个性化
                /*
                 * String auditOnlineRegisterStr =
                 * jsonObject.getString("auditonlineregister");
                 * AuditOnlineRegister auditOnlineRegister =
                 * JSON.parseObject(auditOnlineRegisterStr,
                 * AuditOnlineRegister.class);
                 */
                // 个性化 获取地址
                String requestUrl = jsonObject.getString("qurl");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    JSONObject applyerJson = new JSONObject();
                    String acceptworkingday = handleConfigService.getFrameConfig("Public_AcceptWorkingDay", centerguid)
                            .getResult();
                    dataJson.put("acceptworkingday", acceptworkingday);
                    // 5、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                    if (auditTask != null) {

                        String AESInfo = getAESInfo(taskGuid, auditOnlineRegister);

                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("department", auditTask.getOuname());// 部门名称
                        dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        dataJson.put("taskicon", requestUrl
                                + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码内容

                        //// 是否禁用
                        dataJson.put("is_enable", auditTask.getIs_enable());
                        //// 是否网厅展示事项
                        dataJson.put("iswtshow", auditTask.get("iswtshow"));

                        areaCode = auditTask.getAreacode();
                        dataJson.put("areacode", areaCode);// 区域编码
                        if (StringUtil.isNotBlank(auditTask.getTaskname())
                                && auditTask.getTaskname().contains("公共场所卫生许可") && "370883".equals(areaCode)) {
                            // 辖区是邹城的
                            dataJson.put("isShowTip", 1);
                        }
                        // 引导视频
                        if (StringUtil.isNotBlank(auditTaskExtension.get("pcspydguid"))) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(auditTaskExtension.getStr("pcspydguid"));
                            if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                dataJson.put("pcspydurl", WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + frameAttachInfos.get(0).getAttachGuid());
                            }
                        }

                        // 个别事项需要弹出受理通知书
                        if ("11370800MB285591843370123029001".equals(auditTask.getItem_id())
                                || "11370800MB285591843371073014000".equals(auditTask.getItem_id())) {
                            dataJson.put("isPostInfo", "1");
                        }

                        // dataJson.put("isShowTip", 1);
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                            dataJson.put("promiseday", "1");// 即办件显示1天
                        }
                        else {
                            dataJson.put("promiseday", auditTask.getPromise_day());
                        }
                        JSONObject taskElementJson = new JSONObject();
                        taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype())) ? "0" : "1");// 是否可以网上申报
                        taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                        dataJson.put("taskelement", taskElementJson);
                        // 6、获取用户个人信息
                        AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                        if (auditOnlineRegister != null && auditOnlineIndividual != null) {
                            String peosonidnumber = auditOnlineIndividual.getIdnumber();

                            // 7、获取申请信息
                            applyerJson.put("declarername", auditOnlineIndividual.getClientname()); // 实际申请人（企业用）
                            applyerJson.put("applyername", auditOnlineIndividual.getClientname());// 申报人
                            applyerJson.put("contactperson", auditOnlineIndividual.getClientname());// 联系人
                            applyerJson.put("applyercard", peosonidnumber);// 申报人身份证
                            applyerJson.put("linkphone", auditOnlineRegister.getMobile());// 联系人手机
                            applyerJson.put("contactphone", auditOnlineRegister.getMobile());// 联系人电话
                            applyerJson.put("contactidnum", auditOnlineIndividual.getIdnumber());// 联系人身份证
                            applyerJson.put("certnum", auditOnlineIndividual.getIdnumber());// 联系人证照编号
                            if (StringUtil.isNotBlank(peosonidnumber) && "1".equals(auditTask.getStr("isbigshow"))) {
                                FrameAttachInfo attachinfo = iJnProjectService
                                        .getFrameAttachByIdenumber(peosonidnumber);
                                if (attachinfo == null) {
                                    applyerJson.put("needbigshow", "1");
                                }

                                List<FrameAttachInfo> attachinfos = iJnProjectService
                                        .getFrameAttachByIdenumberBigType(peosonidnumber, "爱山东身份证");
                                if (attachinfos == null || attachinfos.size() < 1) {
                                    applyerJson.put("asdbigshow", "1");
                                }
                            }
                            // 7.1、当申请人为个人的时候，默认带出个人信息
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyertype)) {
                                applyerJson.put("address", auditOnlineIndividual.getDeptaddress()); // 地址
                                applyerJson.put("postcode", auditOnlineIndividual.getContactpostcode()); // 邮编
                                applyerJson.put("email", auditOnlineIndividual.getContactemail()); // Email
                                applyerJson.put("fax", auditOnlineIndividual.getContactfax()); // Email
                            }

                            String projectname = auditTask.getTaskname();
                            /*
                             * if (projectname.contains("建筑业企业资质（简单变更）（企业名称变更）")
                             * || projectname.contains("建筑业企业资质（简单变更）（法人变更）") ||
                             * projectname.contains("建筑业企业资质（简单变更）（注册资本变更）") ||
                             * projectname.contains("建筑业企业资质（简单变更）（多项变更）") ||
                             * projectname.contains("建筑业企业资质（简单变更）（经济性质变更）") ||
                             * projectname.contains("建筑业企业资质（简单变更）（企业地址变更）")) {
                             * dataJson.put("jnaisp", "1"); } else
                             */
                            if (projectname.contains("变更成品油零售经营批准证书企业名称或法定代表人")
                                    || projectname.contains("变更成品油零售经营批准证书企业名称和法定代表人")) {
                                dataJson.put("jnaisp", "2");
                            }
                            // else if(projectname.contains("建设工程质量检测机构资质审批")) {
                            // dataJson.put("jnaisp", "3");
                            // }
                            else if (projectname.contains("房地产开发企业暂定资质变更") || projectname.contains("房地产开发企业四级资质变更")
                                    || projectname.contains("房地产开发企业三级资质变更")
                                    || projectname.contains("房地产开发企业二级资质变更（多项变更）")) {
                                dataJson.put("jnaisp", "4");
                            }
                            else if (projectname.contains("统一社会信用代码信息服务")) {
                                dataJson.put("jnaisp", "5");
                            }
                            else if (projectname.contains("粮食收购资格备案")) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H437309800900101".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H4370114011007".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H4370115041000".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H4370173016000".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H437309800300101".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370800MB285591847370117036000".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                    && getIsAllZmMateri(projectGuid)) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if ("11370883MB2857374H437301100800101".equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "7");
                            }
                            else if (projectname.contains("公共场所卫生许可证注销") || projectname.contains("终止经营省际普通货物水路运输业务")
                            // ||
                            // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                            // 船舶注销
                                    || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 消毒水注销
                                    || "11370800MB28559184737011702000016".equals(auditTask.getItem_id())//
                                    || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                    || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                            ) {
                                dataJson.put("jnaisp", "6");
                            }
                            else if (getGgcsxsqConfig().equals(auditTask.getItem_id())) {
                                dataJson.put("jnaisp", "6");
                            }

                            if (StringUtil.isNotBlank(projectGuid)) {
                                /*
                                 * String fields =
                                 * " rowguid,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,legal,legalid,certtype,applyertype,applyername,applyeruserguid "
                                 * ; AuditProject auditProject = iAuditProject
                                 * .getAuditProjectByRowGuid(fields,
                                 * projectGuid, areaCode).getResult();
                                 */
                                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                                sqlConditionUtil.eq("SOURCEGUID", projectGuid);
                                sqlConditionUtil.isNotBlank("applyerguid");
                                AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                                        .getAuditOnlineProjectPageData(sqlConditionUtil.getMap(), 0, 0, null, null)
                                        .getResult().getList().get(0);
                                if (StringUtil.isBlank(applyertype)) {
                                    applyertype = auditOnlineProject.getApplyertype();
                                }
                                if (auditOnlineProject != null) {
                                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyertype)) {
                                        if (!auditOnlineIndividual.getApplyerguid()
                                                .equals(auditOnlineProject.getApplyerguid())) {
                                            return JsonUtils.zwdtRestReturn("0", "获取个人办件信息失败,登录人信息与所要访问的办件信息不匹配！", "");
                                        }
                                    }
                                    else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyertype)) {
                                        // 根据法人身份证查询企业列表
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("orgalegal_idnumber", auditOnlineIndividual.getIdnumber());
                                        sql.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                                        sql.eq("isactivated", ZwfwConstant.CONSTANT_STR_ONE); // 激活的企业
                                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                                                .selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
                                        // 如果企业列表里存在所登陆的企业，就将sign设置为false，不执行return语句
                                        boolean sign = true;
                                        // 存在法人办件，applyerguid值为auditOnlineIndividual.getApplyerguid()，此处特殊处理
                                        if (auditOnlineIndividual.getApplyerguid()
                                                .equals(auditOnlineProject.getApplyerguid())) {
                                            sign = false;
                                        }

                                        if (auditRsCompanyBaseinfoList != null
                                                && auditRsCompanyBaseinfoList.size() != 0) {
                                            // 如果登录人是法人本人
                                            for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                                                if (auditOnlineProject.getApplyerguid()
                                                        .equals(auditRsCompanyBaseinfo.getCompanyid())) {
                                                    sign = false;
                                                }
                                            }
                                            if (sign) {
                                                // 如果登录人是代办人
                                                SqlConditionUtil sqlCondite = new SqlConditionUtil();
                                                sqlCondite.eq("bsquserguid", auditOnlineIndividual.getAccountguid());
                                                sqlCondite.isBlankOrValue("isrelieve", "0"); // 是否解除授权
                                                List<AuditOnlineCompanyGrant> grants = iAuditOnlineCompanyGrant
                                                        .selectCompanyGrantByConditionMap(sqlCondite.getMap())
                                                        .getResult();
                                                for (AuditOnlineCompanyGrant grant : grants) {
                                                    if (auditOnlineProject.getApplyerguid()
                                                            .equals(grant.getCompanyid())) {
                                                        sign = false;
                                                    }
                                                }
                                                if (sign) {
                                                    return JsonUtils.zwdtRestReturn("0",
                                                            "获取法人办件信息失败,登录人信息与所要访问的办件信息不匹配！", "");
                                                }
                                            }
                                        }
                                        else {
                                            // 如果登录人是代办人
                                            SqlConditionUtil sqlCondite = new SqlConditionUtil();
                                            sqlCondite.eq("bsquserguid", auditOnlineIndividual.getAccountguid());
                                            sqlCondite.isBlankOrValue("isrelieve", "0"); // 是否解除授权
                                            List<AuditOnlineCompanyGrant> grants = iAuditOnlineCompanyGrant
                                                    .selectCompanyGrantByConditionMap(sqlCondite.getMap()).getResult();
                                            for (AuditOnlineCompanyGrant grant : grants) {
                                                if (auditOnlineProject.getApplyerguid().equals(grant.getCompanyid())) {
                                                    sign = false;
                                                }
                                            }
                                            if (sign) {
                                                return JsonUtils.zwdtRestReturn("0",
                                                        "获取法人办件信息失败,登录人信息与所要访问的办件信息不匹配！", "");
                                            }
                                        }

                                    }
                                }
                                AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);
                                if (auditProject != null) {
                                    // 安全性问题处理开始
                                    if (StringUtil.isBlank(applyertype)) {
                                        applyertype = auditProject.getApplyertype().toString();
                                    }
                                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyertype)) {
                                        if (!auditOnlineIndividual.getApplyerguid()
                                                .equals(auditProject.getApplyeruserguid())) {
                                            return JsonUtils.zwdtRestReturn("0", "获取办件信息失败！", "");
                                        }
                                    }

                                    // 安全性问题处理结束
                                    dataJson.put("bianhao", auditProject.getRemark());
                                    applyerJson.put("applyername", auditProject.getApplyername()); // 办件申报人
                                    applyerJson.put("contactperson", auditProject.getContactperson());// 联系人
                                    applyerJson.put("linkphone", auditProject.getContactmobile());// 联系人手机
                                    applyerJson.put("contactidnum", auditProject.getContactcertnum());// 联系人证照编号
                                    applyerJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                                    applyerJson.put("postcode", auditProject.getContactpostcode()); // 邮编
                                    applyerJson.put("address", auditProject.getAddress()); // 地址
                                    applyerJson.put("remark", auditProject.getRemark()); // 备注
                                    applyerJson.put("taskcaseguid", auditProject.getTaskcaseguid()); // 事项多情形标识
                                    applyerJson.put("email", auditProject.getContactemail());// 电子邮件
                                    applyerJson.put("fax", auditProject.getContactfax()); // 传真
                                    applyerJson.put("certtype", auditProject.getCerttype());
                                    applyerJson.put("legal", auditProject.getLegal());
                                    applyerJson.put("orgalegalidnumber", auditProject.getLegalid());
                                    applyerJson.put("certnum", auditProject.getCertnum());
                                    applyerJson.put("applyerguid", auditProject.getApplyeruserguid()); // 办件申报人
                                    // 个性化
                                    // applyerJson.put("searchpwd",
                                    // sendMaterials.getSearchPwd(projectGuid));
                                    applyerJson.put("searchpwd", auditProject.getStr("searchpwd"));
                                    applyerJson.put("flowsn", auditProject.getFlowsn());
                                    applyerJson.put("projectguid", auditProject.getRowguid());
                                    // 8、企业的applyerguid为企业的companyid
                                    if (ZwfwConstant.APPLAYERTYPE_QY
                                            .equals(String.valueOf(auditProject.getApplyertype()))) {
                                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                                .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid())
                                                .getResult();
                                        if (auditRsCompanyBaseinfo != null) {
                                            applyerJson.put("applyerguid", auditRsCompanyBaseinfo.getCompanyid()); // 办件申报人
                                        }
                                    }
                                }
                            }

                        }

                        // 新增两个代码项 寄件人地址、收件人地址
                        // 获取代码项目
                        List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName("辖区对应关系");
                        List<JSONObject> resultJsonList1 = new ArrayList<>();
                        JSONObject json1 = new JSONObject();
                        json1.put("optionvalue", "");
                        json1.put("optiontext", "请选择");
                        json1.put("isselected", 1);
                        resultJsonList1.add(json1);
                        for (CodeItems codeItems : itemtypes) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("optionvalue", codeItems.getItemValue());
                            objJson.put("optiontext", codeItems.getItemText());
                            resultJsonList1.add(objJson);
                        }

                        // 新增两个代码项 寄件人地址、收件人地址
                        List<JSONObject> resultJsonList2 = new ArrayList<>();
                        JSONObject json2 = new JSONObject();
                        json2.put("optionvalue", "");
                        json2.put("optiontext", "请选择");
                        json2.put("isselected", 1);
                        resultJsonList2.add(json2);
                        for (CodeItems codeItems : itemtypes) {
                            JSONObject objJson = new JSONObject();
                            objJson.put("optionvalue", codeItems.getItemValue());
                            objJson.put("optiontext", codeItems.getItemText());
                            resultJsonList2.add(objJson);
                        }

                        applyerJson.put("sendaddresslist", resultJsonList1);
                        applyerJson.put("backaddresslist", resultJsonList2);
                        if(StringUtils.isNotBlank(auditTask.getStr("IS_DELIVERY")) && "1".equals(auditTask.getStr("IS_DELIVERY"))){
                            AuditProjectDelivery delivery = deliveryService.getDeliveryByprojectguid(projectGuid);
                            if (delivery != null) {
                                if ("1".equals(delivery.getIssend())) {
                                    dataJson.put("issend", "1");
                                    if(StringUtils.isNotBlank(delivery.getRecipientname())){
                                        dataJson.put("recipientname", delivery.getRecipientname());
                                    }
                                    if(StringUtils.isNotBlank(delivery.getRecipientmobile())){
                                        dataJson.put("recipientmobile", delivery.getRecipientmobile());
                                    }
                                    if(StringUtils.isNotBlank(delivery.getRecipientaddress())){
                                        dataJson.put("recipientaddress", delivery.getRecipientaddress());
                                    }
                                }
                                else {
                                    dataJson.put("issend", "0");
                                }
                                if ("1".equals(delivery.getIssendback())) {
                                    dataJson.put("issendback", "1");
                                }
                                else {
                                    dataJson.put("issendback", "0");
                                }
                            }

                            if(StringUtils.isNotBlank(auditTaskExtension.getIf_express())&&"1".equals(auditTaskExtension.getIf_express())){
                                if(StringUtils.isBlank(dataJson.getString("recipientname"))){
                                    dataJson.put("recipientname", auditTaskExtension.getStr("ADDRESSEE"));
                                }
                                if(StringUtils.isBlank(dataJson.getString("recipientmobile"))){
                                    dataJson.put("recipientmobile", auditTaskExtension.getStr("addresseePhone"));
                                }
                                if(StringUtils.isBlank(dataJson.getString("recipientaddress"))){
                                    dataJson.put("recipientaddress", auditTaskExtension.getStr("transactADDR"));
                                }
                            }
                        }

//                        if (StringUtil.isNotBlank(auditTask.getOuname())) {
//                            FrameOu frameOu = ouService.getOuByOuGuid(auditTask.getOuguid());
//                            if (frameOu != null) {
//                                dataJson.put("recipientname", frameOu.getOuname());// 实施主体
//                            }
//                            else {
//                                dataJson.put("recipientname", auditTask.getOuname());// 实施主体
//                            }
//                        }

//                        dataJson.put("recipientmobile", auditTask.getLink_tel());// 咨询电话
                        String addresstime = auditTask.getStr("ACCEPT_ADDRESS_INFO");
                        List<Record> acceptaddinfos = new ArrayList<Record>();
//                        if (StringUtil.isNotBlank(addresstime)) {
//                            acceptaddinfos = syncbanlididiansj(addresstime);// 受理地点信息
//                            if (acceptaddinfos != null && !acceptaddinfos.isEmpty()) {
//                                dataJson.put("recipientaddress", acceptaddinfos.get(0).getStr("address"));// 收件人地址
//                            }
//                        }
                        applyerJson.put("ifexpress", auditTaskExtension.getIf_express());// 是否使用物流
                        dataJson.put("AESInfo", AESInfo);
                        dataJson.put("applerinfo", applyerJson);
                        log.info("=======结束调用getProjectAndTaskBasic接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取办件基本信息成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
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
            log.info("=======getProjectAndTaskBasic接口参数：params【" + params + "】=======");
            log.info("=======getProjectAndTaskBasic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件申报获取办件基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 同步办事流程
     */
    @SuppressWarnings("unchecked")
    public static List<Record> syncbanlididiansj(String banlididiansj) {
        List<Record> list = new ArrayList<Record>();
        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(banlididiansj);
            if (banlididiansj != null && StringUtil.isNotBlank(banlididiansj)) {
                if (document != null) {
                    Element root = document.getRootElement();
                    if (root == null) {
                        return list;
                    }
                    Element data1 = root.element("ACCEPT_ADDRESSS");
                    if (data1 == null) {
                        List<Element> dataflows = root.elements("node");
                        for (Element data : dataflows) {
                            Record record = new Record();
                            List<Element> content2 = data.content();
                            for (Element element : content2) {
                                String label = element.attributeValue("label");
                                String text = element.getText();
                                if ("OFFICE_HOUR".equals(label)) {
                                    record.set("officehour", text);
                                }
                                else if ("ADDRESS".equals(label)) {
                                    record.set("address", text);
                                }
                            }
                            list.add(record);
                        }
                        return list;
                    }
                    List<Element> datas = data1.elements("ACCEPT_ADDRESS");
                    if (datas == null) {
                        return list;
                    }
                    for (Element data : datas) {
                        Element NAME = data.element("NAME");
                        Element ADDRESS = data.element("ADDRESS");
                        Element OFFICE_HOUR = data.element("OFFICE_HOUR");
                        Element WINDOW_NUM = data.element("WINDOW_NUM");
                        Record record = new Record();
                        if (StringUtil.isNotBlank(NAME)) {
                            record.set("name", NAME.getStringValue());
                        }
                        if (StringUtil.isNotBlank(ADDRESS)) {
                            record.set("address", ADDRESS.getStringValue());
                        }
                        if (StringUtil.isNotBlank(OFFICE_HOUR)) {
                            record.set("officehour", OFFICE_HOUR.getStringValue());
                        }
                        if (StringUtil.isNotBlank(WINDOW_NUM)) {
                            record.set("windownum", WINDOW_NUM.getStringValue());
                        }

                        list.add(record);
                    }
                }
            }
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 获取秒批秒办办件告知基本信息（邹城个性化）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getMpmbGz", method = RequestMethod.POST)
    public String getMpmbGz(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMpmbGz接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域标识
                String areaCode = obj.getString("areacode");
                // 1.4、获取中心标识
                String centerguid = obj.getString("centerguid");
                // 1.5、申请人类型
                String applyertype = obj.getString("applyertype");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();

                    // 5、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                    if (auditTask != null) {

                        String AESInfo = getAESInfo(taskGuid, auditOnlineRegister);

                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        // 6、获取用户个人信息
                        AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                        if (auditOnlineRegister != null && auditOnlineIndividual != null) {
                            if (StringUtil.isNotBlank(projectGuid)) {
                                AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);

                                if (auditProject != null) {
                                    // 安全性问题处理开始
                                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyertype)) {
                                        if (!auditOnlineIndividual.getApplyerguid()
                                                .equals(auditProject.getApplyeruserguid())) {
                                            return JsonUtils.zwdtRestReturn("0", "获取办件信息失败！", "");
                                        }
                                    }

                                    // 安全性问题处理结束
                                    dataJson.put("bianhao", auditProject.getRemark());
                                    dataJson.put("applydate",
                                            new SimpleDateFormat("yyyy年MM月dd日").format(auditProject.getApplydate()));
                                    // 获取电子表单字段信息
                                    String jndzbdurl = ConfigUtil.getConfigValue("datasyncjdbc", "jndzbdurl");
                                    String jndzbdusername = ConfigUtil.getConfigValue("datasyncjdbc", "jndzbdusername");
                                    String jndzbdpassword = ConfigUtil.getConfigValue("datasyncjdbc", "jndzbdpassword");
                                    DataSourceConfig jndzbdDataSourceConfig = new DataSourceConfig(jndzbdurl,
                                            jndzbdusername, jndzbdpassword);
                                    ICommonDao dzbdDao = null;
                                    try {
                                        dzbdDao = CommonDao.getInstance(jndzbdDataSourceConfig);
                                        String sql = "SELECT * from formtable20220318104104 where rowguid=?";
                                        Record r = dzbdDao.find(sql, Record.class, projectGuid);
                                        dataJson.put("dwmc", r.get("mc"));
                                        dataJson.put("dz", r.get("dz"));
                                        dataJson.put("fddbr", r.get("fddbr"));
                                        dataJson.put("fuwfw", r.get("fuwfw"));
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    finally {
                                        if (dzbdDao != null) {
                                            dzbdDao.close();
                                        }
                                    }

                                }
                            }

                        }
                        dataJson.put("AESInfo", AESInfo);
                        log.info("=======结束调用getMpmbGz接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取秒批秒办告知基本信息成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
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
            log.info("=======getMpmbGz接口参数：params【" + params + "】=======");
            log.info("=======getMpmbGz异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取秒批秒办告知基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 执行电子签章材料自动上传的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/signatureMaterialAutoUpload", method = RequestMethod.POST)
    public String signatureMaterialAutoUpload(@RequestBody String params) {
        try {
            log.info("=======开始调用signatureMaterialAutoUpload接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject obj = JSONObject.parseObject(params);
            // 1.2、获取办件材料名称
            String materialname = obj.getString("materialname");
            // 1.7、获取办件标识
            String projectGuid = obj.getString("projectguid");
            // 1.7、电子表单传过来的附件下载地址
            String attachUrl = obj.getString("attachUrl");
            // attachUrl =
            // "http://192.168.219.160:7788/epoint-zwfwsform-web/rest/attachAction/getContent?isCommondto=true&attachGuid=a2ea9dec-c901-4238-b772-dd30bb875ea1";
            InputStream inputStream = getFileInputStreamByUrl(attachUrl);
            log.info("=======signatureMaterialAutoUpload接口入参=======》" + obj);
            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            sqlUtil.eq("projectguid", projectGuid);
            sqlUtil.eq("taskmaterial", materialname);
            AuditProjectMaterial auditProjectMaterial = new SQLManageUtil()
                    .getBeanByCondition(AuditProjectMaterial.class, sqlUtil.getMap());
            if (StringUtil.isBlank(auditProjectMaterial.getCliengguid())) {
                return JsonUtils.zwdtRestReturn("0", "projectGuidguid为" + projectGuid + "的办件未找到对应材料：" + materialname,
                        "");
            }
            if (inputStream == null) {
                return JsonUtils.zwdtRestReturn("0", "projectGuidguid为" + projectGuid + "的办件未找生成对应材料流：" + materialname,
                        "");
            }
            // 生成签章后的附件到网厅数据库
            String attachGuidQz = UUID.randomUUID().toString();
            // 附件入库
            FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
            frameAttachInfo.setAttachGuid(attachGuidQz);
            frameAttachInfo.setCliengGuid(auditProjectMaterial.getCliengguid());
            frameAttachInfo.setCliengTag("接收回调文件流");
            frameAttachInfo.setCliengInfo("电子印章签章文件");
            frameAttachInfo.setUploadUserDisplayName("在线三方对接签章上传");
            frameAttachInfo.setContentType(".pdf");
            frameAttachInfo.setAttachFileName(materialname + ".pdf");
            frameAttachInfo.setUploadDateTime(new Date());
            iAttachService.addAttach(frameAttachInfo, inputStream);
            // // 2、获取办件材料对应的附件数量
            int attachCount = iAttachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
            int intStatus = 10; // 材料未提交状态
            // 3、根据材料是否有附件及材料的提交状态更新材料的提交状态
            if (attachCount > 0) {
                intStatus = 20;
            }
            // 4、更新办件材料数据
            auditProjectMaterial.setStatus(intStatus);
            iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
            // 6、定义返回JSON对象
            log.info("=======结束调用signatureMaterialAutoUpload接口=======");
            return JsonUtils.zwdtRestReturn("1", "签章材料自动上传成功", "");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======signatureMaterialAutoUpload接口参数：params【" + params + "】=======");
            log.info("=======signatureMaterialAutoUpload异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "签章材料自动上传异常：" + e.getMessage(), "");
        }
    }

    public InputStream getFileInputStreamByUrl(String wordurl) {
        try {
            URL url = new URL(wordurl);
            if (wordurl.startsWith("https")) {
                disableSslVerification();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream in = conn.getInputStream();
                return in;
            }
            else {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream in = conn.getInputStream();
                return in;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager()
            {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            } };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询是否生成电子签章材料自动上传接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/isSignatureMaterialAutoUpload", method = RequestMethod.POST)
    public String isSignatureMaterialAutoUpload(@RequestBody String params) {
        try {
            log.info("=======开始调用isSignatureMaterialAutoUpload接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.2、获取taskguid
                String taskguid = obj.getString("taskguid");
                // 1.7、获取办件标识
                String projectGuid = obj.getString("projectguid");
                log.info("=======isSignatureMaterialAutoUpload接口入参2=======》" + obj);

                // String signaturetaskid =
                // iConfigService.getFrameConfigValue("signaturetaskid");
                if (StringUtil.isNotBlank(taskguid) && StringUtil.isNotBlank(projectGuid)) {
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                    String taskname = auditTask.getTaskname();
                    // 办件材料
                    AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                    // if (!signaturetaskid.contains(taskid)) {
                    // log.info("=======该事项非个性化签章事项taskid：" + taskid + "
                    // taskname:"
                    // +
                    // taskname);
                    // return JsonUtils.zwdtRestReturn("0", "该事项非个性化签章事项taskid："
                    // +
                    // taskid + " taskname:" + taskname, "");
                    // }
                    // 个性化签章事项
                    SqlConditionUtil sqlUtil = new SqlConditionUtil();
                    sqlUtil.eq("projectguid", projectGuid);
                    // 1.假装城市大型户外广告设置审核事项为高中、中职教师资格证书信息变更
                    if ("高中、中职教师资格证书信息变更".equals(taskname)) {
                        sqlUtil.eq("taskmaterial", "教师资格证书信息更正备案表");
                    }
                    // if ("城市大型户外广告设置审核".equals(taskname)) {
                    // sqlUtil.eq("taskmaterial", "教师资格证书信息更正备案表");
                    // }
                    // 2.高中、中职教师资格证书补办
                    else if ("高中、中职教师资格证书补办".equals(taskname)) {
                        sqlUtil.eq("taskmaterial", "教师资格证书补发换发申请表");
                    }
                    // 3.高中、中职教师资格认定申请表补办
                    else if ("高中、中职教师资格认定申请表补办".equals(taskname)) {
                        sqlUtil.eq("taskmaterial", "教师资格认定申请表（补）");
                    }
                    auditProjectMaterial = new SQLManageUtil().getBeanByCondition(AuditProjectMaterial.class,
                            sqlUtil.getMap());
                    log.info("=======isSignatureMaterialAutoUpload接口auditProjectMaterial=======》"
                            + auditProjectMaterial);
                    // 6、定义返回JSON对象
                    log.info("=======结束调用isSignatureMaterialAutoUpload接口=======");
                    if (auditProjectMaterial != null) {
                        // 材料提交状态
                        Integer status = auditProjectMaterial.getStatus();
                        if (status == 20) {
                            return JsonUtils.zwdtRestReturn("1", "该材料已生成电子签章材料自动上传成功", "");
                        }
                    }

                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

            return JsonUtils.zwdtRestReturn("0", "该材料还未生成电子签章材料自动上传", "");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======isSignatureMaterialAutoUpload接口参数：params【" + params + "】=======");
            log.info("=======isSignatureMaterialAutoUpload异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询是否生成电子签章材料自动上传异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办件初始化（办件申报页面初始化调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/initProjectReturnMaterials", method = RequestMethod.POST)
    public String initProjectReturnMaterials(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initProjectReturnMaterials接口=======");
            log.info("=======params=======" + params);
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取区域标识
                String areacode = obj.getString("areacode");
                // 个性化areacode不用分
                areacode = areacode.length() > 6 ? areacode.substring(0, 6) : areacode;// 用来解决乡镇事项无法进入分表的问题
                // 1.3、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.4、获取中心标识
                String centerGuid = obj.getString("centerguid");
                // 1.5、获取申请人类型（10:企业 20:个人）
                String applyerType = obj.getString("applyertype");

                String hasventilationsystem = obj.getString("hasventilationsystem");
                // 1.6、申请人类型如果为空则默认为个人
                applyerType = StringUtil.isBlank(applyerType) ? ZwfwConstant.APPLAYERTYPE_GR : applyerType;
                // 1.7、是否为手机申报
                String isMobile = obj.getString("ismobile");
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                if (auditTask == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询事项失败", "");
                }
                else {
                    if (StringUtil.isBlank(auditTask.getAreacode())) {
                        return JsonUtils.zwdtRestReturn("0", "配置事项辖区错误", "");
                    }
                    else {
                        // areacode = auditTask.getAreacode();
                        // 1.7、获取用户注册信息 个性化
                        // 1.5、获取用户基本信息
                        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                        if (auditOnlineRegister != null) {
                            // 2、获取用户信息
                            AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                    .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                            List<AuditProjectMaterial> auditProjectMaterials = null;
                            // 生成材料信息
                            // 2.1、设置办件标识
                            if (StringUtil.isBlank(projectGuid)) {
                                projectGuid = UUID.randomUUID().toString();
                            }
                            // 查找办件是否存在
                            AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);
                            if (auditProject == null) {
                                // 2.2、设置申请人标识(个人类型默认为申请人Applyerguid，企业默认为空)
                                String applyerGuid = "";
                                // 2.3、设置申请人姓名（申请人类型为个人则申请人姓名为个人信息中的姓名，申请人类型为企业则为空）
                                String applyerUserName = "";
                                // 2.4、设置申请人证照编号（申请人类型为个人则申请人证照编号为个人信息中的身份证，申请人类型为企业则为空）
                                String certNum = "";
                                // 2.5、如果是手机提交申报的办件
                                if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                                    applyerGuid = auditOnlineIndividual.getApplyerguid();
                                    applyerUserName = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)
                                            ? auditOnlineIndividual.getClientname()
                                            : "";
                                    certNum = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)
                                            ? auditOnlineIndividual.getIdnumber()
                                            : "";
                                    auditProjectMaterials = iTaHandleProject.InitOnlineProjectReturnMaterials(taskGuid,
                                            centerGuid, areacode, applyerGuid, applyerUserName, certNum, projectGuid,
                                            taskCaseGuid, applyerType).getResult();
                                }
                                // 2.6、如果为PC端申报的办件
                                else {
                                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                                        applyerUserName = auditOnlineIndividual.getClientname();
                                        applyerGuid = auditOnlineIndividual.getApplyerguid();
                                        certNum = auditOnlineIndividual.getIdnumber();
                                        auditProjectMaterials = iTaHandleProject.InitOnlineProjectReturnMaterials(
                                                taskGuid, centerGuid, areacode, applyerGuid, applyerUserName, certNum,
                                                projectGuid, taskCaseGuid, applyerType).getResult();
                                    }
                                    else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                                        certNum = auditOnlineIndividual.getIdnumber();
                                        SqlConditionUtil sql = new SqlConditionUtil();
                                        sql.eq("ORGALEGAL_IDNUMBER", certNum);
                                        List<AuditRsCompanyBaseinfo> company = iAuditRsCompanyBaseinfo
                                                .selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
                                        if (company != null && company.size() > 0) {
                                            for (AuditRsCompanyBaseinfo companyinfo : company) {
                                                certNum = companyinfo.getCreditcode();
                                            }
                                        }
                                        // 2.7、设置企业申请人姓名
                                        String declarerName = auditOnlineIndividual.getClientname();
                                        // 2.8、设置企业申请人guid
                                        String declarerGuid = auditOnlineIndividual.getApplyerguid();
                                        // 3、初始化办件数据并且返回办件材料数据
                                        auditProjectMaterials = iTaHandleProject
                                                .InitOnlineCompanyProjectReturnMaterials(taskGuid, centerGuid, areacode,
                                                        applyerGuid, applyerUserName, certNum, projectGuid,
                                                        taskCaseGuid, applyerType, declarerName, declarerGuid)
                                                .getResult();
                                    }
                                }
                            }
                            // 直接查找材料
                            else {
                                SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
                                materialSqlConditionUtil.eq("projectguid", projectGuid);
                                auditProjectMaterials = iAuditProjectMaterial
                                        .selectProjectMaterialByCondition(materialSqlConditionUtil.getMap())
                                        .getResult();
                            }

                            // 4、定义返回JSON对象
                            JSONObject dataJson = new JSONObject();
                            List<JSONObject> materialJsonlist = new ArrayList<JSONObject>();
                            if (auditProjectMaterials != null && auditProjectMaterials.size() > 0) {
                                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                            .getResult();
                                    JSONObject materialJson = new JSONObject();

                                    if ("使用集中空调通风系统的，需提供集中空调通风系统卫生检测或者评价报告（由有资质认定的检验机构提供）"
                                            .equals(auditTaskMaterial.getMaterialname())) {
                                        if ("有无无".equals(hasventilationsystem)) {
                                            continue;
                                        }
                                    }

                                    // 4.1、获取办件材料基本信息返回JSON
                                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                    materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 办件材料名称
                                    materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                    materialJson.put("sharematerialiguid",
                                            auditProjectMaterial.getSharematerialiguid());// 办件材料关联共享材料实例标识
                                    materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                                    materialJson.put("type", auditTaskMaterial.getType());// 材料类型
                                    materialJson.put("ordernum", auditTaskMaterial.getOrdernum());

                                    materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                            String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                                    // 4.2、返回材料来源，默认为申请人自备
                                    String materialSource;
                                    if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                        materialSource = "申请人自备";
                                    }
                                    else {
                                        materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                auditTaskMaterial.getFile_source());
                                    }
                                    materialJson.put("materialsource",
                                            StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                                    // 4.3、返回材料必要性（默认为事项材料中设置，如果有多情形需要按照多情形中材料必要性）
                                    int necessity = auditTaskMaterial.getNecessity();
                                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                                        necessity = auditTaskMaterial.getNecessity();
                                        // 当内网不勾选材料情形时，默认不显示该材料
                                        if (ZwdtConstant.INT_NO == necessity) {
                                            continue;
                                        }
                                    }
                                    // 首先判断情形，若存在情形，则所有材料为必须材料
                                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                                        materialJson.put("necessary", "1");
                                    }
                                    else {
                                        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                            materialJson.put("necessary", "0");
                                        }
                                        else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                            materialJson.put("necessary", "1");
                                        }
                                    }

                                    // TODO：这边系统方法返回的是cliengguid,应该返回attachguid,先查询attachguid,按理说只有一个，取第一个
                                    // System.out.println("...." +
                                    // auditTaskMaterial);
                                    // 4.4、返回空白表格的附件标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {

                                        int templateAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                        if (templateAttachCount > 0) {
                                            String temguid = sendMaterials.getAttachguidByClientguid(
                                                    auditTaskMaterial.getTemplateattachguid()).get(0);
                                            materialJson.put("templateattachguid", temguid);
                                        }
                                    }
                                    // 4.4、返回填报示例的附件标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {

                                        int exampleAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                        if (exampleAttachCount > 0) {
                                            String examguid = sendMaterials
                                                    .getAttachguidByClientguid(auditTaskMaterial.getExampleattachguid())
                                                    .get(0);
                                            materialJson.put("exampleattachguid", examguid);
                                        }
                                    }
                                    // 4.5、返回办件材料对应的附件业务标识
                                    String clientGuid = auditProjectMaterial.getCliengguid();
                                    materialJson.put("clientguid", StringUtil.isNotBlank(clientGuid) ? clientGuid : "");
                                    // 4.6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                                    // 在线填表、4:已填表）
                                    int count = iAttachService.getAttachCountByClientGuid(clientGuid); // 获取办件材料对应的附件数量
                                    int showButton = 0; // 按钮显示方式
                                    String needLoad = ZwdtConstant.CERTLEVEL_C
                                            .equals(auditProjectMaterial.get("materialLevel")) ? "1" : "0"; // 是否需要上传到证照库
                                    // 0:不需要
                                    // 、1:需要
                                    if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                        // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                        if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                            // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                            showButton = count > 0 ? 4 : 3;
                                        }
                                        else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                            // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                            showButton = count > 0 ? 1 : 0;
                                        }
                                    }
                                    else {
                                        // 4.6.2、如果关联了证照库
                                        if (count > 0) {
                                            // 4.6.2.1、获取材料类别
                                            String materialType = auditProjectMaterial.get("materialType");
                                            if (materialType == null) {
                                                materialType = "0";
                                            }
                                            if (Integer.parseInt(materialType) == 1) {
                                                // 4.6.2.1.1、已引用证照库
                                                showButton = Integer
                                                        .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                            }
                                            else if (Integer.parseInt(materialType) == 2) {
                                                // 4.6.2.1.2、已引用批文
                                                showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                            }
                                            else {
                                                // 4.6.2.1.3、已引用材料
                                                showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                            }
                                        }
                                        else {
                                            // 4.6.2.2、如果没有附件，则标识为未上传
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                        }
                                        materialJson.put("shareMid", auditProjectMaterial.getSharematerialiguid());
                                    }
                                    materialJson.put("needload", needLoad);
                                    materialJson.put("showbutton", showButton);

                                    String isxtendfield = auditTaskMaterial.getStr("isxtendfield");

                                    if ("1".equals(isxtendfield)) {
                                        materialJson.put("materialsource", "已关联电子证照，可免提交");
                                        materialJson.put("isxtendfield", 1);
                                        materialJson.put("isshuoming", 0);
                                        if (showButton == 0) {
                                            showButton = 9;
                                        }
                                    }
                                    else {
                                        materialJson.put("isxtendfield", 0);
                                        materialJson.put("isshuoming", 1);
                                    }
                                    materialJsonlist.add(materialJson);
                                }
                            }
                            // 5、对返回的材料列表进行排序
                            Collections.sort(materialJsonlist, new Comparator<JSONObject>()
                            {
                                @Override
                                public int compare(JSONObject b1, JSONObject b2) {
                                    Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                                    Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                                    // 5.1、优先对比材料必要性（必要在前）
                                    int comNecessity = necessaryb2.compareTo(necessaryb1);
                                    int ret = comNecessity;
                                    // 5.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                    if (comNecessity == 0) {
                                        Integer ordernumb1 = b1.get("ordernum") == null ? 0
                                                : (Integer) b1.get("ordernum");
                                        Integer ordernumb2 = b2.get("ordernum") == null ? 0
                                                : (Integer) b2.get("ordernum");
                                        ret = ordernumb2.compareTo(ordernumb1);
                                    }
                                    return ret;
                                }
                            });
                            dataJson.put("materiallist", materialJsonlist);
                            dataJson.put("projectguid", projectGuid);
                            log.info("=======结束调用initProjectReturnMaterials接口=======");
                            return JsonUtils.zwdtRestReturn("1", "初始化办件成功", dataJson.toString());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                        }
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initProjectReturnMaterials接口参数：params【" + params + "】=======");
            log.info("=======initProjectReturnMaterials异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "初始化办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 保存办件的接口（办件申报保存调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/saveProjectInfo", method = RequestMethod.POST)
    public String saveProjectInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveProjectInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取法人代表
                String legal = obj.getString("legal");
                // 1.3、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.4、获取申请人guid(如果为企业为企业的companyid)
                String applyerGuid = obj.getString("applyerguid");
                // 1.5、获取申请人证照编号
                String idCard = obj.getString("idcard");
                // 1.6、获取证照类型
                String certType = obj.getString("certtype");
                // 1.7、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.8、获取联系人手机
                String contactMobile = obj.getString("contactmobile");
                // 1.9、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.10、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.11、获取邮编
                String postCode = obj.getString("postcode");
                // 1.12、获取电子邮件
                String email = obj.getString("email");
                // 1.13、获取传真
                String fax = obj.getString("fax");
                // 1.14、获取地址
                String address = obj.getString("address");
                // 1.15、获取备注
                String remark = obj.getString("remark");
                // 1.16、获取是否快递
                String ifExpress = obj.getString("if_express");
                // 1.17、获取申请人类型
                String applyType = obj.getString("applyertype");
                // 1.18、获取申请人类型
                // String areaCode = obj.getString("areacode");
                // 1.19、获取当前企业的主键标识 companyguid
                String companyrowguid = obj.getString("companyrowguid");
                // 1.20、手机端申报参数
                String isMobile = obj.getString("ismobile");
                // 1.21、法人代表身份证
                String orgalegalidnumber = obj.getString("orgalegalidnumber");
                // 1.21、dataid
                String dataid = obj.getString("dataid");
                // 1.21、dataid
                String formid = obj.getString("formid");

                // 物流优化需求
                String issend = obj.getString("issend");
                String issendback = obj.getString("issendback");
                String sendname = obj.getString("sendname");
                String sendmobile = obj.getString("sendmobile");
                String sendaddress = obj.getString("sendaddress");
                String senddetailaddress = obj.getString("senddetailaddress");
                String recipientname = obj.getString("recipientname");
                String recipientmobile = obj.getString("recipientmobile");
                String recipientaddress = obj.getString("recipientaddress");
                String materialbill = obj.getString("materialbill");
                String materialguidbill = obj.getString("materialguidbill");
                String backmobile = obj.getString("backmobile");
                String backname = obj.getString("backname");
                String backaddress = obj.getString("backaddress");
                String backdetailaddress = obj.getString("backdetailaddress");

                // 2、获取用户信息 个性化
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、更新初始化的办件的数据
                    AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);
                    /*
                     * AuditProject auditProject =
                     * iAuditProject.getAuditProjectByRowGuid(projectGuid,
                     * areaCode) .getResult();
                     */
                    if (auditProject == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取办件信息失败！", "");
                    }

                    // ---安全性问题处理开始---
                    // 4、处理用户在办件已提交后再做保存办件的操作
                    int projectCurrentStatus = auditProject.getStatus();
                    // System.out.println(projectCurrentStatus);
                    if (projectCurrentStatus >= ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                        return JsonUtils.zwdtRestReturn("0", "异常操作，请确认！", "");
                    }
                    // 5、处理其他用户操作本用户数据问题
                    if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                        // 5.1、 企业的情况只判断有企业办件存在的情况
                        String applyerUserGuid = auditProject.getApplyeruserguid();
                        if (StringUtil.isNotBlank(applyerUserGuid)) {
                            AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getAuditRsCompanyBaseinfoByRowguid(applyerUserGuid).getResult();
                            String companyID = "";
                            if (originAuditRsCompanyBaseinfo != null) {
                                companyID = originAuditRsCompanyBaseinfo.getCompanyid();
                            }
                            // 5.2、获取企业外网办件信息
                            AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(projectGuid, companyID).getResult();
                            if (auditOnlineProject != null) {
                                // 5.3、判断用户信息是否一致
                                if (!auditOnlineIndividual.getApplyerguid()
                                        .equals(auditOnlineProject.getDeclarerguid())) {
                                    // 5.3.1、如果外网申请GUID不一致则认为不是一人提交申报，退出操作
                                    return JsonUtils.zwdtRestReturn("0", "保存办件发生异常！", "");
                                }
                            }
                        }
                    }
                    else {
                        // 5.4、 个人申报判断
                        String onlineApplyerGuid = auditProject.getApplyeruserguid();
                        if (!onlineApplyerGuid.equals(auditOnlineIndividual.getApplyerguid())) {
                            return JsonUtils.zwdtRestReturn("0", "保存办件发生异常！", "");
                        }
                    }
                    // ---安全性问题处理结束---

                    // 6、在做保存操作前，获取办件原来的申请人Guid(企业为企业RowGuid,个人为ApplyerGuid)
                    String originApplyerGuid = auditProject.getApplyeruserguid();
                    auditProject.setRowguid(projectGuid);// 办件标识
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 保存时办件状态：外网申报未提交
                    auditProject.setOperatedate(new Date());
                    auditProject.setCertnum(idCard);
                    auditProject.setApplyername(applyerName);
                    // 6.1、如果为PC端申报企业办事，则需要设置申请人guid
                    if (StringUtil.isBlank(isMobile)) {
                        // 6.2、类型如果为企业则申请人guid为企业的companyguid
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                            auditProject.setApplyeruserguid(companyrowguid);
                        }
                    }
                    auditProject.setContactperson(contactName);
                    auditProject.setContactmobile(contactMobile);
                    auditProject.setContactphone(contactPhone);
                    auditProject.setContactpostcode(postCode);
                    auditProject.setContactcertnum(contactIdnum);
                    auditProject.setRemark(remark);
                    auditProject.setAddress(address);
                    auditProject.setLegal(legal);
                    auditProject.setContactemail(email);
                    auditProject.setContactfax(fax);
                    // 说明邮寄材料
                    if ("1".equals(issend)) {
                        auditProject.setIf_express("1");
                        // 同时存储物流表 新表
                        AuditProjectDelivery delivery = new AuditProjectDelivery();
                        delivery.setRowguid(UUID.randomUUID().toString());
                        delivery.setOperatedate(new Date());
                        delivery.setProjectguid(projectGuid);
                        delivery.setIssend(issend);
                        delivery.setIssendback(issendback);
                        delivery.setSendname(sendname);
                        delivery.setSendmobile(sendmobile);
                        delivery.setSendaddress(sendaddress);
                        delivery.setSenddetailaddress(senddetailaddress);
                        delivery.setRecipientname(recipientname);
                        delivery.setRecipientmobile(recipientmobile);
                        delivery.setRecipientaddress(recipientaddress);
                        delivery.setBackname(backname);
                        delivery.setBackmobile(backmobile);
                        delivery.setBackaddress(backaddress);
                        delivery.setBackdetailaddress(backdetailaddress);
                        delivery.setMaterialbill(materialbill);
                        delivery.setMaterialguidbill(materialguidbill);
                        deliveryService.insert(delivery);
                    }
                    else {
                        // 说明不邮寄材料，在判断是否邮寄结果
                        if ("1".equals(issendback)) {
                            auditProject.setIf_express("1");
                            // 同时存储物流表 新表
                            AuditProjectDelivery delivery = new AuditProjectDelivery();
                            delivery.setRowguid(UUID.randomUUID().toString());
                            delivery.setOperatedate(new Date());
                            delivery.setProjectguid(projectGuid);
                            delivery.setIssend(issend);
                            delivery.setIssendback(issendback);
                            delivery.setBackname(backname);
                            delivery.setBackmobile(backmobile);
                            delivery.setBackaddress(backaddress);
                            delivery.setBackdetailaddress(backdetailaddress);
                            deliveryService.insert(delivery);
                        }
                        else {
                            auditProject.setIf_express(ifExpress);
                        }
                    }
                    auditProject.setLegalid(orgalegalidnumber);
                    auditProject.setIs_test(0);
                    auditProject.set("dataid", dataid);
                    auditProject.set("formid", formid);
                    if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                        auditProject.setOnlineapplyerguid(applyerGuid);
                    }
                    else {
                        auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());
                    }
                    // 6.3、设置办件证照类型
                    if (StringUtil.isBlank(certType)) {
                        // 6.3.1、如果证照类型为空，需要根据申请人类型设置默认值
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 6.3.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 6.3.1.2、若申请人类型为企业，则默认设置证照类型为统一信用代码
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM);
                        }
                    }
                    else {
                        // 6.3.2、如果证照类型不为空，则获取传递的证照类型
                        auditProject.setCerttype(certType);
                    }
                    if (StringUtil.isNotBlank(applyType)) {
                        auditProject.setApplyertype(Integer.parseInt(applyType));
                    }
                    iAuditProject.updateProject(auditProject);
                    // 6.4、个人用户更新外网申请人信息
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                        // 6.4.1、更新外网个人用户信息
                        auditOnlineIndividual.setDeptaddress(address);
                        auditOnlineIndividual.setContactfax(fax);
                        auditOnlineIndividual.setContactemail(email);
                        auditOnlineIndividual.setContactpostcode(postCode);
                        auditOnlineIndividual.setContactperson(contactName);
                        auditOnlineIndividual.setContactmobile(contactMobile);
                        auditOnlineIndividual.setContactphone(contactPhone);
                        iAuditOnlineIndividual.updateIndividual(auditOnlineIndividual);
                    }
                    // 7、判断是否是手机申报
                    if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                        Map<String, String> updateFieldMap = new HashMap<>(16);
                        if (StringUtil.isNotBlank(applyType)) {
                            updateFieldMap.put("Applyertype=", applyType);
                        }
                        updateFieldMap.put("applyername=", applyerName);
                        updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
                        Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                        updateDateFieldMap.put("applydate=",
                                EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("sourceguid", projectGuid);
                        sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                        iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                sqlConditionUtil.getMap());
                    }
                    // 8、PC端申报
                    else {
                        // 8.1、企业办件则初始化OnlinePrject表
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                            String declarerguid = auditOnlineIndividual.getApplyerguid();
                            String declarername = auditOnlineIndividual.getClientname();
                            // 8.2、查看AuditOnlineProject办件数据是否存在,
                            String originCompanyid = "";
                            if (StringUtil.isNotBlank(originApplyerGuid)) {
                                AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                        .getAuditRsCompanyBaseinfoByRowguid(originApplyerGuid).getResult();
                                originCompanyid = originAuditRsCompanyBaseinfo.getCompanyid();
                            }
                            // 8.3、第一次申报为为null，则新增网厅办件数据
                            AuditOnlineProject currentAuditOnlineProject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(projectGuid, originCompanyid).getResult();
                            // 8.4、获取当前申报的企业基本信息
                            AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfoQY = iAuditRsCompanyBaseinfo
                                    .getAuditRsCompanyBaseinfoByRowguid(companyrowguid).getResult();
                            AuditOnlineCompanyGrant auditOnlineCompanyGrant = null;
                            if (originAuditRsCompanyBaseinfoQY != null) {
                                // 8.5、获取当前用户授权基本信息
                                auditOnlineCompanyGrant = iAuditOnlineCompanyGrant.getGrantByBsqUserGuidAndCompanyId(
                                        originAuditRsCompanyBaseinfoQY.getCompanyid(),
                                        auditOnlineRegister.getAccountguid()).getResult();
                            }

                            String sqGuid = auditOnlineCompanyGrant != null ? auditOnlineCompanyGrant.getRowguid() : "";
                            if (currentAuditOnlineProject != null) {
                                // 8.6、如果已有该办件，判断当前的企业是否与之前的相同
                                if (originCompanyid.equals(applyerGuid)) {
                                    currentAuditOnlineProject.setApplydate(new Date());
                                    // 8.6.1、判断当前用户的授权信息与原办件是否相同，如果不同则需要更新
                                    if (!sqGuid.equals(currentAuditOnlineProject.getSqGuid())) {
                                        // 8.6.1.1、更新授权信息
                                        currentAuditOnlineProject.setSqGuid(sqGuid);
                                    }
                                    iAuditOnlineProject.updateProject(currentAuditOnlineProject);
                                }
                                else {
                                    // 8.6.2、如果企业数据与之前的不同，则需要删除原有企业
                                    iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid, originCompanyid);
                                    // 8.6.3新增企业
                                    iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                            applyerGuid, applyerName, companyrowguid, sqGuid);
                                }
                            }
                            else {
                                // 8.7、初始化新增OnlineProject办件
                                iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                        applyerGuid, applyerName, companyrowguid, sqGuid);
                            }
                        }
                        // 9、个人办事逻辑，此处不改变原有逻辑
                        else {
                            Map<String, String> updateFieldMap = new HashMap<>();
                            updateFieldMap.put("applyername=", applyerName);
                            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
                            Map<String, String> updateDateFieldMap = new HashMap<String, String>();
                            updateDateFieldMap.put("applydate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("sourceguid", projectGuid);
                            sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                            iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                    sqlConditionUtil.getMap());
                        }
                    }
                    log.info("=======结束调用saveProjectInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
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
            log.info("=======saveProjectInfo接口参数：params【" + params + "】=======");
            log.info("=======saveProjectInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                // 1.3、获取多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                int noSubmitNum = 0;// 必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(materialGuid).getResult();
                        if (auditTaskMaterial != null) {
                            // 4、根据材料的提交状态及材料设置的必要性判断材料是否已提交（材料类型为附件的才需要判断）
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 5、材料必要性默认为事项材料配置的必要性
                                int necessity = auditTaskMaterial.getNecessity();
                                // 6、如果事项配置了多情形，需要考虑材料在多情形中的必要性
                                if (StringUtil.isNotBlank(taskCaseGuid)) {
                                    AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                            .selectTaskMaterialCaseByGuid(taskCaseGuid, auditTaskMaterial.getRowguid())
                                            .getResult();
                                    necessity = auditTaskMaterialCase == null ? necessity
                                            : auditTaskMaterialCase.getNecessity();
                                }
                                // 7、如果材料设置了必填，则需要通过材料的提交方式进行判断（外网纸质材料不予判断）
                                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                    int submitType = Integer.parseInt(auditTaskMaterial.getSubmittype()); // 材料提交方式
                                    int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
                                    if (submitType == WorkflowKeyNames9.SubmitType_Submit) {
                                        // 7.1、提交方式为电子，则状态不是电子已提交或者电子和纸质都提交，说明材料未提交
                                        if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                                && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                            noSubmitNum++;
                                        }
                                    }
                                    else if (submitType == WorkflowKeyNames9.SubmitType_Submit_And_PaperSubmit) {
                                        // 7.2、提交方式为电子和纸质，则状态不是电子已提交或者电子和纸质都提交，说明材料未提交
                                        if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                                && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                            noSubmitNum++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkAllMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交办件的接口（办件申报提交调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/submitProjectByTaskguid", method = RequestMethod.POST)
    public String submitProjectByTaskguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitProjectByTaskguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                log.info("一体机入参=======" + obj);
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.4、获取法人代表
                String legal = obj.getString("legal");
                // 1.5、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.6、获取申请人证照编号
                String certNum = obj.getString("idcard");
                // 1.7、获取证照类型
                String certType = obj.getString("certtype");
                // 1.8、获取申请人类型
                String applyerType = obj.getString("applyertype");
                // 1.9、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.10、获取联系人手机号
                String contactMobile = obj.getString("contactmobile");
                // 1.11、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.12、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.13、获取邮编
                String postCode = obj.getString("postcode");
                // 1.14、获取通讯地址
                String address = obj.getString("address");
                // 1.15、获取备注
                String remark = obj.getString("remark");
                // 1.16、获取电子邮箱
                String email = obj.getString("email");
                // 1.17、获取传真
                String fax = obj.getString("fax");
                // 1.18、获取多情形标识
                // String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.19、获取是否需要快递
                String ifExpress = obj.getString("if_express");
                // 1.20、获取企业applyerguid
                String applyerGuid = obj.getString("applyerguid");
                // 1.21、获取企业主键标识
                String companyrowguid = obj.getString("companyrowguid");
                // 1.22、手机端申报参数
                String isMobile = obj.getString("ismobile");
                // 1.23、法人代表身份证
                String orgalegalidnumber = obj.getString("orgalegalidnumber");
                // 1.23、法人代表身份证
                String dataid = obj.getString("dataid");
                // 1.23、formid
                String formid = obj.getString("formid");

                String fformid = obj.getString("fformid");

                // 物流优化需求
                String issend = obj.getString("issend");
                String issendback = obj.getString("issendback");
                String sendname = obj.getString("sendname");
                String sendmobile = obj.getString("sendmobile");
                String sendaddress = obj.getString("sendaddress");
                String senddetailaddress = obj.getString("senddetailaddress");
                String recipientname = obj.getString("recipientname");
                String recipientmobile = obj.getString("recipientmobile");
                String recipientaddress = obj.getString("recipientaddress");
                String materialbill = obj.getString("materialbill");
                String materialguidbill = obj.getString("materialguidbill");
                String backmobile = obj.getString("backmobile");
                String backname = obj.getString("backname");
                String backaddress = obj.getString("backaddress");
                String backdetailaddress = obj.getString("backdetailaddress");

                List<String> pnList = new ArrayList<>();
                pnList.add("51a74101-84c0-44f0-aaff-534e6f82675d");
                pnList.add("b574893d-024c-4672-8aa0-827b856088c6");
                pnList.add("5e5afa55-d1a3-48ca-88ab-e079cd1b47dd");
                pnList.add("5d617e32-9691-4cb2-89b4-426477205e54");
                pnList.add("a2e57190-6b85-46c6-860b-c9b2830f7a89");
                pnList.add("e7d6f9da-cb5b-4dea-ab74-e17b7113217d");
                pnList.add("c328ee00-4e7d-4674-8fd1-43513cdc1edc");
                pnList.add("c73e6826-1880-437e-b820-7c2e42747c99");
                pnList.add("42163cde-0795-47e7-82eb-1549af70d601");
                pnList.add("8b2cfa6a-41ff-475f-aac8-51a24646994a");
                pnList.add("02921679-9926-4c98-a04b-5a2436a51af7");
                pnList.add("1220893a-6a10-4a5c-b61f-f954f4e70244");
                pnList.add("ce9acd2c-0d8c-4e68-be79-363c36d893a1");
                pnList.add("c9024160-5031-4370-be66-96405b6cbafe");
                // 1.24、获取用户注册信息 个性化
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {

                    String AESInfo = getAESInfo(taskGuid, auditOnlineRegister);
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();

                    AuditProject auditproject = sendMaterials.getAuditProjectByRowguid(projectGuid);
                    if (auditproject != null) {
                        if ("12".equals(auditproject.getStatus())) {
                            return JsonUtils.zwdtRestReturn("0", "不允许重复提交办件", "");
                        }
                    }

                    // ------ 手机端逻辑 -----
                    if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                        // 3、获取用户申报的事项
                        AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                                .getOnlineProjectByApplyerGuid(projectGuid, auditOnlineIndividual.getApplyerguid())
                                .getResult();

                        int oldstatus = 0; // 预审打回时使用
                        if (auditOnlineProject != null) {
                            oldstatus = Integer.parseInt(auditOnlineProject.getStatus());
                            if (pnList.contains(auditOnlineProject.getProjectname())) {
                                // 根据certNum判断是否体检合格
                                log.info("=========查询到教师体检报告=========");
                                SqlConditionUtil sqlUtil = new SqlConditionUtil();
                                sqlUtil.eq("sfz", certNum);
                                TeacherHealthReport teacherHealthReportByCondition = iTeacherHealthReportService
                                        .getTeacherHealthReportByCondition(sqlUtil.getMap());
                                if (teacherHealthReportByCondition == null || (teacherHealthReportByCondition != null
                                        && "0".equals(teacherHealthReportByCondition.getIspass()))) {
                                    return JsonUtils.zwdtRestReturn("0", "您的体检不合格或未查询到您的体检信息，无法进行申报！", "");
                                }
                            }
                        }
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                        AuditTask task = iAuditTask.selectUsableTaskByTaskID(auditTask.getTask_id()).getResult();
                        if (task == null) {
                            return JsonUtils.zwdtRestReturn("0", "该事项未查询到！", "");
                        }
                        // 4、获取事项扩展信息
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                        // 5、获取办件状态，如网上申报类型是 网上申报后直接办理，则办件状态更新为待接件
                        int status = ZwfwConstant.BANJIAN_STATUS_WWYTJ;// 默认办件状态：外网申报已提交
                        if (ZwfwConstant.WEB_APPLY_TYPE_SL
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                            status = ZwfwConstant.BANJIAN_STATUS_DJJ;
                        }

                        String flowsn = "";
                        String bianhao = "";

                        if ("11370800MB285591843370123020002".equals(auditTask.getItem_id())) {
                            log.info("消毒水注销");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220831153522", projectGuid);
                            if (records != null) {
                                bianhao = records.getStr("xkzjbh");
                                log.info("消毒水注销bianhao：" + bianhao);
                            }
                        }

                        if ("11370800MB28559184737011702000016".equals(auditTask.getItem_id())) {
                            log.info("排水注销");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20230222161520", projectGuid);
                            if (records != null) {
                                bianhao = records.getStr("xkzbh");
                                log.info("排水注销bianhao：" + bianhao);
                            }
                        }

                        if ("终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())) {
                            log.info("终止经营省际普通货物水路运输业务");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220519115927", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("wbk1");
                                log.info("获取到要注销的水路运输bianhao：" + bianhao);
                            }

                        }
                        if ("公共场所卫生许可证注销".equals(auditTask.getTaskname())) {
                            log.info("获取到要注销的公共许可证号");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220413141101", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("zzbh");
                                log.info("获取到要注销的公共许可bianhao：" + bianhao);
                            }

                        }

                        if ("11370883MB2857374H437309800300101".equals(auditTask.getItem_id())) {

                            Record records = iCxBusService.getPorjectByRowguid("formtable20211118012314", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("wbk11");
                            }

                        }

                        if ("11370883MB2857374H437309800900101".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zccsy");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zccsy");

                            bianhao = "鲁济邹城村兽医第" + result + "号";
                        }
                        if ("11370883MB2857374H4370114011007".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zcrlzyba");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zcrlzyba");

                            bianhao = "(鲁)人服备字[2022]第0805" + result + "号";
                        }
                        if ("11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zccbw");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zccbw");

                            bianhao = "新出发鲁字第H1" + result + "号";
                        }
                        if ("11370883MB2857374H4370115041000".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zclczz");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            iJnService.updateZjSlFlowsn(result, "zclczz");

                            bianhao = "X370883202200" + result;
                        }
                        if ("11370883MB2857374H4370173016000".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zcdyfy");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            iJnService.updateZjSlFlowsn(result, "zcdyfy");

                            bianhao = "邹行审字【2022】影映第" + result + "号";
                        }

                        if ("11370883MB2857374H4371099370000".equals(auditTask.getItem_id())) {
                            String flwosn = iJnService.getZjSlFlowsn("zlba");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zlba");

                            bianhao = "鲁邹粮备案370883" + result;
                        }

                        // 6、更新AUDIT_ONLINE_PROJECT表数据
                        Map<String, String> updateFieldMap = new HashMap<>(16);
                        if ("11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                || auditTask.getTaskname().contains("房地产开发企业暂定资质变更")
                                || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                || auditTask.getTaskname().contains("房地产开发企业四级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业三级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业二级资质变更（多项变更）")
                                || "变更成品油零售经营批准证书企业名称或法定代表人".equals(auditTask.getTaskname())
                                || "变更成品油零售经营批准证书企业名称和法定代表人".equals(auditTask.getTaskname())
                                || "统一社会信用代码信息服务".equals(auditTask.getTaskname())
                                || "公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                // ||
                                // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())
                                // // 船舶注销
                                || "11370800MB285591843370123020002".equals(auditTask.getItem_id()) // 消毒水注销
                                || "11370800MB28559184737011702000016".equals(auditTask.getItem_id()) // 排水注销
                                || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                                // ||
                                // auditTask.getTaskname().contains("张贴宣传品审批")
                                || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                        && getIsAllZmMateri(projectGuid))) {

                            updateFieldMap.put("projectname=", "<AI审批>" + auditTask.getTaskname() + " " + bianhao);
                            updateFieldMap.put("status=", "90");
                            updateFieldMap.put("Banjieresult=", "40");
                            updateFieldMap.put("banjiedate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

                        }
                        else {
                            updateFieldMap.put("status=", String.valueOf(status));
                        }
                        updateFieldMap.put("applyername=", applyerName);
                        Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                        if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                            updateDateFieldMap.put("applydate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        }
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("sourceguid", projectGuid);
                        iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                sqlConditionUtil.getMap());
                        // 7、更新AUDIT_PROJECT表数据
                        AuditProject auditProject = new AuditProject();
                        // 8.1、设置办件证照类型
                        if (StringUtil.isBlank(certType)) {
                            // 8.1.1、如果证照类型为空，需要根据申请人类型设置默认值
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                                // 8.1.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                                auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                            }
                            else if (ZwfwConstant.APPLAYERTYPE_QY
                                    .equals(String.valueOf(auditProject.getApplyertype()))) {
                                // 8.1.1.2、若申请人类型为企业，则默认设置证照类型为组织机构代码证
                                auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                            }
                        }
                        else {
                            // 8.1.2、如果证照类型不为空，则获取传递的证照类型
                            auditProject.setCerttype(certType);
                        }
                        auditProject.setRowguid(projectGuid);
                        if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                            if(auditProject.getApplydate()==null){
                                auditProject.setApplydate(new Date());
                            }
                        }
                        auditProject.setCertnum(certNum);
                        auditProject.setApplyername(applyerName);
                        auditProject.setContactperson(contactName);
                        auditProject.setContactmobile(contactMobile);
                        auditProject.setContactphone(contactPhone);
                        auditProject.setContactpostcode(postCode);
                        auditProject.setContactcertnum(contactIdnum);
                        auditProject.setRemark(remark);
                        auditProject.setAddress(address);
                        auditProject.setLegal(legal);
                        auditProject.setLegalid(orgalegalidnumber);
                        auditProject.setContactemail(email);
                        auditProject.setContactfax(fax);
                        if ("1".equals(issend)) {
                            auditProject.setIf_express("1");
                            // 同时存储物流表 新表
                            AuditProjectDelivery delivery = new AuditProjectDelivery();
                            delivery.setRowguid(UUID.randomUUID().toString());
                            delivery.setOperatedate(new Date());
                            delivery.setProjectguid(projectGuid);
                            delivery.setIssend(issend);
                            delivery.setIssendback(issendback);
                            delivery.setSendname(sendname);
                            delivery.setSendmobile(sendmobile);
                            delivery.setSendaddress(sendaddress);
                            delivery.setSenddetailaddress(senddetailaddress);
                            delivery.setRecipientname(recipientname);
                            delivery.setRecipientmobile(recipientmobile);
                            delivery.setRecipientaddress(recipientaddress);
                            delivery.setBackname(backname);
                            delivery.setBackmobile(backmobile);
                            delivery.setBackaddress(backaddress);
                            delivery.setBackdetailaddress(backdetailaddress);
                            delivery.setMaterialbill(materialbill);
                            delivery.setMaterialguidbill(materialguidbill);
                            delivery.set("ismail", "0");
                            deliveryService.insert(delivery);
                        }
                        else {
                            // 说明不邮寄材料，在判断是否邮寄结果
                            if ("1".equals(issendback)) {
                                auditProject.setIf_express("1");
                                // 同时存储物流表 新表
                                AuditProjectDelivery delivery = new AuditProjectDelivery();
                                delivery.setRowguid(UUID.randomUUID().toString());
                                delivery.setOperatedate(new Date());
                                delivery.setProjectguid(projectGuid);
                                delivery.setIssend(issend);
                                delivery.setIssendback(issendback);
                                delivery.setBackname(backname);
                                delivery.setBackmobile(backmobile);
                                delivery.setBackaddress(backaddress);
                                delivery.setBackdetailaddress(backdetailaddress);
                                delivery.set("ismail", "0");
                                deliveryService.insert(delivery);
                            }
                            else {
                                auditProject.setIf_express(ifExpress);
                            }
                        }

                        if ("11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                || auditTask.getTaskname().contains("房地产开发企业暂定资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业四级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业三级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业二级资质变更（多项变更）")
                                || "变更成品油零售经营批准证书企业名称或法定代表人".equals(auditTask.getTaskname())
                                || "变更成品油零售经营批准证书企业名称和法定代表人".equals(auditTask.getTaskname())
                                || "统一社会信用代码信息服务".equals(auditTask.getTaskname())
                                || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                || "公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                // ||
                                // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                                // 船舶注销
                                || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 消毒水注销
                                || "11370800MB28559184737011702000016".equals(auditTask.getItem_id())// 排水注销
                                || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                                // ||
                                // auditTask.getTaskname().contains("张贴宣传品审批")
                                || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                        && getIsAllZmMateri(auditProject.getRowguid()))) {
                            auditProject.setStatus(90);
                            auditProject.set("is_miaopi", "1");
                            auditProject.setBanjieresult(40);
                            auditProject.setBanjiedate(new Date());
                            if ("公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                    // ||
                                    // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                                    // 船舶注销
                                    || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 消毒水注销
                                    || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                    || "11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                    || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                    // ||
                                    // auditTask.getTaskname().contains("张贴宣传品审批")
                                    || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                            && getIsAllZmMateri(auditProject.getRowguid()))) {
                                auditProject.setRemark(bianhao);
                                auditProject.setProjectname("<AI审批>" + auditTask.getTaskname() + " " + bianhao);
                            }
                            else {
                                auditProject.setProjectname("<AI审批>" + auditTask.getTaskname());
                            }

                            // if (auditProject != null
                            // &&
                            // "11370800MB28559184337011800900413".equals(auditTask.getItem_id()))
                            // {// 船舶注销
                            // log.info("开始注销船舶营业运输证证照");
                            // CertCbyyysz certCbyyysz =
                            // iCertCbyyyszService.getCertByCertno(bianhao);
                            // if (certCbyyysz != null) {
                            // certCbyyysz.setIs_cancel("1");
                            // iCertCbyyyszService.update(certCbyyysz);
                            // }
                            // /*
                            // * Record ylggrecord =
                            // * iJnProjectService.getGgcswsxkxsqByNumber(
                            // * bianhao); if (ylggrecord != null) {
                            // * ylggrecord.set("state", "cancel");
                            // * iJnProjectService.UpdateRecord(ylggrecord);
                            // * log.info("公共场所卫生许可证变更法人名称"); }
                            // */
                            // }
                            if (auditProject != null
                                    && "11370800MB285591843370123020002".equals(auditTask.getItem_id())) {// 消毒水注销
                                log.info("开始注销消毒水证照");
                                CertCbyyysz certCbyyysz = iCertCbyyyszService.getCertByCertno(bianhao);
                                if (certCbyyysz != null) {
                                    certCbyyysz.setIs_cancel("1");
                                    iCertCbyyyszService.update(certCbyyysz);
                                }

                                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_XDCPSCWSXKZ_1", bianhao);
                                if (ylggrecord != null) {
                                    iJnProjectService.CancelRecord("EX_XDCPSCWSXKZ_1", bianhao);
                                    log.info("消毒水证照数据注销推送");
                                }

                            }

                            if (auditProject != null
                                    && "11370800MB28559184737011702000016".equals(auditTask.getItem_id())) {
                                log.info("开始注销排水证照");

                                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_WSPRPSGWXKZ_1", bianhao);
                                if (ylggrecord != null) {
                                    iJnProjectService.CancelRecord("EX_WSPRPSGWXKZ_1", bianhao);
                                    log.info("消毒水证照数据注销推送");
                                }
                            }

                            if (auditProject != null && "公共场所卫生许可证注销".equals(auditTask.getTaskname())) {
                                log.info("开始注销公共场所卫生许可证照");
                                CertGgxkws certggxk = iCertGgxkwsService.getCertByCertno(bianhao);
                                if (certggxk != null) {
                                    certggxk.setIs_canale("1");
                                    iCertGgxkwsService.update(certggxk);
                                }

                                Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(bianhao);
                                if (ylggrecord != null) {
                                    ylggrecord.set("state", "cancel");
                                    iJnProjectService.UpdateRecord(ylggrecord);
                                    log.info("公共场所卫生许可证变更法人名称");
                                }
                            }
                            if (auditProject != null && "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())) {
                                log.info("开始注销水路运输");
                                CertHwslysjyxk certHwslysjyxk = iCertHwslysjyxkService.getCertByCertno(bianhao);
                                if (certHwslysjyxk != null) {
                                    certHwslysjyxk.setIs_cancel("1");
                                    iCertHwslysjyxkService.update(certHwslysjyxk);
                                }

                                Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1",
                                        bianhao);
                                if (ylggrecord != null) {
                                    ylggrecord.set("state", "cancel");
                                    ylggrecord.set("operatedate", new Date());
                                    iJnProjectService.UpdateRecord(ylggrecord);
                                    log.info("终止经营省际普通货物水路运输业务注销");
                                }
                            }

                        }
                        else {
                            auditProject.setStatus(status);
                        }
                        auditProject.setIs_test(0);
                        auditProject.set("dataid", dataid);
                        auditProject.set("formid", formid);
                        // auditProject.set("formid",
                        // ZwdtUserSession.getInstance("").getc);

                        auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());
                        Integer applyway = auditProject.getApplyway();
                        if (applyway == null) {
                            auditProject.setApplyway(10); // 默认外网申报
                        }

                        // 查看有无centerguid
                        if (StringUtils.isBlank(auditProject.getCenterguid())) {
                            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                    .getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                            if (auditOrgaServiceCenter != null) {
                                log.info("centerguid:"+auditOrgaServiceCenter.getRowguid());
                                if("undefined".equals(auditOrgaServiceCenter.getRowguid())){
                                    return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "中心标记有问题", "");
                                }
                                auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                            }
                        }
                        // 更新承诺办结时间
                        if (auditTask != null) {
                            List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual
                                    .getZantingData(auditProject.getRowguid());
                            Date acceptdat = auditProject.getAcceptuserdate();
                            Date shouldEndDate;
                            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                                IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                                        .getComponent(IAuditOrgaWorkingDay.class);
                                shouldEndDate = auditCenterWorkingDayService
                                        .getWorkingDayWithOfficeSet(auditProject.getCenterguid(), acceptdat,
                                                auditTask.getPromise_day())
                                        .getResult();
                                log.info("shouldEndDate:" + shouldEndDate);
                            }
                            else {
                                shouldEndDate = null;
                            }
                            if (auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                Duration totalDuration = Duration.ZERO; // 用于累加时间差（以秒为单位）
                                LocalDateTime currentTime = null;
                                for (AuditProjectUnusual auditProjectUnusual : auditProjectUnusuals) {
                                    // 将Date转换为Instant
                                    Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                                    if (10 == auditProjectUnusual.getOperatetype()) {
                                        // 通过Instant和系统默认时区获取LocalDateTime
                                        currentTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                    }
                                    if (currentTime != null && 11 == auditProjectUnusual.getOperatetype()) {
                                        LocalDateTime nextTime = LocalDateTime.ofInstant(instant,
                                                ZoneId.systemDefault());
                                        Duration danci = Duration.between(currentTime, nextTime);
                                        totalDuration = totalDuration.plus(danci);
                                        currentTime = null;
                                    }
                                }
                                // 将累加的时间差加到初始的Date类型的shouldEndDate上
                                Instant instant = shouldEndDate.toInstant();
                                Instant newInstant = instant.plus(totalDuration);
                                shouldEndDate = Date.from(newInstant);
                                log.info("shouldEndDate:" + shouldEndDate);
                            }
                            if (shouldEndDate != null
                                    && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))) {
                                auditProject.setPromiseenddate(shouldEndDate);
                            }
                        }
                        if(StringUtils.isNotBlank(auditProject.getStr("commitnum"))){
                            int commitnum = Integer.parseInt(auditProject.getStr("commitnum"));
                            auditProject.set("commitnum",++commitnum);
                        }else{
                            auditProject.set("commitnum",1);  
                        }
                        auditProject.set("lastcommitdate",new Date());
                        iAuditProject.updateProject(auditProject);
                    }
                    // ------ PC端逻辑 -----
                    else {
                        // 3、判断当前用户在申报的时候，已被授权
                        AuditRsCompanyBaseinfo auditrscompanybaseinfo = null;
                        // 2.1、个人用户更新外网申请人信息
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                            auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                                    .getAuditRsCompanyBaseinfoByRowguid(companyrowguid).getResult();
                            // 3.2、根据条件查询出企业基本信息数据
                            if (auditrscompanybaseinfo != null) {
                                String userLevel = this.getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                                        auditOnlineRegister.getIdnumber(), auditrscompanybaseinfo.getCompanyid());
                                // 3.2.1、如果无法查询到授权信息，则无法申报办件
                                if (userLevel == null) {
                                    return JsonUtils.zwdtRestReturn("0", "请确保用户已被该企业授权", "");
                                }
                                applyerName = auditrscompanybaseinfo.getOrganname();
                                if (ZwfwConstant.CERT_TYPE_TYSHXYDM.equals(certType)) {
                                    certNum = auditrscompanybaseinfo.getCreditcode();
                                }
                                else if (ZwfwConstant.CERT_TYPE_ZZJGDMZ.equals(certType)) {
                                    certNum = auditrscompanybaseinfo.getOrgancode();
                                }
                                legal = auditrscompanybaseinfo.getOrganlegal();
                                orgalegalidnumber = auditrscompanybaseinfo.getOrgalegal_idnumber();
                            }
                        }
                        // 个人类型应该将当前用户作为申报用户
                        else if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                            applyerName = auditOnlineIndividual.getClientname();
                            // certNum = auditOnlineIndividual.getIdnumber();
                        }

                        // 4、获取事项扩展信息
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                        // 5、获取办件状态，如网上申报类型是 网上申报后直接办理，则办件状态更新为待接件
                        int status = ZwfwConstant.BANJIAN_STATUS_WWYTJ;// 默认办件状态：外网申报已提交
                        if (ZwfwConstant.WEB_APPLY_TYPE_SL
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                            status = ZwfwConstant.BANJIAN_STATUS_DJJ;
                        }

                        // 6、更新AUDIT_PROJECT表数据
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                        AuditTask task = iAuditTask.selectUsableTaskByTaskID(auditTask.getTask_id()).getResult();
                        if (task == null) {
                            return JsonUtils.zwdtRestReturn("0", "该事项未查询到！", "");
                        }
                        if (pnList.contains(auditTask.getTask_id())) {
                            // 根据certNum判断是否体检合格
                            log.info("=========查询到教师体检报告=========");
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("sfz", certNum);
                            TeacherHealthReport teacherHealthReportByCondition = iTeacherHealthReportService
                                    .getTeacherHealthReportByCondition(sqlConditionUtil.getMap());
                            if (teacherHealthReportByCondition == null || (teacherHealthReportByCondition != null
                                    && "0".equals(teacherHealthReportByCondition.getIspass()))) {
                                return JsonUtils.zwdtRestReturn("0", "您的体检不合格或未查询到您的体检信息，无法进行申报！", "");
                            }
                        }
                        String bianhao = "";

                        if ("11370800MB285591843370123020002".equals(auditTask.getItem_id())) {
                            log.info("消毒水注销");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220831153522", projectGuid);
                            if (records != null) {
                                bianhao = records.getStr("xkzjbh");
                                log.info("消毒水注销bianhao：" + bianhao);
                            }

                        }
                        if ("11370800MB28559184737011702000016".equals(auditTask.getItem_id())) {
                            log.info("排水注销");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20230222161520", projectGuid);
                            if (records != null) {
                                bianhao = records.getStr("xkzbh");
                                log.info("排水注销bianhao：" + bianhao);
                            }

                        }

                        if ("终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())) {
                            log.info("获取到要注销的水路运输");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220519115927", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("zzbh");
                                log.info("获取到要注销的水路运输bianhao：" + bianhao);
                            }

                        }
                        if ("公共场所卫生许可证注销".equals(auditTask.getTaskname())) {
                            log.info("获取到要注销的公共许可证号");
                            Record records = iCxBusService.getPorjectByRowguid("formtable20220413141101", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("zzbh");
                                log.info("获取到要注销的公共许可bianhao：" + bianhao);
                            }

                        }

                        if ("11370883MB2857374H437106900100101".equals(auditTask.getItem_id())) {

                            Record records = iCxBusService.getPorjectByRowguid("formtable20220317111024", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("bh");
                            }

                        }

                        if ("11370883MB2857374H437309800300101".equals(auditTask.getItem_id())) {

                            Record records = iCxBusService.getPorjectByRowguid("formtable20211118012314", projectGuid);

                            if (records != null) {
                                bianhao = records.getStr("zysyzgzsbh");
                            }

                        }

                        if ("11370883MB2857374H437309800900101".equals(auditTask.getItem_id())) {
                            String flwosn = iJnService.getZjSlFlowsn("zccsy");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zccsy");

                            bianhao = "鲁济邹城村兽医第" + result + "号";

                        }
                        if ("11370883MB2857374H4370114011007".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zcrlzyba");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zcrlzyba");

                            bianhao = "(鲁)人服备字[2022]第0805" + result + "号";
                        }
                        if ("11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zccbw");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zccbw");

                            bianhao = "新出发鲁字第H1" + result + "号";
                        }
                        if ("11370883MB2857374H4370115041000".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zclczz");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            iJnService.updateZjSlFlowsn(result, "zclczz");

                            bianhao = "X370883202200" + result;
                        }
                        if ("11370883MB2857374H4370173016000".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zcdyfy");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            iJnService.updateZjSlFlowsn(result, "zcdyfy");

                            bianhao = "邹行审字【2022】影映第" + result + "号";
                        }

                        if ("11370883MB2857374H437301100800101".equals(auditTask.getItem_id())) {

                            String flwosn = iJnService.getZjSlFlowsn("zcyljg");
                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }

                            iJnService.updateZjSlFlowsn(result, "zcyljg");

                            bianhao = "邹行审370883" + Calendar.getInstance().get(Calendar.YEAR) + result;
                        }

                        if ("11370883MB2857374H4371099370000".equals(auditTask.getItem_id())) {
                            String flwosn = iJnService.getZjSlFlowsn("zlba");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }
                            iJnService.updateZjSlFlowsn(result, "zlba");
                            bianhao = "鲁邹粮备案370883" + result;

                        }

                        if ("11370883MB2857374H437305900100101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437305900100102".equals(auditTask.getItem_id())) {
                            String flwosn = iJnService.getZjSlFlowsn("lssg");

                            String result = Integer.parseInt(flwosn) + 1 + "";

                            if (result.length() == 1) {
                                result = "000" + result;
                            }
                            else if (result.length() == 2) {
                                result = "00" + result;
                            }
                            else if (result.length() == 3) {
                                result = "0" + result;
                            }
                            iJnService.updateZjSlFlowsn(result, "lssg");
                            bianhao = "鲁邹粮备案370883" + result;
                            log.info("===粮食事项编号信息TXTXTX====" + bianhao);
                        }

                        log.info("提交办件111：" + projectGuid);
                        AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);
                        // 7、在做保存操作前，获取办件原来的申请人Guid
                        String originApplyerGuid = auditProject.getApplyeruserguid();
                        // 7.1、设置办件证照类型
                        if (StringUtil.isBlank(certType)) {
                            // 7.1.1、如果证照类型为空，需要根据申请人类型设置默认值
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                                // 7.1.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                                auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                            }
                            else if (ZwfwConstant.APPLAYERTYPE_QY
                                    .equals(String.valueOf(auditProject.getApplyertype()))) {
                                // 7.1.1.2、若申请人类型为企业，则默认设置证照类型为组织机构代码证
                                auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);
                            }
                        }
                        else {
                            // 7.1.2、如果证照类型不为空，则获取传递的证照类型
                            auditProject.setCerttype(certType);
                        }
                        auditProject.setRowguid(projectGuid);
                        auditProject.setCertnum(certNum);
                        auditProject.setApplyername(applyerName);
                        // 7.1.3、类型如果为企业则申请人guid为企业的companyguid
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                            auditProject.setApplyeruserguid(companyrowguid);
                            auditProject.setOnlineapplyerguid(applyerGuid);
                        }
                        else {
                            auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());
                        }
                        auditProject.setContactperson(contactName);
                        auditProject.setContactmobile(contactMobile);
                        auditProject.setContactphone(contactPhone);
                        auditProject.setContactpostcode(postCode);
                        auditProject.setContactcertnum(contactIdnum);
                        auditProject.setRemark(remark);
                        auditProject.setAddress(address);
                        auditProject.setLegal(legal);
                        auditProject.setLegalid(orgalegalidnumber);
                        auditProject.setContactemail(email);
                        auditProject.setContactfax(fax);

                        // 预审退回的情况，需要更新物流表
                        AuditProjectDelivery delivery = deliveryService.getDeliveryByprojectguid(projectGuid);
                        // 说明第一次提交有物流记录
                        if (delivery != null) {
                            if ("1".equals(issend)) {
                                auditProject.setIf_express("1");
                                // 同时存储物流表 新表
                                delivery.setOperatedate(new Date());
                                delivery.setProjectguid(projectGuid);
                                delivery.setIssend(issend);
                                delivery.setIssendback(issendback);
                                delivery.setSendname(sendname);
                                delivery.setSendmobile(sendmobile);
                                delivery.setSendaddress(sendaddress);
                                delivery.setSenddetailaddress(senddetailaddress);
                                delivery.setRecipientname(recipientname);
                                delivery.setRecipientmobile(recipientmobile);
                                delivery.setRecipientaddress(recipientaddress);
                                delivery.setBackname(backname);
                                delivery.setBackmobile(backmobile);
                                delivery.setBackaddress(backaddress);
                                delivery.setBackdetailaddress(backdetailaddress);
                                delivery.setMaterialbill(materialbill);
                                delivery.setMaterialguidbill(materialguidbill);
                                delivery.set("ismail", "0");
                                deliveryService.update(delivery);
                            }
                            else {
                                // 说明不邮寄材料，在判断是否邮寄结果
                                if ("1".equals(issendback)) {
                                    auditProject.setIf_express("1");
                                    // 同时存储物流表 新表
                                    delivery.setOperatedate(new Date());
                                    delivery.setProjectguid(projectGuid);
                                    delivery.setIssend(issend);
                                    delivery.setIssendback(issendback);
                                    delivery.setBackname(backname);
                                    delivery.setBackmobile(backmobile);
                                    delivery.setBackaddress(backaddress);
                                    delivery.setBackdetailaddress(backdetailaddress);
                                    delivery.set("ismail", "0");
                                    deliveryService.update(delivery);
                                }
                                else {
                                    auditProject.setIf_express(ifExpress);
                                    deliveryService.deleteByGuid(delivery.getRowguid());
                                }
                            }
                        }
                        else {
                            if ("1".equals(issend)) {
                                auditProject.setIf_express("1");
                                // 同时存储物流表 新表
                                delivery = new AuditProjectDelivery();
                                delivery.setRowguid(UUID.randomUUID().toString());
                                delivery.setOperatedate(new Date());
                                delivery.setProjectguid(projectGuid);
                                delivery.setIssend(issend);
                                delivery.setIssendback(issendback);
                                delivery.setSendname(sendname);
                                delivery.setSendmobile(sendmobile);
                                delivery.setSendaddress(sendaddress);
                                delivery.setSenddetailaddress(senddetailaddress);
                                delivery.setRecipientname(recipientname);
                                delivery.setRecipientmobile(recipientmobile);
                                delivery.setRecipientaddress(recipientaddress);
                                delivery.setBackname(backname);
                                delivery.setBackmobile(backmobile);
                                delivery.setBackaddress(backaddress);
                                delivery.setBackdetailaddress(backdetailaddress);
                                delivery.setMaterialbill(materialbill);
                                delivery.setMaterialguidbill(materialguidbill);
                                delivery.set("ismail", "0");
                                deliveryService.insert(delivery);
                            }
                            else {
                                // 说明不邮寄材料，在判断是否邮寄结果
                                if ("1".equals(issendback)) {
                                    auditProject.setIf_express("1");
                                    // 同时存储物流表 新表
                                    delivery = new AuditProjectDelivery();
                                    delivery.setRowguid(UUID.randomUUID().toString());
                                    delivery.setOperatedate(new Date());
                                    delivery.setProjectguid(projectGuid);
                                    delivery.setIssend(issend);
                                    delivery.setIssendback(issendback);
                                    delivery.setBackname(backname);
                                    delivery.setBackmobile(backmobile);
                                    delivery.setBackaddress(backaddress);
                                    delivery.setBackdetailaddress(backdetailaddress);
                                    delivery.set("ismail", "0");
                                    deliveryService.insert(delivery);
                                }
                                else {
                                    auditProject.setIf_express(ifExpress);
                                }
                            }
                        }

                        if ("11370883MB2857374H437301100800101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                || auditTask.getTaskname().contains("房地产开发企业暂定资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业四级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业三级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业二级资质变更（多项变更）")
                                || "变更成品油零售经营批准证书企业名称或法定代表人".equals(auditTask.getTaskname())
                                || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                || "变更成品油零售经营批准证书企业名称和法定代表人".equals(auditTask.getTaskname())
                                || "统一社会信用代码信息服务".equals(auditTask.getTaskname())
                                || "公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                // ||
                                // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                                // 船舶注销
                                || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 消毒水注销
                                || "11370800MB28559184737011702000016".equals(auditTask.getItem_id())// 排水注销
                                || "11370883MB2857374H437305900100101".equals(auditTask.getItem_id())// 粮食收购企业备案（首次
                                || "11370883MB2857374H437305900100102".equals(auditTask.getItem_id())// 粮食收购企业备案（变更
                                || "11370883MB2857374H437106900100101".equals(auditTask.getItem_id())// 中医诊所备案
                                || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                                // ||
                                // auditTask.getTaskname().contains("张贴宣传品审批")
                                || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                        && getIsAllZmMateri(auditProject.getRowguid()))) {
                            auditProject.set("is_miaopi", "1");
                            auditProject.setStatus(90);
                            auditProject.setBanjieresult(40);
                            if ("公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                    // ||
                                    // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                                    // 船舶注销
                                    || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 消毒水注销
                                    || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                    || "11370883MB2857374H437301100800101".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                    || "11370883MB2857374H437305900100101".equals(auditTask.getItem_id())// 粮食收购企业备案（首次
                                    || "11370883MB2857374H437305900100102".equals(auditTask.getItem_id())// 粮食收购企业备案（变更
                                    || "11370883MB2857374H437106900100101".equals(auditTask.getItem_id())// 中医诊所备案
                                    || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                    || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                                    // ||
                                    // auditTask.getTaskname().contains("张贴宣传品审批")
                                    || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                            && getIsAllZmMateri(auditProject.getRowguid()))) {
                                auditProject.setRemark(bianhao);
                                auditProject.setProjectname("<AI审批>" + auditTask.getTaskname() + " " + bianhao);
                            }
                            else {
                                auditProject.setProjectname("<AI审批>" + auditTask.getTaskname());
                            }

                        }
                        else {
                            auditProject.setStatus(status);
                        }

                        if (auditProject != null && "11370800MB285591843370123020002".equals(auditTask.getItem_id())) {// 消毒水注销
                            log.info("开始注销消毒水证照");
                            CertCbyyysz certCbyyysz = iCertCbyyyszService.getCertByCertno(bianhao);
                            if (certCbyyysz != null) {
                                certCbyyysz.setIs_cancel("1");
                                iCertCbyyyszService.update(certCbyyysz);
                            }

                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_XDCPSCWSXKZ_1", bianhao);
                            if (ylggrecord != null) {
                                iJnProjectService.CancelRecord("EX_XDCPSCWSXKZ_1", bianhao);
                                log.info("消毒水证照数据注销推送");
                            }

                        }

                        if (auditProject != null
                                && "11370800MB28559184737011702000016".equals(auditTask.getItem_id())) {// 排水注销
                            log.info("开始注销排水证照");

                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("EX_WSPRPSGWXKZ_1", bianhao);
                            if (ylggrecord != null) {
                                iJnProjectService.CancelRecord("EX_WSPRPSGWXKZ_1", bianhao);
                                log.info("排水证照数据注销推送");
                            }

                        }

                        if (auditProject != null && "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())) {
                            log.info("开始注销水路运输");
                            CertHwslysjyxk certHwslysjyxk = iCertHwslysjyxkService.getCertByCertno(bianhao);
                            if (certHwslysjyxk != null) {
                                certHwslysjyxk.setIs_cancel("1");
                                iCertHwslysjyxkService.update(certHwslysjyxk);
                            }

                            Record ylggrecord = iJnProjectService.getDzbdDetailByZzbh("ex_sjpthwslysjyxk_1", bianhao);
                            if (ylggrecord != null) {
                                ylggrecord.set("state", "cancel");
                                ylggrecord.set("operatedate", new Date());
                                iJnProjectService.UpdateRecord(ylggrecord);
                                log.info("终止经营省际普通货物水路运输业务注销");
                            }
                        }

                        if (auditProject != null && "公共场所卫生许可证注销".equals(auditTask.getTaskname())) {
                            log.info("开始注销公共场所卫生许可证照");
                            CertGgxkws certggxk = iCertGgxkwsService.getCertByCertno(bianhao);
                            if (certggxk != null) {
                                certggxk.setIs_canale("1");
                                iCertGgxkwsService.update(certggxk);
                            }

                            Record ylggrecord = iJnProjectService.getGgcswsxkxsqByNumber(bianhao);
                            if (ylggrecord != null) {
                                ylggrecord.set("state", "cancel");
                                ylggrecord.set("operatedate", new Date());
                                iJnProjectService.UpdateRecord(ylggrecord);
                                log.info("公共场所卫生许可证变更法人名称");
                            }
                        }

                        if (auditTask.getTaskname().contains("社会团体成立登记") && "370800".equals(auditTask.getAreacode())) {
                            JSONObject submitString = new JSONObject();

                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnShzzpt/submitProject",
                                    submitString.toString());
                            log.info("社会团体成立登记返回结果：" + projectid);
                        }

                        if (auditTask.getTaskname().contains("社会团体名称变更登记")
                                && "370800".equals(auditTask.getAreacode())) {
                            JSONObject submitString = new JSONObject();
                            String remark1 = getStringRandom(32).toLowerCase();
                            auditProject.setRemark(remark1 + "SHZZ");
                            submitString.put("projectguid", auditProject.getRowguid());
                            submitString.put("id", remark1);
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnShzzpt/submitProjectShttdj",
                                    submitString.toString());
                            log.info("社会团体名称变更登记返回结果：" + projectid);
                        }

                        if (auditTask.getTaskname().contains("在限制、禁止的区域或者路段通行、停靠机动车的许可")
                                && "370800".equals(auditTask.getAreacode())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            submitString.put("dhuuid", auditOnlineRegister.getStr("dhuuid"));
                            submitString.put("formid", fformid);
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnwjw/addApply", submitString.toString());
                            log.info("微警务返回结果：" + projectid);
                        }

                        if ("11370800MB285591843371018037005".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addJtjXyCHfApply",
                                    submitString.toString());
                            log.info("addJtjXyCHfApply返回结果：" + projectid);
                        }

                        //
                        if ("11370800MB285591843370118033002".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addJtjHwcyzgsqApply",
                                    submitString.toString());
                            log.info("addJtjHwcyzgsqApply返回结果：" + projectid);
                        }

                        if ("11370800MB285591843370118033001".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addJtjLkcyzgsqApply",
                                    submitString.toString());
                            log.info("addJtjLkcyzgsqApply返回结果：" + projectid);
                        }

                        if ("11370800MB28559184337101803700603".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addWlyyczqcdlyshfApply",
                                    submitString.toString());
                            log.info("addWlyyczqcdlyshfApply返回结果：" + projectid);
                        }

                        if ("11370800MB285591843370118035001".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addWhjsycyzgxkApply",
                                    submitString.toString());
                            log.info("addWlyyczqcdlyshfApply返回结果：" + projectid);
                        }

                        if ("11370800MB285591843370118035003".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addWhyyycyzgxkApply",
                                    submitString.toString());
                            log.info("addWlyyczqcdlyshfApply返回结果：" + projectid);
                        }

                        if ("11370800MB28559184337011802600001".equals(auditTask.getItem_id())) {
                            JSONObject submitString = new JSONObject();
                            submitString.put("projectguid", auditProject.getRowguid());
                            String projectid = HttpUtil.doPostJson(
                                    "http://112.6.110.176:25001/jnzwdt/rest/jnjtj/addXyczckyjyxkApply",
                                    submitString.toString());
                            log.info("addXyczckyjyxkApply返回结果：" + projectid);
                        }

                        // 公共场所卫生许可证新申请新增字段
                        String businesstype = obj.getString("businesstype");
                        String hasventilationsystem = obj.getString("hasventilationsystem");
                        if ("370829".equals(areaCode) && StringUtil.isNotBlank(businesstype)) {
                            auditProject.set("businesstype", businesstype);
                            auditProject.set("hasventilationsystem", hasventilationsystem);
                            auditProject.set("iscommitment", "1");
                            auditProject.set("isnotify", "1");
                        }

                        auditProject.setIs_test(0);
                        auditProject.set("dataid", dataid);
                        auditProject.set("formid", formid);
                        // 个性化办件流水号，以及搜索密码
/*                        TAHandleProjectImpl jnproject = new TAHandleProjectImpl();
                        Record record = jnproject.getFlowsn(auditTask, auditProject);
                        String flowsn = record.getStr("receiveNum");
                        String searchpwd = record.getStr("searchpwd");*/
                        String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());
                        auditProject.setFlowsn(flowsn);
                        // auditProject.setFlowsn("9988721223");
/*
                        auditProject.set("searchpwd", searchpwd);
*/
                        auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());
                        Integer applyway = auditProject.getApplyway();
                        if (applyway == null) {
                            auditProject.setApplyway(10); // 默认外网申报
                        }
                        log.info("提交办件222：" + projectGuid);
//                        if(StringUtils.isNotBlank(auditProject.getStr("commitnum"))){
//                            int commitnum = Integer.parseInt(auditProject.getStr("commitnum"));
//                            auditProject.set("commitnum",++commitnum);
//                        }else{
//                            auditProject.set("commitnum",1);
//                        }
//                        auditProject.set("lastcommitdate",new Date());
                        iAuditProject.updateProject(auditProject);

                        AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
                        auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                        auditProjectOperation.setAreaCode(auditTask.getAreacode());
                        auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                        auditProjectOperation.setApplyerName(auditProject.getApplyername());
                        auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                        auditProjectOperation.setOperatedate(new Date());
                        auditProjectOperation.setOperateusername(auditOnlineIndividual.getClientname());
                        auditProjectOperation.setOperateUserGuid(auditOnlineIndividual.getApplyerguid());
                        auditProjectOperation.setOperateType(String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ));
                        auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                        iAuditProjectOperation.addProjectOperation(auditProjectOperation);

                        // 3、获取用户申报的事项
                        String applyerUserGuid = auditOnlineIndividual.getApplyerguid();
                        // 如果是企业办件
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                            applyerUserGuid = auditrscompanybaseinfo.getCompanyid();
                        }
                        // 首先查看是否存在企业办件
                        AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                                .getOnlineProjectByApplyerGuid(projectGuid, applyerUserGuid).getResult();
                        // 判断当前办件是否已生成
                        String declarerguid = auditOnlineIndividual.getApplyerguid();
                        String declarername = auditOnlineIndividual.getClientname();
                        // 5.3、获取当前申报的企业基本信息
                        AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfoQY = iAuditRsCompanyBaseinfo
                                .getAuditRsCompanyBaseinfoByRowguid(companyrowguid).getResult();
                        String sqGuid = "";
                        if (StringUtil.isNotBlank(originAuditRsCompanyBaseinfoQY)) {
                            // 5.4、获取当前用户授权基本信息
                            AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(originAuditRsCompanyBaseinfoQY.getCompanyid(),
                                            auditOnlineRegister.getAccountguid())
                                    .getResult();
                            sqGuid = auditOnlineCompanyGrant != null ? auditOnlineCompanyGrant.getRowguid() : "";
                        }
                        if (auditOnlineProject != null) {
                            if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                                // 如果已有该办件，判断当前的企业是否与之前的相同
                                if (originApplyerGuid.equals(companyrowguid)) {
                                    auditOnlineProject.setApplydate(new Date());
                                    // 5.5.1、判断当前用户的授权信息与原办件是否相同，如果不同则需要更新
                                    if (!sqGuid.equals(auditOnlineProject.getSqGuid())) {
                                        // 5.5.1.1、更新授权信息
                                        auditOnlineProject.setSqGuid(sqGuid);
                                    }
                                    iAuditOnlineProject.updateProject(auditOnlineProject);
                                }
                                else {
                                    // 如果企业数据与之前的不同，则需要删除原有企业
                                    iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid, originApplyerGuid);
                                    // 新增企业
                                    iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                            applyerGuid, applyerName, companyrowguid, sqGuid);
                                }
                            }
                            else {
                                auditOnlineProject.setApplydate(new Date());
                                iAuditOnlineProject.updateProject(auditOnlineProject);
                            }
                        }
                        else {
                            // 企业初始化生成办件
                            iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                    applyerGuid, applyerName, companyrowguid, sqGuid);
                        }

                        int oldstatus = 0; // 预审打回时使用
                        if (auditOnlineProject != null) {
                            oldstatus = Integer.parseInt(auditOnlineProject.getStatus());
                        }
                        if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                            if(auditProject.getApplydate()==null){
                                auditProject.setApplydate(new Date());
                            }
                            if(StringUtils.isNotBlank(auditProject.getStr("commitnum"))){
                                int commitnum = Integer.parseInt(auditProject.getStr("commitnum"));
                                auditProject.set("commitnum",++commitnum);
                            }else{
                                auditProject.set("commitnum",1);
                            }
                            auditProject.set("lastcommitdate",new Date());
                            iAuditProject.updateProject(auditProject);
                        }
                        // 6、更新AUDIT_ONLINE_PROJECT表数据
                        Map<String, String> updateFieldMap = new HashMap<>();
                        updateFieldMap.put("applyername=", applyerName);
                        if ("11370883MB2857374H437301100800101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800300101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H437309800900101".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370114011007".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173001002-cx".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370115041000".equals(auditTask.getItem_id())
                                || "11370883MB2857374H4370173016000".equals(auditTask.getItem_id())
                                || auditTask.getTaskname().contains("房地产开发企业暂定资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业四级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业三级资质变更")
                                || auditTask.getTaskname().contains("房地产开发企业二级资质变更（多项变更）")
                                || "变更成品油零售经营批准证书企业名称或法定代表人".equals(auditTask.getTaskname())
                                || "11370883MB2857374H4371099370000".equals(auditTask.getItem_id())
                                || "变更成品油零售经营批准证书企业名称和法定代表人".equals(auditTask.getTaskname())
                                || "统一社会信用代码信息服务".equals(auditTask.getTaskname())
                                || "公共场所卫生许可证注销".equals(auditTask.getTaskname())
                                // ||
                                // "11370800MB28559184337011800900413".equals(auditTask.getItem_id())//
                                // 船舶注销
                                || "11370800MB285591843370123020002".equals(auditTask.getItem_id())// 船舶注销
                                || "11370800MB28559184737011702000016".equals(auditTask.getItem_id())// 排水注销
                                || "终止经营省际普通货物水路运输业务".equals(auditTask.getTaskname())
                                || "11370883MB2857374H437305900100101".equals(auditTask.getItem_id())// 粮食收购企业备案（首次
                                || "11370883MB2857374H437305900100102".equals(auditTask.getItem_id())// 粮食收购企业备案（变更
                                || "113708000043121241337201705400101".equals(auditTask.getItem_id()) // 城建档案利用服务
                                || "11370881004339634T437201705400101".equals(auditTask.getItem_id()) // 曲阜城建档案利用服务
                                // ||
                                // auditTask.getTaskname().contains("张贴宣传品审批")
                                || (getGgcsxsqConfig().equals(auditTask.getItem_id())
                                        && getIsAllZmMateri(auditProject.getRowguid()))

                        ) {
                            updateFieldMap.put("projectname=", "<AI审批>" + auditTask.getTaskname() + " " + bianhao);
                            updateFieldMap.put("status=", "90");
                            updateFieldMap.put("Banjieresult=", "40");
                            updateFieldMap.put("is_evaluat=", "1");
                            updateFieldMap.put("spendtime=", "0");
                            updateFieldMap.put("banjiedate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                            // 城建档案利用服务
                            if ("113708000043121241337201705400101".equals(auditTask.getItem_id())
                                    || "11370881004339634T437201705400101".equals(auditTask.getItem_id())) {
                                auditProjectOperation = new AuditProjectOperation();
                                auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                auditProjectOperation.setAreaCode(auditTask.getAreacode());
                                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                auditProjectOperation.setOperatedate(new Date());
                                auditProjectOperation.setOperateusername("AI审批");
                                auditProjectOperation.setRemarks("处理完成！");
                                auditProjectOperation.setOperateUserGuid(auditOnlineIndividual.getApplyerguid());
                                auditProjectOperation.setOperateType(String.valueOf(ZwfwConstant.OPERATE_SL));
                                auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                                iAuditProjectOperation.addProjectOperation(auditProjectOperation);
                                auditProjectOperation = new AuditProjectOperation();
                                auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                auditProjectOperation.setAreaCode(auditTask.getAreacode());
                                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                auditProjectOperation.setOperatedate(new Date());
                                auditProjectOperation.setOperateusername("AI审批");
                                auditProjectOperation.setRemarks("审核通过！");
                                auditProjectOperation.setOperateUserGuid(auditOnlineIndividual.getApplyerguid());
                                auditProjectOperation.setOperateType(String.valueOf(ZwfwConstant.OPERATE_SPTG));
                                auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                                iAuditProjectOperation.addProjectOperation(auditProjectOperation);
                                auditProjectOperation = new AuditProjectOperation();
                                auditProjectOperation.setRowguid(UUID.randomUUID().toString());
                                auditProjectOperation.setAreaCode(auditTask.getAreacode());
                                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                                auditProjectOperation.setOperatedate(new Date());
                                auditProjectOperation.setOperateusername("AI审批");
                                auditProjectOperation.setRemarks("处理完成！");
                                auditProjectOperation.setOperateUserGuid(auditOnlineIndividual.getApplyerguid());
                                auditProjectOperation.setOperateType(String.valueOf(ZwfwConstant.OPERATE_BJ));
                                auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                                iAuditProjectOperation.addProjectOperation(auditProjectOperation);
                            }
                        }
                        else {
                            updateFieldMap.put("status=", String.valueOf(status));
                        }
                        Map<String, String> updateDateFieldMap = new HashMap<String, String>();
                        if (oldstatus != ZwfwConstant.BANJIAN_STATUS_WWYSTU) { // 判断是否是预审退回
                            updateDateFieldMap.put("applydate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        }
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("sourceguid", projectGuid);
                        sqlConditionUtil.eq("applyerguid", applyerUserGuid);
                        iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                sqlConditionUtil.getMap());

                    }
                    JSONObject datajson = new JSONObject();
                    datajson.put("AESInfo", AESInfo);
                    log.info("=======结束调用submitProjectByTaskguid接口=======" + projectGuid);
                    // 推送mq消息,同步操作给
                    log.info("提交办件推送信息给省空间对接！");
                    sendMQMessageService.sendByExchange("zwdt_exchange_handle", projectGuid + "@" + "",
                            "space.saving.docking.1");
                    return JsonUtils.zwdtRestReturn("1", "提交办件成功", datajson.toJSONString());
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
            log.info("=======submitProject接口参数：params【" + params + "】=======");
            log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    private String getAESInfo(String taskGuid, AuditOnlineRegister auditOnlineRegister)
            throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        // 返回经 BASE64 处理之后的密钥字符串
        String strKeyAES = AESUtil.getStrKeyAES();
        // TODO: 填入文档中的秘钥
        // String strKeyAES = "ahFl8AGitZYMg0HV9XT69/XaEguuRjriZkSZzojw40E=";
        // System.out.println("密钥：" + strKeyAES);
        // 将使用 Base64 加密后的字符串类型的 secretKey 转为 SecretKey
        SecretKey secretKey = AESUtil.strKey2SecretKey(strKeyAES);

        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();

        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("itemCode", auditTask.getItem_id());
        jsonObject.put("itemName", auditTask.getTaskname());
        jsonObject.put("deptName", auditTask.getOuname());
        jsonObject.put("userId", auditOnlineRegister.getLoginid());
        jsonObject.put("userType", auditOnlineRegister.getUsertype());
        jsonObject.put("projectTarget", auditOnlineRegister.getUsername());
        jsonObject.put("licenseNo", auditOnlineRegister.getIdnumber());
        jsonObject.put("mobile", auditOnlineRegister.getMobile());
        String content = jsonObject.toJSONString();
        System.out.println("加密的内容：" + content);
        // 避免出现中文乱码，此处务必指定编码
        // byte[] publicEncrypt =
        // AESUtil.encryptAES(content.getBytes(StandardCharsets.UTF_8),
        // secretKey);
        // 使用 base64 转字符串
        // String publicEncryptStr = AESUtil.byte2Base64(publicEncrypt);
        // System.out.println("AES加密后：" + publicEncryptStr);
        return "";
    }

    /**
     * 删除办件的接口(待提交页面中放弃申报调用)
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/deleteProject", method = RequestMethod.POST)
    public String deleteProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用deleteProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.4、 获取事项guid
                String taskGuid = obj.getString("taskguid");
                // 1.5、 获取公司id
                String companyid = obj.getString("companyid");
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 如果传了taskguid，以事项的areacode为准，如果没传则用前台传来的areacode
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 2、删除AUDIT_PROJECT表数据
                    iAuditProject.deleteProjectByGuid(projectGuid, areaCode);
                    // 3、删除AUDIT_ONLINE_PROJECT表数据，如果传递了companyid，则为企业办件，需要根据该guid删除办件
                    if (StringUtil.isNotBlank(companyid)) {
                        iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid, companyid);
                    }
                    else {
                        iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid,
                                auditOnlineIndividual.getApplyerguid());
                    }
                    log.info("=======结束调用deleteProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "删除办件成功", "");
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
            log.info("=======deleteProject接口参数：params【" + params + "】=======");
            log.info("=======deleteProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "删除办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取材料合格标准的接口（点开上传按钮后页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getMaterialstandardByGuid", method = RequestMethod.POST)
    public String getMaterialstandardByGuid(@RequestBody String params) {
        try {
            log.info("=======开始调用getMaterialstandardByGuid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项材料标识
                String taskMaterialGuid = obj.getString("taskmaterialguid");
                // 1.2、获取办件材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskMaterialGuid)
                        .getResult();
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (auditTaskMaterial != null) {
                    dataJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
                    dataJson.put("projectmaterialguid", projectMaterialGuid);// 办件材料主键
                    dataJson.put("materialstantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                            : auditTaskMaterial.getStandard());// 材料合格标准
                }
                log.info("=======结束调用getMaterialstandardByGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取材料合格标准成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialstandardByGuid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialstandardByGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料合格标准异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取材料对应的附件列表接口（点开上传按钮后页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMaterialAttachListbyClientguid", method = RequestMethod.POST)
    public String getMaterialAttachListbyClientguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMaterialAttachListbyClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料对应附件的业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取上传方式（0:云盘 1:本地上传 2:手机上传 为空显示所有）
                String uploadType = obj.getString("uploadtype");
                List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
                // 2、获取办件材料对应的附件

                List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                    String cliengTag = frameAttachInfo.getCliengTag();
                    if (StringUtil.isBlank(uploadType) || uploadType.equals(cliengTag)) {
                        JSONObject attachJson = new JSONObject();
                        FrameAttachStorage frameAttachStorage = iAttachService
                                .getAttach(frameAttachInfo.getAttachGuid());
                        if (frameAttachStorage != null) {
                            // 3、设置附件信息
                            attachJson.put("attachicon", tazwfwrootpath + "rest/intermediary/getmaterials?attachguid="
                                    + frameAttachStorage.getAttachGuid());// 附件图标
                            attachJson.put("attachname", frameAttachInfo.getAttachFileName()); // 附件名称
                            attachJson.put("attachsize", AttachUtil.convertFileSize(frameAttachInfo.getAttachLength()));// 附件大小
                            attachJson.put("attachsource",
                                    "dzzzglxt".equals(frameAttachInfo.getCliengTag()) ? "电子证照管理系统"
                                            : frameAttachInfo.getCliengTag());// 附件来源
                            attachJson.put("attachguid", frameAttachInfo.getAttachGuid());// 附件唯一标识
                            attachJsonList.add(attachJson);
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("attachlist", attachJsonList);
                log.info("=======结束调用getMaterialAttachListbyClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取上传的附件列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialAttachListbyClientguid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialAttachListbyClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取上传的附件列表异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断材料是否上传的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkMaterialIsUploadByClientguid", method = RequestMethod.POST)
    public String checkMaterialIsUploadByClientguid(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件材料附件业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取办件材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                // 1.3、获取共享材料实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.4、获取材料提交状态
                String status = obj.getString("status");
                // 1.5、获取材料类型
                String type = obj.getString("type");
                // 1.6、获取材料对应附件是否需要上传到证照库（只有C级证照才需要）
                String needLoad = obj.getString("needload");
                // 1.7、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 2、获取办件材料对应的附件数量
                int attachCount = iAttachService.getAttachCountByClientGuid(clientGuid);
                int intStatus = Integer.parseInt(status); // 材料提交状态
                // 3、根据材料是否有附件及材料的提交状态更新材料的提交状态
                if (attachCount > 0) {
                    // 3.1、若材料有对应的附件，说明电子材料已提交
                    switch (intStatus) {
                        // 3.1.1、材料原先的状态为未提交更新为电子已提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                            break;
                        // 3.1.2、材料原先的状态为纸质已提交更新为电子和纸质都提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                            break;
                        default:
                            break;
                    }
                }
                else {
                    // 3.2、若材料没有对应的附件，说明电子材料没有提交
                    switch (intStatus) {
                        // 3.2.1、材料原先的状态为电子已提交更新为未提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                            break;
                        // 3.2.2、材料原先的状态为电子和纸质都提交更新为纸质已提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                            break;
                        default:
                            break;
                    }

                    // 新增逻辑 待邮寄状态
                    if (StringUtil.isNotBlank(projectMaterialGuid)) {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("rowguid", projectMaterialGuid);
                        List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditProjectMaterials != null && !auditProjectMaterials.isEmpty()
                                && auditProjectMaterials.get(0).getStatus() == 50) {
                            intStatus = 50;
                        }
                    }

                }
                // 4、更新办件材料数据
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setStatus(intStatus);
                auditProjectMaterial.setCliengguid(clientGuid);
                auditProjectMaterial.setRowguid(projectMaterialGuid);
                if (StringUtil.isNotBlank(projectGuid)) {
                    auditProjectMaterial.setProjectguid(projectGuid);
                }
                iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                // 5、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表、5：待邮寄）
                int showButton = 0; // 按钮显示方式
                String cliengTag = "";
                if (StringUtil.isBlank(shareMaterialIGuid)) {
                    // 5.1、如果没有从证照库引用附件，则为普通附件及填表
                    if (String.valueOf(WorkflowKeyNames9.MaterialType_Form).equals(type)) {
                        // 5.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                        showButton = attachCount > 0 ? 4 : 3;
                    }
                    else {
                        // 5.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                        // 新增情况 没上传材料但是点击按钮可能是待邮寄
                        if (attachCount > 0) {
                            showButton = 1;
                        }
                        else {
                            if (auditProjectMaterial != null && auditProjectMaterial.getStatus() == 50) {
                                showButton = 5;
                            }
                            else {
                                showButton = 0;
                            }
                        }
                    }
                }
                else {
                    // 5.2、如果关联了证照库
                    if (ZwdtConstant.NEEDLOAD_Y.equals(needLoad)) {
                        // 5.2.1、如果需要更新证照库，有附件则显示已上传，没有附件则显示未未上传
                        showButton = attachCount > 0 ? 1 : 0;
                        // 5.2.2、更新证照库版本
                        CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                        List<JSONObject> rsInfos = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                ZwdtConstant.CERTAPPKEY);
                        int countRs = 0;
                        if (rsInfos != null && rsInfos.size() > 0) {
                            countRs = rsInfos.size();
                        }
                        // 5.2.2.1、数量不一致
                        if (attachCount > 0 && attachCount != countRs) {
                            certInfo.setCertcliengguid(clientGuid);
                            shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                    ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                            // 5.2.2.1.1、关联到办件材料
                            auditProjectMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                            iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                        }
                        else if (attachCount == countRs) {
                            // 5.2.2.2、数量一致继续比较
                            List<FrameAttachInfo> projectInfos = iAttachService
                                    .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());

                            projectInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                            boolean isModify = false;
                            if (rsInfos != null && rsInfos.size() > 0) {
                                rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                for (int i = 0; i < rsInfos.size(); i++) {
                                    // 如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                    cliengTag = projectInfos.get(i).getCliengTag();
                                    if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                            || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                            && rsInfos.get(i).getLong("size").longValue() != projectInfos.get(i)
                                                    .getAttachLength().longValue()) {
                                        isModify = true;
                                        break;
                                    }
                                    else {
                                        // 若文件未发生改变，则按钮显示维持原样
                                        CertInfo certInfo2 = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                        if (certInfo2 != null) {
                                            if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                String materialType = certInfo2.getMaterialtype();
                                                if (Integer.parseInt(materialType) == 1) {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                    // 已应用证照
                                                    // 修改audit_project_material
                                                    // 。 is_glcert
                                                    auditProjectMaterial.set("is_glcert", "1");
                                                    iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                                                }
                                                else if (Integer.parseInt(materialType) == 2) {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                }
                                                else {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                isModify = true;
                            }
                            if (isModify) {
                                certInfo.setCertcliengguid(clientGuid);
                                shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                        ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                // 5.2.2.2.2、关联到办件材料
                                auditProjectMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                            }
                        }
                    }
                    else {
                        // 5.2.3、如果不需要更新证照库，有附件则则根据共享材料的类别显示已引用共享材料，没有附件则显示未未上传
                        if (attachCount > 0) {
                            CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                            if (certInfo != null) {
                                // 5.2.3.1、比较证照实例里的附件和上传的附件
                                List<JSONObject> rsInfos = iCertAttachExternal
                                        .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                int countRs = 0;
                                if (rsInfos != null && rsInfos.size() > 0) {
                                    countRs = rsInfos.size();
                                }
                                if (attachCount != countRs) {
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                }
                                // 5.2.3.1.1、数量一致比较详细的附件内容
                                else if (attachCount == countRs) {
                                    List<FrameAttachInfo> projectInfos = iAttachService
                                            .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                    projectInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                    if (rsInfos != null && rsInfos.size() > 0) {
                                        rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            // 5.2.3.1.2、AttachStorageGuid不同说明附件改动过
                                            cliengTag = projectInfos.get(i).getCliengTag();
                                            if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                    || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                    && rsInfos.get(i).getLong("size").longValue() != projectInfos.get(i)
                                                            .getAttachLength().longValue()) {
                                                showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                break;
                                            }
                                            else {
                                                // 5.2.3.1.3、若文件未发生改变，则按钮显示维持原样
                                                CertInfo certInfo2 = iCertInfoExternal
                                                        .getCertInfoByRowguid(shareMaterialIGuid);
                                                if (certInfo2 != null) {
                                                    if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                        String materialType = certInfo2.getMaterialtype();
                                                        if (Integer.parseInt(materialType) == 1) {
                                                            showButton = Integer.parseInt(
                                                                    ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                            // 已应用证照
                                                            // 修改audit_project_material
                                                            // 。 is_glcert
                                                            auditProjectMaterial.set("is_glcert", "1");
                                                            iAuditProjectMaterial
                                                                    .updateProjectMaterial(auditProjectMaterial);
                                                        }
                                                        else if (Integer.parseInt(materialType) == 2) {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                        }
                                                        else {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            // 5.2.3.2、未上传
                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);

                        }

                    }
                }
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("showbutton", showButton);
                dataJson.put("needload", needLoad);
                dataJson.put("status", intStatus);
                dataJson.put("sharematerialiguid", shareMaterialIGuid);
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkMaterialIsUploadByClientguid接口参数：params【" + params + "】=======");
            log.info("=======checkMaterialIsUploadByClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的办件的接口（办件中心各模块调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getMyProject", method = RequestMethod.POST)
    public String getMyProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取搜索内容
                String keyWord = obj.getString("keyword");
                // 1.4、获取办件的状态（0:全部 1:待提交 2:待补正 3:待提交原件 4:待缴费 5:审批中 6:待签收 7:待评价
                // 8：已办结 9:其他）
                String status = obj.getString("status");
                // 1.5、获取区域编码
                // String areaCode = obj.getString("areacode");
                // 1.6、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、查询各个状态的办件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件
                    sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid()); // 申请人标识
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("projectname", keyWord); // 事项名称
                    }
                    // 3.2、根据各种办件的状态拼接不同的SQL语句
                    int intStatus = Integer.parseInt(status);
                    switch (intStatus) {
                        case 0:
                            // 全部办件（外网申报已提交、外网申报预审退回、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过、正常办结、不予受理、撤销申请、异常终止）
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTU + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + "," + ZwdtConstant.BANJIAN_STATUS_YJJ
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DBB + "," + ZwdtConstant.BANJIAN_STATUS_YSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + "," + ZwdtConstant.BANJIAN_STATUS_BYSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        case 1:
                            // 待提交(外网申报未提交和预审不通过的办件)
                            sqlConditionUtil.in("status",
                                    ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU);
                            break;
                        case 2:
                            // 待补正(待补办的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                            break;
                        case 3:
                            // 待原件提交（功能暂无）TODO
                            break;
                        case 4:
                            // 待缴费(需要缴费并且已核价未收讫的办件)
                            sqlConditionUtil.eq("is_charge", "1"); // 需要缴费
                            sqlConditionUtil.eq("is_check", "1"); // 已核价
                            sqlConditionUtil.eq("is_fee", "0"); // 未收讫
                            break;
                        case 5:
                            // 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_YSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG);
                            break;
                        case 6:
                            // 待签收（功能暂无） TODO
                            break;
                        case 7:
                            // 待评价(未评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "0");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 8:
                            // 已办结(已评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "1");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 9:
                            // 其他(不予受理、撤销申请、异常终止)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_BYSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        default:
                            break;
                    }
                    // 3.3、根据条件查询到办件数据
                    PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int totalCount = pageData.getRowCount(); // 办件总数
                    List<AuditOnlineProject> auditOnlineProjectList = pageData.getList(); // 办件数据集合
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
                    for (AuditOnlineProject auditOnlineProject : auditOnlineProjectList) {
                        String type = auditOnlineProject.getType();
                        // 4.1、如果是普通办件，返回普通办件的JSON数据
                        if (ZwdtConstant.ONLINEPROJECTTYPE_PROJECT.equals(type)) {
                            String projectGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus()); // 办件状态
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            String taskType = auditOnlineProject.getTasktype(); // 事项类型
                            // 4.1.1、获取事项基本信息
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditOnlineProject.getTaskguid(), true)
                                    .getResult();
                            if (auditTask != null) {
                                JSONObject projectJson = new JSONObject();
                                // 4.1.2、获取办件基本的信息拼接JSON
                                // 4.1.2.1、获取办件的基本信息拼接JSON
                                projectJson.put("projectname", auditOnlineProject.getProjectname()); // 事项名称
                                projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                                projectJson.put("projectguid", projectGuid); // 办件标识
                                projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 事项标识
                                projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                projectJson.put("type", auditOnlineProject.getType()); // 类型
                                // 0：普通办件
                                // 1：套餐
                                projectJson.put("flowsn", auditOnlineProject.getFlowsn()); // 办件编号
                                projectJson.put("projectstatus", ZwdtConstant
                                        .getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus()))); // 办件状态
                                projectJson.put("ouname", auditOnlineProject.getOuname()); // 办件受理部门
                                projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT)); // 保存时间
                                projectJson.put("applydate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT)); // 办件申请时间
                                projectJson.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                projectJson.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                // 4.1.2.2、获取办件办结时间
                                String banJieData = "";
                                if (auditOnlineProject.getBanjiedate() != null) {
                                    banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                            EpointDateUtil.DATE_FORMAT);
                                }
                                projectJson.put("banjiedate", banJieData);
                                // 4.1.3、获取办件评价内容
                                String evaluate = "无评价";
                                AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                        .selectEvaluatByClientIdentifier(auditOnlineProject.getSourceguid())
                                        .getResult();
                                if (auditOnlineEvaluat != null
                                        && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                    evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
                                            auditOnlineEvaluat.getSatisfied());
                                }
                                projectJson.put("evaluate", evaluate);
                                // 4.1.4、获取办件剩余时间
                                String spareTime = "";
                                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                        .getSparetimeByProjectGuid(projectGuid).getResult();
                                if (auditProjectSparetime != null) {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                        spareTime = "即办件";
                                    }
                                    else {
                                        int spareMinutes = auditProjectSparetime.getSpareminutes();
                                        if (spareMinutes >= 0) {
                                            spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                        else {
                                            spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                    }
                                }
                                else {
                                    if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                        spareTime = "待预审";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                        spareTime = "预审通过";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "已接件";
                                        }
                                    }
                                    else if ((projectStatus == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ)) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            if (auditOnlineProject.getSpendtime() != null) {
                                                int spendMinute = auditOnlineProject.getSpendtime();
                                                spareTime = "办理用时：" + CommonUtil.getSpareTimes(spendMinute);
                                            }
                                        }
                                    }
                                    else {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "距承诺办结期限：--个工作日";
                                        }
                                    }
                                }
                                projectJson.put("sparetime", spareTime);

                                // 4.1.5、获取办件状态
                                if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                    // 4.1.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                    projectJson.put("currentstatus", "待提交");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                    // 4.1.5.2、外网申报预审退回（预审退回状态）
                                    projectJson.put("currentstatus", "预审退回");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                                    // 4.1.5.2.1、获取预审退回原因
                                    /*
                                     * SqlConditionUtil opSqlConditionUtil = new
                                     * SqlConditionUtil();
                                     * opSqlConditionUtil.eq("projectGuid",
                                     * projectGuid);
                                     * opSqlConditionUtil.eq("operatetype",
                                     * ZwfwConstant.OPERATE_YSDH);
                                     */
                                    AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                                            .getAuditOperation(projectGuid, ZwfwConstant.OPERATE_YSDH).getResult();
                                    projectJson.put("remarks",
                                            auditProjectOperation == null ? "" : auditProjectOperation.getRemarks());
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                    // 4.1.5.3、待补办、受理后待补办（待补正状态）
                                    projectJson.put("currentstatus", "待补正");
                                    String buZhengText = "";// 补正材料名称
                                    int buZhengCount = 0; // 补正材料数量
                                    SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();// 补正材料检索条件
                                    materialSqlConditionUtil.eq("projectguid", projectGuid);
                                    materialSqlConditionUtil.in("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                                    List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                            .selectProjectMaterialByCondition(materialSqlConditionUtil.getMap())
                                            .getResult();
                                    for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                        buZhengText += auditProjectMaterial.getTaskmaterial() + "；";
                                        buZhengCount++;
                                    }
                                    if (StringUtil.isNotBlank(buZhengText)) {
                                        buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                                + buZhengCount + "份材料）";
                                    }
                                    projectJson.put("buzhengtext",
                                            StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);// 补正材料
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                    // 4.1.5.4、正常办结且未评价（待评价状态）
                                    projectJson.put("currentstatus", "待评价");
                                    // 获取办件是否是审批不通过,40不通过，50通过
                                    if (StringUtil.isNotBlank(auditOnlineProject.getBanjieresult())) {
                                        Integer spStatus = auditOnlineProject.getBanjieresult();
                                        if (ZwfwConstant.BANJIAN_STATUS_SPBTG == spStatus) {
                                            projectJson.put("banjieresult", "1");
                                        }
                                    }
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                    // 4.1.5.5、正常办结且已评价（待评价状态）
                                    projectJson.put("currentstatus", "已办结");
                                    // 获取办件是否是审批不通过,40不通过，50通过
                                    if (StringUtil.isNotBlank(auditOnlineProject.getBanjieresult())) {
                                        Integer spStatus = auditOnlineProject.getBanjieresult();
                                        if (ZwfwConstant.BANJIAN_STATUS_SPBTG == spStatus) {
                                            projectJson.put("banjieresult", "1");
                                        }
                                    }
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_CXSQ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                                    // 4.1.5.6、不予受理、撤销申请、异常终止（其他状态）
                                    projectJson.put("currentstatus", "其他");
                                }
                                else {
                                    // 4.1.5.7、除去之前列出的状态外的办件（审批中状态）
                                    projectJson.put("currentstatus", "审批中");
                                }
                                projectJsonList.add(projectJson);
                            }
                        }
                        else if (ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(auditOnlineProject.getType())) {
                            // 4.2、如果是套餐，返回套餐的JSON数据
                            String biGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus());
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            JSONObject projectJson = new JSONObject();
                            // 4.2.1、获取套餐的基本信息拼接JSON
                            projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                            projectJson.put("projectname", auditOnlineProject.getProjectname());// 主题名称
                            projectJson.put("flowsn", auditOnlineProject.getFlowsn());// 套餐申报编号
                            projectJson.put("biguid", biGuid); // 主题实例标识
                            projectJson.put("projectguid", biGuid); // 主题实例标识
                            projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 主题标识
                            projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                            projectJson.put("type", auditOnlineProject.getType()); // 类型
                            // 0：普通办件
                            // 1：套餐
                            projectJson.put("projectstatus",
                                    ZwdtConstant.getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus())));// 办件状态
                            projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                    auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT));// 保存时间
                            projectJson.put("applydate", EpointDateUtil
                                    .convertDate2String(auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT));// 办件申请时间
                            // 4.2.2、获取套餐办结时间
                            String banJieData = "";
                            if (auditOnlineProject.getBanjiedate() != null) {
                                banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                        EpointDateUtil.DATE_FORMAT);
                            }
                            projectJson.put("banjiedate", banJieData);
                            // 4.2.3、获取套餐评价内容
                            String evaluate = "无评价";
                            AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                    .selectEvaluatByClientIdentifier(biGuid).getResult();
                            if (auditOnlineEvaluat != null
                                    && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
                                        auditOnlineEvaluat.getSatisfied());
                            }
                            projectJson.put("evaluate", evaluate);
                            // 4.2.4、获取业务标识、子申报标识、阶段标识
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            projectJson.put("yewuguid", auditSpInstance == null ? "" : auditSpInstance.getYewuguid());
                            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                    .getResult();
                            String subAppGuid = "";
                            if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
                                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                subAppGuid = auditSpISubapp.getRowguid();
                                projectJson.put("subappguid", auditSpISubapp.getRowguid());
                                projectJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                            }
                            // 4.2.5、获取套餐状态
                            if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                // 4.2.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                projectJson.put("currentstatus", "待提交");
                                projectJson.put("sparetime", "待提交");
                                projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                // 4.2.5.2、预审退回（预审退回状态）
                                projectJson.put("currentstatus", "预审退回");
                                projectJson.put("remarks", auditOnlineProject.getBackreason());// 套餐退回原因
                                projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                // 4.2.5.3、待补办、受理后待补办（预审退回状态）
                                projectJson.put("currentstatus", "待补正");
                                projectJson.put("sparetime", "待补正");

                                String buZhengText = "";// 补正材料名称
                                int buZhengCount = 0;// 补正材料数量
                                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                                        .getSpIPatchMaterialBySubappGuid(subAppGuid).getResult();
                                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                                    buZhengText += auditSpIMaterial.getMaterialname() + "、";
                                    buZhengCount++;
                                }
                                if (StringUtil.isNotBlank(buZhengText)) {
                                    buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                            + buZhengCount + "份材料）";
                                }
                                projectJson.put("buzhengtext",
                                        StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                // 4.2.5.4、正常办结且未评价（待评价状态）
                                projectJson.put("currentstatus", "待评价");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                // 4.2.5.5、正常办结且已评价（已办结状态）
                                projectJson.put("currentstatus", "已办结");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                // 4.2.5.6、待预审（外网申报已提交状态）
                                projectJson.put("currentstatus", "待预审");
                                projectJson.put("sparetime", "待预审");
                            }
                            else {
                                // 4.2.5.7、除去之前列出的状态外的办件（已办结状态）
                                projectJson.put("currentstatus", "审批中");
                                projectJson.put("sparetime", "审批中");
                            }
                            projectJsonList.add(projectJson);
                        }
                    }
                    dataJson.put("projectlist", projectJsonList);
                    dataJson.put("totalcount", totalCount);
                    dataJson.put("projectcount", totalCount);// 我的空间（我的申请使用）
                    log.info("=======结束调用getMyProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的办件成功", dataJson.toString());
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
            log.info("=======getMyProject异常信息：" + e.getMessage() + "=======");
            log.info("=======getMyProject接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的办件异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的企业办件的接口（办件中心各模块调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getMyCompanyProject", method = RequestMethod.POST)
    public String getMyCompanyProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCompanyProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取搜索内容
                String keyWord = obj.getString("keyword");
                // 1.4、获取办件的状态（0:全部 1:待提交 2:待补正 3:待提交原件 4:待缴费 5:审批中 6:待签收 7:待评价
                // 8：已办结 9:其他）
                String status = obj.getString("status");
                // 1.5、获取企业id
                String compamyId = obj.getString("companyid");
                // 1.6、用户guid
                String userguid = obj.getString("userguid");
                // 1.6、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取企业用户角色
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    String userLevel = this.getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                            auditOnlineRegister.getIdnumber(), compamyId);
                    if (userLevel == null) {
                        return JsonUtils.zwdtRestReturn("0", "请确保用户为企业用户", "");
                    }
                    // 3、查询各个状态的办件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件，如果用户是法人或者管理者，可以看到企业所有办件
                    sqlConditionUtil.eq("applyerguid", compamyId);
                    // 3.2、 如果当前用户是经办人，则只能看到自己所申请的办件
                    if (ZwdtConstant.GRANT_AGENT.equals(userLevel)) {
                        sqlConditionUtil.eq("declarerguid", auditOnlineIndividual.getApplyerguid());
                    }
                    // 3.3、如果传递了userguid，则代表为查看某个人所有的办件，则显示该userguid下所有的办件
                    if (StringUtil.isNotBlank(userguid)) {
                        AuditOnlineIndividual dbrAuditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(userguid).getResult();
                        sqlConditionUtil.eq("declarerguid", dbrAuditOnlineIndividual.getApplyerguid());
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("projectname", keyWord); // 事项名称
                    }
                    // 3.2、根据各种办件的状态拼接不同的SQL语句
                    int intStatus = Integer.parseInt(status);
                    switch (intStatus) {
                        case 0:
                            // 全部办件（外网申报已提交、外网申报预审退回、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过、正常办结、不予受理、撤销申请、异常终止）
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTU + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + "," + ZwdtConstant.BANJIAN_STATUS_YJJ
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DBB + "," + ZwdtConstant.BANJIAN_STATUS_YSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + "," + ZwdtConstant.BANJIAN_STATUS_BYSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        case 1:
                            // 待提交(外网申报未提交和预审不通过的办件)
                            sqlConditionUtil.in("status",
                                    ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU);
                            break;
                        case 2:
                            // 待补正(待补办的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                            break;
                        case 3:
                            // 待原件提交（功能暂无）TODO
                            break;
                        case 4:
                            // 待缴费(需要缴费并且已核价未收讫的办件)
                            sqlConditionUtil.eq("is_charge", "1"); // 需要缴费
                            sqlConditionUtil.eq("is_check", "1"); // 已核价
                            sqlConditionUtil.eq("is_fee", "0"); // 未收讫
                            break;
                        case 5:
                            // 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_YSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG);
                            break;
                        case 6:
                            // 待签收（功能暂无） TODO
                            break;
                        case 7:
                            // 待评价(未评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "0");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 8:
                            // 已办结(已评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "1");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 9:
                            // 其他(不予受理、撤销申请、异常终止)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_BYSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        default:
                            break;
                    }
                    // 3.3、根据条件查询到办件数据
                    PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int totalCount = pageData.getRowCount(); // 办件总数
                    // 3.4、获取近一个月的办件数量
                    sqlConditionUtil.between("operatedate", EpointDateUtil.getDateBefore(new Date(), 30), new Date());
                    PageData<AuditOnlineProject> recentpageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int recentcount = recentpageData.getRowCount(); // 近一个月办件总数
                    List<AuditOnlineProject> auditOnlineProjectList = pageData.getList(); // 办件数据集合
                    // 4、定义返回JSON对象
                    List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
                    for (AuditOnlineProject auditOnlineProject : auditOnlineProjectList) {
                        String type = auditOnlineProject.getType();
                        // 4.1、如果是普通办件，返回普通办件的JSON数据
                        if (ZwdtConstant.ONLINEPROJECTTYPE_PROJECT.equals(type)) {
                            String projectGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus()); // 办件状态
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            String taskType = auditOnlineProject.getTasktype(); // 事项类型
                            // 4.1.1、获取事项基本信息
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditOnlineProject.getTaskguid(), true)
                                    .getResult();
                            String projectApplyerGuid = auditOnlineProject.getDeclarerguid();
                            if (auditTask != null && StringUtil.isNotBlank(projectApplyerGuid)) {
                                JSONObject projectJson = new JSONObject();
                                // 4.1.2.2、如果是企业办件，获取当前办件实际申报人
                                projectJson.put("declarerguid", projectApplyerGuid);
                                projectJson.put("declarername", auditOnlineProject.getDeclarername());
                                // 4.1.2.3、如果是此办件的申报人和当前用户不同
                                if (!projectApplyerGuid.equals(auditOnlineIndividual.getApplyerguid())) {
                                    projectJson.put("watchmode", 1);
                                    // 4.1.2.4、获取提交办件人信息
                                    AuditOnlineIndividual applyerAuditOnlineIndividual = iAuditOnlineIndividual
                                            .getIndividualByApplyerGuid(projectApplyerGuid).getResult();
                                    // 4.1.2.5、获取提交办件人的等级,
                                    String projectUserLevel = this.getCompanyUserLevel(
                                            applyerAuditOnlineIndividual.getAccountguid(),
                                            applyerAuditOnlineIndividual.getIdnumber(), compamyId);
                                    // 4.1.2.6、返回提交用户等级
                                    if (ZwdtConstant.GRANT_LEGEL_PERSION.equals(projectUserLevel)) {
                                        projectJson.put("declarerlevel", "法人");
                                    }
                                    else {
                                        // 4.1.2.7、如果不为法人，则通过sqGuid去获取授权等级
                                        String sqGuid = auditOnlineProject.getSqGuid();
                                        AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                                                .getAuditOnlineCompanyGrantByGrantguid(sqGuid).getResult();
                                        String myuserLevel = auditOnlineCompanyGrant != null
                                                ? auditOnlineCompanyGrant.getBsqlevel()
                                                : "";
                                        // 4.1.2.8、判断用户授权等级
                                        if (ZwdtConstant.GRANT_MANAGER.equals(myuserLevel)) {
                                            projectJson.put("declarerlevel", "管理者");
                                        }
                                        else if (ZwdtConstant.GRANT_AGENT.equals(myuserLevel)) {
                                            projectJson.put("declarerlevel", "代办人");
                                        }
                                    }
                                }
                                // 4.1.2.1、获取办件的基本信息拼接JSON
                                projectJson.put("projectname", auditOnlineProject.getProjectname()); // 事项名称
                                projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                                projectJson.put("projectguid", projectGuid); // 办件标识
                                projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 事项标识
                                projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                projectJson.put("type", auditOnlineProject.getType()); // 类型
                                // 0：普通办件
                                // 1：套餐
                                projectJson.put("flowsn", auditOnlineProject.getFlowsn()); // 办件编号
                                projectJson.put("projectstatus", ZwdtConstant
                                        .getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus()))); // 办件状态
                                projectJson.put("ouname", auditOnlineProject.getOuname()); // 办件受理部门
                                projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT)); // 保存时间
                                projectJson.put("applydate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT)); // 办件申请时间
                                projectJson.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                projectJson.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                // 4.1.2.2、获取办件办结时间
                                String banJieData = "";
                                if (auditOnlineProject.getBanjiedate() != null) {
                                    banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                            EpointDateUtil.DATE_FORMAT);
                                }
                                projectJson.put("banjiedate", banJieData);
                                // 4.1.3、获取办件评价内容
                                String evaluate = "无评价";
                                AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                        .selectEvaluatByClientIdentifier(auditOnlineProject.getSourceguid())
                                        .getResult();
                                if (auditOnlineEvaluat != null
                                        && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                    evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
                                            auditOnlineEvaluat.getSatisfied());
                                }
                                projectJson.put("evaluate", evaluate);
                                // 4.1.4、获取办件剩余时间
                                String spareTime = "";
                                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                        .getSparetimeByProjectGuid(projectGuid).getResult();
                                if (auditProjectSparetime != null) {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                        spareTime = "即办件";
                                    }
                                    else {
                                        int spareMinutes = auditProjectSparetime.getSpareminutes();
                                        if (spareMinutes >= 0) {
                                            spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                        else {
                                            spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                    }
                                }
                                else {
                                    if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                        spareTime = "待预审";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                        spareTime = "预审通过";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "已接件";
                                        }
                                    }
                                    else if ((projectStatus == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ)) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            if (auditOnlineProject.getSpendtime() != null) {
                                                int spendMinute = auditOnlineProject.getSpendtime();
                                                spareTime = "办理用时：" + CommonUtil.getSpareTimes(spendMinute);
                                            }
                                        }
                                    }
                                    else {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "距承诺办结期限：--个工作日";
                                        }
                                    }
                                }
                                projectJson.put("sparetime", spareTime);
                                // 获取办件是否是审批不通过
                                List<AuditProjectOperation> auditProjectOperations = iAuditProjectOperation
                                        .getOperationListForDT(projectGuid).getResult();
                                for (AuditProjectOperation auditProjectOperation : auditProjectOperations) {
                                    String operateType = auditProjectOperation.getOperateType();
                                    if (StringUtil.isNotBlank(operateType)
                                            && ZwfwConstant.OPERATE_SPBTG.equals(operateType)) {
                                        projectJson.put("isspbtg", "1");
                                    }
                                }
                                // 4.1.5、获取办件状态
                                if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                    // 4.1.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                    projectJson.put("currentstatus", "待提交");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                    // 4.1.5.2、外网申报预审退回（预审退回状态）
                                    projectJson.put("currentstatus", "预审退回");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                                    // 4.1.5.2.1、获取预审退回原因
                                    SqlConditionUtil opSqlConditionUtil = new SqlConditionUtil();
                                    opSqlConditionUtil.eq("projectGuid", projectGuid);
                                    opSqlConditionUtil.eq("operatetype", ZwfwConstant.OPERATE_YSDH);
                                    AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                                            .getAuditOperationByCondition(opSqlConditionUtil.getMap()).getResult();
                                    projectJson.put("remarks",
                                            auditProjectOperation == null ? "" : auditProjectOperation.getRemarks());
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                    // 4.1.5.3、待补办、受理后待补办（待补正状态）
                                    projectJson.put("currentstatus", "待补正");
                                    String buZhengText = "";// 补正材料名称
                                    int buZhengCount = 0; // 补正材料数量
                                    SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();// 补正材料检索条件
                                    materialSqlConditionUtil.eq("projectguid", projectGuid);
                                    materialSqlConditionUtil.in("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                                    List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                            .selectProjectMaterialByCondition(materialSqlConditionUtil.getMap())
                                            .getResult();
                                    for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                        buZhengText += auditProjectMaterial.getTaskmaterial() + "；";
                                        buZhengCount++;
                                    }
                                    if (StringUtil.isNotBlank(buZhengText)) {
                                        buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                                + buZhengCount + "份材料）";
                                    }
                                    projectJson.put("buzhengtext",
                                            StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);// 补正材料
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                    // 4.1.5.4、正常办结且未评价（待评价状态）
                                    projectJson.put("currentstatus", "待评价");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                    // 4.1.5.5、正常办结且已评价（待评价状态）
                                    projectJson.put("currentstatus", "已办结");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_CXSQ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                                    // 4.1.5.6、不予受理、撤销申请、异常终止（其他状态）
                                    projectJson.put("currentstatus", "其他");
                                }
                                else {
                                    // 4.1.5.7、除去之前列出的状态外的办件（审批中状态）
                                    projectJson.put("currentstatus", "审批中");
                                }
                                projectJsonList.add(projectJson);
                            }
                        }
                        else if (ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(auditOnlineProject.getType())) {
                            // 4.2、如果是套餐，返回套餐的JSON数据
                            String biGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus());
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            JSONObject projectJson = new JSONObject();
                            // 4.2.1、获取套餐的基本信息拼接JSON
                            projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                            projectJson.put("projectname", auditOnlineProject.getProjectname());// 主题名称
                            projectJson.put("flowsn", auditOnlineProject.getFlowsn());// 套餐申报编号
                            projectJson.put("biguid", biGuid); // 主题实例标识
                            projectJson.put("projectguid", biGuid); // 主题实例标识
                            projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 主题标识
                            projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                            projectJson.put("type", auditOnlineProject.getType()); // 类型
                            // 0：普通办件
                            // 1：套餐
                            projectJson.put("projectstatus",
                                    ZwdtConstant.getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus())));// 办件状态
                            projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                    auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT));// 保存时间
                            projectJson.put("applydate", EpointDateUtil
                                    .convertDate2String(auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT));// 办件申请时间
                            // 4.2.2、获取套餐办结时间
                            String banJieData = "";
                            if (auditOnlineProject.getBanjiedate() != null) {
                                banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                        EpointDateUtil.DATE_FORMAT);
                            }
                            projectJson.put("banjiedate", banJieData);
                            // 4.2.3、获取套餐评价内容
                            String evaluate = "无评价";
                            AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                    .selectEvaluatByClientIdentifier(biGuid).getResult();
                            if (auditOnlineEvaluat != null
                                    && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
                                        auditOnlineEvaluat.getSatisfied());
                            }
                            projectJson.put("evaluate", evaluate);
                            // 4.2.4、获取业务标识、子申报标识、阶段标识
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            projectJson.put("yewuguid", auditSpInstance == null ? "" : auditSpInstance.getYewuguid());
                            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                    .getResult();
                            String subAppGuid = "";
                            if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
                                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                subAppGuid = auditSpISubapp.getRowguid();
                                projectJson.put("subappguid", auditSpISubapp.getRowguid());
                                projectJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                            }
                            // 4.2.5、获取套餐状态
                            if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                // 4.2.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                projectJson.put("currentstatus", "待提交");
                                projectJson.put("sparetime", "待提交");
                                projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                // 4.2.5.2、预审退回（预审退回状态）
                                projectJson.put("currentstatus", "预审退回");
                                projectJson.put("remarks", auditOnlineProject.getBackreason());// 套餐退回原因
                                projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                // 4.2.5.3、待补办、受理后待补办（预审退回状态）
                                projectJson.put("currentstatus", "待补正");
                                projectJson.put("sparetime", "待补正");

                                String buZhengText = "";// 补正材料名称
                                int buZhengCount = 0;// 补正材料数量
                                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                                        .getSpIPatchMaterialBySubappGuid(subAppGuid).getResult();
                                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                                    buZhengText += auditSpIMaterial.getMaterialname() + "、";
                                    buZhengCount++;
                                }
                                if (StringUtil.isNotBlank(buZhengText)) {
                                    buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                            + buZhengCount + "份材料）";
                                }
                                projectJson.put("buzhengtext",
                                        StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                // 4.2.5.4、正常办结且未评价（待评价状态）
                                projectJson.put("currentstatus", "待评价");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                // 4.2.5.5、正常办结且已评价（已办结状态）
                                projectJson.put("currentstatus", "已办结");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                // 4.2.5.6、待预审（外网申报已提交状态）
                                projectJson.put("currentstatus", "待预审");
                                projectJson.put("sparetime", "待预审");
                            }
                            else {
                                // 4.2.5.7、除去之前列出的状态外的办件（已办结状态）
                                projectJson.put("currentstatus", "审批中");
                                projectJson.put("sparetime", "审批中");
                            }
                            projectJsonList.add(projectJson);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("projectlist", projectJsonList);
                    dataJson.put("compamyid", compamyId);
                    dataJson.put("totalcount", totalCount);
                    dataJson.put("recentcount", recentcount);// 近期办件（目前默认显示近一个月）
                    log.info("=======结束调用getMyCompanyProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的企业办件成功", dataJson.toString());
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
            log.info("=======getMyCompanyProject异常信息：" + e.getMessage() + "=======");
            log.info("=======getMyCompanyProject接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的企业办件异常：" + e.getMessage(), "");
        }
    }

    /**
     * 申报办件获取用户所在企业列表接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getUserCompanyList", method = RequestMethod.POST)
    public String getCompanyList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getUserCompanyList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、根据用户guid获取法人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.eq("isactivated", "1");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    int companyCount = 0;
                    // 3.1.1、添加默认选择列，请选择
                    List<JSONObject> companyJsonList = new ArrayList<JSONObject>();
                    JSONObject exampleJson = new JSONObject();
                    exampleJson.put("companyguid", "");
                    exampleJson.put("companyname", "请选择");
                    companyJsonList.add(exampleJson);
                    // 3.2、列表加入是法人的公司列表
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("companyid", auditRsCompanyBaseinfo.getCompanyid());
                        dataJson.put("credit", auditRsCompanyBaseinfo.getCreditcode());
                        dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                        companyJsonList.add(dataJson);
                        companyCount += 1;
                    }
                    // 3.3、列表加入是管理者代办人的公司列表
                    SqlConditionUtil agentconditionUtil = new SqlConditionUtil();
                    agentconditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    agentconditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    agentconditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                    // 0-否，1-是
                    agentconditionUtil.eq("m_isactive", "1"); // 是否已激活： 1 已激活
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(agentconditionUtil.getMap()).getResult();
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("companyid", auditOnlineCompanyGrant.getCompanyid());
                        // 获取企业名称
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByCompanyId(auditOnlineCompanyGrant.getCompanyid()).getResult();
                        dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("credit", auditRsCompanyBaseinfo.getCreditcode());
                        dataJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                        companyJsonList.add(dataJson);
                        companyCount += 1;
                    }
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("companycount", companyCount);
                    dataJson.put("companylist", companyJsonList);
                    log.info("=======结束调用getUserCompanyList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办用户所在企业列表成功", dataJson.toString());
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
            log.info("=======getUserCompanyList接口参数：params【" + params + "】=======");
            log.info("=======getUserCompanyList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户所在企业列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的企业所属证照信息接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getMyCompanyCertInfo", method = RequestMethod.POST)
    public String getMyCompanyCertInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCompanyCertInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取企业标识
                String companyId = obj.getString("companyid");
                // 1.2、获取材料的标识
                String materialGuidsArr = obj.getString("materialarray");
                // 1.3、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.4、获取辖区
                String areaCode = obj.getString("areacode");
                // 2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByCompanyId(companyId).getResult();
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 4、获取证照类型：1 统一社会信用代码（默认） 2 组织机构代码证
                    String certtype = "";
                    String centnum = "";
                    // 4.1、获取统一信用代码
                    if (auditRsCompanyBaseinfo != null) {
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                            centnum = auditRsCompanyBaseinfo.getCreditcode();
                            certtype = ZwfwConstant.CERT_TYPE_TYSHXYDM;
                        }
                        else {
                            // 4.2、获取组织机构代码
                            if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                                certtype = ZwfwConstant.CERT_TYPE_ZZJGDMZ;
                                centnum = auditRsCompanyBaseinfo.getOrgancode();
                            }
                            else {
                                return JsonUtils.zwdtRestReturn("0", "获取企业信息异常！", "");
                            }
                        }
                    }
                    // 5、企业证照列表
                    List<JSONObject> companycertlist = new ArrayList<JSONObject>();
                    dataJson.put("certtype", certtype);
                    dataJson.put("centnum", centnum);
                    if (auditRsCompanyBaseinfo != null) {
                        dataJson.put("name", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("organlegal",
                                auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getOrganlegal());
                        dataJson.put("address",
                                auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getRegisteraddress());
                        dataJson.put("companyrowguid",
                                auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getRowguid());
                        dataJson.put("orgalegalidnumber",
                                auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                    }
                    // 6、获取企业对应的法人信息
                    AuditRsCompanyLegal auditRsCompanyLegal = iAuditRsCompanyLegal
                            .getAuditRsCompanyLegalByCompanyid(companyId).getResult();
                    dataJson.put("contactpostcode",
                            auditRsCompanyLegal == null ? "" : auditRsCompanyLegal.getContactpostcode());
                    dataJson.put("contactemail",
                            auditRsCompanyLegal == null ? "" : auditRsCompanyLegal.getContactemail());
                    dataJson.put("contactfax", auditRsCompanyLegal == null ? "" : auditRsCompanyLegal.getContactfax());

                    // 加入判断该办件是否需要关联电子表单：1.建筑企业资质简单变更 2、成品油变更

                    AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectGuid);

                    if (auditProject != null) {
                        String projectname = auditProject.getProjectname();
                        /*
                         * if (projectname.contains("建筑业企业资质（简单变更）（企业名称变更）") ||
                         * projectname.contains("建筑业企业资质（简单变更）（法人变更）") ||
                         * projectname.contains("建筑业企业资质（简单变更）（注册资本变更）") ||
                         * projectname.contains("建筑业企业资质（简单变更）（多项变更）") ||
                         * projectname.contains("建筑业企业资质（简单变更）（经济性质变更）") ||
                         * projectname.contains("建筑业企业资质（简单变更）（企业地址变更）")) {
                         * dataJson.put("jnaisp", "1"); } else
                         */
                        if (projectname.contains("变更成品油零售经营批准证书企业名称或法定代表人")
                                || projectname.contains("变更成品油零售经营批准证书企业名称和法定代表人")) {
                            dataJson.put("jnaisp", "2");
                        }
                        // else if(projectname.contains("建设工程质量检测机构资质审批")) {
                        // dataJson.put("jnaisp", "3");
                        // }
                        else if (projectname.contains("房地产开发企业暂定资质变更") || projectname.contains("房地产开发企业四级资质变更")
                                || projectname.contains("房地产开发企业三级资质变更")
                                || projectname.contains("房地产开发企业二级资质变更（多项变更）")) {
                            dataJson.put("jnaisp", "4");
                        }
                        else if (projectname.contains("统一社会信用代码信息服务")) {
                            dataJson.put("jnaisp", "5");
                        }
                        else if (projectname.contains("粮食收购资格备案")) {
                            dataJson.put("jnaisp", "6");
                        }
                        else if (projectname.contains("乡村兽医备案")) {
                            dataJson.put("jnaisp", "6");
                        }
                    }
                    // 7、获取企业相关的共享材料信息
                    if (StringUtil.isNotBlank(materialGuidsArr)) {
                        // 7.1、将传递的材料标识的字符串首尾的[]去除，然后组合成数组
                        materialGuidsArr = materialGuidsArr.replace("[", "").replace("]", "");
                        String[] materialGuids = materialGuidsArr.split(","); // 材料标识数组
                        int materialCount = materialGuids.length;
                        // 7.2、遍历页面上的材料列表，判断材料是否为共享材料
                        for (int i = 0; i < materialCount; i++) {
                            JSONObject companyCertJson = new JSONObject();
                            String materialGuid = materialGuids[i];
                            materialGuid = materialGuid.replaceAll("\"", "");
                            // 7.3、获取办件材料信息
                            AuditProjectMaterial auditProjectMaterial = iAuditProjectMaterial
                                    .selectProjectMaterialByMaterialGuid(projectGuid, materialGuid).getResult();
                            if (auditProjectMaterial != null) {
                                // 7.4、获取事项材料信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(materialGuid).getResult();
                                if (auditTaskMaterial != null) {
                                    // 7.4.1、判断是否为共享材料
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getSharematerialguid())) {
                                        String materialLevel = "";
                                        String materialType = "";
                                        // 7.4.2、取出所有证照实例信息
                                        // 7.4.2.1、将组织机构代码证与统一社会信用代码取出来放到一个list里面
                                        List<JSONObject> idList = new ArrayList<JSONObject>();
                                        if (auditRsCompanyBaseinfo != null) {
                                            JSONObject creditJson = new JSONObject();
                                            creditJson.put("idnumber", auditRsCompanyBaseinfo.getCreditcode());
                                            idList.add(creditJson);
                                            JSONObject organjson = new JSONObject();
                                            organjson.put("idnumber", auditRsCompanyBaseinfo.getOrgancode());
                                            idList.add(organjson);
                                        }
                                        // 7.4.2.2、根据组织机构代码和统一社会信用代码取出所以的证照实例信息
                                        List<CertInfo> certInfoList = new ArrayList<CertInfo>();
                                        List<CertInfo> infoList = null;
                                        if (idList != null && idList.size() > 0) {
                                            for (int j = 0; j < idList.size(); j++) {
                                                String idNumber = idList.get(j).getString("idnumber");
                                                infoList = iCertInfoExternal.selectCertByOwner(
                                                        auditTaskMaterial.getSharematerialguid(),
                                                        ZwdtConstant.CERTOWNERTYPE_COMPANY, "", idNumber, false,
                                                        areaCode, null);
                                                certInfoList.addAll(infoList);
                                            }
                                        }
                                        // 7.4.3、将证照实例关联上共享材料
                                        if (certInfoList != null && certInfoList.size() > 0) {
                                            CertInfo certInfo = certInfoList.get(0);
                                            if (certInfo != null) {
                                                // 7.4.3.1、如果材料是证照，则返回证照级别
                                                materialType = certInfo.getMaterialtype();
                                                if (ZwdtConstant.MATERIALTYPE_CERT.equals(materialType)) {
                                                    materialLevel = certInfo.getCertlevel();
                                                }
                                                // 7.4.3.2、如果材料原先有对应的附件,把原先的附件删除
                                                iAttachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                                auditProjectMaterial.setSharematerialiguid(certInfo.getRowguid());
                                                auditProjectMaterial.setCertinfoinstanceguid(certInfo.getRowguid());
                                                // 7.4.3.3、取出证照实例关联的附件信息
                                                List<JSONObject> attachList = iCertAttachExternal
                                                        .getAttachList(certInfo.getCertcliengguid(), areaCode);
                                                if (attachList != null && attachList.size() > 0) {
                                                    for (JSONObject json : attachList) {
                                                        // 7.4.3.3.1、独立证照库情况下，将附件存储到本地附件库中。本地模式下，实现附件复制
                                                        AttachUtil.saveFileInputStream(UUID.randomUUID().toString(),
                                                                auditProjectMaterial.getCliengguid(),
                                                                json.getString("attachname"),
                                                                json.getString("contentype"),
                                                                json.getString("cliengtag"), json.getLong("size"),
                                                                iCertAttachExternal.getAttach(
                                                                        json.getString("attachguid"), areaCode),
                                                                null, null);
                                                    }
                                                    // 7.4.3.3.2、更新流程材料状态
                                                    auditProjectMaterial
                                                            .setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                                                }
                                            }
                                        }
                                        else {
                                            // 7.4.4、不存在证照实例
                                            // 7.4.4.1、如果材料原先有对应的附件,把原先的附件删除
                                            iAttachService.deleteAttachByGuid(auditProjectMaterial.getCliengguid());
                                            // 7.4.4.2、未关联证照实例，去除sharematerialguid字段
                                            auditProjectMaterial.remove("sharematerialiguid");
                                        }
                                        // 7.4.5、更新办件材料
                                        iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                                        auditProjectMaterial.set("materialLevel", materialLevel);
                                        auditProjectMaterial.set("materialType", materialType);
                                    }
                                }
                                // 7.6、显示按钮标识（0:未上传、1：已上传、2：已引用证照库、3:
                                // 在线填表、4:已填表）以及是否需要更新证照库
                                String bigshowtype = auditProjectMaterial.getStr("companyshow");
                                String clientGuid = "";
                                if (StringUtil.isNotBlank(bigshowtype)) {
                                    List<FrameAttachInfo> attachinfos = iJnProjectService
                                            .getFrameAttachByIdenumberBigType(centnum, bigshowtype);
                                    if (attachinfos != null && attachinfos.size() > 0) {
                                        clientGuid = attachinfos.get(0).getCliengGuid();
                                        log.info("获取到对应的大数据证照信息：" + clientGuid);
                                    }
                                    else {
                                        clientGuid = auditProjectMaterial.getCliengguid();
                                    }
                                }
                                else {
                                    clientGuid = auditProjectMaterial.getCliengguid();
                                }
                                int count = iAttachService.getAttachCountByClientGuid(clientGuid); // 获取办件材料对应的附件数量
                                int showButton = 0; // 按钮显示方式
                                String needLoad = ZwdtConstant.CERTLEVEL_C
                                        .equals(auditProjectMaterial.get("materialLevel")) ? "1" : "0"; // 是否需要上传到证照库
                                // 0:不需要
                                // 、1:需要
                                if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                    // 7.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                    if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                        // 7.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                        showButton = count > 0 ? 4 : 3;
                                    }
                                    else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                        // 7.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                        showButton = count > 0 ? 1 : 0;
                                    }
                                }
                                else {
                                    // 7.6.2、如果关联了证照库
                                    if (count > 0) {
                                        // 7.6.2.1、获取材料类别
                                        String materialType = auditProjectMaterial.get("materialType");
                                        if (Integer.parseInt(materialType) == 1) {
                                            // 7.6.2.1.1、已引用证照库
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                        }
                                        else if (Integer.parseInt(materialType) == 2) {
                                            // 7.6.2.1.2、已引用批文
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                        }
                                        else {
                                            // 7.6.2.1.3、已引用材料
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                        }
                                    }
                                    else {
                                        // 7.6.2.2、如果没有附件，则标识为未上传
                                        showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                    }
                                }
                                companyCertJson.put("cliengguid", clientGuid);
                                companyCertJson.put("showbutton", showButton);
                                companyCertJson.put("needLoad", needLoad);
                                companyCertJson.put("sharematerialiguid",
                                        StringUtil.isNotBlank(auditProjectMaterial.getSharematerialiguid())
                                                ? auditProjectMaterial.getSharematerialiguid()
                                                : "");// 办件材料关联共享材料实例标识
                                companyCertJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                                companycertlist.add(companyCertJson);
                            }
                        }
                    }
                    // 8、返回企业共享材料相关信息
                    dataJson.put("companycertlist", companycertlist);
                    log.info("=======结束调用getMyCompanyCertInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办用户所在企业列表成功", dataJson.toString());
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
            log.info("=======getMyCompanyCertInfo接口参数：params【" + params + "】=======");
            log.info("=======getMyCompanyCertInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的企业所属证照信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件材料信息的接口（继续申报至办件申报页面调用、办件详细页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getInitMaterialList", method = RequestMethod.POST)
    public String getInitMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getInitMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            // 获取用户注册信息
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 判断当前登录人是否与办件人相同
                // 获取用户信息
                AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                        .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                SqlConditionUtil sqlCondition = new SqlConditionUtil();
                sqlCondition.eq("SOURCEGUID", projectGuid);
                AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                        .getAuditOnlineProjectPageData(sqlCondition.getMap(), 0, 10, null, null).getResult().getList()
                        .get(0);
                String applyertype = auditOnlineProject.getApplyertype();
                if (auditOnlineProject != null) {
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyertype)) {
                        if (!auditOnlineIndividual.getApplyerguid().equals(auditOnlineProject.getApplyerguid())) {
                            return JsonUtils.zwdtRestReturn("0", "获取办件信息失败,登录人信息与所要访问的办件信息不匹配！", "");
                        }
                    }
                    else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyertype)) {
                        // 根据法人身份证查询企业列表
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("orgalegal_idnumber", auditOnlineIndividual.getIdnumber());
                        sql.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                        sql.eq("isactivated", ZwfwConstant.CONSTANT_STR_ONE); // 激活的企业
                        List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                                .selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
                        // 如果企业列表里存在所登陆的企业，就将sign设置为false，不执行return语句
                        boolean sign = true;
                        for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                            if (auditOnlineProject.getApplyerguid().equals(auditRsCompanyBaseinfo.getCompanyid())) {
                                sign = false;
                            }
                        }
                        // 如果登录人是代办人
                        SqlConditionUtil sqlCondite = new SqlConditionUtil();
                        sqlCondite.eq("bsquserguid", auditOnlineIndividual.getAccountguid());
                        sqlCondite.isBlankOrValue("isrelieve", "0"); // 是否解除授权
                        List<AuditOnlineCompanyGrant> grants = iAuditOnlineCompanyGrant
                                .selectCompanyGrantByConditionMap(sqlCondite.getMap()).getResult();
                        for (AuditOnlineCompanyGrant grant : grants) {
                            if (auditOnlineProject.getApplyerguid().equals(grant.getCompanyid())) {
                                sign = false;
                            }
                        }
                        if (sign) {
                            return JsonUtils.zwdtRestReturn("0", "获取办件信息失败,登录人信息与所要访问的办件信息不匹配！", "");
                        }

                    }
                }
                // 1.2、获取所要获取材料的类型（0:未提交的材料、1：需要补正材料、2:原件提交、空:办件所有材料）
                String type = obj.getString("type");
                // 1.3、获取辖区编码
                // String areaCode = obj.getString("areacode");
                // 1.4、获取事项多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                String hasventilationsystem = obj.getString("hasventilationsystem");
                // 2、拼接办件材料条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", projectGuid);
                if ("0".equals(type)) {
                    // 2.1、材料提交状态：未提交
                    sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_WTJ);
                }
                else if ("1".equals(type)) {
                    // 2.2、材料提交状态：待补正
                    sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                }
                // 3、获取办件的材料
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                        // 3.1、如果事项配置了材料多情形情况，获取该情形下的材料列表
                        List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                                .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                        for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                            if (auditProjectMaterial.getTaskmaterialguid()
                                    .equals(auditTaskMaterialCase.getMaterialguid())) {
                                // 3.1.1、获取多情形中配置的材料基本信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    JSONObject materialJson = new JSONObject();
                                    // 3.1.2、获取办件材料基本信息
                                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                    materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                    materialJson.put("status", auditProjectMaterial.getStatus()); // 办件材料提交状态
                                    materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                                    materialJson.put("sharematerialiguid",
                                            auditProjectMaterial.getSharematerialiguid());// 材料关联的共享材料标识
                                    materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                    materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                                    int necessity = auditTaskMaterialCase.getNecessity(); // 材料是否必需
                                    if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                        materialJson.put("necessary", "0");
                                    }
                                    else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                        materialJson.put("necessary", "1");
                                    }
                                    // 3.1.3、获取材料提交方式
                                    materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                            String.valueOf(auditTaskMaterial.getSubmittype())));
                                    // 3.1.4、获取材料来源渠道
                                    String materialsource;
                                    if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                        materialsource = "申请人自备";
                                    }
                                    else {
                                        materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                auditTaskMaterial.getFile_source());
                                    }
                                    materialJson.put("materialsource",
                                            StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource); // 材料来源渠道
                                    // 3.1.5、获取材料空白表格标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                        int templateAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                        if (templateAttachCount > 0) {
                                            String templateattachguid = iAttachService
                                                    .getAttachListByGuid(auditTaskMaterial.getTemplateattachguid())
                                                    .get(0).getAttachGuid();
                                            materialJson.put("templateattachguid", templateattachguid);
                                        }
                                    }
                                    // 3.1.6、获取材料填报示例标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                        int exampleAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                        if (exampleAttachCount > 0) {
                                            String exampleattachguid = iAttachService
                                                    .getAttachListByGuid(auditTaskMaterial.getExampleattachguid())
                                                    .get(0).getAttachGuid();
                                            materialJson.put("exampleattachguid", exampleattachguid);
                                        }
                                    }
                                    // 3.1.7、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                                    // 在线填表、4:已填表）
                                    int count = iAttachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                                    int showButton = 0; // 按钮显示方式
                                    if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                        // 3.1.7.1、如果没有从证照库引用附件，则为普通附件及填表
                                        if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                            // 3.1.7.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                            showButton = count > 0 ? 4 : 3;
                                        }
                                        else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                            // 3.1.7.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                            showButton = count > 0 ? 1 : 0;
                                        }
                                    }
                                    else {
                                        // 3.1.7.2、如果从证照库引用
                                        if (count > 0) {
                                            // 3.1.7.2.1、获取材料所引用的证照实例
                                            CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(
                                                    auditProjectMaterial.getCertinfoinstanceguid());
                                            if (certInfo != null) {
                                                // 3.1.7.2.2、获取证照实例对应的附件数量
                                                List<JSONObject> rsInfos = iCertAttachExternal.getAttachList(
                                                        certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                                int countRs = 0;
                                                if (rsInfos != null && rsInfos.size() > 0) {
                                                    countRs = rsInfos.size();
                                                }
                                                // 3.1.7.2.3、数量不一致
                                                if (count != countRs) {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                }
                                                // 3.1.7.2.4、数量一致的情况
                                                else {
                                                    String cliengTag = "";
                                                    List<FrameAttachInfo> projectInfos = iAttachService
                                                            .getAttachInfoListByGuid(
                                                                    auditProjectMaterial.getCliengguid());
                                                    projectInfos.sort((a, b) -> a.getAttachLength()
                                                            .compareTo(b.getAttachLength()));
                                                    if (rsInfos != null && rsInfos.size() > 0) {
                                                        rsInfos.sort((a, b) -> a.getLong("size")
                                                                .compareTo(b.getLong("size")));
                                                        for (int i = 0; i < rsInfos.size(); i++) {
                                                            // 3.1.7.2.4.1、大小不同说明附件改动过
                                                            cliengTag = projectInfos.get(i).getCliengTag();
                                                            if ((StringUtil.isBlank(cliengTag)
                                                                    || "本地上传".equals(cliengTag)
                                                                    || "云盘上传".equals(cliengTag)
                                                                    || "手机上传".equals(cliengTag))
                                                                    && rsInfos.get(i).getLong("size")
                                                                            .longValue() != projectInfos.get(i)
                                                                                    .getAttachLength().longValue()) {
                                                                showButton = Integer.parseInt(
                                                                        ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                                break;
                                                            }
                                                            else {
                                                                // 3.1.7.2.4.2、若文件未发生改变，则按钮显示维持原样
                                                                if (StringUtil.isNotBlank(certInfo.getMaterialtype())) {
                                                                    String materialType = certInfo.getMaterialtype();
                                                                    if (Integer.parseInt(materialType) == 1) {
                                                                        showButton = Integer.parseInt(
                                                                                ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                                    }
                                                                    else if (Integer.parseInt(materialType) == 2) {
                                                                        showButton = Integer.parseInt(
                                                                                ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                                    }
                                                                    else {
                                                                        showButton = Integer.parseInt(
                                                                                ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            // 3.1.7.3、如果没有附件，则标识为未上传
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                        }
                                    }
                                    String isxtendfield = auditTaskMaterial.getStr("isxtendfield");

                                    if ("1".equals(isxtendfield)) {
                                        materialJson.put("materialsource", "已关联电子证照，可免提交");
                                        if (showButton == 0) {
                                            showButton = 9;
                                        }
                                    }

                                    materialJson.put("shareMid", auditProjectMaterial.getSharematerialiguid());
                                    materialJson.put("showbutton", showButton);
                                    materialJsonList.add(materialJson);
                                }
                            }
                        }
                    }
                    else {
                        // 4.2、如果事项配置了材料没有配置多情形情况
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {

                            if ("使用集中空调通风系统的，需提供集中空调通风系统卫生检测或者评价报告（由有资质认定的检验机构提供）"
                                    .equals(auditTaskMaterial.getMaterialname())) {
                                if ("有无无".equals(hasventilationsystem)) {
                                    continue;
                                }
                            }

                            JSONObject materialJson = new JSONObject();
                            // 4.2.1、获取办件材料基本信息
                            materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                            materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                            materialJson.put("status", auditProjectMaterial.getStatus()); // 办件材料提交状态
                            materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                            materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid());// 材料关联的共享材料标识
                            materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                            materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                            int necessity = auditTaskMaterial.getNecessity(); // 材料是否必需
                            if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                materialJson.put("necessary", "0");
                            }
                            else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                materialJson.put("necessary", "1");
                            }
                            // 4.2.2、获取材料提交方式
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditTaskMaterial.getSubmittype())));
                            // 4.2.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource); // 材料来源渠道
                            // 4.2.4、获取材料空白表格标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachAcount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachAcount > 0) {
                                    String templateattachguid = iAttachService
                                            .getAttachListByGuid(auditTaskMaterial.getTemplateattachguid()).get(0)
                                            .getAttachGuid();
                                    materialJson.put("templateattachguid", templateattachguid);
                                }
                            }
                            // 4.2.5、获取材料填报示例标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachAcount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachAcount > 0) {
                                    String exampleattachguid = iAttachService
                                            .getAttachListByGuid(auditTaskMaterial.getExampleattachguid()).get(0)
                                            .getAttachGuid();
                                    materialJson.put("exampleattachguid", exampleattachguid);
                                }
                            }
                            // 4.2.6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton = 0; // 按钮显示方式
                            if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                // 4.2.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.2.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                    // 4.2.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.2.6.2、如果从证照库引用
                                if (count > 0) {
                                    // 4.2.6.2.1、获取材料所引用的证照实例
                                    CertInfo certInfo = iCertInfoExternal
                                            .getCertInfoByRowguid(auditProjectMaterial.getCertinfoinstanceguid());
                                    if (certInfo != null) {
                                        // 4.2.6.2.2、获取证照实例对应的附件数量
                                        List<JSONObject> rsInfos = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        int countRs = 0;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            countRs = rsInfos.size();
                                        }
                                        // 4.2.6.2.3、数量不一致
                                        if (count != countRs) {
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                        }
                                        // 4.2.6.2.4、数量一致的情况
                                        else {
                                            String cliengTag = "";
                                            List<FrameAttachInfo> projectInfos = iAttachService
                                                    .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                                            projectInfos
                                                    .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                            if (rsInfos != null && rsInfos.size() > 0) {
                                                rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                                for (int i = 0; i < rsInfos.size(); i++) {
                                                    // 4.2.6.2.4.1、大小不同说明附件改动过
                                                    cliengTag = projectInfos.get(i).getCliengTag();
                                                    if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                            || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                            && rsInfos.get(i).getLong("size")
                                                                    .longValue() != projectInfos.get(i)
                                                                            .getAttachLength().longValue()) {
                                                        showButton = Integer
                                                                .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                        break;
                                                    }
                                                    else {
                                                        // 4.2.6.2.4.2、若文件未发生改变，则按钮显示维持原样
                                                        if (StringUtil.isNotBlank(certInfo.getMaterialtype())) {
                                                            String materialType = certInfo.getMaterialtype();
                                                            if (Integer.parseInt(materialType) == 1) {
                                                                showButton = Integer.parseInt(
                                                                        ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                            }
                                                            else if (Integer.parseInt(materialType) == 2) {
                                                                showButton = Integer.parseInt(
                                                                        ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                            }
                                                            else {
                                                                showButton = Integer.parseInt(
                                                                        ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                                else {
                                    // 4.2.6.3、如果没有附件，则标识为未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                }
                            }
                            materialJson.put("showbutton", showButton);
                            materialJsonList.add(materialJson);
                        }
                    }
                }
                // 5、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 5.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 5.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                            Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getInitMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getInitMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getInitMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查补正材料是否都提交的接口（补正详细页面提交材料前调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkBZMaterialIsSubmit", method = RequestMethod.POST)
    public String checkBZMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkBZMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料信息
                String materialList = obj.getString("materiallist");
                int noSubmitNum = 0;// 补正的材料没有提交的个数
                // 2、将材料信息转换为弱类型的实体集合
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    // 2.1、获取办件材料的提交状态
                    String status = record.getStr("status");
                    // 2.2、获取事项材料标识
                    String taskMaterialGuid = record.getStr("taskmaterialguid");
                    // 2.3、获取材料信息
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(taskMaterialGuid).getResult();
                    if (auditTaskMaterial != null) {
                        // 2.4、根据材料的提交方式和提交状态判断是否需要提交
                        int submitType = Integer.parseInt(auditTaskMaterial.getSubmittype());
                        int intStatus = Integer.parseInt(status);
                        if (submitType == WorkflowKeyNames9.SubmitType_Submit) {
                            // 2.4.1、如果材料的提交方式是电子，则状态为非电子获取非电子和纸质均认为未提交
                            if (intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                noSubmitNum++;
                            }
                        }
                        else if (submitType == WorkflowKeyNames9.SubmitType_Submit_And_PaperSubmit) {
                            // 2.4.2、如果材料的提交方式是电子和，则状态为非电子获取非电子和纸质均认为未提交
                            if (intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC
                                    && intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC) {
                                noSubmitNum++;
                            }
                        }
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkBZMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查补正材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkBZMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkBZMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查补正材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交补正材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取提交材料列表信息
                String materialList = obj.getString("materiallist");
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    String projectMaterialGuid = record.getStr("projectmaterialguid"); // 办件材料标识
                    // 2、更新办件材料审核状态为已补正
                    iAuditProjectMaterial.updateProjectMaterialAuditStatus(projectMaterialGuid,
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), projectGuid);
                }
                // 3定义返回JSON对象
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交材料成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitMaterial接口参数：params【" + params + "】=======");
            log.info("=======submitMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 云盘文件选择接口（办件申报云盘选择调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/private/submitMaterialsByCloud", method = RequestMethod.POST)
    public String submitMaterialsByCloud(@RequestBody String params) {
        try {
            log.info("=======开始调用submitMaterialsByCloud接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取云盘中已选文件的标识
                String selectedGuids = obj.getString("curguids");
                // 1.2、获取办件材料对应的附件标识
                String clientGuid = obj.getString("clientguid");
                String[] selectedGuidsArr = null; // 云盘中已选文件标识数组
                int size = 0;
                if (StringUtil.isNotBlank(selectedGuids)) {
                    selectedGuids = selectedGuids.replace("[", "").replace("]", "");
                    selectedGuidsArr = selectedGuids.split(",");
                    size = selectedGuidsArr.length;
                }
                // 2、需要清除原先从云盘选择并且关联到材料的附件
                List<FrameAttachInfo> frameAttachInfolist = iAttachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : frameAttachInfolist) {
                    if ("云盘上传".equals(frameAttachInfo.getCliengTag())) {
                        iAttachService.deleteAttach(frameAttachInfo);
                    }
                }
                // 3、将已选择的云盘文件对应的附件关联到办件材料
                for (int i = 0; i < size; i++) {
                    String selectedGuid = selectedGuidsArr[i];
                    selectedGuid = selectedGuid.replaceAll("\"", "");
                    // 3.1、获取已选的云盘文件
                    AuditOnlineCloudFiles auditOnlineCloudFiles = iAuditOnlineCloudFiles
                            .getAuditOnlineCloudFilesByRowguid(selectedGuid);
                    if (auditOnlineCloudFiles != null) {
                        // 3.2、将已选的云盘文件关联到材料附件，需要复制云盘文件对应的附件进行关联（云盘文件clientGuid关联了attachGuid）
                        FrameAttachInfo frameAttachInfoCloudFiles = iAttachService
                                .getAttachInfoDetail(auditOnlineCloudFiles.getClientguid());
                        if (frameAttachInfoCloudFiles != null) {
                            // 3.3、复制云盘文件
                            iAttachService.copyAttachByClientGuid(frameAttachInfoCloudFiles.getCliengGuid(), "云盘上传",
                                    auditOnlineCloudFiles.getClientguid(), clientGuid);
                            List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                            // 3.4、更新附件上传时间
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                frameAttachInfo.setUploadDateTime(new Date());
                                iAttachService.updateAttach(frameAttachInfo, null);
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用submitMaterialsByCloud接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交云盘选中材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitMaterialsByCloud接口参数：params【" + params + "】=======");
            log.info("=======submitMaterialsByCloud异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交云盘选中材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取已提交材料列表接口（办件详细页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getSubmitMaterialList", method = RequestMethod.POST)
    public String getSubmitMaterialList(@RequestBody String params) {
        try {
            log.info("=======开始调用getSubmitMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取事项材料情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、获取事项标识
                // String taskGuid = obj.getString("taskguid");
                // 2、获取已提交的材料数据（排除未提交状态的办件材料）
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", projectGuid);
                sqlConditionUtil.nq("status", String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT));
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    // 2.1、获取事项材料信息
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    if (auditTaskMaterial != null) {
                        JSONObject materialJson = new JSONObject();
                        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid()); // 办件材料标识
                        materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial()); // 办件材料名称
                        int necessary = auditTaskMaterial.getNecessity(); // 是否必要(默认设置为事项材料的必要性)
                        if (StringUtil.isNotBlank(taskCaseGuid)) {
                            // 2.1.1、如果事项材料存在多情形则需要获取多情形中材料的必要性
                            AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                    .selectTaskMaterialCaseByGuid(taskCaseGuid,
                                            auditProjectMaterial.getTaskmaterialguid())
                                    .getResult();
                            necessary = auditTaskMaterialCase.getNecessity();
                        }
                        materialJson.put("necessary",
                                necessary == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES) ? 1 : 0); // 是否必要
                        materialJson.put("ordernumber", auditTaskMaterial.getOrdernum()); // 排序
                        String materialsource;
                        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                            materialsource = "申请人自备";
                        }
                        else {
                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                    auditTaskMaterial.getFile_source());
                        }
                        materialJson.put("materialsource",
                                StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 材料来源
                        materialJsonList.add(materialJson);
                    }
                }
                // 3、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 3.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 3.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernumber") == null ? 0 : (Integer) b1.get("ordernumber");
                            Integer ordernumb2 = b2.get("ordernumber") == null ? 0 : (Integer) b2.get("ordernumber");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                dataJson.put("materialcount", materialJsonList.size());
                log.info("=======结束调用getSubmitMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取已提交材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSubmitMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getSubmitMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取已提交材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件中所有纸质材料接口（申报告知页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getPaperMaterial", method = RequestMethod.POST)
    public String getPaperMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPaperMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                // String areaCode = obj.getString("areacode");
                // 1.3、获取事项材料多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 2、如果存在事项材料配置了多情形，则需要获取情形与材料对应关系
                List<AuditTaskMaterialCase> auditTaskMaterialCases = null;
                if (StringUtil.isNotBlank(taskCaseGuid)) {
                    auditTaskMaterialCases = iAuditTaskMaterialCase.selectTaskMaterialCaseByCaseGuid(taskCaseGuid)
                            .getResult();
                }
                // 2、获取办件材料信息
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterial(projectGuid).getResult();
                List<JSONObject> materialJsonlist = new ArrayList<JSONObject>();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                        // 2.1、如果是配置了多情形的情况，需要展示多情形对应的纸质材料数据
                        for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                            if (auditProjectMaterial.getTaskmaterialguid()
                                    .equals(auditTaskMaterialCase.getMaterialguid())) {
                                // 2.1.2、获取事项材料信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    String submitType = auditTaskMaterial.getSubmittype();
                                    // 2.1.3、需要返回的是提交方式包含纸质方式的材料
                                    if (!String.valueOf(WorkflowKeyNames9.SubmitType_Submit).equals(submitType)) {
                                        JSONObject materialJson = new JSONObject();
                                        // 2.1.4、获取办件材料基本信息
                                        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                        materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                        materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                        materialJson.put("sharematerialiguid",
                                                auditProjectMaterial.getSharematerialiguid()); // 办件材料关联共享材料标识
                                        materialJson.put("status", auditProjectMaterial.getStatus()); // 材料提交状态
                                        materialJson.put("type", auditTaskMaterial.getType()); // 材料类型
                                        materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                                String.valueOf(auditTaskMaterial.getSubmittype()))); // 提交方式
                                        materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                        // 2.1.5、获取材料来源渠道
                                        String materialsource;
                                        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                            materialsource = "申请人自备";
                                        }
                                        else {
                                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                    auditTaskMaterial.getFile_source());
                                        }
                                        materialJson.put("materialsource",
                                                StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 来源渠道
                                        // 2.1.6、获取材料必要性
                                        int necessity = auditTaskMaterialCase.getNecessity();
                                        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                            materialJson.put("necessary", "0");
                                        }
                                        else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                            materialJson.put("necessary", "1");
                                        }
                                        // 2.1.6、获取材料空白表格附件标识
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                            int templateAttachCount = iAttachService.getAttachCountByClientGuid(
                                                    auditTaskMaterial.getTemplateattachguid());
                                            if (templateAttachCount > 0) {
                                                materialJson.put("templateattachguid",
                                                        auditTaskMaterial.getTemplateattachguid());
                                            }
                                        }
                                        // 2.1.7、获取材料填报示例附件标识
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                            int exampleAttachCount = iAttachService.getAttachCountByClientGuid(
                                                    auditTaskMaterial.getExampleattachguid());
                                            if (exampleAttachCount > 0) {
                                                materialJson.put("exampleattachguid",
                                                        auditTaskMaterial.getExampleattachguid());
                                            }
                                        }
                                        materialJsonlist.add(materialJson);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        // 2.2、如果是没有配置多情形的情况下，需要展示办件材料中的包含纸质的材料
                        // 2.2.2、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            String submitType = auditTaskMaterial.getSubmittype();
                            // 2.2.3、需要返回的是提交方式包含纸质方式的材料
                            if (!String.valueOf(WorkflowKeyNames9.SubmitType_Submit).equals(submitType)) {
                                JSONObject materialJson = new JSONObject();
                                // 2.2.4、获取材料基本信息
                                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid()); // 办件材料关联共享材料标识
                                materialJson.put("status", auditProjectMaterial.getStatus()); // 材料提交状态
                                materialJson.put("type", auditTaskMaterial.getType()); // 材料类型
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype()))); // 提交方式
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                // 2.2.5、获取材料来源渠道
                                String materialsource;
                                if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialsource = "申请人自备";
                                }
                                else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource",
                                        StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 来源渠道
                                // 2.2.6、获取材料必要性
                                int necessity = auditTaskMaterial.getNecessity();
                                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                    materialJson.put("necessary", "0");
                                }
                                else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                    materialJson.put("necessary", "1");
                                }
                                // 2.2.7、获取材料空白模板附件标识
                                String templateAttachGuid = "";
                                if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                    List<FrameAttachInfo> tempFrameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
                                    if (tempFrameAttachInfos != null && tempFrameAttachInfos.size() > 0) {
                                        templateAttachGuid = tempFrameAttachInfos.get(0).getAttachGuid();
                                    }
                                }
                                materialJson.put("templateattachguid", templateAttachGuid);
                                // 2.2.8、获取材料填报示例附件标识
                                String exampleAttachGuid = "";
                                if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                    List<FrameAttachInfo> examFrameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
                                    if (examFrameAttachInfos != null && examFrameAttachInfos.size() > 0) {
                                        exampleAttachGuid = examFrameAttachInfos.get(0).getAttachGuid();
                                    }
                                }
                                materialJson.put("exampleattachguid", exampleAttachGuid);
                                materialJsonlist.add(materialJson);
                            }
                        }
                    }
                }
                // 3、对返回的材料列表进行排序
                Collections.sort(materialJsonlist, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 3.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 3.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                            Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonlist);
                dataJson.put("projectguid", projectGuid);// 办件标识
                log.info("=======结束调用getPaperMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取纸质材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPaperMaterial接口参数：params【" + params + "】=======");
            log.info("=======getPaperMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取纸质材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 办件公示的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    /*
     * @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
     * public String getProjectList(@RequestBody String params) { try {
     * log.info("=======开始调用getProjectList接口======="); // 1、接口的入参转化为JSON对象
     * JSONObject jsonObject = JSONObject.parseObject(params); String token =
     * jsonObject.getString("token"); if
     * (ZwdtConstant.SysValidateData.equals(token)) { JSONObject obj =
     * (JSONObject) jsonObject.get("params"); // 1.1、获取搜索关键字（办件流水号、申请人姓名） String
     * keyWord = obj.getString("keyword"); // 1.2、获取当前页码 String currentPage =
     * obj.getString("currentpage"); // 1.3、获取每页显示数量 String pageSize =
     * obj.getString("pagesize"); // 1.4、获取区域编码 String areacode =
     * obj.getString("areacode"); // 1.5、获取事项标识 String taskguid =
     * obj.getString("taskguid"); // 2、获取公示需要展示的办件 SqlConditionUtil
     * sqlConditionUtil = new SqlConditionUtil();
     * sqlConditionUtil.eq("areacode", areacode); if
     * (StringUtil.isNotBlank(taskguid)) { sqlConditionUtil.eq("taskguid",
     * taskguid); } //
     * 2.1、办件状态：外网申报已提交\外网申报预审退回\外网申报预审通过\待接件\已接件\待补办\已受理\审批不通过\审批通过\正常办结\不予受理\
     * 撤销申请\异常终止） sqlConditionUtil.in("status",
     * ZwdtConstant.BANJIAN_STATUS_WWYTJ + "," +
     * ZwdtConstant.BANJIAN_STATUS_WWYSTU + "," +
     * ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," +
     * ZwdtConstant.BANJIAN_STATUS_DJJ + "," + ZwdtConstant.BANJIAN_STATUS_YJJ +
     * "," + ZwdtConstant.BANJIAN_STATUS_DBB + "," +
     * ZwdtConstant.BANJIAN_STATUS_YSL + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG
     * + "," + ZwdtConstant.BANJIAN_STATUS_SPTG + "," +
     * ZwdtConstant.BANJIAN_STATUS_ZCBJ + "," + ZwdtConstant.BANJIAN_STATUS_BYSL
     * + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," +
     * ZwdtConstant.BANJIAN_STATUS_YCZZ); // 2.2、查询需要的办件字段 String fields =
     * "  rowguid,taskguid,projectname,applyername,status,receivedate,ouname,acceptuserdate,flowsn,tasktype,applyway,yushendate,applydate "
     * ; PageData<AuditProject> pageData =
     * iAuditProject.getAuditProjectPageDataByCondition(fields,
     * sqlConditionUtil.getMap(), Integer.parseInt(currentPage) *
     * Integer.parseInt(pageSize), Integer.parseInt(pageSize), "applydate",
     * "desc", keyWord).getResult(); int totalCount = pageData.getRowCount();
     * List<AuditProject> auditProjects = pageData.getList(); // 3、定义返回JSON对象
     * JSONObject dataJson = new JSONObject(); List<JSONObject> projectJsonList
     * = new ArrayList<>(); for (AuditProject auditProject : auditProjects) {
     * JSONObject projectJson = new JSONObject(); projectJson.put("projectguid",
     * auditProject.getRowguid()); // 办件标识 projectJson.put("projectname",
     * auditProject.getProjectname());// 办件名称 projectJson.put("flowsn",
     * auditProject.getFlowsn());// 办件流水号 projectJson.put("ouname",
     * auditProject.getOuname());// 部门名称 projectJson.put("applyername",
     * auditProject.getApplyername());// 申请人/申请单位 projectJson.put("status",
     * ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus()))
     * );// 办件状态 int tasktype = auditProject.getTasktype(); // 取出事项类别 //
     * 1：即办件，2：承诺件 int applyway = auditProject.getApplyway(); // 取出申报类型 //
     * 10：网上申报预审，11：网上直接申报 // 3.1、默认取出受理时间 String date =
     * StringUtil.isNotBlank(auditProject.getAcceptuserdate()) ? EpointDateUtil
     * .convertDate2String(auditProject.getAcceptuserdate(),
     * EpointDateUtil.DATE_FORMAT) : "--"; // 3.2、即办件 if (tasktype ==
     * Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) { //
     * 3.2.1、(申报预审),先判断是否有接件时间，没有在判断是否有受理时间，没有取预审时间 if (applyway ==
     * Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS)) { if
     * (auditProject.getStatus() == ZwdtConstant.BANJIAN_STATUS_YJJ) { date =
     * EpointDateUtil.convertDate2String(auditProject.getYushendate(),
     * EpointDateUtil.DATE_FORMAT);// 预审时间 } if
     * (StringUtil.isNotBlank(auditProject.getReceivedate())) { date =
     * EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
     * EpointDateUtil.DATE_FORMAT);// 接件时间 } } // 3.2.2、(直接受理),判断是否有受理时间，没有取申请时间
     * else if (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {
     * if ("--".equals(date)) { date =
     * EpointDateUtil.convertDate2String(auditProject.getApplydate(),
     * EpointDateUtil.DATE_FORMAT);// 申请时间 } } // 3.2.3、(窗口登记) else if (applyway
     * == Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ)) { if
     * (auditProject.getStatus() == ZwdtConstant.BANJIAN_STATUS_YJJ) { if
     * ("--".equals(date)) { date =
     * EpointDateUtil.convertDate2String(auditProject.getApplydate(),
     * EpointDateUtil.DATE_FORMAT);// 申请时间 } } if
     * (StringUtil.isNotBlank(auditProject.getReceivedate())) { date =
     * EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
     * EpointDateUtil.DATE_FORMAT);// 接件时间 } } } // 3.3、 承诺件 else if (tasktype
     * == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)) { // 3.3.1、直接受理 if
     * (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) { if
     * ("--".equals(date)) { date =
     * EpointDateUtil.convertDate2String(auditProject.getApplydate(),
     * EpointDateUtil.DATE_FORMAT);// 申请时间 } } if
     * (StringUtil.isNotBlank(auditProject.getReceivedate())) { date =
     * EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
     * EpointDateUtil.DATE_FORMAT);// 接件时间 } } projectJson.put("receivedate",
     * date);// 接件时间 projectJsonList.add(projectJson); }
     * dataJson.put("totalcount", totalCount); dataJson.put("projectlist",
     * projectJsonList); log.info("=======结束调用getProjectList接口======="); return
     * JsonUtils.zwdtRestReturn("1", "获取办件公示成功", dataJson.toString()); } else {
     * return JsonUtils.zwdtRestReturn("0", "身份验证失败！", ""); } } catch (Exception
     * e) { e.printStackTrace(); log.info("=======getProjectList异常信息：" +
     * e.getMessage() + "======="); log.info("=======getProjectList接口参数：params【"
     * + params + "】======="); return JsonUtils.zwdtRestReturn("0", "获取办件公示异常："
     * + e.getMessage(), ""); } }
     */

    /**
     * 办件公示详情的接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getPublicProjectDetail", method = RequestMethod.POST)
    public String getPublicProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPublicProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyername,pviguid,status,ouname,receiveusername,receivedate,flowsn,acceptusername,acceptuserdate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    // 4、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        dataJson.put("taskname", auditProject.getProjectname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("receivename", auditProject.getOuname());// 收件单位
                        dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                        dataJson.put("flowsn", auditProject.getFlowsn());// 办件编号
                        // 4.1、获取经办人与操作时间（优先取接件时间和接件人，没有的话取受理时间和受理人，否则显示--）
                        String userName = "--";
                        String date = "--";
                        userName = StringUtil.isNotBlank(auditProject.getAcceptusername())
                                ? auditProject.getAcceptusername()
                                : userName;
                        date = StringUtil.isNotBlank(auditProject.getAcceptuserdate())
                                ? EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                                        EpointDateUtil.DATE_TIME_FORMAT)
                                : date;
                        userName = StringUtil.isNotBlank(auditProject.getReceiveusername())
                                ? auditProject.getReceiveusername()
                                : userName;
                        dataJson.put("receiveusername", userName);// 经办人
                        dataJson.put("receivedate", date);// 接件时间
                        dataJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                    }
                }
                log.info("=======结束调用getPublicProjectDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPublicProjectDetail异常信息：" + e.getMessage() + "=======");
            log.info("=======getPublicProjectDetail接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办件详情的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.3、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 2.1、辖区编码以事项所在辖区为准（考虑到站点切换后查看个人申请的办件信息）
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                // 3、获取办件信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,applydate,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,windowname,ouname,receiveusername,receivedate,is_pause,applyertype,certtype,legal,legalid,is_fee,is_check,is_evaluat,spendtime,operatedate,is_cert ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    int status = auditProject.getStatus(); // 办件状态
                    int isCheck = auditProject.getIs_check() == null ? 0 : auditProject.getIs_check(); // 是否已核价
                    int isFee = auditProject.getIs_fee() == null ? 0 : auditProject.getIs_fee(); // 是否已收费
                    int isEvaluat = auditProject.getIs_evaluat() == null ? 0 : auditProject.getIs_evaluat(); // 办件是否已评价
                    // 4、获取事项基本信息拼接JSON
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    // 4.1、获取办件相关基本信息
                    dataJson.put("taskname", auditProject.getProjectname()); // 事项名称
                    dataJson.put("itemid", auditTask.getItem_id()); // 事项编码
                    dataJson.put("type", auditTask.getType()); // 办理类型
                    dataJson.put("promiseday", auditTask.getPromise_day());// 承诺时间
                    dataJson.put("applyertype", auditProject.getApplyertype()); // 办件申请人类型
                    dataJson.put("applyway", auditProject.getApplyway().equals(10) ? "网上申报预审" : "网上申报直接办理");// 申请方式
                    dataJson.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 申请时间
                    dataJson.put("savedate", EpointDateUtil.convertDate2String(auditProject.getOperatedate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 保存时间
                    dataJson.put("projectname", auditProject.getProjectname()); // 办件名称
                    dataJson.put("applyername", auditProject.getApplyername()); // 申请人/申请单位
                    dataJson.put("applyercertnum", auditProject.getCertnum()); // 申请人身份证
                    dataJson.put("contactperson", auditProject.getContactperson()); // 联系人
                    dataJson.put("contactmobile", auditProject.getContactmobile()); // 联系人手机
                    dataJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                    dataJson.put("contactcertnum", auditProject.getContactcertnum()); // 联系人身份证
                    dataJson.put("contactpostcode", auditProject.getContactpostcode()); // 邮编
                    dataJson.put("address", auditProject.getAddress());// 地址
                    dataJson.put("windowname", auditProject.getWindowname());// 办理部门
                    dataJson.put("remark", auditProject.getRemark());// 备注
                    dataJson.put("receivename", auditProject.getOuname());// 收件单位
                    dataJson.put("receiveusername", auditProject.getReceiveusername());// 经办人
                    dataJson.put("email", auditProject.getContactemail());// 邮箱
                    dataJson.put("fax", auditProject.getContactfax());// 传真
                    dataJson.put("legal", auditProject.getLegal()); // 法人代表
                    dataJson.put("orgalegalidnumber", auditProject.getLegalid()); // 法人代表身份证
                    dataJson.put("ispause", auditProject.getIs_pause()); // 是否暂停
                    dataJson.put("ouname", auditProject.getOuname()); // 部门名称
                    dataJson.put("receivedate", EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                            EpointDateUtil.DATE_TIME_FORMAT)); // 接件时间
                    dataJson.put("status", ZwdtConstant.getBanjanStatusKey(String.valueOf(status)));// 办件状态
                    dataJson.put("projectstatus", ZwdtConstant.getBanjanStatusKey(String.valueOf(status)));// 办件状态
                    dataJson.put("certtype",
                            iCodeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype())); // 办件证照类型
                    dataJson.put("qrcontent",
                            WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwmhwz/pages/mobile/html/appprojectdetail?taskguid="
                                    + auditTask.getRowguid() + "&projectguid=" + projectGuid); // 二维码内容
                    dataJson.put("areacode", areaCode); // 辖区编码
                    AuditOnlineProject auditonlineproject = null;
                    // 4.1.1 网上办件表中的declarername字段一起返回
                    if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditProject.getApplyertype().toString())) {
                        AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                                .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                        if (auditrscompanybaseinfo != null) {
                            String applyerUserGuid = auditrscompanybaseinfo.getCompanyid();
                            auditonlineproject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(auditProject.getRowguid(), applyerUserGuid)
                                    .getResult();
                            // 企业办件申请人姓名
                            if (auditonlineproject != null) {
                                dataJson.put("declarername", auditonlineproject.getDeclarername()); // 申报人姓名(企业)
                                dataJson.put("companyid", auditonlineproject.getApplyerguid()); // 申报人姓名(企业)
                            }
                        }
                    }
                    // 4.2、获取事项是否可以网上预约和申报
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTaskExtension != null) {
                        String webApplyType = String.valueOf(auditTaskExtension.getWebapplytype());
                        String onlinehandle = "0"; // 是否在线办理：默认否
                        if (StringUtil.isNotBlank(webApplyType)) {
                            onlinehandle = ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(webApplyType) ? "0" : "1";
                        }
                        dataJson.put("onlinehandle", onlinehandle);
                        dataJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                    }
                    // 4.3、获取事项配置的审批结果
                    AuditTaskResult auditResult = iAuditTaskResult
                            .getAuditResultByTaskGuid(auditProject.getTaskguid(), false).getResult();
                    dataJson.put("resultname", auditResult == null ? "无" : auditResult.getResultname());
                    // 4.4、返回是否已经发证
                    dataJson.put("iscert",
                            auditProject.getIs_cert() == null ? "0" : auditProject.getIs_cert().toString());
                    // 4.5、办件处于收费状态（已核价未收讫）
                    if (isCheck == ZwfwConstant.CONSTANT_INT_ONE && isFee == ZwfwConstant.CONSTANT_INT_ZERO) {
                        dataJson.put("pausereason", "收费中");
                    }
                    // 4.6、获取办件满意度
                    if (status == ZwfwConstant.BANJIAN_STATUS_ZCBJ || isEvaluat == ZwfwConstant.CONSTANT_INT_ONE) {
                        AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                .selectEvaluatByClientIdentifier(projectGuid).getResult();
                        if (auditOnlineEvaluat != null) {
                            String satisfied = auditOnlineEvaluat.getSatisfied(); // 满意度
                            String evaluateContent = StringUtil.isBlank(auditOnlineEvaluat.getEvaluatecontent()) ? ""
                                    : auditOnlineEvaluat.getEvaluatecontent(); // 评价内容
                            dataJson.put("satisfied", iCodeItemsService.getItemTextByCodeName("满意度", satisfied));
                            dataJson.put("evaluatecontent", evaluateContent);
                        }
                    }
                    // 4.7、获取办件暂停原因及暂停的时间
                    int isPause = auditProject.getIs_pause() == null ? 0 : auditProject.getIs_pause();
                    if (isPause == ZwfwConstant.CONSTANT_INT_ONE) {
                        AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual
                                .getProjectUnusualByProjectGuidAndType(projectGuid, ZwdtConstant.BANJIANOPERATE_TYPE_ZT)
                                .getResult();
                        if (auditProjectUnusual != null) {
                            dataJson.put("pausereason", auditProjectUnusual.getNote()); // 暂停原因
                        }
                    }
                    // 4.8、获取办件计时
                    String spareTime = "";
                    if (auditTask.getType() != Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                        AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                .getSparetimeByProjectGuid(projectGuid).getResult();
                        if (auditProjectSparetime != null) {
                            if (auditTask.getType() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                                spareTime = "即办件";
                            }
                            else {
                                int spareMinutes = auditProjectSparetime.getSpareminutes();
                                if (spareMinutes >= 0) {
                                    spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                }
                                else {
                                    spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                }
                            }
                        }
                        else {
                            if (status == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                spareTime = "待预审";
                            }
                            else if (status == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                spareTime = "预审通过";
                            }
                            else if (status == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                spareTime = "已接件";
                            }
                            else if (status == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                spareTime = "已办结";
                            }
                            else if ((status == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                    || (status == ZwfwConstant.BANJIAN_STATUS_SPBTG)) {
                                int spendMinutes = auditProject.getSpendtime();
                                spareTime = "办理用时 : " + CommonUtil.getSpareTimes(spendMinutes);
                            }
                            else {
                                if (auditTask.getType() == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                                    spareTime = "即办件";
                                }
                                else {
                                    spareTime = "距承诺办结期限：--个工作日";
                                }
                            }
                        }
                    }
                    else {
                        spareTime = "即办件";
                    }
                    dataJson.put("spendtime", spareTime);
                    // 4.9、获取办件审批结果的附件(提供给微信使用)
                    List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
                    // 4.9.1、办件审批的状态为审批通过或者正常办结需要查看办件的审批结果
                    if (status == ZwfwConstant.BANJIAN_STATUS_SPTG || status == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(projectGuid);
                        if (frameAttachInfos != null) {
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                JSONObject attachJson = new JSONObject();
                                FrameAttachStorage frameAttachStorage = iAttachService
                                        .getAttach(frameAttachInfo.getAttachGuid());
                                if (frameAttachStorage != null) {
                                    attachJson.put("attachicon",
                                            WebUtil.getRequestCompleteUrl(request)
                                                    + "/rest/auditattach/readAttach?attachguid="
                                                    + frameAttachStorage.getAttachGuid());// 附件图标
                                    attachJson.put("attachname", frameAttachInfo.getAttachFileName()); // 附件名称
                                    attachJson.put("attachsize",
                                            AttachUtil.convertFileSize(frameAttachInfo.getAttachLength()));// 附件大小
                                    attachJson.put("attachsource", frameAttachInfo.getCliengTag());// 附件来源
                                    attachJson.put("attachguid", frameAttachInfo.getAttachGuid());// 附件唯一标识
                                    attachJsonList.add(attachJson);
                                }
                            }
                        }
                    }
                    dataJson.put("attachlist", attachJsonList);
                    dataJson.put("attachcount", attachJsonList.size());
                    // 4.10、办件材料补正状态 1:补正待审核(无需显示提交按钮) 0:待补正(需要显示提交按钮)
                    String materialstatus = "1"; // 默认无需显示提交按钮(考虑到大厅页面没有关闭，审批系统直接受理情况)
                    if (status == ZwfwConstant.BANJIAN_STATUS_DBB || status == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("projectguid", projectGuid);
                        sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                        List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditProjectMaterials != null && auditProjectMaterials.size() > 0) {
                            materialstatus = "0"; // 如果办件材料中存在待补正的材料，则需要显示提交按钮
                        }
                    }
                    dataJson.put("materialstatus", materialstatus);
                    log.info("=======结束调用getProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDetail接口参数：params【" + params + "】=======");
            log.info("=======getProjectDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 根据办件流水号获取办件信息的接口（APP用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectDetailByFlowSN", method = RequestMethod.POST)
    public String getProjectDetailByFlowSN(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectDetailByFlowSN接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取办件的流水号
                String flowsn = obj.getString("flowsn");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、获取办件
                SqlConditionUtil conditionMapsql = new SqlConditionUtil();
                JSONObject dataJson = new JSONObject();
                conditionMapsql.eq("areacode", areaCode);
                // 3、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,applydate,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,windowname,ouname,receiveusername,receivedate,is_pause,applyertype,certtype,legal,is_fee,is_check,acceptuserdate,acceptusername ";
                AuditProject auditProject = iAuditProject.getAuditProjectByFlowsn(fields, flowsn, areaCode).getResult();
                if (auditProject != null) {
                    // 4、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        dataJson.put("taskname", auditProject.getProjectname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                        dataJson.put("receivename", auditProject.getOuname());// 收件单位
                        dataJson.put("flowsn", auditProject.getFlowsn());// 办件编号
                        String userName = "--";
                        userName = StringUtil.isNotBlank(auditProject.getAcceptusername())
                                ? auditProject.getAcceptusername()
                                : userName;
                        userName = StringUtil.isNotBlank(auditProject.getReceiveusername())
                                ? auditProject.getReceiveusername()
                                : userName;
                        dataJson.put("receiveusername", userName);// 经办人
                        dataJson.put("receivedate", EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                                EpointDateUtil.DATE_TIME_FORMAT));// 接件时间
                        dataJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                        log.info("=======结束调用getProjectDetailByFlowSN接口=======");
                        return JsonUtils.zwdtRestReturn("1", "根据办件流水号获取办件信息成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDetailByFlowSN接口参数：params【" + params + "】=======");
            log.info("=======getProjectDetailByFlowSN异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "根据办件流水号获取办件信息的接口异常：" + e.getMessage(), "");
        }
    }

    /**
     * 评价接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/evaluateProject", method = RequestMethod.POST)
    public String evaluateProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用evaluateProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取评价等级
                String evaluateLevel = obj.getString("evaluatelevel");
                // 1.3、获取评价内容
                String evaluateNote = obj.getString("evaluatenote");
                // 1.4、获取申请人类型
                String companyid = obj.getString("companyid");
                // 1.5、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 2.1、新增在线评价数据
                    AuditOnlineEvaluat auditOnlineEvaluat = new AuditOnlineEvaluat();
                    auditOnlineEvaluat.setRowguid(UUID.randomUUID().toString());
                    auditOnlineEvaluat.setClientidentifier(projectGuid); // 业务标识
                    auditOnlineEvaluat.setClienttype(ZwdtConstant.EVALUATEOBJECT_PROJECT); // 评价对象(10：办件、20：事项)
                    auditOnlineEvaluat.setSatisfied(evaluateLevel); // 满意度级别
                    auditOnlineEvaluat.setEvaluatecontent(evaluateNote); // 满意度内容
                    auditOnlineEvaluat.setEvaluatedate(new Date()); // 评价时间
                    auditOnlineEvaluat.setEvaluatetype(ZwdtConstant.EVALUATETYPE_ONLINE); // 评价方式(10：网上申报
                    // 20：短信
                    // 30：评价器)
                    auditOnlineEvaluat.setEvaluateuserguid(auditOnlineRegister.getAccountguid()); // 评价用户标识
                    auditOnlineEvaluat.setEvaluateusername(auditOnlineIndividual.getClientname());// 评价用户名称
                    iAuditOnlineEvaluat.addAuditOnineEvaluat(auditOnlineEvaluat);
                    // 2.2、更新AUDIT_ONLINE_PROJECT为已评价
                    // 获取外网办件表，如果是企业办件，则首先需要获取companyid
                    AuditOnlineProject auditOnlineProject = null;
                    if (StringUtil.isNotBlank(companyid)) {
                        auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(projectGuid, companyid)
                                .getResult();
                    }
                    else {
                        auditOnlineProject = iAuditOnlineProject
                                .getOnlineProjectByApplyerGuid(projectGuid, auditOnlineIndividual.getApplyerguid())
                                .getResult();
                    }

                    auditOnlineProject.setIs_evaluat(1);
                    iAuditOnlineProject.updateProject(auditOnlineProject);
                    // 2.3、更新AUDIT_PROJECT为已评价
                    AuditProject auditProject = new AuditProject();
                    auditProject.setRowguid(projectGuid);
                    auditProject.setAreacode(auditOnlineProject.getAreacode());
                    auditProject.setIs_evaluat(1);
                    iAuditProject.updateProject(auditProject);
                    log.info("=======结束调用evaluateProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
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
            log.info("=======evaluateProject接口参数：params【" + params + "】=======");
            log.info("=======evaluateProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "评价失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件各个状态具体数量以及申请总量（手机端用）
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/private/getProjectCountList", method = RequestMethod.POST)
    public String getappcountlist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getprojectcountlist接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String areacode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 返回手机端中各个统计数量
                AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                        .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                // 3、拼接我的办件的检索条件
                SqlConditionUtil conditionsqlall = new SqlConditionUtil();
                SqlConditionUtil conditionsqldtj = new SqlConditionUtil();
                SqlConditionUtil conditionsqldbz = new SqlConditionUtil();
                SqlConditionUtil conditionsqldjf = new SqlConditionUtil();
                SqlConditionUtil conditionsqlspz = new SqlConditionUtil();
                SqlConditionUtil conditionsqldpj = new SqlConditionUtil();
                SqlConditionUtil conditionsqlybj = new SqlConditionUtil();
                SqlConditionUtil conditionsqlqt = new SqlConditionUtil();
                String applyerguid = auditOnlineIndividual.getApplyerguid();
                // 3.2、拼接办件状态
                SqlConditionUtil conditionAll = handleProjectSqlCondition(conditionsqlall, 0, areacode, applyerguid);
                SqlConditionUtil conditionDtj = handleProjectSqlCondition(conditionsqldtj, 1, areacode, applyerguid);
                SqlConditionUtil conditionDbz = handleProjectSqlCondition(conditionsqldbz, 2, areacode, applyerguid);
                SqlConditionUtil conditionDjf = handleProjectSqlCondition(conditionsqldjf, 4, areacode, applyerguid);
                SqlConditionUtil conditionSpz = handleProjectSqlCondition(conditionsqlspz, 5, areacode, applyerguid);
                SqlConditionUtil conditionDpj = handleProjectSqlCondition(conditionsqldpj, 7, areacode, applyerguid);
                SqlConditionUtil conditionYbj = handleProjectSqlCondition(conditionsqlybj, 8, areacode, applyerguid);
                SqlConditionUtil conditionQt = handleProjectSqlCondition(conditionsqlqt, 9, areacode, applyerguid);
                // 查询办件各个状态数量
                int allProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionAll.getMap()).getResult();
                dataJson.put("allprojectcount", allProjectCount);
                int dtjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDtj.getMap()).getResult();
                dataJson.put("dtjprojectcount", dtjProjectCount);
                int dbzProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDbz.getMap()).getResult();
                dataJson.put("dbzprojectcount", dbzProjectCount);
                int djfProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDjf.getMap()).getResult();
                dataJson.put("djfprojectcount", djfProjectCount);
                int spzProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionSpz.getMap()).getResult();
                dataJson.put("spzprojectcount", spzProjectCount);
                int dpjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDpj.getMap()).getResult();
                dataJson.put("dpjprojectcount", dpjProjectCount);
                int ybjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionYbj.getMap()).getResult();
                dataJson.put("ybjprojectcount", ybjProjectCount);
                int qtProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionQt.getMap()).getResult();
                dataJson.put("qtprojectcount", qtProjectCount);
                log.info("=======结束调用getprojectcountlist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取统计数量成功", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取统计数量失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getprojectcountlist接口参数：params【" + params + "】=======");
            log.info("=======getprojectcountlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取统计数量失败：" + e.getMessage(), "");
        }
    }

    private SqlConditionUtil handleProjectSqlCondition(SqlConditionUtil conditionsql, int intstatus, String areacode,
            String applyerguid) {
        conditionsql.eq("APPLYERGUID", applyerguid);
        // 3.1、拼接通用的申请人、区域
        if (StringUtil.isNotBlank(areacode)) {
            conditionsql.eq("AREACODE", areacode);
        }
        switch (intstatus) {
            case 0:// 我的申请全部办件信息 TODO
                conditionsql.in("status", "12,16,24,26,30,40,50");
                break;
            case 1:// 待提交(外网申报未提交、预审不通过)
                conditionsql.in("status", ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU);
                break;
            case 2:// 待补正(待补办)
                conditionsql.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                break;
            case 3:// 待原件提交 TODO
                conditionsql.eq("status", String.valueOf(100));// 暂时不显示
                break;
            case 4:// 待缴费(需要收费、已核价未收讫)
                conditionsql.eq("is_charge", "1"); // 办件需要缴费
                conditionsql.eq("is_check", "1"); // 办件已核价
                conditionsql.eq("is_fee", "0"); // 办件未收讫
                break;
            case 5:// 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                conditionsql.in("status", "12,16,24,26,30,40,50");
                break;
            case 6:// 待签收 TODO
                break;
            case 7:// 待评价(正常办结、未评价)
                conditionsql.eq("is_evaluat", "0");
                conditionsql.eq("status", "90");
                break;
            case 8:// 已办结(正常办结、已评价)
                conditionsql.eq("is_evaluat", "1");
                conditionsql.eq("status", "90");
                break;
            case 9:// 其他(不予受理、撤销申请、异常终止)
                conditionsql.in("status", "97,98,99");
                break;
            default:
                break;
        }
        return conditionsql;
    }

    /**
     * 获取办件流转信息接口(网厅小圆球展示)
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectFlowlist", method = RequestMethod.POST)
    public String getProjectFlowlist(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectFlowlist接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取传入参数值
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String projectGuid = obj.getString("projectguid");
                String areaCode = obj.getString("areacode");
                // 3、根据办件GUID获取办件的一些基本信息，是否收费等
                String fields = " rowguid,taskguid,projectname,status,charge_when,operatedate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    // 4、获取该办件所对应的事项信息，是否收费、何时收费。
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    int chargeFlag = auditTask.getCharge_flag() == null ? 0 : auditTask.getCharge_flag();
                    int chargeWhen = auditProject.getCharge_when() == null ? 0 : auditProject.getCharge_when();
                    // 5、下面的六个对象对应了详情页面上的六个圆球
                    JSONObject wssqJson = new JSONObject(); // 网上申请（从外网提交到接件前）
                    JSONObject clbzJson = new JSONObject(); // 材料补正
                    JSONObject slJson = new JSONObject();// 受理
                    JSONObject blJson = new JSONObject();// 办理
                    // 步骤是从受理到批准中间的步骤都属于办理
                    JSONObject bjJson = new JSONObject();// 办结，评价后属于办结
                    JSONObject sfJson = new JSONObject();// 收费
                    // 6、默认初始化信息，除了网上申请，默认都是未办理
                    // 网上申请
                    wssqJson.put("nodename", "网上申请");
                    // 网上申请圆球设置为正在处理状态 0:已完成 1：正在处理 2：未处理 （各个状态同下）
                    wssqJson.put("nodestatus", "1");//
                    wssqJson.put("order", 1);
                    // 补正
                    clbzJson.put("nodename", "材料补正");
                    clbzJson.put("nodestatus", "2");
                    // 受理、最大状态为补正后属于受理中，预审通过也属于受理中
                    slJson.put("nodename", "受理");
                    slJson.put("nodestatus", "2");
                    // 办理、 受理完毕后批准前属于办理
                    blJson.put("nodename", "办理");
                    blJson.put("nodestatus", "2");
                    // 办结、 评价完毕以后属于办结
                    bjJson.put("nodename", "办结");
                    bjJson.put("nodestatus", "2");
                    // 收费、 收费分受理前收费和办结前收费
                    sfJson.put("nodename", "收费");
                    sfJson.put("nodestatus", "2");
                    // 7、获取办件操作日志来设置圆球状态
                    List<AuditProjectOperation> auditProjectOperations = new ArrayList<AuditProjectOperation>();
                    auditProjectOperations = iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
                    Boolean isHasSF = false; // 查看是否已收费
                    // 补正的显示有以下几种情况，0:不显示补正,1：存在补正，2：受理前收费情况下，先补正后收费，3：受理前收费情况下，先收费再补正
                    int bzDisplay = 0;
                    // 8、返回的操作日志按时间顺序排序输出，只筛选指定的节点步骤，每输出一次，假定当前输出步骤为最后步骤，覆盖前一步骤圆球状态
                    List<JSONObject> nodelist = new ArrayList<JSONObject>();
                    for (int i = 0; i < auditProjectOperations.size(); i++) {
                        String operateType = auditProjectOperations.get(i).getOperateType();
                        switch (operateType) {
                            // 8.1、接件节点
                            case ZwfwConstant.OPERATE_JJ:
                                // 8.1.1、节点状态，下同： 0:已完成 1：正在处理 2：未处理
                                wssqJson.put("nodestatus", "0");
                                // 8.1.2、如果存在接件步骤默认设置受理为正在处理，如果存在收费，则设置收费为待处理，受理为未处理
                                slJson.put("nodestatus", "1");
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "2");
                                    sfJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.2、补正节点（有补正节点，则补正为处理中）
                            case ZwfwConstant.OPERATE_BZ:
                                bzDisplay = ZwdtConstant.NOFEE_BZ;
                                clbzJson.put("nodestatus", "1");
                                // 8.2.1、补正默认现在有3种情况，没有收费的补正，收费前补正、收费后补正
                                // 1 受理前收费的补正
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    // 1.1 收费前补正，设置收费正在处理
                                    if (isHasSF == false) {
                                        bzDisplay = ZwdtConstant.BEFORE_FEE_BZ;
                                        sfJson.put("nodestatus", "2");
                                    }
                                    // 1.2 收费后补正，设置受理状态为未处理
                                    else {
                                        bzDisplay = ZwdtConstant.AFTER_FEE_BZ;
                                        slJson.put("nodestatus", "2");
                                    }
                                }
                                else {
                                    slJson.put("nodestatus", "2");
                                }
                                break;
                            // 8.3、办件收费节点，如果是受理前收费，则需要修改补正状态为已处理
                            case ZwfwConstant.OPERATE_BJSF:
                                // 8.3.1、设置收费状态为待处理
                                sfJson.put("nodestatus", "1");
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    // 1、 如果收费状态为收费前补正，则需要把补正状态改为已处理
                                    if (bzDisplay == ZwdtConstant.BEFORE_FEE_BZ) {
                                        clbzJson.put("nodestatus", "0");
                                    }
                                    // 2、如果收费状态为收费后补正，则需要把收费改为待处理
                                    else if (bzDisplay == ZwdtConstant.AFTER_FEE_BZ) {
                                        clbzJson.put("nodestatus", "0");
                                        sfJson.put("nodestatus", "1");
                                    }
                                }
                                else if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    sfJson.put("nodestatus", "1");
                                }
                                break;
                            case ZwfwConstant.OPERATE_SL:
                                // 受理表示补正状态已结束，设置补正为已处理
                                clbzJson.put("nodestatus", "0");
                                slJson.put("nodestatus", "0");
                                blJson.put("nodestatus", "1");
                                break;
                            // 8.4、收讫节点，收讫后存在2种情况，一种是受理前收费，一种是办结前
                            case ZwfwConstant.OPERATE_SQ:
                                isHasSF = true;
                                // 8.4.1 设置收费已完成
                                sfJson.put("nodestatus", "0");
                                // 8.4.2 受理前收费、设置受理为正在处理
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "1");
                                }
                                // 8.4.3 办结前收费、 设置办理为已处理
                                // （因目前已经不存在批准按钮，以审批通过为节点判断）
                                else if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    bjJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.5 审批通过节点、办理设置为已处理，办结设置为正在处理
                            case ZwfwConstant.OPERATE_SPTG:
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    sfJson.put("nodestatus", "1");
                                    bjJson.put("nodestatus", "2");
                                }
                                else {
                                    blJson.put("nodestatus", "0");
                                    bjJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.6 办结节点
                            case ZwfwConstant.OPERATE_BJ:
                                blJson.put("nodestatus", "0");
                                bjJson.put("nodestatus", "0");
                                break;
                            // 8.7 外网申报提交节点
                            case ZwfwConstant.OPERATE_WWYTJ:
                                wssqJson.put("nodestatus", "0");
                                // 受理前收费
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "2");
                                    sfJson.put("nodestatus", "1");
                                }
                                else {
                                    slJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.8 预审退回节点
                            case ZwfwConstant.OPERATE_YSDH:
                                wssqJson.put("nodestatus", "1");
                                // 受理前收费
                                if (chargeFlag == ZwdtConstant.INT_YES
                                        && chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    sfJson.put("nodestatus", "2");
                                }
                                else {
                                    slJson.put("nodestatus", "2");
                                }
                                break;
                            // 8.5 审批不通过节点、办理设置为已处理，办结设置为正在处理
                            case ZwfwConstant.OPERATE_SPBTG:
                                blJson.put("nodestatus", "0");
                                bjJson.put("nodestatus", "1");
                                break;
                            default:
                                break;
                        }

                    }
                    // 9、设置圆球显示顺序
                    nodelist.add(wssqJson);
                    // 9.1 网上申请默认显示在第一个
                    wssqJson.put("order", 1);
                    // 9.2 判定补正显示的顺序
                    boolean isShowSF = false; // 是否已显示收费环节
                    // 不存在收费后补正操作的情况
                    if (bzDisplay > 0) {
                        // 补正在收费之后
                        if (bzDisplay == ZwdtConstant.AFTER_FEE_BZ) {
                            isShowSF = true;
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                        nodelist.add(clbzJson);
                        clbzJson.put("order", nodelist.size());
                    }
                    // 9.3 受理前收费按钮显示，如果收费在补正前，这边就不显示了
                    if (chargeFlag == ZwfwConstant.CONSTANT_INT_ONE && isShowSF == false) {
                        if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                    }
                    nodelist.add(slJson);
                    // 9.4 添加受理
                    slJson.put("order", nodelist.size());
                    nodelist.add(blJson);
                    // 9.5 添加办理
                    blJson.put("order", nodelist.size());
                    // 9.6 添加办结前收费
                    if (chargeFlag == ZwfwConstant.CONSTANT_INT_ONE) {
                        if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                    }
                    // 9.7、若有审批不通过操作，则去掉办结前收费
                    boolean flag = false;
                    for (int i = 0; i < auditProjectOperations.size(); i++) {
                        AuditProjectOperation auditProjectOperation = auditProjectOperations.get(i);
                        if (auditProjectOperation != null) {
                            if (ZwfwConstant.OPERATE_SPBTG.equals(auditProjectOperation.getOperateType())) {
                                flag = true;
                            }
                        }
                    }
                    if (flag) {
                        if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                            nodelist.remove(sfJson);
                        }
                    }
                    nodelist.add(bjJson);
                    bjJson.put("order", nodelist.size());
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodelist);
                    log.info("=======结束调用getProjectFlowlist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件流转信息成功", dataJson.toString());
                }
                else {
                    JSONObject dataJson = new JSONObject();
                    log.info("=======结束调用getProjectFlowlist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "办件信息不存在！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectFlowlist接口参数：params【" + params + "】=======");
            log.info("=======getProjectFlowlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件流转信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件过程信息（所有操作步骤都会列出）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectProcesslist", method = RequestMethod.POST)
    public String getProjectProcesslist(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectProcesslist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,status,charge_when,operatedate,applydate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    List<JSONObject> nodeJsonList = new ArrayList<JSONObject>();
                    // 根据办件日志填写对应内容(按照时间顺序排列)
                    List<AuditProjectOperation> auditProjectOperations = new ArrayList<AuditProjectOperation>();
                    auditProjectOperations = iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
                    // 这里为每一个步骤添加开始时间
                    String beginTime = "";
                    for (int i = 0; i < auditProjectOperations.size(); i++) {
                        if (i > 0) {
                            // 添加上一个节点的操作时间
                            beginTime = EpointDateUtil.convertDate2String(
                                    auditProjectOperations.get(i - 1).getOperatedate(),
                                    EpointDateUtil.DATE_TIME_FORMAT);
                        }
                        else {
                            beginTime = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                    EpointDateUtil.DATE_TIME_FORMAT);
                        }
                        // 手机端要求返回操作类型中文
                        String operateType = auditProjectOperations.get(i).getOperateType();
                        String operateTypeName = ZwdtConstant.getOperateTypeKey(operateType);
                        JSONObject tempJson = new JSONObject();
                        tempJson.put("nodename", operateTypeName);
                        tempJson.put("nodestatus", "0"); // 默认展示
                        tempJson.put("endtime", EpointDateUtil.convertDate2String(
                                auditProjectOperations.get(i).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                        tempJson.put("begintime", beginTime);
                        tempJson.put("operateusername", auditProjectOperations.get(i).getOperateusername());
                        tempJson.put("remark", auditProjectOperations.get(i).getRemarks());
                        nodeJsonList.add(tempJson);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodeJsonList);
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件过程信息成功", dataJson.toString());
                }
                else {
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "办件过程信息不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectProcesslist接口参数：params【" + params + "】=======");
            log.info("=======getProjectProcesslist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件过程信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取流程中产生文书的接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getProjectDoclist", method = RequestMethod.POST)
    public String getProjectDoclist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectDoclist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");// 办件标识，audit_project的主键
                // 2、获取办件文书快照信息
                List<AuditProjectDocSnap> auditProjectDocSnaps = iAuditProjectDocSnap
                        .selectDocSnapByProjectGuid(projectGuid).getResult();
                List<JSONObject> docJsonList = new ArrayList<JSONObject>();
                for (AuditProjectDocSnap auditProjectDocSnap : auditProjectDocSnaps) {
                    JSONObject docJson = new JSONObject();
                    // 2、文书名称默认不需要事项名称
                    String docName = iCodeItemsService.getItemTextByCodeName("文书类型",
                            String.valueOf(auditProjectDocSnap.getDoctype()));
                    docJson.put("docname", docName);
                    // 3、判断使用NTKO插件还是HTML页面形式
                    if (StringUtil.isBlank(auditProjectDocSnap.getDocattachguid())) {
                        // 3.1、如果是HTML页面形式，返回全路径
                        docJson.put("doccontent", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/myspace/doc?rowguid=" + auditProjectDocSnap.getRowguid());
                        docJson.put("type", 1);
                    }
                    else {
                        // 3.2、如果是NTKO形式
                        String url = ConfigUtil.getConfigValue("officeweb365url");
                        FrameAttachStorage frameAttachStorage = iAttachService
                                .getAttach(auditProjectDocSnap.getDocattachguid());
                        String attachguid = "";
                        String documentType = "";
                        String fname = "";
                        if (frameAttachStorage != null) {
                            attachguid = frameAttachStorage.getAttachGuid();
                            documentType = frameAttachStorage.getDocumentType();
                            fname = attachguid + documentType;
                        }
                        url = url + "fname=" + fname + "&furl=" + WebUtil.getRequestCompleteUrl(request)
                                + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                + attachguid;
                        docJson.put("doccontent", url);
                        docJson.put("type", 0);
                    }
                    docJsonList.add(docJson);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("doclist", docJsonList);
                int totalcount = docJsonList == null ? 0 : docJsonList.size();
                dataJson.put("totalcount", totalcount);
                log.info("=======结束调用getProjectDoclist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取流程中产生文书成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDoclist接口参数：params【" + params + "】=======");
            log.info("=======getProjectDoclist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取流程中产生文书失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取待预审办件接口（自检页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getDYSProjectList", method = RequestMethod.POST)
    public String getDYSProjectList(@RequestBody String params) {
        try {
            log.info("=======开始获取getDYSProjectList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页
                String currentPage = obj.getString("pageindex");
                // 1.2、获取每页数量
                String pageSize = obj.getString("pagesize");
                // 2、获取外网待预审办件列表
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                SqlConditionUtil conditionUtil = new SqlConditionUtil();
                conditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_WWYTJ)); // 待预审状态：12
                PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                        conditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "ouname,applydate", "desc").getResult();
                // 2.1、获取外网提交预审办件列表数量
                int count = pageData.getRowCount();
                List<AuditOnlineProject> list = pageData.getList();
                for (AuditOnlineProject auditOnlineProject : list) {
                    JSONObject recordJson = new JSONObject();
                    recordJson.put("projectname", auditOnlineProject.getProjectname());
                    recordJson.put("ouname", auditOnlineProject.getOuname());
                    recordJson.put("applyername", auditOnlineProject.getApplyername());
                    recordJson.put("applydate", EpointDateUtil.convertDate2String(auditOnlineProject.getApplydate(),
                            EpointDateUtil.DATE_FORMAT));
                    // 2.2、查出办件对应的窗口
                    AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditOnlineProject.getTaskguid()).getResult();
                    if (auditTask != null) {
                        List<AuditOrgaWindow> auditOrgaWindowList = iAuditOrgaWindow
                                .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                        String windowname = "";
                        if (auditOrgaWindowList != null && auditOrgaWindowList.size() > 0) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindowList) {
                                if (StringUtil.isBlank(auditOrgaWindow.getWindowname())) {
                                    windowname = "无对应窗口";
                                }
                                else {
                                    windowname = windowname + auditOrgaWindow.getWindowname() + ",";
                                }
                            }
                            String name = auditOnlineProject.getOuname() + "【" + windowname;
                            recordJson.put("projectname", name.substring(0, name.length() - 1) + "】");
                        }
                        else {
                            recordJson.put("projectname", auditOnlineProject.getOuname() + "【无对应窗口】");
                        }
                    }
                    jsonList.add(recordJson);
                }
                // 3、获取每个部门以及部门待预审的办件数量
                List<JSONObject> groupJsonList = new ArrayList<JSONObject>();
                List<Record> auditOnlineProjects = iAuditOnlineProject.getDYSProjectListByGroup().getResult();
                for (Record record : auditOnlineProjects) {
                    JSONObject groupJson = new JSONObject();
                    groupJson.put("groupOuname", record.get("ouname"));
                    groupJson.put("groupCount", record.get("count"));
                    if (StringUtil.isBlank(record.get("ouname"))) {
                        groupJson.put("groupOuname", "其他");
                    }
                    groupJsonList.add(groupJson);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("jsonList", jsonList);
                dataJson.put("count", count);
                dataJson.put("groupJsonList", groupJsonList);
                log.info("=======结束调用getDYSProjectList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取待预审办件成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDYSProjectList接口参数：params【" + params + "】=======");
            log.info("=======getDYSProjectList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取待预审办件异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办件结果详情的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectResult", method = RequestMethod.POST)
    public String getProjectResult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectResult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取办件编号
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<JSONObject> resultlist = new ArrayList<JSONObject>();
                // 3、获取办件
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid, areaCode).getResult();
                if (auditProject != null) {
                    // 4、获取证照实例
                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(auditProject.getCertrowguid());
                    if (certInfo != null) {
                        // 5、获取结果对应的附件
                        List<JSONObject> attachList = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                ZwdtConstant.CERTAPPKEY);
                        if (attachList != null && attachList.size() > 0) {
                            for (JSONObject attachJson : attachList) {
                                JSONObject resultJson = new JSONObject();
                                resultJson.put("resultattachname", attachJson.get("attachname"));
                                resultJson.put("resultattachurl",
                                        WebUtil.getRequestCompleteUrl(request)
                                                + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                + attachJson.getString("attachguid") + "&filename="
                                                + attachJson.get("attachname"));// 附件图标
                                resultlist.add(resultJson);
                            }
                        }
                        dataJson.put("resultlist", resultlist);
                    }
                }
                log.info("=======结束调用getProjectResult接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件结果详情成功", dataJson.toString());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectResult接口参数：params【" + params + "】=======");
            log.info("=======getProjectResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件结果详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办件详情的接口 办件详情二维码处调用
     *
     * @param params
     *            接口的入参
     * @param request
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectInfo", method = RequestMethod.POST)
    public String getProjectInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取办件信息
                // 3.1、获取办件辖区信息
                String areaCode = "";
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 3.1.1、辖区编码以事项所在辖区为准（考虑到站点切换后查看个人申请的办件信息）
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                // 3.2、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,applydate,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,windowname,ouname,receiveusername,receivedate,is_pause,applyertype,certtype,legal,legalid,is_fee,is_check,is_evaluat,spendtime,operatedate,is_cert,flowsn ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    int status = auditProject.getStatus(); // 办件状态
                    dataJson.put("flowsn", auditProject.getFlowsn()); // 办件编号
                    dataJson.put("applyertype", auditProject.getApplyertype()); // 办件申请人类型
                    dataJson.put("applyway", auditProject.getApplyway().equals(10) ? "网上申报预审" : "网上申报直接办理");// 申请方式
                    dataJson.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 申请时间
                    dataJson.put("projectname", auditProject.getProjectname()); // 办件名称
                    dataJson.put("applyername", auditProject.getApplyername()); // 申请人/申请单位
                    dataJson.put("applyercertnum", auditProject.getCertnum()); // 申请人身份证
                    dataJson.put("contactperson", auditProject.getContactperson()); // 联系人
                    dataJson.put("contactmobile", auditProject.getContactmobile()); // 联系人手机
                    dataJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                    dataJson.put("address", auditProject.getAddress());// 地址
                    dataJson.put("legal", auditProject.getLegal()); // 法人代表
                    dataJson.put("orgalegalidnumber", auditProject.getLegalid()); // 法人代表身份证
                    dataJson.put("certtype",
                            iCodeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype())); // 办件证照类型
                    dataJson.put("projectstatus", ZwdtConstant.getBanjanStatusKey(String.valueOf(status)));// 办件状态
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "办件不存在！", "");
                }
                // 4、获取办件材料信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", projectGuid);
                sqlConditionUtil.nq("status", String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT));
                sqlConditionUtil.setOrderAsc("necessary");
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditProjectMaterials != null && auditProjectMaterials.size() > 0) {
                    for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                        // 4.1、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            JSONObject materialJson = new JSONObject();
                            materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid()); // 办件材料标识
                            materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial()); // 办件材料名称
                            int necessary = auditTaskMaterial.getNecessity(); // 是否必要(默认设置为事项材料的必要性)
                            materialJson.put("necessary",
                                    necessary == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES) ? 1 : 0); // 是否必要
                            materialJson.put("ordernumber", auditTaskMaterial.getOrdernum()); // 排序
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 材料来源
                            materialJsonList.add(materialJson);
                        }
                    }
                    dataJson.put("materiallist", materialJsonList);
                    dataJson.put("materialcount", materialJsonList.size());
                }
                // 5、获取办件流转相关信息
                List<JSONObject> nodeJsonList = new ArrayList<JSONObject>();
                List<AuditProjectOperation> auditProjectOperations = new ArrayList<AuditProjectOperation>();
                auditProjectOperations = iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
                // 5.1、这里为每一个步骤添加开始时间
                String beginTime = "";
                for (int i = 0; i < auditProjectOperations.size(); i++) {
                    if (i > 0) {
                        // 5.1.1、添加上一个节点的操作时间
                        beginTime = EpointDateUtil.convertDate2String(
                                auditProjectOperations.get(i - 1).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT);
                    }
                    else {
                        beginTime = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                EpointDateUtil.DATE_TIME_FORMAT);
                    }
                    String operateType = auditProjectOperations.get(i).getOperateType();
                    String operateTypeName = ZwdtConstant.getOperateTypeKey(operateType);// 操作名称
                    JSONObject tempJson = new JSONObject();
                    tempJson.put("nodename", operateTypeName);
                    tempJson.put("nodestatus", "0"); // 默认展示
                    tempJson.put("endtime", EpointDateUtil.convertDate2String(
                            auditProjectOperations.get(i).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));// 结束时间
                    tempJson.put("begintime", beginTime);// 开始时间
                    tempJson.put("operateusername", auditProjectOperations.get(i).getOperateusername());// 操作人员名称
                    tempJson.put("remark", auditProjectOperations.get(i).getRemarks());// 备注
                    nodeJsonList.add(tempJson);
                }
                dataJson.put("nodelist", nodeJsonList);
                return JsonUtils.zwdtRestReturn("1", "获取办件结果详情成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectInfo接口参数：params【" + params + "】=======");
            log.info("=======getProjectInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 更新办件表onlineapplyerguid字段的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/updateProjectInfo", method = RequestMethod.POST)
    public String updateProjectInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用updateProjectInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取所有外网办件
                List<AuditOnlineProject> auditOnlineProjectList = iAuditOnlineProject
                        .getAuditOnlineProjectPageData(null, 0, 0, null, null).getResult().getList();
                if (auditOnlineProjectList != null && auditOnlineProjectList.size() > 0) {
                    // 3.遍历所有外网办件，更新对应申报人的办件表中的onlineapplyerguid字段
                    for (AuditOnlineProject auditOnlineProject : auditOnlineProjectList) {
                        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(
                                auditOnlineProject.getSourceguid(), auditOnlineProject.getAreacode()).getResult();
                        if (auditProject != null) {
                            if (StringUtil.isBlank(auditProject.getOnlineapplyerguid())) {
                                auditProject.setOnlineapplyerguid(auditOnlineProject.getApplyerguid());
                            }
                            iAuditProject.updateProject(auditProject);
                        }
                    }
                }
                log.info("=======结束调用updateProjectInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "刷新办件表成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateProjectInfo异常信息：" + e.getMessage() + "=======");
            log.info("=======updateProjectInfo接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "刷新办件表异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件材料信息的接口（手机端扫码上传材料调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getScanMaterialList", method = RequestMethod.POST)
    public String getScanMaterialList(@RequestBody String params) {
        try {
            log.info("=======开始调用getScanMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String clientIdentifier = obj.getString("clientidentifier");
                // 1.2、获取事项多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 2、拼接办件材料条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", clientIdentifier);
                // 3、获取办件的材料
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                        // 3.1、如果事项配置了材料多情形情况，获取该情形下的材料列表
                        List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                                .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                        for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                            if (auditProjectMaterial.getTaskmaterialguid()
                                    .equals(auditTaskMaterialCase.getMaterialguid())) {
                                // 3.1.1、获取多情形中配置的材料基本信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    JSONObject materialJson = new JSONObject();
                                    // 3.1.2、获取办件材料基本信息
                                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                    materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                    materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                                    materialJson.put("sharematerialiguid",
                                            auditProjectMaterial.getSharematerialiguid());// 材料关联的共享材料标识
                                    materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                    materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                                    materialJsonList.add(materialJson);
                                }
                            }
                        }
                    }
                    else {
                        // 4.2、如果事项配置了材料没有配置多情形情况
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            JSONObject materialJson = new JSONObject();
                            // 4.2.1、获取办件材料基本信息
                            materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                            materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                            materialJson.put("status", auditProjectMaterial.getStatus()); // 办件材料提交状态
                            materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                            materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid());// 材料关联的共享材料标识
                            materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                            materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                            materialJsonList.add(materialJson);
                        }
                    }
                }
                // 5、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                        Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                        int ret = ordernumb2.compareTo(ordernumb1);
                        return ret;
                    }
                });
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getScanMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getScanMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getScanMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料列表失败：" + e.getMessage(), "");
        }
    }

    /******************************************
     * 暂时无用接口
     *********************************************/
    /**
     * 获取办件基本信息接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectBaseInfo", method = RequestMethod.POST)
    public String getProjectBaseInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectBaseInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取办件编号
                String areaCode = obj.getString("areacode");
                // 2、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (auditProject != null) {
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                        dataJson.put("taskicon",
                                WebUtil.getRequestCompleteUrl(request)
                                        + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid="
                                        + auditTask.getRowguid());// 二维码内容
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("department", auditTask.getOuname());// 部门名称
                        dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        // 即办件承诺日期默认为 1天
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                            dataJson.put("promiseday", "1");// 承诺日期
                        }
                        else {
                            dataJson.put("promiseday", auditTask.getPromise_day());// 承诺日期
                        }
                        JSONObject taskElementJson = new JSONObject();
                        taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype())) ? "0" : "1");// 是否可以网上申报
                        taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                        dataJson.put("taskelement", taskElementJson);
                        JSONObject applyerJson = new JSONObject();
                        applyerJson.put("applyername", auditProject.getApplyername());
                        applyerJson.put("applyercard", auditProject.getCertnum());
                        applyerJson.put("contactname", auditProject.getContactperson());
                        applyerJson.put("contactmobile", auditProject.getContactmobile());
                        applyerJson.put("contactphone", auditProject.getContactphone());
                        applyerJson.put("contactidnum", auditProject.getContactcertnum());
                        applyerJson.put("postcode", auditProject.getContactpostcode());
                        applyerJson.put("address", auditProject.getAddress());
                        applyerJson.put("remark", auditProject.getRemark());
                        applyerJson.put("flowsn", auditProject.getFlowsn());
                        dataJson.put("applerinfo", applyerJson);
                    }
                }
                log.info("=======结束调用getProjectBaseInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件基本信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectBaseInfo接口参数：params【" + params + "】=======");
            log.info("=======getProjectBaseInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据flowsn查询办件信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/getTaskInfoByFlowsn", method = RequestMethod.POST)
    public String getTaskInfoByFlowsn(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectBaseInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");

                // TODO:1.1、获取办流水号
                String flowsn = obj.getString("flowsn");

                JSONObject result = new JSONObject();

                Record record = sendMaterials.getTaskInfoByFlowsn(flowsn);
                if (record != null) {
                    result.put("project", record.getStr("rowguid"));
                    result.put("taskguid", record.getStr("taskguid"));

                    return JsonUtils.zwdtRestReturn("1", "获取办件guid成功", result.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "办件流水号不正确！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskInfoByFlowsn接口参数：params【" + params + "】=======");
            log.info("=======getTaskInfoByFlowsn异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件信息失败：" + e.getMessage(), "");
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

    /**
     * 判断用户在某企业中的身份
     *
     * @param accountGuid
     *            用户标识
     * @param idnum
     *            用户身份证
     * @param companyId
     *            公司标识
     * @return
     */
    private String getCompanyUserLevel(String accountGuid, String idnum, String companyId) {
        // 默认用户类型为0, 1：法人，2：管理者，3：代办人
        String userLevel = null;
        // 1、根据用户身份证和公司标识获取企业基本信息
        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                .getCompanyByCompanyIdAndIdnum(companyId, idnum).getResult();
        if (auditRsCompanyBaseinfo != null) {
            // 2、如果个人有对应的企业信息，则为法人
            userLevel = ZwdtConstant.GRANT_LEGEL_PERSION;
        }
        else {
            // 3、获取用户授权基本信息
            AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                    .getGrantByBsqUserGuidAndCompanyId(companyId, accountGuid).getResult();
            if (auditOnlineCompanyGrant != null) {
                userLevel = auditOnlineCompanyGrant.getBsqlevel();
            }
        }
        return userLevel;
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/getProjectFormByProjectguid", method = RequestMethod.POST)
    public String getProjectFormByProjectguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectFormByProjectguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");

                // TODO:1.1、获取办流水号
                String proectguid = obj.getString("projectguid");

                JSONObject applyerJson = new JSONObject();

                AuditProjectFormSgxkz record = iAuditProjectFormSgxkzService.getRecordBy(proectguid);

                if (record != null) {
                    // 项目代码
                    applyerJson.put("XiangMuDaiMa", record.getStr("XiangMuDaiMa"));
                    // 项目编号
                    applyerJson.put("XiangMuBianHao", record.getStr("XiangMuBianHao"));
                    // 项目名称
                    applyerJson.put("xiangmumingchen", record.getStr("xiangmumingchen"));
                    // 项目分类
                    applyerJson.put("XiangMuFenLei", getSelectItemList("项目分类", record.getStr("XiangMuFenLei")));
                    // applyerJson.put("XiangMuFenLei",
                    // record.getStr("XiangMuFenLei"));
                    // 工程投资性质
                    applyerJson.put("InvPropertyNum", getSelectItemList("工程投资性质", record.getStr("InvPropertyNum")));
                    // applyerJson.put("InvPropertyNum",
                    // record.getStr("InvPropertyNum"));
                    // 合同工期（天）
                    applyerJson.put("BargainDays", record.getStr("BargainDays"));
                    // 项目所在省份
                    applyerJson.put("XiangMuSuoZaiShengFen", record.getStr("XiangMuSuoZaiShengFen"));
                    // 项目所在城市
                    applyerJson.put("XiangMuSuoZaiChengShi", record.getStr("XiangMuSuoZaiChengShi"));
                    // 项目所在区县
                    applyerJson.put("XiangMuSuoZaiQuXian",
                            getSelectItemList("所在区县", record.getStr("XiangMuSuoZaiQuXian")));
                    // applyerJson.put("XiangMuSuoZaiQuXian",
                    // record.getStr("XiangMuSuoZaiQuXian"));
                    // 项目地点
                    applyerJson.put("XiangMuDiDian", record.getStr("XiangMuDiDian"));
                    // 立项批复机关
                    applyerJson.put("LiXiangPiFuJiGuan", record.getStr("LiXiangPiFuJiGuan"));
                    // 立项批复时间
                    applyerJson.put("lixiangpifushijian", record.getStr("lixiangpifushijian"));
                    // 中标通知书编号
                    applyerJson.put("ZhongBiaoTongZhiShuBianHao", record.getStr("ZhongBiaoTongZhiShuBianHao"));
                    // 施工图审查项目编号
                    applyerJson.put("ShiGongTuShenChaXiangMuBianHao", record.getStr("ShiGongTuShenChaXiangMuBianHao"));
                    // 建设用地规划许可证编号
                    applyerJson.put("JianSheYongDiGuiHuaXuKeZhen", record.getStr("JianSheYongDiGuiHuaXuKeZhen"));
                    // 建设工程规划许可证编号
                    applyerJson.put("JianSheGongChengGuiHuaXuKeZhen", record.getStr("JianSheGongChengGuiHuaXuKeZhen"));
                    // 资金来源
                    applyerJson.put("ZiJinLaiYuan", record.getStr("ZiJinLaiYuan"));
                    // 总投资
                    applyerJson.put("xiangmuzongtouziwanyuan", record.getStr("xiangmuzongtouziwanyuan"));
                    // 总建筑面积
                    applyerJson.put("xiangmuzongjianzhumianjipingfa", record.getStr("xiangmuzongjianzhumianjipingfa"));
                    // 地上建筑面积
                    applyerJson.put("xiangmudishangjianzhumianjipin", record.getStr("xiangmudishangjianzhumianjipin"));
                    // 地下建筑面积
                    applyerJson.put("xiangmudixiajianzhumianjipingf", record.getStr("xiangmudixiajianzhumianjipingf"));
                    // 总长度
                    applyerJson.put("zongchangdumi1", record.getStr("zongchangdumi1"));
                    // 地上长度
                    applyerJson.put("xiangmudishangchangdumi", record.getStr("xiangmudishangchangdumi"));
                    // 地下长度
                    applyerJson.put("xiangmudixiachangdumi", record.getStr("xiangmudixiachangdumi"));
                    // 跨度
                    applyerJson.put("xiangmukuadumi", record.getStr("xiangmukuadumi"));
                    // 工程用途
                    applyerJson.put("GongChengYongTu", getSelectItemList("工程用途", record.getStr("GongChengYongTu")));
                    // applyerJson.put("GongChengYongTu",
                    // record.getStr("GongChengYongTu"));
                    // 建设规模
                    applyerJson.put("jiansheguimo", record.getStr("jiansheguimo"));
                    // 建设性质
                    applyerJson.put("jianshexingzhi", getSelectItemList("建设性质", record.getStr("jianshexingzhi")));
                    // applyerJson.put("jianshexingzhi",
                    // record.getStr("jianshexingzhi"));
                    // 重点项目
                    applyerJson.put("zhongdianxiangmu", getSelectItemList("重点项目", record.getStr("zhongdianxiangmu")));
                    // applyerJson.put("zhongdianxiangmu",
                    // record.getStr("zhongdianxiangmu"));
                    // 合同价格
                    applyerJson.put("hetongjiagewanyuan", record.getStr("hetongjiagewanyuan"));
                    // 合同开工日期
                    applyerJson.put("hetongkaigongriqi", record.getStr("hetongkaigongriqi"));
                    // 合同竣工日期
                    applyerJson.put("hetongjungongriqi", record.getStr("hetongjungongriqi"));
                    // 建设单位名称
                    applyerJson.put("jianshedanweimingchen", record.getStr("jianshedanweimingchen"));
                    // 建设单位统一社会信用代码
                    applyerJson.put("JianSheDanWeiTongYiSheHuiXinYo", record.getStr("JianSheDanWeiTongYiSheHuiXinYo"));
                    // 建设单位地址
                    applyerJson.put("jianshedanweidizhi", record.getStr("jianshedanweidizhi"));
                    // 建设单位所有制形式
                    applyerJson.put("jianshedanweisuoyouzhixingshi", record.getStr("jianshedanweisuoyouzhixingshi"));
                    // 建设单位电话
                    applyerJson.put("jianshedanweidianhua", record.getStr("jianshedanweidianhua"));
                    // 建设单位法定代表人
                    applyerJson.put("jianshedanweifadingdaibiaoren", record.getStr("jianshedanweifadingdaibiaoren"));
                    // 建设单位项目负责人
                    applyerJson.put("jianshedanweixiangmufuzeren", record.getStr("jianshedanweixiangmufuzeren"));
                    // 工程总承包单位
                    applyerJson.put("gongchengzongchengbaodanwei", record.getStr("gongchengzongchengbaodanwei"));
                    // 工程总承包单位统一社会信用代码
                    applyerJson.put("gongchengzongchengbaodanweixin", record.getStr("gongchengzongchengbaodanweixin"));
                    // 工程总承包单位项目经理
                    applyerJson.put("gongchengzongchengbaodanweixia", record.getStr("gongchengzongchengbaodanweixia"));
                    // 施工总承包企业
                    applyerJson.put("shigongzongchengbaoqiye", record.getStr("shigongzongchengbaoqiye"));
                    // 施工总承包企业统一社会信用代码
                    applyerJson.put("ShiGongZongChengBaoQiYeTongYiS", record.getStr("ShiGongZongChengBaoQiYeTongYiS"));
                    // 施工总承包企业项目负责人
                    applyerJson.put("shigongzongchengbaoqiyexiangmu", record.getStr("shigongzongchengbaoqiyexiangmu"));
                    // 全过程工程咨询企业
                    applyerJson.put("quanguochenggongchengzixunqiye", record.getStr("quanguochenggongchengzixunqiye"));
                    // 全过程工程咨询企业统一社会信用代码
                    applyerJson.put("QuanGuoChengGongChengZiXunXY", record.getStr("QuanGuoChengGongChengZiXunXY"));
                    // 全过程工程咨询企业项目负责人
                    applyerJson.put("QuanGuoChengGongChengZiXunFZR", record.getStr("QuanGuoChengGongChengZiXunFZR"));
                    // 勘察单位
                    applyerJson.put("kanchadanwei", record.getStr("kanchadanwei"));
                    // 勘察单位项目负责人
                    applyerJson.put("kanchadanweixiangmufuzeren", record.getStr("kanchadanweixiangmufuzeren"));
                    // 勘察单位统一社会信用代码
                    applyerJson.put("KanChaDanWeiCode", record.getStr("KanChaDanWeiCode"));
                    applyerJson.put("shejidanwei", record.getStr("shejidanwei"));
                    applyerJson.put("shejidanweixiangmufuzeren", record.getStr("shejidanweixiangmufuzeren"));
                    applyerJson.put("SheJiDanWeiCode", record.getStr("SheJiDanWeiCode"));
                    applyerJson.put("jianlidanwei", record.getStr("jianlidanwei"));
                    applyerJson.put("JianLiDanWeiCode", record.getStr("JianLiDanWeiCode"));
                    applyerJson.put("zongjianligongchengshi", record.getStr("zongjianligongchengshi"));
                    applyerJson.put("jiancedanwei", record.getStr("jiancedanwei"));
                    applyerJson.put("jiancedanweifuzeren", record.getStr("jiancedanweifuzeren"));
                    applyerJson.put("JianCeDanWeiCode", record.getStr("JianCeDanWeiCode"));
                    applyerJson.put("shentudanwei", record.getStr("shentudanwei"));
                    applyerJson.put("shentudanweixiangmufuzeren", record.getStr("shentudanweixiangmufuzeren"));
                    applyerJson.put("ShenTuDanWeiCode", record.getStr("ShenTuDanWeiCode"));
                    return JsonUtils.zwdtRestReturn("1", "获取项目信息成功", applyerJson.toString());
                }
                else {
                    applyerJson.put("XiangMuSuoZaiQuXian", getSelectItemList("所在区县", ""));
                    applyerJson.put("XiangMuFenLei", getSelectItemList("项目分类", ""));
                    applyerJson.put("InvPropertyNum", getSelectItemList("工程投资性质", ""));
                    applyerJson.put("jianshexingzhi", getSelectItemList("建设性质", ""));
                    applyerJson.put("zhongdianxiangmu", getSelectItemList("重点项目", ""));
                    applyerJson.put("GongChengYongTu", getSelectItemList("工程用途", ""));

                    return JsonUtils.zwdtRestReturn("1", "项目信息未创建！", applyerJson.toString());
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectFormByProjectguid接口参数：params【" + params + "】=======");
            log.info("=======getProjectFormByProjectguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/submitProjectForm", method = RequestMethod.POST)
    public String submitProjectForm(@RequestBody String params) {
        try {
            log.info("=======开始调用submitProjectForm接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // TODO:1.1、获取办流水号
                String projectguid = obj.getString("projectguid");

                String XiangMuDaiMa = obj.getString("XiangMuDaiMa");
                String XiangMuBianHao = obj.getString("XiangMuBianHao");
                String xiangmumingchen = obj.getString("xiangmumingchen");
                String XiangMuFenLei = obj.getString("XiangMuFenLei");
                String InvPropertyNum = obj.getString("InvPropertyNum");
                String BargainDays = obj.getString("BargainDays");
                String XiangMuSuoZaiShengFen = obj.getString("XiangMuSuoZaiShengFen");
                String XiangMuSuoZaiChengShi = obj.getString("XiangMuSuoZaiChengShi");
                String XiangMuSuoZaiQuXian = obj.getString("XiangMuSuoZaiQuXian");
                String XiangMuDiDian = obj.getString("XiangMuDiDian");
                String LiXiangPiFuJiGuan = obj.getString("LiXiangPiFuJiGuan");
                String lixiangpifushijian = obj.getString("lixiangpifushijian");
                String ZhongBiaoTongZhiShuBianHao = obj.getString("ZhongBiaoTongZhiShuBianHao");
                String ShiGongTuShenChaXiangMuBianHao = obj.getString("ShiGongTuShenChaXiangMuBianHao");
                String JianSheYongDiGuiHuaXuKeZhen = obj.getString("JianSheYongDiGuiHuaXuKeZhen");
                String JianSheGongChengGuiHuaXuKeZhen = obj.getString("JianSheGongChengGuiHuaXuKeZhen");
                String ZiJinLaiYuan = obj.getString("ZiJinLaiYuan");
                String xiangmuzongtouziwanyuan = obj.getString("xiangmuzongtouziwanyuan");
                String xiangmuzongjianzhumianjipingfa = obj.getString("xiangmuzongjianzhumianjipingfa");
                String xiangmudishangjianzhumianjipin = obj.getString("xiangmudishangjianzhumianjipin");
                String xiangmudixiajianzhumianjipingf = obj.getString("xiangmudixiajianzhumianjipingf");
                String zongchangdumi1 = obj.getString("zongchangdumi1");
                String xiangmudishangchangdumi = obj.getString("xiangmudishangchangdumi");
                String xiangmudixiachangdumi = obj.getString("xiangmudixiachangdumi");
                String xiangmukuadumi = obj.getString("xiangmukuadumi");
                String GongChengYongTu = obj.getString("GongChengYongTu");
                String jiansheguimo = obj.getString("jiansheguimo");
                String jianshexingzhi = obj.getString("jianshexingzhi");
                String zhongdianxiangmu = obj.getString("zhongdianxiangmu");
                String hetongjiagewanyuan = obj.getString("hetongjiagewanyuan");
                String hetongkaigongriqi = obj.getString("hetongkaigongriqi");
                String hetongjungongriqi = obj.getString("hetongjungongriqi");
                String jianshedanweimingchen = obj.getString("jianshedanweimingchen");
                String JianSheDanWeiTongYiSheHuiXinYo = obj.getString("JianSheDanWeiTongYiSheHuiXinYo");
                String jianshedanweidizhi = obj.getString("jianshedanweidizhi");
                String jianshedanweisuoyouzhixingshi = obj.getString("jianshedanweisuoyouzhixingshi");
                String jianshedanweidianhua = obj.getString("jianshedanweidianhua");
                String jianshedanweifadingdaibiaoren = obj.getString("jianshedanweifadingdaibiaoren");
                String jianshedanweixiangmufuzeren = obj.getString("jianshedanweixiangmufuzeren");
                String gongchengzongchengbaodanwei = obj.getString("gongchengzongchengbaodanwei");
                String gongchengzongchengbaodanweixin = obj.getString("gongchengzongchengbaodanweixin");
                String gongchengzongchengbaodanweixia = obj.getString("gongchengzongchengbaodanweixia");
                String shigongzongchengbaoqiye = obj.getString("shigongzongchengbaoqiye");
                String ShiGongZongChengBaoQiYeTongYiS = obj.getString("ShiGongZongChengBaoQiYeTongYiS");
                String shigongzongchengbaoqiyexiangmu = obj.getString("shigongzongchengbaoqiyexiangmu");
                String quanguochenggongchengzixunqiye = obj.getString("quanguochenggongchengzixunqiye");
                String QuanGuoChengGongChengZiXunXY = obj.getString("QuanGuoChengGongChengZiXunXY");
                String QuanGuoChengGongChengZiXunFZR = obj.getString("QuanGuoChengGongChengZiXunFZR");
                String kanchadanwei = obj.getString("kanchadanwei");
                String kanchadanweixiangmufuzeren = obj.getString("kanchadanweixiangmufuzeren");
                String KanChaDanWeiCode = obj.getString("KanChaDanWeiCode");
                String shejidanwei = obj.getString("shejidanwei");
                String shejidanweixiangmufuzeren = obj.getString("shejidanweixiangmufuzeren");
                String SheJiDanWeiCode = obj.getString("SheJiDanWeiCode");
                String jianlidanwei = obj.getString("jianlidanwei");
                String JianLiDanWeiCode = obj.getString("JianLiDanWeiCode");
                String zongjianligongchengshi = obj.getString("zongjianligongchengshi");
                String jiancedanwei = obj.getString("jiancedanwei");
                String jiancedanweifuzeren = obj.getString("jiancedanweifuzeren");
                String JianCeDanWeiCode = obj.getString("JianCeDanWeiCode");
                String shentudanwei = obj.getString("shentudanwei");
                String shentudanweixiangmufuzeren = obj.getString("shentudanweixiangmufuzeren");
                String ShenTuDanWeiCode = obj.getString("ShenTuDanWeiCode");
                AuditProjectFormSgxkz record = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
                if (record == null) {
                    record = new AuditProjectFormSgxkz();
                    record.setRowguid(UUID.randomUUID().toString());
                    record.setOperateusername(auditProject.getApplyername());
                    record.setOperatedate(new Date());
                    record.setProjectguid(projectguid);
                    // 项目代码
                    record.set("XiangMuDaiMa", XiangMuDaiMa);
                    // 项目编号
                    record.set("XiangMuBianHao", XiangMuBianHao);
                    // 项目名称
                    record.setXiangmumingchen(xiangmumingchen);
                    // 项目分类
                    record.setXiangmufenlei(XiangMuFenLei);
                    // 工程投资性质
                    record.set("InvPropertyNum", InvPropertyNum);
                    // 合同工期（天）
                    record.set("BargainDays", BargainDays);
                    // 项目所在省份
                    record.setXiangmusuozaishengfen(XiangMuSuoZaiShengFen);
                    // 项目所在城市
                    record.setXiangmusuozaichengshi(XiangMuSuoZaiChengShi);
                    // 项目所在区县
                    record.setXiangmusuozaiquxian(XiangMuSuoZaiQuXian);
                    // 项目地点
                    record.setXiangmudidian(XiangMuDiDian);
                    // 立项批复机关
                    record.setLiXiangPiFuJiGuan(LiXiangPiFuJiGuan);
                    // 立项批复时间
                    record.set("lixiangpifushijian", lixiangpifushijian);
                    // 中标通知书编号
                    record.setZhongBiaoTongZhiShuBianHao(ZhongBiaoTongZhiShuBianHao);
                    // 施工图审查项目编号
                    record.setShiGongTuShenChaXiangMuBianHao(ShiGongTuShenChaXiangMuBianHao);
                    // 建设用地规划许可证编号
                    record.set("JianSheYongDiGuiHuaXuKeZhen", JianSheYongDiGuiHuaXuKeZhen);
                    // 建设工程规划许可证编号
                    record.setJianSheGongChengGuiHuaXuKeZhen(JianSheGongChengGuiHuaXuKeZhen);
                    // 资金来源
                    record.setZijinlaiyuan(ZiJinLaiYuan);
                    // 总投资
                    record.setXiangmuzongtouziwanyuan(xiangmuzongtouziwanyuan);
                    // 总建筑面积
                    record.setXiangmuzongjianzhumianjipingfa(xiangmuzongjianzhumianjipingfa);
                    // 地上建筑面积
                    record.setXiangmudishangjianzhumianjipin(xiangmudishangjianzhumianjipin);
                    // 地下建筑面积
                    record.setXiangmudixiajianzhumianjipingf(xiangmudixiajianzhumianjipingf);
                    // 总长度
                    record.setZongchangdumi1(zongchangdumi1);
                    // 地上长度
                    record.setXiangmudishangchangdumi(xiangmudishangchangdumi);
                    // 地下长度
                    record.setXiangmudixiachangdumi(xiangmudixiachangdumi);
                    // 跨度
                    record.setXiangmukuadumi(xiangmukuadumi);
                    // 工程用途
                    record.setGongChengYongTu(GongChengYongTu);
                    // 建设规模
                    record.setJiansheguimo(jiansheguimo);
                    // 建设性质
                    record.setJianshexingzhi(jianshexingzhi);
                    // 重点项目
                    record.setZhongdianxiangmu(zhongdianxiangmu);
                    // 合同价格
                    record.setHetongjiagewanyuan(hetongjiagewanyuan);
                    // 合同开工日期
                    record.setHetongkaigongriqi(hetongkaigongriqi);
                    // 合同竣工日期
                    record.setHetongjungongriqi(hetongjungongriqi);
                    // 建设单位名称
                    record.setJianshedanweimingchen(jianshedanweimingchen);
                    // 建设单位统一社会信用代码
                    record.setJianSheDanWeiTongYiSheHuiXinYo(JianSheDanWeiTongYiSheHuiXinYo);
                    // 建设单位地址
                    record.setJianshedanweidizhi(jianshedanweidizhi);
                    // 建设单位所有制形式
                    record.setJianshedanweisuoyouzhixingshi(jianshedanweisuoyouzhixingshi);
                    // 建设单位电话
                    record.setJianshedanweidianhua(jianshedanweidianhua);
                    // 建设单位法定代表人
                    record.setJianshedanweifadingdaibiaoren(jianshedanweifadingdaibiaoren);
                    // 建设单位项目负责人
                    record.setJianshedanweixiangmufuzeren(jianshedanweixiangmufuzeren);
                    // 工程总承包单位
                    record.setGongchengzongchengbaodanwei(gongchengzongchengbaodanwei);
                    // 工程总承包单位统一社会信用代码
                    record.set("gongchengzongchengbaodanweixin", gongchengzongchengbaodanweixin);
                    // 工程总承包单位项目经理
                    record.setGongchengzongchengbaodanweixia(gongchengzongchengbaodanweixia);
                    // 施工总承包企业
                    record.setShigongzongchengbaoqiye(shigongzongchengbaoqiye);
                    // 施工总承包企业统一社会信用代码
                    record.setShiGongZongChengBaoQiYeTongYiS(ShiGongZongChengBaoQiYeTongYiS);
                    // 施工总承包企业项目负责人
                    record.setShigongzongchengbaoqiyexiangmu(shigongzongchengbaoqiyexiangmu);
                    // 全过程工程咨询企业
                    record.setQuanguochenggongchengzixunqiye(quanguochenggongchengzixunqiye);
                    // 全过程工程咨询企业统一社会信用代码
                    record.setQuanGuoChengGongChengZiXunXY(QuanGuoChengGongChengZiXunXY);
                    // 全过程工程咨询企业项目负责人
                    record.setQuanGuoChengGongChengZiXunFZR(QuanGuoChengGongChengZiXunFZR);
                    // 勘察单位
                    record.setKanchadanwei(kanchadanwei);
                    // 勘察单位项目负责人
                    record.setKanchadanweixiangmufuzeren(kanchadanweixiangmufuzeren);
                    // 勘察单位统一社会信用代码
                    record.setKanChaDanWeiCode(KanChaDanWeiCode);
                    record.setShejidanwei(shejidanwei);
                    record.setShejidanweixiangmufuzeren(shejidanweixiangmufuzeren);
                    record.setSheJiDanWeiCode(SheJiDanWeiCode);
                    record.setJianlidanwei(jianlidanwei);
                    record.setJianLiDanWeiCode(JianLiDanWeiCode);
                    record.setZongjianligongchengshi(zongjianligongchengshi);
                    record.setJiancedanwei(jiancedanwei);
                    record.setJianCeDanWeiFuZeRen(jiancedanweifuzeren);
                    record.setJianCeDanWeiCode(JianCeDanWeiCode);
                    record.setShentudanwei(shentudanwei);
                    record.setShentudanweixiangmufuzeren(shentudanweixiangmufuzeren);
                    record.setShenTuDanWeiCode(ShenTuDanWeiCode);
                    iAuditProjectFormSgxkzService.insert(record);
                    return JsonUtils.zwdtRestReturn("1", "提交项目信息成功", "");
                }
                else {
                    record.setOperateusername(auditProject.getApplyername());
                    record.setOperatedate(new Date());
                    record.setProjectguid(projectguid);
                    // 项目代码
                    record.set("XiangMuDaiMa", XiangMuDaiMa);
                    // 项目编号
                    record.set("XiangMuBianHao", XiangMuBianHao);
                    // 项目名称
                    record.setXiangmumingchen(xiangmumingchen);
                    // 项目分类
                    record.setXiangmufenlei(XiangMuFenLei);
                    // 工程投资性质
                    record.set("InvPropertyNum", InvPropertyNum);
                    // 合同工期（天）
                    record.set("BargainDays", BargainDays);
                    // 项目所在省份
                    record.setXiangmusuozaishengfen(XiangMuSuoZaiShengFen);
                    // 项目所在城市
                    record.setXiangmusuozaichengshi(XiangMuSuoZaiChengShi);
                    // 项目所在区县
                    record.setXiangmusuozaiquxian(XiangMuSuoZaiQuXian);
                    // 项目地点
                    record.setXiangmudidian(XiangMuDiDian);
                    // 立项批复机关
                    record.setLiXiangPiFuJiGuan(LiXiangPiFuJiGuan);
                    // 立项批复时间
                    record.set("lixiangpifushijian", lixiangpifushijian);
                    // 中标通知书编号
                    record.setZhongBiaoTongZhiShuBianHao(ZhongBiaoTongZhiShuBianHao);
                    // 施工图审查项目编号
                    record.setShiGongTuShenChaXiangMuBianHao(ShiGongTuShenChaXiangMuBianHao);
                    // 建设用地规划许可证编号
                    record.set("JianSheYongDiGuiHuaXuKeZhen", JianSheYongDiGuiHuaXuKeZhen);
                    // 建设工程规划许可证编号
                    record.setJianSheGongChengGuiHuaXuKeZhen(JianSheGongChengGuiHuaXuKeZhen);
                    // 资金来源
                    record.setZijinlaiyuan(ZiJinLaiYuan);
                    // 总投资
                    record.setXiangmuzongtouziwanyuan(xiangmuzongtouziwanyuan);
                    // 总建筑面积
                    record.setXiangmuzongjianzhumianjipingfa(xiangmuzongjianzhumianjipingfa);
                    // 地上建筑面积
                    record.setXiangmudishangjianzhumianjipin(xiangmudishangjianzhumianjipin);
                    // 地下建筑面积
                    record.setXiangmudixiajianzhumianjipingf(xiangmudixiajianzhumianjipingf);
                    // 总长度
                    record.setZongchangdumi1(zongchangdumi1);
                    // 地上长度
                    record.setXiangmudishangchangdumi(xiangmudishangchangdumi);
                    // 地下长度
                    record.setXiangmudixiachangdumi(xiangmudixiachangdumi);
                    // 跨度
                    record.setXiangmukuadumi(xiangmukuadumi);
                    // 工程用途
                    record.setGongChengYongTu(GongChengYongTu);
                    // 建设规模
                    record.setJiansheguimo(jiansheguimo);
                    // 建设性质
                    record.setJianshexingzhi(jianshexingzhi);
                    // 重点项目
                    record.setZhongdianxiangmu(zhongdianxiangmu);
                    // 合同价格
                    record.setHetongjiagewanyuan(hetongjiagewanyuan);
                    // 合同开工日期
                    record.setHetongkaigongriqi(hetongkaigongriqi);
                    // 合同竣工日期
                    record.setHetongjungongriqi(hetongjungongriqi);
                    // 建设单位名称
                    record.setJianshedanweimingchen(jianshedanweimingchen);
                    // 建设单位统一社会信用代码
                    record.setJianSheDanWeiTongYiSheHuiXinYo(JianSheDanWeiTongYiSheHuiXinYo);
                    // 建设单位地址
                    record.setJianshedanweidizhi(jianshedanweidizhi);
                    // 建设单位所有制形式
                    record.setJianshedanweisuoyouzhixingshi(jianshedanweisuoyouzhixingshi);
                    // 建设单位电话
                    record.setJianshedanweidianhua(jianshedanweidianhua);
                    // 建设单位法定代表人
                    record.setJianshedanweifadingdaibiaoren(jianshedanweifadingdaibiaoren);
                    // 建设单位项目负责人
                    record.setJianshedanweixiangmufuzeren(jianshedanweixiangmufuzeren);
                    // 工程总承包单位
                    record.setGongchengzongchengbaodanwei(gongchengzongchengbaodanwei);
                    // 工程总承包单位统一社会信用代码
                    record.set("gongchengzongchengbaodanweixin", gongchengzongchengbaodanweixin);
                    // 工程总承包单位项目经理
                    record.setGongchengzongchengbaodanweixia(gongchengzongchengbaodanweixia);
                    // 施工总承包企业
                    record.setShigongzongchengbaoqiye(shigongzongchengbaoqiye);
                    // 施工总承包企业统一社会信用代码
                    record.setShiGongZongChengBaoQiYeTongYiS(ShiGongZongChengBaoQiYeTongYiS);
                    // 施工总承包企业项目负责人
                    record.setShigongzongchengbaoqiyexiangmu(shigongzongchengbaoqiyexiangmu);
                    // 全过程工程咨询企业
                    record.setQuanguochenggongchengzixunqiye(quanguochenggongchengzixunqiye);
                    // 全过程工程咨询企业统一社会信用代码
                    record.setQuanGuoChengGongChengZiXunXY(QuanGuoChengGongChengZiXunXY);
                    // 全过程工程咨询企业项目负责人
                    record.setQuanGuoChengGongChengZiXunFZR(QuanGuoChengGongChengZiXunFZR);
                    // 勘察单位
                    record.setKanchadanwei(kanchadanwei);
                    // 勘察单位项目负责人
                    record.setKanchadanweixiangmufuzeren(kanchadanweixiangmufuzeren);
                    // 勘察单位统一社会信用代码
                    record.setKanChaDanWeiCode(KanChaDanWeiCode);
                    record.setShejidanwei(shejidanwei);
                    record.setShejidanweixiangmufuzeren(shejidanweixiangmufuzeren);
                    record.setSheJiDanWeiCode(SheJiDanWeiCode);
                    record.setJianlidanwei(jianlidanwei);
                    record.setJianLiDanWeiCode(JianLiDanWeiCode);
                    record.setZongjianligongchengshi(zongjianligongchengshi);
                    record.setJiancedanwei(jiancedanwei);
                    record.setJianCeDanWeiFuZeRen(jiancedanweifuzeren);
                    record.setJianCeDanWeiCode(JianCeDanWeiCode);
                    record.setShentudanwei(shentudanwei);
                    record.setShentudanweixiangmufuzeren(shentudanweixiangmufuzeren);
                    record.setShenTuDanWeiCode(ShenTuDanWeiCode);
                    iAuditProjectFormSgxkzService.update(record);
                    return JsonUtils.zwdtRestReturn("1", "提交项目信息成功", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitProjectForm接口参数：params【" + params + "】=======");
            log.info("=======submitProjectForm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存项目信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/saveProjectFormDantiByProjectguid", method = RequestMethod.POST)
    public String saveProjectFormDantiByProjectguid(@RequestBody String params) {
        try {
            log.info("=======开始调用saveProjectFormDantiByProjectguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject result = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // TODO:1.1、获取办流水号
                String projectguid = obj.getString("projectguid");
                AuditProjectFormSgxkzDanti auditProjectFormSgxkzDanti = new AuditProjectFormSgxkzDanti();

                String dantijiangouzhuwumingchen = obj.getString("dantijiangouzhuwumingchen");
                String dishangjianzhumianjipingfangmi = obj.getString("dishangjianzhumianjipingfangmi");
                String dishangchangdumi = obj.getString("dishangchangdumi");
                String dishangcengshu = obj.getString("dishangcengshu");
                String dixiajianzhumianjipingfangmi = obj.getString("dixiajianzhumianjipingfangmi");
                String dixiachangdumi = obj.getString("dixiachangdumi");
                String dixiacengshu = obj.getString("dixiacengshu");
                String jianzhumianjipingfangmi = obj.getString("jianzhumianjipingfangmi");
                String changdumi = obj.getString("changdumi");
                String kuadumi = obj.getString("kuadumi");
                String SeismicIntensityScale = obj.getString("SeismicIntensityScale");
                String ISSuperHightBuilding = obj.getString("ISSuperHightBuilding");

                auditProjectFormSgxkzDanti.setRowguid(UUID.randomUUID().toString());
                auditProjectFormSgxkzDanti.setOperatedate(new Date());
                auditProjectFormSgxkzDanti.setProjectguid(projectguid);
                auditProjectFormSgxkzDanti.setDantijiangouzhuwumingchen(dantijiangouzhuwumingchen);
                auditProjectFormSgxkzDanti.setDishangjianzhumianjipingfangmi(dishangjianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setDishangchangdumi(dishangchangdumi);
                auditProjectFormSgxkzDanti.setDishangcengshu(dishangcengshu);
                auditProjectFormSgxkzDanti.setDixiajianzhumianjipingfangmi(dixiajianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setDixiachangdumi(dixiachangdumi);
                auditProjectFormSgxkzDanti.setDixiacengshu(dixiacengshu);
                auditProjectFormSgxkzDanti.setJianzhumianjipingfangmi(jianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setChangdumi(changdumi);
                auditProjectFormSgxkzDanti.setKuadumi(kuadumi);
                auditProjectFormSgxkzDanti.set("SeismicIntensityScale", SeismicIntensityScale);
                auditProjectFormSgxkzDanti.set("ISSuperHightBuilding", ISSuperHightBuilding);
                iAuditProjectFormSgxkzDantiService.insert(auditProjectFormSgxkzDanti);
                result.put("projectguid", projectguid);
                return JsonUtils.zwdtRestReturn("1", "保存项目单体信息成功", result.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveProjectFormDantiByProjectguid接口参数：params【" + params + "】=======");
            log.info("=======saveProjectFormDantiByProjectguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存项目单体信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/updateProjectFormDantiByProjectguid", method = RequestMethod.POST)
    public String updateProjectFormDantiByProjectguid(@RequestBody String params) {
        try {
            log.info("=======开始调用updateProjectFormDantiByProjectguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");

                String rowguid = obj.getString("guid");

                AuditProjectFormSgxkzDanti auditProjectFormSgxkzDanti = iAuditProjectFormSgxkzService
                        .getDantiByRowguid(rowguid);

                String dantijiangouzhuwumingchen = obj.getString("dantijiangouzhuwumingchen");
                String dishangjianzhumianjipingfangmi = obj.getString("dishangjianzhumianjipingfangmi");
                String dishangchangdumi = obj.getString("dishangchangdumi");
                String dishangcengshu = obj.getString("dishangcengshu");
                String dixiajianzhumianjipingfangmi = obj.getString("dixiajianzhumianjipingfangmi");
                String dixiachangdumi = obj.getString("dixiachangdumi");
                String dixiacengshu = obj.getString("dixiacengshu");
                String jianzhumianjipingfangmi = obj.getString("jianzhumianjipingfangmi");
                String changdumi = obj.getString("changdumi");
                String kuadumi = obj.getString("kuadumi");
                String SeismicIntensityScale = obj.getString("SeismicIntensityScale");
                String ISSuperHightBuilding = obj.getString("ISSuperHightBuilding");

                auditProjectFormSgxkzDanti.setOperatedate(new Date());
                auditProjectFormSgxkzDanti.setDantijiangouzhuwumingchen(dantijiangouzhuwumingchen);
                auditProjectFormSgxkzDanti.setDishangjianzhumianjipingfangmi(dishangjianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setDishangchangdumi(dishangchangdumi);
                auditProjectFormSgxkzDanti.setDishangcengshu(dishangcengshu);
                auditProjectFormSgxkzDanti.setDixiajianzhumianjipingfangmi(dixiajianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setDixiachangdumi(dixiachangdumi);
                auditProjectFormSgxkzDanti.setDixiacengshu(dixiacengshu);
                auditProjectFormSgxkzDanti.setJianzhumianjipingfangmi(jianzhumianjipingfangmi);
                auditProjectFormSgxkzDanti.setChangdumi(changdumi);
                auditProjectFormSgxkzDanti.setKuadumi(kuadumi);
                auditProjectFormSgxkzDanti.set("SeismicIntensityScale", SeismicIntensityScale);
                auditProjectFormSgxkzDanti.set("ISSuperHightBuilding", ISSuperHightBuilding);
                iAuditProjectFormSgxkzDantiService.update(auditProjectFormSgxkzDanti);
                return JsonUtils.zwdtRestReturn("1", "修改项目单体信息成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateProjectFormDantiByProjectguid接口参数：params【" + params + "】=======");
            log.info("=======updateProjectFormDantiByProjectguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改项目单体信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/deleteProjectFormDanti", method = RequestMethod.POST)
    public String deleteProjectFormDanti(@RequestBody String params) {
        try {
            log.info("=======开始调用deleteProjectFormDanti接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String rowguid = obj.getString("guid");
                iAuditProjectFormSgxkzDantiService.deleteByGuid(rowguid);
                return JsonUtils.zwdtRestReturn("1", "删除项目单体信息成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteProjectFormDanti接口参数：params【" + params + "】=======");
            log.info("=======deleteProjectFormDanti异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "删除项目单体信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/initProjectFormDanti", method = RequestMethod.POST)
    public String initProjectFormDanti(@RequestBody String params) {
        try {
            log.info("=======开始调用initProjectFormDanti接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // TODO:1.1、获取办流水号
                String proectguid = obj.getString("proectguid");

                String rowguid = obj.getString("guid");

                AuditProjectFormSgxkzDanti danti = iAuditProjectFormSgxkzService.getDantiByRowguid(rowguid);
                JSONObject applyerJson = new JSONObject();
                if (danti == null) {
                    danti = new AuditProjectFormSgxkzDanti();
                    applyerJson.put("guid", proectguid);
                    applyerJson.put("dantijiangouzhuwumingchen", danti.getDantijiangouzhuwumingchen());
                    applyerJson.put("dishangjianzhumianjipingfangmi", danti.getDishangjianzhumianjipingfangmi());
                    applyerJson.put("dishangchangdumi", danti.getDishangchangdumi());
                    applyerJson.put("dishangcengshu", danti.getDishangcengshu());
                    applyerJson.put("dixiajianzhumianjipingfangmi", danti.getDixiajianzhumianjipingfangmi());
                    applyerJson.put("dixiachangdumi", danti.getDixiachangdumi());
                    applyerJson.put("dixiacengshu", danti.getDixiacengshu());
                    applyerJson.put("jianzhumianjipingfangmi", danti.getJianzhumianjipingfangmi());
                    applyerJson.put("changdumi", danti.getChangdumi());
                    applyerJson.put("kuadumi", danti.getKuadumi());
                    applyerJson.put("SeismicIntensityScale", getSelectItemList("抗震设防烈度", ""));
                    // applyerJson.put("SeismicIntensityScale",
                    // danti.getStr("SeismicIntensityScale"));
                    applyerJson.put("ISSuperHightBuilding", danti.getStr("ISSuperHightBuilding"));
                    return JsonUtils.zwdtRestReturn("1", "初始化单体表成功！", applyerJson.toString());
                }
                else {
                    applyerJson.put("guid", proectguid);
                    applyerJson.put("dantijiangouzhuwumingchen", danti.getDantijiangouzhuwumingchen());
                    applyerJson.put("dishangjianzhumianjipingfangmi", danti.getDishangjianzhumianjipingfangmi());
                    applyerJson.put("dishangchangdumi", danti.getDishangchangdumi());
                    applyerJson.put("dishangcengshu", danti.getDishangcengshu());
                    applyerJson.put("dixiajianzhumianjipingfangmi", danti.getDixiajianzhumianjipingfangmi());
                    applyerJson.put("dixiachangdumi", danti.getDixiachangdumi());
                    applyerJson.put("dixiacengshu", danti.getDixiacengshu());
                    applyerJson.put("jianzhumianjipingfangmi", danti.getJianzhumianjipingfangmi());
                    applyerJson.put("changdumi", danti.getChangdumi());
                    applyerJson.put("kuadumi", danti.getKuadumi());
                    applyerJson.put("SeismicIntensityScale",
                            getSelectItemList("抗震设防烈度", danti.getStr("SeismicIntensityScale")));
                    // applyerJson.put("SeismicIntensityScale",
                    // danti.getStr("SeismicIntensityScale"));
                    applyerJson.put("ISSuperHightBuilding", danti.getStr("ISSuperHightBuilding"));
                    return JsonUtils.zwdtRestReturn("1", "查询单体列表成功！", applyerJson.toString());
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initProjectFormDanti接口参数：params【" + params + "】=======");
            log.info("=======initProjectFormDanti异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "单体列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/getProjectFormDantiByProjectguid", method = RequestMethod.POST)
    public String getProjectFormDantiByProjectguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectFormDantiByProjectguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");

                // TODO:1.1、获取办流水号
                String proectguid = obj.getString("projectguid");

                JSONObject dataJson = new JSONObject();

                List<AuditProjectFormSgxkzDanti> list = iAuditProjectFormSgxkzService.findDantiList(proectguid);

                dataJson.put("items", list);
                return JsonUtils.zwdtRestReturn("1", "查询单体列表成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectFormDantiByProjectguid接口参数：params【" + params + "】=======");
            log.info("=======getProjectFormDantiByProjectguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "单体列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据projectguid查询项目信息 @author male @date 2019年10月19日
     *               下午6:42:21 @return String 返回类型 @throws
     */
    @RequestMapping(value = "/saveProjectForm", method = RequestMethod.POST)
    public String saveProjectForm(@RequestBody String params) {
        try {
            log.info("=======开始调用saveProjectForm接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // TODO:1.1、获取办流水号
                String projectguid = obj.getString("projectguid");

                String XiangMuDaiMa = obj.getString("XiangMuDaiMa");
                String XiangMuBianHao = obj.getString("XiangMuBianHao");
                String xiangmumingchen = obj.getString("xiangmumingchen");
                String XiangMuFenLei = obj.getString("XiangMuFenLei");
                String InvPropertyNum = obj.getString("InvPropertyNum");
                String BargainDays = obj.getString("BargainDays");
                String XiangMuSuoZaiShengFen = obj.getString("XiangMuSuoZaiShengFen");
                String XiangMuSuoZaiChengShi = obj.getString("XiangMuSuoZaiChengShi");
                String XiangMuSuoZaiQuXian = obj.getString("XiangMuSuoZaiQuXian");
                String XiangMuDiDian = obj.getString("XiangMuDiDian");
                String LiXiangPiFuJiGuan = obj.getString("LiXiangPiFuJiGuan");
                String lixiangpifushijian = obj.getString("lixiangpifushijian");
                String ZhongBiaoTongZhiShuBianHao = obj.getString("ZhongBiaoTongZhiShuBianHao");
                String ShiGongTuShenChaXiangMuBianHao = obj.getString("ShiGongTuShenChaXiangMuBianHao");
                String JianSheYongDiGuiHuaXuKeZhen = obj.getString("JianSheYongDiGuiHuaXuKeZhen");
                String JianSheGongChengGuiHuaXuKeZhen = obj.getString("JianSheGongChengGuiHuaXuKeZhen");
                String ZiJinLaiYuan = obj.getString("ZiJinLaiYuan");
                String xiangmuzongtouziwanyuan = obj.getString("xiangmuzongtouziwanyuan");
                String xiangmuzongjianzhumianjipingfa = obj.getString("xiangmuzongjianzhumianjipingfa");
                String xiangmudishangjianzhumianjipin = obj.getString("xiangmudishangjianzhumianjipin");
                String xiangmudixiajianzhumianjipingf = obj.getString("xiangmudixiajianzhumianjipingf");
                String zongchangdumi1 = obj.getString("zongchangdumi1");
                String xiangmudishangchangdumi = obj.getString("xiangmudishangchangdumi");
                String xiangmudixiachangdumi = obj.getString("xiangmudixiachangdumi");
                String xiangmukuadumi = obj.getString("xiangmukuadumi");
                String GongChengYongTu = obj.getString("GongChengYongTu");
                String jiansheguimo = obj.getString("jiansheguimo");
                String jianshexingzhi = obj.getString("jianshexingzhi");
                String zhongdianxiangmu = obj.getString("zhongdianxiangmu");
                String hetongjiagewanyuan = obj.getString("hetongjiagewanyuan");
                String hetongkaigongriqi = obj.getString("hetongkaigongriqi");
                String hetongjungongriqi = obj.getString("hetongjungongriqi");
                String jianshedanweimingchen = obj.getString("jianshedanweimingchen");
                String JianSheDanWeiTongYiSheHuiXinYo = obj.getString("JianSheDanWeiTongYiSheHuiXinYo");
                String jianshedanweidizhi = obj.getString("jianshedanweidizhi");
                String jianshedanweisuoyouzhixingshi = obj.getString("jianshedanweisuoyouzhixingshi");
                String jianshedanweidianhua = obj.getString("jianshedanweidianhua");
                String jianshedanweifadingdaibiaoren = obj.getString("jianshedanweifadingdaibiaoren");
                String jianshedanweixiangmufuzeren = obj.getString("jianshedanweixiangmufuzeren");
                String gongchengzongchengbaodanwei = obj.getString("gongchengzongchengbaodanwei");
                String gongchengzongchengbaodanweixin = obj.getString("gongchengzongchengbaodanweixin");
                String gongchengzongchengbaodanweixia = obj.getString("gongchengzongchengbaodanweixia");
                String shigongzongchengbaoqiye = obj.getString("shigongzongchengbaoqiye");
                String ShiGongZongChengBaoQiYeTongYiS = obj.getString("ShiGongZongChengBaoQiYeTongYiS");
                String shigongzongchengbaoqiyexiangmu = obj.getString("shigongzongchengbaoqiyexiangmu");
                String quanguochenggongchengzixunqiye = obj.getString("quanguochenggongchengzixunqiye");
                String QuanGuoChengGongChengZiXunXY = obj.getString("QuanGuoChengGongChengZiXunXY");
                String QuanGuoChengGongChengZiXunFZR = obj.getString("QuanGuoChengGongChengZiXunFZR");
                String kanchadanwei = obj.getString("kanchadanwei");
                String kanchadanweixiangmufuzeren = obj.getString("kanchadanweixiangmufuzeren");
                String KanChaDanWeiCode = obj.getString("KanChaDanWeiCode");
                String shejidanwei = obj.getString("shejidanwei");
                String shejidanweixiangmufuzeren = obj.getString("shejidanweixiangmufuzeren");
                String SheJiDanWeiCode = obj.getString("SheJiDanWeiCode");
                String jianlidanwei = obj.getString("jianlidanwei");
                String JianLiDanWeiCode = obj.getString("JianLiDanWeiCode");
                String zongjianligongchengshi = obj.getString("zongjianligongchengshi");
                String jiancedanwei = obj.getString("jiancedanwei");
                String jiancedanweifuzeren = obj.getString("jiancedanweifuzeren");
                String JianCeDanWeiCode = obj.getString("JianCeDanWeiCode");
                String shentudanwei = obj.getString("shentudanwei");
                String shentudanweixiangmufuzeren = obj.getString("shentudanweixiangmufuzeren");
                String ShenTuDanWeiCode = obj.getString("ShenTuDanWeiCode");
                AuditProjectFormSgxkz record = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
                AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
                if (record == null) {
                    record = new AuditProjectFormSgxkz();
                    record.setRowguid(UUID.randomUUID().toString());
                    record.setOperateusername(auditProject.getApplyername());
                    record.setOperatedate(new Date());
                    record.setProjectguid(projectguid);
                    // 项目代码
                    record.set("XiangMuDaiMa", XiangMuDaiMa);
                    // 项目编号
                    record.set("XiangMuBianHao", XiangMuBianHao);
                    // 项目名称
                    record.setXiangmumingchen(xiangmumingchen);
                    // 项目分类
                    record.setXiangmufenlei(XiangMuFenLei);
                    // 工程投资性质
                    record.set("InvPropertyNum", InvPropertyNum);
                    // 合同工期（天）
                    record.set("BargainDays", BargainDays);
                    // 项目所在省份
                    record.setXiangmusuozaishengfen(XiangMuSuoZaiShengFen);
                    // 项目所在城市
                    record.setXiangmusuozaichengshi(XiangMuSuoZaiChengShi);
                    // 项目所在区县
                    record.setXiangmusuozaiquxian(XiangMuSuoZaiQuXian);
                    // 项目地点
                    record.setXiangmudidian(XiangMuDiDian);
                    // 立项批复机关
                    record.setLiXiangPiFuJiGuan(LiXiangPiFuJiGuan);
                    // 立项批复时间
                    record.set("lixiangpifushijian", lixiangpifushijian);
                    // 中标通知书编号
                    record.setZhongBiaoTongZhiShuBianHao(ZhongBiaoTongZhiShuBianHao);
                    // 施工图审查项目编号
                    record.setShiGongTuShenChaXiangMuBianHao(ShiGongTuShenChaXiangMuBianHao);
                    // 建设用地规划许可证编号
                    record.set("JianSheYongDiGuiHuaXuKeZhen", JianSheYongDiGuiHuaXuKeZhen);
                    // 建设工程规划许可证编号
                    record.setJianSheGongChengGuiHuaXuKeZhen(JianSheGongChengGuiHuaXuKeZhen);
                    // 资金来源
                    record.setZijinlaiyuan(ZiJinLaiYuan);
                    // 总投资
                    record.setXiangmuzongtouziwanyuan(xiangmuzongtouziwanyuan);
                    // 总建筑面积
                    record.setXiangmuzongjianzhumianjipingfa(xiangmuzongjianzhumianjipingfa);
                    // 地上建筑面积
                    record.setXiangmudishangjianzhumianjipin(xiangmudishangjianzhumianjipin);
                    // 地下建筑面积
                    record.setXiangmudixiajianzhumianjipingf(xiangmudixiajianzhumianjipingf);
                    // 总长度
                    record.setZongchangdumi1(zongchangdumi1);
                    // 地上长度
                    record.setXiangmudishangchangdumi(xiangmudishangchangdumi);
                    // 地下长度
                    record.setXiangmudixiachangdumi(xiangmudixiachangdumi);
                    // 跨度
                    record.setXiangmukuadumi(xiangmukuadumi);
                    // 工程用途
                    record.setGongChengYongTu(GongChengYongTu);
                    // 建设规模
                    record.setJiansheguimo(jiansheguimo);
                    // 建设性质
                    record.setJianshexingzhi(jianshexingzhi);
                    // 重点项目
                    record.setZhongdianxiangmu(zhongdianxiangmu);
                    // 合同价格
                    record.setHetongjiagewanyuan(hetongjiagewanyuan);
                    // 合同开工日期
                    record.setHetongkaigongriqi(hetongkaigongriqi);
                    // 合同竣工日期
                    record.setHetongjungongriqi(hetongjungongriqi);
                    // 建设单位名称
                    record.setJianshedanweimingchen(jianshedanweimingchen);
                    // 建设单位统一社会信用代码
                    record.setJianSheDanWeiTongYiSheHuiXinYo(JianSheDanWeiTongYiSheHuiXinYo);
                    // 建设单位地址
                    record.setJianshedanweidizhi(jianshedanweidizhi);
                    // 建设单位所有制形式
                    record.setJianshedanweisuoyouzhixingshi(jianshedanweisuoyouzhixingshi);
                    // 建设单位电话
                    record.setJianshedanweidianhua(jianshedanweidianhua);
                    // 建设单位法定代表人
                    record.setJianshedanweifadingdaibiaoren(jianshedanweifadingdaibiaoren);
                    // 建设单位项目负责人
                    record.setJianshedanweixiangmufuzeren(jianshedanweixiangmufuzeren);
                    // 工程总承包单位
                    record.setGongchengzongchengbaodanwei(gongchengzongchengbaodanwei);
                    // 工程总承包单位统一社会信用代码
                    record.set("gongchengzongchengbaodanweixin", gongchengzongchengbaodanweixin);
                    // 工程总承包单位项目经理
                    record.setGongchengzongchengbaodanweixia(gongchengzongchengbaodanweixia);
                    // 施工总承包企业
                    record.setShigongzongchengbaoqiye(shigongzongchengbaoqiye);
                    // 施工总承包企业统一社会信用代码
                    record.setShiGongZongChengBaoQiYeTongYiS(ShiGongZongChengBaoQiYeTongYiS);
                    // 施工总承包企业项目负责人
                    record.setShigongzongchengbaoqiyexiangmu(shigongzongchengbaoqiyexiangmu);
                    // 全过程工程咨询企业
                    record.setQuanguochenggongchengzixunqiye(quanguochenggongchengzixunqiye);
                    // 全过程工程咨询企业统一社会信用代码
                    record.setQuanGuoChengGongChengZiXunXY(QuanGuoChengGongChengZiXunXY);
                    // 全过程工程咨询企业项目负责人
                    record.setQuanGuoChengGongChengZiXunFZR(QuanGuoChengGongChengZiXunFZR);
                    // 勘察单位
                    record.setKanchadanwei(kanchadanwei);
                    // 勘察单位项目负责人
                    record.setKanchadanweixiangmufuzeren(kanchadanweixiangmufuzeren);
                    // 勘察单位统一社会信用代码
                    record.setKanChaDanWeiCode(KanChaDanWeiCode);
                    record.setShejidanwei(shejidanwei);
                    record.setShejidanweixiangmufuzeren(shejidanweixiangmufuzeren);
                    record.setSheJiDanWeiCode(SheJiDanWeiCode);
                    record.setJianlidanwei(jianlidanwei);
                    record.setJianLiDanWeiCode(JianLiDanWeiCode);
                    record.setZongjianligongchengshi(zongjianligongchengshi);
                    record.setJiancedanwei(jiancedanwei);
                    record.setJianCeDanWeiFuZeRen(jiancedanweifuzeren);
                    record.setJianCeDanWeiCode(JianCeDanWeiCode);
                    record.setShentudanwei(shentudanwei);
                    record.setShentudanweixiangmufuzeren(shentudanweixiangmufuzeren);
                    record.setShenTuDanWeiCode(ShenTuDanWeiCode);
                    iAuditProjectFormSgxkzService.insert(record);
                    return JsonUtils.zwdtRestReturn("1", "保存项目信息成功", "");
                }
                else {
                    record.setOperateusername(auditProject.getApplyername());
                    record.setOperatedate(new Date());
                    record.setProjectguid(projectguid);
                    // 项目代码
                    record.set("XiangMuDaiMa", XiangMuDaiMa);
                    // 项目编号
                    record.set("XiangMuBianHao", XiangMuBianHao);
                    // 项目名称
                    record.setXiangmumingchen(xiangmumingchen);
                    // 项目分类
                    record.setXiangmufenlei(XiangMuFenLei);
                    // 工程投资性质
                    record.set("InvPropertyNum", InvPropertyNum);
                    // 合同工期（天）
                    record.set("BargainDays", BargainDays);
                    // 项目所在省份
                    record.setXiangmusuozaishengfen(XiangMuSuoZaiShengFen);
                    // 项目所在城市
                    record.setXiangmusuozaichengshi(XiangMuSuoZaiChengShi);
                    // 项目所在区县
                    record.setXiangmusuozaiquxian(XiangMuSuoZaiQuXian);
                    // 项目地点
                    record.setXiangmudidian(XiangMuDiDian);
                    // 立项批复机关
                    record.setLiXiangPiFuJiGuan(LiXiangPiFuJiGuan);
                    // 立项批复时间
                    record.set("lixiangpifushijian", lixiangpifushijian);
                    // 中标通知书编号
                    record.setZhongBiaoTongZhiShuBianHao(ZhongBiaoTongZhiShuBianHao);
                    // 施工图审查项目编号
                    record.setShiGongTuShenChaXiangMuBianHao(ShiGongTuShenChaXiangMuBianHao);
                    // 建设用地规划许可证编号
                    record.set("JianSheYongDiGuiHuaXuKeZhen", JianSheYongDiGuiHuaXuKeZhen);
                    // 建设工程规划许可证编号
                    record.setJianSheGongChengGuiHuaXuKeZhen(JianSheGongChengGuiHuaXuKeZhen);
                    // 资金来源
                    record.setZijinlaiyuan(ZiJinLaiYuan);
                    // 总投资
                    record.setXiangmuzongtouziwanyuan(xiangmuzongtouziwanyuan);
                    // 总建筑面积
                    record.setXiangmuzongjianzhumianjipingfa(xiangmuzongjianzhumianjipingfa);
                    // 地上建筑面积
                    record.setXiangmudishangjianzhumianjipin(xiangmudishangjianzhumianjipin);
                    // 地下建筑面积
                    record.setXiangmudixiajianzhumianjipingf(xiangmudixiajianzhumianjipingf);
                    // 总长度
                    record.setZongchangdumi1(zongchangdumi1);
                    // 地上长度
                    record.setXiangmudishangchangdumi(xiangmudishangchangdumi);
                    // 地下长度
                    record.setXiangmudixiachangdumi(xiangmudixiachangdumi);
                    // 跨度
                    record.setXiangmukuadumi(xiangmukuadumi);
                    // 工程用途
                    record.setGongChengYongTu(GongChengYongTu);
                    // 建设规模
                    record.setJiansheguimo(jiansheguimo);
                    // 建设性质
                    record.setJianshexingzhi(jianshexingzhi);
                    // 重点项目
                    record.setZhongdianxiangmu(zhongdianxiangmu);
                    // 合同价格
                    record.setHetongjiagewanyuan(hetongjiagewanyuan);
                    // 合同开工日期
                    record.setHetongkaigongriqi(hetongkaigongriqi);
                    // 合同竣工日期
                    record.setHetongjungongriqi(hetongjungongriqi);
                    // 建设单位名称
                    record.setJianshedanweimingchen(jianshedanweimingchen);
                    // 建设单位统一社会信用代码
                    record.setJianSheDanWeiTongYiSheHuiXinYo(JianSheDanWeiTongYiSheHuiXinYo);
                    // 建设单位地址
                    record.setJianshedanweidizhi(jianshedanweidizhi);
                    // 建设单位所有制形式
                    record.setJianshedanweisuoyouzhixingshi(jianshedanweisuoyouzhixingshi);
                    // 建设单位电话
                    record.setJianshedanweidianhua(jianshedanweidianhua);
                    // 建设单位法定代表人
                    record.setJianshedanweifadingdaibiaoren(jianshedanweifadingdaibiaoren);
                    // 建设单位项目负责人
                    record.setJianshedanweixiangmufuzeren(jianshedanweixiangmufuzeren);
                    // 工程总承包单位
                    record.setGongchengzongchengbaodanwei(gongchengzongchengbaodanwei);
                    // 工程总承包单位统一社会信用代码
                    record.set("gongchengzongchengbaodanweixin", gongchengzongchengbaodanweixin);
                    // 工程总承包单位项目经理
                    record.setGongchengzongchengbaodanweixia(gongchengzongchengbaodanweixia);
                    // 施工总承包企业
                    record.setShigongzongchengbaoqiye(shigongzongchengbaoqiye);
                    // 施工总承包企业统一社会信用代码
                    record.setShiGongZongChengBaoQiYeTongYiS(ShiGongZongChengBaoQiYeTongYiS);
                    // 施工总承包企业项目负责人
                    record.setShigongzongchengbaoqiyexiangmu(shigongzongchengbaoqiyexiangmu);
                    // 全过程工程咨询企业
                    record.setQuanguochenggongchengzixunqiye(quanguochenggongchengzixunqiye);
                    // 全过程工程咨询企业统一社会信用代码
                    record.setQuanGuoChengGongChengZiXunXY(QuanGuoChengGongChengZiXunXY);
                    // 全过程工程咨询企业项目负责人
                    record.setQuanGuoChengGongChengZiXunFZR(QuanGuoChengGongChengZiXunFZR);
                    // 勘察单位
                    record.setKanchadanwei(kanchadanwei);
                    // 勘察单位项目负责人
                    record.setKanchadanweixiangmufuzeren(kanchadanweixiangmufuzeren);
                    // 勘察单位统一社会信用代码
                    record.setKanChaDanWeiCode(KanChaDanWeiCode);
                    record.setShejidanwei(shejidanwei);
                    record.setShejidanweixiangmufuzeren(shejidanweixiangmufuzeren);
                    record.setSheJiDanWeiCode(SheJiDanWeiCode);
                    record.setJianlidanwei(jianlidanwei);
                    record.setJianLiDanWeiCode(JianLiDanWeiCode);
                    record.setZongjianligongchengshi(zongjianligongchengshi);
                    record.setJiancedanwei(jiancedanwei);
                    record.setJianCeDanWeiFuZeRen(jiancedanweifuzeren);
                    record.setJianCeDanWeiCode(JianCeDanWeiCode);
                    record.setShentudanwei(shentudanwei);
                    record.setShentudanweixiangmufuzeren(shentudanweixiangmufuzeren);
                    record.setShenTuDanWeiCode(ShenTuDanWeiCode);
                    iAuditProjectFormSgxkzService.update(record);
                    return JsonUtils.zwdtRestReturn("1", "修改项目信息成功", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveProjectForm接口参数：params【" + params + "】=======");
            log.info("=======saveProjectForm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存项目信息失败：" + e.getMessage(), "");
        }
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
     * 根据办件编号推送办件流水号(人社打印事项)
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitRsjFlowsnInfo", method = RequestMethod.POST)
    public String submitRsjFlowsnInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用submitRsjFlowsnInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String projectGuid = obj.getString("projectguid"); // 办件标识
                String areaCode = obj.getString("areacode"); // 区域编码
                log.info("=======获取办件标识projectguid:" + projectGuid + "=======");
                log.info("=======获取区域编码areaCode:" + areaCode + "=======");
                // 2.1、根据条件查询到办件数据
                String fields = "rowguid,projectname,task_id,taskguid,centerguid,flowsn,status,banjieresult";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                String taskGuid = auditProject.getTaskguid();
                String isSendFlowsn = "0";
                if (StringUtil.isNotBlank(taskGuid)) {
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, false).getResult();
                    if (StringUtil.isNotBlank(auditTaskExtension)) {
                        isSendFlowsn = auditTaskExtension.getStr("issendflowsn");
                    }
                }
                if ("1".equals(isSendFlowsn)) {
                    // 此处点击受理后，不会给窗口发送办件，因为受理完就要将申报流水号提送给浪潮方，不需要窗口人员
                    String flowsn = auditProject.getFlowsn(); // 获取要推送的办件的申报流水号
                    JSONObject paramObject = new JSONObject();
                    String logs = "";
                    if (StringUtil.isNotBlank(flowsn)) {
                        paramObject.put("flowsn", flowsn);
                        logs = TARequestUtil.sendPost(sendFlowsnPath, paramObject.toString(), "", "");
                        JSONObject result = JSONObject.parseObject(logs);
                        String custom = result.getString("custom");
                        JSONObject codeResult = JSONObject.parseObject(custom);
                        String code = codeResult.getString("code");
                        if ("1".equals(code)) {
                            AuditProject newAuditProject = iAuditProject
                                    .getAuditProjectByRowGuid(fields, projectGuid, areaCode).getResult();
                            if (StringUtil.isNotBlank(newAuditProject.getBanjieresult())) {
                                if (newAuditProject.getStatus() == 90 && 40 == newAuditProject.getBanjieresult()) {
                                    newAuditProject.setStatus(90);
                                }
                            }
                            else {
                                newAuditProject.setStatus(1);

                            }
                            iAuditProject.updateProject(newAuditProject);

                        }
                        // 修改办件状态为已推送
                    }
                    log.info("=======网厅调用推送办件流水号的接口的结果是=======" + logs);
                }

                JSONObject dataJson = new JSONObject();
                dataJson.put("isSendFlowsn", isSendFlowsn);
                return JsonUtils.zwdtRestReturn("1", "推送办件流水号接口调用成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitRsjFlowsnInfo接口参数：params【" + params + "】=======");
            log.info("=======submitRsjFlowsnInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用推送申报流水号接口失败：" + e.getMessage(), "");
        }
    }

    // 生成随机数字和字母,
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            }
            else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 通过接口获取相关的后台配置的便民服务信息列表
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getConvenientList", method = RequestMethod.POST)
    public String getConvenientList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getConvenientList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                Integer prmomiseType = obj.getInteger("prmomiseType");
                // 此处先进行判断这个类型数据什么范围内然后再根据范围去获取附件
                int param = 2; // 文化娱乐场所
                if (prmomiseType != null) {
                    if (prmomiseType == 9) {
                        param = 3;// 游泳场所
                    }
                    else if (prmomiseType >= 18) {
                        param = 4;// 住宿场所
                    }
                    else if (prmomiseType == 15 || prmomiseType == 16) {
                        param = 1;// 美容美发场所
                    }
                }
                // 通过api获取相关的数据
                AuditPromiseBook convenientList = promiseBookService.getPromiseBookByType(param);
                JSONObject convenientJson = new JSONObject();
                // 两个附件：一个需要返回图标的内容，
                String clientGuid = convenientList.getPromiseattachguid();
                if (StringUtil.isNotBlank(clientGuid)) {
                    List<FrameAttachStorage> attachStorageList = iAttachService.getAttachListByGuid(clientGuid);
                    if (attachStorageList != null && !attachStorageList.isEmpty()) {
                        // 图片的内容返回
                        for (FrameAttachStorage attachStorage : attachStorageList) {
                            convenientJson.put("photo", "data:image/png;base64," + Base64Util
                                    .encode(FileManagerUtil.getContentFromInputStream(attachStorage.getContent())));
                        }
                    }
                }
                // 定义返回的对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("bookAttach", convenientJson);
                log.info("=======结束调用getConvenientList接口=======");
                return JsonUtils.zwdtRestReturn("1", "承诺告知信息获取成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getConvenientList接口参数：params【" + params + "】=======");
            log.info("=======getConvenientList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "承诺告知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取附件attachguid接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getBusinessTypeDoc", method = RequestMethod.POST)
    public String getBusinessTypeDoc(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessTypeDoc接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 定义返回的对象
                JSONObject dataJson = new JSONObject();
                String taskid = obj.getString("taskid");
                String option = obj.getString("option");
                String areacode = obj.getString("areacode");
                String configValue = iConfigService.getFrameConfigValue("PP_SANITA_PERMIT_TASKID");
                if (StringUtil.isBlank(taskid) || "undefind".equals(taskid) || !configValue.contains(taskid)) {
                    dataJson.put("msg", "参数出错");
                    dataJson.put("flag", "1");
                    return JsonUtils.zwdtRestReturn("1", "附件attachguid获取失败", dataJson.toString());
                }
                BjwsService bjwsService = new BjwsService();
                String businessType = bjwsService.getCodeValue(option);
                if (StringUtil.isBlank(businessType)) {
                    dataJson.put("msg", "请联系管理员维护代码项！");
                    return JsonUtils.zwdtRestReturn("1", "系统出错！", dataJson.toString());
                }
                String gzsCliengGuid = bjwsService.getDoc(businessType, "10", areacode);
                if (StringUtil.isNotBlank(gzsCliengGuid)) {
                    String gzsGuid = getAttachGuid(gzsCliengGuid);
                    if (StringUtil.isNotBlank(gzsGuid)) {
                        dataJson.put("commitmentattachguid", gzsGuid);
                    }
                }
                String clsCliengGuid = bjwsService.getDoc(businessType, "20", areacode);
                if (StringUtil.isNotBlank(clsCliengGuid)) {
                    String clsGuid = getAttachGuid(clsCliengGuid);
                    if (StringUtil.isNotBlank(clsGuid)) {
                        dataJson.put("notifyattachguid", clsGuid);
                    }
                }
                dataJson.put("businessType", businessType);
                log.info("=======结束调用getConvenientList接口=======");
                return JsonUtils.zwdtRestReturn("1", "附件attachguid获取成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessTypeDoc接口参数：params【" + params + "】=======");
            log.info("=======getBusinessTypeDoc异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "附件attachguid获取失败：" + e.getMessage(), "");
        }
    }

    public String getAttachGuid(String cliengGuid) {
        List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengGuid);
        if (attachInfoList != null && !attachInfoList.isEmpty()) {
            return attachInfoList.get(0).getAttachGuid();
        }
        return "";
    }

    /**
     * 提交教师资格认定的表单信息
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitTeacherProjectForm", method = RequestMethod.POST)
    public String submitTeacherProjectForm(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitTeacherProjectForm接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String projectGuid = obj.getString("projectguid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String xm = obj.getString("xm");
                    String hjssd = obj.getString("hjssd");
                    String sfzh = obj.getString("sfzh");
                    String tjsj = obj.getString("tjsj");
                    String sfxyyj = obj.getString("sfxyyj");
                    String sxzy = obj.getString("sxzy");
                    String jszglb = obj.getString("jszglb");
                    String yjdz = obj.getString("yjdz");
                    String yymc = obj.getString("yymc");
                    String byyx = obj.getString("byyx");
                    String istjhg = obj.getString("istjhg");
                    Record record = iCxBusService.getDzbdDetail("formtable20220426153449", projectGuid);
                    if (record == null) {
                        record = new Record();
                        record.setSql_TableName("formtable20220426153449");
                        record.setPrimaryKeys("rowguid");
                        record.set("rowguid", projectGuid);
                        record.set("xm", xm);
                        record.set("hjssd", hjssd);
                        record.set("sfzh", sfzh);
                        record.set("tjsj", tjsj);
                        record.set("dxkz1", sfxyyj);
                        record.set("sxzy", sxzy);
                        record.set("xlxz1", jszglb);
                        record.set("wbk14", yjdz);
                        record.set("yymc", yymc);
                        record.set("byyx", byyx);
                        record.set("wbk10", istjhg);
                        iCxBusService.insert(record);
                    }
                    else {
                        record.setSql_TableName("formtable20220426153449");
                        record.setPrimaryKeys("rowguid");
                        record.set("rowguid", projectGuid);
                        record.set("xm", xm);
                        record.set("hjssd", hjssd);
                        record.set("sfzh", sfzh);
                        record.set("tjsj", tjsj);
                        record.set("dxkz1", sfxyyj);
                        record.set("sxzy", sxzy);
                        record.set("xlxz1", jszglb);
                        record.set("wbk14", yjdz);
                        record.set("yymc", yymc);
                        record.set("byyx", byyx);
                        record.set("wbk10", istjhg);
                        iCxBusService.update(record);
                    }
                    return JsonUtils.zwdtRestReturn("1", "提交成功！", "");
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
            log.info("=======submitTeacherProjectForm接口参数：params【" + params + "】=======");
            log.info("=======submitTeacherProjectForm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "调用推送申报流水号接口失败：" + e.getMessage(), "");
        }
    }

    public String uploadYqYjsAttach(String subappguid) {

        return null;

    }

    public void generateResult(String rowguid, String formid, String taskname) throws IOException {
        String epointsformurl = configServicce.getFrameConfigValue("epointsformurlwt")
                + "/rest/jnepointsform/eformToWordOrPdf";
        JSONObject params = new JSONObject();
        JSONObject reqParms = new JSONObject();
        params.put("savetype", ".pdf");
        params.put("rowguid", rowguid);
        params.put("formid", formid);
        reqParms.put("token", "epoint_webserivce_**##0601");
        reqParms.put("params", params);
        String rtnStr = HttpUtil.doHttp(epointsformurl, reqParms.toJSONString(), "post", 2);
        JSONObject rtnJson = JSONObject.parseObject(rtnStr);
        String attachUrl = rtnJson.getJSONObject("custom").getString("attachUrl");
        InputStream inputStream = null;
        String attachguid = UUID.randomUUID().toString();
        try {
            InputStream in = null;
            ByteArrayOutputStream outStream = null;
            in = HttpUtil.get(attachUrl);
            // String aaa = HttpUtil.doGet(url);
            // in = getInputStreamByDownloadUrl(url);

            // in = new
            // ByteArrayInputStream(aaa.getBytes(StandardCharsets.UTF_8));

            // 2.获取字节
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] fileData = outStream.toByteArray();
            in = new ByteArrayInputStream(fileData);

            FrameAttachInfo frame = AttachUtil.saveFileInputStream(attachguid, rowguid, taskname + ".pdf", ".pdf",
                    "审批结果", in.available(), in, "", "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private String getGgcsxsqConfig() {
        String result = iConfigService.getFrameConfigValue("ggcsxsq_mp_itemid");
        if (StringUtil.isBlank(result)) {
            result = "无";
        }
        return result;
    }

    private boolean getIsAllZmMateri(String projectGuid) {
        boolean result = true;
        if (StringUtil.isNotBlank(projectGuid)) {
            List<AuditProjectMaterial> auditProjectMaterialList = iAuditProjectMaterial
                    .selectProjectMaterialList(projectGuid).getResult();
            if (auditProjectMaterialList != null && auditProjectMaterialList.size() > 0) {
                //
                AuditTaskMaterial auditTaskMaterial = null;

                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterialList) {
                    auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    if (auditTaskMaterial != null) {
                        if (StringUtil.isNotBlank(auditTaskMaterial.getStr("bigshowtype"))
                                && "1".equals(auditTaskMaterial.getStr("isxtendfield"))) {
                            // 获取附件 看是否是 新爱山东材料
                            List<FrameAttachInfo> frameAttachInfoList = iAttachService
                                    .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                            boolean isAsd = false;
                            if (frameAttachInfoList != null && frameAttachInfoList.size() > 0) {
                                for (int i = 0; i < frameAttachInfoList.size(); i++) {
                                    if ("新爱山东材料".equals(frameAttachInfoList.get(i).getCliengTag())) {
                                        isAsd = true;
                                    }
                                }
                                if (!isAsd) {
                                    result = false;
                                }
                            }
                        }
                    }
                    else {
                        result = false;
                    }
                }
            }
        }
        else {
            result = false;
        }

        return result;
    }
}
