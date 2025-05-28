package com.epoint.ggyjstj.api;

import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

public interface IgetSpDataByAreacode
{
    /**
     * 查询申报量
     *
     * @return
     * @throws Exception
     */
    public Record getSpDataByAreacode(String areacode) ;
    /**
     * 查询一件事
     *
     * @return
     * @throws Exception
     */
    public PageData<AuditSpInstance> getyjsDataByAreacodeAndType(String areacode, String type, String applyername, String itemname, String status, Date beginTime,Date endTime,Integer first,Integer pagesize) ;
}
