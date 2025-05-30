package com.epoint.zwdt.zwdtrest.project.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlinecompany.domain.AuditOnlineCompany;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.project.api.entity.JnTcId;
import org.springframework.stereotype.Component;

import java.util.List;

@Service
@Component
public class JnProjectServiceImpl implements IJnProjectService {

    /**
     *
     */
    private static final long serialVersionUID = -4737301911580678408L;

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTcId record) {
        return new JnProjectService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnProjectService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTcId record) {
        return new JnProjectService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public JnTcId find(Object primaryKey) {
        return new JnProjectService().find(primaryKey);
    }

    public int updateChargeDetail(String rowguid) {
        return new JnProjectService().updateChargeDetail(rowguid);
    }

    public JnTcId getDzbdForBusiness(String yewguid) {
        return new JnProjectService().getDzbdForBusiness(yewguid);
    }

    public void insertCodeItemForTxxl(String addvaluetxxl) {
        new JnProjectService().insertCodeItemForTxxl(addvaluetxxl);
    }

    public FrameAttachInfo getFrameAttachByIdenumber(String idnumber) {
        return new JnProjectService().getFrameAttachByIdenumber(idnumber);
    }

    public List<FrameAttachInfo> getFrameAttachByIdenumberBigType(String idnumber, String bigshowtype) {
        return new JnProjectService().getFrameAttachByIdenumberBigType(idnumber, bigshowtype);
    }

    public List<FrameAttachInfo> getFrameAttachByIdenumberTag(String idnumber, String cliengtag) {
        return new JnProjectService().getFrameAttachByIdenumberTag(idnumber, cliengtag);
    }

    public List<Record> getNoEvaluteProject(String time) {
        return new JnProjectService().getNoEvaluteProject(time);
    }

    public AuditSpBusiness getAuditBusinessByName(String name, String areacode) {
        return new JnProjectService().getAuditBusinessByName(name, areacode);
    }

    public Record getGgcswsxkxsqByNumber(String zmwh) {
        return new JnProjectService().getGgcswsxkxsqByNumber(zmwh);
    }

    public int UpdateRecord(Record record) {
        return new JnProjectService().updateRecord(record);
    }

    public int CancelRecord(String tableName, String licenNumber) {
        return new JnProjectService().CancelRecord(tableName, licenNumber);
    }

    public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
        return new JnProjectService().getDzbdDetailByZzbh(tablename, zzbh);
    }


    public List<AuditOrgaArea> selectAuditAreaList() {
        return new JnProjectService().selectAuditAreaList();
    }

    public List<AuditProject> selectAuditProjectByBiguid(String biguid) {
        return new JnProjectService().selectAuditProjectByBiguid(biguid);
    }

    public List<Record> getBiguidsByParentId(String parentguid) {
        return new JnProjectService().getBiguidsByParentId(parentguid);
    }

    public List<Record> getBiguidsByYewuguid(String yewuguid) {
        return new JnProjectService().getBiguidsByYewuguid(yewuguid);
    }

    @Override
    public PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize) {
        return new JnProjectService().getAuditSpBusinessByPage(record,pageIndex,pageSize);
    }

    @Override
    public String getSdqYjsBusinessGuid(String areacode, String businessname) {
        return new JnProjectService().getSdqYjsBusinessGuid(areacode,businessname);
    }

    @Override
    public AuditOnlineCompany getOnlineCompany(String itemlegalcertnum) {
        return new JnProjectService().getOnlineCompany(itemlegalcertnum);
    }

    @Override
    public AuditOnlineIndividual getOnlineIndividualByCertnum(String itemlegalcertnum) {
        return new JnProjectService().getOnlineIndividualByCertnum(itemlegalcertnum);
    }


}
