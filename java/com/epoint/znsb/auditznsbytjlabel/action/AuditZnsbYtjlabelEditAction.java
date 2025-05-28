package com.epoint.znsb.auditznsbytjlabel.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.znsb.auditznsbytjlabel.api.entity.AuditZnsbYtjlabel;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.znsb.auditznsbytjlabel.api.IAuditZnsbYtjlabelService;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 一体机模块标签修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 09:47:21]
 */
@RightRelation(AuditZnsbYtjlabelListAction.class)
@RestController("auditznsbytjlabeleditaction")
@Scope("request")
public class AuditZnsbYtjlabelEditAction  extends BaseController
{

	@Autowired
	private IAuditZnsbYtjlabelService service;
    
    /**
     * 一体机模块标签实体对象
     */
  	private AuditZnsbYtjlabel dataBean=null;

	/**
	 * 底色下拉列表model
	 */
	private List<SelectItem>  labelcolorModel=null;



	/**
	 * 上传图片modul
	 */
	private FileUploadModel9 picUploadModel;

	/**
	 * 文件附件标识
	 */
	private String logocliengguid;

    public void pageLoad()
    {
       String guid = getRequestParameter("guid");
       dataBean = service.find(guid);
		if (StringUtil.isNotBlank(dataBean.getPngattachguid())) {
			logocliengguid = dataBean.getPngattachguid();
		}
		else {
			if (StringUtil.isBlank(getViewData("logocliengguid"))) {
				logocliengguid = UUID.randomUUID().toString();
				addViewData("logocliengguid", logocliengguid);
			}
			else {
				logocliengguid = getViewData("logocliengguid");
			}
		}
	   if(dataBean==null)
	   {
		      dataBean=new AuditZnsbYtjlabel();  
	   }
    }

    /**
     * 保存修改
     * 
     */
	public void save() 
	{
	    dataBean.setOperatedate(new Date());
		dataBean.setPngattachguid(logocliengguid);
	    service.update(dataBean);
	    addCallbackParam("msg", "修改成功！");
	}

	public AuditZnsbYtjlabel getDataBean()
	      {
	          return dataBean;
	      }

	      public void setDataBean(AuditZnsbYtjlabel dataBean)
	      {
	          this.dataBean = dataBean;
	      }

	public  List<SelectItem> getLabelcolorModel(){if(labelcolorModel==null){labelcolorModel=DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","一体机标签底色",null,true));
	} return this.labelcolorModel;}


	/**
	 *
	 *  [logo图片上传]
	 *  @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public FileUploadModel9 getPicUploadModel() {
		if (picUploadModel == null) {
			AttachHandler9 handler = new AttachHandler9()
			{

				private static final long serialVersionUID = 1L;

				@Override
				public void afterSaveAttachToDB(Object attach) {
					picUploadModel.getExtraDatas().put("msg", "上传成功");
				}

				@Override
				public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
					return true;
				}
			};
			picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(logocliengguid, null, null, handler,
					userSession.getUserGuid(), userSession.getDisplayName()));
		}
		return picUploadModel;
	}

}
