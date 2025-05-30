package com.epoint.zwdt.zwdtrest.project.api;

import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.project.api.entity.JnTcId;

import java.util.List;

public interface IJnProjectService {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record);

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey);

    public int updateChargeDetail(String rowguid);

    public JnTcId getDzbdForBusiness(String yewguid);

    public Record getGgcswsxkxsqByNumber(String zmwh);

    public void insertCodeItemForTxxl(String addvaluetxxl);

    public FrameAttachInfo getFrameAttachByIdenumber(String idnumber);

    public List<FrameAttachInfo> getFrameAttachByIdenumberBigType(String idnumber, String bigshowtype);

    public List<FrameAttachInfo> getFrameAttachByIdenumberTag(String idnumber, String cliengtag);

    public List<Record> getNoEvaluteProject(String time);

    public AuditSpBusiness getAuditBusinessByName(String busienssname, String areacode);

    public int UpdateRecord(Record record);

    public int CancelRecord(String tableName, String licenNumber);

    public Record getDzbdDetailByZzbh(String tableName, String zzbh);

    public List<AuditOrgaArea> selectAuditAreaList();

    public List<AuditProject> selectAuditProjectByBiguid(String biguid);

    public List<Record> getBiguidsByParentId(String parentguid);

    public List<Record> getBiguidsByYewuguid(String yewuguid);

    PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize);

    String getSdqYjsBusinessGuid(String areacode, String businessname);

    AuditOnlineCompany getOnlineCompany(String itemlegalcertnum);

    AuditOnlineIndividual getOnlineIndividualByCertnum(String itemlegalcertnum);
}
