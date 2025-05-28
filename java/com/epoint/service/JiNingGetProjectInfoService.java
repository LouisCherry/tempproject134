package com.epoint.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangjia 集中生产
 * @create 2021-06-29 11:02
 */
@Service
public class JiNingGetProjectInfoService {
    public List<Record> getProjectInfo(String businessGuid,String taskIDs,String itemName,String itemCode,String pageNo,String pageSize) {
       String sql = "";
       if (StringUtil.isBlank(itemName)){
           itemName = "";
       }
       if (StringUtil.isNotBlank(itemCode)){
           if(CommonDao.getInstance().isMySql()) {
               sql = "SELECT * " +
                       "FROM (" +
                       "SELECT it.BIGUID " +
                       "FROM (" +
                       "SELECT BIGUID, TASKGUID " +
                       "FROM audit_sp_i_task " +
                       ") it, (" +
                       "SELECT RowGuid, TASK_ID " +
                       "FROM AUDIT_TASK " +
                       "WHERE IS_EDITAFTERIMPORT = 1 " +
                       "AND IS_ENABLE = 1 " +
                       "AND IFNULL(IS_HISTORY, 0) = 0 " +
                       ") t" +
                       " WHERE it.TASKGUID = t.RowGuid " +
                       "GROUP BY it.BIGUID" +
                       ") bi " +
                       "INNER JOIN (" +
                       "SELECT ITEMNAME, ITEMCODE, ITEMTYPE, CONSTRUCTIONPROPERTY, ITEMLEGALDEPT" +
                       ", ITEMLEGALCERTTYPE, ITEMLEGALCREDITCODE, XMTZLY, ITEMSTARTDATE, ITEMFINISHDATE" +
                       ", TOTALINVEST, BELONGTINDUSTRY, LEGALPROPERTY, CONTRACTPERSON, CONTRACTPHONE" +
                       ", TDHQFS, ISIMPROVEMENT, TDSFDSJFA, SFWCQYPG, GBHY" +
                       ", XMZJSX, LANDAREA, JZMJ, CONSTRUCTIONSITE, CONSTRUCTIONSITEDESC" +
                       ", CONSTRUCTIONSCALEANDDESC, BIGUID, lxpfcliengguid, jsgcfliengguid " +
                       "FROM AUDIT_RS_ITEM_BASEINFO " +
                       "WHERE ifnull(IS_HISTORY, 0) = 0 and lxpfcliengguid is not null and jsgcfliengguid is not null " +
                       ") item " +
                       "ON item.BIGUID = bi.BIGUID where item.ITEMNAME like '%" + itemName + "%' and item.ITEMCODE ='" +itemCode + "' limit " + pageNo +"," + pageSize;
           }
          
       }
       else{
           if(CommonDao.getInstance().isMySql()) {
               sql = "SELECT * " +
                       "FROM (" +
                       "SELECT it.BIGUID " +
                       "FROM (" +
                       "SELECT BIGUID, TASKGUID " +
                       "FROM audit_sp_i_task " +
                       ") it, (" +
                       "SELECT RowGuid, TASK_ID " +
                       "FROM AUDIT_TASK " +
                       "WHERE IS_EDITAFTERIMPORT = 1 " +
                       "AND IS_ENABLE = 1 " +
                       "AND IFNULL(IS_HISTORY, 0) = 0 " +
                       ") t" +
                       " WHERE it.TASKGUID = t.RowGuid " +
                       "GROUP BY it.BIGUID" +
                       ") bi " +
                       "INNER JOIN (" +
                       "SELECT ITEMNAME, ITEMCODE, ITEMTYPE, CONSTRUCTIONPROPERTY, ITEMLEGALDEPT" +
                       ", ITEMLEGALCERTTYPE, ITEMLEGALCREDITCODE, XMTZLY, ITEMSTARTDATE, ITEMFINISHDATE" +
                       ", TOTALINVEST, BELONGTINDUSTRY, LEGALPROPERTY, CONTRACTPERSON, CONTRACTPHONE" +
                       ", TDHQFS, ISIMPROVEMENT, TDSFDSJFA, SFWCQYPG, GBHY" +
                       ", XMZJSX, LANDAREA, JZMJ, CONSTRUCTIONSITE, CONSTRUCTIONSITEDESC" +
                       ", CONSTRUCTIONSCALEANDDESC, BIGUID, lxpfcliengguid, jsgcfliengguid " +
                       "FROM AUDIT_RS_ITEM_BASEINFO " +
                       "WHERE ifnull(IS_HISTORY, 0) = 0 and lxpfcliengguid is not null and jsgcfliengguid is not null" +
                       ") item " +
                       "ON item.BIGUID = bi.BIGUID where item.ITEMNAME like '%" + itemName + "%'  limit " + pageNo +"," + pageSize;
           }
       }

        return CommonDao.getInstance().findList(sql, Record.class);
    }
    
    
    public int getCountProjectInfo(String businessGuid,String taskIDs,String itemName,String itemCode) {
        String sql = "";
        if (StringUtil.isBlank(itemName)){
            itemName = "";
        }
        if (StringUtil.isNotBlank(itemCode)){
            if(CommonDao.getInstance().isMySql()) {
                sql = "SELECT count(1) as total " +
                        "FROM (" +
                        "SELECT it.BIGUID " +
                        "FROM (" +
                        "SELECT BIGUID, TASKGUID " +
                        "FROM audit_sp_i_task " +
                        ") it, (" +
                        "SELECT RowGuid, TASK_ID " +
                        "FROM AUDIT_TASK " +
                        "WHERE IS_EDITAFTERIMPORT = 1 " +
                        "AND IS_ENABLE = 1 " +
                        "AND IFNULL(IS_HISTORY, 0) = 0 " +
                        ") t" +
                        " WHERE it.TASKGUID = t.RowGuid " +
                        "GROUP BY it.BIGUID" +
                        ") bi " +
                        "INNER JOIN (" +
                        "SELECT ITEMNAME, ITEMCODE, ITEMTYPE, CONSTRUCTIONPROPERTY, ITEMLEGALDEPT" +
                        ", ITEMLEGALCERTTYPE, ITEMLEGALCREDITCODE, XMTZLY, ITEMSTARTDATE, ITEMFINISHDATE" +
                        ", TOTALINVEST, BELONGTINDUSTRY, LEGALPROPERTY, CONTRACTPERSON, CONTRACTPHONE" +
                        ", TDHQFS, ISIMPROVEMENT, TDSFDSJFA, SFWCQYPG, GBHY" +
                        ", XMZJSX, LANDAREA, JZMJ, CONSTRUCTIONSITE, CONSTRUCTIONSITEDESC" +
                        ", CONSTRUCTIONSCALEANDDESC, BIGUID, lxpfcliengguid, jsgcfliengguid " +
                        "FROM AUDIT_RS_ITEM_BASEINFO " +
                        "WHERE ifnull(IS_HISTORY, 0) = 0 and lxpfcliengguid is not null and jsgcfliengguid is not null " +
                        ") item " +
                        "ON item.BIGUID = bi.BIGUID where item.ITEMNAME like '%" + itemName + "%' and item.ITEMCODE ='" +itemCode ;
            }
           
        }
        else{
            if(CommonDao.getInstance().isMySql()) {
                sql = "SELECT count(1) as total " +
                        "FROM (" +
                        "SELECT it.BIGUID " +
                        "FROM (" +
                        "SELECT BIGUID, TASKGUID " +
                        "FROM audit_sp_i_task " +
                        ") it, (" +
                        "SELECT RowGuid, TASK_ID " +
                        "FROM AUDIT_TASK " +
                        "WHERE IS_EDITAFTERIMPORT = 1 " +
                        "AND IS_ENABLE = 1 " +
                        "AND IFNULL(IS_HISTORY, 0) = 0 " +
                        ") t" +
                        " WHERE it.TASKGUID = t.RowGuid " +
                        "GROUP BY it.BIGUID" +
                        ") bi " +
                        "INNER JOIN (" +
                        "SELECT ITEMNAME, ITEMCODE, ITEMTYPE, CONSTRUCTIONPROPERTY, ITEMLEGALDEPT" +
                        ", ITEMLEGALCERTTYPE, ITEMLEGALCREDITCODE, XMTZLY, ITEMSTARTDATE, ITEMFINISHDATE" +
                        ", TOTALINVEST, BELONGTINDUSTRY, LEGALPROPERTY, CONTRACTPERSON, CONTRACTPHONE" +
                        ", TDHQFS, ISIMPROVEMENT, TDSFDSJFA, SFWCQYPG, GBHY" +
                        ", XMZJSX, LANDAREA, JZMJ, CONSTRUCTIONSITE, CONSTRUCTIONSITEDESC" +
                        ", CONSTRUCTIONSCALEANDDESC, BIGUID, lxpfcliengguid, jsgcfliengguid " +
                        "FROM AUDIT_RS_ITEM_BASEINFO " +
                        "WHERE ifnull(IS_HISTORY, 0) = 0 and lxpfcliengguid is not null and jsgcfliengguid is not null" +
                        ") item " +
                        "ON item.BIGUID = bi.BIGUID where item.ITEMNAME like '%" + itemName + "%' ";
            }
        }

         return CommonDao.getInstance().queryInt(sql, Record.class);
     }
    
    
}
