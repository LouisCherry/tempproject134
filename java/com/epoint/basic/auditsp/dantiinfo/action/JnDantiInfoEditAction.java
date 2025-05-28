package com.epoint.basic.auditsp.dantiinfo.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.api.IJnDantiinfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantiinfo.utils.DantiFmUtil;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

/**
 * 项目单体信息表
 */
@RestController("jndantiinfoeditaction")
@Scope("request")
public class JnDantiInfoEditAction extends BaseController
{

    @Autowired
    private IDantiInfoService infoService;

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private IDantiInfoService iDantiInfoService;

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    @Autowired
    private IJnDantiinfoService iJnDantiinfoService;

    @Autowired
    private IDantiInfoV3Service iDantiInfoV3Service;
    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/

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
        }
        else {
            dataBean = new DantiInfo();
        }
    }

    /**
     * 重推单体
     */
    public void repush() {
        if (dataBean != null) {
            String dantiurl = "http://172.20.217.23:8080/dz/system/monomer/v1/add";
            String xmurl = "http://172.20.217.23:8080/dz/system/project/v1/addProjects";
            DantiFmUtil fmUtil = new DantiFmUtil();
            AuditRsItemBaseinfo rsItemBaseinfo = iAuditRsItemBaseinfo
                    .getAuditRsItemBaseinfoByRowguid(dataBean.getProjectguid()).getResult();
            if (rsItemBaseinfo != null) {
                String json = fmUtil.PackagingParametersDanti(dataBean, rsItemBaseinfo);
                log.info("单体赋码入参：" + json);
                String result = HttpUtil.doPostJson(dantiurl, json, null);
                log.info("单体赋码返回值：" + result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                String msg = jsonObject.getString("msg");
                if ("操作成功".equals(msg)) {
                    // 给单体赋码
                    String data = jsonObject.getString("data");
                    JSONArray monos = JSONObject.parseObject(data).getJSONArray("monos");
                    if (!monos.isEmpty()) {
                        for (int i = 0; i < monos.size(); i++) {
                            JSONObject danti = (JSONObject) monos.get(i);
                            String dtid = danti.getString("dtid");
                            if (dataBean.getRowguid().equals(dtid)) {
                                dataBean.set("dtbm", danti.getString("dtdm"));
                                dataBean.set("isfm", "1");
                                iDantiInfoService.update(dataBean);
                                addCallbackParam("msg", "赋码成功！");
                            }
                        }
                    }
                }
                else if (msg.contains("未找到项目")) {
                    // 取流水号 从1开始，成功调用赋码就增长
                    String lsh = ConfigUtil.getFrameConfigValue("fm_lsh");
                    // 推送项目在推送单体
                    String xmjson = fmUtil.PackagingParametersXm(rsItemBaseinfo, Long.parseLong(lsh));
                    log.info("推送项目入参：" + xmjson);
                    String xmresult = HttpUtil.doPostJson(xmurl, xmjson, null);
                    log.info("推送项目返回值：" + xmresult);
                    JSONObject xmjsonObject = JSONObject.parseObject(xmresult);
                    String xmmsg = xmjsonObject.getString("msg");
                    if ("操作成功".equals(xmmsg)) {
                        String fmjson = fmUtil.PackagingParametersDanti(dataBean, rsItemBaseinfo);
                        String fmresult = HttpUtil.doPostJson(dantiurl, fmjson, null);
                        JSONObject fmjsonObject = JSONObject.parseObject(fmresult);
                        String fmmsg = fmjsonObject.getString("msg");
                        if ("操作成功".equals(fmmsg)) {
                            // 给单体赋码
                            String data = jsonObject.getString("data");
                            JSONArray monos = JSONObject.parseObject(data).getJSONArray("monos");
                            if (!monos.isEmpty()) {
                                for (int i = 0; i < monos.size(); i++) {
                                    JSONObject danti = (JSONObject) monos.get(i);
                                    String dtid = danti.getString("dtid");
                                    if (dataBean.getRowguid().equals(dtid)) {
                                        dataBean.set("dtbm", danti.getString("dtdm"));
                                        dataBean.set("isfm", "1");
                                        iDantiInfoService.update(dataBean);
                                        addCallbackParam("msg", "赋码成功！");
                                    }
                                }
                            }
                            lsh = lsh + 1;
                            // 修改系统参数值
                            FrameConfigService9 configService = new FrameConfigService9();
                            configService.update("fm_lsh", lsh);
                        }
                    }
                    else {
                        addCallbackParam("msg", "赋码失败：" + result);
                    }
                }
                else {
                    addCallbackParam("msg", "赋码失败：" + result);
                }
            }
        }
    }

    /***** 工改单体赋码与住建图审系统对接需求登记单开始 *****/
    /**
     * 重推单体到图审系统
     */
    public void rePushToTuShen() {
        if (dataBean != null) {
            DantiInfoV3 dantiInfo = iDantiInfoV3Service.find(dataBean.getRowguid());
            if (dantiInfo != null) {
                // 调用三方接口中的【推送项目信息】获取对应返回值的id（项目id）
                JSONObject insertInfo = iJnDantiinfoService.insertInfo(dantiInfo.getItemguid());
                if (insertInfo == null) {
                    addCallbackParam("error", "推送项目信息失败");
                    return;
                }
                if (StringUtil.isNotBlank(insertInfo.get("msg"))) {
                    addCallbackParam("error", insertInfo.get("msg"));
                    return;
                }
                if (!insertInfo.containsKey("data") || insertInfo.getJSONObject("data") == null
                        || insertInfo.getJSONObject("data").isEmpty()) {
                    addCallbackParam("error", "图审系统未返回项目信息");
                    return;
                }
                String itemId = insertInfo.getJSONObject("data").getString("itemId");
                if (StringUtil.isBlank(itemId)) {
                    addCallbackParam("error", "图审系统未返回项目id");
                    return;
                }

                JSONArray danTiInfos = new JSONArray();
                JSONObject danTiInfo = new JSONObject();
                danTiInfo.put("id", dantiInfo.getZjid());
                danTiInfo.put("Itemid", itemId);
                danTiInfo.put("SubName", dantiInfo.getDtmc());
                danTiInfos.add(danTiInfo);
                // 根据id调用【新增单体】接口
                JSONObject insertSubsAsync = iJnDantiinfoService.insertSubsAsync(danTiInfos);
                if (insertSubsAsync == null) {
                    addCallbackParam("error", "重推失败");
                    return;
                }
                if (StringUtil.isNotBlank(insertSubsAsync.get("msg"))) {
                    addCallbackParam("error", insertSubsAsync.get("msg"));
                    return;
                }
                if (!insertSubsAsync.containsKey("data") || insertSubsAsync.getJSONArray("data") == null
                        || insertSubsAsync.getJSONArray("data").size() == ZwfwConstant.CONSTANT_INT_ZERO) {
                    addCallbackParam("error", "图审系统未返回单体信息");
                    return;
                }
                String subCode = ((JSONObject) (insertSubsAsync.getJSONArray("data")
                        .get(ZwfwConstant.CONSTANT_INT_ZERO))).getString("subCode");
                if (StringUtil.isBlank(subCode)) {
                    addCallbackParam("error", "图审系统未返回单体赋码");
                    return;
                }
                String zjid = ((JSONObject) (insertSubsAsync.getJSONArray("data").get(ZwfwConstant.CONSTANT_INT_ZERO)))
                        .getString("id");
                if (StringUtil.isBlank(zjid)) {
                    addCallbackParam("error", "图审系统未返回单体id");
                    return;
                }
                dantiInfo.setDtbm(subCode);
                dantiInfo.setZjid(zjid);
                dataBean.set("dtbm", subCode);
                dataBean.set("isfm", ZwfwConstant.CONSTANT_STR_ONE);
                iDantiInfoV3Service.update(dantiInfo);
                iDantiInfoService.update(dataBean);
                addCallbackParam("msg", "重推成功！");
            }
        }
    }

    /***** 工改单体赋码与住建图审系统对接需求登记单结束 *****/

    public DantiInfo getDataBean() {
        return dataBean;
    }

    public void setDantiInfo(DantiInfo dantiInfo) {
        this.dataBean = dantiInfo;
    }

}
