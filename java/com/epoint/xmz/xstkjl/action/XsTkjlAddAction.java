package com.epoint.xmz.xstkjl.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xstkjl.api.entity.XsTkjl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xstkjl.api.IXsTkjlService;

/**
 * 国土踏勘记录新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:18:51]
 */
@RightRelation(XsTkjlListAction.class)
@RestController("xstkjladdaction")
@Scope("request")
public class XsTkjlAddAction  extends BaseController
{
	@Autowired
	private IXsTkjlService service;  
    /**
     * 国土踏勘记录实体对象
     */
  	private XsTkjl dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new XsTkjl();
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
		dataBean = new XsTkjl();
	}

    public XsTkjl getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XsTkjl();
        }
        return dataBean;
    }

    public void setDataBean(XsTkjl dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
