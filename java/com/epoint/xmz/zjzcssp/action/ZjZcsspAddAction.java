package com.epoint.xmz.zjzcssp.action;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.zjzcssp.api.entity.ZjZcssp;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.zjzcssp.api.IZjZcsspService;

/**
 * 邹城随手拍表新增页面对应的后台
 * 
 * @author 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@RightRelation(ZjZcsspListAction.class)
@RestController("zjzcsspaddaction")
@Scope("request")
public class ZjZcsspAddAction  extends BaseController
{
	@Autowired
	private IZjZcsspService service;  
	
	 @Autowired
	 private IAttachService attachService;
	    
	 
    /**
     * 邹城随手拍表实体对象
     */
  	private ZjZcssp dataBean=null;
  	
  	 /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;
    
    /**
     * 附件cliengguid
     */
    
    private String ItemResultPhoto = "";
  
  

    public void pageLoad()
    {
        dataBean=new ZjZcssp();
        if (StringUtil.isBlank(getViewData("ItemResultPhoto"))) {
        	addViewData("ItemResultPhoto", UUID.randomUUID().toString());
        }else {
        	ItemResultPhoto = getViewData("ItemResultPhoto");
        }
    }

    /**
     * 保存并关闭
     * 
     */
	public void add()
    {
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setAttachguid(ItemResultPhoto);
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
		dataBean = new ZjZcssp();
	}

    public ZjZcssp getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new ZjZcssp();
        }
        return dataBean;
    }

    public void setDataBean(ZjZcssp dataBean)
    {
        this.dataBean = dataBean;
    }
    
    /**
     * 上传图片
     * @return
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage storage) {
                    byte[] picContent = FileManagerUtil.getContentFromInputStream(storage.getIn());
                    String picContentType = storage.getContentType();
                    String base64Str = Base64Util.encode(picContent);
                    fileUploadModel.getExtraDatas().put("lblPicture",
                            "data:" + picContentType + ";base64," + base64Str);

                    storage.setIn(new ByteArrayInputStream(picContent));
                    
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setAttachGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName(storage.getAttachFileName());
                    frameAttachInfo.setCliengGuid(getViewData("ItemResultPhoto"));
                    frameAttachInfo.setCliengTag("ItemResultPhoto");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType(storage.getContentType());
                    frameAttachInfo.setAttachLength(Long.valueOf((long) storage.getSize()));
                    
                    attachService.addAttach(frameAttachInfo, storage.getIn());
                    
                    
                    return false;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("ItemResultPhoto"),
                    "ItemResultPhoto", null, handler, UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName()));
        }
        return fileUploadModel;
    }
    

}
