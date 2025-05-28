package com.epoint.zoucheng.znsb.worktablecomment.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain.ZCAuditZnsbCommentPeople;
import com.epoint.zoucheng.znsb.auditznsbcommentpeople.inter.IZCAuditZnsbCommentPeopleService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.code.entity.CodeMain;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 
 *  [工作台评价模块rest] 
 * @author chencong
 * @version [版本号, 2020年3月30日]
 */
@RestController
@RequestMapping("/zcworktablecomment")
public class ZCWorkTableCommentController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditOrgaWindowYjs auditorgawindow;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditOrgaServiceCenter servicecenter;
    @Autowired
    private ICodeItemsService codeitemservice;
    @Autowired
    private ICodeMainService codemainservice;
    @Autowired
    private IZCAuditZnsbCommentPeopleService peopleservice;
    @Autowired
    private IZCAuditZnsbCommentMatterService matterservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditProject projectservice;
    @Autowired
    private IAuditOnlineEvaluat evaluateservice;
    /**
     * 
     *  [评价人员模块部门获取] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getDepartList", method = RequestMethod.POST)
    public String getDepartList(@RequestBody String params) {
        log.info("================调用getDepartList评价人员模块部门获取接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");
            String centerguid = obj.getString("centerguid");
            String departname = obj.getString("departname");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> departlist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            SqlConditionUtil windowcountsql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            // 查询中心名称
            dataJson.put("centername", servicecenter.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
            sql.setSelectFields(" distinct ouguid ");
            // 获取窗口关联的部门guid
            List<AuditOrgaWindow> oulist = auditorgawindow.getAllWindow(sql.getMap()).getResult();
            // 通过部门guid获取对应的部门对象
            for (AuditOrgaWindow window : oulist) {
                JSONObject data = new JSONObject();
                String ouguid = window.getOuguid();
                FrameOu frameou = ouservice.getOuByOuGuid(ouguid);
                if(StringUtil.isNotBlank(frameou)){
                    data.put("ouguid", ouguid);
                    data.put("ouname", StringUtil.isNotBlank(frameou.getOushortName()) ? frameou.getOushortName()
                            : frameou.getOuname());
                    data.put("ougnum", frameou.getOrderNumber());
                    // 查询部门下有多少窗口
                    windowcountsql.eq("ouguid", ouguid);
                    int windowcount = auditorgawindow.getWindowCount(windowcountsql.getMap()).getResult();
                    data.put("windowcount", StringUtil.getNotNullString(windowcount));
                    // 对部门进行查询
                    if(frameou.getOuname().indexOf(departname)!=-1){
                        departlist.add(data);
                    }
                }
            }
            //根据ougnum对部门数据进行降序排序
            Collections.sort(departlist,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("ougnum") - l1.getIntValue("ougnum"));
            //截取对应的部门list数据
            int firstint = Integer.parseInt(currentpage) * Integer.parseInt(pagesize);
            int endint = (firstint + Integer.parseInt(pagesize)) >= departlist.size() ? departlist.size()
                    : (firstint + Integer.parseInt(pagesize));
            List<JSONObject> rtnlist = departlist.subList(firstint, endint);
            int oucount = departlist.size();
            dataJson.put("departlist", rtnlist);
            dataJson.put("departcount", StringUtil.getNotNullString(oucount));
            log.info("================调用getDepartList评价人员模块部门获取接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     *  [评价人员模块窗口获取] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getWindowList", method = RequestMethod.POST)
    public String getWindowList(@RequestBody String params) {
        log.info("================调用getWindowList评价人员模块窗口获取接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");
            String ouguid = obj.getString("ouguid");
            String windowname = obj.getString("windowname");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> windowlist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("ouguid", ouguid);
            sql.like("windowname", windowname);
            sql.setSelectFields(" rowguid,windowname,ordernum ");
            List<AuditOrgaWindow> auditorgawindowlist = auditorgawindow.getAllWindow(sql.getMap()).getResult();
            for (AuditOrgaWindow auditOrgaWindow : auditorgawindowlist) {
                JSONObject data = new JSONObject();
                data.put("windowguid", auditOrgaWindow.getRowguid());
                data.put("windowname", auditOrgaWindow.getWindowname());
                data.put("windowordernum", auditOrgaWindow.getOrdernum());
                // 查询窗口下人员
                List<AuditOrgaWindowUser> peoplelist = auditorgawindow.getUserByWindow(auditOrgaWindow.getRowguid()).getResult();
                if(peoplelist!=null&&!peoplelist.isEmpty()){
                    data.put("peoplecount", peoplelist.size());
                }
                else{
                    data.put("peoplecount", 0);
                }
                windowlist.add(data);
            }
            //根据ordernum对窗口数据进行降序排序
            Collections.sort(windowlist,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("windowordernum") - l1.getIntValue("windowordernum"));
            //截取对应的窗口list数据
            int firstint = Integer.parseInt(currentpage) * Integer.parseInt(pagesize);
            int endint = (firstint + Integer.parseInt(pagesize)) >= windowlist.size() ? windowlist.size()
                    : (firstint + Integer.parseInt(pagesize));
            List<JSONObject> rtnlist = windowlist.subList(firstint, endint);
            int windowcount = windowlist.size();
            dataJson.put("windowlist", rtnlist);
            dataJson.put("windowcount", StringUtil.getNotNullString(windowcount));
            log.info("================调用getWindowList评价人员模块窗口获取接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     *  [评价人员模块工作人员列表获取] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPeopleList", method = RequestMethod.POST)
    public String getPeopleList(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用getPeopleList评价人员模块工作人员列表获取接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String currentpage = obj.getString("currentpage");
            String pagesize = obj.getString("pagesize");
            String windowguid = obj.getString("windowguid");
            String peoplename = obj.getString("peoplename");
            List<JSONObject> peoplelist = new ArrayList<JSONObject>();
            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowguid);
            List<AuditOrgaWindowUser> windowuserlist = auditorgawindow.getUserByWindow(windowguid).getResult();
            for (AuditOrgaWindowUser windowuser : windowuserlist) {
                JSONObject data = new JSONObject();
                data.put("username", windowuser.getUsername());
                data.put("userguid", windowuser.getUserguid());
                data.put("photourl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/rest/auditattach/getUserPic?userguid=" + windowuser.getUserguid());
                // 对窗口人员进行查询
                if(windowuser.getUsername().indexOf(peoplename)!=-1){
                    peoplelist.add(data);
                }
            }
            //截取对应的窗口人员list数据
            int firstint = Integer.parseInt(currentpage) * Integer.parseInt(pagesize);
            int endint = (firstint + Integer.parseInt(pagesize)) >= peoplelist.size() ? peoplelist.size()
                    : (firstint + Integer.parseInt(pagesize));
            List<JSONObject> rtnlist = peoplelist.subList(firstint, endint);
            int peoplecount = peoplelist.size();
            dataJson.put("peoplelist", rtnlist);
            dataJson.put("peoplecount", StringUtil.getNotNullString(peoplecount));
            log.info("================调用getPeopleList评价人员模块工作人员列表获取接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [评价模块获取各种满意度评分文本数据] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getCommentSatisfiedList", method = RequestMethod.POST)
    public String getCommentSatisfiedList(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用getCommentSatisfiedList评价模块获取各种满意度评分文本数据接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String type = obj.getString("type");
            String itemid = obj.getString("itemid");
            JSONObject dataJson = new JSONObject();
            List<CodeItems> listCodeItemsByCodeName = new ArrayList<CodeItems>();
            
            CodeMain codeMainByName = new CodeMain();
            if(QueueConstant.CONSTANT_STR_ZERO.equals(type)){
                codeMainByName = codemainservice.getCodeMainByName("工作台评价人员满意度");
            }
            else if(QueueConstant.CONSTANT_STR_ONE.equals(type)){
                codeMainByName = codemainservice.getCodeMainByName("工作台评价事项满意度");
            }
            else{
                
            }
            if(StringUtil.isNotBlank(codeMainByName)){
                if(StringUtil.isBlank(itemid)){
                    listCodeItemsByCodeName = codeitemservice.listCodeItemsByLevel(codeMainByName.getCodeID().toString(), "", 0);
                }
                else{
                    listCodeItemsByCodeName = codeitemservice.listCodeItemsByLevel(codeMainByName.getCodeID().toString(), itemid ,0);
                }
            }
            // 排序
            listCodeItemsByCodeName.sort((CodeItems c1,CodeItems c2)->c2.getItemValue().compareTo(c1.getItemValue()));
            dataJson.put("satisfiedlist", listCodeItemsByCodeName);
            log.info("================调用getCommentSatisfiedList评价模块获取各种满意度评分文本数据接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [评价模块上传评价窗口人员数据] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/savePeopleComment", method = RequestMethod.POST)
    public String savePeopleComment(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用savePeopleComment评价模块上传评价窗口人员数据接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String ouguid = obj.getString("ouguid");
            String windowguid = obj.getString("windowguid");
            String windowpeoplename = obj.getString("windowpeoplename");
            String windowuserguid = obj.getString("windowuserguid");
            String satisfiedtype = obj.getString("satisfiedtype");
            String satisfiedtext = obj.getString("satisfiedtext");
            String commentcard = obj.getString("commentcard");
            String commentpeoplename = obj.getString("commentpeoplename");
            String commentcontent = obj.getString("commentcontent");
            JSONObject dataJson = new JSONObject();
            if(StringUtil.isNotBlank(commentcard)){
                List<ZCAuditZnsbCommentPeople> result = peopleservice.getAuditZnsbCommentPeopleByCard(commentcard).getResult();
                if(StringUtil.isNotBlank(result)){
                    for (ZCAuditZnsbCommentPeople auditZnsbCommentPeople : result) {
                        if(windowuserguid.equals(auditZnsbCommentPeople.getWindowuserguid())){
                            return JsonUtils.zwdtRestReturn("0",commentpeoplename+"当天已对窗口人员进行评价，请勿重复评价!","");
                        }
                    }
                }
            }
            ZCAuditZnsbCommentPeople commentPeople = new ZCAuditZnsbCommentPeople();
            commentPeople.setRowguid(UUID.randomUUID().toString());
            commentPeople.setCenterguid(centerguid);
            commentPeople.setOuguid(ouguid);
            commentPeople.setWindowguid(windowguid);
            commentPeople.setWindowpeoplename(windowpeoplename);
            commentPeople.setWindowuserguid(windowuserguid);
            commentPeople.setSatisfiedtype(satisfiedtype);
            commentPeople.setSatisfiedtext(satisfiedtext);
            commentPeople.setCommentcard(commentcard);
            commentPeople.setCommentpeoplename(commentpeoplename);
            commentPeople.setCommentcontent(commentcontent);
            commentPeople.setCommentdate(new Date());
            peopleservice.insert(commentPeople);
            log.info("================调用savePeopleComment评价模块上传评价窗口人员数据接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [评价模块上传评价事项数据] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/saveTaskComment", method = RequestMethod.POST)
    public String saveTaskComment(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用saveTaskComment评价模块上传评价事项数据接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String taskguid = obj.getString("taskguid");
            String taskname = obj.getString("taskname");
            String satisfiedtype = obj.getString("satisfiedtype");
            String satisfiedtext = obj.getString("satisfiedtext");
            String commentcard = obj.getString("commentcard");
            String commentpeoplename = obj.getString("commentpeoplename");
            String commentcontent = obj.getString("commentcontent");
            JSONObject dataJson = new JSONObject();
            if(StringUtil.isNotBlank(commentcard)){
                List<ZCAuditZnsbCommentMatter> result = matterservice.getAuditZnsbCommentMatterByCard(commentcard).getResult();
                if(StringUtil.isNotBlank(result)){
                    for (ZCAuditZnsbCommentMatter auditZnsbCommentMatter : result) {
                        if(taskguid.equals(auditZnsbCommentMatter.getTaskguid())){
                            return JsonUtils.zwdtRestReturn("0",commentpeoplename+"当天已对事项进行评价，请勿重复评价!","");
                        }
                    }
                }
            }
            ZCAuditZnsbCommentMatter commentmatter = new ZCAuditZnsbCommentMatter();
            commentmatter.setRowguid(UUID.randomUUID().toString());
            commentmatter.setCenterguid(centerguid);
            commentmatter.setTaskguid(taskguid);
            if(StringUtil.isNotBlank(taskname)&&taskname.length()>50){
                taskname = taskname.substring(0, 47)+"...";
            }
            commentmatter.setTaskname(taskname);
            commentmatter.setSatisfiedtype(satisfiedtype);
            commentmatter.setSatisfiedtext(satisfiedtext);
            commentmatter.setCommentcard(commentcard);
            commentmatter.setCommentpeoplename(commentpeoplename);
            commentmatter.setCommentcontent(commentcontent);
            commentmatter.setCommentdate(new Date());
            matterservice.insert(commentmatter);
            log.info("================调用saveTaskComment评价模块上传评价事项数据接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [获取所有中心数据，前提是该中心下有工作台设备] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getAreaList", method = RequestMethod.POST)
    public String getAreaList(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用getAreaList获取所有中心数据接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject dataJson = new JSONObject();
            JSONObject obj = (JSONObject) json.get("params");
            // 当前页
            String currentpage = obj.getString("currentpage");
            // 每页展示条数
            String pagesize = obj.getString("pagesize");
            String centername = obj.getString("centername");
            // 先查询工作台设备列表中不重复的centerguid
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ24);
            // 是否启用
            sql.eq("status", QueueConstant.Common_yes_String);
            // 中心不为空，不获取相同的centerguid
            sql.isNotBlank("centerguid");
            sql.setSelectFields("distinct(centerguid)");
            List<AuditZnsbEquipment> equipmentlist = equipmentservice.getEquipmentList(sql.getMap()).getResult();
            StringBuilder sb = new StringBuilder();
            String centerguids = "";
            for (AuditZnsbEquipment equipment : equipmentlist) {
                sb.append(equipment.getCenterguid()).append("','");
            }
            centerguids = "'" + sb.toString() + "'";
            sql.clear();
            sql.in("rowguid", centerguids);
            sql.setOrderDesc("ordernum");
            sql.like("centername", centername);
            sql.setSelectFields("rowguid,centername,address,phonenum");
            List<AuditOrgaServiceCenter> centerlist = servicecenter
                    .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
            for (AuditOrgaServiceCenter center : centerlist) {
                sql.clear();
                // 获取工作台设备中第一个来跳转
                sql.eq("centerguid", center.getRowguid());
                sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ24);
                sql.eq("status", QueueConstant.Common_yes_String);
                List<AuditZnsbEquipment> equipmentlistfinal = equipmentservice.getEquipmentList(sql.getMap())
                        .getResult();
                center.put("macaddress", equipmentlistfinal.get(0).getMacaddress());
            }

            List<AuditOrgaServiceCenter> centerpagedata = Page(centerlist, Integer.parseInt(pagesize),
                    Integer.parseInt(currentpage));
            dataJson.put("arealist", centerpagedata);
            dataJson.put("totalcount", centerlist.size());
            log.info("================调用getAreaList获取所有中心数据接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [获取办件信息] 
     *  @param params
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params, HttpServletRequest request) {
        log.info("================调用getProjectDetail获取所有中心数据接口开始=======================");
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyername,acceptuserdate,ouname,banjiedate,status,certnum,flowsn";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            dataJson.put("acceptuserdate",EpointDateUtil.convertDate2String(project.getAcceptuserdate(), "yyyy-MM-dd HH:mm:ss"));
            dataJson.put("banjiedate",EpointDateUtil.convertDate2String(project.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            dataJson.put("statustext",
                    codeitemservice.getItemTextByCodeName("办件状态", String.valueOf(project.getStatus())));
            dataJson.put("project", project);
            log.info("================调用getProjectDetail获取所有中心数据接口结束=======================");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  [根据办件流水号评价办件【横屏】]
     *  [F9.3 根据办件流水号评价办件【横屏】]
     *  @param params
     *  @return    
     * 
     * 
     */
    @RequestMapping(value = "/evalProjectByFlowsn", method = RequestMethod.POST)
    public String evalProjectByFlowsn(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));//token验证
            JSONObject obj = (JSONObject) json.get("params");
            String flowsn = obj.getString("flowsn");//办件号
            String Project_MYD = obj.getString("projectmyd");
            String areacode = obj.getString("areacode");
            String username = obj.getString("username");
            String userguid = obj.getString("userguid");
            JSONObject jsondata = new JSONObject();
            if (StringUtil.isNotBlank(flowsn)) {
                String fields = " rowguid ";
                AuditProject auditproject = projectservice.getAuditProjectByFlowsn(fields, flowsn, areacode)
                        .getResult();
                if (auditproject != null) {
                    if (!evaluateservice.isExistEvaluate(auditproject.getRowguid()).getResult()) {
                        AuditOnlineEvaluat evaluat = new AuditOnlineEvaluat();
                        evaluat.setRowguid(UUID.randomUUID().toString());
                        evaluat.setEvaluatetype("50");
                        evaluat.setClientidentifier(auditproject.getRowguid());
                        evaluat.setClienttype("10");
                        evaluat.setEvaluatedate(new Date());
                        evaluat.setSatisfied(Project_MYD);
                        evaluat.setEvaluateusername(username);
                        evaluat.setEvaluateuserguid(userguid);
                        evaluateservice.addAuditOnineEvaluat(evaluat);
                        jsondata.put("status", "1");//返回评价成功
                    }
                    else {
                        jsondata.put("status", "0");//返回已经评价过
                        jsondata.put("msg", "办件已评价，无法再次评价");//返回评价成功
                    }
                }
            }
            else{
                jsondata.put("status", "0");//无流水号
                jsondata.put("msg", "办件尚未被接件，无法评价");//无法评价
            }
            return JsonUtils.zwdtRestReturn("1", "", jsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    public static List<AuditOrgaServiceCenter> Page(List<AuditOrgaServiceCenter> dataList, int pageSize,
            int currentPage) {
        List<AuditOrgaServiceCenter> currentPageList = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            int currIdx = (currentPage >= 1 ? currentPage * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                AuditOrgaServiceCenter data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }
}
