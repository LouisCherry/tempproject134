package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestiondetail.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;

/**
 * 问卷调查-问题详情list页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:10]
 */
@RestController("zcauditznsbquestiondetaillistaction")
@Scope("request")
public class ZCAuditZnsbQuestiondetailListAction extends BaseController
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestiondetailService service;

    /**
     * 问卷调查-问题详情实体对象
     */
    private AuditZnsbQuestiondetail dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestiondetail> model;

    private String questionguid;

    public void pageLoad() {
        questionguid = getRequestParameter("questionguid");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditZnsbQuestiondetail> getDataGridData() {

        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestiondetail>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditZnsbQuestiondetail> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(questionguid)) {
                        sql.eq("questionguid", questionguid);
                    }
                    if (StringUtil.isNotBlank(dataBean.getAnsweroption())) {
                        sql.like("answeroption", dataBean.getAnsweroption());
                    }
                    sql.setOrderDesc("sort");
                    PageData<AuditZnsbQuestiondetail> pageData = service
                            .findListByQuestionInfoguid(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();

                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
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
