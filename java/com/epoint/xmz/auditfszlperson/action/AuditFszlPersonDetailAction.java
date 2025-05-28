package com.epoint.xmz.auditfszlperson.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 反射工作人员表详情页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:38]
 */
@RightRelation(AuditFszlPersonListAction.class)
@RestController("auditfszlpersondetailaction")
@Scope("request")
public class AuditFszlPersonDetailAction extends BaseController {
    @Autowired
    private IAuditFszlPersonService service;

    /**
     * 反射工作人员表实体对象
     */
    private AuditFszlPerson dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditFszlPerson();
        }
    }


    public AuditFszlPerson getDataBean() {
        return dataBean;
    }
}