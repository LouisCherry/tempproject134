package com.epoint.union.auditunionproject.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;

/**
 * 异地通办办件信息表list页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@RestController("auditunionprojectlistaction")
@Scope("request")
public class AuditUnionProjectListAction extends BaseController {
	@Autowired
	private IAuditUnionProjectService service;

	/**
	 * 异地通办办件信息表实体对象
	 */
	private AuditUnionProject dataBean;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditUnionProject> model;

	/**
	 * 是否启用下拉列表model
	 */
	private List<SelectItem> statusModel = null;

	private String projectstatus;

	private String flowsn;

	private String is_submit;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;

	@Autowired
	private IAuditTask auditTaskService;

	@Autowired
	private IAuditTaskExtension auditTaskExtensionService;

	// 组合服务
	@Autowired
	private IHandleProject handleProjectService;
	
	@Autowired
	private IAuditProject projectService;

	/**
	 * 事项扩展表实体对象
	 */
	private AuditTaskExtension auditTaskExtension = null;

	public void pageLoad() {
		setIs_submit(getRequestParameter("status"));
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
		addCallbackParam("msg", "成功删除！");
	}

	public DataGridModel<AuditUnionProject> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditUnionProject>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public List<AuditUnionProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					List<Object> conditionList = new ArrayList<Object>();
					String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
							conditionList);
					if (StringUtil.isNotBlank(projectstatus)) {
						conditionSql += " and STATUS = " + Integer.parseInt(projectstatus) + " ";
					}
					if (StringUtil.isNotBlank(flowsn)) {
						conditionSql += " and flowsn ='" + flowsn + "' ";
					}
					if (StringUtil.isNotBlank(is_submit)) {
						conditionSql += " and is_submit ='" + is_submit + "' ";
					}
					List<AuditUnionProject> list = service.findList(
							ListGenerator.generateSql("audit_union_project", conditionSql, sortField, sortOrder), first,
							pageSize, conditionList.toArray());
					int count = service.countAuditUnionProject(
							ListGenerator.generateSql("audit_union_project", conditionSql), conditionList.toArray());
					this.setRowCount(count);
					return list;
				}

			};
		}
		return model;
	}

	public void initProjectLocal() {
		String projectguid = getRequestParameter("projectguid");
		String cardno = getRequestParameter("cardno");
		String qno = getRequestParameter("qno");
		String taskid = this.getRequestParameter("taskid");
		String delegatetype = this.getRequestParameter("delegatetype");
		AuditTask audittask = auditTaskService.getUseTaskAndExtByTaskid(taskid).getResult();
		if (audittask != null) {
			// 事项是否启用
			if (ZwfwConstant.CONSTANT_INT_ZERO == audittask.getIs_enable()) {
				addCallbackParam("isenable", audittask.getIs_enable());
				return;
			}
			String acceptareacode = ZwfwUserSession.getInstance().getAreaCode();
			auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(audittask.getRowguid(), false)
					.getResult();
			String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl()) ? auditTaskExtension.getCustomformurl()
					: ZwfwConstant.CONSTANT_FORM_URL;
			String taskguid = audittask.getRowguid();
			String cityLevel = ZwfwUserSession.getInstance().getCitylevel();
			String fieldstr = " rowguid,flowsn,status ";
			AuditProject project = projectService.getAuditProjectByRowGuid(fieldstr, projectguid, acceptareacode).getResult();
			if(project == null) {
				projectguid = service.InitProject(taskguid, projectguid, userSession.getDisplayName(),
						userSession.getUserGuid(), ZwfwUserSession.getInstance().getWindowGuid(),
						ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getCenterGuid(),
						cardno, qno, acceptareacode, cityLevel, delegatetype).getResult();
			}
			String url = "";
			if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(audittask.getType()))
					&& !ZwfwConstant.JBJMODE_STANDARD.equals(audittask.getJbjmode())) {
				url = formUrl + "?taskguid=" + taskguid + "&projectguid=" + projectguid + "&taskid=";
			} else {
				url = formUrl + "?processguid=" + audittask.getProcessguid() + "&taskguid=" + taskguid + "&projectguid="
						+ projectguid + "&taskid=";
			}
			addCallbackParam("url", url);
		}
	}

	public AuditUnionProject getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditUnionProject();
		}
		return dataBean;
	}

	public void setDataBean(AuditUnionProject dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("applydate,applyername,applyertype,certnum,status,flowsn,projectname",
					"申请时间,申请人姓名,申请人类型,申请人证件编号,办件状态,办件编号,办件名称");
		}
		return exportModel;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getStatusModel() {
		if (statusModel == null) {
			statusModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
			if (statusModel != null) {
				Iterator<SelectItem> it = statusModel.iterator();
				if (it.hasNext()) {
					it.next();
				}
				while (it.hasNext()) {
					SelectItem item = it.next();
					// 删除列表中不需要的列表选项
					if ((Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_DJJ)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWINIT)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWWTJ)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_INIT)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_YDJ)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTU)
							|| (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTG)) {
						it.remove();
					}
				}
			}
		}
		return this.statusModel;
	}

	public String getProjectstatus() {
		return projectstatus;
	}

	public void setProjectstatus(String projectstatus) {
		this.projectstatus = projectstatus;
	}

	public String getFlowsn() {
		return flowsn;
	}

	public void setFlowsn(String flowsn) {
		this.flowsn = flowsn;
	}

	public String getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(String is_submit) {
		this.is_submit = is_submit;
	}

}
