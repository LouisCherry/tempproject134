package com.epoint.union.auditunionproject.action;



import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;

@RestController("unionauditattachlistaction")
@Scope("request")
public class UnionAuditAttachlistAction extends BaseController
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
    
    private String submittype;

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
    
	@Autowired
	private IAuditUnionProjectMaterialService auditUnionProjectMaterialService;

    @Override
    public void pageLoad() {
    	projectGuid = getRequestParameter("projectGuid");
        submittype = getRequestParameter("submittype");
//        materialInstanceGuid = getRequestParameter("materialInstanceGuid");
        addCallbackParam("submittype", submittype);
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            String cliengGuid = getRequestParameter("cliengGuid");
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
            String materialInstanceGuid= getRequestParameter("materialInstanceGuid");
            //如果原本为0，那么需要更新状态为电子提交或电子或纸质提交
            if(attachCount==0)
            {
            	auditUnionProjectMaterialService.updateProjectMaterialStatus(materialInstanceGuid, projectGuid, "20");
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
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid,"异地通办上传",null, handler,
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
        String materialInstanceGuid= getRequestParameter("materialInstanceGuid");
        for (String sel : select) {
            frameAttachInfoService.deleteAttachByAttachGuid(sel);
        }
        //如果删除的附件和当前所有的附件一致，那就说明已经没有电子件了，需要更新材料提交状态
        if(attachCount==select.size())
        {
        	auditUnionProjectMaterialService.updateProjectMaterialStatus(materialInstanceGuid, projectGuid, "10");
        }
        addCallbackParam("msg", "成功删除！");
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
