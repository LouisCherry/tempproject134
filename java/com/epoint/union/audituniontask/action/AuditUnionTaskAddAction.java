package com.epoint.union.audituniontask.action;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.union.audituniontask.api.entity.AuditUnionTask;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.union.audituniontask.api.IAuditUnionTaskService;

/**
 * 异地通办事项配置表新增页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 16:59:46]
 */
@RightRelation(AuditUnionTaskListAction.class)
@RestController("audituniontaskaddaction")
@Scope("request")
public class AuditUnionTaskAddAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private IAuditUnionTaskService service;
	/**
	 * 异地通办事项配置表实体对象
	 */
	private AuditUnionTask dataBean = null;


	public void pageLoad() {
		dataBean = new AuditUnionTask();
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		dataBean.setRowguid(UUID.randomUUID().toString());
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		service.insert(dataBean);
		addCallbackParam("msg", "保存成功！");
		dataBean = null;
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditUnionTask();
	}

	public AuditUnionTask getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditUnionTask();
		}
		return dataBean;
	}

	public void setDataBean(AuditUnionTask dataBean) {
		this.dataBean = dataBean;
	}

}
