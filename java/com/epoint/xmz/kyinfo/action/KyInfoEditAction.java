package com.epoint.xmz.kyinfo.action;
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
import com.epoint.xmz.kyinfo.api.IKyInfoService;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
/**
 * 勘验信息表修改页面对应的后台
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:18]
 */
@RightRelation(KyInfoListAction.class)
@RestController("kyinfoeditaction")
@Scope("request")
public class KyInfoEditAction  extends BaseController
{

	@Autowired
	private IKyInfoService service;
    
    /**
     * 勘验信息表实体对象
     */
  	private KyInfo dataBean=null;
  
       /**
  * 勘验城市下拉列表model
  */
 private List<SelectItem>  kycityModel=null;


    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
	   if(dataBean==null)
	   {
		      dataBean=new KyInfo();  
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

	public KyInfo getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(KyInfo dataBean)
	      {
	          this.dataBean = dataBean;
	      }
	        public  List<SelectItem> getKycityModel(){if(kycityModel==null){kycityModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","区县市",null,false));
} return this.kycityModel;}

}
