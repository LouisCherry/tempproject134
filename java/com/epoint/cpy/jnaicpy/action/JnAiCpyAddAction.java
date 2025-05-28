package com.epoint.cpy.jnaicpy.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.cpy.jnaicpy.api.entity.JnAiCpy;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.cpy.jnaicpy.api.IJnAiCpyService;

/**
 * 成品油零售经营企业库新增页面对应的后台
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@RightRelation(JnAiCpyListAction.class)
@RestController("jnaicpyaddaction")
@Scope("request")
public class JnAiCpyAddAction  extends BaseController
{
	@Autowired
	private IJnAiCpyService service;  
    /**
     * 成品油零售经营企业库实体对象
     */
  	private JnAiCpy dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new JnAiCpy();
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
        dataBean.setVersion(1);
        dataBean.setIs_enable("1");
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
		dataBean = new JnAiCpy();
	}

    public JnAiCpy getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new JnAiCpy();
        }
        return dataBean;
    }

    public void setDataBean(JnAiCpy dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
