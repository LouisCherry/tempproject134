package com.epoint.sghd.auditmember.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.sghd.auditmember.api.IJnAuditOrgaMember;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@Component
public class JnAuditOrgaMemberServiceImpl implements IJnAuditOrgaMember {

    /**
     *
     */
    private static final long serialVersionUID = -1524663021103775320L;

    @Override
    public int updateIndivAuditMember(String isAdvanced, String isBiaoBing, String isRed, String rowguid) {
        return new JnAuditOrgaMemberService().updateIndivAuditMember(isAdvanced, isBiaoBing, isRed, rowguid);
    }

    @Override
    public List<Record> getAuditMemberIndivColum(String rowguid) {
        return new JnAuditOrgaMemberService().getAuditMemberIndivColum(rowguid);
    }

}
