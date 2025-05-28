package com.epoint.xmz.rpagxbyxxb.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.rpagxbyxxb.api.entity.RpaGxbyxxb;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.rpagxbyxxb.api.IRpaGxbyxxbService;

/**
 * 高校毕业信息表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-12-23 16:40:09]
 */
@RightRelation(RpaGxbyxxbListAction.class)
@RestController("rpagxbyxxbaddaction")
@Scope("request")
public class RpaGxbyxxbAddAction  extends BaseController
{
	@Autowired
	private IRpaGxbyxxbService service;  
    /**
     * 高校毕业信息表实体对象
     */
  	private RpaGxbyxxb dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new RpaGxbyxxb();
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
		dataBean = new RpaGxbyxxb();
	}

    public RpaGxbyxxb getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new RpaGxbyxxb();
        }
        return dataBean;
    }

    public void setDataBean(RpaGxbyxxb dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
