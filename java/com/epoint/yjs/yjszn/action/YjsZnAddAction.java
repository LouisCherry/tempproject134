package com.epoint.yjs.yjszn.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 一件事指南配置新增页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-10-08 15:22:37]
 */
@RightRelation(YjsZnListAction.class)
@RestController("yjsznaddaction")
@Scope("request")
public class YjsZnAddAction  extends BaseController
{
	@Autowired
	private IYjsZnService service;  
    /**
     * 一件事指南配置实体对象
     */
  	private YjsZn dataBean=null;
  
   /**
  * type单选按钮组model
  */
 private List<SelectItem>  typeModel=null;
 /**
  * areacode下拉列表model
  */
 private List<SelectItem>  areacodeModel=null;


    public void pageLoad()
    {
        dataBean=new YjsZn();
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
		dataBean = new YjsZn();
	}

    public YjsZn getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new YjsZn();
        }
        return dataBean;
    }

    public void setDataBean(YjsZn dataBean)
    {
        this.dataBean = dataBean;
    }
    
  public List<SelectItem> getTypeModel(){if(typeModel==null){typeModel= DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","一件事指南类型",null,false));
} return this.typeModel;}
  public  List<SelectItem> getAreacodeModel(){if(areacodeModel==null){areacodeModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","辖区对应关系",null,false));
} return this.areacodeModel;}

}
