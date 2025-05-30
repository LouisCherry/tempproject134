package com.epoint.basic.auditorga.auditworkingday.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditorga.auditworkingday.domain.AuditOrgaWorkingDay;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.config.JdbcConstantValue;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public class JNAuditOrgaWorkingDayService {
    
    /**
     * 通用dao操作
     */
    private CommonDao commonDao;

    public JNAuditOrgaWorkingDayService() {
        commonDao = CommonDao.getInstance("orga");
    }
	/**
	 * 
	 * 获取某个中心的工作日设置 [功能详细描述]
	 * 
	 * @param CenterGuid
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public List<AuditOrgaWorkingDay> selectAll(String centerGuid) {
		List<AuditOrgaWorkingDay> list = null;
		if (StringUtil.isNotBlank(centerGuid)) {
			String sql = "select * from AUDIT_ORGA_WORKINGDAY where CenterGuid=?1";
			list = commonDao.findList(sql, AuditOrgaWorkingDay.class, centerGuid);
		}
		return list;
	}

	/**
	 * 
	 * 按年月获取某个中心的工作日设置 [功能详细描述]
	 * 
	 * @param year
	 * @param month
	 * @param centerGuid
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public List<AuditOrgaWorkingDay> selectByYearAndMonth(String year, String month, String centerGuid) {
		if (month.length() == 1){
			month = "0" + month;
		}
		String yearAndMonth = year + "-" + month;
		List<AuditOrgaWorkingDay> list = null;
		List<Object> params = new ArrayList<>();
		if (StringUtil.isNotBlank(centerGuid)) {
			String sql = "select * from AUDIT_ORGA_WORKINGDAY where CenterGuid=? and wdate like ? order by wdate asc";
			params.add(centerGuid);
			params.add(yearAndMonth.replace("\\", "\\\\").replace("%", "\\%") + "%");
			list = commonDao.findList(sql, AuditOrgaWorkingDay.class, params.toArray());
		}
		return list;
	}

	/**
	 * 
	 * 更新一条工作日设置 [功能详细描述]
	 * 
	 * @param auditWorkingDay
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void update(AuditOrgaWorkingDay auditWorkingDay) {
		commonDao.update(auditWorkingDay);
	}

	/**
	 * 
	 * 插入一条工作日数据 [功能详细描述]
	 * 
	 * @param auditWorkingDay
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void add(AuditOrgaWorkingDay auditWorkingDay) {
		commonDao.insert(auditWorkingDay);
	}

	/**
	 * 
	 * 删除一条工作日数据 [功能详细描述]
	 * 
	 * @param auditWorkingDay
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void delete(AuditOrgaWorkingDay auditWorkingDay) {
		commonDao.delete(auditWorkingDay);
	}

	/**
	 * 返回当前日期向后的7个工作日
	 * @param centerGuid  中心GUID
	 * @param ReferenceDate   当前日期
	 * @param OffsetDays  偏移日期
	 * @return
	 */
	public Date getWorkingDayWithOfficeSet(String centerGuid, Date ReferenceDate, int OffsetDays) {
		String Reference = EpointDateUtil.convertDate2String(ReferenceDate, EpointDateUtil.DATE_TIME_FORMAT);
		if (OffsetDays != 0) {
			String orderDirection = OffsetDays > 0 ? " asc " : " desc ";
			String compare = OffsetDays > 0 ? " > " : " < ";
			if (OffsetDays < 0) {
				OffsetDays = OffsetDays * -1 + 1;
			}
			List<Object> params= new ArrayList<>();
			String subsql = null;
			if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.ORACLE)){
				subsql = " and to_date(wdate,'YYYY- MM-DD HH24:MI:SS')  "+compare+" to_date(?,'YYYY- MM-DD HH24:MI:SS') ";
			}
			if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.MYSQL)){
				subsql = " and  DATE_FORMAT(wdate,'%Y-%m-%d %H:%i:%s') "+compare+" DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s') ";
			}
			if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.SQLSERVER)){
				subsql = " and CONVERT(varchar(100), cast( wdate as datetime),120)  "+compare+" CONVERT(varchar(100) ,cast(? as datetime ) ,120 ) ";
			}
			if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.DM)){
	                subsql = " and to_date(wdate,'YYYY- MM-DD HH24:MI:SS')  "+compare+" to_date(?,'YYYY- MM-DD HH24:MI:SS') ";
			}
			params.add(Reference);
			if (StringUtil.isNotBlank(centerGuid)) {
				subsql += " and centerGuid=? ";
				params.add(centerGuid);
			}
			String orastr = "select wdate  from AUDIT_ORGA_WORKINGDAY WHERE isworkingday = 1 " + subsql
					+ " order by wdate "+ orderDirection;
			List<Record> list = commonDao.findList(orastr, 0, OffsetDays, Record.class,params.toArray());
			commonDao.close();
            if (list.size() == OffsetDays) {
                String time = EpointDateUtil.convertDate2String(ReferenceDate, "HH:mm:ss");
                return EpointDateUtil
                        .convertString2DateAuto(list.get(OffsetDays - 1).get("wdate").toString() + " " + time);
            }
            else {
                return EpointDateUtil.convertString2DateAuto("1753-1-1");
            }
		}
		return ReferenceDate;
	}

	/**
	 * 获取两个时间段内的工作日
	 * 
	 * @param centerGuid
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public int GetWorkingDays_Between_From_To(String centerGuid, Date fromDate, Date toDate) {
		String beginTime = EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDate(fromDate), "yyyy-MM-dd");
		String endTime = EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDate(toDate), "yyyy-MM-dd");
		String subsql = null;
		List<Object> params = new ArrayList<>();
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.ORACLE)){
			subsql = " and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  >=to_date(?,'YYYY- MM-DD HH24:MI:SS') and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  <=to_date(?,'YYYY- MM-DD HH24:MI:SS')";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.MYSQL)){
			subsql = " and  DATE_FORMAT(wdate,'%Y-%m-%d %H:%i:%s')  >=DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s') and  DATE_FORMAT(wdate,'%Y-%m-%d %H:%i:%s')  <=DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s')";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.SQLSERVER)){
			subsql = " and CONVERT(varchar(100), cast( wdate as datetime),120)  >= CONVERT(varchar(100) ,cast(? as datetime ) ,120 ) and CONVERT(varchar(100), cast( wdate as datetime),120)  <= CONVERT(varchar(100) ,cast(? as datetime ) ,120 )";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.DM)){
		    subsql = " and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  >=to_date(?,'YYYY- MM-DD HH24:MI:SS') and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  <=to_date(?,'YYYY- MM-DD HH24:MI:SS')";
        }
		params.add(beginTime);
		params.add(endTime);
		if (StringUtil.isNotBlank(centerGuid)) {
			subsql += " and centerGuid=? ";
			params.add(centerGuid);
		}
		String orastr = "select Count(wdate) cnt  from AUDIT_ORGA_WORKINGDAY WHERE isworkingday = 1 " + subsql
				+ " order by wdate ";
		String days = commonDao.queryString(orastr,params.toArray());
		// commonDao.close();
		return Integer.parseInt(days);
	}

	/**
     * 获取两个时间段内的非工作日
     * 
     * @param centerGuid
     * @param fromDate
     * @param toDate
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int GetWeekendDays_Between_From_To(String centerGuid, Date fromDate, Date toDate) {
        String beginTime = EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDate(fromDate), "yyyy-MM-dd");
        String endTime = EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDate(toDate), "yyyy-MM-dd");
        String subsql = null;
        List<Object> list = new ArrayList<>();
        if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.ORACLE)){
            subsql = " and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  >=to_date(?,'YYYY- MM-DD HH24:MI:SS') and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  <=to_date(?,'YYYY- MM-DD HH24:MI:SS')";
        }
        if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.MYSQL)){
            subsql = " and  DATE_FORMAT(wdate,'%Y-%m-%d %H:%i:%s')  >=DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s') and  DATE_FORMAT(wdate,'%Y-%m-%d %H:%i:%s')  <=DATE_FORMAT(?,'%Y-%m-%d %H:%i:%s')";
        }
        if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.SQLSERVER)){
            subsql = " and CONVERT(varchar(100), cast( wdate as datetime),120)  >= CONVERT(varchar(100) ,cast(? as datetime ) ,120 ) and CONVERT(varchar(100), cast( wdate as datetime),120)  <= CONVERT(varchar(100) ,cast(? as datetime ) ,120 )";
        }
        if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.DM)){
            subsql = " and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  >=to_date(?,'YYYY- MM-DD HH24:MI:SS') and to_date(wdate,'YYYY-MM-DD HH24:MI:SS')  <=to_date(?,'YYYY- MM-DD HH24:MI:SS')";
        }
        list.add(beginTime);
        list.add(endTime);
        if (StringUtil.isNotBlank(centerGuid)) {
            subsql += " and centerGuid=? ";
            list.add(centerGuid);
        }
        String orastr = "select Count(wdate) cnt  from AUDIT_ORGA_WORKINGDAY WHERE isworkingday = 0 " + subsql
                + " order by wdate ";
        String days = commonDao.queryString(orastr,list.toArray());
        // commonDao.close();
        return Integer.parseInt(days);
    }
    
	/**
	 * 判断该日期是否是工作日
	 * 
	 * @param centerGuid
	 * @param KQDate
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public Boolean isWorkingDay(String centerGuid, String KQDate) {
		String subsql = "";
		List<Object> params= new ArrayList<>();
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.ORACLE)){
			subsql = " and to_date(wdate,'YYYY-MM-DD')=to_date(?,'YYYY-MM-DD')";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.MYSQL)){
			subsql = " and  DATE_FORMAT(wdate,'%Y-%m-%d')=DATE_FORMAT(?,'%Y-%m-%d')";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.SQLSERVER)){
			subsql = " and CONVERT(varchar(100), cast( wdate as datetime),23) = CONVERT(varchar(100) ,cast(? as datetime ) ,23 )";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.DM)){
            subsql = " and to_date(wdate,'YYYY-MM-DD')=to_date(?,'YYYY-MM-DD')";
        }
		params.add(KQDate);
		if (StringUtil.isNotBlank(centerGuid)) {
			subsql += " and centerGuid=? ";
			params.add(centerGuid);
		}
		String orastr = "select Count(wdate) cnt  from AUDIT_ORGA_WORKINGDAY WHERE isworkingday = 1 " + subsql
				+ " order by wdate ";
		String days = commonDao.queryString(orastr,params.toArray());
		if (Integer.parseInt(days) > 0) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 根据年份删除对应的工作日记录
	 * 
	 * @param CenterGuid
	 * @param BeginYear
	 * @param EndYear
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void Delete_WORKINGDAY_ByYear(String CenterGuid, String BeginYear, String EndYear) {
		String sql = "DELETE FROM AUDIT_ORGA_WORKINGDAY WHERE 1=1";
		String subsql = "";
		List<Object> params= new ArrayList<>();
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.ORACLE)){
			subsql = " AND extract(year from wdate)>=? and extract(year from wdate)<=? ";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.MYSQL)){
			subsql = " AND year(wdate)>=? and year(wdate)<=? ";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.SQLSERVER)){
			subsql = " AND DATEPART(YEAR,wdate)>=? and DATEPART(YEAR,wdate)<=? ";
		}
		if (commonDao.getDataSource().getDatabase().equalsIgnoreCase(JdbcConstantValue.DM)){
            subsql = " AND extract(year from wdate)>=? and extract(year from wdate)<=? ";
        }
		params.add(BeginYear);
		params.add(EndYear);
		if (StringUtil.isNotBlank(CenterGuid)) {
			subsql += " and CenterGuid=? ";
			params.add(CenterGuid);
		}
		commonDao.execute(sql + subsql,params.toArray());
	}

    public PageData<Record> getCenterPageData(Map<String, String> map, int first, int pageSize, String sortField,String sortOrder) {
        PageData<Record> pageData = new PageData<Record>();
        SQLManageUtil sqlutil = new SQLManageUtil("orga",true);
        String exsql = sqlutil.buildSql(map);
        String sql = "select maxdate,mindate,CENTERNAME,c.xiaquname xiaquname,a.RowGuid centerguid from audit_orga_servicecenter a "
                + "JOIN (select MAX(WDATE) maxdate,MIN(wdate) mindate,centerguid from audit_orga_workingday GROUP BY CENTERGUID) b  on a.RowGuid=b.CENTERGUID"
                + " join audit_orga_area c on a.BELONGXIAQU=c.XiaQuCode"+exsql;
        String sqlcount = sql.replace("maxdate,mindate,CENTERNAME,c.xiaquname xiaquname,a.RowGuid centerguid", "count(1)");
        pageData.setList(commonDao.findList(sql, first, pageSize,Record.class));
        pageData.setRowCount(commonDao.queryInt(sqlcount));
        return pageData;
    }
    
    /**
     *  获取中心最迟工作日时间
     *  @param centerguid
     *  @return    
     */
    public String getMaxWdateByCenterguid(String centerguid){
        String sql ="select max(wdate) from AUDIT_ORGA_WORKINGDAY where CenterGuid=?1";
        return commonDao.queryString(sql, centerguid);
    }

    public void deleteAllByCenter(String centernow) {
        String sql = "delete from audit_orga_workingday where centerguid =?";
        commonDao.execute(sql, centernow);
    }

    public List<AuditOrgaWorkingDay> selectAll(String centerGuid, Map<String, String> conditionmap) {
        SQLManageUtil sqlutil = new SQLManageUtil("orga",true);
        String sqlexte = sqlutil.buildSql(conditionmap);
        sqlexte = sqlexte.replace("where", "and");
        List<AuditOrgaWorkingDay> list = null;
        if (StringUtil.isNotBlank(centerGuid)) {
            String sql = "select * from AUDIT_ORGA_WORKINGDAY where CenterGuid=?1"+sqlexte;
            list = commonDao.findList(sql, AuditOrgaWorkingDay.class, centerGuid);
        }
        return list;
    }
}
