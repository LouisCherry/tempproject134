package com.epoint.auditsp.dantiinfo.action;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.api.IJnDantiinfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

public class JnDantiInfoUtil
{

    /**
     * 在新数据未保存前处理对接
     * [一句话功能简述]
     * 
     * @param dantiInfo
     *            新v3数据
     * @param info
     *            新v数据
     * @param dantiguid
     *            单体主键
     * @param itemguid
     *            projectguid
     * @return
     */
    public static String handleDantiFm(DantiInfoV3 dantiInfo, DantiInfo info, String dantiguid, String itemguid) {
        IJnDantiinfoService iJnDantiinfoService = ContainerFactory.getContainInfo()
                .getComponent(IJnDantiinfoService.class);
        IDantiInfoService iDantiInfoService = ContainerFactory.getContainInfo().getComponent(IDantiInfoService.class);
        IDantiInfoV3Service iDantiInfoV3Service = ContainerFactory.getContainInfo()
                .getComponent(IDantiInfoV3Service.class);
        // 旧的单体信息
        DantiInfoV3 dantiInfoExit = iDantiInfoV3Service.find(dantiguid);
        DantiInfo dantiInfoExit2 = iDantiInfoService.find(dantiguid);

        /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
        // 调用三方接口中的【推送项目信息】获取对应返回值的id（项目id）
        JSONObject insertInfo = iJnDantiinfoService.insertInfo(itemguid);
        if (insertInfo == null) {
            return "推送项目信息失败";
        }
        if (StringUtil.isNotBlank(insertInfo.get("msg"))) {
            return insertInfo.getString("msg");
        }
        if (!insertInfo.containsKey("data") || insertInfo.getJSONObject("data") == null
                || insertInfo.getJSONObject("data").isEmpty()) {
            return "图审系统未返回项目信息";
        }
        String itemId = insertInfo.getJSONObject("data").getString("itemId");
        if (StringUtil.isBlank(itemId)) {
            return "图审系统未返回项目id";
        }
        /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
        if (dantiInfoExit != null && dantiInfoExit2 != null) {
            if (StringUtil.isBlank(dantiInfoExit.getIs_enable())
                    || ZwfwConstant.CONSTANT_STR_ZERO.equals(dantiInfoExit.getIs_enable())) {
                dantiInfo.setIs_enable(ZwfwConstant.CONSTANT_STR_ONE);
            }
            /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
            JSONArray danTiInfos = new JSONArray();
            JSONObject danTiInfo = new JSONObject();
            // 修改了单体名称才能更新单体赋码
            if (dantiInfoExit != null && !dantiInfo.getDtmc().equals(dantiInfoExit.getDtmc())) {
                danTiInfo.put("id", dantiInfoExit.getZjid());
                danTiInfo.put("Itemid", itemId);
                danTiInfo.put("SubName", dantiInfo.getDtmc());
                danTiInfos.add(danTiInfo);
                // 根据id调用【更新单体】接口
                JSONObject updateSubsAsync = iJnDantiinfoService.updateSubsAsync(danTiInfos);
                if (updateSubsAsync == null) {
                    return "保存失败";
                }
                if (StringUtil.isNotBlank(updateSubsAsync.get("msg"))) {
                    return updateSubsAsync.getString("msg");
                }
                if (!updateSubsAsync.containsKey("data") || updateSubsAsync.getJSONArray("data") == null
                        || updateSubsAsync.getJSONArray("data").size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                    return "图审系统未返回单体信息";
                }
                // String subCode = ((JSONObject)
                // (updateSubsAsync.getJSONArray("data")
                // .get(ZwfwConstant.CONSTANT_INT_ZERO))).getString("subCode");
                // if (StringUtil.isBlank(subCode)) {
                // return
                // JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO,
                // "图审系统未返回单体赋码", "");
                // }
                // dantiInfo.setDtbm(subCode);
                // info.set("dtbm", subCode);
                // info.set("isfm", ZwfwConstant.CONSTANT_STR_ONE);
            }
            /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
            iDantiInfoV3Service.update(dantiInfo);
            iDantiInfoService.update(info);
            return null;
        }
        else {
            /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
            JSONArray danTiInfos = new JSONArray();
            JSONObject danTiInfo = new JSONObject();
            danTiInfo.put("Itemid", itemId);
            danTiInfo.put("SubName", dantiInfo.getDtmc());
            danTiInfos.add(danTiInfo);
            // 根据id调用【新增单体】接口
            JSONObject insertSubsAsync = iJnDantiinfoService.insertSubsAsync(danTiInfos);
            if (insertSubsAsync == null) {
                return "保存失败";
            }
            if (StringUtil.isNotBlank(insertSubsAsync.get("msg"))) {
                return insertSubsAsync.getString("msg");
            }
            if (!insertSubsAsync.containsKey("data") || insertSubsAsync.getJSONArray("data") == null
                    || insertSubsAsync.getJSONArray("data").size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                return "图审系统未返回单体信息";
            }
            String subCode = ((JSONObject) (insertSubsAsync.getJSONArray("data").get(ZwfwConstant.CONSTANT_INT_ZERO)))
                    .getString("subCode");
            if (StringUtil.isBlank(subCode)) {
                return "图审系统未返回单体赋码";
            }
            String zjid = ((JSONObject) (insertSubsAsync.getJSONArray("data").get(ZwfwConstant.CONSTANT_INT_ZERO)))
                    .getString("id");
            if (StringUtil.isBlank(zjid)) {
                return "图审系统未返回单体id";
            }
            dantiInfo.setDtbm(subCode);
            dantiInfo.setZjid(zjid);
            info.set("dtbm", subCode);
            info.set("isfm", ZwfwConstant.CONSTANT_STR_ONE);
            /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
            dantiInfo.setIs_enable("1");// 是否完善
            dantiInfo.setCreatedate(new Date());
            iDantiInfoV3Service.insert(dantiInfo);
            iDantiInfoService.insert(info);
            return null;
        }
    }

    public static void setDantiInfoFromV3(DantiInfo dataBean, DantiInfoV3 threeBean) {
        dataBean.setDantiname(threeBean.getDtmc());
        dataBean.setGclb(threeBean.getGcyt());
        dataBean.setJiegoutx(StringUtil.isNotBlank(threeBean.getJgtx()) ? Integer.valueOf(threeBean.getJgtx()) : null);// 结构体系
        dataBean.setFirelevel(StringUtil.isNotBlank(threeBean.getNhdj()) ? Integer.valueOf(threeBean.getNhdj()) : null);// 耐火等级
        dataBean.set("jzfs", StringUtil.isNotBlank(threeBean.getJzfs()) ? Integer.valueOf(threeBean.getJzfs()) : null);// 建造方式
        dataBean.setPrice(
                StringUtil.isNotBlank(threeBean.getDtgczzj()) ? Double.valueOf(threeBean.getDtgczzj()) : null);// 单体工程总造价
        dataBean.setZjzmj(StringUtil.isNotBlank(threeBean.getJzmj()) ? Double.valueOf(threeBean.getJzmj()) : null);// 建筑面积
        dataBean.set("zdmj", StringUtil.isNotBlank(threeBean.getZdmj()) ? Double.valueOf(threeBean.getZdmj()) : null);// 占地面积
        dataBean.setDishangmianji(
                StringUtil.isNotBlank(threeBean.getDsjzmj()) ? String.valueOf(threeBean.getDsjzmj()) : null);// 地上建筑面积
        dataBean.setDixiamianji(
                StringUtil.isNotBlank(threeBean.getDxjzmj()) ? String.valueOf(threeBean.getDxjzmj()) : null);// 地下建筑面积
        dataBean.setDscs(threeBean.getDscs());// 地上层数
        dataBean.setDxcs(threeBean.getDxcs());// 地下层数
        dataBean.setJzgd(StringUtil.isNotBlank(threeBean.getJzgcgd()) ? Double.valueOf(threeBean.getJzgcgd()) : null);// 建筑工程高度
        dataBean.set("cd", StringUtil.isNotBlank(threeBean.getCd()) ? Double.valueOf(threeBean.getCd()) : null);// 长度
        dataBean.setSpan(StringUtil.isNotBlank(threeBean.getKd()) ? String.valueOf(threeBean.getKd()) : null);// 跨度
        dataBean.set("dtjwdzb", threeBean.getDtjwdzb());// 单体经纬度坐标
        dataBean.set("gmzb", threeBean.getGmzb());// 规模指标
        dataBean.set("xkbabh", threeBean.getXkbabh());
        // dataBean.set("isfm", "0");// 待赋码
    }
}
