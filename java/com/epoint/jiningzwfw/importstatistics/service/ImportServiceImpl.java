package com.epoint.jiningzwfw.importstatistics.service;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.importstatistics.api.IImportService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportServiceImpl implements IImportService {


    @Override
    public List<AuditOrgaArea> getAreaWithLevel(int level, String XiaQuCode) {
        return new ImportService().getAreaWithLevel(level, XiaQuCode);
    }

    @Override
    public Record findProjectStatistics(Map<String, String> map) {
        return new ImportService().findProjectStatistics(map);
    }


    @Override
    public Record findProjectStatistics1(Map<String, String> map) {
        return new ImportService().findProjectStatistics1(map);
    }

    @Override
    public Record findEvaInstanceStatistics(HashMap<String, String> map) {
        return new ImportService().findEvaInstanceStatistics(map);
    }
}
