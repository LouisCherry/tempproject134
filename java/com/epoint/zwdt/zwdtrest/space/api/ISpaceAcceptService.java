package com.epoint.zwdt.zwdtrest.space.api;

import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.xmz.job.yqarea;
import com.epoint.xmz.job.yqbm;
import com.epoint.xmz.job.yqdog;

public interface ISpaceAcceptService
{


    List<Record> getGradinfo(String grade);

	Record getHcpStati(String value);

	Record getstaicfiedByTaskguid(String taskguid);

	Record getevaluatStatiByTaskguid(String taskguid);
	
	public int insert(yqarea record);
	
	public int insertDog(yqdog record);
	
	public int insertYqbm(yqbm record);


}
