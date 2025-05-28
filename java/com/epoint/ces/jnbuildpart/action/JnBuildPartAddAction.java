package com.epoint.ces.jnbuildpart.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;

/**
 * 建筑业企业资质数据库新增页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@RightRelation(JnBuildPartListAction.class)
@RestController("jnbuildpartaddaction")
@Scope("request")
public class JnBuildPartAddAction  extends BaseController
{
	@Autowired
	private IJnBuildPartService service;  
    /**
     * 建筑业企业资质数据库实体对象
     */
  	private JnBuildPart dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new JnBuildPart();
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
        dataBean.set("version", "1");
        dataBean.set("is_enable", "1");
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
		dataBean = new JnBuildPart();
	}

    public JnBuildPart getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new JnBuildPart();
        }
        return dataBean;
    }

    public void setDataBean(JnBuildPart dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
