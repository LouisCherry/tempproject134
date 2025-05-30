package com.epoint.fmgl.tzxmmuqd.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;

/**
 * 投资项目目录清单新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2020-09-26 12:07:53]
 */
@RightRelation(TzxmmuqdListAction.class)
@RestController("tzxmmuqdaddaction")
@Scope("request")
public class TzxmmuqdAddAction  extends BaseController
{
	@Autowired
	private ITzxmmuqdService service;  
    /**
     * 投资项目目录清单实体对象
     */
  	private Tzxmmuqd dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new Tzxmmuqd();
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
		dataBean = new Tzxmmuqd();
	}

    public Tzxmmuqd getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new Tzxmmuqd();
        }
        return dataBean;
    }

    public void setDataBean(Tzxmmuqd dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
