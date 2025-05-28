package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷修改页面对应的后台
 *
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@RightRelation(ZCAuditZnsbQuestionnaireListAction.class)
@RestController("zcauditznsbquestionnaireeditaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireEditAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireService service;

    /**
     * 问卷调查-问卷实体对象
     */
    private AuditZnsbQuestionnaire dataBean = null;

    String name;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaire();
        }
        name = dataBean.getQuestionnairename();
        // 同步刷新时间
        if (StringUtil.isBlank(getViewData("loadtime"))) {
            addViewData("loadtime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        if (StringUtil.isNotBlank(name)) {
            if (name.equals(dataBean.getQuestionnairename())) {
                service.update(dataBean);
                addCallbackParam("msg", "修改成功！");
                dataBean = null;
            }
            else {
                if (service.getCountByName(dataBean.getQuestionnairename()) > 0) {
                    addCallbackParam("msg", "问卷名称重复，请重新添加！");
                }
                else {
                    AuditZnsbQuestionnaire questionnaire = service.find(dataBean.getRowguid());
                    if (questionnaire.getOperatedate().getTime() > EpointDateUtil
                            .convertString2Date(getViewData("loadtime"), "yyyy-MM-dd HH:mm:ss").getTime()) {
                        addCallbackParam("msg", "该问卷已被修改，请退出后重新进入修改！");
                        addViewData("loadtime", EpointDateUtil
                                .convertDate2String(EpointDateUtil.addSeconds(new Date(), 1), "yyyy-MM-dd HH:mm:ss"));
                        return;
                    }
                    service.update(dataBean);
                    addViewData("loadtime", EpointDateUtil.convertDate2String(EpointDateUtil.addSeconds(new Date(), 1),
                            "yyyy-MM-dd HH:mm:ss"));
                    addCallbackParam("msg", "修改成功！");
                    dataBean = null;
                }
            }
        }
        else {
            addCallbackParam("msg", "该问卷已被删除，保存失败！");
        }

    }

    public AuditZnsbQuestionnaire getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaire dataBean) {
        this.dataBean = dataBean;
    }
}
