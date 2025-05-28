package com.epoint.expert.expertinfo.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.common.services.DBServcie;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.expert.expertinfo.api.entity.ExpertInfo;

/**
 * 专家表对应的后台service
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:51]
 */
public class ExpertInfoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ExpertInfoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExpertInfo record) {
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
        T t = baseDao.find(ExpertInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ExpertInfo record) {
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
    public ExpertInfo find(Object primaryKey) {
        return baseDao.find(ExpertInfo.class, primaryKey);
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
    public ExpertInfo find(String sql, Object... args) {
        return baseDao.find(sql, ExpertInfo.class, args);
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
    public List<ExpertInfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, ExpertInfo.class, args);
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
    public List<ExpertInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ExpertInfo.class, args);
    }

    /**
     * 
     *  根据条件获取列表
     *  @param conditionMap
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertInfo> findListByCondition(Map<String, String> conditionMap) {
        return DBServcie.getInstance().getListByCondition(ExpertInfo.class, conditionMap);
    }

    /**
     * 
     * 获取符合评标专业的专家
     *  @param professionGuids
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    //TODO 确认评标专业规则
    public List<String> getExpertGuidsByProfession(String professionGuids) {
        String sql = "select rowguid from expert_info where PingBZY like ? and Is_del=0 and status=1";
        return baseDao.findList(sql, String.class, "%" + professionGuids + "%");
    }

    /**
     * 
     *  获取可用专家
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> getAvailiableExpertList() {
        String sql = "select rowguid from expert_info where Is_del=0 and status=1";
        return baseDao.findList(sql, String.class);
    }

    /**
     * 
     *  根据条件返回列表
     *  @param conditionMap
     *  @param pageNumber
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<ExpertInfo> getPageByCondition(Map<String, String> conditionMap, int pageNumber, int pageSize,
            String sortField, String sortOrder) {
        return DBServcie.getInstance().getListByPage(ExpertInfo.class, conditionMap, pageNumber, pageSize, sortField,
                sortOrder);
    }

    /**
     * 
     * 获取当前编号最大值
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getExpertFlowSn() {
        String sql = " SELECT max(ExpertNO) from expert_info ";
        return baseDao.queryInt(sql);
    }

    /**
     * 分页获取一个list
     *  @param conditionMap 条件
     *  @param pageNumber
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertInfo> findList(Map<String, String> conditionMap, int pageNumber, int pageSize, String sortField,
            String sortOrder) {
        // 专家编号
        String expertno = conditionMap.get("expertno");
        // 专家姓名
        String name = conditionMap.get("name");
        // 评标专业
        String pingbzy = conditionMap.get("pingbzy");
        // 事项名称
        String shixiangname = conditionMap.get("shixiangname");
        String sql = "";
        if (StringUtil.isNotBlank(shixiangname)) {
            sql = "select e.* from expert_info e RIGHT JOIN "
                    + " (select distinct(ExpertGuid) from audit_task aut RIGHT JOIN expert_task et on aut.task_id = et.task_id "
                    + " where aut.taskname LIKE '%" + shixiangname + "%') t on t.expertguid = e.rowguid where 1 = 1 ";

        }
        else {
            sql = "select * from  expert_info where 1 = 1";
        }

        if (StringUtil.isNotBlank(expertno)) {
            sql += " and expertno like '%" + expertno + "%' ";
        }

        if (StringUtil.isNotBlank(name)) {
            sql += " and name like '%" + name + "%'";
        }

        if (StringUtil.isNotBlank(pingbzy)) {
            sql += " and pingbzy = '" + pingbzy + "'";
        }
        // 去除逻辑删除的数据
        sql += " and is_del = '0' order by expertNo asc";
        return baseDao.findList(sql, pageNumber, pageSize, ExpertInfo.class);
    }

    /**
     * 获取一个list
     *  @param conditionMap 条件
     *  @param pageNumber
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<ExpertInfo> findList(Map<String, String> conditionMap) {
        // 专家编号
        String expertno = conditionMap.get("expertno");
        // 专家姓名
        String name = conditionMap.get("name");
        // 评标专业
        String pingbzy = conditionMap.get("pingbzy");
        // 事项名称
        String shixiangname = conditionMap.get("shixiangname");
        String sql = "";
        if (StringUtil.isNotBlank(shixiangname)) {
            sql = "select e.* from expert_info e RIGHT JOIN "
                    + " (select distinct(ExpertGuid) from audit_task aut RIGHT JOIN expert_task et on aut.task_id = et.task_id "
                    + " where aut.taskname LIKE '%" + shixiangname + "%') t on t.expertguid = e.rowguid where 1 = 1 ";

        }
        else {
            sql = "select * from  expert_info where 1 = 1";
        }

        if (StringUtil.isNotBlank(expertno)) {
            sql += " and expertno like '%" + expertno + "%' ";
        }

        if (StringUtil.isNotBlank(name)) {
            sql += " and name like '%" + name + "%'";
        }

        if (StringUtil.isNotBlank(pingbzy)) {
            sql += " and pingbzy like '%" + pingbzy + "%'";
        }
        // 去除逻辑删除的数据
        sql += " and is_del = '0' ";
        return baseDao.findList(sql, ExpertInfo.class);
    }

}
