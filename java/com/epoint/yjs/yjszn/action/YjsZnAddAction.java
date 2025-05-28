package com.epoint.yjs.yjszn.action;

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
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.yjs.yjszn.api.IYjsZnService;

/**
 * 一件事指南配置新增页面对应的后台
 *
 * @author panshunxing
 * @version [版本号, 2024-10-08 19:07:22]
 */
@RightRelation(YjsZnListAction.class)
@RestController("yjsznaddaction")
@Scope("request")
public class YjsZnAddAction extends BaseController {
    @Autowired
    private IYjsZnService service;
    /**
     * 一件事指南配置实体对象
     */
    private YjsZn dataBean = null;

    /**
     * 类型单选按钮组model
     */
    private List<SelectItem> typeModel = null;
    /**
     * 辖区下拉列表model
     */
    private List<SelectItem> areacodeModel = null;

    private FileUploadModel9 attachUploadModel;

    /**
     * 附件信息操作service
     */
    @Autowired
    private IAttachService frameattacinfonewservice;

    /**
     * 附件cliengguid
     */
    private String clengGuid = null;


    public void pageLoad() {
        dataBean = new YjsZn();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setAttachguid(getViewData("attachguid"));

        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new YjsZn();
    }

    public YjsZn getDataBean() {
        if (dataBean == null) {
            dataBean = new YjsZn();
        }
        return dataBean;
    }

    public void setDataBean(YjsZn dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "一件事指南类型", null, false));
        }
        return this.typeModel;
    }

    public List<SelectItem> getAreacodeModel() {
        if (areacodeModel == null) {
            areacodeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, false));
        }
        return this.areacodeModel;
    }

    public FileUploadModel9 getAttachUploadModel() {
        if (attachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("attachguid", storage.getAttachGuid());
                    }
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("attachguid"))) {
                this.addViewData("attachguid", dataBean.get("attachguid"));
            }
            dataBean.set("attachguid",this.getViewData("attachguid"));
            if (StringUtil.isNotBlank(this.getViewData("attachguid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("attachguid"));
                if (frameAttachInfo != null) {
                    clengGuid = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(clengGuid)) {
                attachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(clengGuid, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            } else {
                attachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return attachUploadModel;
    }

}
