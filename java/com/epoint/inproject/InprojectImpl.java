package com.epoint.inproject;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

@Service
@Component
public class InprojectImpl implements InprojectService
{

    @Override
    public List<Record> findList(String startDate, String endDate) {
        String sql = "SELECT OUNAME,count(CASE when TASKTYPE='1' then 1 end )as JBACCEPTNUM,"
                + " count(CASE when TASKTYPE='1' and BANJIEDATE is not null then 1 end )as JBBANJIENUM,"
                + " count(CASE when TASKTYPE='2' then 1 end )as CNACCEPTNUM,"
                + " count(CASE when TASKTYPE='2' and BANJIEDATE is not null then 1 end )as CNBANJIENUM,"
                + " count(CASE when 1=1 then 1 end ) as SUMACCEPT,"
                + " count(CASE when BANJIEDATE is not null then 1 end ) as SUMBANJIE,"
                + " count(CASE when sparetime like('%超时') then 1 end ) as CHAOSHINUM,"
                + " count(CASE when BANJIEDATE<PROMISEENDDATE then 1 end ) as TIQIANNUM,"
                + " count(CASE when BANJIEDATE<=PROMISEENDDATE then 1 end ) as ANQINNUM,"
                + " IFNULL(concat(ROUND(count(CASE when BANJIEDATE<PROMISEENDDATE then 1 end )/count(CASE when BANJIEDATE is not null then 1 end )*100,1),'%') ,0) as TIQIANLV,"
                + " IFNULL(concat(ROUND(count(CASE when BANJIEDATE<=PROMISEENDDATE then 1 end )/count(CASE when BANJIEDATE is not null then 1 end )*100,1),'%'),0) as ANQILV,"
                + " IFNULL(concat(ROUND(sum(case when BANJIEDATE>RECEIVEDATE then TIMESTAMPDIFF(hour,RECEIVEDATE,BANJIEDATE) end)/(SELECT count(*) from audit_project )*100,1),'%'),0) as AVG "
                + " from audit_project where 1=1 ";
        if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
            sql += " and DATE_FORMAT(APPLYDATE,'%Y-%m-%d')>='" + startDate + "'"
                    + " and DATE_FORMAT(APPLYDATE,'%Y-%m-%d')<='" + endDate + "'";
        }
        sql += "  GROUP BY ouguid";
        return CommonDao.getInstance().findList(sql, Record.class);
    }

}
