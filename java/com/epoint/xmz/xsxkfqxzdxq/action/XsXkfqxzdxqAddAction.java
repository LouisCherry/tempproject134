package com.epoint.xmz.xsxkfqxzdxq.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsxkfqxzdxq.api.entity.XsXkfqxzdxq;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xsxkfqxzdxq.api.IXsXkfqxzdxqService;

/**
 * 国土许可分区县占地详情新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:32]
 */
@RightRelation(XsXkfqxzdxqListAction.class)
@RestController("xsxkfqxzdxqaddaction")
@Scope("request")
public class XsXkfqxzdxqAddAction extends BaseController {
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
		dataBean = new XsXkfqxzdxq();
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
		dataBean = new XsXkfqxzdxq();
	}

	public XsXkfqxzdxq getDataBean() {
		if (dataBean == null) {
			dataBean = new XsXkfqxzdxq();
		}
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
