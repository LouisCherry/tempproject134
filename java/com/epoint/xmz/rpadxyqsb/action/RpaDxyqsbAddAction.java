package com.epoint.xmz.rpadxyqsb.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpadxyqsb.api.entity.RpaDxyqsb;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.rpadxyqsb.api.IRpaDxyqsbService;

/**
 * 大型仪器设备协作共用网新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 17:36:22]
 */
@RightRelation(RpaDxyqsbListAction.class)
@RestController("rpadxyqsbaddaction")
@Scope("request")
public class RpaDxyqsbAddAction  extends BaseController
{
	@Autowired
	private IRpaDxyqsbService service;  
    /**
     * 大型仪器设备协作共用网实体对象
     */
  	private RpaDxyqsb dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new RpaDxyqsb();
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
		dataBean = new RpaDxyqsb();
	}

    public RpaDxyqsb getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new RpaDxyqsb();
        }
        return dataBean;
    }

    public void setDataBean(RpaDxyqsb dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
