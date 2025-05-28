package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaireresult.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.inter.IZCAuditZnsbQuestionnaireresultService;

/**
 * 问卷结果list页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-05 15:25:58]
 */
@RestController("zcauditznsbquestionnaireresultlistaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireresultListAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireresultService service;

    /**
     * 问卷结果实体对象
     */
    private AuditZnsbQuestionnaireresult dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByName(service.find(sel).getQuestionnaireguid());
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String s = dataBean.getQuestionnairename();
                    if (StringUtil.isNotBlank(s)) {
                        if (dataBean.getQuestionnairename().contains("_")
                                || dataBean.getQuestionnairename().contains("%")) {
                            s = dataBean.getQuestionnairename().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
                        }
                    }
                    List<Record> allList = service.getAllList(s, ZwfwUserSession.getInstance().getCenterGuid(), first,
                            pageSize);

                    SqlConditionUtil sql;
                    for (Record record : allList) {
                        if (StringUtil.isNotBlank(record.getStr("newName"))) {
                            record.set("questionnairename", record.getStr("newName"));
                        }
                        else {
                            record.set("questionnairename", record.getStr("oldName"));
                        }
                        sql = new SqlConditionUtil();
                        sql.eq("questionnaireguid", record.getStr("questionnaireguid"));
                        sql.setOrderDesc("OperateDate");
                        List<AuditZnsbQuestionnaireresult> questionnaireresults = service
                                .getAuditZnsbQuestionnaireResult(sql.getMap(), 0, 0, null, null).getResult().getList();
                        for (int i = 0; i < questionnaireresults.size(); i++) {
                            if (i == 0) {
                                record.set("operatedate", EpointDateUtil.convertDate2String(
                                        questionnaireresults.get(0).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                            }
                        }
                        if (StringUtil.isBlank(record.getStr("createtime"))) {
                            record.set("createtime", "已删除");
                        }
                    }
                    Integer allListCount = service.getAllListCount(dataBean.getQuestionnairename());
                    this.setRowCount(allListCount);
                    return allList;
                }
            };
        }
        return model;
    }

    public AuditZnsbQuestionnaireresult getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaireresult();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaireresult dataBean) {
        this.dataBean = dataBean;
    }

}
