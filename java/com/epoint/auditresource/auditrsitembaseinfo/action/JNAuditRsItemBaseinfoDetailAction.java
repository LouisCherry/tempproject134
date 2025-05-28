package com.epoint.auditresource.auditrsitembaseinfo.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;

/**
 * 项目基本信息表详情页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 10:25:43]
 */
@RestController("jnauditrsitembaseinfodetailaction")
@Scope("request")
public class JNAuditRsItemBaseinfoDetailAction extends BaseController
{
    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    /**
     * 项目基本信息表实体对象
     */
    private AuditRsItemBaseinfo dataBean = null;
    /**
     * 项目库接口
     */
    @Autowired
    private IAuditRsItemBaseinfo auditRsItemBaseinfoImpl;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean=auditRsItemBaseinfoImpl.getAuditRsItemBaseinfoByRowguid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
    }

    public AuditRsItemBaseinfo getDataBean() {
        return dataBean;
    }
    
    /**
     * 生成人员库tab的json（基本信息，证照信息，申报历史）
     */
    public void initTabData() {
        String itemguid = getRequestParameter("guid");
        List<Record> recordList = new ArrayList<Record>();
        // 基本信息
        Record jbxx = new Record();
        jbxx.put("name", "项目信息");
        jbxx.put("code", "jbxx");
        jbxx.put("icon", "modicon-6");
        jbxx.put("url", "epointzwfw/auditbusiness/auditresource/iteminfo/auditrsitembaseinfodetail?guid=" + itemguid);
        recordList.add(jbxx);
        // 证照信息
        Record zzxx = new Record();
        zzxx.put("name", "材料信息");
        zzxx.put("code", "zzxx");
        zzxx.put("icon", "modicon-11");
        zzxx.put("url", "epointzwfw/auditbusiness/auditresource/iteminfo/auditxiangmumaterialdetail?guid="+ itemguid);
        recordList.add(zzxx);
        // 申报历史
        Record sbls = new Record();
        sbls.put("name", "办件信息");
        sbls.put("code", "sbls");
        sbls.put("icon", "modicon-10");
        sbls.put("url", "epointzwfw/auditbusiness/auditresource/iteminfo/auditxiangmuprojectdetail?guid="+itemguid);
        recordList.add(sbls);
        
        
        Record lcjg = new Record();
        lcjg.put("name", "流程监管");
        lcjg.put("code", "lcjg");
        lcjg.put("icon", "modicon-12");
        //lcjg.put("url", "jiningzwfw/individuation/overall/auditbusiness/auditresource/iteminfo/auditxiangmusupervisedetail?guid="+itemguid);
        lcjg.put("url", "jiningzwfw/pages/iteminfo/Html/flow_building?guid="+itemguid);
        recordList.add(lcjg);
        sendRespose(JsonUtil.listToJson(recordList));
    }
}
