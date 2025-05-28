package com.epoint.auditqueue.auditznsbmashinehelp.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.domain.AuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.inter.IAuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;

/**
 * 新增页面对应的后台
 * 
 * @author JackLove
 * @version [版本号, 2017-12-27 09:43:53]
 */
@RestController("auditznsbmachinehelpaction")
@Scope("request")
public class AuditZnsbMachineHelpAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditZnsbMachineHelp auditZnsbMachineHelpService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAuditZnsbRemoteHelpUserService auditZnsbRemoteHelpUserService;
    /**
     * 实体对象
     */
    private AuditZnsbMachineHelp dataBean = null;

    public void pageLoad() {
        String rowGuid = getRequestParameter("guid");
        dataBean = auditZnsbMachineHelpService.selectByRowguid(rowGuid).getResult();
        if (dataBean!=null) {
            AuditZnsbEquipment equipment = equipmentservice.getEquipmentByRowguid(dataBean.getMachineguid())
                    .getResult();
            String hallName = equipment.getHallname();
            String centerName = equipment.getCentername();
            String macAddress = equipment.getMacaddress();
            String machineno = equipment.getMachineno();
            Date sendTime=dataBean.getSenddate();
            SimpleDateFormat sfm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date=sfm.format(sendTime);
            JSONObject dataJson = new JSONObject();
            dataJson.put("hallName", returnStr(hallName));
            dataJson.put("centerName", returnStr(centerName));
            dataJson.put("macAddress", returnStr(macAddress));
            dataJson.put("machineno", returnStr(machineno));
            dataJson.put("sendTime", returnStr(date));            
            dataJson.put("currentStatus", dataBean.getCurrentstatus());
            addCallbackParam("msg", dataJson);
        }
        else {
            addCallbackParam("msg", "error");
        }
    }
    
    public String returnStr(String param){
        if(StringUtil.isBlank(param)){
            return "";
        }
        return param;
    }

    /**
     * 接受
     * 
     */
    public void update() {
        String rowGuid = getRequestParameter("guid");
        dataBean = auditZnsbMachineHelpService.selectByRowguid(rowGuid).getResult();
        if ("0".equals(dataBean.getCurrentstatus())){
            dataBean.setCurrentstatus(QueueConstant.CONSTANT_STR_ONE);
            dataBean.setOperatedate(new Date());
            dataBean.setAcceptdate(new Date());
            dataBean.setUserguid(userSession.getUserGuid());
            auditZnsbMachineHelpService.update(dataBean);
            deleteMessage(rowGuid);
            addCallbackParam("msg", "success");
        }
        else if ("2".equals(dataBean.getCurrentstatus())) {
            deleteMessage(rowGuid);
        }
        else {
            addCallbackParam("msg", "error");
        }
    }
    
    /**
     * 远程监控主席端  登录信息
     * 
     */
    public void getServer() {
        String rowGuid = getRequestParameter("guid");
        AuditZnsbMachineHelp dataBean = auditZnsbMachineHelpService.selectByRowguid(rowGuid).getResult();
        AuditZnsbRemoteHelpUser auditZnsbRemoteHelpUser = auditZnsbRemoteHelpUserService.getDetailByUserguid(dataBean.getUserguid()).getResult();
        if( auditZnsbRemoteHelpUser!=null ){
            addCallbackParam("username", auditZnsbRemoteHelpUser.getAccount());
            addCallbackParam("password", auditZnsbRemoteHelpUser.getPassword());
            addCallbackParam("room", auditZnsbRemoteHelpUser.getRoom());
            addCallbackParam("tcp", handleConfigservice.getFrameConfig("AS_HST_TCP", auditZnsbRemoteHelpUser.getCenterguid()).getResult());
        }else{
            addCallbackParam("msg","error");
        }
    }

    /**
     * 删除消息
     * 
     */
    public void deleteMessage(String rowGuid) {
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "远程协助");
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                    .getRelationListByField("roleGuid", roleguid, null, null);
            if (frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
                //删除该部门下对应角色人的待办消息
                for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                    String targetUserGuid = frameUserRoleRelation.getUserGuid();
                    messageCenterService.deleteMessageByIdentifier(rowGuid, targetUserGuid);
                    ;
                }
            }
        }
    }

    public AuditZnsbMachineHelp getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbMachineHelp();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbMachineHelp dataBean) {
        this.dataBean = dataBean;
    }

}
