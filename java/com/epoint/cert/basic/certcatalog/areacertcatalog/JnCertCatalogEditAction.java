package com.epoint.cert.basic.certcatalog.areacertcatalog;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.ConstValue9;
import com.epoint.basic.faces.tree.EpointTreeHandler9;
import com.epoint.basic.faces.tree.LazyTreeModal9;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.cert.basic.certcatalog.areacertcatalog.inter.ICertCatalog;
import com.epoint.cert.basic.certcatalog.certcatalogou.inter.ICertCatalogOu;
import com.epoint.cert.basic.certcatalog.certmetadata.domain.CertMetadata;
import com.epoint.cert.basic.certcatalog.certmetadata.inter.ICertMetaData;
import com.epoint.cert.commonutils.CertConstant;
import com.epoint.cert.commonutils.NoSQLSevice;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.common.cert.authentication.*;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.exception.security.ReadOnlyException;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 证照编辑页面对应的后台
 *
 * @author lsting
 * @version [版本号, 2017-10-29 19:45:16]
 */
@RestController("jncertcatalogeditaction")
@Scope("request")
public class JnCertCatalogEditAction extends BaseController
{

    private static final long serialVersionUID = 5524292473322446471L;

    /**
     * 数据表清单实体对象
     */
    private CertCatalog dataBean = null;

    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;

    /**
     * 证照目录API
     */
    @Autowired
    private ICertCatalog iCertCatalog;

    /**
     * 证照目录关系表API
     */
    @Autowired
    private ICertCatalogOu iCertCatalogOu;

    /**
     * 元数据API
     */
    @Autowired
    private ICertMetaData iCertMetaData;

    /**
     * 消息api
     */
    @Autowired
    private IMessagesCenterService messagesService;

    /**
     * 角色api
     */
    @Autowired
    private IRoleService roleService;

    /**
     * 人员api
     */
    @Autowired
    private IUserService userService;

    /**
     * 部门api
     */
    @Autowired
    private IOuService ouService;

    /**
     * 代码项api
     */
    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 证照持有人类型
     */
    private List<SelectItem> belongtypeModel = null;

    /**
     * 所属行业类型
     */
    private List<SelectItem> industryModel = null;

    /**
     * 证照定义机构代码下拉列表model
     */
    private List<SelectItem> authoritycodeModel = null;

    /**
     * 父证照目录下拉列表model
     */
    private List<SelectItem> parentModel = null;

    /**
     * 证照分类
     */
    private List<SelectItem> kindModel = null;

    /**
     * 证照定义机构级别下拉列表model
     */
    private List<SelectItem> levelModel = null;

    /**
     * 证照颁发机构级别下拉列表model
     */
    private List<SelectItem> issuingLevelModel = null;

    /**
     * 证照附件（正本）上传model
     */
    private FileUploadModel9 templetFileUploadModel;

    /**
     * 证照附件（副本）上传model
     */
    private FileUploadModel9 copyCertFileUploadModel;
    
    /**
     * 套打证照附件（正本）上传model
     */
    private FileUploadModel9 tdcertFileUploadModel;

    /**
     * 套打证照附件（副本）上传model
     */
    private FileUploadModel9 tdcopyCertFileUploadModel;


    /**
     * 证照彩色附件上传model
     */
    private FileUploadModel9 colourFileUploadModel;

    /**
     * 证照灰色附件上传model
     */
    private FileUploadModel9 grayFileUploadModel;

    /**
     * 证照空白底图上传model
     */
    private FileUploadModel9 blankFileUploadModel;

    /**
     * 证照示例底图上传model
     */
    private FileUploadModel9 exampleFileUploadModel;

    /**
     * 代码项bizlogic
     */
    private CodeBizlogic codeBizlogic = new CodeBizlogic();

    /**
     * 操作日志bizlogic
     */
    private CertLogBizlogic certLogBizlogic = new CertLogBizlogic();

    /**
     * 操作附件bizlogic
     */
    private AttachBizlogic attachBizlogic = new AttachBizlogic();

    /**
     * 证照目录bizlogic
     */
    private CertcatalogBizlogic certcatalogBizlogic = new CertcatalogBizlogic();

    /**
     * 配置参数（是否开启目录审核）
     */
    private String configValue;

    /**
     * 部门树
     */
    private LazyTreeModal9 treeModel;

    private boolean isblankCode;
    
    private String templetcliengguid;
    
    private String copytempletcliengguid;

    private String tdtempletcliengguid;
    
    private String tdcopytempletcliengguid;

    private String industry;

    private String tycertcatcode;

    private String certificatetypecode;
    
    private String yyyzcertcode;

    /**
     * MongoDB\HBase通用service
     */
    private NoSQLSevice noSQLSevice = new NoSQLSevice();

    /**
     * 选择字段（下拉框）
     */
    private List<SelectItem> pushcerttypeModel;

    private String ispush;
    private String pushcerttypeStr;



    @Override
    public void pageLoad() {
        // 是否开启目录审核
        configValue = iConfigService.getFrameConfigValue("IsEnableCatalogAudit");
        //目录申请不用审核
        if(CertConstant.CONSTANT_STR_ONE.equals(getRequestParameter("isapply"))){
            configValue = CertConstant.CONSTANT_STR_ZERO;
        }
        if (dataBean == null) {
            // 获取修改记录rowguid
            String rowguid = getRequestParameter("guid");
            dataBean = iCertCatalog.getCatalogDetailByrowguid(rowguid);
            if (dataBean == null) {
                dataBean = new CertCatalog();
            }
        }
        //如果dataBean不是空，回填，推送字段
        if(dataBean != null){
            ispush = dataBean.get("is_push");
            pushcerttypeStr = dataBean.get("pushcerttype");
        }
        //system.out.println("dataBean:"+dataBean);
        if(StringUtil.isNotBlank(dataBean.getVersion())){
            if(dataBean.getVersion() > 1){
                tycertcatcode = dataBean.getTycertcatcode();
            }
            if(StringUtil.isNotBlank(dataBean.getCertificatetypecode()) && dataBean.getVersion() > 1){
                certificatetypecode = dataBean.getCertificatetypecode();
            }
        }
        isblankCode = StringUtil.isBlank(dataBean.getCertificatetypecode());
        if (StringUtil.isNotBlank(dataBean.getParentid())) {
            addCallbackParam("parentid", dataBean.getParentid());
            industry = dataBean.getIndustry();
        }
        if(StringUtil.isNotBlank(dataBean.getTempletcliengguid())){
           templetcliengguid= dataBean.getTempletcliengguid();
        }else{
           templetcliengguid= UUID.randomUUID().toString();            
        }
        if (StringUtil.isNotBlank(dataBean.getTdTempletcliengguid())) {
        	tdtempletcliengguid = dataBean.getTdTempletcliengguid();
        }else {
        	tdtempletcliengguid = UUID.randomUUID().toString();
        }
        //system.out.println("tdtempletcliengguid:"+dataBean.getTdTempletcliengguid());
        if (StringUtil.isNotBlank(dataBean.getTdTempletcliengguid())) {
        	tdcopytempletcliengguid = dataBean.getTdCopytempletcliengguid();
        }else {
        	tdcopytempletcliengguid = UUID.randomUUID().toString();
        }
        if(StringUtil.isNotBlank(dataBean.getCopytempletcliengguid())){
           copytempletcliengguid= dataBean.getCopytempletcliengguid();
        }else{
           copytempletcliengguid= UUID.randomUUID().toString();            
        }
        if (StringUtil.isBlank(getViewData("templetcliengguid"))) {
            addViewData("templetcliengguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("copytempletcliengguid"))) {
            addViewData("copytempletcliengguid", UUID.randomUUID().toString());
        }
        
        // 证照模板文件(是用数维时需要是用)
        if (StringUtil.isBlank(getViewData("templetattachguid"))) {
            addViewData("templetattachguid", UUID.randomUUID().toString());
        }
        if (StringUtil.isBlank(getViewData("copytempletattachguid"))) {
            addViewData("copytempletattachguid", UUID.randomUUID().toString());
        }
        
        if(StringUtil.isNotBlank(ConfigUtil.getConfigValue("ofdserviceurl"))){
            addCallbackParam("suwell", CertConstant.CONSTANT_STR_ONE);
        }
        // 回传模板文件attachguid（正本）
        addCallbackParam("templetattachguid", getViewData("templetattachguid"));
        // 回传模板文件attachguid（副本）
        addCallbackParam("copytempletattachguid", getViewData("copytempletattachguid"));
        // 回传模板文件（正本）
        addCallbackParam("templetcliengguid", templetcliengguid);
        // 回传模板文件（副本）
        addCallbackParam("copytempletcliengguid", copytempletcliengguid);
        // 返回审核状态
        addCallbackParam("auditstatus", dataBean.getAuditstatus());
        // 回传目录主键
        addCallbackParam("rowguid", dataBean.getRowguid());
        // 回传版本号
        addCallbackParam("version", dataBean.getVersion());
        addCallbackParam("officeweb365url", new ConfigBizlogic().getOfficeConfig());
        addCallbackParam("ofd", ConfigUtil.getConfigValue("ofdserviceurl"));
        addCallbackParam("ofdpreviewurl", ConfigUtil.getConfigValue("ofdpreviewurl"));
    }

    /**
     * 保存草稿目录
     */
    public void save() {
        if (dataBean == null || dataBean.isEmpty()) {
            return;
        }
        if(StringUtil.isNotBlank(industry) && !industry.equals(dataBean.getIndustry())){
            throw new ReadOnlyException("industry");
        }
        if(StringUtil.isNotBlank(tycertcatcode) && !tycertcatcode.equals(dataBean.getTycertcatcode())){
            throw new ReadOnlyException("tycertcatcode");
        }
        if(StringUtil.isNotBlank(certificatetypecode) && !certificatetypecode.equals(dataBean.getCertificatetypecode())){
            throw new ReadOnlyException("certificatetypecode");
        }
        // 不为待上报、审核不通过目录不可保存（解决多人操作）
        if (!CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(dataBean.getAuditstatus()) && !CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(dataBean.getAuditstatus())) {
            addCallbackParam("msg", "该证照类型不为草稿状态，操作无效！");
            addCallbackParam("canChange", "0");
            return;
        }
        // 证照目录编号是否重复(排除自身目录)
        int count = iCertCatalog.getCertCatalogCountByTyCertCatcode(dataBean.getTycertcatcode(),
                dataBean.getCertcatalogid());
        if (count >= 1) {
            addCallbackParam("msg", "证照类型地方代码重复!");
            addCallbackParam("canChange", "0");
            return;
        }

        //国标且一开始为空
        if(CertConstant.CONSTANT_INT_ONE.equals(dataBean.getIsstandardcert()) && isblankCode){
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            if (iCertCatalog.getCertCatalogCount(sqlUtils.getMap())>0){
                addCallbackParam("msg", "证照类型代码重复!");
                addCallbackParam("isClose", "0");
                return;
            }
        }

        if(StringUtil.isNotBlank(ispush)){
            dataBean.set("is_push",ispush);
        }

        if(StringUtil.isNotBlank(pushcerttypeStr)){
            dataBean.set("pushcerttype",pushcerttypeStr);
        }

        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setUserguid(userSession.getUserGuid());
        // 排序号（默认为0）
        if (dataBean.getOrdernum() == null) {
            dataBean.setOrdernum(CertConstant.CONSTANT_INT_ZERO);
        }
        // 默认启用
        dataBean.setIsenable(CertConstant.CONSTANT_INT_ONE);
        // excel导入的目录字段更新
        if (StringUtil.isBlank(dataBean.getCertcatcode())) {
            dataBean.setCertcatcode(iCertCatalog.generateCertcatcode(dataBean.getBelongtype().split(",")[0], dataBean.getIndustry()));
        }
        //证照定义机构
        dataBean.setCertificatedefineauthorityname(codeItemsService.getItemTextByCodeName("证照定义机构", dataBean.getCertificatedefineauthoritycode()));
        iCertCatalog.updateCatalog(dataBean);
        addCallbackParam("msg", "保存成功！");
        addCallbackParam("canChange", "1");
    }

    /**
     * 归档目录/上报目录
     */
    public void change() {
        if (dataBean == null || dataBean.isEmpty()) {
            return;
        }


        if(StringUtil.isNotBlank(industry) && !industry.equals(dataBean.getIndustry())){
            throw new ReadOnlyException("industry");
        }
        if(StringUtil.isNotBlank(tycertcatcode) && !tycertcatcode.equals(dataBean.getTycertcatcode())){
            throw new ReadOnlyException("tycertcatcode");
        }
        if(StringUtil.isNotBlank(certificatetypecode) && !certificatetypecode.equals(dataBean.getCertificatetypecode())){
            throw new ReadOnlyException("certificatetypecode");
        }


        // 解决多人操作问题（重复归档/上报）
        String errorMsg = "该证照类型已归档，操作无效！";
        // 是否开启目录审核
        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)) { // 启用目录审核
            errorMsg = "该证照类型已上报，操作无效！";
        }
        // 启用目录审核 不为待上报/审核不通过，提示错误信息
        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)&&!CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(dataBean.getAuditstatus()) &&
                !CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(dataBean.getAuditstatus())) {
            addCallbackParam("msg", errorMsg);
            addCallbackParam("canChange", "0");
            return;
        }
        // 不启用目录审核 不为待上报/审核不通过/待审核，提示错误信息
        if (CertConstant.CONSTANT_STR_ZERO.equals(configValue)&&!CertConstant.CERT_CHECK_STATUS_WAIT_REPORT.equals(dataBean.getAuditstatus()) &&
                !CertConstant.CERT_CHECK_STATUS_NOT_PASS.equals(dataBean.getAuditstatus()) && !CertConstant.CERT_CHECK_STATUS_WAIT_CHECK.equals(dataBean.getAuditstatus())) {
            addCallbackParam("msg", errorMsg);
            addCallbackParam("canChange", "0");
            return;
        }

        // 证照目录编号是否重复(排除自身目录)
        int count = iCertCatalog.getCertCatalogCountByTyCertCatcode(dataBean.getTycertcatcode(),
                dataBean.getCertcatalogid());
        if (count >= 1) {
            addCallbackParam("msg", "证照类型地方代码重复!");
            addCallbackParam("canChange", "0");
            return;
        }

        //国标且一开始为空
        if(CertConstant.CONSTANT_INT_ONE.equals(dataBean.getIsstandardcert()) && isblankCode){
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            if (iCertCatalog.getCertCatalogCount(sqlUtils.getMap())>0){
                addCallbackParam("msg", "证照类型代码重复!");
                addCallbackParam("canChange", "0");
                return;
            }
        }

        // 检测是否禁用
        if (CertConstant.CONSTANT_STR_ZERO.equals(configValue)) { // 禁用目录审核
            SqlUtils sql = new SqlUtils();
            sql.eq("isenable", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("certcatalogid", dataBean.getCertcatalogid());
            List<CertCatalog> list = iCertCatalog.selectCertCatalogByCondition(sql.getMap());
            if(list!=null && !list.isEmpty()){
                addCallbackParam("msg", "当前证照类型处于禁用状态无法归档！");
                addCallbackParam("canChange", "0");
                return;
            }
        }
        // 检测是否禁用
        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)) { // 启用目录审核
            SqlUtils sql = new SqlUtils();
            sql.eq("isenable", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("certcatalogid", dataBean.getCertcatalogid());
            List<CertCatalog> list = iCertCatalog.selectCertCatalogByCondition(sql.getMap());
            if(list!=null && !list.isEmpty()){
                addCallbackParam("msg", "当前证照类型处于禁用状态无法上报！");
                addCallbackParam("canChange", "0");
                return;
            }
        }
        //检测父目录是否禁用
       /* if(CertConstant.CONSTANT_STR_ONE.equals(configValue)){
            SqlUtils sql = new SqlUtils();
            sql.eq("isenable", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("ishistory", CertConstant.CONSTANT_STR_ZERO);
            sql.eq("certcatalogid", dataBean.getParentid());
            List<CertCatalog> list = iCertCatalog.selectCertCatalogByCondition(sql.getMap());
            if(list != null && list.size() > 0){
                addCallbackParam("msg", "该证照类型的父目录已被禁用，无法上报!");
                addCallbackParam("canChange", "0");
                return;
            }
        }*/
        // 检测元数据配置
        String msg = checkMetaDataConfig(dataBean);
        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("canChange", "0");
            addCallbackParam("msg", msg);
            return;
        }

        if(StringUtil.isNotBlank(ispush)){
            dataBean.set("is_push",ispush);
        }

        if(StringUtil.isNotBlank(pushcerttypeStr)){
            dataBean.set("pushcerttype",pushcerttypeStr);
        }

        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setUserguid(userSession.getUserGuid());
        // 排序号（默认为0）
        if (dataBean.getOrdernum() == null) {
            dataBean.setOrdernum(CertConstant.CONSTANT_INT_ZERO);
        }
        // excel导入的目录字段更新
        if (StringUtil.isBlank(dataBean.getCertcatcode())) {
            dataBean.setCertcatcode(iCertCatalog.generateCertcatcode(dataBean.getBelongtype().split(",")[0], dataBean.getIndustry()));
        }

        if(StringUtil.isBlank(dataBean.getCertificatetypecode())){
            // 为空，需要自动生成证照类型代码
            dataBean.setCertificatetypecode(iCertCatalog.generateCertTyCertcatcode(dataBean.getCertificatedefineauthoritycode()));
            //由于可以手填，需要验证唯一
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            sqlUtils.eq("materialtype", "1");
            sqlUtils.nq("certcatalogid", dataBean.getCertcatalogid());
            //如果重复，需要再次生成直到唯一
            while(iCertCatalog.getCertCatalogCount(sqlUtils.getMap())>0){
                dataBean.setCertificatetypecode(iCertCatalog.generateCertTyCertcatcode(dataBean.getCertificatedefineauthoritycode()));
                sqlUtils.eq("certificatetypecode", dataBean.getCertificatetypecode());
            }
        }

        // 是否开启目录审核
        if (CertConstant.CONSTANT_STR_ONE.equals(configValue)) { // 启用目录审核
            // 目录是否存在已上报(待审核)
            SqlUtils sqlUtils = new SqlUtils();
            sqlUtils.eq("certcatalogid", dataBean.getCertcatalogid());
            sqlUtils.eq("auditstatus", CertConstant.CERT_CHECK_STATUS_WAIT_CHECK);
            sqlUtils.eq("ishistory", CertConstant.CONSTANT_STR_NEGATIVE_ONE);
            int waitAuditCount = iCertCatalog.getCertCatalogCount(sqlUtils.getMap());
            if (waitAuditCount > 0) {
                addCallbackParam("canChange", "0");
                addCallbackParam("msg", "该证照类型已上报!");
                return;
            }

            // 审核状态为待审核
            dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_WAIT_CHECK);
            dataBean.setIshistory(CertConstant.CONSTANT_INT_NEGATIVE_ONE);
            //证照定义机构
            dataBean.setCertificatedefineauthorityname(codeItemsService.getItemTextByCodeName("证照定义机构", dataBean.getCertificatedefineauthoritycode()));
            iCertCatalog.updateCatalog(dataBean);
            
            // 根据版本记录日志
            if (dataBean.getVersion() == 1) {
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                        CertConstant.LOG_OPERATETYPE_ADD_REPORT, "", null);
            } else {
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                        CertConstant.LOG_OPERATETYPE_CHANGE_REPORT, "", null);
            }

            FrameRole frameRole = roleService.getRoleByRoleField("rolename", "目录审核员");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                // 2、获取该角色的对应的人员
                List<FrameUser> listUser = userService.listUserByOuGuid("", roleguid, "", "", false, true, false, 3);
                if (listUser != null && listUser.size() > 0) {
                    // 3、发送待办给审核人员
                    for (FrameUser frameUser : listUser) {
                        String title = "【证照类型审核】" + dataBean.getCertname();
                        // 处理页面
                        String handleurl = "epointcert/certcatalog/areacertcatalog/catalogaudit/certcatalogaudit?guid=" + dataBean.getRowguid();
                        String messageItemGuid = UUID.randomUUID().toString();
                        messagesService.insertWaitHandleMessage(messageItemGuid, title,
                                IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                                frameUser.getDisplayName(), userSession.getUserGuid(), userSession.getDisplayName(), "证照类型审核",
                                handleurl, frameUser.getOuGuid(), "", CertConstant.CONSTANT_INT_ONE, "", "",
                                dataBean.getRowguid(), "", new Date(), "", frameUser.getUserGuid(), "", "");
                    }
                }
            }
            
            addCallbackParam("canChange", "1");
            addCallbackParam("msg", "上报成功！");
        } else {
            /*if(StringUtil.isNotBlank(dataBean.getParentid())){
                CertCatalog parentCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(dataBean.getParentid());
                if(parentCatalog==null){
                    addCallbackParam("msg", "父证照类型未启用，请先启用！");
                    return;
                }
            }*/
            // 设置为归档状态
            dataBean.setAuditstatus(CertConstant.CERT_CHECK_STATUS_PASS);
            dataBean.setIshistory(CertConstant.CONSTANT_INT_ZERO);
            // 老版本变为历史
            CertCatalog oldCatalog = iCertCatalog.getLatestCatalogDetailByCatalogid(dataBean.getCertcatalogid());
            if (oldCatalog != null) {
                oldCatalog.setIshistory(CertConstant.CONSTANT_INT_ONE);
                iCertCatalog.updateCatalog(oldCatalog);
            }

            iCertCatalog.updateCatalog(dataBean);
            // 删除演示拓展信息
            certcatalogBizlogic.deleteDemoExtension(dataBean.getRowguid());

            // 部门目录更新为未同步
            iCertCatalogOu.updateToUnpublished(dataBean.getCertcatalogid());
            if(StringUtil.isNotBlank(dataBean.getParentid())){
                CertCatalog catalog = iCertCatalog.getLatestCatalogDetailByEnale(dataBean.getParentid() , CertConstant.CONSTANT_INT_ZERO);
                if(catalog != null && CertConstant.CONSTANT_INT_ZERO.equals(catalog.getIsenable())){
                    catalog.setIsenable(CertConstant.CONSTANT_INT_ONE);
                    iCertCatalog.updateCatalog(catalog);
                }
            }

            // 根据版本记录日志
            if (dataBean.getVersion() == 1) {
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                        CertConstant.LOG_OPERATETYPE_ADD, "", null);
            } else {
                certLogBizlogic.addInternalLog(userSession.getUserGuid(), userSession.getDisplayName(), dataBean,
                        CertConstant.LOG_OPERATETYPE_MODIFY, "", null);
            }
            addCallbackParam("canChange", "1");
            addCallbackParam("msg", "归档成功！");

        }

    }

    /**
     *  检测元数据配置（元数据预览时）
     */
    public void checkMetaDataConfigByCatalogguid() {
        if (dataBean == null || dataBean.isEmpty()) {
            return;
        }
        String msg = checkMetaDataWhenReview(dataBean);
        if (StringUtil.isNotBlank(msg)) {
            addCallbackParam("msg", msg);
        }
        Map<String, Object> filter = new HashMap<>();
        filter.put("catalogguid", dataBean.getRowguid());
        Record metadata = noSQLSevice.find(CertConstant.SQL_TABLE_DEMO_EXTENSION, filter, false);
        boolean isAdd = false;
        if (metadata == null) {
            isAdd = true;
        }
        addCallbackParam("isAdd", isAdd);

    }

    /**
     *  检测元数据配置（元数据预览时）
     *  @param certCatalog
     *  @return 没有错误信息返回null
     */
    private String checkMetaDataWhenReview(CertCatalog certCatalog) {
        // 检测元数据配置(中英文是否重复，只检测顶层的)
        Set<String> chineseNameSet = new HashSet<>();
        Set<String> englishNameSet = new HashSet<>();
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certCatalog.getRowguid());
        if (ValidateUtil.isBlankCollection(metadataList)) {
            return "请先配置业务信息！";
        }
        for (CertMetadata metadata : metadataList) {
            chineseNameSet.add(metadata.getFieldchinesename());
            englishNameSet.add(metadata.getFieldname());
            if(CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())){
                if(iCertMetaData.selectSubDispinListByCertguid(metadata.getRowguid()).size() == 0){
                    return "业务信息存在未配置子表，请完善！";
                }
            }
        }
        if (chineseNameSet.size() != metadataList.size() || englishNameSet.size() != metadataList.size()) {
            return "业务信息配置有误，请先完善！";
        }
        return null;
    }

    /**
     *  检测元数据配置（归档时）
     *  @param certCatalog
     *  @return 没有错误信息返回null
     */
    private String checkMetaDataConfig(CertCatalog certCatalog) {
        // 检测元数据配置(中英文是否重复，只检测顶层的)
        Set<String> chineseNameSet = new HashSet<>();
        Set<String> englishNameSet = new HashSet<>();
        List<CertMetadata> metadataList = iCertMetaData.selectTopDispinListByCertguid(certCatalog.getRowguid());
        if (ValidateUtil.isNotBlankCollection(metadataList)) {
            for (CertMetadata metadata : metadataList) {
                chineseNameSet.add(metadata.getFieldchinesename());
                englishNameSet.add(metadata.getFieldname());
                if(CertConstant.CONSTANT_STR_ONE.equals(metadata.getIsrelatesubtable())){
                    if(iCertMetaData.selectSubDispinListByCertguid(metadata.getRowguid()).size() == 0){
                        return "业务信息存在未配置子表，请完善！";
                    }
                }
            }
            if (chineseNameSet.size() != metadataList.size() || englishNameSet.size() != metadataList.size()) {
                return "业务信息配置有误，请完善后归档！";
            }
        }
        return null;
    }

    /**
     * 证照模板（正本）上传
     * @return 上传model
     */
    public FileUploadModel9 getTempletFileUploadModel() {
        if (templetFileUploadModel == null) {
            templetFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getTempletcliengguid(), "证照模板文件");
        }
        return templetFileUploadModel;
    }

    /**
     * 证照模板（副本）上传
     * @return 上传model
     */
    public FileUploadModel9 getCopyCertFileUploadModel() {
        if (copyCertFileUploadModel == null) {
            if (StringUtil.isBlank(dataBean.getCopytempletcliengguid())) {
                dataBean.setCopytempletcliengguid(UUID.randomUUID().toString());
                iCertCatalog.updateCatalog(dataBean);
            }
            copyCertFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getCopytempletcliengguid(), "证照模板文件（副本）");
        }
        return copyCertFileUploadModel;
    }
    
    public FileUploadModel9 getTdCertFileUploadModel() {
        if (tdcertFileUploadModel == null) {
            if (StringUtil.isBlank(dataBean.getTdTempletcliengguid())) {
                dataBean.setTdTempletcliengguid(tdtempletcliengguid);
                iCertCatalog.updateCatalog(dataBean);
            }
            tdcertFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getTdTempletcliengguid(), "套打证照模板文件");
        }
        return tdcertFileUploadModel;
    }
    
    public FileUploadModel9 getTdCopyCertFileUploadModel() {
        if (tdcopyCertFileUploadModel == null) {
            if (StringUtil.isBlank(dataBean.getTdCopytempletcliengguid())) {
                dataBean.setTdCopytempletcliengguid(tdcopytempletcliengguid);
                iCertCatalog.updateCatalog(dataBean);
            }
            tdcopyCertFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getTdCopytempletcliengguid(), "套打证照模板文件（副本）");
        }
        return tdcopyCertFileUploadModel;
    }
    
    public FileUploadModel9 getColourFileUploadModel() {
        if (colourFileUploadModel == null) {
            colourFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getColourcliengguid(), "彩色封面底图");
        }
        return colourFileUploadModel;
    }

    public FileUploadModel9 getGrayFileUploadModel() {
        if (grayFileUploadModel == null) {
            grayFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getGraycliengguid(), "灰色封面底图");
        }
        return grayFileUploadModel;
    }

    /**
     *  空白底图model
     *  @return
     */
    public FileUploadModel9 getBlankFileUploadModel() {
        if (blankFileUploadModel == null) {
            // 初始化空白底图guid
            if (StringUtil.isBlank(dataBean.getBlankcliengguid())) {
                dataBean.setBlankcliengguid(UUID.randomUUID().toString());
                iCertCatalog.updateCatalog(dataBean);
            }
            blankFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getBlankcliengguid(), "空白底图");
        }
        return blankFileUploadModel;
    }

    /**
     *  示例底图model
     *  @return
     */
    public FileUploadModel9 getExampleFileUploadModel() {
        if (exampleFileUploadModel == null) {
            // 初始化示例底图guid
            if (StringUtil.isBlank(dataBean.getExamplecliengguid())) {
                dataBean.setExamplecliengguid(UUID.randomUUID().toString());
                iCertCatalog.updateCatalog(dataBean);
            }
            exampleFileUploadModel = attachBizlogic.miniOaUploadAttach(dataBean.getExamplecliengguid(), "示例底图");
        }
        return exampleFileUploadModel;
    }

    public List<SelectItem> getAuthoritycodeModel() {
        if (authoritycodeModel == null) {
            authoritycodeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照定义机构", null, false));
        }
        return this.authoritycodeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getbelongtypeModel() {
        if (belongtypeModel == null) {
            belongtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照持有人类型", null, false));
        }
        return this.belongtypeModel;
    }

    public void checkOrgcode(String ouguid){
        boolean hasOrgcode = false;
        FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(ouguid);
        if(ouExtendInfo!=null && StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))){
            hasOrgcode = true;
        }
        addCallbackParam("hasOrgcode", hasOrgcode);
    }

    public TreeModel getTreeModel() {
        if (treeModel == null) {
            int tableName = ConstValue9.FRAMEOU;
            treeModel = new LazyTreeModal9(new EpointTreeHandler9(tableName));
            treeModel.setRootName("所有部门");
        }
        return treeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getindustryModel() {
        if (industryModel == null) {
            industryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "证照所属行业", null, false));
        }
        return this.industryModel;
    }

    public List<SelectItem> getKindModel() {
        if (kindModel == null) {
            kindModel = new ArrayList<>();
            if("001".equals(dataBean.getBelongtype()) || "002".equals(dataBean.getBelongtype())){
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", dataBean.getBelongtype(), false);
            }else{
                kindModel = codeBizlogic.getSelectedItemsListByPrefix("证照分类", "003", false);
            }
        }
        return this.kindModel;
    }

    public List<SelectItem> getLevelModel() {
        if (levelModel == null) {
            levelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "机构级别", null, false));
        }
        return this.levelModel;
    }
    public List<SelectItem> getPushcerttypeModel() {
        if (pushcerttypeModel == null) {
            pushcerttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "推送证照类型", null, false));
        }
        return pushcerttypeModel;
    }


    public List<SelectItem> getIssuingLevelModel() {
        if (issuingLevelModel == null) {
            issuingLevelModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "机构级别", null, false));
        }
        return this.issuingLevelModel;
    }

    public List<SelectItem> getParentModel() {
        if (parentModel == null) {
            parentModel = new ArrayList<>();
            if (StringUtil.isNotBlank(dataBean.getIndustry())) {
                List<CertCatalog> certCatalogs = iCertCatalog.selectParentCatalog("certname,certcatalogid",
                        dataBean.getIndustry());
                if (ValidateUtil.isBlankCollection(certCatalogs)) {
                    return new ArrayList<>();
                }
                else {
                    for (CertCatalog certCatalog : certCatalogs) {
                        parentModel.add(new SelectItem(certCatalog.getCertcatalogid(), certCatalog.getCertname()));
                    }
                }
            }
        }
        return this.parentModel;
    }


    public CertCatalog getDataBean() {
        return dataBean;
    }

    public void setDataBean(CertCatalog dataBean) {
        this.dataBean = dataBean;
    }

	public String getYyyzcertcode() {
		return yyyzcertcode;
	}

	public void setYyyzcertcode(String yyyzcertcode) {
		this.yyyzcertcode = yyyzcertcode;
	}

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    public String getPushcerttypeStr() {
        return pushcerttypeStr;
    }

    public void setPushcerttypeStr(String pushcerttypeStr) {
        this.pushcerttypeStr = pushcerttypeStr;
    }
}
