package com.epoint.rsj.impl;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;
import com.epoint.rsj.api.IJnService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class JnServiceImpl implements IJnService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;

   

	@Override
	public int insert(Record rec) {
		return new JnService().insert(rec);
	}

	@Override
	public AuditTask getAuditBasicInfo(String taskName, String areacode) {
		return new JnService().getAuditBasicInfo(taskName, areacode);
	}

	@Override
	public Record getAuditProjectZjxtByRowguid(String rowguid) {
		return new JnService().getAuditProjectZjxtByRowguid(rowguid);
	}

	@Override
	public Record getAuditRsApplyZjxtByRowguid(String rowguid) {
		return  new JnService().getAuditRsApplyZjxtByRowguid(rowguid);
	}

    @Override
    public String getAreacodeByareaname(String areaname) {
        return  new JnService().getAreacodeByareaname(areaname);
    }

	@Override
	public List<Record> getGxbysjyProjectList() {
		return  new JnService().getGxbysjyProjectList();
	}
	@Override
	public List<Record> getSjglProjectList() {
		return  new JnService().getSjglProjectList();
	}
	@Override
	public List<Record> getRskszxProjectList() {
		return  new JnService().getRskszxProjectList();
	}
	@Override
	public List<Record> getZyjsryProjectList() {
		return  new JnService().getZyjsryProjectList();
	}
	
	@Override
	public List<Record> getZyjnjdProjectList() {
		return  new JnService().getZyjnjdProjectList();
	}
	
	@Override
	public List<Record> getKjjProjectList() {
		return  new JnService().getKjjProjectList();
	}

	@Override
	public List<Record> getHjProjectList(String oRGBUSNO) {
		return  new JnService().getHjProjectList(oRGBUSNO);
	}

	@Override
	public Record getIfSyncByProid(String str) {
		return  new JnService().getIfSyncByProid(str);
	}

	@Override
	public int updateByProid(String str,String status) {
		return  new JnService().updateByProid(str,status);
		
	}

}
