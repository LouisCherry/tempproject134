package com.epoint.auditybj.impl;

import com.epoint.auditybj.api.IWFPVOP;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class WFPVOPServiceImpl implements IWFPVOP {
    @Override
    public Integer getCountByPvguidAndUserguid(String Pvguid, String Userguid) {
        return new WFPVOPService().getCountByPvguidAndUserguid(Pvguid, Userguid);
    }

    @Override
    public List<String> getPviguidByUserguid(String Userguid) {
        return new WFPVOPService().getPviguidByUserguid(Userguid);
    }

    @Override
    public List<String> getProjectguidByUserguid(String userguid) {
        return new WFPVOPService().getProjectguidByUserguid(userguid);
    }

}
