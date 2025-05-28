package com.epoint.xmz.thirdreporteddata.dantiinfov3.api;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 项目单体信息表对应的后台service接口
 *
 * @author ysai
 * @version [版本号, 2023-10-18 10:39:39]
 */
public interface IDantiInfoV3Service extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(DantiInfoV3 record);

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(DantiInfoV3 record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public DantiInfoV3 find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public DantiInfoV3 find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countDantiInfoV3(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param projectguid 办件唯一标识
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findListByProjectguid(String projectguid);

    /**
     * 查找一个list
     *
     * @param subappguid 子申报唯一标识
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findListBySubAppguid(String subappguid);

    /**
     * 根据gcguid查找一个list
     *
     * @param gcguid gcguid
     * @return T extends BaseEntity
     */
    public List<DantiInfoV3> findListByGCguid(String gcguid);

    /**
     * 分页查询
     *
     * @param 查询条件（map拼接）
     * @param 页码
     * @param 分页数
     * @param 排序字段
     * @param 排序方式desc    Or asc
     * @return PageData<DantiInfo>
     */
    public PageData<DantiInfoV3> getDantiInfoPageData(Map<String, String> conditionMap, Integer firstResult,
                                                      Integer maxResults, String sortField, String sortOrder);

    /**
     * 根据参数分页查询
     *
     * @param conditionSql
     * @param projectguid
     * @param selectedguid
     * @param gongchengguid
     * @param conditionList
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<DantiInfoV3> pageDantiInfo(String conditionSql, String projectguid, String selectedguid,
                                               String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                               String sortOrder);

    /**
     * 根据参数分页查询
     *
     * @param conditionSql
     * @param projectguid
     * @param subappguid
     * @param gongchengguid
     * @param conditionList
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<DantiInfoV3> pageDantiLisrInfo(String conditionSql, String projectguid, String subappguid,
                                                   String gongchengguid, List<Object> conditionList, int first, int pageSize, String sortField,
                                                   String sortOrder);

    /**
     * 根据查询条件查询所有记录列表
     *
     * @param 查询条件（map拼接）
     * @return List<DantiInfo>
     */
    public List<DantiInfoV3> getDantiInfoListByCondition(Map<String, String> conditionMap);

}
