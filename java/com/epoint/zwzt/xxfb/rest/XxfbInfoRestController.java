package com.epoint.zwzt.xxfb.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.api.ApiBaseController;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.IXxfbInfoColumnService;
import com.epoint.zwzt.xxfb.xxfbinfocolumn.api.entity.XxfbInfoColumn;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.IXxfbInfoContentService;
import com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity.XxfbInfoContent;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.IXxfbInfoReleaseService;
import com.epoint.zwzt.xxfb.xxfbinforelease.api.entity.XxfbInfoRelease;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping("/inforest")
public class XxfbInfoRestController extends ApiBaseController
{
    transient Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IXxfbInfoColumnService columnService;
    @Autowired
    private IXxfbInfoReleaseService releaseService;
    @Autowired
    private IXxfbInfoContentService contentService;
    @Autowired
    private IAttachService attachService;

    /**
     * 获取栏目列表
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getInfoColumnList", method = RequestMethod.POST)
    public String getInfoColumnList(@RequestBody String params) {
        log.info("==开始调用getInfoColumnList接口===");
        log.info("getInfoColumnList params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        JSONObject paramJson = JSON.parseObject(param.getString("params"));
        if (paramJson != null) {
            Integer currentPage = StringUtil.isNotBlank(paramJson.getString("currentPage"))
                    ? paramJson.getInteger("currentPage")
                    : 0;
            Integer pageSize = StringUtil.isNotBlank(paramJson.getString("pageSize"))
                    ? paramJson.getInteger("pageSize")
                    : 10;
            String parentColumnNumber = StringUtil.isNotBlank(paramJson.getString("parentColumnNumber"))
                    ? paramJson.getString("parentColumnNumber")
                    : "";
            String columnNumber = StringUtil.isNotBlank(paramJson.getString("columnNumber"))
                    ? paramJson.getString("columnNumber")
                    : "";
            String parentColumnGuid = StringUtil.isNotBlank(paramJson.getString("parentColumnGuid"))
                    ? paramJson.getString("parentColumnGuid")
                    : "";
            String name = StringUtil.isNotBlank(paramJson.getString("name")) ? paramJson.getString("name") : "";

            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            if (StringUtil.isNotBlank(columnNumber)) {
                sqlUtil.like("column_number", columnNumber);
            }
            if (StringUtil.isNotBlank(parentColumnNumber)) {
                sqlUtil.eq("parent_column_number", parentColumnNumber);

            }
            if (StringUtil.isNotBlank(parentColumnGuid)) {
                sqlUtil.eq("parent_column_rowguid", parentColumnGuid);
            }
            if (StringUtil.isNotBlank(name)) {
                sqlUtil.like("info_column_name", name);
            }
            sqlUtil.setOrderDesc("ordernum");
            PageData<XxfbInfoColumn> pageData = columnService.paginatorList(sqlUtil.getMap(), currentPage*pageSize,
                    pageSize);
            JSONArray array = new JSONArray();
            for (XxfbInfoColumn column : pageData.getList()) {
                JSONObject obj = new JSONObject();
                obj.put("columnGuid", column.getRowguid());
                obj.put("columnName", column.getInfo_column_name());
                obj.put("orderNum", column.getOrdernum());
                array.add(obj);
            }
            custom.put("columnList", array);
            return generateReturnInfo("1", "调用成功", custom);

        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);

        }
    }

    /**
     * 获取信息发布列内容列表
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getInfoReleaseList", method = RequestMethod.POST)
    public String getInfoReleaseList(@RequestBody String params) {
        log.info("==开始调用getInfoReleaseList接口===");
        log.info("getInfoReleaseList params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        JSONObject paramJson = JSON.parseObject(param.getString("params"));
        if (paramJson != null) {
            Integer currentPage = StringUtil.isNotBlank(paramJson.getString("currentPage"))
                    ? paramJson.getInteger("currentPage")
                    : 0;
            Integer pageSize = StringUtil.isNotBlank(paramJson.getString("pageSize"))
                    ? paramJson.getInteger("pageSize")
                    : 10;
            String infoStatus = StringUtil.isNotBlank(paramJson.getString("status")) ? paramJson.getString("status")
                    : "";
            String infoTitle = StringUtil.isNotBlank(paramJson.getString("infoTitle"))
                    ? paramJson.getString("infoTitle")
                    : "";
            String columnNumber = StringUtil.isNotBlank(paramJson.getString("columnNumber"))
                    ? paramJson.getString("columnNumber")
                    : "";
            String columnGuid = StringUtil.isNotBlank(paramJson.getString("columnGuid"))
                    ? paramJson.getString("columnGuid")
                    : "";

            SqlConditionUtil sqlUtil = new SqlConditionUtil();
            if (StringUtil.isNotBlank(infoStatus)) {
                sqlUtil.eq("info_status", infoStatus);
            }
            if (StringUtil.isNotBlank(infoTitle)) {
                sqlUtil.like("info_title", infoTitle);
            }

            if (StringUtil.isNotBlank(columnNumber)) {
                SqlConditionUtil columnSqlUtil = new SqlConditionUtil();
                columnSqlUtil.eq("column_number", columnNumber);
                XxfbInfoColumn column = columnService.find(columnSqlUtil.getMap());
                if (column != null && !column.isEmpty() && StringUtil.isNotBlank(column.getRowguid())) {
                    sqlUtil.eq("column_guid", column.getRowguid());
                }
            }
            if (StringUtil.isNotBlank(columnGuid)) {
                sqlUtil.eq("column_guid", columnGuid);
            }
            sqlUtil.setOrderDesc("is_top");
            sqlUtil.setOrderDesc("operatedate");
            sqlUtil.setOrderDesc("ordernum");
            PageData<XxfbInfoRelease> pageData = releaseService.paginatorList(sqlUtil.getMap(), currentPage*pageSize,
                    pageSize);
            JSONArray array = new JSONArray();
            for (XxfbInfoRelease release : pageData.getList()) {
                JSONObject obj = new JSONObject();
                obj.put("infoGuid", release.getRowguid());
                obj.put("infoTitle", release.getInfo_title());
                obj.put("infoType", release.getInfo_type());
                obj.put("operateUser", release.getOperateusername());
                obj.put("operateDate",
                        release.getOperatedate() != null
                                ? EpointDateUtil.convertDate2String(release.getOperatedate(), "yyyy-MM-dd HH:mm:ss")
                                : "");
                array.add(obj);
            }
            custom.put("infoList", array);
            return generateReturnInfo("1", "调用成功", custom);
        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }
    }

    /**
     * 获取信息发布内容 根据主键查询信息发布内容
     *
     * @param params 参数
     * @return 栏目列表JSON字符串
     */
    @RequestMapping(value = "/getInfoReleaseByGuid", method = RequestMethod.POST)
    public String getInfoReleaseByGuid(@RequestBody String params) {
        log.info("==开始调用getInfoReleaseByGuid接口===");
        log.info("getInfoReleaseByGuid params:" + params);
        JSONObject param = JSON.parseObject(params);
        JSONObject custom = new JSONObject();
        JSONObject paramJson = JSON.parseObject(param.getString("params"));
        if (paramJson != null) {
            String infoGuid = paramJson.getString("infoGuid");
            if (StringUtil.isNotBlank(infoGuid)) {
                XxfbInfoRelease release = releaseService.find(infoGuid);
                if (release != null && !release.isEmpty()) {
                    custom.put("infoGuid", release.getRowguid());
                    custom.put("infoTitle", release.getInfo_title());
                    custom.put("infoType", release.getInfo_type());
                    custom.put("operateUser", release.getOperateusername());
                    custom.put("operateDate",
                            release.getOperatedate() != null
                                    ? EpointDateUtil.convertDate2String(release.getOperatedate(),
                                    "yyyy-MM-dd HH:mm:ss")
                                    : "");
                    custom.put("createdate",
                            release.getCreate_time() != null
                                    ? EpointDateUtil.convertDate2String(release.getCreate_time(),
                                    "yyyy-MM-dd HH:mm:ss")
                                    : "");
                    String infoContent = "";
                    String linkUrl = "";
                    JSONArray array = new JSONArray();
                    switch (release.getInfo_type()) {
                        case "10":
                            List<FrameAttachInfo> frameAttachInfos = attachService.getAttachInfoListByGuid(
                                    release.getRowguid());
                            if(frameAttachInfos!=null&&!frameAttachInfos.isEmpty()) {
                                for (FrameAttachInfo infoDetail : frameAttachInfos) {
                                    if (infoDetail != null && !infoDetail.isEmpty()) {
                                        JSONObject objDetail = new JSONObject();
                                        objDetail.put("attachName", infoDetail.getAttachFileName());
                                        objDetail.put("type", infoDetail.getContentType());
                                        objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                        array.add(objDetail);
                                    }
                                }
                            }
                            if (StringUtil.isNotBlank(release.getInfo_content_rowguid())) {
                                XxfbInfoContent content = contentService.find(release.getInfo_content_rowguid());
                                if (content != null && !content.isEmpty()
                                        && StringUtil.isNotBlank(content.getInfo_content())) {
                                    infoContent = content.getInfo_content();
                                }
                            }
                            if (StringUtil.isNotBlank(release.getEnclosure_guids())) {
                                String[] guids = release.getEnclosure_guids().split(",");
                                for (String guid : guids) {
                                    FrameAttachInfo infoDetail = attachService.getAttachInfoDetail(guid);
                                    if (infoDetail != null && !infoDetail.isEmpty()) {
                                        JSONObject objDetail = new JSONObject();
                                        objDetail.put("attachName", infoDetail.getAttachFileName());
                                        objDetail.put("type", infoDetail.getContentType());
                                        objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                        array.add(objDetail);
                                    }
                                }
                            }
                            break;
                        case "21":
                        case "20":
                            if (StringUtil.isNotBlank(release.getInfo_content_rowguid())) {
                                XxfbInfoContent content = contentService.find(release.getInfo_content_rowguid());
                                if (content != null && !content.isEmpty()
                                        && StringUtil.isNotBlank(content.getInfo_content())) {
                                    infoContent = content.getInfo_content();
                                }
                            }
                            if (StringUtil.isNotBlank(release.getEnclosure_guids())) {
                                String[] guids = release.getEnclosure_guids().split(",");
                                for (String guid : guids) {
                                    FrameAttachInfo infoDetail = attachService.getAttachInfoDetail(guid);
                                    if (infoDetail != null && !infoDetail.isEmpty()) {
                                        JSONObject objDetail = new JSONObject();
                                        objDetail.put("attachName", infoDetail.getAttachFileName());
                                        objDetail.put("type", infoDetail.getContentType());
                                        objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                        array.add(objDetail);
                                    }
                                }
                            }
                            break;
                        // 链接
                        case "30":
                            linkUrl = release.getLinkurl();
                            break;
                        // 视频
                        case "40":
                            if (StringUtil.isNotBlank(release.getVedio_guid())) {
                                FrameAttachInfo infoDetail = attachService
                                        .getAttachInfoDetail(release.getVedio_guid());
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    JSONObject objDetail = new JSONObject();
                                    objDetail.put("attachName", infoDetail.getAttachFileName());
                                    objDetail.put("type", infoDetail.getContentType());
                                    objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                    array.add(objDetail);
                                }
                            }
                            if (StringUtil.isNotBlank(release.getEnclosure_guids())) {
                                String[] guids = release.getEnclosure_guids().split(",");
                                for (String guid : guids) {
                                    FrameAttachInfo infoDetail = attachService.getAttachInfoDetail(guid);
                                    if (infoDetail != null && !infoDetail.isEmpty()) {
                                        JSONObject objDetail = new JSONObject();
                                        objDetail.put("attachName", infoDetail.getAttachFileName());
                                        objDetail.put("type", infoDetail.getContentType());
                                        objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                        array.add(objDetail);
                                    }
                                }
                            }
                            break;
                        // 附件
                        case "50":
                            if (StringUtil.isNotBlank(release.getEnclosure_guids())) {
                                String[] guids = release.getEnclosure_guids().split(",");
                                for (String guid : guids) {
                                    FrameAttachInfo infoDetail = attachService.getAttachInfoDetail(guid);
                                    if (infoDetail != null && !infoDetail.isEmpty()) {
                                        JSONObject objDetail = new JSONObject();
                                        objDetail.put("attachName", infoDetail.getAttachFileName());
                                        objDetail.put("type", infoDetail.getContentType());
                                        objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                        array.add(objDetail);
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    JSONArray pictureArray = new JSONArray();

                    // 是否启用图片
                    if (StringUtil.isNotBlank(release.getIs_enablePicture())
                            && "1".equals(release.getIs_enablePicture())) {
                        if (StringUtil.isNotBlank(release.getPicture_guids())) {
                            String[] guids = release.getPicture_guids().split(",");
                            for (String guid : guids) {
                                FrameAttachInfo infoDetail = attachService.getAttachInfoDetail(guid);
                                if (infoDetail != null && !infoDetail.isEmpty()) {
                                    JSONObject objDetail = new JSONObject();
                                    objDetail.put("attachName", infoDetail.getAttachFileName());
                                    objDetail.put("type", infoDetail.getContentType());
                                    objDetail.put("downUrl", attachService.getAttachDownPageUrl(infoDetail));
                                    pictureArray.add(objDetail);
                                }
                            }
                        }
                    }
                    
                    if (infoContent.contains("getContent")) {
                    	infoContent.replaceAll("/jnzwfw/rest/frame/base/attach/attachAction/getContent", "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent");
                    }
                    custom.put("infoContent", infoContent);
                    custom.put("linkUrl", linkUrl);
                    custom.put("attachList", array);
                    custom.put("pictureAttachList", pictureArray);
                }
                else {
                    return generateReturnInfo("0", "未查询到相关信息", custom);
                }
            }
            return generateReturnInfo("0", "未查询到相关信息", custom);

        }
        else {
            return generateReturnInfo("0", "参数验证失败", custom);
        }

    }

    private String generateReturnInfo(String code, String text, JSONObject custom) {
        JSONObject result = new JSONObject();
        JSONObject status = new JSONObject();
        status.put("code", "200");
        status.put("text", "");
        custom.put("code", code);
        custom.put("text", text);
        result.put("status", status);
        result.put("custom", custom);
        return result.toJSONString();
    }

}
