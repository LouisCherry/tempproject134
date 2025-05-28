package com.epoint.auditproject.auditproject.api;

import com.epoint.core.grammar.Record;

public interface IZtProjectService {
	
    
	public void inserRecord(Record record);
	
	public int UpdateRecord(Record record);
	
	
	public Record getDzbdDetailByZzbh(String tableName, String zzbh);

}
