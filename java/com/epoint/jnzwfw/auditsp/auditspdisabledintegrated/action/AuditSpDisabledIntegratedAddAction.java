package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.core.utils.container.ContainerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.dto.model.TreeModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.EnhancedCodeModelFactory;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfo;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfoDetail;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.CjrApiConfig;

/**
 * 套餐式开公司申报页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("auditspdisabledintegratedaddaction")
@Scope("request")
public class AuditSpDisabledIntegratedAddAction extends BaseController
{
    /**
     *
     */
    private static final long serialVersionUID = 6392947842422492650L;
    /**
     * API
     */
    @Autowired
    private IAuditYjsCjrService iAuditYjsCjrService;

    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IWebUploaderService webupservice;

    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IConfigService configServicce;

    /**
     * 网上办件实例API
     */
    @Autowired
    private IAuditProject iAuditProject;

    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;
    /**
     * 阶段事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
    /**
     * 材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IHandleProject ihandleproject;

    @Autowired
    private IAuditSpElementService iauditspelementservice;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IAuditSpSelectedOptionService iauditspselectedoptionservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    /**
     * 申请残疾类别下拉列表model
     */
    private List<SelectItem> deformity_typeModel = null;

    /**
     * 申请残疾类别下拉列表model
     */
    private List<SelectItem> accountbank_typeModel = null;
    /**
     * 文化程度下拉列表model
     */
    private List<SelectItem> educationModel = null;
    /**
     * 户籍性质下拉列表model
     */
    private List<SelectItem> hjxzModel = null;
    /**
     * 婚姻状况下拉列表model
     */
    private List<SelectItem> maritalModel = null;
    /**
     * 行政区域code下拉列表model
     */
    private TreeModel nodeidModel = null;
    /**
     * 与本人关系下拉列表model
     */
    private List<SelectItem> relationModel = null;
    /**
     * 民族下拉列表model
     */
    private List<SelectItem> nationModel = null;
    /**
     * 户口性质下拉列表model
     */
    private List<SelectItem> residenceModel = null;
    /**
     * 性别下拉列表model
     */
    private List<SelectItem> sexModel = null;

    private List<SelectItem> districtModel = null;
    private List<SelectItem> townModel = null;
    private List<SelectItem> villageModel = null;
    private List<SelectItem> hasGuardModel = null;

    /**
     * 出生一件事实体对象
     */
    private AuditYjsCjr dataBean = null;
    /**
     * 主题标识
     */
    private String businessGuid = "";
    /**
     * 主题实例标识
     */
    private String biGuid = "";

    private String cjrusername;
    private String cjrpassword;
    private String cjrverycode;
    private String mzusername;
    private String mzpassword;
    private String mzverycode;

    private String subappGuid = "";

    /**
     * 套餐式开公司API
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;

    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    @Override
    public void pageLoad() {
        businessGuid = getRequestParameter("businessGuid");
        biGuid = getRequestParameter("biGuid");

        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();

        if (auditSpBusiness != null) {
            addCallbackParam("newformid", auditSpBusiness.get("yjsformid")); //
            addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
            addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
        }

        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        if (auditSpInstance != null) {
            dataBean = iAuditYjsCjrService.find(auditSpInstance.getYewuguid());
        }
        else {
            dataBean = new AuditYjsCjr();
        }
        addCallbackParam("userguid", userSession.getUserGuid());
        com.epoint.core.utils.sql.SqlConditionUtil sqlCondition = new com.epoint.core.utils.sql.SqlConditionUtil();
        sqlCondition.eq("userguid", userSession.getUserGuid());
        List<CjrApiConfig> list = iAuditYjsCjrService.findList(sqlCondition.getMap(), CjrApiConfig.class);
        if (list != null && !list.isEmpty()) {
            for (CjrApiConfig config : list) {
                String type = "";
                if ("1".equals(config.get("type"))) {
                    type = "cjr";
                }
                else {
                    type = "mz";
                }
                addCallbackParam(type + "username", config.getAccountName());
                addCallbackParam(type + "password", config.getPassword());
            }
        }
    }

    /**
     * 残疾人验证码获取
     */
    public void getVeryCode() {
        addCallbackParam("img", CjrApiUtil.getVeryCode());

    }

    /**
     * 验证码获取
     */
    public void getVeryCodeMz() {
        addCallbackParam("img", MzApiUtil.getVeryCode());
        // com.epoint.core.utils.sql.SqlConditionUtil sqlCondition = new
        // com.epoint.core.utils.sql.SqlConditionUtil();
        // sqlCondition.eq("userguid", userSession.getUserGuid());
        // List<CjrApiConfig> list =
        // iAuditYjsCjrService.findList(sqlCondition.getMap(),
        // CjrApiConfig.class);
        // if (list != null && !list.isEmpty()) {
        // CjrApiConfig config = list.get(0);
        // addCallbackParam("username", config.getAccountName());
        // addCallbackParam("password", config.getPassword());
        // }
    }

    public void cjrlogin() throws UnsupportedEncodingException {
        String username = cjrusername;
        String password = cjrpassword;
        String verycode = cjrverycode;
        String dx_verifcode = dataBean.getStr("dx_verifcode");

        CjrApiUtil cjrApiUtil = new CjrApiUtil(username, password, verycode, dx_verifcode);
        // 1、尝试登录
        JSONObject loginResult = cjrApiUtil.login();
        if (loginResult != null) {
            String rtnCode = loginResult.getString("rtnCode");
            // 1.1、若登录失败，需要短信验证
            if ("999999".equals(rtnCode)) {
                String msg = loginResult.getJSONObject("data").getString("param");
                if (msg != null) {
                    boolean isdx = msg.contains("需要核验");
                    if (isdx) {
                        addCallbackParam("dxmsg", msg);
                    }
                    else {
                        addCallbackParam("msg", msg);
                    }
                }
            }
        }
        else {
            addCallbackParam("msg", "登录失败，请联系管理员！");
        }

        // mzlogin();
    }

    public void mzlogin() {
        String username = mzusername;
        String password = mzpassword;
        String verycode = mzverycode;
        String dx_verifcode = dataBean.getStr("dx_verifcode");

        MzApiUtil mzApiUtil = new MzApiUtil(username, password, verycode, dx_verifcode);
        // 1、尝试登录
        JSONObject loginResult = mzApiUtil.login();
        if (loginResult != null) {
            String rtnCode = loginResult.getString("rtnCode");
            // 1.1、若登录失败，需要短信验证
            if (!"000000".equals(rtnCode)) {
                String msg = loginResult.getJSONObject("data").getString("param");
                addCallbackParam("msg", msg);
            }
        }
        else {
            addCallbackParam("msg", "登录失败，请联系管理员！");
        }
    }

    /**
     * 发送短信
     * 
     * @throws UnsupportedEncodingException
     */
    public void sendSms() throws UnsupportedEncodingException {
        JSONObject smsResult = CjrApiUtil.sendSms();
        if ("000000".equals(smsResult.getString("rtnCode"))) {
            /*
             * JSONObject sendResult =
             * CjrApiUtil.Smslogin(dataBean.getStr("dx_verifcode"));
             * if ("true".equals(sendResult.getString("issuccess"))) {
             * addCallbackParam("msg", "验证码发送成功！");
             * addCallbackParam("issuccess", true);
             * }else {
             * addCallbackParam("msg", "验证码登录失败！");
             * addCallbackParam("issuccess", false);
             * }
             */

            addCallbackParam("msg", "验证码发送成功！");
            addCallbackParam("issuccess", true);

        }
        else {
            addCallbackParam("msg", "验证码发送失败,请稍后再试！");
        }
    }

    /**
     * 短信登录
     * 
     * @throws UnsupportedEncodingException
     */
    public void Smslogin(String verycode) throws UnsupportedEncodingException {
        String username = cjrusername;
        String password = cjrpassword;
        String cjverycode = cjrverycode;
        String dx_verifcode = dataBean.getStr("dx_verifcode");
        CjrApiUtil cjrApiUtil = new CjrApiUtil(username, password, cjverycode, dx_verifcode);
        JSONObject sendResult = cjrApiUtil.Smslogin(verycode);
        if ("true".equals(sendResult.getString("issuccess"))) {
            addCallbackParam("msg", "登录成功！");
            addCallbackParam("issuccess", true);
        }
        else {
            addCallbackParam("msg", "登录失败！");
            addCallbackParam("issuccess", false);
        }
    }

    /**
     * 保存基本信息
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        iAuditYjsCjrService.insert(dataBean);
        this.addCallbackParam("msg", "保存成功！");
    }

    /**
     * 登记基本信息
     */
    public void addComplete() {
        // 1、保存基本信息
        String yewuGuid = UUID.randomUUID().toString();
        dataBean.setRowguid(yewuGuid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());

        AuditSpIntegratedCompany auditSpIntegratedCompany = new AuditSpIntegratedCompany();
        auditSpIntegratedCompany.setRowguid(yewuGuid);
        auditSpIntegratedCompany.setOperatedate(new Date());
        auditSpIntegratedCompany.setOperateusername(userSession.getDisplayName());
        iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(auditSpIntegratedCompany);

        // 2、初始化实例信息
        // 2.1、生成主题实例信息
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String biguid = iAuditSpInstance.addBusinessInstance(businessGuid, yewuGuid, dataBean.getRowguid(),
                dataBean.getName(), "", auditSpBusiness.getBusinessname(), ZwfwUserSession.getInstance().getAreaCode(),
                auditSpBusiness.getBusinesstype()).getResult();

        String phaseGuid = "";
        List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
        if (listPhase != null && !listPhase.isEmpty()) {
            phaseGuid = listPhase.get(0).getRowguid();
            // 2.2、生成子申报信息
            String subappGuid = iAuditSpISubapp.addSubapp(businessGuid, biguid, phaseGuid, dataBean.getName(),
                    dataBean.getName(), dataBean.getRowguid(), ZwfwConstant.APPLY_WAY_CKDJ).getResult();

            dataBean.set("subappguid", subappGuid);

            iAuditYjsCjrService.insert(dataBean);
            /*
             * // 2.3、生成事项实例信息
             * List<AuditSpTask> auditSpTasks =
             * iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
             * for (AuditSpTask auditSpTask : auditSpTasks) {
             * Record auditTask =
             * iaudittask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).
             * getResult();
             * iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid,
             * auditTask.get("rowguid"), auditTask.get("taskname"), subappGuid,
             * auditTask.get("ordernum") == null ? 0 :
             * auditTask.get("ordernum"), auditTask.get("areacode"));
             * }
             * 
             * // 2.4、生成材料实例信息
             * iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid,
             * biGuid, phaseGuid, "", "");
             */
            boolean ishaselement = iauditspelementservice.checkByBusinessguid(businessGuid).getResult();
            if (ishaselement) {
                this.addCallbackParam("flag", "2");
            }
            else {
                // 2.3、生成事项实例信息
                List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
                SqlConditionUtil sqlc = new SqlConditionUtil();
                for (AuditSpTask auditSpTask : auditSpTasks) {
                    sqlc.clear();
                    // 套餐去掉标准事项的配置，判断下是否存在标准事项
                    if (StringUtil.isBlank(auditSpTask.getBasetaskguid())) {
                        AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).getResult();
                        iAuditSpITask.addTaskInstance(businessGuid, biguid, phaseGuid, auditTask.getRowguid(),
                                auditTask.getTaskname(), subappGuid,
                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode());
                    }
                    else {
                        sqlc.eq("basetaskguid", auditSpTask.getBasetaskguid());
                        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                        List<AuditSpBasetaskR> listr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap())
                                .getResult();
                        for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                            AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            iAuditSpITask.addTaskInstance(businessGuid, biguid, phaseGuid, auditTask.getRowguid(),
                                    auditTask.getTaskname(), subappGuid,
                                    auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(),
                                    auditTask.getAreacode());
                        }
                    }
                }
                // 2.4、生成材料实例信息
                iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid, biguid, phaseGuid, "", "");
                this.addCallbackParam("flag", "1");
            }
            this.addCallbackParam("msg", "登记成功！");
            this.addCallbackParam("subappGuid", subappGuid);
            this.addCallbackParam("biGuid", biguid);
            this.addCallbackParam("phaseGuid", phaseGuid);
            this.addCallbackParam("businessGuid", businessGuid);
        }
        else {
            this.addCallbackParam("msg", "请先配置主题阶段！");
        }

    }

    /**
     * 初始化材料列表
     *
     * @param subappGuid
     * @param biGuid
     * @param phaseGuid
     * @return
     */
    @SuppressWarnings("unused")
    public String modelAuditMaterial(String subappGuid, String biGuid, String phaseGuid) {
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
        String initMaterial = auditSpISubapp.getInitmaterial(); // 是否初始化材料
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {
            listMaterial = iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "")
                    .getResult();
        }
        else {
            listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
        }
        JSONObject jsonMaterials = new JSONObject();
        try {
            // 这里需要定义几个变量
            int submitedMaterialCount = 0;
            int shouldPaperCount = 0;
            int submitedPaperCount = 0;
            int shouldAttachCount = 0;
            int submitedAttachCount = 0;
            int rongqueCount = 0;
            int buRongqueCount = 0;
            jsonMaterials.put("total", listMaterial != null ? listMaterial.size() : 0);

            if (listMaterial != null && !listMaterial.isEmpty()) {

                for (int i = 0; i < listMaterial.size(); i++) {
                    JSONObject jsonMaterial = new JSONObject();
                    int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                    String submittype = listMaterial.get(i).get("SUBMITTYPE");
                    jsonMaterial.put("status", listMaterial.get(i).getStatus());
                    jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                    jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                    jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));

                    String is_rongque = "";
                    if ("1".equals(listMaterial.get(i).get("NECESSITY").toString())
                            || "10".equals(listMaterial.get(i).get("NECESSITY").toString())) {
                        jsonMaterial.put("isnecessity", "必需");
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).get("ALLOWRONGQUE").toString())) {
                            is_rongque = "1";
                            rongqueCount++;
                        }
                        else {
                            is_rongque = "0";
                            buRongqueCount++;
                        }
                    }
                    else {
                        jsonMaterial.put("isnecessity", "非必需");
                    }

                    jsonMaterial.put("submittype", submittype);
                    jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                    jsonMaterial.put("is_rongque", is_rongque);
                    materialList.add(jsonMaterial);
                    // 这里对材料信息进行判断
                    // 材料的提交情况
                    if (materialstatus > 10) {
                        submitedMaterialCount++;
                        if (materialstatus == 20) {
                            submitedAttachCount++;
                        }
                        else if (materialstatus == 15) {
                            submitedPaperCount++;
                        }
                        else {
                            submitedAttachCount++;
                            submitedPaperCount++;
                        }
                    }
                    // 材料的应提交情况
                    if (!"20".equals(submittype)) {
                        shouldAttachCount++;
                    }
                    if (!"10".equals(submittype)) {
                        shouldPaperCount++;
                    }
                }
            }
            jsonMaterials.put("isAllPaper", shouldPaperCount > submitedPaperCount ? 0 : 1);
            jsonMaterials.put("data", materialList);
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMaterials.toString();
    }

    /**
     * 操作纸质
     *
     * @param dataMaterials
     */
    public void paperOperate(Map<String, String> dataMaterials) {
        Map<String, String> params = getRequestParametersMap();
        String param = params.get("params");
        JSONArray jsonParam = JSONObject.parseArray(param);
        jsonParam.forEach(jsonPram -> {
            Map<String, Object> map = JsonUtil.jsonToMap(jsonPram.toString());
            String operateType = String.valueOf(map.get("operateType"));
            String materialInstanceGuid = String.valueOf(map.get("materialInstanceGuid"));
            if ("submit".equals(operateType)) {
                iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, 5);
            }
            else {
                iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, -5);
            }
        });

        // 处理返回数据
        Record rtnInfo = new Record();
        rtnInfo.put("customer", "success");
        sendRespose(JsonUtil.objectToJson(rtnInfo));
    }

    /**
     * 受理提交
     * 
     * @throws UnsupportedEncodingException
     */
    public void accpetSubmit() throws UnsupportedEncodingException {
        iAuditYjsCjrService.update(dataBean);
        CjrApiUtil instance = CjrApiUtil.getInstance(UserSession.getInstance().getUserGuid());
        log.info("获取的instance为：" + instance + ",用户id为：" + UserSession.getInstance().getUserGuid());
        if (instance != null) {
            String isAcceptSuccess = instance.acceptSubmit(dataBean);
            if (!"success".equals(isAcceptSuccess)) {
                addCallbackParam("msg", isAcceptSuccess);
            }
        }
    }

    /**
     * 收件完成操作
     *
     * @param subappGuid
     * @param biGuid
     * @param businessGuid
     */
    public void accept(String subappGuid, String biGuid, String businessGuid) {
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        auditSpInstance.setApplyername(dataBean.getName());
        iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
        iAuditYjsCjrService.update(dataBean);
        List<AuditSpITask> listTask = iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();

        // 查找情形匹配的材料
        AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice
                .getSelectedoptionsBySubappGuid(subappGuid).getResult();
        List<String> opguids = new ArrayList<>();
        List<AuditTaskOption> optionlist = new ArrayList<>();
        List<String> materialids = new ArrayList<>();
        if (auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
            JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
            List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
            for (Map<String, Object> map : jsona) {
                List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                for (Map<String, Object> map2 : maplist) {
                    opguids.add(String.valueOf(map2.get("optionguid")));
                }
            }
        }
        if (!opguids.isEmpty()) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("rowguid", StringUtil.joinSql(opguids));
            optionlist = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
        }
        // 遍历map将其中的材料添加进materialids中
        for (AuditTaskOption audittaskoption : optionlist) {
            String material = audittaskoption.getMaterialids();
            if (StringUtil.isBlank(material)) {
                continue;
            }
            String[] materials = material.split(";");
            for (String string : materials) {
                if (!materialids.contains(string)) {
                    materialids.add(string);
                }
            }
        }

        for (AuditSpITask task : listTask) {
            // 先生成办件，再初始化材料
            String projectGuid = ihandleproject.InitProject(task.getTaskguid(), "", userSession.getDisplayName(),
                    userSession.getUserGuid(), "", "", ZwfwUserSession.getInstance().getCenterGuid(), biGuid,
                    subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ).getResult();
            // 再进行材料初始化

            if (StringUtil.isNotBlank(projectGuid)) {
                iAuditSpITask.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
                // 再进行材料初始化
                if (auditspselectedoption == null) {
                    iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
                }
                else {
                    iHandleSPIMaterial.initProjctMaterialFilterM(businessGuid, subappGuid, task.getTaskguid(),
                            projectGuid, materialids);
                }
            }
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
        addCallbackParam("msg", "收件完成！");
    }

    /**
     * 推送民政接口
     */
    public void syncMz() {
        // 1、获取可用的 Util
        MzApiUtil instance = MzApiUtil.getInstance(userSession.getUserGuid());
        if (instance == null) {
            addCallbackParam("needlogin", true);
            return;
        }
        else {
            boolean loginSuccess = instance.isLoginSuccess();
            if (!loginSuccess) {
                addCallbackParam("needlogin", true);
                return;
            }
        }
        com.epoint.core.utils.sql.SqlConditionUtil sqlCondition = new com.epoint.core.utils.sql.SqlConditionUtil();
        sqlCondition.isBlankOrValue("is_push_mz", "0");
        List<AuditYjsCjr> list = iAuditYjsCjrService.findList(sqlCondition.getMap(), AuditYjsCjr.class);
        String resultt = "推送成功！";
        log.info("获取到需要推送民政的数据：" + list);
        for (AuditYjsCjr yjscjr : list) {
            boolean result = instance.acceptSubmit(yjscjr);
            if (result) {
                Record record = new Record();
                record.setSql_TableName("audit_yjs_cjr");
                record.set("is_push_mz", "1");
                record.set("rowguid", yjscjr.getRowguid());
                record.setPrimaryKeys("rowguid");
                iAuditYjsCjrService.update(record);
            }
            else {
                resultt = "推送民政数据失败!";
                continue;
            }
        }
        addCallbackParam("msg", resultt);
    }

    /**
     * 同步残联结果
     */
    public void sync() {
        // 1、获取可用的 Util
        CjrApiUtil instance = CjrApiUtil.getInstance(userSession.getUserGuid());
        if (instance == null) {
            addCallbackParam("needlogin", true);
            return;
        }
        else {
            boolean loginSuccess = instance.isLoginSuccess();
            if (!loginSuccess) {
                addCallbackParam("needlogin", true);
                return;
            }
        }

        String oldareacode = ZwfwUserSession.getInstance().getAreaCode();

        if ("370890".equals(oldareacode)) {
            oldareacode = "370899";
        }

        String areacode = oldareacode + "000000";

        log.info("同步残联结果辖区：" + areacode);
        com.epoint.core.utils.sql.SqlConditionUtil sqlCondition = new com.epoint.core.utils.sql.SqlConditionUtil();
        List<AuditYjsCjr> list = iAuditYjsCjrService.findList(areacode);

        if (list != null && !list.isEmpty()) {
            Date now = new Date();
            try {
                EpointFrameDsManager.begin(null);
                for (AuditYjsCjr auditYjsCjr : list) {
                    JSONObject cjrInfo = instance.getCjrglInfo(auditYjsCjr.getName(), auditYjsCjr.getIdcard(),
                            auditYjsCjr.getBelong_district());
                    if (cjrInfo != null) {
                        if ("000000".equals(cjrInfo.getString("rtnCode"))) {
                            log.info("获取到对应的残疾人信息，开始修改办件状态");
                            // 0、把所有相关申报、办件变为已办结
                            AuditSpInstance spInstance = iAuditSpInstance.getDetailByYeWuGuid(auditYjsCjr.getRowguid())
                                    .getResult();
                            if (spInstance != null) {
                                // 更新申报实例
                                spInstance.setStatus(ZwfwConstant.LHSP_Status_YBJ);
                                iAuditSpInstance.updateAuditSpInstance(spInstance);
                                // 更新子申报
                                List<AuditSpISubapp> spISubapps = iAuditSpISubapp
                                        .getSubappByBIGuid(spInstance.getRowguid()).getResult();
                                for (AuditSpISubapp spISubapp : spISubapps) {
                                    spISubapp.setStatus(ZwfwConstant.LHSP_Status_YBJ);
                                    spISubapp.setFinishdate(new Date());
                                    iAuditSpISubapp.updateAuditSpISubapp(spISubapp);
                                    EpointFrameDsManager.commit();
                                }
                                sqlCondition.clear();
                                sqlCondition.eq("sourceguid", spInstance.getRowguid());
                                // 更新网上办件
                                List<AuditOnlineProject> onlineProjects = iAuditYjsCjrService
                                        .findList(sqlCondition.getMap(), AuditOnlineProject.class);
                                if (onlineProjects != null && !onlineProjects.isEmpty()) {
                                    for (AuditOnlineProject onlineProject : onlineProjects) {
                                        onlineProject.setStatus(String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                                        onlineProject.setBanjiedate(now);
                                        iAuditYjsCjrService.update(onlineProject);
                                    }
                                }
                                // 更新办件
                                SqlConditionUtil sqlCondition2 = new SqlConditionUtil();
                                sqlCondition2.eq("biguid", spInstance.getRowguid());
                                List<AuditProject> auditProjects = iAuditProject
                                        .getAuditProjectListByCondition(sqlCondition2.getMap()).getResult();
                                for (AuditProject auditProject : auditProjects) {
                                    auditProject.setBanjiedate(now);
                                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);

                                    //查看有无centerguid
                                    if(StringUtils.isBlank(auditProject.getCenterguid())){
                                        AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
                                        if(auditOrgaServiceCenter!=null){
                                            auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
                                        }
                                    }
                                    //更新承诺办结时间
                                    AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
                                    if(auditTask!=null) {
                                        List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
                                        Date acceptdat = auditProject.getAcceptuserdate();
                                        Date shouldEndDate;
                                        if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                                            IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                                                    .getComponent(IAuditOrgaWorkingDay.class);
                                            shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                                                    auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                                            log.info("shouldEndDate:"+shouldEndDate);
                                        } else {
                                            shouldEndDate = null;
                                        }
                                        if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                            Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
                                            LocalDateTime currentTime = null;
                                            for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
                                                // 将Date转换为Instant
                                                Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                                                if(10==auditProjectUnusual.getOperatetype()){
                                                    // 通过Instant和系统默认时区获取LocalDateTime
                                                    currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                                }
                                                if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
                                                    LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                                                    Duration danci = Duration.between(currentTime, nextTime);
                                                    totalDuration = totalDuration.plus(danci);
                                                    currentTime = null;
                                                }
                                            }
                                            // 将累加的时间差加到初始的Date类型的shouldEndDate上
                                            Instant instant = shouldEndDate.toInstant();
                                            Instant newInstant = instant.plus(totalDuration);
                                            shouldEndDate = Date.from(newInstant);
                                            log.info("shouldEndDate:"+shouldEndDate);
                                        }
                                        if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
                                    }
                                    
                                    iAuditProject.updateProject(auditProject);
                                    EpointFrameDsManager.commit();
                                }

                            }

                            JSONObject data = cjrInfo.getJSONObject("data");
                            // 1、基本信息
                            AuditYjsCjrInfo info = new AuditYjsCjrInfo();
                            info.setRowguid(UUID.randomUUID().toString());
                            info.setCjrguid(auditYjsCjr.getRowguid());
                            info.setCjrcard(data.getString("cjrcard"));
                            info.setCjlb(data.getString("cjlb"));
                            info.setCjdj(data.getString("cjdj"));
                            info.setCjxq(data.getString("cjxq"));
                            info.setZhshrq(EpointDateUtil.convertString2Date(data.getString("zhshrq"),
                                    EpointDateUtil.DATE_FORMAT));
                            info.setScbzrq(EpointDateUtil.convertString2Date(data.getString("scbzrq"),
                                    EpointDateUtil.DATE_FORMAT));
                            info.setYxqjssj(EpointDateUtil.convertString2Date(data.getString("yxqjssj"),
                                    EpointDateUtil.DATE_FORMAT));
                            info.setYxqkssj(EpointDateUtil.convertString2Date(data.getString("yxqkssj"),
                                    EpointDateUtil.DATE_FORMAT));
                            iAuditYjsCjrService.insert(info);
                            log.info("【调试信息===推送社保】");
                            // 推送社保
                            pushSb(auditYjsCjr, info);
                            // 医保数据推送
                            pushYb(auditYjsCjr, info);
                            // 2、详细信息
                            JSONArray cjrInfoList = data.getJSONArray("list");
                            if (cjrInfoList != null && !cjrInfoList.isEmpty()) {
                                for (int i = 0; i < cjrInfoList.size(); i++) {
                                    JSONObject detailObj = cjrInfoList.getJSONObject(i);
                                    AuditYjsCjrInfoDetail detail = new AuditYjsCjrInfoDetail();
                                    detail.setRowguid(UUID.randomUUID().toString());
                                    detail.setInfoguid(info.getRowguid());
                                    detail.setCjzl(detailObj.getString("cjzl"));
                                    detail.setPdsj(EpointDateUtil.convertString2Date(detailObj.getString("pdsj"),
                                            EpointDateUtil.DATE_FORMAT));
                                    detail.setPdys(detailObj.getString("pdys"));
                                    detail.setPdyy(detailObj.getString("pdyy"));
                                    detail.setZcyy(detailObj.getString("zcyy"));
                                    detail.setCjbz(detailObj.getString("cjbz"));
                                    iAuditYjsCjrService.insert(detail);
                                }
                            }
                        }
                    }
                }
                EpointFrameDsManager.commit();
            }
            catch (Exception ex) {
                EpointFrameDsManager.rollback();
                ex.printStackTrace();
            }
            finally {
                EpointFrameDsManager.close();
            }
        }
        // 调用民政推送
        syncMz();
    }

    /**
     * 推送社保
     */
    public void pushSb(AuditYjsCjr cjr, AuditYjsCjrInfo cjrinfo) {
        Record record = new Record();
        record.setSql_TableName("person");
        record.set("xm", cjr.getName());
        record.set("sfzhm", cjr.getIdcard());
        record.set("n", getAge(cjr.getIdcard()));
        record.set("xb", cjr.getSex());
        record.set("shbzhm", cjr.getDbzh());
        record.set("cjlx", cjrinfo.getCjlb());
        record.set("cjdj", cjrinfo.getCjdj());
        String date = EpointDateUtil.convertDate2String(cjrinfo.getScbzrq(), "yyyy-MM-dd hh:mm:ss");
        record.set("ffsj", date);
        record.set("cjrzh", cjrinfo.getCjrcard());
        record.set("ffyh", cjr.getAccountbank());
        record.set("yhzh", cjr.getBankaccount());
        record.set("dbzh", cjr.getDbzh());
        record.set("lxdh", cjr.getCon_phone());
        record.set("jtzz", cjr.getResidence_area());
        webupservice.insertSbDate(record);
    }

    /**
     * 推送医保
     */
    public void pushYb(AuditYjsCjr cjr, AuditYjsCjrInfo cjrinfo) {
        Record record = new Record();
        record.setSql_TableName("person");
        record.set("xm", cjr.getName());
        record.set("sfzhm", cjr.getIdcard());
        record.set("n", getAge(cjr.getIdcard()));
        record.set("xb", cjr.getSex());
        record.set("shbzhm", cjr.getDbzh());
        record.set("cjlx", cjrinfo.getCjlb());
        record.set("cjdj", cjrinfo.getCjdj());
        String date = EpointDateUtil.convertDate2String(cjrinfo.getScbzrq(), "yyyy-MM-dd hh:mm:ss");
        record.set("ffsj", date);
        record.set("cjrzh", cjrinfo.getCjrcard());
        record.set("ffyh", cjr.getAccountbank());
        record.set("yhzh", cjr.getBankaccount());
        record.set("dbzh", cjr.getDbzh());
        record.set("lxdh", cjr.getCon_phone());
        webupservice.insertYbDate(record);
    }

    public void saveForm() {
        String msg = "保存成功";
        iAuditYjsCjrService.update(dataBean);
        addCallbackParam("msg", msg);
    }

    public AuditYjsCjr getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditYjsCjr dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getDeformity_typeModel() {
        if (deformity_typeModel == null) {
            deformity_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_残联残疾类别", null, false));
        }
        return this.deformity_typeModel;
    }

    public List<SelectItem> getAccountbankModel() {
        if (accountbank_typeModel == null) {
            accountbank_typeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_开户银行", null, false));
        }
        return this.accountbank_typeModel;
    }

    public List<SelectItem> getEducationModel() {
        if (educationModel == null) {
            educationModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_残联文化程度", null, false));
        }
        return this.educationModel;
    }

    public List<SelectItem> getHjxzModel() {
        if (hjxzModel == null) {
            hjxzModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_户籍性质", null, false));
        }
        return this.hjxzModel;
    }

    public List<SelectItem> getMaritalModel() {
        if (maritalModel == null) {
            maritalModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_婚姻状况", null, false));
        }
        return this.maritalModel;
    }

    public TreeModel getNodeidModel() {
        if (nodeidModel == null) {
            nodeidModel = CodeModalFactory.factory("下拉单选树", "残联一件事-行政区域", null, false);
        }
        return this.nodeidModel;
    }

    public List<SelectItem> getRelationModel() {
        if (relationModel == null) {
            relationModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_监护人关系", null, false));
        }
        return this.relationModel;
    }

    public List<SelectItem> getNationModel() {
        if (nationModel == null) {
            nationModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_残联民族", null, false));
        }
        return this.nationModel;
    }

    public List<SelectItem> getResidenceModel() {
        if (residenceModel == null) {
            residenceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "残联一件事_户口性质", null, false));
        }
        return this.residenceModel;
    }

    public List<SelectItem> getSexModel() {
        if (sexModel == null) {
            sexModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "性别", null, false));
        }
        return this.sexModel;
    }

    public List<SelectItem> getDistrictModel() {
        if (districtModel == null) {
            districtModel = DataUtil
                    .convertMap2ComboBox(EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, ""));
        }
        return this.districtModel;
    }

    public List<SelectItem> getTownModel() {
        String district = dataBean.getBelong_district();
        if (StringUtil.isBlank(district)) {
            return new ArrayList<>();
        }
        if (townModel == null) {
            townModel = DataUtil
                    .convertMap2ComboBox(EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, district));
        }
        return this.townModel;
    }

    public List<SelectItem> getVillageModel() {
        String town = dataBean.getBelong_town();
        if (StringUtil.isBlank(town)) {
            return new ArrayList<>();
        }
        if (villageModel == null) {
            villageModel = DataUtil
                    .convertMap2ComboBox(EnhancedCodeModelFactory.factoryList("残联一件事-行政区域", null, false, 2, town));
        }
        return this.villageModel;
    }

    public List<SelectItem> getHasGuardModel() {
        if (hasGuardModel == null) {
            hasGuardModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.hasGuardModel;
    }

    public String getCjrusername() {
        return cjrusername;
    }

    public void setCjrusername(String cjrusername) {
        this.cjrusername = cjrusername;
    }

    public String getCjrpassword() {
        return cjrpassword;
    }

    public void setCjrpassword(String cjrpassword) {
        this.cjrpassword = cjrpassword;
    }

    public String getCjrverycode() {
        return cjrverycode;
    }

    public void setCjrverycode(String cjrverycode) {
        this.cjrverycode = cjrverycode;
    }

    public String getMzusername() {
        return mzusername;
    }

    public void setMzusername(String mzusername) {
        this.mzusername = mzusername;
    }

    public String getMzpassword() {
        return mzpassword;
    }

    public void setMzpassword(String mzpassword) {
        this.mzpassword = mzpassword;
    }

    public String getMzverycode() {
        return mzverycode;
    }

    public void setMzverycode(String mzverycode) {
        this.mzverycode = mzverycode;
    }

    public String getAge(String sfz) {
        String age = "";
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        int year = Integer.valueOf(sfz.substring(6, 10));
        int month = Integer.valueOf(sfz.substring(10, 12));
        int day = Integer.valueOf(sfz.substring(12, 14));

        if ((month < monthNow) || (month == monthNow && day <= dayNow)) {
            age = String.valueOf(yearNow - year);
        }
        else {
            age = String.valueOf(yearNow - year - 1);
        }
        return age;
    }

}
