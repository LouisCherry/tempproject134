package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.phasemonitorinfo.api.IPhaseMonitorInfoService;
import com.epoint.phasemonitorinfo.api.entity.PhaseMonitorInfo;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;

/**
 * 并联审批相关接口
 *
 * @version [F9.3, 2017年10月28日]
 * @作者 xli
 */
@RestController
@RequestMapping("/tazwdtItem")
public class TAAuditOnlineItemController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 企业基本信息API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 项目信息API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;
    @Autowired
    private IOuService ouService;
    /**
     * 并联审批主题实例
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    /**
     * 子申报实例API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IPhaseMonitorInfoService monitorService;
    /**
     * sp材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 共享材料API
     */
    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;
    /**
     * 阶段申报事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
    /**
     * 阶段申报材料实例API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;

    /**
     * 证照API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    /**
     * 证照附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;
    /**
     * /**
     * 共享材料关系API
     */
    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;
    @Autowired
    private IParticipantsInfoService iParticipantsInfoService;
    /********************************************** 项目相关接口开始 **********************************************/
    /**
     * 判断材料是否上传的接口（上传附件页面关闭后调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkSPIMaterialStatus", method = RequestMethod.POST)
    public String checkSPIMaterialStatus(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取并联审批材料附件业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取并联审批材料标识
                String materialGuid = obj.getString("materialguid");
                // 1.3、获取共享材料实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.4、获取材料提交状态
                String status = obj.getString("status");
                // 1.5、获取材料类型
                String type = obj.getString("type");
                // 1.6、获取材料对应附件是否需要上传到证照库（只有C级证照才需要）
                String needLoad = obj.getString("needload");
                // 1.7、获取主题实例标识
                String subAppGuid = obj.getString("subappguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                // 4、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                        .getSpIMaterialBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    if (auditSpIMaterial.getRowguid().equals(materialGuid)) {
                        // 5、获取套餐办件材料对应的附件数量
                        int attachCount = iAttachService.getAttachCountByClientGuid(clientGuid);
                        int intStatus = Integer.parseInt(status);
                        if (attachCount > 0) {
                            // 5.1、若材料有对应的附件，说明电子材料已提交
                            switch (intStatus) {
                                // 5.1.1、材料原先的状态为未提交更新为电子已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                                    break;
                                // 5.1.2、材料原先的状态为纸质已提交更新为电子和纸质都提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            // 5.2、若材料没有对应的附件，说明电子材料没有提交
                            switch (intStatus) {
                                // 5.2.1、材料原先的状态为电子已提交更新为未提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                                    break;
                                // 5.2.2、材料原先的状态为电子和纸质都提交更新为纸质已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                                    break;
                                default:
                                    break;
                            }
                        }
                        auditSpIMaterial.setStatus(String.valueOf(intStatus));
                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                        // 6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                        int showButton = 0;
                        String cliengTag = "";
                        if (StringUtil.isBlank(shareMaterialIGuid)) {
                            // 6.1、如果没有从证照库引用附件，则为普通附件及填表
                            if (String.valueOf(WorkflowKeyNames9.MaterialType_Form).equals(type)) {
                                // 6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = attachCount > 0 ? 4 : 3;
                            } else {
                                // 6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = attachCount > 0 ? 1 : 0;
                            }
                        } else {
                            // 5.2、如果关联了证照库
                            if (ZwdtConstant.NEEDLOAD_Y.equals(needLoad)) {
                                // 5.2.1、如果需要更新证照库，有附件则显示已上传，没有附件则显示未未上传
                                showButton = attachCount > 0 ? 1 : 0;
                                // 5.2.2、更新证照库版本
                                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                List<JSONObject> rsInfos = iCertAttachExternal
                                        .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                int countRs = 0;
                                if (rsInfos != null && rsInfos.size() > 0) {
                                    countRs = rsInfos.size();
                                }
                                // 5.2.2.1、数量不一致
                                if (attachCount > 0 && attachCount != countRs) {
                                    certInfo.setCertcliengguid(clientGuid);
                                    shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                            ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                    //5.2.2.1.1、关联到并联审批材料
                                    auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                } else if (attachCount == countRs) {
                                    // 5.2.2.2、数量一致继续比较                           
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());

                                    frameAttachInfos.sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                    boolean isModify = false;
                                    if (rsInfos != null && rsInfos.size() > 0) {
                                        rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            //如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                            cliengTag = frameAttachInfos.get(i).getCliengTag();
                                            if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                    || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                    && rsInfos.get(i).getLong("size").longValue() != frameAttachInfos
                                                    .get(i).getAttachLength().longValue()) {
                                                isModify = true;
                                                break;
                                            } else {
                                                // 若文件未发生改变，则按钮显示维持原样
                                                CertInfo certInfo2 = iCertInfoExternal
                                                        .getCertInfoByRowguid(shareMaterialIGuid);
                                                if (certInfo2 != null) {
                                                    if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                        String materialType = certInfo2.getMaterialtype();
                                                        if (Integer.parseInt(materialType) == 1) {
                                                            showButton = Integer.parseInt(
                                                                    ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                        } else if (Integer.parseInt(materialType) == 2) {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                        } else {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        isModify = true;
                                    }
                                    if (isModify) {
                                        certInfo.setCertcliengguid(clientGuid);
                                        shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                                ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                        //5.2.2.2.2、关联到并联审批材料
                                        auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                    }
                                }
                            } else {
                                // 5.2.3、如果不需要更新证照库，有附件则则根据共享材料的类别显示已引用共享材料，没有附件则显示未未上传                       
                                if (attachCount > 0) {
                                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                    if (certInfo != null) {
                                        // 5.2.3.1、比较证照实例里的附件和上传的附件
                                        List<JSONObject> rsInfos = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        int countRs = 0;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            countRs = rsInfos.size();
                                        }
                                        if (attachCount != countRs) {
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                        }
                                        // 5.2.3.1.1、数量一致比较详细的附件内容
                                        else if (attachCount == countRs) {
                                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                    .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                            frameAttachInfos
                                                    .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                            if (rsInfos != null && rsInfos.size() > 0) {
                                                rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                                for (int i = 0; i < rsInfos.size(); i++) {
                                                    // 5.2.3.1.2、AttachStorageGuid不同说明附件改动过
                                                    cliengTag = frameAttachInfos.get(i).getCliengTag();
                                                    if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                            || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                            && rsInfos.get(i).getLong("size")
                                                            .longValue() != frameAttachInfos.get(i)
                                                            .getAttachLength().longValue()) {
                                                        showButton = Integer
                                                                .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                        break;
                                                    } else {
                                                        // 5.2.3.1.3、若文件未发生改变，则按钮显示维持原样
                                                        CertInfo certInfo2 = iCertInfoExternal
                                                                .getCertInfoByRowguid(shareMaterialIGuid);
                                                        if (certInfo2 != null) {
                                                            if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                                String materialType = certInfo2.getMaterialtype();
                                                                if (Integer.parseInt(materialType) == 1) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                                } else if (Integer.parseInt(materialType) == 2) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                                } else {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // 5.2.3.2、未上传
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);
                                }
                            }

                        }
                        dataJson.put("showbutton", showButton);
                        dataJson.put("needload", needLoad);
                        dataJson.put("status", intStatus);
                        dataJson.put("sharematerialiguid", shareMaterialIGuid);
                    }
                }
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======checkSPIMaterialStatus接口参数：params【" + params + "】=======");
            log.info("=======checkSPIMaterialStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我所在企业下的项目列表（项目管理页面选择企业后筛选调用）
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMyItemListByCondition", method = RequestMethod.POST)
    public String getMyItemListByCondition(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyItemListByCondition接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取项目名称搜索关键字
                String keyWord = obj.getString("keyword");
                // 1.2、获取企业统一社会信用代码
                String creditCode = obj.getString("creditcode");
                // 1.3、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.4、获取当前页数
                String currentPage = obj.getString("currentpage");
                // 2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);
                if (auditOnlineRegister != null) {
                    // 3、获取用户身份是代办人或者管理者的企业ID
                    SqlConditionUtil grantSqlConditionUtil = new SqlConditionUtil();
                    grantSqlConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantSqlConditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    grantSqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权： 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(grantSqlConditionUtil.getMap()).getResult();
                    String strWhereCompanyId = "";//拼接被授权的所有企业id
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                    }
                    // 4、获取用户身份是代办人或者管理者的企业ID
                    SqlConditionUtil legalSqlConditionUtil = new SqlConditionUtil();
                    legalSqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    legalSqlConditionUtil.eq("is_history", "0");
                    legalSqlConditionUtil.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(legalSqlConditionUtil.getMap()).getResult();
                    String strInCreditCode = "";
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                    }
                    if (StringUtil.isNotBlank(strWhereCompanyId) || StringUtil.isNotBlank(strInCreditCode)) {
                        //把拼接的where条件最后一个“,”去掉
                        if (StringUtil.isNotBlank(strWhereCompanyId)) {
                            strWhereCompanyId = strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1);
                            //根据企业companyid查询出非历史版本被激活的企业的信用代码
                            SqlConditionUtil sqlSelectCompanyUtil = new SqlConditionUtil();
                            sqlSelectCompanyUtil.in("companyid", strWhereCompanyId);
                            sqlSelectCompanyUtil.isBlankOrValue("is_history", "0");
                            sqlSelectCompanyUtil.eq("isactivated", "1");
                            List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                                    .selectAuditRsCompanyBaseinfoByCondition(sqlSelectCompanyUtil.getMap()).getResult();
                            for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                                strInCreditCode += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                            }
                        }
                        if (StringUtil.isNotBlank(strInCreditCode)) {
                            strInCreditCode = strInCreditCode.substring(0, strInCreditCode.length() - 1);
                        }
                    } else {
                        //如果登陆人没有被授权的企业或不是法人，则提示“无权”
                        return JsonUtils.zwdtRestReturn("0", "您无权查询项目", "");
                    }
                    List<JSONObject> itemList = new ArrayList<>();
                    Integer totalCount = 0;
                    if (StringUtil.isNotBlank(strInCreditCode)) {
                        SqlConditionUtil conditionUtil = new SqlConditionUtil();
                        // 2.1 拼接查询条件
                        if (StringUtil.isNotBlank(keyWord)) {
                            conditionUtil.like("itemname", keyWord);
                        }
                        if (StringUtil.isNotBlank(creditCode)) {
                            conditionUtil.eq("itemlegalcreditcode", creditCode);
                        }
                        conditionUtil.in("itemlegalcreditcode", strInCreditCode);
                        // 3、 获取项目信息
                        PageData<AuditRsItemBaseinfo> auditRsItemBaseinfosPageData = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByPage(AuditRsItemBaseinfo.class, conditionUtil.getMap(),
                                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                        Integer.parseInt(pageSize), "", "")
                                .getResult();
                        totalCount = auditRsItemBaseinfosPageData.getRowCount();
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = auditRsItemBaseinfosPageData.getList();
                        if (auditRsItemBaseinfos != null && auditRsItemBaseinfos.size() > 0) {
                            // 3.1、 将项目信息返回
                            for (AuditRsItemBaseinfo auditRsItemBaseinfo : auditRsItemBaseinfos) {
                                JSONObject itemJson = new JSONObject();
                                itemJson.put("itemname", auditRsItemBaseinfo.getItemname());//项目名称
                                itemJson.put("itemcode", auditRsItemBaseinfo.getItemcode());//项目代码
                                itemJson.put("itemguid", auditRsItemBaseinfo.getRowguid());
                                itemJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());//建设单位
                                itemJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                                String biGuid = auditRsItemBaseinfo.getBiguid();
                                List<JSONObject> phaseList = new ArrayList<>();
                                if (StringUtil.isNotBlank(biGuid)) {
                                    //根据申报实例查询套餐阶段
                                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid)
                                            .getResult();
                                    //查询实例所有子申报
                                    List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                            .getResult();
                                    if (auditSpInstance != null) {
                                        String businessguid = auditSpInstance.getBusinessguid();//主题guid
                                        SqlConditionUtil sqlSelectPhase = new SqlConditionUtil();
                                        sqlSelectPhase.eq("businedssguid", businessguid);
                                        sqlSelectPhase.setOrderDesc("ordernumber");
                                        //查询出该主题的所有阶段
                                        List<AuditSpPhase> auditSpPhaseList = iAuditSpPhase
                                                .getAuditSpPhase(sqlSelectPhase.getMap()).getResult();
                                        int key = 0;
                                        for (AuditSpPhase auditSpPhase : auditSpPhaseList) {
                                            int i = 0;
                                            //查询子申报的数量
                                            for (AuditSpISubapp auditSpISubapp : auditSpISubappList) {
                                                if (auditSpISubapp.getPhaseguid().equals(auditSpPhase.getRowguid())) {
                                                    //当前子申报数量+1
                                                    i++;
                                                }
                                            }
                                            JSONObject objPhase = new JSONObject();
                                            objPhase.put("key", key++);//用于控制前台项目列表中的阶段样式
                                            objPhase.put("phasename", i == 0 ? auditSpPhase.getPhasename()
                                                    : auditSpPhase.getPhasename() + "(" + i + ")");
                                            objPhase.put("phasesubappcount", i);
                                            phaseList.add(objPhase);
                                        }
                                        itemJson.put("businessguid", businessguid);
                                    }
                                }
                                itemJson.put("phaselist", phaseList);
                                itemList.add(itemJson);
                            }
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("itemlist", itemList);
                    dataJson.put("totalcount", totalCount);
                    log.info("=======结束调用getMyItemListByCondition接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我所在企业下的项目列表成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用getMyItemListByCondition接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getMyItemListByCondition接口参数：params【" + params + "】=======");
            log.info("=======getMyItemListByCondition异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我所在企业下的项目列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取申报中心列表（申报中心页面加载调用）
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanySubappList", method = RequestMethod.POST)
    public String getCompanySubappList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanySubappList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、项目名称搜索条件
                String keyWord = obj.getString("keyword");
                // 1.2、每页数量
                String pageSize = obj.getString("pagesize");
                // 1.3、当前页
                String currentPage = obj.getString("currentpage");
                // 1.4、排序方式 0：升序1：降序
                String clockWise = obj.getString("clockwise");
                // 1.5、申报状态
                String status = obj.getString("status");
                // 2、获取用户注册信息
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);
                //AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1 查询该用户是否为该企业的代办人或者管理者
                    SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                    grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权： 0-否，1-是
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                    String strWhereCompanyId = "";//拼接被授权的所有企业id
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        strWhereCompanyId += "'" + auditOnlineCompanyGrant.getCompanyid() + "',";
                    }
                    // 2.2、如果当前登陆人是法人，则没有授权信息。需要查法人身份证所属的企业
                    SqlConditionUtil sqlLegal = new SqlConditionUtil();
                    sqlLegal.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlLegal.isBlankOrValue("is_history", "0");
                    sqlLegal.eq("isactivated", "1");
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlLegal.getMap()).getResult();
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
                        strWhereCompanyId += "'" + auditRsCompanyBaseinfo.getCompanyid() + "',";
                    }
                    if (StringUtil.isBlank(strWhereCompanyId)) {
                        // 2.2.1 如果登陆人没有被授权的企业或被解除授权，则提示“无权”
                        return JsonUtils.zwdtRestReturn("0", "您无权查询项目", "");
                    } else {
                        // 2.2.1 去除字段的最后一个逗号
                        strWhereCompanyId = strWhereCompanyId.substring(0, strWhereCompanyId.length() - 1);
                    }
                    SqlConditionUtil sqlSelectAllSubapps = new SqlConditionUtil();
                    //2.3 拼接查询条件
                    sqlSelectAllSubapps.eq("applyerguid", auditOnlineRegister.getAccountguid());
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlSelectAllSubapps.like("subappname", keyWord);
                    }
                    if (StringUtil.isNotBlank(status)) {
                        if (ZwfwConstant.LHSP_Status_DBJ.equals(status)) {
                            status = "'" + ZwfwConstant.LHSP_Status_DBJ + "','" + ZwfwConstant.LHSP_Status_YSDBZ + "'";
                            sqlSelectAllSubapps.in("status", status);
                        } else {
                            sqlSelectAllSubapps.eq("status", status);
                        }
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(clockWise)) {
                        sqlSelectAllSubapps.setOrderDesc("createdate");
                    } else {
                        sqlSelectAllSubapps.setOrderAsc("createdate");
                    }
                    // 3、 分页查询用户申报列表
                    PageData<AuditSpISubapp> auditSpISubappPageData = iAuditSpISubapp.selectSubappsPageDataByCondition(
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            sqlSelectAllSubapps.getMap()).getResult();
                    List<JSONObject> itemList = new ArrayList<>();
                    Integer totalCount = 0;
                    totalCount = auditSpISubappPageData.getRowCount();
                    List<AuditSpISubapp> auditSpISubapps = auditSpISubappPageData.getList();
                    // 3.1 遍历所有子申报
                    for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                        JSONObject objJson = new JSONObject();
                        // 3.1.1 返回参数
                        objJson.put("subappname", auditSpISubapp.getSubappname());
                        objJson.put("subappguid", auditSpISubapp.getRowguid());
                        objJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                        objJson.put("createtime",
                                EpointDateUtil.convertDate2String(auditSpISubapp.getCreatedate(), "yyyy-MM-dd HH:mm"));
                        objJson.put("itemguid", auditSpISubapp.getYewuguid());
                        objJson.put("status", auditSpISubapp.getStatus());
                        // 3.1.2获取阶段名称
                        AuditSpPhase auditSpPhase = iAuditSpPhase
                                .getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid()).getResult();
                        objJson.put("phasename", auditSpPhase.getPhasename());
                        String phaseNum = null;
                        switch (auditSpPhase.getPhaseId()) {
                            case "1":
                                phaseNum = "one";
                                break;
                            case "2":
                                phaseNum = "two";
                                break;
                            case "3":
                                phaseNum = "three";
                                break;
                            case "4":
                                phaseNum = "four";
                                break;
                            default:
                                break;
                        }
                        objJson.put("phasenum", phaseNum);
                        // 3.1.3获取项目名称
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditSpISubapp.getBiguid())
                                .getResult();
                        if (auditSpInstance != null) {
                            objJson.put("biguid", auditSpInstance.getRowguid());
                            objJson.put("businessguid", auditSpInstance.getBusinessguid());
                            objJson.put("itemname", auditSpInstance.getItemname());
                        }
                        itemList.add(objJson);
                    }
                    // 4、返回Json数据
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("itemlist", itemList);
                    dataJson.put("totalcount", totalCount);
                    log.info("=======结束调用getCompanySubappList接口=======");
                    return JsonUtils.zwdtRestReturn("1", " 获取申报列表成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用getCompanySubappList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", " 获取项目列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据项目代码或者项目标识获取项目信息（点击项目查看或者申报按钮时调用）
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getCompanyItemInfo", method = RequestMethod.POST)
    public String getCompanyItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCompanyItemInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 2、获取用户注册信息
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);

                //AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取项目信息
                    AuditRsItemBaseinfo auditRsItemBaseinfo = null;
                    if (StringUtil.isNotBlank(itemGuid)) {
                        auditRsItemBaseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemGuid)
                                .getResult();
                    } else {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("itemcode", itemCode);
                        sqlConditionUtil.isBlankOrValue("is_history", "0");
                        // 3.1、根据项目代码和项目名称查询项目信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                                .selectAuditRsItemBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditRsItemBaseinfos.size() > 0) {
                            auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        }
                    }
                    if (auditRsItemBaseinfo != null) {
                        // 3.1.1 获取该项目的申报单位的统一社会信用代码
                        String itemLegalCreditCode = StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum();
                        // 3.1.2 判断当前用户是否属于这家企业
                        if (StringUtil.isNotBlank(itemLegalCreditCode)) {
                            // 3.1.2.1、 查询出这家企业
                            SqlConditionUtil companySqlConditionUtil = new SqlConditionUtil();
                            companySqlConditionUtil.eq("creditcode", itemLegalCreditCode);
                            companySqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
                            companySqlConditionUtil.eq("isactivated", "1");
                            List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
                                    .selectAuditRsCompanyBaseinfoByCondition(companySqlConditionUtil.getMap())
                                    .getResult();
                            if (auditRsCompanyBaseinfos != null && auditRsCompanyBaseinfos.size() > 0) {
                                AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = auditRsCompanyBaseinfos.get(0);
                                // 3.1.2.2、 获取企业法人证件号
                                String idNum = auditRsCompanyBaseinfo.getOrgalegal_idnumber();
                                // 3.1.2.3、 获取企业id
                                String companyId = auditRsCompanyBaseinfo.getCompanyid();
                                // 3.1.2.4 查询该用户是否为该企业的代办人或者管理者
                                SqlConditionUtil grantConditionUtil = new SqlConditionUtil();
                                grantConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                                grantConditionUtil.eq("companyid", companyId);
                                grantConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：
                                // 0-否，1-是
                                List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                                        .selectCompanyGrantByConditionMap(grantConditionUtil.getMap()).getResult();
                                if (idNum.equals(auditOnlineRegister.getIdnumber())
                                        || auditOnlineCompanyGrants.size() > 0) {
                                    JSONObject dataJson = new JSONObject();
                                    // 如果biguid不为空，则返回主题
                                    if (StringUtil.isNotBlank(auditRsItemBaseinfo.getBiguid())) {
                                        AuditSpInstance auditSpInstance = iAuditSpInstance
                                                .getDetailByBIGuid(auditRsItemBaseinfo.getBiguid()).getResult();
                                        if (auditSpInstance != null) {
                                            AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                                    .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid())
                                                    .getResult();
                                            if (auditSpBusiness != null) {
                                                dataJson.put("businessname", auditSpBusiness.getBusinessname());
                                            }
                                        }
                                    }
                                    dataJson.put("itemname", auditRsItemBaseinfo.getItemname());
                                    dataJson.put("itemcode", auditRsItemBaseinfo.getItemcode());
                                    List<CodeItems> itemtypes = iCodeItemsService.listCodeItemsByCodeName("项目类型");
                                    List<JSONObject> itemTypeList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getItemtype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        itemTypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : itemtypes) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemtype())) {
                                            objJson.put("isselected", 1);
                                            dataJson.put("itemtypetext", codeItems.getItemText());
                                        }
                                        itemTypeList.add(objJson);
                                    }
                                    dataJson.put("itemtype", itemTypeList);
                                    List<CodeItems> constructionpropertys = iCodeItemsService
                                            .listCodeItemsByCodeName("建设性质");
                                    List<JSONObject> constructionpropertyList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getConstructionproperty())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        constructionpropertyList.add(objJson);
                                    }
                                    for (CodeItems codeItems : constructionpropertys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getConstructionproperty())) {
                                            objJson.put("isselected", 1);
                                        }
                                        constructionpropertyList.add(objJson);
                                    }
                                    dataJson.put("constructionproperty", constructionpropertyList);
                                    dataJson.put("itemstartdate", EpointDateUtil.convertDate2String(
                                            auditRsItemBaseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
                                    dataJson.put("itemfinishdate", EpointDateUtil.convertDate2String(
                                            auditRsItemBaseinfo.getItemfinishdate(), EpointDateUtil.DATE_FORMAT));
                                    dataJson.put("totalinvest", auditRsItemBaseinfo.getTotalinvest());
                                    List<CodeItems> belongtindustrys = iCodeItemsService
                                            .listCodeItemsByCodeName("所属行业");
                                    List<JSONObject> belongtindustryList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getBelongtindustry())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        belongtindustryList.add(objJson);
                                    }
                                    for (CodeItems codeItems : belongtindustrys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getBelongtindustry())) {
                                            objJson.put("isselected", 1);
                                        }
                                        belongtindustryList.add(objJson);
                                    }
                                    dataJson.put("belongtindustry", belongtindustryList);
                                    List<CodeItems> isimprovements = iCodeItemsService.listCodeItemsByCodeName("是否");
                                    List<JSONObject> isimprovementList = new ArrayList<>();
                                    for (CodeItems codeItems : isimprovements) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是否技改项目为配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getIsimprovement())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        } else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getIsimprovement())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        isimprovementList.add(objJson);
                                    }
                                    dataJson.put("isimprovement", isimprovementList);
                                    dataJson.put("landarea", auditRsItemBaseinfo.getLandarea());
                                    dataJson.put("jzarea", auditRsItemBaseinfo.getJzmj());
                                    dataJson.put("newlandarea", auditRsItemBaseinfo.getNewlandarea());
                                    dataJson.put("agriculturallandarea", auditRsItemBaseinfo.getAgriculturallandarea());
                                    dataJson.put("itemcapital", auditRsItemBaseinfo.getItemcapital());
                                    List<CodeItems> fundsourcess = iCodeItemsService.listCodeItemsByCodeName("资金来源");
                                    List<JSONObject> fundsourcesList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getFundsources())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        fundsourcesList.add(objJson);
                                    }
                                    for (CodeItems codeItems : fundsourcess) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getFundsources())) {
                                            objJson.put("isselected", 1);
                                        }
                                        fundsourcesList.add(objJson);
                                    }
                                    dataJson.put("fundsources", fundsourcesList);
                                    List<CodeItems> financialresourcess = iCodeItemsService
                                            .listCodeItemsByCodeName("财政资金来源");
                                    List<JSONObject> financialresourcesList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getFinancialresources())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        financialresourcesList.add(objJson);
                                    }
                                    for (CodeItems codeItems : financialresourcess) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getFinancialresources())) {
                                            objJson.put("isselected", 1);
                                        }
                                        financialresourcesList.add(objJson);
                                    }
                                    dataJson.put("financialresources", financialresourcesList);
                                    List<CodeItems> quantifyconstructtypes = iCodeItemsService
                                            .listCodeItemsByCodeName("量化建设规模的类别");
                                    List<JSONObject> quantifyconstructtypeList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getQuantifyconstructtype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        quantifyconstructtypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : quantifyconstructtypes) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getQuantifyconstructtype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        quantifyconstructtypeList.add(objJson);
                                    }
                                    dataJson.put("quantifyconstructtype", quantifyconstructtypeList);
                                    dataJson.put("quantifyconstructcount",
                                            auditRsItemBaseinfo.getQuantifyconstructcount());
                                    dataJson.put("quantifyconstructdept",
                                            auditRsItemBaseinfo.getQuantifyconstructdept());
                                    dataJson.put("constructionsite", auditRsItemBaseinfo.getConstructionsite());
                                    dataJson.put("constructionsitedesc", auditRsItemBaseinfo.getConstructionsitedesc());
                                    dataJson.put("constructionscaleanddesc",
                                            auditRsItemBaseinfo.getConstructionscaleanddesc());
                                    dataJson.put("departname", auditRsItemBaseinfo.getDepartname());
                                    List<CodeItems> legalpropertys = iCodeItemsService.listCodeItemsByCodeName("法人性质");
                                    List<JSONObject> legalpropertyList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getLegalproperty())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        legalpropertyList.add(objJson);
                                    }
                                    for (CodeItems codeItems : legalpropertys) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getLegalproperty())) {
                                            objJson.put("isselected", 1);
                                        }
                                        legalpropertyList.add(objJson);
                                    }
                                    dataJson.put("legalproperty", legalpropertyList);
                                    dataJson.put("constructionaddress", auditRsItemBaseinfo.getConstructionaddress());
                                    dataJson.put("itemlegaldept", auditRsItemBaseinfo.getItemlegaldept());
                                    dataJson.put("itemlegalcreditcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum());
                                    List<CodeItems> itemlegalcerttypes = iCodeItemsService
                                            .listCodeItemsByCodeName("申请人用来唯一标识的证照类型");
                                    List<JSONObject> itemlegalcerttypeList = new ArrayList<>();
                                    // 若证件类型未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        itemlegalcerttypeList.add(objJson);
                                    }
                                    for (CodeItems codeItems : itemlegalcerttypes) {
                                        // 代码项中只保留组织机构代码证和统一社会信用代码。与内网保持一致
                                        if (Integer.parseInt(codeItems.getItemValue()) >= Integer
                                                .parseInt(ZwfwConstant.CERT_TYPE_SFZ)) {
                                            continue;
                                        }
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue()
                                                .equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        itemlegalcerttypeList.add(objJson);
                                    }
                                    dataJson.put("itemlegalcerttype", itemlegalcerttypeList);

                                    /**************** 项目组个性化开始 *****************/
                                    List<CodeItems> projectResources = iCodeItemsService
                                            .listCodeItemsByCodeName("项目投资来源");
                                    List<JSONObject> projectResourcesList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getXmtzly())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        projectResourcesList.add(objJson);
                                    }
                                    for (CodeItems codeItems : projectResources) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getXmtzly()) && codeItems
                                                .getItemValue().equals(auditRsItemBaseinfo.getXmtzly().toString())) {
                                            objJson.put("isselected", 1);
                                        }
                                        projectResourcesList.add(objJson);
                                    }
                                    dataJson.put("projectResources", projectResourcesList);

                                    List<CodeItems> tdhqfsItems = iCodeItemsService.listCodeItemsByCodeName("土地获取方式");
                                    List<JSONObject> tdhqfsItemsList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getTdhqfs())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        tdhqfsItemsList.add(objJson);
                                    }
                                    for (CodeItems codeItems : tdhqfsItems) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (StringUtil.isNotBlank(auditRsItemBaseinfo.getTdhqfs()) && codeItems
                                                .getItemValue().equals(auditRsItemBaseinfo.getTdhqfs().toString())) {
                                            objJson.put("isselected", 1);
                                        }
                                        tdhqfsItemsList.add(objJson);
                                    }
                                    dataJson.put("tdhqfsItems", tdhqfsItemsList);
                                    // 土地是否带设计方案
                                    List<JSONObject> tdsfdsjfaList = new ArrayList<>();
                                    for (CodeItems codeItems : isimprovements) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是否技改项目为配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getTdsfdsjfa())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        } else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getTdsfdsjfa().toString())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        tdsfdsjfaList.add(objJson);
                                    }
                                    dataJson.put("tdsfdsjfaList", tdsfdsjfaList);
                                    // 是否完成区域评估
                                    List<JSONObject> isfinishqypg = new ArrayList<>();
                                    for (CodeItems codeItems : isimprovements) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        // 若是否技改项目为配置，则默认显示否
                                        if (StringUtil.isBlank(auditRsItemBaseinfo.getSfwcqypg())) {
                                            if (ZwfwConstant.CONSTANT_STR_ZERO.equals(codeItems.getItemValue())) {
                                                objJson.put("isselected", 1);
                                            }
                                        } else {
                                            if (codeItems.getItemValue()
                                                    .equals(auditRsItemBaseinfo.getSfwcqypg().toString())) {
                                                objJson.put("isselected", 1);
                                            }
                                        }
                                        isfinishqypg.add(objJson);
                                    }
                                    dataJson.put("isfinishqypg", isfinishqypg);

                                    List<CodeItems> itemTypes = iCodeItemsService.listCodeItemsByCodeName("审批流程类型");
                                    List<JSONObject> splclxList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getItemtype())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        splclxList.add(objJson);
                                    }
                                    for (CodeItems codeItems : itemTypes) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", codeItems.getItemValue());
                                        objJson.put("itemtext", codeItems.getItemText());
                                        if (codeItems.getItemValue().equals(auditRsItemBaseinfo.getItemtype())) {
                                            objJson.put("isselected", 1);
                                        }
                                        splclxList.add(objJson);
                                    }
                                    dataJson.put("splclxList", splclxList);
                                    SqlConditionUtil areaSqlConditionUtil = new SqlConditionUtil();
                                    areaSqlConditionUtil.in("citylevel", "1,2");
                                    List<AuditOrgaArea> areaList = iAuditOrgaArea.selectAuditAreaList(areaSqlConditionUtil.getMap()).getResult();
                                    List<JSONObject> belongXiaQuList = new ArrayList<>();
                                    // 若未配置，下拉框默认显示请选择
                                    if (StringUtil.isBlank(auditRsItemBaseinfo.getBelongxiaqucode())) {
                                        JSONObject objJson = new JSONObject();
                                        objJson.put("itemvalue", "");
                                        objJson.put("itemtext", "请选择");
                                        objJson.put("isselected", 1);
                                        belongXiaQuList.add(objJson);
                                    }
                                    if (StringUtil.isNotBlank(areaList)) {
                                        for (AuditOrgaArea auditOrgaArea : areaList) {
                                            JSONObject objJson = new JSONObject();
                                            objJson.put("itemvalue", auditOrgaArea.getXiaqucode());
                                            objJson.put("itemtext", auditOrgaArea.getXiaquname());
                                            if (auditOrgaArea.getXiaqucode()
                                                    .equals(auditRsItemBaseinfo.getBelongxiaqucode())) {
                                                objJson.put("isselected", 1);
                                            }
                                            belongXiaQuList.add(objJson);
                                        }
                                    }
                                    dataJson.put("belongXiaQuList", belongXiaQuList);
                                    /**************** 项目组个性化结束 *****************/

                                    dataJson.put("itemlegalcertnum", auditRsItemBaseinfo.getItemlegalcertnum());
                                    dataJson.put("legalperson", auditRsCompanyBaseinfo.getOrganlegal());
                                    dataJson.put("legalpersonicardnum", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                                    dataJson.put("frphone", auditRsItemBaseinfo.getFrphone());
                                    dataJson.put("fremail", auditRsItemBaseinfo.getFremail());
                                    dataJson.put("contractperson", auditRsItemBaseinfo.getContractperson());
                                    dataJson.put("contractidcart", auditRsItemBaseinfo.getContractidcart());
                                    dataJson.put("contractphone", auditRsItemBaseinfo.getContractphone());
                                    dataJson.put("contractemail", auditRsItemBaseinfo.getContractemail());
                                    log.info("=======结束调用getCompanyItemInfo接口=======");
                                    return JsonUtils.zwdtRestReturn("1", " 获取项目信息成功！", dataJson.toString());
                                } else {
                                    return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                                }
                            } else {
                                return JsonUtils.zwdtRestReturn("0", "您无权申报此项目", "");
                            }
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "项目社会信用代码为空", "");
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", " 获取项目信息失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用getCompanyItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getCompanyItemInfo接口参数：params【" + params + "】=======");
            log.info("=======getCompanyItemInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取项目信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取简单阶段基本信息接口（阶段申请时页面上方加载时调用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSimplePhaseInfo", method = RequestMethod.POST)
    public String getSimplePhaseInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSimplePhaseInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取阶段Guid
                String phaseGuid = obj.getString("phaseguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取项目信息
                AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();
                if (auditSpPhase != null) {
                    dataJson.put("phasename", auditSpPhase.getPhasename());
                    dataJson.put("phaseformurl", auditSpPhase.getEformurl());
                    String phaseNum = null;
                    switch (auditSpPhase.getPhaseId()) {
                        case "1":
                            phaseNum = "one";
                            break;
                        case "2":
                            phaseNum = "two";
                            break;
                        case "3":
                            phaseNum = "three";
                            break;
                        case "4":
                            phaseNum = "four";
                            break;
                        default:
                            break;
                    }
                    dataJson.put("phasenum", phaseNum);
                }
                log.info("=======结束调用getSimplePhaseInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取简单阶段基本信息成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getSimplePhaseInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取简单阶段基本信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 保存项目申报信息（点击保存项目信息按钮时调用）
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveItemInfo", method = RequestMethod.POST)
    public String saveItemInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveItemInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1 获取项目代码
                String itemCode = obj.getString("itemcode");
                // 1.2 获取项目名称
                String itemName = obj.getString("itemname");
                // 1.3 获取项目类型
                String itemType = obj.getString("itemtype");
                // 1.4 获取建设性质
                String constructionProperty = obj.getString("constructionproperty");
                // 1.5 获取项目（法人）单位
                String itemLegalDept = obj.getString("itemlegaldept");
                // 1.6 获取项目（法人）单位统一社会信用代码
                String itemLegalCreditCode = obj.getString("itemlegalcreditcode");
                // 1.7 获取项目法人证照类型
                String itemLegalCertType = obj.getString("itemlegalcerttype");
                // 1.8 项目法人证照号码
                String itemLegalCertNum = obj.getString("itemlegalcertnum");
                // 1.9 拟开工时间
                Date itemStartDate = obj.getDate("itemstartdate");
                // 1.10 拟建成时间
                Date itemFinishDate = obj.getDate("itemfinishdate");
                // 1.11 获取总投资（万元）
                Double totalInvest = obj.getDouble("totalinvest");
                // 1.12 获取建设地点
                String constructionSite = obj.getString("constructionsite");
                // 1.13 获取建设地点详情
                String constructionSiteDesc = obj.getString("constructionsitedesc");
                // 1.14 获取所属行业
                String belongTindustry = obj.getString("belongtindustry");
                // 1.15 获取建设规模及内容
                String constructionScaleAndDesc = obj.getString("constructionscaleanddesc");
                // 1.16 获取联系人
                String contractPerson = obj.getString("contractperson");
                // 1.17 获取联系电话
                String contractPhone = obj.getString("contractphone");
                // 1.18 获取联系人邮箱
                String contractEmail = obj.getString("contractemail");
                // 1.19 获取法人性质
                String legalProperty = obj.getString("legalproperty");
                // 1.20 获取用地面积(亩)
                Double landArea = obj.getDouble("landarea");
                // 1.21 获新增用地面积
                Double newLandArea = obj.getDouble("newlandarea");
                // 1.22 获取农用地面积
                Double agriculturalLandArea = obj.getDouble("agriculturallandarea");
                // 1.23 获取项目资本金
                Double itemCapital = obj.getDouble("itemcapital");
                // 1.24 获取资金来源
                String fundSources = obj.getString("fundsources");
                // 1.25 获取财政资金来源
                String financialResources = obj.getString("financialresources");
                // 1.26 获取量化建设规模的类别
                String quantifyConstructType = obj.getString("quantifyconstructtype");
                // 1.27 获取量化建设规模的数值
                Double quantifyConstructCount = obj.getDouble("quantifyconstructcount");
                // 1.28 获取量化建设规模的单位
                String quantifyConstructDept = obj.getString("quantifyconstructdept");
                // 1.29 获取是否技改项目
                String isImprovement = obj.getString("isimprovement");
                // 1.30 获取主题唯一标识
                String businessGuid = obj.getString("businessguid");
                // 1.31 获取项目唯一标识
                String itemGuid = obj.getString("itemguid");
                // 1.32 获取建设单位名称
                String departname = obj.getString("departname");
                // 1.33 获取建设单位地址
                String constructionaddress = obj.getString("constructionaddress");
                // 1.34 获取法人
                String legalperson = obj.getString("legalperson");
                // 1.35 获取法人身份证
                String legalpersonicardnum = obj.getString("legalpersonicardnum");
                // 1.36 获取法人电话
                String frphone = obj.getString("frphone");
                // 1.37 获取法人邮箱
                String fremail = obj.getString("fremail");
                // 1.38 获取联系人身份证
                String contractidcart = obj.getString("contractidcart");
                // 1.39 获取法人性质
                String legalproperty = obj.getString("legalproperty");

                // 新增参数
                // 建筑面积
                Double jzarea = obj.getDouble("jzarea");
                // 项目投资来源
                Integer projectResource = obj.getInteger("projectresource");
                // 土地获取方式
                Integer tdhqfs = obj.getInteger("tdhqfs");
                // 土地是否带设计方案
                Integer tdsfdjsfa = obj.getInteger("tdsfdjsfa");
                // 是否完成区域评估
                Integer sfwcqypg = obj.getInteger("sfwcqypg");
                // 是否完成区域评估
                String belongarea = obj.getString("belongarea");

                // 2、获取用户注册信息
                // 1.5、获取用户基本信息 个性化
                String auditOnlineRegisterStr = jsonObject.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);

                //AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、 获取用户对应的法人信息
                    if (StringUtil.isNotBlank(itemGuid)) {
                        AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                .getAuditRsItemBaseinfoByRowguid(itemGuid).getResult();
                        if (auditRsItemBaseinfo != null) {
                            //建设单位
                            ParticipantsInfo participantsInfo = iParticipantsInfoService.findParticipantsInfoByItemGuid(itemGuid);
                            //------新增字段保存-------
                            boolean addFlagpart = false; // 默认为更新
                            if (participantsInfo == null) {
                                participantsInfo = new ParticipantsInfo();
                                participantsInfo.setRowguid(UUID.randomUUID().toString());
                                participantsInfo.setCorptype("31");
                                participantsInfo.setItemguid(itemGuid);
                                participantsInfo.setOperatedate(new Date());
                                addFlagpart = true; // 新增
                            }
                            participantsInfo.setCorpcode(itemLegalCreditCode);// 统一社会信用代码
                            participantsInfo.setCorpname(departname);// 项目单位
                            participantsInfo.setLegalproperty(legalproperty);// 法人类型
                            participantsInfo.setLegal(legalperson);// 法定代表人
                            participantsInfo.setAddress(constructionaddress);// 通讯地址
                            //participantsInfo.set("zipcode", postCode);// 邮政编码
                            //participantsInfo.setXmfzr(projectPerson);// 项目负责人
                            //participantsInfo.setXmfzr_phone(telNumber);// 联系电话
                            participantsInfo.setDanweilxr(contractPerson);// 授权申报人
                            participantsInfo.setDanweilxrlxdh(contractidcart);// 联系电话
                            //participantsInfo.set("certType", certType);// 身份证件类型
                            //participantsInfo.setXmfzr_idcard(certNumber);// 身份证件号码
                            if (addFlagpart) {
                                iParticipantsInfoService.insert(participantsInfo);
                            } else {
                                iParticipantsInfoService.update(participantsInfo);
                            }
                            // 3.1 更新项目信息
                            auditRsItemBaseinfo.setOperatedate(new Date());
                            auditRsItemBaseinfo.setDepartname(departname);
                            auditRsItemBaseinfo.setConstructionaddress(constructionaddress);
                            auditRsItemBaseinfo.setLegalperson(legalperson);
                            auditRsItemBaseinfo.setLegalpersonicardnum(legalpersonicardnum);
                            auditRsItemBaseinfo.setFrphone(frphone);
                            auditRsItemBaseinfo.setFremail(fremail);
                            auditRsItemBaseinfo.setLegalproperty(legalproperty);
                            auditRsItemBaseinfo.setContractidcart(contractidcart);
                            auditRsItemBaseinfo.setItemname(itemName);
                            auditRsItemBaseinfo.setItemcode(itemCode);
                            auditRsItemBaseinfo.setItemtype(itemType);
                            auditRsItemBaseinfo.setConstructionproperty(constructionProperty);
                            auditRsItemBaseinfo.setItemlegaldept(itemLegalDept);
                            auditRsItemBaseinfo.setItemlegalcreditcode(itemLegalCreditCode);
                            auditRsItemBaseinfo.setItemlegalcerttype(itemLegalCertType);
                            auditRsItemBaseinfo.setItemlegalcertnum(itemLegalCertNum);
                            auditRsItemBaseinfo.setItemstartdate(itemStartDate);
                            auditRsItemBaseinfo.setItemfinishdate(itemFinishDate);
                            auditRsItemBaseinfo.setTotalinvest(totalInvest);
                            auditRsItemBaseinfo.setConstructionsite(constructionSite);
                            auditRsItemBaseinfo.setConstructionsitedesc(constructionSiteDesc);
                            auditRsItemBaseinfo.setBelongtindustry(belongTindustry);
                            auditRsItemBaseinfo.setConstructionscaleanddesc(constructionScaleAndDesc);
                            auditRsItemBaseinfo.setContractperson(contractPerson);
                            auditRsItemBaseinfo.setContractphone(contractPhone);
                            auditRsItemBaseinfo.setContractemail(contractEmail);
                            auditRsItemBaseinfo.setLegalproperty(legalProperty);
                            auditRsItemBaseinfo.setLandarea(landArea);
                            auditRsItemBaseinfo.setNewlandarea(newLandArea);
                            auditRsItemBaseinfo.setAgriculturallandarea(agriculturalLandArea);
                            auditRsItemBaseinfo.setItemcapital(itemCapital);
                            auditRsItemBaseinfo.setFundsources(fundSources);
                            auditRsItemBaseinfo.setFinancialresources(financialResources);
                            auditRsItemBaseinfo.setQuantifyconstructtype(quantifyConstructType);
                            auditRsItemBaseinfo.setQuantifyconstructdept(quantifyConstructDept);
                            auditRsItemBaseinfo.setQuantifyconstructcount(quantifyConstructCount);
                            auditRsItemBaseinfo.setIsimprovement(isImprovement);

                            auditRsItemBaseinfo.setJzmj(jzarea);
                            auditRsItemBaseinfo.setXmtzly(projectResource);
                            auditRsItemBaseinfo.setTdhqfs(tdhqfs);
                            auditRsItemBaseinfo.setTdsfdsjfa(tdsfdjsfa);
                            auditRsItemBaseinfo.setSfwcqypg(sfwcqypg);
                            auditRsItemBaseinfo.setBelongxiaqucode(belongarea);

                            if (StringUtil.isBlank(auditRsItemBaseinfo.getBiguid())) {
                                // 3.2、如果项目没有对应的主题实例，则生成对应主题实例
                                if (StringUtil.isNotBlank(businessGuid)) {
                                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                            .getAuditSpBusinessByRowguid(businessGuid).getResult();
                                    if (auditSpBusiness != null) {
                                        String newBiguid = iAuditSpInstance.addBusinessInstance(businessGuid,
                                                auditRsItemBaseinfo.getRowguid(), auditOnlineRegister.getAccountguid(),
                                                auditOnlineRegister.getUsername(), ZwfwConstant.APPLY_WAY_NETSBYS,
                                                itemName, belongarea, auditSpBusiness.getBusinesstype()).getResult();
                                        // 3.2.1、项目管理主题实例
                                        auditRsItemBaseinfo.setBiguid(newBiguid);
                                        iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                        JSONObject dataJson = new JSONObject();
                                        dataJson.put("biguid", newBiguid);
                                        dataJson.put("firstflag", "1");
                                        return JsonUtils.zwdtRestReturn("1", " 保存成功", dataJson);
                                    }
                                }
                                return JsonUtils.zwdtRestReturn("0", " 项目性质选择无效", "");
                            } else {
                                // 3.2、如果项目已有对应的主题实例，则直接更新数据
                                iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(auditRsItemBaseinfo);
                                JSONObject dataJson = new JSONObject();
                                dataJson.put("biguid", auditRsItemBaseinfo.getBiguid());
                                dataJson.put("firstflag", "0");
                                return JsonUtils.zwdtRestReturn("1", " 修改成功", dataJson);
                            }
                        } else {
                            return JsonUtils.zwdtRestReturn("0", " 保存失败！项目不存在", "");
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", " 项目查询失败", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                log.info("=======结束调用saveItemInfo接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======saveItemInfo接口参数：params【" + params + "】=======");
            log.info("=======saveItemInfo接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "保存失败：" + e.getMessage(), "");
        }
    }

    /**
     * 阶段申报提交接口（填写阶段表单后点击提交按钮调用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/startPhaseDeclaration", method = RequestMethod.POST)
    public String startPhaseDeclaration(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取startPhaseDeclaration接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 1.2、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.3、获取阶段实例
                String phaseGuid = obj.getString("phaseguid");
                // 1.4、获取子申报名称
                String subappName = obj.getString("subappname");
                // 1.5、获取项目标识
                String itemguid = obj.getString("itemguid");
                // 2、获取用户注册信息
                // 获取用户基本信息 个性化
                String auditOnlineRegisterStr = json.getString("auditonlineregister");
                AuditOnlineRegister auditOnlineRegister = JSON.parseObject(auditOnlineRegisterStr,
                        AuditOnlineRegister.class);

                //AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    SqlConditionUtil conditionUtil = new SqlConditionUtil();
                    conditionUtil.eq("biguid", biGuid);
                    // 3、根据子申报标识获取子申报
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                    if (auditSpISubapp != null) {
                        // 3.1、更新子申报状态为已提交
                        iAuditSpISubapp.updateSubapp(subAppGuid, "5", new Date());
                    } else {
                        // 3.2、获取主题实例信息
                        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                        // 3.2、获取阶段信息
                        AuditSpPhase auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(phaseGuid).getResult();
                        // 3.3、生成子申报信息
                        List<AuditRsItemBaseinfo> auditRsItemBaseinfos = iAuditRsItemBaseinfo
                                .selectAuditRsItemBaseinfoByCondition(conditionUtil.getMap()).getResult();
                        AuditRsItemBaseinfo auditRsItemBaseinfo = auditRsItemBaseinfos.get(0);
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByOneField("creditcode", StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum())
                                .getResult();
                        AuditSpISubapp newAuditSpISubapp = new AuditSpISubapp();
                        newAuditSpISubapp.setRowguid(subAppGuid);
                        newAuditSpISubapp.setApplyerguid(auditRsCompanyBaseinfo.getCompanyid());
                        newAuditSpISubapp.setApplyername(auditRsCompanyBaseinfo.getOrganname());
                        newAuditSpISubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                        newAuditSpISubapp.setBiguid(biGuid);
                        newAuditSpISubapp.setBusinessguid(auditSpInstance.getBusinessguid());
                        newAuditSpISubapp.setCreatedate(new Date());
                        newAuditSpISubapp.setPhaseguid(phaseGuid);
                        newAuditSpISubapp.setStatus("5");
                        if (StringUtils.isNotBlank(itemguid)) {
                            AuditRsItemBaseinfo auditRsItemBaseinfo1 = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                            if (auditRsItemBaseinfo1 != null) {
                                newAuditSpISubapp.setYewuguid(itemguid);
                            } else {
                                newAuditSpISubapp.setYewuguid(auditRsItemBaseinfo.getRowguid());
                            }
                        }
                        if (StringUtil.isBlank(subappName)) {
                            newAuditSpISubapp.setSubappname(auditSpPhase.getPhasename()); // 子申报名称
                        } else {
                            newAuditSpISubapp.setSubappname(subappName);
                        }
                        iAuditSpISubapp.addSubapp(newAuditSpISubapp);
                        String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
                        List<FrameUser> listUser = userService.listUserByOuGuid("", roleGuid, "", "", false, true, true,
                                3);
                        StringBuffer sb = new StringBuffer();
                        if (listUser != null && listUser.size() > 0) {
                            listUser.removeIf(u -> {
                                boolean del = false;
                                del = StringUtil.isBlank(u.getMobile()) ? true : false;
                                if (!del) {
                                    FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(u.getOuGuid());
                                    if (ouExtendInfo == null || StringUtil.isBlank(ouExtendInfo.get("areacode"))
                                            || !auditSpInstance.getAreacode().equals(ouExtendInfo.get("areacode"))) {
                                        del = true;
                                    }
                                }
                                return del;
                            });
                            if (listUser != null && listUser.size() > 0) {
                                String content = "您有一条" + auditSpInstance.getItemname() + "申报待预审，请及时进行预审。";
                                for (FrameUser user : listUser) {
                                    // 发送成功保留记录
                                    messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), content,
                                            new Date(), 0, new Date(), user.getMobile(), user.getUserGuid(),
                                            user.getDisplayName(), "", "", "", "", "", false, "短信");
                                    sb.append(user.getUserGuid()).append(";");
                                }
                            }
                        }

                        // 记录监控
                        PhaseMonitorInfo info = monitorService.findBySubappguid(subAppGuid, "1");
                        if (info != null) {
                            info.setOperatedate(new Date());
                            // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                            info.setStatus("1");
                            // 设置开始时间
                            info.setStarttime(new Date());
                            info.set("ouguid", sb.toString());
                            monitorService.update(info);
                        } else {
                            info = new PhaseMonitorInfo();
                            info.setRowguid(UUID.randomUUID().toString());
                            info.set("subAppGuid", subAppGuid);
                            info.setOperatedate(new Date());
                            info.setItemname(auditSpInstance.getItemname());
                            if (auditSpPhase != null) {
                                info.setPhasename(auditSpPhase.getPhasename());
                            }
                            // 1、申报预审 2、材料审核 3、事项征求
                            info.setMonitortype("1");
                            info.setStarttime(new Date());
                            info.set("ouguid", sb.toString());
                            // 设置完成时间
                            // info.setProcesstime(new Date());
                            // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                            info.setStatus("1");
                            monitorService.insert(info);
                        }

                    }
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("subappguid", subAppGuid);
                    log.info("=======结束调用startPhaseDeclaration接口=======");
                    return JsonUtils.zwdtRestReturn("1", "阶段申报提交成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getPhaseAndSPSubappList接口参数：params【" + params + "】=======");
            log.info("=======startPhaseDeclaration接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "阶段申报提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交套餐办件接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String subAppGuid = obj.getString("subappguid");
                // 4、更新子申报状态为待预审
                iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                // 3.2、获取主题实例信息
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditSpISubapp.getBiguid())
                        .getResult();
                if (auditSpInstance != null) {
                    // 发送短信，通过审批系统服务扫描messages_center表
                    String roleGuid = roleService.listRole("统一收件人员", "").get(0).getRoleGuid();
                    List<FrameUser> listUser = userService.listUserByOuGuid("", roleGuid, "", "", false, true, true, 3);
                    StringBuffer sb = new StringBuffer();
                    if (listUser != null && listUser.size() > 0) {
                        String fromUserGuid = ZwdtUserSession.getInstance("").getAccountGuid(); // 发送待办人guid
                        String fromUserName = ZwdtUserSession.getInstance("").getClientName(); // 发送待办人名称
                        String url = "epointzwfw/auditsp/auditsphandle/handlebicheckmaterialdetail?biGuid="
                                + auditSpISubapp.getBiguid() + "&subappGuid=" + subAppGuid;
                        listUser.removeIf(u -> {
                            boolean del = StringUtil.isBlank(u.getMobile()) ? true : false;
                            if (!del) {
                                FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(u.getOuGuid());
                                if (ouExtendInfo == null || StringUtil.isBlank(ouExtendInfo.get("areacode"))
                                        || !auditSpInstance.getAreacode().equals(ouExtendInfo.get("areacode"))) {
                                    del = true;
                                }
                            }
                            return del;
                        });
                        if (listUser != null && listUser.size() > 0) {
                            String content = auditSpInstance.getItemname() + "申报材料已上传，请及时进行材料审核。";
                            for (FrameUser user : listUser) {
                                // 发送成功保留记录
                                messageCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(),
                                        0, new Date(), user.getMobile(), user.getUserGuid(), user.getDisplayName(), "",
                                        "", "", "", "", false, "短信");
                                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(),
                                        "【材料预审】" + auditSpISubapp.getSubappname(),
                                        IMessagesCenterService.MESSAGETYPE_WAIT, user.getUserGuid(),
                                        user.getDisplayName(), fromUserGuid, fromUserName, "材料预审", url,
                                        user.getOuGuid(), "", 1, "", "", subAppGuid, subAppGuid.substring(0, 1),
                                        new Date(), "", fromUserGuid, "", "");
                                sb.append(user.getUserGuid()).append(";");
                            }
                        }
                    }

                    // 记录登记时间
                    // 记录监控
                    AuditSpPhase auditSpPhase = null;
                    if (auditSpISubapp != null) {
                        // 通过阶段主键获取实体类
                        auditSpPhase = iAuditSpPhase.getAuditSpPhaseByRowguid(auditSpISubapp.getPhaseguid())
                                .getResult();
                    }
                    PhaseMonitorInfo info = monitorService.findBySubappguid(subAppGuid, "2");
                    if (info != null) {
                        info.setOperatedate(new Date());
                        // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                        info.setStatus("1");
                        // 设置完成时间
                        info.setStarttime(new Date());
                        info.set("ouguid", sb.toString());
                        monitorService.update(info);
                    } else {
                        info = new PhaseMonitorInfo();
                        info.setRowguid(UUID.randomUUID().toString());
                        info.set("subAppGuid", subAppGuid);
                        info.setOperatedate(new Date());
                        info.setItemname(auditSpInstance.getItemname());
                        info.set("ouguid", sb.toString());
                        if (auditSpPhase != null) {
                            info.setPhasename(auditSpPhase.getPhasename());
                        }
                        // 1、申报预审 2、材料审核 3、事项征求
                        info.setMonitortype("2");
                        info.setStarttime(new Date());
                        // 设置完成时间
                        // info.setProcesstime(new Date());
                        // 1、待处理 2、已处理 3、超期未处理 4、超期处理完成
                        info.setStatus("1");
                        monitorService.insert(info);
                    }
                }

                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取主题列表接口（项目表单页面下拉选择调用）
     *
     * @param params  传入的参数
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessList", method = RequestMethod.POST)
    public String getBusinessList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、查询辖区下启用的主题
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("businesstype", "1"); // 主题分类：建设性项目
                sqlConditionUtil.eq("del", "0"); // 主题状态：启用
                sqlConditionUtil.eq("areacode", areaCode); // 辖区编码
                sqlConditionUtil.setOrderDesc("ordernumber"); // 按排序字段降序
                List<AuditSpBusiness> auditSpBusinesses = iAuditSpBusiness
                        .getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();
                // 3、定义返回的主题数据
                List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                JSONObject objJson = new JSONObject();
                objJson.put("businessguid", ""); // 主题标识
                objJson.put("businessname", "请选择");// 主题名称
                businessJsonList.add(objJson);
                for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                   /* // 4、主题的第一阶段中配置了事项，且事项都是可用的
                    boolean flag = true;
                    // 4.1、获取主题下的阶段数据
                    List<AuditSpPhase> auditSpPhases = iAuditSpPhase
                            .getSpPaseByBusinedssguid(auditSpBusiness.getRowguid()).getResult();
                    List<AuditSpTask> auditSpTaskList = new ArrayList<AuditSpTask>();
                    if (auditSpPhases != null && auditSpPhases.size() > 0) {
                        // 4.2、获取第一个阶段下配置的事项
                        auditSpTaskList = iAuditSpTask.getAllAuditSpTaskByPhaseguid(auditSpPhases.get(0).getRowguid())
                                .getResult();
                        for (AuditSpTask auditSpTask : auditSpTaskList) {
                            // 4.3、循环主题第一个阶段下的所有事项，如果有事项是不可用的，则该主题不可申报
                            AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid())
                                    .getResult();
                            if (auditTask == null) {
                                flag = false;
                                break;
                            }
                        }
                    }*/
                    // 5、若主题下第一个阶段配置了事项且事项都可用则返回套餐基本信息
                    /* if (auditSpTaskList.size() != 0 && flag) {*/
                    JSONObject bussinessJson = new JSONObject();
                    bussinessJson.put("businessguid", auditSpBusiness.getRowguid()); // 主题标识
                    bussinessJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                    bussinessJson.put("splclx", auditSpBusiness.getSplclx());// 主题名称
                    businessJsonList.add(bussinessJson);
                    /* }*/
                }
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("businesslist", businessJsonList);
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取并联审批主题列表成功", dataJson.toString());
            } else {
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getBusinessList接口参数：params【" + params + "】=======");
            log.info("=======getBusinessList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取并联审批主题列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项及材料清单接口（对子申报实例点击上传材料按钮后加载的页面调用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getSPTaskAndMaterialInstanceList", method = RequestMethod.POST)
    public String getSPTaskAndMaterialInstanceList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getSPTaskAndMaterialInstanceList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取子申报实例
                String subAppGuid = obj.getString("subappguid");
                // 2、查询子申报实例,并判断该申报阶段的状态是否为已评审
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subAppGuid).getResult();
                /* if (ZwfwConstant.LHSP_Status_YPS.equals(auditSpISubapp.getStatus())) {*/
                String biGuid = auditSpISubapp.getBiguid();
                // 2.1、查询申报实例信息
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                String businessGuid = auditSpInstance.getBusinessguid();
                String companyId = auditSpISubapp.getApplyerguid();
                String phaseGuid = auditSpISubapp.getPhaseguid();
                // 2.2、查询申报项目信息
                AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                        .getAuditRsItemBaseinfoByRowguid(auditSpInstance.getYewuguid()).getResult();
                // 2.3 如果材料实例没有初始化，则初始化材料
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                    iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, companyId,
                            auditRsItemBaseinfo.getItemlegalcertnum()).getResult();
                }
                // 3、获取子申报事项实例列表
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getTaskInstanceBySubappGuid(subAppGuid)
                        .getResult();
                List<JSONObject> spiTaskJsonList = new ArrayList<JSONObject>();
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    // 4、返回阶段实例中的事项列表
                    JSONObject spiTaskJson = new JSONObject();
                    spiTaskJson.put("taskname", auditSpITask.getTaskname());
                    spiTaskJson.put("taskguid", auditSpITask.getTaskguid());
                    // 5、返回该事项下的材料列表
                    List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                    // 5.1、获取事项下的材料列表
                    List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                            .selectTaskMaterialListByTaskGuid(auditSpITask.getTaskguid(), false).getResult();
                    // 5.2、遍历事项下的材料列表，获取对应阶段事项实例中的材料列表
                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                        JSONObject spiMaterialJson = new JSONObject();
                        // 5.3、获取事项实例中的材料实例信息
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                .getSpIMaterialByMaterialGuid(subAppGuid, auditTaskMaterial.getMaterialid())
                                .getResult();
                        // 4.11、 判断材料来源
                        String materialsource = auditTaskMaterial.getFile_source();
                        // 5.6、如果没有找到对应的材料，则为共享/结果材料，找到对应材料输出
                        AuditSpShareMaterialRelation auditSpShareMaterialRelation = iAuditSpShareMaterialRelation
                                .getRelationByID(auditSpITask.getBusinessguid(), auditTaskMaterial.getMaterialid())
                                .getResult();
                        if (auditSpIMaterial == null) {
                            // 4.7、如果存在共享材料/结果材料
                            if (auditSpShareMaterialRelation != null) {
                                AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                        .getAuditSpShareMaterialByShareMaterialGuid(
                                                auditSpShareMaterialRelation.getSharematerialguid(), businessGuid)
                                        .getResult();
                                if (auditSpShareMaterial != null) {
                                    materialsource = auditSpShareMaterial.getFile_source();
                                }
                                // 4.9、获取存在的材料共享/结果关系
                                auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByMaterialGuid(subAppGuid,
                                        auditSpShareMaterialRelation.getSharematerialguid()).getResult();
                                // 4.10、判断是否是结果材料，如果是就不显示该材料
                                if (auditSpIMaterial == null
                                        || ZwdtConstant.STRING_YES.equals(auditSpIMaterial.getResult())) {
                                    continue;
                                }
                            } else {
                                // 没有找到共享材料数据，结束该循环
                                continue;
                            }
                        }
                        String necessity = auditSpIMaterial.getNecessity();
                        // 4.11、如果不是结果材料，判断是否共享关联了结果材料
                        // 查询共享材料配置关系
                        if (auditSpShareMaterialRelation != null) {
                            AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                    .getAuditSpShareMaterialByShareMaterialGuid(
                                            auditSpShareMaterialRelation.getSharematerialguid(), businessGuid)
                                    .getResult();
                            List<AuditSpShareMaterialRelation> shareMaterialList = iAuditSpShareMaterialRelation
                                    .selectByShareMaterialGuid(auditSpInstance.getBusinessguid(),
                                            auditSpShareMaterial.getSharematerialguid())
                                    .getResult();
                            // 判断材料中是否存在结果材料
                            if (shareMaterialList != null && shareMaterialList.size() > 0) {
                                for (AuditSpShareMaterialRelation item : shareMaterialList) {
                                    // 如果存在，则添加标识，用于前台判断
                                    if ("20".equals(item.getMaterialtype())) {
                                        necessity = ZwfwConstant.NECESSITY_SET_NO;
                                        // 存在一个即可跳出循环
                                        break;
                                    }
                                }
                            }
                        }
                        if (StringUtil.isBlank(materialsource)) {
                            materialsource = "申请人自备";
                        } else {
                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", materialsource);
                        }
                        spiMaterialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                : auditTaskMaterial.getPage_num());
                        spiMaterialJson.put("cliengguid", auditSpIMaterial.getCliengguid());
                        spiMaterialJson.put("stantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                : auditTaskMaterial.getStandard());
                        spiMaterialJson.put("materialsource", materialsource);
                        spiMaterialJson.put("type", auditTaskMaterial.getType());
                        spiMaterialJson.put("materialguid", auditSpIMaterial.getRowguid());
                        spiMaterialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());
                        spiMaterialJson.put("materialinstancename", auditTaskMaterial.getMaterialname());
                        spiMaterialJson.put("materialinstanceguid", auditSpIMaterial.getMaterialguid());
                        spiMaterialJson.put("status", auditSpIMaterial.getStatus());
                        // 是否共享材料
                        spiMaterialJson.put("shared", auditSpIMaterial.getShared());
                        // 是否必须  10必须 20非必须
                        spiMaterialJson.put("necessity", necessity);
                        // 是否容缺
                        spiMaterialJson.put("allowrongque", auditSpIMaterial.getAllowrongque());
                        spiMaterialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                String.valueOf(auditSpIMaterial.getSubmittype())));
                        // 示例表格
                        String exampleClientGuid = auditTaskMaterial.getExampleattachguid();
                        if (StringUtil.isNotBlank(exampleClientGuid)) {
                            int exampleAttachCount = iAttachService.getAttachCountByClientGuid(exampleClientGuid);
                            if (exampleAttachCount > 0) {
                                spiMaterialJson.put("exampleattachguid", exampleClientGuid);
                            }
                        }
                        // 空白表格
                        String templateClientGuid = auditTaskMaterial.getTemplateattachguid();
                        if (StringUtil.isNotBlank(templateClientGuid)) {
                            int templateAttachCount = iAttachService.getAttachCountByClientGuid(templateClientGuid);
                            if (templateAttachCount > 0) {
                                spiMaterialJson.put("templateattachguid", templateClientGuid);
                            }
                        }
                        // 3.12、 判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                        int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                        int showButton = 0; // 按钮显示方式
                        String needLoad = ZwdtConstant.CERTLEVEL_C.equals(auditTaskMaterial.get("materialLevel"))
                                ? "1" : "0"; // 是否需要上传到证照库  0:不需要 、1:需要
                        if (StringUtil.isBlank(auditTaskMaterial.getSharematerialguid())) {
                            // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = count > 0 ? 4 : 3;
                            } else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = count > 0 ? 1 : 0;
                            }
                        } else {
                            // 4.6.2、如果关联了证照库
                            if (count > 0) {
                                // 4.6.2.1、获取材料类别
                                String materialType = auditTaskMaterial.get("materialType");
                                if (materialType == null) {
                                    materialType = "0";
                                }
                                if (Integer.parseInt(materialType) == 1) {
                                    // 4.6.2.1.1、已引用证照库
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                } else if (Integer.parseInt(materialType) == 2) {
                                    // 4.6.2.1.2、已引用批文
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                } else {
                                    // 4.6.2.1.3、已引用材料
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                }
                            } else {
                                // 4.6.2.2、如果没有附件，则标识为未上传
                                showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_WSC);

                            }
                        }
                        spiMaterialJson.put("showbutton", showButton);
                        spiMaterialJson.put("needload", needLoad);
                        spiMaterialJsonList.add(spiMaterialJson);
                    }
                    // 4、存入事项下的材料列表
                    spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                    spiTaskJsonList.add(spiTaskJson);
                }
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("spitasklist", spiTaskJsonList);
                log.info("=======结束调用getSPTaskAndMaterialInstanceList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项及材料清单成功！", dataJson.toString());
              /*  }
                else {
                    return JsonUtils.zwdtRestReturn("0", "当前阶段不可提交材料！", "");
                }*/
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getSPTaskAndMaterialInstanceList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项及材料清单异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（并联审批申报提交时调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                int noSubmitNum = 0;//必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取材料实例信息
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(materialGuid)
                                .getResult();
                        int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
                        String isRongque = auditSpIMaterial.getAllowrongque(); // 材料容缺状态
                        if (auditSpIMaterial != null) {
                            // 先判断是否共享材料
                            AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                    .getAuditSpShareMaterialByRowguid(auditSpIMaterial.getMaterialguid()).getResult();
                            if (auditSpShareMaterial != null) {
                                List<AuditSpShareMaterialRelation> shareMaterialList = iAuditSpShareMaterialRelation
                                        .selectByShareMaterialGuid(auditSpIMaterial.getBusinessguid(),
                                                auditSpShareMaterial.getSharematerialguid())
                                        .getResult();
                                // 判断材料中是否存在结果材料
                                if (shareMaterialList != null && shareMaterialList.size() > 0) {
                                    boolean isExistResult = false;
                                    for (AuditSpShareMaterialRelation item : shareMaterialList) {
                                        // 如果存在，则添加标识，用于前台判断
                                        if ("20".equals(item.getMaterialtype())) {
                                            isExistResult = true;
                                            // 存在一个即可跳出循环
                                            break;
                                        }
                                    }
                                    // 存在共享结果文件的材料，不判断是否必需
                                    if (isExistResult) {
                                        continue;
                                    }
                                }
                            }
                            // 必要非容缺材料判断提交状态
                            if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())
                                    && !ZwdtConstant.STRING_YES.equals(isRongque)) {
                                // 必要材料如果未提交，则数量+1
                                if (status == ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT) {
                                    noSubmitNum++;
                                }
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkAllMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取简单项目基本信息接口（查看项目阶段时调用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getFlowPath", method = RequestMethod.POST)
    public String getFlowPath(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getFlowPath接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取业务实例
                String businessguid = obj.getString("businessguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();
                String jsIndex = "index.js";
                if (auditSpBusiness != null && StringUtil.isNotBlank(auditSpBusiness.getStr("processimgurl"))) {
                    jsIndex = auditSpBusiness.getStr("processimgurl");
                }
                jsIndex = "../../../zmdzwdt/individuation/overall/pages/approve/js/flow/"
                        + jsIndex.replace("index", "flowdatajson");
                dataJson.put("jspath", jsIndex);
                log.info("=======结束调用getFlowPath接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取流程图路径信息成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getFlowPath接口参数：params【" + params + "】=======");
            log.info("=======getFlowPath接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取流程图路径信息异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取阶段及对应的子申报实例信息（查看阶段阶段信息页面）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getPhaseAndSPSubappList", method = RequestMethod.POST)
    public String getPhaseAndSPSubappList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始获取getPhaseAndSPSubappList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject json = JSONObject.parseObject(params);
            String token = json.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) json.get("params");
                // 1.1、获取业务实例
                String biGuid = obj.getString("biguid");
                // 1.2、获取阶段实例
                String businessguid = obj.getString("businessguid");
                // 2.1、阶段列表List
                List<JSONObject> phaseJsonList = new ArrayList<JSONObject>();
                // 3、获取阶段列表
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessguid).getResult(); // 排序
                // TODO
                for (AuditSpPhase auditSpPhase : auditSpPhases) {
                    JSONObject phaseObject = new JSONObject();
                    phaseObject.put("phasename", auditSpPhase.getPhasename());
                    phaseObject.put("phaseguid", auditSpPhase.getRowguid());

                    String phaseNum = null;
                    switch (auditSpPhase.getPhaseId()) {
                        case "1":
                            phaseNum = "one";
                            break;
                        case "2":
                            phaseNum = "two";
                            break;
                        case "3":
                            phaseNum = "three";
                            break;
                        case "4":
                            phaseNum = "four";
                            break;
                        default:
                            break;
                    }
                    phaseObject.put("phasenum", phaseNum);
                    // 3.1、获取该阶段下子申报列表
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("biguid", biGuid);
                    sqlConditionUtil.eq("phaseguid", auditSpPhase.getRowguid());
                    sqlConditionUtil.setOrderDesc("CREATEDATE");
                    List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappListByMap(sqlConditionUtil.getMap())
                            .getResult();
                    // 3.2、子申报JSON列表
                    List<JSONObject> subappJsonList = new ArrayList<JSONObject>();
                    for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                        JSONObject spiSubappJson = new JSONObject();
                        spiSubappJson.put("subappname", auditSpISubapp.getSubappname());
                        spiSubappJson.put("subappguid", auditSpISubapp.getRowguid());
                        spiSubappJson.put("subappstatus", auditSpISubapp.getStatus());
                        subappJsonList.add(spiSubappJson);
                    }
                    phaseObject.put("subapplist", subappJsonList);
                    // 3.3、获取是否允许阶段申报
                    String allowMultiSubApply = auditSpPhase.getAllowmultisubapply();
                    int allowmultisubapply = 0;
                    if (ZwdtConstant.STRING_YES.equals(allowMultiSubApply)) {
                        allowmultisubapply = 1;
                    } else {
                        if (auditSpISubapps.size() == 0) {
                            allowmultisubapply = 1;
                        }
                    }
                    phaseObject.put("allowmultisubapply", allowmultisubapply);
                    phaseJsonList.add(phaseObject);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("phaselist", phaseJsonList);
                log.info("=======结束调用getPhaseAndSPSubappList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取阶段及对应的子申报实例信息成功！", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getPhaseAndSPSubappList接口参数：params【" + params + "】=======");
            log.info("=======getPhaseAndSPSubappList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取阶段及对应的子申报实例信息异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getSbznBusinessList", method = RequestMethod.POST)
    public String getSbznBusinessList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getSbznBusinessList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 1、接口的入参转化为JSON对象
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取辖区编码
            String areaCode = obj.getString("areacode");
            String businessguid = obj.getString("businessguid");
            areaCode = "370800";
            // 2、查询辖区下启用的主题
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("businesstype", "1"); // 主题分类：建设性项目
            sqlConditionUtil.eq("del", "0"); // 主题状态：启用
            sqlConditionUtil.eq("areacode", areaCode); // 辖区编码
            sqlConditionUtil.setOrderDesc("ordernumber"); // 按排序字段降序
            List<AuditSpBusiness> auditSpBusinesses = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap())
                    .getResult();
            // 3、定义返回的主题数据
            List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
            // JSONObject objJson = new JSONObject();
            /*
             * objJson.put("businessguid", ""); // 主题标识
             * objJson.put("businessname", "请选择");// 主题名称
             * businessJsonList.add(objJson);
             */
            for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                // 4、主题的第一阶段中配置了事项，且事项都是可用的
                boolean flag = true;
                // 4.1、获取主题下的阶段数据
                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(auditSpBusiness.getRowguid())
                        .getResult();
                List<AuditSpTask> auditSpTaskList = new ArrayList<AuditSpTask>();
                if (auditSpPhases != null && auditSpPhases.size() > 0) {
                    // 4.2、获取第一个阶段下配置的事项
                    auditSpTaskList = iAuditSpTask.getAllAuditSpTaskByPhaseguid(auditSpPhases.get(0).getRowguid())
                            .getResult();
                    for (AuditSpTask auditSpTask : auditSpTaskList) {
                        // 4.3、循环主题第一个阶段下的所有事项，如果有事项是不可用的，则该主题不可申报
                        if (StringUtil.isNotBlank(auditSpTask.getTaskid())) {
                            AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid())
                                    .getResult();
                            if (auditTask == null) {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
                // 5、若主题下第一个阶段配置了事项且事项都可用则返回套餐基本信息
                if (auditSpTaskList.size() != 0 && flag) {
                    JSONObject bussinessJson = new JSONObject();
                    bussinessJson.put("businessguid", auditSpBusiness.getRowguid()); // 主题标识
                    bussinessJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                    if (businessguid.equals(auditSpBusiness.getRowguid())) {
                        bussinessJson.put("isselected", "1");
                    }
                    businessJsonList.add(bussinessJson);
                }
            }
            // 6、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("businesslist", businessJsonList);
            log.info("=======结束调用getSbznBusinessList接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取申报指南并联审批主题列表成功", dataJson.toString());

        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======getSbznBusinessList接口参数：params【" + params + "】=======");
            log.info("=======getSbznBusinessList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取并联审批主题列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 网厅申报指南获取的主题接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */

    @RequestMapping(value = "/getSbznTaskmaterialList", method = RequestMethod.POST)
    public String getSbznTaskmaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getSbznTaskmaterialList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // String phaseguid = obj.getString("phaseguid");
                String businessguid = obj.getString("businessguid");// 主题标识
                // AuditSpBusiness auditspbusiness =
                // iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();

                // 3、定义返回的主题数据
                List<AuditSpTask> auditSpTaskList = new ArrayList<AuditSpTask>();
                List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                List<JSONObject> phasenameJsonList = new ArrayList<JSONObject>();

                List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessguid).getResult();// 获取主题下阶段
                if (auditSpPhases.size() > 0) {
                    for (AuditSpPhase auditspphase : auditSpPhases) {
                        JSONObject phasenameobject = new JSONObject();
                        JSONObject phasendatalistobject = new JSONObject();

                        List<JSONObject> businessphaseJsonList = new ArrayList<JSONObject>();

                        String phasename = auditspphase.getPhasename();
                        String phaseid = auditspphase.getPhaseId();
                        phasenameobject.put("phasename", phasename);
                        phasenameobject.put("phaseid", phaseid);
                        phasenameobject.put("phasename", phasename);
                        phasenameJsonList.add(phasenameobject);
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("phaseguid", auditspphase.getRowguid()); // 主题分类：建设性项目
                        sqlConditionUtil.eq("businessguid", businessguid); // 主题状态：启用
//                        if ("5".equals(phaseid)) {
//                            // 并行
//                            // sqlConditionUtil.eq("qlkind", "bxtj");
//                            sqlConditionUtil.isBlank("qlkind");
//                        }
//                        else {
//                            // 主流程需要过滤掉并行推进事项
//                            sqlConditionUtil.isBlank("qlkind");
//                        }
                        // 4.2、
                        auditSpTaskList = iAuditSpTask.getAllAuditSpTask(sqlConditionUtil.getMap()).getResult();
                        if (auditSpTaskList != null && auditSpTaskList.size() > 0) {
                            for (AuditSpTask auditSpTask : auditSpTaskList) {
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("basetaskguid", auditSpTask.getBasetaskguid());
                                sql.eq("areacode", "370800");
                                List<AuditSpBasetaskR> listtastr = iauditspbasetaskr
                                        .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                if (listtastr != null && listtastr.size() > 0) {

                                    for (AuditSpBasetaskR auditspbasetaskr : listtastr) {
                                        AuditTask auditTask = iAuditTask
                                                .selectUsableTaskByTaskID(auditspbasetaskr.getTaskid()).getResult();
                                        if (auditTask == null || auditTask.getIs_enable() == 0) {
                                            continue;
                                        }
                                        JSONObject spiTaskJson = new JSONObject();
                                        if (StringUtil.isNotBlank(auditTask)) {
                                            spiTaskJson.put("taskname", auditTask.getTaskname());// 事项名称
                                            spiTaskJson.put("taskguid", auditTask.getTask_id());// 事项id
                                            spiTaskJson.put("promiseDay", auditTask.getPromise_day());// 承诺时限
                                            spiTaskJson.put("anticipateDay", auditTask.getAnticipate_day());// 法定时限
                                            spiTaskJson.put("ouName", auditTask.getOuname());// 部门名称
                                            List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                                    .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), false)
                                                    .getResult();
                                            List<JSONObject> spiMaterialJsonList = new ArrayList<JSONObject>();
                                            int i = 1;// 记录材料数
                                            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                                JSONObject spiMaterialJson = new JSONObject();
                                                spiMaterialJson.put("materialinstancename",
                                                        auditTaskMaterial.getMaterialname());
                                                spiMaterialJson.put("necessity", auditTaskMaterial.getMaterialname());
                                                spiMaterialJson.put("pagenum", 1);
                                                spiMaterialJson.put("i", i++);
                                                spiMaterialJson.put("submittype",
                                                        iCodeItemsService.getItemTextByCodeName("提交方式",
                                                                String.valueOf(auditTaskMaterial.getSubmittype())));

                                                spiMaterialJson.put("empty", StringUtil.isNotBlank(auditTaskMaterial
                                                        .getTemplateattachguid()) ? (ValidateUtil.isNotBlankCollection(
                                                        iAttachService.getAttachInfoListByGuid(auditTaskMaterial
                                                                .getTemplateattachguid())) ? iAttachService
                                                        .getAttachInfoListByGuid(auditTaskMaterial
                                                                .getTemplateattachguid())
                                                        .get(0).getAttachGuid() : "") : "");
                                                spiMaterialJson.put("example", StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid()) ? (ValidateUtil
                                                        .isNotBlankCollection(iAttachService.getAttachInfoListByGuid(
                                                                auditTaskMaterial.getExampleattachguid()))
                                                        ? iAttachService
                                                        .getAttachInfoListByGuid(
                                                                auditTaskMaterial
                                                                        .getExampleattachguid())
                                                        .get(0).getAttachGuid()
                                                        : "") : "");

                                                spiMaterialJsonList.add(spiMaterialJson);

                                            }
                                            spiTaskJson.put("spimateriallist", spiMaterialJsonList);
                                            businessphaseJsonList.add(spiTaskJson);
                                        }

                                    }
                                }

                            }
                        }
                        phasendatalistobject.put("datalist", businessphaseJsonList);
                        businessJsonList.add(phasendatalistobject);
                    }

                }

                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("phasetasklist", businessJsonList);
                dataJson.put("phasenamelist", phasenameJsonList);
                System.out.println(dataJson.toString());
                log.info("=======结束调用getSbznTaskmaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取该主题下阶段事项成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getSbznTaskmaterialList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            e.printStackTrace();
            log.info("=======getSbznTaskmaterialList接口参数：params【" + params + "】=======");
            log.info("=======getSbznTaskmaterialList接口异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取并联审批配置到阶段事项失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取用户唯一标识
     *
     * @param httpServletRequest
     * @return
     */
   /* private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
            			.getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }*/
}
