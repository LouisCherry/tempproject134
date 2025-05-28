package com.epoint.newshow.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.newshow.api.NewshowService;

@Service
@Component
public class NewshowServiceImpl implements NewshowService {

	@Override
	public int getacceptNum() {
		String sql="SELECT count(1) acceptnum FROM audit_project WHERE month(ACCEPTUSERDATE)=month(NOW()) and year(ACCEPTUSERDATE)=year(NOW()) AND STATUS>=30";
		return CommonDao.getInstance().queryInt(sql);
	}

	@Override
	public int getbanjieNum() {
		String sql="SELECT count(1) banjienum FROM audit_project WHERE month(BANJIEDATE)=month(NOW()) and year(ACCEPTUSERDATE)=year(NOW()) AND STATUS>=90";
		return CommonDao.getInstance().queryInt(sql);
	}

	@Override
	public int getallAcceptNum() {
		String sql="select count(1) allacceptnum from audit_project WHERE ACCEPTUSERDATE is NOT NULL";
		return CommonDao.getInstance().queryInt(sql);
	}

	@Override
	public int getallBanjieNum() {
		String sql="select count(1) allacceptnum from audit_project WHERE BANJIEDATE is NOT NULL";
		return CommonDao.getInstance().queryInt(sql);
	}

	@Override
	public Record getcitySatisfy() {
		String sql="SELECT SUM(case when satisfied = '1' or satisfied = '2'  or satisfied = '0' THEN 1 else 0 END) AS fcmanyi,"
				+" SUM(case when satisfied = '4' or satisfied = '5' THEN 1 else 0 END) AS bumanyi,SUM(case when satisfied = '3' THEN 1 else 0 END) AS jbmanyi"
				+" from audit_online_evaluat e INNER JOIN audit_project p on p.RowGuid = e.ClientIdentifier where clienttype =10 and ACCEPTUSERGUID is NOT NULL";
		return CommonDao.getInstance().find(sql,Record.class);
	}

	@Override
	public List<Record> geteventType() {
		String sql="SELECT b.XiaQuName name,SUM(case when a.SHENPILB= 01 THEN 1 else 0 END) value1,SUM(case when a.SHENPILB= 11 THEN 1 else 0 END) value2,SUM(case when a.SHENPILB= 10 THEN 1 else 0 END) value3 FROM audit_task a LEFT JOIN audit_orga_area b ON a.AREACODE=b.XiaQuCode WHERE ("
				+" a.IS_HISTORY = 0 OR a.IS_HISTORY IS NULL OR a.IS_HISTORY = '') AND a.IS_EDITAFTERIMPORT = 1 AND a.IS_ENABLE = 1 AND a.ISTEMPLATE = 0 GROUP BY AREACODE";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public List<Record> getmapData() {
		String sql="SELECT b.XiaQuName name,count(1) value FROM audit_project a LEFT JOIN audit_orga_area b ON a.AREACODE=b.XiaQuCode WHERE a.AREACODE<>'370800' and a.STATUS>24 GROUP BY AREACODE ORDER BY value desc LIMIT 5";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public Record gethandleEvent() {
		String sql="SELECT sum(case when YEAR(applydate)=YEAR(NOW()) THEN 1 else 0 END) year ,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month,sum(case when DAY(applydate)=DAY(NOW()) and month(applydate)=month(NOW()) THEN 1 else 0 END) day FROM audit_project WHERE `STATUS`>24 and AND applydate >= DATE_FORMAT(NOW(), '%Y-01-01') AND applydate < DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 YEAR), '%Y-01-01')";
		return CommonDao.getInstance().find(sql, Record.class);
	}

	@Override
	public Record getcityData() {
		String sql="SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month,sum(case when DAY(applydate)=DAY(NOW()) and month(applydate)=month(NOW()) THEN 1 else 0 END) day FROM audit_project WHERE `STATUS`>24 and YEAR(applydate)=YEAR(NOW())";
		return CommonDao.getInstance().find(sql, Record.class);
	}
	@Override
	public Record getcityDatabyid() {
		String sql="SELECT count(1) total,sum(case when month(applydate)=month(NOW()) THEN 1 else 0 END) month,sum(case when DAY(applydate)=DAY(NOW()) and month(applydate)=month(NOW()) THEN 1 else 0 END) day FROM audit_project WHERE `STATUS`>24 and areacode='370800' and YEAR(applydate)=YEAR(NOW())";
		return CommonDao.getInstance().find(sql, Record.class);
	}

	@Override
	public Record getsource() {
		String sql="SELECT sum(case when APPLYWAY = '40' THEN 1 else 0 END) wx,sum(case when APPLYWAY = '20' THEN 1 else 0 END) chuangkou,sum(case when APPLYWAY = '10' or APPLYWAY = '11' THEN 1 else 0 END) waiwang,sum(case when APPLYWAY = '50' THEN 1 else 0 END) zz FROM audit_project WHERE APPLYWAY is not NULL AND ACCEPTUSERGUID is NOT NULL";
		return CommonDao.getInstance().find(sql,Record.class);
	}

	@Override
	public List<Record> geteventTop5() {
		String sql="SELECT a.PROJECTNAME name,count(1) value FROM audit_project a WHERE a.ACCEPTUSERGUID is NOT NULL and a.`STATUS`>24 GROUP BY a.TASKID ORDER BY value DESC LIMIT 5";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public List<Record> gettrend() {
		String sql="SELECT concat(month(BANJIEDATE),'月') as name,count(1) as value FROM audit_project WHERE BANJIEDATE is NOT NULL and `STATUS`>24 AND year(BANJIEDATE)=year(NOW()) GROUP BY month(BANJIEDATE)";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public List<Record> getmapbanjian() {
		String sql="SELECT b.XiaQuName as name,SUM(case when year(a.applydate) = year(NOW()) THEN 1 else 0 END) as year,SUM(case when MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as month,SUM(case when day(a.applydate) = day(NOW()) AND MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as day FROM audit_project a LEFT JOIN audit_orga_area b ON a.AREACODE=b.XiaQuCode WHERE a.AREACODE<>'370800' and a.STATUS>24 AND year(a.applydate)=year(NOW()) GROUP BY AREACODE ORDER BY year DESC";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

}
