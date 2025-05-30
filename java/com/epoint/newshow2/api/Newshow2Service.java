package com.epoint.newshow2.api;

import java.util.List;

import com.epoint.core.grammar.Record;

public interface Newshow2Service {

	Record getcitySatisfy(String areaCode);

	List<Record> geteventType(String areaCode);

	List<Record> getmapData();

	Record gethandleEvent(String areaCode);

	Record getcityDatabyid();

	Record getsource(String areaCode);

	List<Record> geteventTop5(String areaCode);

	List<Record> gettrend(String areaCode);

	List<Record> getmapbanjian();

    Record getDaycount(String centerGuid);

    int getYearcount(String centerGuid);

    String findOushortname(Object object);

    String getDayQueue();
    
    Record getBusinessDetail(String certnum);
    
    int updateBusinessDetailStatus(String itemcode);

}
