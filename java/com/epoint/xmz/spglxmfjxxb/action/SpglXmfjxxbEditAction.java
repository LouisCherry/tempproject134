package com.epoint.xmz.spglxmfjxxb.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.spglxmfjxxb.api.ISpglXmfjxxbService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 项目附件信息表修改页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-10-17 18:08:02]
 */
@RightRelation(SpglXmfjxxbListAction.class)
@RestController("spglxmfjxxbeditaction")
@Scope("request")
public class SpglXmfjxxbEditAction  extends BaseController
{

	@Autowired
	private ISpglXmfjxxbService service;
    
    /**
     * 项目附件信息表实体对象
     */
  	private SpglXmfjxxb dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new SpglXmfjxxb();  
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

	public SpglXmfjxxb getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(SpglXmfjxxb dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
