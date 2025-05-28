package com.epoint.basic.auditsp.dantiinfo.action;

import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目单体信息表详情页面对应的后台
 *
 * @author ysai
 * @version [版本号, 2023-10-18 16:07:29]
 */
@RestController("jndantiinfodetailaction")
@Scope("request")
public class JnDantiInfoDetailAction extends BaseController {

    @Autowired
    private IDantiInfoService infoService;

    private DantiInfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = infoService.find(guid);
        if (dataBean != null) {
            if (StringUtil.isNotBlank(dataBean.getJiegoutx())) {
                if (dataBean.getJiegoutx().toString().length() == 1) {
                    dataBean.set("jgtx", "0" + dataBean.getJiegoutx().toString());// 结构体系
                }
            }
        } else {
            dataBean = new DantiInfo();
        }
    }

    public DantiInfo getDataBean() {
        return dataBean;
    }

    public void setDantiInfo(DantiInfo dantiInfo) {
        this.dataBean = dantiInfo;
    }

}
