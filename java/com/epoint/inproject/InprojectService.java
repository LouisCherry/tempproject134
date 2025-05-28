package com.epoint.inproject;

import java.util.List;

import com.epoint.core.grammar.Record;

public interface InprojectService
{

    List<Record> findList(String startDate, String endDate);

}
