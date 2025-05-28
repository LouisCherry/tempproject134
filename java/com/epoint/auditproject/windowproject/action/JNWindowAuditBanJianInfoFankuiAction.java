package com.epoint.auditproject.windowproject.action;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;

/**
 * 核查反馈数据后台
 * 
 * @author Mr.Du
 * @version [版本号, 2023-08-09 15:30:06]
 */
@RestController("jnwindowauditbanjianinfofankuiaction")
@Scope("request")
public class JNWindowAuditBanJianInfoFankuiAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 前台核查数据渲染实体
     */
    private Record dataBean = null;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private ICertInfoExternal certInfoExternalService;

    public void pageLoad() {
        String projectguid = getRequestParameter("projectGuid");

        AuditProject auditproject = auditProjectService.getAuditProjectByRowGuid("certrowguid", projectguid, "")
                .getResult();
        if (dataBean == null) {
            dataBean = new Record();
        }
        String certrowguid = auditproject.getCertrowguid();

        if (StringUtil.isBlank(certrowguid)) {
            return;
        }
        String[] certrowguids = certrowguid.split(";");
        CertInfo cert = certInfoExternalService.getCertInfoByRowguid(certrowguids[0]);
        JSONObject parmasJson = new JSONObject();
        parmasJson.put("text_input_5ks5s5", cert.getCertno());
        // parmasJson.put("text_input_5ks5s5", "GXSP2023（ZT运输）-0015号");
        String returnString = HttpUtil.doPostJson(ConfigUtil.getFrameConfigValue("check_url"), parmasJson.toString());

        log.info("调用三方接口返回核查数据：" + returnString);
        JSONObject returnJson = JSONObject.parseObject(returnString);
        JSONArray resultArray = returnJson.getJSONArray("result");
        if (resultArray != null && !resultArray.isEmpty()) {
            JSONObject checkJson = resultArray.getJSONObject(0);
            dataBean.set("flowsn", checkJson.getString("text_input_5ks5s5"));
            dataBean.set("checkuser", checkJson.getString("text_input_dcu4c5"));
            dataBean.set("checkuserno", checkJson.getString("text_input_eh8nj"));
            dataBean.set("checkresult", checkJson.getString("editor_gi5ik8"));
            dataBean.set("checktime", checkJson.getString("date_picker_8676h8"));
        }
    }

    public Record getDataBean() {
        return dataBean;
    }
}
