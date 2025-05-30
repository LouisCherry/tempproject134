package com.epoint.zwdt.zwdtrest.task.api;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.util.List;
import java.util.Map;

public interface IJnAppRestService {
    public AuditCommonResult<List<Record>> selectOuByApplyertype(String applyertype, String is_fugongfuchan, String areacode, String taskname, String isyc, String xzql, List<String> list);

    public AuditCommonResult<List<Record>> selectOuByApplyertypeYC(String applyertype, String Dao_xc_num, String ISPYC, String Operationscope, String If_express, String mpmb, String CHARGE_FLAG, String mashangban, String wangshangban, String sixshenpilb, String bianminfuwu, String yishenqing, String qctb, String ggfw, String xzxk, String areacode, String kstbsx, String qstb);

    public AuditCommonResult<String> getTaskguidByItemid(String itemid);

    public AuditCommonResult<String> getTaskguidByInnercode(String inner_code);

    public AuditCommonResult<AuditTask> getTaskByInnercode(String innercode);

    public FrameOu getAreanameByAreacode(String areacode);

    public AuditCommonResult<PageData<AuditTask>> getXZGBAuditTaskPageData(Map<String, String> conditionMap, String XZAreaCode,
                                                                           int firstResult, int maxResults, String sortField, String sortOrder);

    public AuditCommonResult<PageData<AuditTask>> getGBAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                                         String sortField, String sortOrder);

    AuditCommonResult<String> getProjectGuidByFlowsnPassword(String flowsn, String password);

    public abstract AuditCommonResult<AuditTask> getAuditTaskByTaskid(String paramString);

    public List<CodeItems> getAreaList();

    List<Record> selectOuByApplyertypeNew(String applyerType, String areaCode);

    List<FrameOu> getOuListByAreacode(String areaCode);

    PageData<AuditTask> getLnrAuditTaskPageData(int firstResultTask, int parseInt);

    AuditCommonResult<PageData<AuditTask>> getNewAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
                                                                   String sortField, String sortOrder);
}
