package com.epoint.knowledge.question.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.api.IMessageService;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserService;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
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
@RestController("cnskinfoansweraction")
@Scope("request")
public class CnsKinfoAnswerAction extends BaseController
{

    private CnsKinfoQuestionService kinfoQuestionService = new CnsKinfoQuestionService();
    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;
    private CnsKinfoService kinfoService = new CnsKinfoService();
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    private CnsUserService cnsUserService = new CnsUserService();

    /**
     * 知识库提问表实体对象
     */
    private CnsKinfoQuestion dataBean = null;

    @Autowired
    private  IMessageService messageService;
    private CnsKinfo cnsKinfo = null;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    private String cliengguid = "";

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = kinfoQuestionService.getBeanByGuid(guid);
        this.addCallbackParam("kstatus", dataBean.getKstatus());
        this.addCallbackParam("guid", guid);
        String iskinfo = dataBean.getIskinfo();
        if(StringUtil.isBlank(iskinfo)){
            iskinfo="0";
        }
        this.addCallbackParam("iskinfo", iskinfo);
        if (dataBean == null) {
            dataBean = new CnsKinfoQuestion();
        }
        if (StringUtils.isNoneBlank(dataBean.getCliengguid())) {
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
    }

    /**
     * 答复并关闭
     * 
     */
    public void answer() {
        MessagesCenterService9 service = new MessagesCenterService9();
        MessagesCenter msg = messageService.getWaitHandleByPviguid(dataBean.getPviguid(),userSession.getUserGuid());
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        if(msg!=null){
            MessageSendUtil.sendWaitHandleMessage("【通知提问人】" + "提问答复", msg.getFromUser(),
                    msg.getFromDispName(),
                    CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoask/cnskinfoquestiondetail?guid=" + dataBean.getRowguid(),
                    dataBean.getRowguid(), dataBean.getPviguid(), "", "通知提问人");
        }
        
        
        cnsKinfo = kinfoService.getBeanByGuid(dataBean.getKguid());
        if (cnsKinfo == null) {
            cnsKinfo = new CnsKinfo();
            cnsKinfo.setKname(dataBean.getQuestioncontent());
            cnsKinfo.setCategoryguid(dataBean.getKinfocategoryguid());
            cnsKinfo.setCategoryname(dataBean.getKinfocategoryname());
            cnsKinfo.setBegindate(dataBean.getBegindate());
            cnsKinfo.setEffectdate(dataBean.getEffectdate());
            cnsKinfo.setKauthor(userSession.getDisplayName());
            cnsKinfo.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
            cnsKinfo.setKcontent(dataBean.getAnswercontent());
            cnsKinfo.setRowguid(UUID.randomUUID().toString());
            cnsKinfo.setOperatedate(new Date());
            cnsKinfo.setOperateusername(userSession.getDisplayName());
            cnsKinfo.setOuname(dataBean.getDeptname());
            cnsKinfo.setOuguid(dataBean.getDeptguid());
            cnsKinfo.setCreatdate(new Date());
            cnsKinfo.setPublishpguid(userSession.getUserGuid());
            cnsKinfo.setPublishperson(userSession.getDisplayName());
            cnsKinfo.setAttachguid(cliengguid);
            cnsKinfo.setIsdel(CnsConstValue.CNS_ZERO_STR);
            //根据类别查找编号存入实体
            if (StringUtil.isNotBlank(cnsKinfo.getCategoryguid())) {
                CnsKinfoCategory parentCat = kinfoCategoryService.getBeanByGuid(cnsKinfo.getCategoryguid());
                cnsKinfo.setCategorycode(parentCat.getCategorycode());
                cnsKinfo.setCategoryname(parentCat.getCategoryname());
                //设置全文检索code
                //cnsKinfo = kinfoService.setCategoryCodeByLevl(cnsKinfo, cnsKinfo.getCategorycode());
            }
            cnsKinfo.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
            kinfoService.addRecord(cnsKinfo);
            //存入审核流程表
            kinfoStepService.addKinfoAuditStep(cnsKinfo, CnsConstValue.KnowledgeActivity.ACTIVITY_DEPT,
                    "提问上报", "答复并上报");
        }
        else {
            cnsKinfo.setBegindate(dataBean.getBegindate());
            cnsKinfo.setEffectdate(dataBean.getEffectdate());
            cnsKinfo.setKcontent(dataBean.getAnswercontent());
            cnsKinfo.setAttachguid(cliengguid);
            cnsKinfo.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
            kinfoService.updateRecord(cnsKinfo);
            //存入审核流程表
            kinfoStepService.addKinfoAuditStep(cnsKinfo, CnsConstValue.KnowledgeActivity.ACTIVITY_DEPT,
                    CnsConstValue.KnowledgeOpt.REPORT_AGAIN, "重新上报");
        }
        //将知识表关联guid插入知识库提问表
        dataBean.setKguid(cnsKinfo.getRowguid());
        dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
        dataBean.setAnswertime(new Date());
        dataBean.setCliengguid(cliengguid);
        dataBean.setAnswerpersonguid(userSession.getUserGuid());
        dataBean.setAnswerpersonname(userSession.getDisplayName());
        dataBean.setAnswerstatus(CnsConstValue.CNS_Answer_Status.ANSWER_FINISH);
        
        //system.out.println(cnsKinfo.getBegindate());
        kinfoQuestionService.updateRecord(dataBean);
        this.addCallbackParam("msg", "答复成功！");
        
        // ------发待办------
        List<FrameUser> frameUsers = cnsUserService.getUserByRoleNameAndCategoryguid(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE, cnsKinfo.getCategoryguid());
               
        if (frameUsers != null && frameUsers.size() > 0) {
            for (FrameUser frameUser : frameUsers) {
                String pviguid= cnsKinfo.getPviguid();
                if(StringUtil.isBlank(pviguid)){
                    pviguid = UUID.randomUUID().toString();
                    cnsKinfo.setPviguid(pviguid);
                    kinfoService.updateRecord(cnsKinfo);
                }
                MessageSendUtil.sendWaitHandleMessage("【知识库审核】" + cnsKinfo.getKname(), frameUser.getUserGuid(),
                        frameUser.getDisplayName(),
                        CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoaudit/kinfoaudit?guid=" + cnsKinfo.getRowguid(),
                        cnsKinfo.getRowguid(), pviguid, "", "知识库审核");
            }
        }
        //存入审核流程表
        //kinfoStepService.addrecordbykguid(cnsKinfo.getRowguid(), CnsConstValue.KnowledgeOpt.KINFO_REPORT, null);
        kinfoStepService.addKinfoaskAuditStep(dataBean, "知识提问审核",
                "提问答复", "已答复");
        dataBean = null;
        cnsKinfo = null;
    }

    /**
     * 拒绝答复并关闭
     * 
     */
    public void refuseAnswer(String reasontype, String reasoncontent) {
        MessagesCenterService9 service = new MessagesCenterService9();
        MessagesCenter msg = messageService.getWaitHandleByPviguid(dataBean.getPviguid(),userSession.getUserGuid());
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        dataBean.setRefusereasontype(reasontype);
        dataBean.setRefusereasoncontext(reasoncontent);
        dataBean.setAnswertime(new Date());
        dataBean.setCliengguid(cliengguid);
//       /dataBean.setAnswercontent("");
        dataBean.setAnswerpersonguid(userSession.getUserGuid());
        dataBean.setAnswerpersonname(userSession.getDisplayName());
        dataBean.setAnswerstatus(CnsConstValue.CNS_Answer_Status.ANSWER_REFUSE);
        kinfoQuestionService.updateRecord(dataBean);
        if(msg!=null){
            MessageSendUtil.sendWaitHandleMessage("【通知提问人】" + dataBean.getRefusereasoncontexte(), msg.getFromUser(),
                    msg.getFromDispName(),
                    CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoask/cnskinfoquestiondetail?guid=" + dataBean.getRowguid(),
                    dataBean.getRowguid(), dataBean.getPviguid(), "", "通知提问人");
        }
        
        
        this.addCallbackParam("msg", "已拒绝答复！");
        kinfoStepService.addKinfoaskAuditStep(dataBean, "知识提问审核",
                "提问答复", "拒绝答复");
        dataBean = null;
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

    public void setDataBean(CnsKinfoQuestion dataBean) {
        this.dataBean = dataBean;
    }

    public CnsKinfoQuestion getDataBean() {
        return dataBean;
    }
    
    public void dafu() {
        cnsKinfo = kinfoService.getBeanByGuid(dataBean.getStr("kinfoguid"));
        MessagesCenterService9 service = new MessagesCenterService9();
        MessagesCenter msg = messageService.getWaitHandleByPviguid(dataBean.getPviguid(),userSession.getUserGuid());
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        if(msg!=null){
            MessageSendUtil.sendWaitHandleMessage("【通知提问人】" + "提问答复", msg.getFromUser(),
                    msg.getFromDispName(),
                    CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoask/cnskinfoquestiondetail?guid=" + dataBean.getRowguid(),
                    dataBean.getRowguid(), dataBean.getPviguid(), "", "通知提问人");
            
        }
        
        
        //将知识表关联guid插入知识库提问表
//        dataBean.setKguid(cnsKinfo.getRowguid());
        //dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
        dataBean.setAnswertime(new Date());
        dataBean.setCliengguid(cliengguid);
        dataBean.setAnswerpersonguid(userSession.getUserGuid());
        dataBean.setAnswerpersonname(userSession.getDisplayName());
        dataBean.setAnswerstatus(CnsConstValue.CNS_Answer_Status.ANSWER_FINISH);
        dataBean.setBegindate(cnsKinfo.getBegindate());
        dataBean.setEffectdate(cnsKinfo.getEffectdate());
        kinfoQuestionService.updateRecord(dataBean);
        this.addCallbackParam("msg", "答复成功！");
        
        
        //存入审核流程表
        //kinfoStepService.addrecordbykguid(cnsKinfo.getRowguid(), CnsConstValue.KnowledgeOpt.KINFO_REPORT, null);
        kinfoStepService.addKinfoaskAuditStep(dataBean, "知识提问审核",
                "提问答复", "已答复");
        dataBean = null;
        cnsKinfo = null;
    }
}
