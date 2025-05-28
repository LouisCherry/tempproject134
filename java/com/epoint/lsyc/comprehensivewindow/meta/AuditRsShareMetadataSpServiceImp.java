package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

@Component
@Service
public class AuditRsShareMetadataSpServiceImp implements IAuditRsShareMetadataSp {
    private static final long serialVersionUID = 1L;

    @Override
    public AuditCommonResult<Void> initSPShareMetaFromTask(String businedssguid, String phaseguid) {
        return new AuditRsShareMetadataSpService().initSPShareMetaFromTask(businedssguid, phaseguid);
    }

    @Override
    public int updateShareMetaSpData(AuditRsShareMetadataSp me) {
        return new AuditRsShareMetadataSpService().updateShareMetaSpData(me);
    }

    @Override
    public int deleteShareMetaSpData(String rowguid) {
    	return new AuditRsShareMetadataSpService().deleteShareMetaSpData(rowguid);
    }
    
    @Override
    public int deleteShareMetaDataByRowGuid(String rowguid) {
        return new AuditRsShareMetadataSpService().deleteShareMetaDataByRowGuid(rowguid);
    }

    @Override
    public int insertRecord(AuditRsShareMetadataSp me) {
        return new AuditRsShareMetadataSpService().insertRecord(me);
    }

    @Override
    public AuditCommonResult<PageData<AuditRsShareMetadataSp>> selectAuditRsShareMetaDataPageData(
            Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        return new AuditRsShareMetadataSpService().selectAuditRsShareMetaDataPageData(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    @Override
    public AuditCommonResult<List<MetaShareRelation>> selectByShareMaterialGuid(String businessGuid, String shareguid) {
        return new AuditRsShareMetadataSpService().selectByShareMaterialGuid(businessGuid, shareguid);
    }

    @Override
    public AuditCommonResult<AuditRsShareMetadata> getMeta(String metaguid) {
        return new AuditRsShareMetadataSpService().getMeta(metaguid);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSp>> getMetaSpListByMetaguid(String metaguid) {
        return new AuditRsShareMetadataSpService().getMetaSpListByMetaguid(metaguid);
    }
    
    @Override
    public AuditCommonResult<AuditRsShareMetadataSp> getMetaSp(String metaspguid) {
        return new AuditRsShareMetadataSpService().getMetaSp(metaspguid);
    }

    @Override
    public boolean ifMetaRelation(String metaguid, String phaseguid) {
        return new AuditRsShareMetadataSpService().ifMetaRelation(metaguid, phaseguid);
    }

    @Override
    public int deleteRelationByMetaSp(String metaspguid) {
        return new AuditRsShareMetadataSpService().deleteRelationByMetaSp(metaspguid);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,
            String sortField, String sortValue) {
        return new AuditRsShareMetadataSpService().selectAuditRsShareMetadataByBusinessguid(businessguid, sortField, sortValue);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,
            String phaseguid, String sortField, String sortValue) {
        return new AuditRsShareMetadataSpService().selectAuditRsShareMetadataByBusinessguid(businessguid, phaseguid, sortField, sortValue);
    }

    /**
     * 查询具有相同分类的元数据字段
     * @param businessguid
     * @param phaseguid
     * @param classguid
     * @return
     */
    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguidAndClassguid(String businessguid,String phaseguid,String classguid){
        return new AuditRsShareMetadataSpService().selectAuditRsShareMetadataByBusinessguidAndClassguid(businessguid, phaseguid, classguid);
    }
    
    @Override
    public AuditCommonResult<AuditRsShareMetadata> selectMeta(String shareguid, String taskid) {
        return new AuditRsShareMetadataSpService().selectMeta(shareguid, taskid);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadata>> selectMetaList(String fieldname, String taskid) {
        return new AuditRsShareMetadataSpService().selectMetaList(fieldname, taskid);
    }

    /*   @Override
    public List<AuditTask> findByTaskbasicId(String taskbasicid) {
        return new AuditRsShareMetadataSpService().findByTaskbasicId(String taskbasicid);
    }*/

    @Override
    public List<AuditRsShareMetadata> getDatas(String rowguid) {
        return new AuditRsShareMetadataSpService().getDatas(rowguid);
    }

    @Override
    public AuditRsShareMetadataSp getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(String businessguid, String phaseguid, String shareguid) {
        return new AuditRsShareMetadataSpService().getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(businessguid, phaseguid, shareguid);
    }

    @Override
    public List<AuditRsShareMetadata> seletemetadataListByMetasharerelationMetaspguidAndShareguid(String metaspguid, String shareguid) {
        return new AuditRsShareMetadataSpService().seletemetadataListByMetasharerelationMetaspguidAndShareguid(metaspguid, shareguid);
    }

    @Override
    public List<AuditRsShareMetadataSp> getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(String businessguid, String phaseguid, String filedname) {
        return new AuditRsShareMetadataSpService().getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(businessguid, phaseguid, filedname);
    }
}
