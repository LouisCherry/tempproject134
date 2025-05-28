package com.epoint.sghd.commonutil.impl;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.util.List;
import java.util.Map;

/**
 * 事项维护对应service
 *
 * @version [版本号, 2019年2月27日]
 * @作者 shibin
 */
public class JnSghdCommonutilService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnSghdCommonutilService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 根据辖区获取部门信息
     *
     * @param areaCode
     * @return
     */
    public List<FrameOu> listOuinfo(String areaCode) {
        String sql = "select a.OUGUID,a.OUNAME from frame_ou a INNER JOIN frame_ou_extendinfo b ON a.OUGUID = b.OUGUID inner join audit_orga_area c on a.PARENTOUGUID=c.ouguid WHERE b.AREACODE = ? AND b.IS_WINDOWOU = '1' ORDER BY ORDERNUMBER DESC ";
        return baseDao.findList(sql, FrameOu.class, areaCode);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public <T> List<T> findList(String sql, Class<T> clazz, Object... args) {
        return baseDao.findList(sql, clazz, args);
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
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, clazz, args);
    }

    public String queryString(String sql, Object... args) {
        return baseDao.queryString(sql, args);
    }

    public int queryInt(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
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
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey) {
        return baseDao.find(clazz, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public <T> T find(String sql, Class<T> clazz, Object... args) {
        return baseDao.find(sql, clazz, args);
    }

    /**
     * 更新数据
     *
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int update(T record) {
        return baseDao.update(record);
    }

    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                             String keyword, String userGuid) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData();
        Entity en = baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        String areacode = conditionMap.get("areacode#zwfw#eq#zwfw#S");
        conditionMap.remove("areacode#zwfw#eq#zwfw#S");
        sb.append(sqlManageUtil.buildSql(conditionMap));
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like '%" + keyword + "%' or applyername like '%" + keyword + "%	')");
        }

        if (StringUtil.isNotBlank(userGuid)) {
            sb.append(" and ((ACCEPTUSERGUID = '" + userGuid
                    + "') OR (TASKTYPE = 2 AND RECEIVEUSERGUID = '" + userGuid + "' ) OR" + "(BANJIEUSERGUID = '"
                    + userGuid + "'))");
        }

        String sql = sb.toString();
        sql += " and ( areacode='"+areacode+"' or areacode ='' or areacode is null) ";

        sb = new StringBuffer(sql);
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }

        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) baseDao.findList(sqlRecord, first, pageSize, baseClass, new Object[0]);
        int dataCount = baseDao.queryInt(sqlCount, new Object[0]);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    /**
     * 获取部门下的在用事项
     *
     * @param objectGuid
     * @param areacode
     * @return
     */
    public List<AuditTask> selectAuditTaskOuByObjectGuid(String objectGuid, String areacode) {
        String sql = "select RowGuid,TASK_ID,ITEM_ID,OUNAME,OUGUID,TaskName,SHENPILB from audit_task  WHERE AREACODE = ? AND IFNULL(IS_HISTORY,0) ='0' AND IS_ENABLE ='1' AND IS_EDITAFTERIMPORT ='1'  AND OUGUID = ?  AND SHENPILB NOT IN ('02') ";
        return baseDao.findList(sql, AuditTask.class, areacode, objectGuid);
    }

    /**
     * 根据taskname获取事项
     *
     * @param searchCondition
     * @param areacode
     * @return
     */
    public List<AuditTask> selectAuditTaskByCondition(String searchCondition, String areacode) {
        // 区县事项 370982000000
        String sql = "select RowGuid,TASK_ID,ITEM_ID,OUNAME,OUGUID,TaskName,SHENPILB from audit_task WHERE taskname like '%"
                + searchCondition
                + "%'  AND AREACODE = ? AND IFNULL(IS_HISTORY,0) ='0' AND IS_ENABLE ='1' AND IS_EDITAFTERIMPORT ='1'  AND SHENPILB NOT IN ('02') ";
        return baseDao.findList(sql, AuditTask.class, areacode);
    }

    /**
     * 根据辖区获取所有在用事项
     *
     * @param areacode
     * @return
     */
    public List<AuditTask> findAuditTaskByareacode(String areacode) {
        String sql = "select rowguid,task_id,item_id,type,taskname,ouguid from audit_task WHERE AREACODE = ? AND IFNULL(IS_HISTORY,0) = '0' AND IS_ENABLE = '1' AND IS_EDITAFTERIMPORT= '1' AND SHENPILB NOT IN ('02')  ";
        return baseDao.findList(sql, AuditTask.class, areacode);
    }

}
