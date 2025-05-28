package com.epoint.xmz.zjknowledge.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.zjknowledge.api.entity.ZjKnowledge;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.zjknowledge.api.IZjKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自建系统知识库表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-25 15:12:09]
 */
@RightRelation(ZjKnowledgeListAction.class)
@RestController("zjknowledgeeditaction")
@Scope("request")
public class ZjKnowledgeEditAction extends BaseController {

	@Autowired
	private IZjKnowledgeService service;

	/**
	 * 自建系统知识库表实体对象
	 */
	private ZjKnowledge dataBean = null;

	/**
	 * 问题类型下拉列表model
	 */
	private List<SelectItem> typeModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new ZjKnowledge();
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

	public ZjKnowledge getDataBean() {
		return dataBean;
	}

	public void setDataBean(ZjKnowledge dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getTypeModel() {
		if (typeModel == null) {
			typeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "自建问题类型", null, false));
		}
		return this.typeModel;
	}

}
