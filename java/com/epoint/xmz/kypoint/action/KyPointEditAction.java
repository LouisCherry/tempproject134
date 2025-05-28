package com.epoint.xmz.kypoint.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.kypoint.api.entity.KyPoint;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.kypoint.api.IKyPointService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 勘验要点表修改页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:28:23]
 */
@RightRelation(KyPointListAction.class)
@RestController("kypointeditaction")
@Scope("request")
public class KyPointEditAction  extends BaseController
{

	@Autowired
	private IKyPointService service;
    
    /**
     * 勘验要点表实体对象
     */
  	private KyPoint dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new KyPoint();  
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

	public KyPoint getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(KyPoint dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
