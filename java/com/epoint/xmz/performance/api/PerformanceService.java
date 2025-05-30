package com.epoint.xmz.performance.api;

import java.util.List;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

public interface PerformanceService {

	AuditCommonResult<List<Record>> getfwfswbd();
	
	AuditCommonResult<List<Record>> gettaskin(int pageindex, int pagesize,String areacode);
	
	AuditCommonResult<List<Record>> getywbljg(int pageindex, int pagesize,String areacode);
	
	AuditCommonResult<List<Record>> getzxblsd(int pageindex, int pagesize);
	
	AuditCommonResult<List<Record>> getzxblsd1(int pageindex, int pagesize,String areacode,String areacode1);
	
	AuditCommonResult<String> gettaskincount(String areacode);
	
	AuditCommonResult<String> getywbljgcount(String areacode);
	
	AuditCommonResult<String> getbanjiecount();

	AuditCommonResult<List<Record>> getzxblsdcount();
	
	AuditCommonResult<List<Record>> getzxblsdcount1(String areacode,String areacode1);
	
	AuditCommonResult<Record> getzxfwcxd();
	
	AuditCommonResult<Record> getfwsxfgd();
	
	AuditCommonResult<List<Record>> zjxnkh();
}
