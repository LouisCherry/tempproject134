package com.epoint.knowledge.kinforead.impl;

import java.util.List;

import com.epoint.core.dao.ICommonDao;
import com.epoint.knowledge.common.CnsCommonImpl;
import com.epoint.knowledge.common.domain.CnsKinfoEvl;
import com.epoint.knowledge.kinforead.inter.ICnsKinfoEvl;



public class CnsKinfoEvlImpl extends CnsCommonImpl<CnsKinfoEvl> implements ICnsKinfoEvl
{

    public CnsKinfoEvlImpl() {
        super();
    }

    public CnsKinfoEvlImpl(ICommonDao commonDao) {
        super(commonDao);
    }

    @Override
    public List<String> getGuidByRemarkCount() {
        String sql = "select kguid from cns_kinfo_evl where evlresult<3 group by kguid having count(*)>2";
        return commonDao.findList(sql, String.class);
    }
}
