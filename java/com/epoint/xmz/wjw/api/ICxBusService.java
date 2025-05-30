package com.epoint.xmz.wjw.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;

/**
 * 车辆信息表对应的后台service接口
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public interface ICxBusService extends Serializable
{ 
   
    public  Record getHdzzl10tyszxhcByRowguid(String projectguid);
    
    public  Record getHdzzl5tys10tyxzxhcByRowguid(String projectguid);
    
    public  Record getZxhcByRowguid(String projectguid);

    public Record getWxpyscByRowguid(String projectguid);
    
    public Record getCsgccByRowguid(String projectguid);
    
    public Record getQxhcByRowguid(String projectguid);
    
    public Record getWlyyczqcdlyszhfByRowguid(String projectguid);
    
    public List<Record> getWlyyczqcdlyszhfzbByRowguid(String projectguid);
    
    public Record getXyczqcdlyszbfByRowguid(String projectguid);
    
    public Record getHwcyzgsqByRowguid(String projectguid);
    
    public Record getLkcyzgsqByRowguid(String projectguid);
    
    public Record getDlkyjyxkByRowguid(String projectguid);
    
    public Record getWhjsycyzgxkByRowguid(String projectguid);
    
    public Record getDlwxhwysyyrycyzgxkByRowguid(String projectguid);
    
    public Record getWlyyczcjsycyzgxkByRowguid(String projectguid);
    
    public Record getSlysqyhzbzsqByRowguid(String xkzbh, String qymc);
    
    public Record getShttzsbgdjByRowguid(String projectguid);
    
    public Record getPorjectByRowguid(String tablename,String projectguid);
    
	public Record getDzbdDetail(String tableName, String rowguid);
	
	public int insert(Record record);
	
	public int update(Record record);

}
