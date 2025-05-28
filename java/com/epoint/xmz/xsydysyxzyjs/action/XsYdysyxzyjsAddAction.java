package com.epoint.xmz.xsydysyxzyjs.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsydysyxzyjs.api.entity.XsYdysyxzyjs;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xsydysyxzyjs.api.IXsYdysyxzyjsService;

/**
 * 国土用地预审与选址意见书新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:36]
 */
@RightRelation(XsYdysyxzyjsListAction.class)
@RestController("xsydysyxzyjsaddaction")
@Scope("request")
public class XsYdysyxzyjsAddAction extends BaseController {
	@Autowired
	private IXsYdysyxzyjsService service;
	/**
	 * 国土用地预审与选址意见书实体对象
	 */
	private XsYdysyxzyjs dataBean = null;

	/**
	 * 行政区代码下拉列表model
	 */
	private List<SelectItem> xzqdmModel = null;

	public void pageLoad() {
		dataBean = new XsYdysyxzyjs();
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
		dataBean = new XsYdysyxzyjs();
	}

	public XsYdysyxzyjs getDataBean() {
		if (dataBean == null) {
			dataBean = new XsYdysyxzyjs();
		}
		return dataBean;
	}

	public void setDataBean(XsYdysyxzyjs dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getXzqdmModel() {
		if (xzqdmModel == null) {
			xzqdmModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "企业所属辖区", null, false));
		}
		return this.xzqdmModel;
	}

}
