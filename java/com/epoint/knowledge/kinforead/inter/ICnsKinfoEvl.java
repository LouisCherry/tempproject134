package com.epoint.knowledge.kinforead.inter;

import java.util.List;

import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoEvl;


public interface ICnsKinfoEvl extends ICnsCommon<CnsKinfoEvl>
{
    /**
     * 
     *  查询差评满3次的知识kguid
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getGuidByRemarkCount();
}
