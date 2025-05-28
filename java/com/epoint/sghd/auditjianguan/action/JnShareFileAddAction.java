package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 政策文件上传action
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnsharefileaddaction")
@Scope("request")
public class JnShareFileAddAction extends BaseController {
    private static final long serialVersionUID = 1L;

    private AuditTaskShareFile dataBean;

    private String fileclientguid;

    @Autowired
    private IAttachService attachSerivce;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    private RenlingService rlService = new RenlingService();

    //male  alert sql实现service
    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Override
    public void pageLoad() {
        this.dataBean = new AuditTaskShareFile();
        if (StringUtil.isNotBlank(this.dataBean.getFileclientguid())) {
            this.fileclientguid = this.dataBean.getFileclientguid();
            this.addViewData("fileclientguid", this.fileclientguid);
        }

        if (StringUtil.isBlank(this.getViewData("fileclientguid"))) {
            this.fileclientguid = UUID.randomUUID().toString();
            this.addViewData("fileclientguid", this.fileclientguid);
        }
    }

    public void add() {
        if (StringUtil.isNotBlank(this.getViewData("fileclientguid"))) {
            int k = rlService.getShareFileIsOn(this.dataBean.getTaskid());
            if (k > 0) {
                addCallbackParam("error", "该事项已上传过文件，请另择事项！");
            } else {
                List taskversion = attachSerivce.getAttachInfoListByGuid(this.getViewData("fileclientguid"));
                if (taskversion != null && taskversion.size() > 0) {
                    Date date = new Date();
                    this.dataBean.setFileclientguid(this.getViewData("fileclientguid"));
                    this.dataBean.setRowguid(UUID.randomUUID().toString());
                    this.dataBean.setCreatetime(date);
                    this.dataBean.setUpdatetime(date);
                    this.dataBean.setOuguid(userSession.getBaseOUGuid());

                    jnAuditJianGuanService.insert(this.dataBean);
                    addCallbackParam("msg", "添加成功！");
                } else {
                    addCallbackParam("error", "请选择上传文件！");
                }
            }
        } else {
            addCallbackParam("error", "请选择上传文件！");
        }
    }

    public void addNew() {
        this.add();
        this.dataBean = new AuditTaskShareFile();
    }

    public AuditTaskShareFile getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTaskShareFile dataBean) {
        this.dataBean = dataBean;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9() {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("fileclientguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
}
