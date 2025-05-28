package com.epoint.jnzwfw.auditorga.auditorgausercondition.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity.AuditOrgaUsercondition;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.IAuditOrgaUserconditionService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 人员在岗信息表修改页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
@RightRelation(JNAuditOrgaUserconditionListAction.class)
@RestController("auditorgauserconditioneditaction")
@Scope("request")
public class AuditOrgaUserconditionEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditOrgaUserconditionService service;

    /**
     * 人员在岗信息表实体对象
     */
    private AuditOrgaUsercondition dataBean = null;

    private List<SelectItem> DepartmentModel = null;
    private List<SelectItem> UserstateModel = null;
    
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditOrgaUsercondition();
        }
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

    public AuditOrgaUsercondition getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOrgaUsercondition dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getDepartmentModel() {
        if (this.DepartmentModel == null) {
            this.DepartmentModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "科室名称", (String) null, false));
        }

        return this.DepartmentModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getUserstateModel() {
        if (this.UserstateModel == null) {
            this.UserstateModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "在岗情况", (String) null, false));
        }

        return this.UserstateModel;
    }
}
