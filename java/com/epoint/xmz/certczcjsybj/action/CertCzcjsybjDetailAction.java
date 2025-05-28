package com.epoint.xmz.certczcjsybj.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certczcjsybj.api.entity.CertCzcjsybj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certczcjsybj.api.ICertCzcjsybjService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 出租车驾驶员人员背景明细库详情页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:57]
 */
@RightRelation(CertCzcjsybjListAction.class)
@RestController("certczcjsybjdetailaction")
@Scope("request")
public class CertCzcjsybjDetailAction  extends BaseController
{
	  @Autowired
      private ICertCzcjsybjService service; 
    
    /**
     * 出租车驾驶员人员背景明细库实体对象
     */
  	private CertCzcjsybj dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CertCzcjsybj();  
		  }
    }
   
   
	      public CertCzcjsybj getDataBean()
	      {
	          return dataBean;
	      }
}