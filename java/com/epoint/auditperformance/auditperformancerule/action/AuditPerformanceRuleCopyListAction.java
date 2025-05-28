package com.epoint.auditperformance.auditperformancerule.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 考评规则表list页面对应的后台
 * 
 * @version [版本号, 2018-01-09 10:05:48]
 */
@RestController("auditperformancerulecopylistaction")
@Scope("request")
public class AuditPerformanceRuleCopyListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceRule service;
	
	@Autowired
	private IAuditOrgaServiceCenter serviceCenter;
	
	@Autowired
	private IAuditPerformanceRuleDetail ruleDetailService;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditPerformanceRule> model;

	/**
	 * 考评对象类别下拉列表model
	 */
	private List<SelectItem> centerModel = null;

	private AuditPerformanceRule dataBean;
	
	private String centerguid;

	@Override
	public void pageLoad() {
		dataBean = new AuditPerformanceRule();
		if(StringUtil.isBlank(ZwfwUserSession.getInstance().getCenterGuid())){
			addCallbackParam("msg", "人员没有分配到中心!");
		}
		
	}

	/**
	 * 复制选定
	 * 
	 */
	public void copySelect() {
		List<String> select = getDataGridData().getSelectKeys();
		String  rulename ="";
		for (String sel : select) {
			//复制规则
			AuditPerformanceRule performanceRule = service.getPerformanceRuleByGuid(sel).getResult();
			if(performanceRule.getRulename().length()<=196){
			    rulename = performanceRule.getRulename()+ "（复制）";
			    //判断是否有重名规则
			    SqlConditionUtil sql = new SqlConditionUtil();
			    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
			    sql.eq("rulename", rulename);
			    int num = service.findFieldSame(sql.getMap()).getResult();
			    if(num>0){
			        addCallbackParam("msg", "已存在名称为"+rulename+"的规则！");
			        return;
			    }
			    performanceRule.setRulename(rulename);
			}
			performanceRule.setOperatedate(new Date());
			performanceRule.setOperateusername(userSession.getDisplayName());
			performanceRule.setRowguid(UUID.randomUUID().toString());
			performanceRule.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
			service.insertPerformanceRule(performanceRule);
			
			//复制细则
			List<AuditPerformanceRuleDetail> ruleDetails = ruleDetailService.selectPerformanceRuleDetailList(sel).getResult();
			if(ruleDetails!=null && ruleDetails.size()>0){
				for (AuditPerformanceRuleDetail auditPerformanceRuleDetail : ruleDetails) {
					auditPerformanceRuleDetail.setOperatedate(auditPerformanceRuleDetail.getOperatedate());
					auditPerformanceRuleDetail.setOperateusername(userSession.getDisplayName());
					auditPerformanceRuleDetail.setRowguid(UUID.randomUUID().toString());
					auditPerformanceRuleDetail.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
					auditPerformanceRuleDetail.setRulerowguid(performanceRule.getRowguid());
					auditPerformanceRuleDetail.setRulename(rulename);
					ruleDetailService.insertPerformanceRuleDetail(auditPerformanceRuleDetail);
				}
			}
			
		}
		addCallbackParam("msg", "复制成功！");
	}

	public DataGridModel<AuditPerformanceRule> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditPerformanceRule>() {

				@Override
				public List<AuditPerformanceRule> fetchData(int first, int pageSize, String sortField,
						String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					if(StringUtil.isNotBlank(centerguid)){
						sql.eq("centerguid", centerguid);
					}
					if (StringUtil.isNotBlank(dataBean.getRulename())) {
						sql.like("Rulename", dataBean.getRulename());
					}
					if (StringUtil.isNotBlank(dataBean.getObjecttype())) {
						sql.eq("Objecttype", dataBean.getObjecttype());
					}
					sql.setOrderAsc("centerguid");
	                sql.setOrderAsc("objecttype");
	                sql.setOrderAsc("rulename");
					PageData<AuditPerformanceRule> pageData = service
							.selectPerformanceRuleByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
							.getResult();
					for (AuditPerformanceRule performanceRule : pageData.getList()) {
						AuditOrgaServiceCenter orgaServiceCenter = serviceCenter.findAuditServiceCenterByGuid(performanceRule.getCenterguid()).getResult();
						if(orgaServiceCenter!=null){
							performanceRule.set("centername", orgaServiceCenter.getCentername());
						}
					}
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}
			};
		}
		return model;
	}

	@SuppressWarnings("serial")
	public List<SelectItem> getCenterModel() {
		if (centerModel == null) {
			centerModel = new ArrayList<SelectItem>(){
				{add(new SelectItem("","请选择"));}
			};
			List<AuditOrgaServiceCenter> serviceCenters = serviceCenter.getAuditOrgaServiceCenterByCondition(null).getResult();
			if(serviceCenters!=null && serviceCenters.size()>0){
				for (AuditOrgaServiceCenter auditOrgaServiceCenter : serviceCenters) {
					centerModel.add(new SelectItem(auditOrgaServiceCenter.getRowguid(), auditOrgaServiceCenter.getCentername()));
				}
			}
		}
		return this.centerModel;
	}

	public AuditPerformanceRule getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditPerformanceRule dataBean) {
		this.dataBean = dataBean;
	}

	public String getCenterguid() {
		return centerguid;
	}

	public void setCenterguid(String centerguid) {
		this.centerguid = centerguid;
	}
}
