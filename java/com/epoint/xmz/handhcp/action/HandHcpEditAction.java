package com.epoint.xmz.handhcp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.handhcp.api.entity.HandHcp;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.handhcp.api.IHandHcpService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 手动推送好差评表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-03-29 16:57:08]
 */
@RightRelation(HandHcpListAction.class)
@RestController("handhcpeditaction")
@Scope("request")
public class HandHcpEditAction extends BaseController {

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
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new HandHcp();
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

	public HandHcp getDataBean() {
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
