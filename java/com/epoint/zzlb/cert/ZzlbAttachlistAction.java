package com.epoint.zzlb.cert;



import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.util.StringUtil;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.yyyz.auditspiyyyzmaterial.api.IAuditSpIYyyzMaterialService;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseResult;

@RestController("zzlbattachlistaction")
@Scope("request")
public class ZzlbAttachlistAction extends BaseController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /*
     * 操作类型
     */
    private String type;
    
    private int attachCount;
    
    private String projectGuid;
    
    private String biguid;
    
    private String certrowguid;
    
    private String taskid;
    
    private String cliengGuid;
    
    
    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;
    
    @Autowired
    private IAttachService frameAttachInfoService;

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;
    
    /**
     * 一业一证材料API
     */
    @Autowired
    private IAuditSpIYyyzMaterialService  auditSpIYyyzMaterialService;
    
	@Autowired
	private IBusinessLicenseResult businessLicenseResult;

    @Override
    public void pageLoad() {
    	projectGuid = getRequestParameter("projectGuid");
    	certrowguid = getRequestParameter("certrowguid");
    	taskid = getRequestParameter("taskid");
    	biguid = getRequestParameter("biguid");
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            cliengGuid = getRequestParameter("cliengGuid");
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            int count = list.size();
            attachCount=count;
            String rtnType = "add";
            if (count > 0){
                rtnType = "addanddel";
            }
            addCallbackParam("type", rtnType);
        }
        catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    AttachHandler9 handler = new AttachHandler9()
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
        public void afterSaveAttachToDB(Object attach) {
            attachUploadModel.getExtraDatas().put("msg", "上传成功");
            //如果原本为0，那么需要更新状态为电子提交或电子或纸质提交
            if(attachCount==0)
            {
            	
            }
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
            return true;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            String cliengGuid = getRequestParameter("cliengGuid");
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid,"一业一证结果上传",null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()){
				private static final long serialVersionUID = 1L;
				private IAttachService frameAttachInfoNewService = (IAttachService) ContainerFactory.getContainInfo().getComponent(IAttachService.class);
                @Override
                public List<FrameAttachInfo> getAllAttach() {
                        FrameAttachInfo info = new FrameAttachInfo();
                        info.setCliengGuid(cliengGuid);
                        info.setSortKey("uploadDateTime");
                        info.setSortDir("asc");
                        List<FrameAttachInfo> attachList = frameAttachInfoNewService.getAttachInfoList(info, (Date) null, (Date) null);
                        if (attachList == null) {
                            attachList = new ArrayList<FrameAttachInfo>();
                        }

                    return attachList;
                }
                @Override
                public int getAllAttachCount() {
                        FrameAttachInfo info = new FrameAttachInfo();
                        info.setCliengGuid(cliengGuid);
                        Integer attachCount = Integer
                                .valueOf(this.frameAttachInfoNewService.getAttachInfoCount(info, (Date) null, (Date) null));
                        if (attachCount == null) {
                            attachCount = Integer.valueOf(0);
                        }
                    return attachCount.intValue();
                }
            });
        }
        return attachUploadModel;
    }

    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 8339801819493001600L;

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String cliengGuid = getRequestParameter("cliengGuid");
                    List<FrameAttachInfo> list = getAttachList(cliengGuid);                
                    int count = list.size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public void delete() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            frameAttachInfoService.deleteAttachByAttachGuid(sel);
        }
        //如果删除的附件和当前所有的附件一致，那就说明已经没有电子件了，需要更新材料提交状态
        if(attachCount==select.size() && StringUtil.isBlank(taskid))
        {
            String sql = "select * from audit_sp_i_yyyz_material where cliengGuid = ?";
            AuditSpIYyyzMaterial auditSpIYyyzMaterial = auditSpIYyyzMaterialService.find(sql, cliengGuid);
            if(!"20".equals(auditSpIYyyzMaterial.getSubmittype())) {
                 if(!"10".equals(auditSpIYyyzMaterial.getStatus()) && !"15".equals(auditSpIYyyzMaterial.getStatus())) {
                     Integer status = Integer.parseInt(auditSpIYyyzMaterial.getStatus()) - 10;
                     auditSpIYyyzMaterial.setStatus(status.toString());
                 }
            }
            auditSpIYyyzMaterialService.update(auditSpIYyyzMaterial);
        }
        addCallbackParam("msg", "成功删除！");
    }
    /**
     * 
     *  [上传完文件，点击确认并关闭时候，修改一业一证主题实例材料表改材料的提交状态]     
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateStatus() {
        List<FrameAttachInfo> list = frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
        int attachNumber = list.size();
        String sql = "select * from audit_sp_i_yyyz_material where cliengGuid = ?";
        AuditSpIYyyzMaterial auditSpIYyyzMaterial = auditSpIYyyzMaterialService.find(sql, cliengGuid);
        if(attachNumber > 0 && !"20".equals(auditSpIYyyzMaterial.getSubmittype())) {
            if("10".equals(auditSpIYyyzMaterial.getStatus()) || "15".equals(auditSpIYyyzMaterial.getStatus())) {
                Integer status = Integer.parseInt(auditSpIYyyzMaterial.getStatus()) + 10;
                auditSpIYyyzMaterial.setStatus(status.toString());
            }
        }
        auditSpIYyyzMaterialService.update(auditSpIYyyzMaterial);
    }
    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        List<FrameAttachInfo> list = frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
