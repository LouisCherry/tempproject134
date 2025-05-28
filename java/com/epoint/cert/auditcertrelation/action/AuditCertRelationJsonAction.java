package com.epoint.cert.auditcertrelation.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import com.epoint.cert.auditcertrelation.util.AuditCertRelationBizlogic;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.log.LogUtil;
import org.apache.log4j.Logger;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.domain.CertInfoSubExtension;
import com.epoint.cert.basic.certinfo.certinfosubextension.inter.ICertInfoSubExtension;
import com.epoint.cert.basic.certinfo.demoextension.DemoExtensionAddAction;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.ConfigBizlogic;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

@RestController("auditcertrelationjsonaction")
@Scope("request")
public class AuditCertRelationJsonAction extends BaseController {
    private static final long serialVersionUID = 1L;

    /**
     * 元数据列表
     */
    private List<CertMetadata> metadataList;

    /**
     * 元数据预览照面信息实体对象
     */
    private Record dataBean = new Record();

    /**
     * 照面信息附件上传map
     */
    private Map<String, FileUploadModel9> modelMap = new HashMap<>();

    private AuditCertRelationBizlogic auditCertRelationBizlogic = new AuditCertRelationBizlogic();

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 子拓展信息api
     */
    @Autowired
    private ICertInfoSubExtension iCertInfoSubExtension;

    /**
     * 附件操作服务service
     */
    @Autowired
    private IAttachService iAttachService;

    @Autowired
    private IAuditCertRelationService auditCertRelationService;

    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(DemoExtensionAddAction.class);

    /**
     * 可以新增
     */
    private boolean canAdd = true;

    /**
     * 是否为新增
     */
    private boolean isAdd = false;

    /**
     * 证照目录主键
     */
    private String certguid;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    /**
     *  证照目录id
     */
    private String certid;

    private String auditcertrelationguid;

    private AuditCertRelation auditCertRelation;

    @Override
    public void pageLoad() {
        certid = getRequestParameter("certid");
        auditcertrelationguid = getRequestParameter("auditcertrelationguid");
        if (StringUtil.isNotBlank(certid)) {
            CertCatalog certCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(certid);
            if(certCatalog != null){
                certguid = certCatalog.getRowguid();
            }
        }
        if (StringUtil.isNotBlank(auditcertrelationguid)) {
            auditCertRelation = auditCertRelationService.find(auditcertrelationguid);
        }
        if (auditCertRelation == null) {
            addCallbackParam("msg", "未查询到关联记录！");
            return;
        }

        if (StringUtil.isNotBlank(certguid)) {
            // 获得元数据配置表所有顶层节点
            metadataList = iCertMetaData.selectTopDispinListByCertguid(certguid);
            if (ValidateUtil.isBlankCollection(metadataList)) {
                addCallbackParam("msg", "请先配置业务信息！");
                return;
            }

//            Map<String, Object> filter = new HashMap<>();
//            filter.put("catalogguid", certguid);
//            dataBean = noSQLSevice.find(CertConstant.SQL_TABLE_DEMO_EXTENSION, filter, false);
            if(StringUtil.isNotBlank(auditCertRelation.getRelationjson())){
                dataBean = JsonUtil.jsonToObject(auditCertRelation.getRelationjson().replace("$value", ""),Record.class);
            }
            if (dataBean == null) {
                isAdd = true;
                dataBean = new Record();

            }
            dataBean.set("rowguid", auditCertRelation.getRowguid());
            //dataBean.setSql_TableName("auditcertrelationjsonaction");

            // 都改成下拉选
            for (CertMetadata metadata : metadataList){
                metadata.setFielddisplaytype("combobox");
                metadata.setDatasource_codename("是否");
            }

            if (!isPostback()) {
                if (isAdd) {
                    // 返回渲染的字段(子表新增模式)
                    Map<String, Object> controlsMap = auditCertRelationBizlogic.generateMapUseSubExtension("auditcertrelationjsonaction",
                            metadataList, CertConstant.MODE_ADD, CertConstant.SUB_MODE_ADD, auditCertRelation.getFormid(), auditCertRelation.getRelationtype(),auditCertRelation.getPhaseguid(),auditCertRelation.getIsdbhy(),auditCertRelation.getPformid());
                    addCallbackParam("controls", controlsMap);
                }
                else {
                    // 返回渲染的字段(子表修改模式)
                    addCallbackParam("controls",
                            auditCertRelationBizlogic.generateMapUseSubExtension("auditcertrelationjsonaction", metadataList,
                                    CertConstant.MODE_EDIT, CertConstant.SUB_MODE_EDIT,auditCertRelation.getFormid(),auditCertRelation.getRelationtype(),auditCertRelation.getPhaseguid(),auditCertRelation.getIsdbhy(),auditCertRelation.getPformid()));
                    addCallbackParam("rowguid", dataBean.get("rowguid"));
                }
            }
        }

        addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
    }

    /**
     * 保存并关闭 存在隐患，微服务时mongodb不回滚
     *
     * @param params
     *            json (key:parentguid value:fieldname)
     * @param cliengguids
     *            json (key:fieldname value:cliengguid)
     */
    public void add(String params, String cliengguids) {
        // 全台传来的json
        JSONObject jsonObject = JSON.parseObject(params);
        JSONObject cliengguidsObject = JSON.parseObject(cliengguids);


        // 保存或新增
        if (isAdd) { // 新增
//            dataBean.set("operatedate", new Date());
//            dataBean.set("operateusername", userSession.getDisplayName());
//            dataBean.set("rowguid", UUID.randomUUID().toString());
            // 关联目录主键
            //dataBean.set("catalogguid", certguid);
            // 设置子数据
//            setSubExtensionJson(jsonObject, dataBean);
//            noSQLSevice.insert(dataBean);
            auditCertRelation.setRelationjson(jsonObject.toJSONString());
            auditCertRelationService.update(auditCertRelation);
        }
        else { // 保存
            try {
//                setSubExtensionJson(jsonObject, dataBean);
//                noSQLSevice.insert(dataBean);
//                // 更新操作时间
//                dataBean.set("operatedate", new Date());
//                noSQLSevice.update(dataBean);
                auditCertRelation.setRelationjson(jsonObject.toJSONString());
                auditCertRelationService.update(auditCertRelation);
            }
            catch (Exception e) {
                log.info(e);
                log.error(String.format("MongoDb更新演示拓展信息 {catalogguid = %s} 失败!", certguid));
            }
        }

        addCallbackParam("msg", "保存成功！");
        // 是否可添加msg
        addCallbackParam("canAdd", canAdd);
    }

    /**
     * 设置照面信息子数据
     *
     * @param jsonObject
     * @return record 照面信息
     */
    public void setSubExtensionJson(JSONObject jsonObject, Record record) {
        if (jsonObject == null) {
            return;
        }
        Set<String> keySet = jsonObject.keySet();
        if (ValidateUtil.isNotBlankCollection(keySet)) {
            for (String parentguid : keySet) {
                List<CertInfoSubExtension> extensionList = iCertInfoSubExtension
                        .selectSubExtensionByParentguid("operatedate, subextension", parentguid);
                JSONArray jsonArray = new JSONArray();
                if (ValidateUtil.isNotBlankCollection(extensionList)) {
                    for (CertInfoSubExtension extension : extensionList) {
                        JSONObject subJsonObject = JSON.parseObject(extension.getSubextension());
                        // 设置主键
                        subJsonObject.put("rowguid", UUID.randomUUID().toString());
                        // 操作时间
                        subJsonObject.put("operatedate", extension.getOperatedate());
                        jsonArray.add(subJsonObject);
                    }
                }
                String json = JsonUtil.objectToJson(jsonArray);
                record.set(jsonObject.getString(parentguid), json);
            }
        }
    }

    /**
     * 删除子表(临时表)数据
     *
     * @param params
     *            json (key:parentguid value:fieldname)
     */
    public void deleteSubExtension(String params) {
        // 删除字表（临时表）数据
        JSONObject jsonObject = JSON.parseObject(params);
        if (jsonObject == null) {
            addCallbackParam("msg", "保存成功！");
            return;
        }
        Set<String> parentguidList = jsonObject.keySet();
        if (ValidateUtil.isNotBlankCollection(parentguidList)) {
            for (String parentguid : parentguidList) {
                // 删除子表
                iCertInfoSubExtension.deleteExtensionByParentguid(parentguid);
            }
        }
        addCallbackParam("msg", "保存成功！");
    }

    public void setDataBean(CertInfoExtension dataBean) {
        this.dataBean = dataBean;
    }

    public Record getDataBean() {
        return dataBean;
    }

    public void setDataBean(Record dataBean) {
        this.dataBean = dataBean;
    }
}
