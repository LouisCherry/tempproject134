package com.epoint.xmz.gxhimportcert.job;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.sm2util.SM2Utils;
import com.epoint.sm2util.TaHttpRequestUtils;
import com.epoint.xmz.gxhimportcert.api.IGxhImportCertLsService;
import com.epoint.xmz.gxhimportcert.api.entity.GxhImportCertLs;

@DisallowConcurrentExecution
public class GxhImportCertLsSyncJob implements Job
{
    /**
     * 电子身份证参数
     */
    private String privatekey = "00be74f417ec9e92fa1532d91c5a011d57b9d2ed88343b0dd716e5d3648f9b92d9";
    private String accessId = "JNS_SPJYC";
    private String accessToken = "711816CC3FAC4592A216F606A74CA89C";

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        IGxhImportCertLsService gxhImportCertLsService = ContainerFactory.getContainInfo().getComponent(IGxhImportCertLsService.class);

        try {
            int first = 0;
            int pageSize = 1000;

            log.info("------------------梁山县电子证照数据同步开始--------------------");
            List<Record> frCertList = gxhImportCertLsService.getSyncCertList("10"); // 法人需要同步的证照
            List<Record> zrrCertList = gxhImportCertLsService.getSyncCertList("20"); // 法人需要同步的证照

            if (frCertList != null && !frCertList.isEmpty()) {
                List<Record> frInfoList = gxhImportCertLsService.getLsCompanyIdNumberList(first, pageSize);

                while (frInfoList != null && !frInfoList.isEmpty()) {
                    for (Record frInfo : frInfoList) {
                        for (Record frCert : frCertList) {

                            // 开启事务
                            EpointFrameDsManager.begin(null);
                            syncCompanyCertInfo(frCert, frInfo);
                            // 提交事务
                            EpointFrameDsManager.commit();
                        }
                    }

                    first += pageSize;
                    frInfoList = gxhImportCertLsService.getLsCompanyIdNumberList(first, pageSize);
                }
            }

            first = 0; // 归零

            if (zrrCertList != null && !zrrCertList.isEmpty()) {
                List<Record> zrrInfoList = gxhImportCertLsService.getLsPersonIdNumberList(first, pageSize);

                while (zrrInfoList != null && !zrrInfoList.isEmpty()) {
                    for (Record zrrInfo : zrrInfoList) {
                        for (Record zrrCert : zrrCertList) {

                            // 开启事务
                            EpointFrameDsManager.begin(null);
                            syncPersonCertInfo(zrrCert, zrrInfo);
                            // 提交事务
                            EpointFrameDsManager.commit();
                        }
                    }

                    first += pageSize;
                    zrrInfoList = gxhImportCertLsService.getLsPersonIdNumberList(first, pageSize);
                }

            }

            log.info("------------------梁山县电子证照数据同步结束--------------------");
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            log.info("------------------梁山县电子证照数据同步异常--------------------");
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    public void syncPersonCertInfo(Record zrrCert, Record zrrInfo) {

        String certName = zrrCert.getStr("cert_name");
        String certTypeCode = zrrCert.getStr("cert_code");
        String idNumber = zrrInfo.getStr("idnumber");
        String applyName = zrrInfo.getStr("username");
        String mobile = zrrInfo.getStr("mobile");

        String result = syncCertInfo(idNumber, certTypeCode, "20");
        if (StringUtil.isNotBlank(result)) {
            log.info("自然人证照信息" + idNumber + "-" + certTypeCode + "：" + result);
            JSONArray resultArray = JSONObject.parseArray(result);
            if (resultArray != null && !resultArray.isEmpty()) {

                for (Object resultObject : resultArray) {

                    JSONObject jObject = (JSONObject) resultObject;
                    if (jObject.containsKey("zzyxqqsrq")) { // 不包含起始日期跳过
                        continue;
                    }

                    handleGxhImportCertLs(jObject, certName, certTypeCode, idNumber, applyName, mobile);

                }
            }
        }

    }

    public void syncCompanyCertInfo(Record frCert, Record frInfo) {

        String certName = frCert.getStr("cert_name");
        String certTypeCode = frCert.getStr("cert_code");
        String idNumber = frInfo.getStr("creditcode");
        String applyName = frInfo.getStr("organname");
        String mobile = frInfo.getStr("contactmobile");

        String result = syncCertInfo(idNumber, certTypeCode, "10");
        if (StringUtil.isNotBlank(result)) {
            log.info("法人证照信息" + idNumber + "-" + certTypeCode + "：" + result);
            JSONArray resultArray = JSONObject.parseArray(result);
            if (resultArray != null && !resultArray.isEmpty()) {
                for (Object resultObject : resultArray) {

                    JSONObject jObject = (JSONObject) resultObject;
                    if (!jObject.containsKey("zzyxqqsrq")) { // 不包含起始日期跳过
                        continue;
                    }

                    handleGxhImportCertLs(jObject, certName, certTypeCode, idNumber, applyName, mobile);
                }
            }
        }

    }

    public void handleGxhImportCertLs(JSONObject jObject, String certName, String certTypeCode, String idNumber, String applyName, String mobile) {

        IGxhImportCertLsService gxhImportCertLsService = ContainerFactory.getContainInfo().getComponent(IGxhImportCertLsService.class);

        Date yxqstartdate = EpointDateUtil.convertString2Date(jObject.getString("zzyxqqsrq"), "yyyy-MM-dd");
        Date yxqenddate = EpointDateUtil.convertString2Date(jObject.getString("zzyxqjzrq"), "yyyy-MM-dd");
        GxhImportCertLs gxhImportCertLs = gxhImportCertLsService.getCertLsByTyshxydmAndCerttypecode(idNumber, certTypeCode);
        if (gxhImportCertLs == null) {
            gxhImportCertLs = new GxhImportCertLs();
            gxhImportCertLs.setRowguid(UUID.randomUUID().toString());
            gxhImportCertLs.setXkzh(jObject.getString("certno"));
            gxhImportCertLs.setCerttypecode(certTypeCode);
            gxhImportCertLs.setCertname(certName);
            gxhImportCertLs.setYxqstartdate(yxqstartdate);
            gxhImportCertLs.setYxqenddate(yxqenddate);
            gxhImportCertLs.setApplyname(applyName);
            gxhImportCertLs.setTyshxydm(idNumber);
            gxhImportCertLs.setMobile(mobile);
            gxhImportCertLs.setSmstatus("10");

            gxhImportCertLsService.insert(gxhImportCertLs);

        }
        else {
            if (gxhImportCertLs.getYxqstartdate().before(yxqstartdate)) {

                gxhImportCertLs.setXkzh(jObject.getString("certno"));
                gxhImportCertLs.setCerttypecode(certTypeCode);
                gxhImportCertLs.setCertname(certName);
                gxhImportCertLs.setYxqstartdate(yxqstartdate);
                gxhImportCertLs.setYxqenddate(yxqenddate);
                gxhImportCertLs.setApplyname(applyName);
                gxhImportCertLs.setTyshxydm(idNumber);
                gxhImportCertLs.setMobile(mobile);
                gxhImportCertLs.setSmstatus("10");

                gxhImportCertLsService.update(gxhImportCertLs);
            }
        }
    }

    public String syncCertInfo(String idNumber, String certTypeCode, String certType) {

        String result = "";

        String holderTypeCode = "111"; // 默认个人身份证

        if ("10".equals(certType)) {
            holderTypeCode = "001"; // 统一社会信用代码
        }

        JSONObject requestjson = new JSONObject();
        JSONObject headjson1 = new JSONObject();
        JSONObject datajson1 = new JSONObject();

        headjson1.put("accessId", accessId);
        headjson1.put("accessToken", accessToken);
        requestjson.put("head", headjson1);

        datajson1.put("holderCode", idNumber);
        datajson1.put("holderTypeCode", holderTypeCode);
        datajson1.put("certTypeCode", certTypeCode);
        datajson1.put("useCause", "济宁市一窗受理系统");
        requestjson.put("data", datajson1);

        log.info("getpdfreason:" + requestjson.toJSONString());

        String httpUrl = "http://59.206.96.173:8080/main/CertShare/queryCertInfoByHolderCode";
        String postResult = TaHttpRequestUtils.sendPost(httpUrl, requestjson.toJSONString(), "", "");
        JSONObject postResultJson = JSONObject.parseObject(postResult);
        JSONObject headJson = postResultJson.getJSONObject("head");

        if ("0".equals(headJson.getString("status"))) {
            JSONObject dataJson = postResultJson.getJSONObject("data");
            result = SM2Utils.getDecrypt(privatekey, dataJson.getString("certData"));

        }
        else {
            log.info(headJson.toJSONString());
        }

        return result;
    }
}
