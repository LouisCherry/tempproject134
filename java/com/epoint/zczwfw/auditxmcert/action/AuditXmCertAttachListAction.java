package com.epoint.zczwfw.auditxmcert.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 
 * [项目证照上传列表]
 * 
 * @author 吕闯
 * @version 2022年6月13日
 */
@RestController("auditxmcertattachlistaction")
@Scope("request")
public class AuditXmCertAttachListAction extends BaseController
{

    /**
     * 序列号
     */
    private static final long serialVersionUID = 1L;

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

    private String cliengGuid = "";

    @Override
    public void pageLoad() {
        cliengGuid = getRequestParameter("cliengGuid");
        // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
        List<FrameAttachInfo> list = getAttachList(cliengGuid);
        int count = list.size();
        addCallbackParam("num", count);
    }

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid, "项目证照上传", null, null,
                    userSession.getUserGuid(), userSession.getDisplayName())
            {
                private static final long serialVersionUID = 1L;
                private IAttachService frameAttachInfoNewService = ContainerFactory.getContainInfo()
                        .getComponent(IAttachService.class);

                @Override
                public List<FrameAttachInfo> getAllAttach() {
                    FrameAttachInfo info = new FrameAttachInfo();
                    info.setCliengGuid(cliengGuid);
                    info.setSortKey("uploadDateTime");
                    info.setSortDir("asc");
                    List<FrameAttachInfo> attachList = frameAttachInfoNewService.getAttachInfoList(info, (Date) null,
                            (Date) null);
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
                    List<FrameAttachInfo> list = getAttachList(cliengGuid);
                    list = getAttachList(cliengGuid);
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
}
