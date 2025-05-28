package com.epoint.cs.tsproject.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cs.tsproject.api.entity.TsProject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.cs.tsproject.api.ITsProjectService;

/**
 * 推送数据新增页面对应的后台
 * 
 * @author 18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@RightRelation(TsProjectListAction.class)
@RestController("tsprojectaddaction")
@Scope("request")
public class TsProjectAddAction  extends BaseController
{
	@Autowired
	private ITsProjectService service;  
    /**
     * 推送数据实体对象
     */
  	private TsProject dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new TsProject();
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
		dataBean = new TsProject();
	}

    public TsProject getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new TsProject();
        }
        return dataBean;
    }

    public void setDataBean(TsProject dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
