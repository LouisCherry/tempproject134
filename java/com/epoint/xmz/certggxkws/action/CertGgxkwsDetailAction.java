package com.epoint.xmz.certggxkws.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 公共许可卫生证照库详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-04-12 17:01:05]
 */
@RightRelation(CertGgxkwsListAction.class)
@RestController("certggxkwsdetailaction")
@Scope("request")
public class CertGgxkwsDetailAction  extends BaseController
{
	  @Autowired
      private ICertGgxkwsService service; 
    
    /**
     * 公共许可卫生证照库实体对象
     */
  	private CertGgxkws dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertGgxkws();  
		  }
    }
   
   
	      public CertGgxkws getDataBean()
	      {
	          return dataBean;
	      }
}