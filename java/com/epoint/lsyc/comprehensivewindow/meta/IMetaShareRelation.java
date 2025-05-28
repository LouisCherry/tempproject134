package com.epoint.lsyc.comprehensivewindow.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

public interface IMetaShareRelation extends Serializable {
    public PageData<MetaShareRelation> getMetaShareRelationList(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder);

    public int insertRecord(MetaShareRelation relation);

    public MetaShareRelation getMetaShareRelation(String rowguid);

    public void delete(MetaShareRelation relation);
    
    public int update(MetaShareRelation relation);

    public List<MetaShareRelation> getMetaShareRelationListByCondition(Map<String, String> map);
    
    public int getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(String businessguid,String phaseguid,String shareguid);
    
}
