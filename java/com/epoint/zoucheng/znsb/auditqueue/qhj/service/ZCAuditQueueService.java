package com.epoint.zoucheng.znsb.auditqueue.qhj.service;



import com.epoint.basic.auditqueue.auditqueueinstance.inter.IAuditQueueInstance;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.utils.container.ContainerFactory;


public class ZCAuditQueueService extends AuditCommonService {

	private static final long serialVersionUID = 8646940188931737486L;

	IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);

	IAuditQueueWindowTasktype windowtasktypeservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueWindowTasktype.class);
	IAuditQueueInstance queueinstanceservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueInstance.class);
	/**
	 * 
	 * 当前身份证取号总数
	 * 
	 * @param conditionMap
	 * @return
	 * 
	 * 
	 */
	public int getQueueCountBySfz(String sfz ,String FromDate,String ToDate) {
		String sql = " select count(1) from (select IDENTITYCARDNUM,GETNOTIME from audit_queue   "
				+ "  union all select IDENTITYCARDNUM,GETNOTIME from audit_queue_history ) aa where IDENTITYCARDNUM = ?1 and GETNOTIME > ?2 and GETNOTIME < ?3 ;";
		return commonDao.queryInt(sql, sfz,FromDate,ToDate);
	}

    public void updateIsSendSmsByQNO(String qno, String centerguid) {
		String sql = "update audit_queue set is_hassendsms = 1 where  qno= ? and centerguid=?";
		commonDao.execute(sql,qno,centerguid);
    }
}
