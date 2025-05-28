package com.epoint.cert.util;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJnProjectService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.api.IJnCertInfo;
import com.epoint.cert.api.IXmzCertPushLog;
import com.epoint.cert.domain.XmzCertPushLog;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.third.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.lang.invoke.MethodHandles;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PushUtil {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    //参数加密key
//    private static String key = ConfigUtil.getConfigValue("xmzArgs", "key");

    private static MongodbUtil getMongodbUtil() {
        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
    }

    /**
     * 获取token
     * @param type 2:竣工验收
     * @return
     */
    public static String getCertToken(int type) {
        String tokenUrl ="";
        String appkey = "";
        String appsecret = "";
        switch (type) {
            case 2:
                tokenUrl = ConfigUtil.getConfigValue("xmzArgs", "jgystokenurl");
                appkey = ConfigUtil.getConfigValue("xmzArgs", "jgysappkey");
                appsecret = ConfigUtil.getConfigValue("xmzArgs", "jgysappsecret");
                break;
            default:
                break;
        }
        // 调用获取token接口
        Map<String, String> herders = new HashMap<>();
        herders.put("Content-Type", "application/x-www-form-urlencoded");
        herders.put("Cache-Control", "no-cache");
        Map<String, Object> pramMap = new HashMap<>();
        pramMap.put("client_id", appkey);
        pramMap.put("client_secret", appsecret);
        pramMap.put("grant_type", "client_credentials");
        String s = com.epoint.cert.commonutils.HttpUtil.doPost(tokenUrl, pramMap, herders);
        if (StringUtil.isBlank(s)) {
            return null;
        }
        log.info("===========token返回值==" + s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String custom = jsonObject.getString("custom");
        String access_token = "";
        if (StringUtil.isNotBlank(custom)) {
            access_token = JSONObject.parseObject(custom).getString("access_token");
        }
        log.info("===========token返回值access_token==" + access_token);
        return access_token;
    }

    /**
     * 安许校验接口
     *
     * @param auditProject
     * @param certrowguid
     * @param citynum
     * @param creditCode
     * @param token
     * @return
     */
    public static JSONObject getCertCheck(AuditProject auditProject, String certrowguid, String citynum, String creditCode,
                                          String token, String ecertid, String certno, String type) {
        String checkUrl = ConfigUtil.getConfigValue("xmzArgs", "axcertcheckurl");
        IXmzCertPushLog iXmzCertPushLog = ContainerFactory.getContainInfo().getComponent(IXmzCertPushLog.class);
        String key = ConfigUtil.getConfigValue("xmzArgs", "key");
        //处理返回值
        JSONObject newJson = new JSONObject();

        //入库留痕
        XmzCertPushLog xmzCertPushLog = new XmzCertPushLog();
        xmzCertPushLog.setRowguid(UUID.randomUUID().toString());
        xmzCertPushLog.setCertrowguid(certrowguid);
        xmzCertPushLog.setProjectguid(auditProject.getRowguid());

        JSONObject paramJson = new JSONObject();
        JSONObject AcceptData = new JSONObject();
        JSONObject obj = new JSONObject();
        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        AuditTask audittask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
        // 操作类型
        // 01 新发电子证照
        // 02 延续
        // 03 暂扣
        // 04 过期失效
        // 05 注销（撤销、吊销）
        // 06 暂扣发还
        // 07 其他
        // 08 变更
        // 获取事项
        if (audittask != null) {
            switch (audittask.getItem_id()) {
                case "11370800MB285591843370117002011":
                case "11370800MB28559184300011711200001":
                case "11370800MB285591843370117002016":
                    obj.put("operateType", "01");
                    break;
                case "11370800MB285591843370117002013":
                case "11370800MB28559184300011711200002":
                case "11370800MB285591843370117002014":
                case "11370800MB28559184300011711200003":
                    obj.put("operateType", "02");
                    break;
                case "11370800MB285591843370117002017":
                case "11370800MB28559184300011711200005":
                    obj.put("operateType", "05");
                    break;
                case "11370800MB285591843370117002015":
                case "11370800MB285591843370117002024":
                case "11370800MB285591843370117002027":
                case "11370800MB285591843370117002026":
                case "11370800MB285591843370117002025":
                case "11370800MB28559184300011711200004":
                    obj.put("operateType", "08");
                    break;
                default:
                    obj.put("operateType", "");
                    break;
            }
        } else {
            log.info("===========找不到事项=======");
        }

        if ("0".equals(type)) {
            obj.put("operateType", "01");
        }

        String data = "";
        obj.put("creditCode", creditCode);
        obj.put("eCertID", ecertid);
        obj.put("provinceNum", "370000");
        obj.put("certNum", certno);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        AcceptData.put("AcceptData", jsonArray);
        // 所属地市
        if (StringUtil.isNotBlank(citynum)) {
            paramJson.put("citynum", citynum.substring(0, 6));
        } else {
            paramJson.put("citynum", citynum);
        }
        log.info("安许证加密前入参：" + AcceptData.toJSONString());
        data = sm4Encryption(AcceptData.toJSONString(), key);
        paramJson.put("data", data);

        log.info("========校验接口========r入参：" + paramJson.toJSONString() + "========接口地址：" + checkUrl);
        String returnStr = HttpUtil.doPostJson(checkUrl + "?access_token=" + token,
                paramJson.toJSONString());
        if (StringUtil.isBlank(returnStr)) {
            log.info("========校验接口========：接口返回值是空");
            xmzCertPushLog.setReturncode("");
            xmzCertPushLog.setErrormsg("接口返回值是空");
            xmzCertPushLog.setWarnmsg("");
            xmzCertPushLog.setPushdate(new Date());
            iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

            newJson.put("returnCode", "-1");
            newJson.put("text", "接口返回值是空");
        } else {
            log.info("========校验接口========：返回值：" + returnStr);
            JSONObject returnJson = JSONObject.parseObject(returnStr);
            String returnCode = returnJson.getString("ReturnCode");
            if (StringUtil.isNotBlank(returnCode)) {
                newJson.put("returnCode", returnCode);
                if ("0".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("ErrorData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);
                        newJson.put("text", errorDataJson.getString("ErrorMsg"));
                        newJson.put("guid", errorDataJson.getString("ErrorGuid"));

                        //失败和预警返回值需入库推送留痕
                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg(errorDataJson.getString("ErrorMsg"));
                        xmzCertPushLog.setWarnmsg("");
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    }

                } else if ("2".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("WarnData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);
                        newJson.put("text", errorDataJson.getString("WarnMsg"));
                        newJson.put("guid", errorDataJson.getString("WarnGuid"));

                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg("");
                        xmzCertPushLog.setWarnmsg(errorDataJson.getString("WarnMsg"));
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    }
                } else if ("1".equals(returnCode)) {
                    newJson.put("text", "校验成功");
                } else {
                    xmzCertPushLog.setReturncode(returnCode);
                    xmzCertPushLog.setErrormsg("接口未返回判断字段状态有误，无法判断调用结果");
                    xmzCertPushLog.setWarnmsg("");
                    xmzCertPushLog.setPushdate(new Date());
                    iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                    newJson.put("text", "接口未返回判断字段状态有误，无法判断调用结果");
                }
            } else {
                xmzCertPushLog.setReturncode("-2");
                xmzCertPushLog.setErrormsg("接口未返回判断字段无法判断调用结果");
                xmzCertPushLog.setWarnmsg("");
                xmzCertPushLog.setPushdate(new Date());
                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                newJson.put("returnCode", "-2");
                newJson.put("text", "接口未返回判断字段无法判断调用结果");
            }
        }
        log.info("========校验方法========newJson：" + newJson.toJSONString());
        return newJson;
    }

    /**
     * 竣工验收校验接口
     *
     * @return
     */
    public static JSONObject getCertCheck1(AuditProject auditProject, String certrowguid, String citynum,
                                          String token,String xmdm,String JGYSBABH) {
        
        String jgyscheckurl = ConfigUtil.getConfigValue("xmzArgs", "jgyscheckorpushurl");
        String key = ConfigUtil.getConfigValue("xmzArgs", "jgyskey");
        IXmzCertPushLog iXmzCertPushLog = ContainerFactory.getContainInfo().getComponent(IXmzCertPushLog.class);
        //处理返回值
        JSONObject newJson = new JSONObject();

        //入库留痕
        XmzCertPushLog xmzCertPushLog = new XmzCertPushLog();
        xmzCertPushLog.setRowguid(UUID.randomUUID().toString());
        xmzCertPushLog.setCertrowguid(certrowguid);
        xmzCertPushLog.setProjectguid(auditProject.getRowguid());
        JSONObject preparamJson = new JSONObject();
        JSONObject paramJson = new JSONObject();
        JSONObject AcceptData = new JSONObject();
        JSONObject obj = new JSONObject();
        String data = "";
        //行政区划代码
        obj.put("XZQHDM", auditProject.getAreacode());
        //项目代码
        obj.put("XMDM", xmdm);
        //竣工验收备案编号
        obj.put("JGYSBABH", JGYSBABH);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        AcceptData.put("AcceptData", jsonArray);
        // 所属地市
//        if (StringUtil.isNotBlank(citynum)) {
//            paramJson.put("citynum", citynum.substring(0, 6));
//        } else {
//            paramJson.put("citynum", citynum);
//        }
        paramJson.put("citynum", "370800");
        log.info("竣工验收加密前入参：" + AcceptData.toJSONString());
        data = sm4Encryption(AcceptData.toJSONString(), key);
        paramJson.put("data", data);
        preparamJson.put("params", paramJson);
        preparamJson.put("MethodName", "prjFinishCheck");

        log.info("========校验接口========r入参：" + preparamJson.toJSONString() + "========接口地址：" + jgyscheckurl);
        String returnStr = HttpUtil.doPostJson(jgyscheckurl + "?access_token=" + token,
                preparamJson.toJSONString());
        if (StringUtil.isBlank(returnStr)) {
            log.info("========校验接口========：接口返回值是空");
            xmzCertPushLog.setReturncode("");
            xmzCertPushLog.setErrormsg("接口返回值是空");
            xmzCertPushLog.setWarnmsg("");
            xmzCertPushLog.setPushdate(new Date());
            iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

            newJson.put("returnCode", "-1");
            newJson.put("text", "接口返回值是空");
        } else {
            log.info("========校验接口========：返回值：" + returnStr);
            JSONObject customeJson = JSONObject.parseObject(returnStr);
            JSONObject returnJson = customeJson.getJSONObject("custom");
            String returnCode = returnJson.getString("ReturnCode");
            if (StringUtil.isNotBlank(returnCode)) {
                newJson.put("returnCode", returnCode);
                if ("0".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("ErrorData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);
                        newJson.put("text", errorDataJson.getString("ErrorMsg"));
                        newJson.put("guid", errorDataJson.getString("ErrorGuid"));

                        //失败和预警返回值需入库推送留痕
                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg(errorDataJson.getString("ErrorMsg"));
                        xmzCertPushLog.setWarnmsg("");
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    }

                } else if ("2".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("WarnData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);
                        newJson.put("text", errorDataJson.getString("WarnMsg"));
                        newJson.put("guid", errorDataJson.getString("WarnGuid"));

                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg("");
                        xmzCertPushLog.setWarnmsg(errorDataJson.getString("WarnMsg"));
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    }
                } else if ("1".equals(returnCode)) {
                    newJson.put("text", "校验成功");
                } else {
                    xmzCertPushLog.setReturncode(returnCode);
                    xmzCertPushLog.setErrormsg("接口未返回判断字段状态有误，无法判断调用结果");
                    xmzCertPushLog.setWarnmsg("");
                    xmzCertPushLog.setPushdate(new Date());
                    iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                    newJson.put("text", "接口未返回判断字段状态有误，无法判断调用结果");
                }
            } else {
                xmzCertPushLog.setReturncode("-2");
                xmzCertPushLog.setErrormsg("接口未返回判断字段无法判断调用结果");
                xmzCertPushLog.setWarnmsg("");
                xmzCertPushLog.setPushdate(new Date());
                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                newJson.put("returnCode", "-2");
                newJson.put("text", "接口未返回判断字段无法判断调用结果");
            }
        }
        log.info("========校验方法========newJson：" + newJson.toJSONString());
        return newJson;
    }

    /**
     * 安许证书归集赋码
     *
     * @param projectguid
     * @param certrowguid
     * @param obj
     * @param token
     * @return
     */
    public static JSONObject getCertPush(String projectguid, String certrowguid, JSONObject obj, String token, String certtype,
                                         String citynum, String type,boolean ifgjfm){

        IXmzCertPushLog iXmzCertPushLog = ContainerFactory.getContainInfo().getComponent(IXmzCertPushLog.class);
        String privateKey = ConfigUtil.getConfigValue("xmzArgs", "privateKey");
        String pushUrl = ConfigUtil.getConfigValue("xmzArgs", "axcertpushurl");
        String pushgzUrl = ConfigUtil.getConfigValue("xmzArgs", "axcertbgpushurl");
        String publicKey = ConfigUtil.getConfigValue("xmzArgs", "publicKey");
        String key = ConfigUtil.getConfigValue("xmzArgs", "key");
        //处理返回值
        JSONObject newJson = new JSONObject();

        //入库留痕
        XmzCertPushLog xmzCertPushLog = new XmzCertPushLog();
        xmzCertPushLog.setRowguid(UUID.randomUUID().toString());
        xmzCertPushLog.setCertrowguid(certrowguid);
        xmzCertPushLog.setProjectguid(projectguid);

        if ("0".equals(type)) {
            obj.put("operateType", "01");
        }

        JSONObject paramJson = new JSONObject();
        JSONObject AcceptData = new JSONObject();
        String data = "";
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        AcceptData.put("AcceptData", jsonArray);
        paramJson.put("citynum", citynum);
        log.info("安许证加密前入参：" + AcceptData.toJSONString());
        data = sm4Encryption(AcceptData.toJSONString(), key);
        paramJson.put("data", data);
        String returnStr = "";
        //是更正
        if(!ifgjfm){
            log.info("========更正========r入参：" + paramJson.toJSONString() + "========接口调用地址：" + pushgzUrl);
            returnStr = HttpUtil.doPostJson(pushgzUrl + "?access_token=" + token,
                    paramJson.toJSONString());
            if (StringUtil.isBlank(returnStr)) {
                log.info("========更正========：返回值是空");
                xmzCertPushLog.setReturncode("");
                xmzCertPushLog.setErrormsg("接口返回值是空");
                xmzCertPushLog.setWarnmsg("");
                xmzCertPushLog.setPushdate(new Date());
                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                newJson.put("returnCode", "-1");
                newJson.put("text", "接口返回值是空");
            }else{
                log.info("returnStr"+returnStr);
            }

        }else{
            //新增
            log.info("========归集赋码========r入参：" + paramJson.toJSONString() + "========接口调用地址：" + pushUrl);
            returnStr = HttpUtil.doPostJson(pushUrl + "?access_token=" + token,
                    paramJson.toJSONString());
            if (StringUtil.isBlank(returnStr)) {
                log.info("========归集赋码========：返回值是空");
                xmzCertPushLog.setReturncode("");
                xmzCertPushLog.setErrormsg("接口返回值是空");
                xmzCertPushLog.setWarnmsg("");
                xmzCertPushLog.setPushdate(new Date());
                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                newJson.put("returnCode", "-1");
                newJson.put("text", "接口返回值是空");
            } else {
                log.info("========归集赋码接口========返回值：" + returnStr);
                JSONObject returnJson = JSONObject.parseObject(returnStr);
                String returnCode = returnJson.getString("ReturnCode");
                if (StringUtil.isNotBlank(returnCode)) {
                    if ("0".equals(returnCode)) {
                        JSONObject returnData = returnJson.getJSONObject("ReturnData");
                        if (StringUtil.isNotBlank(returnData)) {
                            JSONArray errorData = returnData.getJSONArray("ErrorData");
                            JSONObject errorDataJson = errorData.getJSONObject(0);
                            newJson.put("text", errorDataJson.getString("ErrorMsg"));
                            newJson.put("guid", errorDataJson.getString("ErrorGuid"));

                            //失败和预警返回值需入库推送留痕
                            xmzCertPushLog.setReturncode(returnCode);
                            xmzCertPushLog.setErrormsg(errorDataJson.getString("ErrorMsg"));
                            xmzCertPushLog.setWarnmsg("");
                            xmzCertPushLog.setPushdate(new Date());
                            iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                        }

                    } else if ("2".equals(returnCode)) {
                        JSONObject returnData = returnJson.getJSONObject("ReturnData");
                        if (StringUtil.isNotBlank(returnData)) {
                            JSONArray errorData = returnData.getJSONArray("WarnData");
                            JSONObject errorDataJson = errorData.getJSONObject(0);

                            xmzCertPushLog.setReturncode(returnCode);
                            xmzCertPushLog.setErrormsg("");
                            xmzCertPushLog.setWarnmsg(errorDataJson.getString("WarnMsg"));
                            xmzCertPushLog.setPushdate(new Date());
                            iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                            //成功信息
                            JSONArray successData = returnData.getJSONArray("SuccessData");
                            JSONObject successDataJson = successData.getJSONObject(0);
                            //二维码
                            String encryCertid = successDataJson.getString("encryCertid");
                            String encryKey = successDataJson.getString("encryKey");

                            try {
                                newJson.put("qrcoderas", getQrcode(encryCertid, encryKey,privateKey));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            newJson.put("text", errorDataJson.getString("WarnMsg"));
                            newJson.put("guid", errorDataJson.getString("WarnGuid"));

                            newJson.put("successData", successDataJson);
                        }
                    } else if ("1".equals(returnCode)) {
                        JSONObject returnData = returnJson.getJSONObject("ReturnData");
                        //成功
                        JSONArray SuccessData = returnData.getJSONArray("SuccessData");
                        log.info("SuccessData：" + SuccessData);
                        if (StringUtil.isNotBlank(SuccessData) && StringUtil.isNotBlank(SuccessData.getJSONObject(0))) {
                            JSONObject returnDataJson = SuccessData.getJSONObject(0);
                            //二维码
                            String encryCertid = returnDataJson.getString("encryCertid");
                            String encryKey = returnDataJson.getString("encryKey");
                            //存储值
                            String eCertid = returnDataJson.getString("eCertID");
                            newJson.put("text", "校验成功");
                            String qrcode = "";
                            try {
                                qrcode = getQrcode(encryCertid, encryKey,privateKey);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                newJson.put("qrcoderas", qrcode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            newJson.put("ecertid", eCertid);
                            newJson.put("successData", returnDataJson);
                            newJson.put("returnCode", "1");
                        }
                    } else {
                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg("接口未返回判断字段状态有误，无法判断调用结果");
                        xmzCertPushLog.setWarnmsg("");
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                        newJson.put("text", "接口未返回判断字段状态有误，无法判断调用结果");
                    }
                    newJson.put("returnCode", returnCode);
                } else {
                    xmzCertPushLog.setReturncode("-2");
                    xmzCertPushLog.setErrormsg("接口未返回判断字段无法判断调用结果");
                    xmzCertPushLog.setWarnmsg("");
                    xmzCertPushLog.setPushdate(new Date());
                    iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                    newJson.put("returnCode", "-2");
                    newJson.put("text", "接口未返回判断字段无法判断调用结果");
                }
            }

        }


        log.info("========归集赋码方法========newJson：" + newJson.toJSONString());
        return newJson;
    }

    /**
     * 竣工验收调用省电子证照归集接口
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject jgyscerguiji(JSONObject gjationJson, String citynum) {
        JSONObject returnJson = new JSONObject();
        String pushUrl = ConfigUtil.getConfigValue("xmzArgs", "jgyscheckorpushurl");
        String key = ConfigUtil.getConfigValue("xmzArgs", "jgyskey");
        try {
            // 先获取token
            String access_token =  PushUtil.getCertToken(2);
            if (StringUtil.isBlank(access_token)) {
                returnJson.put("sucess", false);
                returnJson.put("msg", "access_token为空");
                return returnJson;
            }
            JSONArray acceptArray = new JSONArray();
            log.info("归集请求参数" + gjationJson.toJSONString());
            acceptArray.add(gjationJson);
            JSONObject dataJson = new JSONObject();
            dataJson.put("AcceptData", acceptArray);
            String base64Pass = sm4Encryption(dataJson.toJSONString(), key);
            JSONObject postJson = new JSONObject();
            JSONObject paramsJson = new JSONObject();
            paramsJson.put("citynum", "370800");
            paramsJson.put("data", base64Pass);
            postJson.put("params", paramsJson);
            postJson.put("MethodName", "prjFinishGJ");
            log.info("归集请求参数postJson:" + postJson.toJSONString());
            String postUrl = pushUrl + "?access_token=" + access_token;
            String dataResult = HttpUtil.doPostJson(postUrl, postJson.toJSONString());
            log.info("jgysbadzzzdataresult获取归集返回结果:" + dataResult);
            JSONObject resultJson = JSONObject.parseObject(dataResult);
            JSONObject customJson = resultJson.getJSONObject("custom");
            String returnCode = customJson.getString("ReturnCode");
            if ("1".equals(returnCode)) {
                returnJson.put("sucess", true);
                returnJson.put("msg", customJson.getString("ReturnMsg"));
                returnJson.put("allResult", dataResult);
            }
            else {
                JSONObject ReturnData = customJson.getJSONObject("ReturnData");
                if ("0".equals(returnCode)) {
                    JSONArray ErrorDataja = JSONArray.parseArray(ReturnData.get("ErrorData").toString());
                    JSONObject ErrorDatajo = JSONObject.parseObject(ErrorDataja.get(0).toString());
                    returnJson.put("sucess", false);
                    returnJson.put("msg", ErrorDatajo.get("ErrorMsg").toString());
                    returnJson.put("allResult", dataResult);
                }
                else if ("2".equals(returnCode)) {
                    JSONArray WarnDataja = JSONArray.parseArray(ReturnData.get("WarnData").toString());
                    JSONObject WarnDatajo = JSONObject.parseObject(WarnDataja.get(0).toString());
                    returnJson.put("sucess", false);
                    returnJson.put("msg", WarnDatajo.get("WarnMsg").toString());
                    returnJson.put("allResult", dataResult);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            returnJson.put("sucess", false);
            returnJson.put("msg", "调用省电子证照归集接口异常,请联系管理员");
        }
        return returnJson;
    }

    /**
     * 竣工验收调用省电子证照注销接口
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject jgyscercancel(JSONObject gjationJson, String citynum) {
        JSONObject returnJson = new JSONObject();
        String pushUrl = ConfigUtil.getConfigValue("xmzArgs", "jgyscheckorpushurl");
        String key = ConfigUtil.getConfigValue("xmzArgs", "jgyskey");
        try {
            // 先获取token
            String access_token =  PushUtil.getCertToken(2);
            if (StringUtil.isBlank(access_token)) {
                returnJson.put("sucess", false);
                returnJson.put("msg", "access_token为空");
                return returnJson;
            }
            JSONArray acceptArray = new JSONArray();
            log.info("归集请求参数" + gjationJson.toJSONString());
            acceptArray.add(gjationJson);
            JSONObject dataJson = new JSONObject();
            dataJson.put("AcceptData", acceptArray);
            String base64Pass = sm4Encryption(dataJson.toJSONString(), key);
            JSONObject postJson = new JSONObject();
            JSONObject paramsJson = new JSONObject();
            paramsJson.put("citynum", "370800");
            paramsJson.put("data", base64Pass);
            postJson.put("params", paramsJson);
            postJson.put("MethodName", "prjFinishZX");
            log.info("归集请求参数postJson:" + postJson.toJSONString());
            String postUrl = pushUrl + "?access_token=" + access_token;
            String dataResult = HttpUtil.doPostJson(postUrl, postJson.toJSONString());
            log.info("jgysbadzzzdataresult获取归集返回结果:" + dataResult);
            JSONObject resultJson = JSONObject.parseObject(dataResult);
            JSONObject customJson = resultJson.getJSONObject("custom");
            String returnCode = customJson.getString("ReturnCode");
            if ("1".equals(returnCode)) {
                returnJson.put("sucess", true);
                returnJson.put("msg", customJson.getString("ReturnMsg"));
                returnJson.put("allResult", dataResult);
            }
            else {
                JSONObject ReturnData = customJson.getJSONObject("ReturnData");
                if ("0".equals(returnCode)) {
                    JSONArray ErrorDataja = JSONArray.parseArray(ReturnData.get("ErrorData").toString());
                    JSONObject ErrorDatajo = JSONObject.parseObject(ErrorDataja.get(0).toString());
                    returnJson.put("sucess", false);
                    returnJson.put("msg", ErrorDatajo.get("ErrorMsg").toString());
                    returnJson.put("allResult", dataResult);
                }
                else if ("2".equals(returnCode)) {
                    JSONArray WarnDataja = JSONArray.parseArray(ReturnData.get("WarnData").toString());
                    JSONObject WarnDatajo = JSONObject.parseObject(WarnDataja.get(0).toString());
                    returnJson.put("sucess", false);
                    returnJson.put("msg", WarnDatajo.get("WarnMsg").toString());
                    returnJson.put("allResult", dataResult);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            returnJson.put("sucess", false);
            returnJson.put("msg", "调用省电子证照归集接口异常,请联系管理员");
        }
        return returnJson;
    }



    /**
     * 竣工验收归集赋码
     *
     * @return
     */
    public static JSONObject getCertPush1(String projectguid, String certrowguid, JSONObject obj, String token,
                                         String citynum, String type){

        String jgyscertpushurl = ConfigUtil.getConfigValue("xmzArgs", "jgyscheckorpushurl");
        String privateKey = ConfigUtil.getConfigValue("xmzArgs", "jgysprivateKey");
        String publicKey = ConfigUtil.getConfigValue("xmzArgs", "publicKey");
        String key = ConfigUtil.getConfigValue("xmzArgs", "jgyskey");
        IXmzCertPushLog iXmzCertPushLog = ContainerFactory.getContainInfo().getComponent(IXmzCertPushLog.class);
        //处理返回值
        JSONObject newJson = new JSONObject();

        //入库留痕
        XmzCertPushLog xmzCertPushLog = new XmzCertPushLog();
        xmzCertPushLog.setRowguid(UUID.randomUUID().toString());
        xmzCertPushLog.setCertrowguid(certrowguid);
        xmzCertPushLog.setProjectguid(projectguid);


        JSONObject preparamJson = new JSONObject();
        JSONObject paramJson = new JSONObject();
        JSONObject AcceptData = new JSONObject();
        String data = "";
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        AcceptData.put("AcceptData", jsonArray);
        paramJson.put("citynum", "370800");
        log.info("竣工验收加密前入参：" + AcceptData.toJSONString());
        data = sm4Encryption(AcceptData.toJSONString(), key);
        paramJson.put("data", data);
        preparamJson.put("params", paramJson);
        preparamJson.put("MethodName", "prjFinishFM");
        if ("0".equals(type)) {
            paramJson.put("operatetype", "1");
        }
        log.info("preparamJson:"+preparamJson.toJSONString());
        String returnStr = "";
        returnStr = HttpUtil.doPostJson(jgyscertpushurl + "?access_token=" + token,
                preparamJson.toJSONString());
        if (StringUtil.isBlank(returnStr)) {
            log.info("========归集赋码========：返回值是空");
            xmzCertPushLog.setReturncode("");
            xmzCertPushLog.setErrormsg("接口返回值是空");
            xmzCertPushLog.setWarnmsg("");
            xmzCertPushLog.setPushdate(new Date());
            iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

            newJson.put("returnCode", "-1");
            newJson.put("text", "接口返回值是空");
        } else {
            log.info("========归集赋码接口========返回值：" + returnStr);
            JSONObject customeJson = JSONObject.parseObject(returnStr);
            JSONObject returnJson = customeJson.getJSONObject("custom");
            String returnCode = returnJson.getString("ReturnCode");
            if (StringUtil.isNotBlank(returnCode)) {
                if ("0".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("ErrorData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);
                        newJson.put("text", errorDataJson.getString("ErrorMsg"));
                        newJson.put("guid", errorDataJson.getString("ErrorGuid"));

                        //失败和预警返回值需入库推送留痕
                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg(errorDataJson.getString("ErrorMsg"));
                        xmzCertPushLog.setWarnmsg("");
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    }

                } else if ("2".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    if (StringUtil.isNotBlank(returnData)) {
                        JSONArray errorData = returnData.getJSONArray("WarnData");
                        JSONObject errorDataJson = errorData.getJSONObject(0);

                        xmzCertPushLog.setReturncode(returnCode);
                        xmzCertPushLog.setErrormsg("");
                        xmzCertPushLog.setWarnmsg(errorDataJson.getString("WarnMsg"));
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                        //成功信息
                        JSONArray successData = returnData.getJSONArray("SuccessData");
                        JSONObject successDataJson = successData.getJSONObject(0);
                        //二维码
                        String encryCertid = successDataJson.getString("encryCertid");
                        String encryKey = successDataJson.getString("encryKey");

                        try {
                            newJson.put("qrcoderas", getQrcode(encryCertid, encryKey,privateKey));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newJson.put("text", errorDataJson.getString("WarnMsg"));
                        newJson.put("guid", errorDataJson.getString("WarnGuid"));

                        newJson.put("successData", successDataJson);
                    }
                } else if ("1".equals(returnCode)) {
                    JSONObject returnData = returnJson.getJSONObject("ReturnData");
                    //成功
                    JSONArray SuccessData = returnData.getJSONArray("SuccessData");
                    log.info("SuccessData：" + SuccessData);
                    if (StringUtil.isNotBlank(SuccessData) && StringUtil.isNotBlank(SuccessData.getJSONObject(0))) {
                        JSONObject returnDataJson = SuccessData.getJSONObject(0);
                        //业务数据竣工验收备案编号
                        String JGYSBABH = returnDataJson.getString("JGYSBABH");
                        log.info("JGYSBABH:"+JGYSBABH);
                        //二维码
                        String encryCertid = returnDataJson.getString("encryCertid");
                        String encryKey = returnDataJson.getString("encryKey");
                        //存储值
                        String fmcode = returnDataJson.getString("fmcode");
                        newJson.put("text", "校验成功");
                        String qrcode = "";
                        try {
                            qrcode = getQrcode(encryCertid, encryKey,privateKey);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            newJson.put("qrcoderas", qrcode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        newJson.put("ecertid", fmcode);
                        newJson.put("successData", returnDataJson);
                        newJson.put("returnCode", "1");
                    }
                } else {
                    xmzCertPushLog.setReturncode(returnCode);
                    xmzCertPushLog.setErrormsg("接口未返回判断字段状态有误，无法判断调用结果");
                    xmzCertPushLog.setWarnmsg("");
                    xmzCertPushLog.setPushdate(new Date());
                    iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                    newJson.put("text", "接口未返回判断字段状态有误，无法判断调用结果");
                }
                newJson.put("returnCode", returnCode);
            } else {
                xmzCertPushLog.setReturncode("-2");
                xmzCertPushLog.setErrormsg("接口未返回判断字段无法判断调用结果");
                xmzCertPushLog.setWarnmsg("");
                xmzCertPushLog.setPushdate(new Date());
                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);

                newJson.put("returnCode", "-2");
                newJson.put("text", "接口未返回判断字段无法判断调用结果");
            }
        }


        log.info("========归集赋码方法========newJson：" + newJson.toJSONString());
        return newJson;
    }

    /**
     * 变更接口
     *
     * @param projectguid
     * @return
     */
    public static String getCertUpdate(String projectguid) {
        String updateUrl = ConfigUtil.getConfigValue("xmzArgs", "aqsgxkzsztupdateurl");
        String key = ConfigUtil.getConfigValue("xmzArgs", "key");
        IXmzCertPushLog iXmzCertPushLog = ContainerFactory.getContainInfo().getComponent(IXmzCertPushLog.class);
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IJnCertInfo iJnCertInfo = ContainerFactory.getContainInfo().getComponent(IJnCertInfo.class);
        IJnProjectService iJnProjectService = ContainerFactory.getContainInfo().getComponent(IJnProjectService.class);
        String citynum = "370800";
        // 获取token
        String token = PushTokenUtil.getCertToken();
        //处理返回值
        String message = "";

        //入库留痕
        XmzCertPushLog xmzCertPushLog = new XmzCertPushLog();
        xmzCertPushLog.setRowguid(UUID.randomUUID().toString());
        xmzCertPushLog.setProjectguid(projectguid);

        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectguid, "").getResult();
        if (auditProject != null) {
            JSONObject paramJson = new JSONObject();
            JSONObject AcceptData = new JSONObject();
            JSONObject obj = new JSONObject();
            String eCertID = UUID.randomUUID().toString();
            String creditCode = auditProject.getCertnum();
            Record info_new = iJnCertInfo.getCertInfoByTyshxydm(creditCode);
            if (info_new != null) {
                String associatedZzeCertID = info_new.getStr("GUANLIANZHENGZHAOBIAOSHI");
                String certNum = info_new.getStr("LICENSE_NUMBER");
                obj.put("certNum", certNum);//证书编号
                obj.put("eCertID", eCertID);
                obj.put("zzeCertID", associatedZzeCertID);
                obj.put("provinceNum", "370000");
                obj.put("creditCode", creditCode);
                obj.put("certState", "05");
                obj.put("certStatusDescription", "");
                obj.put("operateType", "05");
                String data = "";
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(obj);
                AcceptData.put("AcceptData", jsonArray);
                paramJson.put("citynum", citynum);
                log.info("变更===安许证加密前入参：" + AcceptData.toJSONString());
                data = sm4Encryption(AcceptData.toJSONString(), key);
                paramJson.put("data", data);

                log.info("========变更接口========r入参：" + paramJson.toJSONString() + "========接口调用地址：" + updateUrl);
                String returnStr = HttpUtil.doPostJson(updateUrl + "?access_token=" + token, paramJson.toJSONString());
                if (StringUtil.isBlank(returnStr)) {
                    xmzCertPushLog.setReturncode("");
                    xmzCertPushLog.setErrormsg("接口返回值是空");
                    xmzCertPushLog.setWarnmsg("");
                    xmzCertPushLog.setPushdate(new Date());
                    iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                    message = "0";
                } else {
                    log.info("========变更接口========返回值：" + returnStr);
                    JSONObject returnJson = JSONObject.parseObject(returnStr);
                    String returnCode = returnJson.getString("ReturnCode");
                    if (StringUtil.isNotBlank(returnCode)) {
                        if ("0".equals(returnCode)) {
                            JSONObject returnData = returnJson.getJSONObject("ReturnData");
                            if (StringUtil.isNotBlank(returnData)) {
                                JSONArray errorData = returnData.getJSONArray("ErrorData");
                                JSONObject errorDataJson = errorData.getJSONObject(0);
                                //失败和预警返回值需入库推送留痕
                                xmzCertPushLog.setReturncode(returnCode);
                                xmzCertPushLog.setErrormsg(errorDataJson.getString("ErrorMsg"));
                                xmzCertPushLog.setWarnmsg("");
                                xmzCertPushLog.setPushdate(new Date());
                                iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                            }
                        } else if ("1".equals(returnCode)) {
                            //说明成功调用，置为cancel 注销状态
                            message = "1";
                            info_new.set("rowguid", info_new.getStr("rowguid"));
                            info_new.set("state", "cancel");
                            info_new.setSql_TableName("EX_JZSGQYAQSCXKZX_1");
                            info_new.setPrimaryKeys("rowguid");
                            iJnProjectService.UpdateRecord(info_new);
                        }
                    } else {
                        xmzCertPushLog.setReturncode("-2");
                        xmzCertPushLog.setErrormsg("接口未返回判断字段无法判断调用结果");
                        xmzCertPushLog.setWarnmsg("");
                        xmzCertPushLog.setPushdate(new Date());
                        iXmzCertPushLog.addXmzCertPushLog(xmzCertPushLog);
                        message = "0";
                    }
                }

            } else {
                //查不到的，直接跳过办结，不影响流程
                message = "1";
            }

        }
        return message;
    }

    /**
     * RSA公钥解密
     *
     * @param str 加密字符串
     */
    public static String RasDecryptByPublicKey(String str,String publicKey) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        // PKCS8EncodedKeySpec该类代表私有密钥的ASN.1编码
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    public static String getQrcode(String encryCertid, String encryKey,String privateKey) throws Exception {
        JSONObject result = decryption(encryCertid, encryKey, privateKey);
        log.info("二维码解密结果result:" + result.toString());
        String qrcode = result.getString("qrcode");
        if (StringUtil.isNotBlank(qrcode)) {
            return qrcode;
        } else {
            return "";
        }
    }

    /**
     * 解密
     *
     * @param encryCertid      加密后的证照的唯一标识
     * @param encryKey         ras加密后的key
     * @param privateKeyString 私钥
     */
    public static JSONObject decryption(String encryCertid, String encryKey, String privateKeyString) {
        JSONObject rtnObj = new JSONObject();
        try {
            // 先将key进行ras解密
            String decryKey = RasDecrypt(encryKey, privateKeyString);
            // 生成密钥对象,如果算法是DES，那么这个构造函数不会检查key是否为8个字节长
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryKey.getBytes(), "DES");
            // 获取加解密实例
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // 初始化解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // HexBinaryAdapter十六进制转换工具
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            // 解密
            byte[] ecertidResult = cipher.doFinal(hexBinaryAdapter.unmarshal(encryCertid));
            // 结果
            rtnObj.put("qrcode", new String(ecertidResult));
            rtnObj.put("decryKey", decryKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rtnObj;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     */
    public static String RasDecrypt(String str, String privateKey) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        // PKCS8EncodedKeySpec该类代表私有密钥的ASN.1编码
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 入参加密方法
     *
     * @param str
     * @param key
     * @return
     */
    public static String sm4Encryption(String str, String key) {
        SM4 sm4 = SmUtil.sm4(key.getBytes());
        String base64Pass = sm4.encryptBase64(str);
        return base64Pass;
    }

}
