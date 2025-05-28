package com.epoint.xmz.onlinetaskconfig.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.onlinetaskconfig.api.IOnlinetaskConfigService;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 居民办事配置新增页面对应的后台
 *
 * @author RaoShaoliang
 * @version [版本号, 2023-10-17 15:38:09]
 */
@RightRelation(OnlinetaskConfigListAction.class)
@RestController("onlinetaskconfigaddaction")
@Scope("request")
public class OnlinetaskConfigAddAction extends BaseController {
    @Autowired
    private IOnlinetaskConfigService service;

    @Autowired
    private IAttachService frameattacinfonewservice;

    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 居民办事配置实体对象
     */
    private OnlinetaskConfig dataBean = null;

    /**
     * 所属分类下拉列表model
     */
    private List<SelectItem> kindModel = null;
    private List<SelectItem> infokindModel = null;

    /**
     * 所属b部门下拉列表model
     */
    private List<SelectItem> ounameModel = null;
    private FileUploadModel9 fileUploadModel9;
    private String clientGuid = null;

    public void pageLoad() {
        dataBean = new OnlinetaskConfig();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        if (StringUtil.isNotBlank(this.getViewData("clientGuid"))){
            dataBean.setClientguid(this.getViewData("clientGuid"));
        }else {
            dataBean.setClientguid(UUID.randomUUID().toString());
        }

        dataBean.setOperateusername(userSession.getDisplayName());
        String ouname = iCodeItemsService.getItemTextByCodeName("居民办事部门", dataBean.getOuguid());
        dataBean.setOuname(ouname);
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new OnlinetaskConfig();
    }


    public FileUploadModel9 getFileUploadModel9() {
        if (fileUploadModel9 == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("clientGuid", storage.getAttachGuid());
                    }
                    fileUploadModel9.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            /* if(StringUtil.isBlank(this.getViewData("taskoutimgguid"))){
                this.addViewData("taskoutimgguid", dataBean.getTaskoutimgguid());
            }*/
            dataBean.setClientguid(this.getViewData("clientGuid"));
            if (StringUtil.isNotBlank(this.getViewData("clientGuid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("clientGuid"));
                if (frameAttachInfo != null) {
                    clientGuid = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(clientGuid)) {
                fileUploadModel9 = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(clientGuid, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
            else {
                fileUploadModel9 = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return fileUploadModel9;
    }

    public OnlinetaskConfig getDataBean() {
        if (dataBean == null) {
            dataBean = new OnlinetaskConfig();
        }
        return dataBean;
    }

    public void setDataBean(OnlinetaskConfig dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getKindModel() {
        if (kindModel == null) {
            kindModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "居民办事分类", null, false));
        }
        return this.kindModel;
    }

    public List<SelectItem> getInfokindModel() {
        if (infokindModel == null) {
            infokindModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "居民办事信息类型", null, false));
        }
        return this.infokindModel;
    }

    public List<SelectItem> getOunameModel() {
        if (ounameModel == null) {
            ounameModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "居民办事部门", null, true));
        }
        return this.ounameModel;
    }
    
    
}
