package com.epoint.common.util;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * @author hzchen
 * @version 1.0
 * @date 2024/10/30 10:43
 */
public class Zjconstant
{
    public static String getOunamebyOuguid(String ouguid) {
        IOuServiceInternal frameOUService = ContainerFactory.getContainInfo().getComponent(IOuServiceInternal.class);
        IAuditOrgaArea iauditorgaarea = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        String areaCode = frameOUService.getFrameOuExtendInfo(ouguid).get("areacode");
        AuditOrgaArea auditOrgaArea = iauditorgaarea.getAreaByAreacode(areaCode).getResult();
        FrameOu frameou = frameOUService.getOuByOuGuid(ouguid);
        if (frameou == null) {
            return null;
        }
        else {
            String ouname = frameou.getOuname();
            if (auditOrgaArea != null) {
                ouname = auditOrgaArea.getXiaquname() + ouname;
                if ("2".equals(auditOrgaArea.getCitylevel())) {
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.eq("citylevel", "1");
                    AuditOrgaArea area = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (area != null) {
                        ouname = area.getXiaquname() + ouname;
                    }
                }
            }

            return ouname;
        }
    }
}
