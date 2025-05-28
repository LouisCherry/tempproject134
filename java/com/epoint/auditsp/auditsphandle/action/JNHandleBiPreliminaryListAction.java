package com.epoint.auditsp.auditsphandle.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 申报查询列表页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnhandlebipreliminarylistaction")
@Scope("request")
public class JNHandleBiPreliminaryListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;
    private AuditSpBusiness dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;
    /**
     * 建设单位
     */
    private String applyername;
    /**
     * 申报名称
     */
    private String itemname;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = -4807579952328718148L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 页面上搜索条件
                    if (StringUtil.isNotBlank(applyername)) {
                        sql.like("subapp.applyername", applyername);
                    }
                    sql.eq("status", "5");
                    if (StringUtil.isNotBlank(itemname)) {
                        sql.like("businessname", itemname);
                    }
                    // 2023/8/28 任城区/太白湖新区 增加查看市本级项目的权限
                    String areacode = ZwfwUserSession.getInstance().getAreaCode();
                    if ("370811".equals(areacode) || "370891".equals(areacode)) {
                        sql.eq("(areacode", new StringBuilder().append(areacode)
                                .append("' or (areacode='370800' and phase.phasename='竣工验收')) and '1=1").toString());
                    }
                    else {
                        sql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    }
                    // sql.eq("areacode",
                    // ZwfwUserSession.getInstance().getAreaCode());
                    PageData<Record> pageData = iAuditSpISubapp.getWaitHandleSubapp(first, pageSize, sql.getMap())
                            .getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
