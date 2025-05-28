package com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jiningstimulsoftanalysis.ByTaskAnalysis.api.IJNReportProjectByTaskService;

@Component
@Service
public class JNReportProjectByTaskServiceImpl implements IJNReportProjectByTaskService
{

    @Override
    public List<Record> findPageList(String startdate, String enddate, String ouguid, int first, int pageSize) {
        return new JNReportProjectByTaskService().findPageList( startdate,  enddate,  ouguid ,  first,  pageSize);
    }

    @Override
    public List<Record> findAllList(String startdate, String enddate, String ouguid) {
        return new JNReportProjectByTaskService().findAllList( startdate,  enddate,  ouguid);
    }

    @Override
    public List<String> getAllAreacode() {
        return new JNReportProjectByTaskService().getAllAreacode();
    }
    
    @Override
    public Record getTaskCountByouguid(String ouguid) {
        return new JNReportProjectByTaskService().getTaskCountByouguid(ouguid);
    }

}
