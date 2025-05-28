package com.epoint.auditqueueqhjmodule.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditqueueqhjmodule.api.IAuditQueueQhjmoduleService;
import com.epoint.auditqueueqhjmodule.entity.AuditQueueQhjmodule;
import com.epoint.auditqueueqhjmoduletasktype.api.IAuditQueueQhjmoduleTasktypeService;
import com.epoint.auditqueueqhjmoduletasktype.api.entity.AuditQueueQhjmoduleTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;

@RestController
@RequestMapping("/auditqueueqhjmodule")
public class AuditQueueQhjmoduleRestController
{

    @Autowired
    private IAuditQueueQhjmoduleService qhjmoduleService;

    @Autowired
    private IAuditQueueQhjmoduleTasktypeService qhjmoduleTasktypeService;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private IHandleQueue handlequeueservice;

    @Autowired
    private IOuService ouservice;

    /**
     * 取号机模块 list 获取
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getQhjModuleList", method = RequestMethod.POST)
    public String getQhjModuleList(@RequestBody String params) {
        JSONObject json = JSON.parseObject(params);
        JsonUtils.checkUserAuth(json.getString("token"));
        JSONObject obj = (JSONObject) json.get("params");
        JSONObject dataJson = new JSONObject();
        String centerguid = obj.getString("centerguid");
        String hallguid = obj.getString("hallguid");

        if (StringUtil.isNotBlank(centerguid)) {
            List<AuditQueueQhjmodule> list = qhjmoduleService.findList(
                    "select rowguid, modulename, bgcls, isshowou from audit_queue_qhjmodule where centerguid = ? order by ordernum desc",
                    centerguid);
            for (AuditQueueQhjmodule auditQueueQhjmodule : list) {
                auditQueueQhjmodule.put("hallguid", hallguid);
                if ("1".equals(auditQueueQhjmodule.getIsshowou())) {
                    auditQueueQhjmodule.put("url", "./unit_selection?hallguid=" + hallguid);
                }
                else {
                    auditQueueQhjmodule.put("url", "./list_page?qhjmoduleguid=" + auditQueueQhjmodule.getRowguid());
                }
            }
            dataJson.put("list", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        else {
            return JsonUtils.zwdtRestReturn("0", "参数传递错误！", "");
        }

    }

    /**
     * 取号机模块关联事项类别 list 获取
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getQhjModuleTasktypeList", method = RequestMethod.POST)
    public String getQhjModuleTasktypeList(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            String qhjmoduleguid = obj.getString("qhjmoduleguid");
            int currentpage = obj.getIntValue("currentpage");
            int pagesize = obj.getIntValue("pagesize");

            if (StringUtil.isNotBlank(qhjmoduleguid)) {
                List<AuditQueueQhjmoduleTasktype> datalist = qhjmoduleTasktypeService.findList(
                        "select tasktypeguid from audit_queue_qhjmodule_tasktype where qhjmoduleguid = ?",
                        qhjmoduleguid);
                int index = currentpage * pagesize;
                int finalIndex = index + pagesize;
                finalIndex = finalIndex > datalist.size() ? datalist.size() : finalIndex;
                for (int i = index; i < finalIndex; i++) {
                    JSONObject tasktypeJson = new JSONObject();
                    AuditQueueTasktype tasktype = tasktypeservice
                            .getAuditQueueTasktypeByRowguid(datalist.get(i).getTasktypeguid()).getResult();
                    tasktypeJson.put("tasktypeguid", tasktype.getRowguid());
                    tasktypeJson.put("tasktypename", tasktype.getTasktypename());
                    tasktypeJson.put("isface", tasktype.getIs_face());
                    tasktypeJson.put("taskwaitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult()));
                    tasktypeJson.put("ouname", ouservice.getOuByOuGuid(tasktype.getOuguid()).getOuname());
                    list.add(tasktypeJson);
                }

                dataJson.put("tasktypelist", list);
                dataJson.put("totalcount", datalist.size());
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数传递错误！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 取号机模块关联事项类别 list 通过模块名称获取
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getQhjModuleTasktypeListByName", method = RequestMethod.POST)
    public String getQhjModuleTasktypeListByName(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            String qhjmodulename = obj.getString("qhjmodulename");
            int currentpage = obj.getIntValue("currentpage");
            int pagesize = obj.getIntValue("pagesize");

            AuditQueueQhjmodule find = qhjmoduleService
                    .find("select rowguid from audit_queue_qhjmodule where modulename = ?", qhjmodulename);

            if (find != null) {
                String qhjmoduleguid = find.getRowguid();
                List<AuditQueueQhjmoduleTasktype> datalist = qhjmoduleTasktypeService.findList(
                        "select tasktypeguid from audit_queue_qhjmodule_tasktype where qhjmoduleguid = ?",
                        qhjmoduleguid);
                int index = currentpage * pagesize;
                int finalIndex = index + pagesize;
                finalIndex = finalIndex > datalist.size() ? datalist.size() : finalIndex;
                for (int i = index; i < finalIndex; i++) {
                    JSONObject tasktypeJson = new JSONObject();
                    AuditQueueTasktype tasktype = tasktypeservice
                            .getAuditQueueTasktypeByRowguid(datalist.get(i).getTasktypeguid()).getResult();
                    tasktypeJson.put("tasktypeguid", tasktype.getRowguid());
                    tasktypeJson.put("tasktypename", tasktype.getTasktypename());
                    tasktypeJson.put("isface", tasktype.getIs_face());
                    tasktypeJson.put("taskwaitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult()));
                    tasktypeJson.put("ouname", ouservice.getOuByOuGuid(tasktype.getOuguid()).getOuname());
                    list.add(tasktypeJson);
                }

                dataJson.put("tasktypelist", list);
                dataJson.put("totalcount", datalist.size());
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数传递错误！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
