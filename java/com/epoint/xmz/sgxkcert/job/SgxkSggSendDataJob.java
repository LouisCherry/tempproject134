package com.epoint.xmz.sgxkcert.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.auditsp.auditspspsgxk.inter.IAuditSpSpSgxkService;
import com.epoint.basic.auditsp.dantiinfo.api.IDantiInfoService;
import com.epoint.basic.auditsp.dantiinfo.entity.DantiInfo;
import com.epoint.basic.basedata.participantsinfo.api.IParticipantsInfoService;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;

/**
 * 
 * 施工许可数据上报到省工改平台job
 * 
 * @作者 65250
 * @version [版本号, 2021年9月24日]
 */
@DisallowConcurrentExecution
public class SgxkSggSendDataJob implements Job
{
    private Logger log = Logger.getLogger(SgxkSggSendDataJob.class);
    /**
     * 证照api
     */
    private ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo()
            .getComponent(ICertInfoExternal.class);

    private IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    private IAuditRsItemBaseinfo auditRsItemBaseinfoService = ContainerFactory.getContainInfo()
            .getComponent(IAuditRsItemBaseinfo.class);
    private IAuditSpISubapp auditSpISubappService = ContainerFactory.getContainInfo()
            .getComponent(IAuditSpISubapp.class);
    private IAuditSpSpSgxkService sgxkService = ContainerFactory.getContainInfo()
            .getComponent(IAuditSpSpSgxkService.class);
    private IDantiInfoService dantiService = ContainerFactory.getContainInfo().getComponent(IDantiInfoService.class);
    private IParticipantsInfoService iparticipantsinfoservice = ContainerFactory.getContainInfo()
            .getComponent(IParticipantsInfoService.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        log.info("=====开始向省工改平台同步施工许可证===");
        try {
        	SgxkSggSendDataService service = new SgxkSggSendDataService();
            EpointFrameDsManager.begin(null);
            List<Record> sgxkcertinfolist = service.getCertInfoList();
            if (StringUtil.isNotBlank(sgxkcertinfolist) && sgxkcertinfolist.size() > 0) {
                for (Record sgxkcertinfo : sgxkcertinfolist) {
                    // 向省工改推送数据
                    syncsgxk(sgxkcertinfo);
                }
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
        log.info("=====结束向省工改平台同步施工许可证===");
    }

    /**
     * 向省工改推送数据
     */
    public void syncsgxk(Record sgxkcertinfo) {
    	SgxkSggSendDataService service = new SgxkSggSendDataService();
    	
        Map<String, String> certguid = new HashMap<>();
        certguid.put("CERTROWGUID=", sgxkcertinfo.getStr("RowGuid"));
        AuditProject auditproject = auditProjectService.getAuditProjectByCondition(certguid);
        if (auditproject == null) {
            // 更新本地
            sgxkcertinfo.set("sggsync", 99);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        }
        AuditSpISubapp subapp = auditSpISubappService.getSubappByGuid(auditproject.getSubappguid()).getResult();

        if (subapp == null) {
            // 更新本地
            sgxkcertinfo.set("sggsync", 98);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        }
        AuditRsItemBaseinfo baseinfo = auditRsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(subapp.getYewuguid())
                .getResult();
        if (baseinfo == null) {
            // 更新本地
            sgxkcertinfo.set("sggsync", 97);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        }
        AuditSpSpSgxk sgxk = sgxkService.findAuditSpSpSgxkBysubappguid(auditproject.getSubappguid());
        if (sgxk == null) {
            // 更新本地
            sgxkcertinfo.set("sggsync", 96);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        }
        List<DantiInfo> dantiList = dantiService.findListBySubAppguid(auditproject.getSubappguid());

        if (dantiList == null || dantiList.size() == 0) {
            // 更新本地
            sgxkcertinfo.set("sggsync", 95);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        }
        Date Planstartdate = sgxk.getPlanstartdate();
        Date Planenddate = sgxk.getPlanenddate();
        
        if(StringUtil.isBlank(Planstartdate)) {
        	Planstartdate = sgxkcertinfo.getDate("EXPIREDATEFROM");
        }
        
        if(StringUtil.isBlank(Planenddate)) {
        	Planenddate = sgxkcertinfo.getDate("EXPIREDATETO");
        }
        
        if (StringUtil.isBlank(sgxkcertinfo.getStr("CERTNO")) || StringUtil.isBlank(baseinfo.getItemname()) || StringUtil.isBlank(baseinfo.getItemcode())
        		|| StringUtil.isBlank(baseinfo.getConstructionaddress()) || StringUtil.isBlank(Planstartdate)
        		|| StringUtil.isBlank(Planenddate) || StringUtil.isBlank(baseinfo.getTotalinvest()) || StringUtil.isBlank(sgxkcertinfo.getDate("operatedate"))
        		|| StringUtil.isBlank(auditproject.getOuname()) || StringUtil.isBlank(auditproject.getAcceptusername())
        		) {
        	  // 更新本地
            sgxkcertinfo.set("sggsync", 80);
            // 更新本地库
            service.updateRecord(sgxkcertinfo);
            return;
        
        }
        
        // 工程项目施工许可信息
        Record builderLicenceManage = new Record();
        
        String gcfl = sgxk.getGcyt();
        
        if (StringUtil.isBlank(gcfl)) {
        	builderLicenceManage.set("Prjtype", "其他"); // 项目分类
        }else {
        	builderLicenceManage.set("Prjtype", sgxk.getGcfl()); // 项目分类
        }
        
        builderLicenceManage.setPrimaryKeys("rowguid");
        builderLicenceManage.setSql_TableName("TBBuilderLicenceManage");
        builderLicenceManage.set("operatedate", new Date());
        builderLicenceManage.set("BuilderLicenceNum", sgxkcertinfo.getStr("CERTNO")); // 施工许可证编号
        builderLicenceManage.set("ProjectName", baseinfo.getItemname()); // 工程名称
        builderLicenceManage.set("PrjCode", baseinfo.getItemcode()); // 项目代码
        builderLicenceManage.set("PrjNum", baseinfo.getStr("ITEMCODE")); // 项目编号
        builderLicenceManage.set("AreaCode", sgxkcertinfo.getStr("areacode")); // 项目所属辖区
        builderLicenceManage.set("LocationX", ""); // 项目坐标(经度)
        builderLicenceManage.set("LocationY", ""); // 项目坐标(纬度)
        builderLicenceManage.set("Address", baseinfo.getConstructionaddress()); // 项目地址
        builderLicenceManage.set("prjfunctionnum", sgxk.getGcyt()); // 项目分类
        
        // --01房屋建筑工程02市政基础设施工程99其他
        builderLicenceManage.set("BeginDate", Planstartdate); // 计划开工时间
        builderLicenceManage.set("EndDate", Planenddate); // 计划竣工时间
        
        
        //builderLicenceManage.set("ContractMoney", dataBean.getDouble("htjg")); // 合同金额(万元)
        builderLicenceManage.set("ContractMoney", baseinfo.getTotalinvest()); // 合同金额(万元)
        builderLicenceManage.set("structuretypenum", dantiList.get(0).getJiegoutx()); // 结构体系
        builderLicenceManage.set("PrjSize", baseinfo.getQuantifyconstructcount()); // 建设规模
        builderLicenceManage.set("ReleaseDate", sgxkcertinfo.getDate("AWARDDATE")); // 发证日期
        // 是为 01；否为 02
        String islowshock = dantiList.get(0).getIslowshock();
        if ("1".equals(islowshock)) {
            islowshock = "01";
        }
        else {
            islowshock = "02";
        }
        builderLicenceManage.set("IsShockisolationBuilding", islowshock); // 是否为减隔震建筑
        builderLicenceManage.set("UnitQuantity", dantiList.size()); // 单体建筑数量
        builderLicenceManage.set("UniteDtails", dantiList.get(0).getDantiname()); // 单体建筑明细
        builderLicenceManage.set("CreateDate", new Date()); // 记录登记时间
        builderLicenceManage.set("CheckDepartName", auditproject.getOuname()); // 信息审核部门
        builderLicenceManage.set("CheckPersonName", auditproject.getAcceptusername()); // 信息审核人
        builderLicenceManage.set("RowGuid", sgxkcertinfo.getStr("rowguid")); // 地方数据主键
        // 插入前置库
        service.insertSggRecord(builderLicenceManage);

        // 获取项项目单位信息
        List<ParticipantsInfo> participantsinfolist = iparticipantsinfoservice
                .listParticipantsInfoBySubappGuid(auditproject.getSubappguid());
        for (ParticipantsInfo participants : participantsinfolist) {
            if (StringUtil.isNotBlank(participants.getCorpcode())) {
                // 工程项目参与单位及相关负责人信息
                Record projectCorpInfo = new Record();
                projectCorpInfo.setPrimaryKeys("rowguid");
                projectCorpInfo.setSql_TableName("TBProjectCorpInfo");
                projectCorpInfo.set("operatedate", new Date());
                projectCorpInfo.set("PrjCode", baseinfo.getItemcode()); // 项目代码
                projectCorpInfo.set("PrjNum", baseinfo.getStr("ITEMCODE")); // 项目编号
                Integer dwlx = getDwlxByCorptype(participants.getCorptype());
                if (StringUtil.isBlank(dwlx) || StringUtil.isBlank(participants.getCorpname()) || StringUtil.isBlank(participants.getCorpcode())
                		|| StringUtil.isBlank(participants.getXmfzr()) || StringUtil.isBlank(participants.getXmfzr_idcard()) || StringUtil.isBlank(participants.getXmfzr_phone())
                		|| StringUtil.isBlank(sgxkcertinfo.getStr("CERTNO"))) {
                	continue;
                }
                projectCorpInfo.set("CorpRoleNum", dwlx); // 企业承担角色
                projectCorpInfo.set("CorpName", participants.getCorpname()); // 企业名称
                projectCorpInfo.set("CorpCode", participants.getCorpcode()); // 企业统一社会信用代码
                projectCorpInfo.set("PersonName", participants.getXmfzr()); // 负责人姓名
                projectCorpInfo.set("IDCardTypeNum", "1"); // 负责人证件类型--1身份证
                projectCorpInfo.set("PersonIDCard", participants.getXmfzr_idcard()); // 负责人证件号码
                projectCorpInfo.set("PersonPhone", participants.getXmfzr_phone()); // 负责人手机号码
                projectCorpInfo.set("BuilderLicenceNum", sgxkcertinfo.getStr("CERTNO")); // 施工许可证编号
                projectCorpInfo.set("RowGuid", participants.getRowguid()); // 地方数据主键
                projectCorpInfo.set("BuilderLicenceRowGuid", sgxkcertinfo.getStr("rowguid")); // 施工许可信息表主键
                service.insertSggRecord(projectCorpInfo);
            }
        }
        // 更新本地
        sgxkcertinfo.set("sggsync", 1);
        // 更新本地库
        service.updateRecord(sgxkcertinfo);
    }

    // 1勘察企业2设计企业3施工企业4监理企业5工程总承包单位6质量检测机构99其他
    private Integer getDwlxByCorptype(String corptype) {
        Integer dwlx = 99;
        if ("6".equals(corptype)) {
            dwlx = 5;
        }
        else if ("10".equals(corptype)) {
            dwlx = 6;
        }
        else if ("1".equals(corptype) || "2".equals(corptype) || "3".equals(corptype) || "4".equals(corptype)) {
            dwlx = Integer.valueOf(corptype);
        }
        return dwlx;
    }
}
