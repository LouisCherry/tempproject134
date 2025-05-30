package com.epoint.danti.bizcommon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantisubrelation.api.IDantiSubRelationService;
import com.epoint.basic.auditsp.dantisubrelation.entity.DantiSubRelation;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.dantiinfov3.api.entity.DantiInfoV3;
import com.epoint.zwdt.zwdtrest.project.api.IJnDantiinfoService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.UUID;

public class DantiBizcommon {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 通用保存单体方法
     * @param params ifconfirm 是否关联子申报
     * @param request
     * @return
     */
    public String saveDantiInfo(String params,HttpServletRequest request,boolean ifconfirm) {
        try {
            IDantiInfoV3Service iDantiInfoV3Service = ContainerFactory
                    .getContainInfo().getComponent(IDantiInfoV3Service.class);
            IDantiInfoService iDantiInfoService = ContainerFactory
                    .getContainInfo().getComponent(IDantiInfoService.class);
            IJnDantiinfoService iJnDantiinfoService = ContainerFactory
                    .getContainInfo().getComponent(IJnDantiinfoService.class);
            IDantiSubRelationService iDantiSubRelationService = ContainerFactory
                    .getContainInfo().getComponent(IDantiSubRelationService.class);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String itemguid = obj.getString("itemguid");// 工程标识
                String subAppguid = obj.getString("subappguid");
                if (StringUtil.isBlank(itemguid)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数itemguid不能为空", "");
                }
                String dantiguid = obj.getString("dantiguid");// 单体标识
                String dantiname = obj.getString("dtmc");// 单体名称
                if (StringUtil.isBlank(dantiname)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dtmc不能为空", "");
                }
                String gclb = obj.getString("gcyt");// 工程类别
                if (StringUtil.isBlank(gclb)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数gcyt不能为空", "");
                }
                String dtbm = obj.getString("dtbm");// 单体编码
                String xkbabh = obj.getString("xkbabh");// 许可（备案、技术审查）编号
                String jiegoutx = obj.getString("jgtx");// 结构体系
                String firelevel = obj.getString("nhdj");// 耐火等级
                String jzfs = obj.getString("jzfs");// 建造方式
                String dtgczzj = obj.getString("dtgczzj");// 单体工程总造价
                String jzmj = obj.getString("jzmj");// 建筑面积
                if (StringUtil.isBlank(jzmj)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数jzmj不能为空", "");
                }
                String zdmj = obj.getString("zdmj");// 占地面积
                if (StringUtil.isBlank(zdmj)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数zdmj不能为空", "");
                }
                String dishangmianji = obj.getString("dsjzmj");// 其中，地上建筑面积
                if (StringUtil.isBlank(dishangmianji)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dsjzmj不能为空", "");
                }
                String dixiamianji = obj.getString("dxjzmj");// 其中，地下建筑面积
                if (StringUtil.isBlank(dixiamianji)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dxjzmj不能为空", "");
                }
                String dscs = obj.getString("dscs");// 地上层数
                if (StringUtil.isBlank(dscs)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dscs不能为空", "");
                }
                String dxcs = obj.getString("dxcs");// 地下层数
                if (StringUtil.isBlank(dxcs)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数dxcs不能为空", "");
                }
                String jzgcgd = obj.getString("jzgcgd");// 建筑工程高度
                if (StringUtil.isBlank(jzgcgd)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数jzgcgd不能为空", "");
                }
                String cd = obj.getString("cd");// 长度
                String kd = obj.getString("kd");// 跨度
                String dtjwdzb = obj.getString("dtjwdzb");// 单体经纬度坐标
                String gmzb = obj.getString("gmzb");// 规模指标
                String is_enable = obj.getString("is_enable");// 是否完善

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 为了保证两个表主键一致
                    String dantirowguid = UUID.randomUUID().toString();
                    DantiInfoV3 dantiInfo = new DantiInfoV3();
                    dantiInfo.setItemguid(itemguid);
                    dantiInfo.setRowguid(StringUtil.isNotBlank(dantiguid) ? dantiguid : dantirowguid);
                    dantiInfo.setDtmc(dantiname);// 单体名称
                    dantiInfo.setGcyt(gclb);// 工程类别
                    dantiInfo.setDtbm(dtbm);// 单体编码
                    dantiInfo.setXkbabh(xkbabh);
                    dantiInfo.setJgtx(jiegoutx);// 结构体系
                    if (StringUtil.isNotBlank(firelevel)) {
                        dantiInfo.setNhdj(Integer.valueOf(firelevel));// 耐火等级
                    }
                    if (StringUtil.isNotBlank(jzfs)) {
                        dantiInfo.setJzfs(Integer.valueOf(jzfs));// 建造方式
                    }
                    if (StringUtil.isNotBlank(dtgczzj)) {
                        dantiInfo.setDtgczzj(Double.valueOf(dtgczzj));// 单体工程总造价
                    }
                    if (StringUtil.isNotBlank(jzmj)) {
                        dantiInfo.setJzmj(Double.valueOf(jzmj));// 建筑面积
                    }
                    if (StringUtil.isNotBlank(zdmj)) {
                        dantiInfo.setZdmj(Double.valueOf(zdmj));// 占地面积
                    }
                    if (StringUtil.isNotBlank(dishangmianji)) {
                        dantiInfo.setDsjzmj(Double.valueOf(dishangmianji));// 地上建筑面积
                    }
                    if (StringUtil.isNotBlank(dixiamianji)) {
                        dantiInfo.setDxjzmj(Double.valueOf(dixiamianji));// 地下建筑面积
                    }

                    dantiInfo.setDscs(dscs);// 地上层数
                    dantiInfo.setDxcs(dxcs);// 地下层数

                    if (StringUtil.isNotBlank(jzgcgd)) {
                        dantiInfo.setJzgcgd(Double.valueOf(jzgcgd));// 建筑工程高度
                    }
                    if (StringUtil.isNotBlank(cd)) {
                        dantiInfo.setCd(Double.valueOf(cd));// 长度
                    }
                    if (StringUtil.isNotBlank(kd)) {
                        dantiInfo.setKd(Double.valueOf(kd));// 跨度
                    }
                    dantiInfo.setDtjwdzb(dtjwdzb);// 单体经纬度坐标
                    dantiInfo.setGmzb(gmzb);// 规模指标
                    // ===========================================================================
                    // 保存2.0的单体表
                    DantiInfo info = new DantiInfo();
                    info.setProjectguid(itemguid);
                    info.setRowguid(StringUtil.isNotBlank(dantiguid) ? dantiguid : dantirowguid);
                    info.setDantiname(dantiname);
                    if (StringUtil.isNotBlank(gclb)) {
                        info.setGclb(gclb);
                    }
                    if (StringUtil.isNotBlank(jiegoutx)) {
                        info.setJiegoutx(Integer.parseInt(jiegoutx));// 结构体系
                    }
                    if (StringUtil.isNotBlank(firelevel)) {
                        info.setFirelevel(Integer.valueOf(firelevel));// 耐火等级
                    }
                    if (StringUtil.isNotBlank(jzfs)) {
                        info.set("jzfs", Integer.valueOf(jzfs));// 建造方式
                    }
                    if (StringUtil.isNotBlank(dtgczzj)) {
                        info.setPrice(Double.valueOf(dtgczzj));// 单体工程总造价
                    }
                    if (StringUtil.isNotBlank(jzmj)) {
                        info.setZjzmj(Double.valueOf(jzmj));// 建筑面积
                    }
                    if (StringUtil.isNotBlank(zdmj)) {
                        info.set("zdmj", Double.valueOf(zdmj));// 占地面积
                    }
                    if (StringUtil.isNotBlank(dishangmianji)) {
                        info.setDishangmianji(dishangmianji);// 地上建筑面积
                    }
                    if (StringUtil.isNotBlank(dixiamianji)) {
                        info.setDixiamianji(dixiamianji);// 地下建筑面积
                    }

                    info.setDscs(dscs);// 地上层数
                    info.setDxcs(dxcs);// 地下层数

                    if (StringUtil.isNotBlank(jzgcgd)) {
                        info.setJzgd(Double.valueOf(jzgcgd));// 建筑工程高度
                    }
                    if (StringUtil.isNotBlank(cd)) {
                        info.set("cd", Double.valueOf(cd));// 长度
                    }
                    if (StringUtil.isNotBlank(kd)) {
                        info.setSpan(kd);// 跨度
                    }
                    info.set("dtjwdzb", dtjwdzb);// 单体经纬度坐标
                    info.set("gmzb", gmzb);// 规模指标
                    info.set("xkbabh", xkbabh);
                    info.set("isfm", "0");// 待赋码

                    DantiInfoV3 dantiInfoExit = iDantiInfoV3Service.find(dantiguid);
                    DantiInfo dantiInfoExit2 = iDantiInfoService.find(dantiguid);

                    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
                    // 调用三方接口中的【推送项目信息】获取对应返回值的id（项目id）
                    JSONObject insertInfo = iJnDantiinfoService.insertInfo(itemguid);
                    if (insertInfo == null) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "推送项目信息失败", "");
                    }
                    if (StringUtil.isNotBlank(insertInfo.get("msg"))) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, insertInfo.getString("msg"),
                                "");
                    }
                    if (!insertInfo.containsKey("data") || insertInfo.getJSONObject("data") == null
                            || insertInfo.getJSONObject("data").isEmpty()) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回项目信息", "");
                    }
                    String itemId = insertInfo.getJSONObject("data").getString("itemId");
                    if (StringUtil.isBlank(itemId)) {
                        return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回项目id", "");
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
                        DantiInfoV3 oldDantiInfo = iDantiInfoV3Service.find(dantiInfo.getRowguid());
                        // 修改了单体名称才能更新单体赋码
                        if (oldDantiInfo != null && !dantiname.equals(oldDantiInfo.getDtmc())) {
                            danTiInfo.put("id", oldDantiInfo.getZjid());
                            danTiInfo.put("Itemid", itemId);
                            danTiInfo.put("SubName", dantiname);
                            danTiInfos.add(danTiInfo);
                            // 根据id调用【更新单体】接口
                            JSONObject updateSubsAsync = iJnDantiinfoService.updateSubsAsync(danTiInfos);
                            if (updateSubsAsync == null) {
                                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "保存失败", "");
                            }
                            if (StringUtil.isNotBlank(updateSubsAsync.get("msg"))) {
                                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO,
                                        updateSubsAsync.getString("msg"), "");
                            }
                            if (!updateSubsAsync.containsKey("data") || updateSubsAsync.getJSONArray("data") == null
                                    || updateSubsAsync.getJSONArray("data").size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回单体信息", "");
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
                        return JsonUtils.zwdtRestReturn("1", "修改成功", "");
                    }
                    else {
                        /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
                        JSONArray danTiInfos = new JSONArray();
                        JSONObject danTiInfo = new JSONObject();
                        danTiInfo.put("Itemid", itemId);
                        danTiInfo.put("SubName", dantiname);
                        danTiInfos.add(danTiInfo);
                        // 根据id调用【新增单体】接口
                        JSONObject insertSubsAsync = iJnDantiinfoService.insertSubsAsync(danTiInfos);
                        if (insertSubsAsync == null) {
                            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "保存失败", "");
                        }
                        if (StringUtil.isNotBlank(insertSubsAsync.get("msg"))) {
                            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO,
                                    insertSubsAsync.getString("msg"), "");
                        }
                        if (!insertSubsAsync.containsKey("data") || insertSubsAsync.getJSONArray("data") == null
                                || insertSubsAsync.getJSONArray("data").size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回单体信息", "");
                        }
                        String subCode = ((JSONObject) (insertSubsAsync.getJSONArray("data")
                                .get(ZwfwConstant.CONSTANT_INT_ZERO))).getString("subCode");
                        if (StringUtil.isBlank(subCode)) {
                            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回单体赋码", "");
                        }
                        String zjid = ((JSONObject) (insertSubsAsync.getJSONArray("data")
                                .get(ZwfwConstant.CONSTANT_INT_ZERO))).getString("id");
                        if (StringUtil.isBlank(zjid)) {
                            return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "图审系统未返回单体id", "");
                        }
                        dantiInfo.setDtbm(subCode);
                        dantiInfo.setZjid(zjid);
                        info.set("dtbm", subCode);
                        info.set("isfm", ZwfwConstant.CONSTANT_STR_ONE);
                        /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/
                        dantiInfo.setIs_enable(is_enable);// 是否完善
                        dantiInfo.setCreatedate(new Date());
                        iDantiInfoV3Service.insert(dantiInfo);
                        iDantiInfoService.insert(info);
                        //直接关联
                        if(ifconfirm){
                            if(StringUtils.isNotBlank(subAppguid)){
                                DantiSubRelation dantiSubRelation = new DantiSubRelation();
                                dantiSubRelation.setSubappguid(subAppguid);
                                dantiSubRelation.setDantiguid(dantiInfo.getRowguid());
                                dantiSubRelation.setRowguid(UUID.randomUUID().toString());
                                dantiSubRelation.set("is_v3", ZwfwConstant.CONSTANT_STR_ONE);
                                iDantiSubRelationService.insert(dantiSubRelation);
                            }
                        }
                        return JsonUtils.zwdtRestReturn("1", "保存成功", "");
                    }

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请登录后再试", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "系统异常", "");
        }

    }

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory
                .getContainInfo().getComponent(IAuditOnlineRegister.class);
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }
}
