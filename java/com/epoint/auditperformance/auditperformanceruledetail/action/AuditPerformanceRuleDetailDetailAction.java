package com.epoint.auditperformance.auditperformanceruledetail.action;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformanceruledetail.api.IAuditPerformanceRuleDetail;
import com.epoint.basic.auditperformance.auditperformanceruledetail.domain.AuditPerformanceRuleDetail;
import com.epoint.basic.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考评细则表详情页面对应的后台
 *
 * @version [版本号, 2018-01-09 15:34:36]
 */
@RestController("auditperformanceruledetaildetailaction")
@Scope("request")
public class AuditPerformanceRuleDetailDetailAction extends BaseController {
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

    private String rulename;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.getPerformanceRuleDetailByGuid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditPerformanceRuleDetail();
        }
        AuditPerformanceRule auditPerformanceRule = ruleService.getPerformanceRuleByGuid(dataBean.getRulerowguid()).getResult();
        if (auditPerformanceRule != null) {
            rulename = auditPerformanceRule.getRulename();
        }
    }


    public AuditPerformanceRuleDetail getDataBean() {
        return dataBean;
    }

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }
}