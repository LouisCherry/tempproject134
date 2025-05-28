package com.epoint.union.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.epoint.union.audituniontask.api.IAuditUnionTaskService;
import com.epoint.union.audituniontask.api.entity.AuditUnionTask;
import com.epoint.union.audituniontaskuser.api.IAuditUnionTaskUserService;

@RestController
@RequestMapping("/unionProject")
public class JnUnionProjectRest
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditTask auditTask;
    @Autowired
    private IAuditTaskMaterial auditTaskMaterial;
    
    
    /**
     * 
     *  异地通办事项
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public String getTaskList(@RequestBody String params) {
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String taskname = param.getString("taskname");
                String ouname = param.getString("ouname");
                String itemid = param.getString("itemid");
                String areacode = param.getString("areacode");
                Integer first = param.getInteger("first");
                Integer pageSize = param.getInteger("pageSize");
                JSONObject dataJson = new JSONObject();
                String sql = "select * from audit_union_task where 1=1";
                if(StringUtil.isNotBlank(taskname)) {
                	sql +=" and taskname like '%"+taskname+"%'";
                }
                if(StringUtil.isNotBlank(ouname)) {
                	sql +=" and ouname like '%"+ouname+"%'";
                }
                if(StringUtil.isNotBlank(itemid)) {
                	sql +=" and itemid like '%"+itemid+"%'";
                }
                if(StringUtil.isNotBlank(areacode)) {
                	sql +=" and areacode = '"+areacode+"'";
                }
                IAuditUnionTaskService auditUnionTaskService = 
                		ContainerFactory.getContainInfo().getComponent(IAuditUnionTaskService.class);
                List<AuditUnionTask> tasklist = auditUnionTaskService.findList(sql, first,pageSize, new Object[]{});
                dataJson.put("tasklist", tasklist);
                int count = auditUnionTaskService.countAuditUnionTask(sql.replace("select * from", "select count(1) from"),new Object[]{});
                dataJson.put("total", count);
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", dataJson);
            }
        }
        catch (Exception e) {
            log.info("【异地通办事项结束调用getTaskList异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", params);
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params);
    }

    /**
     * 
     *  获取事项详情
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
    public String getTaskDetail(@RequestBody String params) {
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String taskid = param.getString("taskid");
                String is_needall = param.getString("is_needall");
                JSONObject rtnjson = new JSONObject();
                AuditTask task = auditTask.getUseTaskAndExtByTaskid(taskid).getResult();
                if(ZwfwConstant.CONSTANT_STR_ONE.equals(is_needall)) {
	                if(task!=null) {
	                	rtnjson.put("taskguid", task.getRowguid());
	                	rtnjson.put("audittask", task);
	                	SqlConditionUtil sql = new SqlConditionUtil();
	                	sql.eq("TASKGUID", task.getRowguid());
	                	//直接查所有材料，不分页
	                	PageData<AuditTaskMaterial> pageData = auditTaskMaterial
	                			.getAuditTaskMaterialPageData(sql.getMap(), 0, 100, "ORDERNUM", "desc").getResult();
	                	if(pageData!=null && pageData.getRowCount()>0) {
	                		rtnjson.put("materiallist", pageData.getList());
	                	}
	                }
                }else {
                	if(task!=null) {
	                	rtnjson.put("taskguid", task.getRowguid());
	                	rtnjson.put("taskid", task.getTask_id());
	                	rtnjson.put("taskname", task.getTaskname());
	                }
                }
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", rtnjson);
            }
        }
        catch (Exception e) {
            log.info("【获取事项详情getTaskDetail异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", params);
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params);
    }
    
    
    /**
     * 
     *  获取事项详情
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getMaterialDetail", method = RequestMethod.POST)
    public String getMaterialDetail(@RequestBody String params) {
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String projectguid = param.getString("projectguid");
                String taskmaterialguid = param.getString("taskmaterialguid");
                JSONObject rtnjson = new JSONObject();
                if(StringUtil.isNotBlank(projectguid)) {
                	IAuditUnionProjectMaterialService auditProjectMaterialService = ContainerFactory.getContainInfo()
                            .getComponent(IAuditUnionProjectMaterialService.class);
                	AuditUnionProjectMaterial AuditUnionProjectMaterial = auditProjectMaterialService.getProjectMaterialDetail(taskmaterialguid, projectguid);
                	if(AuditUnionProjectMaterial !=null) {
                		rtnjson.put("material", AuditUnionProjectMaterial);
                		List<JSONObject> filearray = new ArrayList<JSONObject>();
                		IAttachService attachService = ContainerFactory.getContainInfo()
                                .getComponent(IAttachService.class);
                		List<FrameAttachInfo> attachinfolist = attachService.getAttachInfoListByGuid(AuditUnionProjectMaterial.getCliengguid());
                		if(attachinfolist != null && attachinfolist.size()>0) {
                			for (FrameAttachInfo frameAttachInfo : attachinfolist) {
                				JSONObject jsonattach = new JSONObject();
                				jsonattach.put("filename", frameAttachInfo.getAttachFileName());
                				jsonattach.put("length", frameAttachInfo.getAttachLength());
                				jsonattach.put("attachguid", frameAttachInfo.getAttachGuid());
                				filearray.add(jsonattach);
							}
                			rtnjson.put("filelist", filearray);
                		}
                	}
                }
                return JsonUtils.zwdtRestReturn("1", "接口调用成功", rtnjson);
            }
        }
        catch (Exception e) {
            log.info("【获取材料详情getMaterialDetail异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口调用报错", params);
        }
        return JsonUtils.zwdtRestReturn("0", "接口调用失败", "params：" + params);
    }
    
    
    /**
     * 
     *  初始化办件
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/initUnionProject", method = RequestMethod.POST)
    public String initUnionProject(@RequestBody String params) {
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            JSONObject dataJson = new JSONObject();
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                JSONObject project = param.getJSONObject("project");
                String rowguid = project.getString("rowguid");
                String taskguid = project.getString("taskguid");
                String projectname = project.getString("projectname");
                String task_id = project.getString("task_id");
                String applyername = project.getString("applyername");
                String applyertype = project.getString("applyertype");
                String certnum = project.getString("certnum");
                String certtype = project.getString("certtype");
                String address = project.getString("address");
                String contactphone = project.getString("contactphone");
                String remark = project.getString("remark");
                int status = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
                Date applydate = new Date();
                AuditUnionProject unionproject = new AuditUnionProject();
                unionproject.setIs_submit(ZwfwConstant.CONSTANT_STR_ONE);
                unionproject.setOperatedate(applydate);
                unionproject.setOperateusername(project.getString("operateusername"));
                unionproject.setRowguid(rowguid);
                unionproject.setTask_id(task_id);
                unionproject.setProjectname(projectname);
                unionproject.setTaskguid(taskguid);
                unionproject.setApplyername(applyername);
                unionproject.setApplyertype(applyertype);
                unionproject.setApplydate(applydate);
                unionproject.setCertnum(certnum);
                unionproject.setCerttype(certtype);
                unionproject.setAddress(address);
                unionproject.setContactphone(contactphone);
                unionproject.setStatus(status);
                unionproject.setRemark(remark);
                long starttime = System.currentTimeMillis();
                IAuditUnionProjectService auditUnionProjectService = 
                		ContainerFactory.getContainInfo().getComponent(IAuditUnionProjectService.class);
                if(auditUnionProjectService.find(rowguid)!=null) {
                	auditUnionProjectService.update(unionproject);
                }else{
                	auditUnionProjectService.insert(unionproject);
                };
                long endtime = System.currentTimeMillis();
                log.info("【异地通办接口调用耗时】"+(endtime-starttime));
                dataJson.put("rowguid", rowguid);
                doMessges(unionproject);
                return JsonUtils.zwdtRestReturn("1", "办件初始化成功", dataJson);
            }
        }
        catch (Exception e) {
            log.info("【初始化办件initUnionProject异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "初始化办件失败！", params);
        }
        return JsonUtils.zwdtRestReturn("0", "初始化办件失败！", "params：" + params);
    }
    
    public void doMessges(AuditUnionProject unionProject){
    	 // 如果存在待办事宜，则删除待办
        IOnlineMessageInfoService iOnlineMessageInfoService = 
        		ContainerFactory.getContainInfo().getComponent(IOnlineMessageInfoService.class);
        IAuditUnionTaskUserService auditUnionTaskUserService = 
        		ContainerFactory.getContainInfo().getComponent(IAuditUnionTaskUserService.class);
        String title = "【来自于"+unionProject.getRemark()+"的异地通办待预审】" + unionProject.getProjectname() + "(" + unionProject.getApplyername() + ")";
        List<Record> userlist = auditUnionTaskUserService.getUserBytaskid(unionProject.getTask_id());
        for (Record record : userlist) {
			String userguid = record.getStr("userguid");
        	iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
        			UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
        			userguid, "", userguid, title, new Date());
		}
    }
    
    /**
     * 
     *  更新办件
     *  @param params
     * @return 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    public String updateProject(@RequestBody String params) {
        try {
            JSONObject jsonparams = JSON.parseObject(params);
            if (jsonparams != null) {
                JSONObject param = jsonparams.getJSONObject("params");
                String projectguid = param.getString("projectguid");
                //is_submit 0:异地提交办件时修改了信息（暂时不用） 1；异地审核流程更新办件
                String is_submit = param.getString("is_submit");
                IAuditUnionProjectService auditUnionProjectService = 
                		ContainerFactory.getContainInfo().getComponent(IAuditUnionProjectService.class);
                AuditUnionProject project = auditUnionProjectService.find(projectguid);
                int status = param.getInteger("status");
                if(ZwfwConstant.CONSTANT_STR_ZERO.equals(is_submit)) {
                	String projectname = param.getString("projectname");
                	String applyername = param.getString("applyername");
                	String applyertype = param.getString("applyertype");
                	String flowsn = param.getString("flowsn");
                	String certnum = param.getString("certnum");
                	String certtype = param.getString("certtype");
                	String address = param.getString("address");
                	String contactphone = param.getString("contactphone");
                	project.setProjectname(projectname);
                	project.setApplyername(applyername);
                	project.setApplyertype(applyertype);
                	project.setApplydate(new Date());
                	project.setFlowsn(flowsn);
                	project.setCertnum(certnum);
                	project.setCerttype(certtype);
                	project.setAddress(address);
                	project.setContactphone(contactphone);
                }else {
                	String operateusername = param.getString("operateusername");
                	Date date = new Date();
                	project.setOperatedate(date);
                	project.setOperateusername(operateusername);
                	if(status >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                		project.setBanjiedate(date);
                	}
                	project.setReason(StringUtil.getNotNullString(param.getString("reason")));
                }
                project.setStatus(status);
                int rtn = auditUnionProjectService.update(project);
                if(rtn > 0) {
                	return JsonUtils.zwdtRestReturn("1", "办件更新成功", "");
                }else {
                	return JsonUtils.zwdtRestReturn("0", "修改办件状态失败！", params);
                }
            }
        }
        catch (Exception e) {
            log.info("【修改办件状态updateProject异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "修改办件状态失败！", params);
        }
        return JsonUtils.zwdtRestReturn("0", "初始化办件失败！", "params：" + params);
    }

}
