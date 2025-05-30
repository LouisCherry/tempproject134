package com.epoint.zwdt.zwdtrest.space.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.job.yqarea;
import com.epoint.xmz.job.yqbm;
import com.epoint.xmz.job.yqdog;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;

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
    
    public int insert(yqarea record) {
        return new SpaceAcceptService().insert(record);
    }
    
    public int insertDog(yqdog record) {
    	return new SpaceAcceptService().insertDog(record);
    }
    public int insertYqbm(yqbm record) {
    	return new SpaceAcceptService().insertYqbm(record);
    }
    


}
