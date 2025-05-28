package com.epoint.xmz.cjrmzarea.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.cjrmzarea.api.entity.CjrMzArea;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.cjrmzarea.api.ICjrMzAreaService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 残疾人民政辖区对应表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-05-24 17:45:08]
 */
@RightRelation(CjrMzAreaListAction.class)
@RestController("cjrmzareaeditaction")
@Scope("request")
public class CjrMzAreaEditAction  extends BaseController
{

	@Autowired
	private ICjrMzAreaService service;
    
    /**
     * 残疾人民政辖区对应表实体对象
     */
  	private CjrMzArea dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new CjrMzArea();  
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

	public CjrMzArea getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(CjrMzArea dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
