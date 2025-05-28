package com.epoint.auditproject.audithandlecontrol.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.auditfszl.api.IAuditFszlService;
import com.epoint.xmz.auditfszl.api.entity.AuditFszl;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.xmz.auditfszlperson.api.IAuditFszlPersonService;
import com.epoint.xmz.auditfszlperson.api.entity.AuditFszlPerson;

public class JnAuditFszlDataUpdate
{
    private static final Logger log = Logger.getLogger(JnAuditFszlDataUpdate.class);
    
    /**
     * 放射诊疗数据插入
     * 如果对应放射诊疗的事项，需要新增/更新放射诊疗数据
     *  @param project
     */
    public static void updateFszlData(AuditProject project) {
        log.info(">>>>>>>>>>>>>开始更新放射诊疗数据>>>>>>>>>>>>>>" + project.getRowguid());
        if (project != null && StringUtil.isNotBlank(project.getSubappguid()) && StringUtil.isNotBlank(project.getTask_id())
            && StringUtil.isNotBlank(project.getCertnum())) {
            ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo()
                    .getComponent(ICertInfoExternal.class);
            CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(project.get("certrowguid"));
            IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpISubapp.class);
            IAuditFszlService iAuditFszlService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditFszlService.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTask.class);
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(project.getSubappguid()).getResult();
            if (auditSpISubapp == null) {
                return;
            }
            AuditTask auditTask = iAuditTask.selectTaskByRowGuid(project.getTaskguid()).getResult();
            if (StringUtil.isNotBlank(auditSpISubapp.get("optionfromid"))
                && auditTask != null) {
                com.epoint.core.utils.sql.SqlConditionUtil sqlConditionUtil = new com.epoint.core.utils.sql.SqlConditionUtil();
                sqlConditionUtil.eq("certno", project.getCertnum());
                sqlConditionUtil.eq("is_history", "0");
                sqlConditionUtil.setOrderDesc("version");
                List<AuditFszl> auditFszls = iAuditFszlService.findList(sqlConditionUtil.getMap());

                // 设置X射线影像诊断项目许可（新办）
                if (auditTask.getItem_id().equals(ConfigUtil.getFrameConfigValue("fszl_xb_itemid"))) {
                    log.info(">>>>>>>>>>>>>设置X射线影像诊断项目许可（新办）>>>>>>>>>>>>>>");;
                    // 存在则不插入
                    if (EpointCollectionUtils.isEmpty(auditFszls)) {
                        // 调用表单【sform/getPageData】接口获取业务数据
                        JSONObject formInfo = getEformPageData(auditSpISubapp.getRowguid(), auditSpISubapp.get("optionfromid"));
                        // 1、向【audit_fszl】插入数据
                        AuditFszl auditFszl = new AuditFszl();
                        auditFszl.setRowguid(UUID.randomUUID().toString());
                        auditFszl.setIs_history("0");
                        auditFszl.setVersion(1);
                        auditFszl.setVersiondate(new Date());
                        auditFszl.setSendtip("0");
                        auditFszl.setFszlid(UUID.randomUUID().toString());
                        auditFszl.setAreacode(project.getAreacode());
                        auditFszl.setYljgmc(formInfo.getString("yljgmc"));
                        auditFszl.setYljgdjh(formInfo.getString("yljgdjh"));
                        auditFszl.setJydz(formInfo.getString("yjdz"));
                        auditFszl.setSyzxs(formInfo.getString("syzxs"));
                        auditFszl.setFddbr(formInfo.getString("fddbr"));
                        auditFszl.setZyfzr(formInfo.getString("zyfzr"));
                        auditFszl.setLxdh(formInfo.getString("lxdh"));
                        List<String> xkxmStrs = new ArrayList<>();
                        if (StringUtil.isNotBlank(formInfo.getString("fszl"))) {
                            xkxmStrs.add(formInfo.getString("fszl"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("jrfsx"))) {
                            xkxmStrs.add(formInfo.getString("jrfsx"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("Xsxyxzd"))) {
                            xkxmStrs.add(formInfo.getString("Xsxyxzd"));
                        }
                        auditFszl.setXkxm(StringUtil.join(xkxmStrs, ","));
                        auditFszl.setCertificatedate(EpointDateUtil.convertDate2String(project.getBanjiedate(), EpointDateUtil.DATE_TIME_FORMAT));
                        if(certinfo!=null){
                            auditFszl.setCertno(certinfo.getCertno());
                        }
                        auditFszl.setJydate(project.getBanjiedate());
                        String xyyxq = formInfo.getString("xyyxq");
                        auditFszl.setValiditydate(xyyxq);
                        if (auditFszl.getJydate() != null) {
                            // 若有效期15年，则3年校验一次
                            if ("15年".equals(xyyxq)) {
                                auditFszl.setNextjydate(EpointDateUtil.addYear(auditFszl.getJydate(), 3));
                            }
                            // 若有效期5年或长期，则1年校验一次
                            else {
                                auditFszl.setNextjydate(EpointDateUtil.addYear(auditFszl.getJydate(), 1));
                            }
                        }
                        auditFszl.setSyndsbztjcbgcjsj(null);
                        auditFszl.setBaogbzdw(formInfo.getString("baogbzdw"));
                        auditFszl.setCertstatus("1");
                        auditFszl.setUpdatetime(new Date());
                        iAuditFszlService.insert(auditFszl);
                        // 新增/更新人员和装备
                        addFszlPersonAndDevice(formInfo, auditFszl.getRowguid(), "");
                    }
                }
                // 设置X射线影像诊断项目许可（变更）
                else if (auditTask.getItem_id().equals(ConfigUtil.getFrameConfigValue("fszl_bg_itemid"))) {
                    log.info(">>>>>>>>>>>>>设置X射线影像诊断项目许可（变更）>>>>>>>>>>>>>>");;
                    // 不存在历史记录则不插入
                    if (EpointCollectionUtils.isNotEmpty(auditFszls)) {
                        JSONObject formInfo = getEformPageData(auditSpISubapp.getRowguid(), auditSpISubapp.get("optionfromid"));
                        // 1、设置历史版本
                        AuditFszl auditFszl = auditFszls.get(0);
                        auditFszl.setIs_history("1");
                        iAuditFszlService.update(auditFszl);
                        String oldFszlGuid = auditFszl.getRowguid();
                        // 2、新增新版本【audit_fszl】表
                        auditFszl.setRowguid(UUID.randomUUID().toString());
                        auditFszl.setIs_history("0");
                        auditFszl.setVersion(auditFszl.getVersion() + 1);
                        auditFszl.setCertstatus("1");
                        auditFszl.setVersiondate(new Date());
                        auditFszl.setUpdatetime(new Date());
                        auditFszl.setAreacode(project.getAreacode());
                        auditFszl.setYljgmc(formInfo.getString("yljgmc"));
                        if (StringUtil.isNotBlank(formInfo.getString("jydz"))) {
                            auditFszl.setJydz(formInfo.getString("jydz"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("fddbrbgh"))) {
                            auditFszl.setFddbr(formInfo.getString("fddbrbgh"));
                        }
                        // formid=983
                        if (StringUtil.isNotBlank(formInfo.getString("fzrbgq"))) {
                            auditFszl.setZyfzr(formInfo.getString("fzrbgq"));
                        }
                        // formid=986
                        else if (StringUtil.isNotBlank(formInfo.getString("zyfzr"))) {
                            auditFszl.setZyfzr(formInfo.getString("zyfzr"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("lxdh"))) {
                            auditFszl.setLxdh(formInfo.getString("lxdh"));
                        }
                        List<String> xkxmStrs = new ArrayList<>();
                        if (StringUtil.isNotBlank(formInfo.getString("fangszl"))) {
                            xkxmStrs.add(formInfo.getString("fangszl"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("jrfsx"))) {
                            xkxmStrs.add(formInfo.getString("jrfsx"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("Xsxyxzd"))) {
                            xkxmStrs.add(formInfo.getString("Xsxyxzd"));
                        }
                        String xkxm = StringUtil.join(xkxmStrs, ",");
                        if (StringUtil.isNotBlank(StringUtil.isNotBlank(xkxm))) {
                            auditFszl.setXkxm(xkxm);
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("fszlxkzh"))) {
                            auditFszl.setCertno(formInfo.getString("fszlxkzh"));
                        }
                        iAuditFszlService.insert(auditFszl);

                        // 新增/更新人员和装备
                        addFszlPersonAndDevice(formInfo, auditFszl.getRowguid(), oldFszlGuid);
                    }
                }
                // 设置X射线影像诊断项目许可（校验）
                else if (auditTask.getItem_id().equals(ConfigUtil.getFrameConfigValue("fszl_jy_itemid"))) {
                    log.info(">>>>>>>>>>>>>设置X射线影像诊断项目许可（校验）>>>>>>>>>>>>>>");;
                    // 不存在历史记录则不插入
                    if (EpointCollectionUtils.isNotEmpty(auditFszls)) {
                        JSONObject formInfo = getEformPageData(auditSpISubapp.getRowguid(), auditSpISubapp.get("optionfromid"));
                        // 2、旧版本auditfszl
                        AuditFszl auditFszl = auditFszls.get(0);
                        auditFszl.setIs_history("1");
                        iAuditFszlService.update(auditFszl);
                        String oldFszlGuid = auditFszl.getRowguid();
                        // 1、新版本auditfszl
                        auditFszl.setRowguid(UUID.randomUUID().toString());
                        auditFszl.setIs_history("0");
                        auditFszl.setVersion(auditFszl.getVersion() + 1);
                        auditFszl.setCertstatus("1");
                        auditFszl.setVersiondate(new Date());
                        auditFszl.setUpdatetime(new Date());
                        auditFszl.setSendtip("0");
                        auditFszl.setAreacode(project.getAreacode());
                        List<String> xkxmStrs = new ArrayList<>();
                        if (StringUtil.isNotBlank(formInfo.getString("fangszl"))) {
                            xkxmStrs.add(formInfo.getString("fangszl"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("jrfsx"))) {
                            xkxmStrs.add(formInfo.getString("jrfsx"));
                        }
                        if (StringUtil.isNotBlank(formInfo.getString("Xsxyxzd"))) {
                            xkxmStrs.add(formInfo.getString("Xsxyxzd"));
                        }
                        String xkxm = StringUtil.join(xkxmStrs, ",");
                        auditFszl.setXkxm(xkxm);
                        String fszlxkzh = formInfo.getString("fszlxkzh");
                        if (StringUtil.isNotBlank(fszlxkzh)) {
                            auditFszl.setCertno(fszlxkzh);
                        }
                        auditFszl.setJydate(project.getBanjiedate());
                        auditFszl.setSyndsbztjcbgcjsj(EpointDateUtil.convertString2DateAuto(formInfo.getString("syndsbztjcbgcjsj")));
                        iAuditFszlService.insert(auditFszl);
                        // 新增/更新人员和装备
                        addFszlPersonAndDevice(formInfo, auditFszl.getRowguid(), oldFszlGuid);
                    }
                } 
                // 设置X射线影像诊断项目许可（注销）
                else if (auditTask.getItem_id().equals(ConfigUtil.getFrameConfigValue("fszl_zx_itemid"))) {
                    log.info(">>>>>>>>>>>>>设置X射线影像诊断项目许可（注销）>>>>>>>>>>>>>>");;
                    // 不存在历史记录则不插入
                    if (EpointCollectionUtils.isNotEmpty(auditFszls)) {
                        AuditFszl auditFszl = auditFszls.get(0);
                        auditFszl.setIs_history("1");
                        iAuditFszlService.update(auditFszl);

                        auditFszl.setRowguid(UUID.randomUUID().toString());
                        auditFszl.setIs_history("0");
                        auditFszl.setVersion(auditFszl.getVersion() + 1);
                        auditFszl.setCertstatus("2");
                        auditFszl.setVersiondate(new Date());
                        auditFszl.setUpdatetime(new Date());
                        auditFszl.setAreacode(project.getAreacode());
                        iAuditFszlService.insert(auditFszl);
                    }
                }
            }
        }
    }

    private static void addFszlPersonAndDevice(JSONObject formInfo, String newFszlguid, String oldFszlguid) {
        IAuditFszlPersonService iAuditFszlPersonService = ContainerFactory.getContainInfo()
                .getComponent(IAuditFszlPersonService.class);
        IAuditFszlDeviceService iAuditFszlDeviceService = ContainerFactory.getContainInfo()
                .getComponent(IAuditFszlDeviceService.class);
        // 1、插入工作人员信息到【audit_fszl_person】
        JSONArray personArray = formInfo.getJSONArray("personArray");
        if (EpointCollectionUtils.isNotEmpty(personArray)) {
            for (int i = 0; i < personArray.size(); i++) {
                JSONObject personInfo = personArray.getJSONObject(i);
                AuditFszlPerson auditFszlPerson = new AuditFszlPerson();
                auditFszlPerson.setRowguid(UUID.randomUUID().toString());
                auditFszlPerson.setFszlguid(newFszlguid);
                auditFszlPerson.setXm(personInfo.getString("xm")); // 姓名
                auditFszlPerson.setFsgzryzbh(personInfo.getString("fsgzryzbh")); // 放射工作人员证编号
                auditFszlPerson.setZylb(personInfo.getString("zylb")); // 职业类别
                auditFszlPerson.setZyfw(personInfo.getString("zyfw")); // 执业范围
                auditFszlPerson.setZyfl(personInfo.getString("zyfl")); // 执业分类
                auditFszlPerson.setSyndndzyjktjrq(personInfo.getDate("syndndzyjktjrq")); // 上一年度年度职业健康体检日期
                auditFszlPerson.setSyjdgrjljcrq(personInfo.getDate("syjdgrjljcrq")); // 上一季度个人剂量检测日期
                iAuditFszlPersonService.insert(auditFszlPerson);
            }
        }
        else if (StringUtil.isNotBlank(oldFszlguid)) {
            List<AuditFszlPerson> oldPersonList = iAuditFszlPersonService.findListByFszlguid(oldFszlguid);
            if (EpointCollectionUtils.isNotEmpty(oldPersonList)) {
                for (AuditFszlPerson auditFszlPerson : oldPersonList) {
                    auditFszlPerson.setRowguid(UUID.randomUUID().toString());
                    auditFszlPerson.setFszlguid(newFszlguid);
                    iAuditFszlPersonService.insert(auditFszlPerson);
                }
            }
        }
        // 2、插入放射装置信息到【audit_fszl_device】
        JSONArray deviceArray = formInfo.getJSONArray("deviceArray");
        if (EpointCollectionUtils.isNotEmpty(deviceArray)) {
            for (int i = 0; i < deviceArray.size(); i++) {
                JSONObject deviceInfo = deviceArray.getJSONObject(i);
                AuditFszlDevice auditFszlDevice = new AuditFszlDevice();
                auditFszlDevice.setRowguid(UUID.randomUUID().toString());
                auditFszlDevice.setFszlguid(newFszlguid);
                auditFszlDevice.setSzcs(deviceInfo.getString("szcs")); // 所在场所
                auditFszlDevice.setZycs(deviceInfo.getString("zycs")); // 主要参数
                auditFszlDevice.setSccj(deviceInfo.getString("sccj")); // 生产厂家
                auditFszlDevice.setSbbh(deviceInfo.getString("sbbh")); // 设备编号
                auditFszlDevice.setZzmc(deviceInfo.getString("zzmc")); // 装置名称
                auditFszlDevice.setXh(deviceInfo.getString("xh")); // 型号
                iAuditFszlDeviceService.insert(auditFszlDevice);
            }
        }
        else if (StringUtil.isNotBlank(oldFszlguid)) {
            List<AuditFszlDevice> oldDeviceList = iAuditFszlDeviceService.findListByFszlguid(oldFszlguid);
            if (EpointCollectionUtils.isNotEmpty(oldDeviceList)) {
                for (AuditFszlDevice auditFszlDevice : oldDeviceList) {
                    auditFszlDevice.setRowguid(UUID.randomUUID().toString());
                    auditFszlDevice.setFszlguid(newFszlguid);
                    iAuditFszlDeviceService.insert(auditFszlDevice);
                }
            }
        }
    }

    private static JSONObject getEformPageData(String rowGuid, String formid) {
        JSONObject allInfo = new JSONObject();
        String epointsformurlwt = ConfigUtil.getFrameConfigValue("epointsformurl_innernet_ip");
        if (StringUtil.isNotBlank(epointsformurlwt) && !epointsformurlwt.endsWith("/")) {
            epointsformurlwt = epointsformurlwt + "/";
        }
        String getPageDataurl = epointsformurlwt + "rest/sform/getPageData";
        Map<String, Object> param = new HashMap<>();
        JSONObject object = new JSONObject();
        object.put("formId", formid);
        object.put("rowGuid", rowGuid);
        param.put("params", object);
        String result = HttpUtil.doPost(getPageDataurl, param);
        if (StringUtil.isNotBlank(result)) {
            JSONObject obj1 = JSONObject.parseObject(result);
            JSONObject recordData = obj1.getJSONObject("custom").getJSONObject("recordData");
            JSONArray mainList = recordData.getJSONArray("mainList");
            for (int i = 0; i<mainList.size();i++) {
                JSONObject jsonObject = mainList.getJSONObject(i);
                JSONArray rowList = jsonObject.getJSONArray("rowList");
                for (int j = 0; j<rowList.size();j++) {
                    JSONObject rowObject = rowList.getJSONObject(j);
                    allInfo.put(rowObject.getString("FieldName"), rowObject.getString("value"));
                }
            }

            JSONArray personArray = new JSONArray();
            JSONArray deviceArray = new JSONArray();
            JSONArray subList = recordData.getJSONArray("subList");
            if (EpointCollectionUtils.isNotEmpty(subList)) {
                for (int i = 0; i<subList.size();i++) {
                    JSONObject subObject = subList.getJSONObject(i);
                    // 如果有多个子表，此处需要循环遍历
                    JSONArray mainRecordList = subObject.getJSONArray("mainRecordList");
                    for (int j = 0; j<mainRecordList.size(); j++) {
                        // 如果每个子表有多条记录，此处需要循环遍历
                        JSONArray subRecordList = mainRecordList.getJSONObject(j).getJSONArray("subRecordList");
                        if (EpointCollectionUtils.isNotEmpty(subRecordList)) {
                            for (int k = 0; k<subRecordList.size();k++) {
                                JSONObject rowInfo = new JSONObject();
                                JSONObject subRecord = subRecordList.getJSONObject(k);
                                JSONArray rowList = subRecord.getJSONArray("rowList");
                                for (int l = 0; l<rowList.size();l++) {
                                    JSONObject rowObject = rowList.getJSONObject(l);
                                    rowInfo.put(rowObject.getString("FieldName"), rowObject.getString("value"));
                                }
                                if (rowInfo.containsKey("xm")) {
                                    personArray.add(rowInfo);
                                }
                                else if (rowInfo.containsKey("zzmc")) {
                                    deviceArray.add(rowInfo);
                                }
                            }
                        }
                    }
                }
            }
            allInfo.put("personArray", personArray);
            allInfo.put("deviceArray", deviceArray);
        }
        return allInfo;
    }
}
