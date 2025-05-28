package com.epoint.zoucheng.znsb.auditqueue.cxj.question.auditznsbquestionnaire.action;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷list页面对应的后台
 *
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@RestController("zcauditznsbquestionnairelistaction")
@Scope("request")
public class ZCAuditZnsbQuestionnaireListAction extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IZCAuditZnsbQuestionnaireService service;

    /**
     * 问卷调查-问卷实体对象
     */
    private AuditZnsbQuestionnaire dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbQuestionnaire> model;

    public void pageLoad() {
    }

    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditZnsbQuestionnaire> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbQuestionnaire>()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditZnsbQuestionnaire> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getQuestionnairename())) {
                        String s = dataBean.getQuestionnairename();
                        if (dataBean.getQuestionnairename().contains("_")
                                || dataBean.getQuestionnairename().contains("%")) {
                            s = dataBean.getQuestionnairename().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
                        }
                        sql.like("questionnairename", s);
                    }
                    sql.setOrderDesc("sort");
                    sql.setOrderDesc("operatedate");
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    PageData<AuditZnsbQuestionnaire> pagedata = service
                            .getQuestionnaireListPageData(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pagedata.getRowCount());
                    return pagedata.getList();
                }
            };
        }
        return model;
    }

    public AuditZnsbQuestionnaire getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbQuestionnaire();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbQuestionnaire dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 匹配替换特殊字符
     *
     * @param target
     *            目标字符串
     * @param extra
     *            忽略替换字符
     * @return
     */
    public static String replaceSpecialChar(String target, String extra) {
        if (target == null) {
            return null;
        }
        String regEx = "[^\\p{L}\\p{Nd}" + extra + "]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(StringUtil.toLowerCase(target));
        String re = m.replaceAll("").trim();
        if ("".equals(re)) {
            return target;
        }
        return re;
    }

}
