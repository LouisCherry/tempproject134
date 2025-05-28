package com.epoint.xmz.handhcp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.handhcp.api.entity.HandHcp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.handhcp.api.IHandHcpService;

/**
 * 手动推送好差评表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-03-29 16:57:08]
 */
@RightRelation(HandHcpListAction.class)
@RestController("handhcpaddaction")
@Scope("request")
public class HandHcpAddAction extends BaseController {
	@Autowired
	private IHandHcpService service;
	/**
	 * 手动推送好差评表实体对象
	 */
	private HandHcp dataBean = null;

	/**
	 * 所属辖区下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;
	/**
	 * 推送标识下拉列表model
	 */
	private List<SelectItem> sbsignModel = null;
	/**
	 * 服务次数下拉列表model
	 */
	private List<SelectItem> servicenumberModel = null;

	public void pageLoad() {
		dataBean = new HandHcp();
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
		dataBean = new HandHcp();
	}

	public HandHcp getDataBean() {
		if (dataBean == null) {
			dataBean = new HandHcp();
		}
		return dataBean;
	}

	public void setDataBean(HandHcp dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评辖区", null, false));
		}
		return this.areacodeModel;
	}

	public List<SelectItem> getSbsignModel() {
		if (sbsignModel == null) {
			sbsignModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.sbsignModel;
	}

	public List<SelectItem> getServicenumberModel() {
		if (servicenumberModel == null) {
			servicenumberModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "好差评服务次数", null, false));
		}
		return this.servicenumberModel;
	}

}
