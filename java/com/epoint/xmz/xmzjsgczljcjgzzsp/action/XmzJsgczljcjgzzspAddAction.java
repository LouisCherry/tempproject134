package com.epoint.xmz.xmzjsgczljcjgzzsp.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.entity.XmzJsgczljcjgzzsp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xmzjsgczljcjgzzsp.api.IXmzJsgczljcjgzzspService;

/**
 * 建设工程质量检测机构资质审批表新增页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-08 17:01:26]
 */
@RightRelation(XmzJsgczljcjgzzspListAction.class)
@RestController("xmzjsgczljcjgzzspaddaction")
@Scope("request")
public class XmzJsgczljcjgzzspAddAction  extends BaseController
{
	@Autowired
	private IXmzJsgczljcjgzzspService service;  
    /**
     * 建设工程质量检测机构资质审批表实体对象
     */
  	private XmzJsgczljcjgzzsp dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new XmzJsgczljcjgzzsp();
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
		dataBean = new XmzJsgczljcjgzzsp();
	}

    public XmzJsgczljcjgzzsp getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XmzJsgczljcjgzzsp();
        }
        return dataBean;
    }

    public void setDataBean(XmzJsgczljcjgzzsp dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
