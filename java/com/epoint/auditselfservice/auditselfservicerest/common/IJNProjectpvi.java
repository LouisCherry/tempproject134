package com.epoint.auditselfservice.auditselfservicerest.common;

import java.util.List;

import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public interface IJNProjectpvi
{

    List<WorkflowWorkItem> getworkflowbypvi(String pviguid);

}
