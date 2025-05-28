package com.epoint.xmz.xsbzgzxx.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsbzgzxx.api.entity.XsBzgzxx;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsbzgzxx.api.IXsBzgzxxService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土补正告知修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:12]
 */
@RightRelation(XsBzgzxxListAction.class)
@RestController("xsbzgzxxeditaction")
@Scope("request")
public class XsBzgzxxEditAction extends BaseController {

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
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new XsBzgzxx();
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

	public XsBzgzxx getDataBean() {
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
