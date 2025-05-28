package com.epoint.jiningzwfw.projectstatistics.evaInstance.service.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jiem
 */
public class EvaInstanceStatisticsService {

    private ICommonDao baseDao;

    public EvaInstanceStatisticsService() {
        if (baseDao == null){
            baseDao = CommonDao.getInstance();
        }
    }

    /**
     * 查询分页的统计数据
     *
     * @param map
     * @param month    用于分页
     * @param first
     * @param pageSize
     * @return
     */
    public List<Record> findStatisticsPage(Map<String, String> map, String month, int first, int pageSize) {

        String sql = "select deptcode,proDepart,pf,sum(if(satisfaction=5,1,0)) fcmynum,sum(if(satisfaction=4,1,0)) mynum," +
                "sum(if(satisfaction=3,1,0)) jbmynum,sum(if(satisfaction=2,1,0)) bmynum,sum(if(satisfaction=1,1,0)) fcbmynum from evaInstance_";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        List<Record> list;

        if (StringUtil.isNotBlank(month)){
            // 根据月份分表进行查询，构建sql
            String[] split = month.split(",");
            List<String> sqlList = new ArrayList<>();
            for (String s : split) {
                sqlList.add("(" + sql + s + sqlManageUtil.buildSql(map) + " group by deptCode)");
            }
            String join = StringUtil.join(sqlList, " union all ");
            // 合并多张表的数据
            join = "select deptcode,proDepart,sum(fcmynum) fcmynum,sum(mynum) mynum,sum(jbmynum) jbmynum,sum(bmynum) bmynum,sum(fcbmynum) fcbmynum,pf from (" + join + ") t group by deptcode";
            list = baseDao.findList(join, first, pageSize, Record.class);
        }else {
            int monthOfDate = EpointDateUtil.getMonthOfDate(new Date()) + 1;
            String join = sql + monthOfDate + sqlManageUtil.buildSql(map) + " group by deptCode";
            list = baseDao.findList(join, first, pageSize, Record.class);
        }
        return list;
    }

    /**
     * 计算总数
     *
     * @param map
     * @param month 用于分页
     * @return
     */
    public int count(Map<String, String> map, String month) {
        String sql = "select count(1) from evaInstance_";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        int count = 0;

        if (StringUtil.isNotBlank(month)){
            // 根据月份分表进行查询，构建sql
            String[] split = month.split(",");
            for (String s : split) {
                String join= sql + s + sqlManageUtil.buildSql(map);
                count += baseDao.queryInt(join);
            }
        }else {
            int monthOfDate = EpointDateUtil.getMonthOfDate(new Date()) + 1;
            sql += monthOfDate + sqlManageUtil.buildSql(map);
            count += baseDao.queryInt(sql);
        }
        return count;
    }

    public int countStatistics(Map<String, String> map, String month) {

        String sql = "select distinct deptcode,proDepart from evaInstance_";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        int count = 0;

        if (StringUtil.isNotBlank(month)){
            // 根据月份分表进行查询，构建sql
            String[] split = month.split(",");
            List<String> sqlList = new ArrayList<>();
            for (String s : split) {
                sqlList.add("(" + sql + s + sqlManageUtil.buildSql(map) + " group by deptCode" + ")");
            }
            String join = StringUtil.join(sqlList, " union all ");
            // 合并多张表的数据
            join = "select deptcode from (" + join + ") t group by deptcode";
            count = baseDao.queryInt("select count(1) from (" + join + ") t");
        }else {
            int monthOfDate = EpointDateUtil.getMonthOfDate(new Date()) + 1;
            String join = sql + monthOfDate + sqlManageUtil.buildSql(map);
            count = baseDao.queryInt("select count(1) from (" + join + ") t");
        }

        return count;
    }

    public List<Record> findPage(Map<String, String> map, String month, int first, int pageSize) {

        String sql = "select * from evaInstance_";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        List<Record> list;

        if (StringUtil.isNotBlank(month)){
            // 根据月份分表进行查询，构建sql
            String[] split = month.split(",");
            List<String> sqlList = new ArrayList<>();
            for (String s : split) {
                sqlList.add("(" + sql + s + sqlManageUtil.buildSql(map) + ")");
            }
            String join = StringUtil.join(sqlList, " union all ");
            list = baseDao.findList(join, first, pageSize, Record.class);
        }else {
            int monthOfDate = EpointDateUtil.getMonthOfDate(new Date()) + 1;
            String join = sql + monthOfDate + sqlManageUtil.buildSql(map);
            list = baseDao.findList(join, first, pageSize, Record.class);
        }
        return list;
    }

    /**
     * 查询所有的好差评数据列表，用于导出
     * @param map
     * @param month
     * @return
     */
    public List<Record> findDetailList(Map<String, String> map, String month) {
        String sql = "select * from evaInstance_";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        List<Record> list;

        if (StringUtil.isNotBlank(month)){
            // 根据月份分表进行查询，构建sql
            String[] split = month.split(",");
            List<String> sqlList = new ArrayList<>();
            for (String s : split) {
                sqlList.add("(" + sql + s + sqlManageUtil.buildSql(map) + ")");
            }
            String join = StringUtil.join(sqlList, " union all ");
            list = baseDao.findList(join, Record.class);
        }else {
            int monthOfDate = EpointDateUtil.getMonthOfDate(new Date()) + 1;
            String join = sql + monthOfDate + sqlManageUtil.buildSql(map);
            list = baseDao.findList(join, Record.class);
        }
        return list;
    }
}
