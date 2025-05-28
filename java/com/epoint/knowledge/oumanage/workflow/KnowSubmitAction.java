
package com.epoint.knowledge.oumanage.workflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.orga.role.service.FrameRoleService9;
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
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserService;
import com.epoint.knowledge.common.CnsUserSession;
import com.epoint.knowledge.common.CnsWorkflowValue;

import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.search.inteligentsearch.restful.sdk.InteligentSearchRestNewSdk;
import com.epoint.search.inteligentsearch.sdk.domain.IndexFieldFormat;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessVersionService9;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 知识库信息表新增页面对应的后台
 * 
 * @author wry
 * @version [版本号, 2016-11-03 19:45:26]
 */
@RestController("knowsubmitaction")
@Scope("request")
public class KnowSubmitAction extends BaseController
{
    transient private static Logger log = Logger.getLogger(KnowSubmitAction.class);

    private static final long serialVersionUID = 1L;
    private transient CnsKinfoService kinfoService = new CnsKinfoService();
    private transient CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();
    private transient CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();
    private transient CnsUserService cnsUserService = new CnsUserService();
    private transient ResourceModelService resourceModelService = new ResourceModelService();

   
    private WorkflowActivityService9 activityService = new WorkflowActivityService9();
    private FrameConfigService9 frameConfigService = new FrameConfigService9();
    
    /**
     * 表格控件model
     */
    private transient DataGridModel<CnsKinfoStep> model;
    private FrameRoleService9 frameRoleService = new FrameRoleService9();
    /**
     * 知识库信息表实体对象
     */
    private CnsKinfo dataBean = null;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    /**
     * 附件的guid
     */
    private String cliengguid;

    String categoryGuid = "";

    //判断是否为暂存知识
    Boolean mark = true;

    @Override
    public void pageLoad() {
        String guid = this.getRequestParameter("guid");
       
        if (StringUtil.isNotBlank(guid)) {
            dataBean = kinfoService.getBeanByGuid(guid);
        }

        if (dataBean != null) {
            categoryGuid = dataBean.getCategoryguid();
        }
        else {
            dataBean = new CnsKinfo();
            categoryGuid = this.getRequestParameter("categoryguid");
        }
        //如果父节点为空
        if (StringUtil.isNotBlank(categoryGuid)) {
            CnsKinfoCategory kinfoCategory = kinfoCategoryService.getBeanByGuid(categoryGuid);
            //如果选择的是叶子节点，才会把类别传递过去。
            dataBean.setCategoryguid(categoryGuid);
            dataBean.setCategoryname(kinfoCategory.getCategoryname());
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
     * 知识上报，知识审核岗上报直接审核通过，其他都需要走审核
     * 
     */
    public void submit() {

        String msg = "  "; 
        //根据类别查找编号存入实体
        if (StringUtil.isNotBlank(categoryGuid)) {
            CnsKinfoCategory category = kinfoCategoryService.getBeanByGuid(dataBean.getCategoryguid());
            dataBean.setCategorycode(category.getCategorycode());
           }
        //没有暂存直接新增
        if (StringUtil.isBlank(dataBean.getRowguid())) {
            //设置知识信息
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setOuname(userSession.getOuName());
            dataBean.setOuguid(userSession.getOuGuid());
            dataBean.setCreatdate(new Date());
            dataBean.setPublishpguid(userSession.getUserGuid());
            dataBean.setPublishperson(userSession.getDisplayName());
            dataBean.setKstatus(CnsConstValue.KinfoStatus.NEED_REPORT);
            dataBean.setAttachguid(cliengguid);
            dataBean.setIsdel(CnsConstValue.CNS_ZERO_STR);
            dataBean.setPviguid(UUID.randomUUID().toString());
            kinfoService.addRecord(dataBean);
            
        }
        List<FrameRole> rolename = frameRoleService.listRoleByUserGuid(UserSession.getInstance().getUserGuid());
        String str = "";
        for(FrameRole item:rolename){
            str+=item.getRoleName();
            str+=";";
        }
        //如果是该人员是审核岗，则直接审核通过否则则送下一步,并把状态改为启用
        if (str.contains(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE)) {
            //CnsUserSession.getInstance().getUserRolesName()
            dataBean.setKstatus(CnsConstValue.KinfoStatus.PASS_AUDIT);
            dataBean.setIsenable(CnsConstValue.CNS_ONT_INT.toString());
            //存入审核流程表
            kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_AUDIT,
                    CnsConstValue.KnowledgeOpt.KINFO_REPORT, "知识上报同时审核通过");
            //插入智能检索索引
            msg = "知识成功添加到知识库！";
        }
        else {
            dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
            List<FrameUser> frameUsers = cnsUserService.getUserByRoleNameAndCategoryguid(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE, dataBean.getCategoryguid());
            if(frameUsers==null || frameUsers.size()==0 ){
                addCallbackParam("error", "请联系管理员添加相关处理人！");
                return;
            }
            
            String url = CnsConstValue.GXHML;
            if (frameUsers != null && frameUsers.size() > 0) {
                for (FrameUser frameUser : frameUsers) {
                    MessageSendUtil.sendWaitHandleMessage("【知识库审核】" + dataBean.getKname(), frameUser.getUserGuid(),
                            frameUser.getDisplayName(),
                            CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoaudit/kinfoaudit?guid=" + dataBean.getRowguid(),
                            dataBean.getRowguid(),dataBean.getPviguid(), "", "知识库审核");
                }
            }
            //存入审核流程表
            kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_DEPT,
                    CnsConstValue.KnowledgeOpt.KINFO_REPORT, "知识上报");
            msg = "上报成功,请等待审核结果！";
        }   
        addCallbackParam("msg", msg);
        kinfoService.updateRecord(dataBean);

      
    }

    /**
     * 
     *  暂存
     *  @return    
     */
    public void saveTemp() {
        //设置知识信息
        if (StringUtil.isBlank(dataBean.getRowguid())) {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setIsenable(CnsConstValue.CNS_ZERO_INT.toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setOuname(userSession.getOuName());
            dataBean.setOuguid(userSession.getOuGuid());
            dataBean.setCreatdate(new Date());
            dataBean.setPublishpguid(userSession.getUserGuid());
            dataBean.setPublishperson(userSession.getDisplayName());
            dataBean.setKstatus(CnsConstValue.KinfoStatus.NEED_REPORT);
            dataBean.setAttachguid(cliengguid);
            dataBean.setIsdel(CnsConstValue.CNS_ZERO_STR);
            dataBean.setPviguid(UUID.randomUUID().toString());
            //根据类别查找编号存入实体
            if (StringUtil.isNotBlank(categoryGuid)) {
                CnsKinfoCategory category = kinfoCategoryService.getBeanByGuid(dataBean.getCategoryguid());
                dataBean.setCategorycode(category.getCategorycode());
                //设置全文检索code
                //dataBean = kinfoService.setCategoryCodeByLevl(dataBean, dataBean.getCategorycode());
            }
            kinfoService.addRecord(dataBean);

        }
        else {
            dataBean.setPublishpguid(userSession.getUserGuid());
            dataBean.setPublishperson(userSession.getDisplayName());
            kinfoService.updateRecord(dataBean);
        }
        addCallbackParam("msg", "暂存成功！");
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

    public LazyTreeModal9 getCategoryOuModal() {
        LazyTreeModal9 model = null;
        //成员单位只能看到自己的
        if (CnsUserSession.getInstance().getUserRolesName().contains(CnsConstValue.CnsRole.HANDLE_DEPT)) {
            String ouguid = userSession.getOuGuid();
            model = resourceModelService.getCategoryOuModel(ouguid);
        }
        else {
            model = resourceModelService.getCategoryAllModel();
        }
        return model;
    }

    public CnsKinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new CnsKinfo();
        }
        return dataBean;
    }

    public void setDataBean(CnsKinfo dataBean) {
        this.dataBean = dataBean;
    }
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

}
