package com.epoint.jnzwdt.cert.rest;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/cert")
public class ApiCertRestController
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ICertInfoExternal iCertInfoService;
    
    @Autowired
    private IBusinessLicenseBaseinfo iBusinessLicenseBaseinfo;
    
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;
    
    @Autowired
    private IAttachService attachService;

    /**
     * 获取证照照面信息
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getCertinfo", method = RequestMethod.POST)
    public String getCertinfo(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertinfo接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                CertInfo certInfo = iCertInfoService.getCertInfoByRowguid(obj.getString("certinfoguid"));

                if (certInfo == null) {
                    return JsonUtils.zwdtRestReturn("0", "参数错误，数据为空!", "");
                }

                String appkey = ConfigUtil.getConfigValue("businesslicense", certInfo.getAreacode());
                if (StringUtil.isBlank(appkey)) {
                    appkey = obj.getString("appkey");
                }

                JSONObject sumbit = new JSONObject();
                JSONObject param = new JSONObject();
                param.put("certno", certInfo.getCertno());
                param.put("certcatalogid", "");
                param.put("tycertcatcode", certInfo.getTycertcatcode());
                sumbit.put("appkey", appkey);
                sumbit.put("params", param);

                log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
                log.info("=======appkey=======" + appkey);

                // 发送请求
                String note = TARequestUtil.sendPost(
                        "http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtension", sumbit.toJSONString(),
                        "", "");
                log.info("返回的证照信息：" + note);
                JSONObject object = JSONObject.parseObject(note);
                JSONObject object2 = object.getJSONObject("custom");
                JSONObject object3 = object2.getJSONObject("certinfoextension");

                if (object3 == null) {
                    return JsonUtils.zwdtRestReturn("1", "信息查询失败！", object2.getString("text"));
                }

                String zzbh = object3.getString("zzbh");
                String jsgs = object3.getString("jsgs");
                String xmmc = object3.getString("xmmc");
                String bsfhpjbg = object3.getString("bsfhpjbg");
                String scfhpjbg = object3.getString("scfhpjbg");
                String fjfhpjbg = object3.getString("fjfhpjbg");

                JSONObject info = new JSONObject();
                info.put("zzbh", zzbh); // 证书编号
                info.put("jsgs", jsgs); // 企业名称
                info.put("frdb", certInfo.getCertownername()); // 法人代表
                info.put("tyxydmz", certInfo.getCertownerno()); // 统一信用代码证
                info.put("fzjg", certInfo.getCertawarddept()); // 发证机关
                info.put("fzrq", EpointDateUtil.convertDate2String(certInfo.getAwarddate(), "yyyy-MM-dd HH:mm:ss")); // 发证日期
                info.put("yxq", certInfo.getExpiredateto()); // 有效期
                info.put("xmmc", xmmc); //
                info.put("bsfhpjbg", bsfhpjbg); //
                info.put("scfhpjbg", scfhpjbg); //
                info.put("fjfhpjbg", fjfhpjbg); //
                
                //查找certrowguid对应的办件材料附件
                JSONArray jsonArray = new JSONArray();
                String projectguid = iBusinessLicenseBaseinfo.findProjectByCertrowguid(obj.getString("certinfoguid"));
                if(StringUtil.isNotBlank(projectguid)) {
                	SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();// 补正材料检索条件
                    materialSqlConditionUtil.eq("projectguid", projectguid);
                    materialSqlConditionUtil.in("auditstatus", ZwfwConstant.Material_AuditStatus_SHTG);
                    List<AuditProjectMaterial> materialList = iAuditProjectMaterial.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();
                    if( (materialList!=null) && (!materialList.isEmpty()) ) {
                    	for (AuditProjectMaterial projectMaterial : materialList) {
                    		if("建设项目防洪评价报告".equals(projectMaterial.getTaskmaterial()) || "建设项目涉及河道与防洪部分的建设方案（含图纸）".equals(projectMaterial.getTaskmaterial())
                    				|| "建设单位对河道管理范围内建设项目工程建设方案审查的申请".equals(projectMaterial.getTaskmaterial()) ) {
                    			List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(projectMaterial.getCliengguid());
                    			if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {
                                    for (FrameAttachInfo attachinfo : frameAttachInfos) {
                                    	JSONObject attachobj = new JSONObject();
                                    	attachobj.put("attachguid", attachinfo.getAttachGuid());
                                    	attachobj.put("attachname", attachinfo.getAttachFileName());
                                    	 jsonArray.add(attachobj);
                                    }
                    			}
                    		}
                    	}
                    }
                }
                info.put("materials", jsonArray); 

                JSONObject resultjson = new JSONObject();
                resultjson.put("info", info);
                resultjson.put("total", 1);
                return JsonUtils.zwdtRestReturn("1", "信息查询成功！", resultjson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "信息查询失败", "");
        }
    }

    /*@RequestMapping(value = "/getCertinfo", method = RequestMethod.POST)
    public String getCertinfo(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("=======开始调用getCertinfo接口=======");
            // 1.解析入参
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                JSONObject info = new JSONObject();
                info.put("zzbh", "12213"); // 证书编号
                info.put("jsgs", "国泰新点"); // 企业名称
                info.put("frdb", "曹立斌"); // 法人代表
                info.put("tyxydmz", "1233322222222"); // 统一信用代码证
                info.put("fzjg", "苏州市监局"); // 发证机关
                info.put("fzrq", "2023-07-01 10:10:21"); // 发证日期
                info.put("yxq", "2023-11-11 10:10:21"); // 有效期
                info.put("xmmc", "测试项目"); //
                info.put("bsfhpjbg", "1"); //
                info.put("scfhpjbg", "2"); //
                info.put("fjfhpjbg", "3"); //
                
                //查找certrowguid对应的办件材料附件
                JSONArray jsonArray = new JSONArray();
                JSONObject attachobj = new JSONObject();
            	attachobj.put("attachguid", "a6c14ba0-cbf3-4647-ac2b-ebbc6aad4e79");
            	attachobj.put("attachname", "一体机--功能介绍.xlsx");
            	 jsonArray.add(attachobj);
            	 
            	 JSONObject attachobj2 = new JSONObject();
            	 attachobj2.put("attachguid", "5e16668e-651f-4658-b561-0ac070be6415");
            	 attachobj2.put("attachname", "一体机远程.xlsx");
             	 jsonArray.add(attachobj2);
             	 
             	JSONObject attachobj3 = new JSONObject();
             	attachobj3.put("attachguid", "fc6e67f1-3836-4b72-b98d-f0b9ef14fbfc");
             	attachobj3.put("attachname", "一体机功能介绍.xlsx");
            	jsonArray.add(attachobj3);

                info.put("materials", jsonArray); 

                JSONObject resultjson = new JSONObject();
                resultjson.put("info", info);
                resultjson.put("total", 1);
                return JsonUtils.zwdtRestReturn("1", "信息查询成功！", resultjson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "信息查询失败", "");
        }
    }*/
}
