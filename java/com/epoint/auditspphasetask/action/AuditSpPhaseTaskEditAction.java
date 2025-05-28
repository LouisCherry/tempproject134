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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 前四阶段事项配置表修改页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:06:22]
 */
@RightRelation(AuditSpPhaseTaskListAction.class)
@RestController("auditspphasetaskeditaction")
@Scope("request")
public class AuditSpPhaseTaskEditAction extends BaseController
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
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPhaseTask();
        }

        addCallbackParam("taskname", dataBean.getTaskname());
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
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
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseTask dataBean) {
        this.dataBean = dataBean;
    }

}
