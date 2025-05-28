package com.epoint.auditsp.auditspresultmaterial.action;

import java.util.Date;
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
 * 一件事结果材料新增页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:53]
 */
@RightRelation(AuditSpResultmaterialListAction.class)
@RestController("auditspresultmaterialaddaction")
@Scope("request")
public class AuditSpResultmaterialAddAction extends BaseController
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
        dataBean = new AuditSpResultmaterial();

        materialExampleTable = dataBean.getResultguid();
        if (StringUtil.isNotBlank(materialExampleTable)) {
            addViewData("materialExampleTable", materialExampleTable);
        }
        else {
            materialExampleTable = UUID.randomUUID().toString();
        }
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setBusinessguid(getRequestParameter("businessguid"));

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setResultguid(this.getViewData("materialExampleTable"));

        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditSpResultmaterial();
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

    public AuditSpResultmaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpResultmaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpResultmaterial dataBean) {
        this.dataBean = dataBean;
    }

}
