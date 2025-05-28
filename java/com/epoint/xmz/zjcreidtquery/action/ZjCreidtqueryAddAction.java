package com.epoint.xmz.zjcreidtquery.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.zjcreidtquery.api.entity.ZjCreidtquery;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.zjcreidtquery.api.IZjCreidtqueryService;

/**
 * 信用查询调用统计表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@RightRelation(ZjCreidtqueryListAction.class)
@RestController("zjcreidtqueryaddaction")
@Scope("request")
public class ZjCreidtqueryAddAction  extends BaseController
{
	@Autowired
	private IZjCreidtqueryService service;  
    /**
     * 信用查询调用统计表实体对象
     */
  	private ZjCreidtquery dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new ZjCreidtquery();
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
		dataBean = new ZjCreidtquery();
	}

    public ZjCreidtquery getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new ZjCreidtquery();
        }
        return dataBean;
    }

    public void setDataBean(ZjCreidtquery dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
