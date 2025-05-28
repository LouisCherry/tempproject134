package com.epoint.knowledge.audit.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.bizlogic.orga.user.service.FrameUserService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.impl.MessagesCenterService9;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.knowledge.category.service.CnsKinfoCategoryService;
import com.epoint.knowledge.common.CnsConstValue;
import com.epoint.knowledge.common.CnsUserSession;

import com.epoint.knowledge.common.MessageSendUtil;
import com.epoint.knowledge.common.ResourceModelService;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoCategory;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.kinforead.service.CnsKinfoCollectService;
import com.epoint.knowledge.oumanage.service.CnsKinfoService;
import com.epoint.knowledge.oumanage.service.CnsKinfoStepService;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 知识信息表list页面对应的后台
 * 
 * @author xuyunhai
 * @version [版本号, 2017-02-13 10:28:44]
 */
@RestController("kinfoauditlistaction")
@Scope("request")
public class KinfoAuditListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CnsKinfoStepService kinfoStepService = new CnsKinfoStepService();
    private CnsKinfoCollectService collectService = new CnsKinfoCollectService();

    private FrameUserService9 frameUserService = new FrameUserService9();
    private MessagesCenterService9 messageservice = new MessagesCenterService9();
    private CnsKinfoService cnsKinfoService = new CnsKinfoService();
    

    private ResourceModelService resourceModelService = new ResourceModelService();
    /**
     * 知识库类型service
     */
    private CnsKinfoCategoryService kinfoCategoryService = new CnsKinfoCategoryService();

    /**
     * 知识信息表实体对象
     */
    private CnsKinfo cnsKinfo;

    /**
     * 表格控件model
     */
    private DataGridModel<CnsKinfo> model;

    /**
     * 类别标识
     */
    private String categoryguid;

    @Override
    public void pageLoad() {
        if (cnsKinfo == null) {
            cnsKinfo = new CnsKinfo();
        }
    }

    public DataGridModel<CnsKinfo> getDataGridData() {
        // 获得表格对象
        Map<String, String> conditionMap = new HashMap<>();
        if (model == null) {
            model = new DataGridModel<CnsKinfo>()
            {
                @Override
                public List<CnsKinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(categoryguid)) {
                        //获取该类别下面所有类别，作为条件
                        List<CnsKinfoCategory> childCategory = kinfoCategoryService.getChildCategoryList(categoryguid);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";
                            String ouname = UserSession.getInstance().getOuName();
                            String ouguid = UserSession.getInstance().getOuGuid();
                            
                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
       
                            }
                            
                            inStr = inStr.substring(0, inStr.length() - 1);
                            if(StringUtil.isBlank(inStr.trim())){
                                List<CnsKinfo> list = new ArrayList<CnsKinfo>();
                                return list;
                                        
                            }else{
                                conditionMap.put("CATEGORYGUIDIN", inStr);
                            }
                            
                        }
                    }else{
                        String sql = "select * from cns_kinfo_category";
                        List<CnsKinfoCategory> childCategory = CommonDao.getInstance().findList(sql, CnsKinfoCategory.class, null);
                        if (childCategory != null && childCategory.size() > 0) {
                            String inStr = " ";

                            for (CnsKinfoCategory cnsKinfoCategory : childCategory) {
                                inStr += "'" + cnsKinfoCategory.getRowguid() + "',";
                            }
                            
                            inStr = inStr.substring(0, inStr.length() - 1);
                            if(StringUtil.isBlank(inStr.trim())){
                                List<CnsKinfo> list = new ArrayList<CnsKinfo>();
                                return list;
                                        
                            }else{
                                conditionMap.put("CATEGORYGUIDIN", inStr);
                            }
                            
                        }
                    }
                    //查看该数据是否被删除
                    conditionMap.put("ISDEL=", CnsConstValue.CNS_ZERO_STR);
                    conditionMap.put("KSTATUS=", CnsConstValue.KinfoStatus.IN_AUDIT);
                    if (StringUtil.isNotBlank(cnsKinfo.getKname())) {
                        conditionMap.put("KNAMELIKE", cnsKinfo.getKname());
                    }
                    if (StringUtil.isBlank(sortField)) {
                        sortField = "CREATDATE";
                    }
                    if (StringUtil.isBlank(sortOrder)) {
                        sortOrder = "DESC";
                    }
                    List<CnsKinfo> list = cnsKinfoService.getRecordPage("*", conditionMap, first, pageSize, sortField,
                            sortOrder);
                    int count = cnsKinfoService.getRecordCount(conditionMap);
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public LazyTreeModal9 getCategoryModal() {
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

    public CnsKinfo getCnsKinfo() {
        return cnsKinfo;
    }

    public void setCnsKinfo(CnsKinfo cnsKinfo) {
        this.cnsKinfo = cnsKinfo;
    }

    public String getCategoryguid() {
        return categoryguid;
    }

    public void setCategoryguid(String categoryguid) {
        this.categoryguid = categoryguid;
    }

    public void auditAll() {
        List<String> keys = this.getDataGridData().getSelectKeys();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                CnsKinfo kinfo = cnsKinfoService.getBeanByGuid(key);
                kinfo.setKstatus(CnsConstValue.KinfoStatus.PASS_AUDIT);
                kinfo.setIsenable(CnsConstValue.CNS_ONT_INT.toString());
                cnsKinfoService.updateRecord(kinfo);
                MessageSendUtil.closeWaitHandlemsgbyguid(key);
                //---结束待办----
                
               // MessagesCenter msg = null;//messageservice.selectExamMessgeGuid(userSession.getUserGuid(), kinfo.getRowguid());
                //---发起通过待办-----
                MessageSendUtil.sendWaitHandleMessage("【知识审核通过】" + kinfo.getKname(), "",
                        "",
                        "bmfw/resources/knowledge/kinfomanage/cnskinfodetail?guid=" + kinfo.getRowguid(),
                        kinfo.getRowguid(), "", "", "知识审核通过");
                //存入审核流程表
                //          kinfoStepService.addrecordbykguid(kinfo.getRowguid(), CnsConstValue.KnowledgeOpt.AUDIT_PASS, null);
                //父类guid不为空，说明为新增的修改数据
                if (StringUtil.isNotBlank(kinfo.getEditguid())) {
                    //更新父类操作
                    CnsKinfo cnsk = cnsKinfoService.getBeanByGuid(kinfo.getEditguid());
                    cnsk.setIsdel(CnsConstValue.CNS_ONT_STR);
                    cnsKinfoService.updateRecord(cnsk);
                    List<CnsKinfoCollect> collects = collectService.getListByOneField("KGUID", kinfo.getEditguid());
                    if (collects != null && collects.size() > 0) {
                        for (CnsKinfoCollect collect : collects) {
                            FrameUser frameUser = frameUserService.getUserByUserField("USERGUID",
                                    collect.getUserguid());
                            //发起变更通知
                            MessageSendUtil.sendWaitHandleMessage("【知识变更通知】" + kinfo.getKname(),
                                    frameUser.getUserGuid(), frameUser.getDisplayName(),
                                    "bmfw/resources/knowledge/kinfo/cnskinfodetail?guid=" + kinfo.getRowguid(),
                                    kinfo.getRowguid(), "", "", "知识变更");
                            //将新的kguid设置给该收藏数据
                            collect.setKguid(kinfo.getRowguid());
                            collectService.updateRecord(collect);
                        }
                    }
                    //---结束掉原知识待办---
                    MessageSendUtil.closeWaitHandlemsgbyguid(kinfo.getEditguid());
                    //删除这条原来数据的待办步骤
                    kinfoStepService.deleteRecords("KGUID", kinfo.getEditguid());
                    //将edit清空
                    kinfo.setEditguid(null);
                    cnsKinfoService.updateRecord(kinfo);

                }
                addCallbackParam("msg", "审核通过！");
            }
        }
    }
    

    
}
