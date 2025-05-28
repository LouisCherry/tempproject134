package com.epoint.takan.kanyanmain.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.takan.kanyanmain.api.entity.Kanyanmain;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.takan.kanyanmain.api.IKanyanmainService;

/**
 * 勘验主表新增页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 02:27:25]
 */
@RightRelation(KanyanmainListAction.class)
@RestController("kanyanmainaddaction")
@Scope("request")
public class KanyanmainAddAction  extends BaseController
{
	@Autowired
	private IKanyanmainService service;  
    /**
     * 勘验主表实体对象
     */
  	private Kanyanmain dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new Kanyanmain();
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
	    addCallbackParam("msg", l("保存成功！"));
	    dataBean = null;
	}

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new Kanyanmain();
	}

    public Kanyanmain getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new Kanyanmain();
        }
        return dataBean;
    }

    public void setDataBean(Kanyanmain dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
