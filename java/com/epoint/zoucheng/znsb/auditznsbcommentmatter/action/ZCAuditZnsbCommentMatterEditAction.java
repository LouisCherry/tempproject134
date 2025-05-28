package com.epoint.zoucheng.znsb.auditznsbcommentmatter.action;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
/**
 * 工作台评价事项记录表修改页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-08 13:37:36]
 */
@RightRelation(ZCAuditZnsbCommentMatterListAction.class)
@RestController("zcauditznsbcommentmattereditaction")
@Scope("request")
public class ZCAuditZnsbCommentMatterEditAction  extends BaseController
{

	/**
     * 
     */
    private static final long serialVersionUID = -5337141918851787553L;

    @Autowired
	private IZCAuditZnsbCommentMatterService service;
    
    /**
     * 工作台评价事项记录表实体对象
     */
  	private ZCAuditZnsbCommentMatter dataBean=null;
  
      

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new ZCAuditZnsbCommentMatter();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public ZCAuditZnsbCommentMatter getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(ZCAuditZnsbCommentMatter dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      
}
