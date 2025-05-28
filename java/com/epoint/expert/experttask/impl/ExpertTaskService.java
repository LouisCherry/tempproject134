package com.epoint.expert.experttask.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.expert.experttask.api.entity.ExpertTask;

/**
 * 专家关联事项表对应的后台service
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:57]
 */
public class ExpertTaskService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExpertTaskService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertTask record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(ExpertTask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertTask record) {
        return baseDao.update(record);
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
    public ExpertTask find(Object primaryKey) {
        return baseDao.find(ExpertTask.class, primaryKey);
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
    public ExpertTask find(String sql, Object... args) {
        return baseDao.find(sql, ExpertTask.class, args);
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
    public List<ExpertTask> findList(String sql, Object... args) {
        return baseDao.findList(sql, ExpertTask.class, args);
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
    public List<ExpertTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExpertTask.class, args);
    }
    
    /**
     * 获取关联事项guid列表 
     *  @param expertinfoguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getTaskId(String expertinfoguid){
        String sql = "select Task_ID from Expert_Task where ExpertGuid = ?";
        return baseDao.findList(sql, String.class, expertinfoguid);
    }
    
    /**
     * 根据专家guid删除专家关联事项 
     *  @param expertguid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteByExpertguid(String expertguid) {
        String sql = "delete from expert_task where ExpertGuid = ?";
        baseDao.execute(sql, expertguid);
    }

    /**
     * 
     *  根据事项获取专家
     *  @param taskid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getExpertGuidsByTaskid(String taskid) {
        String sql = "select expertguid from expert_task where task_id=?";
        return baseDao.findList(sql, String.class, taskid);
    }

}
