package com.epoint.basic.auditonlineuser.auditonlineconsult.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsultExt;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditDaibanConsultExt;
import com.epoint.basic.auditonlineuser.service.AuditOnlineUserService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.esotericsoftware.minlog.Log;

@Component
@Service
public class AuditDaibanConsultExtImpl implements IAuditDaibanConsultExt{

	@Override
	public AuditCommonResult<String> addConsultExt(AuditDaibanConsultExt consultExt) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
		AuditCommonResult<String> result = new AuditCommonResult<String>();
		try {
			auditConsultExt.addRecord(AuditDaibanConsultExt.class, consultExt, false);
		} catch (Exception e) {
		    e.printStackTrace();
			result.setSystemFail(e.toString());
		}	
		return result;
	}

	@Override
	public AuditCommonResult<String> updateConsultExt(AuditDaibanConsultExt consultExt) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
		AuditCommonResult<String> result = new AuditCommonResult<String>();
		try {
			auditConsultExt.updateRecord(AuditDaibanConsultExt.class, consultExt,"rowguid", false);
		} catch (Exception e) {
		    e.printStackTrace();
			result.setSystemFail(e.toString());
		}	
		return result;
	}

	@Override
	public AuditCommonResult<AuditDaibanConsultExt> getConsultExtByRowguid(String rowguid) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsult = new AuditOnlineUserService<AuditDaibanConsultExt>();
		AuditCommonResult<AuditDaibanConsultExt> result = new AuditCommonResult<AuditDaibanConsultExt>();
		try {
			AuditDaibanConsultExt consultExt=auditConsult.getDetail(AuditDaibanConsultExt.class, rowguid,"rowguid", false);
			result.setResult(consultExt);
		} catch (Exception e) {
		    e.printStackTrace();
			result.setSystemFail(e.toString());
		}	
		return result;
	}

	@Override
	public AuditCommonResult<String> deleteConsultExt(AuditDaibanConsultExt consultExt) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
        	auditConsultExt.deleteRecod(AuditDaibanConsultExt.class, consultExt, "rowguid", false);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
	}

	@Override
	public AuditCommonResult<String> deleteConsultExtByRowguid(String rowguid) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
        	AuditDaibanConsultExt consultExt = this.getConsultExtByRowguid(rowguid).getResult();
        	auditConsultExt.deleteRecod(AuditDaibanConsultExt.class, consultExt, "rowguid", true);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
	}

	@Override
	public AuditCommonResult<List<AuditDaibanConsultExt>> selectConsultExtByPage(Map<String, String> conditionMap,
			Integer first, Integer pageSize, String sortField, String sortOrder) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
        AuditCommonResult<List<AuditDaibanConsultExt>> result = new AuditCommonResult<List<AuditDaibanConsultExt>>();
        try {
             PageData<AuditDaibanConsultExt> pageData= auditConsultExt.getAllRecordByPage(AuditDaibanConsultExt.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
             List<AuditDaibanConsultExt> consultExt=pageData.getList();
            result.setResult(consultExt);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
	}

	@Override
	public AuditCommonResult<Integer> getConsultExtCount(Map<String, String> conditionMap) {
		AuditOnlineUserService<AuditDaibanConsultExt> auditConsultExt = new AuditOnlineUserService<AuditDaibanConsultExt>();
    	AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
    	try {
    		Integer allRecordCount = auditConsultExt.getAllRecordCount(AuditDaibanConsultExt.class, conditionMap, false);
    		result.setResult(allRecordCount);
    	} catch (Exception e) {
    	    e.printStackTrace();
			result.setSystemFail(e.toString());
		}
        return result;  
	}

	@Override
	public AuditCommonResult<List<AuditDaibanConsultExt>> selectConsultExtList(Map<String, String> conditionMap) {
	    if (!SQLManageUtil.validate(conditionMap)) {
            Log.info("本次查询无任何条件，请检查代码！");
            return null;
        }
		AuditOnlineUserService<AuditDaibanConsultExt> auditOnlineUserService = new AuditOnlineUserService<AuditDaibanConsultExt>();
        AuditCommonResult<List<AuditDaibanConsultExt>> result = new AuditCommonResult<List<AuditDaibanConsultExt>>();
        try {
            List<AuditDaibanConsultExt> consultsExtList = auditOnlineUserService.getAllRecord(AuditDaibanConsultExt.class,
                    conditionMap, false);
            result.setResult(consultsExtList);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
	}
}
