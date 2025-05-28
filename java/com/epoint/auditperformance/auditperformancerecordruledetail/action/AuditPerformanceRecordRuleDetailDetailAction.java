package com.epoint.auditperformance.auditperformancerecordruledetail.action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancerecordruledetail.domain.AuditPerformanceRecordRuleDetail;
import com.epoint.basic.auditperformance.auditperformancerecordruledetail.inter.IAuditPerformanceRecordRuleDetailService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 考评记录考评细则详情页面对应的后台
 * 
 * @author 泪流云
 * @version [版本号, 2018-01-09 16:07:13]
 */
@RightRelation(AuditPerformanceRecordRuleDetailListAction.class)
@RestController("auditperformancerecordruledetaildetailaction")
@Scope("request")
public class AuditPerformanceRecordRuleDetailDetailAction  extends BaseController
{
	  /**
     * 
     */
    private static final long serialVersionUID = -2632390467989343522L;

    @Autowired
      private IAuditPerformanceRecordRuleDetailService service; 
    
    /**
     * 考评记录考评细则实体对象
     */
  	private AuditPerformanceRecordRuleDetail dataBean=null;
  
  	@Override
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new AuditPerformanceRecordRuleDetail();  
		  }
    }
   
   
	      public AuditPerformanceRecordRuleDetail getDataBean()
	      {
	          return dataBean;
	      }
}