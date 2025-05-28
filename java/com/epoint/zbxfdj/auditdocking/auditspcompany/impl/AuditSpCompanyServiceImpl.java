package com.epoint.zbxfdj.auditdocking.auditspcompany.impl;

import java.util.List;
import java.util.Map;

import com.epoint.zbxfdj.auditdocking.auditspcompany.api.IAuditSpCompanyService;
import com.epoint.zbxfdj.auditdocking.auditspcompany.api.entity.AuditSpCompany;
import org.springframework.stereotype.Component;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 单位信息表对应的后台service实现类
 *
 * @author WZW
 * @version [版本号, 2022-12-07 15:04:12]
 */
@Component
public class AuditSpCompanyServiceImpl implements IAuditSpCompanyService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(AuditSpCompany record) {
        return new AuditSpCompanyService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new AuditSpCompanyService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(AuditSpCompany record) {
        return new AuditSpCompanyService().update(record);
    }

    /**
     * 查询数量
     *
     * @param conditionMap 查询条件集合
     * @return Integer
     */
    @Override
    public Integer countAuditSpCompany(Map<String, Object> conditionMap) {
        return new AuditSpCompanyService().countAuditSpCompany(conditionMap);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public AuditSpCompany find(Object primaryKey) {
        return new AuditSpCompanyService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public AuditSpCompany find(Map<String, Object> conditionMap) {
        return new AuditSpCompanyService().find(conditionMap);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<AuditSpCompany> findList(Map<String, Object> conditionMap) {
        return new AuditSpCompanyService().findList(conditionMap);
    }

    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    @Override
    public PageData<AuditSpCompany> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        return new AuditSpCompanyService().paginatorList(conditionMap, pageNumber, pageSize);
    }

    @Override
    public List<Record> findListWindowUserByTaskid(String taskid) {
        return new AuditSpCompanyService().findListWindowUserByTaskid(taskid);
    }

    @Override
    public AuditSpCompany findByParticipantsguidAndSpprojectguid(String participantsguid, String spprojectguid,
                                                                 String tableName) {
        return new AuditSpCompanyService().findByParticipantsguidAndSpprojectguid(participantsguid, spprojectguid,
                tableName);
    }
}
