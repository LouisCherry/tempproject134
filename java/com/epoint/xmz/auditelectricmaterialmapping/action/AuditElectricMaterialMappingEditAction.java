package com.epoint.xmz.auditelectricmaterialmapping.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 电力材料映射表修改页面对应的后台
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
@RightRelation(AuditElectricMaterialMappingListAction.class)
@RestController("auditelectricmaterialmappingeditaction")
@Scope("request")
public class AuditElectricMaterialMappingEditAction extends BaseController {

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

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public AuditElectricMaterialMapping getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditElectricMaterialMapping dataBean) {
        this.dataBean = dataBean;
    }

}
