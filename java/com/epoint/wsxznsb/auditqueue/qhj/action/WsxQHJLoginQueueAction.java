package com.epoint.wsxznsb.auditqueue.qhj.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;

@RestController("wsxqhjloginqueueaction")
@Scope("request")
public class WsxQHJLoginQueueAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1440961262223529820L;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IHandleConfig handleconfig;

    @Override
    public void pageLoad() {
    }

    public void getLeftPiece(String MacAddress) {
        if (StringUtil.isNotBlank(MacAddress)) {
            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String isBlink = "no";
            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(MacAddress, " centerguid, Alertlength,Leftpaperpiece ").getResult();
            if (StringUtil.isNotBlank(equipment)) {
                alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
                leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();

                if (alertlength >= leftpaperpiece) {
                    isBlink = "is";
                }
                if (leftpaperpiece == 0) {
                    isBlink = "red";
                }
                String loveqnoconfigvalue = handleconfig
                        .getFrameConfig("AS_ZNSB_USE_LOVEQNO", equipment.getCenterguid()).getResult();
                if (StringUtil.isNotBlank(loveqnoconfigvalue)) {
                    addCallbackParam("loveqnoconfigvalue", loveqnoconfigvalue);
                }
                String privateconfigvalue = handleconfig
                        .getFrameConfig("AS_ZNSB_QHJ_PRIVACY", equipment.getCenterguid()).getResult();
                if (StringUtil.isNotBlank(privateconfigvalue)) {
                    addCallbackParam("privateconfigvalue", privateconfigvalue);
                }

            }
            addCallbackParam("leftpiece", String.valueOf(leftpaperpiece));
            addCallbackParam("isBlink", isBlink);
        }
    }

}
