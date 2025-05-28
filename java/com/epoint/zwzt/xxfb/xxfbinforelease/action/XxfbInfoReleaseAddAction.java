package com.epoint.zwzt.xxfb.xxfbinforelease.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.TreeData;
import com.epoint.basic.faces.tree.TreeFunction9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.base.TreeNode;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zwzt.xxfb.constant.XxfbConstant;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.IXxfbInfoContentService;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity.XxfbInfoContent;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.IXxfbInfoReleaseService;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.entity.XxfbInfoRelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 信息发布表新增页面对应的后台
 *
 * @author D0Be
 * @version [版本号, 2022-04-27 15:34:21]
 */
@RightRelation(XxfbInfoReleaseListAction.class)
@RestController("xxfbinforeleaseaddaction")
@Scope("request")
public class XxfbInfoReleaseAddAction extends BaseController
{
    /**
     * 信息发布表service
     */
    @Autowired
    private IXxfbInfoReleaseService service;
    /**
     * 信息发布表service
     */
    @Autowired
    private IXxfbInfoColumnService columnService;
    /**
     * 信息内容表service
     */
    @Autowired
    private IXxfbInfoContentService contentService;
    /**
     * 附件信息service
     */
    @Autowired
    private IAttachService attachService;
    /**
     * 信息发布表实体对象
     */
    private XxfbInfoRelease dataBean = null;

    /**
     * 信息发布内容实体对象
     */
    private XxfbInfoContent content = null;
    /**
     * 发布人类型下拉列表model
     */
    private List<SelectItem> publisher_typeModel = null;
    /**
     * 是否置顶单选按钮组model
     */
    private List<SelectItem> is_topModel = null;
    /**
     * 信息类型下拉列表model
     */
    private List<SelectItem> info_typeModel = null;
    /**
     * 信息状态下拉列表model
     */
    private List<SelectItem> info_statusModel = null;
    /**
     * 是否热门下拉列表model
     */
    private List<SelectItem> is_hotModel = null;
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
    /**
     * 信息所属栏目树
     */
    private TreeModel treeModel;

    /**
     * 信息所属栏目标识
     */
    private String column_guid;

    /**
     * 信息发布标识
     */
    private String rowguid;

    /**
     * 信息内容标识
     */
    private String contentGuid;

    public void pageLoad() {
        column_guid = getRequestParameter("column_guid");
        dataBean = new XxfbInfoRelease();
        content = new XxfbInfoContent();
        if (!isPostback()) {
            rowguid = UUID.randomUUID().toString();
            contentGuid = UUID.randomUUID().toString();
            addViewData("rowguid", rowguid);
            addViewData("contentGuid", contentGuid);
            if (StringUtil.isNotBlank(column_guid)) {
                dataBean.setColumn_guid(column_guid);
                XxfbInfoColumn parent = columnService.find(column_guid);
                if (parent != null) {
                    dataBean.setColumn_name(parent.getInfo_column_name());
                    addCallbackParam("parentName", parent.getInfo_column_name());
                }
            }
            dataBean.setIs_enablePicture("0");
            dataBean.setOrdernum(0);
            Date now = new Date();
            dataBean.setCreate_time(now);
            dataBean.setDead_line(now);
            dataBean.setCreate_userguid(userSession.getUserGuid());
            dataBean.setCreate_username(userSession.getDisplayName());
            dataBean.setPublisher(userSession.getDisplayName());
            dataBean.setPublisher_type("10");
        }
        rowguid = getViewData("rowguid");
        contentGuid = getViewData("contentGuid");
        dataBean.setInfo_content_rowguid(contentGuid);
        boolean isEnablePublisher = "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_PUBLISHER__IS);
        if (isEnablePublisher) {
            addCallbackParam("currentOuName", userSession.getOuName());
            addCallbackParam("currentUserName", userSession.getDisplayName());
        }
        addCallbackParam("isEnablePublisher", isEnablePublisher);
        addCallbackParam("isEnableIsHot", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_ISHOT__IS));
        addCallbackParam("isEnableInfoAuthor", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_INFOAUTHOR__IS));
        addCallbackParam("isEnableDeadline", "1".equals(XxfbConstant.ZWZT_XXFB_INFO_RELEASE_DEADLINE_IS));

    }

    /**
     * 生成栏目树
     *
     * @return 栏目树
     */
    public TreeModel getTreeModel() {
        if (treeModel == null) {
            treeModel = new TreeModel()
            {

                public List<TreeNode> fetch(TreeNode treeNode) {
                    TreeData treeData = TreeFunction9.getData(treeNode);
                    List<TreeNode> list = new ArrayList<>();

                    // 首次加载树结构
                    if (treeData == null) {
                        TreeNode root = new TreeNode();
                        root.setText("所有栏目");
                        root.setId("f9root");
                        root.setPid("-1");
                        root.setCkr(false);
                        list.add(root);
                        root.setExpanded(true);// 展开下一层节点
                        list.addAll(fetch(root));// 自动加载下一层树结构
                    }
                    // 每次点击树节点前的加号，进行加载
                    else {
                        String objectGuid = treeData.getObjectGuid();
                        SqlConditionUtil sqlUtil = new SqlConditionUtil();
                        sqlUtil.eq("parent_column_rowguid", StringUtil.isNotBlank(objectGuid) ? objectGuid : "f9root");
                        sqlUtil.setOrderAsc("create_time");
                        List<XxfbInfoColumn> columnList = columnService.findList(sqlUtil.getMap());

                        // 部门的绑定
                        for (int i = 0; i < columnList.size(); i++) {
                            TreeNode node = new TreeNode();
                            node.setId(columnList.get(i).getRowguid());
                            node.setText(columnList.get(i).getInfo_column_name());
                            node.setPid(columnList.get(i).getParent_column_rowguid());
                            node.setCkr(true);
                            node.setLeaf(true);
                            sqlUtil.clear();
                            sqlUtil.eq("parent_column_rowguid", columnList.get(i).getRowguid());
                            List<XxfbInfoColumn> subList = columnService.findList(sqlUtil.getMap());
                            if (!subList.isEmpty()) {
                                node.setLeaf(false);
                            }
                            list.add(node);
                        }
                    }
                    return list;
                }
            };
        }
        return treeModel;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(rowguid, null, null, null,
                    userSession.getUserGuid(), userSession.getDisplayName()));

        }
        return fileUploadModel;
    }

    public FileUploadModel9 getPictureUploadModel() {
        if (pictureUploadModel == null) {
            pictureUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(rowguid + "picture", null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return pictureUploadModel;
    }

    public FileUploadModel9 getVideoUploadModel() {
        if (videoUploadModel == null) {
            videoUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(rowguid + "video", null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));

        }
        return videoUploadModel;
    }

    /**
     * 保存并关闭
     */
    public void add() {
        generateDateBean();
        dataBean.setInfo_status("5");
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并发布
     */
    public void addAndPublish() {
        generateDateBean();
        dataBean.setInfo_status("10");
        service.insert(dataBean);
        addCallbackParam("msg", "发布成功！");
        dataBean = null;
    }

    /**
     * 构造信息发布的数据dataBean
     */
    private void generateDateBean() {
        Date now = new Date();
        dataBean.setRowguid(rowguid);
        dataBean.setOperatedate(now);
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCreate_time(now);
        dataBean.setCreate_userguid(userSession.getUserGuid());
        dataBean.setCreate_username(userSession.getDisplayName());
        // 设置信息的所属栏目
        if (StringUtil.isNotBlank(dataBean.getColumn_guid())) {
            XxfbInfoColumn column = columnService.find(dataBean.getColumn_guid());
            if (column != null && !column.isEmpty() && StringUtil.isNotBlank(column.getInfo_column_name())) {
                dataBean.setColumn_name(column.getInfo_column_name());
            }
        }
        // 若存在内容则设置信息的的内容
        if (StringUtil.isNotBlank(content.getInfo_content())) {
            content.setRowguid(contentGuid);
            content.setOperatedate(now);
            content.setOperateusername(userSession.getDisplayName());
            contentService.insert(content);
            dataBean.setInfo_content_rowguid(contentGuid);
        }
        // 根据信息的类型设置信息的内容
        if (StringUtil.isNotBlank(dataBean.getInfo_type())) {
            String clientGuid = "";
            // 信息类型为附件
            if ("50".equals(dataBean.getInfo_type())||"10".equals(dataBean.getInfo_type())) {
                clientGuid = rowguid;
            }
            // 信息类型为视频
            else if ("70".equals(dataBean.getInfo_type())) {
                clientGuid = rowguid + "video";
            }
            if (StringUtil.isNotBlank(clientGuid)) {
                List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(clientGuid);
                List<String> guidList = attachInfoList.stream().map(FrameAttachInfo::getAttachGuid)
                        .collect(Collectors.toList());
                if (guidList != null && !guidList.isEmpty()) {
                    if ("70".equals(dataBean.getInfo_type())) {
                        dataBean.setVedio_guid(StringUtil.join(guidList, ","));
                    }
                    else {
                        dataBean.setEnclosure_guids(StringUtil.join(guidList, ","));
                    }
                }
            }
        }
        // 根据是否启用图片设置信息的图片内容
        if (StringUtil.isNotBlank(dataBean.getIs_enablePicture()) && "1".equals(dataBean.getIs_enablePicture())) {
            List<FrameAttachInfo> pictureList = attachService.getAttachInfoListByGuid(rowguid + "picture");
            List<String> guidList = pictureList.stream().map(FrameAttachInfo::getAttachGuid)
                    .collect(Collectors.toList());
            if (guidList != null && !guidList.isEmpty()) {
                dataBean.setPicture_guids(StringUtil.join(guidList, ","));
            }
        }
        // 设置信息未非置顶
        dataBean.setIs_top("0");
    }

    public XxfbInfoRelease getDataBean() {
        if (dataBean == null) {
            dataBean = new XxfbInfoRelease();
        }
        return dataBean;
    }

    public void setDataBean(XxfbInfoRelease dataBean) {
        this.dataBean = dataBean;
    }

    public XxfbInfoContent getContent() {
        if (content == null) {
            content = new XxfbInfoContent();
        }
        return content;
    }

    public void setContent(XxfbInfoContent content) {
        this.content = content;
    }

    public List<SelectItem> getPublisher_typeModel() {
        if (publisher_typeModel == null) {
            publisher_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "发布人类型", null, false));
        }
        return this.publisher_typeModel;
    }

    public List<SelectItem> getIs_topModel() {
        if (is_topModel == null) {
            is_topModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_topModel;
    }

    public List<SelectItem> getInfo_typeModel() {
        if (info_typeModel == null) {
            info_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "信息类型", null, false));
        }
        return this.info_typeModel;
    }

    public List<SelectItem> getInfo_statusModel() {
        if (info_statusModel == null) {
            info_statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "信息状态", null, false));
        }
        return this.info_statusModel;
    }

    public List<SelectItem> getIs_hotModel() {
        if (is_hotModel == null) {
            is_hotModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_hotModel;
    }

    public String getColumn_rowguid() {
        return column_guid;
    }

    public void setColumn_rowguid(String column_rowguid) {
        this.column_guid = column_rowguid;
    }
}
