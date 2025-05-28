package com.epoint.union.auditunionproject.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.union.auditunionproject.api.entity.AuditUnionProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.union.auditunionproject.api.IAuditUnionProjectService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 异地通办办件信息表修改页面对应的后台
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@RightRelation(AuditUnionProjectListAction.class)
@RestController("auditunionprojecteditaction")
@Scope("request")
public class AuditUnionProjectEditAction extends BaseController {

	@Autowired
	private IAuditUnionProjectService service;

	/**
	 * 异地通办办件信息表实体对象
	 */
	private AuditUnionProject dataBean = null;

	/**
	 * 证件类型下拉列表model
	 */
	private List<SelectItem> certtypeModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new AuditUnionProject();
		}
	}

	/**
	 * 保存修改
	 * 
	 */
	public void save() {
		dataBean.setOperatedate(new Date());
		service.update(dataBean);
		addCallbackParam("msg", "修改成功！");
	}

	public AuditUnionProject getDataBean() {
		return dataBean;
	}

	public void setDataBean(AuditUnionProject dataBean) {
		this.dataBean = dataBean;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getCerttypeModel() {
		if (certtypeModel == null) {
			certtypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证件类型", null, false));
		}
		return this.certtypeModel;
	}

}
