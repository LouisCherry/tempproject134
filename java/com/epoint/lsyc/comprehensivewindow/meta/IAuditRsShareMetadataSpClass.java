package com.epoint.lsyc.comprehensivewindow.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSpClass;

public interface IAuditRsShareMetadataSpClass extends Serializable {
	
	/**
	 *  更新元数据
	 * @return
	 */
	int updateAuditRsShareMetadataSpClass(AuditRsShareMetadataSpClass me);
	
	/**
	 * 删除共享元数据
	 * @param rowguid
	 * @return
	 */
	int deleteAuditRsShareMetadataSpClassByRowGuid(String rowguid);
	
	/**
	 * 插入记录
	 * @param me
	 * @return
	 */
	int insertRecord(AuditRsShareMetadataSpClass me);
	
    /**
     * 
     *  获取元数据
     *  [功能详细描述]
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    AuditRsShareMetadataSpClass getAuditRsShareMetadataSpClass(String rowguid);
    
	/**
	 * 获得主题的共享元数据
	 * @param baseClass
	 * @param conditionMap
	 * @param first
	 * @param pageSize
	 * @param sortField
	 * @param sortOrder
	 * @return
	 */
	AuditCommonResult<PageData<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassPageData(
			Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
			String sortField, String sortOrder);
	
	/**
	 * 根据businessguid获得
	 * *@param businessguid
	 */
	AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByBusinessguid(String businessguid, String phaseguid);
	   
    /**
     * 根据materialguid获得
     * *@param materialguid
     */
    AuditCommonResult<List<AuditRsShareMetadataSpClass>> selectAuditRsShareMetadataSpClassByMaterialguid(String materialguid);

    /**
     * 删除阶段下的主题元数据分类
     */
    public void deleteAuditRsShareMetadataSpClassByPhaseguid(String phaseguid);
    /**
     * 根据taskid查询阶段下的元数据分类
     */
    public AuditRsShareMetadataSpClass selectAuditRsShareMetadataSpClassByTaskId(String phaseguid, String taskid);
}
