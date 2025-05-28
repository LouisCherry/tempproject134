package com.epoint.xmz.xstkdk.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstkdk.api.entity.XsTkdk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstkdk.api.IXsTkdkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土踏勘地块修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:19:01]
 */
@RightRelation(XsTkdkListAction.class)
@RestController("xstkdkeditaction")
@Scope("request")
public class XsTkdkEditAction extends BaseController {

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
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new XsTkdk();
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

	public XsTkdk getDataBean() {
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
