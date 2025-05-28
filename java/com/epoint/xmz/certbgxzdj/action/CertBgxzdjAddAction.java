package com.epoint.xmz.certbgxzdj.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.certbgxzdj.api.entity.CertBgxzdj;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.certbgxzdj.api.ICertBgxzdjService;

/**
 * 变更性质登记库新增页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:41]
 */
@RightRelation(CertBgxzdjListAction.class)
@RestController("certbgxzdjaddaction")
@Scope("request")
public class CertBgxzdjAddAction  extends BaseController
{
	@Autowired
	private ICertBgxzdjService service;  
    /**
     * 变更性质登记库实体对象
     */
  	private CertBgxzdj dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new CertBgxzdj();
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
		dataBean = new CertBgxzdj();
	}

    public CertBgxzdj getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new CertBgxzdj();
        }
        return dataBean;
    }

    public void setDataBean(CertBgxzdj dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
