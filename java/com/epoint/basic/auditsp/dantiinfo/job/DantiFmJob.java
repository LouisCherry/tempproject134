package com.epoint.basic.auditsp.dantiinfo.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.api.IJnDantiinfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.auditsp.dantiinfo.utils.DantiFmUtil;
import com.epoint.basic.bizlogic.sysconf.systemparameters.service.FrameConfigService9;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.IDantiInfoV3Service;
import com.epoint.xmz.thirdreporteddata.dantiinfov3.api.entity.DantiInfoV3;

public class DantiFmJob implements Job
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SyncDantiinfo();
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
    }

    // 推送省里单体信息
    private void SyncDantiinfo() {
        IJnDantiinfoService dantiinfoService = ContainerFactory.getContainInfo()
                .getComponent(IJnDantiinfoService.class);
        IDantiInfoService iDantiInfoService = ContainerFactory.getContainInfo().getComponent(IDantiInfoService.class);
        IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
                .getComponent(IAuditRsItemBaseinfo.class);
        IDantiInfoV3Service iDantiInfoV3Service = ContainerFactory.getContainInfo()
                .getComponent(IDantiInfoV3Service.class);
        List<DantiInfo> list = dantiinfoService.getUnFmDantiInfoList();
        String dantiurl = "http://172.20.217.23:8080/dz/system/monomer/v1/add";
        String xmurl = "http://172.20.217.23:8080/dz/system/project/v1/addProjects";
        if (!list.isEmpty()) {
            for (DantiInfo info : list) {
                DantiFmUtil fmUtil = new DantiFmUtil();
                AuditRsItemBaseinfo rsItemBaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(info.getProjectguid()).getResult();
                // 查询该项目下有无其他未成功的单体，重新赋码
                List<DantiInfo> errrodantis = dantiinfoService
                        .getErrorDantiInfoListByProjectguid(info.getProjectguid());
                if (rsItemBaseinfo != null) {
                    String json = fmUtil.PackagingParametersDanti(info, rsItemBaseinfo);
                    log.info("单体赋码入参：" + json);
                    String result = HttpUtil.doPostJson(dantiurl, json, null);
                    log.info("单体赋码返回值：" + result);
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject != null) {
                        String msg = jsonObject.getString("msg");
                        if ("操作成功".equals(msg)) {
                            // 给单体赋码
                            String data = jsonObject.getString("data");
                            JSONArray monos = JSONObject.parseObject(data).getJSONArray("monos");
                            if (!monos.isEmpty()) {
                                // 项目单体清单
                                for (int i = 0; i < monos.size(); i++) {
                                    JSONObject danti = (JSONObject) monos.get(i);
                                    String dtid = danti.getString("dtid");
                                    if (info.getRowguid().equals(dtid)) {
                                        DantiInfo dantiinfo = iDantiInfoService.find(dtid);
                                        if (dantiinfo != null) {
                                            dantiinfo.set("dtbm", danti.getString("dtdm"));
                                            dantiinfo.set("isfm", "1");
                                            iDantiInfoService.update(dantiinfo);
                                        }
                                        // 同时赋值v3表的数据
                                        DantiInfoV3 infov3 = iDantiInfoV3Service.find(dtid);
                                        if (infov3 != null) {
                                            infov3.set("dtbm", danti.getString("dtdm"));
                                            iDantiInfoV3Service.update(infov3);
                                        }
                                    }
                                    // 如果存在赋值失败的单体
                                    if (EpointCollectionUtils.isNotEmpty(errrodantis)) {
                                        DantiInfo dantiinfo = dantiinfoService.getErrorDantiInfoListByRowguid(dtid);
                                        if (dantiinfo != null) {
                                            dantiinfo.set("dtbm", danti.getString("dtdm"));
                                            dantiinfo.set("isfm", "1");
                                            iDantiInfoService.update(dantiinfo);
                                        }
                                    }
                                }
                            }
                        }
                        else if ("根据名称和编码未找到项目".equals(msg)) {
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
                                String fmjson = fmUtil.PackagingParametersDanti(info, rsItemBaseinfo);
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
                                            if (info.getRowguid().equals(dtid)) {
                                                DantiInfo dantiinfo = iDantiInfoService.find(dtid);
                                                if (dantiinfo != null) {
                                                    dantiinfo.set("dtbm", danti.getString("dtdm"));
                                                    dantiinfo.set("isfm", "1");
                                                    iDantiInfoService.update(dantiinfo);

                                                }
                                                // 同时赋值v3表的数据
                                                DantiInfoV3 infov3 = iDantiInfoV3Service.find(dtid);
                                                if (infov3 != null) {
                                                    infov3.set("dtbm", danti.getString("dtdm"));
                                                    iDantiInfoV3Service.update(infov3);
                                                }
                                            }
                                            // 如果存在赋值失败的单体
                                            if (EpointCollectionUtils.isNotEmpty(errrodantis)) {
                                                DantiInfo dantiinfo = dantiinfoService
                                                        .getErrorDantiInfoListByRowguid(dtid);
                                                if (dantiinfo != null) {
                                                    dantiinfo.set("dtbm", danti.getString("dtdm"));
                                                    dantiinfo.set("isfm", "1");
                                                    iDantiInfoService.update(dantiinfo);
                                                }
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
                                info.set("isfm", "2");
                                iDantiInfoService.update(info);
                            }
                        }
                        else if (msg.contains("单体ID") && msg.contains("已存在")) {
                            info.set("isfm", "2");
                            iDantiInfoService.update(info);
                        }
                        else if ("系统内部错误，请联系管理人员".equals(msg)) {
                            info.set("isfm", "2");
                            iDantiInfoService.update(info);
                        }
                        else {
                            info.set("isfm", "2");
                            iDantiInfoService.update(info);
                        }
                    }
                }

            }
        }
    }

}
