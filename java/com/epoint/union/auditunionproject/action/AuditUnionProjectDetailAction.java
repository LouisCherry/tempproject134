package com.epoint.union.auditunionproject.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异地通办办件信息表详情页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@RightRelation(AuditUnionProjectListAction.class)
@RestController("auditunionprojectdetailaction")
@Scope("request")
public class AuditUnionProjectDetailAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditUnionProjectService service;

	/**
	 * 异地通办办件信息表实体对象
	 */
	private AuditUnionProject dataBean = null;
	
	private AuditTask audittask = null;
	
    /**
     * 表格控件model
     */
    private DataGridModel<AuditUnionProjectMaterial> model;
	
	@Autowired
    private IAuditUnionProjectMaterialService materialservice;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new AuditUnionProject();
		}else {
			initTask(dataBean.getTask_id());
		}
	}
	
    public DataGridModel<AuditUnionProjectMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditUnionProjectMaterial>()
            {
				private static final long serialVersionUID = 1L;
				@Override
                public List<AuditUnionProjectMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	List<Object> conditionList = new ArrayList<Object>();
                	List<AuditUnionProjectMaterial> list = new ArrayList<AuditUnionProjectMaterial>();
                	int count =0;
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    if(dataBean!=null && StringUtil.isNotBlank(dataBean.getRowguid())) {
                    	conditionSql += " and unionprojectguid='"+dataBean.getRowguid()+"'";
                    	list = materialservice.findList(
                    			ListGenerator.generateSql("audit_union_project_material", conditionSql, sortField, sortOrder), first, pageSize,
                    			conditionList.toArray());
                    	count = materialservice.countAuditUnionProjectMaterial(ListGenerator.generateSql("audit_union_project_material", conditionSql), conditionList.toArray());
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }
	
    
    public void initTask(String taskid) {
		String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
		JSONObject params = new JSONObject();
		params.put("taskid", taskid);
		params.put("is_needall", 1);
		JSONObject param = new JSONObject();
		param.put("params", params);
		String rtn = HttpUtil.doPostJson(url+"rest/unionProject/getTaskDetail", param.toJSONString());
		JSONObject rtnjson = JSON.parseObject(rtn);
		if(rtnjson!=null) {
			JSONObject custom = rtnjson.getJSONObject("custom");
			if(custom != null && custom.getIntValue("code")==1) {
				JSONObject audittaskjson = custom.getJSONObject("audittask");
				if(audittaskjson != null) {
					if(audittask == null) {
						audittask = new AuditTask();
						audittask.setRowguid(audittaskjson.getString("rowguid"));
						audittask.setTaskname(audittaskjson.getString("taskname"));
						audittask.setTask_id(audittaskjson.getString("task_id"));
						audittask.setItem_id(audittaskjson.getString("item_id"));
						audittask.setType(audittaskjson.getInteger("type"));
						audittask.setOuname(audittaskjson.getString("ouname"));
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setPromise_day(audittaskjson.getInteger("promise_day"));
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
						audittask.setApplyertype(audittaskjson.getString("applyertype"));
						audittask.setAcceptcondition(audittaskjson.getString("acceptcondition"));
					}
					
				}
			}
		}
    }

	public AuditUnionProject getDataBean() {
		return dataBean;
	}
	
	public AuditTask getAudittask() {
		if (audittask == null) {
			audittask = new AuditTask();
		}
		return audittask;
	}

}