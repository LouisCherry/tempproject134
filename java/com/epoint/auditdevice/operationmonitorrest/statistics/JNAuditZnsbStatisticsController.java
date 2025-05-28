package com.epoint.auditdevice.operationmonitorrest.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbabnormalshutdown.domain.AuditZnsbAbnormalShutdown;
import com.epoint.basic.auditqueue.auditznsbabnormalshutdown.inter.IAuditZnsbAbnormalShutdown;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbmodule.domain.FunctionModule;
import com.epoint.basic.auditqueue.auditznsbmodule.inter.IAuditZnsbModule;
import com.epoint.basic.auditqueue.auditznsbselfmachineproject.inter.IAuditZnsbSelfmachineproject;
import com.epoint.basic.auditqueue.printcount.inter.IAuditZnsbPrintCountService;
import com.epoint.basic.auditqueue.selflogin.inter.IAuditZnsbSelfloginService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.PinyinUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/jnmonitorstatistics")
public class JNAuditZnsbStatisticsController
{
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditZnsbPrintCountService printcountservice;
    @Autowired
    private IAuditZnsbSelfloginService selfloginservice;
    @Autowired
    private IAuditZnsbSelfmachineproject IAuditZnsbSelfmachineproject;
    @Autowired
    private IAuditZnsbAbnormalShutdown auditZnsbAbnormalShutdownservice;

    @Autowired
    private IAuditZnsbModule auditznsbmoduleservice;
    @Autowired
    private IAuditOrgaServiceCenter auditOrgaServiceCenter;
    @Autowired
    private IAuditQueueUserinfo userinfoservice;
    
    @Autowired
    private IConfigService configservice;

    /**
     * 获取中心信息
     * 
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getCenterList", method = RequestMethod.POST)
    public String getCenterList(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String projectmanageno = obj.getString("projectmanageno");
            String nowareacode = obj.getString("nowareacode");
            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setOrder("ordernum", "asc");
            if (StringUtil.isNotBlank(projectmanageno)) {
                sql.eq("address", projectmanageno);
            }
            if(StringUtil.isBlank(nowareacode)){
                sql.eq("CHAR_LENGTH(BELONGXIAQU)", "6");
            }else{
                sql.eq("CHAR_LENGTH(BELONGXIAQU)", nowareacode.length()+3+"");
                sql.leftLike("BELONGXIAQU", nowareacode);
            }
            
            List<AuditOrgaServiceCenter> listServiceCenter = auditOrgaServiceCenter
                    .getAuditOrgaServiceCenterByCondition(sql.getMap()).getResult();
            List<JSONObject> centerlist = new ArrayList<JSONObject>();
            for (AuditOrgaServiceCenter center : listServiceCenter) {
                JSONObject data = new JSONObject();
                data.put("centerguid", center.getRowguid());
                data.put("centername", center.getCentername());
                data.put("areacode", center.getBelongxiaqu());
                data.put("picurl",
                        QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/znsb/selfservicemachine/images/centerimg/"
                                + PinyinUtil.getFirstPinYinOfHanzi(center.getCentername()) + ".jpg");
                
                centerlist.add(data);
            }
            dataJson.put("centerlist", centerlist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     *  [根据中心guid获取中心名称]
     * 
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getCenterNameByGuid", method = RequestMethod.POST)
    public String getCenterNameByGuid(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            if(StringUtil.isBlank(obj.getString("centerguid"))) {
                dataJson.put("centername", "所有中心");
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            AuditOrgaServiceCenter center = auditOrgaServiceCenter.findAuditServiceCenterByGuid(obj.getString("centerguid")).getResult();
            dataJson.put("centername", center.getCentername());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /**
     * 使用统计
     */
    @RequestMapping(value = "getStatisticsList", method = RequestMethod.POST)
    public String getStatisticsList(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String centerguid = obj.getString("centerguid");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String sortOrder = obj.getString("sortorder");
            List<JSONObject> statisticslist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            else {
                sql.isNotBlank("centerguid");
            }
            if (StringUtil.isBlank(sortOrder)) {
                sortOrder = "machineno";
            }
            sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ);
            sql.setSelectFields("machinename,machineno,Centername,macaddress");
            PageData<AuditZnsbEquipment> auditZnsbEquipmentlist = equipmentservice
                    .getEquipmentByPage(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), sortOrder, "asc")
                    .getResult();
            int totalcount = auditZnsbEquipmentlist.getRowCount();
            int allprintcount=printcountservice.getRecordCountByCenterguidAndTime(centerguid, startime, endtime).getResult();
            int alllogincount=selfloginservice.getRecordCountBycenterguidAndTime(centerguid, startime, endtime).getResult();
            int allapplycount=IAuditZnsbSelfmachineproject.getRecordCountByCenterguidAndTime(centerguid, startime, endtime).getResult();
            int allclickcount=auditznsbmoduleservice.getRecordCountByCenterguidAndTime(centerguid, startime, endtime).getResult();
            
            for (AuditZnsbEquipment equipment : auditZnsbEquipmentlist.getList()) {
                if (StringUtil.isNotBlank(equipment)) {
                    JSONObject data = new JSONObject();
                    data.put("machineno", equipment.getMachineno());//机器编号
                    data.put("machinename", equipment.getMachinename());//机器名称
                    int printcount= printcountservice.getRecordCount( equipment.getMacaddress(), startime, endtime).getResult();
                    int logincount= selfloginservice.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();
                    int applycount=IAuditZnsbSelfmachineproject.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();
                    int clickcount=auditznsbmoduleservice.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();                   
                    data.put("printcount", printcount);//打印次数
                    data.put("logincount", logincount);//登录次数
                    data.put("applycount", applycount);//申报次数
                    data.put("clickcount", clickcount);//模块点击次数
                    statisticslist.add(data);
                }
            }
            dataJson.put("allprintcount", allprintcount);
            dataJson.put("alllogincount", alllogincount);
            dataJson.put("allapplycount", allapplycount);
            dataJson.put("allclickcount", allclickcount);
            dataJson.put("statisticsList", statisticslist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * [使用统计，无分页，用于导出]
     */
    @RequestMapping(value = "getStatisticsListNoPage", method = RequestMethod.POST)
    public String getStatisticsListNoPage(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String sortOrder = obj.getString("sortorder");
            List<JSONObject> statisticslist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            else {
                sql.isNotBlank("centerguid");
            }
            if (StringUtil.isBlank(sortOrder)) {
                sql.setOrderAsc("machineno");
            }
            sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ);
            sql.setSelectFields("machinename,machineno,Centername,macaddress");
            List<AuditZnsbEquipment> result = new ArrayList<AuditZnsbEquipment>();
            PageData<AuditZnsbEquipment> auditZnsbEquipmentlist = equipmentservice
                    .getEquipmentByPage(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), sortOrder, "asc")
                    .getResult();
            int totalcount = auditZnsbEquipmentlist.getRowCount();
            int totalpage = (int) Math.ceil(totalcount/Integer.parseInt(pageSize));
            for(int i = 0;i<=totalpage;i++) {
                auditZnsbEquipmentlist = equipmentservice
                        .getEquipmentByPage(sql.getMap(), i * Integer.parseInt(pageSize),
                                Integer.parseInt(pageSize), sortOrder, "asc")
                        .getResult();
                result.addAll(auditZnsbEquipmentlist.getList());
            }
            for (AuditZnsbEquipment equipment : result) {
                if (StringUtil.isNotBlank(equipment)) {
                    JSONObject data = new JSONObject();
                    data.put("machineno", equipment.getMachineno());//机器编号
                    data.put("machinename", equipment.getMachinename());//机器名称
                    int printcount= printcountservice.getRecordCount( equipment.getMacaddress(), startime, endtime).getResult();
                    int logincount= selfloginservice.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();
                    int applycount=IAuditZnsbSelfmachineproject.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();
                    int clickcount=auditznsbmoduleservice.getRecordCount(equipment.getMacaddress(), startime, endtime).getResult();                   
                    data.put("printcount", printcount);//打印次数
                    data.put("logincount", logincount);//登录次数
                    data.put("applycount", applycount);//申报次数
                    data.put("clickcount", clickcount);//模块点击次数
                    statisticslist.add(data);
                }
            }
            dataJson.put("statisticsList", statisticslist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 分页获取异常关机记录
     */
    @RequestMapping(value = "getAbnormalShutdownList", method = RequestMethod.POST)
    public String getAbnormalShutdownList(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String centerguid = obj.getString("centerguid");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            List<JSONObject> abnormalshutdownlist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
         // 根据系统参数AS_ZNSB_MONITORMACHINETYPE 筛选设备类型 默认为0 全部设备;1 只监控一体机
            String monitormachinetype = configservice.getFrameConfigValue("AS_ZNSB_MONITORMACHINETYPE");
            if (StringUtil.isNotBlank(monitormachinetype)&&QueueConstant.CONSTANT_STR_ONE.equals(monitormachinetype)) {
                sql.eq("machinetype", QueueConstant.EQUIPMENT_TYPE_YTJ);
            }
            
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            else {
                sql.nq("centerguid", "");
            }
            sql.setSelectFields(
                    "macaddress,centerguid,machinename,machineno,centername,operatoruser,operatorusermobile");
            PageData<AuditZnsbEquipment> auditZnsbEquipmentlist = equipmentservice
                    .getEquipmentByPage(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "machineno", "asc")
                    .getResult();
            int totalcount = auditZnsbEquipmentlist.getRowCount();
            for (AuditZnsbEquipment equipment : auditZnsbEquipmentlist.getList()) {
                if (StringUtil.isNotBlank(equipment)) {
                    JSONObject data = new JSONObject();
                    data.put("macaddress", equipment.getMacaddress());
                    data.put("machinename", equipment.getMachinename());
                    data.put("machineno", equipment.getMachineno());
                    data.put("machineaddress", equipment.getCentername());
                    data.put("username", equipment.getOperatoruser());
                    data.put("mobile", equipment.getOperatorusermobile());
                    data.put("shutdowncount", auditZnsbAbnormalShutdownservice
                            .getRecordCount(centerguid, equipment.getMacaddress(), startime, endtime).getResult());
                    abnormalshutdownlist.add(data);
                }
            }
            dataJson.put("abnormalshutdownlist", abnormalshutdownlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取关机详情
     */
    @RequestMapping(value = "getAbnormalShutdownDetail", method = RequestMethod.POST)
    public String getAbnormalShutdownDetail(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject data = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            List<JSONObject> shutdowntimelist = new ArrayList<JSONObject>();
            String macaddress = obj.getString("macaddress");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            if (StringUtil.isNotBlank(macaddress)) {
                AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
                if (StringUtil.isNotBlank(equipment)) {
                    data.put("machinename", equipment.getMachinename());
                    data.put("machineno", equipment.getMachineno());
                    data.put("machineaddress", equipment.getCentername());
                    data.put("username", equipment.getOperatoruser());
                    data.put("mobile", equipment.getOperatorusermobile());
                }
                else {
                    data.put("machinename", "");
                    data.put("machineno", "");
                    data.put("machineaddress", "");
                    data.put("username", "");
                    data.put("mobile", "");
                }
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("macaddress", macaddress);  
                sql.gt("shutdowntime",EpointDateUtil.convertString2Date(startime,"yyyy-MM-dd"));
                sql.lt("shutdowntime", EpointDateUtil.convertString2Date(endtime,"yyyy-MM-dd"));
                sql.setSelectFields("shutdowntime");
                sql.setOrder("shutdowntime", "desc");
                List<AuditZnsbAbnormalShutdown> abnormalShutdownlist = auditZnsbAbnormalShutdownservice
                        .getAbnormalShutdownList(sql.getMap()).getResult();
                if (abnormalShutdownlist.size() < 1) {
                    data.put("shutdowntimelist", "");
                    data.put("shutdowncount", "");
                }
                else {
                    for (AuditZnsbAbnormalShutdown shutdown : abnormalShutdownlist) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("shutdowntime", shutdown.getShutDownTime());
                        shutdowntimelist.add(dataJson);
                    }
                    data.put("shutdowntimelist", shutdowntimelist);
                    data.put("shutdowncount", shutdowntimelist.size());
                }

            }
            return JsonUtils.zwdtRestReturn("1", "", data);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取模块点击数量top10
     */
    @RequestMapping(value = "getModuleClickList", method = RequestMethod.POST)
    public String getModuleClickList(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject data = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            List<Record> list = null;
            if (StringUtil.isNotBlank(centerguid)) {
                list = auditznsbmoduleservice.getModuleClick(centerguid, startime ,endtime).getResult();
            }
            else {
                list = auditznsbmoduleservice.getModuleClick(startime ,endtime).getResult();
            }

            data.put("moduleclicklist", list);
            return JsonUtils.zwdtRestReturn("1", "", data);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 获取两个时间中所有月份的前10模块点击量
     */
    @RequestMapping(value = " getModuleClickListForEchart", method = RequestMethod.POST)
    public String  getModuleClickListForEchart(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String startime = obj.getString("startime");
            String endtime = obj.getString("endtime");
            List<JSONObject> resultJson = new ArrayList<JSONObject>();
            // 获取前十模块名称
            List<Record> list = null;
            List<String> topmodulename = new ArrayList<String>();
            if (StringUtil.isNotBlank(centerguid)) {
                list = auditznsbmoduleservice.getModuleClick(centerguid, startime ,endtime).getResult();
            }
            else {
                list = auditznsbmoduleservice.getModuleClick(startime ,endtime).getResult();
            }
            for (int i=0;i<list.size();i++) {
                topmodulename.add(list.get(i).getStr("modulename"));
                JSONObject module = new JSONObject();
                module.put("name", list.get(i).get("modulename"));
                module.put("type", "line");
                module.put("stack", list.get(i).get("modulename"));
                module.put("data", new JSONArray());
                resultJson.add(module);
            }
            // 获取所有月份
            List<String> monthBetween = getMonthBetween(startime,endtime);
            List<Record> detaillist = null;
            for (int i=0;i<monthBetween.size();i++) {
                String eachstarttime = monthBetween.get(i);
                String eachendtime;
                if(i+1==monthBetween.size()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    Date date = sdf.parse(eachstarttime);
                    Calendar c = Calendar.getInstance();  
                    c.setTime(date);  
                    c.add(Calendar.MONTH, 1);  
                    eachendtime = sdf.format(c.getTime());
                }else {
                    eachendtime = monthBetween.get(i+1);
                }
                if (StringUtil.isNotBlank(centerguid)) {
                    detaillist = auditznsbmoduleservice.getModuleClick(centerguid, eachstarttime ,eachendtime).getResult();
                }
                else {
                    detaillist = auditznsbmoduleservice.getModuleClick(eachstarttime ,eachendtime).getResult();
                }
                for (Record record : detaillist) {
                    for(int j=0;j<resultJson.size();j++) {
                        JSONObject eachobj = resultJson.get(j);
                        if(eachobj.containsValue(record.get("modulename"))) {
                            eachobj.getJSONArray("data").add(record.get("clicknum"));
                            break;
                        }
                    }
                }
                for(int j=0;j<resultJson.size();j++) {
                    JSONObject eachobj = resultJson.get(j);
                    if(eachobj.getJSONArray("data").size()<i+1) {
                        eachobj.getJSONArray("data").add(0);
                    }
                }
            }
            JSONObject resultJsondata = new JSONObject();
            resultJsondata.put("clicknumlist", resultJson);
            resultJsondata.put("moduleclicknamelist", topmodulename);
            resultJsondata.put("monthBetween", monthBetween);
            return JsonUtils.zwdtRestReturn("1", "", resultJsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
   
    /**
     * 
     *  [获取两个年月时间中所有月份的方法] 
     *  @param minDate
     *  @param maxDate
     *  @return    
     * 
     * 
     */
    public List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        min = null;
        max = null;
        curr = null;
        return result;
    }
    
    /**
     *  [获取指定模块，指定月份的每天的模块点击量]
     */
    @RequestMapping(value = " getModuleDayClickEchart", method = RequestMethod.POST)
    public String  getModuleDayClickEchart(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String truename = obj.getString("truename");
            // 点击的年月2019-11，需要进行分割
            String clickmonth = obj.getString("clickmonth");
            String[] datestr = clickmonth.split("-");
            // 获取所有天
            Map<String, List<String>> daymap = getDayByMonth(Integer.parseInt(datestr[0]), Integer.parseInt(datestr[1]));
            List<String> dayByMonth = daymap.get("list");
            List<Integer> clicknumlist = new ArrayList<Integer>();
            for(int i=0; i<dayByMonth.size(); i++) {
                String eachstarttime = dayByMonth.get(i);
                String eachendtime;
                if(i+1==dayByMonth.size()) {
                    // 2019-11-30
                    Date date = EpointDateUtil.convertString2Date(dayByMonth.get(i),"yyyy-MM-dd");
                    eachendtime = EpointDateUtil.convertDate2String(EpointDateUtil.addDay(date, 1), "yyyy-MM-dd");
                }else {
                    eachendtime = dayByMonth.get(i+1);
                }
                Integer result = auditznsbmoduleservice.getModuleDayClick(centerguid, truename, eachstarttime, eachendtime).getResult();
                clicknumlist.add(result);
            }
            JSONObject resultJsondata = new JSONObject();
            resultJsondata.put("dayclicknumlist", clicknumlist);
            resultJsondata.put("daylist", daymap.get("shortday"));
            return JsonUtils.zwdtRestReturn("1", "", resultJsondata);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    public Map<String,List<String>> getDayByMonth(int yearParam,int monthParam){
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        List<String> shortday = new ArrayList<String>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.set(yearParam,monthParam-1 ,1);
        int year = aCalendar.get(Calendar.YEAR);
        int month = aCalendar.get(Calendar.MONTH)+ 1;
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate=null;
            if(month<10&&i<10){
                aDate = year+"-0"+month+"-0"+i;
            }
            if(month<10&&i>=10){
                aDate = year+"-0"+month+"-"+i;
            }
            if(month>=10&&i<10){
                aDate = year+"-"+month+"-0"+i;
            }
            if(month>=10&&i>=10){
                aDate = year+"-"+month+"-"+i;
            }
            shortday.add(i+"");
            list.add(aDate);
        }
        map.put("list", list);
        map.put("shortday", shortday);
        return map;
    }

    

    /**
     * 获取模块点击详情
     */
    @RequestMapping(value = "getModuleClickDetailList", method = RequestMethod.POST)
    public String getModuleClickDetailList(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String modulename = obj.getString("modulename");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            sql.eq("modulename", modulename);
            String fieldstr = "macaddress,modulename,onclicktime,cardid";
            List<JSONObject> detaillist = new ArrayList<JSONObject>();
            PageData<FunctionModule> list = auditznsbmoduleservice.getAuditQueueModule(fieldstr, sql.getMap(),
                    Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                    "onclicktime", "desc").getResult();
            int totalcount = list.getRowCount();
            for (FunctionModule functionModule : list.getList()) {
                if (StringUtil.isNotBlank(functionModule)) {
                    JSONObject data = new JSONObject();
                    data.put("macaddress", functionModule.getMacaddress());
                    AuditZnsbEquipment equipment = equipmentservice
                            .getDetailbyMacaddress(functionModule.getMacaddress()).getResult();
                    if (StringUtil.isNotBlank(equipment)) {
                        data.put("machinename", equipment.getMachinename());
                        data.put("machineno", equipment.getMachineno());
                        AuditOrgaServiceCenter center=auditOrgaServiceCenter.findAuditServiceCenterByGuid(equipment.getCenterguid()).getResult();
                        if (StringUtil.isNotBlank(center)) {
                            data.put("machineaddress", center.getCentername()+("所有大厅".equals(equipment.getHallname())?"":("("+equipment.getHallname()+")")));
                        }else{
                            data.put("machineaddress", "");
                        }
//                        data.put("machineaddress", equipment.getCentername());
                        data.put("operatorusername", equipment.getOperatoruser());
                        data.put("operatormobile", equipment.getOperatorusermobile());
                    }
                    else {
                        data.put("machinename", "");
                        data.put("machineno", "");
                        data.put("machineaddress", "");
                        data.put("operatorusername", "");
                        data.put("operatormobile", "");
                    }
                    data.put("modulename", functionModule.getModulename());
                    data.put("onclicktime", functionModule.getOnclicktime());
                    data.put("cardid", functionModule.getCardid());
                    AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(functionModule.getCardid()).getResult();
                    if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                        data.put("username", auditQueueUserinfo.getDisplayname());
                        data.put("mobile", auditQueueUserinfo.getMobile());
                    }
                    else {
                        data.put("username", "");
                        data.put("mobile", "");
                    }

                    detaillist.add(data);
                }
            }
            dataJson.put("moduleclickdetaillist", detaillist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (

        JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    

}
