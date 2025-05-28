package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
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
@RestController("jntaskselectaction")
@Scope("request")
public class JnTaskSelectAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 常量：事项编码区分
     */
    private static final String CONSTANT_ZERO = "0";
    /**
     * 常量：大小项配置表区分
     */
    private static final String CONSTANT_ONE = "1";
    /**
     * 常量：不区分大小项
     */
    private static final String CONSTANT_TWO = "2";
    /**
     * AS_ITEM_ID_LENGTH是否配置
     */
    private String isexititemid = CONSTANT_ONE;
    /**
     * AS_ITEM_ID_LENGTH是否配置正确
     */
    private String isitemidright = CONSTANT_ONE;

    /**
     * 审批事项基本信息实体对象
     */
    private AuditTask dataBean = new AuditTask();

    private String condition;
    /**
     * 领域树
     */
    private TreeModel treeModel;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * 事项大小项分类配置
     */
    private String astaskcategory;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTask> model;
    /**
     * 获取系统参数
     */
    @Autowired
    private IHandleConfig auditCenterConfigService;
    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;
    /**
     * 父项长度
     */
    private String parentlen;
    /**
     * 子项长度
     */
    private String sublen;

    @Autowired
    private IAuditTask auditService;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        // 事项大小项分类配置
        String str = auditCenterConfigService.getFrameConfig("AS_Task_Category", "").getResult();
        astaskcategory = StringUtil.isBlank(str) ? CONSTANT_TWO : str;
        // 事项编码区分大小项
        if (CONSTANT_ZERO.equals(astaskcategory)) {
            String itemlen = auditCenterConfigService.getFrameConfig("AS_ITEM_ID_LENGTH", "").getResult();
            if (StringUtil.isNotBlank(itemlen)) {
                String[] arrlen = itemlen.split(";");
                if (arrlen.length == 2) {
                    parentlen = arrlen[0];
                    sublen = arrlen[1];
                } else {
                    isitemidright = CONSTANT_ZERO;
                }
            } else {
                isexititemid = CONSTANT_ZERO;
            }
        }
    }

    public DataGridModel<AuditTask> getDataGridData() {

        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTask>() {

                @Override
                public List<AuditTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getTaskname())) {
                        sql.like("taskname", dataBean.getTaskname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getItem_id())) {
                        sql.like("item_id", dataBean.getItem_id());
                    }
                    sql.eq("ouguid", userSession.getBaseOUGuid());
                    sql.eq("IS_EDITAFTERIMPORT", "1");
                    sql.isBlankOrValue("is_history", "0");
                    sql.eq("is_enable", "1");
                    PageData<AuditTask> pageData = null;
                    pageData = auditService.getAuditTaskPageData(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public AuditTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditTask dataBean) {
        this.dataBean = dataBean;
    }
}
