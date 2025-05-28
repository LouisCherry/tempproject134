package com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.IAuditZnsbQuestionnaireQuestionService;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;

/**
 * 试卷和题目的关系表新增页面对应的后台
 * 
 * @author TJX
 * @version [版本号, 2022-07-08 16:34:55]
 */
@RestController("auditznsbquestionnairequestionaddaction")
@Scope("request")
public class AuditZnsbQuestionnaireQuestionAddAction extends BaseController
{
    @Autowired
    private IAuditZnsbQuestionnaireQuestionService service;
    /**
     * 试卷和题目的关系表实体对象
     */
    private AuditZnsbQuestionnaireQuestion dataBean = null;

    public void pageLoad() {
        dataBean = new AuditZnsbQuestionnaireQuestion();
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
        dataBean = new AuditZnsbQuestionnaireQuestion();
    }

    public AuditZnsbQuestionnaireQuestion getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaireQuestion();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaireQuestion dataBean) {
        this.dataBean = dataBean;
    }

}
