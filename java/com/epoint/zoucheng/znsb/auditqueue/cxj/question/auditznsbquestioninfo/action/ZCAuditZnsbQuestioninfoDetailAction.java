package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestioninfo.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;

/**
 * 问卷调查-问题详情页面对应的后台
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:17]
 */
@RightRelation(ZCAuditZnsbQuestioninfoListAction.class)
@RestController("zcauditznsbquestioninfodetailaction")
@Scope("request")
public class ZCAuditZnsbQuestioninfoDetailAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestioninfoService service;
    @Autowired
    private IZCAuditZnsbQuestiondetailService questiondetailService;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestiondetail> model;

    /**
     * 问卷调查-问题实体对象
     */
    private AuditZnsbQuestioninfo dataBean = null;
    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestioninfo();
        }
    }
    @SuppressWarnings("serial")
    public DataGridModel<AuditZnsbQuestiondetail> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestiondetail>() {

                @Override
                public List<AuditZnsbQuestiondetail> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                        sql.eq("questionguid", dataBean.getRowguid());
                    }
                    sql.setSelectFields("answeroption,sort");
                    sql.setOrderDesc("sort");
                    PageData<AuditZnsbQuestiondetail> questiondata = questiondetailService.findListByQuestionInfoguid(sql.getMap(), first, pageSize, "sort", "desc").getResult();
                    return questiondata.getList();
                }
            };
        }
        return model;
    }


    public AuditZnsbQuestioninfo getDataBean() {
        return dataBean;
    }
}
