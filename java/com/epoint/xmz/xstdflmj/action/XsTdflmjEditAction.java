package com.epoint.xmz.xstdflmj.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xstdflmj.api.entity.XsTdflmj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xstdflmj.api.IXsTdflmjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土土地分类面积修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:56]
 */
@RightRelation(XsTdflmjListAction.class)
@RestController("xstdflmjeditaction")
@Scope("request")
public class XsTdflmjEditAction extends BaseController {

	@Autowired
	private IXsTdflmjService service;

	/**
	 * 国土土地分类面积实体对象
	 */
	private XsTdflmj dataBean = null;

	/**
	 * 县级行政区代码下拉列表model
	 */
	private List<SelectItem> xjxzqdmModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new XsTdflmj();
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

	public XsTdflmj getDataBean() {
		return dataBean;
	}

	public void setDataBean(XsTdflmj dataBean) {
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
