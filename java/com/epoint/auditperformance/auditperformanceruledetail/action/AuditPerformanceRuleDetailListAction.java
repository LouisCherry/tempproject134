package com.epoint.auditperformance.auditperformanceruledetail.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评细则表list页面对应的后台
 * 
 * @author Willy
 * @version [版本号, 2018-01-09 15:34:36]
 */
@RestController("auditperformanceruledetaillistaction")
@Scope("request")
public class AuditPerformanceRuleDetailListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceRuleDetail service;

	/**
	 * 考评细则表实体对象
	 */
	private AuditPerformanceRuleDetail dataBean;
	
	/**
	 * 考评规则唯一标志
	 */
	private String ruleGuid;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditPerformanceRuleDetail> model;

	/**
     * 考评方式下拉列表model
     */
    private List<SelectItem> typeModel = null;

    @Override
	public void pageLoad() {
		dataBean = new AuditPerformanceRuleDetail();
		ruleGuid = getRequestParameter("ruleguid");
	}

	/**
	 * 删除选定
	 * 
	 */
	public void deleteSelect() {
		List<String> select = getDataGridData().getSelectKeys();
		for (String sel : select) {
			service.deletePerformanceRuleDetailByGuid(sel);
		}
		addCallbackParam("msg", "成功删除！");
	}

	public DataGridModel<AuditPerformanceRuleDetail> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditPerformanceRuleDetail>() {

				@Override
				public List<AuditPerformanceRuleDetail> fetchData(int first, int pageSize, String sortField,
						String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					if (StringUtil.isNotBlank(dataBean.getDetailrulename())) {
						sql.like("detailrulename", dataBean.getDetailrulename());
					}
					if (StringUtil.isNotBlank(dataBean.getType())) {
						sql.eq("type", dataBean.getType());
					}
					sql.eq("rulerowguid", ruleGuid);
					sql.setOrderDesc("ordernum");
					sql.setOrderAsc("operatedate");
					PageData<AuditPerformanceRuleDetail> pageData = service
							.selectPerformanceRuleDetailByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
							.getResult();
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}

			};
		}
		return model;
	}

	public void save(){
		List<AuditPerformanceRuleDetail> performanceRuleDetails = this.getDataGridData().getWrappedData();
		for (AuditPerformanceRuleDetail performanceRuleDetail : performanceRuleDetails) {
			performanceRuleDetail.keep("rowguid", "ordernum");
			service.updatePerformanceRuleDetail(performanceRuleDetail);
		}
		addCallbackParam("msg", "保存成功！");
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
        	typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评方式", null, true));
        }
        return this.typeModel;
    }
	
	public AuditPerformanceRuleDetail getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditPerformanceRuleDetail();
		}
		return dataBean;
	}

	public void setDataBean(AuditPerformanceRuleDetail dataBean) {
		this.dataBean = dataBean;
	}

}
