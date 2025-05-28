package com.epoint.lsyc.comprehensivewindow.meta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSpClass;

@Component
@Service
public class AuditRsShareMetadataSpClassImp implements IAuditRsShareMetadataSpClass
{
    private static final long serialVersionUID = 1L;

    @Override
    public int updateAuditRsShareMetadataSpClass(AuditRsShareMetadataSpClass me) {
        return new AuditRsShareMetadataSpClassService().updateAuditRsShareMetadataSpClass(me);
    }

    @Override
    public int deleteAuditRsShareMetadataSpClassByRowGuid(String rowguid) {
        return new AuditRsShareMetadataSpClassService().deleteAuditRsShareMetadataSpClassByRowGuid(rowguid);
    }

    @Override
    public int insertRecord(AuditRsShareMetadataSpClass me) {
        return new AuditRsShareMetadataSpClassService().insertRecord(me);
    }

    @Override
    public AuditRsShareMetadataSpClass getAuditRsShareMetadataSpClass(String rowguid) {
        return new AuditRsShareMetadataSpClassService().getAuditRsShareMetadataSpClass(rowguid);
    }

    @Override
    public AuditCommonResult<PageData<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassPageData(
            Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        return new AuditRsShareMetadataSpClassService().selectAuditRsShareMetadataSpClassPageData(baseClass,
                conditionMap, first, pageSize, sortField, sortOrder);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByBusinessguid(
            String businessguid, String phaseguid) {
        return new AuditRsShareMetadataSpClassService().selectAuditRsShareMetadataSpClassByBusinessguid(businessguid,
                phaseguid);
    }

    @Override
    public AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByMaterialguid(
            String materialguid) {
        return new AuditRsShareMetadataSpClassService().selectAuditRsShareMetadataSpClassByMaterialguid(materialguid);
    }

    @Override
    public void deleteAuditRsShareMetadataSpClassByPhaseguid(String phaseguid) {
        new AuditRsShareMetadataSpClassService().deleteAuditRsShareMetadataSpClassByPhaseguid(phaseguid);
    }

    @Override
    public AuditRsShareMetadataSpClass selectAuditRsShareMetadataSpClassByTaskId(String phaseguid, String taskid) {
        
        return new AuditRsShareMetadataSpClassService().selectAuditRsShareMetadataSpClassByTaskId(phaseguid, taskid);
    }
}
