package com.epoint.evainstanceck.action;

import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;
import com.epoint.evainstanceck.space.api.ISpaceAcceptService;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 好差评信息表修改页面对应的后台
 *
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
@RightRelation(EvalortReplyListAction.class)
@RestController("evalortreplyeditaction")
@Scope("request")
public class EvalortReplyEditAction extends BaseController {

    @Autowired
    private IAuditOnlineEvaluat service;
    @Autowired
    private IAuditProject auditProjectService;
    /**
     * 好差评信息表实体对象
     */
    private AuditOnlineEvaluat dataBean = null;
    /**
     * 待办消息API
     */
    @Autowired
    private IMessagesCenterService iMessagesCenterService;
    /**
     * 是否整改下拉列表model
     */
    private List<SelectItem> iszgModel = null;

    @Autowired
    private ISpaceAcceptService iSpaceAcceptService;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = new CommonDao().find(AuditOnlineEvaluat.class,guid);
        String areacode = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        if (dataBean == null) {
            dataBean = new AuditOnlineEvaluat();
        } else {
            AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid(dataBean.getClientidentifier(), areacode).getResult();
            if (StringUtil.isNotBlank(auditproject.getContactmobile())) {
                dataBean.put("Mobile", auditproject.getContactmobile());
                dataBean.put("applicant", auditproject.getApplyername());
                dataBean.put("flowsn", auditproject.getFlowsn());
                dataBean.put("taskname", auditproject.getProjectname());
                dataBean.put("applytime", auditproject.getApplydate());
            }
            String gradeinfo = dataBean.getStr("hcpevaluate");
            StringBuffer sb = new StringBuffer();
            String[] split = null;
            if (gradeinfo != null) {
                split = gradeinfo.split(",");
            }
            if (split != null && split.length > 0) {
                for (String info : split) {
                    Record record = iSpaceAcceptService.getHcpStati(info);
                    if (record != null) {
                        sb.append(record.getStr("itemtext"));
                        sb.append(";");
                    }
                }
                dataBean.put("evacontextlist", sb.toString());
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
//        String content = "尊敬的用户您好，您反馈的服务差评目前有了最新整改进展。" + writingFeedback + "感谢您的评价，我们会竭尽全力为您更好的服务";
//        // 5.2、往messages_center表里插数据字段FromDispName存ck表的rowguid
//        iMessagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, null,
//                dataBean.getMobile(), dataBean.getMobile(), dataBean.getApplicant(), "",
//                String.valueOf(dataBean.getRowid()), "", "", null, false, ZwfwUserSession.getInstance().getAreaCode());
//
        dataBean.set("iszg", "1");
        dataBean.set("status","3");
        service.updateAuditOnineEvaluat(dataBean);
        addCallbackParam("msg", l("反馈成功") + "！");
    }

    public AuditOnlineEvaluat getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditOnlineEvaluat dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIszgModel() {
        if (iszgModel == null) {
            iszgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.iszgModel;
    }

}
