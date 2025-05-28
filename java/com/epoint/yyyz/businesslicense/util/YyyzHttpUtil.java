package com.epoint.yyyz.businesslicense.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.atmosphere.util.StringEscapeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;

/**
 * @作者 zhaoy
 * @version [版本号, 2019年1月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class YyyzHttpUtil
{
    transient static Logger log = LogUtil.getLog(YyyzHttpUtil.class);

    /**
     * 回传证照信息接口
     * @description
     * @author shibin
     * @date  2020年6月10日 下午4:49:33
     */
    public static JSONObject sendInfoToLC(String biguid, String certrowguid) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        //    	IBusinessLicenseResult businessLicenseResult = ContainerFactory.getContainInfo().getComponent(IBusinessLicenseResult.class);
        IHandleConfig handleConfigService = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        ICertCatalog iCertCatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
        ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        IBusinessLicenseBaseinfo baseinfoService = ContainerFactory.getContainInfo()
                .getComponent(IBusinessLicenseBaseinfo.class);

        NoSQLSevice noSQLSevice = new NoSQLSevice();
        JSONObject rtnjson = new JSONObject();
        JSONObject paramjson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        JSONArray data = new JSONArray();
        String msg = "";

        String centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        String catalogid = handleConfigService.getFrameConfig("AS_CENTER_CERT", centerGuid).getResult();
        if (StringUtil.isBlank(catalogid)) {
            catalogid = configService.getFrameConfigValue("AS_CENTER_CERT");
        }
        String AS_YYYZ_LC_USENAME = configService.getFrameConfigValue("AS_YYYZ_LC_USENAME");
        String yyyzqrcode = configService.getFrameConfigValue("yyyzqrcode");
        String domainNameDowload = configService.getFrameConfigValue("domainNameDowload");

        String serialNo = "";
        BusinessLicenseBaseinfo baseinfo = null;
        if (StringUtil.isNotBlank(biguid)) {
            baseinfo = baseinfoService.getBaseinfoByBiguid(biguid);
            if (baseinfo != null) {
                serialNo = baseinfo.getSerialNo();
            }
        }
        else {
            rtnjson.put("status", 0);
            rtnjson.put("massage", "biguid为空！");
            return rtnjson;
        }
        if (StringUtil.isNotBlank(serialNo)) {
            paramjson.put("serialNo", serialNo);
        }
        else {
            paramjson.put("serialNo", certrowguid.replace("-", ""));
        }

        if (StringUtil.isNotBlank(catalogid)) {
            CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByEnale(catalogid,
                    CertConstant.CONSTANT_INT_ZERO);
            //    		ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
            if (certCatalog == null) {
                msg = "该证照对应的证照类型不存在！";
            }
            else if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())) {
                msg = "该证照对应的证照类型未启用！";
            }
            else {
                JSONObject itemjson = new JSONObject();
                JSONObject certInfo = new JSONObject();
                if (StringUtil.isNotBlank(AS_YYYZ_LC_USENAME)) {
                    itemjson.put("item", certCatalog.getCertname());
                }
                else {
                    itemjson.put("item", certCatalog.getStr("yyyzcertcode"));//YYYZ_HYZH_XK
                }

                CertInfo info = iCertInfo.getCertInfoByRowguid(certrowguid);

                FrameAttachInfo frameAttachInfo = null;
                if (StringUtil.isNotBlank(info.getSltimagecliengguid())) {
                    frameAttachInfo = iAttachService.getAttachInfoListByGuid(info.getSltimagecliengguid()).get(0);
                }
                // 获得照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certrowguid);
                CertInfoExtension dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
                if (dataBean != null) {
                    for (Entry<String, Object> entry : dataBean.entrySet()) {
                        log.info("【照面信息数据输出】k:v=" + entry.getKey() + ":" + entry.getValue());
                        switch (entry.getKey()) {
                            case "operateusername":
                                break;
                            case "rowguid":
                                break;
                            case "zzn":
                                break;
                            case "zzy":
                                break;
                            case "zzr":
                                break;
                            case "operatedate":
                                break;
                            case "issuedate":
                                break;
                            case "certinfoguid":
                                certInfo.put("certId", entry.getValue().toString().replace("-", ""));
                                break;
                            case "jyzmc":
                                certInfo.put("entName", entry.getValue());
                                break;
                            case "tyxy":
                                certInfo.put("socialCode", entry.getValue());
                                break;
                            case "xkxm":
                                certInfo.put("jyxm", entry.getValue());
                                break;
                            case "fzjg":
                                certInfo.put("issueDept", entry.getValue());
                                break;
                            case "socialcode":
                                certInfo.put("socialCode", entry.getValue());
                                break;
                            case "issuedept":
                                certInfo.put("issueDept", entry.getValue());
                                break;
                            case "entname":
                                certInfo.put("entName", entry.getValue());
                                break;
                            default:
                                certInfo.put(entry.getKey(), entry.getValue());
                                break;
                        }
                    }

                    certInfo.put("qrCodeUrl", yyyzqrcode + "?certguid=" + certrowguid);//二维码URL
                    //电子证照路径
                    if (StringUtil.isNotBlank(frameAttachInfo.getAttachGuid())) {
                        certInfo.put("electUrl", domainNameDowload + frameAttachInfo.getAttachGuid());
                    }
                    else {
                        certInfo.put("electUrl", "");
                    }

                    certInfo.put("certNo", info.getCertno());
                    certInfo.put("issueDate",
                            EpointDateUtil.convertDate2String(info.getAwarddate(), EpointDateUtil.DATE_FORMAT));
                    log.info(certInfo);
                    itemjson.put("certInfo", certInfo);
                    data.add(itemjson);
                }
                else {
                    msg = "未获取到证照照面信息！certrowguid：" + certrowguid;
                }
            }
            paramjson.put("data", data);
        }
        else {
            rtnjson.put("status", 0);
            rtnjson.put("massage", msg);
            return rtnjson;
        }

        log.info("====paramjson====" + paramjson);
        String appKey = configService.getFrameConfigValue("AS_YYYZ_AppKey");
        String encryptKey = configService.getFrameConfigValue("AS_YYYZ_EncryptKey");
        String tokenUrl = configService.getFrameConfigValue("AS_YYYZ_TokenUrl");

        // 获取token
        String token;
        String tokenJson = SafetyUtil.doHttpPost(tokenUrl, appKey);
        JSONObject tokenObject = JSONObject.parseObject(tokenJson);
        String code = tokenObject.getString("code");
        if ("0".equals(code)) {
            return tokenObject;
        }
        else {
            token = tokenObject.getString("token");
        }
        // AES对data数据加密
        String dataAES = AESUtil.aesEncode(paramjson.toJSONString(), encryptKey);
        jsonObject.put("token", token);
        jsonObject.put("appKey", appKey);
        jsonObject.put("data", dataAES);

        log.info("====object====" + jsonObject.toString());
        String url = configService.getFrameConfigValue("AS_YYYZ_LCURL_CREATE");
        String rtnstr = null;
        try {
            // 设置参数
            Map<String, String> map = new HashMap<>(16);
            map.put("param", jsonObject.toString());
            Map<String, Object> params = new HashMap<>();
            params.putAll(map);
            rtnstr = HttpUtil.doPost(url, params);
            rtnstr = StringEscapeUtils.unescapeJava(rtnstr);
            rtnstr = rtnstr.substring(1, rtnstr.length() - 1);
            log.info("====rtnstr====" + rtnstr);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        if (StringUtil.isNotBlank(rtnstr)) {
            rtnjson = JSON.parseObject(rtnstr);
        }

        //{"massage":"success","status":"1"}
        return rtnjson;
    }

    /**
     * 传递办件状态
     * @description
     * @author shibin
     * @date  2020年6月10日 下午4:50:01
     */
    public static JSONObject updateStatusBy(String serialNo, String state, String item) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String url = configService.getFrameConfigValue("AS_YYYZ_LCURL_UPDATE");
        JSONObject rtnjson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject paramjson = new JSONObject();

        paramjson.put("serialNo", serialNo);
        paramjson.put("state", state);
        paramjson.put("item", item);
        //办理状态，02：受理，03：许可决定，04：补齐补正，05：补齐补正提交，06：补齐补正不通过，07：办结

        jsonArray.add(paramjson);
        String appKey = configService.getFrameConfigValue("AS_YYYZ_AppKey");
        String encryptKey = configService.getFrameConfigValue("AS_YYYZ_EncryptKey");
        String tokenUrl = configService.getFrameConfigValue("AS_YYYZ_TokenUrl");

        // 获取token
        String token;
        String tokenJson = SafetyUtil.doHttpPost(tokenUrl, appKey);
        JSONObject tokenObject = JSONObject.parseObject(tokenJson);
        String code = tokenObject.getString("code");
        if ("0".equals(code)) {
            return tokenObject;
        }
        else {
            token = tokenObject.getString("token");
        }
        // AES对data数据加密
        String dataAES = AESUtil.aesEncode(jsonArray.toJSONString(), encryptKey);
        jsonObject.put("token", token);
        jsonObject.put("appKey", appKey);
        jsonObject.put("data", dataAES);

        log.info("====object====" + jsonObject.toString());
        String rtnstr = null;
        try {
            // 设置参数
            Map<String, String> map = new HashMap<>(16);
            map.put("data", jsonObject.toString());
            Map<String, Object> params = new HashMap<>();
            params.putAll(map);
            rtnstr = HttpUtil.doPost(url, params);
            rtnstr = StringEscapeUtils.unescapeJava(rtnstr);
            rtnstr = rtnstr.substring(1, rtnstr.length() - 1);
            log.info("====rtnstr====" + rtnstr);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        if (StringUtil.isNotBlank(rtnstr)) {
            rtnjson = JSON.parseObject(rtnstr);
        }

        ////{"massage":"success","status":"1"}
        return rtnjson;
    }

}
