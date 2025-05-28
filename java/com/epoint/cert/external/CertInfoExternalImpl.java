package com.epoint.cert.external;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.DataSet;
import com.epoint.apimanage.utils.api.ICommonDaoService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.domain.CertCatalogOu;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certcorrection.certcorrectioninfo.domain.CertCorrectionInfo;
import com.epoint.cert.basic.certcorrection.certcorrectioninfo.inter.ICertCorrectionInfo;
import com.epoint.cert.basic.certcorrection.domain.CertCorrection;
import com.epoint.cert.basic.certcorrection.inter.ICertCorrection;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.inter.ICertInfoSubExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certownerinfo.domain.CertOwnerInfo;
import com.epoint.cert.basic.certownerinfo.inter.ICertOwnerInfo;
import com.epoint.cert.common.api.ICertIndividual;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.*;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.common.cert.authentication.GenerateBizlogic;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.convert.ConvertUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.api.IOuServiceInternal;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 证照实例RPC接口实现
 *
 * @作者 dingwei
 * @version [版本号, 2017年11月13日]
 */
@Service
@Component
public class CertInfoExternalImpl implements ICertInfoExternal
{
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
    private static ExecutorService executorService = new ThreadPoolExecutor(CertConstant.CONSTANT_INT_ONE,
            CertConstant.CONSTANT_INT_ONE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
            namedThreadFactory);

    private static String bigdataappkey = ConfigUtil.getConfigValue("bigDataAppKey");

    private long currentTime = 0;

    private String accessToken = null;

    private long expiresIn = 0;

    private MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        MongodbUtil mongodbUtil = new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(),
                dsc.getPassword());
        return mongodbUtil;
    }

    @Override
    public List<CertInfo> selectCertByOwner(String certCatalogids, String certOwnerType, String certOwnerCertType,
            String certOwnerNo, Boolean isQueryApi, String appKey, Map<String, String> extraParams) {
        List<CertInfo> certInfos = new ArrayList<>();
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String selectCertInfoByOwner = asRestUrl + "/certInfo/selectCertInfoByOwner";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            //证照持有者类型
            jsonObject.put("certownertype", certOwnerType);
            //持有者证件类型
            jsonObject.put("certownercerttype", certOwnerCertType);
            //持有者证件号码
            jsonObject.put("certownerno", certOwnerNo);
            //证照目录id
            jsonObject.put("certcatalogid", certCatalogids);
            //是否与第三方系统对接
            jsonObject.put("isqueryapi", ConvertUtil.convertBooleanToInteger(isQueryApi));
            jsonObject.put("extraparams", extraParams);
            paramsToken.put("params", jsonObject);
            paramsToken.put("appkey", appKey);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(selectCertInfoByOwner, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    certInfos = JsonUtil.jsonToList(objReturn.getJSONObject("custom").get("certinfo").toString(),
                            CertInfo.class);
                }
                else {
                    log.error(objReturn.getJSONObject("custom").getString("text"));
                }
            }
        }
        else {
            ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
            for (String certCatalogid : certCatalogids.split(";")) {
                CertCatalog certCatalog = certCatalogService.getLatestCatalogDetailByCatalogid(certCatalogid);
                if (certCatalog == null || CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())) {
                    continue;
                }
                // 需要调用接口且能找到实现，由第三方实现
                if (isQueryApi && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
                    // 持有人类型匹配，从第三方接口查找
                    if (certCatalog.getBelongtype().contains(certOwnerType)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerCertType)
                            || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerCertType)) {
                        certInfos.addAll(handleIndividualCert(certCatalogid, certOwnerType, certOwnerCertType,
                                certOwnerNo, certCatalog.getApiaddress(), null, extraParams));
                    }
                }
                // 从库里找
                else {
                    String sql = "";
                    List<Object> params = new ArrayList<>(
                            Arrays.asList(certCatalogid, "%" + certOwnerType + "%", certOwnerNo));
                    Integer isMulti = certCatalogService.isMultiOwner(certCatalogid);
                    if (CertConstant.CONSTANT_INT_ONE.equals(isMulti)) {
                        //多持有人
                        sql = "select * from cert_info a where certCatalogid=?1 and certOwnerType like ?2 "
                                + "and ishistory=0 and status='10' and rowguid in";
                        String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?3 ";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            childSql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                        sql = sql + "(" + childSql + ")";
                    }
                    else if (CertConstant.CONSTANT_INT_ZERO.equals(isMulti)) {
                        //单持有人
                        sql = "select * from cert_info where certCatalogid=?1 and certOwnerType like ?2 and certOwnerNo=?3 "
                                + "and ishistory=0 and status='10'";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            sql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                    }
                    else {
                        //混合，上面两种union一下
                        sql = "select * from cert_info a where certCatalogid=?1 and certOwnerType like ?2 "
                                + "and ishistory=0 and status='10' and rowguid in";
                        String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?3 ";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            childSql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                        sql = sql + "(" + childSql + ")";
                        sql += " union all ";
                        sql += "select * from cert_info where certCatalogid=?1 and certOwnerType like ?2 and certOwnerNo=?3 "
                                + "and ishistory=0 and status='10'";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            sql += " and certOwnerCertType=?4";
                        }
                    }
                    certInfos.addAll(new DBServcie().getDao().findList(sql, CertInfo.class, params.toArray()));
                }

                // 是主目录的情况
                if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsparent())) {
                    List<CertCatalog> certCatalogs = certCatalogService.selectChildCatalog(certCatalogid);
                    if (ValidateUtil.isNotBlankCollection(certCatalogs)) {
                        StringBuilder ids = new StringBuilder();
                        for (CertCatalog childCertCatalog : certCatalogs) {
                            //排除禁用的
                            if (CertConstant.CONSTANT_INT_ZERO.equals(childCertCatalog.getIsenable())) {
                                continue;
                            }
                            // 需要调用第三方接口的直接查询，不需要的记录下id
                            if (isQueryApi && CertConstant.CONSTANT_INT_ONE.equals(childCertCatalog.getIsuseapi())) {
                                // 持有人类型匹配，从第三方接口查找
                                if (childCertCatalog.getBelongtype().contains(certOwnerType)
                                        || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerCertType)
                                        || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerCertType)) {
                                    certInfos.addAll(handleIndividualCert(childCertCatalog.getCertcatalogid(),
                                            certOwnerType, certOwnerCertType, certOwnerNo,
                                            childCertCatalog.getApiaddress(), null, extraParams));
                                }
                            }
                            else {
                                //拼接所有子目录的certcatalogid
                                ids.append("'" + childCertCatalog.getCertcatalogid() + "',");
                            }
                        }
                        // 有id需要从数据库中查询
                        if (ids.length() > 0) {
                            //去掉最后一个","号
                            ids.deleteCharAt(ids.length() - 1);
                            List<Object> params = new ArrayList<>(
                                    Arrays.asList("%" + certOwnerType + "%", certOwnerNo));
                            String sql = "select * from cert_info where certCatalogid in (" + ids.toString()
                                    + ") and certOwnerType like ?1 and certOwnerNo=?2 and ishistory=0 and status='10'";
                            String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?2";
                            if (StringUtil.isNotBlank(certOwnerCertType)) {
                                sql = sql + " and certOwnerCertType=?3 union all " + sql + " and rowguid in ("
                                        + childSql + " and certOwnerCertType=?3)";
                                params.add(certOwnerCertType);
                            }
                            else {
                                sql = sql + " union all " + sql + " and rowguid in (" + childSql + ")";
                            }
                            certInfos.addAll(new DBServcie().getDao().findList(sql, CertInfo.class, params.toArray()));
                        }
                    }
                }
            }
        }
        return certInfos;
    }

    @Override
    public Boolean isExistCertByOwner(String certCatalogids, String certOwnerType, String certOwnerCertType,
            String certOwnerNo, Map<String, String> extraParams) {
        boolean isExist = false;
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return false;
            }
            // 对应rest接口地址
            String isExistCertByOwner = asRestUrl + "/certInfo/isExistCertByOwner";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            //持有者类型
            jsonObject.put("certownertype", certOwnerType);
            //持有者证件类型
            jsonObject.put("certownercerttype", certOwnerCertType);
            //持有者证件号码
            jsonObject.put("certownerno", certOwnerNo);
            jsonObject.put("certcatalogid", certCatalogids);
            jsonObject.put("extraparams", extraParams);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(isExistCertByOwner, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    isExist = ConvertUtil
                            .convertStringToBoolean(objReturn.getJSONObject("custom").get("isexist").toString());
                }
                return isExist;
            }
        }
        else {
            List<CertInfo> certInfos = new ArrayList<>();
            ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
            for (String certCatalogid : certCatalogids.split(";")) {
                CertCatalog certCatalog = certCatalogService.getLatestCatalogDetailByCatalogid(certCatalogid);
                // 需要调用接口且能找到实现
                if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
                    // 持有人类型匹配时， 从第三方接口查找
                    if (certCatalog.getBelongtype().contains(certOwnerType)
                            || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerCertType)
                            || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerCertType)) {
                        certInfos = handleIndividualCert(certCatalogid, certOwnerType, certOwnerCertType, certOwnerNo,
                                certCatalog.getApiaddress(), null, extraParams);
                        if (certInfos.size() > 0) {
                            return true;
                        }
                    }
                }
                else {
                    // 从库里找
                    String sql = "";
                    Integer isMulti = certCatalogService.isMultiOwner(certCatalogid);
                    List<Object> params = new ArrayList<>(
                            Arrays.asList(certCatalogid, "%" + certOwnerType + "%", certOwnerNo));
                    if (CertConstant.CONSTANT_INT_ONE.equals(isMulti)) {
                        //多持有人
                        sql = "select count(1) from cert_info a where certCatalogid=?1 and certOwnerType like ?2 "
                                + "and ishistory=0 and status='10' and rowguid in";
                        String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?3 ";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            childSql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                        sql = sql + "(" + childSql + ")";
                    }
                    else if (CertConstant.CONSTANT_INT_ZERO.equals(isMulti)) {
                        //单持有人
                        sql = "select count(1) from cert_info where certCatalogid=?1 and certOwnerType like ?2 and certOwnerNo=?3 "
                                + "and ishistory=0 and status='10'";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            sql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                    }
                    else {
                        //混合，上面两种union一下
                        sql = "select * from cert_info a where certCatalogid=?1 and certOwnerType like ?2 "
                                + "and ishistory=0 and status='10' and rowguid in";
                        String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?3 ";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            childSql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                        sql = sql + "(" + childSql + ")";
                        sql += " union all ";
                        sql += "select * from cert_info where certCatalogid=?1 and certOwnerType like ?2 and certOwnerNo=?3 "
                                + "and ishistory=0 and status='10'";
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            sql += " and certOwnerCertType=?4";
                        }
                        sql = "select count(*) from (" + sql + ") c";
                    }
                    Boolean flag = new DBServcie().getDao().queryBoolean(sql, params.toArray());
                    if (flag) {
                        return true;
                    }
                }

                // 是主目录的情况
                List<CertCatalog> certCatalogs = new ArrayList<>();
                if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsparent())) {
                    certCatalogs = certCatalogService.selectChildCatalog(certCatalogid);
                    if (ValidateUtil.isNotBlankCollection(certCatalogs)) {
                        StringBuilder ids = new StringBuilder();
                        for (CertCatalog childCertCatalog : certCatalogs) {
                            //排除禁用的
                            if (CertConstant.CONSTANT_INT_ZERO.equals(childCertCatalog.getIsenable())) {
                                continue;
                            }
                            if (CertConstant.CONSTANT_INT_ONE.equals(childCertCatalog.getIsuseapi())) {
                                // 持有人类型匹配时， 从第三方接口查找
                                if (childCertCatalog.getBelongtype().contains(certOwnerType)
                                        || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerCertType)
                                        || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerCertType)) {
                                    certInfos = handleIndividualCert(certCatalogid, certOwnerType, certOwnerCertType,
                                            certOwnerNo, certCatalog.getApiaddress(), null, extraParams);
                                }
                                if (certInfos.size() > 0) {
                                    return true;
                                }
                            }
                            else {
                                //拼接所有子目录的certcatalogid
                                ids.append("'" + childCertCatalog.getCertcatalogid() + "',");
                            }
                        }
                        // 有id需要从数据库中查询
                        if (StringUtil.isNotBlank(ids)) {
                            ids.deleteCharAt(ids.length() - 1);
                            List<Object> params = new ArrayList<>(
                                    Arrays.asList("%" + certOwnerType + "%", certOwnerNo));
                            String sql = "select * from cert_info where certCatalogid in (" + ids.toString()
                                    + ") and certOwnerType like ?1 and certOwnerNo=?2 and ishistory=0 and status='10'";
                            String childSql = "select certinfoguid from cert_ownerinfo where certOwnerNo=?2";
                            if (StringUtil.isNotBlank(certOwnerCertType)) {
                                sql = sql + " and certOwnerCertType=?3 union all " + sql + " and rowguid in ("
                                        + childSql + " and certOwnerCertType=?3)";
                                params.add(certOwnerCertType);
                            }
                            else {
                                sql = sql + " union all " + sql + " and rowguid in (" + childSql + ")";
                            }
                            sql = "select count(*) from (" + sql + ") c";
                            Boolean flag = new DBServcie().getDao().queryBoolean(sql, params.toArray());
                            if (flag) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public CertInfo getCertInfoByRowguid(String rowGuid) {
        CertInfo certInfo = null;
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String getCertInfoByGuid = asRestUrl + "/certInfo/getCertInfoByGuid";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            jsonObject.put("rowguid", rowGuid);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(getCertInfoByGuid, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                String certinfo = objReturn.getJSONObject("custom").getString("certinfo");
                if (StringUtil.isNotBlank(certinfo)) {
                    certInfo = JsonUtil.jsonToObject(certinfo, CertInfo.class);
                }

            }
        }
        else {
            ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            certInfo = iCertInfo.getCertInfoByRowguid(rowGuid);
        }
        return certInfo;
    }

    @Override
    public List<CertInfo> selectCertInfo(String certOwnerType, String certOwnerNo, String kind, String certLevel,
            Map<String, String> extraParams) {
        String sql = "select a.* from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                + " where certOwnerType like ? and certOwnerNo=? and a.ishistory=0 and a.status='10'"
                + " and b.ishistory=0 and b.isuseapi=0";
        List<Object> params = new ArrayList<>(Arrays.asList("%" + certOwnerType + "%", certOwnerNo));
        if (StringUtil.isNotBlank(kind)) {
            sql += " and kind=?";
            params.add(kind);
        }
        if (StringUtil.isNotBlank(certLevel)) {
            sql += " and certlevel=?";
            params.add(certLevel);
        }
        DBServcie dbServcie = new DBServcie();
        List<CertInfo> certInfos = dbServcie.getDao().findList(sql, CertInfo.class, params.toArray());
        params.clear();
        sql = "select * from cert_catalog where ishistory=0 and isuseapi=1 and belongtype like ?";
        params.add("%" + certOwnerType + "%");
        if (StringUtil.isNotBlank(certLevel)) {
            sql += " and certlevel=?";
            params.add(certLevel);
        }
        List<CertCatalog> certCatalogs = dbServcie.getDao().findList(sql, CertCatalog.class, params.toArray());
        if (certCatalogs != null && certCatalogs.size() > 0) {
            for (CertCatalog certCatalog : certCatalogs) {
                // 持有人类型匹配，从第三方接口查找
                if (certCatalog.getBelongtype().contains(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerType)) {
                    certInfos.addAll(handleIndividualCert(certCatalog.getCertcatalogid(), certOwnerType, null,
                            certOwnerNo, certCatalog.getApiaddress(), certLevel, extraParams));
                }
            }
        }
        return certInfos;
    }

    /**
     * 根据条件获取申请人材料信息 重载selectCertInfo方法
     *
     * @param certOwnerType
     *            持有人类型
     * @param certOwnerNo
     *            持有人证件号码
     * @param kind
     *            证照分类
     * @param certLevel
     *            可信等级
     * @param materialtype
     *            材料类型
     */
    public List<CertInfo> selectCertInfo(String certOwnerType, String certOwnerNo, String kind, String certLevel,
            String materialtype, String appKey, Boolean isQueryApi, Map<String, String> extraParams) {
        List<CertInfo> certInfos = null;
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (extraParams == null) {
            extraParams = new HashMap<>();
        }
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)
                && !CertConstant.CONSTANT_STR_ONE.equals(extraParams.get("useexternal"))) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String selectCertInfoByOwner = asRestUrl + "/certInfo/selectCertInfoByOwner";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            //持有者类型
            jsonObject.put("certownertype", certOwnerType);
            //持有者证件编号
            jsonObject.put("certownerno", certOwnerNo);
            //证照分类
            jsonObject.put("kind", kind);
            //可信等级
            jsonObject.put("certlevel", certLevel);
            //材料类型（必传）
            jsonObject.put("materialtype", materialtype);
            //是否需要从第三方取数据
            jsonObject.put("isqueryapi", ConvertUtil.convertBooleanToInteger(isQueryApi));
            extraParams.put("useexternal", "1");
            jsonObject.put("extraparams", extraParams);
            paramsToken.put("params", jsonObject);
            paramsToken.put("appkey", appKey);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(selectCertInfoByOwner, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    certInfos = JsonUtil.jsonToList(objReturn.getJSONObject("custom").get("certinfo").toString(),
                            CertInfo.class);
                }
                else {
                    log.error(objReturn.getJSONObject("custom").getString("text"));
                }

            }
        }
        else {
            String sql = "";
            List<Object> params = null;
            if (CertConstant.MATERIAL_ZZ.equals(materialtype)) {
                sql = "select a.*,b.kind from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                        + " where certOwnerType like ? and certOwnerNo=? and a.ishistory=0 and a.status='10' and a.materialtype=?"
                        + " and b.ishistory=0 and b.isuseapi=0"
                        + " union all select a.*,b.kind from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                        + " where certOwnerType like ? and a.ishistory=0 and a.status='10' and a.materialtype=?"
                        + " and b.ishistory=0 and b.isuseapi=0 and a.rowguid in (select certinfoguid from cert_ownerinfo where certOwnerNo=? )";
                params = new ArrayList<>(Arrays.asList("%" + certOwnerType + "%", certOwnerNo, materialtype,
                        "%" + certOwnerType + "%", materialtype, certOwnerNo));
            }
            else {
                sql = "select a.*,b.kind from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                        + " where certOwnerType like ? and certOwnerNo=? and a.ishistory=0 and a.status='10' and a.materialtype=?"
                        + " and b.ishistory=0 and b.isuseapi=0";
                params = new ArrayList<>(Arrays.asList("%" + certOwnerType + "%", certOwnerNo, materialtype));
            }
            if (StringUtil.isNotBlank(kind)) {
                sql = "select * from (" + sql + ")c where kind=?";
                params.add(kind);
            }
            if (StringUtil.isNotBlank(certLevel)) {
                sql = "select * from (" + sql + ")c where certlevel=?";
                params.add(certLevel);
            }
            DBServcie dbServcie = new DBServcie();
            certInfos = dbServcie.getDao().findList(sql, CertInfo.class, params.toArray());
            //            params.clear();
            //            // 需要从第三方获取数据的
            //            sql = "select * from cert_catalog where ishistory=0 and isuseapi=1 and belongtype like ? and materialtype=?";
            //            params.add("%" + certOwnerType + "%");
            //            params.add(materialtype);
            //            List<CertCatalog> certCatalogs = dbServcie.getDao().findList(sql, CertCatalog.class, params.toArray());
            //            if (certCatalogs != null && certCatalogs.size() > 0) {
            //                for (CertCatalog certCatalog : certCatalogs) {
            //                    // 持有人类型匹配，从第三方接口查找
            //                    if (certCatalog.getBelongtype().contains(certOwnerType)) {
            //                        extraParams.remove("useexternal");
            //                        certInfos.addAll(handleIndividualCert(certCatalog.getCertcatalogid(), certOwnerType, null,
            //                                certOwnerNo, certCatalog.getApiaddress(), certLevel, extraParams));
            //                    }
            //                }
            //            }
        }
        return certInfos;
    }

    @Override
    public Integer getCertCount(String certOwnerType, String certOwnerNo, String kind) {
        String sql = "select * from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                + " where certOwnerNo=?1 and certOwnerNo=?2 a.ishistory=0 and a.status='10'" + " and b.ishistory=0";
        List<Object> params = new ArrayList<>(Arrays.asList(certOwnerType, certOwnerNo));
        if (StringUtil.isNotBlank(kind)) {
            sql += " and kind=?";
            params.add(kind);
        }
        return new DBServcie().getDao().queryInt(sql, CertInfo.class, params.toArray());
    }

    /**
     * 获取申请人在不同证照分类下的证照数量 重载getCertCount方法
     *
     * @param certOwnerType
     *            持有人类型
     * @param certOwnerNo
     *            持有人证件号码
     * @param kind
     *            证照分类
     * @param materialtype
     *            材料类型
     * @return
     */
    @Override
    public Integer getCertCount(String certOwnerType, String certOwnerNo, String kind, String materialtype) {
        String sql = "select * from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                + " where certOwnerNo=?1 and certOwnerNo=?2 and materialtype=?3 and a.ishistory=0 and a.status='10'"
                + " and b.ishistory=0";
        List<Object> params = new ArrayList<>(Arrays.asList(certOwnerType, certOwnerNo, materialtype));
        if (StringUtil.isNotBlank(kind)) {
            sql += " and kind=?";
            params.add(kind);
        }
        return new DBServcie().getDao().queryInt(sql, CertInfo.class, params.toArray());
    }

    @Override
    public List<CertInfo> selectCertByOwner(String certCatalogids, String certOwnerType, String certOwnerCertType,
            String certOwnerNo, Boolean isQueryApi, Map<String, String> extraParams) {
        List<CertInfo> certInfos = new ArrayList<>();
        ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
        for (String certCatalogid : certCatalogids.split(";")) {
            CertCatalog certCatalog = certCatalogService.getLatestCatalogDetailByCatalogid(certCatalogid);
            // 需要调用接口且能找到实现
            if (isQueryApi && certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsuseapi())) {
                // 持有人类型匹配，从第三方接口查找
                if (certCatalog.getBelongtype().contains(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerType)) {
                    certInfos.addAll(handleIndividualCert(certCatalogid, certOwnerType, certOwnerCertType, certOwnerNo,
                            certCatalog.getApiaddress(), null, extraParams));
                }
            }
            else {
                // 从库里找
                String sql = "select * from cert_info where certCatalogid=?1 and certOwnerType like ?2 and certOwnerNo=?3 "
                        + "and ishistory=0 and status='10'";
                List<Object> params = new ArrayList<>(
                        Arrays.asList(certCatalogid, "%" + certOwnerType + "%", certOwnerNo));
                if (StringUtil.isNotBlank(certOwnerCertType)) {
                    sql += " and certOwnerCertType=?4";
                    params.add(certOwnerCertType);
                }
                certInfos.addAll(new DBServcie().getDao().findList(sql, CertInfo.class, params.toArray()));
            }

            // 是主目录的情况
            if (certCatalog != null && CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsparent())) {
                List<CertCatalog> certCatalogs = certCatalogService.selectChildCatalog(certCatalogid);
                if (ValidateUtil.isNotBlankCollection(certCatalogs)) {
                    StringBuilder ids = new StringBuilder();
                    for (CertCatalog childCertCatalog : certCatalogs) {
                        // 需要调用第三方接口的直接查询，不需要的记录下id
                        if (isQueryApi && CertConstant.CONSTANT_INT_ONE.equals(childCertCatalog.getIsuseapi())) {
                            // 持有人类型匹配，从第三方接口查找
                            if (childCertCatalog.getBelongtype().contains(certOwnerType)
                                    || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerType)
                                    || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerType)) {
                                certInfos.addAll(handleIndividualCert(childCertCatalog.getCertcatalogid(),
                                        certOwnerType, certOwnerCertType, certOwnerNo, childCertCatalog.getApiaddress(),
                                        null, extraParams));
                            }
                        }
                        else {
                            ids.append("'" + childCertCatalog.getCertcatalogid() + "',");
                        }
                    }
                    // 有id需要从数据库中查询
                    if (StringUtil.isNotBlank(ids)) {
                        ids.deleteCharAt(ids.length() - 1);

                        String sql = "select * from cert_info where certCatalogid in (" + ids.toString()
                                + ") and certOwnerType like ?2 and certOwnerNo=?3 " + "and ishistory=0 and status='10'";
                        List<Object> params = new ArrayList<>(
                                Arrays.asList(certCatalogid, "%" + certOwnerType + "%", certOwnerNo));
                        if (StringUtil.isNotBlank(certOwnerCertType)) {
                            sql += " and certOwnerCertType=?4";
                            params.add(certOwnerCertType);
                        }
                        certInfos.addAll(new DBServcie().getDao().findList(sql, CertInfo.class, params.toArray()));
                    }
                }
            }
        }
        return certInfos;
    }

    @Override
    public List<CertInfo> selectCertInfo(String certOwnerType, String certOwnerNo, String kind, String certLevel,
            String materialtype, Map<String, String> extraParams) {
        String sql = "select a.* from cert_info a inner join cert_catalog b on a.certcatalogid=b.certcatalogid"
                + " where certOwnerType like ? and certOwnerNo=? and a.ishistory=0 and a.status='10' and a.materialtype=?"
                + " and b.ishistory=0 and b.isuseapi=0";
        List<Object> params = new ArrayList<>(Arrays.asList("%" + certOwnerType + "%", certOwnerNo, materialtype));
        if (StringUtil.isNotBlank(kind)) {
            sql += " and kind=?";
            params.add(kind);
        }
        if (StringUtil.isNotBlank(certLevel)) {
            sql += " and certlevel=?";
            params.add(certLevel);
        }
        DBServcie dbServcie = new DBServcie();
        List<CertInfo> certInfos = dbServcie.getDao().findList(sql, CertInfo.class, params.toArray());
        params.clear();
        // 需要从第三方获取数据的
        sql = "select * from cert_catalog where ishistory=0 and isuseapi=1 and belongtype like ? and materialtype=?";
        params.add("%" + certOwnerType + "%");
        params.add(materialtype);
        if (StringUtil.isNotBlank(certLevel)) {
            sql += " and certlevel=?";
            params.add(certLevel);
        }
        List<CertCatalog> certCatalogs = dbServcie.getDao().findList(sql, CertCatalog.class, params.toArray());
        if (certCatalogs != null && certCatalogs.size() > 0) {
            for (CertCatalog certCatalog : certCatalogs) {
                // 持有人类型匹配，从第三方接口查找
                if (certCatalog.getBelongtype().contains(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_HH.equals(certOwnerType)
                        || CertConstant.CERTOWNERTYPE_QT.equals(certOwnerType)) {
                    certInfos.addAll(handleIndividualCert(certCatalog.getCertcatalogid(), certOwnerType, null,
                            certOwnerNo, certCatalog.getApiaddress(), certLevel, extraParams));
                }
            }
        }
        return certInfos;
    }

    @Override
    public CertInfoExtension getCertExtenByCertInfoGuid(String certInfoGuid) {
        CertInfoExtension certInfoExtension = null;
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String getCertInfoExtenByGuid = asRestUrl + "/certInfo/getCertInfoExtenByGuid";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            jsonObject.put("certinfoguid", certInfoGuid);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(getCertInfoExtenByGuid, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                String certinfo = objReturn.getJSONObject("custom").getString("certinfoextension");
                if (StringUtil.isNotBlank(certinfo)) {
                    certInfoExtension = JsonUtil.jsonToObject(certinfo, CertInfoExtension.class);
                }
            }
        }
        else {
            MongodbUtil mongodbUtil = getMongodbUtil();
            Map<String, Object> filter = new HashMap<>();
            filter.put("certinfoguid", certInfoGuid);
            certInfoExtension = mongodbUtil.find(CertInfoExtension.class, filter, false);
        }
        return certInfoExtension;
    }

    @Override
    public List<CertInfo> selectCertInfo(String s, String s1, String s2, String s3, String s4, String s5, Boolean aBoolean, String s6, Map<String, String> map) {
        return null;
    }

    /**
     * 新增或变更材料信息（生成新的已备案版本）
     *
     * @param certInfo
     *            证照信息实体
     * @param certInfoExtension
     *            证照拓展信息实体
     * @param materialtype
     *            材料类型
     * @param appkey
     *            外部应用标识
     * @return
     */
    @Override
    public String submitCertInfo(CertInfo certInfo, CertInfoExtension certInfoExtension, String materialtype,
            String appkey) {
        if (StringUtil.isBlank(materialtype)) {
            return "请填写材料类型！";
        }
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        //使用rest接口
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return "证照库rest对接地址未配置";
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();

            // 对应rest接口地址
            String submitCertInfo = asRestUrl + "/certInfo/submitCertInfo";
            // 拼接参数
            //证照基本信息
            jsonObject.put("certinfo", JsonUtil.objectToJson(certInfo));
            //照面信息
            jsonObject.put("certinfoextension", JsonUtil.objectToJson(certInfoExtension));
            //材料类型
            jsonObject.put("materialtype", materialtype);
            paramsToken.put("appkey", appkey);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(submitCertInfo, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    String msg = objReturn.getJSONObject("custom").getString("msg");
                    String success = objReturn.getJSONObject("custom").getString("success");
                    if (msg.contains("成功") || CertConstant.CONSTANT_STR_ONE.equals(success)) {
                        String certcliengguid = objReturn.getJSONObject("custom").getString("certcliengguid");
                        String copycertcliengguid = objReturn.getJSONObject("custom").getString("copycertcliengguid");
                        jsonObject.clear();
                        jsonObject.put("certcliengguid", certcliengguid);
                        jsonObject.put("copycertcliengguid", copycertcliengguid);
                        jsonObject.put("oldcertcliengguid", certInfo.getCertcliengguid());
                        jsonObject.put("oldcopycertcliengguid", certInfo.getCopycertcliengguid());
                        jsonObject.put("appkey", appkey);
                        String attachurl = asRestUrl + "/certAttach/addAttach";
                        jsonObject.put("url", attachurl);
                        if (CertConstant.CONSTANT_STR_ONE.equals(configService.getFrameConfigValue("Cert_NotUseMQ"))) {
                            executorService.execute(new Runnable()
                            {
                                @Override
                                public void run() {
                                    new ExternalListener().handleAttach(attachurl, certcliengguid, copycertcliengguid,
                                            certInfo.getCertcliengguid(), certInfo.getCopycertcliengguid(), appkey);
                                }
                            });
                        }
                        else {
                            MQSender.send(CertConstant.QUEUENAME, jsonObject);
                        }
                    }
                    return msg;
                }
            }
            return "新增失败";
        }
        else {
            CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();
            certInfo.setAreacode(appkey);
            String rowGuid = certInfo.getRowguid();
            if (StringUtil.isBlank(rowGuid)) {
                return "无法匹配到数据，请传递主键！";
            }
            String cliengGuid = certInfo.getCertcliengguid();
            String newCliengGuid = UUID.randomUUID().toString();
            String copycliengGuid = certInfo.getCopycertcliengguid();
            String newCopyCliengGuid = UUID.randomUUID().toString();
            Boolean hasAttach = false;
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            //是否存在附件
            if (attachService.getAttachCountByClientGuid(cliengGuid) > 0) {
                hasAttach = true;
            }
            //是否存在副本附件
            Boolean hasCopyAttach = false;
            if (attachService.getAttachCountByClientGuid(copycliengGuid) > 0) {
                hasCopyAttach = true;
            }

            ICertInfo certInfoService = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            CertInfo oldCertInfo = certInfoService.getCertInfoByRowguid(certInfo.getRowguid());
            ICertOwnerInfo ownerInfoService = ContainerFactory.getContainInfo().getComponent(ICertOwnerInfo.class);

            // 存在对应版本
            if (oldCertInfo != null) {
                // 草稿状态，直接更新实体信息并修改状态
                // auditstatus = 20, ishistory = 1
                boolean isDraft = CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(oldCertInfo.getAuditstatus())
                        && CertConstant.CONSTANT_INT_ONE.equals(oldCertInfo.getIshistory());
                if (isDraft) {
                    // 更新实体信息
                    updateCertInfo(oldCertInfo, certInfo);
                    certInfo.setVersiondate(new Date());
                    certInfo.setOperatedate(new Date());
                    certInfo.setCertcliengguid(newCliengGuid);
                    certInfo.setCopycertcliengguid(newCopyCliengGuid);
                    certInfo.setIscreatecert(ConvertUtil.convertBooleanToInteger(hasAttach));
                    // 传递了附件则复制
                    if (hasAttach) {
                        attachService.copyAttachByClientGuid(cliengGuid, "", "", newCliengGuid);
                    }

                    if (hasCopyAttach) {
                        attachService.copyAttachByClientGuid(copycliengGuid, "", "", newCopyCliengGuid);
                    }
                    certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                    certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                    certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                    // 设置材料类型
                    certInfo.setMaterialtype(materialtype);
                    // 设置材料来源
                    certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);

                    ICertCatalog certCatalogService = ContainerFactory.getContainInfo()
                            .getComponent(ICertCatalog.class);
                    CertCatalog certCatalog = certCatalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
                    //多持有人处理
                    if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())
                            && StringUtil.isNotBlank(certInfo.getCertownerno())) {
                        String[] certownerno = certInfo.getCertownerno().split("\\^");
                        String ownername = "";
                        if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                            ownername = certInfo.getCertownername();
                        }
                        String[] certownername = ownername.split("\\^");
                        String ownertype = "";
                        if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                            ownertype = certInfo.getCertownercerttype();
                        }
                        String[] certownercerttype = ownertype.split("\\^");
                        for (int i = 0; i < certownerno.length; i++) {
                            CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                            certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                            certOwnerInfo.setCertownerno(certownerno[i]);
                            if (certownername.length > i) {
                                certOwnerInfo.setCertownername(certownername[i]);
                            }
                            if (certownercerttype.length > i) {
                                certOwnerInfo.setCertownercerttype(certownercerttype[i]);
                            }
                            certOwnerInfo.setCertinfoguid(certInfo.getRowguid());
                            ownerInfoService.addCertOwnerInfo(certOwnerInfo);
                        }
                        if (!certInfo.getCertownerno().endsWith("^")) {
                            certInfo.setCertownerno(certInfo.getCertownerno() + "^");
                        }
                    }
                    certInfo.ignoreModifyFlag();
                    // 证照类型代码
                    certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                    //设置证照标识
                    certInfo.setCertificateidentifier(
                            certInfoBizlogic.generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                    certInfoService.updateCertInfo(certInfo);

                    // 更新照面信息
                    if (certInfoExtension != null) {
                        // 获得照面信息
                        Map<String, Object> filter = new HashMap<>();
                        // 设置基本信息guid
                        filter.put("certinfoguid", rowGuid);
                        MongodbUtil mongodbUtil = getMongodbUtil();
                        String extensionGuid = mongodbUtil.find(CertInfoExtension.class, filter, false).getRowguid();
                        certInfoExtension.setRowguid(extensionGuid);
                        mongodbUtil.update(certInfoExtension);
                    }
                    //时间延长一秒，防止日志记录在查询前面
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND, 1);
                    // 增加操作日志
                    new CertLogBizlogic().addExternalLog(appkey, "政务综管平台或政务门户网站", certInfo,
                            CertConstant.LOG_OPERATETYPE_ADD, "submitCertInfo", "", calendar.getTime());
                    return "保存成功！";
                }
                else {
                    // 获取当前可用的证照信息
                    CertInfo useCertInfo = certInfoService.getUseCertInfoByCertId(oldCertInfo.getCertid());
                    if (useCertInfo != null) {
                        // 更新实体信息,这边证照类和非证照类统一处理。都生成版本，前面版本设置为历史版本
                        updateCertInfo(useCertInfo, certInfo);

                        String newrowguid = UUID.randomUUID().toString();
                        certInfo.setRowguid(newrowguid);
                        certInfo.setVersiondate(new Date());
                        certInfo.setOperatedate(new Date());
                        certInfo.setCertcliengguid(newCliengGuid);
                        certInfo.setCopycertcliengguid(newCopyCliengGuid);
                        certInfo.setVersion(useCertInfo.getVersion() + 1);
                        certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                        certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                        certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                        // 设置材料类型
                        certInfo.setMaterialtype(materialtype);
                        // 设置材料来源
                        certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);
                        // 传递了附件则复制
                        if (hasAttach) {
                            attachService.copyAttachByClientGuid(cliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCliengGuid);
                        }

                        if (hasCopyAttach) {
                            attachService.copyAttachByClientGuid(copycliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCopyCliengGuid);
                        }

                        ICertCatalog certCatalogService = ContainerFactory.getContainInfo()
                                .getComponent(ICertCatalog.class);
                        CertCatalog certCatalog = certCatalogService
                                .getCatalogDetailByrowguid(certInfo.getCertareaguid());
                        //多持有人处理
                        if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())
                                && StringUtil.isNotBlank(certInfo.getCertownerno())) {
                            String[] certownerno = certInfo.getCertownerno().split("\\^");
                            String ownername = "";
                            if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                                ownername = certInfo.getCertownername();
                            }
                            String[] certownername = ownername.split("\\^");
                            String ownertype = "";
                            if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                                ownertype = certInfo.getCertownercerttype();
                            }
                            String[] certownercerttype = ownertype.split("\\^");
                            for (int i = 0; i < certownerno.length; i++) {
                                CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                                certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                                certOwnerInfo.setCertownerno(certownerno[i]);
                                if (certownername.length > i) {
                                    certOwnerInfo.setCertownername(certownername[i]);
                                }
                                if (certownercerttype.length > i) {
                                    certOwnerInfo.setCertownercerttype(certownercerttype[i]);
                                }
                                certOwnerInfo.setCertinfoguid(certInfo.getRowguid());
                                ownerInfoService.addCertOwnerInfo(certOwnerInfo);
                            }
                            if (!certInfo.getCertownerno().endsWith("^")) {
                                certInfo.setCertownerno(certInfo.getCertownerno() + "^");
                            }
                        }
                        // 证照类型代码
                        certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                        //设置证照标识
                        certInfo.setCertificateidentifier(
                                certInfoBizlogic.generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                        certInfoService.addCertInfo(certInfo);

                        MongodbUtil mongodbUtil = getMongodbUtil();
                        //没有拓展信息则取可用版本的拓展信息
                        if (certInfoExtension == null) {
                            // 获得照面信息
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", useCertInfo.getRowguid());
                            certInfoExtension = mongodbUtil.find(CertInfoExtension.class, filter, false);
                        }

                        // 更新照面信息
                        if (certInfoExtension != null) {
                            certInfoExtension.setRowguid(UUID.randomUUID().toString());
                            certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                            mongodbUtil.update(certInfoExtension);
                        }

                        // 上一个版本变更为历史
                        useCertInfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                        certInfoService.updateCertInfo(useCertInfo);

                        //时间延长一秒，防止日志记录在查询前面
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.SECOND, 1);
                        // 增加操作日志
                        new CertLogBizlogic().addExternalLog("", "政务综管平台或政务门户网站", useCertInfo,
                                CertConstant.LOG_OPERATETYPE_MODIFY, "submitCertInfo", "", calendar.getTime());
                        return newrowguid;
                    }
                    else {
                        return "数据异常，保存失败！";
                    }
                }
            }
            // 不存在对应版本
            else {
                ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
                CertCatalog certCatalog = null;
                if (StringUtil.isNotBlank(certInfo.getCertareaguid())) {
                    certCatalog = certCatalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
                }
                else {
                    certCatalog = certCatalogService.getLatestCatalogDetailByCatalogid(certInfo.getCertcatalogid());
                }
                if (certCatalog == null) {
                    return "数据异常，保存失败！";
                }

                // 证照类判断下编号
                if (CertConstant.CONSTANT_STR_ONE.equals(materialtype) && StringUtil.isNotBlank(certInfo.getCertno())) {
                    if (certInfoService.isExistCertno(certInfo.getCertcatalogid(), certInfo.getCertno())) {
                        return "证照编号重复！";
                    }
                }

                //多持有人处理
                if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())
                        && StringUtil.isNotBlank(certInfo.getCertownerno())) {
                    String[] certownerno = certInfo.getCertownerno().split("\\^");
                    String ownername = "";
                    if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                        ownername = certInfo.getCertownername();
                    }
                    String[] certownername = ownername.split("\\^");
                    String ownertype = "";
                    if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                        ownertype = certInfo.getCertownercerttype();
                    }
                    String[] certownercerttype = ownertype.split("\\^");
                    for (int i = 0; i < certownerno.length; i++) {
                        CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                        certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                        certOwnerInfo.setCertownerno(certownerno[i]);
                        if (certownername.length > i) {
                            certOwnerInfo.setCertownername(certownername[i]);
                        }
                        if (certownercerttype.length > i) {
                            certOwnerInfo.setCertownercerttype(certownercerttype[i]);
                        }
                        certOwnerInfo.setCertinfoguid(certInfo.getRowguid());
                        ownerInfoService.addCertOwnerInfo(certOwnerInfo);
                    }
                    if (!certInfo.getCertownerno().endsWith("^")) {
                        certInfo.setCertownerno(certInfo.getCertownerno() + "^");
                    }
                }

                //判断目录是否是多实例数据
                if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsoneCertInfo())
                        && !CertConstant.CERT_LEVEL_C.equals(certInfo.getCertlevel())) {
                    // 不允许多实例且不是C类证照，通过持有人证件号码，目录唯一标识，持有人类型来判断
                    SqlUtils sql = new SqlUtils();
                    sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                    sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                    sql.eq("certownertype", certInfo.getCertownertype());
                    List<CertInfo> readycertinfos = certInfoService.getCertInfoListMultiOwner(sql.getMap(),
                            certCatalog.getCertcatalogid(), certInfo.getCertownerno(), null);

                    if (readycertinfos != null && readycertinfos.size() > 0) {
                        CertInfo readycertinfo = readycertinfos.get(0);

                        // 更新实体信息,这边证照类和非证照类统一处理。都生成版本，前面版本设置为历史版本
                        updateCertInfo(readycertinfo, certInfo);

                        //TODO
                        // 政务综管平台或政务门户网站系统提交，默认清除掉缩率图关系
                        certInfo.setSltimagecliengguid("");
                        certInfo.setVersiondate(new Date());
                        certInfo.setOperatedate(new Date());
                        certInfo.setCertcliengguid(newCliengGuid);
                        certInfo.setCopycertcliengguid(newCopyCliengGuid);
                        certInfo.setVersion(readycertinfo.getVersion() + 1);
                        certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                        // 设置材料类型
                        certInfo.setMaterialtype(materialtype);
                        // 设置材料来源
                        certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);
                        // 传递了附件则复制
                        if (hasAttach) {
                            attachService.copyAttachByClientGuid(cliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCliengGuid);
                        }

                        if (hasCopyAttach) {
                            attachService.copyAttachByClientGuid(copycliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCopyCliengGuid);
                        }
                        // 证照类型代码
                        certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                        //设置证照标识
                        certInfo.setCertificateidentifier(
                                certInfoBizlogic.generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                        certInfoService.addCertInfo(certInfo);

                        MongodbUtil mongodbUtil = getMongodbUtil();
                        //没有拓展信息则取可用版本的拓展信息
                        if (certInfoExtension == null) {
                            // 获得照面信息
                            Map<String, Object> filter = new HashMap<>();
                            // 设置基本信息guid
                            filter.put("certinfoguid", readycertinfo.getRowguid());
                            certInfoExtension = mongodbUtil.find(CertInfoExtension.class, filter, false);
                        }

                        // 更新照面信息
                        if (certInfoExtension != null) {
                            certInfoExtension.setRowguid(UUID.randomUUID().toString());
                            certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                            mongodbUtil.update(certInfoExtension);
                        }

                        // 上一个版本变更为历史
                        readycertinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                        certInfoService.updateCertInfo(readycertinfo);

                        //时间延长一秒，防止日志记录在查询前面
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.SECOND, 1);
                        // 增加操作日志
                        new CertLogBizlogic().addExternalLog("", "政务综管平台或政务门户网站", readycertinfo,
                                CertConstant.LOG_OPERATETYPE_MODIFY, "submitCertInfo", "", calendar.getTime());
                        return "变更版本成功！";

                    }
                    else {
                        //默认新增一条数据

                        certInfo.setCertid(UUID.randomUUID().toString());
                        //业务证照目录
                        certInfo.setCertcatcode(certCatalog.getCertcatcode());
                        //通用证照目录
                        certInfo.setTycertcatcode(certCatalog.getTycertcatcode());
                        // 证照类型代码
                        certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                        //证照目录的版本
                        certInfo.setCertcatalogversion(certCatalog.getVersion());
                        //关联区域 证照目录的唯一标志
                        certInfo.setCertareaguid(certCatalog.getRowguid());
                        //关联证照目录唯一版本标识
                        certInfo.setCertcatalogid(certCatalog.getCertcatalogid());

                        certInfo.setVersiondate(new Date());
                        certInfo.setVersion(CertConstant.CONSTANT_INT_ONE);
                        certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                        if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsparent())) {
                            certInfo.setParentcertcatalogid(certCatalog.getParentid());
                        }
                        certInfo.setAddusername("政务综管平台或政务门户网站新增");
                        certInfo.setOperatedate(new Date());
                        certInfo.setCertcliengguid(newCliengGuid);
                        certInfo.setCopycertcliengguid(newCopyCliengGuid);
                        certInfo.setIscreatecert(ConvertUtil.convertBooleanToInteger(hasAttach));
                        // 传递了附件则复制
                        if (hasAttach) {
                            attachService.copyAttachByClientGuid(cliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCliengGuid);
                        }

                        if (hasCopyAttach) {
                            attachService.copyAttachByClientGuid(copycliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                    newCopyCliengGuid);
                        }
                        certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                        certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                        // 设置材料类型
                        certInfo.setMaterialtype(materialtype);
                        // 设置材料来源
                        certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);
                        if (StringUtil.isNotBlank(certInfo.getOuguid())) {
                            ICertCatalogOu certCatalogOuService = ContainerFactory.getContainInfo()
                                    .getComponent(ICertCatalogOu.class);
                            CertCatalogOu certCatalogOu = certCatalogOuService.getCertCataLogOuByCatalogid("*", false,
                                    certInfo.getOuguid(), certCatalog.getCertcatalogid());
                            if (certCatalogOu != null) {
                                certInfo.setCertawarddeptcode(certCatalogOu.getCertawarddeptcode());
                            }
                        }
                        // 电子证照编号
                        certInfo.setCertinfocode(new CertInfoBizlogic().generateCertinfoCode(certInfo));
                        //设置证照标识
                        certInfo.setCertificateidentifier(
                                certInfoBizlogic.generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                        certInfoService.addCertInfo(certInfo);
                        // 新增照面信息
                        if (certInfoExtension != null) {
                            certInfoExtension.setRowguid(UUID.randomUUID().toString());
                            certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                            getMongodbUtil().insert(certInfoExtension);
                        }
                        else {
                            CertInfoExtension extension = new CertInfoExtension();
                            extension.setRowguid(UUID.randomUUID().toString());
                            extension.setCertinfoguid(certInfo.getRowguid());
                            getMongodbUtil().insert(extension);
                        }

                        //时间延长一秒，防止日志记录在查询前面
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.SECOND, 1);
                        // 增加操作日志
                        new CertLogBizlogic().addExternalLog("", "政务综管平台或政务门户网站", certInfo,
                                CertConstant.LOG_OPERATETYPE_ADD, "submitCertInfo", "", calendar.getTime());

                        return "新增成功！";
                    }

                }
                else {                   
                    //允许多实例证照数据
                    certInfo.setCertid(UUID.randomUUID().toString());
                    //业务证照目录
                    certInfo.setCertcatcode(certCatalog.getCertcatcode());
                    //通用证照目录
                    certInfo.setTycertcatcode(certCatalog.getTycertcatcode());
                    // 证照类型代码
                    certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                    //证照目录的版本
                    certInfo.setCertcatalogversion(certCatalog.getVersion());
                    //关联区域 证照目录的唯一标志
                    certInfo.setCertareaguid(certCatalog.getRowguid());
                    //关联证照目录唯一版本标识
                    certInfo.setCertcatalogid(certCatalog.getCertcatalogid());

                    certInfo.setVersiondate(new Date());
                    certInfo.setVersion(CertConstant.CONSTANT_INT_ONE);
                    certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                    if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsparent())) {
                        certInfo.setParentcertcatalogid(certCatalog.getParentid());
                    }
                    certInfo.setAddusername("政务综管平台或政务门户网站新增");
                    certInfo.setOperatedate(new Date());
                    certInfo.setCertcliengguid(newCliengGuid);
                    certInfo.setCopycertcliengguid(newCopyCliengGuid);
                    certInfo.setIscreatecert(ConvertUtil.convertBooleanToInteger(hasAttach));
                    // 传递了附件则复制
                    if (hasAttach) {
                        attachService.copyAttachByClientGuid(cliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交", newCliengGuid);
                    }

                    if (hasCopyAttach) {
                        attachService.copyAttachByClientGuid(copycliengGuid, "dzzzglxt", "政务综管平台或政务门户网站提交",
                                newCopyCliengGuid);
                    }
                    certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                    certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                    // 设置材料类型
                    certInfo.setMaterialtype(materialtype);
                    // 设置材料来源
                    certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);
                    if (StringUtil.isNotBlank(certInfo.getOuguid())) {
                        ICertCatalogOu certCatalogOuService = ContainerFactory.getContainInfo()
                                .getComponent(ICertCatalogOu.class);
                        CertCatalogOu certCatalogOu = certCatalogOuService.getCertCataLogOuByCatalogid("*", false,
                                certInfo.getOuguid(), certCatalog.getCertcatalogid());
                        if (certCatalogOu != null) {
                            certInfo.setCertawarddeptcode(certCatalogOu.getCertawarddeptcode());
                        }
                    }
                    // 电子证照编号
                    certInfo.setCertinfocode(new CertInfoBizlogic().generateCertinfoCode(certInfo));
                    //设置证照标识
                    certInfo.setCertificateidentifier(
                            certInfoBizlogic.generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                    certInfoService.addCertInfo(certInfo);
                    // 新增照面信息
                    if (certInfoExtension != null) {
                        certInfoExtension.setRowguid(UUID.randomUUID().toString());
                        certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                        getMongodbUtil().insert(certInfoExtension);
                    }
                    else {
                        CertInfoExtension extension = new CertInfoExtension();
                        extension.setRowguid(UUID.randomUUID().toString());
                        extension.setCertinfoguid(certInfo.getRowguid());
                        getMongodbUtil().insert(extension);
                    }

                    //时间延长一秒，防止日志记录在查询前面
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND, 1);
                    // 增加操作日志
                    new CertLogBizlogic().addExternalLog("", "政务综管平台或政务门户网站", certInfo,
                            CertConstant.LOG_OPERATETYPE_ADD, "submitCertInfo", "", calendar.getTime());
                    return "新增成功！";
                }
            }
        }
    }

    @Override
    public void updateCert(String appkey, String rowGuid, String certcCliengguid, String copyCertcliengguid) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertRest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertRest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();

            // 对应rest接口地址
            String updateCert = asRestUrl + "/certInfo/updateCert";
            // 拼接参数
            String newcertcliengguid = UUID.randomUUID().toString();
            String newcopycertcliengguid = UUID.randomUUID().toString();
            //证照基本信息
            jsonObject.put("rowguid", rowGuid);
            jsonObject.put("certcliengguid", newcertcliengguid);
            jsonObject.put("copycertcliengguid", newcopycertcliengguid);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(updateCert, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    Map<String, String> cliengguids = new ExternalListener().getCliengguidsMap(rowGuid, appkey);
                    //照面信息中的附件guid加入certcCliengguid
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> entry : cliengguids.entrySet()) {
                        sb.append(";" + entry.getValue());
                    }
                    certcCliengguid += sb.toString();
                    jsonObject.clear();
                    jsonObject.put("certcliengguid", certcCliengguid);
                    jsonObject.put("copycertcliengguid", copyCertcliengguid);
                    jsonObject.put("oldcertcliengguid", certcCliengguid);
                    jsonObject.put("oldcopycertcliengguid", copyCertcliengguid);
                    jsonObject.put("appkey", appkey);
                    String attachurl = asRestUrl + "/certAttach/addAttach";
                    jsonObject.put("url", attachurl);
                    if (CertConstant.CONSTANT_STR_ONE.equals(configService.getFrameConfigValue("Cert_NotUseMQ"))) {
                        executorService.execute(new Runnable()
                        {
                            @Override
                            public void run() {
                                new ExternalListener().handleAttach(attachurl, jsonObject.getString("certcliengguid"),
                                        jsonObject.getString("copycertcliengguid"),
                                        jsonObject.getString("oldcertcliengguid"),
                                        jsonObject.getString("oldcopycertcliengguid"), appkey);
                            }
                        });
                    }
                    else {
                        MQSender.send(CertConstant.QUEUENAME, jsonObject);
                    }
                }
            }
        }
        else {
            ICertInfo certInfoService = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            CertInfo certInfo = certInfoService.getCertInfoByRowguid(rowGuid);
            //默认是新增类型，当为单证照并且证件唯一编号下已经存在实例，就版本加1，类型为变更
            String logtype = CertConstant.LOG_OPERATETYPE_ADD;
            if (certInfo != null) {
                String oldcliengGuid = certInfo.getCertcliengguid();
                String oldcopycliengGuid = certInfo.getCopycertcliengguid();
                boolean isrest = true;
                //老的guid等于新的guid说明直接走的本地，不是rest模式，此时需要产生新的guid并复制附件。如果走的rest不复制，避免异步生成证照时附件还未生成。
                if (oldcliengGuid != null && oldcliengGuid.equals(certcCliengguid)) {
                    certcCliengguid = UUID.randomUUID().toString();
                    certInfo.setCertcliengguid(certcCliengguid);
                    isrest = false;
                }
                if (oldcopycliengGuid != null && oldcopycliengGuid.equals(copyCertcliengguid)) {
                    copyCertcliengguid = UUID.randomUUID().toString();
                    certInfo.setCopycertcliengguid(copyCertcliengguid);
                }
                IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);

                certInfo.setVersiondate(new Date());
                certInfo.setOperatedate(new Date());

                // 传递了附件则复制
                if (attachService.getAttachCountByClientGuid(oldcliengGuid) > 0) {
                    if (!isrest) {
                        attachService.copyAttachByClientGuid(oldcliengGuid, "", "", certcCliengguid);
                    }
                    certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ONE);
                }
                else {
                    certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
                }

                if (attachService.getAttachCountByClientGuid(oldcopycliengGuid) > 0) {
                    if (!isrest) {
                        attachService.copyAttachByClientGuid(oldcopycliengGuid, "", "", copyCertcliengguid);
                    }
                }
                certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
                certInfo.setIshistory(CertConstant.CONSTANT_INT_ZERO);
                // 设置材料类型
                certInfo.setMaterialtype(CertConstant.MATERIAL_ZZ);
                // 设置材料来源
                certInfo.setMaterialsource(CertConstant.CONSTANT_STR_TWO);

                ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
                CertCatalog certCatalog = certCatalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
                //单证照的情况
                if (certCatalog != null && CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsoneCertInfo())) {
                    //TODO 多持有人 暂时用循环处理
                    if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())) {
                        for (String no : certInfo.getCertownerno().split("\\^")) {
                            SqlUtils sql = new SqlUtils();
                            sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                            sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                            sql.eq("certownertype", certInfo.getCertownertype());
                            //排除本身这条记录
                            if (StringUtil.isNotBlank(certInfo.getRowguid())) {
                                sql.nq("rowguid", certInfo.getRowguid());
                            }
                            List<CertInfo> certInfos = certInfoService.getCertInfoListMultiOwner(sql.getMap(),
                                    certInfo.getCertcatalogid(), no , null);
                            if (certInfos.size() > 0) {
                                CertInfo oldCertinfo = certInfos.get(0);
                                certInfo.setCertid(oldCertinfo.getCertid());
                                certInfo.setVersion(oldCertinfo.getVersion() + 1);
                                oldCertinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                                certInfoService.updateCertInfo(oldCertinfo);
                                logtype = CertConstant.LOG_OPERATETYPE_MODIFY;
                                break;
                            }
                        }
                    }
                    else {
                        SqlUtils sql = new SqlUtils();
                        sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                        sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                        sql.eq("certownertype", certInfo.getCertownertype());
                        //排除本身这条记录
                        if (StringUtil.isNotBlank(certInfo.getRowguid())) {
                            sql.nq("rowguid", certInfo.getRowguid());
                        }
                        List<CertInfo> certInfos = certInfoService.getCertInfoListMultiOwner(sql.getMap(),
                                certInfo.getCertcatalogid(), certInfo.getCertownerno(), null);
                        if (certInfos.size() > 0) {
                            CertInfo oldCertinfo = certInfos.get(0);
                            certInfo.setCertid(oldCertinfo.getCertid());
                            certInfo.setVersion(oldCertinfo.getVersion() + 1);
                            oldCertinfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                            certInfoService.updateCertInfo(oldCertinfo);
                            logtype = CertConstant.LOG_OPERATETYPE_MODIFY;
                        }
                    }
                }
                ICertOwnerInfo ownerInfoService = ContainerFactory.getContainInfo().getComponent(ICertOwnerInfo.class);
                //多持有人处理
                if (CertConstant.CONSTANT_INT_ONE.equals(certCatalog.getIsmultiowners())
                        && StringUtil.isNotBlank(certInfo.getCertownerno())) {
                    String[] certownerno = certInfo.getCertownerno().split("\\^");
                    String ownername = "";
                    if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                        ownername = certInfo.getCertownername();
                    }
                    String[] certownername = ownername.split("\\^");
                    String ownertype = "";
                    if (StringUtil.isNotBlank(certInfo.getCertownername())) {
                        ownertype = certInfo.getCertownercerttype();
                    }
                    String[] certownercerttype = ownertype.split("\\^");
                    for (int i = 0; i < certownerno.length; i++) {
                        CertOwnerInfo certOwnerInfo = new CertOwnerInfo();
                        certOwnerInfo.setRowguid(UUID.randomUUID().toString());
                        certOwnerInfo.setCertownerno(certownerno[i]);
                        if (certownername.length > i) {
                            certOwnerInfo.setCertownername(certownername[i]);
                        }
                        if (certownercerttype.length > i) {
                            certOwnerInfo.setCertownercerttype(certownercerttype[i]);
                        }
                        certOwnerInfo.setCertinfoguid(certInfo.getRowguid());
                        ownerInfoService.addCertOwnerInfo(certOwnerInfo);
                    }
                    if (!certInfo.getCertownerno().endsWith("^")) {
                        certInfo.setCertownerno(certInfo.getCertownerno() + "^");
                    }
                }
                certInfo.setCertificatetypecode(certCatalog.getCertificatetypecode());
                certInfo.setCertificateidentifier(
                        new CertInfoBizlogic().generateIdentifier(certInfo, certCatalog.getCertificatetypecode()));
                certInfoService.updateCertInfo(certInfo);
                // 增加操作日志
                new CertLogBizlogic().addExternalLog("", "政务综管平台或政务门户网站", certInfo, logtype, "updateCert", "", null);
            }
        }
    }

    /**
     * 验证证照目录
     *  @return
     */
    private String validateCatalog(CertCatalog certCatalog) {
        String msg = "";
        if (certCatalog == null) {
            msg = "当前证照对应的证照类型不存在或未启用，操作失败！";
        }
        else if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsenable())) {
            msg = "当前证照对应的证照类型未启用，操作失败！";
        }
        return msg;
    }

    /**
     * 生成证照接口
     * 若元数据中存在附件，生成新的clienggguid，调用addAttach接口插入附件，然后调用生成证照接口（generateCertNoCheck），同时
     * 传递新生成的clienggguid。在generateCertNoCheck中用新的clienggguid替换certinfoextension中原本的clienggguid（certinfoextension
     * 不保存），生成证照，成功以后将新clienggguid对应的附件删除
     *
     * @param certInfo
     * @param certInfoExtension
     * @param isGenerateCopy
     * @param appkey
     * @return
     */
    @Override
    public String generateCert(CertInfo certInfo, CertInfoExtension certInfoExtension, String isGenerateCopy,
            String appkey) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        //正式系统配置的0
        //使用rest接口
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String generateCert = asRestUrl + "/certInfo/checkAndSave";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            //证照基本信息
            jsonObject.put("certinfo", JsonUtil.objectToJson(certInfo));
            //照面信息
            jsonObject.put("certinfoextension", JsonUtil.objectToJson(certInfoExtension));
            //是否生成副本
            jsonObject.put("isgeneratecopy", isGenerateCopy);
            paramsToken.put("appkey", appkey);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(generateCert, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("success"))) {
                        //检查通过发送消息
                        String generateCertNoCheck = asRestUrl + "/certInfo/generateCertNoCheck";
                        jsonObject.clear();
                        paramsToken.clear();
                        jsonObject.put("rowguid", certInfo.getRowguid());
                        jsonObject.put("isgeneratecopy", isGenerateCopy);
                        paramsToken.put("params", jsonObject);
                        JSONObject mqObject = new JSONObject();
                        mqObject.put("url", generateCertNoCheck);
                        mqObject.put("params", paramsToken);
                        mqObject.put("appkey", appkey);

                        if (CertConstant.CONSTANT_STR_ONE.equals(configService.getFrameConfigValue("Cert_NotUseMQ"))) {
                            executorService.execute(new Runnable()
                            {
                                @Override
                                public void run() {
                                    Map<String, String> cliengguids = new ExternalListener()
                                            .getCliengguidsMap(certInfo.getRowguid(), appkey);
                                    //若存在附件照面信息
                                    if (!cliengguids.isEmpty()) {
                                        String certcCliengguids = "";
                                        StringBuilder oldCertcCliengguids = new StringBuilder();
                                        JSONObject jsonCliengguids = new JSONObject();
                                        for (Map.Entry<String, String> entry : cliengguids.entrySet()) {
                                            jsonCliengguids.put(entry.getKey(), UUID.randomUUID().toString());
                                            certcCliengguids += jsonCliengguids.getString(entry.getKey()) + ";";
                                            oldCertcCliengguids.append(entry.getValue() + ";");
                                        }
                                        String attachurl = asRestUrl + "/certAttach/addAttach";
                                        //传递附件
                                        new ExternalListener().handleAttach(attachurl, certcCliengguids, "",
                                                oldCertcCliengguids.toString(), "", appkey);
                                        //参数中加入 元数据名称:新cliengguid 的json数据
                                        jsonObject.put("certcliengguids", jsonCliengguids.toString());
                                    }
                                    //如果超时报错 配置文件中设置 httpclient.maxtimeout 超时时间
                                    HttpUtil.doPostJson(generateCertNoCheck, paramsToken.toString());
                                }
                            });
                        }
                        else {
                            MQSender.send(CertConstant.QUEUENAME, mqObject);
                        }
                    }
                    return objReturn.getJSONObject("custom").getString("msg");
                }
            }
            return "证照生成失败!";
        }
        else {
            Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
            ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            IOuServiceInternal iOuService = ContainerFactory.getContainInfo().getComponent(IOuServiceInternal.class);
            ICertCatalogOu catalogOuService = ContainerFactory.getContainInfo().getComponent(ICertCatalogOu.class);
            CommonDao commonDao = CommonDao.getInstance();
//            IOuService iOuService1 = ContainerFactory.getContainInfo().getComponent(IOuService.class);
//            IHandleFrameOU iHandleFrameOU = ContainerFactory.getContainInfo().getComponent(IHandleFrameOU.class);
            String ouguid = null;
//            FrameOu frameOu= iOuService.getOuByOuField("orgcode",certInfo.getOuguid());
//            FrameOuExtendInfo frameOuExtendInfo = iHandleFrameOU
//            if(frameOu!=null){
//                ouguid = frameOu.getOuguid();
//            }
            if (StringUtil.isBlank(ouguid)) {
                ouguid = certInfo.getOuguid();
            }
            FrameOuExtendInfo frameOuExten = iOuService.getFrameOuExtendInfo(ouguid);
            if (frameOuExten == null) {
                String sql = "select * from Frame_OU_ExtendInfo where orgcode=?";
                frameOuExten = commonDao.find(sql,FrameOuExtendInfo.class,certInfo.getOuguid());
                if(frameOuExten!=null){
                    ouguid=frameOuExten.getOuGuid();
                }
            }

            CertCatalogOu catalogOu = null;
            if(StringUtil.isNotBlank(ouguid)){
                catalogOu = catalogOuService.getCertCataLogOuByCatalogid("writeauthority, ouname", false,
                        ouguid, certInfo.getCertcatalogid());
            }
            if (CertConstant.CONSTANT_STR_ONE.equals(certInfo.getMaterialtype()) && catalogOu == null
                    || !CertConstant.CONSTANT_INT_ONE.equals(catalogOu.getWriteauthority())) {
                return "该部门没有证照类型的写权限，请联系管理员！";
            }
            CertCatalog certCatalog = certCatalogService.getCatalogDetailByrowguid(certInfo.getCertareaguid());
            log.info("certInfo.getCertareaguid():"+certInfo.getCertareaguid());
            log.info("certCatalog:"+certCatalog.getRowguid());
            // 判断目录是否开启
            String catalogMsg = validateCatalog(certCatalog);
            certInfo.setAreacode(appkey);
            if (StringUtil.isNotBlank(catalogMsg)) {
                return catalogMsg;
            }

            // 子表非空校验
            boolean isSubExtensionValidatePass = true;
            Set<String> subExtensionNameSet = new HashSet<>();
            // 查出所有父元数据（配置子表）
            ICertMetaData certMetaDataService = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
            List<CertMetadata> parentMetadataList = certMetaDataService
                    .selectParentDispinListByCertguid(certCatalog.getRowguid());
            if (ValidateUtil.isNotBlankCollection(parentMetadataList)) {
                for (CertMetadata parentMetadata : parentMetadataList) {
                    if (CertConstant.CONSTANT_STR_ONE.equals(parentMetadata.getNotnull())) {
                        List<Record> recordList = null;
                        String subextensionList = certInfoExtension.getStr(parentMetadata.getFieldname());
                        try {
                            if (StringUtil.isNotBlank(subextensionList)) {
                                recordList = JsonUtil.jsonToList(subextensionList, Record.class);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            log.error(String.format("获取子拓展信息失败！JSON转换错误[subextensionList = %s]", subextensionList));
                        }
                        if (ValidateUtil.isBlankCollection(recordList)) {
                            isSubExtensionValidatePass = false;
                            subExtensionNameSet.add(parentMetadata.getFieldchinesename());
                        }
                    }
                }
            }

            // 子表校验
            if (!isSubExtensionValidatePass) {
                return String.format("子表%s不能为空，生成证照失败！", subExtensionNameSet.toString());
            }

            // 模板文件guid
            String cliengGuid = certCatalog.getTempletcliengguid();
            boolean isCopy = false;

            if (CertConstant.CONSTANT_STR_ONE.equals(isGenerateCopy)) { // 生成副本
                cliengGuid = certCatalog.getCopytempletcliengguid();
                log.info("cliengGUid"+cliengGuid);
                isCopy = true;
                // 删除打印附件（副本）
                if (StringUtil.isNotBlank(certInfo.getCopyprintdocguid())) {
                    attachService.deleteAttachByAttachGuid(certInfo.getCopyprintdocguid());
                }
                // 清空打印附件guid
                certInfo.setCopyprintdocguid(null);
            }
            else {
                // 删除打印附件（正本）
                if (StringUtil.isNotBlank(certInfo.getPrintdocguid())) {
                    attachService.deleteAttachByAttachGuid(certInfo.getPrintdocguid());
                }
                // 清空打印附件guid
                certInfo.setPrintdocguid(null);
            }

            String msg = saveCertinfo(certInfo, certInfoExtension, appkey);
            if (!msg.contains("成功")) {
                return msg;
            }
            GenerateBizlogic generateBizlogic = new GenerateBizlogic();
            ICertMetaData metaDataService = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
            List<CertMetadata> metadataList = metaDataService.selectTopDispinListByCertguid(certInfo.getCertareaguid());
            // 获取word域名数组和域值数组
            Map<String, Object> wordMap = generateBizlogic.getWordInfo(metadataList, certInfoExtension);
            // word域名数组
            String[] fieldNames = (String[]) wordMap.get("fieldNames");
            // word域值数组
            Object[] values = (Object[]) wordMap.get("values");
            // 图片map
            @SuppressWarnings("unchecked")
            Map<String, Record> imageMap = (Map<String, Record>) wordMap.get("imageMap");
            // 子表数据
            DataSet dataSet = (DataSet) wordMap.get("dataSet");
            // 生成正/副本
            log.info("生成正/副本: cliengGUid"+cliengGuid);
            Map<String, String> returnMap = generateBizlogic.generateLicenseSupportCopy(cliengGuid, isCopy,
                    UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(), fieldNames,
                    values, imageMap, dataSet, certInfo);
            // 设置提示信息
            return returnMap.get("msg");
        }
    }

    @Override
    public String saveCertinfo(CertInfo certInfo, CertInfoExtension certInfoExtension, String appkey) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        //使用rest接口
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return "证照库rest对接地址未配置";
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();

            // 对应rest接口地址
            String saveCertinfo = asRestUrl + "/certInfo/saveCertinfo";
            // 拼接参数
            //证照基本信息
            jsonObject.put("certinfo", JsonUtil.objectToJson(certInfo));
            //照面信息
            jsonObject.put("certinfoextension", JsonUtil.objectToJson(certInfoExtension));
            paramsToken.put("appkey", appkey);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(saveCertinfo, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    return objReturn.getJSONObject("custom").getString("msg");
                }
            }
            return "保存失败";
        }
        else {
            certInfo.setAreacode(appkey);
            String rowGuid = certInfo.getRowguid();
            if (StringUtil.isBlank(rowGuid)) {
                rowGuid = UUID.randomUUID().toString();
                certInfo.setRowguid(rowGuid);
            }
            ICertInfo certInfoService = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            CertInfo oldCertInfo = certInfoService.getCertInfoByRowguid(certInfo.getRowguid());
            //存在对应版本
            if (oldCertInfo != null) {
                if (StringUtil.isNotBlank(certInfo.getCertno()) && StringUtil.isNotBlank(oldCertInfo.getCertno())) {
                    if (certInfoService.isExistCertno(certInfo.getCertcatalogid(), certInfo.getCertno())
                            && (!oldCertInfo.getCertno().equals(certInfo.getCertno()))) {
                        return "证照编号重复！";
                    }
                }
                    updateCertInfo(oldCertInfo, certInfo);
                    //忽略更新标记
                    certInfo.ignoreModifyFlag();
                    certInfoService.updateCertInfo(certInfo);

                    //更新照面信息
                    if (certInfoExtension != null) {
                        // 获得照面信息
                        Map<String, Object> filter = new HashMap<>();
                        // 设置基本信息guid
                        filter.put("certinfoguid", certInfo.getRowguid());
                        MongodbUtil mongodbUtil = getMongodbUtil();
                        String extensionGuid = UUID.randomUUID().toString();
                        CertInfoExtension oldExtension = mongodbUtil.find(CertInfoExtension.class, filter, false);
                        if (oldExtension != null) {
                            extensionGuid = certInfoExtension.getRowguid();
                        }
                        certInfoExtension.setRowguid(extensionGuid);
                        certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                        mongodbUtil.update(certInfoExtension);
                    }

                    return "保存成功！";
            }
            else {
                ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
                //获取证照目录
                CertCatalog certCatalog = certCatalogService
                        .getLatestCatalogDetailByCatalogid(certInfo.getCertcatalogid());
                if (certCatalog == null) {
                    return "数据异常，保存失败！";
                }

                // 不用验证
                /*if (certCatalog.getIsoneCertInfo() == CertConstant.CONSTANT_INT_ZERO) {
                    // 不允许多实例，通过持有人证件号码，目录唯一标识，持有人类型来判断
                    SqlUtils sql = new SqlUtils();
                    sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
                    sql.eq("status", CertConstant.CERT_STATUS_COMMON);
                    sql.eq("certcatalogid", certCatalog.getCertcatalogid());
                    sql.eq("certownerno",certInfo.getCertownerno());
                    sql.eq("certownertype",certInfo.getCertownertype());
                    //排除本身这条记录
                    if(StringUtil.isNotBlank(certInfo.getRowguid())){
                        sql.nq("rowguid",certInfo.getRowguid());
                    }
                
                    int oldcertinfocount= certInfoService.getCountByCondition(sql.getMap());
                    if(oldcertinfocount>0){
                        return "该目录下同一持有者类型、持有者证件号码，只能存在一条证照实例！";
                    }
                }*/
                //判断证照编号是否重复
                if (StringUtil.isNotBlank(certInfo.getCertno())) {
                    if (certInfoService.isExistCertno(certInfo.getCertcatalogid(), certInfo.getCertno())) {
                        return "证照编号重复！";
                    }
                }
                certInfo.setCertid(UUID.randomUUID().toString());
                certInfo.setCertcatcode(certCatalog.getCertcatcode());
                certInfo.setCertareaguid(certCatalog.getRowguid());
                certInfo.setVersion(CertConstant.CONSTANT_INT_ONE);
                //该状态系统不可见且排除单证照检查
                certInfo.setIshistory(CertConstant.CONSTANT_INT_ONE);
                certInfo.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_REPORT);
                if (CertConstant.CONSTANT_INT_ZERO.equals(certCatalog.getIsparent())) {
                    certInfo.setParentcertcatalogid(certCatalog.getParentid());
                }
                certInfo.setAddusername("政务综管平台或政务门户网站");
                certInfo.setOperatedate(new Date());
                certInfo.setIscreatecert(CertConstant.CONSTANT_INT_ZERO);
                certInfo.setStatus(CertConstant.CERT_STATUS_COMMON);
                certInfoService.addCertInfo(certInfo);
                //新增照面信息
                if (certInfoExtension != null) {
                    certInfoExtension.setRowguid(UUID.randomUUID().toString());
                    certInfoExtension.setCertinfoguid(certInfo.getRowguid());
                    getMongodbUtil().insert(certInfoExtension);
                }
                return "保存成功！";
            }
        }
    }

    /**
     * 更新实体的信息
     *
     * @param oldCertInfo
     *            老的实体
     * @param newCertInfo
     *            新的实体
     */
    private void updateCertInfo(CertInfo oldCertInfo, CertInfo newCertInfo) {
        for (Entry<String, Object> entry : oldCertInfo.entrySet()) {
            if (entry.getValue() instanceof String) {
                // 新的证照信息字段为空且不是修改，则由老的覆盖
                if (StringUtil.isBlank(newCertInfo.getStr(entry.getKey()))
                        && !newCertInfo.getModifyFlag().contains(entry.getKey())) {
                    newCertInfo.set(entry.getKey(), entry.getValue());
                }
            }
            else {
                if (newCertInfo.get(entry.getKey()) == null) {
                    newCertInfo.set(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private List<CertInfo> handleIndividualCert(String certCatalogid, String certOwnerType, String certOwnerCertType,
            String certOwnerNo, String apiAddress, String certLevel, Map<String, String> extraParams) {
        List<CertInfo> certInfos = new ArrayList<>();
        if (StringUtil.isNotBlank(apiAddress) && ReflectUtil.exist(apiAddress)) {
            MongodbUtil mongodbUtil = getMongodbUtil();
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            ICertIndividual certIndividualService = (ICertIndividual) ReflectUtil.getObjByClassName(apiAddress);
            ICertInfo certInfoService = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
            CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();
            JSONArray jsonArray = certIndividualService.getCertContent(certCatalogid, certOwnerType, certOwnerCertType,
                    certOwnerNo, null, extraParams);
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
                        CertInfo oldCertinfo = certInfoService.getCertInfoByCertno(certInfo.getCertcatalogid(),
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
                            CertCatalog certCatalog = certCatalogService
                                    .getLatestCatalogDetailByCatalogid(certCatalogid);

                            // 设置证照基本信息
                            String oldCertcliengguid = "";
                            if (StringUtil.isBlank(certInfo.getCertcatalogid())) {
                                certInfo.setCertcatalogid(certCatalogid);
                            }
                            CertInfo oldCertInfo = certInfoService.getCertInfoByCertno(certInfo.getCertcatalogid(),
                                    certInfo.getCertno());
                            if (oldCertInfo != null) {
                                certInfoService.deleteCertInfoByRowguid(oldCertInfo.getRowguid());
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
                                certInfoService.deleteCertInfoByRowguid(oldCertInfo.getRowguid());
                            }
                            //设置证照标识
                            certInfo.setCertificateidentifier(certInfoBizlogic.generateIdentifier(certInfo,
                                    certCatalog.getCertificatetypecode()));
                            certInfoService.addCertInfo(certInfo);

                            // 设置证照拓展信息
                            CertInfoExtension certInfoExtension = certIndividualService.getCertInfoExtension(jsonObject,
                                    certInfo.getRowguid());
                            if (certInfoExtension != null) {
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
                                certInfoService.updateCertInfo(certInfo);
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

    @Override
    public Integer getCountByCatalogGuid(String certareaguid) {
        Integer cnt = 0;
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String getCountByCatalogGuid = asRestUrl + "/certInfo/getCountByCatalogGuid";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            jsonObject.put("certareaguid", certareaguid);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(getCountByCatalogGuid, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    cnt = objReturn.getJSONObject("custom").getInteger("count");
                }
            }
        }
        else {
            ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            SqlUtils sql = new SqlUtils();
            sql.in("certareaguid", certareaguid);
            cnt = iCertInfo.getCountByCondition(sql.getMap());
        }
        return cnt;
    }

    @Override
    public JSONObject getProveAttach(String certCatalogid, String certOwnerCertType, String certOwnerNo, String appKey,
            Map<String, String> extraParams) {
        JSONObject result = new JSONObject();
        Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {

                log.error("=============证照库rest对接地址未配置===========");
                return null;
            }
            // 对应rest接口地址
            String getProveAttach = asRestUrl + "/certInfo/getProveAttach";
            // 拼接参数
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();
            jsonObject.put("certcatalogid", certCatalogid);
            jsonObject.put("certownercerttype", certOwnerCertType);
            jsonObject.put("certownerno", certOwnerNo);
            jsonObject.put("extraparams", extraParams);
            paramsToken.put("appkey", appKey);
            paramsToken.put("params", jsonObject);

            try {
                URL url = new URL(getProveAttach);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                //写入参数
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(paramsToken.toJSONString());
                writer.close();
                os.close();
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream attachinputstream = connection.getInputStream();
                    if (connection.getHeaderField("Content-Disposition") == null) {
                        String msg = JSONObject.parseObject(IOUtils.toString(attachinputstream, "UTF-8"))
                                .getJSONObject("custom").getString("text");
                        log.error(msg);
                    }
                    else {
                        String filename = URLDecoder.decode(connection.getHeaderField("Content-Disposition"), "UTF-8")
                                .split("Filename=")[1];
                        /*AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), UUID.randomUUID().toString(), filename, ".pdf", "",
                                attachinputstream.available(), attachinputstream, "", "");*/
                        result.put("filename", filename);
                        result.put("inputstream", attachinputstream);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            Map<String, Object> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rowGuid", certCatalogid);
            jsonObject.put("credentialType", "certType_" + certOwnerCertType);
            jsonObject.put("credentialNum", certOwnerNo);
            params.put("json", jsonObject.toJSONString());
            getToken();
            if (StringUtil.isBlank(accessToken)) {
                return null;
            }
            headers.put("Authorization", "Bearer " + accessToken);
            String url = ConfigUtil.getConfigValue("bigDataServiceUrl");
            String returnMsg = HttpUtil.doPost(url + "/getInstanceData", params);
            JSONObject returnJson = JSONObject.parseObject(returnMsg);
            if (CertConstant.CONSTANT_STR_ONE.equals(returnJson.getString("success"))) {
                JSONArray fieldList = returnJson.getJSONArray("fieldList");
                List<String> nameList = new ArrayList<>();
                List<String> valueList = new ArrayList<>();
                for (Object field : fieldList) {
                    nameList.add(((JSONObject) field).getString("normName"));
                    valueList.add(((JSONObject) field).getString("fieldValue"));
                }
                String[] fieldNames = new String[nameList.size()];
                nameList.toArray(fieldNames);
                Object[] values = new Object[valueList.size()];
                valueList.toArray(values);
                GenerateBizlogic generateBizlogic = new GenerateBizlogic();
                ICertCatalog certCatalogService = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
                CertCatalog certCatalog = certCatalogService.getLatestCatalogDetailByCatalogid(certCatalogid);
                Map<String, Object> resultMap = generateBizlogic.generateCertification(certCatalog, fieldNames, values);
                if (CertConstant.CONSTANT_STR_ONE.equals(String.valueOf(resultMap.get("issuccess")))) {
                    result.put("filename", certCatalog.getCertname() + ".pdf");
                    ByteArrayOutputStream baos = (ByteArrayOutputStream) resultMap.get("outputstream");
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
                    result.put("inputstream", inputStream);
                }
            }

        }
        return result;
    }

    @Override
    public String getCertinfoCountByDate(String appkey, String ouguid, Map<String, String> extraParams) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        //使用rest接口
        if (CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return "证照库rest对接地址未配置";
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();

            // 对应rest接口地址
            String getCertinfoCountByDate = asRestUrl + "/certInfo/getcertinfocountbydate";
            // 拼接参数
            jsonObject.put("appkey", appkey);
            jsonObject.put("ouguid", ouguid);
            jsonObject.put("extraparams", extraParams);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(getCertinfoCountByDate, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if (CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))) {
                    return objReturn.getJSONObject("custom").getString("count");
                }
            }
            return "查询失败";
        }
        else {
            ICertInfo certInfoService = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            boolean isAreacode = false;
            Record record = new Record();
            if(StringUtil.isBlank(ouguid)){
                isAreacode = true;
                record = certInfoService.getCertinfoSumByDateFromStatistic(isAreacode , appkey);
            }else{
                record = certInfoService.getCertinfoSumByDateFromStatistic(isAreacode , ouguid);
            }
            JSONObject jsonObject = new JSONObject();
            if(record != null){
                jsonObject.put("total" , record.get("total"));
                jsonObject.put("week" , record.get("week"));
                jsonObject.put("year" , record.get("year"));
                jsonObject.put("month" , record.get("month"));
                jsonObject.put("day" , record.get("day"));
            }
            return jsonObject.toString();
        }
    }

    private void getToken() {
        if (expiresIn == 0 || currentTime == 0 || (System.currentTimeMillis() - currentTime) / 1000 > expiresIn
                || StringUtil.isBlank(accessToken)) {

            String appsecret = ConfigUtil.getConfigValue("bigDataAppSecret");
            String tokenurl = ConfigUtil.getConfigValue("bigDataTokenUrl");
            Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
            if (StringUtil.isBlank("appkey")) {
                log.error("=============大数据appkey未配置===========");
                return;
            }
            else if (StringUtil.isBlank("appsecret")) {
                log.error("=============大数据appsecret未配置===========");
                return;
            }
            else if (StringUtil.isBlank("tokenurl")) {
                log.error("=============大数据tokenurl未配置===========");
                return;
            }

            Map<String, Object> params = new HashMap<>();
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");

            // 获取token
            params.put("grant_type", "client_credentials");
            params.put("client_id", bigdataappkey);
            params.put("client_secret", appsecret);

            String result = HttpUtil.doPost(tokenurl + "/rest/oauth2/token", params);
            currentTime = System.currentTimeMillis();
            if (StringUtil.isNotBlank(result)) {
                try {
                    Map<String, Object> tokenMap = JsonUtil.jsonToMap(JsonUtil.jsonToMap(result).get("custom"));
                    accessToken = tokenMap.get("access_token").toString();
                    expiresIn = Integer.parseInt(tokenMap.get("expires_in").toString());
                }
                catch (Exception e) {
                    accessToken = null;
                    e.printStackTrace();
                    //system.out.println("获取Token失败");
                }
            }
        }
    }

    /**
     *  获得证照子拓展信息
     *  @param fields 查询字段
     *  @param parentguid
     *  @return
     */
    @Override
    public List<CertInfoSubExtension> selectSubExtensionByParentguid(String fields, String parentguid) {
        ICertInfoSubExtension subService = ContainerFactory.getContainInfo().getComponent(ICertInfoSubExtension.class);
        return subService.selectSubExtensionByParentguid(fields, parentguid);
    }

    /**
     * 获得证照子拓展信息
     *  @param rowguid
     *  @return
     */
    @Override
    public CertInfoSubExtension getSubExtensionByRowguid(String rowguid) {
        ICertInfoSubExtension subService = ContainerFactory.getContainInfo().getComponent(ICertInfoSubExtension.class);
        return subService.getSubExtensionByRowguid(rowguid);
    }

    /**
     * 添加证照子拓展信息
     *  @param subExtension
     */
    @Override
    public void addSubExtension(CertInfoSubExtension subExtension) {
        ICertInfoSubExtension subService = ContainerFactory.getContainInfo().getComponent(ICertInfoSubExtension.class);
        subService.addSubExtension(subExtension);
    }

    /**
     * 更新证照子拓展信息
     *  @param subExtension
     */
    @Override
    public void updateSubExtension(CertInfoSubExtension subExtension) {
        ICertInfoSubExtension subService = ContainerFactory.getContainInfo().getComponent(ICertInfoSubExtension.class);
        subService.updateSubExtension(subExtension);
    }

    /**
     *  删除证照子拓展信息
     *  @param rowguid
     */
    @Override
    public void deleteExtensionByRowguid(String rowguid) {
        ICertInfoSubExtension subService = ContainerFactory.getContainInfo().getComponent(ICertInfoSubExtension.class);
        subService.deleteExtensionByRowguid(rowguid);
    }

    /**
     *  证照纠错
     *
     */
    @Override
    public String CorrectCertInfo(String appkey, String certinfoguid, String certno, String certcatalogid,
            String tycertcatcode, String correctreason, String cliengguid) {
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String asUseCertrest = configService.getFrameConfigValue("AS_USE_CERTREST");
        //使用rest接口
        if(CertConstant.CONSTANT_STR_ONE.equals(asUseCertrest)) {
            String asRestUrl = configService.getFrameConfigValue("AS_REST_URL");
            if (StringUtil.isBlank(asRestUrl)) {
                Logger log = LogUtil.getLog(CertInfoExternalImpl.class);
                log.error("=============证照库rest对接地址未配置===========");
                return "证照库rest对接地址未配置";
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject paramsToken = new JSONObject();

            // 对应rest接口地址
            String correctCertInfo = asRestUrl + "/certCorrect/correctCertInfo";
            String newCliengguid = UUID.randomUUID().toString();
            // 拼接参数
            //证照基本信息
            jsonObject.put("certinfoguid", certinfoguid);
            //照面信息
            jsonObject.put("certno",certno);
            //证照目录唯一标识
            jsonObject.put("certcatalogid",certcatalogid);
            //目录编号
            jsonObject.put("tycertcatcode",tycertcatcode);
            //纠错原因
            jsonObject.put("correctreason",correctreason);
            //附件关联guid
            jsonObject.put("cliengguid",newCliengguid);
            paramsToken.put("appkey", appkey);
            paramsToken.put("params", jsonObject);
            // 发送post请求
            String returnMsg = HttpUtil.doPostJson(correctCertInfo, paramsToken.toString());
            // 解析结果
            if (StringUtil.isNotBlank(returnMsg)) {
                JSONObject objReturn = JSONObject.parseObject(returnMsg);
                if(CertConstant.CONSTANT_STR_ONE.equals(objReturn.getJSONObject("custom").getString("code"))){
                    String msg = objReturn.getJSONObject("custom").getString("text");
                    if(msg.contains("成功")){
                        jsonObject.clear();
                        jsonObject.put("certcliengguid", newCliengguid);
                        jsonObject.put("oldcertcliengguid", cliengguid);
                        jsonObject.put("appkey", appkey);
                        String attachurl = asRestUrl + "/certAttach/addAttach";
                        jsonObject.put("url", attachurl);
                        if(CertConstant.CONSTANT_STR_ONE.equals(configService.getFrameConfigValue("Cert_NotUseMQ"))){
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    new ExternalListener().handleAttach(attachurl, newCliengguid, null,
                                            cliengguid, null, appkey);
                                }
                            });
                        }else{
                            MQSender.send(CertConstant.QUEUENAME, jsonObject);
                        }
                    }
                    return msg;
                }
            }
            return "纠错成功";
        }else{
            // 获取需要纠错的证照
            CertInfo certInfo = null;
            ICertInfo iCertInfo = ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
            ICertCatalog iCertCatalog = ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);
            ICertCatalogOu iCertCatalogOu = ContainerFactory.getContainInfo().getComponent(ICertCatalogOu.class);
            IRoleService iRoleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
            IUserRoleRelationService iUserRoleRelationService = ContainerFactory.getContainInfo().getComponent(IUserRoleRelationService.class);
            IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            if (StringUtil.isNotBlank(certno)) {
                if (StringUtil.isNotBlank(certcatalogid)) {
                    certInfo = iCertInfo.getCertInfoByCertno(certcatalogid, certno);
                }
                else {
                    // 条件查询
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("tycertcatcode", tycertcatcode);
                    sqlUtils.eq("CERTNO", certno);
                    // 不为历史记录
                    sqlUtils.eq("ISHISTORY", CertConstant.CONSTANT_STR_ZERO);
                    // 状态为正常
                    sqlUtils.eq("STATUS", CertConstant.CERT_STATUS_COMMON);
                    // 设置查询字段
                    sqlUtils.setSelectFields(
                            "rowguid, certid, certcliengguid,copycertcliengguid, certno, certlevel, certawarddept, certownername, certownercerttype, certownerno, awarddate, expiredatefrom, expiredateto, certname, version, versiondate ");
                    List<CertInfo> certInfoList = iCertInfo.getListByCondition(sqlUtils.getMap());
                    if (ValidateUtil.isNotBlankCollection(certInfoList)) {
                        certInfo = certInfoList.get(0);
                    }
                }
            }
            else {
                certInfo = iCertInfo.getCertInfoByRowguid(certinfoguid);
            }
            // 新建纠错信息对象
            CertCorrectionInfo certCorrectionInfo = new CertCorrectionInfo();
            // 定义纠错信息rowguid
            String rowGuid = UUID.randomUUID().toString();
            // 设置纠错信息
            certCorrectionInfo.setRowguid(rowGuid);
            certCorrectionInfo.setInsideappid(appkey);
            certCorrectionInfo.setCertinfoguid(certinfoguid);
            certCorrectionInfo.setCorrectreason(correctreason);
            certCorrectionInfo.setCorrectattachcliengguid(cliengguid);
            certCorrectionInfo.setCorrectdate(new Date()); // 设置申请纠错时间
            certCorrectionInfo.setCorrectstatus(CertConstant.CERT_CORRECT_KEEP); // 设置纠错状态为“纠错中”

            // 检测纠错证照表中是否存在该版本证照的纠错证照
            int version = certInfo.getVersion(); // 获取申请纠错证照的版本号
            String certId = certInfo.getCertid(); // 获取申请纠错证照的版本唯一标志
            ICertCorrection iCertCorrection = ContainerFactory.getContainInfo().getComponent(ICertCorrection.class);
            IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
            IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
            List<CertCorrection> certCorrectionList = iCertCorrection.getCertCorrection(certId, version);
            if (ValidateUtil.isNotBlankCollection(certCorrectionList)) {
                iCertCorrection.updateToHistory(certId, version); // 全部置为历史
                // 删除历史待办消息
                for (CertCorrection certCorrection : certCorrectionList) {
                    if (StringUtil.isNotBlank(certCorrection.getAdduserguid())
                            && iUserService.getUserByUserField("userguid", certCorrection.getAdduserguid()) != null) {
                        List<MessagesCenter> messagesCenters = messageCenterService
                                .queryForList(certCorrection.getAdduserguid(), null, null, "",
                                        IMessagesCenterService.MESSAGETYPE_WAIT,
                                        (certCorrection.getCertcorrectinfoguid()), "", -1, "", null, null, 0, -1);
                        if(ValidateUtil.isNotBlankCollection(messagesCenters)) {
                            if (messagesCenters.get(0) != null) { // 判断消息是否存在
                                messageCenterService.deleteMessageForEver(messagesCenters.get(0).getMessageItemGuid(),
                                        certCorrection.getAdduserguid());
                            }
                        }
                    }
                    else { // 接口新增的证照的纠错
                        // 获取目录guid
                        String certCatalogRowGuid = certCorrection.getCertareaguid();

                        // 获取目录
                        CertCatalog certCatalog = iCertCatalog.getCatalogDetailByrowguid(certCatalogRowGuid);
                        // 获取目录版本唯一标识
                        String certCatalogid = certCatalog.getCertcatalogid();
                        // 获取所有同步该目录的部门guid
                        List<String> ouguidList = iCertCatalogOu.getOuGuidListByCatalogId(false, certCatalogid);

                        // 获取证照维护员角色信息
                        FrameRole frameRole = iRoleService.getRoleByRoleField("rolename", "证照维护员");
                        // 获取拥有证照维护员角色的用户标识（userguid）
                        if (frameRole != null) {
                            List<FrameUserRoleRelation> frameUserRoleRelationList = iUserRoleRelationService
                                    .getRelationListByField("roleguid", frameRole.getRoleGuid(),
                                            frameRole.getRoleType(), null);
                            if (ValidateUtil.isNotBlankCollection(frameUserRoleRelationList)) {
                                for (FrameUserRoleRelation frameUserRoleRelation : frameUserRoleRelationList) {
                                    if (StringUtil.isNotBlank(frameUserRoleRelation.getUserGuid())) {
                                        String userGuid = frameUserRoleRelation.getUserGuid();
                                        // 获取FrameUser对象
                                        FrameUser frameUser = iUserService.getUserByUserField("userguid", userGuid);
                                        // 获取FrameOu对象
                                        FrameOu frameOu = iOuService.getOuByOuGuid(frameUser.getOuGuid());
                                        // 判断baseouguid是否已被授权
                                        if (ValidateUtil.isNotBlankCollection(ouguidList)
                                                && ouguidList.contains(frameOu.getBaseOuguid())) {
                                            List<MessagesCenter> messagesCenters = messageCenterService
                                                    .queryForList(certCorrection.getAdduserguid(), null, null, "",
                                                            IMessagesCenterService.MESSAGETYPE_WAIT,
                                                            (certCorrection.getCertcorrectinfoguid()), "", -1, "", null, null, 0, -1);
                                            if(ValidateUtil.isNotBlankCollection(messagesCenters)) {
                                                if (messagesCenters.get(0) != null) { // 判断消息是否存在
                                                    messageCenterService.deleteMessageForEver(
                                                            messagesCenters.get(0).getMessageItemGuid(), userGuid);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            CertInfo oldCertInfo = (CertInfo) certInfo.clone();
            // 新建纠错证照，并拷贝需要纠错的证照数据
            CertCorrection certCorrection = oldCertInfo.toEntity(CertCorrection.class);
            // 设置新主键
            certCorrection.setRowguid(UUID.randomUUID().toString());
            // 设置操作时间
            certCorrection.setOperatedate(new Date());
            // 版本时间
            certCorrection.setVersiondate(new Date());
            // 插入证照纠错唯一标识（用于关联纠错信息）
            certCorrection.setCertcorrectinfoguid(rowGuid);
            // 更新证照的ishistory字段为零
            certCorrection.setIshistory(CertConstant.CONSTANT_INT_ZERO);
            IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            // 拷贝证照附件（正本）
            int count = iAttachService.getAttachCountByClientGuid(oldCertInfo.getCertcliengguid());
            certCorrection.setCertcliengguid(UUID.randomUUID().toString());
            if (count > 0) {
                iAttachService.copyAttachByClientGuid(oldCertInfo.getCertcliengguid(), null, null,
                        certCorrection.getCertcliengguid());
            }
            // 拷贝证照附件（副本）
            int copyCount = iAttachService.getAttachCountByClientGuid(oldCertInfo.getCopycertcliengguid());
            certCorrection.setCopycertcliengguid(UUID.randomUUID().toString());
            if (copyCount > 0) {
                iAttachService.copyAttachByClientGuid(oldCertInfo.getCopycertcliengguid(), null, null,
                        certCorrection.getCopycertcliengguid());
            }
            // 拷贝缩略图
            int sltImageCount = iAttachService.getAttachCountByClientGuid(oldCertInfo.getSltimagecliengguid());
            certCorrection.setSltimagecliengguid(UUID.randomUUID().toString());
            if (sltImageCount > 0) {
                iAttachService.copyAttachByClientGuid(oldCertInfo.getSltimagecliengguid(), null, null,
                        certCorrection.getSltimagecliengguid());
            }
            // 不拷贝打印doc
            certCorrection.setPrintdocguid(null);
            certCorrection.setCopyprintdocguid(null);
            // 新增到纠错证照表中
            iCertCorrection.addCertCorrection(certCorrection);
            // 拷贝照面信息
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("certinfoguid", certinfoguid);
                CertInfoExtension certExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
                if(certExtension != null){
                    // 设置新的certinfoguid
                    certExtension.setCertinfoguid(certCorrection.getRowguid());
                    certExtension.setRowguid(UUID.randomUUID().toString());
                    // 设置操作时间
                    certExtension.setOperatedate(new Date());
                    // 拷贝照面信息附件
                    AttachBizlogic attachBizlogic = new AttachBizlogic();
                    ICertMetaData iCertMetaData = ContainerFactory.getContainInfo().getComponent(ICertMetaData.class);
                    List<CertMetadata> metadataList = iCertMetaData
                            .selectTopDispinListByCertguid(oldCertInfo.getCertareaguid());
                    if (ValidateUtil.isNotBlankCollection(metadataList)) {
                        for (CertMetadata metadata : metadataList) {
                            if ("image".equals(metadata.getFieldtype().toLowerCase())) {
                                String imagecliengguid = certExtension.getStr(metadata.getFieldname());
                                if (StringUtil.isNotBlank(imagecliengguid)) {
                                    certExtension.set(metadata.getFieldname(), UUID.randomUUID().toString());
                                    attachBizlogic.copyAttach(imagecliengguid,
                                            certExtension.getStr(metadata.getFieldname()));
                                }
                            }
                        }
                    }
                    getMongodbUtil().insert(certExtension);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            ICertCorrectionInfo iCertCorrectionInfo = ContainerFactory.getContainInfo().getComponent(ICertCorrectionInfo.class);
            // 新增纠错信息
            certCorrectionInfo.setCertcorrectionguid(certCorrection.getRowguid());
            iCertCorrectionInfo.addCertCorrectionInfo(certCorrectionInfo);
            // 纠错申请待办消息标题定义
            String title = "【纠错申请】" + certCorrection.getCertname() + "(" + appkey + ")"; // 送阅读标题
            // 纠错申请地址url
            String handleUrl = "epointcert/certinfo/cert/certcorrect/certcorrectedit?guid="
                    + certCorrection.getRowguid();
            // 获取目录guid
            String certCatalogRowGuid = certCorrection.getCertareaguid();
            // 获取目录
            CertCatalog certCatalog = iCertCatalog.getCatalogDetailByrowguid(certCatalogRowGuid);
            // 获取目录版本唯一标识
            String certCatalogid = certCatalog.getCertcatalogid();
            // 获取所有同步该目录的部门guid
            List<String> ouguidList = iCertCatalogOu.getOuGuidListByCatalogId(false, certCatalogid);

            // 获取证照维护员角色信息
            FrameRole frameRole = iRoleService.getRoleByRoleField("rolename", "证照维护员");
            // 获取拥有证照维护员角色的用户标识（userguid）
            if (frameRole != null) {
                List<FrameUserRoleRelation> frameUserRoleRelationList = iUserRoleRelationService
                        .getRelationListByField("roleguid", frameRole.getRoleGuid(), frameRole.getRoleType(), null);
                if (ValidateUtil.isNotBlankCollection(frameUserRoleRelationList)) {
                    for (FrameUserRoleRelation frameUserRoleRelation : frameUserRoleRelationList) {
                        if (StringUtil.isNotBlank(frameUserRoleRelation.getUserGuid())) {
                            String userGuid = frameUserRoleRelation.getUserGuid();
                            // 获取FrameUser对象
                            FrameUser frameUser = iUserService.getUserByUserField("userguid", userGuid);
                            if(frameUser == null){
                                continue;
                            }
                            // 获取FrameOu对象
                            FrameOu frameOu = iOuService.getOuByOuGuid(frameUser.getOuGuid());
                            if(!frameUser.getOuGuid().equals(certCorrection.getOuguid())){
                                FrameOuExtendInfo ouExtendInfo = new FrameOuExtendInfo();
                                if(frameOu!=null){
                                    ouExtendInfo = iOuService.getFrameOuExtendInfo(frameOu.getOuguid());
                                }
                                if(StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))){
                                    if(!ouExtendInfo.getStr("orgcode").equals(certCorrection.getOuguid())) {
                                        continue;
                                    }
                                }else{
                                    continue;
                                }
                            }
                            // 判断baseouguid是否已被授权
                            if (ValidateUtil.isNotBlankCollection(ouguidList)
                                    && ouguidList.contains(frameOu.getBaseOuguid())) {
                                // 发送待办消息给该用户
                                messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                                        IMessagesCenterService.MESSAGETYPE_WAIT, userGuid, userGuid, userGuid,
                                        userGuid, "纠错申请", handleUrl, rowGuid, rowGuid,
                                        CertConstant.CONSTANT_INT_ONE, "", "", rowGuid, "电子证照库", new Date(),
                                        rowGuid, rowGuid, "", "");
                            }
                        }
                    }
                }
            }
            return rowGuid;
        }
    }


	@Override
	public List<CertInfo> selectNearExpiredCertInfo(String arg0, String arg1, String arg2, Map<String, String> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCert(String arg0, String arg1, String arg2, String arg3, Integer arg4) {
		// TODO Auto-generated method stub
		
	}
   
}
