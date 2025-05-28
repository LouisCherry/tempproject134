package com.epoint.jiningzwfw.importstatistics.api;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.core.grammar.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IImportService {

    List<AuditOrgaArea> getAreaWithLevel(int level, String XiaQuCode);

    Record findProjectStatistics(Map<String, String> map);

    Record findProjectStatistics1(Map<String, String> map);

    Record findEvaInstanceStatistics(HashMap<String, String> map);
}
