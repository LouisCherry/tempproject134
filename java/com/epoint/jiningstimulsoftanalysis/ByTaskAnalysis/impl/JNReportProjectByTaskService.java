package com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.epoint.common.util.StringUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 
 * @author oliver
 */
public class JNReportProjectByTaskService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;
    
    private IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);    

    public JNReportProjectByTaskService() {
        baseDao = CommonDao.getInstance();
    }
   
    
    
    /**
     * 根据申请时间范围和部门查找所有数据列表
     *  @param startdate
     *  @param enddate
     *  @param ouguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> findAllList(String startdate,String  enddate, String ouguid) {
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        String[] ouguids ;
        String ousql ;
        String ounameSql = "";
        String strstart;
        String str;
        String strEnd;
        //拼接部门查询条件sql
        if (StringUtil.isNotBlank(ouguid)) {
              ouguids = ouguid.split(",");
               strstart ="(";
               str ="";
               strEnd =")";
              for (int i = 0; i < ouguids.length; i++) {
                  String ouName = ouService.getOuByOuGuid(ouguids[i]).getOuname();
                  ousql = "'"+ouName+"'";
                  if (i!=ouguids.length-1) {
                      str += ousql+",";
                  }else {
                      str += ousql;
                }
                  
            }
               ounameSql = strstart+str+strEnd;
        }
        String sql = "";
            sql =  " SELECT " + 
                    "    ouname," + 
                    "    ouguid," + 
                    "    sum(ct2) AS 有办件的依申请事项," + 
                    "    sum(ct3) AS 办件办结量," + 
                    "    sum(ct4) AS 外网申报数," + 
                    "    sum(ct5) AS 外网申报率," + 
                    "    sum(ct6) AS 外网申报未受理数," + 
                    "    sum(ct7) AS 按期办理数," + 
                    "    sum(ct8) AS 办件按期办结率 " + 
                    " FROM " + 
                    "    (" + 
                    "        SELECT" + 
                    "                ouname," + 
                    "                ouguid," + 
                    "                ct2," + 
                    "                ct3," + 
                    "                ct4," + 
                    "                ct5," + 
                    "                ct6," + 
                    "                ct7," + 
                    "                ct8" + 
                    "        FROM" + 
                    "            (" + 
                    "            select " + 
                    "            b.ouname," + 
                    "            b.ouguid," + 
                    "            count(1) ct2," + 
                    "            SUM(CASE WHEN  b.status = '90' THEN 1 ELSE 0 END) ct3," + 
                    "            SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END) ct4,   " + 
                    "            SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END)/SUM(CASE WHEN b.rowguid is not null THEN 1 ELSE 0 END) as ct5," + 
                    "            SUM(CASE WHEN  b.status = '12' THEN 1 ELSE 0 END) as ct6," + 
                    "            SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct7," + 
                    "            SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END)/SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct8" + 
                    "            from audit_task a  join audit_project b on a.rowguid = b.taskguid " + 
                    "            where  is_enable = '1' and a.businesstype = '1' and ifnull(IS_HISTORY,0) = 0 and b.ouname is not null " ;
                    if (!"370800".equals(areacode)) {
                        sql+= " and b.`AREACODE` = '"+areacode+"' ";
                    }
                    if (StringUtil.isNotBlank(startdate)&&StringUtil.isNotBlank(enddate)) {
                        sql+= " and ( b.`APPLYDATE` >= '"+startdate+"' and b.`APPLYDATE` <= '"+enddate+"' ) ";
                    }else if (StringUtil.isNotBlank(startdate)&&StringUtil.isBlank(enddate)) {
                        sql+= " and  b.`APPLYDATE` >= '"+startdate+"'  ";
                   }else if (StringUtil.isBlank(startdate)&&StringUtil.isNotBlank(enddate)) {
                       sql+= " and  b.`APPLYDATE` <= '"+enddate+"'  ";
                  }
                    sql+=
                    "            group by b.ouname" + 
                    "            ) a" + 
                    "        UNION ALL" + 
                    "            SELECT" + 
                    "                ouname," + 
                    "    			 ouguid," + 
                    "                ct2," + 
                    "                ct3," + 
                    "                ct4," + 
                    "                ct5," + 
                    "                ct6," + 
                    "                ct7," + 
                    "                ct8 " + 
                    "            FROM " + 
                    "                (" + 
                    "                select " + 
                    "                b.ouname," + 
                    "                b.ouguid," + 
                    "                count(1) ct2," + 
                    "                SUM(CASE WHEN  b.status = '90' THEN 1 ELSE 0 END) ct3," + 
                    "                SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END) ct4,   " + 
                    "                SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END)/SUM(CASE WHEN b.rowguid is not null THEN 1 ELSE 0 END) as ct5," + 
                    "                SUM(CASE WHEN  b.status = '12' THEN 1 ELSE 0 END) as ct6," + 
                    "                SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct7," + 
                    "                SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END)/SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct8" + 
                    "                from audit_task a  join lc_project b on a.rowguid = b.taskguid " + 
                    "                where  is_enable = '1' and a.businesstype = '1' and ifnull(IS_HISTORY,0) = 0 and b.ouname is not null ";
                    if (!"370800".equals(areacode)) {
                        sql+= " and b.`AREACODE` = '"+areacode+"' ";
                    }
                    sql+=
                    "                group by b.ouname" + 
                    "                ) b" + 
                    "    ) dd " ;
            if (StringUtil.isNotBlank(ouguid)) {
                sql+= " where dd.ouname in  " +ounameSql;
           }
             sql+=
                    " GROUP BY " + 
                    "    dd.ouname ";
        
        return baseDao.findList(sql, Record.class);
    }
    /**
     * 根据申请时间范围和部门分页查找数据列表
     *  @param startdate
     *  @param enddate
     *  @param ouguid
     * @param pageSize 
     * @param first 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> findPageList(String startdate, String enddate, String ouguid , int first, int pageSize) {
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        String[] ouguids ;
        String ousql ;
        String ounameSql = "";
        String strstart;
        String str;
        String strEnd;
        //拼接部门查询条件sql
        if (StringUtil.isNotBlank(ouguid)) {
              ouguids = ouguid.split(",");
               strstart ="(";
               str ="";
               strEnd =")";
              for (int i = 0; i < ouguids.length; i++) {
                  String ouName = ouService.getOuByOuGuid(ouguids[i]).getOuname();
                  ousql = "'"+ouName+"'";
                  if (i!=ouguids.length-1) {
                      str += ousql+",";
                  }else {
                      str += ousql;
                }
                  
            }
               ounameSql = strstart+str+strEnd;
        }
        String sql = "";
               sql = " select\r\n" + 
                       "    ouname,\r\n" + 
                       "    ouguid,\r\n" + 
                       "    sum(ct2) AS 有办件的依申请事项,\r\n" + 
                       "    sum(ct3) AS 办件办结量,\r\n" + 
                       "    sum(ct4) AS 外网申报数,\r\n" + 
                       "    sum(ct5) AS 外网申报率,\r\n" + 
                       "    sum(ct6) AS 外网申报未受理数,\r\n" + 
                       "    sum(ct7) AS 按期办理数,\r\n" + 
                       "    sum(ct8) AS 办件按期办结率\r\n" + 
                       "FROM\r\n" + 
                       "    (\r\n" + 
                       "        SELECT\r\n" + 
                       "                ouname,\r\n" + 
                       "                ouguid,\r\n" + 
                       "                ct2, \r\n" + 
                       "                ct3,\r\n" + 
                       "                ct4,\r\n" + 
                       "                ct5,\r\n" + 
                       "                ct6,\r\n" + 
                       "                ct7,\r\n" + 
                       "                ct8\r\n" + 
                       "        FROM\r\n" + 
                       "            (\r\n" + 
                       "            select \r\n" + 
                       "            b.ouname,\r\n" + 
                       "            b.ouguid,\r\n" + 
                       "            SUM(CASE WHEN  businesstype = '1' THEN 1 ELSE 0 END) ct2,\r\n" + 
                       "            SUM(CASE WHEN  b.status = '90' THEN 1 ELSE 0 END) ct3,\r\n" + 
                       "            SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END) ct4,   \r\n" + 
                       "            SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END)/SUM(CASE WHEN b.rowguid is not null THEN 1 ELSE 0 END) as ct5,\r\n" + 
                       "            SUM(CASE WHEN  b.status = '12' THEN 1 ELSE 0 END) as ct6,\r\n" + 
                       "            SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct7,\r\n" + 
                       "            SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END)/SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct8\r\n" + 
                       "            from audit_task a  join audit_project b on a.rowguid = b.taskguid \r\n" + 
                       "            where  is_enable = '1' and a.businesstype = '1' and is_editafterimport = '1' and ifnull(IS_HISTORY,0) = 0 and b.ouname is not null " ;
               if (!"370800".equals(areacode)) {
                   sql+= " and b.`AREACODE` = '"+areacode+"' ";
               }
               if (StringUtil.isNotBlank(startdate)&&StringUtil.isNotBlank(enddate)) {
                   sql+= " and ( b.`APPLYDATE` >= '"+startdate+"' and b.`APPLYDATE` <= '"+enddate+"' ) ";
               }else if (StringUtil.isNotBlank(startdate)&&StringUtil.isBlank(enddate)) {
                   sql+= " and  b.`APPLYDATE` >= '"+startdate+"'  ";
              }else if (StringUtil.isBlank(startdate)&&StringUtil.isNotBlank(enddate)) {
                  sql+= " and  b.`APPLYDATE` <= '"+enddate+"'  ";
             }
               sql+=
                       "            group by b.ouname\r\n" + 
                       "            ) a\r\n" + 
                       "        UNION ALL\r\n" + 
                       "            select\r\n" + 
                       "                ouname,\r\n" + 
                       "                ouguid,\r\n" + 
                       "                ct2,\r\n" + 
                       "                ct3,\r\n" + 
                       "                ct4,\r\n" + 
                       "                ct5,\r\n" + 
                       "                ct6,\r\n" + 
                       "                ct7,\r\n" + 
                       "                ct8\r\n" + 
                       "\r\n" + 
                       "            FROM\r\n" + 
                       "                (\r\n" + 
                       "                select \r\n" + 
                       "                b.ouname,\r\n" + 
                       "                b.ouguid,\r\n" + 
                       "                count(1) ct2,\r\n" + 
                       "                SUM(CASE WHEN  b.status = '90' THEN 1 ELSE 0 END) ct3,\r\n" + 
                       "                SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END) ct4,   \r\n" + 
                       "                SUM(CASE WHEN  b.applyway in ('10','11') THEN 1 ELSE 0 END)/SUM(CASE WHEN b.rowguid is not null THEN 1 ELSE 0 END) as ct5,\r\n" + 
                       "                SUM(CASE WHEN  b.status = '12' THEN 1 ELSE 0 END) as ct6,\r\n" + 
                       "                SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct7,\r\n" + 
                       "                SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END)/SUM(CASE WHEN b.banjiedate is not null and b.PROMISEENDDATE is not null and PROMISEENDDATE > banjiedate THEN 1 ELSE 0 END) ct8\r\n" + 
                       "                from audit_task a  join lc_project b on a.rowguid = b.taskguid \r\n" + 
                       "                where  is_enable = '1' and a.businesstype = '1' and is_editafterimport = '1' and ifnull(IS_HISTORY,0) = 0 and b.ouname is not null\r\n" ;
               if (!"370800".equals(areacode)) {
                   sql+= " and b.`AREACODE` = '"+areacode+"' ";
               }
               sql+=
                       "                group by b.ouname\r\n" + 
                       "\r\n" + 
                       "                ) b\r\n" + 
                       "    ) dd\r\n" ;
                 if (StringUtil.isNotBlank(ouguid)) {
                     sql+= " where dd.ouname in  " +ounameSql;
                }
                  sql+=
                       " GROUP BY\r\n" + 
                       "    dd.ouname "+
                        " LIMIT "+first+ ","+ pageSize;
        return baseDao.findList(sql, Record.class);
    }



    public List<String> getAllAreacode() {
        String sql = " select  XiaQuCode from audit_orga_area where length(`XiaQuCode`) = 6  ";
        return baseDao.findList(sql, String.class);
    }
    
    
    public Record getTaskCountByouguid(String ouguid) {
        String sql = " select count(1) as total from audit_task b where  is_enable = '1' and b.businesstype = '1' and is_editafterimport = '1' and ifnull(IS_HISTORY,0) = 0 and b.ouname is not null and ouguid = ?  ";
        return baseDao.find(sql, Record.class,ouguid);
    }
    
   
}
