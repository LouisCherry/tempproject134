package com.epoint.auditqueue.auditqueuetasktype.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 事项分类修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-11-09 09:27:31]
 */
@RestController("lsauditqueuetasktypeeditaction")
@Scope("request")
public class LSAuditQueueTasktypeEditAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

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
     * 是否配置到混合窗口单选按钮组model
     */
    private List<SelectItem> ismixwindowModel = null;

    /**
     * 是否需要人脸识别单选按钮组model
     */
    private List<SelectItem> is_faceModel = null;
    private List<SelectItem> is_limitnoModel = null;

    @Override
    public void pageLoad() {
        dataBean = getAuditQueueTasktypeByRowguid(getRequestParameter("guid"));
        if (dataBean == null) {
            dataBean = new AuditQueueTasktype();
        }
        this.addCallbackParam("ouname", getName(dataBean.getOuguid()));
        this.addCallbackParam("ouguid", dataBean.getOuguid());

    }

    public AuditQueueTasktype getAuditQueueTasktypeByRowguid(String rowguid) {
        return tasktypeservice.getAuditQueueTasktypeByRowguid(rowguid).getResult();
    }

    public String getName(String ouguid) {
        return frameOuService9.getOuByOuGuid(ouguid).getOuname();
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        String msg = "";

        dataBean.setOperatedate(new Date());
        AuditCommonResult<String> addResult = null;

        addResult = tasktypeservice.updateAuditQueueTasktype(dataBean);

        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        addCallbackParam("msg", msg);
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
    public List<SelectItem> getIsmixwindowModel() {
        if (ismixwindowModel == null) {
            ismixwindowModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.ismixwindowModel;
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
    public List<SelectItem> getIs_limitnoModel() {
        if (is_limitnoModel == null) {
            is_limitnoModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_limitnoModel;
    }

    public AuditQueueTasktype getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditQueueTasktype dataBean) {
        this.dataBean = dataBean;
    }

}
