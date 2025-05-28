package com.epoint.jnrestforyanglao;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;

public interface IJnYangLaoService extends Serializable
{  
    public List<Record> getYangLaoList(int pageIndex,int pageSize);
    
    public List<Record> getYangLaoListByName(String cname,int pageIndex,int pageSize);
    
    public int getCount();

    public int getCountByCName(String cname);

    public Record findByPk(String pk_old_agency);

}
