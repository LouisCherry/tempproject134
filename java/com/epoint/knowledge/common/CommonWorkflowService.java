/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.bizlogic.orga.ou.service.FrameOuService9;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.workflow.bizlogic.api.WFAPI9;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityOperationService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowContextService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessVersionService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowPvMaterialService9;
import com.epoint.workflow.bizlogic.service.execute.WorkflowWorkItemService9;
import com.epoint.workflow.service.common.custom.ActivityInstance;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.config.WorkflowPvMaterial;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.common.runtime.WorkflowParameterMaterial9;
import com.epoint.workflow.service.common.runtime.WorkflowResult9;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

public class CommonWorkflowService {
	private static transient Logger log = Logger.getLogger(CommonWorkflowService.class);
	private WorkflowProcessVersionService9 processVersionService;
	private WorkflowPvMaterialService9 pvMaterialService;
	private WorkflowContextService9 contextService;
	private FrameUserService9 frameUserService;
	private FrameRoleService9 frameRoleService;
	private WorkflowActivityOperationService9 activityOperationService;
	private CnsUserService cnsUserService;
	private WFAPI9 wfapi;
	private FrameOuService9 frameOuService;
	private ICommonWorkflow cnsWorkflowImpl;
	private WorkflowWorkItemService9 workflowWorkItemService = null;
	private WorkflowActivityService9 activityService;
	@Autowired
    private IWFInstanceAPI9 instanceapi;
	@Autowired
    private IWFDefineAPI9 defineapi;
	@Autowired
    private IWFInitPageAPI9 initapi;
	
	
	private IWFEngineAPI9 wfeapi =
	        ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class); ;
	
	
	public CommonWorkflowService() {
	    
		this.processVersionService = new WorkflowProcessVersionService9();
		this.pvMaterialService = new WorkflowPvMaterialService9();
		this.contextService = new WorkflowContextService9();
		this.frameUserService = new FrameUserService9();
		this.frameRoleService = new FrameRoleService9();
		this.activityOperationService = new WorkflowActivityOperationService9();
		this.wfapi = new WFAPI9();
		this.frameOuService = new FrameOuService9();
		this.cnsWorkflowImpl = new CommonWorkflowImpl();
		this.workflowWorkItemService = new WorkflowWorkItemService9();
		this.cnsUserService = new CnsUserService();
		this.activityService = new WorkflowActivityService9();
	}

	public CommonWorkflowService(ICommonDao commonDao) {
		this.processVersionService = new WorkflowProcessVersionService9(commonDao);
		this.pvMaterialService = new WorkflowPvMaterialService9(commonDao);
		this.contextService = new WorkflowContextService9(commonDao);
		this.frameUserService = new FrameUserService9(commonDao);
		this.frameRoleService = new FrameRoleService9(commonDao);
		this.activityOperationService = new WorkflowActivityOperationService9(commonDao);
		this.wfapi = new WFAPI9(commonDao);
		this.frameOuService = new FrameOuService9(commonDao);
		this.cnsWorkflowImpl = new CommonWorkflowImpl(commonDao);
		this.workflowWorkItemService = new WorkflowWorkItemService9(commonDao);
		this.cnsUserService = new CnsUserService();
		this.activityService = new WorkflowActivityService9(commonDao);
	}

	public WorkflowWorkItem startWorkflow(String rowguid, String sqlTableName, String processGuid, String userGuid) {
        wfapi.getDao().beginTransaction();
        //返回第一个工作项
        WorkflowWorkItem workflowWorkItem = null;
        //获取当前激活的pvguid
        String processversionGuid = processVersionService.getPv(processGuid).getProcessVersionGuid();
        //获取该流程版本下面的材料
        List<WorkflowPvMaterial> pvmateriallist = pvMaterialService.selectAllByProcessVersionGuid(processversionGuid);
        List<WorkflowParameterMaterial9> parameterMaterialList = null;
        //转化为材料入参对象list
        if (pvmateriallist != null && pvmateriallist.size() > 0) {
            parameterMaterialList = new ArrayList<WorkflowParameterMaterial9>();
            for (WorkflowPvMaterial pvmaterial : pvmateriallist) {
                WorkflowParameterMaterial9 parameterMaterial = new WorkflowParameterMaterial9();
                parameterMaterial.setMaterialGuid(pvmaterial.getMaterialGuid());
                parameterMaterial.setMaterialName(pvmaterial.getMaterialName());
                parameterMaterial.setMaterialType(WorkflowKeyNames9.MaterialType_Form);
                parameterMaterialList.add(parameterMaterial);
            }
        }
        //启动流程
        
        
        WorkflowParameter9 param = new WorkflowParameter9(); 
        param.setProcessGuid(processGuid); 
        param.setSendGuid(userGuid); 
        param.setOperateType(WorkflowKeyNames9.OperationType_Start); 
        param.setProcessVersionGuid(processversionGuid);      
        //system.out.println(param.ConvertToJson());
        WorkflowResult9 workFlowResult = wfeapi.operate(param.ConvertToJson());
        if (workFlowResult != null) { 
            List<ActivityInstance> list = workFlowResult.getChildContext().get(0).getNextActIstanceList();
            if(list!=null && list.size()>0){           
                    List<WorkflowWorkItem> wlist = list.get(0).getWorkItemList();
                    if(wlist!=null&&wlist.size()>0){
                        workflowWorkItem = wlist.get(0);
                        ProcessVersionInstance pvi  = wfapi.getWFInstanceAPI().
                                getProcessVersionInstance(wlist.get(0).getProcessVersionInstanceGuid());
                        
                        wfapi.getWFInstanceAPI().createOrUpdateContext(pvi, sqlTableName, rowguid, 2);
                    }
              
            }         

        }
        //提交事物，不然送一下读取不到数据
        wfapi.getDao().commitTransaction();
        return workflowWorkItem;
    }
	public WorkflowWorkItem getItem(String pviguid,String userGuid,int i){
	    ProcessVersionInstance pvi  = wfapi.getWFInstanceAPI().getProcessVersionInstance(pviguid);
	    List<WorkflowWorkItem> wlist = wfapi.getWFInstanceAPI().getWorkItemListByUserGuid(pvi, userGuid);
	    return wlist.get(i);
	            
	}
	public WorkflowResult9 handleOperateByRole(String workitemguid, String operationguid, String userguid,
			String opinion, String... roleNames) {
		try {
			this.wfapi.getDao().beginTransaction();
			JSONObject e = new JSONObject();
			e.put("workitemguid", workitemguid);
			e.put("operationguid", operationguid);
			e.put("userguid", userguid);
			e.put("opinion", opinion);
			JSONArray nextsteplist = new JSONArray();
			JSONObject nextStep = new JSONObject();
			if (roleNames != null && roleNames.length > 0) {
				JSONArray workFlowResult = new JSONArray();
				String[] arg9 = roleNames;
				int arg10 = roleNames.length;

				for (int arg11 = 0; arg11 < arg10; ++arg11) {
					String roleName = arg9[arg11];
					List frameUserList = this.getUserByRoleName(roleName);
					if (frameUserList != null && frameUserList.size() > 0) {
						Iterator arg14 = frameUserList.iterator();

						while (arg14.hasNext()) {
							FrameUser frameUser = (FrameUser) arg14.next();
							JSONObject handler = new JSONObject();
							handler.put("ouguid", frameUser.getOuGuid());
							handler.put("handlerguid", frameUser.getUserGuid());
							workFlowResult.add(handler);
						}
					}
				}

				nextStep.put("stepguid", "");
				nextStep.put("handlerlist", workFlowResult);
				nextsteplist.add(nextStep);
			}

			e.put("nextsteplist", nextsteplist);
			WorkflowResult9 arg18 = this.wfapi.getWFEngineAPI().operate(e.toJSONString());
			this.wfapi.getDao().commitTransaction();
			return arg18;
		} catch (JSONException arg17) {
			arg17.printStackTrace();
			return null;
		}
	}

//	public WorkflowResult9 handleOperate(String workitemguid, String operationguid, String userguid, String opinion,
//			List<FrameUser> frameUserList) {
//		try {
//			this.wfapi.getDao().beginTransaction();
//			
//			JSONObject e = new JSONObject();
//			
//			
//			e.put("workitemguid", workitemguid);
//			
//			e.put("operationguid", operationguid);
//			e.put("userguid", userguid);
//			e.put("opinion", opinion);
//			JSONArray nextsteplist = new JSONArray();
//			JSONObject nextStep = new JSONObject();
//			if (frameUserList != null && frameUserList.size() > 0) {
//				JSONArray workFlowResult = new JSONArray();
//				Iterator arg9 = frameUserList.iterator();
//
//				while (arg9.hasNext()) {
//					FrameUser frameUser = (FrameUser) arg9.next();
//					JSONObject handler = new JSONObject();
//					handler.put("ouguid", frameUser.getOuGuid());
//					handler.put("handlerguid", frameUser.getUserGuid());
//					workFlowResult.add(handler);
//				}
//
//				nextStep.put("stepguid", "");
//				nextStep.put("handlerlist", workFlowResult);
//				nextsteplist.add(nextStep);
//				e.put("nextsteplist", nextsteplist);
//			}
//
//			WorkflowResult9 workFlowResult1 = wfeapi.operate(e.toJSONString());
//			this.wfapi.getDao().commitTransaction();
//			return workFlowResult1;
//			//return null;
//		} catch (JSONException arg12) {
//			arg12.printStackTrace();
//			return null;
//		}
//	}
	public WorkflowResult9 handleOperate1(WorkflowWorkItem item, String operationguid, String userguid, String opinion,
            List<FrameUser> frameUserList) {
        try {
            this.wfapi.getDao().beginTransaction();
            JSONObject e = new JSONObject();
            e.put("workitemguid", item.getWorkItemGuid());
            e.put("pviguid", item.getProcessVersionInstanceGuid());
            e.put("processversionGuid", item.getProcessVersionGuid());
            e.put("processguid", item.getProcessGuid());
            e.put("operationguid", operationguid);
            e.put("userguid", userguid);
            e.put("opinion", opinion);
            WorkflowParameter9 param = new WorkflowParameter9(); 
            param.setProcessGuid(item.getProcessGuid()); 
            param.setSendGuid(userguid); 
            param.setProcessVersionGuid(item.getProcessVersionGuid());
            param.setProcessVersionInstanceGuid(item.getProcessVersionInstanceGuid());
            param.setWorkItemGuid(item.getWorkItemGuid());
            param.setOpinion(opinion);
            param.setOperationGuid(operationguid);

            JSONArray nextsteplist = new JSONArray();
            JSONObject nextStep = new JSONObject();
            if (frameUserList != null && frameUserList.size() > 0) {
                JSONArray workFlowResult = new JSONArray();
                Iterator arg9 = frameUserList.iterator();
                while (arg9.hasNext()) {
                    FrameUser frameUser = (FrameUser) arg9.next();
                    JSONObject handler = new JSONObject();
                    handler.put("ouguid", frameUser.getOuGuid());
                    handler.put("handlerguid", frameUser.getUserGuid());
                    workFlowResult.add(handler);
                }

                nextStep.put("stepguid", "");
                nextStep.put("handlerlist", workFlowResult);
                nextsteplist.add(nextStep);
                e.put("nextsteplist", nextsteplist);
                
            }
            e.put("nextsteplist", nextsteplist);
            
            WorkflowResult9 workFlowResult1 = wfeapi.operate(e.toJSONString());
            //WorkflowResult9 workFlowResult1 = (new WFEngineAPI()).operate(e.toJSONString());
            this.wfapi.getDao().commitTransaction();
            return workFlowResult1;
            //return null;
        } catch (JSONException arg12) {
            arg12.printStackTrace();
            return null;
        }
    }

	public WorkflowResult9 handleOperateByOuAndRole(WorkflowWorkItem workitem, String operationguid, String userguid,
			String opinion, String ouGuid, String roleName) {
		WorkflowResult9 workFlowResult = null;
		List frameUserList = this.cnsUserService.getUserListByOu(ouGuid, roleName, 1);
		if (frameUserList != null && frameUserList.size() > 0) {
			workFlowResult = this.handleOperate1(workitem, operationguid, userguid, opinion, frameUserList);
		} else {
			FrameOu frameOu = this.frameOuService.getOuByOuGuid(ouGuid);
			if (frameOu != null) {
				log.info("------------" + frameOu.getOuname() + "下没有处理人！-----------");
			} else {
				log.info("------------没有获取到部门，该部门guid为" + ouGuid + "-----------");
			}
		}

		return workFlowResult;
	}

	public String getOperationGuidByActivityGuid(String activityGuid, String operationNote) {
        String operationGuid = "";
        List<WorkflowActivityOperation> operationList = activityOperationService.selectByActivityGuid(activityGuid);
        if (operationList != null && operationList.size() > 0) {
            for (WorkflowActivityOperation workflowActivityOperation : operationList) {
                if (operationNote.equals(workflowActivityOperation.getNote())) {
                    operationGuid = workflowActivityOperation.getOperationGuid();
                    break;
                }
            }
        }
        return operationGuid;
    }

	public String getOperationGuidByWorkflowitem(WorkflowWorkItem workflowWorkItem, String operationNote) {
		return this.getOperationGuidByActivityGuid(workflowWorkItem.getActivityGuid(), operationNote);
	}

	public List<FrameUser> getUserByRoleName(String roleName) {
		List frameUserList = null;
		if (StringUtil.isNotBlank(roleName)) {
			String roleGuid = this.frameRoleService.getRoleGuidByRoleName(roleName);
			if (StringUtil.isNotBlank(roleGuid)) {
				frameUserList = this.frameUserService.listUserByOuGuid("", roleGuid, "", (String) null, false, false,
						false, 3);
			}
		}

		return frameUserList;
	}

//	public void finishWorkflow(String workitemguid, String operationguid, String userguid, String opinion) {
//		this.handleOperate(workitemguid, operationguid, userguid, opinion, (List) null);
//	}

//	public WorkflowTransitionInstance getSourceActivity(String workitemGuid) {
//		WorkflowWorkItem workflowWorkItem = this.wfapi.getWFWorkItemAPI().getWorkflowWorkItem(workitemGuid);
//		String activityInstanceGuid = workflowWorkItem.getActivityInstanceGuid();
//		WorkflowTransitionInstance transitionInstance = this.cnsWorkflowImpl
//				.getTansitionBySrcActIns(activityInstanceGuid);
//		return transitionInstance;
//	}

//	public WorkflowActivity getSrcActivityByWorkitemGuid(String workitemGuid) {
//		WorkflowActivity workflowActivity = null;
//		WorkflowTransitionInstance transitionInstance = this.getSourceActivity(workitemGuid);
//		if (transitionInstance != null) {
//			workflowActivity = this.cnsWorkflowImpl.getActivityByGuid(transitionInstance.getSourceActiviryGuid());
//		}
//
//		return workflowActivity;
//	}

	public String generateSerialNum(String rqstsource) {
		return this.cnsWorkflowImpl.generateSerialNum(rqstsource);
	}
	//333
	public WorkflowWorkItem getActiveWorkitem(String pviguid, String userGuid) {
		WorkflowWorkItem workflowWorkItem = null;
		//List workflowWorkItemList = this.wfapi.getWFWorkItemAPI().getWorkItembyPviGuid(pviguid, userGuid);
		//system.out.println(pviguid+"ovi");
		if(pviguid!=null){
		    ProcessVersionInstance pvi = wfapi.getWFInstanceAPI().getProcessVersionInstance(pviguid);
	        
	        List workflowWorkItemList =this.wfapi.getWFInstanceAPI().getWorkItemListByUserGuid(pvi, userGuid);
	        if (workflowWorkItemList != null && workflowWorkItemList.size() > 0) {
	            workflowWorkItem = (WorkflowWorkItem) workflowWorkItemList.get(0);
	        }
		}
		
		//List workflowWorkItemList=null;
		

		return workflowWorkItem;
	}

	public String getActivityGuidByTransitionGuid(String transitionguid) {
		return this.cnsWorkflowImpl.getActivityGuidByTransitionGuid(transitionguid);
	}

//	public List<WorkflowWorkItem> getActiveWorkitemByPviguid(String pviguid) {
//		List workflowWorkItemList = this.workflowWorkItemService.selectByProcessVersionInstanceGuid(pviguid, 20);
//		return workflowWorkItemList;
//	}
	
	///333
	public WorkflowActivity getFirstActivityByProcessGuid(String processGuid) {
//	    String pvguid = (new WFAPI9()).getDefinitionAPI().getWFProcessVersionAPI()
//                .selectEnableProcessVersion(processGuid);
//	    
	    WorkflowProcessService9 p = new WorkflowProcessService9();
	    p.getByProcessGuid(processGuid);
	    WorkflowProcessVersionService9 i = new WorkflowProcessVersionService9();
	    String pvguid = i.getPv(processGuid).getProcessVersionGuid();
	    ////system.out.println(pvguid+"-------------------pviguid");
	    WorkflowActivityService9 t = new WorkflowActivityService9();
	    return t.getFirstActivity(pvguid);
	    //return new WFAPI9().getPoolService().getFirstActivity(pvguid);
        //return this.activityService.getFirstActivity(processGuid);
	    
	}

    
}