package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity.SpglQypgxxb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 区域评估信息表对应的后台service接口
 *
 * @author Epoint
 * @version [版本号, 2023-09-15 13:56:34]
 */
public interface ISpglQypgxxbService extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpglQypgxxb record);

    /**
     * 删除数据
     *
     * @param guid
     * @return
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpglQypgxxb record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey
     * @return
     */
    public SpglQypgxxb find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql
     * @param args
     * @return
     */
    public SpglQypgxxb find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql
     * @param args
     * @return
     */
    public List<SpglQypgxxb> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     * @param args
     * @return
     */
    public List<SpglQypgxxb> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countSpglQypgxxb(String sql, Object... args);

    /**
     * 获取列表
     *
     * @param conditionMap
     * @param firstResult
     * @param maxResults
     * @param sortField
     * @param sortOrder
     * @return
     */
    public AuditCommonResult<PageData<SpglQypgxxb>> getAuditSpDanitemByPage(Map<String, String> conditionMap,
                                                                            int firstResult, int maxResults, String sortField, String sortOrder);

    /**
     * 获取list
     *
     * @param map
     * @return
     */
    public List<SpglQypgxxb> getListByMap(Map<String, String> map);

    /**
     * 获取未关联列表
     *
     * @param itemguid
     * @param firstResult
     * @param maxResults
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<SpglQypgxxb> getNotAssociationPageData(String itemguid, int firstResult, int maxResults,
                                                           String sortField, String sortOrder);

    List<SpglQypgxxb> getSpglQypgxxbListByitemguid(String rowguid);

}
