package com.epoint.zoucheng.device.infopub.webinfo.action;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.device.infopub.webinfo.api.IInfopubWebinfoService;
import com.epoint.zoucheng.device.infopub.webinfo.api.entity.InfopubWebinfo;
/**
 * 网页信息表修改页面对应的后台
 * 
 * @author why
 * @version [版本号, 2019-09-17 11:17:19]
 */
@RightRelation(InfopubWebinfoListAction.class)
@RestController("infopubwebinfoeditaction")
@Scope("request")
public class InfopubWebinfoEditAction  extends BaseController
{

	@Autowired
	private IInfopubWebinfoService service;
    
    /**
     * 网页信息表实体对象
     */
  	private InfopubWebinfo dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new InfopubWebinfo();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public InfopubWebinfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(InfopubWebinfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
