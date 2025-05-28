package com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.inter.IJNBaiDuMap;
import com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.service.JNBaiDuMapService;

@Service
@Component
public class JNBaiDuMapImpl implements IJNBaiDuMap
{

    /**
     * 
     * [获取机器的位置]
     * 
     * @param centerguid
     *            中心guid，为all表示所有，否则按centerguid查询
     * @return
     */
    @Override
    public List<AuditZnsbEquipment> getMachineLocationList(String centerguid) {
        List<AuditZnsbEquipment> result = new ArrayList<>();
        JNBaiDuMapService service = new JNBaiDuMapService();

        if ("all".equals(centerguid)) {
            result = service.getAllMachineLocationList();
        }
        else {
            result = service.getMachineLocationListByCenterGuid(centerguid);
        }

        return result;
    }

    @Override
    public AuditZnsbEquipment getCurrentMachineLocation(String macaddress) {
        JNBaiDuMapService service = new JNBaiDuMapService();
        return service.getCurrentMachineLocation(macaddress);
    }

}
