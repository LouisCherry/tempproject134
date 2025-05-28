package com.epoint.jn.zndf.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.jn.zndf.api.IJnzndf;

@Service
@Component
public class JnZndfImpl implements IJnzndf
{

    @Override
    public AuditCommonResult<List<Record>> getTaskNameByPos(String pos) {
        JnZndfService service=new JnZndfService();
        AuditCommonResult<List<Record>> result=new  AuditCommonResult<List<Record>>();
        try {
            result.setResult(service.getTaskNameByPos(pos));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getWindowByTaskguid(String taskguid) {
        JnZndfService service=new JnZndfService();
        AuditCommonResult<String> result=new  AuditCommonResult<String>();
        try {
            result.setResult(service.getWindowByTaskguid(taskguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNumByMac(String mac) {
        JnZndfService service=new JnZndfService();
        AuditCommonResult<String> result=new  AuditCommonResult<String>();
        try {
            result.setResult(service.getNumByMac(mac));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWindowNameByLobby(String lobbytype) {
        JnZndfService service=new JnZndfService();
        AuditCommonResult<List<Record>> result=new  AuditCommonResult<List<Record>>();
        try {
            result.setResult(service.getWindowNameByLobby(lobbytype));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

}
