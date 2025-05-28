package com.epoint.auditspphasetask.action;

import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.auditspphasetask.api.IAuditSpPhaseTaskService;
import com.epoint.auditspphasetask.api.entity.AuditSpPhaseTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 前四阶段事项配置表新增页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:06:22]
 */
@RightRelation(AuditSpPhaseTaskListAction.class)
@RestController("auditspphasetaskaddaction")
@Scope("request")
public class AuditSpPhaseTaskAddAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseTaskService service;

    @Autowired
    private IAuditSpPhaseGroupService iAuditSpPhaseGroupService;
    /**
     * 前四阶段事项配置表实体对象
     */
    private AuditSpPhaseTask dataBean = null;

    private List<SelectItem> groupModel = null;

    private List<SelectItem> smalltypeModel = null;

    private List<SelectItem> isimportantMods = null;

    public void pageLoad() {
        dataBean = new AuditSpPhaseTask();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditSpPhaseTask();
    }

    public List<SelectItem> getGroupModel() {
        if (groupModel == null) {
            groupModel = new ArrayList<>();
            SqlConditionUtil sql = new SqlConditionUtil();

            List<AuditSpPhaseGroup> list = iAuditSpPhaseGroupService.getAllPhaseGroup();
            if (!list.isEmpty()) {
                for (AuditSpPhaseGroup auditSpPhaseGroup : list) {
                    groupModel.add(new SelectItem(auditSpPhaseGroup.getRowguid(), auditSpPhaseGroup.getGroupname()));
                }
            }
        }
        return this.groupModel;
    }

    public List<SelectItem> getIsimportantMods() {
        if (isimportantMods == null) {
            isimportantMods = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.isimportantMods;
    }

    public List<SelectItem> getSmalltypeModel() {
        if (smalltypeModel == null) {
            smalltypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "展示类型", null, false));
        }
        return this.smalltypeModel;
    }

    public AuditSpPhaseTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPhaseTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseTask dataBean) {
        this.dataBean = dataBean;
    }

}
