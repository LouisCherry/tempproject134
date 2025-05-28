package com.epoint.xmz.xsxmjbqk.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xsxmjbqk.api.entity.XsXmjbqk;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xsxmjbqk.api.IXsXmjbqkService;

/**
 * 国土项目基本情况新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:46:23]
 */
@RightRelation(XsXmjbqkListAction.class)
@RestController("xsxmjbqkaddaction")
@Scope("request")
public class XsXmjbqkAddAction  extends BaseController
{
	@Autowired
	private IXsXmjbqkService service;  
    /**
     * 国土项目基本情况实体对象
     */
  	private XsXmjbqk dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new XsXmjbqk();
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
		dataBean = new XsXmjbqk();
	}

    public XsXmjbqk getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XsXmjbqk();
        }
        return dataBean;
    }

    public void setDataBean(XsXmjbqk dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
