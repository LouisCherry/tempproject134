package com.epoint.znsb.auditznsbpayment.action;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.znsb.auditznsbpayment.api.entity.AuditZnsbPayment;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.znsb.auditznsbpayment.api.IAuditZnsbPaymentService;

/**
 * 水电气缴费新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2021-05-18 18:32:18]
 */
@RightRelation(AuditZnsbPaymentListAction.class)
@RestController("auditznsbpaymentaddaction")
@Scope("request")
public class AuditZnsbPaymentAddAction  extends BaseController
{
	@Autowired
	private IAuditZnsbPaymentService service;

    @Autowired
    private IAttachService attachService;
    /**
     * 水电气缴费实体对象
     */
  	private AuditZnsbPayment dataBean=null;
  
   /**
  * 缴费类型下拉列表model
  */
 private List<SelectItem>  paytypeModel=null;

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
        dataBean=new AuditZnsbPayment();
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        dataBean.setOperateusername(userSession.getDisplayName());

        List<FrameAttachInfo> logoattachlist = attachService.getAttachInfoListByGuid(getViewData("logocliengguid"));
        if (logoattachlist != null && !logoattachlist.isEmpty()) {
            dataBean.setPngattachguid(getViewData("logocliengguid"));
        }
        else {
            addCallbackParam("msg", "fail");
            return;
        }
	    service.insert(dataBean);
	    addCallbackParam("msg", "保存成功！");
	    dataBean = null;
	}

    /**
     * [logo图片上传]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getPicUploadModel() {
        if (picUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

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
            if (StringUtil.isNotBlank(getViewData("logocliengguid"))) {
                logocliengguid = getViewData("logocliengguid");
            } else {
                logocliengguid = UUID.randomUUID().toString();
                addViewData("logocliengguid", logocliengguid);
            }
            picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(logocliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return picUploadModel;
    }

    /**
     * 保存并新建
     * 
     */
	public void addNew() {
		add();
		dataBean = new AuditZnsbPayment();

        logocliengguid = UUID.randomUUID().toString();
        addViewData("logocliengguid", logocliengguid);
	}

    public AuditZnsbPayment getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditZnsbPayment();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbPayment dataBean)
    {
        this.dataBean = dataBean;
    }
    
  public List<SelectItem> getPaytypeModel(){if(paytypeModel==null){paytypeModel= DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表","缴费机构",null,false));
} return this.paytypeModel;}

}
