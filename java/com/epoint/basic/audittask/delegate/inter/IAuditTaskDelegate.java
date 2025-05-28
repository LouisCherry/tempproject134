package com.epoint.basic.audittask.delegate.inter;

import java.util.List;
import java.util.Map;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 下放事项表对应的后台service接口
 *
 * @author Administrator
 * @version [版本号, 2017-12-06 13:36:43]
 */
public interface IAuditTaskDelegate
{

    /**
     *
     * 新增下放事项对象
     *
     * @param auditTaskDelegate
     *  下放事项对象
     *
     * @return 事项信息集合
     */
    public AuditCommonResult<String> addAuditTaskDelegate(AuditTaskDelegate auditTaskDelegate);


    /**
     *
     * 新增下放事项对象
     *
     * @param auditTaskDelegate
     *  下放事项对象
     *
     * @return 事项信息集合
     */
    public AuditTaskDelegate getAuditTaskDelegatebyRowguid(String rowugid);

    /**
     *
     *  查询乡镇分页数据的
     *
     *  @param sql
     *      sql
     *  @param first
     *      起始页
     *  @param pageSize
     *      分页大小
     *  @param sortField
     *      排序字段
     *  @param sortOrder
     *      排序方式
     *  @return
     */
    public AuditCommonResult<PageData<Record>> getXZpageData(SqlConditionUtil sql, int first, int pageSize,
            String sortField, String sortOrder);

    /**
     *
     * 根据事项标识查询下放事项实体
     *
     *  @param taskID
     *      事项标识
     *  @param areacode
     *      辖区编码
     *  @return
     */
    public AuditCommonResult<AuditTaskDelegate> findByTaskIDAndAreacode(String taskID, String areacode);

    /**
     *
     * 更新下放事项实体
     *
     *  @param bean
     *      下放事项实体
     *  @return
     */
    public AuditCommonResult<Void> updata(AuditTaskDelegate bean);

    /**
     *
     *  获取乡镇区域下的所有可用事项
     *
     *  @param areaCode
     *      辖区编码
     *  @return
     */
    public AuditCommonResult<List<AuditTask>> getTaskListByArea(String areaCode);

    /**
     *  根据条件搜索乡镇事项
     *
     *  @param taskname
     *      事项名称
     *  @param areaCode
     *      辖区编码
     *  @return
     */
    public AuditCommonResult<List<AuditTask>> selectAuditTaskByCondition(String taskname, String areaCode);

    /**
     *
     *  根据事项标识获取所有的下放事项
     *
     *  @param taskid
     *      事项标识
     *  @return
     */
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateByTaskID(String taskid);

    /**
     *  根据市级事项版本唯一标识和辖区获取下放至该辖区的授权事项
     *
     *  @param taskid
     *      事项标识
     *  @param areacode
     *      辖区编码
     *  @return
     */
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateListByTaskIDAndAreacode(String taskid,
            String areacode);

    /**
     * 根据事项标识获取最新可用的事项
     *
     *  @param taskid
     *      事项标识
     *    @param areacode
     *    辖区编码
     *  @return
     */
    public AuditCommonResult<AuditTask> selectUsableTaskByTaskID(String taskid, String areacode);

    /**
     *
     *  根据区域标识获取所有下放事项实体
     *  @param areacode
     *      辖区编码
     *  @return
     */
    public AuditCommonResult<List<AuditTaskDelegate>> selectDelegateByAreacode(String areacode);

    /**
     *
     *  查询村级分页数据
     *
     *  @param sql
     *      sql
     *  @param first
     *      初始页
     *  @param pageSize
     *      分页大小
     *  @param sortField
     *      排序字段
     *  @param sortOrder
     *      排序方式
     *  @return
     */
    public AuditCommonResult<PageData<Record>> getCJpageData(SqlConditionUtil sql, int first, int pageSize, String sortField,
            String sortOrder);

    /**
     *
     *  查询乡镇分页数据的
     *
     *  @param sql
     *      sql
     *  @param first
     *      初始页
     *  @param pageSize
     *      分页大小
     *  @param sortField
     *      排序字段
     *  @param sortOrder
     *      排序方式
     *  @return
     */
    public AuditCommonResult<PageData<AuditTask>> getXZpageDataList(SqlConditionUtil sql, int first, int pageSize,
            String sortField, String sortOrder);

    /**
     *
     *
     *  @param rowguid 唯一标识
     */
    public void deleteByrowguid(String rowguid);

    /**
     *
     *  [根据条件呢查询列表数据]
     *  @param map
     *  @return
     */
    public List<AuditTaskDelegate> getDelegateListByCondition(Map<String, String> map);

    /**
     *
     *  [根据条件呢查询列表数据]
     *  @param xzareacode
     *  @param taskdelegateTypeXzfd
     *  @return
     */
    public List<AuditTaskDelegate> getDelegateListByAreacodeandType(String xzareacode, String taskdelegateTypeXzfd);
}
