package com.epoint.auditperformance.auditperformancecomplain.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancecomplain.api.IAuditPerformanceComplain;
import com.epoint.basic.auditperformance.auditperformancecomplain.domain.AuditPerformanceComplain;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 考评规则表list页面对应的后台
 * 
 * @version [版本号, 2018-01-09 10:05:48]
 */
@RestController("auditperformancecomplainlistaction")
@Scope("request")
public class AuditPerformanceComplainListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAuditPerformanceComplain complainService;

	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditPerformanceComplain> model;

	private AuditPerformanceComplain performanceComplain;

	@Override
	public void pageLoad() {
        performanceComplain = new AuditPerformanceComplain();
        if(StringUtil.isNotBlank(getRequestParameter("processstatus"))){
            performanceComplain.setProcessstatus(getRequestParameter("processstatus"));
        }
	}

	/**
	 * 删除选定
	 * 
	 */
	public void deleteSelect(String rowguid) {
		complainService.deleteComplainByRowGuid(rowguid);
		addCallbackParam("msg", "成功删除！");
	}

	public DataGridModel<AuditPerformanceComplain> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditPerformanceComplain>() {

				@Override
				public List<AuditPerformanceComplain> fetchData(int first, int pageSize, String sortField,
						String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
                    if(StringUtil.isNotBlank(performanceComplain.getProcessstatus()) && !"all".equals(performanceComplain.getProcessstatus())){
                    	sql.eq("processstatus", performanceComplain.getProcessstatus());
					}
					if(ZwfwConstant.CONSTANT_STR_ONE.equals(getRequestParameter("iscenter"))){
						sql.eq("a.centerguid", ZwfwUserSession.getInstance().getCenterGuid());
						if("all".equals(performanceComplain.getProcessstatus())){
							sql.in("processstatus", "'1','2'");
						}
					}else{
						sql.eq("complainuserguid", userSession.getUserGuid());
					}
                    sql.setLeftJoinTable("audit_performance_account b", "a.accountrowguid","b.rowguid");
					sql.setSelectFields("a.*,b.recordrulename,b.recorddetailrulename,b.groupscore,b.rowguid accountguid,b.recordname");
					sql.setOrderAsc("a.processstatus");
					sql.setOrderDesc("a.operatedate");
                    if(StringUtil.isNotBlank(performanceComplain.getStr("recordname"))){
                        sql.like("b.recordname", performanceComplain.getStr("recordname"));
                    }
                    if(StringUtil.isNotBlank(performanceComplain.getComplainusername())){
                        sql.like("a.complainusername", performanceComplain.getComplainusername());
                    }
					PageData<AuditPerformanceComplain> pageData = complainService
							.selectPerformanceComplainByPage(sql.getMap(), first, pageSize, sortField, sortOrder)
							.getResult();
					this.setRowCount(pageData.getRowCount());
					List<AuditPerformanceComplain> performanceComplains = pageData.getList();
                    return performanceComplains;
				}
			};
		}
		return model;
    }

    public AuditPerformanceComplain getPerformanceComplain() {
        return performanceComplain;
    }

    public void setPerformanceComplain(AuditPerformanceComplain performanceComplain) {
        this.performanceComplain = performanceComplain;
    }
}
