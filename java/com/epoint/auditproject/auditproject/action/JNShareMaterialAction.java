package com.epoint.auditproject.auditproject.action;



import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditOnlineConsult;
import com.epoint.basic.auditsp.auditspspjgys.domain.AuditSpSpJgys;
import com.epoint.basic.auditsp.auditspspjgys.inter.IAuditSpSpJgysService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.userfeedback.api.IUserFeedbackService;
import com.epoint.xmz.userfeedback.api.entity.UserFeedback;

@RestController("jnsharematerialaction")
@Scope("request")
public class JNShareMaterialAction extends BaseController
{


    /**
     * 
     */
    private static final long serialVersionUID = 90412820785420099L;

    /**
     * 
     */
    private UserFeedback dataBean = null;
    
    /**
     * 行政区划级别下拉列表model
     */
    private List<SelectItem> publishOnWeb = null;

    /**
     * 表格控件model
     */
    private DataGridModel<UserFeedback> model;
    /**
     * 外网提交咨询投诉的附件
     */
    private FileUploadModel9 uploadModel;
    /**
     * 外网提交咨询投诉的附件
     */
    private FileUploadModel9 applyUploadModel;
    
    private FileUploadModel9 ycuploadModel;
    
    private FileUploadModel9 lhchcUploadModel;
    /**
     * 附件上传model追答上传
     */
    private FileUploadModel9 uploadModelExt;
    /**
     * 附件上传model第一次回复上传
     */
    private FileUploadModel9 uploadModelApply;
    
    @Autowired
    private IUserFeedbackService iUserFeedbackService;
    @Autowired
    private IAuditOnlineConsult onlineConsult;
    @Autowired
    private IAttachService iAttachService;
    @Autowired
    private IAuditSpSpJgysService iAuditSpSpJgysService;
    
    private String subappGuid;

    
    private String applycliengguid = "";
    
    private String yscliengguid = "";
    
    private String lhchcliengguid = "";

    private String clientGuidExt = "";

    private String clientGuidApply = "";

    private String sorucename = "";


    private String answeroutput = "";//获取答复框内容
    
    @Override
    public void pageLoad() {
    	subappGuid = getRequestParameter("subappGuid");
    	 AuditSpSpJgys auditSpSpJgys = iAuditSpSpJgysService.findAuditSpSpJgysBySubappGuid(subappGuid);
    	
    	 if(auditSpSpJgys != null) {
    		 if (StringUtils.isNoneBlank(auditSpSpJgys.getStr("applycliengguid"))) {
    	        	applycliengguid = auditSpSpJgys.getStr("applycliengguid");
    	        }
    	        else {
    	        	applycliengguid = UUID.randomUUID().toString();
    	        }
    	        
    	        if (StringUtils.isNoneBlank(auditSpSpJgys.getStr("yscliengguid"))) {
    	        	yscliengguid = auditSpSpJgys.getStr("yscliengguid");
    	        }
    	        else {
    	        	yscliengguid = UUID.randomUUID().toString();
    	        }
    	        
    	        if (StringUtils.isNoneBlank(auditSpSpJgys.getStr("lhchcliengguid"))) {
    	        	lhchcliengguid = auditSpSpJgys.getStr("lhchcliengguid");
    	        }
    	        else {
    	        	lhchcliengguid = UUID.randomUUID().toString();
    	        }
    	 }else {
    		 applycliengguid = UUID.randomUUID().toString();
    		 yscliengguid = UUID.randomUUID().toString();
    		 lhchcliengguid = UUID.randomUUID().toString();
    	 }
         clientGuidExt = UUID.randomUUID().toString();
        
        
    }

    /**
     * 保存修改
     * 
     */
    public void save(String content) {
    	 UserSession instance = UserSession.getInstance();
    	 UserFeedback userfeedback = new UserFeedback();
    	 userfeedback.setRowguid(UUID.randomUUID().toString());
    	 userfeedback.setOperatedate(new Date());
    	 userfeedback.setOperateusername(instance.getDisplayName());
    	 userfeedback.setBacktime(new Date());
    	 userfeedback.setUsername(instance.getDisplayName());
    	 userfeedback.setContent(content);
    	 userfeedback.setBackcliengguid(getViewData("clientGuidExt"));
    	 userfeedback.setSubappguid(subappGuid);
    	 iUserFeedbackService.insert(userfeedback);
        addCallbackParam("msg", "反馈成功！");
    }


    

    public DataGridModel<UserFeedback> getDialogDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<UserFeedback>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 3090410626460776791L;

                @Override
                public List<UserFeedback> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                	
                	String sql = "select * from user_feedback where subappguid = ? order by operatedate desc";
                    
                    List<UserFeedback> feedbacks = iUserFeedbackService.findList(sql,subappGuid);
                    
                    for (UserFeedback back : feedbacks) {
                    	List<FrameAttachInfo> attachs = iAttachService.getAttachInfoListByGuid(back.getBackcliengguid());
                    	if (!attachs.isEmpty()) {
                    		FrameAttachInfo attachinfo = attachs.get(0);
                    		back.set("attachguid", attachinfo.getAttachGuid());
                    	}else {
                    		back.set("attachguid", "");
                    	}
                    }
                    this.setRowCount(feedbacks.size());
                    return feedbacks;
                }
            };
        }
        return model;
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

    public UserFeedback getDataBean() {
        return dataBean;
    }

    public void setDataBean(UserFeedback dataBean) {
        this.dataBean = dataBean;
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
    public String updateConsult(AuditOnlineConsult consult) {
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
    
    
    public FileUploadModel9 getApplyUploadModel() {
        if (applyUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                	applyUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            addViewData("applycliengguid", applycliengguid);
            applyUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(applycliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return applyUploadModel;
    }
    
    public FileUploadModel9 getYcUploadModel() {
        if (ycuploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                	ycuploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            addViewData("yscliengguid", yscliengguid);
            ycuploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(yscliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return ycuploadModel;
    }
    
    public FileUploadModel9 getLhchcUploadModel() {
        if (lhchcUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                	lhchcUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            addViewData("lhchcliengguid", lhchcliengguid);
            lhchcUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(lhchcliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return lhchcUploadModel;
    }

	
}
