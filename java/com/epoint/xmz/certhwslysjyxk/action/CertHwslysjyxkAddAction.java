package com.epoint.xmz.certhwslysjyxk.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 省际普通货物水路运输经营许可本地库新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@RightRelation(CertHwslysjyxkListAction.class)
@RestController("certhwslysjyxkaddaction")
@Scope("request")
public class CertHwslysjyxkAddAction extends BaseController {
	@Autowired
	private ICertHwslysjyxkService service;
	/**
	 * 省际普通货物水路运输经营许可本地库实体对象
	 */
	private CertHwslysjyxk dataBean = null;

	/**
	 * 经营方式下拉列表model
	 */
	private List<SelectItem> jyfsModel = null;
	/**
	 * 经营区域下拉列表model
	 */
	private List<SelectItem> jyqyModel = null;
	/**
	 * 内河沿海下拉列表model
	 */
	private List<SelectItem> nhyhModel = null;

	private List<SelectItem> isCancelModel;

	public void pageLoad() {
		dataBean = new CertHwslysjyxk();
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		dataBean.setJyxkzbh(dataBean.getBh());
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
		dataBean = new CertHwslysjyxk();
	}

	public CertHwslysjyxk getDataBean() {
		if (dataBean == null) {
			dataBean = new CertHwslysjyxk();
		}
		return dataBean;
	}

	public void setDataBean(CertHwslysjyxk dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getJyfsModel() {
		if (jyfsModel == null) {
			jyfsModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "港航经营许可证经营方式", null, false));
		}
		return this.jyfsModel;
	}

	public List<SelectItem> getJyqyModel() {
		if (jyqyModel == null) {
			jyqyModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "港航经营许可证经营区域", null, false));
		}
		return this.jyqyModel;
	}

	public List<SelectItem> getNhyhModel() {
		if (nhyhModel == null) {
			nhyhModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "港航经营许可证内河沿海", null, false));
		}
		return this.nhyhModel;
	}

	public List<SelectItem> getIsCancelModel() {
		if (isCancelModel == null) {
			isCancelModel = DataUtil.convertMap2ComboBox(
					CodeModalFactory.factory("下拉列表", "是否注销", null, false)
			);
		}
		return isCancelModel;
	}

}
