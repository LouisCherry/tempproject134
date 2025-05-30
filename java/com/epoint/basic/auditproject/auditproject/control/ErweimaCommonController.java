package com.epoint.basic.auditproject.auditproject.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.businesslicense.api.entity.BusinessLicenseExtension;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.*;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.zwdt.util.TARequestUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 证照二维码
 */
@RestController
@RequestMapping("/erweimacommon")
public class ErweimaCommonController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 基本信息API
     */
    @Autowired
    private IBusinessLicenseBaseinfo baseinfoService;

    /**
     * 扩展信息API
     */
    @Autowired
    private ICertInfo certInfoService;

    @Autowired
    private ICertConfigExternal iCertConfigExternal;
    
    @Autowired
    private IAttachService attachService;

    
    /**
     * 获取证照二维码信息
     */
    @RequestMapping(value = "/getCertinfoList", method = RequestMethod.POST)
    public String getCertinfoList(@RequestBody String params) {

        log.info("=======开始调用getCertinfoList接口======="
                + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        log.info("=======params=======" + params);
        JSONObject dataJson = new JSONObject();
        JSONObject param = new JSONObject();
        JSONObject sumbit = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = certInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                if (certInfo.getIshistory() == 1) {
    				return JsonUtils.zwdtRestReturn("5", "该证照已注销！", dataJson.toJSONString());
    			}

                //获取该证照的照面名称，照面值，按顺序排放
                JSONArray infosjsonArray = new JSONArray();
                //获取照面名称
                List<CertMetadata> metadataList =  iCertConfigExternal.selectMetadataByIdAndVersion(certInfo.getCertcatalogid(),
                        String.valueOf(certInfo.getCertcatalogversion()), certInfo.getAreacode());

                //获取照面值
                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());

                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);
                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                //发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension",
                        sumbit.toJSONString(), "", "");
                log.info("返回的证照信息："+note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                if(metadataList!=null && !metadataList.isEmpty()){
                    for(CertMetadata metadata:metadataList){
                        Map<String, String> infomap = new HashMap<String, String>();
                        infomap.put("infoname", metadata.getFieldchinesename());
                        infomap.put("infovalue", object3.getString(metadata.getFieldname()));
                        infosjsonArray.add(infomap);
                    }
                    dataJson.put("infos", infosjsonArray);
                }
                
                JSONArray jsonArray = new JSONArray();
                if(StringUtil.isNotBlank(certInfo.get("ftfjcliengguid"))) {
                	List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(certInfo.get("ftfjcliengguid"));
                	if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {
                        for (FrameAttachInfo attachinfo : frameAttachInfos) {
                        	JSONObject attachobj = new JSONObject();
                        	attachobj.put("attachguid", attachinfo.getAttachGuid());
                        	attachobj.put("attachname", attachinfo.getAttachFileName());
                        	 jsonArray.add(attachobj);
                        }
        			}
                }

                dataJson.put("materials", jsonArray);

                log.info("=======结束调用getCertinfoList接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("=======结束调用getCertinfoList接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======getCertinfoList异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
    }
    
    
}
