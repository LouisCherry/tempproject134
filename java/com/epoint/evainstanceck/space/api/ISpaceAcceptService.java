package com.epoint.evainstanceck.space.api;

import com.epoint.core.grammar.Record;

import java.util.List;

public interface ISpaceAcceptService
{


    List<Record> getGradinfo(String grade);

	Record getHcpStati(String value);

	Record getstaicfiedByTaskguid(String taskguid);

	Record getevaluatStatiByTaskguid(String taskguid);



}
