package com.epoint.xmz.job.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.job.api.IGsProjectService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class GsProjectServiceImpl implements IGsProjectService
{

    /**
     * 
     */
    private static final long serialVersionUID = 3258268474743087728L;

   
    @Override
    public List<AuditProject> getWaitEvaluateSbList() {
        return new GsProjectService().getWaitEvaluateSbList();
    }


	@Override
	public List<AuditRsItemBaseinfo> getWaitItemList() {
		return new GsProjectService().getWaitItemList();
	}
	
	@Override
	public List<AuditRsItemBaseinfo> getSwWaitItemList() {
		return new GsProjectService().getSwWaitItemList();
	}
	
	@Override
	public  List<Record> getSpglSgxkzList() {
		return new GsProjectService().getSpglSgxkzList();
	}
	
	@Override
	public  List<Record> getSpglSgxkzXkList() {
		return new GsProjectService().getSpglSgxkzXkList();
	}
	
	@Override
	public  List<Record> getSpglSgxkzDantiList() {
		return new GsProjectService().getSpglSgxkzDantiList();
	}


	@Override
	public List<Record> getHpDataList() {
		return new GsProjectService().getHpDataList();
	}


	@Override
	public void insert(Record rec) {
		new GsProjectService().insert(rec);
	}
	
	@Override
	public void insertQzk(Record rec) {
		new GsProjectService().insertQzk(rec);
	}
	
	@Override
	public void updateQzk(Record rec) {
		new GsProjectService().updateQzk(rec);
	}
	
	@Override
    public Record getSpglSgxkIteminfoBySubappguid(String projectguid) {
        return new GsProjectService().getSpglSgxkIteminfoBySubappguid(projectguid);
    }
	
	@Override
    public Record getSpglDantiInfoBySubappguid(String projectguid) {
        return new GsProjectService().getSpglDantiInfoBySubappguid(projectguid);
    }
	
	@Override
    public List<Record> getSpglParticipnListBySubappguid(String projectguid) {
        return new GsProjectService().getSpglParticipnListBySubappguid(projectguid);
    }
	
	@Override
    public Record getSpglSgxkInfoBySubappguid(String projectguid) {
        return new GsProjectService().getSpglSgxkInfoBySubappguid(projectguid);
    }
	
	@Override
    public Record getSpglJgYsInfoBySubappguid(String projectguid) {
        return new GsProjectService().getSpglJgYsInfoBySubappguid(projectguid);
    }
	
	@Override
	public Record getProjectBasicInfoByRowguid(String id) {
		return new GsProjectService().getProjectBasicInfoByRowguid(id);
	}
	
	@Override
	public Record getProjectUnitInfoByRowguid(String id) {
		return new GsProjectService().getProjectUnitInfoByRowguid(id);
	}
	@Override
	public Record getProjectCorpInfoByRowguid(String id) {
		return new GsProjectService().getProjectCorpInfoByRowguid(id);
	}
	@Override
	public Record getProjectFinishByRowguid(String id) {
		return new GsProjectService().getProjectFinishByRowguid(id);
	}
	@Override
	public Record getProjectLicenceByRowguid(String id) {
		return new GsProjectService().getProjectLicenceByRowguid(id);
	}
	
	@Override
	public Record getSgxkzInfoByPorjectId(String id) {
		return new GsProjectService().getSgxkzInfoByPorjectId(id);
	}
	
	@Override
	public AuditSpSpSgxk getSpSgxkByRowguid(String rowguid) {
		return new GsProjectService().getSpSgxkByRowguid(rowguid);
	}

}
