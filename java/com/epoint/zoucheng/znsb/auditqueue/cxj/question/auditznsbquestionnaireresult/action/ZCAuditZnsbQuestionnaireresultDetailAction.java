package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaireresult.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.inter.IZCAuditZnsbQuestionnaireresultService;

/**
 * 问卷结果详情页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-05 15:25:58]
 */
@RightRelation(ZCAuditZnsbQuestionnaireresultListAction.class)
@RestController("zcauditznsbquestionnaireresultdetailaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireresultDetailAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    @Autowired
    private IZCAuditZnsbQuestionnaireresultService service;
    @Autowired
    private IZCAuditZnsbQuestiondetailService questiondetailService;
    @Autowired
    private IAuditZnsbEquipment auditZnsbEquipment;
    /**
     * 问卷结果实体对象
     */
    private AuditZnsbQuestionnaireresult dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaireresult();
        }
        JSONObject questionnairereinfo = JSONObject.parseObject(dataBean.getQuestionnairereinfo());
        JSONArray answerList = JSONArray.parseArray(dataBean.getQuestionnairereanswer());
        JSONArray questionlist = (JSONArray) questionnairereinfo.get("questionlist");
        if (StringUtil.isNotBlank(dataBean.getMacaddress())) {
            AuditZnsbEquipment equipment = auditZnsbEquipment.getDetailbyMacaddress(dataBean.getMacaddress())
                    .getResult();
            dataBean.setMacaddress(equipment.getMachinename());
        }
        int rightcount = 0;
        int errorcount = 0;
        if (questionlist != null && !questionlist.isEmpty()) {
            // 遍历判断答案是否正确
            for (int i = 0; i < questionlist.size(); i++) {
                JSONObject questioninfo = questionlist.getJSONObject(i);
                JSONObject question = questioninfo.getJSONObject("question");
                List<AuditZnsbQuestiondetail> questiondetails = questiondetailService.findListByQuestionGuid(question.getString("rowguid"));
                if (questiondetails != null && !questiondetails.isEmpty()) {
                    // 遍历所有选项
                    for (AuditZnsbQuestiondetail questiondetail: questiondetails) {                        
                        if ("1".equals(questiondetail.getAnswer())) {
                            // 此选项是正确答案,提交答案中没有此选项则为错误
                            if (!answerList.contains(questiondetail.getRowguid())) {
                                errorcount++;
                                questionlist.getJSONObject(i).put("isright", 0);
                                break;
                            }
                        }else {
                            // 此选项非正确答案，提交的答案中有此选项则为错误
                            if (answerList.contains(questiondetail.getRowguid())) {
                                errorcount++;
                                questionlist.getJSONObject(i).put("isright", 0);
                                break;
                            }
                        }
                    }
                }
            }
            rightcount = questionlist.size() - errorcount;
        }
        
        dataBean.set("rightcount", rightcount);
        dataBean.set("errorcount", errorcount);
        JSONObject data = new JSONObject();
        data.put("question", questionlist);
        data.put("answer", answerList);
        addCallbackParam("data", data);
    }

    public AuditZnsbQuestionnaireresult getDataBean() {
        return dataBean;
    }
}
