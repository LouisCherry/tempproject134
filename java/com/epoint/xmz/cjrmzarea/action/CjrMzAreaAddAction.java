package com.epoint.xmz.cjrmzarea.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.cjrmzarea.api.entity.CjrMzArea;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.cjrmzarea.api.ICjrMzAreaService;

/**
 * 残疾人民政辖区对应表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2021-05-24 17:45:08]
 */
@RightRelation(CjrMzAreaListAction.class)
@RestController("cjrmzareaaddaction")
@Scope("request")
public class CjrMzAreaAddAction  extends BaseController
{
	@Autowired
	private ICjrMzAreaService service;  
    /**
     * 残疾人民政辖区对应表实体对象
     */
  	private CjrMzArea dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new CjrMzArea();
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
		dataBean = new CjrMzArea();
	}

    public CjrMzArea getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new CjrMzArea();
        }
        return dataBean;
    }

    public void setDataBean(CjrMzArea dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
