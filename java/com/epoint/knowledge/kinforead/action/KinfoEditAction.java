package com.epoint.knowledge.kinforead.action;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

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
import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;

/**
 * 知识信息表修改页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-14 09:03:24]
 */
@RestController("kinfoeditaction")
@Scope("request")
public class KinfoEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CnsKinfoService kinfoService = new CnsKinfoService();

    private ResourceModelService commonModelService = new ResourceModelService();

    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();

    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    private CnsUserService cnsUserService = new CnsUserService();

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo dataBean = null;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfoStep> model;

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
     * 上报被退回后再次上报
     * 
     */
    public void saveReport() {
        //根据类别查找编号存入实体
        if (StringUtil.isNotBlank(dataBean.getCategoryguid())) {
            CnsKinfoCategory category = kinfoCategoryService.getBeanByGuid(dataBean.getCategoryguid());
            dataBean.setCategorycode(category.getCategorycode());
            dataBean.setCategoryname(category.getCategoryname());
            dataBean = kinfoService.setCategoryCodeByLevl(dataBean, category.getCategorycode());
        }
        dataBean.setKstatus(CnsConstValue.KinfoStatus.IN_AUDIT);
        kinfoService.updateRecord(dataBean);
        //---若为退回先结束掉当前步骤待办---
        MessageSendUtil.closeWaitHandlemsgbyguid(dataBean.getPviguid());
        // ------发待办------
        String pviguid = dataBean.getPviguid();
        if(StringUtil.isBlank(pviguid)){
            pviguid=UUID.randomUUID().toString();
                    
        }
        List<FrameUser> frameUsers = cnsUserService.getUserByRoleNameAndCategoryguid(CnsConstValue.CnsRole.AUDIT_KNOWLEDGE, dataBean.getCategoryguid());
        for (FrameUser frameUser : frameUsers) {
            MessageSendUtil.sendWaitHandleMessage("【知识库审核】" + dataBean.getKname(), frameUser.getUserGuid(),
                    frameUser.getDisplayName(),
                    CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoaudit/kinfoaudit?guid=" + dataBean.getRowguid(),
                    dataBean.getRowguid(), pviguid, "", "知识上报审核");
        }
        //存入审核流程表
        kinfoStepService.addKinfoAuditStep(dataBean, CnsConstValue.KnowledgeActivity.ACTIVITY_DEPT,
                CnsConstValue.KnowledgeOpt.REPORT_AGAIN, "重新上报");
        addCallbackParam("msg", "上报成功,请等待审核结果！");

    }

    /**
     * 
     *保存操作
     * 
     */
    public void save() {
        //根据类别查找编号存入实体
        if (StringUtil.isNotBlank(dataBean.getCategoryguid())) {
            CnsKinfoCategory parentCat = kinfoCategoryService.getBeanByGuid(dataBean.getCategoryguid());

            dataBean.setCategorycode(parentCat.getCategorycode());
            dataBean.setCategoryname(parentCat.getCategoryname());
            dataBean = kinfoService.setCategoryCodeByLevl(dataBean, parentCat.getCategorycode());
        }
        kinfoService.updateRecord(dataBean);
        addCallbackParam("msg", "保存修改成功");
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
        return commonModelService.getCategoryOuModel(userSession.getOuGuid());
    }

    public CnsKinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(CnsKinfo dataBean) {
        this.dataBean = dataBean;
    }

}
