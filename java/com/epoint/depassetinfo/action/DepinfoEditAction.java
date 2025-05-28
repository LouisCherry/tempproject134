package com.epoint.depassetinfo.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.New;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.depassetinfo.domain.Deptask;
/**
 * 
 * 
 * @author xbn
 * @version [版本号, 2018-03-27 08:13:16]
 */
@RestController("depinfoeditaction")
@Scope("request")
public class DepinfoEditAction  extends BaseController
{

	private CommonService service = new CommonService();
    /**
     * 采购基本信息表实体对象
     */
  	private Deptask dataBean=null;
  
       /**
  * 采购类型下拉列表model
  */
 private List<SelectItem>  assettypeModel=null;


    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(Deptask.class, guid);
	   if(dataBean==null)
	   {
		      dataBean=new Deptask();  
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
	public Deptask getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(Deptask dataBean)
	      {
	          this.dataBean = dataBean;
	      }


}
