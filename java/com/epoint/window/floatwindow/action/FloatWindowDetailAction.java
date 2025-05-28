package com.epoint.window.floatwindow.action;

import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.window.floatwindow.api.entity.FloatWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.window.floatwindow.api.IFloatWindowService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 飘窗浮窗表详情页面对应的后台
 *
 * @author 86183
 * @version [版本号, 2023-07-17 10:15:09]
 */
@RightRelation(FloatWindowListAction.class)
@RestController("floatwindowdetailaction")
@Scope("request")
public class FloatWindowDetailAction extends BaseController {
    @Autowired
    private IFloatWindowService service;

    /**
     * 飘窗浮窗表实体对象
     */
    private FloatWindow dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new FloatWindow();
        }
        if (!isPostback()) {
            if (StringUtil.isNotBlank(dataBean.getImgclientguid())) {
                addViewData("attachguid", dataBean.getImgclientguid());
            }
        }
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
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getImgclientguid(), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }


    public FloatWindow getDataBean() {
        return dataBean;
    }
}