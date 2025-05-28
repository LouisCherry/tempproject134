package com.epoint.auditqueue.qhj.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueue.qhj.api.IJNOulistinner;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@Component
@Service
public class JNOulistinnerImpl implements IJNOulistinner
{
    @Override
    public List<FrameOu> getOrderdLinkedOulist(int firstpage,int pagesize,String condition){
        JNOulistinnerService service = new JNOulistinnerService();
        return service.getOrderdLinkedOulist(firstpage, pagesize, condition);
    }
    @Override
    public int getOrderdLinkedOucount(String condition){
        JNOulistinnerService service = new JNOulistinnerService();
        return service.getOrderdLinkedOucount(condition);
    }
}
