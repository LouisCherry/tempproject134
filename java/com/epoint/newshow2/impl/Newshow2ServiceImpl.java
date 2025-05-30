package com.epoint.newshow2.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.newshow2.api.Newshow2Service;

@Service
@Component
public class Newshow2ServiceImpl implements Newshow2Service
{

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
        }else {
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
    public List<Record> geteventType(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "SELECT count(1) as value,OUNAME,OUGUID  from audit_project where AREACODE= ?1 GROUP BY OUGUID ORDER BY count(1) desc limit 10";
            return CommonDao.getInstance().findList(sql, Record.class, areaCode);
        }else {
            sql = "SELECT count(1) as value,OUNAME,OUGUID  from audit_project GROUP BY OUGUID ORDER BY count(1) desc limit 10";
            return CommonDao.getInstance().findList(sql, Record.class);
        }
        
    }

    /***
     * 地图数据
     */
    @Override
    public List<Record> getmapData() {
        String sql = "SELECT b.XiaQuName name,count(1) value FROM audit_project a LEFT JOIN audit_orga_area b ON a.AREACODE=b.XiaQuCode WHERE a.AREACODE<>'370800' and a.STATUS>24 GROUP BY AREACODE ORDER BY value desc LIMIT 5";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    @Override
    public List<Record> getmapbanjian() {
        String sql = "SELECT b.XiaQuName as name,SUM(case when year(a.applydate) = year(NOW()) THEN 1 else 0 END) as year,SUM(case when MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as month,SUM(case when day(a.applydate) = day(NOW()) AND MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as day FROM audit_project a LEFT JOIN audit_orga_area b ON a.AREACODE=b.XiaQuCode WHERE a.AREACODE<>'370800' and a.STATUS>24 AND year(a.applydate)=year(NOW()) GROUP BY AREACODE ORDER BY year DESC";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

    /***
     * 本年、月、日实时办件量
     */
    @Override
    public Record gethandleEvent(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "SELECT sum(case when YEAR(applydate)=YEAR(NOW()) THEN 1 else 0 END) year ,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month FROM audit_project WHERE status > 10 and status <> 20 and status <> 22 and YEAR(applydate)=YEAR(NOW()) and areaCode = ?1";
            return CommonDao.getInstance().find(sql, Record.class, areaCode);
        }else {
        	 sql = "select  sum(total) as year,sum(month) as month  from (select total,month from (SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month FROM audit_project WHERE  areacode='370800' and status > 10 and status <> 20 and status <> 22 and YEAR(applydate)=YEAR(NOW())) a ";
             sql += " union all select total as year,month from (SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month FROM lc_project WHERE  areacode='370800' and status > 10 and status <> 20 and status <> 22 and YEAR(applydate)=YEAR(NOW())) b) dd ";
             return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    /***
     * 市直办件总量
     */

    @Override
    public Record getcityDatabyid() {
        String sql = "SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month FROM audit_project WHERE `STATUS`>24 and areacode='370800' and YEAR(applydate)=YEAR(NOW())";
        return CommonDao.getInstance().find(sql, Record.class);
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
        }else {
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
            sql = "SELECT a.PROJECTNAME name,count(1) value FROM audit_project a WHERE a.ACCEPTUSERGUID is NOT NULL and a.`STATUS`>24 and a.areaCode = ?1 GROUP BY a.TASKID ORDER BY value DESC LIMIT 5";
            return CommonDao.getInstance().findList(sql, Record.class, areaCode);
        }else {
            sql = "SELECT a.PROJECTNAME name,count(1) value FROM audit_project a WHERE a.ACCEPTUSERGUID is NOT NULL and a.`STATUS`>24 GROUP BY a.TASKID ORDER BY value DESC LIMIT 5";
            return CommonDao.getInstance().findList(sql, Record.class);
        }
    }

    /***
     * 全市办结量趋势
     */
    @Override
    public List<Record> gettrend(String areaCode) {
        String sql = "";
        if (StringUtil.isNotBlank(areaCode)) {
            sql = "SELECT concat(month(BANJIEDATE),'月') as name,count(1) as value FROM audit_project WHERE BANJIEDATE is NOT NULL and areaCode = ?1 and `STATUS`>24 AND year(BANJIEDATE)=year(NOW()) GROUP BY month(BANJIEDATE)";
            return CommonDao.getInstance().findList(sql, Record.class, areaCode);
        }else {
            sql = "SELECT concat(month(BANJIEDATE),'月') as name,count(1) as value FROM audit_project WHERE BANJIEDATE is NOT NULL and `STATUS`>24 AND year(BANJIEDATE)=year(NOW()) GROUP BY month(BANJIEDATE)";
            return CommonDao.getInstance().findList(sql, Record.class);
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
                    + " from audit_queue where centerGuid = ?1";
            return CommonDao.getInstance().find(sql, Record.class, centerGuid);
        }else {
            sql = "SELECT count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') then 1 end ) as daycount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='1' then 1 end ) as nowcount,"
                    + " count(case when DATE_FORMAT(GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d') and STATUS='0' then 1 end ) as waitcount"
                    + " from audit_queue";           
            return CommonDao.getInstance().find(sql, Record.class);
        }
    }

    @Override
    public int getYearcount(String centerGuid) {
        String sql = "";
        if (StringUtil.isNotBlank(centerGuid)) {
            sql = "select count(case when year(GETNOTIME)=year(NOW()) then 1 end ) as yaercount from audit_queue_history where centerGuid = ?1";
            return CommonDao.getInstance().find(sql, Integer.class, centerGuid);
        }else {
            sql = "select count(case when year(GETNOTIME)=year(NOW()) then 1 end ) as yaercount from audit_queue_history";
            return CommonDao.getInstance().find(sql, Integer.class);
        }
    }

    @Override
    public String findOushortname(Object object) {
        String sql = "SELECT oushortname from frame_ou where ouguid =?";
        return CommonDao.getInstance().find(sql, String.class,object);
    }

    @Override
    public String getDayQueue() {
        String sql = "SELECT count(a.ROWGUID) from audit_queue a join audit_queue_tasktype b on a.TASKGUID=b.RowGuid where DATE_FORMAT(a.GETNOTIME,'%y-%m-%d') = DATE_FORMAT(now(),'%y-%m-%d')";
        return CommonDao.getInstance().find(sql, String.class);
    }
    
    @Override
    public Record getBusinessDetail(String certnum) {
        String sql = "select * from jn_build_part where code = ? order by version desc limit 1";
        return CommonDao.getInstance().find(sql, Record.class, certnum);
    }
    
    public int updateBusinessDetailStatus(String itemcode) {
        String sql = "update jn_build_part set is_enable = '0' where code = '" + itemcode + "'";
        int result = CommonDao.getInstance().execute(sql);
        CommonDao.getInstance().close();
        return result;
    }
    
    public Record getYqByRowguid(String rowguid)
    {
      String sql = "select * from cert_info where rowguid = ?";
      return CommonDao.getInstance().find(sql, Record.class, rowguid);
    }

    public List<Record> getYcProjectList(int pageNumber, int pageSize)
    {
      String sql = "select b.task_id,b.taskname,b.ouname,b.areacode,a.projectname,a.applyername,a.applydate,a.rowguid from audit_project a join audit_task b on a.taskguid = b.rowguid ";
      sql = sql + "where (a.centerguid='' OR a.centerguid IS NULL) and b.task_id is not null and IFNULL(b.IS_HISTORY,0)= 0 and b.is_enable = 1  and b.IS_EDITAFTERIMPORT=1 order by applydate desc";
      return CommonDao.getInstance().findList(sql, pageNumber, pageSize, Record.class);
    }

    public int getYcProjectCount()
    {
      String sql = "select count(1) from audit_project a join audit_task b on a.taskguid = b.rowguid ";
      sql = sql + "where (a.centerguid='' OR a.centerguid IS NULL) and b.task_id is not null and IFNULL(b.IS_HISTORY,0)= 0 and b.is_enable = 1  and b.IS_EDITAFTERIMPORT=1 order by applydate desc";
      return CommonDao.getInstance().find(sql, Integer.class).intValue();
    }

    public AuditSpSpSgxk getSgxkProjectBySubappGuid(String subappguid)
    {
      String sql = "select * from audit_sp_sp_sgxk where subappguid = '" + subappguid + "'";
      return CommonDao.getInstance().find(sql, AuditSpSpSgxk.class);
    }

    public List<FrameOu> getOuListByAreacode(String areacode) {
      String sql = "select distinct ouguid from frame_ou where OUCODE like '%" + areacode + "%' " + 
        " and (ISNULL(tel)=0 and LENGTH(trim(tel))>0) GROUP BY OUGUID ORDER BY ORDERNUMBER desc";
      return CommonDao.getInstance().findList(sql, FrameOu.class, areacode );
    }


}
