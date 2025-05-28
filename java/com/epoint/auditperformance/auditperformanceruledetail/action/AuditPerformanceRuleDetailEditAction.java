package com.epoint.auditperformance.auditperformanceruledetail.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评细则表修改页面对应的后台
 * 
 * @version [版本号, 2018-01-09 15:34:36]
 */
@RestController("auditperformanceruledetaileditaction")
@Scope("request")
public class AuditPerformanceRuleDetailEditAction extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceRuleDetail service;

	/**
	 * 考评细则表实体对象
	 */
	private AuditPerformanceRuleDetail dataBean = null;

	@Autowired
	private IAuditPerformanceRule ruleService;
	
	/**
     * 考评方式下拉列表model
     */
    private List<SelectItem> typeModel = null;

	private String rulename;

	private  Integer rulescore;

	@Override
	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.getPerformanceRuleDetailByGuid(guid).getResult();
		if (dataBean == null) {
			dataBean = new AuditPerformanceRuleDetail();
		}
		AuditPerformanceRule auditPerformanceRule = ruleService.getPerformanceRuleByGuid(dataBean.getRulerowguid()).getResult();
		if(auditPerformanceRule!=null){
			rulename = auditPerformanceRule.getRulename();
			rulescore = auditPerformanceRule.getRulescore();
		}
	}

	/**
	 * 保存修改
	 * 
	 */
	public void save() {
		dataBean.setOperatedate(new Date());
	    dataBean.setDetailrulename(dataBean.getDetailrulename().replaceAll(",", "，"));
		service.updatePerformanceRuleDetail(dataBean);
		addCallbackParam("msg", "修改成功！");
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
        	typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评方式", null, false));
        }
        return this.typeModel;
    }

	public AuditPerformanceRuleDetail getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditPerformanceRuleDetail dataBean) {
		this.dataBean = dataBean;
	}

	public String getRulename() {
		return rulename;
	}

	public void setRulename(String rulename) {
		this.rulename = rulename;
	}

	public Integer getRulescore() {
		return rulescore;
	}

	public void setRulescore(Integer rulescore) {
		this.rulescore = rulescore;
	}
}
