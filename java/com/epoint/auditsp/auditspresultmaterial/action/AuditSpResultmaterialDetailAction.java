package com.epoint.auditsp.auditspresultmaterial.action;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspresultmaterial.api.IAuditSpResultmaterialService;
import com.epoint.auditsp.auditspresultmaterial.api.entity.AuditSpResultmaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

/**
 * 一件事结果材料详情页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:53]
 */
@RightRelation(AuditSpResultmaterialListAction.class)
@RestController("auditspresultmaterialdetailaction")
@Scope("request")
public class AuditSpResultmaterialDetailAction extends BaseController
{
    @Autowired
    private IAuditSpResultmaterialService service;

    /**
     * 一件事结果材料实体对象
     */
    private AuditSpResultmaterial dataBean = null;

    private FileUploadModel9 materialExampleTableModel;
    private String materialExampleTable = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpResultmaterial();
        }
        materialExampleTable = dataBean.getResultguid();
        if (StringUtil.isNotBlank(materialExampleTable)) {
            addViewData("materialExampleTable", materialExampleTable);
        }
        else {
            materialExampleTable = UUID.randomUUID().toString();
        }
    }

    public AuditSpResultmaterial getDataBean() {
        return dataBean;
    }

    public FileUploadModel9 getMaterialExampleTableModel() {
        if (materialExampleTableModel == null) {
            // clientGuid一般是地址中获取到的，此处只做参考使用
            AttachHandler9 handler = new AttachHandler9()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("materialExampleTable", storage.getCliengGuid());
                    }
                }

            };
            materialExampleTableModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(materialExampleTable,
                    null, null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        return materialExampleTableModel;
    }
}
