package com.epoint.jnycsl.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.jnycsl.dto.FromPage;


/**
 * 泰安基础action，专门让其他类继承的，便于增删改查四个页面绑定一个action
 * @author 刘雨雨
 * @time 2018年9月12日下午3:23:01
 */
public abstract class TaianBaseAction extends BaseController {
	
	private static final long serialVersionUID = -5971636717700656312L;
	
	/** 来自那个页面 可能的值 list,edit,detail,add,为空则认为它是list*/
	protected static final String FROM_PAGE = "fromPage";

	protected void init(String fromPage) {
		if (FromPage.ADD.equals(fromPage)) {
			initAdd();
		} else if (FromPage.DETAIL.equals(fromPage)) {
			initDetail();
		} else if (FromPage.EDIT.equals(fromPage)) {
			initEdit();
		} else if (FromPage.lIST.equals(fromPage)) {
			initList();
		} else {
			initList();
		}
	}
	
	protected abstract void initList();

	protected abstract void initEdit();

	protected abstract void initDetail();

	protected abstract void initAdd();
}
