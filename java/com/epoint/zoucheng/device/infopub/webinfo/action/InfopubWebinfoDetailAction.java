package com.epoint.zoucheng.device.infopub.webinfo.action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.device.infopub.webinfo.api.IInfopubWebinfoService;
import com.epoint.zoucheng.device.infopub.webinfo.api.entity.InfopubWebinfo;

/**
 * 网页信息表详情页面对应的后台
 * 
 * @author why
 * @version [版本号, 2019-09-17 11:17:19]
 */
@RightRelation(InfopubWebinfoListAction.class)
@RestController("infopubwebinfodetailaction")
@Scope("request")
public class InfopubWebinfoDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
      private IInfopubWebinfoService service; 
    
    /**
     * 网页信息表实体对象
     */
  	private InfopubWebinfo dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new InfopubWebinfo();  
		  }
    }
   
   
	      public InfopubWebinfo getDataBean()
	      {
	          return dataBean;
	      }
}