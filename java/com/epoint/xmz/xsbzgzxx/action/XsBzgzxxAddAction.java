package com.epoint.xmz.xsbzgzxx.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsbzgzxx.api.entity.XsBzgzxx;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xsbzgzxx.api.IXsBzgzxxService;

/**
 * 国土补正告知新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:12]
 */
@RightRelation(XsBzgzxxListAction.class)
@RestController("xsbzgzxxaddaction")
@Scope("request")
public class XsBzgzxxAddAction extends BaseController {
	@Autowired
	private IXsBzgzxxService service;
	/**
	 * 国土补正告知实体对象
	 */
	private XsBzgzxx dataBean = null;

	/**
	 * 行政区名称下拉列表model
	 */
	private List<SelectItem> xzqmcModel = null;

	public void pageLoad() {
		dataBean = new XsBzgzxx();
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
		dataBean = new XsBzgzxx();
	}

	public XsBzgzxx getDataBean() {
		if (dataBean == null) {
			dataBean = new XsBzgzxx();
		}
		return dataBean;
	}

	public void setDataBean(XsBzgzxx dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getXzqmcModel() {
		if (xzqmcModel == null) {
			xzqmcModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "企业所属辖区", null, false));
		}
		return this.xzqmcModel;
	}

}
