package com.epoint.auditqueue.qhj.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueue.qhj.api.IJNTasktypeList;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.core.grammar.Record;
@Service
@Component
public class JNTasktypeListImpl implements IJNTasktypeList
{
    @Override
    public List<Record> getWindowLinkedTskType(String centerguid,String ouguid,int firstpage,int pagesize){
        JNTasktypeListService service = new JNTasktypeListService();
        return  service.getWindowLinkedTskType(centerguid, ouguid, firstpage, pagesize);
    }
    @Override
    public int getWindowLinkedTskTypeCount(String centerguid,String ouguid){
        JNTasktypeListService service = new JNTasktypeListService();
        return  service.getWindowLinkedTskTypeCount(centerguid, ouguid);
    }
}
