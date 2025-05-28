package com.epoint.zoucheng.znsb.auditqueue.qhj.action;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController("zcqhjfirstpagequeueaction")
@Scope("request")
public class ZCQHJFirstPageQueueAction extends BaseController {
    private static final long serialVersionUID = 1443529820L;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    
    @Autowired
    private IHandleConfig configService;

    public void pageLoad() {
    }

    public void getFirstPage(String MacAddress) {
        if (StringUtil.isNotBlank(MacAddress)) {
            this.addCallbackParam("pageURL", this.getPageUrl(MacAddress));
        }

    }

    public String getPageUrl(String MacAddress) {
     
        String FirstPageUrl = "";
        String Centerguid = "";
        String hallguid = "";
        String Homepageurl = "";
        AuditZnsbEquipment equipment;
        if (!(Boolean) this.equipmentservice.IsTerminalRegister(MacAddress).getResult()) {
            equipment = new AuditZnsbEquipment();
            equipment.setMacaddress(MacAddress);
            equipment.setStatus("1");
            equipment.setMachinetype("1");
            equipment.setIsneedpass("1");
            equipment.setIsuseappointment("1");
            equipment.setLeftpaperpiece(100);
            equipment.setRowguid(UUID.randomUUID().toString());
            equipment.setOperatedate(new Date());
            this.equipmentservice.insertEquipment(equipment);
        }

      
        equipment = (AuditZnsbEquipment) this.equipmentservice.getDetailbyMacaddress(MacAddress).getResult();
        
        
        if (StringUtil.isNotBlank(equipment)) {
            if ("1".equals(equipment.getStatus())) {
                Homepageurl = equipment.getHomepageurl();
                Centerguid = equipment.getCenterguid();
                hallguid = equipment.getHallguid();
                
                String noneedwaitecord = "&noneedwaitecord=0";
                String macAddresses = configService.getFrameConfig("AS_ZNSB_NONEEDWRITECORD", Centerguid).getResult();
                if(StringUtil.isNotBlank(macAddresses) && macAddresses.indexOf(MacAddress) != -1){
                    noneedwaitecord = "&noneedwaitecord=1";
                }
                
                if (StringUtil.isNotBlank(Centerguid)) {
                    if ("1".equals(equipment.getIsuseappointment())) {
                        if ("all".equals(hallguid)) {
                            FirstPageUrl = "qhjlogin?Type=APPOINTMENT&CurrentPage=" + Homepageurl + "&Centerguid="
                                    + Centerguid + "&hallguid=all" + "&IsneedPass=" + equipment.getIsneedpass() + noneedwaitecord
                                    + "&MacAddress=" + MacAddress;
                        } else {
                            FirstPageUrl = "qhjlogin?Type=APPOINTMENT&CurrentPage=" + Homepageurl + "&Centerguid="
                                    + Centerguid + "&hallguid=" + hallguid + "&IsneedPass=" + equipment.getIsneedpass() + noneedwaitecord
                                    + "&MacAddress=" + MacAddress;
                        }
                    } else if ("all".equals(hallguid)) {
                        FirstPageUrl = "qhjframe?Type=NOAPPOINTMENT&CurrentPage=" + Homepageurl + "&Centerguid="
                                + Centerguid + "&hallguid=all" + "&IsneedPass=" + equipment.getIsneedpass()+ noneedwaitecord
                                + "&MacAddress=" + MacAddress;
                    } else {
                        FirstPageUrl = "qhjframe?Type=NOAPPOINTMENT&CurrentPage=" + Homepageurl + "&Centerguid="
                                + Centerguid + "&hallguid=" + hallguid + "&IsneedPass=" + equipment.getIsneedpass()+ noneedwaitecord
                                + "&MacAddress=" + MacAddress;
                    }
                } else {
                    FirstPageUrl = "status=1";
                }
            } else {
                FirstPageUrl = "status=0";
            }
        }

        return FirstPageUrl;
    }
}