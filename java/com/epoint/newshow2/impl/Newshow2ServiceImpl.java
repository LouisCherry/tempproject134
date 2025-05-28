package com.epoint.newshow2.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.newshow2.api.Newshow2Service;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
@Component
public class Newshow2ServiceImpl implements Newshow2Service {

    /***
     * 办件满意度
     */
    @Override
    public Record getcitySatisfy(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "SELECT SUM(case when satisfied = '1' or satisfied = '2'  or satisfied = '0' THEN 1 else 0 END) AS fcmanyi,"
                    + " SUM(case when satisfied = '4' or satisfied = '5' THEN 1 else 0 END) AS bumanyi,SUM(case when satisfied = '3' THEN 1 else 0 END) AS jbmanyi"
                    + " from audit_online_evaluat e INNER JOIN audit_project p on p.RowGuid = e.ClientIdentifier where clienttype =10 and ACCEPTUSERGUID is NOT NULL and p.areaCode = ?1";
            return CommonDao.getInstance().find(sql, Record.class, areaCode);
        } else {
            sql = "SELECT SUM(case when satisfied = '1' or satisfied = '2'  or satisfied = '0' THEN 1 else 0 END) AS fcmanyi,"
                    + " SUM(case when satisfied = '4' or satisfied = '5' THEN 1 else 0 END) AS bumanyi,SUM(case when satisfied = '3' THEN 1 else 0 END) AS jbmanyi"
                    + " from audit_online_evaluat e INNER JOIN audit_project p on p.RowGuid = e.ClientIdentifier where clienttype =10 and ACCEPTUSERGUID is NOT NULL";
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    /***
     * 部门业务量top10
     */
    @Override
    public List<Record> geteventType() {
        // String sql = "SELECT count(1) as VALUE ,sum(TOTALINVEST) money, substring(itemcode, 6, 6) areacode FROM audit_rs_item_baseinfo where substring(itemcode, 6, 6)!='370871' and substring(itemcode, 6, 6)!='370893' and YEAR(itemstartdate) = year(now()) GROUP BY substring(itemcode, 6, 6) ORDER BY  sum(TOTALINVEST) DESC limit 15";
        String sql = "select areacode,sum(TOTALINVEST) money ,sum(num) as VALUE from  ("
                + " select  substring(itemcode, 1, 20) itemcode ,1 as num, substring(itemcode, 6, 6) areacode, TOTALINVEST"
                + " from  audit_rs_item_baseinfo where length(substring(itemcode, 6, 6))=6 and substring(itemcode, 6, 6) like '3708%' "
                + " and ifnull(TOTALINVEST,'')<>''  and YEAR(itemstartdate) = year(now())"
                + "  and substring(itemcode, 6, 6)!='370871' and substring(itemcode, 6, 6)!='370893'"
                + " group by substring(itemcode, 1, 20) ) a group by areacode order by sum(TOTALINVEST) desc  ";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    @Override
    public List<Record> getWindowNumByCenterguid(String centerGuid) {
        String sql = "select b.oushortname as name,b.ouguid,count(1) as value from audit_orga_window a join frame_ou b on a.ouguid = b.ouguid where centerguid = ? group by b.ouguid order by value desc limit 15";
        return CommonDao.getInstance().findList(sql, Record.class, centerGuid);
    }

    @Override
    public Record getQueueNumByOuguid(String weekbegin, String centerguid, String ouguid) {
        String sql = "SELECT count(1) as total FROM audit_queue_history a join audit_orga_window b on a.HANDLEWINDOWGUID = b.rowguid WHERE HANDLEWINDOWGUID != '' AND GETNOTIME >= ? AND a.centerguid = ? and b.ouguid = ? ";
        return CommonDao.getInstance().find(sql, Record.class, weekbegin, centerguid, ouguid);
    }

    @Override
    public List<Record> getapplyerTypeByAreacode(String areaCode) {
        String sql = "select APPLYERTYPE,count(1) as num from audit_project where areacode = ? and YEAR(APPLYDATE) = year(now()) group by APPLYERTYPE";
        return CommonDao.getInstance().findList(sql, Record.class, areaCode);
    }


    /***
     * 地图数据
     */
    @Override
    public List<Record> getmapData() {
        int year = EpointDateUtil.getYearOfDate(new Date());
        String year_new = year + "-01-01 00:00:00";
        String sql = "SELECT a.areacode name,count(1) value FROM audit_project a WHERE a.AREACODE<>'370800' and a.status > 10 and a.status <> 20 and a.status <> 22 and " +
                "a.applydate >= ? GROUP BY AREACODE  order by value desc limit 5";
        return CommonDao.getInstance().findList(sql, Record.class, year_new);
    }

    @Override
    public List<Record> getLsmapData() {
        String sql = "SELECT b.areacode name,count(1) value FROM audit_project a join frame_ou_extendinfo b on a.ouguid = b.ouguid WHERE a.AREACODE = '370832' and b.areacode <> '370832' and a.status > 10 and a.status <> 20 and a.status <> 22 and year(a.applydate)=year(NOW()) GROUP BY b.AREACODE ORDER BY value desc limit 6";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    @Override
    public List<Record> getmapbanjian() {
        int year = EpointDateUtil.getYearOfDate(new Date());
        String year_new = year + "-01-01 00:00:00";
        int month = EpointDateUtil.getMonthOfDate(new Date())+1;
        int day = EpointDateUtil.getDayOfDate(new Date());
        String sql = "SELECT a.areacode as name,SUM(case when a.applydate >= ? THEN 1 else 0 END) as value," +
                "SUM(case when MONTH(a.applydate) = ? THEN 1 else 0 END) as month," +
                "SUM(case when day(a.applydate) = ? AND MONTH(a.applydate) = ? THEN 1 else 0 END) as day " +
                "FROM audit_project a WHERE a.AREACODE<>'370800' and a.status > 10 and a.status <> 20 and a.status <> 22 " +
                "AND a.applydate >= ? GROUP BY AREACODE limit 14 ";
        return CommonDao.getInstance().findList(sql, Record.class, year_new, month, day, month, year_new);
    }

    @Override
    public List<Record> getLsmapbanjian() {
        String sql = "SELECT b.areacode as name,SUM(case when year(a.applydate) = year(NOW()) THEN 1 else 0 END) as value,SUM(case when MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as month,SUM(case when day(a.applydate) = day(NOW()) AND MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as day FROM audit_project a join frame_ou_extendinfo b on a.ouguid = b.ouguid WHERE a.AREACODE = '370832' and b.areacode <> '370832' and a.status > 10 and a.status <> 20 and a.status <> 22 AND year(a.applydate)=year(NOW()) GROUP BY b.AREACODE ORDER BY value DESC limit 14 ";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    /***
     * 本年、月、日实时办件量
     */
    @Override
    public Record gethandleEvent(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            if ("370883".equals(areaCode)) {
                sql = "select count(case when year(GETNOTIME)= year(now()) then 1 end ) as year,sum(case when month(GETNOTIME)= month(now()) and year(GETNOTIME)= year(now()) then 1 else 0 end) month";
                sql += " from audit_queue_history where centerGuid = '25b4169f-a79d-4721-8cc6-7e578b6aa936'";
            } else {
                sql = "SELECT sum(CASE WHEN applydate >= DATE_FORMAT(NOW(), '%Y-01-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 YEAR), '%Y-01-01') THEN 1 ELSE 0 END) YEAR,sum(CASE WHEN applydate >= DATE_FORMAT(NOW(), '%Y-%m-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH), '%Y-%m-01') THEN 1 ELSE 0 END) MONTH FROM audit_project WHERE status > 10 and status <> 20 and status <> 22 AND applydate >= DATE_FORMAT(NOW(), '%Y-01-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 YEAR), '%Y-01-01') and areaCode = ? ";
            }
            return CommonDao.getInstance().find(sql, Record.class, areaCode, areaCode);
        } else {
            sql = "SELECT sum(CASE WHEN applydate >= DATE_FORMAT(NOW(), '%Y-01-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 YEAR), '%Y-01-01') THEN 1 ELSE 0 END) YEAR,sum(CASE WHEN applydate >= DATE_FORMAT(NOW(), '%Y-%m-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 MONTH), '%Y-%m-01') THEN 1 ELSE 0 END) MONTH FROM audit_project WHERE status > 10 and status <> 20 and status <> 22 AND applydate >= DATE_FORMAT(NOW(), '%Y-01-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 YEAR), '%Y-01-01')";
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    /***
     * 市直办件总量
     */
    @Override
    public Record getcityDatabyid(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            if ("370883".equals(areaCode)) {
                sql = "select count(case when year(GETNOTIME)= year(now()) then 1 end ) as total,sum(case when month(GETNOTIME)= month(now()) and year(GETNOTIME)= year(now()) then 1 else 0 end) month";
                sql += " from audit_queue_history where centerGuid = '25b4169f-a79d-4721-8cc6-7e578b6aa936'";
            } else {
                sql = "SELECT count(1) total,sum(case when month(a.applydate)=month(NOW()) THEN 1 else 0 END) month FROM audit_project a,frame_ou_extendinfo b " +
                        "where a.ouguid = b.ouguid and LENGTH(b.AREACODE) = 6 and a.areacode= ? and b.areacode = ? and status > 10 and status <> 20 and status <> 22 and YEAR(a.applydate)=YEAR(NOW()) ";
            }
            return CommonDao.getInstance().find(sql, Record.class, areaCode, areaCode);
        } else {
            sql = "SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month FROM audit_project WHERE  areacode='370800' and status > 10 and status <> 20 and status <> 22 and YEAR(applydate)=YEAR(NOW()) ";
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    /***
     * 服务对象画像
     */
    @Override
    public Record getsource(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "SELECT sum(case when APPLYWAY = '40' THEN 1 else 0 END) wx,sum(case when APPLYWAY = '20' THEN 1 else 0 END) chuangkou,sum(case when APPLYWAY = '10' or APPLYWAY = '11' THEN 1 else 0 END) waiwang,sum(case when APPLYWAY = '50' THEN 1 else 0 END) zz FROM audit_project WHERE APPLYWAY is not NULL AND ACCEPTUSERGUID is NOT NULL and areaCode = ?1";
            return CommonDao.getInstance().find(sql, Record.class, areaCode);
        } else {
            sql = "SELECT sum(case when APPLYWAY = '40' THEN 1 else 0 END) wx,sum(case when APPLYWAY = '20' THEN 1 else 0 END) chuangkou,sum(case when APPLYWAY = '10' or APPLYWAY = '11' THEN 1 else 0 END) waiwang,sum(case when APPLYWAY = '50' THEN 1 else 0 END) zz FROM audit_project WHERE APPLYWAY is not NULL AND ACCEPTUSERGUID is NOT NULL";
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    /***
     * 热门办理事项TOP5
     */
    @Override
    public List<Record> geteventTop5(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "select  name,sum(value) as value  from (select name,value from (SELECT a.PROJECTNAME name,count(1) value FROM audit_project a WHERE a.status > 10 and a.status <> 20 and a.status <> 22 and a.areaCode = ? and YEAR(applydate)=YEAR(NOW()) GROUP BY a.TASKID  ORDER BY value DESC LIMIT 20) a ";
            sql += " union all select name,value from (SELECT a.PROJECTNAME name,count(1) value FROM lc_project a WHERE a.status > 10 and a.status <> 20 and a.status <> 22 and a.areaCode = ? and YEAR(applydate)=YEAR(NOW()) GROUP BY a.TASKID  ORDER BY value DESC LIMIT 20) b) dd group by name order by sum(value) desc limit 5";
            return CommonDao.getInstance().findList(sql, Record.class, areaCode, areaCode);
        } else {
            sql = "select  name,sum(value) as value  from (select name,value from (SELECT a.PROJECTNAME name,count(1) value FROM audit_project a WHERE a.status > 10 and a.status <> 20 and a.status <> 22 and YEAR(applydate)=YEAR(NOW()) GROUP BY a.TASKID  ORDER BY value DESC LIMIT 20) a ";
            sql += " union all select name,"
                    + " CASE WHEN NAME = '职工参保登记' THEN value \\+ 2528687 \\+ 1668624 \\+ 415153 \\+ 333555 "
                    + "	WHEN NAME = '长期异地工作人员备案' THEN value \\+ 2262741"
                    + "	WHEN NAME = '居民养老保险待遇核定支付' THEN value \\+ 1549131 \\+ 1053806 \\+ 959210"
                    + "	WHEN NAME = '居民养老保险参保登记' THEN value \\+ 1045997 \\+ 369804"
                    + "	WHEN NAME = '公司设立登记' THEN value \\+ 717545"
                    + "	WHEN NAME = '城乡规划查询' THEN value \\+ 545719 \\+ 224470"
                    + "	WHEN NAME = '社会保障卡领取' THEN value \\+ 538707"
                    + "	WHEN NAME = '医疗费用零星报销' THEN value \\+ 508334"
                    + "	WHEN NAME = '道路运输从业人员从业资格证件换证' THEN value \\+ 470591"
                    + "	WHEN NAME = '失业人员一次性丧葬补助金及抚恤金申领' THEN value \\+ 404580"
                    + "	WHEN NAME = '家庭成员变更备案' THEN value \\+ 284105"
                    + "	WHEN NAME = '企业职工参保登记' THEN value \\+ 230520"
                    + "	WHEN NAME = '企业社会保险缴费基数申报' THEN value \\+ 230020 ELSE value END AS VALUE "
                    + "value from (SELECT a.PROJECTNAME name,count(1) value FROM lc_project a WHERE a.status > 10 and a.status <> 20 and a.status <> 22 and YEAR(applydate)=YEAR(NOW()) GROUP BY a.TASKID  ORDER BY value DESC LIMIT 20) b) dd group by name order by sum(value) desc limit 5";
            return CommonDao.getInstance().findList(sql, Record.class);
        }
    }

    /***
     * 全市办结量趋势
     */
    @Override
    public List<Record> gettrend(String areaCode) {
        String sql = "";
        int year = EpointDateUtil.getYearOfDate(new Date());
        int month = EpointDateUtil.getMonthOfDate(new Date())+1;
        String year_new = year + "-01-01 00:00:00";
        //如果是一月的话，需要设置为当年第一天到一月最后一天，否则按照当年第一天到当前月第一天
        Timestamp endtime = null;
        if(1==month){
            String dateTimeString = year + "-02-01 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                // 将 String 转换为 java.sql.Timestamp
                endtime = new Timestamp(sdf.parse(dateTimeString).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            //当前月的一开始
            LocalDate currentMonthFirstDay = LocalDate.now().withDayOfMonth(1);
            LocalDateTime firstDayDateTime = LocalDateTime.of(currentMonthFirstDay, LocalTime.MIN);
            endtime = Timestamp.valueOf(firstDayDateTime);
        }

        if (StringUtil.isNotBlank(areaCode)) {
            if ("370883".equals(areaCode)) {
                sql = "SELECT concat(month(GETNOTIME),'月') as name,count(1) as value FROM audit_queue_history WHERE GETNOTIME is NOT NULL AND year(GETNOTIME)=year(NOW()) ";
                sql += " and centerGuid = '25b4169f-a79d-4721-8cc6-7e578b6aa936' GROUP BY month(GETNOTIME)";
                return CommonDao.getInstance().findList(sql, Record.class);
            } else {
                sql = "select concat(sname, '月') as name,sum(value) as value from (select sname,value from (SELECT month(BANJIEDATE) as sname,count(1) as value FROM " +
                        "audit_project WHERE BANJIEDATE is NOT NULL and status > 89 and areaCode = ? AND BANJIEDATE >= ? AND BANJIEDATE < ? GROUP BY month(BANJIEDATE)) a ";
                sql += " ) dd group by name order by sname";
                return CommonDao.getInstance().findList(sql, Record.class, areaCode, year_new,endtime);
            }
        } else {
            sql = "select  concat(sname, '月') as name,sum(value) as value from (select sname,value from (SELECT month(BANJIEDATE) as sname,count(1) as value FROM " +
                    "audit_project WHERE BANJIEDATE is NOT NULL and status > 89 AND BANJIEDATE >= ? AND BANJIEDATE < ? GROUP BY month(BANJIEDATE)) a ";
            sql += " ) dd group by name order by sname ";
            return CommonDao.getInstance().findList(sql, Record.class, year_new,endtime);
        }
    }

    /***
     * 取号情况
     */
    public Record getDaycount(String centerGuid) {
        String sql = "";
        if (StringUtil.isNotBlank(centerGuid)) {
            sql = "SELECT count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') then 1 end ) as daycount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='1' then 1 end ) as nowcount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='0' then 1 end ) as waitcount"
                    + " from audit_queue where HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8' and centerGuid = ?1";
            return CommonDao.getInstance().find(sql, Record.class, centerGuid);
        } else {
            sql = "SELECT count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') then 1 end ) as daycount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='1' then 1 end ) as nowcount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='0' then 1 end ) as waitcount"
                    + " from audit_queue where HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8' and centerGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884')";
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    @Override
    public int getYearcount(String centerGuid) {
        String sql = "";
        if (StringUtil.isNotBlank(centerGuid)) {
            sql = "select count(case when year(GETNOTIME)=year(NOW()) then 1 end ) as yaercount from audit_queue_history where HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8' and centerGuid = ?1";
            return CommonDao.getInstance().find(sql, Integer.class, centerGuid);
        } else {
            sql = "select count(case when year(GETNOTIME)=year(NOW()) then 1 end ) as yaercount from audit_queue_history where HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8' and centerGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884')";
            return CommonDao.getInstance().find(sql, Integer.class);
        }
    }

    @Override
    public String findOushortname(Object object) {
        String sql = "SELECT oushortname from frame_ou where ouguid =?";
        return CommonDao.getInstance().find(sql, String.class, object);
    }

    @Override
    public String getDayQueue(String centerGuid) {
        String sql = "";
        if (StringUtil.isNotBlank(centerGuid)) {
            sql = "SELECT count(a.ROWGUID) from audit_queue a join audit_queue_tasktype b on a.TASKGUID=b.RowGuid where DATE_FORMAT(a.GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and a.centerGuid = ?1  and a.HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8'  ";
            return CommonDao.getInstance().find(sql, String.class, centerGuid);
        } else {
            sql = "SELECT count(a.ROWGUID) from audit_queue a join audit_queue_tasktype b on a.TASKGUID=b.RowGuid where a.HALLGuid <>'f71d6900-d8a7-47d9-9288-0db9f77775a8' and DATE_FORMAT(a.GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d')  and a.centerGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884')";
            return CommonDao.getInstance().find(sql, String.class);
        }
    }

    @Override
    public Record getYqByRowguid(String rowguid) {
        String sql = "select * from cert_info where rowguid = ?";
        return CommonDao.getInstance().find(sql, Record.class, rowguid);
    }


    /***
     * 获取不存在centerguid的办件
     */
    @Override
    public List<Record> getYcProjectList(int pageNumber, int pageSize) {
        String sql = "select b.task_id,b.taskname,b.ouname,b.areacode,a.projectname,a.applyername,a.applydate,a.rowguid from audit_project a join audit_task b on a.taskguid = b.rowguid ";
        sql += "where (a.centerguid='' OR a.centerguid IS NULL) and b.task_id is not null and IFNULL(b.IS_HISTORY,0)= 0 and b.is_enable = 1  and b.IS_EDITAFTERIMPORT=1 order by applydate desc";
        return CommonDao.getInstance().findList(sql, pageNumber, pageSize, Record.class);
    }

    /***
     * 获取不存在centerguid的办件总数
     */
    @Override
    public int getYcProjectCount() {
        String sql = "select count(1) from audit_project a join audit_task b on a.taskguid = b.rowguid ";
        sql += "where (a.centerguid='' OR a.centerguid IS NULL) and b.task_id is not null and IFNULL(b.IS_HISTORY,0)= 0 and b.is_enable = 1  and b.IS_EDITAFTERIMPORT=1 order by applydate desc";
        return CommonDao.getInstance().find(sql, Integer.class);
    }

    /***
     * 获取sgxkb表的所有数据
     */
    @Override
    public AuditSpSpSgxk getSgxkProjectBySubappGuid(String subappguid) {
        String sql = "select * from audit_sp_sp_sgxk where subappguid = '" + subappguid + "'";
        return CommonDao.getInstance().find(sql, AuditSpSpSgxk.class);
    }

    public List<FrameOu> getOuListByAreacode(String areacode) {
        String sql = "select distinct ouguid from frame_ou where OUCODE like '%" + areacode + "%' "
                + " and (ISNULL(tel)=0 and LENGTH(trim(tel))>0) GROUP BY OUGUID ORDER BY ORDERNUMBER desc";
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }

}
