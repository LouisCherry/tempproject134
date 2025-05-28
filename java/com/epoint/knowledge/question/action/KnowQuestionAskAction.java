package com.epoint.knowledge.question.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.tree.SimpleFetchHandler9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.audit.service.CnsKinfoQuestionService;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserService;
import com.epoint.knowledge.common.CnsWorkflowValue;

import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.workflow.bizlogic.api.WFAPI9;
import com.epoint.workflow.bizlogic.service.config.WorkflowActivityService9;
import com.epoint.workflow.bizlogic.service.config.WorkflowProcessVersionService9;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 知识库提问表新增页面对应的后台
 * 
 * @author wxlin
 * @version [版本号, 2017-06-01 13:43:10]
 */
@RestController("knowquestionaskaction")
@Scope("request")
public class KnowQuestionAskAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8720193913540299219L;

    private CnsKinfoQuestionService kinfoQuestionService = new CnsKinfoQuestionService();
    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();
    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    private WorkflowActivityService9 activityService = new WorkflowActivityService9();

   
    private CnsUserService cnsUserService = new CnsUserService();
    /**
     * 知识库提问表实体对象
     */
    private CnsKinfoQuestion cnsKinfoQuestion;

    private CnsKinfoCategory cnsKinfoCategory;
    private FrameConfigService9 frameConfigService = new FrameConfigService9();
    /**
     * 知识库类型树
     */
    private LazyTreeModal9 kinfoCategoryTree;

    String categoryguid = "";

    @Override
    public void pageLoad() {
        if (cnsKinfoQuestion == null) {
            cnsKinfoQuestion = new CnsKinfoQuestion();
        }
        categoryguid = this.getRequestParameter("categoryguid");
        if (StringUtil.isNotBlank(categoryguid)) {
            cnsKinfoCategory = kinfoCategoryService.getBeanByGuid(categoryguid);
            if(cnsKinfoCategory!=null)
            {
                this.addCallbackParam("isleaf", cnsKinfoCategory.getIsleaf());
                this.addCallbackParam("categoryname", categoryguid);
                this.addCallbackParam("categoryname", cnsKinfoCategory.getCategoryname());
                this.addCallbackParam("ouname", cnsKinfoCategory.getOuname());
                this.addCallbackParam("ouguid", cnsKinfoCategory.getOuguid());
            }

        }

    }

    /**
     * 提问并关闭
     * 
     */
    public void add() {
        List<FrameUser> frameUsers = this.cnsUserService.getUserListByOu(userSession.getOuGuid(), "成员单位", 1);
        if(frameUsers==null || frameUsers.size()==0 ){
            addCallbackParam("error", "请联系管理员添加相关处理人！");
            return;
        }
        cnsKinfoQuestion.setRowguid(UUID.randomUUID().toString());
        cnsKinfoQuestion.setOperatedate(new Date());
        cnsKinfoQuestion.setOperateusername(userSession.getDisplayName());
        cnsKinfoQuestion.setKinfocategoryguid(categoryguid);
        cnsKinfoQuestion.setKinfocategoryname(cnsKinfoCategory.getCategoryname());
        
        cnsKinfoQuestion.setAsktime(new Date());
        cnsKinfoQuestion.setAskpersonguid(userSession.getUserGuid());
        cnsKinfoQuestion.setAskpersonname(userSession.getDisplayName());
        cnsKinfoQuestion.setAnswerstatus(CnsConstValue.CNS_Answer_Status.ANSWER_WAIT);
        cnsKinfoQuestion.setKstatus(CnsConstValue.KinfoStatus.NO_AUDIT);
        cnsKinfoQuestion.setPviguid(UUID.randomUUID().toString());
        kinfoQuestionService.addRecord(cnsKinfoQuestion);
        this.addCallbackParam("msg", "提问成功！");
        
        
        if (frameUsers != null && frameUsers.size() > 0) {
            for (FrameUser frameUser : frameUsers) {
               
                MessageSendUtil.sendWaitHandleMessage("【知识库提问审核】" + cnsKinfoQuestion.getQuestioncontent(), frameUser.getUserGuid(),
                        frameUser.getDisplayName(),
                        CnsConstValue.GXHML+"/individuation/overall/knowledge/kinfoask/cnskinfoquestionanswer?guid=" + cnsKinfoQuestion.getRowguid(),
                        cnsKinfoQuestion.getRowguid(), cnsKinfoQuestion.getPviguid(), "", "知识库提问");
            }
        }
       
        kinfoStepService.addKinfoaskAuditStep(cnsKinfoQuestion, "知识提问审核",
                "提问上报", "类别提问");
        
        
    }

    public LazyTreeModal9 getCategoryModal() {
        if (kinfoCategoryTree == null) {
            kinfoCategoryTree = new LazyTreeModal9(new SimpleFetchHandler9()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public <T> List<T> search(String keyword) {
                    List<CnsKinfoCategory> cnsKinfoCategoryList = null;
                    if (StringUtil.isNotBlank(keyword)) {
                        cnsKinfoCategoryList = kinfoCategoryService.getListByNameOrPY(keyword, true);
                    }
                    return (List<T>) cnsKinfoCategoryList;
                }

                @SuppressWarnings({"rawtypes", "unchecked" })
                @Override
                public <T> List<T> fetchData(int arg0, TreeData treeData) {
                    List dataList = new ArrayList<>();
                    // 如果treeData的guid不为空，则说明不是第一次加载,根据父节点获取子节点
                    if (treeData != null && StringUtil.isNotBlank(treeData.getObjectGuid())) {
                        dataList = kinfoCategoryService.getListByOneField("parentguid", treeData.getObjectGuid());
                    }
                    else {
                        // 获取顶级部门，parentguid为top
                        dataList = kinfoCategoryService.getListByOneField("parentguid",categoryguid);
                    }
                    return dataList;
                }

                @SuppressWarnings("rawtypes")
                @Override
                public int fetchChildCount(TreeData treeData) {
                    List dataList = null;
                    // 如果treeData的guid不为空，则说明不是第一次加载,根据父节点获取子节点
                    if (treeData != null && StringUtil.isNotBlank(treeData.getObjectGuid())) {
                        dataList = kinfoCategoryService.getListByOneField("parentguid", treeData.getObjectGuid());
                    }
                    else {
                        // 获取顶级部门，parentguid为top
                        dataList = kinfoCategoryService.getListByOneField("parentguid", categoryguid);
                    }
                    return dataList.size();
                }

                @Override
                public List<TreeData> changeDBListToTreeDataList(List<?> dataList) {
                    List<TreeData> treeDataList = new ArrayList<TreeData>();
                    if (dataList != null) {
                        for (Object obj : dataList) {
                            if (obj instanceof CnsKinfoCategory) {
                                CnsKinfoCategory cnsKinfoCategory = (CnsKinfoCategory) obj;
                                TreeData treeData = new TreeData();
                                treeData.setObjectcode(cnsKinfoCategory.getCategorycode());
                                treeData.setObjectGuid(cnsKinfoCategory.getRowguid());
                                treeData.setTitle(cnsKinfoCategory.getCategoryname());
                                treeDataList.add(treeData);
                            }
                        }
                    }
                    return treeDataList;
                }

            });
        }
        kinfoCategoryTree.setRootName(cnsKinfoCategory.getCategoryname());
        return kinfoCategoryTree;
    }

    public void queryDept(String rowguid) {
        if (StringUtil.isNotBlank(rowguid)) {
            cnsKinfoCategory = kinfoCategoryService.getOuByRowguid(rowguid);
            if (cnsKinfoCategory != null) {
                this.addCallbackParam("ouname", cnsKinfoCategory.getOuname());
                this.addCallbackParam("ouguid", cnsKinfoCategory.getOuguid());
            }
        }
    }

    public CnsKinfoQuestion getCnsKinfoQuestion() {
        return cnsKinfoQuestion;
    }

    public void setCnsKinfoQuestion(CnsKinfoQuestion cnsKinfoQuestion) {
        this.cnsKinfoQuestion = cnsKinfoQuestion;
    }

}
