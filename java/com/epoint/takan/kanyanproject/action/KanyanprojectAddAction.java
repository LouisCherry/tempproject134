package com.epoint.takan.kanyanproject.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.takan.kanyanproject.api.entity.Kanyanproject;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.takan.kanyanproject.api.IKanyanprojectService;

/**
 * 勘验项目新增页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-09-20 03:15:13]
 */
@RightRelation(KanyanprojectListAction.class)
@RestController("kanyanprojectaddaction")
@Scope("request")
public class KanyanprojectAddAction  extends BaseController
{
	@Autowired
	private IKanyanprojectService service;  
    /**
     * 勘验项目实体对象
     */
  	private Kanyanproject dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new Kanyanproject();
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
		dataBean = new Kanyanproject();
	}

    public Kanyanproject getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new Kanyanproject();
        }
        return dataBean;
    }

    public void setDataBean(Kanyanproject dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
