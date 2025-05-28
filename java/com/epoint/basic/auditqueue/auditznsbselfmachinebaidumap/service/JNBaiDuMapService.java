package com.epoint.basic.auditqueue.auditznsbselfmachinebaidumap.service;

import java.util.List;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.common.service.AuditCommonService;

public class JNBaiDuMapService extends AuditCommonService
{
    private static final long serialVersionUID = -5272077358546683422L;

    private static final double EARTH_RADIUS = 6378.137;// 地球半径,单位千米

    /**
     * 
     * [获取所有中心下机器的位置]
     * 
     * @return
     */
    public List<AuditZnsbEquipment> getAllMachineLocationList() {
        String sql = "select MACHINENO,MACHINENAME,longitude,latitude,centerguid,macaddress from audit_znsb_equipment where latitude!='' and longitude!=''";
        return commonDao.findList(sql, AuditZnsbEquipment.class);
    }

    /**
     * 
     * [获取指定中心下所有机器的位置]
     * 
     * @param centerguid
     *            中心guid
     * @return
     */
    public List<AuditZnsbEquipment> getMachineLocationListByCenterGuid(String centerguid) {
        String sql = "select machineno, machinename, longitude, latitude, centerguid,macaddress  from audit_znsb_equipment where CenterGuid=? and latitude!='' and longitude!=''";
        return commonDao.findList(sql, AuditZnsbEquipment.class, centerguid);
    }

    public AuditZnsbEquipment getCurrentMachineLocation(String macaddress) {
        String sql = "select longitude,latitude,machineno,centerguid,machinename from audit_znsb_equipment where macaddress=? and MACHINETYPE=3  ";
        return commonDao.find(sql, AuditZnsbEquipment.class, macaddress);
    }
}
