package com.epoint.window.floatwindow.action;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.window.floatwindow.api.entity.FloatWindow;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.window.floatwindow.api.IFloatWindowService;

/**
 * 飘窗浮窗表新增页面对应的后台
 *
 * @author 86183
 * @version [版本号, 2023-07-17 10:15:09]
 */
@RightRelation(FloatWindowListAction.class)
@RestController("floatwindowaddaction")
@Scope("request")
public class FloatWindowAddAction extends BaseController {
    @Autowired
    private IFloatWindowService service;
    /**
     * 飘窗浮窗表实体对象
     */
    private FloatWindow dataBean = null;

    /**
     * 所属站点下拉列表model
     */
    private List<SelectItem> siteModel = null;
    /**
     * 功能类型下拉列表model
     */
    private List<SelectItem> typeModel = null;
    /**
     * 飘窗浮窗启用状态下拉列表model
     */
    private List<SelectItem> startModel = null;


    public void pageLoad() {

        dataBean = new FloatWindow();
        dataBean.setStart("1");

        if (!isPostback()) {
            if (StringUtil.isNotBlank(dataBean.getImgclientguid())) {
                addViewData("attachguid", dataBean.getImgclientguid());
            }
            else {
                addViewData("attachguid", UUID.randomUUID().toString());
            }
        }

    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setImgclientguid(getViewData("attachguid"));
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new FloatWindow();
    }

    public FloatWindow getDataBean() {
        if (dataBean == null) {
            dataBean = new FloatWindow();
        }
        return dataBean;
    }

    private FileUploadModel9 attachUploadModel;
    public FileUploadModel9 getAttachUploadModel() {
        if (attachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");
                }
                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }
            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("attachguid"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }



    public void setDataBean(FloatWindow dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSiteModel() {
        if (siteModel == null) {
            siteModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属站点", null, false));
        }
        return this.siteModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "功能类型", null, false));
        }
        return this.typeModel;
    }

    public List<SelectItem> getStartModel() {
        if (startModel == null) {
            startModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "飘窗浮窗启用状态", null, false));
        }
        return this.startModel;
    }

}
