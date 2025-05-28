package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.domain.AuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.inter.IAuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/selfservicemachinehelp"})
public class MachineHelp {
	@Autowired
	private IAuditZnsbMachineHelp auditZnsbMachineHelpService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IUserRoleRelationService roleRelationService;
	@Autowired
	private IMessagesCenterService messageCenterService;
	@Autowired
	private IAuditZnsbRemoteHelpUserService auditZnsbRemoteHelpUserService;
	@Autowired
	private IAuditZnsbRemoteHelpService auditZnsbRemoteHelpService;
	@Autowired
	private IHandleConfig handleConfigservice;

	@RequestMapping(value = {"/sendMessage"}, method = {RequestMethod.POST})
	public String sendMessage(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			AuditZnsbMachineHelp dataBean = new AuditZnsbMachineHelp();
			String machineguid = obj.getString("machineguid");
			dataBean.setMachineguid(machineguid);
			String guid = UUID.randomUUID().toString();
			dataBean.setRowguid(guid);
			dataBean.setCurrentstatus("0");
			dataBean.setSenddate(new Date());
			String str = (String) this.auditZnsbMachineHelpService.addRecord(dataBean).getResult();
			if ("success".equals(str)) {
				FrameRole frameRole = this.roleService.getRoleByRoleField("rolename", "远程协助");
				if (frameRole != null) {
					String roleguid = frameRole.getRoleGuid();
					List<FrameUserRoleRelation> frameuserrolerelationlist = this.roleRelationService
							.getRelationListByField("roleGuid", roleguid, (String) null, (String) null);
					if (frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
						Iterator var12 = frameuserrolerelationlist.iterator();

						while (var12.hasNext()) {
							FrameUserRoleRelation frameUserRoleRelation = (FrameUserRoleRelation) var12.next();
							String targetusername = this.userService
									.getUserNameByUserGuid(frameUserRoleRelation.getUserGuid());
							String title = "";
							String handleUrl = "";
							String item = "";
							title = "【待办】 远程协助" + item;
							handleUrl = "znsb/equipmentdisplay/selfservicemachine/machinehelp/showhelpmessage?guid="
									+ guid;
							String messageItemGuid = UUID.randomUUID().toString();
							this.messageCenterService.insertWaitHandleMessage(messageItemGuid, title, "办理",
									frameUserRoleRelation.getUserGuid(), targetusername,
									frameUserRoleRelation.getUserGuid(),
									this.userService.getUserNameByUserGuid(frameUserRoleRelation.getUserGuid()), "",
									handleUrl, frameUserRoleRelation.getUserGuid(), "", 1, "", "", guid,
									messageItemGuid.substring(0, 1), new Date(), frameUserRoleRelation.getUserGuid(),
									frameUserRoleRelation.getUserGuid(), "", "");
						}

						dataJson.put("msg", guid);
					} else {
						dataJson.put("msg", "请先确认系统是否存在'远程协助'角色人员！");
					}
				} else {
					dataJson.put("msg", "请先确认系统是否存在'远程协助'角色！");
				}
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException var19) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + var19.getMessage(), "");
		}
	}

	@RequestMapping(value = {"/cancel"}, method = {RequestMethod.POST})
	public String cancle(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String rowGuid = obj.getString("rowGuid");
			AuditZnsbMachineHelp dataBean = (AuditZnsbMachineHelp) this.auditZnsbMachineHelpService
					.selectByRowguid(rowGuid).getResult();
			if (!"1".equals(dataBean.getCurrentstatus())) {
				dataBean.setCurrentstatus("2");
				dataBean.setOperatedate(new Date());
				this.auditZnsbMachineHelpService.update(dataBean);
				this.deleteMessage(rowGuid);
				return JsonUtils.zwdtRestReturn("1", "", "关闭成功");
			} else {
				return JsonUtils.zwdtRestReturn("0", "", "工作人员正在连接，不能取消");
			}
		} catch (JSONException var6) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + var6.getMessage(), "");
		}
	}

	public void deleteMessage(String rowGuid) {
		FrameRole frameRole = this.roleService.getRoleByRoleField("rolename", "远程协助");
		if (frameRole != null) {
			String roleguid = frameRole.getRoleGuid();
			List<FrameUserRoleRelation> frameuserrolerelationlist = this.roleRelationService
					.getRelationListByField("roleGuid", roleguid, (String) null, (String) null);
			if (frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
				Iterator var5 = frameuserrolerelationlist.iterator();

				while (var5.hasNext()) {
					FrameUserRoleRelation frameUserRoleRelation = (FrameUserRoleRelation) var5.next();
					String targetUserGuid = frameUserRoleRelation.getUserGuid();
					this.messageCenterService.deleteMessageByIdentifier(rowGuid, targetUserGuid);
				}
			}
		}

	}

	@RequestMapping(value = {"/getClient"}, method = {RequestMethod.POST})
	public String getClient(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String rowGuid = obj.getString("rowGuid");
			JSONObject dataJson = new JSONObject();
			AuditZnsbMachineHelp dataBean = (AuditZnsbMachineHelp) this.auditZnsbMachineHelpService
					.selectByRowguid(rowGuid).getResult();
			if (dataBean.getCurrentstatus().equals("1")) {
				AuditZnsbRemoteHelp auditZnsbAioconfig = (AuditZnsbRemoteHelp) this.auditZnsbRemoteHelpService
						.getDetailByAioRowguid(dataBean.getMachineguid()).getResult();
				AuditZnsbRemoteHelpUser auditZnsbAssessconfig = (AuditZnsbRemoteHelpUser) this.auditZnsbRemoteHelpUserService
						.getDetailByUserguid(dataBean.getUserguid()).getResult();
				dataJson.put("username", auditZnsbAioconfig.getAccount());
				dataJson.put("password", auditZnsbAioconfig.getPassword());
				dataJson.put("room", auditZnsbAssessconfig.getRoom());
				dataJson.put("tcp", this.handleConfigservice
						.getFrameConfig("AS_HST_TCP", auditZnsbAioconfig.getCenterguid()).getResult());
				return JsonUtils.zwdtRestReturn("1", "", dataJson);
			} else {
				return JsonUtils.zwdtRestReturn("0", "获取数据失败！", "");
			}
		} catch (JSONException var9) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + var9.getMessage(), "");
		}
	}

	@RequestMapping(value = {"/isMachineHelp"}, method = {RequestMethod.POST})
	public String isMachineHelp(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String centerguid = obj.getString("centerguid");
			JSONObject dataJson = new JSONObject();
			String conf = (String) this.handleConfigservice.getFrameConfig("ismachinehelp", centerguid).getResult();
			if (StringUtil.isNotBlank(conf) && "1".equals(conf)) {
				dataJson.put("conf", conf);
				return JsonUtils.zwdtRestReturn("1", "", dataJson);
			} else {
				return JsonUtils.zwdtRestReturn("0", "远程协助系统参数未配置！", "");
			}
		} catch (JSONException var7) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + var7.getMessage(), "");
		}
	}
}