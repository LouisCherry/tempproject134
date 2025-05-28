package com.epoint.xmz.yjsczcapplyer.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一件事住租车申请人信息表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-18 15:38:32]
 */
@RightRelation(YjsCzcApplyerListAction.class)
@RestController("yjsczcapplyereditaction")
@Scope("request")
public class YjsCzcApplyerEditAction extends BaseController {

	@Autowired
	private IYjsCzcApplyerService service;

	/**
	 * 一件事住租车申请人信息表实体对象
	 */
	private YjsCzcApplyer dataBean = null;

	/**
	 * 性别下拉列表model
	 */
	private List<SelectItem> sexModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new YjsCzcApplyer();
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

	public YjsCzcApplyer getDataBean() {
		return dataBean;
	}

	public void setDataBean(YjsCzcApplyer dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getSexModel() {
		if (sexModel == null) {
			sexModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "性别", null, false));
		}
		return this.sexModel;
	}

}
