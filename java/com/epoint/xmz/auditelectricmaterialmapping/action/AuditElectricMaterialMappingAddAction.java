package com.epoint.xmz.auditelectricmaterialmapping.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 电力材料映射表新增页面对应的后台
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:06]
 */
@RightRelation(AuditElectricMaterialMappingListAction.class)
@RestController("auditelectricmaterialmappingaddaction")
@Scope("request")
public class AuditElectricMaterialMappingAddAction extends BaseController {
    @Autowired
    private IAuditElectricMaterialMappingService service;
    /**
     * 电力材料映射表实体对象
     */
    private AuditElectricMaterialMapping dataBean = null;


    public void pageLoad() {
        dataBean = new AuditElectricMaterialMapping();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditElectricMaterialMapping();
    }

    public AuditElectricMaterialMapping getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditElectricMaterialMapping();
        }
        return dataBean;
    }

    public void setDataBean(AuditElectricMaterialMapping dataBean) {
        this.dataBean = dataBean;
    }


}
