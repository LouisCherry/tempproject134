package com.epoint.jnzwfw.auditkaoqinchecktime;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

/**
 * @作者 wurui
 * @version [版本号, 2018年5月9日]
 */
public class KaoqinCheckInfoService {

	/**
	 * 根据年月返回考勤记录
	 * 
	 * @return
	 */
	public List<Record> getKaoqinInfoAll(String year, String month) {
		String sql = "select name as username,userguid,gonghao,ouname,ouguid,userchecktime from kaoqinrecord a join ("
				+ "select gonghao,a.ouname,a.ouguid,a.userguid FROM audit_orga_member a INNER JOIN audit_orga_department b ON a.OUGuid = b.OUGuid "
				+ "WHERE   Is_KaoQin = 1 ORDER BY b.OrderNum DESC ,gonghao ASC"
				+ ") b on a.jobnumber = b.gonghao where DATE_FORMAT(userchecktime,'%Y-%m') like CONCAT(" + year
				+ " , '-' , " + month + ")"
				+ " order by gonghao ";
		List<Record> kaoqinlist = CommonDao.getInstance().findList(sql, Record.class);
		return kaoqinlist;
	}
	
	/**
	 * 根据年月 考勤人工号返回考勤记录
	 * 
	 * @return
	 */
	public List<Record> getKaoqinInfoByGh(String year, String month,String userId) {
		String sql = "select name as username,userguid,gonghao,ouname,ouguid,userchecktime,timeresult from kaoqinrecord a join ("
				+ "select gonghao,a.ouname,a.ouguid,a.userguid,a.userid FROM audit_orga_member a INNER JOIN audit_orga_department b ON a.OUGuid = b.OUGuid"
				+ " WHERE   Is_KaoQin = 1 ORDER BY b.OrderNum DESC ,gonghao ASC"
				+ ") b on a.userId = b.userId where DATE_FORMAT(userchecktime,'%Y-%m') like CONCAT(? , '-' , ?) and b.userId = ?"
				+ " order by gonghao asc,userchecktime asc ";
		List<Record> kaoqinlist = CommonDao.getInstance().findList(sql, Record.class,year,month,userId);
		return kaoqinlist;
	}

}
