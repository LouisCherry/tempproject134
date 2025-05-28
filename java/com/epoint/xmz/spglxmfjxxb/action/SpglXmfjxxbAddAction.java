package com.epoint.xmz.spglxmfjxxb.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.spglxmfjxxb.api.entity.SpglXmfjxxb;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.spglxmfjxxb.api.ISpglXmfjxxbService;

/**
 * 项目附件信息表新增页面对应的后台
 * 
 * @author dahe
 * @version [版本号, 2024-10-17 18:08:02]
 */
@RightRelation(SpglXmfjxxbListAction.class)
@RestController("spglxmfjxxbaddaction")
@Scope("request")
public class SpglXmfjxxbAddAction  extends BaseController
{
	@Autowired
	private ISpglXmfjxxbService service;  
    /**
     * 项目附件信息表实体对象
     */
  	private SpglXmfjxxb dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new SpglXmfjxxb();
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
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new SpglXmfjxxb();
	}

    public SpglXmfjxxb getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new SpglXmfjxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglXmfjxxb dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
