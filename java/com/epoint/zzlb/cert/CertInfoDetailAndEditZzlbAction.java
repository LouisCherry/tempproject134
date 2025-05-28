package com.epoint.zzlb.cert;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.certlog.domain.CertLog;
import com.epoint.cert.basic.certlog.inter.ICertLog;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.common.cert.authentication.AttachBizlogic;
import com.epoint.common.cert.authentication.CertInfoBizlogic;
import com.epoint.common.cert.authentication.CertLogBizlogic;
import com.epoint.common.cert.authentication.ConfigBizlogic;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.HttpUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseResult;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 证照基本信息表详情页面对应的后台
 *
 * @author dingwei
 * @version [版本号, 2017-11-01 16:01:53]
 */
@RestController("certinfodetailandeditzzlbaction")
@Scope("request")
public class CertInfoDetailAndEditZzlbAction extends BaseController
{

    private static final long serialVersionUID = 1L;
    
    /**
     * 日志
     */
    private Logger log = LogUtil.getLog(CertInfoDetailAndEditZzlbAction.class);
    
    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 证照基本信息表实体对象
     */
    private CertInfo certinfo;

    /**
     * 证照照面信息表实体对象
     */
    private CertInfoExtension dataBean;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 元数据api
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 证照目录api
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 证照基本信息api
     */
    @Autowired
    private ICertInfo iCertInfo;
    

    /**
     * 日志api
     */
    @Autowired
    private ICertLog certLogService;
    
    /**
     * 行业标识下拉列表model
     */
    private List<SelectItem> businessguidModel;

    /**
     * 证照信息bizlogic
     */
    private CertInfoBizlogic certInfoBizlogic = new CertInfoBizlogic();

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    /**
     * 证照附件（正本）上传
     */
    private FileUploadModel9 certFileUploadModel;

    /**
     * 证照附件（副本）上传
     */
    private FileUploadModel9 copyCertFileUploadModel;

    /**
     * 证照缩略图上传
     */
    private FileUploadModel9 sltImageUploadModel;

    /**
     * 照面信息附件上传
     */
    private FileUploadModel9 fileUploadModel;

    /**'
     * 照面信息主键
     */
    private String extendguid;

    /**
     * 证照基本信息主键
     */
    private String guid;

    /**
     * 审核不通过意见
     */
    private String opinion;
    
    /**
     * 行业标识
     */
    public String businessguid;
    @Autowired
    private IAuditSpTask auditSpTaskService;
 //一业一证行业相关方法
    
    private DataGridModel<AuditSpTask> modelTask = null;
    
    private List<AuditSpTask> spTasks = null;
    
    @Autowired
    private IBusinessLicenseResult businessLicenseResult;
    @Autowired
    private IAuditSpBusiness auditSpBusiness;
    @Override
    public void pageLoad() {
        guid = getRequestParameter("guid");
        if (StringUtil.isNotBlank(guid)) {
            // 获取基本信息
            certinfo = iCertInfo.getCertInfoByRowguid(guid);

            if (certinfo == null) {
                addCallbackParam("msg", "证照基本信息不存在！");
                return;
            }
            if(StringUtil.isNotBlank(certinfo.getCertownercerttype())){
                certinfo.setCertownercerttype(certinfo.getCertownercerttype().replaceAll("\\^", ","));
            }
            // 获取照面信息
            Map<String, Object> filter = new HashMap<>();
            // 设置基本信息guid
            filter.put("certinfoguid", guid);
            dataBean = noSQLSevice.find(CertInfoExtension.class, filter, false);
            if (dataBean == null) {
                addCallbackParam("msg", "证照业务信息不存在！");
                return;
            }
            extendguid = dataBean.getRowguid();
            // 获得元数据配置表所有顶层节点
            List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certinfo.getCertareaguid());
            // 照面信息日期字段格式YYYY-MM-DD
            certInfoBizlogic.convertDate(metadataList, dataBean);
            // 返回渲染的字段(子表查看模式)
            addCallbackParam("controls",
                    certInfoBizlogic.generateMapUseSubExtension("certinfodetailaction", metadataList,
                            CertConstant.MODE_DETAIL, CertConstant.SUB_MODE_DETAIL));

            // 回传目录模板文件（副本）个数
            int copyTempletCount = 0;
            CertCatalog certCatalog = iCertCatalog.getCatalogDetailByrowguid(certinfo.getCertareaguid());
            if (certCatalog != null && StringUtil.isNotBlank(certCatalog.getCopytempletcliengguid())) {
                copyTempletCount = iAttachService.getAttachCountByClientGuid(certCatalog.getCopytempletcliengguid());
            }
            addCallbackParam("copyTempletCount", copyTempletCount);

            // 注销、挂失显示备注
            if (CertConstant.CERT_STATUS_CANCEL.equals(certinfo.getStatus())
                    || CertConstant.CERT_STATUS_LOSE.equals(certinfo.getStatus())) {
                addCallbackParam("status", certinfo.getStatus());
            }
            if(CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(certinfo.getAuditstatus())){
                if(!isPostback()){
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("targetid", certinfo.getCertid());
                    sqlUtils.eq("operatetype", CertConstant.LOG_OPERATETYPE_NOTPASS);
                    sqlUtils.setOrderDesc("operatedate");
                    List<CertLog> certLogs = certLogService.getCertLogList(sqlUtils.getMap());
                    if (certLogs.size() > 0) {
                        opinion = certLogs.get(0).getNote();
                        if (StringUtil.isNotBlank(opinion)) {
                            opinion = opinion.split("审核意见:")[1];
                        }
                    }
                    addCallbackParam("auditstatus", certinfo.getAuditstatus());
                }

            }
            // 回传有年检时间
            if (certinfo.getInspectiondate() != null) {
                addCallbackParam("hasinspectiondate", CertConstant.CONSTANT_STR_ONE);
            } else {
                addCallbackParam("hasinspectiondate", CertConstant.CONSTANT_STR_ZERO);
            }
            String certrowguid = certinfo.getRowguid();
//            String taskid = 
            //获取一业一证并联办件结果实体
            String sql = "select * from BusinessLicense_result where certrowguid = ?1";
            List<BusinessLicenseResult> businessLicenseResultList = businessLicenseResult.findList(sql, certinfo.getRowguid());
            if(StringUtil.isNotBlank(businessLicenseResultList) && !businessLicenseResultList.isEmpty()) {
                
            }else {
                addCallbackParam("nofile",true);
            }
//            BusinessLicenseResult businessLicenseResult = businessLicenseResult.find(sql,certinfo.getRowguid());
            //渲染基本信息
            addCallbackParam("basiccontrols", certInfoBizlogic.generateBasicDefaultMap(metadataList, true, "certinfo"));
            addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
            addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
            //将多持有者中^符号更换为,显示,无需逻辑判断
            if(StringUtil.isNotBlank(certinfo.getCertownername())){
                certinfo.setCertownername(certinfo.getCertownername().replace("^", ","));
            }
            if(StringUtil.isNotBlank(certinfo.getCertownerno())){
                certinfo.setCertownerno(certinfo.getCertownerno().replace("^", ","));
            }
            if(StringUtil.isNotBlank(certinfo.getCertawarddept())){
                certinfo.setCertawarddept(certinfo.getCertawarddept().replace("^", ","));
            }
        }
    }

    /**
     *  证照附件（正本）上传
     *  @return
     */
    public FileUploadModel9 getCertFileUploadModel() {
        if (certFileUploadModel == null) {
            if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
                certFileUploadModel = new AttachBizlogic().miniOaUploadAttach(certinfo.getCertcliengguid(), null);
            }
        }
        return certFileUploadModel;
    }

    /**
     *  证照附件（副本）上传
     *  @return
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (copyCertFileUploadModel == null) {
            if (StringUtil.isNotBlank(certinfo.getCopycertcliengguid())) {
                copyCertFileUploadModel = new AttachBizlogic().miniOaUploadAttach(certinfo.getCopycertcliengguid(), null);
            }
        }
        return copyCertFileUploadModel;
    }

    /**
     *  证照缩略图model
     *  @return
     */
    public FileUploadModel9 getSltImageUploadModel() {
        if (sltImageUploadModel == null && certinfo != null) {
            if (StringUtil.isNotBlank(certinfo.getSltimagecliengguid())) {
                sltImageUploadModel = new AttachBizlogic().miniOaUploadAttach(certinfo.getSltimagecliengguid(), null);
            }
        }
        return sltImageUploadModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getBusinessguidModel() {
        if (businessguidModel == null) {
            businessguidModel = new ArrayList<SelectItem>();
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.eq("BUSINESSTYPE", "3");
            sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
            List<AuditSpBusiness> businesslist = auditSpBusiness.getAllAuditSpBusiness(sqlc.getMap()).getResult();
            for (AuditSpBusiness auditSpBusiness : businesslist) {
                SelectItem item = new SelectItem();
                item.setText(auditSpBusiness.getBusinessname());
                item.setValue(auditSpBusiness.getRowguid());
                businessguidModel.add(item);
            }
        }
        return this.businessguidModel;
    }
    public DataGridModel<AuditSpTask> getDataGridTask() {
        spTasks = new ArrayList<AuditSpTask>();
        String sql = "select * from BusinessLicense_result where certrowguid = ?1";
        List<BusinessLicenseResult> businessLicenseResultList = businessLicenseResult.findList(sql, certinfo.getRowguid());
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpTask>()
            {
                @Override
                public List<AuditSpTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (businessLicenseResultList != null && businessLicenseResultList.size() > 0) {
                        for (BusinessLicenseResult result1 : businessLicenseResultList) {
                            String taskid = result1.getTaskid();
                            String businessguid = result1.getBusinessguid();
                            String businessname = "";
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("taskid=", taskid);
                            if(StringUtil.isNotBlank(businessguid)) {
                                map.put("businessguid=", businessguid);
                                AuditSpBusiness spBusiness =auditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                                if(StringUtil.isNotBlank(spBusiness)) {
                                    businessname = spBusiness.getBusinessname();
                                }
                            }
                            List<AuditSpTask> spsTask = auditSpTaskService.getAllAuditSpTask(map).getResult();
                            String cliengguid = result1.getClientguid();
                            if(spsTask != null && spsTask.size() > 0) {
                                AuditSpTask task = spsTask.get(0);
                                task.set("cliengguid", cliengguid);
                                task.set("businessname", businessname);
                                spTasks.add(task);
                                
                            }
                        }
                    }
                    this.setRowCount(spTasks.size());
                    return spTasks;
                }
            };
        }
        return modelTask;
    }
    
    public DataGridModel<AuditSpTask> getDateGridTask() {
        spTasks = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessguid).getResult();
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpTask>()
            {
                @Override
                public List<AuditSpTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    AuditSpBusiness business = auditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();
                    if (spTasks != null && spTasks.size() > 0) {
                        for (AuditSpTask task : spTasks) {
                            String sql = "select * from BusinessLicense_result where certrowguid=?1 and task_id=?2 ";
                            BusinessLicenseResult result = businessLicenseResult.find(sql, certinfo.getRowguid(), task.getTaskid());
                            /*String sql1 = "select * from BusinessLicense_result where certrowguid=?1";
                            List<BusinessLicenseResult> listResult = businessLicenseResult.findList(sql1, certinfo.getRowguid());
                            if(StringUtil.isNotBlank(listResult) && !listResult.isEmpty()) {
                                for (BusinessLicenseResult bresult : listResult) {
                                    businessLicenseResult.deleteByGuid(bresult.getRowguid());
                                }
                            }*/
                            String cliengguid = "";
                            if(result == null) {
                                result = new BusinessLicenseResult();
                                cliengguid = UUID.randomUUID().toString();
                                result.setOperatedate(new Date());
                                result.setOperateusername(userSession.getDisplayName());
                                result.setRowguid(UUID.randomUUID().toString());
                                result.setTaskid(task.getTaskid());
                                result.setCertrowguid(certinfo.getRowguid());
                                result.setClientguid(cliengguid);
                                result.setBusinessguid(businessguid);
                                businessLicenseResult.insert(result);
                            }else {
                                cliengguid = result.getClientguid();
                            }
                            task.set("businessname", business.getBusinessname());
                            task.set("cliengguid", cliengguid);
                        }
                    }
                    this.setRowCount(spTasks.size());
                    return spTasks;
                }
            };
        }
        return modelTask;
    }
    /**
     *  照面信息附件上传
     *  @param fieldName
     *  @return
     */
    public FileUploadModel9 getFileUploadModel(String fieldName) {
        if (dataBean != null && fileUploadModel == null) {
            if (StringUtil.isNotBlank(dataBean.getStr(fieldName))) {
                fileUploadModel = new AttachBizlogic().miniOaUploadAttach(dataBean.getStr(fieldName), null);
            }
        }
        return fileUploadModel;
    }

    /**
     * 保存并关闭
     * @description
     * @author shibin
     * @date  2020年6月22日 上午11:57:32
     */
    public void addEditMessageLog() {
        try {
            log.info("=====certguid=====:" + guid);
                CertInfo certinfo = iCertInfo.getCertInfoByRowguid(guid);
                // 添加日志
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), certinfo,
                        "子证维护", "", null);
        }
        catch (Exception e) {
            addCallbackParam("msg", "子证维护失败:" + e);
        }
    }
    
    public CertInfo getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(CertInfo certinfo) {
        this.certinfo = certinfo;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public CertInfoExtension getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertInfoExtension dataBean) {
        this.dataBean = dataBean;
    }

    public String getExtendguid() {
        return extendguid;
    }

    public void setExtendguid(String extendguid) {
        this.extendguid = extendguid;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
    public String getBusinessguid() {
        return businessguid;
    }

    public void setBusinessguid(String businessguid) {
        this.businessguid = businessguid;
    }
}
