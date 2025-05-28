package com.epoint.knowledge.oumanage.inter;

import java.util.List;

import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoStep;




/**
 * 
 *  [知识库审核步骤接口]
 * @作者 xuyunhai
 * @version [版本号, 2017年2月15日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ICnsKinfoStep extends ICnsCommon<CnsKinfoStep>
{
    public CnsKinfoStep getLastStep(String cguid);
    
    public List<CnsKinfoStep> getAllStepByKguid(String kguid);
}
