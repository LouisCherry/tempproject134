package com.epoint.auditsp.sqnbz.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditsp.sqnbz.api.ITaSqnProject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxb;
import com.epoint.basic.spgl.domain.SpglXmspsxblxxxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcjdsxxxb;
import com.epoint.basic.spgl.inter.ISpglDfxmsplcxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxblxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxblxxxxb;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.*;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;
import com.epoint.zwdt.auditsp.handleproject.api.ITAHandleProject;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmspsxblxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.SpglXmspsxblxxxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.domain.Spglsplcjblsxxxb;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglXmspsxblxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglXmspsxblxxxxbV3;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglsplcjdsxxxb;
import com.epoint.zwdt.xmgxh.basic.spglv3.inter.ISpglsplcxxb;
import com.epoint.zwdt.xmgxh.basic.spglv3.service.SpglsplcjdsxxxbService;
import com.epoint.zwdt.xmgxh.basic.zwfwbase.api.IZwfwBasoDao;

@RestController
@RequestMapping("/jnsqnprojectcontroller")
public class JnSqnProjectController
{
    /**
     * 日志
     */ 
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ITaSqnProject service;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private ITAHandleProject iTaHandleProject;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private ISendMaterials sendMaterials;

    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;

    @Autowired
    private ISpglXmspsxblxxb spglXmspsxblxxbservice;

    @Autowired
    private IOuService iouservice;

    @Autowired
    private IAuditSpBusiness iauditspbusiness;

    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private ISpglDfxmsplcxxb ispgldfxmsplcxxb;

    @Autowired
    private ISpglDfxmsplcjdsxxxb ispgldfxmsplcjdsxxxb;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private ISpglXmspsxblxxxxb ispglXmspsxblxxxb;

    @Autowired
    private IZwfwBasoDao baseDao;
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    @Autowired
    private ICodeItemsService codeItemService;
    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private ISpglsplcxxb ispglsplcxxb;

    @Autowired
    private ISpglXmspsxblxxbV3 ispglxmspsxblxxbv3;

    @Autowired
    private ISpglXmspsxblxxxxbV3 ispglxmspsxblxxxxbv3;
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    @Autowired
    private ISpglsplcjdsxxxb iSpglsplcjdsxxxb;

    @Autowired
    private IAuditSpITask iAuditSpITask;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    /**
     * @Description: 初始化事项并推送数据到省前置库
     * @author male
     * @date 2020年8月4日 下午2:28:31
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/initproject", method = RequestMethod.POST)
    public String initProject(@RequestBody String params) {
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、工程代码
                String itemcode = obj.getString("itemcode");
                // 1.2、审批事项编码
                String spsxbm = obj.getString("spsxbm");
                // 1.3、审批部门名称
                String ouname = obj.getString("ouname");
                // 1.4、行政区划代码
                // 图审对接市级辖区编码（泰安示例 370900000000）tsdj_sqn_sjareacode
                String areacode = iConfigService.getFrameConfigValue("tsdj_sqn_sjareacode");
                // 1.5、证照类型 16:社会信用代码 14:组织机构代码
                String certtype = obj.getString("certtype");
                // 1.6、申请人证照编号
                String certnum = obj.getString("certnum");
                // 1.7、企业名称
                String companyname = obj.getString("companyname");
                // 1.8、企业地址 N
                String companyaddress = obj.getString("companyaddress");
                // 1.9、法人代表
                String legal = obj.getString("legal");
                // 1.10、法人代表身份证 N
                String legalid = obj.getString("legalid");
                // 1.11、联系人
                String contactperson = obj.getString("contactperson");
                // 1.12、联系人手机号
                String contactphone = obj.getString("contactphone");

                String fromTs = obj.getString("fromts");
                // 法人和法人身份证为空处理
                if ("1".equals(fromTs)) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("itemcode", itemcode);
                    AuditCommonResult<List<AuditRsItemBaseinfo>> result = iAuditRsItemBaseinfo
                            .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap());
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (result != null && result.getResult() != null && !result.getResult().isEmpty()) {
                        auditRsItemBaseinfo = result.getResult().get(0);
                    }
                    if (auditRsItemBaseinfo != null) {
                        // 获取建设单位
                        ParticipantsInfo participantsInfo = service
                                .getlegalByCreditcode(StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());
                        if (StringUtil.isBlank(legal) && participantsInfo != null) {

                            legal = participantsInfo.getLegal();
                        }
                        if (StringUtil.isBlank(legalid) && participantsInfo != null) {
                            legalid = participantsInfo.getLegalpersonicardnum();
                        }
                        if (StringUtil.isBlank(contactperson)) {
                            contactperson = auditRsItemBaseinfo.getContractperson();
                        }
                        if (StringUtil.isBlank(contactphone)) {
                            contactphone = auditRsItemBaseinfo.getContractphone();
                        }
                    }
                    // 如果还是空的 那么就默认一个值吧
                    if (StringUtil.isBlank(legal)) {
                        legal = "图审推送";
                    }
                    if (StringUtil.isBlank(legalid)) {
                        legalid = "999";
                    }
                }

                // 验证必填参数是否为空
                if (StringUtil.isBlank(itemcode) || StringUtil.isBlank(spsxbm) || StringUtil.isBlank(ouname)
                        || StringUtil.isBlank(areacode) || StringUtil.isBlank(certtype) || StringUtil.isBlank(certnum)
                        || StringUtil.isBlank(companyname) || StringUtil.isBlank(legal)
                        || StringUtil.isBlank(contactperson) || StringUtil.isBlank(contactphone)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数为空！", "");
                }
                // 验证是否有此项工程代码
                if (service.isExsitXmdm(itemcode) == 0) {
                    return JsonUtils.zwdtRestReturn("0", "工程代码在省前置库不存在！", "");
                }

                // 初始化一个事项
                // 根据事项编码获取事项
                // 图审对接初始化办件中心标识（泰安示例 8adfc4f0-ce41-453e-94a7-14356a44db79）tsdj_sqn_centerGuid
                String centerGuid = iConfigService.getFrameConfigValue("tsdj_sqn_centerGuid");
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("ITEM_ID", spsxbm);
                sql.eq("IS_HISTORY", "0");
                sql.eq("IS_ENABLE", "1");

                List<AuditTask> tasks = iAuditTask.getAllTask(sql.getMap()).getResult();

                if (CollectionUtils.isEmpty(tasks)) {
                    return JsonUtils.zwdtRestReturn("0", "事项不存在！", "");
                }

                AuditTask task = tasks.get(0);

                String rowguid = UUID.randomUUID().toString();
                // 初始化
                iTaHandleProject.InitOnlineProjectReturnMaterials(task.getRowguid(), centerGuid, areacode, "",
                        companyname, certnum, rowguid, "", "20").getResult();

                JSONObject result = new JSONObject();

                AuditProject project = sendMaterials.getAuditProjectByRowguid(rowguid);
                if (project != null) {

                    // 办件编码生成-济宁
                    // 获取事项ID
                    String unid = task.getStr("unid");
                    String resultflowsn = FlowsnUtil.createReceiveNum(unid, task.getRowguid());

                    project.setFlowsn(resultflowsn);

                    project.setOperatedate(new Date());
                    project.setOperateusername("水气暖对接");
                    if ("1".equals(fromTs)) {
                        project.setOperateusername("图审对接");
                    }

                    project.setOuname(ouname);
                    project.setApplyername(companyname);
                    project.setAddress(companyaddress);
                    project.setLegal(legal);
                    project.setLegalid(legalid);

                    // 证照类型和证照号码
                    project.setCerttype(certtype);
                    project.setCertnum(certnum);

                    project.setContactperson(contactperson);
                    project.setContactphone(contactphone);
                    project.setWindowname("水气暖报装对接");
                    if ("1".equals(fromTs)) {
                        project.setWindowname("图审对接");
                    }

                    // 关联工程项目
                    Record rec = service.getContectInfo(itemcode);

                    String childItemcode;

                    if (rec != null) {
                        project.setBiguid(rec.getStr("insguid"));
                        project.setSubappguid(rec.getStr("subguid"));
                        project.setXiangmubh(rec.getStr("baseguid"));
                        project.setBusinessguid(rec.getStr("businessguid"));

                        childItemcode = rec.getStr("itemcode");

                        project.setProjectname(project.getProjectname() + "(" + rec.getStr("itemname") + ")");
                    }
                    else {
                        childItemcode = itemcode;
                    }

                    // 状态设置为待接件
                    project.setStatus(26);

                    iAuditProject.updateProject(project);

                    // 插入audit_sp_i_task表信息
                    AuditSpITask spITask = new AuditSpITask();
                    spITask.setOperatedate(new Date());
                    spITask.setRowguid(UUID.randomUUID().toString());

                    if (rec != null) {
                        spITask.setBusinessguid(rec.getStr("businessguid"));
                        spITask.setBiguid(rec.getStr("insguid"));
                        spITask.setPhaseguid(rec.getStr("phaseguid"));
                        spITask.setSubappguid(rec.getStr("subguid"));
                    }
                    spITask.setProjectguid(rowguid);
                    spITask.setTaskguid(task.getRowguid());
                    spITask.setTaskname(task.getTaskname());
                    spITask.setOrdernumber(0);
                    spITask.setAreacode(task.getAreacode());
                    spITask.setSflcbsx("0");
                    spITask.setStatus("1");

                    baseDao.insert(spITask);

                    // 推送到中间表
                    pushSpglXmspsxblxxb(project.getFlowsn());
                    pushSpglXmspsxblxxbV3(project.getFlowsn());

                    // 返回数据
                    result.put("flowsn", project.getFlowsn());
                    result.put("childitemcode", childItemcode);

                    return JsonUtils.zwdtRestReturn("1", "推送成功！", result);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "推送失败！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            log.info("initProject 接口失败异常信息" + e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "查询项目是否存在信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 根据发改编号查询项目是否存在
     * @author male
     * @date 2020年8月4日 下午2:28:31
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/isexsitfgbh", method = RequestMethod.POST)
    public String isExsitFgbh(@RequestBody String params) {
        try {
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、工程代码
                String itemcode = obj.getString("itemcode");

                if (StringUtil.isBlank(itemcode)) {
                    return JsonUtils.zwdtRestReturn("0", "工程代码不能为空！", "");
                }

                int count = service.isExsitXmdm(itemcode);

                JSONObject result = new JSONObject();

                if (count > 0) {
                    result.put("exsit", 1);
                }
                else {
                    result.put("exsit", 0);
                }
                return JsonUtils.zwdtRestReturn("1", "成功！", result);

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            log.info("isExsitFgbh 接口失败异常信息" + e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "查询项目是否存在信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 加入暂停计时
     *
     * @Description: 流程推送
     * @author male
     * @date 2020年8月4日 下午5:45:38
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/pushprocess", method = RequestMethod.POST)
    public String pushProcess(@RequestBody String params) {
        try {
            log.info(">>>>>>>>>>>>>.开始调用pushprocess接口，入参为：" + params);
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、办件流水号
                String flowsn = obj.getString("flowsn");
                // 1.2、工程代码
                String childitemcode = obj.getString("childitemcode");
                // 1.3、办理处（科）室
                String ouname = obj.getString("ouname");
                // 1.4、办理人
                String operatperson = obj.getString("operatperson");
                // 1.5、办理状态
                String operatstatus = obj.getString("operatstatus");
                // 1.6、办理意见
                String opinion = obj.getString("opinion");
                // 1.7、办理时间
                String blsj = obj.getString("blsj");
                // 1.4、行政区划代码
                // 图审对接市级辖区编码（泰安示例 370900000000）tsdj_sqn_sjareacode
                String areacode = iConfigService.getFrameConfigValue("tsdj_sqn_sjareacode");
                String BYZDC = obj.getString("BYZDC");

                String tsdjSqnXzqhbm = iConfigService.getFrameConfigValue("tsdj_sqn_xzqhbm");

                // 验证必填字段
                if (StringUtil.isBlank(flowsn) || StringUtil.isBlank(childitemcode) || StringUtil.isBlank(operatperson)
                        || StringUtil.isBlank(operatstatus) || StringUtil.isBlank(blsj)) {
                    return JsonUtils.zwdtRestReturn("0", "必填参数为空！", "");
                }
                // 验证是否有此项工程代码
                if (service.isExsitXmdm(childitemcode) == 0) {
                    return JsonUtils.zwdtRestReturn("0", "工程代码在省前置库不存在！", "");
                }
                // 获取办件
                String fields = " * ";
                AuditProject project = iAuditProject.getAuditProjectByFlowsn(fields, flowsn, areacode).getResult();
                String gid = "";
                // 图审对接审批事项编码（泰安示例 3709000480）tsdj_sqn_spsxbm
                String SPSXBM = iConfigService.getFrameConfigValue("tsdj_sqn_spsxbm");
                Double SPSXBBH = 2.0;
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("ITEM_ID", SPSXBM);
                sql.eq("IS_HISTORY", "0");
                sql.eq("IS_ENABLE", "1");
                List<AuditTask> tasks = iAuditTask.getAllTask(sql.getMap()).getResult();
                if (!tasks.isEmpty()) {
                    AuditTask audittask = tasks.get(0);
                    SPSXBBH = StringUtil.isNotBlank(audittask.getVersion()) ? Double.parseDouble(audittask.getVersion())
                            : 2.0;
                }
                AuditCommonResult<AuditRsItemBaseinfo> childbaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(project.getXiangmubh());
                if (childbaseinfo.getResult() != null) {
                    if (StringUtil.isNotBlank(childbaseinfo.getResult().getParentid())) {
                        AuditCommonResult<AuditRsItemBaseinfo> parentbaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(childbaseinfo.getResult().getParentid());
                        if (parentbaseinfo.getResult() != null) {
                            gid = parentbaseinfo.getResult().getItemcode();
                        }
                    }
                    else {
                        // 父项guid未空则自身为主项
                        gid = childbaseinfo.getResult().getItemcode();
                    }

                }
                // 获取图审对应的业务表单数据
                String url = "";
                String license = "";
                String token_route = "";
                String SpglSgtsjwjscxxb_route = "";
                String SpglZrztxxb_route = "";
                String SpglXmdtxxb_route = "";
                String SpglKcsjryxxb_route = "";
                String SpglSgtsjwjscxxxxb_route = "";
                if ("图审对接".equals(project.getWindowname())) {
                    List<CodeItems> codeitemlist = codeItemService.listCodeItemsByCodeName("图审对接参数");
                    if (!codeitemlist.isEmpty()) {
                        for (CodeItems codeitems : codeitemlist) {
                            if ("url".equals(codeitems.getItemText())) {
                                url = codeitems.getItemValue();
                            }
                            if ("license".equals(codeitems.getItemText())) {
                                license = codeitems.getItemValue();
                            }
                            if ("token_route".equals(codeitems.getItemText())) {
                                token_route = codeitems.getItemValue();
                            }
                            if ("SpglSgtsjwjscxxb_route".equals(codeitems.getItemText())) {
                                SpglSgtsjwjscxxb_route = codeitems.getItemValue();
                            }
                            if ("SpglZrztxxb_route".equals(codeitems.getItemText())) {
                                SpglZrztxxb_route = codeitems.getItemValue();
                            }
                            if ("SpglXmdtxxb_route".equals(codeitems.getItemText())) {
                                SpglXmdtxxb_route = codeitems.getItemValue();
                            }
                            if ("SpglKcsjryxxb_route".equals(codeitems.getItemText())) {
                                SpglKcsjryxxb_route = codeitems.getItemValue();
                            }
                            if ("SpglSgtsjwjscxxxxb_route".equals(codeitems.getItemText())) {
                                SpglSgtsjwjscxxxxb_route = codeitems.getItemValue();
                            }
                        }
                    }
                }

                // 更改办件状态
                if ("1".equals(operatstatus)) {
                    project.setStatus(26);
                    project.setReceivedate(obj.getDate("blsj"));
                    project.setReceiveusername(operatperson);
                }
                else if ("3".equals(operatstatus)) {
                    project.setStatus(30);
                    project.setApplydate(obj.getDate("blsj"));
                    project.setIs_pause(0);
                }
                else if ("8".equals(operatstatus)) {
                    project.setStatus(50);
                }
                else if ("11".equals(operatstatus)) {
                    project.setStatus(90);
                    project.setBanjieresult(40);
                    project.setBanjiedate(obj.getDate("blsj"));
                    project.setBanjieusername(operatperson);
                    //查看有无centerguid
                    if(StringUtils.isBlank(project.getCenterguid())){
                        AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(project.getAreacode());
                        if(auditOrgaServiceCenter!=null){
                            log.info("centerguid:"+auditOrgaServiceCenter.getRowguid());
                            if("undefined".equals(auditOrgaServiceCenter.getRowguid())){
                                return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "中心标记有问题", "");
                            }
                            project.setCenterguid(auditOrgaServiceCenter.getRowguid());
                        }
                    }
                    
                    //更新承诺办结时间
                    AuditTask  auditTask = iAuditTask.getAuditTaskByGuid(project.getTaskguid(),true).getResult();
                    if(auditTask!=null) {
                        List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(project.getRowguid());
                        Date acceptdat = project.getAcceptuserdate();
                        Date shouldEndDate;
                        if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                                    .getComponent(IAuditOrgaWorkingDay.class);
                            shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                                    project.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                            log.info("shouldEndDate:"+shouldEndDate);
                        } else {
                            shouldEndDate = null;
                        }
                        if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                            Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
                            LocalDateTime currentTime = null;
                            for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
                                // 将Date转换为Instant
                                Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                                if(10==auditProjectUnusual.getOperatetype()){
                                    // 通过Instant和系统默认时区获取LocalDateTime
                                    currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                }
                                if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
                                    LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                    Duration danci = Duration.between(currentTime, nextTime);
                                    totalDuration = totalDuration.plus(danci);
                                    currentTime = null;
                                }
                            }
                            // 将累加的时间差加到初始的Date类型的shouldEndDate上
                            Instant instant = shouldEndDate.toInstant();
                            Instant newInstant = instant.plus(totalDuration);
                            shouldEndDate = Date.from(newInstant);
                            log.info("shouldEndDate:"+shouldEndDate);
                        }
                        if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
    project.setPromiseenddate(shouldEndDate);
}
                    }
                    
                    // url不为空则为图审对接
                    if (StringUtil.isNotBlank(url)) {
                        // 图审对接行政辖区编号tsdj_sqn_xzqhbm 泰安例 370900
                        String XZQHDM = iConfigService.getFrameConfigValue("tsdj_sqn_xzqhbm");
                        // 办件推送办结时获取对应的业务表数据
                        String SpglSgtsjwjscxxbtoken = getToken(url, license, token_route, SpglSgtsjwjscxxb_route);
                        if (StringUtil.isNotBlank(SpglSgtsjwjscxxbtoken)) {
                            getTSform(SpglSgtsjwjscxxbtoken, url, license, SpglSgtsjwjscxxb_route, XZQHDM, gid,
                                    "SPGL_SGTSJWJSCXXB_V3", SPSXBM, SPSXBBH);
                        }
                        else {
                            log.info("获取SpglSgtsjwjscxxbtoken失败无法继续获取图审业务信息！");
                        }
                        // 办件推送办结时获取对应的业务表数据
                        String SpglZrztxxbtoken = getToken(url, license, token_route, SpglZrztxxb_route);
                        if (StringUtil.isNotBlank(SpglZrztxxbtoken)) {
                            getTSform(SpglZrztxxbtoken, url, license, SpglZrztxxb_route, XZQHDM, gid, "SPGL_ZRZTXXB_V3",
                                    SPSXBM, SPSXBBH);
                        }
                        else {
                            log.info("获取SpglZrztxxbtoken失败无法继续获取图审业务信息！");
                        }
                        // 办件推送办结时获取对应的业务表数据
                        String SpglXmdtxxbtoken = getToken(url, license, token_route, SpglXmdtxxb_route);
                        if (StringUtil.isNotBlank(SpglXmdtxxbtoken)) {
                            getTSform(SpglXmdtxxbtoken, url, license, SpglXmdtxxb_route, XZQHDM, gid, "SPGL_XMDTXXB_V3",
                                    SPSXBM, SPSXBBH);
                        }
                        else {
                            log.info("获取SpglXmdtxxbtoken失败无法继续获取图审业务信息！");
                        }
                        // 办件推送办结时获取对应的业务表数据
                        String SpglKcsjryxxbtoken = getToken(url, license, token_route, SpglKcsjryxxb_route);
                        if (StringUtil.isNotBlank(SpglKcsjryxxbtoken)) {
                            getTSform(SpglKcsjryxxbtoken, url, license, SpglKcsjryxxb_route, XZQHDM, gid,
                                    "SPGL_KCSJRYXXB_V3", SPSXBM, SPSXBBH);
                        }
                        else {
                            log.info("获取SpglKcsjryxxbtoken失败无法继续获取图审业务信息！");
                        }
                        // 办件推送办结时获取对应的业务表数据
                        String SpglSgtsjwjscxxxxbtoken = getToken(url, license, token_route, SpglSgtsjwjscxxxxb_route);
                        if (StringUtil.isNotBlank(SpglSgtsjwjscxxxxbtoken)) {
                            getTSform(SpglSgtsjwjscxxxxbtoken, url, license, SpglSgtsjwjscxxxxb_route, XZQHDM, gid,
                                    "SPGL_SGTSJWJSCXXXXB_V3", SPSXBM, SPSXBBH);
                        }
                        else {
                            log.info("获取SpglSgtsjwjscxxxxbtoken失败无法继续获取图审业务信息！");
                        }
                    }
                }
                else if ("5".equals(operatstatus)) {
                    project.setStatus(97);
                }
                // 暂停计时
                else if ("6".equals(operatstatus)) {
                    log.info(">>>>>>>>>>>>>>暂停计时" + flowsn);
                    // 1、更新办件状态 恢复计时0 暂停计时1
                    project.setIs_pause(ZwfwConstant.CONSTANT_INT_ONE);
                    iAuditProject.updateProject(project);
                    // 更新时间表状态 恢复计时0 暂停计时1
                    AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                            .getSparetimeByProjectGuid(project.getRowguid()).getResult();
                    if (auditProjectSparetime != null) {
                        auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                        iAuditProjectSparetime.updateSpareTime(auditProjectSparetime);
                    }
                }
                // 恢复计时
                else if ("7".equals(operatstatus)) {
                    log.info(">>>>>>>>>>>>>>恢复计时" + flowsn);
                    // 更新办件状态
                    project.setIs_pause(ZwfwConstant.CONSTANT_INT_ZERO);// 0恢复
                    // 1暂停
                    iAuditProject.updateProject(project);
                    // 更新时间表状态 恢复计时0 暂停计时1
                    AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                            .getSparetimeByProjectGuid(project.getRowguid()).getResult();
                    if (auditProjectSparetime != null) {
                        auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
                        iAuditProjectSparetime.updateSpareTime(auditProjectSparetime);
                    }
                }

                // 施工图审优化功能
                // 判断BYZDC是否为空，不为空则保存到办件表BYZDC字段
                if (StringUtil.isNotBlank(BYZDC)) {
                    project.set("BYZDC", BYZDC);
                }

                iAuditProject.updateProject(project);

                // 插入流程信息
                String rowguid = UUID.randomUUID().toString();
                AuditProjectOperation operation = new AuditProjectOperation();
                operation.setOperatedate(obj.getDate("blsj"));
                operation.setRowguid(rowguid);
                // 意见
                operation.setRemarks(opinion);
                operation.setProjectGuid(project.getRowguid());
                operation.setPVIGuid(project.getPviguid());

                operation.setOperateusername(operatperson);
                operation.setAreaCode(project.getAreacode());
                operation.setTaskGuid(project.getTaskguid());

                operation.setApplyerName(project.getApplyername());

                iAuditProjectOperation.addProjectOperation(operation);

                // 插入中间表
                // 验证一下spsxslbm在详细信息表是否存在
                SpglXmspsxblxxxxb sxblxxxxb = new SpglXmspsxblxxxxb();
                sxblxxxxb.setRowguid(UUID.randomUUID().toString());

                sxblxxxxb.setXzqhdm(tsdjSqnXzqhbm);
                sxblxxxxb.setGcdm(childitemcode);
                sxblxxxxb.setDfsjzj(rowguid);
                sxblxxxxb.setSpsxslbm(flowsn);

                sxblxxxxb.setBlcs(ouname);
                sxblxxxxb.setBlr(operatperson);

                sxblxxxxb.setBlzt(obj.getInteger("operatstatus"));
                // 暂停计时
                if ("6".equals(operatstatus)) {
                    sxblxxxxb.setBlzt(9);
                }
                else if ("7".equals(operatstatus)) {
                    sxblxxxxb.setBlzt(10);
                }
                sxblxxxxb.setBlyj(opinion);
                sxblxxxxb.setBlsj(obj.getDate("blsj"));

                sxblxxxxb.set("sync", 0);
                sxblxxxxb.setSjyxbs(1);
                sxblxxxxb.setSjsczt(0);

                ispglXmspsxblxxxb.insert(sxblxxxxb);
                // 插入3.0中间表
                // 验证一下spsxslbm在详细信息表是否存在
                SpglXmspsxblxxxxbV3 sxblxxxxbv3 = new SpglXmspsxblxxxxbV3();
                sxblxxxxbv3.setRowguid(UUID.randomUUID().toString());
                sxblxxxxbv3.setXzqhdm("370900");
                sxblxxxxbv3.setGcdm(childitemcode);
                sxblxxxxbv3.setDfsjzj(rowguid);
                sxblxxxxbv3.setSpsxslbm(flowsn);
                sxblxxxxbv3.setBlcs(ouname);
                sxblxxxxbv3.setDwmc(ouname);
                AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                        .getTaskExtensionByTaskGuid(project.getTaskguid(), true).getResult();
                if (auditTaskExtension != null) {
                    String SYSTEM_NAME = codeItemService.getItemTextByCodeName("办理系统",
                            auditTaskExtension.getStr("handle_system"));
                    if (StringUtil.isNotBlank(SYSTEM_NAME)) {
                        sxblxxxxbv3.setSjly(SYSTEM_NAME);
                    }
                    else {
                        sxblxxxxbv3.set("sjsczt", "-1");
                        sxblxxxxbv3.setSbyy("未配置系统参数SYSTEM_NAME！");
                    }
                }
                sxblxxxxbv3.setBlr(operatperson);
                sxblxxxxbv3.setBlzt(obj.getInteger("operatstatus"));
                // 暂停计时
                if ("6".equals(operatstatus)) {
                    sxblxxxxbv3.setBlzt(9);
                }
                else if ("7".equals(operatstatus)) {
                    sxblxxxxbv3.setBlzt(10);
                }
                sxblxxxxbv3.setBlyj(opinion);
                sxblxxxxbv3.setBlsj(obj.getDate("blsj"));
                sxblxxxxbv3.set("sync", 0);
                sxblxxxxbv3.setSjyxbs(1);
                sxblxxxxbv3.setSjsczt(0);
                ispglxmspsxblxxxxbv3.insert(sxblxxxxbv3);
                JSONObject result = new JSONObject();
                result.put("success", 1);

                return JsonUtils.zwdtRestReturn("1", "成功！", result);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            log.info("pushProcess 接口失败异常信息" + e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "流程推送失败：" + e.getMessage(), "");
        }

    }

    public String getToken(String url, String license, String token_route, String yewu_route) {
        String token = "";
        JSONObject params = new JSONObject();
        params.put("sendu", url);
        JSONObject sendparams = new JSONObject();
        sendparams.put("routeid", yewu_route);
        sendparams.put("license", license);
        params.put("sendparams", sendparams);
        JSONObject sendheader = new JSONObject();
        sendheader.put("route", token_route);
        params.put("sendheader", sendheader);
        // 发送请求
        String result = TARequestUtil.sendPost(
                iConfigService.getFrameConfigValue("jn_zwdt_url") + "/rest/apppletdocking/intoout", params.toString(),
                "", "");
        // String result = TARequestUtil.sendPost(
        // "http://localhost:8070/epoint-web-zwdt/rest/apppletdocking/intoout",
        // params.toString(),
        // "", "");
        JSONObject resultObject = JSONObject.parseObject(result);
        JSONObject object = resultObject.getJSONObject("custom");
        if (object.containsKey("token")) {
            token = object.getString("token");
        }
        else {
            log.info("获取token失败返回：" + resultObject);
        }
        return token;
    }

    // 通用方法
    public void getTSform(String token, String url, String license, String routeid, String XZQHDM, String gid,
            String tablename, String SPSXBM, Double SPSXBBH) {
        try {

            String removefield = "SJYXBS,SJSCZT,LSH,DFSJZJ,SBYY,SJWXYY,SPSXBM,SPSXBBH,SPSXSLBM,data";
            JSONObject params = new JSONObject();
            params.put("sendu", url);
            JSONObject sendparams = new JSONObject();
            sendparams.put("token", token);
            sendparams.put("license", license);
            sendparams.put("XZQHDM", XZQHDM);
            sendparams.put("gid", gid);
            params.put("sendparams", sendparams);
            JSONObject sendheader = new JSONObject();
            sendheader.put("route", routeid);
            params.put("sendheader", sendheader);
            // 发送请求
            String result = TARequestUtil.sendPost(
                    iConfigService.getFrameConfigValue("jn_zwdt_url") + "/rest/apppletdocking/intoout",
                    params.toString(), "", "");
            // String result = TARequestUtil.sendPost(
            // "http://localhost:8070/epoint-web-zwdt/rest/apppletdocking/intoout",
            // params.toString(),
            // "", "");

            log.info(result);

            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject object = resultObject.getJSONObject("custom");
            if ("1".equals(object.getString("status"))) {
                JSONArray projectary = object.getJSONArray("project");
                if (!projectary.isEmpty()) {
                    Date now = new Date();
                    for (int a = 0; a < projectary.size(); a++) {
                        JSONObject obj = projectary.getJSONObject(a);
                        // 如果包含data则为带子表数据
                        if (obj.containsKey("data")) {
                            JSONArray dataary = obj.getJSONArray("data");
                            if (!dataary.isEmpty()) {
                                for (int b = 0; b < dataary.size(); b++) {
                                    JSONObject dataobj = dataary.getJSONObject(b);
                                    Record record = new Record();
                                    record.setPrimaryKeys("Row_ID");
                                    record.setSql_TableName(tablename);
                                    record.set("rowguid", UUID.randomUUID().toString());
                                    record.set("Operateusername", "图审对接:TaSqnProjectController");
                                    record.set("Operatedate", now);
                                    record.set("DFSJZJ", UUID.randomUUID().toString());
                                    record.set("SPSXBM", SPSXBM);
                                    record.set("SPSXBBH", SPSXBBH);
                                    record.set("SPSXSLBM", obj.getString("SPSXSLBM"));
                                    record.set("sync", "0");
                                    // 通用字段
                                    for (Map.Entry<String, Object> entry : obj.entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();
                                        if (removefield.contains(key)) {
                                            continue;
                                        }
                                        if (value == null) {
                                            record.put(StringUtil.toUpperCase(key), null);
                                        }
                                        else {
                                            String classname = obj.get(key).getClass().getName();
                                            if (classname.contains("BigDecimal") || obj.getString(key).contains(".")) {
                                                record.put(StringUtil.toUpperCase(key), obj.getDouble(key));
                                            }
                                            else {
                                                record.put(StringUtil.toUpperCase(key), obj.get(key));
                                            }
                                        }
                                    }
                                    // 字表字段
                                    for (Map.Entry<String, Object> entry : dataobj.entrySet()) {
                                        String key = entry.getKey();
                                        Object value = entry.getValue();
                                        if (removefield.contains(key)) {
                                            continue;
                                        }
                                        if (value == null) {
                                            record.put(StringUtil.toUpperCase(key), null);
                                        }
                                        else {
                                            String classname = dataobj.get(key).getClass().getName();
                                            if (classname.contains("BigDecimal")
                                                    || dataobj.getString(key).contains(".")) {
                                                record.put(StringUtil.toUpperCase(key), dataobj.getDouble(key));
                                            }
                                            else {
                                                record.put(StringUtil.toUpperCase(key), dataobj.get(key));
                                            }
                                        }
                                    }
                                    record.set("SJYXBS", ZwfwConstant.CONSTANT_INT_ONE);
                                    record.set("SJSCZT", ZwfwConstant.CONSTANT_INT_ZERO);
                                    record.set("STYWBH", obj.getString("SPSXSLBM"));
                                    // 人员表的单体存在单体编码多个因此拆分上报
                                    if ("SPGL_KCSJRYXXB_V3".equals(tablename)) {
                                        // 存在空格的
                                        String dtbmstr = dataobj.getString("DTBM");
                                        if (StringUtil.isNotBlank(dtbmstr)) {
                                            dtbmstr = dtbmstr.replaceAll(" ", ",");
                                        }
                                        if (dtbmstr.contains(",")) {
                                            String[] dtbms = dtbmstr.split(",");
                                            for (String dtbm : dtbms) {
                                                record.set("DTBM", dtbm);
                                                service.addHtxx(record);
                                            }
                                        }
                                        else {
                                            service.addHtxx(record);
                                        }
                                    }
                                    else {
                                        service.addHtxx(record);
                                    }
                                    if ("SPGL_SGTSJWJSCXXXXB_V3".equals(tablename)) {
                                        // 补充1、4状态数据,办理时间往前提半小时
                                        Date banjieDate = record.getDate("BLSJ");
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(banjieDate);
                                        calendar.add(Calendar.MINUTE, -30);
                                        Date banliDate = calendar.getTime();
                                        // 插入状态4数据
                                        record.remove("rowguid");
                                        record.remove("DFSJZJ");
                                        record.remove("BLZT");
                                        record.remove("BLSJ");
                                        record.set("rowguid", UUID.randomUUID().toString());
                                        record.set("DFSJZJ", UUID.randomUUID().toString());
                                        record.set("BLZT", "4");
                                        record.set("BLSJ", banliDate);
                                        service.addHtxx(record);
                                        // 插入状态1数据
                                        record.remove("rowguid");
                                        record.remove("DFSJZJ");
                                        record.remove("BLZT");
                                        record.set("rowguid", UUID.randomUUID().toString());
                                        record.set("DFSJZJ", UUID.randomUUID().toString());
                                        record.set("BLZT", "1");
                                        service.addHtxx(record);
                                    }
                                }
                            }
                            else {
                                // data为空默认插入一条数据
                                Record record = new Record();
                                record.setPrimaryKeys("Row_ID");
                                record.setSql_TableName(tablename);
                                record.set("rowguid", UUID.randomUUID().toString());
                                record.set("Operateusername", "图审对接:TaSqnProjectController");
                                record.set("Operatedate", now);
                                record.set("DFSJZJ", UUID.randomUUID().toString());
                                record.set("SPSXBM", SPSXBM);
                                record.set("SPSXBBH", SPSXBBH);
                                record.set("SPSXSLBM", obj.getString("SPSXSLBM"));
                                record.set("sync", "0");
                                for (Map.Entry<String, Object> entry : obj.entrySet()) {
                                    String key = entry.getKey();
                                    Object value = entry.getValue();
                                    if (removefield.contains(key)) {
                                        continue;
                                    }
                                    if (value == null) {
                                        record.put(StringUtil.toUpperCase(key), null);
                                    }
                                    else {
                                        String classname = obj.get(key).getClass().getName();
                                        if (classname.contains("BigDecimal") || obj.getString(key).contains(".")) {
                                            record.put(StringUtil.toUpperCase(key), obj.getDouble(key));
                                        }
                                        else {
                                            record.put(StringUtil.toUpperCase(key), obj.get(key));
                                        }
                                    }
                                }
                                record.set("SJYXBS", ZwfwConstant.CONSTANT_INT_ONE);
                                record.set("SJSCZT", ZwfwConstant.CONSTANT_INT_ZERO);
                                record.set("STYWBH", obj.getString("SPSXSLBM"));
                                // 人员表的单体存在单体编码多个因此拆分上报
                                if ("SPGL_KCSJRYXXB_V3".equals(tablename)) {
                                    // 存在空格的
                                    String dtbmstr = obj.getString("DTBM").replaceAll(" ", ",");
                                    if (dtbmstr.contains(",")) {
                                        String[] dtbms = dtbmstr.split(",");
                                        for (String dtbm : dtbms) {
                                            record.set("DTBM", dtbm);
                                            service.addHtxx(record);
                                        }
                                    }
                                    else {
                                        service.addHtxx(record);
                                    }
                                }
                                else {
                                    service.addHtxx(record);
                                }
                                if ("SPGL_SGTSJWJSCXXXXB_V3".equals(tablename)) {
                                    // 补充1、4状态数据,办理时间往前提半小时
                                    Date banjieDate = record.getDate("BLSJ");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(banjieDate);
                                    calendar.add(Calendar.MINUTE, -30);
                                    Date banliDate = calendar.getTime();
                                    // 插入状态4数据
                                    record.remove("rowguid");
                                    record.remove("DFSJZJ");
                                    record.remove("BLZT");
                                    record.remove("BLSJ");
                                    record.set("rowguid", UUID.randomUUID().toString());
                                    record.set("DFSJZJ", UUID.randomUUID().toString());
                                    record.set("BLZT", "4");
                                    record.set("BLSJ", banliDate);
                                    service.addHtxx(record);
                                    // 插入状态1数据
                                    record.remove("rowguid");
                                    record.remove("DFSJZJ");
                                    record.remove("BLZT");
                                    record.set("rowguid", UUID.randomUUID().toString());
                                    record.set("DFSJZJ", UUID.randomUUID().toString());
                                    record.set("BLZT", "1");
                                    service.addHtxx(record);
                                }
                            }
                        }
                        else {
                            Record record = new Record();
                            record.setPrimaryKeys("Row_ID");
                            record.setSql_TableName(tablename);
                            record.set("rowguid", UUID.randomUUID().toString());
                            record.set("Operateusername", "图审对接:TaSqnProjectController");
                            record.set("Operatedate", now);
                            record.set("DFSJZJ", UUID.randomUUID().toString());
                            record.set("SPSXBM", SPSXBM);
                            record.set("SPSXBBH", SPSXBBH);
                            record.set("SPSXSLBM", obj.getString("SPSXSLBM"));
                            record.set("sync", "0");
                            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                                String key = entry.getKey();
                                if (removefield.contains(key)) {
                                    continue;
                                }
                                if (obj.get(key) == null) {
                                    record.put(StringUtil.toUpperCase(key), null);
                                }
                                else {
                                    String classname = obj.get(key).getClass().getName();
                                    if (classname.contains("BigDecimal") || obj.getString(key).contains(".")) {
                                        record.put(StringUtil.toUpperCase(key), obj.getDouble(key));
                                    }
                                    else {
                                        record.put(StringUtil.toUpperCase(key), obj.get(key));
                                    }
                                }
                            }
                            record.set("SJYXBS", ZwfwConstant.CONSTANT_INT_ONE);
                            record.set("SJSCZT", ZwfwConstant.CONSTANT_INT_ZERO);
                            record.set("STYWBH", obj.getString("SPSXSLBM"));
                            // 人员表的单体存在单体编码多个因此拆分上报
                            if ("SPGL_KCSJRYXXB_V3".equals(tablename)) {
                                // 存在空格的
                                String dtbmstr = obj.getString("DTBM").replaceAll(" ", ",");
                                if (dtbmstr.contains(",")) {
                                    String[] dtbms = dtbmstr.split(",");
                                    for (String dtbm : dtbms) {
                                        record.set("DTBM", dtbm);
                                        service.addHtxx(record);
                                    }
                                }
                                else {
                                    service.addHtxx(record);
                                }
                            }
                            else {
                                service.addHtxx(record);
                            }
                            if ("SPGL_SGTSJWJSCXXXXB_V3".equals(tablename)) {
                                // 补充1、4状态数据,办理时间往前提半小时
                                Date banjieDate = record.getDate("BLSJ");
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(banjieDate);
                                calendar.add(Calendar.MINUTE, -30);
                                Date banliDate = calendar.getTime();
                                // 插入状态4数据
                                record.remove("rowguid");
                                record.remove("DFSJZJ");
                                record.remove("BLZT");
                                record.remove("BLSJ");
                                record.set("rowguid", UUID.randomUUID().toString());
                                record.set("DFSJZJ", UUID.randomUUID().toString());
                                record.set("BLZT", "4");
                                record.set("BLSJ", banliDate);
                                service.addHtxx(record);
                                // 插入状态1数据
                                record.remove("rowguid");
                                record.remove("DFSJZJ");
                                record.remove("BLZT");
                                record.set("rowguid", UUID.randomUUID().toString());
                                record.set("DFSJZJ", UUID.randomUUID().toString());
                                record.set("BLZT", "1");
                                service.addHtxx(record);
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            log.info("getTSform 方法失败异常信息" + e.getMessage(), e);
        }
    }

    /**
     * @Description: 根据发改编号查询项目是否存在
     * @author male
     * @date 2020年8月4日 下午2:28:31
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "/receiveResultFiles", method = RequestMethod.POST)
    public String receiveResultFiles(@RequestBody String params) {
        try {
            // 1、接口的入参转化为JSON对象
            log.info(">>>>>>>>>>>>>>>>>>>开始调用receiveResultFiles接口：" + params);
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String flowsn = obj.getString("flowsn");
                if (StringUtil.isBlank(flowsn)) {
                    return JsonUtils.zwdtRestReturn("0", "办件编号不能为空！", "");
                }
                // 查找办件
                AuditProject project = iAuditProject.getAuditProjectByFlowsn("rowguid,status", flowsn, "").getResult();
                if (project == null) {
                    return JsonUtils.zwdtRestReturn("0", "未找到相关办件！", "");
                }
                // 解析结果
                String files = obj.getString("files");
                List<Record> records = JsonUtil.jsonToList(files, Record.class);
                Date now = new Date();
                for (Record record : records) {
                    String filename = record.getStr("filename");
                    String attachguid = UUID.randomUUID().toString();
                    String cliengguid = project.getRowguid();
                    String fileurl = record.getStr("fileurl");
                    log.info(">>>>>>>>>>>.开始下载并保存附件" + fileurl);

                    JSONObject fileparmaobj = new JSONObject();
                    fileparmaobj.put("fileurl", fileurl);
                    String returnobj = TARequestUtil.sendPost(
                            iConfigService.getFrameConfigValue("jn_zwdt_url")
                                    + "/rest/attachloadservicecontroller/downloadfile",
                            fileparmaobj.toJSONString(), "", "");
                    JSONObject fileobj = JSONObject.parseObject(returnobj);
                    // 转换成文件流
                    InputStream is = new ByteArrayInputStream(Base64Util.decodeBuffer(fileobj.getString("content")));
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setAttachGuid(attachguid);
                    frameAttachInfo.setCliengGuid(cliengguid);
                    frameAttachInfo.setAttachFileName(filename);
                    frameAttachInfo.setUploadDateTime(now);
                    frameAttachInfo.setContentType("application/octet-stream");
                    iAttachService.addAttach(frameAttachInfo, is);
                }
                return JsonUtils.zwdtRestReturn("0", "结果附件接受成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            log.info("receiveResultFiles 接口失败异常信息" + e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "结果附件失败：" + e.getMessage(), "");
        }
    }

    /**
     * @Description: 上报项目审批事项办理信息表信息
     * @author male
     * @date 2020年8月4日 下午7:07:53
     * @return void 返回类型
     * @throws
     */
    public void pushSpglXmspsxblxxb(String flowSn) {

        if (StringUtil.isBlank(flowSn)) {
            return;
        }
        // 查一下是否已经有这个办件
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("spsxslbm", flowSn);
        PageData<SpglXmspsxblxxb> pageData = spglXmspsxblxxbservice.getAllByPage(sqlc.getMap(), 0, 10, null, null)
                .getResult();

        if (!CollectionUtils.isEmpty(pageData.getList())) {
            return;
        }

        sqlc = new SqlConditionUtil();
        sqlc.eq("flowsn", flowSn);
        List<AuditProject> listproject = iAuditProject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
        if (CollectionUtils.isEmpty(listproject)) {
            return;
        }

        AuditProject project = listproject.get(0);

        // 主题阶段唯一标识
        String subappguid = project.getSubappguid();
        // 辖区code
        String areacode;

        AuditTask audittask;
        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (sub == null) {
            return;
        }
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                .getResult();
        if (auditspbusiness == null) {
            return;
        }

        // 事务控制
        try {
            EpointFrameDsManager.begin(null);

            String businessareacode = auditspbusiness.getAreacode();
            // 图审对接主题示例区县辖区编码转换为市级辖区编码（示例370990000000,370982000000,370983000000）tsdj_sqn_ztsxxqzh
            String tsdjZtsxxqzh = iConfigService.getFrameConfigValue("tsdj_sqn_ztsxxqzh");
            // 图审对接市级辖区编码
            String sjareacode = iConfigService.getFrameConfigValue("tsdj_sqn_sjareacode");
            // 图审对接行政辖区编码
            String tsdjSqnXzqhbm = iConfigService.getFrameConfigValue("tsdj_sqn_xzqhbm");
            List<String> xqareacode = Arrays.asList(tsdjZtsxxqzh.split(","));
            if (!xqareacode.isEmpty()) {
                if (xqareacode.contains(businessareacode)) {
                    businessareacode = sjareacode;
                    areacode = sjareacode;

                    // 取市AuditSpBusiness
                    SqlConditionUtil sqlutil = new SqlConditionUtil();
                    sqlutil.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                    sqlutil.eq("areacode", areacode);
                    sqlutil.eq("BUSINESSNAME", auditspbusiness.getBusinessname());
                    List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlutil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(listb)) {
                        auditspbusiness = listb.get(0);
                    }
                }
            }

            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
            String sjsczt = "0";
            String sbyy = "";
            if (area != null) {
                // 如果是县级，查找市级主题
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                    sqlc.clear();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlc.eq("XiaQuCode", sjareacode);
                    // 查找市级辖区
                    AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (sjarea == null) {
                        sjsczt = "-1";
                        sbyy = "该主题类型未存在市级主题中！";
                    }
                    else {
                        sqlc.clear();
                        sqlc.eq("splclx", String.valueOf(auditspbusiness.getSplclx()));
                        sqlc.eq("areacode", sjarea.getXiaqucode());
                        sqlc.eq("BUSINESSNAME", auditspbusiness.getBusinessname());
                        List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(listb)) {
                            auditspbusiness = listb.get(0);
                        }
                        else {
                            sjsczt = "-1";
                            sbyy = "该主题类型未存在市级主题中！";
                        }
                    }
                }
            }
            // 重新设置subapp的bussuid
            sub.setBusinessguid(auditspbusiness.getRowguid());

            AuditRsItemBaseinfo rsitem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                    .getResult();
            if (rsitem == null) {
                return;
            }

            Double maxbbh = ispgldfxmsplcxxb.getMaxSplcbbh(sub.getBusinessguid());

            for (AuditProject auditProject : listproject) {
                // 插入批事项办理信息
                audittask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask == null) {
                    continue;
                }
                SpglXmspsxblxxb spgl = new SpglXmspsxblxxb();
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setDfsjzj(auditProject.getRowguid());
                spgl.setXzqhdm(tsdjSqnXzqhbm);
                spgl.setGcdm(rsitem.getItemcode());
                spgl.setSpsxbm(audittask.getItem_id());
                spgl.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                spgl.setSplcbm(sub.getBusinessguid());
                spgl.setSplcbbh(maxbbh);// 设置关联最新的版本号
                spgl.setSpsxslbm(auditProject.getFlowsn());

                spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                spgl.setSpbmmc(audittask.getOuname());

                spgl.setSlfs(1); // 窗口受理

                spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);// 默认1主动公开
                spgl.setBlspslbm(auditProject.getSubappguid());
                spgl.setSxblsx(audittask.getPromise_day());
                spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);

                // 数据验证，查询主题是否推送成功，事项版本号和流程版本号，流程编码是否能获取数据
                if (!ispgldfxmsplcjdsxxxb.isExistSplcSx(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbbh(),
                        spgl.getSpsxbm())) {
                    spgl.set("sjsczt", "-1");
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }
                if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(sjsczt)) {
                    spgl.set("sjsczt", sjsczt);
                    spgl.setSbyy(spgl.getSbyy() + sbyy);
                }
                spglXmspsxblxxbservice.insert(spgl);

            }

            EpointFrameDsManager.commit();

        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public void pushSpglXmspsxblxxbV3(String flowSn) {
        if (StringUtil.isBlank(flowSn)) {
            return;
        }
        // 查一下是否已经有这个办件
        SqlConditionUtil sqlc = new SqlConditionUtil();
        sqlc.eq("spsxslbm", flowSn);
        PageData<SpglXmspsxblxxbV3> pageData = ispglxmspsxblxxbv3.getAllByPage(sqlc.getMap(), 0, 10, null, null)
                .getResult();

        if (!CollectionUtils.isEmpty(pageData.getList())) {
            return;
        }

        sqlc = new SqlConditionUtil();
        sqlc.eq("flowsn", flowSn);
        List<AuditProject> listproject = iAuditProject.getAuditProjectListByCondition(sqlc.getMap()).getResult();
        if (CollectionUtils.isEmpty(listproject)) {
            return;
        }

        AuditProject project = listproject.get(0);

        // 主题阶段唯一标识
        String subappguid = project.getSubappguid();
        // 辖区code
        String areacode;

        AuditTask audittask;
        AuditSpISubapp sub = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
        if (sub == null) {
            return;
        }
        AuditSpBusiness auditspbusiness = iauditspbusiness.getAuditSpBusinessByRowguid(sub.getBusinessguid())
                .getResult();
        if (auditspbusiness == null) {
            return;
        }
        // 事务控制
        try {
            EpointFrameDsManager.begin(null);

            String businessareacode = auditspbusiness.getAreacode();
            // 图审对接主题示例区县辖区编码转换为市级辖区编码（示例370990000000,370982000000,370983000000）tsdj_sqn_ztsxxqzh
            String tsdjZtsxxqzh = iConfigService.getFrameConfigValue("tsdj_sqn_ztsxxqzh");
            // 图审对接市级辖区编码
            String sjareacode = iConfigService.getFrameConfigValue("tsdj_sqn_sjareacode");
            // 图审对接行政辖区编码
            String tsdjSqnXzqhbm = iConfigService.getFrameConfigValue("tsdj_sqn_xzqhbm");
            List<String> xqareacode = Arrays.asList(tsdjZtsxxqzh.split(","));
            if (!xqareacode.isEmpty()) {
                if (xqareacode.contains(businessareacode)) {
                    businessareacode = sjareacode;
                    areacode = sjareacode;

                    // 取市AuditSpBusiness
                    SqlConditionUtil sqlutil = new SqlConditionUtil();
                    sqlutil.eq("splclxv3", String.valueOf(auditspbusiness.getInt("splclxv3")));
                    sqlutil.eq("areacode", areacode);
                    sqlutil.eq("BUSINESSNAME", auditspbusiness.getBusinessname());
                    List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlutil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(listb)) {
                        auditspbusiness = listb.get(0);
                    }
                }
            }
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(businessareacode).getResult();
            String sjsczt = "0";
            String sbyy = "";
            if (area != null) {
                // 如果是县级，查找市级主题
                if (ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())) {
                    sqlc.clear();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlc.eq("XiaQuCode", sjareacode);
                    // 查找市级辖区
                    AuditOrgaArea sjarea = iauditorgaarea.getAuditArea(sqlc.getMap()).getResult();
                    if (sjarea == null) {
                        sjsczt = "-1";
                        sbyy = "该主题类型未存在市级主题中！";
                    }
                    else {
                        sqlc.clear();
                        sqlc.eq("splclxv3", String.valueOf(auditspbusiness.getInt("splclxv3")));
                        sqlc.eq("areacode", sjarea.getXiaqucode());
                        sqlc.eq("BUSINESSNAME", auditspbusiness.getBusinessname());
                        List<AuditSpBusiness> listb = iauditspbusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
                        if (ValidateUtil.isNotBlankCollection(listb)) {
                            auditspbusiness = listb.get(0);
                        }
                        else {
                            sjsczt = "-1";
                            sbyy = "该主题类型未存在市级主题中！";
                        }
                    }
                }
            }
            // 重新设置subapp的bussuid
            sub.setBusinessguid(auditspbusiness.getRowguid());

            AuditRsItemBaseinfo rsitem = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid())
                    .getResult();
            if (rsitem == null) {
                return;
            }

            Double maxbbh = ispglsplcxxb.getMaxSplcbbh(sub.getBusinessguid());

            Date date = new Date();
            for (AuditProject auditProject : listproject) {
                // 插入批事项办理信息
                audittask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                if (audittask == null) {
                    continue;
                }
                AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getIszijianxitong().toString())) {
                    // 如果是自建系统办件、根据系统参数判断是否上报自建系统办件
                    String notreport = iConfigService.getFrameConfigValue("AS_NOTREPORTED_ZJ");
                    if (StringUtil.isNotBlank(notreport) && ZwfwConstant.CONSTANT_STR_ONE.equals(notreport)) {
                        continue;
                    }
                }
                SpglXmspsxblxxbV3 spgl = new SpglXmspsxblxxbV3();
                spgl.setRowguid(UUID.randomUUID().toString());
                spgl.setDfsjzj(auditProject.getRowguid());
                spgl.setXzqhdm(tsdjSqnXzqhbm);
                spgl.setGcdm(rsitem.getItemcode());
                spgl.setSpsxbm(audittask.getItem_id());
                spgl.setSpsxbm(audittask.getItem_id());

                spgl.setSplcbm(sub.getBusinessguid());
                spgl.setSplcbbh(maxbbh);// 设置关联最新的版本号
                spgl.setSpsxslbm(auditProject.getFlowsn());
                spgl.setSpbmbm(iouservice.getOuByOuGuid(audittask.getOuguid()).getOucode());
                spgl.setSpbmmc(Zjconstant.getOunamebyOuguid(audittask.getOuguid()));
                spgl.setSlfs(1); // 窗口受理
                spgl.setGkfs(ZwfwConstant.CONSTANT_INT_ONE);// 默认1主动公开
                // 根据batchguid去填并联审批实例标识
                AuditSpITask auditspitask = iAuditSpITask.getAuditSpITaskByProjectGuid(auditProject.getRowguid())
                        .getResult();
                if (StringUtil.isNotBlank(auditspitask.getBatchguid())) {
                    spgl.setBlspslbm(auditspitask.getBatchguid());
                }
                else {
                    spgl.setBlspslbm(auditProject.getSubappguid());
                }
                spgl.setSxblsx(audittask.getPromise_day());
                spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
                spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
                // 默认查询该事项的上一个已上报最新的事项版本号
                Spglsplcjblsxxxb spglsplcjblsxxxb = new SpglsplcjdsxxxbService().querybbh(spgl.getSplcbbh(),
                        spgl.getSplcbm(), spgl.getSpsxbm());
                if (spglsplcjblsxxxb != null) {
                    spgl.setSpsxbbh(spglsplcjblsxxxb.getSpsxbbh());
                }
                else {
                    spgl.setSpsxbbh(Double.valueOf(audittask.getVersion()));
                }
                // 数据验证，查询主题是否推送成功，事项版本号和流程版本号，流程编码是否能获取数据
                if (!iSpglsplcjdsxxxb.isExistSplcSx(spgl.getSplcbbh(), spgl.getSplcbm(), spgl.getSpsxbbh(),
                        spgl.getSpsxbm())) {
                    spgl.set("sjsczt", "-1");
                    spgl.setSbyy("事项版本在已同步的审批流程阶段事项信息表中无对应，请同步审批流程信息！");
                }
                if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(sjsczt)) {
                    spgl.set("sjsczt", sjsczt);
                    spgl.setSbyy(spgl.getSbyy() + sbyy);
                }
                spgl.setOperatedate(date);
                spgl.setOperateusername("对接处理图审或水热气的办件基本信息（3.0）数据（SpglXmspsxblxxbV3ClientHandle）");
                spgl.setBelongxiaqucode("log");
                // 3.0新增变更字段
                spgl.setLxr(auditProject.getContactperson());
                spgl.setLxrsjh(StringUtil.isNotBlank(auditProject.getContactmobile()) ? auditProject.getContactmobile()
                        : auditProject.getContactphone());
                spgl.setYwqx(audittask.getInt("YWQX"));
                // 3.0新增变更字段结束
                ispglxmspsxblxxbv3.insert(spgl);
            }

            EpointFrameDsManager.commit();

        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

}
