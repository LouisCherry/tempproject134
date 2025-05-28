package com.epoint.knowledge.oumanage.service;


import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.common.CnsCommonService;
import com.epoint.knowledge.common.ICnsCommon;
import com.epoint.knowledge.common.domain.CnsKinfoAbandon;
import com.epoint.knowledge.oumanage.impl.CnsKinfoAbandonImpl;
import com.epoint.knowledge.oumanage.inter.ICnsKinfoAbandon;



public class CnsKinfoAbandonService extends CnsCommonService<CnsKinfoAbandon>
{
    private ICnsKinfoAbandon cnsKinfoAbandonImpl=null;
    
    public CnsKinfoAbandonService() {
        cnsKinfoAbandonImpl=new CnsKinfoAbandonImpl();
    }
    
    public CnsKinfoAbandonService(ICommonDao commonDao) {
        cnsKinfoAbandonImpl=new CnsKinfoAbandonImpl(commonDao);
    }
    
    @Override
    protected ICnsCommon<CnsKinfoAbandon> getICnsCommon() {
        // TODO Auto-generated method stub
        return cnsKinfoAbandonImpl;
    }
    
    /**
     * 
     *  查询过期的知识类别
     *  @param cnsKinfo
     *  @param categoryCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
   // public List<cnskinfo>

}
