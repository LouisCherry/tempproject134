package com.epoint.jn.inproject.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.ces.jnbuildpart.action.JnBuildPartListAction;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.jn.externalprojectinfoext.api.IExternalProjectInfoExtService;
import com.epoint.jn.externalprojectinfoext.api.entity.ExternalProjectInfoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部办件详情页面对应的后台
 *
 */
@RightRelation(JnBuildPartListAction.class)
@RestController("inprojectbyoudetailaction")
@Scope("request")
public class InProjectByOuDetailAction extends BaseController
{
	@Autowired
	private IExternalProjectInfoExtService service;

	/**
	 * 建筑业企业资质数据库实体对象
	 */
	private ExternalProjectInfoExt dataBean=null;

	public void pageLoad()
	{
		String	guid = getRequestParameter("guid");
		dataBean = service.find(guid);
		if(dataBean==null){
			dataBean=new ExternalProjectInfoExt();
		}
	}


	public ExternalProjectInfoExt getDataBean()
	{
		return dataBean;
	}
}