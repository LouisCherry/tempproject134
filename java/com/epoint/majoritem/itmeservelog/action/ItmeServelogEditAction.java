package com.epoint.majoritem.itmeservelog.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.majoritem.itmeservelog.api.IItmeServelogService;
import com.epoint.majoritem.itmeservelog.api.entity.ItmeServelog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 重点项目服务记录表修改页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:57]
 */
@RightRelation(ItmeServelogListAction.class)
@RestController("itmeservelogeditaction")
@Scope("request")
public class ItmeServelogEditAction extends BaseController {

    @Autowired
    private IItmeServelogService service;

    /**
     * 重点项目服务记录表实体对象
     */
    private ItmeServelog dataBean = null;
    /**
     * 附件上传model
     */
    private FileUploadModel9 photoUploadModel = null;

    /**
     * 附件上传model
     */
    private FileUploadModel9 zmclUploadModel = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ItmeServelog();
        }
        if (StringUtil.isNotBlank(dataBean.getSgxcclientguid())) {
            addViewData("sgxcclientguid", dataBean.getSgxcclientguid());
        }
        else {
            addViewData("sgxcclientguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isNotBlank(dataBean.getSplctclientguid())) {
            addViewData("splctclientguid", dataBean.getSplctclientguid());
        }
        else {
            addViewData("splctclientguid", UUID.randomUUID().toString());
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        if (StringUtil.isBlank(getViewData("sgxcclientguid"))) {
            addViewData("sgxcclientguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("splctclientguid"))) {
            addViewData("splctclientguid", UUID.randomUUID().toString());
        }
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public FileUploadModel9 getPhotoUploadModel() {
        if (photoUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    photoUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            photoUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("sgxcclientguid"), null,
                    null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return photoUploadModel;
    }

    public FileUploadModel9 getZmclUploadModel() {
        if (zmclUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    zmclUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            zmclUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("splctclientguid"),
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return zmclUploadModel;
    }

    public ItmeServelog getDataBean() {
        return dataBean;
    }

    public void setDataBean(ItmeServelog dataBean) {
        this.dataBean = dataBean;
    }

}
