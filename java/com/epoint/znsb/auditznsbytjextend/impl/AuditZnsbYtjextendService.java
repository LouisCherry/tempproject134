package com.epoint.znsb.auditznsbytjextend.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.znsb.auditznsbytjextend.api.entity.AuditZnsbYtjextend;

/**
 * 一体机模块额外配置对应的后台service
 * 
 * @author Administrator
 * @version [版本号, 2021-04-20 10:11:49]
 */
public class AuditZnsbYtjextendService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditZnsbYtjextendService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbYtjextend record) {
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
        T t = baseDao.find(AuditZnsbYtjextend.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbYtjextend record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditZnsbYtjextend find(Object primaryKey) {
        return baseDao.find(AuditZnsbYtjextend.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditZnsbYtjextend find(String sql,  Object... args) {
        return baseDao.find(sql, AuditZnsbYtjextend.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditZnsbYtjextend> findList(String sql, Object... args) {
        if("moduleguid".equals(sql)){
            sql = "select * from Audit_Znsb_Ytjextend where moduleguid = ?";
        }
        return baseDao.findList(sql, AuditZnsbYtjextend.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditZnsbYtjextend> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbYtjextend.class, args);
    }

	/**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countAuditZnsbYtjextend(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public List<AuditZnsbSelfmachinemodule> getModuleListByLabel(String labelguid, boolean isflag) {

        String sql = "select * from Audit_Znsb_SelfmachineModule module,Audit_Znsb_SelfmachineModule";
        return baseDao.findList(sql, AuditZnsbSelfmachinemodule.class, labelguid);
    }
}
