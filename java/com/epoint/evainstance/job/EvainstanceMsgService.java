package com.epoint.evainstance.job;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

public class EvainstanceMsgService
{

    /**
     * 
     *  [查询大于24小时并且未评价数据]
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static List<Record> getKhkUser() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT\r\n" + 
                "    fur.USERGUID,fou.OUGUID,DISPLAYNAME,AREACODE\r\n" + 
                "FROM\r\n" + 
                "    frame_user fur\r\n" + 
                "INNER JOIN (\r\n" + 
                "    SELECT\r\n" + 
                "        USERGUID\r\n" + 
                "    FROM\r\n" + 
                "        frame_userrolerelation\r\n" + 
                "    WHERE\r\n" + 
                "        roleguid = (\r\n" + 
                "            SELECT\r\n" + 
                "                roleguid\r\n" + 
                "            FROM\r\n" + 
                "                frame_role\r\n" + 
                "            WHERE\r\n" + 
                "                rolename = '考核管理员'\r\n" + 
                "        )\r\n" + 
                ") fule ON fur.userguid = fule.USERGUID\r\n" + 
                "\r\n" + 
                "INNER JOIN\r\n" + 
                "frame_ou_extendinfo fou \r\n" + 
                "on fou.OUGUID = fur.OUGUID\r\n" + 
                "where AREACODE is not null";
        List<Record> messagelist = dao.findList(sql, Record.class);
        dao.close();
        return messagelist;
    }
    
    public static List<Record> getEvaList(){
        CommonDao dao = CommonDao.getInstance();
        String sql  = "select areacode,taskname,username,createdate,rowguid from evainstance where satisfaction<3 and createdate>='2020-11-11 00:00:00' and is_tip is null ";
        List<Record> messagelist = dao.findList(sql, Record.class);
        dao.close();
        return messagelist;
    }
    
    public static void updateEvaMsg(String rowguid) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "update evainstance set is_tip = '1' where rowguid=?";
        dao.execute(sql, rowguid);
        dao.close();
    }
}
