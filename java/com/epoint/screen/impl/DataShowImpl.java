package com.epoint.screen.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.screen.api.ILyDataShow;

@Component
@Service
public class DataShowImpl implements ILyDataShow {

	@Override
	public List<Integer[]> getRenliuliangNum(String sql) {
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[7];
			for (int i = 0; i < 7; i++) {
				String reqSql = " select count(1) from ( select date_format(q.getnotime,'%w') as code from audit_queue_history q where 1=1 "
						+ sql + " ) as n " + "where code = '" + i  + "' ";
				List<Integer> list = new CommonDao().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
		} catch (Exception e) {
			e.getMessage();
		}
		return oblist;
	}

	@Override
	public Integer getEvaluationNum(String satisfied) {
		int numb = 0;
		try {
			String sql = " select count(1) from AUDIT_ONLINE_EVALUAT where 1=1 ";
			if (StringUtil.isNotBlank(satisfied)) {
				sql += " and satisfied = '" + satisfied + "' ";
			}
			numb = new CommonDao().find(sql, Integer.class);
		} catch (Exception e) {
			e.getMessage();
		}
		return numb;
	}
	
	@Override
    public Integer getEvaluationNumByTime(String satisfied, String startdate, String enddate) {
        int numb = 0;
        try {
            String sql = " select count(1) from AUDIT_ONLINE_EVALUAT where 1=1 and clienttype = 10 ";
            if (StringUtil.isNotBlank(satisfied)) {
                sql += " and satisfied = '" + satisfied + "' ";
            }
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and evaluatedate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and evaluatedate <= '" + enddate + "'";
            }
            numb = new CommonDao().find(sql, Integer.class);
        } catch (Exception e) {
            e.getMessage();
        }
        return numb;
    }

	@Override
	public AuditCommonResult<List<AuditOrgaDepartment>> getCenterOuList(String centerguid) {
		AuditCommonResult<List<AuditOrgaDepartment>> result = new AuditCommonResult<List<AuditOrgaDepartment>>();
		try {
			String sql = " select * from audit_orga_department where centerguid = '" + centerguid + "' ";

			List<AuditOrgaDepartment> returnList = new CommonDao().findList(sql, AuditOrgaDepartment.class);
			result.setResult(returnList);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	@Override
	public List<Integer[]> getNianlingduanNum(String sql) {
		List<Integer[]> nllist = new ArrayList<Integer[]>();
		String[] str = { "1940", "194", "195", "196", "197", "198", "199", "200", "201" };
		String reqSql = "";
		try {
			Integer[] ob = new Integer[9];
			for (int i = 0; i < 9; i++) {
				if (i == 0) {
					reqSql = "select COUNT(1) from AUDIT_QUEUE_HISTORY where SUBSTR(IDENTITYCARDNUM,7,4) < '" + str[i] + "'" + sql;
				} else {
					reqSql = "select COUNT(1) from AUDIT_QUEUE_HISTORY where SUBSTR(IDENTITYCARDNUM,7,4) like '" + str[i] + "%'" + sql;
				}
				List<Integer> list = new CommonDao().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			nllist.add(ob);
		} catch (Exception e) {
			e.getMessage();
		}
		return nllist;
	}

	@Override
	public AuditCommonResult<List<Integer[]>> getQuhaoNum(String sql, List<String> monthlist) {
		AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[monthlist.size()];
			for (int i = 0; i < monthlist.size(); i++) {
				String reqSql = " select count(1) from audit_queue_history where 1=1 and getnotime like '" + monthlist.get(i) + "%' " + sql;
				List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	@Override
	public AuditCommonResult<List<Integer[]>> getBanjianNum(String sql, List<String> monthlist) {
		AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[monthlist.size()];
			for (int i = 0; i < monthlist.size(); i++) {
				String reqSql = " select count(1) from audit_project where 1=1 and applydate like '" + monthlist.get(i) + "%' " + sql;
				List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	@Override
	public AuditCommonResult<List<Integer[]>> getBanjieNum(String sql, List<String> monthlist) {
		AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[monthlist.size()];
			for (int i = 0; i < monthlist.size(); i++) {
				String reqSql = " select count(1) from audit_project where 1=1 and banjiedate like '" + monthlist.get(i) + "%' " + sql;
				List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}

	@Override
	public AuditCommonResult<List<Integer[]>> getQuhaoNumByDate(String sql, List<String> dateList) {
		AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[dateList.size()];
			for (int i = 0; i < dateList.size(); i++) {
				String reqSql = "";
				if (EpointDateUtil.compareDateOnDay(EpointDateUtil.convertString2Date(dateList.get(i), "yyyy-MM-dd"),
						EpointDateUtil.convertString2Date(EpointDateUtil.convertDate2String(new Date()), "yyyy-MM-dd")) == 0) {
					reqSql = " select count(1) from audit_queue where 1=1 and getnotime like '" + dateList.get(i) + "%' " + sql;
				} else {
					reqSql = " select count(1) from audit_queue_history where 1=1 and getnotime like '" + dateList.get(i) + "%' " + sql;
				}
				List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}

		return result;
	}
	
	   @Override
	    public AuditCommonResult<List<Integer[]>> getVisNumByDate(String sql, List<String> dateList) {
	        AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
	        List<Integer[]> oblist = new ArrayList<Integer[]>();
	        try {
	            Integer[] ob = new Integer[dateList.size()];
	            for (int i = 0; i < dateList.size(); i++) {
	                String reqSql = "";
	                if (EpointDateUtil.compareDateOnDay(EpointDateUtil.convertString2Date(dateList.get(i), "yyyy-MM-dd"),
	                        EpointDateUtil.convertString2Date(EpointDateUtil.convertDate2String(new Date()), "yyyy-MM-dd")) == 0) {
	                    reqSql = " select count(1) from audit_visitcount where 1=1 and visitdate like '" + dateList.get(i) + "%' " + sql;
	                } else {
	                    reqSql = " select count(1) from audit_visitcount where 1=1 and visitdate like '" + dateList.get(i) + "%' " + sql;
	                }
	                List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
	                ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
	            }
	            oblist.add(ob);
	            result.setResult(oblist);
	        } catch (Exception e) {
	            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
	        }

	        return result;
	    }

	@Override
	public AuditCommonResult<List<Integer[]>> getGenderNum(String sql) {
		AuditCommonResult<List<Integer[]>> result = new AuditCommonResult<List<Integer[]>>();
		List<Integer[]> oblist = new ArrayList<Integer[]>();
		try {
			Integer[] ob = new Integer[2];
			String reqSql = "";
			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					reqSql = " select count(1) from audit_queue_history where 1=1 and SUBSTR(IDENTITYCARDNUM, 16, 17)&1 " + sql;
				} else {
					reqSql = " select count(1) from audit_queue_history where 1=1 and SUBSTR(IDENTITYCARDNUM, 16, 17)=(SUBSTR(IDENTITYCARDNUM, 16, 17)>>1)<<1 " + sql;
				}
				List<Integer> list = CommonDao.getInstance().findList(reqSql, Integer.class);
				ob[i] = Integer.parseInt(String.valueOf(list.get(0)));
			}
			oblist.add(ob);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}

		return result;
	}

	@Override
	public AuditCommonResult<List<Record>> getWindowTop10() {
		AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
		List<Record> oblist = new ArrayList<Record>();
		try {
			String sql = "SELECT WINDOWGUID,COUNT(WINDOWGUID) count from audit_project where status >=30 GROUP BY WINDOWGUID ORDER BY COUNT(WINDOWGUID) desc LIMIT 10";
			oblist = CommonDao.getInstance().findList(sql, Record.class);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<List<Record>> getWindowTop10ByTime(String startdate, String enddate,String isjd) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        List<Record> oblist = new ArrayList<Record>();
        try {
            String sql = "SELECT WINDOWGUID,COUNT(WINDOWGUID) count from audit_project where status >=30 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            if(isjd.equals("true")){
                sql += "  and ouguid in(select ouguid from frame_ou where isjd = 3)";
            }else if(isjd.equals("false")){
                sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
            }
            sql += " GROUP BY WINDOWGUID ORDER BY COUNT(WINDOWGUID) desc LIMIT 10";
            oblist = CommonDao.getInstance().findList(sql, Record.class);
            result.setResult(oblist);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

	@Override
	public AuditCommonResult<Integer> getBanjianSumByDate(String date) {
		AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
		Integer numb = 0;
		try {
			String sql = " select count(1) from audit_project where 1=1 and status >=30 ";
			if (StringUtil.isNotBlank(date)) {
				sql += " and applydate like '" + date + "%' ";
			}
			numb = CommonDao.getInstance().find(sql, Integer.class);
			result.setResult(numb);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<Integer> getBanjianSumByTime(String date, String startdate, String enddate) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer numb = 0;
        try {
            String sql = " select count(1) from audit_project where 1=1 and status >=30 ";
            if (StringUtil.isNotBlank(date)) {
                sql += " and applydate like '" + date + "%' ";
            }
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            numb = CommonDao.getInstance().find(sql, Integer.class);
            result.setResult(numb);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

	@Override
	public AuditCommonResult<Integer> getBanjieSumByDate(String date) {
		AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
		Integer numb = 0;
		try {
			String sql = " select count(1) from audit_project where 1=1 ";
			if (StringUtil.isNotBlank(date)) {
				sql += " and banjiedate like '" + date + "%' ";
			} else {
				sql += " and banjiedate is not null ";
			}
			numb = CommonDao.getInstance().find(sql, Integer.class);
			result.setResult(numb);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<Integer> getBanjieSumByTime(String date, String startdate, String enddate) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        Integer numb = 0;
        try {
            String sql = " select count(1) from audit_project where 1=1 ";
            if (StringUtil.isNotBlank(date)) {
                sql += " and applydate like '" + date + "%' ";
            }
            sql += " and banjiedate is not null ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            numb = CommonDao.getInstance().find(sql, Integer.class);
            result.setResult(numb);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
	
	@Override
    public AuditCommonResult<Integer> getQueueCountByTime(String startdate, String enddate) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            String sql = "select count(1) from audit_queue where 1=1 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and getnotime >= '" + startdate + "' ";
            } 
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and getnotime <= '" + enddate + "' ";
            }
            Integer count = CommonDao.getInstance().queryInt(sql);
            sql = "select count(1) from audit_queue_history where 1=1 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and getnotime >= '" + startdate + "' ";
            } 
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and getnotime <= '" + enddate + "' ";
            }
            Integer countHis = CommonDao.getInstance().queryInt(sql);
            result.setResult(count + countHis);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

	@Override
	public AuditCommonResult<Integer> getQueueCountByDate() {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		try {
		    String sql = "select count(1) from audit_queue where 1=1 and " + 
	                " getnotime between DATE_SUB(curdate(),INTERVAL 0 DAY) and DATE_SUB(curdate(),INTERVAL -1 DAY) ";
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getProjectCountByDate() {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_queue where status = '1' and " + 
                " getnotime between DATE_SUB(curdate(),INTERVAL 0 DAY) and DATE_SUB(curdate(),INTERVAL -1 DAY) ";
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getWaitNumByDate() {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_queue where status = '0' and getnotime between " + 
                " DATE_SUB(curdate(),INTERVAL 0 DAY) and DATE_SUB(curdate(),INTERVAL -1 DAY) ";
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<List<Record>> getOuTop10() {
		AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
		List<Record> oblist = new ArrayList<Record>();
		try {
			String sql = " SELECT OUGUID,COUNT(OUGUID) count from audit_project where status >=30 GROUP BY OUGUID ORDER BY COUNT(OUGUID) desc LIMIT 10 ";
			oblist = CommonDao.getInstance().findList(sql, Record.class);
			result.setResult(oblist);
		} catch (Exception e) {
			result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<List<Record>> getOuTop10ByTime(String startdate, String enddate,String isjd) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        List<Record> oblist = new ArrayList<Record>();
        try {
            String sql = " SELECT OUGUID,COUNT(OUGUID) count from audit_project where status >=30 ";
            String sql1 = "";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            if(isjd.equals("true")){
                sql += "  and ouguid in(select ouguid from frame_ou where isjd = 2)";
                sql1 = " limit 11";
            }else if(isjd.equals("false")){
                sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
                sql1 = " limit 10";
            }
            sql += " GROUP BY OUGUID ORDER BY COUNT(OUGUID) desc"+sql1;
            oblist = CommonDao.getInstance().findList(sql, Record.class);
            result.setResult(oblist);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


	@Override
	public AuditCommonResult<Integer> getTaskCountByShenpilb(String shenpilb1,String shenpilb2,String shenpilb3) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_task where IS_EDITAFTERIMPORT = '1' and IFNULL(IS_HISTORY,0) = 0 and IS_ENABLE = '1' and  (shenpilb = ?1 or shenpilb like '"+shenpilb3+"%')";
		if (shenpilb2 == null) {
            sql = "select count(1) from audit_task where IS_EDITAFTERIMPORT = '1' and IFNULL(IS_HISTORY,0) = 0 and IS_ENABLE = '1'and (shenpilb in (?1,?2) or shenpilb like '"+shenpilb3+"%')";
        }
		try {
			Integer count = CommonDao.getInstance().queryInt(sql, new Object[] {shenpilb1,shenpilb2});
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getTaskCountByType(String type) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_task where IS_EDITAFTERIMPORT = '1' and IFNULL(IS_HISTORY,0) = 0 and IS_ENABLE = '1' and type = '" + type + "' ";
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getBanjianCountByMonth(String month,String isjd) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_project where status >= 30 and applydate like '" + month + "%' ";
		if(isjd.equals("true")){
            sql += " and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
        }else if(isjd.equals("false")){
            sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
        }
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<List<Record>> getBanjianTodayCountByHour() {
	    AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            String sql = "SELECT HOUR (applydate) AS HOUR, count(1) AS count FROM audit_project WHERE STATUS >= 30 AND applydate BETWEEN ?1 AND ?2 GROUP BY HOUR (applydate) ORDER BY HOUR (applydate);";
            Date today = new Date();
            String start = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 09:00:00");
            String end = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 16:30:00");
//            String start = "2019-01-03 9:00:00";
//            String end = "2019-01-03 16:30:00";
            CommonDao common = CommonDao.getInstance();
            List<Record> returnList = common.findList(sql, Record.class, new Object[]{start,end});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

	@Override
	public AuditCommonResult<List<AuditProject>> getHotTaskTop5() {
		AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
		List<AuditProject> list = new ArrayList<AuditProject>();
		try {
			String sql = " select task_id,projectname,COUNT(task_id) count from audit_project where status >=30 GROUP BY task_id ORDER BY COUNT(task_id) desc LIMIT 5 ";
			list = CommonDao.getInstance().findList(sql, AuditProject.class);
			result.setResult(list);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}

		return result;
	}
	
	@Override
    public AuditCommonResult<List<AuditProject>> getHotTaskTop5ByTime(String startdate, String enddate,String isjd) {
        AuditCommonResult<List<AuditProject>> result = new AuditCommonResult<List<AuditProject>>();
        List<AuditProject> list = new ArrayList<AuditProject>();
        try {
            String sql = " select task_id,projectname,COUNT(task_id) count from audit_project where status >=30 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            if(isjd.equals("true")){
                sql += " and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
            }else if(isjd.equals("false")){
                sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
            }
            sql += " GROUP BY task_id ORDER BY COUNT(task_id) desc LIMIT 5 ";
            list = CommonDao.getInstance().findList(sql, AuditProject.class);
            result.setResult(list);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }

        return result;
    }

	@Override
	public AuditCommonResult<Integer> getCompanyCountByEnterprisetype(String enterprisetype) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		try {
			String sql = " select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY,0) = 0 ";
			if (StringUtil.isNotBlank(enterprisetype)) {
				sql += " and enterprisetype = '" + enterprisetype + "' ";
			}

			int numb = CommonDao.getInstance().queryInt(sql, Integer.class);
			result.setResult(numb);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getCompanyCountByTown(String town) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		try {
			String sql = " select count(1) from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY,0) = 0 ";
			if (StringUtil.isNotBlank(town)) {
				sql += " and BUSINESSADDRESS like '%" + town + "%' ";
			}

			int numb = CommonDao.getInstance().queryInt(sql);
			result.setResult(numb);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
	@Override
	public AuditCommonResult<Integer> getCompanyCount() {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		try {
			String sql = " select count(1) from AUDIT_RS_COMPANY_BASEINFO where IFNULL(IS_HISTORY,0) = 0 ";
			
			int numb = CommonDao.getInstance().queryInt(sql);
			result.setResult(numb);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}

	@Override
	public AuditCommonResult<Integer> getAgeGroupNum(String age, String gender) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		try {
			String sql = " select COUNT(1) from AUDIT_QUEUE_HISTORY where 1=1 and LENGTH(IDENTITYCARDNUM) = 18 ";
			if ("1".equals(gender)) {
				sql += " and SUBSTR(IDENTITYCARDNUM,17,4)&1 ";
				if ("1950".equals(age) && StringUtil.isNotBlank(age)) {
					sql += " and SUBSTR(IDENTITYCARDNUM,7,4) < '" + age + "' ";
				} else if (!"1950".equals(age) && StringUtil.isNotBlank(age)) {
					sql += " and SUBSTR(IDENTITYCARDNUM,7,4) like '" + age + "%' " ;
				}
					
			}else{
				sql+= " and SUBSTR(IDENTITYCARDNUM,17,1)=( SUBSTR(IDENTITYCARDNUM,17,1)>>1)<<1 ";
				if ("1950".equals(age) && StringUtil.isNotBlank(age)) {
					sql += " and SUBSTR(IDENTITYCARDNUM,7,4) < '" + age + "' ";
				} else if (!"1950".equals(age) && StringUtil.isNotBlank(age)) {
					sql += " and SUBSTR(IDENTITYCARDNUM,7,4) like '" + age + "%' " ;
				}
			}
				
			int numb = CommonDao.getInstance().queryInt(sql, Integer.class);
			result.setResult(numb);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
    @Override
    public AuditCommonResult<Integer> getProjectHome(String startdate, String enddate,int type) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            String sql = " select COUNT(1) from AUDIT_QUEUE_HISTORY where 1=1 and LENGTH(IDENTITYCARDNUM) = 18 ";
            if (1==type) {
                sql +=" and IDENTITYCARDNUM like '34%'";
                    
            }else{
                sql+="";
            }               
            int numb = CommonDao.getInstance().queryInt(sql, Integer.class);
            result.setResult(numb);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }   

	@Override
	public AuditCommonResult<Double> getRegisteredcapitalSum() {
		AuditCommonResult<Double> result = new AuditCommonResult<Double>();
		try {
			String sql = " select sum(REGISTEREDCAPITAL) sum from AUDIT_RS_COMPANY_REGISTER where IFNULL(IS_HISTORY,0) = 0 AND REGISTERDATE >= '2017-12-12' ";
			Record record = CommonDao.getInstance().find(sql, Record.class);
			Double numb = 0.0;
			if (StringUtil.isNotBlank(record.get("sum"))) {
				numb =  record.get("sum");
			}
			result.setResult(numb);
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
	@Override
	public AuditCommonResult<Integer> getQueueCountByMonth(String month) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		String sql = "select count(1) from audit_queue_history where getnotime like '" + month + "%' ";
		String today = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
		if(today.equals(month)){
			sql = "select count(1) from audit_queue ";
		}
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}
	
	@Override
    public AuditCommonResult<List<Record>> getQueueTodayCountByHour() {
	    AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            String sql = "SELECT HOUR (GETNOTIME) AS HOUR, count(1) AS count FROM audit_queue WHERE GETNOTIME BETWEEN ?1 AND ?2 GROUP BY HOUR (GETNOTIME);";
            Date today = new Date();
            String start = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 08:00:00");
            String end = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 16:30:00");
//            String start = "2019-01-03 08:00:00";
//            String end = "2019-01-03 17:00:00";
            CommonDao common = CommonDao.getInstance();
            List<Record> returnList = common.findList(sql, Record.class, new Object[]{start,end});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

	@Override
	public AuditCommonResult<Integer> getHomeCount(String status) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		
		String sql = "select count(DISTINCT(IDENTITYCARDNUM)) from audit_queue_history where `STATUS` in (1,2) and IDENTITYCARDNUM like '34%'";
		if("2".equals(status)){
		    sql = "select count(DISTINCT(IDENTITYCARDNUM)) from audit_queue_history where `STATUS` in (1,2) and IDENTITYCARDNUM not like '34%'";
		}
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}
	
	@Override
	public AuditCommonResult<Integer> getJDHomeCount(String status) {
		AuditCommonResult<Integer> result = new AuditCommonResult<>();
		
		String sql = "select count(DISTINCT(CERTNUM)) from audit_project where `STATUS` not in ('8','10','20','22','24','14','28') and applyertype = '20' and certnum like '34%' and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
		if("2".equals(status)){
		    sql = "select count(DISTINCT(CERTNUM)) from audit_project where `STATUS` not in ('8','10','20','22','24','14','28') and applyertype = '20' and certnum not like '34%' and centerguid != 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
		}
		try {
			Integer count = CommonDao.getInstance().queryInt(sql);
			result.setResult(count);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

	@Override
	public AuditCommonResult<List<Record>> getQuhaoliangByOu(String sql, List<AuditOrgaDepartment> ouList) {
		AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
		try {
			String reqSql = "select COUNT(t.OUGuid) as cnt,d.ouname,d.ouguid from audit_queue_history h, audit_queue_tasktype t,audit_orga_department d where h.TASKGUID = t.RowGuid and t.OUGuid = d.OUGUID "+sql+"  GROUP BY t.ouguid ";
			List<Record> list = CommonDao.getInstance().findList(reqSql, Record.class);
			result.setResult(list);
		} catch (Exception e) {
			result.setSystemFail(e.getMessage());
		}
		return result;
	}
	
	   @Override
	    public AuditCommonResult<Integer> getOuQuhaoliangByWindowguidList(String sql, String windowlist) {
	        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
	        try {
	            String reqSql = "SELECT COUNT(1) AS count FROM audit_queue_history WHERE HANDLEWINDOWGUID in ("+windowlist+") "+sql;
	            Integer count = CommonDao.getInstance().queryInt(reqSql, new Object[]{});
	            result.setResult(count);
	        } catch (Exception e) {
	            result.setSystemFail(e.getMessage());
	        }
	        return result;
	    }
	
	   @Override
	    public AuditCommonResult<List<Record>> getQuhaoliangWindowByOuguid(String sql, String ouguid) {
	        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
	        try {
	            String reqSql = "SELECT t.*,w.WINDOWNO from audit_orga_window w right join (select t.OUGuid,d.ouname,count(h.HANDLEWINDOWGUID) count,h.HANDLEWINDOWGUID from audit_queue_history h, audit_queue_tasktype t,audit_orga_department d where h.TASKGUID = t.RowGuid and t.OUGuid = d.OUGUID "+sql+" and t.OUGuid = ?1  GROUP BY HANDLEWINDOWGUID)t on t.HANDLEWINDOWGUID =w.RowGuid  ";
	            List<Record> list = CommonDao.getInstance().findList(reqSql, Record.class,new Object[]{ouguid});
	            result.setResult(list);
	        } catch (Exception e) {
	            result.setSystemFail(e.getMessage());
	        }
	        return result;
	    }
	   
       @Override
       public AuditCommonResult<Integer> getQuhaoliangByHandleWindowguid(String sql, String handlewindowguid) {
           AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
           try {
               String reqSql = "select COUNT(1) as count from audit_queue_history  where HANDLEWINDOWGUID =?1"+sql;
               Integer count = CommonDao.getInstance().queryInt(reqSql, new Object[]{handlewindowguid});
               result.setResult(count);
           } catch (Exception e) {
               result.setSystemFail(e.getMessage());
           }
           return result;
       }

    @Override
    public AuditCommonResult<Integer> getXianxiaCount(String startdate, String enddate,String isjd) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            String sql = " select count(1) from audit_project where status >=30 and applyway = 20";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            if(isjd.equals("true")){
                sql += "  and ouguid in(select ouguid from frame_ou where isjd = 3)";
            }else if(isjd.equals("false")){
                sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
            }
            int numb = CommonDao.getInstance().queryInt(sql);
            result.setResult(numb);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getTotalCount(String startdate, String enddate,String isjd) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            String sql = " select count(1) from audit_project where status >=30 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            if(isjd.equals("true")){
                sql += "  and ouguid in(select ouguid from frame_ou where isjd = 3)";
            }else if(isjd.equals("false")){
                sql += " and centerguid = 'cae7fa67-0afb-4362-a113-20bc330e32c0'";
            }
            int numb = CommonDao.getInstance().queryInt(sql);
            result.setResult(numb);
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> isBelongTo(String status) {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        String sql = "select count(1) from AUDIT_RS_INDIVIDUAL_BASEINFO where IFNULL(IS_HISTORY,0) = 0 ";
        String sql2 = "select count(1) from audit_rs_company_baseinfo where IFNULL(IS_HISTORY,0) = 0 ";
        try {
            if ("1".equals(status)) {
                sql+= " and idnumber like '34%' ";
                sql2+= " and ORGALEGAL_IDNUMBER like '34%' ";
            }else if("2".equals(status)){
                sql +=  "";
                sql2 +=  "";
            }else{
                sql += " and idnumber like '11%' or idnumber like '31%' or idnumber like '4401%' or idnumber like '4403%' ";
                sql2 += " and ORGALEGAL_IDNUMBER like '11%' or ORGALEGAL_IDNUMBER like '31%' or ORGALEGAL_IDNUMBER like '4401%' or ORGALEGAL_IDNUMBER like '4403%' ";
            }
            Integer count = CommonDao.getInstance().queryInt(sql);
            Integer count2 = CommonDao.getInstance().queryInt(sql2);
            result.setResult(count+count2);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getWindowTaskCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
            String sql = "SELECT count(1) FROM audit_task WHERE ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0 AND TASK_ID IN ( SELECT TASKID FROM AUDIT_ORGA_WINDOWTASK WHERE windowguid IN ( SELECT RowGuid FROM audit_orga_window WHERE WINDOWTYPE = '10' AND lobbytype = '45c9f7ed-849e-4adb-b926-ea18904d0009'));";
            result.setResult(CommonDao.getInstance().queryInt(sql));
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<Integer> getAppointTodayCount() {
        AuditCommonResult<Integer> result = new AuditCommonResult<>();
        try {
        	String date = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");
            String sql = "SELECT count(1) FROM audit_queue_appointment where CREATEDATE like '"+date+"%'";
            result.setResult(CommonDao.getInstance().queryInt(sql));
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getLightOuList(String startdate, String enddate,String isjd, int first, int pagesize) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            String sql = "select rowguid,light,ouname,IFNULL(count(1),0) count from audit_project where ((ACCEPTUSERDATE < ?1 and ACCEPTUSERDATE > ?2) or ACCEPTUSERDATE is NULL) and OUGUID in(select ouguid from frame_ou where isjd = ?3) GROUP BY light,OUGUID ORDER BY ouname";
            CommonDao common = CommonDao.getInstance();
            List<Record> returnList = common.findList(sql, Record.class, new Object[]{enddate,startdate,isjd});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getLightOuCount(String startdate, String enddate, String isjd) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            String sql = "select DISTINCT(OUNAME) from audit_project where ((ACCEPTUSERDATE < ?1 and ACCEPTUSERDATE > ?2) or ACCEPTUSERDATE is NULL) and OUGUID in(select ouguid from frame_ou where isjd = ?3) GROUP BY light,OUGUID ORDER BY ouname";
            CommonDao common = CommonDao.getInstance();
            Integer returnList = common.findList(sql, Record.class,new Object[]{enddate,startdate,isjd}).size();
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTodayAppointCountByHour() {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            String sql = "SELECT HOUR(GETNOTIME) as hour,count(1) as count FROM audit_queue_appointment WHERE GETNOTIME BETWEEN ?1 AND ?2 GROUP BY HOUR(GETNOTIME) ORDER BY Hour(GETNOTIME);";
            Date today = new Date();
            String start = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 09:00:00");
            String end = EpointDateUtil.convertDate2String(today, "yyyy-MM-dd 16:30:00");
//            String start = "2019-01-03 09:00:00";
//            String end = "2019-01-03 16:30:00";
            CommonDao common = CommonDao.getInstance();
            List<Record> returnList = common.findList(sql, Record.class, new Object[]{start,end});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Integer>> getRiskPointCompare() {
        AuditCommonResult<List<Integer>> result = new AuditCommonResult<List<Integer>>();
        try {
            String sql = "select SUM(a.count) from (select COUNT(TASKGUID) count from audit_task_riskpoint where TASKGUID in (select RowGuid from audit_task where IS_EDITAFTERIMPORT = '1' and IFNULL(IS_HISTORY,0) = 0 and IS_ENABLE = '1' and IS_ENABLE = '1') GROUP BY TASKGUID) a UNION select SUM(b.count) from (select COUNT(TASKGUID) count from audit_task_riskpoint where TASKGUID in (select RowGuid from audit_task where version = '1') GROUP BY TASKGUID) b";
            CommonDao common = CommonDao.getInstance();
            List<Integer> returnList = common.findList(sql, Integer.class, new Object[]{});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String,List<String>>> getRiskUserCompare() {
        AuditCommonResult<Map<String,List<String>>> result = new AuditCommonResult<>();
        try {
            String sqlright = "select DISTINCT(ACCEPTNAME) from audit_task_riskpoint where ACTIVITYNAME = '审查' and TASKGUID in (select RowGuid from audit_task where IS_EDITAFTERIMPORT = '1' and IFNULL(IS_HISTORY,0) = 0 and IS_ENABLE = '1' and IS_ENABLE = '1')";
            String sqlleft = "select DISTINCT(ACCEPTNAME) from audit_task_riskpoint where ACTIVITYNAME = '审查' and TASKGUID in (select RowGuid from audit_task where version = '1')";
            CommonDao common = CommonDao.getInstance();
            List<String> rightList = common.findList(sqlright, String.class, new Object[]{});
            List<String> leftList = common.findList(sqlleft, String.class, new Object[]{});
            Map<String,List<String>> returnList = new HashMap<>();
            returnList.put("rightList", rightList);
            returnList.put("leftList", leftList); 
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<List<Record>> getChildOuProjectCount(String parentouname,String starttime,String endtime) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            String sql = "select IFNULL(COUNT(1),0) count,ouname,ouguid,APPLYWAY from audit_project where `STATUS` not in('8','10','14','20','22','24','28') AND APPLYDATE >=?1 AND APPLYDATE <=?2 AND ouguid in(select ouguid from frame_ou where PARENTOUGUID = (select ouguid from frame_ou where OUNAME LIKE '%"+parentouname+"') ORDER BY ORDERNUMBER desc) GROUP BY ouguid,APPLYWAY ORDER BY OUNAME,LENGTH(OUNAME) DESC";
            if(StringUtil.isBlank(starttime)){
            	starttime = "1900-01-01 00:00:00";
            }
            if(StringUtil.isBlank(endtime)){
            	endtime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd 23:59:59");
            }
            CommonDao common = CommonDao.getInstance();
            List<Record> returnList = common.findList(sql, Record.class, new Object[]{starttime,endtime});
            result.setResult(returnList);
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getChildOuHotTaskTop5ByTime(String startdate, String enddate,String ouguid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        List<Record> list = new ArrayList<Record>();
        try {
            String sql = " select projectname,COUNT(task_id) count from audit_project where status not in('8','10','14','20','22','24','28') and ouguid = ?1 ";
            if (StringUtil.isNotBlank(startdate)) {
                sql += " and applydate >= '" + startdate + "'";
            }
            if (StringUtil.isNotBlank(enddate)) {
                sql += " and applydate <= '" + enddate + "'";
            }
            sql += " GROUP BY task_id ORDER BY COUNT(task_id) desc LIMIT 5 ";
            list = CommonDao.getInstance().findList(sql, Record.class,new Object[]{ouguid});
            result.setResult(list);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }

        return result;
    }

}
