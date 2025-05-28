package com.epoint.xmz.gtuser.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.gtuser.api.entity.GtUser;
import com.epoint.xmz.spglgt.util.RsaUtil;
import com.epoint.xmz.spglgt.util.SignUtil;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.gtuser.api.IGtUserService;

/**
 * 国土用户管理表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@RightRelation(GtUserListAction.class)
@RestController("gtuseraddaction")
@Scope("request")
public class GtUserAddAction extends BaseController {
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
		dataBean = new GtUser();
	}

	/**
	 * 保存并关闭
	 * 
	 */
	public void add() {
		String KeyString = RsaUtil.generateKeyPair();
		String publicKeyString = "";
		String privateKeyString = "";
		if (KeyString.contains("_SPLIT_")) {
			String[] strings = KeyString.split("_SPLIT_");
			publicKeyString = strings[0];
			privateKeyString = strings[1];
		}
		privateKeyString = SignUtil.sign("12345", privateKeyString);
		dataBean.setSign(privateKeyString);
		dataBean.setPublickey(publicKeyString);
		
		dataBean.setRowguid(UUID.randomUUID().toString());
		dataBean.setOperatedate(new Date());
		dataBean.setOperateusername(userSession.getDisplayName());
		
		dataBean.setStage("XS");
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
		dataBean = new GtUser();
	}

	public GtUser getDataBean() {
		if (dataBean == null) {
			dataBean = new GtUser();
		}
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
