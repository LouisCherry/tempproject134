package com.epoint.xmz.certcbyyysz.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certcbyyysz.api.ICertCbyyyszService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 船舶营业运输证本地库详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-06-15 14:41:12]
 */
@RightRelation(CertCbyyyszListAction.class)
@RestController("certcbyyyszdetailaction")
@Scope("request")
public class CertCbyyyszDetailAction  extends BaseController
{
	  @Autowired
      private ICertCbyyyszService service; 
    
    /**
     * 船舶营业运输证本地库实体对象
     */
  	private CertCbyyysz dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertCbyyysz();  
		  }
    }
   
   
	      public CertCbyyysz getDataBean()
	      {
	          return dataBean;
	      }
}