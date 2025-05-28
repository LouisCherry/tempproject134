package com.epoint.xmz.hongxiaobangallocation.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.hongxiaobangallocation.api.IHongxiaobangAllocationService;
import com.epoint.xmz.hongxiaobangallocation.api.entity.HongxiaobangAllocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 红小帮配置表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-05-20 17:08:27]
 */
@RestController("hongxiaobangallocationaddaction")
@Scope("request")
public class HongxiaobangAllocationAddAction extends BaseController {
    @Autowired
    private IHongxiaobangAllocationService service;
    /**
     * 红小帮配置表实体对象
     */
    private HongxiaobangAllocation dataBean = null;

    /**
     * 上传图片modul
     */
    private FileUploadModel9 picUploadModel;

    /**
     * 文件附件标识
     */
    private String piccliengguid;

    public void pageLoad() {
        dataBean = service.getAllocation();
        if (dataBean == null) {
            dataBean = new HongxiaobangAllocation();
            if (StringUtil.isBlank(getViewData("piccliengguid"))) {
                piccliengguid = UUID.randomUUID().toString();
                addViewData("piccliengguid", piccliengguid);
            } else {
                piccliengguid = getViewData("piccliengguid");
            }
        } else {
            if (StringUtil.isNotBlank(dataBean.getPictureguid())) {
                piccliengguid = dataBean.getPictureguid();
            } else {
                if (StringUtil.isBlank(getViewData("piccliengguid"))) {
                    piccliengguid = UUID.randomUUID().toString();
                    addViewData("piccliengguid", piccliengguid);
                } else {
                    piccliengguid = getViewData("piccliengguid");
                }
            }
        }


    }

    /**
     * 保存并关闭
     */
    public void add() {
        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setPictureguid(piccliengguid);
            service.update(dataBean);
        } else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setPictureguid(piccliengguid);
            service.insert(dataBean);
        }
        addCallbackParam("msg", l("保存成功！"));
    }


    /**
     * [图片上传]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getPicUploadModel() {
        if (picUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    picUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }
            };
            picUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(piccliengguid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return picUploadModel;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new HongxiaobangAllocation();
    }

    public HongxiaobangAllocation getDataBean() {
        if (dataBean == null) {
            dataBean = new HongxiaobangAllocation();
        }
        return dataBean;
    }

    public void setDataBean(HongxiaobangAllocation dataBean) {
        this.dataBean = dataBean;
    }


}
