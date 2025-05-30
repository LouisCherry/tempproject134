package com.epoint.jnzwdt.jnggtask.impl;

import com.epoint.jnzwdt.jnggtask.api.IJnAuditSpBaseTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JnAuditSpBaseTaskServiceImpl implements IJnAuditSpBaseTaskService {
    @Override
    public List<String> listTaskIdsByPhaseGuid(String phaseguid) {
        return new JnAuditSpBaseTaskService().listTaskIdsByPhaseGuid(phaseguid);
    }

    @Override
    public List<String> listTaskIdsByBusinessGuid(String businessGuid) {
        return new JnAuditSpBaseTaskService().listTaskIdsByBusinessGuid(businessGuid);
    }
}
