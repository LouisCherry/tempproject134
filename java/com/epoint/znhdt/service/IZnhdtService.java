package com.epoint.znhdt.service;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public interface IZnhdtService extends Serializable
{
    public Record getProjectCount(String areacode);

    public List<Record> getBusinessCountList(String areacode, int limitInt);

    public List<Record> getTownMapProjectCountList(String areacode);

    public List<Record> getNowMonthWinProjectCountList(String areacode, int limitInt);

    public PageData<Record> getBusinessDataList(String areacode, int first, int pagesize);

    public Record getXmCount(String areacode);

    public List<Record> getXmlxCountList(String areacode, int limitInt);

    public List<Record> getXmGchyflCountList(String areacode, int limitInt);

    public List<Record> getXmLxlxCountList(String areacode);

    public PageData<Record> getXmTjDataList(String areacode, int first, int pagesize);
}
