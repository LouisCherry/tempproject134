package com.epoint.tongbufw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.commonvalue.FWConstants;
import com.epoint.audittask.util.SyncCommonUtil;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.workflow.service.common.custom.Activity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;

/**
 * 事项同步服务
 * 
 * @author xbn
 * @version 
 */
public class EpointSynunidService
{

    transient  Logger log = LogUtil.getLog(EpointSynunidService.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected  ICommonDao commonDaoTo;
    private IWorkflowProcessService iWorkflowProcessService = ContainerFactory.getContainInfo()
            .getComponent(IWorkflowProcessService.class);
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointSynunidService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

	public List<AuditTask> getUnid() {
		String sql="select * from audit_task where (IS_HISTORY=0 OR IS_HISTORY IS NULL OR IS_HISTORY = '') AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0 and UNID is null LIMIT 10";
		return commonDaoTo.findList(sql, AuditTask.class);
	}

	public String  getqzkunid(String itemid) {
		String sql="select rowguid from QLT_QLSX where dh_state=1 and INNER_CODE=?";
		return commonDaoFrom.queryString(sql, itemid);
	}


}
