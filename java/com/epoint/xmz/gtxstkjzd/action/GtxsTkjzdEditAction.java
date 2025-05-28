package com.epoint.xmz.gtxstkjzd.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.gtxstkjzd.api.entity.GtxsTkjzd;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.gtxstkjzd.api.IGtxsTkjzdService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 国土踏勘界址点修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 18:06:12]
 */
@RightRelation(GtxsTkjzdListAction.class)
@RestController("gtxstkjzdeditaction")
@Scope("request")
public class GtxsTkjzdEditAction  extends BaseController
{

	@Autowired
	private IGtxsTkjzdService service;
    
    /**
     * 国土踏勘界址点实体对象
     */
  	private GtxsTkjzd dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new GtxsTkjzd();  
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

	public GtxsTkjzd getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(GtxsTkjzd dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
