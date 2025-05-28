package com.epoint.xmz.xstkdk.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xstkdk.api.entity.XsTkdk;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xstkdk.api.IXsTkdkService;

/**
 * 国土踏勘地块新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:19:01]
 */
@RightRelation(XsTkdkListAction.class)
@RestController("xstkdkaddaction")
@Scope("request")
public class XsTkdkAddAction extends BaseController {
	@Autowired
	private IXsTkdkService service;
	/**
	 * 国土踏勘地块实体对象
	 */
	private XsTkdk dataBean = null;

	/**
	 * 县级行政区代码下拉列表model
	 */
	private List<SelectItem> xjxzqdmModel = null;

	public void pageLoad() {
		dataBean = new XsTkdk();
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
		dataBean = new XsTkdk();
	}

	public XsTkdk getDataBean() {
		if (dataBean == null) {
			dataBean = new XsTkdk();
		}
		return dataBean;
	}

	public void setDataBean(XsTkdk dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getXjxzqdmModel() {
		if (xjxzqdmModel == null) {
			xjxzqdmModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "企业所属辖区", null, false));
		}
		return this.xjxzqdmModel;
	}

}
