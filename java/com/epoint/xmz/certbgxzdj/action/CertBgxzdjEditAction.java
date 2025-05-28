package com.epoint.xmz.certbgxzdj.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.certbgxzdj.api.entity.CertBgxzdj;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.certbgxzdj.api.ICertBgxzdjService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 变更性质登记库修改页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:41]
 */
@RightRelation(CertBgxzdjListAction.class)
@RestController("certbgxzdjeditaction")
@Scope("request")
public class CertBgxzdjEditAction  extends BaseController
{

	@Autowired
	private ICertBgxzdjService service;
    
    /**
     * 变更性质登记库实体对象
     */
  	private CertBgxzdj dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new CertBgxzdj();  
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

	public CertBgxzdj getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(CertBgxzdj dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
