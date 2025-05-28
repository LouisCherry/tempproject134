package com.epoint.yczwfw.auditqueue.windowled.impl;

import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.gxqzwfw.auditqueue.windowled.domain.WindowLed;

public class  WindowLedService extends AuditCommonService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public WindowLedService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(WindowLed record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(WindowLed.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(WindowLed record) {
        return baseDao.update(record);
    }
    public List<WindowLed> findList(String sql, Object... args) {
        return baseDao.findList(sql, WindowLed.class, args);
    }

    public WindowLed find(String windowno) {
        String sql="select * from audit_znsb_windowled where windowno=?1";
        return baseDao.find(sql,WindowLed.class, windowno);
    }
}
