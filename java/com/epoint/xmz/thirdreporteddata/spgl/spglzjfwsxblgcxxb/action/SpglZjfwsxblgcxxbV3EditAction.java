package com.epoint.xmz.thirdreporteddata.spgl.spglzjfwsxblgcxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZjfwsxblgcxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZjfwsxblgcxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 中介服务事项办理过程信息表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:43]
 */
@RightRelation(SpglZjfwsxblgcxxbV3ListAction.class)
@RestController("spglzjfwsxblgcxxbv3editaction")
@Scope("request")
public class SpglZjfwsxblgcxxbV3EditAction extends BaseController {

    @Autowired
    private ISpglZjfwsxblgcxxbV3Service service;

    /**
     * 中介服务事项办理过程信息表实体对象
     */
    private SpglZjfwsxblgcxxbV3 dataBean = null;

    /**
     * 办理状态下拉列表model
     */
    private List<SelectItem> blztModel = null;
    /**
     * 数据有效标识下拉列表model
     */
    private List<SelectItem> sjyxbsModel = null;

    @Autowired
    private IAttachService iAttachService;

    private FileUploadModel9 attachUploadModel;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> sjscztModel = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglZjfwsxblgcxxbV3();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        dataBean.set("sync", "0");
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public void delete(String attachguid) {
        iAttachService.deleteAttachByAttachGuid(attachguid);
        addCallbackParam("msg", "删除成功！");
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
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage arg0) {

                    return true;
                }

            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getRowguid(), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    public SpglZjfwsxblgcxxbV3 getDataBean() {
        return dataBean;
    }

    public void setDataBean(SpglZjfwsxblgcxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSjscztModel() {
        if (sjscztModel == null) {
            sjscztModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sjscztModel;
    }

    public List<SelectItem> getBlztModel() {
        if (blztModel == null) {
            blztModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_中介服务事项办理状态", null, false));
        }
        return this.blztModel;
    }

    public List<SelectItem> getSjyxbsModel() {
        if (sjyxbsModel == null) {
            sjyxbsModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据有效标识", null, false));
        }
        return this.sjyxbsModel;
    }

}
