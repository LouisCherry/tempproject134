package com.epoint.cs.sjtsproject.api;

import java.util.List;

import com.epoint.core.grammar.Record;

public interface ISjTsprojectService
{


    List<Record> findList(String taskname, String taskid, String ouname, String applyuser, String starttime, String endtime);

    List<Record> findApply(Object object);

    List<Record> findBanjie(Object object);

    String findStatus(Object object);

    Record findByUnid(String unid);

    List<Record> findPhase(String proId);

}
