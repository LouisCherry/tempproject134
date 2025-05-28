package com.epoint.auditydp.jnydp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditydp.jnydp.api.IJnAuditydp;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController
@RequestMapping("/jnauditydp")
public class JnAuditydpRestcontroller
{
    @Autowired
   private IJnAuditydp ydpservice;
    @Autowired
    private IAuditZnsbCentertask centertaskservice;
    
    @RequestMapping(value = {"/getOuList" }, method = {RequestMethod.POST })
    public String getOuList(@RequestBody String params) {
        try{
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String condition = obj.getString("condition");
            int firstpage = Integer.parseInt(obj.getString("firstpage")) ;
            int pagesize = Integer.parseInt(obj.getString("pagesize"));
            List<JSONObject> jsonList = new ArrayList<>();
            List<FrameOu> oulist = ydpservice.getConditionOU(condition, firstpage*pagesize, pagesize).getResult();
            int count = ydpservice.getConditionOUCount(condition);
            JSONObject dataJson = new JSONObject();
            for (FrameOu ou : oulist){
                JSONObject jsonobject = new JSONObject();
                String ouname = StringUtil.isNotBlank(ou.getOushortName())?ou.getOushortName():ou.getOuname();
                jsonobject.put("ouguid", ou.getOuguid());
                jsonobject.put("ouname", ouname);
                jsonList.add(jsonobject);
            }
            
            dataJson.put("count", count);
            dataJson.put("oulist", jsonList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常", e.getMessage());
        }
    }
    @RequestMapping(value = {"/getTaskList" }, method = {RequestMethod.POST })
    public String getTaskList(@RequestBody String params) {
        try{
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String condition = obj.getString("condition");
            int firstpage = Integer.parseInt(obj.getString("firstpage")) ;
            int pagesize = Integer.parseInt(obj.getString("pagesize"));
            List<JSONObject> jsonList = new ArrayList<>();
            int count = ydpservice.getConditionTaskCount(condition);
            List<AuditZnsbCentertask> taskList = ydpservice.getConditionTask(condition, firstpage*pagesize, pagesize).getResult();
            JSONObject dataJson = new JSONObject();
            for (AuditZnsbCentertask task : taskList){
                JSONObject jsonobject = new JSONObject();
            
                jsonobject.put("taskID", task.getTask_id());
                jsonobject.put("taskname", task.getTaskname());
                jsonobject.put("taskguid", task.getTaskguid());
                jsonList.add(jsonobject);
            }
            dataJson.put("count", count);
            dataJson.put("tasklist", jsonList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常", e.getMessage());
        }
    }
    @RequestMapping(value = {"/getMapList" }, method = {RequestMethod.POST })
    public String getMapList(@RequestBody String params) {
        try{
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String condition = obj.getString("condition");
            int firstpage = Integer.parseInt(obj.getString("firstpage")) ;
            int pagesize = Integer.parseInt(obj.getString("pagesize"));
            List<JSONObject> jsonList = new ArrayList<>();
            int count = ydpservice.getConditionDictCount(condition);
            List<AuditTaskDict> mapList = ydpservice.getConditionDict(condition, firstpage*pagesize, pagesize).getResult();
            JSONObject dataJson = new JSONObject();
            for (AuditTaskDict map : mapList){
                JSONObject jsonobject = new JSONObject();
            
                jsonobject.put("dictno", map.getNo());
                jsonobject.put("dictname", map.getClassname());
                jsonList.add(jsonobject);
            }
            dataJson.put("count", count);
            dataJson.put("dictlist", jsonList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常", e.getMessage());
        }
    }
    @RequestMapping(value = {"/getWindowList" }, method = {RequestMethod.POST })
    public String getWindowList(@RequestBody String params) {
        try{
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String taskID = obj.getString("taskID");
            int firstpage = Integer.parseInt(obj.getString("firstpage")) ;
            int pagesize = Integer.parseInt(obj.getString("pagesize"));
            List<JSONObject> jsonList = new ArrayList<>();
            int count = ydpservice.getWindowListCountByTaskId(taskID);
            List<AuditOrgaWindow> windowList = ydpservice.getWindowListByTaskId(taskID, firstpage*pagesize, pagesize).getResult();
            JSONObject dataJson = new JSONObject();
            for (AuditOrgaWindow map : windowList){
                JSONObject jsonobject = new JSONObject();
            
                jsonobject.put("windowguid", map.getRowguid());
                jsonobject.put("windowname", map.getWindowname());
                jsonobject.put("windowno", map.getWindowno());
                jsonList.add(jsonobject);
            }
            dataJson.put("count", count);
            dataJson.put("windowlist", jsonList);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "出现异常", e.getMessage());
        }
    }
    @RequestMapping(value = {"/getTaskListByOU" }, method = {RequestMethod.POST })
    public String getTaskListByOU(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String ouguid = obj.getString("ouguid");
            String currentPage = obj.getString("firstpage");
            String pageSize = obj.getString("pagesize");
            List<JSONObject> list = new ArrayList<JSONObject>();
            new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.in("centerguid", "'46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884'");
            sql.eq("is_enable", "1");
            sql.eq("ouguid", ouguid);
            PageData<AuditZnsbCentertask> hottaskpagedata = centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "ordernum", "desc")
                    .getResult();
            int totalcount = hottaskpagedata.getRowCount();
            for( AuditZnsbCentertask hottask :hottaskpagedata.getList()){
                JSONObject hottaskJson = new JSONObject();
                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskID", hottask.getTask_id());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("tasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
    @RequestMapping(value = {"/getTaskListByMAP" }, method = {RequestMethod.POST })
    public String getTaskListByMAP(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String no = obj.getString("no");
            String currentPage = obj.getString("firstpage");
            String pageSize = obj.getString("pagesize");
            List<JSONObject> list = new ArrayList<JSONObject>();
            new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.in("centerguid", "'46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884'");
            sql.eq("is_enable", "1");
            sql.like("taskmap", no + ";");
            PageData<AuditZnsbCentertask> hottaskpagedata = centertaskservice
                    .getCenterTaskPageData(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "ordernum", "desc")
                    .getResult();
            int totalcount = hottaskpagedata.getRowCount();
            for( AuditZnsbCentertask hottask :hottaskpagedata.getList()){
                JSONObject hottaskJson = new JSONObject();
                hottaskJson.put("taskname", hottask.getTaskname());
                hottaskJson.put("taskID", hottask.getTask_id());
                hottaskJson.put("taskguid", hottask.getTaskguid());
                list.add(hottaskJson);
            }

            dataJson.put("tasklist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }   
}
