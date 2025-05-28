package com.epoint.takan.kanyanproject.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 勘验项目修改页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 03:15:13]
 */
@RightRelation(KanyanprojectListAction.class)
@RestController("kanyanprojecteditaction")
@Scope("request")
public class KanyanprojectEditAction  extends BaseController
{

	@Autowired
	private IKanyanprojectService service;
    
    /**
     * 勘验项目实体对象
     */
  	private Kanyanproject dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new Kanyanproject();  
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

	public Kanyanproject getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(Kanyanproject dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
