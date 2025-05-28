package com.epoint.union.audituniontask.action;

import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.audituniontask.api.entity.AuditUnionTask;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.union.audituniontask.api.IAuditUnionTaskService;

/**
 * 异地通办事项配置表list页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 16:59:46]
 */
@RestController("audituniontasklistaction")
@Scope("request")
public class AuditUnionTaskListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditUnionTaskService service;
	
	@Autowired
	private IConfigService configService;

	/**
	 * 异地通办事项配置表实体对象
	 */
	private AuditUnionTask dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditUnionTask> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;
	
	private String status= "";

	public void pageLoad() {
		status  = getRequestParameter("status");
	}

	/**
	 * 删除选定
	 * 
	 */
	public void deleteSelect() {
		List<String> select = getDataGridData().getSelectKeys();
		for (String sel : select) {
			service.deleteByGuid(sel);
		}
		addCallbackParam("msg", "刷新成功！");
	}

	public DataGridModel<AuditUnionTask> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditUnionTask>() {
				private static final long serialVersionUID = 1L;
				@Override
				public List<AuditUnionTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					String url = configService.getFrameConfigValue("AS_UNION_POJRCTURL");
					List<AuditUnionTask> list1 = new ArrayList<AuditUnionTask>();
					int total = 0;
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					if(ZwfwConstant.CONSTANT_STR_ONE.equals(status) && StringUtil.isNotBlank(url)) {
						int i = 0;
						JSONObject params = new JSONObject();
						for (Object condition : conditionList) {
							String condstr = condition.toString();
							if(!"%%".equals(condstr)) {
								switch (i) {
								case 0: params.put("itemid", condstr.replace("%", "")); break;
								case 1: params.put("taskname", condstr.replace("%", "")); break;
								case 2: params.put("ouname", condstr.replace("%", "")); break;
								case 3: params.put("areacode", condstr.replace("%", "")); break;
								default: break;
								}
							}
							i++;
						}
						params.put("first", first);
						params.put("pageSize", pageSize);
						JSONObject param = new JSONObject();
						param.put("params", params);
						String rtn = HttpUtil.doPostJson(url+"rest/unionProject/getTaskList", param.toJSONString());
						JSONObject rtnjson = JSON.parseObject(rtn);
						if(rtnjson!=null) {
							JSONObject custom = rtnjson.getJSONObject("custom");
							if(custom != null && custom.getIntValue("code")==1) {
								JSONArray taskarray = custom.getJSONArray("tasklist");
								for (Object object : taskarray) {
									AuditUnionTask uniontask = new AuditUnionTask();
									JSONObject json = (JSONObject) object;
									uniontask.setRowguid(json.getString("rowguid"));
									uniontask.setTaskname(json.getString("taskname"));
									uniontask.setTask_id(json.getString("task_id"));
									uniontask.setItem_id(json.getString("item_id"));
									uniontask.setOuguid(json.getString("ouguid"));
									uniontask.setOuname(json.getString("ouname"));
									uniontask.setAreacode(json.getString("areacode"));
									uniontask.setLink_tel(json.getString("link_tel"));
									list1.add(uniontask);
								}
								total = custom.getIntValue("total");
							}
						}
					}else {
						conditionSql += " AND IFNULL(IS_HISTORY,0)= 0 and IS_EDITAFTERIMPORT=1 and is_enable = 1 and is_union='1' ";
						List<AuditTask> list = service.findUnionTaskList(
								ListGenerator.generateSql("audit_task", conditionSql, sortField, sortOrder), first,
								pageSize, conditionList.toArray());
						int count = service.countAuditUnionTask(ListGenerator.generateSql("audit_task", conditionSql),
								conditionList.toArray());
						total = count;
						for (AuditTask auditTask : list) {
							AuditUnionTask uniontask = service.find(auditTask.getTask_id());
							if(uniontask != null) {
								list1.add(uniontask);
							}else {
								uniontask = new AuditUnionTask();
								uniontask.setRowguid(auditTask.getTask_id());
								uniontask.setTaskname(auditTask.getTaskname());
								uniontask.setTask_id(auditTask.getTask_id());
								uniontask.setItem_id(auditTask.getItem_id());
								uniontask.setOuguid(auditTask.getOuguid());
								uniontask.setOuname(auditTask.getOuname());
								uniontask.setAreacode(auditTask.getAreacode());
								uniontask.setLink_tel(auditTask.getLink_tel());
								service.insert(uniontask);
								list1.add(uniontask);
							}
						}
					}
					this.setRowCount(total);
					return list1;
				}
			};
		}
		return model;
	}

	public AuditUnionTask getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditUnionTask();
		}
		return dataBean;
	}

	public void setDataBean(AuditUnionTask dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("item_id,ouname,taskname", "事项编码,部门名称,事项名称");
		}
		return exportModel;
	}

}
