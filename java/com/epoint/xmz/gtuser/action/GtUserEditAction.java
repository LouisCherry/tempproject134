package com.epoint.xmz.gtuser.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtuser.api.entity.GtUser;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.gtuser.api.IGtUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 国土用户管理表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@RightRelation(GtUserListAction.class)
@RestController("gtusereditaction")
@Scope("request")
public class GtUserEditAction extends BaseController {

	@Autowired
	private IGtUserService service;

	/**
	 * 国土用户管理表实体对象
	 */
	private GtUser dataBean = null;

	/**
	 * 群组名称下拉列表model
	 */
	private List<SelectItem> datapackagetypeModel = null;

	public void pageLoad() {
		String guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if (dataBean == null) {
			dataBean = new GtUser();
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

	public GtUser getDataBean() {
		return dataBean;
	}

	public void setDataBean(GtUser dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getDatapackagetypeModel() {
		if (datapackagetypeModel == null) {
			datapackagetypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国土_数据类型编码", null, false));
		}
		return this.datapackagetypeModel;
	}

}
