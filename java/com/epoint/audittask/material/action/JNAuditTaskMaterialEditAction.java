package com.epoint.audittask.material.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.CodeTreeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 事项材料表修改页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-08 15:54:42]
 */
@RestController("jnaudittaskmaterialeditaction")
@Scope("request")
public class JNAuditTaskMaterialEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -5332781162645689297L;

    @Autowired
    private IAttachService attachServiceImpl;

    /**
     * 事项材料表实体对象
     */
    private AuditTaskMaterial dataBean = null;
    /**
     * 共享材料实体对象
     */
    private CertCatalog certCatalog = null;

    /**
     * 必需设定单选按钮组model
     */
    private List<SelectItem> necessityModel = null;

    /**
     * 允许容缺单选按钮组model
     */
    private List<SelectItem> is_rongqueModel = null;

    /**
     * 允许办件是否可关联中介超市材料单选按钮组model
     */
    private List<SelectItem> iszjcsresModel = null;

    /**
     * 提交方式单选按钮组model
     */
    private List<SelectItem> submittypeModel = null;
    /**
     * 提交方式单选按钮组model
     */
    private List<SelectItem> bigShowTypeModel = null;
    /**
     * 来源渠道下拉列表model
     */
    private List<SelectItem> filesourceModel = null;
    /**
     * 材料类型下拉列表model
     */
    private LazyTreeModal9 materialtypeModel = null;
    /**
     * 空白模板model
     */
    private FileUploadModel9 templateAttachUploadModel;
    /**
     * 填报示例model
     */
    private FileUploadModel9 exampleAttachUploadModel;
    /**
     * 自助填表model
     */
    private FileUploadModel9 formAttachUploadModel;
    /**
     * 原空白模板标识
     */
    private String templateattachguid;
    /**
     * 原填报示例标识
     */
    private String exampleattachguid;
    /**
     * 原自助填表标识
     */
    private String formattachguid;
    /**
     * 新空白模板标识
     */
    private String templateguid;
    /**
     * 新填报示例标识
     */
    private String exampleguid;
    /**
     * 新自助填表标识
     */
    private String formguid;
    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAuditTaskMaterial auditTaskMaterialImpl;

    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseImpl;

    @Autowired
    private ICertConfigExternal certConfigExternalImpl;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    @Autowired
    private IAuditTask iaudittask;
    private FileUploadModel9 egpicAttachUploadModel;

    private String egpiccliengguid;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = auditTaskMaterialImpl.getAuditTaskMaterialByRowguid(guid).getResult();
        dataBean.set("iszjcsres",dataBean.getStr("iszjcsres"));
        String areacode = "";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        //共享材料标识
        String sharematerialguid = dataBean.getSharematerialguid();
        String materialname = "";
        if (StringUtils.isNoneBlank(sharematerialguid)) {
            certCatalog = certConfigExternalImpl.getCatalogByCatalogid(sharematerialguid, areacode);
            //返回共享材料名字
            if (certCatalog != null) {
                materialname = certCatalog.getCertname();
            }
            this.addCallbackParam("materialname", materialname);
        }
        else {
            //返回共享材料名字
            this.addCallbackParam("materialname", materialname);
        }
        if (dataBean == null) {
            dataBean = new AuditTaskMaterial();
        }
        templateattachguid = dataBean.getTemplateattachguid();
        exampleattachguid = dataBean.getExampleattachguid();
        formattachguid = dataBean.getFormattachguid();
        egpiccliengguid = dataBean.getStr("egpic_clengguid");
        //原先是否存空白模板
        if (StringUtils.isNoneBlank(templateattachguid)) {
            templateguid = templateattachguid;
        }
        else {
            templateguid = UUID.randomUUID().toString();
        }
        if (StringUtil.isBlank(egpiccliengguid)) {
            egpiccliengguid = UUID.randomUUID().toString();
        }
        //原先是否存在填报示例
        if (StringUtils.isNoneBlank(exampleattachguid)) {
            exampleguid = exampleattachguid;
        }
        else {
            exampleguid = UUID.randomUUID().toString();
        }
        //原先是否存在自助填表
        if (StringUtils.isNoneBlank(formattachguid)) {
            formguid = formattachguid;
        }
        else {
            formguid = UUID.randomUUID().toString();
        }
        //附件信息加载
        List<FrameAttachInfo> frameattachlist = attachServiceImpl.getAttachInfoListByGuid(templateguid);
        if (frameattachlist != null && frameattachlist.size() > 0) {
            // 空白模板显示
            this.addCallbackParam("templateattachUrl", getTempUrl(frameattachlist.get(0).getAttachGuid()));
        }
        else {
            this.addCallbackParam("templateattachUrl", "无附件！");
        }
        frameattachlist = attachServiceImpl.getAttachInfoListByGuid(exampleguid);
        if (frameattachlist != null && frameattachlist.size() > 0) {
            // 填报示例显示
            this.addCallbackParam("exampleattachUrl", getTempUrl(frameattachlist.get(0).getAttachGuid()));
        }
        else {
            this.addCallbackParam("exampleattachUrl", "无附件！");
        }
        frameattachlist = attachServiceImpl.getAttachInfoListByGuid(formguid);
        if (frameattachlist != null && frameattachlist.size() > 0) {
            // 自助填表显示
            this.addCallbackParam("formattachUrl", getTempUrl(frameattachlist.get(0).getAttachGuid()));
        }
        else {
            this.addCallbackParam("formattachUrl", "无附件！");
        }
        frameattachlist = attachServiceImpl.getAttachInfoListByGuid(egpiccliengguid);
        if (frameattachlist != null && frameattachlist.size() > 0) {
            this.addCallbackParam("egpiceattachUrl", getTempUrl(frameattachlist.get(0).getAttachGuid()));
        }
        else {
            this.addCallbackParam("egpicattachUrl", "无附件！");
        }
        this.addCallbackParam("ouguid", userSession.getBaseOUGuid());
        //添加材料必要性字符
        addViewData("necessity", String.valueOf(dataBean.getNecessity()));
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        dataBean.setTemplateattachguid(templateguid);
        dataBean.setExampleattachguid(exampleguid);
        dataBean.setFormattachguid(formguid);
        dataBean.set("egpic_clengguid", egpiccliengguid);

        dataBean.set("iszjcsres",dataBean.getStr("iszjcsres"));
        List<Record> reords = getDataGridData().getWrappedData();
        for (Record reord : reords) {
            int necessity = 0;
            if (reord.get("necessity1") != null && ("1").equals(reord.get("necessity1").toString())) {
                necessity = 10;
            }
            else if (reord.get("necessity2") != null && ("1").equals(reord.get("necessity2").toString())) {
                necessity = 20;
            }
            else if (reord.get("necessity1") != null && ("0").equals(reord.get("necessity1").toString())
                    && reord.get("necessity2") != null && ("0").equals(reord.get("necessity2").toString())) {
                necessity = 0;
            }
            else {
                continue;
            }
            // 先删除原有的记录
            auditTaskMaterialCaseImpl.deleteTaskMaterialCaseByCaseGuid(reord.get("rowguid"), dataBean.getRowguid());
            // 保存情形材料关系
            AuditTaskMaterialCase auditMaterialcaseMaterial = new AuditTaskMaterialCase();
            auditMaterialcaseMaterial.setOperatedate(new Date());
            auditMaterialcaseMaterial.setRowguid(UUID.randomUUID().toString());
            auditMaterialcaseMaterial.setTaskcaseguid(reord.get("rowguid"));
            auditMaterialcaseMaterial.setMaterialguid(dataBean.getRowguid());
            auditMaterialcaseMaterial.setNecessity(necessity);
            auditMaterialcaseMaterial.setTaskguid(dataBean.getTaskguid());
            auditTaskMaterialCaseImpl.addAuditTaskMaterialCase(auditMaterialcaseMaterial);
        }
      //  auditTaskMaterialImpl.updateAuditTaskMaterial();



      //  iaudittaskoptionservice.updateByField(" iszjcsres",dataBean.getStr(" iszjcsres"),dataBean.getRowguid());
        update(dataBean);

        addCallbackParam("msg", "修改成功！");
    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Record> auditTaskMaterials = auditTaskMaterialImpl
                            .getMaterialCaseDatePage(dataBean.getTaskguid(), dataBean.getRowguid(), first, pageSize)
                            .getResult();
                    if (auditTaskMaterials != null) {
                        this.setRowCount(auditTaskMaterials.size());
                        for (Record r : auditTaskMaterials) {
                            if (r.get("necessity") != null && "10".equals(r.get("necessity").toString())) {
                                r.put("necessity1", "1");
                            }
                            else if (r.get("necessity") != null && "20".equals(r.get("necessity").toString())) {
                                r.put("necessity2", "1");
                            }
                        }
                    }
                    else {
                        this.setRowCount(0);
                    }
                    return auditTaskMaterials;

                }

            };
        }
        return model;
    }

    public AuditTaskMaterial getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTaskMaterial dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getNecessityModel() {
        if (necessityModel == null) {
            necessityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否必需", null, false));
        }
        return this.necessityModel;
    }

    /**
     * 办件是否可关联中介超市材料
     * @return
     */
    @SuppressWarnings("unchecked")
    //iszjcsresModel
    public List<SelectItem> getIszjcsresModel() {
        if (iszjcsresModel == null) {
            iszjcsresModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "办件是否可关联中介超市材料", null, false));
        }
        return this.iszjcsresModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_rongqueModel() {
        if (is_rongqueModel == null) {
            is_rongqueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_rongqueModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getSubmittypeModel() {
        if (submittypeModel == null) {
            submittypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "提交方式", null, false));
        }
        return this.submittypeModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getBigShowTypeModel() {
        if (bigShowTypeModel == null) {
        	bigShowTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "大数据证照类型", null, false));
        }
        return this.bigShowTypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFilesourceModel() {
        if (filesourceModel == null) {
            filesourceModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory
                    .factory(EpointKeyNames9.CHECK_SELECT_GROUP, "来源渠道", null, false));
        }
        return this.filesourceModel;
    }

    public LazyTreeModal9 getMaterialtypeModel() {
        if (materialtypeModel == null) {
            materialtypeModel = new LazyTreeModal9(new CodeTreeHandler("材料类型", false));
            materialtypeModel.setLoadAllNode(true);
            materialtypeModel.setSubTableCanSelect(false);
            materialtypeModel.setRootName("所有材料类型");
        }
        return this.materialtypeModel;
    }

    public FileUploadModel9 getTemplateAttachUploadModel() {
        if (templateAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    templateAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            templateAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(templateguid, null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return templateAttachUploadModel;
    }

    public FileUploadModel9 getExampleAttachUploadModel() {
        if (exampleAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    exampleAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            exampleAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(exampleguid, null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return exampleAttachUploadModel;
    }

    public FileUploadModel9 getEgpicAttachUploadModel() {
        if (egpicAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    egpicAttachUploadModel.getExtraDatas().put("msg", "上传成功");
                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };

            egpicAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(egpiccliengguid, null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return egpicAttachUploadModel;
    }

    public FileUploadModel9 getFormAttachUploadModel() {
        if (formAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    formAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            formAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(formguid, null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return formAttachUploadModel;
    }

    public String update(AuditTaskMaterial material) {
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskMaterialImpl.updateAuditTaskMaterial(material);
        //如果材料的必要性从非必要改成必要,删除情形的关联
        if (ZwfwConstant.NECESSITY_SET_NO.equals(getViewData("necessity"))
                && ZwfwConstant.NECESSITY_SET_YES.equals(String.valueOf(material.getNecessity()))) {
            AuditTask task = iaudittask.getAuditTaskByGuid(material.getTaskguid(), true).getResult();
            if (task != null) {
                List<AuditTaskOption> list = iaudittaskoptionservice
                        .findListByTaskidAndMaterialId(task.getTask_id(), material.getMaterialid()).getResult();
                if (list != null && list.size() > 0) {
                    for (AuditTaskOption auditTaskOption : list) {
                        auditTaskOption.setMaterialids(
                                auditTaskOption.getMaterialids().replaceAll(material.getMaterialid() + ";", ""));
                        iaudittaskoptionservice.update(auditTaskOption);
                    }
                }
            }
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("taskguid", material.getTaskguid());
            sqlc.eq("materialguid", material.getRowguid());
            List<AuditTaskMaterialCase> listc = auditTaskMaterialCaseImpl
                    .getAuditTaskMaterialCaseListByCondition(sqlc.getMap()).getResult();
            if (listc != null) {
                for (AuditTaskMaterialCase auditTaskMaterialCase2 : listc) {
                    auditTaskMaterialCaseImpl.deleteAuditTaskMaterialCase(auditTaskMaterialCase2);
                }
            }
        }
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "修改成功！";
        }
        return msg;
    }

    /**
     * 附件下载地址
     * 
     * @param cliengguid
     *            业务guid
     *
     */
    public String getTempUrl(String attachguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(attachguid)) {
            //FrameAttachInfo frameAttachInfo = attachServiceImpl.getAttachInfoDetail(cliengguid);
            FrameAttachInfo frameAttachInfo = attachServiceImpl.getAttachInfoDetail(attachguid);
            if (frameAttachInfo != null && StringUtils.isNoneBlank(frameAttachInfo.getAttachFileName())) {
                String strURL = "onclick=\"goToAttach('" + frameAttachInfo.getAttachGuid() + "')\"";
                wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" " + strURL
                        + ">" + frameAttachInfo.getAttachFileName() + "</a>&nbsp;&nbsp;";
            }
            else {
                wsmbName = "无附件！";
            }
        }
        else {
            wsmbName = "无附件！";
        }
        return wsmbName;
    }
}
