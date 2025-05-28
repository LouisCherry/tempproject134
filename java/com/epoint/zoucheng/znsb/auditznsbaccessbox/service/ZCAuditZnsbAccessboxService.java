package com.epoint.zoucheng.znsb.auditznsbaccessbox.service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.auditqueue.auditznsbaccessbox.domain.AuditZnsbAccessbox;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

/**
 * 智能化存取盒表对应的后台service
 * 
 * @author 54201
 * @version [版本号, 2019-02-20 14:45:08]
 */
public class ZCAuditZnsbAccessboxService  extends AuditCommonService
{
 /**
     * 
     */
    private static final long serialVersionUID = 1L;
/**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZCAuditZnsbAccessboxService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            
     * @return int
     */
    public int insert(AuditZnsbAccessbox record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditZnsbAccessbox.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            
     * @return int
     */
    public int update(AuditZnsbAccessbox record) {
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
    public AuditZnsbAccessbox find(Object primaryKey) {
        return baseDao.find(AuditZnsbAccessbox.class, primaryKey);
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
    public AuditZnsbAccessbox find(String sql,  Object... args) {
        return baseDao.find(sql, AuditZnsbAccessbox.class, args);
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
    public List<AuditZnsbAccessbox> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditZnsbAccessbox.class, args);
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
    public List<AuditZnsbAccessbox> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbAccessbox.class, args);
    }
    
    /**
     * 根据centerguid删除
     */
    public void deleteRecords(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接删除语句进行处理
            String sql = "delete from " + en.table() + " where " + key + "=?1";
            commonDao.execute(sql, keyValue);
        }
    }
    
    public void initBox(String boxno ,String abscissa,String ordinate,String boxstatus,String cabinetguid) {
        AuditZnsbAccessbox box =new AuditZnsbAccessbox();
        box.setRowguid(UUID.randomUUID().toString());
        box.setOperatedate(new Date());     
        box.setBoxno(boxno);
        box.setAbscissa(abscissa);
        box.setOrdinate(ordinate);
        box.setBoxstatus(boxstatus);
        box.setCabinetguid(cabinetguid);
        insert(box);
    }

}
