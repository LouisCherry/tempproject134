

package com.epoint.auditorga.windowindex.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.cache.util.ZwfwCommonCacheUtil;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;

/**
 * 区域配置list页面对应的后台
 * 
 * @author Dong
 * @version [版本号, 2016-09-26 17:13:31]
 */
@RestController("windowindexaction")
@Scope("request")
public class WindowIndexAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    /**
     * 区域配置实体对象
     */
    private AuditOrgaArea dataBean;

    private Map<String, Integer> map;

    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;

    @Autowired
    private IWorkflowProcessVersionService workflowProcessVersionService;

    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

    @Autowired
    private IJNAuditProject auditProjectService;

    @Autowired
    private IAuditTask taskService;

    @Autowired
    private IAuditOnlineConsult onlineConsultService;

    @Autowired
    private IHandleConfig config;
    
    @Autowired
    private IConfigService configservice;

    private String itemcategory;
    
    private String notcache;

    private String windowguid;
    
    private static ZwfwCommonCacheUtil cacheUtil = new ZwfwCommonCacheUtil(1800);

    @Override
    public void pageLoad() {
        itemcategory = config.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
        notcache = config.getFrameConfig("AS_NO_INDEXCACHE", "").getResult();
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
    }

    //窗口人员业务办理信息
    public void getCountStatus() {
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            return;
        }
        String windowGuid = ZwfwUserSession.getInstance().getWindowGuid();
        String userguid = UserSession.getInstance().getUserGuid();
        
        String cacheKey = this.getClass().getSimpleName() + "-getCountStatus-" + windowGuid+userguid;
        // 先判断缓存有无数据 - 非第一次加载
        String cacheStr = ZwfwConstant.CONSTANT_STR_ONE.equals(notcache)?"":cacheUtil.getCacheByKey(cacheKey);
        if (StringUtil.isNotBlank(cacheStr)) {
            String[] values = cacheStr.split(";");
            if (values != null && values.length > 0) {
                addCallbackParam("todayHandle", values[0]);
                addCallbackParam("todayFinish", values[1]);
                addCallbackParam("msg", JSONObject.parseObject(values[2]));
            }
        }
        else {
            // 从数据库查询，然后放入缓存 - 第一次加载
            //办件状态数量
            List<String> taskidList = auditOrgaWindowService
                    .getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
            if (taskidList != null && taskidList.size() > 0) {
                List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
                if (taskidZJList != null && taskidZJList.size() > 0) {
                    for (String zjTaskid : taskidZJList) {
                        taskidList.remove(zjTaskid);
                    }
                }
            }
            String area = "";
            // 如果是镇村接件
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                area = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else {
                area = ZwfwUserSession.getInstance().getAreaCode();
            }
            map = auditProjectService.getCountStatusByWindowguidAndCenterguidAndAreacode(taskidList,
                    ZwfwUserSession.getInstance().getWindowGuid(), ZwfwUserSession.getInstance().getAreaCode(),
                    ZwfwUserSession.getInstance().getCenterGuid(), area).getResult();
            JSONObject dataJson = new JSONObject();
            JSONArray newJson = new JSONArray();
            JSONObject cJson = new JSONObject();

            int dys = 0;
            if (map != null) {
                dys = map.get("DYS");
            }
            cJson.put("count", dys);
            newJson.add(cJson);
            dataJson.put("DYS", newJson);

            newJson = new JSONArray();
            cJson = new JSONObject();
            int dbb = 0;
            if (map != null) {
                dbb = map.get("DBB");
            }
            cJson.put("count", dbb);
            newJson.add(cJson);
            dataJson.put("DBZ", newJson);

            newJson = new JSONArray();
            cJson = new JSONObject();
            int dsl = 0;
            if (map != null) {
                dsl = map.get("DSL");
            }
            cJson.put("count", dsl);
            newJson.add(cJson);
            dataJson.put("DSL", newJson);

            SqlConditionUtil sql = new SqlConditionUtil();
            String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
            sql.like("handleareacode", handleareacode + ",");
            sql.eq("areacode", area);
            sql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DJJ));
            sql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
            String taskids = "";
            Integer pageproject = null;
            if (taskidList != null && taskidList.size() > 0) {
                taskids = "'" + StringUtil.join(taskidList, "','") + "'";
                sql.in("task_id", taskids);
                pageproject = auditProjectService.getAuditProjectCountByCondition(sql.getMap()).getResult();

            }
            newJson = new JSONArray();
            cJson = new JSONObject();
            int spz = 0;
            if (pageproject != null) {
                spz = pageproject;
            }
            cJson.put("count", spz);
            newJson.add(cJson);
            dataJson.put("SPZ", newJson);

            newJson = new JSONArray();
            cJson = new JSONObject();
            int dbj = 0;
            if (map != null) {
                dbj = map.get("DBJ");
            }
            cJson.put("count", dbj);
            newJson.add(cJson);
            dataJson.put("DBJ", newJson);

            newJson = new JSONArray();
            cJson = new JSONObject();
            int yzt = 0;
            if (map != null) {
                yzt = map.get("YZT");
            }
            cJson.put("count", yzt);
            newJson.add(cJson);
            dataJson.put("YZT", newJson);

            //临近超期       
            int ljcq = 0;
            SqlConditionUtil sqlproject = new SqlConditionUtil();
            if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())|| ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                sqlproject.eq("a.areacode", ZwfwUserSession.getInstance().getBaseAreaCode());
            }
            else {
                sqlproject.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode());
            }
            sqlproject.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
            sqlproject.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
            sqlproject.eq("windowGuid", ZwfwUserSession.getInstance().getWindowGuid());
            sqlproject.lt("spareminutes", "1440");
            sqlproject.gt("spareminutes", "0");
            ljcq = auditProjectService.getPagaBySpareTimeCount(sqlproject.getMap()).getResult();
            newJson = new JSONArray();
            cJson = new JSONObject();
            cJson.put("count", ljcq);
            newJson.add(cJson);
            dataJson.put("LJCQ", newJson);
            //咨询回复
            SqlConditionUtil sqlzxhf = new SqlConditionUtil();
            sqlzxhf.eq("ouguid", userSession.getOuGuid());
            sqlzxhf.in("status", "0,2");
            sqlzxhf.eq("consulttype", "1");
            sqlzxhf.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
            dataJson.put("ZXHF", onlineConsultService.getConsultCount(sqlzxhf.getMap()).getResult());

            dataJson.put("name", UserSession.getInstance().getDisplayName());
            //正常办结
            sqlproject.clear();
            newJson = new JSONArray();
            cJson = new JSONObject();
            handleareacode = ZwfwUserSession.getInstance().getAreaCode();
//            sqlproject.like("handleareacode", handleareacode + ",");
            sqlproject.eq("areacode", area);
            sqlproject.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
            taskidList = auditOrgaWindowService.getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid())
                    .getResult();
            if (taskidList != null && taskidList.size() > 0) {
                taskids = "'" + StringUtil.join(taskidList, "','") + "'";
                sqlproject.in("task_id", taskids);
                int pageProject = auditProjectService
                        .getAuditProjectCountByCondition(sqlproject.getMap()).getResult();
                cJson.put("count", pageProject);
            }
            else {
                cJson.put("count", ZwfwConstant.CONSTANT_STR_ZERO);
            }

            newJson.add(cJson);
            dataJson.put("YBJ", newJson);

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
            //今日受理
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date dtFrom = calendar.getTime();
            sqlproject.clear();
            sqlproject.eq("windowGuid", ZwfwUserSession.getInstance().getWindowGuid());
            sqlproject.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
            sqlproject.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
            sqlproject.eq("ACCEPTUSERGUID", UserSession.getInstance().getUserGuid());
            sqlproject.gt("ACCEPTUSERDATE", dtFrom);
            SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
            // 条件sql
            StringBuffer sb = new StringBuffer();
            
            sb.append(sqlManageUtil.buildSql(sqlproject.getMap()));
            int todayHandle = 0;
            Integer tdHandle = auditProjectService.getTodayHandleProjectCount(sb.toString()).getResult();
            todayHandle = tdHandle == null ? ZwfwConstant.CONSTANT_INT_ZERO : tdHandle;
            //今日办结
            sqlproject.clear();
            sqlproject.eq("windowGuid", ZwfwUserSession.getInstance().getWindowGuid());
            sqlproject.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
            sqlproject.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
            sqlproject.gt("BANJIEDATE", dtFrom);
            // 条件sql
            StringBuffer sb2 = new StringBuffer();
            sb2.append(sqlManageUtil.buildSql(sqlproject.getMap()));
            Integer tdFinish = auditProjectService.getTodayHandleProjectCount(sb2.toString()).getResult();
            int todayFinish = tdFinish == null ? ZwfwConstant.CONSTANT_INT_ZERO : tdFinish;
            addCallbackParam("todayHandle", todayHandle);
            addCallbackParam("todayFinish", todayFinish);
            addCallbackParam("msg", dataJson);

            // 第一次加载，把需要的数据放入缓存 todayHandle;todayFinish;dataJson
            String value = todayHandle + ";" + todayFinish + ";" + dataJson.toJSONString();
            cacheUtil.putCacheByKey(cacheKey, value);
        }
    }

    //搜索办件联想
    public void getSearchKeys() {
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            return;
        }
        SqlConditionUtil sql = new SqlConditionUtil();

        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
        sql.like("handleareacode", handleareacode + ",");
        sql.eq("areacode", areacode);
        sql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DJJ));
        sql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));

        String taskids = "";
        List<String> taskidList = auditOrgaWindowService
                .getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
        //自建系统办件
        if (taskidList != null && taskidList.size() > 0) {

            List<String> taskidZJList = taskService.selectZJTaskByTaskids(taskidList).getResult();
            if (taskidZJList != null && taskidZJList.size() > 0) {
                for (String zjTaskid : taskidZJList) {
                    taskidList.remove(zjTaskid);
                }
            }
        }
        if (taskidList != null && taskidList.size() > 0) {
            taskids = "'" + StringUtil.join(taskidList, "','") + "'";

            sql.in("task_id", taskids);
        }
        sql.setOrderDesc("flowsn");
        String sortField = "rowguid,taskguid,projectname,applyername,flowsn";
        List<AuditProject> pageProject = auditProjectService.getAuditProjectListByCondition(sortField, sql.getMap())
                .getResult();

        JSONArray hotJson = new JSONArray();
        JSONObject hJson = new JSONObject();
        if (pageProject != null && pageProject.size() > 0) {
            for (AuditProject auditProject : pageProject) {
                hJson = new JSONObject();
                if (StringUtil.isNotBlank(auditProject.getFlowsn())) {
                    hJson.put("value", auditProject.getApplyername() + "-" + auditProject.getFlowsn());
                }
                else {
                    hJson.put("value", auditProject.getApplyername());
                }
                hJson.put("data", auditProject.getProjectname());
                hotJson.add(hJson);
            }
        }
        addCallbackParam("keys", hotJson);
    }

    //业务办理
    public void getBusiness() {
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            return;
        }
        String cacheKey = this.getClass().getSimpleName() + "-getBusiness-" + ZwfwUserSession.getInstance().getWindowGuid();
        String cacheStr = "";
        // 先判断缓存有无数据 - 非第一次加载
        cacheStr = ZwfwConstant.CONSTANT_STR_ONE.equals(notcache)?"":cacheUtil.getCacheByKey(cacheKey);
        if (StringUtil.isNotBlank(cacheStr)) {
       //     String[] values = cacheStr.split(";");
       //     if (values != null && values.length > 0) {
                addCallbackParam("tasks", cacheStr);
      //      }
        }
        else {
            String sortField = "ordernum";
            String sortOrder = "desc";
            PageData<AuditTask> pageData = null;

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);

            if (StringUtil.isNotBlank(itemcategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(itemcategory)
                    && StringUtil.isNotBlank(windowguid)) {
                String fields = "rowguid,task_id,type,jbjmode,taskname,processguid,yw_catalog_id";
                List<String> tasklists = auditOrgaWindowService.getTaskidsByWindow(windowguid).getResult();
                StringBuilder tasks = new StringBuilder();
                tasks.append("'");
                for (String string : tasklists) {
                    tasks.append(string);
                    tasks.append("','");
                }
                tasks.append("'");
                sql.in("task_id", tasks.toString());
                pageData = taskService.getAuditEnableTaskPageData(fields, sql.getMap(), 0, 5, sortField, sortOrder)
                        .getResult();
            }
            else {
                pageData = taskService.getAuditWindowTaskPageData(ZwfwUserSession.getInstance().getWindowGuid(),
                        sql.getMap(), 0, 5, sortField, sortOrder).getResult();
            }
            JSONArray daJson = new JSONArray();
            JSONObject taskJson = new JSONObject();
            for (AuditTask auditTask : pageData.getList()) {
                taskJson = new JSONObject();
                String url = getOperateURL(auditTask.getRowguid(), auditTask.getProcessguid(), auditTask.getTaskname(),
                        auditTask.getTask_id(), String.valueOf(auditTask.getType()), auditTask.getJbjmode(),auditTask.getYw_catalog_id()); 
                taskJson.put("createProject", url);
                daJson.add(taskJson);
            }

            addCallbackParam("tasks", daJson);
            String value = daJson.toJSONString();
            cacheUtil.putCacheByKey(cacheKey, value);
        }
    }

    /**
     * 判断是否存在岗位配置 [一句话功能简述] [功能详细描述]
     * 
     * @param taskguid
     * @param processguid
     * @param taskname
     * @param taskid
     * @param taskType
     * @param jbjMode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean getHasFlow(String taskguid, String processguid, String taskname, String taskid, String taskType,
            String jbjMode) {
        boolean returl = false;
        // 事项大项
        if (StringUtil.isBlank(taskid)) {
            returl = false;
        }
        else {

            String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(processguid);
            if (processversionguid != null) {
                List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                if (list != null && list.size() > 0) {
                    returl = true;
                }
                else {
                    returl = false;
                }
            }
            else {
                returl = true;
            }

        }
        return returl;

    }

    /**
     * 获取窗口事项名称链接地址 如果流程没有出现在过程版本信息表里面，返回 【<span
     * style=\"color:red\">流程配置错误</span>】 如果流程没有变迁条件，返回【<span
     * style=\"color:red\">尚未新增岗位</span>】 其余则返回以事项名称为内容的a标签
     * 
     * @param taskguid
     *            事项guid
     * @param processguid
     *            过程guid
     * @param taskname
     *            事项名称
     * @param taskid
     *            事项id
     * @return String 返回的HTMl
     */
    public String getOperateURL(String taskguid, String processguid, String taskname, String taskid, String taskType,
            String jbjMode,String ywcayalogid) {
        if (StringUtil.isBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            return "";
        }
        String returl = "";
        if (StringUtil.isNotBlank(ywcayalogid) && "1".equals(configservice.getFrameConfigValue("IS_CHECKNEWTASK"))) {
            taskname="【新】"+taskname;
        }
        String name = taskname;
        if (taskname.length() > 20) {
            name = taskname.substring(0, 20) + "...";
        }
        // 事项大项
        if (StringUtil.isBlank(taskid)) {
            returl = taskname;
        }
        else {
            if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType) && !ZwfwConstant.JBJMODE_STANDARD.equals(jbjMode)) {

                returl = "<a  href='javascript:void(0)' title='" + taskname + "' onclick='creatProject(\"" + taskid
                        + "\",\"\")'>" + name + "</a>";
            }
            else {
                String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(processguid);
                if (processversionguid != null) {
                    List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                    if (list != null && list.size() > 0) {
                        returl = "<a  href='javascript:void(0)' title='" + taskname + "' onclick='creatProject(\""
                                + taskid + "\",\"" + processguid + "\")'>" + name + "</a>";
                    }
                    else {
                        returl = "<a>" + taskname + "【<span style=\"color:red\">尚未新增岗位</span>】" + "</a>";
                    }
                }
                else {
                    returl = "<a>" + taskname + "【<span style=\"color:red\">流程配置错误</span>】" + "</a>";
                }
            }
        }
        return returl;
    }

    public AuditOrgaArea getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaArea();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaArea dataBean) {
        this.dataBean = dataBean;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
