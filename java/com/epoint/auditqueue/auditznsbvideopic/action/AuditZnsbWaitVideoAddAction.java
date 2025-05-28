package com.epoint.auditqueue.auditznsbvideopic.action;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.ces.auditznsbwaitvedio.api.IAuditZnsbWaitvedioService;
import com.epoint.ces.auditznsbwaitvedio.api.entity.AuditZnsbWaitvedio;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;


/**
 * 视频图片表新增页面对应的后台
 * 
 * 
 */

@RestController("auditznsbwaitvideoaddaction")
@Scope("request")
public class AuditZnsbWaitVideoAddAction  extends BaseController
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
	private IAuditZnsbWaitvedioService AuditZnsbWaitvedioservice;  
    
    @Autowired
    private IAttachService attachservice;
    
    /**
     * 视频图片表实体对象
     */
  	private AuditZnsbWaitvedio dataBean=null;
  
    private FileUploadModel9 FileUploadModel;
    

    
    /**
     * 文件附件标识
     */
    private String videopicattachguid;

    
    public void pageLoad()
    {
        dataBean=new AuditZnsbWaitvedio();
     
    }

    /**
     * 保存并关闭
     * 
     */
	public void add(){
        dataBean.setRowguid( UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
 

        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
     

        List<FrameAttachInfo> attachInfoList = attachservice.getAttachInfoListByGuid(getViewData("videopicattachguid"));
     
            if (attachInfoList != null && attachInfoList.size() > 0) {
                dataBean.setVideoguid(getViewData("videopicattachguid"));
                AuditZnsbWaitvedioservice.insert(dataBean);
                addCallbackParam("msg", "保存成功！");
            } else {
                addCallbackParam("msg", "文件未上传！");
            }
        

	    dataBean = null;
	}

	 public FileUploadModel9 getFileUploadModel() {        
	        if (FileUploadModel == null) {
	            AttachHandler9 handler = new AttachHandler9() {
	                /**
	                 * 
	                 */
	                private static final long serialVersionUID = 1L;

	                @Override
	                public void afterSaveAttachToDB(Object attach) {
	                    FileUploadModel.getExtraDatas().put("msg", "上传成功");
	                }

					@Override
					public boolean beforeSaveAttachToDB(AttachStorage arg0) {
						
						return true;
					}

	            };
	            videopicattachguid = UUID.randomUUID().toString();
	            addViewData("videopicattachguid", videopicattachguid);
	            FileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(videopicattachguid, null,
	                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
	        }
	        return FileUploadModel;
	    }
 
	    
    public AuditZnsbWaitvedio getDataBean()
    {
        if(dataBean==null)
        {
          dataBean = new AuditZnsbWaitvedio();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbWaitvedio dataBean)
    {
        this.dataBean = dataBean;
    }
    

}
