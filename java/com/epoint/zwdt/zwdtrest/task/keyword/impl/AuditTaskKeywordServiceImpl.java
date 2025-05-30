package com.epoint.zwdt.zwdtrest.task.keyword.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.task.keyword.api.IAuditTaskKeywordService;
import com.epoint.zwdt.zwdtrest.task.keyword.api.entity.AuditTaskKeyword;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 事项关键字关系表对应的后台service实现类
 *
 * @author yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
@Component
@Service
public class AuditTaskKeywordServiceImpl implements IAuditTaskKeywordService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskKeyword record) {
        return new AuditTaskKeywordService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskKeywordService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskKeyword record) {
        return new AuditTaskKeywordService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskKeyword find(Object primaryKey) {
        return new AuditTaskKeywordService().find(primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditTaskKeyword find(String sql, Object... args) {
        return new AuditTaskKeywordService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskKeyword> findList(String sql, Object... args) {
        return new AuditTaskKeywordService().findList(sql, args);
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
    public List<AuditTaskKeyword> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditTaskKeywordService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditTaskKeyword(String sql, Object... args) {
        return new AuditTaskKeywordService().countAuditTaskKeyword(sql, args);
    }


    @Override
    public void deleteByTaskid(String taskid) {
        new AuditTaskKeywordService().deleteByTaskid(taskid);
    }

    @Override
    public List<String> findListByKeyWord(String keyWord, String areacode) {
        return new AuditTaskKeywordService().findListByKeyWord(keyWord, areacode);
    }

    @Override
    public List<String> findListBytaskname(String keyWord, String areaCode) {
        return new AuditTaskKeywordService().findListBytaskname(keyWord, areaCode);
    }

    @Override
    public PageData<AuditTask> getTaskListByTaskName(Record record, int pageInt, int pageSize, boolean fromKeyWord) {
        return new AuditTaskKeywordService().getTaskListByTaskName(record, pageInt, pageSize, fromKeyWord);
    }

    @Override
    public AuditTask getTaskcityByTaskname(String taskname) {
        return new AuditTaskKeywordService().getTaskcityByTaskname(taskname);
    }

    @Override
    public boolean Ishighlight(String itemValue, String taskname) {
        return new AuditTaskKeywordService().Ishighlight(itemValue, taskname);
    }

    @Override
    public List<AuditTask> getTaskDistrictByTaskname(String taskname, String areacode) {
        return new AuditTaskKeywordService().getTaskDistrictByTaskname(taskname, areacode);
    }

    @Override
    public List<AuditTask> getTaskTownByTaskname(String taskname, String areacode) {
        return new AuditTaskKeywordService().getTaskTownByTaskname(taskname, areacode);
    }

    @Override
    public List<AuditOrgaArea> getOrgaAreaListByAreacodeAndType(String areacode, String s) {
        return new AuditTaskKeywordService().getOrgaAreaListByAreacodeAndType(areacode, s);
    }

    @Override
    public boolean IshighlightTown(String xiaqucode, String taskname) {
        return new AuditTaskKeywordService().IshighlightTown(xiaqucode, taskname);
    }
    @Override
    public boolean IsTownTask(String xiaqucode, String taskname) {
        return new AuditTaskKeywordService().IsTownTask(xiaqucode, taskname);
    }
    @Override
    public List<Record> highlightareacode(String tasknames) {
        return new AuditTaskKeywordService().highlightareacode(tasknames);
    }
    @Override
    public List<AuditTask> getTaskByTaskname(String taskname, String areacode) {
        return new AuditTaskKeywordService().getTaskByTaskname(taskname, areacode);
    }

}
