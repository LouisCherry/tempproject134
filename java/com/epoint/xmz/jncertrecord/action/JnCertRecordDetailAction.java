package com.epoint.xmz.jncertrecord.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 证照调用次数统计表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-08-22 16:53:37]
 */
@RightRelation(JnCertRecordListAction.class)
@RestController("jncertrecorddetailaction")
@Scope("request")
public class JnCertRecordDetailAction  extends BaseController
{
	  @Autowired
      private IJnCertRecordService service; 
    
    /**
     * 证照调用次数统计表实体对象
     */
  	private JnCertRecord dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new JnCertRecord();  
		  }
    }
   
   
	      public JnCertRecord getDataBean()
	      {
	          return dataBean;
	      }
}