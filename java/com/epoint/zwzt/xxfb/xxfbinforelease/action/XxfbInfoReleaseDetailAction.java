package com.epoint.zwzt.xxfb.xxfbinforelease.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.zwzt.xxfb.constant.XxfbConstant;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.IXxfbInfoContentService;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity.XxfbInfoContent;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.IXxfbInfoReleaseService;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.entity.XxfbInfoRelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信息发布表详情页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-27 15:34:21]
 */
@RightRelation(XxfbInfoReleaseListAction.class)
@RestController("xxfbinforeleasedetailaction")
@Scope("request")
public class XxfbInfoReleaseDetailAction extends BaseController
{
    /**
     * 信息发布表service
     */
    @Autowired
    private IXxfbInfoReleaseService service;
    /**
     * 信息内容表service
     */
    @Autowired
    private IXxfbInfoContentService contentService;

    /**
     * 信息发布表实体对象
     */
    private XxfbInfoRelease dataBean = null;

    /**
     * 信息发布内容实体对象
     */
    private XxfbInfoContent content = null;
    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel;
    /**
     * 图片附件上传model
     */
    private FileUploadModel9 pictureUploadModel;
    /**
     * 视频附件上传model
     */
    private FileUploadModel9 videoUploadModel;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean != null) {
            if (StringUtil.isNotBlank(dataBean.getInfo_content_rowguid())) {
                content = contentService.find(dataBean.getInfo_content_rowguid());
            }
            else {
                content = new XxfbInfoContent();
            }
            if (StringUtil.isNotBlank(dataBean.getColumn_name())) {
                addCallbackParam("column_name", dataBean.getColumn_name());
            }
            if (StringUtil.isNotBlank(dataBean.getInfo_type())) {
                addCallbackParam("info_type", dataBean.getInfo_type());
            }
            boolean is_enablePicture = "1".equals(dataBean.getIs_enablePicture());
            if (StringUtil.isNotBlank(dataBean.getIs_enablePicture())) {
                addCallbackParam("is_EnablePicture", is_enablePicture);
            }
            // 构造图片与预览内容列表
            if (is_enablePicture) {
                String rootPath = WebUtil.getRequestRootUrl(WebUtil.getRequest());
                if (StringUtil.isNotBlank(dataBean.getPicture_guids())) {
                    String[] picture_guids = dataBean.getPicture_guids().split(",");
                    JSONArray pictureItems = new JSONArray();
                    for (String picture_guid : picture_guids) {
                        JSONObject pictureItem = new JSONObject();
                        pictureItem.put("downloadUrl",
                                rootPath + "/rest/attachAction.action?cmd=getContent&attachGuid=" + picture_guid);
                        pictureItems.add(pictureItem);
                    }
                    addCallbackParam("pictureItems", pictureItems);
                }
            }
        }
        else {
            dataBean = new XxfbInfoRelease();
        }
        addCallbackParam("isEnablePublisher", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_PUBLISHER__IS));
        addCallbackParam("isEnableIsHot", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_ISHOT__IS));
        addCallbackParam("isEnableInfoAuthor", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_INFOAUTHOR__IS));
        addCallbackParam("isEnableDeadline", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_DEADLINE_IS));
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getRowguid(), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));

        }
        return fileUploadModel;
    }

    public FileUploadModel9 getPictureUploadModel() {
        if (pictureUploadModel == null) {
            pictureUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(dataBean.getRowguid() + "picture", null, null, null,
                            userSession.getUserGuid(), userSession.getDisplayName()));

        }
        return pictureUploadModel;
    }

    public FileUploadModel9 getVideoUploadModel() {
        if (videoUploadModel == null) {
            videoUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getRowguid() + "video",
                    null, null, null, userSession.getUserGuid(), userSession.getDisplayName()));

        }
        return videoUploadModel;
    }

    public XxfbInfoRelease getDataBean() {
        return dataBean;
    }

    public XxfbInfoContent getContent() {
        return content;
    }

    public void setContent(XxfbInfoContent content) {
        this.content = content;
    }
}
