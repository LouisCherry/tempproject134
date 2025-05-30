package com.epoint.hqzc.rest;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.hqzc.api.IJnHqzcRestService;

/**
 * 事项相关接口
 * 
 * @作者 WST
 * @version [F9.3, 2017年11月9日]
 */
@RestController
@RequestMapping("/jjnzwdtTask")
public class JnHqzcController
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnHqzcRestService iJnHqzcRestService;
    
    @Autowired
    private IAttachService iAttachService;
   
    
    /**g
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/selectOuList", method = RequestMethod.POST)
    public String selectOuList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用selectOuList接口=======");
            // 1、入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            JSONObject result = json.getJSONObject("params");
            String areacode = result.getString("areacode");
            List<Record> list = iJnHqzcRestService.selectOuList(areacode).getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                log.info("=======结束调用selectOuList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======selectOuList接口参数：params【" + params + "】=======");
            log.info("=======selectOuList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getHyflList", method = RequestMethod.POST)
    public String getHyflList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getHyflList接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            List<Record> list = iJnHqzcRestService.getHyflList().getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                log.info("=======结束调用getHyflList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getHyflList接口参数：params【" + params + "】=======");
            log.info("=======getHyflList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getHygmList", method = RequestMethod.POST)
    public String getHygmList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getHygmList接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            List<Record> list = iJnHqzcRestService.getHygmList().getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                log.info("=======结束调用getHygmList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getHygmList接口参数：params【" + params + "】=======");
            log.info("=======getHygmList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getSmzqList", method = RequestMethod.POST)
    public String getSmzqList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getSmzqList接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            List<Record> list = iJnHqzcRestService.getSmzqList().getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                log.info("=======结束调用getSmzqList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getSmzqList接口参数：params【" + params + "】=======");
            log.info("=======getSmzqList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }
   
    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getPolicyist", method = RequestMethod.POST)
    public String getPolicyist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPolicyist接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String department = jsonObject.getString("department");
            String hangyeguimo = jsonObject.getString("hangyeguimo");
            String areacode = jsonObject.getString("areacode");
            int pageIndex = Integer.parseInt(jsonObject.getString("pageIndex"));
            int pageSize = Integer.parseInt(jsonObject.getString("pageSize"));
            String policyType = jsonObject.getString("policyType");
            String shengmingzhouqi = jsonObject.getString("shengmingzhouqi");
            String sixinqiye = jsonObject.getString("sixinqiye");
            
            List<Record> records = iJnHqzcRestService.selectOuList(areacode).getResult();
            String ouguids = "";
            if (records != null && records.size() > 0) {
                for (Record record : records) {
                    ouguids += "'" + record.getStr("ssbm") + "',";
                }
                ouguids = "(" + ouguids.substring(0, ouguids.length() - 1) + ")";
            }
            System.out.println("ouguids:"+ouguids);
            List<Record> list = iJnHqzcRestService.getPolicyListByContion(department, policyType, hangyeguimo, shengmingzhouqi, sixinqiye, pageIndex * pageSize, pageSize,ouguids).getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                dataJson.put("total", iJnHqzcRestService.getPolicyListByContion(department, policyType, hangyeguimo, shengmingzhouqi, sixinqiye,ouguids).getResult().getStr("total"));
                log.info("=======结束调用getPolicyist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPolicyist接口参数：params【" + params + "】=======");
            log.info("=======getPolicyist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getPolicySerach", method = RequestMethod.POST)
    public String getPolicySerach(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPolicySerach接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            int pageIndex = Integer.parseInt(jsonObject.getString("pageIndex"));
            int pageSize = Integer.parseInt(jsonObject.getString("pageSize"));
            String serachpolicy = jsonObject.getString("serachpolicy");
            List<Record> list = iJnHqzcRestService.getPolicyListByContionFirst(serachpolicy, pageIndex, pageSize).getResult();
                JSONObject dataJson = new JSONObject();
                dataJson.put("list", list);
                dataJson.put("total", list.size());
                log.info("=======结束调用getPolicySerach接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPolicySerach接口参数：params【" + params + "】=======");
            log.info("=======getPolicySerach异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getPolicydetail", method = RequestMethod.POST)
    public String getPolicydetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPolicydetail接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject param = jsonObject.getJSONObject("params");
            String rowguid = param.getString("rowguid");
            Record record = iJnHqzcRestService.getPolicyDetailByRowguid(rowguid).getResult();
            String zczn = record.getStr("zczn");
            String ssxz = record.getStr("ssxz");
            JSONObject dataJson = new JSONObject();
            List<FrameAttachInfo> attachzczn =  iAttachService.getAttachInfoListByGuid(zczn);
            List<FrameAttachInfo> attachssxz =  iAttachService.getAttachInfoListByGuid(ssxz);
            if (StringUtil.isNotBlank(zczn) && attachzczn != null && attachzczn.size() > 0) {
                record.set("zcznname",attachzczn.get(0).getAttachFileName());
                record.set("zcznattachguid", attachzczn.get(0).getAttachGuid());
            }
            if (StringUtil.isNotBlank(ssxz) && attachssxz != null && attachssxz.size() > 0) {
                record.set("ssxzname", attachssxz.get(0).getAttachFileName());
                record.set("ssxzattachguid", attachssxz.get(0).getAttachGuid());
            }
            dataJson.put("record", record);
            log.info("=======结束调用getPolicydetail接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
        }
        catch (Exception e) {
            log.info("=======getPolicydetail接口参数：params【" + params + "】=======");
            log.info("=======getPolicydetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }

}
