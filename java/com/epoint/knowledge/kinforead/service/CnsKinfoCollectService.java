package com.epoint.knowledge.kinforead.service;

import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.kinforead.impl.CnsKinfoCollectImpl;
import com.epoint.knowledge.kinforead.inter.ICnsKinfoCollect;

/**
 * 
 * 知识收藏逻辑层
 * @作者 ASUS
 * @version [版本号, 2017年3月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoCollectService  extends CnsCommonService<CnsKinfoCollect>
{
    private ICnsKinfoCollect kinfoCollectImpl=new CnsKinfoCollectImpl();
    @Override
    protected ICnsCommon<CnsKinfoCollect> getICnsCommon() {
        return new CnsKinfoCollectImpl();
    }
    /**
     * 
     *  按照知识guid和userguid删除知识收藏
     *  @param sel
     *  @param userGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteRecordByKguidAndUserguid(String sel, String userGuid) {
        kinfoCollectImpl.deleteRecordByKguidAndUserguid(sel,userGuid);
        
    }
    /**
     * 
     *   按照知识guid和userguid查找知识收藏
     *  @param rowguid
     *  @param userGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public CnsKinfoCollect findRecordByKguidAndUserGuid(String kguid, String userGuid) {
        return kinfoCollectImpl.findRecordByKguidAndUserGuid(kguid,userGuid);
    }
  
}
