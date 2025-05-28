package com.epoint.xmz.kyinfo.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.kyinfo.api.IKyInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 勘验信息表详情页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:19]
 */
@RightRelation(KyInfoListAction.class)
@RestController("kyinfodetailaction")
@Scope("request")
public class KyInfoDetailAction  extends BaseController
{
	  @Autowired
      private IKyInfoService service; 
    
    /**
     * 勘验信息表实体对象
     */
  	private KyInfo dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new KyInfo();  
		  }
    }
   
   
	      public KyInfo getDataBean()
	      {
	          return dataBean;
	      }
}