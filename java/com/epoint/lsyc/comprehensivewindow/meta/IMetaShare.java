package com.epoint.lsyc.comprehensivewindow.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShare;

public interface IMetaShare extends Serializable
{
    public PageData<MetaShare> getMetaShareList(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder);

    public int insertRecord(MetaShare metashare);

    public MetaShare getMetaShare(String rowguid);

    public void delete(MetaShare metaShare);

    public List<AuditRsShareMetadata> selectAuditRsShareMetadataByCondition(String rowguid);

    public List<MetaShare> getAuditSpBusinessCaseListByCondition(Map<String, String> map);
}
