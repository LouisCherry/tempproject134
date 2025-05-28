package com.epoint.xmz.certggxkws.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;

/**
 * 公共许可卫生证照库新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-12 17:01:05]
 */
@RightRelation(CertGgxkwsListAction.class)
@RestController("certggxkwsaddaction")
@Scope("request")
public class CertGgxkwsAddAction extends BaseController {
	@Autowired
	private ICertGgxkwsService service;
	/**
	 * 公共许可卫生证照库实体对象
	 */
	private CertGgxkws dataBean = null;

	/**
	 * 申请类别下拉列表model
	 */
	private List<SelectItem> applytypeModel = null;
	/**
	 * 所属区县下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;
	/**
	 * 证件名称下拉列表model
	 */
	private List<SelectItem> certtypeModel = null;
	/**
	 * 经济类型下拉列表model
	 */
	private List<SelectItem> econtypeModel = null;
	/**
	 * 是否注销下拉列表model
	 */
	private List<SelectItem> is_canaleModel = null;
	/**
	 * 是否在用下拉列表model
	 */
	private List<SelectItem> is_enableModel = null;
	/**
	 * 集中空调通风系统下拉列表model
	 */
	private List<SelectItem> ishvacModel = null;
	/**
	 * 专业类别下拉列表model
	 */
	private List<SelectItem> majortypeModel = null;
	/**
	 * 饮用水下拉列表model
	 */
	private List<SelectItem> watertypeModel = null;

	public void pageLoad() {
		dataBean = new CertGgxkws();
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		dataBean.setIs_enable("1");
		dataBean.setVersion("0");
		dataBean.setIs_canale("0");
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
		dataBean = new CertGgxkws();
	}

	public CertGgxkws getDataBean() {
		if (dataBean == null) {
			dataBean = new CertGgxkws();
		}
		return dataBean;
	}

	public void setDataBean(CertGgxkws dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getApplytypeModel() {
		if (applytypeModel == null) {
			applytypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请方式", null, false));
		}
		return this.applytypeModel;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属区县", null, false));
		}
		return this.areacodeModel;
	}

	public List<SelectItem> getCerttypeModel() {
		if (certtypeModel == null) {
			certtypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "身份证件类型", null, false));
		}
		return this.certtypeModel;
	}

	public List<SelectItem> getEcontypeModel() {
		if (econtypeModel == null) {
			econtypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "经济类型", null, false));
		}
		return this.econtypeModel;
	}

	public List<SelectItem> getIs_canaleModel() {
		if (is_canaleModel == null) {
			is_canaleModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.is_canaleModel;
	}

	public List<SelectItem> getIs_enableModel() {
		if (is_enableModel == null) {
			is_enableModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.is_enableModel;
	}

	public List<SelectItem> getIshvacModel() {
		if (ishvacModel == null) {
			ishvacModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.ishvacModel;
	}

	public List<SelectItem> getMajortypeModel() {
		if (majortypeModel == null) {
			majortypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "专家库_评标专业", null, false));
		}
		return this.majortypeModel;
	}

	public List<SelectItem> getWatertypeModel() {
		if (watertypeModel == null) {
			watertypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "中心类别", null, false));
		}
		return this.watertypeModel;
	}

}
