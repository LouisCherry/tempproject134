package com.epoint.auditproject.auditproject.api;

import java.util.List;
import java.util.Map;

import com.epoint.auditproject.auditproject.api.entity.JnTcId;
import com.epoint.auditproject.entity.csfxxb;
import com.epoint.auditproject.entity.jbxxb;
import com.epoint.auditproject.entity.msfxxb;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

public interface IJnProjectService {
	
	  /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record);
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertJbxxb(jbxxb record);
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertCsfxxb(csfxxb record);
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insertMsfxxb(msfxxb record);
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public List<Record> findSource();
    
    
    
    public void updateSource(String flag,String flowsn);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey);

    public int updateChargeDetail(String rowguid);

    public JnTcId getDzbdForBusiness(String yewguid);
    
    public void insertCodeItemForTxxl(String addvaluetxxl);
    
    public FrameAttachInfo getFrameAttachByIdenumber(String idnumber);
    
    public List<FrameAttachInfo> getFrameAttachByIdenumberBigType(String idnumber, String bigshowtype);
    
    public List<FrameAttachInfo> getFrameAttachByIdenumberTag(String idnumber,String cliengtag);
    
    public Record DzbdItemBaseinfoByProjectGuid(String rowguid);

    public AuditProject getProjectByflowsn(String flowsn);
    
	public void inserRecord(Record record);
	
	public void inserCsjzlj(Record record);
	
	public int UpdateRecord(Record record);
	
	public  Record getYlggsczmByZmwh(String zmwh);
	
	public  Record getYljgfsjshByNumber(String zmwh);


    /**
     * 查询大数据局对应表单是否存在该记录
     * @param tableName
     * @param zzbh
     * @return
     */
	public Record getDzbdDetailByZzbh(String tableName, String zzbh);

    /**
     * 查询大数据局对应表单是否存在该记录
     * @param tableName
     * @param zzbh
     * @return
     */
    public Record getDzbdDetailByfield(String tableName,String field, String zzbh);
	
	public Record getSpfysxkByRowguid(String rowguid);

	public  Record getGgcswsxkxsqByNumber(String zmwh);
	
	public  Record getYljgfsjgysByNumber(String zmwh);
	
	public  Record getTsqkyswtsByWtsbh(String wtsbh);
	
	public  Record getCshwggpzsByZmwh(String zmwh);
	
	public boolean checkSignByCliengguid(String str);
	
	public AuditCommonResult<PageData<AuditProjectZjxt>>  getAuditProjectZjxtPageData(Map<String, String> map, int first, int pageSize, String string,
			String string2);
	
	public boolean checkIsky(String projectguid);
}
