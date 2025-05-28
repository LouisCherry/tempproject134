package com.epoint.zzlb.cert;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.DataSet;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.JNGenerateCert;
import com.epoint.cert.basic.certarea.domain.CertArea;
import com.epoint.cert.basic.certarea.inter.ICertArea;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certownerinfo.domain.CertOwnerInfo;
import com.epoint.cert.basic.certownerinfo.inter.ICertOwnerInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.FormatUtil;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.common.cert.authentication.CertUserSession;
import com.epoint.common.cert.authentication.ConfigBizlogic;
import com.epoint.common.cert.authentication.GenerateBizlogic;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 证照基本信息表修改页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("certinfoeditzzlbaction")
@Scope("request")
public class CertInfoEditZzlbAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 证照基本信息表实体对象
     */
    private CertInfo certinfo;

    /**
     * 证照照面信息实体对象
     */
    private CertInfoExtension dataBean;

    /**
     * 元数据列表(顶层)
     */
    private List<CertMetadata> metadataList;

    /**
     * 可信等级下拉列表model
     */
    private List<SelectItem> certLevelModel;

    /**
     * 持证人证件类型下拉列表model
     */
    private List<SelectItem> certOwnerCertTypeModel;

    /**
     * 证照附件（正本）上传
     */
    private FileUploadModel9 certFileUploadModel;

    /**
     * 证照附件（副本）上传
     */
    private FileUploadModel9 copyCertFileUploadModel;

    /**
     * 照面信息附件上传map
     */
    private Map<String, FileUploadModel9> modelMap = new HashMap<>();

    /**
     * 证照基本信息api
     */
    @Autowired
    private ICertInfo iCertInfo;

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 证照元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 框架附件api
     */
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IConfigService configService;

    /**
     * 消息api
     */
    @Autowired
    private IMessagesCenterService messagesService;

    /**
     * 角色api
     */
    @Autowired
    private IRoleService roleService;

    /**
     * 人员api
     */
    @Autowired
    private IUserService userService;

    @Autowired
    private ICertArea areaService;

    /**
     * 部门api
     */
    @Autowired
    private IOuService ouService;

    /**
     * 多持有人api
     */
    @Autowired
    private ICertOwnerInfo ownerInfoService;

    /**
     * 证照信息bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 生成证照bizlogic
     */
    private GenerateBizlogic generateBizlogic = new GenerateBizlogic();

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(CertInfoEditZzlbAction.class);

    /**
     * 证照目录实体
     */
    private CertCatalog certCatalog;

    /**
     * 目录证照模板（副本）文件数量
     */
    private int copyTempletCount = 0;

    /**
     * 证照基本信息guid
     */
    private String guid;


    /**
     * 在用版本证照基本信息guid
     */
    private String oldcertguid;

    /**
     * 照面信息主键
     */
    private String extendguid;

    /**
     * 是否启用证照审核
     */
    private String isenablecertinfoaudit;

    /**
     * 多持有人信息
     */
    private String certownerinfo;

    /**
     * 证书数据
     */
    private String certData;

    /**
     * key数据
     */
    private String keyString;

    private String certno;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        oldcertguid = getRequestParameter("oldcertguid");
        // 获得基本信息
        certinfo = iCertInfo.getCertInfoByRowguid(guid);
        // 验证是否存在基本信息
        if (certinfo == null) {
            // 不存在证照基本信息，提示消息，并关闭页面
            addCallbackParam("msg", "该证照基础信息不存在！");
            return;
        }
        if(StringUtil.isNotBlank(certinfo.getCertno()) && CertConstant.CERT_LEVEL_A.equals(certinfo.getCertlevel())
                && certinfo.getVersion() != 1){
            certno = certinfo.getCertno();
        }
        // 缓存证照编号
        addViewData("certno", certinfo.getCertno());
        //如果没有证照附件，设置Certcliengguid
        if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
            certinfo.setCertcliengguid(UUID.randomUUID().toString());
        }
        // 获得元数据配置表所有顶层节点
        metadataList = iCertMetaData.selectTopDispinListByCertguid(certinfo.getCertareaguid());
        // 设置页面展示的持有人类型
        certCatalog = iCertCatalog.getCatalogDetailByrowguid(certinfo.getCertareaguid());
        if (certCatalog != null) {
            addCallbackParam("certownertype", certCatalog.getBelongtype());
        }
        // 回传目录模板文件（副本）个数
        if (StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
            copyTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid());
        }
        addCallbackParam("copyTempletCount", copyTempletCount);

        // 获得照面信息
        Map<String, Object> filter = new HashMap<>();
        // 设置基本信息guid
        filter.put("certinfoguid", guid);
        dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
        if (dataBean != null) {
            extendguid = dataBean.getRowguid();
        }else{
            dataBean = new CertInfoExtension();
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setCertinfoguid(guid);
        }

        if(CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())){
            List<CertOwnerInfo> ownerInfos = ownerInfoService.selectOwnerInfoByCertInfoGuid(guid);
            JSONArray jsonArray = new JSONArray();
            for (CertOwnerInfo ownerInfo : ownerInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ownerno", FormatUtil.getdecryptSM4ToData(ownerInfo.getCertownerno() , certinfo.getCertcatalogid() , -1));
                jsonObject.put("ownername", FormatUtil.getdecryptSM4ToData(ownerInfo.getCertownername() , certinfo.getCertcatalogid() , -1));
                jsonObject.put("ownertype", ownerInfo.getCertownercerttype());

                jsonArray.add(jsonObject);
            }
            certownerinfo = jsonArray.toString();
            if(!isPostback()){
                addCallbackParam("codetype", getCertOwnerCertTypeModel());
            }
        }else{
            certinfo.setCertownername(FormatUtil.getdecryptSM4ToData(certinfo.getCertownername() , certinfo.getCertcatalogid() , -1));
            certinfo.setCertownerno(FormatUtil.getdecryptSM4ToData(certinfo.getCertownerno() , certinfo.getCertcatalogid() , -1));
        }

        // 返回渲染的字段(子表修改模式)
        addCallbackParam("controls",
                certInfoBizlogic.generateMapUseSubExtension("certinfoeditaction" ,metadataList,
                        CertConstant.MODE_EDIT, CertConstant.SUB_MODE_EDIT));

        if(StringUtil.isNotBlank(certinfo.get("certawarddeptguid"))){
            addCallbackParam("certawarddeptguid", certinfo.get("certawarddeptguid"));
        }
        if(StringUtil.isNotBlank(certinfo.getCertawarddept())){
            addCallbackParam("certawarddept", certinfo.getCertawarddept().replace("^", ","));
        }
        addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
        //渲染基本信息
        addCallbackParam("basiccontrols", certInfoBizlogic.generateBasicDefaultMap(metadataList, false, "certinfo", certCatalog.getIsmultiowners()));

        isenablecertinfoaudit = configService.getFrameConfigValue("IsEnableCertInfoAudit_" + CertUserSession.getInstance().getAreaCode());
        addCallbackParam("isenablecertinfoaudit", isenablecertinfoaudit);
        addCallbackParam("version", certinfo.getVersion());
        addCallbackParam("certlevel", certinfo.getCertlevel());
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
        addCallbackParam("ismultiowners", certCatalog.getIsmultiowners());
        //是否要对ofd签章
        if(StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))){
            List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certCatalog.getTempletcliengguid());
            if(frameAttachInfos.size()>0 && StringUtil.toLowerCase(frameAttachInfos.get(0).getAttachFileName()).endsWith("ofd")){
                if(CertConstant.CONSTANT_STR_ONE.equals(ConfigUtil.getConfigValue("ofdautoseal"))){
                    addCallbackParam("ofdautoseal", "1");
                }
            }
        }
    }

    /**
     * 变更保存
     */
    public void change(String ouguids) {
        // 判断目录是否开启
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("isClose", "0");
            addCallbackParam("msg", catalogMsg);
            return;
        }
        if (certinfo != null) {
            // 解决多人操作问题（重复归档/上报）
            String errorMsg = "该证照已归档，操作无效！";
            // 是否开启实例审核
            if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) { // 启用实例审核
                errorMsg = "该证照已上报，操作无效！";
            }
            // 不为待上报/审核不通过，提示错误信息
            if (!CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(certinfo.getAuditstatus()) &&
                    !CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(certinfo.getAuditstatus())) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg", errorMsg);
                return;
            }

            // 照面信息附件非空验证
            String msg = validateImageType();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg", msg);
                return;
            }
            //验证目录是否允许多实例数据
            msg=validateIsoneCertInfo();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg", msg);
                return;
            }

            //第一个版本检查编号是否重复
            if(!certinfo.getCertno().equals(getViewData("certno"))){
                boolean existCertno = iCertInfo.isExistCertno(certCatalog.getCertcatalogid(), certinfo.getCertno());
                if (existCertno) {
                    addCallbackParam("msg", "证照编号重复，保存失败！");
                    addCallbackParam("isClose", "0");
                    return;
                }
            }
            // 变更证照
            saveOrChangeCert(true, ouguids);

            if(CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)){
                if(certinfo.getVersion() > 1){
                    // 添加日志（变更待上报）
                    certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                            CertConstant.LOG_OPERATETYPE_CHANGE_REPORT, "", null);
                }
                FrameRole frameRole = roleService.getRoleByRoleField("rolename", "证照审核员");
                if (frameRole != null) {
                    String roleguid = frameRole.getRoleGuid();
                    // 2、获取该角色的对应的人员
                    String ouguid = "";
                    CertArea area = areaService.getCertAreaByareacode(CertUserSession.getInstance().getAreaCode());
                    if (area != null) {
                        ouguid = area.getOuguid();
                    }
                    List<FrameUser> listUser = userService.listUserByOuGuid(area.getOuguid(), roleguid, "", "", false, true, false, 3);
                    if (listUser != null && listUser.size() > 0) {
                        // 3、发送待办给审核人员
                        for (FrameUser frameUser : listUser) {
                            String title = "【证照审核】" + certinfo.getCertname();
                            // 处理页面
                            String handleurl = "epointcert/certinfo/cert/certinfoaudit?guid=" + certinfo.getRowguid();
                            String messageItemGuid = UUID.randomUUID().toString();
                            messagesService.insertWaitHandleMessage(messageItemGuid, title,
                                    IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                                    frameUser.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "证照审核",
                                    handleurl, frameUser.getOuGuid(), "", CertConstant.CONSTANT_INT_ONE, "", "",
                                    certinfo.getRowguid(), "", new Date(), "", frameUser.getUserGuid(), "", "");
                        }
                    }
                }
                // 返回消息
                addCallbackParam("msg", "上报成功");
            }else{
                // 添加日志
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                        CertConstant.LOG_OPERATETYPE_MODIFY, "", null);
                // 返回消息
                addCallbackParam("msg", "变更成功！");
            }
            addCallbackParam("isClose", "1");
        }
    }

    /**
     * 保存修改
     */
    public void save(String ouguids) {
        // 判断目录是否开启
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("isClose", "0");
            addCallbackParam("msg", catalogMsg);
            return;
        }

        if (certinfo != null) {
            // 不为待上报、审核不通过实例不可保存（解决多人操作）
            if (!CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(certinfo.getAuditstatus()) && !CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(certinfo.getAuditstatus())) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg",  "该证照不为草稿状态，操作无效！");
                return;
            }

            // 照面信息附件非空验证
            String msg = validateImageType();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg", msg);
                return;
            }

            //验证目录是否允许多实例数据
            msg=validateIsoneCertInfo();
            if (StringUtil.isNotBlank(msg)) {
                addCallbackParam("isClose", "0");
                addCallbackParam("msg", msg);
                return;
            }

            //第一个版本检查编号是否重复
            if(!certinfo.getCertno().equals(getViewData("certno"))){
                boolean existCertno = iCertInfo.isExistCertno(certCatalog.getCertcatalogid(), certinfo.getCertno());
                if (existCertno) {
                    addCallbackParam("msg", "证照编号重复，保存失败！");
                    addCallbackParam("isClose", "0");
                    return;
                }
            }

            // 保存证照
            saveOrChangeCert(false, ouguids);
            // 返回消息
            addCallbackParam("isClose", "1");
            addCallbackParam("msg", "保存成功！");
        }
    }

    /**
     *  照面信息附件非空验证
     *  @return
     */
    private String validateImageType() {
        String msg = "";
        // 照面信息附件非空验证
        if (ValidateUtil.isNotBlankCollection(metadataList) && dataBean != null && !dataBean.isEmpty()) {
            Set<String> nameSet = new HashSet<>();
            for (CertMetadata metadata : metadataList) {
                if ("image".equals(StringUtil.toLowerCase(metadata.getFieldtype()))
                        && CertConstant.CONSTANT_STR_ONE.equals(metadata.getNotnull())) {
                    String cliengguid = dataBean.getStr(metadata.getFieldname());
                    if (StringUtil.isNotBlank(cliengguid)) {
                        int attchCount = iAttachService.getAttachCountByClientGuid(cliengguid);
                        if (attchCount == 0) {
                            nameSet.add(metadata.getFieldchinesename());
                        }
                    }
                }
            }

            if (ValidateUtil.isNotBlankCollection(nameSet)) {
                msg = String.format("%s不能为空！", nameSet.toString());
            }
        }

        return msg;
    }

    /**
     *  验证目录是否允许多实例数据
     *
     */
    private String validateIsoneCertInfo(){
        String msg="";
        CertCatalog certCatalog = iCertCatalog.getCatalogDetailByrowguid(certinfo.getCertareaguid());
        // 证照目录是否是多实例数据验证
        if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsoneCertInfo())) {
            //TODO 多持有人 暂时用循环处理
            if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {
                JSONArray jsonArray = JSONArray.parseArray(certownerinfo);
                for (Object object : jsonArray) {
                    JSONObject jsonCert = (JSONObject) object;

                    // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
                    SqlUtils sql = new SqlUtils();
                    sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                    sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                    sql.nq("certid", certinfo.getCertid());
                    if (StringUtil.isNotBlank(oldcertguid)) {
                        //排除本身这条记录
                        sql.nq("rowguid", oldcertguid);
                    }
                    int oldcertinfocount = iCertInfo.getCertInfoListMultiOwner(sql.getMap(), certCatalog.getCertcatalogid(),
                            jsonCert.getString("ownerno"), jsonCert.getString("ownertype")).size();
                    if (oldcertinfocount > 0) {
                        msg = "该证照类型下同一持证主体代码类型、持证主体代码，只能存在一条证照实例！";
                    }
                }
            } else {
                // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
                SqlUtils sql = new SqlUtils();
                sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                sql.nq("certid", certinfo.getCertid());
                if (StringUtil.isNotBlank(oldcertguid)) {
                    //排除本身这条记录
                    sql.nq("rowguid", oldcertguid);
                }
                int oldcertinfocount = iCertInfo.getCertInfoListMultiOwner(sql.getMap(), certCatalog.getCertcatalogid(),
                        certinfo.getCertownerno(), certinfo.getCertownercerttype()).size();
                if (oldcertinfocount > 0) {
                    msg = "该证照类型下同一持证主体代码类型、持证主体代码，只能存在一条证照实例！";
                }
            }
        }
        return msg;
    }

    /**
     *  保存或变更证照
     *  @param isChange 是否变更
     */
    private void saveOrChangeCert(boolean isChange, String ouguids) {
        if (certinfo == null) {
            return;
        }

        if(StringUtil.isNotBlank(certinfo.getCertownertype()) && StringUtil.isNotBlank(certCatalog.getBelongtype())){
            if(CertConstant.CERTOWNERTYPE_ZRR.equals(certCatalog.getBelongtype()) || CertConstant.CERTOWNERTYPE_FR.equals(certCatalog.getBelongtype())){
                if(!certinfo.getCertownertype().equals(certCatalog.getBelongtype()) && CertConstant.CERT_LEVEL_A.equals(certinfo.getCertlevel())){
                    throw new ReadOnlyException("certownertype");
                }
            }
        }

        if(StringUtil.isNotBlank(certno) && !certno.equals(certinfo.getCertno())){
            throw new ReadOnlyException("certno");
        }

        // 更新基本信息
        certinfo.setOperateusername(userSession.getDisplayName());
        certinfo.setOperatedate(new Date());
        //certinfo.remove("certownertype"); // 排除持有人类型
        certinfo.setAdduserguid(userSession.getUserGuid());
        certinfo.setAddusername(userSession.getDisplayName());
        if(StringUtil.isNotBlank(ouguids)) {
            StringBuilder oucodes = new StringBuilder();
            certinfo.setCertawarddeptguid(ouguids);
            String[] ouguidarr = ouguids.split(";");
            for(int i = 0 ; i < ouguidarr.length ; i++){
                FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguidarr[i]);
                oucodes.append((StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))?ouExtendInfo.getStr("orgcode") : "") + "^");
            }
            oucodes.deleteCharAt(oucodes.length()-1);
            certinfo.setCertawarddeptorgcode(oucodes.toString());
        }
        certinfo.setCertawarddept(certinfo.getCertawarddept().replace(",", "^"));
        // 变更
        if (isChange) {
            if (CertConstant.CONSTANT_STR_ONE.equals(isenablecertinfoaudit)) { // 开启审核
                // 状态更新为待审核
                certinfo.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
                certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_CHECK);
                certinfo.setStatus(CertConstant.CERT_STATUS_COMMON);
            } else { // 不开启审核，草稿生效
                boolean isCertChange = CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(certinfo.getAuditstatus())
                        && CertConstant.CONSTANT_INT_NEGATIVE_ONE.equals(certinfo.getIshistory());
                if (isCertChange) {
                    // 草稿生效
                    certinfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                    certinfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                    certinfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                    certinfo.setVersiondate(new Date());
                    // 老版本失效
                    SqlUtils sqlUtils = new SqlUtils();
                    int version = certinfo.getVersion() - 1;
                    sqlUtils.eq("certid", certinfo.getCertid());
                    sqlUtils.eq("version", String.valueOf(version));
                    sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                    sqlUtils.eq("status", CertConstant.CERT_STATUS_COMMON);
                    //类型是证照的
                    sqlUtils.eq("materialtype", CertConstant.CONSTANT_STR_ONE);
                    List<CertInfo> certInfoList = iCertInfo.getListByCondition(sqlUtils.getMap());
                    if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                        CertInfo oldCertInfo = certInfoList.get(0);
                        oldCertInfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                        iCertInfo.updateCertInfo(oldCertInfo);
                    }
                }
                //设置证照标识
                certinfo.setCertificateidentifier(certInfoBizlogic.generateIdentifier(certinfo, certCatalog.getCertificatetypecode()));
            }
        }

        //多持有人处理
        if(CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())){
            //先删除
            ownerInfoService.deleteOwnerInfoByCertInfoGuid(certinfo.getRowguid());

            JSONArray jsonArray = JSONArray.parseArray(certownerinfo);
            StringBuilder certownerno = new StringBuilder();
            StringBuilder certownername =  new StringBuilder();
            StringBuilder certownercerttype =  new StringBuilder();
            for (Object object : jsonArray) {
                //新增持有人信息
                JSONObject jsonCert = (JSONObject) object;
                CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                certOwnerInfo.setCertinfoguid(certinfo.getRowguid());
                certOwnerInfo.setCertownerno(FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownerno") , certinfo.getCertcatalogid() , ""));
                certOwnerInfo.setCertownername(FormatUtil.getEncryptSM4ToData(jsonCert.getString("ownername") , certinfo.getCertcatalogid() , ""));
                certOwnerInfo.setCertownercerttype(jsonCert.getString("ownertype"));
                ownerInfoService.addCertOwnerInfo(certOwnerInfo);

                certownerno.append(jsonCert.getString("ownerno") + "^");
                certownername.append(jsonCert.getString("ownername") + "^");
                certownercerttype.append(jsonCert.getString("ownertype") + "^");
            }

            certownername.deleteCharAt(certownername.length() - 1);
            certownercerttype.deleteCharAt(certownercerttype.length() - 1);
            //设置持有人字段
            certinfo.setCertownerno(certownerno.toString());
            certinfo.setCertownername(certownername.toString());
            certinfo.setCertownercerttype(certownercerttype.toString());
        }
        certinfo.setCertownername(FormatUtil.getEncryptSM4ToData(certinfo.getCertownername() , certinfo.getCertcatalogid() , ""));
        certinfo.setCertownerno(FormatUtil.getEncryptSM4ToData(certinfo.getCertownerno() , certinfo.getCertcatalogid() , ""));
        iCertInfo.updateCertInfo(certinfo);

        if (dataBean != null && !dataBean.isEmpty()) {
            // 更新照面信息，先删除，再插入，效率低
            try {
                dataBean.setOperatedate(new Date()); // 更新操作时间
                noSQLSevice.update(dataBean);
            }
            catch (Exception e) {
                e.printStackTrace();
                log.error(String.format("MongoDb更新照面信息 {certinfoguid = %s} 失败!", certinfo.getRowguid()));
                throw new RuntimeException();
            }
        }
    }

    /**
     * 生成证照
     *
     * @param isGenerateCopy 是否生成副本
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public void generateCert(String isGenerateCopy, String ouguids) throws ParseException {
        // 判断目录是否开启
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("msg", catalogMsg);
            return;
        }

        //第一个版本检查编号是否重复
        if(!certinfo.getCertno().equals(getViewData("certno"))){
            boolean existCertno = iCertInfo.isExistCertno(certCatalog.getCertcatalogid(), certinfo.getCertno());
            if (existCertno) {
                addCallbackParam("repeat", "证照编号重复，保存失败！");
                addCallbackParam("isClose", "0");
                return;
            }
        }

        // 子表非空校验
        boolean isSubExtensionValidatePass = true;
        Set<String> subExtensionNameSet = new HashSet<>();
        // 查出所有父元数据（配置子表）
        List<CertMetadata> parentMetadataList = iCertMetaData.selectParentDispinListByCertguid(certCatalog.getRowguid());
        if (ValidateUtil.isNotBlankCollection(parentMetadataList)) {
            for (CertMetadata parentMetadata : parentMetadataList) {
                List<Record> recordList = null;
                String subextensionList = dataBean.getStr(parentMetadata.getFieldname());
                try {
                    if (StringUtil.isNotBlank(subextensionList)) {
                        recordList = JsonUtil.jsonToList(subextensionList, Record.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(String.format("获取子拓展信息失败！JSON转换错误[subextensionList = %s]", subextensionList));
                }
                if (ValidateUtil.isBlankCollection(recordList)) {
                    isSubExtensionValidatePass = false;
                    subExtensionNameSet.add(parentMetadata.getFieldchinesename());
                }
            }
        }

        // 子表校验
        if (!isSubExtensionValidatePass) {
            addCallbackParam("msg", String.format("子表%s不能为空，生成证照失败！", subExtensionNameSet.toString()));
            return;
        }

        // 模板文件guid
        String cliengGuid = certCatalog.getTempletcliengguid();
        boolean isCopy = false;
        if (CertConstant.CONSTANT_STR_ONE.equals(isGenerateCopy)) { // 生成副本
            cliengGuid = certCatalog.getCopytempletcliengguid();
            isCopy = true;
            // 删除打印附件（副本）
            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) {
                iAttachService.deleteAttachByAttachGuid(certinfo.getCopyprintdocguid());
            }
            // 清空打印附件guid
            certinfo.setCopyprintdocguid(null);
        } else {
            // 删除打印附件（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) {
                iAttachService.deleteAttachByAttachGuid(certinfo.getPrintdocguid());
            }
            // 清空打印附件guid
            certinfo.setPrintdocguid(null);
        }

        save(ouguids);
        // 获取word域名数组和域值数组
        Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
        // word域名数组
        String[] fieldNames = (String[]) wordMap.get("fieldNames");
        // word域值数组
        Object[] values = (Object[]) wordMap.get("values");
        // 图片map
        Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
        // 子表数据
        DataSet dataSet = (DataSet) wordMap.get("dataSet");
        // 生成正/副本
        Map<String, String> returnMap = generateBizlogic.generateLicenseSupportCopy(cliengGuid, isCopy,
                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), fieldNames,
                values, imageMap, dataSet, certinfo, keyString, certData);
        // 设置提示信息
        addCallbackParam("msg", returnMap.get("msg"));
    }

    /**
     * 打印证照
     * @param isPrintCopy 是否打印副本
     */
    public void printCert(String isPrintCopy) {
        // 获取证照信息
        if (certinfo == null || certinfo.isEmpty()) {
            addCallbackParam("msg", "该证照不存在！");
            return;
        }
        // 判断目录是否开启
        String catalogMsg = validateCatalog();
        if (StringUtil.isNotBlank(catalogMsg)) {
            addCallbackParam("msg", catalogMsg);
            return;
        }
        // 添加提示信息
        if (copyTempletCount == 0) { // 不支持副本打印
            // 判断是否生成证照
            if (StringUtil.isBlank(certinfo.getCertcliengguid())) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
            // 未先生成证照，提示
            boolean isGenerate = false;
            for (FrameAttachInfo attchInfo : attachInfoList) {
                if ("系统生成".equals(attchInfo.getCliengInfo())) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate) {
                addCallbackParam("msg", "请先生成证照");
                return;
            }
        } else { // 支持副本打印
            String cliengGuid = certinfo.getCertcliengguid();
            String cliengInfo = "系统生成";
            String tip = "正本";
            if (CertConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) { // 打印副本
                cliengGuid = certinfo.getCopycertcliengguid();
                tip = "副本";
                cliengInfo = "系统生成（副本）";
            }
            if (StringUtil.isBlank(cliengGuid)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
            List<FrameAttachInfo> attachInfoList = iAttachService.getAttachInfoListByGuid(cliengGuid);
            if (ValidateUtil.isBlankCollection(attachInfoList)) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
            // 未先生成证照，提示
            boolean isGenerate = false;
            for (FrameAttachInfo attchInfo : attachInfoList) {
                if (cliengInfo.equals(attchInfo.getCliengInfo())) {
                    isGenerate = true;
                    break;
                }
            }
            if (!isGenerate) {
                addCallbackParam("msg", String.format("请先生成证照（%s）", tip));
                return;
            }
        }

        // 打印正/副本
        Map<String, String> returnMap = null;
        if (CertConstant.CONSTANT_STR_ONE.equals(isPrintCopy)) { // 打印副本
            // 是否打印过（副本）
            if (StringUtil.isNotBlank(certinfo.getCopyprintdocguid())) { // 已打印
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getCopyprintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getCopyprintdocguid());
                if(info != null && StringUtil.isNotBlank(info.getContentType())){
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }
            // 没打印，打印(判断是否有套打模版副本附件)
            //先默认cliengGuid为副本模版
            String cliengGuid=certCatalog.getCopytempletcliengguid();
            //套打模版（副本）cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdCopytempletcliengguid())) {
                //查询附件数量
                int TdCopyTempletCount = iAttachService
                        .getAttachCountByClientGuid(certCatalog.getTdCopytempletcliengguid());
                if (TdCopyTempletCount > 0) {
                    cliengGuid = certCatalog.getTdCopytempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, true);

        } else { // 打印正本
            // 是否打印过（正本）
            if (StringUtil.isNotBlank(certinfo.getPrintdocguid())) { // 已打印
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", certinfo.getPrintdocguid());
                FrameAttachInfo info = iAttachService.getAttachInfoDetail(certinfo.getPrintdocguid());
                if(info != null && StringUtil.isNotBlank(info.getContentType())){
                    addCallbackParam("contenttype", info.getContentType());
                }
                addCallbackParam("filename", certinfo.getCertname());
                return;
            }
            // 没打印，打印(判段是否有套打模版正本附件)
            //先默认cliengGuid为正本模版
            String cliengGuid = certCatalog.getTempletcliengguid();
            //套打模版正本cliengguid不为空
            if (StringUtil.isNotBlank(certCatalog.getTdTempletcliengguid())) {
                //查询附件数量
                int TdTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getTdTempletcliengguid());
                if (TdTempletCount > 0) {
                    cliengGuid = certCatalog.getTdTempletcliengguid();
                }
            }
            returnMap = generateCertDoc(cliengGuid, false);
        }

        if (ValidateUtil.isNotBlankMap(returnMap)) {
            String isSuccess = returnMap.get("issuccess");
            if (CertConstant.CONSTANT_STR_ONE.equals(isSuccess)) { // 打印成功
                addCallbackParam("certinfoguid", certinfo.getRowguid());
                addCallbackParam("attachguid", returnMap.get("attachguid"));
                addCallbackParam("filename", certinfo.getCertname());
                addCallbackParam("contenttype", returnMap.get("contenttype"));
            } else { // 打印失败
                addCallbackParam("msg", returnMap.get("msg"));
            }
        }
    }

    /**
     * 打印证照doc
     * @param cliengGuid 证照模板文件（正/副本）guid
     * @param isPrintCopy 是否打印副本
     * @return 返回Map issuccess（1 打印成功, 0 打印失败）
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> generateCertDoc(String cliengGuid, boolean isPrintCopy) {
        Map<String, String> returnMap = null;
        try {
            EpointFrameDsManager.begin(null);
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, dataBean);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            String certClass = ConfigUtil.getConfigValue("cert_reflectgenratecert");
            if (StringUtil.isNotBlank(certClass) && ReflectUtil.exist(certClass)) {
            	JNGenerateCert generateBizlogic = (JNGenerateCert) ReflectUtil.getObjByClassName(certClass);
            	returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
            			userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);

            }else {
            	returnMap = generateBizlogic.generatePrintDocSupportCopy(cliengGuid, isPrintCopy, userSession.getUserGuid(),
            			userSession.getDisplayName(), fieldNames, values, imageMap, dataSet, certinfo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.commit();
            EpointFrameDsManager.close();
        }
        return returnMap;
    }

    /**
     * 验证证照目录
     *  @return
     */
    private String validateCatalog() {
        String msg = "";
        if (certCatalog == null) {
            msg = "当前证照对应的证照类型不存在，操作失败！";
        } else if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())) {
            msg = "当前证照对应的证照类型未启用，操作失败！";
        }
        return msg;
    }

    public void getOFDGuid(){
        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
        for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
            if(StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd") && "系统生成".equals(frameAttachInfo.getCliengInfo())){
                addCallbackParam("attachguid", frameAttachInfo.getAttachGuid());
            }
        }
        List<FrameAttachInfo> copyFrameAttachInfos = iAttachService.getAttachInfoListByGuid(certinfo.getCopycertcliengguid());
        for (FrameAttachInfo frameAttachInfo : copyFrameAttachInfos) {
            if (StringUtil.toLowerCase(frameAttachInfo.getAttachFileName()).endsWith("ofd")
                    && "系统生成（副本）".equals(frameAttachInfo.getCliengInfo())) {
                addCallbackParam("copyattachguid", frameAttachInfo.getAttachGuid());
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCertLevelModel() {
        if (certLevelModel == null) {
            certLevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "可信等级", null, false));
        }
        return this.certLevelModel;
    }

    /**
     * 证照附件上传
     *
     * @return
     */
    public FileUploadModel9 getCertFileUploadModel() {
        if (certFileUploadModel == null && certinfo != null && StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
            AttachHandler9 handler = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object arg0) {
                    if (!CertConstant.CONSTANT_INT_ONE.equals(certinfo.getIscreatecert())) {
                        //上传以后更新是否生成证照字段为1
                        certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                        iCertInfo.updateCertInfo(certinfo);
                    }
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }
            };
            certFileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(certinfo.getCertcliengguid(), CertConstant.CERT_SYS_FLAG, null,
                            handler, userSession.getUserGuid(), userSession.getDisplayName()))
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void deleteAttach(String attachGuid) {
                    FrameAttachInfo frameAttachInfo = iAttachService.getAttachInfoDetail(attachGuid);
                    if("系统生成".equals(frameAttachInfo.getCliengInfo()) && StringUtil.isNotBlank(certinfo.getSltimagecliengguid())){
                      //如果删除生成的附件，同步删除缩略图
                        iAttachService.deleteAttachByGuid(certinfo.getSltimagecliengguid());
                    }
                    iAttachService.deleteAttachByAttachGuid(attachGuid);
                    if (iAttachService.getAttachCountByClientGuid(certinfo.getCertcliengguid()) == 0) {
                        //删除以后更新是否生成证照字段为0
                        certinfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
                        iCertInfo.updateCertInfo(certinfo);
                    }
                }

            };
            certFileUploadModel.setCustomerFilePath(new ConfigBizlogic().getCustomerFilePath());
            certFileUploadModel.setUploadType("file");
        }
        return certFileUploadModel;
    }

    /**
     * 基本信息附件（副本）上传
     *
     * @return
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (copyCertFileUploadModel == null && certinfo != null && StringUtil.isNotBlank(certinfo.getCopycertcliengguid())) {
            copyCertFileUploadModel = new AttachBizlogic().miniOaUploadAttach(certinfo.getCopycertcliengguid(), null);
        }
        return copyCertFileUploadModel;
    }

    /**
     *  照面信息附件上传(返回不同的model)
     *  @param fieldName
     *  @return
     */
    public FileUploadModel9 getFileUploadModel(String fieldName) {
        FileUploadModel9 uploadModel9 = null;
        if (dataBean != null && StringUtil.isNotBlank(fieldName)) {
            String cliengguid = dataBean.getStr(fieldName);
            if (StringUtil.isNotBlank(cliengguid)) {
                if (modelMap.containsKey(cliengguid)) {
                    uploadModel9 = modelMap.get(cliengguid);
                } else {
                    uploadModel9 = new AttachBizlogic().miniOaUploadAttach(cliengguid, null);
                    modelMap.put(cliengguid, uploadModel9);
                }
            }
        }
        return uploadModel9;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCertOwnerCertTypeModel() {
        if (certOwnerCertTypeModel == null) {
            certOwnerCertTypeModel = DataUtil.convertMap2ComboBox(
                (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "申请人用来唯一标识的证照类型", null, false));
            // 自然人去除组织机构代码证、统一社会信用代码
            if ("001".equals(certinfo.getCertownertype())) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if (selectItem.getValue().toString().startsWith("1")) {
                        return true;
                    } else {
                        return false;
                    }
                });
            // 法人保留组织机构代码证、统一社会信用代码
            } else if ("002".equals(certinfo.getCertownertype())) {
                certOwnerCertTypeModel.removeIf(selectItem -> {
                    if (selectItem.getValue().toString().startsWith("2")) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }
        return this.certOwnerCertTypeModel;
    }

    public void getExpireDate(String expiredate){
        if(StringUtil.isNotBlank(expiredate)){
            String[] separators = configService.getFrameConfigValue("AS_DATE_SEPARATOR").split(";");
            for(String separator : separators){
                if(expiredate.contains(separator)){
                    String[] date = expiredate.split(separator);
                    if(date.length > 1){
                        addCallbackParam("expiredatefrom" , date[0]);
                        addCallbackParam("expiredateto" , date[1]);
                    }
                }
            }
        }
    }

    public CertInfo getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(CertInfo certinfo) {
        this.certinfo = certinfo;
    }

    public CertInfoExtension getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertInfoExtension dataBean) {
        this.dataBean = dataBean;
    }

    public String getExtendguid() {
        return extendguid;
    }

    public void setExtendguid(String extendguid) {
        this.extendguid = extendguid;
    }

    public String getCertownerinfo() {
        return certownerinfo;
    }

    public void setCertownerinfo(String certownerinfo) {
        this.certownerinfo = certownerinfo;
    }

    public String getCertData() {
        return certData;
    }

    public void setCertData(String certData) {
        this.certData = certData;
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }
}
