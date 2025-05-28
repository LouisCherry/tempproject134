package com.epoint.evainstance.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.IEvainstanceService;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.evainstance.service.EvainstanceService;

@Component
@Service
public class EvainstanceServiceImpl implements IEvainstanceService
{
    private static final long serialVersionUID = 2675312000937798436L;
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Evainstance record) {
        return new EvainstanceService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new EvainstanceService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Evainstance record) {
        return new EvainstanceService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public Evainstance find(Object primaryKey) {
       return new EvainstanceService().find(primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public Evainstance find(String sql, Object... args) {
        return new EvainstanceService().find(sql,args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Evainstance> findList(String sql, Object... args) {
       return new EvainstanceService().findList(sql,args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<Evainstance> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new EvainstanceService().findList(sql,pageNumber,pageSize,args);
    }
    
     /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
     @Override
    public Integer countEvainstance(String sql, Object... args){
        return new EvainstanceService().countEvainstance(sql, args);
    }


    public List<Record> getEvalDetail(String grade) {
        return new EvainstanceService().getEvalDetail(grade);
    }

    public int addEvainstance(Evainstance evainstance) {
        return new EvainstanceService().addEvainstance(evainstance);
    }

    public Record findProject(String projectNo, String areacode) {
        return new EvainstanceService().findProject(projectNo, areacode);
    }

    public boolean isExistEvaluate(String projectNo, int servicenum) {
        return new EvainstanceService().isExistEvaluate(projectNo, servicenum);
    }

    public void insertEvainstanceState(Record record) {
        new EvainstanceService().insertEvainstanceState(record);
    }

    public void updateEvainstanceState(Record record) {
        new EvainstanceService().updateEvainstanceState(record);
    }

    public int getMaxServicenum(String flowsn) {
        return new EvainstanceService().getMaxServicenum(flowsn);
    }

    public boolean findProService(String flowsn, String workItemGuid) {
        return new EvainstanceService().findProService(flowsn, workItemGuid);
    }

    public boolean isExistProService(String projectno, int assessNumber) {
        return new EvainstanceService().isExistProService(projectno, assessNumber);
    }

    public Record getServiceByProjectno(String projectno, int assessNumber) {
        return new EvainstanceService().getServiceByProjectno(projectno, assessNumber);
    }

    public Evainstance findEvaluate(String projectno, int assessNumber) {
        return new EvainstanceService().findEvaluate(projectno, assessNumber);
    }

    @Override
    public Record findAuditProjectByFlown(String projectno) {
        // TODO Auto-generated method stub
        return new EvainstanceService().findAuditProjectByFlown(projectno);
    }
    
    @Override
    public AuditCommonResult<PageData<Evainstance>> getEvaluateservicePageData(String fieldstr,
            Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
            String sortOrder, String userGuid) {
        EvainstanceService auditProjectService = new EvainstanceService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData<Evainstance> projectList = auditProjectService.getRecordPageData(fieldstr,
                    Evainstance.class, conditionMap, firstResult, maxResults, sortField, sortOrder, "", userGuid);
            result.setResult(projectList);
        }
        catch (Exception var10) {
            result.setSystemFail(var10.getMessage());
        }

        return result;
    }

    @Override
    public List<Record> getServiceByProjectno(String projectno) {
        
        return new EvainstanceService().getServiceByProjectno(projectno);
    }

    @Override
    public Record getZhibiao(String string) {
        // TODO Auto-generated method stub
        return  new EvainstanceService().getZhibiao(string);
    }

    @Override
    public int findFcbmyTotal(String areacode) {
        // TODO Auto-generated method stub
        return new EvainstanceService().findFcbmyTotal( areacode);
    }

    @Override
    public int findBmyTotal(String areacode) {
        // TODO Auto-generated method stub
        return new EvainstanceService().findBmyTotal(areacode);
    }
    @Override
    public List<Evainstance> getPageDate(Evainstance data) {
        return new EvainstanceService().getPageDate(data);
    }

}
