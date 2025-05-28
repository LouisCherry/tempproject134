package com.epoint.jn.zndf.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

public class JnZndfService
{
    public List<Record> getTaskNameByPos(String pos){
        ICommonDao dao=CommonDao.getInstance(); 
        String sql="select a.TASKGUID ,a.taskname from audit_znsb_centertask a ,audit_orga_windowtask b ,audit_orga_window c where b.rowguid = a.taskguid and b.WINDOWGUID = c.RowGuid and c.WINDOWNO='"+pos+"'";
         return dao.findList(sql, Record.class, pos);
     }
    
    public String getWindowByTaskguid(String taskguid){
        ICommonDao dao=CommonDao.getInstance(); 
        String window="";
        String sql="select a.WINDOWNO from audit_orga_window a,AUDIT_ORGA_WINDOWTASK b where a.rowguid=b.WINDOWGUID and a.IS_USEQUEUE=1 and b.taskguid='"+taskguid+"'";
        List<String> bhList=dao.findList(sql, String.class);
        if(bhList!=null && bhList.size()>0){
            window=StringUtil.join(bhList, ",");
        }
        return window;
    }
    public String getNumByMac(String mac){
        ICommonDao dao=CommonDao.getInstance(); 
        String sql="SELECT num from audit_znsb_equipment WHERE MACADDRESS=?1";
        return dao.find(sql, String.class, mac);
    }
    public List<Record> getWindowNameByLobby(String lobbytype){
        ICommonDao dao=CommonDao.getInstance(); 
        String condition ="";
        switch (lobbytype) {
            //四楼
        case "009da827-a73c-42ef-853b-ffde53a1311b" :
            
        case "b3a6f2c3-ceb8-4c1c-a168-66cbc93fed9a" : 
            condition =" LOBBYTYPE in  ('009da827-a73c-42ef-853b-ffde53a1311b','b3a6f2c3-ceb8-4c1c-a168-66cbc93fed9a')";
            break;
            //三楼
        case "3f11bd9f-550e-454c-8e02-1316f1f3d23c" :
            
        case "3c56f8cc-9a36-4a49-bccd-716b83dfa5d7" : 
            condition =" LOBBYTYPE in  ('3f11bd9f-550e-454c-8e02-1316f1f3d23c','3c56f8cc-9a36-4a49-bccd-716b83dfa5d7')";
         
            break;
            //二楼
        case "f155b358-22b7-40b0-9c84-d5444448502a" :
        case "a6faf82d-328b-41b1-b237-29a891682389" :    
        case "0e506bc8-341a-4399-8d96-3488de63c175" : 
            condition =" LOBBYTYPE in  ('f155b358-22b7-40b0-9c84-d5444448502a','0e506bc8-341a-4399-8d96-3488de63c175','a6faf82d-328b-41b1-b237-29a891682389')";
            break;
            //一楼
        case "63a5595b-fcd0-4b5b-a060-693c671ddd2c" :
       
        case "10fc66de-bfd4-4d96-828b-cbdbd3af9ade" : 
            condition =" LOBBYTYPE in  ('63a5595b-fcd0-4b5b-a060-693c671ddd2c','10fc66de-bfd4-4d96-828b-cbdbd3af9ade')";
      
            break;
        default: 
            condition ="LOBBYTYPE='"+lobbytype+"'";
    }
        String sql="SELECT windowno,indicating,portno from audit_orga_window WHERE  "+condition+"  and windowtype='10' order by windowno asc";
        return dao.findList(sql, Record.class);
    }

}
