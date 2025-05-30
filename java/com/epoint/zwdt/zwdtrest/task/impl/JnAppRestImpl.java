package com.epoint.zwdt.zwdtrest.task.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Service
public class JnAppRestImpl implements IJnAppRestService {

    public AuditCommonResult<List<Record>> selectOuByApplyertype(String applyertype, String is_fugongfuchan, String areacode, String taskname, String isyc, String xzql, List<String> list) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditTaskBasicService.selectOuByApplyertype(applyertype, is_fugongfuchan, areacode, taskname, isyc, xzql, list));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    public AuditCommonResult<List<Record>> selectOuByApplyertypeYC(String applyertype, String Dao_xc_num, String ISPYC, String Operationscope, String If_express, String mpmb, String CHARGE_FLAG, String mashangban, String wangshangban, String sixshenpilb, String bianminfuwu, String yishenqing, String qctb, String ggfw, String xzxk, String areacode, String kstbsx, String qstb) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditTaskBasicService.selectOuByApplyertypeYC(applyertype, Dao_xc_num, ISPYC, Operationscope, If_express, mpmb, CHARGE_FLAG, mashangban, wangshangban, sixshenpilb, bianminfuwu, yishenqing, qctb, ggfw, xzxk, areacode, kstbsx, qstb));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }


    @Override
    public AuditCommonResult<String> getTaskguidByItemid(String itemid) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(auditTaskBasicService.getTaskguidByItemid(itemid));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditTask> getTaskByInnercode(String innercode) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<AuditTask> result = new AuditCommonResult<AuditTask>();
        try {
            result.setResult(auditTaskBasicService.getTaskByInnercode(innercode));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getTaskguidByInnercode(String inner_code) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(auditTaskBasicService.getTaskguidByInnercode(inner_code));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public FrameOu getAreanameByAreacode(String areacode) {
        return new JnAppRestService().getAreanameByAreacode(areacode);
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getXZGBAuditTaskPageData(Map<String, String> conditionMap, String XZAreaCode,
                                                                           int firstResult, int maxResults, String sortField, String sortOrder) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<PageData<AuditTask>>();
        try {
            result.setResult(auditTaskBasicService.getXZGBAuditTaskPageData(conditionMap, XZAreaCode, firstResult, maxResults, sortField, sortOrder));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getGBAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                                         String sortField, String sortOrder) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<PageData<AuditTask>>();
        try {
            result.setResult(auditTaskBasicService.getGBAuditTaskPageData(conditionMap, firstResult, maxResults, sortField, sortOrder));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getProjectGuidByFlowsnPassword(String sortField, String sortOrder) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(auditTaskBasicService.getProjectGuidByFlowsnPassword(sortField, sortOrder));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    public AuditCommonResult<AuditTask> getAuditTaskByTaskid(String taskguid) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<AuditTask> result = new AuditCommonResult<AuditTask>();
        try {
            result.setResult(auditTaskBasicService.getAuditTaskByTaskid(taskguid));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public List<CodeItems> getAreaList() {
        return new JnAppRestService().getAreaList();
    }

    @Override
    public List<Record> selectOuByApplyertypeNew(String applyerType, String areaCode) {
        return new JnAppRestService().selectOuByApplyertypeNew(applyerType, areaCode);
    }

    @Override
    public List<FrameOu> getOuListByAreacode(String areaCode) {
        return new JnAppRestService().getOuListByAreacode(areaCode);
    }

    @Override
    public PageData<AuditTask> getLnrAuditTaskPageData(int firstResultTask, int parseInt) {
        return new JnAppRestService().getLnrAuditTaskPageData(firstResultTask, parseInt);
    }

    @Override
    public AuditCommonResult<PageData<AuditTask>> getNewAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                                          String sortField, String sortOrder) {
        JnAppRestService auditTaskBasicService = new JnAppRestService();
        AuditCommonResult<PageData<AuditTask>> result = new AuditCommonResult<PageData<AuditTask>>();
        try {
            result.setResult(auditTaskBasicService.getNewAuditTaskPageData(conditionMap, firstResult, maxResults, sortField, sortOrder));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

}
