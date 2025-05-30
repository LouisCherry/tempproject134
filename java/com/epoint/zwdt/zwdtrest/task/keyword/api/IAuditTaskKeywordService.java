package com.epoint.zwdt.zwdtrest.task.keyword.api;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.task.keyword.api.entity.AuditTaskKeyword;

import java.io.Serializable;
import java.util.List;

/**
 * 事项关键字关系表对应的后台service接口
 *
 * @author yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
public interface IAuditTaskKeywordService extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskKeyword record);

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
    public int update(AuditTaskKeyword record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskKeyword find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditTaskKeyword find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskKeyword> findList(String sql, Object... args);

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
    public List<AuditTaskKeyword> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditTaskKeyword(String sql, Object... args);

    void deleteByTaskid(String taskid);

    List<String> findListByKeyWord(String keyWord, String areacode);

    List<String> findListBytaskname(String keyWord, String areaCode);

    PageData<AuditTask> getTaskListByTaskName(Record record, int firstResultTask, int parseInt, boolean fromKeyWord);

    AuditTask getTaskcityByTaskname(String taskname);

    boolean Ishighlight(String itemValue, String taskname);

    List<AuditTask> getTaskDistrictByTaskname(String taskname, String areacode);

    List<AuditTask> getTaskTownByTaskname(String taskname, String areacode);

    List<AuditOrgaArea> getOrgaAreaListByAreacodeAndType(String areacode, String s);

    boolean IshighlightTown(String xiaqucode, String taskname);
    boolean IsTownTask(String xiaqucode, String taskname);
    /**
     * 根据事项名称批量查询辖区
     * @param taskname
     * @return
     */
    List<Record> highlightareacode(String tasknames);

    List<AuditTask> getTaskByTaskname(String taskname, String areacode);
}

