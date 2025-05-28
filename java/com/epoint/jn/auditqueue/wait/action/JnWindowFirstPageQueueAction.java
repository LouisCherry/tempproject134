package com.epoint.jn.auditqueue.wait.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.string.StringUtil;

@RestController("jnwindowfirstpagequeueaction")
@Scope("request")
public class JnWindowFirstPageQueueAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1443529820L;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Override
    public void pageLoad() {
    }

    public void getFirstPage(String MacAddress) {
        if (StringUtil.isNotBlank(MacAddress)) {
            String url = getFirstPageUrl(MacAddress);
            if ("status=0".equals(url)) {
                addCallbackParam("msg", "该设备" + MacAddress + "的状态离线！");
            }
            else {
                if (StringUtil.isNotBlank(url)) {
                    redirect("znsb/epointqueue/wait/" + url);
                }
                else {
                    addCallbackParam("msg", "该设备的页面地址没有配置！");
                }
            }
        }
    }

    public String getFirstPageUrl(String MacAddress) {
        String FirstPageUrl = "";
        String Homepageurl = "";
        String Centerguid = "";
        String Windowguid = "";
        String hallguid = "";

        AuditZnsbEquipment auditqueueequipment = equipmentservice.getDetailbyMacaddress(MacAddress).getResult();
        if (auditqueueequipment != null) {
            if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(auditqueueequipment.getStatus())) {
                FirstPageUrl = "status=0";
            }
            else {
                // 等待屏
                if (QueueConstant.EQUIPMENT_TYPE_DDP.equals(auditqueueequipment.getMachinetype())) {
                    hallguid = auditqueueequipment.getHallguid();
                    Centerguid = auditqueueequipment.getCenterguid();
                    Homepageurl = auditqueueequipment.getHomepageurl();
                    if (Homepageurl.contains("?")) {
                        if (StringUtil.isNotBlank(hallguid)) {
                            FirstPageUrl = Homepageurl + "&hallguid=" + hallguid + "&Centerguid=" + Centerguid
                                    + "&MacAddress=" + MacAddress;
                            ;
                        }
                    }
                    else {
                        if (StringUtil.isNotBlank(hallguid)) {
                            FirstPageUrl = Homepageurl + "?hallguid=" + hallguid + "&Centerguid=" + Centerguid
                                    + "&MacAddress=" + MacAddress;
                        }
                    }

                }
                // 窗口屏
                else if (QueueConstant.EQUIPMENT_TYPE_CKP.equals(auditqueueequipment.getMachinetype())) {
                    Windowguid = auditqueueequipment.getWindowguid();
                    Centerguid = auditqueueequipment.getCenterguid();
                    Homepageurl = auditqueueequipment.getHomepageurl();
                    if (Homepageurl.contains("?")) {
                        if (StringUtil.isNotBlank(Windowguid)) {
                            FirstPageUrl = Homepageurl + "&windowguid=" + Windowguid + "&Centerguid=" + Centerguid
                                    + "&MacAddress=" + MacAddress;
                            ;
                        }
                    }
                    else {
                        if (StringUtil.isNotBlank(Windowguid)) {
                            FirstPageUrl = Homepageurl + "?windowguid=" + Windowguid + "&Centerguid=" + Centerguid
                                    + "&MacAddress=" + MacAddress;
                            ;
                        }
                    }

                }
            }

        }

        return FirstPageUrl;
    }

}
