package com.epoint.auditstatics.api;

import java.util.List;
import com.epoint.core.grammar.Record;

public interface IJnabutmentService {

	List<Record> outaskStatics(String centerguid);

	String getItemTextByCodeName(String object);

	List<Record> categoryStatics(String areacode);

	List<Record> taskSourceStatics(String areacode);

	List<Record> typeStatics(String areacode);


}
