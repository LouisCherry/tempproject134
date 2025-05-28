package com.epoint.xmz.buildinfo.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 工改二阶段建筑表修改页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@RightRelation(BuildInfoListAction.class)
@RestController("buildinfoeditaction")
@Scope("request")
public class BuildInfoEditAction  extends BaseController
{

	@Autowired
	private IBuildInfoService service;
    
    /**
     * 工改二阶段建筑表实体对象
     */
  	private BuildInfo dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new BuildInfo();  
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

	public BuildInfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(BuildInfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
