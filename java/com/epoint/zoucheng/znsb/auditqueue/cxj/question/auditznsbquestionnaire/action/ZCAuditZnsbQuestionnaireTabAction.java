package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
@RestController("zcauditznsbquestionnairetabaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireTabAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void pageLoad() {
      

    }
    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        Record questionlist = new Record();
        questionlist.put("name", "题库维护");
        questionlist.put("code", "question");
        questionlist.put("url", "znsb/questionconfig/auditznsbquestioninfo/auditznsbquestioninfolist");
        recordList.add(questionlist);
        Record list = new Record();
        list.put("name", "试卷列表");
        list.put("code", "list");
        list.put("url", "znsb/questionconfig/auditznsbquestionnaire/auditznsbquestionnairelist");
        recordList.add(list);
        // 待上报
        Record config = new Record();
        config.put("name", "题目结果列表");
        config.put("code", "config");
        config.put("url", "znsb/questionconfig/auditznsbquestionnaireresult/auditznsbquestionnaireresultlist");
        recordList.add(config);
        sendRespose(JsonUtil.listToJson(recordList));
    }
}
