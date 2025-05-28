package com.epoint.xmz.xsydysyxzyjs.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsydysyxzyjs.api.entity.XsYdysyxzyjs;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsydysyxzyjs.api.IXsYdysyxzyjsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土用地预审与选址意见书修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:36]
 */
@RightRelation(XsYdysyxzyjsListAction.class)
@RestController("xsydysyxzyjseditaction")
@Scope("request")
public class XsYdysyxzyjsEditAction extends BaseController {

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
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new XsYdysyxzyjs();
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

	public XsYdysyxzyjs getDataBean() {
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
