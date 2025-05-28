package com.epoint.zoucheng.znsb.auditznsbcommentmatter.action;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.zoucheng.znsb.auditznsbcommentmatter.domain.ZCAuditZnsbCommentMatter;
import com.epoint.zoucheng.znsb.auditznsbcommentmatter.inter.IZCAuditZnsbCommentMatterService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 工作台评价事项记录表新增页面对应的后台
 * 
 * @author chencong
 * @version [版本号, 2020-04-08 13:37:36]
 */
@RightRelation(ZCAuditZnsbCommentMatterListAction.class)
@RestController("zcauditznsbcommentmatteraddaction")
@Scope("request")
public class ZCAuditZnsbCommentMatterAddAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = -1984079136869066965L;
    @Autowired
	private IZCAuditZnsbCommentMatterService service;  
    /**
     * 工作台评价事项记录表实体对象
     */
  	private ZCAuditZnsbCommentMatter dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new ZCAuditZnsbCommentMatter();
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
		dataBean = new ZCAuditZnsbCommentMatter();
	}

    public ZCAuditZnsbCommentMatter getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new ZCAuditZnsbCommentMatter();
        }
        return dataBean;
    }

    public void setDataBean(ZCAuditZnsbCommentMatter dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
