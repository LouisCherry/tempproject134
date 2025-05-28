package com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.inter;

import java.util.List;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;

public interface IJNBaiDuMap
{

    /**
     * 
     * [获取机器的位置]
     * 
     * @param centerguid
     *            中心guid，为all表示所有，否则按centerguid查询
     * @return
     */
    public List<AuditZnsbEquipment> getMachineLocationList(String centerguid);

    AuditZnsbEquipment getCurrentMachineLocation(String macaddress);

}
