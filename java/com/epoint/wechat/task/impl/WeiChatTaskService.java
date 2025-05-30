package com.epoint.wechat.task.impl;

import java.util.ArrayList;
import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

/**
 * 快递信息表对应的后台service
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
public class WeiChatTaskService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public WeiChatTaskService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskHottask record) {
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
        T t = baseDao.find(AuditTaskHottask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskHottask record) {
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
    public AuditTaskHottask find(Object primaryKey) {
        return baseDao.find(AuditTaskHottask.class, primaryKey);
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
    public AuditTaskHottask find(String sql, Object... args) {
        return baseDao.find(sql, AuditTaskHottask.class, args);
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
    public List<AuditTaskHottask> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskHottask.class, args);
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
    public List<AuditTaskHottask> findListByPage(SqlConditionUtil sqlConditionUtil, int pageNumber, int pageSize,
            Object... args) {

        String fields = " select a.*,b.taskname as hottaskname,b.ROWGUID as NEWTASKGUID from audit_task_hottask a LEFT JOIN audit_task b on a.TASKID=b.TASK_ID   ";

        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        StringBuffer sb = new StringBuffer();

        sb.append(sqlManageUtil.buildSql(sqlConditionUtil.getMap()));
        fields = fields + sb.toString()
                + " and (b.IS_HISTORY=0 or b.IS_HISTORY is null) and b.IS_EDITAFTERIMPORT=1 and b.IS_ENABLE=1 ORDER BY OperateDate DESC ";

        String sql = "select * from (" + fields + ") a LIMIT " + pageNumber + "," + pageSize + "";
        return baseDao.findList(sql, AuditTaskHottask.class);
    }

    public int getTotalNum(SqlConditionUtil sqlConditionUtil) {
        String fields = " select count(*) from audit_task_hottask a LEFT JOIN audit_task b on a.TASKID=b.TASK_ID  ";

        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        StringBuffer sb = new StringBuffer();

        sb.append(sqlManageUtil.buildSql(sqlConditionUtil.getMap()));
        fields = fields + sb.toString()
                + " and (b.IS_HISTORY=0 or b.IS_HISTORY is null) and b.IS_EDITAFTERIMPORT=1 and b.IS_ENABLE=1 ";

        return baseDao.queryInt(fields, AuditTaskHottask.class);
    }

    /**
     *  获取事项对应的二维码cliengguid
     *  @param taskid
     *  @return
     */
    public String getQrCodeUrl(String taskid) {
        String sql = "select ATTACHGUID from frame_attachinfo f INNER JOIN audit_task_taian t ON t.serviceCodeclientguid = f.CLIENGGUID WHERE task_id = ? ";
        return baseDao.queryString(sql, taskid);
    }

    /**
     *  获取事项对应的视频路径
     *  @param taskid
     *  @return
     */
    public String getVideoDemo(String taskid) {
        String sql = "select wechatvideoUrl from audit_task_taian WHERE task_id =? ";
        return baseDao.queryString(sql, taskid);
    }

    /**
     *  获取事项对应的图标以及事项简称
     *  @param taskid
     *  @return
     */
    public Record findTaskIconByTaskid(String taskid) {
        String sql = "select ATTACHGUID,shorttaskname from frame_attachinfo f INNER JOIN audit_task_taian t ON f.CLIENGGUID=t.wechattaskiconguid WHERE task_id = ? ";
        return baseDao.find(sql, Record.class, taskid);
    }

    /**
     * 根据事项task_id获取事项信息
     * @authory shibin
     * @version 2019年10月3日 下午7:15:41
     * @param taskid
     * @return
     */
    public AuditTask getTaskByTaskid(String taskid) {
        String sql = "select * from audit_task WHERE TASK_ID = ? AND IFNULL(IS_HISTORY,0) = 0 AND  IS_ENABLE ='1' AND IS_EDITAFTERIMPORT ='1' ";
        return baseDao.find(sql, AuditTask.class, taskid);
    }

    /**
     * 更新办件材料状态
     * @authory shibin
     * @version 2019年10月18日 下午2:10:21
     * @param projectGuid
     */
    public void updateProjectMaterialStatus(String projectGuid) {
        List<String> list = new ArrayList<String>();
        String attachguid = "";
        String sql = "select CLIENGGUID from audit_project_material WHERE PROJECTGUID = ?";
        String sqlAttach = "select ATTACHGUID from frame_attachinfo WHERE CLIENGGUID= ? ";
        String sqlUpdate = "UPDATE audit_project_material SET STATUS ='20' WHERE CLIENGGUID = ? AND PROJECTGUID= ? ";
        list = baseDao.findList(sql, String.class, projectGuid);
        for (String str : list) {
            attachguid = baseDao.queryString(sqlAttach, str);
            if (StringUtil.isNotBlank(attachguid)) {
                baseDao.execute(sqlUpdate, str, projectGuid);
            }
        }

    }
}
