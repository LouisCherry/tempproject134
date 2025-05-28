package com.epoint.zbxfdj.auditdocking.auditspcompany.api;

import java.util.List;
import java.util.Map;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zbxfdj.auditdocking.auditspcompany.api.entity.AuditSpCompany;

/**
 * 单位信息表对应的后台service接口
 *
 * @author WZW
 * @version [版本号, 2022-12-07 15:04:12]
 */
public interface IAuditSpCompanyService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpCompany record);

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
    public int update(AuditSpCompany record);

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    public Integer countAuditSpCompany(Map<String, Object> conditionMap);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditSpCompany find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpCompany find(Map<String, Object> conditionMap);

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    public List<AuditSpCompany> findList(Map<String, Object> conditionMap);

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<AuditSpCompany> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize);

    /**
     * 查找事项对应的窗口人员
     *
     * @return
     */
    public List<Record> findListWindowUserByTaskid(String taskid);

    /***
     *
     * [根据Participantsguid和spprojectguid查询数据]
     *
     * @param rowguid
     * @param spprojectguid
     * @param tableName
     * @return
     */
    public AuditSpCompany findByParticipantsguidAndSpprojectguid(String participantsguid, String spprojectguid,
                                                                 String tableName);
}
