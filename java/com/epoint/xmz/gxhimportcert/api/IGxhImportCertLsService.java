package com.epoint.xmz.gxhimportcert.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCertLs;

/**
 * 个性化证照导入表对应的后台service接口
 * 
 * @author dyxin
 * @version [版本号, 2023-06-12 16:30:17]
 */
public interface IGxhImportCertLsService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(GxhImportCertLs record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(GxhImportCertLs record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public GxhImportCertLs find(Object primaryKey);

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
    public GxhImportCertLs find(String sql, Object... args);

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
    public List<GxhImportCertLs> findList(String sql, Object... args);

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
    public List<GxhImportCertLs> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer getListCount(Map<String, String> map);

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
    public PageData<GxhImportCertLs> findPageData(Map<String, String> map, int first, int pageSize, String sortField, String sortOrder);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countGxhImportCertLs(String sql, Object... args);

    /**
     * 
     * 查找一个list
     * 
     * @param map
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<GxhImportCertLs> findListByCondition(Map<String, String> map);

    public GxhImportCertLs getCertByCondition(Map<String, String> map);

    public List<Record> getSyncCertList(String certType);

    // 获取梁山市个人身份信息
    public List<Record> getLsPersonIdNumberList(int offset, int pageSize);

    // 获取梁山市法人身份信息
    public List<Record> getLsCompanyIdNumberList(int offset, int pageSize);

    public GxhImportCertLs getCertLsByTyshxydmAndCerttypecode(String tyshxydm, String certtypecode);
}
