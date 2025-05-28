package com.epoint.cs.tsproject.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.cs.tsproject.api.entity.TsProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.cs.tsproject.api.ITsProjectService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 推送数据修改页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RightRelation(TsProjectListAction.class)
@RestController("tsprojecteditaction")
@Scope("request")
public class TsProjectEditAction  extends BaseController
{

	@Autowired
	private ITsProjectService service;
    
    /**
     * 推送数据实体对象
     */
  	private TsProject dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new TsProject();  
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

	public TsProject getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(TsProject dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
