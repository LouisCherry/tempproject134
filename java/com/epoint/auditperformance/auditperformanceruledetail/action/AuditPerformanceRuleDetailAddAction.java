package com.epoint.auditperformance.auditperformanceruledetail.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评细则表新增页面对应的后台
 * 
 * @version [版本号, 2018-01-09 15:34:36]
 */
@RestController("auditperformanceruledetailaddaction")
@Scope("request")
public class AuditPerformanceRuleDetailAddAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceRuleDetail service;
	
	@Autowired
	private IAuditPerformanceRule ruleService;
	
	/**
	 * 考评细则表实体对象
	 */
	private AuditPerformanceRuleDetail dataBean = null;
	
	/**
     * 考评方式下拉列表model
     */
    private List<SelectItem> typeModel = null;
    
    /**
     * 考评规则实体对象
     */
    private AuditPerformanceRule performanceRule;
    
    private String ruleGuid;

    private String rulename;

    private  Integer rulescore;

    @Override
	public void pageLoad() {
		dataBean = new AuditPerformanceRuleDetail();
		ruleGuid = getRequestParameter("ruleguid");
		performanceRule = ruleService.getPerformanceRuleByGuid(ruleGuid).getResult();
		if (performanceRule == null) {
			addCallbackParam("msg", "对应的考评规则不存在！");
		}else{
			rulename = performanceRule.getRulename();
			rulescore = performanceRule.getRulescore();
		}
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {    
	    //判断细则名称
        if (service.findFieldSame(ZwfwUserSession.getInstance().getCenterGuid(), dataBean.getDetailrulename(), ruleGuid).getResult().size() > 0) {
            addCallbackParam("same", "考评细则名称已存在！");
        }else{
		dataBean.setRowguid(UUID.randomUUID().toString());
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		dataBean.setRulerowguid(performanceRule.getRowguid());
		dataBean.setObjecttype(performanceRule.getObjecttype());
		dataBean.setRulename(performanceRule.getRulename());
		dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
		dataBean.setDetailrulename(dataBean.getDetailrulename().replaceAll(",", "，"));
		service.insertPerformanceRuleDetail(dataBean);
		addCallbackParam("msg", "保存成功！");
        }
		dataBean = null;
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditPerformanceRuleDetail();
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
		if (dataBean == null) {
			dataBean = new AuditPerformanceRuleDetail();
		}
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
