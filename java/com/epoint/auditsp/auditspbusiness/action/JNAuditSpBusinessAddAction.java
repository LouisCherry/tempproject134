package com.epoint.auditsp.auditspbusiness.action;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 主题配置表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017-03-02 10:51:09]
 */
@SuppressWarnings("unchecked")
@RestController("jnauditspbusinessaddaction")
@Scope("request")
public class JNAuditSpBusinessAddAction extends BaseController {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = -1515237959913265019L;

    /**
     * 主题配置表实体对象
     */
    private AuditSpBusiness dataBean = null;

    /**
     * 主题类别，这里主要是为了区分是建设项目的还是一般并联审批的下拉列表model
     */
    private List<SelectItem> businesstypeModel = null;
    /**
     * 套餐类别
     */
    private List<SelectItem> businesskindModel = null;
    /**
     * 审批流程类型
     */
    private List<SelectItem> splclxmodel = null;
    /**
     * 3.0国标审批流程类型
     */
    private List<SelectItem> splclxV3model = null;
    /**
     * 是否单选按钮组model
     */
    private List<SelectItem> commonModel = null;
    /**
     * 辖区编码下拉列表model
     */
    private List<SelectItem> areacodeModel = null;
    /**
     * 套餐电子表单
     */
    private List<SelectItem> businessformModel = null;
    /**
     * 辖区编码下拉列表model
     */
    @Autowired
    private IAuditOrgaArea auditOrgaArea;
    /**
     * 现场次数下拉列表model
     */
    private List<SelectItem> dao_xc_numModel = null;
    /**
     * 外部流程图modal
     */
    private FileUploadModel9 taskoutimgAttachUploadModel;
    /**
     * 外部流程图guid
     */
    private String taskoutimgguid;
    /**
     * 附件cliengguid
     */
    private String taskoutimgClengGuid = null;
    /**
     * 附件信息操作service
     */
    @Autowired
    private IAttachService frameattacinfonewservice;
    /**
     * 是否网上申报下拉列表
     */
    private List<SelectItem> iswssbModel = null;

    @Autowired
    private IAuditSpBusiness businseeImpl;

    /**
     * 并联审批阶段接口
     */
    @Autowired
    private IAuditSpPhase auditspphaseImpl;

    @Override
    public void pageLoad() {
        dataBean = new AuditSpBusiness();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        String msg = "";
        String DisplayName = userSession.getDisplayName();
        // msg=bussinessBiz.addAuditSpBusiness(dataBean,DisplayName);        
        if (dataBean.getOrdernumber() == null) {
            dataBean.setOrdernumber(0);
        }
        dataBean.setAreacode(ZwfwUserSession.getInstance().getAreaCode());
        dataBean.setDel("0");
        dataBean.setTaskoutimgguid(this.getViewData("taskoutimgguid"));
        String guid = UUID.randomUUID().toString();
        dataBean.setRowguid(guid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(DisplayName);
        dataBean.setDel("0");
        try {
            EpointFrameDsManager.begin(null);
            AuditCommonResult<String> addResult = businseeImpl.addAuditSpBusiness(dataBean);
            if (StringUtil.isNotBlank(addResult.getSystemDescription())) {
                msg = "99";
            } else if (StringUtil.isNotBlank(addResult.getBusinessDescription())) {
                msg = "88";
            } else {
                // 主题创建成功
                if ("2".equals(dataBean.getBusinesstype())) {
                    syncWindowBusiness("enable", dataBean.getRowguid());
                    msg = dataBean.getRowguid();
                }
                // 如果是建设审批,当前主题关联初始化的四个阶段
                if ("1".equals(dataBean.getBusinesstype())) {
                    // 创建一维数组，值为阶段名，下标为排序号
                    String[] phases = {"并行推进", "竣工验收", "施工许可", "工程建设许可", "立项用地规划许可"};
                    String[] phaseids = {"5", "4", "3", "2", "1"};
                    // 遍历数组，实例化四个阶段，并插入数据库，businessguid是当前主题id
                    // 计数，判断是否四个阶段都创建成功
                    int count = 0;
                    for (int i = 0; i < phases.length; i++) {
                        AuditSpPhase aPhase = new AuditSpPhase();
                        aPhase.setRowguid(UUID.randomUUID().toString());
                        aPhase.setOperateusername(userSession.getDisplayName());
                        // businessguid是dataBean的rowguid
                        aPhase.setBusinedssguid(dataBean.getRowguid());
                        aPhase.setOperatedate(new Date());
                        // 排序号是Index
                        aPhase.setOrdernumber(i);
                        // 阶段名是数组的值
                        aPhase.setPhasename(phases[i]);
                        // 保存对应基本阶段id
                        aPhase.setPhaseId(phaseids[i]);
                        aPhase.setSpjdsx(365);
                        // 不可子申报
                        aPhase.setAllowmultisubapply("0");
                        // 是否第一个阶段,暂不设置
                        aPhase.setFirstphase("0");

                        AuditCommonResult<String> addPhaseResult = auditspphaseImpl.addAuditSpPhase(aPhase);
                        // 无重复且添加过程没有失败,count+1
                        if (StringUtil.isBlank(addPhaseResult.getSystemDescription())
                                && StringUtil.isBlank(addPhaseResult.getBusinessDescription())) {
                            count++;
                        }
                    }
                    // 四个阶段都创建成功,则返回主题id;否则返回99
                    if (count == 5) {
                        msg = dataBean.getRowguid();
                    } else {
                        msg = "99";
                    }
                }

                if ("3".equals(dataBean.getBusinesstype()) || "6".equals(dataBean.getBusinesstype())) {
                    msg = dataBean.getRowguid();
                }

            }
            EpointFrameDsManager.commit();
            addCallbackParam("msg", msg);
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
            EpointFrameDsManager.rollback();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    public void syncWindowBusiness(String SendType, String businessGuid) {
        try {
            String RabbitMQMsg = "handleSerachIndexBusiness:" + SendType + ";" + businessGuid;
            ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);
        } catch (Exception e) {
            log.info("========Exception信息========" + e.getMessage());
        }
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditSpBusiness();
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
            /* if(StringUtil.isBlank(this.getViewData("taskoutimgguid"))){
                this.addViewData("taskoutimgguid", dataBean.getTaskoutimgguid());
            }*/
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

    public AuditSpBusiness getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpBusiness();
        }
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

    public List<SelectItem> getAreacodeModel() {
        areacodeModel = new ArrayList<SelectItem>();

        String areaCode = ZwfwUserSession.getInstance().getAreaCode();
        auditOrgaArea.getAreaByAreacode(areaCode).getResult();
        String areaName = auditOrgaArea.getAreaByAreacode(areaCode).getResult().getXiaquname();
        areacodeModel.add(new SelectItem(areaCode, areaName));

        //        List<Record> listmap = auditServiceCenter.GetXiaquList();
        //        for (Record rec : listmap) {
        //            areacodeModel.add(new SelectItem(rec.get("XiaQuCode"), rec.get("XiaQuName")));
        //        }
        return areacodeModel;

    }

    public List<SelectItem> getIswssbModel() {
        if (iswssbModel == null) {
            iswssbModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return this.iswssbModel;
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


    public String getTaskoutimgguid() {
        return taskoutimgguid;
    }

    public void setTaskoutimgguid(String taskoutimgguid) {
        this.taskoutimgguid = taskoutimgguid;
    }

}
