package com.epoint.xmz.certhwslysjyxk.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.certcbyyysz.api.entity.CertCbyyysz;
import com.epoint.xmz.certhwslysjyxk.api.ICertHwslysjyxkService;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;

/**
 * 省际普通货物水路运输经营许可本地库对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@Component
@Service
public class CertHwslysjyxkServiceImpl implements ICertHwslysjyxkService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CertHwslysjyxk record) {
        return new CertHwslysjyxkService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new CertHwslysjyxkService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CertHwslysjyxk record) {
        return new CertHwslysjyxkService().update(record);
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
    public CertHwslysjyxk find(Object primaryKey) {
        return new CertHwslysjyxkService().find(primaryKey);
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
    public CertHwslysjyxk find(String sql, Object... args) {
        return new CertHwslysjyxkService().find(sql, args);
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
    public List<CertHwslysjyxk> findList(String sql, Object... args) {
        return new CertHwslysjyxkService().findList(sql, args);
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
    public List<CertHwslysjyxk> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new CertHwslysjyxkService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countCertHwslysjyxk(String sql, Object... args) {
        return new CertHwslysjyxkService().countCertHwslysjyxk(sql, args);
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
    public CertHwslysjyxk getCertByCertno(String certno) {
        return new CertHwslysjyxkService().getCertByCertno(certno);
    }

    public CertCbyyysz getYyysCertByCertno(String certno) {
        return new CertHwslysjyxkService().getYyysCertByCertno(certno);
    }

    public CertCbyyysz getYyysZxCertByCertno(String yyzbh) {
        return new CertHwslysjyxkService().getYyysZxCertByCertno(yyzbh);
    }

    public CertHwslysjyxk getCertByCertno(String certno, String jyzmc) {
        return new CertHwslysjyxkService().getCertByCertno(certno, jyzmc);
    }

    @Override
    public CertHwslysjyxk getGhslysCertByCertJyxkzbh(String jyxkzbh) {

        return new CertHwslysjyxkService().getGhslysCertByCertJyxkzbh(jyxkzbh);
    }

}
