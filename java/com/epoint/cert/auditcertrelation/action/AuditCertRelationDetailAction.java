package com.epoint.cert.auditcertrelation.action;

import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 证照字段关系表详情页面对应的后台
 *
 * @author miemieyang12128
 * @version [版本号, 2024-10-16 09:08:40]
 */
@RightRelation(AuditCertRelationListAction.class)
@RestController("auditcertrelationdetailaction")
@Scope("request")
public class AuditCertRelationDetailAction extends BaseController {
    @Autowired
    private IAuditCertRelationService service;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    /**
     * 证照字段关系表实体对象
     */
    private AuditCertRelation dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditCertRelation();
        }
    }

    public AuditCertRelation getDataBean() {
        return dataBean;
    }
}