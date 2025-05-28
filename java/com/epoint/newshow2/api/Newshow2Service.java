package com.epoint.newshow2.api;

import java.util.List;

import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public interface Newshow2Service {

	Record getcitySatisfy(String areaCode);

	List<Record> geteventType();
	
	List<Record> getmapData();
	
	List<Record> getLsmapData();

	Record gethandleEvent(String areaCode);

	Record getcityDatabyid(String areaCode);

	Record getsource(String areaCode);

	List<Record> geteventTop5(String areaCode);

	List<Record> gettrend(String areaCode);

	List<Record> getmapbanjian();
	
	List<Record> getLsmapbanjian();

    Record getDaycount(String centerGuid);

    int getYearcount(String centerGuid);

    String findOushortname(Object object);

    String getDayQueue(String centerGuid);
    
    Record getYqByRowguid(String rowguid);
    
    List<Record> getYcProjectList(int pageNumber, int pageSize);

    int getYcProjectCount();
    
    AuditSpSpSgxk getSgxkProjectBySubappGuid(String subappguid);
    
    List<FrameOu> getOuListByAreacode(String areacode);

	List<Record> getWindowNumByCenterguid(String centerGuid);

	Record getQueueNumByOuguid(String weekbegin, String centerguid, String ouguid);

	List<Record> getapplyerTypeByAreacode(String areaCode);
}
