package com.epoint.xmz.jntaskrelation.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.jntaskrelation.api.entity.JnTaskRelation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.jntaskrelation.api.IJnTaskRelationService;

/**
 * 事项关联乡镇表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-10-09 16:26:57]
 */
@RightRelation(JnTaskRelationListAction.class)
@RestController("jntaskrelationaddaction")
@Scope("request")
public class JnTaskRelationAddAction  extends BaseController
{
	@Autowired
	private IJnTaskRelationService service;  
    /**
     * 事项关联乡镇表实体对象
     */
  	private JnTaskRelation dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new JnTaskRelation();
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
		dataBean = new JnTaskRelation();
	}

    public JnTaskRelation getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new JnTaskRelation();
        }
        return dataBean;
    }

    public void setDataBean(JnTaskRelation dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
