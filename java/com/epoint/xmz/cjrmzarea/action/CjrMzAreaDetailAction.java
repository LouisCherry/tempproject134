package com.epoint.xmz.cjrmzarea.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.cjrmzarea.api.entity.CjrMzArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.cjrmzarea.api.ICjrMzAreaService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 残疾人民政辖区对应表详情页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-05-24 17:45:08]
 */
@RightRelation(CjrMzAreaListAction.class)
@RestController("cjrmzareadetailaction")
@Scope("request")
public class CjrMzAreaDetailAction  extends BaseController
{
	  @Autowired
      private ICjrMzAreaService service; 
    
    /**
     * 残疾人民政辖区对应表实体对象
     */
  	private CjrMzArea dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new CjrMzArea();  
		  }
    }
   
   
	      public CjrMzArea getDataBean()
	      {
	          return dataBean;
	      }
}