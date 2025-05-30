package com.epoint.hcp.rest.controller;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.hcp.HcpJsonUtils;
import com.epoint.hcp.api.IAuditHcpShow;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;

/**
 * 好差评大屏相关接口
 * @author an
 * 
 */
@RestController
@RequestMapping("/xjhcpshow")
public class AuditXjHcpShowController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditHcpShow iAuditHcpShow;
    
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 获取好差评辖区信息
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/area", method = RequestMethod.POST)
    public String getArea(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用arealist接口=======");
            List<JSONObject> list = iAuditHcpShow.getArea();
            return HcpJsonUtils.hcpRestReturn("1", "获取成功！", true, list.toString());
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======arealist接口参数：params【" + params + "】=======");
            log.info("=======arealist异常信息：" + e.getMessage() + "=======");
            return HcpJsonUtils.hcpRestReturn("0", "获取失败：" + e.getMessage(), false, "");
        }
    }

    /**
     * 获取省级部门好差评情况信息
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/oulist", method = RequestMethod.POST)
    public String getOuList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getOuList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");// 辖区编码
            String num = obj.getString("num");// 前N位
            if (StringUtil.isBlank(num)) {
                num="7";//默认7位
            }
            List<AuditHcpOuinfo> oulist = iAuditHcpShow.getOuList(Integer.valueOf(num), areacode);
            JSONArray jsonArray = new JSONArray();
            if (oulist != null && oulist.size() > 0) {
                for (AuditHcpOuinfo ou : oulist) {
                    JSONObject json = new JSONObject();
                    json.put("ouname", ou.getOuname());
                    json.put("bjcount", ou.getBjcount());
                    json.put("cpcount", ou.getCpcount());
                    json.put("myd", ou.getMyd());
                    jsonArray.add(json);
                }
                JSONObject retJson = new JSONObject();
                retJson.put("oulist", jsonArray);
                return HcpJsonUtils.hcpRestReturn("1", "获取成功！", true, retJson);
            }
            else {
                return HcpJsonUtils.hcpRestReturn("0", "获取失败！", false, "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======summaryinfo接口参数：params【" + params + "】=======");
            log.info("=======summaryinfo异常信息：" + e.getMessage() + "=======");
            return HcpJsonUtils.hcpRestReturn("0", "获取失败：" + e.getMessage(), false, "");
        }
    }

    /**
     * 统计全省各个区域好差评数据
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/arealist", method = RequestMethod.POST)
    public String getAreaList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用arealist接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");// 辖区编码
            String num = obj.getString("num");// 前N位
            if (StringUtil.isBlank(num)) {
                num="7";//默认7位
            }
            if (StringUtil.isBlank(areacode)) {
                return HcpJsonUtils.hcpRestReturn("0", "参数结构不正确，请检查参数结构！", false, "");
            }
            List<AuditHcpAreainfo> arealist = iAuditHcpShow.getAreaList(Integer.valueOf(num), areacode);
            JSONArray jsonArray = new JSONArray();
            if (arealist != null && arealist.size() > 0) {
                for (AuditHcpAreainfo area : arealist) {
                    JSONObject json = new JSONObject();
                    json.put("areaname", area.getAreaname());
                    json.put("bjcount", area.getBjcount());
                    json.put("cpcount", area.getCpcount());
                    json.put("myd", area.getMyd());
                    jsonArray.add(json);
                }
            }
            AuditHcpAreainfo area = iAuditHcpShow.getAreaByCode(areacode);
            JSONObject retJson = new JSONObject();
            retJson.put("totalnum", area.getPjcount());
            retJson.put("fcmynum", area.getFcmynum());
            retJson.put("mynum", area.getMynum());
            retJson.put("jbmynum", area.getJbmynum());
            retJson.put("bmynum", area.getBmynum());
            retJson.put("fcbmynum", area.getFcbmynum());
            retJson.put("cpnum", area.getCpcount());
            retJson.put("zgl", area.getZgl());
            retJson.put("pc", area.getPcnum());
            retJson.put("app", 33961);
            retJson.put("yd", area.getYdnum());
            retJson.put("pad", area.getPadnum());
            retJson.put("ytj", area.getYtjnum());
            retJson.put("arealist", jsonArray);
            return HcpJsonUtils.hcpRestReturn("1", "获取成功！", true, retJson);
            
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======arealist接口参数：params【" + params + "】=======");
            log.info("=======arealist异常信息：" + e.getMessage() + "=======");
            return HcpJsonUtils.hcpRestReturn("0", "获取失败：" + e.getMessage(), false, "");
        }
    }
    
    /**
     * 获取用户信息列表
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/userpage", method = RequestMethod.POST)
    public String getUserPage(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用sourceinfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");// 区域标识
            if("370882".equals(areacode)) {
            	areacode = "370812";
            }
            int currentPage = obj.getInteger("currentpage");// 当前页码
            int pageSize = obj.getInteger("pagesize");// 每页显示数量
            List<Record> userList = iAuditHcpShow.getUserList(areacode, currentPage, pageSize);
            int totalcount = iAuditHcpShow.getUserListCount(areacode);
            JSONObject dataJson = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (userList != null && userList.size() > 0) {
                for (Record user : userList) {
                    JSONObject json = new JSONObject();
                    json.put("username", coverUsername(user.getStr("username")));
                    json.put("taskname", user.getStr("taskname"));
                    json.put("prodepart", user.getStr("prodepart"));
                    json.put("satisfaction", user.getStr("satisfaction"));
                    json.put("acceptdate", user.getStr("acceptdate"));
                    jsonArray.add(json);
                }
            }
            dataJson.put("userlist", jsonArray);
            // 3、定义返回JSON对象
            dataJson.put("totalcount", totalcount);
            return HcpJsonUtils.hcpRestReturn("1", "获取成功！", true, dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======userpage接口参数：params【" + params + "】=======");
            log.info("=======userpage异常信息：" + e.getMessage() + "=======");
            return HcpJsonUtils.hcpRestReturn("0", "获取失败：" + e.getMessage(), false, "");
        }
    }
    
    
    /**
     * 统计全省各个区域好差评数据
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/areaCount", method = RequestMethod.POST)
    public String areaCount(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用areaCount接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");// 辖区编码
            if (StringUtil.isBlank(areacode)) {
                return HcpJsonUtils.hcpRestReturn("0", "参数结构不正确，请检查参数结构！", false, "");
            }
            AuditHcpAreainfo area = iAuditHcpShow.getAreaByCode(areacode);
            
            int Fcmynum = area.getFcmynum();
            int Mynum = area.getMynum();
            int Jbmynum = area.getJbmynum();
            int Bmynum = area.getBmynum();
            int Fcbmynum = area.getFcbmynum();
            int total = Fcmynum + Mynum + Jbmynum + Bmynum + Fcbmynum;
            DecimalFormat df = new DecimalFormat("0.00");
            double fcbaifen = Double.parseDouble(df.format((float) Fcmynum * 100 / total)) ;
            double mybaifen = Double.parseDouble(df.format((float) Mynum * 100 / total)) ;
            double jbbaifen = Double.parseDouble(df.format((float) Jbmynum * 100 / total)) ;
            double bubaifen = Double.parseDouble(df.format((float) Bmynum * 100 / total)) ;
            double fcbbaifen = Double.parseDouble(df.format((float) Fcbmynum * 100 / total)) ;
            JSONArray jsonarray = new JSONArray();
            JSONObject result = new JSONObject();
            JSONObject retJson = new JSONObject();
            retJson.put("name", "非常满意");
            retJson.put("rate", fcbaifen);
            retJson.put("value", Fcmynum);
            jsonarray.add(retJson);
            
            JSONObject retJson1 = new JSONObject();
            retJson1.put("name", "满意");
            retJson1.put("rate", mybaifen);
            retJson1.put("value", Mynum);
            jsonarray.add(retJson1);
            JSONObject retJson2 = new JSONObject();
            retJson2.put("name", "基本满意");
            retJson2.put("rate", jbbaifen);
            retJson2.put("value", Jbmynum);
            jsonarray.add(retJson2);
            JSONObject retJson3 = new JSONObject();
            retJson3.put("name", "不满意");
            retJson3.put("rate", bubaifen);
            retJson3.put("value", Bmynum);
            jsonarray.add(retJson3);
            JSONObject retJson4 = new JSONObject();
            retJson4.put("name", "非常不满意");
            retJson4.put("rate", fcbbaifen);
            retJson4.put("value", Fcbmynum);
            jsonarray.add(retJson4);
            
            JSONObject abarbeitung = new JSONObject();
            abarbeitung.put("rate", 100);
            abarbeitung.put("done", 10000);
            abarbeitung.put("not", 1000);
            
            result.put("abarbeitung", abarbeitung);
            result.put("evaluate", jsonarray);
            
            
            result.put("total",total );
            return HcpJsonUtils.hcpRestReturn("1", "获取成功！", true, result);
            
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======areaCount接口参数：params【" + params + "】=======");
            log.info("=======areaCount异常信息：" + e.getMessage() + "=======");
            e.printStackTrace();
            return HcpJsonUtils.hcpRestReturn("0", "获取失败：" + e.getMessage(), false, "");
        }
    }

    //用户姓名脱敏
    private static String coverUsername(String username) {
        // 如果是姓名的话
        if (StringUtil.isNotBlank(username)) {
            if (username.length() == 2) {
                username = username.replace(username.substring(username.length() - 1), "*");
            }
            if (username.length() == 3) {
                username = username.replace(username.substring(username.length() - 2), "**");
            }
            if (username.length() == 4) {
                username = username.replace(username.substring(username.length() - 2), "**");
            }
            //如果是公司名称
            if (username.length() > 5) {
                username = username.replace(username.substring(2, 5), "**");
            }
            return username;
        }
        return null;
    }
    
    /**
     * 获取用户信息列表
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/evaluatezb", method = RequestMethod.POST)
    public String getEvaluateZb(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用evaluatezb接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String areacode = obj.getString("areacode");// 区域标识
            String evatype = obj.getString("evatype");// 好评或者差评
            List<Record> list = iAuditHcpShow.getEvaluateZb(areacode,evatype);
            List<Record> results = new ArrayList<Record>(); 
            Map<String,Integer> map = new HashMap<String,Integer>();
            if (list != null && list.size() > 0) {
            	for (Record detail : list) {
            		String evaldetail = detail.getStr("evaldetail");
            		int total = detail.getInt("total");
            		if (evaldetail.contains(",")) {
            			String[] evas = evaldetail.split(",");
            			for (String eva : evas) {
            				if (StringUtil.isBlank(map.get(eva))) {
            					map.put(eva, total);
            				}else {	
            					map.put(eva, total+map.get(eva));
            				}
            			}
            		}else {
            			if (StringUtil.isBlank(map.get(evaldetail))) {
        					map.put(evaldetail, total);
        				}else {	
        					map.put(evaldetail, total+map.get(evaldetail));
        				}
            		}
            	}
            }
            JSONObject dataJson = new JSONObject();
            map = sortDescend(map);
            int count = 0;
            for (Entry<String, Integer> entry : map.entrySet()) {
               String key = entry.getKey();
               Record record = new Record();
               record.set("name", iCodeItemsService.getItemTextByCodeName("好差评满意度", key));
               record.set("total", entry.getValue());
               results.add(record);
               count += entry.getValue();
              }
            if (results.size() >= 5) {
            	results = results.subList(0, 5);
            }
            for (Record record : results) {
            	int cc = record.getInt("total");
            	record.set("avg", Math.ceil((double)cc/(double)count*100));
            }
            dataJson.put("list", results);
            dataJson.put("total", count);
            log.info("=======结束调用evaluatezb接口=======");
            return JsonUtils.zwdtRestReturn("1", "查询好差评指标成功！", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======evaluatezb接口参数：params【" + params + "】=======");
            log.info("=======evaluatezb异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询好差评指标失败：" + e.getMessage(), "");
        }
    }
    
    
    // Map的value值降序排序
    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });
 
        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

}
