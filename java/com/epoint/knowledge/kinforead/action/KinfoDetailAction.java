package com.epoint.knowledge.kinforead.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserService;

import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.common.domain.CnsKinfoEvl;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.kinforead.service.CnsKinfoEvlService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessVersionService9;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 知识信息表详情页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("kinfodetailaction")
@Scope("request")
public class KinfoDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 1L;
    private CnsKinfoService cnsKinfoService = new CnsKinfoService();
    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();
    private CnsKinfoEvlService kinfoEvlService = new CnsKinfoEvlService();
    private CnsKinfoCollectService kinfoCollectService = new CnsKinfoCollectService();
    private FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
    private MessagesCenterService9 messagesCenterService = new MessagesCenterService9();
    private String evlresult = null;
    private CnsUserService cnsUserService=new CnsUserService();
    private String evlcontent = null;
    /**
     * 知识信息表实体对象
     */
    private CnsKinfo dataBean = null;
    /**
     * 知识信息评价实体对象
     */
    private CnsKinfoEvl cnskinfoEvl = null;
    private List<SelectItem> evlResultModel = null;
    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;
    private WorkflowActivityService9 activityService = new WorkflowActivityService9();
    private FrameConfigService9 frameConfigService = new FrameConfigService9();
    
    private String guid;
    /**
     * 知识库提问表实体对象
     */
    private CnsKinfoQuestion cnsKinfoQuestion;
    private String cliengguid;
    private CnsKinfoQuestionService kinfoQuestionService = new CnsKinfoQuestionService();
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    private FileUploadModel9 fileUploadModel;

    private String messageItemGuid;

    @Override
    public void pageLoad() {
        messageItemGuid = getRequestParameter("MessageItemGuid");
        if (StringUtil.isNotBlank(messageItemGuid)) {
            this.addCallbackParam("messageItem", true);
        }
        guid = getRequestParameter("guid");
        dataBean = cnsKinfoService.getBeanByGuid(guid);
        //获取该知识附件
        List<FrameAttachInfo> frameAttachInfos = frameAttachInfoNewService.getAttachInfoList(dataBean.getAttachguid());
        if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
            this.addCallbackParam("frameAttachInfos", frameAttachInfos);
        }
        this.addCallbackParam("dataBean", dataBean);
        this.addCallbackParam("iscollect", findCollect(guid));
        //得到自己的评价人信息
        List<CnsKinfoEvl> cnsKinfoEvls = kinfoEvlService.getListByOneField("KGUID", guid, "EVLTIME", "DESC");
        this.addCallbackParam("cnskinfoEvlCount", cnsKinfoEvls.size());
        if (cnsKinfoEvls != null && cnsKinfoEvls.size() > 0) {
            this.addCallbackParam("cnskinfoEvlList", cnsKinfoEvls);
        }else {
            this.addCallbackParam("cnskinfoEvlList", "");
        }
        //获取提问列表
        List<CnsKinfoQuestion> ques = kinfoQuestionService.getListByKinfoGuid(guid);
        if(ques!=null && ques.size()>0){
            this.addCallbackParam("ques", ques);
        }else{
            
        }
        
        //修改点击次数
        
//        if (dataBean.getVisittimes() == null) {
//            dataBean.setVisttimes(CnsConstValue.CNS_ONT_INT);
//        }
//        else {
//            dataBean.setVisttimes(CnsConstValue.CNS_ONT_INT + dataBean.getVisittimes());
//        }
        cnsKinfoService.updateRecord(dataBean);
        if (dataBean == null) {
            dataBean = new CnsKinfo();
        }
        this.addCallbackParam("kstatus", dataBean.getKstatus());
        this.addCallbackParam("currentUserName", userSession.getDisplayName());
    }

    /**
     * 
     *  查找该知识是否被收藏
     *  @param cnsk
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String findCollect(String kguid) {
        CnsKinfoCollect collect = kinfoCollectService.findRecordByKguidAndUserGuid(kguid, userSession.getUserGuid());
        if (collect != null) {
            return "1";
        }
        return "0";
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
                    List<CnsKinfoStep> list = kinfoStepService.getAllStepByKguid(guid);
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

    public void audit() {
        dataBean.setOperatedate(new Date());
        dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
        cnsKinfoService.updateRecord(dataBean);
        //------发待办------
        List<FrameUser> frameUsers = cnsUserService.getUserByRoleName(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE);
        if (frameUsers != null && frameUsers.size() > 0) {
            for (FrameUser frameUser : frameUsers) {
                MessageSendUtil.sendWaitHandleMessage("【知识库审核】" + dataBean.getKname(), frameUser.getUserGuid(),
                        frameUser.getDisplayName(),
                        "bmfw/resources/knowledge/kinfoaudit/cnskinfoaudit?rowguid=" + dataBean.getRowguid(),
                        dataBean.getRowguid(), "", "", "知识库审核");
            }
        }
        //存入审核流程表
        //  kinfoStepService.addrecordbykguid(dataBean.getRowguid(), CnsConstValue.KnowledgeOpt.REPORT, null);
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getEvlResultModel() {
        if (evlResultModel == null) {
            evlResultModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "知识评价", null, true));
        }
        return evlResultModel;
    }

    public void setEvlResultModel(List<SelectItem> evlResultModel) {
        this.evlResultModel = evlResultModel;
    }

    public CnsKinfoEvl getCnskinfoEvl() {
        return cnskinfoEvl;
    }

    public void setCnskinfoEvl(CnsKinfoEvl cnskinfoEvl) {
        this.cnskinfoEvl = cnskinfoEvl;
    }

    public String getEvlresult() {
        return evlresult;
    }

    public void setEvlresult(String evlresult) {
        this.evlresult = evlresult;
    }

    public String getEvlcontent() {
        return evlcontent;
    }

    public void setEvlcontent(String evlcontent) {
        this.evlcontent = evlcontent;
    }

    public void addEvl() {
        cnskinfoEvl = new CnsKinfoEvl();
        if (StringUtil.isNotBlank(evlresult) && StringUtil.isNotBlank(evlcontent)) {
            cnskinfoEvl.setRowguid(UUID.randomUUID().toString());
            cnskinfoEvl.setEvltime(new Date());
            cnskinfoEvl.setKguid(guid);
            cnskinfoEvl.setEvlresult(evlresult);
            cnskinfoEvl.setEvlperson(userSession.getDisplayName());
            cnskinfoEvl.setEvlpersonguid(userSession.getUserGuid());
            cnskinfoEvl.setEvlcontent(evlcontent);
            cnskinfoEvl.setOuguid(userSession.getOuGuid());
            cnskinfoEvl.setOuname(userSession.getOuName());
            kinfoEvlService.addRecord(cnskinfoEvl);
        }
    }

    /**
     * 
     * 收藏  
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void collect() {
        CnsKinfoCollect cnsKinfoCollect = new CnsKinfoCollect();
        cnsKinfoCollect.setRowguid(UUID.randomUUID().toString());
        cnsKinfoCollect.setOperatedate(new Date());
        cnsKinfoCollect.setKguid(dataBean.getRowguid());
        cnsKinfoCollect.setUserguid(userSession.getUserGuid());
        kinfoCollectService.addRecord(cnsKinfoCollect);
    }

    /**
     * 
     *  取消收藏
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void canlCollect() {
        kinfoCollectService.deleteRecordByKguidAndUserguid(dataBean.getRowguid(), userSession.getUserGuid());
    }

    /**
     * 
     *  关闭待办信息 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void closeMsg() {
        //---结束掉该知识待办---
        
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
    }
    
    public void addQuestion(){
            String categoryguid = dataBean.getCategoryguid();
            cnsKinfoQuestion = new CnsKinfoQuestion();
            String categoryname = dataBean.getCategoryname();
            cnsKinfoQuestion.setRowguid(UUID.randomUUID().toString());
            cnsKinfoQuestion.setOperatedate(new Date());
            cnsKinfoQuestion.setOperateusername(userSession.getDisplayName());
            cnsKinfoQuestion.setKinfocategoryguid(categoryguid);
            cnsKinfoQuestion.setKinfocategoryname(categoryname);
            cnsKinfoQuestion.setAsktime(new Date());
            cnsKinfoQuestion.setAskpersonguid(userSession.getUserGuid());
            cnsKinfoQuestion.setAskpersonname(userSession.getDisplayName());
            cnsKinfoQuestion.setAnswerstatus(CnsConstValue.CNS_Answer_Status.ANSWER_WAIT);
            cnsKinfoQuestion.setKstatus(CnsConstValue.KinfoStatus.NO_AUDIT);
            cnsKinfoQuestion.setIskinfo("1");
            cnsKinfoQuestion.setKinfoguid(dataBean.getRowguid());
            cnsKinfoQuestion.setDeptguid(dataBean.getOuguid());
            cnsKinfoQuestion.setDeptname(dataBean.getOuname());
            
            if(StringUtil.isNotBlank(evlcontent)){
                cnsKinfoQuestion.setQuestioncontent(evlcontent);
            }
            cnsKinfoQuestion.setPviguid(UUID.randomUUID().toString());
            kinfoQuestionService.addRecord(cnsKinfoQuestion);
            this.addCallbackParam("msg", "提问成功！");
            List<FrameUser> frameUsers = this.cnsUserService.getUserListByOu(dataBean.getOuguid(), "成员单位", 1);
            if (frameUsers != null && frameUsers.size() > 0) {
                for (FrameUser frameUser : frameUsers) {
                    MessageSendUtil.sendWaitHandleMessage("【知识库提问审核】" + cnsKinfoQuestion.getQuestioncontent(), frameUser.getUserGuid(),
                            frameUser.getDisplayName(),
                            CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoask/cnskinfoquestionanswer?guid=" + cnsKinfoQuestion.getRowguid(),
                            cnsKinfoQuestion.getRowguid(), cnsKinfoQuestion.getPviguid(), "", "知识库提问");
                }
            }
          //存入审核流程表
            kinfoStepService.addKinfoaskAuditStep(cnsKinfoQuestion, "知识提问审核",
                    "提问上报", "知识提问");
            
            
        
        
    }
}
