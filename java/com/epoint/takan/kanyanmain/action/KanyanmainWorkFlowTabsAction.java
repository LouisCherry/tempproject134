package com.epoint.takan.kanyanmain.action;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.epoint.workflow.bizlogic.cache.WorkflowDefineService9;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.dto.model.SelectItem;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.controller.execute.WorkflowBaseController;
import com.epoint.workflow.bizlogic.service.execute.WorkflowPviMaterialService9;
import com.epoint.workflow.service.common.entity.config.WorkflowPvMisTableSet;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowPviMaterial;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;

/**
 * 勘验主表工作流页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RestController("kanyanmainworkflowtabsaction")
@Scope("request")
public class KanyanmainWorkFlowTabsAction extends WorkflowBaseController
{
	private String workitemGuid;

    private String processVersionInstanceGuid;
    
    //流程配置api，兼容自定义流程
    private WorkflowDefineService9 defineService = null;
    
      /**
     *  获取流程版本实例操作API（注意：TCC主业务服务中不允许调用）
     */
    @Autowired
    private IWFInstanceAPI9 instanceapi;
    
    private WorkflowWorkItem item = null;
    private Kanyanmain dataBean = null;
    // 数据表名
    private String sqlTableName = "KANYANMAIN";
    private String rowguid;
    
    private String materialInstanceGuid;

    Map<String, String> map = new HashMap<String, String>();
    private String activityGuid;
    private String materialGuid;
    private int tableId;
     private ProcessVersionInstance pvi = null;
    
   	@Autowired
    private IKanyanmainService service;
   
    @Override
    public void pageLoad() {
		materialInstanceGuid = getRequestParameter("MaterialInstanceGuid");
        processVersionInstanceGuid = getRequestParameter("ProcessVersionInstanceGuid");
        workitemGuid = getRequestParameter("WorkItemGuid");
        pvi = instanceapi.getProcessVersionInstance(processVersionInstanceGuid);
        if (StringUtil.isNotBlank(workitemGuid)) {
            item = instanceapi.getWorkItem(pvi, workitemGuid);
            activityGuid = item.getActivityGuid();
        }
	//	WorkFlowParameter9 param = new WorkFlowParameter9();
    //  param.setProcessVersionInstanceGuid(processVersionInstanceGuid);
        defineService = new WorkflowDefineService9(pvi);
        
        WorkflowPviMaterial pvimaterial = new WorkflowPviMaterialService9().getDetail(pvi,materialInstanceGuid);
        materialGuid = pvimaterial.getMaterialGuid();

        //根据materialGuid查询对应tableid和materialGuid
        List<WorkflowPvMisTableSet> wpmList = defineService.getByMaterialGuid(materialGuid);
        if (wpmList != null && wpmList.size() > 0) {
            sqlTableName = wpmList.get(0).getSqlTableName();
            tableId = wpmList.get(0).getTableId();
        }
        rowguid = getRequestParameter("guid");
        // 初始化mis表
        if (StringUtil.isBlank(rowguid)) {
            rowguid = instanceapi.getContextItemValue(pvi, sqlTableName);
        }
        if (StringUtil.isNotBlank(rowguid)) {
            dataBean = service.find(rowguid);
        }
        else {
            dataBean = new Kanyanmain();
        }
        //        //需要设置字段权限时设置
//        JSONObject jsonobject = new JSONObject();
//        try {
//            jsonobject.put("activityGuid", activityGuid);
//            jsonobject.put("materialGuid", materialGuid);
//            jsonobject.put("tableId", tableId);
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
        if (save()) {
            instanceapi.tabmaterialSubmit(pvi, materialInstanceGuid, sqlTableName, rowguid, userSession.getUserGuid(),
                    true);
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
        instanceapi.tabmaterialSubmit(pvi, materialInstanceGuid, sqlTableName, rowguid, userSession.getUserGuid(),
                false);
        
        //如果满足业务逻辑返回true,msg为空
        addCallbackParam("msg", "");
        return true;
    }

    public void setDataBean(Kanyanmain dataBean) {
        this.dataBean = dataBean;
    }

    public Kanyanmain getDataBean() {
        return dataBean;
    }
    
    
}
