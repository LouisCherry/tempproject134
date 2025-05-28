package com.epoint.xmz.auditelectricmaterialmapping.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电力材料映射表详情页面对应的后台
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
@RightRelation(AuditElectricMaterialMappingListAction.class)
@RestController("auditelectricmaterialmappingdetailaction")
@Scope("request")
public class AuditElectricMaterialMappingDetailAction extends BaseController {
    @Autowired
    private IAuditElectricMaterialMappingService service;

    /**
     * 电力材料映射表实体对象
     */
    private AuditElectricMaterialMapping dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditElectricMaterialMapping();
        }
    }


    public AuditElectricMaterialMapping getDataBean() {
        return dataBean;
    }
}