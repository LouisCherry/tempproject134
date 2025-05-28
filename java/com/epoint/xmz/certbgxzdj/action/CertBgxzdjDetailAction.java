package com.epoint.xmz.certbgxzdj.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certbgxzdj.api.entity.CertBgxzdj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certbgxzdj.api.ICertBgxzdjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 变更性质登记库详情页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:42]
 */
@RightRelation(CertBgxzdjListAction.class)
@RestController("certbgxzdjdetailaction")
@Scope("request")
public class CertBgxzdjDetailAction  extends BaseController
{
	  @Autowired
      private ICertBgxzdjService service; 
    
    /**
     * 变更性质登记库实体对象
     */
  	private CertBgxzdj dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertBgxzdj();  
		  }
    }
   
   
	      public CertBgxzdj getDataBean()
	      {
	          return dataBean;
	      }
}