package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.IAuditZnsbQuestionnaireQuestionService;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷修改页面对应的后台
 *
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@RightRelation(ZCAuditZnsbQuestionnaireListAction.class)
@RestController("zcauditznsbquestionnaireselectaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireSelectAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireService service;

    @Autowired
    private IAuditZnsbQuestionnaireQuestionService questionnaireQuestionService;

    @Autowired
    private IZCAuditZnsbQuestioninfoService questioninfoService;

    /**
     * 问卷调查-问卷实体对象
     */
    private AuditZnsbQuestionnaire dataBean = null;

    private String guid;

    public void pageLoad() {
        guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaire();
        }
        int radiocount = questionnaireQuestionService.getCountByQuestiontype(guid, "0");
        int multicount = questionnaireQuestionService.getCountByQuestiontype(guid, "1");
        int judgecount = questionnaireQuestionService.getCountByQuestiontype(guid, "2");
        dataBean.set("radiocount", radiocount);
        dataBean.set("multicount", multicount);
        dataBean.set("judgecount", judgecount);
    }

    /**
     * 自动抽题
     */
    public void save() {
        List<AuditZnsbQuestioninfo> alllist = new ArrayList<>();
        String centerguid = ZwfwUserSession.getInstance().getCenterGuid();

        int radiocount = dataBean.getInt("radiocount");
        int multicount = dataBean.getInt("multicount");
        int judgecount = dataBean.getInt("judgecount");

        List<AuditZnsbQuestioninfo> radiolist = questioninfoService.findListByQuestionType("0", centerguid);
        List<AuditZnsbQuestioninfo> multilist = questioninfoService.findListByQuestionType("1", centerguid);
        List<AuditZnsbQuestioninfo> judgelist = questioninfoService.findListByQuestionType("2", centerguid);
        if (radiocount > 0 && radiolist != null && !radiolist.isEmpty()) {
            if (radiocount > radiolist.size()) {
                addCallbackParam("msg", "输入的单选题数量不能超过题库数量！");
                return;
            }
            else {
                Collections.shuffle(radiolist);
                alllist.addAll(radiolist.subList(0, radiocount));
            }
        }
        if (multicount > 0 && multilist != null && !multilist.isEmpty()) {
            if (multicount > multilist.size()) {
                addCallbackParam("msg", "输入的多选题数量不能超过题库数量！");
                return;
            }
            else {
                Collections.shuffle(multilist);
                alllist.addAll(multilist.subList(0, multicount));
            }
        }
        if (judgecount > 0 && judgelist != null && !judgelist.isEmpty()) {
            if (judgecount > judgelist.size()) {
                addCallbackParam("msg", "输入的判断题数量不能超过题库数量！");
                return;
            }
            else {
                Collections.shuffle(judgelist);
                alllist.addAll(judgelist.subList(0, judgecount));
            }
        }
        if (alllist != null && !alllist.isEmpty()) {
            // 添加新的问题关联关系
            if (StringUtil.isNotBlank(guid)) {
                questionnaireQuestionService.deleteByQuestionNaireGuid(guid);
                Date now = new Date();
                for (AuditZnsbQuestioninfo questioninfo : alllist) {
                    // 操作audit_znsb_questionnaire_question
                    AuditZnsbQuestionnaireQuestion questionnaireQuestion = new AuditZnsbQuestionnaireQuestion();
                    questionnaireQuestion.setOperatedate(now);
                    questionnaireQuestion.setOperateusername(UserSession.getInstance().getDisplayName());
                    questionnaireQuestion.setRowguid(UUID.randomUUID().toString());
                    questionnaireQuestion.setQuestionnaireguid(guid);
                    questionnaireQuestion.setQuestion(questioninfo.getRowguid());
                    questionnaireQuestionService.insert(questionnaireQuestion);
                }
            }
        }
        addCallbackParam("msg", "抽题成功！");
    }

    public AuditZnsbQuestionnaire getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaire dataBean) {
        this.dataBean = dataBean;
    }
}
