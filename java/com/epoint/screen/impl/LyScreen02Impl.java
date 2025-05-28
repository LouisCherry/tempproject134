package com.epoint.screen.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.screen.api.ILyScreen02;

@Component
@Service
public class LyScreen02Impl implements ILyScreen02
{

    @Override
    public AuditCommonResult<Integer> getAllCountByType(String starttime, String endtime, Integer type) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        String sql = "select sum(count) sum from (SELECT count(1) count FROM AUDIT_RS_COMPANY_REGISTER WHERE IFNULL(IS_HISTORY, 0) = 0  ";
        if (10 == type) {
            sql += " AND EnterpriseProperty != '个体' ";
        }
        else if (20 == type) {
            sql += "AND EnterpriseProperty = '个体' ";
        }
        sql += datesql + "AND INDUSTRYTYPE IS NOT NULL AND INDUSTRYTYPE != '' GROUP BY INDUSTRYTYPE)t";
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getEnterpriseIndustrytype(String starttime, String endtime, String limit) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        List<Record> list = new ArrayList<Record>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        String sql = "select INDUSTRYTYPE,count(1) count from AUDIT_RS_COMPANY_REGISTER "
                + " where IFNULL(IS_HISTORY,0)=0 and EnterpriseProperty != '个体' " + datesql
                + "and INDUSTRYTYPE is not null and INDUSTRYTYPE != '' GROUP BY INDUSTRYTYPE ORDER BY 2 desc limit "
                + limit;
        try {
            list = CommonDao.getInstance().findList(sql, Record.class);
            result.setResult(list);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getindividualType(String starttime, String endtime, String limit) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        List<Record> list = new ArrayList<Record>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        String sql = "select INDUSTRYTYPE,count(1) count from AUDIT_RS_COMPANY_REGISTER "
                + " where IFNULL(IS_HISTORY,0)=0 and EnterpriseProperty = '个体' " + datesql
                + "and INDUSTRYTYPE is not null and INDUSTRYTYPE != '' GROUP BY INDUSTRYTYPE ORDER BY 2 desc limit "
                + limit;
        try {
            list = CommonDao.getInstance().findList(sql, Record.class);
            result.setResult(list);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getSetupCountByType(String starttime, String endtime, Integer type, String town) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        String sql = "";
        if (10 == type) {
            sql = "select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY , 0)=0 and EnterpriseProperty != '个体' ";
        }
        else if (20 == type) {
            sql = "select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY , 0)=0 and EnterpriseProperty = '个体' ";
        }
        if(!town.equals("其他")){
        	sql = sql + datesql + " and BUSINESSADDRESS REGEXP  '" + town + "'";
        }else{
        	sql = sql + datesql;
        }
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAgeGroupNum(String starttime, String endtime, Integer sex, String agerange) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and getnotime  >= '" + starttime + "' and getnotime  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and getnotime  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and getnotime  >= '" + starttime + "' ";
        }
        String sql = "select COUNT(1) from AUDIT_QUEUE_HISTORY where 1=1 " + datesql;
        if (1 == sex) {
            sql += " and SUBSTR(IDENTITYCARDNUM,17,4)&1";
            if ("1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(IDENTITYCARDNUM,7,4) < '" + agerange + "' ";
            }
            else if (!"1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(IDENTITYCARDNUM,7,4) like '" + agerange + "%' ";
            }
        }
        else {
            sql += " and SUBSTR(IDENTITYCARDNUM,17,1)=( SUBSTR(IDENTITYCARDNUM,17,1)>>1)<<1";
            if ("1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(IDENTITYCARDNUM,7,4) < '" + agerange + "' ";
            }
            else if (!"1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(IDENTITYCARDNUM,7,4) like '" + agerange + "%' ";
            }
        }
        
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<Integer> getJDAgeGroupNum(String starttime, String endtime, Integer sex, String agerange) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and applydate  >= '" + starttime + "' and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and applydate  >= '" + starttime + "' ";
        }
        String sql = "select COUNT(1) from AUDIT_project where 1=1 " + datesql;
        if (1 == sex) {
            sql += " and SUBSTR(certnum,17,4)&1";
            if ("1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(certnum,7,4) < '" + agerange + "' ";
            }
            else if (!"1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(certnum,7,4) like '" + agerange + "%' ";
            }
        }
        else {
            sql += " and SUBSTR(certnum,17,1)=( SUBSTR(certnum,17,1)>>1)<<1";
            if ("1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(certnum,7,4) < '" + agerange + "' ";
            }
            else if (!"1950".equals(agerange) && StringUtil.isNotBlank(agerange)) {
                sql += " and SUBSTR(certnum,7,4) like '" + agerange + "%' ";
            }
        }
        
        sql += "and `STATUS` not in ('8','10','20','22','24','14','28') and applyertype = '20' and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
        
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Double> getRegisteredcapital(String starttime, String endtime, Integer type) {
        AuditCommonResult<Double> result = new AuditCommonResult<Double>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '2017-12-12' and REGISTERDATE <= '" + endtime + "' ";
            //datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        else {
            datesql = " and REGISTERDATE >= '2017-12-12' ";
        }
        String sql = "";
        if (10 == type) {
            sql = "select sum(REGISTEREDCAPITAL) sum from AUDIT_RS_COMPANY_REGISTER where (IS_HISTORY is null or  IS_HISTORY = 0) and EnterpriseProperty != '个体' ";
        }
        else if (20 == type) {
            sql = "select sum(REGISTEREDCAPITAL) sum from AUDIT_RS_COMPANY_REGISTER where (IS_HISTORY is null or  IS_HISTORY = 0) and EnterpriseProperty = '个体' ";
        }
        sql = sql + datesql + " and INDUSTRYTYPE is not null and INDUSTRYTYPE != ''";
        try {
            Record record = CommonDao.getInstance().find(sql, Record.class);
            Double numb = 0.0;
            if (StringUtil.isNotBlank(record.get("sum"))) {
                numb = record.get("sum");
            }
            result.setResult(numb);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<Double> getRegisteredcapitalIsBelong(String starttime, String endtime, Integer type) {
        AuditCommonResult<Double> result = new AuditCommonResult<Double>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and r.REGISTERDATE >= '" + starttime + "' and r.REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and r.REGISTERDATE >= '2017-12-12' and r.REGISTERDATE <= '" + endtime + "' ";
            //datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and r.REGISTERDATE >= '" + starttime + "' ";
        }
        else {
            datesql = " and r.REGISTERDATE >= '2017-12-12' ";
        }
        String sql = "";
        sql = "select sum(r.REGISTEREDCAPITAL) sum from audit_rs_company_register r ,audit_rs_company_baseinfo b where r.COMPANYID = b.COMPANYID and (r.IS_HISTORY is null or  r.IS_HISTORY = 0)  ";
        if (1 == type) {
            //合肥
            sql += " and (b.ORGALEGAL_IDNUMBER like '34%')";
        }
        else if (2 == type) {
            //北上广深
            sql += " and (b.ORGALEGAL_IDNUMBER like '11%' or b.ORGALEGAL_IDNUMBER like '31%' or b.ORGALEGAL_IDNUMBER like '4401%' or b.ORGALEGAL_IDNUMBER like '4401%')";
        }
        sql = sql + datesql + " and r.INDUSTRYTYPE is not null and r.INDUSTRYTYPE != ''";
        try {
            Record record = CommonDao.getInstance().find(sql, Record.class);
            Double numb = 0.0;
            if (StringUtil.isNotBlank(record.get("sum"))) {
                numb = record.get("sum");
            }
            result.setResult(numb);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getSetupSumByType(String starttime, String endtime, Integer type) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and REGISTERDATE <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and REGISTERDATE >= '" + starttime + "' ";
        }
        String sql = "";
        if (10 == type) {
            sql = "select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY , 0)=0 and EnterpriseProperty != '个体' ";
        }
        else if (20 == type) {
            sql = "select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY , 0)=0 and EnterpriseProperty = '个体' ";
        }
        sql = sql + datesql;
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getSignCount(String starttime, String endtime) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String datesql = "";
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and applydate  >= '" + starttime + "' and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            datesql = " and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            datesql = " and applydate  >= '" + starttime + "' ";
        }
        String sql = "Select count(1) from AUDIT_project where status = '90' and task_id= '36e467f6-d31f-44cc-a74c-fe3b1b2427b1' "
                + datesql;
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public List<Record> getCountDaysParams(String starttime, String endtime) {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT p.rowguid,p.PROMISEENDDATE,p.BANJIEDATE,p.PROMISE_DAY,p.ACCEPTUSERDATE FROM audit_task t,audit_project p WHERE t.RowGuid = p.TASKGUID AND t.TYPE = '2' AND p.`STATUS` = '90'";
        /*if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
            sql = sql + " AND cast(p.banjiedate as datetime) between cast('"+starttime+"' as datetime) and cast('"+endtime+"' as datetime);";
        }*/
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and p.banjiedate  >= '" + starttime + "' and p.banjiedate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and p.banjiedate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            sql += " and p.banjiedate  >= '" + starttime + "' ";
        }
        records = CommonDao.getInstance().findList(sql, Record.class);
        return records;
    }

    @Override
    public Map<String, Integer> getEvaluatProjectCount(String starttime, String endtime) {
        CommonDao dao = CommonDao.getInstance();
        Map<String, Integer> map = new HashMap<>();
        try {
            String sql1 = "select COUNT(1) from (select evaluatedate,satisfied from audit_online_evaluat limit 10000) e where 1=1 and e.satisfied = '2'";
            String sql2 = "select COUNT(1) from (select evaluatedate,satisfied from audit_online_evaluat limit 10000) e where 1=1 and e.satisfied = '4'";
            String sql3 = "select COUNT(1) from (select evaluatedate,satisfied from audit_online_evaluat limit 10000) e where 1=1 and e.satisfied in ('2','4') ";
            /* if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
                String sql4 = " AND evaluatedate between '"+starttime+"' and '"+endtime+"';";
                sql1 += sql4;
                sql2 += sql4;
                sql3 += sql4;
            }*/
            String sql4 = "";
            if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
                sql4 = " and evaluatedate  >= '" + starttime + "' and evaluatedate  <= '" + endtime + "' ";
            }
            else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
                sql4 = " and evaluatedate  <= '" + endtime + "' ";
            }
            else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
                sql4 = " and evaluatedate  >= '" + starttime + "' ";
            }
            sql1 += sql4;
            sql2 += sql4;
            sql3 += sql4;

            int satisfied = dao.queryInt(sql1, new Object[] {});
            map.put("satisfied", satisfied);
            int unsatisfied = dao.queryInt(sql2, new Object[] {});
            map.put("unsatisfied", unsatisfied);
            int all = dao.queryInt(sql3, new Object[] {});
            map.put("all", all);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dao.close();
        }
        return map;
    }

    @Override
    public AuditCommonResult<Integer> getOlineRegister(String starttime, String endtime) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select COUNT(1) from audit_online_register where 1 = 1";
        /*if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
            sql += " AND operatedate between '"+starttime+"' and '"+endtime+"' ";
        }*/
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and operatedate  >= '" + starttime + "' and operatedate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and operatedate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            sql += " and operatedate  >= '" + starttime + "' ";
        }
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOlineProject(String starttime, String endtime) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select COUNT(1) from audit_online_project where 1=1";
        /*if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
            sql += " AND applydate between '"+starttime+"' and '"+endtime+"' ";
        }*/
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and applydate  >= '" + starttime + "' and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and applydate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            sql += " and applydate  >= '" + starttime + "' ";
        }
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOlineQueue(String starttime, String endtime) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select COUNT(1) from audit_queue_appointment where 1=1";
        /*if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
            sql += " AND createdate between '"+starttime+"' and '"+endtime+"' ";
        }*/
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and createdate  >= '" + starttime + "' and createdate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and createdate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            sql += " and createdate  >= '" + starttime + "' ";
        }
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getOlineConsult(String starttime, String endtime) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select COUNT(1) from audit_online_consult where CONSULTTYPE = '1'";
        /*if (starttime != null && !starttime.isEmpty() && endtime != null && !endtime.isEmpty()) {
            sql += " AND askdate between '"+starttime+"' and '"+endtime+"' ";
        }*/
        if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and askdate  >= '" + starttime + "' and askdate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
            sql += " and askdate  <= '" + endtime + "' ";
        }
        else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
            sql += " and askdate  >= '" + starttime + "' ";
        }
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, Integer>> getProjectCountByType(String starttime, String endtime) {
        AuditCommonResult<Map<String, Integer>> result = new AuditCommonResult<Map<String, Integer>>();
        CommonDao dao = CommonDao.getInstance();
        Map<String, Integer> map = new HashMap<>();
        try {
            String sql1 = "select COUNT(1) from audit_project where APPLYWAY = '20' and status >=30";
            String sql2 = "select COUNT(1) from audit_project where APPLYWAY <> '20' and status >=30";
            String sql3 = "select COUNT(1) from audit_project where TASKTYPE = '1' and status >=30";
            String sql4 = "select COUNT(1) from audit_project where TASKTYPE = '2' and status >=30";

            String sql5 = "";
            if (StringUtil.isNotBlank(starttime) && StringUtil.isNotBlank(endtime)) {
                sql5 = " and applydate  >= '" + starttime + "' and applydate  <= '" + endtime + "' ";

            }
            else if (StringUtil.isBlank(starttime) && StringUtil.isNotBlank(endtime)) {
                sql5 = " and applydate  <= '" + endtime + "' ";

            }
            else if (StringUtil.isNotBlank(starttime) && StringUtil.isBlank(endtime)) {
                sql5 = " and applydate  >= '" + starttime + "' ";
            }

            sql1 += sql5;
            sql2 += sql5;
            sql3 += sql5;
            sql4 += sql5;
            int xianxia = dao.queryInt(sql1, new Object[] {});
            map.put("xianxia", xianxia);
            int online = dao.queryInt(sql2, new Object[] {});
            map.put("online", online);
            int jiban = dao.queryInt(sql3, new Object[] {});
            map.put("jiban", jiban);
            int chengnuo = dao.queryInt(sql4, new Object[] {});
            map.put("chengnuo", chengnuo);
            result.setResult(map);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<Map<String, List<Record>>> getTaskOuTypeByType() {
        AuditCommonResult<Map<String, List<Record>>> result = new AuditCommonResult<Map<String, List<Record>>>();
        CommonDao dao = CommonDao.getInstance();
        Map<String, List<Record>> map = new HashMap<>();
        try {
            String sql1 = "(select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '1') and type = '1' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0) UNION (select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '2') and type = '1' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0) UNION (select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '3') and type = '1' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0)";
            String sql2 = "(select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '1') and type = '2' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0) UNION (select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '2') and type = '2' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0) UNION (select COUNT(1) count from audit_task where OUGUID in(select OUGUID from frame_ou where isjd = '3') and type = '2' and ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0)";
            List<Record> jiban = dao.findList(sql1, Record.class, new Object[] {});
            map.put("jiban", jiban);
            List<Record> chengnuo = dao.findList(sql2, Record.class, new Object[] {});
            map.put("chengnuo", chengnuo);
            result.setResult(map);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }


    @Override
    public AuditCommonResult<Integer> getCompanyCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select count(1) from audit_rs_company_baseinfo where is_history = '0'";
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getIndividualCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        String sql = "select count(1) from audit_rs_individual_baseinfo where is_history = '0'";
        try {
            Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {});
            if (count == null) {
                count = 0;
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTaskCountByType(String shenpilb1,String shenpilb2,String shenpilb3) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        CommonDao dao = CommonDao.getInstance();
        try {
            String sql = "SELECT a.isjd, IFNULL(b.count, 0) count FROM ( SELECT isjd FROM frame_ou WHERE isjd IS NOT NULL and isjd != '' GROUP BY isjd ) a LEFT JOIN ( SELECT COUNT(t.rowguid) count, isjd FROM audit_task t, ( SELECT ouguid, isjd FROM frame_ou WHERE isjd IS NOT NULL and isjd != '' ) f WHERE ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0 AND ( shenpilb = ?1 OR shenpilb LIKE '"+shenpilb3+"%' ) AND t.OUGUID = f.ouguid GROUP BY f.isjd ) b ON a.isjd = b.isjd;";
            if (StringUtil.isNotBlank(shenpilb2)) {
                sql = "SELECT a.isjd, IFNULL(b.count, 0) count FROM ( SELECT isjd FROM frame_ou WHERE isjd IS NOT NULL and isjd != '' GROUP BY isjd ) a LEFT JOIN ( SELECT COUNT(t.rowguid) count, isjd FROM audit_task t, ( SELECT ouguid, isjd FROM frame_ou WHERE isjd IS NOT NULL and isjd != '' ) f WHERE ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0 AND ( shenpilb in(?1,?2) OR shenpilb LIKE '"+shenpilb3+"%' ) AND t.OUGUID = f.ouguid GROUP BY f.isjd ) b ON a.isjd = b.isjd;";
            }
            List<Record> findList = dao.findList(sql, Record.class, new Object[]{shenpilb1,shenpilb2});
            result.setResult(findList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getOuCount() {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        CommonDao dao = CommonDao.getInstance();
        try {

            String sql = "SELECT isjd,COUNT(isjd) count FROM frame_ou WHERE OUGUID IN ( SELECT DISTINCT (OUGUID) FROM audit_task WHERE ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0 ) and isjd is not null and isjd != '' GROUP BY isjd;";
            List<Record> findList = dao.findList(sql, Record.class, new Object[]{});
            result.setResult(findList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTaskCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        CommonDao dao = CommonDao.getInstance();
        try {

            String sql = "select count(1) from audit_task where ( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1 and ISTEMPLATE=0";
            int count = dao.queryInt(sql, new Object[] {});
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRiskPointCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        CommonDao dao = CommonDao.getInstance();
        try {

            String sql = "select count(1) from audit_task_riskpoint";
            int count = dao.queryInt(sql, new Object[] {});
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getMaterialCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        CommonDao dao = CommonDao.getInstance();
        try {

            String sql = "SELECT count(1) FROM frame_attachinfo f,(SELECT m.TEMPLATEATTACHGUID,m.EXAMPLEATTACHGUID,m.FORMATTACHGUID FROM audit_task_material m, (SELECT RowGuid FROM audit_task WHERE (IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0) pr WHERE m.TASKGUID = pr.rowguid ) t WHERE f.CLIENGGUID = t.TEMPLATEATTACHGUID or f.CLIENGGUID = t.EXAMPLEATTACHGUID or f.CLIENGGUID = t.FORMATTACHGUID";
            int count = dao.queryInt(sql, new Object[] {});
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTaskCountByWebLevel(int level) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        CommonDao dao = CommonDao.getInstance();
        int count = 0;
        try {
            String sql = "select count(1) from audit_task t,audit_task_extension e where t.RowGuid = e.TASKGUID and ( t.IS_HISTORY=0 or t.IS_HISTORY is null) and t.IS_EDITAFTERIMPORT=1  and t.IS_ENABLE=1 and t.ISTEMPLATE=0 ";
            if (level != 0) {
                sql = sql + " and e.weblevel >= ?1";
                count = dao.queryInt(sql, new Object[] {level });
            }
            else {
                count = dao.queryInt(sql, new Object[] {});
            }
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<List<Record>> getTaskCountByWebLevelAndOUtype(int level) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<>();
        CommonDao dao = CommonDao.getInstance();
        try {
            String sql = "(SELECT count(1) count FROM audit_task t, audit_task_extension e WHERE t.RowGuid = e.TASKGUID AND ( t.IS_HISTORY = 0 OR t.IS_HISTORY IS NULL ) AND t.IS_EDITAFTERIMPORT = 1 AND t.IS_ENABLE = 1 AND t.ISTEMPLATE = 0 and e.weblevel >= ?1 and OUGUID in(select ouguid from frame_ou where isjd = 1)) UNION (SELECT count(1) count FROM audit_task t, audit_task_extension e WHERE t.RowGuid = e.TASKGUID AND ( t.IS_HISTORY = 0 OR t.IS_HISTORY IS NULL ) AND t.IS_EDITAFTERIMPORT = 1 AND t.IS_ENABLE = 1 AND t.ISTEMPLATE = 0 and e.weblevel >= ?1 and OUGUID in(select ouguid from frame_ou where isjd = 2)) UNION (SELECT count(1) count FROM audit_task t, audit_task_extension e WHERE t.RowGuid = e.TASKGUID AND ( t.IS_HISTORY = 0 OR t.IS_HISTORY IS NULL ) AND t.IS_EDITAFTERIMPORT = 1 AND t.IS_ENABLE = 1 AND t.ISTEMPLATE = 0 and e.weblevel >= ?1 and OUGUID in(select ouguid from frame_ou where isjd = 3))";
            List<Record> resultlist = dao.findList(sql, Record.class, new Object[]{level});
            result.setResult(resultlist);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWeekProjectTimeAndCount(String time1, String time2, String applyway,String isjd) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        int count = 0;
        CommonDao dao = CommonDao.getInstance();
        String sql = "";
        if("20".equals(applyway)){            
            sql = "select count(1) from audit_project where APPLYWAY = 20 and applydate >= ?1  and applydate< ?2";
        }else{
            sql = "select count(1) from audit_project where APPLYWAY != 20 and applydate >= ?1  and applydate< ?2";
        }
        
        if(isjd.equals("true")){
            sql += " and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
        }else if(isjd.equals("false")){
            sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
        }
        
        try {
            count = dao.queryInt(sql, new Object[] { time1, time2 });
            result.setResult(count);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        finally {
            dao.close();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWeChartCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        CommonDao dao = CommonDao.getInstance();
        String sql = "select wechartcount from wechart";
        int count = dao.queryInt(sql, new Object[] {});
        result.setResult(count);
        return result;
    }


}
