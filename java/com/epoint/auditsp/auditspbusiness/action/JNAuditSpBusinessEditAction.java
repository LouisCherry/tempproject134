package com.epoint.auditsp.auditspbusiness.action;

import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.external.ICertConfigExternal;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 主题配置表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@SuppressWarnings("unchecked")
@RestController("jnauditspbusinesseditaction")
@Scope("request")
public class JNAuditSpBusinessEditAction extends BaseController {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = -4908503270273363533L;

    /**
     * 主题配置表实体对象
     */
    private AuditSpBusiness dataBean = null;

    /**
     * 主题类别，这里主要是为了区分是建设项目的还是一般并联审批的下拉列表model
     */
    private List<SelectItem> businesstypeModel = null;
    /**
     * 申请人类型单选按钮model
     */
    private List<SelectItem> issqlbModel = null;

    /**
     * 主题背景图片
     */
    private List<SelectItem> backgroundImgModel = null;
    /**
     * 套餐类别
     */
    private List<SelectItem> businesskindModel = null;
    /**
     * 套餐电子表单
     */
    private List<SelectItem> businessformModel = null;

    /**
     * 审批流程类型
     */
    private List<SelectItem> splclxmodel = null;

    /**
     * 3.0国标审批流程类型
     */
    private List<SelectItem> splclxV3model = null;

    /**
     * 是否说明单选按钮组model
     */
    private List<SelectItem> commonModel = null;
    /**
     * 辖区编码下拉列表model
     */
    private List<SelectItem> areacodeModel = null;
    /**
     * 证照列表下拉列表model
     */
    private List<SelectItem> catalogModel = null;
    /**
     * 双全双百下拉列表model
     */
    private List<SelectItem> sqsbModel = null;
    /**
     * 辖区编码下拉列表model
     */
    @Autowired
    private IAuditOrgaServiceCenter auditServiceCenter;

    /**
     * 现场次数下拉列表model
     */
    private List<SelectItem> dao_xc_numModel = null;
    /**
     * 外部流程图modal
     */
    private FileUploadModel9 taskoutimgAttachUploadModel;

    private FileUploadModel9 managementUploadModel;
    /**
     * 附件cliengguid
     */
    private String taskoutimgClengGuid = null;

    private String managementClengGuid = null;
    /**
     * 附件信息操作service
     */
    @Autowired
    private IAttachService frameattacinfonewservice;

    @Autowired
    private ICertConfigExternal certConfigExternalImpl;


    /**
     * 是否网上申报下拉列表
     */
    private List<SelectItem> iswssbModel = null;

    /**
     * 修改前的主题名称
     */
    private String preName = null;

    @Autowired
    private IAuditSpBusiness businseeImpl;

    /**
     * 是否
     */
    private List<SelectItem> ifModel = null;
    /**
     * 审批阶段
     */
    private List<SelectItem> phaseModel = null;

    /**
     * 一件事logo
     */
    private FileUploadModel9 logoAttachUploadModel;

    /**
     * 附件cliengguid
     */
    private String logoClengGuid = null;
    
    @Autowired
    private IOuService iuService;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = businseeImpl.getAuditSpBusinessByRowguid(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }else{
            preName = dataBean.getBusinessname();
            String leadouguid = dataBean.get("leadouguid");
            if(StringUtil.isNotBlank(leadouguid)){
                FrameOu ou = iuService.getOuByOuGuid(leadouguid);
                if(ou!=null){
                    addCallbackParam("leadouname",ou.getOuname());
                }
            }
            
        }

       
        
    }

    /**
     * 保存修改
     */
    public void save() {
        String msg = "";
        EpointFrameDsManager.begin(null);
        dataBean.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
        dataBean.setTaskoutimgguid(this.getViewData("taskoutimgguid"));
        dataBean.set("logoattachguid",(this.getViewData("logoattachguid")));
        dataBean.set("management", this.getViewData("managementguid"));
        dataBean.setOperatedate(new Date());
        AuditCommonResult<String> editResult = businseeImpl.updateAuditSpBusiness(dataBean, preName);
        EpointFrameDsManager.commit();
        if (StringUtil.isNotBlank(editResult.getSystemDescription())) {
            msg = "99";
        } else if (StringUtil.isNotBlank(editResult.getBusinessDescription())) {
            msg = "88";
        } else {
            if ("2".equals(dataBean.getBusinesstype())) {
                syncWindowBusiness("enable", dataBean.getRowguid());
            }
            msg = "保存成功！";
        }

        addCallbackParam("msg", msg);
    }

    public void syncWindowBusiness(String SendType, String businessGuid) {
        // TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
        // 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
        try {
            String RabbitMQMsg = "handleSerachIndexBusiness:" + SendType + ";" + businessGuid;
            ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    /**
     * 一件事logo
     * @return
     */
    public FileUploadModel9 getLogoAttachUploadModel() {
        if (logoAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("logoattachguid", storage.getAttachGuid());
                    }
                    logoAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("logoattachguid"))) {
                this.addViewData("logoattachguid", dataBean.get("logoattachguid"));
            }
            dataBean.set("logoattachguid",this.getViewData("logoattachguid"));
            if (StringUtil.isNotBlank(this.getViewData("logoattachguid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("logoattachguid"));
                if (frameAttachInfo != null) {
                    logoClengGuid = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(logoClengGuid)) {
                logoAttachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(logoClengGuid, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            } else {
                logoAttachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return logoAttachUploadModel;
    }


    public FileUploadModel9 getTaskoutimgAttachUploadModel() {
        if (taskoutimgAttachUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("taskoutimgguid", storage.getAttachGuid());
                    }
                    taskoutimgAttachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("taskoutimgguid"))) {
                this.addViewData("taskoutimgguid", dataBean.getTaskoutimgguid());
            }
            dataBean.setTaskoutimgguid(this.getViewData("taskoutimgguid"));
            if (StringUtil.isNotBlank(this.getViewData("taskoutimgguid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("taskoutimgguid"));
                if (frameAttachInfo != null) {
                    taskoutimgClengGuid = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(taskoutimgClengGuid)) {
                taskoutimgAttachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(taskoutimgClengGuid, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            } else {
                taskoutimgAttachUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return taskoutimgAttachUploadModel;
    }

    public FileUploadModel9 getManagementUploadModel() {
        if (managementUploadModel == null) {
            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    if (attach instanceof FrameAttachStorage) {
                        FrameAttachStorage storage = (FrameAttachStorage) attach;
                        addViewData("managementguid", storage.getAttachGuid());
                    }
                    managementUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
                    return true;
                }

            };
            if (StringUtil.isBlank(this.getViewData("managementguid"))) {
                this.addViewData("managementguid", dataBean.getStr("management"));
            }
            dataBean.set("management", this.getViewData("managementguid"));
            if (StringUtil.isNotBlank(this.getViewData("managementguid"))) {
                FrameAttachStorage frameAttachInfo = frameattacinfonewservice
                        .getAttach(this.getViewData("managementguid"));
                if (frameAttachInfo != null) {
                    managementClengGuid = frameAttachInfo.getCliengGuid();
                }
            }

            if (StringUtil.isNotBlank(managementClengGuid)) {
                managementUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(managementClengGuid, null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            } else {
                managementUploadModel = new FileUploadModel9(
                        new DefaultFileUploadHandlerImpl9(UUID.randomUUID().toString(), null, null, handler,
                                userSession.getUserGuid(), userSession.getDisplayName()));
            }
        }
        return managementUploadModel;
    }


    public AuditSpBusiness getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpBusiness dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getBusinesstypeModel() {
        if (businesstypeModel == null) {
            businesstypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "auditsp_ztlb", null, false));
        }
        return this.businesstypeModel;
    }

    public List<SelectItem> getCommonModel() {
        if (commonModel == null) {
            commonModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.commonModel;
    }

    public List<SelectItem> getIswssbModel() {
        if (iswssbModel == null) {
            iswssbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.iswssbModel;
    }

    public List<SelectItem> getAreacodeModel() {
        areacodeModel = new ArrayList<SelectItem>();
        List<Record> listmap = auditServiceCenter.GetXiaquList();
        for (Record rec : listmap) {
            areacodeModel.add(new SelectItem(rec.get("XiaQuCode"), rec.get("XiaQuName")));
        }
        return areacodeModel;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public List<SelectItem> getDao_xc_numModel() {
        if (dao_xc_numModel == null) {
            dao_xc_numModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "现场次数", null, false));
        }
        return this.dao_xc_numModel;
    }

    public List<SelectItem> getBusinesskindModel() {
        if (businesskindModel == null) {
            businesskindModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "套餐类型", null, false));
        }
        return businesskindModel;
    }

    public List<SelectItem> getBusinessformModel() {
        if (businessformModel == null) {
            businessformModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "套餐电子表单", null, false));
        }
        return businessformModel;
    }

    public List<SelectItem> getSplclxModel() {
        if (splclxmodel == null) {
            splclxmodel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "审批流程类型", null, false));
        }
        return splclxmodel;
    }

    public List<SelectItem> getSplclxV3Model() {
        if (splclxV3model == null) {
            splclxV3model = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "国标_审批流程类型_3.0", null, false));
        }
        return splclxV3model;
    }

    public List<SelectItem> getIssqlbModel() {
        if (issqlbModel == null) {
            issqlbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "申请人类型", null, false));
        }
        return this.issqlbModel;
    }

    public List<SelectItem> getBackgroundImgModel() {
        if (backgroundImgModel == null) {
            backgroundImgModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "一件事背景图片", null, false));
        }
        return backgroundImgModel;
    }

    public List<SelectItem> getCatalogModel() {
        catalogModel = new ArrayList<SelectItem>();
        String areacode = "";
        // 证照和批文要按区域划分
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }

        List<CertCatalog> list = certConfigExternalImpl.selectCatalogByAreaCode(
                areacode, "", "1", "", false);

        for (CertCatalog catalog : list) {
            catalogModel.add(new SelectItem(catalog.getCertcatalogid(), catalog.getCertname()));
        }
        return catalogModel;
    }

    public List<SelectItem> getSqsbModel() {
        sqsbModel = new ArrayList<SelectItem>();
        String areacode = ZwfwUserSession.getInstance().getAreaCode();
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("areacode", areacode);
        sql.eq("businesstype", "2");
        sql.eq("del", "0");
        List<AuditSpBusiness> list = businseeImpl.getAllAuditSpBusiness(sql.getMap()).getResult();

        for (AuditSpBusiness business : list) {
            sqsbModel.add(new SelectItem(business.getRowguid(), business.getBusinessname()));
        }
        return sqsbModel;
    }

    public List<SelectItem> getIfModel() {
        if (ifModel == null) {
            ifModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.ifModel;
    }

    public List<SelectItem> getPhaseModel() {
        if (phaseModel == null) {
            phaseModel = DataUtil.convertMap2ComboBox(CodeModalFactory.factory("下拉列表", "审批阶段", null, false));
            // 仅保留1234四个阶段
            phaseModel = phaseModel.stream()
                    .filter(phase -> ZwfwConstant.CONSTANT_STR_ONE.equals(String.valueOf(phase.getValue()))
                            || ZwfwConstant.CONSTANT_STR_TWO.equals(String.valueOf(phase.getValue()))
                            || ZwfwConstant.CONSTANT_STR_THREE.equals(String.valueOf(phase.getValue()))
                            || "4".equals(String.valueOf(phase.getValue())))
                    .collect(Collectors.toList());
        }
        return phaseModel;
    }

}
