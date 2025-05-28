package com.epoint.xmz.xsxkjl.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsxkjl.api.entity.XsXkjl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xsxkjl.api.IXsXkjlService;

/**
 * 国土许可记录新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:26:17]
 */
@RightRelation(XsXkjlListAction.class)
@RestController("xsxkjladdaction")
@Scope("request")
public class XsXkjlAddAction  extends BaseController
{
	@Autowired
	private IXsXkjlService service;  
    /**
     * 国土许可记录实体对象
     */
  	private XsXkjl dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new XsXkjl();
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
		dataBean = new XsXkjl();
	}

    public XsXkjl getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XsXkjl();
        }
        return dataBean;
    }

    public void setDataBean(XsXkjl dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
