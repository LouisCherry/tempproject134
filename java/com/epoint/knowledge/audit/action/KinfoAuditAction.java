package com.epoint.knowledge.audit.action;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
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
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.api.IMessageService;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.common.CnsConstValue;

import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;


/**
 * 知识信息表详情页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("kinfoauditaction")
@Scope("request")
public class KinfoAuditAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private CnsKinfoService cnsKinfoService = new CnsKinfoService();

    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    private CnsKinfoQuestionService kinfoQuestionService = new CnsKinfoQuestionService();

    private MessagesCenterService9 messageservice = new MessagesCenterService9();

    private CnsKinfoCollectService collectService = new CnsKinfoCollectService();

    private FrameUserService9 frameUserService = new FrameUserService9();

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo dataBean = null;
    private CnsKinfoQuestion cnsKinfoQuestion = null;
    private String cliengguid;
    private FileUploadModel9 fileUploadModel;
    
    @Autowired
    private IMessageService service;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;

    @Override
    public void pageLoad() {
        String guid = this.getRequestParameter("guid");
        dataBean = cnsKinfoService.getBeanByGuid(guid);
        if (dataBean == null) {
            dataBean = new CnsKinfo();
        }
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
     *  审核通过操作    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveVerify() {
        dataBean.setKstatus(CnsConstValue.KinfoStatus.PASS_AUDIT);
        dataBean.setIsenable(CnsConstValue.CNS_ONT_INT.toString());
        dataBean.setVisttimes(CnsConstValue.CNS_ZERO_INT);
        cnsKinfoService.updateRecord(dataBean);
        cnsKinfoQuestion = kinfoQuestionService.getBeanByOneField("kguid", dataBean.getRowguid());
        if(cnsKinfoQuestion!=null)
        {
            cnsKinfoQuestion.setKstatus(CnsConstValue.KinfoStatus.PASS_AUDIT);
            kinfoQuestionService.updateRecord(cnsKinfoQuestion);
        }
//        //获取当前用户的待办
        MessagesCenter msg = service.getWaitHandleByPviguid(dataBean.getPviguid(),userSession.getUserGuid());        
//        //办结该知识的所有待办
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
//        //发起新的通知待办待办
        if(msg!=null){
            MessageSendUtil.sendWaitHandleMessage("【知识审核通过】" + dataBean.getKname(), msg.getFromUser(),
                    msg.getFromDispName(),
                    CnsConstValue.GXHML+"/individuation/overall/knowledge/oumanage/knowworkflow/knownotify?guid=" + dataBean.getRowguid(), dataBean.getRowguid(), dataBean.getPviguid(), "", "知识库审核");
        }
        
        //存入审核流程表
        kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_AUDIT,
                CnsConstValue.KnowledgeOpt.AUDIT_PASS, "审核通过");
        //变更guid不为空，说明为变更数据
        if (StringUtil.isNotBlank(dataBean.getEditguid())) {
            List<CnsKinfoCollect> collects = collectService.getListByOneField("KGUID", dataBean.getEditguid());
            if (collects != null && collects.size() > 0) {
                for (CnsKinfoCollect collect : collects) {
                    FrameUser frameUser = frameUserService.getUserByUserField("USERGUID", collect.getUserguid());
                    //将新的kguid设置给该收藏数据
                    collect.setKguid(dataBean.getRowguid());
                    collectService.updateRecord(collect);
                }
            }
            //---结束原知识待办----
            CnsKinfo info = cnsKinfoService.getBeanByGuid(dataBean.getEditguid());
            MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getEditguid());
            //更新父类操作
            CnsKinfo oldKinfo = cnsKinfoService.getBeanByGuid(dataBean.getEditguid());
            MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getEditguid());
            oldKinfo.setIsdel(CnsConstValue.CNS_ONT_STR);
            oldKinfo.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
            cnsKinfoService.updateRecord(oldKinfo);
            //删除这条原来数据的步骤
            kinfoStepService.deleteRecords("KGUID", dataBean.getEditguid());
           
            //将edit清空
            dataBean.setEditguid(null);
            cnsKinfoService.updateRecord(dataBean);
        }
        
        addCallbackParam("msg", "审核通过！");
    }

    /**
     * 
     * 审核不通过操作
     *  @param option    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveBack(String option) {
        dataBean.setKstatus(CnsConstValue.KinfoStatus.BACK_AUDIT);
        cnsKinfoService.updateRecord(dataBean);
        //cnsKinfoQuestion = kinfoQuestionService.getBeanByOneField("kguid", dataBean.getRowguid());
        //---结束待办----
        MessagesCenter msg = service.getWaitHandleByPviguid(dataBean.getPviguid(),userSession.getUserGuid());
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        if (cnsKinfoQuestion != null) {
            String pviguid = dataBean.getPviguid();
            if(StringUtil.isBlank(pviguid)){
                pviguid=UUID.randomUUID().toString();
                        
            }
            cnsKinfoQuestion.setKstatus(CnsConstValue.KinfoStatus.BACK_AUDIT);
            kinfoQuestionService.updateRecord(cnsKinfoQuestion);
            if(msg!=null){
                MessageSendUtil.sendWaitHandleMessage("【提问审核退回】" + cnsKinfoQuestion.getQuestioncontent(), msg.getFromUser(),
                        msg.getFromDispName(),
                        "lhzwfw/individuation/overall/knowledge/kinfoask/cnskinfoquestionanswer?guid=" + cnsKinfoQuestion.getRowguid(),
                        cnsKinfoQuestion.getRowguid(), pviguid, "", "提问审核退回");
            }
            
        }
        //---发起退回待办-----
        else {
            //WorkflowWorkItem item = new CommonWorkflowService().getItem(dataBean.getPviguid(),msg.getFromUser() , 1);
            if(msg!=null){
                MessageSendUtil.sendWaitHandleMessage("【知识退回】" + dataBean.getKname(), msg.getFromUser(),
                        msg.getFromDispName(),
                        "lhzwfw/individuation/overall/knowledge/kinfototal/kinfoedit?guid=" + dataBean.getRowguid(),dataBean.getRowguid(), dataBean.getPviguid(), "",
                        "知识库上报退回");
            }
            
            
        }
        //存入审核流程表
        kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_AUDIT,
                CnsConstValue.KnowledgeOpt.AUDIT_BACK, option);
        addCallbackParam("msg", "审核不通过！");
        
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

    public CnsKinfo getDataBean() {
        return dataBean;
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
                    fileUploadModel.getExtraDatas().put("msg", "上传成功");
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
