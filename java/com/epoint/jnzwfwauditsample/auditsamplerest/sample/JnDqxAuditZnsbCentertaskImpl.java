package com.epoint.jnzwfwauditsample.auditsamplerest.sample;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Component
@Service
public class JnDqxAuditZnsbCentertaskImpl implements IJnDqxAuditZnsbCentertask
{

    @Override
    public AuditCommonResult<PageData<Record>> getSampleTaskPageData(String centerguid, String ouguid, String taskname,
            int first, int pageSize) {
        JnDqxAuditZnsbCentertaskService centertaskservice = new JnDqxAuditZnsbCentertaskService();
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        try {
            PageData<Record> pageData = new PageData<Record>();
            List<Record> tasklist = centertaskservice.getSampleTaskList(centerguid, ouguid, taskname, first, pageSize);
            int dataCount = centertaskservice.getSampleTaskCount(centerguid, ouguid, taskname);
            pageData.setList(tasklist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<Record>> getCommonSampleTaskPageData(String centerguid, String taskname,
            int first, int pageSize) {
        JnDqxAuditZnsbCentertaskService centertaskservice = new JnDqxAuditZnsbCentertaskService();
        AuditCommonResult<PageData<Record>> result = new AuditCommonResult<PageData<Record>>();
        try {
            PageData<Record> pageData = new PageData<Record>();
            List<Record> tasklist = centertaskservice.getCommonSampleTaskList(centerguid, taskname, first, pageSize);
            int dataCount = centertaskservice.getCommonSampleTaskCount(centerguid, taskname);
            pageData.setList(tasklist);
            pageData.setRowCount(dataCount);
            result.setResult(pageData);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}
