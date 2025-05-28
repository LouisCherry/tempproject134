package com.epoint.cert.basic.certbasic;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.common.api.ICertIndividual;
import com.epoint.cert.commonutils.AttachUtil;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.JsonUtils;
import com.epoint.cert.commonutils.RecordUtil;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;

/**
 * 证照信息接口
 *
 * @作者 dingwei
 * @version [版本号, 2017年11月17日]
 */
@RestController
@RequestMapping("/tacertInfo")
public class TACertInfoController
{

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 证照信息api
     */
    @Autowired
    private ICertInfo iCertInfo;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 附件api
     */
    @Autowired
    private IAttachService attachService;

    /**
     * 授权部门目录api
     */
    @Autowired
    private ICertCatalogOu iCertCatalogOu;

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取证照基本信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertInfoDetail", method = RequestMethod.POST)
    public String getCertInfoDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getCertInfoDetail接口=======");
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            // appkey是否存在
            String appkey = json.getString("appkey");
            if (StringUtil.isBlank(appkey)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
            }
            // 证照编号
            String certno = obj.getString("certno");
            if (StringUtil.isBlank(certno)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照编号！", "");
            }
            // 证照目录唯一版本标识
            String certcatalogid = obj.getString("certcatalogid");
            // 证照目录编码
            String tycertcatcode = obj.getString("tycertcatcode");
            if (StringUtil.isBlank(certcatalogid) && StringUtil.isBlank(tycertcatcode)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照目录唯一版本标识或者证照目录编码！", "");
            }
            CertCatalogOu catalogOu = null;
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 有读权限
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatalogid("readauthority, ouname", true, appkey,
                        certcatalogid);
            }
            else {
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatcode("readauthority, ouname", true, appkey,
                        tycertcatcode);
            }
            if (catalogOu == null || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getReadauthority())) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "该appkey没有读权限，请联系管理员！", "");
            }

            List<CertInfo> certInfoList = null;
            CertCatalog certCatalog = null;
            if(StringUtil.isNotBlank(certcatalogid)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
            }else if(StringUtil.isNotBlank(tycertcatcode)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCertcatcode("*" , tycertcatcode);
            }
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
                certInfoList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                        certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            }
            else {
                // 条件查询
                SqlUtils sqlUtils = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils.setSelectFields(
                        "rowguid, certid, certcliengguid,copycertcliengguid, certno, certlevel, certawarddept, certownername, certownercerttype, certownerno, awarddate, expiredatefrom, expiredateto, certname, version, versiondate ");
                certInfoList = iCertInfo.getListByCondition(sqlUtils.getMap());
            }

            CertInfo certInfo = new CertInfo();
            if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                certInfo = certInfoList.get(0);
                // 日期格式转换
                RecordUtil.convertDate2String(certInfo);
                // 增加操作日志
                new CertLogBizlogic().addExternalLog(appkey, catalogOu.getOuname(), certInfo,
                        CertConstant.LOG_OPERATETYPE_QUERY, "getCertInfoDetail", "", null);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("certInfo", certInfo);

            log.info("=======结束调用getCertInfoDetail接口=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照信息成功！", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoDetail接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }
    
    /**
     * 获取证照照面信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertInfoExtensionJzjn", method = RequestMethod.POST)
    public String getCertInfoExtensionJzjn(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertInfoExtensionJzjn接口=======");
			JSONObject json = JSON.parseObject(params);
			log.info("getCertInfoExtension接口入参" + json);
			JSONObject obj = (JSONObject) json.get("params");
			// appkey是否存在
			String appkey = json.getString("appkey");
			if (StringUtil.isBlank(appkey)) {
				return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
			}
			// 证照编号
			String zsbh = obj.getString("zsbh");
			if (StringUtil.isBlank(zsbh)) {
				return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证书编号！", "");
			}

			CertInfoExtension certinfoExtension = null;
			// 获取照面信息
			Map<String, Object> filter = new HashMap<>();
			// 设置基本信息guid
			filter.put("zsbh", zsbh);
			certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
			log.info("jzjn查询证照拓展信息：" + certinfoExtension);
			if (certinfoExtension == null) {
				certinfoExtension = new CertInfoExtension();
			}
			// 日期格式转换
			RecordUtil.convertDate2String(certinfoExtension);

			JSONObject dataJson = new JSONObject();
			dataJson.put("certinfoextension", certinfoExtension);

			log.info("=======结束调用getCertInfoExtensionJzjn接口=======");
			return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照照面信息成功！", dataJson);
		}
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoExtensionJzjn接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoExtensionJzjn异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }   
    
    
    /**
     * 获取证照照面信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertInfoExtensionCertid", method = RequestMethod.POST)
    public String getCertInfoExtensionCertid(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertInfoExtensionCertid接口=======");
			JSONObject json = JSON.parseObject(params);
			log.info("getCertInfoExtensionCertid接口入参" + json);
			JSONObject obj = (JSONObject) json.get("params");
			// appkey是否存在
			String appkey = json.getString("appkey");
			if (StringUtil.isBlank(appkey)) {
				return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
			}
			// 证照编号
			String certinfoguid = obj.getString("certinfoguid");
			if (StringUtil.isBlank(certinfoguid)) {
				return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证书编号！", "");
			}

			CertInfoExtension certinfoExtension = null;
			// 获取照面信息
			Map<String, Object> filter = new HashMap<>();
			// 设置基本信息guid
			filter.put("certinfoguid", certinfoguid);
			certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
			log.info("jzjn查询证照拓展信息：" + certinfoExtension);
			if (certinfoExtension == null) {
				certinfoExtension = new CertInfoExtension();
			}
			// 日期格式转换
			RecordUtil.convertDate2String(certinfoExtension);

			JSONObject dataJson = new JSONObject();
			dataJson.put("certinfoextension", certinfoExtension);

			log.info("=======结束调用getCertInfoExtensionCertid接口=======");
			return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照照面信息成功！", dataJson);
		}
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoExtensionCertid接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoExtensionCertid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }
    
    
    /**
     * 获取证照照面信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertInfoExtensionSgxkz", method = RequestMethod.POST)
    public String getCertInfoExtensionSgxkz(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertInfoExtensionSgxkz接口=======");
			JSONObject json = JSON.parseObject(params);
			log.info("getCertInfoExtensionCertid接口入参" + json);
			JSONObject obj = (JSONObject) json.get("params");
			List<CertInfoExtension> certinfoExtensions = null;
			// 证照编号
			String certinfoguid = obj.getString("certinfoguid");
			String xmmc = obj.getString("xmmc");
			String certno = obj.getString("certno");
			
			Map<String, Object> filter = new HashMap<>();
			
			if (StringUtil.isNotBlank(certinfoguid)) {
				filter.put("certinfoguid", certinfoguid);
			}
			
			if (StringUtil.isNotBlank(xmmc)) {
				filter.put("xiangmumingchen", xmmc);
			}
			
			if (StringUtil.isNotBlank(certno)) {
				filter.put("bh", certno);
			}
			
			certinfoExtensions = getMongodbUtil().findList(CertInfoExtension.class, filter, null, false);

			if (certinfoExtensions == null) {
				certinfoExtensions = new ArrayList<CertInfoExtension>();
			}
			
			for (CertInfoExtension certinfoExtension : certinfoExtensions) {
				// 日期格式转换
				RecordUtil.convertDate2String(certinfoExtension);
			}

			JSONObject dataJson = new JSONObject();
			dataJson.put("certinfoextensions", certinfoExtensions);

			log.info("=======结束调用getCertInfoExtensionSgxkz接口=======");
			return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照照面信息成功！", dataJson);
		}
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoExtensionSgxkz接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoExtensionSgxkz异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }
    
    @RequestMapping(value = "/getCertInfoExtensionGhxkz", method = RequestMethod.POST)
    public String getCertInfoExtensionGhxkz(@RequestBody String params) {
		try {
			log.info("=======开始调用getCertInfoExtensionGhxkz接口=======");
			JSONObject json = JSON.parseObject(params);
			log.info("getCertInfoExtensionCertid接口入参" + json);
			JSONObject obj = (JSONObject) json.get("params");
			CertInfoExtension certinfoExtension = null;
			// 证照编号
			String certinfoguid = obj.getString("certinfoguid");
			String xmmc = obj.getString("xmmc");
			String certno = obj.getString("certno");
			Map<String, Object> filter = new HashMap<>();
			
			if (StringUtil.isNotBlank(certinfoguid)) {
				filter.put("certinfoguid", certinfoguid);
			}
			
			if (StringUtil.isNotBlank(xmmc)) {
				filter.put("ydxmmc", xmmc);
			}
			
			if (StringUtil.isNotBlank(certno)) {
				filter.put("bianhao", certno);
			}
			
			certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
			log.info("查询证照拓展信息：" + certinfoExtension);
			
			if (certinfoExtension == null) {
				certinfoExtension = new CertInfoExtension();
			}
			// 日期格式转换
			RecordUtil.convertDate2String(certinfoExtension);

			JSONObject dataJson = new JSONObject();
			dataJson.put("certinfoextension", certinfoExtension);

			log.info("=======结束调用getCertInfoExtensionGhxkz接口=======");
			return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照照面信息成功！", dataJson);
		}
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoExtensionGhxkz接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoExtensionGhxkz异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }
    
    
    
    
    
    /**
     * 获取证照照面信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getCertInfoExtension", method = RequestMethod.POST)
    public String getCertInfoExtension(@RequestBody String params) {
        try {
            log.info("=======开始调用getCertInfoExtension接口=======");
            JSONObject json = JSON.parseObject(params);
            log.info("getCertInfoExtension接口入参"+json);
            JSONObject obj = (JSONObject) json.get("params");
            // appkey是否存在
            String appkey = json.getString("appkey");
            if (StringUtil.isBlank(appkey)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
            }
            // 证照编号
            String certno = obj.getString("certno");
            if (StringUtil.isBlank(certno)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照编号！", "");
            }
            // 证照目录唯一版本标识
            String certcatalogid = obj.getString("certcatalogid");
            // 证照目录编码
            String tycertcatcode = obj.getString("tycertcatcode");
            if (StringUtil.isBlank(certcatalogid) && StringUtil.isBlank(tycertcatcode)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照目录唯一版本标识或者证照目录编码！", "");
            }

            List<CertInfo> certInfoList = null;
            CertCatalog certCatalog = null;
            if(StringUtil.isNotBlank(certcatalogid)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
            }else if(StringUtil.isNotBlank(tycertcatcode)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCertcatcode("*" , tycertcatcode);
            }
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
                certInfoList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                        certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            }
            else {
                // 条件查询
                SqlUtils sqlUtils = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                //sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils.setSelectFields(" rowguid ");
                certInfoList = iCertInfo.getListByCondition(sqlUtils.getMap());
            }
            log.info("查询到对应的证照信息："+certInfoList);
            CertInfoExtension certinfoExtension = null;
            if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                CertInfo certInfo = certInfoList.get(0);
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certInfo.getRowguid());
                certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                log.info("查询证照拓展信息："+certinfoExtension);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 日期格式转换
                RecordUtil.convertDate2String(certinfoExtension);
                // 设置照面附件
                AttachBizlogic attachBizlogic = new AttachBizlogic();
                setCertinfoExtenionAttch(certinfoExtension, certInfo, attachBizlogic);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("certinfoextension", certinfoExtension);

            log.info("=======结束调用getCertInfoExtension接口=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取证照照面信息成功！", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCertInfoExtension接口参数：params【" + params + "】=======");
            log.info("=======getCertInfoExtension异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    }   
    
    
    /**
     * 获取建设工程规划许可证（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getJSGCCertInfo", method = RequestMethod.POST)
    public String getJSGCCertInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getJSGCCertInfo接口=======");
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            // appkey是否存在
            String appkey = json.getString("appkey");
            if (StringUtil.isBlank(appkey)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
            }
            // 证照编号
            String certno = obj.getString("certno");
            if (StringUtil.isBlank(certno)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照编号！", "");
            }
            // 证照目录唯一版本标识
            String certcatalogid = obj.getString("certcatalogid");
            // 证照目录编码
            String tycertcatcode = obj.getString("tycertcatcode");
            if (StringUtil.isBlank(certcatalogid) && StringUtil.isBlank(tycertcatcode)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照目录唯一版本标识或者证照目录编码！", "");
            }
            CertCatalogOu catalogOu = null;
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 有读权限
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatalogid("readauthority, ouname", true, appkey,
                        certcatalogid);
            }
            else {
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatcode("readauthority, ouname", true, appkey,
                        tycertcatcode);
            }
            if (catalogOu == null || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getReadauthority())) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "该appkey没有读权限，请联系管理员！", "");
            }

            List<CertInfo> certInfoList = null;
            List<CertInfo> certInfoExList = null;
            CertCatalog certCatalog = null;
            if(StringUtil.isNotBlank(certcatalogid)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
            }else if(StringUtil.isNotBlank(tycertcatcode)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCertcatcode("*" , tycertcatcode);
            }
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
            	certInfoExList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                        certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            	 certInfoList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                         certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            }
            else {
                // 照面信息条件查询
                SqlUtils sqlUtils = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils.setSelectFields(" rowguid ");
                certInfoExList = iCertInfo.getListByCondition(sqlUtils.getMap());
                
                // 基本信息条件查询
                SqlUtils sqlUtils2 = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils2.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils2.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                	sqlUtils2.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils2.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils2.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils2.setSelectFields("certcliengguid,copycertcliengguid");
                certInfoList = iCertInfo.getListByCondition(sqlUtils2.getMap());
            }
            FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
            CertInfo certInfo = new CertInfo();
            String certurl="";
            String copycerturl="";
            if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                certInfo = certInfoList.get(0);
                List<FrameAttachInfo> certfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCertcliengguid());
                List<FrameAttachInfo> copycertfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCopycertcliengguid());
                if(certfilelist!=null&&certfilelist.size()>0){
                	certurl=JsonUtils.getRootUrl(request)
        					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + certfilelist.get(0).getAttachGuid() + "";
                }
                if(copycertfilelist!=null&&copycertfilelist.size()>0){
            	   copycerturl=JsonUtils.getRootUrl(request)
       					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + copycertfilelist.get(0).getAttachGuid() + "";
               }
            }
            
            CertInfoExtension certinfoExtension = null;
            if (ValidateUtil.isNotBlankCollection(certInfoExList)) {
                CertInfo certExInfo = certInfoExList.get(0);
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certExInfo.getRowguid());
                certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 日期格式转换
                RecordUtil.convertDate2String(certinfoExtension);
                // 设置照面附件
                AttachBizlogic attachBizlogic = new AttachBizlogic();
                setCertinfoExtenionAttch(certinfoExtension, certExInfo, attachBizlogic);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("certinfo", certinfoExtension);
            dataJson.put("copycerturl", copycerturl);
            dataJson.put("certurl", certurl);

            log.info("=======结束调用getJSGCCertInfo接口=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取建设工程规划许可证信息成功！", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getJSGCCertInfo接口参数：params【" + params + "】=======");
            log.info("=======getJSGCCertInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    } 
    
    /**
     * 获取建设用地规划许可证（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getJSYDCertInfo", method = RequestMethod.POST)
    public String getJSYDCertInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getJSYDCertInfo接口=======");
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            // appkey是否存在
            String appkey = json.getString("appkey");
            if (StringUtil.isBlank(appkey)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
            }
            // 证照编号
            String certno = obj.getString("certno");
            if (StringUtil.isBlank(certno)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照编号！", "");
            }
            // 证照目录唯一版本标识
            String certcatalogid = obj.getString("certcatalogid");
            // 证照目录编码
            String tycertcatcode = obj.getString("tycertcatcode");
            if (StringUtil.isBlank(certcatalogid) && StringUtil.isBlank(tycertcatcode)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照目录唯一版本标识或者证照目录编码！", "");
            }
            CertCatalogOu catalogOu = null;
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 有读权限
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatalogid("readauthority, ouname", true, appkey,
                        certcatalogid);
            }
            else {
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatcode("readauthority, ouname", true, appkey,
                        tycertcatcode);
            }
            if (catalogOu == null || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getReadauthority())) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "该appkey没有读权限，请联系管理员！", "");
            }

            List<CertInfo> certInfoList = null;
            List<CertInfo> certInfoExList = null;
            CertCatalog certCatalog = null;
            if(StringUtil.isNotBlank(certcatalogid)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
            }else if(StringUtil.isNotBlank(tycertcatcode)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCertcatcode("*" , tycertcatcode);
            }
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
            	certInfoExList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                        certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            	 certInfoList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                         certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            }
            else {
                // 照面信息条件查询
                SqlUtils sqlUtils = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils.setSelectFields(" rowguid ");
                certInfoExList = iCertInfo.getListByCondition(sqlUtils.getMap());
                
                // 基本信息条件查询
                SqlUtils sqlUtils2 = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils2.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils2.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                	sqlUtils2.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils2.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils2.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils2.setSelectFields("certcliengguid,copycertcliengguid");
                certInfoList = iCertInfo.getListByCondition(sqlUtils2.getMap());
            }
            FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
            CertInfo certInfo = new CertInfo();
            String certurl="";
            String copycerturl="";
            if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                certInfo = certInfoList.get(0);
                List<FrameAttachInfo> certfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCertcliengguid());
                List<FrameAttachInfo> copycertfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCopycertcliengguid());
                if(certfilelist!=null&&certfilelist.size()>0){
                	certurl=JsonUtils.getRootUrl(request)
        					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + certfilelist.get(0).getAttachGuid() + "";
                }
                if(copycertfilelist!=null&&copycertfilelist.size()>0){
            	   copycerturl=JsonUtils.getRootUrl(request)
       					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + copycertfilelist.get(0).getAttachGuid() + "";
               }
            }
            
            CertInfoExtension certinfoExtension = null;
            if (ValidateUtil.isNotBlankCollection(certInfoExList)) {
                CertInfo certExInfo = certInfoExList.get(0);
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certExInfo.getRowguid());
                certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 日期格式转换
                RecordUtil.convertDate2String(certinfoExtension);
                // 设置照面附件
                AttachBizlogic attachBizlogic = new AttachBizlogic();
                setCertinfoExtenionAttch(certinfoExtension, certExInfo, attachBizlogic);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("certinfo", certinfoExtension);
            dataJson.put("copycerturl", copycerturl);
            dataJson.put("certurl", certurl);

            log.info("=======结束调用getJSYDCertInfo接口=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取建设用地规划许可证信息成功！", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getJSYDCertInfo接口参数：params【" + params + "】=======");
            log.info("=======getJSYDCertInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取证照信息失败！", "");
        }
    } 
    
    /**
     * 获取建设项目信息（外部接口）
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getJSXMBAInfo", method = RequestMethod.POST)
    public String getJSXMBAInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getJSXMBAInfo接口=======");
            JSONObject json = JSON.parseObject(params);
            JSONObject obj = (JSONObject) json.get("params");
            // appkey是否存在
            String appkey = json.getString("appkey");
            if (StringUtil.isBlank(appkey)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入appkey！", "");
            }
            // 证照编号
            String certno = obj.getString("certno");
            if (StringUtil.isBlank(certno)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入项目编号！", "");
            }
            // 证照目录唯一版本标识
            String certcatalogid = obj.getString("certcatalogid");
            // 证照目录编码
            String tycertcatcode = obj.getString("tycertcatcode");
            if (StringUtil.isBlank(certcatalogid) && StringUtil.isBlank(tycertcatcode)) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "请输入证照目录唯一版本标识或者证照目录编码！", "");
            }
            CertCatalogOu catalogOu = null;
            if (StringUtil.isNotBlank(certcatalogid)) {
                // 有读权限
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatalogid("readauthority, ouname", true, appkey,
                        certcatalogid);
            }
            else {
                catalogOu = iCertCatalogOu.getCertCataLogOuByCatcode("readauthority, ouname", true, appkey,
                        tycertcatcode);
            }
            if (catalogOu == null || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getReadauthority())) {
                return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "该appkey没有读权限，请联系管理员！", "");
            }

            List<CertInfo> certInfoList = null;
            List<CertInfo> certInfoExList = null;
            CertCatalog certCatalog = null;
            if(StringUtil.isNotBlank(certcatalogid)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certcatalogid);
            }else if(StringUtil.isNotBlank(tycertcatcode)){
                certCatalog = iCertCatalog.getLatestCatalogDetailByCertcatcode("*" , tycertcatcode);
            }
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
            	certInfoExList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                        certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            	 certInfoList = handleIndividualCert(certCatalog.getCertcatalogid(), null, null, null, certno,
                         certCatalog.getApiaddress(), null, JsonUtil.jsonToMapString(obj.getString("extraparams")));
            }
            else {
                // 照面信息条件查询
                SqlUtils sqlUtils = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils.setSelectFields(" rowguid ");
                certInfoExList = iCertInfo.getListByCondition(sqlUtils.getMap());
                
                // 基本信息条件查询
                SqlUtils sqlUtils2 = new SqlUtils();
                if(StringUtil.isNotBlank(certcatalogid)){
                    sqlUtils2.eq("CERTCATALOGID", certcatalogid);
                }
                sqlUtils2.eq("CERTNO", certno);
                if(StringUtil.isNotBlank(tycertcatcode)){
                	sqlUtils2.eq("tycertcatcode", tycertcatcode);
                }
                // 不为历史记录
                sqlUtils2.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                // 状态为正常
                sqlUtils2.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                // 设置查询字段
                sqlUtils2.setSelectFields("certcliengguid,copycertcliengguid");
                certInfoList = iCertInfo.getListByCondition(sqlUtils2.getMap());
            }
            FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
            CertInfo certInfo = new CertInfo();
            String certurl="";
            String copycerturl="";
            if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                certInfo = certInfoList.get(0);
                List<FrameAttachInfo> certfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCertcliengguid());
                List<FrameAttachInfo> copycertfilelist=frameAttachInfoNewService.getAttachInfoList(certInfo.getCopycertcliengguid());
                if(certfilelist!=null&&certfilelist.size()>0){
                	certurl=JsonUtils.getRootUrl(request)
        					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + certfilelist.get(0).getAttachGuid() + "";
                }
                if(copycertfilelist!=null&&copycertfilelist.size()>0){
            	   copycerturl=JsonUtils.getRootUrl(request)
       					+ "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + copycertfilelist.get(0).getAttachGuid() + "";
               }
            }
            
            CertInfoExtension certinfoExtension = null;
            if (ValidateUtil.isNotBlankCollection(certInfoExList)) {
                CertInfo certExInfo = certInfoExList.get(0);
                // 获取照面信息
                Map<String, Object> filter = new HashMap<>();
                // 设置基本信息guid
                filter.put("certinfoguid", certExInfo.getRowguid());
                certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if (certinfoExtension == null) {
                    certinfoExtension = new CertInfoExtension();
                }
                // 日期格式转换
                RecordUtil.convertDate2String(certinfoExtension);
                // 设置照面附件
                AttachBizlogic attachBizlogic = new AttachBizlogic();
                setCertinfoExtenionAttch(certinfoExtension, certExInfo, attachBizlogic);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("certinfo", certinfoExtension);
            dataJson.put("copycerturl", copycerturl);
            dataJson.put("certurl", certurl);

            log.info("=======结束调用getJSXMBAInfo接口=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ONE, "获取建设项目信息成功！", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getJSXMBAInfo接口参数：params【" + params + "】=======");
            log.info("=======getJSXMBAInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.certRestReturn(CertConstant.CONSTANT_STR_ZERO, "获取建设项目信息失败！", "");
        }
    } 


    /**
     * 设置证照照面信息附件
     *
     * @param certinfoExtension
     * @param certInfo
     * @param attachBizlogic
     */
    private void setCertinfoExtenionAttch(CertInfoExtension certinfoExtension, CertInfo certInfo,
            AttachBizlogic attachBizlogic) {
        // 获取照面附件
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certInfo.getCertareaguid(),
                "image");
        if (ValidateUtil.isNotBlankCollection(metadataList)) {
            for (CertMetadata metadata : metadataList) {
                String cliengguid = certinfoExtension.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(cliengguid)) {
                    Record attach = new Record();
                    attach.set("metacliengguid", cliengguid);
                    certinfoExtension.set(metadata.getFieldname(), attach);
                }
            }
        }
    }
    
	private void setCertinfoExtenionAttchZsch(CertInfoExtension certinfoExtension, String Certareaguid,
            AttachBizlogic attachBizlogic) {
        // 获取照面附件
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(Certareaguid,
                "image");
        if (ValidateUtil.isNotBlankCollection(metadataList)) {
            for (CertMetadata metadata : metadataList) {
                String cliengguid = certinfoExtension.getStr(metadata.getFieldname());
                if (StringUtil.isNotBlank(cliengguid)) {
                    Record attach = new Record();
                    attach.set("metacliengguid", cliengguid);
                    certinfoExtension.set(metadata.getFieldname(), attach);
                }
            }
        }
    }
    

    /**
     * 个性化
     *
     * @param certCatalogid
     * @param certOwnerType
     * @param certOwnerCertType
     * @param certOwnerNo
     * @param certNo
     * @param apiAddress
     * @param certLevel
     * @return
     */
    private List<CertInfo> handleIndividualCert(String certCatalogid, String certOwnerType, String certOwnerCertType,
            String certOwnerNo, String certNo, String apiAddress, String certLevel, Map<String, String> extraParams) {
        List<CertInfo> certInfos = new ArrayList<>();
        if (StringUtil.isNotBlank(apiAddress) && ReflectUtil.exist(apiAddress)) {
            ICertIndividual certIndividualService = (ICertIndividual) ReflectUtil.getObjByClassName(apiAddress);
            JSONArray jsonArray = certIndividualService.getCertContent(certCatalogid, certOwnerType, certOwnerCertType,
                    certOwnerNo, certNo, extraParams);
            if (!jsonArray.isEmpty()) {
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    CertInfo certInfo = certIndividualService.getCertInfo(jsonObject);
                    Boolean isChange = certIndividualService.getIsChange(jsonObject, certCatalogid);
                    // 根据可信等级筛选
                    if (StringUtil.isNotBlank(certLevel) && !certLevel.equals(certInfo.getCertlevel())) {
                        continue;
                    }
                    if (isChange != null && !isChange) {
                        // 未改变，直接返回
                        CertInfo oldCertinfo = iCertInfo.getCertInfoByCertno(certInfo.getCertcatalogid(),
                                certInfo.getCertno());
                        if (oldCertinfo != null) {
                            certInfos.add(oldCertinfo);
                        }
                        else {
                            certInfos.add(certInfo);
                        }
                        continue;
                    }
                    else {
                        // null和true的情况暂时合并处理
                        // 证照改变，修改原数据
                        try {
                            EpointFrameDsManager.begin(null);
                            CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certCatalogid);

                            // 设置证照基本信息
                            String oldCertcliengguid = "";
                            if (StringUtil.isBlank(certInfo.getCertcatalogid())) {
                                certInfo.setCertcatalogid(certCatalogid);
                            }
                            CertInfo oldCertInfo = iCertInfo.getCertInfoByCertno(certInfo.getCertcatalogid(),
                                    certInfo.getCertno());
                            if (oldCertInfo != null) {
                                iCertInfo.deleteCertInfoByRowguid(oldCertInfo.getRowguid());
                                // 若为空，使用原来的rowguid
                                if (StringUtil.isBlank(certInfo.getRowguid())) {
                                    certInfo.setRowguid(oldCertInfo.getRowguid());
                                }
                                certInfo.setCertid(oldCertInfo.getCertid());
                                oldCertcliengguid = oldCertInfo.getCertcliengguid();
                            }
                            else {
                                certInfo.setCertid(UUID.randomUUID().toString());
                            }

                            if (StringUtil.isBlank(certInfo.getRowguid())) {
                                certInfo.setRowguid(UUID.randomUUID().toString());
                            }
                            certInfo.setCertareaguid(certCatalog.getRowguid());
                            if (StringUtil.isBlank(certInfo.getCertownertype())) {
                                certInfo.setCertownertype(certOwnerType);
                            }
                            if (StringUtil.isBlank(certInfo.getVersion())) {
                                certInfo.setVersion(CertConstant.CONSTANT_INT_ONE);
                            }
                            if (StringUtil.isBlank(certInfo.getVersiondate())) {
                                certInfo.setVersiondate(new Date());
                            }
                            certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                            if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsparent())) {
                                certInfo.setParentcertcatalogid(certCatalog.getParentid());
                            }
                            certInfo.setAddusername("第三方接口");
                            if (StringUtil.isBlank(certInfo.getOperatedate())) {
                                certInfo.setOperatedate(new Date());
                            }
                            if (StringUtil.isBlank(certInfo.getCertcliengguid())) {
                                certInfo.setCertcliengguid(UUID.randomUUID().toString());
                            }
                            certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                            certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);

                            if (oldCertInfo != null) {
                                iCertInfo.deleteCertInfoByRowguid(oldCertInfo.getRowguid());
                            }
                            iCertInfo.addCertInfo(certInfo);

                            // 设置证照拓展信息
                            CertInfoExtension certInfoExtension = certIndividualService.getCertInfoExtension(jsonObject,
                                    certInfo.getRowguid());
                            if (certInfoExtension != null) {
                                MongodbUtil mongodbUtil = getMongodbUtil();
                                // 获得照面信息
                                Map<String, Object> filter = new HashMap<>();
                                // 设置基本信息guid
                                filter.put("certinfoguid", certInfo.getRowguid());
                                String extensionGuid = UUID.randomUUID().toString();
                                CertInfoExtension oldExtension = mongodbUtil.find(CertInfoExtension.class, filter,
                                        false);
                                if (oldExtension != null) {
                                    extensionGuid = oldExtension.getRowguid();
                                }
                                certInfoExtension.setRowguid(extensionGuid);
                                certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                                mongodbUtil.update(certInfoExtension);
                            }

                            // 设置附件信息
                            List<FrameAttachStorage> frameAttachStorages = certIndividualService.getAttach(jsonObject,
                                    certInfo.getCertcliengguid());
                            if (ValidateUtil.isNotBlankCollection(frameAttachStorages)) {
                                for (FrameAttachStorage frameAttachStorage : frameAttachStorages) {
                                    if (StringUtil.isNotBlank(oldCertcliengguid)) {
                                        attachService.deleteAttachByGuid(oldCertcliengguid);
                                    }
                                    AttachUtil.addAttach(frameAttachStorage, certInfo.getCertcliengguid());
                                }
                            }
                            else {
                                certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
                                iCertInfo.updateCertInfo(certInfo);
                            }

                            certInfos.add(certInfo);
                            EpointFrameDsManager.commit();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            EpointFrameDsManager.rollback();
                        }
                    }
                }
            }
        }
        return certInfos;
    }
}
