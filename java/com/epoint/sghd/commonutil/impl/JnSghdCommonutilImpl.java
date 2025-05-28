package com.epoint.sghd.commonutil.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 事项维护对应接口实现
 *
 * @version [版本号, 2019年2月27日]
 * @作者 shibin
 */
@Service
@Component
public class JnSghdCommonutilImpl implements IJnSghdCommonutil {

    @Override
    public List<FrameOu> listOuinfo(String areaCode) {
        return new JnSghdCommonutilService().listOuinfo(areaCode);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    @Override
    public <T> List<T> findList(String sql, Class<T> clazz, Object... args) {
        return new JnSghdCommonutilService().findList(sql, clazz, args);
    }

    @Override
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args) {
        return new JnSghdCommonutilService().findList(sql, pageNumber, pageSize, clazz, args);
    }

    @Override
    public String queryString(String sql, Object... args) {
        return new JnSghdCommonutilService().queryString(sql, args);
    }

    @Override
    public int queryInt(String sql, Object... args) {
        return new JnSghdCommonutilService().queryInt(sql, args);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public <T extends Record> int update(T record) {
        return new JnSghdCommonutilService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    @Override
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey) {
        return new JnSghdCommonutilService().find(clazz, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public <T> T find(String sql, Class<T> clazz, Object... args) {
        return new JnSghdCommonutilService().find(sql, clazz, args);
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldstr,
                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder, String userGuid) {
        JnSghdCommonutilService auditProjectService = new JnSghdCommonutilService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData<AuditProject> projectList = auditProjectService.getRecordPageData(fieldstr, AuditProject.class,
                    conditionMap, firstResult, maxResults, sortField, sortOrder, "", userGuid);
            result.setResult(projectList);
        } catch (Exception var10) {
            result.setSystemFail(var10.getMessage());
        }

        return result;
    }

    @Override
    public PageData<Record> getTaskByWindowguid(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                String sortField, String sortOrder) {
        CommonDao dao = CommonDao.getInstance();
        PageData<Record> pageData = new PageData<Record>();
        String sql = "select b.OUGUID,b.RowGuid,b.processguid,b.TASKNAME,b.TASK_ID,b.TYPE ";
        String sqlCount = "select count(1) ";
        String sqlCondition = " from audit_orga_windowtask a,audit_task b";
        AuditCommonService commonService = new AuditCommonService();
        sqlCondition += commonService.buildSql(conditionMap);
        sqlCondition += " and a.taskid=b.task_id";
        sql += sqlCondition;
        sqlCount += sqlCondition;
        List<Record> auditTaskList = dao.findList(sql, firstResult, maxResults, Record.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(dao.find(sqlCount, Integer.class));
        return pageData;
    }

    @Override
    public List<String> getAllOuguidsByWindowGuids(String windowGuids) {
        return null;
    }

    @Override
    public List<AuditTask> selectAuditTaskByCondition(String searchCondition, String areacode) {
        return new JnSghdCommonutilService().selectAuditTaskByCondition(searchCondition, areacode);
    }

    @Override
    public List<AuditTask> selectAuditTaskOuByObjectGuid(String objectGuid, String areacode) {
        return new JnSghdCommonutilService().selectAuditTaskOuByObjectGuid(objectGuid, areacode);
    }

    @Override
    public List<AuditTask> findAuditTaskByareacode(String areacode) {
        return new JnSghdCommonutilService().findAuditTaskByareacode(areacode);
    }


}
