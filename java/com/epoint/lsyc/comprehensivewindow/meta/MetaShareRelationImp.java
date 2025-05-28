package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

@Component
@Service
public class MetaShareRelationImp implements IMetaShareRelation {
    private static final long serialVersionUID = 1L;
    private ICommonDao ibaseDao;

    public MetaShareRelationImp() {
        ibaseDao = CommonDao.getInstance();
    }

    @Override
    public PageData<MetaShareRelation> getMetaShareRelationList(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        return (new DBServcie()).getListByPage(MetaShareRelation.class, conditionMap, Integer.valueOf(first), Integer.valueOf(pageSize), sortField, sortOrder);
    }

    @Override
    public int insertRecord(MetaShareRelation relation) {
        return ibaseDao.insert(relation);
    }

    @Override
    public MetaShareRelation getMetaShareRelation(String rowguid) {
        return ibaseDao.find(MetaShareRelation.class, rowguid);
    }

    @Override
    public void delete(MetaShareRelation relation) {
        ibaseDao.delete(relation);
    }

    @Override
    public int update(MetaShareRelation relation) {
        return ibaseDao.update(relation);
    }
    
    public List<MetaShareRelation> getMetaShareRelationListByCondition(Map<String, String> map) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(MetaShareRelation.class);
        if(sqlManageUtil.getListByCondition(MetaShareRelation.class, map)==null){
            return new ArrayList<>(); 
        }
        return sqlManageUtil.getListByCondition(MetaShareRelation.class, map);
    }

    @Override
    public int getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(String businessguid, String phaseguid, String shareguid) {
        return ibaseDao.queryInt("select count(1) from metasharerelation where metaspguid in"
                + "(select rowguid from AUDIT_RS_SHARE_METADATA_SP where businessGuid=? and phaseguid=?) and shareguid=?",
                businessguid, phaseguid, shareguid);
    }
}
