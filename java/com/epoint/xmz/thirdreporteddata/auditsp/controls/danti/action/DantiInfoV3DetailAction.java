package com.epoint.xmz.thirdreporteddata.auditsp.controls.danti.action;

import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目单体信息表详情页面对应的后台
 *
 * @author ysai
 * @version [版本号, 2023-10-18 16:07:29]
 */
@RestController("dantiinfov3detailaction")
@Scope("request")
public class DantiInfoV3DetailAction extends BaseController {

    @Autowired
    private IDantiInfoV3Service service;

    @Autowired
    private IDantiInfoService infoService;

    /**
     * 项目单体信息表实体对象
     */
    private DantiInfoV3 dataBean = null;

    private DantiInfo dantiInfo = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dantiInfo = infoService.find(guid);
            if (dantiInfo != null) {
                dataBean = new DantiInfoV3();
                dataBean.setDtmc(dantiInfo.getDantiname());
                dataBean.setGcyt(dantiInfo.getGclb());// 工程类别
                dataBean.setDtbm(dantiInfo.getStr("dtbm"));// 单体编码
                dataBean.setXkbabh(dantiInfo.getStr("xkbabh"));
                if (StringUtil.isNotBlank(dantiInfo.getJiegoutx())) {
                    if (dantiInfo.getJiegoutx().toString().length() == 1) {
                        dataBean.setJgtx("0" + dantiInfo.getJiegoutx().toString());// 结构体系
                    }
                }

                dataBean.setNhdj(dantiInfo.getFirelevel());// 耐火等级
                if (StringUtil.isNotBlank(dantiInfo.getInt("jzfs"))) {
                    dataBean.setJzfs(dantiInfo.getInt("jzfs"));// 建造方式
                }

                dataBean.setDtgczzj(dantiInfo.getPrice());// 单体工程总造价
                dataBean.setJzmj(dantiInfo.getZjzmj());// 建筑面积

                if (StringUtil.isNotBlank(dantiInfo.getStr("zdmj"))) {
                    dataBean.setZdmj(Double.valueOf(dantiInfo.getStr("zdmj")));// 占地面积
                }
                if (StringUtil.isNotBlank(dantiInfo.getDishangmianji())) {
                    dataBean.setDsjzmj(Double.valueOf(dantiInfo.getDishangmianji()));// 地上建筑面积
                }
                if (StringUtil.isNotBlank(dantiInfo.getDixiamianji())) {
                    dataBean.setDxjzmj(Double.valueOf(dantiInfo.getDixiamianji()));// 地下建筑面积
                }
                dataBean.setDscs(dantiInfo.getDscs());// 地上层数
                dataBean.setDxcs(dantiInfo.getDxcs());// 地下层数
                dataBean.setJzgcgd(dantiInfo.getJzgd());// 建筑工程高度
                if (StringUtil.isNotBlank(dantiInfo.getStr("cd"))) {
                    dataBean.setCd(Double.valueOf(dantiInfo.getStr("cd")));// 长度
                }
                if (StringUtil.isNotBlank(dantiInfo.getSpan())) {
                    dataBean.setKd(Double.valueOf(dantiInfo.getSpan()));// 跨度
                }
                dataBean.setDtjwdzb(dantiInfo.getStr("dtjwdzb"));// 单体经纬度坐标
                dataBean.setGmzb(dantiInfo.getStr("gmzb"));// 规模指标

            } else {
                dataBean = new DantiInfoV3();
            }
        }
    }

    public DantiInfoV3 getDataBean() {
        return dataBean;
    }

    public DantiInfo getDantiInfo() {
        return dantiInfo;
    }

    public void setDantiInfo(DantiInfo dantiInfo) {
        this.dantiInfo = dantiInfo;
    }

}
