package com.epoint.cert.basic.certinfo;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
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
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.cert.util.PushUtil;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 *  证照信息状态提交页面对应后台
 * @作者 dingwei
 * @version [版本号, 2018年1月16日]
 */
@RestController("jncertinfostatussubmitaction")
@Scope("request")
public class JnCertinfoStatusSubmitAction extends BaseController {
    private static final long serialVersionUID = 4618487476387326334L;

    /**
     * 证照实例
     */
    private CertInfo certInfo;

    /**
     * 证照实例api
     */
    @Autowired
    private ICertInfo iCertInfo;

    /**
     * 证照类型api
     */
    @Autowired
    private ICertCatalog iCertCatalog;


    /**
     * 多持有人api
     */
    @Autowired
    private ICertOwnerInfo ownerInfoService;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 证照日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 证照信息bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * 附件bizlogic
     */
    private AttachBizlogic attachBizlogic = new AttachBizlogic();

    /**
     * 证照实例唯一标识
     */
    private String guid;

    /**
     * 操作类型
     */
    private String opt;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        opt = getRequestParameter("opt");
        if (StringUtil.isNotBlank(guid)) {
            certInfo = iCertInfo.getCertInfoByRowguid(guid);
        }

        if (certInfo == null) {
            certInfo = new CertInfo();
        }
        //将多持有者中^符号更换为,显示,无需逻辑判断
        certInfo.setCertownername(certInfo.getCertownername().replace("^", ","));
        certInfo.setCertownerno(certInfo.getCertownerno().replace("^", ","));
    }
    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }
    /**
     *  提交
     */
    public void submit() {
        CertCatalog certcatalog = iCertCatalog.getLatestCatalogDetailByEnale(certInfo.getCertcatalogid() , CertConstant.CONSTANT_INT_ZERO);
        if(certcatalog == null){
            addCallbackParam("disenable" , "该证照对应的证照类型不存在！");
            return;
        } else if (CertConstant.CONSTANT_INT_ZERO.equals(certcatalog.getIsenable())) {
            addCallbackParam("disenable" , "当前证照对应的证照类型未启用，操作失败！");
            return;
        }
        if (!certInfo.isEmpty() && StringUtil.isNotBlank(opt)) {
            certInfo.setCertownername(certInfo.getCertownername().replace(",", "^"));
            certInfo.setCertownerno(certInfo.getCertownerno().replace(",", "^"));
            // 日志操作类型
            String logOperate = "";
            // 回传提示消息
            String msg = "";
            if (CertConstant.CERT_OPT_CANCEL.equals(opt)) { // 注销

                //对特殊证照处理
                // 竣工验收证书
                if("房屋建筑和市政基础设施工程竣工验收备案(申请赋码)".equals(certInfo.getCertname())){
                    String fmCode = certInfo.get("ecertid");
                    Map<String, Object> filter = new HashMap<>();
                    // 设置基本信息guid
                    filter.put("certinfoguid", certInfo.getRowguid());
                    CertInfoExtension certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                    String JGYSBABH = certinfoExtension.get("bh");
                    String cancelReason = StringUtils.isNotBlank(certInfo.getRemark()) ? certInfo.getRemark() :"空";
                    String cancelDate = EpointDateUtil.convertDate2String(new Date(),"yyyy-MM-dd");
                    String auditDepartment = userSession.getOuName();
                    String auditPeople = userSession.getDisplayName();
                    String certStatus = "3";
                    if(StringUtils.isNotBlank(fmCode) && StringUtils.isNotBlank(JGYSBABH)) {
                        JSONObject gjationJson = new JSONObject();
                        gjationJson.put("fmCode", fmCode);
                        gjationJson.put("cancelReason", cancelReason);
                        gjationJson.put("cancelDate", cancelDate);
                        gjationJson.put("auditDepartment", auditDepartment);
                        gjationJson.put("auditPeople", auditPeople);
                        gjationJson.put("certStatus", certStatus);
                        gjationJson.put("JGYSBABH", JGYSBABH);
                        JSONObject returnJson = PushUtil.jgyscercancel(gjationJson, certInfo.getAreacode());
                        if(returnJson != null) {
                            boolean flag = returnJson.getBoolean("sucess");
                            if(!flag) {
                                log.info("errormsg:"+returnJson.getString("msg"));
                                certInfo.set("zxerror",returnJson.getString("msg"));
                                iCertInfo.updateCertInfo(certInfo);
                                addCallbackParam("disenable" , "注销失败");
                                return;
                            }
                        }
                    }else{
                        addCallbackParam("disenable" , "注销失败,证照缺少部分信息");
                        return;
                    }
                }
                
                CertInfo newCertInfo = certInfo.toEntity(CertInfo.class);
                certInfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                newCertInfo.setCertownername(certInfo.getCertownername().replace(",", "^"));
                newCertInfo.setCertownerno(certInfo.getCertownerno().replace(",", "^"));
                newCertInfo.setRowguid(UUID.randomUUID().toString());
                newCertInfo.setOperateusername(userSession.getDisplayName());
                newCertInfo.setOperatedate(new Date());
                newCertInfo.setIspublish(0);
                newCertInfo.setVersion(certInfo.getVersion() + 1);
                newCertInfo.setVersiondate(new Date());
                newCertInfo.setCertificateidentifier(certInfoBizlogic.generateIdentifier(newCertInfo, newCertInfo.getCertificatetypecode()));
                // 状态为注销
                newCertInfo.setStatus(CertConstant.CERT_STATUS_CANCEL);
                newCertInfo.setLicdata_sync_sign(CertConstant.CONSTANT_STR_ZERO);
                //在新的证照中替换
                newCertInfo.setCertownername(certInfo.getCertownername().replace(",", "^"));
                newCertInfo.setCertownerno(certInfo.getCertownerno().replace(",", "^"));
                //多持有人复制
                List<CertOwnerInfo> ownerInfos = ownerInfoService.selectOwnerInfoByCertInfoGuid(certInfo.getRowguid());
                for (CertOwnerInfo ownerInfo : ownerInfos) {
                    ownerInfo.setRowguid(UUID.randomUUID().toString());
                    ownerInfo.setCertinfoguid(newCertInfo.getRowguid());
                    ownerInfoService.addCertOwnerInfo(ownerInfo);
                }
                // 拷贝照面信息
                try {
                    Map<String, Object> filter = new HashMap<>();
                    filter.put("certinfoguid", certInfo.getRowguid());
                    CertInfoExtension certExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
                    if (certExtension != null) {
                        // 设置新的certinfoguid
                        certExtension.setCertinfoguid(newCertInfo.getRowguid());
                        certExtension.setRowguid(UUID.randomUUID().toString());
                        // 设置操作时间
                        certExtension.setOperatedate(new Date());
                        certExtension.setOperateusername(userSession.getDisplayName());
                        // 拷贝照面信息附件
                        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(newCertInfo.getCertareaguid());
                        if (ValidateUtil.isNotBlankCollection(metadataList)) {
                            for (CertMetadata metadata : metadataList) {
                                if ("image".equals(StringUtil.toLowerCase(metadata.getFieldtype()))) {
                                    String cliengguid = certExtension.getStr(metadata.getFieldname());
                                    if (StringUtil.isNotBlank(cliengguid)) {
                                        certExtension.set(metadata.getFieldname(), UUID.randomUUID().toString());
                                        attachBizlogic.copyAttach(cliengguid, certExtension.getStr(metadata.getFieldname()));
                                    }
                                }
                            }
                        }
                        noSQLSevice.insert(certExtension);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }

                
                iCertInfo.addCertInfo(newCertInfo);
                

                
                logOperate = CertConstant.LOG_OPERATETYPE_CANCEL;
                msg = "注销成功！";
            } else if (CertConstant.CERT_OPT_LOSE.equals(opt)) { // 挂失
                // 状态为挂失
                certInfo.setStatus(CertConstant.CERT_STATUS_LOSE);
                logOperate = CertConstant.LOG_OPERATETYPE_LOSE;
                msg = "挂失成功！";
            }
            if (StringUtil.isNotBlank(logOperate)) {
                certInfo.setOperatedate(new Date());
                certInfo.setOperateusername(userSession.getDisplayName());
                // 更新CertInfo
                iCertInfo.updateCertInfo(certInfo);
                // 添加日志
                String note = userSession.getDisplayName() + "在" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss")
                    + "时对《" + certInfo.getCertname() + "》进行了" + logOperate + "， " + logOperate + "备注：" + certInfo.getRemark();
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certInfo,
                        logOperate, note, null);
                addCallbackParam("msg", msg);
            }
        }
    }

    public CertInfo getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(CertInfo certInfo) {
        this.certInfo = certInfo;
    }
}
