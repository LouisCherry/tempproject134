package com.epoint.xmz.certczqcgxdj.action;
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
import com.epoint.xmz.certczqcgxdj.api.ICertCzqcgxdjService;
import com.epoint.xmz.certczqcgxdj.api.entity.CertCzqcgxdj;
/**
 * 出租汽车更新登记计划库修改页面对应的后台
 * 
 * @author dyxin
 * @version [版本号, 2023-05-22 13:17:25]
 */
@RightRelation(CertCzqcgxdjListAction.class)
@RestController("certczqcgxdjeditaction")
@Scope("request")
public class CertCzqcgxdjEditAction  extends BaseController
{

	@Autowired
	private ICertCzqcgxdjService service;
    
    /**
     * 出租汽车更新登记计划库实体对象
     */
  	private CertCzqcgxdj dataBean=null;
  
       /**
  * 燃料类型单选按钮组model
  */
 private List<SelectItem>  fueltypeModel=null;


    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new CertCzqcgxdj();  
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

	public CertCzqcgxdj getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(CertCzqcgxdj dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	        public  List<SelectItem> getFueltypeModel(){if(fueltypeModel==null){fueltypeModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组","燃料类型",null,false));
} return this.fueltypeModel;}

}
