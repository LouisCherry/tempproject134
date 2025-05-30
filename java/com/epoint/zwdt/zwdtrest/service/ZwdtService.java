package com.epoint.zwdt.zwdtrest.service;

import java.util.List;
import java.util.UUID;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class ZwdtService
{

	 public AuditTask getAuditTaskByItemid(String itemid) {
	        String sql = "select rowguid from audit_task where IS_ENABLE='1' and ifnull(IS_HISTORY,0) = 0 and IS_EDITAFTERIMPORT='1' and item_id = ?";
	        return CommonDao.getInstance().find(sql, AuditTask.class, itemid);
	    }
	 
	 
    public List<FrameOu> getOuListByAreacode(String areacode) {
        String sql = "select OUCODE,ouguid,ouname,OUSHORTNAME,tel from frame_ou where OUCODE like '%"+areacode+"%' "
                + " and iszixun = '1' GROUP BY OUGUID ORDER BY ORDERNUMBER desc";
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }
    public List<FrameOu> getOuList(String areacode) {
        // 兖州区特殊，370882以及370812
        String sql = "select o.OUCODE,o.ouguid,o.ouname,o.OUSHORTNAME,o.tel from frame_ou o join frame_ou_extendinfo e on o.ouguid = e.ouguid where OUCODE like '%"+areacode+"%' "
                + "  and o.OUCODELEVEL is not null and e.isshow='1'  GROUP BY o.OUGUID order by o.ORDERNUMBER desc";
        if ("370882".equals(areacode)) {
            sql = "select o.OUCODE,o.ouguid,o.ouname,o.OUSHORTNAME,o.tel from frame_ou o join frame_ou_extendinfo e on o.ouguid = e.ouguid where (OUCODE like '%370882%' or OUCODE like '%370812%') "
                    + "  and o.OUCODELEVEL is not null and e.isshow='1'  GROUP BY o.OUGUID order by o.ORDERNUMBER desc";
        }
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }
    
    public FrameOu getOuShortNameByOuguid(String ouguid) {
        String sql = "select b.oushortname from audit_task a join frame_ou b on a.ouguid = b.ouguid where b.ouguid = ?";
        return CommonDao.getInstance().find(sql, FrameOu.class, ouguid);
    }
    
    public FrameOu getFrameOuByOuguid(String ouguid) {
        String sql = "select b.areacode,ouname,tel,address,a.webaddress,case when LENGTH(a.webaddress) > 36 then CONCAT(SUBSTR(a.webaddress,1,37),'...') else a.webaddress end as webaddressurl from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid where a.ouguid = ?";
        return CommonDao.getInstance().find(sql, FrameOu.class, ouguid);
    }
    
    public List<FrameOu> getOuListQuestion(String areacode) {
        String sql = "select a.ouguid,a.ouname,a.oushortname from frame_ou a join audit_task b on a.OUGUID=b.OUGUID where a.OUCODE like  '%"+areacode+"%'"
                   + " and b.IS_ENABLE='1' and b.IS_HISTORY='0' and b.IS_EDITAFTERIMPORT='1'  GROUP BY a.OUGUID   ";
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }
    
    public List<AuditTask> getShenpiList(String ouguid,String applyertype) {
        String sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num from audit_task where task_id is not null and IFNULL(IS_HISTORY,0)= 0 AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and is_enable = 1  and IS_EDITAFTERIMPORT=1 and ouguid = ? group by shenpilb ";
        return CommonDao.getInstance().findList(sql, AuditTask.class, ouguid);
    }
    
    public List<FrameOu> getOuListByAreacode1(String areacode) {
        String sql = "SELECT b.ouguid,b.ouname,b.oushortname FROM frame_ou b join frame_ou_extendinfo e on b.ouguid = e.OUGUID WHERE ";
        sql += " b.OUGUID IN (SELECT a.ouguid FROM audit_task a WHERE a.AREACODE = ? AND (IFNULL(a.iswtshow,1) = 1 or a.iswtshow = '') AND IFNULL(a.is_history, 0) = 0 AND a.IS_EDITAFTERIMPORT = 1 AND a.IS_ENABLE = 1 GROUP BY a.OUGUID)";
        sql += "and length(e.areacode) = 6 ORDER BY ordernumber DESC";
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }

    public List<FrameOu> getOuListByAreacodeHaveTasks(String areacode) {
        String sql = "select a.*,b.OUSHORTNAME from (select distinct OUGUID,ouname from audit_task t where AREACODE = ? " +
                "AND (IFNULL(iswtshow, 1) = 1 or iswtshow = '') AND IFNULL(is_history, 0) = 0 AND IS_EDITAFTERIMPORT = 1 " +
                "AND IS_ENABLE = 1) a left join frame_ou as b on a.OUGUID=b.OUGUID  where OUSHORTNAME is not null and OUSHORTNAME!='' ";
        return CommonDao.getInstance().findList(sql, FrameOu.class, areacode);
    }
    
    public List<AuditTask> getShenpiListByAreacode(String areacode, String ouguid, String applyertype,String dictid) {
    	String sql = "";
    	if(StringUtil.isNotBlank(dictid)) {
            sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num,SUM(CASE WHEN length(item_id) = 33 THEN 1 ELSE 0 END) as numson from audit_task a left join audit_task_extension b on a.rowguid = b.taskguid where task_id is not null AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1  and IS_EDITAFTERIMPORT=1 and areacode = ?1 and applyertype like '%"+applyertype+"%' and a.shenpilb <> '15'";
    	}else {
            sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num,SUM(CASE WHEN length(item_id) = 33 THEN 1 ELSE 0 END) as numson from audit_task where task_id is not null and IFNULL(IS_HISTORY,0)= 0 AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and is_enable = 1  and IS_EDITAFTERIMPORT=1 and areacode = ?1 and applyertype like '%"+applyertype+"%' and shenpilb <> '15'";
    	}
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = '"+ouguid+"'";
        }
        if (StringUtil.isNotBlank(dictid)) {
        	if ("10".equals(applyertype)) {
        		sql += " and b.TASKCLASS_FORCOMPANY like '%"+dictid+"%'";
        	}else {
        		sql += " and b.TASKCLASS_FORPERSION like '%"+dictid+"%'";
        	}
        }
        sql += " group by shenpilb order by count(1) desc";
        return CommonDao.getInstance().findList(sql, AuditTask.class, areacode);
    }
    
    
    public List<AuditTask> getShenpiListByAreacodeDt(String areacode) {
    	String sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num,SUM(CASE WHEN length(item_id) = 33 THEN 1 ELSE 0 END) as numson from audit_task where task_id is not null and IFNULL(IS_HISTORY,0)= 0 AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and is_enable = 1  and IS_EDITAFTERIMPORT=1 and areacode = ?1  and shenpilb <> '15'";
        sql += " group by shenpilb order by count(1) desc";
        return CommonDao.getInstance().findList(sql, AuditTask.class, areacode);
    }
    
    
    public List<AuditTask> getShenpiListByAreacodeYC(String areacode, String ouguid, String applyertype,String dictid,String Dao_xc_num,String ISPYC,String Operationscope,String If_express,String mpmb,String CHARGE_FLAG,String mashangban, String wangshangban, String sixshenpilb,String yishenqing,String isyc,String qctb,String xzql,String kstbsx,String qstb) {
    	String sql = "";
    	if(StringUtil.isNotBlank(dictid)) {
            sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num,SUM(CASE WHEN length(item_id) = 33 THEN 1 ELSE 0 END) as numson from audit_task a left join audit_task_extension b on a.rowguid = b.taskguid where task_id is not null and IFNULL(IS_HISTORY,0)= 0 AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and is_enable = 1  and IS_EDITAFTERIMPORT=1 and areacode = ?1 and a.shenpilb <> '15' ";
    	}else {
            sql = "select shenpilb,SUM(CASE WHEN length(item_id) = 31 THEN 1 ELSE 0 END) as num,SUM(CASE WHEN length(item_id) = 33 THEN 1 ELSE 0 END) as numson from audit_task a where task_id is not null and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1  AND (IFNULL(iswtshow,1) = 1 or iswtshow = '') and IS_EDITAFTERIMPORT=1 and areacode = ?1 and a.shenpilb <> '15' and taskcode <> ''  ";
    	}
    	if ("1".equals(kstbsx)) {
        	sql +=  " and iskstb  = '1' ";
        }
    	if ("1".equals(qstb)) {
        	sql +=  " and isjnqstb = '1' ";
        }
    	
    	if (StringUtil.isNotBlank(Dao_xc_num) && "1".equals(Dao_xc_num)) {
        	sql +=  " and a.applyermin_count in ('0','1') and a.businesstype = '1' ";
        }
    	if (StringUtil.isNotBlank(applyertype) && !"1".equals(isyc)) {
    		sql +=  " and a.applyertype LIKE '%"+applyertype+"%'";
    	}
    	if ("1".equals(isyc)) {
    		sql +=  " and a.businesstype = '1' ";
    	}
    	if ("1".equals(xzql)) {
    		sql +=  " and a.shenpilb <> '11' ";
    	}
    	if (StringUtil.isNotBlank(sixshenpilb) && "1".equals(sixshenpilb)) {
        	sql +=  " and a.shenpilb in ('01','10','07','05','08','06','11','16')";
        }
    	if (StringUtil.isNotBlank(yishenqing) && "1".equals(yishenqing)) {
    		sql +=  " and a.businesstype = '1' ";
    	}
    	 if (StringUtil.isNotBlank(ISPYC)) {
         	sql +=  " and a.ISPYC = '1'";
         }
    	 if (StringUtil.isNotBlank(CHARGE_FLAG)) {
          	sql +=  " and a.CHARGE_FLAG = '1'";
          }
    	 if (StringUtil.isNotBlank(mashangban)) {
         	sql +=  " and a.promise_day in ('0','1') and a.taskcode <> '' and a.businesstype = '1'";
         }
         if (StringUtil.isNotBlank(wangshangban)) {
         	sql +=  " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' or a.wangbanshendu like '%2%' or a.wangbanshendu like '%3%' or a.wangbanshendu like '%4%') and a.businesstype = '1'";
         }
         if (StringUtil.isNotBlank(qctb)) {
          	sql +=  " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%') and a.businesstype = '1'";
          }
    	 if (StringUtil.isNotBlank(mpmb) && "1".equals(mpmb)) {
         	sql +=  " and a.is_mpmb = '1' ";
         }
         if (StringUtil.isNotBlank(Operationscope)) {
         	sql +=  " and a.CROSS_SCOPE in ('1','2','3') ";
         }
         if (StringUtil.isNotBlank(If_express)) {
         	sql +=  " and a.IS_DELIVERY = '1' ";
         }
         if (StringUtil.isNotBlank(Dao_xc_num) && "0".equals(Dao_xc_num)) {
         	sql +=  " and (a.applyermin_count = '0' or a.wangbanshendu REGEXP '5|6|7') and a.taskcode <> '' and a.businesstype = '1' ";
         }
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = '"+ouguid+"'";
        }
        if (StringUtil.isNotBlank(dictid)) {
        	if ("10".equals(applyertype)) {
        		sql += " and b.TASKCLASS_FORCOMPANY like '%"+dictid+"%'";
        	}else {
        		sql += " and b.TASKCLASS_FORPERSION like '%"+dictid+"%'";
        	}
        }
        sql += " group by shenpilb order by count(1) desc";
        return CommonDao.getInstance().findList(sql, AuditTask.class, areacode);
    }
    
    public List<AuditTask> getTaskListByContension(String taskname, String ouguid, int pageNumber, int pageSize) {
        String sql = "select Transact_addr,ouname from audit_task where task_id is not null and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1  and IS_EDITAFTERIMPORT=1";
        if(StringUtil.isNotBlank(ouguid) && !"--请选择部门--".equals(ouguid)) {
            sql += " and ouguid = '"+ ouguid + "'";
        }
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like '%"+ taskname + "%'";
        }
        sql += " and Transact_addr <> '' group by Transact_addr";
        return CommonDao.getInstance().findList(sql, pageNumber, pageSize, AuditTask.class);
    }
    
    public List<AuditTask> getTaskListByAddress(String address, String taskname, String ouguid ) {
        String sql = "select rowguid,task_id,taskname from audit_task where task_id is not null and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1  and IS_EDITAFTERIMPORT=1 and Transact_addr = ?1 and ouguid = ?2";
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like '%"+ taskname + "%'";
        }
        return CommonDao.getInstance().findList(sql, AuditTask.class, address, ouguid);
    }

    public List<AuditOnlineConsult> getConsultListByOu(String areacode, String ouguid, int currentPage, int pageSize, String titlename, String taskname,String status) {
        String sql = "SELECT a.RowGuid,a.title,b.OUNAME,DATE_FORMAT(a.ASKDATE,'%Y-%m-%d') as askdate,status from audit_online_consult a join frame_ou b on a.OUGUID=b.OUGUID "
                + " where a.areacode= '"+areacode+"' and (iswtshow = '1' or iswtshow is null) ";
                if(!"all".equals(ouguid)){
                    sql +=" and a.ouguid = '"+ouguid+"'";
                }
                if (StringUtil.isNotBlank(titlename)) {
                    sql +=" and a.title like '%"+titlename+"%'";
                }
                
                /*if (StringUtil.isNotBlank(taskname)) {
                    sql +=" and t.taskname like '%"+taskname+"%'";
                }*/
                if (StringUtil.isNotBlank(status)) {
                    sql +=" and a.status ="+status;
                }
               sql  += "  ORDER BY ASKDATE desc limit "+currentPage+","+pageSize;
        return CommonDao.getInstance().findList(sql, AuditOnlineConsult.class, ouguid);
    }
    
    public List<AuditDaibanConsult> getConsultDaibanListByOu(String areacode, String ouguid, int currentPage, int pageSize, String titlename, String taskname,String status) {
        String sql = "SELECT a.RowGuid,a.title,b.OUNAME,DATE_FORMAT(a.ASKDATE,'%Y-%m-%d') as askdate,status from audit_daiban_consult a join frame_ou b on a.OUGUID=b.OUGUID "
                + " where a.areacode= '"+areacode+"' and (iswtshow = '1' or iswtshow is null) ";
                if(!"all".equals(ouguid)){
                    sql +=" and a.ouguid = '"+ouguid+"'";
                }
                if (StringUtil.isNotBlank(titlename)) {
                    sql +=" and a.title like '%"+titlename+"%'";
                }
                
                /*if (StringUtil.isNotBlank(taskname)) {
                    sql +=" and t.taskname like '%"+taskname+"%'";
                }*/
                if (StringUtil.isNotBlank(status)) {
                    sql +=" and a.status ="+status;
                }
               sql  += "  ORDER BY ASKDATE desc limit "+currentPage+","+pageSize;
        return CommonDao.getInstance().findList(sql, AuditDaibanConsult.class, ouguid);
    }
    

    public void addExmine(String age, String education, String line, String text) {
        
        String rowguid = UUID.randomUUID().toString();
        
        String sql = "INSERT into audit_online_examine (rowguid,operatedate,age,education,line,text) VALUES(?1,NOW(),?2,?3,?4,?5)";
        
        CommonDao.getInstance().execute(sql, rowguid,age,education,line,text);
    }
    
    public List<AuditTask> getTaskListByAreacode(String ouguid) {
        String sql = "select task_id from audit_task t where t.areacode = ? and t.task_id is not null and IFNULL(t.IS_HISTORY,0)= 0 and t.is_enable = 1  and t.IS_EDITAFTERIMPORT=1";
        return CommonDao.getInstance().findList(sql, AuditTask.class, ouguid);
    }
    
    public List<AuditTask> getTaskListByOuguid(String ouguid) {
        String sql = "select * from audit_task t where t.ouguid = ? and t.task_id is not null and IFNULL(t.IS_HISTORY,0)= 0 and t.is_enable = 1  and t.IS_EDITAFTERIMPORT=1";
        return CommonDao.getInstance().findList(sql, AuditTask.class, ouguid);
    }
    public List<AuditTask> getTaskListByOuguidAndName(String ouguid,String taskname) {
    	String sql = "";
    	if(StringUtil.isBlank(taskname)) {
    		sql = "select * from audit_task t where t.ouguid = ? and t.task_id is not null and IFNULL(t.IS_HISTORY,0)= 0 and t.is_enable = 1  and t.IS_EDITAFTERIMPORT=1 ";
        	return CommonDao.getInstance().findList(sql, AuditTask.class, ouguid);
    	}else {
    		sql = "select * from audit_task t where t.ouguid = ? and t.task_id is not null and IFNULL(t.IS_HISTORY,0)= 0 and t.is_enable = 1  and t.IS_EDITAFTERIMPORT=1 and taskname like ? ";
        	return CommonDao.getInstance().findList(sql, AuditTask.class, ouguid,"%"+taskname+"%");
    	}
    	
    }
    
    public List<String> findUserMoblieList(String ouGuid) {
        String sql = "SELECT a.MOBILE from frame_user a join frame_userrolerelation b on a.USERGUID=b.USERGUID join frame_role c on b.ROLEGUID=c.ROLEGUID "
                + "  where ouguid=? and c.ROLENAME='咨询投诉'";
        return CommonDao.getInstance().findList(sql, String.class, ouGuid);
    }
    
}
