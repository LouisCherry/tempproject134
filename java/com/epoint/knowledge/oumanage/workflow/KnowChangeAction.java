package com.epoint.knowledge.oumanage.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserService;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.CnsWorkflowValue;
import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoAbandon;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.oumanage.service.CnsKinfoAbandonService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.search.inteligentsearch.restful.sdk.InteligentSearchRestNewSdk;
import com.epoint.search.inteligentsearch.sdk.domain.IndexFieldFormat;
import com.epoint.workflow.bizlogic.api.WFAPI9;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessVersionService9;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 知识信息表修改页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("knowchangeaction")
@Scope("request")
public class KnowChangeAction extends BaseController
{
    transient private static Logger log = Logger.getLogger(KnowSubmitAction.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private transient CnsKinfoService kinfoService = new CnsKinfoService();

    private transient CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    private transient CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    private transient CnsKinfoAbandonService cnsKinfoAbandonService=new CnsKinfoAbandonService();
    
    private transient CnsKinfoCollectService collectService = new CnsKinfoCollectService();

    private FrameUserService9 frameUserService = new FrameUserService9();

    private transient ResourceModelService resourceModelService = new ResourceModelService();

    private transient CnsKinfoStepService cnsKinfoStepService = new CnsKinfoStepService();

    private transient CnsUserService cnsUserService = new CnsUserService();

    private FrameConfigService9 frameConfigService = new FrameConfigService9();

    private WorkflowActivityService9 activityService = new WorkflowActivityService9();

   

    private FrameConfigService9 configService9 = new FrameConfigService9();

    /**
     * 附件上传model
     */
    private  FileUploadModel9 fileUploadModel = null;

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo dataBean = null;

    private CnsKinfoAbandon cnsKinfoAbandon=null;

    /**
     * 表格控件model
     */
    private transient DataGridModel<CnsKinfoStep> model;

    /**
     * 知识 唯一标识
     */
    private String guid;

    /**
     * 附件的guid
     */
    private String cliengguid;

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        dataBean = kinfoService.getBeanByGuid(guid);
        if (dataBean == null) {
            dataBean = new CnsKinfo();
        }
        this.addCallbackParam("categoryname", dataBean.getCategoryname());
        this.addCallbackParam("categoryguid", dataBean.getCategoryguid());
        this.addCallbackParam("kstatus", dataBean.getKstatus());
        if (CnsConstValue.KinfoStatus.NEED_REPORT.equals(dataBean.getKstatus())) {
            this.addCallbackParam("stepstatus", "hidaccording");
        }
        if (StringUtils.isNoneBlank(dataBean.getAttachguid())) {
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
     * 变更知识
     * 
     */
    public void changeKinfo() {
        

        String msg = "";
        //根据类别查找编号存入实体
        if (StringUtil.isNotBlank(dataBean.getCategoryguid())) {
            CnsKinfoCategory parentCat = kinfoCategoryService.getBeanByGuid(dataBean.getCategoryguid());
            dataBean.setCategorycode(parentCat.getCategorycode());
            dataBean.setCategoryname(parentCat.getCategoryname());
            dataBean = kinfoService.setCategoryCodeByLevl(dataBean, parentCat.getCategorycode());
        }
        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE)) {
            dataBean.setKstatus(CnsConstValue.KinfoStatus.PASS_AUDIT);
            dataBean.setIsenable(CnsConstValue.CNS_ONT_INT.toString());
            //直接修改数据
            kinfoService.updateRecord(dataBean);
            //存入审核流程表
            kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_AUDIT,
                    "变更上报", "知识变更上报同时审核通过");
          //删除下架知识
            cnsKinfoAbandon=cnsKinfoAbandonService.getBeanByOneField("kguid", dataBean.getRowguid());
            if(cnsKinfoAbandon!=null)
            {
                cnsKinfoAbandonService.delete(cnsKinfoAbandon);
            }

            msg = "变更知识成功添加到知识库！";
        }
        else {
            //editguid为空且状态为通过的为新增一条知识并上报
            if (StringUtil.isBlank(dataBean.getEditguid())
                    && CnsConstValue.KinfoStatus.PASS_AUDIT.equals(dataBean.getKstatus())) {
                //复制一条知识数据
                dataBean.setEditguid(dataBean.getRowguid());
                dataBean.setRowguid(UUID.randomUUID().toString());
                //默认为不启用
                
                dataBean.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
                dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
                kinfoService.addRecord(dataBean);//将数据插入表中 
                List<CnsKinfoStep> cnsKinfoStepList = cnsKinfoStepService.getListByOneField("kguid",
                        dataBean.getEditguid());
                if (cnsKinfoStepList != null && cnsKinfoStepList.size() > 0) {
                    for (CnsKinfoStep cnsKinfoStep : cnsKinfoStepList) {
                        CnsKinfoStep kinfoStep = (CnsKinfoStep) cnsKinfoStep.clone();
                        kinfoStep.setRowguid(UUID.randomUUID().toString());
                        kinfoStep.setKguid(dataBean.getRowguid());
                        kinfoStepService.addRecord(kinfoStep);
                    }
                }
                List<FrameUser> frameUsers = cnsUserService.getUserByRoleNameAndCategoryguid(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE, dataBean.getCategoryguid());
                if(frameUsers==null || frameUsers.size()==0 ){
                    addCallbackParam("msg", "请联系管理员添加相关处理人！");
                    return;
                }
                //发送代办
                if (frameUsers != null && frameUsers.size() > 0) {
                    for (FrameUser frameUser : frameUsers) {
                        MessageSendUtil.sendWaitHandleMessage("【知识库审核】" + dataBean.getKname(), frameUser.getUserGuid(),
                                frameUser.getDisplayName(),
                                CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoaudit/kinfoaudit?guid=" + dataBean.getRowguid(),
                                dataBean.getRowguid(),dataBean.getPviguid(), "", "知识库审核");
                    }
                }
            }
                
                //存入审核流程表
                kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_DEPT,
                        "变更上报", "知识变更上报");
                msg = "知识变更上报成功,请等待审核结果！";
            }
            addCallbackParam("msg", msg);
            kinfoService.updateRecord(dataBean);

        
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

    public LazyTreeModal9 getCategoryModal() {
        return resourceModelService.getCategoryOuModel(userSession.getOuGuid());
    }

    public CnsKinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(CnsKinfo dataBean) {
        this.dataBean = dataBean;
    }

}
