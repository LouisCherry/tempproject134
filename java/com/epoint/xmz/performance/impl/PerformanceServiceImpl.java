package com.epoint.xmz.performance.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.performance.api.PerformanceService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;

@Component
@Service
public class PerformanceServiceImpl implements PerformanceService
{

    public AuditCommonResult<List<Record>> getfwfswbd() {
        PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getfwfswbd());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    

    public AuditCommonResult<List<Record>> gettaskin(int pageindex, int pagesize,String areacode) {
        PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.gettaskin(pageindex,pagesize,areacode));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<List<Record>> getywbljg(int pageindex, int pagesize,String areacode) {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getywbljg(pageindex,pagesize,areacode));
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    public AuditCommonResult<List<Record>> getzxblsd(int pageindex, int pagesize) {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getzxblsd(pageindex,pagesize));
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    public AuditCommonResult<List<Record>> getzxblsd1(int pageindex, int pagesize,String areacode,String areacode1) {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getzxblsd1(pageindex,pagesize,areacode,areacode1));
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    

    public AuditCommonResult<String> gettaskincount(String areacode) {
        PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        try {
            result.setResult(auditTaskBasicService.gettaskincount(areacode));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<String> getywbljgcount(String areacode) {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<String> result = new AuditCommonResult<String>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getywbljgcount(areacode));
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    
    public AuditCommonResult<List<Record>> getzxblsdcount() {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getzxblsdcount());
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    public AuditCommonResult<List<Record>> getzxblsdcount1(String areacode,String areacode1) {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getzxblsdcount1(areacode,areacode1));
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    
    public AuditCommonResult<String> getbanjiecount() {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<String> result = new AuditCommonResult<String>();
    	
    	try {
    		result.setResult(auditTaskBasicService.getbanjiecount());
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }
    
    public AuditCommonResult<Record> getzxfwcxd() {
        PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();

        try {
            result.setResult(auditTaskBasicService.getzxfwcxd());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<Record> getfwsxfgd() {
        PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();

        try {
            result.setResult(auditTaskBasicService.getfwsxfgd());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<List<Record>> zjxnkh() {
    	PerformanceServiceImplService auditTaskBasicService = new PerformanceServiceImplService();
    	AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
    	
    	try {
    		result.setResult(auditTaskBasicService.zjxnkh());
    	} catch (Exception e) {
    		result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
    	}
    	
    	return result;
    }

}
