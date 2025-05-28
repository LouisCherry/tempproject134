package com.epoint.xmz.impl;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 网厅通用service
 * 
 * @author LYA
 * @version [版本号, 2022-02-16 15:36:48]
 */
public class ZwdtCommonService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZwdtCommonService() {
        baseDao = CommonDao.getInstance();
    }

    public List<AuditTask> findTaskList(String taskname, String areacode, int type,int first,int pageSize) {
        String sql = "select atk.rowguid,atk.taskname,atk.ouname,atk.ouguid,atk.areacode,atk.task_id from audit_task atk join audit_task_extension ate"
                + " on atk.rowguid = ate.taskguid where 1=1" +
                     " and atk.is_history = 0 and atk.IS_EDITAFTERIMPORT =1 and atk.IS_ENABLE =1 and atk.areacode = ? and atk.shenpilb not in ('02','03','04','09') and atk.ouguid not in ('80297365-a538-44e1-8110-dbec337fcc96','c305cf09-9b4e-4335-8f34-7423f5888acd','a013628d-b92e-4a41-8169-e19fbc15582b','9bc9ef8f-09de-43e2-83e6-469ef6f75609','b24bb942-4ab2-4753-ad14-05d7449772e6')";
        if(0==type) {//个人
            sql += " and atk.applyertype in ('20','30')";
        }
        else {//法人
            sql += " and atk.applyertype in ('10','30')";
        }
        sql += " and ate.webapplytype in ('0','1','2')";
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and atk.taskname like '%"+taskname+"%' ";
        }
        sql += " and atk.new_item_code is not null ";
        sql += "order by atk.ouname limit "+first*pageSize+","+pageSize;
        return baseDao.findList(sql, AuditTask.class, areacode);
    }

    public List<Record> findOuList(String areacode) {
        String sql = "select fo.ouguid,fo.ouname from audit_task atk\r\n" + 
                "join frame_ou fo \r\n" + 
                "on atk.ouguid = fo.OUGUID \r\n" + 
                "where atk.is_history = 0 and atk.IS_EDITAFTERIMPORT =1 and atk.IS_ENABLE =1 \r\n" + 
                "and atk.areacode = ? \r\n" + 
                " and atk.ouguid not in ('80297365-a538-44e1-8110-dbec337fcc96','c305cf09-9b4e-4335-8f34-7423f5888acd','a013628d-b92e-4a41-8169-e19fbc15582b','9bc9ef8f-09de-43e2-83e6-469ef6f75609','b24bb942-4ab2-4753-ad14-05d7449772e6') \r\n"+
                "group by fo.ouguid,fo.ouname\r\n" + 
                "order by fo.ORDERNUMBER asc";
        return baseDao.findList(sql, Record.class, areacode);
    }

    public List<AuditTask> findOuTaskList(String taskname, String ouguid, int first, int pageSize) {
        String sql = "select atk.rowguid,atk.taskname,atk.ouname,atk.ouguid,atk.areacode,atk.task_id from audit_task atk \r\n" +
                " join audit_task_extension ate on atk.rowguid = ate.taskguid "+
                "where atk.is_history = 0 and atk.IS_EDITAFTERIMPORT =1 and atk.IS_ENABLE =1 and ate.webapplytype in ('0','1','2') and atk.shenpilb not in ('02','03','04','09') \r\n" + 
                "and ouguid = ? \r\n";
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like '%"+taskname+"%'";
        }
        sql += " and atk.new_item_code is not null ";
        sql += " order by ouname limit "+first*pageSize+","+pageSize;
                
        return baseDao.findList(sql, AuditTask.class, ouguid);
    }

    public Record findQueueTasktypeByTaskid(String task_id) {
        String sql = "select aqt.rowguid,aqt.TaskTypeName from audit_queue_tasktype aqt\r\n" + 
                "join audit_queue_tasktype_task aqtt\r\n" + 
                "on aqt.rowguid = aqtt.tasktypeguid \r\n" + 
                "where task_id = ?";
        return baseDao.find(sql, Record.class, task_id);
    }

    public int findTaskListCount(String taskname, String areacode, int type) {
        String sql = "select count(atk.rowguid) from audit_task atk join audit_task_extension ate"
                + " on atk.rowguid = ate.taskguid where 1=1" +
                     " and atk.is_history = 0 and atk.IS_EDITAFTERIMPORT =1 and atk.IS_ENABLE =1 and atk.areacode = ? and atk.shenpilb not in ('02','03','04','09') and atk.ouguid not in ('80297365-a538-44e1-8110-dbec337fcc96','c305cf09-9b4e-4335-8f34-7423f5888acd','a013628d-b92e-4a41-8169-e19fbc15582b','9bc9ef8f-09de-43e2-83e6-469ef6f75609','b24bb942-4ab2-4753-ad14-05d7449772e6')";
        if(0==type) {//个人
            sql += " and atk.applyertype in ('20','30')";
        }
        else {//法人
            sql += " and atk.applyertype in ('10','30')";
        }
        sql += " and ate.webapplytype in ('0','1','2')";
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and atk.taskname like '%"+taskname+"%' ";
        }
        sql += " and atk.new_item_code is not null";
        return baseDao.queryInt(sql, areacode);
    }

    public int findOuTaskListCount(String taskname, String ouguid) {
        // TODO Auto-generated method stub
        String sql = "select count(atk.rowguid) from audit_task atk \r\n" +
                " join audit_task_extension ate on atk.rowguid = ate.taskguid "+
                "where atk.is_history = 0 and atk.IS_EDITAFTERIMPORT =1 and atk.IS_ENABLE =1 and ate.webapplytype in ('0','1','2') and atk.shenpilb not in ('02','03','04','09') \r\n" + 
                "and ouguid = ? \r\n";
        if(StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like '%"+taskname+"%'";
        }
        sql += " and atk.new_item_code is not null";
        return baseDao.queryInt(sql, ouguid);
    }

    public List<Record> getQueueList() {
        String sql = "select\r\n" + 
                "    aqu.displayname ,\r\n" + 
                "    aqu.identitycardnum,\r\n" + 
                "    aqe.getnotime\r\n" + 
                "from\r\n" + 
                "    audit_queue aqe\r\n" + 
                "join audit_queue_userinfo aqu \r\n" + 
                "on\r\n" + 
                "    aqe.IDENTITYCARDNUM = aqu.IDENTITYCARDNUM\r\n" + 
                "where\r\n" + 
                "    aqe.centerguid = '8adfc4f0-ce41-453e-94a7-14356a44db79'\r\n";
        return baseDao.findList(sql, Record.class);
    }

    public List<Record> getXzzsProjectList(String mondayDate, String sundayDate, String type) {
        String sql = "select\r\n" + 
                "    ap.applyername as companyname,\r\n" + 
                "    ap.certnum as creditcode,\r\n" + 
                "    atk.taskname,\r\n" + 
                "    atk.item_id,\r\n" + 
                "    atk.by_law\r\n" + 
                "from\r\n" + 
                "    audit_project ap\r\n" + 
                "    join audit_task atk \r\n" + 
                "    on ap.taskguid = atk.RowGuid \r\n" + 
                "where\r\n" + 
                "    ap.applyertype = '10' \r\n" + 
                "    and ap.status >='24'\r\n" + 
                "    and atk.shenpilb = ? \r\n" + 
                "    and atk.new_item_code is not null\r\n" + 
                "    and ap.applydate >= ? and ap.applydate <= ? \r\n" +
                " group by ap.applyername,\r\n" + 
                "    ap.certnum,\r\n" + 
                "    atk.taskname,\r\n" + 
                "    atk.item_id,\r\n" + 
                "    atk.by_law";
        return baseDao.findList(sql, Record.class,type,mondayDate,sundayDate);
    }

    public List<AuditRsItemBaseinfo> getItemInfoByCreditcode(String creditcode) {
        String sql = "select\r\n" + 
                "    asib.rowguid,\r\n" + 
                "    asib.itemname,\r\n" + 
                "    asib.ITEMLEGALCREDITCODE,\r\n" + 
                "    asib.CONSTRUCTIONSCALEANDDESC,\r\n" + 
                "    asib.CONSTRUCTIONSITEDESC,\r\n" + 
                "    asib.TOTALINVEST,\r\n" + 
                "    asib.CONTRACTPERSON,\r\n" + 
                "    asib.CONTRACTPHONE\r\n" + 
                "from\r\n" + 
                "    audit_rs_item_baseinfo asib where asib.itemcode = ?";
        return baseDao.findList(sql,AuditRsItemBaseinfo.class,creditcode);
    }

    public int getProjectNumByYewuguidAndBasetaskGuid(String yewuguid, String basetaskguid) {
        String sql = "select count(ap.rowguid) from audit_sp_instance asi \r\n" + 
                "join audit_project ap \r\n" + 
                "on asi.rowguid= ap.BIGUID\r\n" + 
                "join audit_sp_basetask_r asbr \r\n" + 
                "on ap.task_id = asbr.taskid \r\n" + 
                "join audit_sp_basetask asb \r\n" + 
                "on asbr.basetaskguid = asb.RowGuid \r\n" + 
                "where asb.rowguid = ? \r\n" + 
                "and asi.yewuguid = ? \r\n" + 
                "and ap.status = 90";
        return baseDao.queryInt(sql, basetaskguid,yewuguid);
    }

    public List<AuditTask> getAuditTaskByTaskids(String taskids) {
        String sql = "select taskname,id,item_id from audit_task where task_id in ("+taskids+") and IS_HISTORY = 0 and IS_ENABLE = 1 and IS_EDITAFTERIMPORT = 1 and New_ITEM_CODE is not null";
        return baseDao.findList(sql, AuditTask.class);
    }

    public List<AuditTask> getAuditTaskByDictid(String dictid, int first, int pageSize,String keyword) {
        String sql = "select\r\n" + 
                "    atk.*\r\n" + 
                "from\r\n" + 
                "    audit_task atk\r\n" + 
                "join audit_task_map atp\r\n" + 
                "on\r\n" + 
                "    atk.task_id = atp.TASK_ID\r\n" + 
                "where\r\n" + 
                "    atp.dict_id = ? \r\n" + 
                "    and atk.IS_HISTORY = 0\r\n" + 
                "    and atk.IS_ENABLE = 1\r\n" + 
                "    and atk.IS_EDITAFTERIMPORT = 1 ";
            if(StringUtil.isNotBlank(keyword)) {
                sql += " and atk.taskname like '%"+keyword+"%' ";
            }
                sql += " order by OperateDate limit ?,?";
                
        return baseDao.findList(sql, AuditTask.class,dictid,first*pageSize,pageSize);
    }

    public int getAuditTaskCountByDictid(String dictid,String keyword) {
        String sql = "select\r\n" + 
                "    count(atk.rowguid)\r\n" + 
                "from\r\n" + 
                "    audit_task atk\r\n" + 
                "join audit_task_map atp\r\n" + 
                "on\r\n" + 
                "    atk.task_id = atp.TASK_ID\r\n" + 
                "where\r\n" + 
                "    atp.dict_id = ? \r\n" + 
                "    and atk.IS_HISTORY = 0\r\n" + 
                "    and atk.IS_ENABLE = 1\r\n" + 
                "    and atk.IS_EDITAFTERIMPORT = 1";
        if(StringUtil.isNotBlank(keyword)) {
            sql += " and atk.taskname like '%"+keyword+"%' ";
        }
        return baseDao.queryInt(sql, dictid);
    }

    public CertInfo getLeastCertInfoByCreditCodeAndCertCatalogid(String creditCode, String certCatalogid) {
        String sql = "select\r\n" + 
                "    cio.rowguid,\r\n" + 
                "    cio.certname,\r\n" + 
                "    cio.AWARDDATE ,\r\n" + 
                "    cio.CERTCLIENGGUID,\r\n" + 
                "    cio.EXPIREDATEFROM ,\r\n" + 
                "    cio.EXPIREDATETO,\r\n" + 
                "    cio.isdygprint, \r\n" + 
                "    cio.wordcliengguid \r\n" + 
                "from\r\n" + 
                "    audit_project ap\r\n" + 
                "join cert_info cio \r\n" + 
                "on\r\n" + 
                "    ap.certrowguid = cio.rowguid\r\n" + 
                "where\r\n" + 
                "    ap.certnum = ? \r\n" + 
                "    and ap.BANJIEDATE >= '2022-06-27 00:00:00'" +
                "    and cio.certcatalogid = ? \r\n" + 
                "    and cio.ISHISTORY = 0\r\n" + 
                "order by\r\n" + 
                "    cio.OperateDate desc\r\n" + 
                "limit 1";
        return baseDao.find(sql, CertInfo.class,creditCode,certCatalogid);
    }

    public FrameAttachInfo getLeastFrameAttachInfoByClinegguid(String wordCliengguid) {
        String sql = "select * from frame_attachinfo where cliengguid = ? order by uploaddatetime desc limit 1";
        return baseDao.find(sql,FrameAttachInfo.class,wordCliengguid);
    }

    public void updateShouLiguidByProjectguid(String attachguid, String projectguid) {
        String sql = "update audit_project set shouliattachguid= ? where rowguid = ?";
        baseDao.execute(sql, attachguid,projectguid);
        
    }
    
    public List<Record> getProjectnumGroupByTaskid(String groupfield, Integer recordnum,
            Map<String, String> conditionmap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        String sqle = sqlm.buildSql(conditionmap);
        String sql = "select count(1) as num," + groupfield + " from audit_project " + sqle + " group by " + groupfield
                + " order by num desc";
        if ("Atlas".equalsIgnoreCase(baseDao.getDataSource().getDatabase())) {
            String sqltemp[] = StringUtil.toLowerCase(sql).split("order by");
            sql = sqltemp[0];
        }
        return baseDao.findList(sql, 0, recordnum, Record.class);
    }

    public List<Record> selectKaoQinInfo(String kaoqinYear, String month) {
        String sql = "select\r\n" + 
                "    adm.rowguid,adm.userguid,adm.username,adm.ouguid,adm.cdcount,adm.ztcount,adm.yzcdcount,adm.kfyjinfo\r\n" + 
                "from\r\n" + 
                "    audit_ddkqtj_month adm\r\n" + 
                "join frame_ou_extendinfo foe \r\n" + 
                "on\r\n" + 
                "    adm.ouguid = foe.OUGUID\r\n" + 
                "join audit_orga_member aom \r\n" + 
                "on adm.userguid = aom.UserGuid \r\n" + 
                "where\r\n" + 
                "    adm.year = ? \r\n" + 
                "    and adm.month = ? \r\n" + 
                "    and foe.areacode like '370911%'\r\n" + 
                "    and aom.Is_KaoQin = '1'" +
                "    and adm.kqtxstatus = '0'"
                ;
        return baseDao.findList(sql, Record.class,kaoqinYear,month);
    }

    public Object updateKfyjStatus(String str, String kaoqinYear, String kaoqinMonth) {
        String sql = "update audit_ddkqtj_month set kqtxstatus = '1' where rowguid=? and year =? and month = ?"; 
        return baseDao.execute(sql,str,kaoqinYear,kaoqinMonth);
    }

    public List<Record> getJbjTaskInfo(String areacode, int first, int pageSize) {
        String sql = "select atk.taskname,atk.task_id,count(1) as num from audit_project ap \r\n" + 
                "join audit_task atk \r\n" + 
                "on ap.taskguid = atk.rowguid\r\n" + 
                "where ap.areacode = ? \r\n" + 
                "and ap.status>=24\r\n" + 
                "and ap.applydate >= '2022-01-01 00:00:00'\r\n" +
                "and atk.type='1'\r\n" + 
                "group by atk.taskname,atk.task_id\r\n" + 
                "order by num desc limit ?,?";
        return baseDao.findList(sql, Record.class,areacode,first*pageSize,pageSize);
    }

    public AuditTask getTaskInfo(String task_id) {
        String sql = "select rowguid,taskname from audit_task a where a.task_id = '6e3f2788-664e-4637-90f0-1211dbb7dba9' and a.is_enable =1 and a.IS_EDITAFTERIMPORT = 1 and a.is_history =0";
        return baseDao.find(sql,AuditTask.class, task_id);
    }

    public int getJbjTaskInfoNum(String areacode) {
        String sql = "select count(1) from (\r\n" + 
                "select atk.task_id as num from audit_project ap \r\n" + 
                "join audit_task atk \r\n" + 
                "on ap.taskguid = atk.rowguid\r\n" + 
                "where ap.areacode = ? \r\n" + 
                "and ap.status>=24\r\n" + 
                "and ap.applydate >= '2022-01-01 00:00:00'\r\n" + 
                "and atk.type='1'\r\n" + 
                "group by atk.taskname,atk.task_id\r\n" + 
                ") a";
        return baseDao.queryInt(sql,areacode);
    }

    public List<AuditPerformanceDetail> findDeailList(String recordrowguid, String recordrulerowguid, String str) {
        String sql = "select * from AUDIT_PERFORMANCE_DETAIL where recordrowguid = ? and recordrulerowguid = ? and objectguid = ? ";
        return baseDao.findList(sql, AuditPerformanceDetail.class, recordrowguid,recordrulerowguid,str);
    }

    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getAuditTaskTaianByTaskId(String taskId) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from audit_task_taian where task_id = ?1";
        return dao.find(sql, Record.class, taskId);
    }

    public CertInfo getCertinfoByCertCatalogidAndcplb(String certcatalogid, String cplb) {
        String sql ="select operatedate,certno,certcatalogid from cert_info where certcatalogid = ? and certno like '"+cplb+"%' order by operatedate desc limit 1";
        return baseDao.find(sql, CertInfo.class,certcatalogid);
    }
}
