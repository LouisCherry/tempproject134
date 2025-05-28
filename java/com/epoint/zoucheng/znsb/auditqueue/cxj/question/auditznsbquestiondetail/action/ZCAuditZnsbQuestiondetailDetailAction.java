package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestiondetail.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;

/**
 * 问卷调查-问题详情详情页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:09]
 */
@RightRelation(ZCAuditZnsbQuestiondetailListAction.class)
@RestController("zcauditznsbquestiondetaildetailaction")
@Scope("request")
public class ZCAuditZnsbQuestiondetailDetailAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestiondetailService service;

    /**
     * 问卷调查-问题详情实体对象
     */
    private AuditZnsbQuestiondetail dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestiondetail();
        }
    }

    public AuditZnsbQuestiondetail getDataBean() {
        return dataBean;
    }
}
