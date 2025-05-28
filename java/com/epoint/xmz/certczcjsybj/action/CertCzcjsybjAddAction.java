package com.epoint.xmz.certczcjsybj.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.certczcjsybj.api.ICertCzcjsybjService;
import com.epoint.xmz.certczcjsybj.api.entity.CertCzcjsybj;

/**
 * 出租车驾驶员人员背景明细库新增页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:56]
 */
@RightRelation(CertCzcjsybjListAction.class)
@RestController("certczcjsybjaddaction")
@Scope("request")
public class CertCzcjsybjAddAction  extends BaseController
{
	@Autowired
	private ICertCzcjsybjService service;  
    /**
     * 出租车驾驶员人员背景明细库实体对象
     */
  	private CertCzcjsybj dataBean=null;
  
   /**
  * 性别单选按钮组model
  */
 private List<SelectItem>  sexModel=null;


    public void pageLoad()
    {
        dataBean=new CertCzcjsybj();
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
		dataBean = new CertCzcjsybj();
	}

    public CertCzcjsybj getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new CertCzcjsybj();
        }
        return dataBean;
    }

    public void setDataBean(CertCzcjsybj dataBean)
    {
        this.dataBean = dataBean;
    }
    
  public  List<SelectItem> getSexModel(){if(sexModel==null){sexModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","性别",null,false));
} return this.sexModel;}

}
