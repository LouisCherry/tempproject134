package com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api.entity.AuditSpProjectinfo;

import java.util.List;
import java.util.Map;

public interface IAuditSpProjectinfoService {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */

    public int insert(AuditSpProjectinfo record);

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpProjectinfo record);

    public Integer countAuditSpProjectinfo(Map<String, Object> conditionMap);


    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */

    public AuditSpProjectinfo find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpProjectinfo findbymap(Map<String, String> conditionMap);

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<AuditSpProjectinfo> findList(Map<String, Object> conditionMap);

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<AuditSpProjectinfo> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize);

}
