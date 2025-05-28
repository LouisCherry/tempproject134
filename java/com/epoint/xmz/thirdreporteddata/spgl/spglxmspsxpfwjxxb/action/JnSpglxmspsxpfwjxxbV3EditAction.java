package com.epoint.xmz.thirdreporteddata.spgl.spglxmspsxpfwjxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmspsxpfwjxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmspsxpfwjxxbV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 地方规划控制线信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:47]
 */
@RightRelation(JnSpglXmspsxpfwjxxbListV3Action.class)
@RestController("jnspglxmspsxpfwjxxbv3editaction")
@Scope("request")
public class JnSpglxmspsxpfwjxxbV3EditAction extends BaseController {
    @Autowired
    private ISpglXmspsxpfwjxxbV3 service;

    /**
     * 实体对象
     */
    private SpglXmspsxpfwjxxbV3 dataBean;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> spjglxModel = null;
    private FileUploadModel9 attachUploadModel;
    @Autowired
    private IAttachService attachService;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglXmspsxpfwjxxbV3();
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        if (dataBean != null) {
            dataBean.setOperatedate(new Date());
            dataBean.setSjsczt(0);
            dataBean.setSjyxbs(1);
            dataBean.set("sync", "0");
            dataBean.setSbyy(null);
            dataBean.set("pfwjyxqx", EpointDateUtil.convertDate2String(dataBean.getPfwjyxqx(), EpointDateUtil.DATE_FORMAT));
            List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(dataBean.getRowguid());
            if (!attachInfoList.isEmpty()) {
                FrameAttachInfo attachInfo = attachInfoList.get(0);
                dataBean.setFjid(attachInfo.getAttachGuid());
            }
            service.update(dataBean);
            addCallbackParam("msg", l("修改成功！"));

        } else {
            addCallbackParam("msg", l("查询实体成功！"));
        }
    }

    public FileUploadModel9 getAttachUploadModel() {
        if (attachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

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

    public SpglXmspsxpfwjxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglXmspsxpfwjxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglXmspsxpfwjxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSpjglxModel() {
        if (spjglxModel == null) {
            spjglxModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_审批结果类型", null, false));
        }
        return this.spjglxModel;
    }

}
