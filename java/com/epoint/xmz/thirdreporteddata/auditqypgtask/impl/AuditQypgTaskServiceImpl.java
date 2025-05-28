package com.epoint.xmz.thirdreporteddata.auditqypgtask.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.IAuditQypgTaskService;
import com.epoint.xmz.thirdreporteddata.auditqypgtask.api.entity.AuditQypgTask;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 区域评估事项表对应的后台service实现类
 *
 * @author ysai
 * @version [版本号, 2023-11-02 14:37:06]
 */
@Component
@Service
public class AuditQypgTaskServiceImpl implements IAuditQypgTaskService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditQypgTask record) {
        return new AuditQypgTaskService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditQypgTaskService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditQypgTask record) {
        return new AuditQypgTaskService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditQypgTask find(Object primaryKey) {
        return new AuditQypgTaskService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditQypgTask find(String sql, Object... args) {
        return new AuditQypgTaskService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditQypgTask> findList(String sql, Object... args) {
        return new AuditQypgTaskService().findList(sql, args);
    }

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
    public List<AuditQypgTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditQypgTaskService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditQypgTask(String sql, Object... args) {
        return new AuditQypgTaskService().countAuditQypgTask(sql, args);
    }

    @Override
    public List<AuditQypgTask> findAllList() {
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("1", "1");
        return new SQLManageUtil().getListByCondition(AuditQypgTask.class, sqlConditionUtil.getMap());
    }

    @Override
    public PageData<AuditQypgTask> getPageData(Map<String, String> map, int first, int pageSize, String sortField,
                                               String sortOrder) {
        return new AuditQypgTaskService().getPageData(map, first, pageSize, sortField, sortOrder);
    }

    /**
     * 根据编码查询单个区域评估事项实体
     * [一句话功能简述]
     *
     * @param taskcode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public AuditQypgTask findByTaskcode(String taskcode) {
        return new AuditQypgTaskService().findByTaskcode(taskcode);
    }

    /**
     * 根据标准事项编码查询集合
     * [一句话功能简述]
     *
     * @param basetaskguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<AuditQypgTask> getAuditQypgistByBaseTaskGuid(String basetaskguid) {
        return new AuditQypgTaskService().getAuditQypgistByBaseTaskGuid(basetaskguid);
    }

}
