package com.epoint.knowledge.kinforead.inter;

import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;

/**
 * 
 * 知识收藏接口
 * @version [版本号, 2017年3月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ICnsKinfoCollect extends ICnsCommon<CnsKinfoCollect>
{
    /**
     *  按照Kguid和Userguid删除
     *  @param sel
     *  @param userGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteRecordByKguidAndUserguid(String sel, String userGuid);
    /**
     * 
     * 
     *  按照Kguid和Userguid查询
     *  @param kguid
     *  @param userGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CnsKinfoCollect findRecordByKguidAndUserGuid(String kguid, String userGuid);
    
}
