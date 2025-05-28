package com.epoint.auditqueueqhjmodule.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueueqhjmodule.api.IAuditQueueQhjmoduleService;
import com.epoint.auditqueueqhjmodule.entity.AuditQueueQhjmodule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 取号机大厅模块配置表新增页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2024-11-14 09:51:07]
 */
@RightRelation(AuditQueueQhjmoduleListAction.class)
@RestController("auditqueueqhjmoduleaddaction")
@Scope("request")
public class AuditQueueQhjmoduleAddAction extends BaseController
{
    @Autowired
    private IAuditQueueQhjmoduleService service;

    /**
     * 取号机大厅模块背景颜色model
     */
    private List<SelectItem> bgclsModel = null;

    /**
     * 是否展示部门model
     */
    private List<SelectItem> isshowouModel = null;

    /**
     * 取号机大厅模块配置表实体对象
     */
    private AuditQueueQhjmodule dataBean = null;

    private String centerguid = ZwfwUserSession.getInstance().getCenterGuid();

    public void pageLoad() {
        dataBean = new AuditQueueQhjmodule();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCenterguid(centerguid);
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditQueueQhjmodule();
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBgclsModel() {
        if (bgclsModel == null) {
            bgclsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "取号机大厅模块颜色", null, false));
        }
        return this.bgclsModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsshowouModel() {
        if (isshowouModel == null) {
            isshowouModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.isshowouModel;
    }

    public AuditQueueQhjmodule getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueueQhjmodule();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueueQhjmodule dataBean) {
        this.dataBean = dataBean;
    }

}
