package com.epoint.xmz.xssbb.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xssbb.api.entity.XsSbb;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xssbb.api.IXsSbbService;

/**
 * 国土_申报表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 15:34:58]
 */
@RightRelation(XsSbbListAction.class)
@RestController("xssbbaddaction")
@Scope("request")
public class XsSbbAddAction  extends BaseController
{
	@Autowired
	private IXsSbbService service;  
    /**
     * 国土_申报表实体对象
     */
  	private XsSbb dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new XsSbb();
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
		dataBean = new XsSbb();
	}

    public XsSbb getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new XsSbb();
        }
        return dataBean;
    }

    public void setDataBean(XsSbb dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
