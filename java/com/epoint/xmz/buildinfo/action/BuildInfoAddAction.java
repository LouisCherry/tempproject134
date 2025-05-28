package com.epoint.xmz.buildinfo.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.buildinfo.api.entity.BuildInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.buildinfo.api.IBuildInfoService;

/**
 * 工改二阶段建筑表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@RightRelation(BuildInfoListAction.class)
@RestController("buildinfoaddaction")
@Scope("request")
public class BuildInfoAddAction  extends BaseController
{
	@Autowired
	private IBuildInfoService service;  
    /**
     * 工改二阶段建筑表实体对象
     */
  	private BuildInfo dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new BuildInfo();
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
	    service.insert(dataBean);
	    addCallbackParam("msg", "保存成功！");
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new BuildInfo();
	}

    public BuildInfo getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new BuildInfo();
        }
        return dataBean;
    }

    public void setDataBean(BuildInfo dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
