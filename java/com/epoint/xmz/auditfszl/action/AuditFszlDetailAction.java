package com.epoint.xmz.auditfszl.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 放射诊疗数据详情页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:15]
 */
@RightRelation(AuditFszlListAction.class)
@RestController("auditfszldetailaction")
@Scope("request")
public class AuditFszlDetailAction extends BaseController {
    @Autowired
    private IAuditFszlService service;

    /**
     * 放射诊疗数据实体对象
     */
    private AuditFszl dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditFszl();
        }
    }


    public AuditFszl getDataBean() {
        return dataBean;
    }
}