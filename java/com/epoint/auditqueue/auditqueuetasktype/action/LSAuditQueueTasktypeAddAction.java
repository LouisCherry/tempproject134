package com.epoint.auditqueue.auditqueuetasktype.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditqueuedefinition.inter.IAuditQueueDefinition;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 事项分类新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-11-09 09:27:31]
 */
@RestController("lsauditqueuetasktypeaddaction")
@Scope("request")
public class LSAuditQueueTasktypeAddAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditQueueDefinition queueDefinitionservice;
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    /**
     * 部门service
     */
    @Autowired
    private IOuService frameOuService9;
    /**
     * 事项分类实体对象
     */
    private AuditQueueTasktype dataBean = null;

    /**
     * 是否允许预约单选按钮组model
     */
    private List<SelectItem> is_yuyueModel = null;
    /**
     * 是否需要人脸识别单选按钮组model
     */
    private List<SelectItem> is_faceModel = null;

    private List<SelectItem> is_limitnoModel = null;
    /**
     * 是否配置到混合窗口单选按钮组model
     */
    private List<SelectItem> ismixwindowModel = null;

    private String centerGuid;

    @Override
    public void pageLoad() {
        dataBean = new AuditQueueTasktype();
        centerGuid = getRequestParameter("centerguid");
        if (StringUtil.isBlank(centerGuid)) {
            centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        }
        dataBean.setCenterguid(centerGuid);

        // 左侧选择部门名称，则为该部门的名称
        if (StringUtil.isNotBlank(this.getRequestParameter("ouguid"))) {
            dataBean.setOuguid(this.getRequestParameter("ouguid"));
            this.addCallbackParam("ouname",
                    frameOuService9.getOuByOuGuid(this.getRequestParameter("ouguid")).getOuname());
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {

        String msg = "";

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
        AuditCommonResult<String> addResult = null;
        // 当一个中心下只有一个队列时，新增事项分类自动全部关联该队列
        List<String> result = queueDefinitionservice.getAllQueueValue(ZwfwUserSession.getInstance().getCenterGuid())
                .getResult();
        if (result != null && result.size() == 1) {
            dataBean.setQueuevalue(result.get(0));
        }
        addResult = tasktypeservice.insertTasktype(dataBean);

        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "保存成功！";
        }

        addCallbackParam("msg", msg);
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditQueueTasktype();
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_yuyueModel() {
        if (is_yuyueModel == null) {
            is_yuyueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_yuyueModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_faceModel() {
        if (is_faceModel == null) {
            is_faceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_faceModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsmixwindowModel() {
        if (ismixwindowModel == null) {
            ismixwindowModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.ismixwindowModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_limitnoModel() {
        if (is_limitnoModel == null) {
            is_limitnoModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_limitnoModel;
    }

    public AuditQueueTasktype getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueueTasktype();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueueTasktype dataBean) {
        this.dataBean = dataBean;
    }
}
