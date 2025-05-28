package com.epoint.auditybj.api;

import java.util.List;

public interface IWFPVOP {
    public Integer getCountByPvguidAndUserguid(String Pvguid, String Userguid);

    public List<String> getPviguidByUserguid(String Userguid);

    List<String> getProjectguidByUserguid(String userguid);

}
