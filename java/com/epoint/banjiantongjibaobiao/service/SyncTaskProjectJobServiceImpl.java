package com.epoint.banjiantongjibaobiao.service;

import com.epoint.banjiantongjibaobiao.api.ISyncTaskProjectJobService;
import com.epoint.core.grammar.Record;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Component
@Service
public class SyncTaskProjectJobServiceImpl implements ISyncTaskProjectJobService {

    @Override
    public Record getLastDate() {
        return new SyncTaskProjectJobService().getLastDate();
    }

    @Override
    public Date getMinDate() {
        return new SyncTaskProjectJobService().getMinDate();
    }

    @Override
    public List<String> getOuGuidList(Date startDate) {
        return new SyncTaskProjectJobService().getOuGuidList(startDate);
    }

    @Override
    public List<Record> getInfos(Date startDate, String ouGuid) {
        return new SyncTaskProjectJobService().getInfos(startDate, ouGuid);
    }

    @Override
    public int deleteExistDate(Date startDate, String ouGuid) {
        return new SyncTaskProjectJobService().deleteExistDate(startDate, ouGuid);
    }

    @Override
    public void insertNewRecord(Record value) {
        new SyncTaskProjectJobService().insertNewRecord(value);
    }

    @Override
    public List<Record> getTjInfo(Date startDate, String ouGuid) {
        return new SyncTaskProjectJobService().getTjInfo(startDate, ouGuid);
    }
}
