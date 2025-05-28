package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷新增页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:23]
 */
@RightRelation(ZCAuditZnsbQuestionnaireListAction.class)
@RestController("zcauditznsbquestionnaireaddaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireAddAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    @Autowired
    private IZCAuditZnsbQuestionnaireService service;
    /**
     * 问卷调查-问卷实体对象
     */
    private AuditZnsbQuestionnaire dataBean = null;

    private FileUploadModel9 FileUploadModel;
    /**
     * 文件附件标识
     */
    private String picattachguid;

    public void pageLoad() {
        dataBean = new AuditZnsbQuestionnaire();
        dataBean.setSort(0);
        dataBean.setIs_use(QueueConstant.Common_no_String);
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setCreatetime(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());

        // 判断名称是否重复
        if (service.getCountByName(dataBean.getQuestionnairename()) > 0) {
            addCallbackParam("msg", "问卷名称重复，请重新添加！");
        }
        else {
            service.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null;
        }
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditZnsbQuestionnaire();
    }

    public AuditZnsbQuestionnaire getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaire();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaire dataBean) {
        this.dataBean = dataBean;
    }

}
