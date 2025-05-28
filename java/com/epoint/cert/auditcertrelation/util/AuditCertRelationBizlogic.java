package com.epoint.cert.auditcertrelation.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certinfo.demoextension.DemoExtensionAddAction;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.core.utils.collection.EpointCollectionUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.util.JnConstant;
import org.apache.log4j.Logger;

import java.util.*;

public class AuditCertRelationBizlogic {
    private Logger log = LogUtil.getLog(DemoExtensionAddAction.class);

    private ICodeItemsService iCodeItemsService = (ICodeItemsService) ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
    private ICertInfo certInfoService = (ICertInfo)ContainerFactory.getContainInfo().getComponent(ICertInfo.class);
    private ICertCatalog certCatalogService = (ICertCatalog)ContainerFactory.getContainInfo().getComponent(ICertCatalog.class);

    /**
     *  生成控件map数据(使用拓展子信息表)
     *  @param actionName
     *  @param certMetadataList
     *  @param parentMode 父表模式
     *  @param subMode 子表模式
     *  @return
     */
    public Map<String, Object> generateMapUseSubExtension(String actionName, List<CertMetadata> certMetadataList, String parentMode, String subMode,String formid,String relationtype,String phaseguid,String isdbhy,String pformid) {
        if (ValidateUtil.isBlankCollection(certMetadataList)) {
            return new HashMap<String, Object>();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        for (CertMetadata metadata : certMetadataList) {
            Map<String, Object> metaDataMap = generateMetaDataMap(actionName, metadata, parentMode, subMode,formid,relationtype,phaseguid,isdbhy,pformid);
            recordList.add(metaDataMap);
        }
        map.put("data", recordList);
        return map;
    }

    /**
     *  生成元数据map
     *  @param actionName
     *  @param metadata
     *  @param parentMode 父表模式
     *  @param subMode 子表模式
     *  @return
     */
    public Map<String, Object> generateMetaDataMap(String actionName, CertMetadata metadata, String parentMode, String subMode,String formid,String relationtype,String phaseguid,String isdbhy,String pformid) {
        Map<String, Object> recordMap = new HashMap<String, Object>();
        // 展示子节点 属性(label type, fieldName, parentguid, onclick, iconCls)
        if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
            // 设置类型
            recordMap.put("type", "templWithChlid");
            // 设置parentguid
            String parentguid = "";
            // 子表模式
            if (CertConstant.SUB_MODE_ADD.equals(subMode)) { // 新增模式
                parentguid = UUID.randomUUID().toString();
                recordMap.put("onclick", "openAddSublist('" + metadata.getRowguid()
                        + "', '" + metadata.getFieldchinesename() + "', '" + parentguid + "')");
                recordMap.put("iconCls", "icon-edit");
            }
            else if (CertConstant.SUB_MODE_EDIT.equals(subMode)) { // 修改模式
                recordMap.put("onclick", "openEditSublist('" + metadata.getRowguid()
                        + "', '" + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
                recordMap.put("iconCls", "icon-edit");
            }
            else if (CertConstant.SUB_MODE_DETAIL.equals(subMode)) { // 查看模式
                recordMap.put("onclick", "openDetailSublist('" + metadata.getRowguid()
                        + "', '" + metadata.getFieldchinesename() + "', '" + metadata.getFieldname() + "')");
                recordMap.put("iconCls", "icon-search");
            }

            recordMap.put("parentguid", parentguid);
        }
        else { // 不展示子节点
            // 详情页面处理
            if (CertConstant.MODE_DETAIL.equals(parentMode)) {
                // 类型
                if ("webuploader".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                    // 使用uc-oauploader控件
                    recordMap.put("type", "oauploader");
                }
                else if ("webeditor".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                    recordMap.put("type", metadata.getFielddisplaytype());
                }
                else if ("checkbox".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                    recordMap.put("type", metadata.getFielddisplaytype());
                }
                else {
                    recordMap.put("type", "outputtext");
                }

                // 数据代码
                if (StringUtil.isNotBlank(metadata.getDatasource_codename())) {
                    recordMap.put("dataOptions", "{'code' : '" + metadata.getDatasource_codename() + "'}");
                }
                // 详细页面时间去除时分秒
                if ("DateTime".equals(metadata.getFieldtype())) {
                    recordMap.put("dataOptions", "{'format' : 'yyyy-MM-dd'}");
                }
                // 展示子节点
                if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())) {
                    recordMap.put("type", recordMap.get("type") + "WithChlid");
                    recordMap.put("onclick", "ondetail('" + metadata.getRowguid()
                            + "', '" + metadata.getFieldchinesename() + "')");
                }
            }
            else { // 新增、修改
                if ("webuploader".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                    // 使用uc-oauploader控件
                    recordMap.put("type", "oauploader");
                }
                else {
                    recordMap.put("type", metadata.getFielddisplaytype());
                }
                // 是否必填
                if (CertConstant.CONSTANT_STR_ONE.equals(metadata.getNotnull())) {
                    recordMap.put("required", true);
                    // 设置提示信息
                    recordMap.put("requiredErrorText", metadata.getFieldchinesename() + "不能为空!");
                }
                else {
                    recordMap.put("required", false);
                }
                // 代码项值渲染
                if (StringUtil.isNotBlank(relationtype)) {
                    recordMap.put("dataData", getCodeDataNew(relationtype, formid,phaseguid,isdbhy,pformid));
                }
            }

            String vType = "";

            // 字符串类型进行长度校验
            if("nvarchar".equals(metadata.getFieldtype())){
                // 字段长度校验
                if (StringUtil.isBlank(metadata.getMaxlength())
                        || CertConstant.CONSTANT_STR_ZERO.equals(metadata.getMaxlength().toString())) {
                    // 默认长度50
                    vType = "maxLength:50";
                    recordMap.put("maxlength", "50");
                }
                else {
                    vType = "maxLength:" + metadata.getMaxlength();
                    recordMap.put("maxlength", metadata.getMaxlength());
                }
            }

            // int类型在表单中的校验
            if ("Integer".equals(metadata.getFieldtype())) {
                vType = "int";
            }
            else if("Numeric".equals(metadata.getFieldtype())) { //数字类型在表单中校验
                vType = "float;" + vType;
            }

            if(metadata.getRelatedfield() != null){
                recordMap.put("onvaluechanged", "valuechanged('"+metadata.getFieldname()+"','" + metadata.getRelatedfield()+"')");
            }

            // 自定义校验规则
            String onvalidation = metadata.getOnvalidation();
            if (StringUtil.isNotBlank(onvalidation)) {
                if (onvalidation.startsWith("mini:")) { // miniui自带规则
                    onvalidation = onvalidation.replace("mini:", "");
                    vType += ";" + onvalidation;
                }
                else if (onvalidation.startsWith("custom:")) { // 自定义校验规则
                    onvalidation = onvalidation.replace("custom:", "");
                    recordMap.put("onvalidation", onvalidation);
                }
            }
            recordMap.put("vType", vType);

            // 文件上传
            if ("webuploader".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                // 根据父表模式，渲染附件控件
                if (CertConstant.MODE_ADD.equals(parentMode)) {
                    // 生成cliengguid
                    recordMap.put("action", String.format("%s.getFileUploadModel(%s,%s)",
                            actionName, metadata.getFieldname(), UUID.randomUUID().toString()));
                }
                else {
                    recordMap.put("action", String.format("%s.getFileUploadModel(%s)", actionName, metadata.getFieldname()));
                }
                // 附件只支持图片类型
                recordMap.put("limitType", "bmp,gif,jpeg,png,jpg");
                recordMap.put("previewExt", "bmp,gif,jpeg,png,jpg");
                recordMap.put("editext", "bmp,gif,jpeg,png,jpg");
                recordMap.put("fileNumLimit", "1");
                //recordMap.put("fileSingleSizeLimit", "5120");
                //recordMap.put("mimeTypes", "image/*");
            }

            if (CertConstant.CONSTANT_INT_ONE.equals(metadata.getControlwidth())) {
                recordMap.put("width", CertConstant.CONSTANT_INT_ONE);
            }

            if (CertConstant.CONSTANT_INT_TWO.equals(metadata.getControlwidth())) {
                recordMap.put("width", CertConstant.CONSTANT_INT_TWO);
            }

            // 如果是webeditor控件，设置双倍宽。
            if ("webeditor".equals(StringUtil.toLowerCase(metadata.getFielddisplaytype()))) {
                recordMap.put("width", CertConstant.CONSTANT_INT_TWO);
            }
            recordMap.put("bind", "dataBean." + StringUtil.toLowerCase(metadata.getFieldname()));
        }

        // 设置字段名称
        recordMap.put("fieldName", metadata.getFieldname());
        recordMap.put("label", metadata.getFieldchinesename());
        recordMap.put("required", false);
        return recordMap;
    }

    /**
     * 获取代码项内容
     *
     * @param codeName
     * @return
     */
    public String getCodeData(String codeName) {
        StringBuffer rtnString = new StringBuffer("[");
        List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName(codeName);
        if (ValidateUtil.isBlankCollection(codeItemList)) {
            return "";
        }

        for (CodeItems codeItems : codeItemList) {
            rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
        }
        rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
        rtnString.append("]");
        return rtnString.toString();
    }

    /**
     * 构建下拉选项，展示一张表单或电子表单中的字段名称
     *
     * @param relationtype
     * @return
     */
    public String getCodeDataNew(String relationtype,String formid,String phaseguid,String isdbhy,String pformid) {
        StringBuffer rtnString = new StringBuffer("[");
        // 一张表单
        if(JnConstant.ONEFORM.equals(relationtype)){
            if(JnConstant.LXYDGHXK.equals(phaseguid)){
                // 审批阶段
                List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName("字段对应_立项用地规划许可");
                if(EpointCollectionUtils.isNotEmpty(codeItemList)){
                    for (CodeItems codeItems : codeItemList){
                        rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
                    }
                }
            } else if (JnConstant.SGXK.equals(phaseguid)) {
                List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName("字段对应_施工许可");
                if(EpointCollectionUtils.isNotEmpty(codeItemList)){
                    for (CodeItems codeItems : codeItemList){
                        rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
                    }
                }
            }
            // 字段对应_竣工验收许可
            else if (JnConstant.JGYL.equals(phaseguid)) {
                List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName("字段对应_竣工验收许可");
                if(EpointCollectionUtils.isNotEmpty(codeItemList)){
                    for (CodeItems codeItems : codeItemList){
                        rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
                    }
                }
            }
            //字段对应_工程建设许可
            else if (JnConstant.GCJSXK.equals(phaseguid)) {
                List<CodeItems> codeItemList = iCodeItemsService.listCodeItemsByCodeName("字段对应_工程建设许可");
                if(EpointCollectionUtils.isNotEmpty(codeItemList)){
                    for (CodeItems codeItems : codeItemList){
                        rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
                    }
                }
            }
        }
        // 电子表单
        else if(JnConstant.EFORM.equals(relationtype)){
            String epointsformurl = ConfigUtil.getFrameConfigValue("epointsformurl");
            if (StringUtil.isNotBlank(epointsformurl) && !epointsformurl.endsWith("/")) {
                epointsformurl = epointsformurl + "/";
            }

            //查询表单
            JSONObject param = new JSONObject();
            JSONObject taskparam = new JSONObject();
            // 如果是多表合一
            if("1".equals(isdbhy)){
                //taskparam.put("formId", pformid);
                //taskparam.put("formIds", formid);
                taskparam.put("formId", formid);
            }
            else{
                taskparam.put("formId", formid);
            }
            param.put("params", taskparam);
            log.info("接口地址==="+epointsformurl);
            log.info("表单接口getEpointSformInfo入参：===" + param.toJSONString());
            String result = HttpUtil.doPost(epointsformurl + "rest/sform/getEpointSformInfo", param);
            log.info("表单接口getEpointSformInfo返回：===" + result);
            if (StringUtil.isBlank(result)) {
                return "";
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            int intValue = jsonObject.getJSONObject("status").getIntValue("code");
            if(intValue==1){
                //SONObject jsonObject1 = jsonObject.getJSONObject("custom").getJSONObject("formData").getJSONObject("formInfo");
                JSONArray jsonObjectStructList = jsonObject.getJSONObject("custom").getJSONObject("formData").getJSONArray("structList");
                if(EpointCollectionUtils.isEmpty(jsonObjectStructList)){
                    return "";
                }

                // 遍历 jsonObjectStructList
                for (int i = 0; i < jsonObjectStructList.size(); i++) {
                    JSONObject item = jsonObjectStructList.getJSONObject(i);
                    String fieldchinesename = item.getString("fieldchinesename");
                    String fieldname = item.getString("fieldname");
                    rtnString.append("{id:'" + fieldname + "',text:'" + fieldchinesename + "'},");
                }
            }
            else{
                return "";
            }
        }
        rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
        rtnString.append("]");
        return rtnString.toString();
    }
}
