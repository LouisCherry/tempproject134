package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IAuditTaskShareFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 事项登记list页面对应的后台
 *
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("jnsharefilelistaction")
@Scope("request")
public class JnShareFileListAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private DataGridModel<AuditTaskShareFile> model;

    private AuditTaskShareFile dateBean;

    private String taskname;

    @Autowired
    private IAuditTaskShareFile shareService;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<AuditTaskShareFile> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskShareFile>() {

                @Override
                public List<AuditTaskShareFile> fetchData(int first, int pageSize, String sortField,
                                                          String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();

                    if (StringUtil.isNotBlank(taskname)) {
                        sql.like("taskname", taskname);
                    }

                    String ouguid = userSession.getBaseOUGuid();

                    sql.eq("ouguid", ouguid);

                    sortField = "updatetime";
                    sortOrder = "desc";

                    PageData<AuditTaskShareFile> pageData = shareService.getShareFilePageData(
                            sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public AuditTaskShareFile getDateBean() {
        return dateBean;
    }

    public void setDateBean(AuditTaskShareFile dateBean) {
        this.dateBean = dateBean;
    }

}
