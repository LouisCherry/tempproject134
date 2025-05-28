package com.epoint.auditorga.centerwatch.action;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitor.inter.IAuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.cache.util.ZwfwCommonCacheUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 中心领导首页面对应的后台
 * 
 */
@RestController("centerWatchAction")
@Scope("request")
public class CenterWatchAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 6672720034689124784L;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditProjectMonitor monitorService;

    @Autowired
    private IAuditOnlineConsult onlineConsult;

    @Autowired
    private IAuditProject ProjectService;

    @Autowired
    private IOuService ouService;
    
    @Autowired
    private IHandleConfig config;

    private String centerGuid;
    
    private String notcache;

    private static ZwfwCommonCacheUtil cacheUtil = new ZwfwCommonCacheUtil(1800);

    @Override
    public void pageLoad() {
        centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        notcache = config.getFrameConfig("AS_NO_INDEXCACHE", "").getResult();

    }

    /**
     * 
     * 中心办件数据的接口
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     * @return
     */
    public void getCenterProjects() {
        try {
            log.info("=======开始调用getCenterProjects接口=======");
            if (StringUtil.isBlank(centerGuid)) {
                log.info("========Exception信息======== centerGuid为空！");
                return;
            }
            String cacheKey = this.getClass().getSimpleName() + "-getCenterProjects-" + centerGuid;
            // 先判断缓存有无数据 - 非第一次加载
            String cacheStr = ZwfwConstant.CONSTANT_STR_ONE.equals(notcache)?"":cacheUtil.getCacheByKey(cacheKey);
            if (StringUtil.isNotBlank(cacheStr)) {
                addCallbackParam("data", JSONObject.parseObject(cacheStr));
            }
            else {
                JSONObject dataJson = new JSONObject();

                String areacode = "";
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }
                else {
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
                int onlineapply = 0;
                int total = 0;
                int jbj = 0;
                int cnj = 0;
                int sbj = 0;
                //近一月
                GregorianCalendar gcNew = new GregorianCalendar();
                gcNew.add(Calendar.MONTH, -1);
                Date dtFrom = gcNew.getTime();
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("areacode", areacode);
                List<Record> records = auditProjectService.getAuditProjectStatusCountByCondition(conditionsql.getMap())
                        .getResult();
                /*       if(records.size()==0){
                jbj = 0;
                cnj = 0;
                sbj = 0;
                       }*/
                if (records != null && records.size() > 0) {
                    for (Record r : records) {
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(r.getInt("tasktype")))) {
                            jbj = r.getInt("num");
                        }
                        else if (ZwfwConstant.ITEMTYPE_CNJ.equals(String.valueOf(r.getInt("tasktype")))) {
                            cnj = r.getInt("num");
                        }
                        else if (ZwfwConstant.ITEMTYPE_SBJ.equals(String.valueOf(r.getInt("tasktype")))) {
                            sbj = r.getInt("num");
                        }
                    }
                }
                total = jbj + cnj + sbj;
                JSONObject totalhandleJson = new JSONObject();
                totalhandleJson.put("total", total);
                JSONArray handletypeJson = new JSONArray();
                JSONObject handleJson = new JSONObject();
                handleJson.put("name", "即办件");
                handleJson.put("value", jbj);
                handletypeJson.add(handleJson);
                handleJson = new JSONObject();
                handleJson.put("name", "承诺件");
                handleJson.put("value", cnj);
                handletypeJson.add(handleJson);
                handleJson = new JSONObject();
                handleJson.put("name", "上报件");
                handleJson.put("value", sbj);
                handletypeJson.add(handleJson);
                totalhandleJson.put("handletype", handletypeJson);
                //办件办结             
                int jiShiBanJieLv = 0;
                int banJieNum = 0;
                int jiShiBanJieNum = 0;
                int cnsj = 0;
                int cnsjTiSulv = 0;
                int sjsjTiSulv = 0;
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.isNotBlank("SPARETIME");
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                Integer banJieNum1 = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap())
                        .getResult();
                banJieNum = banJieNum1 == null ? 0 : banJieNum1;
                //满意办件 TODO
                double MYDLv = 0;
                int MYDNum = 0;
                /* for (AuditProject auditProject : auditProjects) {
                AuditOnlineEvaluat auditOnlineEvaluat = auditOnlineEvaluatService
                        .selectEvaluatByClientIdentifier(auditProject.getRowguid()).getResult();
                if (auditOnlineEvaluat != null) {
                    int satisfied = Integer.parseInt(auditOnlineEvaluat.getSatisfied());
                    if (satisfied < 3) {
                        MYDNum += 1;
                    }
                }
                }*/
                //及时办结办件
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.gt("SPARETIME", ZwfwConstant.CONSTANT_STR_ZERO);
                conditionsql.ge("promiseenddate", "banjiedate");
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                jiShiBanJieNum = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();

                if (banJieNum != 0 && StringUtil.isNotBlank(jiShiBanJieNum)) {
                    jiShiBanJieLv = (int) (jiShiBanJieNum * 100.0 / banJieNum);
                    MYDLv = MYDNum * 100.0 / banJieNum;
                }
                //平均办结耗时
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.isNotBlank("SPARETIME");
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                cnsj = auditProjectService.getAvgSpendtime(conditionsql.getMap()).getResult();
                //平均承诺时间
                Record r = taskservice.getAvgPromiseAndanticipatedate(areacode).getResult();
                Double promise = 1440 * Double.valueOf(r.getStr("promise"));
                Double anticipate = 1440 * Double.valueOf(r.getStr("anticipate"));
                //法定时限
                if (banJieNum != 0 && StringUtil.isNotBlank(cnsj)) {
                    cnsjTiSulv = (int) ((promise - cnsj) * 100 / promise);
                    if (cnsjTiSulv < 0) {
                        cnsjTiSulv = 0;
                    }
                }
                //承诺时限
                if (banJieNum != 0 && StringUtil.isNotBlank(cnsj)) {
                    sjsjTiSulv = (int) ((anticipate - cnsj) * 100 / anticipate);
                    if (sjsjTiSulv < 0) {
                        sjsjTiSulv = 0;
                    }
                }

                //网上申报
                conditionsql.clear();
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.ge("ACCEPTUSERDATE", dtFrom);
                conditionsql.in("applyway", "10,11");
                onlineapply = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();

                JSONObject backresultJson = new JSONObject();
                backresultJson.put("cnsjTiSulv", sjsjTiSulv);
                backresultJson.put("sjsjTiSulv", cnsjTiSulv);
                backresultJson.put("finishrate", jiShiBanJieLv);
                backresultJson.put("goodrate", MYDLv);
                backresultJson.put("onlineapply", onlineapply);
                totalhandleJson.put("backresult", backresultJson);

                JSONObject specialJson = new JSONObject();
                int nohandle = 0;
                int nopass = 0;
                int delay = 0;
                int prevent = 0;
                //不予受理      
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL));
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                nohandle = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("nohandle", nohandle);
                //不予批准 办件审核不通过
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG));
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                nopass = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("nopass", nopass);
                //延期申请
                conditionsql.clear();
                conditionsql.in("is_delay", "1,10");
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                delay = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("delay", delay);
                //异常终止
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YCZZ));
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                prevent = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("prevent", prevent);
                totalhandleJson.put("special", specialJson);

                JSONObject abnormal = new JSONObject();
                //临近超期       
                int ljcq = 0;
                conditionsql.clear();
                conditionsql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode());
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("a.centerGuid", ZwfwUserSession.getInstance().getCenterGuid());
                conditionsql.lt("spareminutes", "1440");
                String field = "a.rowguid rowguid,status,pviguid,projectname,windowname,flowsn,applyername,applydate,ACCEPTUSERDATE";
                PageData<AuditProject> projects = auditProjectService
                        .getPagaBySpareTime(field, conditionsql.getMap(), 0, 0, "", "").getResult();
                if (projects != null && projects.getRowCount() > 0) {
                    for (AuditProject auditProject : projects.getList()) {
                        AuditProjectSparetime auditProjectSparetime = projectSparetimeService
                                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                        if (auditProjectSparetime != null) {
                            if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                                int i = auditProjectSparetime.getSpareminutes();
                                if (i > 0 && i < 1440) {
                                    ljcq += 1;
                                }
                            }
                        }
                    }
                }
                abnormal.put("ljcq", ljcq);
                //超期       
                int overTime = 0;
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerGuid", ZwfwUserSession.getInstance().getCenterGuid());
                projects = auditProjectService.getAuditProjectPageData("rowguid", conditionsql.getMap(), 0, 0, "", "")
                        .getResult();
                if (projects != null && projects.getRowCount() > 0) {
                    for (AuditProject auditProject : projects.getList()) {
                        AuditProjectSparetime auditProjectSparetime = projectSparetimeService
                                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                        if (auditProjectSparetime != null) {
                            if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                                int i = auditProjectSparetime.getSpareminutes();
                                if (i <= 0) {
                                    overTime += 1;
                                }
                            }
                        }
                    }
                }
                abnormal.put("overTime", overTime);
                //不满意度
                int unsatisfy = 0;
                auditProjectService.getAuditProjectListByCondition("rowguid", conditionsql.getMap()).getResult();

                /* for (AuditProject auditProject : auditProjects) {
                AuditOnlineEvaluat auditOnlineEvaluat = auditOnlineEvaluatService
                    .selectEvaluatByClientIdentifier(auditProject.getRowguid()).getResult();
                if (auditOnlineEvaluat != null) {
                int satisfied = Integer.parseInt(auditOnlineEvaluat.getSatisfied());
                if (satisfied >= 3) {
                    unsatisfy += 1;
                }
                }
                }*/
                abnormal.put("unsatisfy", unsatisfy);
                //异常总数
                int abnormalSum = 0;
                abnormalSum = overTime + ljcq;
                abnormal.put("abnormalSum", abnormalSum);

                int unsatisfyRate = 0;
                if (abnormalSum != 0 && StringUtil.isNotBlank(unsatisfy)) {
                    unsatisfyRate = (int) (unsatisfy * 100.0 / total);
                }
                abnormal.put("unsatisfyRate", unsatisfyRate);

                int overTimeRate = 0;
                if (abnormalSum != 0 && StringUtil.isNotBlank(overTime)) {
                    overTimeRate = (int) (overTime * 100.0 / total);
                }
                abnormal.put("overTimeRate", overTimeRate);

                int ljcqRate = 0;
                if (abnormalSum != 0 && StringUtil.isNotBlank(ljcq)) {
                    ljcqRate = (int) (ljcq * 100.0 / total);
                }
                abnormal.put("ljcqRate", ljcqRate);

                totalhandleJson.put("abnormal", abnormal);

                //督办
                conditionsql.clear();
                conditionsql.ge("supervise_date", dtFrom);
                conditionsql.eq("AREACODE", areacode);
                PageData<AuditProjectMonitor> pageData = monitorService
                        .getMonitorPageData(conditionsql.getMap(), Integer.valueOf(ZwfwConstant.CONSTANT_STR_ZERO),
                                Integer.valueOf(ZwfwConstant.CONSTANT_STR_ZERO), "", "")
                        .getResult();
                int watch = pageData.getRowCount();
                totalhandleJson.put("watch", watch);

                //投诉
                conditionsql.clear();
                conditionsql.ge("askdate", dtFrom);
                conditionsql.eq("consulttype", "2");
                conditionsql.eq("centerguid", centerGuid);
                Integer complainCount = onlineConsult.getConsultCount(conditionsql.getMap()).getResult();
                totalhandleJson.put("complain", complainCount);

                //咨询
                conditionsql.clear();
                conditionsql.ge("askdate", dtFrom);
                conditionsql.eq("consulttype", "1");
                conditionsql.eq("centerguid", centerGuid);
                Integer consultCount = onlineConsult.getConsultCount(conditionsql.getMap()).getResult();
                totalhandleJson.put("consult", consultCount);
                dataJson.put("totalhandle", totalhandleJson);

                JSONObject oaJson = new JSONObject();

                String oarest = "";
                String path = ConfigUtil.getConfigValue("thirdSysPath");
                if (StringUtil.isNotBlank(path)) {
                    String[] thirdSysPath = path.split(";");
                    for (int i = 0; i < thirdSysPath.length; i++) {
                        String[] patharr = thirdSysPath[i].toString().split(":");
                        if (patharr.length == 2) {
                            String realPath = patharr[0];
                            String rootName = patharr[1];
                            if ("oa9".equals(rootName)) {
                                oarest = realPath;
                            }
                        }
                    }
                }
                String categoryguid = ConfigUtil.getConfigValue("categoryguid");
                oaJson.put("oarest", "/../" + oarest + "/rest");
                oaJson.put("categoryguid", categoryguid);
                dataJson.put("oa", oaJson);
                log.info("=======结束调用getCenterProjects接口=======");
                this.addCallbackParam("data", dataJson);

                // 第一次加载，把需要的数据放入缓存
                String value = dataJson.toJSONString();
                cacheUtil.putCacheByKey(cacheKey, value);
            }
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            log.info("=======getCenterProjects异常信息：" + e.getMessage() + "=======");
        }
    }

    /**
     * 首页元件数据获取
     */
    public Map<String, Object> getPortalData() {
        String query = getRequestParameter("query");
        Map<String, Object> result = new HashMap<String, Object>(16);
        if (StringUtil.isNotBlank(query)) {
            // 查询消息
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("AREACODE", ZwfwUserSession.getInstance().getAreaCode());
            sql.isBlankOrValue("end_date", "");
            String sortField = "supervise_date";
            String sortOrder = "desc";
            PageData<AuditProjectMonitor> pageData = monitorService
                    .getMonitorPageData(sql.getMap(), 0, 10, sortField, sortOrder).getResult();
            // 组装返回数据
            if (pageData.getList() != null) {
                // 消息列表
                List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
                for (AuditProjectMonitor projectMonitor : pageData.getList()) {
                    String fields = "  rowguid,taskguid,projectname,pviguid ";
                    AuditProject auditProject = ProjectService
                            .getAuditProjectByFlowsn(fields, projectMonitor.getBj_no(), projectMonitor.getAreacode())
                            .getResult();
                    if (auditProject != null) {
                        Map<String, Object> messageMap = new HashMap<String, Object>(16);
                        messageMap.put("projectguid", auditProject.getRowguid());
                        messageMap.put("title", auditProject.getProjectname());
                        messageMap.put("pviguid", auditProject.getPviguid());
                        messageMap.put("date",
                                EpointDateUtil.convertDate2String(projectMonitor.getOperatedate(), "MM-dd"));// 列表显示日期
                        messageList.add(messageMap);
                    }
                }
                result.put("messageList", messageList);
            }
        }

        return result;
    }

    /**
     * 首页元件数据获取
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getWeekLineData() {
        Map<String, Object> result = new HashMap<String, Object>(16);

        String cacheKey = this.getClass().getSimpleName() + "-getWeekLineData-" + centerGuid;
        // 先判断缓存有无数据 - 非第一次加载
        String cacheStr = ZwfwConstant.CONSTANT_STR_ONE.equals(notcache)?"":cacheUtil.getCacheByKey(cacheKey);
        if (StringUtil.isNotBlank(cacheStr)) {
            List<Map<String, Object>> messageList = (List<Map<String, Object>>) JSONArray.parse(cacheStr);
            result.put("messageList", messageList);
        }
        else {
            String query = getRequestParameter("query");
            if (StringUtil.isNotBlank(query)) {
                // 消息列表
                List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();
                int count = 0;
                GregorianCalendar gcNew = new GregorianCalendar();
                gcNew.add(Calendar.MONTH, -1);
                Date dtFrom = gcNew.getTime();
                List<Record> weekLines = auditProjectService.getWeekLineList(
                        ZwfwUserSession.getInstance().getCenterGuid(), ZwfwUserSession.getInstance().getAreaCode(),
                        String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL), dtFrom).getResult();
                if (weekLines != null && weekLines.size() > 0) {
                    for (int i = 0; i < weekLines.size(); i++) {
                        if (i >= 5) {
                            break;
                        }
                        count++;
                        Map<String, Object> messageMap = new HashMap<String, Object>(16);
                        FrameOu ou = ouService.getOuByOuGuid(weekLines.get(i).getStr("ouguid"));
                        messageMap.put("count", count);
                        messageMap.put("title", ou.getOuname());
                        messageMap.put("weekLine", weekLines.get(i).get("num"));
                        messageList.add(messageMap);
                    }
                }
                result.put("messageList", messageList);

                // 第一次加载，把需要的数据放入缓存
                JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(messageList));
                cacheUtil.putCacheByKey(cacheKey, array.toJSONString());
            }
        }
        return result;
    }

}
