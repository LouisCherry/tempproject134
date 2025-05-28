package com.epoint.xmz.jnyjsevaluate.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;

/**
 * 一件事评价表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@RightRelation(JnYjsEvaluateListAction.class)
@RestController("jnyjsevaluateaddaction")
@Scope("request")
public class JnYjsEvaluateAddAction extends BaseController {
	@Autowired
	private IJnYjsEvaluateService service;
	/**
	 * 一件事评价表实体对象
	 */
	private JnYjsEvaluate dataBean = null;

	/**
	 * 辖区编码下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;

	public void pageLoad() {
		dataBean = new JnYjsEvaluate();
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
		dataBean = new JnYjsEvaluate();
	}

	public JnYjsEvaluate getDataBean() {
		if (dataBean == null) {
			dataBean = new JnYjsEvaluate();
		}
		return dataBean;
	}

	public void setDataBean(JnYjsEvaluate dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评辖区", null, false));
		}
		return this.areacodeModel;
	}

}
