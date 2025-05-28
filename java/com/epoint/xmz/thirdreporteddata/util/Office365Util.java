package com.epoint.xmz.thirdreporteddata.util;

import com.epoint.common.util.OfficeWebUrlEncryptUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

public class Office365Util {
    public static String get365Url(FrameAttachInfo frameAttachInfo) {
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String web365Server = configService.getFrameConfigValue("AS_OfficeWeb365_Server");
        String zwfwMsgurl = configService.getFrameConfigValue("zwfwMsgurl");
        String attachDownPath = zwfwMsgurl + "/" + iAttachService.getAttachDownPath(frameAttachInfo);
        String encryptUrl = OfficeWebUrlEncryptUtil.getEncryptUrl(attachDownPath, frameAttachInfo.getContentType());
        return "http://" + web365Server + "?fname=" + frameAttachInfo.getAttachGuid() + frameAttachInfo.getContentType()
                + "&furl=" + encryptUrl;
    }
}
