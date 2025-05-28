package com.epoint.knowledge.kinforead.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.domain.CnsKinfoCollect;
import com.epoint.knowledge.kinforead.inter.ICnsKinfoCollect;

/**
 * 
 *  知识收藏持久层
 * @作者 ASUS
 * @version [版本号, 2017年3月14日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CnsKinfoCollectImpl extends CnsCommonImpl<CnsKinfoCollect>implements ICnsKinfoCollect
{

    private ICommonDao commonDao;

    public CnsKinfoCollectImpl() {
        commonDao = CommonDao.getInstance();
    }

    @Override
    public void deleteRecordByKguidAndUserguid(String sel, String userGuid) {
        commonDao.execute("delete from CNS_KINFO_COLLECT where KGUID='"+sel+"' and USERGUID='"+userGuid+"'");
    }

    @Override
    public CnsKinfoCollect findRecordByKguidAndUserGuid(String kguid, String userGuid) {
        return commonDao.find("select * from cns_kinfo_collect where kguid='"+kguid+"' "
                + "and userguid='"+userGuid+"'", CnsKinfoCollect.class);
    }

}
