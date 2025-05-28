package com.epoint.jn.qh.statistics.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.jn.qh.statistics.api.QhStatistics;


/**
 * 取号量统计对应的后台service
 * 
 * @author 夜雨清尘
 * @version [版本号, 2019-06-06 16:10:26]
 */
public class QhStatisticsService
{
    
    IAuditZnsbEquipment equipmentservice = ContainerFactory.getContainInfo().getComponent(IAuditZnsbEquipment.class);
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public QhStatisticsService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(QhStatistics record) {
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
        T t = baseDao.find(QhStatistics.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(QhStatistics record) {
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
    public QhStatistics find(Object primaryKey) {
        return baseDao.find(QhStatistics.class, primaryKey);
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
    public QhStatistics find(String sql,  Object... args) {
        return baseDao.find(sql, QhStatistics.class, args);
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
    public List<QhStatistics> findList(String sql, Object... args) {
        return baseDao.findList(sql, QhStatistics.class, args);
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
    public List<QhStatistics> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, QhStatistics.class, args);
    }
    
    public void setCount(String macaddress,String centerguid) {
        String sql="select rowguid,count from qh_statistics where macaddress=?1 and centerguid=?2 and to_days(createdate) = to_days(now())";
        QhStatistics qhStatistics=baseDao.find(sql, QhStatistics.class,macaddress,centerguid);        
        String machinename="";
        String machineno="";
        AuditZnsbEquipment equipment=equipmentservice.getDetailbyMacaddress(macaddress).getResult();        
        if(equipment!=null){
            machinename=equipment.getMachinename();
            machineno=equipment.getMachineno();
        }
        if(qhStatistics!=null){
            //更新
            qhStatistics.setCount(qhStatistics.getCount()+1);
            baseDao.update(qhStatistics);
        }else{
            //新增
            QhStatistics dataBean=new QhStatistics();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setMachinename(machinename);
            dataBean.setMachineno(machineno);
            dataBean.setMacaddress(macaddress);
            dataBean.set("centerguid", centerguid);
            dataBean.setCount(1);
            dataBean.setCreatedate(new Date());
            baseDao.insert(dataBean);
        }
    }

}
