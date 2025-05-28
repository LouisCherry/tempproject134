package com.epoint.takan.kanyanmain.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 勘验主表修改页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RightRelation(KanyanmainListAction.class)
@RestController("kanyanmaineditaction")
@Scope("request")
public class KanyanmainEditAction  extends BaseController
{

	@Autowired
	private IKanyanmainService service;
    
    /**
     * 勘验主表实体对象
     */
  	private Kanyanmain dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new Kanyanmain();  
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
	    addCallbackParam("msg", l("修改成功")+"！");
	}

	public Kanyanmain getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(Kanyanmain dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
