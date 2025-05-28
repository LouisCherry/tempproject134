package com.epoint.auditproject.auditproject.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import com.epoint.auditproject.zjxt.entity.AuditProjectProcessZjxt;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.EvainstanceCk;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Component
public class JNAuditProjectServiceImpl implements IJNAuditProject {

    /**
     *
     */
    private static final long serialVersionUID = 746918315479510329L;

    @Override
    public int getAuditExpressCount(String projectguid) {
        return new JNAuditProjectService().getAuditExpressCount(projectguid);
    }

    @Override
    public String getAttachConnect(String configName) {
        return new JNAuditProjectService().getAttachConnect(configName);
    }

    @Override
    public String getMaterialStatus(String cliengguid) {
        return new JNAuditProjectService().getMaterialStatus(cliengguid);
    }

    @Override
    public int updateMaterialStatus(String cliengguid) {
        return new JNAuditProjectService().updateMaterialStatus(cliengguid);
    }

    @Override
    public String getReaderType(String windowguid) {
        return new JNAuditProjectService().getReaderType(windowguid);
    }

    @Override
    public String getFormtableidByTaskid(String taskId) {
        return new JNAuditProjectService().getFormtableidByTaskid(taskId);
    }

    @Override
    public AuditTask getTaskinfoByTaskid(String taskId) {
        return new JNAuditProjectService().getTaskinfoByTaskid(taskId);
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(String fieldstr,
                                                                             Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField,
                                                                             String sortOrder, String userGuid) {
        JNAuditProjectService auditProjectService = new JNAuditProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData<AuditProject> projectList = auditProjectService.getRecordPageData(fieldstr, AuditProject.class,
                    conditionMap, firstResult, maxResults, sortField, sortOrder, "", userGuid);
            result.setResult(projectList);
        }
        catch (Exception var10) {
            result.setSystemFail(var10.getMessage());
        }

        return result;
    }

    @Override
    public List<Record> getEpointsformTableStruct(String tableid, String fielddisplaytype) {
        return new JNAuditProjectService().getEpointsformTableStruct(tableid, fielddisplaytype);
    }

    @Override
    public List<Record> getEpointsformTableStructbyradioandcheck(String tableid) {
        return new JNAuditProjectService().getEpointsformTableStructbyradioandcheck(tableid);
    }

    @Override
    public List<Record> getEpointsformTableStructbyDatepicker(String tableid) {
        return new JNAuditProjectService().getEpointsformTableStructbyDatepicker(tableid);
    }

    @Override
    public Record getTableId(String sql_tablename) {
        return new JNAuditProjectService().getTableId(sql_tablename);
    }

    @Override
    public int updateProjectIsrongque(String rowguid, String str) {
        return new JNAuditProjectService().updateProjectIsrongque(rowguid, str);
    }

    @Override
    public AuditProject getProjectByprojectguid(String projectGuid) {
        return new JNAuditProjectService().getProjectByprojectguid(projectGuid);
    }

    @Override
    public int updatePromisefileguid(String projectGuid, String viewData) {
        return new JNAuditProjectService().updatePromisefileguid(projectGuid, viewData);
    }

    @Override
    public int updateProjectIsreminded(String projectguid, String string) {
        return new JNAuditProjectService().updateProjectIsreminded(projectguid, string);
    }

    @Override
    public int getBanjieAndProminseDays(String projectguid2) {
        return new JNAuditProjectService().getBanjieAndProminseDays(projectguid2);
    }

    @Override
    public Record getApplyerinfo(String projectguid2) {
        return new JNAuditProjectService().getApplyerinfo(projectguid2);
    }

    @Override
    public String getScancodeByguid(String projectGuid) {
        return new JNAuditProjectService().getScancodeByguid(projectGuid);
    }

    @Override
    public String getJstScancodeByguid(String projectGuid) {
        return new JNAuditProjectService().getJstScancodeByguid(projectGuid);
    }

    @Override
    public int updateFrameCliengguid(String qRcodeAttachguid, String cliengguid) {
        return new JNAuditProjectService().updateFrameCliengguid(qRcodeAttachguid, cliengguid);
    }

    @Override
    public int updateMaterialStatus(String cliengguid, String projectGuid) {
        return new JNAuditProjectService().updateMaterialStatus(cliengguid, projectGuid);
    }

    @Override
    public String getScanLegalcodeByguid(String projectGuid) {
        return new JNAuditProjectService().getScanLegalcodeByguid(projectGuid);
    }

    @Override
    public List<AuditProjectProcessZjxt> findProcessList(String flowsn) {
        return new JNAuditProjectService().findProcessList(flowsn);
    }

    @Override
    public AuditProjectZjxt getAuditProjectZjxtByRowGuid(String rowguid) {
        return new JNAuditProjectService().getAuditProjectZjxtByRowGuid(rowguid);
    }

    @Override
    public List<Record> findDataList(AuditProject dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist
            , int first, int pagesize) {
        return new JNAuditProjectService().findDataList(dataBean, applydateStart, applydateEnd, finishdateStart
                , finishdateEnd, areacode, searchOulist, first, pagesize);
    }
    
    @Override
    public List<Record> findEvaluateDataList(EvainstanceCk dataBean, String applydateStart, String applydateEnd
    		, String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist
    		, int first, int pagesize) {
    	return new JNAuditProjectService().findEvaluateDataList(dataBean, applydateStart, applydateEnd, finishdateStart
    			, finishdateEnd, areacode, searchOulist, first, pagesize);
    }

    @Override
    public Integer getEvaluateDataCount(String areacode, List<String> searchOulist) {
    	return new JNAuditProjectService().getEvaluateDataCount(areacode, searchOulist);
    }
    
    @Override
    public Integer getAuditDataCount(String areacode, List<String> searchOulist) {
        return new JNAuditProjectService().getAuditDataCount(areacode, searchOulist);
    }

    @Override
    public int findTotalDataCount(AuditProject dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist) {
        return new JNAuditProjectService().findTotalDataCount(dataBean, applydateStart, applydateEnd, finishdateStart
                , finishdateEnd, areacode, searchOulist);
    }
    @Override
    public int findEvaluateTotalDataCount(EvainstanceCk dataBean, String applydateStart, String applydateEnd
    		, String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist) {
    	return new JNAuditProjectService().findEvaluateTotalDataCount(dataBean, applydateStart, applydateEnd, finishdateStart
    			, finishdateEnd, areacode, searchOulist);
    }

    @Override
    public List<AuditProject> getAuditProjectDataPageData(AuditProject dataBean, String applyDateStart
            , String applyDateEnd, String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids
            , int first, int pageSize, String sortField, String sortOrder) {
        return new JNAuditProjectService().getAuditProjectDataPageData(dataBean, applyDateStart, applyDateEnd
                , finishDateStart, finishDateEnd, areacode, ouguids, first, pageSize,sortField,sortOrder);
    }

    @Override
    public int getAuditProjectDataCount(AuditProject dataBean, String applyDateStart, String applyDateEnd
            , String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids) {
        return new JNAuditProjectService().getAuditProjectDataCount(dataBean, applyDateStart, applyDateEnd
                , finishDateStart, finishDateEnd, areacode, ouguids);
    }

    @Override
    public List<String> findOUGuidList(String areacode, String leftTreeNodeGuid) {
        return new JNAuditProjectService().findOUGuidList(areacode,leftTreeNodeGuid);
    }
    
    @Override
    public void updateCrtInfo(String certrowguid) {
        new JNAuditProjectService().updateCrtInfo(certrowguid);
    }
    
    @Override
    public List<Record> getSpglDantiInfoBySubappguid(String projectguid) {
        return new JNAuditProjectService().getSpglDantiInfoBySubappguid(projectguid);
    }
    
    public AuditProject getAuditProjectByCertrowguid(String certrowguid) {
    	return new JNAuditProjectService().getAuditProjectByCertrowguid(certrowguid);
    }


    @Override
    public Integer getLcprojectTwoListCount(Map<String,String> map) {
        return new JNAuditProjectService().getLcprojectTwoListCount(map);
    }
    @Override
    public Integer getLcprojectTenListCount(Map<String,String> map) {
        return new JNAuditProjectService().getLcprojectTwoListCount(map);
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageData(Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        JNAuditProjectService auditProjectService = new JNAuditProjectService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            new PageData();
            PageData<AuditProject> projectList = getDbListByPage(AuditProject.class, conditionMap, firstResult, maxResults, sortField, sortOrder);
            result.setResult(projectList);
        }
        catch (Exception var10) {
            result.setSystemFail(var10.getMessage());
        }

        return result;

    }

    public  PageData<AuditProject> getDbListByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        PageData<AuditProject> pageData = new PageData();
        Entity en = (Entity)baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        StringBuffer sbcount = new StringBuffer();
        String fields = "*";
        String sortCondition = "";
        String tableName = en.table();
        SQLManageUtil sqlManageUtil=new SQLManageUtil();
        String buildSql = sqlManageUtil.buildSql(conditionMap);
        sb.append(buildSql);
        sbcount.append(buildSql);
        CommonDao commonDao = CommonDao.getInstance();

        if (conditionMap != null) {
            if (conditionMap.containsKey("#fields")) {
                fields = (String)conditionMap.get("#fields");
                if (StringUtil.isBlank(fields)) {
                    fields = "*";
                }
            }

            if (StringUtil.isBlank(conditionMap.get("#sort")) && StringUtil.isNotBlank(sortField)) {
                sortCondition = " order by " + sortField + " " + sortOrder;
            }

            if (StringUtil.isNotBlank(conditionMap.get("#join"))) {
                tableName = getTable(tableName + " a", (String)conditionMap.get("#join"));
            }
        } else if (StringUtil.isNotBlank(sortField)) {
            sortCondition = " order by " + sortField + " " + sortOrder;
        }

        String sqlRecord = "select " + fields + " from " + tableName + sb.toString() + sortCondition;
        String sqlCount = "select count(*) from " + tableName + sbcount.toString();
        List<String> params = new ArrayList();
        Object[] paramsobject = params.toArray();
        List<AuditProject> dataList = commonDao.findList(sqlRecord, first, pageSize, AuditProject.class, paramsobject);
        int dataCount = commonDao.queryInt(sqlCount, paramsobject);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    private String getTable(String leftTablestr, String join) {
        String[] joinTables = join.split(";");
        String joinType = "";
        if (joinTables[0].startsWith("#left#")) {
            joinType = " left join ";
        } else if (joinTables[0].startsWith("#right#")) {
            joinType = " right join ";
        } else if (joinTables[0].startsWith("#inner#")) {
            joinType = " inner join ";
        }

        String[] field = joinTables[0].split("#");
        String str = leftTablestr + joinType + field[2] + " on " + field[3] + "=" + field[4];
        return joinTables.length > 1 ? getTable(str, joinTables[1]) : str;
    }

}
