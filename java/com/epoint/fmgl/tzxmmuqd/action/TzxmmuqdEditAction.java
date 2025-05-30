package com.epoint.fmgl.tzxmmuqd.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 投资项目目录清单修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-26 12:07:54]
 */
@RightRelation(TzxmmuqdListAction.class)
@RestController("tzxmmuqdeditaction")
@Scope("request")
public class TzxmmuqdEditAction  extends BaseController
{

	@Autowired
	private ITzxmmuqdService service;
    
    /**
     * 投资项目目录清单实体对象
     */
  	private Tzxmmuqd dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new Tzxmmuqd();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public Tzxmmuqd getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(Tzxmmuqd dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
