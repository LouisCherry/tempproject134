package com.epoint.xmz.certhwslysjyxk.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 省际普通货物水路运输经营许可本地库详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@RightRelation(CertHwslysjyxkListAction.class)
@RestController("certhwslysjyxkdetailaction")
@Scope("request")
public class CertHwslysjyxkDetailAction  extends BaseController
{
	  @Autowired
      private ICertHwslysjyxkService service; 
    
    /**
     * 省际普通货物水路运输经营许可本地库实体对象
     */
  	private CertHwslysjyxk dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertHwslysjyxk();  
		  }
    }
   
   
	      public CertHwslysjyxk getDataBean()
	      {
	          return dataBean;
	      }
}