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
/**
 * 一件事指南配置修改页面对应的后台
 * 
 * @author panshunxing
 * @version [版本号, 2024-10-08 15:22:37]
 */
@RightRelation(YjsZnListAction.class)
@RestController("yjszneditaction")
@Scope("request")
public class YjsZnEditAction  extends BaseController
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
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new YjsZn();  
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
	    addCallbackParam("msg", l("修改成功")+"！");
	}

	public YjsZn getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(YjsZn dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	        public  List<SelectItem> getTypeModel(){if(typeModel==null){typeModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","一件事指南类型",null,false));
} return this.typeModel;}
  public  List<SelectItem> getAreacodeModel(){if(areacodeModel==null){areacodeModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","辖区对应关系",null,false));
} return this.areacodeModel;}

}
