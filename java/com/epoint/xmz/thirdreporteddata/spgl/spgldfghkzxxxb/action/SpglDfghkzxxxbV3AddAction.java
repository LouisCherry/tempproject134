package com.epoint.xmz.thirdreporteddata.spgl.spgldfghkzxxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglDfghkzxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglDfghkzxxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 地方规划控制线信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:47]
 */
@RightRelation(SpglDfghkzxxxbV3ListAction.class)
@RestController("spgldfghkzxxxbv3addaction")
@Scope("request")
public class SpglDfghkzxxxbV3AddAction extends BaseController {
    @Autowired
    private ISpglDfghkzxxxbV3Service service;
    /**
     * 地方规划控制线信息表实体对象
     */
    private SpglDfghkzxxxbV3 dataBean = null;

    /**
     * 数据上传状态下拉列表model
     */
    private List<SelectItem> sjscztModel = null;
    private FileUploadModel9 attachUploadModel;
    @Autowired
    private IAttachService attachService;

    public void pageLoad() {
        dataBean = new SpglDfghkzxxxbV3();
        if (!isPostback()) {
            String rowguid = UUID.randomUUID().toString();
            addViewData("rowguid", rowguid);
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(getViewData("rowguid"));
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setDfsjzj(UUID.randomUUID().toString());
        dataBean.setXzqhdm("370800");
        dataBean.setSjsczt(0);
        dataBean.setSjyxbs(1);
        List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(getViewData("rowguid"));
        if (attachInfoList.size() > 0) {
            FrameAttachInfo attachInfo = attachInfoList.get(0);
            InputStream input = attachService.getAttachByInfo(attachInfo).getContent();
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream(); // 创建字节数组输出流对象

                byte[] buffer = new byte[4096]; // 定义缓冲区大小
                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) { // 从输入流读取数据到缓冲区
                    output.write(buffer, 0, bytesRead); // 写入字节数组输出流中
                }

                byte[] result = output.toByteArray(); // 获取字节数组结果
                dataBean.setKzxwjfj(result);
                // 关闭输入流和输出流
                input.close();
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } // 从输入流读取数据到缓冲区
        }

        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new SpglDfghkzxxxbV3();
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
//                    attachService.deleteAttachByGuid(dataBean.getRowguid());
                    return true;
                }
            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("rowguid"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    public SpglDfghkzxxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglDfghkzxxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglDfghkzxxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getSjscztModel() {
        if (sjscztModel == null) {
            sjscztModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_数据上传状态", null, false));
        }
        return this.sjscztModel;
    }

}
