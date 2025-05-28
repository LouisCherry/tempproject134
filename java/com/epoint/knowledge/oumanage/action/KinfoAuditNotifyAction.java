package com.epoint.knowledge.oumanage.action;

import java.io.IOException;
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
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.knowledge.api.IMessageService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;


/**
 * 知识信息表详情页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("kinfoauditnotifyaction")
@Scope("request")
public class KinfoAuditNotifyAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private MessagesCenterService9 messagesCenterService = new MessagesCenterService9();

    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    private CnsKinfoService kinfoService = new CnsKinfoService();

    private FileUploadModel9 fileUploadModel;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;
    
    @Autowired
    private  IMessageService service;
    

    private String messageItemGuid;

    private CnsKinfo dataBean;

    private String cliengguid;

    @Override
    public void pageLoad() {
        messageItemGuid = getRequestParameter("MessageItemGuid");
        String guid = this.getRequestParameter("guid");
        dataBean = kinfoService.getBeanByGuid(guid);
        if (StringUtil.isNotBlank(dataBean.getAttachguid())) {
            cliengguid = dataBean.getAttachguid();
        }
        else {
            if (StringUtil.isBlank(this.getViewData("cliengguid"))) {
                cliengguid = UUID.randomUUID().toString();
            }
            else {
                cliengguid = this.getViewData("cliengguid");
            }
        }
    }

    /**
     * 
     *  关闭待办信息 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void closeMsg() {
        //---结束掉该知识待办---
        service.deleteByMessageItemGuid(messageItemGuid);
    }

    public CnsKinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(CnsKinfo dataBean) {
        this.dataBean = dataBean;
    }

    /**
     * 
     *  [获取审核流程表格]
     *  [功能详细描述]
     */
    public DataGridModel<CnsKinfoStep> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<CnsKinfoStep>()
            {
                @Override
                public List<CnsKinfoStep> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<CnsKinfoStep> list = kinfoStepService.getAllStepByKguid(dataBean.getRowguid());
                    this.setRowCount(list.size());
                    return list;
                }
            };
        }
        return model;
    }

    /**
     * 
     *  [附件上传]
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                public boolean beforeSaveAttachToDB(Object attach) {
                    FrameAttachStorage storage = (FrameAttachStorage) attach;
                    byte[] contents = FileManagerUtil.getContentFromInputStream(storage.getContent());
                    try {
                        storage.getContent().reset();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileUploadModel.getExtraDatas().put("signPicture",
                            "data:" + storage.getContentType() + ";base64," + Base64Util.encode(contents));
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    // TODO Auto-generated method stub
                    return false;
                }
            };
            addViewData("cliengguid", cliengguid);
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
}
