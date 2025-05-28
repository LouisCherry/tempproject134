package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷详情页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@RightRelation(ZCAuditZnsbQuestionnaireListAction.class)
@RestController("zcauditznsbquestionnairedetailaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireDetailAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireService service;

    @Autowired
    private IZCAuditZnsbQuestioninfoService questioninfoService;

    /**
     * 问卷调查-问卷实体对象
     */
    private AuditZnsbQuestionnaire dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestioninfo> model;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaire();
        }
    }

    @SuppressWarnings("serial")
    public DataGridModel<AuditZnsbQuestioninfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestioninfo>()
            {

                @Override
                public List<AuditZnsbQuestioninfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                        sql.eq("questionnaireguid", dataBean.getRowguid());
                    }
                    sql.setSelectFields("question,questiontype,sort");
                    PageData<AuditZnsbQuestioninfo> pageData = questioninfoService
                            .findListByQuestionnairerowguid(sql.getMap(), first, pageSize, "sort", "desc").getResult();
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditZnsbQuestionnaire getDataBean() {
        return dataBean;
    }
}
