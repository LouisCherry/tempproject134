package com.epoint.audittask.material.action;

import com.epoint.basic.EpointKeyNames9;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.CodeTreeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 事项材料表新增页面对应的后台
 * 
 * @author hanjiankai
 * @version [版本号, 2022年3月16日11点15分 ]
 * 新增材料是否可关联中介超市材料
 */
@RestController("jnaudittaskmaterialaddaction")
@Scope("request")
public class JNAuditTaskMaterialAddAction extends BaseController
{

    /**
     * 是否可关联中介超市材料:否
     */
    private String ISZJCSRES_ZERO = "0";
    /**
     * 是否可关联中介超市材料:是
     */
    private String ISZJCSRES_ONE = "1";
    /**
     * 
     */
    private static final long serialVersionUID = -9052830536865776848L;
    /**
     * 事项材料表实体对象
     */
    private AuditTaskMaterial dataBean = null;
//    /**
//     * 共享材料配置实体对象
//     */
//    private CertCatalog dataBean2;
    private Record dataBean2;
    /**
     * 必需设定单选按钮组model
     */
    private List<SelectItem> necessityModel = null;
    /**
     * 允许容缺单选按钮组model
     */
    private List<SelectItem> is_rongqueModel = null;
    /**
     * 提交方式单选按钮组model
     */
    private List<SelectItem> submittypeModel = null;

    /**
     * 允许办件是否可关联中介超市材料单选按钮组model
     */
    private List<SelectItem> iszjcsresModel = null;
    /**
     * 来源渠道下拉列表model
     */
    private List<SelectItem> filesourceModel = null;
    /**
     * 材料类型下拉树model
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
     * 空白模板标识
     */
    private String templateattachguid;
    /**
     * 填报示例标识
     */
    private String exampleattachguid;
    /**
     * 自助填表标识
     */
    private String formattachguid;
    /**
     * 事项guid
     */
    private String taskGuid;
    /**
     * 复制过来的taskguid
     */
    private String copyTaskGuid;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAuditTaskMaterial auditTaskMaterialImpl;

    String materialguid = "";

    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseImpl;

    private FileUploadModel9 egpicAttachUploadModel;

    private String egpiccliengguid;

    @Override
    public void pageLoad() {
        dataBean = new AuditTaskMaterial();
        dataBean2 = new Record();
        taskGuid = this.getRequestParameter("taskGuid");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        dataBean.setNecessity(Integer.valueOf(ZwfwConstant.NECESSITY_SET_NO));// 是否必需：否
        dataBean.setIs_rongque(ZwfwConstant.CONSTANT_INT_ZERO);// 允许容缺：否
        //默认设置是否关联中介超市材料为否
        dataBean.set("iszjcsres",ISZJCSRES_ZERO);
        this.addCallbackParam("ouguid", userSession.getBaseOUGuid());
        if(!isPostback()){
        	  addViewData("templateattachguid", UUID.randomUUID().toString());
              addViewData("exampleattachguid", UUID.randomUUID().toString());
              addViewData("formattachguid", UUID.randomUUID().toString());
              addViewData("egpiccliengguid", UUID.randomUUID().toString());
        }
     
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        //需要先判断是否有重名材料
        SqlConditionUtil sqlc = new SqlConditionUtil();
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            sqlc.eq("taskguid", copyTaskGuid);
        }
        else {
            sqlc.eq("taskguid", taskGuid);
        }
        sqlc.eq("materialname", dataBean.getMaterialname());
        List<AuditTaskMaterial> list = auditTaskMaterialImpl.selectMaterialListByCondition(sqlc.getMap()).getResult();
        if(!list.isEmpty()){
            addCallbackParam("fmsg", "该材料名称已存在，请修改材料名称！");
            return;
        }
        
        materialguid = UUID.randomUUID().toString();
        dataBean.setRowguid(materialguid);
        //关联中介超市材料
        dataBean.set("iszjcsres",dataBean.getStr("iszjcsres"));
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        templateattachguid = getViewData("templateattachguid");
        dataBean.setTemplateattachguid(templateattachguid);
        exampleattachguid = getViewData("exampleattachguid");
        dataBean.setExampleattachguid(exampleattachguid);
        egpiccliengguid = getViewData("egpiccliengguid");
        dataBean.set("egpic_clengguid", egpiccliengguid);
        formattachguid = getViewData("formattachguid");
        dataBean.setFormattachguid(formattachguid);
        dataBean.setSharematerialguid(dataBean2.get("certcatalogid"));
        dataBean.setMaterialid(UUID.randomUUID().toString());
        dataBean.setType(20);
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            dataBean.setTaskguid(copyTaskGuid);
        }
        else {
            dataBean.setTaskguid(taskGuid);
        }
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
            // 保存情形材料关系
            AuditTaskMaterialCase auditMaterialcaseMaterial = new AuditTaskMaterialCase();
            auditMaterialcaseMaterial.setOperatedate(new Date());
            auditMaterialcaseMaterial.setRowguid(UUID.randomUUID().toString());
            auditMaterialcaseMaterial.setTaskcaseguid(reord.get("rowguid"));
            auditMaterialcaseMaterial.setMaterialguid(materialguid);
            auditMaterialcaseMaterial.setNecessity(necessity);
            if (StringUtil.isNotBlank(copyTaskGuid)) {
                auditMaterialcaseMaterial.setTaskguid(copyTaskGuid);
            }
            else {
                auditMaterialcaseMaterial.setTaskguid(taskGuid);
            }
            auditTaskMaterialCaseImpl.addAuditTaskMaterialCase(auditMaterialcaseMaterial);
        }
        String msg = addAuditTaskMaterial(dataBean);
        addCallbackParam("msg", msg);
        addCallbackParam("materialid", dataBean.getMaterialid());
        addCallbackParam("materialname", dataBean.getMaterialname());
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditTaskMaterial();
        addViewData("templateattachguid", UUID.randomUUID().toString());
        addViewData("exampleattachguid", UUID.randomUUID().toString());
        addViewData("formattachguid", UUID.randomUUID().toString());
        addViewData("egpiccliengguid", UUID.randomUUID().toString());

    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -8546279483405674780L;

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(copyTaskGuid)) {
                        taskGuid = copyTaskGuid;
                    }
                    List<Record> auditTaskMaterials = auditTaskMaterialImpl
                            .getMaterialCaseDatePage(taskGuid, materialguid, first, pageSize).getResult();
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
        if (dataBean == null) {
            dataBean = new AuditTaskMaterial();
        }
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

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIs_rongqueModel() {
        if (is_rongqueModel == null) {
            is_rongqueModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_rongqueModel;
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
    public List<SelectItem> getSubmittypeModel() {
        if (submittypeModel == null) {
            submittypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "提交方式", null, false));
        }
        return this.submittypeModel;
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
            templateAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("templateattachguid"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
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

            exampleAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("exampleattachguid"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
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

            egpicAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("egpiccliengguid"), null,
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
            formAttachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("formattachguid"), null, null,
                    handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return formAttachUploadModel;
    }

    public String getTemplateattachguid() {
        return templateattachguid;
    }

    public void setTemplateattachguid(String templateattachguid) {
        this.templateattachguid = templateattachguid;
    }

    public String getExampleattachguid() {
        return exampleattachguid;
    }

    public void setExampleattachguid(String exampleattachguid) {
        this.exampleattachguid = exampleattachguid;
    }

    public String getFormattachguid() {
        return formattachguid;
    }

    public void setFormattachguid(String formattachguid) {
        this.formattachguid = formattachguid;
    }

    public Record getDataBean2() {
        return dataBean2;
    }

    public void setDataBean2(Record dataBean2) {
        this.dataBean2 = dataBean2;
    }
    
    public String addAuditTaskMaterial(AuditTaskMaterial material) {
        String msg = "";
        AuditCommonResult<String> addResult = auditTaskMaterialImpl.addAuditTaskMaterial(material);
        if (!addResult.isSystemCode()) {
            msg = addResult.getSystemDescription();
        }
        else if (!addResult.isBusinessCode()) {
            msg = addResult.getBusinessDescription();
        }
        else {
            msg = "保存成功！";
        }
        return msg;
    }

}
