package com.epoint.auditorga.auditonlineconsult.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsultExt;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditDaibanConsultExt;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import com.epoint.newshow2.api.Newshow2Service;

/**
 * 区域配置修改页面对应的后台
 * 
 * @author yangjl
 * @version [版本号, 2017-04-12 17:13:31]
 */
@RestController("auditonlinedaibanansweraction")
@Scope("request")
public class AuditOnlineDaibanAnswerAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 90412820785420099L;

    /**
     * 
     */
    private AuditDaibanConsult dataBean = null;
    
    private TreeModel treeModel = null;

    /**
     * 行政区划级别下拉列表model
     */
    private List<SelectItem> publishOnWeb = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditDaibanConsultExt> model;
    /**
     * 外网提交咨询投诉的附件
     */
    private FileUploadModel9 uploadModel;
    /**
     * 附件上传model追答上传
     */
    private FileUploadModel9 uploadModelExt;
    /**
     * 附件上传model第一次回复上传
     */
    private FileUploadModel9 uploadModelApply;
    
    @Autowired
    private IOuService ouService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IAuditDaibanConsultExt consultExt;
    @Autowired
    private IAuditOrgaServiceCenter serviceCenter;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditDaibanConsult onlineConsult;
    @Autowired
    private IAuditDaibanConsultExt onlineConsultExt;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditOrgaArea auditOrgaAreaService;
    @Autowired
    private IAuditOrgaWindow auditOrgaWindowService;
    @Autowired
    private IConfigService configService;
    
    @Autowired
    private IHandleProject ihandleproject;
    
    @Autowired
    private Newshow2Service newshow2Service;
    
    
    private String guid;

    private String ouname = "";

    private String userOuguid = "";

    private String messageItemGuid = "";

    private String clientGuid = "";

    private String clientGuidExt = "";

    private String clientGuidApply = "";

    private String sorucename = "";

    private String status = "";

    private String answeroutput = "";//获取答复框内容
    
    @Override
    public void pageLoad() {
        messageItemGuid = getRequestParameter("messageItemGuid");
        guid = getRequestParameter("guid");
        dataBean = onlineConsult.getConsultByRowguid(guid).getResult();
        if(dataBean!=null){
            dataBean.put("answerouguid", dataBean.getOuguid());//本类save()方法使用
            AuditOrgaServiceCenter result = serviceCenter
                    .findAuditServiceCenterByGuid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            if (result != null) {
                userOuguid = result.getOuguid();
            }
            if (StringUtil.isBlank(dataBean.getOuguid())) {
                if (StringUtil.isNotBlank(userOuguid)) {
                    FrameOu ou = ouService.getOuByOuGuid(userOuguid);
                    ouname = ou.getOuname();
                    dataBean.put("ouname", ouname);
                }
            }
            else {
                FrameOu ou = ouService.getOuByOuGuid(dataBean.getOuguid());
                ouname = ou.getOuname();
                dataBean.put("ouname", ouname);
            }

            // 设置转交情况，如果空页面显示无
            if (dataBean != null && StringUtil.isBlank(dataBean.getZhuanjiaolog())) {
                dataBean.setZhuanjiaolog("无");
            }
            // clientguid是外网咨询附件 clientapplyguid是回复附件
            if (StringUtils.isNoneBlank(dataBean.getClientapplyguid())) {
                clientGuidApply = dataBean.getClientapplyguid();
            }
            else {
                clientGuidApply = UUID.randomUUID().toString();
            }

            if (StringUtils.isNoneBlank(dataBean.getClientguid())) {
                clientGuid = dataBean.getClientguid();
            }
            else {
                clientGuid = UUID.randomUUID().toString();
            }

            // 通过代码项获取数据
            if (StringUtils.isNoneBlank(dataBean.getSource())) {
                sorucename = iCodeItemsService.getItemTextByCodeName("咨询建议来源", dataBean.getSource());
            }

            // 当答复内容为空时，将其显示为无
            if (StringUtil.isBlank(dataBean.getAnswer())) {
                dataBean.put("answer", "无");
            }

            if (dataBean != null && ZwfwConstant.CONSTANT_STR_ONE.equals(dataBean.getIsAnonymous())) {
                addCallbackParam("askerusername", "匿名用户");
                addCallbackParam("askermobile", "*****");
            }
            else {
                addCallbackParam("askerusername", dataBean.getAskerusername());
                addCallbackParam("askermobile", dataBean.getAskerMobile());
            }
            clientGuidExt = UUID.randomUUID().toString();
            if (dataBean == null) {
                dataBean = new AuditDaibanConsult();
            }
                addCallbackParam("iscurrentou", ZwfwConstant.CONSTANT_STR_ONE);
        }else{
            addCallbackParam("msg","咨询不存在或者已删除！");
            clientGuid = UUID.randomUUID().toString();
            clientGuidExt = UUID.randomUUID().toString();
            clientGuidApply = UUID.randomUUID().toString();
            dataBean = new AuditDaibanConsult();
        }
        
       
        
    }

    public void moveDepart() {
        String sendMessage = "";
        if(StringUtil.isNotBlank(dataBean.getRowguid())){
            if(ZwfwConstant.CONSULT_TYPE_ZX.equals(dataBean.getConsulttype()) && StringUtil.isNotBlank(dataBean.getBusinessguid()) && dataBean.getStatus().equals(ZwfwConstant.ZIXUN_TYPE_DDF)){
                ihandleproject.delZwfwMessage(null, ZwfwUserSession.getInstance().getAreaCode(), "中心管理员",dataBean.getRowguid());
                messageItemGuid="";
            }else{                
                deleteMessages();
            }            
            sendMessage = sendMessage(dataBean.getRowguid());
            //网厅过来的套餐咨询需要删除待办,删除辖区下的中心管理员待办
            StringBuffer buffer = new StringBuffer();            
            buffer.append(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            buffer.append("/");
            buffer.append(ouname);
            buffer.append(UserSession.getInstance().getDisplayName() + "转交给");
            buffer.append(ouService.getOuByOuGuid(dataBean.getOuguid()).getOuname());
            dataBean.setHandledate(new Date());
            dataBean.setZhuanjiaolog(!"无".equals(dataBean.getZhuanjiaolog())
                    ? (dataBean.getZhuanjiaolog() + ";<br/>" + buffer.toString()) : buffer.toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
            sendMessage = updateConsult(dataBean);
            if (StringUtil.isNotBlank(sendMessage)){
                sendMessage ="转移成功！";
            }
        }else{
            sendMessage = "办件不存在或者已删除！";;
        }
       
        addCallbackParam("msg", sendMessage);
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        if (dataBean != null) {
            dataBean.setAnswer(answeroutput);
            dataBean.remove("ouguid");
            dataBean.remove("ouname");
        }
        String sendMessage = "";
        if (dataBean!=null && StringUtil.isNotBlank(dataBean.getRowguid()) && StringUtil.isNotBlank(dataBean.getAnswer())) {
            // 如果status 0(待答复)-->变为1(已答复)
            // 如果status 1-->变为2(追问未答复)
            // 如果status 2-->变为3追问已答复
            if (dataBean.getStatus().equals(ZwfwConstant.ZIXUN_TYPE_DDF)) {
                // 已答复，直接存在consult表中
                dataBean.setStatus(ZwfwConstant.ZIXUN_TYPE_YDF);
                dataBean.setAnswerdate(new Date());
                dataBean.setAnsweruserguid(UserSession.getInstance().getUserGuid());
                dataBean.setAnswerusername(UserSession.getInstance().getDisplayName());
                dataBean.setClientapplyguid(getViewData("clientGuidExt"));
                dataBean.setHandledate(new Date());
                dataBean.setAnswerouguid(dataBean.getAnswerouguid());
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername(UserSession.getInstance().getDisplayName());
                sendMessage = updateConsult(dataBean);
                //网厅过来的套餐咨询需要删除待办,删除辖区下的中心管理员待办
                if(ZwfwConstant.CONSULT_TYPE_ZX.equals(dataBean.getConsulttype()) && StringUtil.isNotBlank(dataBean.getBusinessguid())){
                    ihandleproject.delZwfwMessage(null, ZwfwUserSession.getInstance().getAreaCode(), "中心管理员",dataBean.getRowguid());
                    messageItemGuid="";
                }
            }
            else if (dataBean.getStatus().equals(ZwfwConstant.ZIXUN_TYPE_YDF)) {
                // 向consult扩展表加一条追答数据，我想追答
                insertConsultExt();
                sendMessage = "答复成功！";
            }
            else if (dataBean.getStatus().equals(ZwfwConstant.ZIXUN_TYPE_ZWDDF)) {
                // 追问已答复
                dataBean.setStatus(ZwfwConstant.ZIXUN_TYPE_ZWYDF);
                dataBean.setReadstatus(ZwdtConstant.CONSULT_READSTATUS_NO);
                // 向consult扩展表加一条追答数据
                insertConsultExt();
                updateConsultByField(dataBean.getRowguid(), "readstatus",
                        ZwdtConstant.CONSULT_READSTATUS_NO);
                sendMessage = updateConsultByField(dataBean.getRowguid(), "status",
                        dataBean.getStatus());
            }
            else if (dataBean.getStatus().equals(ZwfwConstant.ZIXUN_TYPE_ZWYDF)) {
                // 向consult扩展表加一条追答数据
                insertConsultExt();
                sendMessage = "答复成功！";
            }
            // 删除待办事宜，并同步转移到历史表中
            if (StringUtil.isNotBlank(messageItemGuid) && !"undefined".equals(messageItemGuid)) {
            	//system.out.println(messageItemGuid);
            	//system.out.println(dataBean.getRowguid());
               //messageCenterService.deleteMessageByIdentifier(dataBean.getRowguid(), "");
               messageCenterService.deleteMessage(messageItemGuid,
                      UserSession.getInstance().getUserGuid());
            }
            
            
        }
        else {
            if(dataBean!=null && StringUtil.isNotBlank(dataBean.getRowguid())){
                sendMessage = updateConsultByField(dataBean.getRowguid(), "publishonweb",
                        dataBean.getPublishonweb());
                sendMessage = "修改成功！";
            }else{
                sendMessage = "办件不存在或者已删除！";
            }
        }
        if("答复成功！".equals(sendMessage)){
            //向大厅发送代办消息
            String title = "";
            String type = dataBean.getConsulttype();
            String openUrl = configService.getFrameConfigValue("zwdtMsgurl")+"/epointzwmhwz/pages/myspace/complaindetail?consultguid="+dataBean.getRowguid()
            +"&clientguid="+dataBean.getClientguid();
            if(ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                title = "【咨询已回复】" + dataBean.getQuestion(); 
            }
            else {
                title = "【投诉已回复】" + dataBean.getQuestion();
            }
            messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title, IMessagesCenterService.MESSAGETYPE_WAIT, dataBean.getAskeruserguid(), "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                       "", openUrl, UserSession.getInstance().getOuGuid(),  UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
          //删除大厅的代办消息
            String msgguid = this.getRequestParameter("messageItemGuid");       
            messageCenterService.deleteMessage(msgguid, userSession.getUserGuid()); 
            deleteMessages();
        }
        addCallbackParam("msg", sendMessage);
    }

    public void insertConsultExt() {
        UserSession instance = UserSession.getInstance();
        AuditDaibanConsultExt ext = new AuditDaibanConsultExt();
        ext.setRowguid(UUID.randomUUID().toString());
        ext.setOperatedate(new Date());
        ext.setOperateusername(instance.getDisplayName());
        ext.setQauserguid(instance.getUserGuid());
        ext.setQausername(instance.getDisplayName());
        ext.setQadate(new Date());
        ext.setQaloginguid(instance.getLoginID());
        ext.setConsultguid(dataBean.getRowguid());
        ext.setContent(dataBean.getAnswer());
        ext.setOuguid(dataBean.getAnswerouguid());
        ext.setType(ZwfwConstant.ZIXUN_EXT_TYPE_ZD);
        ext.setClientguid(getViewData("clientGuidExt"));
        consultExt.addConsultExt(ext);
    }

    public String sendMessage(String consultGuid) {
        String retstr = "";
        // 根据转移的部门获取其下人员信息
        List<FrameUser> userList = userService.getUserListByOuGuidWithNotEnabled(dataBean.getOuguid(), "", "", "",
                false, false, false, 1);

        // 1、获取角色标识
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "窗口负责人");
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            // 2、获取该角色的对应的人员
            List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                    .getRelationListByField("roleGuid", roleguid, null, null);
            if (frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
                // 3、发送待办给审核人员
                for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                    for (FrameUser user : userList) {
                        if (frameUserRoleRelation.getUserGuid().equals(user.getUserGuid())) {
                            String targetuserguid = frameUserRoleRelation.getUserGuid();
                            String targetusername = userService.getUserNameByUserGuid(targetuserguid);
                            String title = "";
                            String handleUrl = "";
                            String item = "";
                            if (dataBean.getConsulttype().equals(ZwfwConstant.CONSULT_TYPE_ZX)) {
                                item = "未答复咨询！";
                                // 待办名称
                                title = "【待办】" + dataBean.getTitle() + "，" + item;
                                // 处理页面
                                handleUrl = "epointzwfw/auditorga/auditconsult/auditonlineconsultanswer?guid=" + guid;
                            }
                            else {
                                item = "未答复投诉！";
                                title = "【待办】" + dataBean.getTitle() + "，" + item;
                                handleUrl = "epointzwfw/auditorga/auditcomplain/auditonlinecomplainanswer?guid=" + guid;
                            }
                            FrameUser frameUser = userService.getUserByUserField("userguid",
                                    UserSession.getInstance().getUserGuid());
                            String messageItemGuid = UUID.randomUUID().toString();
                            messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                                    IMessagesCenterService.MESSAGETYPE_WAIT, targetuserguid, targetusername,
                                    frameUser.getUserGuid(), frameUser.getDisplayName(), "", handleUrl,
                                    user.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", targetuserguid,
                                    messageItemGuid.substring(0, 1), new Date(), frameUser.getUserGuid(),
                                    frameUser.getUserGuid(), "", "");
                        }
                    }
                }
                retstr = "ok";
            }
            else {
                retstr = "请先确认系统是否存在'窗口负责人'角色人员！";
            }
        }
        else {
            retstr = "请先确认系统是否存在'窗口负责人'角色！";
        }
        return retstr;
    }
    
    /**
     * 删除一个部门对应人员的待办消息
     */
    public void deleteMessages() {
        //获取当前部门下的人员信息
        List<FrameUser> userList = userService.getUserListByOuGuidWithNotEnabled(userSession.getOuGuid(), "", "", "",
                false, false, false, 1);
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "窗口负责人");
        if(frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                    .getRelationListByField("roleGuid", roleguid, null, null);
            if(frameuserrolerelationlist != null && frameuserrolerelationlist.size() > 0) {
                //删除该部门下对应角色人的待办消息
                for(FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                    for(FrameUser user : userList) {
                        if(frameUserRoleRelation.getUserGuid().equals(user.getUserGuid())) {
                            String targetUserGuid = frameUserRoleRelation.getUserGuid();
                            messageCenterService.deleteMessageByIdentifier(targetUserGuid, targetUserGuid);
                        }
                    }
                }
            }
        }
    }

    public void updateEvaluation() {
        // dataBean.setEvaluationdate(new Date());
        String updateConsult = updateConsult(dataBean);
        addCallbackParam("msg", updateConsult);
    }

    public DataGridModel<AuditDaibanConsultExt> getDialogDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditDaibanConsultExt>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 3090410626460776791L;

                @Override
                public List<AuditDaibanConsultExt> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(dataBean.getRowguid())) {
                        sql.eq("consultguid", dataBean.getRowguid());
                    }
                    PageData<AuditDaibanConsultExt> pageData = getConsultExtPageData(sql.getMap(),
                            first, pageSize, sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public FileUploadModel9 getUploadModel() {
        if (uploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    uploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            addViewData("clientGuid", clientGuid);
            uploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(clientGuid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return uploadModel;
    }

    public FileUploadModel9 getUploadModelExt() {
        if (uploadModelExt == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    uploadModelExt.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(getViewData("clientGuidExt"))) {
                addViewData("clientGuidExt", clientGuidExt);
            }
            clientGuidExt = getViewData("clientGuidExt");
            uploadModelExt = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(clientGuidExt, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return uploadModelExt;
    }

    public FileUploadModel9 getUploadModelApply() {
        if (uploadModelApply == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    uploadModelApply.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            addViewData("clientGuidApply", clientGuidApply);
            uploadModelApply = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(clientGuidApply, null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return uploadModelApply;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getPublishOnWeb() {
        if (publishOnWeb == null) {
            publishOnWeb = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return publishOnWeb;
    }

    public AuditDaibanConsult getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditDaibanConsult dataBean) {
        this.dataBean = dataBean;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }

    public String getUserOuguid() {
        return userOuguid;
    }

    public void setUserOuguid(String userOuguid) {
        this.userOuguid = userOuguid;
    }

    public String getMessageItemGuid() {
        return messageItemGuid;
    }

    public void setMessageItemGuid(String messageItemGuid) {
        this.messageItemGuid = messageItemGuid;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientGuidExt() {
        return clientGuidExt;
    }

    public void setClientGuidExt(String clientGuidExt) {
        this.clientGuidExt = clientGuidExt;
    }

    public void setUploadModelApply(FileUploadModel9 uploadModelApply) {
        this.uploadModelApply = uploadModelApply;
    }
    
    
    /**
     * List页面 action中 保存修改
     * 
     * @param auditConsultList
     *            咨询投诉实体集合
     * @return 字符串
     */
    public String updateConsult(AuditDaibanConsult consult) {
        String msg = "";
        String result = onlineConsult.updateConsult(consult).getResult();
        if (result == null && consult != null && StringUtil.isNotBlank(consult.getAnswer())
                && StringUtil.isBlank(result)) {
            msg = "答复成功！";
        }
        return msg;
    }
    
    public String updateConsultByField(String rowGuid, String key, String value) {
        String msg = "";
        String result = onlineConsult.updateConsultByField(rowGuid, key, value).getResult();
        if (result == null) {
            msg = "答复成功！";
        }
        else {
            msg = result;
        }
        return msg;
    }
    
    /**
     * 查consult扩展表分页数据
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageData<AuditDaibanConsultExt> getConsultExtPageData(Map<String, String> conditionMap, Integer first,
            Integer pageSize, String sortField, String sortOrder) {
        PageData<AuditDaibanConsultExt> pageData = new PageData<AuditDaibanConsultExt>();
        List<AuditDaibanConsultExt> consultExtList = onlineConsultExt
                .selectConsultExtByPage(conditionMap, first, pageSize, sortField, sortOrder).getResult();
        for (AuditDaibanConsultExt consultExt : consultExtList) {
            consultExt.setClientguid(getTempUrl(consultExt.getClientguid()));
            if (StringUtil.isNotBlank(consultExt) && 
                    consultExt.getType().equals(ZwfwConstant.ZIXUN_EXT_TYPE_ZW)) {  //判断是否是追问
                if (StringUtil.isNotBlank(consultExt.getConsultguid())) {   
                	AuditDaibanConsult auditOnlineConsult =    //获取追问的主题
                            onlineConsult.getConsultByRowguid(consultExt.getConsultguid()).getResult();
                    if (StringUtil.isNotBlank(auditOnlineConsult) && 
                            auditOnlineConsult.getIsAnonymous().equals(ZwfwConstant.CONSTANT_STR_ONE)) {  //判断是否是匿名
                        consultExt.setQausername("匿名用户");
                    }
                }
            }
        }
        Integer count = onlineConsultExt.getConsultExtCount(conditionMap).getResult();
        pageData.setList(consultExtList);
        pageData.setRowCount(count);
        return pageData;
    }
    
    /**
     * 获取附件，如果有附件将附件信息显示在页面列表上
     * @param cliengguid
     * @return
     */
    public String getTempUrl(String cliengguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(cliengguid) && cliengguid != null) {
            List<FrameAttachInfo> listFrameAttachInfo = iAttachService.getAttachInfoListByGuid(cliengguid);
            // 有附件
            if (listFrameAttachInfo.size() > 0) {
                for (FrameAttachInfo frameAttachInfo : listFrameAttachInfo) {
                    String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                    wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" "
                            + strURL + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;</br>";
                }
            }
            else {
                wsmbName = "无附件！";
            }
        }
        else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }

    public String getSorucename() {
        return sorucename;
    }

    public void setSorucename(String sorucename) {
        this.sorucename = sorucename;
    }

    public String getAnsweroutput() {
        return answeroutput;
    }

    public void setAnsweroutput(String answeroutput) {
        this.answeroutput = answeroutput;
    }
    
    public TreeModel getTreeModelExceptOwnOu() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {
                private static final long serialVersionUID = -7089566877270145158L;

                @Override
                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();
                    List<FrameOu> listou = new ArrayList<FrameOu>();

                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有部门");
                        root.setId("f9root");
                        root.setPid("-1");
                        root.getColumns().put("isOU", "true");//标记：不是部门节点
                        list.add(root);
                        root.setCkr(false);
                        root.setExpanded(true);//展开下一层节点
                        list.addAll(fetch(root));//自动加载下一层树结构
                    }
                    else {
                        if (StringUtil.isNotBlank(treeNode.getSearchCondition())) {
                            AuditOrgaArea auditOrgaArea = auditOrgaAreaService.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
                            if (auditOrgaArea != null) {
                                    List<FrameOu> frameOus = ouService.listDependOuByParentGuid(auditOrgaArea.getOuguid(), "", 2);
                                    // 筛选并排序
                                    listou = frameOus.stream()
                                            .filter(x -> x.getOuname().contains(treeNode.getSearchCondition())
                                                    && !auditOrgaArea.getOuguid().equals(x.getOuguid()))
                                            .sorted((a, b) -> b.getOrderNumber().compareTo(a.getOrderNumber()))
                                            .collect(Collectors.toList());
                            }
                        }else{
                            List<FrameOu> oulist = newshow2Service.getOuListByAreacode(ZwfwUserSession.getInstance().getAreaCode());
                            //List<String> ouguids = auditOrgaWindowService.getoulistBycenterguid(ZwfwUserSession.getInstance().getCenterGuid()).getResult();
                            for (FrameOu frame : oulist) {
                                if (!frame.getOuguid().equals(userSession.getOuGuid())) {
                                    listou.add(ouService.getOuByOuGuid(frame.getOuguid()));
                                    
                                }
                            }
                        }
                    }
                    
                    //部门绑定
                    for (FrameOu frameOu : listou) {
                        if (frameOu != null) {
                            TreeNode node = new TreeNode();
                            node.setId(frameOu.getOuguid());
                            node.setText(frameOu.getOuname());
                            node.setPid("f9root");
                            node.setLeaf(true);
                            list.add(node);
                        }
                    }
                    return list;
                }
            };
            treeModel.setSelectNode(new ArrayList<SelectItem>()
            {
                {
                    add(new SelectItem("", "请选择部门"));
                }
            });
        }

        return treeModel;
    }


}
