package com.epoint.auditorga.leaderindex.action;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditorga.leaderindex.service.JNLeaderIndexService;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditorga.auditorgacompany.inter.IAuditOrgaCompany;
import com.epoint.basic.auditorga.auditorgaredflagwindow.domain.AuditOrgaRedflagwindow;
import com.epoint.basic.auditorga.auditorgaredflagwindow.inter.IAuditOrgaRedflagwindow;
import com.epoint.basic.auditorga.auditorgaredflagwindowr.domain.AuditOrgaRedflagwindowR;
import com.epoint.basic.auditorga.auditorgaxiangmu.inter.IAuditOrgaXiangmu;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.hottask.inter.IAuditTaskHottask;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.external.ICertInfoExternal;
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
import com.epoint.jnzwfw.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;

/**
 * 中心领导首页面对应的后台
 * 
 */
@RestController("jnleaderIndexAction")
@Scope("request")
public class JNLeaderIndexAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    private JNLeaderIndexService service = new JNLeaderIndexService();

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditOnlineConsult auditOnlineConsultService;

    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditLogisticsBasicInfo auditLogisticsBasicInfoService;

    @Autowired
    private IAuditOrgaCompany companyservice;

    @Autowired
    private IAuditOrgaXiangmu xiangmuservice;

    @Autowired
    private IAuditOrgaRedflagwindow redflagservice;

    @Autowired
    private IAuditTaskHottask iAuditTaskHottask;

    @Autowired
    private ICertInfoExternal icertinfoexternal;
    
    @Autowired
    private IHandleConfig config;
    
    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    private String centerGuid;
    
    private String notcache;
    
    private static ZwfwCommonCacheUtil cacheUtil = new ZwfwCommonCacheUtil(1800);
    
    @Autowired
    private IJNAuditProjectRestService service2;

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
                DecimalFormat df = new DecimalFormat("0.00");
                //定义返回JSON对象    
                JSONObject dataJson = new JSONObject();
                JSONArray newJson = new JSONArray();
                JSONObject cJson = new JSONObject();
                cJson.put("count", 10);
                newJson.add(cJson);
                cJson = new JSONObject();
                cJson.put("count", 10);
                newJson.add(cJson);
                cJson = new JSONObject();
                cJson.put("count", 10);
                newJson.add(cJson);
                dataJson.put("news", newJson);
                String areacode = "";
                if (ZwfwUserSession.getInstance().getCitylevel() != null
                        && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
                    areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                }
                else {
                    areacode = ZwfwUserSession.getInstance().getAreaCode();
                }
                int onlineapply = 0;
                int total = 0;
                int jbj = 0;
                int cnj = 0;
                int sbj = 0;
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
                //近一月
                GregorianCalendar gcNew = new GregorianCalendar();
                gcNew.set(Calendar.MONTH, gcNew.get(Calendar.MONTH) - 1);
                Date dtFrom = gcNew.getTime();
                conditionsql.ge("APPLYDATE", dtFrom);
                List<Record> records = auditProjectService.getAuditProjectStatusCountByCondition(conditionsql.getMap())
                        .getResult();
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
                JSONObject totalhandleJson = new JSONObject();
                total = jbj + cnj + sbj;
                totalhandleJson.put("total", total);
                JSONObject weekhandleJson = new JSONObject();
                weekhandleJson.put("total", total);

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
                weekhandleJson.put("handletype", handletypeJson);
                //办件办结             
                String jiShiBanJieLv = "0";
                int banJieNum = 0;
                int jiShiBanJieNum = 0;
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.isNotBlank("SPARETIME");
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                banJieNum = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                //满意办件 
                conditionsql.clear();

                //评价为满意的办件数量
                int satisfiedNum = 0;
                String MydLevel = "0";
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                //申报类型为网上申报
                conditionsql.in("applyway", "10,11");
                List<Record> auditProjects = auditProjectService.getAuditProjectSatisfiedList(conditionsql.getMap())
                        .getResult();
                int totalcount = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                if (auditProjects != null && auditProjects.size() > 0) {
                    for (Record auditProject : auditProjects) {
                        if (StringUtil.isNotBlank(auditProject.get("Satisfied"))) {
                            int satisfied = Integer.parseInt(auditProject.get("Satisfied"));
                            //计算评价在满意以上的办件数量
                            if (satisfied >= 3) {
                                satisfiedNum += auditProject.getInt("count");
                            }
                        }
                    }
         //           MydLevel = (int) (satisfiedNum * 100.0 / totalcount);
                    float num = (float) satisfiedNum * 100 / totalcount;
                    MydLevel = df.format(num);
                }
                
                String MydLevel2 = getSatis(areacode);
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
                    float num = (float) jiShiBanJieNum * 100 / banJieNum;
                    jiShiBanJieLv = df.format(num);
                }

                //网上申报
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.ge("ACCEPTUSERDATE", dtFrom);
                conditionsql.in("applyway", "10,11");
                onlineapply = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();

                JSONObject backresultJson = new JSONObject();
                backresultJson.put("finishrate", jiShiBanJieLv);
                backresultJson.put("goodrate", MydLevel2);
                backresultJson.put("onlineapply", onlineapply);
                weekhandleJson.put("backresult", backresultJson);

                JSONObject countJson = new JSONObject();
                //TODO 取号量
              
                String queuecount = service.getQueuecount(EpointDateUtil.convertDate2String(dtFrom));
                
                countJson.put("getcount", queuecount);

                //受理量
                int shouLiNum = 0;
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.ge("APPLYDATE", dtFrom);
                shouLiNum = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                countJson.put("dealcout", shouLiNum);
                //办结量
                conditionsql.clear();
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("BANJIEDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("areacode", areacode);
                banJieNum = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                countJson.put("finishcount", banJieNum);
                //办件投诉
                int handlecomplain = 0;
                conditionsql.clear();
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("consulttype", ZwfwConstant.CONSULT_TYPE_TS);
                conditionsql.ge("ASKDATE", dtFrom);
                handlecomplain = auditOnlineConsultService.getConsultCount(conditionsql.getMap()).getResult();
                countJson.put("handlecomplain", handlecomplain);
                weekhandleJson.put("count", countJson);

                JSONObject specialJson = new JSONObject();
                int nohandle = 0;
                int nopass = 0;
                int delay = 0;
                int prevent = 0;
                //不予受理      
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL));
                conditionsql.ge("APPLYDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                nohandle = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("nohandle", nohandle);
                //不予批准 办件审核不通过
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG));
                conditionsql.ge("ACCEPTUSERDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                nopass = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("nopass", nopass);
                //延期申请
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.in("is_delay", "1,10");
                conditionsql.ge("ACCEPTUSERDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                delay = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("delay", delay);
                //异常终止
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YCZZ));
                conditionsql.ge("ACCEPTUSERDATE", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                prevent = auditProjectService.getAuditProjectCountByCondition(conditionsql.getMap()).getResult();
                specialJson.put("prevent", prevent);
                weekhandleJson.put("special", specialJson);
                JSONArray hotJson = new JSONArray();
                JSONObject hJson = new JSONObject();

                List<String> taskList = new ArrayList<String>();
                List<String> taskIdList = auditOrgaWindowService.getTaskIdListByCenter(centerGuid).getResult();
                if (taskIdList != null && taskIdList.size() > 0) {
                    for (String taskId : taskIdList) {
                        AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(taskId).getResult();
                        if (auditTask != null && ZwfwConstant.CONSTANT_INT_ONE == auditTask.getIs_enable()) {
                            taskList.add(auditTask.getTask_id());
                        }
                    }
                }
                //热门办件
                conditionsql.clear();
                conditionsql.eq("a.area", ZwfwUserSession.getInstance().getAreaCode());
                conditionsql.ge("a.enable", ZwfwConstant.CONSTANT_STR_ONE);
                List<AuditTaskHottask> hotProjectList = iAuditTaskHottask
                        .getAuditHottaskPageData(conditionsql.getMap(), 0, 10, "ordernum", "desc").getResult()
                        .getList();
                List<String> hotTaskidList = new ArrayList<String>();
                if (hotProjectList != null && hotProjectList.size() > 0) {
                    for (AuditTaskHottask hotProject : hotProjectList) {
                        hotTaskidList.add(hotProject.getTaskid());
                    }
                }
                List<Record> hotProjectRecordList = auditProjectService
                        .getHotBanJianList(hotTaskidList, centerGuid, ZwfwUserSession.getInstance().getAreaCode())
                        .getResult();
                if (hotProjectRecordList != null && hotProjectRecordList.size() > 0) {
                    for (Record hotProject : hotProjectRecordList) {
                        hJson = new JSONObject();
                        hJson.put("num", hotProject.get("num"));
                        AuditTask auditTask = auditTaskService.getUseTaskAndExtByTaskid(hotProject.get("TASK_ID"))
                                .getResult();
                        if (auditTask != null) {
                            hJson.put("hot_name", auditTask.getTaskname());
                            hJson.put("hot_url", auditTask.getRowguid());
                        }
                        hotJson.add(hJson);
                    }
                }
                weekhandleJson.put("hot", hotJson);

                JSONObject sendcountJson = new JSONObject();
                //电子证照数
                //电子证照
                int certInfoCount = 0;
                //电子证          
                if (StringUtil.isNotBlank(areacode)) {
                    String jsonstr = icertinfoexternal.getCertinfoCountByDate(areacode, null, null);
                    JSONObject js = JSON.parseObject((jsonstr));
                    certInfoCount = js.getIntValue("month");
                }
                sendcountJson.put("electrical", certInfoCount);

                //物流快递数
                int express = 0;
                conditionsql.clear();
                conditionsql.ge("sendtime", dtFrom);
                conditionsql.eq("centerguid", centerGuid);
                express = auditLogisticsBasicInfoService.getExpressCountByCondition(conditionsql.getMap()).getResult();
                sendcountJson.put("express", express);

                weekhandleJson.put("sendcount", sendcountJson);
                weekhandleJson.put("handletype", handletypeJson);
                dataJson.put("weekhandle", weekhandleJson);
                //中心概况
                JSONObject centerJson = new JSONObject();
                //入驻部门            
                int deptno = 0;
                deptno = auditOrgaWindowService.getoulistBycenterguid(centerGuid).getResult().size();
                centerJson.put("depcount", deptno);
                //业务窗口
                int windowcount = 0;
                conditionsql.clear();
                conditionsql.eq("centerguid", centerGuid);
                windowcount = auditOrgaWindowService.getWindowCount(conditionsql.getMap()).getResult();
                centerJson.put("windowcount", windowcount);

                //入驻事项centerJson
                int mattercount = 0;
                if (taskList != null && taskList.size() > 0) {
                    mattercount = taskList.size();
                }
                centerJson.put("mattercount", mattercount);

                JSONArray ringechartsJson = new JSONArray();
                JSONObject ringeJson = new JSONObject();
                ringeJson.put("name", "即时办理");
                ringeJson.put("value", jbj);
                ringechartsJson.add(ringeJson);
                ringeJson = new JSONObject();
                ringeJson.put("name", "限时审批");
                ringeJson.put("value", cnj);
                ringechartsJson.add(ringeJson);
                ringeJson = new JSONObject();
                ringeJson.put("name", "上报审批");
                ringeJson.put("value", sbj);
                ringechartsJson.add(ringeJson);
                centerJson.put("ringecharts", ringechartsJson);
                //窗口工作人员数
                int workernum = 0;
                workernum = auditOrgaWindowService.getWindowUserByCenterGuid(centerGuid).getResult();
                centerJson.put("workernum", workernum);

                //红旗窗口
                conditionsql.clear();
                //最近一月            
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                String mon = EpointDateUtil.convertDate2String(m, "yyyy-MM");
                conditionsql.eq("centerguid", centerGuid);
                conditionsql.eq("belongmonth", mon);
                //查询当月的评比
                List<AuditOrgaRedflagwindow> redFlag = redflagservice.showWindow(conditionsql.getMap()).getResult();
                List<String> rowGuids = new ArrayList<>();
                if (redFlag != null && redFlag.size() > 0) {
                    for (AuditOrgaRedflagwindow auditOrgaRedflagwindow : redFlag) {
                        rowGuids.add("'" + auditOrgaRedflagwindow.getRowguid() + "'");
                    }
                }
                conditionsql.clear();
                conditionsql.eq("centerguid", centerGuid);
                List<AuditOrgaRedflagwindowR> redFlagR = new ArrayList<>();
                if (rowGuids != null && rowGuids.size() > 0) {
                    conditionsql.in("recordguid", StringUtil.join(rowGuids, ","));
                    //查询当月评比下的红旗窗口及评选次数
                    List<AuditOrgaRedflagwindowR> redflagR = redflagservice.showWindowR(conditionsql.getMap())
                            .getResult();
                    //取前三个红旗窗口显示
                    if (redflagR != null && redflagR.size() > 0) {
                        for (int i = 0; i < redflagR.size(); i++) {
                            if (i >= 3) {
                                break;
                            }
                            redFlagR.add(redflagR.get(i));
                        }
                    }
                }
                centerJson.put("redflag", redFlagR);

                //收费
                int freepercent = 0;
                conditionsql.clear();
                if (taskList != null && taskList.size() > 0) {
                    for (String taskid : taskList) {
                        AuditTask task = auditTaskService.selectUsableTaskByTaskID(taskid).getResult();
                        if (task != null && task.getCharge_flag() != null
                                && ZwfwConstant.CONSTANT_INT_ZERO == task.getCharge_flag()) {
                            freepercent++;
                        }
                    }
                }
                int count = 0;
                count = taskList.size();
                int unfreepercewnt = 0;
                if (count != 0) {
                    freepercent = (int) (freepercent * 100.0 / count);
                    unfreepercewnt = 100 - freepercent;
                }
                centerJson.put("freepercent", freepercent);
                centerJson.put("unfreepercewnt", unfreepercewnt);

                dataJson.put("center", centerJson);
                //近半年企业设立数量
                JSONObject setcountJson = new JSONObject();
                JSONArray barlineJson = new JSONArray();
                JSONObject barJson = null;
                List<Object> list = companyservice.monthAndNum(ZwfwUserSession.getInstance().getCenterGuid())
                        .getResult();
                String[] mous = (String[]) list.get(0);
                int[] nums = (int[]) list.get(1);
                for (int i = 5; i >= 0; i--) {
                    barJson = new JSONObject();
                    barJson.put("name", mous[i]);
                    barJson.put("value", nums[i]);
                    barlineJson.add(barJson);
                }
                setcountJson.put("maxcompany", list.get(2));
                setcountJson.put("barline", barlineJson);
                Record companytypeandnum = companyservice.typeAndNum(ZwfwUserSession.getInstance().getCenterGuid())
                        .getResult();
                setcountJson.put("total", isNull(companytypeandnum.getInt("totalcount")));
                JSONArray listJson = new JSONArray();
                JSONObject liJson = new JSONObject();
                liJson.put("name", "国有企业");
                liJson.put("count", isNull(companytypeandnum.getInt("gyqycount")));
                listJson.add(liJson);
                liJson = new JSONObject();
                liJson.put("name", "民营企业");
                liJson.put("count", isNull(companytypeandnum.getInt("myqycount")));
                listJson.add(liJson);
                liJson = new JSONObject();
                liJson.put("name", "合资企业");
                liJson.put("count", isNull(companytypeandnum.getInt("hzqycount")));
                listJson.add(liJson);
                liJson = new JSONObject();
                liJson.put("name", "外资企业");
                liJson.put("count", isNull(companytypeandnum.getInt("wzqycount")));
                listJson.add(liJson);
                liJson = new JSONObject();
                liJson.put("name", "其他企业");
                liJson.put("count", isNull(companytypeandnum.getInt("qtcount")));
                listJson.add(liJson);
                setcountJson.put("list", listJson);
                dataJson.put("setcount", setcountJson);

                JSONObject approvecountJson = new JSONObject();
                JSONArray barlinespJson = new JSONArray();
                JSONObject barspJson = new JSONObject();
                //近半年项目审批数量

                list = xiangmuservice.monthAndNum(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                mous = (String[]) list.get(0);
                nums = (int[]) list.get(1);
                Record record = service.getSpProject(areacode);
                for (int i = 5; i >= 0; i--) {
                    barspJson = new JSONObject();
                    barspJson.put("name", mous[i]);
                    barspJson.put("value", record.getStr(""+i));
                    barlinespJson.add(barspJson);
                }
                // Record record=spInstanceService.getAuditSpCountByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                /*             String[] mous1 = new String[6];
                            Date nowdate = new Date();
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM");
                            String mouth = "";
                            for (int i = 0; i < 6; i++) {
                c.setTime(nowdate);
                c.add(Calendar.MONTH, -(i+1));
                mouth = format.format(c.getTime()).substring(5);
                mous1[i] = mouth;
                            }
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m6"));
                            barspJson.put("name", mous1[5]);
                            barlinespJson.add(barspJson);
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m5"));
                            barspJson.put("name", mous1[4]);
                            barlinespJson.add(barspJson);
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m4"));
                            barspJson.put("name", mous1[3]);
                            barlinespJson.add(barspJson);
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m3"));
                            barspJson.put("name", mous1[2]);
                            barlinespJson.add(barspJson);
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m2"));
                            barspJson.put("name", mous1[1]);
                            barlinespJson.add(barspJson);
                            barspJson = new JSONObject();
                            barspJson.put("value", record.get("m1"));
                            barspJson.put("name", mous1[0]);
                            barlinespJson.add(barspJson);*/
                approvecountJson.put("barline", barlinespJson);
                approvecountJson.put("maxxiangmu", list.get(2));
                Record xiangmutypeandnum = xiangmuservice.typeAndNum(ZwfwUserSession.getInstance().getCenterGuid())
                        .getResult();

                approvecountJson.put("total", isNull(xiangmutypeandnum.getInt("totalcount")));
                JSONArray listJson2 = new JSONArray();
                JSONObject liJson2 = new JSONObject();
                liJson2.put("name", "工业类");
                liJson2.put("count", isNull(xiangmutypeandnum.getInt("gycount")));
                listJson2.add(liJson2);
                liJson2 = new JSONObject();
                liJson2.put("name", "市政类");
                liJson2.put("count", isNull(xiangmutypeandnum.getInt("szcount")));
                listJson2.add(liJson2);
                liJson2 = new JSONObject();
                liJson2.put("name", "影视娱乐类");
                liJson2.put("count", isNull(xiangmutypeandnum.getInt("ysylcount")));
                listJson2.add(liJson2);
                liJson2 = new JSONObject();
                liJson2.put("name", "土地开发类");
                liJson2.put("count", isNull(xiangmutypeandnum.getInt("tdkfcount")));
                listJson2.add(liJson2);
                liJson2 = new JSONObject();
                liJson2.put("name", "其他类");
                liJson2.put("count", isNull(xiangmutypeandnum.getInt("qtcount")));
                listJson2.add(liJson2);
                approvecountJson.put("list", listJson2);
                dataJson.put("approvecount", approvecountJson);

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
             
                JSONObject abnormal = new JSONObject();
                //超期       
                int overTime = 0;
                conditionsql.clear();
                conditionsql.eq("areacode", areacode);
                conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                conditionsql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                conditionsql.ge("applydate", dtFrom);
                conditionsql.eq("centerGuid", ZwfwUserSession.getInstance().getCenterGuid());
                PageData<AuditProject> projects = auditProjectService.getAuditProjectPageData("rowguid", conditionsql.getMap(), 0, 0, "", "")
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
                projects = auditProjectService
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
            
                //异常总数
                int abnormalSum = 0;
                abnormalSum = overTime + ljcq;
                abnormal.put("abnormalSum", abnormalSum);
                
                int ljcqRate = 0;
                if (abnormalSum != 0 && StringUtil.isNotBlank(ljcq)) {
                    ljcqRate = (int) (ljcq * 100.0 / total);
                }
                abnormal.put("ljcqRate", ljcqRate);

                int overTimeRate = 0;
                if (abnormalSum != 0 && StringUtil.isNotBlank(overTime)) {
                    overTimeRate = (int) (overTime * 100.0 / total);
                }
                abnormal.put("overTimeRate", overTimeRate);
                
                totalhandleJson.put("abnormal", abnormal);
                
                String categoryguid = ConfigUtil.getConfigValue("categoryguid");
                oaJson.put("oarest", "/../" + oarest + "/rest");
                oaJson.put("categoryguid", categoryguid);
                dataJson.put("oa", oaJson);
                dataJson.put("totalhandle", totalhandleJson);
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

    private String getSatis(String areacode) {
        AuditProject auditProject = service.getSatisfiedCount(areacode);
        int satiscount = Integer.parseInt(auditProject.getStr("satiscount"));
        int totalAudit = Integer.parseInt(service.getTotalSatisfiedCount(areacode).getStr("totalAudit"));
        DecimalFormat df = new DecimalFormat("0.0");
        String satis = df.format((float)(totalAudit-satiscount)*100/totalAudit);
        return satis;
    }

    private int isNull(Integer num) {
        if (num == null) {
            return 0;
        }
        else {
            return num;
        }
    }

}
