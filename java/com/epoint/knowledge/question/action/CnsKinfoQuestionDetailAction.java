package com.epoint.knowledge.question.action;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;

/**
 * 知识库提问表详情页面对应的后台
 * 
 * @author wxlin
 * @version [版本号, 2017-06-01 13:43:10]
 */
@RestController("cnskinfoquestiondetailaction")
@Scope("request")
public class CnsKinfoQuestionDetailAction extends BaseController
{
    private CnsKinfoQuestionService kinfoQuestionService=new CnsKinfoQuestionService();
    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    /**
     * 知识库提问表实体对象
     */
    private CnsKinfoQuestion dataBean = null;
    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;
    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    private String cliengguid;


    @Override
    public void pageLoad() {
        String rowguid = getRequestParameter("guid");
        if(StringUtil.isNotBlank(rowguid)){
            dataBean = kinfoQuestionService.getBeanByGuid(rowguid);
            if(StringUtil.isNotBlank(dataBean.getPviguid())){
                this.addCallbackParam("pviguid", dataBean.getPviguid());
            }else{
                this.addCallbackParam("pviguid", "");
            }
            
        }else{
            String pviguid = getRequestParameter("ProcessVersionInstanceGuid");
            this.addCallbackParam("pviguid", pviguid);
            dataBean=kinfoQuestionService.getBeanByOneField("pviguid", pviguid);
        }
        
        
        if (dataBean == null) {
            dataBean = new CnsKinfoQuestion();
        }
        this.addCallbackParam("answerstatus", dataBean.getAnswerstatus());
    }
    public void hasKnow() {
        if("20".equals(dataBean.getAnswerstatus())){
            MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        }
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
    public FileUploadModel9 getFileUploadModel() {
        if (StringUtil.isNotBlank(dataBean.getCliengguid())) {
            cliengguid = dataBean.getCliengguid();
        }
        else {
            if (StringUtil.isBlank(this.getViewData("cliengguid"))) {
                cliengguid = UUID.randomUUID().toString();
            }
            else {
                cliengguid = this.getViewData("cliengguid");
            }
        }
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

    public CnsKinfoQuestion getDataBean() {
        return dataBean;
    }


   
}
