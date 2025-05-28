package com.epoint.newshow.api;

import java.util.List;

import com.epoint.core.grammar.Record;

public interface NewshowService {

	int getacceptNum();

	int getbanjieNum();

	int getallAcceptNum();

	int getallBanjieNum();

	Record getcitySatisfy();

	List<Record> geteventType();

	List<Record> getmapData();

	Record gethandleEvent();

	Record getcityData();

	Record getcityDatabyid();

	Record getsource();

	List<Record> geteventTop5();

	List<Record> gettrend();

	List<Record> getmapbanjian();

}
