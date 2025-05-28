package com.epoint.auditperformance.auditperformancerule.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评规则表list页面对应的后台
 * 
 * @version [版本号, 2018-01-09 10:05:48]
 */
@RestController("auditperformancerulelistaction")
@Scope("request")
public class AuditPerformanceRuleListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceRule service;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditPerformanceRule> model;

	/**
	 * 考评对象类别下拉列表model
	 */
	private List<SelectItem> objecttypeModel = null;

	private AuditPerformanceRule dataBean;

	@Override
	public void pageLoad() {
		dataBean = new AuditPerformanceRule();
		if(StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())){
			addCallbackParam("msg", "人员没有分配到中心!");
		}
	}

	/**
	 * 删除选定
	 * 
	 */
	public void deleteSelect() {
		List<String> select = getDataGridData().getSelectKeys();
		for (String sel : select) {
			service.deletePerformanceRuleByGuid(sel);
		}
		addCallbackParam("msg", "成功删除！");
	}

	public DataGridModel<AuditPerformanceRule> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditPerformanceRule>() {

				@Override
				public List<AuditPerformanceRule> fetchData(int first, int pageSize, String sortField,
						String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
					if (StringUtil.isNotBlank(dataBean.getRulename())) {
						sql.like("Rulename", dataBean.getRulename());
					}
					if (StringUtil.isNotBlank(dataBean.getObjecttype())) {
						sql.eq("Objecttype", dataBean.getObjecttype());
					}
					sql.setOrderDesc("ordernum");
					sql.setOrderDesc("operatedate");
					sql.setOrderDesc("rulename");
					PageData<AuditPerformanceRule> pageData = service
							.selectPerformanceRuleByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
							.getResult();
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}
			};
		}
		return model;
	}

	public void save(){
		List<AuditPerformanceRule> performanceRules = this.getDataGridData().getWrappedData();
		for (AuditPerformanceRule performanceRule : performanceRules) {
			performanceRule.keep("rowguid", "ordernum");
            service.updatePerformanceRule(performanceRule);
		}
		addCallbackParam("msg", "保存成功！");
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getObjecttypeModel() {
		if (objecttypeModel == null) {
			objecttypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, true));
		}
		return this.objecttypeModel;
	}

	public AuditPerformanceRule getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditPerformanceRule dataBean) {
		this.dataBean = dataBean;
	}
}
