package com.epoint.jnrestforyanglao;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class JnYangLaoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;
    
    public JnYangLaoService() {
        baseDao = CommonDao.getInstance();
    }
    
    public List<Record> getYangLaoList(int pageIndex,int pageSize){
        String sql="select pk_old_agency,cname,addr,legal_person,reg_name,tel_phone from yljg1 order by morg_area_code ";
        return baseDao.findList(sql, pageIndex*pageSize, pageSize, Record.class);
    }
    
    public List<Record> getYangLaoListByName(String cname,int pageIndex,int pageSize){
        String sql="select pk_old_agency,cname,addr,legal_person,reg_name,tel_phone from yljg1 where cname like ? order by morg_area_code ";
        String selectName="%"+cname+"%";
        return baseDao.findList(sql, pageIndex*pageSize, pageSize, Record.class, selectName);
    }
    
    public int getCount() {
        String sql="select count(*) from yljg1 ";
        return baseDao.queryInt(sql);
    }

    public int getCountByCName(String cname) {
        String sql="select count(*) from yljg1 where cname like ? ";
        String selectName="%"+cname+"%";
        return baseDao.queryInt(sql, selectName);
    }

    public Record findByPk(String pk_old_agency) {
        String sql="select pk_old_agency,cname,addr,legal_person,reg_name,tel_phone from yljg1 where pk_old_agency=? ";
        return baseDao.find(sql, Record.class, pk_old_agency);
    }
}
