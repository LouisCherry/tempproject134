package com.epoint.znsb.auditznsbpayment.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbpayment.api.entity.AuditZnsbPayment;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbpayment.api.IAuditZnsbPaymentService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 水电气缴费详情页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-05-18 18:32:19]
 */
@RightRelation(AuditZnsbPaymentListAction.class)
@RestController("auditznsbpaymentdetailaction")
@Scope("request")
public class AuditZnsbPaymentDetailAction  extends BaseController
{
	  @Autowired
      private IAuditZnsbPaymentService service; 
    
    /**
     * 水电气缴费实体对象
     */
  	private AuditZnsbPayment dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditZnsbPayment();  
		  }
    }
   
   
	      public AuditZnsbPayment getDataBean()
	      {
	          return dataBean;
	      }
}