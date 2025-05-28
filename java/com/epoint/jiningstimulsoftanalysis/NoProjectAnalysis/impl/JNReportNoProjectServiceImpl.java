package com.epoint.jiningstimulsoftanalysis.NoProjectAnalysis.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jiningstimulsoftanalysis.NoProjectAnalysis.api.IJNReportNoProjectService;

@Component
@Service
public class JNReportNoProjectServiceImpl implements IJNReportNoProjectService
{

    @Override
    public List<Record> findPageList(String startdate, String enddate, String ouguid, int first, int pageSize) {
        return new JNReportNoProjectService().findPageList( startdate,  enddate,  ouguid ,  first,  pageSize);
    }

    @Override
    public List<Record> findAllList(String startdate, String enddate, String ouguid) {
        return new JNReportNoProjectService().findAllList( startdate,  enddate,  ouguid);
    }

}
