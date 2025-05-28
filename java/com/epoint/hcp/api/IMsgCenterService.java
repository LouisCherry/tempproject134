package com.epoint.hcp.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;


public interface IMsgCenterService extends Serializable
{

   
    public List<Record> getMsgList(String areacode);

  
    public void insert(String rowguid,String content,String mobile,String areacode,String add_time);
    
    public void update(String rowguid);
}
