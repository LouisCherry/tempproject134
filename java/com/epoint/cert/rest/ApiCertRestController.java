package com.epoint.cert.rest;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;

@RestController
@RequestMapping("/cert")
public class ApiCertRestController
{

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ICertInfoExternal iCertInfoService;

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

                CertInfo certInfo = iCertInfoService.getCertInfoByRowguid(obj.getString("rowguid"));

                CertInfoExtension certInfoExtension = iCertInfoService
                        .getCertExtenByCertInfoGuid(obj.getString("rowguid"));

                if (certInfo == null || certInfoExtension == null) {
                    return JsonUtils.zwdtRestReturn("0", "参数错误，数据为空!", "");
                }

                JSONObject info = new JSONObject();
                info.put("zzbh", certInfoExtension.getStr("zzbh")); // 证书编号
                info.put("jsgs", certInfoExtension.getStr("jsgs")); // 企业名称
                info.put("frdb", certInfo.getCertownername()); // 法人代表
                info.put("tyxydmz", certInfo.getCertownerno()); // 统一信用代码证
                info.put("fzjg", certInfo.getCertawarddept()); // 发证机关
                info.put("fzrq", certInfo.getAwarddate()); // 发证日期
                info.put("yxq", certInfo.getExpiredateto()); // 有效期
                info.put("xmmc", certInfoExtension.getStr("xmmc")); //
                info.put("bsfhpjbg", certInfoExtension.getStr("bsfhpjbg")); //
                info.put("scfhpjbg", certInfoExtension.getStr("scfhpjbg")); //
                info.put("fjfhpjbg", certInfoExtension.getStr("fjfhpjbg")); //

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

}
