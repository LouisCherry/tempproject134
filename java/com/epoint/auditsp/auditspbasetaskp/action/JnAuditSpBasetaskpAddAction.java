package com.epoint.auditsp.auditspbasetaskp.action;

import com.epoint.auditsp.auditspbasetaskp.domain.AuditSpBasetaskP;
import com.epoint.auditsp.auditspbasetaskp.inter.IAuditSpBasetaskP;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 并联审批事项表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2019-04-25 10:07:31]
 */
@RestController("jnauditspbasetaskpaddaction")
@Scope("request")
public class JnAuditSpBasetaskpAddAction extends BaseController {
    /* 3.0新增逻辑开始 */
    private static final long serialVersionUID = 7798753811284837045L;

    @Autowired
    private IAuditSpBasetaskP service;

    private AuditSpBasetaskP dataBean;

    private List<SelectItem> oumodel;
    private List<SelectItem> phasemodel;

    public void pageLoad() {
    }

    public void save() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        // 查询事项编码是不是重复
        if (!"9990000".equals(dataBean.getTasktaskcode())) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskcode", dataBean.getTasktaskcode());
            Integer count = service.getCountByCondition(sqlc.getMap()).getResult();
            if (count != null && count > 0) {
                addCallbackParam("msg", "事项编码重复！");
                return;
            }
        }
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
        dataBean.setCreatedate(new Date());
        service.addBasetask(dataBean);
        addCallbackParam("msg", "添加成功！");
    }

    public AuditSpBasetaskP getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBasetaskP();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBasetaskP dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getOuModel() {
        if (oumodel == null) {
            oumodel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批部门", null, false));
        }
        return this.oumodel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getPhaseModel() {
        if (phasemodel == null) {
            phasemodel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批阶段", null, false));
        }
        return this.phasemodel;
    }

    /* 3.0新增逻辑结束 */
}
