package com.epoint.yyyz.businesslicense.handle;

import com.alibaba.fastjson.JSONObject;
import com.epoint.auditclient.listener.AuditClientMessageListener;
import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditmqmessage.domain.AuditMqMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.dataassetsexport.api.IDataassetsExportService;
import com.epoint.basic.audittask.dataassetsexport.api.entity.DataassetsExport;
import com.epoint.basic.audittask.dataassetsexportdetail.api.IDataassetsExportDetailService;
import com.epoint.basic.audittask.dataassetsexportdetail.api.entity.DataassetsExportDetail;
import com.epoint.common.util.ProjectConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.api.IZwdtCommonService;
import com.epoint.xmz.util.TaHttpRequestUtils;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.util.AESUtil;
import com.epoint.yyyz.businesslicense.util.SafetyUtil;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

public class PushProjectStatusClientHandle extends AuditClientMessageListener {


    public void handleMessage(AuditMqMessage proMessage) throws Exception {
        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
        log.info("=========proMessage=====" + proMessage);
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        //新增证照联办推送
        IAuditSpBusiness auditSpBusinessService =  ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
        IBusinessLicenseBaseinfo businessLicenseBaseinfoService = ContainerFactory.getContainInfo().getComponent(IBusinessLicenseBaseinfo.class);
        IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IConfigService configServicce = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        IZwdtCommonService zwdtCommonService = ContainerFactory.getContainInfo().getComponent(IZwdtCommonService.class);
        String[] sendroutingkey = proMessage.getSendroutingkey().split("\\.");
        if (ProjectConstant.SJZCLX_DSX.equals(sendroutingkey[1])) {
            String projectguid = sendroutingkey[2];
         // 根据主题标准guid busineeguid查询主题，然后判断是否是三联办申报数据
            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            sqlUtil.eq("rowguid", projectguid);
            AuditProject auditProject = auditProjectService.getAuditProjectByCondition(sqlUtil.getMap());
            if(StringUtil.isNotBlank(auditProject)) {
                if(StringUtil.isNotBlank(auditProject.getBusinessguid()) && StringUtil.isNotBlank(auditProject.getBiguid())) {
                    AuditSpBusiness auditSpBusiness = auditSpBusinessService.getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                    String zzlbsql = "select * from businesslicense_baseinfo where biguid = ? and businessguid = ?";
                    BusinessLicenseBaseinfo licenseBaseinfo = businessLicenseBaseinfoService.find(zzlbsql, auditProject.getBiguid(),auditProject.getBusinessguid());
                    if(StringUtil.isNotBlank(auditSpBusiness) && StringUtil.isNotBlank(auditSpBusiness.getBusinesstype()) && StringUtil.isNotBlank(licenseBaseinfo)) {
                        //获取三联办申报时的业务流水号
                        String serino = licenseBaseinfo.getSerialNo();
                        String appKey = configServicce.getFrameConfigValue("AS_YYYZ_AppKey");
                        String encryptKey = configServicce.getFrameConfigValue("AS_YYYZ_EncryptKey");
                        String tokenUrl = configServicce.getFrameConfigValue("AS_YYYZ_TokenUrl");
                        String zzlbProjectUrl  = configServicce.getFrameConfigValue("zzlbProjectUrl");
                        // 获取token
                        String banjietoken;
                        String tokenJson = SafetyUtil.doHttpPost(tokenUrl, appKey);
                        JSONObject tokenObject = JSONObject.parseObject(tokenJson);
                        banjietoken = tokenObject.getString("token");
                        //调用接口推送办件办结信息
                        Record auditTaskTaian = zwdtCommonService.getAuditTaskTaianByTaskId(auditProject.getTask_id());
                        if(StringUtil.isNotBlank(auditTaskTaian) && StringUtil.isNotBlank(auditTaskTaian.getStr("zzlbitemcode"))) {
                            //拼接办结信息，字段分别为：三联办申报业务流水号，state07 办结，item入参为三联办事项编码
                            String banjieInfo = "[{\"serialNo\":\""+serino+"\",\"state\":\"07\",\"item\":\""+auditTaskTaian.getStr("zzlbitemcode")+"\" }]";
                            String encryInfo = AESUtil.aesEncode(banjieInfo, encryptKey);
                            JSONObject obj = new JSONObject();
                            obj.put("token", banjietoken);
                            obj.put("appKey", appKey);
                            obj.put("data", encryInfo);
                            String returnInfo = TaHttpRequestUtils.sendPostRequest(obj.toJSONString(),zzlbProjectUrl+"/yyyz/restful/status/update");
                            log.info("推送证照联办办件办结，流水号为："+auditProject.getFlowsn()+",返回接口信息==" + returnInfo);
                        }
                        
                    }
                }
            }
        }
    }


}
