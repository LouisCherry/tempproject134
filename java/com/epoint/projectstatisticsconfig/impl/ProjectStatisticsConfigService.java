package com.epoint.projectstatisticsconfig.impl;

import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.Parameter;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.projectstatisticsconfig.api.entity.ProjectStatisticsConfig;

/**
 * 办件统计配置表对应的后台service
 *
 * @author 15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
public class ProjectStatisticsConfigService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ProjectStatisticsConfigService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectStatisticsConfig record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(ProjectStatisticsConfig.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectStatisticsConfig record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public ProjectStatisticsConfig find(Object primaryKey) {
        return baseDao.find(ProjectStatisticsConfig.class, primaryKey);
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
    public ProjectStatisticsConfig find(String sql, Object... args) {
        return baseDao.find(sql, ProjectStatisticsConfig.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<ProjectStatisticsConfig> findList(String sql, Object... args) {
        return baseDao.findList(sql, ProjectStatisticsConfig.class, args);
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
    public List<ProjectStatisticsConfig> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ProjectStatisticsConfig.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countProjectStatisticsConfig(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * getAreaCodeByAreaName 根据areaname找到areacode
     *
     * @param areaName
     * @return String
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 11:13
     */
    public String getAreaCodeByAreaName(String areaName) {
        String sql = "select xiaqucode from audit_orga_area where xiaquname = ? ";
        Parameter parameter = new Parameter();
        parameter.getParamValue().add(areaName);
        parameter.setSql(sql);
        return baseDao.find(parameter.getSql(), String.class, parameter.getParamValue().toArray());
    }

    /**
     * getAllOuguidByAreacode 根据areacode，获取所有部门
     *
     * @param areacode
     * @return List<String>
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 13:55
     */
    public List<String> getAllOuguidByAreacode(String areacode) {
        String sql = "select ouguid from frame_ou_extendinfo where areacode = ? ";
        Parameter parameter = new Parameter();
        parameter.getParamValue().add(areacode);
        parameter.setSql(sql);
        return baseDao.findList(parameter.getSql(), String.class, parameter.getParamValue().toArray());
    }

    /**
     * getInfoByAreacodeAndOuguid 根据条件查找信息
     *
     * @param areacode
     * @param ouguid
     * @return ProjectStatisticsConfig
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 14:12
     */
    public ProjectStatisticsConfig getInfoByAreacodeAndOuguid(String areacode, String ouguid) {
        String sql = "select * from Project_Statistics_Config where area_code = ? and ou_guid = ? ";
        Parameter parameter = new Parameter();
        parameter.getParamValue().add(areacode);
        parameter.getParamValue().add(ouguid);
        parameter.setSql(sql);
        return baseDao.find(parameter.getSql(), ProjectStatisticsConfig.class, parameter.getParamValue().toArray());
    }

    /**
     * pageData 根据条件查询List
     *
     * @param first
     * @param pageSize
     * @param dataBean
     * @return PageData<Record>
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 15:10
     */
    public PageData<Record> pageData(int first, int pageSize, Record dataBean) {
        String sql = "";
        //调用sql条件拼接方法
        String sqlCondition = getSqlCondition(dataBean);
        Parameter parameter = new Parameter();

        //先根据数据来源判断需要用到哪个sql
        String projectsource = dataBean.get("projectsource");
        if (StringUtil.isNotBlank(projectsource)) {
            if ("1".equals(projectsource)) {
                sql = "select count(*) projectnum,ate.keshinum,(select TaskName from audit_task where RowGuid = ap.TASKGUID ) as taskname ,'1' as projectsource from lc_project_eight ap,audit_task_extension ate \n" +
                        " where ap.TASKGUID = ate.TASKGUID ";
                sql += sqlCondition;
            } else if ("2".equals(projectsource)) {
                sql = "select count(*) projectnum,ate.keshinum,(select TaskName from audit_task where RowGuid = ap.TASKGUID ) as taskname ,'2' as projectsource from audit_project ap,audit_task_extension ate \n" +
                        " where ap.TASKGUID = ate.TASKGUID and ap.is_lczj = '5' ";
                sql += sqlCondition;
            } else if ("3".equals(projectsource)) {
                sql = "select count(*) projectnum,ate.keshinum,(select TaskName from audit_task where RowGuid = ap.TASKGUID ) as taskname ,'3' as projectsource from audit_project ap ,audit_task_extension ate \n" +
                        " where ap.TASKGUID = ate.TASKGUID and ap.is_lczj <> '5' ";
                sql += sqlCondition;
            }
        } else {
            //查询所有办件来源，拼接搜索条件，拼接联查
            //导入办件
            sql = "select count(*) projectnum,ate.keshinum,ap.projectname as taskname,'1' as projectsource from lc_project_eight ap,audit_task_extension ate \n" +
                    " where ap.TASKGUID = ate.TASKGUID " +
                    " and ate.keshinum is not null " +
                    " and ate.keshinum <> '' ";
            sql += sqlCondition;

            //扫码办件
            sql += " union select count(*) projectnum,ate.keshinum,ap.projectname as taskname,'2' as projectsource from audit_project ap,audit_task_extension ate \n" +
                    " where ap.TASKGUID = ate.TASKGUID and ap.is_lczj = '5' " +
                    " and ate.keshinum is not null " +
                    " and ate.keshinum <> '' ";
            sql += sqlCondition;

            //正常办件
            sql += " union select count(*) projectnum,ate.keshinum,ap.projectname as taskname,'3' as projectsource from audit_project ap ,audit_task_extension ate \n" +
                    " where ap.TASKGUID = ate.TASKGUID and (ap.is_lczj <> '5' or  ap.is_lczj is null) " +
                    " and ate.keshinum is not null " +
                    " and ate.keshinum <> '' ";
            sql += sqlCondition;
        }

        //排序
        sql += " order by projectnum desc,keshinum";

        parameter.setSql(sql);
        List<Record> list = baseDao.findList(parameter.getSql(), first, pageSize, Record.class, parameter.getParamValue().toArray());
        List<Record> listCount = baseDao.findList(parameter.getSql(), Record.class, parameter.getParamValue().toArray());
        return new PageData<>(list, listCount == null ? 0 : listCount.size());
    }

    /**
     * getSqlCondition 拼接list查询语句
     *
     * @param dataBean
     * @return String
     * @author 成都研发4部-付荣煜
     * @date 2022/5/24 15:30
     */
    private String getSqlCondition(Record dataBean) {
        String sqlCondition = "";

        //先判断部门条件，筛选当前配置的部门/当前部门
        if (StringUtil.isNotBlank(dataBean.getStr("ouguid"))) {
            sqlCondition += " and ap.ouguid = '" + dataBean.getStr("ouguid") + "' ";
        }
        //申请时间开始
        if (StringUtil.isNotBlank(dataBean.getStr("applydateFrom"))) {
            sqlCondition += " and date_format(ap.applydate,'%Y-%m-%d') >= '" + dataBean.getStr("applydateFrom") + "' ";
        }
        //申请时间结束
        if (StringUtil.isNotBlank(dataBean.getStr("applydateTo"))) {
            sqlCondition += " and date_format(ap.applydate,'%Y-%m-%d') <= '" + dataBean.getStr("applydateTo") + "' ";
        }
        //受理时间开始
        if (StringUtil.isNotBlank(dataBean.getStr("acceptuserdateFrom"))) {
            sqlCondition += " and date_format(ap.acceptuserdate,'%Y-%m-%d') >= '" + dataBean.getStr("acceptuserdateFrom") + "' ";
        }
        //受理时间结束
        if (StringUtil.isNotBlank(dataBean.getStr("acceptuserdateTo"))) {
            sqlCondition += " and date_format(ap.acceptuserdate,'%Y-%m-%d') <= '" + dataBean.getStr("acceptuserdateTo") + "' ";
        }
        //办结时间开始
        if (StringUtil.isNotBlank(dataBean.getStr("banjiedateFrom"))) {
            sqlCondition += " and date_format(ap.banjiedate,'%Y-%m-%d') >= '" + dataBean.getStr("banjiedateFrom") + "' ";
        }
        //办结时间结束
        if (StringUtil.isNotBlank(dataBean.getStr("banjiedateTo"))) {
            sqlCondition += " and date_format(ap.banjiedate,'%Y-%m-%d') <= '" + dataBean.getStr("banjiedateTo") + "' ";
        }
        //办件状态
        if (StringUtil.isNotBlank(dataBean.getStr("status"))) {
            sqlCondition += " and ap.status = '" + dataBean.getStr("status") + "' ";
        }
        //所属科室
        if (StringUtil.isNotBlank(dataBean.getStr("keshinum"))) {
            sqlCondition += " and ate.keshinum = '" + dataBean.getStr("keshinum") + "' ";
        }
        //事项名称
        if (StringUtil.isNotBlank(dataBean.getStr("taskname"))) {
            sqlCondition += " and ap.projectname like '%" + dataBean.getStr("taskname") + "%' ";
        }

        //分组
        sqlCondition += " group by ate.keshinum,ap.projectname";

        return sqlCondition;
    }
}
