package com.epoint.screen.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.screen.api.ILyAuditVisitCount;
import com.epoint.screen.api.LyAuditVisitCountService;

@Component
@Service
public class LyAuditVisitCountImpl implements ILyAuditVisitCount {

    @Override
    public int findVisitcount(String searchtimefrom, String searchtimeto) {
        return new LyAuditVisitCountService().findVisitcount(searchtimefrom, searchtimeto);
    }

    @Override
    public int findRegistercount() {
        return new LyAuditVisitCountService().findRegistercount();
    }
    
}
