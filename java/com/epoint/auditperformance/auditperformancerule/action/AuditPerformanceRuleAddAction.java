package com.epoint.auditperformance.auditperformancerule.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerule.api.IAuditPerformanceRule;
import com.epoint.basic.auditperformance.auditperformancerule.domain.AuditPerformanceRule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 考评规则表新增页面对应的后台
 * 
 * @version [版本号, 2018-01-09 10:05:48]
 */
@RestController("auditperformanceruleaddaction")
@Scope("request")
public class AuditPerformanceRuleAddAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private IAuditPerformanceRule service;
	/**
	 * 考评规则表实体对象
	 */
	private AuditPerformanceRule dataBean = null;
	
	 /**
     * 考评对象类别下拉列表model
     */
    private List<SelectItem> objecttypeModel = null;

    
    @Override
	public void pageLoad() {
		dataBean = new AuditPerformanceRule();
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
	    SqlConditionUtil sql = new SqlConditionUtil();
	    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
	    sql.eq("rulename", dataBean.getRulename());
	    Integer num = service.findFieldSame(sql.getMap()).getResult();
        if (num > 0) {
            addCallbackParam("same", "考评规则名称已存在！");
            return;
        }else{
		dataBean.setRowguid(UUID.randomUUID().toString());
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		dataBean.setRulename(dataBean.getRulename().replaceAll(",", "，"));
		dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
		service.insertPerformanceRule(dataBean);
		addCallbackParam("msg", "保存成功！");
		addCallbackParam("ruleguid", dataBean.getRowguid());
        }
		dataBean = null;
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditPerformanceRule();
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getObjecttypeModel() {
        if (objecttypeModel == null) {
        	objecttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "考评对象类别", null, false));
        }
        return this.objecttypeModel;
    }
	
	public AuditPerformanceRule getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditPerformanceRule();
		}
		return dataBean;
	}

	public void setDataBean(AuditPerformanceRule dataBean) {
		this.dataBean = dataBean;
	}

}
