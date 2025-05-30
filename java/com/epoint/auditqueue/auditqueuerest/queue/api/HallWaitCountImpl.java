package com.epoint.auditqueue.auditqueuerest.queue.api;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

@Component
@Service
public class HallWaitCountImpl implements IHallWaitCount
{
    @Override
    public int getHallWaitCount(String Hallguid,String status){
        HallWaitCountService service = new HallWaitCountService();
        return   service.getHallWaitCount(Hallguid,status);
    }
}
