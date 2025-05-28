package com.epoint.medicalinsurance.rest;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.auditrsapplyprocess.domain.AuditRsApplyProcess;
import com.epoint.basic.auditresource.auditrsapplyprocess.inter.IAuditRsApplyProcess;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.util.TARequestUtil;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFManageAPI9;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 医保办件推送的相关接口
 *
 * @Author 成都研发4部-左喜辰
 * @Date 2023/12/20
 */
@RestController
@RequestMapping(value = "/syncAuditProject")
public class MedicalInsuranceController {

    Logger log = Logger.getLogger(MedicalInsuranceController.class);

    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditRsApplyProcess applyProcessService;

    @Autowired
    private IAuditProjectOperation projectOperationService;

    @Autowired
    private IWFManageAPI9 iwfManageAPI9;

    @Autowired
    private IWebUploaderService iWebUploaderService;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 获取三方推送的办件基本信息，再推送至好差评接口及省前置库
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/receiveProjectInfo", method = RequestMethod.POST)
    public String receiveProjectInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("==========开始调用办件基本信息推送接口==========");
            JSONObject paramObj = JSONObject.parseObject(params);
            log.info("接口入参：" + paramObj);
            // 原系统的主键
            String orgbusno = paramObj.getString("orgbusno");
            // 申报号(办件流水号)
            String projid = paramObj.getString("projid");
            // 办件类型
            String approvaltype = paramObj.getString("approvaltype");
            // 事项办理承诺时限
            String promisetimelimit = paramObj.getString("promisetimelimit");
            // 事项办理承诺事项单位
            String promisetimeunit = paramObj.getString("promisetimeunit");
            // 规定办理时限
            String timelimit = paramObj.getString("timelimit");
            // 业务归类
            String submit = paramObj.getString("submit");
            // 环节发生时间
            String occurtime = paramObj.getString("occurtime");
            // 申报名称
            String projectname = paramObj.getString("projectname");
            // 受理单位名称
            String acceptdeptname = paramObj.getString("acceptdeptname");
            // 受理单位id，组织机构代码
            String acceptdeptid = paramObj.getString("acceptdeptid");
            // 行政区划
            String region_id = paramObj.getString("region_id");
            // 审批事项编号
            String itemno = paramObj.getString("itemno");
            // 审批事项名称
            String itemname = paramObj.getString("itemname");
            // 事项类型
            String itemtype = paramObj.getString("itemtype");
            // 事项所属区划
            String itemregionid = paramObj.getString("itemregionid");
            // 基本编码
            String catalogCode = paramObj.getString("catalogCode");
            // 实施编码
            String taskCode = paramObj.getString("taskCode");
            // 申报者或申报单位名称
            String applicant = paramObj.getString("applicant");
            // 联系人/代理人证件类型
            String applicanttype = paramObj.getString("applicanttype");
            // 联系人/代理人证件号码
            String applicantcode = paramObj.getString("applicantcode");
            // 申请人类型
            String applyerType = paramObj.getString("applyerType");
            // 申请人证件类型
            String applyerPageType = paramObj.getString("applyerPageType");
            // 申请人证件号码
            String applyerPageCode = paramObj.getString("applyerPageCode");
            // 申报者手机
            String applicantmobile = paramObj.getString("applicantmobile");
            // 申报材料清单
            String acceptlist = paramObj.getString("acceptlist");
            // 统一社会信用代码
            String acceptdeptcode = paramObj.getString("acceptdeptcode");
            // 工商登记码
            String acceptdeptcode1 = paramObj.getString("acceptdeptcode1");
            // 税务登记码
            String acceptdeptcode2 = paramObj.getString("acceptdeptcode2");
            // 检查必填入参是否为空
            if (StringUtil.isBlank(approvaltype)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数approvaltype为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(promisetimelimit)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数promisetimelimit为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(promisetimeunit)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数promisetimeunit为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(timelimit)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数timelimit为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(submit)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数submit为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(occurtime)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数occurtime为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptdeptcode1)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptdeptcode1为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptdeptcode2)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptdeptcode2为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(itemregionid)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数itemregionid为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(orgbusno)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数orgbusno为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(projid)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数projid为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(itemno)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数itemno为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(itemname)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数itemname为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(projectname)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数projectname为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(applicant)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数applicant为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptdeptid)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptdeptid为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(region_id)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数region_id为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptdeptname)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptdeptname为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptlist)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptlist为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(itemtype)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数itemtype为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(catalogCode)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数catalogCode为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(taskCode)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数taskCode为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(applyerType)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数applyerType为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(applyerPageType)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数applyerPageType为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(applyerPageCode)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数applyerPageCode为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(applicantmobile)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数applicantmobile为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(acceptdeptcode)) {
                // 必填参数为空
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数acceptdeptcode为空，请检查入参！", "");
            }
            // 推送好差评
            String flag1 = turnhcpevaluate(paramObj, 1, "提交申请信息",
                    EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            // 推送省办件归集库
            boolean flag2 = insertBaseInfo(paramObj);
            if (flag1.contains("true") && flag2) {
                // 本地入库
                AuditProject oldProject = iWebUploaderService.getProjectByFlowsn(projid);
                if (oldProject == null) {
                    AuditProject auditProject = new AuditProject();
                    auditProject.setRowguid(UUID.randomUUID().toString());
                    auditProject.setFlowsn(projid);
                    // 申请人类型
                    if ("1".equals(applyerType)) {
                        auditProject.setApplyertype(20);
                    } else {
                        auditProject.setApplyertype(10);
                    }
                    // 申请人证照类型
                    switch (applyerPageType) {
                        case "111":
                            auditProject.setCerttype("22");
                            break;
                        case "01":
                            auditProject.setCerttype("16");
                            break;
                        case "02":
                            auditProject.setCerttype("14");
                            break;
                    }
                    // 申请方式
                    if ("0".equals(submit)) {
                        auditProject.setApplyway(20);
                    } else {
                        auditProject.setApplyway(10);
                    }
                    auditProject.setCertnum(applyerPageCode);
                    auditProject.setProjectname(projectname);
                    auditProject.setApplyername(applicant);
                    auditProject.setContactmobile(applicantmobile);
                    auditProject.setContactphone(applicantmobile);
                    auditProject.setContactcertnum(applicantcode);
                    auditProject.setOuname(acceptdeptname);
                    auditProject.setOuguid(acceptdeptid);
                    auditProject.setApplydate(EpointDateUtil.convertString2Date(occurtime, EpointDateUtil.DATE_TIME_FORMAT));
                    auditProject.setAcceptusername(acceptdeptname);
                    auditProject.setAcceptuserguid(acceptdeptid);
                    auditProject.setAcceptareacode(region_id.substring(0, 6));
                    auditProject.setAcceptuserdate(EpointDateUtil.convertString2Date(occurtime, EpointDateUtil.DATE_TIME_FORMAT));
                    auditProject.setAreacode(region_id.substring(0, 6));
                    auditProject.setPromise_day(Integer.valueOf(promisetimelimit));
                    auditProject.setPviguid(UUID.randomUUID().toString());
                    auditProject.setStatus(30);
                    auditProject.setOperateusername("济宁市医疗保障局同步办件");
                    // 获取在用事项信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("is_enable", "1");
                    sqlConditionUtil.eq("is_history", "0");
                    sqlConditionUtil.eq("is_editafterimport", "1");
                    sqlConditionUtil.eq("item_id", taskCode);
                    List<AuditTask> auditTaskList = auditTaskService.getAllTask(sqlConditionUtil.getMap()).getResult();
                    if (EpointCollectionUtils.isEmpty(auditTaskList)) {
                        sqlConditionUtil.clear();
                        sqlConditionUtil.eq("is_enable", "1");
                        sqlConditionUtil.eq("is_history", "0");
                        sqlConditionUtil.eq("is_editafterimport", "1");
                        sqlConditionUtil.eq("taskCode", taskCode);
                        auditTaskList = auditTaskService.getAllTask(sqlConditionUtil.getMap()).getResult();
                    }
                    if (EpointCollectionUtils.isNotEmpty(auditTaskList)) {
                        auditProject.setTaskguid(auditTaskList.get(0).getRowguid());
                        auditProject.setTask_id(auditTaskList.get(0).getTask_id());
                        auditProject.setTasktype(auditTaskList.get(0).getType());
                    }
                    iWebUploaderService.insertProject(auditProject);
                }
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_YES, "办件基本信息推送成功！", "");
            } else if (flag1.contains("false")) {
                // 好差评接口的错误返回
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, flag1.replace("false", ""), "");
            } else {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "办件基本信息推送失败！", "");
            }
        } catch (Exception e) {
            log.info("=====接口调用异常=====");
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "接口调用异常！", "");
        } finally {
            log.info("==========结束调用办件基本信息推送接口==========");
        }
    }

    /**
     * 获取三方推送的办件流程信息，再推送至省前置库
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/receiveProcessInfo", method = RequestMethod.POST)
    public String receiveProcessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("==========开始调用办件流程信息推送接口==========");
            JSONObject paramObj = JSONObject.parseObject(params);
            log.info("接口入参：" + paramObj);
//            // 原系统的主键
//            String orgbusno = paramObj.getString("orgbusno");
            // 办件编号
            String proId = paramObj.getString("proId");
            // 办理环节名称
            String dealStep = paramObj.getString("dealStep");
            // 办理环节类型
            String dealPro = paramObj.getString("dealPro");
            // 办理人员
            String dealName = paramObj.getString("dealName");
            // 办理人员id
            String dealUserid = paramObj.getString("dealUserid");
            // 环节开始时间
            String receTime = paramObj.getString("receTime");
            // 环节结束时间
            String dealTime = paramObj.getString("dealTime");
            // 处理意见
            String dealOpinion = paramObj.getString("dealOpinion");
            // 处理结果
            String dealResult = paramObj.getString("dealResult");
            // 处理单位
            String dealOu = paramObj.getString("dealOu");
            // 处理单位组织机构代码
            String dealOuId = paramObj.getString("dealOuId");
            // 办件状态
            String proStatus = paramObj.getString("proStatus");
            // 行政区划
            String areacode = paramObj.getString("areacode");
            // 办理状态
            String dealState = paramObj.getString("dealState");
            // 提交人员
            String subName = paramObj.getString("subName");
            // 办结参数-办结结果
            String doneresult = paramObj.getString("doneresult");
//            if (StringUtil.isBlank(orgbusno)) {
//                // 必填参数为空
//                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数orgbusno为空，请检查入参！", "");
//            }
            if (StringUtil.isBlank(proId)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数proId为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealStep)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealStep为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealName)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealName为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealUserid)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealUserid为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(receTime)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数receTime为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealTime)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealTime为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealOpinion)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealOpinion为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealResult)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealResult为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealOu)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealOu为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(subName)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数subName为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealPro)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealPro为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(proStatus)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数proStatus为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(areacode)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数areacode为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealState)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealState为空，请检查入参！", "");
            }
            if (StringUtil.isBlank(dealOuId)) {
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数dealOuId为空，请检查入参！", "");
            }
            if (StringUtil.isNotBlank(doneresult)) {
                if ("0".equals(doneresult)) {
                    if (StringUtil.isBlank(paramObj.getString("certificatenam"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数certificatenam为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("certificateno"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数certificateno为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("certificatelimit"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数certificatelimit为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("publisher"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数publisher为空，请检查入参！", "");
                    }
                } else {
                    if (StringUtil.isBlank(paramObj.getString("exitres"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数exitres为空，请检查入参！", "");
                    }
                }
                String isfee = paramObj.getString("isfee");
                if (StringUtil.isBlank(isfee)) {
                    return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数isfee为空，请检查入参！", "");
                }
                if ("1".equals(isfee)) {
                    if (StringUtil.isBlank(paramObj.getString("fee"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数fee为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("feestandard"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数feestandard为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("feestandard"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数feestandard为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("paypersonname"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数paypersonname为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("payperidcard"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数payperidcard为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("payermobile"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数payermobile为空，请检查入参！", "");
                    }
                    if (StringUtil.isBlank(paramObj.getString("payertel"))) {
                        return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "必填参数payertel为空，请检查入参！", "");
                    }
                }
                // 推送办结信息至前置库
                boolean flag1 = insertStepInfo(paramObj, "2");
                boolean flag2 = insertBanjieInfo(paramObj);
                if (!flag1) {
                    return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "办件流程信息推送失败！", "");
                }
                if (!flag2) {
                    return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "办件办结信息推送失败！", "");
                }
                // 本地入库
                insertLocalProcessInfo(paramObj);
                return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_YES, "办件流程信息推送成功！", "");
            } else {
                // 推送流程信息至前置库
                boolean flag = insertStepInfo(paramObj, "1");
                if (flag) {
                    // 本地入库
                    insertLocalProcessInfo(paramObj);
                    return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_YES, "办件流程信息推送成功！", "");
                } else {
                    return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "办件流程信息推送失败！", "");
                }
            }
        } catch (Exception e) {
            log.info("=====接口调用异常=====");
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn(ZwdtConstant.STRING_NO, "接口调用异常！", "");
        } finally {
            log.info("==========结束调用办件流程信息推送接口==========");
        }
    }

    /**
     * 推送省办件归集库
     *
     * @param paramObj
     * @return
     */
    public boolean insertBaseInfo(JSONObject paramObj) {
        // 查询前置库有没有重复办件
        eajcstepbasicinfogt baseinfo = iWebUploaderService.getQzkBaseInfoByOrgbusno(paramObj.getString("orgbusno"));
        if (baseinfo == null) {
            String areacodere = "";
            if (paramObj.getString("region_id").length() == 6) {
                areacodere = paramObj.getString("region_id") + "000000";
            } else {
                areacodere = paramObj.getString("region_id");
            }
            log.info("=====开始推送前置库办件=====");
            baseinfo = new eajcstepbasicinfogt();
            baseinfo.setOrgbusno(paramObj.getString("orgbusno"));
            baseinfo.setProjid(paramObj.getString("projid"));
            baseinfo.setProjpwd("11111");
            baseinfo.setValidity_flag("1");
            baseinfo.setDataver("1");
            baseinfo.setStdver("1");
            baseinfo.setItemno(paramObj.getString("itemno"));
            baseinfo.set("ACCEPTLIST", paramObj.getString("acceptlist"));
            baseinfo.set("ITEMTYPE", paramObj.getString("itemtype"));
            baseinfo.set("CATALOGCODE", paramObj.getString("catalogCode"));
            baseinfo.set("TASKCODE", paramObj.getString("taskCode"));
            baseinfo.set("APPLYERTYPE", paramObj.getString("applyerType"));
            baseinfo.set("ApplyerPageType", paramObj.getString("applyerPageType"));
            baseinfo.set("ApplyerPageCode", paramObj.getString("applyerPageCode"));
            baseinfo.setItemname(paramObj.getString("itemname"));
            baseinfo.setProjectname(paramObj.getString("projectname"));
            baseinfo.setApplicant(paramObj.getString("applicant"));
            baseinfo.setApplicanttel(paramObj.getString("applicanttel"));
            baseinfo.setAcceptdeptid(paramObj.getString("acceptdeptid"));
            baseinfo.setAcceptdeptname(paramObj.getString("acceptdeptname"));
            baseinfo.setRegion_id(areacodere);
            baseinfo.setApprovaltype(paramObj.getString("approvaltype"));
            baseinfo.setPromisetimelimit(paramObj.getString("promisetimelimit"));
            baseinfo.setPromisetimeunit(paramObj.getString("promisetimeunit"));
            baseinfo.setTimelimit(paramObj.getString("timelimit"));
            baseinfo.setItemregionid(areacodere);
            baseinfo.setSubmit(paramObj.getString("submit"));
            baseinfo.setOccurtime(paramObj.getString("occurtime"));
            baseinfo.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            baseinfo.setAcceptdeptcode(paramObj.getString("acceptdeptcode"));
            baseinfo.setAcceptdeptcode1(paramObj.getString("acceptdeptcode1"));
            baseinfo.setAcceptdeptcode2(paramObj.getString("acceptdeptcode2"));
            baseinfo.setApplicantCardtype(paramObj.getString("applicanttype"));
            baseinfo.setApplicantCardCode(paramObj.getString("applicantcode"));
            // 插入办件的基本信息
            int count = iWebUploaderService.insertRestQzkBaseInfo(baseinfo);
            return count > 0;
        } else {
            log.info("=====请勿重复推送办件=====");
            return true;
        }
    }

    /**
     * 前置库插入办件流程信息
     *
     * @param paramObj
     * @return
     */
    public boolean insertStepInfo(JSONObject paramObj, String sn) {
        // 查询办件基本信息
        eajcstepbasicinfogt baseInfo = iWebUploaderService.getQzkBaseInfoByOrgbusno(paramObj.getString("proId"));
        if (baseInfo != null) {
            Record processInfo = iWebUploaderService.getQzkStepInfo(baseInfo.getOrgbusno(), sn);
            if (processInfo == null) {
                String areacodere = "";
                if (paramObj.getString("areacode").length() == 6) {
                    areacodere = paramObj.getString("areacode") + "000000";
                } else {
                    areacodere = paramObj.getString("areacode");
                }
                // 插入办件流程表
                eajcstepprocgt process = new eajcstepprocgt();
                // 原系统业务流水号
                process.setOrgbusno(baseInfo.getOrgbusno());
                // 申办号
                process.setProjid(paramObj.getString("proId"));
                process.setValidity_flag("1");
                // 数据版本号
                process.setDataver("1");
                // 标准版本号
                process.setStdver("1");
                // 审批过程序号
                process.setSn(sn);
                // 环节名称
                process.setNodename(paramObj.getString("dealStep"));
                // 环节类型
                process.setNodetype(paramObj.getString("dealPro"));
                // 环节处理人id
                process.setNodeprocer(paramObj.getString("dealUserid"));
                // 环节处理人姓名
                process.setNodeprocername(paramObj.getString("dealName"));
                // 环节处理人行政区划
                process.setNodeprocerarea(areacodere);
                // 处理行政区划
                process.setRegion_id(areacodere);
                // 处理单位id，组织机构代码
                process.setProcunit(paramObj.getString("dealOuId"));
                // 处理单位名称
                process.setProcunitname(paramObj.getString("dealOu"));
                // 环节办理状态
                process.setNodestate(paramObj.getString("dealState"));
                // 环节开始时间
                process.setNodestarttime(paramObj.getString("receTime"));
                // 环节结束时间
                process.setNodeendtime(paramObj.getString("dealTime"));
                // 环节处理意见
                process.setNodeadv(paramObj.getString("dealOpinion"));
                // 环节处理结果
                process.setNoderesult(paramObj.getString("dealResult"));
                // 环节办理时间
                process.setOccurtime(
                        paramObj.getString("dealTime"));
                // 数据存库时间
                process.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                // 数据交换状态标志位
                process.setSignstate("0");
                // 行政区划
                process.setItemregionid(areacodere);
                // 环节批次id(可为空)
                process.setNodecode("");
                try {
                    int count = iWebUploaderService.insertRestQzkProcess(process);
                    return count > 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("=====请勿重复推送办件流程信息=====");
                return true;
            }
        }
        return false;
    }

    /**
     * 插入办件办结信息
     */
    public boolean insertBanjieInfo(JSONObject paramObj) {
        // 查询办件基本信息
        eajcstepbasicinfogt baseInfo = iWebUploaderService.getQzkBaseInfoByOrgbusno(paramObj.getString("proId"));
        if (baseInfo != null) {
            Record doneInfo = iWebUploaderService.getQzkBanjieInfo(baseInfo.getOrgbusno());
            if (doneInfo == null) {
                String areacodere = "";
                if (paramObj.getString("areacode").length() == 6) {
                    areacodere = paramObj.getString("areacode") + "000000";
                } else {
                    areacodere = paramObj.getString("areacode");
                }
                eajcstepdonegt done = new eajcstepdonegt();
                // 原系统业务流水号
                done.setOrgbusno(baseInfo.getOrgbusno());
                // 办件申办号
                done.setProjid(paramObj.getString("proId"));
                done.setValidity_flag("1");
                // 标准版本号
                done.setStdver("1");
                // 数据版本号
                done.setDataver("1");
                // 业务办理行政区划
                done.setRegion_id(areacodere);
                // 办结结果
                String doneresult = paramObj.getString("doneresult");
                done.setDoneresult(doneresult);
                if ("0".equals(doneresult)) {
                    // 证件名称
                    String certificatenam = paramObj.getString("certificatenam");
                    if (certificatenam.length() > 2) {
                        certificatenam = "11";
                    }
                    done.setCertificatenam(certificatenam);
                    // 证件编号
                    done.setCertificateno(paramObj.getString("certificateno"));
                    // 证件有效期限
                    done.setCertificatelimit(paramObj.getString("certificatelimit"));
                    // 发证/盖章单位
                    done.setPublisher(paramObj.getString("publisher"));
                    done.setExitres("");
                } else {
                    // 作废或退回原因
                    done.setExitres(paramObj.getString("exitres"));
                    done.setCertificatenam("");
                    done.setCertificateno("");
                    done.setCertificatelimit("");
                    done.setPublisher("");
                }
                // 是否收费 0-不收 1-收
                String isfee = paramObj.getString("isfee");
                done.setIsfee(isfee);
                if ("1".equals(isfee)) {
                    // 收费金额
                    done.setFee(paramObj.getString("fee"));
                    // 收费标准
                    done.setFeestandard(paramObj.getString("feestandard"));
                    // 收费依据
                    done.setFeestandaccord(paramObj.getString("feestandaccord"));
                    // 缴费人姓名
                    done.setPaypersonname(paramObj.getString("paypersonname"));
                    // 缴费人身份证号
                    done.setPayperidcard(paramObj.getString("payperidcard"));
                    // 缴费人手机号
                    done.setPayermobile(paramObj.getString("payermobile"));
                    // 缴费人电话
                    done.setPayertel(paramObj.getString("payertel"));
                } else {
                    done.setFee("");
                    done.setFeestandard("");
                    done.setFeestandaccord("");
                    done.setPaypersonname("");
                    done.setPayperidcard("");
                    done.setPayermobile("");
                    done.setPayertel("");
                }
                // 环节发生时间
                done.setOccurtime(paramObj.getString("dealTime"));
                // 环节办理人
                done.setTransactor(paramObj.getString("dealName"));
                // 数据入库时间
                done.setMaketime(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                // 数据交换状态标志位
                done.setSignstate("0");
                // 行政区划
                done.setItemregionid(areacodere);
                try {
                    int count = iWebUploaderService.insertRestQzkDone(done);
                    return count > 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.info("=====请勿重复推送办件办结信息=====");
                return true;
            }
        }
        return false;
    }

    /**
     * 本地流程信息入库
     *
     * @param paramObj
     * @return
     */
    public void insertLocalProcessInfo(JSONObject paramObj) {
        AuditProject auditProject = iWebUploaderService.getProjectByFlowsn(paramObj.getString("proId"));
        if (auditProject != null) {
            // 获取在用事项信息
            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            // 入库audit_rs_apply_process表
            AuditRsApplyProcess auditRsApplyProcess = new AuditRsApplyProcess();
            auditRsApplyProcess.setRowguid(UUID.randomUUID().toString());
            auditRsApplyProcess.setProjectid(auditProject.getRowguid());
            auditRsApplyProcess.setNodename(paramObj.getString("dealStep"));
            auditRsApplyProcess.setAction(paramObj.getString("dealResult"));
            auditRsApplyProcess.setStatus("5");
            auditRsApplyProcess.setHandleusername(paramObj.getString("dealName"));
            auditRsApplyProcess.setHandleopinion(paramObj.getString("dealOpinion"));
            auditRsApplyProcess.setDepartment(paramObj.getString("dealOu"));
            auditRsApplyProcess.setORG_ID(paramObj.getString("dealOuId"));
            auditRsApplyProcess.setNO(paramObj.getString("proId"));
            auditRsApplyProcess.setStarttime(EpointDateUtil.convertString2Date(
                    paramObj.getString("receTime"), EpointDateUtil.DATE_TIME_FORMAT));
            auditRsApplyProcess.setEndtime(EpointDateUtil.convertString2Date(
                    paramObj.getString("dealTime"), EpointDateUtil.DATE_TIME_FORMAT));
            auditRsApplyProcess.setNO_ORD(1);
            if (auditTask != null) {
                auditRsApplyProcess.setItem_ID(auditTask.getItem_id());
            }
            applyProcessService.insertApplyProcess(auditRsApplyProcess);
            // 入库workflow_workitem表
            WorkflowWorkItem workflowWorkItem = new WorkflowWorkItem();
            workflowWorkItem.setWorkItemGuid(UUID.randomUUID().toString());
            workflowWorkItem.setWorkItemName("【" + paramObj.getString("dealStep") + "】" + auditProject.getProjectname());
            workflowWorkItem.setActivityName(paramObj.getString("dealStep"));
            workflowWorkItem.setStatus(getProjectStatus(paramObj.getString("proStatus")));
            workflowWorkItem.setSenderName(paramObj.getString("subName"));
            workflowWorkItem.setOperatorName(paramObj.getString("dealName"));
            workflowWorkItem.setOperatorGuid(paramObj.getString("dealUserid"));
            workflowWorkItem.setOperatorForDisplayName(paramObj.getString("dealName"));
            workflowWorkItem.setOperatorForDisplayGuid(paramObj.getString("dealUserid"));
            workflowWorkItem.setOpinion(paramObj.getString("dealOpinion"));
            workflowWorkItem.setOuguid(paramObj.getString("dealOuId"));
            workflowWorkItem.setProcessVersionInstanceGuid(auditProject.getPviguid());
            workflowWorkItem.setStartDate(EpointDateUtil.convertString2Date(
                    paramObj.getString("receTime"), EpointDateUtil.DATE_TIME_FORMAT));
            workflowWorkItem.setEndDate(EpointDateUtil.convertString2Date(
                    paramObj.getString("dealTime"), EpointDateUtil.DATE_TIME_FORMAT));
            iwfManageAPI9.addWorkItem(workflowWorkItem, false);
            // 入库audit_project_operation表
            AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
            auditProjectOperation.setRowguid(UUID.randomUUID().toString());
            auditProjectOperation.setProjectGuid(auditProject.getRowguid());
            auditProjectOperation.setRemarks(paramObj.getString("dealOpinion"));
            auditProjectOperation.setPVIGuid(auditProject.getPviguid());
            auditProjectOperation.setApplyerName(paramObj.getString("subName"));
            auditProjectOperation.setOperateusername(paramObj.getString("dealName"));
            auditProjectOperation.setOperateUserGuid(paramObj.getString("dealUserid"));
            auditProjectOperation.setAreaCode(paramObj.getString("areacode").substring(0, 6));
            auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
            int type = getOperateTypeKey(paramObj.getString("proStatus"));
            auditProjectOperation.setOperateType(String.valueOf(type));
            projectOperationService.addProjectOperation(auditProjectOperation);
        }
    }

    /**
     * 推送好差评办件服务数据
     */
    public String turnhcpevaluate(JSONObject paramObj, int serviceNumber, String servicename, String newserviceTime) {
        JSONObject json = new JSONObject();
        String itemregionid = paramObj.getString("itemregionid");
        if (itemregionid.length() == 6) {
            itemregionid = itemregionid + "000000";
        } else {
            itemregionid = paramObj.getString("itemregionid");
        }
        String deptcode = paramObj.getString("acceptdeptcode");
        // 统一社会信用代码
        json.put("deptCode", deptcode);
        // 事项编码
        json.put("taskCode", paramObj.getString("taskCode"));
        // 行政区划编码
        json.put("areaCode", itemregionid);
        // 事项名称
        json.put("taskName", paramObj.getString("itemname"));
        // 办件编号
        json.put("projectNo", paramObj.getString("projid"));
        // 办理状态
        String proStatus = serviceNumber + "";
        json.put("proStatus", proStatus);
        // 部门编码
        json.put("orgcode", itemregionid + "_" + deptcode);
        // 部门id
        json.put("ouguid", paramObj.getString("acceptdeptid"));
        // 部门名称
        json.put("orgName", paramObj.getString("acceptdeptname"));
        // 收件时间
        json.put("acceptDate", paramObj.getString("occurtime"));
        // 申请人类型
        json.put("userProp", paramObj.getString("applyerType"));
        // 申请人姓名
        json.put("userName", paramObj.getString("applicant"));
        // 申请人证件类型
        json.put("userPageType", paramObj.getString("applyerPageType"));
        // 办理人姓名
        json.put("proManager", paramObj.getString("acceptdeptname"));
        // 申请人证件号码(线上)
        json.put("certKey", paramObj.getString("applyerPageCode"));
        // 申请人证件号码(线下)
        json.put("certKeyGOV", paramObj.getString("applyerPageCode"));
        // 服务名称
        json.put("serviceName", servicename);
        // 服务次数
        json.put("serviceNumber", serviceNumber);
        // 服务时间
        if (StringUtil.isNotBlank(newserviceTime)) {
            json.put("serviceTime", newserviceTime);
        } else {
            json.put("serviceTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        // 办件类型
        json.put("projectType", paramObj.getString("approvaltype"));
        // 事项类型
        String taskType = paramObj.getString("itemtype");
        json.put("taskType", taskType);
        // 联系人电话
        json.put("mobile", paramObj.getString("applicanttel"));
        // 办件名称
        json.put("projectName", "关于" + paramObj.getString("applicant") + paramObj.getString("itemname") + "的业务");
        // 证件类型
        json.put("creditType", paramObj.getString("applyerPageType"));
        // 证件编号
        json.put("creditNum", paramObj.getString("applyerPageCode"));
        // 承诺办结时间
        json.put("promiseDay", paramObj.getString("promisetimelimit"));
        // 法定办结时间
        json.put("anticipateDay", paramObj.getString("timelimit"));
        json.put("month", "ck");
        // 线上评价为1
        json.put("proChannel", "1");
        // 办件类型(即办件1, 承诺件2)
        json.put("promiseTime", paramObj.getString("approvaltype"));
        JSONObject submit = new JSONObject();
        submit.put("params", json);
        String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
        if (StringUtil.isNotBlank(resultsign)) {
            JSONObject jsonobject = JSONObject.parseObject(resultsign);
            JSONObject jsonstatus = (JSONObject) jsonobject.get("status");
            if ("200".equals(jsonstatus.get("code").toString())) {
                JSONObject jsoncustom = (JSONObject) jsonobject.get("custom");
                if ("1".equals(jsoncustom.get("code").toString())) {
                    return "true";
                } else {
                    log.info("保存办件服务数据失败：" + paramObj.getString("projid") + "，原因：" + resultsign);
                    String text = JSONObject.parseObject(resultsign).getJSONObject("custom").getString("text");
                    if (text.contains("已存在该条服务信息")) {
                        return "true";
                    }
                    return "false" + text;
                }
            } else {
                log.info("保存办件服务数据失败：" + paramObj.getString("projid") + "，原因：" + resultsign);
                String text = JSONObject.parseObject(resultsign).getJSONObject("custom").getString("text");
                if (text.contains("已存在该条服务信息")) {
                    return "true";
                }
                return "false" + text;
            }
        } else {
            log.info("=====网厅连接失败=====");
        }
        return "false网厅接口连接失败";
    }

    /**
     * 根据环节，返回状态
     *
     * @param operateName
     * @return
     */
    public int getProjectStatus(String operateName) {
        String type = "30";
        if (StringUtil.isNotBlank(operateName)) {
            List<CodeItems> list = codeItemsService.listCodeItemsByCodeName("办件状态");
            if (EpointCollectionUtils.isNotEmpty(list)) {
                for (CodeItems codeItems : list) {
                    if (operateName.equals(codeItems.getItemText())) {
                        type = codeItems.getItemValue();
                        break;
                    }
                }

            }
        }
        return Integer.parseInt(type);
    }


    /**
     * 根据环节，返回状态
     *
     * @param operateName
     * @return
     */
    public int getOperateTypeKey(String operateName) {
        int type = 23;
        if (StringUtil.isNotBlank(operateName)) {
            switch (operateName) {
                case "预审通过":
                    type = 10;
                    break;
                case "预审打回":
                    type = 11;
                    break;
                case "外网已提交":
                    type = 12;
                    break;
                case "保存":
                    type = 20;
                    break;
                case "接件":
                    type = 21;
                    break;
                case "补正":
                    type = 22;
                    break;
                case "已受理":
                    type = 23;
                    break;
                case "不予受理":
                    type = 24;
                    break;
                case "办件收费":
                    type = 30;
                    break;
                case "收讫":
                    type = 31;
                    break;
                case "办件收费取消":
                    type = 32;
                    break;
                case "审批通过":
                    type = 35;
                    break;
                case "审批不通过":
                    type = 36;
                    break;
                case "暂停计时":
                    type = 40;
                    break;
                case "恢复计时":
                    type = 41;
                    break;
                case "批准":
                    type = 50;
                    break;
                case "不予批准":
                    type = 51;
                    break;
                case "办结":
                    type = 60;
                    break;
                case "撤销申请":
                    type = 61;
                    break;
                case "异常终止":
                    type = 62;
                    break;
                case "办件延期":
                    type = 70;
                    break;
                case "办件延期审批通过":
                    type = 71;
                    break;
                case "办件延期审批不通过":
                    type = 72;
                    break;
                case "结果发放":
                    type = 80;
                    break;
                case "评价":
                    type = 81;
                    break;
                default:
                    type = 0;
            }
        }
        return type;
    }

}
