package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestiondetail.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;

/**
 * 问卷调查-问题详情新增页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:09]
 */
@RightRelation(ZCAuditZnsbQuestiondetailListAction.class)
@RestController("zcauditznsbquestiondetailaddaction")
@Scope("request")
public class ZCAuditZnsbQuestiondetailAddAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    @Autowired
    private IZCAuditZnsbQuestiondetailService service;
    /**
     * 问卷调查-问题详情实体对象
     */
    private AuditZnsbQuestiondetail dataBean = null;

    private String questionguid;
    @Override
    public void pageLoad() {
        questionguid = getRequestParameter("questionguid");
        dataBean = new AuditZnsbQuestiondetail();
        dataBean.setSort(0);
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        if (StringUtil.isBlank(questionguid)) {
            addCallbackParam("msg", "问题guid不能为空！");
            return;
        }
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setQuestionguid(questionguid);

        if (service.getCountByName(dataBean.getAnsweroption(), dataBean.getQuestionguid(), dataBean.getRowguid()) > 0) {
            addCallbackParam("msg", "选项名称重复，请重新添加！");
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
        dataBean = new AuditZnsbQuestiondetail();
    }

    public AuditZnsbQuestiondetail getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestiondetail();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestiondetail dataBean) {
        this.dataBean = dataBean;
    }

}
