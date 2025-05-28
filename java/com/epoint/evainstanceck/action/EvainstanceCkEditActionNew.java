package com.epoint.evainstanceck.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 好差评信息表修改页面对应的后台
 * 
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@RightRelation(EvainstanceCkListActionNew.class)
@RestController("evainstanceckeditactionnew")
@Scope("request")
public class EvainstanceCkEditActionNew extends BaseController
{

    @Autowired
    private IEvainstanceCkService service;
    @Autowired
    private IAuditProject auditProjectService;
    /**
     * 好差评信息表实体对象
     */
    private EvainstanceCk dataBean = null;
    /**
     * 待办消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;
    /**
     * 好差评反馈信息
     */
    private String writingFeedback = "";
    /**
     * 是否整改下拉列表model
     */
    private List<SelectItem> iszgModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new EvainstanceCk();
        }
        else {
            AuditProject auditproject = auditProjectService
                    .getAuditProjectByFlowsn(dataBean.getProjectno().substring(2), "").getResult();

            if (StringUtil.isNotBlank(auditproject.getContactmobile())) {
                dataBean.setMobile(auditproject.getContactmobile());

            }
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        String content = "尊敬的用户您好，您反馈的服务差评目前有了最新整改进展。" + writingFeedback + "感谢您的评价，我们会竭尽全力为您更好的服务";
        // 5.2、往messages_center表里插数据字段FromDispName存ck表的rowguid
        iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
                dataBean.getMobile(), dataBean.getMobile(), dataBean.getApplicant(), "",
                String.valueOf(dataBean.getRowid()), "", "", null, false, ZwfwUserSession.getInstance().getAreaCode());
        dataBean.setIszg("1");
        service.update(dataBean);
        addCallbackParam("msg", l("反馈成功") + "！");
    }

    public EvainstanceCk getDataBean() {
        return dataBean;
    }

    public void setDataBean(EvainstanceCk dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIszgModel() {
        if (iszgModel == null) {
            iszgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.iszgModel;
    }

    public String getWritingFeedback() {
        return writingFeedback;
    }

    public void setWritingFeedback(String writingFeedback) {
        this.writingFeedback = writingFeedback;
    }

}
