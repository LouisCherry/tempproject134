package com.epoint.xmz.kyinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.kyinfo.api.entity.KyInfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.kyinfo.api.IKyInfoService;
/**
 * 勘验信息表对应的后台service实现类
 * 
 * @author RaoShaoliang
 * @version [版本号, 2023-07-10 16:17:19]
 */
@Component
@Service
public class KyInfoServiceImpl implements IKyInfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(KyInfo record) {
        return new KyInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new KyInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(KyInfo record) {
        return new KyInfoService().update(record);
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
    public KyInfo find(Object primaryKey) {
       return new KyInfoService().find(primaryKey);
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
    public KyInfo find(String sql, Object... args) {
        return new KyInfoService().find(sql,args);
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
    public List<KyInfo> findList(String sql, Object... args) {
       return new KyInfoService().findList(sql,args);
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
    public List<KyInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new KyInfoService().findList(sql,pageNumber,pageSize,args);
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
     @Override
    public Integer countKyInfo(String sql, Object... args){
        return new KyInfoService().countKyInfo(sql, args);
    }

    @Override
    public KyInfo findByProjectguid(String projectguid) {
        return new KyInfoService().findByProjectguid(projectguid);
    }

    @Override
    public List<KyInfo> getNeedInfo() {
        return new KyInfoService().getNeedInfo();
    }

}
