package com.epoint.jnzwdt.jnggtask.api;

import java.util.List;

public interface IJnAuditSpBaseTaskService {
    List<String> listTaskIdsByPhaseGuid(String phaseguid);

    List<String> listTaskIdsByBusinessGuid(String businessGuid);
}
