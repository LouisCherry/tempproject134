package com.epoint.auditsp.auditsphandle.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.DbKit;
import com.epoint.database.jdbc.Parameter;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

import java.util.List;

public class JNQiYeMaService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JNQiYeMaService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 根据guid分页查询
     *
     * @param attachguids
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<FrameAttachInfo> findListByGuids(String attachguids, int first, int pageSize, String sortField,
                                                 String sortOrder) {
        String sql = "select * from frame_attachinfo where ATTACHGUID in ";
        Parameter pa = DbKit.splitIn(attachguids);
        sql += pa.getSql();
        sql += " order by " + DbKit.checkOrderField(sql, sortField, FrameAttachInfo.class);
        sql += DbKit.checkOrderDirect(sql, sortOrder);
        return baseDao.findList(sql, first, pageSize, FrameAttachInfo.class, pa.getParamValue().toArray());
    }
}
