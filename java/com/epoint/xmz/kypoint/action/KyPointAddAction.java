package com.epoint.xmz.kypoint.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.kypoint.api.entity.KyPoint;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.kypoint.api.IKyPointService;

/**
 * 勘验要点表新增页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:28:23]
 */
@RightRelation(KyPointListAction.class)
@RestController("kypointaddaction")
@Scope("request")
public class KyPointAddAction  extends BaseController
{
	@Autowired
	private IKyPointService service;  
    /**
     * 勘验要点表实体对象
     */
  	private KyPoint dataBean=null;
  
  	private String kyguid;

    public void pageLoad()
    {
        kyguid = getRequestParameter("projectguid");
        dataBean=new KyPoint();
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setKyguid(kyguid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
	    service.insert(dataBean);
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new KyPoint();
	}

    public KyPoint getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new KyPoint();
        }
        return dataBean;
    }

    public void setDataBean(KyPoint dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
