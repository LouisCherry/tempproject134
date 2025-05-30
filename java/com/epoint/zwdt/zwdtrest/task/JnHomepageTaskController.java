package com.epoint.zwdt.zwdtrest.task;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/homepagetask")
public class JnHomepageTaskController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    IAttachService attachService;

    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnAppRestService iJnAppRestService;

    /**
     * 获取老年人事项信息的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskList1", method = RequestMethod.POST)
    public String getTaskList1(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskList1接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskList(obj);
                log.info("=======结束调用getTaskList1接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskList1接口参数：params【" + params + "】=======");
            log.info("=======getTaskList1异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项失败：" + e.getMessage(), "");
        }
    }

    /**
     * 通用方法获取事项列表
     *
     * @param obj 接口传入参数
     * @return
     */
    public JSONObject handleGetTaskList(JSONObject obj) {
        //事项当前页码
        String currentPageTask = obj.getString("currentpage");
        //事项每页数目
        String pageSizeTask = obj.getString("pagesize");
        // 定义返回JSON对象
        JSONObject dataJson = new JSONObject();
        try {
            int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
            // 3.1.2、主项事项结果返回
            PageData<AuditTask> fatherTaskPageData = null;
            // 3.1.2.2、获取事项
            fatherTaskPageData = iJnAppRestService.getLnrAuditTaskPageData(firstResultTask, Integer.parseInt(pageSizeTask));
            int totalTaskCount = fatherTaskPageData.getRowCount();// 查询事项的数量
            List<AuditTask> fatherAuditTaskList = fatherTaskPageData.getList();// 查询事项的信息
            List<JSONObject> fatherTaskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表
            for (AuditTask auditTask : fatherAuditTaskList) {
                JSONObject auditTaskJson = new JSONObject();
                auditTaskJson.put("taskname", auditTask.getTaskname());// 事项名称
                auditTaskJson.put("taskguid", auditTask.getRowguid());// 事项标识
                auditTaskJson.put("taskid", auditTask.getTask_id());// 事项唯一标识
                auditTaskJson.put("ouname", auditTask.getOuname());// 部门名称
                fatherTaskJsonList.add(auditTaskJson);
            }
            dataJson.put("tasklist", fatherTaskJsonList);
            dataJson.put("totalcount", totalTaskCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJson;
    }

}
