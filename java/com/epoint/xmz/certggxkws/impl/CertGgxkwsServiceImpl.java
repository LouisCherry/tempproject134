package com.epoint.xmz.certggxkws.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.certggxkws.api.entity.CertGgxkws;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.certggxkws.api.ICertGgxkwsService;
/**
 * 公共许可卫生证照库对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-04-12 17:01:05]
 */
@Component
@Service
public class CertGgxkwsServiceImpl implements ICertGgxkwsService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CertGgxkws record) {
        return new CertGgxkwsService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new CertGgxkwsService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CertGgxkws record) {
        return new CertGgxkwsService().update(record);
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
    public CertGgxkws find(Object primaryKey) {
       return new CertGgxkwsService().find(primaryKey);
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
    public CertGgxkws find(String sql, Object... args) {
        return new CertGgxkwsService().find(sql,args);
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
    public List<CertGgxkws> findList(String sql, Object... args) {
       return new CertGgxkwsService().findList(sql,args);
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
    public List<CertGgxkws> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new CertGgxkwsService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countCertGgxkws(String sql, Object... args){
        return new CertGgxkwsService().countCertGgxkws(sql, args);
    }
     
     /**
      * 根据CERTNO查找单个实体
      * 
      * @param clazz
      *            类<必须继承BaseEntity>
      * @param primaryKey
      *            主键
      * @return T extends BaseEntity
      */
     public CertGgxkws getCertByCertno(String certno) {
        return new CertGgxkwsService().getCertByCertno(certno);
     }

}
