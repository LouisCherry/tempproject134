package com.epoint.xmz.certcbyyysz.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certcbyyysz.api.ICertCbyyyszService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 船舶营业运输证本地库修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-06-15 14:41:12]
 */
@RightRelation(CertCbyyyszListAction.class)
@RestController("certcbyyyszeditaction")
@Scope("request")
public class CertCbyyyszEditAction extends BaseController {

	@Autowired
	private ICertCbyyyszService service;

	/**
	 * 船舶营业运输证本地库实体对象
	 */
	private CertCbyyysz dataBean = null;

	/**
	 * 是否注销下拉列表model
	 */
	private List<SelectItem> is_cancelModel = null;
	/**
	 * 是否在用下拉列表model
	 */
	private List<SelectItem> is_enableModel = null;
	/**
	 * 是否标准船型下拉列表model
	 */
	private List<SelectItem> sfbzcxModel = null;
	/**
	 * 是否自有船舶下拉列表model
	 */
	private List<SelectItem> sfzycbModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new CertCbyyysz();
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

	public CertCbyyysz getDataBean() {
		return dataBean;
	}

	public void setDataBean(CertCbyyysz dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getIs_cancelModel() {
		if (is_cancelModel == null) {
			is_cancelModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.is_cancelModel;
	}

	public List<SelectItem> getIs_enableModel() {
		if (is_enableModel == null) {
			is_enableModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.is_enableModel;
	}

	public List<SelectItem> getSfbzcxModel() {
		if (sfbzcxModel == null) {
			sfbzcxModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.sfbzcxModel;
	}

	public List<SelectItem> getSfzycbModel() {
		if (sfzycbModel == null) {
			sfzycbModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
		}
		return this.sfzycbModel;
	}

}
