package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestiondetail.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;

/**
 * 问卷调查-问题详情修改页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:09]
 */
@RightRelation(ZCAuditZnsbQuestiondetailListAction.class)
@RestController("zcauditznsbquestiondetaileditaction")
@Scope("request")
public class ZCAuditZnsbQuestiondetailEditAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestiondetailService service;

    /**
     * 问卷调查-问题详情实体对象
     */
    private AuditZnsbQuestiondetail dataBean = null;
    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestiondetail();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        if (service.getCountByName(dataBean.getAnsweroption(), dataBean.getQuestionguid(), dataBean.getRowguid()) > 0) {
            addCallbackParam("msg", "选项名称重复，请重新添加！");
        }
        else {
            service.update(dataBean);
            addCallbackParam("msg", "修改成功！");
        }
    }

    public AuditZnsbQuestiondetail getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestiondetail dataBean) {
        this.dataBean = dataBean;
    }

}
