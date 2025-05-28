package com.epoint.znsb.znsbclientarea.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.znsb.znsbclientarea.domain.ZnsbClientArea;

public class ZnsbClientAreaService
{
    protected ICommonDao baseDao = CommonDao.getInstance();

    public int insert(ZnsbClientArea record) {
        return this.baseDao.insert(record);
    }

    public <T extends Record> int deleteByGuid(String guid) {
        Record t = (Record) this.baseDao.find(ZnsbClientArea.class, guid);
        return this.baseDao.delete(t);
    }

    public int deleteByAreacode(String areacode) {
        String sql = "delete from Audit_Znsb_Client_Area where areacode=?";
        return this.baseDao.execute(sql, new Object[] {areacode });
    }

    public int update(ZnsbClientArea record) {
        return this.baseDao.update(record);
    }

    public ZnsbClientArea find(Object primaryKey) {
        return (ZnsbClientArea) this.baseDao.find(ZnsbClientArea.class, primaryKey);
    }

    public int batchupdateClient(String filename, String version, String remark, String attachguid, String clientguid) {
        String sql = "update audit_znsb_client_area set clientguid=?,version=?,remark=?,attachguid=? where clientfilename=?";
        return this.baseDao.execute(sql, new Object[] {clientguid, version, remark, attachguid, filename });
    }
}
