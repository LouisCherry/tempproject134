package com.epoint.znsb.composite.auditqueue.handlequeue.service;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueinstance.domain.AuditQueueInstance;
import com.epoint.basic.auditqueue.auditqueueinstance.inter.IAuditQueueInstance;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueueturn.domain.AuditQueueTurn;
import com.epoint.basic.auditqueue.auditqueueturn.inter.IAuditQueueTurn;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueuewindowextendinfo.inter.IAuditQueueWindowExtendinfo;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlecommon.inter.IHandleCommon;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.znsb.composite.auditqueue.handlecommon.inter.JxIHandleCommon;

public class JxHandleQueueService {

	IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
	IAuditOrgaWindowYjs auditWindowservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);
	IAuditQueueUserinfo userinfoservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueUserinfo.class);
	JxIHandleCommon handlecommonseervice = ContainerFactory.getContainInfo().getComponent(JxIHandleCommon.class);
	IAuditQueueWindowTasktype windowtasktypeservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueWindowTasktype.class);
	IMessagesCenterService imessageservice = ContainerFactory.getContainInfo()
			.getComponent(IMessagesCenterService.class);
	IAuditQueueAppointment appointmentservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueAppointment.class);
	IAuditQueueTasktype tasktypeservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueTasktype.class);
	IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);
	IAuditQueueWindowExtendinfo windowextendinfoservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueWindowExtendinfo.class);
	IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
	IAuditQueueInstance queueinstanceservice = ContainerFactory.getContainInfo()
			.getComponent(IAuditQueueInstance.class);
	IHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(IHandleCommon.class);
	IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
	IHandleFlowSn flowsnservice = ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
	IAuditQueueTurn queueTurnService = ContainerFactory.getContainInfo().getComponent(IAuditQueueTurn.class);
	IAuditOrgaWindowYjs iwindow = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);

	Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public Map<String, String> getQNO(String sfz, String phone, String taskguid, String centerguid, String hallguid) {
		String QNO = "";
		String Handlewindowno = "";
		String flowsnno = "";
		String QueueGuid = "";
		String queueweight = "";
		String queuevalue = "";
		String handlewindows = "";
		String QNOPre = "";
		String handlehallguid = "";
		String messagecontent = "";
		String AS_Queue_Mode = "";
		String[] strHandlewindowno;
		boolean whiteOpenFlag = false;
		boolean isWhiteUser = false;
		ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
		Map<String, String> records = new HashMap<String, String>(16);
		try {
			AS_Queue_Mode = configservice.getFrameConfigValue("AS_Queue_Mode");
			AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(taskguid).getResult();
			if (StringUtil.isBlank(tasktype)) {
				records.put("msg", "该取号事项不存在！");
				return records;
			}
			// 更新身份证对应手机号
			if (StringUtil.isNotBlank(sfz) && StringUtil.isNotBlank(phone)) {
				userinfoservice.updateMobileBySFZ(sfz, phone);
			}
			AuditQueue auditQueue = new AuditQueue();
			flowsnno = EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd")
					+ QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn("QueueFlowSN")), 6, '0');

			QueueGuid = UUID.randomUUID().toString();
			auditQueue.setRowguid(QueueGuid);
			auditQueue.setIdentitycardnum(sfz);
			auditQueue.setOperatedate(new Date());
			auditQueue.setPhone(phone);
			auditQueue.setTaskguid(taskguid);
			auditQueue.setFlowno(flowsnno);
			List<String> windowguids = windowtasktypeservice.getWindowListByTaskTypeGuid(taskguid).getResult();
			if (windowguids != null && windowguids.size() > 0) {
				auditQueue.setHandlewindowguid("");
				// 判断事项是否已配置窗口
				AuditOrgaWindow auditwindow = null;
				for (String para : windowguids) {
					auditwindow = auditWindowservice.getWindowByWindowGuid(para).getResult();
					if (auditwindow != null && StringUtil.isNotBlank(auditwindow.getIs_usequeue())) {
						if (QueueConstant.Common_yes_String.equals(auditwindow.getIs_usequeue().toString())) {
							Handlewindowno += auditwindow.getWindowno() + ";";
							handlewindows += para + ";";
							QNOPre = auditwindow.getWindowbh();
							if (StringUtil.isBlank(handlehallguid)) {
								handlehallguid = auditwindow.getLobbytype();
							}
						}
					} else {
						// 将不存在的窗口自动删除
						windowtasktypeservice.deletebyWindowguid(para);
					}
				}

				if (StringUtil.isBlank(Handlewindowno)) {
					records.put("msg", "该事项并未正确配置到窗口！");
					return records;
				}

				// 窗口号排序
				strHandlewindowno = Handlewindowno.split(";");
				Arrays.sort(strHandlewindowno);
				Handlewindowno = "";// 置空重新加载
				for (String str : strHandlewindowno) {
					Handlewindowno += str + ";";
				}

				if (StringUtil.isNotBlank(tasktype.getPre())) {
					QNOPre = tasktype.getPre();
				}
				// 查询中心系统参数中有没有配置开启白名单功能
				String isUseWhiteList = configservice.getFrameConfigValue("AS_IS_USE_WHITELIST");
				whiteOpenFlag = StringUtil.isNotBlank(isUseWhiteList)&&QueueConstant.CONSTANT_STR_ONE.equals(isUseWhiteList);
				// 判断当前用户是不是白名单用户
				AuditQueueUserinfo userinfo = userinfoservice.getUserinfo(sfz).getResult();
				if(StringUtil.isNotBlank(userinfo)&&QueueConstant.CONSTANT_STR_ONE.equals(userinfo.getIs_vip())) {
				    isWhiteUser = true;
				}
				// 两者同时满足
				if(whiteOpenFlag&&isWhiteUser) {
				    // 白名单取出的小票前缀固定为VIP
				    QNOPre = "VIP";
				}
				QNO = QNOPre + QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn(centerguid + QNOPre)), 3, '0');
			} else {
				records.put("msg", "该事项并未正确配置到窗口！");
				return records;
			}

			auditQueue.setHandlewindowno(Handlewindowno);
			auditQueue.setQno(QNO);
			auditQueue.setStatus(QueueConstant.Qno_Status_Init);
			auditQueue.setGetnotime(new Date());
			
			// 如果白名单开启,并且用户是白名单设置类型为vip号
			if(whiteOpenFlag&&isWhiteUser) {
			    auditQueue.setApplyusertype(QueueConstant.Qno_Type_VIP);
            }else {
                auditQueue.setApplyusertype(QueueConstant.Qno_Type_Common);// 1代表普通号2代表预约号3代表VIP号
            }
			
			auditQueue.setAppointfromtime(EpointDateUtil.convertString2Date("9999-12-31", "yyyy-MM-dd"));
			auditQueue.setCenterguid(centerguid);
			auditQueue.setHallguid(handlehallguid);
			queueweight = tasktype.getQueueweight();
			queuevalue = tasktype.getQueuevalue();

			// 判断是否使用分表模式
			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				if (StringUtil.isBlank(queuevalue)) {
					records.put("msg", "该事项并未配置到相关队列！");
					return records;
				}
			}
			if (StringUtil.isBlank(queueweight)) {
				queueweight = "0";
			}

			auditQueue.setQueueweight(queueweight);
			queueservice.addQueue(auditQueue);

			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				AuditQueueInstance queueinstance = new AuditQueueInstance();
				queueinstance.setRowguid(UUID.randomUUID().toString());
				queueinstance.setQno(QNO);
				queueinstance.setTaskguid(taskguid);
				queueinstance.setQueueguid(QueueGuid);
				queueinstance.setQueueweight(queueweight);
				queueinstance.setGetnotime(new Date());
				queueinstance.setAppointfromtime(EpointDateUtil.convertString2Date("9999-12-31", "yyyy-MM-dd"));
				queueinstance.setQueuevalue(queuevalue);
				queueinstance.setHandlewindows(handlewindows);

				
				// 如果白名单开启,并且用户是白名单设置类型为vip号
	            if(whiteOpenFlag&&isWhiteUser) {
	                queueinstance.setApplyusertype(QueueConstant.Qno_Type_VIP);
	            }else {
	                queueinstance.setApplyusertype(QueueConstant.Qno_Type_Common);;// 1代表普通号2代表预约号3代表VIP号
	            }
				
				queueinstanceservice.insert(queueinstance);
			}

			// redis 计数+1
			for (String para : windowguids) {
				redis.hincrBy("AuditQueueOrgaWindow_" + para, "waitnum", 1);
			}

			if (StringUtil.isNotBlank(phone)) {
				// 如果值为0，则不发短信
				messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_QH", centerguid).getResult();
				if (StringUtil.isNotBlank(messagecontent)) {
					if (!QueueConstant.Common_no_String.equals(messagecontent)) {
						messagecontent = messagecontent.replace("[#=WaitNum#]",
								String.valueOf(getTaskWaitNum(taskguid, true)));
						messagecontent = messagecontent.replace("[#=HandleWindowNo#]", Handlewindowno);
					} else {
						messagecontent = "";
					}
				} else {
					messagecontent = "您所办理事项的当前等待人数" + getTaskWaitNum(taskguid, true) + "人,办理窗口为" + Handlewindowno
							+ "。请耐心等待，轮到您时会有短信提醒，也可留意大屏上的显示内容。";
				}
				if (StringUtil.isNotBlank(messagecontent)) {
					imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0,
							new Date(), phone, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
				}
			}

			records.put("msg", "success");
			records.put("qno", QNO);

		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			records.put("msg", "取号异常");
		} finally {
			redis.close();
		}
		return records;
	}

	public Map<String, String> getTurnTaskQNO(String auditqueueguid, String taskguid, String handleuserguid,
			String centerguid) {

		String Handlewindowno = "";
		String queueweight = "";
		String queuevalue = "";
		String handlewindows = "";
		String handlehallguid = "";
		String messagecontent = "";
		String AS_Queue_Mode = "";
		String[] strHandlewindowno;
		String AS_Queue_TurnPriority = "";
		String QueueGuid = "";
		String QNO = "";
		String phone = "";
		Date getnotime;
		String applyusertype="";
		Date appointfromtime;
		
		AuditQueue auditQueue = queueservice.getQueueByGuid("Rowguid,Qno,Phone,Getnotime,Applyusertype,Appointfromtime,Taskguid",auditqueueguid).getResult();
		QueueGuid = auditQueue.getRowguid();
		QNO = auditQueue.getQno();
		phone = auditQueue.getPhone();
		getnotime = auditQueue.getGetnotime();
		applyusertype=auditQueue.getApplyusertype();
		appointfromtime=auditQueue.getAppointfromtime();
		
		AuditQueueTurn queueTurn = new AuditQueueTurn();
		queueTurn.setRowguid(UUID.randomUUID().toString());
		queueTurn.setQueueguid(auditqueueguid);
		queueTurn.setTurnDate(new Date());
		queueTurn.setPreviousTaskguid(auditQueue.getTaskguid());
		queueTurn.setCurrentTaskguid(taskguid);
		queueTurn.setTurnQnotype(QueueConstant.QUEUE_TURN_TASK);
		queueTurn.setHandleUserguid(handleuserguid);
		queueTurnService.insertQueueturn(queueTurn);

		AS_Queue_TurnPriority = handleConfigservice.getFrameConfig("AS_Queue_TurnPriority", centerguid).getResult();
		

		ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
		Map<String, String> records = new HashMap<String, String>(16);
		try {
			AS_Queue_Mode = configservice.getFrameConfigValue("AS_Queue_Mode");
			AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(taskguid).getResult();
			if (StringUtil.isBlank(tasktype)) {
				records.put("msg", "该取号事项不存在！");
				return records;
			}

			List<String> windowguids = windowtasktypeservice.getWindowListByTaskTypeGuid(taskguid).getResult();
			if (windowguids != null && windowguids.size() > 0) {
				// 判断事项是否已配置窗口
				AuditOrgaWindow auditwindow = null;
				for (String para : windowguids) {
					auditwindow = auditWindowservice.getWindowByWindowGuid(para).getResult();
					if (auditwindow != null && StringUtil.isNotBlank(auditwindow.getIs_usequeue())) {
						if (QueueConstant.Common_yes_String.equals(auditwindow.getIs_usequeue().toString())) {
							Handlewindowno += auditwindow.getWindowno() + ";";
							handlewindows += para + ";";
							if (StringUtil.isBlank(handlehallguid)) {
								handlehallguid = auditwindow.getLobbytype();
							}
						}
					} else {
						// 将不存在的窗口自动删除
						windowtasktypeservice.deletebyWindowguid(para);
					}
				}

				if (StringUtil.isBlank(Handlewindowno)) {
					records.put("msg", "该事项并未正确配置到窗口！");
					return records;
				}

				// 窗口号排序
				strHandlewindowno = Handlewindowno.split(";");
				Arrays.sort(strHandlewindowno);
				Handlewindowno = "";// 置空重新加载
				for (String str : strHandlewindowno) {
					Handlewindowno += str + ";";
				}
			} else {
				records.put("msg", "该事项并未正确配置到窗口！");
				return records;
			}

			queueweight = tasktype.getQueueweight();
			queuevalue = tasktype.getQueuevalue();

			// 判断是否使用分表模式
			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				if (StringUtil.isBlank(queuevalue)) {
					records.put("msg", "该事项并未配置到相关队列！");
					return records;
				}
			}
			if (StringUtil.isBlank(queueweight)) {
				queueweight = "0";
			}

			queueservice.updateTurnQnoTask(auditqueueguid, "", Handlewindowno, handlehallguid, taskguid, queueweight);

			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				AuditQueueInstance queueinstance = new AuditQueueInstance();
				queueinstance.setRowguid(UUID.randomUUID().toString());
				queueinstance.setQno(QNO);
				queueinstance.setTaskguid(taskguid);
				queueinstance.setQueueguid(QueueGuid);
				queueinstance.setQueueweight(queueweight);
				// 针对不同的转号参数设置进行处理
				if (QueueConstant.TURN_PRIORITY_LAST.equals(AS_Queue_TurnPriority)) {
					queueinstance.setGetnotime(new Date());					
				} else {
					queueinstance.setGetnotime(getnotime);
				}
				queueinstance.setAppointfromtime(appointfromtime);
				queueinstance.setQueuevalue(queuevalue);
				queueinstance.setHandlewindows(handlewindows);

				queueinstance.setApplyusertype(applyusertype);
				queueinstanceservice.insert(queueinstance);
			}

			// redis 计数+1
			for (String para : windowguids) {
				redis.hincrBy("AuditQueueOrgaWindow_" + para, "waitnum", 1);
			}

			if (StringUtil.isNotBlank(phone)) {
				// 如果值为0，则不发短信
				messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_QH", centerguid).getResult();
				if (StringUtil.isNotBlank(messagecontent)) {
					if (!QueueConstant.Common_no_String.equals(messagecontent)) {
						messagecontent = messagecontent.replace("[#=WaitNum#]",
								String.valueOf(getTaskWaitNum(taskguid, true)));
						messagecontent = messagecontent.replace("[#=HandleWindowNo#]", Handlewindowno);
					} else {
						messagecontent = "";
					}
				} else {
					messagecontent = "您所办理事项的当前等待人数" + getTaskWaitNum(taskguid, true) + "人,办理窗口为" + Handlewindowno
							+ "。请耐心等待，轮到您时会有短信提醒，也可留意大屏上的显示内容。";
				}
				if (StringUtil.isNotBlank(messagecontent)) {
					imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0,
							new Date(), phone, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
				}
			}

			records.put("msg", "success");

		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			records.put("msg", "取号异常");
		} finally {
			redis.close();
		}
		return records;
	}

	public Map<String, String> getTurnWindowQNO(String auditqueueguid, String windowguid, String handleuserguid,
			String centerguid) {

		String handlewindowno = "";
		String queuevalue = "";
		String queueweight = "";
		String handlewindows = "";
		String messagecontent = "";
		String AS_Queue_Mode = "";
		String AS_Queue_TurnPriority = "";
		String taskguid = "";
		String QueueGuid = "";
		String QNO = "";
		String phone = "";
		Date getnotime;
		String applyusertype="";
		Date appointfromtime;
		
		AuditQueue auditQueue = queueservice.getQueueByGuid("Taskguid,Rowguid,Qno,Phone,Getnotime,Applyusertype,Appointfromtime,Handlewindowguid",auditqueueguid).getResult();

		taskguid = auditQueue.getTaskguid();
		QueueGuid = auditQueue.getRowguid();
		QNO = auditQueue.getQno();
		phone = auditQueue.getPhone();
		getnotime = auditQueue.getGetnotime();
		applyusertype=auditQueue.getApplyusertype();
		appointfromtime=auditQueue.getAppointfromtime();
		
		AuditQueueTurn queueTurn = new AuditQueueTurn();
		queueTurn.setRowguid(UUID.randomUUID().toString());
		queueTurn.setQueueguid(auditqueueguid);
		queueTurn.setTurnDate(new Date());
		queueTurn.setPreviousWindowguid(auditQueue.getHandlewindowguid());
		queueTurn.setCurrentWindowguid(windowguid);
		queueTurn.setTurnQnotype(QueueConstant.QUEUE_TURN_WINDOW);
		queueTurn.setHandleUserguid(handleuserguid);
		queueTurnService.insertQueueturn(queueTurn);

		AuditOrgaWindow window = auditWindowservice.getWindowByWindowGuid(windowguid).getResult();
		queueservice.updateTurnQnoWindow(auditqueueguid, windowguid, window.getWindowno(), window.getLobbytype());

		AS_Queue_TurnPriority = handleConfigservice.getFrameConfig("AS_Queue_TurnPriority", centerguid).getResult();

		

		ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
		Map<String, String> records = new HashMap<String, String>(16);
		try {
			AS_Queue_Mode = configservice.getFrameConfigValue("AS_Queue_Mode");
			AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(taskguid).getResult();

			handlewindowno = window.getWindowno() + ";";
			handlewindows = windowguid + ";";
			queueweight = tasktype.getQueueweight();
			queuevalue = tasktype.getQueuevalue();

			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				AuditQueueInstance queueinstance = new AuditQueueInstance();
				queueinstance.setRowguid(UUID.randomUUID().toString());
				queueinstance.setQno(QNO);
				queueinstance.setTaskguid(taskguid);
				queueinstance.setQueueguid(QueueGuid);
				queueinstance.setQueueweight(queueweight);
				// 针对不同的转号参数设置进行处理

				if (QueueConstant.TURN_PRIORITY_LAST.equals(AS_Queue_TurnPriority)) {
					queueinstance.setGetnotime(new Date());					
				} else {
					queueinstance.setGetnotime(getnotime);
				}

				queueinstance.setAppointfromtime(appointfromtime);
				queueinstance.setQueuevalue(queuevalue);
				queueinstance.setHandlewindows(handlewindows);

				queueinstance.setApplyusertype(applyusertype);
				queueinstanceservice.insert(queueinstance);
			}

			// redis 计数+1
			redis.hincrBy("AuditQueueOrgaWindow_" + windowguid, "waitnum", 1);

			if (StringUtil.isNotBlank(phone)) {
				// 如果值为0，则不发短信
				messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_QH", centerguid).getResult();
				if (StringUtil.isNotBlank(messagecontent)) {
					if (!QueueConstant.Common_no_String.equals(messagecontent)) {
						messagecontent = messagecontent.replace("[#=WaitNum#]",
								String.valueOf(getTaskWaitNum(taskguid, true)));
						messagecontent = messagecontent.replace("[#=HandleWindowNo#]", handlewindowno);
					} else {
						messagecontent = "";
					}
				} else {
					messagecontent = "您所办理事项的当前等待人数" + getTaskWaitNum(taskguid, true) + "人,办理窗口为" + handlewindowno
							+ "。请耐心等待，轮到您时会有短信提醒，也可留意大屏上的显示内容。";
				}
				if (StringUtil.isNotBlank(messagecontent)) {
					imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0,
							new Date(), phone, UUID.randomUUID().toString(), "", "", "", "", "", "", false, "");
				}
			}

			records.put("msg", "success");

		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			records.put("msg", "取号异常");
		} finally {
			redis.close();
		}
		return records;
	}

	public Map<String, String> getAPQNO(String appointguid, String centerguid) {

		String QNO = "";
		String flowsnno = "";
		String QueueGuid = "";
		String queueweight = "";
		String queuevalue = "";
		String handlewindows = "";
		String Handlewindowno = "";
		String handlehallguid = "";
		String[] strHandlewindowno;
		String AS_Queue_Mode = "";
		String messagecontent = "";
		ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
		Map<String, String> records = new HashMap<String, String>(16);
		try {
			AS_Queue_Mode = configservice.getFrameConfigValue("AS_Queue_Mode");
			AuditQueueAppointment auditqueueappointment = appointmentservice.getDetailByRowGuid(appointguid)
					.getResult();

			AuditQueue auditQueue = new AuditQueue();

			flowsnno = EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd")
					+ QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn("QueueFlowSN")), 6, '0');

			QueueGuid = UUID.randomUUID().toString();

			auditQueue.setRowguid(QueueGuid);
			auditQueue.setIdentitycardnum(auditqueueappointment.getIdentitycardnum());
			auditQueue.setPhone(auditqueueappointment.getPhone());
			auditQueue.setTaskguid(auditqueueappointment.getApptaskguid());

			auditQueue.setFlowno(flowsnno);
			List<String> windowguids = windowtasktypeservice
					.getWindowListByTaskTypeGuid(auditqueueappointment.getApptaskguid()).getResult();

			if (windowguids != null && windowguids.size() > 0) {
				auditQueue.setHandlewindowguid("");
				// 判断事项是否已配置窗口
				AuditOrgaWindow auditwindow = null;
				for (String para : windowguids) {
					auditwindow = auditWindowservice.getWindowByWindowGuid(para).getResult();
					if (auditwindow != null && StringUtil.isNotBlank(auditwindow.getIs_usequeue())) {
						if (QueueConstant.Common_yes_String.equals(auditwindow.getIs_usequeue().toString())) {
							Handlewindowno += auditwindow.getWindowno() + ";";
							handlewindows += para + ";";
							if (StringUtil.isBlank(handlehallguid)) {
								handlehallguid = auditwindow.getLobbytype();
							}
						}
					} else {
						// 将不存在的窗口自动删除
						windowtasktypeservice.deletebyWindowguid(para);
					}
				}
				if (StringUtil.isBlank(Handlewindowno)) {
					records.put("msg", "该事项并未正确配置到窗口！");
					return records;
				}

				// 窗口号排序
				strHandlewindowno = Handlewindowno.split(";");
				Arrays.sort(strHandlewindowno);
				Handlewindowno = "";// 置空重新加载
				for (String str : strHandlewindowno) {
					Handlewindowno += str + ";";
				}

			} else {
				records.put("msg", "该事项并未正确配置到窗口！");
				return records;
			}

			auditQueue.setHandlewindowno(Handlewindowno);
			// 获取排队流水号
			QNO = "AP" + QueueCommonUtil.padLeft(String.valueOf(redis.getFlowsn(centerguid + "AP")), 3, '0');
			auditQueue.setQno(QNO);
			auditQueue.setStatus(QueueConstant.Qno_Status_Init);
			auditQueue.setGetnotime(new Date());
			auditQueue.setApplyuserguid(auditqueueappointment.getApplyuserguid());
			auditQueue.setApplyusertype(QueueConstant.Qno_Type_AP);
			auditQueue.setAppointfromtime(auditqueueappointment.getAppointmentfromtime());
			auditQueue.setAppointguid(appointguid);
			auditQueue.setCenterguid(centerguid);
			auditQueue.setHallguid(handlehallguid);
			AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(auditqueueappointment.getApptaskguid())
					.getResult();
			if (tasktype != null) {
				queueweight = tasktype.getQueueweight();
				queuevalue = tasktype.getQueuevalue();
				// 判断是否使用分表模式
				if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
					if (StringUtil.isBlank(queuevalue)) {
						records.put("msg", "该事项并未配置到相关队列！");
						return records;
					}
				}
			}
			if (StringUtil.isBlank(queueweight)) {
				queueweight = "0";
			}
			auditQueue.setQueueweight(queueweight);
			queueservice.addQueue(auditQueue);
			// 判断是否使用分表模式
			if (StringUtil.isBlank(AS_Queue_Mode) || "1".equals(AS_Queue_Mode) || "2".equals(AS_Queue_Mode)) {
				AuditQueueInstance queueinstance = new AuditQueueInstance();
				queueinstance.setRowguid(UUID.randomUUID().toString());
				queueinstance.setQno(QNO);
				queueinstance.setTaskguid(auditqueueappointment.getApptaskguid());
				queueinstance.setQueueguid(QueueGuid);
				queueinstance.setQueueweight(queueweight);
				queueinstance.setGetnotime(new Date());
				queueinstance.setAppointfromtime(auditqueueappointment.getAppointmentfromtime());
				queueinstance.setQueuevalue(queuevalue);
				queueinstance.setHandlewindows(handlewindows);
				queueinstance.setApplyusertype(QueueConstant.Qno_Type_AP);
				queueinstanceservice.insert(queueinstance);
			}

			// 更新预约表数据
			appointmentservice.updateStatusbyRowGuid(appointguid, QueueConstant.Appoint_Status_Process, QNO, new Date(),
					QueueGuid);
			// redis 计数+1
			for (String para : windowguids) {
				redis.hincrBy("AuditQueueOrgaWindow_" + para, "waitnum", 1);
			}
			if (StringUtil.isNotBlank(auditqueueappointment.getPhone())) {
				// 如果值为0，则不发短信
				messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_QH", centerguid).getResult();
				if (StringUtil.isNotBlank(messagecontent)) {
					if (!QueueConstant.Common_no_String.equals(messagecontent)) {
						messagecontent = messagecontent.replace("[#=WaitNum#]",
								String.valueOf(getTaskWaitNum(auditqueueappointment.getApptaskguid(), true)));
						messagecontent = messagecontent.replace("[#=HandleWindowNo#]", Handlewindowno);
					} else {
						messagecontent = "";
					}
				} else {
					messagecontent = "您所办理事项的当前等待人数" + getTaskWaitNum(auditqueueappointment.getApptaskguid(), true)
							+ "人,办理窗口为" + Handlewindowno + "。请耐心等待，轮到您时会有短信提醒，也可留意大屏上的显示内容。";
				}
				if (StringUtil.isNotBlank(messagecontent)) {
					imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(), 0,
							new Date(), auditqueueappointment.getPhone(), UUID.randomUUID().toString(), "", "", "", "",
							"", "", false, "");
				}

			}

			records.put("msg", "success");
			records.put("qno", QNO);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			records.put("msg", "取号异常");
		} finally {
			redis.close();
		}
		return records;
	}

	public String getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid, Boolean UseCall) {
		String qno = QueueConstant.Current_None;
		String AS_IS_USE_LED = "";
		String ledcontent = "";
		// 查找需要办理的号码，针对自动分配过来的号码

		String CurrentHandleNO = queueservice.getCurrentHandleNO(WindowGuid, false).getResult();

		if (StringUtil.isNotBlank(CurrentHandleNO)) {
			qno = CurrentHandleNO;
			if (UseCall) {
				handlecommonseervice.sendQueueMessage(CurrentHandleNO, WindowGuid, CenterGuid, WindowNo);
			}
		}
		// 否则手动查找下一位需要办理的人员
		else {
			String NextQueueNO = GetQueueNOForWindow(WindowGuid, CenterGuid, UserGuid);
			if (StringUtil.isNotBlank(NextQueueNO)) {

				qno = NextQueueNO;
				if (UseCall) {
					handlecommonseervice.sendQueueMessage(NextQueueNO, WindowGuid, CenterGuid, WindowNo);
					// 提前发送短信提醒
					handlecommonseervice.tiqianSendMessage(WindowGuid, CenterGuid, WindowNo, UserGuid);
				}
			} else {
				// led推送（空闲）
				AS_IS_USE_LED = handleConfigservice.getFrameConfig("AS_IS_USE_LED", CenterGuid).getResult();
				if (QueueConstant.Common_yes_String.equals(AS_IS_USE_LED)) {
					ledcontent = handleConfigservice.getFrameConfig("AS_QUEUE_LED_FREE", CenterGuid).getResult();
					if (StringUtil.isBlank(ledcontent)) {
						ledcontent = "欢迎光临！";
					}
					handlecommonservice.sendLEDMQ(ledcontent, WindowNo, CenterGuid);
				}
				qno = QueueConstant.Current_None;

			}
		}

		return qno;
	}

	public int getTaskWaitNum(String TaskGuid, Boolean redis) {
		Integer waitnum = 0;
		Integer sum = 0;
		AuditQueueOrgaWindow queuewindow = new AuditQueueOrgaWindow();

		AuditQueueTasktype auditQueueTasktype = tasktypeservice.getTasktypeByguid(TaskGuid).getResult();
		if (StringUtil.isNotBlank(auditQueueTasktype)) {

			List<String> windowlists = windowtasktypeservice.getWindowListByTaskTypeGuid(TaskGuid).getResult();

			// 对于未配置到混合型窗口的事项，算事项等待人数只需随机取一个窗口进行计算即可。
			if (StringUtil.isNotBlank(auditQueueTasktype.getIsmixwindow())
					&& QueueConstant.CONSTANT_STR_ZERO.equals(auditQueueTasktype.getIsmixwindow())) {
				for (String windowlist : windowlists) {
					// 排除不需要排队叫号的窗口
					AuditOrgaWindow auditwindow = auditWindowservice.getWindowByWindowGuid(windowlist).getResult();

					if (auditwindow != null && auditwindow.getIs_usequeue() == 1) {
						if (redis) {
							queuewindow = windowservice.getDetailbyWindowguid(windowlist).getResult();
							if (StringUtil.isNotBlank(queuewindow) && Integer.parseInt(queuewindow.getWaitnum()) > 0) {
								waitnum = Integer.parseInt(queuewindow.getWaitnum());
							} else {
								waitnum = 0;
							}
						} else {
							waitnum = queueservice.getWindowWaitNum(windowlist, false).getResult();

						}
						break;

					}
				}
			} else {
				for (String windowlist : windowlists) {
					// 排除不需要排队叫号的窗口
					AuditOrgaWindow auditwindow = auditWindowservice.getWindowByWindowGuid(windowlist).getResult();
					if (auditwindow != null && auditwindow.getIs_usequeue() == 1) {
						sum++;
						if (redis) {
							queuewindow = windowservice.getDetailbyWindowguid(windowlist).getResult();
							if (StringUtil.isNotBlank(queuewindow) && Integer.valueOf(queuewindow.getWaitnum()) > 0) {
								waitnum += Integer.valueOf(queuewindow.getWaitnum());
							} else {
								waitnum += 0;
							}

						} else {
							waitnum += queueservice.getWindowWaitNum(windowlist, false).getResult();

						}
					}
				}
				waitnum = (int) Math.ceil((double) waitnum / (double) sum);
			}
		}

		return waitnum;
	}

	public String GetQueueNOForWindow(String WindowGuid, String CenterGuid, String UserGuid) {

		String Mode = configservice.getFrameConfigValue("AS_Queue_Mode");
		// redis 分布式锁
		if (StringUtil.isBlank(Mode) || "1".equals(Mode)) {
			String QNO = "";

			String key = "Lock_Audit_Queue_Instance";
			AuditQueueOrgaWindow window = windowservice.getDetailbyWindowguid(WindowGuid).getResult();
			if (window != null && StringUtil.isNotBlank(window.getQueuevalue())) {
				key += "_" + window.getQueuevalue();

				Boolean boollock = true;
				ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
				try {

					if (redis.tryLock(key, 20, TimeUnit.SECONDS)) {

						boollock = true;
						// 相关业务处理
						QNO = queueservice.getNextQueueNOshardingNoLock(WindowGuid, CenterGuid, UserGuid).getResult();
					} else {

						boollock = false;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (boollock) {
						redis.unlock(key);
					} else {
						redis.close();
					}

				}
			} else {
				log.error("窗口" + WindowGuid + "未关联队列！");
				// 重新初始化QueueValue
				SqlConditionUtil sql = new SqlConditionUtil();
				sql.eq("windowguid", WindowGuid);
				List<AuditQueueWindowTasktype> tasktypelists = windowtasktypeservice.getAllWindowTasktype(sql.getMap())
						.getResult();
				if (tasktypelists != null && tasktypelists.size() > 0) {
					AuditQueueTasktype tasktype = tasktypeservice
							.getTasktypeByguid(tasktypelists.get(0).getTasktypeguid()).getResult();
					if (StringUtil.isNotBlank(tasktype)) {
						if (StringUtil.isNotBlank(tasktype.getQueuevalue())) {
							windowservice.updateWindowQueueValue(WindowGuid, tasktype.getQueuevalue());
						} else {
							log.error("事项分类" + tasktypelists.get(0).getTasktypeguid() + "未配置队列！");
						}
					} else {
						log.error("事项分类" + tasktypelists.get(0).getTasktypeguid() + "对应实体不存在！");
					}

				} else {
					log.error("窗口" + WindowGuid + "未配置事项分类！");
				}
			}
			return QNO;
		}
		// 判断是否使用分表模式
		else if ("2".equals(Mode)) {
			String QNO = "";
			QNO = queueservice.getNextQueueNOsharding(WindowGuid, CenterGuid, UserGuid).getResult();
			return QNO;
		} else {
			ZwfwRedisCacheUtil redis = new ZwfwRedisCacheUtil(false);
			// 先判断窗口属于哪个队列
			String Queue = "";
			String QNO = "";
			Queue = windowextendinfoservice.getQueueByWindowGuid(WindowGuid).getResult();
			try {

				// 插入redis队列
				redis.rpush("AuditQueueWindowQueue_" + Queue, WindowGuid);
				while (true) {
					// 对同一队列窗口点下一位请求进行排序，如果轮到他再执行下一位
					if (WindowGuid.equals(redis.lindex("AuditQueueWindowQueue_" + Queue, 1))) {
						break;
					}
					// 休眠100ms
					Thread.sleep(100);
				}
				// 取下一个号
				QNO = queueservice.getNextQueueNO(WindowGuid, CenterGuid, UserGuid).getResult();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 移除redis队列里面的窗口
				redis.lrem("AuditQueueWindowQueue_" + Queue, 1, WindowGuid);
				redis.close();
			}
			return QNO;
		}

	}

	public Integer getQueueCount(String centerguid, Date startTime, Date endTime) {
		Integer nowCount = 0;
		Integer historyCount = 0;
		// 开始时间设置为当天凌晨
		startTime = EpointDateUtil
				.convertString2Date(EpointDateUtil.convertDate2String(startTime, "yyyy-MM-dd") + " 00:00:00");
		// 结束时间设置为当天23时59分59秒
		endTime = EpointDateUtil
				.convertString2Date(EpointDateUtil.convertDate2String(endTime, "yyyy-MM-dd") + " 23:59:59");
		nowCount = queueservice.getNowQueueCount(centerguid, startTime, endTime).getResult();
		historyCount = queueservice.getHistoryQueueCount(centerguid, startTime, endTime).getResult();
		return nowCount + historyCount;
	}

}
