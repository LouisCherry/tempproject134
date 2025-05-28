package com.epoint.xmz.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.api.IZwdtCommonService;
/**
 * 网厅service实现类
 * 
 * @author LYA
 * @version [版本号, 2022-02-16 15:36:48]
 */
@Component
@Service
public class ZwdtCommonServiceImpl implements IZwdtCommonService
{

    @Override
    public List<AuditTask> findTaskList(String taskname, String areacode, int type, int first,int pageSize) {
        
        return new ZwdtCommonService().findTaskList(taskname,areacode,type,first,pageSize);
    }

    @Override
    public List<Record> findOuList(String areacode) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().findOuList(areacode);
    }

    @Override
    public List<AuditTask> findOuTaskList(String taskname, String ouguid, int first, int pageSize) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().findOuTaskList(taskname,ouguid,first,pageSize);
    }

    @Override
    public Record findQueueTasktypeByTaskid(String task_id) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().findQueueTasktypeByTaskid(task_id);
    }

    @Override
    public int findTaskListCount(String taskname, String areacode, int type) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().findTaskListCount(taskname,areacode,type);
    }

    @Override
    public int findOuTaskListCount(String taskname, String ouguid) {
        return new ZwdtCommonService().findOuTaskListCount(taskname,ouguid);
    }

    @Override
    public List<Record> getQueueList() {
        return new ZwdtCommonService().getQueueList();
    }

    @Override
    public List<Record> getXzzsProjectList(String mondayDate, String sundayDate,String type) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getXzzsProjectList(mondayDate,sundayDate,type);
    }

    @Override
    public List<AuditRsItemBaseinfo> getItemInfoByCreditcode(String creditcode) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getItemInfoByCreditcode(creditcode);
    }

    @Override
    public int getProjectNumByYewuguidAndBasetaskGuid(String yewuguid, String basetaskguid) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getProjectNumByYewuguidAndBasetaskGuid(yewuguid,basetaskguid);
    }

    @Override
    public List<AuditTask> getAuditTaskByTaskids(String taskids) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getAuditTaskByTaskids(taskids);
    }

    @Override
    public List<AuditTask> getAuditTaskByDictid(String dictid, int first, int pageSize,String keyword) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getAuditTaskByDictid(dictid,first,pageSize,keyword);
    }

    @Override
    public int getAuditTaskCountByDictid(String dictid,String keyword) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getAuditTaskCountByDictid(dictid,keyword);
    }

    @Override
    public CertInfo getLeastCertInfoByCreditCodeAndCertCatalogid(String creditCode, String certCatalogid) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getLeastCertInfoByCreditCodeAndCertCatalogid(creditCode,certCatalogid);
    }

    @Override
    public FrameAttachInfo getLeastFrameAttachInfoByClinegguid(String wordCliengguid) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getLeastFrameAttachInfoByClinegguid(wordCliengguid);
    }

    @Override
    public void updateShouLiguidByProjectguid(String attachguid, String projectguid) {
        // TODO Auto-generated method stub
        new ZwdtCommonService().updateShouLiguidByProjectguid(attachguid,projectguid);
    }
    @Override
    public AuditCommonResult<List<Record>> getProjectnumGroupByFiled(String groupfield, Integer recordnum,
            Map<String, String> conditionmap) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(new ZwdtCommonService().getProjectnumGroupByTaskid(groupfield, recordnum,
                conditionmap));
        return result;
    }

    @Override
    public List<Record> selectKaoQinInfo(String kaoqinYear, String month) {
        
        return new ZwdtCommonService().selectKaoQinInfo(kaoqinYear,month);
    }

    @Override
    public void updateKfyjStatus(String str, String kaoqinYear, String kaoqinMonth) {
        new ZwdtCommonService().updateKfyjStatus(str,kaoqinYear,kaoqinMonth);
    }

    @Override
    public List<Record> getJbjTaskInfo(String areacode, int first, int pageSize) {
        // TODO Auto-generated method stub
        return  new ZwdtCommonService().getJbjTaskInfo(areacode,first,pageSize);
    }

    @Override
    public AuditTask getTaskInfo(String task_id) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getTaskInfo(task_id);
    }

    @Override
    public int getJbjTaskInfoNum(String areacode) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getJbjTaskInfoNum(areacode);
    }

    @Override
    public List<AuditPerformanceDetail> findDeailList(String recordrowguid, String recordrulerowguid, String str) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().findDeailList(recordrowguid,recordrulerowguid,str);
    }
    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Record getAuditTaskTaianByTaskId(String taskId) {
        return new ZwdtCommonService().getAuditTaskTaianByTaskId(taskId);
    }

    @Override
    public CertInfo getCertinfoByCertCatalogidAndcplb(String certcatalogid, String cplb) {
        // TODO Auto-generated method stub
        return new ZwdtCommonService().getCertinfoByCertCatalogidAndcplb(certcatalogid,cplb);
    }
}
