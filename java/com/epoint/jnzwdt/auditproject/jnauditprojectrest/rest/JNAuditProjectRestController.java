package com.epoint.jnzwdt.auditproject.jnauditprojectrest.rest;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.jnzwdt.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;

@RestController
@RequestMapping("/jnauditprojectrest")
public class JNAuditProjectRestController
{
    @Autowired
    private IJNAuditProjectRestService service;
    
    /**
     * 
     *  获取办件表的列表清单
     *  @param params
     *  @param request
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditProjectList", method = RequestMethod.POST)
    public String getJNAuditProjectList(@RequestBody String params, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(params);
        JSONObject dataJson = new JSONObject();
        try {
            String areaCode = json.getString("areaCode");
            int pageNumber = Integer.parseInt(json.getString("pageNumber"));
            int pageSize = Integer.parseInt(json.getString("pageSize"));
            if (pageSize == 0) {
            	pageSize = 10;
            }
            List<AuditProject> list = service.getAuditProjectRestList(pageNumber*pageSize, pageSize, areaCode);
            dataJson.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  获取办件的详细信息
     *  @param params
     *  @param request
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditProjectDetail", method = RequestMethod.POST)
    public String getJNAuditProjectDetail(@RequestBody String params, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(params);
        JSONObject dataJson = new JSONObject();
        try {
            String rowGuid = json.getString("rowGuid");
            AuditProject auditProject = service.getAuditProjectRestDetail(rowGuid);
            dataJson.put("auditProject", auditProject);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计年月日办件次数的数量
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditCount", method = RequestMethod.POST)
    public String getJNAuditCount(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        JSONObject dataJson = new JSONObject();
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getAuditCount(areaCode);
            String everyYearCount = auditProject.getStr("everyYearCount");
            String everyMonthCount = auditProject.getStr("everyMonthCount");
            String everyDayCount = auditProject.getStr("everyDayCount");
            map.put("everyYearCount", everyYearCount);
            map.put("everyMonthCount", everyMonthCount);
            map.put("everyDayCount", everyDayCount);
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价数量
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditSatisfiedCount", method = RequestMethod.POST)
    public String getJNAuditSatisfiedCount(@RequestBody String params, HttpServletRequest request) {
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        JSONObject dataJson = new JSONObject();
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getSatisfiedCount(areaCode);
            String basicSatisfiedCount = auditProject.getStr("basicSatisfiedCount");
            String totalAudit = service.getTotalSatisfiedCount(areaCode).getStr("totalAudit");
            String disSatisfiedCount = auditProject.getStr("disSatisfiedCount");
            int verySatisfiedCount = Integer.parseInt(totalAudit)-Integer.parseInt(basicSatisfiedCount)-Integer.parseInt(disSatisfiedCount);
            map.put("非常满意", String.valueOf(verySatisfiedCount));
            map.put("基本满意", basicSatisfiedCount);
            map.put("不满意", disSatisfiedCount);
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    
    /**
     * 
     *  统计办件满意度评价百分比
     *  @return    
     */
    @RequestMapping(value = "/getJNAuditSatisfiedPercent", method = RequestMethod.POST)
    public String getJNAuditSatisfiedPercent(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        JSONObject result = JSON.parseObject(params);
        String areaCode = result.getString("areaCode");
        Map<String,String> map = new HashMap<String,String>();
        try {
            AuditProject auditProject = service.getSatisfiedCount(areaCode);
            int basicSatisfiedCount = Integer.parseInt(auditProject.getStr("basicSatisfiedCount"));
            int totalAudit = Integer.parseInt(service.getTotalSatisfiedCount(areaCode).getStr("totalAudit"));
            int disSatisfiedCount = Integer.parseInt(auditProject.getStr("disSatisfiedCount"));
            int verySatisfiedCount = totalAudit - basicSatisfiedCount - disSatisfiedCount;
            DecimalFormat df = new DecimalFormat("0.0");
            String verySatisfiedPercent = df.format((float)verySatisfiedCount*100/totalAudit);
            String basicSatisfiedPercent = df.format((float)basicSatisfiedCount*100/totalAudit);
            String disSatisfiedPercent = df.format((float)disSatisfiedCount*100/totalAudit);
            map.put("verySatisfiedCount", verySatisfiedPercent+"%");
            map.put("basicSatisfiedCount", basicSatisfiedPercent+"%");
            map.put("disSatisfiedCount", disSatisfiedPercent+"%");
            dataJson.put("data", map);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
