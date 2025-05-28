package com.epoint.qfzwfw.auditznsbyyzzinfo.action;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.IAuditZnsbYyzzInfoService;
import com.epoint.qfzwfw.auditznsbyyzzinfo.api.entity.AuditZnsbYyzzInfo;
/**
 * 智能设备营业执照表修改页面对应的后台
 * 
 * @author LIUCTT
 * @version [版本号, 2018-06-07 09:51:27]
 */
@RightRelation(AuditZnsbYyzzInfoListAction.class)
@RestController("auditznsbyyzzinfoeditaction")
@Scope("request")
public class AuditZnsbYyzzInfoEditAction  extends BaseController
{

	@Autowired
	private IAuditZnsbYyzzInfoService service;
    
    /**
     * 智能设备营业执照表实体对象
     */
  	private AuditZnsbYyzzInfo dataBean=null;
  
       /**
  * 是否已打印单选按钮组model
  */
 private List<SelectItem>  isprintModel=null;
 /**
  * 证照类型下拉列表model
  */
 private List<SelectItem>  zztypeModel=null;


    @Override
    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditZnsbYyzzInfo();  
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

	public AuditZnsbYyzzInfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditZnsbYyzzInfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	      public  List<SelectItem> getIsprintModel(){if(isprintModel==null){isprintModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","是否",null,false));
	        } return this.isprintModel;}
	        public  List<SelectItem> getZztypeModel(){if(zztypeModel==null){zztypeModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","智能设备证照打印",null,false));
	        } return this.zztypeModel;}

}
