package com.epoint.yjs.yjszn.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;

/**
 * 一件事指南配置修改页面对应的后台
 *
 * @author panshunxing
 * @version [版本号, 2024-10-08 19:07:22]
 */
@RightRelation(YjsZnListAction.class)
@RestController("yjszneditaction")
@Scope("request")
public class YjsZnEditAction extends BaseController {

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

    private FileUploadModel9 attachUploadModel1;

    /**
     * 附件信息操作service
     */
    @Autowired
    private IAttachService frameattacinfonewservice;

    /**
     * 附件cliengguid
     */
    private String clengGuid = null;

    /**
     * 附件cliengguid
     */
    private String clengGuid1 = null;
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new YjsZn();
        }else{
            if(!isPostback()){
                if (StringUtil.isNotBlank(dataBean.getAttachguid())){
                    addViewData("attachguid", dataBean.getAttachguid());
                }
            }
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setAttachguid(getViewData("attachguid"));

        //保存图片的第一帧
        String imgattachguid = "";
        try{
            if(StringUtils.isNotBlank(getViewData("attachguid"))){

                if(StringUtil.isNotBlank(dataBean.get("imgattachguid"))){
                    imgattachguid = dataBean.get("imgattachguid");
                }
                FrameAttachInfo frameAttachInfo = frameattacinfonewservice.getAttachInfoDetail(getViewData("attachguid"));
                if(frameAttachInfo!=null){
                    InputStream inputStream =  frameattacinfonewservice.getInputStreamByInfo(frameAttachInfo);
                    if(inputStream!=null){
                        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream)) {
                            grabber.start();
                            // 获取第一帧
                            Frame frame = grabber.grabImage();
                            if (frame!= null) {
                                Java2DFrameConverter converter = new Java2DFrameConverter();
                                BufferedImage bufferedImage = converter.getBufferedImage(frame);
                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                ImageIO.write(bufferedImage, "png", os);
                                InputStream is = new ByteArrayInputStream(os.toByteArray());
                                if(StringUtils.isNotBlank(imgattachguid)){
                                    frameattacinfonewservice.deleteAttachByAttachGuid(imgattachguid);
                                }else{
                                    imgattachguid = UUID.randomUUID().toString();
                                }
                                String fileName =imgattachguid+".png";

                                FrameAttachInfo frameAttachInfonew = new FrameAttachInfo();
                                frameAttachInfonew.setAttachGuid(imgattachguid);
                                frameAttachInfonew.setCliengGuid(UUID.randomUUID().toString());
                                frameAttachInfonew.setAttachFileName(fileName);
                                frameAttachInfonew.setCliengTag("视频生成");
                                frameAttachInfonew.setUploadUserGuid(userSession.getUserGuid());
                                frameAttachInfonew.setUploadUserDisplayName(userSession.getDisplayName());
                                frameAttachInfonew.setUploadDateTime(new Date());
                                frameattacinfonewservice.addAttach(frameAttachInfonew, is);
                            } else {
                                log.info("未能获取到视频的有效帧。");
                            }
                            grabber.stop();
                        } catch (UnsatisfiedLinkError e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(StringUtils.isNotBlank(imgattachguid)){
            dataBean.set("imgattachguid", imgattachguid);
        }else{
            dataBean.set("imgattachguid", getViewData("imgattachguid"));
        }
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public YjsZn getDataBean() {
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
                        dataBean.setAttachguid(storage.getAttachGuid());
                    }
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("attachguid"))&& StringUtils.isNotBlank(dataBean.get("attachguid"))) {
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

    public FileUploadModel9 getAttachUploadModel1() {
        if (attachUploadModel1 == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("imgattachguid", storage.getAttachGuid());
                        dataBean.set("imgattachguid",storage.getAttachGuid());
                    }
                    attachUploadModel1.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("imgattachguid"))&& StringUtils.isNotBlank(dataBean.get("imgattachguid"))) {
                this.addViewData("imgattachguid", dataBean.get("imgattachguid"));
            }
            dataBean.set("imgattachguid",this.getViewData("imgattachguid"));
            if (StringUtil.isNotBlank(this.getViewData("imgattachguid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("imgattachguid"));
                if (frameAttachInfo != null) {
                    clengGuid1 = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(clengGuid1)) {
                attachUploadModel1 = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(clengGuid1, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            } else {
                attachUploadModel1 = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return attachUploadModel1;
    }

}
