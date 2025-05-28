package com.epoint.auditqueue.auditznsbremotehelp.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;

@RestController("auditznsbremotehelptabaction")
@Scope("request")
public class AuditZnsbRemoteHelpTabAction extends BaseController {
    
    private static final long serialVersionUID = -6684751409062289728L;

    public void pageLoad() {
    }

    public void initTabData() {
        List<Record> recordList = new ArrayList<Record>();
        Record kfry = new Record();
        kfry.put("name", "客服人员账户配置");
        kfry.put("code", "kfry");
        kfry.put("url", "znsb/auditznsbremotehelpuser/auditznsbremotehelpuserlist");
        recordList.add(kfry);
        
        Record zzzd = new Record();
        zzzd.put("name", "自助终端账户配置");
        zzzd.put("code", "zzzd");
        zzzd.put("url", "znsb/auditznsbremotehelp/auditznsbremotehelplist");
        recordList.add(zzzd);
        sendRespose(JsonUtil.listToJson(recordList));
    }
}
