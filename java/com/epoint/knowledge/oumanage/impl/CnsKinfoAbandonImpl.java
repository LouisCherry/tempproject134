package com.epoint.knowledge.oumanage.impl;


import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.domain.CnsKinfoAbandon;
import com.epoint.knowledge.oumanage.inter.ICnsKinfoAbandon;

/**
 * 
 *  知识库信息实现接口类
 * @作者 Administrator
 * @version [版本号, 2017年2月13日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoAbandonImpl extends CnsCommonImpl<CnsKinfoAbandon>implements ICnsKinfoAbandon
{
    public CnsKinfoAbandonImpl() {
        super();
    }
    
    public CnsKinfoAbandonImpl(ICommonDao dao) {
        super(dao);
    }
}
