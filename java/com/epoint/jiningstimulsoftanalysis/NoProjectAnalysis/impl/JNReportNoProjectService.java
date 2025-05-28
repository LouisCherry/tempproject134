package com.epoint.jiningstimulsoftanalysis.NoProjectAnalysis.impl;
import java.util.ArrayList;
import java.util.List;

import com.epoint.common.util.StringUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 
 * @author oliver
 */
public class JNReportNoProjectService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JNReportNoProjectService() {
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
        String guid = "";
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
                  ousql = "'"+ouguids[i]+"'";
                  if (i!=ouguids.length-1) {
                      str += ousql+",";
                  }else {
                      str += ousql;
                }
            }
               guid = strstart+str+strEnd;
        }
        String sql = "";
        if (StringUtil.isBlank(guid)) {
            sql =  "  select  t.item_id,t.taskname,t.ouname,t.ouguid from(\r\n" + 
                    "select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (\r\n" + 
                    "  select DISTINCT t.ITEM_ID\r\n" + 
                    "          from audit_project a\r\n" + 
                    "          right join audit_task t on a.taskguid=t.RowGuid\r\n" + 
                    "          where a.AREACODE='"+areacode+"'\r\n" + 
                    "          AND a.APPLYDATE >'2021-01-01 00:00:00' \r\n" + 
                    "          AND a.`STATUS`='90'\r\n" + 
                    "          and t.businesstype='1'\r\n" + 
                    "          GROUP BY t.ITEM_ID\r\n" + 
                    "          ) and s.areacode = '"+areacode+"' and foe.areacode = '"+areacode+"'\r\n" + 
                    "          \r\n" + 
                    " union \r\n" + 
                    " \r\n" + 
                    "   select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (\r\n" + 
                    " \r\n" + 
                    " select itemid as ITEM_ID from old_itemid  where AREACODE='"+areacode+"'\r\n"+
                    " union \r\n" + 
                    "     select DISTINCT t.ITEM_ID \r\n" + 
                    "          from lc_project a\r\n" + 
                    "          join audit_task t on a.taskguid=t.RowGuid\r\n" + 
                    "          where a.AREACODE='"+areacode+"'\r\n" + 
                    "          AND a.APPLYDATE >'2021-01-01 00:00:00' \r\n" + 
                    "          AND a.`STATUS`='90'\r\n" + 
                    "          and t.businesstype='1'\r\n" + 
                    "          GROUP BY t.ITEM_ID\r\n" + 
                    "           ) and s.areacode = '"+areacode+"'  and foe.areacode = '"+areacode+"'\r\n" + 
                    "         ) t" + 
                    "  ";
        }else {
            sql =  "   select  t.item_id,t.taskname,t.ouname,t.ouguid from(" + 
                    "select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                    "  select DISTINCT t.ITEM_ID" + 
                    "          from audit_project a" + 
                    "          right join audit_task t on a.taskguid=t.RowGuid" + 
                    "          where a.AREACODE='"+areacode+"'" + 
                    "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                    "          AND a.`STATUS`='90'" + 
                    "          and t.businesstype='1'" + 
                    "          GROUP BY t.ITEM_ID" + 
                    "          ) and s.areacode = '"+areacode+"' and foe.areacode = '"+areacode+"'" + 
                    "          " + 
                    " union " + 
                    "   select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                    " select itemid as ITEM_ID from old_itemid  where AREACODE='"+areacode+"'"+
                    " union" + 
                    " " + 
                    " " + 
                    "     select DISTINCT t.ITEM_ID " + 
                    "          from lc_project a" + 
                    "          join audit_task t on a.taskguid=t.RowGuid" + 
                    "          where a.AREACODE='"+areacode+"'" + 
                    "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                    "          AND a.`STATUS`='90'" + 
                    "          and t.businesstype='1'" + 
                    "          GROUP BY t.ITEM_ID" + 
                    "           ) and s.areacode = '"+areacode+"'  and foe.areacode = '"+areacode+"'" + 
                    "         ) t " + 
                    "      where t.`OUGUID` in "+guid+"" ;
        }
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
        String guid = "";
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
                  ousql = "'"+ouguids[i]+"'";
                  if (i!=ouguids.length-1) {
                      str += ousql+",";
                  }else {
                      str += ousql;
                }
                  
            }
               guid = strstart+str+strEnd;
        }
        String sql = "";
            if (StringUtil.isBlank(guid)) {
                sql =  " select s.item_id,s.taskname,s.ouname from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                        "  select DISTINCT t.ITEM_ID" + 
                        "          from audit_project a" + 
                        "          right join audit_task t on a.taskguid=t.RowGuid" + 
                        "          where a.AREACODE='"+areacode+"'" + 
                        "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                        "          AND a.`STATUS`='90'" + 
                        "          and t.businesstype='1'" + 
                        "          GROUP BY t.ITEM_ID" + 
                        "          ) and s.areacode = '"+areacode+"' and foe.areacode = '"+areacode+"'" + 
                        "          " + 
                        " union " + 
                        "   select s.item_id,s.taskname,s.ouname from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                        " " + 
                        "     select DISTINCT t.ITEM_ID " + 
                        "          from lc_project a" + 
                        "          join audit_task t on a.taskguid=t.RowGuid" + 
                        "          where a.AREACODE='"+areacode+"'" + 
                        "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                        "          AND a.`STATUS`='90'" + 
                        "          and t.businesstype='1'" + 
                        "          GROUP BY t.ITEM_ID" + 
                        "           ) and s.areacode = '"+areacode+"'  and foe.areacode = '"+areacode+"'" + 
                        " " + 
                        " LIMIT "+first+ ","+ pageSize;
            }else {
                sql =  "  select  t.item_id,t.taskname,t.ouname,t.ouguid from(" + 
                        "select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                        "  select DISTINCT t.ITEM_ID" + 
                        "          from audit_project a" + 
                        "          right join audit_task t on a.taskguid=t.RowGuid" + 
                        "          where a.AREACODE='"+areacode+"'" + 
                        "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                        "          AND a.`STATUS`='90'" + 
                        "          and t.businesstype='1'" + 
                        "          GROUP BY t.ITEM_ID" + 
                        "          ) and s.areacode = '"+areacode+"' and foe.areacode = '"+areacode+"'" + 
                        "          " + 
                        " union " + 
                        "   select s.item_id,s.taskname,s.ouname,s.`OUGUID` from audit_task s join frame_ou_extendinfo foe on s.ouguid = foe.ouguid where s.item_id  not in (" + 
                        " " + 
                        "     select DISTINCT t.ITEM_ID " + 
                        "          from lc_project a" + 
                        "          join audit_task t on a.taskguid=t.RowGuid" + 
                        "          where a.AREACODE='"+areacode+"'" + 
                        "          AND a.APPLYDATE >'2021-01-01 00:00:00' " + 
                        "          AND a.`STATUS`='90'" + 
                        "          and t.businesstype='1'" + 
                        "          GROUP BY t.ITEM_ID" + 
                        "           ) and s.areacode = '"+areacode+"'  and foe.areacode = '"+areacode+"'" + 
                        "         ) t" + 
                        "      where t.`OUGUID` in "+guid+"  " + 
                        " LIMIT "+first+ ","+ pageSize;
            }
            
        
        return baseDao.findList(sql, Record.class);
    }
    
   
}
