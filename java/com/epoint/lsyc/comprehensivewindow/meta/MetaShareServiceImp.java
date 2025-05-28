package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShare;

@Component
@Service
public class MetaShareServiceImp implements IMetaShare
{
    private static final long serialVersionUID = 1L;
    private ICommonDao ibaseDao;

    public MetaShareServiceImp() {
        ibaseDao = CommonDao.getInstance();
    }

    @Override
    public PageData<MetaShare> getMetaShareList(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        return (new DBServcie()).getListByPage(MetaShare.class, conditionMap, Integer.valueOf(first),
                Integer.valueOf(pageSize), sortField, sortOrder);
    }

    @Override
    public int insertRecord(MetaShare metashare) {
        return ibaseDao.insert(metashare);
    }

    @Override
    public MetaShare getMetaShare(String rowguid) {
        return ibaseDao.find(MetaShare.class, rowguid);
    }

    @Override
    public void delete(MetaShare metaShare) {
        ibaseDao.delete(metaShare);
        
    }

    @Override
    public List<AuditRsShareMetadata> selectAuditRsShareMetadataByCondition(String shareguid) {
       String sql="select * from AUDIT_RS_SHARE_METADATA where shareguid=?";
        return ibaseDao.findList(sql, AuditRsShareMetadata.class, shareguid);
    }

    public List<MetaShare> getAuditSpBusinessCaseListByCondition(Map<String, String> map) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(MetaShare.class);
        if(sqlManageUtil.getListByCondition(MetaShare.class, map)==null){
            return new ArrayList<>(); 
        }
        return sqlManageUtil.getListByCondition(MetaShare.class, map);
    }
   
}
