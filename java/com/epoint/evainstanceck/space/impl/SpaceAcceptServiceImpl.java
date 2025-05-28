package com.epoint.evainstanceck.space.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.evainstanceck.space.api.ISpaceAcceptService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class SpaceAcceptServiceImpl implements ISpaceAcceptService
{

    @Override
    public List<Record> getGradinfo(String grade) {
        return new SpaceAcceptService().getGradinfo(grade);
    }

    @Override
    public Record getHcpStati(String value) {
        return new SpaceAcceptService().getHcpStati(value);
    }
    
    @Override
    public Record getstaicfiedByTaskguid(String taskguid) {
    	return new SpaceAcceptService().getstaicfiedByTaskguid(taskguid);
    }
    
    @Override
    public Record getevaluatStatiByTaskguid(String taskguid) {
    	return new SpaceAcceptService().getevaluatStatiByTaskguid(taskguid);
    }


}
