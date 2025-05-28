package com.epoint.knowledge.oumanage.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.authentication.UserSession;
import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfo;
import com.epoint.knowledge.common.domain.CnsKinfoQuestion;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.impl.CnsKinfoStepImpl;
import com.epoint.knowledge.oumanage.inter.ICnsKinfoStep;



/**
 * 
 * 知识库类别中间层
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoStepService extends CnsCommonService<CnsKinfoStep>
{
    private ICnsKinfoStep kinfoStepImpl = new CnsKinfoStepImpl();

    @Override
    protected ICnsCommon<CnsKinfoStep> getICnsCommon() {
        return new CnsKinfoStepImpl();
    }

    /**
     * 
     * 插入处理流程记录
     *  @param rowguid
     *  @param inAudit    
     * @param option 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addKinfoAuditStep(CnsKinfo cnsKinfo, String activityName, String opeartionstatus,
            String opinion) {
        CnsKinfoStep cnsKinfoStep = new CnsKinfoStep();
        cnsKinfoStep.setRowguid(UUID.randomUUID().toString());
        cnsKinfoStep.setKguid(cnsKinfo.getRowguid());
        cnsKinfoStep.setHandletime(new Date());
        cnsKinfoStep.setHandlername(UserSession.getInstance().getDisplayName());
        cnsKinfoStep.setHandlerguid(UserSession.getInstance().getUserGuid());
        cnsKinfoStep.setHandlerouguid(UserSession.getInstance().getOuGuid());
        cnsKinfoStep.setHandlerouname(UserSession.getInstance().getOuName());
        cnsKinfoStep.setCreatetime(new Date());
        cnsKinfoStep.setHandleopinion(opinion);
        cnsKinfoStep.setActivitytype(activityName);
        cnsKinfoStep.setOpeartionstatus(opeartionstatus);
        // 如果操作步骤为空，那么就是第一个插入，创建时间和操作时间一样
        CnsKinfoStep cnsLastStep = kinfoStepImpl.getLastStep(cnsKinfo.getRowguid());
        if (cnsLastStep == null) {
            cnsKinfoStep.setSendtime(new Date());
            cnsKinfoStep.setSendername(UserSession.getInstance().getDisplayName());
        }
        // 不为空，则从上一步获取创建时间
        else {
            cnsKinfoStep.setSendtime(cnsLastStep.getHandletime());
            cnsKinfoStep.setSendername(cnsLastStep.getHandlername());
        }
        kinfoStepImpl.addRecord(cnsKinfoStep);
    }
    public void addKinfoaskAuditStep(CnsKinfoQuestion cnsKinfo, String activityName, String opeartionstatus,
            String opinion) {
        CnsKinfoStep cnsKinfoStep = new CnsKinfoStep();
        cnsKinfoStep.setRowguid(UUID.randomUUID().toString());
        cnsKinfoStep.setKguid(cnsKinfo.getRowguid());
        cnsKinfoStep.setHandletime(new Date());
        cnsKinfoStep.setHandlername(UserSession.getInstance().getDisplayName());
        cnsKinfoStep.setHandlerguid(UserSession.getInstance().getUserGuid());
        cnsKinfoStep.setHandlerouguid(UserSession.getInstance().getOuGuid());
        cnsKinfoStep.setHandlerouname(UserSession.getInstance().getOuName());
        cnsKinfoStep.setCreatetime(new Date());
        cnsKinfoStep.setHandleopinion(opinion);
        cnsKinfoStep.setActivitytype(activityName);
        cnsKinfoStep.setOpeartionstatus(opeartionstatus);
        // 如果操作步骤为空，那么就是第一个插入，创建时间和操作时间一样
        CnsKinfoStep cnsLastStep = kinfoStepImpl.getLastStep(cnsKinfo.getRowguid());
        if (cnsLastStep == null) {
            cnsKinfoStep.setSendtime(new Date());
            cnsKinfoStep.setSendername(UserSession.getInstance().getDisplayName());
        }
        // 不为空，则从上一步获取创建时间
        else {
            cnsKinfoStep.setSendtime(cnsLastStep.getHandletime());
            cnsKinfoStep.setSendername(cnsLastStep.getHandlername());
        }
        kinfoStepImpl.addRecord(cnsKinfoStep);
    }

    /**
     * 
     *  [获取到所有审核流程]
     *  [根据知识主键kguid获取到所有审核流程]
     *  @param kguid  知识主键
     *  @return List<CnsKinfoStep>   
     */
    public List<CnsKinfoStep> getAllStepByKguid(String kguid) {
        return kinfoStepImpl.getAllStepByKguid(kguid);
    }

}
