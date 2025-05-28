package com.epoint.sghd.commonutil.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.util.List;
import java.util.Map;

public interface IJnSghdCommonutil {

    /**
     * 根据辖区获取部门信息
     *
     * @param areaCode
     * @return
     */
    public List<FrameOu> listOuinfo(String areaCode);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */

    public <T> List<T> findList(String sql, Class<T> clazz, Object... args);

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
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args);

    public String queryString(String sql, Object... args);

    public int queryInt(String sql, Object... args);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int update(T record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public <T> T find(String sql, Class<T> clazz, Object... args);

    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldstr, Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder, String userGuid);

    public PageData<Record> getTaskByWindowguid(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                String sortField, String sortOrder);

    //获取窗口下有事项的所有部门guid
    List<String> getAllOuguidsByWindowGuids(String windowGuids);

    public List<AuditTask> selectAuditTaskByCondition(String searchCondition, String areacode);

    public List<AuditTask> selectAuditTaskOuByObjectGuid(String objectGuid, String areacode);

    public List<AuditTask> findAuditTaskByareacode(String areacode);


}
