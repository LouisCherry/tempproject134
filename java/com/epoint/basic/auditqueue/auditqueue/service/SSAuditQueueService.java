package com.epoint.basic.auditqueue.auditqueue.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueueinstance.inter.IAuditQueueInstance;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public class SSAuditQueueService extends AuditCommonService {

	private static final long serialVersionUID = 8646940188931737486L;

	IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);

	IAuditQueueWindowTasktype windowtasktypeservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueWindowTasktype.class);
	IAuditQueueInstance queueinstanceservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueInstance.class);

	private Logger log = Logger.getLogger(SSAuditQueueService.class);

	
	 public String getNextQueueNOshardingNoLock(String WindowGuid, String CenterGuid, String UserGuid,boolean isautoassign) {
		 String sql = "";
		 String QNO = "";
		 String TaskGuid = "";
		 String QueueTableName = "Audit_Queue_Instance";
		 String Queuevalue = "";
		 AuditQueueOrgaWindow window = windowservice.getDetailbyWindowguid(WindowGuid).getResult();
		 if (window != null) {
			 Queuevalue = window.getQueuevalue();
		 }
		 QueueTableName += "_" + Queuevalue;
		 String APPLYUSERTYPE = " APPLYUSERTYPE = " + QueueConstant.Qno_Type_Common + " ";
		 // 判断队列里面是否有优先号
		 if (queueinstanceservice.existPriority(WindowGuid, Queuevalue).getResult()) {
			 APPLYUSERTYPE = " APPLYUSERTYPE != " + QueueConstant.Qno_Type_Common + " ";
		 }
		 // 如果使用窗口事项分类优先级功能
		 int isusepriority = windowtasktypeservice.IsUsePriorityLevel(WindowGuid).getResult();
		 if (isusepriority > 0) {
			 if (commonDao.isSqlserver()) {
				 sql = "select Top 1 QNO,Taskguid from " + QueueTableName
						 + " a  ( select TaskTypeGuid,prioritylevel from audit_queue_window_tasktype  "
						 + " where WindowGuid = '" + WindowGuid + "' ) b  ";
				 sql += " where a.TaskGuid = b.TaskTypeGuid and " + APPLYUSERTYPE
						 + " and a.handlewindows like '%" + WindowGuid + "%'"
						 + " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111)  ";
				 sql += " order by ifnull(prioritylevel,0) desc,QueueWeight desc,GETNOTIME asc,QNO asc ";
			 }
			 else if (commonDao.isOracle() || commonDao.isDM()
					 || "Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
					 || "zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
					 || "kingbase8".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
				 sql = "select QNO,Taskguid from ( select QNO,Taskguid from " + QueueTableName + " a, "
						 + " ( select TaskTypeGuid,prioritylevel from audit_queue_window_tasktype  where WindowGuid = '"
						 + WindowGuid + "' ) b ";
				 sql += " where a.TaskGuid = b.TaskTypeGuid  and " + APPLYUSERTYPE
						 + " and a.handlewindows like '%" + WindowGuid + "%'"
						 + "  and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD') ";
				 sql += " order by NVL(prioritylevel,0) desc,QueueWeight desc,GETNOTIME asc,QNO asc ) q where rownum <= 1";

			 }
			 else {
				 sql = "select QNO,Taskguid from ( select QNO,Taskguid from " + QueueTableName + " a, "
						 + " ( select TaskTypeGuid,prioritylevel from audit_queue_window_tasktype  where WindowGuid = '"
						 + WindowGuid + "' ) b ";
				 sql += "  where a.TaskGuid = b.TaskTypeGuid and " + APPLYUSERTYPE
						 + " and a.handlewindows like '%" + WindowGuid + "%'"
						 + " and  date(GETNOTIME) = curdate() ";
				 sql += " order by ifnull(prioritylevel,0) desc, QueueWeight desc,GETNOTIME asc,QNO asc ) q limit 0,1";
			 }
		 }
		 else {
			 if (commonDao.isSqlserver()) {
				 sql = "select Top 1 QNO,Taskguid from " + QueueTableName + " where handlewindows like '%" + WindowGuid
						 + "%'";
				 sql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) and "
						 + APPLYUSERTYPE + " ";
				 sql += " order by QueueWeight desc,GETNOTIME asc,QNO asc";
			 }
			 else if (commonDao.isOracle() || commonDao.isDM()
					 || "Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
					 || "zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
					 || "kingbase8".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
				 sql = "select QNO,Taskguid from ( select QNO,Taskguid from " + QueueTableName
						 + " where handlewindows like '%" + WindowGuid + "%'";
				 sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD') and " + APPLYUSERTYPE
						 + " ";
				 sql += " order by QueueWeight desc,GETNOTIME asc,QNO asc ) q where rownum <= 1";

			 }
			 else {
				 sql = "select QNO,Taskguid from ( select QNO,Taskguid from " + QueueTableName
						 + " where handlewindows like '%" + WindowGuid + "%'";
				 sql += " and  date(GETNOTIME) = curdate() and  " + APPLYUSERTYPE + " ";
				 sql += " order by QueueWeight desc,GETNOTIME asc,QNO asc ) q limit 0,1";
			 }
		 }
		 Record result = commonDao.find(sql, Record.class, WindowGuid);
		 // 自动分配等待最长时间窗口编号
		 String maxWaitWindow = WindowGuid;
		 if (StringUtil.isNotBlank(result)) {
			 QNO = StringUtil.getNotNullString(result.get("QNO"));
			 TaskGuid = StringUtil.getNotNullString(result.get("Taskguid"));
			 // 判断逻辑,先获取在同一轮询次数的窗口列表
			 List<String> windowguids = windowtasktypeservice.getWindowListByTaskTypeGuid(TaskGuid).getResult();
			 List<AuditQueueOrgaWindow> windowList = new ArrayList<AuditQueueOrgaWindow>();
			 int currentLoop =  Integer.parseInt(window.get("loopcount"));
			 String currentWaitQno = window.get("waitqno");
			 for (String para : windowguids) {
				 AuditQueueOrgaWindow currentWindow = windowservice.getDetailbyWindowguid(para).getResult();
				 if(StringUtil.isNotBlank(currentWindow.get("waittime"))){
					 int tempLoop =  Integer.parseInt(currentWindow.get("loopcount"));
					 String tempWaitQno = currentWindow.get("waitqno");
					 if((currentLoop == 0 || (currentLoop>=1 && tempLoop==currentLoop && currentWaitQno.equals(tempWaitQno))) && currentLoop<=2){
						 windowList.add(currentWindow);
					 }
				 }
			 }
			 // 先判断是否有需要判断的轮询窗口列表
			 if(!windowList.isEmpty()){
				 // 根据等待时间，获取出等待最长的窗口编号
				 windowList.sort((AuditQueueOrgaWindow w1,AuditQueueOrgaWindow w2)->EpointDateUtil.compareDateOnDay(
						 EpointDateUtil.convertString2Date(w1.get("waittime"), "yyyy-MM-dd HH:mm:ss"),
						 EpointDateUtil.convertString2Date(w2.get("waittime"), "yyyy-MM-dd HH:mm:ss")));
				 maxWaitWindow = windowList.get(0).getWindowguid();
			 }
			 // 如果当前窗口就是最长等待窗口
			 if(maxWaitWindow.equals(WindowGuid)){
				 // 删除实例表数据
				 commonDao.execute("delete from " + QueueTableName + " where qno='" + QNO + "'");
				 commonDao.commitTransaction();
			 }

		 }

		 // 更新排队状态
		 if (StringUtil.isNotBlank(QNO)) {
			 if(maxWaitWindow.equals(WindowGuid)){
				 updateQNOStatusandHandleWindow(QueueConstant.Qno_Status_Processing, QNO, WindowGuid, CenterGuid, UserGuid,
						 new Date());
			 }
			 ZwfwRedisCacheUtil redis = null;
			 try {
				 redis = new ZwfwRedisCacheUtil(false);
				 // 转号判断
				 if (QueueConstant.QUEUE_TURN_WINDOW
						 .equals(getQNODetailByQNO("turnqnotype", QNO, CenterGuid).getTurnQnoType())) {
					 // redis 计数-1
					 redis.hincrBy("AuditQueueOrgaWindow_" + WindowGuid, "waitnum", -1);
				 }
				 else {
					 List<String> windowguids = windowtasktypeservice.getWindowListByTaskTypeGuid(TaskGuid).getResult();
					 if(maxWaitWindow.equals(WindowGuid)){
						 for (String para : windowguids) {
							 // redis 计数-1
							 redis.hincrBy("AuditQueueOrgaWindow_" + para, "waitnum", -1);
							 // 叫号并且重置本次轮询数据
							 redis.updateByHash(AuditQueueOrgaWindow.class, para, "loopcount", QueueConstant.CONSTANT_STR_ZERO);
							 redis.updateByHash(AuditQueueOrgaWindow.class, para, "waitqno", "");
						 }
					 }
					 else{
						 // 设置窗口等待分配号码，轮询次数
						 redis.updateByHash(AuditQueueOrgaWindow.class, WindowGuid, "waitqno", QNO);
						 redis.hincrBy("AuditQueueOrgaWindow_" + WindowGuid, "loopcount", 1);
					 }
				 }
			 }
			 catch (Exception e) {
				 throw new RuntimeException("redis执行发生了异常", e);
			 }
			 finally {
				 if (redis != null) {
					 redis.close();
				 }
			 }
		 }
		 // 如果当前窗口就是最长等待窗口
		 if(!maxWaitWindow.equals(WindowGuid)){
			 QNO = "";
		 }
		 windowservice.updateCurrentHandleNO(WindowGuid, QNO);
		 return StringUtil.getNotNullString(QNO);

	 }

	public void updateQNOStatusandHandleWindow(String Status, String QNO, String Windowguid, String centerguid) {
		String sql = "update AUDIT_QUEUE set Status=?1,HANDLEWINDOWGUID=?3 where QNO=?2 and CenterGuid=?4 ";

		if (commonDao.isSqlserver()) {
			sql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) ";
		}
		else if (commonDao.isOracle() || commonDao.isDM()
				|| "Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "kingbase8".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
			sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
		}
		else {
			sql += " and  date(GETNOTIME) = curdate() ";
		}
		commonDao.execute(sql, Status, QNO, Windowguid, centerguid);
	}

	public void updateQNOStatusandHandleWindow(String Status, String QNO, String Windowguid, String centerguid,
											   String handleuserguid, Date CallTime) {
		String sql = "update AUDIT_QUEUE set Status=?1,HANDLEWINDOWGUID=?3,handleuserguid=?5,CallTime=?6 where QNO=?2 and CenterGuid=?4 ";

		if (commonDao.isSqlserver()) {
			sql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) ";
		}
		else if (commonDao.isOracle() || commonDao.isDM()
				|| "Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "kingbase8".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
			sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
		}
		else {
			sql += " and  date(GETNOTIME) = curdate() ";
		}
		commonDao.execute(sql, Status, QNO, Windowguid, centerguid, handleuserguid, CallTime);
	}

	public AuditQueue getQNODetailByQNO(String fieldstr, String QNO, String CenterGuid) {
		String strSql = "select " + fieldstr + " from AUDIT_QUEUE where QNO=?1 and CenterGuid=?2 ";
		if (commonDao.isSqlserver()) {
			strSql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) ";
		}
		else if (commonDao.isOracle() || commonDao.isDM()
				|| "Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())
				|| "kingbase8".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
			strSql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
		}
		else {
			strSql += " and  date(GETNOTIME) = curdate() ";
		}
		return commonDao.find(strSql, AuditQueue.class, QNO, CenterGuid);
	}
}
