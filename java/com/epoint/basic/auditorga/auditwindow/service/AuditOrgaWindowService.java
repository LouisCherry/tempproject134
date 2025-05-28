package com.epoint.basic.auditorga.auditwindow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.service.SSAuditQueueService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

/**
 * 产品代码排查，后续需要删除
 * 事项窗口service
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 
 
 */
public class AuditOrgaWindowService 
{
    private Logger log = Logger.getLogger(AuditOrgaWindowService.class);    /**
     * 通用dao操作
     */
    private CommonDao commonDao;

    public AuditOrgaWindowService() {
        commonDao = CommonDao.getInstance("orga");
    }
    /**
     * 
     * 获取所有的窗口
     *  @return List<AuditWindow>   
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditOrgaWindow> selectAllWindow() {
        String sql = "SELECT * FROM AUDIT_ORGA_WINDOW order by ORDERNUM desc";
        List<AuditOrgaWindow> list = commonDao.findList(sql, AuditOrgaWindow.class);
        return list;
    }

    /**
     * 
     *  根据用户guid获取用户窗口信息
     *  @param userGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditOrgaWindow selectWindowByUserGuid(String userGuid) {
        AuditOrgaWindow auditWindow = null;
        String windowGuid = commonDao.find("select WINDOWGUID from AUDIT_ORGA_WINDOWUSER where USERGUID=?1",
                String.class, userGuid);
        if (StringUtil.isNotBlank(windowGuid)) {
            auditWindow = commonDao.find(AuditOrgaWindow.class, windowGuid);
        }
        return auditWindow;
    }

    /**
     * 
     *  根据窗口guid删除 窗口用户、窗口事项的关联关系
     *  @param windowGuid 窗口guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteWindowAndRelation(String windowGuid) {
        if (StringUtil.isNotBlank(windowGuid)) {
            log.error("删除窗口："+windowGuid+",操作人："+ UserSession.getInstance().getDisplayName());
            commonDao.execute("delete from AUDIT_ORGA_WINDOW where ROWGUID=?1", windowGuid);
            //删除窗口事项关系
            commonDao.execute("delete from  AUDIT_ORGA_WINDOWTASK where WINDOWGUID=?1", windowGuid);
            //删除窗口人员关系
            commonDao.execute("delete from AUDIT_ORGA_WINDOWUSER where WINDOWGUID=?1", windowGuid);
        }
    }

    /**
     * 
     *  更新窗口
     *  @param auditwindow 窗口对象
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateAuditWindow(AuditOrgaWindow auditwindow) {
        commonDao.update(auditwindow);
    }

    /**
     * 
     *  获取窗口分页
     *  @param conditionMap
     *  @param first
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditOrgaWindow> getWindowByPage(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        return this.getListByPage(AuditOrgaWindow.class, conditionMap, first, pageSize, sortField, sortOrder);
    }

    /**
     * 
     * 获取窗口个数
     *  @param conditionMap
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer getWindowCount(Map<String, String> conditionMap) {
        return this.getListByPageCount(AuditOrgaWindow.class, conditionMap);
    }

    /**
     * 
     *  添加窗口信息
     *  @param auditWindow    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void insertWindow(AuditOrgaWindow auditWindow) {
        commonDao.insert(auditWindow);
    }

    /**
     * 根据窗口编号、大厅类型判断窗口编号是否存在
     *
     * @param windowno
     *            窗口编号
     * @param lobbytype
     *            大厅类型
     * @return boolean true:窗口编号存在，false:窗口编号不存在
     */
    public boolean isExitWindowNo(String windowno, String lobbytype) {
        boolean retbol = false;
        // 允许不同大厅编号相同，同一大厅编号唯一
        String sql = "select * from AUDIT_ORGA_WINDOW where WINDOWNO =?1 AND LOBBYTYPE=?2";
        List<AuditOrgaWindow> windowlist = commonDao.findList(sql, AuditOrgaWindow.class, windowno, lobbytype);
        if (!windowlist.isEmpty()) {
            retbol = true;
        }
        return retbol;
    }

    /**
     * 
     *  通过窗口guid获取窗口对象
     *  @param windowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditOrgaWindow getWindowByGuid(String windowGuid) {
        AuditOrgaWindow auditWindow = null;
        auditWindow = commonDao.find(AuditOrgaWindow.class, windowGuid);
        return auditWindow;
    }

    /**
     * 
     *  通过taskid获取中心标识
     *  
     *  @param taskid
     *  @return    
     */
    public List<Record> selectCenterGuidsByTaskId(String taskid) {
        String sql = "SELECT DISTINCT a.centerGuid FROM audit_orga_window a ,audit_orga_windowtask b WHERE a.RowGuid=b.WINDOWGUID AND b.TASKID =?1";
        return commonDao.findList(sql, Record.class, taskid);
    }
    
    /**
     * 
     *  通过taskid获取窗口列表
     *  
     *  @param taskid
     *  @return    
     */
    public List<AuditOrgaWindow> getWindowListByTaskId(String taskid) {
        String sql = "SELECT a.* FROM audit_orga_window a ,audit_orga_windowtask b WHERE a.RowGuid=b.WINDOWGUID AND b.TASKID =?1";
        return commonDao.findList(sql, AuditOrgaWindow.class, taskid);
    }

    public List<AuditOrgaWindow> getWindowListByUserGuid(String userguid) {
        String strSql = "select audit_orga_window.* from audit_orga_window,audit_orga_windowuser where audit_orga_window.RowGuid=audit_orga_windowuser.WINDOWGUID and userguid=?1";

        return commonDao.findList(strSql, AuditOrgaWindow.class, userguid);
    }

    public int getWindowCountByUserGuid(String userguid) {

        String sql = "select count(1) from audit_orga_window,audit_orga_windowuser where audit_orga_window.RowGuid=audit_orga_windowuser.WINDOWGUID and userguid=?1";

        return commonDao.queryInt(sql, userguid);
    }

    public String getWindowByMacandUserGuid(String Mac, String userguid) {

        String sql = "select audit_orga_window.rowguid from audit_orga_window,audit_orga_windowuser where audit_orga_window.RowGuid=audit_orga_windowuser.WINDOWGUID and Mac=?1 and userguid=?2";

        return commonDao.find(sql, String.class, Mac, userguid);
    }
    
    public List<String> getOUListbyHall(String centerguid, String hallguid, int first, int pageSize) {
		String sql = "";
		if ("all".equals(hallguid)) {
			sql = "select DISTINCT ouguid from audit_orga_window where centerguid=?1 and IS_USEQUEUE='1'";
		} else {
			sql = "select DISTINCT ouguid from audit_orga_window where centerguid=?1 and  lobbytype=?2 and IS_USEQUEUE='1'";
		}
		return commonDao.findList(sql, first, pageSize, String.class, centerguid, hallguid);
	}

	public int getOUListCountbyHall(String centerguid, String hallguid) {

		String sql = "";
		if ("all".equals(hallguid)) {
			sql = "select Count(1) from ( select DISTINCT ouguid from audit_orga_window where centerguid=?1 and IS_USEQUEUE='1' ) a";
		} else {
			sql = "select Count(1) from ( select DISTINCT ouguid from audit_orga_window where centerguid=?1 and  lobbytype=?2 and IS_USEQUEUE='1' ) a";
		}

		return commonDao.queryInt(sql, centerguid, hallguid);
	}
	
	public List<String> getOUListbyHall(String centerguid, String hallguid) {
		String sql = "";
		if ("all".equals(hallguid)) {
			sql = "select DISTINCT ouguid from audit_orga_window where centerguid=?1 and IS_USEQUEUE='1'";
		} else {
			sql = "select DISTINCT ouguid from audit_orga_window where centerguid=?1 and  lobbytype=?2 and IS_USEQUEUE='1'";
		}
		return commonDao.findList(sql, String.class, centerguid, hallguid);
	}

    public String getOuguidByWindowGuid(String windowguid) {
        Record record = commonDao.find("select ouguid from audit_orga_window where rowguid=?", Record.class,windowguid);
        String ouguid = "";
        if(record!=null){
            ouguid = record.get("ouguid");
        }
        return ouguid ;
    }

    public List<Record> getoulistBycenterguid(String centerGuid) {
        List<Record> record = commonDao.findList("select distinct(ouguid) from audit_orga_window where centerguid = ?", Record.class, centerGuid);
        return record;
    }
    
    public  List<AuditOrgaWindow> selectByCenter(String condition,String centerguid){
        List<AuditOrgaWindow> list = new ArrayList<AuditOrgaWindow>();
        if(StringUtil.isNotBlank(condition) && StringUtil.isNotBlank(centerguid)){
            String sql = "select * from audit_orga_window where windowname like ? and centerguid=?";
            List<Object> params= new ArrayList<>();
            params.add("%" + condition.replace("\\", "\\\\").replace("%", "\\%") + "%");
            params.add(centerguid);
            list = commonDao.findList(sql,AuditOrgaWindow.class,params.toArray());     
        }
        return list;
    }
    public  List<AuditOrgaWindow> getWindowListByOU(String ouguid){
        return commonDao.findList("select * from audit_orga_window where ouguid=?1",AuditOrgaWindow.class,ouguid);
    }
    
    public List<AuditOrgaWindow> getAllByCenter(String centerguid){
        List<AuditOrgaWindow> list = new ArrayList<AuditOrgaWindow>();
            list = commonDao.findList("select * from audit_orga_window where  centerguid=? ",AuditOrgaWindow.class,centerguid);     
        return list;
    }
    
    public Integer getListByPageCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        Entity en = (Entity) baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        sb.append(this.buildSql(conditionMap));
        String sql = "select count(*) from " + en.table() + sb.toString();
        return this.commonDao.queryInt(sql, new Object[0]);
    }
    public String buildSql(Map<String, String> conditionMap) {
        // 条件sql
        StringBuffer sb = new StringBuffer(" where 1=1");
        sb.append(buildPatchSql(conditionMap));
        return sb.toString();
    }

    public String buildPatchSql(Map<String, String> conditionMap) {
        // 条件sql
        StringBuffer sb = new StringBuffer();
        if (conditionMap != null && conditionMap.size() > 0) {
            for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
                String fieldName = StringUtil.toUpperCase(entry.getKey().trim());

                // 如果是“like”开头
                if (StringUtil.toUpperCase(fieldName).endsWith("LIKE")) {
                    fieldName = fieldName.replaceAll("(?i)like$", "");
                    sb.append(" and " + fieldName + " like '%" + entry.getValue() + "%'");
                }
                // 如果是“IN”开头
                else if (StringUtil.toUpperCase(fieldName).endsWith("IN")) {
                    fieldName = fieldName.replaceAll("(?i)in$", "");
                    sb.append(" and " + fieldName + " in (" + entry.getValue() + ")");
                }
                //如果是link结尾
                else if(StringUtil.toUpperCase(fieldName).endsWith("LINK")){
                    fieldName = fieldName.replaceAll("(?i)link$", "");
                    sb.append(" and " + fieldName +" " +entry.getValue());
                }
                // 符号直接连接
                else {
                    sb.append(" and " + fieldName + "'" + entry.getValue() + "'");
                }
            }
        }
        return sb.toString();
    }
    /**
     * 
     * 获取列表分页,单表查询分页可以这么做。
     * 
     * @param <T>
     * @param baseClass
     * @param conditionMap
     *            条件map，key为字段值， value为条件，如果是=条件，则key为 "XXX=",如果是like条件，则key为
     *            "XXXlike", 目前只支持=，like，>,>=,<,<=
     * @param first
     *            分页第一条记录
     * @param pageSize
     *            分页大小
     * @param sortField
     *            排序字段
     * @param sortOrder
     *            排序顺序
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getListByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        Entity en = baseClass.getAnnotation(Entity.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(buildSql(conditionMap));

        // 增加Orderby语句
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }
        String sql = "select * from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) commonDao.findList(sql, first, pageSize, baseClass);
        return dataList;
    }
    
    public List<AuditOrgaWindow> getWindowListByUserGuidAndCondition(String userGuid, Map<String, String> conditionMap) {
        StringBuffer sb = new StringBuffer();
        SQLManageUtil sUtil = new SQLManageUtil();
        sb.append("select audit_orga_window.* from audit_orga_window,audit_orga_windowuser where audit_orga_window.RowGuid=audit_orga_windowuser.WINDOWGUID and userguid=?1");
        sb.append(sUtil.buildPatchSql(conditionMap));
        return commonDao.findList(sb.toString(), AuditOrgaWindow.class, userGuid);
    }
}
