package com.epoint.xmz.buildinfo.action;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import com.epoint.core.dto.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.basic.faces.util.DataUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.workflow.controller.execute.WorkflowBaseController;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.runtime.WorkflowResult9;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.workflow.service.tcc.api.IWFRMSAPI;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;

/**
 * 工改二阶段建筑表工作流页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@RestController("buildinfoworkflowaction")
@Scope("request")
public class BuildInfoWorkFlowAction extends WorkflowBaseController
{
	private WorkflowWorkItem item = null;

    /**
     * 获取流程版本实例操作API
     */
    @Autowired
    private IWFInstanceAPI9 instanceapi;

    /**
     * 获取流程流转页面信息API
     */
    @Autowired
    private IWFInitPageAPI9 initapi;

    /**
     * 获取工作流流程定义信息API
     */
    @Autowired
    private IWFDefineAPI9 defineapi;

    @Autowired
    IWFEngineAPI9 engineapi;
    
    private ProcessVersionInstance pvi = null;

    @Autowired
    private IWFRMSAPI rmsapi;
    
    private BuildInfo dataBean = null;
    // 数据表名
    private String SQLTableName = "build_info";
    
    private String rowguid;
    
  	@Autowired
    private IBuildInfoService service;
    
    

    @Override
    public void pageLoad() {
		String workitemGuid = getRequestParameter("WorkItemGuid");
        String processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        pvi = instanceapi.getProcessVersionInstance(processVersionInstanceGuid);
        if (StringUtil.isNotBlank(workitemGuid)) {
            item = instanceapi.getWorkItem(pvi, workitemGuid);
        }
        
        rowguid = getRequestParameter("guid");
        // 初始化mis表
        if (StringUtil.isBlank(rowguid)) {
         	rowguid = instanceapi.getContextItemValue(pvi, SQLTableName);
        }
        if (StringUtil.isNotBlank(rowguid)) {
            dataBean = service.find(rowguid);
        }
        else {
            dataBean = new BuildInfo();
        }
        
        //         //需要设置字段权限时设置
//        JSONObject jsonobject = new JSONObject();
//        try {
//            jsonobject.put("activityGuid", item.getActivityGuid());
//            jsonobject.put("issingleform", true);
//            jsonobject.put("processversioninstanceguid", processVersionInstanceGuid);
//            addCallbackParam("accessRight", initapi.init_getTablePropertyControl(jsonobject.toJSONString()));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void submit() {
        if(save()){
        	instanceapi.singlematerialSubmit(pvi, SQLTableName, rowguid, userSession.getUserGuid(), true);
    	}
    }

    /**
     * 保存并新建
     * 
     */
    public void saveForm() {
    	save();
    }
    
    private boolean save() {
    	//如果不满足业务逻辑返回false
    	//if (不符合条件) {
        //    addCallbackParam("msg", "提示错误");
        //    return false;
        //}
    	
        if (StringUtil.isBlank(rowguid)) {
            rowguid = UUID.randomUUID().toString();
            dataBean.setRowguid(rowguid);
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            service.insert(dataBean);
        }
        else
        {
            rowguid = dataBean.getRowguid();
            dataBean.setOperatedate(new Date());
            service.update(dataBean);
        }
        instanceapi.singlematerialSubmit(pvi, SQLTableName, rowguid, userSession.getUserGuid(), false);
        
        //如果满足业务逻辑返回true,msg为空
        addCallbackParam("msg", "");
        return true;
    }

    public void setDataBean(BuildInfo dataBean) {
        this.dataBean = dataBean;
    }

    public BuildInfo getDataBean() {
        return dataBean;
    }

    /**
     * 系统方法，勿删 获取按钮打开页面或者默认处理
     * 
     * @param operationGuid
     *            操作按钮guid
     * @param transitionGuid
     *            变迁guid
     * @return
     */
    public String getPageUrlOfOperate(String data) {
        //String returndata = EncodeUtil.decode(data);
        JSONObject jsonobject = JSONObject.parseObject(data);
        jsonobject.put("userguid", userSession.getUserGuid());
        jsonobject.put("ishandleendactivity", true);
        if (StringUtil.isNotBlank(getRequestParameter("batchHandleGuid"))) {
            String otherurlparam = "batchHandleGuid=" + getRequestParameter("batchHandleGuid");
            jsonobject.put("otherurlparam", otherurlparam);
        }
        String operationGuid = jsonobject.get("operationguid").toString();

        // 获取操作处理页面或者默认处理
        JSONObject returnobject = JSONObject.parseObject(initapi.init_getPageUrlOfOperate(jsonobject.toString()));
        WorkflowActivityOperation operation = (StringUtil.isNotBlank(operationGuid))
                ? defineapi.getActOperation(pvi, operationGuid) : new WorkflowActivityOperation();
        if (returnobject.containsKey("isdefoperation")) {
            boolean isdefault = ConvertUtil.convertStringToBoolean(returnobject.get("isdefoperation").toString());
            if (isdefault) {
                // 需要默认操作前确认提示返回returnobject.toString()即可
                jsonobject.put("operationname", operation.getOperationName());
                jsonobject.put("isdefoperation", isdefault);
                return jsonobject.toString();
                // 如果不需要提示直接处理
                // return getoperate(data);
            }
        }
        returnobject.put("operationtype", operation.getOperationType());
        return returnobject.toString();
    }

    /**
     * 系统方法，勿删 操作处理页面返回json后回调
     * 
     * @param data
     *            json数据
     * @return
     */
    public String getoperate(String data) {
        //data = EncodeUtil.decode(data);
        JSONObject jsonobject = JSONObject.parseObject(data);
        jsonobject.put("userguid", userSession.getUserGuid());
        jsonobject.put("displayname", userSession.getDisplayName());
        String json = jsonobject.toString();
        // 调用IWFRMSAPI.operateTCC包裹分布式事务，如果存在操作前后事件请个性化主业务服务IWFRMSAPI
        // String resultString = rmsapi.operateTCC(json);
        // WorkflowResult9 result = JsonUtil.jsonToObject(resultString,
        // WorkflowResult9.class);
        WorkflowResult9 result = engineapi.operate(json);
        // 非服务化部署，本地事务情况可以编写操作后事件
        // 服务化架构部署的此处不允许编写操作后事件
        if(StringUtil.isNotBlank(result.getCurrentActIstance().getActivity().getNote())){
            switch (result.getCurrentActIstance().getActivity().getNote()) // getActivityDispName
            {
                case "审核":
                    switch (result.getOperation().getNote()) // getOperationName()
                    {
                        case "送下一步":
                            //操作后事件 
                            break;
                    }
                    break;
            }
        }

        JSONObject returnjsonobject = new JSONObject();
        returnjsonobject.put("message", "");
        return returnjsonobject.toString();
    }
     
    
}
