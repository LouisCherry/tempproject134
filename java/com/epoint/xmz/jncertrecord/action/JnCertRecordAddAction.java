package com.epoint.xmz.jncertrecord.action;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;

/**
 * 证照调用次数统计表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2022-08-22 16:53:37]
 */
@RightRelation(JnCertRecordListAction.class)
@RestController("jncertrecordaddaction")
@Scope("request")
public class JnCertRecordAddAction  extends BaseController
{
	@Autowired
	private IJnCertRecordService service;  
    /**
     * 证照调用次数统计表实体对象
     */
  	private JnCertRecord dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new JnCertRecord();
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
		dataBean = new JnCertRecord();
	}

    public JnCertRecord getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new JnCertRecord();
        }
        return dataBean;
    }

    public void setDataBean(JnCertRecord dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
