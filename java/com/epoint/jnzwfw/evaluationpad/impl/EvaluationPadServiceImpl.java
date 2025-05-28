package com.epoint.jnzwfw.evaluationpad.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.evaluationpad.api.IEvaluationPadService;

@Component
@Service
public class EvaluationPadServiceImpl implements IEvaluationPadService
{

    /**
     * 
     */
    private static final long serialVersionUID = 785890304510655530L;

    @Override
    public String findOUTel(String windowguid) {
        return new EvaluationPadService().findOUTel(windowguid);
    }

    @Override
    public Record findUserInfo(String userguid) {
        return new EvaluationPadService().findUserInfo(userguid);
    }

    @Override
    public List<Record> findTaskInfoList(String windowguid) {
        return new EvaluationPadService().findTaskInfoList(windowguid);
    }

    @Override
    public String getWindowguidByMacAddress(String macaddress) {
        return new EvaluationPadService().getWindowguidByMacAddress(macaddress);
    }

    @Override
    public String getAttachguidByCliengguid(String str) {
        return new EvaluationPadService().getAttachguidByCliengguid(str);
    }

    @Override
    public int updateQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo) {
        return new EvaluationPadService().updateQRcodeinfoByprojectGuid(projectGuid, qRcodeinfo);
    }
    
    @Override
    public int updateJSTcodeinfoByprojectGuid(String projectGuid, String jstcodeinfo) {
        return new EvaluationPadService().updateJSTcodeinfoByprojectGuid(projectGuid, jstcodeinfo);
    }

    @Override
    public int updateLegalQRcodeinfoByprojectGuid(String projectGuid, String qRcodeinfo) {
        return new EvaluationPadService().updateLegalQRcodeinfoByprojectGuid(projectGuid, qRcodeinfo);
    }

}
