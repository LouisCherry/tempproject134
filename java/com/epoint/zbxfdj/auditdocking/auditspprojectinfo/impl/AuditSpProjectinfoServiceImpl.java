package com.epoint.zbxfdj.auditdocking.auditspprojectinfo.impl;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api.IAuditSpProjectinfoService;
import com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api.entity.AuditSpProjectinfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 工程信息表对应的后台service实现类
 *
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
@Component
public class AuditSpProjectinfoServiceImpl implements IAuditSpProjectinfoService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(AuditSpProjectinfo record) {
        return new AuditSpProjectinfoService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid 主键guid
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new AuditSpProjectinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(AuditSpProjectinfo record) {
        return new AuditSpProjectinfoService().update(record);
    }

    @Override
    public Integer countAuditSpProjectinfo(Map<String, Object> conditionMap) {
        return new AuditSpProjectinfoService().countAuditSpProjectinfo(conditionMap);
    }


    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public AuditSpProjectinfo find(Object primaryKey) {
        return new AuditSpProjectinfoService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param conditionMap 查询条件集合
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public AuditSpProjectinfo findbymap(Map<String, String> conditionMap) {
        return new AuditSpProjectinfoService().findbymap(conditionMap);
    }

    /**
     * 查找一个list
     *
     * @param conditionMap 查询条件集合
     * @return T extends BaseEntity
     */
    @Override
    public List<AuditSpProjectinfo> findList(Map<String, Object> conditionMap) {
        return new AuditSpProjectinfoService().findList(conditionMap);
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
    public PageData<AuditSpProjectinfo> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        return new AuditSpProjectinfoService().paginatorList(conditionMap, pageNumber, pageSize);
    }

}
