package com.epoint.xmz.rpaysgzzba.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpaysgzzba.api.entity.RpaYsgzzba;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.rpaysgzzba.api.IRpaYsgzzbaService;

/**
 * 院士工作站备案新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-20 10:26:25]
 */
@RightRelation(RpaYsgzzbaListAction.class)
@RestController("rpaysgzzbaaddaction")
@Scope("request")
public class RpaYsgzzbaAddAction  extends BaseController
{
	@Autowired
	private IRpaYsgzzbaService service;  
    /**
     * 院士工作站备案实体对象
     */
  	private RpaYsgzzba dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new RpaYsgzzba();
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
		dataBean = new RpaYsgzzba();
	}

    public RpaYsgzzba getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new RpaYsgzzba();
        }
        return dataBean;
    }

    public void setDataBean(RpaYsgzzba dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
