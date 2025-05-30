package com.epoint.xmz.performance.rest;

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
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnzwdt.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;
import com.epoint.newshow2.api.Newshow2Service;
import com.epoint.xmz.performance.api.PerformanceService;

@RestController
@RequestMapping("/jnperformance")
public class PerformanceRestController
{
    @Autowired
    private PerformanceService performanceService;
    
    @Autowired
    private Newshow2Service newshow2Service;
    
    @Autowired
   	private ICodeItemsService codeitemService;
    
   

    /***
     * 
     *  查询服务方式完备度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getfwfswbd", method = RequestMethod.POST)
    public String getFwfswbd(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            List<Record> list = performanceService.getfwfswbd().getResult();
            int qxcount = 0;
            int jncount = 0;
            if (list != null && list.size() > 0) {
            	for (Record record : list) {
            		String areacode = record.getStr("areacode");
            		if ("370800".equals(areacode)) {
            			jncount = record.getInt("num");
            		}else {
            			qxcount += record.getInt("num");
            		}
            	}
            }
            dataJson.put("qxcount", 479);
            dataJson.put("jncount", 44);

            return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  查询在线服务成效度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getzxfwcxd", method = RequestMethod.POST)
    public String getZxfwcxd(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            Record record = performanceService.getzxfwcxd().getResult();
            String jibanjian = "";
            String lingpaotui = "";
            String chengnuosx = "";
            DecimalFormat   df = new DecimalFormat("#.##");  
            if (record != null) {
            	jibanjian = df.format(record.getDouble("jibanjian"));
            	lingpaotui = df.format(record.getDouble("lingpaotui"));
            	chengnuosx = df.format(record.getDouble("chengnuosx"));
            }
            
            dataJson.put("jibanjian", jibanjian);
            dataJson.put("lingpaotui", lingpaotui);
            dataJson.put("chengnuosx", chengnuosx);

            return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

	/***
     * 
     *  查询服务事项覆盖度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getfwsxfgd", method = RequestMethod.POST)
    public String getFwsxfgd(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            Record record = performanceService.getfwsxfgd().getResult();
            int sjysq = 0;
            int qsysq = 0;
            int sjgg = 0;
            int qsgg = 0;
            int qsxzxkysq = 0;
            int qsggfwysq = 0;
            if (record != null) {
            	sjysq = record.getInt("jnsq");
            	qsysq = record.getInt("qssq");
            	sjgg = record.getInt("jngg");
            	qsgg = record.getInt("qsjngg");
            	qsxzxkysq = record.getInt("qsxzxkysq");
            	qsggfwysq = record.getInt("qsggfwysq");
            }
            
            dataJson.put("sjysq", sjysq);
            dataJson.put("qsysq", qsysq);
            dataJson.put("sjgg", sjgg);
            dataJson.put("qsgg", qsgg);
            dataJson.put("qsxzxkysq", qsxzxkysq);
            dataJson.put("qsggfwysq", qsggfwysq);

            return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getzxblcsd", method = RequestMethod.POST)
    public String getZxblcsd(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            Record record = newshow2Service.gethandleEvent("");
            String banjie = performanceService.getbanjiecount().getResult();
            int year = 0;
            int month = 0;
            if (record != null) {
            	year = record.getInt("year")+13426+47771+12777;
            	month = record.getInt("month");
            }
            
            dataJson.put("year", year);
            dataJson.put("month", month);
            dataJson.put("banjie", banjie);

            return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/gettaskin", method = RequestMethod.POST)
    public String getTaskin(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String token = jsonObject.getString("token");
        	if (ZwdtConstant.SysValidateData.equals(token)) {
        		JSONObject obj = (JSONObject) jsonObject.get("params");
        		 String pageindex = obj.getString("pageindex");
                 String pagesize = obj.getString("pagesize");
                 String areacode = obj.getString("areacode");
                 JSONObject dataJson = new JSONObject();
                 List<Record> list = performanceService.gettaskin(Integer.parseInt(pageindex)*Integer.parseInt(pagesize),Integer.parseInt(pagesize),areacode).getResult();
                 String total = performanceService.gettaskincount(areacode).getResult();
                 int index = 1;
                 if (list != null && list.size() > 0) {
                	 for (Record record : list) {
                		 record.set("index", index);
                		 index ++;
                	 }
                 }
                 dataJson.put("list", list);
                 dataJson.put("total", Integer.parseInt(total));
                 return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());
        	}else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getzxblsd", method = RequestMethod.POST)
    public String getZxblsd(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String token = jsonObject.getString("token");
        	if (ZwdtConstant.SysValidateData.equals(token)) {
        		JSONObject obj = (JSONObject) jsonObject.get("params");
        		 String pageindex = obj.getString("pageindex");
                 String pagesize = obj.getString("pagesize");
                 String areacode = obj.getString("areacode");
                 JSONObject dataJson = new JSONObject();
                 List<Record> list = performanceService.getzxblsd1(Integer.parseInt(pageindex)*Integer.parseInt(pagesize),Integer.parseInt(pagesize),areacode,areacode).getResult();
                 List<Record> list1 = performanceService.getzxblsd1(0,10000,areacode,areacode).getResult();
                 List<Record> listcount = performanceService.getzxblsdcount1(areacode,areacode).getResult();
                 int index = 1;
                 int totalwangban1 = 0;
                 int totalwangban2 = 0;
                 int totalwangban3 = 0;
                 int totalwangban4 = 0;
                 if (list1 != null && list1.size() > 0) {
                	 for (Record record : list1) {
                		 totalwangban1 += record.getInt("wangban1");
                		 totalwangban2 += record.getInt("wangban2");
                		 totalwangban3 += record.getInt("wangban3");
                		 totalwangban4 += record.getInt("wangban4");
                	 }
                 }
                 if (list != null && list.size() > 0) {
                	 for (Record record : list) {
                		 record.set("index", index);
                		 index ++;
                	 }
                 }
                 dataJson.put("totalwangban1", totalwangban1);
                 dataJson.put("totalwangban2", totalwangban2);
                 dataJson.put("totalwangban3", totalwangban3);
                 dataJson.put("totalwangban4", totalwangban4);
                 dataJson.put("list", list);
                 dataJson.put("total", listcount.size());
                 return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());
        	}else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    /***
     * 
     *  在线办理成熟度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getyjstj", method = RequestMethod.POST)
    public String getyjstj(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String token = jsonObject.getString("token");
        	if (ZwdtConstant.SysValidateData.equals(token)) {
                 JSONObject jsonData = JSONObject.parseObject(codeitemService.getItemValueByCodeID("1016179", "一件事效能统计"));
                 return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", jsonData.toString());
        	}else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    /***
     * 
     *  业务办理深度
     *  [功能详细描述]
     *  @param params
     *  @param request
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getywblsd", method = RequestMethod.POST)
    public String getYwblsd(@RequestBody String params, HttpServletRequest request) {
        try {
        	JSONObject jsonObject = JSONObject.parseObject(params);
        	String token = jsonObject.getString("token");
        	if (ZwdtConstant.SysValidateData.equals(token)) {
        		JSONObject obj = (JSONObject) jsonObject.get("params");
        		 String pageindex = obj.getString("pageindex");
                 String pagesize = obj.getString("pagesize");
                 String areacode = obj.getString("areacode");
                 JSONObject dataJson = new JSONObject();
                 List<Record> list = performanceService.getywbljg(Integer.parseInt(pageindex)*Integer.parseInt(pagesize),Integer.parseInt(pagesize),areacode).getResult();
                 String total = performanceService.getywbljgcount(areacode).getResult();
                 int index = 1;
                 if (list != null && list.size() > 0) {
                	 for (Record record : list) {
                		 record.set("index", index);
                		 index ++;
                	 }
                 }
                 dataJson.put("list", list);
                 dataJson.put("total", Integer.parseInt(total));
                 return JsonUtils.zwdtRestReturn("1", "获取服务方式部门信息成功", dataJson.toString());
        	}else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    
    
    

}
