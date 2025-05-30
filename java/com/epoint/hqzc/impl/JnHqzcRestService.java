package com.epoint.hqzc.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

@Component
@Service
public class JnHqzcRestService
{
    private ICommonDao commonDao;
    
    public JnHqzcRestService() {
        commonDao = CommonDao.getInstance();
    }

    public List<Record> selectOuList(String areacode) {
        String sql = "select distinct a.ssbm,b.ouname from epoint_sp_hmzc a join frame_ou b on a.ssbm = b.ouguid join frame_ou_extendinfo c on c.ouguid = a.ssbm where c.areacode = ?";
        return commonDao.findList(sql, Record.class,areacode);
    }
    
    public List<Record> getHyflList() {
    	String sql = "select itemtext,itemvalue from code_items where codeid = '1015961'";
        return commonDao.findList(sql, Record.class);
    }
    
    public List<Record> getHygmList() {
        String sql = "select itemtext,itemvalue from code_items where codeid = '1015959'";
        return commonDao.findList(sql, Record.class);
    }
    
    public List<Record> getSmzqList() {
        String sql = "select itemtext,itemvalue from code_items where codeid = '1015960'";
        return commonDao.findList(sql, Record.class);
    }
    
    
    public List<Record> getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy, int pageIndex, int pageSize, String ouguids) {
        String sql = "select a.rowguid,a.zcmc,a.xztj,a.zcld,a.cengji,b.ouname from epoint_sp_hmzc a join frame_ou b on a.ssbm = b.ouguid where 1=1 ";
        if (StringUtil.isNotBlank(ouguids)) {
            sql += " and a.ssbm in "+ouguids+" ";
        }
        if (StringUtil.isNotBlank(ssbm) && !"全部".equals(ssbm) && !"0".equals(ssbm)) {
            sql += " and a.ssbm = '"+ssbm+"'";
        }
        if (StringUtil.isNotBlank(qybq) && !"全部".equals(qybq) && !"0".equals(qybq)) {
            sql += " and a.qybq like '%"+qybq+"%'";
        }
        if (StringUtil.isNotBlank(jnhygm) && !"全部".equals(jnhygm) && !"0".equals(jnhygm)) {
            sql += " and a.jnhygm like '%"+jnhygm+"%'";
        }
        if (StringUtil.isNotBlank(wwsmzq) && !"全部".equals(wwsmzq) && !"0".equals(wwsmzq)) {
            sql += " and a.wwsmzq like '%"+wwsmzq+"%'";
        }
        if (StringUtil.isNotBlank(sfsxqy) && !"全部".equals(sfsxqy) && !"0".equals(sfsxqy)) {
            sql += " and a.sfsxqy = '"+sfsxqy+"'";
        }
        return commonDao.findList(sql, pageIndex, pageSize, Record.class);
    }
    
    public Record getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy,String ouguids) {
        String sql = "select count(1) as total from epoint_sp_hmzc a join frame_ou b on a.ssbm = b.ouguid where 1=1 ";
        if (StringUtil.isNotBlank(ouguids)) {
            sql += " and a.ssbm in "+ouguids+" ";
        }
        if (StringUtil.isNotBlank(ssbm) && !"全部".equals(ssbm) && !"0".equals(ssbm)) {
            sql += " and a.ssbm = '"+ssbm+"'";
        }
        if (StringUtil.isNotBlank(qybq) && !"全部".equals(qybq) && !"0".equals(qybq)) {
            sql += " and a.qybq like '%"+qybq+"%'";
        }
        if (StringUtil.isNotBlank(jnhygm) && !"全部".equals(jnhygm) && !"0".equals(jnhygm)) {
            sql += " and a.jnhygm like '%"+jnhygm+"%'";
        }
        if (StringUtil.isNotBlank(wwsmzq) && !"全部".equals(wwsmzq) && !"0".equals(wwsmzq)) {
            sql += " and a.wwsmzq like '%"+wwsmzq+"%'";
        }
        if (StringUtil.isNotBlank(sfsxqy) && !"全部".equals(sfsxqy) && !"0".equals(sfsxqy)) {
            sql += " and a.sfsxqy = '"+sfsxqy+"'";
        }
        return  commonDao.find(sql,  Record.class);
    }
    
    
    public List<Record> getPolicyListByContionFirst(String str, int pageIndex, int pageSize) {
        String sql = "select a.rowguid,a.zcmc,a.xztj,a.zcld,a.cengji,b.ouname from epoint_sp_hmzc a join frame_ou b on a.ssbm = b.ouguid where 1=1 and ( a.zcld like '%"+str+"%' or a.zcmc like '%"+str+"%' or a.xztj like '%"+str+"%')";
        return commonDao.findList(sql, pageIndex, pageSize, Record.class);
    }
    
    public Record getPolicyDetailByRowguid(String rowguid) {
        String sql = "select a.zcmc,a.sbrq,a.zxks,a.bgdd,a.zxdh,a.zcyxq,a.jddh,a.xztj,a.zcld,a.bslc,b.ouname,a.zczn,a.ssxz from epoint_sp_hmzc a join frame_ou b on a.ssbm = b.ouguid  where a.rowguid = ?";
        return commonDao.find(sql, Record.class, rowguid);
    }
    
}
