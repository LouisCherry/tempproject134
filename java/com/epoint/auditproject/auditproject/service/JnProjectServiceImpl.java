package com.epoint.auditproject.auditproject.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
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
import com.epoint.xmz.cxbus.impl.CxBusService;

@Service
@Component
public class JnProjectServiceImpl implements IJnProjectService
{

	/**
     * 
     */
    private static final long serialVersionUID = -4737301911580678408L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record) {
        return new JnProjectService().insert(record);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int  insertJbxxb(jbxxb record) {
        return new JnProjectService().insertJbxxb(record);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int  insertCsfxxb(csfxxb record) {
        return new JnProjectService().insertCsfxxb(record);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int  insertMsfxxb(msfxxb record) {
        return new JnProjectService().insertMsfxxb(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnProjectService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record) {
        return new JnProjectService().update(record);
    }
    
    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey) {
       return new JnProjectService().find(primaryKey);
    }
    
    public int updateChargeDetail(String rowguid){
    	 return new JnProjectService().updateChargeDetail(rowguid);
    }
    
    public JnTcId getDzbdForBusiness(String yewguid){
    	 return new JnProjectService().getDzbdForBusiness(yewguid);
    }
    
    public void insertCodeItemForTxxl(String addvaluetxxl){
    	new JnProjectService().insertCodeItemForTxxl(addvaluetxxl);
    }
    
    public FrameAttachInfo getFrameAttachByIdenumber(String idnumber){
    	return new JnProjectService().getFrameAttachByIdenumber(idnumber);
    }
    
    public List<FrameAttachInfo> getFrameAttachByIdenumberBigType(String idnumber, String bigshowtype){
    	return new JnProjectService().getFrameAttachByIdenumberBigType(idnumber, bigshowtype);
    }
    
    public List<FrameAttachInfo> getFrameAttachByIdenumberTag(String idnumber,String cliengtag){
    	return new JnProjectService().getFrameAttachByIdenumberTag(idnumber,cliengtag);
    }
    
    public Record DzbdItemBaseinfoByProjectGuid(String rowguid){
    	return new JnProjectService().DzbdItemBaseinfoByProjectGuid(rowguid);
    }
    
    public List<Record> findSource() {
        return new JnProjectService().findSource();
    }

    public void updateSource(String flag, String flowsn) {
       new JnProjectService().updateSource(flag,flowsn);
    }
    
    public AuditProject getProjectByflowsn(String flowsn) {
    	return new JnProjectService().getProjectByflowsn(flowsn);
    }

	@Override
	public void inserRecord(Record record) {
		 new JnProjectService().inserRecord(record);
	}
	
	@Override
	public void inserCsjzlj(Record record) {
		new JnProjectService().inserCsjzlj(record);
	}

	@Override
	public boolean checkSignByCliengguid(String cliengguid) {
		return new JnProjectService().checkSignByCliengguid(cliengguid);
	}

	@Override
	public int UpdateRecord(Record record) {
		return new JnProjectService().updateRecord(record);
	}
	
	public Record getYlggsczmByZmwh(String zmwh) {
	       return new JnProjectService().getYlggsczmByZmwh(zmwh);
	}
	
	public Record getYljgfsjshByNumber(String zmwh) {
		return new JnProjectService().getYljgfsjshByNumber(zmwh);
	}
	
	public Record getGgcswsxkxsqByNumber(String zmwh) {
		return new JnProjectService().getGgcswsxkxsqByNumber(zmwh);
	}
	
	public Record getYljgfsjgysByNumber(String zmwh) {
		return new JnProjectService().getYljgfsjgysByNumber(zmwh);
	}
	
	public Record getCshwggpzsByZmwh(String wtsbh) {
		return new JnProjectService().getCshwggpzsByZmwh(wtsbh);
	}
	
	public Record getTsqkyswtsByWtsbh(String zmwh) {
		return new JnProjectService().getTsqkyswtsByWtsbh(zmwh);
	}

	@Override
	public AuditCommonResult<PageData<AuditProjectZjxt>> getAuditProjectZjxtPageData(Map<String, String> map, int first,
			int pageSize, String string, String string2) {
		return new JnProjectService().getAuditProjectZjxtPageData(map,first,pageSize,string,string2);
	}
	
	public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
		return new JnProjectService().getDzbdDetailByZzbh(tablename,zzbh);
	}

    public Record getDzbdDetailByfield(String tablename,String field, String zzbh) {
        return new JnProjectService().getDzbdDetailByfield(tablename,field,zzbh);
    }
	
	public Record getSpfysxkByRowguid(String rowguid) {
		return new JnProjectService().getSpfysxkByRowguid(rowguid);
	}

    @Override
    public boolean checkIsky(String projectguid) {
        return new JnProjectService().checkIsky(projectguid);
    }
	
	

}
