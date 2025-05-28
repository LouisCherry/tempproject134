package com.epoint.audittask.result.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.CompareUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 事项审批结果新增页面对应的后台
 * 
 * @author Dong
 * @version [版本号, 2016-10-09 09:14:32]
 */
@RestController("jnauditresultaddaction")
@Scope("request")
public class JNAuditResultAddAction extends BaseController {
	/**
	* 
	*/
	private static final long serialVersionUID = -1542727706130420803L;
	

	/**
	 * 事项审批结果实体对象
	 */
	private AuditTaskResult dataBean = null;
	
	private CompareUtil compareUtil = new CompareUtil();

	/**
	 * 审批结果类型下拉列表model
	 */
	private List<SelectItem> resulttypeModel = null;

	/**
	 * 是否代码项
	 */
	private List<SelectItem> isprintModal;

	/**
	 * 选择的文书
	 */
	private String doc;

	/**
	 * 选择主题的guid
	 */
	private String businessguid;

	/**
	 * 选择共享材料的guid
	 */
	private String sharematirialguid;

	/**
	 * 事项主键
	 */
	private String taskGuid;

	/**
	 * 事项
	 */
	private AuditTask auditTask;
	/**
	 * 事项扩展信息
	 */
	private AuditTaskExtension auditTaskExtension;

	/**
	 * 下拉列表：文书
	 */
	private List<SelectItem> docModel;
	/**
	 * 复制的guid
	 */
	private String copyTaskGuid;
	/**
	 * 事项id
	 */
	private String taskId;
	/**
	 * 当前版本guid
	 */
	private String currentTaskGuid;
	/**
	 * 对比版本guid
	 */
	private String compareTaskGuid;
	/**
	 * 代码项service
	 */
	@Autowired
	private ICodeItemsService codeItemsService;
	/**
	 * 操作
	 */
	private String operation;

	@Autowired
    private ICertConfigExternal certConfigExternalImpl;

	@Autowired
	private IHandleTask handleTaskService;

	@Autowired
	private IAuditTask auditTaskServie;
	
	@Autowired
	private IAuditTaskResult auditResultImpl ;

	@Autowired
	private IAuditTaskExtension auditTaskExtensionImpl;
	
	@Autowired
    private IHandleConfig handleConfigService;

	@Override
	public void pageLoad() {
		// 获取操作
		operation = this.getRequestParameter("operation");
		// 从viewdata里面获取当前版本和比较的版本事项guid
		currentTaskGuid = this.getViewData("currentTaskGuid");
		if (StringUtil.isBlank(currentTaskGuid)) {
			// 第一次加载的时候从页面上获取
			currentTaskGuid = this.getRequestParameter("currentGuid");
		}
		compareTaskGuid = this.getViewData("compareTaskGuid");
		if (StringUtil.isBlank(compareTaskGuid)) {
			// 第一次加载的时候从页面上获取
			compareTaskGuid = this.getRequestParameter("compTaskGuid");
		}
		// 获取taskguid
		taskGuid = this.getRequestParameter("taskGuid");
		copyTaskGuid = this.getRequestParameter("copyTaskGuid");
		taskId = this.getRequestParameter("taskId");
		Integer resulyType = ZwfwConstant.LHSP_RESULTTYPE_NULL;
		String compJson = "";
		// 如果结果配置为空，则进行初始化，插入一条新的
		// 增加匹配条件，如果是审核情况，则也读取现有数据
		if (StringUtil.isNotBlank(copyTaskGuid)) {
			dataBean = auditResultImpl.getAuditResultByTaskGuid(copyTaskGuid, false).getResult();
			// 获取到结果配置实体
			if (dataBean == null) {
				// 初始化结果实体
				dataBean = new AuditTaskResult();
				dataBean.setRowguid(UUID.randomUUID().toString());
				dataBean.setOperatedate(new Date());
				dataBean.setResulttype(ZwfwConstant.LHSP_RESULTTYPE_NULL);
				dataBean.setIs_print(ZwfwConstant.CONSTANT_INT_ZERO);
				dataBean.setOperateusername(userSession.getDisplayName());
				dataBean.setTaskguid(copyTaskGuid);
				addAuditTaskResult(dataBean);
			}
			resulyType = dataBean.getResulttype();
			auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
			auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
		}
		// taskGuid为空则是查看页面，guid直接从页面获取
		else if (StringUtil.isNotBlank(taskGuid)) {
			dataBean = auditResultImpl.getAuditResultByTaskGuid(taskGuid, false).getResult();
			// 获取到结果配置实体
			if (dataBean == null) {
				// 初始化结果实体
				dataBean = new AuditTaskResult();
				dataBean.setRowguid(UUID.randomUUID().toString());
				dataBean.setOperatedate(new Date());
				dataBean.setResulttype(ZwfwConstant.LHSP_RESULTTYPE_NULL);
				dataBean.setIs_print(ZwfwConstant.CONSTANT_INT_ZERO);
				dataBean.setOperateusername(userSession.getDisplayName());
				dataBean.setTaskguid(taskGuid);
				addAuditTaskResult(dataBean);
			}
			resulyType = dataBean.getResulttype();
			auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
			auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid,false).getResult();
		} else if (StringUtil.isNotBlank(currentTaskGuid)) {
			dataBean = auditResultImpl.getAuditResultByTaskGuid(currentTaskGuid, false).getResult();
		}
		Integer compareResultType = null;
		if (dataBean != null && StringUtil.isNotBlank(compareTaskGuid)) {
			AuditTaskResult compareAuditResult = auditResultImpl.getAuditResultByTaskGuid(compareTaskGuid, false)
					.getResult();
			if (compareAuditResult == null) {
				compareAuditResult = new AuditTaskResult();
				compareResultType = 0;
			} else {
				compareResultType = compareAuditResult.getResulttype();
			}
			String areacode="";
	        // 如果是镇村接件
	        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
	            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
	        }else{
	            areacode = ZwfwUserSession.getInstance().getAreaCode();
	        }
			Map<String, Object> mapResult = compareUtil.compare2Bean(dataBean, compareAuditResult);
			Map<String, Object> mapAll = new HashMap<String, Object>(16);
			for (Map.Entry<String, Object> map : mapResult.entrySet()) {
				// 对代码项单独处理
				if (map.getKey().equalsIgnoreCase("resulttype")) {
					mapAll.put(map.getKey(),
							codeItemsService.getItemTextByCodeName("审批结果类型", (String.valueOf(map.getValue()))));
				} else if (map.getKey().equalsIgnoreCase("is_print")) {
					mapAll.put(map.getKey(),
							codeItemsService.getItemTextByCodeName("是否", (String.valueOf(map.getValue()))));
				} else if (map.getKey().equalsIgnoreCase("sharematerialguid")) {
				    CertCatalog  certCatalog = certConfigExternalImpl.getCatalogByCatalogid(String.valueOf(map.getValue()) , areacode);
					String materialname = "";
					if (certCatalog != null) {
						// 返回共享材料名字
						materialname = certCatalog.getCertname();
					}
					mapAll.put(map.getKey(), materialname);
				} else {
					mapAll.put(map.getKey(), map.getValue());
				}
			}
			compJson = JsonUtil.objectToJson(mapAll);
		}
		this.addCallbackParam("compareResultType", compareResultType);
		this.addCallbackParam("compJson", compJson);
		this.addCallbackParam("resulttype", resulyType);
		// 共享材料标识
		String areacode="";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
		String sharematerialguid = dataBean == null ? "" : dataBean.getSharematerialguid();
		if (StringUtil.isNotBlank(sharematerialguid)) {
		    CertCatalog  certCatalog = certConfigExternalImpl.getCatalogByCatalogid(sharematerialguid , areacode);
			if (certCatalog != null) {
				// 返回共享材料名字
				this.addCallbackParam("materialname", certCatalog.getCertname());
			} else {
				// 返回共享材料名字
				this.addCallbackParam("materialname", "");
			}
		}
		
	    AuditTask Task = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
    	if(StringUtil.isNotBlank(Task)){
    		String unid=Task.get("unid");
    		//system.out.println(unid);
    		auditTask.set("unid", Task.get("unid"));
    	}
	}

	/**
	 * 点击下一步操作
	 * 
	 */
	public void add() {
		// 保存数据
		if (StringUtil.isNotBlank(copyTaskGuid)) {
			if ("windowCopy".equals(operation) || operation.contains("copy")) {
				updateAuditResult(dataBean);
			}
			// 需要版本对比的，就把新版本数据存入数据库
			else {
				updateAuditResult(dataBean);
				updateAuditTask(auditTask);
			}
		} else {
			// 事项直接更新到数据库
			updateAuditResult(dataBean);
		}
		this.addCallbackParam("msg", "保存成功！");
	}

	/**
	 * 
	 * 把当前的guid和比较的guid存入view
	 * 
	 * @param currentTaskGuid
	 * @param compareTaskGuid
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void addTaskGuidToView(String currentTaskGuid, String compareTaskGuid) {
		this.addViewData("currentTaskGuid", currentTaskGuid);
		this.addViewData("compareTaskGuid", compareTaskGuid);
	}

	/**
	 * 
	 * 点击提交按钮触发
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void submit() {
		String msg = "";
		String syncTaskGuid = auditTask.getRowguid();
		AuditCommonResult<String> result;
		updateAuditResult(dataBean);
		if (StringUtil.isNotBlank(copyTaskGuid) && !"copy".equals(operation) || "confirmEdit".equals(operation)) {
			if ("confirmEdit".equals(operation)) {
				result = handleTaskService.submitTask(true, taskGuid, auditTask, auditTaskExtension,
						userSession.getDisplayName(), userSession.getUserGuid());
			} else {
				boolean taskversion =true;
				String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
				if(ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)){
					taskversion=false;
				}
				int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
				boolean flag = taskversion &&( isNeedNewVersion == 1 || auditTaskServie
						.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG), taskGuid,
								auditTask.getRowguid(), auditTask, auditTaskExtension)
						.getResult());
				
				result = handleTaskService.submitTask(flag, taskGuid, auditTask, auditTaskExtension,
						userSession.getDisplayName(), userSession.getUserGuid());
				if (!flag) {
					syncTaskGuid = taskGuid;
				}
			}
		} else {
			updateAuditResult(dataBean);
			result = handleTaskService.submitNewTask(auditTask, auditTaskExtension, operation,
					ZwfwUserSession.getInstance().getCenterGuid());
		}
		if (!result.isSystemCode()) {
			msg = "处理失败！";
		} else {
			msg = result.getResult();
			syncWindowTask("modify", syncTaskGuid);
		}
		addCallbackParam("msg", msg);
	}

	/**
	 * 
	 * 审核通过操作
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void passAudit() {
		String msg = "";
		// 调用存储过程审核通过
		updateAuditResult(dataBean);
		AuditCommonResult<String> result;
		String syncTaskGuid = auditTask.getRowguid();
		int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
		boolean flag = isNeedNewVersion == 1
				|| auditTaskServie.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
						taskGuid, auditTask.getRowguid(), auditTask, auditTaskExtension).getResult();
		if (!flag) {
			syncTaskGuid = taskGuid;
		}
		if (StringUtil.isNotBlank(auditTask.getVersion())) {
			result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension,
					userSession.getDisplayName(), userSession.getUserGuid());
		} else {
			result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension,
					userSession.getDisplayName(), userSession.getUserGuid());
		}
		if (!result.isSystemCode()) {
			msg = "处理失败！";
		} else {
			msg = result.getResult();
			syncWindowTask("modify", syncTaskGuid);
		}
		this.addCallbackParam("msg", msg);
	}

	/**
	 * 
	 * 审核不通过操作
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void notPassAudit() {
		String msg = "";
		// 调用存储过程审核通过
		msg = handleTaskService.notpassAuditTask(auditTask.getRowguid()).getResult();
		this.addCallbackParam("msg", msg);
	}

	/**
	 * 点击上报按钮触发
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void report() {
		String msg = "";
		// 判断事项编码是否正确
		if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
			Map<Object, Object> map = handleTaskService
					.judgeItemId(auditTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
							ZwfwUserSession.getInstance().getCenterGuid())
					.getResult();
			msg = (String) map.get("msg");
			if (StringUtil.isNotBlank(msg)) {
				this.addCallbackParam("msg", msg);
				return;
			}
		}
		updateAuditResult(dataBean);
		// 上报事项
		msg = handleTaskService.report(auditTask, auditTaskExtension).getResult();
		this.addCallbackParam("msg", msg);
	}

	/**
	 * 保存并新建
	 * 
	 */
	public void addNew() {
		add();
		dataBean = new AuditTaskResult();
	}

	/**
	 * 
	 * 同步事项到窗口
	 * 
	 * @param SendType
	 *            事项消息发送类型 消息类型有enable、insert、modify、delete
	 * @param RabbitMQMsg
	 *            事项id
	 */
	public void syncWindowTask(String SendType, String taskGuid) {
		// TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
		// 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
		try {
			String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" + taskGuid + "#" + "handleCenterTask:" + SendType
					+ ";" + taskGuid + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
			ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AuditTaskResult getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditTaskResult();
		}
		return dataBean;
	}

	public void setDataBean(AuditTaskResult dataBean) {
		this.dataBean = dataBean;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getBusinessguid() {
		return businessguid;
	}

	public void setBusinessguid(String businessguid) {
		this.businessguid = businessguid;
	}

	public String getSharematirialguid() {
		return sharematirialguid;
	}

	public void setSharematirialguid(String sharematirialguid) {
		this.sharematirialguid = sharematirialguid;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getResulttypeModel() {
		if (resulttypeModel == null) {
			resulttypeModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批结果类型", null, false));
		}
		return this.resulttypeModel;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getDocModel() {
		if (docModel == null) {
			docModel = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "文书类型", null, false));
		}
		return this.docModel;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getIsprintModal() {
		if (isprintModal == null) {
			isprintModal = DataUtil.convertMap2ComboBox(
					(List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
		}
		return this.isprintModal;
	}

	public String addAuditTaskResult(AuditTaskResult dataBean) {
		String msg = "";
		AuditCommonResult<String> addResult = auditResultImpl.addAuditResult(dataBean);
		if (!addResult.isSystemCode()) {
			msg = addResult.getSystemDescription();
		} else if (!addResult.isBusinessCode()) {
			msg = addResult.getBusinessDescription();
		} else {
			msg = "保存成功！";
		}
		return msg;
	}

	public String updateAuditResult(AuditTaskResult dataBean) {
		String msg = "";
		AuditCommonResult<String> result = auditResultImpl.updateAuditResult(dataBean);
		if (!result.isSystemCode()) {
			msg = result.getSystemDescription();
		} else if (!result.isBusinessCode()) {
			msg = result.getBusinessDescription();
		} else {
			msg = "修改成功！";
		}
		return msg;
	}
	
	/**
     * 
     * 修改
     * 
     * @param dataBean
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String updateAuditTask(AuditTask dataBean) {
        String msg = "";
        dataBean.setOperatedate(new Date());
        AuditCommonResult<String> editResult = auditTaskServie.updateAuditTask(dataBean);
        // String m=editResult.getResult();
        if (!editResult.isSystemCode()) {
            msg = editResult.getSystemDescription();
        }

        else if (!editResult.isBusinessCode()) {
            msg = editResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
    }
}
