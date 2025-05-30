package com.epoint.wswshow.rest;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwdt.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;
import com.epoint.newshow2.api.Newshow2Service;

@RestController
@RequestMapping("/wsshow")
public class WsShowRestController
{
    @Autowired
    private Newshow2Service newshow2Service;
    
    @Autowired
    private IJNAuditProjectRestService iJNAuditProjectRestService;

    /***
     * 
     *  [汶上为民服务中心]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/servecenter", method = RequestMethod.POST)
    public String servecenter(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
           
            dataJson.put("processing", 223);
            dataJson.put("today", 6914);
            dataJson.put("total", 604632);
            dataJson.put("waiting", 6026);

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [办件满意度]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/satisfaction", method = RequestMethod.POST)
    public String satisfaction(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject dataJson1 = new JSONObject();
        JSONObject result = JSON.parseObject(params);
        List<String> record = new ArrayList<String>();
        String areaCode = result.getString("areaCode");
        try {
            AuditProject auditProject = iJNAuditProjectRestService.getSatisfiedCount(areaCode);
            int basicSatisfiedCount = Integer.parseInt(auditProject.getStr("basicSatisfiedCount"));
            int totalAudit = Integer.parseInt(iJNAuditProjectRestService.getTotalSatisfiedCount(areaCode).getStr("totalAudit"));
            int disSatisfiedCount = Integer.parseInt(auditProject.getStr("disSatisfiedCount"));
            int verySatisfiedCount = totalAudit - basicSatisfiedCount - disSatisfiedCount;
            DecimalFormat df = new DecimalFormat("0.0");
            String manyidu = df.format(Math.floor((float)verySatisfiedCount*1000/totalAudit)/10) + "%";
            String jbmanyidu = df.format((float)basicSatisfiedCount*100/totalAudit) + "%";
            String bumanyidu = df.format(Math.ceil((float)disSatisfiedCount*1000/totalAudit)/10) + "%";
            String totalmanyiPercent = df.format(100-Math.ceil((float)disSatisfiedCount*1000/totalAudit)/10);
            record.add(manyidu);
            record.add(jbmanyidu);
            record.add(bumanyidu);
            dataJson1.put("detail", record);
            dataJson1.put("city", totalmanyiPercent);
            dataJson.put("satisfy", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /***
     * 
     *  [每日考勤]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/attendance", method = RequestMethod.POST)
    public String attendance(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            JSONObject dataJson = new JSONObject();
            List<Record> list = newshow2Service.geteventType(areaCode);
            for (Record record : list) {
                String oushortname = newshow2Service.findOushortname(record.get("ouguid"));
                record.set("name", oushortname);
            }
            dataJson.put("data", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [中间部分]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/centerlist", method = RequestMethod.POST)
    public String centerlist(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            JSONObject dataJson1 = new JSONObject();
            List<Record> list = newshow2Service.getmapData();
            dataJson1.put("scatter", list);
            List<Record> list1 = newshow2Service.getmapbanjian();
            dataJson1.put("map", list1);
            dataJson.put("mapData", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [性别比例]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/sexratio", method = RequestMethod.POST)
    public String sexratio(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            JSONObject dataJson = new JSONObject();
            Record record = newshow2Service.gethandleEvent(areaCode);
            long nowdate=System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            String year = sdf.format(date);
            String firstdate = year + "-01-01 00-00-00";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            long time = simpleDateFormat.parse(firstdate).getTime();
            long days = (nowdate - time)/(1000 * 60 * 60 * 24L);
            record.set("day", record.getInt("year") / days);
            dataJson.put("handleEvent", record);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [年龄分析]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/ageanalysis", method = RequestMethod.POST)
    public String ageanalysis(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            Record record = newshow2Service.getcityDatabyid();
            String day = newshow2Service.getDayQueue();
            record.set("day", day);
            dataJson.put("cityData", record);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [服务渠道]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/servicechannel", method = RequestMethod.POST)
    public String servicechannel(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            List<String> source = new ArrayList<String>();
            JSONObject dataJson = new JSONObject();
            JSONObject sexObject = new JSONObject();
            JSONArray ageArray = new JSONArray();
            JSONObject dataJson1 = new JSONObject();
            sexObject.put("male", "58.3");
            sexObject.put("female", "41.7");
            dataJson1.put("sex", sexObject);
            for (int i = 0; i < 6; i++) {
                JSONObject ageObject = new JSONObject();
                if (i == 0) {
                    ageObject.put("name", "15-30岁");
                    ageObject.put("value", "34");
                }
                else if (i == 1) {
                    ageObject.put("name", "31-40岁");
                    ageObject.put("value", "26");
                }
                else if (i == 2) {
                    ageObject.put("name", "41-50岁");
                    ageObject.put("value", "20");
                }
                else if (i == 3) {
                    ageObject.put("name", "51-60岁");
                    ageObject.put("value", "16");
                }
                else if (i == 4) {
                    ageObject.put("name", "61-70岁");
                    ageObject.put("value", "3");
                }
                else if (i == 5) {
                    ageObject.put("name", "70岁以上");
                    ageObject.put("value", "1");
                }
                ageArray.add(ageObject);
            }
            Record record = newshow2Service.getsource(areaCode);
            int wx = record.getInt("wx");
            int ck = record.getInt("chuangkou");
            int ww = record.getInt("waiwang");
            int zz = record.getInt("zz");
            int total = wx + ck + ww + zz;
            DecimalFormat df = new DecimalFormat("0.0");
            float wxnum = (float) wx * 100 / total;
            float cknum = (float) ck * 100 / total;
            float wwnum = (float) ww * 100 / total;
            float zznum = (float) zz * 100 / total;
            String wxnumstr = df.format(wxnum) + "%";
            String cknumstr = df.format(cknum) + "%";
            String wwnumstr = df.format(wwnum) + "%";
            String zznumstr = df.format(zznum) + "%";
            source.add(wxnumstr);
            source.add(cknumstr);
            source.add(wwnumstr);
            source.add(zznumstr);
            dataJson1.put("age", ageArray);
            dataJson1.put("source", source);
            dataJson.put("serviceObj", dataJson1);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     *  [地图]
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/map", method = RequestMethod.POST)
    public String map(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject result = JSON.parseObject(params);
            String areaCode = result.getString("areaCode");
            JSONObject dataJson = new JSONObject();
            List<Record> list = newshow2Service.geteventTop5(areaCode);
            dataJson.put("eventTop5", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
