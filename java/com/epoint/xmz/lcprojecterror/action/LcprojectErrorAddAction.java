package com.epoint.xmz.lcprojecterror.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.lcprojecterror.api.entity.LcprojectError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.lcprojecterror.api.ILcprojectErrorService;

/**
 * 浪潮推送失败记录表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@RightRelation(LcprojectErrorListAction.class)
@RestController("lcprojecterroraddaction")
@Scope("request")
public class LcprojectErrorAddAction extends BaseController {
	@Autowired
	private ILcprojectErrorService service;
	/**
	 * 浪潮推送失败记录表实体对象
	 */
	private LcprojectError dataBean = null;

	/**
	 * 辖区编码下拉列表model
	 */
	private List<SelectItem> areacodeModel = null;

	public void pageLoad() {
		dataBean = new LcprojectError();
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
		dataBean = new LcprojectError();
	}

	public LcprojectError getDataBean() {
		if (dataBean == null) {
			dataBean = new LcprojectError();
		}
		return dataBean;
	}

	public void setDataBean(LcprojectError dataBean) {
		this.dataBean = dataBean;
	}

	public List<SelectItem> getAreacodeModel() {
		if (areacodeModel == null) {
			areacodeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, false));
		}
		return this.areacodeModel;
	}

}
