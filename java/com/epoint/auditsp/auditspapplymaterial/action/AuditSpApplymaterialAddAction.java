package com.epoint.auditsp.auditspapplymaterial.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspapplymaterial.api.IAuditSpApplymaterialService;
import com.epoint.auditsp.auditspapplymaterial.api.entity.AuditSpApplymaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 一件事申报材料新增页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:47]
 */
@RightRelation(AuditSpApplymaterialListAction.class)
@RestController("auditspapplymaterialaddaction")
@Scope("request")
public class AuditSpApplymaterialAddAction extends BaseController
{
    @Autowired
    private IAuditSpApplymaterialService service;
    /**
     * 一件事申报材料实体对象
     */
    private AuditSpApplymaterial dataBean = null;

    /**
     * 材料介质下拉列表model
     */
    private List<SelectItem> material_formModel = null;
    /**
     * 材料类型下拉列表model
     */
    private List<SelectItem> material_typeModel = null;
    /**
     * 来源渠道单选按钮组model
     */
    private List<SelectItem> source_typeModel = null;

    private FileUploadModel9 materialExampleTableModel;
    private String materialExampleTable = null;

    public void pageLoad() {
        dataBean = new AuditSpApplymaterial();

        materialExampleTable = dataBean.getExample_guid();
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
        dataBean.setExample_guid(this.getViewData("materialExampleTable"));

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
        dataBean = new AuditSpApplymaterial();
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

    public AuditSpApplymaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpApplymaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpApplymaterial dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getMaterial_formModel() {
        if (material_formModel == null) {
            material_formModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_材料形式", null, false));
        }
        return this.material_formModel;
    }

    public List<SelectItem> getMaterial_typeModel() {
        if (material_typeModel == null) {
            material_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_材料类型", null, false));
        }
        return this.material_typeModel;
    }

    public List<SelectItem> getSource_typeModel() {
        if (source_typeModel == null) {
            source_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "来源渠道", null, false));
        }
        return this.source_typeModel;
    }

}
