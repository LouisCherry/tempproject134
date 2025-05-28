package com.epoint.znsb.auditznsbytjextend.action;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbytjextend.api.IAuditZnsbYtjextendService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 一体机模块额外配置修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:49]
 */
@RightRelation(AuditZnsbYtjextendListAction.class)
@RestController("auditznsbytjextendeditaction")
@Scope("request")
public class AuditZnsbYtjextendEditAction  extends BaseController
{

	@Autowired
	private IAuditZnsbYtjextendService service;
    
    /**
     * 一体机模块额外配置实体对象
     */
  	private AuditZnsbYtjextend dataBean=null;
  
       /**
  * 是否热门模块单选按钮组model
  */
 private List<SelectItem>  is_hotModel=null;


    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new AuditZnsbYtjextend();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
	    //system.out.println(dataBean);
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public AuditZnsbYtjextend getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditZnsbYtjextend dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	        public  List<SelectItem> getIs_hotModel(){if(is_hotModel==null){is_hotModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","是否",null,false));
} return this.is_hotModel;}

}
