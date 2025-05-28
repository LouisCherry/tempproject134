package com.epoint.audittask.baisc.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.hottask.inter.IAuditTaskHottask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;

/**
 * 
 * 事项基本信息action
 * 
 * @author Administrator
 * @version [版本号, 2016年10月8日]
 
 
 */
@RestController("jnaudittaskbasiceditaction")
@Scope("request")
public class JNAuditTaskBasicEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -2215014088842095485L;
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;
    
    
    @Autowired
    private IAuditTask auditTaskBasicImpl;
    
    @Autowired
    private IAuditTaskHottask iAuditTaskHottask;
    @Autowired
    private ISendMQMessage sendMQMessageService;

    /**
     * 事项
     */
    private AuditTask dataBeanTask;
    /**
     * 事项guid
     */
    private String taskGuid;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension dataBeanTaskExtension;
    /**
     * 事项类型代码项
     */
    private List<SelectItem> auditTaskTypeModal;
    /**
     * 申请人类型代码项
     */
    private List<SelectItem> applyertypeModal;
    /**
     * 是否代码项
     */
    private List<SelectItem> yesornoModal;
    /**
     * 网厅电子表单
     */
    private List<SelectItem> projectFormModel;
    /**
     * 审批类别代码项
     */
    private List<SelectItem> shenpilbModel;
    
    /**
     * 服务方式
     */
    private List<SelectItem> fwfsModel;
    
    /**
     * 服务类别
     */
    private List<SelectItem> fwlyModel;
    
    /**
     * 即办件流转模式
     */
    private List<SelectItem> jbjModel;
    /**
     * 是否允许上报
     */
    private List<SelectItem> sbjModel;
    /**
     * 部门service
     */
    @Autowired
    private IOuService frameOuService9;
    /**
     * 事项id
     */
    private String taskId;
    /**
     * 操作
     */
    private String operation;
    /**
     * 复制事项guid
     */
    private String copyTaskGuid;
    
    @Autowired
    private IHandleTask handleTaskService;

    @Autowired
    private IHandleConfig handleConfigService;
    
    @Override
    public void pageLoad() {
        // 获取taskguid
        taskGuid = this.getRequestParameter("taskGuid");
        taskId = this.getRequestParameter("taskId");
        operation = this.getRequestParameter("operation");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        String taskoutInfo = "";
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            dataBeanTask = auditTaskBasicImpl.getAuditTaskByGuid(copyTaskGuid, false).getResult();
            dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
                    //auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(copyTaskGuid, false);
            AuditTask Task = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
        	if(StringUtil.isNotBlank(Task)){
        		String unid=Task.get("unid");
        		//system.out.println(unid);
        		dataBeanTask.set("unid", Task.get("unid"));
        	}
        
        }
        else if (StringUtil.isNotBlank(taskGuid)) {
            dataBeanTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
            dataBeanTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid,false).getResult();
        }
        if(ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())){
            addCallbackParam("xz", "true");
        }
        
        if (StringUtil.isNotBlank(taskGuid)) {
            // 编辑页面部门显示
            this.addCallbackParam("ouname", frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
            this.addCallbackParam("ouguid", dataBeanTask.getOuguid());
            this.addCallbackParam("taskoutInfo", taskoutInfo);
            this.addCallbackParam("is_editafterimport", dataBeanTask.getIs_editafterimport());
            this.addCallbackParam("tasksource", dataBeanTask.getTasksource());
        }
    }

    /**
     * 编辑页面点击下一步的操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void update() {
    	  String msg = "";
    	  // 判断事项编码是否正确
          if ((StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) && Integer.valueOf(ZwfwConstant.TASKAUDIT_STATUS_DQR) != dataBeanTask.getIs_editafterimport()) {
              Map<Object, Object> map = handleTaskService.judgeItemId(dataBeanTask.getItem_id(),
                      StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
              msg = (String) map.get("msg");
              if (StringUtil.isNotBlank(msg)) {
                  this.addCallbackParam("msg", msg);
                  return;
              }
          }
          msg = "保存成功！";
          dataBeanTask.setOperatedate(new Date());
          
          dataBeanTask.setOperateusername(userSession.getDisplayName());
          dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
          setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
          dataBeanTaskExtension.setOperatedate(new Date());
          dataBeanTaskExtension.setOperateusername(userSession.getDisplayName());
          //窗口点击下一步状态变成待上报
          if (operation.contains("window")
                  && !ZwfwConstant.TASKAUDIT_STATUS_SHWTG.equals(String.valueOf(dataBeanTask.getIs_editafterimport()))) {
              dataBeanTask.setIs_editafterimport(Integer.parseInt(ZwfwConstant.TASKAUDIT_STATUS_DSB));
          }
          if (StringUtil.isNotBlank(copyTaskGuid)) {
              //窗口事项复制和窗口事项变更点击下一步直接对数据库操作
              if (operation.equals("windowCopy") || operation.contains("copy")) {
                  dataBeanTaskExtension.setTaskadduserdisplayname(userSession.getDisplayName());
                  dataBeanTaskExtension.setTaskadduserguid(userSession.getUserGuid());
                  auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension,false);
              }
              //需要版本对比的，就把新版本数据存入数据库
              else {
                  // 点击下一步先只对数据库进行操作，更新复制过来的事项
                  auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension,false);
              }
          }
          else {
              //事项直接更新到数据库
              auditTaskExtensionImpl.updateAuditTaskAndExt(dataBeanTask, dataBeanTaskExtension,false);
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
		try {
			EpointFrameDsManager.begin(null);
			dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
			String msg = "";
			setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
			AuditCommonResult<String> result;
			String syncTaskGuid = dataBeanTask.getRowguid();
			int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0 : dataBeanTask.getIsneednewversion();
			boolean flag = isNeedNewVersion == 1
					|| auditTaskBasicImpl
							.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
									taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension)
							.getResult();
			if (!flag) {
				syncTaskGuid = taskGuid;
			}
			if (StringUtil.isNotBlank(dataBeanTask.getVersion())) {
				result = handleTaskService.passTask(flag, taskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
						userSession.getDisplayName(), userSession.getUserGuid());
			} else {
				result = handleTaskService.passTask(flag, taskGuid, taskId, dataBeanTask, dataBeanTaskExtension,
						userSession.getDisplayName(), userSession.getUserGuid());
			}
			EpointFrameDsManager.commit();
			if (!result.isSystemCode()) {
				msg = "处理失败！";
			} else {
				msg = result.getResult();			
				syncWindowTask("modify", syncTaskGuid);
			}
			this.addCallbackParam("msg", msg);
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			e.printStackTrace();
		}
	}

    /**
     * 
     * 审核不通过操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void notPassAudit() {
        dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
        String msg = "";
        setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
        // 调用存储过程审核通过
        msg = handleTaskService.notpassAuditTask(dataBeanTask.getRowguid()).getResult();
        this.addCallbackParam("msg", msg);
    }

    /**
     * 点击上报按钮触发，使事项的状态变成待审核
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void report() {
    	String msg = "";
        // 判断事项编码是否正确
    	if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            Map<Object, Object> map = handleTaskService.judgeItemId(dataBeanTask.getItem_id(),
                    StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                this.addCallbackParam("msg", msg);
                return;
            }
        }
        //更新事项状态
        dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
        setChargeAndZjMode(dataBeanTask, dataBeanTaskExtension);
        msg = handleTaskService.report(dataBeanTask, dataBeanTaskExtension).getResult();
        this.addCallbackParam("msg", msg);
    }

	/**
	 * 
	 * 点击提交按钮触发
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void submit() {
		try {
			EpointFrameDsManager.begin(null);
			dataBeanTask.setOuname(frameOuService9.getOuByOuGuid(dataBeanTask.getOuguid()).getOuname());
			String msg = "";
			String syncTaskGuid = dataBeanTask.getRowguid();
			AuditCommonResult<String> result;
			if (StringUtil.isNotBlank(copyTaskGuid) && !operation.equals("copy") || operation.equals("confirmEdit")) {
				if (operation.equals("confirmEdit")) {
					result = handleTaskService.submitTask(true, taskGuid, dataBeanTask, dataBeanTaskExtension,
							userSession.getDisplayName(), userSession.getUserGuid());
				} else {
					boolean taskversion =true;
					String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
					if(ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)){
						taskversion=false;
					}
					int isNeedNewVersion = dataBeanTask.getIsneednewversion() == null ? 0
							: dataBeanTask.getIsneednewversion();
					boolean flag = taskversion &&(isNeedNewVersion == 1 || auditTaskBasicImpl
							.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
									taskGuid, dataBeanTask.getRowguid(), dataBeanTask, dataBeanTaskExtension)
							.getResult());
					
					result = handleTaskService.submitTask(flag, taskGuid, dataBeanTask, dataBeanTaskExtension,
							userSession.getDisplayName(), userSession.getUserGuid());
					updateHotTask(dataBeanTask);
					if (!flag) {
						syncTaskGuid = taskGuid;
					}
				}
			}
			// 新增操作的时候第一次生成事项
			else {
				result = handleTaskService.submitNewTask(dataBeanTask, dataBeanTaskExtension, operation,
						ZwfwUserSession.getInstance().getCenterGuid());
			}
			EpointFrameDsManager.commit();
			if (!result.isSystemCode()) {
				msg = "处理失败！";
			} else {
				msg = result.getResult();			
				syncWindowTask("modify", syncTaskGuid);
			}
			addCallbackParam("msg", msg);
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			e.printStackTrace();
		}
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
/*			String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" + taskGuid
					+ "#" + "handleCenterTask:" + SendType + ";" + taskGuid + 
					"/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
			ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);*/
	        String RabbitMQMsg = SendType + ";" + taskGuid ;
	        sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);

        }
        catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  如果该事项已经加入到热门事项，则将对应的热门事项更新
	 *  @param auditTask    
	 */
	public void updateHotTask(AuditTask auditTask) {
	    AuditTaskHottask auditTaskHottask = iAuditTaskHottask.getDetailbyTaskID(auditTask.getTask_id()).getResult();
	    if(auditTaskHottask != null) {
	        auditTaskHottask.setTaskname(auditTask.getTaskname());
	        auditTaskHottask.setOuname(auditTask.getOuname());
	        auditTaskHottask.setEnable(auditTask.getIs_enable());
	        auditTaskHottask.setApplyertype(auditTask.getApplyertype());
	        auditTaskHottask.setTaskguid(auditTask.getRowguid());
	        iAuditTaskHottask.updateHottak(auditTaskHottask, auditTaskHottask.getRowguid());
	    }
	}
	
    @SuppressWarnings("unchecked")
    public List<SelectItem> getAuditTaskTypeModal() {
        if (auditTaskTypeModal == null) {
            auditTaskTypeModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "事项类型", null, false));
        }
        return this.auditTaskTypeModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getYesornoModal() {
        if (yesornoModal == null) {
            yesornoModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.yesornoModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getApplyertypeModal() {
        if (applyertypeModal == null) {
            applyertypeModal = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "申请人类型", null, false));
        }
        return this.applyertypeModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getShenpilbModel() {
        if (shenpilbModel == null) {
            shenpilbModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "审批类别", null, false));
        }
        return this.shenpilbModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getProjectFormModel() {
        if (projectFormModel == null) {
            projectFormModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "网厅电子表单", null, false));
        }
        return this.projectFormModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getfwfsModel() {
        if (fwfsModel == null) {
            fwfsModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "服务方式", null, false));
        }
        return this.fwfsModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getfwlyModel() {
        if (fwlyModel == null) {
            fwlyModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "服务领域", null, false));
        }
        return this.fwlyModel;
    }
    
    
    public List<SelectItem> getjbjmodeModal() {
        if (jbjModel == null) {
        	jbjModel = new ArrayList<>();
        	jbjModel.add(new SelectItem(ZwfwConstant.JBJMODE_SIMPLE,"简易模式（不启动流程）"));
        	jbjModel.add(new SelectItem(ZwfwConstant.JBJMODE_STANDARD,"正常模式"));
        }
        return this.jbjModel;
    }
    public List<SelectItem> getsbjmodeModal() {
        if (sbjModel == null) {
            sbjModel = new ArrayList<>();
            sbjModel.add(new SelectItem(ZwfwConstant.CONSTANT_INT_ZERO, "否"));
            sbjModel.add(new SelectItem(ZwfwConstant.CONSTANT_INT_ONE, "是"));
        }
        return this.sbjModel;
    }
    /**
     * 如果收费为否，那么何时收费等就为空，如果是否自建为否，那么自建模式为空
     * 
     * @param auditTask
     *            事项
     * @param auditTaskExtension
     *            事项扩展信息
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setChargeAndZjMode(AuditTask auditTask, AuditTaskExtension auditTaskExtension) {
        // 是否收费：否
        if (auditTask.getCharge_flag() == null || ZwfwConstant.CONSTANT_INT_ZERO == auditTask.getCharge_flag()) {
            auditTaskExtension.setIs_feishui(null); // 是否非税
            auditTaskExtension.setCharge_when(null);// 何时收费：受理前
            auditTask.setCharge_standard(null);// 收费标准
            auditTask.setCharge_basis(null); // 收费依据
            auditTask.setFeeaccountguid(null);// 收费账号
        }
        // 是否自建模式
        if (auditTaskExtension.getIszijianxitong() == null || ZwfwConstant.CONSTANT_INT_ZERO == auditTaskExtension.getIszijianxitong()) {
            auditTaskExtension.setZijian_mode(null);
        }
    }

    public void setAuditTaskTypeModal(List<SelectItem> auditTaskTypeModal) {
        this.auditTaskTypeModal = auditTaskTypeModal;
    }

    public void setApplyertypeModal(List<SelectItem> applyertypeModal) {
        this.applyertypeModal = applyertypeModal;
    }

    public AuditTask getDataBeanTask() {
        return dataBeanTask;
    }

    public void setDataBeanTask(AuditTask dataBeanTask) {
        this.dataBeanTask = dataBeanTask;
    }

    public AuditTaskExtension getDataBeanTaskExtension() {
        return dataBeanTaskExtension;
    }

    public void setDataBeanTaskExtension(AuditTaskExtension dataBeanTaskExtension) {
        this.dataBeanTaskExtension = dataBeanTaskExtension;
    }

}
