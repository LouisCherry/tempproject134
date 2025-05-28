package com.epoint.xmz.yjsczcapplyer.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;

/**
 * 一件事住租车申请人信息表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-18 15:38:32]
 */
@RightRelation(YjsCzcApplyerListAction.class)
@RestController("yjsczcapplyeraddaction")
@Scope("request")
public class YjsCzcApplyerAddAction extends BaseController {
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
		dataBean = new YjsCzcApplyer();
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
		dataBean = new YjsCzcApplyer();
	}

	public YjsCzcApplyer getDataBean() {
		if (dataBean == null) {
			dataBean = new YjsCzcApplyer();
		}
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
