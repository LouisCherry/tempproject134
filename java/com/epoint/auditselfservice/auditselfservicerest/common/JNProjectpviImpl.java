package com.epoint.auditselfservice.auditselfservicerest.common;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
@Component
@Service
public class JNProjectpviImpl implements IJNProjectpvi
{
    @Override
    public  List<WorkflowWorkItem> getworkflowbypvi(String pviguid){
        JNProjectpviService service = new JNProjectpviService();
        return service.getworkflowbypvi(pviguid);
    }

}
