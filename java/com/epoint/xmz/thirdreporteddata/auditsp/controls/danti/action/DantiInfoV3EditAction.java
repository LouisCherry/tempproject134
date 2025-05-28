package com.epoint.xmz.thirdreporteddata.auditsp.controls.danti.action;

import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目单体信息表修改页面对应的后台
 *
 * @author ysai
 * @version [版本号, 2023-10-18 16:07:29]
 */
@RightRelation(DantiInfov3ListAction.class)
@RestController("dantiinfov3editaction")
@Scope("request")
public class DantiInfoV3EditAction extends BaseController {

    @Autowired
    private IDantiInfoV3Service service;

    @Autowired
    private IDantiInfoService dantiInfoService;

    /**
     * 项目单体信息表实体对象
     */
    private DantiInfoV3 dataBean = null;

    private DantiInfo info = null;

    /**
     * 工程类别下拉列表model
     */
    private List<SelectItem> gcytModel = null;
    /**
     * 结构体系下拉列表model
     */
    private List<SelectItem> jgtxModel = null;
    /**
     * 耐火等级下拉列表model
     */
    private List<SelectItem> nhdjModel = null;
    /**
     * 建造方式下拉列表model
     */
    private List<SelectItem> jzfsModel = null;

    String gcfl = "";

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        gcfl = getRequestParameter("gcfl");

        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new DantiInfoV3();
        }

        info = dantiInfoService.find(guid);
        if (info == null) {
            info = new DantiInfo();
        }

    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setIs_enable(ZwfwConstant.CONSTANT_STR_ONE);
        service.update(dataBean);

        if (info != null) {
            info.setOperatedate(new Date());
            info.setDantiname(dataBean.getDtmc());
            info.setGclb(dataBean.getGcyt());
            if (StringUtil.isNotBlank(dataBean.getJgtx())) {
                info.setJiegoutx(Integer.parseInt(dataBean.getJgtx()));// 结构体系
            }
            info.setFirelevel(dataBean.getNhdj());// 耐火等级
            info.set("jzfs", dataBean.getJzfs());// 建造方式
            info.setPrice(dataBean.getDtgczzj());// 单体工程总造价
            info.setZjzmj(dataBean.getJzmj());// 建筑面积
            info.set("zdmj", dataBean.getZdmj());// 占地面积
            if (StringUtil.isNotBlank(dataBean.getDsjzmj())) {
                info.setDishangmianji(dataBean.getDsjzmj().toString());// 地上建筑面积
            }
            if (StringUtil.isNotBlank(dataBean.getDxjzmj())) {
                info.setDixiamianji(dataBean.getDxjzmj().toString());// 地下建筑面积
            }
            info.setDscs(dataBean.getDscs());// 地上层数
            info.setDxcs(dataBean.getDxcs());// 地下层数
            info.setJzgd(dataBean.getJzgcgd());// 建筑工程高度
            info.set("cd", dataBean.getCd());// 长度
            if (StringUtil.isNotBlank(dataBean.getKd())) {
                info.setSpan(dataBean.getKd().toString());// 跨度
            }
            info.set("dtjwdzb", dataBean.getDtjwdzb());// 单体经纬度坐标
            info.set("gmzb", dataBean.getGmzb());// 规模指标
            info.set("xkbabh", dataBean.getXkbabh());//许可（备案、技术审查）编号
            dantiInfoService.update(info);
        }
        addCallbackParam("msg", "修改成功！");
    }

    public DantiInfoV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(DantiInfoV3 dataBean) {
        this.dataBean = dataBean;
    }

    public DantiInfo getInfo() {
        return info;
    }

    public void setInfo(DantiInfo info) {
        this.info = info;
    }

    public List<SelectItem> getGcytModel() {
        if (gcytModel == null) {
            gcytModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_工程类别", null, false));
            gcytModel.removeIf(a -> {
                String value = (String) a.getValue();
                gcfl = String.format("%2s", gcfl).replaceAll(" ", "0");
                return !value.startsWith(gcfl);
            });
        }
        return this.gcytModel;
    }

    public List<SelectItem> getJgtxModel() {
        if (jgtxModel == null) {
            jgtxModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_结构体系", null, false));
        }
        return this.jgtxModel;
    }

    public List<SelectItem> getNhdjModel() {
        if (nhdjModel == null) {
            nhdjModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_耐火等级", null, false));
        }
        return this.nhdjModel;
    }

    public List<SelectItem> getJzfsModel() {
        if (jzfsModel == null) {
            jzfsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_建造方式", null, false));
        }
        return this.jzfsModel;
    }

}
