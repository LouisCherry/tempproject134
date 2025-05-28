package com.epoint.evainstance.evainstance.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.evainstance.IEvainstanceService;
import com.epoint.evainstance.entity.Evainstance;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController("evainstanceattachlistaction")
@Scope("request")
public class EvainstanceAttachlistAction extends BaseController
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

    private String spimaterial;

    private Boolean isPaperSubmit;

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
    private IHandleSPIMaterial handleSpiMaterialService;

    @Autowired
    private IAuditSpIMaterial iauditspimaterial;
    @Autowired
    private IEvainstanceService service;
    @Override
    public void pageLoad() {
        AuditSpIMaterial auditspimaterial = iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
        if (auditspimaterial != null) {
            addCallbackParam("auditspimaterial", auditspimaterial);
        }
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            String cliengGuid = getRequestParameter("cliengGuid");
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            int count = list.size();
            attachCount = count;
            String rtnType = "add";
            if (count > 0) {
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

            attachUploadModel.getExtraDatas().put("msg", "上传成功");
            String cliengGuid = getRequestParameter("cliengGuid");
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            if(list.size()>0) {
                Evainstance eva = service.find(cliengGuid);
                eva.set("cliengGuid", "1");
                service.update(eva);
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
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid, "好差评上传", null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName())
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
                private static final long serialVersionUID = -5146727297689776456L;

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
        String cliengGuid = getRequestParameter("cliengGuid");
        List<FrameAttachInfo> list = getAttachList(cliengGuid);
        if(list.size()==0) {
            Evainstance eva = service.find(cliengGuid);
            eva.set("cliengGuid", null);
            service.update(eva);
        }
        
        addCallbackParam("msg", "成功删除！");
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        return frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
    }

    public void pass() {
        String msg = "";
        if (StringUtil.isNotBlank(spimaterial)) {
            AuditSpIMaterial auditspimaterial = iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
            if (auditspimaterial != null) {
                auditspimaterial.setIsbuzheng(ZwfwConstant.CONSTANT_STR_THREE);
                if (StringUtil.isNotBlank(auditspimaterial.getNopassreason())) {
                    auditspimaterial.setNopassreason(null);
                }
                if (StringUtil.isNotBlank(auditspimaterial.getStatus()) && isPaperSubmit) {
                    Integer oldStatus = Integer.parseInt(auditspimaterial.getStatus());
                    // 如果是只提交了电子件，则允许添加
                    if (oldStatus >= 10 && oldStatus <= 25 && (oldStatus % 10 != 5)) {
                        auditspimaterial.setStatus(String.valueOf(oldStatus + 5));
                    }
                }
                iauditspimaterial.updateSpIMaterial(auditspimaterial);
                msg = "审核成功！";
            }
            else {
                msg = "未获取到材料！";
            }
        }
        else {
            msg = "未获取到材料标识！";
        }
        addCallbackParam("msg", msg);
    }

    public void getMaterial() {
        if (StringUtil.isNotBlank(spimaterial)) {
            AuditSpIMaterial auditspimaterial = iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
            if (auditspimaterial != null) {
                addCallbackParam("auditspimaterial", auditspimaterial);
            }
        }
    }

    public void notpass() {
        String msg = "";
        if (StringUtil.isNotBlank(spimaterial)) {
            AuditSpIMaterial auditspimaterial = iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
            if (auditspimaterial != null) {
                auditspimaterial.setIsbuzheng(ZwfwConstant.CONSTANT_STR_ONE);
                iauditspimaterial.updateSpIMaterial(auditspimaterial);
                msg = "审核成功！";
            }
            else {
                msg = "未获取到材料！";
            }
        }
        else {
            msg = "未获取到材料标识！";
        }
        addCallbackParam("msg", msg);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPaperSubmit() {
        return isPaperSubmit;
    }

    public void setIsPaperSubmit(Boolean isPaperSubmit) {
        this.isPaperSubmit = isPaperSubmit;
    }

}
