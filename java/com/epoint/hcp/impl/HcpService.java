package com.epoint.hcp.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.hcp.api.entity.lcproject;
import com.epoint.hcp.api.entity.lcprojecteight;
import com.epoint.hcp.api.entity.lcprojectfive;
import com.epoint.hcp.api.entity.lcprojectfour;
import com.epoint.hcp.api.entity.lcprojectnine;
import com.epoint.hcp.api.entity.lcprojectseven;
import com.epoint.hcp.api.entity.lcprojectsix;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojectthree;
import com.epoint.hcp.api.entity.lcprojecttwo;

/**
 * 好差评相关接口的详细实现
 *
 * @version [版本号, 2020年6月8日]
 * @作者 atjiao
 */
public class HcpService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;

    public HcpService() {
        dao = CommonDao.getInstance();
    }

    public int addEvaluateService(Record r) {
        return dao.insert(r);
    }


    public int addEvaluate(Record r) {
        return dao.insert(r);
    }

    /**
     * 根据办件编号及服务次数，获取办件服务信息
     *
     * @param projectno
     * @param assessNumber
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getServiceByProjectno(String projectno, int assessNumber) {
        String sql = "SELECT * FROM evaluateservice WHERE projectNo = ? AND serviceNumber = ?";
        return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnotwo(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicetwo WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnothree(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicethree WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnofour(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicefour WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnofive(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicefive WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnosix(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicesix WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnoseven(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateserviceseven WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnoeight(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateserviceeight WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnonine(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateservicenine WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }
    public Record getServiceByProjectnoten(String projectno, int assessNumber) {
    	String sql = "SELECT * FROM evaluateserviceten WHERE projectNo = ? AND serviceNumber = ?";
    	return dao.find(sql, Record.class, projectno, assessNumber);
    }

    /**
     * 根据rowguid获取办件评价信息
     *
     * @param rowguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getInatanceByRowguid(String rowguid) {
        String sql = "SELECT * FROM evainstance WHERE rowguid = ? ";
        return dao.find(sql, Record.class, rowguid);
    }

    /**
     * 修改评价数据
     *
     * @param r
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateEva(Record r) {
        String projectno = r.getStr("projectno");
        String assessNumber = r.getStr("assessNumber");
        String sbsign = r.getStr("sbsign");
        String sberrordesc = r.getStr("sberrordesc");
        String sql = "update evainstance set sbsign =?,sberrordesc =? where projectno = ? and assessNumber = ?";
        dao.execute(sql, sbsign, sberrordesc, projectno, assessNumber);
    }

    /**
     * 修改办件服务数据
     *
     * @param r
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateProService(Record r) {
        String projectno = r.getStr("projectno");
        String servicenumber = r.getStr("servicenumber");
        String sbsign = r.getStr("sbsign");
        String sberrordesc = r.getStr("sberrordesc");
        String sql = "update evaluateservice set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
        dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicetwo(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicetwo set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicethree(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicethree set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicefour(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicefour set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicefive(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicefive set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicesix(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicesix set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServiceseven(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateserviceseven set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServiceeight(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateserviceeight set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServicenine(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateservicenine set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }
    
    public void updateProServiceten(Record r) {
    	String projectno = r.getStr("projectno");
    	String servicenumber = r.getStr("servicenumber");
    	String sbsign = r.getStr("sbsign");
    	String sberrordesc = r.getStr("sberrordesc");
    	String sql = "update evaluateserviceten set sbsign =?,sberrordesc =? where projectno = ? and servicenumber = ?";
    	dao.execute(sql, sbsign, sberrordesc, projectno, servicenumber);
    }

    /**
     * 根据办件编号及服务次数，获取评价信息
     *
     * @param projectno
     * @param assessNumber
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Boolean findEva(String projectno, int assessNumber) {
        String sql = "SELECT * FROM evainstance WHERE projectNo = ? AND assessNumber = ?";
        Record eva = dao.find(sql, Record.class, projectno, assessNumber);
        if (eva != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取我的评价列表数据
     *
     * @param projectname
     * @param applyername
     * @param applyerpagecode
     * @param status          1：未评价  2：已评价
     * @param currentpage
     * @param pagesize
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getMyEvaluate(String projectname, String applyername, String applyerpagecode, Integer status,
                                      Integer currentpage, Integer pagesize) {
        String sql = "SELECT s.projectNo,s.taskName projectname,s.userName ApplyerName,s.acceptDate ApplyDate,s.orgName,s.proStatus projectstatus,s.serviceName,s.serviceNumber FROM evaluateservice s";
        if (status == 1) {
            sql += " LEFT JOIN evainstance e ON e.projectno = s.projectNo AND e.assessNumber = s.serviceNumber WHERE e.projectno is NULL AND e.assessNumber is NULL ";
        } else {
            sql += " JOIN evainstance e ON e.projectno = s.projectNo AND e.assessNumber = s.serviceNumber WHERE 1=1 ";
        }
        sql += " and s.userName = '" + applyername + "' AND (s.certKey = '" + applyerpagecode + "' or s.certKeyGOV = '" + applyerpagecode + "') AND s.taskName like '%" + projectname + "%' order by ApplyDate desc limit " + (currentpage * pagesize) + "," + pagesize;
        return dao.findList(sql, Record.class);
    }

    /**
     * 获取我的评价总数
     *
     * @param projectname
     * @param applyername
     * @param applyerpagecode
     * @param status
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public int getMyEvaluateCount(String projectname, String applyername, String applyerpagecode, Integer status) {
        String sql = "SELECT count(1) FROM evaluateservice s";
        if (status == 1) {
            sql += " LEFT JOIN evainstance e ON e.projectno = s.projectNo AND e.assessNumber = s.serviceNumber WHERE e.projectno is NULL AND e.assessNumber is NULL ";
        } else {
            sql += " JOIN evainstance e ON e.projectno = s.projectNo AND e.assessNumber = s.serviceNumber WHERE 1=1 ";
        }
        sql += " and s.userName = '" + applyername + "' AND (s.certKey = '" + applyerpagecode + "' or s.certKeyGOV = '" + applyerpagecode + "') AND s.taskName like '%" + projectname + "%'";
        return dao.queryInt(sql);
    }

    /**
     * 评价详情
     *
     * @param projectno
     * @param servicenumber
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getProService(String projectno, int servicenumber) {
        Record record = new Record();
        String sql = "SELECT * FROM evaluateservice WHERE projectNo = ? AND serviceNumber = ?";
        Record proService = dao.find(sql, Record.class, projectno, servicenumber);
        String proStatus = proService.getStr("prostatus");
        if ("1".equals(proStatus)) {
            proStatus = "待受理";
        } else if ("2".equals(proStatus)) {
            proStatus = "已受理";
        } else {
            proStatus = "已办结";
        }
        proService.set("idnumber", proService.getStr("certKey"));
        if (StringUtil.isBlank(proService.getStr("certKey"))) {
            proService.set("idnumber", proService.getStr("certKeyGOV"));
        }
        proService.set("prostatus", proStatus);
        record.put("proservice", proService);
        String evasql = "SELECT * FROM evainstance WHERE projectNo = ? AND assessNumber = ?";
        Record evainstance = dao.find(evasql, Record.class, projectno, servicenumber);
        if (evainstance != null) {
            String gradeinfo = evainstance.getStr("evalDetail");
            List<Record> list = new ArrayList<Record>();
            String[] split = null;
            if (gradeinfo != null) {
                split = gradeinfo.split(",");
            }
            if (split != null && split.length > 0) {
                for (String info : split) {
                    String sql2 = "select itemtext from code_items where CODEID=8 and  ITEMVALUE = '" + info + "'";
                    Record find2 = dao.find(sql2, Record.class);

                    if (find2 != null) {
                        list.add(find2);
                    }
                }
            }
            evainstance.put("evacontextlist", list);
        }
        record.put("evainstance", evainstance);
        //事项信息
        String tasksql = "SELECT deptcode,TaskName,deptname,TaskCode,isonline,isschedule,isexpress,ispayonline,LimitSceneNum,UseLevel from tablecache_tasklist where taskcode=? ";
        Record task = this.dao.find(tasksql, Record.class, proService.getStr("taskcode"));
        record.put("task", task);
        return record;
    }



    public List<Record> getWaitEvaluateList(int start, int size) {
        String sql = "select a.* from evaluateservice a where sbsign in ('1','88')  limit ?1,?2";
        return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateTwoList(int start, int size) {
    	String sql = "select a.* from evaluateservicetwo a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateThreeList(int start, int size) {
    	String sql = "select a.* from evaluateservicethree a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateFourList(int start, int size) {
    	String sql = "select a.* from evaluateservicefour a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateFiveList(int start, int size) {
    	String sql = "select a.* from evaluateservicefive a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateSixList(int start, int size) {
    	String sql = "select a.* from evaluateservicesix a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateSevenList(int start, int size) {
    	String sql = "select a.* from evaluateserviceseven a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateEightList(int start, int size) {
    	String sql = "select a.* from evaluateserviceeight a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateNineList(int start, int size) {
    	String sql = "select a.* from evaluateservicenine a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }
    
    public List<Record> getWaitEvaluateTenList(int start, int size) {
    	String sql = "select a.* from evaluateserviceten a where sbsign in ('1','88')  limit ?1,?2";
    	return dao.findList(sql, Record.class, start, size);
    }

    public List<Record> getOneTwoEvaluateList() {
        String sql = "select * from evaluateservice where  sbsign = '1' and servicenumber = '2' and areacode = '370800' group by projectno limit 200";
        return dao.findList(sql, Record.class);
    }

    public Record findEvaluateservice(String projectno, String servicenumber) {
        String sql = "SELECT * FROM evainstance WHERE projectNo = ? AND assessNumber = ?";
        return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicetwo(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancetwo WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicethree(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancethree WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicefour(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancefour WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicefive(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancefive WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicesix(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancesix WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateserviceseven(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstanceseven WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateserviceeight(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstanceeight WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateservicenine(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstancenine WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }
    
    public Record findEvaluateserviceten(String projectno, String servicenumber) {
    	String sql = "SELECT * FROM evainstanceten WHERE projectNo = ? AND assessNumber = ?";
    	return dao.find(sql, Record.class, projectno, servicenumber);
    }

    public List<AuditProject> getWaitEvaluateSbList() {
		String sql = "select * from audit_project where ROUND((UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(applydate))/60/60/24) <= 5 and is_lczj in ('2','8','9') and ifnull(hcpstatus,0) = 0 limit 200";
        return dao.findList(sql, AuditProject.class);
    }

    public List<lcproject> getLcEvaluateSbList(int start, int size) {
        String sql = "select a.* from lc_project a where ROUND((UNIX_TIMESTAMP(now())-UNIX_TIMESTAMP(applydate))/60/60/24) <= 5  and ifnull(hcpstatus,0) = 0 limit ?1,?2";
        return dao.findList(sql, lcproject.class, start, size);
    }
    
    public List<lcprojecttwo> getLcEvaluateSbTwoList(int start, int size) {
    	String sql = "select a.* from lc_project_two a where ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojecttwo.class, start, size);
    }
    public List<lcprojectthree> getLcEvaluateSbThreeList(int start, int size) {
    	String sql = "select a.* from lc_project_three a where ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectthree.class, start, size);
    }
    public List<lcprojectfour> getLcEvaluateSbFourList(int start, int size) {
    	String sql = "select a.* from lc_project_four a where  ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectfour.class, start, size);
    }
    public List<lcprojectfive> getLcEvaluateSbFiveList(int start, int size) {
    	String sql = "select a.* from lc_project_five a where  ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectfive.class, start, size);
    }
    public List<lcprojectsix> getLcEvaluateSbSixList(int start, int size) {
    	String sql = "select a.* from lc_project_six a where  ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectsix.class, start, size);
    }
    public List<lcprojectseven> getLcEvaluateSbSevenList(int start, int size) {
    	String sql = "select a.* from lc_project_seven a where ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectseven.class, start, size);
    }
    public List<lcprojecteight> getLcEvaluateSbEightList(int start, int size) {
    	String sql = "select a.* from lc_project_eight a where ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojecteight.class, start, size);
    }
    public List<lcprojectnine> getLcEvaluateSbNineList(int start, int size) {
    	String sql = "select a.* from lc_project_nine a where  ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectnine.class, start, size);
    }
    public List<lcprojectten> getLcEvaluateSbTenList(int start, int size) {
    	String sql = "select a.* from lc_project_ten a where  ifnull(hcpstatus,0) = 0 limit ?1,?2";
    	return dao.findList(sql, lcprojectten.class, start, size);
    }

    public void updateLcProject(String status, String rowguid) {
        String sql = "update lc_project set hcpstatus = ? where rowguid = ? ";
        dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjecttwo(String status, String rowguid) {
    	String sql = "update lc_project_two set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectthree(String status, String rowguid) {
    	String sql = "update lc_project_three set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectfour(String status, String rowguid) {
    	String sql = "update lc_project_four set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectfive(String status, String rowguid) {
    	String sql = "update lc_project_five set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectsix(String status, String rowguid) {
    	String sql = "update lc_project_six set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectseven(String status, String rowguid) {
    	String sql = "update lc_project_seven set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjecteight(String status, String rowguid) {
    	String sql = "update lc_project_eight set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectnine(String status, String rowguid) {
    	String sql = "update lc_project_nine set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }
    
    public void updateLcProjectten(String status, String rowguid) {
    	String sql = "update lc_project_ten set hcpstatus = ? where rowguid = ? ";
    	dao.execute(sql, status, rowguid);
    }

    public List<Record> findEvaluateserviceByFlowsn(String s) {
        String sql = "select username,servicenumber,areacode,prostatus,acceptdate,servicetime,taskcode,orgcode,orgname,certkey from evaluateservice where projectno=? order by servicenumber desc";
        List<Record> list = dao.findList(sql, Record.class, s);
        dao.close();
        return list;
    }

    public void updateProject(String projectno, String s) {
        try {
            dao.beginTransaction();
            String sql = "update audit_project set hcpstatus= ?1 where flowsn=?2 ";
            dao.execute(sql,s,projectno);
            dao.commitTransaction();
        } catch (Exception e) {
            dao.rollBackTransaction();
        } finally {
            dao.close();
        }
    }

}
