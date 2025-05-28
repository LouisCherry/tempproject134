package com.epoint.xmz.jnyjsevaluate.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一件事评价表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@RightRelation(JnYjsEvaluateListAction.class)
@RestController("jnyjsevaluateeditaction")
@Scope("request")
public class JnYjsEvaluateEditAction extends BaseController {

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
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new JnYjsEvaluate();
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

	public JnYjsEvaluate getDataBean() {
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
