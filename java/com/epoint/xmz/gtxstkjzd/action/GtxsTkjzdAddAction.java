package com.epoint.xmz.gtxstkjzd.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.gtxstkjzd.api.entity.GtxsTkjzd;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.gtxstkjzd.api.IGtxsTkjzdService;

/**
 * 国土踏勘界址点新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-06 18:06:12]
 */
@RightRelation(GtxsTkjzdListAction.class)
@RestController("gtxstkjzdaddaction")
@Scope("request")
public class GtxsTkjzdAddAction  extends BaseController
{
	@Autowired
	private IGtxsTkjzdService service;  
    /**
     * 国土踏勘界址点实体对象
     */
  	private GtxsTkjzd dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new GtxsTkjzd();
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
		dataBean = new GtxsTkjzd();
	}

    public GtxsTkjzd getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new GtxsTkjzd();
        }
        return dataBean;
    }

    public void setDataBean(GtxsTkjzd dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
