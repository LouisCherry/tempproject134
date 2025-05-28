package com.epoint.auditsp.auditsphandle.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zbxfdj.controller.TsProjectDataRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消防办件基本信息页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2016-10-14 11:46:07]
 */
@RestController("jnxfinfoaction")
@Scope("request")
public class JnXfInfoAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -4246072106352276268L;
    /**
     * 办件查询字段
     */
    private String fields = " xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,ACCEPTUSERGUID,rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,is_test,if_express,spendtime,subappguid,businessguid,applydate,currentareacode,handleareacode,acceptareacode,legalid,task_id,IS_SAMECITY,dataObj_baseinfo,dataObj_baseinfo_other";

    // 数据表名
    private String SQLTableName = "AUDIT_PROJECT";
    /**
     * 办件表实体对象
     */
    private AuditProject auditproject = null;
    /**
     * 事项表实体对象
     */
    private AuditTask audittask = null;
    /**
     * 事项扩展表实体对象
     */
    private AuditTaskExtension auditTaskExtension = null;

    @Autowired
    private IConfigService configServicce;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    IAttachService attachService;

    private String formid = "";

    @Autowired
    private IAuditSpPhase iauditspphase;

    private String lsh;

    @Override
    public void pageLoad() {
        lsh = getRequestParameter("flowsn");
        auditproject = auditProjectService.getAuditProjectByFlowsn(lsh, "").getResult();
        if (auditproject != null) {
            // 1、获取事项信息
            audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), true).getResult();
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditproject.getTaskguid(), true).getResult();
            // 如果是并联审批的办件，那就需要返回项目信息
            if (StringUtil.isNotBlank(auditproject.getBiguid())) {
                // 工改单事项表单展示
                String formids = auditTaskExtension.getStr("formid");
                if (StringUtil.isNotBlank(formids)) {
                    AuditSpISubapp subapp = iAuditSpISubapp.getSubappByGuid(auditproject.getSubappguid())
                            .getResult();
                    AuditSpPhase auditSpPhase = iauditspphase.getAuditSpPhaseByRowguid(subapp.getPhaseguid()).getResult();
                    if (StringUtil.isNotBlank(auditSpPhase.getStr("formid"))) {
                        addCallbackParam("formid", auditSpPhase.getStr("formid"));
                        addCallbackParam("formids", formids);
                        addCallbackParam("yewuGuid", subapp.getRowguid());
                        addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
                        addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
                    }
                    addCallbackParam("certnum", auditproject.getCertnum());
                }
            }
        }

    }

    public void sendXf() {
        TsProjectDataRest tsprojectdatarest = ContainerFactory.getContainInfo().getComponent(TsProjectDataRest.class);
        log.info("需要上报的消防事项" + auditproject.getRowguid());
        tsprojectdatarest.tsprojectdata(auditproject.getRowguid());
    }

}
