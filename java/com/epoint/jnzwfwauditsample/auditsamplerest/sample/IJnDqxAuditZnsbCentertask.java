package com.epoint.jnzwfwauditsample.auditsamplerest.sample;

import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Service
public interface IJnDqxAuditZnsbCentertask
{

    public AuditCommonResult<PageData<Record>> getSampleTaskPageData(String centerguid, String ouguid, String taskname,
            int first, int pageSize);

    public AuditCommonResult<PageData<Record>> getCommonSampleTaskPageData(String centerguid, String taskname,
            int first, int pageSize);

}
