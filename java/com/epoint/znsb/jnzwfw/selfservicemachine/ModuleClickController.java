package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbmodule.domain.FunctionModule;
import com.epoint.basic.auditqueue.auditznsbnoticemanager.domain.AuditZnsbNoticeManager;
import com.epoint.basic.auditqueue.auditznsbnoticemanager.inter.IAuditZnsbNoticeManagerService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.znsb.jnzwfw.module.IAuditZnsbModuleService;
import com.epoint.znsb.jnzwfw.module.entity.AuditZnsbModule;
/**
 * 
 *  [模块插入rest] 
 * @author chencong
 * @version [版本号, 2019年10月10日]
 */
@RestController
@RequestMapping("/moduleClick")
public class ModuleClickController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditZnsbModuleService moduleservice;
    @Autowired
    private IConfigService configservice;
    /**
     * 插入点击模块信息
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/insertOnClickModule", method = RequestMethod.POST)
    public String insertOnClickModule(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String CardID = obj.getString("cardID");
            String Centerguid = obj.getString("centerguid");
            String Modulename = obj.getString("modulename");
            String Macaddress = obj.getString("macaddress");
            String fromareacode = obj.getString("fromareacode");
            String basecode = obj.getString("basecode");

            if (StringUtil.isBlank(Centerguid) || StringUtil.isBlank(Modulename) || StringUtil.isBlank(Macaddress)) {
                return JsonUtils.zwdtRestReturn("0", "Centerguid,ModuleName,MacAddress都不能为空", "");
            }
            else {
                AuditZnsbModule module = new AuditZnsbModule();
                module.setRowguid(UUID.randomUUID().toString());
                module.setCardid(CardID);
                module.setCenterguid(Centerguid);
                module.setModulename(Modulename);
                module.setMacaddress(Macaddress);
                module.setOnclicktime(new Date());
                module.setFromareacode(fromareacode);
                module.setToareacode(basecode);
                module.setIssync(QueueConstant.Common_no_String);
                moduleservice.insert(module);
                return JsonUtils.zwdtRestReturn("1", "", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
