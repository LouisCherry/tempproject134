package com.epoint.xmz.xsxkfqxzdxq.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.xsxkfqxzdxq.api.entity.XsXkfqxzdxq;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.xsxkfqxzdxq.api.IXsXkfqxzdxqService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土许可分区县占地详情修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:32]
 */
@RightRelation(XsXkfqxzdxqListAction.class)
@RestController("xsxkfqxzdxqeditaction")
@Scope("request")
public class XsXkfqxzdxqEditAction extends BaseController {

	@Autowired
	private IXsXkfqxzdxqService service;

	/**
	 * 国土许可分区县占地详情实体对象
	 */
	private XsXkfqxzdxq dataBean = null;

	/**
	 * 行政区代码下拉列表model
	 */
	private List<SelectItem> xzqdmModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new XsXkfqxzdxq();
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

	public XsXkfqxzdxq getDataBean() {
		return dataBean;
	}

	public void setDataBean(XsXkfqxzdxq dataBean) {
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
