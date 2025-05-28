package com.epoint.knowledge.oumanage.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.domain.CnsKinfoStep;
import com.epoint.knowledge.oumanage.inter.ICnsKinfoStep;


/**
 * 
 *  知识库审核步骤实现接口类
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoStepImpl extends CnsCommonImpl<CnsKinfoStep>implements ICnsKinfoStep
{
    private ICommonDao commonDao;

    public CnsKinfoStepImpl() {
        commonDao = CommonDao.getInstance();
    }

    @Override
    public CnsKinfoStep getLastStep(String cguid) {
        CnsKinfoStep cnsHandleStep = null;
        List<CnsKinfoStep> handleStepList = null;
        String sql = "select * from CNS_KINFO_STEP where KGUID=?1 order by CREATETIME desc";
        handleStepList = commonDao.findList(sql, CnsKinfoStep.class, cguid);
        if (handleStepList != null && handleStepList.size() > 0) {
            cnsHandleStep = handleStepList.get(0);
        }
        return cnsHandleStep;
    }

    @Override
    public List<CnsKinfoStep> getAllStepByKguid(String kguid) {
        String sql = "select * from CNS_KINFO_STEP where  kguid=?1  order by HANDLETIME";
        List<CnsKinfoStep> list = commonDao.findList(sql, CnsKinfoStep.class, kguid);
        return list;
    }
}
