package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.code.entity.CodeMain;

/**
 * 建设工程竣工联合验收一件事相关接口
 */
@RestController
@RequestMapping("/gglhysyjs")
public class GglhysYjsController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private ICodeMainService iCodeMainService;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditSpTask iAuditSpTask;
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    @RequestMapping(value = "/getxftask", method = RequestMethod.POST)
    public String getxftask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getxftask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1 辖区代码
                JSONArray areacode = obj.getJSONArray("areacode");
                String xfystype = obj.getString("xfystype");
                // 是否为高新区
                String isGxqarea = obj.getString("isGxqarea");

                JSONArray tasklist = new JSONArray();
                if (areacode != null && !areacode.isEmpty()) {
                    for(int i = 0;i<areacode.size();i++){
                        String value = areacode.getString(i);
                        AuditOrgaArea area = iAuditOrgaArea.getAreaByAreacode(value).getResult();
                        if (area != null) {
                            log.info("===area==="+area.getXiaquname());
                            String CodeText = area.getXiaquname()+"消防验收事项";
                            CodeMain codeMainByName = iCodeMainService.getCodeMainByName(CodeText);
                            if (codeMainByName != null) {
                                CodeItems itemTextByCodeName = iCodeItemsService.getCodeItemByCodeID(codeMainByName.getCodeID() + "", xfystype);
                                List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByLevel(codeMainByName.getCodeID()+"", itemTextByCodeName.getItemId()+"", 0);
                                if (codeItems != null && !codeItems.isEmpty()) {
                                    for (CodeItems codeItem : codeItems) {
                                        log.info("===codeItem==="+codeItem.getItemValue());
                                        AuditTask task = iAuditTask.getAuditTaskByGuid(codeItem.getItemValue(), true).getResult();
                                        log.info("===task==="+task);
                                        if(task!=null){
                                            JSONObject taskjson = new JSONObject();
                                            taskjson.put("taskname",task.getTaskname());
                                            taskjson.put("taskguid",task.getRowguid());
                                            taskjson.put("task_id",task.getTask_id());
                                            tasklist.add(taskjson);
                                        }
                                    }
                                }
                            }
                        }
                        // 房屋建筑和市政基础设施工程竣工验收备案事项
                        // 是高新区的时候该事项需要获取不同的
                        if ("1".equals(isGxqarea)) {
                            String taskid = iCodeItemsService.getItemTextByCodeName("房屋建筑和市政基础设施工程竣工验收备案事项", value);
                            if (StringUtil.isNotBlank(taskid)) {
                                AuditTask task = iAuditTask.getUseTaskAndExtByTaskid(taskid).getResult();
                                if (task != null) {
                                    JSONObject taskjson = new JSONObject();
                                    taskjson.put("taskname", task.getTaskname());
                                    taskjson.put("taskguid", task.getRowguid());
                                    taskjson.put("task_id", task.getTask_id());
                                    tasklist.add(taskjson);
                                }
                            }
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", tasklist);
                log.info("=======结束调用getxftask接口=======");

                return JsonUtils.zwdtRestReturn("1", " 获取事项信息成功！", dataJson.toString());


            } else {
                log.info("=======结束调用getxftask接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getxftask接口参数：params【" + params + "】=======");
            log.info("=======getxftask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getxftask异常信息：" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getxftaskbybusiness", method = RequestMethod.POST)
    public String getxftaskbybusiness(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getxftaskbybusiness接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = jsonObject.getJSONObject("params");
                // 1.1
                String businessguid = obj.getString("businessguid");
                String phaseguid = obj.getString("phaseguid");
                String areacode = obj.getString("areacode");

                AuditOrgaArea area = iAuditOrgaArea.getAreaByAreacode(areacode).getResult();
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();

                sqlConditionUtil.setLeftJoinTable("audit_sp_basetask b","a.BASETASKGUID","b.rowguid");
                sqlConditionUtil.eq("a.BUSINESSGUID",businessguid);
                sqlConditionUtil.eq("a.PHASEGUID",phaseguid);
                sqlConditionUtil.eq("b.belongto_area",areacode);
                List<String> taskidlist = new ArrayList<>();
                List<String> basetaskguidlist = iAuditSpTask.getAllAuditSpTask(sqlConditionUtil.getMap()).getResult().stream().map(sptask -> sptask.getStr("BASETASKGUID")).collect(Collectors.toList());
                System.out.println("basetaskguidlist:"+basetaskguidlist);
                if(basetaskguidlist.size()>0&&!basetaskguidlist.isEmpty()){
                    SqlConditionUtil basetaskrsql  = new SqlConditionUtil();
                    basetaskrsql.in("BASETASKGUID",StringUtil.joinSql(basetaskguidlist));
                    taskidlist= iAuditSpBasetaskR.getAuditSpBasetaskrByCondition(basetaskrsql.getMap()).getResult().stream().map(basetakr -> basetakr.getTaskid()).collect(Collectors.toList());

                }
                JSONArray tasklist = new JSONArray();
                if(taskidlist.size()>0&&!taskidlist.isEmpty()){
                    for (String taskid : taskidlist) {
                        AuditTask task = iAuditTask.selectUsableTaskByTaskID(taskid).getResult();
                        AuditOrgaArea taskarea = iAuditOrgaArea.getAreaByAreacode(task.getAreacode()).getResult();
                        if(task!=null){
                            JSONObject taskjson = new JSONObject();
                            if(taskarea!=null){
                                taskjson.put("taskname",task.getTaskname()+"("+taskarea.getXiaquname()+")");
                            }
                            else{
                                taskjson.put("taskname",task.getTaskname());
                            }

                            taskjson.put("taskguid",task.getRowguid());
                            taskjson.put("task_id",task.getTask_id());
                            tasklist.add(taskjson);
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", tasklist);
                log.info("=======结束调用getxftaskbybusiness接口=======");

                return JsonUtils.zwdtRestReturn("1", " 获取事项信息成功！", dataJson.toString());


            } else {
                log.info("=======结束调用getxftaskbybusiness接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getxftaskbybusiness接口参数：params【" + params + "】=======");
            log.info("=======getxftaskbybusiness异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getxftaskbybusiness异常信息：" + e.getMessage(), "");
        }
    }

}
