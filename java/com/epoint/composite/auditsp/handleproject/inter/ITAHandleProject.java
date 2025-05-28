package com.epoint.composite.auditsp.handleproject.inter;

import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import java.util.List;
import java.util.Map;

public interface ITAHandleProject {
    AuditCommonResult<String> InitProject(String var1, String var2, String var3, String var4, String var5, String var6,
            String var7, String var8, String var9, String var10, String var11);

    AuditCommonResult<String> InitProject(String var1, String var2, String var3, String var4, String var5, String var6,
            String var7, String var8, String var9);

    AuditCommonResult<AuditProject> InitOnlineProject(String var1, String var2, String var3, String var4, String var5,
            String var6);

    AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String var1, String var2,
            String var3, String var4, String var5, String var6, String var7, String var8);

    AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String var1, String var2,
            String var3, String var4, String var5, String var6, String var7, String var8, String var9);

    AuditCommonResult<List<AuditProjectMaterial>> InitOnlineCompanyProjectReturnMaterials(String var1, String var2,
            String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10,
            String var11);

    AuditCommonResult<Void> addCompanyOnlineProject(AuditProject var1, String var2, String var3, String var4,
            String var5, String var6, String var7);

    AuditCommonResult<String> InitBLSPProject(String var1, String var2, String var3, String var4, String var5,
            String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13,
            String var14);

    AuditCommonResult<String> InitIntegratedProject(String var1, String var2, String var3, String var4, String var5,
            String var6, String var7, String var8, String var9, String var10, String var11);

    AuditCommonResult<String> saveProject(AuditProject var1);

    AuditCommonResult<String> handleReceive(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleAccept(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6);

    AuditCommonResult<String> handleAccept(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6, String var7);

    AuditCommonResult<String> handleYstg(AuditProject var1, String var2, String var3);

    AuditCommonResult<String> handleYstgAddreason(AuditProject var1, String var2, String var3, String var4);

    AuditCommonResult<String> handleYsdh(AuditProject var1, String var2, String var3);

    AuditCommonResult<String> handleYsdhAddreason(AuditProject var1, String var2, String var3, String var4);

    AuditCommonResult<String> handlePatch(AuditProject var1, String var2, String var3);

    AuditCommonResult<String> handleReceipt(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6, String var7, Integer var8);

    AuditCommonResult<String> handleReceipt(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleReject(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleApprove(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleNotApprove(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleFinish(AuditProject var1, String var2, String var3, String var4);

    AuditCommonResult<String> handlePricing(AuditProject var1, String var2, String var3, List<Record> var4, Double var5,
            Double var6, String var7);

    AuditCommonResult<String> handlePriceCancel(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handlePriceCancel(AuditProject var1, String var2, String var3, String var4, String var5,
            Integer var6);

    AuditCommonResult<String> handlePause(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handlePause(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6);

    AuditCommonResult<String> handleRecover(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleDelay(AuditProject var1, String var2, String var3, int var4, String var5,
            String var6, String var7);

    AuditCommonResult<String> handleDelayPass(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6, String var7);

    AuditCommonResult<String> handleDelayNotPass(AuditProject var1, String var2, String var3, String var4, String var5,
            String var6, String var7);

    AuditCommonResult<String> handleRevoke(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleSuspension(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<String> handleEvaluate(AuditProject var1, String var2, String var3);

    AuditCommonResult<String> handleResult(AuditProject var1, String var2, String var3);

    @Deprecated
    AuditCommonResult<List<SelectItem>> initDocList(String var1, String var2);

    AuditCommonResult<List<Map<String, String>>> initProjectDocList(String var1, String var2, String var3, String var4,
            String var5, boolean var6);

    AuditCommonResult<String> handleMaterialNotPass(AuditProject var1, String var2, String var3, String var4);

    AuditCommonResult<Void> handleProjectPass(AuditProject var1, String var2, String var3, String var4);

    AuditCommonResult<Void> handleProjectNotPass(AuditProject var1, String var2, String var3);

    AuditCommonResult<Void> InitOnlineProjectForBusiness(String var1, String var2, String var3, String var4,
            String var5, String var6, String var7);

    AuditCommonResult<Void> saveOperateLog(AuditProject var1, String var2, String var3, String var4, String var5);

    AuditCommonResult<Void> handleLogistics(AuditProject var1, AuditLogisticsBasicinfo var2);

    AuditCommonResult<Void> updateLogistics(AuditProject var1, AuditLogisticsBasicinfo var2);

    AuditCommonResult<String> InitProject(String var1, String var2, String var3, String var4, String var5, String var6,
            String var7, String var8, String var9, String var10, String var11, String var12);
}