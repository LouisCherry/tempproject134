package com.epoint.cs.yyzzprint.action;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.yyzzprint.api.entity.Yyzzprint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.yyzzprint.api.IYyzzprintService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 营业执照打印记录表详情页面对应的后台
 * 
 * @author admin
 * @version [版本号, 2020-04-23 11:25:13]
 */
@RightRelation(YyzzprintListAction.class)
@RestController("yyzzprintdetailaction")
@Scope("request")
public class YyzzprintDetailAction  extends BaseController
{
	  @Autowired
      private IYyzzprintService service; 
    
    /**
     * 营业执照打印记录表实体对象
     */
  	private Yyzzprint dataBean=null;
  
    public void pageLoad()
    {
	   	String	guid = getRequestParameter("guid");
	   	dataBean = service.find(guid);
		  if(dataBean==null){
		      dataBean=new Yyzzprint();  
		  }
    }
   
   
	      public Yyzzprint getDataBean()
	      {
	          return dataBean;
	      }
	      
	      public void getImg() {
	             
              addCallbackParam("faceimg", dataBean.getStr("faceattachguid"));
              addCallbackParam("fontimg", dataBean.getStr("fontattachguid"));
              
            

        }
}