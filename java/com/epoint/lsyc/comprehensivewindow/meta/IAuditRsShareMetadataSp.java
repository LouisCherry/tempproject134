package com.epoint.lsyc.comprehensivewindow.meta;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.lsyc.comprehensivewindow.meta.domain.AuditRsShareMetadataSp;
import com.epoint.lsyc.comprehensivewindow.meta.domain.MetaShareRelation;

public interface IAuditRsShareMetadataSp extends Serializable
{
	/**
	 * 处理共享元数据
	 * @param businedssguid
	 * @return
	 */
	AuditCommonResult<Void> initSPShareMetaFromTask(String businedssguid,String phaseguid);
	
	/**
	 *  更新元数据
	 * @return
	 */
	int updateShareMetaSpData(AuditRsShareMetadataSp me);
	
	/**
	 *  删除元数据
	 * @return
	 */
	int deleteShareMetaSpData(String rowguid);
	
	/**
	 * 删除共享元数据
	 * @param rowguid
	 * @return
	 */
	int deleteShareMetaDataByRowGuid(String rowguid);
	
	/**
	 * 插入记录
	 * @param me
	 * @return
	 */
	int insertRecord(AuditRsShareMetadataSp me);
	
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
	AuditCommonResult<PageData<AuditRsShareMetadataSp>> selectAuditRsShareMetaDataPageData(
			Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int first, int pageSize,
			String sortField, String sortOrder);
	
	/**
	 *  根据主题guid和shareguid获得
	 * @param businessGuid
	 * @param shareguid
	 * @return
	 */
	AuditCommonResult<List<MetaShareRelation>> selectByShareMaterialGuid(String businessGuid,String shareguid);
	
	/**
	 * 根据metaguid获得元数据
	 * @param metaguid
	 * @return
	 */
	AuditCommonResult<List<AuditRsShareMetadataSp>> getMetaSpListByMetaguid(String metaguid);
	
	   /**
     * 根据metaguid获得元数据
     * @param metaguid
     * @return
     */
    AuditCommonResult<AuditRsShareMetadata> getMeta(String metaguid);
    
	/**
	 * 
	 * @param metaspguid
	 * @return
	 */
	AuditCommonResult<AuditRsShareMetadataSp> getMetaSp(String metaspguid);
	
	/**
	 * 如果存在元数据共享，则提示与其他材料进行关联
	 * @param metaguid
	 * @param businessguid
	 * @return
	 */
	boolean ifMetaRelation(String metaguid,String phaseguid);
	
	/**
	 * 删除主题配置元数据的关联数据
	 * @param metaspguid
	 * @return
	 */
	int deleteRelationByMetaSp(String metaspguid);
	
	/**
	 * 根据businessguid获得
	 * *@param businessguid
	 * @param sortField
	 * @param sortValue
	 * @return
	 */
	AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,
			String sortField, String sortValue);
	
	/**
	 * 根据businessguid获得
	 * *@param businessguid
	 * @param sortField
	 * @param sortValue
	 * @return
	 */
	AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguid(String businessguid,String phaseguid,
			String sortField, String sortValue);
	
	/**
     * 查询具有相同分类的元数据字段
     * @param businessguid
     * @param phaseguid
     * @param classguid
     * @return
     */
    AuditCommonResult<List<AuditRsShareMetadataSp>> selectAuditRsShareMetadataByBusinessguidAndClassguid(String businessguid,String phaseguid,String classguid);
    
	/**
	 * 获取唯一的元数据
	 * @param businessGuid
	 * @param shareguid
	 * @param taskid
	 * @return
	 */
	AuditCommonResult<AuditRsShareMetadata> selectMeta(String shareguid,String taskid);

	/**
	 * 获取元数据
	 * @param businessGuid
	 * @param shareguid
	 * @param taskid
	 * @return
	 */
	AuditCommonResult<List<AuditRsShareMetadata>> selectMetaList(String fieldname,String taskid);

    /**
     * 
     *  获取元数据
     *  [功能详细描述]
     *  @param rowguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    List<AuditRsShareMetadata> getDatas(String rowguid);

    AuditRsShareMetadataSp getMetaShareRelationCountByBusinessGuidAndPhaseguidAndShareguid(String businessguid,String phaseguid,String shareguid);
    
    /**
     * 获取主题字段对应的事项字段
     */
    List<AuditRsShareMetadata> seletemetadataListByMetasharerelationMetaspguidAndShareguid(String metaspguid,String shareguid);
    
    /**
     * 获取事项字段名对应的主题字段
     */
    List<AuditRsShareMetadataSp> getAuditRsShareMetadataSpListByBusinessGuidAndPhaseguidAndFieldname(String businessguid,String phaseguid,String fieldname);
    
}
