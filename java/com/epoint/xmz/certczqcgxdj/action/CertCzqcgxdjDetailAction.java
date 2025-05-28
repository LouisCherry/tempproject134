package com.epoint.xmz.certczqcgxdj.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certczqcgxdj.api.entity.CertCzqcgxdj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certczqcgxdj.api.ICertCzqcgxdjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 出租汽车更新登记计划库详情页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:25]
 */
@RightRelation(CertCzqcgxdjListAction.class)
@RestController("certczqcgxdjdetailaction")
@Scope("request")
public class CertCzqcgxdjDetailAction  extends BaseController
{
	  @Autowired
      private ICertCzqcgxdjService service; 
    
    /**
     * 出租汽车更新登记计划库实体对象
     */
  	private CertCzqcgxdj dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertCzqcgxdj();  
		  }
    }
   
   
	      public CertCzqcgxdj getDataBean()
	      {
	          return dataBean;
	      }
}