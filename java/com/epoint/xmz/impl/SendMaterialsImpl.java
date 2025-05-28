package com.epoint.xmz.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyLegal;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyRegister;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.api.ISendMaterials;

@Component
@Service
public class SendMaterialsImpl implements ISendMaterials
{

    /**
     * 
     */
    private static final long serialVersionUID = -1806078688745872696L;

    @Override
    public Record getMaterialsInfo(String attachguid) {
        return new SendMaterialsService().getMaterialsInfo(attachguid);
    }

    @Override
    public AuditProject getAuditProjectByRowguid(String rowguid) {
        return new SendMaterialsService().getAuditProjectByRowguid(rowguid);
    }

    @Override
    public String getStrFlowSn(String numberName, String numberFlag, int snLength) {
        return new SendMaterialsService().getStrFlowSn(numberName, numberFlag, snLength);
    }

    @Override
    public String getSearchPwd(String projectguid) {
        return new SendMaterialsService().getSearchPwd(projectguid);
    }

    @Override
    public int updateSearchPwd(String projectguid) {
        return new SendMaterialsService().updateSearchPwd(projectguid);
    }

    @Override
    public Record getProjectProcessInfo(String projectGuid) {
        return new SendMaterialsService().getProjectProcessInfo(projectGuid);
    }

    @Override
    public int insertIndivdual(AuditOnlineIndividual record) {
        return new SendMaterialsService().insertIndivdual(record);
    }

    @Override
    public int insertRegister(AuditOnlineRegister record) {
        return new SendMaterialsService().insertRegister(record);
    }

    @Override
    public int insertCompany(AuditOnlineCompany record) {
        return new SendMaterialsService().insertCompany(record);
    }

    /**
     * 工建查询法人所拥有公司
     *  @param creditcode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public AuditRsCompanyBaseinfo getAuditRsCompanyBaseinfo(String creditcode) {
        return new SendMaterialsService().getAuditRsCompanyBaseinfo(creditcode);
    }

    @Override
    public List<String> getAttachguidByClientguid(String cliengguid) {
        return new SendMaterialsService().getAttachguidByClientguid(cliengguid);
    }

    @Override
    public Record getTaskInfoByFlowsn(String flowsn) {
        return new SendMaterialsService().getTaskInfoByFlowsn(flowsn);
    }

    @Override
    public AuditOnlineCompanyGrant getIAuditOnlineCompanyGrantByCompanyID(String companyid) {
        return new SendMaterialsService().getIAuditOnlineCompanyGrantByCompanyID(companyid);
    }

    @Override
    public int insertRsComoany(AuditRsCompanyBaseinfo record) {
        return new SendMaterialsService().insertRsComoany(record);
    }

    @Override
    public int insertAuditRsCompanyRegister(AuditRsCompanyRegister record) {
        return new SendMaterialsService().insertAuditRsCompanyRegister(record);
    }

    @Override
    public int insertAuditRsCompanyLegal(AuditRsCompanyLegal record) {
        return new SendMaterialsService().insertAuditRsCompanyLegal(record);
    }

    @Override
    public int insertAuditOnlineCompanyGrant(AuditOnlineCompanyGrant record) {
        return new SendMaterialsService().insertAuditOnlineCompanyGrant(record);
    }
    
    @Override
    public AuditTask getAuditTaskByNewItemCode(String code) {
        return new SendMaterialsService().getAuditTaskByNewItemCode(code);
    }
    

}
