package com.epoint.xmz.performance.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

@Component
@Service
public class PerformanceServiceImplService
{
    private ICommonDao commonDao;
    
    public PerformanceServiceImplService() {
        commonDao = CommonDao.getInstance();
    }

    public List<Record> getfwfswbd() {
        String sql = "select b.areacode,count(1) as num from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid where LENGTH(b.areacode) = 6  group by b.areacode ";
        return commonDao.findList(sql, Record.class);
    }
    
    /**
     * 
     * 分页查询事项进驻情况
     * 
     */
    public List<Record> gettaskin(int pageindex, int pagesize,String areacode) {
        String sql = "select * from zj_xnkh_sxjz where areacode = ? ORDER BY ordernumber DESC";
        return commonDao.findList(sql, pageindex, pagesize, Record.class,areacode);
    }
    
    public String gettaskincount(String areacode) {
    	 String sql = "select count(1) as total from zj_xnkh_sxjz where areacode = ?";
         return commonDao.find(sql, String.class,areacode);
    }
    
    /**
     * 
     * 业务办理结果
     * 
     */
    public List<Record> getywbljg(int pageindex, int pagesize,String areacode) {
        String sql = "select * from zj_xnkh_ywbl where areacode = ? and tasknum is not null ORDER BY total DESC";
        return commonDao.findList(sql, pageindex, pagesize, Record.class,areacode);
    }
    
    public String getywbljgcount(String areacode) {
   	 String sql = "select count(1) from zj_xnkh_ywbl where areacode = ? and tasknum is not null";
        return commonDao.find(sql, String.class,areacode);
   }
   
    /**
     * 
     * 在线服务成效度的即办件和零跑动占比
     * 
     */
    public Record getzxfwcxdjb() {
        String sql = "SELECT SUM(CASE WHEN type = '1' THEN 1 ELSE 0 END) jiban,SUM( CASE WHEN applyermin_count = '0' or wangbanshendu REGEXP '5|6|7' THEN 1 ELSE 0 END) lpt,count(1) ";
        sql += " FROM audit_task WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) ";
        sql += " and shenpilb in ('01','10','07','05','08','06','11') AND LENGTH(areacode) = 6 ";
        return commonDao.find(sql, Record.class);
    }
    
    /**
     * 
     * 在线服务成效度的承诺时限压缩比
     * 
     */
    public Record getzxfwcxdcn() {
        String sql = "SELECT (sum(anticipate_day) - sum(promise_day))/sum(anticipate_day) as total FROM audit_task WHERE IS_EDITAFTERIMPORT = 1  ";
        sql += " AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) AND promise_day IS NOT NULL  ";
        sql += " and shenpilb in ('01','10','07','05','08','06','11') AND LENGTH(areacode) = 6 AND anticipate_day IS NOT NULL ";
        return commonDao.find(sql, Record.class);
    }
    
    
    /**
     * 
     * 分页查询在线办理深度
     * 
     */
    public List<Record> getzxblsd(int pageindex, int pagesize) {
        String sql = "select frame_ou.oushortname as ouname,count(1) as total,FORMAT(SUM(CASE WHEN wangbanshendu REGEXP '2|3|4|5|6|7' THEN 1 ELSE 0 END)/count(1)*100,2) as keban,";
        sql += " FORMAT(SUM(CASE WHEN wangbanshendu REGEXP '5|6|7' THEN 1 ELSE 0 END)/count(1)*100,2) as quancheng from audit_task join frame_ou on audit_task.ouguid = frame_ou.ouguid ";
        sql += " WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) and shenpilb = '01' AND areacode = '370800' ";
        sql += " AND(IFNULL(iswtshow,1) = 1 or iswtshow = '') group by audit_task.ouname order by frame_ou.ORDERNUMBER desc ";
        return commonDao.findList(sql, pageindex, pagesize, Record.class);
    }
    
    /**
     * 
     * 分页查询在线办理深度
     * 
     */
    public List<Record> getzxblsd1( int pageindex, int pagesize,String areacode,String areacode1) {
        String sql = "select frame_ou.OUSHORTNAME as ouname,SUM(CASE WHEN businesstype = '1' and (wangbanshendu = '1' or wangbanshendu = '1^9' or wangbanshendu = '9' or wangbanshendu = '' or wangbanshendu is null) THEN 1 ELSE 0 END) wangban1,";
        sql += " SUM(CASE WHEN businesstype = '1' and wangbanshendu not REGEXP '4|5|6|7' and wangbanshendu REGEXP '2|3' THEN 1 ELSE 0 END) wangban2, ";
        sql += " SUM(CASE WHEN businesstype = '1' and wangbanshendu not REGEXP '5|6|7' and wangbanshendu like '%4%' THEN 1 ELSE 0 END) wangban3, ";
        sql += " SUM(CASE WHEN businesstype = '1' and wangbanshendu REGEXP '5|6|7' THEN 1 ELSE 0 END) wangban4 from audit_task  join frame_ou on  ";
        sql += " audit_task.ouguid = frame_ou.ouguid join frame_ou_extendinfo on frame_ou.ouguid = frame_ou_extendinfo.ouguid WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL)  ";
        sql += " and shenpilb in ('01','10','07','05','08','06','11') AND audit_task.areacode = ? and frame_ou_extendinfo.areacode = ? AND(IFNULL(iswtshow,1) = 1 or iswtshow = '') group by audit_task.ouname order by frame_ou.ORDERNUMBER desc  ";
        return commonDao.findList(sql, pageindex, pagesize, Record.class,areacode,areacode1);
    }
    
    
    
    /**
     * 
     * 分页查询在线办理深度
     * 
     */
    public List<Record> getzxblsdcount() {
        String sql = "select 1 from audit_task join frame_ou on audit_task.ouguid = frame_ou.ouguid WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0  ";
        sql += " AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) and shenpilb = '01' AND areacode = '370800' ";
        sql += " AND(IFNULL(iswtshow,1) = 1 or iswtshow = '') group by audit_task.ouname order by frame_ou.ORDERNUMBER desc  ";
        return commonDao.findList(sql, Record.class);
    }
    
    /**
     * 
     * 分页查询在线办理深度
     * 
     */
    public List<Record> getzxblsdcount1(String areacode,String areacode1) {
    	String sql = "select 1 from audit_task  join frame_ou on audit_task.ouguid = frame_ou.ouguid join frame_ou_extendinfo on frame_ou.ouguid = frame_ou_extendinfo.ouguid WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0  ";
    	sql += " AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) and shenpilb in ('01','10','07','05','08','06','11') AND audit_task.areacode = ? and frame_ou_extendinfo.areacode = ? ";
    	sql += " AND(IFNULL(iswtshow,1) = 1 or iswtshow = '') group by audit_task.ouname order by frame_ou.ORDERNUMBER desc   ";
    	return commonDao.findList(sql, Record.class,areacode,areacode1);
    }
    
    
    public String getbanjiecount() {
    	 String sql = "SELECT FORMAT(SUM(CASE WHEN BANJIEDATE  < PROMISEENDDATE THEN 1 ELSE 0 END)/count(1)*100,2) as banjie FROM audit_project ";
         sql += " WHERE STATUS >= 90 and LENGTH(areacode) = 6 and PROMISEENDDATE is not NULL and BANJIEDATE is not null ";
    	 return commonDao.find(sql, String.class);
    }
    
    /**
     * 
     * 在线服务成效度
     * 
     */
    public Record getzxfwcxd() {
        String sql = "select * from zj_xnkh_zxfw";
        return commonDao.find(sql, Record.class);
    }
    
    /**
     * 
     * 服务事项覆盖度
     * 
     */
    public Record getfwsxfgd() {
        String sql = "select * from zj_xnkh_fwsx";
        return commonDao.find(sql, Record.class);
    }
    
    /**
     * 
     * 统计效能考核数据
     * @return 
     * 
     */
    public List<Record> zjxnkh() {
        String sql = "zjxnkh";
        return commonDao.executeProcudure(sql, "370800");
    }
    
}
