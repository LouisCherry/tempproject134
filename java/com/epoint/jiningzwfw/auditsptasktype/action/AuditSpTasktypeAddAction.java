package com.epoint.jiningzwfw.auditsptasktype.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jiningzwfw.auditsptasktype.api.IAuditSpTasktypeService;
import com.epoint.jiningzwfw.auditsptasktype.api.entity.AuditSpTasktype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;

/**
 * 事项分类新增页面对应的后台
 *
 * @author qichudong
 * @version [版本号, 2024-09-22 10:56:21]
 */
@RightRelation(AuditSpTasktypeListAction.class)
@RestController("auditsptasktypeaddaction")
@Scope("request")
public class AuditSpTasktypeAddAction extends BaseController {
    @Autowired
    private IAuditSpTasktypeService service;
    /**
     * 事项分类实体对象
     */
    private AuditSpTasktype dataBean = null;

    /**
     * 所属阶段下拉列表model
     */
    private List<SelectItem> phaseidModel = null;


    public void pageLoad() {
        dataBean = new AuditSpTasktype();
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
        dataBean = new AuditSpTasktype();
    }

    public AuditSpTasktype getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpTasktype();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpTasktype dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getPhaseidModel() {
        if (phaseidModel == null) {
            phaseidModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批阶段", null, false));
        }
        return this.phaseidModel;
    }

}
