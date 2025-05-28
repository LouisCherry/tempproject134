package com.epoint.xmz.attachupload.action;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

@RestController("auditspattachaction")
@Scope("request")
public class AuditSpAttachAction extends BaseController
{
     
    /**
     * 
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /*
     * 操作类型
     */
    private String type;
    
    private int attachCount;
    
    
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
    
    
    
    @Override
    public void pageLoad() {
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
            e.printStackTrace();
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
           
          
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
            return true;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            String cliengGuid = getRequestParameter("cliengGuid");
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid,"综管上传",null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()){
                private static final long serialVersionUID = 1L;
                private IAttachService frameAttachInfoNewService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
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
                        Integer attachcount = Integer
                                .valueOf(this.frameAttachInfoNewService.getAttachInfoCount(info, (Date) null, (Date) null));
                        if (attachcount == null) {
                            attachcount = Integer.valueOf(0);
                        }
                    return attachcount.intValue();
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
        addCallbackParam("msg", "成功删除！");
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        return frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
