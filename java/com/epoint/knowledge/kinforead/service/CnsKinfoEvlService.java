package com.epoint.knowledge.kinforead.service;

import java.util.List;

import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoEvl;
import com.epoint.knowledge.kinforead.impl.CnsKinfoEvlImpl;



public class CnsKinfoEvlService extends CnsCommonService<CnsKinfoEvl>
{
    private CnsKinfoEvlImpl kinfoEvlImpl=new CnsKinfoEvlImpl();
    @Override
    protected ICnsCommon<CnsKinfoEvl> getICnsCommon() {
        return new CnsKinfoEvlImpl();
    }
    
    /**
     * 
     *  查询差评满3次的知识kguid
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getGuidByRemarkCount()
    {
        return kinfoEvlImpl.getGuidByRemarkCount();
    }

}
