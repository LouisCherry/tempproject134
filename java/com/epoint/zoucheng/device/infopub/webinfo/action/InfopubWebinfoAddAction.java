package com.epoint.zoucheng.device.infopub.webinfo.action;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zoucheng.device.infopub.webinfo.api.IInfopubWebinfoService;
import com.epoint.zoucheng.device.infopub.webinfo.api.entity.InfopubWebinfo;

/**
 * 网页信息表新增页面对应的后台
 * 
 * @author why
 * @version [版本号, 2019-09-17 11:17:19]
 */
@RightRelation(InfopubWebinfoListAction.class)
@RestController("infopubwebinfoaddaction")
@Scope("request")
public class InfopubWebinfoAddAction  extends BaseController
{
	@Autowired
	private IInfopubWebinfoService service;  
    /**
     * 网页信息表实体对象
     */
  	private InfopubWebinfo dataBean=null;
  
  

    public void pageLoad()
    {
        dataBean=new InfopubWebinfo();
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
		dataBean = new InfopubWebinfo();
	}

    public InfopubWebinfo getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new InfopubWebinfo();
        }
        return dataBean;
    }

    public void setDataBean(InfopubWebinfo dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
