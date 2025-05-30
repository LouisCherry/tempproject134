package com.epoint.certbasic.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController
@RequestMapping("/certinfos")
public class CertinfoControllerRest
{
    private Logger log = Logger.getLogger(CertinfoControllerRest.class);

    @Autowired
    private ICertInfo iCertInfo;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IAttachService attachService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getcertbybh", method = RequestMethod.POST)
    public String getcertbybh(@RequestBody String params, HttpServletRequest request) {
        try {
            log.info("==============开始调用getcertbybh==============");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");

            if (!ZwdtConstant.SysValidateData.equals(token)) {
                return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
            }
            // 获取证照编号
            String zzbh = obj.getString("zzbh");
            // 统一社会信用代码
            String tyshxydm = obj.getString("tyshxydm");

            // 非空判断
            if (StringUtil.isBlank(tyshxydm)) {
                return JsonUtils.zwdtRestReturn("0", "tyshxydm不能为空！", "");
            }

            if (StringUtil.isBlank(zzbh)) {
                return JsonUtils.zwdtRestReturn("0", "zzbh不能为空！", "");
            }

            // 通过zzbh获取mongodb中的CertInfoExtension
            NoSQLSevice noSQLSevice = new NoSQLSevice();
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("bh", zzbh);
            CertInfoExtension certInfoExtension = noSQLSevice.find(CertInfoExtension.class, filter, false);
            String certinfoguid = certInfoExtension.getStr("certinfoguid");

            // 获取证照实例
            CertInfo certInfo = iCertInfo.getCertInfoByRowguid(certinfoguid);

            // 打印证照实例和拓展信息,开发调试用
            log.info("certinfo : " + certInfo);
            log.info("certInfoExtension : " + certInfoExtension);

            // 关联办件表
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("CERTROWGUID", certInfo.getRowguid());
            AuditProject project = iAuditProject.getAuditProjectByCondition(sql.getMap());

            if (project == null) {
                return JsonUtils.zwdtRestReturn("0", "获取办件信息失败！", "");
            }
            if (!tyshxydm.equals(project.getCertnum())) {
                return JsonUtils.zwdtRestReturn("0", "统一社会信用代码输入有误！", "");
            }

            // 获取证照附件信息
            List<FrameAttachInfo> attachInfos = attachService.getAttachInfoListByGuid(certInfo.getCertcliengguid());
            String path = "";
            JSONObject datajson = new JSONObject();
            for (FrameAttachInfo frameAttachInfo : attachInfos) {
                path = attachService.getAttachDownPath(frameAttachInfo);
                log.info("下载地址为: " + path);
                datajson.put("path", path);

            }
            return JsonUtils.zwdtRestReturn("1", "请求成功！", datajson);

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "异常错误！", "");
        }

    }

}
